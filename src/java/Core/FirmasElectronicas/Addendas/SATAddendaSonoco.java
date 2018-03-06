package Core.FirmasElectronicas.Addendas;

import Core.FirmasElectronicas.SAT.Comprobante;
import Core.FirmasElectronicas.SAT.Comprobante.Addenda;
import comSIWeb.Operaciones.Conexion;
import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.PrintStream;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import Core.FirmasElectronicas.SAT_SONOCO.ObjectFactory;
import Core.FirmasElectronicas.SAT_SONOCO.Factura;
import Core.FirmasElectronicas.SAT_SONOCO.Factura.Entrega;
import Core.FirmasElectronicas.SAT_SONOCO.Factura.Moneda;
import Core.FirmasElectronicas.SAT_SONOCO.Factura.Proveedor;

/**
 *Genera el XML de la addenda de SONOCO
 * @author zeus
 * Querys para generar los campos adicionales:
 * Alter table vta_facturas 
 * add SON_CODIGO integer not null default 0,
 * add SON_PLANTA integer not null default 0,
 * add SON_CP integer not null default 0,
 * add SON_TIPOMONEDA varchar(3) not null default '';
 */
public class SATAddendaSonoco extends SATAddenda {

   public void FillAddenda(Addenda addenda, Comprobante objComp,
           String strPath, int intTransaccion, Conexion oConn) {
      int intSON_CODIGO = 0;
      int intSON_PLANTA = 0;
      String strSON_TIPOMONEDA = "";
      String strSON_CP = "";
      /*Consultamos datos del docto*/
      String strSql = "SELECT SON_CODIGO,SON_PLANTA,SON_CP,SON_TIPOMONEDA "
              + " FROM vta_facturas WHERE FAC_ID = " + intTransaccion;
      try {
         ResultSet rs = oConn.runQuery(strSql);
         while (rs.next()) {
            intSON_CODIGO = rs.getInt("SON_CODIGO");
            intSON_PLANTA = rs.getInt("SON_PLANTA");
            strSON_TIPOMONEDA = rs.getString("SON_TIPOMONEDA");
            strSON_CP = rs.getString("SON_CP");
         }
         //if(rs.getStatement() != null )rs.getStatement().close(); 
         rs.close();
      } catch (SQLException ex) {
         ex.fillInStackTrace();
      }
      //Instanciamos constructor de SONOCO
      ObjectFactory objFac = new ObjectFactory();
      //Objeto maestro de SONOCO
      Factura fact = objFac.createFactura();
      //Aqui llenamos los atributos que necesitamos
      fact.setVersion(BigDecimal.valueOf(1.0));
      fact.setTipoDocumento("FACTURA");//NOTA CREDITO
      fact.setFolio(objComp.getFolio());
      fact.setFecha(objComp.getFecha());
      Moneda moneda = objFac.createFacturaMoneda();
      moneda.setTipoMoneda(strSON_TIPOMONEDA);
      fact.setMoneda(moneda);
      Proveedor proveedor = objFac.createFacturaProveedor();
      proveedor.setCodigo(intSON_CODIGO + "");
      fact.setProveedor(proveedor);
      Entrega entrega = objFac.createFacturaEntrega();
      BigInteger BGnSon_CP = new BigInteger(strSON_CP);
      BigInteger BGSon_Planta = new BigInteger(intSON_PLANTA + "");
      entrega.setCodigoPostal(BGnSon_CP);
      entrega.setPlantaEntrega(BGSon_Planta);
      fact.setEntrega(entrega);
      addenda.getAny().add(fact);
   }

   @Override
   public void makeNameSpaceDeclaration(String strPath, int intTransaccion, Conexion oConn) {
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
               if (nNode.getNodeName().equals("Addenda")) {
                  if (nNode.hasChildNodes()) {
                     NodeList nList2 = nNode.getChildNodes();
                     for (int temp2 = 0; temp2 < nList2.getLength(); temp2++) {
                        Node nNode2 = nList2.item(temp2);
                        if (nNode2.getNodeName().equals("Sonoco:Factura")) {
                           if (nNode2.getNodeType() == Node.ELEMENT_NODE) {
                              Element eElement = (Element) nNode2;
                              eElement.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:schemaLocation", "http://www.itcomplements.com/printweb http://www.itcomplements.com/printweb/sonocoV1.xsd");
                           }
                        }
                     }
                  }
               }
            }
         }
         //Save XML
         DOMSource source = new DOMSource(doc);
         // PrintStream will be responsible for writing
         // the text data to the file
         PrintStream ps = new PrintStream(strPath);
         StreamResult result = new StreamResult(ps);
         // Once again we are using a factory of some sort,
         // this time for getting a Transformer instance,
         // which we use to output the XML
         TransformerFactory transformerFactory = TransformerFactory.newInstance();
         Transformer transformer = transformerFactory.newTransformer();
         transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
         // The actual output to a file goes here
         transformer.transform(source, result);
      } catch (Exception e) {
         System.out.println("error " + e.getMessage());
      }
   }
}
