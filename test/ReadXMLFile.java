
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.io.PrintStream;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;

/**
 *
 * @author zeus
 */
public class ReadXMLFile {

   public static void main(String argv[]) {

      try {

         File fXmlFile = new File("C:\\Users\\zeus\\.SIWEBXML\\XmlSAT148 .xml");
         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         dbFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
         dbFactory.setNamespaceAware(true);
         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
         Document doc = dBuilder.parse(fXmlFile);
         //doc.getDocumentElement().normalize();
         doc.setStrictErrorChecking(false);

         System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

         NodeList nList = doc.getDocumentElement().getChildNodes();
         if (nList == null) {
            System.out.println("Is null");
         } else {
            System.out.println("-----------------------");

            for (int temp = 0; temp < nList.getLength(); temp++) {
               Node nNode = nList.item(temp);
               if (nNode.getNodeName().equals("Addenda")) {
                  if (nNode.hasChildNodes()) {
                     NodeList nList2 = nNode.getChildNodes();
                     for (int temp2 = 0; temp2 < nList2.getLength(); temp2++) {
                        Node nNode2 = nList2.item(temp2);
                        if (nNode2.getNodeName().equals("mabe:Factura")) {
                           if (nNode2.getNodeType() == Node.ELEMENT_NODE) {
                              Element eElement = (Element) nNode2;
                              eElement.setAttributeNS("http://www.w3.org/2001/xsi/", "xsi:schemaLocation", "http://recepcionfe.mabempresa.com/cfd/addenda/v1 http://recepcionfe.mabempresa.com/cfd/addenda/v1/mabev1.xsd");
                              eElement.setAttributeNS("http://www.w3.org/2001/XMLSchema", "xmlns:xsi", "http://www.w3.org/2001/XMLSchema");
                              eElement.setAttribute("xmlns:xsi","http://www.w3.org/2001/XMLSchema-instance");
                              eElement.setAttributeNS("http://recepcionfe.mabempresa.com/cfd/addenda/v1", "xmlns:mabe", "http://www.w3.org/2001/XMLSchema-instance");
                              //eElement.setAttribute("xmlns:xsi","http://www.w3.org/2001/XMLSchema-instance");
                           }
                        }
                     }
                  }
               }
            }
         }

         //Save XML

// The XML document we created above is still in memory
         // so we have to output it to a real file.
         // In order to do it we first have to create
         // an instance of DOMSource
         DOMSource source = new DOMSource(doc);

         // PrintStream will be responsible for writing
         // the text data to the file
         PrintStream ps = new PrintStream("C:\\Users\\zeus\\.SIWEBXML\\XmlSAT148Parser.xml");
         StreamResult result = new StreamResult(ps);

         // Once again we are using a factory of some sort,
         // this time for getting a Transformer instance,
         // which we use to output the XML
         TransformerFactory transformerFactory = TransformerFactory.newInstance();
         Transformer transformer = transformerFactory.newTransformer();
         transformer.setOutputProperty(OutputKeys.ENCODING,"utf-8");

         // The actual output to a file goes here
         transformer.transform(source, result);

         /*
         NodeList nList = doc.getElementsByTagName("staff");
          */
      } catch (Exception e) {
         System.out.println("error " + e.getMessage());
      }
   }

}
