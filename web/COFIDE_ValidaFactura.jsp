<%-- 
    Document   : COFIDE_ValidaFactura
    Created on : 01-nov-2016, 12:26:17
    Author     : COFIDE
--%>

<%@page import="Tablas.vta_tickets"%>
<%@page import="comSIWeb.Operaciones.TableMaster"%>
<%@page import="Tablas.vta_cliente"%>
<%@page import="Tablas.vta_facturas"%>
<%@page import="java.util.Enumeration"%>
<%@page import="ERP.Folios"%>
<%@page import="java.io.File"%>
<%@page import="comSIWeb.Utilerias.Mail"%>
<%@page import="comSIWeb.ContextoApt.atrJSP"%>
<%@page import="java.sql.SQLException"%>
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
    String strRes = "";
    String strSql = "";
    ResultSet rs = null;

    if (varSesiones.getIntNoUser() != 0 && seg.ValidaURL(request)) {
        String strid = request.getParameter("id");
        if (strid != null) {

            //Consulta el reporte de comisiones por mes y en base a la base de los usuarios
            if (strid.equals("1")) {
                String strMes = request.getParameter("strMes");
                String strAnio = request.getParameter("strAnio");
                String strValidos = request.getParameter("Validos");
                int intTipoDoc = Integer.parseInt(request.getParameter("intTipoDoc"));
                int intCtId = Integer.parseInt(request.getParameter("intCtId"));
                String strFolio = request.getParameter("strFolio");
                int intIdVenta = Integer.parseInt(request.getParameter("intIdVenta"));

                String strFechaPago = "";
                String strCondicion = "";
                StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
                strXML.append("<ValidaFacturas>");
                if (strMes.equals("0")) {
                    strMes = fec.getFechaActual().substring(4, 6);
                }
                strSql = "select TIPO_DOC, FAC_ID, FAC_FECHA, FAC_FECHA_COBRO, FAC_SERIE, FAC_FOLIO, "
                        + "if(TIPO_DOC = 'F', FAC_FOLIO_C, FAC_FOLIO) AS FAC_FOLIO_C, "
                        + "FAC_RAZONSOCIAL, "
                        + "FAC_NOMPAGO, FAC_IMPORTE, FAC_IMPUESTO1, FAC_TOTAL, FAC_COFIDE_VALIDA, FAC_PAGADO AS FAC_COFIDE_PAGADO,"
                        + "CONCAT(nombre_usuario,' ', apellido_paterno , ' ' ,apellido_materno) AS AGENTE, FAC_FORMADEPAGO,FAC_METODODEPAGO,  "
                        + "(select MET_DESCRIPCION from vta_formas_pago where MET_CLAVE = FAC_FORMADEPAGO) as MET_DESCRIPCION,FAC_REFERENCIA,"
                        + "IFNULL((SELECT TKT_ID FROM vta_tickets WHERE TKT_ID = view_ventasglobales.TKT_ID_OLD),0) AS ID_VTA_OLD,"
                        + "IFNULL((SELECT TKT_FOLIO FROM vta_tickets WHERE TKT_ID = view_ventasglobales.TKT_ID_OLD),'0000000') AS FOLIO_OLD "
                        + "from view_ventasglobales, usuarios "
                        + "where FAC_US_ALTA = id_usuarios and DUPLICADO = 0 ";

                if (intCtId == 0) { //busca todo el historial del cliente
                    if (strFolio.equals("")) { //busca todas las coincidencias con el folio
                        strSql += "AND LEFT(FAC_FECHA,6) = '" + strAnio + strMes + "' ";
                    }
                }
                strSql += "and FAC_COFIDE_VALIDA = " + strValidos + " and FAC_ANULADA = 0 "
                        + "AND COFIDE_CARRITO = 0 AND CANCEL = 0 and ";

                if (intTipoDoc == 1) {
                    strCondicion += "FAC_PAGADO = 1  and FAC_ES_RESERVACION = 0 and TIPO_DOC = 'F' ";
                }

                if (intTipoDoc == 2) {
                    strCondicion += "FAC_PAGADO = 1  and FAC_ES_RESERVACION = 0 and TIPO_DOC = 'T' ";
                }

                if (intTipoDoc == 6) {
                    strCondicion += "((FAC_PAGADO = 1  and FAC_ES_RESERVACION = 0)"
                            + "OR "
                            + "(FAC_FACTURAR = 1 and FAC_ES_RESERVACION = 1 AND FAC_PAGADO = 1 ) ) "
                            + "and TIPO_DOC = 'T' ";
                }

                if (intCtId != 0) {
                    strCondicion += " and CT_ID = " + intCtId;
                }
                if (intIdVenta != 0) {
                    strCondicion += " and FAC_ID = " + intIdVenta;
                }

                if (!strFolio.equals("")) {
                    //factura
                    if (intTipoDoc == 1) {
                        strCondicion += " and FAC_FOLIO_C = '" + strGetFolioSql(oConn, strFolio) + "'";
                    }
                    //ticket
                    if (intTipoDoc == 2) {
                        strCondicion += " and FAC_FOLIO = '" + strGetFolioSql(oConn, strFolio) + "'";
                    }
                }

                strSql = strSql + strCondicion;

                try {
                    rs = oConn.runQuery(strSql, true);
                    while (rs.next()) {
                        strFechaPago = "";
                        if (!rs.getString("FAC_FECHA_COBRO").equals("")) {
                            strFechaPago = fec.FormateaDDMMAAAA(rs.getString("FAC_FECHA_COBRO"), "/");
                        }
                        strXML.append("<datos");
                        strXML.append(" FAC_FECHA = \"").append(fec.FormateaDDMMAAAA(rs.getString("FAC_FECHA"), "/")).append("\"");
                        strXML.append(" FAC_FECHA_PAGO = \"").append(strFechaPago).append("\"");
                        strXML.append(" FAC_SERIE = \"").append(util.Sustituye(rs.getString("FAC_SERIE"))).append("\"");
                        strXML.append(" FAC_FOLIO_C = \"").append(util.Sustituye(rs.getString("FAC_FOLIO_C"))).append("\"");
                        strXML.append(" FAC_FOLIO = \"").append(util.Sustituye(rs.getString("FAC_FOLIO"))).append("\"");
                        strXML.append(" FAC_RAZONSOCIAL = \"").append(util.Sustituye(rs.getString("FAC_RAZONSOCIAL"))).append("\"");
                        strXML.append(" FAC_NOMPAGO = \"").append(util.Sustituye(rs.getString("FAC_NOMPAGO"))).append("\"");
                        strXML.append(" FAC_IMPORTE = \"").append(rs.getDouble("FAC_IMPORTE")).append("\"");
                        strXML.append(" FAC_IMPUESTO1 = \"").append(rs.getDouble("FAC_IMPUESTO1")).append("\"");
                        strXML.append(" FAC_TOTAL = \"").append(rs.getDouble("FAC_TOTAL")).append("\"");
                        strXML.append(" FAC_COFIDE_VALIDA = \"").append((rs.getInt("FAC_COFIDE_VALIDA") == 1 ? "SI" : "NO")).append("\"");
                        strXML.append(" FAC_COFIDE_PAGADO = \"").append((rs.getInt("FAC_COFIDE_PAGADO") == 1 ? "SI" : "NO")).append("\"");
                        strXML.append(" FAC_ID = \"").append(rs.getInt("FAC_ID")).append("\"");
                        strXML.append(" FAC_TIPO = \"").append(rs.getString("TIPO_DOC")).append("\"");
                        strXML.append(" AGENTE = \"").append(rs.getString("AGENTE")).append("\"");
                        strXML.append(" FAC_FORMADEPAGO = \"").append(rs.getString("MET_DESCRIPCION")).append("\"");
                        strXML.append(" FAC_METODODEPAGO = \"").append(rs.getString("FAC_METODODEPAGO")).append("\"");
                        strXML.append(" FAC_REFERENCIA = \"").append(rs.getString("FAC_REFERENCIA")).append("\"");
                        strXML.append(" FAC_ID_OLD = \"").append(rs.getString("ID_VTA_OLD")).append("\"");
                        strXML.append(" FAC_FOLIO_OLD = \"").append(rs.getString("FOLIO_OLD")).append("\"");
                        strXML.append(" />");
                    }
                    rs.close();
                    strXML.append("</ValidaFacturas>");
                } catch (SQLException ex) {
                    System.out.println("Error GetConsulta Validar Facturas[1] " + ex.getLocalizedMessage());
                }
                strXML.toString();
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML);//Pintamos el resultado
            }//Fin ID 1

            //Transacciones Valida(1), Deniega(2) y Edita Fecha Pago(3) de las Facturas
            if (strid.equals("2")) {
                String strOpc = request.getParameter("strOpc");
                System.out.println(strOpc + " opción");
                if (strOpc.equals("1")) {
                    //Validamos la Factura
                    String strFacId = request.getParameter("FAC_ID");
                    String strTipo = request.getParameter("FAC_TIPO");
                    if (strTipo.equals("F")) {
                        strSql = "update vta_facturas set FAC_COFIDE_VALIDA = 1,FAC_COFIDE_PAGADO = 1 where FAC_ID = " + strFacId;
                    } else {
                        strSql = "update vta_tickets set TKT_COFIDE_VALIDA = 1,TKT_COFIDE_PAGADO = 1 where TKT_ID = " + strFacId;
                    }
                    oConn.runQueryLMD(strSql);
                    if (oConn.getStrMsgError().equals("")) {
                        strRes = "OK";
                    } else {
                        strRes = oConn.getStrMsgError();
                    }
                }//Fin Valida

                if (strOpc.equals("2")) {
                    //Denegamos la Factura
                    int intUser = varSesiones.getIntNoUser();
                    String strFacId = request.getParameter("FAC_ID");
                    String strFacDenegar = request.getParameter("strMotivoDen");
                    String strTipo = request.getParameter("FAC_TIPO");
                    if (strTipo.equals("F")) {
                        strSql = "update vta_facturas set FAC_COFIDE_VALIDA = 0,FAC_COFIDE_PAGADO = 0,"
                                + "FAC_MOTIVO_DENEGADA = '" + strFacDenegar + "'"
                                + " where FAC_ID = " + strFacId;
                    } else {
                        strSql = "update vta_tickets set TKT_COFIDE_VALIDA = 0,TKT_COFIDE_PAGADO = 0,"
                                + "TKT_MOTIVO_DENEGADA = '" + strFacDenegar + "'"
                                + " where TKT_ID = " + strFacId;
                    }
                    oConn.runQueryLMD(strSql);
                    if (oConn.getStrMsgError().equals("")) {
                        //Enviamos el MAIL al cliente
                        strRes = getMailDenegarPago(oConn, strFacId, strFacDenegar, intUser);
                    } else {
                        strRes = oConn.getStrMsgError();
                    }
                }//Fin Deniega

                if (strOpc.equals("3")) {
                    //Edita Fecha de Pago
                    String strFacId = request.getParameter("FAC_ID");
                    String strFechaPago = request.getParameter("FechaPago");
                    String strTipo = request.getParameter("FAC_TIPO");
                    strFechaPago = fec.FormateaBD(strFechaPago, "/");
                    if (strTipo.equals("F")) {
                        strSql = "update vta_facturas set FAC_FECHA_COBRO = '" + strFechaPago + "' where FAC_ID = " + strFacId;
                    } else {
                        strSql = "update vta_tickets set TKT_FECHA_COBRO = '" + strFechaPago + "' where TKT_ID = " + strFacId;
                    }
                    oConn.runQueryLMD(strSql);
                    if (oConn.getStrMsgError().equals("")) {
                        strRes = "OK";
                    } else {
                        strRes = oConn.getStrMsgError();
                    }
                }//Fin Deniega

                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(strRes);//Pintamos el resultado

            }//Fin ID 2

            //Imprime el documento que se adjunto a la FACTURA.
            if (strid.equals("3")) {
                String strFacId = request.getParameter("FAC_ID");
                String strNombrePago = "";
                String strFechaCreate = "";
                String strFilePath = "";
                boolean bolExistFile = false;
                strSql = "select FAC_FECHA,FAC_NOMPAGO from view_ventasglobales where FAC_ID = " + strFacId;
                try {
                    rs = oConn.runQuery(strSql, true);
                    while (rs.next()) {
                        strNombrePago = rs.getString("FAC_NOMPAGO");
                        if (strNombrePago.equals("")) {
                            bolExistFile = false;
                        } else {
                            bolExistFile = true;
                        }
                        strFechaCreate = rs.getString("FAC_FECHA").substring(0, 6);
//                        strFechaCreate = rs.getString("FAC_FECHACREATE").substring(0, 6);
                    }
                    rs.close();
                } catch (SQLException ex) {
                    System.out.println("Get Nombre del Pago de la Factura [ID:3]: " + ex.getLocalizedMessage());
                }
                String strPathBase = this.getServletContext().getRealPath("/");
                String strSeparator = System.getProperty("file.separator");
                if (strSeparator.equals("\\")) {
                    strSeparator = "/";
                    strPathBase = strPathBase.replace("\\", "/");
                }
                strFilePath = strPathBase + "document" + strSeparator + "Comprobantes" + strSeparator + strFechaCreate + strSeparator + strNombrePago;

                System.out.println("########### FECHA CREADA: " + strFechaCreate);
                String strFechaCreate_tmp = "";

                File saveTo = new File(strFilePath);

                System.out.println("resultado: " + bolExistFile);

                if (bolExistFile) {

//                    System.out.println("SAVETO 1: " + saveTo);
                    if (saveTo.exists()) { //busca si, existe en al primer ruta

                        strFilePath = "document" + strSeparator + "Comprobantes" + strSeparator + strFechaCreate + strSeparator + strNombrePago;
                        strRes = "OK." + strFilePath;
//                        System.out.println("ruta 1: " + strFilePath);

                    } else {
                        boolean bolRespuesta = false;
                        for (int i = 1; i <= 12; i++) {

                            strFechaCreate_tmp = fec.addFecha(strFechaCreate + "01", 2, i);
                            strFilePath = strPathBase + "document" + strSeparator + "Comprobantes" + strSeparator + strFechaCreate_tmp.substring(0, 6) + strSeparator + strNombrePago;
                            saveTo = new File(strFilePath);
                            System.out.println("RUTA " + i + " : " + strFilePath);

                            if (saveTo.exists()) {
                                strFilePath = "document" + strSeparator + "Comprobantes" + strSeparator + strFechaCreate_tmp.substring(0, 6) + strSeparator + strNombrePago;
                                strRes = "OK." + strFilePath;
                                bolRespuesta = true;
                                break;
                            }

                            strFechaCreate_tmp = fec.addFecha(strFechaCreate + "01", 2, -i);
                            strFilePath = strPathBase + "document" + strSeparator + "Comprobantes" + strSeparator + strFechaCreate_tmp.substring(0, 6) + strSeparator + strNombrePago;
                            saveTo = new File(strFilePath);
                            System.out.println("RUTA " + -i + " : " + strFilePath);

                            if (saveTo.exists()) {
                                strFilePath = "document" + strSeparator + "Comprobantes" + strSeparator + strFechaCreate_tmp.substring(0, 6) + strSeparator + strNombrePago;
                                strRes = "OK." + strFilePath;
                                bolRespuesta = true;
                                break;
                            }

                        }
                        if (!bolRespuesta) {
                            System.out.println("EL ARCHIVO NO EXISTE, EN NINUGNA DE TODAS LAS RUTAS");
                            strRes = "El archivo " + strNombrePago + ". No Existe.";
                        }

                    }

                } else {
                    strRes = "No cuenta con un archivo.";
                }

                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(strRes);//Pintamos el resultado
                
            }//Fin ID 3

            //Parametros para timbrar una factura
            if (strid.equals("4")) {
                String strIdFactura = request.getParameter("FAC_ID");
                System.out.println("ENTRO AL ID 4");
                if (request.getParameter("FacRespuesta") != null) {
                    System.out.println("Se obtiene respuesta: " + request.getParameter("FacRespuesta"));
                    strRes = request.getParameter("FacRespuesta");
                    if (strRes.contains("OK")) {
                        strSql = "update vta_tickets set "
                                + "TKT_ANULADA = 1, "
                                + "TKT_FECHAANUL = '" + fec.getFechaActual() + "', "
                                + "TKT_US_MOD = " + varSesiones.getIntNoUser()
                                + " where TKT_ID = " + strIdFactura;
                        oConn.runQueryLMD(strSql);
                        System.out.println("SOMOENOE DO THIS");
                    }
                } else {
                    System.out.println("ENTRO DIRECTO PARA HACER LA VENTA Y RECIBIO ID DE VENTA: " + strIdFactura);
                    String strParametros = setParametrosFactura(oConn, Integer.parseInt(strIdFactura), "TKT", "TKTD");
                    request.getRequestDispatcher("/COFIDE_Tmk_vta.jsp?ID=9" + strParametros).forward(request, response);
                }

                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(strRes);//Pintamos el resultado
            }//Fin ID 4
            if (strid.equals("5")) {

                String strIdFactura = request.getParameter("FAC_ID");
                String strFormaPago = request.getParameter("forma");
                String strMetodoPago = request.getParameter("metodo");
                String strDigito = request.getParameter("digito");

                strSql = "update vta_tickets set "
                        + "TKT_METODODEPAGO = '" + strMetodoPago + "', "
                        + "TKT_FORMADEPAGO = '" + strFormaPago + "', "
                        + "TKT_REFERENCIA = '" + strDigito + "' "
                        + "where TKT_ID = " + strIdFactura + ";";

                oConn.runQueryLMD(strSql);

                if (!oConn.isBolEsError()) {
                    strRes = "OK";
                } else {
                    strRes = "ERROR: [ " + oConn.getStrMsgError() + " ]";
                }

                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(strRes);//Pintamos el resultado

            }
            if (strid.equals("6")) {
                StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
                strXML.append("<vta>");
                //información de la venta
                String strIdFactura = request.getParameter("FAC_ID");
                strSql = "select TKT_ID, TKT_FORMADEPAGO, TKT_METODODEPAGO, TKT_REFERENCIA from vta_tickets where TKT_ID = " + strIdFactura;
                rs = oConn.runQuery(strSql, true);
                while (rs.next()) {

                    strXML.append("<datos");
                    strXML.append(" DIGITO = \"").append(rs.getString("TKT_REFERENCIA")).append("\"");
                    strXML.append(" METODO = \"").append(rs.getString("TKT_METODODEPAGO")).append("\"");
                    strXML.append(" FORMA = \"").append(rs.getString("TKT_FORMADEPAGO")).append("\"");
                    strXML.append(" />");
                }
                rs.close();
                strXML.append("</vta>");
                strXML.toString();
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML);//Pintamos el resultado                                                                              
            }
            if (strid.equals("7")) {
                //metodo de pago
                StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
                strXML.append("<vta>");
                strSql = "select MP_CLAVE, concat(MP_CLAVE, ' - ' ,upper(MP_DESPRIPCION)) as descripcion from vta_metodo_pago;";
                rs = oConn.runQuery(strSql, true);
                while (rs.next()) {

                    strXML.append("<datos");
                    strXML.append(" CLAVE = \"").append(rs.getString("MP_CLAVE")).append("\"");
                    strXML.append(" DESCRIPCION = \"").append(rs.getString("descripcion")).append("\"");
                    strXML.append(" />");

                }
                rs.close();

                strXML.append("</vta>");
                strXML.toString();
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML);//Pintamos el resultado

            }
            if (strid.equals("8")) {
                //forma de pago
                StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
                strXML.append("<vta>");
                strSql = "select MET_CLAVE, concat(MET_CLAVE,' - ',upper(MET_DESCRIPCION)) as descripcion from vta_formas_pago;";
                rs = oConn.runQuery(strSql, true);
                while (rs.next()) {

                    strXML.append("<datos");
                    strXML.append(" CLAVE = \"").append(rs.getString("MET_CLAVE")).append("\"");
                    strXML.append(" DESCRIPCION = \"").append(rs.getString("descripcion")).append("\"");
                    strXML.append(" />");

                }
                rs.close();

                strXML.append("</vta>");
                strXML.toString();
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML);//Pintamos el resultado

            } //8

            if (strid.equals("9")) {
                strRes = "";
                String strIdTrx = request.getParameter("idTrx");
                strSql = "select FAC_TOTAL, FAC_SALDO, FAC_METODODEPAGO from vta_facturas where FAC_ID = " + strIdTrx;
                String strParametros = "&FAC_TIPO=F";
                strParametros += "&FAC_ID=" + strIdTrx;
                strParametros += "&strOpc=1";
                try {
                    rs = oConn.runQuery(strSql, true);
                    while (rs.next()) {

                        if (rs.getString("FAC_METODODEPAGO").equals("PPD")) {
                            //solamente ventas PPD
                            if (rs.getDouble("FAC_SALDO") > 0) {
                                //tiene aun saldo pendiente, continua en el listado de pendientes

                            } else {
                                // se liquido la factura y se valida
                                request.getRequestDispatcher("/COFIDE_ValidaFactura.jsp?id=2" + strParametros).forward(request, response);
                            }
                            strRes = "OK";

                        }

                    }
                    rs.close();

                } catch (SQLException sql) {
                    System.out.println("Error: " + sql.getMessage());
                }
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(strRes);//Pintamos el resultado
            } //9
        }
        oConn.close();
    } else {
        out.print("Sin Acceso");
    }
%>

<%!
    public String getMailDenegarPago(Conexion oConn, String strFacId, String strRazonDenegar, int intUser) {
        String strSql = "select FAC_ID,view_ventasglobales.CT_ID,FAC_RAZONSOCIAL,IF(tipo_doc = 'F',FAC_FOLIO_C,FAC_FOLIO) AS FOLIO, SMTP_US as MailCt,CT_EMAIL1,CT_EMAIL2  "
                + "from view_ventasglobales,usuarios,vta_cliente "
                + "where view_ventasglobales.FAC_US_ALTA = usuarios.id_usuarios "
                + "and view_ventasglobales.CT_ID = vta_cliente.CT_ID "
                + "and FAC_ID = " + strFacId;
        String strFolioFac = "c";
        String strMailCT = "";
        String strRes = "";
        Fechas fec = new Fechas();

        String strCT_ID = "";
        String strFacRazonSocial = "";
        String strUser = "";
        String strPassword = "";
        int intTLS = 0;
        int intSTLS = 0;
        String strServer = "";
        String strPort = "";

        try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                strCT_ID = rs.getString("CT_ID");
                strFacRazonSocial = rs.getString("FAC_RAZONSOCIAL");
                strFolioFac = rs.getString("FOLIO");
                strMailCT = rs.getString("MailCt");
            }
            rs.close();
        } catch (SQLException ex) {
            System.out.println("Get Mail Denegar Pago: " + ex.getLocalizedMessage());
        }
        strSql = "select * from usuarios where id_usuarios = " + intUser;
        try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                strUser = rs.getString("SMTP_US");
                strPassword = rs.getString("SMTP_PASS");
                strServer = rs.getString("SMTP");
                strPort = rs.getString("PORT");
                intTLS = rs.getInt("SMTP_USATLS");
                intSTLS = rs.getInt("SMTP_USASTLS");
            }
            rs.close();
        } catch (SQLException ex) {
            System.out.println("Error al obtener ejecutivo: " + ex);
        }
        String strMailEnvio = "";
        if (!strMailCT.equals("")) {
//            if (!strMailCT1.equals(""))             strMailEnvio = strMailCT + "," + strMailC              if (!strMailCT2.equals("                    strMailEnvio = strMailEnvio + "," + strM                              }
            strMailEnvio = strMailCT;
        }
        if (!strMailCT.equals("")) {
            Mail mail = new Mail();
            mail.setUsuario(strUser);
            mail.setContrasenia(strPassword);
            mail.setHost(strServer);
            mail.setPuerto(strPort);
//Adjuntamos XML y PDF
            if (intTLS == 1) {
                mail.setBolUsaTls(true);
            }
            if (intSTLS == 1) {
                mail.setBolUsaStartTls(true);
            }
            if (mail.isEmail(strMailCT)) {
                //Intentamos mandar el mail
                mail.setBolDepuracion(false);
                mail.getTemplate("FAC_DENEGADA", oConn);
                String strAsuntoMail = mail.getAsunto().replace("%FECHA%", fec.getFechaActualDDMMAAAAguion());
                String strMessage = mail.getMensaje().replace("%FAC_FOLIO_C%", strFolioFac);
                strMessage = strMessage.replace("%ID_CLIENTE%", strCT_ID);
                strMessage = strMessage.replace("%RAZON_SOCIAL%", strFacRazonSocial);
                strMessage = strMessage.replace("%RAZON_DENIED%", strRazonDenegar);
                strMessage = replaceCharMailValida(strMessage);
                mail.setMensaje(strMessage);
                mail.setDestino(strMailEnvio);
                mail.setAsunto(strAsuntoMail);
                boolean bol = mail.sendMail();
                if (bol) {
                    strRes = "OK";
                    //strResp = "MAIL ENVIADO.";
                } else {
                    strRes = "no se envio...";
                    //strResp = "FALLO EL ENVIO DEL MAIL.";
                }
            } else {
                strRes = "El correo del ejecutivo no es valido.";
            }
        } else {
            strRes = "No se ha definido un Correo para el cliente de la venta.";
        }

        return strRes;
    }//Fin getMailInstructor

    public String replaceCharMailValida(String strChar) {
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

    public String strGetFolioSql(Conexion oConn, String strFolio) {
        Folios folio = new Folios();
        int intNumDigitos = folio.getIntNumDigitos();
        String strTmpFolio = "";
        int intTmp = 0;
        intTmp = intNumDigitos - strFolio.length();
        for (int i = 0; i < intTmp; i++) {
            strTmpFolio += "0";
        }
        String strFinalFolio = strTmpFolio + strFolio;
        return strFinalFolio;
    }

    public String setParametrosFactura(Conexion oConn, int intFacId, String strPrefijoMaster, String strPrefijoDeta) {
        System.out.println("ENTRO PARA OBTENER PARAMETROS DE LA VENTA QUE SE VA A FACTURAR");

        int intIdCliente = 0;
        String strTipoVenta = "";
        TableMaster Factura;

        if (strPrefijoMaster.equals("FAC")) {
            strTipoVenta = "1";
            Factura = new vta_facturas();
        } else {
            strTipoVenta = "3";
            Factura = new vta_tickets();
        }
        //Definición de Objetos
        vta_cliente Cliente = new vta_cliente();

        //Variables
        Factura.ObtenDatos(intFacId, oConn);

        intIdCliente = Factura.getFieldInt("CT_ID");
        Cliente.ObtenDatos(intIdCliente, oConn);

        StringBuilder strBuild = new StringBuilder();

        //Datos del cliente
//        System.out.println("************************************************************************************** DFA: " + Factura.getFieldInt("DFA_ID"));
        strBuild.append("&DFA_ID=").append(Factura.getFieldInt("DFA_ID"));
        strBuild.append("&refactura=").append(0);
        strBuild.append("&ID_FAC_OLD=").append(intFacId);
        strBuild.append("&VALIDA_FACTURAS=").append("1");
        strBuild.append("&CT_ID=").append(Cliente.getFieldInt("CT_ID"));
        strBuild.append("&CT_RAZONSOCIAL=").append(Factura.getFieldString("TKT_RAZONSOCIAL"));
        strBuild.append("&CT_RFC=").append(Factura.getFieldString("TKT_RFC"));
//        strBuild.append("&CT_CORREO=").append(Cliente.getFieldString("CT_CORREO"));
//        strBuild.append("&CT_CORREO2=").append(Cliente.getFieldString("CT_CORREO2"));
        strBuild.append("&CT_COMENTARIO=").append(Factura.getFieldString("TKT_NOTASPIE"));
        strBuild.append("&CT_CP=").append(Factura.getFieldString("TKT_CP"));
        strBuild.append("&CT_CALLE=").append(Factura.getFieldString("TKT_CALLE"));
        strBuild.append("&CT_COL=").append(Factura.getFieldString("TKT_COLONIA"));
        strBuild.append("&CT_NUM=").append(Factura.getFieldString("TKT_NUMERO"));
        strBuild.append("&CT_NUMINT=").append(Factura.getFieldString("TKT_NUMINT"));
        strBuild.append("&CT_MUNICIPIO=").append(Factura.getFieldString("TKT_MUNICIPIO"));
        strBuild.append("&CT_ESTADO=").append(Factura.getFieldString("TKT_ESTADO"));

        strBuild.append("&US_ALTA=").append(Factura.getFieldString("TKT_US_ALTA"));
//        System.out.println("\n\n&&&&&&&&&&&&&&& USUARIO QUIEN DIO DE ALTA: " + Factura.getFieldString("TKT_US_ALTA") + " &&&&&&&&&&&&&&&&&&&&&&\n\n");

        //Datos de la Factura
        strBuild.append("&FAC_SERIE=").append(Factura.getFieldString("TKT_SERIE"));
        strBuild.append("&SC_ID=").append(Factura.getFieldInt("SC_ID"));
        strBuild.append("&esReservacion=").append(0);
        strBuild.append("&IDMOV=").append(Factura.getFieldInt(strPrefijoMaster + "_ID_MOV"));
        strBuild.append("&").append("FAC").append("_MONEDA=").append(Factura.getFieldInt(strPrefijoMaster + "_MONEDA"));
        strBuild.append("&VE_ID=").append(Factura.getFieldInt("VE_ID"));
        strBuild.append("&TI_ID=").append(Factura.getFieldInt("TI_ID"));
        strBuild.append("&TI_ID2=").append(Factura.getFieldInt("TI_ID2"));
        strBuild.append("&TI_ID3=").append(Factura.getFieldInt("TI_ID3"));

        strBuild.append("&").append("FAC").append("_ESSERV=").append(Factura.getFieldString(strPrefijoMaster + "_ESSERV"));
        strBuild.append("&").append("FAC").append("_FOLIO=").append(Factura.getFieldString(strPrefijoMaster + "_FOLIO"));
        strBuild.append("&").append("FAC").append("_FOLIO_C=").append(Factura.getFieldString(strPrefijoMaster + "_FOLIO"));
        strBuild.append("&").append("FAC").append("_NOTAS=").append(Factura.getFieldString(strPrefijoMaster + "_NOTAS"));
        strBuild.append("&").append("FAC").append("_NOTASPIE=").append(Factura.getFieldString(strPrefijoMaster + "_NOTASPIE"));
        strBuild.append("&").append("FAC").append("_REFERENCIA=").append(Factura.getFieldString(strPrefijoMaster + "_REFERENCIA"));
        strBuild.append("&").append("FAC").append("_CONDPAGO=").append(Factura.getFieldString(strPrefijoMaster + "_CONDPAGO"));
        strBuild.append("&").append("FAC").append("_METODOPAGO=").append(Factura.getFieldString(strPrefijoMaster + "_METODODEPAGO"));
        strBuild.append("&").append("FAC").append("_NUMCUENTA=").append("0");
        strBuild.append("&").append("FAC").append("_FORMADEPAGO=").append(Factura.getFieldString(strPrefijoMaster + "_FORMADEPAGO"));
        double dblImporte = (Factura.getFieldDouble(strPrefijoMaster + "_IMPORTE") - Factura.getFieldDouble(strPrefijoMaster + "_DESCUENTO"));
//        System.out.println("\n\n&&&&&&&&&&&&& Importe OKIS(importe - descuento): " + dblImporte + " &&&&&&&&&&&&&&&&&&&&&&\n\n");

        strBuild.append("&").append("FAC").append("_IMPORTE=").append(Factura.getFieldDouble(strPrefijoMaster + "_IMPORTE"));
//        strBuild.append("&").append("FAC").append("_IMPORTE=").append(dblImporte);
//        System.out.println("\n\n&&&&&&&&&&&&& Importe anterior(importe bruto): " + Factura.getFieldDouble(strPrefijoMaster + "_IMPORTE") + " &&&&&&&&&&&&&&&&&&&&&&\n\n");
        strBuild.append("&").append("FAC").append("_DESCUENTO=").append(Factura.getFieldDouble(strPrefijoMaster + "_DESCUENTO"));
//        System.out.println("\n\n&&&&&&&&&&&&& DESCUENTO " + Factura.getFieldDouble(strPrefijoMaster + "_DESCUENTO") + " &&&&&&&&&&&&&&&&&&&&&&\n\n");
        strBuild.append("&").append("FAC").append("_IMPUESTO1=").append(Factura.getFieldDouble(strPrefijoMaster + "_IMPUESTO1"));
//        System.out.println("\n\n&&&&&&&&&&&&& IMPUESTO " + Factura.getFieldDouble(strPrefijoMaster + "_IMPUESTO1") + " &&&&&&&&&&&&&&&&&&&&&&\n\n");
        strBuild.append("&").append("FAC").append("_IMPUESTO2=").append(Factura.getFieldDouble(strPrefijoMaster + "_IMPUESTO2"));
        strBuild.append("&").append("FAC").append("_IMPUESTO3=").append(Factura.getFieldDouble(strPrefijoMaster + "_IMPUESTO3"));
        strBuild.append("&").append("FAC").append("_TOTAL=").append(Factura.getFieldDouble(strPrefijoMaster + "_TOTAL"));
//        System.out.println("\n\n&&&&&&&&&&&&& TOTAL " + Factura.getFieldDouble(strPrefijoMaster + "_TOTAL") + " &&&&&&&&&&&&&&&&&&&&&&\n\n");
        strBuild.append("&").append("FAC").append("_TASA1=").append(Factura.getFieldDouble(strPrefijoMaster + "_TASA1"));
        strBuild.append("&").append("FAC").append("_TASA2=").append(Factura.getFieldDouble(strPrefijoMaster + "_TASA2"));
        strBuild.append("&").append("FAC").append("_TASA3=").append(Factura.getFieldDouble(strPrefijoMaster + "_TASA3"));
        strBuild.append("&").append("FAC").append("_TASAPESO=").append(Factura.getFieldDouble(strPrefijoMaster + "_TASAPESO"));

        strBuild.append("&").append("FAC").append("_DIASCREDITO=").append(Factura.getFieldInt(strPrefijoMaster + "_DIASCREDITO"));
        strBuild.append("&").append("FAC").append("_FACTURAR=").append(Factura.getFieldInt(strPrefijoMaster + "_FACTURAR"));
        strBuild.append("&TR_ID=").append(Factura.getFieldInt("TR_ID"));
        strBuild.append("&ME_ID=").append(Factura.getFieldInt("ME_ID"));
        strBuild.append("&TF_ID=").append(Factura.getFieldInt("TF_ID"));
        strBuild.append("&").append("FAC").append("_NUM_GUIA=").append(Factura.getFieldInt(strPrefijoMaster + "_NUM_GUIA"));
        strBuild.append("&").append("FAC").append("_RETISR=").append(Factura.getFieldInt(strPrefijoMaster + "_RETISR"));
        strBuild.append("&").append("FAC").append("_RETIVA=").append(Factura.getFieldInt(strPrefijoMaster + "_RETIVA"));
        strBuild.append("&").append("FAC").append("_NETO=").append(Factura.getFieldDouble(strPrefijoMaster + "_NETO"));
        strBuild.append("&").append("FAC").append("_REGIMENFISCAL=").append(Factura.getFieldString(strPrefijoMaster + "_REGIMENFISCAL"));
        strBuild.append("&").append("FAC").append("_SERIE=").append(Factura.getFieldString(strPrefijoMaster + "_SERIE"));
        strBuild.append("&").append("FAC").append("_TURNO=").append(Factura.getFieldInt(strPrefijoMaster + "_TURNO"));
        strBuild.append("&").append("CEV_USO_CFDI=").append(Factura.getFieldString(strPrefijoMaster + "_USO_CFDI"));
        strBuild.append("&COFIDE_DUPLICIDAD=").append(Factura.getFieldInt("COFIDE_DUPLICIDAD"));
        strBuild.append("&COFIDE_DUPLICIDAD_ID=").append(Factura.getFieldInt("COFIDE_DUPLICIDAD_ID"));
        strBuild.append("&").append("FAC").append("_TIPO_CURSO=").append(Factura.getFieldInt(strPrefijoMaster + "_TIPO_CURSO"));

        //Venta
        strBuild.append("&TIPOVENTA=").append(strTipoVenta);

        //Faltantes
        strBuild.append("&CEV_CORREO=").append("");
        strBuild.append("&CEV_CORREO2=").append("");
        strBuild.append("&CEV_MPUBLICIDAD=").append(Factura.getFieldInt("MP_ID"));
        strBuild.append("&CEV_DUPLICIDAD=").append(Factura.getFieldInt("COFIDE_DUPLICIDAD"));
        strBuild.append("&CEV_DUPLICIDAD_ID=").append(0);
        strBuild.append("&CEV_IDCURSO=").append(0);
        strBuild.append("&CEV_COMENTARIO=").append("");
//        strBuild.append("&CEV_FECHAPAGO=").append("");
        strBuild.append("&CEV_DIGITO=").append("");
        strBuild.append("&CEV_TIPO_CURSO=").append("");//SE REPITE

        strBuild.append("&vta_nvo=").append(Factura.getFieldInt("COFIDE_NVO"));
        strBuild.append("&pagoOk=").append(Factura.getFieldInt("TKT_COFIDE_PAGADO"));
        strBuild.append("&CEV_NOM_FILE=").append(Factura.getFieldString("TKT_NOMPAGO"));
        strBuild.append("&CEV_FECHAPAGO=").append(Factura.getFieldString("TKT_FECHA_COBRO"));
//        System.out.println("fecha en que se cobro: " + Factura.getFieldString("TKT_FECHA_COBRO"));
//        strBuild.append("&FAC_COFIDE_VALIDA=").append(Factura.getFieldString("TKT_COFIDE_VALIDA"));
        strBuild.append("&FAC_COFIDE_VALIDA=1");//PROVIENE DESDE CONTABILIDAD, YA VIENE VALIDADO
        strBuild.append("&promo=").append(0);

        //Parametros de los productos de la factura
        int intCountItem = getCountFacturasDeta(oConn, intFacId, "TKTD");
        strBuild.append("&COUNT_ITEM=").append(intCountItem);
        strBuild.append(getStringFacDeta(oConn, intFacId, strPrefijoDeta));

        return strBuild.toString();

    }//Fin setParametrosFactura

    public int getCountFacturasDeta(Conexion oConn, int intFacId, String strPrefijoDeta) {
        String strNomTablaDeta = "";
        if (strPrefijoDeta.equals("FACD")) {
            strNomTablaDeta = "vta_facturasdeta";
        } else {
            strNomTablaDeta = "vta_ticketsdeta";
        }
        String strSql = "select count(*) as CuantosDeta from " + strNomTablaDeta + " where " + strPrefijoDeta + "_ID = " + intFacId;
        int intCuantos = 0;

        try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                intCuantos = rs.getInt("CuantosDeta");
            }
            rs.close();
        } catch (SQLException ex) {
            System.out.println("Error getCountFacturasDeta " + ex.getLocalizedMessage());
        }
        return intCuantos;
    }//Fin getCountFacturasDeta

    public String getStringFacDeta(Conexion oConn, int intFacId, String strPrefijoDeta) {
        StringBuilder strBuild = new StringBuilder();
        String strSql = "select * from vta_ticketsdeta where TKT_ID = " + intFacId;

        try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {

                strBuild.append("&SC_ID=").append(rs.getInt("SC_ID"));
                strBuild.append("&PR_ID=").append(rs.getInt("PR_ID"));
                strBuild.append("&").append("CV_CURSO=").append(rs.getString(strPrefijoDeta + "_DESCRIPCION"));
                strBuild.append("&").append("FACD").append("_TASAIVA1=").append(rs.getDouble(strPrefijoDeta + "_TASAIVA1"));
                strBuild.append("&").append("FACD").append("_UNIDAD_MEDIDA=").append(rs.getString(strPrefijoDeta + "_UNIDAD_MEDIDA"));
                strBuild.append("&").append("FACD").append("_CVE_PRODSERV=").append(rs.getString(strPrefijoDeta + "_CVE_PRODSERV"));
                strBuild.append("&").append("FACD").append("_COMENTARIO=").append(rs.getString(strPrefijoDeta + "_COMENTARIO"));

            }//Fin WHILE

            rs.close();
        } catch (SQLException ex) {
            System.out.println("Error getStringFacDeta " + ex.getLocalizedMessage());
        }
        return strBuild.toString();
    }//Fin getStringFacDeta

%>