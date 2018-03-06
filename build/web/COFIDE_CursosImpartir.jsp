<%-- 
    Document   : COFIDE_CursosImpartir
    Created on : 25-oct-2016, 9:45:09
    Author     : casajosefa
--%>
<%@page import="java.net.URLDecoder"%>
<%@page import="java.util.ArrayList"%>
<%@page import="comSIWeb.Utilerias.Mail"%>
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
            //Consulta el Catalogo de Cursos Asignados
            if (strid.equals("1")) {
                String strMes = request.getParameter("strMes");
                String strAnio = request.getParameter("strAnio");
                StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
                strXML.append("<CursosImpartir>");
                if (strMes.equals("0")) {
                    strMes = fec.getFechaActual().substring(4, 6);
                }
                strSql = "select *,"
                        + "(select CSH_SEDE from cofide_sede_hotel where CSH_ID in (select GROUP_CONCAT(concat_ws(', ',CC_SEDE_ID)) "
                        + "from cofide_cursos where cofide_cursos.CC_CLAVES = cofide_catalogo_curso.CCU_ID_M and SUBSTR(cofide_cursos.CC_FECHA_INICIAL,1,6) = '" + strAnio + strMes + "') ) as SEDE_ASIGNADA,"
                        /*+ "(select GROUP_CONCAT(concat_ws(', ',CC_SEDE)) from cofide_cursos where cofide_cursos.CC_CLAVES = cofide_catalogo_curso.CCU_ID_M and SUBSTR(cofide_cursos.CC_FECHA_INICIAL,1,6) = '" + strAnio + strMes + "') as SEDE_ASIGNADA,"*/
                        + "(select GROUP_CONCAT(concat_ws(', ',CC_FECHA_INICIAL)) from cofide_cursos where cofide_cursos.CC_CLAVES = cofide_catalogo_curso.CCU_ID_M and SUBSTR(cofide_cursos.CC_FECHA_INICIAL,1,6) = '" + strAnio + strMes + "') as FECHA_ASIGNADA "
                        + "from cofide_catalogo_curso order by CCU_CURSO";
                try {
                    int intDisponibles = 0;
                    rs = oConn.runQuery(strSql, true);
                    ArrayList<Integer> intArrC = new ArrayList<Integer>();
                    intArrC = getCursoSug(oConn, strAnio, strMes);
                    String strEsSug = "";
                    while (rs.next()) {
                        String strSedeAsig = "";
                        String strFechaAsig = "";
                        if (rs.getString("SEDE_ASIGNADA") != null) {
                            strSedeAsig = util.Sustituye(rs.getString("SEDE_ASIGNADA"));
                        }
                        if (rs.getString("FECHA_ASIGNADA") != null) {
                            strFechaAsig = fec.FormateaDDMMAAAA(rs.getString("FECHA_ASIGNADA"), "/");
                        }
                        strEsSug = "NO";

                        for (int e : intArrC) {
                            if (rs.getInt("CCU_ID_M") == e) {
                                strEsSug = "SI";
                            }
                        }
                        strXML.append("<datos");
                        strXML.append(" CCU_ID_M = \"").append(rs.getInt("CCU_ID_M")).append("\"");
                        strXML.append(" CCU_CURSO = \"").append(util.Sustituye(rs.getString("CCU_CURSO"))).append("\"");
                        strXML.append(" SEDE_ASIGNADA = \"").append(strSedeAsig).append("\"");
                        strXML.append(" FECHA_ASIGNADA = \"").append(strFechaAsig).append("\"");
                        strXML.append(" CC_SUGERENCIA = \"").append(strEsSug).append("\"");
                        strXML.append(" />");
                    }
                    rs.close();
                    strXML.append("</CursosImpartir>");
                } catch (SQLException ex) {
                    System.out.println("Error GetConsulta COFIDE CATALOGO: " + ex.getLocalizedMessage());
                }
                strXML.toString();
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML);//Pintamos el resultado
            }//Fin ID 1

            //Regresa las SEDES Disponibles del mes
            if (strid.equals("2")) {
                String strFiltro = "";
                String strMes = request.getParameter("strMes");
                String strAnio = request.getParameter("strAnio");
                String strCursoEd = "";
                if (request.getParameter("CursoEditar") != null) {
                    strCursoEd = request.getParameter("CursoEditar");
                }
                StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
                strXML.append("<SedeDisponible>");
                if (strMes.equals("0")) {
                    strMes = fec.getFechaActual().substring(4, 6);
                }
                if (!strCursoEd.equals("0")) {
                    strFiltro = " OR CC_CURSO_ID = " + strCursoEd;
                }

                strSql = "select CC_CURSO_ID,(select CSH_SEDE from cofide_sede_hotel where CSH_ID = cofide_cursos.CC_SEDE_ID)as SedeDesc,CC_ALIAS_FEC,CC_FECHA_INICIAL,CC_HR_EVENTO_INI,CC_HR_EVENTO_FIN from cofide_cursos "
                        + "where SUBSTR(cofide_cursos.CC_FECHA_INICIAL,1,6) = '" + strAnio + strMes + "' and CC_CLAVES = ''"
                        + strFiltro
                        + " order by CC_FECHA_INICIAL, CC_HR_EVENTO_INI;";
                try {
                    rs = oConn.runQuery(strSql, true);
                    while (rs.next()) {
                        String strSedeAsig = "";
                        String strFechaAsig = "";
                        String strHoraAsig = "";
                        String strHoraAsigFin = "";
                        String strAux = "";
                        if (rs.getString("SedeDesc") != null) {
                            strSedeAsig = rs.getString("SedeDesc");
                        }
                        if (!rs.getString("CC_ALIAS_FEC").equals("")) {
                            //strFechaAsig = fec.FormateaDDMMAAAA(rs.getString("CC_FECHA_INICIAL"), "/");
                            strAux = "/" + rs.getString("CC_FECHA_INICIAL").substring(4, 6) + "/" + rs.getString("CC_FECHA_INICIAL").substring(0, 4);
                            strFechaAsig = rs.getString("CC_ALIAS_FEC") + strAux;
                        } else {
                            strFechaAsig = fec.FormateaDDMMAAAA(rs.getString("CC_FECHA_INICIAL"), "/");
                        }
                        if (rs.getString("CC_HR_EVENTO_INI") != null) {
                            strHoraAsig = rs.getString("CC_HR_EVENTO_INI");
                        }
                        if (rs.getString("CC_HR_EVENTO_FIN") != null) {
                            strHoraAsigFin = rs.getString("CC_HR_EVENTO_FIN");
                        }
                        strXML.append("<datos");
                        strXML.append(" CC_CURSO_ID = \"").append(rs.getInt("CC_CURSO_ID")).append("\"");
                        strXML.append(" CCU_CURSO = \"").append(util.Sustituye(strSedeAsig) + " " + strFechaAsig + " " + strHoraAsig + " A " + strHoraAsigFin).append("\"");
                        strXML.append(" SEDE_ASIGNADA = \"").append(strSedeAsig).append("\"");
                        strXML.append(" FECHA_ASIGNADA = \"").append(strFechaAsig).append("\"");
                        strXML.append(" HORA_ASIGNADA = \"").append(strHoraAsig).append("\"");
                        strXML.append(" />");
                    }
                    rs.close();

                    if (!strCursoEd.equals("0")) {
                        strXML.append("<CursoEdit>");
                        strSql = "select CC_PROGRAMAR, CC_IS_PRESENCIAL, CC_IS_ONLINE, CC_INSTRUCTOR_ID from cofide_cursos where CC_CURSO_ID = " + strCursoEd;
                        try {
                            rs = oConn.runQuery(strSql, true);
                            while (rs.next()) {
                                strXML.append("<datos");
                                strXML.append(" CC_PROGRAMAR = \"").append(rs.getInt("CC_PROGRAMAR")).append("\"");
                                strXML.append(" CC_IS_PRESENCIAL = \"").append(rs.getInt("CC_IS_PRESENCIAL")).append("\"");
                                strXML.append(" CC_IS_ONLINE = \"").append(rs.getInt("CC_IS_ONLINE")).append("\"");
                                strXML.append(" CC_INSTRUCTOR_ID = \"").append(rs.getInt("CC_INSTRUCTOR_ID")).append("\"");
                                strXML.append(" />");
                            }
                            rs.close();
                            strXML.append("</CursoEdit>");
                        } catch (SQLException ex) {
                            System.out.println("Error GetConsulta COFIDE CURSO _edit " + ex.getLocalizedMessage());
                        }
                    }
                    strXML.append("</SedeDisponible>");
                } catch (SQLException ex) {
                    System.out.println("Error GetConsulta COFIDE CATALOGO: " + ex.getLocalizedMessage());
                }
                strXML.toString();
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML);//Pintamos el resultado
            }//Fin ID 2

            //Regresa las SEDES usadas en los 12 meses anteriores
            if (strid.equals("3")) {
                String strMes = request.getParameter("strMes");
                String strAnio = request.getParameter("strAnio");
                String strClave = request.getParameter("Clave");
                StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
                strXML.append("<MesesAnteriores>");

                strXML.append("<datos");
                strXML.append(getCursosMesAnterior(oConn, strAnio, strMes, strClave));
                strXML.append(" />");
                strXML.append("</MesesAnteriores>");

                strXML.toString();
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML);//Pintamos el resultado
            }//Fin ID 3

            //Actualiza Informacion del Curso
            if (strid.equals("4")) {
                strRes = "";
                String strSede = request.getParameter("SEDE");
                String strInstructor = request.getParameter("INSTRUCTOR");
                String strHrsInstructor = request.getParameter("HORAS_INSTRUCTOR");
                String strCursoClave = request.getParameter("CursoClave");
                final String strCursoDesc2 = URLDecoder.decode(new String(request.getParameter("CursoDesc").getBytes("iso-8859-1")), "UTF-8");
                String strCursoDesc = strCursoDesc2.replace("'", "\\'");
                String strProgramar = request.getParameter("SePrograma");
                String strPresencial = request.getParameter("Presencial");
                String strOnline = request.getParameter("Online");
                String[] strDetaCurso = setDetaCurso(oConn, strCursoClave);
                strSql = "update cofide_cursos set "
                        + "CC_CLAVES = " + strCursoClave
                        + ",CC_NOMBRE_CURSO = '" + strCursoDesc + "' "
                        + ",CC_IS_PRESENCIAL = " + strPresencial
                        + ",CC_IS_ONLINE = " + strOnline
                        + ",CC_PROGRAMAR = " + strProgramar
                        + ",CC_DETALLE = '" + strDetaCurso[0] + "'"
                        + ",CC_TEMARIO = '" + strDetaCurso[1] + "'"
                        + ",CC_DIRIGIDO = '" + strDetaCurso[2] + "'"
                        + ",CC_REQUISITO = '" + strDetaCurso[3] + "'"
                        + ",CC_OBJETIVO = '" + strDetaCurso[4] + "'"
                        + " where CC_CURSO_ID = " + strSede;
                oConn.runQueryLMD(strSql);
                //Guardamos los instructores que impartiran el curso
                setInstructoresImpartir(oConn, strInstructor, strHrsInstructor, strSede);

                if (oConn.getStrMsgError().equals("")) {
                    strRes = "OK";
                } else {
                    strRes = oConn.getStrMsgError();
                }

                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(strRes);//Pintamos el resultado
            }//Fin ID 4

            //Envia correo al instructor sobre el nuevo curso a impartir
            if (strid.equals("5")) {
                strRes = "";
                String strMail = "";
                String strNomInstructor = "";
                String strInstructor = request.getParameter("IdInstructor");
                String strMes = request.getParameter("Mes");
                String strAnio = request.getParameter("Anio");
                if (strMes.equals("0")) {
                    strMes = fec.getFechaActual().substring(4, 6);
                }
                strSql = "select CI_EMAIL,CI_INSTRUCTOR from cofide_instructor where CI_INSTRUCTOR_ID = " + strInstructor;

                try {
                    rs = oConn.runQuery(strSql, true);
                    while (rs.next()) {
                        strMail = rs.getString("CI_EMAIL");
                        strNomInstructor = util.Sustituye(rs.getString("CI_INSTRUCTOR"));
                    }
                    rs.close();
                } catch (SQLException ex) {
                    System.out.println("ErrorMail Instructor: " + ex.getLocalizedMessage());
                }

                if (strMail.equals("")) {
                    strRes = "El Instructor no tiene configurado un Email.";
                } else {

                    Mail mail = new Mail();

                    if (mail.isEmail(strMail)) {
                        String strMailHTML = getMailInstructor(oConn, strInstructor, strAnio + strMes, strNomInstructor);
                        //Intentamos mandar el mail
                        mail.setBolDepuracion(false);
                        mail.getTemplate("CURSO_INSTR", oConn);
                        String strMessage = mail.getMensaje().replace("%strHTML%", strMailHTML);
                        mail.setMensaje(strMessage);

                        mail.setDestino(strMail);
                        boolean bol = mail.sendMail();
                        if (bol) {
                            strRes = "OK";
                            //strResp = "MAIL ENVIADO.";
                        } else {
                            strRes = "no se envio...";
                            //strResp = "FALLO EL ENVIO DEL MAIL.";
                        }
                    } else {
                        strRes = "El Email del instructor no es un Email valido.";
                    }
                }

                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(strRes);//Pintamos el resultado
            }//Fin ID 5

            //Envia correo al instructor sobre el nuevo curso a impartir
            if (strid.equals("6")) {
                String strOpc = request.getParameter("opc");
                String strMes = "";
                String strAnio = "";
                String strFecIni = "";
                String strFecFin = "";
                String strCompletQuery = "";
                if (strOpc.equals("1")) {
                    strMes = request.getParameter("strMes");
                    strAnio = request.getParameter("strAnio");
//                    strCompletQuery = "  and SUBSTR(CC_FECHA_INICIAL,1,6) = '" + strAnio + strMes + "' ";
                    strCompletQuery = "where SUBSTR((select CC_FECHA_INICIAL from cofide_cursos where CC_CURSO_ID = cofide_evaluacion_instructor.CC_CURSO_ID),1,4) = '" + strAnio + "' "
                            + "and SUBSTR((select CC_FECHA_INICIAL from cofide_cursos where CC_CURSO_ID = cofide_evaluacion_instructor.CC_CURSO_ID),5,2) = '" + strMes + "' ";
                }
                if (strOpc.equals("2")) {
                    strFecIni = request.getParameter("fec_ini");
                    strFecIni = fec.FormateaBD(strFecIni, "/");
                    strFecFin = request.getParameter("fec_fin");
                    strFecFin = fec.FormateaBD(strFecFin, "/");
//                    strCompletQuery = " and CC_FECHA_INICIAL between '" + strFecIni + "' and '" + strFecFin + "' ";
                    strCompletQuery = "where (select CC_FECHA_INICIAL from cofide_cursos where CC_CURSO_ID = cofide_evaluacion_instructor.CC_CURSO_ID) between '" + strFecIni + "' "
                            + "and '" + strFecFin + "' ";
                }
                String strInstructor = request.getParameter("Instructor");
                StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
                strXML.append("<EvaluacionIns>");
                if (strMes.equals("0")) {
                    strMes = fec.getFechaActual().substring(4, 6);
                }

                strSql = "select CI_INSTRUCTOR_ID,"
                        + "(select CI_INSTRUCTOR from cofide_instructor where cofide_instructor.CI_INSTRUCTOR_ID = CI_INSTRUCTOR_ID limit 1) as Instructor,"
                        + "round(avg(EI_PROMEDIO),2) as PromTotal,"
                        + "(select CC_NOMBRE_CURSO from cofide_cursos where CC_CURSO_ID = cofide_evaluacion_instructor.CC_CURSO_ID) as CURSO_DESC,"
                        + "(select CC_FECHA_INICIAL from cofide_cursos where CC_CURSO_ID = cofide_evaluacion_instructor.CC_CURSO_ID) as CURSO_FECHA,"
                        + "(select (select CSH_SEDE from cofide_sede_hotel where CSH_ID =  CC_SEDE_ID) as SEDEDESC from cofide_cursos where CC_CURSO_ID = cofide_evaluacion_instructor.CC_CURSO_ID) as SEDE,"
                        + "(select count(*) from cofide_participantes where CP_ID_CURSO = CC_CURSO_ID and CP_ASISTENCIA = 1 ) Asistentes,"
                        + "(select count(*) from cofide_ev_cursos where CEC_CURSO_ID = CC_CURSO_ID ) as Evaluaciones "
                        + "from cofide_evaluacion_instructor "
                        + strCompletQuery
                        + "and CI_INSTRUCTOR_ID = " + strInstructor
                        + " group by CI_INSTRUCTOR_ID,CC_CURSO_ID order by CC_CURSO_ID";

//                strSql = "select cofide_cursos.CC_CURSO_ID,cofide_cursos.CC_NOMBRE_CURSO,cofide_cursos.CC_FECHA_INICIAL,"
//                        + "(select CI_INSTRUCTOR from cofide_instructor where CI_INSTRUCTOR_ID = 1)as Instructor,"
//                        + "avg(cofide_evaluacion_curso.CEC_EVALUACION) as PROMEDIO from `cofide_evaluacion_curso`, cofide_cursos "
//                        + "where cofide_evaluacion_curso.CEC_ID_CURSO =  cofide_cursos.CC_CURSO_ID "
//                        + "and cofide_cursos.CC_INSTRUCTOR_ID = " + strInstructor + strCompletQuery
//                        //                        + "  and SUBSTR(CC_FECHA_INICIAL,1,6) = '" + strAnio + strMes + "' "
//                        + "group by cofide_cursos.CC_CURSO_ID,cofide_cursos.CC_NOMBRE_CURSO,cofide_cursos.CC_FECHA_INICIAL";
                try {
                    int intDisponibles = 0;
                    rs = oConn.runQuery(strSql, true);
                    while (rs.next()) {
                        String strSedeAsig = "";
                        String strFechaAsig = "";
                        if (rs.getString("CURSO_FECHA") != null) {
//                        if (rs.getString("CC_FECHA_INICIAL") != null) {
//                            strFechaAsig = fec.FormateaDDMMAAAA(rs.getString("CC_FECHA_INICIAL"), "/");
                            strFechaAsig = fec.FormateaDDMMAAAA(rs.getString("CURSO_FECHA"), "/");
                        }
                        strXML.append("<datos");
                        strXML.append(" GR_CURSODESC = \"").append(util.Sustituye(rs.getString("CURSO_DESC"))).append("\"");
                        strXML.append(" GR_CURSODPROM = \"").append(rs.getDouble("PromTotal")).append("\"");
                        strXML.append(" GR_CURSOFECHA = \"").append(strFechaAsig).append("\"");
                        strXML.append(" Instructor = \"").append(rs.getString("Instructor")).append("\"");
                        strXML.append(" GR_CURSOSEDE = \"").append(rs.getString("SEDE")).append("\"");
                        strXML.append(" GR_CURSONOASIST = \"").append(rs.getString("Asistentes")).append("\"");
                        strXML.append(" GR_CURSONOEVAL = \"").append(rs.getString("Evaluaciones")).append("\"");
                        strXML.append(" />");
//                        strXML.append("<datos");
//                        strXML.append(" GR_CURSODESC = \"").append(util.Sustituye(rs.getString("CC_NOMBRE_CURSO"))).append("\"");
//                        strXML.append(" GR_CURSODPROM = \"").append(rs.getDouble("PROMEDIO")).append("\"");
//                        strXML.append(" GR_CURSOFECHA = \"").append(strFechaAsig).append("\"");
//                        strXML.append(" Instructor = \"").append(rs.getString("Instructor")).append("\"");
//                        strXML.append(" />");
                    }
                    rs.close();
                    strXML.append("</EvaluacionIns>");
                } catch (SQLException ex) {
                    System.out.println("Error GetConsulta Evaluacion Cursos Instructor: " + ex.getLocalizedMessage());
                }
                strXML.toString();
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML);//Pintamos el resultado
            }//Fin ID 6

            //Regresa lInstructores Disponibles para el curso seleccionado
            if (strid.equals("7")) {
                String strFiltro = "";
                String strCurso = request.getParameter("Curso");
                String strIdIntr = "";
                StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
                strXML.append("<InstructorDisponible>");

                strSql = "select DISTINCT CI_INSTRUCTOR_ID from cofide_cursos_instructor where CCU_ID_M = " + strCurso;

                try {
                    rs = oConn.runQuery(strSql, true);
                    while (rs.next()) {
                        strIdIntr += rs.getString("CI_INSTRUCTOR_ID") + ",";
                    }
                    rs.close();

                    if (strIdIntr.endsWith(",")) {
                        strIdIntr = strIdIntr.substring(0, strIdIntr.length() - 1);
                    }
                    if (!strIdIntr.equals("")) {
                        strSql = "select * from cofide_instructor where CI_INSTRUCTOR_ID in (" + strIdIntr + ")  ORDER BY CI_NOMBRE";

                        rs = oConn.runQuery(strSql, true);
                        while (rs.next()) {
                            strXML.append("<datos");
                            strXML.append(" CI_INSTRUCTOR_ID = \"").append(rs.getInt("CI_INSTRUCTOR_ID")).append("\"");
                            strXML.append(" CI_INSTRUCTOR = \"").append(util.Sustituye(rs.getString("CI_INSTRUCTOR"))).append("\"");
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
            }//Fin ID 7

            //Regresa Nombre del Instructor
            if (strid.equals("8")) {
                String strIdIntr = request.getParameter("IdInstructor");
                StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
                strXML.append("<NombreInstructor>");

                try {
                    String strNombreInstructor = getNomInstructor(oConn, strIdIntr);
                    strXML.append("<datos");
                    strXML.append(" CI_INSTRUCTOR = \"").append(util.Sustituye(strNombreInstructor)).append("\"");
                    strXML.append(" />");

                    strXML.append("</NombreInstructor>");
                } catch (Exception ex) {
                    System.out.println("Error GetNombre INSTRUCTOR :" + ex.getLocalizedMessage());
                }
                strXML.toString();
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML);//Pintamos el resultado
            }//Fin ID 8

            //Consulta Los instructores que impartiran el curso
            if (strid.equals("9")) {
                String strCurso = request.getParameter("strCurso");
                StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
                strXML.append("<InstructorImparte>");
                strSql = "select *,"
                        + "(select CI_INSTRUCTOR from cofide_instructor where cofide_instructor.CI_INSTRUCTOR_ID = cofide_instructor_imparte.CI_INSTRUCTOR_ID)as CC_INSTRUCTOR "
                        + "from cofide_instructor_imparte where CC_CURSO_ID = " + strCurso;
                try {
                    rs = oConn.runQuery(strSql, true);
                    while (rs.next()) {
                        strXML.append("<datos");
                        strXML.append(" CII_ID_INSTR = \"").append(rs.getInt("CI_INSTRUCTOR_ID")).append("\"");
                        strXML.append(" CC_CURSO_ID = \"").append(rs.getInt("CC_CURSO_ID")).append("\"");
                        strXML.append(" CCI_INSTRUCTOR = \"").append(util.Sustituye(rs.getString("CC_INSTRUCTOR"))).append("\"");
                        strXML.append(" II_NUM_HORAS = \"").append(rs.getDouble("II_NUM_HORAS")).append("\"");
                        strXML.append(" />");
                    }
                    rs.close();
                    strXML.append("</InstructorImparte>");
                } catch (SQLException ex) {
                    System.out.println("Error GetConsulta COFIDE CATALOGO: " + ex.getLocalizedMessage());
                }
                strXML.toString();
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML);//Pintamos el resultado
            }//Fin ID 9

        } else {
            out.print("SIN CONEXION.");
        }
        oConn.close();
    }
%>
<%!
    Fechas fec = new Fechas();
    String strRes = "";
    String strSql = "";
    ResultSet rs = null;
    UtilXml util = new UtilXml();

    public String getCursosMesAnterior(Conexion oConn, String strAnio, String strMes, String strClave) {
        StringBuilder strXML = new StringBuilder("");
        Fechas fec = new Fechas();
        String strFechaActual = fec.getFechaActual();
        int intAnioActual = Integer.parseInt(strAnio);
        int intMesActual = Integer.parseInt(strMes);
        boolean blAnioPas = false;
        for (int i = 1; i <= 12; i++) {
            int tmpMes = 0;
            int tmpAnio = 0;

            tmpMes = intMesActual - 1;

            if (tmpMes == 0) {
                tmpAnio = intAnioActual - 1;
                tmpMes = 12;
                blAnioPas = true;
            } else if (!blAnioPas) {
                tmpAnio = intAnioActual;
            } else {
                tmpAnio = intAnioActual - 1;
            }
            String strTmpMes = "" + tmpMes;
            if (strTmpMes.length() == 1) {
                strTmpMes = "0" + strTmpMes;
            }
            strSql = "select *,"
                    + "(select GROUP_CONCAT(concat_ws(', ',CC_SEDE)) from cofide_cursos where cofide_cursos.CC_CLAVES = cofide_catalogo_curso.CCU_ID_M and SUBSTR(cofide_cursos.CC_FECHA_INICIAL,1,6) = '" + tmpAnio + strTmpMes + "') as SEDE_ASIGNADA,"
                    + "(select GROUP_CONCAT(concat_ws(', ',CC_FECHA_INICIAL)) from cofide_cursos where cofide_cursos.CC_CLAVES = cofide_catalogo_curso.CCU_ID_M and SUBSTR(cofide_cursos.CC_FECHA_INICIAL,1,6) = '" + tmpAnio + strTmpMes + "') as FECHA_ASIGNADA "
                    + "from cofide_catalogo_curso where CCU_ID_M = " + strClave;

            try {
                rs = oConn.runQuery(strSql, true);
                while (rs.next()) {
                    String strSedeAsig = "";
                    String strFechaAsig = "";
                    if (rs.getString("SEDE_ASIGNADA") != null) {
                        strSedeAsig = rs.getString("SEDE_ASIGNADA");
                        strSedeAsig = util.Sustituye(strSedeAsig);
                    }
                    if (rs.getString("FECHA_ASIGNADA") != null) {
                        strFechaAsig = fec.FormateaDDMMAAAA(rs.getString("FECHA_ASIGNADA"), "/");
                    }
                    if (strFechaAsig.equals("")) {
                        strXML.append(" CCU_MES" + i + " = \"").append("NO SE IMPARTIO").append("\"");
                    } else {
                        strXML.append(" CCU_MES" + i + " = \"").append(strSedeAsig + " - " + strFechaAsig).append("\"");
                    }
                    strXML.append(" CCU_MES_DESC" + i + " = \"").append(strTmpMes + "/" + tmpAnio).append("\"");
                }
                rs.close();
            } catch (SQLException ex) {
                System.out.println("GET Meses Anteriores: " + ex.getLocalizedMessage());
            }
            intMesActual = tmpMes;
        }
        return strXML.toString();
    }//Fin getCursosMesAnterior

    public String getMailInstructor(Conexion oConn, String strInstructor, String strFecha, String strNomInstructor) {
        String strSql = "SELECT CC_CURSO_ID,CC_FECHA_INICIAL,(select CS_SEDE from cofide_sede where CS_SEDE_ID = CC_SEDE_ID) as Sede,CC_SESION,CC_DURACION_HRS,CC_NOMBRE_CURSO,CC_INSTRUCTOR,CC_IS_PRESENCIAL "
                + "FROM cofide_cursos where CC_INSTRUCTOR_ID = " + strInstructor + " and CC_FECHA_INICIAL like '%" + strFecha + "%' and CC_CLAVES <> ''  and CC_ENVIO_MAIL = 0 "
                + "order by CC_FECHA_INICIAL";
        StringBuilder strXML = new StringBuilder();
        Fechas fec = new Fechas();
        UtilXml util = new UtilXml();
        String strPieCorreo = "<br><br><br>"
                + "De no ser recibida respuesta alguna en los siguientes 4 días hábiles consideraremos que no le es posible apoyarnos en esta programación."
                + "<br><br><br>"
                + "Saludos.<br><br>"
                + "Departamento de Academia<br><br>"
                + "<b>CORPORATIVO FISCAL DÉCADA S.C.</b>";
        strXML.append("<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<style>"
                + "table {"
                + "    font-family: arial, sans-serif;"
                + "    border-collapse: collapse;"
                + "    width: 100%;"
                + "}"
                + "td, th {"
                + "    border: 1px solid #dddddd;"
                + "    text-align: left;"
                + "    padding: 8px;"
                + "}"
                + "tr:nth-child(even) {"
                + "    background-color: #dddddd;"
                + "}"
                + "</style>"
                + "<br>Estimado: " + replaceCharMail(strNomInstructor)
                + "<br><br>"
                + "<br> Favor de revisar su agenda y confirmar fechas y horarios de los siguientes cursos:"
                + "<br><br>"
                + "</head>");
        strXML.append("<table>"
                + "<tr>"
                + "    <th>FECHA DEL EVENTO</th>"
                + "    <th>SEDE</th>"
                + "    <th>TIPO DE EVENTO</th>"
                + "    <th>SESI&Oacute;N</th>"
                + "    <th>DURACI&Oacute;N EN HORAS</th>"
                + "    <th>NOMBRE DEL CURSO</th>"
                + "</tr>");

        try {
            //CC_FECHA_INICIAL,CC_SEDE,CC_SESION,CC_DURACION_HRS,CC_NOMBRE_CURSO,CC_INSTRUCTOR
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                String stFecha = "";
                if (rs.getString("CC_FECHA_INICIAL") != null) {
                    strFecha = fec.FormateaDDMMAAAA(rs.getString("CC_FECHA_INICIAL"), "/");
                }
                strXML.append("<tr>");
                strXML.append("<td style=\"text-align:center\">").append(strFecha).append("</td>");
                strXML.append("<td>").append(replaceCharMail(rs.getString("Sese"))).append("</td>");
                if (rs.getInt("CC_IS_PRESENCIAL") == 1) {
                    strXML.append("<td>").append("PRESENCIAL").append("</td>");
                } else {
                    strXML.append("<td>").append("LINEA").append("</td>");
                }
                strXML.append("<td>").append(rs.getString("CC_SESION")).append("</td>");
                strXML.append("<td style=\"text-align:center\">").append(rs.getString("CC_DURACION_HRS")).append("</td>");
                strXML.append("<td>").append(replaceCharMail(rs.getString("CC_NOMBRE_CURSO"))).append("</td>");
                strXML.append("</tr>");
                String strEnvio = "update cofide_cursos set CC_ENVIO_MAIL = 1 where CC_CURSO_ID = " + rs.getInt("CC_CURSO_ID");
                oConn.runQueryLMD(strEnvio);
            }
            strXML.append("</table>"
                    + replaceCharMail(strPieCorreo)
                    + "</body>"
                    + "</html>");

            rs.close();
        } catch (SQLException ex) {
            System.out.println("Get MailCursos Instructor: " + ex.getLocalizedMessage());
        }

        return strXML.toString();
    }//Fin getMailInstructor

    public String replaceCharMail(String strChar) {
        if (strChar.contains("á")) {
            strChar = strChar.replace("á", "&aacute;");
        }
        if (strChar.contains("é")) {
            strChar = strChar.replace("é", "&eacute;");
        }
        if (strChar.contains("í")) {
            strChar = strChar.replace("í", "&iacute;");
        }
        if (strChar.contains("ó")) {
            strChar = strChar.replace("ó", "&oacute;");
        }
        if (strChar.contains("ú")) {
            strChar = strChar.replace("ú", "&uacute;");
        }
        if (strChar.contains("Á")) {
            strChar = strChar.replace("Á", "&Aacute;");
        }
        if (strChar.contains("É")) {
            strChar = strChar.replace("É", "&Eacute;");
        }
        if (strChar.contains("Í")) {
            strChar = strChar.replace("Í", "&Iacute;");
        }
        if (strChar.contains("Ó")) {
            strChar = strChar.replace("Ó", "&Oacute;");
        }
        if (strChar.contains("Ú")) {
            strChar = strChar.replace("Ú", "&Uacute;");
        }
        return strChar;
    }

    public ArrayList<Integer> getCursoSug(Conexion oConn, String strAnio, String strMes) {
        ArrayList<Integer> intArrCC = new ArrayList<Integer>();

        String strFechaAnt = "";
        strSql = "select DATE_SUB('" + strAnio + "-" + strMes + "-01',INTERVAL '3' MONTH) as FechaAnterior";
        try {
            rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                strFechaAnt = rs.getString("FechaAnterior");
            }
            rs.close();
        } catch (SQLException sx) {
            System.out.println("Error Get FECHA ANTERIOR:" + sx.getLocalizedMessage());
        }
        String strTmpAnio = strFechaAnt.substring(0, 4);
        String strTmpMes = strFechaAnt.substring(5, 7);

        strSql = "select DISTINCT CC_CLAVES from cofide_cursos where CC_FECHA_INICIAL >= '" + strTmpAnio + strTmpMes + "01' and CC_CLAVES <> '';";
        try {
            rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                intArrCC.add(rs.getInt("CC_CLAVES"));
            }
            rs.close();
        } catch (SQLException sx) {
            System.out.println("Error Get Cursos Sugerencia:" + sx.getLocalizedMessage());
        }
        return intArrCC;
    }

    public String getNomInstructor(Conexion oConn, String strInstructor) {
        strSql = "select CI_INSTRUCTOR from cofide_instructor where CI_INSTRUCTOR_ID = " + strInstructor;
        String strNomInst = "";
        try {
            rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                strNomInst = util.Sustituye(rs.getString("CI_INSTRUCTOR"));
            }
            rs.close();
        } catch (SQLException sx) {
            System.out.println("Error Get Nombre Instructor:" + sx.getLocalizedMessage());
        }
        return strNomInst;
    }//Fin GetNom Instructor

    public void setInstructoresImpartir(Conexion oConn, String strInstructores, String strHrsInstructor, String strCursoId) {
        String str = strInstructores;
        String delimiter = ",";
        String[] temp;
        temp = str.split(delimiter);
        String[] tempHrs;
        tempHrs = strHrsInstructor.split(delimiter);
        strSql = "delete from cofide_instructor_imparte where CC_CURSO_ID = " + strCursoId;
        oConn.runQueryLMD(strSql);
        for (int i = 0; i < temp.length; i++) {
            strSql = "insert into cofide_instructor_imparte ( CI_INSTRUCTOR_ID, CC_CURSO_ID, II_NUM_HORAS) values ( " + temp[i] + ", " + strCursoId + "," + tempHrs[i] + ")";
            oConn.runQueryLMD(strSql);
        }
    }//Fin setInstructoresImpartir

    public String[] setDetaCurso(Conexion oConn, String strClaveCurso) {
//detalle, temario, dirigido, requisito, objetivo
        String strDetalle = "";
        String strTemario = "";
        String strDirigido = "";
        String strRequisito = "";
        String strObjetivo = "";
        String[] lstDetalle = new String[5];
        strSql = "select CCU_ID_M, CCU_DETALLE, CCU_TEMARIO, CCU_REQUISITO, CCU_OBJETIVO, CCU_DIRIGIDO "
                + "from cofide_catalogo_curso "
                + "where CCU_ID_M = " + strClaveCurso;
        try {
            rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                if (!rs.getString("CCU_DETALLE").equals("")) {
                    strDetalle = rs.getString("CCU_DETALLE");
                }
                if (!rs.getString("CCU_TEMARIO").equals("")) {
                    strTemario = rs.getString("CCU_TEMARIO");
                }
                if (!rs.getString("CCU_REQUISITO").equals("")) {
                    strRequisito = rs.getString("CCU_REQUISITO");
                }
                if (!rs.getString("CCU_OBJETIVO").equals("")) {
                    strObjetivo = rs.getString("CCU_OBJETIVO");
                }
                if (!rs.getString("CCU_DIRIGIDO").equals("")) {
                    strDirigido = rs.getString("CCU_DIRIGIDO");
                }
                lstDetalle[0] = strDetalle;
                lstDetalle[1] = strTemario;
                lstDetalle[2] = strDirigido;
                lstDetalle[3] = strRequisito;
                lstDetalle[4] = strObjetivo;
            }
            rs.close();
        } catch (SQLException sql) {
            System.out.println("Error al obtener los detalles del curso: " + strClaveCurso + "; ERROR: " + sql);
        }
        return lstDetalle;
    }

%>