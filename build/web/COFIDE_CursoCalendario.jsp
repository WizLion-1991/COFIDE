<%-- 
    Document   : COFIDE_CursoCalendario
    Created on : 8/10/2016, 06:03:04 PM
    Author     : WizLion
--%>
<%@page import="com.mx.siweb.erp.especiales.cofide.Telemarketing"%>
<%@page import="com.mx.siweb.erp.especiales.cofide.COFIDE_Calendario"%>
<%@page import="java.util.Locale"%>
<html>
    <%@page import="java.util.Date"%>
    <%@page import="java.text.SimpleDateFormat"%>
    <%@page import="java.text.SimpleDateFormat"%>
    <%@page import="comSIWeb.Operaciones.Conexion"%>
    <%@page import="comSIWeb.ContextoApt.VariableSession"%>
    <%@page import="java.sql.ResultSet"%>
    <%@page import="comSIWeb.Utilerias.Fechas"%>
    <%@page contentType="text/html" pageEncoding="UTF-8"%>
    <!DOCTYPE html>
    <!-- 
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="css/CIP_Cofide.css" />
        <link href="//maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css" rel="stylesheet">
    </head>
    -->
    <style>
        .headTable{
            padding-top: 0%;
        }
        table{
            font-size: 12px;
            width: 95%;
            padding-left: 10%;
            text-align: left;
            border-collapse: collapse;
            margin: auto;
            background-color: #E6E6E6;
        }
        .table1{
            border: black 3px solid;
        }
        .tr1{
            border: black 3px solid;
        }
        .td1{
            border: black 3px solid;
            padding: 10px;
        }
        .fecha{
            color:green;
        }
        .inTable{
            text-align: center;
        }
        .Curso{
            width: 40%;
        }
        .FechaCurso{
            width: 9%;
            font-size: 18px;
        }
        .DetalleCurso{
            width: 20%;
            font-size: 9px;
        }
        .CursosDisp{
            width: 10%;
        }
        .iconos{
            font-size: 20px;
        }
    </style>
    <body style="font-family: Verdana, Geneva, serif;">
        <div style="overflow: scroll;  height:700px"> 
            <div class="headTable">
                <table class="table1">
                    <tr>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td style="text-align: center"><B>Disponibilidad</B></td>
                        <td style="text-align: center"><B>Pagos</B></td>
                        <td></td>
                    <tr>
                        <%
                            /*Obtenemos las variables de sesion*/
                            VariableSession varSesionesx = new VariableSession(request);
                            varSesionesx.getVars();
                            //Abrimos la conexion
                            Conexion oConnx = new Conexion(varSesionesx.getStrUser(), this.getServletContext());
                            oConnx.open();
                            Fechas fecx = new Fechas();
                            COFIDE_Calendario cal = new COFIDE_Calendario(oConnx);
                            Telemarketing tele_ = new Telemarketing();
                            SimpleDateFormat conver = new SimpleDateFormat("yyyyMMdd");
                            SimpleDateFormat formatDiaLetra = new SimpleDateFormat("E. ", new Locale("es", "MX")); //obtener día de la semana
                            SimpleDateFormat formatDia = new SimpleDateFormat("dd", new Locale("es", "MX")); //obtener día en numero
                            SimpleDateFormat formatMesAnio = new SimpleDateFormat("MMM,yyyy", new Locale("es", "MX")); //obtener mes y año
                            int intNFechas = 1;
                            String strFechaIni = "";

                            //valida si es tmk
                            String strIsTMK = "select IS_TMK from usuarios where IS_TMK = 1 and IS_SUPERVISOR = 0 and id_usuarios = " + varSesionesx.getIntNoUser();
                            ResultSet rsTMK = oConnx.runQuery(strIsTMK, true);
                            boolean bolIsTMK = false;
                            while (rsTMK.next()) {
                                bolIsTMK = true; //es telemarketing
                            }
                            rsTMK.close();
                            //valida si es tmk

                            //valida si es contabilidad
                            String strIsConta = "select PERF_ID from usuarios where PERF_ID = 33 and id_usuarios = " + varSesionesx.getIntNoUser();
                            ResultSet rsConta = oConnx.runQuery(strIsConta, true);
                            boolean bolContabilidad = false;
                            while (rsConta.next()) {
                                bolContabilidad = true;
                            }
                            rsConta.close();
                            //valida si es contabilidad

                            //obtenemos las fechas y cuantos cursos tiene                            
                            String strSQLNFecha = "select count(CC_FECHA_INICIAL) as NFechas , CC_FECHA_INICIAL"
                                    //                                    + " from cofide_cursos where CC_ACTIVO = 1 and CC_FECHA_INICIAL >= '20180112' GROUP BY CC_FECHA_INICIAL ORDER BY CC_FECHA_INICIAL";
                                    + " from cofide_cursos where CC_ACTIVO = 1 and CC_CURSO_ID <> 1 and CC_FECHA_INICIAL >= '" + fecx.getFechaActual() + "' GROUP BY CC_FECHA_INICIAL ORDER BY CC_FECHA_INICIAL";
                            ResultSet rsNFechas = oConnx.runQuery(strSQLNFecha, true);
                            while (rsNFechas.next()) {
                                //obtener los cursos de cada fecha
                                intNFechas = rsNFechas.getInt("NFechas");
                                strFechaIni = rsNFechas.getString("CC_FECHA_INICIAL");
                                Date date = conver.parse(strFechaIni);
                                String strLiga = "";
                                String strTipo = "";
                        %>            
                    <tr class="tr1">
                        <td class="fecha td1 FechaCurso" rowspan="<%=intNFechas%>">
                            <table class="inTable FechaCurso">
                                <tr><td><%=formatDiaLetra.format(date)%></td></tr>
                                <tr><td><%=formatDia.format(date)%></td></tr>
                                <tr><td><%=formatMesAnio.format(date)%></td></tr>
                            </table>
                        </td>
                        <%
                            String strSQL = "select CC_ID_WEB,CC_HRS_NDPC,CC_IVA_ON,CC_PRECIO_ON,CC_IVA_PRES,CC_PRECIO_PRES,CC_SESION,"
                                    + "CC_NOMBRE_CURSO,CC_MONTAJE,cc.CC_CURSO_ID,CC_FECHA_INICIAL,CC_IS_SEMINARIO,CC_IS_DIPLOMADO,"
                                    + "CC_DURACION_HRS, CC_DURACION_HRS2, CC_DURACION_HRS3,CC_MONTAJE,"
                                    + "(select CSH_COLOR from cofide_sede_hotel where CSH_ID = CC_SEDE_ID) as HCOLOR,"
                                    + "(select GROUP_CONCAT(CONCAT_WS(',',CC_GIRO)) from cofide_curso_giro where cofide_curso_giro.CC_CURSO_ID = cc.CC_CLAVES) as CC_GIRO,"
                                    + "(select GROUP_CONCAT(CONCAT_WS(',',CC_AREA)) from cofide_curso_segmento where cofide_curso_segmento.CC_CURSO_ID = cc.CC_CLAVES) as CC_AREA,"
                                    + "(select GROUP_CONCAT(CONCAT_WS(',',ci_instructor)) from cofide_instructor,cofide_instructor_imparte as ci where cofide_instructor.CI_INSTRUCTOR_ID = ci.CI_INSTRUCTOR_ID and ci.CC_CURSO_ID = cc.CC_CURSO_ID) as CC_INSTRUCTOR "
                                    + "from cofide_cursos as cc "
                                    + "where CC_ACTIVO = 1 and CC_FECHA_INICIAL = '" + rsNFechas.getString("CC_FECHA_INICIAL") + "' and CC_CURSO_ID <> 1 order by CC_FECHA_INICIAL, CC_HR_EVENTO_INI";
                            ResultSet rsx = oConnx.runQuery(strSQL, true);
                            while (rsx.next()) {
                                if (rsx.getString("CC_IS_SEMINARIO").equals("1") && rsx.getString("CC_IS_DIPLOMADO").equals("0")) {
                                    strTipo = "3";
                                } else {
                                    if (rsx.getString("CC_IS_SEMINARIO").equals("0") && rsx.getString("CC_IS_DIPLOMADO").equals("1")) {
                                        strTipo = "2";
                                    } else {
                                        strTipo = "1";
                                    }
                                }

                                if (rsx.getString("CC_ID_WEB").equals("")) {
                                    strLiga = "http://www.cofide.org/producto/";
                                } else {
                                    strLiga = rsx.getString("CC_ID_WEB");
                                }

                                /**
                                 * 0 - lugares vendidos presencial 1 - lugares
                                 * vendidos en linea 2 - lugares pagados
                                 * presencial 3 - lugares vendidos en linea 4 -
                                 * lugares pendientes presencial 5 - lugares
                                 * vendidos en linea
                                 */
                                int[] intInfoCursos = cal.getintVenta(rsx.getString("CC_CURSO_ID"));
                                String strDuracion = cal.strGetDuracion(rsx.getString("CC_DURACION_HRS"), rsx.getString("CC_DURACION_HRS2"), rsx.getString("CC_DURACION_HRS3"));

                                int intDisponibles = rsx.getInt("CC_MONTAJE");
                                int intLugaresPresencialRRHH = tele_.getAsistentesRRHH(rsx.getString("CC_CURSO_ID"), oConnx, 1);
                                int intLugaresEnLineaRRHH = tele_.getAsistentesRRHH(rsx.getString("CC_CURSO_ID"), oConnx, 2);
                                //lugares para sumar a los pagados 
                                int intpagadoPres = intInfoCursos[2] + intLugaresPresencialRRHH;
                                int intpagadoEnLinea = intInfoCursos[3] + intLugaresEnLineaRRHH;
                                //lugares ocupados prsenciales y en linea
                                intLugaresPresencialRRHH = intLugaresPresencialRRHH + intInfoCursos[0];
                                intLugaresEnLineaRRHH = intLugaresEnLineaRRHH + intInfoCursos[1];

                                intDisponibles = intDisponibles - intLugaresPresencialRRHH;
                        %>
                        <td class="td1" style="background-color: <%=rsx.getString("HCOLOR")%>;"></td>
                        <td class="td1 Curso">
                            <b>
                                <div style="text-align: center">
                                    <cite>ID: 
                                        <%=rsx.getString("cc.CC_CURSO_ID")%>                                    
                                    </cite>
                                    <%
                                        if (rsx.getInt("CC_HRS_NDPC") > 0) {
                                    %>
                                    <cite> / NDPC: 
                                        <%=rsx.getString("CC_HRS_NDPC")%>

                                    </cite>
                                    <%
                                        }
                                    %>
                                </div>                                
                                <br>                                
                                <a href="<%=strLiga%>" target="_blank">
                                    <%=rsx.getString("CC_NOMBRE_CURSO")%>
                                </a>
                                <br>
                                <br>
                                <div style="font-size: 10px">
                                    <%
                                        if (rsx.getString("CC_GIRO") != null) {
                                    %>
                                    <bold>GIRO: </bold>
                                        <%=rsx.getString("CC_GIRO")%>
                                    <br>
                                    <%
                                        }
                                    %>

                                    <%
                                        if (rsx.getString("CC_AREA") != null) {
                                    %>
                                    <bold>&Aacute;rea: </bold>
                                        <%=rsx.getString("CC_AREA")%>
                                    <br>
                                </div>
                                <%
                                    }
                                %>
                            </b>
                        </td>
                        <td class="td1 DetalleCurso">
                            <table>
                                <tr><td><%=rsx.getString("CC_INSTRUCTOR")%></td></tr>
                                <tr><td><i class="fa fa-clock-o"></i> <%=rsx.getString("CC_SESION")%> Hrs. 
                                        <br> 
                                        <i class="fa fa-hourglass-half"></i> <%=strDuracion%> Hrs.</td></tr>
                            </table>
                        </td>
                        <td class="td1 DetalleCurso">
                            <table>
                                <tr><td><i class="fa fa-user iconos"></i></td><td>$ <%=rsx.getString("CC_PRECIO_PRES")%>0 + IVA <br>$ <%=rsx.getString("CC_IVA_PRES")%>0</td></tr>
                                <tr><td><i class="fa fa-desktop iconos"></i></td><td>$ <%=rsx.getString("CC_PRECIO_ON")%>.00 + IVA <br>$ <%=rsx.getString("CC_IVA_ON")%>.00</td></tr>
                            </table>
                        </td>
                        <td class="td1 CursosDisp">
                            <table class="CursosDisp">
                                <tr>
                                    <td><i class="fa fa-shopping-cart iconos" title="Inscritos" style="color: #00D827"> </i></td><td><%=intLugaresPresencialRRHH%></td> <!-- inscritos presenciales -->                                    
                                    <td><i class="fa fa-shopping-cart iconos" title="Ventidos Online" style="color: #2cc3f9"> </i></td><td><%=intLugaresEnLineaRRHH%></td> <!-- inscritos online -->                                    
                                </tr>
                                <tr>
                                    <td><i class="fa fa-shopping-cart iconos" title="Disponibles" style="color: gray"> </i></td><td><%=intDisponibles%></td>
                                </tr>
                            </table>
                        </td>
                        <td class="td1 CursosDisp">
                            <table style="width: 115px">
                                <tr>
                                    <td><i class="fa fa-usd iconos" title="Pagados" style="color: #00D827"> </i></td><td><%= intpagadoPres%></td> <!-- presencial-->
                                    <td><i class="fa fa-usd iconos" title="Pagados Online" style="color: #2cc3f9"> </i></td><td><%=intpagadoEnLinea%></td> <!-- en linea -->
                                </tr>
                                <tr>
                                    <td><i class="fa fa-usd iconos" title="Pendiente" style="color: gray"> </i></td><td><%=intInfoCursos[4]%></td> <!-- presensial -->
                                    <td><i class="fa fa-usd iconos" title="Pendiente Online" style="color: #001c4f"> </i></td><td><%=intInfoCursos[5]%></td> <!-- en linea -->
                                </tr>
                            </table>
                        </td>
                        <%
                            if (!bolIsTMK && !bolContabilidad) { //tmk = false and contabilidad = false
                        %>
                        <td class="td1">
                            <input type="button" value="Lista asistencia" onclick="OpnDetalleCurso(<%=rsx.getString("CC_CURSO_ID")%>)" size="15px" class="btn btn-default btn-lg active" title="Listado de asistencia">                            
                        </td>
                        <%
                            }
                        %>
                    </tr>
                    <%
                            }
                            rsx.close();
                        }
                        rsNFechas.close();
                    %>
                </table>
            </div>
        </div>
    </body>
    <%    oConnx.close();
    %>
</html>