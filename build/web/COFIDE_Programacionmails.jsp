<%-- 
    Document   : COFIDE_Programacionmails
    Created on : 30-mar-2016, 0:09:30
    Author     : juliocesar
--%>

<%@page import="com.mx.siweb.erp.especiales.cofide.Telemarketing"%>
<%@page import="Tablas.vta_tickets"%>
<%@page import="Tablas.vta_facturas"%>
<%@page import="comSIWeb.Operaciones.TableMaster"%>
<%@page import="com.mx.siweb.erp.especiales.cofide.CFD_factura33"%>
<%@page import="com.mx.siweb.erp.especiales.cofide.COFIDE_MailPreferente"%>
<%@page import="comSIWeb.Utilerias.Mail"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="java.sql.SQLException"%>
<%@page import="com.mx.siweb.erp.especiales.cofide.entidades.CrmCorreo"%>
<%@page import="com.mx.siweb.erp.especiales.cofide.entidades.CrmCorreoDeta"%>
<%@page import="com.mx.siweb.erp.especiales.cofide.COFIDE_Mail_cursos"%>
<%@page import="com.mx.siweb.erp.especiales.cofide.CRM_Envio_Template"%>
<%@page import="Tablas.crm_envio_masivo_deta"%>
<%@page import="Tablas.crm_envio_masivo"%>
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
    UtilXml util = new UtilXml();
    Fechas fec = new Fechas();
    COFIDE_Mail_cursos mg = new COFIDE_Mail_cursos(); //enviar mail group
    CRM_Envio_Template crm_tmp = new CRM_Envio_Template();
    Mail mail = new Mail();

    StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
    strXML.append("<Mail>");
    if (varSesiones.getIntNoUser() != 0 && seg.ValidaURL(request)) {
        String strid = request.getParameter("ID");
        if (strid != null) {
            if (strid.equals("1")) { //mostrar mails sugeridos con la fecha de mail masivos del dia de hoy
                String strIdCurso = "";
                String strClave = "";
                String strFechaIni = "";
                String strTemp1 = "";
                String strArea = "";
                String strGiro = "";
                String strSede = "";
                String strNomCurso = "";
                String strFecGroup = "";
                String strConfirm = "";
                String strIdSede = "";
                String strFechaMasivo = fec.getFechaActual();
                String strFecaMasivo = "";
                String strConfirmadoSIoNo = "";
//                System.out.println("####################################### inicia el querytho: ");
                String strSql = "select cc.*,"
                        + "(select csh_sede from cofide_sede_hotel where csh_id = CC_SEDE_ID) as Sede,"
                        + "(select CTT_DESC from cofide_tipo_template where CTT_ID = CC_TEMPLATE1)as strTemplate,"
                        + "(select group_concat(concat_ws(', ',cc_area)) from cofide_curso_segmento where cc_curso_id = cc.cc_curso_id) as cc_areas, "
                        + "(select group_concat(concat_ws(', ',cc_giro)) from cofide_curso_giro where cc_curso_id = cc.cc_curso_id) as cc_giros "
                        + "from cofide_cursos as cc where cc_masivos <= '" + strFechaMasivo + "' and CC_FECHA_INICIAL >= REPLACE(DATE_ADD(CURDATE(),INTERVAL 1 day),'-','')"
                        + " and cc_masivos <> '' and CC_ACTIVO = 1 order by CC_FECHA_INICIAL";
                ResultSet rs = oConn.runQuery(strSql, true);
                while (rs.next()) {
                    strIdCurso = rs.getString("CC_CURSO_ID");
                    strIdSede = rs.getString("CC_SEDE_ID");
                    strClave = rs.getString("CC_CLAVES");

                    if (rs.getString("CC_FECHA_INICIAL") != null && rs.getString("CC_FECHA_INICIAL") != "") {
                        strFechaIni = fec.FormateaDDMMAAAA(rs.getString("CC_FECHA_INICIAL"), "/");
                    } else {
                        strFechaIni = "";
                    }
                    if (rs.getString("CC_MASIVOS") != null && !rs.getString("CC_MASIVOS").equals("")) {
                        strFecaMasivo = fec.FormateaDDMMAAAA(rs.getString("CC_MASIVOS"), "/");
                    } else {
                        strFecaMasivo = "";
                    }

                    strTemp1 = rs.getString("strTemplate");
                    strArea = rs.getString("cc_areas");
                    strGiro = rs.getString("cc_giros");
                    strSede = rs.getString("Sede");
                    strNomCurso = rs.getString("CC_NOMBRE_CURSO");
                    strFecGroup = rs.getString("CC_MAILGROUP");

                    strConfirm = rs.getString("CC_CONFIRMA_MAIL");

                    if (strConfirm.equals("1")) {
                        strConfirmadoSIoNo = "SI";
                    } else {
                        strConfirmadoSIoNo = "NO";
                    }
//                    System.out.println("####################################### id curso " + strIdCurso);
//                    System.out.println("####################################### sede: " + strSede);
                    strXML.append("<datos");
                    strXML.append(" id = \"").append(strIdCurso).append("\"");
                    strXML.append(" id_sede = \"").append(strIdSede).append("\"");
                    strXML.append(" clave = \"").append(strClave).append("\"");
                    strXML.append(" fecini = \"").append(strFechaIni).append("\"");
                    strXML.append(" t1 = \"").append((strTemp1 != null ? strTemp1 : "")).append("\"");
                    strXML.append(" areas = \"").append((strArea == null ? "" : strArea)).append("\"");
                    strXML.append(" giros = \"").append((strGiro == null ? "" : strGiro)).append("\"");
                    strXML.append(" sede = \"").append(util.Sustituye(strSede)).append("\"");
                    strXML.append(" nombre = \"").append(util.Sustituye(strNomCurso)).append("\"");
//                    strXML.append(" nombre = \"").append(strNomCurso).append("\"");
                    strXML.append(" grupo = \"").append(strFecGroup).append("\"");
                    strXML.append(" masivo = \"").append(strFecaMasivo).append("\"");
//                    strXML.append(" confirma = \"").append((strConfirm == "1" ? "SI" : "NO")).append("\"");
                    strXML.append(" confirma = \"").append(strConfirmadoSIoNo).append("\"");
                    strXML.append(" />");
                }
                strXML.append("</Mail>");
                strXML.toString();
                rs.close();
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML);//Pintamos el resultado
            } //fin 1

            if (strid.equals("2")) {  //confirmar plantilla 
                String strIdPlantilla = request.getParameter("id_plantilla");
                String strPlantillaPuntos = request.getParameter("plantilla_puntos");
                crm_envio_masivo crm = new crm_envio_masivo();
                crm_envio_masivo_deta crmd = new crm_envio_masivo_deta();
                //filtro
                String strTipoFiltro = request.getParameter("segmento"); //filtro para el envio de clientes/prospectos/todos/be-correos
                String strGiro = "";
                String strSede = "";
                String strArea = "";
                boolean bolGiro = true;
                boolean bolSede = true;
                boolean bolArea = true;

                if (!request.getParameter("giro").isEmpty()) {
                    strGiro = request.getParameter("giro");
                } else {
                    bolGiro = false;
                }
                if (!request.getParameter("sede").isEmpty()) {
                    strSede = request.getParameter("sede");
                } else {
                    bolSede = false;
                }
                if (!request.getParameter("area").isEmpty()) {
                    strArea = request.getParameter("area");
                } else {
                    bolArea = false;
                }
                String strComplete = "";

                final String strAsunto = URLDecoder.decode(new String(request.getParameter("asunto").getBytes("iso-8859-1")), "UTF-8");
                String strMailMes = request.getParameter("mailmes"); //incluye a los de mail mensual

                String strConfirmaSql = "update crm_correos_deta set CRCD_CONFIRMA = 1 where CRC_ID = " + strIdPlantilla;
                oConn.runQueryLMD(strConfirmaSql);
                //datos del curso
                String strtemplate1 = "";
                String strtemplate2 = "";
                String strtemplate3 = "";
                String strtemplate1d = "";
                String strtemplate2d = "";
                String strtemplate3d = "";
                String strFechaIni = "";
                String strCT_ID = "";
                String strEmail = "";
                String strEmail2 = "";
                String strCorreoPrincipal = "";
//                String strUsuario = varSesiones.getStrUser();
                String strUsuario = "";
                String strSql = "select nombre_usuario from usuarios where id_usuarios = " + varSesiones.getIntNoUser();
                try {
                    ResultSet rs = oConn.runQuery(strSql, true);
                    while (rs.next()) {
                        strUsuario = rs.getString("nombre_usuario");
                    }
                    rs.close();
                } catch (SQLException ex) {
                    System.out.println("Error a obtener el usuario: " + ex.getMessage());
                }

                String strFecha = fec.getFechaActual();
                String strHora = fec.getHoraActual();
                int intCte = 0;
                int intResultado = 0;

                crm.setFieldString("CRM_TEMPLATE", strIdPlantilla); //id de la plantilla evento
                crm.setFieldString("CRM_FECHAFIN", strFechaIni);
                crm.setFieldString("CRM_FECHA", strFecha);
                crm.setFieldString("CRM_USUARIO", strUsuario);
                crm.setFieldString("CRM_HORA", strHora);
                crm.setFieldString("CRM_CURSO", "0");
                crm.setFieldString("CRM_ASUNTO", strAsunto);
                crm.setFieldString("CRM_MENSUAL", strMailMes);
                crm.setFieldString("CRM_GIRO", strGiro);
                crm.setFieldString("CRM_SEDE", strSede);
                crm.setFieldString("CRM_AREA", strArea);
                crm.setFieldString("CRM_TIPO", strTipoFiltro); //1 general / 2 prospecto / 3 ex participantes
                crm.setBolGetAutonumeric(true);
                crm.Agrega(oConn); //guarda la plantilla para ser enviada
                String Result1 = crm.getValorKey(); //el id master que ocupare para el mail masivo deta
                intResultado = Integer.parseInt(Result1);
                String strFiltroCte = "";

                //Envio Masivo a ontactos de acuerdo a los puntos de lealta
                if (strPlantillaPuntos.equals("8")) {
                    int intPLealtad = Integer.parseInt(request.getParameter("p_lealtad"));
                    strComplete = " where CONTACTO_ID in (" + getStrIdsPuntos(oConn, intPLealtad) + ");";

                    strCorreoPrincipal = ", '' as Principal ";

                } else {

                    if (strTipoFiltro.equals("2")) {

                        strFiltroCte = " and CT_ES_PROSPECTO = 1"; //prospecto

                    } else if (strTipoFiltro.equals("3")) {

                        strFiltroCte = " and CT_ES_PROSPECTO = 0"; //cliente

                    }

                    if (bolGiro || bolSede || bolArea) { // con filtros
                        String strSedeDesc = "select CS_SEDE from cofide_sede where CS_SEDE_ID in (" + strSede + ")";
                        String strAreaDesc = "select CS_AREA from cofide_segmento where CS_ID_M in (" + strArea + ")";
                        String strGiroDesc = "select CG_GIRO from cofide_giro where CG_ID_M in (" + strGiro + ")";
                        if (bolGiro) { //con giro

                            if (bolArea) { //con giro, con area

                                if (bolSede) { //con giro, con area, con sede

                                    strComplete = " where CT_ID in (select CT_ID from vta_cliente "
                                            + "where CT_GIRO in (" + strGiroDesc + ") "
                                            + "and CT_SEDE in (" + strSedeDesc + ") and CT_ACTIVO = 1 " + strFiltroCte + ")"
                                            + "and CCO_AREA in (" + strAreaDesc + ") "
                                            + " and CCO_CORREO <> ''";

                                    strCorreoPrincipal = ",(select CT_EMAIL1 from vta_cliente "
                                            + "where CT_GIRO in (" + strGiroDesc + ")"
                                            + "and CT_SEDE in (" + strSedeDesc + ") and CT_ACTIVO = 1 "
                                            + strFiltroCte
                                            + " and vta_cliente.CT_ID = cofide_contactos.CT_ID) as Principal ";

                                } else { //con giro, con area, sin sede

                                    strComplete = " where CT_ID in (select CT_ID from vta_cliente "
                                            + "where CT_GIRO in (" + strGiroDesc + ") "
                                            + "and CT_ACTIVO = 1 " + strFiltroCte + ")"
                                            + "and CCO_AREA in (" + strAreaDesc + ") "
                                            + "and CCO_CORREO <> ''";

                                    strCorreoPrincipal = ",(select CT_EMAIL1 from vta_cliente "
                                            + "where CT_GIRO in (" + strGiroDesc + ") "
                                            + "and CT_ACTIVO = 1 "
                                            + strFiltroCte
                                            + " and vta_cliente.CT_ID = cofide_contactos.CT_ID  ) as Principal ";
                                }

                            } else { // con giro, sin area

                                if (bolSede) { //con giro, sin area, con sede

                                    strComplete = " where CT_ID in (select CT_ID from vta_cliente "
                                            + "where CT_GIRO in (" + strGiroDesc + ") "
                                            + "and CT_SEDE in (" + strSedeDesc + ") and CT_ACTIVO = 1 " + strFiltroCte + ")"
                                            + " and CCO_CORREO <> ''";

                                    strCorreoPrincipal = ",(select CT_EMAIL1 from vta_cliente "
                                            + "where CT_GIRO in (" + strGiroDesc + ") "
                                            + "and CT_SEDE in (" + strSedeDesc + ") "
                                            + "and CT_ACTIVO = 1 "
                                            + strFiltroCte
                                            + " and vta_cliente.CT_ID = cofide_contactos.CT_ID ) as Principal";

                                } else { // con giro, sin area, sin sede

                                    strComplete = " where CT_ID in (select CT_ID from vta_cliente "
                                            + "where CT_GIRO in (" + strGiroDesc + ") "
                                            + "and CT_ACTIVO = 1 " + strFiltroCte + ")"
                                            + " and CCO_CORREO <> ''";

                                    strCorreoPrincipal = ",(select CT_EMAIL1 from vta_cliente "
                                            + "where CT_GIRO in (" + strGiroDesc + ") "
                                            + "and CT_ACTIVO = 1 "
                                            + strFiltroCte
                                            + " and vta_cliente.CT_ID = cofide_contactos.CT_ID ) as Principal ";

                                }

                            }

                        } else { // sin giro

                            if (bolArea) { // sin giro, con area

                                if (bolSede) { //sin giro, con area, con sede

                                    strComplete = " where CT_ID in (select CT_ID from vta_cliente "
                                            + "where CT_SEDE in (" + strSedeDesc + ") and CT_ACTIVO = 1 " + strFiltroCte + ")"
                                            + "and CCO_AREA in (" + strAreaDesc + ") "
                                            + " and CCO_CORREO <> ''";

                                    strCorreoPrincipal = ",(select CT_EMAIL1 from vta_cliente "
                                            + "where CT_SEDE in (" + strSedeDesc + ") "
                                            + "and CT_ACTIVO = 1 "
                                            + strFiltroCte
                                            + " and vta_cliente.CT_ID = cofide_contactos.CT_ID ) as Principal ";

                                } else { //sin giro, con area, sin sede

                                    strComplete = " where CT_ID in (select CT_ID from vta_cliente "
                                            + "where CT_ACTIVO = 1 " + strFiltroCte + ")"
                                            + "and CCO_AREA in (" + strAreaDesc + ") "
                                            + " and CCO_CORREO <> ''";

                                    strCorreoPrincipal = ",(select CT_EMAIL1 from vta_cliente "
                                            + "where CT_ACTIVO = 1 "
                                            + strFiltroCte
                                            + " and vta_cliente.CT_ID = cofide_contactos.CT_ID ) as Principal ";

                                }

                            } else { // sin giro, sin area

                                if (bolSede) { //sin giro, sin area, con sede

                                    strComplete = " where CT_ID in (select CT_ID from vta_cliente "
                                            + "where CT_SEDE in (" + strSedeDesc + ") and CT_ACTIVO = 1 " + strFiltroCte + ")"
                                            + " and CCO_CORREO <> ''";

                                    strCorreoPrincipal = ",(select CT_EMAIL1 from vta_cliente "
                                            + "where CT_SEDE in (" + strSedeDesc + ") "
                                            + "and CT_ACTIVO = 1 "
                                            + strFiltroCte
                                            + " and vta_cliente.CT_ID = cofide_contactos.CT_ID ) as Principal ";

                                } else { // sin giro, sin area, sin sede

                                    strComplete = " where CT_ID in (select CT_ID from vta_cliente "
                                            + "where CT_ACTIVO = 1 " + strFiltroCte + ")"
                                            + " and CCO_CORREO <> ''";

                                    strCorreoPrincipal = ",(select CT_EMAIL1 from vta_cliente "
                                            + "where CT_ACTIVO = 1 "
                                            + strFiltroCte
                                            + " and vta_cliente.CT_ID = cofide_contactos.CT_ID ) as Principal ";
                                }

                            }

                        }
                    } else { // sin filtro

                        strComplete = " where CT_ID in (select CT_ID from vta_cliente "
                                + "where CT_ACTIVO = 1 " + strFiltroCte + ")"
                                + " and CCO_CORREO <> ''";

                        strCorreoPrincipal = ",(select CT_EMAIL1 from vta_cliente "
                                + "where CT_ACTIVO = 1 "
                                + strFiltroCte
                                + " and vta_cliente.CT_ID = cofide_contactos.CT_ID ) as Principal ";

                    }

                }

                String strCorreo1 = ""; //correo principal
                String strResp = "";
                strSql = "select *" + strCorreoPrincipal + " from cofide_contactos " + strComplete;

//                System.out.println("################################# va a entrar al query\n" + strSql);
                ResultSet rsCte = oConn.runQuery(strSql, true);
                while (rsCte.next()) {

                    strResp = "OK";
                    strEmail = rsCte.getString("CCO_CORREO");
                    strEmail = strEmail.trim().toLowerCase();
                    strEmail2 = rsCte.getString("CCO_CORREO2");
                    strEmail2 = strEmail2.trim().toLowerCase();
                    strCorreo1 = rsCte.getString("Principal");
                    strCorreo1 = strCorreo1.trim().toLowerCase();

                    if (!strCorreo1.isEmpty() && !strCorreo1.equals("") && mail.isEmail(strCorreo1)) {
                        if (!bolValidaDuplicado(oConn, strCorreo1, intResultado)) {
                            if (!getListaNegra(oConn, strCorreo1)) {

                                strCT_ID = rsCte.getString("CT_ID");
                                crmd.setFieldString("CT_ID", strCT_ID);
                                crmd.setFieldString("CRMD_EMAIL", strCorreo1);
                                crmd.setFieldInt("CRM_ID", intResultado);
                                crmd.setFieldString("CRMD_ESTATUS_ENVIO", "0");
                                crmd.setFieldString("CRMD_ESTATUS", "0");
                                crmd.setBolGetAutonumeric(true);
                                crmd.Agrega(oConn); //agrega el detalle de los mail principal  

                            } else {
                                System.out.println("Correo en lista negra: " + strCorreo1);
                            }
                        } else {
                            System.out.println(" El correo ya existe en el listado: " + strCorreo1);
                        }
                    } else {
                        System.out.println(" El correo : " + strCorreo1 + " no es un correo.");
                    }

                    if (!strEmail.isEmpty() && !strEmail.equals("") && mail.isEmail(strEmail)) {
                        if (!bolValidaDuplicado(oConn, strEmail, intResultado)) {
                            if (!getListaNegra(oConn, strEmail)) {

                                strCT_ID = rsCte.getString("CT_ID");
                                crmd.setFieldString("CT_ID", strCT_ID);
                                crmd.setFieldString("CRMD_EMAIL", strEmail);
                                crmd.setFieldInt("CRM_ID", intResultado);
                                crmd.setFieldString("CRMD_ESTATUS_ENVIO", "0");
                                crmd.setFieldString("CRMD_ESTATUS", "0");
                                crmd.setBolGetAutonumeric(true);
                                crmd.Agrega(oConn); //agrega el detalle de los mail principal 

                            } else {
                                System.out.println("Correo en lista negra: " + strEmail);
                            }
                        } else {
                            System.out.println(" El correo ya existe en el listado: " + strEmail);
                        }
                    } else {
                        System.out.println(" El correo : " + strEmail + " no es un correo.");
                    }

                    if (!strEmail2.isEmpty() && !strEmail2.equals("") && mail.isEmail(strEmail2)) {
                        if (!bolValidaDuplicado(oConn, strEmail2, intResultado)) {
                            if (!getListaNegra(oConn, strEmail2)) { //falso = no existe en lista negra

                                strCT_ID = rsCte.getString("CT_ID");
                                crmd.setFieldString("CT_ID", strCT_ID);
                                crmd.setFieldString("CRMD_EMAIL", strEmail2);
                                crmd.setFieldInt("CRM_ID", intResultado);
                                crmd.setFieldString("CRMD_ESTATUS_ENVIO", "0");
                                crmd.setFieldString("CRMD_ESTATUS", "0");
                                crmd.setBolGetAutonumeric(true);
                                crmd.Agrega(oConn); //agrega el detalle de los mail principal     

                            } else { // lista negra
                                System.out.println("Correo en lista negra: " + strEmail2);
                            }
                        } else {
                            System.out.println(" El correo ya existe en el listado: " + strEmail2);
                        }
                    } else {
                        System.out.println(" El correo : " + strEmail2 + " no es un correo.");
                    } //es correo

                }
//                System.out.println("######################### TERMINO");
                rsCte.close();

                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(strResp);//Pintamos el resultado
            } // fin 2

            if (strid.equals("3")) { // 
                String strFecIni = fec.FormateaBD(request.getParameter("fecini"), "/");
                String strFecFin = fec.FormateaBD(request.getParameter("fecfin"), "/");
                String strCrm_Id = "";
                String strCrm_Fecha = "";
                String strCrm_Hora = "";
                String strCrm_Usuario = "";
                String strCrm_template = "";
                String strCrm_Curso = "";
                String strCrm_CursoNombre = "";
                String strCrm_Asunto = "";
                String strSqlEmail = "select crm_envio_masivo.*,"
                        + "(select CONCAT_WS('',GROUP_CONCAT("
                        + "(select CC_NOMBRE_CURSO from cofide_cursos where CC_CURSO_ID = CRCD_ID_CURSO)"
                        + ")) from crm_correos_deta where CRC_ID = CRM_TEMPLATE) as CC_NOMBRE_CURSO "
                        + "from crm_envio_masivo where CRM_FECHA between '" + strFecIni + "' and '" + strFecFin + "' order by CRM_FECHA";
                ResultSet rs = oConn.runQuery(strSqlEmail, true);
                while (rs.next()) {
                    strCrm_Id = rs.getString("CRM_ID");
                    if (rs.getString("CRM_FECHA") != null && !rs.getString("CRM_FECHA").equals("")) {
                        strCrm_Fecha = fec.FormateaDDMMAAAA(rs.getString("CRM_FECHA"), "/");
                    } else {
                        strCrm_Fecha = "";
                    }

                    strCrm_Hora = rs.getString("CRM_HORA");
                    strCrm_Usuario = rs.getString("CRM_USUARIO");
                    strCrm_template = rs.getString("CRM_TEMPLATE");
                    strCrm_Curso = rs.getString("CRM_CURSO");
                    strCrm_CursoNombre = rs.getString("CC_NOMBRE_CURSO");
                    strCrm_Asunto = rs.getString("CRM_ASUNTO");

                    if (strCrm_template != null) {
                        String strSqlEmail2 = "select CRC_ID_PLANTILLA from crm_correos where CRC_ID = " + strCrm_template;
                        ResultSet rs2 = oConn.runQuery(strSqlEmail2, true);
                        while (rs2.next()) {
                            strCrm_template = getPlantilla(oConn, rs2.getString("CRC_ID_PLANTILLA"));
                        }
                        rs2.close();
                    } else {
                        strCrm_template = "";
                    }
                    strXML.append("<datos");
                    strXML.append(" id = \"").append(strCrm_Id).append("\"");
                    strXML.append(" fecha = \"").append(strCrm_Fecha).append("\"");
                    strXML.append(" hora = \"").append(strCrm_Hora).append("\"");
                    strXML.append(" usuario = \"").append(strCrm_Usuario).append("\"");
                    strXML.append(" template = \"").append(util.Sustituye(strCrm_template)).append("\"");
                    strXML.append(" curso = \"").append(util.Sustituye(strCrm_CursoNombre)).append("\"");
                    strXML.append(" asunto = \"").append(util.Sustituye(strCrm_Asunto)).append("\"");
                    strXML.append(" />");
                }
                strXML.append("</Mail>");
                strXML.toString();
                rs.close();
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML);//Pintamos el resultado
            } //3
            if (strid.equals("4")) { // 
                String strCrm_id = request.getParameter("idm");
                String strCT_ID = "";
                String strCrmd_ID = "";
                String strCrmd_Correo = "";
                String strCrmd_Procesado = "";
                String strSqlMailD = "select * from crm_envio_masivo_deta WHERE CRM_ID  = " + strCrm_id + " order by CT_ID";
                ResultSet rs = oConn.runQuery(strSqlMailD, true);
                while (rs.next()) {
                    strCT_ID = rs.getString("CT_ID");
                    strCrmd_ID = rs.getString("CRMD_ID");
                    strCrmd_Correo = rs.getString("CRMD_EMAIL");
                    strCrmd_Procesado = rs.getString("CRM_PROCESADO");
                    strXML.append("<datos");
                    strXML.append(" id_cte = \"").append(strCT_ID).append("\"");
                    strXML.append(" idm = \"").append(strCrm_id).append("\"");
                    strXML.append(" id = \"").append(strCrmd_ID).append("\"");
                    strXML.append(" mail = \"").append(strCrmd_Correo).append("\"");
                    strXML.append(" procesado = \"").append(strCrmd_Procesado == "0" ? "NO" : "SI").append("\"");
                    strXML.append(" />");
                }
                strXML.append("</Mail>");
                strXML.toString();
                rs.close();
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML);//Pintamos el resultado
            } //fin4
            if (strid.equals("6")) { //llena listado de cursos sugeridos mailgroup
                String strIdCurso = "";
                String strClave = "";
                String strFechaIni = "";
                String strTemp2 = "";
                String strArea = "";
                String strGiro = "";
                String strSede = "";
                String strNomCurso = "";
                String strFecGroup = "";
                String strConfirm = "";
                String strIdSede = "";
                String strConfirmadoSIoNo = "";
                String strFechaActualGroup = "";
                String strSql = "select cc.*,"
                        + "(select csh_sede from cofide_sede_hotel where csh_id = CC_SEDE_ID) as Sede,"
                        + "(select CTT_DESC from cofide_tipo_template where CTT_ID = CC_TEMPLATE1)as strTemplate2,"
                        + "(select group_concat(concat_ws(', ',cc_area)) from cofide_curso_segmento where cc_curso_id = cc.cc_curso_id) as cc_areas, "
                        + "(select group_concat(concat_ws(', ',cc_giro)) from cofide_curso_giro where cc_curso_id = cc.cc_curso_id) as cc_giros "
                        + "from cofide_cursos as cc "
                        + "where CC_MAILGROUP <= '" + fec.getFechaActual() + "' "
                        + "and CC_FECHA_INICIAL >= REPLACE(DATE_ADD(CURDATE(),INTERVAL 1 day),'-','') and CC_MAILGROUP <> ''"
                        + " and CC_ACTIVO = 1 "
                        + "order by CC_FECHA_INICIAL";
                ResultSet rs = oConn.runQuery(strSql, true);
                while (rs.next()) {
                    strIdCurso = rs.getString("CC_CURSO_ID");
                    strIdSede = rs.getString("CC_SEDE_ID");
                    strClave = rs.getString("CC_CLAVES");
                    strFechaIni = rs.getString("CC_FECHA_INICIAL");
//                    strTemp2 = rs.getString("CC_TEMPLATE2");
                    strTemp2 = rs.getString("strTemplate2");
                    strArea = rs.getString("cc_areas");
                    strGiro = rs.getString("cc_giros");
                    strSede = rs.getString("Sede");
//                    strSede = rs.getString("CC_SEDE");
                    strNomCurso = rs.getString("CC_NOMBRE_CURSO");
                    strFecGroup = rs.getString("CC_MAILGROUP");
                    strConfirm = rs.getString("CC_CONFIRMA_MAIL");

                    if (strConfirm.equals("1")) {
                        strConfirmadoSIoNo = "SI";
                    } else {
                        strConfirmadoSIoNo = "NO";
                    }
                    strXML.append("<datos");
                    strXML.append(" id = \"").append(strIdCurso).append("\"");
                    strXML.append(" id_sede = \"").append(strIdSede).append("\"");
                    strXML.append(" clave = \"").append(strClave).append("\"");
                    strXML.append(" fecini = \"").append(strFechaIni).append("\"");
                    strXML.append(" t2 = \"").append((strTemp2 != null ? strTemp2 : "")).append("\"");
                    strXML.append(" areas = \"").append((strArea == null ? "" : strArea)).append("\"");
                    strXML.append(" giros = \"").append((strGiro == null ? "" : strGiro)).append("\"");
                    strXML.append(" sede = \"").append(strSede).append("\"");
//                    strXML.append(" nombre = \"").append(strNomCurso).append("\"");
                    strXML.append(" nombre = \"").append(util.Sustituye(strNomCurso)).append("\"");
                    strXML.append(" grupo = \"").append(strFecGroup).append("\"");
                    strXML.append(" confirma = \"").append(strConfirmadoSIoNo).append("\"");
//                    strXML.append(" confirma = \"").append((strConfirm == "1" ? "SI" : "NO")).append("\"");
                    strXML.append(" />");
                }
                strXML.append("</Mail>");
                strXML.toString();
                rs.close();
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML);//Pintamos el resultado
            } //6
            if (strid.equals("7")) {
                CrmCorreo crmc = new CrmCorreo();
                CrmCorreoDeta crmcd = new CrmCorreoDeta();
                int intLength = Integer.parseInt(request.getParameter("length"));

                String strCC_ID_CURSO = "0";
                String strTemplate = request.getParameter("template");
                int intTipoMail = 1;
                int intIdPlantilla = 0;
                if (request.getParameter("tipo") != null) {
                    if (request.getParameter("tipo").equals("1")) {
                        intTipoMail = 1;
                    } else {
                        intTipoMail = 2;
                    }
                } else {
                    intTipoMail = 1;
                }

                //crea la plantilla contenedora
                crmc.setFieldInt("CRC_ID_PLANTILLA", Integer.parseInt(strTemplate));
                crmc.setBolGetAutonumeric(true);
                crmc.Agrega(oConn);
                intIdPlantilla = Integer.parseInt(crmc.getValorKey());
//                System.out.println(intLength + " tamaño de cursos");
                if (intLength > 0) {
                    for (int i = 0; i < intLength; i++) {
                        strCC_ID_CURSO = request.getParameter("id_curso" + i);
                        //llena la plantilla
                        crmcd.setFieldInt("CRC_ID", intIdPlantilla);
                        crmcd.setFieldInt("CRCD_ID_CURSO", Integer.parseInt(strCC_ID_CURSO));
                        crmcd.setFieldInt("CRCD_TIPO", intTipoMail);
                        crmcd.setFieldInt("CRCD_ESTATUS", 0);
                        crmcd.setFieldInt("CRCD_CONFIRMA", 0);
                        crmcd.Agrega(oConn);
                        //actualiza las plantillas en los cursos
                        String strSql = "update cofide_cursos set CC_TEMPLATE1 = " + strTemplate + " where CC_CURSO_ID = " + strCC_ID_CURSO;
                        oConn.runQueryLMD(strSql);
                    }
                } else {
                    //plantillas sin detalles del curso, plantillas personalizadas
                    if (strTemplate.equals("8") || strTemplate.equals("9") || strTemplate.equals("10")) {
                        int intPLealtad = Integer.parseInt(request.getParameter("id_curso"));
                        crmcd.setFieldInt("CRC_ID", intIdPlantilla);
                        crmcd.setFieldInt("CRCD_ID_CURSO", intPLealtad);
                        crmcd.setFieldInt("CRCD_TIPO", 1);
                        crmcd.setFieldInt("CRCD_ESTATUS", 0);
                        crmcd.setFieldInt("CRCD_CONFIRMA", 0);
                        crmcd.Agrega(oConn);
                    }
                }

//                System.out.println(intIdPlantilla + " plantilla");//Pintamos el resultado
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(intIdPlantilla);//Pintamos el resultadoo
            } //7
            if (strid.equals("8")) {
                String strAsunto = "";
                String strIdPlantilla = "";
                int strIdPlantillaD = 0;
                int intAgente = varSesiones.getIntNoUser();
                String strSql = "";
                ResultSet rs;
                String strIdCte = request.getParameter("id_cte");
                int intIdCte = Integer.parseInt(strIdCte);
                String strIdCurso = "";
                boolean bolMG = false;
                String strRespuesta = "";
                strSql = "select *,"
                        + "(select CRC_ID_PLANTILLA from crm_correos where crm_correos.CRC_ID = crm_correos_deta.CRC_ID) as idPlantilla "
                        + "from crm_correos_deta where CRCD_CONFIRMA = 1 and CRCD_TIPO = 2 order by CRCD_ID desc limit 1";
                rs = oConn.runQuery(strSql, true);
                while (rs.next()) {
                    bolMG = true;
                    strAsunto = rs.getString("CRCD_ASUNTO");
                    strIdPlantilla = getPlantilla(oConn, rs.getString("idPlantilla")); //plantilla seleccionada
                    strIdPlantillaD = rs.getInt("CRC_ID"); //vista previa que se genero
                    strIdCurso = rs.getString("CRCD_ID_CURSO"); //curso 
                }
                rs.close();
                if (bolMG) {
                    strSql = "select * from vta_cliente where CT_ID = " + strIdCte;
                    rs = oConn.runQuery(strSql, true);
                    while (rs.next()) {
                        if (!rs.getString("CT_EMAIL1").equals("")) {
                            strRespuesta = mg.MailGroup(oConn, intAgente, rs.getString("CT_EMAIL1"), strAsunto, 0, strIdPlantilla, strIdPlantillaD, intIdCte, rs.getString("CT_MAILMES"), strIdCurso);
                        } else {
                            strRespuesta = "OK";
                        }
                    }
                    rs.close();
                    strSql = "select * from cofide_contactos where CT_ID = " + strIdCte;
                    rs = oConn.runQuery(strSql, true);
                    while (rs.next()) {
                        if (!rs.getString("CCO_CORREO").equals("")) {
                            strRespuesta = mg.MailGroup(oConn, intAgente, rs.getString("CCO_CORREO"), strAsunto, 0, strIdPlantilla, strIdPlantillaD, intIdCte, rs.getString("CT_MAILMES"), strIdCurso);
                        }
                        if (!rs.getString("CCO_CORREO2").equals("")) {
                            strRespuesta = mg.MailGroup(oConn, intAgente, rs.getString("CCO_CORREO2"), strAsunto, 0, strIdPlantilla, strIdPlantillaD, intIdCte, rs.getString("CT_MAILMES2"), strIdCurso);
                        }
                    }
                    rs.close();
                } else {
                    strRespuesta = "FALSE";
                }
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(strRespuesta);
            }
            if (strid.equals("9")) {
                String strRespuesta = "";
                String strMailMes = request.getParameter("mailmes");
                String strIdPlantilla = request.getParameter("id_plantilla");
//                String strAsunto = request.getParameter("asunto");
                final String strAsunto = URLDecoder.decode(new String(request.getParameter("asunto").getBytes("iso-8859-1")), "UTF-8");
                String strConfirmaSql = "update crm_correos_deta set CRCD_CONFIRMA = 1,CRCD_ASUNTO = '" + strAsunto + "' where CRC_ID = " + strIdPlantilla;
                oConn.runQueryLMD(strConfirmaSql);
                if (!oConn.isBolEsError()) {
                    strRespuesta = "OK";
                }
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(strRespuesta);
            }
            if (strid.equals("10")) {
                String strCorreo = request.getParameter("email");
                String strCorreo2 = request.getParameter("email2");
                int intCurso = 0;
                if (!request.getParameter("curso").equals("")) {
                    intCurso = Integer.parseInt(request.getParameter("curso"));
                }
                String strMes = request.getParameter("id_mes");
                String strTipo = request.getParameter("tipo_mail");
                String strRespuesta = "";
                int intAgente = varSesiones.getIntNoUser();
                if (intCurso != 0) {
                    if (!strCorreo.equals("")) {
                        strRespuesta = crm_tmp.MailDirecto(oConn, strCorreo, intCurso, intAgente);
                    }
                    if (!strCorreo2.equals("")) {
                        strRespuesta = crm_tmp.MailDirecto(oConn, strCorreo2, intCurso, intAgente);
                    }
                }

                if (strTipo.equals("1")) { //mensual

                    if (!strMes.equals("")) {

                        if (!strCorreo.equals("")) {
                            strRespuesta = crm_tmp.CofideMensualDirecto(oConn, intAgente, strCorreo, "Tenemos estos cursos para el mes de: " + getMesNombre(oConn, strMes), strMes);
                        }

                        if (!strCorreo2.equals("")) {
                            strRespuesta = crm_tmp.CofideMensualDirecto(oConn, intAgente, strCorreo2, "Tenemos estos cursos para el mes de: " + getMesNombre(oConn, strMes), strMes);
                        }

                    }

                }

                if (strTipo.equals("2")) { //sede

                    if (!strMes.equals("")) {
//                        System.out.println("sede " + strMes);
                        if (!strCorreo.equals("")) {
                            strRespuesta = crm_tmp.CofideSedeDirecto(oConn, intAgente, strCorreo, strMes, "Tenemos estos cursos en la sede de: " + strMes);
                        }

                        if (!strCorreo2.equals("")) {
                            strRespuesta = crm_tmp.CofideSedeDirecto(oConn, intAgente, strCorreo2, strMes, "Tenemos estos cursos en la sede de: " + strMes);
                        }

                    }

                }

                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(strRespuesta);
            } //fin 10
            if (strid.equals("11")) {
                String strIdMasivo = request.getParameter("idMasivo");
                String strSql = "select count(*) as total, "
                        + "(select count(*) from crm_envio_masivo_deta as c1 where c1.CRM_ID = crm_envio_masivo_deta.CRM_ID and CRM_PROCESADO = 1) as Enviados, "
                        + "(select count(*) from crm_envio_masivo_deta as c3 where c3.CRM_ID = crm_envio_masivo_deta.CRM_ID and CRM_PROCESADO = 1 and CRMD_ESTATUS = 'CORRECTO') as Correcto,"
                        + "(select count(*) from crm_envio_masivo_deta as c4 where c4.CRM_ID = crm_envio_masivo_deta.CRM_ID and CRM_PROCESADO = 1 and CRMD_ESTATUS <> 'CORRECTO') as Error "
                        + "from crm_envio_masivo_deta  where CRM_ID = " + strIdMasivo;
                String strProceso = "";
                String strExito = "";
                String strDelivery = "";
                try {
                    ResultSet rs = oConn.runQuery(strSql, true);
                    while (rs.next()) {
                        strProceso = rs.getString("Enviados") + " de " + rs.getString("total"); // # enviados / # total
                        strExito = rs.getString("Correcto");
                        strDelivery = rs.getString("Error");
                        strXML.append("<datos");
                        strXML.append(" proceso = \"").append(strProceso).append("\"");
                        strXML.append(" exito = \"").append(strExito).append("\"");
                        strXML.append(" delivery = \"").append(strDelivery).append("\"");
                        strXML.append(" />");
                    }
                    strXML.append("</Mail>");
                    strXML.toString();
                    rs.close();
                } catch (SQLException sql) {
                    System.out.println("ocurrio un error al obtener le estatus del masivo " + sql.getMessage());
                }
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML);//Pintamos el resultado
            } // fin 11

            //ID 12
            //Envio de la tarjeta Cliente Preferente
            if (strid.equals("12")) {
                String strRes = "";
                //Recuperamos parametros
                String strContactoId = request.getParameter("strContactoId");

                //Recuperamos paths de web.xml
                String strPathXML = this.getServletContext().getInitParameter("PathMembresias");
                String strPathBase = this.getServletContext().getRealPath("/");

                //objeto de COFIDE_MailPreferente
                COFIDE_MailPreferente mailPref = new COFIDE_MailPreferente(oConn, varSesiones, request);
                mailPref.setStrPATHXml(strPathXML);
                mailPref.setStrPATHBase(strPathBase);
                mailPref.setStrContactoId(strContactoId);
                //mailPref.setStrNomFileJasper("repo_cliente_preferente.jrxml");
                mailPref.doEnvioCtPreferente(strContactoId);

                String strResult = mailPref.getStrResultLast();
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(strResult);//Pintamos el resultado
            }//Fin ID 12
            if (strid.equals("13")) {
                String strresp = "";
                final String strPlantilla = URLDecoder.decode(new String(request.getParameter("plantilla").getBytes("iso-8859-1")), "UTF-8");
//                System.out.println(strPlantilla);
                String strSql = "UPDATE mailtemplates SET MT_CONTENIDO='" + strPlantilla + "' WHERE MT_ID=61";
                oConn.runQueryLMD(strSql);
                if (!oConn.isBolEsError()) {
                    strresp = "OK";
                } else {
                    strresp = "Hubó un error al actualizar la plantilla: [ " + oConn.getStrMsgError() + " ]";
                    System.out.println(strresp);
                }
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(strresp);//Pintamos el resultado
            } //fin ID 13
            //Envio de la factura
            if (strid.equals("14")) {
                String strRes = "";
                //Recuperamos parametros                
                String strIdFac = request.getParameter("id_fac");
                int intIdFac = Integer.parseInt(strIdFac);
                TableMaster factura = new vta_facturas();
                factura.ObtenDatos(intIdFac, oConn);
                //OBTENEMOS EL FOLIO FISCAL DE LA FACTURA
                String strFolioFiscal = factura.getFieldString("FAC_FOLIO");

//                System.out.println("FOLIO FISCAL ############  " + strFolioFiscal);
                //Recuperamos paths de web.xml UBICACIÓN DE LOS XML
                String strPathXML = this.getServletContext().getInitParameter("PathXml");
                String strPathBase = this.getServletContext().getRealPath("/");

//                System.out.println("PATH ############ " + strPathXML);
                //OBJETO PARA CREAR LA FACTURA
                CFD_factura33 cfdi = new CFD_factura33(oConn, varSesiones, request);
                cfdi.setStrPATHXml(strPathXML);
                cfdi.setStrPATHBase(strPathBase);
                cfdi.setStrIDFactura(strIdFac);
                cfdi.setStrFolioFiscal(strFolioFiscal);
                //envia la factura
                cfdi.doEnvioFactura();
                String strResult = cfdi.getStrResultLast();

                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(strResult);//Pintamos el resultado
            }//Fin ID 14
            if (strid.equals("15")) {
                String strRes = "";
//                System.out.println("entro el id: " + request.getParameter("id_tkt"));
                //Recuperamos parametros                
                String strIdTKT = request.getParameter("id_tkt");
                int intIdTKT = Integer.parseInt(strIdTKT);
                TableMaster ticket = new vta_tickets();
                ticket.ObtenDatos(intIdTKT, oConn);
                //OBTENEMOS EL FOLIO DElL TICKET
                String strFolio = ticket.getFieldString("TKT_FOLIO");

//                System.out.println("FOLIO TICKET ############  " + strFolio);
                //Recuperamos paths de web.xml UBICACIÓN DE LOS TICKET
                String PATHTKT = this.getServletContext().getInitParameter("PATHTKT");
                String strPathBase = this.getServletContext().getRealPath("/");

//                System.out.println("PATH ############ " + PATHTKT);
                //OBJETO PARA CREAR LA FACTURA
                CFD_factura33 cfdi = new CFD_factura33(oConn, varSesiones, request);
                cfdi.setPATHTKT(PATHTKT);
                cfdi.setStrPATHBase(strPathBase);
                cfdi.setStrIdTicket(strIdTKT);
                cfdi.setStrFolio(strFolio);
                //envia la factura
                cfdi.doEnvioTicket();
                String strResult = cfdi.getStrResultLast();

                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(strResult);//Pintamos el resultado
            }//Fin ID 15
            if (strid.equals("16")) {
                //COMPLEMENTO DE PAGO
                String strRes = "";
//                System.out.println("entro el id para el complemento : " + request.getParameter("id_complemento"));
                //Recuperamos parametros                
                String strIdComplemento = request.getParameter("id_complemento");
                //Recuperamos paths de web.xml UBICACIÓN DE LOS TICKET
                String strPathXML = this.getServletContext().getInitParameter("PathXml");
                String strPathBase = this.getServletContext().getRealPath("/");

//                System.out.println("PATH ############ " + strPathXML);
                //OBJETO PARA CREAR LA FACTURA
                CFD_factura33 cfdi = new CFD_factura33(oConn, varSesiones, request);
                cfdi.setStrPATHXml(strPathXML);
                cfdi.setStrPATHBase(strPathBase);
                cfdi.setStrIdComplemento(strIdComplemento);
                //envia la factura
                cfdi.doEnvioComplemento();
                String strResult = cfdi.getStrResultLast();

                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(strResult);//Pintamos el resultado
            } //fin ID 16
            //envio de plantilla de reservación y/o confirmación
            if (strid.equals("17")) {

                String stridVta = request.getParameter("id_tkt");
                String strIdMaster = request.getParameter("IDMOV");
                String strNuevo = request.getParameter("venta_nueva");
                String strPagado = request.getParameter("CEV_PAGO_OK");

                String strRespuesta = new Telemarketing().sendMail(oConn, varSesiones, request, stridVta);

                System.out.println("Respuesta: " + strRespuesta);

                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(strRespuesta);//Pintamos el resultado
            }

        } //fin if strid != null
    }

    oConn.close();
%>

<%!
    /**
     * obtiene la descripcion de la plantilla
     */
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
            System.out.println("ERROR: no se encontro la plantilla " + ex);
        }
        return strPlantilla;
    }

    /**
     * omite correos que esten en la lista negra
     */
    public boolean getListaNegra(Conexion oConn, String strCorreo) {
        boolean bolExisteLN = false;
        String strSql = "select CB_CT_CORREO from cofide_lista_negra where CB_CT_CORREO = '" + strCorreo + "'";
        try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                bolExisteLN = true;
            }
            rs.close();
        } catch (SQLException sql) {
            System.out.println("error al validar la lista negra: " + sql);
        }
        return bolExisteLN;
    }

    public String getMesNombre(Conexion oConn, String strNumMes) {
        String strMesNombre = "";
        String strSql = "select ME_DESCRIPCION from vta_meses where ME_NUM_1 = " + strNumMes;
        try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                strMesNombre = rs.getString("ME_DESCRIPCION");
            }
            rs.close();
        } catch (SQLException sql) {
            System.out.println("Error al obtener el mes: " + sql);
        }
        return strMesNombre;
    }

    public Mail setPropertiesMail(Mail mail, int intUsuario, Conexion oConn) {
        String strSql = "select nombre_usuario, SMTP, PORT, SMTP_US, SMTP_PASS, SMTP_USATLS, SMTP_USASTLS from usuarios where id_usuarios = " + intUsuario;
        String strNombre = "";
        String strUsuario = "";;
        String strPassword = "";;
        String strDominio = "";;
        String strPuerto = "";
        int intSSL = 0;
        int intSSSL = 0;
        try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                strNombre = rs.getString("nombre_usuario");
                strDominio = rs.getString("SMTP");
                strPuerto = rs.getString("PORT");
                strUsuario = rs.getString("SMTP_US");
                strPassword = rs.getString("SMTP_PASS");
                intSSL = rs.getInt("SMTP_USATLS");
                intSSSL = rs.getInt("SMTP_USASTLS");
            }
            rs.close();
        } catch (SQLException sql) {
            System.out.println("Error al obtener: " + sql.getMessage());
        }

        mail.setBolDepuracion(false);
        if (intSSL == 1) {
            mail.setBolUsaTls(true);
        }
        if (intSSSL == 1) {
            mail.setBolUsaStartTls(true);
        }
        mail.setHost(strDominio);
        mail.setUsuario(strNombre);
        mail.setContrasenia(strPassword);
        mail.setPuerto(strPuerto);

        return mail;
    }//Fin setPropertiesMail

//Obtiene los CONTACTO_ID de los contactos con puntos N de lealtad
    public String getStrIdsPuntos(Conexion oConn, int intPuntosLealtad) {
        String strPuntos = "";
        String strIdsContacto = "";
        if (intPuntosLealtad == 1000) {
            strPuntos = " >= " + intPuntosLealtad;
        } else {
            strPuntos = " = " + intPuntosLealtad;
        }
        String strSql = "select CONTACTO_ID,sum(CPC_PUNTOS) as SUMA "
                + "from cofide_puntos_contacto "
                + "where CPC_ACTIVO = 1 "
                + "group by CONTACTO_ID "
                + "HAVING SUMA " + strPuntos
                + " order by SUMA DESC";

        try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                strIdsContacto += rs.getString("CONTACTO_ID") + ",";
            }
            rs.close();
        } catch (SQLException ex) {
            System.out.println("Error [getStrCtIdsPuntos_1.0] " + ex.getLocalizedMessage());
        }
        if (strIdsContacto.endsWith(",")) {
            strIdsContacto = strIdsContacto.substring(0, strIdsContacto.length() - 1);
        }
        return strIdsContacto;
    }//fin getStrCtIdsPuntos

    public boolean bolValidaDuplicado(Conexion oConn, String strCorreo, int intIdMasivo) {
        boolean bolRespuesta = false;

        String strSQL = "select CRMD_EMAIL "
                + "from crm_envio_masivo_deta "
                + "where CRM_ID = '" + intIdMasivo + "' "
                + "and CRMD_EMAIL = '" + strCorreo + "'";
        try {
            ResultSet rs = oConn.runQuery(strSQL, true);
            while (rs.next()) {
                // ya existe el correo en el listado de emails
                bolRespuesta = true;
            }
            rs.close();
        } catch (SQLException sql) {
            System.out.println(" Hubó un problema al ejecutar la consulta : [ " + sql.getMessage() + " ], [ " + sql.getSQLState() + " ]");
        }
        return bolRespuesta;
    }

%>