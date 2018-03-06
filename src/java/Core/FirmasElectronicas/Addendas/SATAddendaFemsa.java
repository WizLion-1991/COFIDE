package Core.FirmasElectronicas.Addendas;

import comSIWeb.Operaciones.Conexion;
import java.io.File;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
 * Genera la addenda de femsa para CFDI
 *
 * @author ZeusGalindo
 */
public class SATAddendaFemsa extends SATAddenda {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private int intTipo;
   private String strVersion;
   private String strSociedad;
   private String strNumProveedor;
   private String strNumPedido;
   private String strMoneda;
   private String strNumRecepcionSAP;
   private String strNumRemision;
   private String strRetencion;
   private String strMail;

   public int getIntTipo() {
      return intTipo;
   }

   public void setIntTipo(int intTipo) {
      this.intTipo = intTipo;
   }

   public String getStrSociedad() {
      return strSociedad;
   }

   public void setStrSociedad(String strSociedad) {
      this.strSociedad = strSociedad;
   }

   public String getStrNumProveedor() {
      return strNumProveedor;
   }

   public void setStrNumProveedor(String strNumProveedor) {
      this.strNumProveedor = strNumProveedor;
   }

   public String getStrNumPedido() {
      return strNumPedido;
   }

   public void setStrNumPedido(String strNumPedido) {
      this.strNumPedido = strNumPedido;
   }

   public String getStrMoneda() {
      return strMoneda;
   }

   public void setStrMoneda(String strMoneda) {
      this.strMoneda = strMoneda;
   }

   public String getStrNumRecepcionSAP() {
      return strNumRecepcionSAP;
   }

   public void setStrNumRecepcionSAP(String strNumRecepcionSAP) {
      this.strNumRecepcionSAP = strNumRecepcionSAP;
   }

   public String getStrNumRemision() {
      return strNumRemision;
   }

   public void setStrNumRemision(String strNumRemision) {
      this.strNumRemision = strNumRemision;
   }

   public String getStrRetencion() {
      return strRetencion;
   }

   public void setStrRetencion(String strRetencion) {
      this.strRetencion = strRetencion;
   }

   public String getStrMail() {
      return strMail;
   }

   public void setStrMail(String strMail) {
      this.strMail = strMail;
   }

   public String getStrVersion() {
      return strVersion;
   }

   public void setStrVersion(String strVersion) {
      this.strVersion = strVersion;
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   @Override
   public void makeNameSpaceDeclaration(String strPath, int intTransaccion, Conexion oConn) {
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
               if (nNode.getNodeName().equals("cfdi:Complemento")) {
                  Node nodeParent = nNode.getParentNode();//Obtenemos nodo padre del complemento
                  Node addendaNode = doc.createElement("cfdi:Addenda");
                  //Generamos el detalle de la addenda
                  nodeParent.appendChild(addendaNode);

                  //Nodo raiz de la addenda
                  Element nodeMain = doc.createElement("Documento");
                  addendaNode.appendChild(nodeMain);

                  //Nodo raiz(2) de la addenda
                  Element nodeFemsa = doc.createElement("FacturaFemsa");
                  nodeMain.appendChild(nodeFemsa);

                  //NoVersAdd
                  Element NoVersAdd = doc.createElement("noVersAdd");
                  NoVersAdd.appendChild(doc.createTextNode(this.strVersion));
                  nodeFemsa.appendChild(NoVersAdd);
                  //claseDoc
                  Element claseDoc = doc.createElement("claseDoc");
                  claseDoc.appendChild(doc.createTextNode(this.intTipo + ""));
                  nodeFemsa.appendChild(claseDoc);
                  //noSociedad
                  Element noSociedad = doc.createElement("noSociedad");
                  noSociedad.appendChild(doc.createTextNode(this.strSociedad));
                  nodeFemsa.appendChild(noSociedad);
                  //noProveedor
                  Element noProveedor = doc.createElement("noProveedor");
                  noProveedor.appendChild(doc.createTextNode(this.strNumProveedor));
                  nodeFemsa.appendChild(noProveedor);
                  //noPedido
                  Element noPedido = doc.createElement("noPedido");
                  noPedido.appendChild(doc.createTextNode(this.strNumPedido));
                  nodeFemsa.appendChild(noPedido);
                  //moneda
                  Element moneda = doc.createElement("moneda");
                  moneda.appendChild(doc.createTextNode(this.strMoneda));
                  nodeFemsa.appendChild(moneda);
                  //noEntrada
                  Element noEntrada = doc.createElement("noEntrada");
                  noEntrada.appendChild(doc.createTextNode(this.strNumRecepcionSAP));
                  nodeFemsa.appendChild(noEntrada);
                  //noRemision
                  Element noRemision = doc.createElement("noRemision");
                  noRemision.appendChild(doc.createTextNode(this.strNumRemision));
                  nodeFemsa.appendChild(noRemision);
                  //noSocio
                  Element noSocio = doc.createElement("noSocio");
//                  noSocio.appendChild(doc.createTextNode(this.));
                  nodeFemsa.appendChild(noSocio);
                  //centroCostos
                  Element centroCostos = doc.createElement("centroCostos");
//                  centroCostos.appendChild(doc.createTextNode(this.intTipo + ""));
                  nodeFemsa.appendChild(centroCostos);
                  //iniPerLiq
                  Element iniPerLiq = doc.createElement("iniPerLiq");
//                  iniPerLiq.appendChild(doc.createTextNode(this.intTipo + ""));
                  nodeFemsa.appendChild(iniPerLiq);
                  //finPerLiq
                  Element finPerLiq = doc.createElement("finPerLiq");
//                  finPerLiq.appendChild(doc.createTextNode(this.intTipo + ""));
                  nodeFemsa.appendChild(finPerLiq);
                  //retencion1
                  Element retencion1 = doc.createElement("retencion1");
                  if (!this.strRetencion.isEmpty()) {
                     retencion1.appendChild(doc.createTextNode(this.strRetencion));
                  }
                  nodeFemsa.appendChild(retencion1);
                  //retencion2
                  Element retencion2 = doc.createElement("retencion2");
//                  retencion2.appendChild(doc.createTextNode(this.intTipo + ""));
                  nodeFemsa.appendChild(retencion2);
                  //email
                  Element email = doc.createElement("email");
                  email.appendChild(doc.createTextNode(this.strMail));
                  nodeFemsa.appendChild(email);

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
         transformer.setOutputProperty(OutputKeys.INDENT, "yes");
         // The actual output to a file goes here
         transformer.transform(source, result);
      } catch (Exception e) {
         System.out.println("error " + e.getMessage());
      }
   }

   private void GetData(int intTransaccion, Conexion oConn) {
      String strSql = "SELECT FEM_VER,FEM_TIPO,FEM_SOC,FEM_NUM_PROV,FEM_MONEDA"
              + ",FEM_NUM_PED,FEM_NUM_ENTR_SAP,FEM_NUM_REMI,FEM_CORREO,FEM_RET "
              + " from vta_facturas "
              + " where FAC_ID = " + intTransaccion;
      try {
         ResultSet rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            this.strVersion = rs.getString("FEM_VER");
            this.intTipo = rs.getInt("FEM_TIPO");
            this.strSociedad = rs.getString("FEM_SOC");
            this.strNumProveedor = rs.getString("FEM_NUM_PROV");
            this.strNumPedido = rs.getString("FEM_NUM_PED");
            this.strMoneda = rs.getString("FEM_MONEDA");
            this.strNumRecepcionSAP = rs.getString("FEM_NUM_ENTR_SAP");
            this.strNumRemision = rs.getString("FEM_NUM_REMI");
            this.strMail = rs.getString("FEM_CORREO");
            this.strRetencion = rs.getString("FEM_RET");
         }
         //if(rs.getStatement() != null )rs.getStatement().close(); 
         rs.close();
      } catch (SQLException ex) {
         Logger.getLogger(SATAddendaLala.class.getName()).log(Level.SEVERE, null, ex);
      }
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">

   public SATAddendaFemsa() {
   }
   // </editor-fold>
}
