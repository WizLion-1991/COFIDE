<%@page import="com.mx.siweb.erp.especiales.cofide.Telemarketing"%>
<%@page import="com.mx.siweb.erp.especiales.cofide.COFIDE_Calendario"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="comSIWeb.Operaciones.Conexion"%>
<%@page import="comSIWeb.ContextoApt.VariableSession"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="comSIWeb.Utilerias.Fechas"%>
<!DOCTYPE html>

<%
    /*Obtenemos las variables de sesion*/
    VariableSession varSesiones = new VariableSession(request);
    varSesiones.getVars();
    //Abrimos la conexion
    Conexion oConn = new Conexion(varSesiones.getStrUser(), this.getServletContext());
    oConn.open();
    Fechas fec = new Fechas();
    COFIDE_Calendario cal = new COFIDE_Calendario(oConn);
    Telemarketing tele_ = new Telemarketing();
    ResultSet rs;
    String strCurso = "";
    String strFecha = "";
    String strSede = "";
    int NParticipante = 0;
    int NParticipanteO = 0;
    String strPresencial = "0";
    String strOnline = "0";
    int intEquipoA = 0; //a = 1
    int intEquipoB = 0; //d = 2
    int intEquipoC = 0; //e = 3
    boolean bolCurso = false;
    int intLugarPRRHH = 0;
    int intLugarORRHH = 0;
    String strIdCurso = request.getParameter("id");
    intLugarPRRHH = tele_.getAsistentesRRHH(strIdCurso, oConn, 1);
    intLugarORRHH = tele_.getAsistentesRRHH(strIdCurso, oConn, 2);
    String strSqlInfCurso = "select CC_CURSO_ID, CC_NOMBRE_CURSO, CC_FECHA_INICIAL, CC_IS_PRESENCIAL,CC_IS_ONLINE, "
            + "(select CSH_SEDE from cofide_sede_hotel where CSH_ID = CC_SEDE_ID) as SEDE "
            + "from cofide_cursos where CC_ACTIVO = 1 and CC_CURSO_ID = " + strIdCurso;
    rs = oConn.runQuery(strSqlInfCurso, true);
    while (rs.next()) {
        bolCurso = true;
        strCurso = rs.getString("CC_NOMBRE_CURSO");
        strFecha = rs.getString("CC_FECHA_INICIAL");
        strSede = rs.getString("SEDE");
        int[] intVendido = cal.getintVenta(strIdCurso);
        NParticipante = intVendido[0];
        NParticipanteO = intVendido[1];
    }
    rs.close();
    if (bolCurso) {
        String strSql = "SELECT "
                + "p.CT_ID,"
                + "if(CP_TIPO_CURSO = 1,'PRESENCIAL','EN LÍNEA') AS tipocurso,"
                + "CONCAT(p.CP_TITULO,' ',p.CP_NOMBRE,' ',p.CP_APPAT,' ',p.CP_APMAT) AS Nombre,"
                + "IFNULL((select if(FAC_VALIDA = 1,'PAGADA','PENDIENTE') from view_ventasglobales where FAC_ID = if(TIPO_DOC='F',CP_FAC_ID, CP_TKT_ID)),0) AS Estatus,"
                + "IFNULL((select if(TIPO_DOC='F',FAC_FOLIO_C,FAC_FOLIO) from view_ventasglobales where FAC_ID = if(TIPO_DOC='F',CP_FAC_ID, CP_TKT_ID)),0)AS FOLIO,"
                + "(select nombre_usuario from usuarios where id_usuarios = CP_USUARIO_ALTA) AS nombre_usuario "
                + "FROM "
                + "cofide_participantes AS p, cofide_cursos AS c "
                + "WHERE "
                + "p.CP_ID_CURSO = c.CC_CURSO_ID "
                + "AND "
                + "IF ("
                + "CP_FAC_ID = 0,"
                + "CP_TKT_ID IN ("
                + "(SELECT "
                + "v.FAC_ID "
                + "FROM "
                + "view_ventasglobales AS v,"
                + "view_ventasglobalesdeta AS vd "
                + "WHERE "
                + "v.TIPO_DOC = vd.TIPO_DOC "
                + "AND v.FAC_ID = vd.FAC_ID "
                + "AND FAC_ANULADA = 0 "
                + "AND PR_ID = CP_ID_CURSO "
                + "AND vd.FACD_TIPO_CURSO = CP_TIPO_CURSO "
                + "AND v.TIPO_DOC = 'T' and v.CANCEL = 0)),"
                + "CP_FAC_ID IN ("
                + "(SELECT "
                + "v.FAC_ID "
                + "FROM "
                + "view_ventasglobales AS v,"
                + "view_ventasglobalesdeta AS vd "
                + "WHERE "
                + "v.TIPO_DOC = vd.TIPO_DOC "
                + "AND v.FAC_ID = vd.FAC_ID "
                + "AND FAC_ANULADA = 0 "
                + "AND PR_ID = CP_ID_CURSO "
                + "AND vd.FACD_TIPO_CURSO = CP_TIPO_CURSO "
                + "AND v.TIPO_DOC = 'F' and v.CANCEL = 0"
                + "))) "
                + "AND CP_ID_CURSO = " + strIdCurso + " "
                + "ORDER BY CP_TIPO_CURSO,CP_APPAT,CP_NOMBRE;";

%>
<head>
    <!--Version 1.9.0.min -->
    <script type="text/javascript" src="jqGrid/jquery-1.9.0.min.js" ></script>
    <script type="text/javascript" src="jqGrid/grid.locale-es4.7.0.js" ></script>
    <script src="https://code.highcharts.com/highcharts.js"></script>
    <script src="https://code.highcharts.com/modules/exporting.js"></script>
    <script type="text/javascript" src="javascript/cofide_curso_activo.js"></script> <!-- importar archivo javascript--> 
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">   
    <script type="text/javascript" src="javascript/Ventanas.js"></script>
    <!-- script de grafica -->
    <title>
        Participantes del Curso <%=strCurso%>
    </title>
</head>
<style>
    .imagen{
        background-color: black;
        width: 100%;
        height: 20%;
    }
    .TituloDetalle{
        text-align: center;
        font-size: 18px;
    }
    .Detalles{
        padding-left: 10%;
    }
    .Detalles2{
        padding-left: 20%;
    }
    table{
        border-collapse: collapse;
        margin: auto;
        /*background-color: #E6E6E6;*/
        border: black 3px solid;
        width: 80%;
    }
    thead{
        background-color: black;
        color: #C6D880;
        display: block;
    }
    tbody{
        /*background-color: #C6D880;*/
        display: block;
        height: 500px;
        overflow-y: auto;
        overflow-x: hidden;
    }
    tr{
        border: black 3px solid;
    }
    td{
        border: black 3px solid;
        padding: 10px;
    }
    th{
        border: black 3px solid;
        padding: 10px;
        text-align: center;
    }

    table th:first-child{
        width: 90px;
    }
    table th:nth-child(2){
        width: 200px;
    }
    table th:nth-child(3){
        width: 300px;
    }
    table th:nth-child(4){
        width: 200px;
    }
    table th:nth-child(5){
        width: 300px;
    }
    table th:nth-child(6){
        width: 100px;
    }

    table td:first-child{
        width: 90px;
    }
    table td:nth-child(2){
        width: 200px;
    }
    table td:nth-child(3){
        width: 300px;
    }
    table td:nth-child(4){
        width: 200px;
    }
    table td:nth-child(5){
        width: 300px;
    }
    table td:nth-child(6){
        width: 100px;
    }

    .idcurso{
        width: 20%;
    }
    .tipocurso{
        width: 40%;
    }
    .nombre{
        width: 50%;
    }
    .estatus{
        width: 40%;
    }
    .ejecutivo{
        width: 40%;
    }
    .nfactura{
        width: 20%;
    }

</style>
<body onload="DrawGraphic(<%=strIdCurso%>,<%=intEquipoA%>,<%=intEquipoB%>,<%=intEquipoC%>)">
    <div>
        <div class="TituloDetalle">
            <div class=" imagen">
                <div style="padding-left: 4%">
                    <img src="images/cofide.png" style="width: 30%; padding-top: 2%">
                </div>
            </div>
            <h3><p>PARTICIPANTES DEL CURSO</p></h3>
            <div>
            </div>

        </div>
        <div class="row">
            <div class="col-md-8  Detalles">
                <div>
                    <h5>Curso: <b><%=strCurso%></b></h5>
                </div>
                <div>
                    <h5>Fecha: <b><%=fec.FormateaDDMMAAAA(strFecha, "/")%></b></h5>
                </div>
                <div>
                    <h5>Sede: <b><%=strSede%></b></h5>
                </div>
                <div>
                    <h5>Nº. Participantes Presenciales: <b><%=(NParticipante + intLugarPRRHH)%></b></h5>
                </div>
                <div>
                    <h5>Nº. Participantes en línea: <b><%=(NParticipanteO + intLugarORRHH)%></b></h5>
                </div>
                <div>
                    <input type="button" value="Exportar Excel Presencial" class="btn btn-default btn-lg active" onclick="openRepCurso_P(<%=strIdCurso%>)">                
                    <input type="button" value="Exportar Excel en Línea" class="btn btn-default btn-lg active" onclick="openRepCurso_O(<%=strIdCurso%>)">
                </div>
            </div>
        </div>
        <hr>
        <div>
            <table>
                <thead>
                    <tr>
                        <th class="idcurso">ID Cliente</th>
                        <th class="tipocurso">Tipo</th>
                        <th class="nombre">Nombre Completo</th>
                        <th class="estatus">Estatus</th>
                        <th class="ejecutivo">Ejecutivo</th>
                        <th class="nfactura">Nº. Venta</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        String strEstatus = "";
                        String strAgente = "";
                        String strFact = "";

                        rs = oConn.runQuery(strSql, true);
                        while (rs.next()) {

                            strEstatus = rs.getString("Estatus");
                            strAgente = rs.getString("nombre_usuario");
                            strPresencial = rs.getString("tipocurso");
                            if (rs.getString("FOLIO") != null) {
                                strFact = rs.getString("FOLIO");
                            } else {
                                strFact = "0";
                            }
                    %>
                    <tr>
                        <td><%=rs.getString("CT_ID")%></td>
                        <td><%=strPresencial%></td>
                        <td><%=rs.getString("Nombre")%></td>
                        <td><%=strEstatus%></td>
                        <td><%=strAgente%></td>
                        <td><%=strFact%></td>
                    </tr>
                    <%
                        }
                        rs.close();
                        strSql = "select CT_ID,  "
                                + "if(CP_TIPO_CURSO = 1,'PRESENCIAL','EN LÍNEA') AS tipocurso, "
                                + "CONCAT(CP_TITULO,' ',CP_NOMBRE,' ',CP_APPAT,' ',CP_APMAT) AS Nombre, "
                                + "'CORTESÍA' as Estatus, "
                                + "'0' as FOLIO, "
                                + "'COFIDE S.C.' as  nombre_usuario "
                                + "from cofide_participantes "
                                + "where CP_ID_CURSO = " + strIdCurso + " and CP_TKT_ID = 1;";
                        rs = oConn.runQuery(strSql, true);
                        while (rs.next()) {

                            strEstatus = rs.getString("Estatus");
                            strAgente = rs.getString("nombre_usuario");
                            strPresencial = rs.getString("tipocurso");
                            if (rs.getString("FOLIO") != null) {
                                strFact = rs.getString("FOLIO");
                            } else {
                                strFact = "0";
                            }
                    %>
                    <tr>
                        <td><%=rs.getString("CT_ID")%></td>
                        <td><%=strPresencial%></td>
                        <td><%=rs.getString("Nombre")%></td>
                        <td><%=strEstatus%></td>
                        <td><%=strAgente%></td>
                        <td><%=strFact%></td>
                    </tr>
                    <%

                        }
                        rs.close();
                    %>
                </tbody>
            </table>
        </div>
        <div>
            <hr>
        </div>
    </div>
</body>
<%
} else {
%>
<h1><P>No hay Información disponible para este curso</P></h1>
        <%
            }
        %>
</html>