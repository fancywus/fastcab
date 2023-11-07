package com.cn.fastcab.controller;


import lombok.extern.slf4j.Slf4j;
import org.jose4j.jwk.JsonWebKey;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.lang.JoseException;

import java.util.UUID;

/**
 * <p>
 *  创建jwk keyId , 公钥 ，秘钥
 * </p>
 *
 * @Author FancyWu
 * @Date 2022/11/30 0030
 * @Version 1.0
 */
@Slf4j
public class RsaJsonWebKeyUtil {
    public static RsaJsonWebKey rsaJsonWebKey = null;

    public static String publicKey = "{\"kty\":\"RSA\",\"kid\":\"43d02bb3775949d9a691f06a91e61846\",\"n\":\"gu5y2JmVj09iLcfxlZCkWtGXO3_RNRM1h4PJ1Ox-icaEsDykLztJS4-bJ7Flb8qaJa6e7KTlm210UqsV_iKN3XMyFZoU1co1lTR8--SkZDH7-INiqcN5ONcbvZF4hY5ploWrBN3Ig90QILs6ClMNHyzsTQrL50otRus15XXyCcVnQgX4N_8nnIwupHAMXJRZJyWVpcXq7vETDKVECojKtG6MxK1twXN4_BVDaVuXrTj5xyIWTKT1vPDYOmazOXggLIcimC-g7T3GQf71yUIHOwrz4XbnaU9_8V-LKdw_wvzkPJN9CpfGd_zcC0ef4W-Q_TF7MOKRMA_4d70DuGVcLQ\",\"e\":\"AQAB\"}";

    public static String privateKey = "{\"kty\":\"RSA\",\"kid\":\"43d02bb3775949d9a691f06a91e61846\",\"n\":\"gu5y2JmVj09iLcfxlZCkWtGXO3_RNRM1h4PJ1Ox-icaEsDykLztJS4-bJ7Flb8qaJa6e7KTlm210UqsV_iKN3XMyFZoU1co1lTR8--SkZDH7-INiqcN5ONcbvZF4hY5ploWrBN3Ig90QILs6ClMNHyzsTQrL50otRus15XXyCcVnQgX4N_8nnIwupHAMXJRZJyWVpcXq7vETDKVECojKtG6MxK1twXN4_BVDaVuXrTj5xyIWTKT1vPDYOmazOXggLIcimC-g7T3GQf71yUIHOwrz4XbnaU9_8V-LKdw_wvzkPJN9CpfGd_zcC0ef4W-Q_TF7MOKRMA_4d70DuGVcLQ\",\"e\":\"AQAB\",\"d\":\"XIoLwetLOa7xxUm8BenlQAtCyFrekpSxEMaksEk2ZkpZHfa-VGOiujUVhEhtWUXpDRkOWlNxtRj3TjZ9pNDnU81HaN0uPKxeksY2UBKZ3fc2D79MzI_L7dCbWrCmbj7pgemXpijav9hOvdOWghWRAKfPm-Q1mZNjzOwRe8yQ9TX9GHW3emrf8hp4JddAaofxjmbB1reKAGU5c0mVhNmyBV_UN1UBmX52pI63rM2xoiBZh6BirrhtTdcUsscb_k-sQ5Fz_BZOS7foAx6mxfyHqasm28Y7dvH1A689YBxPEEEE7uEuc7OW4cvwNnwktXQs2xRenej6cdHVfXO4hdOcQQ\",\"p\":\"w5R-1ZdXioEP_0V3q2AdwsN1G8UoGB72rfshL9YZao2qZkyWPgToxkAjAPXn10patbldh-Tn2mHv-jwrGR4mw34BWvobiVjtx-lJkwwia70caPoLEn9GkE3oq5U-YK5R6uT9Exbh0cqZYrgWLDbZS_ZZHpxynyvYVecaBjBp7as\",\"q\":\"q2Ey3QiiKYxZutIbc0fvUFGxRUbJrhIgc_c8Zz9njlDvsUMSL2Qc9Tcvj6x1cLaZUNTxJ9VUE3Nip6TEOwQsbdGkKEiC7IDpkqsWP5tp_RUg8Z6ShfabIWR62MZvzgh23G03_LdNjUZanrd9tB8swwqNaMU1LsukzbjSFvIeFYc\",\"dp\":\"b3NVe53Mku7NEOijMxOIog4D1BlbqbMrWyV6WeolfaTQ747BJwJhK2gtCkUXIoYKfvfspp34yWd5-x0CakPWTxyk5RUVUAVPKtZzyMnna4HOiLvER3wj1-OtrOlVLH7py3NGC_TEJmrPUYQZjoSK-1CcpGb-olm34-vX1qMMGhU\",\"dq\":\"eY7--eLGyvufJbEkAVfzuh_HwmCcati2piqmcIF7nWlxKmVtf6neFFypYporzt_lzXzM9LkO4qEER-7JtSaXsOlzK5OLMd-aTQsHQ-Sjf_y9mkQkn9q0XDIeJKZT5WsBjAX4IUuPOXa-jjeOdPVr1YgllzvSJjgR1E2Uzuu7RP8\",\"qi\":\"mpcUf9RhHcmalmPM4mZACkElSf6Pl3RUowJ3h2Txx5uqWal2WnFqqSd7QqVmRxPJtCtgiF5Ps9gHD6-Oi6YvTu4RvhPwBtgOHuZxSQM8ZoIobag-pVPHOwjPW7owsO8JSJkIoU4Zcrks1C8W7xHjpvNZUIxZk09MBjaZp-OcIBg\"}";

    private RsaJsonWebKeyUtil() {
    }

    public static RsaJsonWebKey getInstance() {
        String keyId = UUID.randomUUID().toString().replaceAll("-", "");
        // 生成一个RSA密钥对，用于签署和验证JWT，包装在JWK中
        if (rsaJsonWebKey == null) {
            try {
                rsaJsonWebKey = RsaJwkGenerator.generateJwk(2048);
            } catch (JoseException e) {
                e.printStackTrace();
                log.error("创建jwk keyId失败{}" + e.getMessage());
            }
            rsaJsonWebKey.setKeyId(keyId);
            publicKey = rsaJsonWebKey.toJson(RsaJsonWebKey.OutputControlLevel.PUBLIC_ONLY);
            privateKey = rsaJsonWebKey.toJson(JsonWebKey.OutputControlLevel.INCLUDE_PRIVATE);
//            System.err.println("**************: " + keyId);
//            System.err.println("**************: " + publicKey);
//            System.err.println("**************: " + privateKey);
        }
        // 给JWK一个关键ID（kid），这是礼貌的做法
        return rsaJsonWebKey;
    }

    public static String getPubKey() {
        return publicKey;
    }

    public static String getPrKey() {
        return privateKey;
    }

//    public static void main(String[] args) {
//        RsaJsonWebKey instance = getInstance();
//    }
}
