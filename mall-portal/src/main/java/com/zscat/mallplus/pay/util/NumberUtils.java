//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zscat.mallplus.pay.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class NumberUtils {
    public NumberUtils() {
    }

    public static String formatBigDecimal(BigDecimal num, String pattern) {
        DecimalFormat format = new DecimalFormat(pattern);
        return format.format(num);
    }

    public static void main(String[] args) {
        BigDecimal b = new BigDecimal(124.34D);
        System.out.println(formatBigDecimal(b, "#,##0.00"));
    }
}
