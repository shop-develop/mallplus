//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zscat.mallplus.pay.util;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

public class JacksonUtil {
    private static Logger log = LoggerFactory.getLogger(JacksonUtil.class);
    private static ObjectMapper objectMapper = new ObjectMapper();


    public JacksonUtil() {
    }



    public static String obj2json(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException var2) {
            log.error("obj2json error. obj=" + ToStringBuilder.reflectionToString(obj), var2);
            return null;
        }
    }


    public static <T> T json2pojo(String jsonStr, Class<T> clazz) {
        try {
            return objectMapper.readValue(jsonStr, clazz);
        } catch (IOException var3) {
            log.error("json2pojo error. json=" + jsonStr + ", clazz=" + clazz, var3);
            return null;
        }
    }


    public static <T> Map<String, Object> json2map(String jsonStr) {
        try {
            return (Map)objectMapper.readValue(jsonStr, Map.class);
        } catch (IOException var2) {
            log.error("json2map error, json=" + jsonStr, var2);
            return null;
        }
    }
    public static  Map<String, String> json2maps(String jsonStr, Class clazz) {
        try {
            return (Map)objectMapper.readValue(jsonStr, clazz);
        } catch (IOException var2) {
            log.error("json2map error, json=" + jsonStr, var2);
            return null;
        }
    }
    public static <T> Map<String, T> json2map(String jsonStr, Class<T> clazz) {
        try {
            Map<String, Map<String, Object>> map = (Map)objectMapper.readValue(jsonStr, new TypeReference<Map<String, T>>() {
            });
            Map<String, T> result = new HashMap();
            Iterator var4 = map.entrySet().iterator();

            while(var4.hasNext()) {
                Entry<String, Map<String, Object>> entry = (Entry)var4.next();
                result.put(entry.getKey(), objectMapper.convertValue(entry.getValue(), clazz));
            }

            return result;
        } catch (Exception var6) {
            log.error("json2map error, json=" + jsonStr + ", clazz=" + clazz, var6);
            return null;
        }
    }

    public static <T> List<T> json2list(String jsonArrayStr, Class<T> clazz) throws Exception {
        List<Map<String, Object>> list = (List)objectMapper.readValue(jsonArrayStr, new TypeReference<List<T>>() {
        });
        List<T> result = new ArrayList();
        Iterator var4 = list.iterator();

        while(var4.hasNext()) {
            Map<String, Object> map = (Map)var4.next();
            result.add(map2pojo(map, clazz));
        }

        return result;
    }

    public static <T> T map2pojo(Map map, Class<T> clazz) {
        return objectMapper.convertValue(map, clazz);
    }



    static {
        objectMapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    }
}
