/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.restful;

import com.mx.siweb.erp.restful.entity.Pedidos;
import com.mx.siweb.erp.restful.entity.PedidosDeta;
import com.mx.siweb.erp.restful.entity.DirEntrega;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import comSIWeb.Operaciones.Reportes.CIP_Formato;
import comSIWeb.Operaciones.Formatos.Formateador;
import comSIWeb.Operaciones.Reportes.PDFEventPage;
import java.io.FileOutputStream;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import java.io.FileNotFoundException;
import comSIWeb.Utilerias.Mail;
import java.sql.SQLException;
import Tablas.vta_cliente_dir_entrega;
import java.util.Iterator;
import java.sql.ResultSet;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Utilerias.Fechas;
import Tablas.vta_ticketsdeta;
import Tablas.vta_pedidosdeta;
import Tablas.vta_facturadeta;
import Tablas.vta_cotizadeta;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Operaciones.TableMaster;
import ERP.Ticket;
import com.mx.siweb.erp.especiales.cofide.vta_clientes;
import com.mx.siweb.ui.web.Site;
import javax.ws.rs.POST;

/**
 * REST Web Service
 *
 * @author ZeusGalindo
 */
@Path("pedidos")
public class PedidosResource {

    @Context
    private UriInfo context;
    @Context
    private HttpServletRequest servletRequest;
    @Context
    private javax.servlet.ServletContext servletContext;
    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(PedidosResource.class.getName());

    /**
     * Creates a new instance of PedidosResource
     */
    public PedidosResource() {
    }

    /**
     * Retrieves representation of an instance of
     * com.mx.siweb.erp.restful.PedidosResource
     *
     * @return an instance of com.mx.siweb.erp.restful.entity.Pedidos
     */
    @GET
    @Produces({"application/json"})
    public Pedidos getJson(@DefaultValue("0") @QueryParam("idPedido") String idPedido, @DefaultValue("") @QueryParam("Codigo") String strCodigo) {
        Pedidos pedido = new Pedidos();
        //Accedemos a la base de datos para ver si tenemos acceso
        VariableSession varSesiones = new VariableSession(servletRequest);
        varSesiones.getVars();

        try {
            //Abrimos la conexion
            Conexion oConn = new Conexion(varSesiones.getStrUser(), servletContext);
            oConn.open();

            //Validamos si tenemos acceso
            EvalSesion eval = new EvalSesion();
            if (eval.evaluaSesion(strCodigo, oConn)) {
                if (eval.evaluaPermiso(556, oConn) || eval.isBolEsCliente()) {
                    pedido.setIdProducto(232);
                    PedidosDeta deta = new PedidosDeta();
                    deta.setCantidad(2);
                    deta.setCodigo("330303");
                    pedido.getLstItems().add(deta);
                }

            } else {
                pedido.setNotas("Error:Access Denied");
            }

            oConn.close();
        } catch (Exception ex) {
            log.error("MobilServiceLogin1" + ex.getMessage() + " " + ex.getLocalizedMessage());
        }
        return pedido;
    }

    /**
     * Retrieves representation of an instance of
     * com.mx.siweb.erp.restful.CambioPasswordResource
     *
     * @return an instance of java.lang.String
     */
    /**
     * PUT method for updating or creating an instance of CambioPasswordResource
     *
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @POST
    @Consumes({"application/json"})
    public Response putJson(Pedidos pedidos) {
        //Accedemos a la base de datos para ver si tenemos acceso
        VariableSession varSesiones = new VariableSession(servletRequest);
        varSesiones.getVars();
        try {
            //Abrimos la conexion
            Conexion oConn = new Conexion(varSesiones.getStrUser(), servletContext);
            oConn.open();
            ResultSet rs = null;
            String strConsulta = "";
            int intCt_Id = 0;

            //Validamos si tenemos acceso
            EvalSesion eval = new EvalSesion();

            if (eval.evaluaSesion(pedidos.getCodigoAcceso(), oConn)) {
                Site webBase = new Site(oConn);
                if (eval.evaluaPermiso(556, oConn) || eval.isBolEsCliente()) {
                    boolean blExisteCt = false;
                    int moneda = 0;

                    if (pedidos.getIdCliente() == 0) {
                        String strFiltroEmail2 = "";
                        if (pedidos.getEmail2() != null) {
                            strFiltroEmail2 = " or CT_EMAIL2 = '" + pedidos.getEmail2() + "'";
                        }
                        //Primer validacion en la tbala de clientes.
                        strConsulta = "select CT_ID from vta_cliente where (CT_EMAIL1 = '"
                                + pedidos.getEmail1() + "' " + strFiltroEmail2 + ")";
                        rs = oConn.runQuery(strConsulta, true);

                        while (rs.next()) {
                            pedidos.setIdCliente(rs.getInt("CT_ID"));
                            blExisteCt = true;
                        }
                        rs.close();
                        //No existe en clientes buscamos en contactos
                        if (!blExisteCt) {
                            if (pedidos.getEmail2() != null) {
                                strFiltroEmail2 = " or CCO_CORREO2 = '" + pedidos.getEmail2() + "'";
                            }

                            strConsulta = "select CT_ID from cofide_contactos where (CCO_CORREO = '"
                                    + pedidos.getEmail1() + "' " + strFiltroEmail2 + ")";
                            rs = oConn.runQuery(strConsulta, true);

                            while (rs.next()) {
                                pedidos.setIdCliente(rs.getInt("CT_ID"));
                                blExisteCt = true;
                            }
                            rs.close();
                        }
                        if (!blExisteCt) {
                            vta_clientes cte = new vta_clientes();
                            //crear idcte
                            cte.setFieldString("CT_RAZONSOCIAL", pedidos.getCtRazonSocial());
                            cte.setFieldString("CT_RFC", pedidos.getCtRFC());
                            cte.setFieldString("CT_CALLE", pedidos.getCtCalle());
                            cte.setFieldString("CT_COLONIA", pedidos.getCtColonia());
                            cte.setFieldString("CT_MUNICIPIO", pedidos.getCtMunicipio());
                            cte.setFieldString("CT_ESTADO", pedidos.getCtEstado());
                            cte.setFieldString("CT_CP", pedidos.getCtCp());
                            cte.setFieldString("CT_EMAIL1", pedidos.getEmail1());
                            cte.setFieldString("CT_EMAIL2", pedidos.getEmail2());
                            cte.setFieldString("CT_NUMERO", pedidos.getCtNumero());
                            cte.setFieldString("CT_NUMINT", pedidos.getCtNumInterior());
                            cte.setFieldInt("EMP_ID", pedidos.getIdEmpresa());
                            cte.setFieldInt("SC_ID", pedidos.getIdsucursal());
                            cte.setFieldInt("PA_ID", 0);
                            cte.Agrega(oConn);
                            intCt_Id = Integer.parseInt(cte.getValorKey());
                            pedidos.setIdCliente(intCt_Id);
                        }

                    }
                    
                    String calle_nwEntrega = "";
                    String colonia_nwEntrega = "";
                    String municipio_nwEntrega = "";
                    String estado_nwEntrega = "";
                    String cp_fact_nwEntrega = "";
                    String numero_nwEntrega = "";
                    String numeroInt_nwEntrega = "";
                    String nombre_nwEntrega = "";
                    String email_nwEntrega = "";

                    Iterator<DirEntrega> ite = pedidos.getLstItems1().iterator();
                    while (ite.hasNext()) {
                        DirEntrega direntrega = ite.next();
                        calle_nwEntrega = (direntrega.getCalle_nw());
                        colonia_nwEntrega = (direntrega.getColonia_nw());
                        municipio_nwEntrega = (direntrega.getMunicipio_nw());
                        estado_nwEntrega = (direntrega.getEstado_nw());
                        cp_fact_nwEntrega = (direntrega.getCp_fact_nw());
                        numero_nwEntrega = (direntrega.getNumero_nw());
                        numeroInt_nwEntrega = (direntrega.getNumeroInt_nw());
                        nombre_nwEntrega = (direntrega.getNombre_nw());
                        email_nwEntrega = (direntrega.getEmail_nw());
                    }

                    //Inicializamos datos
                    Fechas fecha = new Fechas();
                    //Recuperamos paths de web.xml
                    String strPathXML = servletContext.getInitParameter("PathXml");
                    String strfolio_GLOBAL = servletContext.getInitParameter("folio_GLOBAL");
                    String strmod_Inventarios = servletContext.getInitParameter("mod_Inventarios");
                    String strSist_Costos = servletContext.getInitParameter("SistemaCostos");
                    String strPathPrivateKeys = servletContext.getInitParameter("PathPrivateKey");
                    String strPathFonts = servletContext.getRealPath("/") + System.getProperty("file.separator") + "fonts";

                    if (strfolio_GLOBAL == null) {
                        strfolio_GLOBAL = "SI";
                    }
                    if (strmod_Inventarios == null) {
                        strmod_Inventarios = "NO";
                    }
                    if (strSist_Costos == null) {
                        strSist_Costos = "0";
                    }

                    //Instanciamos el objeto que generara la venta
                    Ticket ticket = new Ticket(oConn, varSesiones, servletRequest);
                    ticket.setStrPATHKeys(strPathPrivateKeys);
                    ticket.setStrPATHXml(strPathXML);
                    ticket.setStrPATHFonts(strPathFonts);

                    log.debug("(//////////// inventarios ////////////// ");
                    log.debug("strmod_Inventarios = " + strmod_Inventarios);

                    //Desactivamos inventarios
                    if (strmod_Inventarios.equals("NO")) {
                        ticket.setBolAfectaInv(false);
                        log.debug("ticket.setBolAfectaInv(false)");
                    } else {
                        ticket.setBolAfectaInv(true);
                        log.debug("ticket.setBolAfectaInv(true)");
                    }

                    log.debug("pedidos.getInv() " + pedidos.getInv());
                    //Validamos si envian la peticion con la bandera de afectar inventarios
                    if (pedidos.getInv() != null) {
                        if (pedidos.getInv().equals("0")) {
                            ticket.setBolAfectaInv(false);
                            log.debug("ticket.setBolAfectaInv(false)");
                        } else {
                            ticket.setBolAfectaInv(true);
                            log.debug("ticket.setBolAfectaInv(true)");
                        }
                    }
                    log.debug("(//////////// inventarios ////////////// ");
                    //Definimos el sistema de costos
                    try {
                        ticket.setIntSistemaCostos(Integer.valueOf(strSist_Costos));
                        log.debug("(//////////// El sistema de costos es " + Integer.valueOf(strSist_Costos));
                    } catch (NumberFormatException ex) {
                        log.error("No hay sistema de costos definido");
                    }

                    //Recibimos parametros
                    String strPrefijoMaster = "TKT";
                    String strPrefijoDeta = "TKTD";
                    String strTipoVtaNom = Ticket.TICKET;
                    String strTipoFormat = "TICKET";
                    String strNomTemplate = "TICKET_MAI";
                    //Recuperamos el tipo de venta 1:FACTURA 2:TICKET 3:PEDIDO
                    String strTipoVta = Integer.toString(pedidos.getTipoVenta());
                    if (strTipoVta == null) {
                        log.debug("Entro a TICKET strTipoVta.equals(2) ");
                        strTipoVta = "2";
                    }
                    if (strTipoVta.equals("1")) {
                        strPrefijoMaster = "FAC";
                        strPrefijoDeta = "FACD";
                        strTipoVtaNom = Ticket.FACTURA;
                        ticket.initMyPass(servletContext);
                        strTipoFormat = "FACTURA";
                        strNomTemplate = "FACTURA";
                    }
                    if (strTipoVta.equals("2")) {
                        strPrefijoMaster = "TKT";
                        strPrefijoDeta = "TKTD";
                        strTipoVtaNom = Ticket.TICKET;
                        strTipoFormat = "TICKET";
                        strNomTemplate = "TICKET_MAI";
                    }
                    if (strTipoVta.equals("3")) {
                        log.debug("////////////  Entro a pedido");
                        strPrefijoMaster = "PD";
                        strPrefijoDeta = "PDD";
                        strTipoVtaNom = Ticket.PEDIDO;
                        strTipoFormat = "PEDIDO";
                        strNomTemplate = "PEDIDO_MAI";
                    }
                    if (strTipoVta.equals("4")) {
                        log.debug("////////////  Entro a COTIZACION");
                        strPrefijoMaster = "PD";
                        strPrefijoDeta = "PDD";
                        strTipoVtaNom = Ticket.COTIZACION;
                        strTipoFormat = "COTIZA";
                        strNomTemplate = "COTIZA_MAI";
                    }

                    log.debug("////////////  Tipo de Venta es " + strTipoVtaNom);
                    ticket.setStrTipoVta(strTipoVtaNom);
                    //Validamos si tenemos un empresa seleccionada
                    if (webBase.getIntEMP_ID() != 0) {

                        //Asignamos la empresa seleccionada
                        ticket.setIntEMP_ID(webBase.getIntEMP_ID());
                        log.debug("////////////  La empresa es " + webBase.getIntEMP_ID());
                    }
                    //Validamos si usaremos un folio global
                    log.debug("////////////  Validamos si usaremos un folio global");
                    if (strfolio_GLOBAL.equals("NO")) {
                        ticket.setBolFolioGlobal(false);
                    }
                    //Recibimos datos para el encabezado
                    int intPD_ID = 0;
                    if (pedidos.getIdPedido() != 0) {
                        //Validamos si recibimos una lista de pedidos
                        log.debug("//////////// Validamos si recibimos una lista de pedidos");
                        if (!pedidos.getFacPedi().equals(0)) {
                            log.debug("//////////// FacPedi Diferente de 0");
                            if (pedidos.getFacPedi().contains("1")) {
                                ticket.getListPedidos(String.valueOf(pedidos.getIdPedido()));
                                log.debug("//////////// FacPedi Diferente de 0--contiene un 1");
                            }
                        } else {
                            log.debug("//////////// FacPedi Igual de 0");
                            try {
                                log.debug("//////////// FacPedi Igual de 0--Entro try");
                                intPD_ID = Integer.valueOf(pedidos.getIdPedido());
                                log.debug("//////////// Asignamos valor a pd_id" + intPD_ID);
                            } catch (NumberFormatException ex) {
                                log.error("ERP_Ventas PD_ID " + ex.getMessage());
                            }
                        }
                    }
                    ticket.setIntPedidoGenero(intPD_ID);
                    if (strTipoVta.equals("1") || strTipoVta.equals("2")) {
                        log.debug("//////////// PD_ID = " + intPD_ID);
                        ticket.getDocument().setFieldInt("PD_ID", intPD_ID);
                    } else {
                        log.debug("//////////// Entro al else");
                        //Edicion de un pedido
                        if (strTipoVta.equals("3")) {
                            log.debug("//////////// PD_ID = " + intPD_ID);
                            //Si llega el campo de pedido entonces estamos editando un pedido
                            if (pedidos.getIdPedido() != 0) {
                                ticket.getDocument().setFieldInt("PD_ID", intPD_ID);
                                log.debug("//////////// PD_ID = " + intPD_ID + " es diferente de 0");
                                //Validamos si la modificacion de un pedido
                                if (ticket.getDocument().getFieldInt("PD_ID") != 0) {
                                    //Generamos transaccion
                                    ticket.getDocument().setValorKey(ticket.getDocument().getFieldInt("PD_ID") + "");
                                    ticket.Init();
                                    log.debug("////////////  ticket.Init();");
                                }
                            }
                        }
                    }

                    ticket.getDocument().setFieldInt("SC_ID", Integer.valueOf(webBase.getIntSC_ID()));
                    ticket.getDocument().setFieldInt("CT_ID", Integer.valueOf(pedidos.getIdCliente()));

                    if (Integer.valueOf(moneda) == 0) {
                        ticket.getDocument().setFieldInt(strPrefijoMaster + "_MONEDA", 1);
                        log.debug("//////////// Primer IF MONEDA = 1");
                    } else {
                        ticket.getDocument().setFieldInt(strPrefijoMaster + "_MONEDA", Integer.valueOf(moneda));
                        log.debug("//////////// MONEDA ELSE MONEDA " + Integer.valueOf(moneda));
                    }

                    //Clave de vendedor
                    int intVE_ID = 0;
                    try {
                        intVE_ID = Integer.valueOf(pedidos.getIdVendedor());
                    } catch (NumberFormatException ex) {
                        log.error("ERP_Ventas VE_ID " + ex.getMessage());
                    }
                    //Tarifas de IVA
                    int intTI_ID = 0;
                    int intTI_ID2 = 0;
                    int intTI_ID3 = 0;
                    try {
                        intTI_ID = Integer.valueOf(pedidos.getIdTi());
                        intTI_ID2 = Integer.valueOf(pedidos.getIdTi2());
                        intTI_ID3 = Integer.valueOf(pedidos.getIdTi3());
                    } catch (NumberFormatException ex) {
                        log.error("ERP_Ventas TI_ID " + ex.getMessage());
                    }

                    //Tipo de comprobante
                    int intFAC_TIPOCOMP = 0;
                    try {
                        intFAC_TIPOCOMP = Integer.valueOf(pedidos.getFacTipocomp());
                    } catch (NumberFormatException ex) {
                        log.error("ERP_Ventas FAC_TIPOCOMP " + ex.getMessage());
                    }
                    
                    //Asignamos los valores al objeto
                    ticket.getDocument().setFieldInt("VE_ID", intVE_ID);
                    ticket.getDocument().setFieldInt("TI_ID", intTI_ID);
                    ticket.getDocument().setFieldInt("TI_ID2", intTI_ID2);
                    ticket.getDocument().setFieldInt("TI_ID3", intTI_ID3);
                    ticket.setIntFAC_TIPOCOMP(intFAC_TIPOCOMP);
                    ticket.getDocument().setFieldInt(strPrefijoMaster + "_ESSERV", pedidos.getEsserv());
                    ticket.getDocument().setFieldString(strPrefijoMaster + "_FECHA", fecha.FormateaBD(pedidos.getFecha(), "/"));
//                    ticket.getDocument().setFieldString(strPrefijoMaster + "_FOLIO", pedidos.getFolio());
                    ticket.getDocument().setFieldString(strPrefijoMaster + "_NOTAS", pedidos.getNotas());
                    ticket.getDocument().setFieldString(strPrefijoMaster + "_NOTASPIE", pedidos.getNotasPie());
                    ticket.getDocument().setFieldString(strPrefijoMaster + "_REFERENCIA", pedidos.getReferencia());
                    ticket.getDocument().setFieldString(strPrefijoMaster + "_CONDPAGO", pedidos.getCondpago());
                    
                    if (pedidos.getMetodopago() != null) {
                        ticket.getDocument().setFieldString(strPrefijoMaster + "_METODODEPAGO", pedidos.getMetodopago());
                        log.debug("//////////// Metodo de pago " + pedidos.getMetodopago());
                    }
                    
                    if (pedidos.getNumcuenta() != null) {
                        ticket.getDocument().setFieldString(strPrefijoMaster + "_NUMCUENTA", pedidos.getNumcuenta());
                        log.debug("//////////// Numero de Cuenta" + pedidos.getNumcuenta());
                    }
                    
                    ticket.getDocument().setFieldString(strPrefijoMaster + "_FORMADEPAGO", pedidos.getFormadepago());
                    //ticket.getDocument().setFieldString(strPrefijoMaster + "_FORMADEPAGO", "En una sola Exhibicion");
                    ticket.getDocument().setFieldDouble(strPrefijoMaster + "_IMPORTE", Double.valueOf(pedidos.getImporte()));
                    ticket.getDocument().setFieldDouble(strPrefijoMaster + "_IMPUESTO1", Double.valueOf(pedidos.getImpuesto1()));
                    ticket.getDocument().setFieldDouble(strPrefijoMaster + "_IMPUESTO2", Double.valueOf(pedidos.getImpuesto2()));
                    ticket.getDocument().setFieldDouble(strPrefijoMaster + "_IMPUESTO3", Double.valueOf(pedidos.getImpuesto3()));
                    ticket.getDocument().setFieldDouble(strPrefijoMaster + "_TOTAL", Double.valueOf(pedidos.getTotal()));
                    ticket.getDocument().setFieldDouble(strPrefijoMaster + "_TASA1", Double.valueOf(pedidos.getTasa1()));
                    ticket.getDocument().setFieldDouble(strPrefijoMaster + "_TASA2", Double.valueOf(pedidos.getTasa2()));
                    ticket.getDocument().setFieldDouble(strPrefijoMaster + "_TASA3", Double.valueOf(pedidos.getTasa3()));

                    if (pedidos.getTasaPeso() != 0) {
                        if (Double.valueOf(pedidos.getTasaPeso()) == 0) {
                            ticket.getDocument().setFieldDouble(strPrefijoMaster + "_TASAPESO", 1);
                        } else {
                            ticket.getDocument().setFieldDouble(strPrefijoMaster + "_TASAPESO", Double.valueOf(pedidos.getTasaPeso()));
                        }
                    } else {
                        ticket.getDocument().setFieldDouble(strPrefijoMaster + "_TASAPESO", 1);
                    }

                    //Validamos IEPS
                    if (pedidos.getUsoIeps() != 0) {
                        try {
                            ticket.getDocument().setFieldInt(strPrefijoMaster + "_USO_IEPS", Integer.valueOf(pedidos.getUsoIeps()));
                            ticket.getDocument().setFieldInt(strPrefijoMaster + "_TASA_IEPS", Integer.valueOf(pedidos.getTasaIeps()));
                            ticket.getDocument().setFieldDouble(strPrefijoMaster + "_IMPORTE_IEPS", Double.valueOf(pedidos.getImporteIeps()));
                        } catch (NumberFormatException ex) {
                        }
                    }

                    //Validamos CONSIGNACION
                    if (pedidos.getConsignacion() != 0) {
                        try {
                            ticket.getDocument().setFieldInt(strPrefijoMaster + "_CONSIGNACION", Integer.valueOf(pedidos.getConsignacion()));
                        } catch (NumberFormatException ex) {
                        }
                    }
                    
                    //Validamos MLM
                    if (pedidos.getPuntos() != 0) {
                        try {
                            ticket.getDocument().setFieldDouble(strPrefijoMaster + "_IMPORTE_PUNTOS", Double.valueOf(pedidos.getPuntos()));
                            ticket.getDocument().setFieldDouble(strPrefijoMaster + "_IMPORTE_NEGOCIO", Double.valueOf(pedidos.getNegocio()));
                        } catch (NumberFormatException ex) {
                        }
                    }
                    
                    //Datos de la aduana
                    ticket.getDocument().setFieldString(strPrefijoMaster + "_NUMPEDI", pedidos.getNumPedi());
                    String strFechaPedimento = pedidos.getFechapedi();
                    if (strFechaPedimento.contains("/") && strFechaPedimento.length() == 10) {
                        strFechaPedimento = fecha.FormateaBD(strFechaPedimento, "/");
                    }
                    ticket.getDocument().setFieldString(strPrefijoMaster + "_FECHAPEDI", strFechaPedimento);
                    ticket.getDocument().setFieldString(strPrefijoMaster + "_ADUANA", pedidos.getAduana());

                    //Si no hay moneda seleccionada que ponga tasa 1
                    if (Integer.valueOf(moneda) == 0) {
                        ticket.getDocument().setFieldDouble(strPrefijoMaster + "_TASAPESO", 1);
                    }
                    if (pedidos.getDiasCredito() != 0) {
                        if (Double.valueOf(pedidos.getDiaper()) == 0) {
                            ticket.getDocument().setFieldDouble(strPrefijoMaster + "_DIASCREDITO", 1);
                        }
                    }
                    //Validamos parametros para recibos de honorarios}
                    if (pedidos.getRetIsr() != 0 && strTipoVtaNom.equals(Ticket.FACTURA)) {
                        ticket.getDocument().setFieldDouble(strPrefijoMaster + "_RETISR", Double.valueOf(pedidos.getRetIsr()));
                    }
                    if (pedidos.getRetIva() != 0 && strTipoVtaNom.equals(Ticket.FACTURA)) {
                        ticket.getDocument().setFieldDouble(strPrefijoMaster + "_RETIVA", Double.valueOf(pedidos.getRetIva()));
                    }
                    if (pedidos.getNeto() != 0 && strTipoVtaNom.equals(Ticket.FACTURA)) {
                        ticket.getDocument().setFieldDouble(strPrefijoMaster + "_NETO", Double.valueOf(pedidos.getNeto()));
                    }
                    
                    //Opciones de facturacion recurrente
                    if (pedidos.getEsrecu() != 0) {
                        int intEsRecu = 0;
                        int intPeriodicidad = 1;
                        int intDiaPer = 1;
                        try {
                            intEsRecu = Integer.valueOf(pedidos.getEsrecu());
                        } catch (NumberFormatException ex) {
                            log.error("Ventas: Error convertir campo " + strPrefijoMaster + "_ESRECU" + " " + ex.getMessage());
                        }
                        try {
                            intPeriodicidad = Integer.valueOf(pedidos.getPeriodicidad());
                        } catch (NumberFormatException ex) {
                            log.error("Ventas: Error convertir campo " + strPrefijoMaster + "_PERIODICIDAD" + " " + ex.getMessage());
                        }

                        try {
                            intDiaPer = Integer.valueOf(pedidos.getDiaper());
                        } catch (NumberFormatException ex) {
                            log.error("Ventas: Error convertir campo " + strPrefijoMaster + "_DIAPER" + " " + ex.getMessage());
                        }
                        ticket.getDocument().setFieldInt(strPrefijoMaster + "_ESRECU", intEsRecu);
                        ticket.getDocument().setFieldInt(strPrefijoMaster + "_PERIODICIDAD", intPeriodicidad);
                        ticket.getDocument().setFieldInt(strPrefijoMaster + "_DIAPER", intDiaPer);
                    }

                    //Recibimos el regimen fiscal
                    if (pedidos.getRegimenFiscal() != null) {
                        ticket.getDocument().setFieldString(strPrefijoMaster + "_REGIMENFISCAL", pedidos.getRegimenFiscal());
                    }

                    //Recibimos datos de los items o partidas
                    log.debug("###############################################################################");

                    Iterator<PedidosDeta> it = pedidos.getLstItems().iterator();
                    while (it.hasNext()) {
                        PedidosDeta pedidosdeta = it.next();

                        TableMaster deta = null;
                        if (strTipoVtaNom.equals(Ticket.TICKET)) {
                            deta = new vta_ticketsdeta();
                        }
                        if (strTipoVtaNom.equals(Ticket.FACTURA)) {
                            deta = new vta_facturadeta();
                        }
                        if (strTipoVtaNom.equals(Ticket.PEDIDO)) {
                            deta = new vta_pedidosdeta();
                        }
                        if (strTipoVtaNom.equals(Ticket.COTIZACION)) {
                            deta = new vta_cotizadeta();
                        }
                        deta.setFieldInt("SC_ID", Integer.valueOf(webBase.getIntSC_ID()));

                        String strCodigo = "";
                        String strSql = "select PR_ID from vta_producto where pr_codigo = '" + pedidosdeta.getCodigo() + "' and SC_ID =" + webBase.getIntSC_ID() + " ;";
                        rs = oConn.runQuery(strSql, true);
                        while (rs.next()) {
                            strCodigo = rs.getString("PR_ID");
                        }
                        rs.close();

                        deta.setFieldInt("PR_ID", Integer.valueOf(strCodigo));
                        log.debug("PR_ID " + strCodigo);
                        deta.setFieldInt(strPrefijoDeta + "_EXENTO1", Integer.valueOf(pedidosdeta.getExento1()));
                        deta.setFieldInt(strPrefijoDeta + "_EXENTO2", Integer.valueOf(pedidosdeta.getExento2()));
                        deta.setFieldInt(strPrefijoDeta + "_EXENTO3", Integer.valueOf(pedidosdeta.getExento3()));
                        deta.setFieldInt(strPrefijoDeta + "_ESREGALO", Integer.valueOf(pedidosdeta.getEsRegalo()));
                        deta.setFieldString(strPrefijoDeta + "_CVE", pedidosdeta.getCodigo());
                        log.debug("Descripcion " + pedidosdeta.getDescripcion());
                        deta.setFieldString(strPrefijoDeta + "_DESCRIPCION", pedidosdeta.getDescripcion());
                        //Cambiar el objeto
                        deta.setFieldString(strPrefijoDeta + "_NOSERIE", pedidosdeta.getNoSerie());
                        deta.setFieldDouble(strPrefijoDeta + "_IMPORTE", Double.valueOf(pedidosdeta.getImporte()));
                        deta.setFieldDouble(strPrefijoDeta + "_CANTIDAD", Double.valueOf(pedidosdeta.getCantidad()));
                        deta.setFieldDouble(strPrefijoDeta + "_TASAIVA1", Double.valueOf(pedidosdeta.getTasaIva1()));
                        deta.setFieldDouble(strPrefijoDeta + "_TASAIVA2", Double.valueOf(pedidosdeta.getTasaIva2()));
                        deta.setFieldDouble(strPrefijoDeta + "_TASAIVA3", Double.valueOf(pedidosdeta.getTasaIva3()));
                        deta.setFieldDouble(strPrefijoDeta + "_IMPUESTO1", Double.valueOf(pedidosdeta.getImpuesto1()));
                        deta.setFieldDouble(strPrefijoDeta + "_IMPUESTO2", Double.valueOf(pedidosdeta.getImpuesto2()));
                        deta.setFieldDouble(strPrefijoDeta + "_IMPUESTO3", Double.valueOf(pedidosdeta.getImpuesto3()));
                        deta.setFieldDouble(strPrefijoDeta + "_IMPORTEREAL", Double.valueOf(pedidosdeta.getImporteReal()));
                        deta.setFieldDouble(strPrefijoDeta + "_PRECIO", Double.valueOf(pedidosdeta.getPrecio()));
                        deta.setFieldDouble(strPrefijoDeta + "_DESCUENTO", Double.valueOf(pedidosdeta.getDescuento()));
                        deta.setFieldDouble(strPrefijoDeta + "_PORDESC", Double.valueOf(pedidosdeta.getPordesc()));
                        deta.setFieldDouble(strPrefijoDeta + "_PRECREAL", Double.valueOf(pedidosdeta.getPrecReal()));
                        //Retencion de ISR
                        if ((pedidosdeta.getRetIsr()) != 0) {
                            try {
                                deta.setFieldDouble(strPrefijoDeta + "_RET_ISR", Integer.valueOf(pedidosdeta.getRetIsr()));
                                deta.setFieldDouble(strPrefijoDeta + "_RET_IVA", Integer.valueOf(pedidosdeta.getRetIva()));
                                deta.setFieldDouble(strPrefijoDeta + "_RET_FLETE", Integer.valueOf(pedidosdeta.getRetFlete()));
                            } catch (NumberFormatException ex) {
                                log.error("EN ERP_Ventas falta definir retencion ISR");
                            }

                        }
                        //Solo aplica si es ticket o factura
                        if (strTipoVtaNom.equals(Ticket.TICKET) || strTipoVtaNom.equals(Ticket.FACTURA)) {
                            deta.setFieldInt(strPrefijoDeta + "_ESDEVO", Integer.valueOf(pedidosdeta.getEsdevo()));
                        }
                        deta.setFieldInt(strPrefijoDeta + "_PRECFIJO", Integer.valueOf(pedidosdeta.getPrecfijo()));
                        deta.setFieldInt(strPrefijoDeta + "_ESREGALO", Integer.valueOf(pedidosdeta.getEsRegalo()));
                        deta.setFieldString(strPrefijoDeta + "_COMENTARIO", pedidosdeta.getNotas());
                        //UNIDAD DE MEDIDA UNIDAD_MEDIDA
                        if ((pedidosdeta.getUnidadMedida()) != null) {
                            deta.setFieldString(strPrefijoDeta + "_UNIDAD_MEDIDA", pedidosdeta.getUnidadMedida());
                        }
                        //Evaluamos si envian el id del pedido
                        if (servletRequest.getParameter("PDD_ID") != null) {/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                            deta.setFieldInt("PDD_ID", Integer.valueOf(servletRequest.getParameter("PDD_ID")));//////////////////////////////////////////////////////////////////////////////////////////////////////
                        }
                        //Validamos MLM
                        if ((pedidosdeta.getPuntos()) != 0) {
                            try {
                                deta.setFieldDouble(strPrefijoDeta + "_PUNTOS", Double.valueOf(pedidosdeta.getPuntos()));
                                deta.setFieldDouble(strPrefijoDeta + "_VNEGOCIO", Double.valueOf(pedidosdeta.getVnegocio()));
                                deta.setFieldDouble(strPrefijoDeta + "_IMP_PUNTOS", Double.valueOf(pedidosdeta.getImpPuntos()));
                                deta.setFieldDouble(strPrefijoDeta + "_IMP_VNEGOCIO", Double.valueOf(pedidosdeta.getImpVnegocio()));
                                deta.setFieldDouble(strPrefijoDeta + "_DESC_ORI", Double.valueOf(pedidosdeta.getDescori()));
                                deta.setFieldInt(strPrefijoDeta + "_DESC_PREC", Integer.valueOf(pedidosdeta.getDescprec()));
                                deta.setFieldInt(strPrefijoDeta + "_DESC_PUNTOS", Integer.valueOf(pedidosdeta.getDescpto()));
                                deta.setFieldInt(strPrefijoDeta + "_DESC_VNEGOCIO", Integer.valueOf(pedidosdeta.getDescvn()));
                                deta.setFieldInt(strPrefijoDeta + "_REGALO", Integer.valueOf(pedidosdeta.getRegalo()));
                                deta.setFieldInt(strPrefijoDeta + "_ID_PROMO", Integer.valueOf(pedidosdeta.getIdPromo()));
                            } catch (NumberFormatException ex) {
                                log.error("Error al recuperar los valores de MLM " + ex.getMessage() + " " + ex.getLocalizedMessage() + " " + ex.toString());
                                ex.fillInStackTrace();
                            }
                        }
                        //Validamos si estan enviando datos de movimientos de surtido de numeros de serie
                        if ((pedidosdeta.getSeriesmpd()) != null) {
                            String[] lstSeriesMPD = (pedidosdeta.getSeriesmpd()).split(",");
                            for (int iSerM = 0; iSerM < lstSeriesMPD.length; iSerM++) {
                                int intMPD_ID = 0;
                                try {
                                    intMPD_ID = Integer.valueOf(lstSeriesMPD[iSerM]);
                                    ticket.addItemLstSeries(intMPD_ID);
                                } catch (NumberFormatException ex) {
                                }
                            }
                        }

                        //
                        ticket.AddDetalle(deta);
                    }
                    log.debug("###############################################################################");

                    //Direccion de entrega adicional
                    String appDirEntrega = String.valueOf(pedidos.getAppDirEntrega());
                    if (appDirEntrega == null) {
                        appDirEntrega = "0";
                    }
                    if (appDirEntrega.equals("1")) {

                        //Obtenemos el nombre de la sucursal default
                        int intEMP_ID = 0;
                        String strSql = "select EMP_ID "
                                + " from vta_cliente "
                                + " where  vta_cliente.CT_ID = " + pedidos.getIdCliente();
                        rs = oConn.runQuery(strSql, true);
                        while (rs.next()) {
                            intEMP_ID = rs.getInt("EMP_ID");
                        }
                        rs.close();
                        //Agregamos con el objeto de la direccion de entrega
                        vta_cliente_dir_entrega dirEntrega = new vta_cliente_dir_entrega();
                        dirEntrega.setBolGetAutonumeric(true);
                        dirEntrega.setFieldString("CDE_CALLE", calle_nwEntrega);
                        dirEntrega.setFieldString("CDE_COLONIA", colonia_nwEntrega);
                        //dirEntrega.setFieldString("CDE_LOCALIDAD", request.getParameter(""));
                        dirEntrega.setFieldString("CDE_MUNICIPIO", municipio_nwEntrega);
                        dirEntrega.setFieldString("CDE_ESTADO", estado_nwEntrega);
                        dirEntrega.setFieldString("CDE_CP", cp_fact_nwEntrega);
                        dirEntrega.setFieldString("CDE_NUMERO", numero_nwEntrega);
                        dirEntrega.setFieldString("CDE_NUMINT", numeroInt_nwEntrega);
                        dirEntrega.setFieldInt("EMP_ID", intEMP_ID);
                        dirEntrega.setFieldString("CDE_DESCRIPCION", "");
                        dirEntrega.setFieldInt("CT_ID", pedidos.getIdCliente());
                        dirEntrega.setFieldString("CDE_NOMBRE", nombre_nwEntrega);
                        dirEntrega.setFieldString("CDE_EMAIL", email_nwEntrega);
                        dirEntrega.Agrega(oConn);
                        int intCDE_ID = 0;
                        try {
                            intCDE_ID = Integer.valueOf(dirEntrega.getValorKey());
                            ticket.getDocument().setFieldInt("CDE_ID", intCDE_ID);
                        } catch (NumberFormatException ex) {
                            log.error("Error in CDE_ID");
                        }
                    }

                    //Validamos si es un pedido que se esta editando para solo modificar el pedido anterior
                    if (strTipoVta.equals("3") && ticket.getDocument().getFieldInt("PD_ID") != 0) {
                        log.debug("//////////// Antes de doTrxMod");
                        ticket.doTrxMod();
                        log.debug("//////////// Despues doTrxMod");
                    } else {
                        log.debug("//////////// Antes de Init");
                        ticket.Init();
                        log.debug("//////////// Ejecuta Init");
                        //Generamos transaccion
                        log.debug("//////////// Antes de doTrx");
                        ticket.doTrx();
                        log.debug("//////////// Despues doTrx");
                    }
                    String strRes = "";
                    if (ticket.getStrResultLast().equals("OK")) {

                        strRes = ticket.getDocument().getFieldString(strPrefijoMaster + "_FOLIO");

                        //Envio del pedido por mail
                        String strMailCte = getMail(pedidos.getIdCliente(), oConn);
                        if (!strMailCte.isEmpty()) {
                            String strRespPdf = GeneraImpresionPDF(oConn, strPathXML, strTipoFormat, ticket.getDocument().getFieldString(strPrefijoMaster + "_FOLIO"), Integer.valueOf(ticket.getDocument().getValorKey()),
                                    strPathFonts, strTipoFormat);
                            log.debug("//////////// 1-1");

                            if (strRespPdf.equals("OK")) {
                                String strRespEnvio = GeneraMail(oConn, strMailCte, "",
                                        ticket.getDocument().getFieldString(strPrefijoMaster + "_FOLIO"),
                                        strPathXML, strNomTemplate, strTipoFormat);
                            }
                        }

                        Iterator<PedidosDeta> itp = pedidos.getLstItems().iterator();
                        while (itp.hasNext()) {
                            PedidosDeta pedidosdeta = itp.next();

                            String strExistencia = "";

                            String strSql1 = "select PR_EXISTENCIA from vta_producto where pr_codigo = '" + pedidosdeta.getCodigo() + "' and SC_ID =" + webBase.getIntSC_ID() + " ;";
                            ResultSet rspr = oConn.runQuery(strSql1, true);
                            while (rspr.next()) {
                                strExistencia = rspr.getString("PR_EXISTENCIA");
                            }
                            rspr.close();
                            pedidosdeta.setExistenciaActual(strExistencia);
                        }

                    } else {
                        strRes = ticket.getStrResultLast();
                    }

                    log.debug("//////////// Respuesta  " + strRes);
                    pedidos.setNumPedi(strRes);

                }

            } else {
                pedidos.setNotas("Error:Access Denied");
            }

            oConn.close();
        } catch (Exception ex) {
            log.error("MobilServiceLogin1" + ex.getMessage() + " " + ex.getLocalizedMessage());
        }
        return Response.ok(pedidos).build();

    }

    /**
     * Funciones
     */
    /**
     * Envia el mail al cliente
     *
     * @param strMailCte Es el mail del cliente
     * @param strMailCte2 Es el segundo mail del cli
     * @param strNomTemplateente
     * @param strFolio Es el folio
     * @param strPath Es el path donde se alojara temporalmente el pdf
     * @param strNomTemplate
     * @param abrFormato
     * @return Regresa OK si fue exitoso el envio del mail
     */
    protected String GeneraMail(Conexion oConn, String strMailCte, String strMailCte2,
            String strFolio,
            String strPath,
            String strNomTemplate,
            String abrFormato) {
        String strResp = "OK";
        //Nombre de archivo
        //Obtenemos datos del smtp
        String strsmtp_server = "";
        String strsmtp_user = "";
        String strsmtp_pass = "";
        String strsmtp_port = "";
        String strsmtp_usaTLS = "";
        String strsmtp_usaSTLS = "";
        //Buscamos los datos del SMTP
        String strSql = "select * from cuenta_contratada where ctam_id = 1";
        ResultSet rs;
        try {
            rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                strsmtp_server = rs.getString("smtp_server");
                strsmtp_user = rs.getString("smtp_user");
                strsmtp_pass = rs.getString("smtp_pass");
                strsmtp_port = rs.getString("smtp_port");
                strsmtp_usaTLS = rs.getString("smtp_usaTLS");
                strsmtp_usaSTLS = rs.getString("smtp_usaSTLS");
            }
            rs.close();
        } catch (SQLException ex) {
            log.error(ex.getMessage());
        }
        //Obtenemos los textos para el envio del mail

        String[] lstMail = getMailTemplate(oConn, strNomTemplate);

        /**
         * Si estan llenos todos los datos mandamos el mail
         */
        if (!strsmtp_server.equals("")
                && !strsmtp_user.equals("")
                && !strsmtp_pass.equals("")) {
            //armamos el mail
            Mail mail = new Mail();
            //Activamos envio de acuse de recibo
            mail.setBolAcuseRecibo(true);
            //Obtenemos los usuarios a los que mandaremos el mail
            String strLstMail = "";
            //Validamos si el mail del cliente es valido
            if (mail.isEmail(strMailCte)) {
                strLstMail += "," + strMailCte;
            }
            if (mail.isEmail(strMailCte2)) {
                strLstMail += "," + strMailCte2;
            }
            //Mandamos mail si hay usuarios
            if (!strLstMail.equals("")) {
                String strMsgMail = lstMail[1];
                strMsgMail = strMsgMail.replace("%folio%", strFolio);
                //Establecemos parametros
                mail.setUsuario(strsmtp_user);
                mail.setContrasenia(strsmtp_pass);
                mail.setHost(strsmtp_server);
                mail.setPuerto(strsmtp_port);
                mail.setAsunto(lstMail[0].replace("%folio%", strFolio));
                mail.setDestino(strLstMail);
                mail.setMensaje(strMsgMail);
                //Adjuntamos XML y PDF
                mail.setFichero(strPath + abrFormato + "_web" + strFolio + ".pdf");

                if (strsmtp_usaTLS.equals("1")) {
                    mail.setBolUsaTls(true);
                }
                if (strsmtp_usaSTLS.equals("1")) {
                    mail.setBolUsaStartTls(true);
                }
                boolean bol = mail.sendMail();
                if (!bol) {
                    strResp = "Fallo el envio del Mail.";
                }
            }
        }
        return strResp;
    }

    /**
     * Obtenemos los valores del template para el mail
     *
     * @param strNom Es el nombre del template
     * @return Regresa un arreglo con los valores del template
     */
    public String[] getMailTemplate(Conexion oConn, String strNom) {
        String[] listValores = new String[2];
        String strSql = "select MT_ASUNTO,MT_CONTENIDO from mailtemplates where MT_ABRV ='" + strNom + "'";
        ResultSet rs;
        try {
            rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                listValores[0] = rs.getString("MT_ASUNTO");
                listValores[1] = rs.getString("MT_CONTENIDO");
            }
            rs.close();
        } catch (SQLException ex) {
            log.error(ex.getMessage());
        }
        return listValores;
    }

    /**
     * Genera el formato de impresion en PDF
     *
     * @param strPath Es el path
     * @param intEMP_TIPOCOMP Es el tipo de comprobante
     * @param intEmpId Es el id de la empresa
     * @param strFAC_NOMFORMATO Es el nombre del formato
     * @return Regresa OK si se genero el formato
     */
    protected String GeneraImpresionPDF(Conexion oConn, String strPath,
            String strFAC_NOMFORMATO, String strFolio, int intTransaccion,
            String strPATHFonts, String abrFormato) {
        String strResp = "OK";
        //Posicion inicial para el numero de pagina
        String strPosX = null;
        String strTitle = "";
        strTitle = "Factura ";

        try {
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(strPath + abrFormato + "_web" + strFolio + ".pdf"));
            //Objeto que dibuja el numero de paginas
            PDFEventPage pdfEvent = new PDFEventPage();
            pdfEvent.setStrTitleApp(strTitle);
            //Colocamos el numero donde comienza X por medio del parametro del web Xml por si necesitamos algun ajuste
            if (strPosX != null) {
                try {
                    int intPosX = Integer.valueOf(strPosX);
                    pdfEvent.setIntXPageNum(intPosX);
                } catch (NumberFormatException ex) {
                }
            } else {
                pdfEvent.setIntXPageNum(300);
                pdfEvent.setIntXPageNumRight(50);
                pdfEvent.setIntXPageTemplate(252.3f);
            }
            //Anexamos el evento
            writer.setPageEvent(pdfEvent);
            document.open();
            Formateador format = new Formateador();
            format.setIntTypeOut(Formateador.FILE);
            format.setStrPath(strPath);
            format.InitFormat(oConn, strFAC_NOMFORMATO);
            log.debug("//////////// Antes de format.DoFormat(oConn, intTransaccion)");
            String strRes = format.DoFormat(oConn, intTransaccion);
            log.debug("//////////// Despues de format.DoFormat(oConn, intTransaccion)");
            if (strRes.equals("OK")) {
                CIP_Formato fPDF = new CIP_Formato();
                fPDF.setDocument(document);
                fPDF.setWriter(writer);
                fPDF.setStrPathFonts(strPATHFonts);
                fPDF.EmiteFormato(format.getFmXML());
            } else {
                strResp = strRes;
            }
            document.close();
            writer.close();
        } catch (FileNotFoundException ex) {
            log.error(ex.getMessage());
            strResp = "ERROR:" + ex.getMessage();
        } catch (DocumentException ex) {
            log.error(ex.getMessage());
            strResp = "ERROR:" + ex.getMessage();
        }
        return strResp;
    }

    /**
     * Obtenemos el mail del cliente
     */
    public String getMail(int intIdUser, Conexion oConn) {
        String strMail = "";
        String strSql = "select CT_EMAIL1 from vta_cliente where CT_ID = " + intIdUser;
        try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                strMail = rs.getString("CT_EMAIL1");
            }
            rs.close();
        } catch (SQLException ex) {
            log.error(ex.getMessage());
        }
        return strMail;
    }

}
