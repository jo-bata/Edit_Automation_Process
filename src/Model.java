import org.w3c.dom.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Model {
    /***********************************************************************************************************************************************************/
    private String[] fName = new String[2];  // 원본 xml, xsl 파일명
    private Element[] root = new Element[3];  // 0 : .xml(원본), 1 : .xsl(원본), 2 : .xml(최종)
    private String[] eName0 = { "P", "TEXT", "CHAR", "PICTURE", "SHAPEOBJECT", "TABLE", "PARALIST"};  // 원본  xml element name
    private String[] aName0 = { "PageBreak", "ParaShape", "Style", "CharShape", "InstId" };  // 원본 xml attribute name
    private String[] eName1 = { "hml:PARASHAPE", "hml:STYLE", "hml:CHARSHAPE", "hml:FONTID", "hml:FONTFACE", "hml:FONT", "xsl:template", "xsl:if", "IMG" };   // 원본 xsl element name
    private String[] aName1 = { "Id", "Align", "Name", "Height", "TextColor", "Hangul", "Lang", "name", "IMG", "test", "src", "width", "height" };   // 원본 xsl attribute name
    private String[] eName2 = { "Root", "P", "Char", "Img", "Table" };  // 최종 xml element name
    private String[] aName2 = { "Align", "Style", "Shape", "Size", "Color", "href", "Width", "Height"};   // 최종 xml attribute name
    private HashMap<String, String> psMap = new HashMap<String, String>();  // 정렬 방식
    private HashMap<String, String> styleMap = new HashMap<String, String>();  // 스타일
    private HashMap<String, String> cs_0 = new HashMap<String, String>(); // 글씨 크기
    private HashMap<String, String> cs_1 = new HashMap<String, String>();  // 글씨 색
    private HashMap<String, String> cs_2 = new HashMap<String, String>();  // 글꼴
    private HashMap<String, String> img_0 = new HashMap<String, String>();  // 파일명
    private HashMap<String, String> img_1 = new HashMap<String, String>();  // 가로
    private HashMap<String, String> img_2 = new HashMap<String, String>();  // 세로
    private String resultPath = "C:\\Users\\p c\\IdeaProjects\\Make_Xml\\xml";
    /***********************************************************************************************************************************************************/
    public String[] getFName() { return fName; }
    public Element[] getRoot() { return root; }
    public HashMap<String, String> getPsMap() { return psMap; }
    public HashMap<String, String> getStyleMap() { return styleMap; }
    public HashMap<String, String> getCs_0() { return cs_0; }
    public HashMap<String, String> getCs_1() { return cs_1; }
    public HashMap<String, String> getCs_2() { return cs_2; }
    public HashMap<String, String> getImg_0() { return img_0; }
    public HashMap<String, String> getImg_1() { return img_1; }
    public HashMap<String, String> getImg_2() { return img_2; }
    /***********************************************************************************************************************************************************/
    public void setFName(int index, String fName) { this.fName[index] = fName; }
    public void setRoot(int index, String uri) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document xmlDoc = db.parse("xml//" + uri);
            root[index] = xmlDoc.getDocumentElement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setPsMap(Element root) {
        NodeList nl = root.getElementsByTagName(eName1[0]);
        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            Element element = (Element)node;
            psMap.put(element.getAttribute(aName1[0]), element.getAttribute(aName1[1]));
        }
    }
    public void setStyleMap(Element root) {
        NodeList nl = root.getElementsByTagName(eName1[1]);
        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            Element element = (Element)node;
            styleMap.put(element.getAttribute(aName1[0]), element.getAttribute(aName1[2]));
        }
    }
    public void setCsMap(Element root) {
        NodeList nl = root.getElementsByTagName(eName1[2]);
        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            Element element = (Element)node;
            cs_0.put(element.getAttribute(aName1[0]), transformPT(element.getAttribute(aName1[3])));
            cs_1.put(element.getAttribute(aName1[0]), transformColor(element.getAttribute(aName1[4])));
            NodeList nl2 = element.getElementsByTagName(eName1[3]);
            for (int j = 0; j < nl2.getLength(); j++) {
                Node node2 = nl2.item(j);
                Element element2 = (Element)node2;
                NodeList nl3 = root.getElementsByTagName(eName1[4]);
                for (int k = 0; k < nl3.getLength(); k++) {
                    Node node3 = nl3.item(k);
                    Element element3 = (Element)node3;
                    NodeList nl4;
                    if(element3.getAttribute(aName1[6]).equals(aName1[5])) {
                        nl4 = element3.getElementsByTagName(eName1[5]);
                        for (int l = 0; l < nl4.getLength(); l++) {
                            Node node4 = nl4.item(l);
                            Element element4 = (Element)node4;
                            if(element4.getAttribute(aName1[0]).equals(element2.getAttribute(aName1[5])))
                                cs_2.put(element.getAttribute(aName1[0]), element4.getAttribute(aName1[2]));
                        }
                    }
                }
            }
        }
    }
    public void setImgMap(Element root) {
        NodeList nl = root.getElementsByTagName(eName1[6]);
        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            Element element = (Element)node;
            if (element.getAttribute(aName1[7]).equals(aName1[8])) {
                NodeList nl2 = element.getElementsByTagName(eName1[7]);
                for (int j = 0; j < nl2.getLength(); j++) {
                    Node node2 = nl2.item(j);
                    Element element2 = (Element)node2;
                    Pattern p = Pattern.compile("[0-9]+");
                    Matcher m = p.matcher(element2.getAttribute(aName1[9]));
                    Element element3 = (Element)element2.getElementsByTagName(eName1[8]).item(0);
                    while (m.find()) {
                        img_0.put(m.group(), element3.getAttribute(aName1[10]));
                        img_1.put(m.group(), element3.getAttribute(aName1[11]));
                        img_2.put(m.group(), element3.getAttribute(aName1[12]));
                    }
                }
            }
        }
    }
    /***********************************************************************************************************************************************************/
    public String transformPT(String str) {
        return (Integer.parseInt(str) / 100) + "pt";
    }
    public String transformColor(String str) {
        int dec_color = Integer.parseInt(str);
        String str_hex = Integer.toHexString(dec_color);
        String color = "";
        if (dec_color == 0) {
            for (int i = 0; i < 6; i++)
                color += "0";
        }
        else {
            if (str_hex.length() < 6) {
                color += str_hex;
                for (int i = str_hex.length(); i < 6; i++)
                    color += "0";
            }
            else{
                color = str_hex.substring(4) + str_hex.substring(2, 4) + str_hex.substring(0, 2);
            }
        }
        return "#" + color;
    }
    public void createXML(Element root) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document xmlDoc = db.newDocument();
            createElement(root, xmlDoc);
            //XML 파일로 쓰기
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer t = tf.newTransformer();
            // 변환 설정
            t.setOutputProperty(OutputKeys.METHOD, "xml");
            t.setOutputProperty(OutputKeys.VERSION, "1.0");
            t.setOutputProperty(OutputKeys.ENCODING, "utf-8");
            t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            t.setOutputProperty(OutputKeys.STANDALONE, "no");
            t.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource ds = new DOMSource(xmlDoc);
            StreamResult sr = new StreamResult(new FileOutputStream(new File(resultPath + "\\Result_" + fName[0])));  // 생성용
//            StreamResult sr = new StreamResult(System.out);  // 출력용
            t.transform(ds, sr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createElement(Element root, Document xmlDoc) {
        root = xmlDoc.createElement(eName2[0]);  // root 엘리먼트 생성
        xmlDoc.appendChild(root);
        NodeList nl = this.root[0].getElementsByTagName(eName0[0]);
        Element p = null;
        for (int i = 0; i< nl.getLength(); i++) {
            Element element = (Element)nl.item(i);  // P 노드 1개
            NodeList nl2 = element.getElementsByTagName(eName0[1]);
            if (element.getAttribute(aName0[0]).length() > 0) { // 엘리먼트 P가 속성 PageBreak를 가지면
                p = xmlDoc.createElement(eName2[1]);
                root.appendChild(p);
                int cnt_char = 0;
                Element c = null;
                for (int j = 0; j < nl2.getLength(); j++) {
                    Element element2 = (Element)nl2.item(j);  // TEXT 노드 1개
                    if(element2.getParentNode().getParentNode().getNodeName().equals(eName0[6]))  // TABLE 내의 char 제거
                        continue;
                    for (int k = 0; k < element2.getChildNodes().getLength(); k++) {
                        if(element2.getChildNodes().item(k).getNodeName().equals(eName0[2]) && element2.getChildNodes().item(k).getTextContent().length() > 0) {  // CHAR
                            if (cnt_char == 0) {
                                c = xmlDoc.createElement(eName2[2] + element.getAttribute(aName0[2]));
                                c.appendChild(xmlDoc.createTextNode(element2.getChildNodes().item(k).getTextContent()));
                                p.appendChild(c);
                                Attr align = xmlDoc.createAttribute(aName2[0]);
                                align.setValue(psMap.get(element.getAttribute(aName0[1])));
                                c.setAttributeNode(align);
                                Attr style = xmlDoc.createAttribute(aName2[1]);
                                style.setValue(styleMap.get(element.getAttribute(aName0[2])));
                                c.setAttributeNode(style);
                                Attr size = xmlDoc.createAttribute(aName2[3]);
                                size.setValue(cs_0.get(element2.getAttribute(aName0[3])));
                                c.setAttributeNode(size);
                                Attr color = xmlDoc.createAttribute(aName2[4]);
                                color.setValue(cs_1.get(element2.getAttribute(aName0[3])));
                                c.setAttributeNode(color);
                                Attr shape = xmlDoc.createAttribute(aName2[2]);
                                shape.setValue(cs_2.get(element2.getAttribute(aName0[3])));
                                c.setAttributeNode(shape);
                                cnt_char++;
                            }
                            else {
                                c.appendChild(xmlDoc.createTextNode(element2.getChildNodes().item(k).getTextContent()));
                                cnt_char++;
                            }
                        }
                        else if(element2.getChildNodes().item(k).getNodeName().equals(eName0[3])) {  // PICTURE
                            Element img = xmlDoc.createElement(eName2[3]);
                            Element element3 = (Element)element2.getChildNodes().item(k);
                            Element element4 = (Element)element3.getElementsByTagName(eName0[4]).item(0);
//                            img.appendChild(xmlDoc.createTextNode("[" + element4.getAttribute(aName0[4]) + "]" + "이미지입니다."));
                            p.appendChild(img);
//                            Attr align = xmlDoc.createAttribute(aName2[0]);
//                            align.setValue(psMap.get(element.getAttribute(aName0[1])));
//                            img.setAttributeNode(align);
                            Attr href = xmlDoc.createAttribute(aName2[5]);
                            href.setValue("file://IMG//" + img_0.get(element4.getAttribute(aName0[4])));
                            img.setAttributeNode(href);
//                            Attr width = xmlDoc.createAttribute(aName2[6]);
//                            width.setValue(img_1.get(element4.getAttribute(aName0[4])));
//                            img.setAttributeNode(width);
//                            Attr height = xmlDoc.createAttribute(aName2[7]);
//                            height.setValue(img_2.get(element4.getAttribute(aName0[4])));
//                            img.setAttributeNode(height);
                        }
                        else if(element2.getChildNodes().item(k).getNodeName().equals(eName0[5])) {  // TABLE
                            Element table = xmlDoc.createElement(eName2[4]);
                            table.appendChild(xmlDoc.createTextNode("표입니다."));
                            p.appendChild(table);
                        }
                    }
                }
            }
            else {  // 엘리먼트 P 가 속성 PageBreak를 가지지 않으면
                int cnt_char = 0;
                Element c = null;
                for (int j = 0; j < nl2.getLength(); j++) {
                    Element element2 = (Element)nl2.item(j);  // TEXT 노드 1개
                    if(element2.getParentNode().getParentNode().getNodeName().equals(eName0[6]))  // TABLE 내의 char 제거
                        continue;
                    for (int k = 0; k < element2.getChildNodes().getLength(); k++) {
                        if(element2.getChildNodes().item(k).getNodeName().equals(eName0[2]) && element2.getChildNodes().item(k).getTextContent().length() > 0) {  // CHAR
                            if (cnt_char == 0) {
                                c = xmlDoc.createElement(eName2[2] + element.getAttribute(aName0[2]));
                                c.appendChild(xmlDoc.createTextNode(element2.getChildNodes().item(k).getTextContent()));
                                p.appendChild(c);
                                Attr align = xmlDoc.createAttribute(aName2[0]);
                                align.setValue(psMap.get(element.getAttribute(aName0[1])));
                                c.setAttributeNode(align);
                                Attr style = xmlDoc.createAttribute(aName2[1]);
                                style.setValue(styleMap.get(element.getAttribute(aName0[2])));
                                c.setAttributeNode(style);
                                Attr size = xmlDoc.createAttribute(aName2[3]);
                                size.setValue(cs_0.get(element2.getAttribute(aName0[3])));
                                c.setAttributeNode(size);
                                Attr color = xmlDoc.createAttribute(aName2[4]);
                                color.setValue(cs_1.get(element2.getAttribute(aName0[3])));
                                c.setAttributeNode(color);
                                Attr shape = xmlDoc.createAttribute(aName2[2]);
                                shape.setValue(cs_2.get(element2.getAttribute(aName0[3])));
                                c.setAttributeNode(shape);
                                cnt_char++;
                            }
                            else {
                                c.appendChild(xmlDoc.createTextNode(element2.getChildNodes().item(k).getTextContent()));
                                cnt_char++;
                            }
                        }
                        else if(element2.getChildNodes().item(k).getNodeName().equals(eName0[3])) {  // PICTURE
                            Element img = xmlDoc.createElement(eName2[3]);
                            Element element3 = (Element)element2.getChildNodes().item(k);
                            Element element4 = (Element)element3.getElementsByTagName(eName0[4]).item(0);
//                            img.appendChild(xmlDoc.createTextNode("[" + element4.getAttribute(aName0[4]) + "]" + "이미지입니다."));
                            p.appendChild(img);
//                            Attr align = xmlDoc.createAttribute(aName2[0]);
//                            align.setValue(psMap.get(element.getAttribute(aName0[1])));
//                            img.setAttributeNode(align);
                            Attr href = xmlDoc.createAttribute(aName2[5]);
                            href.setValue("file://IMG//" + img_0.get(element4.getAttribute(aName0[4])));
                            img.setAttributeNode(href);
//                            Attr width = xmlDoc.createAttribute(aName2[6]);
//                            width.setValue(img_1.get(element4.getAttribute(aName0[4])));
//                            img.setAttributeNode(width);
//                            Attr height = xmlDoc.createAttribute(aName2[7]);
//                            height.setValue(img_2.get(element4.getAttribute(aName0[4])));
//                            img.setAttributeNode(height);
                        }
                        else if(element2.getChildNodes().item(k).getNodeName().equals(eName0[5])) {  // TABLE
                            Element table = xmlDoc.createElement(eName2[4]);
                            table.appendChild(xmlDoc.createTextNode("표입니다."));
                            p.appendChild(table);
                        }
                    }
                }
            }
        }
    }
}