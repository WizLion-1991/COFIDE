<%-- 
    Document   : COFIDE_DiplomadoSeminario
    Created on : 12-nov-2016, 22:12:34
    Author     : Fernando
--%>
<%@page import="comSIWeb.ContextoApt.atrJSP"%>
<%@page import="java.sql.SQLException"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="comSIWeb.Utilerias.Fechas"%>
<%@page import="comSIWeb.Utilerias.UtilXml"%>
<%@page import="comSIWeb.ContextoApt.Seguridad"%>
<%@page import="comSIWeb.Operaciones.Conexion"%>
<%@page import="comSIWeb.ContextoApt.VariableSession"%>
<%
    /*Obtenemos las variables de sesion*/
    VariableSession varSesiones = new VariableSession(request);
    varSesiones.getVars();
    //Abrimos la conexion
    Conexion oConn = new Conexion(varSesiones.getStrUser(), this.getServletContext());
    oConn.open();
    //Instanciamos objeto de seguridad para no permitir el acceso externo a ver los XML
    Seguridad seg = new Seguridad();//Valida que la peticion se halla hecho desde el mismo sitio
    UtilXml util = new UtilXml();
    Fechas fec = new Fechas();
    String strRes = "";
    String strSql = "";
    ResultSet rs = null;

    if (varSesiones.getIntNoUser() != 0 && seg.ValidaURL(request)) {
        String strid = request.getParameter("id");
        if (strid != null) {

            //Consulta el reporte facturas hechas para cursos que son Diplomados o Seminarios
            if (strid.equals("1")) {
                String strMes = request.getParameter("strMes");
                String strAnio = request.getParameter("strAnio");
                String strFechaPago = "";
                String strFechaInicial = "";
                String strAplicaBono = "";
                String strTipoCurso = "";
                StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
                strXML.append("<DiplomadoSeminario>");
                if (strMes.equals("0")) {
                    strMes = fec.getFechaActual().substring(4, 6);
                }
                strSql = "select id_usuarios,nombre_usuario,TMK_CLAVE,US_GRUPO,"
                        + "(select CG_DESCRIPCION from cofide_grupo_trabajo where CG_ID = US_GRUPO) as EQUIPO,"
                        + "FAC_ID,FAC_FOLIO_C,CC_FECHA_INICIAL,FAC_TIPO_CURSO,CC_NOMBRE_CURSO,"
                        + "FAC_US_ALTA,TIPO_DOC,(FAC_IMPORTE + FAC_DESCUENTO) as IMPORTE,FAC_DESCUENTO,"
                        + "FAC_IMPORTE as TOTAL,FAC_FECHA_COBRO,FAC_IMP_PAGADO,FAC_VALIDA,FAC_PAGADO,"
                        + "(select CSD_APLICA from cofide_comision_semdip where FAC_ID = view_ventasglobales.FAC_ID) as APLICA_BONO "
                        + "from view_ventasglobales, cofide_cursos, usuarios "
                        + "where view_ventasglobales.CC_CURSO_ID = cofide_cursos.CC_CURSO_ID and FAC_US_ALTA = id_usuarios "
                        + "and left(CC_FECHA_INICIAL,4) = '" + strAnio + "'  "
                        + "and mid(CC_FECHA_INICIAL,5,2) = '" + strMes + "' "
                        + "and (CC_IS_DIPLOMADO = 1 or CC_IS_SEMINARIO = 1) "
                        + "and FAC_VALIDA = 1 "
                        + "and FAC_PAGADO = 1 "
                        + "and FAC_ANULADA = 0";
                try {
                    rs = oConn.runQuery(strSql, true);
                    while (rs.next()) {
                        strFechaPago = "";
                        if (rs.getString("FAC_FECHA_COBRO") != null && !rs.getString("FAC_FECHA_COBRO").equals("")) {
                            strFechaPago = fec.FormateaDDMMAAAA(rs.getString("FAC_FECHA_COBRO"), "/");
                        }
                        strFechaInicial = "";
                        if (rs.getString("CC_FECHA_INICIAL") != null && !rs.getString("CC_FECHA_INICIAL").equals("")) {
                            strFechaInicial = fec.FormateaDDMMAAAA(rs.getString("CC_FECHA_INICIAL"), "/");
                        }
                        double tmpBono = 0;
                        double tmpImporte = 0;

                        tmpImporte = rs.getDouble("TOTAL");
                        if (tmpImporte > 0 && tmpImporte < 5000) {
                            tmpBono = 200;
                        } else {
                            if (tmpImporte >= 5001 && tmpImporte < 10000) {
                                tmpBono = 300;
                            } else {
                                if (tmpImporte >= 10001 && tmpImporte < 15000) {
                                    tmpBono = 500;
                                } else {
                                    if (tmpImporte >= 15001) {
                                        tmpBono = 700;
                                    }
                                }
                            }
                        }
                        strAplicaBono = "";
                        if (rs.getString("APLICA_BONO") != null) {
                            if (rs.getString("APLICA_BONO").equals("1")) {
                                strAplicaBono = "APLICA";
                            } else {
                                strAplicaBono = "NO APLICA";
                            }
                        }

                        strTipoCurso = "";
                        if (rs.getString("FAC_TIPO_CURSO").equals("1")) {
                            strTipoCurso = "PRESENCIAL";
                        } else {
                            if (rs.getString("FAC_TIPO_CURSO").equals("2")) {
                                strTipoCurso = "EN LINEA";
                            } else {
                                if (rs.getString("FAC_TIPO_CURSO").equals("3")) {
                                    strTipoCurso = "RETRANSMISION";
                                }
                            }
                        }

                        strXML.append("<datos");
                        strXML.append(" TIPO_CURSO = \"").append(strTipoCurso).append("\"");
                        strXML.append(" CC_NOMBRE_CURSO = \"").append(util.Sustituye(rs.getString("CC_NOMBRE_CURSO"))).append("\"");
                        strXML.append(" CC_FECHA_INICIAL = \"").append(strFechaInicial).append("\"");
                        strXML.append(" TMK_CLAVE = \"").append((rs.getString("TMK_CLAVE") != null ? util.Sustituye(rs.getString("TMK_CLAVE")) : "")).append("\"");
                        strXML.append(" EQUIPO = \"").append((rs.getString("EQUIPO") != null ? util.Sustituye(rs.getString("EQUIPO")) : "")).append("\"");
                        strXML.append(" nombre_usuario = \"").append((rs.getString("nombre_usuario") != null ? util.Sustituye(rs.getString("nombre_usuario")) : "")).append("\"");
                        strXML.append(" TIPO_DOC = \"").append((rs.getString("TIPO_DOC").equals("T") ? "TICKET" : "FACTURA")).append("\"");
                        strXML.append(" FAC_FOLIO = \"").append(rs.getString("FAC_FOLIO_C")).append("\"");
                        strXML.append(" IMPORTE = \"").append(rs.getDouble("IMPORTE")).append("\"");
                        strXML.append(" FAC_DESCUENTO = \"").append(rs.getDouble("FAC_DESCUENTO")).append("\"");
                        strXML.append(" TOTAL = \"").append(rs.getDouble("TOTAL")).append("\"");
                        strXML.append(" FAC_FECHA_COBRO = \"").append(strFechaPago).append("\"");
                        strXML.append(" FAC_IMP_PAGADO = \"").append(rs.getDouble("FAC_IMP_PAGADO")).append("\"");
                        strXML.append(" IMP_BONO = \"").append(tmpBono).append("\"");
                        strXML.append(" APLICA_BONO = \"").append(strAplicaBono).append("\"");
                        strXML.append(" FAC_ID = \"").append(rs.getString("FAC_ID")).append("\"");
                        strXML.append(" />");
                    }
                    rs.close();
                    strXML.append("</DiplomadoSeminario>");
                } catch (SQLException ex) {
                    System.out.println("Error GetConsulta Diplomados, Seminarios " + ex.getLocalizedMessage());
                }
                strXML.toString();
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML);//Pintamos el resultado
            }//Fin ID 1

            //Agrega los Bonos Pagados o Denegados
            if (strid.equals("2")) {
                String strFAC_ID = request.getParameter("FAC_ID");
                String strAplica = request.getParameter("APLICA");
                String strFechaActual = fec.getFechaActual();
                String str = strFAC_ID;
                String delimiter = ",";
                String[] temp;
                temp = str.split(delimiter);

                for (int i = 0; i < temp.length; i++) {
                    strSql = "delete from cofide_comision_semdip where FAC_ID = " + temp[i];
                    oConn.runQueryLMD(strSql);

                    strSql = "insert into cofide_comision_semdip ( CSD_FECHA_REG, CSD_APLICA, FAC_ID) values ( '" + strFechaActual + "', " + strAplica + ", " + temp[i] + ")";
                    oConn.runQueryLMD(strSql);
                }
                if (oConn.getStrMsgError().equals("")) {
                    strRes = "OK";
                } else {
                    strRes = oConn.getStrMsgError();
                }

                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(strRes);//Pintamos el resultado
            }//Fin ID 2

        }
        oConn.close();
    } else {
        out.print("Sin Acceso");
    }
%>