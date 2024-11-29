package org.gpd.dicenetwork.secret;

import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.ec.CustomNamedCurves;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECKeyGenerationParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.signers.ECDSASigner;
import org.bouncycastle.crypto.signers.HMacDSAKCalculator;
import org.bouncycastle.math.ec.ECPoint;
import org.gpd.dicenetwork.lottery.entity.LotteryListEntity;
import org.gpd.dicenetwork.lottery.service.LotteryListService;
import org.gpd.dicenetwork.lottery.service.impl.LotteryListServiceImpl;
import org.gpd.dicenetwork.utils.DateTimeUtils;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;

public class Secp256k1Utils {

    // SECP256K1参数
    public static final X9ECParameters CURVE_PARAMS = CustomNamedCurves.getByName("secp256k1");
    public static final ECDomainParameters DOMAIN_PARAMS = new ECDomainParameters(
            CURVE_PARAMS.getCurve(), CURVE_PARAMS.getG(), CURVE_PARAMS.getN(), CURVE_PARAMS.getH()
    );

    // 生成密钥对
    public static synchronized KeyPair generateKeyPair() {
        ECKeyPairGenerator generator = new ECKeyPairGenerator();
        ECKeyGenerationParameters keyGenParams = new ECKeyGenerationParameters(DOMAIN_PARAMS, new SecureRandom());
        generator.init(keyGenParams);
        org.bouncycastle.crypto.AsymmetricCipherKeyPair keyPair = generator.generateKeyPair();
        ECPrivateKeyParameters privateKeyParams = (ECPrivateKeyParameters) keyPair.getPrivate();
        ECPublicKeyParameters publicKeyParams = (ECPublicKeyParameters) keyPair.getPublic();
        return new KeyPair(privateKeyParams.getD(), publicKeyParams.getQ());
    }

    // 签名
    public static synchronized ECDSASignature sign(BigInteger privateKey, byte[] messageHash) {
        ECDSASigner signer = new ECDSASigner(new HMacDSAKCalculator(new SHA256Digest()));
        ECPrivateKeyParameters privateKeyParams = new ECPrivateKeyParameters(privateKey, DOMAIN_PARAMS);
        signer.init(true, privateKeyParams);
        BigInteger[] components = signer.generateSignature(messageHash);

        // 对签名进行规范化
        BigInteger r = components[0];
        BigInteger s = components[1];
        BigInteger halfCurveOrder = DOMAIN_PARAMS.getN().shiftRight(1);
        if (s.compareTo(halfCurveOrder) > 0) {
            s = DOMAIN_PARAMS.getN().subtract(s);
        }

        return new ECDSASignature(r, s);
    }

    public static synchronized byte[] sha256Hex(byte[] data) {
        SHA256Digest digest = new SHA256Digest();
        digest.update(data, 0, data.length);
        byte[] hash = new byte[digest.getDigestSize()];
        digest.doFinal(hash, 0);
        return hash;
    }

    // 验签
    public static synchronized boolean verify(ECPoint publicKey, byte[] messageHash, ECDSASignature signature) {
        ECDSASigner signer = new ECDSASigner(new HMacDSAKCalculator(new SHA256Digest()));
        ECPublicKeyParameters publicKeyParams = new ECPublicKeyParameters(publicKey, DOMAIN_PARAMS);
        signer.init(false, publicKeyParams);
        return signer.verifySignature(messageHash, signature.r, signature.s);
    }

    public static synchronized boolean verifyBycompressedPublicKey(String publicKey, byte[] messageHash, ECDSASignature signature) {
        ECDSASigner signer = new ECDSASigner(new HMacDSAKCalculator(new SHA256Digest()));
        ECPublicKeyParameters publicKeyParams = new ECPublicKeyParameters(decompressPublicKey(new BigInteger(publicKey, 16).toByteArray()), DOMAIN_PARAMS);
        signer.init(false, publicKeyParams);
        return signer.verifySignature(messageHash, signature.r, signature.s);
    }

    // 导入压缩公钥
    public static synchronized ECPoint decompressPublicKey(byte[] compressedPublicKey) {
        return DOMAIN_PARAMS.getCurve().decodePoint(compressedPublicKey);
    }

    // 密钥对和签名类
    public static class KeyPair {
        public final BigInteger privateKey;
        public final ECPoint publicKey;

        public KeyPair(BigInteger privateKey, ECPoint publicKey) {
            this.privateKey = privateKey;
            this.publicKey = publicKey;
        }
    }

    public static class ECDSASignature {
        public final BigInteger r;
        public final BigInteger s;

        public ECDSASignature(BigInteger r, BigInteger s) {
            this.r = r;
            this.s = s;
        }

        public ECDSASignature(String signatureHex){
            this.r = new BigInteger(signatureHex.substring(0, 64), 16);
            this.s = new BigInteger(signatureHex.substring(64), 16);
        }
    }

    public static void main(String[] args) throws Exception {
        // 生成密钥对
        String publicKeyHex = "024dbb0517374fa524558c02aff77448bb45b09807549f471a374951bce9459ab9";
        String randomHashHex = "0x2cCE873422C71992A8F326Ba268F6705167fB40aD8:3A:DD:C2:5D:79";
        String signatureHex = "685ec12eda90d4a1c991f4454f36f0e8728f1d65d3f03fc28c614f013e0bfbc139852281124626acd50ac73b51b635f584dcaf09c3a0f8e84a461081942a1cc2";

        BigInteger publicKey = new BigInteger(publicKeyHex, 16);
//        System.out.println(signatureHex.substring(0, 32));
//        System.out.println(signatureHex.substring(32, 64));
        BigInteger signatureR = new BigInteger(signatureHex.substring(0, 64), 16);
        BigInteger signatureS = new BigInteger(signatureHex.substring(64), 16);
        // 导入公钥
        ECPoint publicKeyPoint = Secp256k1Utils.decompressPublicKey(publicKey.toByteArray());
        System.out.println(publicKeyPoint.toString());

        // 创建ECDSASignature对象
        Secp256k1Utils.ECDSASignature signature = new Secp256k1Utils.ECDSASignature(signatureR, signatureS);

        // 验证签名
        boolean isSignatureValid = Secp256k1Utils.verify(publicKeyPoint, sha256Hex(randomHashHex.getBytes()), signature);
        // 输出结果
        System.out.println("Signature is valid: " + isSignatureValid);


    }
}

