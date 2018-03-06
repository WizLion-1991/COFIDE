/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.especiales.cofide;

import Tablas.CofideParticipantes;
import Tablas.cofide_contactos;
import Tablas.crm_eventos;
import Tablas.vta_cliente_facturacion;
import com.mx.siweb.erp.especiales.cofide.entidades.CofideParticipanteVta;
import com.mx.siweb.erp.especiales.cofide.entidades.VtaContactoTMP;
import com.mx.siweb.erp.especiales.cofide.entidades.VtaRazonsocialTMP;
import com.mx.siweb.erp.especiales.cofide.entidades.VtaCteTMP;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Utilerias.Fechas;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import javax.servlet.http.HttpServletRequest;

/**
 * Realiza las operaciones de negocio de la pantalla de telemarketing
 *
 * @author ZeusSIWEB
 */
public class Telemarketing {

    // <editor-fold defaultstate="collapsed" desc="Propiedades">
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Constructores">
    Fechas fec = new Fechas();
    COFIDE_Mail_cursos mg = new COFIDE_Mail_cursos();

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Metodos">
    /**
     * GUARDA el cliente o prospecto en la base de datos
     *
     * @param oConn Es la conexion
     * @param strBolBase Indica si es de la base
     * @param intCT_ID Es el id del cliente
     * @param strCT_NO_CLIENTE Es el numero de cliente
     * @param strRazonsocial Nombre
     * @param strRfc RFC
     * @param strContacto Contacto
     * @param strContacto2
     * @param strCorreo Correo
     * @param strCorreo2
     * @param strFecha Fecha evento
     * @param strHora Hora evento
     * @param strComent Comentario
     * @param strSede Sede
     * @param strGiro
     * @param strArea
     * @param strCp
     * @param strCalle
     * @param strNumEx
     * @param strCol
     * @param varSesiones Variable de sesion
     * @param intMailMes
     * @param strNombre
     * @param strConmutador
     * @param intAAA
     * @return
     * @throws java.sql.SQLException
     */
    public String doSaveProspectoBase(Conexion oConn, String strBolBase, int intCT_ID,
            String strCT_NO_CLIENTE, String strRazonsocial, String strRfc, String strContacto, String strContacto2, String strCorreo,
            String strCorreo2, String strFecha, String strHora, String strComent, String strSede, String strGiro, String strArea, String strCp, String strCalle,
            String strNumEx, String strCol, VariableSession varSesiones, int intMailMes, String strNombre, String strConmutador, int intAAA, String strIdMedioPublicidad,
            String strMotivo) throws SQLException {
        String strResultado = "OK";

        //Actualizar el cliente
        vta_clientes cte = new vta_clientes();
        //Recueperamos valores guardados
        cte.ObtenDatos(intCT_ID, oConn);
        cte.setFieldInt("CT_ID", intCT_ID);
        cte.setFieldString("CT_NUMERO", strNumEx);
        cte.setFieldString("CT_RAZONSOCIAL", strRazonsocial);
        cte.setFieldString("CT_RFC", strRfc);
        cte.setFieldString("CT_SEDE", strSede);
        cte.setFieldString("CT_GIRO", strGiro);
        cte.setFieldString("CT_AREA", strArea);
        cte.setFieldString("CT_TELEFONO1", strContacto);
        cte.setFieldString("CT_TELEFONO2", strContacto2);
        cte.setFieldString("CT_EMAIL1", strCorreo);
        cte.setFieldString("CT_EMAIL2", strCorreo2);
        cte.setFieldString("CT_COLONIA", strCol);
        cte.setFieldString("CT_CALLE", strCalle);
        cte.setFieldString("CT_CP", strCp);
        cte.setFieldInt("CT_MAILMES", intMailMes);
        cte.setFieldString("CT_CONTACTO1", strNombre);
        cte.setFieldString("CT_CONMUTADOR", strConmutador);
        cte.setFieldInt("CT_AAA", intAAA); //clasificación aaa
        cte.setFieldString("CT_MKT", strIdMedioPublicidad); //clasificación aaa
        cte.setFieldString("CT_NOMBRE_INVITO", strMotivo); //clasificación aaa
        //Guardamos
        String strRes1 = cte.Modifica(oConn);
        //ponemos el contacto en pendiente
        String strSqlUpdateCrm = "Update crm_eventos set EV_ESTADO = 0 where EV_CT_ID = " + intCT_ID; //tiene que ser 0, porque el 1 es agendado
        oConn.runQueryLMD(strSqlUpdateCrm);
        if (strRes1.equals("OK")) {
            //Guardar fecha de siguiente evento
            String strCT_ID = String.valueOf(intCT_ID); //convertir el id a string
            if (!strFecha.isEmpty()) {
                crm_eventos eve = new crm_eventos();
                //asignamos valores de crm_eventos
                eve.setFieldString("EV_FECHA_INICIO", strFecha);
                eve.setFieldString("EV_HORA_INICIO", strHora);
                eve.setFieldString("EV_ASUNTO", strComent);
                eve.setFieldString("EV_CT_ID", strCT_ID);
                eve.setFieldInt("EV_ASIGNADO_A", varSesiones.getIntNoUser());
                eve.setFieldInt("EV_ESTADO", 1);
                //agrega la cita del evento a crm_eventos
                eve.Agrega(oConn);
                //actualizar comentarios de la ultima llamada
                String strSql = "select CL_ID from cofide_llamada where CL_ID_CLIENTE = " + strCT_ID + " order by CL_FECHA desc limit 1";
                ResultSet rs = oConn.runQuery(strSql, true);
                while (rs.next()) {
                    String strUpComent = "update cofide_llamada set CL_COMENTARIO = '" + strComent + "', CL_PROXIMA_FECHA = '" + strFecha + "', CL_COMPLETO = 1 where CL_ID = " + rs.getInt("CL_ID");
                    oConn.runQueryLMD(strUpComent);
                }
                rs.close();
            }
            strResultado = "OK" + strCT_ID;
        } else {
            strResultado = strRes1;
        }
        return strResultado;
    }

    /**
     * Guarda los contactos definidos
     *
     * @param oConn Conexion
     * @param varSesiones Varables de sesion
     * @param request Peticion
     * @param intCT_ID Id de cliente
     * @return
     */
    public String guardaContactos(Conexion oConn, VariableSession varSesiones, HttpServletRequest request, int intCT_ID) {
        String strResultado = "OK";
        //borrado
        String strDelSql = "delete from cofide_contactos where ct_id = " + intCT_ID;
        oConn.runQueryLMD(strDelSql);
        cofide_contactos cco = new cofide_contactos();
        //recuperamos el lenght del arreglo para recuperar las cadenas del grid de contacto        

        int intlength = Integer.parseInt(request.getParameter("length_contactos"));
        for (int i = 0; i < intlength; i++) {
            int intMailMes1 = 0;
            int intMailMes2 = 0;
            System.out.println("mail1 " + request.getParameter("CT_MAILMES1" + i));
            System.out.println("mail2 " + request.getParameter("CT_MAILMES2" + i));
            if (request.getParameter("CT_MAILMES1" + i) != null) {
                if (request.getParameter("CT_MAILMES1" + i).equals("SI")) {
                    intMailMes1 = 1;
                }
            }
            if (request.getParameter("CT_MAILMES2" + i) != null) {
                if (request.getParameter("CT_MAILMES2" + i).equals("SI")) {
                    intMailMes2 = 1;
                }
            }

            String strNombre = request.getParameter("CCO_NOMBRE" + i).trim();
            String strApPat = request.getParameter("CCO_APPATERNO" + i).trim();
            String strApMat = request.getParameter("CCO_APMATERNO" + i).trim();
            String strTitulo = request.getParameter("CCO_TITULO" + i).trim();
            String strNSocio = request.getParameter("CCO_NOSOCIO" + i);
            String strArea = request.getParameter("CCO_AREA" + i);
            String strAsoc = request.getParameter("CCO_ASOCIACION" + i);
            String strCorreo = request.getParameter("CCO_CORREO_" + i);
            String strCorreo2 = request.getParameter("CCO_CORREO2_" + i);
            String strTelefono = request.getParameter("CCO_TELEFONO" + i);
            String strExt = request.getParameter("CCO_EXTENCION" + i);
            String strAlt = request.getParameter("CCO_ALTERNO" + i);
            int intIDContacto = getNewIdContact(oConn, request.getParameter("CONTACTO_ID" + i));
            System.out.println("nuevo id o viejo: " + intIDContacto);

            //OBTENEMOS LOS DATOS
            cco.setFieldInt("CT_ID", intCT_ID);
            cco.setFieldString("CCO_NOMBRE", strNombre);
            cco.setFieldString("CCO_APPATERNO", strApPat);
            cco.setFieldString("CCO_APMATERNO", strApMat);
            cco.setFieldString("CCO_TITULO", strTitulo);
            cco.setFieldString("CCO_NOSOCIO", strNSocio);
            cco.setFieldString("CCO_AREA", strArea);
            cco.setFieldString("CCO_ASOCIACION", strAsoc);
            cco.setFieldString("CCO_CORREO", strCorreo);
            cco.setFieldString("CCO_CORREO2", strCorreo2);
            cco.setFieldString("CCO_TELEFONO", strTelefono);
            cco.setFieldString("CCO_EXTENCION", strExt);
            cco.setFieldString("CCO_ALTERNO", strAlt);
            cco.setFieldInt("CT_MAILMES", intMailMes1);
            cco.setFieldInt("CT_MAILMES2", intMailMes2);
            cco.setFieldInt("CONTACTO_ID", intIDContacto);
            //AGREGAMOS REGISTROS A LA TABLA
            String strResCCO = cco.Agrega(oConn);
            if (strResCCO.equals("OK")) {
                strResultado = strResCCO;
            }
        }
        return strResultado;
    }

    /**
     * Guarda los contactos definidos
     *
     * @param oConn Conexion
     * @param varSesiones Varables de sesion
     * @param request Peticion
     * @param intCT_ID Id de cliente
     * @param intTktId Es el id del ticket
     * @param intFacId Es el id de la factura
     * @param intReqFac Requiere factura
     * @param intReqMatImp Requiere material impreso
     * @param intTipoCurso
     * @param strFechaPago
     * @param strDigitoPago
     * @param strNomComprobantePago
     * @param intMod
     * @param strPagoOk
     * @return
     * @throws java.sql.SQLException
     * @throws java.text.ParseException
     */
    public String guardaParticipantes(Conexion oConn, VariableSession varSesiones, HttpServletRequest request,
            int intCT_ID, int intTktId, int intFacId, int intReqFac, int intReqMatImp,
            int intTipoCurso, String strFechaPago, String strDigitoPago, String strNomComprobantePago,
            int intMod, String strPagoOk) throws SQLException, ParseException, UnsupportedEncodingException {
        String strResultado = "OK";
        Fechas fecha = new Fechas();
        CofideParticipantes cco = new CofideParticipantes();
        //recuperamos el lenght del arreglo para recuperar las cadenas del grid de contactos
        int intlength = Integer.parseInt(request.getParameter("length_participa"));
        int intIdCurso = Integer.parseInt(request.getParameter("CEV_IDCURSO"));
        int intContactoID = 0;
        String strPrecioVta = "0.00";

        String strTipoVtaDoc = request.getParameter("TIPOVENTA");
        if (strTipoVtaDoc == null) {
            strPrecioVta = request.getParameter("TKT_TOTAL");
        } else {
            if (strTipoVtaDoc.equals("1")) {
                strPrecioVta = request.getParameter("FAC_TOTAL");
            } else {
                strPrecioVta = request.getParameter("TKT_TOTAL");
            }
        }
        String strPromocion = request.getParameter("promo"); //si es 1 = es promocion

        //limpia la venta en la web, para los nuevos usuarios, ANTES DEL FOR
        Conexion oConnMGV = new Conexion();
        oConnMGV.setStrNomPool("jdbc/COFIDE");
        oConnMGV.open();
        int intVta_ = 0;
        if (intTktId != 0) {
            intVta_ = intTktId;
        }
        if (intFacId != 0) {
            intVta_ = intFacId;
        }
        System.out.println("##### LIMPIA LA VENTA EN LA WEB #####");
        String strDelete = "delete from ventas_crm where tkt_id_crm = " + intVta_;
        oConnMGV.runQueryLMD(strDelete);
        //limpia la venta en la web, para los nuevos usuarios

        for (int i = 0; i < intlength; i++) {
            String strTitulo = request.getParameter("CCO_TITULO" + i).trim();
            String strNombre = request.getParameter("CCO_NOMBRE" + i).trim();
            String strApPat = request.getParameter("CCO_APPATERNO" + i).trim();
            String strApMat = request.getParameter("CCO_APMATERNO" + i).trim();
            String strNSocio = request.getParameter("CCO_NOSOCIO" + i);
            String strAsoc = request.getParameter("CCO_ASOCIACION" + i);
            String strCorreo = request.getParameter("CCO_CORREO" + i);
            String strMaterial = request.getParameter("material" + i);
            //si trae ID lo convierte a numero y lo almacena en variable
            if (!request.getParameter("CONTACTO_ID" + i).equals("0")) {
                intContactoID = Integer.parseInt(request.getParameter("CONTACTO_ID" + i));
            } else {
                //si no existe el contacto id, crea uno nuevo para obtener su ID
                System.out.println("############################ es un contacto nuevo, lo agregamos al cliente con su nuevo id_contacto");
                intContactoID = CreaContacto(oConn, strTitulo, strNombre, strApPat, strApMat, strCorreo, intCT_ID);
            }
            //OBTENEMOS LOS DATOS
            cco.setFieldInt("CT_ID", intCT_ID);
            cco.setFieldInt("CP_ID_CURSO", intIdCurso);
            cco.setFieldInt("CP_FAC_ID", intFacId);
            cco.setFieldInt("CP_TKT_ID", intTktId);
            cco.setFieldInt("CP_USUARIO_ALTA", varSesiones.getIntNoUser());
            cco.setFieldString("CP_NOMBRE", strNombre);
            cco.setFieldString("CP_APPAT", strApPat);
            cco.setFieldString("CP_APMAT", strApMat);
            cco.setFieldString("CP_TITULO", strTitulo);
            cco.setFieldString("CCO_NOSOCIO", strNSocio);
            cco.setFieldString("CP_ASCOC", strAsoc);
            cco.setFieldString("CP_CORREO", strCorreo);
            cco.setFieldInt("CP_REQFAC", intReqFac);
            cco.setFieldString("CP_MATERIAL_IMPRESO", (strMaterial.equals("SI") ? "1" : "0"));
            cco.setFieldInt("CP_TIPO_CURSO", intTipoCurso);
            cco.setFieldInt("CP_ESTATUS", 1);
            cco.setFieldInt("CONTACTO_ID", intContactoID);
            System.out.println("FECHA PAGO : " + strFechaPago);
            if (!strFechaPago.isEmpty()) {
//                System.out.println("FECHA BD : " + fecha.FormateaBD(strFechaPago, "/"));
                cco.setFieldString("CP_FECHA_PAGO", fecha.FormateaBD(strFechaPago, "/"));
            }
            cco.setFieldString("CP_DIGITO", strDigitoPago);
            cco.setFieldString("CP_NOM_COMPROBANTE", strNomComprobantePago);

            //AGREGAMOS REGISTROS A LA TABLA
            String strResCCO = cco.Agrega(oConn);
//            COFIDE_Mail_cursos mg = new COFIDE_Mail_cursos();
// <editor-fold defaultstate="collapsed" desc="Crea y asigna usuarios en la web">            
            //Creamos el usuario en la BAse COFIDE MGV
            String strIdNewUser = mg.createUsuarioNuevoCurso(strCorreo, intCT_ID, strNombre, strApPat + " " + strApMat, strTitulo);
            //GUARDAMOS LA VENTA EN LA WEB
            if (!strCorreo.equals("")) {
                // si realizo una venta, se reflejara en la web
                if (intFacId != 0) {
//                    mg.VtaWeb(oConn, intFacId, strIdNewUser, intTipoCurso, intMod, "F");
                }
                if (intTktId != 0) {
//                    mg.VtaWeb(oConn, intTktId, strIdNewUser, intTipoCurso, intMod, "T");
                }
            }
            //crea usuarios y los asigna en el correo
            String strUser = "";
            String strPassWordMGV = "";
            try {
//                Conexion oConnMGV = new Conexion();
//                oConnMGV.setStrNomPool("jdbc/COFIDE");
//                oConnMGV.open();
                String strSql = "select usr,psw from cat_usr_w where id_usr_w = " + strIdNewUser;
                ResultSet rs = oConnMGV.runQuery(strSql, true);
                while (rs.next()) {
                    strPassWordMGV = rs.getString("psw");
                    strUser = rs.getString("usr");
                }
                rs.close();
            } catch (SQLException ex) {
                System.out.println("GET PASSWORD COFIDE MGV: " + ex.getLocalizedMessage());
            }
            mg.setStrUsuario(strUser);
            mg.setStrContraseña(strPassWordMGV);
            System.out.println("\n################## " + strUser + " ###################\n");
            System.out.println("\n################## " + strPassWordMGV + " ###################\n");
// </editor-fold>                      

            int intNumEjecutivo = 0;
            try {
                String strSql = "select CT_CLAVE_DDBB from vta_cliente where CT_ID = '" + intCT_ID + "';";
                ResultSet rs = oConn.runQuery(strSql, true);
                while (rs.next()) {
                    strSql = "select id_usuarios from usuarios where COFIDE_CODIGO = '" + rs.getString("CT_CLAVE_DDBB") + "';";
                    rs = oConn.runQuery(strSql, true);
                    while (rs.next()) {
                        intNumEjecutivo = rs.getInt("id_usuarios");
                    }
                }
                rs.close();
            } catch (SQLException ex) {
                System.out.println("Error: Consultar Cliente para obtener ID USUARIO: " + ex.getLocalizedMessage());
            }
//            String strConcatParticipante = request.getParameter("CCO_TITULO" + i) + " " + request.getParameter("CCO_NOMBRE" + i) + " " + request.getParameter("CCO_APPATERNO" + i) + " " + request.getParameter("CCO_APMATERNO" + i);
            String strConcatParticipante = request.getParameter("participantes");
            System.out.println("####################particvipantes: \n" + strConcatParticipante);
            int intPR_ID = Integer.parseInt(request.getParameter("CEV_IDCURSO"));
            final String strRazonsocial = URLDecoder.decode(new String(request.getParameter("CT_RAZONSOCIAL").getBytes("iso-8859-1")), "UTF-8");
            //registramos promocion
            System.out.println("################################# PROMOCIÓN: " + strPromocion);
            if (strPromocion.equals("1")) { //consume puntos

                System.out.println("################################# VA A CONSUMIR SUS PUNTOS CON LA PROMOCION");
                getAndSetPuntos(oConn, intContactoID, intIdCurso, 1);
                System.out.println("################################# MANDA RESERVACIÓN");
//                mg.MailReservacion(oConn, strCorreo, strConcatParticipante, request.getParameter("CT_RAZONSOCIAL"), intPR_ID, intNumEjecutivo, intTipoCurso, strPrecioVta);                
                mg.MailReservacion(oConn, strCorreo, strConcatParticipante, strRazonsocial, intPR_ID, intNumEjecutivo, intTipoCurso, strPrecioVta);
                System.out.println("################################# MANDA DESCARGA DE MAIL DE LA PROMOCIÓN");
//                mg.MailDescargaMaterial(oConn, strCorreo, strConcatParticipante, request.getParameter("CT_RAZONSOCIAL"), intIdCurso, intNumEjecutivo, intTipoCurso, strPrecioVta);
                mg.MailDescargaMaterial(oConn, strCorreo, strConcatParticipante, strRazonsocial, intIdCurso, intNumEjecutivo, intTipoCurso, strPrecioVta);
                strPagoOk = "1";

            } else { // asigna puntos
                // pago = 0 && presencial && con pago
                if (strPagoOk.equals("0") && intTipoCurso == 1 && !strNomComprobantePago.equals("")) { //cuando es la venta por primera vez
                    System.out.println("################################# SE LE VAN A ASIGNAR PUNTOS POR SU COMPRA");
                    getAndSetPuntos(oConn, intContactoID, intIdCurso, 0);
                }

            }
            System.out.println("pagado ya una vez? " + strPagoOk);
            //MANDAMOS RESERVACIÓN
            if (strPagoOk.equals("0")) { //cuando es la venta por primera vez

                if (intTipoCurso == 4) {

                    System.out.println("################################# MANDA RESERVACIÓN NET");
                    mg.MailCofideNet(oConn, strCorreo, strConcatParticipante, strRazonsocial, intPR_ID, intNumEjecutivo, intTipoCurso, strPrecioVta);

                } else {

                    System.out.println("################################# MANDA RESERVACIÓN");
                    mg.MailReservacion(oConn, strCorreo, strConcatParticipante, strRazonsocial, intPR_ID, intNumEjecutivo, intTipoCurso, strPrecioVta);

                }

            }

            if (!strNomComprobantePago.equals("")) { // si pago 

                if (strPagoOk.equals("0")) { //tiene pago neuvo / no se actualiza el pago
                    System.out.println("\n\n########## EL PAGO ES POR PRIMERA VEZ ##########");
                    if (intTipoCurso != 1) {

                        if (intTipoCurso == 4) {
                            System.out.println("################################# MANDA ACCESO NET: USUARIO: " + strUser + " / PASSWORD: " + strPassWordMGV);
                            mg.MailAccesoCofideNet(oConn, strCorreo, strConcatParticipante, strRazonsocial, intIdCurso, intNumEjecutivo, intTipoCurso, strPrecioVta);
                            //envio de acceso a net
                        } else {
                            System.out.println("################################# MANDA ACCESO ONLINE: USUARIO: " + strUser + " / PASSWORD: " + strPassWordMGV);
                            mg.MailAccesoOnline(oConn, strCorreo, strConcatParticipante, strRazonsocial, intIdCurso, intNumEjecutivo, intTipoCurso, strPrecioVta);
                        }

                    } else {
                        System.out.println("################################# MANDA DESCARGA DE MAIL");
//                        mg.MailDescargaMaterial(oConn, strCorreo, strConcatParticipante, request.getParameter("CT_RAZONSOCIAL"), intIdCurso, intNumEjecutivo, intTipoCurso, strPrecioVta);
                        mg.MailDescargaMaterial(oConn, strCorreo, strConcatParticipante, strRazonsocial, intIdCurso, intNumEjecutivo, intTipoCurso, strPrecioVta);
                        //ACTUALIZAMOS el numero de participantes del curso
                        String strUpdateCurs = "update cofide_cursos set CC_INSCRITOS = CC_INSCRITOS + " + intlength + " WHERE CC_CURSO_ID=" + intIdCurso;
                        oConn.runQueryLMD(strUpdateCurs);
                    }
                    //cambia de prospecto a ex participante
                    String strExParticipante = "update vta_cliente set CT_ES_PROSPECTO = 0 where CT_ID = " + intCT_ID;
                    oConn.runQueryLMD(strExParticipante);
                }
            }
        }
//        oConnMGV.close();
        return strResultado;
    }

    public int saveDatosFactura(Conexion oConn, VariableSession varSesiones, HttpServletRequest request, int intCT_ID) throws UnsupportedEncodingException {
        int intIdDatos = 0;
        if (request.getParameter("DFA_ID") != null) {
            intIdDatos = Integer.valueOf(request.getParameter("DFA_ID"));
            vta_cliente_facturacion datFac = new vta_cliente_facturacion();
            if (intIdDatos != 0) {
                datFac.ObtenDatos(intIdDatos, oConn);
            }
            //Recibimos valores
            final String strRazonsocial = URLDecoder.decode(new String(request.getParameter("CT_RAZONSOCIAL").getBytes("iso-8859-1")), "UTF-8");
            final String strRFC = URLDecoder.decode(new String(request.getParameter("CT_RFC").getBytes("iso-8859-1")), "UTF-8");
            final String strCalle = URLDecoder.decode(new String(request.getParameter("CT_CALLE").getBytes("iso-8859-1")), "UTF-8");
            datFac.setFieldString("DFA_RAZONSOCIAL", strRazonsocial);
            datFac.setFieldString("DFA_RFC", strRFC);
            datFac.setFieldString("DFA_NUMERO", request.getParameter("CT_NUM"));
            datFac.setFieldString("DFA_NUMINT", request.getParameter("CT_NUMINT"));
            datFac.setFieldString("DFA_CALLE", strCalle);
            datFac.setFieldString("DFA_COLONIA", request.getParameter("CT_COL"));
            datFac.setFieldString("DFA_MUNICIPIO", request.getParameter("CT_MUNICIPIO"));
            datFac.setFieldString("DFA_ESTADO", request.getParameter("CT_ESTADO"));
            datFac.setFieldString("DFA_CP", request.getParameter("CT_CP"));
            datFac.setFieldString("DFA_TELEFONO", request.getParameter("CT_TELEFONO"));
            datFac.setFieldString("DFA_EMAIL", request.getParameter("CT_CORREO"));
            datFac.setFieldString("DFA_EMAI2", request.getParameter("CT_CORREO2"));
            datFac.setFieldInt("CT_ID", intCT_ID);
            //Guardamos
            if (intIdDatos == 0) {
                datFac.setBolGetAutonumeric(true);
                //Guardamos
                String strRes1 = datFac.Agrega(oConn);
                if (strRes1.equals("OK")) {
                    intIdDatos = Integer.valueOf(datFac.getValorKey());
                }
            } else {

                System.out.println("va a modificar el registro");
                datFac.Modifica(oConn);
            }
        }
        return intIdDatos;
    }

    /**
     *
     * @param oConn
     * @param strCTE EL ID DEL CTE
     * @param request TODOS LOS CAMPOS DEL JAVASCRIPT
     * @param intCT_CLAVE_DDBB
     * @return
     * @throws java.io.UnsupportedEncodingException
     * @throws java.sql.SQLException
     */
    public String GuardarCteTmp(Conexion oConn, String strCTE, HttpServletRequest request, int intCT_CLAVE_DDBB) throws UnsupportedEncodingException, SQLException {
        String strCT_CLAVE_DDBB = "";
        //limpiamos los registros que haya de este cliente
        String strSqlDel = "delete from vta_cliente_tmp where CT_ID = " + strCTE;
        oConn.runQueryLMD(strSqlDel);

        String strSql = "select COFIDE_CODIGO from usuarios where id_usuarios = " + intCT_CLAVE_DDBB;
        ResultSet rs = oConn.runQuery(strSql, true);
        while (rs.next()) {
            strCT_CLAVE_DDBB = rs.getString("COFIDE_CODIGO");
        }
        rs.close();
        //System.out.println(strCTE + " cliente");

        String strMotivo = "";
        if (request.getParameter("motivo") != null) {
            strMotivo = request.getParameter("motivo");
        }

        String strResp = "OK";
        VtaCteTMP CTE = new VtaCteTMP();
        final String strRazonsocial = URLDecoder.decode(new String(request.getParameter("CT_RAZONSOCIAL").getBytes("iso-8859-1")), "UTF-8");
        final String strCalle = URLDecoder.decode(new String(request.getParameter("CT_CALLE").getBytes("iso-8859-1")), "UTF-8");
        final String strRFC = URLDecoder.decode(new String(request.getParameter("CT_RFC").getBytes("iso-8859-1")), "UTF-8");
        CTE.setFieldInt("CT_ID", Integer.parseInt(strCTE));
        CTE.setFieldString("CT_RAZONSOCIAL", strRazonsocial);
        CTE.setFieldString("CT_RFC", strRFC);
        CTE.setFieldString("CT_CALLE", strCalle);
        CTE.setFieldString("CT_COLONIA", request.getParameter("CT_COL"));
        CTE.setFieldString("CT_MUNICIPIO", request.getParameter("CT_MUNI"));
        CTE.setFieldString("CT_ESTADO", request.getParameter("CT_EDO"));
        CTE.setFieldString("CT_CP", request.getParameter("CT_CP"));
        CTE.setFieldString("CT_TELEFONO1", request.getParameter("CT_CONTACTO"));
        CTE.setFieldString("CT_TELEFONO2", request.getParameter("CT_CONTACTO2"));
        CTE.setFieldString("CT_CONTACTO1", request.getParameter("CT_NOMBRE"));
        CTE.setFieldString("CT_EMAIL1", request.getParameter("CT_CORREO"));
        CTE.setFieldString("CT_EMAIL2", request.getParameter("CT_CORREO2"));
        CTE.setFieldString("CT_NUMERO", request.getParameter("CT_NUM"));
        CTE.setFieldString("CT_SEDE", request.getParameter("CT_SEDE"));
        CTE.setFieldString("CT_GIRO", request.getParameter("CT_GIRO"));
        CTE.setFieldString("CT_AREA", request.getParameter("CT_AREA"));
        CTE.setFieldString("CT_MAILMES", request.getParameter("CT_MAILMES"));
        CTE.setFieldString("CT_CONMUTADOR", request.getParameter("CT_CONMUTADOR"));
        CTE.setFieldString("CT_ES_PROSPECTO", request.getParameter("exp_pro"));
        CTE.setFieldString("CT_FECHA_GUARDADO", fec.getFechaActual());
        CTE.setFieldString("CT_HORA_GUARDADO", fec.getHoraActual());
        CTE.setFieldString("CT_CLAVE_DDBB", strCT_CLAVE_DDBB);
        CTE.setFieldString("CT_COMENTARIO", request.getParameter("coment"));
        CTE.setFieldString("CT_FECHA", request.getParameter("fecha"));
        CTE.setFieldString("CT_HORA", request.getParameter("hora"));
        CTE.setFieldString("CT_NOMBRE_INVITO", strMotivo);
        CTE.Agrega(oConn);
        return strResp;
    }

    /**
     *
     * @param oConn
     * @param strCTE EL ID DEL CLIENTE
     * @param request TODOS LOS CAMPOS DEL JAVASCRIPT
     * @return
     * @throws java.sql.SQLException
     */
    public String GuardarContactoTmp(Conexion oConn, String strCTE, HttpServletRequest request) throws SQLException {
        //System.out.println(strCTE + " contacto");
        String strResp = "OK";
        //limpiamos los registros que haya de este cliente
        String strSqlDel = "delete from cofide_contactos_tmp where CT_ID = " + strCTE;
        oConn.runQueryLMD(strSqlDel);
        VtaContactoTMP CCOTmp = new VtaContactoTMP();
        int intlength = Integer.parseInt(request.getParameter("length_contactos"));
        CCOTmp.setFieldInt("CT_ID", Integer.parseInt(strCTE));
        int intMailmes1 = 0;
        int intMailmes2 = 0;
        for (int i = 0; i < intlength; i++) {
            if (request.getParameter("CT_MAILMES1" + i).equals("SI")) {
                intMailmes1 = 1;
            }
            if (request.getParameter("CT_MAILMES2" + i).equals("SI")) {
                intMailmes2 = 1;
            }
//            CCOTmp.setFieldInt("CCO_ID", Integer.parseInt(request.getParameter("CCO_ID" + i)));
            CCOTmp.setFieldString("CCO_NOMBRE", request.getParameter("CCO_NOMBRE" + i).trim());
            CCOTmp.setFieldString("CCO_APPATERNO", request.getParameter("CCO_APPATERNO" + i).trim());
            CCOTmp.setFieldString("CCO_APMATERNO", request.getParameter("CCO_APMATERNO" + i).trim());
            CCOTmp.setFieldString("CCO_TITULO", request.getParameter("CCO_TITULO" + i).trim());
            CCOTmp.setFieldString("CCO_NOSOCIO", request.getParameter("CCO_NOSOCIO" + i));
            CCOTmp.setFieldString("CCO_AREA", request.getParameter("CCO_AREA" + i));
            CCOTmp.setFieldString("CCO_ASOCIACION", request.getParameter("CCO_ASOCIACION" + i));
            CCOTmp.setFieldString("CCO_CORREO", request.getParameter("CCO_CORREO_" + i));
            CCOTmp.setFieldString("CCO_TELEFONO", request.getParameter("CCO_TELEFONO" + i));
            CCOTmp.setFieldString("CCO_EXTENCION", request.getParameter("CCO_EXTENCION" + i));
            CCOTmp.setFieldString("CCO_ALTERNO", request.getParameter("CCO_ALTERNO" + i));
            CCOTmp.setFieldInt("CT_MAILMES", intMailmes1);
            CCOTmp.setFieldInt("CT_MAILMES2", intMailmes2);
            CCOTmp.setFieldString("CCO_CORREO2", request.getParameter("CCO_CORREO2_" + i));
            CCOTmp.setFieldString("CONTACTO_ID", request.getParameter("CONTACTO_ID" + i));
            CCOTmp.Agrega(oConn);
        }
        return strResp;
    }

    /**
     *
     * @param oConn
     * @param strCTE EL ID CEL CTE
     * @param request TODOS LOS CAMPOS DEL JAVASCRIPT
     * @return
     */
    public String GuardaRazonSocialTmp(Conexion oConn, String strCTE, HttpServletRequest request) {
        //System.out.println(strCTE + " razon social");

        //limpiamos los registros que haya de este cliente
        String strSqlDel = "delete from cofide_razonsocial_tmp where CT_ID = " + strCTE;
        oConn.runQueryLMD(strSqlDel);

        String strResp = "OK";
        VtaRazonsocialTMP RZNTmp = new VtaRazonsocialTMP();
        int intlength = Integer.parseInt(request.getParameter("length_razon"));
        RZNTmp.setFieldInt("CT_ID", Integer.parseInt(strCTE));
        for (int i = 0; i < intlength; i++) {
//            RZNTmp.setFieldInt("CR_ID", Integer.parseInt(request.getParameter("CR_ID" + i)));
            RZNTmp.setFieldString("CR_RAZONSOCIAL", request.getParameter("RZN_NOMBRE" + i));
            RZNTmp.Agrega(oConn);
        }
        return strResp;
    }

    public String guardaParticipantesUpdate(Conexion oConn, VariableSession varSesiones, HttpServletRequest request,
            int intTktId, int intReqMatImp,
            int intTipoCurso, String strNomComprobantePago, String strPagoOk) {
        String strResultado = "OK";
        Fechas fecha = new Fechas();
        CofideParticipantes cco = new CofideParticipantes();
        int intCT_ID = 0;
        int intMod = 0;
        String strRazonSocial = "";
        String strDoc = request.getParameter("doc");
        String strDoc_ = "TKT";

        if (strDoc.equals("F")) {

            strDoc_ = "FAC";
        }
        String strPrecioVta = "0.00";
        if (request.getParameter(strDoc_ + "_TOTAL") != null) {
            strPrecioVta = request.getParameter(strDoc_ + "_TOTAL");
        }
        if (request.getParameter("total") != null) {
            strPrecioVta = request.getParameter("total");
        }
        System.out.println("########################### precio: " + strPrecioVta);

        //obtenemor al cliente
        String strSqlCte = "select CT_ID, FAC_RAZONSOCIAL from view_ventasglobales where FAC_ID = " + intTktId + " and TIPO_DOC = '" + strDoc + "'";
        try {
            ResultSet rs = oConn.runQuery(strSqlCte, true);
            while (rs.next()) {
                intCT_ID = rs.getInt("CT_ID");
                strRazonSocial = rs.getString("FAC_RAZONSOCIAL");
            }
            rs.close();
        } catch (SQLException ex) {
            System.out.println("error sql " + ex);
        }
        //limpiamos a los participantes, para insertarlos de neuvo
        strSqlCte = "delete from cofide_participantes where CP_" + strDoc_ + "_ID = " + intTktId;
        oConn.runQueryLMD(strSqlCte);
        //participanets
        String strConcatParticipante = "";
        int intContactoID = 0;
        //recuperamos el lenght del arreglo para recuperar las cadenas del grid de contactos
        System.out.println("######################################recuperamos los participantes");
        if (!request.getParameter("length_participa").equals("0")) {
            int intlength = Integer.parseInt(request.getParameter("length_participa"));
            int intIdCurso = Integer.parseInt(request.getParameter("id_curso"));

            System.out.println("#####################################obtenemos la modalidad");
            String strIdCurso = String.valueOf(intIdCurso);
            intMod = getModalidadCurso(oConn, strIdCurso);

            //limpia la venta en la web, para los nuevos usuarios, ANTES DEL FOR
            Conexion oConnMGV = new Conexion();
            oConnMGV.setStrNomPool("jdbc/COFIDE");
            oConnMGV.open();
            System.out.println("##### LIMPIA LA VENTA EN LA WEB #####");
            String strDelete = "delete from ventas_crm where tkt_id_crm = " + intTktId;
            oConnMGV.runQueryLMD(strDelete);
            //limpia la venta en la web, para los nuevos usuarios

            for (int i = 0; i < intlength; i++) {
                String strTitulo = request.getParameter("CCO_TITULO" + i).trim();
                String strNombre = request.getParameter("CCO_NOMBRE" + i).trim();
                String strApPat = request.getParameter("CCO_APPATERNO" + i).trim();
                String strApMat = request.getParameter("CCO_APMATERNO" + i).trim();
                String strNSocio = request.getParameter("CCO_NOSOCIO" + i);
                String strAsoc = request.getParameter("CCO_ASOCIACION" + i);
                String strCorreo = request.getParameter("CCO_CORREO" + i);
                String strMaterial = request.getParameter("material" + i);
                //id del contacto
                if (!request.getParameter("CONTACTO_ID" + i).equals("0")) {
                    intContactoID = Integer.parseInt(request.getParameter("CONTACTO_ID" + i));
                } else {
                    //si no existe el contacto id, crea uno nuevo para obtener su ID
                    System.out.println("############################ es un contacto nuevo, lo agregamos al cliente con su nuevo id_contacto");
                    intContactoID = CreaContacto(oConn, strTitulo, strNombre, strApPat, strApMat, strCorreo, intCT_ID);
                }
                //fin id del contacto

                //OBTENEMOS LOS DATOS
                cco.setFieldInt("CT_ID", intCT_ID);
                cco.setFieldInt("CP_ID_CURSO", intIdCurso);
                if (strDoc.equals("F")) {
                    cco.setFieldInt("CP_FAC_ID", intTktId);
                    cco.setFieldInt("CP_TKT_ID", 0);
                } else {
                    cco.setFieldInt("CP_FAC_ID", 0);
                    cco.setFieldInt("CP_TKT_ID", intTktId);
                }
                cco.setFieldInt("CP_USUARIO_ALTA", varSesiones.getIntNoUser());
                cco.setFieldString("CP_NOMBRE", strNombre);
                cco.setFieldString("CP_APPAT", strApPat);
                cco.setFieldString("CP_APMAT", strApMat);
                cco.setFieldString("CP_TITULO", strTitulo);
                cco.setFieldString("CCO_NOSOCIO", strNSocio);
                cco.setFieldString("CP_ASCOC", strAsoc);
                cco.setFieldString("CP_CORREO", strCorreo);

                cco.setFieldString("CP_MATERIAL_IMPRESO", (strMaterial.equals("SI") ? "1" : "0"));
                cco.setFieldInt("CP_TIPO_CURSO", intTipoCurso);
                cco.setFieldInt("CP_ESTATUS", 1);
                cco.setFieldString("CP_NOM_COMPROBANTE", strNomComprobantePago);
                cco.setFieldInt("CONTACTO_ID", intContactoID);

                //AGREGAMOS REGISTROS A LA TABLA
                String strResCCO = cco.Agrega(oConn);
                System.out.println("agrega los participantes OK");
//                strConcatParticipante = strTitulo + " " + strNombre + " " + strApPat + " " + strApMat;
                strConcatParticipante = request.getParameter("participantes");
                System.out.println("####################particvipantes: \n" + strConcatParticipante);
// <editor-fold defaultstate="collapsed" desc="Crea y asigna usuarios en la web">            
                //Creamos el usuario en la BAse COFIDE MGV
                String strIdNewUser = mg.createUsuarioNuevoCurso(strCorreo, intCT_ID, strNombre, strApPat + " " + strApMat, strTitulo);
                if (!strCorreo.equals("")) {
                    // si realizo una venta, se reflejara en la web
                    if (strDoc.equals("F")) {
//                        mg.VtaWeb(oConn, intTktId, strIdNewUser, intTipoCurso, intMod, "F");
                    } else {
//                        mg.VtaWeb(oConn, intTktId, strIdNewUser, intTipoCurso, intMod, "T");
                    }
                }
                //crea usuarios y los asigna en el correo
                String strUser = "";
                String strPassWordMGV = "";
                try {
//                    Conexion oConnMGV = new Conexion();
//                    oConnMGV.setStrNomPool("jdbc/COFIDE");
//                    oConnMGV.open();
                    String strSql = "select usr,psw from cat_usr_w where id_usr_w = " + strIdNewUser;
                    ResultSet rs = oConnMGV.runQuery(strSql, true);
                    while (rs.next()) {
                        strPassWordMGV = rs.getString("psw");
                        strUser = rs.getString("usr");
                    }
                    rs.close();
                } catch (SQLException ex) {
                    System.out.println("GET PASSWORD COFIDE MGV: " + ex.getLocalizedMessage());
                }
                mg.setStrUsuario(strUser);
                mg.setStrContraseña(strPassWordMGV);
                System.out.println("\n################## " + strUser + " ###################\n");
                System.out.println("\n################## " + strPassWordMGV + " ###################\n");
// </editor-fold>  
                System.out.println("################################# ASIGNA PUNTOS DE PROMOCIÓN ");
                System.out.println("############ va a mandar los accesos porque ya pago: " + strPagoOk + " ####### nombre del archivo: " + strNomComprobantePago);
                // pago = 0 && presencial && con pago
                if (strPagoOk.equals("0") && intTipoCurso == 1 && !strNomComprobantePago.equals("")) { //cuando es la venta por primera vez
                    System.out.println("################################# SE LE VAN A ASIGNAR PUNTOS POR SU COMPRA");
                    getAndSetPuntos(oConn, intContactoID, intIdCurso, 0);
                }
                try {
                    if (!strNomComprobantePago.equals("")) {
                        if (strPagoOk.equals("0")) {
                            if (intTipoCurso != 1) {

                                if (intTipoCurso == 4) {
                                    System.out.println("################ manda acceso en net / EDICION");
                                    mg.MailAccesoCofideNet(oConn, strCorreo, strConcatParticipante, strRazonSocial, intIdCurso, varSesiones.getIntNoUser(), intTipoCurso, strPrecioVta);
                                } else {
                                    System.out.println("################ manda acceso en linea");
                                    mg.MailAccesoOnline(oConn, strCorreo, strConcatParticipante, strRazonSocial, intIdCurso, varSesiones.getIntNoUser(), intTipoCurso, strPrecioVta);
                                }

                            } else {
                                System.out.println("################ manda el material de curso");
                                mg.MailDescargaMaterial(oConn, strCorreo, strConcatParticipante, strRazonSocial, intIdCurso, varSesiones.getIntNoUser(), intTipoCurso, strPrecioVta);
                            }
                            //cambia prospecto a ex participante, si ya pago
                            String strExParticipante = "update vta_cliente set CT_ES_PROSPECTO = 0 where CT_ID = " + intCT_ID;
                            oConn.runQueryLMD(strExParticipante);
                        }
                    }
                } catch (ParseException parse) {
                    System.out.println("Error al enviar confirmaciones: " + parse);
                } catch (SQLException sql) {
                    System.out.println("Error al enviar confirmaciones: " + sql);
                }

            }
            //ACTUALIZAMOS el numero de participantes del curso
            String strUpdateCurs = "update cofide_cursos set CC_INSCRITOS = CC_INSCRITOS + " + intlength + " WHERE CC_CURSO_ID=" + intIdCurso;
            oConn.runQueryLMD(strUpdateCurs);
        }
        return strResultado;
    }

//venta temporal
    public String guardaParticipantesVta(Conexion oConn, VariableSession varSesiones, HttpServletRequest request,
            int intTktId, int intReqMatImp,
            int intTipoCurso, String strNomComprobantePago, int intCT_ID) {
        String strResultado = "OK";
        Fechas fecha = new Fechas();
        CofideParticipanteVta cco = new CofideParticipanteVta();
        //obtenemor al cliente
        String strSqlCte = "";
        //limpiamos a los participantes, para insertarlos de neuvo
        strSqlCte = "delete from cofide_participantes_vta where CP_TKT_ID = " + intTktId; //venta temporal
        oConn.runQueryLMD(strSqlCte);
        //recuperamos el lenght del arreglo para recuperar las cadenas del grid de contactos
        if (!request.getParameter("length_participa").equals("0")) {
            int intlength = Integer.parseInt(request.getParameter("length_participa"));
            int intIdCurso = Integer.parseInt(request.getParameter("id_curso"));
//            System.out.println("el id del cliente con que se guardaran los contactos------------------------------- " + intCT_ID);
            for (int i = 0; i < intlength; i++) {
                String strTitulo = request.getParameter("CCO_TITULO" + i).trim();
                String strNombre = request.getParameter("CCO_NOMBRE" + i).trim();
                String strApPat = request.getParameter("CCO_APPATERNO" + i).trim();
                String strApMat = request.getParameter("CCO_APMATERNO" + i).trim();
                String strNSocio = request.getParameter("CCO_NOSOCIO" + i);
                String strAsoc = request.getParameter("CCO_ASOCIACION" + i);
                String strCorreo = request.getParameter("CCO_CORREO" + i);
                String strIDContacto = request.getParameter("CONTACTO_ID" + i);
                String strMaterial = request.getParameter("material" + i);

                //OBTENEMOS LOS DATOS
                cco.setFieldInt("CT_ID", intCT_ID);
                cco.setFieldInt("CP_ID_CURSO", intIdCurso);
                cco.setFieldInt("CP_FAC_ID", 0);
                cco.setFieldInt("CP_TKT_ID", intTktId); //id master
                cco.setFieldInt("CP_USUARIO_ALTA", varSesiones.getIntNoUser());
                cco.setFieldString("CP_NOMBRE", strNombre);
                cco.setFieldString("CP_APPAT", strApPat);
                cco.setFieldString("CP_APMAT", strApMat);
                cco.setFieldString("CP_TITULO", strTitulo);
                cco.setFieldString("CCO_NOSOCIO", strNSocio);
                cco.setFieldString("CP_ASCOC", strAsoc);
                cco.setFieldString("CP_CORREO", strCorreo);
                cco.setFieldString("CP_MATERIAL_IMPRESO", (strMaterial.equals("SI") ? "1" : "0"));
                cco.setFieldInt("CP_TIPO_CURSO", intTipoCurso);
                cco.setFieldInt("CP_ESTATUS", 1);
                cco.setFieldString("CONTACTO_ID", strIDContacto);

                cco.setFieldString("CP_NOM_COMPROBANTE", strNomComprobantePago);
                //AGREGAMOS REGISTROS A LA TABLA
                String strResCCO = cco.Agrega(oConn);
                System.out.println("################################# ASIGNA PUNTOS DE PROMOCIÓN ");
                // presencial && con pago
                if (intTipoCurso == 1 && !strNomComprobantePago.equals("")) { //cuando es la venta por primera vez
                    System.out.println("################################# SE LE VAN A ASIGNAR PUNTOS POR SU COMPRA");
                    getAndSetPuntos(oConn, Integer.parseInt(strIDContacto), intIdCurso, 0);
                }
            }
        }
        return strResultado;
    }

    public int getNewIdContact(Conexion oConn, String strIdContacto) {
        int intId = 0;
        String strSql = "";
        if (strIdContacto.equals("0")) {
            strSql = "select MAX(CONTACTO_ID)+1 as maximo from cofide_contactos";
            try {
                ResultSet rs = oConn.runQuery(strSql, true);
                while (rs.next()) {
                    intId = rs.getInt("maximo");
                }
                rs.close();
            } catch (SQLException sql) {
                System.out.println("Error al obtener el ultimo ID de contactos: " + sql.getMessage());
            }
            //pendiente de eliminación de codigo, sera temporal para asignar los puntos a cada uno
            strSql = "insert into cofide_puntos_contacto(CPC_PUNTOS,CONTACTO_ID,CPC_ACTIVO) values (200,'" + intId + "',1)";
            oConn.runQueryLMD(strSql);
            if (oConn.isBolEsError()) {
                System.out.println("Error al asignar sus puntos al contacto: " + oConn.getStrMsgError());
            }
        } else {
            intId = Integer.parseInt(strIdContacto);
        }
        return intId;
    }

    /**
     * toma los puntos del curso y los pone como usados, registra el curso en el
     * quue se gasto
     *
     * @param oConn
     * @param intIdContacto
     * @param intCursoId
     * @param intOpcionGetSet opcion 1 = consume, 1 != asigna
     * @return
     */
    public String getAndSetPuntos(Conexion oConn, int intIdContacto, int intCursoId, int intOpcionGetSet) {
        String strRespuesta = "";
        String strSql = "";
        String strSqlConsume = "";
        ResultSet rs;
        int intPuntos = 0;
        String strIDPunto = ""; //pk del punto
        //consume puntos 1000 por promocion
        if (intOpcionGetSet == 1) {
            strSql = "select CPC_ID, CPC_PUNTOS from cofide_puntos_contacto where CPC_ACTIVO = 1 and CONTACTO_ID = " + intIdContacto;
            try {
                rs = oConn.runQuery(strSql, true);
                while (rs.next()) {
                    intPuntos += rs.getInt("CPC_PUNTOS");
                    strIDPunto = rs.getString("CPC_ID");
                    if (intPuntos <= 1000) {
                        strSqlConsume = "update cofide_puntos_contacto set CPC_ACTIVO = 0, CC_CURSO_ID = " + intCursoId + " where CPC_ID = " + strIDPunto;
                        oConn.runQueryLMD(strSqlConsume);
                        if (oConn.isBolEsError()) {
                            System.out.println("Error al consumir puntos para el curso");
                        }
                    }
                }
                rs.close();
            } catch (SQLException sql) {
                System.out.println("Error al consumir puntos: " + sql.getMessage());
            }
        } else { //otorga 200 puntos por curso comprado                 
            boolean bolSeminarioDiplomado = false;
            System.out.println("############ valida si es un seminario o diplomado para otorgar 200 pts por curso/modulo");
            strSql = "select * from cofide_modulo_curso where CC_CURSO_IDD = " + intCursoId; //vemos si es seminario o diplomado
            try {
                rs = oConn.runQuery(strSql, true);
                // si es seminario o diplomado, hara un insert por curso o modulo
                while (rs.next()) {
                    bolSeminarioDiplomado = true;
                    if (bolSeminarioDiplomado) {
                        strSqlConsume = "insert into cofide_puntos_contacto(CPC_PUNTOS,CONTACTO_ID,CPC_ACTIVO) values (200,'" + intIdContacto + "',1)";
                        oConn.runQueryLMD(strSqlConsume);
                        if (oConn.isBolEsError()) {
                            System.out.println("Error al asignar puntos por la compra: ");
                        }
                    }
                }
                rs.close();
            } catch (SQLException sql) {
                System.out.println("Error al obtener modulos del curso/seminario/diplomado: " + sql.getMessage());
            }
            if (!bolSeminarioDiplomado) { // es un curso normal, solo asignara una vez los puntos
                strSqlConsume = "insert into cofide_puntos_contacto(CPC_PUNTOS,CONTACTO_ID,CPC_ACTIVO) values (200,'" + intIdContacto + "',1)";
                oConn.runQueryLMD(strSqlConsume);
                if (oConn.isBolEsError()) {
                    System.out.println("Error al asignar puntos por la compra ");
                }
            }
        }
        return strRespuesta;
    }

    public int CreaContacto(Conexion oConn, String strTitulo, String strNombre, String strAppat, String strApmat, String strCorreo, int intCT_ID) {

        int intIdContacto = getNewIdContact(oConn, "0");

        cofide_contactos cco = new cofide_contactos();
        cco.setFieldString("CCO_NOMBRE", strNombre);
        cco.setFieldString("CCO_APPATERNO", strAppat);
        cco.setFieldString("CCO_APMATERNO", strApmat);
        cco.setFieldString("CCO_TITULO", strTitulo);
        cco.setFieldString("CCO_CORREO", strCorreo);
        cco.setFieldInt("CONTACTO_ID", intIdContacto);
        cco.setFieldInt("CT_ID", intCT_ID);
        cco.Agrega(oConn);
        return intIdContacto;
    }

    public boolean EsPromo(Conexion oConn, String strIdVta) {
        boolean bolResp = false;
        String strSql = "";
        String strTipoVta = "";
        String strPromo = "";
        String strIdCurso = "";
        try {
//            strSql = "select TIPO_DOC,FAC_PROMO,CC_CURSO_ID from view_ventasglobales where fac_id = " + strIdVta;
            strSql = "select v.TIPO_DOC,FAC_PROMO,CC_CURSO_ID, PR_ID "
                    + "from view_ventasglobales v, view_ventasglobalesdeta vd "
                    + "where v.FAC_ID = vd.FAC_ID "
                    + "and FAC_PROMO = 1 "
                    + "and v.FAC_ID = " + strIdVta;
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                strTipoVta = rs.getString("TIPO_DOC");
                strPromo = rs.getString("FAC_PROMO");
                strIdCurso = rs.getString("CC_CURSO_ID");
                bolResp = true;
                System.out.println("################### es promoción?");
            }
            rs.close();
            if (bolResp && strPromo.equals("1")) {
                System.out.println("################### va a recuperar los puntos de la promoción");
                bolResp = RestartPointsPromo(oConn, strIdVta, strTipoVta, strIdCurso);
            }
        } catch (SQLException sql) {
            System.out.println("ERROR: al recuperar la información de la venta... " + sql.getMessage());
        }
        return bolResp;
    }

    public boolean RestartPointsPromo(Conexion oConn, String strIdVta, String strTipoVta, String strIdCurso) {
        boolean bolResp = false;
        String strSql = "select CONTACTO_ID from cofide_participantes where ";
        String strContactoID = "";

        if (strTipoVta.equals("1")) { //factura
            strSql += " CP_FAC_ID = " + strIdVta;
        } else { //ticket
            strSql += " CP_TKT_ID = " + strIdVta;
        }
        try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                strContactoID = rs.getString("CONTACTO_ID");
                System.out.println("################### recupera al participante");
            }
            rs.close();
            strSql = "update cofide_puntos_contacto set CPC_ACTIVO = 1, CC_CURSO_ID = 0 where CONTACTO_ID = " + strContactoID + " and CC_CURSO_ID = " + strIdCurso;
            oConn.runQueryLMD(strSql);
            if (!oConn.isBolEsError()) {
                System.out.println("################### recupero sus puntos con exito");
                bolResp = true;
            }
        } catch (SQLException sql) {
            System.out.println("ERROR: al recuperar puntos consumidos en la promoción... " + sql.getMessage());
        }
        return bolResp;
    }

    /**
     * obtiene la infirmación del cliente a actualizar su venta desde el
     * HISTORIAL DE VENTAS GENERAL
     *
     * @param oConn CONEXIÓN
     * @param strIdVta ID DE LA VENTA A ACTUALIZAR
     * @param strTipoVta
     * @return INFORMACIÓN DEL CLIENTE [ 1 - ID DEL CLIENTE 2 - RFC 3 - CORREO 4
     * - TELÉFONO ]
     */
    public String[] getInfoCTE(Conexion oConn, String strIdVta, String strTipoVta) {
        String[] strInfoCte = new String[4];
        String strIdcte = "";
        String strRFC = "";
        String strCORREO = "";
        String strTELEFONO = "";
//        String strSQL = "SELECT CT_ID, CT_RFC, CT_EMAIL1, CT_TELEFONO1 "
//                + "FROM vta_cliente "
//                + "WHERE CT_ID = (select CT_ID "
//                + "from view_ventasglobales "
//                + "where FAC_ID = " + strIdVta + " and TIPO_DOC = " + (strTipoVta.equals("1") ? "'F'" : "'T'") + ") ";
        String strSQL = "SELECT CT_ID, CT_RFC, CT_EMAIL1, CT_TELEFONO1 "
                + "FROM vta_cliente "
                + "WHERE CT_ID = (select CT_ID "
                + "from view_ventasglobales "
                + "where FAC_ID = " + strIdVta + ") ";
        ResultSet rs;
        try {
            rs = oConn.runQuery(strSQL, true);
            while (rs.next()) {

                strIdcte = rs.getString("CT_ID");
                strRFC = rs.getString("CT_RFC");
                strCORREO = rs.getString("CT_EMAIL1");
                strTELEFONO = rs.getString("CT_TELEFONO1");

            }
            rs.close();

        } catch (SQLException sql) {
            System.out.println("Error al obtener la información del cliente: " + sql.getMessage());
        }

        strInfoCte[0] = strIdcte;
        strInfoCte[1] = strRFC;
        strInfoCte[2] = strCORREO;
        strInfoCte[3] = strTELEFONO;
        return strInfoCte;
    }

    // </editor-fold>
    public int getAsistentesRRHH(String strIdCurso, Conexion oConn, int intModalidad) {
        int intCuantos = 0;
        String strSql = "select CP_ID_CURSO from cofide_participantes where  CP_TKT_ID = 1 and CP_ESTATUS = 1 and CP_TIPO_CURSO = " + intModalidad + " and CP_ID_CURSO = " + strIdCurso;
        ResultSet rs;
        try {
            rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                intCuantos++;
            }
            rs.close();
        } catch (SQLException sql) {
            System.out.println("Error al obtener la cantidad de colaboradores: " + sql.getMessage());
        }
        return intCuantos;
    }

    /*
    obtener modalidad del curso
     */
    public int getModalidadCurso(Conexion oConn, String strModalidad) {
        int intMod = 1;
        String strSql = "select CC_IS_DIPLOMADO, CC_IS_SEMINARIO, "
                + "ifnull((select count(*) from cofide_modulo_curso where CC_CURSO_IDD = " + strModalidad + "),0) as EsPrincipal "
                + "from cofide_cursos where CC_CURSO_ID = " + strModalidad; //id de curso       
        try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                if (rs.getInt("CC_IS_DIPLOMADO") == 0 && rs.getInt("CC_IS_SEMINARIO") == 0) {
                    intMod = 1;
                } else {
                    if (rs.getInt("CC_IS_DIPLOMADO") == 1 && rs.getInt("EsPrincipal") != 0) {
                        intMod = 2;
                    }
                    if (rs.getInt("CC_IS_SEMINARIO") == 1 && rs.getInt("EsPrincipal") != 0) {
                        intMod = 3;
                    }
                }
            }
            rs.close();
        } catch (SQLException sql) {
            System.out.println("error al obtener la modalidad del curso:" + sql.getMessage());
        }
        return intMod;
    }

    /**
     * envio de correos con base a la venta
     *
     * @param request
     * @param oConn
     * @param varSesiones
     * @param strIdVta
     * @return respuesta de la operación
     */
    public String sendMail(Conexion oConn, VariableSession varSesiones, HttpServletRequest request, String strIdVta) {
        String strRespuesta = "";
        String strSql = "";
        ResultSet rs;
        String strPagado = "0";
        int intIdMaster = Integer.parseInt(request.getParameter("IDMOV"));
        int intReenvio = 0;
        if (request.getParameter("reenvio") != null) {
            intReenvio = Integer.parseInt(request.getParameter("reenvio"));
        }

        if (request.getParameter("CEV_PAGO_OK") != null) {

            if (!request.getParameter("CEV_PAGO_OK").equals("0")) {

                if (!request.getParameter("CEV_PAGO_OK").equals("")) {

                    strPagado = "1"; //ya ha sido pagado con anterioridad
                }
            }
        }

        //información general de la venta
        int intIdVta = 0;
        String strPrecioVta = "0.00";
        String strTipoVtaDoc = "";
        String strCT_ID = "";
        String strRazonSocial = "";
        String strFacUsAlta = "";
        int intPagado = 0;
        String strNombre = "";
        String strServidor = "";
        String strPassword = "";
        String strCorreoUsuario = "";
        int intSTLS = 0;
        int intTLS = 0;
        //información de cada curso
        String strNombreCurso = "";
        String strIdCurso = "";
        int intTipoCurso = 0;
        String strParticipantes = "";
        //correo destinatario
        String strCorreo = "";
        int intContactoID = 0;
        String strNombreContacto = "";
        String strApellidoPat = "";
        String strApellidoMat = "";
        String strTitulo = "";
        int intModalidad = 1;

        String strUsuariocreado = "";

        String strPromo = "0";

        if (request.getParameter("promo") != null) {

            strPromo = request.getParameter("promo"); //si es 1 = es promocion
        }

        // si no se a pagado ninguna vez, envia correos, de lo contrario, ya mando reservaciones y accesos
        System.out.println("################ VALIDA SI VIENE PAGADO O NO");
        if (strPagado.equals("0")) {
            System.out.println("################ NO ESTA PAGADO");
            try {
                //obtenemos información de la venta y vendedor
                strSql = "select TIPO_DOC, FAC_ID, ROUND(FAC_TOTAL,2) as TOTAL, view_ventasglobales.CT_ID, FAC_RAZONSOCIAL, FAC_US_ALTA, FAC_PAGADO, nombre_usuario, "
                        + "SMTP, SMTP_PASS, SMTP_US, SMTP_USASTLS, SMTP_USATLS "
                        + "from view_ventasglobales, usuarios "
                        + "where FAC_US_ALTA = id_usuarios and ID_MASTER = " + request.getParameter("IDMOV") + " AND FAC_ID = " + strIdVta;

                rs = oConn.runQuery(strSql, true);
                while (rs.next()) {
                    //obtiene la información general de la venta
                    strTipoVtaDoc = rs.getString("TIPO_DOC");
                    intIdVta = rs.getInt("FAC_ID");
                    strPrecioVta = rs.getString("TOTAL");
                    strCT_ID = rs.getString("CT_ID");
                    strRazonSocial = rs.getString("FAC_RAZONSOCIAL");
                    strFacUsAlta = rs.getString("FAC_US_ALTA"); //ejecutivo
                    strNombre = rs.getString("nombre_usuario");
                    strServidor = rs.getString("SMTP");
                    strPassword = rs.getString("SMTP_PASS");
                    strCorreoUsuario = rs.getString("SMTP_US");
                    intSTLS = rs.getInt("SMTP_USASTLS");
                    intTLS = rs.getInt("SMTP_USATLS");
                    intPagado = rs.getInt("FAC_PAGADO");

                }
                rs.close();

                //obtenemos información de la partida y participantes(concatenados)
                strSql = "select CV_ID_M, CV_CURSO, CV_ID_CURSO, CV_TIPO_CURSO, "
                        + "(select GROUP_CONCAT(CONCAT_WS('', CONCAT(CP_TITULO, ' ', CP_NOMBRE ,' ', CP_APPAT,' ', CP_APMAT),'<br>')) from cofide_participantes "
                        + "where CP_ID_M = CV_ID_M and CP_ID_CURSO = CV_ID_CURSO and CP_TIPO_CURSO = CV_TIPO_CURSO) as participantes "
                        + "from cofide_venta where CV_ID_M = " + request.getParameter("IDMOV");
                rs = oConn.runQuery(strSql, true);
                while (rs.next()) {
                    //obtiene los detalles del curso y los participantes en general por articulo
                    strNombreCurso = rs.getString("CV_CURSO");
                    strIdCurso = rs.getString("CV_ID_CURSO");
                    intTipoCurso = rs.getInt("CV_TIPO_CURSO");
//                    strParticipantes = rs.getString("participantes");
//                    strParticipantes = getParticipantes(oConn, rs.getString("CV_ID_M"), rs.getString("CP_ID_CURSO"));

                    //obtenemos información de los participantes(destino de los mail)
                    strSql = "select CP_NOMBRE, CP_APPAT, CP_APMAT, CP_TITULO, CP_CORREO, CONTACTO_ID "
                            + "from cofide_participantes "
                            + "where CP_ID_M = " + request.getParameter("IDMOV") + " and CP_ID_CURSO = " + strIdCurso + " and CP_TIPO_CURSO = " + intTipoCurso;
                    ResultSet rsx = oConn.runQuery(strSql, true);
                    while (rsx.next()) {
                        strParticipantes = getParticipantes(oConn, request.getParameter("IDMOV"), strIdCurso);
                        System.out.println("\n\n ##### Participante: [ 2-venta ] " + strParticipantes + " Y CURSO " + strIdCurso);
                        //envia correos con la informaicón de cada participante
                        strCorreo = rsx.getString("CP_CORREO");
                        intContactoID = rsx.getInt("CONTACTO_ID");
                        strNombreContacto = rsx.getString("CP_NOMBRE");
                        strApellidoPat = rsx.getString("CP_APPAT");
                        strApellidoMat = rsx.getString("CP_APMAT");
                        strTitulo = rsx.getString("CP_TITULO");

                        //obtiene modalidad del curso adquirido
                        intModalidad = getModalidadCurso(oConn, strIdCurso); //obtenemos la modalidad

                        //crea usuario en la web, otiene accesos
                        strUsuariocreado = createUser(strCorreo, Integer.parseInt(strCT_ID), strNombreContacto, strApellidoPat, strApellidoMat, strTitulo, oConn, intIdVta, intTipoCurso, intModalidad, strTipoVtaDoc, strIdCurso);

                        //si se crteo correctamente el usuario, va a mandar los email
                        if (strUsuariocreado.equals("OK")) {

                            if (request.getParameter("venta_nueva").equals("1")) { // envia reservaciones unicamente si es una venta nueva

                                //la venta es por primera vez, y no tiene pago, para enviasr reservaciones y no es promosion
                                if (strPromo.equals("0")) {

                                    //envia resevraciones de todos y cada curso y participante
                                    if (intTipoCurso == 4) {

                                        //envia reservación COFIDEnet
                                        System.out.println("################################# MANDA RESERVACIÓN NET");
                                        mg.MailCofideNet(oConn, strCorreo, strParticipantes, strRazonSocial, Integer.parseInt(strIdCurso), Integer.parseInt(strFacUsAlta), intTipoCurso, strPrecioVta);

                                    } else {
                                        //envia reservaciones presencial, en linea, video curso
                                        System.out.println("################################# MANDA RESERVACIÓN");
                                        mg.MailReservacion(oConn, strCorreo, strParticipantes, strRazonSocial, Integer.parseInt(strIdCurso), Integer.parseInt(strFacUsAlta), intTipoCurso, strPrecioVta);

                                    }

                                }
                            }
                            //envia accesos y materiales de los cursos y no es promosion
                            System.out.println("################ MANDA ACCESOS Y/O MATERIAL, SE ACABA DE PAGAR");
                            if (intPagado == 1 && strPromo.equals("0")) {

                                if (intTipoCurso == 1) {
                                    //envía descarga de material
                                    System.out.println("################################# MANDA DESCARGA DE MAIL");
                                    mg.MailDescargaMaterial(oConn, strCorreo, strParticipantes, strRazonSocial, Integer.parseInt(strIdCurso), Integer.parseInt(strFacUsAlta), intTipoCurso, strPrecioVta);

                                }
                                if (intTipoCurso == 2 || intTipoCurso == 3) {
                                    //envía accesos OnLine
                                    System.out.println("################################# MANDA ACCESO ONLINE: USUARIO: " + mg.getStrUsuario() + " / PASSWORD: " + mg.getStrContraseña());
                                    mg.MailAccesoOnline(oConn, strCorreo, strParticipantes, strRazonSocial, Integer.parseInt(strIdCurso), Integer.parseInt(strFacUsAlta), intTipoCurso, strPrecioVta);

                                }
                                if (intTipoCurso == 4) {
                                    //envía accesos de COFIDEnet
                                    System.out.println("################################# MANDA ACCESO NET: USUARIO: " + mg.getStrUsuario() + " / PASSWORD: " + mg.getStrContraseña());
                                    mg.MailAccesoCofideNet(oConn, strCorreo, strParticipantes, strRazonSocial, Integer.parseInt(strIdCurso), Integer.parseInt(strFacUsAlta), intTipoCurso, strPrecioVta);

                                }
                            } else {
                                System.out.println("################ NO ENVIA ACCESOS Y/O MATERIAL, NO SE HA PAGADO");
                            }
                            //promosion de puntos de lealtad
                            if (strPromo.equals("1")) {

                                //si es una venta por promosion, le va a restar los 1000 puntos por movimiento
                                System.out.println("################################# VA A CONSUMIR SUS PUNTOS CON LA PROMOCION");
                                getAndSetPuntos(oConn, intContactoID, Integer.parseInt(strIdCurso), 1);
                                System.out.println("################################# MANDA RESERVACIÓN");
                                mg.MailReservacion(oConn, strCorreo, strParticipantes, strRazonSocial, Integer.parseInt(strIdCurso), Integer.parseInt(strFacUsAlta), intTipoCurso, strPrecioVta);
                                System.out.println("################################# MANDA DESCARGA DE MAIL DE LA PROMOCIÓN");
                                mg.MailDescargaMaterial(oConn, strCorreo, strParticipantes, strRazonSocial, Integer.parseInt(strIdCurso), Integer.parseInt(strFacUsAlta), intTipoCurso, strPrecioVta);

                            } else {
                                //asigna puntos con base a la venta presencial y pagada
                                if (intPagado == 1 && intTipoCurso == 1 && intReenvio == 0) {

                                    //si no es una promosion, le va a sumar 200 pts por cada curso presecial
                                    System.out.println("################################# SE LE VAN A ASIGNAR PUNTOS POR SU COMPRA");
                                    getAndSetPuntos(oConn, intContactoID, Integer.parseInt(strIdCurso), 0);

                                }

                            }

                        }

                    }
                    rsx.close();
                }
                rs.close();

            } catch (SQLException sql) {
                System.out.println("Hubó un problema al obtener la información de la venta y sus detalles: [SQL] " + sql.getMessage());
            } catch (ParseException parse) {
                System.out.println("Hubó un problema al obtener la información de la venta y sus detalles: [PARSE] " + parse.getMessage());
            }
        } else {
            //ya esta pagado, ya no envia accesos.
            strUsuariocreado = "OK";
        }
        System.out.println("################ TERMINA PROCESO DE SEND");
        return strUsuariocreado;
    }

    /**
     * limpiar ventas en la web
     */
    public void cleanWebMICOFIDE(int intIdVenta, String stridWeb, String stridCurso) {
        //limpia la venta en la web, para los nuevos usuarios
        Conexion oConnMGV = new Conexion();
        oConnMGV.setStrNomPool("jdbc/COFIDE");
        oConnMGV.open();
        System.out.println("##### LIMPIA LA VENTA EN LA WEB #####");
        String strDelete = "delete from ventas_crm where tkt_id_crm  = " + intIdVenta + " and id_usr_web = " + stridWeb + " and id_evento = " + stridCurso;
        oConnMGV.runQueryLMD(strDelete);
        if (oConnMGV.isBolEsError()) {
            System.out.println("######## ERROR al limpiar la venta: " + oConnMGV.getStrMsgError() + " ########");
        }
        oConnMGV.close();
    }

    /**
     * crea el usuario en la web para sus accesos
     *
     */
    public String createUser(String strCorreo, int intCT_ID, String strNombre, String strApPat, String strApMat, String strTitulo, Conexion oConn, int intIDVta, int intTipoCurso, int intMod, String strTipoDoc, String strIdCurso) {

        Conexion oConnMGV = new Conexion();
        oConnMGV.setStrNomPool("jdbc/COFIDE");
        oConnMGV.open();

        String strRespuesta = "";

        //Creamos el usuario en la BAse COFIDE MGV
        String strIdNewUser = mg.createUsuarioNuevoCurso(strCorreo, intCT_ID, strNombre, strApPat + " " + strApMat, strTitulo);

        //Limpia la venta anterior, con este id para crearlo nuevamente, con el id de la venta, el id_web del cliente y el id del curso
        cleanWebMICOFIDE(intIDVta, strIdNewUser, strIdCurso);

        //GUARDAMOS LA VENTA EN LA WEB
        System.out.println("######################## guardamos vta en la web #############################");
        if (!strCorreo.equals("")) {
            // si realizo una venta, se reflejara en la web            
            mg.VtaWeb(oConn, intIDVta, strIdNewUser, intTipoCurso, intMod, strTipoDoc, strIdCurso);

        }
        System.out.println("######################## ya la guardo #############################");
        //crea usuarios y los asigna en el correo
        String strUser = "";
        String strPassWordMGV = "";

        try {

            String strSql = "select usr,psw from cat_usr_w where id_usr_w = " + strIdNewUser;
            ResultSet rs = oConnMGV.runQuery(strSql, true);
            while (rs.next()) {

                strPassWordMGV = rs.getString("psw");
                strUser = rs.getString("usr");

            }
            rs.close();

        } catch (SQLException ex) {
            System.out.println("GET PASSWORD COFIDE MGV: " + ex.getLocalizedMessage());
        }
        //asignamos los valores a una variable
        mg.setStrUsuario(strUser);
        mg.setStrContraseña(strPassWordMGV);

        System.out.println("\n################## " + strUser + " ###################\n");
        System.out.println("\n################## " + strPassWordMGV + " ###################\n");

        if (!strUser.equals("") && !strPassWordMGV.equals("")) {

            strRespuesta = "OK";

        }
        oConnMGV.close();

        return strRespuesta;
    }

    /**
     * obtener el ID del cliente a quien se le hizo la venta
     *
     * @param oConn conexión
     * @param strFac_id id de la venta
     * @return id del cliente
     */
    public int getCT_ID(Conexion oConn, String strFac_id) {
        int intCT_ID = 0;
        String strSQL = "select CT_ID from view_ventasglobales where FAC_ID = " + strFac_id;
        try {
            ResultSet rs = oConn.runQuery(strSQL, true);
            while (rs.next()) {
                intCT_ID = rs.getInt("CT_ID");
            }
            rs.close();
        } catch (SQLException sql) {
            System.out.println("Hubó un error al recuperar el id del cliente: " + sql.getMessage());
        }
        return intCT_ID;
    }

    /**
     * ENVIA EL TICKET AL EJECUTIVO
     *
     * @param oConn
     * @param intIdUsuario
     * @return
     */
    public String strGetCorreoTicket(Conexion oConn, int intIdUsuario) {
        String strCorreoTicket = "";
        System.out.println("########################## OBTENEMOS EL CORREO DEL EJECUTIVO PARA ENVIARLE EL TICKET");

        String strSql = "select EMAIL from usuarios where id_usuarios = " + intIdUsuario;
        strCorreoTicket = "";

        try {
            ResultSet rs = oConn.runQuery(strSql, true);

            while (rs.next()) {

                if (!rs.getString("EMAIL").equals("")) {

                    strCorreoTicket = rs.getString("EMAIL");

                }
            }
            rs.close();

        } catch (SQLException ex) {

            System.out.println("strGetCorreoTicket [" + ex.getLocalizedMessage() + "]");
        }

        return strCorreoTicket;
    }//Fin strGetCorreoTicket

    public String getParticipantes(Conexion oConn, String strIDMaster, String stridCurso) {
        String strParticipantes = "";
        String strSql = "select CONCAT(CP_TITULO, ' ', CP_NOMBRE ,' ', CP_APPAT,' ', CP_APMAT) as participantes from cofide_participantes  where CP_ID_M = " + strIDMaster + " and CP_ID_CURSO = " + stridCurso;
        try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                strParticipantes += rs.getString("participantes") + " <br>";
            }
            rs.close();
        } catch (SQLException sql) {
            System.out.println(" error al obtener participantes : [" + sql.getSQLState() + " ]");
        }
        System.out.println("\n\n ##### Participante: [ 1-consulta ] " + strParticipantes);
        return strParticipantes;
    }
}
