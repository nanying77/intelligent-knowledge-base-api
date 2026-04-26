package com.bujian.aipersnonknowledge.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bujian.aipersnonknowledge.entity.EmailCode;
import com.bujian.aipersnonknowledge.entity.SmsCode;
import com.bujian.aipersnonknowledge.entity.User;
import com.bujian.aipersnonknowledge.service.EmailCodeService;
import com.bujian.aipersnonknowledge.service.EmailService;
import com.bujian.aipersnonknowledge.service.ISmsCodeService;
import com.bujian.aipersnonknowledge.service.MonitorService;
import com.bujian.aipersnonknowledge.service.UserService;
import com.bujian.aipersnonknowledge.sms.Interface.SmsBlend;
import com.bujian.aipersnonknowledge.sms.SmsSender;
import com.bujian.aipersnonknowledge.sms.entity.SmsResponse;
import com.bujian.aipersnonknowledge.utils.DateUtils;
import com.bujian.aipersnonknowledge.utils.IpUtils;
import com.bujian.aipersnonknowledge.utils.JwtUtils;
import com.bujian.aipersnonknowledge.vo.Result;
import com.bujian.aipersnonknowledge.vo.UserVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Tag(name="用户管理")
@RequestMapping("base/user")
@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final ISmsCodeService ismsCodeService;
    private final EmailCodeService emailCodeService;
    private final EmailService emailService;
    private final SmsSender smsSender;
    private final MonitorService monitorService;
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
        log.info("加密后的密码：" + encryptedPassword);
        log.info("原始密码：" + user.getPassword());
        boolean isPasswordValid = encryptedPassword.equals(user.getPassword());
        log.info("密码验证结果：" + isPasswordValid);
        if (!encryptedPassword.equals(user.getPassword())) {

            return Result.error(401, "用户名或密码错误");
        }

        // 验证账号状态
        if (user.getStatus() == 0) {
            return Result.error(403, "账号已被禁用");
        }

        // 生成 JWT 令牌
        String token = jwtUtils.generateToken(user.getId().toString(), userName);
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("avatar", user.getAvatar());
        map.put("role", user.getRole());
                
        // 增加在线用户数（基于 Token 去重）
        monitorService.incrementOnlineUsers(token);
                
        return Result.success("登录成功", map);
    }

    /**
     * 用户注册
     */
   @Operation(summary= "用户注册")
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
                if(userVo.getUsername().matches("\\d+")){
                    return Result.error("用户名不能为纯数字");
                }
                User user = new User();
                user.setUsername(userVo.getUsername());
                //校验密码是否过于简单
                if (userVo.getPassword().length() < 6 ){
                    return Result.error("密码过于简单");
                } else {
                    //不允许纯数字
                    if (userVo.getPassword().matches("\\d+")||userVo.getPassword().matches("[a-zA-Z]+")) {
                        return Result.error("密码不能为纯数字或纯字母");
                    }
                }
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
        uservo.setRole(user.getRole());
        return Result.success(uservo);
    }

    /**
     * 发送验证码
     * @param phone
     */
    @Operation(summary = "发送验证码")
    @GetMapping("/sendCode")
    public Result<?> sendCode(@RequestParam("phone") String phone, HttpServletRequest request) {
        log.info("开始发送验证码，手机号：{}", phone);

        try {
            SmsBlend smsBlend = smsSender.getSmsBlend();
            if (smsBlend == null) {
                log.error("未找到可用的短信服务，请检查配置");
                return Result.error("短信服务配置错误");
            }
            log.info("获取到短信服务：{}", smsBlend.getSupplier());
        } catch (Exception e) {
            log.error("获取短信服务失败", e);
            return Result.error("短信服务不可用");
        }
        
        String ipAddr = IpUtils.getIpAddr(request);
        log.info("请求 IP: {}", ipAddr);
        
        Long sendCount = ismsCodeService.lambdaQuery()
                .eq(SmsCode::getIp, ipAddr)
                .gt(SmsCode::getExpirationTime, DateUtils.getDateDiff(-30 * 60))
                .count();
        if (sendCount > 5) {
            log.warn("IP {} 发送次数过多，已拒绝", ipAddr);
            return Result.error("发送次数过多，请稍后再试");
        }
        
        Optional<SmsCode> smsCodeOpt = ismsCodeService.lambdaQuery()
                .eq(SmsCode::getPhone, phone)
                .gt(SmsCode::getExpirationTime, new Date())
                .oneOpt();
        if (smsCodeOpt.isPresent()) {
            log.warn("手机号 {} 验证码已发送，未过期", phone);
            return Result.error("验证码已发送，请稍后再试");
        }
        
        String code = RandomStringUtils.randomNumeric(6);
        log.info("生成的验证码：{}", code);
        
        SmsCode smsCode = SmsCode.builder()
                .smsCode(code)
                .phone(phone)
                .ip(ipAddr)
                .expirationTime(DateUtils.getDateDiff(5 * 60))
                .build();
        
        boolean saved = ismsCodeService.save(smsCode);
        log.info("验证码保存到数据库：{}", saved ? "成功" : "失败");
        
        if (!saved) {
            log.error("保存验证码到数据库失败");
            return Result.error("保存验证码失败");
        }
        
        try {
            log.info("准备发送短信，手机号：{}, 验证码：{}", phone, code);
            SmsResponse response = smsSender.getSmsBlend().sendMessage(phone, code);
            log.info("短信发送响应：success={}, supplier={}, data={}", 
                    response.isSuccess(), response.getSupplier(), response.getData());
            
            if (!response.isSuccess()) {
                log.error("短信发送失败：{}", response.getData());
                return Result.error("发送失败：" + response.getData());
            }
        } catch (Exception e) {
            log.error("发送短信异常，手机号：{}, 验证码：{}", phone, code, e);
            return Result.error("发送失败：" + e.getMessage());
        }
        
        log.info("验证码发送成功，手机号：{}", phone);
        return Result.success("发送成功");
    }
    /**
     * 校验验证码
     * @param phone
     */
    @Operation(summary = "校验验证码")
    @GetMapping("/phoneVerification")
    public Result<?> phoneVerification(@RequestParam("phone") String phone, @RequestParam("smscode") String smsCode) {
        Optional<SmsCode> smsCodeOpt = ismsCodeService.lambdaQuery()
                .eq(SmsCode::getPhone, phone)
                .eq(SmsCode::getSmsCode, smsCode)
                .gt(SmsCode::getExpirationTime, new Date())
                .oneOpt();
        if (!smsCodeOpt.isPresent()) {
            return Result.error("验证码错误或已过期");
        }
        return Result.success("验证码有效");
    }

    /**
     * 发送邮箱验证码
     * @param email 邮箱地址
     * @param request HTTP 请求
     * @return 结果
     */
    @Operation(summary = "发送邮箱验证码")
    @GetMapping("/sendEmailCode")
    public Result<?> sendEmailCode(@RequestParam("email") String email, HttpServletRequest request) {
        LambdaQueryWrapper<User> query = new LambdaQueryWrapper<>();
        query.eq(User::getEmail, email);
        userService.getOne(query);
        if (userService.getOne(query) == null) {
            return Result.error("当前邮箱未关联用户");
        }
        log.info("开始发送邮箱验证码，邮箱：{}", email);

        // 验证邮箱格式
        if (!isValidEmail(email)) {
            return Result.error("邮箱格式不正确");
        }
        // 检查 IP 发送频率限制

        String ipAddr = IpUtils.getIpAddr(request);
        log.info("请求 IP: {}", ipAddr);
        
        Long sendCount = emailCodeService.lambdaQuery()
                .eq(EmailCode::getIp, ipAddr)
                .gt(EmailCode::getExpirationTime, DateUtils.getDateDiff(-30 * 60))
                .count();
        if (sendCount > 5) {
            log.warn("IP {} 发送次数过多，已拒绝", ipAddr);
            return Result.error("发送次数过多，请稍后再试");
        }
        
        // 检查该邮箱是否已有未过期的验证码
        Optional<EmailCode> emailCodeOpt = emailCodeService.lambdaQuery()
                .eq(EmailCode::getEmail, email)
                .gt(EmailCode::getExpirationTime, new Date())
                .oneOpt();
        if (emailCodeOpt.isPresent()) {
            log.warn("邮箱 {} 验证码已发送，未过期", email);
            return Result.error("验证码已发送，请稍后再试");
        }
        
        // 生成 6 位数字验证码
        String code = RandomStringUtils.randomNumeric(6);
        log.info("生成的验证码：{}", code);
        
        // 构建验证码对象
        EmailCode emailCode = EmailCode.builder()
                .emailCode(code)
                .email(email)
                .ip(ipAddr)
                .expirationTime(DateUtils.getDateDiff(5 * 60))
                .createBy("system")
                .createTime(new Date())
                .build();
        
        // 保存到数据库
        boolean saved = emailCodeService.save(emailCode);
        log.info("验证码保存到数据库：{}", saved ? "成功" : "失败");
        
        if (!saved) {
            log.error("保存验证码到数据库失败");
            return Result.error("保存验证码失败");
        }
        
        // 调用邮件服务发送邮件
        try {
            boolean sent = emailService.sendVerificationCodeMail(email, code);
            if (!sent) {
                log.error("邮件发送失败，邮箱：{}", email);
                // 删除已保存的验证码
                emailCodeService.removeById(emailCode.getId());
                return Result.error("邮件发送失败");
            }
        } catch (Exception e) {
            log.error("发送邮件异常，邮箱：{}, 验证码：{}", email, code, e);
            // 删除已保存的验证码
            emailCodeService.removeById(emailCode.getId());
            return Result.error("邮件发送异常：" + e.getMessage());
        }
        
        log.info("验证码发送成功，邮箱：{}", email);
        return Result.success("发送成功");
    }

    /**
     * 校验邮箱验证码
     * @param email 邮箱地址
     * @param emailCode 验证码
     * @return 结果
     */
    @Operation(summary = "校验邮箱验证码")
    @GetMapping("/emailVerification")
    public Result<?> emailVerification(@RequestParam("email") String email, @RequestParam("emailCode") String emailCode) {
        Optional<EmailCode> codeOpt = emailCodeService.lambdaQuery()
                .eq(EmailCode::getEmail, email)
                .eq(EmailCode::getEmailCode, emailCode)
                .gt(EmailCode::getExpirationTime, new Date())
                .oneOpt();
        if (!codeOpt.isPresent()) {
            return Result.error("验证码错误或已过期");
        }

        return Result.success("验证通过");
    }

    /**
     * 验证邮箱格式
     * @param email 邮箱地址
     * @return 是否有效
     */
    private boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        // 简单的邮箱格式验证正则"^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$
        String regex = "^[\\w-]+(\\.[\\w-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        return email.matches(regex);
    }

    /**
     * 用户退出登录
     */
    @Operation(summary = "退出登录")
    @PostMapping("/logout")
    public Result<?> logout(HttpServletRequest request) {
        try {
            // 从请求头获取 Token
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            
            // 减少在线用户数（基于 Token 去重）
            monitorService.decrementOnlineUsers(token);
            log.info("用户退出登录成功");
            return Result.success("退出成功");
        } catch (Exception e) {
            log.error("退出登录异常", e);
            return Result.error("退出失败");
        }
    }

    /**
     * 重置密码
     */
    @Operation(summary = "重置密码")
    @GetMapping("/resetPassword")
    public Result<?> resetPassword(@RequestParam("email") String email,
                                   @RequestParam("newPassword") String newPassword) {
        LambdaQueryWrapper<User> query = new LambdaQueryWrapper<>();
        query.eq(User::getEmail, email);
        User  user = userService.getOne(query);
        if (user == null) {
            return Result.error("当前邮箱未关联用户");
        }
        String md5Password = DigestUtils.md5DigestAsHex(newPassword.getBytes(StandardCharsets.UTF_8));
        user.setPassword(md5Password);
        boolean updated = userService.updateById(user);
        return updated ? Result.success("重置成功") : Result.error("重置失败");
    }
}