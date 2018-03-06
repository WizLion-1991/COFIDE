<%-- 
    Document   : cofide_mailing_
    Created on : 6/11/2017, 10:35:26 AM
    Author     : Desarrollo_COFIDE
--%>

<%@page import="java.sql.ResultSet"%>
<%@page import="comSIWeb.Utilerias.Fechas"%>
<%@page import="comSIWeb.Operaciones.Conexion"%>
<%@page import="comSIWeb.ContextoApt.VariableSession"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Mailing COFIDE</title>
</head>

<%
    /*Obtenemos las variables de sesion*/
    VariableSession varSesiones = new VariableSession(request);
    varSesiones.getVars();
    //Abrimos la conexion
    Conexion oConn = new Conexion(varSesiones.getStrUser(), this.getServletContext());
    oConn.open();
    Fechas fec = new Fechas();
    ResultSet rs;
    String strSql = "";
    String strOpcion = "";
    String strCorreo = "";
    String strIdMasivo = "";
    String strIdCte = "";

    if (request.getParameter("opc") != null) {

        strOpcion = request.getParameter("opc");

    }

    if (request.getParameter("correo") != null) {

        strCorreo = request.getParameter("correo");

    }

    if (request.getParameter("idm") != null) {

        strIdMasivo = request.getParameter("idm");

    }

    if (request.getParameter("idcte") != null) {

        strIdCte = request.getParameter("idcte");

    }

    if (!strOpcion.equals("") && !strCorreo.equals("") && !strIdMasivo.equals("") && !strIdCte.equals("")) { //viene con parametros, cargara las opciones

        strSql = "select * "
                + "from cofide_mailing "
                + "where MAIL_CT_ID = '" + strIdCte + "' "
                + "and MAIL_ID_MASIVO = '" + strIdMasivo + "' "
                + "and MAIL_CORREO = '" + strCorreo + "' "
                + "and MAIL_FECHA_READ = '' and MAIL_HORA_READ = ''";
        rs = oConn.runQuery(strSql, true);
        while (rs.next()) {

            strSql = "UPDATE cofide_mailing "
                    + "SET MAIL_FECHA_READ = '" + fec.getFechaActual() + "', "
                    + "MAIL_HORA_READ = '" + fec.getHoraActual() + "' "
                    + "WHERE MAIL_CT_ID = '" + strIdCte + "' "
                    + "AND MAIL_ID_MASIVO= '" + strIdMasivo + "' "
                    + "AND MAIL_CORREO ='" + strCorreo + "'";
            oConn.runQueryLMD(strSql);

            if (oConn.isBolEsError()) {
                System.out.println("Error al actualizar la vista al mail: [ " + oConn.getStrMsgError() + " ] ");
            }

        }
        rs.close();

        if (strOpcion.equals("1")) { //logo COFIDE

            System.out.println("LOGO COFIDE");
            response.sendRedirect("http://192.168.2.246:9001/cofide/images/cofide_logo_small_1.png");

        }
        if (strOpcion.equals("2")) { //header COFIDE

            System.out.println("HEADER COFIDE");
            response.sendRedirect("http://192.168.2.246:9001/cofide/images/1-banner.png");

        }

    } else { //viene vacio, redirecciona a otra pagina

        if (strOpcion.equals("1")) { //logo COFIDE

            System.out.println("LOGO COFIDE");
            response.sendRedirect("http://192.168.2.246:9001/cofide/images/cofide_logo_small_1.png");

        }
        if (strOpcion.equals("2")) { //header COFIDE

            System.out.println("HEADER COFIDE");
            response.sendRedirect("http://192.168.2.246:9001/cofide/images/1-banner.png");

        }

    }


%>


