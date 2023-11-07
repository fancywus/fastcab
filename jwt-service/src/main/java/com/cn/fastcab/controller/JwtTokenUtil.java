package com.cn.fastcab.controller;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jose4j.json.JsonUtil;
import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.NumericDate;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.lang.JoseException;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  JWT实现access token和fresh token令牌校验
 * </p>
 *
 * @Author FancyWu
 * @Date 2022/11/30 0030
 * @Version 1.0
 */
@Component
@Slf4j
public class JwtTokenUtil {

    public static RsaJsonWebKey rsaJsonWebKey = RsaJsonWebKeyUtil.getInstance();
    private static final String APPLICATION_NAME = "Fastcab";

    private static final String ACCESS_TOKEN = "access_token";
    private static final String FRESH_TOKEN = "fresh_token";

    /**
     * 生成Token令牌返回给客户端
     *
     * @param phone 用户手机号
     * @return Token令牌
     */
    public Map<String, Object> generateToken(String phone) {
        Map<String, Object> tokenMap = buildToken(phone);
        return tokenMap;
    }

    /**
     * 生成access token和fresh token的方法
     *
     * @param phone 用户手机号
     * @return 返回给tokenMap
     */
    private Map<String, Object> buildToken(String phone) {
        String accessToken = genAccessOrFreshToken(phone, 60);
        String freshToken = genAccessOrFreshToken(phone, 120);
        Map<String, Object> tokenMap = new HashMap<>();
        tokenMap.put(ACCESS_TOKEN, accessToken);
        tokenMap.put(FRESH_TOKEN, freshToken);
        System.out.println("expiration: " + YamlConfigurerUtil.getStrYmlVal("fastcab.jwt.expiration"));
        System.out.println("enabled: " + YamlConfigurerUtil.getStrYmlVal("fastcab.jwt.enabled"));
        long exp = Long.parseLong(YamlConfigurerUtil.getStrYmlVal("fastcab.jwt.expiration"));
        tokenMap.put("access_token_exp", exp);
        tokenMap.put("fresh_token_exp", exp * 2);
        return tokenMap;
    }

    /**
     * 具体创建和刷新token过程
     *
     * @param phone 用户手机号
     * @param exp 令牌的有效时间
     * @return JWT中claims的部分 其中记录用户信息、创建时间，为后面刷新令牌、判断令牌是否失效做根据
     */
    private String genAccessOrFreshToken(String phone, long exp) {
        try {
            NumericDate date = NumericDate.now();
            date.addSeconds(exp);
            // 创建claims
            final JwtClaims claims = new JwtClaims();
            claims.setIssuer(APPLICATION_NAME); // 谁创建了令牌并签署了它
            claims.setAudience("user"); // 令牌将被发送给谁
            claims.setSubject("authorization"); // 主题 ,是令牌的对象
            claims.setClaim("phone", phone);
            claims.setClaim("created", new Date());
            claims.setExpirationTime(date); //令牌失效的时间长（60分钟）
            claims.setIssuedAtToNow(); // 当令牌被发布/创建时（现在）生效

            //JsonWebSignature对象
            final JsonWebSignature jws = new JsonWebSignature();
            // JWS的有效负载是JWT声明的JSON内容
            jws.setPayload(claims.toJson());
            // JWT使用私钥签署
            jws.setKey(new RsaJsonWebKey(JsonUtil.parseJson(RsaJsonWebKeyUtil.getPrKey())).getPrivateKey());
            //设置关键ID（kid）头，我们只有一个键但是使用键ID可以帮助 促进平稳的关键滚动过程
            jws.setKeyIdHeaderValue(rsaJsonWebKey.getKeyId());
            // jws上设置签名算法，该算法将完整性保护声明
            jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);
            //返回token
            String token = jws.getCompactSerialization();
            return token;
        } catch (JoseException e) {
            log.error("创建Token{}: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 校验JWT
     * @param token 客户端携带的token令牌
     * @return 校验结果
     */
    public boolean validateToken(String token) {
        try {
            JwtConsumer jwtConsumer= new JwtConsumerBuilder()
                    .setRequireExpirationTime()
                    .setAllowedClockSkewInSeconds(10) // 允许在验证基于时间的令牌时留有一定的余地，以计算时钟偏差。单位/秒
                    .setVerificationKey(new RsaJsonWebKey(JsonUtil.parseJson(RsaJsonWebKeyUtil.getPubKey())).getPublicKey())
                    .setExpectedIssuer(APPLICATION_NAME)
                    .setExpectedAudience("user")
                    .setExpectedSubject("authorization")
                    .setJwsAlgorithmConstraints(new AlgorithmConstraints
                            (AlgorithmConstraints.ConstraintType.PERMIT // 白名单
                            , AlgorithmIdentifiers.RSA_USING_SHA256))
                    .build();

                JwtClaims jwtClaims = jwtConsumer.processToClaims(token);
                //判断claims是否为空
                if (jwtClaims != null) {
                    NumericDate expirationTime = jwtClaims.getExpirationTime();
                    String phone = (String) jwtClaims.getClaimValue("phone");
                    //判断令牌是否超时失效
                    if (!expirationTime.isBefore(NumericDate.now())) {
                        return false;
                    }
                    if (StringUtils.isBlank(phone)) {
                        return false;
                    }
                    return true;
                }
                return false;
        } catch (InvalidJwtException e) {
            log.error("InvalidJwtException{}" + e.getMessage());
            throw new RuntimeException(e);
        } catch (MalformedClaimException e) {
            log.error("MalformedClaimException{}" + e.getMessage());
            throw new RuntimeException(e);
        } catch (JoseException e) {
            log.error("JoseException{}" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 刷新令牌
     *
     * @param token 客户端传来的令牌
     * @return 新令牌
     */
    public Map<String, Object> reFreshToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parserBuilder().setSigningKey(new RsaJsonWebKey(JsonUtil.parseJson(RsaJsonWebKeyUtil.getPrKey())).getPrivateKey())
                    .build().parseClaimsJws(token).getBody();
        } catch (JoseException e) {
            log.error("JoseException{}" + e.getMessage());
            throw new RuntimeException(e);
        }
        claims.put("created", new Date());
        String phone = (String) claims.get("phone");
        Map<String, Object> tokenMap = generateToken(phone);
        return tokenMap;
    }
}
