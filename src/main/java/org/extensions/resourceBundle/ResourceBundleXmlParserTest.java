package org.extensions.resourceBundle;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.HashMap;

public class ResourceBundleXmlParserTest {

    private HashMap<String, String> file1 = new HashMap<>();
    private HashMap<String, String> file2 = new HashMap<>();

    @Test
    void someTest() {
        this.file1 = this.readFromXML("mcdk1.xml");
        this.file2 = this.readFromXML("mcdk2.xml");
    }

    public HashMap<String,String> readFromXML(String file) {
        try {
            return this.parseStringFromXML(file);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private HashMap<String, String> parseStringFromXML(String file) throws Exception {
        InputStream inputStream = ResourceBundleXmlParserTest.class.getClassLoader().getResourceAsStream(file);

        DocumentBuilder builder = DocumentBuilderFactory.newNSInstance().newDocumentBuilder();

        Document document = builder.parse(inputStream);
        document.getDocumentElement().normalize();
        NodeList nList = document.getElementsByTagName("string");

        HashMap<String, String> stringMap = new HashMap<>();

        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node node = nList.item(temp);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) node;
                stringMap.put(eElement.getAttribute("name"), eElement.getTextContent());
            }
        }

        return stringMap;
    }
}
