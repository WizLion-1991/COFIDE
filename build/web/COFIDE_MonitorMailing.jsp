<%-- 
    Document   : COFIDE_MonitorMailing
    Created on : 29/09/2017, 01:33:23 PM
    Author     : Desarrollo_COFIDE
--%>

<%@page import="comSIWeb.ContextoApt.atrJSP"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="comSIWeb.Utilerias.Fechas"%>
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
    Seguridad seg = new Seguridad();//Valida que la peticion se halla hecho desde el mismo sitio
    UtilXml util = new UtilXml();
    Fechas fec = new Fechas();
    StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
    strXML.append("<vta>");
    String strSQL = "";
    ResultSet rs;
    if (varSesiones.getIntNoUser() != 0 && seg.ValidaURL(request)) {
        String strid = request.getParameter("ID");
        if (strid != null) {
            if (strid.equals("1")) {
                String strFechaIni = request.getParameter("fech_ini");
                String strFechaFin = request.getParameter("fech_fin");
                String strFecha = "";
                String strHora = "";
                String strPlantilla = "";
                String strAsunto = "";
                String strGiro = "";
                String strArea = "";
                String strSede = "";
                int intTotal = 0;
                int intEnviados = 0;
                int intError = 0;
                int intLeidos = 0;
                int intPendiente = 0;
                double dblTotal = 0.0;
                double dblEnviado = 0.0;
                double dblError = 0.0;
                double dblLeido = 0.0;
                String stridmasivo = "";
                strFechaIni = fec.FormateaBD(strFechaIni, "/");
                strFechaFin = fec.FormateaBD(strFechaFin, "/");
                strSQL = "select CRM_ID,CRM_FECHA, CRM_HORA, CRM_TEMPLATE, CRM_ASUNTO, CRM_GIRO,CRM_AREA,CRM_SEDE,"
                        + "(select CTT_DESC from cofide_tipo_template, crm_correos where CRC_ID_PLANTILLA = CTT_ID and CRC_ID = CRM_TEMPLATE) as PLANTILLA "
                        + "from crm_envio_masivo "
                        + "where CRM_FECHA BETWEEN '" + strFechaIni + "' and '" + strFechaFin + "' "
                        + "order by CRM_FECHA, CRM_HORA";
                rs = oConn.runQuery(strSQL, true);
                while (rs.next()) {

                    stridmasivo = rs.getString("CRM_ID");
                    strFecha = rs.getString("CRM_FECHA");
                    strHora = rs.getString("CRM_HORA");
                    strPlantilla = rs.getString("PLANTILLA");
                    strAsunto = rs.getString("CRM_ASUNTO");
                    strGiro = getSegmento(oConn, 1, rs.getString("CRM_GIRO"));
                    strArea = getSegmento(oConn, 2, rs.getString("CRM_AREA"));
                    strSede = getSegmento(oConn, 3, rs.getString("CRM_SEDE"));
                    int[] intMasivoDetaArr = getMasivoDeta(oConn, rs.getString("CRM_ID"));
                    intTotal = intMasivoDetaArr[0];
                    intEnviados = intMasivoDetaArr[1];
                    intPendiente = intMasivoDetaArr[2];
                    if (intPendiente == 0) {
                        intError = intTotal - intEnviados;
                    } else {
                        intError = 0;
                    }
                    intLeidos = getMasivoDetaLeido(oConn, rs.getString("CRM_ID"));

                    if (intTotal == 0) {
                        intTotal = 1;
                    }
                    dblTotal = (intTotal / intTotal) * 100;
                    dblEnviado = (intEnviados / intTotal) * 100;
                    dblError = (intError / intTotal) * 100;
                    dblLeido = (intLeidos / intTotal) * 100;

                    strFecha = fec.FormateaDDMMAAAA(strFecha, "/");

                    strXML.append("<datos");
                    strXML.append(" idmasivo = \"").append(stridmasivo).append("\"");
                    strXML.append(" fecha = \"").append(strFecha).append("\"");
                    strXML.append(" hora = \"").append(strHora).append("\"");
                    strXML.append(" plantilla = \"").append(strPlantilla).append("\"");
                    strXML.append(" asunto = \"").append(util.Sustituye(strAsunto)).append("\"");
                    strXML.append(" giro = \"").append(strGiro).append("\"");
                    strXML.append(" area = \"").append(strArea).append("\"");
                    strXML.append(" sede = \"").append(strGiro).append("\"");
                    strXML.append(" total = \"").append(intTotal).append("\"");
                    strXML.append(" enviado = \"").append(intEnviados).append("\"");
                    strXML.append(" error = \"").append(intError).append("\"");
                    strXML.append(" leido = \"").append(intLeidos).append("\"");
                    strXML.append(" totalpor = \"").append(dblTotal).append("\"");
                    strXML.append(" enviadopor = \"").append(dblEnviado).append("\"");
                    strXML.append(" errorpor = \"").append(dblError).append("\"");
                    strXML.append(" leidopor = \"").append(dblLeido).append("\"");
                    strXML.append(" />");

                }
                strXML.append("</vta>");
                strXML.toString();
                rs.close();
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML);//Pintamos el resultado
            }

        }
    } //fin if strid != null    

    oConn.close();
%>

<%!
    String strQuery = "";
    ResultSet rs;

    public String getSegmento(Conexion oConn, int intOpcion, String strSegmentos) {
        String strSegmento = "";
        if (!strSegmentos.equals("")) {
            if (intOpcion == 1) { //
                strQuery = "select GROUP_CONCAT(CONCAT_WS(',',CG_GIRO)) as Segmento from cofide_giro where CG_ID_M in (" + strSegmentos + ");";

            }
            if (intOpcion == 2) {
                strQuery = "select GROUP_CONCAT(CONCAT_WS(',',CS_AREA)) as Segmento from cofide_segmento where CS_ID_M in (" + strSegmentos + ");";

            }
            if (intOpcion == 3) {
                strQuery = "select GROUP_CONCAT(CONCAT_WS(',',CS_SEDE)) as Segmento from cofide_sede where CS_SEDE_ID in (" + strSegmentos + ");";
            }
            try {
                rs = oConn.runQuery(strQuery, true);
                while (rs.next()) {
                    strSegmento = rs.getString("Segmento");
                }
                rs.close();
            } catch (SQLException sql) {
                System.out.println("Error al obtener el segmento de la opción: " + intOpcion + " [ " + sql.getMessage() + " ]");
            }
        }
        return strSegmento;
    }

//    public int getMasivoDeta(Conexion oConn, int intOpcion, String strIdMasivo) {
//        int intResultado = 0;
//        if (intOpcion == 1) { //total
//            strQuery = "select CRMD_EMAIL from crm_envio_masivo_deta where CRM_ID = " + strIdMasivo;
//        }
//        if (intOpcion == 2) { //enviados con exito
//            strQuery = "select CRMD_EMAIL from crm_envio_masivo_deta where CRM_ID = " + strIdMasivo + " and CRMD_ESTATUS = 'CORRECTO'";
//        }
//        if (intOpcion == 3) { //leidos
//            strQuery = "select MAIL_CORREO from cofide_mailing where MAIL_ID_MASIVO = " + strIdMasivo + " AND MAIL_FECHA_READ <> ''";
//        }
//        if (intOpcion == 4) { //ya se procesaron todos
//            strQuery = "select CRMD_EMAIL from crm_envio_masivo_deta where CRM_ID = " + strIdMasivo + " and CRM_PROCESADO = 0";
//        }
//        try {
//            rs = oConn.runQuery(strQuery, true);
//            while (rs.next()) {
//                intResultado++;
//            }
//            rs.close();
//        } catch (SQLException sql) {
//            System.out.println("Error al obtener el conteo de la opción: " + intOpcion + " [ " + sql.getMessage() + " ]");
//        }
//        return intResultado;
//    }
    public int[] getMasivoDeta(Conexion oConn, String strIdMasivo) {
        int[] intResultado = {0, 0, 0};
        int intTotal = 0, intEnviados = 0, intFaltante = 0;

        strQuery = "select CRMD_EMAIL,CRMD_ESTATUS,CRM_PROCESADO from crm_envio_masivo_deta where CRM_ID = " + strIdMasivo;

        try {
            rs = oConn.runQuery(strQuery, true);
            while (rs.next()) {
                intTotal++; //total de envios
                if (rs.getString("CRMD_ESTATUS").equals("CORRECTO")) {
                    intEnviados++;
                }
                if (rs.getString("CRM_PROCESADO").equals("0")) {
                    intFaltante++;
                }

            }
            rs.close();
        } catch (SQLException sql) {
            System.out.println("Error al obtener el conteo  [ " + sql.getMessage() + " ]");
        }

        intResultado[0] = intTotal;
        intResultado[1] = intEnviados;
        intResultado[2] = intFaltante;

        return intResultado;
    }

    public int getMasivoDetaLeido(Conexion oConn, String strIdMasivo) {
        int intResultado = 0;

        strQuery = "select MAIL_CORREO from cofide_mailing where MAIL_ID_MASIVO = " + strIdMasivo + " AND MAIL_FECHA_READ <> ''";

        try {
            rs = oConn.runQuery(strQuery, true);
            while (rs.next()) {
                intResultado++;
            }
            rs.close();
        } catch (SQLException sql) {
            System.out.println("Error al obtener el conteo de la opción: LEIDO [ " + sql.getMessage() + " ]");
        }
        return intResultado;
    }
%>

