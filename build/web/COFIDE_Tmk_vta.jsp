<%-- 
    Document   : COFIDE_Tmk_vta
    Created on : 21/08/2017, 06:08:33 PM
    Author     : Desarrollo_COFIDE
--%>

<%@page import="Tablas.vta_mov_cte_deta"%>
<%@page import="Tablas.vta_facturadeta"%>
<%@page import="Tablas.vta_ticketsdeta"%>
<%@page import="comSIWeb.Operaciones.TableMaster"%>
<%@page import="ERP.Ticket"%>
<%@page import="com.mx.siweb.erp.especiales.cofide.COFIDE_Venta"%>
<%@page import="com.mx.siweb.erp.especiales.cofide.entidades.CofideVenta"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="java.sql.SQLException"%>
<%@page import="comSIWeb.ContextoApt.atrJSP"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.mx.siweb.erp.especiales.cofide.LlamadasPBX"%>
<%@page import="com.mx.siweb.erp.especiales.cofide.LlamadaHistorial"%>
<%@page import="com.mx.siweb.erp.especiales.cofide.Telemarketing"%>
<%@page import="comSIWeb.Utilerias.Fechas"%>
<%@page import="comSIWeb.Utilerias.UtilXml"%>
<%@page import="comSIWeb.ContextoApt.Seguridad"%>
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
    Telemarketing tele = new Telemarketing();
    COFIDE_Venta vta = new COFIDE_Venta();
    String strRes = "";
    String strSql = "";
    ResultSet rs;
    StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
    strXML.append("<vta>");

    //ventas
    String strPathXML = this.getServletContext().getInitParameter("PathXml");
    String strfolio_GLOBAL = this.getServletContext().getInitParameter("folio_GLOBAL");
    String strPathPrivateKeys = this.getServletContext().getInitParameter("PathPrivateKey");
    String strPathFonts = this.getServletContext().getRealPath("/") + System.getProperty("file.separator") + "fonts";
    //ventas

    if (varSesiones.getIntNoUser() != 0 && seg.ValidaURL(request)) {
        String strid = request.getParameter("ID");
        if (strid != null) {

            if (strid.equals("1")) {

                String strIdM = "0";

                if (request.getParameter("id_m").equals("0")) {

                    strIdM = getIdMasterVta(oConn);

                } else {

                    strIdM = request.getParameter("id_m");

                }

                String strIdCurso = request.getParameter("idcurso");
                final String strCurso = URLDecoder.decode(new String(request.getParameter("curso").getBytes("iso-8859-1")), "UTF-8");
                String strFecha = request.getParameter("fecha");
                String strPrecio = request.getParameter("precio");
                String strTipo = request.getParameter("tipocurso");

                if (!strFecha.equals("")) {

                    strFecha = fec.FormateaBD(strFecha, "/");

                }
                //se da de alta el curso como una partida
                String strCurso_ = strCurso.replace("'", "\\'");
                CofideVenta cv = new CofideVenta();
                cv.setFieldString("CV_ID_M", strIdM);
                cv.setFieldString("CV_CURSO", strCurso_);
                cv.setFieldString("CV_ID_CURSO", strIdCurso);
                cv.setFieldString("CV_PRECIO", strPrecio);
                cv.setFieldString("CV_TIPO_CURSO", strTipo);
                cv.setFieldString("CV_FECHA_INI", strFecha);
                cv.setFieldString("CV_DESC", "0.0");
                cv.setFieldString("CV_DESC_POR", "0");
                cv.setFieldString("CV_TOTAL", "0.0");
                cv.setFieldString("CV_LUGARES", "0");
                String strRespuesta = cv.Agrega(oConn); //regresa un OK, o el error de la tabla

                if (strRespuesta.equals("OK")) {

                    strIdM += ".OK";

                } else {

                    System.out.println("Error al agregar el registro a la tabla: " + oConn.getStrMsgError());

                }

                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML                
                out.println(strIdM);//Pintamos el resultado
            } //1
            if (strid.equals("2")) {
                String strIdMaster = request.getParameter("id_master");
                String strIdCurso = request.getParameter("id_curso");
                String strTipoCurso = request.getParameter("tipocurso");
                strSql = "DELETE FROM cofide_venta WHERE CV_ID_M = " + strIdMaster + " AND CV_ID_CURSO = " + strIdCurso + " AND CV_TIPO_CURSO = " + strTipoCurso;
                oConn.runQueryLMD(strSql);

                if (!oConn.isBolEsError()) {

                    strSql = "delete from cofide_participantes "
                            + "where CP_ID_CURSO = " + strIdCurso + " "
                            + "AND CP_ID_M = " + strIdMaster + " "
                            + "AND CP_TIPO_CURSO = " + strTipoCurso;
                    oConn.runQueryLMD(strSql);

                    if (!oConn.isBolEsError()) {

                        strRes = "OK";

                    } else {

                        System.out.println("Error al eliminar el registro de la tabla: " + oConn.getStrMsgError());

                    }

                } else {

                    System.out.println("Error al eliminar el registro de la tabla: " + oConn.getStrMsgError());

                }

                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML                
                out.println(strRes);//Pintamos el resultado
            } //2

            if (strid.equals("3")) {

                String stridMaster = request.getParameter("id_master");
                String strTipoCurso = "";
                String strFecha = "";

                //precios
                int intLugares = 0;
                double dblPrecio = 0;
                double dblDescuento = 0;
                double intDescuento = 0;
                double dblImporte = 0;
                double dblTotal = 0;
                double dblIvaImpuesto = 0;
                double dblSubTotal = 0;
                double dblIVA = 0;
                //precios

                strSql = "select * from cofide_venta where CV_ID_M = " + stridMaster;
                try {
                    rs = oConn.runQuery(strSql, true);
                    while (rs.next()) {

                        if (rs.getString("CV_TIPO_CURSO").equals("1")) {

                            strTipoCurso = "PRESENCIAL";

                        }

                        if (rs.getString("CV_TIPO_CURSO").equals("2")) {

                            strTipoCurso = "EN LINEA";

                        }

                        if (rs.getString("CV_TIPO_CURSO").equals("3")) {

                            strTipoCurso = "VIDEO CURSO";

                        }

                        if (rs.getString("CV_TIPO_CURSO").equals("4")) {

                            strTipoCurso = "COFIDEnet";

                        }

                        if (!rs.getString("CV_FECHA_INI").equals("")) {

                            strFecha = fec.FormateaDDMMAAAA(rs.getString("CV_FECHA_INI"), "/");

                        } else {

                            strFecha = "";

                        }

                        try {

                            intLugares = getParticipantes(oConn, rs.getString("CV_ID_M"), rs.getString("CV_ID_CURSO"), rs.getString("CV_TIPO_CURSO"));

                        } catch (Exception ex) {

                            System.out.println("Error: [ " + ex.getMessage() + " ]");

                        }
                        //operaciones
                        dblPrecio = rs.getDouble("CV_PRECIO"); //precio unitario                        
                        intDescuento = rs.getDouble("CV_DESC_POR"); //descuento / 100 = 50 , 30 , 20 , 10 ...
                        dblImporte = dblPrecio * intLugares; //importe 
                        dblDescuento = rs.getDouble("CV_DESC"); //descuento $
                        dblSubTotal = dblImporte - dblDescuento;
                        dblIVA = rs.getDouble("CV_IVA");
                        dblTotal = rs.getDouble("CV_TOTAL");
//                        dblDescuento = (dblImporte * (intDescuento / 100)); //descuento $
//                        dblSubTotal = dblImporte - dblDescuento;
//                        dblIVA = (dblSubTotal * .16);
//                        dblTotal = dblSubTotal + dblIVA;
                        //operaciones                   
                        //xml
                        strXML.append("<datos ");
                        strXML.append(" CV_ID_M = \"").append(rs.getString("CV_ID_M")).append("\"");
                        strXML.append(" CV_CURSO = \"").append(util.Sustituye(rs.getString("CV_CURSO"))).append("\"");
                        strXML.append(" CV_ID_CURSO = \"").append(rs.getString("CV_ID_CURSO")).append("\"");
                        strXML.append(" CV_PRECIO = \"").append(rs.getString("CV_PRECIO")).append("\"");
                        strXML.append(" CV_TIPO_CURSO = \"").append(strTipoCurso).append("\"");
                        strXML.append(" CV_LUGARES = \"").append(intLugares).append("\"");
                        strXML.append(" CV_FECHA_INI = \"").append(strFecha).append("\"");
                        strXML.append(" P_UNIT = \"").append(dblPrecio).append("\"");
                        strXML.append(" P_DESC = \"").append(dblDescuento).append("\"");
                        strXML.append(" P_DESC_POR = \"").append(intDescuento).append("\"");
                        strXML.append(" P_IVA = \"").append(dblIVA).append("\"");
                        strXML.append(" P_SUBTOTAL = \"").append(dblSubTotal).append("\"");
                        strXML.append(" P_TOTAL = \"").append(dblTotal).append("\"");
                        strXML.append(" />");
                        //xml

                    }
                    rs.close();
                } catch (SQLException sql) {
                    System.out.println("Error al obtener los cursos [ " + sql.getMessage() + " ]");
                }

                strXML.append("</vta>");
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML.toString());//Pintamos el resultado

            } //3

            if (strid.equals("4")) {

                String strNombre = request.getParameter("nombre");
                String strAppat = request.getParameter("appat");
                String strApmat = request.getParameter("apmat");
                String strTitulo = request.getParameter("titulo");
                String strNumSoc = request.getParameter("numerosoc");
                String strCorreo = request.getParameter("correo");
                String strCtId = request.getParameter("ct_id");
                String strMaterial = request.getParameter("material");
                String strAsoc = request.getParameter("asociacion");

                String strContactoID = "";

                strSql = "select max(CONTACTO_ID) + 1  as maximo from cofide_contactos;";

                try {

                    rs = oConn.runQuery(strSql, true);

                    while (rs.next()) {

                        strContactoID = rs.getString("maximo");

                        strSql = "INSERT INTO cofide_contactos (CCO_NOMBRE, CCO_APPATERNO, CCO_APMATERNO, CCO_TITULO, "
                                + "CCO_NOSOCIO, CCO_CORREO, CT_ID, CONTACTO_ID) "
                                + "VALUES "
                                + "('" + strNombre + "', '" + strAppat + "', '" + strApmat + "', '" + strTitulo + "', "
                                + "'" + strNumSoc + "', '" + strCorreo + "', '" + strCtId + "', '" + strContactoID + "')";

                        oConn.runQueryLMD(strSql);

                        if (!oConn.isBolEsError()) {

                            strRes = "OK," + strContactoID;

                            strSql = "INSERT INTO cofide_puntos_contacto (CPC_PUNTOS, CPC_ACTIVO, CONTACTO_ID) VALUES ('200', '1', '" + strContactoID + "')";
                            oConn.runQueryLMD(strSql);
                        }

                    }

                    rs.close();

                } catch (SQLException sql) {

                    System.out.println("Error al obtener el ultimo ID de contacto [ " + sql.getMessage() + " ]");

                }

                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(strRes);//Pintamos el resultado

            } //4

            if (strid.equals("5")) {

                String strIdCurso = request.getParameter("idcurso");
                String stridMaster = request.getParameter("idmaster");
                int intLenght = Integer.parseInt(request.getParameter("length"));
                String strTipoCurso = request.getParameter("tipocurso");
                String strPrecio = request.getParameter("precio");
                String strDescuento = request.getParameter("descuento");
                String strDescuentoPor = request.getParameter("descuentopor");
                String strImporte = request.getParameter("importe");
                String strTotal = request.getParameter("total");
                String strIVA = request.getParameter("iva");
                System.out.println("1st iva: " + strIVA);

                String strTipoDoc = request.getParameter("tipodoc"); //edición de ventsa
                String strIdVta = request.getParameter("idvta");
                String strFAC_ID = "0";
                String strTKT_ID = "0";

                if (!strTipoDoc.equals("")) {
                    if (strTipoDoc.equals("T")) { //ticket
                        strTKT_ID = strIdVta;
                    }
                    if (strTipoDoc.equals("F")) { //factura
                        strFAC_ID = "" + strIdVta;
                    }
                }

                String strTitulo = "";
                String strNombre = "";
                String strAppat = "";
                String strApmat = "";
                String strNumSoc = "";
                String strAsoc = "";
                String strCorreo = "";
                String strMaterial = "";
                String strCT_ID = "";
                String strContactoId = "";

                strSql = "delete from cofide_participantes where CP_ID_CURSO = " + strIdCurso + " and CP_ID_M = " + stridMaster + " and CP_TIPO_CURSO = " + strTipoCurso;
                oConn.runQueryLMD(strSql);
                if (!oConn.isBolEsError()) {

                    for (int i = 0; i < intLenght; i++) {

                        strTitulo = request.getParameter("titulo" + i);
                        strNombre = request.getParameter("nombre" + i);
                        strAppat = request.getParameter("appat" + i);
                        strApmat = request.getParameter("apmat" + i);
                        strNumSoc = request.getParameter("nosocio" + i);
                        strAsoc = request.getParameter("asociacion" + i);
                        strCorreo = request.getParameter("correo" + i);

                        if (request.getParameter("material" + i).equals("NO")) {

                            strMaterial = "0";

                        } else {

                            strMaterial = "1";

                        }

                        strCT_ID = request.getParameter("ct_id" + i);
                        strContactoId = request.getParameter("idcontacto" + i);

                        strSql = "INSERT INTO cofide_participantes (CP_ID_CURSO, CP_NOMBRE, CP_APPAT, CP_APMAT, CP_TITULO, "
                                + "CCO_NOSOCIO, CP_ASCOC, CP_CORREO, CP_USUARIO_ALTA, CT_ID, "
                                + "CP_MATERIAL_IMPRESO, CP_TIPO_CURSO,CP_ESTATUS, CONTACTO_ID, CP_ID_M,CP_COMENT,CP_FAC_ID, CP_TKT_ID) VALUES "
                                + "('" + strIdCurso + "', '" + strNombre + "', '" + strAppat + "', '" + strApmat + "', '" + strTitulo + "', "
                                + "'" + strNumSoc + "', '" + strAsoc + "', '" + strCorreo + "', '" + varSesiones.getIntNoUser() + "', '" + strCT_ID + "', "
                                + "'" + strMaterial + "', '" + strTipoCurso + "', '1', '" + strContactoId + "','" + stridMaster + "',''," + strFAC_ID + "," + strTKT_ID + ");";

                        oConn.runQueryLMD(strSql);
                        if (!oConn.isBolEsError()) {

                            strRes = "OK";
                            //actualiza la información del curso

                        } else {

                            strRes = "Hubó un problema al guardar participantes en el curso : ID " + strIdCurso + " "
                                    + "con el participante: " + strNombre + " " + strAppat + " " + strApmat + " "
                                    + "ERROR[ " + oConn.getStrMsgError() + " ]";
                            break;

                        }

                    }
                    if (strRes.equals("OK")) {

                        strRes = updateCurso(oConn, request);

                    }

                } else {

                    strRes = "Hubó un problema al intentar guardar participantes en el curso : ID " + strIdCurso + " "
                            + "ERROR[ " + oConn.getStrMsgError() + " ]";

                }

                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(strRes);//Pintamos el resultado

            } //5
            if (strid.equals("6")) {

                String strIdmaster = request.getParameter("idmaster");
                String strIdcurso = request.getParameter("idcurso");
                String strTipoCurso = request.getParameter("tipocurso");

                strSql = "select * from cofide_participantes where CP_ID_M = " + strIdmaster + " and CP_ID_CURSO = " + strIdcurso + " and CP_TIPO_CURSO = " + strTipoCurso;

                try {

                    rs = oConn.runQuery(strSql, true);
                    while (rs.next()) {
                        String strMaterial = "NO";

                        if (rs.getString("CP_MATERIAL_IMPRESO").equals("1")) {
                            strMaterial = "SI";
                        }
                        //xml
                        strXML.append("<datos ");
                        strXML.append(" CCO_NOMBRE = \"").append(rs.getString("CP_NOMBRE")).append("\"");
                        strXML.append(" CCO_APPATERNO = \"").append(rs.getString("CP_APPAT")).append("\"");
                        strXML.append(" CCO_APMATERNO = \"").append(rs.getString("CP_APMAT")).append("\"");
                        strXML.append(" CCO_TITULO = \"").append(rs.getString("CP_TITULO")).append("\"");
                        strXML.append(" CCO_NOSOCIO = \"").append(rs.getString("CCO_NOSOCIO")).append("\"");
                        strXML.append(" CCO_ASOCIACION = \"").append(rs.getString("CP_ASCOC")).append("\"");
                        strXML.append(" CCO_CORREO = \"").append(rs.getString("CP_CORREO")).append("\"");
                        strXML.append(" CT_ID = \"").append(rs.getString("CT_ID")).append("\"");
                        strXML.append(" MATERIAL = \"").append(strMaterial).append("\"");
                        strXML.append(" CP_OBSERVACIONES = \"").append(rs.getString("CP_OBSERVACIONES")).append("\"");
                        strXML.append(" CONTACTO_ID = \"").append(rs.getString("CONTACTO_ID")).append("\"");
                        strXML.append(" />");
                        //xml

                    }
                    rs.close();

                } catch (SQLException sql) {

                    System.out.println("Error al obtener los participantes registrados en el curso: [ " + sql.getMessage() + " ]");

                }

                strXML.append("</vta>");
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML.toString());//Pintamos el resultado

            } //6
            if (strid.equals("7")) {

                //limites del curso
                String stridCurso = request.getParameter("idcurso");
                int intInscritos = getLugarVendido(stridCurso, oConn);
                int intMontaje = 0;
                int intDisponibilidad = 0;
                strSql = "select CC_MONTAJE from cofide_cursos where CC_CURSO_ID = " + stridCurso;

                try {

                    rs = oConn.runQuery(strSql, true);
                    while (rs.next()) {

                        intMontaje = rs.getInt("CC_MONTAJE");

                    }
                    rs.close();
                    System.out.println("montaje: " + intMontaje);
                    System.out.println("inscritos " + intInscritos);
                    intDisponibilidad = intMontaje - intInscritos;
                    System.out.println("disponibles " + intDisponibilidad);

                    if (intDisponibilidad > 0) {

                        strRes = "OK" + intDisponibilidad;

                    }

                } catch (SQLException sql) {

                    System.out.println("Error: ocurrio un problema al obtener los lugares disponibles del curso: [ " + sql.getMessage() + " ]");

                }

                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(strRes);//Pintamos el resultado

            } //7
            if (strid.equals("8")) {

                String strIdMaster = request.getParameter("idMaster");
                String strTipoDoc = request.getParameter("tipodoc");

                int intCursos = 0;
                int intParticipantes = 0;
                double dblSubtotal = 0.00;
                double dblIVA = 0.00;
                double dblTotal = 0.00;
                double dblDescuento = 0.00;
                double dblPrecioBase = 0.00;
                String strFormaPago = "0";
                String strMetodoPago = "0";
                String strComprobante = "";
                String strparticipantes = "";
                String strCursos = "";
                String strFechaCobro = "";
                String strReferencia = "";

                strSql = "select FAC_FORMADEPAGO,  FAC_METODODEPAGO, FAC_NOMPAGO, FAC_FECHA_COBRO, FAC_REFERENCIA,"
                        + "(if(TIPO_DOC = 'T', (select sum(TKTD_CANTIDAD) from vta_ticketsdeta where tkt_id = FAC_ID), (select sum(FACD_CANTIDAD) from vta_facturasdeta where vta_facturasdeta.FAC_ID = view_ventasglobales.FAC_ID))) as participantes,"
                        + "(if(TIPO_DOC = 'T', (select count(TKTD_CANTIDAD) from vta_ticketsdeta where tkt_id = FAC_ID), (select count(FACD_CANTIDAD) from vta_facturasdeta where vta_facturasdeta.FAC_ID = view_ventasglobales.FAC_ID)))  as cursos "
                        //                        + "from view_ventasglobales where ID_MASTER = " + strIdMaster + " and TIPO_DOC = " + (strTipoDoc.equals("3") ? "'F'" : "'T'");
                        + "from view_ventasglobales where ID_MASTER = " + strIdMaster + " and FAC_ANULADA = 0";
                rs = oConn.runQuery(strSql, true);
                while (rs.next()) {

                    if (!rs.getString("FAC_FORMADEPAGO").equals("")) {
                        strFormaPago = rs.getString("FAC_FORMADEPAGO");
                    }

                    if (!rs.getString("FAC_METODODEPAGO").equals("")) {
                        strMetodoPago = rs.getString("FAC_METODODEPAGO");
                    }

                    strComprobante = rs.getString("FAC_NOMPAGO");
                    strparticipantes = rs.getString("participantes");
                    strCursos = rs.getString("cursos");
                    strFechaCobro = rs.getString("FAC_FECHA_COBRO");
                    strReferencia = rs.getString("FAC_REFERENCIA");

                }
                rs.close();

                quitIVA(oConn, strIdMaster, strTipoDoc);

                strSql = "select * from cofide_venta where CV_ID_M = " + strIdMaster;
                rs = oConn.runQuery(strSql, true);
                while (rs.next()) {

                    intCursos++;
                    dblPrecioBase += (rs.getDouble("CV_PRECIO") * rs.getDouble("CV_LUGARES"));
                    dblSubtotal += ((rs.getDouble("CV_PRECIO") * rs.getDouble("CV_LUGARES")) - rs.getDouble("CV_DESC"));
                    dblTotal += rs.getDouble("CV_TOTAL");
                    dblIVA += rs.getDouble("CV_IVA");
                    dblDescuento += rs.getDouble("CV_DESC");

                }
                rs.close();

                strSql = "select * from cofide_participantes where CP_ID_M = " + strIdMaster;

                rs = oConn.runQuery(strSql, true);
                while (rs.next()) {

                    intParticipantes++;

                }
                rs.close();
                if (!strFechaCobro.equals("")) {
                    strFechaCobro = fec.FormateaDDMMAAAA(strFechaCobro, "/");
                }
                strXML.append("<datos ");
                strXML.append(" num_cursos = \"").append(intCursos).append("\"");
                strXML.append(" num_participantes = \"").append(intParticipantes).append("\"");
                strXML.append(" precio_base = \"").append(dblPrecioBase).append("\"");
                strXML.append(" subtotal = \"").append(dblSubtotal).append("\"");
                strXML.append(" iva = \"").append(dblIVA).append("\"");
                strXML.append(" total = \"").append(dblTotal).append("\"");
                strXML.append(" descuento = \"").append(dblDescuento).append("\"");
                strXML.append(" formapago = \"").append(strFormaPago).append("\"");
                strXML.append(" metodopago = \"").append(strMetodoPago).append("\"");
                strXML.append(" comprobante = \"").append(strComprobante).append("\"");
                strXML.append(" participantesvendidos = \"").append(strparticipantes).append("\"");
                strXML.append(" cursosvendidos = \"").append(strCursos).append("\"");
                strXML.append(" fechacobro = \"").append(strFechaCobro).append("\"");
                strXML.append(" referencia = \"").append(strReferencia).append("\"");
                strXML.append(" />");
                //xml
                strXML.append("</vta>");
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML.toString());//Pintamos el resultado

            } //8

            if (strid.equals("9")) { //reservación
                boolean bolReservacion = false;
                //guardamos la venta
                System.out.println("\n\n INICIA LA VENTA RESERVACION\n\n");

                if (request.getParameter("venta_nueva") != null) {
                    System.out.println("es nueva la venta? " + (request.getParameter("venta_nueva").equals("1") ? "NUEVA" : "EDICIÓN"));
                }
                //Inicializamos datos
                //Recuperamos paths de web.xml
                String strmod_Inventarios = this.getServletContext().getInitParameter("mod_Inventarios");
                String strSist_Costos = this.getServletContext().getInitParameter("SistemaCostos");
                String strClienteUniversal = this.getServletContext().getInitParameter("ClienteUniversal");

                if (strfolio_GLOBAL == null) {
                    strfolio_GLOBAL = "SI";
                }
                if (strmod_Inventarios == null) {
                    strmod_Inventarios = "NO";
                }
                if (strSist_Costos == null) {
                    strSist_Costos = "0";
                }
                if (strClienteUniversal == null) {
                    strClienteUniversal = "0";
                }

                String strIdOldFac = "0";
                String strIdFacVta = "0";

                String strTipoVta = request.getParameter("TIPOVENTA");

                String strCorreo = request.getParameter("CT_CORREO");
                String strCorreo2 = request.getParameter("CT_CORREO2");

                //SE VA A MANDAR A FACTURAR POR UNA REFACTURACIÓN
                System.out.println("\n\neste es el id de la venta anterior: " + request.getParameter("ID_FAC_OLD"));
                if (request.getParameter("ID_FAC_OLD") != null) {

                    if (!request.getParameter("ID_FAC_OLD").equals("")) {

                        if (!request.getParameter("ID_FAC_OLD").equals("undefined")) {

                            //IMPORTANTE, PARA RECIBIR EL ID DEL CLIENTE, SIN IMPORTAR DESDE DONDE SE GENERE LA VENTA
                            strIdFacVta = request.getParameter("ID_FAC_OLD");
                            if (request.getParameter("refactura").equals("1")) {
                                strIdOldFac = "1";
                                //RESERVACIÓN
                                strTipoVta = "1";
                            }
                            System.out.println("********** ID DE LA VENTA CON LA QUE SE OBTENDRA EL ID DEL CLIENTE: " + strIdFacVta + " **********");
                        }
                    }
                }

                String strIdMovimiento = request.getParameter("IDMOV");
                String strPromocion = request.getParameter("promo"); //si es 1 = es promocion                
                System.out.println("\n\n\n&&&& promoción: " + strPromocion);

//                int intCevPublicidad = Integer.valueOf(request.getParameter("CEV_MPUBLICIDAD"));
                int intCevPublicidad = 0;
                int intCT_ID = 0;
                if (strIdOldFac.equals("1")) {
                    intCT_ID = tele.getCT_ID(oConn, strIdFacVta);

                } else {
                    intCT_ID = Integer.valueOf(request.getParameter("CT_ID"));
                }
                //Instanciamos el objeto que generara la venta
                Ticket ticket = new Ticket(oConn, varSesiones, request);
                ticket.setStrPATHKeys(strPathPrivateKeys);
                ticket.setStrPATHXml(strPathXML);
                ticket.setStrPATHFonts(strPathFonts);
                ticket.setBolAfectaInv(false);

                //Recibimos parametros
                String strPrefijoMaster = "TKT";
                String strPrefijoDeta = "TKTD";
                String strTipoVtaNom = Ticket.TICKET;
                String strTipoDoc = "TICKET";
                //DATOS DE FACTURACIÓN
                int intDfaId = Integer.parseInt(request.getParameter("DFA_ID"));
                System.out.println("\n\n############################## DFA_ID: id donde vienen lso datos fiscales de lo que se va a facturar: " + intDfaId);
                System.out.println("strTipoVta " + strTipoVta);
                //Recuperamos el tipo de venta 3:FACTURA 2:TICKET 1:RESERVACIÓN
                if (strTipoVta.equals("3")) {

                    strPrefijoMaster = "FAC";
                    strPrefijoDeta = "FACD";
                    strTipoVtaNom = Ticket.FACTURA;
                    ticket.initMyPass(this.getServletContext());
                    if (intDfaId == 0) {
                        intDfaId = tele.saveDatosFactura(oConn, varSesiones, request, intCT_ID);
                    }
                    strTipoDoc = "FACTURA";
                    strIdOldFac = "0";
                }

                ticket.setStrTipoVta(strTipoVtaNom);
                //Validamos si tenemos un empresa seleccionada
                if (varSesiones.getIntIdEmpresa() != 0) {
                    //Asignamos la empresa seleccionada
                    ticket.setIntEMP_ID(varSesiones.getIntIdEmpresa());
                }
                //Validamos si usaremos un folio global
                if (strfolio_GLOBAL.equals("NO")) {

                    ticket.setBolFolioGlobal(false);
                }

                //ID DE LA RAZÓN SOCIAL
                ticket.getDocument().setFieldInt("DFA_ID", intDfaId);
                //ID DEL MOVIMIENTO
                ticket.getDocument().setFieldString(strPrefijoMaster + "_ID_MOV", strIdMovimiento);
                if (request.getParameter("esReservacion") != null) {
                    ticket.getDocument().setFieldString(strPrefijoMaster + "_ES_RESERVACION", request.getParameter("esReservacion"));
                    bolReservacion = true;
                }
                if (request.getParameter(strPrefijoMaster + "_MONEDA") != null) {
                    ticket.getDocument().setFieldInt(strPrefijoMaster + "_MONEDA", Integer.valueOf(request.getParameter(strPrefijoMaster + "_MONEDA")));
                } else {
                    ticket.getDocument().setFieldInt(strPrefijoMaster + "_MONEDA", 1);
                }

                final String strNotas = URLDecoder.decode(new String(request.getParameter(strPrefijoMaster + "_NOTAS").getBytes("iso-8859-1")), "UTF-8");

                ticket.getDocument().setFieldString(strPrefijoMaster + "_NOTAS", strNotas);

                final String strComentario = URLDecoder.decode(new String(request.getParameter("CT_COMENTARIO").getBytes("iso-8859-1")), "UTF-8");

                ticket.getDocument().setFieldString(strPrefijoMaster + "_NOTASPIE", strComentario); //guarda el comentario del agente sobre la venta
                ticket.getDocument().setFieldInt(strPrefijoMaster + "_ESSERV", Integer.valueOf(request.getParameter(strPrefijoMaster + "_ESSERV")));
                System.out.println("####### MAESTRO #######");
                ticket.getDocument().setFieldDouble(strPrefijoMaster + "_IMPORTE", Double.valueOf(request.getParameter(strPrefijoMaster + "_IMPORTE")));
                ticket.getDocument().setFieldDouble(strPrefijoMaster + "_DESCUENTO", Double.valueOf(request.getParameter(strPrefijoMaster + "_DESCUENTO")));
                ticket.getDocument().setFieldDouble(strPrefijoMaster + "_IMPUESTO1", Double.valueOf(request.getParameter(strPrefijoMaster + "_IMPUESTO1")));
                ticket.getDocument().setFieldDouble(strPrefijoMaster + "_TOTAL", Double.valueOf(request.getParameter(strPrefijoMaster + "_TOTAL")));
                System.out.println("IMPORTE:" + request.getParameter(strPrefijoMaster + "_IMPORTE"));
                System.out.println("DESCUENTO:" + request.getParameter(strPrefijoMaster + "_DESCUENTO"));
                System.out.println("IMPUESTO:" + request.getParameter(strPrefijoMaster + "_IMPUESTO1"));
                System.out.println("TOTAL:" + request.getParameter(strPrefijoMaster + "_TOTAL"));
                System.out.println("####### MAESTRO #######");
                ticket.getDocument().setFieldDouble(strPrefijoMaster + "_IMPUESTO2", Double.valueOf(request.getParameter(strPrefijoMaster + "_IMPUESTO2")));
                ticket.getDocument().setFieldDouble(strPrefijoMaster + "_IMPUESTO3", Double.valueOf(request.getParameter(strPrefijoMaster + "_IMPUESTO3")));
                ticket.getDocument().setFieldDouble(strPrefijoMaster + "_RETISR", Double.valueOf(request.getParameter(strPrefijoMaster + "_RETISR")));
                ticket.getDocument().setFieldDouble(strPrefijoMaster + "_RETIVA", Double.valueOf(request.getParameter(strPrefijoMaster + "_RETIVA")));
                ticket.getDocument().setFieldDouble(strPrefijoMaster + "_NETO", Double.valueOf(request.getParameter(strPrefijoMaster + "_NETO")));
                ticket.getDocument().setFieldString(strPrefijoMaster + "_REFERENCIA", request.getParameter(strPrefijoMaster + "_REFERENCIA"));
                ticket.getDocument().setFieldString(strPrefijoMaster + "_CONDPAGO", request.getParameter(strPrefijoMaster + "_CONDPAGO"));
                ticket.getDocument().setFieldString(strPrefijoMaster + "_FORMADEPAGO", request.getParameter(strPrefijoMaster + "_FORMADEPAGO"));
                if (request.getParameter(strPrefijoMaster + "_METODOPAGO") != null) {
                    ticket.getDocument().setFieldString(strPrefijoMaster + "_METODODEPAGO", request.getParameter(strPrefijoMaster + "_METODOPAGO"));
                }

                System.out.println("request.getParameter(strPrefijoMaster + _NUMCUENTA) : " + request.getParameter(strPrefijoMaster + "_NUMCUENTA"));
//                ticket.getDocument().setFieldString(strPrefijoMaster + "_NUMCUENTA", request.getParameter(strPrefijoMaster + "_NUMCUENTA"));
                ticket.getDocument().setFieldString(strPrefijoMaster + "_NUMCUENTA", "0000");
                System.out.println("### uso de CFDI");
                // uso cfdi	
                ticket.getDocument().setFieldString(strPrefijoMaster + "_USO_CFDI", request.getParameter("CEV_USO_CFDI"));
                ticket.getDocument().setFieldDouble(strPrefijoMaster + "_TASA1", Double.valueOf(request.getParameter(strPrefijoMaster + "_TASA1")));
                ticket.getDocument().setFieldDouble(strPrefijoMaster + "_TASA2", Double.valueOf(request.getParameter(strPrefijoMaster + "_TASA2")));
                ticket.getDocument().setFieldDouble(strPrefijoMaster + "_TASA3", Double.valueOf(request.getParameter(strPrefijoMaster + "_TASA3")));
                //Tarifas de IVA
                int intTI_ID = 0;
                int intTI_ID2 = 0;
                int intTI_ID3 = 0;
                try {
                    intTI_ID = Integer.valueOf(request.getParameter("TI_ID"));
                    intTI_ID2 = Integer.valueOf(request.getParameter("TI_ID2"));
                    intTI_ID3 = Integer.valueOf(request.getParameter("TI_ID3"));
                } catch (NumberFormatException ex) {
                    System.out.println("ERP_Ventas TI_ID " + ex.getMessage());
                }
                //Asignamos los valores al objeto                
                ticket.getDocument().setFieldInt("TI_ID", intTI_ID);
                ticket.getDocument().setFieldInt("TI_ID2", intTI_ID2);
                ticket.getDocument().setFieldInt("TI_ID3", intTI_ID3);
                String strComprobantedepago = "";
                int intPagado = 0;
                String strFecha = "";
                if (request.getParameter("CEV_NOM_FILE") != null) {
                    if (request.getParameter("CEV_NOM_FILE") != "") {
                        strComprobantedepago = request.getParameter("CEV_NOM_FILE");
                        ticket.getDocument().setFieldInt(strPrefijoMaster + "_COFIDE_PAGADO", 1);
                        intPagado = 1;
                        strFecha = fec.getFechaActual();
                    }
                    ticket.getDocument().setFieldString(strPrefijoMaster + "_NOMPAGO", request.getParameter("CEV_NOM_FILE"));
                    if (request.getParameter("FAC_COFIDE_VALIDA") != null) {
                        ticket.getDocument().setFieldString(strPrefijoMaster + "_COFIDE_VALIDA", request.getParameter("FAC_COFIDE_VALIDA"));
                    }
                }
                String strFechaCobro = "";
                if (request.getParameter("CEV_FECHAPAGO") != null && !request.getParameter("CEV_FECHAPAGO").equals("")) {
                    if (request.getParameter("CEV_FECHAPAGO").contains("/")) {
                        strFechaCobro = fec.FormateaBD(request.getParameter("CEV_FECHAPAGO"), "/");
                    } else {
                        strFechaCobro = request.getParameter("CEV_FECHAPAGO");
                    }
                    ticket.getDocument().setFieldString(strPrefijoMaster + "_FECHA_COBRO", strFechaCobro);
                }

                final String strRazonsocial = URLDecoder.decode(new String(request.getParameter("CT_RAZONSOCIAL").getBytes("iso-8859-1")), "UTF-8");
                final String strRfc = URLDecoder.decode(new String(request.getParameter("CT_RFC").getBytes("iso-8859-1")), "UTF-8");

                //Recibimos la serie por guardar...
                if (request.getParameter(strPrefijoMaster + "_SERIE") != null) {
                    ticket.getDocument().setFieldString(strPrefijoMaster + "_SERIE", request.getParameter(strPrefijoMaster + "_SERIE"));
                }
                ticket.getDocument().setFieldString("COFIDE_NVO", request.getParameter("vta_nvo")); //para ver si es nuevo cliente o no y ñigarlo a la venta
                ticket.getDocument().setFieldString(strPrefijoMaster + "_PROMO", strPromocion);
                ticket.getDocument().setFieldString(strPrefijoMaster + "_RAZONSOCIAL", strRazonsocial);
                ticket.getDocument().setFieldString(strPrefijoMaster + "_RFC", strRfc);
                ticket.getDocument().setFieldString(strPrefijoMaster + "_CALLE", request.getParameter("CT_CALLE"));
                ticket.getDocument().setFieldString(strPrefijoMaster + "_COLONIA", request.getParameter("CT_COL"));
                ticket.getDocument().setFieldString(strPrefijoMaster + "_MUNICIPIO", request.getParameter("CT_MUNICIPIO"));
                ticket.getDocument().setFieldString(strPrefijoMaster + "_ESTADO", request.getParameter("CT_ESTADO"));
                ticket.getDocument().setFieldString(strPrefijoMaster + "_CP", request.getParameter("CT_CP"));
                ticket.getDocument().setFieldString(strPrefijoMaster + "_NUMERO", request.getParameter("CT_NUM"));
                ticket.getDocument().setFieldString(strPrefijoMaster + "_NUMINT", request.getParameter("CT_NUMINT"));

                //Edicion de pedidos
                ticket.getDocument().setFieldInt("SC_ID", Integer.valueOf(request.getParameter("SC_ID")));
                ticket.getDocument().setFieldInt("CT_ID", intCT_ID);
                if (vta.isUserInBound(oConn, varSesiones.getIntNoUser())) {
                    ticket.getDocument().setFieldInt(strPrefijoMaster + "_INBOUND", 1);
                    System.out.println("\n\n ES INBOUND ");
                } else {
                    ticket.getDocument().setFieldInt(strPrefijoMaster + "_INBOUND", 0);
                    System.out.println("\n\n ES INBOUND ");
                }
                ticket.getDocument().setFieldString(strPrefijoMaster + "_FECHA", fec.getFechaActual());
                // SE VA A MANDAR PETICIÓN DE FACTURA
                if (!strTipoVta.equals("3")) {
                    ticket.getDocument().setFieldString(strPrefijoMaster + "_FACTURAR", strIdOldFac);
                }
                ticket.getDocument().setFieldDouble(strPrefijoMaster + "_TASAPESO", 1);
                ticket.getDocument().setFieldString(strPrefijoMaster + "_SERIE", "C");
                System.out.println("####### regimen fiscal #######");
                ticket.getDocument().setFieldString(strPrefijoMaster + "_REGIMENFISCAL", request.getParameter(strPrefijoMaster + "_REGIMENFISCAL"));
                System.out.println("\n####### \nID DE LA RESERVACIÓN ORIGEN: " + strIdFacVta);
                ticket.getDocument().setFieldString("TKT_ID_OLD", strIdFacVta);

                //DETALLE
                //RECORRER DETALLES DE LA VENTA , MEDIANTE LA CONSULTA DEL MOVIMIENTO
                strSql = "select * from cofide_venta where CV_ID_M = " + strIdMovimiento;

                rs = oConn.runQuery(strSql, true);
                while (rs.next()) {

                    TableMaster deta = null;
                    if (strTipoVtaNom.equals(Ticket.TICKET)) {
                        deta = new vta_ticketsdeta();
                    }
                    if (strTipoVtaNom.equals(Ticket.FACTURA)) {
                        deta = new vta_facturadeta();
                    }

                    int intMod = vta.getModalidadCurso(oConn, rs.getString("CV_ID_CURSO")); //id del curso
                    deta.setFieldInt("SC_ID", Integer.valueOf(request.getParameter("SC_ID")));
                    deta.setFieldInt("PR_ID", rs.getInt("CV_ID_CURSO")); //id del producto
                    deta.setFieldInt(strPrefijoDeta + "_ID_CURSO", rs.getInt("CV_ID_CURSO")); //id del producto
                    deta.setFieldInt(strPrefijoDeta + "_TIPO_CURSO", rs.getInt("CV_TIPO_CURSO")); //tipo producto, 1:presencial 2:online, 3:videocurso, 4:cofidenet
                    deta.setFieldString(strPrefijoDeta + "_DESCRIPCION", rs.getString("CV_CURSO").replace("'", "\\'")); //descipricon del curso   
                    if (request.getParameter(strPrefijoDeta + "_CVE") != null) {
                        deta.setFieldString(strPrefijoDeta + "_CVE", request.getParameter(strPrefijoDeta + "_CVE"));
                    } else {
                        deta.setFieldString(strPrefijoDeta + "_CVE", "....");
                    }
                    //UNIDAD DE MEDIDA                    
                    System.out.println("########### nuevos campos de cfdi 3.3");
                    deta.setFieldString(strPrefijoDeta + "_UNIDAD_MEDIDA", request.getParameter(strPrefijoDeta + "_UNIDAD_MEDIDA"));
//                    if (varSesiones.getIntNoUser() == 144) { //cambio de clave de producto
//                        deta.setFieldString(strPrefijoDeta + "_CVE_PRODSERV", "80111500");
//                    } else {
                    deta.setFieldString(strPrefijoDeta + "_CVE_PRODSERV", request.getParameter(strPrefijoDeta + "_CVE_PRODSERV"));
//                    }
                    deta.setFieldDouble(strPrefijoDeta + "_TASAIVA1", Double.valueOf(request.getParameter(strPrefijoDeta + "_TASAIVA1"))); //tasa iva
                    deta.setFieldString(strPrefijoDeta + "_COMENTARIO", strNotas);

                    double dblPrecio = rs.getDouble("CV_PRECIO");
                    int intCantidad = rs.getInt("CV_LUGARES");
                    double dblDescuento = rs.getDouble("CV_DESC");
                    double dblIVA = rs.getDouble("CV_IVA");
                    double dblTotalDetalle = rs.getDouble("CV_TOTAL"); //temportal

                    double dblImporte = ((dblPrecio * intCantidad) - dblDescuento) + dblIVA;

                    System.out.println("########## DETALLE ############");
                    System.out.println("PRECIO: " + dblPrecio);
                    System.out.println("CANTIDAD: " + intCantidad);
                    System.out.println("DESCUENTO: " + dblDescuento);
                    System.out.println("IVA: " + dblIVA);
                    System.out.println("IMPORTE DIRECTO: " + dblTotalDetalle);
                    System.out.println("IMPORTE CALCULADO: " + dblImporte);
                    System.out.println("########## DETALLE ############");

                    deta.setFieldDouble(strPrefijoDeta + "_IMPORTE", dblImporte); //importe = ((PU * Cantidad) - Desc + Iva)
                    deta.setFieldInt(strPrefijoDeta + "_CANTIDAD", intCantidad); //cantidad                    
                    deta.setFieldDouble(strPrefijoDeta + "_IMPORTEREAL", dblImporte); //importe real
                    deta.setFieldDouble(strPrefijoDeta + "_IMPUESTO1", dblIVA); //importe real
                    deta.setFieldDouble(strPrefijoDeta + "_PRECIO", dblPrecio); //precio unitario
                    deta.setFieldDouble(strPrefijoDeta + "_DESCUENTO", dblDescuento);
                    deta.setFieldDouble(strPrefijoDeta + "_PORDESC", rs.getDouble("CV_DESC_POR"));
                    deta.setFieldDouble(strPrefijoDeta + "_PRECREAL", dblPrecio);
                    //GUARDAMOS LOS DETALLES
                    ticket.AddDetalle(deta);
                    System.out.println(" ############################################# descripcion " + deta.getFieldString(strPrefijoDeta + "_DESCRIPCION"));
                }
                rs.close();
                //Inicializamos objeto
                ticket.setBolSendMailMasivo(false);

                //Validamos si es un pedido que se esta editando para solo modificar el pedido anterior 
                ticket.Init();
                //Generamos transaccion
                ticket.doTrx();
                if (ticket.getStrResultLast().equals("OK")) {

                    strRes = "OK." + ticket.getDocument().getValorKey();
                    int intIdVta = Integer.valueOf(ticket.getDocument().getValorKey());
                    //Posicion inicial para el numero de pagina
                    String strPosX = "";
                    String strTitle = "";
                    strPosX = this.getServletContext().getInitParameter("PosXTitle");
                    strTitle = this.getServletContext().getInitParameter("TitleApp");

                    //ES FACTURA ENVIO
                    if (strTipoVta.equals("3")) {
                        System.out.println("*** ENVIA LA FACTURA POR CORREO ***");
//                        tele.enviaMailMasivo(oConn, strPosX, strTitle, ticket.getDocument().getValorKey(), varSesiones, strTipoVta,
//                                strCorreo, strCorreo2);
                    }
                    //ES TICKET ENVIO
                    if (strTipoVta.equals("2")) {
                        System.out.println("*** ENVIA EL TICKET POR CORREO ***");
//                        strCorreo = tele.strGetCorreoTicket(oConn, varSesiones.getIntNoUser());
//                        ticket.generaMail(oConn, Integer.valueOf(ticket.getDocument().getValorKey()), strTipoDoc, strPathXML, strPathFonts, strCorreo);
                    }
                    int intIdvtaParticipanteTKT = 0;
                    int intIdvtaParticipanteFAC = 0;

                    if (strTipoVta.equals("3")) {
                        intIdvtaParticipanteFAC = intIdVta;
                    } else {
                        intIdvtaParticipanteTKT = intIdVta;
                    }

                    //Guardamos los participantes del curso
                    System.out.println("############################## actualiza la información de los participantes");
                    strSql = "UPDATE cofide_participantes SET "
                            + "CP_TKT_ID='" + intIdvtaParticipanteTKT + "', "
                            + "CP_FAC_ID='" + intIdvtaParticipanteFAC + "', "
                            + "CP_PAGO='" + intPagado + "', "
                            + "CP_FECHA_PAGO='" + strFecha + "', "
                            + "CP_NOM_COMPROBANTE='" + strComprobantedepago + "' "
                            + "WHERE CP_ID_M = " + strIdMovimiento;
                    oConn.runQueryLMD(strSql);
                    String strResultado = "";

                    //SI PROVIENE DE FACTURACIÓN YA NO ENVIAR ACCESOS
                    if (request.getParameter("VALIDA_FACTURAS") != null) {
                        //SI NO ES DESDE CONTABILIDAD, ENVIA PLANTILLAS
                        if (!request.getParameter("VALIDA_FACTURAS").equals("1")) {
                            //envia correos
                            System.out.println("envia plantillas desde la validacion VALIDA_FACTURAS linea 950");
                            strResultado = tele.sendMail(oConn, varSesiones, request, String.valueOf(intIdVta));
                            if (strResultado.equals("")) {
                                System.out.println("Hubó un problema al enviar los correos correspondientes a la venta");
                            }
                        }
                    } else {
                        //SI VIENE VACIO, ENVIA PLANTILLAS, NO PROVIENE DESDE CONTABILIDAD
                        //envia correos
                        System.out.println("envia plantillas desde la validacion opcion ## NO ## linea 958");
                        strResultado = tele.sendMail(oConn, varSesiones, request, String.valueOf(intIdVta));
                        if (strResultado.equals("")) {
                            System.out.println("Hubó un problema al enviar los correos correspondientes a la venta");
                        }
                    }

                    System.out.println("################## resultado de enviar los correos " + strResultado);
                    //cambia a ex participante, si NO es reservación/ ya se hizo ticket o se hizo factura
                    if (!bolReservacion && !strComprobantedepago.equals("")) {
                        //cambia de prospecto a ex participante
                        String strExParticipante = "update vta_cliente set CT_ES_PROSPECTO = 0 where CT_ID = " + intCT_ID;
                        oConn.runQueryLMD(strExParticipante);
                    }

                } else {
                    strRes = ticket.getStrResultLast();
                }

                if (request.getParameter("VALIDA_FACTURAS") != null) {
                    String strValidaFac = request.getParameter("VALIDA_FACTURAS");
                    if (strValidaFac.equals("1")) {
                        System.out.println("Se envia respuesta a: " + strRes);
                        request.getRequestDispatcher("/COFIDE_ValidaFactura.jsp?id=4&FAC_ID=" + request.getParameter("ID_FAC_OLD") + "&FacRespuesta=" + strRes).forward(request, response);

                        //asigna al autor de la venta
                        strRes = strRes.replace("OK.", "");
                        strSql = "UPDATE vta_facturas set FAC_US_ALTA = '" + request.getParameter("US_ALTA") + "' where FAC_ID = " + strRes;
                        oConn.runQueryLMD(strSql);
                        //asigna al autor de la venta
                    }
                }

                System.out.println("####### ID DE LA VENTA: [ " + strRes + " ] #######");
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(strRes);//Pintamos el resultado

            } //9

            if (strid.equals("10")) { //catalogos

                String strIdVta = request.getParameter("fac_id");
                String strTipoDoc = request.getParameter("tipo_doc");
                boolean bolPagado = false;
                strRes = "OK";

                strSql = "select * from view_ventasglobalesdeta as deta "
                        + "where FAC_ID = " + strIdVta + " and TIPO_DOC = '" + (strTipoDoc.equals("FACTURA") ? "F" : "T") + "' and FACD_TIPO_CURSO = 1 and "
                        + "(select FAC_PAGADO "
                        + "from view_ventasglobales as mastr "
                        + "where mastr.FAC_ID = deta.FAC_ID and mastr.TIPO_DOC = deta.TIPO_DOC and mastr.FAC_PAGADO = 1) = 1;";

                rs = oConn.runQuery(strSql, true);
                while (rs.next()) {
                    bolPagado = true;
                    strRes = "";
                }
                rs.close();

                //LA VENTA ESTA PAGADA Y ES PRESENCIAL, SE LE VAN A RETIRAR SUS PUNTOS
                if (bolPagado) {
                    //BUSCA PARTICIPANTES DE ESA VENTA
                    strSql = "select CONTACTO_ID from cofide_participantes "
                            + "where " + (strTipoDoc.equals("FACTURA") ? "CP_FAC_ID" : "CP_TKT_ID") + " = " + strIdVta + " AND CP_TIPO_CURSO = 1 ";
                    rs = oConn.runQuery(strSql, true);
                    while (rs.next()) {
                        String strSqlUpdate = "update cofide_puntos_contacto set CPC_ACTIVO = 0 "
                                + "where CONTACTO_ID = '" + rs.getString("CONTACTO_ID") + "' and CC_CURSO_ID = 0 limit 1";
                        oConn.runQueryLMD(strSqlUpdate);

                        if (!oConn.isBolEsError()) {
                            strRes = "OK";
                        }
                    }
                    rs.close();
                }
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(strRes);//Pintamos el resultado
            }
            if (strid.equals("11")) { //ROLLBACK

//                StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
//                strXML.append("<vta>");
                String strIdCTE = "";
                String strCCO_ID = "";
                String strCCO_NOMBRE = "";
                String strCCO_APPAT = "";
                String strCCO_APMAT = "";
                String strCCO_TITULO = "";
                String strCCO_NOSOCIO = "";
                String strCCO_AREA = "";
                String strCCO_ASOC = "";
                String strCCO_CORREO = "";
                String strCT_MailMes = "";
                String strCT_MailMes2 = "";
                String strCCO_CORREO2 = "";
                String strCCO_TELEFONO = "";
                String strCCO_EXT = "";
                String strCCO_ALTERNO = "";
                double dblTotalPtos = 0.0;
                int intIdContacto = Integer.parseInt(request.getParameter("contacto_id"));
                String strEnvio = "0"; //revisa si ya se le envio o no su cortesia
                String strSqlCTE = "select * from cofide_contactos where CONTACTO_ID = " + intIdContacto;
                try {
                    ResultSet rsCTE = oConn.runQuery(strSqlCTE, true);
                    while (rsCTE.next()) {
                        strCCO_ID = rsCTE.getString("CCO_ID");
                        strCCO_NOMBRE = rsCTE.getString("CCO_NOMBRE");
                        strCCO_APPAT = rsCTE.getString("CCO_APPATERNO");
                        strCCO_APMAT = rsCTE.getString("CCO_APMATERNO");
                        strCCO_TITULO = rsCTE.getString("CCO_TITULO");
                        strCCO_NOSOCIO = rsCTE.getString("CCO_NOSOCIO");
                        strCCO_AREA = rsCTE.getString("CCO_AREA");
                        strCCO_ASOC = rsCTE.getString("CCO_ASOCIACION");
                        strCCO_CORREO = rsCTE.getString("CCO_CORREO");
                        strCT_MailMes = rsCTE.getString("CT_MAILMES");
                        strCT_MailMes2 = rsCTE.getString("CT_MAILMES2");
                        strCCO_CORREO2 = rsCTE.getString("CCO_CORREO2");
                        strCCO_TELEFONO = rsCTE.getString("CCO_TELEFONO");
                        strCCO_EXT = rsCTE.getString("CCO_EXTENCION");
                        strCCO_ALTERNO = rsCTE.getString("CCO_ALTERNO");
                        intIdContacto = rsCTE.getInt("CONTACTO_ID");
                        strIdCTE = rsCTE.getString("CT_ID");

                        strXML.append("<datos ");
                        strXML.append(" CCO_ID = \"").append(strCCO_ID).append("\"");
                        strXML.append(" CCO_NOMBRE = \"").append(strCCO_NOMBRE).append("\"");
                        strXML.append(" CCO_APPATERNO = \"").append(strCCO_APPAT).append("\"");
                        strXML.append(" CCO_APMATERNO = \"").append(strCCO_APMAT).append("\"");
                        strXML.append(" CCO_TITULO = \"").append(strCCO_TITULO).append("\"");
                        strXML.append(" CCO_NOSOCIO = \"").append(strCCO_NOSOCIO).append("\"");
                        strXML.append(" CCO_AREA = \"").append(strCCO_AREA).append("\"");
                        strXML.append(" CCO_ASOCIACION = \"").append(strCCO_ASOC).append("\"");
                        strXML.append(" CCO_CORREO = \"").append(strCCO_CORREO).append("\"");
                        strXML.append(" CT_MAILMES1 = \"").append((strCT_MailMes.equals("1") ? "SI" : "NO")).append("\"");
                        strXML.append(" CT_MAILMES2 = \"").append((strCT_MailMes2.equals("1") ? "SI" : "NO")).append("\"");
                        strXML.append(" CCO_CORREO2 = \"").append(strCCO_CORREO2).append("\"");
                        strXML.append(" CCO_TELEFONO = \"").append(util.Sustituye(strCCO_TELEFONO)).append("\"");
                        strXML.append(" CCO_EXTENCION = \"").append(strCCO_EXT).append("\"");
                        strXML.append(" CCO_ALTERNO = \"").append(util.Sustituye(strCCO_ALTERNO)).append("\"");
                        strXML.append(" CCO_PUNTOS = \"").append(dblTotalPtos).append("\"");
                        strXML.append(" CONTACTO_ID = \"").append(intIdContacto).append("\"");
                        strXML.append(" CT_ID = \"").append(strIdCTE).append("\"");
                        strXML.append(" enviado = \"").append(strEnvio).append("\"");
                        strXML.append(" />");
                    }
                    rsCTE.close();
                } catch (SQLException e) {
                    System.out.println("ERROR AL OBTENER INFORMACIÓN DEL PARTICIANTE CON PROCMO/CLIENTE " + e.getMessage());
                }
                strXML.append("</vta>");
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML.toString());//Pintamos el resultado
            }

        } else {

            out.println("Sin Acceso");

        }
    }
%>

<%!
    Fechas fec = new Fechas();
    Telemarketing tele = new Telemarketing();

    public String getIdMasterVta(Conexion oConn) {
        String strIdMaster = "";
        String strSql = "insert into cofide_venta_m (CVM_FECHA) values (" + fec.getFechaActual() + ")";
        oConn.runQueryLMD(strSql);
        if (!oConn.isBolEsError()) {
            strSql = "select @@identity as Consecutivo";
            try {
                ResultSet rs = oConn.runQuery(strSql, true);
                while (rs.next()) {
                    strIdMaster = rs.getString("Consecutivo");
                }
                rs.close();
            } catch (SQLException sql) {
                System.out.println("Error al obtener el consecutivo [" + sql.getMessage() + "]");
            }
        }
        return strIdMaster;
    }

    /**
     * numero de participantes
     */
    public int getParticipantes(Conexion oConn, String strIdMaster, String strIdCurso, String strTipoCurso) {
        int intParticipantes = 0;
        String strSql = "select * from cofide_participantes where CP_ID_M = " + strIdMaster + " and CP_ID_CURSO = " + strIdCurso + " and CP_TIPO_CURSO = " + strTipoCurso;

        try {

            ResultSet rs = oConn.runQuery(strSql, true);

            while (rs.next()) {

                intParticipantes++;

            }

            rs.close();

        } catch (SQLException sql) {

            System.out.println("Error al Obtener la cantidad de participantes: [ " + sql.getMessage() + " ]");

        }

        return intParticipantes;
    }

//lugares ocupaos
    public int getLugarVendido(String strNomCurso, Conexion oConn) {
        int intVendidos = 0;
        int intRRHH = 0;
        String strSql = "select vd.FACD_CANTIDAD "
                + " from view_ventasglobalesdeta as vd, view_ventasglobales as v "
                + " where vd.FAC_ID = v.FAC_ID and vd.TIPO_DOC = v.TIPO_DOC "
                + " and FAC_ANULADA = 0 and CANCEL = 0 and FACD_TIPO_CURSO = 1 and PR_ID = " + strNomCurso
                + " order by v.TIPO_DOC, v.FAC_ID";
        try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                intVendidos += rs.getInt("FACD_CANTIDAD");
            }
            rs.close();
        } catch (SQLException sql) {
            System.out.println("Error al obtener el numero de lugares vendidos..." + sql.getMessage());
        }
        intRRHH = tele.getAsistentesRRHH(strNomCurso, oConn, 1);
        intVendidos += intRRHH;
        return intVendidos;
    }

//actualiza la información del curso
    public String updateCurso(Conexion oConn, HttpServletRequest request) {
        String strRespuesta = "OK";

        String strIdCurso = request.getParameter("idcurso");
        String stridMaster = request.getParameter("idmaster");
        int intLenght = Integer.parseInt(request.getParameter("length"));
        String strTipoCurso = request.getParameter("tipocurso");
        String strPrecio = request.getParameter("precio");
        String strDescuento = request.getParameter("descuento");
        String strDescuentoPor = request.getParameter("descuentopor");
        String strImporte = request.getParameter("importe");
        String strTotal = request.getParameter("total");
        String strIVA = request.getParameter("iva");
        System.out.println("2nd: " + strIVA);

        String strSql = "update cofide_venta set "
                + "CV_DESC = '" + strDescuento + "' , "
                + "CV_DESC_POR = '" + strDescuentoPor + "', "
                + "CV_TOTAL = '" + strTotal + "', "
                + "CV_IVA = '" + strIVA + "', "
                + "CV_LUGARES = '" + intLenght + "' "
                + "WHERE CV_ID_M = '" + stridMaster + "' AND CV_ID_CURSO = '" + strIdCurso + "' and CV_TIPO_CURSO = '" + strTipoCurso + "';";

        oConn.runQueryLMD(strSql);
        if (oConn.isBolEsError()) { //hubo un error

            strRespuesta = "Hubo un problema al afectar la venta con el curso:  " + strIdCurso + " [ error: " + oConn.getStrMsgError() + " ]";
            System.out.println(strRespuesta);

        }

        return strRespuesta;
    }

    /**
     * si es ticket, le va a quitar el IVA a los montos de la venta
     */
    public void quitIVA(Conexion oConn, String strIdmaster, String strTipoDoc) {
        double dblIva = 0.16;
        if (strTipoDoc.equals("2")) {
            dblIva = 0;
        }

        String strSql = "update cofide_venta "
                + "set CV_IVA = (((CV_PRECIO*CV_LUGARES) - CV_DESC) * " + dblIva + "), "
                + "CV_TOTAL = (((CV_PRECIO * CV_LUGARES) - CV_DESC) + CV_IVA) "
                + "where CV_ID_M = " + strIdmaster;
        oConn.runQueryLMD(strSql);

        if (oConn.isBolEsError()) {

            System.out.println("Ocurrio un problema al actualziar los montos en los cursos: [ " + oConn.getStrMsgError() + " ]");
        }
    }
%>