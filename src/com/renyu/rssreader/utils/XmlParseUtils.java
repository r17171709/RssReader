package com.renyu.rssreader.utils;

import com.renyu.rssreader.bean.RSSBean;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by renyu on 2016/12/9.
 */
public class XmlParseUtils {

    private static volatile XmlParseUtils xmlParseUtils;

    public static XmlParseUtils getIntance() {
        if (xmlParseUtils==null) {
            synchronized (XmlParseUtils.class) {
                if (xmlParseUtils==null) {
                    xmlParseUtils=new XmlParseUtils();
                }
            }
        }
        return xmlParseUtils;
    }

    private XmlParseUtils() {}

    public ArrayList<RSSBean> parse(String text) {
        ArrayList<RSSBean> beans=new ArrayList<>();
        try {
            Document document= DocumentHelper.parseText(text);
            Element rootElement=document.getRootElement();
            List<Element> items=rootElement.element("channel").elements("item");
            for (Element item : items) {
                Element title=item.element("title");
                Element link=item.element("link");
                RSSBean bean=new RSSBean(title.getText(), link.getText());
                beans.add(bean);
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return beans;
    }
}
