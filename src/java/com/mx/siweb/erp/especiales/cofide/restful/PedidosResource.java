///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.mx.siweb.erp.especiales.cofide.restful;
//
////import com.mx.siweb.erp.restful.entity.Pedidos;
////import com.mx.siweb.erp.restful.entity.PedidosDeta;
//import com.mx.siweb.erp.restful.entity.DirEntrega;
//import javax.servlet.http.HttpServletRequest;
//import javax.ws.rs.core.Context;
//import javax.ws.rs.core.UriInfo;
//import javax.ws.rs.Consumes;
//import javax.ws.rs.DefaultValue;
//import javax.ws.rs.Path;
//import javax.ws.rs.GET;
//import javax.ws.rs.Produces;
//import javax.ws.rs.QueryParam;
//import javax.ws.rs.core.Response;
//import org.apache.logging.log4j.LogManager;
//import comSIWeb.Operaciones.Reportes.CIP_Formato;
//import comSIWeb.Operaciones.Formatos.Formateador;
//import comSIWeb.Operaciones.Reportes.PDFEventPage;
//import java.io.FileOutputStream;
//import com.itextpdf.text.pdf.PdfWriter;
//import com.itextpdf.text.Document;
//import com.itextpdf.text.DocumentException;
//import java.io.FileNotFoundException;
//import comSIWeb.Utilerias.Mail;
//import java.sql.SQLException;
//import Tablas.vta_cliente_dir_entrega;
//import java.util.Iterator;
//import java.sql.ResultSet;
//import comSIWeb.ContextoApt.VariableSession;
//import comSIWeb.Utilerias.Fechas;
//import Tablas.vta_ticketsdeta;
//import Tablas.vta_facturadeta;
//import Tablas.vta_cotizadeta;
//import comSIWeb.Operaciones.Conexion;
//import comSIWeb.Operaciones.TableMaster;
//import ERP.Ticket;
//import Tablas.CofideParticipantes;
//import Tablas.vta_facturas;
//import Tablas.vta_pedidosdeta;
//import com.mx.siweb.erp.especiales.cofide.COFIDE_Mail_cursos;
//import com.mx.siweb.erp.especiales.cofide.CRM_Envio_Template;
//import com.mx.siweb.erp.especiales.cofide.restful.entidades.Participantes;
//import com.mx.siweb.erp.especiales.cofide.restful.entidades.Pedidos;
//import com.mx.siweb.erp.especiales.cofide.restful.entidades.PedidosDeta;
//import com.mx.siweb.erp.especiales.cofide.vta_clientes;
//import com.mx.siweb.erp.restful.EvalSesion;
//import com.mx.siweb.ui.web.Site;
//import comSIWeb.Utilerias.UtilXml;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.Locale;
//import javax.ws.rs.POST;
//
///**
// * REST Web Service
// *
// * @author ZeusGalindo
// */
//@Path("pedidos")
//public class PedidosResource {
//
//    @Context
//    private UriInfo context;
//    @Context
//    private HttpServletRequest servletRequest;
//    @Context
//    private javax.servlet.ServletContext servletContext;
//    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(PedidosResource.class.getName());
//    String strUsuario = "";
//    String strContraseña = "";
//    Fechas fec = new Fechas();
//    COFIDE_Mail_cursos mg = new COFIDE_Mail_cursos();
//
//    /**
//     * Creates a new instance of PedidosResource
//     */
//    public PedidosResource() {
//    }
//
//    public String getStrUsuario() {
//        return strUsuario;
//    }
//
//    public void setStrUsuario(String strUsuario) {
//        this.strUsuario = strUsuario;
//    }
//
//    public String getStrContraseña() {
//        return strContraseña;
//    }
//
//    public void setStrContraseña(String strContraseña) {
//        this.strContraseña = strContraseña;
//    }
//
//    /**
//     * Retrieves representation of an instance of
//     * com.mx.siweb.erp.restful.PedidosResource
//     *
//     * @return an instance of com.mx.siweb.erp.restful.entity.Pedidos
//     */
//    @GET
//    @Produces({"application/json"})
//    public Pedidos getJson(@DefaultValue("0") @QueryParam("idPedido") String idPedido, @DefaultValue("") @QueryParam("Codigo") String strCodigo) {
//        Pedidos pedido = new Pedidos();
//        //Accedemos a la base de datos para ver si tenemos acceso
//        VariableSession varSesiones = new VariableSession(servletRequest);
//        varSesiones.getVars();
//
//        try {
//            //Abrimos la conexion
//            Conexion oConn = new Conexion(varSesiones.getStrUser(), servletContext);
//            oConn.open();
//
//            //Validamos si tenemos acceso
//            EvalSesion eval = new EvalSesion();
//            if (eval.evaluaSesion(strCodigo, oConn)) {
//                if (eval.evaluaPermiso(556, oConn) || eval.isBolEsCliente()) {
//                    pedido.setIdProducto(232);
//                    PedidosDeta deta = new PedidosDeta();
//                    deta.setCantidad(2);
//                    deta.setCodigo("330303");
//                    pedido.getLstItems().add(deta);
//                }
//
//            } else {
//                pedido.setNotas("Error:Access Denied");
//            }
//
//            oConn.close();
//        } catch (Exception ex) {
//            log.error("MobilServiceLogin1" + ex.getMessage() + " " + ex.getLocalizedMessage());
//        }
//        return pedido;
//    }
//
//    /**
//     * Retrieves representation of an instance of
//     * com.mx.siweb.erp.restful.CambioPasswordResource
//     *
//     * @return an instance of java.lang.String
//     */
//    /**
//     * PUT method for updating or creating an instance of CambioPasswordResource
//     *
//     * @param content representation for the resource
//     * @return an HTTP response with content of the updated or created resource.
//     */
//    @POST
//    @Consumes({"application/json"})
//    public Response putJson(Pedidos pedidos) {
//        //Accedemos a la base de datos para ver si tenemos acceso
//        VariableSession varSesiones = new VariableSession(servletRequest);
//        varSesiones.getVars();
//        Fechas fecha = new Fechas();
////        vta_clientes cte = new vta_clientes();
//        String strRes = "";
//        log.debug("//////////// OBJETO JSON: ////////////// ");
//        log.debug("///////////// Fecha: " + fecha.getFechaActual() + " ////////////");
//        log.debug("//////////// Hora: " + fecha.getHoraActual() + " ////////////");
//        log.debug("//////////// EMAIL: " + pedidos.getEmail1() + " ////////////");
//        log.debug("//////////// CP: " + pedidos.getCtCp() + " ////////////");
//        log.debug("//////////// Razon Social: " + pedidos.getCtRazonSocial() + " ////////////");
//        log.debug("//////////// RFC: " + pedidos.getCtRFC() + " ////////////");
//        log.debug("//////////// CLIENTE   : " + pedidos.getIdCliente() + " ////////////");
//        log.debug("//////////// OBJETO JSON: ////////////// ");
//        try {
//            //Abrimos la conexion
//            Conexion oConn = new Conexion(varSesiones.getStrUser(), servletContext);
//            oConn.open();
//            ResultSet rs = null;
//            String strConsulta = "";
//            int intCt_Id = 0;
//            int moneda = 0;
//            String strIdNewUser = "";
//            int intIdVta = 0;
//            int intTipoCurso = 0;
//            int tmpPrId = 0;
//            String strCodigo = "";
//            String strPrecioVta = "0.00";
//
//            if (pedidos.getIdCliente() != 0) {
//                intCt_Id = pedidos.getIdCliente();
//            }
//            //Validamos si tenemos acceso
//            EvalSesion eval = new EvalSesion();
//            if (eval.evaluaSesion(pedidos.getCodigoAcceso(), oConn)) {
//                Site webBase = new Site(oConn);
//                if (eval.evaluaPermiso(556, oConn) || eval.isBolEsCliente()) { //si accesa, empieza a recibir información del JSON
//                    String strPedRazonSocial = pedidos.getCtRazonSocial();
//                    boolean blExisteCt = false; //no existe el cliente     
//                    // valida si el id del cleinte viene en cero
//                    log.debug("//////////// VALIDA CTE SI ES = 0 ////////////// ");
//                    if (pedidos.getIdCliente() == 0) {
//                        //Primer validacion en la tabla de clientes.
//                        log.debug("//////////// BUSCA AL CTE POR CORREO EN VTA_CLIENTE || COFIDE_CONTACTOS ////////////// ");
//                        strConsulta = "select CT_ID from vta_cliente "
//                                + "where CT_EMAIL1 = '" + pedidos.getEmail1() + "' or "
//                                + "CT_ID in (select CT_ID from cofide_contactos where CCO_CORREO = '" + pedidos.getEmail1() + "') limit 1";
//                        rs = oConn.runQuery(strConsulta, true);
//                        while (rs.next()) {
//                            log.debug("//////////// SE ENCONTRO AL CTE ////////////// ");
//                            pedidos.setIdCliente(rs.getInt("CT_ID")); //tomamos el ID
//                            intCt_Id = rs.getInt("CT_ID"); //tomamos el ID
//                            blExisteCt = true; //existe el cliente-
//                        }
//                        rs.close();
//                        log.debug("//////////// DATOS FISCALES ////////////// " + pedidos.getCtCalle() + " / " + pedidos.getCtColonia() + " / " + pedidos.getCtMunicipio() + " / " + pedidos.getCtNumero() + " / " + pedidos.getCtNumInterior());
//                        if (!blExisteCt) {
//                            log.debug("//////////// NO EXISTE EL CLIENTE, SE VA A CREAR UNO NUEVO ////////////// ");
//                            vta_clientes cte = new vta_clientes();
//                            //crear idcte                            
//                            cte.setFieldString("CT_RAZONSOCIAL", pedidos.getCtRazonSocial());
//                            cte.setFieldString("CT_RFC", pedidos.getCtRFC());
//                            cte.setFieldString("CT_CALLE", pedidos.getCtCalle());
//                            cte.setFieldString("CT_COLONIA", pedidos.getCtColonia());
//                            cte.setFieldString("CT_MUNICIPIO", pedidos.getCtMunicipio());
//                            cte.setFieldString("CT_ESTADO", pedidos.getCtEstado());
//                            cte.setFieldString("CT_CP", pedidos.getCtCp());
//                            cte.setFieldString("CT_EMAIL1", pedidos.getEmail1());
//                            cte.setFieldString("CT_NUMERO", pedidos.getCtNumero());
//                            cte.setFieldString("CT_NUMINT", pedidos.getCtNumInterior());
//                            cte.setFieldString("CT_FECHAREG", fecha.getFechaActual());
//                            cte.setFieldInt("EMP_ID", 1);
//                            cte.setFieldInt("SC_ID", 1);
//                            cte.setFieldInt("PA_ID", 0);
//                            cte.setFieldInt("CT_ACTIVO", 1);
//                            cte.setFieldInt("CT_ES_PROSPECTO", 0);
//                            cte.Agrega(oConn);
//                            intCt_Id = Integer.parseInt(cte.getValorKey());
//                            pedidos.setIdCliente(intCt_Id);
//                        } else { //fin de si existe el cliente
//                            log.debug("//////////// EXISTE EL CLIENTE, SE ACTUALIZA SU INFORMACIÓN ////////////// ");
//                            vta_clientes cte = new vta_clientes();
//                            cte.ObtenDatos(intCt_Id, oConn);
//                            if (!cte.getFieldString("CT_RFC").equals(pedidos.getCtRFC())) {
//                                strConsulta = "insert into cofide_rfc (CRFC_CT_ID, CRFC_RFC) values ('" + intCt_Id + "','" + cte.getFieldString("CT_RFC") + "')";
//                                oConn.runQueryLMD(strConsulta);
//                            }
//                            cte.setFieldString("CT_RAZONSOCIAL", pedidos.getCtRazonSocial());
//                            cte.setFieldString("CT_RFC", pedidos.getCtRFC());
//                            cte.setFieldString("CT_CALLE", pedidos.getCtCalle());
//                            cte.setFieldString("CT_COLONIA", pedidos.getCtColonia());
//                            cte.setFieldString("CT_CP", pedidos.getCtCp());
//                            cte.setFieldString("CT_MUNICIPIO", pedidos.getCtMunicipio());
//                            cte.setFieldString("CT_ESTADO", pedidos.getCtEstado());
//                            cte.setFieldString("CT_NUMERO", pedidos.getCtNumero());
//                            cte.setFieldString("CT_NUMINT", pedidos.getCtNumInterior());
//                            cte.setFieldString("CT_EMAIL1", pedidos.getEmail1());
//                            cte.Modifica(oConn);
//                            pedidos.setIdCliente(intCt_Id);
//                        }
//                    } else { //fin de cleinte == 0
//                        log.debug("//////////// EL ID NO VENIA EN CERO, SE ACTUALIZA SU INFORMACIÓN ////////////// ");
//                        log.debug("//////////// RAZON SOCIAL RECIBIDA: " + pedidos.getCtRazonSocial() + "  ////////////// ");
//                        vta_clientes cte = new vta_clientes();
//                        cte.ObtenDatos(intCt_Id, oConn);
//                        if (!cte.getFieldString("CT_RFC").equals(pedidos.getCtRFC())) {
//                            strConsulta = "insert into cofide_rfc (CRFC_CT_ID, CRFC_RFC) values ('" + intCt_Id + "','" + cte.getFieldString("CT_RFC") + "')";
//                            oConn.runQueryLMD(strConsulta);
//                        }
//                        cte.setFieldString("CT_RAZONSOCIAL", pedidos.getCtRazonSocial());
//                        cte.setFieldString("CT_RFC", pedidos.getCtRFC());
//                        cte.setFieldString("CT_CALLE", pedidos.getCtCalle());
//                        cte.setFieldString("CT_COLONIA", pedidos.getCtColonia());
//                        cte.setFieldString("CT_CP", pedidos.getCtCp());
//                        cte.setFieldString("CT_MUNICIPIO", pedidos.getCtMunicipio());
//                        cte.setFieldString("CT_ESTADO", pedidos.getCtEstado());
//                        cte.setFieldString("CT_NUMERO", pedidos.getCtNumero());
//                        cte.setFieldString("CT_NUMINT", pedidos.getCtNumInterior());
//                        cte.setFieldString("CT_EMAIL1", pedidos.getEmail1());
//                        cte.Modifica(oConn);
//                        pedidos.setIdCliente(intCt_Id);
//                    }
//                    log.debug("//////////// TERMINO DE CREAR Y OBTENER EL ID CLIENTE ////////////// ");
//                    String strPathXML = servletContext.getInitParameter("PathXml");
//                    String strfolio_GLOBAL = servletContext.getInitParameter("folio_GLOBAL");
//                    String strmod_Inventarios = servletContext.getInitParameter("mod_Inventarios");
//                    String strSist_Costos = servletContext.getInitParameter("SistemaCostos");
//                    String strPathPrivateKeys = servletContext.getInitParameter("PathPrivateKey");
//                    String strPathFonts = servletContext.getRealPath("/") + System.getProperty("file.separator") + "fonts";
//                    if (strfolio_GLOBAL == null) {
//                        strfolio_GLOBAL = "SI";
//                    }
//                    if (strmod_Inventarios == null) {
//                        strmod_Inventarios = "NO";
//                    }
//                    if (strSist_Costos == null) {
//                        strSist_Costos = "0";
//                    }
//                    //Instanciamos el objeto que generara la venta
//                    Ticket ticket = new Ticket(oConn, varSesiones, servletRequest);
//                    ticket.setStrPATHKeys(strPathPrivateKeys);
//                    ticket.setStrPATHXml(strPathXML);
//                    ticket.setStrPATHFonts(strPathFonts);
//                    //Desactivamos inventarios
//                    log.debug("//////////// VALIDAR, SI ES NECESARIO EL INVENTARIO: (" + strmod_Inventarios + ") ////////////// ");
//                    if (strmod_Inventarios.equals("NO")) {
//                        ticket.setBolAfectaInv(false);
//                        log.debug("ticket.setBolAfectaInv(false)");
//                    } else {
//                        ticket.setBolAfectaInv(true);
//                        log.debug("ticket.setBolAfectaInv(true)");
//                    }
//                    //Validamos si envian la peticion con la bandera de afectar inventarios                                        
//                    try {
//                        log.debug("//////////// VALIDAR, SI ES NECESARIO EL SISTEMA DE COSTOS ////////////// ");
//                        ticket.setIntSistemaCostos(Integer.valueOf(strSist_Costos));
//                        log.debug("(//////////// El sistema de costos es " + Integer.valueOf(strSist_Costos));
//                    } catch (NumberFormatException ex) {
//                        log.error("No hay sistema de costos definido");
//                    }
//                    //Recibimos parametros
//                    String strPrefijoMaster = "TKT";
//                    String strPrefijoDeta = "TKTD";
//                    String strTipoVtaNom = Ticket.TICKET;
//                    String strTipoFormat = "TICKET";
//                    String strNomTemplate = "TICKET_MAI";
//                    String strDoc = "T"; //tipo de documento
//                    //Recuperamos el tipo de venta 1:FACTURA 2:TICKET 3:PEDIDO
//                    log.debug("//////////// INICIA LA VENTA, VALIDA EL TIPO DE VENTA QUE SE REALIZARA, TICKET O FACTURA ////////////// ");
//                    String strTipoVta = Integer.toString(pedidos.getTipoVenta());
//                    if (strTipoVta == null) {
//                        log.debug("//////////// SE REALIZARA UN TICKET PORQUE VENIA NULL EL VALOR////////////// ");
//                        strTipoVta = "2";
//                    }
//                    if (strTipoVta.equals("1")) {
//                        log.debug("//////////// SE REALIZARA UNA FACTURA ////////////// ");
//                        strPrefijoMaster = "FAC";
//                        strPrefijoDeta = "FACD";
//                        strTipoVtaNom = Ticket.FACTURA;
//                        ticket.initMyPass(servletContext);
//                        strTipoFormat = "FACTURA";
//                        strNomTemplate = "FACTURA";
//                        strDoc = "F";
//                    }
//                    if (strTipoVta.equals("2")) {
//                        log.debug("//////////// SE REALIZARA UN TICKET ////////////// ");
//                        strPrefijoMaster = "TKT";
//                        strPrefijoDeta = "TKTD";
//                        strTipoVtaNom = Ticket.TICKET;
//                        strTipoFormat = "TICKET";
//                        strNomTemplate = "TICKET_MAI";
//                        strDoc = "T";
//                    }
//                    ticket.setStrTipoVta(strTipoVtaNom);
//                    //Validamos si tenemos un empresa seleccionada
//                    if (webBase.getIntEMP_ID() != 0) {
//                        //Asignamos la empresa seleccionada
//                        ticket.setIntEMP_ID(webBase.getIntEMP_ID());
//                        log.debug("//////////// LA EMPRESA ES: (" + webBase.getIntEMP_ID() + "), validar si es necesaria esta validación ////////////// ");
//                    }
//                    //Validamos si usaremos un folio global                   
//                    log.debug("//////////// VALIDACIÓN DE FOLIO GLOBAL ////////////// ");
//                    if (strfolio_GLOBAL.equals("NO")) {
//                        ticket.setBolFolioGlobal(false);
//                    }
//                    ticket.getDocument().setFieldInt("SC_ID", Integer.valueOf(webBase.getIntSC_ID()));
//                    ticket.getDocument().setFieldInt("CT_ID", Integer.valueOf(pedidos.getIdCliente()));
//                    ticket.getDocument().setFieldString(strPrefijoMaster + "_RAZONSOCIAL", strPedRazonSocial);
//                    ticket.getDocument().setFieldString(strPrefijoMaster + "_RFC", pedidos.getCtRFC());
//                    ticket.getDocument().setFieldInt(strPrefijoMaster + "_MONEDA", 1);
//                    //Clave de vendedor
//                    int intVE_ID = 0;
//                    //Tarifas de IVA
//                    int intTI_ID = 0;
//                    int intTI_ID2 = 0;
//                    int intTI_ID3 = 0;
//                    try {
//                        intTI_ID = Integer.valueOf(pedidos.getIdTi());
//                        intTI_ID2 = Integer.valueOf(pedidos.getIdTi2());
//                        intTI_ID3 = Integer.valueOf(pedidos.getIdTi3());
//                    } catch (NumberFormatException ex) {
//                        log.error("ERP_Ventas TI_ID " + ex.getMessage());
//                    }
//                    //Tipo de comprobante
//                    int intFAC_TIPOCOMP = 0;
//                    //Asignamos los valores al objeto
//                    ticket.getDocument().setFieldInt("VE_ID", intVE_ID);
//                    ticket.getDocument().setFieldInt("TI_ID", intTI_ID);
//                    ticket.getDocument().setFieldInt("TI_ID2", intTI_ID2);
//                    ticket.getDocument().setFieldInt("TI_ID3", intTI_ID3);
//                    ticket.setIntFAC_TIPOCOMP(intFAC_TIPOCOMP);
//                    ticket.getDocument().setFieldInt(strPrefijoMaster + "_ESSERV", pedidos.getEsserv());
//                    ticket.getDocument().setFieldString(strPrefijoMaster + "_FECHA", pedidos.getFecha());
//                    ticket.getDocument().setFieldString(strPrefijoMaster + "_FOLIO", "");
//                    ticket.getDocument().setFieldString(strPrefijoMaster + "_FOLIO_C", "C");
////                    ticket.getDocument().setFieldString(strPrefijoMaster + "_NOTAS", pedidos.getNotas());
//                    ticket.getDocument().setFieldString(strPrefijoMaster + "_NOTAS", "");
////                    ticket.getDocument().setFieldString(strPrefijoMaster + "_NOTASPIE", pedidos.getNotasPie());
//                    ticket.getDocument().setFieldString(strPrefijoMaster + "_NOTASPIE", "");
////                    ticket.getDocument().setFieldString(strPrefijoMaster + "_REFERENCIA", pedidos.getReferencia());
//                    ticket.getDocument().setFieldString(strPrefijoMaster + "_REFERENCIA", "");
////                    ticket.getDocument().setFieldString(strPrefijoMaster + "_CONDPAGO", pedidos.getCondpago());
//                    ticket.getDocument().setFieldString(strPrefijoMaster + "_CONDPAGO", "");
//                    if (pedidos.getMetodopago() != null) {
//                        ticket.getDocument().setFieldString(strPrefijoMaster + "_METODODEPAGO", pedidos.getMetodopago());
//                    }
//                    log.debug("//////////// PAGO ////////////// ");
//                    ticket.getDocument().setFieldString(strPrefijoMaster + "_FORMADEPAGO", pedidos.getFormadepago());
//                    ticket.getDocument().setFieldDouble(strPrefijoMaster + "_IMPORTE", Double.valueOf(pedidos.getImporte()));
//                    ticket.getDocument().setFieldDouble(strPrefijoMaster + "_IMPUESTO1", Double.valueOf(pedidos.getImpuesto1()));
//                    ticket.getDocument().setFieldDouble(strPrefijoMaster + "_IMPUESTO2", Double.valueOf(pedidos.getImpuesto2()));
//                    ticket.getDocument().setFieldDouble(strPrefijoMaster + "_IMPUESTO3", Double.valueOf(pedidos.getImpuesto3()));
//                    ticket.getDocument().setFieldDouble(strPrefijoMaster + "_TOTAL", Double.valueOf(pedidos.getTotal()));
//                    log.debug("//////////// PRECIO ////////////// ");
//                    strPrecioVta = String.valueOf(pedidos.getTotal());
//                    log.debug("//////////// PRECIO ////////////// ");
//                    ticket.getDocument().setFieldDouble(strPrefijoMaster + "_TASA1", Double.valueOf(pedidos.getTasa1()));
//                    ticket.getDocument().setFieldDouble(strPrefijoMaster + "_TASA2", Double.valueOf(pedidos.getTasa2()));
//                    ticket.getDocument().setFieldDouble(strPrefijoMaster + "_TASA3", Double.valueOf(pedidos.getTasa3()));
//                    ticket.getDocument().setFieldString(strPrefijoMaster + "_SERIE", "C"); //numero de serie
//                    ticket.getDocument().setFieldInt("COFIDE_CARRITO", 1); //numero de serie
//                    log.debug("//////////// IMPORTE E IMPUESTO ////////////// ");
//                    ticket.getDocument().setFieldDouble(strPrefijoMaster + "_TASAPESO", 1);
//                    log.debug("//////////// FECHA DEL PEDIDO (" + pedidos.getFechapedi() + ") ////////////// ");
//                    if (pedidos.getFechapedi() != null) {
//                        String strFechaPedimento = pedidos.getFechapedi();
//                        if (strFechaPedimento.contains("/") && strFechaPedimento.length() == 10) {
//                            strFechaPedimento = fecha.FormateaBD(strFechaPedimento, "/");
//                        }
//                        ticket.getDocument().setFieldString(strPrefijoMaster + "_FECHAPEDI", strFechaPedimento);
//                    }
//                    //Si no hay moneda seleccionada que ponga tasa 1                    
//                    ticket.getDocument().setFieldDouble(strPrefijoMaster + "_TASAPESO", 1);
//                    log.debug("//////////// DEFINE AL EJECUTIVO = COFIDE, SI NO TIENE EJECUTIVO ASIGNADO ////////////// ");
//                    int intNumEjecutivo = 116;
//                    log.debug("//////////// BUSCA LA CLAVE DE BASE DE DATOS DEL CLIENTE ////////////// ");
//                    String strSql = "select CT_CLAVE_DDBB from vta_cliente where CT_ID = '" + intCt_Id + "' and CT_CLAVE_DDBB <> ''";
//                    try {
//                        rs = oConn.runQuery(strSql, true);
//                        while (rs.next()) {
//                            log.debug("//////////// SI TIENE BASE, BUSCA AL EJECUTIVO ////////////// ");
//                            strSql = "select id_usuarios from usuarios where COFIDE_CODIGO = '" + rs.getString("CT_CLAVE_DDBB") + "';";
//                            ResultSet rs2 = oConn.runQuery(strSql, true);
//                            while (rs2.next()) {
//                                log.debug("//////////// ASIGNA NUEVO EJECUTIVO ////////////// ");
//                                intNumEjecutivo = rs2.getInt("id_usuarios");
//                            }
//                            rs2.close();
//                        }
//                        rs.close();
//                    } catch (SQLException ex) {
//                        log.error("Consultar Cliente para obtener ID USUARIO: " + ex.getLocalizedMessage());
//                    }
//                    //################## almacenar los cursos que lleguen para los accesos
//                    ArrayList<String> arrCurso = new ArrayList<String>(); //id de curso String
//                    ArrayList<Integer> arrTipo = new ArrayList<Integer>(); //tipo de curso Integer            
//                    //################## almacenar los cursos que lleguen para los accesos
//                    int intModalidad = 1; //1 = normal, 2 = diplomado, 3 = seminario
//                    //Recibimos datos de los items o partidas
//                    log.debug("//////////// INICIAN LOS DETALLES DLE CURSO ////////////// ");
//                    Iterator<PedidosDeta> it = pedidos.getLstItems().iterator();
//                    log.debug("//////////// RECORREO LA LISTA DEL CURSO O CURSOS ////////////// ");
//                    while (it.hasNext()) {
//                        PedidosDeta pedidosdeta = it.next();
//                        TableMaster deta = null;
//                        if (strTipoVtaNom.equals(Ticket.TICKET)) {
//                            deta = new vta_ticketsdeta();
//                        }
//                        if (strTipoVtaNom.equals(Ticket.FACTURA)) {
//                            deta = new vta_facturadeta();
//                        }
//                        //modalidad
//                        if (pedidosdeta.getModcurso() != 0) {
//                            if (pedidosdeta.getModcurso() == 1) {
//                                intModalidad = 1;
//                            }
//                            if (pedidosdeta.getModcurso() == 2) {
//                                intModalidad = 2;
//                            }
//                            if (pedidosdeta.getModcurso() == 3) {
//                                intModalidad = 3;
//                            }
//                        } else {
//                            intModalidad = 1;
//                        }
//                        //modalidad
//                        deta.setFieldInt("SC_ID", Integer.valueOf(webBase.getIntSC_ID()));
//                        log.debug("//////////// ID DEL CURSO: (" + pedidosdeta.getCodigo() + ") ////////////// ");
//                        ticket.getDocument().setFieldString("CC_CURSO_ID", pedidosdeta.getCodigo());
//                        strCodigo = pedidosdeta.getCodigo();
//                        log.debug("//////////// TIPO DE CURSO: (" + pedidosdeta.getTipoCurso() + ") ////////////// ");
//                        log.debug("//////////// DESCUENTO DE CURSO: (" + pedidos.getDescuentototal() + ") ////////////// ");
////                        log.debug("//////////// DESCUENTO DE CURSO: (" + pedidosdeta.getDescuento() + ") ////////////// ");
//                        intTipoCurso = pedidosdeta.getTipoCurso();
//                        //Verificamos si el curso es ONLINE creamos lista de participantes
//                        tmpPrId = Integer.parseInt(strCodigo);
//                        //###############
//                        deta.setFieldInt("PR_ID", Integer.valueOf(strCodigo));
//                        deta.setFieldInt(strPrefijoDeta + "_TIPO_CURSO", intTipoCurso);
//                        deta.setFieldString(strPrefijoDeta + "_UNIDAD_MEDIDA", "CURSO");
//                        deta.setFieldInt(strPrefijoDeta + "_EXENTO1", Integer.valueOf(pedidosdeta.getExento1()));
//                        deta.setFieldInt(strPrefijoDeta + "_EXENTO2", Integer.valueOf(pedidosdeta.getExento2()));
//                        deta.setFieldInt(strPrefijoDeta + "_EXENTO3", Integer.valueOf(pedidosdeta.getExento3()));
//                        deta.setFieldInt(strPrefijoDeta + "_ESREGALO", Integer.valueOf(pedidosdeta.getEsRegalo()));
//                        deta.setFieldString(strPrefijoDeta + "_CVE", pedidosdeta.getCodigo());
//                        String strDescripcion = getPrDescripcion(strCodigo, oConn);
//                        log.debug("//////////// SE OBTUVO EL NOMBRE DEL CURSO: (" + strDescripcion + ") ////////////// ");
//                        deta.setFieldString(strPrefijoDeta + "_DESCRIPCION", strDescripcion);
//                        deta.setFieldString(strPrefijoDeta + "_NOSERIE", pedidosdeta.getNoSerie());
//                        deta.setFieldDouble(strPrefijoDeta + "_IMPORTE", Double.valueOf(pedidosdeta.getImporte()));
//                        deta.setFieldDouble(strPrefijoDeta + "_CANTIDAD", Double.valueOf(pedidosdeta.getCantidad()));
//                        deta.setFieldDouble(strPrefijoDeta + "_TASAIVA1", Double.valueOf(pedidosdeta.getTasaIva1()));
//                        deta.setFieldDouble(strPrefijoDeta + "_TASAIVA2", Double.valueOf(pedidosdeta.getTasaIva2()));
//                        deta.setFieldDouble(strPrefijoDeta + "_TASAIVA3", Double.valueOf(pedidosdeta.getTasaIva3()));
//                        deta.setFieldDouble(strPrefijoDeta + "_IMPUESTO1", Double.valueOf(pedidosdeta.getImpuesto1()));
//                        deta.setFieldDouble(strPrefijoDeta + "_IMPUESTO2", Double.valueOf(pedidosdeta.getImpuesto2()));
//                        deta.setFieldDouble(strPrefijoDeta + "_IMPUESTO3", Double.valueOf(pedidosdeta.getImpuesto3()));
//                        deta.setFieldDouble(strPrefijoDeta + "_IMPORTEREAL", Double.valueOf(pedidosdeta.getImporteReal()));
//                        deta.setFieldDouble(strPrefijoDeta + "_PRECIO", Double.valueOf(pedidosdeta.getPrecio()));
//                        deta.setFieldDouble(strPrefijoDeta + "_DESCUENTO", Double.valueOf(pedidos.getDescuentototal()));
//                        deta.setFieldDouble(strPrefijoDeta + "_PORDESC", Double.valueOf(pedidosdeta.getPordesc()));
//                        deta.setFieldDouble(strPrefijoDeta + "_PRECREAL", Double.valueOf(pedidosdeta.getPrecReal()));
//                        //Solo aplica si es ticket o factura
//                        if (strTipoVtaNom.equals(Ticket.TICKET) || strTipoVtaNom.equals(Ticket.FACTURA)) {
//                            deta.setFieldInt(strPrefijoDeta + "_ESDEVO", Integer.valueOf(pedidosdeta.getEsdevo()));
//                        }
//                        deta.setFieldInt(strPrefijoDeta + "_PRECFIJO", Integer.valueOf(pedidosdeta.getPrecfijo()));
//                        deta.setFieldInt(strPrefijoDeta + "_ESREGALO", Integer.valueOf(pedidosdeta.getEsRegalo()));
//                        deta.setFieldString(strPrefijoDeta + "_COMENTARIO", pedidosdeta.getNotas());
//                        if (servletRequest.getParameter("PDD_ID") != null) {/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                            deta.setFieldInt("PDD_ID", Integer.valueOf(servletRequest.getParameter("PDD_ID")));//////////////////////////////////////////////////////////////////////////////////////////////////////
//                        }
//                        ticket.AddDetalle(deta);
//                        //cuantos cursos recibimos
//                        arrCurso.add(strCodigo);
//                        arrTipo.add(intTipoCurso);
//                        //cuantos cursos recibimos
//                    }
//                    log.debug("//////////// SE GUARDO LA VENTA ////////////// ");
//                    log.debug("//////////// COMIENZA EL INIT ////////////// ");
//                    ticket.Init();
//                    //Generamos transaccion
//                    log.debug("//////////// COMIENZA EL DOTRX ////////////// ");
//                    ticket.doTrx();
//                    intIdVta = Integer.parseInt(ticket.getDocument().getValorKey());
//                    log.debug("//////////// ID DE LA VENTA: (" + intIdVta + ") ////////////// ");
//
//                    log.debug("//////////// PARTICIPANTES ////////////// ");
//                    log.debug("//////////// #CURSOS: (" + arrCurso.size() + ") y #tipo: (" + arrTipo.size() + ") ////////////// ");
//                    for (int i = 0; i < arrCurso.size(); i++) { //recorre los cursos
//                        Iterator<Participantes> itPrt = pedidos.getLstItemsPart().iterator();
//                        log.debug("//////////// SE RECORREN LOS PARTICIPANTES ////////////// ");
//                        while (itPrt.hasNext()) {
//                            Participantes participante = itPrt.next();
//                            TableMaster part = new CofideParticipantes();
//                            part.setFieldString("CP_NOMBRE", participante.getStrNombre());
//                            part.setFieldString("CP_APPAT", participante.getStrApPaterno());
//                            part.setFieldString("CP_APMAT", participante.getStrApMaterno());
//                            part.setFieldString("CP_TITULO", participante.getStrTitulo());
//                            part.setFieldString("CCO_NOSOCIO", participante.getStrNumSocio());
//                            part.setFieldString("CP_ASCOC", participante.getStrAsociacion());
//                            part.setFieldString("CP_CORREO", participante.getStrCorreo());
//                            log.debug("//////////// CLLIENTE CON EL QUE SE VA A LIGAR EL O LOS PARTICIPANTES ////////////// ");
//                            part.setFieldInt("CT_ID", intCt_Id);//ID DEL CLIENTE CON EL QUE SE VA A LIGAR
//                            part.setFieldInt("CP_MATERIAL_IMPRESO", participante.getIntReqMaterial());
//                            part.setFieldInt("CP_FAC_ID", intIdVta);
//                            part.setFieldInt("CP_TKT_ID", intIdVta);
//                            part.setFieldInt("CP_ID_CURSO", Integer.parseInt(arrCurso.get(i)));
////                            part.setFieldInt("CP_ID_CURSO", tmpPrId);
//                            part.setFieldInt("CP_ASISTENCIA", 1);
//                            part.setFieldInt("CP_TIPO_CURSO", arrTipo.get(i));
////                            part.setFieldInt("CP_TIPO_CURSO", intTipoCurso);
//                            part.setFieldInt("CP_ESTATUS", 1);
//                            part.Agrega(oConn);
//                            log.debug("//////////// AGREGO AL PARTICIPANTE ////////////// ");
//                            String strCorreo = participante.getStrCorreo();
//                            String strConcatParticipante = participante.getStrTitulo() + " " + participante.getStrNombre() + " " + participante.getStrApPaterno() + " " + participante.getStrApMaterno();
//                            int intPR_ID = Integer.parseInt(strCodigo);
//                            log.debug("//////////// SE CREA EL USUARIO EN LA WEB, SI NO EXISTE, SI EXISTE, REGRESA SU ID DE LA WEB ////////////// ");
//                            String strUser = "";
//                            strIdNewUser = mg.createUsuarioNuevoCurso(participante.getStrCorreo(), intCt_Id, participante.getStrNombre(), participante.getStrApPaterno(), participante.getStrTitulo());
//                            String strPassWordMGV = "";
//                            log.debug("//////////// BUSCAMOS USUARIO Y ID DEL USUARIO ////////////// ");
//                            try {
//                                Conexion oConnMGV = new Conexion();
//                                oConnMGV.setStrNomPool("jdbc/COFIDE");
//                                oConnMGV.open();
//                                strSql = "select usr,psw from cat_usr_w where id_usr_w = " + strIdNewUser;
//                                rs = oConnMGV.runQuery(strSql, true);
//                                while (rs.next()) {
//                                    log.debug("//////////// SE OBTUVO USUARIO Y CONTRASENIA DE LA WEB ////////////// ");
//                                    strPassWordMGV = rs.getString("psw");
//                                    strUser = rs.getString("usr");
//                                }
//                                rs.close();
//                                oConnMGV.close();
//                            } catch (SQLException ex) {
//                                log.error("GET PASSWORD COFIDE MGV: " + ex.getLocalizedMessage());
//                            }
//                            log.debug("//////////// ASIGNAS USUARIO Y CONTRASENIA PARA ENVIARLO POR CORREO ////////////// ");
//                            setStrUsuario(strUser);
//                            setStrContraseña(strPassWordMGV);
//                            log.debug("//////////// INICIA EL ENVIO DE ACCESOS O CONFIRMACIÓN  ////////////// ");
//                            log.debug("//////////// DATOS ////////////// ");
//                            log.debug("//////////// USUARIO: " + strUser + "////////////// ");
//                            log.debug("//////////// PASSWORD: " + strPassWordMGV + "////////////// ");
//                            log.debug("//////////// DATOS ////////////// ");
//                            log.debug("//////////// CORREO: (" + strCorreo + ") ////////////// ");
//                            log.debug("//////////// PARTICIPANTE: (" + strConcatParticipante + ") ////////////// ");
//                            log.debug("//////////// RAZÓN SOCIAL: (" + strPedRazonSocial + ") ////////////// ");
//                            log.debug("//////////// ID CURSO: (" + arrCurso.get(i) + ") ////////////// ");
//                            log.debug("//////////// EJECUTIVO: (" + intNumEjecutivo + ") ////////////// ");
//                            log.debug("//////////// TIPO DE CURSO: (" + arrTipo.get(i) + ") ////////////// ");
//                            log.debug("//////////// DATOS ////////////// ");
//                            String strRespuestaEmail = "";
//                            if (intTipoCurso == 1) {
//                                log.debug("//////////// PRESENCIAL ////////////// ");
//                                log.debug("//////////// CORREO: " + strCorreo + "////////////// ");
//                                strRespuestaEmail = MailDescargaMaterial(oConn, strCorreo, strConcatParticipante, strPedRazonSocial, Integer.parseInt(arrCurso.get(i)), intNumEjecutivo, arrTipo.get(i), strPrecioVta);
//                                log.debug("//////////// RESPUESTA EMAIL: (" + strRespuestaEmail + ") PRESENCIAL ////////////// ");
//                            } else {
//                                log.debug("//////////// EN LINEA ////////////// ");
//                                log.debug("//////////// CORREO: " + strCorreo + "////////////// ");
//                                strRespuestaEmail = MailAccesoOnline(oConn, strCorreo, strConcatParticipante, strPedRazonSocial, Integer.parseInt(arrCurso.get(i)), intNumEjecutivo, arrTipo.get(i), strPrecioVta);
//                                log.debug("//////////// RESPUESTA EMAIL: (" + strRespuestaEmail + ") EN LINEA ////////////// ");
//                            }
//                        }//Fin WHILE participantes
//                    }
//                    log.debug("//////////// PARTICIPANTES ////////////// ");
//                    //##################                                      
//                    log.debug("//////////// SE VALIDA LA VENTA ////////////// ");
//                    if (ticket.getStrResultLast().equals("OK")) {
//                        log.debug("//////////// LA VENTA FUE EXITOSA!! ////////////// ");
//                        strRes = ticket.getDocument().getFieldString(strPrefijoMaster + "_FOLIO");
//                        log.debug("\n\n//////////// RESPUESTA DE LA VENTA: (" + intIdVta + ") ////////////// \n\n");
//                        log.debug("\n\n//////////// SE ASIGNA LA VENTA A INBOUND  ////////////// \n\n");
//                        if (setAgente(oConn, intIdVta, strPrefijoMaster).equals("OK")) {
//                            log.debug("\n\n//////////// SE ASIGNÓ LA VENTA A INBOUND  ////////////// \n\n");
//                        } else {
//                            log.debug("\n\n//////////// NO SE ASIGNÓ LA VENTA A INBOUND  ////////////// \n\n");
//                        }
//                        log.debug("//////////// SE SINCRONIZA LA VENTA CON LA WEB, PARA LOS ACCESOS ////////////// ");
////                        mg.VtaWeb(oConn, intIdVta, strIdNewUser, Integer.parseInt(strTipoVta), intModalidad);
//                        mg.VtaWeb(oConn, intIdVta, strIdNewUser, intTipoCurso, intModalidad, strDoc);
//                        //Envio del pedido por mail
//                        log.debug("//////////// SE ENVIARA EL COMPROBANTE POR CORREO DE LA VENTA ////////////// ");
//                        String strMailCte = pedidos.getEmail1();
//                        if (!strMailCte.isEmpty()) {
//                            log.debug("//////////// SE GENERA EL PDF ////////////// ");
//                            log.debug("//////////// OBTENEMOS EL FOLIO DE LA FACTURA ////////////// ");
//
//                            String strRespPdf = "";
//                            if (strTipoVta.equals("1")) {
//                                vta_facturas fac = new vta_facturas();
//                                fac.ObtenDatos(intIdVta, oConn);
//                                String strFolioFac = fac.getFieldString("FAC_FOLIO");
//                                log.debug("//////////// FOLIO DE LA FACTURA ( " + strFolioFac + " )////////////// ");
//                                strRespPdf = GeneraImpresionPDF(oConn, strPathXML, "FACTURA_cfdi", strFolioFac, Integer.valueOf(ticket.getDocument().getValorKey()),
//                                        strPathFonts, strTipoFormat);
//                            } else {
//                                strRespPdf = GeneraImpresionPDF(oConn, strPathXML, strTipoFormat, ticket.getDocument().getFieldString(strPrefijoMaster + "_FOLIO"), Integer.valueOf(ticket.getDocument().getValorKey()),
//                                        strPathFonts, strTipoFormat);
//                            }
//                            if (strRespPdf.equals("OK")) {
//                                log.debug("//////////// SE GENERO CORRECTAMENTE, SE ENVIARA EL PDF AL CORREO INGRESADO ////////////// ");
//                                if (strTipoVta.equals("1")) {
//                                    log.debug("//////////// SE ENVIARA UNICAMENTE LA FACTURA ////////////// ");
//
//                                    String strRespEnvio = GeneraMail(oConn, strMailCte, "",
//                                            ticket.getDocument().getFieldString(strPrefijoMaster + "_FOLIO"),
//                                            strPathXML, strNomTemplate, strTipoFormat);
//                                    log.debug("//////////// RESPUESTA DEL CORREO: FACTURA: (" + strRespEnvio + ")////////////// ");
//                                }
//                            }
//                        }
//                    } else {
//                        log.debug("//////////// ERRROR EN LA VENTA ////////////// ");
//                        strRes = ticket.getStrResultLast();
//                        log.debug("//////////// RESPUESTA DEL ERROR: (" + strRes + ") ////////////// ");
//                    }
//                    log.debug("//////////// RESPUESTA FINAL: (" + strRes + ", ID DE LA VENTA: " + intIdVta + " ) LA QUE SE GENERA EN EL FOLIO DEL XML DE REGRESO////////////// ");
//                    if (intIdVta != 0) {
//                        strRes = String.valueOf(intIdVta);
//                        pedidos.setFolio(strRes); // manda el folio de la venta, ticket o factura
//                    } else {
//                        pedidos.setFolio(strRes); //manda el error
//                    }
//                }
//            } else {
//                log.debug("//////////// ERROR DE ACCESO ////////////// ");
//                pedidos.setNotas("Error:Access Denied");
//            }
//            oConn.close();
//        } catch (Exception ex) {
//            System.out.println("MobilServiceLogin1" + ex.getMessage() + " " + ex.getLocalizedMessage());
//        }
//        return Response.ok(pedidos).build();
//    }
//
//    /**
//     * Funciones
//     */
//    /**
//     * Envia el mail al cliente
//     *
//     * @param strMailCte Es el mail del cliente
//     * @param strMailCte2 Es el segundo mail del cli
//     * @param strFolio Es el folio
//     * @param strPath Es el path donde se alojara temporalmente el pdf
//     * @param strNomTemplate
//     * @param abrFormato
//     * @return Regresa OK si fue exitoso el envio del mail
//     */
//    protected String GeneraMail(Conexion oConn, String strMailCte, String strMailCte2,
//            String strFolio,
//            String strPath,
//            String strNomTemplate,
//            String abrFormato) {
//        String strResp = "OK";
//        //Nombre de archivo
//        //Obtenemos datos del smtp
//        String strsmtp_server = "";
//        String strsmtp_user = "";
//        String strsmtp_pass = "";
//        String strsmtp_port = "";
//        String strsmtp_usaTLS = "";
//        String strsmtp_usaSTLS = "";
//        //Buscamos los datos del SMTP
//        String strSql = "select * from cuenta_contratada where ctam_id = 1";
//        ResultSet rs;
//        try {
//            rs = oConn.runQuery(strSql, true);
//            while (rs.next()) {
//                strsmtp_server = rs.getString("smtp_server");
//                strsmtp_user = rs.getString("smtp_user");
//                strsmtp_pass = rs.getString("smtp_pass");
//                strsmtp_port = rs.getString("smtp_port");
//                strsmtp_usaTLS = rs.getString("smtp_usaTLS");
//                strsmtp_usaSTLS = rs.getString("smtp_usaSTLS");
//            }
//            rs.close();
//        } catch (SQLException ex) {
//            log.error(ex.getMessage());
//        }
//        //Obtenemos los textos para el envio del mail
//
//        String[] lstMail = getMailTemplate(oConn, strNomTemplate);
//
//        /**
//         * Si estan llenos todos los datos mandamos el mail
//         */
//        if (!strsmtp_server.equals("")
//                && !strsmtp_user.equals("")
//                && !strsmtp_pass.equals("")) {
//            //armamos el mail
//            Mail mail = new Mail();
//            //Activamos envio de acuse de recibo
//            mail.setBolAcuseRecibo(true);
//            //Obtenemos los usuarios a los que mandaremos el mail
//            String strLstMail = "";
//            //Validamos si el mail del cliente es valido
//            if (mail.isEmail(strMailCte)) {
//                strLstMail += "," + strMailCte;
//            }
//            if (mail.isEmail(strMailCte2)) {
//                strLstMail += "," + strMailCte2;
//            }
//            //Mandamos mail si hay usuarios
//            if (!strLstMail.equals("")) {
//                String strMsgMail = lstMail[1];
//                strMsgMail = strMsgMail.replace("%folio%", strFolio);
//                //Establecemos parametros
//                mail.setUsuario(strsmtp_user);
//                mail.setContrasenia(strsmtp_pass);
//                mail.setHost(strsmtp_server);
//                mail.setPuerto(strsmtp_port);
//                mail.setAsunto(lstMail[0].replace("%folio%", strFolio));
//                mail.setDestino(strLstMail);
//                mail.setMensaje(strMsgMail);
//                //Adjuntamos XML y PDF
//                mail.setFichero(strPath + abrFormato + "_web" + strFolio + ".pdf");
//
//                if (strsmtp_usaTLS.equals("1")) {
//                    mail.setBolUsaTls(true);
//                }
//                if (strsmtp_usaSTLS.equals("1")) {
//                    mail.setBolUsaStartTls(true);
//                }
//                boolean bol = mail.sendMail();
//                if (!bol) {
//                    strResp = "Fallo el envio del Mail.";
//                }
//            }
//        }
//        return strResp;
//    }
//
//    /**
//     * Obtenemos los valores del template para el mail
//     *
//     * @param strNom Es el nombre del template
//     * @return Regresa un arreglo con los valores del template
//     */
//    public String[] getMailTemplate(Conexion oConn, String strNom) {
//        String[] listValores = new String[2];
//        String strSql = "select MT_ASUNTO,MT_CONTENIDO from mailtemplates where MT_ABRV ='" + strNom + "'";
//        ResultSet rsM;
//        try {
//            rsM = oConn.runQuery(strSql, true);
//            while (rsM.next()) {
//                System.out.println("esta obteniendo la información de la plantilla");
//                listValores[0] = rsM.getString("MT_ASUNTO");
//                listValores[1] = rsM.getString("MT_CONTENIDO");
//            }
//            rsM.close();
//        } catch (SQLException ex) {
//            log.error(ex.getMessage());
//            System.out.println("error al obtener la plantilla");
//        }
//        return listValores;
//    }
//
//    /**
//     * Genera el formato de impresion en PDF
//     *
//     * @param strPath Es el path
//     * @param intEMP_TIPOCOMP Es el tipo de comprobante
//     * @param intEmpId Es el id de la empresa
//     * @param strFAC_NOMFORMATO Es el nombre del formato
//     * @return Regresa OK si se genero el formato
//     */
//    protected String GeneraImpresionPDF(Conexion oConn, String strPath,
//            String strFAC_NOMFORMATO, String strFolio, int intTransaccion,
//            String strPATHFonts, String abrFormato) {
//        String strResp = "OK";
//        //Posicion inicial para el numero de pagina
//        String strPosX = null;
//        String strTitle = "";
//        strTitle = "Factura ";
//
//        try {
//            Document document = new Document();
//            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(strPath + abrFormato + "_web" + strFolio + ".pdf"));
//            //Objeto que dibuja el numero de paginas
//            PDFEventPage pdfEvent = new PDFEventPage();
//            pdfEvent.setStrTitleApp(strTitle);
//            //Colocamos el numero donde comienza X por medio del parametro del web Xml por si necesitamos algun ajuste
//            if (strPosX != null) {
//                try {
//                    int intPosX = Integer.valueOf(strPosX);
//                    pdfEvent.setIntXPageNum(intPosX);
//                } catch (NumberFormatException ex) {
//                }
//            } else {
//                pdfEvent.setIntXPageNum(300);
//                pdfEvent.setIntXPageNumRight(50);
//                pdfEvent.setIntXPageTemplate(252.3f);
//            }
//            //Anexamos el evento
//            writer.setPageEvent(pdfEvent);
//            document.open();
//            Formateador format = new Formateador();
//            format.setIntTypeOut(Formateador.FILE);
//            format.setStrPath(strPath);
//            format.InitFormat(oConn, strFAC_NOMFORMATO);
//            log.debug("//////////// Antes de format.DoFormat(oConn, intTransaccion)");
//            String strRes = format.DoFormat(oConn, intTransaccion);
//            log.debug("//////////// Despues de format.DoFormat(oConn, intTransaccion)");
//            if (strRes.equals("OK")) {
//                CIP_Formato fPDF = new CIP_Formato();
//                fPDF.setDocument(document);
//                fPDF.setWriter(writer);
//                fPDF.setStrPathFonts(strPATHFonts);
//                fPDF.EmiteFormato(format.getFmXML());
//            } else {
//                strResp = strRes;
//            }
//            document.close();
//            writer.close();
//        } catch (FileNotFoundException ex) {
//            log.error(ex.getMessage());
//            strResp = "ERROR:" + ex.getMessage();
//        } catch (DocumentException ex) {
//            log.error(ex.getMessage());
//            strResp = "ERROR:" + ex.getMessage();
//        }
//        return strResp;
//    }
//
//    /**
//     * Obtenemos el mail del cliente
//     */
//    public String getMail(int intIdUser, Conexion oConn) {
//        String strMail = "";
//        String strSql = "select CT_EMAIL1 from vta_cliente where CT_ID = " + intIdUser;
//        try {
//            ResultSet rs = oConn.runQuery(strSql, true);
//            while (rs.next()) {
//                strMail = rs.getString("CT_EMAIL1");
//            }
//            rs.close();
//        } catch (SQLException ex) {
//            log.error(ex.getMessage());
//        }
//
//        return strMail;
//    }
//
//    public String getPrDescripcion(String strCodigo, Conexion oConn) {
//        String strPrDescripcion = "";
//        String strSql = "select CC_NOMBRE_CURSO from cofide_cursos where CC_CURSO_ID = " + strCodigo;
//        ResultSet rs = null;
//        UtilXml util = new UtilXml();
//        try {
//            rs = oConn.runQuery(strSql, true);
//            while (rs.next()) {
//                strPrDescripcion = util.Sustituye(rs.getString("CC_NOMBRE_CURSO"));
//            }
//            rs.close();
//        } catch (SQLException ex) {
//            log.error("Error Producto Descripcion CC_CURSO_ID = " + strCodigo + " ERROR [" + ex + "].");
//        }
//        return strPrDescripcion;
//    }
//
//    //acceso online
//    public String MailAccesoOnline(Conexion oConn, String strMailCte, String strParticipante, String strRazonsocial, int intIdCurso, int intIDEjecutivo, int intTipo, String strPrecioVta) throws SQLException, ParseException {
////Objetenemso los datos dle curso
//        System.out.println("entro para enviar el correo");
//        CRM_Envio_Template tmp = new CRM_Envio_Template();
//        String strNombre = ""; //NOMBRE DEL CURSO
//        String strFechaIni = "";
//        String strHhrsInicio = "";
//        String strDuracion = "";
//        String strPrecio = "";
//        int intTipoCurso = 1;
//        String strIdWEb = "";
//        String strEsSeminario = "";
//        String strEsDiplomado = "";
//        String strLigaCurso = "";
//        String strFechaOk = "";
//        String strSede = "";
//        String strOnline = "";
//        String strModalidad = "";
//        String strTitulo = "";
////        strParticipante = getHtml(strParticipante);
////        strRazonsocial = getHtml(strRazonsocial);
//        String strURLMaterial = "";
//        strPrecioVta = tmp.setPrecioOk(strPrecioVta);
//        SimpleDateFormat conver = new SimpleDateFormat("yyyyMMdd");
//        SimpleDateFormat format = new SimpleDateFormat("E. dd MMM,YYYY", new Locale("es", "MX"));
//        System.out.println("va a consultar la información del curso");
//        String strSqlCurso = "select CC_CURSO_ID, CC_NOMBRE_CURSO, CC_FECHA_INICIAL, CC_MATERIAL, "
//                + "CC_HR_EVENTO_INI, CC_DURACION_HRS, CC_ID_WEB, CC_IS_DIPLOMADO, CC_IS_SEMINARIO, "
//                + "CC_IS_ONLINE,CC_PRECIO_ON,CC_IVA_ON,CC_IS_VIDEO, CC_PRECIO_VID, CC_IVA_VID,"
//                + "(select CSH_SEDE from cofide_sede_hotel where CSH_ID = CC_SEDE_ID) as sede  "
//                + "from cofide_cursos where cc_curso_id = " + intIdCurso;
//        ResultSet rsCurso = oConn.runQuery(strSqlCurso, true);
//        while (rsCurso.next()) {
//            System.out.println("entro a la infirmación del curso");
//            strNombre = rsCurso.getString("CC_NOMBRE_CURSO");
//            strFechaIni = rsCurso.getString("CC_FECHA_INICIAL");
//            strHhrsInicio = rsCurso.getString("CC_HR_EVENTO_INI");
//            strDuracion = rsCurso.getString("CC_DURACION_HRS");
//            if (intTipo == 2) { //online
//                if (!rsCurso.getString("CC_IVA_ON").equals("")) {
//                    strPrecio = tmp.setPrecioOk(rsCurso.getString("CC_IVA_ON"));
//                }
////                strPrecio = rsCurso.getString("CC_IVA_ON");
//                strModalidad = "EN LÍNEA";
//                strTitulo = "CURSO EN LÍNEA.";
//            }
//            if (intTipo == 3) { //video
//                if (!rsCurso.getString("CC_IVA_VID").equals("")) {
//                    strPrecio = tmp.setPrecioOk(rsCurso.getString("CC_IVA_VID"));
//                }
////                strPrecio = rsCurso.getString("CC_IVA_VID");
//                strModalidad = "VIDEO CURSO";
//                strTitulo = "VIDEO CURSO.";
//            }
////            strPrecio = rsCurso.getString("CC_PRECIO_IVA");
//            strIdWEb = rsCurso.getString("CC_CURSO_ID");
//            strEsSeminario = rsCurso.getString("CC_IS_SEMINARIO");
//            strEsDiplomado = rsCurso.getString("CC_IS_DIPLOMADO");
////            strOnline = rsCurso.getString("CC_IS_ONLINE");
//            strSede = rsCurso.getString("sede");
////            strSede = getHtml(strSede);
//            strURLMaterial = rsCurso.getString("CC_MATERIAL");
//
//            Date date = conver.parse(strFechaIni);
//            strFechaOk = format.format(date);
//
//            if (strEsSeminario.equals("1")) {
//                intTipoCurso = 3;
//            }
//            if (strEsDiplomado.equals("1")) {
//                intTipoCurso = 2;
//            }
//            strLigaCurso = fec.FormateaDDMMAAAA(strFechaIni, "") + strIdWEb + intTipoCurso;
//        }
//        rsCurso.close();
//        //Objetenemso los datos dle curso
//        //obtenemos el contato del agente
//        String strAgente = "";
//        String strNumero = "";
//        String strCorreo = "";
//        String strExt = "";
//        String strPass = "";
//        String strServer = "";
//        String strPort = "";
//        String strTLS = "";
//        String strSTLS = "";
//        String strSqlEjecutivo = "select id_usuarios, nombre_usuario, TELEFONO, NUM_EXT,"
//                + "SMTP_US, SMTP_PASS, SMTP, PORT, SMTP_USATLS, SMTP_USASTLS "
//                + "from usuarios where id_usuarios = " + intIDEjecutivo;
//        ResultSet rsAgente = oConn.runQuery(strSqlEjecutivo, true);
//        while (rsAgente.next()) {
//            strAgente = rsAgente.getString("nombre_usuario");
//            strNumero = rsAgente.getString("TELEFONO");
//            strCorreo = rsAgente.getString("SMTP_US");
//            strExt = rsAgente.getString("NUM_EXT");
//            strPass = rsAgente.getString("SMTP_PASS");
//            strServer = rsAgente.getString("SMTP");
//            strPort = rsAgente.getString("PORT");
//            strTLS = rsAgente.getString("SMTP_USATLS");
//            strSTLS = rsAgente.getString("SMTP_USASTLS");
//        }
//        rsAgente.close();
//        //obtenemos el contato del agente
//        String strResp = "OK";
//        String strNomTemplate = "ACCESO_WEB"; //obtenemos el template del mail para le supervisor
//        System.out.println("va a buscar la plantilla");
//        String[] lstMail = getMailTemplate(oConn, strNomTemplate);
//        System.out.println("ya obtuvo la plantilla");
//        String strMsgMail = lstMail[1];
//        //  Si estan llenos todos los datos mandamos el mail
//        System.out.println("valida que sea correo: " + strCorreo);
//        if (!strServer.equals("")
//                && !strCorreo.equals("")
//                && !strPass.equals("")) {
//            System.out.println("es correo: " + strCorreo);
//            //armamos el mail
//            Mail mail = new Mail();
//            mail.setBolDepuracion(false);
//            //Activamos envio de acuse de recibo
//            mail.setBolAcuseRecibo(false);
//            //Obtenemos los usuarios a los que mandaremos el mail
//            String strLstMail = "";
//            //Validamos si el mail del cliente es valido
//            if (mail.isEmail(strMailCte)) {
//                strLstMail = strMailCte;
//            }
//            //Mandamos mail si hay usuarios
//            if (!strLstMail.equals("")) {
//                strMsgMail = strMsgMail.replace("%TITULO%", getHtml(strTitulo));
//                strMsgMail = strMsgMail.replace("%FECHA%", getHtml(strFechaOk));
//                strMsgMail = strMsgMail.replace("%CURSO%", getHtml(strNombre));
//                strMsgMail = strMsgMail.replace("%HR_INI%", strHhrsInicio);
//                strMsgMail = strMsgMail.replace("%DURACION%", strDuracion);
//                strMsgMail = strMsgMail.replace("%SEDE%", getHtml(strSede));
//                strMsgMail = strMsgMail.replace("%PRECIO%", strPrecioVta);
////                strMsgMail = strMsgMail.replace("%PRECIO%", strPrecio);
//                strMsgMail = strMsgMail.replace("%RAZONSOCIAL%", getHtml(strRazonsocial));
//                strMsgMail = strMsgMail.replace("%PARTICIPANTE%", getHtml(strParticipante));
//                strMsgMail = strMsgMail.replace("%MODALIDAD%", getHtml(strModalidad));
//                strMsgMail = strMsgMail.replace("%ID_CURSO_WEB%", strLigaCurso);
//                strMsgMail = strMsgMail.replace("%WEB_USUARIO%", getStrUsuario());
//                strMsgMail = strMsgMail.replace("%WEB_PASSWORD%", getStrContraseña());
//                strMsgMail = strMsgMail.replace("%URL_MATERIAL%", strURLMaterial);
//                strMsgMail = strMsgMail.replace("%AGENTE%", getHtml(strAgente));
//                strMsgMail = strMsgMail.replace("%EXTENSION%", strNumero);
//                strMsgMail = strMsgMail.replace("%CORREO%", strCorreo);
//
//                //Establecemos parametros
//                mail.setUsuario(strCorreo);
//                mail.setContrasenia(strPass);
//                mail.setHost(strServer);
//                mail.setPuerto(strPort);
//                mail.setAsunto(lstMail[0].replace("%CC_NOMBRE_CURSO%", strNombre));
//                mail.setDestino(strLstMail);
//                mail.setMensaje(strMsgMail);
//                mail.setBolDepuracion(false);
//                //Adjuntamos XML y PDF
//                if (strTLS.equals("1")) {
//                    mail.setBolUsaTls(true);
//                }
//                if (strSTLS.equals("1")) {
//                    mail.setBolUsaStartTls(true);
//                }
////                mail.sendMail();
//                System.out.println("enviara el correo");
//                boolean bol = mail.sendMail();
//                if (!bol) {
//                    strResp = "Fallo el envio del Mail.";
//                    System.out.println("respuesta BAD: " + strResp);
//                } else {
//                    strResp = "OK";
//                    System.out.println("respuesta OKIS: " + strResp);
//                }
//            }
//        }
//        return strResp;
//    }
////    // correos
//
//    public String getHtml(String strTxto) {
//        strTxto = strTxto.replace("á", "&aacute;");
//        strTxto = strTxto.replace("é", "&eacute;");
//        strTxto = strTxto.replace("í", "&iacute;");
//        strTxto = strTxto.replace("ó", "&oacute;");
//        strTxto = strTxto.replace("ú", "&uacute;");
//        strTxto = strTxto.replace("Á", "&Aacute;");
//        strTxto = strTxto.replace("É", "&Eacute;");
//        strTxto = strTxto.replace("Í", "&Iacute;");
//        strTxto = strTxto.replace("Ó", "&Oacute;");
//        strTxto = strTxto.replace("Ú", "&Uacute;");
//        strTxto = strTxto.replace("ñ", "&ntilde;");
//        strTxto = strTxto.replace("Ñ", "&Ntilde;");
//        /**
//         * http://www.forosdelweb.com/f4/acentos-codigo-html-481545/ Re: acentos
//         * en el código html a = &aacute
//         *
//         * é = &eacute
//         *
//         * í = &iacute
//         *
//         * ó = &oacute
//         *
//         * ú = &uacute
//         *
//         * ñ = &ntilde
//         *
//         */
//        return strTxto;
//    }
//
//    public String MailDescargaMaterial(Conexion oConn, String strMailCte, String strParticipante, String strRazonsocial, int intIdCurso, int intIDEjecutivo, int intTipo, String strPrecioVta) throws SQLException, ParseException {
////Objetenemso los datos dle curso
//        CRM_Envio_Template tmp = new CRM_Envio_Template();
//        String strNombre = ""; //NOMBRE DEL CURSO
//        String strFechaIni = "";
//        String strHhrsInicio = "";
//        String strDuracion = "";
//        String strPrecio = "";
//        int intTipoCurso = 1;
//        String strIdWEb = "";
//        String strEsSeminario = "";
//        String strEsDiplomado = "";
//        String strLigaCurso = "";
//        String strFechaOk = "";
//        String strSede = "";
//        String strOnline = "";
//        String strModalidad = "";
//        String strURLMaterial = "";
//        strPrecioVta = tmp.setPrecioOk(strPrecioVta);
//        SimpleDateFormat conver = new SimpleDateFormat("yyyyMMdd");
//        SimpleDateFormat format = new SimpleDateFormat("E. dd MMM,YYYY", new Locale("es", "MX"));
//        String strSqlCurso = "select CC_CURSO_ID, CC_NOMBRE_CURSO, CC_FECHA_INICIAL,CC_MATERIAL, "
//                + "CC_HR_EVENTO_INI, CC_DURACION_HRS, CC_PRECIO_PRES,CC_IVA_PRES, CC_PRECIO_ON, CC_IVA_ON, CC_PRECIO_VID, CC_IVA_VID, "
//                + "CC_ID_WEB, CC_IS_DIPLOMADO, CC_IS_SEMINARIO, CC_IS_ONLINE,"
//                + "(select CSH_SEDE from cofide_sede_hotel where CSH_ID = CC_SEDE_ID) as sede  "
//                + "from cofide_cursos where cc_curso_id = " + intIdCurso;
//        ResultSet rsCurso = oConn.runQuery(strSqlCurso, true);
//        while (rsCurso.next()) {
//            strNombre = rsCurso.getString("CC_NOMBRE_CURSO");
//            strFechaIni = rsCurso.getString("CC_FECHA_INICIAL");
//            strHhrsInicio = rsCurso.getString("CC_HR_EVENTO_INI");
//            strDuracion = rsCurso.getString("CC_DURACION_HRS");
//            strIdWEb = rsCurso.getString("CC_CURSO_ID");
//            strEsSeminario = rsCurso.getString("CC_IS_SEMINARIO");
//            strEsDiplomado = rsCurso.getString("CC_IS_DIPLOMADO");
//            strOnline = rsCurso.getString("CC_IS_ONLINE");
//            strSede = rsCurso.getString("sede");
//            strURLMaterial = rsCurso.getString("CC_MATERIAL");
//            if (intTipo == 1) { //presencial
//                if (!rsCurso.getString("CC_IVA_PRES").equals("")) {
//                    strPrecio = tmp.setPrecioOk(rsCurso.getString("CC_IVA_PRES"));
//                }
////                strPrecio = rsCurso.getString("CC_IVA_PRES");
//                strModalidad = "PRESENCIAL";
//            }
//            if (intTipo == 2) { //en linea
//                if (!rsCurso.getString("CC_IVA_ON").equals("")) {
//                    strPrecio = tmp.setPrecioOk(rsCurso.getString("CC_IVA_ON"));
//                }
////                strPrecio = rsCurso.getString("CC_IVA_ON");
//                strModalidad = "EN LÍNEA";
//            }
//            if (intTipo == 3) { //retransmisión
//                if (!rsCurso.getString("CC_IVA_VID").equals("")) {
//                    strPrecio = tmp.setPrecioOk(rsCurso.getString("CC_IVA_VID"));
//                }
////                strPrecio = rsCurso.getString("CC_IVA_VID");
//                strModalidad = "RETRANSMISIÓN";
//            }
//            Date date = conver.parse(strFechaIni);
//            strFechaOk = format.format(date);
//
//            if (strEsSeminario.equals("1")) {
//                intTipoCurso = 3;
//            }
//            if (strEsDiplomado.equals("1")) {
//                intTipoCurso = 2;
//            }
//            strLigaCurso = fec.FormateaDDMMAAAA(strFechaIni, "") + strIdWEb + intTipoCurso;
//        }
//        rsCurso.close();
//        //Objetenemso los datos dle curso
//        //obtenemos el contato del agente
//        String strAgente = "";
//        String strNumero = "";
//        String strCorreo = "";
//        String strExt = "";
//        String strPass = "";
//        String strServer = "";
//        String strPort = "";
//        String strTLS = "";
//        String strSTLS = "";
//        String strSqlEjecutivo = "select id_usuarios, nombre_usuario, TELEFONO, NUM_EXT,"
//                + "SMTP_US, SMTP_PASS, SMTP, PORT, SMTP_USATLS, SMTP_USASTLS "
//                + "from usuarios where id_usuarios = " + intIDEjecutivo;
//        ResultSet rsAgente = oConn.runQuery(strSqlEjecutivo, true);
//        while (rsAgente.next()) {
//            strAgente = rsAgente.getString("nombre_usuario");
//            strNumero = rsAgente.getString("TELEFONO");
//            strCorreo = rsAgente.getString("SMTP_US");
//            strExt = rsAgente.getString("NUM_EXT");
//            strPass = rsAgente.getString("SMTP_PASS");
//            strServer = rsAgente.getString("SMTP");
//            strPort = rsAgente.getString("PORT");
//            strTLS = rsAgente.getString("SMTP_USATLS");
//            strSTLS = rsAgente.getString("SMTP_USASTLS");
//        }
//        rsAgente.close();
//        //obtenemos el contato del agente
//        String strResp = "OK";
//        String strNomTemplate = "DESCARGA"; //obtenemos el template del mail para le supervisor
//        String[] lstMail = getMailTemplate(oConn, strNomTemplate);
//        String strMsgMail = lstMail[1];
//        //  Si estan llenos todos los datos mandamos el mail
//        if (!strServer.equals("")
//                && !strCorreo.equals("")
//                && !strPass.equals("")) {
//            //armamos el mail
//            Mail mail = new Mail();
//            mail.setBolDepuracion(false);
//            //Activamos envio de acuse de recibo
//            mail.setBolAcuseRecibo(false);
//            //Obtenemos los usuarios a los que mandaremos el mail
//            String strLstMail = "";
//            //Validamos si el mail del cliente es valido
//            if (mail.isEmail(strMailCte)) {
//                strLstMail = strMailCte;
//            }
//            //Mandamos mail si hay usuarios
//            if (!strLstMail.equals("")) {
//                strMsgMail = strMsgMail.replace("%FECHA%", getHtml(strFechaOk));
//                strMsgMail = strMsgMail.replace("%CURSO%", getHtml(strNombre));
//                strMsgMail = strMsgMail.replace("%HR_INI%", strHhrsInicio);
//                strMsgMail = strMsgMail.replace("%DURACION%", strDuracion);
//                strMsgMail = strMsgMail.replace("%SEDE%", getHtml(strSede));
//                strMsgMail = strMsgMail.replace("%PRECIO%", strPrecioVta);
////                strMsgMail = strMsgMail.replace("%PRECIO%", strPrecio);
//                strMsgMail = strMsgMail.replace("%RAZONSOCIAL%", getHtml(strRazonsocial));
//                strMsgMail = strMsgMail.replace("%PARTICIPANTE%", getHtml(strParticipante));
//                strMsgMail = strMsgMail.replace("%MODALIDAD%", getHtml(strModalidad));
//                strMsgMail = strMsgMail.replace("%ID_CURSO_WEB%", strLigaCurso);
//                strMsgMail = strMsgMail.replace("%WEB_USUARIO%", getStrUsuario());
//                strMsgMail = strMsgMail.replace("%WEB_PASSWORD%", getStrContraseña());
//                strMsgMail = strMsgMail.replace("%URL_MATERIAL%", strURLMaterial);
//                strMsgMail = strMsgMail.replace("%AGENTE%", getHtml(strAgente));
//                strMsgMail = strMsgMail.replace("%EXTENSION%", strNumero);
//                strMsgMail = strMsgMail.replace("%CORREO%", strCorreo);
//                //Establecemos parametros
//                mail.setUsuario(strCorreo);
//                mail.setContrasenia(strPass);
//                mail.setHost(strServer);
//                mail.setPuerto(strPort);
//                mail.setAsunto(lstMail[0].replace("%CC_NOMBRE_CURSO%", strNombre));
//                mail.setDestino(strLstMail);
//                mail.setMensaje(strMsgMail);
//                mail.setBolDepuracion(false);
//                //Adjuntamos XML y PDF
//                if (strTLS.equals("1")) {
//                    mail.setBolUsaTls(true);
//                }
//                if (strSTLS.equals("1")) {
//                    mail.setBolUsaStartTls(true);
//                }
////                mail.sendMail();
//                boolean bol = mail.sendMail();
//                if (!bol) {
//                    strResp = "Fallo el envio del Mail.";
//                }
//            }
//        }
//        return strResp;
//    }
//
//    public String setAgente(Conexion oConn, int intIdVta, String strTipoDoc) {
//        String strRespuesta = "";
//        String strTable = "vta_tickets";
//        String strSql = "";
//        if (strTipoDoc.equals("FAC")) {
//            strTable = "vta_facturas";
//        }
//        strSql = "update " + strTable + " set " + strTipoDoc + "_US_ALTA= 163 where " + strTipoDoc + "_ID = " + intIdVta;
//        oConn.runQueryLMD(strSql);
//        if (!oConn.isBolEsError()) {
//            strRespuesta = "OK";
//        }
//        return strRespuesta;
//    }
//}
