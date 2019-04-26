//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zscat.mallplus.pay.util;

import com.zscat.mallplus.pay.exeception.BusinessException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class XMLUtil {
    public XMLUtil() {
    }

    public static Map<String, String> doXMLParse(String strxml) throws JDOMException, IOException {
        strxml = strxml.replaceFirst("encoding=\".*\"", "encoding=\"UTF-8\"");
        if (null != strxml && !"".equals(strxml)) {
            Map<String, String> m = new HashMap();
            InputStream in = new ByteArrayInputStream(strxml.getBytes("UTF-8"));
            SAXBuilder builder = new SAXBuilder();
            Document doc = builder.build(in);
            Element root = doc.getRootElement();
            List<?> list = root.getChildren();

            String k;
            String v;
            for(Iterator it = list.iterator(); it.hasNext(); m.put(k, v)) {
                Element e = (Element)it.next();
                k = e.getName();
                v = "";
                List<?> children = e.getChildren();
                if (children.isEmpty()) {
                    v = e.getTextNormalize();
                } else {
                    v = getChildrenText(children);
                }
            }

            in.close();
            return m;
        } else {
            throw new BusinessException("error when parse notify postData, invalid format. " + strxml);
        }
    }

    public static String getChildrenText(List<?> children) {
        StringBuffer sb = new StringBuffer();
        if (!children.isEmpty()) {
            Iterator it = children.iterator();

            while(it.hasNext()) {
                Element e = (Element)it.next();
                String name = e.getName();
                String value = e.getTextNormalize();
                List<?> list = e.getChildren();
                sb.append("<" + name + ">");
                if (!list.isEmpty()) {
                    sb.append(getChildrenText(list));
                }

                sb.append(value);
                sb.append("</" + name + ">");
            }
        }

        return sb.toString();
    }

    public static String getXMLEncoding(String strxml) throws JDOMException, IOException {
        InputStream in = new ByteArrayInputStream(strxml.getBytes());
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(in);
        in.close();
        return (String)doc.getProperty("encoding");
    }
}
