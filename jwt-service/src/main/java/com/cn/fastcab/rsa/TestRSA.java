package com.cn.fastcab.rsa;

import org.apache.tomcat.util.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.core.io.ClassPathResource;

import org.springframework.core.io.Resource;
import javax.crypto.Cipher;
import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * <p>
 *
 * </p>
 *
 * @Author FancyWu
 * @Date 2022/12/2 0002
 * @Version 1.0
 */
public class TestRSA {

    private static String content = "你好呀RSA";


    public static void main(String[] args) throws Exception {
        PublicKey publicKey = getPublicKey();
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] bytes = cipher.doFinal(content.getBytes());
        byte[] bytes1 = Base64.encodeBase64(bytes);
        String result = new String(bytes1,"utf-8");
        System.out.println("公钥：" + result);
        test(result);

    }

    public static void test(String en) throws Exception {
        PrivateKey privateKey = getPrivate();
        KeyFactory keyFactory1 = KeyFactory.getInstance("RSA");
        Cipher cipher = Cipher.getInstance(keyFactory1.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] bytes2 = cipher.doFinal(Base64.decodeBase64(en));
        System.out.println("私钥：" + new String(bytes2, StandardCharsets.UTF_8));
    }

    public static PrivateKey getPrivate() throws Exception {
        Resource resource = new ClassPathResource("rsa_private_key.pem");
        System.out.println(resource.getFile().getPath());
        BufferedReader br = new BufferedReader(new FileReader(resource.getFile().getPath()));
        /**
         * 这个巨重要，会筛选掉密钥内容中的首行标识字段
         */
        String s = br.readLine();
        /**
         * 这里是读取的私钥文件
         */
        String str = "";
        s = br.readLine();
        while (s.charAt(0) != '-') {
            str += s + "\r";
            s = br.readLine();
        }

        Base64 base64 = new Base64();
        byte[] b = base64.decode(str);
        /**
         * 这个也是巨重要的，不信可以去了试试，也有另一种方式去解决这个问题，没试
         */
        BouncyCastleProvider instance = getInstance();
        if (Security.getProvider(instance.getName()) == null) {
            java.security.Security.addProvider(
                    instance
            );
        }

        // 生成私匙
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(b);
        PrivateKey privateKey = kf.generatePrivate(keySpec);
        return privateKey;
    }

    public static PublicKey getPublicKey() throws Exception {
        Resource resource = new ClassPathResource("rsa_public_key.pem");
        System.out.println(resource.getFile().getPath());
        BufferedReader br = new BufferedReader(new FileReader(resource.getFile().getPath()));
        String s = br.readLine();
        /**
         * 这里是读取的公钥文件
         */
        String public_key = "";
        s = br.readLine();
        while (s.charAt(0) != '-') {
            public_key += s + "\r";
            s = br.readLine();
        }

        Base64 base64 = new Base64();
        byte[] decode = base64.decode(public_key);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decode);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    private static org.bouncycastle.jce.provider.BouncyCastleProvider bouncyCastleProvider = null;

    public static synchronized org.bouncycastle.jce.provider.BouncyCastleProvider getInstance() {
        if (bouncyCastleProvider == null) {
            bouncyCastleProvider = new org.bouncycastle.jce.provider.BouncyCastleProvider();
        }
        return bouncyCastleProvider;
    }

}
