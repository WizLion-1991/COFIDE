<%-- 
    Document   : COFIDE_Auto_curso
    Created on : 02-feb-2016, 16:24:32
    Author     : juliocesar
--%>

<%@page import="java.net.URLDecoder"%>
<%@page import="comSIWeb.Utilerias.UtilXml"%>
<%@page import="com.google.common.base.Ascii"%>
<%@page import="com.siweb.utilerias.json.JSONArray"%>
<%@page import="com.siweb.utilerias.json.JSONObject"%>
<%@ page import="comSIWeb.ContextoApt.VariableSession" %>
<%@ page import="comSIWeb.ContextoApt.atrJSP" %>
<%@ page import="comSIWeb.ContextoApt.Seguridad" %>
<%@ page import="comSIWeb.Operaciones.CIP_Form" %>
<%@ page import="Tablas.Usuarios" %>
<%@ page import="comSIWeb.Operaciones.Conexion" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>


<%
    /*Obtenemos las variables de sesion*/
    VariableSession varSesiones = new VariableSession(request);
    varSesiones.getVars();

    UtilXml util = new UtilXml();
    String strRes = "";

    //Abrimos la conexion
    Conexion oConn = new Conexion(varSesiones.getStrUser(), this.getServletContext());
    oConn.open();
    //Instanciamos objeto de seguridad para no permitir el acceso externo a ver los XML
    Seguridad seg = new Seguridad();//Valida que la peticion se halla hecho desde el mismo sitio
    if (varSesiones.getIntNoUser() != 0 && seg.ValidaURL(request)) {
        /*Definimos parametros de la aplicacion*/
        //String strValorBuscar = request.getParameter("term");
        String strId = request.getParameter("ID");
        if (strId != null) {
            if (strId.equals("1")) {
                String strValorBuscar = Ascii.toUpperCase(request.getParameter("term"));
                if (strValorBuscar == null) {
                    strValorBuscar = "";
                }
                //Declaramos objeto json
                JSONArray jsonChild = new JSONArray();
                if (!strValorBuscar.trim().equals("")) {
                    //Busca el valor en la tabla de beneficiarios....
//                    String strSql = "SELECT DISTINCT CEC_CURSO1 "
//                            + "FROM cofide_ev_cursos where "
//                            + "CEC_CURSO1 LIKE '%" + strValorBuscar + "%' ORDER BY CEC_CURSO1";

                    String strSql = "select DISTINCT NombreCurso as CEC_CURSO1 "
                            + "from view_cursos "
                            + "where NombreCurso LIKE '%" + strValorBuscar + "%' order by CEC_CURSO1";
                    
                    ResultSet rsCombo;
                    try {
                        rsCombo = oConn.runQuery(strSql, true);
                        while (rsCombo.next()) {
                            String strCurso1 = rsCombo.getString("CEC_CURSO1");
                            JSONObject objJson = new JSONObject();
                            objJson.put("value", strCurso1);
                            objJson.put("label", strCurso1);
                            jsonChild.put(objJson);
                        }
                        rsCombo.close();
                    } catch (SQLException ex) {
                        ex.fillInStackTrace();
                    }
                }
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(jsonChild.toString());//Pintamos el resultado
            }
            if (strId.equals("2")) {
                String strValorBuscar = Ascii.toUpperCase(request.getParameter("term"));
                if (strValorBuscar == null) {
                    strValorBuscar = "";
                }
                //Declaramos objeto json
                JSONArray jsonChild = new JSONArray();
                if (!strValorBuscar.trim().equals("")) {
                    //Busca el valor en la tabla de beneficiarios....
                    String strSql = "SELECT DISTINCT CEC_CURSO2 "
                            + "FROM cofide_ev_cursos where "
                            + "CEC_CURSO2 LIKE '%" + strValorBuscar + "%' ORDER BY CEC_CURSO2";
                    ResultSet rsCombo;
                    try {
                        rsCombo = oConn.runQuery(strSql, true);
                        while (rsCombo.next()) {
                            String strCurso1 = rsCombo.getString("CEC_CURSO2");
                            JSONObject objJson = new JSONObject();
                            objJson.put("value", strCurso1);
                            objJson.put("label", strCurso1);
                            jsonChild.put(objJson);
                        }
                        rsCombo.close();
                    } catch (SQLException ex) {
                        ex.fillInStackTrace();
                    }
                }
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(jsonChild.toString());//Pintamos el resultado
            }
            if (strId.equals("3")) {

                String strCC_CURSO_ID = request.getParameter("CC_CURSO_ID");
                String strIdMasterEv = request.getParameter("idMasterEv");

                String strAsp1 = request.getParameter("CEC_ASP1");
                String strAsp2 = request.getParameter("CEC_ASP2");
                String strAsp3 = request.getParameter("CEC_ASP3");
                String strAsp4 = request.getParameter("CEC_ASP4");
                String strPromAsp = request.getParameter("CEC_PROM_ASPECTOS");

                String strIn1 = request.getParameter("CEC_IN1");
                String strIn2 = request.getParameter("CEC_IN2");
                String strIn3 = request.getParameter("CEC_IN3");
                String strIn4 = request.getParameter("CEC_IN4");
                String strIn5 = request.getParameter("CEC_IN5");
                String strPromIn = request.getParameter("CEC_PROM_INSTALACION");

                final String strCurso1 = URLDecoder.decode(new String(request.getParameter("CEC_CURSO1").getBytes("iso-8859-1")), "UTF-8");
                final String strCurso2 = URLDecoder.decode(new String(request.getParameter("CEC_CURSO2").getBytes("iso-8859-1")), "UTF-8");

                //Variables para saber si aplica la pregunta
                String strChecAsp1 = request.getParameter("CHECK_ASP1");
                String strChecAsp2 = request.getParameter("CHECK_ASP2");
                String strChecAsp3 = request.getParameter("CHECK_ASP3");
                String strChecAsp4 = request.getParameter("CHECK_ASP4");

                String strChecIn1 = request.getParameter("CHECK_IN1");
                String strChecIn2 = request.getParameter("CHECK_IN2");
                String strChecIn3 = request.getParameter("CHECK_IN3");
                String strChecIn4 = request.getParameter("CHECK_IN4");
                String strChecIn5 = request.getParameter("CHECK_IN5");

                final String strName = URLDecoder.decode(new String(request.getParameter("TMK_NAME").getBytes("iso-8859-1")), "UTF-8");
                final String strEmpresa = URLDecoder.decode(new String(request.getParameter("TMK_EMPRESA").getBytes("iso-8859-1")), "UTF-8");
                String strMail = request.getParameter("TMK_MAIL");
                String strPhone = request.getParameter("TMK_PHONE");

                String strMedio = request.getParameter("TMK_MEDIO");
                final String strMedioDesc = URLDecoder.decode(new String(request.getParameter("TMK_MEDIO_DESC").getBytes("iso-8859-1")), "UTF-8");
                String strPuntos = request.getParameter("TMK_PUNTOS"); //si o no
                final String strPuntosDesc = URLDecoder.decode(new String(request.getParameter("TMK_PUNTOS_DESC").getBytes("iso-8859-1")), "UTF-8");
                final String strRecomendacion = URLDecoder.decode(new String(request.getParameter("TMK_RECOMENDACION").getBytes("iso-8859-1")), "UTF-8");

                String[] strNombre = new String[5];
                String[] strCorreo = new String[5];
                String[] strtelefono = new String[5];
                for (int i = 0; i < 5; i++) {
                    strNombre[i] = request.getParameter("TMK_NOMBRE" + i);
                    strCorreo[i] = request.getParameter("TMK_EMAIL" + i);
                    strtelefono[i] = request.getParameter("TMK_TELEFONO" + i);
                }

                strRes = setCalificacionCursos(oConn, strCC_CURSO_ID,
                        strAsp1, strAsp2, strAsp3, strAsp4,
                        strIn1, strIn2, strIn3, strIn4, strIn5,
                        strCurso1, strCurso2, "0", "0",
                        strChecAsp1, strChecAsp2, strChecAsp3, strChecAsp4,
                        strChecIn1, strChecIn2, strChecIn3, strChecIn4, strChecIn5, strIdMasterEv,
                        strName, strEmpresa, strMail, strPhone,
                        strMedio, strMedioDesc, strPuntos, strPuntosDesc, strRecomendacion, strNombre, strCorreo, strtelefono);

                if (strRes.equals("")) {
                    strRes = "OK";
                }

                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(strRes);//Pintamos el resultado
            }//Fin ID 3

            if (strId.equals("4")) {
                UtilXml utilXML = new UtilXml();
                String strXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
                strXML += "<vta>";
                String strCC_CURSO_ID = request.getParameter("CC_CURSO_ID");
                String strAsp1 = "";
                String strAsp2 = "";
                String strAsp3 = "";
                String strAsp4 = "";
                String strIn1 = "";
                String strIn2 = "";
                String strIn3 = "";
                String strIn4 = "";
                String strIn5 = "";
                String strPromAsp = "";
                String strPromIns = "";
                String strPromIn = "";
                String strCurso1 = "";
                String strCurso2 = "";
                String strCEC_ID = "";
                String strID_Curso = "";
                String strIdMaster = "";
                String strSql = "select * from cofide_ev_cursos where CEC_CURSO_ID = " + strCC_CURSO_ID;
                ResultSet rs = oConn.runQuery(strSql, true);
                while (rs.next()) {
                    strCEC_ID = rs.getString("CEC_ID");
                    strAsp1 = rs.getString("CEC_ASP_Q1");
                    strAsp2 = rs.getString("CEC_ASP_Q2");
                    strAsp3 = rs.getString("CEC_ASP_Q3");
                    strAsp4 = rs.getString("CEC_ASP_Q4");
                    strIn1 = rs.getString("CEC_IN_Q1");
                    strIn2 = rs.getString("CEC_IN_Q2");
                    strIn3 = rs.getString("CEC_IN_Q3");
                    strIn4 = rs.getString("CEC_IN_Q4");
                    strIn5 = rs.getString("CEC_IN_Q5");
                    strPromAsp = rs.getString("CEC_PROM_ASPECTOS");
                    strPromIns = rs.getString("CEC_PROM_INSTRUCTOR");
                    strPromIn = rs.getString("CEC_PROM_INSTALACION");
                    strCurso1 = rs.getString("CEC_CURSO1");
                    strCurso2 = rs.getString("CEC_CURSO2");
                    strIdMaster = rs.getString("ID_EVMASTER");
                    strID_Curso = rs.getString("CEC_CURSO_ID");
                    strXML += "<datos "
                            + " CEC_ID = \"" + strCEC_ID + "\"  "
                            + " CEC_ASP_Q1 = \"" + strAsp1 + "\"  "
                            + " CEC_ASP_Q2 = \"" + strAsp2 + "\"  "
                            + " CEC_ASP_Q3 = \"" + strAsp3 + "\" "
                            + " CEC_ASP_Q4 = \"" + strAsp4 + "\" "
                            + " CEC_IN_Q1 = \"" + strIn1 + "\" "
                            + " CEC_IN_Q2 = \"" + strIn2 + "\" "
                            + " CEC_IN_Q3 = \"" + strIn3 + "\" "
                            + " CEC_IN_Q4 = \"" + strIn4 + "\" "
                            + " CEC_IN_Q5 = \"" + strIn5 + "\" "
                            + " CEC_PROM_ASPECTOS = \"" + strPromAsp + "\" "
                            + " CEC_PROM_INSTRUCTOR = \"" + strPromIns + "\" "
                            + " CEC_PROM_INSTALACION = \"" + strPromIn + "\" "
                            + " CEC_CURSO1 = \"" + strCurso1 + "\" "
                            + " CEC_CURSO2 = \"" + strCurso2 + "\" "
                            + " ID_MASTER = \"" + strIdMaster + "\" "
                            + " CC_CURSO_ID = \"" + strID_Curso + "\" "
                            + " />";
                } //fin del while
                strXML += "</vta>";
                rs.close();
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML);//Pintamos el resultado
            } //fin del caso
            if (strId.equals("5")) {
                String strCC_CURSO_ID = request.getParameter("CC_CURSO_ID");
                String strAsp1 = request.getParameter("LEV_ASP1");
                String strAsp2 = request.getParameter("LEV_ASP2");
                String strAsp3 = request.getParameter("LEV_ASP3");
                String strAsp4 = request.getParameter("LEV_ASP4");
                String strInst1 = request.getParameter("LEV_INS1");
                String strInst2 = request.getParameter("LEV_INS2");
                String strInst3 = request.getParameter("LEV_INS3");
                String strInst4 = request.getParameter("LEV_INS4");
                String strInst5 = request.getParameter("LEV_INS5");
                String strIn1 = request.getParameter("LEV_IN1");
                String strIn2 = request.getParameter("LEV_IN2");
                String strIn3 = request.getParameter("LEV_IN3");
                String strIn4 = request.getParameter("LEV_IN4");
                String strIn5 = request.getParameter("LEV_IN5");
                String strPromAsp = request.getParameter("LEV_P1");
                String strPromIns = request.getParameter("LEV_P2");
                String strPromIn = request.getParameter("LEV_P3");
                String strCurso1 = request.getParameter("LEV_CURSOS1");
                String strCurso2 = request.getParameter("LEV_CURSOS2");
                String strSqlUpdate = "UPDATE cofide_ev_cursos SET CEC_ASP_Q1= " + strAsp1 + ", CEC_ASP_Q2= " + strAsp2 + ", CEC_ASP_Q3= " + strAsp3 + ", CEC_ASP_Q4= " + strAsp4 + ", "
                        + "CEC_INS_Q1= " + strInst1 + ", CEC_INS_Q2= " + strInst2 + ", CEC_INS_Q3= " + strInst3 + ", CEC_INS_Q4= " + strInst4 + ", CEC_INS_Q5= " + strInst5 + ", "
                        + "CEC_IN_Q1= " + strIn1 + ", CEC_IN_Q2= " + strIn2 + ", CEC_IN_Q3= " + strIn3 + ", CEC_IN_Q4= " + strIn4 + ", CEC_IN_Q5= " + strIn5 + ", "
                        + "CEC_CURSO1= '" + strCurso1 + "', CEC_CURSO2= '" + strCurso2 + "', "
                        + "CEC_PROM_INSTRUCTOR= " + strPromIns + ", CEC_PROM_INSTALACION= " + strPromIn + ", CEC_PROM_ASPECTOS= " + strPromAsp + " "
                        + "WHERE  CEC_CURSO_ID = " + strCC_CURSO_ID;
                oConn.runQueryLMD(strSqlUpdate);
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println("OK");//Pintamos el resultado
            } //fin del caso

            //Regresa Instructores a Evaluar por Curso
            if (strId.equals("6")) {
                String strFiltro = "";
                String strCurso = request.getParameter("CURSO_ID");
                String strIdIntr = "";
                String strNotIds = getIntrEvaluados(oConn, strCurso);
                StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
                strXML.append("<InstructorDisponible>");
                String strFiltroSql = "";

                strSql = "select GROUP_CONCAT(concat_ws(',',CI_INSTRUCTOR_ID)) as Instructores "
                        + "from cofide_instructor_imparte where CC_CURSO_ID = " + strCurso + ";";

                if (!strNotIds.equals("")) {
                    strSql = "select GROUP_CONCAT(concat_ws(',',CI_INSTRUCTOR_ID)) as Instructores "
                            + "from cofide_instructor_imparte where CC_CURSO_ID = " + strCurso
                            + " and CI_INSTRUCTOR_ID not in (" + strNotIds + ")";
                }

                try {
                    rs = oConn.runQuery(strSql, true);
                    while (rs.next()) {
                        if (rs.getString("Instructores") != null) {
                            strIdIntr = rs.getString("Instructores");
                        }
                    }
                    rs.close();
                    int intCuantos = getIntCuantos(oConn, strIdIntr);
                    if (!strIdIntr.equals("")) {
                        strSql = "select CI_INSTRUCTOR_ID,CI_INSTRUCTOR,CI_NOMBRE "
                                + "from cofide_instructor where CI_INSTRUCTOR_ID in (" + strIdIntr + ") ORDER BY CI_NOMBRE";
                        rs = oConn.runQuery(strSql, true);
                        while (rs.next()) {
                            strXML.append("<datos");
                            strXML.append(" CI_INSTRUCTOR_ID = \"").append(rs.getInt("CI_INSTRUCTOR_ID")).append("\"");
                            strXML.append(" CI_INSTRUCTOR = \"").append(util.Sustituye(rs.getString("CI_INSTRUCTOR"))).append("\"");
                            strXML.append(" cuantos = \"").append(intCuantos).append("\"");
                            strXML.append(" />");
                        }
                        rs.close();
                    }

                    strXML.append("</InstructorDisponible>");
                } catch (SQLException ex) {
                    System.out.println("Error GetConsulta INSTRUCTOR POR CURSO: " + ex.getLocalizedMessage());
                }
                strXML.toString();
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML);//Pintamos el resultado
            }//Fin ID 6

            //Guarda Calificacion del instructor en el curso seleccionado
            if (strId.equals("7")) {
                String strCC_CURSO_ID = request.getParameter("CC_CURSO_ID");
                String strIns1 = request.getParameter("CEC_INS1");
                String strIns2 = request.getParameter("CEC_INS2");
                String strIns3 = request.getParameter("CEC_INS3");
                String strIns4 = request.getParameter("CEC_INS4");
                String strIns5 = request.getParameter("CEC_INS5");
                String strIns1CHK = request.getParameter("CHK_INS1");
                String strIns2CHK = request.getParameter("CHK_INS2");
                String strIns3CHK = request.getParameter("CHK_INS3");
                String strIns4CHK = request.getParameter("CHK_INS4");
                String strIns5CHK = request.getParameter("CHK_INS5");
                String strPromIns = request.getParameter("CEC_PROM_INSTRUCTOR");
                String strIdInstr = request.getParameter("CI_INSTRUCTOR_ID");
                String strIdMEvaluacion = request.getParameter("CEC_ID_EVALUACION");
                String strSql = "";
                if (strIdMEvaluacion.equals("0")) {
                    strSql = "select MAX(ID_EVMASTER) + 1 as idMasterEv from cofide_evaluacion_instructor";
                    try {
                        ResultSet rs = oConn.runQuery(strSql, true);
                        while (rs.next()) {
                            if (rs.getString("idMasterEv") == null) {
                                strIdMEvaluacion = "1";
                            } else {
                                strIdMEvaluacion = rs.getString("idMasterEv");
                            }
                        }
                        rs.close();
                    } catch (SQLException ex) {
                        System.out.println("Error al obtener Id Maesttro " + ex);
                    }
                }

                strSql = "INSERT INTO cofide_evaluacion_instructor "
                        + "(CI_INSTRUCTOR_ID, CC_CURSO_ID, EI_Q1, EI_Q2, EI_Q3, EI_Q4, EI_Q5, EI_PROMEDIO, "
                        + "EI_CHK_Q1, EI_CHK_Q2, EI_CHK_Q3, EI_CHK_Q4, EI_CHK_Q5, ID_EVMASTER) VALUES "
                        + "('" + strIdInstr + "', '" + strCC_CURSO_ID + "', '" + strIns1 + "', '" + strIns2 + "', '" + strIns3 + "', '" + strIns4 + "', '" + strIns5 + "', "
                        + "'" + strPromIns + "', " + strIns1CHK + ", " + strIns2CHK + ", " + strIns3CHK + ", " + strIns4CHK + ", " + strIns5CHK + ", " + strIdMEvaluacion + ")";

                oConn.runQueryLMD(strSql);
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println("OK" + strIdMEvaluacion);//Pintamos el resultado
            }//Fin ID 7

            //Consulta Los instructores que impartiran el curso
            if (strId.equals("8")) {
                String strCurso = request.getParameter("strCurso");
                StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
                strXML.append("<InstructorImparte>");
                strSql = "select *,"
                        + "(select CI_INSTRUCTOR from cofide_instructor where cofide_instructor.CI_INSTRUCTOR_ID = cofide_instructor_imparte.CI_INSTRUCTOR_ID)as CC_INSTRUCTOR "
                        + "from cofide_instructor_imparte where CC_CURSO_ID = " + strCurso;
                double dblPromedio = 0;
                try {
                    dblPromedio = 0;
                    rs = oConn.runQuery(strSql, true);
                    while (rs.next()) {
                        dblPromedio = getPromedioInstructor(oConn, strCurso, rs.getString("CI_INSTRUCTOR_ID"));
                        strXML.append("<datos");
                        strXML.append(" CII_ID_INSTR = \"").append(rs.getInt("CI_INSTRUCTOR_ID")).append("\"");
                        strXML.append(" CC_CURSO_ID = \"").append(rs.getInt("CC_CURSO_ID")).append("\"");
                        strXML.append(" CCI_INSTRUCTOR = \"").append(util.Sustituye(rs.getString("CC_INSTRUCTOR"))).append("\"");
                        strXML.append(" CCI_PROMEDIO = \"").append(dblPromedio).append("\"");
                        strXML.append(" />");
                    }
                    strXML.append("</InstructorImparte>");
                    strXML.toString();
                    rs.close();
                } catch (SQLException ex) {
                    System.out.println("Error GetConsulta COFIDE CATALOGO: " + ex.getLocalizedMessage());
                }
//                strXML.toString();
//                rs.close();
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML);//Pintamos el resultado
            }//Fin ID 8

            //Regresa un XML con informacion extra del curso
            if (strId.equals("9")) {
                String strCurso = request.getParameter("strCurso");
                StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
                strXML.append("<InfoCurso>");

                strXML.append("<datos");
                strXML.append(" P_ASISTIERON = \"").append(getPAsistieron(oConn, strCurso)).append("\"");
                strXML.append(" SEDEDESC = \"").append(getStrSedeDesc(oConn, strCurso)).append("\"");
                strXML.append(" />");
                strXML.append("</InfoCurso>");
                strXML.toString();
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML);//Pintamos el resultado
            }//Fin Id 9

            //Elimina una partida de la evaluacion
            if (strId.equals("10")) {
                String strID = request.getParameter("CEC_ID");
                String strIdMaster = request.getParameter("MASTER_ID");
                String strIDCURSO = request.getParameter("IDCURSO");
                strSql = "delete from cofide_ev_cursos where CEC_ID = " + strID;
                oConn.runQueryLMD(strSql);
                strSql = "delete from cofide_evaluacion_instructor where ID_EVMASTER = " + strIdMaster + " and CC_CURSO_ID = " + strIDCURSO;
                oConn.runQueryLMD(strSql);
                strRes = oConn.getStrMsgError();
                if (strRes.equals("")) {
                    strRes = "OK";
                }
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(strRes);//Pintamos el resultado
            }//Fin ID 10
        }
    }
    oConn.close();
%>
<%!
    ResultSet rs = null;
    String strSql = "";
    String strRes = "";
    UtilXml util = new UtilXml();

    public String getIntrEvaluados(Conexion oConn, String strCC_ID) {
        String strIds = "";
        strSql = "select GROUP_CONCAT(concat_ws(',',CI_INSTRUCTOR_ID)) as Instructores from cofide_evaluacion_instructor where CC_CURSO_ID = " + strCC_ID + "  and EI_ACTIVO = 0";
        try {
            rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                if (rs.getString("Instructores") != null) {
                    strIds = rs.getString("Instructores");
                }
            }
            rs.close();
        } catch (SQLException ex) {
            System.out.println("Error GetInstructores No Calificados [" + ex.getLocalizedMessage() + "]");
        }
        return strIds;
    }//Fin getIntrEvaluados

    //Guarda calificaciones obtenidas por el Intructor y los puntos a evaluar del curso.
    public String setCalificacionCursos(Conexion oConn, String strCC_CURSO_ID,
            String strAsp1, String strAsp2, String strAsp3, String strAsp4,
            String strIn1, String strIn2, String strIn3, String strIn4, String strIn5,
            String strCurso1, String strCurso2, String strPromAsp, String strPromIn,
            String strCheckAsp1, String strCheckAsp2, String strCheckAsp3, String strCheckAsp4,
            String strCheckIn1, String strCheckIn2, String strCheckIn3, String strCheckIn4, String strCheckIn5, String strIdMasterEv,
            String strName, String strEmpresa, String strMail, String strPhone,
            String strMedio, String strMedioD, String strPuntos, String strPuntosDesc, String strRecomen,
            String[] strNombre, String[] strCorreo, String[] strTelefono) {
        strRes = "";
        double dblPromInst = 0;
        try {

            String strSql1 = "INSERT INTO cofide_ev_cursos (CEC_CURSO_ID,CEC_ASP_Q1, CEC_ASP_Q2, CEC_ASP_Q3, CEC_ASP_Q4, "
                    + "CEC_IN_Q1, CEC_IN_Q2, CEC_IN_Q3, CEC_IN_Q4, CEC_IN_Q5, "
                    + "CEC_CURSO1, CEC_CURSO2, "
                    + "CEC_PROM_INSTALACION,CEC_PROM_ASPECTOS,CEC_PROM_INSTRUCTOR,"
                    + "CEC_CHECK_ASP1,CEC_CHECK_ASP2,CEC_CHECK_ASP3,CEC_CHECK_ASP4,"
                    + "CEC_CHECK_IN1,CEC_CHECK_IN2,CEC_CHECK_IN3,CEC_CHECK_IN4,CEC_CHECK_IN5,ID_EVMASTER,"
                    + "CEC_MEDIO,CEC_MEDIO_DESC,CEC_PUNTOS,CEC_PUNTOS_DESC,CEC_RECOMENDACION,"
                    + "CEC_NOMBRE1,CEC_NOMBRE2,CEC_NOMBRE3,CEC_NOMBRE4,CEC_NOMBRE5,"
                    + "CEC_CORREO1,CEC_CORREO2,CEC_CORREO3,CEC_CORREO4,CEC_CORREO5,"
                    + "CEC_TELEFONO1,CEC_TELEFONO2,CEC_TELEFONO3,CEC_TELEFONO4,CEC_TELEFONO5,"
                    + "CEC_NAME,CEC_EMPRESA,CEC_MAIL,CEC_PHONE ) VALUES "
                    + "(" + strCC_CURSO_ID + ", " + strAsp1 + ", " + strAsp2 + ", " + strAsp3 + ", " + strAsp4 + ","
                    + " " + strIn1 + ", " + strIn2 + ", " + strIn3 + ", " + strIn4 + ", " + strIn5 + ", "
                    + " '" + strCurso1 + "', '" + strCurso2 + "', "
                    + strPromIn + ", " + strPromAsp + "," + dblPromInst + ","
                    + " " + strCheckAsp1 + ", " + strCheckAsp2 + ", " + strCheckAsp3 + ", " + strCheckAsp4 + ", "
                    + " " + strCheckIn1 + ", " + strCheckIn2 + ", " + strCheckIn3 + ", " + strCheckIn4 + ", " + strCheckIn5 + ", " + strIdMasterEv + ","
                    + "'" + strMedio + "','" + strMedioD.replace("'", "\\'") + "','" + strPuntos + "','" + strPuntosDesc.replace("'", "\\'") + "','" + strRecomen.replace("'", "\\'") + "',"
                    + "'" + strNombre[0] + "','" + strNombre[1] + "','" + strNombre[2] + "','" + strNombre[3] + "','" + strNombre[4] + "',"
                    + "'" + strCorreo[0] + "','" + strCorreo[1] + "','" + strCorreo[2] + "','" + strCorreo[3] + "','" + strCorreo[4] + "',"
                    + "'" + strTelefono[0] + "','" + strTelefono[1] + "','" + strTelefono[2] + "','" + strTelefono[3] + "','" + strTelefono[4] + "',"
                    + "'" + strName.replace("'", "\\'") + "','" + strEmpresa.replace("'", "\\'") + "','" + strMail.replace("'", "\\'") + "','" + strPhone.replace("'", "\\'") + "')";

            oConn.runQueryLMD(strSql1);

            strRes = oConn.getStrMsgError();

            if (!strRes.equals("")) {
                strRes = oConn.getStrMsgError();
            } else {
                strSql = "update cofide_evaluacion_instructor set EI_ACTIVO = 1 where CC_CURSO_ID = " + strCC_CURSO_ID;
                oConn.runQueryLMD(strSql);
                if (!oConn.isBolEsError()) {
                    strRes = "OK";
                }
            }

        } catch (Exception ex) {
            System.out.println("AgregaEvaluaciones curso [" + ex.getLocalizedMessage() + "]");
        }
        return strRes;
    }//Fin setCalificacionCursos

    //Regresa el promedio de los instructores por curso
    public double getPromedioInstructor(Conexion oConn, String strCursoId, String strIdInstructor) {
        strSql = "select avg(EI_PROMEDIO) as PromInstructor from cofide_evaluacion_instructor where CC_CURSO_ID = " + strCursoId + " and CI_INSTRUCTOR_ID = " + strIdInstructor;
        double dblPromInst = 0;

        try {
            ResultSet rs1 = oConn.runQuery(strSql, true);
            rs1 = oConn.runQuery(strSql, true);
            while (rs1.next()) {
                dblPromInst = rs1.getDouble("PromInstructor");
            }
            rs1.close();
        } catch (SQLException ex) {
            System.out.println("Get Promedio Instructores [" + ex.getLocalizedMessage() + "].");
        }
        return dblPromInst;
    }//Fin getPromedioInstructor

    //Regresa el numro de asistentes por el curso indicado
    public int getPAsistieron(Conexion oConn, String strCursoId) {
        int intAsistencia = 0;
        strSql = "select count(*) as Asistentes from cofide_participantes where CP_ID_CURSO = " + strCursoId + " and CP_ASISTENCIA = 1 ";
        try {
            rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                intAsistencia = rs.getInt("Asistentes");
            }
            rs.close();
        } catch (SQLException ex) {
            System.out.println("");
        }
        return intAsistencia;
    }//Fin getPAsistieron

    //Regresa la descripcion de la sede por el curso indicado
    public String getStrSedeDesc(Conexion oConn, String strCursoId) {
        String strSedeDesc = "";
        strSql = "select CC_CURSO_ID,CC_SEDE_ID,(select CSH_SEDE from cofide_sede_hotel where CSH_ID =  CC_SEDE_ID) as SEDEDESC from cofide_cursos where CC_CURSO_ID = " + strCursoId;
        try {
            rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                strSedeDesc = rs.getString("SEDEDESC");
            }
            rs.close();
        } catch (SQLException ex) {
            System.out.println("");
        }
        return strSedeDesc;
    }

    public int getIntCuantos(Conexion oConn, String strInstructores) {
        int intCuantos = 0;
        if (strInstructores.equals("")) {
            strInstructores = "0";
        }
        strSql = "select count(*) as cuantos from cofide_instructor where CI_INSTRUCTOR_ID in (" + strInstructores + ")  ORDER BY CI_NOMBRE";
        try {
            rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                intCuantos = rs.getInt("cuantos");
            }
            rs.close();
        } catch (SQLException sql) {
            System.out.println("Error al contar a los Instructroess: " + sql);
        }
        return intCuantos;
    }
%>
