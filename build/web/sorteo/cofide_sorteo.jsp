<%-- 
    Document   : cofide_sorteo
    Created on : 30/11/2017, 01:34:11 PM
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
    if (strid.equals("1")) {

        boolean bolExiste = false;
        String strNombreExiste = "";
        int intConsecutivo = 0;
        String strNombre = request.getParameter("nombre").trim();
        String strCorreo = request.getParameter("correo").trim();
        final String strOpcion1 = URLDecoder.decode(new String(request.getParameter("uno").getBytes("iso-8859-1")), "UTF-8").trim();
        final String strOpcion2 = URLDecoder.decode(new String(request.getParameter("dos").getBytes("iso-8859-1")), "UTF-8").trim();
        final String strOpcion3 = URLDecoder.decode(new String(request.getParameter("tres").getBytes("iso-8859-1")), "UTF-8").trim();
//        String strOpcionFile1 = request.getParameter("nuno").trim();
//        String strOpcionFile2 = request.getParameter("ndos").trim();
//        String strOpcionFile3 = request.getParameter("ntres").trim();
        if (strNombre == null) {
            strNombre = "";
        }
        if (strCorreo == null) {
            strCorreo = "";
        }
//        if (strOpcionFile1 == null) {
//            strOpcionFile1 = "";
//        }
//        if (strOpcionFile2 == null) {
//            strOpcionFile2 = "";
//        }
//        if (strOpcionFile3 == null) {
//            strOpcionFile3 = "";
//        }

        strSQL = "SELECT * FROM cfd_sorteo_usuarios where CFD_CORREO = '" + strCorreo + "';";
        rs = oConn.runQuery(strSQL, true);
        while (rs.next()) {
            strNombreExiste = rs.getString("CFD_NOMBRE");
            bolExiste = true;
        }
        rs.close();
        if (!bolExiste) {
            strSQL = "INSERT INTO cfd_sorteo_usuarios (CFD_NOMBRE, CFD_CORREO, CFD_EVENTO) VALUES "
                    + "('" + strNombre + "', '" + strCorreo + "', '1')";
            oConn.runQueryLMD(strSQL);
            if (!oConn.isBolEsError()) {

                try {
                    strSQL = "select @@identity as Consecutivo";
                    rs = oConn.runQuery(strSQL, true);
                    while (rs.next()) {
                        intConsecutivo = rs.getInt("Consecutivo");

                        strSQL = "INSERT INTO cfd_sorteo_usuarios_deta (CFD_ID, CFDD_OPCION1, CFDD_OPCION2, CFDD_OPCION3, CFDD_FILE1, CFDD_FILE2, CFDD_FILE3) VALUES "
                                //                                + "('" + intConsecutivo + "', '" + strOpcion1.replace("'", "\\'") + "', '" + strOpcion2.replace("'", "\\'") + "', '" + strOpcion3.replace("'", "\\'") + "', '" + strOpcionFile1 + "', '" + strOpcionFile2 + "', '" + strOpcionFile3 + "')";
                                + "('" + intConsecutivo + "', '" + strOpcion1.replace("'", "\\'") + "', '" + strOpcion2.replace("'", "\\'") + "', '" + strOpcion3.replace("'", "\\'") + "', '', '', '')";
                        oConn.runQueryLMD(strSQL);
                        if (!oConn.isBolEsError()) {
                            strRespuesta = "OK";
                        } else {
                            strRespuesta = "Ocurrio un problema al guardar tus deseos :'( [ " + oConn.getStrMsgError() + " ]";
                        }
                    }
                    rs.close();
                } catch (SQLException ex) {
                    System.out.println("Error GetNombre Comprobante: " + ex.getLocalizedMessage());
                }
            } else {
                strRespuesta = "Ocurrio un problema al guardar tu información :'( [ " + oConn.getStrMsgError() + " ]";
            }
        } else {
            strRespuesta = "El usuario: [ " + strNombreExiste + " con el correo: " + strCorreo + " ] ya esta registrado.";
        }

        out.clearBuffer();//Limpiamos buffer
        atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
        out.println(strRespuesta);//Pintamos el resultado
    }
    if (strid.equals("2")) {

        String strIdParticipante = "0";
        String strIdAmigoSecrteto = "0";
        //OBTENEMOS AL PARTICIPANTE
        strSQL = "select CFD_ID from cfd_sorteo_usuarios order by CFD_ID";
        rs = oConn.runQuery(strSQL, true);
        while (rs.next()) {

            strIdParticipante = rs.getString("CFD_ID");
            System.out.println("El participante es el ID : " + strIdParticipante);
            //OBTENEMOS AL AMIGO SECRETO
            strSQL2 = "select CFD_ID from cfd_sorteo_usuarios where CFD_ID <> " + strIdParticipante + " and CFD_ESTATUS = 0 order by RAND() limit 1";
            rsx = oConn.runQuery(strSQL2);
            while (rsx.next()) {
                System.out.println("El amigo secreto es: " + strIdAmigoSecrteto);
                strIdAmigoSecrteto = rsx.getString("CFD_ID");
                //ASIGNAMOS LA RELACIÓN DEL PARTICIPANTE CON SU AMIGO SECRETO
                strSQL_LDM = "insert into cfd_sorteo_relacion (CSE_ID, CFD_ID, CSR_ID_AMIGO_SORPRESA) values (1," + strIdParticipante + "," + strIdAmigoSecrteto + ")";
                oConn.runQueryLMD(strSQL_LDM);
                if (!oConn.isBolEsError()) {
                    //ACTUALIZAMOS AL AMIGO SECRETO PARA QUE YA NO ESTE DISPONIBLE EN EL SORTEO
                    strSQL_LDM = "update cfd_sorteo_usuarios set CFD_ESTATUS = 1 where CFD_ID = " + strIdAmigoSecrteto;
                    oConn.runQueryLMD(strSQL_LDM);
                    if (!oConn.isBolEsError()) {
                        strRespuesta = "OK";
                    } else {
                        strRespuesta = "Ocurrio un error al intentar actualizar el estatus del amigo secreto";
                    }
                } else {
                    strRespuesta = "Ocurrio un problema al asignar la relación con los amigos secretos";
                }
            }
            rsx.close();
        }
        rs.close();

        out.clearBuffer();//Limpiamos buffer
        atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
        out.println(strRespuesta);//Pintamos el resultado
    }
    if (strid.equals("3")) {
        strSQL = "select "
                + "CFD_ID,"
                + "(select CFD_NOMBRE from cfd_sorteo_usuarios as us where us.CFD_ID = r.CFD_ID) as Participante, "
                + "(select CFD_CORREO from cfd_sorteo_usuarios as us where us.CFD_ID = r.CFD_ID) as Correo, "
                + "CSR_ID_AMIGO_SORPRESA, "
                + "(select CFD_NOMBRE from cfd_sorteo_usuarios as us where us.CFD_ID = r.CSR_ID_AMIGO_SORPRESA) as AmigoSecreto "
                + "from cfd_sorteo_relacion as r order by CFD_ID;";
        rs = oConn.runQuery(strSQL, true);
        strRespuesta = "<table id='sorteo'>"
                + "<thead>"
                + "<th>Participante</th>"
                + "<th>Correo del Participante</th>"
                + "<th>Amigo secreto</th>"
                + "</thead>"
                + "<tbody>";
        while (rs.next()) {
            strRespuesta += "<tr>"
                    + "<td>" + rs.getString("Participante") + "</td>"
                    + "<td>" + rs.getString("Correo") + "</td>"
                    + "<td>" + rs.getString("AmigoSecreto") + "</td>"
                    + "</tr>";
        }
        rs.close();
        strRespuesta += "</tbody>"
                + "</table>";
        out.clearBuffer();//Limpiamos buffer
        atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
        out.println(strRespuesta);//Pintamos el resultado
    }
    if (strid.equals("4")) {

        String strAsunto = "CONOCE A TU AMIGO SECRETO!";
        String strIdparticipante = "";
        String strNombre = "";
        String strCorreo = "";
        String strNombreAmigo = "";
        String strOpcion1 = "";
        String strOpcion2 = "";
        String strOpcion3 = "";

        strSQL = "select CFD_ID, CFD_NOMBRE, CFD_CORREO from cfd_sorteo_usuarios ORDER BY CFD_ID";
        rs = oConn.runQuery(strSQL, true);
        while (rs.next()) {

            strIdparticipante = rs.getString("CFD_ID");
            strNombre = rs.getString("CFD_NOMBRE");
            strCorreo = rs.getString("CFD_CORREO");

            strSQL2 = "select c.CFD_ID, c.CFD_NOMBRE, CFDD_OPCION1, CFDD_OPCION2, CFDD_OPCION3 "
                    + "from cfd_sorteo_usuarios_deta as cd, cfd_sorteo_usuarios as c "
                    + "where cd.CFD_ID = c.CFD_ID and "
                    + "cd.CFD_ID = (select CSR_ID_AMIGO_SORPRESA from cfd_sorteo_relacion where CFD_ID = " + strIdparticipante + ")";
            rsx = oConn.runQuery(strSQL2, true);
            while (rsx.next()) {

                strNombreAmigo = rsx.getString("CFD_NOMBRE");
                strOpcion1 = rsx.getString("CFDD_OPCION1");
                strOpcion2 = rsx.getString("CFDD_OPCION2");
                strOpcion3 = rsx.getString("CFDD_OPCION3");

            }
            rsx.close();
            String strBody = "<style>"
                    + ".efecto{"
                    + "	-webkit-box-shadow: 16px 19px 48px 4px rgba(0,0,0,0.38);"
                    + "	-moz-box-shadow: 16px 19px 48px 4px rgba(0,0,0,0.38);"
                    + "	box-shadow: 16px 19px 48px 4px rgba(0,0,0,0.38);"
                    + "}"
                    + "</style>"
                    + "<div style='text-align:center; color:#99cc00;' class='efecto'>"
                    + "<div>"
                    + "<img src='http://201.161.14.206:9001/cofide/images/cofide.png' style='width:200px'/>"
                    + "<br />"
                    + "<label><b>Estimado compañero:</b> </label>"
                    + "<br />"
                    + "<label style='color:#000'><b>" + strNombre + "</b></label>"
                    + "</div>"
                    + "<hr />"
                    + "<div>"
                    + "<label><b>Tu amigo secreto es:</b> </label>"
                    + "<br />"
                    + "<label style='color:#000'><b>" + strNombreAmigo + "</b> </label>"
                    + "</div>"
                    + "<div style='width:95%'>"
                    + "<label><b>Sus opciones de regalo son:</b></label>"
                    + "<ul style='text-align:left; color:#000;'><b>";
            if (strOpcion1 != "") {
                strBody += "<li>" + strOpcion1 + "</li>"
                        + "<br />";
            }
            if (strOpcion2 != "") {
                strBody += "<li>" + strOpcion2 + "</li>"
                        + "<br />";
            }
            if (strOpcion3 != "") {
                strBody += "<li>" + strOpcion3 + "</li>"
                        + "<br />";
            }
            strBody += "</b>"
                    + "</ul>"
                    + "</div>"
                    + "</div>"; 
           //FUNCION DE ENVIO DE CORREO
            new CRM_Envio_Template().CofideIntercambio(oConn, strCorreo, strAsunto, strBody);
        }
        rs.close();

        out.clearBuffer();//Limpiamos buffer
        atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
        out.println(strRespuesta);//Pintamos el resultado
    }
    oConn.close();
%>

