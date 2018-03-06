/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.especiales.cofide;

import ERP.Ticket;
import Tablas.vta_facturadeta;
import Tablas.vta_mov_cte_deta;
import Tablas.vta_ticketsdeta;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Operaciones.TableMaster;
import comSIWeb.Utilerias.Fechas;
import java.net.URLDecoder;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import ERP.ERP_MapeoFormato;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import com.itextpdf.text.Document;
import comSIWeb.Operaciones.Formatos.Formateador;
import comSIWeb.Operaciones.Formatos.FormateadorMasivo;
import comSIWeb.Operaciones.Reportes.CIP_Formato;
import comSIWeb.Operaciones.Reportes.PDFEventPage;
import comSIWeb.Utilerias.Mail;
import java.io.File;
import javax.servlet.ServletContext;

/**
 *
 * @author JULIO CÉSAR CHÁVEZ M.
 */

public class COFIDE_Venta {

    String strResultado = "";
    String strResp = "";
    Fechas fec = new Fechas();
    String strSql = "";
    ResultSet rs;
    private HttpServletRequest servletRequest;
    private javax.servlet.ServletContext servletContext;
    Telemarketing tele = new Telemarketing();

    String strPathXML = "";
    String strfolio_GLOBAL = "";
    String strPathPrivateKeys = "";
    String strPathFonts = "";

    public String getStrPathXML() {
        return strPathXML;
    }

    public void setStrPathXML(String strPathXML) {
        this.strPathXML = strPathXML;
    }

    public String getStrfolio_GLOBAL() {
        return strfolio_GLOBAL;
    }

    public void setStrfolio_GLOBAL(String strfolio_GLOBAL) {
        this.strfolio_GLOBAL = strfolio_GLOBAL;
    }

    public String getStrPathPrivateKeys() {
        return strPathPrivateKeys;
    }

    public void setStrPathPrivateKeys(String strPathPrivateKeys) {
        this.strPathPrivateKeys = strPathPrivateKeys;
    }

    public String getStrPathFonts() {
        return strPathFonts;
    }

    public void setStrPathFonts(String strPathFonts) {
        this.strPathFonts = strPathFonts;
    }

    public String DoVenta(Conexion oConn, HttpServletRequest request, VariableSession varSesiones, ServletContext context) throws Exception {

        System.out.println("##### INICIA LA VENTA DESDE VENTAS GENERALES #####");

        //Recuperamos paths de web.xml
        strPathXML = this.strPathXML;
        strfolio_GLOBAL = this.strfolio_GLOBAL;
        strPathPrivateKeys = this.strPathPrivateKeys;
        strPathFonts = this.strPathFonts;

        if (strfolio_GLOBAL == null) {
            strfolio_GLOBAL = "SI";
        }

        //Recuperamos el tipo de venta 1:FACTURA 2:TICKET 3:PEDIDO
        String strTipoVta = request.getParameter("TIPOVENTA");
        String[] strInformacionCte = new String[5];
        // ID DE LA VENTA QUE SE VA A EDITAR
        String strIdvta = request.getParameter("idvta");
        int intCT_ID = 0;
        String StrCT_ID = "";

//        if (!strIdvta.equals("1")) {
        //OBTIENE LA INFORMACIÓN DEL CLIENTE O DE LA VENTA
        strInformacionCte = getInformacion(oConn, strIdvta); //OBTIENE EL ID DEL CLIENTE
        StrCT_ID = strInformacionCte[0];
        intCT_ID = Integer.parseInt(StrCT_ID);
//        }

        int intCevPublicidad = Integer.valueOf(request.getParameter("CEV_MPUBLICIDAD"));
        String strCev_Correo = request.getParameter("CEV_CORREO");
        String strCev_Correo2 = request.getParameter("CEV_CORREO2");
        System.out.println("######################################");
        System.out.println(strCev_Correo);
        System.out.println(strCev_Correo2);
        System.out.println("######################################");

        ///////////////////////////////////////////////////////////////////////////////////////////////////
        String strFacSerie = request.getParameter("FAC_SERIE");
        String strVtaCteNvo = request.getParameter("vta_nvo");
        String strPagoOk = request.getParameter("pagoOk");
        int intMod = getModalidadCurso(oConn, request.getParameter("CEV_IDCURSO"));
        final String strComentario = URLDecoder.decode(new String(request.getParameter("CEV_COMENTARIO").getBytes("iso-8859-1")), "UTF-8");
//        String strPromocion = request.getParameter("promo"); //si es 1 = es promocion

        //Guardamos datos de facturacion
        int intDfaId = tele.saveDatosFactura(oConn, varSesiones, request, intCT_ID);
        //Instanciamos el objeto que generara la venta
        Ticket ticket = new Ticket(oConn, varSesiones, request);
        ticket.setStrPATHKeys(strPathPrivateKeys);
        ticket.setStrPATHXml(strPathXML);
        ticket.setStrPATHFonts(strPathFonts);

        ticket.setBolAfectaInv(false);
        //Recibimos parametros
        String strPrefijoMaster = "TKT";
        String strPrefijoDeta = "TKTD";
        String strTipoVtaNom = Ticket.TICKET;

        if (strTipoVta == null) { //tkt
            strTipoVta = "2";
        }

        if (strTipoVta.equals("1")) {
            strPrefijoMaster = "FAC";
            strPrefijoDeta = "FACD";
            strTipoVtaNom = Ticket.FACTURA;
//            ticket.initMyPass(servletContext);
            ticket.initMyPass(context);
        }

        ticket.setStrTipoVta(strTipoVtaNom);
        //Validamos si tenemos un empresa seleccionada
        if (varSesiones.getIntIdEmpresa() != 0) {
            //Asignamos la empresa seleccionada
            ticket.setIntEMP_ID(varSesiones.getIntIdEmpresa());
        }
        //Validamos si usaremos un folio global
        if (strfolio_GLOBAL.equals("NO")) {
            ticket.setBolFolioGlobal(false);
        }
        //Edicion de pedidos
        ticket.getDocument().setFieldInt("SC_ID", Integer.valueOf(request.getParameter("SC_ID")));
        ticket.getDocument().setFieldInt("CT_ID", intCT_ID);

        if (request.getParameter("esReservacion") != null) {
            int intReservacion = Integer.parseInt(request.getParameter("esReservacion"));
            ticket.getDocument().setFieldInt(strPrefijoMaster + "_ES_RESERVACION", intReservacion);
        }

        if (isUserInBound(oConn, varSesiones.getIntNoUser())) {
            ticket.getDocument().setFieldInt(strPrefijoMaster + "_INBOUND", 1);
        } else {
            ticket.getDocument().setFieldInt(strPrefijoMaster + "_INBOUND", 0);
        }

        if (Integer.valueOf(request.getParameter(strPrefijoMaster + "_MONEDA")) == 0) {
            ticket.getDocument().setFieldInt(strPrefijoMaster + "_MONEDA", 1);
        } else {
            ticket.getDocument().setFieldInt(strPrefijoMaster + "_MONEDA", Integer.valueOf(request.getParameter(strPrefijoMaster + "_MONEDA")));
        }
        //Categoria del cliente
        String strCCID1 = request.getParameter("CC1_ID");
        if (strCCID1 != null) {
            ticket.getDocument().setFieldInt("CC1_ID", Integer.valueOf(request.getParameter("CC1_ID")));
        }
        //Clave de vendedor
        int intVE_ID = 0;
        try {
            intVE_ID = Integer.valueOf(request.getParameter("VE_ID"));
        } catch (NumberFormatException ex) {
            System.out.println("ERP_Ventas VE_ID " + ex.getMessage());
        }

        //Tarifas de IVA
        int intTI_ID = 0;
        int intTI_ID2 = 0;
        int intTI_ID3 = 0;
        try {
            intTI_ID = Integer.valueOf(request.getParameter("TI_ID"));
            intTI_ID2 = Integer.valueOf(request.getParameter("TI_ID2"));
            intTI_ID3 = Integer.valueOf(request.getParameter("TI_ID3"));
        } catch (NumberFormatException ex) {
            System.out.println("ERP_Ventas TI_ID " + ex.getMessage());
        }

        //Tipo de comprobante
        int intFAC_TIPOCOMP = 0;
//        try {
//            intFAC_TIPOCOMP = Integer.valueOf(request.getParameter("FAC_TIPOCOMP"));
//        } catch (NumberFormatException ex) {
//            System.out.println("ERP_Ventas FAC_TIPOCOMP " + ex.getMessage());
//        }
        //Asignamos los valores al objeto
        ticket.getDocument().setFieldInt("VE_ID", intVE_ID);
        ticket.getDocument().setFieldInt("TI_ID", intTI_ID);
        ticket.getDocument().setFieldInt("TI_ID2", intTI_ID2);
        ticket.getDocument().setFieldInt("TI_ID3", intTI_ID3);
        ticket.setIntFAC_TIPOCOMP(intFAC_TIPOCOMP);
        ticket.getDocument().setFieldInt(strPrefijoMaster + "_ESSERV", Integer.valueOf(request.getParameter(strPrefijoMaster + "_ESSERV")));
        System.out.println("######################### fecha : " + fec.getFechaActual());
        ticket.getDocument().setFieldString(strPrefijoMaster + "_FECHA", fec.getFechaActual());
        ticket.getDocument().setFieldString("COFIDE_NVO", strVtaCteNvo); //para ver si es nuevo cliente o no y ñigarlo a la venta
        ticket.getDocument().setFieldString(strPrefijoMaster + "_PROMO", "0"); //para ver si es nuevo cliente o no y ñigarlo a la venta
        if (request.getParameter("CEV_FECHAPAGO") != null) {
            String strFechaCobro = fec.FormateaBD(request.getParameter("CEV_FECHAPAGO"), "/");
            ticket.getDocument().setFieldString(strPrefijoMaster + "_FECHA_COBRO", strFechaCobro);
        }
        ticket.getDocument().setFieldString(strPrefijoMaster + "_FOLIO", request.getParameter(strPrefijoMaster + "_FOLIO"));
        ticket.getDocument().setFieldString(strPrefijoMaster + "_FOLIO_C", request.getParameter(strPrefijoMaster + "_FOLIO"));
        ticket.getDocument().setFieldString("CC_CURSO_ID", request.getParameter("CEV_IDCURSO"));
        final String strNotas = URLDecoder.decode(new String(request.getParameter(strPrefijoMaster + "_NOTAS").getBytes(
                "iso-8859-1")), "UTF-8");
        final String strNotasPie = URLDecoder.decode(new String(request.getParameter(strPrefijoMaster + "_NOTASPIE").getBytes(
                "iso-8859-1")), "UTF-8");
        ticket.getDocument().setFieldString(strPrefijoMaster + "_NOTAS", strNotas);
        if (!strFacSerie.equals(null)) {
            ticket.getDocument().setFieldString(strPrefijoMaster + "_SERIE", strFacSerie); //numero de serie
        }
        ticket.getDocument().setFieldString(strPrefijoMaster + "_NOTASPIE", strNotasPie); //guarda el comentario del agente sobre la venta
        ticket.getDocument().setFieldString(strPrefijoMaster + "_REFERENCIA", request.getParameter(strPrefijoMaster + "_REFERENCIA"));
        ticket.getDocument().setFieldString(strPrefijoMaster + "_CONDPAGO", request.getParameter(strPrefijoMaster + "_CONDPAGO"));
        if (request.getParameter(strPrefijoMaster + "_METODOPAGO") != null) {
            ticket.getDocument().setFieldString(strPrefijoMaster + "_METODODEPAGO", request.getParameter(strPrefijoMaster + "_METODOPAGO"));
        }

        if (request.getParameter(strPrefijoMaster + "_NUMCUENTA") != null) {
            ticket.getDocument().setFieldString(strPrefijoMaster + "_NUMCUENTA", request.getParameter(strPrefijoMaster + "_NUMCUENTA"));
        }

        if (request.getParameter("CEV_NOM_FILE") != null) {

            if (!request.getParameter("CEV_NOM_FILE").equals("")) {
                ticket.getDocument().setFieldInt(strPrefijoMaster + "_COFIDE_PAGADO", 1);
            }
            ticket.getDocument().setFieldString(strPrefijoMaster + "_NOMPAGO", request.getParameter("CEV_NOM_FILE"));
        } //si suben un comprobante, la venta queda pagada, si no, sigue siendo una venta pendiente

        ticket.getDocument().setFieldString(strPrefijoMaster + "_FORMADEPAGO", request.getParameter(strPrefijoMaster + "_FORMADEPAGO"));
        ticket.getDocument().setFieldDouble(strPrefijoMaster + "_IMPORTE", Double.valueOf(request.getParameter(strPrefijoMaster + "_IMPORTE")));
        ticket.getDocument().setFieldDouble(strPrefijoMaster + "_IMPUESTO1", Double.valueOf(request.getParameter(strPrefijoMaster + "_IMPUESTO1")));
        ticket.getDocument().setFieldDouble(strPrefijoMaster + "_IMPUESTO2", Double.valueOf(request.getParameter(strPrefijoMaster + "_IMPUESTO2")));
        ticket.getDocument().setFieldDouble(strPrefijoMaster + "_IMPUESTO3", Double.valueOf(request.getParameter(strPrefijoMaster + "_IMPUESTO3")));
        ticket.getDocument().setFieldDouble(strPrefijoMaster + "_TOTAL", Double.valueOf(request.getParameter(strPrefijoMaster + "_TOTAL")));
        ticket.getDocument().setFieldDouble(strPrefijoMaster + "_TASA1", Double.valueOf(request.getParameter(strPrefijoMaster + "_TASA1")));
        ticket.getDocument().setFieldDouble(strPrefijoMaster + "_TASA2", Double.valueOf(request.getParameter(strPrefijoMaster + "_TASA2")));
        ticket.getDocument().setFieldDouble(strPrefijoMaster + "_TASA3", Double.valueOf(request.getParameter(strPrefijoMaster + "_TASA3")));

        if (request.getParameter(strPrefijoMaster + "_TASAPESO") != null) {

            if (Double.valueOf(request.getParameter(strPrefijoMaster + "_TASAPESO")) == 0) {
                ticket.getDocument().setFieldDouble(strPrefijoMaster + "_TASAPESO", 1);
            } else {
                ticket.getDocument().setFieldDouble(strPrefijoMaster + "_TASAPESO", Double.valueOf(request.getParameter(strPrefijoMaster + "_TASAPESO")));
            }

        } else {
            ticket.getDocument().setFieldDouble(strPrefijoMaster + "_TASAPESO", 1);
        }

        //Si no hay moneda seleccionada que ponga tasa 1
        if (Integer.valueOf(request.getParameter(strPrefijoMaster + "_MONEDA")) == 0) {

            ticket.getDocument().setFieldDouble(strPrefijoMaster + "_TASAPESO", 1);
        }
        if (request.getParameter(strPrefijoMaster + "_DIASCREDITO") != null) {

            if (Double.valueOf(request.getParameter(strPrefijoMaster + "_DIASCREDITO")) == 0) {
                ticket.getDocument().setFieldInt(strPrefijoMaster + "_DIASCREDITO", 1);
            } else {
                ticket.getDocument().setFieldInt(strPrefijoMaster + "_DIASCREDITO", Integer.valueOf(request.getParameter(strPrefijoMaster + "_DIASCREDITO")));
            }

        }
        //Cliente final
        if (intDfaId != 0l) {
            try {
                ticket.getDocument().setFieldInt("DFA_ID", intDfaId);
            } catch (NumberFormatException ex) {
                System.out.println("Ventas CT_CLIENTEFINAL " + ex.getMessage());
            }
        }
        /*Campos flete, transportista , num guia VENTAS*/
        try {
            ticket.getDocument().setFieldInt("TR_ID", Integer.valueOf(request.getParameter("TR_ID")));
        } catch (NumberFormatException ex) {
            System.out.println("ERP_Compras TR_ID " + ex.getMessage());
        }
        try {
            ticket.getDocument().setFieldInt("ME_ID", Integer.valueOf(request.getParameter("ME_ID")));
        } catch (NumberFormatException ex) {
            System.out.println("ERP_Compras ME_ID " + ex.getMessage());
        }
        try {
            ticket.getDocument().setFieldInt("TF_ID", Integer.valueOf(request.getParameter("TF_ID")));
        } catch (NumberFormatException ex) {
            System.out.println("ERP_Compras TF_ID " + ex.getMessage());
        }
        if (request.getParameter(strPrefijoMaster + "_NUM_GUIA") != null) {
            ticket.getDocument().setFieldString(strPrefijoMaster + "_NUM_GUIA", request.getParameter(strPrefijoMaster + "_NUM_GUIA"));
        }
        //Validamos parametros para recibos de honorarios}
        if (request.getParameter(strPrefijoMaster + "_RETISR") != null && strTipoVtaNom.equals(Ticket.FACTURA)) {
            ticket.getDocument().setFieldDouble(strPrefijoMaster + "_RETISR", Double.valueOf(request.getParameter(strPrefijoMaster + "_RETISR")));
        }
        if (request.getParameter(strPrefijoMaster + "_RETIVA") != null && strTipoVtaNom.equals(Ticket.FACTURA)) {
            ticket.getDocument().setFieldDouble(strPrefijoMaster + "_RETIVA", Double.valueOf(request.getParameter(strPrefijoMaster + "_RETIVA")));
        }
        if (request.getParameter(strPrefijoMaster + "_NETO") != null && strTipoVtaNom.equals(Ticket.FACTURA)) {
            ticket.getDocument().setFieldDouble(strPrefijoMaster + "_NETO", Double.valueOf(request.getParameter(strPrefijoMaster + "_NETO")));
        }
        //Recibimos el regimen fiscal
        if (request.getParameter(strPrefijoMaster + "_REGIMENFISCAL") != null) {
            ticket.getDocument().setFieldString(strPrefijoMaster + "_REGIMENFISCAL", request.getParameter(strPrefijoMaster + "_REGIMENFISCAL"));
        }
        //Recibimos la serie por guardar...
        if (request.getParameter(strPrefijoMaster + "_SERIE") != null) {
            ticket.getDocument().setFieldString(strPrefijoMaster + "_SERIE", request.getParameter(strPrefijoMaster + "_SERIE"));
        }
        //Recibimos la referencia bancaria
        if (request.getParameter(strPrefijoMaster + "_REFERENCIA") != null) {
            ticket.getDocument().setFieldString(strPrefijoMaster + "_REFERENCIA", request.getParameter("CEV_DIGITO"));
        }
        //Recibimos el turno de la operacion
        if (request.getParameter(strPrefijoMaster + "_TURNO") != null) {
            try {
                ticket.getDocument().setFieldInt(strPrefijoMaster + "_TURNO", Integer.valueOf(request.getParameter(strPrefijoMaster + "_TURNO")));
            } catch (NumberFormatException ex) {
                System.out.println("Error al convertir turno");
            }
        }

        //guardamos el tipo de curso vendido en el maestro del ticket
        ticket.getDocument().setFieldInt(strPrefijoMaster + "_TIPO_CURSO", Integer.valueOf(request.getParameter("CEV_TIPO_CURSO")));
        //Recibimos datos de los items o partidas
        int intCount = Integer.valueOf(request.getParameter("COUNT_ITEM"));
        for (int i = 1; i <= intCount; i++) {
            TableMaster deta = null;

            if (strTipoVtaNom.equals(Ticket.TICKET)) {
                deta = new vta_ticketsdeta();
            }
            if (strTipoVtaNom.equals(Ticket.FACTURA)) {
                deta = new vta_facturadeta();
            }

            deta.setFieldInt("SC_ID", Integer.valueOf(request.getParameter("SC_ID")));
            deta.setFieldInt("PR_ID", Integer.valueOf(request.getParameter("PR_ID" + i)));
            deta.setFieldInt(strPrefijoDeta + "_EXENTO1", Integer.valueOf(request.getParameter(strPrefijoDeta + "_EXENTO1" + i)));
            deta.setFieldInt(strPrefijoDeta + "_EXENTO2", Integer.valueOf(request.getParameter(strPrefijoDeta + "_EXENTO2" + i)));
            deta.setFieldInt(strPrefijoDeta + "_EXENTO3", Integer.valueOf(request.getParameter(strPrefijoDeta + "_EXENTO3" + i)));
            deta.setFieldInt(strPrefijoDeta + "_ESREGALO", Integer.valueOf(request.getParameter(strPrefijoDeta + "_ESREGALO" + i)));
            //GUARDA EL TIPO DE CURSO EN DETALLES DEL DOCUMENTO                    
            deta.setFieldInt(strPrefijoDeta + "_TIPO_CURSO", Integer.valueOf(request.getParameter("CEV_TIPO_CURSO")));

            deta.setFieldString(strPrefijoDeta + "_CVE", request.getParameter(strPrefijoDeta + "_CVE" + i));
            final String strDescripcion = URLDecoder.decode(new String(request.getParameter(strPrefijoDeta + "_DESCRIPCION" + i).getBytes("iso-8859-1")), "UTF-8");

            final String strNotasDeta = URLDecoder.decode(new String(request.getParameter(strPrefijoDeta + "_NOTAS" + i).getBytes("iso-8859-1")), "UTF-8");

            deta.setFieldString(strPrefijoDeta + "_DESCRIPCION", strDescripcion);
            deta.setFieldString(strPrefijoDeta + "_NOSERIE", request.getParameter(strPrefijoDeta + "_NOSERIE" + i));
            deta.setFieldDouble(strPrefijoDeta + "_IMPORTE", Double.valueOf(request.getParameter(strPrefijoDeta + "_IMPORTE" + i)));
            deta.setFieldDouble(strPrefijoDeta + "_CANTIDAD", Double.valueOf(request.getParameter(strPrefijoDeta + "_CANTIDAD" + i)));
            deta.setFieldDouble(strPrefijoDeta + "_TASAIVA1", Double.valueOf(request.getParameter(strPrefijoDeta + "_TASAIVA1" + i)));
            deta.setFieldDouble(strPrefijoDeta + "_TASAIVA2", Double.valueOf(request.getParameter(strPrefijoDeta + "_TASAIVA2" + i)));
            deta.setFieldDouble(strPrefijoDeta + "_TASAIVA3", Double.valueOf(request.getParameter(strPrefijoDeta + "_TASAIVA3" + i)));
            deta.setFieldDouble(strPrefijoDeta + "_IMPUESTO1", Double.valueOf(request.getParameter(strPrefijoDeta + "_IMPUESTO1" + i)));
            deta.setFieldDouble(strPrefijoDeta + "_IMPUESTO2", Double.valueOf(request.getParameter(strPrefijoDeta + "_IMPUESTO2" + i)));
            deta.setFieldDouble(strPrefijoDeta + "_IMPUESTO3", Double.valueOf(request.getParameter(strPrefijoDeta + "_IMPUESTO3" + i)));
            deta.setFieldDouble(strPrefijoDeta + "_IMPORTEREAL", Double.valueOf(request.getParameter(strPrefijoDeta + "_IMPORTEREAL" + i)));
            deta.setFieldDouble(strPrefijoDeta + "_PRECIO", Double.valueOf(request.getParameter(strPrefijoDeta + "_PRECIO" + i)));
            deta.setFieldDouble(strPrefijoDeta + "_DESCUENTO", Double.valueOf(request.getParameter(strPrefijoDeta + "_DESCUENTO" + i)));
            deta.setFieldDouble(strPrefijoDeta + "_PORDESC", Double.valueOf(request.getParameter(strPrefijoDeta + "_PORDESC" + i)));
            deta.setFieldDouble(strPrefijoDeta + "_PRECREAL", Double.valueOf(request.getParameter(strPrefijoDeta + "_PRECREAL" + i)));
            //Retencion de ISR
            if (request.getParameter(strPrefijoDeta + "_RET_ISR" + i) != null) {
                try {
                    deta.setFieldDouble(strPrefijoDeta + "_RET_ISR", Integer.valueOf(request.getParameter(strPrefijoDeta + "_RET_ISR" + i)));
                    deta.setFieldDouble(strPrefijoDeta + "_RET_IVA", Integer.valueOf(request.getParameter(strPrefijoDeta + "_RET_IVA" + i)));
                    deta.setFieldDouble(strPrefijoDeta + "_RET_FLETE", Integer.valueOf(request.getParameter(strPrefijoDeta + "_RET_FLETE" + i)));
                } catch (NumberFormatException ex) {
                    System.out.println("EN ERP_Ventas falta definir retencion ISR");
                }
            }
            //Solo aplica si es ticket o factura
            if (strTipoVtaNom.equals(Ticket.TICKET) || strTipoVtaNom.equals(Ticket.FACTURA)) {
                deta.setFieldInt(strPrefijoDeta + "_ESDEVO", Integer.valueOf(request.getParameter(strPrefijoDeta + "_ESDEVO" + i)));
            }

            deta.setFieldInt(strPrefijoDeta + "_PRECFIJO", Integer.valueOf(request.getParameter(strPrefijoDeta + "_PRECFIJO" + i)));
            deta.setFieldInt(strPrefijoDeta + "_ESREGALO", Integer.valueOf(request.getParameter(strPrefijoDeta + "_ESREGALO" + i)));
            deta.setFieldString(strPrefijoDeta + "_COMENTARIO", strComentario); //salia doble el nombre
            //UNIDAD DE MEDIDA
            deta.setFieldString(strPrefijoDeta + "_UNIDAD_MEDIDA", "CURSO");
            //ID DE SERVICIO
            if (request.getParameter(strPrefijoDeta + "_CF_ID" + i) != null) {
                deta.setFieldString("CF_ID", request.getParameter(strPrefijoDeta + "_CF_ID" + i));
            }
            //Evaluamos si envian el id del pedido
            if (request.getParameter("PDD_ID" + i) != null) {
                deta.setFieldInt("PDD_ID", Integer.valueOf(request.getParameter("PDD_ID" + i)));
            }
            if (request.getParameter(strPrefijoDeta + "_DESC_PREC" + i) != null) {
                try {
                    deta.setFieldDouble(strPrefijoDeta + "_DESC_ORI", Double.valueOf(request.getParameter(strPrefijoDeta + "_DESC_ORI" + i)));
                    deta.setFieldInt(strPrefijoDeta + "_DESC_PREC", Integer.valueOf(request.getParameter(strPrefijoDeta + "_DESC_PREC" + i)));
                    deta.setFieldInt(strPrefijoDeta + "_REGALO", Integer.valueOf(request.getParameter(strPrefijoDeta + "_REGALO" + i)));
                    deta.setFieldInt(strPrefijoDeta + "_ID_PROMO", Integer.valueOf(request.getParameter(strPrefijoDeta + "_ID_PROMO" + i)));
                } catch (NumberFormatException ex) {
                    System.out.println("Error al recuperar los valores de MLM " + ex.getMessage() + " " + ex.getLocalizedMessage() + " " + ex.toString());
                    ex.fillInStackTrace();
                }
            }
            ticket.AddDetalle(deta);
        }
        //Recibimos los pagos
        int intCountPagos = Integer.valueOf(request.getParameter("COUNT_PAGOS"));

        for (int i = 1; i <= intCountPagos; i++) {

            if (Double.valueOf(request.getParameter("MCD_IMPORTE" + i)) > 0) {

                vta_mov_cte_deta detaPago = new vta_mov_cte_deta();
                detaPago.setFieldInt("CT_ID", intCT_ID);
                detaPago.setFieldInt("SC_ID", Integer.valueOf(request.getParameter("SC_ID")));
                detaPago.setFieldInt("MCD_MONEDA", Integer.valueOf(request.getParameter("MCD_MONEDA" + i)));
                detaPago.setFieldString("MCD_FOLIO", request.getParameter("MCD_FOLIO" + i));
                detaPago.setFieldString("MCD_FORMAPAGO", request.getParameter("MCD_FORMAPAGO" + i));
                detaPago.setFieldString("MCD_NOCHEQUE", request.getParameter("MCD_NOCHEQUE" + i));
                detaPago.setFieldString("MCD_BANCO", request.getParameter("MCD_BANCO" + i));
                detaPago.setFieldString("MCD_NOTARJETA", request.getParameter("MCD_NOTARJETA" + i));
                detaPago.setFieldString("MCD_TIPOTARJETA", request.getParameter("MCD_TIPOTARJETA" + i));
                detaPago.setFieldDouble("MCD_IMPORTE", Double.valueOf(request.getParameter("MCD_IMPORTE" + i)));
                detaPago.setFieldDouble("MCD_TASAPESO", Double.valueOf(request.getParameter("MCD_TASAPESO" + i)));
                detaPago.setFieldDouble("MCD_CAMBIO", Double.valueOf(request.getParameter("MCD_CAMBIO" + i)));
                //Validamos si tenemos un empresa seleccionada
                if (varSesiones.getIntIdEmpresa() != 0) {
                    //Asignamos la empresa seleccionada
                    detaPago.setFieldInt("EMP_ID", varSesiones.getIntIdEmpresa());
                }
                ticket.AddDetalle(detaPago);
            }
        }
        //Inicializamos objeto
        ticket.setBolSendMailMasivo(false);
        //Validamos si es un pedido que se esta editando para solo modificar el pedido anterior 
        if (strTipoVta.equals("3") && ticket.getDocument().getFieldInt("PD_ID") != 0) {
            ticket.doTrxMod();
        } else {
            ticket.Init();
            //Generamos transaccion
            ticket.doTrx();
        }
        String strRes = "";
//                System.out.println("obtendra respuesta de la venta####################################################################");
        if (ticket.getStrResultLast().equals("OK")) {
//                    System.out.println("la respuesta fue OK####################################################################");
            strRes = "OK." + ticket.getDocument().getValorKey();
            int intIdVta = Integer.valueOf(ticket.getDocument().getValorKey());
            //Posicion inicial para el numero de pagina
            String strPosX = "";
            String strTitle = "";
//            strPosX = servletContext.getInitParameter("PosXTitle");
//            strTitle = servletContext.getInitParameter("TitleApp");
            strPosX = context.getInitParameter("PosXTitle");
            strTitle = context.getInitParameter("TitleApp");
            //strTipoVta = 2 es un ticket o cotizacion
            //strTipoVta = 1 es una factura        
            System.out.println("################################# envio de correo ################################# tipo de venta :" + strTipoVta);
            if (strTipoVta.equals("1")) {
                enviaMailMasivo(oConn, strPosX, strTitle, ticket.getDocument().getValorKey(), varSesiones, strTipoVta,
                        strCev_Correo, strCev_Correo2, context);
            }
//                    System.out.println("################################# ya lo envio #################################");
            //agregar a la funcion de mail masivo el campo de tipodoc
            //Solo aplica en pedidos, tickets y cotizaciones
//                    if (strTipoVta.equals("2") || strTipoVta.equals("3") || strTipoVta.equals("4")) {
            if (strTipoVta.equals("2")) {
                //Enviamos por correo el formato en caso de ser diferente de factura
                String strTipoDoc = "";
//                        if (strTipoVta.equals("2")) { //si es ticket, manda correo con el ticket
                strTipoDoc = "TICKET";
                strCev_Correo = strGetCorreoTicket(oConn, varSesiones.getIntNoUser());
//                        }
                try {
                    if (!strTipoDoc.equals("")) {
                        ticket.generaMail(oConn, Integer.valueOf(ticket.getDocument().getValorKey()), strTipoDoc, strPathXML, strPathFonts, strCev_Correo);
                    }
                } catch (NumberFormatException ex) {
                    System.out.println("ERROR AL ENVIAR EL TICKET POR CORREO " + ex.getMessage());
                }
            }
            //Guardamos los participantes del curso
            int intIdTicket = 0;
            int intIdFactura = 0;
            if (strTipoVta.equals("1")) {
                intIdFactura = intIdVta;
            } else {
                intIdTicket = intIdVta;
            }
            System.out.println("############################## GUARDA PARTICIPANTES");
            strResultado = tele.guardaParticipantes(oConn, varSesiones, request, intCT_ID, intIdTicket, intIdFactura,
                    Integer.valueOf(request.getParameter("CEV_FAC")),
                    Integer.valueOf(request.getParameter("CEV_MIMP")),
                    Integer.valueOf(request.getParameter("CEV_TIPO_CURSO")),
                    request.getParameter("CEV_FECHAPAGO"),
                    request.getParameter("CEV_DIGITO"),
                    request.getParameter("CEV_NOM_FILE"),
                    intMod, strPagoOk
            );
            System.out.println("############################## TERMINA DE GUARDAR PARTICIPANTES");
        } else {
            strRes = ticket.getStrResultLast();
        }
        System.out.println("############ RESULTADOS DE LA VENTA : \n\n" + strRes + "\n\n#############");
        ///////////////////////////////////////////////////////////////////////////////////////////////////
        return strRes;
    }

    public int getModalidadCurso(Conexion oConn, String strModalidad) {
        int intMod = 1;
        String strSql = "select CC_IS_DIPLOMADO, CC_IS_SEMINARIO, "
                + "ifnull((select count(*) from cofide_modulo_curso where CC_CURSO_IDD = " + strModalidad + "),0) as EsPrincipal "
                + "from cofide_cursos where CC_CURSO_ID = " + strModalidad; //id de curso
        try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                if (rs.getInt("CC_IS_DIPLOMADO") == 0 && rs.getInt("CC_IS_SEMINARIO") == 0) {
                    intMod = 1;
                } else {
                    if (rs.getInt("CC_IS_DIPLOMADO") == 1 && rs.getInt("EsPrincipal") != 0) {
                        intMod = 2;
                    }
                    if (rs.getInt("CC_IS_SEMINARIO") == 1 && rs.getInt("EsPrincipal") != 0) {
                        intMod = 3;
                    }
                }
            }
            rs.close();
        } catch (SQLException sql) {
            System.out.println("error al obtener la modalidad del curso:" + sql.getMessage());
        }

        return intMod;
    }

    public boolean isUserInBound(Conexion oConn, int intNoUser) {
        String strCodigoUser = "";
        boolean isInBound = false;
        try {
            String strSql = "";
            strSql = "select COFIDE_CODIGO from usuarios where id_usuarios = " + intNoUser;
            ResultSet rsCod = oConn.runQuery(strSql, true);
            while (rsCod.next()) {
                strCodigoUser = rsCod.getString("COFIDE_CODIGO");
            }
            rsCod.close();
        } catch (SQLException ex) {
            System.out.println("Es usuario INBOUND: [ERROR:] " + ex.getLocalizedMessage());
        }
        if (strCodigoUser.equals("BE-INBOUND")) {
            isInBound = true;
        } else {
            isInBound = false;
        }
        return isInBound;
    }

    public void enviaMailMasivo(Conexion oConn, String strPosX, String strTitle, String strFacId, VariableSession varSesiones, String strTipoVta, String strCorreovta1, String strCorreovta2, ServletContext context) {

//Respuesta del servicio
        boolean bolEsNC = false;
        //Cargamos datos del mail
        //Recuperamos paths de web.xml
//        String strPathXML = servletContext.getInitParameter("PathXml");
//        String strPathFonts = servletContext.getRealPath("/") + System.getProperty("file.separator") + "fonts";
        String strPathXML = context.getInitParameter("PathXml");
        String strPathFonts = context.getRealPath("/") + System.getProperty("file.separator") + "fonts";

        //Recibimos parametros
        String strlstFAC_ID = "";
        String strVIEW_COPIA = "";
        String strVIEW_ASUNTO = "";
        String strVIEW_MAIL = "";//mail del cliente
        String strVIEW_CC = "1";
        String strMailCte = "";
        String strMailCte2 = "";
        int intVIEW_Templete = 1;

        if (strVIEW_COPIA == null) {
            strVIEW_COPIA = "";
        }
        if (strVIEW_CC == null) {
            strVIEW_CC = "1";
        }
//consulta a la cuenta de correo del varsessiones -------------------------
        int intUsuario = varSesiones.getIntNoUser();
        String strNombre = "";
        String strUsuario = "";
        String strPassword = "";
        String strDominio = "";
        String strPuerto = "";
        int intSSL = 0;
        int intSSSL = 0;
//        System.out.println("################################# consulta la información del usuario ejecutivo #################################");
        String strSqlCuentaEjecutivo = "select nombre_usuario, SMTP, PORT, SMTP_US, SMTP_PASS, SMTP_USATLS, SMTP_USASTLS from usuarios where id_usuarios = " + intUsuario;
        try {
            ResultSet rsCuenta = oConn.runQuery(strSqlCuentaEjecutivo, true);
            while (rsCuenta.next()) {
//                System.out.println("################################# obteniendo información#################################");
                strNombre = rsCuenta.getString("nombre_usuario");
                strDominio = rsCuenta.getString("SMTP");
                strPuerto = rsCuenta.getString("PORT");
                strUsuario = rsCuenta.getString("SMTP_US");
                strPassword = rsCuenta.getString("SMTP_PASS");
                intSSL = rsCuenta.getInt("SMTP_USATLS");
                intSSSL = rsCuenta.getInt("SMTP_USASTLS");
            }
            rsCuenta.close();
        } catch (SQLException e) {
            System.out.println("Obtiene Datos para el ENVIO - MAIL: " + e.getLocalizedMessage());
        }
        String strSmtpServer = strDominio;
        String strSmtpUser = strUsuario;
        String strSmtpPassword = strPassword;
        String strSmtpPort = strPuerto;
        int intSmtpUsaSSL = intSSL;
        int intSmtpUsaSSSL = intSSSL;
        //Validamos que todos los datos del mail esten completos
        if (!strSmtpServer.equals("")
                && !strSmtpUser.equals("")
                && !strSmtpPassword.equals("")
                && !strSmtpPort.equals("")) {
            //Recorremos las operaciones seleccionado
            //Recuperamos la factura
            int intFAC_ID = 0;
            try {
                intFAC_ID = Integer.valueOf(strFacId);
            } catch (NumberFormatException ex) {
            }
            //Resultado
            String strResp = "OK";
            String strCode = "OK";
            boolean bolIsOkFacPDF = false;
            //Obtenemos el folio
            String strNumFolio = "";
            int intEMP_TIPOCOMP = 0;
            int intEMP_ID = 0;
            int intCT_ID = 0;
            String strFAC_NOMFORMATO = "";
            String strFAC_RAZONSOCIAL = "";
            String strFAC_FECHA = "";
            int intFAC_ES_CFD = 0;
            int intFAC_ES_CBB = 0;
            String strFolio_C = "";
            String strSql = "";
            // si strTipoVta == 1 es Factura
            if (strTipoVta.equals("1")) {
//                System.out.println("################################# tipo de venta 1 #################################");
                strSql = "select FAC_ID,CT_ID,FAC_FOLIO,FAC_FOLIO_C,FAC_TIPOCOMP,FAC_NOMFORMATO,EMP_ID,FAC_RAZONSOCIAL,FAC_FECHA,FAC_ES_CFD,FAC_ES_CBB from vta_facturas where FAC_ID = " + intFAC_ID;
            }
            // si strTipoVta == 2 es Ticket
            if (strTipoVta.equals("2") || strTipoVta.equals("3")) {
//                System.out.println("################################# tipo de venta 2 o 3 #################################");
                strSql = "select TKT_ID,CT_ID,TKT_FOLIO,TKT_TIPOCOMP, TKT_NOMFORMATO,EMP_ID,TKT_RAZONSOCIAL,TKT_FECHA from vta_tickets where TKT_ID = " + intFAC_ID;
            }
            //Recuperamos el numero de folio que queremos imprimir
            if (bolEsNC) {
                strSql = "select CT_ID,NC_FOLIO,NC_TIPOCOMP,NC_NOMFORMATO,EMP_ID,NC_RAZONSOCIAL,NC_FECHA,NC_ES_CFD,NC_ES_CBB from vta_ncredito where NC_ID = " + intFAC_ID;
            }
            try {
                ResultSet rs = oConn.runQuery(strSql, true);
                //Buscamos el nombre del archivo
                while (rs.next()) {
                    //System.out.println("VENTA: " + strTipoVta);
                    if (strTipoVta.equals("1")) { //fac
                        //System.out.println("ENTRO FACTURA ");
                        //if (!bolEsNC) {
                        strNumFolio = rs.getString("FAC_FOLIO");
                        intEMP_TIPOCOMP = rs.getInt("FAC_TIPOCOMP");
                        intEMP_ID = rs.getInt("EMP_ID");
                        intCT_ID = rs.getInt("CT_ID");
                        strFAC_NOMFORMATO = rs.getString("FAC_NOMFORMATO");
                        strFAC_RAZONSOCIAL = rs.getString("FAC_RAZONSOCIAL");
                        strFAC_FECHA = rs.getString("FAC_FECHA");
                        intFAC_ES_CFD = rs.getInt("FAC_ES_CFD");
                        intFAC_ES_CBB = rs.getInt("FAC_ES_CBB");
                        strFolio_C = rs.getString("FAC_FOLIO_C");
                        //}
                    } else {
                        if (strTipoVta.equals("2")) { //tkt
                            strNumFolio = rs.getString("TKT_FOLIO");
                            intEMP_TIPOCOMP = rs.getInt("TKT_TIPOCOMP");
                            intEMP_ID = rs.getInt("EMP_ID");
                            intCT_ID = rs.getInt("CT_ID");
                            strFAC_NOMFORMATO = "";
                            strFAC_RAZONSOCIAL = rs.getString("TKT_RAZONSOCIAL");
                            strFAC_FECHA = rs.getString("TKT_FECHA");
                            intFAC_ES_CFD = 0;
                            intFAC_ES_CBB = 0;
                            strFolio_C = strNumFolio;
                        } else {
                            strNumFolio = rs.getString("NC_FOLIO");
                            intEMP_TIPOCOMP = rs.getInt("NC_TIPOCOMP");
                            intEMP_ID = rs.getInt("EMP_ID");
                            intCT_ID = rs.getInt("CT_ID");
                            strFAC_NOMFORMATO = rs.getString("NC_NOMFORMATO");
                            strFAC_RAZONSOCIAL = rs.getString("NC_RAZONSOCIAL");
                            strFAC_FECHA = rs.getString("NC_FECHA");
                            intFAC_ES_CFD = rs.getInt("NC_ES_CFD");
                            intFAC_ES_CBB = rs.getInt("NC_ES_CBB");
                            strFolio_C = strNumFolio;
                        }
                    }
                }
                rs.close();

//destinatarios proporcionados en la venta
                strMailCte = strCorreovta1;
                strMailCte2 = strCorreovta2;
//destinatarios proporcionados en la venta
            } catch (SQLException ex) {
                System.out.println(". . . ..  Error al enviar mail 1 " + ex.getMessage());
            }
//agregamos el correo del ejecutivo como copia
            strVIEW_COPIA = strUsuario;
            //Si el cliente tiene mail lo enviamos
            if (!strMailCte.equals("") || !strVIEW_COPIA.equals("")) {
                //Mail personalizado
                String strMailOK = new String(strVIEW_MAIL);
//                strMailOK = strMailOK.replace("[FOLIO]", strFolio_C);
                strMailOK = strMailOK.replace("[FOLIO]", strNumFolio);
                strMailOK = strMailOK.replace("[RAZONSOCIAL]", strFAC_RAZONSOCIAL);
                strMailOK = strMailOK.replace("[FECHA]", strFAC_FECHA);
                strMailOK = strMailOK.replace("[MES]", strFAC_FECHA.substring(5, 6));
                String strMailASOK = new String(strVIEW_ASUNTO);
//                strMailASOK = strMailASOK.replace("[FOLIO]", strFolio_C);
                strMailASOK = strMailASOK.replace("[FOLIO]", strNumFolio);
                strMailASOK = strMailASOK.replace("[RAZONSOCIAL]", strFAC_RAZONSOCIAL);
                strMailASOK = strMailASOK.replace("[FECHA]", strFAC_FECHA);
                strMailASOK = strMailASOK.replace("[MES]", strFAC_FECHA.substring(5, 6));
                //Lista de correo alos que se los enviaremos
                String strEmailSend = "";
                if (!strMailCte.equals("")) {
                    strEmailSend = strMailCte;
                }
                if (!strMailCte2.equals("")) {
                    if (!strMailCte.equals("")) {
                        strEmailSend += ",";
                    }
                    strEmailSend += strMailCte2;
                }
                if (!strVIEW_COPIA.equals("")) {
                    if (!strMailCte.equals("") || !strMailCte2.equals("")) {
                        strEmailSend += ",";
                    }
                    strEmailSend += strVIEW_COPIA;
                }
                //Buscamos si la empresa usa CBB
                int intEMP_USACODBARR = 0;
                int intEMP_CFD_CFDI = 0;
                strSql = "select EMP_USACODBARR,EMP_CFD_CFDI from vta_empresas where EMP_ID = " + intEMP_ID;
                try {
                    ResultSet rs = oConn.runQuery(strSql, true);
                    //Buscamos el nombre del archivo
                    while (rs.next()) {
                        intEMP_USACODBARR = rs.getInt("EMP_USACODBARR");
                        intEMP_CFD_CFDI = rs.getInt("EMP_CFD_CFDI");
                    }
                    rs.close();
                } catch (SQLException ex) {

                }
                ERP_MapeoFormato mapeo = new ERP_MapeoFormato(intEMP_TIPOCOMP);
                String strNomFormato = mapeo.getStrNomFormato();
                if (intEMP_USACODBARR == 1) {
                    strNomFormato += "_CBB";
                }
                if (intEMP_CFD_CFDI == 1 && intFAC_ES_CFD == 0 && intFAC_ES_CBB == 0) {
                    strNomFormato += "_cfdi";
                }
                //Nombres de archivos
                String strFilePdf = strPathXML + "/" + strNomFormato + "_" + intEMP_ID + "_" + strNumFolio + ".pdf";
                String strFileXML = strPathXML + "/" + "XmlSAT" + intFAC_ID + " .xml";
                if (bolEsNC) {
                    strFileXML = strPathXML + "/" + "NC_XML" + intFAC_ID + ".xml";
                }
                //Generamos el formato de impresion
                try {
                    Document document = new Document();
                    PdfWriter writer = PdfWriter.getInstance(document,
                            new FileOutputStream(strFilePdf));
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
                    FormateadorMasivo format = new FormateadorMasivo();
                    format.setBolSeisxHoja(true);
                    format.setIntTypeOut(Formateador.FILE);
//                    format.setStrPath(servletContext.getRealPath("/"));
                    format.setStrPath(context.getRealPath("/"));
                    format.InitFormat(oConn, strNomFormato);
                    String strRes = format.DoFormat(oConn, intFAC_ID);
                    if (strRes.equals("OK")) {
//                        System.out.println("################################# adjuntara pdf #################################");
                        CIP_Formato fPDF = new CIP_Formato();
                        fPDF.setDocument(document);
                        fPDF.setWriter(writer);
                        fPDF.setBolSeisxHoja(true);
                        fPDF.setStrPathFonts(strPathFonts);
                        fPDF.EmiteFormatoMasivo(format.getFmXML());
                        document.close();
                        bolIsOkFacPDF = true;
                    }
                } catch (Exception ex) {
                    System.out.println("error generar formato " + ex.getMessage());
                }
                //Mandamos el mail
                Mail mail = new Mail();
                mail.setBolDepuracion(false);
                if (intSmtpUsaSSL == 1) {
                    mail.setBolUsaTls(true);
                }
                if (intSmtpUsaSSSL == 1) {
                    mail.setBolUsaStartTls(true);
                }
                mail.setHost(strSmtpServer);
                mail.setUsuario(strSmtpUser);
                mail.setContrasenia(strSmtpPassword);
                mail.setPuerto(strSmtpPort);
                //Adjuntamos archivos
                mail.setFichero(strFilePdf);
                if (intEMP_USACODBARR == 0) {
                    //Validamos si existe el archivo con el formato viejo
                    File file = new File(strFileXML);//specify the file path 
                    if (!file.exists()) {
                        //Version 2.0
                        StringBuilder strNomFile = new StringBuilder("");
                        ERP_MapeoFormato mapeoXml = null;
                        mapeoXml = new ERP_MapeoFormato(1);
                        strFileXML = strPathXML + (getNombreFileXml(mapeoXml, intFAC_ID, strFAC_RAZONSOCIAL, strFAC_FECHA, strNumFolio));
                        mail.setFichero(strFileXML); //manda el xml porque ya creo la ruta
                    }
                    if (file.exists()) {
//                        System.out.println("Si existe el archivo y se adjunta; \n" + strFileXML);
                        mail.setFichero(strFileXML);
                    }
                }
                System.out.println("\n\n########################## venta_original_CORREOS: \n\n" + strEmailSend + "\n\n##########################");
                if (intVIEW_Templete == 1) {
                    String[] lstMail = getMailTemplate("FACTURA", oConn);
                    String strMsgAsunto = lstMail[0].replace("%folio%", strFolio_C);
//                    String strMsgAsunto = lstMail[0].replace("%folio%", strNumFolio);
//                    StristMail[1].replace("%folio%", strNumFolio);
                    String strMsgMensaje = lstMail[1].replace("%folio%", strFolio_C).replace("%CT_RAZONSOCIAL%", strFAC_RAZONSOCIAL);
//                    String strMsgMensaje = lstMail[1].replace("%folio%", strNumFolio).replace("%CT_RAZONSOCIAL%", strFAC_RAZONSOCIAL);
                    mail.setAsunto(strMsgAsunto);
                    //Preparamos el mail
                    mail.setDestino(strEmailSend);
//                    System.out.println(strEmailSend + " emails a los que se les enviara!!!! ------------------------------------------");
                    mail.setMensaje(strMsgMensaje);
                    String strQueryVenta = "";
                    if (strTipoVta.equals("1")) {
                        strQueryVenta = "select * from vta_facturas where FAC_ID = ";
                    } else {
                        strQueryVenta = "select * from vta_tickets where TKT_ID = ";
                    }
                    //Reemplazamos campos personalizados
                    String strSqlMail = strQueryVenta + intFAC_ID;
                    ResultSet rsMail;
                    try {
                        rsMail = oConn.runQuery(strSqlMail, true);
                        mail.setReplaceContent(rsMail);
                    } catch (SQLException ex) {
                        System.out.println(ex);
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                    //Reemplazamos campos personalizados en la empresa...
                    strSqlMail = "select * from vta_empresas where EMP_ID = " + varSesiones.getIntIdEmpresa();
                    try {
                        rsMail = oConn.runQuery(strSqlMail, true);
                        mail.setReplaceContent(rsMail);
                    } catch (SQLException ex) {
                        System.out.println(ex);
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                } else {
                    mail.setAsunto(strMailASOK);
                    //Preparamos el mail
                    System.out.println(strEmailSend + " destinatario del correo---------------------------------------");
                    mail.setDestino(strEmailSend);
                    mail.setMensaje(strMailOK);
                }

                //Enviamos el mail
                boolean bol = mail.sendMail();//
                if (!bol) {
                    strResp = "NO SE PUDO ENVIAR EL MAIL.";
                }
            } else {
                strResp = "NO EXISTEN MAILS.";

            }
        }
    }

    public String strGetCorreoTicket(Conexion oConn, int intIdUsuario) {
        String strCorreoTicket = "";
        System.out.println("########################## OBTENEMOS EL CORREO DEL EJECUTIVO PARA ENVIARLE EL TICKET");
//select * from usuarios where PERF_ID = 30
        strSql = "select EMAIL from usuarios where id_usuarios = " + intIdUsuario;
//        strSql = "select EMAIL from usuarios where id_usuarios = " + intIdUsuario + " or PERF_ID = 30";
        strCorreoTicket = "";

        try {
            rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                if (!rs.getString("EMAIL").equals("")) {
                    if (!strCorreoTicket.equals("")) {
                        strCorreoTicket += "," + rs.getString("EMAIL");
                    } else {
                        strCorreoTicket = rs.getString("EMAIL");
                    }
                }
            }
            rs.close();
        } catch (SQLException ex) {
            System.out.println("strGetCorreoTicket [" + ex.getLocalizedMessage() + "]");
        }

        return strCorreoTicket;
    }//Fin strGetCorreoTicket

    /**
     * genera el XML
     *
     * @param mapeo
     * @param intTransaccion
     * @param strNombreReceptor
     * @param strFechaCFDI
     * @param strFolioFiscalUUID
     * @return
     */
    public String getNombreFileXml(ERP_MapeoFormato mapeo, int intTransaccion, String strNombreReceptor, String strFechaCFDI, String strFolioFiscalUUID) {
        String strNomFileXml = null;
        String strPatronNomXml = mapeo.getStrNomXML("NOMINA");
        strNomFileXml = strPatronNomXml.replace("%Transaccion%", intTransaccion + "").replace("", "").replace("%nombre_receptor%", strNombreReceptor).replace("%fecha%", strFechaCFDI).replace("%UUID%", strFolioFiscalUUID).replace(" ", "_");
        strNomFileXml = strNomFileXml.replace("__", "_");
        return strNomFileXml;
    }

    /**
     * Obtenemos valo del template para el mail
     *
     * @param strNom Es el nombre del template
     * @return Regresa un arreglo con los valores del template
     */
    public String[] getMailTemplate(String strNom, Conexion oConn) {
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
            System.out.println(ex.getErrorCode());
        }

        return listValores;
    }

    public String[] getInformacion(Conexion oConn, String StridVta) {
        String[] strRespuesta = new String[5];
        String strIDCTE = "";
        String strRFC = "";
        String strRazonSocial = "";
        String strCorreo = "";
        String strTelefono = "";
        String strSQL = "";
        //CONSULTA
        strSQL = "select CT_ID from view_ventasglobales where FAC_ID = " + StridVta;

        try {
            rs = oConn.runQuery(strSQL, true);

            while (rs.next()) {

                strIDCTE = rs.getString("CT_ID");

            }
            rs.close();

            strSQL = "SELECT CT_ID, CT_RAZONSOCIAL, CT_RFC, CT_EMAIL1, CT_TELEFONO1 FROM vta_cliente WHERE CT_ID = " + strIDCTE;
            rs = oConn.runQuery(strSQL, true);

            while (rs.next()) {

                strRazonSocial = rs.getString("CT_RAZONSOCIAL");
                strRFC = rs.getString("CT_RFC");
                strCorreo = rs.getString("CT_EMAIL1");
                strTelefono = rs.getString("CT_TELEFONO1");

            }
            rs.close();

        } catch (SQLException sql) {
            System.out.println("Error al obtener la información del cliente: " + sql.getMessage());
        }
        //FILLARRAY
        strRespuesta[0] = strIDCTE;
        strRespuesta[1] = strRFC;
        strRespuesta[2] = strRazonSocial;
        strRespuesta[3] = strCorreo;
        strRespuesta[4] = strTelefono;
        //FILLARRAY

        return strRespuesta;
    }
}
