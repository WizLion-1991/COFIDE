<%-- 
    Document   : COFIDE_ValFac_Duplicados
    Created on : 18-nov-2016, 13:10:47
    Author     : COFIDE
--%>

<%@page import="ERP.Folios"%>
<%@page import="java.text.ParseException"%>
<%@page import="com.mx.siweb.erp.especiales.cofide.COFIDE_Mail_cursos"%>
<%@page import="java.io.File"%>
<%@page import="comSIWeb.Operaciones.Reportes.CIP_Formato"%>
<%@page import="comSIWeb.Operaciones.Formatos.Formateador"%>
<%@page import="comSIWeb.Operaciones.Formatos.FormateadorMasivo"%>
<%@page import="comSIWeb.Operaciones.Reportes.PDFEventPage"%>
<%@page import="comSIWeb.Operaciones.Reportes.PDFEventPage"%>
<%@page import="com.itextpdf.text.Document"%>
<%@page import="java.io.FileOutputStream"%>
<%@page import="com.itextpdf.text.pdf.PdfWriter"%>
<%@page import="ERP.ERP_MapeoFormato"%>
<%@page import="Tablas.vta_facturadeta"%>
<%@page import="comSIWeb.Operaciones.TableMaster"%>
<%@page import="Tablas.vta_ticketsdeta"%>
<%@page import="ERP.Ticket"%>
<%@page import="Tablas.vta_cliente_facturacion"%>
<%@page import="com.mx.siweb.erp.especiales.cofide.Telemarketing"%>
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

            //Consulta Tickets con la bandera COFIDE_DUPLICIDAD = 1
            if (strid.equals("1")) {
                StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
                strXML.append("<VtasDuplicadas>");
                strSql = "select VTA_ID_M, if(VTA_TIPO=3,'RESERVACIÓN',if(VTA_TIPO=2,'TICKET','FACTURA')) as TIPO,"
                        + "VTA_CT_ID,VTA_FECHA_ALTA,VTA_HORA,"
                        + "(select CT_CLAVE_DDBB from vta_cliente where vta_cliente.CT_ID = VTA_CT_ID) as DB_CT,"
                        + "(select CT_RAZONSOCIAL from vta_cliente where vta_cliente.CT_ID = VTA_CT_ID) as RAZON_CT,"
                        + "(select nombre_usuario from usuarios where id_usuarios = VTA_AGENTE) as AGENTE,"
                        + "VTA_CORREO_D, VTA_TELEFONO_D, VTA_RFC_D "
                        + "from cofide_venta_temporal where VTA_ESTATUS = 1 "
                        + "order by VTA_FECHA_ALTA,VTA_HORA";
                try {
                    rs = oConn.runQuery(strSql, true);
                    while (rs.next()) {
                        strXML.append("<datos");
                        strXML.append(" VL_ID = \"").append(rs.getString("VTA_ID_M")).append("\""); //id maestro de la venta temporal
                        strXML.append(" FECHA = \"").append(fec.FormateaDDMMAAAA(rs.getString("VTA_FECHA_ALTA"), "/")).append("\""); //id maestro de la venta temporal
                        strXML.append(" HORA = \"").append(rs.getString("VTA_HORA")).append("\""); //id maestro de la venta temporal
                        strXML.append(" CORREO = \"").append(rs.getString("VTA_CORREO_D")).append("\""); //id maestro de la venta temporal
                        strXML.append(" TELEFONO = \"").append(rs.getString("VTA_TELEFONO_D")).append("\""); //id maestro de la venta temporal
                        strXML.append(" RFC = \"").append(rs.getString("VTA_RFC_D")).append("\""); //id maestro de la venta temporal
                        strXML.append(" COFIDE_DUPLICIDAD_ID = \"").append(rs.getString("VTA_CT_ID")).append("\"");
                        strXML.append(" CT_CLAVE_DDBB = \"").append(util.Sustituye(rs.getString("DB_CT"))).append("\"");
                        strXML.append(" CT_RAZONSOCIAL = \"").append(util.Sustituye(rs.getString("RAZON_CT"))).append("\"");
                        strXML.append(" AGENTE = \"").append(util.Sustituye(rs.getString("AGENTE"))).append("\"");
                        strXML.append(" />");
                    }
                    rs.close();
                    strXML.append("</VtasDuplicadas>");
                } catch (SQLException ex) {
                    System.out.println("Error GetConsulta VentasTICKETS Duplicados " + ex.getLocalizedMessage());
                }
                strXML.toString();
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML);//Pintamos el resultado

            }//Fin ID 1

            //Consulta coincidencias del correo 
            if (strid.equals("2")) {
                String strTktId = request.getParameter("intTkt");
                String strCtId = ""; //clientes con conflicto de duplicidad
                StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
                strXML.append("<CtCoincidencia>");
                strSql = "SELECT CVD_CT_ID FROM cofide_ventas_duplicadas WHERE CVD_FAC_ID=" + strTktId;
                String strRfc = "";
                String strEmail = "";
                String strNombre = "";
                String strRazon = "";
                String strDB = "";
                String strAgente = "";
                try {
                    rs = oConn.runQuery(strSql, true);
                    while (rs.next()) {
                        strCtId = rs.getString("CVD_CT_ID");
                        String strSql2 = "select CT_ID,CT_NOMBRE,CT_EMAIL1,CT_RAZONSOCIAL,CT_CLAVE_DDBB,CT_RFC,(select nombre_usuario from usuarios where COFIDE_CODIGO = CT_CLAVE_DDBB limit 1)as strAgente "
                                + "from vta_cliente where CT_ID = " + strCtId + " and CT_ACTIVO = 1";
                        ResultSet rs2 = oConn.runQuery(strSql2, true);
                        while (rs2.next()) {
                            strRfc = rs2.getString("CT_RFC");
                            strEmail = rs2.getString("CT_EMAIL1");
                            strNombre = rs2.getString("CT_NOMBRE");
                            strDB = rs2.getString("CT_CLAVE_DDBB");
                            strRazon = rs2.getString("CT_RAZONSOCIAL");
                            if (rs2.getString("strAgente") != null) {
                                strAgente = rs2.getString("strAgente");
                            } else {
                                strAgente = "Sin Asignar";
                            }

                            strXML.append("<datos");
                            strXML.append(" CT_ID = \"").append(rs2.getString("CT_ID")).append("\"");
                            strXML.append(" CT_NOMBRE = \"").append(util.Sustituye(strNombre)).append("\"");
                            strXML.append(" CT_CLAVE_DDBB = \"").append(util.Sustituye(strDB)).append("\"");
                            strXML.append(" CT_RAZONSOCIAL = \"").append(util.Sustituye(strRazon)).append("\"");
                            strXML.append(" CT_EMAIL1 = \"").append(util.Sustituye(strEmail)).append("\"");
                            strXML.append(" CT_RFC = \"").append(util.Sustituye(strRfc)).append("\"");
                            strXML.append(" strAgente = \"").append(util.Sustituye(strAgente)).append("\"");
                            strXML.append(" />");
                        }
                        rs2.close();
                    }
                    rs.close();
                    strXML.append("</CtCoincidencia>");
                } catch (SQLException ex) {
                    System.out.println("Error GetConsulta coincidencias Clientes Duplicados " + ex.getLocalizedMessage());
                }
                strXML.toString();
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML);//Pintamos el resultado

            }//Fin ID 2

            //Confirma CT y Base de la venta, es a quien se le asignara esa venta
            if (strid.equals("3")) {
//                String strCtId = request.getParameter("CT_ID");
                String strCtBase = request.getParameter("CT_BASE");
                String stridVta = request.getParameter("ID_VTA"); //id venta temporal
                String strIDCTE = getCte(oConn, stridVta); //id del cliente                
                String strAgente = getUsuario(oConn, strCtBase);
                //actualiza
                strSql = "update cofide_venta_temporal set VTA_AGENTE = '" + strAgente + "', VTA_ESTATUS = '0' where VTA_ID_M = " + stridVta; //quitar el conflicto
                oConn.runQueryLMD(strSql);
                if (!oConn.isBolEsError()) {
                    strSql = "update vta_cliente set CT_CLAVE_DDBB = '" + strCtBase + "' where CT_ID = " + strIDCTE;
                    oConn.runQueryLMD(strSql);
                    if (!oConn.isBolEsError()) {
                        strRes = regActividad(oConn, strIDCTE, "2", varSesiones.getIntNoUser(), strCtBase);
                        strRes = TruncaAgenda(oConn, strIDCTE); //trunca la agenda del cliente
                        if (strRes.equals("OK")) {
                            //proceso de venta Id Venta = stridVta                            
//                            strRes = DoVta(oConn, stridVta);
                            String strTipoDoc = "";
                            String strIdVenta = "";

                            strSql = "select VTA_CT_ID, VTA_TIPO, FAC_ID  from cofide_venta_temporal where VTA_ID_M = " + stridVta;
                            rs = oConn.runQuery(strSql, true);

                            while (rs.next()) {
                                strTipoDoc = rs.getString("VTA_TIPO");
                                strIdVenta = rs.getString("FAC_ID");
                            }
                            rs.close();

                            //MANDAR CADENA PARA FACTURAR
                            if (strTipoDoc.equals("3")) {

                                strSql = "update vta_tickets set "
                                        + "TKT_FACTURAR = 1, "
                                        + "COFIDE_DUPLICIDAD = 0, "
                                        + "TKT_US_ALTA = " + strAgente + " "
                                        + "where TKT_ID = " + strIdVenta;
                                oConn.runQueryLMD(strSql);

                            } else {

                                strSql = "update vta_tickets set "
                                        + "COFIDE_DUPLICIDAD = 0, "
                                        + "TKT_US_ALTA = " + strAgente + " "
                                        + "where TKT_ID = " + strIdVenta;
                                oConn.runQueryLMD(strSql);
                            }

                            if (!oConn.isBolEsError()) {
                                strRes = "OK";
                            }

                            //mandar correo
                            SendConfirm(oConn, stridVta, getUsuario(oConn, strCtBase), "2", strIDCTE); //cambio de db
                        }
                    }
                }
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(strRes);//Pintamos el resultado
            }//Fin Id 3

            //Anula un TKT
            if (strid.equals("4")) {
                String strTKTID = request.getParameter("TKT_ID");
                strSql = "update cofide_venta_temporal set VTA_ESTATUS = 2, VTA_FECHA_ANUL = '" + fec.getAnioActual() + "', VTA_US_ANUL = " + varSesiones.getIntNoUser() + " where VTA_ID_M = " + strTKTID; //quitar el conflicto
                oConn.runQueryLMD(strSql);
                strRes = oConn.getStrMsgError();
                if (strRes.equals("")) {
                    strRes = "OK";
                }
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(strRes);//Pintamos el resultado
            }//Fin Id 4

            //Inactiva un cliente en la base
            if (strid.equals("5")) {
                String strCtId = request.getParameter("CT_ID");
                strSql = "update vta_cliente set CT_ACTIVO = 0 where CT_ID = " + strCtId;
                oConn.runQueryLMD(strSql);
                strRes = oConn.getStrMsgError();
                if (strRes.equals("")) {
                    strRes = "OK";
                }
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(strRes);//Pintamos el resultado
            }//Fin Id 5

            //Aprueba la venta
            if (strid.equals("6")) {

                String strTKTID = request.getParameter("TKT_ID");
                String strCtId = "";
                String strTipoDoc = "";
                String strIDVventa = "";
                strSql = "update cofide_venta_temporal set VTA_ESTATUS = 0 where VTA_ID_M = " + strTKTID; //quitar el conflicto
                oConn.runQueryLMD(strSql);
                if (!oConn.isBolEsError()) {
                    strSql = "select VTA_CT_ID, VTA_TIPO, FAC_ID  from cofide_venta_temporal where VTA_ID_M = " + strTKTID;
                    rs = oConn.runQuery(strSql, true);
                    while (rs.next()) {

                        strCtId = rs.getString("VTA_CT_ID");
                        strTipoDoc = rs.getString("VTA_TIPO");
                        strIDVventa = rs.getString("FAC_ID");

                    }
                    rs.close();
                    //proceso de venta Id Venta = strTKTID
//                    strRes = DoVta(oConn, strTKTID);
                    String strMetodoPago = "0"; //PPD=0 / PUE = 1

                    //MANDAR CADENA PARA FACTURAR
                    if (strTipoDoc.equals("3")) {

                        strSql = "update vta_tickets set TKT_FACTURAR = 1, COFIDE_DUPLICIDAD = 0 where TKT_ID = " + strIDVventa;
                        oConn.runQueryLMD(strSql);
                        strMetodoPago = getMetodoPago(oConn, strIDVventa);
//                        strRes = strMetodoPago;
                        if (strMetodoPago.equals("0")) { //PPD
                            System.out.println("ENTRO DIRECTO PARA HACER LA VENTA Y RECIBIO ID DE VENTA: " + strIDVventa);
                            request.getRequestDispatcher("/COFIDE_ValidaFactura.jsp?id=4&FAC_ID=" + strIDVventa).forward(request, response);
                        }

                    } else {

                        strSql = "update vta_tickets set COFIDE_DUPLICIDAD = 0 where TKT_ID = " + strIDVventa;
                        oConn.runQueryLMD(strSql);

                        System.out.println("ENTRO PARA HACER EL TICKET ID DE VENTA: " + strIDVventa);
                        request.getRequestDispatcher("/COFIDE_Programacionmails.jsp?ID=15&id_tkt=" + strIDVventa).forward(request, response);
                    }

                    if (!oConn.isBolEsError()) {

                        strRes = "OK";

                    }
                    //mandar correo de notificación
                    SendConfirm(oConn, strTKTID, "", "1", strCtId); //aprobada // manda el id del ticket o la factura
                    System.out.println("PRUEBA: \n\n respuesta de la venta: " + strRes);
                }
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(strRes);//Pintamos el resultado
            }//Fin Id 6
            if (strid.equals(("7"))) {
                String strOpc = request.getParameter("opcion");
                String strIdCte = request.getParameter("id_cte");
                String strDB = "";
                if (request.getParameter("db_usr") != null) {
                    strDB = request.getParameter("db_usr");
                }
                if (strOpc.equals("1")) {
                    strSql = "update vta_cliente set CT_CLAVE_DDBB = '" + strDB + "' where CT_ID = " + strIdCte;
                }
                if (strOpc.equals("2")) {
                    strSql = "update vta_cliente set CT_ACTIVO = 0 where CT_ID = " + strIdCte;
                }
                oConn.runQueryLMD(strSql);
                if (!oConn.isBolEsError()) {
                    strRes = "OK";
                    if (strRes.equals("OK")) {
                        strRes = TruncaAgenda(oConn, strIdCte); //trunca la agenda del cliente
                        strRes = regActividad(oConn, strIdCte, strOpc, varSesiones.getIntNoUser(), strDB);//si fue Ok la actualización, se guarda el historial de quien hizo la operación
                    }
                }
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(strRes);//Pintamos el resultado
            } //fin id 7
            if (strid.equals(("8"))) {
                String strId_fac = request.getParameter("idAnul");
                String strTipoDoc = request.getParameter("tipo_doc");
                String strTipoDOc_ = "T";
                if (strTipoDoc.equals("FACTURA")) {
                    strTipoDOc_ = "F";
                }
                boolean bolSolicitud = false; //no hay solicitudes a este ID /* strId_fac */
                strSql = "select FAC_ID from view_ventasglobales "
                        + "where CANCEL = 1 and FAC_ID = " + strId_fac + " and TIPO_DOC = '" + strTipoDOc_ + "'";
                rs = oConn.runQuery(strSql, true);
                while (rs.next()) {
                    bolSolicitud = true;
                }
                rs.close();
                if (bolSolicitud) {
                    strRes = "Esta factura ya se encuentra en proceso de cancelación";
                } else {
                    // SOLICITA SU CANCELACIÓN A CONTABILIDADD
                    if (strTipoDoc.equals("FACTURA")) {
                        strSql = "update vta_facturas set FAC_CANCEL = 1 where FAC_ID =  " + strId_fac; // facturas
                    } else {
                        //TICKET, SE VA A CONTABILIDAD PARA SU AUTORIZACIÓN
                        if (strTipoDoc.equals("TICKET")) {
                            strSql = "update vta_tickets set TKT_CANCEL = 1 where TKT_ID = " + strId_fac; // tickets
                        } else {
                            //RESERVACIÓN, SE CANCELA EN DIRECTO
                            strSql = "update vta_tickets set TKT_CANCEL = 1, TKT_ANULADA = 1, TKT_US_ANUL = " + varSesiones.getIntNoUser() + " "
                                    + "where TKT_ID= " + strId_fac; // reservación
                        }
                    }
                    oConn.runQueryLMD(strSql);
                    if (!oConn.isBolEsError()) {

                        if (strTipoDOc_.equals("F")) {
                            strSql = "update cofide_participantes set CP_ESTATUS = 2 where CP_FAC_ID = '" + strId_fac + "'";
                        } else {
                            strSql = "update cofide_participantes set CP_ESTATUS = 2 where CP_TKT_ID = '" + strId_fac + "'";
                        }
                        oConn.runQueryLMD(strSql);
                        if (!oConn.isBolEsError()) {
                            strRes = "OK";
                            //quita los puntos otorgados
//                            System.out.println("\n\n\n\n\n\n\n vamos a quitar puntos");
                            quitPtsCancel(oConn, strId_fac);
//                            System.out.println("\n\n\n\n\n\n\n vamos a quitar puntos");
                            tele.EsPromo(oConn, strId_fac); //si se cancelo una promoción, recuepra los puntos consumidos
                        }
                    }
                }
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(strRes);//Pintamos el resultado
            } //8
            if (strid.equals("9")) {
                StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
                strXML.append("<Cancelar>");
                String strEstat = request.getParameter("estatus");
                String strMes = request.getParameter("mes");
                String strAnio = request.getParameter("anio");
                int intTipoDoc = Integer.parseInt(request.getParameter("intTipoDoc"));
                int intCtId = Integer.parseInt(request.getParameter("intCtId"));
                String strFolio = request.getParameter("strFolio");
                int intIdVenta = Integer.parseInt(request.getParameter("intIdVenta"));
                String strComplete = "";
                String strCondicion = "";
                String strFecha = "";
                String strFechaAnul = "";
                if (strEstat.equals("0")) { //sin cancelar
                    strComplete = "where FAC_ANULADA = 0 ";
                    if (intCtId == 0) {
                        if (strFolio.equals("")) {
                            strComplete += "and left(FAC_FECHA, 6) = '" + strAnio + strMes + "' ";
                        }
                    }
                    strComplete += "and CANCEL = 1 and COFIDE_CARRITO = 0 ";
                } else { //canceladas
                    strComplete = "where FAC_ANULADA = 1 ";
                    if (intCtId == 0) {
                        if (strFolio.equals("")) {
                            strComplete += "and left(FAC_FECHA, 6) = '" + strAnio + strMes + "' ";
                        }
                    }
                    strComplete += "AND COFIDE_CARRITO = 0 AND FAC_US_MOD = 0 ";
                }

                if (intTipoDoc == 1) {
                    strCondicion += " and TIPO_DOC = 'F' ";
                }
                if (intTipoDoc == 2) {
                    strCondicion += " and TIPO_DOC = 'T' ";
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

                strSql = "SELECT *,"
                        + "ROUND(FAC_IMPORTE,2) AS Importe,"
                        + "ROUND(FAC_IMPUESTO1,2) AS Impuesto1,"
                        + "ROUND(FAC_TOTAL,2) AS Total,"
                        //                        + "if(FAC_FOLIO_C = '', 'TICKET', FAC_FOLIO_C) AS FAC_FOLIO_C_,"
                        + "if(TIPO_DOC = 'F', FAC_FOLIO_C, FAC_FOLIO) AS FAC_FOLIO_C_,"
                        + "IF(FAC_ANULADA = 1, 'ANULADA','PENDIENTE') AS FAC_ESTATUS,"
                        + "FAC_RAZONSOCIAL,"
                        + "IF(FAC_US_ANUL = 0, '-', (SELECT nombre_usuario from usuarios where id_usuarios = FAC_US_ANUL)) as FAC_USUARIO_ANUL,"
                        + "(select nombre_usuario from usuarios where id_usuarios = FAC_US_ALTA) as AGENTE "
                        + "FROM view_ventasglobales " + strComplete + strCondicion;
                rs = oConn.runQuery(strSql, true);
                while (rs.next()) {
                    if (!rs.getString("FAC_FECHA").equals("")) {
                        strFecha = fec.FormateaDDMMAAAA(rs.getString("FAC_FECHA"), "/");
                    }
                    strXML.append("<datos");
                    strXML.append(" FAC_ID = \"").append(rs.getString("FAC_ID")).append("\"");
                    strXML.append(" FAC_FECHA = \"").append(strFecha).append("\"");
                    strXML.append(" FAC_SERIE = \"").append(rs.getString("FAC_SERIE")).append("\"");
                    strXML.append(" FAC_FOLIO_C = \"").append(rs.getString("FAC_FOLIO_C_")).append("\"");
                    strXML.append(" FAC_FOLIO = \"").append(rs.getString("FAC_FOLIO")).append("\"");
                    strXML.append(" FAC_IMPORTE = \"").append(rs.getString("Importe")).append("\"");
                    strXML.append(" FAC_IVA = \"").append(rs.getString("Impuesto1")).append("\"");
                    strXML.append(" FAC_TOTAL = \"").append(rs.getString("Total")).append("\"");
                    strXML.append(" FAC_RAZONSOCIAL = \"").append(util.Sustituye(rs.getString("FAC_RAZONSOCIAL"))).append("\"");
                    strXML.append(" FAC_USUARIO = \"").append(util.Sustituye(rs.getString("FAC_USUARIO_ANUL"))).append("\"");
                    strXML.append(" FAC_EJECUTIVO = \"").append(util.Sustituye(rs.getString("AGENTE"))).append("\"");
                    strXML.append(" FAC_ESTATUS = \"").append(rs.getString("FAC_ESTATUS")).append("\"");
                    strXML.append(" FAC_HORA = \"").append(rs.getString("FAC_HORA")).append("\"");
//                    strXML.append(" FAC_EJECUTIVO = \"").append(rs.getString("AGENTE")).append("\"");
                    strXML.append(" FAC_TIPO = \"").append(rs.getString("TIPO_DOC")).append("\"");
                    strXML.append(" FAC_NOMPAGO = \"").append((rs.getString("FAC_NOMPAGO").equals("") ? "NO" : "SI")).append("\"");
                    strXML.append(" />");
                }
                rs.close();
                strXML.append("</Cancelar>");
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, true);//Definimos atributos para el XML
                out.println(strXML);//Pintamos el resultado
            } //9
            if (strid.equals("10")) {
                String strFacID = request.getParameter("id_fac");
                String strRazonSocial = "";
                String strCT_ID = "";
                String strFolio = "";

                strSql = "select CT_ID,FAC_RAZONSOCIAL,if(TIPO_DOC = 'F',FAC_FOLIO_C, FAC_FOLIO) as folio from view_ventasglobales where FAC_ID = " + strFacID + ";";
                rs = oConn.runQuery(strSql, true);
                while (rs.next()) {
                    strRazonSocial = rs.getString("FAC_RAZONSOCIAL");
                    strCT_ID = rs.getString("CT_ID");
                    strFolio = rs.getString("folio");
                }
                rs.close();
                System.out.println("raz+n social: " + strRazonSocial);
                System.out.println("id cte: " + strCT_ID);
                strSql = "select SMTP_US from usuarios where id_usuarios in (select FAC_US_ALTA from view_ventasglobales where FAC_ID = " + strFacID + ")";
                rs = oConn.runQuery(strSql, true);
                while (rs.next()) {
                    strRes = EnvioNotifCancel(oConn, strFolio.trim(), rs.getString("SMTP_US").trim(), strRazonSocial, strCT_ID); //envio correo al ejecutivo
                }
                rs.close();

                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(strRes);//Pintamos el resultado
            } //10
            if (strid.equals("11")) {
                String strFacID = request.getParameter("id_fac");
                strSql = "id_vta";
                rs = oConn.runQuery(strSql, true);
                while (rs.next()) {

                }
                rs.close();
                out.clearBuffer();//Limpiamos buffer
                atrJSP.atrJSP(request, response, true, false);//Definimos atributos para el XML
                out.println(strRes);//Pintamos el resultado
            } //10
        }
        oConn.close();
    } else {
        out.print("Sin Acceso");
    }
%>
<%!
    String strSql = "";
    ResultSet rs;
    String strRes = "";
    Fechas fec = new Fechas();

    public String regActividad(Conexion oConn, String strIdCte, String strOpcion, int intUsuario, String strDB_New) {
        String strDB = ""; //BASE VIEJA
        String strFecha = fec.getFechaActual();
        String strHora = fec.getHoraActual();

        this.strSql = "select CT_CLAVE_DDBB from vta_cliente where CT_ID = " + strIdCte;
        try {
            rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                strDB = rs.getString("CT_CLAVE_DDBB");
            }
            rs.close();
        } catch (SQLException sql) {
            System.out.println("Error al obtener información: " + sql);
        }
        this.strSql = "insert into cofide_logistica_actividad (id_usuarios, CLA_FECHA, CLA_HORA,CLA_OPCION,CT_ID, CLA_DB_OLD, CLA_DB_NEW) values "
                + "(" + intUsuario + ",'" + strFecha + "','" + strHora + "'," + strOpcion + "," + strIdCte + ",'" + strDB + "','" + strDB_New + "')";
        oConn.runQueryLMD(this.strSql);
        if (!oConn.isBolEsError()) {
            this.strRes = "OK";
        }
        return this.strRes;
    }

    public String TruncaAgenda(Conexion oConn, String strIdCte) {
        this.strSql = "update crm_eventos set EV_ESTADO = 0 where EV_CT_ID = " + strIdCte;
        oConn.runQueryLMD(this.strSql);
        if (!oConn.isBolEsError()) {
            this.strRes = "OK";
        }
        return this.strRes;
    }

    public String getUsuario(Conexion oConn, String strDB) {
        strSql = "select id_usuarios from usuarios where COFIDE_CODIGO = '" + strDB + "' and IS_TMK = 1 limit 1";
        try {
            rs = oConn.runQuery(this.strSql, true);
            while (rs.next()) {
                this.strRes = rs.getString("id_usuarios");
            }
            rs.close();
        } catch (SQLException sql) {
            System.out.println("Error al obtener el id del ejecutivo de la DB: " + sql);
        }
        return this.strRes;
    }

    public String getCte(Conexion oConn, String strIdVTA) {
        this.strSql = "select VTA_CT_ID from cofide_venta_temporal where VTA_ID_M = " + strIdVTA;
        try {
            rs = oConn.runQuery(this.strSql, true);
            while (rs.next()) {
                this.strRes = rs.getString("VTA_CT_ID");
            }
            rs.close();
        } catch (SQLException sql) {
            System.out.println("Error al obtener el id del ejecutivo de la Venta: " + sql);
        }
        return this.strRes;
    }

    public String EnvioNotifCancel(Conexion oConn, String strIDFAC, String strCorreo, String strFacRazonSocial, String strCT_ID) {
        this.strRes = "Hubó un problema al enviar la notifiación por correo.";
        Mail mail = new Mail();
        if (mail.isEmail(strCorreo)) {
            mail.setBolDepuracion(false);
            mail.getTemplate("NOT_CANCEL_FAC", oConn);
            String strMessage = mail.getMensaje().replace("%FACTURA%", strIDFAC).replace("%RAZON_SOCIAL%", strFacRazonSocial).replace("%ID_CLIENTE%", strCT_ID);
            mail.setMensaje(strMessage);
            mail.setDestino(strCorreo);
            boolean bol = mail.sendMail();
            if (bol) {
                this.strRes = "OK";
            }
        } else {
            this.strRes = "El correo del ejecutivo no es valido.";
        }
        return this.strRes;
    }
%>

<%!
    /**
     * ventas
     */
    /*Obtenemos las variables de sesion*/
    VariableSession varSesiones = new VariableSession();
    Telemarketing tele = new Telemarketing();
    COFIDE_Mail_cursos cmc = new COFIDE_Mail_cursos();
    private HttpServletRequest servletRequest;

    public String DoVta(Conexion oConn, String strIdVta) {

        String strIdCurso = "";
        String strCurso = "";
        String stridCte = "";
        String strPrecio = "";
        String strMetodo = "";
        String strIva = "";
        String strSubTotal = "";
        String strDesc = "";
        String strTotal = "";
        String strMaterial = "";
        String strTipoVta = "";
        String strComentario = "";
        String strComprobante = "";
        String strTipoCurso = "";
        String strPagado = "";
        String strAgente = "";
        String strFechaAlta = "";
        String strRazonSocial = "";
        String strRfc = "";
        String strVtaNvo = "";
        String strPrecioVta = "0.00";
        int intCantidad = 0; //cuantos participantes
        this.strSql = "select count(*) as cuantos from cofide_participantes_vta where CP_TKT_ID= " + strIdVta;
        try {
            rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                intCantidad = rs.getInt("cuantos");
            }
            rs.close();
        } catch (SQLException sql) {
            System.out.println("Error al obtener el conteo: " + sql.getMessage());
        }
        int intIdDatos = 0;
        String strCorreo = "";
        String strCC = "";
        String strCorreo2 = "";
        String strTelefono = "";
        String strCP = "";
        String strEstado = "";
        String rstrMunicipio = "";
        String strColonia = "";
        String strCalle = "";
        String strNumExt = "";
        String strRFC = "";
        int intMod = 1;
        strSql = "select *,"
                + "(select CT_RAZONSOCIAL from vta_cliente where CT_ID = VTA_CT_ID) as Razon,"
                + "(select CC_NOMBRE_CURSO from cofide_cursos where CC_CURSO_ID = VTA_ID_CURSO) as Curso,"
                + "(select SMTP_US from usuarios where id_usuarios = VTA_AGENTE) as VTA_CC_AGENTE "
                + "from cofide_venta_temporal where VTA_ID_M = " + strIdVta;
        try {
            rs = oConn.runQuery(this.strSql, true);
            while (rs.next()) {
                strIdCurso = rs.getString("VTA_ID_CURSO");
//                strCurso = rs.getString("VTA_CURSO");
                strCurso = rs.getString("Curso");
                stridCte = rs.getString("VTA_CT_ID");
                strPrecio = rs.getString("VTA_PRECIO");
                strMetodo = rs.getString("VTA_METODO");
                strIva = rs.getString("VTA_IVA");
                strSubTotal = rs.getString("VTA_SUBTOTAL");
                strDesc = rs.getString("VTA_DESC");
                strTotal = rs.getString("VTA_TOTAL");
                strMaterial = rs.getString("VTA_MATERIAL");
                strTipoVta = rs.getString("VTA_TIPO"); //1 factura, 2 ticket
                strComentario = rs.getString("VTA_COMENTARIO");
                strComprobante = rs.getString("VTA_COMPROBANTE");
                strTipoCurso = rs.getString("VTA_TIPOCURSO");
                strPagado = rs.getString("VTA_PAGADO");
                strAgente = rs.getString("VTA_AGENTE");
                strFechaAlta = rs.getString("VTA_FECHA_ALTA");
                intMod = rs.getInt("VTA_MOD");
                strVtaNvo = rs.getString("VTA_NVA");
                if (strTipoVta.equals("1")) {
                    strRazonSocial = rs.getString("VTA_RAZONSOCIAL");
                    strRFC = rs.getString("VTA_RFC");
                    strNumExt = rs.getString("VTA_NUMEROEXT");
                    strCalle = rs.getString("VTA_CALLE");
                    strColonia = rs.getString("VTA_COLONIA");
                    rstrMunicipio = rs.getString("VTA_MUNICIPIO");
                    strEstado = rs.getString("VTA_ESTADO");
                    strCP = rs.getString("VTA_CP");
                    strTelefono = rs.getString("VTA_TELEFONO");
                    strCorreo2 = rs.getString("VTA_CC_AGENTE");
                    strCorreo = rs.getString("VTA_CORREO");
                    strCC = rs.getString("VTA_CC");
//                    System.out.println("\n\n cirreo " + strCorreo);

                    System.out.println("############################## RFC: " + strRFC + " ###################################################");
                } else {
                    strRazonSocial = rs.getString("Razon");
//                    strCorreo = rs.getString("");
                }
            }
            rs.close();
        } catch (SQLException sql) {
            System.out.println("Error al obtener la información de la venta msg1: " + sql.getMessage());
//            System.out.println("Error al obtener la información de la venta msg2: " + sql.getSQLState());
        }
//Recuperamos paths de web.xml
        String strPathXML = this.getServletContext().getInitParameter("PathXml");
        String strfolio_GLOBAL = this.getServletContext().getInitParameter("folio_GLOBAL");
        String strmod_Inventarios = this.getServletContext().getInitParameter("mod_Inventarios");
        String strSist_Costos = this.getServletContext().getInitParameter("SistemaCostos");
        String strClienteUniversal = this.getServletContext().getInitParameter("ClienteUniversal");
        String strPathPrivateKeys = this.getServletContext().getInitParameter("PathPrivateKey");
        String strPathFonts = this.getServletContext().getRealPath("/") + System.getProperty("file.separator") + "fonts";
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
        int intCT_ID = Integer.valueOf(stridCte);
        String strCT_NO_CLIENTE = stridCte;
        String strFacSerie = "C";
        //Guardamos datos de facturacion
        if (strTipoVta.equals("1")) { // si es factura
            vta_cliente_facturacion datFac = new vta_cliente_facturacion();
            datFac.setFieldString("DFA_RAZONSOCIAL", strRazonSocial);
            datFac.setFieldString("DFA_RFC", strRFC);
            datFac.setFieldString("DFA_NUMERO", strNumExt);
            datFac.setFieldString("DFA_CALLE", strCalle);
            datFac.setFieldString("DFA_COLONIA", strColonia);
            datFac.setFieldString("DFA_MUNICIPIO", rstrMunicipio);
            datFac.setFieldString("DFA_ESTADO", strEstado);
            datFac.setFieldString("DFA_CP", strCP);
            datFac.setFieldString("DFA_TELEFONO", strTelefono);
            datFac.setFieldString("DFA_EMAIL", strCorreo);
            datFac.setFieldString("DFA_EMAI2", strCC);
            datFac.setFieldInt("CT_ID", intCT_ID);
            datFac.setBolGetAutonumeric(true);
            //Guardamos
            String strRes1 = datFac.Agrega(oConn);
            if (strRes1.equals("OK")) {
                intIdDatos = Integer.valueOf(datFac.getValorKey());
            }
        }
        //Instanciamos el objeto que generara la venta
        Ticket ticket = new Ticket(oConn, varSesiones, servletRequest);
        ticket.setStrPATHKeys(strPathPrivateKeys);
        ticket.setStrPATHXml(strPathXML);
        ticket.setStrPATHFonts(strPathFonts);

        ticket.setBolAfectaInv(false);
        //Recibimos parametros
        String strPrefijoMaster = "TKT";
        String strPrefijoDeta = "TKTD";
        String strTipoVtaNom = Ticket.TICKET;
        //Recuperamos el tipo de venta 1:FACTURA 2:TICKET 3:PEDIDO
        if (strTipoVta == null) { //tkt
            strTipoVta = "2";
        }
        if (strTipoVta.equals("1")) {
            strPrefijoMaster = "FAC";
            strPrefijoDeta = "FACD";
            strTipoVtaNom = Ticket.FACTURA;
            ticket.initMyPass(this.getServletContext());
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
        //Edicion de pedidos
        ticket.getDocument().setFieldInt("SC_ID", 1);
        ticket.getDocument().setFieldInt("CT_ID", intCT_ID);
        if (strTipoVta.equals("3")) {
            int intReservacion = 1;
            ticket.getDocument().setFieldInt(strPrefijoMaster + "_ES_RESERVACION", intReservacion);
        }
        ticket.getDocument().setFieldInt(strPrefijoMaster + "_INBOUND", 0);
        ticket.getDocument().setFieldInt(strPrefijoMaster + "_MONEDA", 1);

        //Clave de vendedor
        int intVE_ID = 0;
        //Tarifas de IVA
        int intTI_ID = 1;
        int intTI_ID2 = 0;
        int intTI_ID3 = 0;
        //Tipo de comprobante
        int intFAC_TIPOCOMP = 0;
        //Asignamos los valores al objeto
        ticket.getDocument().setFieldInt("VE_ID", intVE_ID);
        ticket.getDocument().setFieldInt("TI_ID", intTI_ID);
        ticket.getDocument().setFieldInt("TI_ID2", intTI_ID2);
        ticket.getDocument().setFieldInt("TI_ID3", intTI_ID3);
        ticket.setIntFAC_TIPOCOMP(intFAC_TIPOCOMP);
        ticket.getDocument().setFieldInt(strPrefijoMaster + "_ESSERV", 1);
        ticket.getDocument().setFieldString(strPrefijoMaster + "_FECHA", fec.getFechaActual());

        String strFechaCobro = fec.getFechaActual();
        ticket.getDocument().setFieldString(strPrefijoMaster + "_FECHA_COBRO", strFechaCobro);

        ticket.getDocument().setFieldString(strPrefijoMaster + "_FOLIO", "");
        ticket.getDocument().setFieldString(strPrefijoMaster + "_FOLIO_C", "C");
        ticket.getDocument().setFieldString("CC_CURSO_ID", strIdCurso);
        String strNotas = strComentario;;
        ticket.getDocument().setFieldString(strPrefijoMaster + "_NOTAS", strNotas);
        if (!strFacSerie.equals(null)) {
            ticket.getDocument().setFieldString(strPrefijoMaster + "_SERIE", strFacSerie); //numero de serie
        }
        ticket.getDocument().setFieldString("COFIDE_NVO", strVtaNvo); //para ver si es nuevo cliente o no y ligarlo a la venta
        ticket.getDocument().setFieldString(strPrefijoMaster + "_TIPO_CURSO", strTipoCurso); //para ver si es nuevo cliente o no y ligarlo a la venta
        ticket.getDocument().setFieldString(strPrefijoMaster + "_REFERENCIA", "");
        ticket.getDocument().setFieldString(strPrefijoMaster + "_CONDPAGO", "");
        ticket.getDocument().setFieldString(strPrefijoMaster + "_METODODEPAGO", strMetodo);
        ticket.getDocument().setFieldString(strPrefijoMaster + "_NUMCUENTA", "");
        ticket.getDocument().setFieldString(strPrefijoMaster + "_NOMPAGO", strComprobante);
        ticket.getDocument().setFieldString(strPrefijoMaster + "_FORMADEPAGO", "EN UNA SOLA EXHIBICION");
        ticket.getDocument().setFieldDouble(strPrefijoMaster + "_IMPORTE", Double.valueOf(strPrecio));
        ticket.getDocument().setFieldDouble(strPrefijoMaster + "_IMPUESTO1", Double.valueOf(strIva));
        ticket.getDocument().setFieldDouble(strPrefijoMaster + "_IMPUESTO2", Double.valueOf(0));
        ticket.getDocument().setFieldDouble(strPrefijoMaster + "_IMPUESTO3", Double.valueOf(0));
        ticket.getDocument().setFieldDouble(strPrefijoMaster + "_TOTAL", Double.valueOf(strTotal));
        strPrecioVta = String.valueOf(strTotal);
        ticket.getDocument().setFieldDouble(strPrefijoMaster + "_TASA1", Double.valueOf(16));
        ticket.getDocument().setFieldDouble(strPrefijoMaster + "_TASA2", Double.valueOf(0));
        ticket.getDocument().setFieldDouble(strPrefijoMaster + "_TASA3", Double.valueOf(0));
        ticket.getDocument().setFieldDouble(strPrefijoMaster + "_TASAPESO", 1);
        if (!strComprobante.equals("")) {
            ticket.getDocument().setFieldDouble(strPrefijoMaster + "_COFIDE_PAGADO", 1);
        }

        //Si no hay moneda seleccionada que ponga tasa 1        
        ticket.getDocument().setFieldDouble(strPrefijoMaster + "_TASAPESO", 1);
        ticket.getDocument().setFieldInt(strPrefijoMaster + "_DIASCREDITO", 1);

        //Cliente final
        int intDfaId = intIdDatos;
        if (intDfaId != 0l) {
            try {
                ticket.getDocument().setFieldInt("DFA_ID", intDfaId);
            } catch (NumberFormatException ex) {
                System.out.println("Ventas CT_CLIENTEFINAL " + ex.getMessage());
            }
        }
        /*Campos flete, transportista , num guia VENTAS*/
        ticket.getDocument().setFieldDouble(strPrefijoMaster + "_RETIVA", Double.valueOf(0));
        ticket.getDocument().setFieldDouble(strPrefijoMaster + "_NETO", Double.valueOf(0));
        //Recibimos datos de los items o partidas
        int intCount = 1;
        for (int i = 1; i <= intCount; i++) {
            TableMaster deta = null;
            if (strTipoVtaNom.equals(Ticket.TICKET)) {
                deta = new vta_ticketsdeta();
            }
            if (strTipoVtaNom.equals(Ticket.FACTURA)) {
                deta = new vta_facturadeta();
            }
            deta.setFieldInt("SC_ID", 1);
            deta.setFieldInt("PR_ID", Integer.valueOf(strIdCurso));
            deta.setFieldInt(strPrefijoDeta + "_EXENTO1", 0);
            deta.setFieldInt(strPrefijoDeta + "_EXENTO2", 0);
            deta.setFieldInt(strPrefijoDeta + "_EXENTO3", 0);
            deta.setFieldInt(strPrefijoDeta + "_ESREGALO", 0);
            deta.setFieldString(strPrefijoDeta + "_CVE", "...");
            String strDescripcion = strCurso;
            deta.setFieldString(strPrefijoDeta + "_UNIDAD_MEDIDA", "CURSO");
            deta.setFieldString(strPrefijoDeta + "_DESCRIPCION", strDescripcion);
            deta.setFieldString(strPrefijoDeta + "_NOSERIE", "");
            deta.setFieldDouble(strPrefijoDeta + "_IMPORTE", Double.valueOf(strPrecio));
            deta.setFieldDouble(strPrefijoDeta + "_CANTIDAD", Double.valueOf(intCantidad));
            deta.setFieldDouble(strPrefijoDeta + "_TASAIVA1", Double.valueOf(16));
            deta.setFieldDouble(strPrefijoDeta + "_TASAIVA2", Double.valueOf(0));
            deta.setFieldDouble(strPrefijoDeta + "_TASAIVA3", Double.valueOf(0));
            deta.setFieldDouble(strPrefijoDeta + "_IMPUESTO1", Double.valueOf(strIva));
            deta.setFieldDouble(strPrefijoDeta + "_IMPUESTO2", Double.valueOf(0));
            deta.setFieldDouble(strPrefijoDeta + "_IMPUESTO3", Double.valueOf(0));
            deta.setFieldDouble(strPrefijoDeta + "_IMPORTEREAL", Double.valueOf(strPrecio));
            deta.setFieldDouble(strPrefijoDeta + "_PRECIO", Double.valueOf(strPrecio));
            deta.setFieldDouble(strPrefijoDeta + "_DESCUENTO", Double.valueOf(strDesc));
            deta.setFieldInt(strPrefijoDeta + "_TIPO_CURSO", Integer.parseInt(strTipoCurso));
            deta.setFieldDouble(strPrefijoDeta + "_PRECREAL", Double.valueOf(strPrecio));
            //Retencion de ISR           
            deta.setFieldDouble(strPrefijoDeta + "_RET_ISR", 0);
            deta.setFieldDouble(strPrefijoDeta + "_RET_IVA", 0);
            deta.setFieldDouble(strPrefijoDeta + "_RET_FLETE", 0);
            //Solo aplica si es ticket o factura
            if (strTipoVtaNom.equals(Ticket.TICKET) || strTipoVtaNom.equals(Ticket.FACTURA)) {
                deta.setFieldInt(strPrefijoDeta + "_ESDEVO", Integer.valueOf(0));
            }
            deta.setFieldInt(strPrefijoDeta + "_PRECFIJO", 0);
            deta.setFieldInt(strPrefijoDeta + "_ESREGALO", 0);
            deta.setFieldString(strPrefijoDeta + "_COMENTARIO", strDescripcion);
            ticket.AddDetalle(deta);
        }
        //Inicializamos objeto
        ticket.setBolSendMailMasivo(false);
        //Validamos si es un pedido que se esta editando para solo modificar el pedido anterior 
        if (strTipoVta.equals("3") && ticket.getDocument().getFieldInt("PD_ID") != 0) {
            ticket.doTrxMod();
        } else {
            ticket.Init();
            //Generamos transaccion
            ticket.doTrx();
        }
        if (ticket.getStrResultLast().equals("OK")) {
            this.strRes = "OK." + ticket.getDocument().getValorKey();
            int intIdVta = Integer.valueOf(ticket.getDocument().getValorKey());
            //Posicion inicial para el numero de pagina
            String strPosX = "";
            String strTitle = "";
            strPosX = this.getServletContext().getInitParameter("PosXTitle");
            strTitle = this.getServletContext().getInitParameter("TitleApp");
//información del correo
            String[] strLstUser = new String[7];
            strSql = "select nombre_usuario, SMTP, PORT, SMTP_US, SMTP_PASS, SMTP_USATLS, SMTP_USASTLS from usuarios where id_usuarios = " + strAgente;
            try {
                rs = oConn.runQuery(strSql, true);
                while (rs.next()) {
                    strLstUser[0] = rs.getString("nombre_usuario");
                    strLstUser[1] = rs.getString("SMTP");
                    strLstUser[2] = rs.getString("PORT");
                    strLstUser[3] = rs.getString("SMTP_US");
                    strLstUser[4] = rs.getString("SMTP_PASS");
                    strLstUser[5] = rs.getString("SMTP_USATLS");
                    strLstUser[6] = rs.getString("SMTP_USASTLS");
                }
                rs.close();
            } catch (SQLException sql) {
                System.out.println("Error al obtener: " + sql.getMessage());
            }
//información del correo
            //strTipoVta = 2 es un ticket o cotizacion
            //strTipoVta = 1 es una factura                          
            int intTipoVta = 0;
            int intIdCurso = 0;
            int intAgente = 0;
            intTipoVta = Integer.parseInt(strTipoVta);
            intIdCurso = Integer.parseInt(strIdCurso);
            intAgente = Integer.parseInt(strAgente);
            String strParticipantes = "";
//            System.out.println("PRUEBA: \n\n tipo de venta: " + strTipoVta + " if(fac) \n\n");
            if (strTipoVta.equals("1")) {
                System.out.println("#################### ENVIA CORREO SI ES FACTURA #####################");
                enviaMailMasivo(oConn, strPosX, strTitle, ticket.getDocument().getValorKey(), varSesiones, strTipoVta, strCorreo, strCC, strLstUser);
            }
            //agregar a la funcion de mail masivo el campo de tipodoc
            //Solo aplica en pedidos, tickets y cotizaciones
            if (strTipoVta.equals("2") || strTipoVta.equals("3") || strTipoVta.equals("4")) {
                System.out.println("PRUEBA: \n\n tipo de venta: ticket \n\n");
                //Enviamos por correo el formato en caso de ser diferente de factura
                String strTipoDoc = "";
                strTipoDoc = "TICKET";
                try {
                    System.out.println("#################### ENVIA CORREO SI ES TICKET #####################");
                    System.out.println("PRUEBA: respuesta del tkt \n" + ticket.generaMail(oConn, Integer.valueOf(ticket.getDocument().getValorKey()), strTipoDoc, strPathXML, strPathFonts, strCorreo2)); //correo al agente
                } catch (NumberFormatException ex) {
                }
            }
            //Guardamos los participantes del curso
            int intIdTicket = 0;
            int intIdFactura = 0;
            String strDoc = "T";
            String strDoc_ = "TKT";
            System.out.println("############### TIPO DE VENTA: " + strTipoVta);
            if (strTipoVta.equals("1")) { //id de venta
                intIdFactura = intIdVta;
                strDoc = "F";
                strDoc_ = "FAC";
                this.strSql = "update vta_facturas set FAC_US_ALTA = '" + strAgente + "' where FAC_ID = " + intIdVta;
                oConn.runQueryLMD(this.strSql);
            } else {
                intIdTicket = intIdVta;
                strDoc = "T";
                strDoc_ = "TKT";
                this.strSql = "update vta_tickets set TKT_US_ALTA = '" + strAgente + "' where TKT_ID = " + intIdVta;
                oConn.runQueryLMD(this.strSql);
            }
//            this.strSql = "select *, CONCAT(CP_TITULO,' ', CP_NOMBRE,' ',CP_APPAT,' ', CP_APMAT) as participante from cofide_participantes_vta where CP_TKT_ID = " + strIdVta;
            this.strSql = "select *, REPLACE(GROUP_CONCAT(CONCAT_WS(',',CONCAT(CP_TITULO, ' ' ,CP_NOMBRE, ' ' , CP_APPAT, ' ' ,CP_APMAT))),',','<br>') as participante from cofide_participantes_vta where CP_TKT_ID = " + strIdVta;
            try {
                rs = oConn.runQuery(this.strSql, true);
                System.out.println("############## INICIA EL GUARDADO DE PARTICIPANTES ################");
                while (rs.next()) {
                    strParticipantes = rs.getString("participante");
                    String strSqlInsert = "INSERT INTO cofide_participantes (CP_ID_CURSO, CP_NOMBRE, CP_APPAT, CP_APMAT, CP_FAC_ID, CP_TKT_ID, "
                            + "CP_TITULO, CCO_NOSOCIO, CP_ASCOC, CP_CORREO, CP_COMENT, CP_ASISTENCIA, CP_PAGO, CP_OBSERVACIONES, CP_USUARIO_ALTA, CT_ID, CP_MATERIAL_IMPRESO, "
                            + "CP_TIPO_CURSO, CP_NOM_COMPROBANTE, CP_ESTATUS, CONTACTO_ID) VALUES "
                            + "('" + strIdCurso + "', '" + rs.getString("CP_NOMBRE") + "', '" + rs.getString("CP_APPAT") + "', "
                            + "'" + rs.getString("CP_APMAT") + "', '" + intIdFactura + "', '" + intIdTicket + "', '" + rs.getString("CP_TITULO") + "', '" + rs.getString("CCO_NOSOCIO") + "', "
                            + "'" + rs.getString("CP_ASCOC") + "', '" + rs.getString("CP_CORREO") + "', '" + rs.getString("CP_COMENT") + "', "
                            + "'1', '" + rs.getString("CP_PAGO") + "', '" + rs.getString("CP_OBSERVACIONES") + "', '" + rs.getString("CP_USUARIO_ALTA") + "', '" + rs.getString("CT_ID") + "', "
                            + "'" + rs.getString("CP_MATERIAL_IMPRESO") + "', '" + rs.getString("CP_TIPO_CURSO") + "', '" + rs.getString("CP_NOM_COMPROBANTE") + "', '1','" + rs.getString("CONTACTO_ID") + "')";
                    oConn.runQueryLMD(strSqlInsert);
                    try {
                        //envio de reservación
                        System.out.println("correo: mail reservacion. \n\n" + rs.getString("CP_CORREO") + "\n\n");
                        System.out.println("respuesta del mail reservacion \n" + cmc.MailReservacion(oConn, rs.getString("CP_CORREO"), strParticipantes, strRazonSocial, intIdCurso, intAgente, Integer.parseInt(strTipoCurso), strPrecioVta));
//                        System.out.println("if de pagado");
                        if (strPagado.equals("1")) { //pagado
                            System.out.println("entro al if de pagado OK");
                            if (strTipoCurso.equals("1")) {
                                System.out.println("if de si es presencial");
                                // si es presencual, manda material de descarga
                                cmc.MailDescargaMaterial(oConn, rs.getString("CP_CORREO"), strParticipantes, strRazonSocial, intIdCurso, intAgente, Integer.parseInt(strTipoCurso), strPrecioVta);
                            } else {
                                System.out.println(" es en linea");
                                // si es online, manda el acceso web
                                System.out.println("PRUEBA: \n\n manda material y acceso online \n\n");
                                String[] strInfUser = GetUser(oConn, rs.getString("CP_CORREO"), rs.getString("CT_ID"), rs.getString("CP_NOMBRE"), rs.getString("CP_APPAT") + " " + rs.getString("CP_APMAT"), rs.getString("CP_TITULO"));
                                System.out.println("PRUEBA: \n\n genero usuario web \n\n");
                                cmc.setStrUsuario(strInfUser[1]); //asigna el usuario
                                cmc.setStrContraseña(strInfUser[0]); //asigna la contraseña
                                System.out.println("PRUEBA: \n\n accesos: \n " + strInfUser[1] + " y su pass:  \n " + strInfUser[0] + "PRUEBA: \n id usuario web: " + strInfUser[2] + "\n");
                                System.out.println("PRUEBA: \n\n va a mandar los accesos \n\n");
                                cmc.MailAccesoOnline(oConn, rs.getString("CP_CORREO"), strParticipantes, strRazonSocial, intIdCurso, intAgente, Integer.parseInt(strTipoCurso), strPrecioVta);
                                System.out.println("PRUEBA: \n\n termino de enviar material y acceso online \n\n");
                                System.out.println("PRUEBA: \n\n agrega el curso a la web, para los accesos \n\n");
                                int intRes = Integer.parseInt(this.strRes.replace("OK.", ""));
                                System.out.println("PRUEBA: \n\n respuesta " + intRes + "\n\n");
//PENDIENTE
//                                cmc.VtaWeb(oConn, intRes, strInfUser[2], Integer.parseInt(strTipoCurso), intMod, strDoc);
                                System.out.println("#####################PRUEBA: \n\n tipo de curso a guardar en la web " + strTipoCurso + "\n\n##############################");
                            }
                        }
                    } catch (ParseException ex) {
                        System.out.println("ERRROR: al enviar la reservación y el material " + ex.getMessage());
                    }
                }
                rs.close();
            } catch (SQLException sql) {
                System.out.println("Error al guardar los participantes: " + sql.getMessage());
            }
            //Afectamos otros obo
        } else {
            this.strRes = ticket.getStrResultLast();
        }
        System.out.println("PRUEBA: \n\n respuesta de la venta: " + this.strRes + "  \n\n");
        return this.strRes;
    }

    //envio documento tkt o fac
    public void enviaMailMasivo(Conexion oConn, String strPosX, String strTitle, String strFacId,
            VariableSession varSesiones, String strTipoVta, String strCorreovta1, String strCorreovta2, String[] lstinfUsr) {
        //Respuesta del servicio
        boolean bolEsNC = false;
        //Cargamos datos del mail
        //Recuperamos paths de web.xml
        String strPathXML = this.getServletContext().getInitParameter("PathXml");
        String strPathFonts = this.getServletContext().getRealPath("/") + System.getProperty("file.separator") + "fonts";

        //Recibimos parametros
        String strlstFAC_ID = "";
        String strVIEW_COPIA = "";
        String strVIEW_ASUNTO = "";
        String strVIEW_MAIL = "";//mail del cliente
        String strVIEW_CC = "1";
        String strMailCte = "";
        String strMailCte2 = "";
        int intVIEW_Templete = 1;

        if (strVIEW_COPIA == null) {
            strVIEW_COPIA = "";
        }
        if (strVIEW_CC == null) {
            strVIEW_CC = "1";
        }

        String strNombre = lstinfUsr[0];
        String strUsuario = lstinfUsr[3];
        String strPassword = lstinfUsr[4];
        String strDominio = lstinfUsr[1];
        String strPuerto = lstinfUsr[2];
        int intSSL = Integer.parseInt(lstinfUsr[5]);
        int intSSSL = Integer.parseInt(lstinfUsr[6]
        );

        String strSmtpServer = strDominio;
        String strSmtpUser = strUsuario;
        String strSmtpPassword = strPassword;
        String strSmtpPort = strPuerto;
        int intSmtpUsaSSL = intSSL;
        int intSmtpUsaSSSL = intSSSL;
        //Validamos que todos los datos del mail esten completos
//        System.out.println("valida información del correo");
        String strResp = "OK";
        if (!strSmtpServer.equals("") && !strSmtpUser.equals("") && !strSmtpPassword.equals("") && !strSmtpPort.equals("")) {
//            System.out.println("el correo esta OK");
            //Recorremos las operaciones seleccionado
            //Recuperamos la factura
            int intFAC_ID = 0;
            try {
                intFAC_ID = Integer.valueOf(strFacId);
            } catch (NumberFormatException ex) {
            }
            //Resultado

            String strCode = "OK";
            boolean bolIsOkFacPDF = false;
            //Obtenemos el folio
            String strNumFolio = "";
            int intEMP_TIPOCOMP = 0;
            int intEMP_ID = 0;
            int intCT_ID = 0;
            String strFAC_NOMFORMATO = "";
            String strFAC_RAZONSOCIAL = "";
            String strFAC_FECHA = "";
            int intFAC_ES_CFD = 0;
            int intFAC_ES_CBB = 0;
            String strSql = "";
            // si strTipoVta == 1 es Factura
//            System.out.println("consultas para el tipo de venta");
            if (strTipoVta.equals("1")) {
                strSql = "select FAC_ID,CT_ID,FAC_FOLIO,FAC_TIPOCOMP,FAC_NOMFORMATO,EMP_ID,FAC_RAZONSOCIAL,FAC_FECHA,FAC_ES_CFD,FAC_ES_CBB from vta_facturas where FAC_ID = " + intFAC_ID;
            }
            // si strTipoVta == 2 es Ticket
            if (strTipoVta.equals("2") || strTipoVta.equals("3")) {
                strSql = "select TKT_ID,CT_ID,TKT_FOLIO,TKT_TIPOCOMP, TKT_NOMFORMATO,EMP_ID,TKT_RAZONSOCIAL,TKT_FECHA from vta_tickets where TKT_ID = " + intFAC_ID;
            }
            //Recuperamos el numero de folio que queremos imprimir
            if (bolEsNC) {
                strSql = "select CT_ID,NC_FOLIO,NC_TIPOCOMP,NC_NOMFORMATO,EMP_ID,NC_RAZONSOCIAL,NC_FECHA,NC_ES_CFD,NC_ES_CBB from vta_ncredito where NC_ID = " + intFAC_ID;
            }
            try {
                ResultSet rs = oConn.runQuery(strSql, true);
                //Buscamos el nombre del archivo
                while (rs.next()) {
                    //System.out.println("VENTA: " + strTipoVta);
                    if (strTipoVta.equals("1")) { //fac
                        //System.out.println("ENTRO FACTURA ");
                        //if (!bolEsNC) {
                        strNumFolio = rs.getString("FAC_FOLIO");
                        intEMP_TIPOCOMP = rs.getInt("FAC_TIPOCOMP");
                        intEMP_ID = rs.getInt("EMP_ID");
                        intCT_ID = rs.getInt("CT_ID");
                        strFAC_NOMFORMATO = rs.getString("FAC_NOMFORMATO");
                        strFAC_RAZONSOCIAL = rs.getString("FAC_RAZONSOCIAL");
                        strFAC_FECHA = rs.getString("FAC_FECHA");
                        intFAC_ES_CFD = rs.getInt("FAC_ES_CFD");
                        intFAC_ES_CBB = rs.getInt("FAC_ES_CBB");
                        //}
                    } else {
                        if (strTipoVta.equals("2")) { //tkt
                            strNumFolio = rs.getString("TKT_FOLIO");
                            intEMP_TIPOCOMP = rs.getInt("TKT_TIPOCOMP");
                            intEMP_ID = rs.getInt("EMP_ID");
                            intCT_ID = rs.getInt("CT_ID");
                            strFAC_NOMFORMATO = "";
                            strFAC_RAZONSOCIAL = rs.getString("TKT_RAZONSOCIAL");
                            strFAC_FECHA = rs.getString("TKT_FECHA");
                            intFAC_ES_CFD = 0;
                            intFAC_ES_CBB = 0;
                        } else {
                            strNumFolio = rs.getString("NC_FOLIO");
                            intEMP_TIPOCOMP = rs.getInt("NC_TIPOCOMP");
                            intEMP_ID = rs.getInt("EMP_ID");
                            intCT_ID = rs.getInt("CT_ID");
                            strFAC_NOMFORMATO = rs.getString("NC_NOMFORMATO");
                            strFAC_RAZONSOCIAL = rs.getString("NC_RAZONSOCIAL").trim();
                            strFAC_FECHA = rs.getString("NC_FECHA");
                            intFAC_ES_CFD = rs.getInt("NC_ES_CFD");
                            intFAC_ES_CBB = rs.getInt("NC_ES_CBB");
                        }
                    }
                }
                rs.close();
                //destinatarios proporcionados en la venta
                System.out.println("PRUEBA \n\n correo al que se le envia la factura " + strCorreovta1 + " con copia a su ejecutovo " + strCorreovta2);
                strMailCte = strCorreovta1;
                strMailCte2 = strCorreovta2;
                //destinatarios proporcionados en la venta
            } catch (SQLException ex) {
                System.out.println(". . . ..  Error al enviar mail 1 " + ex.getMessage());
            }
            //agregamos el correo del ejecutivo como copia
            strVIEW_COPIA = strUsuario;
            //Si el cliente tiene mail lo enviamos
            if (!strMailCte.equals("") || !strVIEW_COPIA.equals("")) {
                //Mail personalizado
                String strMailOK = new String(strVIEW_MAIL);
                strMailOK = strMailOK.replace("[FOLIO]", strNumFolio);
                strMailOK = strMailOK.replace("[RAZONSOCIAL]", strFAC_RAZONSOCIAL);
                strMailOK = strMailOK.replace("[FECHA]", strFAC_FECHA);
                strMailOK = strMailOK.replace("[MES]", strFAC_FECHA.substring(5, 6));
                String strMailASOK = new String(strVIEW_ASUNTO);
                strMailASOK = strMailASOK.replace("[FOLIO]", strNumFolio);
                strMailASOK = strMailASOK.replace("[RAZONSOCIAL]", strFAC_RAZONSOCIAL);
                strMailASOK = strMailASOK.replace("[FECHA]", strFAC_FECHA);
                strMailASOK = strMailASOK.replace("[MES]", strFAC_FECHA.substring(5, 6));
                //Lista de correo alos que se los enviaremos
                String strEmailSend = "";
                if (!strMailCte.equals("")) { // el correo del cliente tiene valor, se agrega al destinatario
                    strEmailSend = strMailCte;
                }
                if (!strMailCte2.equals("")) { // si tiene copia de correo, se agrega una coma y el correo 2
                    if (!strMailCte.equals("")) {
                        strEmailSend += ","; //se agrega la coma 
                    }
                    strEmailSend += strMailCte2; // se agrega el correo para la copia
                }
                if (!strVIEW_COPIA.equals("")) { // si tiene valores el correo del ejecutivo, lo agrega como copia
                    if (!strMailCte.equals("") || !strMailCte2.equals("")) {
                        strEmailSend += ",";
                    }
                    strEmailSend += strVIEW_COPIA; // destinatarios completos
                }
                //Buscamos si la empresa usa CBB
                int intEMP_USACODBARR = 0;
                int intEMP_CFD_CFDI = 0;
                strSql = "select EMP_USACODBARR,EMP_CFD_CFDI from vta_empresas where EMP_ID = " + intEMP_ID;
                try {
                    ResultSet rs = oConn.runQuery(strSql, true);
                    //Buscamos el nombre del archivo
                    while (rs.next()) {
                        intEMP_USACODBARR = rs.getInt("EMP_USACODBARR");
                        intEMP_CFD_CFDI = rs.getInt("EMP_CFD_CFDI");
                    }
                    rs.close();
                } catch (SQLException ex) {
                    System.out.println("Error: " + ex.getMessage());
                }
                ERP_MapeoFormato mapeo = new ERP_MapeoFormato(intEMP_TIPOCOMP);
                String strNomFormato = mapeo.getStrNomFormato();
                if (intEMP_USACODBARR == 1) {
                    strNomFormato += "_CBB";
                }
                if (intEMP_CFD_CFDI == 1 && intFAC_ES_CFD == 0 && intFAC_ES_CBB == 0) {
                    strNomFormato += "_cfdi";
                }
                //Nombres de archivos
                String strFilePdf = strPathXML + "/" + strNomFormato + "_" + intEMP_ID + "_" + strNumFolio + ".pdf";
                String strFileXML = strPathXML + "/" + "XmlSAT" + intFAC_ID + " .xml";
                if (bolEsNC) {
                    strFileXML = strPathXML + "/" + "NC_XML" + intFAC_ID + ".xml";
                }
                //Generamos el formato de impresion
                try {
                    Document document = new Document();
                    PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(strFilePdf));
                    //Objeto que dibuja el numero de paginas
                    PDFEventPage pdfEvent = new PDFEventPage();
                    pdfEvent.setStrTitleApp(strTitle);
                    //Colocamos el numero donde comienza X por medio del parametro del web Xml por si necesitamos algun ajuste
                    if (strPosX != null) {
                        try {
                            int intPosX = Integer.valueOf(strPosX);
                            pdfEvent.setIntXPageNum(intPosX);
                        } catch (NumberFormatException ex) {
                        }
                    } else {
                        pdfEvent.setIntXPageNum(300);
                        pdfEvent.setIntXPageNumRight(50);
                        pdfEvent.setIntXPageTemplate(252.3f);
                    }
                    //Anexamos el evento
                    writer.setPageEvent(pdfEvent);

                    document.open();
                    FormateadorMasivo format = new FormateadorMasivo();
                    format.setBolSeisxHoja(true);
                    format.setIntTypeOut(Formateador.FILE);
                    format.setStrPath(this.getServletContext().getRealPath("/"));
                    format.InitFormat(oConn, strNomFormato);
                    String strRes = format.DoFormat(oConn, intFAC_ID);
                    if (strRes.equals("OK")) {
                        CIP_Formato fPDF = new CIP_Formato();
                        fPDF.setDocument(document);
                        fPDF.setWriter(writer);
                        fPDF.setBolSeisxHoja(true);
                        fPDF.setStrPathFonts(strPathFonts);
                        fPDF.EmiteFormatoMasivo(format.getFmXML());
                        document.close();
                        bolIsOkFacPDF = true;
                    }
                } catch (Exception ex) {
                    System.out.println("error generar formato " + ex.getMessage());
                }
                //Mandamos el mail
                Mail mail = new Mail();
                mail.setBolDepuracion(false);
                if (intSmtpUsaSSL == 1) {
                    mail.setBolUsaTls(true);
                }
                if (intSmtpUsaSSSL == 1) {
                    mail.setBolUsaStartTls(true);
                }
                mail.setHost(strSmtpServer);
                mail.setUsuario(strSmtpUser);
                mail.setContrasenia(strSmtpPassword);
                mail.setPuerto(strSmtpPort);
                //Adjuntamos archivos
                mail.setFichero(strFilePdf);
                if (intEMP_USACODBARR == 0) {
                    //Validamos si existe el archivo con el formato viejo
                    File file = new File(strFileXML);//specify the file path 
                    if (!file.exists()) {
                        //Version 2.0
                        StringBuilder strNomFile = new StringBuilder("");
                        ERP_MapeoFormato mapeoXml = null;
                        mapeoXml = new ERP_MapeoFormato(1);
                        strFileXML = strPathXML + (getNombreFileXml(mapeoXml, intFAC_ID, strFAC_RAZONSOCIAL, strFAC_FECHA, strNumFolio));
                        mail.setFichero(strFileXML); //manda el xml porque ya creo la ruta
                    }
                    if (file.exists()) {
                        System.out.println("Si existe el archivo y se adjunta; \n" + strFileXML);
                        mail.setFichero(strFileXML);
                    }
                }
                System.out.println("\n\n########################## venta validada_CORREOS: \n\n" + strEmailSend + "\n\n##########################");
                if (intVIEW_Templete == 1) {
                    String[] lstMail = getMailTemplate("FACTURA", oConn);
                    String strMsgAsunto = lstMail[0].replace("%folio%", strNumFolio);
//                    StristMail[1].replace("%folio%", strNumFolio);
                    String strMsgMensaje = lstMail[1].replace("%folio%", strNumFolio).replace("%CT_RAZONSOCIAL%", strFAC_RAZONSOCIAL);
                    mail.setAsunto(strMsgAsunto);
                    //Preparamos el mail
                    mail.setDestino(strEmailSend);
//                    System.out.println(strEmailSend + " emails a los que se les enviara!!!! ------------------------------------------");
                    mail.setMensaje(strMsgMensaje);
                    String strQueryVenta = "";
                    if (strTipoVta.equals("1")) {
                        strQueryVenta = "select * from vta_facturas where FAC_ID = ";
                    } else {
                        strQueryVenta = "select * from vta_tickets where TKT_ID = ";
                    }
                    //Reemplazamos campos personalizados
                    String strSqlMail = strQueryVenta + intFAC_ID;
                    ResultSet rsMail;
                    try {
                        rsMail = oConn.runQuery(strSqlMail, true);
                        mail.setReplaceContent(rsMail);
                    } catch (SQLException ex) {
                        System.out.println(ex);
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                    //Reemplazamos campos personalizados en la empresa...
                    strSqlMail = "select * from vta_empresas where EMP_ID = " + varSesiones.getIntIdEmpresa();
                    try {
                        rsMail = oConn.runQuery(strSqlMail, true);
                        mail.setReplaceContent(rsMail);
                    } catch (SQLException ex) {
                        System.out.println(ex);
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }

                } else {
                    mail.setAsunto(strMailASOK);
                    //Preparamos el mail
                    mail.setDestino(strEmailSend);
                    mail.setMensaje(strMailOK);
                }

                //Enviamos el mail
                boolean bol = mail.sendMail();//
                if (!bol) {
                    strResp = "NO SE PUDO ENVIAR EL MAIL.";
                }
            } else {
                strResp = "NO EXISTEN MAILS.";
                System.out.println("PRUEBA \n\n no existe o no es correcto el mail");
            }
        } else {
            System.out.println("PRUEBA: \n\n el correo esta mal");
        }
        System.out.println("######################## RESPUESTA DE LA FACTURA: " + strResp);
    }

    /**
     * Genera el n re de ml
     */
    public String getNombreFileXml(ERP_MapeoFormato mapeo, int intTransaccion, String strNombreReceptor, String strFechaCFDI, String strFolioFiscalUUID) {
        String strNomFileXml = null;
        String strPatronNomXml = mapeo.getStrNomXML("NOMINA");
        strNomFileXml = strPatronNomXml.replace("%Transaccion%", intTransaccion + "").replace("", "").replace("%nombre_receptor%", strNombreReceptor).replace("%fecha%", strFechaCFDI).replace("%UUID%", strFolioFiscalUUID).replace(" ", "_");
        return strNomFileXml;
    }

    /**
     * Obtenemos valo del template para el mail
     *
     * @param strNom Es el nombre del template
     * @return Regresa un arreglo con los valores del template
     */
    public String[] getMailTemplate(String strNom, Conexion oConn) {
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
            System.out.println(ex.getErrorCode());
        }
        return listValores;
    }
// obtener datos del usuario

    public String[] GetUser(Conexion oConn, String strCorreo, String strIdUsr, String strNom, String Apellido, String strTitulo) {
        String[] listValores = new String[3];
        String strIdUsrWeb = cmc.createUsuarioNuevoCurso(strCorreo, Integer.parseInt(strIdUsr), strNom, Apellido, strTitulo);
        try {
            Conexion oConnMGV = new Conexion();
            oConnMGV.setStrNomPool("jdbc/COFIDE");
            oConnMGV.open();
            String strSql = "select usr,psw from cat_usr_w where id_usr_w = " + strIdUsrWeb;
            ResultSet rs = oConnMGV.runQuery(strSql, true);
            while (rs.next()) {
                listValores[0] = rs.getString("psw");
                listValores[1] = rs.getString("usr");
                listValores[2] = strIdUsrWeb;
            }
            rs.close();
            oConnMGV.close();
        } catch (SQLException ex) {
            System.out.println("GET PASSWORD COFIDE MGV: " + ex.getLocalizedMessage());
        }
        return listValores;
    }
%>

<%!
    /**
     * String strIdVta - id de la venta que cambio String strUserId - id del
     * usuario nuevo String opc - 1 = manda correo de aprobación, 2 =
     * reasignación
     */
    public String SendConfirm(Conexion oConn, String strIdVta, String strUserId, String opc, String strIdCte) {
        String strPlantilla = "CONFIRM_VTA";
        String strAsunto = "";
        String strCorreo = "";
        String strCorreo2 = "";
        String strCuerpo = "";
        this.strSql = "select SMTP_US from usuarios where id_usuarios = (select VTA_AGENTE from cofide_venta_temporal where VTA_ID_M = " + strIdVta + " limit 1)";
        try {
            rs = oConn.runQuery(this.strSql, true);
            while (rs.next()) {
                strCorreo = rs.getString("SMTP_US"); //DESTINO ORIGINAL
            }
            rs.close();
        } catch (SQLException sql) {
            System.out.println("Errro al enviar coreo");
        }

        if (opc.equals("1")) { //manda correo de confirmacion de venta            
            strAsunto = "CONFIRMACIÖN DE VENTA...";
            strCuerpo = "<div><img style='width:25%'; src='http://www.cofide.org/cofide2017/assets/cofide_newlogo_MR.png' alt='logo' border='0'>"
                    + "<br/><div>Estimado(a); </div><div>Te informamos que la venta del cliente: <b>" + strIdCte + "</b> fue autorizada!.  </div>.";
            Email(strCorreo, strPlantilla, strCuerpo, strAsunto, oConn);
        }
        if (opc.equals("2")) { //manda correo de reasignación            
            //notifica que perdio una venta
            strAsunto = "SE HA REASIGNADO LA VENTA...";
            strCuerpo = "<div><img style='width:25%'; src='http://www.cofide.org/cofide2017/assets/cofide_newlogo_MR.png' alt='logo' border='0'>"
                    + "<br/><div>Estimado(a); </div><div>Te informamos que la venta del cliente: <b>" + strIdCte + "</b> "
                    + "<br/>se encontró duplicada y fue reasignada a otro asesor. </div>";
            Email(strCorreo, strPlantilla, strCuerpo, strAsunto, oConn);
            //confirma reasignado            
            this.strSql = "select SMTP_US from usuarios where id_usuarios = " + strUserId;
            try {
                rs = oConn.runQuery(this.strSql, true);
                while (rs.next()) {
                    strCorreo2 = rs.getString("SMTP_US"); //DESTINO ORIGINAL
                }
                rs.close();
            } catch (SQLException sql) {
                System.out.println("Errro al enviar coreo");
            }
            strAsunto = "SE TE HA ASIGNADO UNA VENTA NUEVA...";
            strCuerpo = "<div><img style='width:25%'; src='http://www.cofide.org/cofide2017/assets/cofide_newlogo_MR.png' alt='logo' border='0'>"
                    + "<br/><div>Estimado(a); </div><div>Te informamos que la venta del cliente: <b>" + strIdCte + "</b> se te ha asignado. </div>";
            System.out.println("Se reasigna la vents");
            Email(strCorreo2, strPlantilla, strCuerpo, strAsunto, oConn);
        }
        return this.strRes;
    }

    public String Email(String strCorreo, String strPlantilla, String strCuerpo, String strAsunto, Conexion oConn) {
        this.strRes = "Hubó un problema al enviar la notifiación por correo.";
        Mail mail = new Mail();
        if (mail.isEmail(strCorreo)) {
            mail.setBolDepuracion(false);
            mail.getTemplate(strPlantilla, oConn);
            String strMessage = mail.getMensaje().replace("%CUERPO%", strCuerpo);
            mail.setAsunto(strAsunto);
            mail.setMensaje(strMessage);
            mail.setDestino(strCorreo);
            boolean bol = mail.sendMail();
            if (bol) {
                this.strRes = "OK";
            }
        } else {
            this.strRes = "El correo del ejecutivo no es valido.";
        }
        return this.strRes;
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

    public void quitPtsCancel(Conexion oConn, String strIdVta) {

        boolean bolAplica = false;
        String strCT_ID = "";
        String strTipoDOC = "";
        String strPR_ID = "";
        int intCuantosModulos = 1;

        strSql = "select v.FAC_ID, CT_ID, v.TIPO_DOC, PR_ID "
                + "from view_ventasglobales v "
                + "INNER JOIN view_ventasglobalesdeta vd on v.FAC_ID = vd.FAC_ID "
                + "where v.FAC_ID = " + strIdVta + " and FAC_PAGADO = 1 and FACD_TIPO_CURSO = 1 AND FAC_PROMO = 0 ";
        try {
            rs = oConn.runQuery(strSql, true);
            while (rs.next()) {

                bolAplica = true;
                strCT_ID = rs.getString("CT_ID");
                strTipoDOC = rs.getString("TIPO_DOC");
                strPR_ID = rs.getString("PR_ID");

                //busca los modulos
                String strSql2 = "select CC_CURSO_ID from cofide_modulo_curso WHERE CC_CURSO_IDD = " + strPR_ID;
                ResultSet rs2 = oConn.runQuery(strSql2, true);
                while (rs2.next()) {
                    intCuantosModulos++;
                }
                rs2.close();

                strSql2 = "select CONTACTO_ID from cofide_participantes where " + (strTipoDOC.equals("F") ? "CP_FAC_ID" : "CP_TKT_ID") + " = '" + strIdVta + "' and CP_ID_CURSO = '" + strPR_ID + "'";
                rs2 = oConn.runQuery(strSql2, true);
                while (rs2.next()) {
                    String strSqlDel = "delete from cofide_puntos_contacto where CPC_ACTIVO = 1 and CONTACTO_ID = '" + rs2.getString("CONTACTO_ID") + "' limit " + intCuantosModulos + " ";
                    oConn.runQueryLMD(strSqlDel);
                }
                rs2.close();
            }
            rs.close();

        } catch (SQLException sql) {
            System.out.println("error al obtener información de la consulta: [ " + sql.getMessage() + " ]");
        }
    }

    public String getMetodoPago(Conexion oConn, String strIdVta) {
        String strSetMetodoPago = "0";

        String strSql = "select TKT_METODODEPAGO from vta_tickets where TKT_ID = " + strIdVta;
        try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                if (rs.getString("TKT_METODODEPAGO").equals("PUE")) {
                    strSetMetodoPago = "1";
                } else {
                    strSetMetodoPago = "0"; //
                }
            }
            rs.close();
        } catch (SQLException sql) {
            System.out.println("error al obtener el metodo de pago: " + sql.getMessage());
        }
        return strSetMetodoPago;
    }
%>