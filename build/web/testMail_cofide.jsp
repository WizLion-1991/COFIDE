<%-- 
    Document   : testMail_cofide
    Created on : 18-jul-2016, 11:44:18
    Author     : juliochavez
--%>

<%@page import="comSIWeb.Utilerias.Fechas"%>
<%@page import="java.text.ParseException"%>
<%@page import="java.sql.SQLException"%>
<%@page import="comSIWeb.Operaciones.Conexion"%>
<%@page import="comSIWeb.ContextoApt.VariableSession"%>
<%@page import="com.mx.siweb.erp.especiales.cofide.CRM_Envio_Template"%>
<%@page import="javax.mail.PasswordAuthentication"%>
<%@page import="javax.mail.MessagingException"%>
<%@page import="javax.mail.Transport"%>
<%@page import="javax.activation.MailcapCommandMap"%>
<%@page import="javax.activation.CommandMap"%>
<%@page import="javax.mail.internet.InternetAddress"%>
<%@page import="javax.mail.internet.MimeMessage"%>
<%@page import="javax.mail.Message"%>
<%@page import="javax.mail.Session"%>
<%@page import="java.util.Properties"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    /*Obtenemos las variables de sesion*/
    VariableSession varSesiones = new VariableSession(request);
    varSesiones.getVars();
    //Abrimos la conexion
    Conexion oConn = new Conexion(varSesiones.getStrUser(), this.getServletContext());
    oConn.open();

    CRM_Envio_Template mail = new CRM_Envio_Template();
//    mail.MailDirecto(oConn, "julio.chavez@cofide.org", 154, 162);

%>
<%!
    CRM_Envio_Template mail = new CRM_Envio_Template();
    Fechas fec = new Fechas();

    public String envio(Conexion oConn, int i) {
        String stat = "";
        try {
            stat = mail.MailDirecto(oConn, "julio.chavez@cofide.org", 154, 162);
        } catch (SQLException sql) {
            System.out.println("error de sql: " + sql);
        } catch (ParseException parse) {
            System.out.println("error de parsear " + parse);
        }
        return fec.getHoraActualHHMMSS() + " numero de envio: " + i + " estatus del correo = " + stat;
    }
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Test Mail Cofide</title>
    </head>
    <body>
        <%            for (int i = 0; i < 100; i++) {
        %>
        <h1>test de correo: <%=envio(oConn, i)%></h1>
        <%
            }%>
    </body>
</html>
