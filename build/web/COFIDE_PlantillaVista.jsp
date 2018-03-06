<%-- 
    Document   : COFIDE_PlantillaVista
    Created on : 27/02/2017, 01:28:29 PM
    Author     : Desarrollo_COFIDE
--%>

<%@page import="comSIWeb.Utilerias.Fechas"%>
<%@page import="comSIWeb.ContextoApt.VariableSession"%>
<%@page import="comSIWeb.Operaciones.Conexion"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript" src="javascript/cofide_programacionmails.js"></script> <!-- importar archivo javascript--> 
        <link href="images/cofide.ico" rel="shortcut icon" type="image/vnd.microsoft.icon" />
        <title>Vista previa de la plantilla</title>
    </head>
    <%
        VariableSession varSesiones = new VariableSession(request);
        varSesiones.getVars();
        //Abrimos la conexion
        Conexion oConn = new Conexion(varSesiones.getStrUser(), this.getServletContext());
        oConn.open();
        Fechas fec = new Fechas();
        String strPlantilla = request.getParameter("plantilla");
        
    %>
    <body>
        <div id="BTNCLOSE">
            <input type="button" value="Cerrar">
        </div>
        <div id="TEMPLATE">
            
        </div>
    </body>
</html>
