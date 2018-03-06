/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Core.FirmasElectronicas.Addendas;

import comSIWeb.Operaciones.Conexion;
import java.io.File;
import java.io.PrintStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author ZeusGalindo
 */
public class SATAddendaAutoZone extends SATAddenda {




   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private String strVendorId;
   private String strDeptId;
   private String Buyer;
   private String Email;

   public String getStrVendorId() {
      return strVendorId;
   }

   public void setStrVendorId(String strVendorId) {
      this.strVendorId = strVendorId;
   }

   public String getStrDeptId() {
      return strDeptId;
   }

   public void setStrDeptId(String strDeptId) {
      this.strDeptId = strDeptId;
   }

   public String getBuyer() {
      return Buyer;
   }

   public void setBuyer(String Buyer) {
      this.Buyer = Buyer;
   }

   public String getEmail() {
      return Email;
   }

   public void setEmail(String Email) {
      this.Email = Email;
   }
   
   
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public SATAddendaAutoZone() {
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   private void GetData(int intTransaccion, Conexion oConn) {
   }

   public void makeNameSpaceDeclaration(String strPath, String strPathNew, int intTransaccion, Conexion oConn) {
      //Obtenemos datos
      GetData(intTransaccion, oConn);
      //Parametros

      try {
         File fXmlFile = new File(strPath);
         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         dbFactory.setNamespaceAware(true);
         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
         Document doc = dBuilder.parse(fXmlFile);
         doc.setStrictErrorChecking(false);
         NodeList nList = doc.getDocumentElement().getChildNodes();
         if (nList != null) {
            for (int temp = 0; temp < nList.getLength(); temp++) {
               Node nNode = nList.item(temp);
               if (nNode.getNodeName().equals("Impuestos")) {
                  Node nodeParent = nNode.getParentNode();//Obtenemos nodo padre del complemento
                  Node addendaNode = doc.createElement("Addenda");
                  //Generamos el detalle de la addenda
                  nodeParent.appendChild(addendaNode);

                  //Nodo raiz de la addenda
                  Element nodeMain = doc.createElement("po:ADDENDA20");
                  nodeMain.setAttribute("xlmns:po", "http://azfest.autozone.com/fssit91/XSD");
                  
                  nodeMain.setAttribute("VENDOR_ID", strVendorId);
                  nodeMain.setAttribute("DEPTID", strDeptId);
                  nodeMain.setAttribute("BUYER", Buyer);
                  nodeMain.setAttribute("EMAIL", Email);
                  nodeMain.setAttribute("VERSION", "2.0");
                  addendaNode.appendChild(nodeMain);

               }
            }
         }
         //Save XML
         DOMSource source = new DOMSource(doc);
         // PrintStream will be responsible for writing
         // the text data to the file
         PrintStream ps = new PrintStream(strPathNew);
         StreamResult result = new StreamResult(ps);
         // Once again we are using a factory of some sort,
         // this time for getting a Transformer instance,
         // which we use to output the XML
         TransformerFactory transformerFactory = TransformerFactory.newInstance();
         Transformer transformer = transformerFactory.newTransformer();
         transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
         transformer.setOutputProperty(OutputKeys.INDENT, "yes");
         // The actual output to a file goes here
         transformer.transform(source, result);
      } catch (Exception e) {
         System.out.println("error " + e.getMessage());
      }

   }
   // </editor-fold>
}
