package com.bujian.aipersnonknowledge.util;

import com.bujian.aipersnonknowledge.exception.BusinessException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * JWT 工具类
 * 负责令牌的生成、验证、解析，以及从请求中提取当前登录用户ID
 * 核心用途：用户登录鉴权、身份信息传递（避免频繁查询数据库）
 */
@Component
public class JwtUtils {

    // JWT 签名密钥（必须足够复杂，建议生产环境从配置文件读取，避免硬编码）
    // 要求：至少32个字符，防止被暴力破解
    private static final String SECRET = "mySuperSecretKeyThatIsAtLeast32CharactersLong!";

    // 令牌过期时间：7200000毫秒 = 2小时（可根据业务调整，如7天、1天）
    private static final long EXPIRATION = 7200000;

    /**
     * 生成JWT令牌
     * 登录成功后调用，将用户核心信息（ID、用户名）存入令牌，返回给前端
     * @param userId 用户ID（数据库中的用户唯一标识，存入令牌用于后续身份识别）
     * @param username 用户名（存入令牌用于快速获取当前登录用户名称）
     * @return 生成的JWT令牌字符串（格式：Header.Payload.Signature）
     */
    public String generateToken(String userId, String username) {
        return Jwts.builder()
                .setSubject(username) // 设置令牌主题：存储用户名
                .claim("userId", userId) // 自定义载荷：存储用户ID（关键身份信息）
                .setIssuedAt(new Date()) // 设置令牌签发时间（用于后续校验有效性）
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION)) // 设置过期时间
                .signWith(SignatureAlgorithm.HS256, SECRET) // 使用HS256算法+密钥签名（防止令牌被篡改）
                .compact();
    }

    /**
     * 验证JWT令牌有效性
     * 校验维度：签名是否合法、令牌是否过期、格式是否正确
     * @param token 待验证的JWT令牌字符串
     * @return true=令牌有效，false=令牌无效（签名错误/已过期/格式异常）
     */
    public boolean validateToken(String token) {
        try {
            // 解析令牌并验证签名：若解析失败则抛出异常，直接返回false
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            // 捕获所有解析异常（如签名错误、令牌过期、格式非法等）
            return false;
        }
    }

    /**
     * 从JWT令牌中解析用户ID
     * 令牌验证通过后调用，提取登录时存入的用户ID（用于后续业务操作，如更新用户信息）
     * @param token 已验证有效的JWT令牌字符串
     * @return 解析出的用户ID字符串（需自行转换为对应类型，如Long）
     * @throws RuntimeException 令牌解析失败（如载荷中无userId字段、令牌格式错误）
     */
    public String getUserIdFromToken(String token) {
        try {
            // 解析令牌载荷（Claims），获取自定义存储的userId字段
            return Jwts.parser()
                    .setSigningKey(SECRET) // 指定签名密钥（与生成时一致）
                    .parseClaimsJws(token) // 解析令牌（已验证有效，此处异常概率较低）
                    .getBody() // 获取令牌载荷
                    .get("userId", String.class); // 提取userId（类型为String，与生成时一致）
        } catch (Exception e) {
            // 解析失败时抛出运行时异常，触发全局异常处理器返回统一错误响应
            throw new RuntimeException("令牌解析失败或无效：无法获取用户ID", e);
        }
    }

    /**
     * 从HTTP请求中提取当前登录用户ID
     * 整合「令牌获取、格式校验、有效性验证、用户ID解析」全流程
     * 供需要用户身份的接口调用（如修改密码、更新个人信息）
     *
     * @param request HTTP请求对象（需前端在请求头中携带令牌）
     * @return 当前登录用户的ID（Long类型，可直接用于数据库查询/更新）
     * @throws RuntimeException  未登录/令牌格式错误
     * @throws BusinessException 令牌无效/已过期（触发全局异常处理器返回401）
     */
    public Integer getCurrentUserId(HttpServletRequest request) {
        // 1. 从请求头获取Authorization字段（前端需按格式传递：Bearer + 令牌）
        String authHeader = request.getHeader("Authorization");

        // 2. 校验令牌格式：必须存在且以"Bearer "开头（空格不可省略）
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("未登录或令牌格式有误：请在Authorization头中传递 Bearer + 令牌");
        }

        // 3. 提取纯令牌字符串（去除"Bearer "前缀，长度为7）
        String token = authHeader.substring(7);

        // 4. 验证令牌有效性：无效则抛出业务异常（返回401状态码）
        if (!validateToken(token)) {
            throw new BusinessException(401, "令牌无效或已过期，请重新登录");
        }

        // 5. 解析令牌中的用户ID，并转换为Long类型（匹配数据库中用户ID字段类型）
        String userIdStr = getUserIdFromToken(token);
        return Integer.valueOf(userIdStr);
    }
}