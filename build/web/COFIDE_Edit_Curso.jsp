<%-- 
    Document   : COFIDE_Edit_Curso.jsp
    Created on : 03-feb-2016, 11:40:35
    Author     : COFIDE
--%>
<%@page import="java.net.URLDecoder"%>
<%@page import="ERP.Folios"%>
<%@page import="comSIWeb.Utilerias.Mail"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="comSIWeb.Utilerias.UtilXml"%>
<%@page import="comSIWeb.ContextoApt.atrJSP"%>
<%@page import="comSIWeb.Utilerias.Fechas"%>
<%@page import="comSIWeb.ContextoApt.Seguridad"%>
<%@page import="comSIWeb.ContextoApt.VariableSession"%>
<%@page import="comSIWeb.Operaciones.Conexion"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%

    /*Obtenemos las variables de sesion*/
    VariableSession varSesiones = new VariableSession(request);
    varSesiones.getVars();
    //Abrimos la conexion
    Conexion oConn = new Conexion(varSesiones.getStrUser(), this.getServletContext());
    oConn.open();
    //Instanciamos objeto de seguridad para no permitir el acceso externo a ver los XML
    Seguridad seg = new Seguridad(); //Valida que la peticion se halla hecho desde el mismo sitio
    Fechas fecha = new Fechas();
    UtilXml util = new UtilXml();
    String strSQL;
    ResultSet rs;
    String strResult = "";

    if (varSesiones.getIntNoUser() != 0 && seg.ValidaURL(request)) {
        //Obtenemos parametros
        String strid = request.getParameter("id");
        if (!strid.equals(null)) {
            //confirmar
            if (strid.equals("1")) {
                String id_Curso = request.getParameter("ID_CURSO");
                String strXMLData = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
                strXMLData += "<Participantes>";
                strSQL = "select * from cofide_participantes where CP_ID_CURSO = " + id_Curso;
                try {
                    rs = oConn.runQuery(strSQL, true);
                    while (rs.next()) {
                        strXMLData += "<datos";
                        strXMLData += " CP_NOMBRE=\'" + rs.getString("CP_NOMBRE") + "\'";
                        strXMLData += " CP_APPAT=\'" + rs.getString("CP_APPAT") + "\'";
                        strXMLData += " CP_APMAT=\'" + rs.getString("CP_APMAT") + "\'";
                        strXMLData += " CP_TITULO=\'" + rs.getString("CP_TITULO") + "\'";
                        strXMLData += " CP_NOSOCIO=\'" + rs.getString("CP_NOSOCIO") + "\'";
                        strXMLData += " CP_CORREO=\'" + rs.getString("CP_CORREO") + "\'";
                        strXMLData += " CP_COMENT=\'" + rs.getString("CP_COMENT") + "\'";
                        strXMLData += " CP_ASCOC=\'" + rs.getString("CP_ASCOC") + "\'";
                        strXMLData += "/>";
                    }
                    rs.getStatement().close();
                    rs.close();
                } catch (SQLException ex) {
                    ex.fillInStackTrace();

                }
                strXMLData += "</Participantes>";

                String strRes = strXMLData.toString();
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strRes);//Pintamos el resultado

            }//fin confirmar

            if (strid.equals("2")) {
                String id_Curso = request.getParameter("ID_CURSO");
                String strXMLData = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
                strXMLData += "<participantes>";
                strSQL = "select *,(select CTI_DESCRIPCION from cofide_titulos where cofide_titulos.CTI_ID = cofide_participantes.CP_TITULO) as titulo "
                        + " ,(select CAS_ASOCIACION from cofide_asociacion where cofide_asociacion.CAS_ID = cofide_participantes.CP_ASCOC) as asociacion "
                        + "from cofide_participantes where CP_ID_CURSO = " + id_Curso;
                try {
                    rs = oConn.runQuery(strSQL, true);
                    while (rs.next()) {
                        strXMLData += "<participantes_deta";
                        strXMLData += " CP_NOMBRE=\'" + rs.getString("CP_NOMBRE") + "\'";
                        strXMLData += " CP_APPAT=\'" + rs.getString("CP_APPAT") + "\'";
                        strXMLData += " CP_APMAT=\'" + rs.getString("CP_APMAT") + "\'";
                        strXMLData += " CP_NOSOCIO=\'" + rs.getString("CP_NOSOCIO") + "\'";
                        strXMLData += " CP_COMENT=\'" + rs.getString("CP_COMENT") + "\'";
                        if (rs.getString("CP_TKT_ID") != "" && rs.getString("CP_TKT_ID") != "0") {
                            strXMLData += " CP_FOLIO=\'" + getFolio(1, rs.getInt("CP_FAC_ID"), oConn) + "\'";
                        } else {
                            strXMLData += " CP_FOLIO=\'" + getFolio(2, rs.getInt("CP_FAC_ID"), oConn) + "\'";
                        }
                        if (rs.getInt("CP_ASISTENCIA") == 1) {
                            strXMLData += " CP_ASISTENCIA=\'" + "SI" + "\'";
                        } else {
                            strXMLData += " CP_ASISTENCIA=\'" + "NO" + "\'";
                        }
                        if (rs.getInt("CP_REQFAC") == 1) {
                            strXMLData += " CP_REQFAC=\'" + "SI" + "\'";
                        } else {
                            strXMLData += " CP_REQFAC=\'" + "NO" + "\'";
                        }
                        if (rs.getInt("CP_PAGO") == 1) {
                            strXMLData += " CP_PAGO=\'" + "SI" + "\'";
                        } else {
                            strXMLData += " CP_PAGO=\'" + "NO" + "\'";
                        }
                        strXMLData += " CP_OBSERVACIONES=\'" + rs.getString("CP_OBSERVACIONES") + "\'";
                        strXMLData += " CP_TITULO=\'" + rs.getString("titulo") + "\'";
                        strXMLData += " CP_ASCOC=\'" + rs.getString("asociacion") + "\'";
                        strXMLData += " CP_CORREO=\'" + rs.getString("CP_CORREO") + "\'";
                        strXMLData += " CP_ID=\'" + rs.getString("CP_ID") + "\'";

                        strXMLData += " LCC_ASOCIACION_ID=\'" + rs.getString("CP_ASCOC") + "\'";
                        strXMLData += " LCC_TITULO_ID=\'" + rs.getString("CP_TITULO") + "\'";

                        strXMLData += "/>";
                    }
                    rs.getStatement().close();
                    rs.close();
                } catch (SQLException ex) {
                    ex.fillInStackTrace();

                }
                strXMLData += "</participantes>";
                String strRes = strXMLData.toString();
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strRes);//Pintamos el resultado

            }//fin confirmar

            if (strid.equals("3")) {
                String strID = request.getParameter("CP_ID");
                String strNombre = request.getParameter("CP_NOMBRE");
                String strAPaterno = request.getParameter("CP_APPAT");
                String strAMaterno = request.getParameter("CP_APMAT");
                strSQL = "update cofide_participantes set CP_NOMBRE = '" + strNombre + "', CP_APPAT = '" + strAPaterno + "', CP_APMAT='" + strAMaterno + "' where CP_ID = " + strID;
                oConn.runQueryLMD(strSQL);
                strResult = oConn.getStrMsgError();
                if (strResult == "") {
                    strResult = "OK";
                }
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(strResult);//Pintamos el resultado
            }

            if (strid.equals("4")) {
                String LCC_NOMBRE = request.getParameter("LCC_NOMBRE");
                String LCC_APPAT = request.getParameter("LCC_APPAT");
                String LCC_APMAT = request.getParameter("LCC_APMAT");
                String LCC_NUMEROSOC = request.getParameter("LCC_NUMEROSOC");
                String LCC_COMENTARIO = request.getParameter("LCC_COMENTARIO");
                String LCC_ASIST = request.getParameter("LCC_ASIST");
                String LCC_FAC = request.getParameter("LCC_FAC");
                String LCC_PAGO = request.getParameter("LCC_PAGO");
                String LCC_OBSERVACIONES = request.getParameter("LCC_OBSERVACIONES");
                String LCC_TITULO = request.getParameter("LCC_TITULO");
                String LCC_ASOCIACION = request.getParameter("LCC_ASOCIACION");
                String LCC_CORREO = request.getParameter("LCC_CORREO");
                String IdCurso = request.getParameter("IdCurso");

                strSQL = "insert into cofide_participantes( CP_APMAT, CP_COMENT, CP_TKT_ID,CP_OBSERVACIONES,CP_NOMBRE,CP_FAC_ID,CP_CORREO,CP_ASCOC,CP_ID_CURSO,CP_APPAT,CP_ASISTENCIA,CP_NOSOCIO,CP_TITULO,CP_REQFAC,CP_PAGO) values "
                        + "('" + LCC_APMAT + "','" + LCC_COMENTARIO + "','0','" + LCC_OBSERVACIONES + "','" + LCC_NOMBRE + "','0','" + LCC_CORREO + "','" + LCC_ASOCIACION + "','" + IdCurso + "','" + LCC_APPAT + "','" + LCC_ASIST + "','" + LCC_NUMEROSOC + "','" + LCC_TITULO + "','" + LCC_FAC + "','" + LCC_PAGO + "');";
                oConn.runQueryLMD(strSQL);
                strResult = oConn.getStrMsgError();
                if (strResult == "") {
                    strResult = "OK";
                }
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(strResult);//Pintamos el resultado
            }

            if (strid.equals("5")) {
                String strObservacion = request.getParameter("observacion");
                String strTitulo = request.getParameter("titulo");
                String strNombre = request.getParameter("nombre");
                String strAppat = request.getParameter("appat");
                String strApmat = request.getParameter("apmat");
                String strIdPart = request.getParameter("id_part");

                strSQL = "update cofide_participantes set CP_OBSERVACIONES='" + strObservacion + "',CP_NOMBRE='" + strNombre + "',CP_APPAT='" + strAppat + "',"
                        + "CP_APMAT='" + strApmat + "' ,CP_TITULO='" + strTitulo + "' "
                        + "where CONTACTO_ID=" + strIdPart + "";
                oConn.runQueryLMD(strSQL);
                if (!oConn.isBolEsError()) {

                    strSQL = "UPDATE cofide_contactos SET CCO_NOMBRE='" + strNombre + "', CCO_APPATERNO='" + strAppat + "', "
                            + "CCO_APMATERNO='" + strApmat + "', CCO_TITULO='" + strTitulo + "' "
                            + "WHERE CONTACTO_ID= " + strIdPart;
                    oConn.runQueryLMD(strSQL);

                    if (!oConn.isBolEsError()) {
                        strResult = "OK";
                    } else {
                        System.out.println("Error al actualizar participantes: [COFIDE_CONTACTO] " + oConn.getStrMsgError());
                    }

                } else {
                    System.out.println("Error al actualizar participantes: [COFIDE_PARTICIPANTE] " + oConn.getStrMsgError());
                }
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(strResult);//Pintamos el resultado
            }
            if (strid.equals("6")) {
                String strCurso_id = request.getParameter("id_curso");
                String strSql = "update cofide_cursos set CC_ESTATUS = 2 where CC_CURSO_ID = " + strCurso_id;
                oConn.runQueryLMD(strSql);
                out.println("OK");
            } //fin6
            if (strid.equals("7")) {
                String id_Curso = request.getParameter("CC_CURSO_ID");
                String strXMLData = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
                strXMLData += "<Participantes>";
                strSQL = "select * from cofide_participantes where CP_ID_CURSO = " + id_Curso;
                try {
                    rs = oConn.runQuery(strSQL, true);
                    while (rs.next()) {
                        strXMLData += "<datos";
                        strXMLData += " CP_NOMBRE=\'" + rs.getString("CP_NOMBRE") + "\'";
                        strXMLData += " CP_APPAT=\'" + rs.getString("CP_APPAT") + "\'";
                        strXMLData += " CP_APMAT=\'" + rs.getString("CP_APMAT") + "\'";
                        strXMLData += " CP_TITULO=\'" + rs.getString("CP_TITULO") + "\'";
                        strXMLData += " CP_NOSOCIO=\'" + rs.getString("CP_NOSOCIO") + "\'";
                        strXMLData += " CP_CORREO=\'" + rs.getString("CP_CORREO") + "\'";
                        strXMLData += " CP_COMENT=\'" + rs.getString("CP_COMENT") + "\'";
                        strXMLData += " CP_ASCOC=\'" + rs.getString("CP_ASCOC") + "\'";
                        strXMLData += " CP_TKT_ID=\'" + rs.getString("CP_TKT_ID") + "\'";
                        strXMLData += " CP_ASISTENCIA=\'" + rs.getString("CP_ASISTENCIA") + "\'";
                        strXMLData += " CP_FAC_ID=\'" + rs.getString("CP_FAC_ID") + "\'";
                        strXMLData += " CP_PAGO=\'" + rs.getString("CP_PAGO") + "\'";
                        strXMLData += " CP_OBSERVACIONES=\'" + rs.getString("CP_OBSERVACIONES") + "\'";
                        strXMLData += "/>";
                    }
                    rs.getStatement().close();
                    rs.close();
                } catch (SQLException ex) {
                    ex.fillInStackTrace();
                }
                strXMLData += "</Participantes>";
                String strRes = strXMLData.toString();
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strRes);//Pintamos el resultado
            } // fin 7
            if (strid.equals("8")) {
                String strMes = "";
                String strAnio = request.getParameter("anio");
                if (request.getParameter("mes").equals("00")) { //si manda mes
                    strMes = fecha.getFechaActual().substring(0, 6);
                    //System.out.println("mes en cero");
                } else {
                    strMes = request.getParameter("anio") + request.getParameter("mes");
                    //System.out.println("manda datos");
                }
                StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
                strXML.append("<vta>");
                String strSql = "select * from cofide_cursos where CC_ESTATUS >= 1 and CC_FECHA_INICIAL >= replace(date_sub('" + strMes + "01', INTERVAL 5 DAY),'-','') order by cc_fecha_inicial";
                rs = oConn.runQuery(strSql, true);
                while (rs.next()) {
                    strXML.append("<datos");
                    strXML.append(" id = \"").append(rs.getString("CC_CURSO_ID")).append("\"");
                    strXML.append(" estatus = \"").append(rs.getString("CC_ESTATUS")).append("\"");
                    strXML.append(" sede = \"").append(rs.getString("CC_SEDE")).append("\"");
                    strXML.append(" fechaini = \"").append(fecha.FormateaDDMMAAAA(rs.getString("CC_FECHA_INICIAL"), " / ")).append("\"");
                    strXML.append(" nombre = \"").append(util.Sustituye(rs.getString("CC_NOMBRE_CURSO"))).append("\"");
                    strXML.append(" />");
                }
                rs.close();
                strXML.append("</vta>");
                strXML.toString();
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML);//Pintamos el resultado 
            } //fin 8
            if (strid.equals("9")) {
                StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
                strXML.append("<vta>");
                String strIdCurso = request.getParameter("id_curso");
                String strSql = "";
                String strNombreC = "";
                String strTipoCurso = "";
                String strColorCurso = "";

                strSql = "select "
                        + "p.CT_ID,"
                        + "(ELT(CP_TIPO_CURSO,'PRESENCIAL','EN LÍNEA','VIDEO CURSO', 'COFIDEnet')) as tipocurso,"
                        + "CONCAT(p.CP_TITULO,' ',p.CP_NOMBRE,' ',p.CP_APPAT,' ',p.CP_APMAT) AS Nombre,"
                        + "if(CP_ASISTENCIA = 1,'ASISTIÓ','NO ASISTIÓ') AS CP_ASISTENCIA,"
                        + "CP_ID,"
                        + "CP_TITULO,"
                        + "CP_NOMBRE,"
                        + "CP_APPAT,"
                        + "CP_APMAT,"
                        + "CONTACTO_ID,"
                        + "CP_ASCOC,"
                        + "CCO_NOSOCIO,"
                        + "CP_FOLIO,"
                        + "CP_OBSERVACIONES,"
                        + "CP_TIPO_CURSO,"
                        + "CT_CLAVE_DDBB as DB,"
                        + "v.FAC_ID,"
                        + "v.FAC_RAZONSOCIAL as RAZONSOCIAL,"
                        + "v.FAC_NOTASPIE as NOTAS,"
                        + "IFNULL(IF(FAC_VALIDA = 1,'PAGADO','PENDIENTE'),'') as Estatus,"
                        + "IF(TIPO_DOC= 'F', FAC_FOLIO_C,FAC_FOLIO) AS FOLIO,"
                        + "IFNULL(IF(TIPO_DOC = 'F','FACTURA',IF(TIPO_DOC = 'T' AND FAC_ES_RESERVACION = 0,'TICKET','RESERVACIÓN')),'') AS documento,"
                        + "nombre_usuario,"
                        + "CC_NOMBRE_CURSO,"
                        + "+c.CC_CURSO_ID,"
                        + "CC_SEDE,"
                        + "CC_SALON "
                        + "from "
                        + "cofide_participantes p "
                        + "LEFT JOIN vta_cliente vta on p.CT_ID = vta.CT_ID "
                        + "LEFT JOIN view_ventasglobales v on IF(CP_TKT_ID = 0, FAC_ID = CP_FAC_ID, FAC_ID = CP_TKT_ID) "
                        + "LEFT JOIN cofide_cursos c on c.CC_CURSO_ID = p.CP_ID_CURSO "
                        + "LEFT JOIN usuarios U ON U.id_usuarios = v.FAC_US_ALTA "
                        + "where "
                        + "FAC_ANULADA = 0 AND "
                        + "CANCEL = 0 AND "
                        + "CP_ID_CURSO = " + strIdCurso + " "
                        + "ORDER BY CP_TIPO_CURSO,CP_APPAT,CP_NOMBRE";

                rs = oConn.runQuery(strSql, true);
                while (rs.next()) {
                    strNombreC = rs.getString("Nombre");
                    strTipoCurso = rs.getString("tipocurso");
                    System.out.println("" + strTipoCurso);

                    if (strTipoCurso.equals("PRESENCIAL")) {
                        strColorCurso = "#99CC00";
                    } else {
                        strColorCurso = "#FFC300";
                    }

                    strXML.append("<datos");
                    strXML.append(" cp_id = \"").append(rs.getString("CP_ID")).append("\"");
                    strXML.append(" titulo = \"").append(util.Sustituye(rs.getString("CP_TITULO"))).append("\"");
                    strXML.append(" nombre = \"").append(util.Sustituye(rs.getString("CP_NOMBRE"))).append("\"");
                    strXML.append(" appat = \"").append(util.Sustituye(rs.getString("CP_APPAT"))).append("\"");
                    strXML.append(" apmat = \"").append(util.Sustituye(rs.getString("CP_APMAT"))).append("\"");
                    strXML.append(" nomcompleto = \"").append(util.Sustituye(strNombreC)).append("\"");
                    strXML.append(" clave = \"").append(rs.getString("DB")).append("\"");
                    strXML.append(" asistencia = \"").append(rs.getString("CP_ASISTENCIA")).append("\"");
                    strXML.append(" razonsocial = \"").append(util.Sustituye(rs.getString("RAZONSOCIAL"))).append("\"");
                    strXML.append(" estatus = \"").append(rs.getString("Estatus")).append("\"");
                    strXML.append(" agente = \"").append(rs.getString("nombre_usuario")).append("\"");
                    strXML.append(" nfac = \"").append(rs.getString("FOLIO")).append("\"");
                    strXML.append(" comentario = \"").append(util.Sustituye(rs.getString("NOTAS"))).append("\"");
                    strXML.append(" asociacion = \"").append(rs.getString("CP_ASCOC")).append("\"");
                    strXML.append(" numero_socio = \"").append(rs.getString("CCO_NOSOCIO")).append("\"");
                    strXML.append(" CONTACTO_ID = \"").append(rs.getString("CONTACTO_ID")).append("\"");
                    strXML.append(" folio = \"").append(rs.getString("CP_FOLIO")).append("\"");
                    strXML.append(" observaciones = \"").append(util.Sustituye(rs.getString("CP_OBSERVACIONES"))).append("\"");
                    strXML.append(" tipo_curso = \"").append(strTipoCurso).append("\"");
                    strXML.append(" color = \"").append(strColorCurso).append("\"");
                    strXML.append(" />");
                }
                rs.close();

                strSQL = "select CT_ID, "
                        + "CP_TITULO,"
                        + "CP_NOMBRE,"
                        + "CP_APPAT,"
                        + "CP_APMAT,"
                        + "if(CP_TIPO_CURSO = 1,'PRESENCIAL','EN LÍNEA') AS tipocurso, "
                        + "CONCAT(CP_TITULO,' ',CP_NOMBRE,' ',CP_APPAT,' ',CP_APMAT) AS Nombre, "
                        + "'CORTESÍA' as Estatus, "
                        + "'0' as FOLIO, "
                        + "'COFIDE S.C.' as  nombre_usuario "
                        + "from cofide_participantes "
                        + "where CP_ID_CURSO = " + strIdCurso + " and CP_TKT_ID = 1";
                rs = oConn.runQuery(strSQL, true);
                while (rs.next()) {

                    strNombreC = rs.getString("Nombre");
                    strTipoCurso = rs.getString("tipocurso");
                    if (strTipoCurso.equals("PRESENCIAL")) {
                        strColorCurso = "#99CC00";
                    } else {
                        strColorCurso = "#FFC300";
                    }

                    strXML.append("<datos");
                    strXML.append(" cp_id = \"").append("0").append("\"");
                    strXML.append(" titulo = \"").append(util.Sustituye(rs.getString("CP_TITULO"))).append("\"");
                    strXML.append(" nombre = \"").append(util.Sustituye(rs.getString("CP_NOMBRE"))).append("\"");
                    strXML.append(" appat = \"").append(util.Sustituye(rs.getString("CP_APPAT"))).append("\"");
                    strXML.append(" apmat = \"").append(util.Sustituye(rs.getString("CP_APMAT"))).append("\"");
                    strXML.append(" nomcompleto = \"").append(util.Sustituye(strNombreC)).append("\"");
                    strXML.append(" clave = \"").append("0").append("\"");
                    strXML.append(" asistencia = \"").append("1").append("\"");
                    strXML.append(" razonsocial = \"").append("COFIDE S.C.").append("\"");
                    strXML.append(" estatus = \"").append(rs.getString("Estatus")).append("\"");
                    strXML.append(" agente = \"").append(rs.getString("nombre_usuario")).append("\"");
                    strXML.append(" nfac = \"").append(rs.getString("FOLIO")).append("\"");
                    strXML.append(" comentario = \"").append("CORTESÍA").append("\"");
                    strXML.append(" asociacion = \"").append("NINGUNA").append("\"");
                    strXML.append(" numero_socio = \"").append("0").append("\"");
                    strXML.append(" CONTACTO_ID = \"").append("0").append("\"");
                    strXML.append(" folio = \"").append("0").append("\"");
                    strXML.append(" observaciones = \"").append("0").append("\"");
                    strXML.append(" tipo_curso = \"").append(strTipoCurso).append("\"");
                    strXML.append(" color = \"").append(strColorCurso).append("\"");
                    strXML.append(" />");

                }
                rs.close();

                strXML.append("</vta>");
                strXML.toString();
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML);//Pintamos el resultado 
            } //fin 9
            if (strid.equals("10")) {
                String strIdPart = request.getParameter("id_participante");
//                String strObservacion = request.getParameter("observacion");
                final String strObservacion = URLDecoder.decode(new String(request.getParameter("observacion").getBytes("iso-8859-1")), "UTF-8");
                String strPago = request.getParameter("pagado");
                String strAsiste = request.getParameter("asiste");
                String strSql = "update cofide_participantes set CP_OBSERVACIONES= '" + strObservacion.replace("'", "\\'") + "', "
                        + "CP_ASISTENCIA = '" + strAsiste + "', CP_PAGO = '" + strPago + "' "
                        + "where CP_ID = " + strIdPart;
                oConn.runQueryLMD(strSql);
                out.println("OK");
            }
            //Con sulta el ultimo Folio para los Diplomas
            if (strid.equals("11")) {
                Folios folio = new Folios();
                int intTipoFolio = Folios.COFIDE_FOLIO;
                String strFolio = "";
                strFolio = folio.getNextFolio(oConn, intTipoFolio, true, 1);
                //strFolio = folio.doFolio(oConn, intTipoFolio, true, 1);
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(strFolio);//Pintamos el resultado;
            }//Fin Id 11

            //Actualiza los participantes con Folio de Diploma e Imprime el reporte
            if (strid.equals("12")) {
                String strIdCursos = request.getParameter("CursoId");
                Folios folio = new Folios();
                int intTipoFolio = Folios.COFIDE_FOLIO;
                String strFolio = "";
                strFolio = folio.getNextFolio(oConn, intTipoFolio, true, 1);
                //strFolio = folio.doFolio(oConn, intTipoFolio, true, 1);
                /*strSQL = "select * from cofide_participantes where CP_ID_CURSO = " + strIdCursos;*/
                strSQL = "SELECT  FAC_ID, CC_NOMBRE_CURSO, CP_ID_CURSO, CP_ID,"
                        + "CONCAT(CP_APPAT, ' ', CP_APMAT, ' ', CP_NOMBRE ) AS NOMBRE,"
                        + "CP_TITULO,CP_NOMBRE, CP_APPAT, CP_APMAT,"
                        + "(select CSH_SEDE from cofide_sede_hotel where csh_id = CC_SEDE_ID) as CC_SEDE,"
                        + "CC_SESION,CP_TIPO_CURSO,"
                        + "if(CP_ASCOC = '', 'NINGUNA', CP_ASCOC) as CP_ASCOC,"
                        + "if(CCO_NOSOCIO = '', 0, CCO_NOSOCIO) as NUMSOCIO,"
                        + "IF(FAC_VALIDA = 1, 'PAGADO','PENDIENTE') AS ESTATUS,"
                        + "if(CP_FAC_ID = 0, 'TICKET', 'FACTURA') as documento,"
                        + "if(TIPO_DOC = 'F', FAC_FOLIO_C, FAC_FOLIO) as FOLIO,"
                        + "view_ventasglobales.CT_ID, FAC_RAZONSOCIAL,"
                        + "(select nombre_usuario from usuarios where id_usuarios = FAC_US_ALTA) as Ejecutivo,"
                        + "(select CT_CLAVE_DDBB from vta_cliente where vta_cliente.CT_ID = view_ventasglobales.CT_ID) as DB,"
                        + "(select PATHIMAGE5 from vta_empresas where vta_empresas.EMP_ID = 1 ) as CabeceroCofide, "
                        + "FAC_NOTAS,CP_FOLIO,CP_OBSERVACIONES "
                        + "FROM cofide_participantes,cofide_cursos, view_ventasglobales "
                        + "WHERE cofide_participantes.CP_ID_CURSO = cofide_cursos.CC_CURSO_ID and FAC_ID = IF(CP_FAC_ID = 0 , CP_TKT_ID, CP_FAC_ID) AND "
                        + "cofide_cursos.CC_CURSO_ID  =  " + strIdCursos + "  and CP_ESTATUS = 1  AND FAC_ANULADA = 0 and CANCEL = 0 "
                        //                        + "GROUP BY CP_ID_CURSO, CP_TITULO, CP_NOMBRE, CP_APPAT, CP_APMAT, CP_CORREO "
                        + "order by CP_APPAT, CP_NOMBRE;";
                try {
                    rs = oConn.runQuery(strSQL, true);
                    while (rs.next()) {
                        if (rs.getString("CP_FOLIO").equals("")) {
                            strFolio = folio.doFolio(oConn, intTipoFolio, true, 1);
                            strSQL = "update cofide_participantes set CP_FOLIO = '" + strFolio + "' where CP_ID = " + rs.getInt("CP_ID");
                            oConn.runQueryLMD(strSQL);
                            if (!oConn.getStrMsgError().equals("")) {
                                strResult = oConn.getStrMsgError();
                                break;
                            }
                        }
                    }
                    rs.close();
                } catch (SQLException ex) {
                    System.out.println("Consulta Participantes [ID:12] . " + ex.getLocalizedMessage());
                }
                if (strResult.equals("")) {
                    strResult = "OK";
                }
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(strResult);//Pintamos el resultado;
            }//Fin Id 12
            if (strid.equals("13")) {
                StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
                strXML.append("<vta>");
                String strIdCurso = request.getParameter("id_curso");
                String strSql = "select concat(CTI_ABRV,' ',UPPER(CI_INSTRUCTOR)) as CI_INSTRUCTOR,cofide_instructor.CI_INSTRUCTOR_ID "
                        + "from cofide_instructor_imparte, cofide_instructor,cofide_titulos "
                        + "where cofide_instructor_imparte.CI_INSTRUCTOR_ID = cofide_instructor.CI_INSTRUCTOR_ID and cofide_titulos.CTI_ID = if(cofide_instructor.CI_TITULO=0,46,cofide_instructor.CI_TITULO) "
                        + "and CC_CURSO_ID = " + strIdCurso;
//                String strSql = "select  UPPER(CI_INSTRUCTOR) as CI_INSTRUCTOR  from cofide_instructor_imparte, cofide_instructor "
//                        + "where cofide_instructor_imparte.CI_INSTRUCTOR_ID = cofide_instructor.CI_INSTRUCTOR_ID "
//                        + "and CC_CURSO_ID = " + strIdCurso;
                rs = oConn.runQuery(strSql, true);
                while (rs.next()) {
                    strXML.append("<datos");
                    strXML.append(" instructor_id = \"").append(rs.getString("CI_INSTRUCTOR_ID")).append("\"");
                    strXML.append(" instructor = \"").append(rs.getString("CI_INSTRUCTOR")).append("\"");
                    strXML.append(" />");
                }
                rs.close();
                strXML.append("</vta>");
                strXML.toString();
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML);//Pintamos el resultado 
            } //13

            if (strid.equals("14")) {
                String strCC_CURSO_ID = request.getParameter("ID_CURSO");
                String strSql = "select * from vta_tickets where CC_CURSO_ID = " + strCC_CURSO_ID + " "
                        + "and TKT_ES_RESERVACION = 0 and TKT_ANULADA = 0 and TKT_CANCEL = 0 "
                        + "and left(TKT_FECHA,6) >= '201705'";
                Boolean blExiste = false;
                try {
                    rs = oConn.runQuery(strSql, true);
                    while (rs.next()) {
                        blExiste = true;
                    }
                    rs.close();
                } catch (SQLException ex) {

                }

                if (blExiste) {
                    strResult = "EXISTE";
                } else {
                    strResult = "NO EXISTE";
                }
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(strResult);//Pintamos el resultado
            }//Fin ID 14
        }
    } else {
        out.println("Sin acceso");
    }
    oConn.close();

%>


<%!
    public String getFolio(int caso, int idMovimiento, Conexion oConn) {

        oConn.open();
        String strFolio = "";
        //Caso Factura
        if (caso == 1) {
            String strSQL = "select FAC_FOLIO from vta_facturas where FAC_ID = " + idMovimiento;
            try {
                ResultSet rs = oConn.runQuery(strSQL, true);
                while (rs.next()) {
                    strFolio = rs.getString("FAC_FOLIO");
                }
                rs.getStatement().close();
                rs.close();
            } catch (SQLException ex) {
                ex.fillInStackTrace();
            }
        }

        //Caso Ticket
        if (caso == 2) {
            String strSQL = "select TKT_FOLIO from vta_tickets where TKT_ID = " + idMovimiento;
            try {
                ResultSet rs = oConn.runQuery(strSQL, true);
                while (rs.next()) {
                    strFolio = rs.getString("TKT_FOLIO");
                }
                rs.getStatement().close();
                rs.close();
            } catch (SQLException ex) {
                ex.fillInStackTrace();
            }
        }

        return strFolio;
    }

    /**
     * Envia el mail al cliente
     *
     * @param strMailCte Es el mail del cliente
     * @param strMailCte2 Es el segundo mail del cliente
     * @param strFolio Es el folio
     * @param strPath Es el path donde se alojara temporalmente el pdf
     * @return Regresa OK si fue exitoso el envio del mail
     */
    protected String GeneraMail(Conexion oConn, String strMailCte, String strMailCte2, String PARTICIPANTE, String NOM_CURSO) {
        String strResp = "OK";
        //Nombre de archivo
        //Obtenemos datos del smtp
        String strsmtp_server = "";
        String strsmtp_user = "";
        String strsmtp_pass = "";
        String strsmtp_port = "";
        String strsmtp_usaTLS = "";
        String strsmtp_usaSTLS = "";
        //Buscamos los datos del SMTP
        String strSql = "select * from cuenta_contratada where ctam_id = 1";
        ResultSet rs;
        try {
            rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                strsmtp_server = rs.getString("smtp_server");
                strsmtp_user = rs.getString("smtp_user");
                strsmtp_pass = rs.getString("smtp_pass");
                strsmtp_port = rs.getString("smtp_port");
                strsmtp_usaTLS = rs.getString("smtp_usaTLS");
                strsmtp_usaSTLS = rs.getString("smtp_usaSTLS");
            }
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        //Obtenemos los textos para el envio del mail
        String strNomTemplate = "COFI_CERRCURSO";
        String[] lstMail = getMailTemplate(oConn, strNomTemplate);

        /**
         * Si estan llenos todos los datos mandamos el mail
         */
        if (!strsmtp_server.equals("")
                && !strsmtp_user.equals("")
                && !strsmtp_pass.equals("")) {
            //armamos el mail
            Mail mail = new Mail();
            mail.setBolDepuracion(false);
            //Activamos envio de acuse de recibo
            mail.setBolAcuseRecibo(true);
            //Obtenemos los usuarios a los que mandaremos el mail
            String strLstMail = "";
            //Validamos si el mail del cliente es valido
            if (mail.isEmail(strMailCte)) {
                strLstMail += "," + strMailCte;
            }
            if (mail.isEmail(strMailCte2)) {
                strLstMail += "," + strMailCte2;
            }
            //Mandamos mail si hay usuarios
            if (!strLstMail.equals("")) {
                String strMsgMail = lstMail[1];
                strMsgMail = strMsgMail.replace("%PARTICIPANTE%", PARTICIPANTE);
                strMsgMail = strMsgMail.replace("%NOM_CURSO%", NOM_CURSO);
                //Establecemos parametros
                mail.setUsuario(strsmtp_user);
                mail.setContrasenia(strsmtp_pass);
                mail.setHost(strsmtp_server);
                mail.setPuerto(strsmtp_port);
                mail.setAsunto(lstMail[0].replace("%NOM_CURSO%", NOM_CURSO));
                mail.setDestino(strLstMail);
                mail.setMensaje(strMsgMail);
                //Adjuntamos XML y PDF
                if (strsmtp_usaTLS.equals("1")) {
                    mail.setBolUsaTls(true);
                }
                if (strsmtp_usaSTLS.equals("1")) {
                    mail.setBolUsaStartTls(true);
                }
                boolean bol = mail.sendMail();
                if (!bol) {
                    strResp = "Fallo el envio del Mail.";
                }
            }
        }
        return strResp;
    }

    /**
     * Obtenemos los valores del template para el mail
     *
     * @param strNom Es el nombre del template
     * @return Regresa un arreglo con los valores del template
     */
    public String[] getMailTemplate(Conexion oConn, String strNom) {
        String[] listValores = new String[2];
        String strSql = "select MT_ASUNTO,MT_CONTENIDO from mailtemplates where MT_ABRV ='" + strNom + "'";
        ResultSet rs;
        try {
            rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                listValores[0] = rs.getString("MT_ASUNTO");
                listValores[1] = rs.getString("MT_CONTENIDO");
            }
            rs.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return listValores;
    }

%>