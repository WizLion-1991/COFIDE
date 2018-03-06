<%-- 
    Document   : COFIDE_Correo
    Created on : 28-mar-2016, 9:07:02
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
<%
    VariableSession varSesiones = new VariableSession(request);
    varSesiones.getVars();
    Conexion oConn = new Conexion(varSesiones.getStrUser(), this.getServletContext());
    oConn.open();

//    COFIDE_Mail_cursos cm = new COFIDE_Mail_cursos();
    CRM_Envio_Template crm = new CRM_Envio_Template();
    String strIdCurso = request.getParameter("id_curso");
    int intIDEjecutivo = varSesiones.getIntNoUser();
    String strTem1 = request.getParameter("Template1");
    String strSede = "0";
    if (!request.getParameter("sede").equals("")) {
        strSede = request.getParameter("sede");
    }
    int intSede = Integer.parseInt(strSede);
    String strTemplate1 = "";
    if (!strTem1.equals("")) {
//        strTemplate1 = cm.Cofide_Mail_Cursos(oConn, strIdCurso, strTem1, varSesiones.getIntNoUser());
        if (strTem1.equals("3 CURSOS")) {
            strTemplate1 = crm.Cofide3Cursos(oConn, intIDEjecutivo, "pruebas@cofide.org", "", 1);
//            strTemplate1 = crm.Cofide3Cursos(oConn, intIDEjecutivo, "pruebas@cofide.org", "", intPreview, strPlantilla, intIdPlantilla)
            System.out.println(strTemplate1 + " 3 cursos");
        }
        if (strTem1.equals("DIPLOMADO")) {
            strTemplate1 = crm.CofideDiplomado(oConn, intIDEjecutivo, "pruebas@cofide.org", "", 1);
            System.out.println(strTemplate1 + " diplomado");
        }
        if (strTem1.equals("DIPLOMADO")) {
            strTemplate1 = crm.CofideDipMod(oConn, intIDEjecutivo, "pruebas@cofide.org", "", 1);
            System.out.println(strTemplate1 + " diplomado");
        }
        if (strTem1.equals("MES")) {
            strTemplate1 = crm.CofideMensual(oConn, intIDEjecutivo, "pruebas@cofide.org", "", 1);
            System.out.println(strTemplate1 + " mes");
        }
        if (strTem1.equals("RECURRENTE")) {
            strTemplate1 = crm.CofideRecurrente(oConn, intIDEjecutivo, "pruebas@cofide.org", "", "", 1);
            System.out.println(strTemplate1 + " recurrente");
        }
        if (strTem1.equals("SEDE")) {
            strTemplate1 = crm.CofideSede(oConn, intIDEjecutivo, "pruebas@cofide.org", getSede(oConn, intSede), "", 1);
            System.out.println(strTemplate1 + " sede");
        }

        out.print(strTemplate1);
        System.out.println("aqui empieza la plantilla : \n" + strTemplate1);
    } else {
        out.print("<h1>No se encontro Template</h>");
    }
    oConn.close();
    out.clearBuffer();//Limpiamos buffer
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
%>
