package com.blog.common;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

public class XMLParserUtil {

    private DocumentBuilderFactory factory;
    private DocumentBuilder builder;
    private Document document;

    public XMLParserUtil(String content) throws Exception {
        StringReader sr = new StringReader(content);
        InputSource is = new InputSource(sr);
        factory = DocumentBuilderFactory.newInstance();
        builder = factory.newDocumentBuilder();
        document = builder.parse(is);
        document.getDocumentElement().normalize();
    }

    public String getElementValue(String tagName) {
        NodeList nodeList = document.getElementsByTagName(tagName);
        if(nodeList.getLength() > 0) {
            Element element = (Element)nodeList.item(0);
            return element.getTextContent();
        }
        return null;
    }
}
