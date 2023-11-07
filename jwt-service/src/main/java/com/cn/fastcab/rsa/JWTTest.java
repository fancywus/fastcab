package com.cn.fastcab.rsa;

import com.cn.fastcab.controller.RsaJsonWebKeyUtil;
import org.jose4j.json.JsonUtil;
import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.ErrorCodes;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.lang.JoseException;

/**
 * <p>
 *
 * </p>
 *
 * @Author FancyWu
 * @Date 2022/11/30 0030
 * @Version 1.0
 */
public class JWTTest {
    public static RsaJsonWebKey rsaJsonWebKey = RsaJsonWebKeyUtil.getInstance();

    public static void main(String[] args) throws JoseException, MalformedClaimException {
        String jwt = jwtsign();
        System.out.println(jwt);
        checkJwt(jwt);
    }

    private static void checkJwt(String jwt) throws MalformedClaimException, JoseException {
        /*
         * 使用JwtConsumer builder构建适当的JwtConsumer，它将 用于验证和处理JWT。 JWT的具体验证需求是上下文相关的， 然而,
         * 通常建议需要一个（合理的）过期时间，一个受信任的时间 发行人, 以及将你的系统定义为预期接收者的受众。
         * 如果JWT也被加密，您只需要提供一个解密密钥对构建器进行解密密钥解析器。
         */
        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                .setRequireExpirationTime() //JWT必须有一个有效期时间
                .setRequireSubject() // 主题声明
                .setAllowedClockSkewInSeconds(10) // 允许在验证基于时间的令牌时留有一定的余地，以计算时钟偏差。单位/秒
                .setExpectedIssuer("Fastcab") // JWT需要由谁来发布,用来验证 发布人
                .setExpectedAudience("User") // JWT的目的是给谁, 用来验证观众
                .setVerificationKey(new RsaJsonWebKey(JsonUtil.parseJson(RsaJsonWebKeyUtil.getPubKey())).getPublicKey()) // 用公钥验证签名 ,验证秘钥
                .setJwsAlgorithmConstraints( // 只允许在给定上下文中预期的签名算法,使用指定的算法验证
                        new AlgorithmConstraints(AlgorithmConstraints.ConstraintType.PERMIT // 白名单
                        , AlgorithmIdentifiers.RSA_USING_SHA256))
                .build();

        // 验证JWT并将其处理为jwtClaims
        try {
            JwtClaims claims = jwtConsumer.processToClaims(jwt);
            System.out.println("JWT validation succeeded! " + claims);
        } catch (InvalidJwtException e) {
            //如果JWT失败的处理或验证，将会抛出InvalidJwtException。
            //希望能有一些有意义的解释（s）关于哪里出了问题。
            System.out.println("Invalid JWT! " + e);
            // 对JWT无效的（某些）特定原因的编程访问也是可能的
            // 在某些情况下，您是否需要不同的错误处理行为。
            // JWT是否已经过期是无效的一个常见原因
            if (e.hasExpired()) {
                System.out.println("JWT expired at " + e.getJwtContext().getJwtClaims().getExpirationTime());
            }
            // 或者观众是无效的
            if (e.hasErrorCode(ErrorCodes.AUDIENCE_INVALID)) {
                System.out.println("JWT had wrong audience: " + e.getJwtContext().getJwtClaims().getAudience());
            }
        }


    }

    private static String jwtsign() throws JoseException {
        // 创建claims，这将是JWT的内容 B部分
        JwtClaims claims = new JwtClaims();
        claims.setIssuer("Fastcab"); // 谁创建了令牌并签署了它
        claims.setAudience("User"); // 令牌将被发送给谁
        claims.setExpirationTimeMinutesInTheFuture(10); // 令牌失效的时间长（从现在开始10分钟）
        claims.setGeneratedJwtId(); // 令牌的唯一标识符
        claims.setIssuedAtToNow(); // 当令牌被发布/创建时（现在）生效
        claims.setNotBeforeMinutesInThePast(2); // 在此之前，令牌无效（2分钟前）
        claims.setSubject("Auth"); // 主题 ,是令牌的对象
        claims.setClaim("role", "user");

        //JsonWebSignature对象
        JsonWebSignature jws = new JsonWebSignature();

        // JWS的有效负载是JWT声明的JSON内容
        jws.setPayload(claims.toJson());

        // JWT使用私钥签署
        jws.setKey(new RsaJsonWebKey(JsonUtil.parseJson(RsaJsonWebKeyUtil.getPrKey())).getPrivateKey());

        //设置关键ID（kid）头，因为这是一种礼貌的做法。 在这个例子中，我们只有一个键但是使用键ID可以帮助 促进平稳的关键滚动过程
        jws.setKeyIdHeaderValue(rsaJsonWebKey.getKeyId());

        // jws上设置签名算法，该算法将完整性保护声明
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);

        /*
         * 签署JWS并生成紧凑的序列化或完整的jJWS 表示，它是由三个点（'.'）分隔的字符串
         * 在表单头.payload.签名中使用base64url编码的部件 如果你想对它进行加密，你可以简单地将这个jwt设置为有效负载
         * 在JsonWebEncryption对象中，并将cty（内容类型）头设置为“jwt”。
         */
        String jwt = jws.getCompactSerialization();
        return jwt;
    }
}
