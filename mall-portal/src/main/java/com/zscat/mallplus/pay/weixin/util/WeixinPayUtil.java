package com.zscat.mallplus.pay.weixin.util;


import com.zscat.mallplus.pay.PayMethod;
import com.zscat.mallplus.pay.exeception.BusinessException;
import org.apache.commons.lang.StringUtils;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.*;

public class WeixinPayUtil {

	/**
	 * 生成指定长度的随机串
	 * 
	 * @param length
	 * @return
	 */
	public static String CreateNoncestr(int length) {
		String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		String res = "";
		Random rd = new Random();
		for (int i = 0; i < length; i++) {
			res += chars.charAt(rd.nextInt(chars.length() - 1));
		}
		return res;
	}

	/**
	 * 
	 * @param paraMap
	 * @param urlencode
	 *            是否把每个参数做urlencode ，utf-8
	 * @param valueNull
	 *            value 是空，是否做签名
	 * @return
	 * @throws
	 */
	public static String formatUnSignParaMap(Map<String, String> paraMap, boolean urlencode, boolean valueNull) {

		StringBuffer buff = new StringBuffer();
		try {
			List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(paraMap.entrySet());

			Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {
				@Override
				public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
					return (o1.getKey()).toString().compareTo(o2.getKey());
				}
			});

			for (int i = 0; i < infoIds.size(); i++) {
				Map.Entry<String, String> item = infoIds.get(i);
				if (StringUtils.isNotBlank(item.getKey())) {

					String key = item.getKey();
					String val = item.getValue();

					// value为空，是否进行排序
					if (valueNull) {
						buff.append(key);
						buff.append("=");
						if (urlencode) {
							val = urlEncode(val);
						}
						buff.append(val);
						buff.append("&");
					} else {
						if (StringUtils.isNotBlank(val)) {
							buff.append(key);
							buff.append("=");
							if (urlencode) {
								val = urlEncode(val);
							}
							buff.append(val);
							buff.append("&");
						}
					}
				}
			}

			if (buff.length() > 0) {
				buff.delete(buff.length() - 1, buff.length());
			}
		} catch (Exception e) {
			throw new BusinessException("FormatUnSignParaMap exception: " + e.getMessage());
		}
		return buff.toString();
	}

	// 特殊字符处理
	public static String urlEncode(String src) throws UnsupportedEncodingException {
		return URLEncoder.encode(src, "utf-8").replace("+", "%20");
	}

	/**
	 * 获取HTTP调用的SSLContext 微信新版用
	 *
	 * @return
	 * @throws Exception
	 */
	public static SSLContext getSSLContext(PayMethod paymethod) throws Exception {
		return getSSLContext(paymethod.getPartnerId());
	}
	
	/**
	 * 获取HTTP调用的SSLContext 微信新版用
	 *
	 * @return
	 * @throws Exception
	 */
	public static SSLContext getSSLContext(String  partnerId) throws Exception {
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		InputStream instream = WeixinPayUtil.class.getResourceAsStream("/cert/weixin/apiclient_cert_" + partnerId + ".p12");
		try {
			keyStore.load(instream, partnerId.toCharArray());
		} finally {
			instream.close();
		}

		KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		kmf.init(keyStore, partnerId.toCharArray());

		KeyManager[] kms = kmf.getKeyManagers();
		SSLContext sslContext = null;
		sslContext = SSLContext.getInstance("TLS");
		sslContext.init(kms, null, new SecureRandom());
		return sslContext;
	}

}
