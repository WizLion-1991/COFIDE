<%-- 
    Document   : sepomex
    Created on : 21/12/2017, 12:06:43 PM
    Author     : Desarrollo_COFIDE
--%>

<%@page import="com.mx.siweb.erp.especiales.cofide.CRM_Envio_Template"%>
<%@page import="org.apache.commons.fileupload.FileItem"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="java.io.File"%>
<%@page import="org.apache.commons.fileupload.disk.DiskFileItemFactory"%>
<%@page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="comSIWeb.ContextoApt.atrJSP"%>
<%@page import="comSIWeb.Utilerias.Fechas"%>
<%@page import="comSIWeb.Operaciones.Conexion"%>
<%@page import="comSIWeb.ContextoApt.VariableSession"%>

<%
    /*Obtenemos las variables de sesion*/
    VariableSession varSesiones = new VariableSession(request);
    varSesiones.getVars();
    //Abrimos la conexion
    Conexion oConn = new Conexion(varSesiones.getStrUser(), this.getServletContext());
    oConn.open();
    Fechas fec = new Fechas();
    String strid = request.getParameter("ID");
    if (strid == null) {
        strid = "0";
    }
    String strRespuesta = "";
    String strSQL = "", strSQL2 = "", strSQL_LDM = "";
    ResultSet rs, rsx;

    if (strid != null) {

        if (strid.equals("1")) {
            //consulta el cp y lista resultados
            strSQL = "select CMX_CP, CMX_COLONIA, CMX_MUNICIPIO, CMX_ESTADO from cofide_sepomex where CMX_CP = '" + request.getParameter("cp") + "' order by CMX_COLONIA;";
            rs = oConn.runQuery(strSQL, true);
%>
<table class="table table-hover">
    <thead>
    <th style="width: 15px; color: orange;"><b>Codigo Postal</b></th>
    <th style="width: 55px; color: orange;"><b>Colonia</b></th>
    <th style="width: 55px; color: orange;"><b>Municipio</b></th>
    <th style="width: 35px; color: orange;"><b>Estado</b></th>
</thead>
<tbody>
    <%
        while (rs.next()) {

    %>
    <tr>
        <td><b><%=rs.getString("CMX_CP")%></b></td>
        <td><b><%=rs.getString("CMX_COLONIA")%></b></td>
        <td><b><%=rs.getString("CMX_MUNICIPIO")%></b></td>
        <td><b><%=rs.getString("CMX_ESTADO")%></b></td>
    </tr>

    <%        }
        rs.close();
    %>
</tbody>
</table>
<%
        }
        if (strid.equals("2")) {
            //guarda nueva colonia
            String strCp = request.getParameter("cp");
            String strColonia = request.getParameter("colonia");
            strRespuesta = "";

            strSQL = "select CMX_CP, CMX_COLONIA, CMX_MUNICIPIO, CMX_ESTADO, CMX_CIUDAD from cofide_sepomex where CMX_CP = '" + strCp + "' limit 1";
            rs = oConn.runQuery(strSQL, true);
            while (rs.next()) {
                strSQL2 = "INSERT INTO cofide_sepomex (CMX_CP, CMX_COLONIA, CMX_MUNICIPIO, CMX_ESTADO, CMX_CIUDAD) VALUES "
                        + "('" + strCp + "', "
                        + "'" + strColonia + "', "
                        + "'" + rs.getString("CMX_MUNICIPIO") + "', "
                        + "'" + rs.getString("CMX_ESTADO") + "', "
                        + "'" + rs.getString("CMX_CIUDAD") + "');";
                oConn.runQueryLMD(strSQL2);
                if (!oConn.isBolEsError()) {
                    strRespuesta = "OK";
                }
            }
            rs.close();
            out.clearBuffer();//Limpiamos buffer
            atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
            out.println(strRespuesta);//Pintamos el resultado
        }
        if (strid.equals("3")) {
            //guarda nueva colonia
            String strCp = request.getParameter("cp");
            String strColonia = request.getParameter("colonia");
            String strMunicipio = request.getParameter("municipio");
            String strEstado = request.getParameter("estado");
            strRespuesta = "";

            strSQL = "INSERT INTO cofide_sepomex (CMX_CP, CMX_COLONIA, CMX_MUNICIPIO, CMX_ESTADO, CMX_CIUDAD) VALUES "
                    + "('" + strCp + "', "
                    + "'" + strColonia + "', "
                    + "'" + strMunicipio + "', "
                    + "'" + strEstado + "', "
                    + "'" + strEstado + "');";
            oConn.runQueryLMD(strSQL);
            if (!oConn.isBolEsError()) {
                strRespuesta = "OK";
            }
            out.clearBuffer();//Limpiamos buffer
            atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
            out.println(strRespuesta);//Pintamos el resultado
        }
    }
%>