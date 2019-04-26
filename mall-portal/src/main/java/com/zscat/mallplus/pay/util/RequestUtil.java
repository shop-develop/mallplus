//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zscat.mallplus.pay.util;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RequestUtil {
    public RequestUtil() {
    }

    public static Map<String, String> getParameterMap(HttpServletRequest request) {
        Map<String, String> retMap = new HashMap();
        Map rawMap = request.getParameterMap();
        Iterator it = rawMap.keySet().iterator();

        while(true) {
            String key;
            Object valueObj;
            do {
                if (!it.hasNext()) {
                    return retMap;
                }

                key = (String)it.next();
                valueObj = rawMap.get(key);
            } while(valueObj == null);

            String value = "";
            if (!(valueObj instanceof String[])) {
                value = (String)valueObj;
            } else {
                String[] var7 = (String[])((String[])valueObj);
                int var8 = var7.length;

                for(int var9 = 0; var9 < var8; ++var9) {
                    String v = var7[var9];
                    value = value + v + ",";
                }

                value = value.substring(0, value.length() - 1);
            }

            retMap.put(key, value);
        }
    }

    public static String getPostData(HttpServletRequest request) {
        ServletInputStream is = null;

        BufferedReader br;
        try {
            String buffer;
            try {
                is = request.getInputStream();
                if (is != null) {
                    br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    buffer = null;
                    StringBuffer sb = new StringBuffer();

                    while((buffer = br.readLine()) != null) {
                        sb.append(buffer);
                    }

                    String var5 = sb.toString();
                    return var5;
                }

                br = null;
            } catch (IOException var16) {
                buffer = null;
                return buffer;
            }
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException var15) {
                    var15.printStackTrace();
                }
            }

        }

        return null;
    }

    public static String getRemoteIp(HttpServletRequest request) {
        String rip = request.getRemoteAddr();
        String xff = request.getHeader("X-Forwarded-For");
        String ip;
        if (xff != null && xff.length() != 0) {
            int px = xff.lastIndexOf(44);
            if (px != -1) {
                ip = xff.substring(px + 1);
            } else {
                ip = xff;
            }
        } else {
            ip = rip;
        }

        return ip.trim();
    }
}
