<%-- 
    Document   : COFIDE_Pago_Instructores
    Created on : 21-nov-2016, 18:56:27
    Author     : Fernando!
--%>

<%@page import="com.mx.siweb.erp.especiales.cofide.SincronizarPaginaWeb"%>
<%@page import="java.sql.SQLException"%>
<%@page import="comSIWeb.ContextoApt.atrJSP"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="comSIWeb.Utilerias.Fechas"%>
<%@page import="comSIWeb.Utilerias.UtilXml"%>
<%@page import="comSIWeb.ContextoApt.Seguridad"%>
<%@page import="comSIWeb.Operaciones.Conexion"%>
<%@page import="comSIWeb.ContextoApt.VariableSession"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    /*Obtenemos las variables de sesion*/
    VariableSession varSesiones = new VariableSession(request);
    varSesiones.getVars();
    //Abrimos la conexion
    Conexion oConn = new Conexion(varSesiones.getStrUser(), this.getServletContext());
    oConn.open();
    //Instanciamos objeto de seguridad para no permitir el acceso externo a ver los XML
    Seguridad seg = new Seguridad();//Valida que la peticion se halla hecho desde el mismo sitio
    UtilXml utils = new UtilXml();
    Fechas fecha = new Fechas();
    String strRes = "";
    //sincronizar la pagina web, para actualizar crm-web
    SincronizarPaginaWeb sincroniza = new SincronizarPaginaWeb(oConn);
    if (varSesiones.getIntNoUser() != 0 && seg.ValidaURL(request)) {
        String strid = request.getParameter("ID");
        if (strid != null) {

            //Consulta el listado de pago  a instructores
            if (strid.equals("1")) {
                String strFecha1 = request.getParameter("FECHA_1");
                String strFecha2 = request.getParameter("FECHA_2");
                String strFechaInicial = "";
                String strObservaciones = "";

                strFecha1 = fecha.FormateaBD(strFecha1, "/");
                strFecha2 = fecha.FormateaBD(strFecha2, "/");

                StringBuilder strXMLData = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
                strXMLData.append("<rows>");
                strXMLData.append("<page>1</page>");
                strXMLData.append("<total>1</total>");
                strXMLData.append("<records>1000</records>");

                String strSql = "select cofide_instructor_imparte.CI_INSTRUCTOR_ID,cofide_instructor.CI_INSTRUCTOR,"
                        + "round(cofide_instructor_imparte.II_NUM_HORAS,1) as II_NUM_HORAS,"
                        + "cofide_cursos.CC_SEDE_ID,cofide_sede_hotel.CSH_SEDE,cofide_sede_hotel.CSH_FORANEO,"
                        + "(select PO_OBSERVACIONES from cofide_instructores_pago_observaciones as CIPO "
                        + "where CIPO.CI_INSTRUCTOR_ID = cofide_instructor_imparte.CI_INSTRUCTOR_ID and CIPO.CC_CURSO_ID = cofide_cursos.CC_CURSO_ID)as OBSERVACIONES,"
                        + "cofide_cursos.CC_CURSO_ID,cofide_cursos.CC_NOMBRE_CURSO,cofide_cursos.CC_FECHA_INICIAL, "
                        + "cofide_cursos.CC_HR_EVENTO_INI,"
                        + "(select count(*) from cofide_participantes where CP_TIPO_CURSO = 1 and CP_ASISTENCIA = 1 and CP_ID_CURSO = cofide_cursos.CC_CURSO_ID) as AsistentesPresencial,"
                        + "(select count(*) from cofide_participantes where CP_TIPO_CURSO = 2 and CP_ASISTENCIA = 1 and CP_ID_CURSO = cofide_cursos.CC_CURSO_ID) as AsistentesEnLinea,"
                        + "(select CIP.CIP_FECHA_PAGO from cofide_instructor_pago as CIP where CIP.CI_INSTRUCTOR_ID = cofide_instructor_imparte.CI_INSTRUCTOR_ID "
                        + "and CIP.CC_CURSO_ID = cofide_cursos.CC_CURSO_ID) as FECHA_PAGADO,"
                        + "(select CIP.CIP_IMPORTE_PAGO from cofide_instructor_pago as CIP where CIP.CI_INSTRUCTOR_ID = cofide_instructor_imparte.CI_INSTRUCTOR_ID "
                        + "and CIP.CC_CURSO_ID = cofide_cursos.CC_CURSO_ID) as IMPORTE_EXTRA,"
                        + "(select CIP.CIP_ESTATUS from cofide_instructor_pago as CIP where CIP.CI_INSTRUCTOR_ID = cofide_instructor_imparte.CI_INSTRUCTOR_ID "
                        + "and CIP.CC_CURSO_ID = cofide_cursos.CC_CURSO_ID) as CIP_ESTATUS, "
                        + "(select CIP.NUM_FACTURA from cofide_instructor_pago as CIP where CIP.CI_INSTRUCTOR_ID = cofide_instructor_imparte.CI_INSTRUCTOR_ID "
                        + "and CIP.CC_CURSO_ID = cofide_cursos.CC_CURSO_ID) as NUM_FACTURA "
                        + "from cofide_instructor_imparte,cofide_instructor,cofide_cursos,cofide_sede_hotel  "
                        + "where "
                        + "cofide_instructor_imparte.CI_INSTRUCTOR_ID = cofide_instructor.CI_INSTRUCTOR_ID "
                        + "and cofide_instructor_imparte.CC_CURSO_ID = cofide_cursos.CC_CURSO_ID "
                        + "and cofide_cursos.CC_SEDE_ID = cofide_sede_hotel.CSH_ID "
                        + "and cofide_cursos.CC_FECHA_INICIAL >= '" + strFecha1 + "'  "
                        + "and cofide_cursos.CC_FECHA_INICIAL <= '" + strFecha2 + "' "
                        + "and cofide_cursos.CC_ESTATUS = 2 "
                        + "order by cofide_cursos.CC_CURSO_ID";

                ResultSet rs = oConn.runQuery(strSql, true);
                int intTotalAsistentes = 0;
                int intTotalAsistentesPres = 0;
                int intTotalAsistentesOnline = 0;
                double dblNumHoras = 0.0;
                double dblCostoInst = 0.0;
                double dblImportePagar = 0.0;
                double dblImporteExtra = 0.0;
                double dblTotalImpporte = 0.0;
                String strFechaPago = "";
                String strEstatusPago = "";
                String strSedeDesc = "";
                int intContador = 0;
                while (rs.next()) {
                    intContador = intContador + 1;
                    intTotalAsistentes = 0;
                    intTotalAsistentesPres = 0;
                    intTotalAsistentesOnline = 0;
                    dblNumHoras = 0.0;
                    dblCostoInst = 0.0;
                    dblImportePagar = 0.0;
                    dblImporteExtra = 0.0;
                    dblTotalImpporte = 0.0;
                    strFechaInicial = "";
                    strObservaciones = "";
                    strSedeDesc = "";
                    strFechaPago = "";
                    strEstatusPago = "";
                    if (rs.getString("CC_FECHA_INICIAL") != null && !rs.getString("CC_FECHA_INICIAL").equals("")) {
                        strFechaInicial = fecha.FormateaDDMMAAAA(rs.getString("CC_FECHA_INICIAL"), "/");
                    }
                    if (rs.getString("AsistentesPresencial") != null) {
                        intTotalAsistentesPres = rs.getInt("AsistentesPresencial");
                    }
                    if (rs.getString("AsistentesEnLinea") != null) {
                        intTotalAsistentesOnline = rs.getInt("AsistentesEnLinea");
                    }
                    intTotalAsistentes = intTotalAsistentesPres + intTotalAsistentesOnline;

                    if (rs.getString("II_NUM_HORAS") != null) {
                        dblNumHoras = rs.getDouble("II_NUM_HORAS");
                    }

                    if (rs.getString("IMPORTE_EXTRA") != null) {
                        dblImporteExtra = rs.getDouble("IMPORTE_EXTRA");
                    }
                    if (rs.getString("OBSERVACIONES") != null) {
                        strObservaciones = utils.Sustituye(rs.getString("OBSERVACIONES"));
                    }
                    String strNum_FACTURA = "";
                    if (rs.getString("NUM_FACTURA") != null) {
                        strNum_FACTURA = utils.Sustituye(rs.getString("NUM_FACTURA"));
                    }
                    strSedeDesc = "";
                    if (rs.getString("CSH_SEDE") != null) {
                        strSedeDesc = utils.Sustituye(rs.getString("CSH_SEDE"));
                    }

                    dblCostoInst = getCostoInstructor(oConn, rs.getString("CI_INSTRUCTOR_ID"), intTotalAsistentes, rs.getString("CSH_FORANEO"));
                    dblImportePagar = dblCostoInst * intTotalAsistentes;

                    dblTotalImpporte = dblImporteExtra + dblImportePagar;

                    if (rs.getString("FECHA_PAGADO") != null && !rs.getString("FECHA_PAGADO").equals("")) {
                        strFechaPago = fecha.FormateaDDMMAAAA(rs.getString("FECHA_PAGADO"), "/");
                    }

                    if (rs.getString("CIP_ESTATUS") != null) {
                        if (rs.getString("CIP_ESTATUS").equals("1")) {
                            strEstatusPago = "PAGADO";
                        }
                    }

                    strXMLData.append("<row id='" + intContador + "'>");
                    strXMLData.append("<cell>" + strFechaInicial + "</cell>");
                    strXMLData.append("<cell>" + utils.Sustituye(rs.getString("CC_HR_EVENTO_INI")) + "</cell>");
                    strXMLData.append("<cell>" + utils.Sustituye(rs.getString("CC_NOMBRE_CURSO")) + "</cell>");
                    strXMLData.append("<cell>" + utils.Sustituye(strSedeDesc) + "</cell>");
                    strXMLData.append("<cell>" + utils.Sustituye(rs.getString("CI_INSTRUCTOR")) + "</cell>");
                    strXMLData.append("<cell>" + utils.Sustituye(strObservaciones) + "</cell>");
                    strXMLData.append("<cell>" + utils.Sustituye(strNum_FACTURA) + "</cell>");
                    strXMLData.append("<cell>" + utils.Sustituye(rs.getString("CC_CURSO_ID")) + "</cell>");
                    strXMLData.append("<cell>" + intTotalAsistentes + "</cell>");
                    strXMLData.append("<cell>" + intTotalAsistentesPres + "</cell>");
                    strXMLData.append("<cell>" + intTotalAsistentesOnline + "</cell>");
                    strXMLData.append("<cell>" + dblNumHoras + "</cell>");
                    strXMLData.append("<cell>" + dblCostoInst + "</cell>");
                    strXMLData.append("<cell>" + dblImportePagar + "</cell>");
                    strXMLData.append("<cell>" + dblImporteExtra + "</cell>");
                    strXMLData.append("<cell>" + dblTotalImpporte + "</cell>");
                    strXMLData.append("<cell>" + strFechaPago + "</cell>");
                    strXMLData.append("<cell>" + strEstatusPago + "</cell>");
                    strXMLData.append("<cell>" + rs.getString("CI_INSTRUCTOR_ID") + "</cell>");
                    strXMLData.append("</row>\n");
                }
                rs.close();
                strXMLData.append("</rows>");
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXMLData.toString());//Mandamos a pantalla el resultado
            }//Fin ID 1

            //Guarda los cambios del monto pagado al instructor
            if (strid.equals("2")) {
                String strIdCurso = request.getParameter("CURSO_ID");
                String strIdInstructor = request.getParameter("INSTRUCTOR_ID");
                String strImporteAjustado = request.getParameter("IMPORTE_EXTRA");
                String strFechaPago = request.getParameter("FECHA_PAGO");
                String strFactura = request.getParameter("NUM_FACTURA");

                strFechaPago = fecha.FormateaBD(strFechaPago, "/");

                strRes = setPagoInstructor(oConn, strIdCurso, strIdInstructor, strFechaPago, strFactura, strImporteAjustado);
                if (strRes.equals("")) {
                    strRes = "OK";
                }
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(strRes);//Mandamos a pantalla el resultado
            }//Fin ID 2

            if (strid.equals("3")) {
                String strIdCurso = request.getParameter("CURSO_ID");
                String strIdInstructor = request.getParameter("INSTRUCTOR_ID");
                String strObservaciones = request.getParameter("OBSERVACIONES");

                strObservaciones = utils.Sustituye(strObservaciones);

                strSql = "insert into cofide_instructores_pago_observaciones "
                        + "( CI_INSTRUCTOR_ID,CC_CURSO_ID, PO_OBSERVACIONES) "
                        + "values ( " + strIdInstructor + ", " + strIdCurso + ", '" + strObservaciones + "')";

                oConn.runQueryLMD(strSql);

                if (strRes.equals("")) {
                    strRes = "OK";
                }
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(strRes);//Mandamos a pantalla el resultado
            }//Fin ID 3
            if (strid.equals("4")) {
                String strIdInstructor = request.getParameter("ID_INSTRC");
                if (!strIdInstructor.equals("")) {
                    strRes = sincroniza.ActualizaExpocitor(strIdInstructor);
                } else {
                    System.out.println("No se encontro el instructor");
                    strRes = "Fallo la actualización en la web";
                }
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(strRes);//Mandamos a pantalla el resultado
            }//Fin ID 4
        }
    } else {
        out.print("Sin Acceso");
    }
    oConn.close();
%>
<%!
    String strSql = "";
    ResultSet rs1 = null;

    public double getCostoInstructor(Conexion oConn, String strCI_INSTRUCTOR_ID, int intTotalAsistentes, String strForaneo) {
        strSql = "select * from cofide_instructor where CI_INSTRUCTOR_ID = " + strCI_INSTRUCTOR_ID;
        int intRango1 = 0;
        int intRango2 = 0;
        int intRango3 = 0;
        int intRango4 = 0;
        int intRango5 = 0;
        int intRango6 = 0;
        int intRango7 = 0;
        int intRango8 = 0;
        double dblCosto1 = 0.0;
        double dblCosto2 = 0.0;
        double dblCosto3 = 0.0;
        double dblCosto4 = 0.0;
        double dblCostoInst = 0.0;
        try {
            rs1 = oConn.runQuery(strSql, true);
            while (rs1.next()) {
                intRango1 = rs1.getInt("CI_RANGOA_1");
                /* A */ intRango2 = rs1.getInt("CI_RANGOB_1");
                intRango3 = rs1.getInt("CI_RANGOA_2");
                /* A */ intRango4 = rs1.getInt("CI_RANGOB_2");
                intRango5 = rs1.getInt("CI_RANGOA_3");
                /* A */ intRango6 = rs1.getInt("CI_RANGOB_3");
                intRango7 = rs1.getInt("CI_RANGOA_4");
                /* A */ intRango8 = rs1.getInt("CI_RANGOB_4");
                if (strForaneo.equals("1")) {
                    dblCosto1 = rs1.getDouble("CI_COSTO_FORANEO1");
                    dblCosto2 = rs1.getDouble("CI_COSTO_FORANEO1");
                    dblCosto3 = rs1.getDouble("CI_COSTO_FORANEO1");
                    dblCosto4 = rs1.getDouble("CI_COSTO_FORANEO1");
                } else {
                    dblCosto1 = rs1.getDouble("CI_COSTO_HR1");
                    dblCosto2 = rs1.getDouble("CI_COSTO_HR2");
                    dblCosto3 = rs1.getDouble("CI_COSTO_HR3");
                    dblCosto4 = rs1.getDouble("CI_COSTO_HR4");
                }

            }
            rs1.close();
        } catch (SQLException ex) {
            System.out.println("Get Costo Instructor ERROR [: " + ex.getLocalizedMessage() + "].");
        }

        //Revisamos el Rango en el cual se le paga al instructor
        if (intTotalAsistentes >= intRango1 && intTotalAsistentes <= intRango2) {
            dblCostoInst = dblCosto1;
        } else {
            if (intTotalAsistentes >= intRango3 && intTotalAsistentes <= intRango4) {
                dblCostoInst = dblCosto2;
            } else {
                if (intTotalAsistentes >= intRango5 && intTotalAsistentes <= intRango6) {
                    dblCostoInst = dblCosto3;
                } else {
                    if (intTotalAsistentes >= intRango7 && intTotalAsistentes <= intRango8) {
                        dblCostoInst = dblCosto4;
                    }
                }
            }
        }
        return dblCostoInst;
    }//Fin getCostoInstructor

    public String setPagoInstructor(Conexion oConn, String strCursoId, String strInstructorId,
            String strFechaPago, String strNumFactura, String strImporteExtra) {
        String strRes = "";

        strSql = "delete from cofide_instructor_pago where CI_INSTRUCTOR_ID = " + strInstructorId + " and CC_CURSO_ID = " + strCursoId;

        oConn.runQueryLMD(strSql);

        strSql = "insert into cofide_instructor_pago "
                + "(CC_CURSO_ID, NUM_FACTURA, CI_INSTRUCTOR_ID, CIP_FECHA_PAGO, CIP_IMPORTE_PAGO, CIP_ESTATUS)"
                + " values ( " + strCursoId + ", '" + strNumFactura + "', " + strInstructorId + ", '" + strFechaPago + "', " + strImporteExtra + ", '1')";

        oConn.runQueryLMD(strSql);
        if (!oConn.getStrMsgError().equals("")) {
            strRes = oConn.getStrMsgError();
        }
        return strRes;
    }

%>