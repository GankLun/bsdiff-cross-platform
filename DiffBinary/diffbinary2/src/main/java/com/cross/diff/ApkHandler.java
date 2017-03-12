package com.cross.diff;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author czl 2017/2/18
 * @Package com.cross.diff
 * @Title: ApkHandler
 * @Description: (映射AXML里的manifest节点的属性)
 * Create DateTime: 2017/2/18
 */

public class ApkHandler extends DefaultHandler {

    private String packageName = null;
    private int versionCode = -1;
    private String versionName = null;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (!"manifest".equals(qName)) {
            return;
        }
        for (int i = 0; i < attributes.getLength(); i++) {
            String value = attributes.getValue(i);// 获取属性的value值
            if ("package".equals(attributes.getQName(i))) {
                packageName = value;
            }
            if ("android:versionCode".equals(attributes.getQName(i))) {
                versionCode = Integer.parseInt(value);
            }
            if ("android:versionName".equals(attributes.getQName(i))) {
                versionName = value;
            }
        }
    }

    public String getPackageName() {
        return packageName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public String getVersionName() {
        return versionName;
    }


}

