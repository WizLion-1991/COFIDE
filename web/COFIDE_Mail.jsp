<%-- 
    Document   : COFIDE_Mail
    Created on : 13/01/2017, 10:16:34 AM
    Author     : juliocesar
--%>

<%@page import="com.mx.siweb.erp.especiales.cofide.CRM_Envio_Template"%>
<%@page import="comSIWeb.ContextoApt.VariableSession"%>
<%@page import="com.mx.siweb.erp.especiales.cofide.COFIDE_Mail_cursos"%>
<%@page import="comSIWeb.Utilerias.Fechas"%>
<%@page import="comSIWeb.Utilerias.Mail"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="comSIWeb.Operaciones.Conexion"%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <script type="text/javascript" src="javascript/cofide_programacionmails.js"></script> <!-- importar archivo javascript--> 
    <link href="images/cofide.ico" rel="shortcut icon" type="image/vnd.microsoft.icon" />
    <title>Vista previa de la plantilla</title>
</head>
<%
    VariableSession varSesiones = new VariableSession(request);
    varSesiones.getVars();
    Conexion oConn = new Conexion(varSesiones.getStrUser(), this.getServletContext());
    oConn.open();

    CRM_Envio_Template crm = new CRM_Envio_Template();
    String strIdPlantilla = "0";
    if (request.getParameter("idPlantilla") != null) {
        if (!request.getParameter("idPlantilla").isEmpty()) {
            strIdPlantilla = request.getParameter("idPlantilla");

            String strIdCurso = "";
            String strTem1 = "";
            String strSede = "0";
            int intIdPlantillaD = 0;
            int intIDEjecutivo = varSesiones.getIntNoUser();
            String strTemplate1 = "";
            boolean bolPlantilla = false;
            int intSede = 0;
            String strSql = "";
            ResultSet rs;

            strSql = "select crm_correos.CRC_ID, CRC_ID_PLANTILLA, CRCD_ID_CURSO, CRCD_TIPO, CRCD_ESTATUS "
                    + "from crm_correos,crm_correos_deta "
                    + "where crm_correos.CRC_ID = crm_correos_deta.CRC_ID "
                    + "and crm_correos.CRC_ID = " + strIdPlantilla + " order by CRCD_ID_CURSO ASC";
            rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                bolPlantilla = true;
                intIdPlantillaD = rs.getInt("CRC_ID"); //id del evento
                strTem1 = getPlantilla(oConn, rs.getString("CRC_ID_PLANTILLA")); //id plantilla/ plantilla
                strIdCurso = rs.getString("CRCD_ID_CURSO");
            }
            rs.close();
            if (bolPlantilla) {
                if (strTem1.equals("PERSONALIZADO")) {
                    System.out.println("PERSONALIZADO");
                    strTemplate1 = crm.Cofide3Cursos(oConn, intIDEjecutivo, "prueba@cofide.org", "", 1, strTem1, intIdPlantillaD);
                }
                if (strTem1.equals("DIPLOMADO_MOD")) {
                    System.out.println(" diplomado");
                    strTemplate1 = crm.CofideDipMod(oConn, intIDEjecutivo, "pruebas@cofide.org", "", 1, strTem1, intIdPlantillaD);
                }
                if (strTem1.equals("MES")) {
                    System.out.println(" mes");
                    strTemplate1 = crm.CofideMensual(oConn, intIDEjecutivo, "pruebas@cofide.org", "", 1, strTem1, intIdPlantillaD);
                }
                if (strTem1.equals("RECURRENTE")) {
                    System.out.println(" recurrente");
                    strTemplate1 = crm.CofideRecurrente(oConn, intIDEjecutivo, "pruebas@cofide.org", "", "", 1, strTem1, intIdPlantillaD);
                }
                if (strTem1.equals("SEDE")) {
                    System.out.println(" sede");
                    strTemplate1 = crm.CofideSede(oConn, intIDEjecutivo, "pruebas@cofide.org", "", "", 1, strTem1, intIdPlantillaD);
                }
                if (strTem1.equals("PUNTOS DE LEALTAD")) {
                    System.out.println(" PUNTOS DE LEALTAD");
                    strTemplate1 = crm.CofidePLealtadCursos(oConn, intIDEjecutivo, "pruebas@cofide.org", "", 1, strTem1, intIdPlantillaD);
                }
                if (strTem1.equals("HTML")) {
                    System.out.println(" HTML");
                    strTemplate1 = crm.CofideHTML(oConn, intIDEjecutivo, "pruebas@cofide.org", "", 1, strTem1, intIdPlantillaD);
                }
                if (strTem1.equals("COFIDEnet")) {
                    System.out.println(" COFIDEnet");
                    strTemplate1 = crm.COFIDEnet(oConn, intIDEjecutivo, "pruebas@cofide.org", "", 1, strTem1, intIdPlantillaD);
                }
            } else {
                strTemplate1 = "<h1>No se encontró Template</h>";
            }
            oConn.close();
            out.clearBuffer();//Limpiamos buffer
%>

<body>
    <%=strTemplate1%>
</body>


<%
} else {
%>
<body style="background: url('images/cofide_bgn.png'); background-size: 100%">
    <div style="height: 30%"></div>
    <div style="text-align: center; height: 30%; background: #000000; color: #bdff76; border-radius: 200px 0px 200px 0px;">
        <br>
        <h2><p>NO<br><i>se ha encontrado una</i><br>Plantilla <br><i>para mostrar</i></p></h2>
    </div>
    <div style="height: 30%"></div>

</body>
<%
    }
} else {
%>
<body style="background: url('images/cofide_bgn.png'); background-size: 100%">
    <div style="height: 30%"></div>
    <div style="text-align: center; height: 30%; background: #000000; color: #bdff76; border-radius: 200px 0px 200px 0px;">
        <br>
        <h2><p>NO<br><i>se ha encontrado una</i><br>Plantilla <br><i>para mostrar</i></p></h2>
    </div>
    <div style="height: 30%"></div>

</body>
<%
    }
%>
<%!
    public String getSede(Conexion oConn, int IntSede) {
        String strSede = "";
        String strSql = "select CSH_SEDE from cofide_sede_hotel where CSH_ID = " + IntSede;
        try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                strSede = rs.getString("");
            }
            rs.close();
            return strSede;
        } catch (SQLException ex) {
            System.out.println("ERROR " + ex);
        }
        return strSede;
    }

    public String getPlantilla(Conexion oConn, String strIdPlantilla) {
        String strPlantilla = "";
        String strSql = "select CTT_DESC from cofide_tipo_template where CTT_ID = " + strIdPlantilla;
        try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                strPlantilla = rs.getString("CTT_DESC");
            }
            rs.close();
        } catch (SQLException ex) {
            System.out.println("ERROR: " + ex);
        }
        return strPlantilla;
    }
%>
