package adblockchaindemo.readerdata;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Dominic Donofrio
 */
public class Parser {

    private String epc = "a";
    private String reader = "a";
    private String item = "a";
    private String time = "a";
    private String antenna = "a";
    private File readerData = null;
    

    private final DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

    public void Parser() throws SAXException, IOException, ParserConfigurationException {
        if (readerData != null) {
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(readerData);
            doc.getDocumentElement().normalize();

            NodeList nList1 = doc.getElementsByTagName("Reader");
            Node nNode1 = nList1.item(0);
            Element eElement1 = (Element) nNode1;

            reader = eElement1.getElementsByTagName("Name")
                    .item(0).getTextContent();
            NodeList nList2 = doc.getElementsByTagName("Tag");

            for (int temp = 0; temp < nList2.getLength(); temp++) {
                Node nNode2 = nList2.item(temp);
                if (nNode2.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement2 = (Element) nNode2;

                    epc = eElement2.getElementsByTagName("TagID")
                            .item(temp).getTextContent();

                    antenna = eElement2.getElementsByTagName("Antenna")
                            .item(temp).getTextContent();

                    time = eElement2.getElementsByTagName("Time")
                            .item(temp).getTextContent();
                }
            }
        }
    }

    public void loadFile(String a) {
        readerData = new File(a);
    }
    
    public int compareFiles(String a, String b) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilder dBuilderA = dbFactory.newDocumentBuilder();
        Document docA = dBuilderA.parse(a);
        docA.getDocumentElement().normalize();
        
        DocumentBuilder dBuilderB = dbFactory.newDocumentBuilder();
        Document docB = dBuilderB.parse(b);
        docB.getDocumentElement().normalize();
        
        if(docA==docB) {
            return 1;
        }
        else
            return -1;
    }
    
    public boolean isEmpty(String a) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(a));
        return br.readLine() == null;
    }
    
    public Document getContent(String a) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilder dBuilderA = dbFactory.newDocumentBuilder();
        Document docA = dBuilderA.parse(a);
        docA.getDocumentElement().normalize();
        
        return docA;
    }

    public String getEPC() throws ParserConfigurationException, SAXException, IOException {
        if (readerData != null) {
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(readerData);
            doc.getDocumentElement().normalize();

            NodeList nList1 = doc.getElementsByTagName("Reader");
            Node nNode1 = nList1.item(0);
            NodeList nList2 = doc.getElementsByTagName("Tag");

            for (int temp = 0; temp < nList2.getLength(); temp++) {
                Node nNode2 = nList2.item(temp);
                if (nNode2.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement2 = (Element) nNode2;

                    epc = eElement2.getElementsByTagName("TagID")
                            .item(temp).getTextContent();

                    if (epc.endsWith("4000")) {
                        item = "Watermelon";
                    } else if(epc.endsWith("5300")) {
                        item = "Cherry";
                    } else if(epc.endsWith("19363")) {
                        item = "Apple";
                    } else if(epc.endsWith("6050")) {
                        item = "Banana";
                    } else {
                        item = "Unknown";
                    }
                }
            }
        }
        return epc;
    }

    public String getReader() throws ParserConfigurationException, SAXException, IOException {
        if (readerData != null) {
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(readerData);
            doc.getDocumentElement().normalize();

            NodeList nList1 = doc.getElementsByTagName("Reader");
            Node nNode1 = nList1.item(0);
            Element eElement1 = (Element) nNode1;
            String temp = eElement1.getElementsByTagName("Name").item(0).getTextContent();
            if (temp.equalsIgnoreCase("Farm_Exit")) {
                reader = "Farm";
            } else if (temp.equalsIgnoreCase("DC_Receive")) {
                reader = "DistributorR";
            } else if (temp.equalsIgnoreCase("DC_SHIP")) {
                reader = "DistributorS";
            } else if (temp.equalsIgnoreCase("Kitchen_Receive")) {
                reader = "Kitchen";
            } else if (temp.equalsIgnoreCase("Kitchen_Exit")) {
                reader = "Store";
            } else if (temp.equalsIgnoreCase("Purchase")) {
                reader = "Customer";
            } else {
                reader = eElement1.getElementsByTagName("Name").item(0).getTextContent();
            }
        }
        return reader;
    }

    public String getItem() {
        return item;
    }

    public String getTime() throws ParserConfigurationException, SAXException, IOException {
        if (readerData != null) {
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(readerData);
            doc.getDocumentElement().normalize();

            NodeList nList1 = doc.getElementsByTagName("Reader");
            Node nNode1 = nList1.item(0);

            NodeList nList2 = doc.getElementsByTagName("Tag");

            for (int temp = 0; temp < nList2.getLength(); temp++) {
                Node nNode2 = nList2.item(temp);
                if (nNode2.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement2 = (Element) nNode2;

                    time = eElement2.getElementsByTagName("Time")
                            .item(temp).getTextContent();
                }
            }
        }
        return time;
    }
    
    public String getTime(String f) throws ParserConfigurationException, SAXException, IOException {
        if (f.getBytes() != null) {
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(f);
            doc.getDocumentElement().normalize();

            NodeList nList1 = doc.getElementsByTagName("Reader");
            Node nNode1 = nList1.item(0);

            NodeList nList2 = doc.getElementsByTagName("Tag");

            for (int temp = 0; temp < nList2.getLength(); temp++) {
                Node nNode2 = nList2.item(temp);
                if (nNode2.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement2 = (Element) nNode2;

                    time = eElement2.getElementsByTagName("Time")
                            .item(temp).getTextContent();
                }
            }
        }
        return time;
    }

    public String getAntenna() throws ParserConfigurationException, SAXException, IOException {
        if (readerData != null) {
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(readerData);
            doc.getDocumentElement().normalize();

            NodeList nList1 = doc.getElementsByTagName("Reader");
            Node nNode1 = nList1.item(0);
            NodeList nList2 = doc.getElementsByTagName("Tag");

            for (int temp = 0; temp < nList2.getLength(); temp++) {
                Node nNode2 = nList2.item(temp);
                if (nNode2.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement2 = (Element) nNode2;
                    antenna = eElement2.getElementsByTagName("Antenna")
                            .item(temp).getTextContent();
                }
            }
        }
        return antenna;
    }
}
