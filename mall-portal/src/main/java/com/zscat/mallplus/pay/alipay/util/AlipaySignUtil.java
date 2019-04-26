package com.zscat.mallplus.pay.alipay.util;


import com.zscat.mallplus.pay.ConfigUtil;
import com.zscat.mallplus.pay.PayMethod;
import com.zscat.mallplus.pay.exeception.BusinessException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.crypto.Cipher;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

public class AlipaySignUtil {
	
	private static Log log = LogFactory.getLog(AlipaySignUtil.class);
	
	private static final String  SIGN_ALGORITHMS = "SHA1WithRSA";
	
	public static String SIGN_TYPE_RSA = "RSA";
	public static String SIGN_TYPE_MD5 = "MD5";

	public static String sign(Map<String, String> paramMap, String charset, String signType, PayMethod paymethod) {
		if (SIGN_TYPE_RSA.equals(signType)) {
			return getRsaSign(paramMap, charset);
		} else if (SIGN_TYPE_MD5.equals(signType)) {
			return getMd5Sign(paramMap, charset, paymethod);
		} else {
			throw new BusinessException("[alipay]invalid sign type : " + signType);
		}
	}

	/**
	 * 验证签名，支持RSA和md5
	 *
	 * @param sign
	 * @param signType
	 * @param paramMap
	 * @param charset
	 * @return
	 */
	public static boolean verifySign(String sign, String signType, Map<String, String> paramMap, String charset, PayMethod paymethod) {
		if (SIGN_TYPE_RSA.equals(signType)) {
			return verifyRsaSign(sign, paramMap, charset, paymethod);
		} else if (SIGN_TYPE_MD5.equals(signType)) {
			return verifyMd5Sign(sign, paramMap, charset, paymethod);
		} else {
			throw new BusinessException("[alipay]invalid sign type : " + signType);
		}
	}

	/**
	 * RSA签名
	 *
	 * @param paramMap
	 * @param charset
	 * @return
	 */
	private static String getRsaSign(Map<String, String> paramMap, String charset) {
		Map<String, String> map = new HashMap<String, String>(paramMap);
		map.remove("sign");
		map.remove("sign_type");
		String source = getParamStr(map, false, charset);
		
		PrivateKey priKey;
		try {
			priKey = getPrivateKey();
			Signature signature = Signature.getInstance(SIGN_ALGORITHMS);

	        signature.initSign(priKey);
	        signature.update( source.getBytes(charset) );

	        byte[] signed = signature.sign();
	        String signStr = StringUtils.remove(Base64.encodeBase64String(signed), System.getProperty("line.separator"));
	        
	        log.info("[alipay-sign-rsa]source=" + source + ", sign=" + signStr);
	        return signStr;
		}
		catch (Exception e) {
			log.fatal("[alipay-sign-rsa]sign error. source=" + source, e);
		}
		return null;
	}

	/**
	 * RSA验签
	 *
	 * @param sign
	 * @param paramMap
	 * @param charset
	 * @return
	 */
	private static boolean verifyRsaSign(String sign, Map<String, String> paramMap, String charset, PayMethod paymethod) {
		Map<String, String> map = new HashMap<String, String>(paramMap);
		map.remove("sign");
		map.remove("sign_type");
		if (sign == null) {
			return false;
		}
		String source = getParamStr(map, false, charset);
		
		try
		{
			PublicKey pubKey = getPubKey(paymethod);
			Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
				
			signature.initVerify(pubKey);
			signature.update(source.getBytes(charset));
		
			boolean bverify = signature.verify(Base64.decodeBase64(sign));
			log.info("[alipay-verifySign-rsa]source=" + source + ", reqSign=" + sign + ", result=" + bverify);
			return bverify;
		}
		catch (Exception e)	{
			log.fatal("[alipay-verifySign-rsa]source=" + source + ", reqSign=" + sign, e);
		}
		return false;
	}

	/**
	 * MD5签名
	 *
	 * @param paramMap
	 * @param charset
	 * @return
	 */
	private static String getMd5Sign(Map<String, String> paramMap, String charset, PayMethod paymethod) {
		Map<String, String> map = new HashMap<String, String>(paramMap);
		map.remove("sign");
		map.remove("sign_type");
		String source = getParamStr(map, false, null) + paymethod.getApiKey();
		String sign = null;
		try {
			sign = DigestUtils.md5Hex(source.getBytes(charset));
		}
		catch (UnsupportedEncodingException e) {
		}
		log.info("[alipay-sign-ms5]source=" + source + ", sign=" + sign);
		return sign;
	}

	/**
	 * MD5验签
	 *
	 * @param sign
	 * @param paramMap
	 * @param charset
	 * @return
	 */
	private static boolean verifyMd5Sign(String sign, Map<String, String> paramMap, String charset, PayMethod paymethod) {
		if (sign == null) {
			return false;
		}
		return sign.equals(getMd5Sign(paramMap, charset, paymethod));
	}

	/**
	* 解密
	* @param content 密文
	* @return 解密后的字符串
	*/
	public static String decrypt(String content, String charset) throws Exception {
        PrivateKey prikey = getPrivateKey();

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, prikey);

        InputStream ins = new ByteArrayInputStream(Base64.decodeBase64(content));
        ByteArrayOutputStream writer = new ByteArrayOutputStream();
        //rsa解密的字节大小最多是128，将需要解密的内容，按128位拆开解密
        byte[] buf = new byte[128];
        int bufl;

        while ((bufl = ins.read(buf)) != -1) {
            byte[] block = null;

            if (buf.length == bufl) {
                block = buf;
            } else {
                block = new byte[bufl];
                for (int i = 0; i < bufl; i++) {
                    block[i] = buf[i];
                }
            }

            writer.write(cipher.doFinal(block));
        }

        return new String(writer.toByteArray(), charset);
    }

	public static String getParamStr(Map<String, String> paramMap, boolean encodeValue, String charset) {
		List<String> keyList = new ArrayList<>(paramMap.keySet());
		Collections.sort(keyList);
		StringBuilder sb = new StringBuilder();
		for (String key : keyList) {
			sb.append(key).append("=");
			try {
				String value = encodeValue ? URLEncoder.encode(paramMap.get(key), charset) : paramMap.get(key);
				//value = value.replace("+", "%20");
				sb.append(value).append("&");
			}
			catch (UnsupportedEncodingException e){}
		}
		if (sb.length() > 3) {
			sb.deleteCharAt(sb.length() - 1);
		}

		return sb.toString();
	}
	
	/**
	* 得到支付网关私钥
	* @throws Exception
	*/
	private static PrivateKey getPrivateKey() throws Exception {

		String prikey = ConfigUtil.getString("mallplus.privateKey");
		byte[] keyBytes = Base64.decodeBase64(prikey);
		
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
		
		return privateKey;
	}

	/**
	 * 获取支付宝的公钥
	 * @return
	 * @throws Exception
	 */
	private static PublicKey getPubKey(PayMethod paymethod) throws Exception {
		String pubkeyStr = paymethod.getPublicKey();
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] encodedKey = Base64.decodeBase64(pubkeyStr);
        PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
        return pubKey;
	}
	
	public static void main(String[] args) {
		String s = DigestUtils.md5Hex("Fittime1991");
		System.out.println(s);
	}
}
