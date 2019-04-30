package com.cxytiandi.encrypt.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

public class AesEncryptUtils {
	private static final String KEY = "d7b85f6e214abcda";
	private static final String ALGORITHMSTR = "AES/ECB/PKCS5Padding";

	public static String base64Encode(byte[] bytes) {
		return Base64.encodeBase64String(bytes);
	}

	public static byte[] base64Decode(String base64Code) throws Exception {
		return Base64.decodeBase64(base64Code);
	}

	public static byte[] aesEncryptToBytes(String content, String encryptKey) throws Exception {
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		kgen.init(128);
		Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
		cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(), "AES"));
		return cipher.doFinal(content.getBytes("utf-8"));
	}

	public static String aesEncrypt(String content, String encryptKey) throws Exception {
		return base64Encode(aesEncryptToBytes(content, encryptKey));
	}

	public static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) throws Exception {
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		kgen.init(128);
		Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
		cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(), "AES"));
		byte[] decryptBytes = cipher.doFinal(encryptBytes);
		return new String(decryptBytes, "utf-8");
	}

	public static String aesDecrypt(String encryptStr, String decryptKey) throws Exception {
		return aesDecryptByBytes(base64Decode(encryptStr), decryptKey);
	}

	public static void main(String[] args) throws Exception {
		String content = "你好";
		System.out.println("加密前：" + content);

		String encrypt = aesEncrypt(content, KEY);
		System.out.println(encrypt.length() + ":加密后：" + encrypt);

		String decrypt = aesDecrypt("2t9Flqu9yxOQOieasr6qRUzLsRxMmFrwiXNRNrup1g/dfIJn5O8zGdMABAN+/fO59iROJeSfoF7YyUUyvg+ZC4MKHhgvPrFtr75H15Xa7avGRR17n4vsyEmuB5ZjvUNIfW4HJ2PyXitwGJjiPv19EXwS9lW+YieHiCGIFwVEZ52e9V3Zy8z1buz1LUMPs04EAZKHCI1W0GDVURrVQxwBM/G5IxW0MW8hiWQXQ8/Vs3Dyav2Me5Wnu8IPWsWIdrFGx74p4ttD/GkVZAiM71lWmnJSW0NDA+38JMonkLeHmCjnbmwH2Js0+lxgxpRWXyt3X/CGzJOfgPF94u2d3WooNsZFHXufi+zISa4HlmO9Q0h9bgcnY/JeK3AYmOI+/X0RfBL2Vb5iJ4eIIYgXBURnnZ71XdnLzPVu7PUtQw+zTgQBkocIjVbQYNVRGtVDHAEzjWPa3WrlgTThWIVfKbOolPMqFly6NU5VpStFyQBrcADtGzRLjKXNzXXAQiORgXTrpwLo4ZIxQx2cr6XfKeEe5FM+VotHr7FjUQSzbzIdJGJznIcgcZnbK2HDemhHFjA7+QTHU6iBLVCV1ccwZGEk+jPuWf8fXo8NJR1NQLs1QGuuoWtMBJLYcwYX8jGKn7TyOeMUSGj1JJgfmqWJDAKq9fHRwxTBjFEFJI7gl7olksgngrxoOBoIFCMbUkFiqTTHbSBs+sBviqXEW638A6buiqVnN67xGDY1GMXhXcMK0UBdYHnkapRReCkgeHPmV4jPsZ2rOo6DXe1EFjb4ce0AQOwGXWYUDwUxxsgEhD4vI6DSZPscUMGtQ0Wy3BV9wDUWCdZ7/1kfKKLUTwAs9I/1Sm0Kb+SZhUNQE1H6TAfplT7oEUQg3ddD+CjHpNqZ8mjX6KCA6LhtqGp8BW4TVkquUTDgNUivJYwmQnQlrBfka2s4mePRJ1qRJ623ZwpqudKtA/ZxR13KnlRK6LR70QF7Qadrj7r8rR8KBy1gS8cKKFUojN9CBfNWOImXayfKidmHSaZvEYu4c48MK1Q4322G/5A/AKCRy7Mqu4qlTRVsHzFmqunY84G3dYNKSKkxeKM2p0sCw9dkVooIw4B1qWmC2w0lQphjpiOib+7JHwmPMsWgCgmDrql1PPBmO6DbwFlJs/nHWctio90mU7ZjJmQwoIoerDN3i1PMhBA/pxndZLRdU9jfiO13JK+LO6wW8iHB2XRmyEFD+sh5EY8rOJlYGNP7npClVRnC9pI44e1xIaQNPaEXM01Pui04KLKdbNul2R8k5Lb9h3twkWnnwCpLiGXlZ1J39vf1zaYFXmypZJNggaTzj12O7Q68u3z1FJbIfE3VC7Z6XbM13AkLXb4ftPCIJ6zVqIXn8kAsPyYl7Eguj9h3SA+v+Uho/pchuQX06WtMGPu0FCJl8Rg/IgJSzRJV8Q4tJBfao3XKgxeZ5xCV9ssdPXLnPOkOtk8npnnlrhPaF5uFAmopL1WAdGIrBUeVM3dVLYRGJTThdTCmP5l5IelpajfNszUxY56ZQcKp", KEY);
		System.out.println("解密后：" + decrypt);
	}
	
}