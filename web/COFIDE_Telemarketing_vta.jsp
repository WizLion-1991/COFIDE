<%-- 
    Document   : COFIDE_Telemarketing_vta
    Created on : 21-ene-2016, 23:50:45
    Author     : juliocesar
--%>

<%@page import="com.mx.siweb.erp.especiales.cofide.vta_clientes"%>
<%@page import="Tablas.vta_cliente"%>
<%@page import="com.mx.siweb.erp.especiales.cofide.COFIDE_Calendario"%>
<%@page import="com.mx.siweb.erp.especiales.cofide.COFIDE_Venta"%>
<%@page import="com.mx.siweb.erp.especiales.cofide.COFIDE_Venta"%>
<%@page import="com.mx.siweb.erp.especiales.cofide.COFIDE_Mail_cursos"%>
<%@page import="comSIWeb.Operaciones.Reportes.PDFEventPage"%>
<%@page import="java.io.FileOutputStream"%>
<%@page import="com.itextpdf.text.pdf.PdfWriter"%>
<%@page import="com.itextpdf.text.Document"%>
<%@page import="comSIWeb.Operaciones.Formatos.Formateador"%>
<%@page import="comSIWeb.Operaciones.Formatos.FormateadorMasivo"%>
<%@page import="comSIWeb.Operaciones.Reportes.CIP_Formato"%>
<%@page import="java.io.File"%>
<%@page import="ERP.ERP_MapeoFormato"%>
<%@page import="comSIWeb.Utilerias.Mail"%>
<%@page import="java.sql.SQLException"%>
<%@page import="com.mx.siweb.erp.especiales.cofide.SincronizarPaginaWeb"%>
<%@page import="com.mx.siweb.erp.especiales.cofide.Telemarketing"%>
<%@page import="Tablas.vta_mov_cte_deta"%>
<%@page import="Tablas.vta_cotizadeta"%>
<%@page import="Tablas.vta_pedidosdeta"%>
<%@page import="Tablas.vta_facturadeta"%>
<%@page import="Tablas.vta_ticketsdeta"%>
<%@page import="comSIWeb.Operaciones.TableMaster"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="ERP.Ticket"%>
<%@page import="comSIWeb.Utilerias.Fechas"%>
<%@page import="comSIWeb.ContextoApt.atrJSP"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="comSIWeb.Utilerias.UtilXml"%>
<%@page import="comSIWeb.ContextoApt.Seguridad"%>
<%@page import="comSIWeb.Operaciones.Conexion"%>
<%@page import="comSIWeb.ContextoApt.VariableSession"%>

<%
    /*Obtenemos las variables de sesion*/
    VariableSession varSesiones = new VariableSession(request);
    varSesiones.getVars();
    //Abrimos la conexion
    Conexion oConn = new Conexion(varSesiones.getStrUser(), this.getServletContext());
    oConn.open();
    //Instanciamos objeto de seguridad para no permitir el acceso externo a ver los XML
    Seguridad seg = new Seguridad();//Valida que la peticion se halla hecho desde el mismo sitio
    UtilXml util = new UtilXml();
    Fechas fec = new Fechas();
//    COFIDE_Mail_cursos mg = new COFIDE_Mail_cursos();
    Telemarketing tele = new Telemarketing();
    COFIDE_Venta vta_ = new COFIDE_Venta();

    String strPathXML = this.getServletContext().getInitParameter("PathXml");
    String strfolio_GLOBAL = this.getServletContext().getInitParameter("folio_GLOBAL");
    String strPathPrivateKeys = this.getServletContext().getInitParameter("PathPrivateKey");
    String strPathFonts = this.getServletContext().getRealPath("/") + System.getProperty("file.separator") + "fonts";

    if (varSesiones.getIntNoUser() != 0 && seg.ValidaURL(request)) {
        String strid = request.getParameter("ID");
        if (strid != null) {
            if (strid.equals("1")) { //obtenemos datos del curso
                String strXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
                strXML += "<vta>";
                String strNomCurso = request.getParameter("CEV_NOMCURSO");
                String strClasCurso = request.getParameter("Clasifica");
                String strTipoCurso = "";
                if (strClasCurso == null) {
                    strClasCurso = "1";
                }
//                if (strClasCurso.equals("1")) {
//                    strTipoCurso = " and CP_TIPO_CURSO = 1";
//                }
                String strNombre = "";
                int intLimite = 0;
                int intInscritos = 0;
                int intIdCurso = 0;
                double douPrecioUnit = 0;
                String strFec = "";
                String strSede = "";
                String strHorario = "";
                String strNomCursotmp = "";
                String strSql = "select CC_NOMBRE_CURSO,CC_MONTAJE,CC_INSCRITOS,CC_PRECIO_UNIT,CC_CURSO_ID,CC_PRECIO_PRES,"
                        + "CC_PRECIO_ON,CC_PRECIO_VID,CC_FECHA_INICIAL,CC_SEDE, CC_SESION "
                        + "from cofide_cursos where CC_CURSO_ID ='" + strNomCurso + "'";
                ResultSet rs = oConn.runQuery(strSql, true);
                while (rs.next()) {
                    strNombre = rs.getString("CC_NOMBRE_CURSO");
                    intLimite = rs.getInt("CC_MONTAJE");
                    douPrecioUnit = rs.getDouble("CC_PRECIO_UNIT");
                    intIdCurso = rs.getInt("CC_CURSO_ID");
                    strSede = rs.getString("CC_SEDE");
                    strHorario = rs.getString("CC_SESION");
                    strNomCursotmp = rs.getString("CC_NOMBRE_CURSO");
                    if (strClasCurso.equals("1")) {
                        douPrecioUnit = rs.getDouble("CC_PRECIO_PRES");
                    }
                    if (strClasCurso.equals("2")) {
                        douPrecioUnit = rs.getDouble("CC_PRECIO_ON");
                    }
                    if (strClasCurso.equals("3")) {
                        douPrecioUnit = rs.getDouble("CC_PRECIO_VID");
                    }
                    if (rs.getString("CC_FECHA_INICIAL") != null || rs.getString("CC_FECHA_INICIAL") != "") {
                        strFec = fec.FormateaDDMMAAAA(rs.getString("CC_FECHA_INICIAL"), "/");
                    }
                }
                rs.close();
                intInscritos = getLugarVendido(strNomCurso, oConn);
                strXML += "<datos "
                        + " CEV_LIMITE = \"" + intLimite + "\" "
                        + " CEV_OCUPADO = \"" + intInscritos + "\" "
                        + " CEV_PRECIO_UNIT = \"" + douPrecioUnit + "\" "
                        + " CEV_FECINICIO = \"" + strFec + "\" "
                        + " CEV_IDCURSO = \"" + intIdCurso + "\" "
                        + " CC_HORARIO = \"" + intIdCurso + "\" "
                        + " CC_SEDE = \"" + intIdCurso + "\" "
                        + " CEV_NOMCURSO_TMP = \"" + util.Sustituye(strNomCursotmp) + "\" "
                        + " />";
                strXML += "</vta>";
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML);//Pintamos el resultado
            } //1
            if (strid.equals("2")) { //obtenemos datos de facturacion
                String strXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
                strXML += "<vta>";
                int intIdCte = Integer.parseInt(request.getParameter("CT_NO_CLIENTE"));
                String strNCliente = "";
                String strNombre = "";
                String strRfc = "";
                String strCalle = "";
                String strColonia = "";
                String strDelegacion = "";
                String strEstado = "";
                String strCP = "";
                String strTelefono = "";
                String strCorreo1 = "";
                String strCorreo2 = "";
                String strNumExt = "";
                String strNumInt = "";
                int DFA_ID = 0;
                boolean bolRespuesta = false;
                String strSql = "select DFA_RAZONSOCIAL, CT_ID, DFA_RFC, DFA_CALLE, DFA_COLONIA, DFA_MUNICIPIO, "
                        + "DFA_ESTADO, DFA_CP, DFA_TELEFONO, DFA_EMAIL, DFA_EMAI2,DFA_NUMERO, DFA_NUMINT "
                        + "from vta_cliente_facturacion "
                        + "where CT_ID = " + intIdCte + " "
                        + "group by DFA_RAZONSOCIAL, CT_ID, DFA_RFC, DFA_CALLE, DFA_COLONIA, DFA_MUNICIPIO, "
                        + "DFA_ESTADO, DFA_CP, DFA_TELEFONO, DFA_EMAIL, DFA_EMAI2";
                ResultSet rs = oConn.runQuery(strSql, true);
                while (rs.next()) {
                    bolRespuesta = true;
                    strNCliente = rs.getString("CT_ID");
                    strRfc = rs.getString("DFA_RFC");
                    strNombre = rs.getString("DFA_RAZONSOCIAL");
                    strCalle = rs.getString("DFA_CALLE");
                    strColonia = rs.getString("DFA_COLONIA");
                    strDelegacion = rs.getString("DFA_MUNICIPIO");
                    strEstado = rs.getString("DFA_ESTADO");
                    strCP = rs.getString("DFA_CP");
                    strTelefono = rs.getString("DFA_TELEFONO");
                    strCorreo1 = rs.getString("DFA_EMAIL");
                    strCorreo2 = rs.getString("DFA_EMAI2");
                    strNumExt = rs.getString("DFA_NUMERO");
                    strNumInt = rs.getString("DFA_NUMINT");
                    DFA_ID++;
                    strXML += "<datos "
                            + " CEV_ID = \"" + DFA_ID + "\" "
                            + " CEV_NUMERO = \"" + strNumExt + "\" "
                            + " CEV_NUMINT = \"" + strNumInt + "\" "
                            + " CEV_RFC = \"" + util.Sustituye(strRfc) + "\" "
                            + " CEV_NOMBRE = \"" + util.Sustituye(strNombre) + "\" "
                            + " CEV_CALLE = \"" + strCalle + "\" "
                            + " CEV_COLONIA = \"" + strColonia + "\" "
                            + " CEV_MUNICIPIO = \"" + strDelegacion + "\" "
                            + " CEV_ESTADO = \"" + strEstado + "\" "
                            + " CEV_CP = \"" + strCP + "\" "
                            + " CEV_TELEFONO = \"" + strTelefono + "\" "
                            + " CEV_EMAIL1 = \"" + strCorreo1 + "\" "
                            + " CEV_EMAIL2 = \"" + strCorreo2 + "\" "
                            + " />";
                }
                rs.close();
                // si no encuentra datos
                if (!bolRespuesta) {
                    strXML += "<datos />";
                }
                strXML += "</vta>";
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML);//Pintamos el resultado
            }
            if (strid.equals("5")) { //guardamos la venta
                System.out.println("\n\n INICIA LA VENTA REGULAR\n\n");
                //Inicializamos datos
                Fechas fecha = new Fechas();
                //Recuperamos paths de web.xml
//                String strPathXML = this.getServletContext().getInitParameter("PathXml");
//                String strfolio_GLOBAL = this.getServletContext().getInitParameter("folio_GLOBAL");
                String strmod_Inventarios = this.getServletContext().getInitParameter("mod_Inventarios");
                String strSist_Costos = this.getServletContext().getInitParameter("SistemaCostos");
                String strClienteUniversal = this.getServletContext().getInitParameter("ClienteUniversal");
//                String strPathPrivateKeys = this.getServletContext().getInitParameter("PathPrivateKey");
//                String strPathFonts = this.getServletContext().getRealPath("/") + System.getProperty("file.separator") + "fonts";

                if (strfolio_GLOBAL == null) {
                    strfolio_GLOBAL = "SI";
                }
                if (strmod_Inventarios == null) {
                    strmod_Inventarios = "NO";
                }
                if (strSist_Costos == null) {
                    strSist_Costos = "0";
                }
                if (strClienteUniversal == null) {
                    strClienteUniversal = "0";
                }
                int intCevPublicidad = Integer.valueOf(request.getParameter("CEV_MPUBLICIDAD"));
                //Validamos si es un cliente de la base lo almacenamos
                //Guardamos el prospecto o lo actualizamos
                int intCT_ID = Integer.valueOf(request.getParameter("CT_ID"));
                String strCT_NO_CLIENTE = request.getParameter("CT_NO_CLIENTE");
                final String strRazonsocial = URLDecoder.decode(new String(request.getParameter("CT_RAZONSOCIAL").getBytes("iso-8859-1")), "UTF-8");
                final String strRfc = URLDecoder.decode(new String(request.getParameter("CT_RFC").getBytes("iso-8859-1")), "UTF-8");
                String strSede = request.getParameter("CT_SEDE");
                String strGiro = request.getParameter("CT_GIRO");
                String strArea = request.getParameter("CT_AREA");
                String strContacto = request.getParameter("CT_CONTACTO");
                String strContacto2 = request.getParameter("CT_CONTACTO2");
                String strCorreo = request.getParameter("CT_CORREO");
                String strCorreo2 = request.getParameter("CT_CORREO2");
                String strCev_Correo = request.getParameter("CEV_CORREO");
                String strCev_Correo2 = request.getParameter("CEV_CORREO2");
                String strBolBase = request.getParameter("CT_BOLBASE");
                String strCp = request.getParameter("CT_CP");
                String strCalle = request.getParameter("CT_CALLE");
                String strCol = request.getParameter("CT_COL");
                String strNumEx = request.getParameter("CT_NUM");
                String strNombre = request.getParameter("CT_NOMBRE");
                String strConmutador = request.getParameter("CT_CONMUTADOR");
                String strFacSerie = request.getParameter("FAC_SERIE");
                String strCEV_DUPLICIDAD = request.getParameter("CEV_DUPLICIDAD");
                String strCEV_DUPLICIDAD_ID = request.getParameter("CEV_DUPLICIDAD_ID");
                String strCT_AAA = request.getParameter("CT_AAA");
                String strIdMedioPublicidad = request.getParameter("mediopublicidad");
                String strVtaCteNvo = request.getParameter("vta_nvo");
                String strPagoOk = request.getParameter("pagoOk");
                int intCT_AAA = Integer.parseInt(strCT_AAA);
                int intMod = getModalidadCurso(oConn, request.getParameter("CEV_IDCURSO"));
                final String strComentario = URLDecoder.decode(new String(request.getParameter("CEV_COMENTARIO").getBytes("iso-8859-1")), "UTF-8");
//                System.out.println("\n\ncomentario: " + strComentario);
                String strPromocion = request.getParameter("promo"); //si es 1 = es promocion

                String strMotivo = "";
                if (request.getParameter("motivo") != null) {
                    strMotivo = request.getParameter("motivo");
                }

                //Guardado del telemarketing
                String strResultado = tele.doSaveProspectoBase(oConn, strBolBase, intCT_ID, strCT_NO_CLIENTE, strRazonsocial, strRfc,
                        strContacto, strContacto2, strCorreo, strCorreo2, "", "", "", strSede, strGiro, strArea, strCp, strCalle, strNumEx, strCol,
                        varSesiones, 0, strNombre, strConmutador, intCT_AAA, strIdMedioPublicidad, strMotivo);
                intCT_ID = Integer.valueOf(strResultado.replace("OK", ""));

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
                //Recuperamos el tipo de venta 1:FACTURA 2:TICKET 3:PEDIDO
                String strTipoVta = request.getParameter("TIPOVENTA");
                if (strTipoVta == null) { //tkt
                    strTipoVta = "2";
                }
                if (strTipoVta.equals("1")) {
                    strPrefijoMaster = "FAC";
                    strPrefijoDeta = "FACD";
                    strTipoVtaNom = Ticket.FACTURA;
                    ticket.initMyPass(this.getServletContext());
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
                try {
                    intFAC_TIPOCOMP = Integer.valueOf(request.getParameter("FAC_TIPOCOMP"));
                } catch (NumberFormatException ex) {
                    System.out.println("ERP_Ventas FAC_TIPOCOMP " + ex.getMessage());
                }
                //Asignamos los valores al objeto
                ticket.getDocument().setFieldInt("VE_ID", intVE_ID);
                ticket.getDocument().setFieldInt("TI_ID", intTI_ID);
                ticket.getDocument().setFieldInt("TI_ID2", intTI_ID2);
                ticket.getDocument().setFieldInt("TI_ID3", intTI_ID3);
                ticket.setIntFAC_TIPOCOMP(intFAC_TIPOCOMP);
                ticket.getDocument().setFieldInt(strPrefijoMaster + "_ESSERV", Integer.valueOf(request.getParameter(strPrefijoMaster + "_ESSERV")));
                ticket.getDocument().setFieldString(strPrefijoMaster + "_FECHA", fecha.getFechaActual());
                ticket.getDocument().setFieldString("COFIDE_NVO", strVtaCteNvo); //para ver si es nuevo cliente o no y ñigarlo a la venta
                ticket.getDocument().setFieldString(strPrefijoMaster + "_PROMO", strPromocion); //para ver si es nuevo cliente o no y ñigarlo a la venta
                if (request.getParameter("CEV_FECHAPAGO") != null) {
                    String strFechaCobro = fecha.FormateaBD(request.getParameter("CEV_FECHAPAGO"), "/");
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
                    if (request.getParameter("CEV_NOM_FILE") != "") {
                        ticket.getDocument().setFieldInt(strPrefijoMaster + "_COFIDE_PAGADO", 1);
                    }
                    ticket.getDocument().setFieldString(strPrefijoMaster + "_NOMPAGO", request.getParameter("CEV_NOM_FILE"));
                } //si suben un comprobante, la venta queda pagada, si no, sigue siendo una venta pendiente
                ticket.getDocument().setFieldString(strPrefijoMaster + "_FORMADEPAGO", request.getParameter(strPrefijoMaster + "_FORMADEPAGO"));
                //ticket.getDocument().setFieldString(strPrefijoMaster + "_FORMADEPAGO", "En una sola Exhibicion");
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
                //campos para marcarlo con duplicidad
                if (strCEV_DUPLICIDAD != null) {
                    ticket.getDocument().setFieldInt("COFIDE_DUPLICIDAD", Integer.valueOf(strCEV_DUPLICIDAD));
                    ticket.getDocument().setFieldInt("COFIDE_DUPLICIDAD_ID", Integer.valueOf(strCEV_DUPLICIDAD_ID));
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
                    final String strDescripcion = URLDecoder.decode(new String(request.getParameter(strPrefijoDeta + "_DESCRIPCION" + i).getBytes(
                            "iso-8859-1")), "UTF-8");
//                    System.out.println("\n\nDescripción: " + strDescripcion);

                    final String strNotasDeta = URLDecoder.decode(new String(request.getParameter(strPrefijoDeta + "_NOTAS" + i).getBytes(
                            "iso-8859-1")), "UTF-8");
                    deta.setFieldString(strPrefijoDeta + "_DESCRIPCION", strDescripcion);
//                    System.out.println("############################################\n" + strDescripcion + "\n#################################");
                    deta.setFieldString(strPrefijoDeta + "_NOSERIE", request.getParameter(strPrefijoDeta + "_NOSERIE" + i));
                    deta.setFieldDouble(strPrefijoDeta + "_IMPORTE", Double.valueOf(request.getParameter(strPrefijoDeta + "_IMPORTE" + i)));
                    deta.setFieldDouble(strPrefijoDeta + "_CANTIDAD", Double.valueOf(request.getParameter(strPrefijoDeta + "_CANTIDAD" + i)));
//                    System.out.println("\n\n#######cantidad: " + request.getParameter(strPrefijoDeta + "_CANTIDAD" + i));
                    deta.setFieldDouble(strPrefijoDeta + "_TASAIVA1", Double.valueOf(request.getParameter(strPrefijoDeta + "_TASAIVA1" + i)));
                    deta.setFieldDouble(strPrefijoDeta + "_TASAIVA2", Double.valueOf(request.getParameter(strPrefijoDeta + "_TASAIVA2" + i)));
                    deta.setFieldDouble(strPrefijoDeta + "_TASAIVA3", Double.valueOf(request.getParameter(strPrefijoDeta + "_TASAIVA3" + i)));
                    deta.setFieldDouble(strPrefijoDeta + "_IMPUESTO1", Double.valueOf(request.getParameter(strPrefijoDeta + "_IMPUESTO1" + i)));
//                    System.out.println("\n\n#######iva " + request.getParameter(strPrefijoDeta + "_IMPUESTO1" + i));
                    deta.setFieldDouble(strPrefijoDeta + "_IMPUESTO2", Double.valueOf(request.getParameter(strPrefijoDeta + "_IMPUESTO2" + i)));
                    deta.setFieldDouble(strPrefijoDeta + "_IMPUESTO3", Double.valueOf(request.getParameter(strPrefijoDeta + "_IMPUESTO3" + i)));
                    deta.setFieldDouble(strPrefijoDeta + "_IMPORTEREAL", Double.valueOf(request.getParameter(strPrefijoDeta + "_IMPORTEREAL" + i)));
//                    System.out.println("\n\n#######importe " + request.getParameter(strPrefijoDeta + "_IMPORTE" + i));
//                    System.out.println("\nprecio unitario:\n" + request.getParameter(strPrefijoDeta + "_PRECIO" + i) + "\n");
                    deta.setFieldDouble(strPrefijoDeta + "_PRECIO", Double.valueOf(request.getParameter(strPrefijoDeta + "_PRECIO" + i)));
//                    System.out.println("\n\n#######precio unitario " + request.getParameter(strPrefijoDeta + "_PRECIO" + i));
                    deta.setFieldDouble(strPrefijoDeta + "_DESCUENTO", Double.valueOf(request.getParameter(strPrefijoDeta + "_DESCUENTO" + i)));
                    deta.setFieldDouble(strPrefijoDeta + "_PORDESC", Double.valueOf(request.getParameter(strPrefijoDeta + "_PORDESC" + i)));
                    deta.setFieldDouble(strPrefijoDeta + "_PRECREAL", Double.valueOf(request.getParameter(strPrefijoDeta + "_PRECREAL" + i)));
//                    System.out.println("\n\n#######precio real " + request.getParameter(strPrefijoDeta + "_PRECREAL" + i));
//                    System.out.println("\n\n#######descuento " + request.getParameter(strPrefijoDeta + "_DESCUENTO" + i));
//                    System.out.println("\n\n####### % descuento " + request.getParameter(strPrefijoDeta + "_PORDESC" + i));
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
//                    deta.setFieldString(strPrefijoDeta + "_COMENTARIO", ""); //salia doble el nombre
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
                    strPosX = this.getServletContext().getInitParameter("PosXTitle");
                    strTitle = this.getServletContext().getInitParameter("TitleApp");
                    //strTipoVta = 2 es un ticket o cotizacion
                    //strTipoVta = 3 es una factura        
                    System.out.println("################################# envio de correo ################################# tipo de venta :" + strTipoVta);
                    if (strTipoVta.equals("1")) {
//                        System.out.println("################################# es tkt o fac #################################");
//                        if (strTipoVta.equals("2")) { //si es ticket, manda correo con el ticket
//                            strCev_Correo = strGetCorreoTicket(oConn, varSesiones.getIntNoUser());
//                        }
                        enviaMailMasivo(oConn, strPosX, strTitle, ticket.getDocument().getValorKey(), varSesiones, strTipoVta,
                                strCev_Correo, strCev_Correo2);
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
//                            System.out.println("################################# envio de TICKET #################################");
//                            System.out.println("################################# tipo de doc " + strTipoDoc + " ################################# ticket");
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
                    System.out.println("##############################save the participants in this sale");
                    strResultado = tele.guardaParticipantes(oConn, varSesiones, request, intCT_ID, intIdTicket, intIdFactura,
                            Integer.valueOf(request.getParameter("CEV_FAC")),
                            Integer.valueOf(request.getParameter("CEV_MIMP")),
                            Integer.valueOf(request.getParameter("CEV_TIPO_CURSO")),
                            request.getParameter("CEV_FECHAPAGO"),
                            request.getParameter("CEV_DIGITO"),
                            request.getParameter("CEV_NOM_FILE"),
                            intMod, strPagoOk
                    );
                    System.out.println("##############################finished to save the participants in this sale");
                } else {
                    strRes = ticket.getStrResultLast();
                }
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(strRes);//Pintamos el resultado
            } //5
            if (strid.equals("3")) { //CP
                String strCp = request.getParameter("CEV_CP");
                StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
                strXML.append("<Sepomex>");
                String strColonia = "";
                String strMunicipio = "";
                String strEstado = "";
                int cont = 0;
                boolean bolEncontro = false;
                String strSql = "select UPPER(CMX_COLONIA) AS CMX_COLONIA, UPPER(CMX_MUNICIPIO) AS CMX_MUNICIPIO, UPPER(CMX_ESTADO) AS CMX_ESTADO "
                        + "from cofide_sepomex "
                        + "where CMX_CP = '" + strCp + "'";
                ResultSet rs = oConn.runQuery(strSql, true);
                while (rs.next()) {
                    strColonia = rs.getString("CMX_COLONIA");
                    strMunicipio = rs.getString("CMX_MUNICIPIO");
                    strEstado = rs.getString("CMX_ESTADO");
                    if (cont == 0) {
                        bolEncontro = true;
                        strXML.append("<General ");
                        strXML.append(" CMX_MUNICIPIO = \"").append(strMunicipio).append("\" ");
                        strXML.append(" CMX_ESTADO = \"").append(strEstado).append("\" ");
                        strXML.append(" >");
                    }
                    strXML.append("<Colonia ");
                    strXML.append(" CMX_COLONIA = \"").append(strColonia).append("\"");
                    strXML.append(" />");
                    cont++;
                }
                if (bolEncontro) {
                    strXML.append("</General>");
                }
                strXML.append("</Sepomex>");
                rs.close();
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML.toString());//Pintamos el resultado
            } //fin 3
            if (strid.equals("4")) { //llenar datos fiscales de venta
                String strXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
                strXML += "<vta>";
                String strIdCte = request.getParameter("CT_NO_CLIENTE");
                String strRazonSocial = "";
                String strNumero = "";
                String strRFC = "";
                String strCalle = "";
                String strColonia = "";
                String strMunicipio = "";
                String strEstado = "";
                String strCP = "";
                String strCorreo = "";
                String strCC = "";
                String strTelefono = "";
                String strSql = "select * from vta_cliente_facturacion where ct_id = " + strIdCte;
                ResultSet rs = oConn.runQuery(strSql, true);
                while (rs.next()) {
                    strRazonSocial = rs.getString("DFA_RAZONSOCIAL");
                    strNumero = rs.getString("DFA_NUMERO");
                    strRFC = rs.getString("DFA_RFC");
                    strCalle = rs.getString("DFA_CALLE");
                    strColonia = rs.getString("DFA_COLONIA");
                    strMunicipio = rs.getString("DFA_MUNICIPIO");
                    strEstado = rs.getString("DFA_ESTADO");
                    strCP = rs.getString("DFA_CP");
                    strCorreo = rs.getString("DFA_EMAIL");
                    strCC = rs.getString("DFA_EMAI2");
                    strTelefono = rs.getString("DFA_TELEFONO");

                    strXML += "<datos "
                            + " DFA_RAZONSOCIAL = \"" + strRazonSocial + "\"  "
                            + " DFA_RFC = \"" + util.Sustituye(strRFC) + "\"  "
                            + " DFA_CALLE = \"" + strCalle + "\"  "
                            + " DFA_COLONIA = \"" + strColonia + "\"  "
                            + " DFA_MUNICIPIO = \"" + strMunicipio + "\"  "
                            + " DFA_ESTADO = \"" + strEstado + "\"  "
                            + " DFA_CP = \"" + strCP + "\"  "
                            + " DFA_EMAIL = \"" + strCorreo + "\"  "
                            + " DFA_EMAI2 = \"" + strCC + "\"  "
                            + " DFA_NUMERO = \"" + strNumero + "\"  "
                            + " DFA_TELEFONO = \"" + strTelefono + "\" "
                            + " />";
                }
                rs.close();
                strXML += "</vta>";
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML);//Pintamos el resultado
            } //fin 4
            if (strid.equals("6")) {
                String strXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
                strXML += "<vta>";
                String strSupervisor = request.getParameter("SC_NOMBRE");
                String strPass = "";
                String strSql = "select SC_PASSWORD from cofide_supervisor where SC_ID = " + strSupervisor;
                ResultSet rs = oConn.runQuery(strSql, true);
                while (rs.next()) {
                    strPass = rs.getString("SC_PASSWORD");
                }
                strXML += "<datos "
                        + " SC_PASSWORD = \"" + strPass + "\" "
                        + " />";
                strXML += "</vta>";
                rs.close();
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML);//Pintamos el resultado
            } //fin 6
            if (strid.equals("7")) {
                String strXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
                strXML += "<vta>";
                String strFechaActual = fec.getFechaActual();
                strXML += "<datos "
                        + " fecha = \"" + strFechaActual + "\" "
                        + " />";
                strXML += "</vta>";
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML);//Pintamos el resultado
            } //fin 7
            //10 cancela los puntos y participantes de la venta seleccionada
            if (strid.equals("10")) {

                System.out.println("SE ACTUALIZA LA VENTA PARA SER FACTURADA");

                String strRespuesta = "";

                String strFac_ID = request.getParameter("fac_id");
                String strDFA_ID = request.getParameter("DFA_ID");
                String strForma = request.getParameter("forma");
                String strMetodo = request.getParameter("metodo");
                String strArchivoPago = request.getParameter("archivopago");
                String strDigito = request.getParameter("digref");
                String strFechaPago = "";
                String strPagado = "0";
                if (!strArchivoPago.equals("")) {
                    strPagado = "1";
                }
                if (!request.getParameter("fechapago").equals("")) {
                    strFechaPago = fec.FormateaBD(request.getParameter("fechapago"), "/");
                }
                System.out.println("dfa_id : " + strDFA_ID + " en el id de vta: " + strFac_ID);
                strSql = "update vta_tickets set "
                        + "TKT_FACTURAR = 1, "
                        + "DFA_ID = " + strDFA_ID + ", "
                        + "TKT_FORMADEPAGO = '" + strForma + "', "
                        + "TKT_METODODEPAGO = '" + strMetodo + "', "
                        + "TKT_NOMPAGO = '" + strArchivoPago + "', "
                        + "TKT_REFERENCIA = '" + strDigito + "', "
                        + "TKT_FECHA_COBRO = '" + strFechaPago + "', "
                        + "TKT_COFIDE_PAGADO = '" + strPagado + "' "
                        + "where TKT_ID = '" + strFac_ID + "'";

                oConn.runQueryLMD(strSql);

                if (!oConn.isBolEsError()) {

                    //ENVIO DE PLANTILLAS
                    strRespuesta = "OK";
                    System.out.println("################# VA A ENVIAR PLANTILLAS- ACTUALIZACIÓN DE RESERVACIÓN A FACTURA");
                    tele.sendMail(oConn, varSesiones, request, strFac_ID);
                    System.out.println("################# TERMINO- ACTUALIZACIÓN DE RESERVACIÓN A FACTURA");
                    if (strRespuesta.equals("")) {
                        System.out.println("Hubó un problema al enviar los correos correspondientes a la venta");
                    }

                }

                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(strRespuesta);//Pintamos el resultado

            } //10

            //Valida si es usuario unbound
            if (strid.equals("11")) { //11
                String strRes = "";
                String strCodigoUser = "";
                try {
                    String strSql = "";
                    strSql = "select COFIDE_CODIGO from usuarios where id_usuarios = " + varSesiones.getIntNoUser();
                    ResultSet rsCod = oConn.runQuery(strSql, true);
                    while (rsCod.next()) {
                        if (rsCod.getString("COFIDE_CODIGO") != "") {
                            strCodigoUser = "OK" + rsCod.getString("COFIDE_CODIGO");
                        }
                    }
                    rsCod.close();
                } catch (SQLException ex) {
                    System.out.println("Error GetBaseUsuario: " + ex.getLocalizedMessage());
                }

                if (strCodigoUser.equals("")) {
                    strCodigoUser = "El Usuario No tiene configurada una Base.";
                }
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(strCodigoUser);//Pintamos el resultado
            }//Fin ID 11

            if (strid.equals("12")) { //12
                String strCorreo = request.getParameter("correo_reviza");
                String strCorreo2 = request.getParameter("correo_reviza2");
                String strTelefono = request.getParameter("telefono_reviza");
                final String strRFC = URLDecoder.decode(new String(request.getParameter("rfc_reviza").getBytes("iso-8859-1")), "UTF-8");
                String strIdCliente = request.getParameter("id_cliente_reviza");
                String strDB = request.getParameter("db_reviza");
                String strIdVTA = request.getParameter("ID_VENTA");
                boolean bolDuplica = false;
                String strCt = "";
                String strCompletasql = "";
                int intMaXduplicado = 0;

                if (!strRFC.equals("")) {
                    strCompletasql = "C_RFC like '%" + strRFC + "%' or";
                }
//                System.out.println("vamos a validar duplicidad");
                //buscar coincidencias en la tabla de cofide_clientes(compaq), si encuentra el ct_id ligado con el id de compaq DIFERENTE AL ID_CTE QUE SE MANDO
                strSql = "select * from cofide_clientes "
                        + "where C_CT_ID not in ('0','" + strIdCliente + "') and "
                        + "(" + strCompletasql + " C_EMAIL like '%" + strCorreo + "%' or C_TELEFONO like '%" + strTelefono + "%') "
                        + "AND (select CT_CLAVE_DDBB from vta_cliente where CT_ID = C_CT_ID) <> '" + strDB + "'";
                rs = oConn.runQuery(this.strSql, true);
                while (rs.next()) {
//                    System.out.println("aqui encontro duplicidad y se va a guardar el conflicto, id de la venta: " + strIdVTA);
                    strCt = rs.getString("C_CT_ID");
                    bolDuplica = true;

                    if (intMaXduplicado <= 10) {
                        // GUARDAR EN ESTA TABLA @cofide_ventas_duplicadas
                        strRes = SaveVtaConflicto(oConn, strCt, strIdVTA); // guarda la venta con el cte y el conflicto   
                    }
                    intMaXduplicado++;
                }
                rs.close();
                if (!bolDuplica) { //no se encontro conflicto de duplicidad, se valida si es o aun no cliente
//                    System.out.println("no encontro duplicados");
                    if (getEsCte(oConn, strRFC, strCorreo, strTelefono, strIdCliente)) { //no es cliente y se agrega a la tabla cofide_clientes(compaq)
//                        System.out.println("no encontro coincidencias como cliente, se va a guardar unoe nuevo");
                        strRes = setCte(oConn, strRFC, strCorreo, strTelefono, strIdCliente); //se guarda a la tabla de cofide_clientes si no existe
//                        System.out.println("############################ se agrego al admin " + strRes + "#############################");
                    }
                }
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(strRes);//Pintamos el resultado
            }//Fin ID 12

            if (strid.equals("13")) {

                System.out.println("######################### SE ACTUALIZA LA VENTA ################################");
                if (request.getParameter("id_tkt") != null) {

                    int intIdFac_Tkt = Integer.parseInt(request.getParameter("id_tkt"));
                    String strIdFac_Tkt = request.getParameter("id_tkt");
                    //id maestro de los movimientos
                    String strIdMov = request.getParameter("IDMOV");
                    String strPrecioUnit = request.getParameter("precio_unit");
                    String strImporte = request.getParameter("importe");
                    String strMetodo = request.getParameter("metodopago");
                    String strFormaPago = request.getParameter("formapago");
                    String strIva = request.getParameter("iva");
                    String strDesc = request.getParameter("descuento");
                    String strTotal = request.getParameter("total");
                    String strTipoVta = request.getParameter("tipo_vta");
                    final String strComentAgente = URLDecoder.decode(new String(request.getParameter("comentA").getBytes("iso-8859-1")), "UTF-8");
                    final String strComent = URLDecoder.decode(new String(request.getParameter("coment").getBytes("iso-8859-1")), "UTF-8");
                    String strComprobante = request.getParameter("comprobante");
                    String strPagoOk = request.getParameter("pagoOk");
                    String strReferencia = request.getParameter("digito");
                    String strFechaPago = "";
                    System.out.println("fecha: " + request.getParameter("fechapago"));
                    if (!request.getParameter("fechapago").equals("")) {
                        strFechaPago = fec.FormateaBD(request.getParameter("fechapago"), "/");
                    }

                    String strEsReservacion = "0";
                    String strPagado = "0";

                    System.out.println("tipo de venta " + strTipoVta);

                    if (strTipoVta.equals("1")) { //tkt-rserva

                        strEsReservacion = "1";
                    }
                    System.out.println(" ################ comprobante de pago: " + strComprobante);
                    if (!strComprobante.equals("")) {

                        strPagado = "1";
                        strSql = "update vta_cliente set CT_ES_PROSPECTO = 0 where CT_ID = (select CT_ID from vta_tickets where TKT_ID = " + strIdFac_Tkt + ");";
                        oConn.runQueryLMD(strSql);
                    }

//                    String strCompletaSql = "";
                    String strSqlUpdate = "";
                    String strResultado = "";

                    //tkt
                    strSqlUpdate = "UPDATE vta_tickets SET "
                            + "TKT_IMPUESTO1='" + strIva + "', "
                            + "TKT_TOTAL='" + strTotal + "', "
                            + "TKT_IMPORTE='" + strImporte + "', "
                            + "TKT_SALDO='" + strTotal + "', "
                            + "TKT_GANANCIA='" + strTotal + "', "
                            + "TKT_NOTAS='" + strComent + "', "
                            + "TKT_DESCUENTO='" + strDesc + "', "
                            + "TKT_NOTASPIE='" + strComentAgente + "', "
                            + "TKT_FORMADEPAGO='" + strFormaPago + "', "
                            + "TKT_METODODEPAGO='" + strMetodo + "', "
                            + "TKT_COFIDE_PAGADO='" + strPagado + "', "
                            + "TKT_NOMPAGO='" + strComprobante + "', "
                            + "TKT_ES_RESERVACION = '" + strEsReservacion + "', "
                            + "TKT_REFERENCIA = '" + strReferencia + "', "
                            + "TKT_FECHA_COBRO = '" + strFechaPago + "' "
                            + "WHERE TKT_ID = " + intIdFac_Tkt;
                    oConn.runQueryLMD(strSqlUpdate);

                    if (!oConn.isBolEsError()) {

                        //ELIMINA LOS DETALLES DE LA VENTA
                        strSqlUpdate = "DELETE FROM vta_ticketsdeta where TKT_ID = " + intIdFac_Tkt;
                        oConn.runQueryLMD(strSqlUpdate);

                        if (!oConn.isBolEsError()) {

                            //LLENA LOS DETALLES DE LA VENTA
                            String strSql = "select * from cofide_venta where CV_ID_M = " + strIdMov;
                            ResultSet rsDeta = oConn.runQuery(strSql, true);
                            while (rsDeta.next()) {

                                double dblPrecio = rsDeta.getDouble("CV_PRECIO");
                                int intCantidad = rsDeta.getInt("CV_LUGARES");
                                double dblDescuento = rsDeta.getDouble("CV_DESC");
                                double dblIVA = rsDeta.getDouble("CV_IVA");
                                double dblTotalDetalle = rsDeta.getDouble("CV_TOTAL"); //temportal

                                double dblImporte = ((dblPrecio * intCantidad) - dblDescuento) + dblIVA;

                                System.out.println("########## DETALLE ############");
                                System.out.println("PRECIO: " + dblPrecio);
                                System.out.println("CANTIDAD: " + intCantidad);
                                System.out.println("DESCUENTO: " + dblDescuento);
                                System.out.println("IVA: " + dblIVA);
                                System.out.println("IMPORTE DIRECTO: " + dblTotalDetalle);
                                System.out.println("IMPORTE CALCULADO: " + dblImporte);

                                //tkt deta
                                System.out.println("\n\n va a iniciar el guardado de detalles \n\n");
                                strSqlUpdate = "INSERT INTO vta_ticketsdeta "
                                        + "(TKT_ID, TKTD_DESCRIPCION, TKTD_IMPORTE, TKTD_CANTIDAD, TKTD_TASAIVA1, TKTD_IMPUESTO1, "
                                        + "TKTD_IMPORTEREAL, TKTD_PRECIO, PR_ID, TKTD_DESCUENTO, TKTD_PORDESC, TKTD_PRECREAL, "
                                        + "TKTD_UNIDAD_MEDIDA, TKTD_DESC_PREC, TKTD_DESC_PUNTOS, TKTD_DESC_VNEGOCIO, TKTD_TIPO_CURSO, "
                                        + "TKTD_ID_CURSO, TKTD_CVE_PRODSERV,TKTD_COMENTARIO, TKTD_CVE, SC_ID ) VALUES "
                                        + "('" + intIdFac_Tkt + "', '" + rsDeta.getString("CV_CURSO") + "', '" + dblImporte + "', '" + intCantidad + "', '16.00000', '" + dblIVA + "', "
                                        + "'" + dblImporte + "', '" + dblPrecio + "', '" + rsDeta.getInt("CV_ID_CURSO") + "', '" + dblDescuento + "', '" + rsDeta.getDouble("CV_DESC_POR") + "', '" + dblPrecio + "', "
                                        + "'678', '" + 0 + "', '" + 0 + "', '" + 0 + "', '" + rsDeta.getInt("CV_TIPO_CURSO") + "', "
                                        + "'" + rsDeta.getInt("CV_ID_CURSO") + "', '86101700','" + strComent + "','....','1')";

                                oConn.runQueryLMD(strSqlUpdate);
                                if (!oConn.isBolEsError()) {

                                    strResultado = "OK";
                                }

                            }
                            rsDeta.close();

                        } else {
                            System.out.println("Hubó un porblema al eliminar lso detalles del ticket para llenarlos nuevamente con la actualización: [ " + oConn.getStrMsgError() + " ]");
                        }

                        // SI TODO VA BIEN, ACTUALIZA A LOS PARTICIPANTES DE LA NUEVA VENTA Y MANDA LOS CORREOS
                        if (strResultado.equals("OK")) {

                            strSqlUpdate = "update cofide_participantes set CP_TKT_ID = " + intIdFac_Tkt + " where CP_ID_M = " + strIdMov;
                            oConn.runQueryLMD(strSqlUpdate);

                            if (!oConn.isBolEsError()) {

                                System.out.println("################# MANDA DATOS PARA EL CORREO ################");
                                //envio de correos con base a la actualización de la venta
                                System.out.println("***** INICIA EL ENVIO DE CORREOS *****");
                                tele.sendMail(oConn, varSesiones, request, strIdFac_Tkt);
                                System.out.println("***** TERMINA EL ENVIO DE CORREOS *****");

                            }

                        }

                    }
                    System.out.println("termino, va a regresar respuesta: " + strResultado);
                    out.clearBuffer();//Limpiamos buffer
                    atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                    out.println(strResultado);//Pintamos el resultado

                } else {

                    out.clearBuffer();//Limpiamos buffer
                    atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                    out.println("No se encontro el ID de la venta: " + request.getParameter("id_tkt"));//Pintamos el resultado
                }

            } //13
//            if (strid.equals("14")) {
//                String strSql = "";
//                String strIdFacNew = request.getParameter("id_fac_new");
//                String strIdFacOld = request.getParameter("id_fac_old");
//                strSql = "update vta_facturas set FAC_ID_OLD = " + strIdFacOld + " where FAC_ID = " + strIdFacNew;
//                oConn.runQueryLMD(strSql);
//                out.clearBuffer();//Limpiamos buffer
//                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
//                out.println("OK.");//Pintamos el resultado
//            }
            if (strid.equals("15")) {
                StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
                strXML.append("<vta>");
                String strIdCte = request.getParameter("ct_id");
                String strSql = "";
                strSql = "select CT_ID, CT_TELEFONO1, CT_EMAIL1 from vta_cliente where CT_ID = " + strIdCte;
                rs = oConn.runQuery(strSql, true);
                while (rs.next()) {
                    strXML.append("<datos ");
                    strXML.append(" telefono = \"").append(rs.getString("CT_TELEFONO1")).append("\" ");
                    strXML.append(" email = \"").append(rs.getString("CT_EMAIL1")).append("\" ");
                    strXML.append(" />");
                }
                strXML.append("</vta>");
                rs.close();
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML);//Pintamos el resultado
            }

            if (strid.equals("16")) {

                String strResultado = "";
                String strIdFac = request.getParameter("id_fac");
                String strNomComp = request.getParameter("nom_comp");

                int intPagado = 0;

                if (!strNomComp.equals("")) { //pagado OK

                    intPagado = 1;
                    strSql = "update vta_cliente set CT_ES_PROSPECTO = 0 where CT_ID = (select CT_ID from view_ventasglobales where FAC_ID = " + strIdFac + ");";
                    oConn.runQueryLMD(strSql);
                }

                String strSql = "update vta_facturas set "
                        + "FAC_NOMPAGO = '" + strNomComp + "', "
                        + "FAC_COFIDE_PAGADO = '" + intPagado + "' "
                        + "where FAC_ID = " + strIdFac;

                oConn.runQueryLMD(strSql);

                if (!oConn.isBolEsError()) {

                    System.out.println("################# manda datos para el correo ################");
                    strResultado = tele.sendMail(oConn, varSesiones, request, strIdFac);

                }

                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(strResultado);//Pintamos el resultado

            } //16
            if (strid.equals("17")) { //valida si existe en compaq cofide_cliente

                String strResultado = "OK"; //manda el ok si no encontro ningun conflicto con las bases de datos
                String strEsNuevo = "0"; //no ES NUEVO

                String strRFC_ = "";
                String strIdCte = "";
                String strCorreo = request.getParameter("correo");
                String strTelefono = request.getParameter("telefono");
                final String strRFC = URLDecoder.decode(new String(request.getParameter("rfc").getBytes("iso-8859-1")), "UTF-8");
                strRFC_ = strRFC;
                String strTipo = request.getParameter("tipovta"); //tipo de venta fac = 1, tkt = 0      
                String strIDVTA = request.getParameter("id_vta"); //venta gral = 1 para buscar la información del cliente  

                System.out.println("###### OBTIENE INFIRMACIÓN DE LA VENTA  #####");
                String[] strInfoCte = tele.getInfoCTE(oConn, strIDVTA, strTipo);
                strIdCte = strInfoCte[0];

                //OBTIENE LOS DATOS PARA EL TICKET DESDE EL CLIENTE
                if (strTipo.equals("0")) {

                    //OBTIENE TODA LA INFORMACIÓN DEL CLIENTE, ID CLIENTE, CORREO Y TELEFONO                                  
                    strRFC_ = "";
                    strCorreo = strInfoCte[2];
                    strTelefono = strInfoCte[3];
                    System.out.println("###################### OBTENDRA INFORMACIÓN PARA VALIDAR LOS TICKETS");

                }

                // VALIDA SI EL REGISTRO YA EXISTE EN COMPAQ O ES NUEVO PARA AGREGARLO TICKET O FACTURA
                System.out.println("############################ SE VA A BUSCAR QUE EXISTA EN LA BASE DE ADMIN #############################");
                //TRUE = NO HAY COINCIDENCIAS, ES NUEVO / FALSE = ENCONTRO ALGO, YA ES EXISTENTE
                if (getEsCte(oConn, strRFC_, strCorreo, strTelefono, strIdCte)) {

                    strEsNuevo = "1";
                    //ETIQUETA COMO NUEVO, EN LA VENTA
                    strSql = "update vta_tickets set COFIDE_NVO = 1 where TKT_ID = " + strIDVTA;
                    oConn.runQueryLMD(strSql);
                    System.out.println("########### ES NUEVO ##############");

                    if (!oConn.isBolEsError()) {
                        System.out.println("############################ NO SE ENCUENTRA EN LA BASE Y SE DARÁ DE ALTA #############################");
                        setCte(oConn, strRFC_, strCorreo, strTelefono, strIdCte); //se guarda a la tabla de cofide_clientes si no existe                    
                    } else {
                        System.out.println("ERROR AL VALIDAR EL CLIENTE NUEVO: [ " + oConn.getStrMsgError() + " ]");
                    }
                } else {//ya valido si es nuevo o ya era cliente
                    System.out.println(" NO ES NUEVO, YA EXISTE O ES EX PARTICIANTE ");
                }

                // valida con la base de datos, regresa OK, o el ID
                System.out.println("\n################### VALIDA, LA DUPLICIDAD ###################\n");
                System.out.println("\n################### TIPO: " + (strTipo.equals("1") ? "FACTURA" : "TICKET") + " ###################\n");
                // VALIDA DUPLICIDAD, OK = NO HAY DUPLICIDAD / ID = HUBO DUPLICIDAD, REGRESA EL ID MAESTRO DE LA DUPLICIDAD
                strResultado = getDuplicidad(oConn, strCorreo, strTelefono, strRFC_, strTipo, strIdCte);

                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(strResultado + "," + strEsNuevo);//Pintamos el resultado
            } // 17
            if (strid.equals("18")) {
                System.out.println("##################### INICIA VENTA DUPLICADA, PARA VALIDACIÓN ###################");

                String strIdFac_Tkt = request.getParameter("id_tkt"); //id vta master temporal
                String strIDVTA = request.getParameter("idventa"); // id de la venta  
                String strTipoVta = request.getParameter("tipo_vta"); //ticket 2 o factura 3
                String strTipoDocumento = request.getParameter("documento"); //tipo de documento
                String strForma = request.getParameter("forma"); //tipo de documento
                String strMetodo = request.getParameter("metodo"); //tipo de documento
                String strTipoDoc = "0";
                if (strTipoDocumento.equals("FACTURA")) {
                    strTipoDoc = "1";
                }
                String[] arrInfoCte = tele.getInfoCTE(oConn, strIDVTA, strTipoDoc);

                String strDuplicado = request.getParameter("duplicado"); //DUPLICADO
                String strFacturar = request.getParameter("facturar"); //FACTURAR

                //datos para mostrar si se duplican o no
                String strCorreoD = request.getParameter("correod");
                String strTelefonoD = request.getParameter("telefonod");
                String strRFC_ = "";
                final String strRFCD = URLDecoder.decode(new String(request.getParameter("rfcd").getBytes("iso-8859-1")), "UTF-8");
                strRFC_ = strRFCD;

                //ETIQUETA LA VENTA COMO DUPLICADA, Y SI SE VA A FACTURAR O NO
                strSql = "update vta_tickets set "
                        + "COFIDE_DUPLICIDAD = '" + strDuplicado + "' , "
                        + "TKT_FORMADEPAGO = '" + strForma + "' , "
                        + "TKT_METODODEPAGO = '" + strMetodo + "' , "
                        + "TKT_FACTURAR = '" + strFacturar + "' "
                        + "where TKT_ID = " + strIDVTA;
                oConn.runQueryLMD(strSql);

                String strCtID = arrInfoCte[0]; //id cliente 
                if (strTipoVta.equals("2")) { //tkt
                    //SI ES UN TICKET , OBTENEMOS LOS DATOS DIRECTO DEL CLIENTE

                    strRFC_ = arrInfoCte[1]; //rfc
                    strCorreoD = arrInfoCte[2]; // correo
                    strTelefonoD = arrInfoCte[3]; //telefono

                }

                //información para facturare
                String strRFCD_ = "";

                //REVISAMOS CUAL DE LOS DATOS, TIENE CONFLICTO, RECUPERAMOS UNICAMENTE LOS QUE ESTAN CON OTROS ID'S
                String[] lstInfo = getDetalleConflicto(oConn, strCtID, strTelefonoD, strCorreoD, strRFCD, strTipoVta);
                strCorreoD = lstInfo[0];
                strTelefonoD = lstInfo[1];
                strRFCD_ = lstInfo[2];
//                strRFCD = strRFCD_;
                //conflictos
                String strSql = "";
                String strResultado = "";
                //tkt
                System.out.println("\n################### INICIA EL GUARDADO DE LA VENTA TEMPORAL ###################\n");
                //GUARDAMOS LA INFORMACIÓN DE LA VENTA QUE TUVO CONFLICTOS, UNICAMENTE LOS DATOS CON CONFLICTOS Y LOS ID DE LAS VENTAS

                strSql = "INSERT INTO cofide_venta_temporal "
                        + "(VTA_ID_M, VTA_CT_ID, VTA_AGENTE, VTA_FECHA_ALTA, "
                        + "VTA_ESTATUS,VTA_CORREO_D, VTA_TELEFONO_D, VTA_RFC_D, FAC_ID, VTA_TIPO) VALUES "
                        + "('" + strIdFac_Tkt + "', '" + strCtID + "', '" + varSesiones.getIntNoUser() + "', '" + fec.getFechaActual() + "', "
                        + "'1', '" + strCorreoD + "', '" + strTelefonoD + "', '" + strRFCD_ + "', '" + strIDVTA + "','" + strTipoVta + "')";

                oConn.runQueryLMD(strSql);
                System.out.println("\n################### termina de guardar la venta temporal ###################\n");

                if (!oConn.isBolEsError()) {

                    strResultado = "OK";

                }
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(strResultado);//Pintamos el resultado
            } //18

            if (strid.equals("19")) { //19
                // ID DE VENTA A ANULAR, POR ACTUALIZACIÓN DE DOCUMENTO
                String strIdVta = request.getParameter("id_vta");
                String strResultado = "";

                String strSql = "update vta_tickets set "
                        + "TKT_US_MOD = " + varSesiones.getIntNoUser() + ", "
                        + "TKT_US_ANUL = " + varSesiones.getIntNoUser() + ", "
                        + "TKT_ANULADA = 1 "
                        + "where TKT_ID = " + strIdVta;

                oConn.runQueryLMD(strSql);

                if (!oConn.isBolEsError()) {

                    strResultado = "OK";

                }

                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(strResultado);//Pintamos el resultado
            } //19

            //Consulta Participantes del cliente
            if (strid.equals("20")) {
                StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
                strXML.append("<vta>");
                String strIdCTE = request.getParameter("CT_ID");
                String strContactos = request.getParameter("CONTACTOS");
                String strCCO_ID = "";
                String strCCO_NOMBRE = "";
                String strCCO_APPAT = "";
                String strCCO_APMAT = "";
                String strCCO_TITULO = "";
                String strCCO_NOSOCIO = "";
                String strCCO_AREA = "";
                String strCCO_ASOC = "";
                String strCCO_CORREO = "";
                String strCT_MailMes = "";
                String strCT_MailMes2 = "";
                String strCCO_CORREO2 = "";
                String strCCO_TELEFONO = "";
                String strCCO_EXT = "";
                String strCCO_ALTERNO = "";
                String strParticipante = "";
                double dblTotalPtos = 0.0;
                int intIdContacto = 0;
                String strSqlCTE = "select *,"
                        + "concat(CCO_TITULO,' ',CCO_NOMBRE,' ',CCO_APPATERNO,' ',CCO_APMATERNO)as NOM_PART "
                        + "from cofide_contactos where CT_ID = " + strIdCTE;
                if (!strContactos.equals("")) {
                    strSqlCTE += " and CONTACTO_ID not in (" + strContactos + ")";
                }
                try {
                    ResultSet rsCTE = oConn.runQuery(strSqlCTE, true);
                    while (rsCTE.next()) {
                        strCCO_ID = rsCTE.getString("CCO_ID");
                        strCCO_NOMBRE = rsCTE.getString("CCO_NOMBRE");
                        strCCO_APPAT = rsCTE.getString("CCO_APPATERNO");
                        strCCO_APMAT = rsCTE.getString("CCO_APMATERNO");
                        strCCO_TITULO = rsCTE.getString("CCO_TITULO");
                        strCCO_NOSOCIO = rsCTE.getString("CCO_NOSOCIO");
                        strCCO_AREA = rsCTE.getString("CCO_AREA");
                        strCCO_ASOC = rsCTE.getString("CCO_ASOCIACION");
                        strCCO_CORREO = rsCTE.getString("CCO_CORREO");
                        strCT_MailMes = rsCTE.getString("CT_MAILMES");
                        strCT_MailMes2 = rsCTE.getString("CT_MAILMES2");
                        strCCO_CORREO2 = rsCTE.getString("CCO_CORREO2");
                        strCCO_TELEFONO = rsCTE.getString("CCO_TELEFONO");
                        strCCO_EXT = rsCTE.getString("CCO_EXTENCION");
                        strCCO_ALTERNO = rsCTE.getString("CCO_ALTERNO");
                        intIdContacto = rsCTE.getInt("CONTACTO_ID");
                        strCCO_ALTERNO = util.Sustituye(rsCTE.getString("NOM_PART"));

                        strXML.append("<datos ");
                        strXML.append(" CCO_ID = \"").append(strCCO_ID).append("\"");
                        strXML.append(" CT_ID = \"").append(strIdCTE).append("\"");
                        strXML.append(" CCO_NOMBRE = \"").append(strCCO_NOMBRE).append("\"");
                        strXML.append(" CCO_APPATERNO = \"").append(strCCO_APPAT).append("\"");
                        strXML.append(" CCO_APMATERNO = \"").append(strCCO_APMAT).append("\"");
                        strXML.append(" CCO_TITULO = \"").append(strCCO_TITULO).append("\"");
                        strXML.append(" CCO_NOSOCIO = \"").append(strCCO_NOSOCIO).append("\"");
                        strXML.append(" CCO_AREA = \"").append(strCCO_AREA).append("\"");
                        strXML.append(" CCO_ASOCIACION = \"").append(strCCO_ASOC).append("\"");
                        strXML.append(" CCO_CORREO = \"").append(strCCO_CORREO).append("\"");
                        strXML.append(" CT_MAILMES1 = \"").append((strCT_MailMes == "1" ? "SI" : "NO")).append("\"");
                        strXML.append(" CT_MAILMES2 = \"").append((strCT_MailMes2 == "1" ? "SI" : "NO")).append("\"");
                        strXML.append(" CCO_CORREO2 = \"").append(strCCO_CORREO2).append("\"");
                        strXML.append(" CCO_TELEFONO = \"").append(util.Sustituye(strCCO_TELEFONO)).append("\"");
                        strXML.append(" CCO_EXTENCION = \"").append(strCCO_EXT).append("\"");
                        strXML.append(" CCO_ALTERNO = \"").append(util.Sustituye(strCCO_ALTERNO)).append("\"");
                        strXML.append(" CCO_PUNTOS = \"").append(dblTotalPtos).append("\"");
                        strXML.append(" CONTACTO_ID = \"").append(intIdContacto).append("\"");
                        strXML.append(" PARTICIPANTE = \"").append(strCCO_ALTERNO).append("\"");
                        strXML.append(" />");
                    }
                    rsCTE.close();
                } catch (SQLException e) {
                    System.out.println("ERROR AL OBTENER INFORMACIÓN DEL APRTICIPANTE/CLIENTE " + e.getMessage());
                }
                strXML.append("</vta>");
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML.toString());//Pintamos el resultado
            }//Fin ID 20

            if (strid.equals("21")) { //guardamos la venta GRAL
                String strRespuesta_ = "";
                vta_.setStrPathFonts(strPathFonts);
                vta_.setStrPathPrivateKeys(strPathPrivateKeys);
                vta_.setStrPathXML(strPathXML);
                vta_.setStrfolio_GLOBAL(strfolio_GLOBAL);
                strRespuesta_ = vta_.DoVenta(oConn, request, varSesiones, this.getServletContext());

                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(strRespuesta_);//Pintamos el resultado
            }//Fin ID 21

            if (strid.equals("22")) { //guardamos la venta GRAL
                String strIdMembresia = request.getParameter("MEMBRESIA_ID");
                String strSql = "select CM_ID, CM_DESCRIPCION, CM_COSTO from cofide_membresia where CM_ID = " + strIdMembresia;
                StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
                strXML.append("<Membresia>");
                try {
                    ResultSet rs = oConn.runQuery(strSql, true);
                    while (rs.next()) {
                        strXML.append("<datos ");
                        strXML.append(" CM_ID = \"").append(rs.getInt("CM_ID")).append("\"");
                        strXML.append(" CM_DESCRIPCION = \"").append(util.Sustituye(rs.getString("CM_DESCRIPCION"))).append("\"");
                        strXML.append(" CM_COSTO = \"").append(rs.getDouble("CM_COSTO")).append("\"");
                        strXML.append(" />");
                    }
                    rs.close();
                } catch (SQLException ex) {
                    System.out.println("Error[ID:22]:" + ex.getLocalizedMessage());
                }
                strXML.append("</Membresia>");
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML.toString());//Pintamos el resultado
            }//Fin ID 22
            if (strid.equals("23")) { //guardamos la venta GRAL
                StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
                strXML.append("<vta>");
                int intFiltro = Integer.parseInt(request.getParameter("filtro"));
                String strCursos = request.getParameter("curso");
                int intLength = Integer.parseInt(request.getParameter("length"));
                String strFiltro = "<=";
                String strIds = "";
                String strExiste = "0";
                String strParticipante = "";
                int intConteo = 0;

                for (int i = 0; i < intLength; i++) {
                    strIds = request.getParameter("ids" + i);
                    String strSql = "select *,count(*) as cuantos "
                            + "from cofide_participantes "
                            + "where CONTACTO_ID = " + strIds + " and CP_ID_CURSO = " + strCursos + " and CP_ESTATUS = 1 and (CP_FAC_ID <> 0 or CP_TKT_ID <> 0)";
                    ResultSet rs = oConn.runQuery(strSql, true);
                    while (rs.next()) {
                        System.out.println(rs.getInt("cuantos"));
                        if (rs.getInt("cuantos") >= 1) {
                            if (intFiltro == 1) {//es edición, busca resultados
                                if (rs.getInt("cuantos") > 1) {
                                    strParticipante += rs.getString("CP_NOMBRE") + " " + rs.getString("CP_APPAT") + "\n";
                                    strExiste = "1";
                                }
                            } else { //arrojo resultados y es nuevo
                                strExiste = "1";
                                strParticipante += rs.getString("CP_NOMBRE") + " " + rs.getString("CP_APPAT") + "\n";
                            }
                        }
                    }
                    rs.close();
                    System.out.println("participantes: " + strParticipante);
                }

                strXML.append("<datos ");
                strXML.append(" existe = \"").append(strExiste).append("\"");
                strXML.append(" participante = \"").append(strParticipante).append("\"");
                strXML.append(" />");
                strXML.append("</vta>");
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML.toString());//Pintamos el resultado

            }
        } //fin if strid != null
    }
    oConn.close();

%>

<%!
    public void PagarFactura(Conexion oConn, String strFac_ID, String strTipoDoc) {
        String strSql = "";
        if (strTipoDoc.equals("1")) {
            strSql = "update vta_facturas set FAC_COFIDE_PAGADO = 1 where FAC_ID =" + strFac_ID;
        } else {
            strSql = "update vta_tickets set TKT_COFIDE_PAGADO = 1 where TKT_ID =" + strFac_ID;
        }
        oConn.runQueryLMD(strSql);
    }
%>

<%!
    public void enviaMailMasivo(Conexion oConn, String strPosX, String strTitle, String strFacId, VariableSession varSesiones, String strTipoVta, String strCorreovta1, String strCorreovta2) {

//Respuesta del servicio
        boolean bolEsNC = false;
        //Cargamos datos del mail
        //Recuperamos paths de web.xml
        String strPathXML = this.getServletContext().getInitParameter("PathXml");
        String strPathFonts = this.getServletContext().getRealPath("/") + System.getProperty("file.separator") + "fonts";

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
                        strFAC_RAZONSOCIAL = rs.getString("FAC_RAZONSOCIAL").trim();
                        strFAC_FECHA = rs.getString("FAC_FECHA").trim();
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
                            strFAC_RAZONSOCIAL = rs.getString("TKT_RAZONSOCIAL").trim();
                            strFAC_FECHA = rs.getString("TKT_FECHA").trim();
                            intFAC_ES_CFD = 0;
                            intFAC_ES_CBB = 0;
                            strFolio_C = strNumFolio;
                        } else {
                            strNumFolio = rs.getString("NC_FOLIO");
                            intEMP_TIPOCOMP = rs.getInt("NC_TIPOCOMP");
                            intEMP_ID = rs.getInt("EMP_ID");
                            intCT_ID = rs.getInt("CT_ID");
                            strFAC_NOMFORMATO = rs.getString("NC_NOMFORMATO");
                            strFAC_RAZONSOCIAL = rs.getString("NC_RAZONSOCIAL").trim();
                            strFAC_FECHA = rs.getString("NC_FECHA").trim();
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
                    format.setStrPath(this.getServletContext().getRealPath("/"));
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
            System.out.println("error al obtener la modalidad del curSO " + sql.getMessage());
        }

        return intMod;
    }
%>


<%!    /**
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
            System.out.println(ex.getErrorCode());;
        }
        return listValores;
    }
%>

<%!   /**
     *
     *
     * Genera el n re de ml
     */
    public String getNombreFileXml(ERP_MapeoFormato mapeo, int intTransaccion, String strNombreReceptor, String strFechaCFDI, String strFolioFiscalUUID) {
        String strNomFileXml = null;
        String strPatronNomXml = mapeo.getStrNomXML("NOMINA");
        strNomFileXml = strPatronNomXml.replace("%Transaccion%", intTransaccion + "").replace("", "").replace("%nombre_receptor%", strNombreReceptor).replace("%fecha%", strFechaCFDI).replace("%UUID%", strFolioFiscalUUID).replace(" ", "_");
        strNomFileXml = strNomFileXml.replace("__", "_");
        return strNomFileXml;
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
%>

<%!
    String strSql = "";
    ResultSet rs;
    String strRes = "";
    boolean bolRes = true;
    Fechas fec = new Fechas();

    //guarda los id de los cleintes con conflictos de duplicidad
    public String SaveVtaConflicto(Conexion oConn, String strIdMVtaTmp, String strIdCteConflicto) { //conexion, idMConflicto, idCteConflicto
//        System.out.println("############################ guardando detalles #############################");
        strSql = "insert into cofide_ventas_duplicadas (CVD_FAC_ID, CVD_CT_ID)values('" + strIdMVtaTmp + "','" + strIdCteConflicto + "')";
        oConn.runQueryLMD(strSql);
        if (!oConn.isBolEsError()) {
            strRes = "OK";
            System.out.println("Se guardo el conflicto");
        } else {
            System.out.println("No se guardo el conflicto");
        }
        return strRes;
    }

// valida que el cliente sea de la misma db del ejecutivo
    public boolean getValidaDB(Conexion oConn, String strIdCte, String strDB) {
        strSql = "select CT_CLAVE_DDBB from vta_cliente where CT_ID = '" + strIdCte + "' and CT_CLAVE_DDBB = '" + strDB + "'";
        try {
            rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                bolRes = false; //sin conflicto de base de datos
            }
            rs.close();
        } catch (SQLException sql) {
            System.out.println("ERROR: al obtener la base de datos con el cliente: " + sql.getMessage());
        }
        return bolRes;
    }

    /**
     * buscamos si es cliente existente en la tbala de clientes compaq, true =
     * es nuevo strRFC = RFC a validar strEmail = correo a validar strTelefono =
     * telefono a validar strCt = id del cliente a validar
     */
    public boolean getEsCte(Conexion oConn, String strRFC, String strEmail, String strTelefono, String strCt) {
        bolRes = true; // es nuevo
        String strCompletesql = "";
        System.out.println("################ \n\n id del cliente: " + strCt);
        if (!strCt.equals("")) {
            vta_clientes cliente = new vta_clientes();
            cliente.ObtenDatos(Integer.parseInt(strCt), oConn);
            // si es proepsecto, hace la validación, si no, no lo hace
            if (cliente.getFieldInt("CT_ES_PROSPECTO") == 1) {

                System.out.println("rfc: " + strRFC);
                System.out.println("correo: " + strEmail);
                System.out.println("telefono: " + strTelefono);
                // busca el RFC en la tabla de AdminPaq
                if (!strRFC.equals("") && !strEmail.equals("") && !strTelefono.equals("")) {
                    strCompletesql = " C_RFC = '" + strRFC + "' and C_TELEFONO = '" + strTelefono + "' and C_EMAIL = '" + strEmail + "'";
                } else {
                    if (!strRFC.equals("") && !strEmail.equals("")) {
                        strCompletesql = " C_RFC = '" + strRFC + "' and C_EMAIL = '" + strEmail + "'";
                    }
                    if (!strTelefono.equals("") && !strEmail.equals("")) {
                        strCompletesql = " C_TELEFONO = '" + strTelefono + "' and C_EMAIL = '" + strEmail + "'";
                    }
                    if (!strTelefono.equals("") && !strRFC.equals("")) {
                        strCompletesql = " C_RFC = '" + strRFC + "' and C_TELEFONO = '" + strTelefono + "'";
                    }

                    if (strRFC.equals("") && strEmail.equals("") && !strTelefono.equals("")) {
                        strCompletesql = " C_TELEFONO = '" + strTelefono + "'";
                    }

                    if (!strRFC.equals("") && strEmail.equals("") && strTelefono.equals("")) {
                        strCompletesql = " C_RFC = '" + strRFC + "'";
                    }

                    if (strRFC.equals("") && !strEmail.equals("") && strTelefono.equals("")) {
                        strCompletesql = " C_EMAIL = '" + strEmail + "'";
                    }
                }

                if (!strRFC.equals("") || !strEmail.equals("") || !strTelefono.equals("")) {

                    strSql = "select C_ID from cofide_clientes where " + strCompletesql;

                    try {
                        rs = oConn.runQuery(strSql, true);

                        while (rs.next()) {

                            //Si lo encontró como cliente existentes
                            bolRes = false;

                        }
                        rs.close();
                    } catch (SQLException sql) {
                        System.out.println("ERROR: al validar si es cliente existente: " + sql.getMessage());
                    }

                } else {
                    //no busco nada, no es nuevo
                    bolRes = false;
                }
            } else {
                bolRes = false;
            }
        } else {
            bolRes = false;
        }
        return bolRes; //true es nuevo
    }

    //SI NO EXISTE EN LA TABLA DE CLEINTES, SE AGREGA COMO UN NUEVO
    public String setCte(Conexion oConn, String strRFC, String strEmail, String strTelefono, String strCt) {
        strRes = "";

        strSql = "INSERT INTO cofide_clientes (C_CT_ID, C_RFC, C_EMAIL, C_TELEFONO, C_FECHA) VALUES "
                + "('" + strCt + "', '" + strRFC + "', '" + strEmail + "', '" + strTelefono + "', '" + fec.getFechaActual() + "');";

        oConn.runQueryLMD(strSql);

        if (!oConn.isBolEsError()) {
            strRes = "OK";
        }
        System.out.println("Es nuevo: " + strRes);
        return strRes;
    }

    // SI NO HAY CONFLICTO, SOLO REGRESA UN OK, SI SI ENCUENTRA CONFLICTO, REGRESA UN OK 
    public String getDuplicidad(Conexion oConn, String strCorreo, String strTelefono, String strRFC, String strTipoVta, String strIdCte) {

        strRes = "OK"; // si se va el OK unicamente, no hubo conflicto, si hubo conflicto manda el numero de registros
        String strComplete = "";
        String strCompleteCorreo = "";
        String strCompleteTelefono = "";
        System.out.println("correo: " + strCorreo);
        System.out.println("telefono " + strTelefono);
        System.out.println("rfc " + strRFC);
        System.out.println("tipo venta " + strTipoVta);
        System.out.println("id cliente " + strIdCte);

        String strCompleteSql = "";
        String strIdMVtaTmp = "";
        int intCuantos = 0;
        boolean bolNext = false;

        //si es factura = 1, SOLO SI ES FACTURA, VALIDA EL RFC
        if (strTipoVta.equals("1")) {

            strCompleteSql = "or CT_RFC = '" + strRFC + "'";

        }

        if (!strCorreo.equals("") && !strTelefono.equals("")) {

            strComplete = " and (CT_TELEFONO1 = '" + strTelefono + "' or CT_TELEFONO2 = '" + strTelefono + "' "
                    + "or CT_EMAIL1 = '" + strCorreo + "' "
                    + "or CT_ID in (select CT_ID from cofide_contactos where CCO_TELEFONO = '" + strTelefono + "' or CCO_ALTERNO = '" + strTelefono + "' "
                    + "or CCO_CORREO = '" + strCorreo + "' or CCO_CORREO2= '" + strCorreo + "') "
                    + strCompleteSql + ")";
            bolNext = true;
        } else {

            if (!strCorreo.equals("")) {

                strComplete = " and ( CT_EMAIL1 = '" + strCorreo + "' "
                        + "or CT_ID in (select CT_ID from cofide_contactos where CCO_CORREO = '" + strCorreo + "' or CCO_CORREO2= '" + strCorreo + "') "
                        + strCompleteSql + ")";
                bolNext = true;

            } else if (!strTelefono.equals("")) {

                strComplete = " and (CT_TELEFONO1 = '" + strTelefono + "' or CT_TELEFONO2 = '" + strTelefono + "'  "
                        + "or CT_ID in (select CT_ID from cofide_contactos where CCO_TELEFONO = '" + strTelefono + "' or CCO_ALTERNO = '" + strTelefono + "' ) "
                        + strCompleteSql + ")";
                bolNext = true;

            }

        }
        if (bolNext) {
            System.out.println("\n################### INICIA LA VALIDACIÓN DE DUPLICIDAD ###################\n");

            strSql = "select * from vta_cliente where CT_ACTIVO = 1 AND CT_ID <> '" + strIdCte + "' " + strComplete;

            try {

                ResultSet rsx = oConn.runQuery(strSql, true);

                while (rsx.next()) { //si encuentra manda el id + cuantos, si no manda OK
                    //guarda conflictos-guarda el ID del cliente principal con los id de clientes con los que presneta conflicto
                    if (intCuantos == 0) {

                        strIdMVtaTmp = SaveVtaConflictoM(oConn, strIdCte);//guarda el conflicto, regresa el id maestro
                    }
                    if (intCuantos <= 5) { //5 ITERACIONES

                        System.out.println("\n################### GUARDA CONFLICTO ###################\n");
                        SaveVtaConflicto(oConn, strIdMVtaTmp, rsx.getString("CT_ID")); //id maestro y id de ctes con conflicto
                    } else {
                        //termina la consulta
                        break;
                    }
                    intCuantos++; //obtiene el numero de conflictos
                    System.out.println("\n################### TERMINA DE GUARDAR LOS CONFLICTOS ###################\n");

                    strRes = strIdMVtaTmp;
                }
                rsx.close();
            } catch (SQLException sql) {
                System.out.println("Error al obtener conflictos con la base: " + sql.getMessage());
            }
        } else {
            System.out.println("################## LA INFORMACIÓN DEL CLIENTE, NO SE PEUDE VALIDAR!");
        }
        return strRes;
    }

    public String SaveVtaConflictoM(Conexion oConn, String strCtID) {
//        System.out.println("############################ guardando al maestro #############################");
        strSql = "insert into cofide_ventas_duplicadas_m (CDM_CT_ID) values (" + strCtID + ")";
        oConn.runQueryLMD(strSql);
        if (!oConn.isBolEsError()) {
            strSql = "select @@identity as Consecutivo";
            try {
                rs = oConn.runQuery(strSql, true);
                while (rs.next()) {
                    strRes = rs.getString("Consecutivo");
                }
                rs.close();
            } catch (SQLException sql) {
                System.out.println("Error GetConsecutivo ID: " + sql.getLocalizedMessage());
            }
        }
        return strRes;
    }

    //RECIBO LOS DATOS VALIDADOS PARA BUSCAR CUAL TUVO EL CONFLICTO
    public String[] getDetalleConflicto(Conexion oConn, String strCte, String strTelefono, String strCorreo, String strRFC, String strTipo) {
        String[] lstInfo = new String[3];
        lstInfo[0] = "";
        lstInfo[1] = "";
        lstInfo[2] = "";
        System.out.println("\n################### obtendra los datos, con los que se tiene conflicto ###################\n");
//limpia basura
        strTelefono = strTelefono.trim();
        strCorreo = strCorreo.trim();
        strRFC = strRFC.trim();
//limpia basura ok
        //BUSCA SI EL CORREO TIENE CONFLICTOS
        String strSqlval1 = "select * "
                + "from vta_cliente "
                + "where CT_ID <> '" + strCte + "' AND CT_ACTIVO = 1 "
                + "and ( CT_EMAIL1 = '" + strCorreo + "' or "
                + "CT_ID in (select CT_ID from cofide_contactos where CCO_CORREO = '" + strCorreo + "' or CCO_CORREO2= '" + strCorreo + "'))";

        //BUSCA SI EL TELEFONO TIENE CONFICTO
        String strSqlval2 = "select * "
                + "from vta_cliente "
                + "where CT_ID <> '" + strCte + "' AND CT_ACTIVO = 1 "
                + "and (CT_TELEFONO1 = '" + strTelefono + "' or CT_TELEFONO2 = '" + strTelefono + "'  or "
                + "CT_ID in (select CT_ID from cofide_contactos where CCO_TELEFONO = '" + strTelefono + "' or CCO_ALTERNO = '" + strTelefono + "' ))";

        //BUSCA SI EL RFC TIENE CONFLICTO
        String strSqlval3 = "select * "
                + "from vta_cliente "
                + "where CT_ID <> '" + strCte + "' AND CT_ACTIVO = 1 "
                + "AND CT_RFC = '" + strRFC + "'";

        ResultSet rsVal;
        try {
// si trae datos, ejecuta la consuylta, si no no.
            if (!strCorreo.equals("")) {
                rsVal = oConn.runQuery(strSqlval1, true);
                while (rsVal.next()) {

                    lstInfo[0] = strCorreo;

                }
                rsVal.close();
            }
//valida telefono
// si trae datos, ejecuta la consuylta, si no no.
            if (!strTelefono.equals("")) {
                rsVal = oConn.runQuery(strSqlval2, true);
                while (rsVal.next()) {

                    lstInfo[1] = strTelefono;

                }
                rsVal.close();
            }
            //ES FACTURA
            if (strTipo.equals("3")) {
// valida rfc
// si trae datos, ejecuta la consuylta, si no no.
                if (!strRFC.equals("")) {
                    rsVal = oConn.runQuery(strSqlval3, true);
                    while (rsVal.next()) {

                        lstInfo[2] = strRFC;

                    }
                    rsVal.close();
                }
            }
        } catch (SQLException sql) {
            System.out.println("Error al obtener la consulta: " + sql.getMessage());
        }
        System.out.println("\n################### MUESTRA LOS CONFLICTOS ###################\n");
        System.out.println("CORREO: " + lstInfo[0]);
        System.out.println("telefono: " + lstInfo[1]);
        System.out.println("rfc: " + lstInfo[2]);
        return lstInfo;
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

    Telemarketing tele_ = new Telemarketing();

    public int getLugarVendido(String strNomCurso, Conexion oConn) {
        int intVendidos = 0;
        int intRRHH = 0;
        strSql = "select sum(if"
                + "(TIPO_DOC = 'F',"
                + "(select FACD_CANTIDAD from vta_facturasdeta where vta_facturasdeta.FAC_ID = view_ventasglobales.FAC_ID),"
                + "(select TKTD_CANTIDAD from vta_ticketsdeta where vta_ticketsdeta.TKT_ID = view_ventasglobales.FAC_ID)"
                + ")"
                + ") as cuantos from view_ventasglobales "
                + "where FAC_ANULADA = 0 and CANCEL = 0 and FAC_TIPO_CURSO = 1 and CC_CURSO_ID = " + strNomCurso;
        try {
            rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                intVendidos = rs.getInt("cuantos");
            }
            rs.close();
        } catch (SQLException sql) {
            System.out.println("Error al obtener el numero de lugares vendidos..." + sql.getMessage());
        }
        intRRHH = tele_.getAsistentesRRHH(strNomCurso, oConn, 1);
        intVendidos += intRRHH;
        return intVendidos;
    }

%>