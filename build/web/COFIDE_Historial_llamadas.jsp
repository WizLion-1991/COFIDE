<%-- 
    Document   : COFIDE_Historial_llamadas
    Created on : 15-abr-2016, 11:32:04
    Author     : juliocesar
--%>
<%@page import="comSIWeb.Utilerias.Fechas"%>
<%@page import="comSIWeb.Operaciones.Conexion"%>
<%@page import="comSIWeb.ContextoApt.VariableSession"%>
<%@page import="java.sql.ResultSet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <link rel="stylesheet" type="text/css" href="css/CIP_Main.css" />
        <link rel="stylesheet" type="text/css" href="css/CIP_Cofide.css" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
        <title>COFIDE - HISTORIAL DE LLAMADAS</title>
        <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
            <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
            <script src="js/sepomex.js"></script> 
            <link rel="stylesheet" type="text/css" href="css/sepomex.css" media="screen" />
            <link href="http://192.168.2.246:9001/cofide/images/cofide.ico" rel="shortcut icon" type="image/vnd.microsoft.icon" />

            <style>
                thead{
                    background: #A9F5A9; /* For browsers that do not support gradients */
                    background: radial-gradient(#A9F5A9,#FFF); /* Standard syntax (must be last) */ 
                    /*background: radial-gradient(#A9F5A9,#A9F5A9,#A9F5A9,#FFF);  Standard syntax (must be last) */
                }
                th{
                    /*border:#000 solid 1px;*/
                    font-size: 20px;  
                    font-weight: bold;    
                    /*background: #A9F5A9;*/                    
                    /*background: linear-gradient(#FFF,#A9F5A9,#A9F5A9,#FFF);  Standard syntax (must be last) */
                }
                td{
                    /*border:#000 solid 1px;*/
                }
                tr:hover{
                    font-weight: bold;
                }
                .th-fecha{
                    width: 200px;
                    text-align:center;
                }
                .th-coment{
                    width: 400px;
                    text-align:center;
                }
                .th-coments{
                    width: 400px;
                    text-align:left;
                }
                .bordes{
                    /*border:#000 solid 1px;*/	
                }
                table{
                    width:80%;
                    text-align:center;
                    background-color: #fcfcfc;		                    
                }
                #contenedor{
                    width: 90%;
                    padding-top: 5px;
                }

                body{
                    background-color: #fbfff4;
                }
                #cabeza{
                    height:100px;
                    width: 100%;
                    background-color: black;                    
                    background: linear-gradient( #000,#000,#000, #FFF); /* Standard syntax (must be last) */
                }
                img{
                    width:20%;
                }
            </style>
    </head>


    <%
        VariableSession varSesiones = new VariableSession(request);
        varSesiones.getVars();
        //Abrimos la conexion
        Conexion oConn = new Conexion(varSesiones.getStrUser(), this.getServletContext());
        oConn.open();
        Fechas fec = new Fechas();
        if (request.getParameter("CT_ID") != null) {
    %>

    <body oncontextmenu='return false;' onmousedown='return false;' onselectstart='return false'>
        <div id="cabeza">
            <center>
                <img src="http://201.161.14.206:9001/cofide/images/logo_MR_Comp_Ver.png" />
            </center>
        </div>
        <center>
            <div id="contenedor">
                <table class="table table-hover bordes">
                    <thead>
                        <th class="th-fecha">FECHA DE LLAMADA</th>
                        <th class="th-fecha">HORA DE LLAMADA</th>
                        <th class="th-coment">COMENTAIO DE LA LLAMADA</th>
                        <th class="th-fecha">AGENTE</th>
                        <th class="th-fecha">PRÓXIMA LLAMADA</th>
                    </thead>
                    <tbody>
                        <%
                            String strIdCTE = request.getParameter("CT_ID").trim();
                            String strFecha = "";
                            String strFechaNext = "00000000";
                            String strHora = "";
                            String strComentario = "";
                            String strAgente = "";
                            String strCTE = "";
                            String strFormatFecha = "";
                            String strFormatFechaNext = "";

                            String strSql = "select CL_FECHA, CL_HORA,CL_ID,CL_ID_CLIENTE, CL_COMENTARIO, nombre_usuario, CL_USUARIO, "
                                    + "CL_PROXIMA_FECHA, CT_RAZONCOMERCIAL"
                                    + " from cofide_llamada LEFT JOIN usuarios on CL_USUARIO = id_usuarios"
                                    + " LEFT JOIN vta_cliente on CL_ID_CLIENTE = CT_ID"
                                    + " where CL_ID_CLIENTE =  " + strIdCTE + " and CL_COMPLETO = 1"
                                    + " order by CL_FECHA DESC,CL_HORA DESC, CL_PROXIMA_FECHA";

                            ResultSet rs = oConn.runQuery(strSql, true);
                            while (rs.next()) {
                                strFecha = rs.getString("CL_FECHA");
                                strFechaNext = rs.getString("CL_PROXIMA_FECHA");
                                if (!strFecha.equals("")) {
                                    strFormatFecha = fec.FormateaDDMMAAAA(strFecha, "/");
                                }
                                if (!strFechaNext.equals("")) {
                                    strFormatFechaNext = fec.FormateaDDMMAAAA(strFechaNext, "/");
                                }
                        %>

                        <tr>
                            <td class="th-fecha">&nbsp; <%=strFormatFecha%></td>
                            <td class="th-fecha">&nbsp; <%=rs.getString("CL_HORA")%> hrs.</td>
                            <td class="th-coments">&nbsp; <%=rs.getString("CL_COMENTARIO")%></td>
                            <td class="th-fecha">&nbsp; <%=(rs.getString("nombre_usuario") == null ? "-" : rs.getString("nombre_usuario"))%></td>
                            <td class="th-fecha"><%=strFormatFechaNext%></td>
                        </tr>
                        <%                }
                            rs.close();
                        %>
                    </tbody>
                </table>
                <!--<hr/>-->
            </div>
        </center>
        </center>
    </body>
    <%
        oConn.close();
    } else {
    %>

    <body style="background: #bdff76">
        <div style="height: 30%"></div>
        <div style="text-align: center; height: 40%; background: #000000; color: #bdff76">            
            <h2><p>NO<i>se ha encontrado el</i><br/>ID del cliente<br/><i>para mostrar su historial de llamadas</i></p></h2>
        </div>
        <div style="height: 30%"></div>
    </body>
    <%
        }
    %>