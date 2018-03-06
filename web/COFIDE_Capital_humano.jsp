<%-- 
    Document   : COFIDE_Capital_humano
    Created on : 8/11/2016, 06:30:35 PM
    Author     : JulioChavez
--%>

<%@page import="comSIWeb.Utilerias.UtilXml"%>
<%@page import="com.mx.siweb.erp.especiales.cofide.COFIDE_Mail_cursos"%>
<%@page import="java.sql.SQLException"%>
<%@page import="Tablas.CofideParticipantes"%>
<%@page import="comSIWeb.ContextoApt.atrJSP"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="comSIWeb.Utilerias.Fechas"%>
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
    Seguridad seg = new Seguridad(); //Valida que la peticion se halla hecho desde el mismo sitio
    Fechas fec = new Fechas();
    COFIDE_Mail_cursos mg = new COFIDE_Mail_cursos();
    String strSQL;
    ResultSet rs;
    String strResult = "";
    StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
    strXML.append("<vta>");
    UtilXml util = new UtilXml();

    if (varSesiones.getIntNoUser() != 0 && seg.ValidaURL(request)) {
        //Obtenemos parametros
        String strid = request.getParameter("id");
        if (!strid.equals(null)) {
            //confirmar
            if (strid.equals("1")) {
                String strRespuesta = "Hubó un problema al registrar al participante";
                String strEsCofide = request.getParameter("es_cofide");
                String strIdCurso = request.getParameter("id_curso");
                String strNomCurso = request.getParameter("nombre_curso");
                String strColaborador = request.getParameter("colaborador");
                String strTitulo = request.getParameter("titulo");
                String strEmpresa = request.getParameter("empresa");
                String strFecha = request.getParameter("fecha");
                String strCosto = request.getParameter("costo");
                strFecha = fec.FormateaBD(strFecha, "/");
                strSQL = "INSERT INTO cofide_cursos_colaborador (CCP_ES_COFIDE, CC_CURSO_ID, CCP_CURSO, "
                        + "ID_USUARIO, CCP_ID_TITULO, CCP_EMPRESA, CCP_FECHA, CCP_COSTO) VALUES "
                        + "(" + strEsCofide + ", " + strIdCurso + ", '" + strNomCurso + "', " + strColaborador + ", "
                        + "'" + strTitulo + "', '" + strEmpresa + "', '" + strFecha + "', '" + strCosto + "')";
                oConn.runQueryLMD(strSQL);
                if (!oConn.isBolEsError()) {
                    strRespuesta = "OK";
                }
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(strRespuesta);//Pintamos el resultado 
            } //1
            if (strid.equals("2")) {
                CofideParticipantes cco = new CofideParticipantes();
                String strIdCurso = request.getParameter("idCurso");
                String strIdParticipante = request.getParameter("idColaborador");
                String strTitulo = request.getParameter("idTitulo");
                String strTipoCurso = request.getParameter("tipo_curso");
                String strTituloDesc = "";
                String strParticipante = "";
                int intEjecutivo = varSesiones.getIntNoUser();
                String strEmpresa = "PERSONAL DE COFIDE S.C.";
                String strResultado = "";
                strSQL = "select CTI_ABRV from cofide_titulos where CTI_ID = " + strTitulo;
                rs = oConn.runQuery(strSQL, true);
                while (rs.next()) {
                    strTituloDesc = rs.getString("CTI_ABRV");
                }
                rs.close();
                strSQL = "select * from usuarios where id_usuarios = " + strIdParticipante;
                rs = oConn.runQuery(strSQL, true);
                while (rs.next()) {
                    strParticipante = strTituloDesc + " " + rs.getString("nombre_usuario") + " " + rs.getString("apellido_paterno") + " " + rs.getString("apellido_materno");
                    cco.setFieldString("CP_NOMBRE", rs.getString("nombre_usuario"));
                    cco.setFieldString("CP_APPAT", rs.getString("apellido_paterno"));
                    cco.setFieldString("CP_APMAT", rs.getString("apellido_materno"));
                    cco.setFieldString("CP_TITULO", strTituloDesc);
                    cco.setFieldString("CP_ASCOC", "NINGUNA");
                    cco.setFieldString("CP_CORREO", rs.getString("SMTP_US"));
                    cco.setFieldInt("CP_ASISTENCIA", 1);
                    cco.setFieldInt("CP_USUARIO_ALTA", intEjecutivo);
                    cco.setFieldString("CP_TIPO_CURSO", strTipoCurso);
                    cco.setFieldString("CP_ID_CURSO", strIdCurso);
                    cco.setFieldString("CP_TKT_ID", "1");
                    cco.setBolGetAutonumeric(true);
                    int intTipoCurso = 0;
                    intTipoCurso = Integer.parseInt(strTipoCurso);
                    if (!rs.getString("SMTP_US").equals("")) {
                        strResultado = cco.Agrega(oConn); //regresa un okis
                        mg.MailReservacion(oConn, rs.getString("SMTP_US"), strParticipante, strEmpresa, Integer.parseInt(strIdCurso), intEjecutivo, Integer.parseInt(strTipoCurso), "0");
                        if (strTipoCurso.equals("1")) {
                            mg.MailDescargaMaterial(oConn, rs.getString("SMTP_US"), strParticipante, strEmpresa, Integer.parseInt(strIdCurso), intEjecutivo, intTipoCurso, "0");
                        } else {
                            String[] strInfUser = GetUser(oConn, rs.getString("SMTP_US"), strIdParticipante, rs.getString("nombre_usuario"), rs.getString("apellido_paterno") + " " + rs.getString("apellido_materno"), strTituloDesc);
                            System.out.println("user: " + strInfUser[1]);
                            System.out.println("pass: " + strInfUser[0]);
                            System.out.println("id web: " + strInfUser[2]);
                            mg.setStrUsuario(strInfUser[1]); //asigna el usuario
                            mg.setStrContraseña(strInfUser[0]); //asigna la contraseña
                            mg.MailAccesoOnline(oConn, rs.getString("SMTP_US"), strParticipante, strEmpresa, Integer.parseInt(strIdCurso), intEjecutivo, intTipoCurso, "0");
                            mg.VtaWebRH(oConn, Integer.parseInt(strIdCurso), strInfUser[2], strIdParticipante, Integer.parseInt(strTipoCurso), 1);
                        }
                        DescuentaCurso(oConn, strIdCurso);
                    } else {
                        strResultado = "El ejecutivo no cuenta con un Corrreo, favor de verificar";
                    }
                }
                rs.close();
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(strResultado);//Pintamos el resultado 
            } //2
            if (strid.equals("3")) {
                String strTipoCurso = request.getParameter("tipocurso");
                String strIdCurso = "0";
                String strCurso = "Sin opciones...";
                String strCompletar = "";
                if (!strTipoCurso.equals("0")) {
                    if (strTipoCurso.equals("1")) {
//                        strCompletar = " and CC_FECHA_INICIAL >= REPLACE(CURDATE(),'-','') and CC_IS_PRESENCIAL = 1 and CC_IS_DIPLOMADO = 0 AND CC_IS_SEMINARIO = 0 ORDER BY CC_FECHA_INICIAL ASC, CC_HR_EVENTO_INI ASC";
                        strCompletar = " and CC_FECHA_INICIAL >= REPLACE(CURDATE(),'-','') and CC_IS_PRESENCIAL = 1 ORDER BY CC_FECHA_INICIAL ASC, CC_HR_EVENTO_INI ASC";
                    }
                    if (strTipoCurso.equals("2")) {
//                        strCompletar = " and CC_FECHA_INICIAL >= REPLACE(CURDATE(),'-','') and CC_IS_ONLINE = 1 and CC_IS_DIPLOMADO = 0 AND CC_IS_SEMINARIO = 0 ORDER BY CC_FECHA_INICIAL ASC, CC_HR_EVENTO_INI ASC";
                        strCompletar = " and CC_FECHA_INICIAL >= REPLACE(CURDATE(),'-','') and CC_IS_ONLINE = 1 ORDER BY CC_FECHA_INICIAL ASC, CC_HR_EVENTO_INI ASC";
                    }
                    if (strTipoCurso.equals("3")) {
                        strCompletar = " and CC_IS_VIDEO = 1";
                    }
                    strSQL = "select CC_CURSO_ID,concat('ID: ',CC_CURSO_ID,' / ',"
                            + "getFormatDate(CC_FECHA_INICIAL),' / ',CC_HR_EVENTO_INI,' / ',"
                            + "(select CSH_SEDE from cofide_sede_hotel where CSH_ID = CC_SEDE_ID),' / ',"
                            + "SUBSTRING(CC_NOMBRE_CURSO,1,70),'...') as Curso "
                            + "from cofide_cursos where CC_ACTIVO = 1 " + strCompletar;
                    rs = oConn.runQuery(strSQL, true);
                    while (rs.next()) {
                        strIdCurso = rs.getString("CC_CURSO_ID");
                        strCurso = rs.getString("Curso");
                        strXML.append("<datos");
                        strXML.append(" id_curso = \"").append(strIdCurso).append("\"");
                        strXML.append(" curso = \"").append(util.Sustituye(strCurso)).append("\"");
                        strXML.append(" />");
                    }
                    rs.close();
                } else {
                    strXML.append("<datos");
                    strXML.append(" id_curso = \"").append(strIdCurso).append("\"");
                    strXML.append(" curso = \"").append(util.Sustituye(strCurso)).append("\"");
                    strXML.append(" />");
                }
                strXML.append("</vta>");
                strXML.toString();
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML);//Pintamos el resultado
            } //3
        }
    } else {
        System.out.println("Sin acceso");
    }
    oConn.close();

%>

<%!
    COFIDE_Mail_cursos mg = new COFIDE_Mail_cursos();

    public void DescuentaCurso(Conexion oConn, String stridCurso) {
        String strSQL = "select CC_CURSO_ID, CC_INSCRITOS from cofide_cursos where CC_CURSO_ID = " + stridCurso;
        try {
            ResultSet rs = oConn.runQuery(strSQL, true);
            while (rs.next()) {
                strSQL = "update cofide_cursos set CC_INSCRITOS = " + (rs.getInt("CC_INSCRITOS") + 1) + " where CC_CURSO_ID = " + stridCurso;
                oConn.runQueryLMD(strSQL);
            }
            rs.close();
        } catch (SQLException ex) {
            System.out.println("ERROR: " + ex);
        }
    }

    public String[] GetUser(Conexion oConn, String strCorreo, String strIdUsr, String strNom, String Apellido, String strTitulo) {
        String[] listValores = new String[3];
        String strIdUsrWeb = mg.createUsuarioNuevoCurso(strCorreo, Integer.parseInt(strIdUsr), strNom, Apellido, strTitulo);
        try {
            Conexion oConnMGV = new Conexion();
            oConnMGV.setStrNomPool("jdbc/COFIDE");
            oConnMGV.open();
            String strSql = "select id_usr_w,usr,psw from cat_usr_w where id_usr_w = " + strIdUsrWeb;
            ResultSet rs = oConnMGV.runQuery(strSql, true);
            while (rs.next()) {
                listValores[0] = rs.getString("psw");
                listValores[1] = rs.getString("usr");
                listValores[2] = rs.getString("id_usr_w");
            }
            rs.close();
            oConnMGV.close();
        } catch (SQLException ex) {
            System.out.println("GET PASSWORD COFIDE MGV: " + ex.getLocalizedMessage());
        }
        return listValores;
    }
%>