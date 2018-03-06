<%-- 
    Document   : COFIDE_Calendario_cursos
    Created on : 12-abr-2016, 13:09:57
    Author     : juliocesar
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="comSIWeb.Utilerias.Fechas"%>
<%@page import="comSIWeb.Utilerias.UtilXml"%>
<%@page import="comSIWeb.Operaciones.Conexion"%>
<%@page import="comSIWeb.ContextoApt.VariableSession"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Calendario de Cursos</title>
        <meta charset="utf-8">
        <link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
        <script src="//code.jquery.com/jquery-1.10.2.js"></script>
        <script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
        <script type="text/javascript" src="javascript/cofide_curso_activo.js"></script> <!-- importar archivo javascript--> 
        <link rel="stylesheet" type="text/css" href="css/CIP_Main.css" />
        <link rel="stylesheet" type="text/css" href="css/CIP_Cofide.css" />
        <link href="//maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="http://192.168.2.246:9001/cofide/images/cofide.ico" rel="shortcut icon" type="image/vnd.microsoft.icon" />
        <script>
            $(function () {
                $("#tabs").tabs();
            });

        </script>
        <style>
            .imagen{
                background-color: black;
                width: 100%;
                height: 20%;
            }

            .fondoMenu{
                background: gray;
            }
            .fondoMenu:hover{
                background: black;
            }
            .iconos{
                font-size: 5px;
            }
            #cabecero{
                position: fixed;
                min-height: 225px;
                margin-bottom: 10px;
                margin-right: 10px;
                overflow: hidden;
                text-align: center;
                width: 100%;
            }
            #contenido{
                padding-top: 20%;
            }
        </style>
    </head>
    <body style="font-family: Verdana, Geneva, serif;">
        <div>
            <div class="row">
                <div class="col-md-4 imagen">
                    <div style="padding-left: 40%">
                        <img src="images/cofide.png" style="width: 220px; padding-top: 3%">
                    </div>
                </div>
            </div>
        </div>
        <div style="background: url('images/cofide_bgn.png'); background-size: 100%">
            <div class="row" style="height: 15%">
                <div class="inline-block" style="text-align: center">
                    <H1><B>Calendario</B></H1>
                </div>
            </div>
            <div class="row">
                <%

                    Conexion oConn = new Conexion();
                    Fechas fec = new Fechas();
                    oConn.open();

                    String strSqlSedeOk = "";
                    String strSqlSedeOk2 = "";
                    ResultSet rs;
                    ResultSet rs2;
//                    strSqlSedeOk = "select distinct CC_ALIAS "
//                            + "from cofide_cursos "
//                            + "where CC_ACTIVO = 1 and CC_FECHA_INICIAL >= '" + fec.getFechaActual() + "' "
//                            + "and CC_ALIAS <> '' order by CC_ALIAS";
                    strSqlSedeOk = "select DISTINCT CSH_SEDE, CSH_COLOR "
                            + "from cofide_cursos, cofide_sede_hotel "
                            + "where CC_SEDE_ID = CSH_ID and CC_ACTIVO = 1 and CC_FECHA_INICIAL >= '" + fec.getFechaActual() + "' and CC_ALIAS <> '' "
                            + "order by CC_ALIAS";
                    rs = oConn.runQuery(strSqlSedeOk, true);
                    while (rs.next()) {
//                        strSqlSedeOk2 = "select CSH_ALIAS, CSH_COLOR "
//                                + "from cofide_sede_hotel "
//                                + "where csh_alias <> '0' and csh_alias = '" + rs.getString("CC_ALIAS") + "' "
//                                + "group by csh_alias;";
//                        rs2 = oConn.runQuery(strSqlSedeOk2, true);
//                        while (rs2.next()) {
                %>
                <div class="col-md-4" style="font-size: 11px"><i class="fa fa-circle iconos" style="color: <%=rs.getString("CSH_COLOR")%>"></i><%=rs.getString("CSH_SEDE")%></div>
                    <%                    }
//                            rs2.close();
//                        }
                        rs.close();
                    %>
            </div>
        </div>
        <div id="tabs" style="border: none; background: url('images/cofide_bgn.png'); background-size: 100%;">
            <ul style="background: transparent; border: none; padding-left: 4%;">
                <li><a href="#tabs-1" style="color: white" class="fondoMenu">Todos</a></li>
                <li><a href="#tabs-2" style="color: white" class="fondoMenu">Mes</a></li>
                <li><a href="#tabs-3" style="color: white" class="fondoMenu">Sede</a></li>
                <li><a href="#tabs-4" style="color: white" class="fondoMenu">Tipo</a></li>
                <li><a href="#tabs-5" style="color: white" class="fondoMenu">Diplomados y Seminarios</a></li>
                <li><a href="#tabs-6" style="color: white" class="fondoMenu">Búsqueda</a></li>
            </ul>
            <div id="tabs-1">
                <%@include  file = "COFIDE_CursoCalendario.jsp" %>
                <div id="CACT_TODO1"> </div>
            </div>
            <div id="tabs-2">
                <div id="CACT_MES2" style="padding-left: 4%">
                    <%                        String strNomMes = "";
                        String strNumMes = "";
                        //String strFechaActual = fec.getFechaActual();
                        String strSQLFecha = "SELECT CC_FECHA_INICIAL, "
                                + "SUBSTRING(CC_FECHA_INICIAL,1,4) as anio, SUBSTRING(CC_FECHA_INICIAL,5,2) as mes "
                                + "from cofide_cursos "
                                + "where CC_ACTIVO = 1 and CC_FECHA_INICIAL >= '" + fec.getFechaActual() + "' "
                                + "group by anio, mes order by anio, mes";
                        ResultSet rsFec = oConn.runQuery(strSQLFecha, true);
                        while (rsFec.next()) {

                            if (rsFec.getString("mes").equals("01")) {
                                strNomMes = "Enero";
                                strNumMes = "01";
                            }
                            if (rsFec.getString("mes").equals("02")) {
                                strNomMes = "Febrero";
                                strNumMes = "02";
                            }
                            if (rsFec.getString("mes").equals("03")) {
                                strNomMes = "Marzo";
                                strNumMes = "03";
                            }
                            if (rsFec.getString("mes").equals("04")) {
                                strNomMes = "Abril";
                                strNumMes = "04";
                            }
                            if (rsFec.getString("mes").equals("05")) {
                                strNomMes = "Mayo";
                                strNumMes = "05";
                            }
                            if (rsFec.getString("mes").equals("06")) {
                                strNomMes = "Junio";
                                strNumMes = "06";
                            }
                            if (rsFec.getString("mes").equals("07")) {
                                strNomMes = "Julio";
                                strNumMes = "07";
                            }
                            if (rsFec.getString("mes").equals("08")) {
                                strNomMes = "Agosto";
                                strNumMes = "08";
                            }
                            if (rsFec.getString("mes").equals("09")) {
                                strNomMes = "Septiembre";
                                strNumMes = "09";
                            }
                            if (rsFec.getString("mes").equals("10")) {
                                strNomMes = "Octubre";
                                strNumMes = "10";
                            }
                            if (rsFec.getString("mes").equals("11")) {
                                strNomMes = "Noviembre";
                                strNumMes = "11";
                            }
                            if (rsFec.getString("mes").equals("12")) {
                                strNomMes = "Diciembre";
                                strNumMes = "12";
                            }
                    %>
                    <input id="btnDip" class="btn btn-default btn-lg active" type="button" value="<%=strNomMes + " " + rsFec.getString("anio")%>" onclick="loadMesScreen(<%=strNumMes%>,<%=rsFec.getString("anio")%>)">
                    <%
                            //} // fin for
                        }
                        rs.close();
                    %>
                </div>
                <div id="CACT_MES1"> </div>
            </div>
            <div id="tabs-3">
                <div id="CACT_SEDE2" style="padding-left: 4%">
                    <%
                        //oConn.open();
                        String strSedes = "";
                        //String strIdSedes = "";
                        String strSqlSede = "select distinct CC_ALIAS from cofide_cursos where CC_ACTIVO = 1 and CC_FECHA_INICIAL >= '" + fec.getFechaActual() + "' "
                                + "and CC_ALIAS <> '' order by CC_ALIAS";
                        ResultSet rsSede = oConn.runQuery(strSqlSede, true);
                        while (rsSede.next()) {
                            //                        strIdSedes = rsSede.getString("CC_SEDE_ID");
                            strSedes = rsSede.getString("CC_ALIAS");
                    %>
                    <input id="btnSede" type="button" class="btn btn-default btn-lg active" value="<%=strSedes%>" onclick="loadSedeScreen(<%='\'' + strSedes + '\''%>)">
                    <%
                        }
                        rsSede.close();
                    %>
                </div>
                <div id="CACT_SEDE1"></div>
            </div>
            <div id="tabs-4">
                <div id="btnTipo" style="padding-left: 3%">
                    <input id="btnTipo" class="btn btn-default btn-lg active" type="button" value="PRESENCIAL" onclick="loadTipoScreen('CC_IS_PRESENCIAL=1')">
                    <input id="btnTipo" class="btn btn-default btn-lg active" type="button" value="ONLINE" onclick="loadTipoScreen('CC_IS_ONLINE=1')">
                    <input id="btnTipo" class="btn btn-default btn-lg active" type="button" value="VIDEO CURSOS" onclick="loadTipoScreen('CC_IS_VIDEO=1')">
                </div>
                <div id="CACT_TIPO1"> </div>
            </div>
            <div id="tabs-5">            
                <%--<jsp:include page="COFIDE_CursoDipSem.jsp"/>--%>
                <div id="btnTipo" style="padding-left: 3%">
                    <input id="btnTipo" class="btn btn-default btn-lg active" type="button" value="SEMINARIOS Y DIPLOMADOS" onclick="loadSeminarioDiplomadoScreen()">
                </div>
                <div id="CACT_SEMI1"> </div>
            </div>
            <div id="tabs-6">
                <div id="buscar" style="padding-left: 3%">
                    <input id="Buscar" type="text" width="50px" size="50">
                    <input id="btnBuscar" class="btn btn-default btn-lg active" type="button" value="Consultar Cursos" onclick="BuscarCurso()"></div>
                <br>
                <div id="CACT_CURSOS"> </div>
            </div>
        </div>
    </body>
</html>
<%
    oConn.close();
%>