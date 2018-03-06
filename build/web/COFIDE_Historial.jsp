<%-- 
    Document   : COFIDE_Historial
    Created on : 14-ene-2016, 13:45:18
    Author     : juliocesar
--%>

<%@page import="com.mx.siweb.erp.especiales.cofide.Telemarketing"%>
<%@page import="com.siweb.utilerias.json.JSONObject"%>
<%@page import="com.siweb.utilerias.json.JSONArray"%>
<%@page import="ERP.Folios"%>
<%@page import="com.mx.siweb.erp.especiales.cofide.COFIDE_Calendario"%>
<%@page import="java.sql.SQLException"%>
<%@page import="comSIWeb.Utilerias.Fechas"%>
<%@page import="com.sun.tools.xjc.api.S2JJAXBModel"%>
<%@page import="comSIWeb.ContextoApt.atrJSP"%>
<%@page import="comSIWeb.Utilerias.UtilXml"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="comSIWeb.ContextoApt.VariableSession"%>
<%@page import="comSIWeb.Operaciones.Conexion"%>
<%@page import="comSIWeb.ContextoApt.Seguridad"%>
<%
    /*Obtenemos las variables de sesion*/
    VariableSession varSesiones = new VariableSession(request);
    varSesiones.getVars();
    //Abrimos la conexion
    Conexion oConn = new Conexion(varSesiones.getStrUser(), this.getServletContext());
    oConn.open();
    Fechas fec = new Fechas();
    UtilXml utilXML = new UtilXml();
    COFIDE_Calendario cal = new COFIDE_Calendario(oConn);
    ResultSet rs;
    String strSql = "";
    Telemarketing tele = new Telemarketing();
    //Instanciamos objeto de seguridad para no permitir el acceso externo a ver los XML
    Seguridad seg = new Seguridad();//Valida que la peticion se halla hecho desde el mismo sitio
    if (varSesiones.getIntNoUser() != 0 && seg.ValidaURL(request)) {
        /*Definimos parametros de la aplicacion*/
        String strid = request.getParameter("ID");
        if (strid == null) {
            strid = "0";
        }
        if (strid.equals("1")) { //historial de llamadas

            String strXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
            strXML += "<vta>";
            String strNCliente = request.getParameter("CT_NO_CLIENTE");
            int intCL_ID = 0;
            String strCL_FECHA = "";
            String strCL_HORA = "";
            String strCL_USUARIO = "";
            int intID_CLIENTE = 0;
            int intID_BASE = 0;
            int intCL_EXITOSO = 0;
            int intCL_DESCARTADO = 0;
            String strCL_COMENTARIO = "";
            String strCL_CONTACTO = "";
            strSql = "select CL_FECHA, CL_HORA,CL_ID,cofide_llamada.CL_ID_CLIENTE,CL_ID_BASE,CL_EXITOSO,CL_DESCARTADO, CL_COMENTARIO, " //toma los registros de ese agente y el cliente
                    + "(select CT_RAZONSOCIAL from vta_cliente where vta_cliente.CT_ID = cofide_llamada.CL_ID_CLIENTE) as CL_CLIENTE, "
                    + "(select user from usuarios where usuarios.id_usuarios = cofide_llamada.CL_USUARIO ) as CL_AGENTE, CL_CONTACTO "
                    + "from cofide_llamada where CL_ID_CLIENTE = " + strNCliente;
            rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                intCL_ID = rs.getInt("CL_ID");
                strCL_FECHA = rs.getString("CL_FECHA");
                strCL_HORA = rs.getString("CL_HORA");
                strCL_USUARIO = rs.getString("CL_AGENTE");
                intID_CLIENTE = rs.getInt("CL_ID_CLIENTE");
                strCL_COMENTARIO = rs.getString("CL_COMENTARIO");
                strCL_CONTACTO = rs.getString("CL_CONTACTO");
                intID_BASE = rs.getInt("CL_ID_BASE");
                intCL_EXITOSO = rs.getInt("CL_EXITOSO");
                intCL_DESCARTADO = rs.getInt("CL_DESCARTADO");
                strXML += "<datos "
                        + " CL_ID = \"" + intCL_ID + "\"  "
                        + " CL_FECHA = \"" + strCL_FECHA + "\"  "
                        + " CL_HORA = \"" + strCL_HORA + "\" "
                        + " CL_USUARIO = \"" + strCL_USUARIO + "\" "
                        + " CL_ID_CLIENTE = \"" + intID_CLIENTE + "\" "
                        + " CL_COMENTARIO = \"" + strCL_COMENTARIO + "\" "
                        + " CL_CONTACTO = \"" + strCL_CONTACTO + "\" "
                        + " CL_ID_BASE = \"" + intID_BASE + "\" "
                        + " CL_EXITOSO = \"" + intCL_EXITOSO + "\" "
                        + " CL_DESCARTADO = \"" + intCL_DESCARTADO + "\" "
                        + " />";
            } //fin del while
            strXML += "</vta>";
            strXML.toString();
            rs.close();
            out.clearBuffer();//Limpiamos buffer
            atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
            out.println(strXML);//Pintamos el resultado
        } //fin del caso

        //historial de ventas
        if (strid.equals("2")) {

            String strFecInicial = request.getParameter("strFechaInicial");
            String strFecFinal = request.getParameter("strFechaFinal");
            String strFiltro = request.getParameter("filtro");
            String strBusqueda = request.getParameter("busqueda");

            String strRazonSocial = " and FAC_RAZONSOCIAL like '%" + strBusqueda + "%' ";
            String strFac_tkt = " and  if(TIPO_DOC = 'F',FAC_FOLIO_C,  FAC_FOLIO)  = '" + strGetFolioSql(oConn, strBusqueda) + "'"; //si es por busqueda
            //String strIDCurso = " and CC_CURSO_ID = " + strBusqueda;
            String strIDCurso = " and FAC_ID in (select FAC_ID from view_ventasglobalesdeta where PR_ID = '" + strBusqueda + "') ";

            strFecInicial = fec.FormateaBD(strFecInicial, "/");
            strFecFinal = fec.FormateaBD(strFecFinal, "/");

            if (!strFiltro.equals("0")) {
                strSql = "select *,if(TIPO_DOC = 'F', FAC_FOLIO_C, FAC_FOLIO) as folio,CONCAT(usuarios.nombre_usuario,' ',usuarios.apellido_paterno) as AGENTE  "
                        + "from view_ventasglobales , usuarios "
                        + "where FAC_US_ALTA = id_usuarios AND FAC_US_MOD = 0 and FAC_US_ALTA = " + varSesiones.getIntNoUser();
            } else {
                strSql = "select *,if(TIPO_DOC = 'F', FAC_FOLIO_C, FAC_FOLIO) as folio,CONCAT(usuarios.nombre_usuario,' ',usuarios.apellido_paterno) as AGENTE  "
                        + "from view_ventasglobales , usuarios "
                        + "where FAC_US_ALTA = id_usuarios AND FAC_US_MOD = 0 and FAC_FECHA >= '" + strFecInicial + "'  and FAC_FECHA <= '" + strFecFinal + "' and FAC_US_ALTA = " + varSesiones.getIntNoUser() + " and DUPLICADO = 0 ";
            }

            if (!strBusqueda.equals("")) {
                if (strFiltro.equals("1")) {
                    strSql += strRazonSocial;
                }
                if (strFiltro.equals("2")) {
                    strSql += strFac_tkt + " and TIPO_DOC = 'F'";
                }
                if (strFiltro.equals("3")) {
                    strSql += strFac_tkt + " and TIPO_DOC = 'T'";
                }
                if (strFiltro.equals("4") || strFiltro.equals("5")) {
                    strSql += strIDCurso;
                }
            }
            strSql += " order by FAC_ANULADA ASC, CANCEL ASC, FAC_FECHA desc, FAC_HORA desc";

            String strXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
            strXML += "<Ventas>";
            rs = oConn.runQuery(strSql, true);
            int intCont = 1;
            while (rs.next()) {
                if (rs.getString("FAC_RAZONSOCIAL") != null) {
                    strRazonSocial = utilXML.Sustituye(rs.getString("FAC_RAZONSOCIAL"));
                }
                if (rs.getString("TIPO_DOC").equals("T") && rs.getString("FAC_ES_RESERVACION").equals("1")) { //si es reservacion y tipo doc ticket = reservacion
                    strXML += "<datos "
                            + " CONTADOR = \"" + intCont + "\"  "
                            + " FAC_ID = \"" + rs.getString("FAC_ID") + "\"  "
                            + " FAC_FECHA = \"" + fec.FormateaDDMMAAAA(rs.getString("FAC_FECHA"), "/") + "\"  "
                            + " FAC_TOTAL = \"" + rs.getDouble("FAC_TOTAL") + "\" "
                            + " RAZONSOCIAL = \"" + utilXML.Sustituye(strRazonSocial) + "\" "
                            + " FAC_FOLIO = \"" + rs.getString("folio") + "\" "
                            + " AGENTE = \"" + rs.getString("AGENTE") + "\" "
                            + " FAC_METODODEPAGO = \"" + rs.getString("FAC_METODODEPAGO") + "\" "
                            + " FAC_HORA = \"" + rs.getString("FAC_HORA") + "\" "
                            + " FAC_IMPORTE = \"" + rs.getDouble("FAC_IMPORTE") + "\" "
                            + " CT_ID = \"" + rs.getString("CT_ID") + "\" "
                            + " SC_ID = \"" + rs.getString("SC_ID") + "\" "
                            + " DOC_TIPO = \"2\"  "
                            + " FAC_PAGADO = \"" + rs.getString("FAC_PAGADO") + "\" "
                            + " FAC_ANULADA = \"" + rs.getString("FAC_ANULADA") + "\" "
                            + " CANCEL = \"" + rs.getString("CANCEL") + "\" "
                            + " FAC_PROMO = \"" + rs.getString("FAC_PROMO") + "\" "
                            + " FAC_IMP_PAGADO = \"" + rs.getString("FAC_IMP_PAGADO") + "\" "
                            + " FAC_ES_RESERVACION = \"" + rs.getString("FAC_ES_RESERVACION") + "\" "
                            + " FAC_ARCHIVO = \"" + rs.getString("ARCHIVO") + "\" "
                            + " FAC_VALIDA = \"" + rs.getString("VALIDA") + "\" "
                            + " FACTURAR = \"" + rs.getString("FAC_FACTURAR") + "\" "
                            + " FAC_ID_M = \"" + rs.getString("ID_MASTER") + "\" "
                            + " />";
                } else {
                    if (rs.getString("TIPO_DOC").equals("T")) {
                        strXML += "<datos "
                                + " CONTADOR = \"" + intCont + "\"  "
                                + " FAC_ID = \"" + rs.getString("FAC_ID") + "\"  "
                                + " FAC_FECHA = \"" + fec.FormateaDDMMAAAA(rs.getString("FAC_FECHA"), "/") + "\"  "
                                + " FAC_TOTAL = \"" + rs.getDouble("FAC_TOTAL") + "\" "
                                + " RAZONSOCIAL = \"" + utilXML.Sustituye(strRazonSocial) + "\" "
                                + " FAC_FOLIO = \"" + rs.getString("folio") + "\" "
                                + " AGENTE = \"" + rs.getString("AGENTE") + "\" "
                                + " FAC_METODODEPAGO = \"" + rs.getString("FAC_METODODEPAGO") + "\" "
                                + " FAC_HORA = \"" + rs.getString("FAC_HORA") + "\" "
                                + " FAC_IMPORTE = \"" + rs.getDouble("FAC_IMPORTE") + "\" "
                                + " CT_ID = \"" + rs.getString("CT_ID") + "\" "
                                + " SC_ID = \"" + rs.getString("SC_ID") + "\" "
                                + " DOC_TIPO = \"0\"  "
                                + " FAC_PAGADO = \"" + rs.getString("FAC_PAGADO") + "\" "
                                + " FAC_ANULADA = \"" + rs.getString("FAC_ANULADA") + "\" "
                                + " CANCEL = \"" + rs.getString("CANCEL") + "\" "
                                + " FAC_PROMO = \"" + rs.getString("FAC_PROMO") + "\" "
                                + " FAC_IMP_PAGADO = \"" + rs.getString("FAC_IMP_PAGADO") + "\" "
                                + " FAC_ES_RESERVACION = \"" + rs.getString("FAC_ES_RESERVACION") + "\" "
                                + " FAC_ARCHIVO = \"" + rs.getString("ARCHIVO") + "\" "
                                + " FAC_VALIDA = \"" + rs.getString("VALIDA") + "\" "
                                + " FACTURAR = \"" + rs.getString("FAC_FACTURAR") + "\" "
                                + " FAC_ID_M = \"" + rs.getString("ID_MASTER") + "\" "
                                + " />";
                    } else {
                        strXML += "<datos "
                                + " CONTADOR = \"" + intCont + "\"  "
                                + " FAC_ID = \"" + rs.getString("FAC_ID") + "\"  "
                                + " FAC_FECHA = \"" + fec.FormateaDDMMAAAA(rs.getString("FAC_FECHA"), "/") + "\"  "
                                + " FAC_TOTAL = \"" + rs.getDouble("FAC_TOTAL") + "\" "
                                + " RAZONSOCIAL = \"" + utilXML.Sustituye(strRazonSocial) + "\" "
                                + " FAC_FOLIO = \"" + rs.getString("folio") + "\" "
                                + " AGENTE = \"" + rs.getString("AGENTE") + "\" "
                                + " FAC_METODODEPAGO = \"" + rs.getString("FAC_METODODEPAGO") + "\" "
                                + " FAC_HORA = \"" + rs.getString("FAC_HORA") + "\" "
                                + " FAC_IMPORTE = \"" + rs.getDouble("FAC_IMPORTE") + "\" "
                                + " CT_ID = \"" + rs.getString("CT_ID") + "\" "
                                + " SC_ID = \"" + rs.getString("SC_ID") + "\" "
                                + " DOC_TIPO = \"1\"  "
                                + " FAC_PAGADO = \"" + rs.getString("FAC_PAGADO") + "\" "
                                + " FAC_ANULADA = \"" + rs.getString("FAC_ANULADA") + "\" "
                                + " CANCEL = \"" + rs.getString("CANCEL") + "\" "
                                + " FAC_PROMO = \"" + rs.getString("FAC_PROMO") + "\" "
                                + " FAC_IMP_PAGADO = \"" + rs.getString("FAC_IMP_PAGADO") + "\" "
                                + " FAC_ES_RESERVACION = \"" + rs.getString("FAC_ES_RESERVACION") + "\" "
                                + " FAC_ARCHIVO = \"" + rs.getString("ARCHIVO") + "\" "
                                + " FAC_VALIDA = \"" + rs.getString("VALIDA") + "\" "
                                + " FACTURAR = \"" + rs.getString("FAC_FACTURAR") + "\" "
                                + " FAC_ID_M = \"" + rs.getString("ID_MASTER") + "\" "
                                + " />";
                    }
                }
                intCont = intCont + 1;
            } //fin del while
            strXML += "</Ventas>";
            strXML.toString();
            rs.close();
            out.clearBuffer();//Limpiamos buffer
            atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
            out.println(strXML);//Pintamos el resultado

        } //fin del caso
        //historial de ventas por cte
        if (strid.equals("3")) {
            // int intCT_ID = varSesiones.getIntNoUser();
            int intCT_ID = Integer.parseInt(request.getParameter("intCTE"));
            strSql = "select *,if(TIPO_DOC = 'F', FAC_FOLIO_C, FAC_FOLIO) as folio,CONCAT(usuarios.nombre_usuario,' ',usuarios.apellido_paterno) as AGENTE "
                    + "from view_ventasglobales , usuarios "
                    + "where FAC_US_ALTA = id_usuarios and  CT_ID = " + intCT_ID + " and FAC_US_MOD = 0 AND DUPLICADO = 0 "
                    + "order by FAC_ANULADA ASC, CANCEL ASC, FAC_FECHA desc, FAC_HORA desc";

            String strXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
            strXML += "<Ventas>";
            rs = oConn.runQuery(strSql, true);
            int intCont = 1;
            while (rs.next()) {
                if (rs.getString("TIPO_DOC").equals("T") && rs.getString("FAC_ES_RESERVACION").equals("1")) { //si es reservacion y tipo doc ticket = reservacion
                    strXML += "<datos "
                            + " CONTADOR = \"" + intCont + "\"  "
                            + " FAC_ID = \"" + rs.getString("FAC_ID") + "\"  "
                            + " FAC_FECHA = \"" + fec.FormateaDDMMAAAA(rs.getString("FAC_FECHA"), "/") + "\"  "
                            + " FAC_TOTAL = \"" + rs.getDouble("FAC_TOTAL") + "\" "
                            + " RAZONSOCIAL = \"" + utilXML.Sustituye(rs.getString("FAC_RAZONSOCIAL")) + "\" "
                            + " FAC_FOLIO = \"" + rs.getString("folio") + "\" "
                            + " AGENTE = \"" + rs.getString("AGENTE") + "\" "
                            + " FAC_METODODEPAGO = \"" + rs.getString("FAC_METODODEPAGO") + "\" "
                            + " FAC_HORA = \"" + rs.getString("FAC_HORA") + "\" "
                            + " FAC_IMPORTE = \"" + rs.getDouble("FAC_IMPORTE") + "\" "
                            + " CT_ID = \"" + rs.getString("CT_ID") + "\" "
                            + " SC_ID = \"" + rs.getString("SC_ID") + "\" "
                            + " DOC_TIPO = \"2\"  "
                            + " FAC_PAGADO = \"" + rs.getString("FAC_PAGADO") + "\" "
                            + " FAC_ANULADA = \"" + rs.getString("FAC_ANULADA") + "\" "
                            + " CANCEL = \"" + rs.getString("CANCEL") + "\" "
                            + " FAC_PROMO = \"" + rs.getString("FAC_PROMO") + "\" "
                            + " FAC_IMP_PAGADO = \"" + rs.getString("FAC_IMP_PAGADO") + "\" "
                            + " FAC_ES_RESERVACION = \"" + rs.getString("FAC_ES_RESERVACION") + "\" "
                            + " FAC_ARCHIVO = \"" + rs.getString("ARCHIVO") + "\" "
                            + " FAC_VALIDA = \"" + rs.getString("VALIDA") + "\" "
                            + " FAC_ID_M = \"" + rs.getString("ID_MASTER") + "\" "
                            + " FACTURAR = \"" + rs.getString("FAC_FACTURAR") + "\" "
                            + " />";
                } else {
                    if (rs.getString("TIPO_DOC").equals("T")) {
                        strXML += "<datos "
                                + " CONTADOR = \"" + intCont + "\"  "
                                + " FAC_ID = \"" + rs.getString("FAC_ID") + "\"  "
                                + " FAC_FECHA = \"" + fec.FormateaDDMMAAAA(rs.getString("FAC_FECHA"), "/") + "\"  "
                                + " FAC_TOTAL = \"" + rs.getDouble("FAC_TOTAL") + "\" "
                                + " RAZONSOCIAL = \"" + utilXML.Sustituye(rs.getString("FAC_RAZONSOCIAL")) + "\" "
                                + " FAC_FOLIO = \"" + rs.getString("folio") + "\" "
                                + " AGENTE = \"" + rs.getString("AGENTE") + "\" "
                                + " FAC_METODODEPAGO = \"" + rs.getString("FAC_METODODEPAGO") + "\" "
                                + " FAC_HORA = \"" + rs.getString("FAC_HORA") + "\" "
                                + " FAC_IMPORTE = \"" + rs.getDouble("FAC_IMPORTE") + "\" "
                                + " CT_ID = \"" + rs.getString("CT_ID") + "\" "
                                + " SC_ID = \"" + rs.getString("SC_ID") + "\" "
                                + " DOC_TIPO = \"0\"  "
                                + " FAC_PAGADO = \"" + rs.getString("FAC_PAGADO") + "\" "
                                + " FAC_ANULADA = \"" + rs.getString("FAC_ANULADA") + "\" "
                                + " CANCEL = \"" + rs.getString("CANCEL") + "\" "
                                + " FAC_PROMO = \"" + rs.getString("FAC_PROMO") + "\" "
                                + " FAC_IMP_PAGADO = \"" + rs.getString("FAC_IMP_PAGADO") + "\" "
                                + " FAC_ES_RESERVACION = \"" + rs.getString("FAC_ES_RESERVACION") + "\" "
                                + " FAC_ARCHIVO = \"" + rs.getString("ARCHIVO") + "\" "
                                + " FAC_VALIDA = \"" + rs.getString("VALIDA") + "\" "
                                + " FAC_ID_M = \"" + rs.getString("ID_MASTER") + "\" "
                                + " FACTURAR = \"" + rs.getString("FAC_FACTURAR") + "\" "
                                + " />";
                    } else {
                        strXML += "<datos "
                                + " CONTADOR = \"" + intCont + "\"  "
                                + " FAC_ID = \"" + rs.getString("FAC_ID") + "\"  "
                                + " FAC_FECHA = \"" + fec.FormateaDDMMAAAA(rs.getString("FAC_FECHA"), "/") + "\"  "
                                + " FAC_TOTAL = \"" + rs.getDouble("FAC_TOTAL") + "\" "
                                + " RAZONSOCIAL = \"" + utilXML.Sustituye(rs.getString("FAC_RAZONSOCIAL")) + "\" "
                                + " FAC_FOLIO = \"" + rs.getString("folio") + "\" "
                                + " AGENTE = \"" + rs.getString("AGENTE") + "\" "
                                + " FAC_METODODEPAGO = \"" + rs.getString("FAC_METODODEPAGO") + "\" "
                                + " FAC_HORA = \"" + rs.getString("FAC_HORA") + "\" "
                                + " FAC_IMPORTE = \"" + rs.getDouble("FAC_IMPORTE") + "\" "
                                + " CT_ID = \"" + rs.getString("CT_ID") + "\" "
                                + " SC_ID = \"" + rs.getString("SC_ID") + "\" "
                                + " DOC_TIPO = \"1\"  "
                                + " FAC_PAGADO = \"" + rs.getString("FAC_PAGADO") + "\" "
                                + " FAC_ANULADA = \"" + rs.getString("FAC_ANULADA") + "\" "
                                + " CANCEL = \"" + rs.getString("CANCEL") + "\" "
                                + " FAC_PROMO = \"" + rs.getString("FAC_PROMO") + "\" "
                                + " FAC_IMP_PAGADO = \"" + rs.getString("FAC_IMP_PAGADO") + "\" "
                                + " FAC_ES_RESERVACION = \"" + rs.getString("FAC_ES_RESERVACION") + "\" "
                                + " FAC_ARCHIVO = \"" + rs.getString("ARCHIVO") + "\" "
                                + " FAC_VALIDA = \"" + rs.getString("VALIDA") + "\" "
                                + " FAC_ID_M = \"" + rs.getString("ID_MASTER") + "\" "
                                + " FACTURAR = \"" + rs.getString("FAC_FACTURAR") + "\" "
                                + " />";
                    }
                }
                intCont = intCont + 1;
            } //fin del while
            strXML += "</Ventas>";
            strXML.toString();
            rs.close();
            out.clearBuffer();//Limpiamos buffer
            atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
            out.println(strXML);//Pintamos el resultado
        } //3
        if (strid.equals("4")) {
            System.out.println("METODO 4");
            String strID_Fac_Tkt = request.getParameter("strFac_Tkt");
            String strTipoDoc = request.getParameter("strTipoDoc");
            String strCurso = "";
            String strFechaCurso = "";
            String strLimite = "";
            String strCosto = "";
            String strCostoIva = "";
            String strReservacion = "";
            String strTipoDocumento = "";
            String strHorario = "";
            String strSede = "";
            String strPrefijo = "";
            String strFechaCobro = "";
            if (strTipoDoc.equals("FACTURA")) {
                strPrefijo = "FAC_";
                strSql = "select *,"
                        + "(select FACD_CANTIDAD FROM vta_facturasdeta where FAC_ID = vta_facturas.FAC_ID limit 1) as CANTIDAD,"
                        + "(select FACD_PRECIO FROM vta_facturasdeta where FAC_ID = vta_facturas.FAC_ID limit 1) as PRECIOUNIT,"
                        + "(select FACD_PORDESC FROM vta_facturasdeta where FAC_ID = vta_facturas.FAC_ID limit 1) as PORDESC,"
                        + "if((select CP_MATERIAL_IMPRESO from cofide_participantes where CP_FAC_ID = FAC_ID limit 1) = 1,1,0) as REQ_MATERIAL,"
                        + "(select FACD_TIPO_CURSO from vta_facturasdeta where vta_facturasdeta.FAC_ID = vta_facturas.FAC_ID limit 1) as TipoCurso "
                        + "from vta_facturas where FAC_ID = " + strID_Fac_Tkt;
            }
            if (strTipoDoc.equals("TICKET") || strTipoDoc.equals("RESERVACIÓN")) {
                strPrefijo = "TKT_";
                strSql = "select *,"
                        + "(select TKTD_CANTIDAD FROM vta_ticketsdeta where TKT_ID = vta_tickets.TKT_ID limit 1) as CANTIDAD,"
                        + "(select TKTD_PRECIO FROM vta_ticketsdeta where TKT_ID = vta_tickets.TKT_ID limit 1) as PRECIOUNIT,"
                        + "(select TKTD_PORDESC FROM vta_ticketsdeta where TKT_ID = vta_tickets.TKT_ID limit 1) as PORDESC,"
                        + "if((select CP_MATERIAL_IMPRESO from cofide_participantes where CP_TKT_ID = TKT_ID limit 1) = 1,1,0) as REQ_MATERIAL, "
                        + "(select TKTD_TIPO_CURSO from vta_ticketsdeta where vta_ticketsdeta.TKT_ID = vta_tickets.TKT_ID) as TipoCurso "
                        + "from vta_tickets where TKT_ID = " + strID_Fac_Tkt;
            }
            System.out.println("TIPO DOCUMENTO : " + strTipoDoc);
            String strXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
            strXML += "<Ventas>";
            String strNomCampo = strPrefijo + "TIPO_CURSO";
            rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                int[] intOcupado = new int[2];
//                System.out.println(rs.getString("FAC_TOTAL") + "\n total #############");
                if (rs.getInt(strNomCampo) == 4) {
                    String strSqlCurso = "select * from cofide_membresia where CM_ID = " + rs.getString("CC_CURSO_ID");
                    ResultSet rs2 = oConn.runQuery(strSqlCurso, true);
                    while (rs2.next()) {
                        strCurso = rs2.getString("CM_DESCRIPCION");
                        strFechaCurso = rs.getString(strPrefijo + "FECHA_COBRO");
                        strLimite = "";
                        strCosto = rs2.getString("CM_COSTO");
                        strHorario = "";
                        strSede = "";
                    }
                    rs2.close();
                } else {
                    intOcupado = cal.getintVenta(rs.getString("CC_CURSO_ID"));
                    String strSqlCurso = "select CC_NOMBRE_CURSO,CC_FECHA_INICIAL,CC_PRECIO_PRES,CC_MONTAJE,concat(CC_HR_EVENTO_INI,' a ',CC_HR_EVENTO_FIN) as CC_HORARIO,"
                            + "(select cofide_sede_hotel.CSH_SEDE from cofide_sede_hotel where cofide_sede_hotel.CSH_ID = cofide_cursos.CC_SEDE_ID) as CC_SEDE "
                            + "from cofide_cursos where cc_curso_id = " + rs.getString("CC_CURSO_ID");
                    ResultSet rs2 = oConn.runQuery(strSqlCurso, true);
                    while (rs2.next()) {
                        strCurso = rs2.getString("CC_NOMBRE_CURSO");
                        strFechaCurso = rs2.getString("CC_FECHA_INICIAL");
                        strLimite = rs2.getString("CC_MONTAJE");
                        strCosto = rs2.getString("CC_PRECIO_PRES");
                        strHorario = rs2.getString("CC_HORARIO");
                        strSede = utilXML.Sustituye(rs2.getString("CC_SEDE"));
                    }
                    rs2.close();
                }

                if (strTipoDoc.equals("FACTURA")) {
                    if (!rs.getString("FAC_FECHA_COBRO").equals("")) {
                        strFechaCobro = fec.FormateaDDMMAAAA(rs.getString("FAC_FECHA_COBRO"), "/");
                    }
                    strXML += "<datos "
                            + " TIPOCURSO = \"" + rs.getString("TipoCurso") + "\"  "
                            + " FAC_ID = \"" + rs.getString("FAC_ID") + "\"  "
                            + " CURSO = \"" + utilXML.Sustituye(strCurso) + "\"  "
                            + " CURSO_ID = \"" + rs.getString("CC_CURSO_ID") + "\"  "
                            + " CURSO_FECHA = \"" + (!strFechaCurso.equals("") ? fec.FormateaDDMMAAAA(strFechaCurso, "/") : "") + "\"  "
                            + " LIMITE = \"" + strLimite + "\"  "
                            + " OCUPADOS = \"" + intOcupado[0] + "\"  "
                            + " COSTO_UNIT = \"" + rs.getString("PRECIOUNIT") + "\"  "
                            + " ARCHIVO = \"" + rs.getString("FAC_NOMPAGO") + "\"  "
                            + " COSTO_SUB = \"" + rs.getString("FAC_IMPUESTO1") + "\"  "
                            + " COSTO_TOT = \"" + rs.getString("FAC_TOTAL") + "\"  "
                            + " IVA = \"" + rs.getString("FAC_IMPUESTO1") + "\"  "
                            + " PORDESC = \"" + rs.getString("PORDESC") + "\"  "
                            + " DESCUENTO = \"" + rs.getString("FAC_DESCUENTO") + "\"  "
                            + " METPAGO = \"" + rs.getString("FAC_METODODEPAGO") + "\"  "
                            + " FECHA_PAGO = \"" + strFechaCobro + "\"  "
                            + " MAT_IMP = \"" + rs.getString("REQ_MATERIAL") + "\"  "
                            + " CANTIDAD = \"" + rs.getString("CANTIDAD") + "\"  "
                            + " COMENTARIO = \"" + utilXML.Sustituye(rs.getString("FAC_NOTASPIE")) + "\"  "
                            + " tipodocumento = \"" + "0" + "\"  "
                            + " DIGITO = \"" + utilXML.Sustituye(rs.getString("FAC_REFERENCIA")) + "\"  "
                            + " HORARIO = \"" + utilXML.Sustituye(strHorario) + "\"  "
                            + " SEDE = \"" + utilXML.Sustituye(strSede) + "\"  "
                            + " />";
                } else {
                    if (!rs.getString("TKT_FECHA_COBRO").equals("")) {
                        strFechaCobro = fec.FormateaDDMMAAAA(rs.getString("TKT_FECHA_COBRO"), "/");
                    }
                    strReservacion = rs.getString("TKT_ES_RESERVACION");
                    if (strReservacion.equals("1")) {
                        strTipoDocumento = "2"; //2 =  reservacion
                    } else {
                        strTipoDocumento = "1"; //1 = tkt
                    }

                    strXML += "<datos "
                            + " TIPOCURSO = \"" + rs.getString("TipoCurso") + "\"  "
                            + " FAC_ID = \"" + rs.getString("TKT_ID") + "\"  "
                            + " CURSO = \"" + utilXML.Sustituye(strCurso) + "\"  "
                            + " CURSO_ID = \"" + rs.getString("CC_CURSO_ID") + "\"  "
                            + " CURSO_FECHA = \"" + (!strFechaCurso.equals("") ? fec.FormateaDDMMAAAA(strFechaCurso, "/") : "") + "\"  "
                            + " LIMITE = \"" + strLimite + "\"  "
                            + " OCUPADOS = \"" + intOcupado[0] + "\"  "
                            + " COSTO_UNIT = \"" + rs.getString("PRECIOUNIT") + "\"  "
                            + " ARCHIVO = \"" + rs.getString("TKT_NOMPAGO") + "\"  "
                            + " COSTO_SUB = \"" + rs.getString("TKT_IMPUESTO1") + "\"  "
                            + " COSTO_TOT = \"" + rs.getString("TKT_TOTAL") + "\"  "
                            + " IVA = \"" + rs.getString("TKT_IMPUESTO1") + "\"  "
                            + " DESCUENTO = \"" + rs.getString("TKT_DESCUENTO") + "\"  "
                            + " PORDESC = \"" + rs.getString("PORDESC") + "\"  "
                            + " METPAGO = \"" + rs.getString("TKT_METODODEPAGO") + "\"  "
                            + " FECHA_PAGO = \"" + strFechaCobro + "\"  "
                            + " MAT_IMP = \"" + rs.getString("REQ_MATERIAL") + "\"  "
                            + " CANTIDAD = \"" + rs.getString("CANTIDAD") + "\"  "
                            + " COMENTARIO = \"" + utilXML.Sustituye(rs.getString("TKT_NOTASPIE")) + "\"  "
                            + " tipodocumento = \"" + strTipoDocumento + "\"  "
                            + " DIGITO = \"" + utilXML.Sustituye(rs.getString("TKT_REFERENCIA")) + "\"  "
                            + " HORARIO = \"" + utilXML.Sustituye(strHorario) + "\"  "
                            + " SEDE = \"" + utilXML.Sustituye(strSede) + "\"  "
                            + " />";
                }

            }
            strXML += "</Ventas>";
            rs.close();
            strXML.toString();
            out.clearBuffer();//Limpiamos buffer
            atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
            out.println(strXML);//Pintamos el resultado
        } //4
        if (strid.equals("5")) { //monitoreo a los agentes
            StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
            strXML.append("<vta>");
            //String strIdCTE = request.getParameter("CT_ID");
            String strID_FAC_TKT = request.getParameter("strFac_Tkt");
            String strTipoDoc = request.getParameter("strTipoDoc");
            String strPrefDoc = "";
            if (strTipoDoc.equals("TICKET") || strTipoDoc.equals("RESERVACIÓN")) {
                strPrefDoc = "TKT";
            } else {
                strPrefDoc = "FAC";
            }
            String strCCO_ID = "";
            String strCCO_NOMBRE = "";
            String strCCO_APPAT = "";
            String strCCO_APMAT = "";
            String strCCO_TITULO = "";
            String strCCO_NOSOCIO = "";
            String strCCO_ASOC = "";
            String strCCO_CORREO = "";
            String strContacto_id = "";
            String strCT_ID = "";
            String strObservacion = "";
            String strMaterial = "";
            String strSqlCTE = "select * from cofide_participantes where CP_ESTATUS >= 1 and CP_" + strPrefDoc + "_ID =  " + strID_FAC_TKT;
            try {
                ResultSet rsCTE = oConn.runQuery(strSqlCTE, true);
                while (rsCTE.next()) {
                    strCCO_ID = rsCTE.getString("CP_ID");
                    strCCO_NOMBRE = rsCTE.getString("CP_NOMBRE");
                    strCCO_APPAT = rsCTE.getString("CP_APPAT");
                    strCCO_APMAT = rsCTE.getString("CP_APMAT");
                    strCCO_TITULO = rsCTE.getString("CP_TITULO");
                    strCCO_NOSOCIO = rsCTE.getString("CCO_NOSOCIO");
                    strCCO_ASOC = rsCTE.getString("CP_ASCOC");
                    strCCO_CORREO = rsCTE.getString("CP_CORREO");
                    strContacto_id = rsCTE.getString("CONTACTO_ID");
                    strCT_ID = rsCTE.getString("CT_ID");
                    strObservacion = rsCTE.getString("CP_OBSERVACIONES");
                    strMaterial = rsCTE.getString("CP_MATERIAL_IMPRESO");

                    strXML.append("<datos ");
                    strXML.append(" CCO_ID = \"").append(strCCO_ID).append("\"");
                    strXML.append(" CCO_NOMBRE = \"").append(strCCO_NOMBRE).append("\"");
                    strXML.append(" CCO_APPATERNO = \"").append(strCCO_APPAT).append("\"");
                    strXML.append(" CCO_APMATERNO = \"").append(strCCO_APMAT).append("\"");
                    strXML.append(" CCO_TITULO = \"").append(strCCO_TITULO).append("\"");
                    strXML.append(" CCO_NOSOCIO = \"").append(strCCO_NOSOCIO).append("\"");
                    strXML.append(" CCO_ASOCIACION = \"").append(strCCO_ASOC).append("\"");
                    strXML.append(" CCO_CORREO = \"").append(strCCO_CORREO).append("\"");
                    strXML.append(" CONTACTO_ID = \"").append(strContacto_id).append("\"");
                    strXML.append(" CT_ID = \"").append(strCT_ID).append("\"");
                    strXML.append(" CP_OBSERVACIONES = \"").append(utilXML.Sustituye(strObservacion)).append("\"");
                    strXML.append(" MATERIAL = \"").append(strMaterial.equals("1") ? "SI" : "NO").append("\"");
                    strXML.append(" />");
                }
                rsCTE.close();
            } catch (SQLException e) {
                System.out.println("ERROR AL OBTENER PARTICIPANTES" + e.getMessage());
            }
            strXML.append("</vta>");
            out.clearBuffer();//Limpiamos buffer
            atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
            out.println(strXML.toString());//Pintamos el resultado
        } //5
        //ACTUALIZA Y/O AGREGA INFORMACIÓN FISCAL DEL CLIENTE
        if (strid.equals("6")) { //obtenemos datos de facturacion
            String strXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
            strXML += "<vta>";
            int intIdCte = 0;
            int intDFA_ID = 0;
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
//            int DFA_ID = 0;
            String strID_FAC_TKT = request.getParameter("strFac_Tkt");
//            String strTipoDoc = request.getParameter("strTipoDoc");
            String strIdCte = "";

            if (strID_FAC_TKT.equals("")) {
                if (request.getParameter("ct_id") != null) {
                    if (!request.getParameter("ct_id").equals("")) {
                        strIdCte = request.getParameter("ct_id");
                    }
                }
                intIdCte = Integer.parseInt(strIdCte);
            } else {

                strSql = "select CT_ID from view_ventasglobales where FAC_ID = " + strID_FAC_TKT + " LIMIT 1";

                rs = oConn.runQuery(strSql, true);
                while (rs.next()) {
                    intIdCte = rs.getInt("CT_ID");
                }
                rs.close();

            }
            //ACTUALIZA LA INFORMACIÓN FISCAL DEL CLIENTE
            System.out.println("respuesta: " + tele.saveDatosFactura(oConn, varSesiones, request, intIdCte) + " okis");
            // obtenemos las razones sociales
            strSql = "select * "
                    + "from vta_cliente_facturacion "
                    + "where CT_ID = " + intIdCte + " "
                    + "group by DFA_RAZONSOCIAL, CT_ID, DFA_RFC, DFA_CALLE, DFA_COLONIA, DFA_MUNICIPIO, "
                    + "DFA_ESTADO, DFA_CP, DFA_TELEFONO, DFA_EMAIL, DFA_EMAI2, DFA_NUMERO, DFA_NUMINT";
            //String strSql = "select * from vta_cliente_facturacion where CT_ID = " + intIdCte;
            rs = oConn.runQuery(strSql, true);
            while (rs.next()) {

                intDFA_ID = rs.getInt("DFA_ID");
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

                strXML += "<datos "
                        + " CEV_ID = \"" + intDFA_ID + "\" "
                        + " CEV_NUMERO = \"" + strNCliente + "\" "
                        + " CEV_RFC = \"" + utilXML.Sustituye(strRfc) + "\" "
                        + " CEV_NOMBRE = \"" + utilXML.Sustituye(strNombre) + "\" "
                        + " CEV_CALLE = \"" + strCalle + "\" "
                        + " CEV_COLONIA = \"" + strColonia + "\" "
                        + " CEV_MUNICIPIO = \"" + strDelegacion + "\" "
                        + " CEV_ESTADO = \"" + strEstado + "\" "
                        + " CEV_CP = \"" + strCP + "\" "
                        + " CEV_TELEFONO = \"" + strTelefono + "\" "
                        + " CEV_NUMEROEXT = \"" + strNumExt + "\" "
                        + " CEV_NUMEROINT = \"" + strNumInt + "\" "
                        + " CEV_EMAIL1 = \"" + strCorreo1 + "\" "
                        + " CEV_EMAIL2 = \"" + strCorreo2 + "\" "
                        + " />";
            }
            rs.close();

            strXML += "</vta>";
            out.clearBuffer();//Limpiamos buffer
            atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
            out.println(strXML);//Pintamos el resultado
        } //6

        if (strid.equals("7")) { //autocompletar

            String strValorBuscar = request.getParameter("term");
            String strCompleteSql = "";
            System.out.println("JSP . . . . .  . . ." + strValorBuscar);
            //Declaramos objeto json
            JSONArray jsonChild = new JSONArray();
            if (strValorBuscar != "") {
                strSql = "select concat(CC_CURSO_ID,' / ',"
                        + "CC_HR_EVENTO_INI,' / ',"
                        + "substring(CC_FECHA_INICIAL,7,2),'-',"
                        + "substring(CC_FECHA_INICIAL,5,2),'-',"
                        + "substring(CC_FECHA_INICIAL,1,4),' / ',"
                        + "CSH_SEDE,' / ',"
                        + "CC_NOMBRE_CURSO) as CC_NOMBRE_CURSO "
                        + "from cofide_cursos LEFT JOIN cofide_sede_hotel on CC_SEDE_ID = CSH_ID "
                        + "where CC_NOMBRE_CURSO like '%" + strValorBuscar + "%' "
                        + "and CC_ACTIVO = 1 and CC_ESTATUS = 1 ";
                strSql += strCompleteSql;
                strSql += " order by CC_FECHA_INICIAL, CC_HR_EVENTO_INI";
                ResultSet rsCombo;
                System.out.println("QUERY : " + strSql);
                try {
                    rsCombo = oConn.runQuery(strSql, true);
                    while (rsCombo.next()) {
                        String strCurso1 = rsCombo.getString("cc_nombre_curso");
                        JSONObject objJson = new JSONObject();
                        objJson.put("value", strCurso1);
                        objJson.put("label", strCurso1);
                        jsonChild.put(objJson);
                    }
                    rsCombo.close();
                } catch (SQLException ex) {
                    ex.fillInStackTrace();
                }
            }
            out.clearBuffer();//Limpiamos buffer
            atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
            out.println(jsonChild.toString());//Pintamos el resultado
        } //fin 7

    } else {
        out.print("SIN ACCESO");
    }
    oConn.close();
%>
<%!
    public String getTktFolio(String strTKT_ID, Conexion oConn) {
        String strFolio = "";
        String strQuery = "select TKT_FOLIO from vta_tickets where TKT_ID = " + strTKT_ID;
        try {
            ResultSet rs = oConn.runQuery(strQuery);
            while (rs.next()) {
                strFolio = rs.getString("TKT_FOLIO");
            }
        } catch (SQLException ex) {
            ex.fillInStackTrace();
        }
        return strFolio;
    }

    public String strGetFolioSql(Conexion oConn, String strFolio) {
        Folios folio = new Folios();
        int intNumDigitos = folio.getIntNumDigitos();
        String strTmpFolio = "";
        int intTmp = 0;
        intTmp = intNumDigitos - strFolio.length();
        for (int i = 0; i < intTmp; i++) {
            strTmpFolio += "0";
        }
        String strFinalFolio = strTmpFolio + strFolio;
        return strFinalFolio;
    }

%>