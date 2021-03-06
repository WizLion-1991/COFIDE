<%-- 
    Document   : COFIDE_curso_activo
    Created on : 04-ene-2016, 18:52:52
    Author     : juliocesar
--%>

<%@page import="comSIWeb.Utilerias.Fechas"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="comSIWeb.Utilerias.UtilXml"%>
<%@page import="comSIWeb.ContextoApt.Seguridad"%>
<%@page import="comSIWeb.Operaciones.Conexion"%>
<%@page import="comSIWeb.ContextoApt.VariableSession"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<link rel="stylesheet" type="text/css" href="css/CIP_Main.css" />
<link rel="stylesheet" type="text/css" href="css/CIP_Cofide.css" />
<!-- grafica -->
<script src="https://code.highcharts.com/highcharts.js"></script>
<script src="https://code.highcharts.com/modules/exporting.js"></script>
<script type="text/javascript" src="javascript/cofide_curso_activo.js"></script> <!-- importar archivo javascript--> 
<!-- script de grafica -->

<%
    /*Obtenemos las variables de sesion*/
    VariableSession varSesiones = new VariableSession(request);
    varSesiones.getVars();
    //Abrimos la conexion
    Conexion oConn = new Conexion(varSesiones.getStrUser(), this.getServletContext());
    oConn.open();
    //Instanciamos objeto de seguridad para no permitir el acceso externo a ver los XML
//    Seguridad seg = new Seguridad();//Valida que la peticion se halla hecho desde el mismo sitio
    UtilXml util = new UtilXml();
    Fechas fec = new Fechas();
    String strFecha = "";
    String strCurso = "";
    String strInstructor = "";
    String strHrIni = "";
    String strHrFin = "";
    String strDuracion = "";
    String strSede = "";
    String strPresencial = "";
    String strPrecioPre = "";
    String strIvaPres = "";
    String strOnLine = "";
    String strPrecioOn = "";
    String strIvaOn = "";
    String strDia = "";
    String strMes = "";
    String MesLetra = "";
    String strAño = "";
    String strDisponible = "";
    String strVendidos = "";
    String Sede = request.getParameter("CACT_SEDE2");
    String FechaActual = fec.getFechaActual();
    String strSql = "select *, (CC_MONTAJE - CC_INSCRITOS) as DISPONIBLES from cofide_cursos where CC_SEDE_ID = " + Sede + " and CC_FECHA_INICIAL >= " + FechaActual + " order by CC_FECHA_INICIAL";
    ResultSet rs = oConn.runQuery(strSql, true);
    while (rs.next()) {
        strFecha = rs.getString("CC_FECHA_INICIAL");
        strDia = strFecha.substring(6, 8);
        strMes = strFecha.substring(4, 6);
        int intMes = Integer.parseInt(strMes);
        strAño = strFecha.substring(0, 4);
        if (intMes == 1) {
            MesLetra = "ENERO";
        }
        if (intMes == 2) {
            MesLetra = "MARZO";
        }
        if (intMes == 3) {
            MesLetra = "MARZO";
        }
        if (intMes == 4) {
            MesLetra = "ABRIL";
        }
        if (intMes == 5) {
            MesLetra = "MAYO";
        }
        if (intMes == 6) {
            MesLetra = "JUN";
        }
        if (intMes == 7) {
            MesLetra = "JUL";
        }
        if (intMes == 8) {
            MesLetra = "AGOS";
        }
        if (intMes == 9) {
            MesLetra = "SEPT";
        }
        if (intMes == 10) {
            MesLetra = "OCT";
        }
        if (intMes == 11) {
            MesLetra = "NOV";
        }
        if (intMes == 12) {
            MesLetra = "DIC";
        }
        strCurso = rs.getString("CC_NOMBRE_CURSO");
        strInstructor = rs.getString("CC_INSTRUCTOR");
        strHrIni = rs.getString("CC_HR_EVENTO_INI");
        strHrFin = rs.getString("CC_HR_EVENTO_FIN");
        strDuracion = rs.getString("CC_DURACION_HRS");
        strSede = rs.getString("CC_SEDE");
        strPresencial = rs.getString("CC_IS_PRESENCIAL");
        int Presen = Integer.parseInt(strPresencial);
        double dblIvaPrecio = 0.0;
        double dblIvaPrecioOn = 0.0;
        if (Presen > 0) {
            strPresencial = "PRESENCIAL";
            strPrecioPre = rs.getString("CC_PRECIO_PRES");
            dblIvaPrecio = Double.parseDouble(rs.getString("CC_IVA_PRES"));
            strIvaPres = String.format("%3.2f", dblIvaPrecio).replace(".00", "");
        } else {
            strPresencial = "NO DISPONIBLE";
            strPrecioPre = "";
            strIvaPres = "";
        }

        strOnLine = rs.getString("CC_IS_ONLINE");
        int Online = Integer.parseInt(strOnLine);
        if (Online > 0) {
            strOnLine = "ON LINE";
            strPrecioOn = rs.getString("CC_PRECIO_ON");
            dblIvaPrecioOn = Double.parseDouble(rs.getString("CC_IVA_ON"));
            strIvaOn = String.format("%3.2f", dblIvaPrecioOn).replace(".00", "");
        } else {
            strOnLine = "ONLINE NO DISPONIBLE";
            strPrecioOn = "";
            strIvaOn = "";
        }
        int intCursoId = rs.getInt("CC_CURSO_ID"); //id del curso
        strVendidos = rs.getString("CC_INSCRITOS");
        strDisponible = rs.getString("DISPONIBLES");
%>
<body onload="DrawGraphic(<%=intCursoId%>,<%=strDisponible%>,<%=strVendidos%>)">
<center>
    <table width="80%" cellspacing="0" cellpadding=30" border="5">
        <td width="5%" valign="middle">
            <table width="30" cellspacing="0" cellpadding="0" border="0">
                <tr>
                    <td class="txt00 txt04a" align="center"><%="DIA" + " " + strDia%></td>
                </tr>
                <tr>
                    <td class="txt00 txt04 decora02" align="center" colspan="2"><%=MesLetra + " ," + strAño%></td>
                </tr>
            </table>
        </td>
        <td width="22%">
            <table width="100%" cellspacing="0" cellpadding="0" border="0">
                <tr>
                    <td class="curso decora02"><%=strCurso%></td>
                </tr>
                <tr>
                    <td>
                        <table width="100%" cellspacing="0" cellpadding="0" border="0">
                            <tbody>
                                <tr>
                                    <td class="detalle decora02"><%=strInstructor%></td>
                                </tr>
                                <tr>
                                    <td class="detalle txt04">
                                        Sesion: <%=strHrIni + " A " + strHrFin + " HRS. || DURACION: " + strDuracion + " HRS. || "%>
                                        <br>
                                        <%=strSede%>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </td>
                </tr>
            </table>
        </td>
        <td width="7%" valign="middle" align="right">
            <table width="100%" cellspacing="0" cellpadding="0" border="0">
                <tr>
                    <td class="detalle decora02"><%=strPresencial%></td>
                </tr>
                <tr>
                    <td class="detalle decora02"><%="$ " + strPrecioPre%></td>
                </tr>
                <tr>
                    <td class="detalle decora02"><%="+ IVA $ " + strIvaPres%></td>
                </tr>
                <tr>
                    <td class="detalle decora02"><%=strOnLine%></td>
                </tr>
                <tr>
                    <td class="detalle decora02"><%="$ " + strIvaOn%></td>
                </tr>
                <tr>
                    <td class="detalle decora02"><%="+ IVA $ " + strIvaOn%></td>
                </tr>
            </table>
        </td>
        <td width="5%" valign="middle" align="right">
            <table>
                <tbody>
                    <tr>
                        <td class="detalleCurso decora02"> Disponibles: <%=strDisponible%> Lugares</td>
                    </tr>
                    <tr>
                        <td class="detalleCurso decora02"> Vendidos: <%=strVendidos%> Lugares </td>
                    </tr>
                </tbody>
            </table>
        </td>
        <td width="5%" valign="middle" align="right">
        <center>
            <!-- pinta la grafica con los parametros de la consulta -->
            <div class="ligaGrafica" onclick="DrawGraphicSede(<%=intCursoId%>,<%=strVendidos%>,<%=strDisponible%>)">Grafica</div>
            <div id="cofide_grafica_sede<%=intCursoId%>" style="width: 200px; height: 300"></div>
        </center>
        </td>
    </table>
</center>
<%                                    }
    rs.close();
%>
</body>