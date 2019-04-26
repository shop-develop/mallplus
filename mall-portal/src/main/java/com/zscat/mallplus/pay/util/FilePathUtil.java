//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zscat.mallplus.pay.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

public class FilePathUtil {
    private static Log log = LogFactory.getLog(FilePathUtil.class);

    public FilePathUtil() {
    }

    public static String getAbsolutePathOfWebRoot() {
        String result = null;
        result = getAbsolutePathOfClassPath();
        result = result.replace(File.separatorChar + "WEB-INF" + File.separatorChar + "classes", "");
        log.debug("Absolute Path Of WebRoot:" + result);
        return result;
    }

    public static String getAbsolutePathOfClassPath() {
        String result = null;

        try {
            File file = new File(getURLOfClassPath().toURI());
            result = file.getAbsolutePath();
            log.debug("Absolute Path Of ClassPath:" + result);
        } catch (URISyntaxException var2) {
            log.error(var2);
        }

        return result;
    }

    private static URL getURLOfClassPath() {
        return getClassLoader().getResource("");
    }

    private static ClassLoader getClassLoader() {
        log.debug("classLoader:" + Thread.currentThread().getContextClassLoader());
        return Thread.currentThread().getContextClassLoader();
    }
}
