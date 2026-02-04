package com.bujian.aipersnonknowledge.controller;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bujian.aipersnonknowledge.entity.User;
import com.bujian.aipersnonknowledge.service.UserService;
import com.bujian.aipersnonknowledge.util.JwtUtils;
import com.bujian.aipersnonknowledge.vo.Result;
import com.bujian.aipersnonknowledge.vo.UserVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
@Tag(name="用户管理")
@RequestMapping("base/user")
@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final JwtUtils jwtUtils;
    /**
     * 用户登录
     * @param userVo
     * @return
     */
    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public Result<?> login(@RequestBody UserVo userVo ) {
        String userName = userVo.getUsername();
        String passWord = userVo.getPassword();

        // 参数校验
        if (userName == null || userName.isEmpty() || passWord == null || passWord.isEmpty()) {
            return Result.error(400, "用户名或密码不能为空");
        }

        // 查询用户
        LambdaQueryWrapper<User> lambdaQuery = new LambdaQueryWrapper<>();
        lambdaQuery.eq(User::getUsername, userName);
        User user = userService.getOne(lambdaQuery);

        if (user == null) {
            return Result.error(401, "用户名不存在");
        }

        // 验证密码
        String encryptedPassword = DigestUtils.md5DigestAsHex(passWord.getBytes(StandardCharsets.UTF_8));
        if (!encryptedPassword.equals(user.getPassword())) {

            return Result.error(401, "用户名或密码错误");
        }

        // 验证账号状态
        if (user.getStatus() == 0) {
            return Result.error(403, "账号已被禁用");
        }

        // 生成JWT令牌
        String token = jwtUtils.generateToken(user.getId().toString(), userName);
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("avatar", user.getAvatar());
        return Result.success("登录成功", map);
    }

    /**
     * 用户注册
     */
    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public Result<?> register(@RequestBody UserVo userVo) {
//
        try {
            if (StringUtils.isEmpty(userVo.getUsername()) || StringUtils.isEmpty(userVo.getPassword()) || StringUtils.isEmpty(userVo.getEmail())) {
                log.warn("用户名或密码为空,请检查");
                return Result.error("注册失败,用户名和密码不能为空");
            } else {
                // 2. 检查用户是否已存在
                boolean usernameExists = userService.lambdaQuery()
                        .eq(User::getUsername, userVo.getUsername()).exists();
                if (usernameExists) {
                    return Result.error("用户已存在");
                }
                boolean eMailExists = userService.lambdaQuery().eq(User::getEmail, userVo.getEmail()).exists();
                if (eMailExists) {
                    return Result.error("邮箱已存在");
                }
                User user = new User();
                user.setUsername(userVo.getUsername());
                // 加密
                String md5Password = DigestUtils.md5DigestAsHex(userVo.getPassword().getBytes(StandardCharsets.UTF_8));
                user.setPassword(md5Password);
                user.setEmail(userVo.getEmail());
                user.setStatus(1);
                user.setUpdateTime(LocalDateTime.now());
                user.setNickname(userVo.getNickname());
//            保存到数据库中
                boolean saved = userService.save(user);
                return saved ? Result.success("注册成功") : Result.error("注册失败");
            }
        } catch (Exception e) {
            log.error("注册失败: {}", e.getMessage(), e);
            return Result.error("系统异常");
        }
    }

    /**
     * 统一用户信息修改接口
     * 支持修改密码、基本信息、头像等
     */
    @Operation(summary = "修改用户信息")
    @PostMapping("/update")
    public Result<?> update(@RequestBody UserVo userVo, HttpServletRequest request) {
        try {
            int userId = jwtUtils.getCurrentUserId(request);
            log.info("当前登录用户ID: {}, 申请修改用户信息", userId);

            //  验证用户存在性
            User user = userService.getById(userId);
            if (user == null) {
                log.warn("用户ID: {} 不存在，修改信息失败", userId);
                return Result.error("用户不存在");
            }

            //  判断修改类型并处理
            boolean isPasswordUpdate = StringUtils.hasText(userVo.getPassword()) &&
                    StringUtils.hasText(userVo.getNewPassword());
            boolean isProfileUpdate = StringUtils.hasText(userVo.getUsername()) ||
                    StringUtils.hasText(userVo.getNickname()) ||
                    StringUtils.hasText(userVo.getEmail()) ;
            boolean isAvatarUpdate = StringUtils.hasText(userVo.getAvatar());
            //  修改密码逻辑
            if (isPasswordUpdate) {
                // 验证原密码是否正确
                String encryptedOldPassword = DigestUtils.md5DigestAsHex(userVo.getPassword().getBytes(StandardCharsets.UTF_8));
                if (!encryptedOldPassword.equals(user.getPassword())) {
                    return Result.error("原密码错误，请仔细检查后再输入");
                }

                // 更新密码
                user.setPassword(DigestUtils.md5DigestAsHex(userVo.getNewPassword().getBytes(StandardCharsets.UTF_8)));
                log.info("用户ID: {} 密码修改成功", userId);
            }

            //  修改基本信息逻辑
            if (isProfileUpdate) {
                // 用户名更新及校验
                if (StringUtils.hasText(userVo.getUsername())) {
                    boolean usernameExists = userService.lambdaQuery()
                            .eq(User::getUsername, userVo.getUsername())
                            .ne(User::getId, userId)
                            .exists();
                    if (usernameExists) {
                        return Result.error("用户名已被占用");
                    }
                    user.setUsername(userVo.getUsername());
                }

                // 昵称更新
                if (StringUtils.hasText(userVo.getNickname())) {
                    user.setNickname(userVo.getNickname());
                }


                // 邮箱更新及校验
                if (StringUtils.hasText(userVo.getEmail())) {
                    boolean emailExists = userService.lambdaQuery()
                            .eq(User::getEmail, userVo.getEmail())
                            .ne(User::getId, userId)
                            .exists();
                    if (emailExists) {
                        return Result.error("邮箱已被占用");
                    }
                    user.setEmail(userVo.getEmail());
                }
            }
            if(StringUtils.hasText(userVo.getAvatar())) {
                user.setAvatar(userVo.getAvatar());
            }

            // 如果没有任何修改内容
            if (!isPasswordUpdate && !isProfileUpdate) {
                return Result.error("没有要修改的内容");
            }

            // 执行更新
            user.setUpdateTime(LocalDateTime.now());
            boolean updated = userService.updateById(user);

            if (updated) {
                String message = isPasswordUpdate ? "密码修改成功" : "个人信息修改成功";
                log.info("用户ID: {} 信息修改成功", userId);

                // 构建返回数据，不返回密码等敏感信息
                User user1 = new User();
                user1.setUsername(user.getUsername());
                user1.setNickname(user.getNickname());
                user1.setEmail(user.getEmail());
                user1.setAvatar(user.getAvatar());
                user1.setUpdateTime(LocalDateTime.now());
                return Result.success(message, user1);
            } else {
                log.warn("用户ID: {} 信息修改失败，数据库更新无效果", userId);
                return Result.error("修改失败");
            }

        } catch (Exception e) {
            log.error("用户信息修改异常，用户ID: {}", jwtUtils.getCurrentUserId(request), e);
            return Result.error("系统异常，修改失败");
        }
    }

    /**
     * 获取用户信息
     */
    @Operation(summary = "用户基本信息")
    @GetMapping("/profile")
    public Result<UserVo> getUser(HttpServletRequest request) {
//        解析token
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        int id  = Integer.parseInt(jwtUtils.getUserIdFromToken(token));
        User user = userService.getById(id);
        UserVo uservo = new UserVo();
        uservo.setEmail(user.getEmail());
        uservo.setNickname(user.getNickname());
        uservo.setUsername(user.getUsername());
        uservo.setCreateTime(user.getCreateTime());
        return Result.success(uservo);
    }
}