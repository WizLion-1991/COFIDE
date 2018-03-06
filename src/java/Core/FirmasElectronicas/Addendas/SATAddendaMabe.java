package Core.FirmasElectronicas.Addendas;

import Core.FirmasElectronicas.SAT22.Comprobante;
import Core.FirmasElectronicas.SAT22.Comprobante.Addenda;
import Core.FirmasElectronicas.SAT22.Comprobante.Conceptos;
import Core.FirmasElectronicas.SAT22.Comprobante.Conceptos.Concepto;
import Core.FirmasElectronicas.SAT_MABE.Factura;
import Core.FirmasElectronicas.SAT_MABE.Factura.Detalles;
import Core.FirmasElectronicas.SAT_MABE.Factura.Detalles.Detalle;
import Core.FirmasElectronicas.SAT_MABE.Factura.Entrega;
import Core.FirmasElectronicas.SAT_MABE.Factura.Moneda;
import Core.FirmasElectronicas.SAT_MABE.Factura.Proveedor;
import Core.FirmasElectronicas.SAT_MABE.Factura.Subtotal;
import Core.FirmasElectronicas.SAT_MABE.Factura.Total;
import Core.FirmasElectronicas.SAT_MABE.ObjectFactory;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Utilerias.NumberString;
import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import java.io.PrintStream;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.logging.log4j.LogManager;
import org.w3c.dom.Element;

/**
 * Genera el XML de la addenda de MABE
 *
 * @author zeus
 */
public class SATAddendaMabe extends SATAddenda {

   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(SATAddendaMabe.class.getName());

   @Override
   public void FillAddenda(Addenda addenda, Comprobante objComp,
           String strPath, int intTransaccion, Conexion oConn) {
      String strMB_CODIGOPROVEEDOR = "";
      String strMB_CALLE = "";
      String strMB_PLANTA = "";
      String strMB_CODIGOPOSTAL = "";
      String strMB_NO_EXT = "";
      String strMB_NO_INT = "";
      String strMB_ORDENCOMPRA = "";
      String strMB_REFERENCIA1 = "";
      String strMB_REFERENCIA2 = "";
      /*Consultamos datos del docto*/
      String strSql = "SELECT MB_CODIGOPROVEEDOR,MB_CALLE,MB_PLANTA,"
              + "MB_CODIGOPOSTAL,MB_NO_EXT,MB_NO_INT, "
              + "MB_ORDENCOMPRA,MB_REFERENCIA1,MB_REFERENCIA2 "
              + " FROM vta_facturas WHERE FAC_ID = " + intTransaccion;
      try {
         ResultSet rs = oConn.runQuery(strSql);
         while (rs.next()) {
            strMB_CODIGOPROVEEDOR = rs.getString("MB_CODIGOPROVEEDOR");
            strMB_CALLE = rs.getString("MB_CALLE");
            strMB_PLANTA = rs.getString("MB_PLANTA");
            strMB_CODIGOPOSTAL = rs.getString("MB_CODIGOPOSTAL");
            strMB_NO_EXT = rs.getString("MB_NO_EXT");
            strMB_NO_INT = rs.getString("MB_NO_INT");
            strMB_ORDENCOMPRA = rs.getString("MB_ORDENCOMPRA");
            strMB_REFERENCIA1 = rs.getString("MB_REFERENCIA1");
            strMB_REFERENCIA2 = rs.getString("MB_REFERENCIA2");
         }
         //if(rs.getStatement() != null )rs.getStatement().close(); 
         rs.close();
      } catch (SQLException ex) {
         ex.fillInStackTrace();
      }
      //Instanciamos constructor de MABE
      ObjectFactory objFac = new ObjectFactory();
      //Objeto maestro de MABE
      Factura fact = objFac.createFactura();
      //Aqui llenamos los atributos que necesitamos
      fact.setVersion(BigDecimal.valueOf(1.0));
      fact.setTipoDocumento("FACTURA");
      if (objComp.getSerie() == null) {
         fact.setFolio(objComp.getFolio());
      } else {
         fact.setFolio(objComp.getSerie() + objComp.getFolio());
      }
      fact.setFecha(objComp.getFecha());
      fact.setOrdenCompra(strMB_ORDENCOMPRA);
      /*fact.setReferencia1(strMB_REFERENCIA1);
       fact.setReferencia2(strMB_REFERENCIA2);*/
      //moneda
      Moneda mone = objFac.createFacturaMoneda();
      mone.setTipoMoneda("MXN");
      fact.setMoneda(mone);
      //proveedor
      Proveedor prove = objFac.createFacturaProveedor();
      prove.setCodigo(strMB_CODIGOPROVEEDOR);
      fact.setProveedor(prove);
      //Entrega
      Entrega entrega = objFac.createFacturaEntrega();
      entrega.setCalle(strMB_CALLE);
      entrega.setPlantaEntrega(strMB_PLANTA);
      if (!strMB_CODIGOPOSTAL.isEmpty()) {
         entrega.setCodigoPostal(strMB_CODIGOPOSTAL);
      }
      if (!strMB_NO_INT.isEmpty()) {
         entrega.setNoInterior(strMB_NO_INT);
      }
      entrega.setNoExterior(strMB_NO_EXT);
      fact.setEntrega(entrega);
      //DETALLES
      Detalles deta = objFac.createFacturaDetalles();
      //lineItem
      Conceptos conceptos = objComp.getConceptos();
      Iterator<Concepto> it = conceptos.getConcepto().iterator();
      long lngConta = 0;
      while (it.hasNext()) {
         Concepto concepto = it.next();
         lngConta++;
         Detalle detalle = objFac.createFacturaDetallesDetalle();
         detalle.setNoLineaArticulo(BigInteger.valueOf(lngConta));
         //detalle.setCodigoArticulo(concepto.getNoIdentificacion());
         detalle.setCodigoArticulo("SRV");
         if (concepto.getDescripcion().length() > 50) {
            detalle.setDescripcion(concepto.getDescripcion().substring(0, 49));
         } else {
            detalle.setDescripcion(concepto.getDescripcion());
         }
         detalle.setUnidad("PZA");
         detalle.setCantidad(concepto.getCantidad());
         detalle.setPrecioSinIva(concepto.getValorUnitario());
         detalle.setImporteSinIva(concepto.getImporte());
         deta.getDetalle().add(detalle);

      }
      fact.setDetalles(deta);
      //Subtotal
      Subtotal sub = objFac.createFacturaSubtotal();
      sub.setImporte(objComp.getSubTotal());
      fact.setSubtotal(sub);
      //TRASLADOS
      Factura.Traslados tras = objFac.createFacturaTraslados();
      Factura.Traslados.Traslado traslado = objFac.createFacturaTrasladosTraslado();
      traslado.setTipo("IVA");
      traslado.setImporte((BigDecimal) objComp.getImpuestos().getTraslados().getTraslado().get(0).getImporte());
      traslado.setTasa(objComp.getImpuestos().getTraslados().getTraslado().get(0).getTasa());
      tras.getTraslado().add(traslado);
      fact.setTraslados(tras);
      //TOTAL
      Total tot = objFac.createFacturaTotal();
      tot.setImporte(objComp.getTotal());
      fact.setTotal(tot);
      addenda.getAny().add(fact);
   }

   public Factura FillAddenda(
           String strPath, int intTransaccion, Conexion oConn) {
      log.debug("Fill addenda MABE");
      String pattern = "yyyyddMM";
      SimpleDateFormat format = new SimpleDateFormat(pattern);
      String strMB_CODIGOPROVEEDOR = "";
      String strMB_CALLE = "";
      String strMB_PLANTA = "";
      String strMB_CODIGOPOSTAL = "";
      String strMB_NO_EXT = "";
      String strMB_NO_INT = "";
      String strMB_ORDENCOMPRA = "";
      String strMB_REFERENCIA1 = "";
      String strMB_REFERENCIA2 = "";
      String strFecha = "";
      String strFolio = "";
      double dblImporte = 0;
      double dblImpuesto = 0;
      double dblTotal = 0;
      double dblTasaImpuesto1 = 0;
      /*Consultamos datos del docto*/
      String strSql = "SELECT FAC_FECHA,FAC_FOLIO_C,FAC_IMPORTE,FAC_IMPUESTO1,FAC_TOTAL,FAC_TASA1,"
              + "MB_CODIGOPROVEEDOR,MB_CALLE,MB_PLANTA,"
              + "MB_CODIGOPOSTAL,MB_NO_EXT,MB_NO_INT, "
              + "MB_ORDENCOMPRA,MB_REFERENCIA1,MB_REFERENCIA2 "
              + " FROM vta_facturas WHERE FAC_ID = " + intTransaccion;
      try {
         ResultSet rs = oConn.runQuery(strSql);
         while (rs.next()) {
            strMB_CODIGOPROVEEDOR = rs.getString("MB_CODIGOPROVEEDOR");
            strMB_CALLE = rs.getString("MB_CALLE");
            strMB_PLANTA = rs.getString("MB_PLANTA");
            strMB_CODIGOPOSTAL = rs.getString("MB_CODIGOPOSTAL");
            strMB_NO_EXT = rs.getString("MB_NO_EXT");
            strMB_NO_INT = rs.getString("MB_NO_INT");
            strMB_ORDENCOMPRA = rs.getString("MB_ORDENCOMPRA");
            strMB_REFERENCIA1 = rs.getString("MB_REFERENCIA1");
            strMB_REFERENCIA2 = rs.getString("MB_REFERENCIA2");
            strFecha = rs.getString("FAC_FECHA");
            strFolio = rs.getString("FAC_FOLIO_C");
            dblImporte = rs.getDouble("FAC_IMPORTE");
            dblImpuesto = rs.getDouble("FAC_IMPUESTO1");
            dblTotal = rs.getDouble("FAC_TOTAL");
            dblTasaImpuesto1 = rs.getDouble("FAC_TASA1");
         }
         //if(rs.getStatement() != null )rs.getStatement().close(); rs.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage() + " " + ex.getSQLState());
      }
      //Instanciamos constructor de MABE
      ObjectFactory objFac = new ObjectFactory();
      //Objeto maestro de MABE
      Factura fact = objFac.createFactura();
      //Aqui llenamos los atributos que necesitamos
      fact.setVersion(BigDecimal.valueOf(1.0));
      fact.setTipoDocumento("FACTURA");
      fact.setFolio(strFolio);
      try {
         fact.setFecha(format.parse(strFecha));
      } catch (ParseException ex) {
         log.error(ex.getMessage());
      }

      fact.setOrdenCompra(strMB_ORDENCOMPRA);
      if (!strMB_REFERENCIA1.isEmpty()) {
         fact.setReferencia1(strMB_REFERENCIA1);
      }
      if (!strMB_REFERENCIA2.isEmpty()) {
         fact.setReferencia2(strMB_REFERENCIA2);
      }
      //moneda
      Moneda mone = objFac.createFacturaMoneda();
      mone.setTipoMoneda("MXN");
      fact.setMoneda(mone);
      //proveedor
      Proveedor prove = objFac.createFacturaProveedor();
      prove.setCodigo(strMB_CODIGOPROVEEDOR);
      fact.setProveedor(prove);
      //Entrega
      Entrega entrega = objFac.createFacturaEntrega();
      entrega.setCalle(strMB_CALLE);
      entrega.setPlantaEntrega(strMB_PLANTA);
      if (!strMB_CODIGOPOSTAL.isEmpty()) {
         entrega.setCodigoPostal(strMB_CODIGOPOSTAL);
      }
      if (!strMB_NO_INT.isEmpty()) {
         entrega.setNoInterior(strMB_NO_INT);
      }
      entrega.setNoExterior(strMB_NO_EXT);
      fact.setEntrega(entrega);
      //DETALLES
      Detalles deta = objFac.createFacturaDetalles();
      //lineItem

      try {
         strSql = "select * from vta_facturasdeta where FAC_ID = " + intTransaccion;
         ResultSet rs = oConn.runQuery(strSql);
         long lngConta = 0;
         while (rs.next()) {

            lngConta++;

            String strConcepto = rs.getString("FACD_DESCRIPCION");
            double dblCantidad = rs.getDouble("FACD_CANTIDAD");
            double FACD_IMPORTE = rs.getDouble("FACD_IMPORTE");
            double FACD_IMPUESTO1 = rs.getDouble("FACD_IMPUESTO1");
            double FACD_IMPORTESINIVA = 0;
            double FACD_VALORUNIT = 0;
            FACD_IMPORTESINIVA = FACD_IMPORTE - FACD_IMPUESTO1;
            FACD_VALORUNIT = FACD_IMPORTESINIVA / rs.getDouble("FACD_CANTIDAD");

            Detalle detalle = objFac.createFacturaDetallesDetalle();
            detalle.setNoLineaArticulo(BigInteger.valueOf(lngConta));
            //detalle.setCodigoArticulo(concepto.getNoIdentificacion());
            detalle.setCodigoArticulo("SRV");

            if (strConcepto.length() > 50) {
               detalle.setDescripcion(strConcepto.substring(0, 49));
            } else {
               detalle.setDescripcion(strConcepto);
            }
            detalle.setUnidad("PZA");
            detalle.setCantidad(new BigDecimal(dblCantidad));
            detalle.setPrecioSinIva(new BigDecimal(NumberString.FormatearDecimal(FACD_VALORUNIT, 2).replace(",", "")));
            detalle.setImporteSinIva(new BigDecimal(NumberString.FormatearDecimal(FACD_IMPORTESINIVA, 2).replace(",", "")));
            deta.getDetalle().add(detalle);



         }
         fact.setDetalles(deta);
         //if(rs.getStatement() != null )rs.getStatement().close(); rs.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage() + " " + ex.getSQLState());
      }
      //DESCUENTOS

      //Subtotal
      Subtotal sub = objFac.createFacturaSubtotal();

      sub.setImporte(new BigDecimal(dblImporte));
      fact.setSubtotal(sub);
      //TRASLADOS
      Factura.Traslados tras = objFac.createFacturaTraslados();
      Factura.Traslados.Traslado traslado = objFac.createFacturaTrasladosTraslado();
      traslado.setTipo("IVA");
      traslado.setImporte(new BigDecimal(NumberString.FormatearDecimal(dblImpuesto, 2).replace(",", "")));
      traslado.setTasa(new BigDecimal(dblTasaImpuesto1));
      tras.getTraslado().add(traslado);
      fact.setTraslados(tras);
      //TOTAL
      Total tot = objFac.createFacturaTotal();
      tot.setImporte(new BigDecimal(NumberString.FormatearDecimal(dblTotal, 2).replace(",", "")));
      fact.setTotal(tot);
      return fact;

   }

   /**
    * Convertimos objeto JAXB a nodo XML
    */
   private void jaxbObjectToXML(Factura fact, Node nodeParent) {
      //String strResult = "";
      try {
         JAXBContext context = JAXBContext.newInstance(Core.FirmasElectronicas.SAT_MABE.ObjectFactory.class);
         Marshaller m = context.createMarshaller();
         //for pretty-print XML in JAXB
         m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
         m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "http://recepcionfe.mabempresa.com/cfd/addenda/v1 http://recepcionfe.mabempresa.com/cfd/addenda/v1/mabev1.xsd");


//         m.setProperty("com.sun.xml.bind.namespacePrefixMapper", new NamespacePrefixMapper() {
//            @Override
//            public String[] getPreDeclaredNamespaceUris() {
//               return new String[]{"http://www.w3.org/2001/XMLSchema-instance"};
//            }
//
//            @Override
//            public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
//               log.debug("namespaceUri:" + namespaceUri + " suggestion:" + suggestion);
//               if (namespaceUri.equals("http://www.w3.org/2001/XMLSchema-instance")) {
//                  log.debug("es el que requerismo");
//                  return "xsi";
//               }
//
//               return suggestion;
//
//            }
//         });

         // Write to string
         StringWriter writer = new StringWriter();
         m.marshal(fact, writer);
         m.marshal(fact, nodeParent);

         System.out.println("writer " + writer.toString());
      } catch (JAXBException e) {
         log.error(e.getMessage());
      }
      //return strResult;
   }

   @Override
   public void makeNameSpaceDeclaration(String strPath, int intTransaccion, Conexion oConn) {
      log.debug("makeNameSpaceDeclaration MABE " + strPath);
      //Llenamos la addenda
      Factura factMABE = FillAddenda(
              strPath, intTransaccion, oConn);
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
                  log.debug("Convertimos objeto a xml");
                  Node addendaNode = doc.createElement("cfdi:Addenda");
                  //Generamos el detalle de la addenda
                  nodeParent.appendChild(addendaNode);
                  //Agregamos la addenda.
                  jaxbObjectToXML(factMABE, addendaNode);

               }
            }
            //Recorremos otra vez para poner el tag x
            for (int temp = 0; temp < nList.getLength(); temp++) {
               Node nNode = nList.item(temp);
               log.debug("__" + nNode.getNodeName());
               if (nNode.getNodeName().equals("cfdi:Addenda")) {
                  log.debug("Found cfdi:Addenda");
                  if (nNode.hasChildNodes()) {
                     NodeList nList2 = nNode.getChildNodes();
                     for (int temp2 = 0; temp2 < nList2.getLength(); temp2++) {
                        Node nNode2 = nList2.item(temp2);
                        if (nNode2.getNodeName().equals("mabe:Factura")) {
                           log.debug("Found mabe:Factura");
                           if (nNode2.getNodeType() == Node.ELEMENT_NODE) {
                              log.debug("Hacemos cambio...");
                              Element eElement = (Element) nNode2;
                              eElement.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
//                              Element eElement = (Element) nNode2;
//                              eElement.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:schemaLocation", "http://recepcionfe.mabempresa.com/cfd/addenda/v1 http://recepcionfe.mabempresa.com/cfd/addenda/v1/mabev1.xsd");
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
