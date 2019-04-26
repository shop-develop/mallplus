//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zscat.mallplus.pay;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ConfigUtil {
    private static CompositeConfiguration config = new CompositeConfiguration();

    public ConfigUtil() {
    }

    public static String getString(String configXPath) {
        return config.getString(configXPath, (String)null);
    }

    public static String getString(String configXPath, String defaultValue) {
        return config.getString(configXPath, defaultValue);
    }

    public static double getDouble(String configXPath, double defaultValue) {
        return config.getDouble(configXPath, defaultValue);
    }

    public static float getFloat(String configXPath, float defaultValue) {
        return config.getFloat(configXPath, defaultValue);
    }

    public static int getInt(String configXPath, int defaultValue) {
        return config.getInt(configXPath, defaultValue);
    }

    public static long getLong(String configXPath, long defaultValue) {
        return config.getLong(configXPath, defaultValue);
    }

    public static boolean getBoolean(String configXPath, boolean defaultValue) {
        return config.getBoolean(configXPath, defaultValue);
    }

    public static List<String> getList(String configXPath) {
        List<String> values = new ArrayList();
        Iterator var2 = config.getList(configXPath).iterator();

        while(var2.hasNext()) {
            Object o = var2.next();
            values.add(o.toString());
        }

        return values;
    }

    public static void setString(String configXPath, String value) {
        config.setProperty(configXPath, value);
    }

    public static void setInt(String configXPath, int value) {
        config.setProperty(configXPath, value);
    }

    public static void setFloat(String configXPath, float value) {
        config.setProperty(configXPath, value);
    }

    public static void setLong(String configXPath, long value) {
        config.setProperty(configXPath, value);
    }

    public static void setBoolean(String configXPath, boolean value) {
        config.setProperty(configXPath, value);
    }

    public static void remove(String configXPath) {
        config.clearProperty(configXPath);
    }

    public static void add(String configXPath, Object value) {
        config.addProperty(configXPath, value);
    }

    static {
        try {
            URL commonConfigUrl = ConfigUtil.class.getResource("/common_config.properties");
            PropertiesConfiguration commonConfig = new PropertiesConfiguration(commonConfigUrl);
            URL envConfigUrl = ConfigUtil.class.getResource("/env_config.properties");
            PropertiesConfiguration envConfig = new PropertiesConfiguration(envConfigUrl);

            config.addConfiguration(commonConfig);
            config.addConfiguration(envConfig);

        } catch (ConfigurationException var8) {
            var8.printStackTrace();
        }

    }
}
