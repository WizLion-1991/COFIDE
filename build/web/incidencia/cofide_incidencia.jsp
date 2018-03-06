<%-- 
    Document   : cofide_incidencia
    Created on : 15/02/2018, 10:53:18 AM
    Author     : Desarrollo_COFIDE
--%>

<%@page import="java.text.ParseException"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="com.mx.siweb.erp.especiales.cofide.entidades.CofideIncidenciaEnt"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.mx.siweb.erp.especiales.cofide.CofideIncidencia"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="comSIWeb.ContextoApt.atrJSP"%>
<%@page import="comSIWeb.Utilerias.Fechas"%>
<%@page import="comSIWeb.Utilerias.UtilXml"%>
<%@page import="comSIWeb.ContextoApt.Seguridad"%>
<%@page import="comSIWeb.Operaciones.Conexion"%>
<%@page import="comSIWeb.ContextoApt.VariableSession"%>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<%
    VariableSession varSesiones = new VariableSession(request);
    varSesiones.getVars();
    //Abrimos la conexion
    Conexion oConn = new Conexion(varSesiones.getStrUser(), this.getServletContext());
    oConn.open();
    //Instanciamos objeto de seguridad para no permitir el acceso externo a ver los XML
    Seguridad seg = new Seguridad();//Valida que la peticion se halla hecho desde el mismo sitio
    UtilXml util = new UtilXml();
    Fechas fec = new Fechas();
    CofideIncidencia incidencia = new CofideIncidencia();
    CofideIncidenciaEnt inc = new CofideIncidenciaEnt();
    StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
    strXML.append("<vta>");

//    if (varSesiones.getIntNoUser() != 0 && seg.ValidaURL(request)) {
    String strid = request.getParameter("ID");
    if (strid != null) {
        String strSQL = "", strRespuesta = "";
        ResultSet rs;
        if (strid.equals("1")) { //inicia sesión

            strRespuesta = "EL USUARIO NO EXISTE";
            String strUsuario = request.getParameter("usuario");
            String strPassword = request.getParameter("password");
            strSQL = "select * from cfd_incidencia_usuario where INC_US_USUARIO = '" + strUsuario.toLowerCase() + "' and INC_US_PASSWORD = '" + strPassword.toLowerCase() + "';";
            try {
                rs = oConn.runQuery(strSQL, true);
                while (rs.next()) {

                    if (rs.getString("INC_US_ESTATUS").equals("0")) {
                        strRespuesta = "EL USUARIO NO ESTA DISPONIBLE";
                    } else {
                        strRespuesta = "OK.";

                        strRespuesta += "<input type='text' name='idusuario' id='idusuario' value='" + rs.getString("INC_US_ID") + "'>";
                        strRespuesta += "<input type='text' name='nombre' id='nombre' value='" + rs.getString("INC_US_NOMBRE") + "'>";
                        strRespuesta += "<input type='text' name='usuario' id='usuario' value='" + rs.getString("INC_US_USUARIO") + "'>";
                        strRespuesta += "<input type='text' name='password' id='password' value='" + rs.getString("INC_US_PASSWORD") + "'>";
                        strRespuesta += "<input type='text' name='perfil' id='perfil' value='" + rs.getString("INC_US_PERFIL") + "'>";

                        incidencia.setStrIdUsuario(rs.getString("INC_US_ID"));
                        incidencia.setStrNombre(rs.getString("INC_US_NOMBRE"));
                        incidencia.setStrUsuario(rs.getString("INC_US_USUARIO"));
                        incidencia.setStrPassword(rs.getString("INC_US_PASSWORD"));
                        incidencia.setStrPerfil(rs.getString("INC_US_PERFIL"));
                    }

                }
                rs.close();
            } catch (SQLException sql) {
                strRespuesta = "Hubó un problema al iniciar sesión: " + sql.getMessage();
            }

            out.clearBuffer();//Limpiamos buffer
            atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML                
            out.println(strRespuesta);//Pintamos el resultado
        } //1
        if (strid.equals("2")) { //registro

        } //2
        if (strid.equals("3")) { //botones menu
            String strPerfil = request.getParameter("perfil");
            incidencia.setStrPerfil(strPerfil);
            strSQL = "select p.CIM_ID, m.CIM_DESCRIPCION from cfd_incidencia_permiso p, cfd_incidencia_menu m where p.CIM_ID = m.CIM_ID and INC_PF_ID = " + incidencia.getStrPerfil() + " order by p.CIM_ID;";
            rs = oConn.runQuery(strSQL, true);
            while (rs.next()) {
%>
<div>    
    <button class="button_menu" type="button" onclick="OpnMainSelection(<%=rs.getString("CIM_ID")%>)"><%=rs.getString("CIM_DESCRIPCION")%></button>
</div>
<%
            }
            rs.close();

        } //3
        if (strid.equals("4")) { //cursos sugeridos

            String strIdCurso = "0";
            String strCurso = "0";

            strSQL = "select CC_CURSO_ID,concat('ID: ',CC_CURSO_ID,' / ',"
                    + "getFormatDate(CC_FECHA_INICIAL),' / ',CC_HR_EVENTO_INI,' / ',"
                    + "(select CSH_SEDE from cofide_sede_hotel where CSH_ID = CC_SEDE_ID),' / ',"
                    + "SUBSTRING(CC_NOMBRE_CURSO,1,90),'...') as Curso "
                    + "from cofide_cursos where CC_ACTIVO = 1 and CC_FECHA_INICIAL <= REPLACE(CURDATE(),'-','') order by CC_FECHA_INICIAL DESC;";
            rs = oConn.runQuery(strSQL, true);
            strXML.append("<datos");
            strXML.append(" id_curso = \"").append("0").append("\"");
            strXML.append(" curso = \"").append(util.Sustituye("SELECCIONE UNA OPCIÓN")).append("\"");
            strXML.append(" />");
            while (rs.next()) {
                strIdCurso = rs.getString("CC_CURSO_ID");
                strCurso = rs.getString("Curso");
                strXML.append("<datos");
                strXML.append(" id_curso = \"").append(strIdCurso).append("\"");
                strXML.append(" curso = \"").append(util.Sustituye(strCurso)).append("\"");
                strXML.append(" />");
            }
            rs.close();

            strXML.append("</vta>");
            strXML.toString();
            out.clearBuffer();//Limpiamos buffer
            atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
            out.println(strXML);//Pintamos el resultado          
        }
        if (strid.equals("5")) { //cursos sugeridos

            String strIdCurso = "0";
            String strCurso = "0";

            strSQL = "select CIP_ID, CIP_DESCRIPCION from cfd_incidencia_problema order by CIP_DESCRIPCION;";
            rs = oConn.runQuery(strSQL, true);
            strXML.append("<datos");
            strXML.append(" id_curso = \"").append("0").append("\"");
            strXML.append(" curso = \"").append(util.Sustituye("SELECCIONE UNA OPCIÓN")).append("\"");
            strXML.append(" />");
            while (rs.next()) {
                strIdCurso = rs.getString("CIP_ID");
                strCurso = rs.getString("CIP_DESCRIPCION");
                strXML.append("<datos");
                strXML.append(" id_curso = \"").append(strIdCurso).append("\"");
                strXML.append(" curso = \"").append(util.Sustituye(strCurso)).append("\"");
                strXML.append(" />");
            }
            rs.close();

            strXML.append("</vta>");
            strXML.toString();
            out.clearBuffer();//Limpiamos buffer
            atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
            out.println(strXML);//Pintamos el resultado          
        }
        if (strid.equals("6")) { //cursos sugeridos

            String strIdCurso = "0";
            String strCurso = "0";

            strSQL = "select CMO_ID, CMO_DESCRIPCION from cfd_incidencia_modulo order by CMO_DESCRIPCION;";
            rs = oConn.runQuery(strSQL, true);
            strXML.append("<datos");
            strXML.append(" id_curso = \"").append("0").append("\"");
            strXML.append(" curso = \"").append(util.Sustituye("SELECCIONE UNA OPCIÓN")).append("\"");
            strXML.append(" />");
            while (rs.next()) {
                strIdCurso = rs.getString("CMO_ID");
                strCurso = rs.getString("CMO_DESCRIPCION");
                strXML.append("<datos");
                strXML.append(" id_curso = \"").append(strIdCurso).append("\"");
                strXML.append(" curso = \"").append(util.Sustituye(strCurso)).append("\"");
                strXML.append(" />");
            }
            rs.close();

            strXML.append("</vta>");
            strXML.toString();
            out.clearBuffer();//Limpiamos buffer
            atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
            out.println(strXML);//Pintamos el resultado          
        }
        if (strid.equals("7")) { //Alta Incidencia

            String strFechaAlta = fec.getFechaActual();
            String strHoraAlta = fec.getHoraActual();
            final String strCliente = URLDecoder.decode(new String(request.getParameter("cliente").getBytes("iso-8859-1")), "UTF-8");
            String strCorreo = request.getParameter("correo");
            String strUsuarioId = request.getParameter("usuario_id");
            String strCursoId = request.getParameter("cursoid");
            String strTipoProblema = request.getParameter("problema");
            String strModuloCRM = request.getParameter("modulo");
            String strOrigenProblema = request.getParameter("origen");
            String strStat = request.getParameter("estatus");
            final String strComentario = URLDecoder.decode(new String(request.getParameter("comentario").getBytes("iso-8859-1")), "UTF-8");
            final String strObservacion = URLDecoder.decode(new String(request.getParameter("observacion").getBytes("iso-8859-1")), "UTF-8");
            String strFechaFin = "-";
            String strHoraFin = "-";
            String strTTiempo = "-";
            strRespuesta = "";

            inc.setFieldString("CCI_FECHA_ALTA", strFechaAlta);
            inc.setFieldString("CCI_HORA_ALTA", strHoraAlta);
            inc.setFieldString("CCI_CLIENTE", strCliente);
            inc.setFieldString("CCI_CORREO", strCorreo);
            inc.setFieldString("CCI_CURSO_ID", strCursoId);
            inc.setFieldString("CCI_TIPO_PROBLEMA", strTipoProblema);
            inc.setFieldString("CCI_MODULO_CRM", strModuloCRM);
            inc.setFieldString("CCI_ORIGEN_PROBLEMA", strOrigenProblema);
            inc.setFieldString("CCI_ESTATUS", strStat);
            inc.setFieldString("CCI_COMENTARIO", strComentario);
            inc.setFieldString("CCI_OBSERVACION", strObservacion);
            inc.setFieldString("CCI_FECHA_TERMINO", strFechaFin);
            inc.setFieldString("CCI_HORA_TERMINO", strHoraFin);
            inc.setFieldString("CCI_TOTAL_TIEMPO", strTTiempo);
            inc.setFieldString("CCI_US_ALTA", strUsuarioId);
            strRespuesta = inc.Agrega(oConn);
            if (strRespuesta != "OK") {
                strRespuesta = "Hubó un problema al ingresar la incidencia. " + oConn.getStrMsgError();
            }
            out.clearBuffer();//Limpiamos buffer
            atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML                
            out.println(strRespuesta);//Pintamos el resultado
        }
        if (strid.equals("8")) { //Alta Incidencia
            String strEstatus = request.getParameter("estatus");
            int intIdIncidencia = Integer.parseInt(request.getParameter("incidencia"));
            strRespuesta = "OK";
            inc.ObtenDatos(intIdIncidencia, oConn);
            inc.setFieldString("CCI_ESTATUS", strEstatus);
            if (strEstatus.equals("1")) { //unicamente si, esta completo, poner fechas y hotas
                inc.setFieldString("CCI_FECHA_TERMINO", fec.getFechaActual());
                inc.setFieldString("CCI_HORA_TERMINO", fec.getHoraActual());
            }
            strRespuesta = inc.Modifica(oConn);
            if (!strRespuesta.equals("OK")) {
                strRespuesta = "ocurrio un problema al actualizar la incidencia: " + oConn.getStrMsgError();
            }
            out.clearBuffer();//Limpiamos buffer
            atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML                
            out.println(strRespuesta);//Pintamos el resultado
        }
        if (strid.equals("9")) { //Alta Incidencia
            String strIdIncidencia = request.getParameter("idincidencia");
            int intIdIncidencia = Integer.parseInt(strIdIncidencia);
            inc.ObtenDatos(intIdIncidencia, oConn);
            strXML.append("<datos");
            strXML.append(" idincidencia = \"").append(inc.getFieldString("CII_ID")).append("\"");
            strXML.append(" correo = \"").append(inc.getFieldString("CCI_CORREO")).append("\"");
            strXML.append(" curso = \"").append(inc.getFieldString("CCI_CURSO_ID")).append("\"");
            strXML.append(" origen = \"").append(inc.getFieldString("CCI_ORIGEN_PROBLEMA")).append("\"");
            strXML.append(" tipo = \"").append(inc.getFieldString("CCI_TIPO_PROBLEMA")).append("\"");
            strXML.append(" modulo = \"").append(inc.getFieldString("CCI_MODULO_CRM")).append("\"");
            strXML.append(" cliente = \"").append(util.Sustituye(inc.getFieldString("CCI_CLIENTE"))).append("\"");
            strXML.append(" comentario = \"").append(util.Sustituye(inc.getFieldString("CCI_COMENTARIO"))).append("\"");
            strXML.append(" observacion = \"").append(util.Sustituye(inc.getFieldString("CCI_OBSERVACION"))).append("\"");
            strXML.append(" />");
            strXML.append("</vta>");
            strXML.toString();
            out.clearBuffer();//Limpiamos buffer
            atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
            out.println(strXML);//Pintamos el resultado 
        }
        if (strid.equals("10")) {

            final String strCliente = URLDecoder.decode(new String(request.getParameter("cliente").getBytes("iso-8859-1")), "UTF-8");
            String strCorreo = request.getParameter("correo");
            int intIdIncidencia = Integer.parseInt(request.getParameter("incidencia_id"));
            String strCursoId = request.getParameter("cursoid");
            String strTipoProblema = request.getParameter("problema");
            String strModuloCRM = request.getParameter("modulo");
            String strOrigenProblema = request.getParameter("origen");
            String strStat = request.getParameter("estatus");
            final String strComentario = URLDecoder.decode(new String(request.getParameter("comentario").getBytes("iso-8859-1")), "UTF-8");
            final String strObservacion = URLDecoder.decode(new String(request.getParameter("observacion").getBytes("iso-8859-1")), "UTF-8");
            strRespuesta = "";
            String strTotalhrs = "-";

            inc.ObtenDatos(intIdIncidencia, oConn);

//            if (!inc.getFieldString("CCI_FECHA_TERMINO").equals("-")) {
//                strTotalhrs = getDiffTime(inc.getFieldString("CCI_FECHA_ALTA"), inc.getFieldString("CCI_HORA_ALTA"), fec.getFechaActual(), fec.getHoraActual());
//            }

            inc.setFieldString("CCI_CLIENTE", strCliente);
            inc.setFieldString("CCI_CORREO", strCorreo);
            inc.setFieldString("CCI_CURSO_ID", strCursoId);
            inc.setFieldString("CCI_TIPO_PROBLEMA", strTipoProblema);
            inc.setFieldString("CCI_MODULO_CRM", strModuloCRM);
            inc.setFieldString("CCI_ORIGEN_PROBLEMA", strOrigenProblema);
            inc.setFieldString("CCI_COMENTARIO", strComentario);
            inc.setFieldString("CCI_OBSERVACION", strObservacion);
//            inc.setFieldString("CCI_FECHA_TERMINO", fec.getFechaActual());
//            inc.setFieldString("CCI_HORA_TERMINO", fec.getHoraActual());
//            inc.setFieldString("CCI_TOTAL_TIEMPO", strTotalhrs);

            strRespuesta = inc.Modifica(oConn);

            if (strRespuesta != "OK") {
                strRespuesta = "Hubó un problema al ingresar la incidencia. " + oConn.getStrMsgError();
            }

            out.clearBuffer();//Limpiamos buffer
            atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML                
            out.println(strRespuesta);//Pintamos el resultado
        }
        if (strid.equals("11")) {
            String strIdUsuario = request.getParameter("idusuario");
            String strIdEstatus = request.getParameter("estatus");
            strRespuesta = "OK";
            strSQL = "UPDATE cfd_incidencia_usuario set INC_US_ESTATUS = '" + strIdEstatus + "' where INC_US_ID = '" + strIdUsuario + "'";
            oConn.runQueryLMD(strSQL);
            if (oConn.isBolEsError()) {
                strRespuesta = "Hubó un problema al actualizar el estatus del usuario: " + oConn.getStrMsgError();
            }
            out.clearBuffer();//Limpiamos buffer
            atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML                
            out.println(strRespuesta);//Pintamos el resultado
        }
        if (strid.equals("12")) {
            String stridprofile = "0";
            String strprofile = "0";

            strSQL = "select * from cfd_incidencia_perfil order by INC_PF_PERFIL";
            rs = oConn.runQuery(strSQL, true);
            strXML.append("<datos");
            strXML.append(" id_perfil = \"").append("0").append("\"");
            strXML.append(" perfil = \"").append(util.Sustituye("SELECCIONE UNA OPCIÓN")).append("\"");
            strXML.append(" />");
            while (rs.next()) {
                stridprofile = rs.getString("INC_PF_ID");
                strprofile = rs.getString("INC_PF_PERFIL");
                strXML.append("<datos");
                strXML.append(" id_perfil = \"").append(stridprofile).append("\"");
                strXML.append(" perfil = \"").append(util.Sustituye(strprofile)).append("\"");
                strXML.append(" />");
            }
            rs.close();

            strXML.append("</vta>");
            strXML.toString();
            out.clearBuffer();//Limpiamos buffer
            atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
            out.println(strXML);//Pintamos el resultado   

        }
        if (strid.equals("13")) {
            String stridUsuario = request.getParameter("idusuario");
            strSQL = "SELECT * FROM cfd_incidencia_usuario where INC_US_ID = " + stridUsuario;
            try {
                rs = oConn.runQuery(strSQL, true);
                while (rs.next()) {

                    strXML.append("<datos");
                    strXML.append(" nombre = \"").append(util.Sustituye(rs.getString("INC_US_NOMBRE"))).append("\"");
                    strXML.append(" usuario = \"").append(util.Sustituye(rs.getString("INC_US_USUARIO"))).append("\"");
                    strXML.append(" contrasenia = \"").append(util.Sustituye(rs.getString("INC_US_PASSWORD"))).append("\"");
                    strXML.append(" perfil = \"").append(rs.getString("INC_US_PERFIL")).append("\"");
                    strXML.append(" estatus = \"").append(rs.getString("INC_US_ESTATUS")).append("\"");
                    strXML.append(" />");

                }
                rs.close();
            } catch (SQLException sql) {
                System.out.println("Error al obtener datos del cliente: " + sql.getMessage());
            }
            strXML.append("</vta>");
            strXML.toString();
            out.clearBuffer();//Limpiamos buffer
            atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
            out.println(strXML);//Pintamos el resultado   
        }
        if (strid.equals("14")) {

            String strIdUsuario = request.getParameter("idusuario");
            String strNombre = request.getParameter("nombre");
            String strUsuario = request.getParameter("usuario");
            String strContrasenia = request.getParameter("contrasenia");
            String strPerfil = request.getParameter("perfil");
            String strEstatus = request.getParameter("estatus");

            strSQL = "UPDATE cfd_incidencia_usuario SET "
                    + "INC_US_NOMBRE='" + strNombre + "', "
                    + "INC_US_USUARIO='" + strUsuario + "', "
                    + "INC_US_PASSWORD='" + strContrasenia + "', "
                    + "INC_US_ESTATUS='" + strEstatus + "', "
                    + "INC_US_PERFIL='" + strPerfil + "' "
                    + "WHERE INC_US_ID= " + strIdUsuario;
            oConn.runQueryLMD(strSQL);
            strRespuesta = "OK";
            if (oConn.isBolEsError()) {
                strRespuesta = "Error al actualizar la información del usuario: " + oConn.getStrMsgError();
            }
            out.clearBuffer();//Limpiamos buffer
            atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
            out.println(strRespuesta);//Pintamos el resultado   
        }
        if (strid.equals("15")) {

            String strNombre = request.getParameter("usuario");
            String strUsuario = request.getParameter("nombre");
            String strPass = request.getParameter("pass");
            strSQL = "INSERT INTO cfd_incidencia_usuario (INC_US_NOMBRE, INC_US_USUARIO, INC_US_PASSWORD) VALUES ('" + strNombre + "', '" + strUsuario + "', '" + strPass + "');";
            strRespuesta = "OK";
            oConn.runQueryLMD(strSQL);
            if (oConn.isBolEsError()) {
                strRespuesta = "Hubó un problema al registrar un nuevo usuario: " + oConn.getStrMsgError();
            }
            out.clearBuffer();//Limpiamos buffer
            atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
            out.println(strRespuesta);//Pintamos el resultado   
        }

    } else {
        System.out.println("SIN ACCESO");
    }
//    }
%>

<%!
    Fechas fec = new Fechas();

    public String getDiffTime(String strFechaini, String strHrIni, String strFechaFin, String strHrFin) {

        String strDiferenciaTiempo = "";
        String strFechaini_ = "";
        String strFechaFin_ = "";

        strFechaini_ = fec.FormateaAAAAMMDD(strFechaini, "-");
        strFechaFin_ = fec.FormateaAAAAMMDD(strFechaFin, "-");

        strFechaini_ += " " + strHrIni + ":00";
        strFechaFin_ += " " + strHrFin + ":00";

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd H:m:s");
            Date fechaInicial = dateFormat.parse(strFechaini_);
            Date fechaFinal = dateFormat.parse(strFechaFin_);
            int diferencia = (int) ((fechaFinal.getTime() - fechaInicial.getTime()) / 1000);
            int dias = 0;
            int horas = 0;
            int minutos = 0;
            if (diferencia > 86400) {
                dias = (int) Math.floor(diferencia / 86400);
                diferencia = diferencia - (dias * 86400);
            }
            if (diferencia > 3600) {
                horas = (int) Math.floor(diferencia / 3600);
                diferencia = diferencia - (horas * 3600);
            }
            if (diferencia > 60) {
                minutos = (int) Math.floor(diferencia / 60);
                diferencia = diferencia - (minutos * 60);
            }
            System.out.println("Hay " + dias + " dias, " + horas + " horas, " + minutos + " minutos y " + diferencia + " segundos de diferencia");
            strDiferenciaTiempo = dias + " DÍAS, " + horas + ":" + minutos + ":" + diferencia + " HRS";
        } catch (ParseException p) {
            System.out.println("error al formatear la cadena a fecha: " + p.getMessage());
        }
        System.out.println("diferencia: " + strDiferenciaTiempo);
        return strDiferenciaTiempo;
    }
%>