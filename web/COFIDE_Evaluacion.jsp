<%-- 
    Document   : COFIDE_Telemarketing
    Created on : 8/12/2015, 04:59:36 PM
    Author     : siweb
--%>



<%@page import="com.mx.siweb.erp.especiales.cofide.COFIDE_EvCurso"%>
<%@page import="comSIWeb.Utilerias.Fechas"%>
<%@page import="comSIWeb.Utilerias.Sesiones"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="comSIWeb.Operaciones.CIP_Tabla"%>
<%@page import="comSIWeb.Utilerias.UtilXml"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="comSIWeb.ContextoApt.atrJSP"%>
<%@page import="comSIWeb.ContextoApt.Seguridad"%>
<%@page import="comSIWeb.Operaciones.Conexion"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
    COFIDE_EvCurso ev = new COFIDE_EvCurso(oConn);
    if (varSesiones.getIntNoUser() != 0 && seg.ValidaURL(request)) {
        String strid = request.getParameter("ID");
        if (strid != null) {
            String strXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
            strXML += "<cofide_evaluacion>";
            if (strid.equals("1")) { //llenado de la hora
                UtilXml utilXML = new UtilXml();
                //Consulta la hora actual
                String strHora = fec.getHoraActual();
                int intUsr = varSesiones.getIntNoUser();
                String strGrupo = "";
                int intGrupo = 0;
                int intEvaluaciones = 0;
                boolean bolTeam = false;
                String strSql = "select US_GRUPO, CG_DESCRIPCION, CG_NUM_EVALUACION from usuarios "
                        + "inner join cofide_grupo_trabajo ON CG_ID = US_GRUPO "
                        + "where id_usuarios = " + intUsr;
                ResultSet rs = oConn.runQuery(strSql, true);
                while (rs.next()) {
                    bolTeam = true;
                    intGrupo = rs.getInt("US_GRUPO");
                    strGrupo = rs.getString("CG_DESCRIPCION");
                    intEvaluaciones = rs.getInt("CG_NUM_EVALUACION");
                }
                rs.close();
                if (!bolTeam) {
                    intEvaluaciones = 2;
                }
                //lo mandamos por xml al archivo js
                strXML += "<datos "
                        + " CE_HORAREG = \"" + strHora + "\"  "
                        + " US_GRUPO = \"" + intGrupo + "\" "
                        + " CG_DESCRIPCION = \"" + strGrupo + "\" "
                        + " CG_NUM_EVALUACION = \"" + intEvaluaciones + "\" "
                        + " />";
                strXML += "</cofide_evaluacion>";
                //Mostramos el resultado
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML);//Pintamos el resultado
            } //fin de la primera opcion

            if (strid.equals("2")) { //llena grid de ejecutivos
                String strGTrabajo = "";
                if (!request.getParameter("CE_ID_GTRABAJO").equals("0")) {
                    strGTrabajo = " US_GRUPO = " + request.getParameter("CE_ID_GTRABAJO") + " and ";
//                    strGTrabajo = request.getParameter("CE_ID_GTRABAJO");
                }
                int intEV = 0;
                if (request.getParameter("EV_PENDIENTES_TMP") != null) {
                    intEV = Integer.parseInt(request.getParameter("EV_PENDIENTES_TMP"));
                }
//                System.out.println("###############evaluaciones pendiente: " + intEV);
//                int Evaluacion_Pen = intEV;
                int intIdEjecutivo = 0;
                String strNombre = "";
                String strIP = "";
                String strExt = "";
                int intNumeroLlamadas = 0;
                int intNumEva = 0;
                String strBase = "";
                int intRegistro = 0;
                int intExp = 0;
                int intProsp = 0;
                int intNotas = 0;
                double dblReserva = 0.0;
                double dblCobrado = 0.0;
                double dblMeta = 0.0;
                double dblFacturado = 0.0;
                String strSql = "select id_usuarios, nombre_usuario, IP_ADDRESS, NUM_EXT, COFIDE_CODIGO "
                        + "from usuarios where " + strGTrabajo + " UsuarioActivo = 1 and IS_TMK = 1 and IS_SUPERVISOR = 0"
                        + " order by US_GRUPO, id_usuarios";
                ResultSet rs = oConn.runQuery(strSql, true);
                while (rs.next()) {
                    intIdEjecutivo = rs.getInt("id_usuarios");
                    strNombre = rs.getString("nombre_usuario");
                    strIP = rs.getString("IP_ADDRESS");
                    strExt = rs.getString("NUM_EXT");
                    strBase = rs.getString("COFIDE_CODIGO");
                    intNotas = ev.getNotas(intIdEjecutivo);
                    double[] dblVenta = ev.getFacturado_Vendido(intIdEjecutivo);
//                    dblReserva = ev.getReserva(intIdEjecutivo);
                    dblCobrado = ev.getCobro(intIdEjecutivo);
                    dblMeta = ev.getMeta(intIdEjecutivo);
//                    dblFacturado = ev.getFacturado(intIdEjecutivo);
                    dblReserva = dblVenta[1];
                    dblFacturado = dblVenta[0];
                    intNumeroLlamadas = ev.getLlamada(intIdEjecutivo); //numero de llamadas
                    int[] intCuantos = ev.getRegistros(strBase);
                    intProsp = intCuantos[0];
                    intExp = intCuantos[1];
                    intRegistro = intCuantos[2];
                    boolean bolTeam = false;// tiene equipo asignado
                    String strSql2 = "select count(CE_ID_USER) as cuantos from cofide_evaluacion "
                            + "where CE_ID_USER = " + intIdEjecutivo + " and CE_FECHAREV = '" + fec.getFechaActual() + "'";
                    ResultSet rs2 = oConn.runQuery(strSql2, true);
                    while (rs2.next()) {
                        bolTeam = true;
                        intNumEva = rs2.getInt("cuantos");
                    }
                    rs2.close();
//                    System.out.println("evaluaciones pendiente: del weon " + intNumEva);
                    int Evaluacion_Pen = 0;
                    if (bolTeam) {
                        Evaluacion_Pen = intEV - intNumEva;
                    } else {
                        Evaluacion_Pen = 1;
                    }
//                    System.out.println("evaluaciones pendiente: " + Evaluacion_Pen);
                    strXML += "<datos "
                            + " id_usuarios = \"" + intIdEjecutivo + "\"  "
                            + " nombre_usuario = \"" + strNombre + "\"  "
                            + " EVALUACIONES = \"" + Evaluacion_Pen + "\" "
                            + " IP_ADDRESS = \"" + strIP + "\" "
                            + " EXTENSION = \"" + strExt + "\" "
                            + " LLAMADA = \"" + intNumeroLlamadas + "\" "
                            + " BASE = \"" + strBase + "\" "
                            + " REGISTROS = \"" + intRegistro + "\" "
                            + " EXP = \"" + intExp + "\" "
                            + " PROS = \"" + intProsp + "\" "
                            + " NOTA = \"" + intNotas + "\" "
                            + " RESERVA = \"" + dblReserva + "\" "
                            + " COBRO = \"" + dblCobrado + "\" "
                            + " META = \"" + dblMeta + "\" "
                            + " FACTURA = \"" + dblFacturado + "\" "
                            + " />";
                }
                rs.close();
                strXML += "</cofide_evaluacion>";
                strXML.toString();
                rs.close();
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML);//Pintamos el resultado
            } //fin de segunda opcion
            if (strid.equals("3")) { //guarda evaluacion
                String strResp = "";
                String intId = request.getParameter("CE_ID_USER");
                String strUser = request.getParameter("EV_USER");
                String strFecha = fec.getFechaActual();//request.getParameter("EV_FECHA");
                String strEvPen = request.getParameter("EV_PEN");
                String strHora = fec.getHoraActual();//request.getParameter("CE_HORAREV");
                String strLlamadas = request.getParameter("CE_NUM_LLAMADA");
                String strPregunta1 = request.getParameter("CE_PREGUNTA1");
                String strPregunta2 = request.getParameter("CE_PREGUNTA2");
                String strPregunta3 = request.getParameter("CE_PREGUNTA3");
                String strPregunta4 = request.getParameter("CE_PREGUNTA4");
                String strPregunta5 = request.getParameter("CE_PREGUNTA5");
                String strPregunta6 = request.getParameter("CE_PREGUNTA6");
                String strPregunta7 = request.getParameter("CE_PREGUNTA7");
                String strPregunta8 = request.getParameter("CE_PREGUNTA8");
                String strPregunta9 = request.getParameter("CE_PREGUNTA9");
                String strPregunta10 = request.getParameter("CE_PREGUNTA10");
                String strCalif = request.getParameter("CE_CALIF");
//                System.out.println("############################calificación: \n" + strCalif);
                String strMsj = request.getParameter("CE_MSG");
                String strTMK = request.getParameter("CE_CAMP_TMK");
                String strRef = request.getParameter("CE_REF");
                String strInCompany = request.getParameter("CE_INCOMPANY");
                String strReg = request.getParameter("CE_REG_PEN");
                String strObs = request.getParameter("CE_OBSERVACION");
                String strIdGtrabajo = request.getParameter("CE_ID_GTRABAJO");
                String strGTrabajo = request.getParameter("CE_GTRABAJO");
                String srtSql = "INSERT INTO cofide_evaluacion (CE_ID_USER, CE_NOMBRE, CE_FECHAREV, CE_HORAREV, CE_NO_LLAMADAS, "
                        + "CE_PREGUNTA1, CE_PREGUNTA2, CE_PREGUNTA3, CE_PREGUNTA4, CE_PREGUNTA5, CE_PREGUNTA6, CE_PREGUNTA7, CE_PREGUNTA8, CE_PREGUNTA9, CE_PREGUNTA10,"
                        + "CE_MSGCOMPLETO, CE_CALIFICACION, CE_CAMP_TELEMARKETING, CE_REGPENDIENTE, CE_OBSERVACIONES, CE_ID_GTRABAJO, CE_GTRABAJO, CE_REF, CE_INCOMPANY) "
                        + "VALUES (" + intId + ", '" + strUser + "', '" + strFecha + "', '" + strHora + "', " + strLlamadas + ", "
                        + "" + strPregunta1 + ", " + strPregunta2 + ", " + strPregunta3 + ", " + strPregunta4 + ", " + strPregunta5 + ", " + strPregunta6 + ", " + strPregunta7 + ", " + strPregunta8 + ", " + strPregunta9 + ", " + strPregunta10 + ", "
                        + "" + strMsj + ", " + strCalif + ", " + strTMK + ", " + strReg + ", '" + strObs + "', " + strIdGtrabajo + ", '" + strGTrabajo + "', " + strRef + ", " + strInCompany + ")";
                oConn.runQueryLMD(srtSql);
                if (!oConn.isBolEsError()) {
                    strResp = "OK";
                }
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(strResp);//Pintamos el resultado
            } //fin de la 3ra ocion

            if (strid.equals("4")) {
                String strId_usr = request.getParameter("EV_USER");
                int intLlamadas = 0;
                int intUsuario = Integer.parseInt(strId_usr);
                intLlamadas = ev.getLlamada(intUsuario);
                strXML += "<datos "
                        + " llamadas = \"" + intLlamadas + "\"  "
                        + " fecha = \"" + fec.FormateaDDMMAAAA(fec.getFechaActual(), "/") + "\"  "
                        + " hora = \"" + fec.getHoraActual() + "\"  "
                        + " />";
                strXML += "</cofide_evaluacion>";
                strXML.toString();
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML);//Pintamos el resultado
            } //fin de la cuarta opcion
            if (strid.equals("5")) {

            } //fin 5
        } //fin if strid != null
    }
    oConn.close();
%>