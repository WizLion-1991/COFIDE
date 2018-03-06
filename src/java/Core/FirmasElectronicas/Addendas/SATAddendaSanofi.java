/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Core.FirmasElectronicas.Addendas;

import comSIWeb.Operaciones.Conexion;
import java.io.File;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
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
 *
 * @author siweb
 */
public class SATAddendaSanofi extends SATAddenda {

    // <editor-fold defaultstate="collapsed" desc="Propiedades">
    private String strNumProveedor;
    private String strNumODC;
    private String strNumSolicitud;
    private String strNumCuentaPuente;
    private double dblRetencion;
    private double dblTotal;
    private int intContador;
    ArrayList<SATAddendaSanofiDeta> addendaDeta = new ArrayList<SATAddendaSanofiDeta>();
    DecimalFormat df = new DecimalFormat("#.###");

    public void setDblTotal(double dblTotal) {
        this.dblTotal = dblTotal;
    }

    public void setaddendaDeta(ArrayList<SATAddendaSanofiDeta> addendaDeta) {
        this.addendaDeta = addendaDeta;
    }

    public ArrayList<SATAddendaSanofiDeta> getaddendaDeta() {
        return addendaDeta;
    }

    public double getDblRetencion() {
        return dblRetencion;
    }

    public void setDblRetencion(double dblRetencion) {
        this.dblRetencion = dblRetencion;
    }

    public void setStrEmail1(String strEmail1) {
        this.strEmail1 = strEmail1;
    }

    public void setStrMoneda(String strMoneda) {
        this.strMoneda = strMoneda;
    }

    public double getDblTotal() {
        return dblTotal;
    }

    public String getStrEmail1() {
        return strEmail1;
    }

    public String getStrMoneda() {
        return strMoneda;
    }
    private String strEmail1;
    private String strMoneda;

    public String getStrNumProveedor() {
        return strNumProveedor;
    }

    public String getStrNumODC() {
        return strNumODC;
    }

    public void setStrNumProveedor(String strNumProveedor) {
        this.strNumProveedor = strNumProveedor;
    }

    public void setStrNumODC(String strNumODC) {
        this.strNumODC = strNumODC;
    }

    public void setStrNumSolicitud(String strNumSolicitud) {
        this.strNumSolicitud = strNumSolicitud;
    }

    public void setStrNumCuentaPuente(String strNumCuentaPuente) {
        this.strNumCuentaPuente = strNumCuentaPuente;
    }

    public String getStrNumSolicitud() {
        return strNumSolicitud;
    }

    public String getStrNumCuentaPuente() {
        return strNumCuentaPuente;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Metodos">

    @Override
    public void makeNameSpaceDeclaration(String strPath, int intTransaccion, Conexion oConn) {
        GetData(intTransaccion, oConn);
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
                        Element nodeMain = doc.createElement("sanofi:sanofi");
                        addendaNode.appendChild(nodeMain);

                        //Nodo Documento
                        Element nodeDoc = doc.createElement("sanofi:Documento");
                        nodeMain.appendChild(nodeDoc);

                        //Nodo header
                        Element nodeHeader = doc.createElement("sanofi:header");
                        nodeDoc.appendChild(nodeHeader);

                        Element tipoDoc = doc.createElement("sanofi:TIPO_DOCTO");
                        tipoDoc.appendChild(doc.createTextNode("01"));
                        nodeHeader.appendChild(tipoDoc);

                        //Num Orden de Compra
                        Element numODC = doc.createElement("sanofi:NUM_ORDEN");
                        if (this.strNumODC.isEmpty()) {
                            numODC.appendChild(doc.createTextNode("0000000000"));
                        } else {
                            numODC.appendChild(doc.createTextNode(this.strNumODC));
                        }
                        nodeHeader.appendChild(numODC);
                        //Num proveedor
                        Element numProv = doc.createElement("sanofi:NUM_PROVEEDOR");
                        if (this.strNumProveedor.isEmpty()) {
                            numProv.appendChild(doc.createTextNode("0000000000"));
                        } else {
                            numProv.appendChild(doc.createTextNode(this.strNumProveedor));
                        }
                        nodeHeader.appendChild(numProv);

                        //node FCTCONV
                        Element fctConv = doc.createElement("sanofi:FCTCONV");
                        fctConv.appendChild(doc.createTextNode("1.000"));
                        nodeHeader.appendChild(fctConv);

                        //node MONEDA
                        Element nodeMoneda = doc.createElement("sanofi:MONEDA");
                        nodeMoneda.appendChild(doc.createTextNode("MXN"));
                        nodeHeader.appendChild(nodeMoneda);

                        //node IMP_RETENCION
                        Element nodeRetencion = doc.createElement("sanofi:IMP_RETENCION");
                        String nvaRetencion = new DecimalFormat("#.##").format(this.dblRetencion);
                        nodeRetencion.appendChild(doc.createTextNode(nvaRetencion));
                        nodeHeader.appendChild(nodeRetencion);

                        //node IMP_TOTAL
                        Element nodeTotal = doc.createElement("sanofi:IMP_TOTAL");
                        String nvoTotal = new DecimalFormat("#.##").format(this.dblTotal);
                        nodeTotal.appendChild(doc.createTextNode(nvoTotal));
                        nodeHeader.appendChild(nodeTotal);

                        //OBSERVACIONES
                        Element nodeObservaciones = doc.createElement("sanofi:OBSERVACIONES");
                        nodeObservaciones.appendChild(doc.createTextNode(""));
                        nodeHeader.appendChild(nodeObservaciones);

                        //node IMP_TOTAL
                        Element nodeEmail = doc.createElement("sanofi:CTA_CORREO");
                        nodeEmail.appendChild(doc.createTextNode(this.strEmail1));
                        nodeHeader.appendChild(nodeEmail);

                        //node DISPONIBLE 1
                        Element nodeDisp1 = doc.createElement("sanofi:DISPONIBLE_1");
                        nodeDisp1.appendChild(doc.createTextNode("0.00"));
                        nodeHeader.appendChild(nodeDisp1);

                        //node DISPONIBLE 2
                        Element nodeDisp2 = doc.createElement("sanofi:DISPONIBLE_2");
                        nodeDisp2.appendChild(doc.createTextNode("0.00"));
                        nodeHeader.appendChild(nodeDisp2);

                        //node DISPONIBLE 3
                        Element nodeDisp3 = doc.createElement("sanofi:DISPONIBLE_3");
                        nodeDisp3.appendChild(doc.createTextNode("0.00"));
                        nodeHeader.appendChild(nodeDisp3);

                        //node DISPONIBLE 4
                        Element nodeDisp4 = doc.createElement("sanofi:DISPONIBLE_4");
                        nodeDisp4.appendChild(doc.createTextNode("0.00"));
                        nodeHeader.appendChild(nodeDisp4);

                        //Nodo details
                        Element nodeDetails = doc.createElement("sanofi:details");
                        nodeDoc.appendChild(nodeDetails);

                        //Query para Facturas Deta
                        GetFacDetalle(intTransaccion, oConn);

                        Iterator<SATAddendaSanofiDeta> it = addendaDeta.iterator();
                        while (it.hasNext()) {
                            intContador += 1;
                                                        
                            //Nodo details

                            SATAddendaSanofiDeta addendaDeta = it.next();
                            
                            //node NUMERO DE LINEA
                            Element nodeNumLinea = doc.createElement("sanofi:NUM_LINEA");
                            nodeNumLinea.appendChild(doc.createTextNode(addendaDeta.getStrPosicion()));
                            nodeDetails.appendChild(nodeNumLinea);
                            
                            //Num Solicitud
                            Element numSolic = doc.createElement("sanofi:NUM_ENTRADA");
                            if (this.strNumSolicitud.isEmpty()) {
                                numSolic.appendChild(doc.createTextNode("0000000000"));
                            } else {
                                numSolic.appendChild(doc.createTextNode(this.strNumSolicitud));
                            }
                            nodeDetails.appendChild(numSolic);

                            //node CUENTA_PUENTE
                            Element numCtaPte = doc.createElement("sanofi:CUENTA_PUENTE");
                            if (this.strNumCuentaPuente.isEmpty()) {
                                numCtaPte.appendChild(doc.createTextNode("0000000000"));
                            } else {
                                numCtaPte.appendChild(doc.createTextNode(this.strNumCuentaPuente));
                            }
                            nodeDetails.appendChild(numCtaPte);

                            //node CANTIDAD
                            Element nodeCantidad = doc.createElement("sanofi:UNIDADES");
                            String strCantidad = Integer.toString(addendaDeta.getIntCantidad());
                            nodeCantidad.appendChild(doc.createTextNode(strCantidad));
                            nodeDetails.appendChild(nodeCantidad);
                            
                            //node PRECIO_UNITARIO
                            Element nodePreico = doc.createElement("sanofi:PRECIO_UNITARIO");
                            double precxPieza = (addendaDeta.getDblPrecio() )/ (1 +(addendaDeta.getDblTasaIva()/100));
                            String strPrecio = new DecimalFormat("#.##").format(precxPieza);
                            nodePreico.appendChild(doc.createTextNode(strPrecio));
                            nodeDetails.appendChild(nodePreico);
                            
                            //node IMPORTE
                            Element nodeImporte = doc.createElement("sanofi:IMPORTE");
                            double dblImporte = precxPieza * addendaDeta.getIntCantidad();
                            String strImporteTmp = new DecimalFormat("#.##").format(dblImporte);
                            nodeImporte.appendChild(doc.createTextNode( strImporteTmp));
                            nodeDetails.appendChild(nodeImporte);
                            
                            //node UNIDAD_MEDIDA
                            Element nodeUMedida = doc.createElement("sanofi:UNIDAD_MEDIDA");
                            if (addendaDeta.getStrUMedida().isEmpty()) {
                            nodeUMedida.appendChild(doc.createTextNode(" "));
                            } else {
                            nodeUMedida.appendChild(doc.createTextNode(addendaDeta.getStrUMedida()));
                            }
                            nodeDetails.appendChild(nodeUMedida);
                            
                            //node TASA_IVA
                            Element nodeTasaIva = doc.createElement("sanofi:TASA_IVA");
                            String strTasaIva = Double.toString(addendaDeta.getDblTasaIva());
                            nodeTasaIva.appendChild(doc.createTextNode(strTasaIva));
                            nodeDetails.appendChild(nodeTasaIva);
                            
                            //node IMPORTE_IVA
                            Element nodeImpIva = doc.createElement("sanofi:IMPORTE_IVA");
                            String strImpIva = Double.toString(addendaDeta.getDblImpuesto1());
                            nodeImpIva.appendChild(doc.createTextNode(strImpIva));
                            nodeDetails.appendChild(nodeImpIva);
                            
                            //node DISPONIBLE 1
                            Element nodeDisp1Deta = doc.createElement("sanofi:DISPONIBLE_1");
                            nodeDisp1Deta.appendChild(doc.createTextNode("0.00"));
                            nodeDetails.appendChild(nodeDisp1Deta);

                            //node DISPONIBLE 2
                            Element nodeDisp2Deta = doc.createElement("sanofi:DISPONIBLE_2");
                            nodeDisp2Deta.appendChild(doc.createTextNode("0.00"));
                            nodeDetails.appendChild(nodeDisp2Deta);

                            //node DISPONIBLE 3
                            Element nodeDisp3Deta = doc.createElement("sanofi:DISPONIBLE_3");
                            nodeDisp3Deta.appendChild(doc.createTextNode("0.00"));
                            nodeHeader.appendChild(nodeDisp3Deta);

                            //node DISPONIBLE 4
                            Element nodeDisp4Deta = doc.createElement("sanofi:DISPONIBLE_4");
                            nodeDisp4Deta.appendChild(doc.createTextNode("0.00"));
                            nodeDetails.appendChild(nodeDisp4Deta);

                            //node DISPONIBLE 5
                            Element nodeDisp5Deta = doc.createElement("sanofi:DISPONIBLE_5");
                            nodeDisp5Deta.appendChild(doc.createTextNode("0.00"));
                            nodeDetails.appendChild(nodeDisp5Deta);

                            //node DISPONIBLE 6
                            Element nodeDisp6Deta = doc.createElement("sanofi:DISPONIBLE_6");
                            nodeDisp6Deta.appendChild(doc.createTextNode("0.00"));
                            nodeDetails.appendChild(nodeDisp6Deta);

                        }//Fin WHILE

                        //NoVersAdd
                        nodeMain.setAttribute("xmlns:sanofi", "https://mexico.sanofi.com/schemas");
                        nodeMain.setAttribute("xsi:schemaLocation", "https://mexico.sanofi.com/schemas https://mexico.sanofi.com/schemas/sanofi.xsd");
                        nodeMain.setAttribute("version", "1.0");

                    }//Fin IF cfdi:Complemento
                }//Fin FOR
            }//Fin IF nList != null
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
        String strSql = "select vta_facturas.SNF_NUM_ODC,vta_facturas.SNF_NUM_PROV,vta_facturas.SNF_CUENTA_PUENTE,"
           + "vta_facturas.SNF_NUM_SOL,vta_facturas.FAC_MONEDA,vta_facturas.FAC_TOTAL,"
           + "(vta_facturas.FAC_RETISR + vta_facturas.FAC_RETIVA) as RETENCION,vta_facturas.CT_ID,vta_cliente.CT_EMAIL1,"
           + "vta_monedas.MON_DESCRIPCION, vta_monedas.MON_SIGLAS "
           + "from vta_facturas inner join vta_cliente on vta_facturas.CT_ID = vta_cliente.CT_ID "
           + "inner join vta_monedas on vta_facturas.FAC_MONEDA = vta_monedas.MON_ID"
           + " where FAC_ID = " + intTransaccion;

        try {
            ResultSet rs = oConn.runQuery(strSql);
            while (rs.next()) {
                this.strNumProveedor = rs.getString("SNF_NUM_PROV");
                this.strNumODC = rs.getString("SNF_NUM_ODC");
                this.strNumSolicitud = rs.getString("SNF_NUM_SOL");
                this.strNumCuentaPuente = rs.getString("SNF_CUENTA_PUENTE");
                this.dblRetencion = rs.getDouble("RETENCION");
                this.dblTotal = rs.getDouble("FAC_TOTAL");
                this.strEmail1 = rs.getString("CT_EMAIL1");
                this.strMoneda = rs.getString("MON_SIGLAS");
            }
        } catch (SQLException ex) {
            Logger.getLogger(SATAddendaSanofi.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//Fin GetData
    // </editor-fold>

    private void GetFacDetalle(int intTransaccion, Conexion oConn) {
        String strDeta = "select vta_facturasdeta.FACD_PRECIO, vta_facturasdeta.FACD_CANTIDAD, vta_facturasdeta.FACD_TASAIVA1,"
           + "vta_facturasdeta.FACD_IMPUESTO1,FACD_UNIDAD_MEDIDA, vta_facturasdeta.FACD_IMPORTE,PR_POSICION "
           + "from vta_facturasdeta inner join vta_producto on vta_facturasdeta.PR_ID = vta_producto.PR_ID "
           + "where vta_facturasdeta.FAC_ID = " + intTransaccion;
        try {
            ResultSet rs = oConn.runQuery(strDeta);
            while (rs.next()) {
                SATAddendaSanofiDeta addSanofi = new SATAddendaSanofiDeta();
                addSanofi.setDblPrecio(rs.getDouble("FACD_PRECIO"));
                addSanofi.setIntCantidad(rs.getInt("FACD_CANTIDAD"));
                addSanofi.setDblTasaIva(rs.getDouble("FACD_TASAIVA1"));
                addSanofi.setDblImpuesto1(rs.getDouble("FACD_IMPUESTO1"));
                addSanofi.setStrUMedida(rs.getString("FACD_UNIDAD_MEDIDA"));
                if(rs.getString("FACD_UNIDAD_MEDIDA").isEmpty()){
                   addSanofi.setStrUMedida("UN");
                }
                addSanofi.setDblImporte(rs.getDouble("FACD_IMPORTE"));
                addSanofi.setStrPosicion(rs.getString("PR_POSICION"));
                addendaDeta.add(addSanofi);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SATAddendaSanofi.class.getName()).log(Level.SEVERE, null, ex);
        }
 
    }//Fin GetFacDetalle

    // <editor-fold defaultstate="collapsed" desc="Constructores">
    public SATAddendaSanofi() {
    }
   // </editor-fold>
}
