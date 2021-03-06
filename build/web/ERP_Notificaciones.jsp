<%-- 
    Document   : ERP_Notificaciones
    Created on : 11-feb-2015, 12:57:31
    Author     : ZeusGalindo
--%>
<%@page import="comSIWeb.Utilerias.Fechas"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="comSIWeb.Operaciones.Conexion"%>
<%@page import="comSIWeb.ContextoApt.Seguridad"%>
<%@page import="comSIWeb.ContextoApt.VariableSession"%>
<%@page import="comSIWeb.ContextoApt.atrJSP"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<style>
    table{
        font-size: 14px;
    }
    #tablanotificacion{
        overflow: auto;
        height: 450px;
    }
</style>
<%
    /*Atributos generales de la pagina*/
    atrJSP.atrJSP(request, response, true, false);
    /*Obtenemos las variables de sesion*/
    VariableSession varSesiones = new VariableSession(request);
    varSesiones.getVars();
    Seguridad seg = new Seguridad();
    //Abrimos la conexion
    Conexion oConn = new Conexion(varSesiones.getStrUser(), this.getServletContext());
    oConn.open();
    if (varSesiones.getIntNoUser() != 0 && seg.ValidaURL(request)) {
        Fechas fecha = new Fechas();
        String strAgenda = "";
        String strNotas = "";
        String strSql = "";
        ResultSet rs;

        //agenda actual 
        strSql = "select count(*) as Agenda from crm_eventos where EV_ASIGNADO_A = " + varSesiones.getIntNoUser() + " and EV_ESTADO = 1 and EV_FECHA_INICIO = " + fecha.getFechaActual();
        rs = oConn.runQuery(strSql, true);
        while (rs.next()) {
            strAgenda = rs.getString("Agenda");
        }
        rs.close();
        //agenda rezagada
        strSql = "select count(*) as Rezagado from crm_eventos where EV_ASIGNADO_A = " + varSesiones.getIntNoUser() + " and EV_ESTADO = 1 and EV_FECHA_INICIO < " + fecha.getFechaActual();
        rs = oConn.runQuery(strSql, true);
        while (rs.next()) {
            strNotas = rs.getString("Rezagado");
        }
        rs.close();
%>
<div id="notificacionesMain" class="panel">
    <center>
        <h1>Mis notificaciones</h1>
        <div style="font-size: 20px">
            <table><tr><td><b>Agenda: <%=strAgenda%></b></td><td><b>Notas: <%=strNotas%></b></td></tr></table>
        </div>
    </center>
    <%
        //Listamos las citas del día de hoy
    %>
    <div id="tablanotificacion">
        <label style="padding-left: 5%; font-size: 18px"><b>Proximos eventos:</b></label>
        <center>
            <table border="0" cellpadding="10" class="table1" width="90%">
                <!--<tr><th>Proximos eventos:</th></tr>-->
                <tr>
                    <th><b>Fecha:</b></th>
                    <th><b>Hora:</b></th>
                    <th><b>Comentario:</b></th>
                    <th><b>Cliente:</b></th>
                </tr>
                <%          //obtener la hora actual
                    String strHora = fecha.getHoraActual();
                    //obtener fecha actual
                    String strFecha = fecha.getFechaActual();
                    //Eventos
                    //String strSql = "select  EV_FECHA_INICIO,EV_HORA_INICIO,EV_COMENTARIO,EV_CT_ID"
                    strSql = "select  EV_FECHA_INICIO,EV_HORA_INICIO,EV_ASUNTO,EV_CT_ID"
                            + ",(select c.CT_RAZONSOCIAL from vta_cliente as c where c.CT_ID = crm_eventos.EV_CT_ID) as NOMBRE_CLIENTE"
                            + " from crm_eventos where EV_FECHA_INICIO <= '" + strFecha + "'  "
                            + " and  EV_ASIGNADO_A = " + varSesiones.getIntNoUser() + " and EV_ESTADO = 1 "
                            + "order by EV_FECHA_INICIO desc ,EV_HORA_INICIO desc";
                    //+ " and  EV_ASIGNADO_A = " + varSesiones.getIntNoUser() + " order by EV_HORA_INICIO ";
                %>
                <tbody>
                    <%
                        rs = oConn.runQuery(strSql, true);
                        while (rs.next()) {
                    %>                
                    <tr>
                        <td><%=fecha.FormateaDDMMAAAA(rs.getString("EV_FECHA_INICIO"), "/")%></td>
                        <td><%=rs.getString("EV_HORA_INICIO")%></td>
                        <td><%=rs.getString("EV_ASUNTO")%></td>
                        <td><b><%=rs.getString("EV_CT_ID")%></b> &nbsp;&nbsp;-&nbsp;&nbsp; <%=(rs.getString("NOMBRE_CLIENTE") == null ? "" : rs.getString("NOMBRE_CLIENTE"))%></td>
                    </tr>
                    <%
                        }
                        rs.close();
                    %>
                </tbody>                                               
                <%         //Listamos las citas del día de hoy
                %>
            </table>
        </center>
    </div>

</div>

<%         }
    oConn.close();
%>