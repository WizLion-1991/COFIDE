/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.especiales.cofide;

import com.mx.siweb.erp.especiales.cofide.entidades.CatUserW;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Utilerias.Fechas;
import comSIWeb.Utilerias.Mail;
import comSIWeb.Utilerias.Texto_UppLow;
import comSIWeb.Utilerias.generateData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author juliocesar
 */
public class COFIDE_Mail_cursos {

    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(COFIDE_Mail_cursos.class.getName());
// <editor-fold defaultstate="collapsed" desc="Constructores">
    Fechas fec = new Fechas();
    String strUsuario = "";
    String strContraseña = "";
    Mail mail = new Mail();
    CRM_MailMasivo crmm = new CRM_MailMasivo();
    CRM_Envio_Template tmp = new CRM_Envio_Template();

    public COFIDE_Mail_cursos() {
    }
// </editor-fold>
// <editor-fold defaultstate="collapsed" desc="Metodos">
    // <editor-fold defaultstate="collapsed" desc="MAIL CURSOS">

    public String getStrUsuario() {
        return strUsuario;
    }

    public void setStrUsuario(String strUsuario) {
        this.strUsuario = strUsuario;
    }

    public String getStrContraseña() {
        return strContraseña;
    }

    public void setStrContraseña(String strContraseña) {
        this.strContraseña = strContraseña;
    }

// <editor-fold defaultstate="collapsed" desc="OBTIENE EL TEMPLATE">
    public String[] getMailTemplate(Conexion oConn, String strNom) { //obtenemos la informacion que se encuentre en el template
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
            log.debug(ex);
        }
        return listValores;
    }
// </editor-fold>

// <editor-fold defaultstate="collapsed" desc="MAIL GROUP">
    /**
     *
     * @param oConn
     * @param intIDEjecutivo id del ejecutivo
     * @param strCRMEmail cuenta de correo
     * @param strAsunto asunto del correo
     * @param intPreview es vista previa? si = 1, no = 0
     * @param strPlantilla plantilla
     * @param intIdPlantilla vista de la plantilla
     * @param intIdCte id del cleinte
     * @param strMes es mensual? si = 1, no = 0
     * @param strCurso id del curso
     * @return
     * @throws SQLException
     * @throws ParseException
     */
    public String MailGroup(Conexion oConn, int intIDEjecutivo, String strCRMEmail,
            String strAsunto, int intPreview, String strPlantilla, int intIdPlantilla,
            int intIdCte, String strMes, String strCurso) throws SQLException, ParseException {
        int intEjecutivo = intIDEjecutivo;
        String strCorreo = strCRMEmail;
        int intIdPlantillaD = intIdPlantilla; //id del evento, vista previa        
        String strTemplate = strPlantilla;
        String strResp = "";
        String strFechaActual = fec.getFechaActual();
//        CRM_Envio_Template tmp = new CRM_Envio_Template();

        if (mail.isEmail(strCorreo)) {//es formato de correo
            if (strTemplate.equals("PERSONALIZADO")) {
                //funcion de correo de 3 meses
                if (crmm.CteMailMensual(oConn, intIdCte, strMes)) {
                    strResp = tmp.Cofide3Cursos(oConn, intEjecutivo, strCorreo, strAsunto, 0, strTemplate, intIdPlantillaD);
                }
            }
            if (strTemplate.equals("SEDES")) {
                if (crmm.CteMailMensual(oConn, intIdCte, strMes)) {
                    String strSql = "select CT_SEDE from vta_cliente where CT_ID = " + intIdCte;
                    ResultSet rs = oConn.runQuery(strSql, true);
                    while (rs.next()) {
                        System.out.println("sede: " + rs.getString("CT_SEDE"));
                        strResp = tmp.CofideSede(oConn, intEjecutivo, strCorreo, rs.getString("CT_SEDE"), strAsunto, 0, strTemplate, intIdPlantillaD);
                    }
                    rs.close();
                }
            }
            if (strTemplate.equals("RECURRENTE")) {
                if (crmm.CteMailMensual(oConn, intIdCte, strMes)) {
                    strResp = tmp.CofideRecurrente(oConn, intEjecutivo, strCorreo, strCurso, strAsunto, 0, strTemplate, intIdPlantillaD);
                }
            }
            if (strTemplate.equals("MES")) {
                strResp = tmp.CofideMensual(oConn, intEjecutivo, strCorreo, strAsunto, 0, strTemplate, intIdPlantillaD);
            }
            if (strTemplate.equals("DIPLOMADO")) {
                if (crmm.CteMailMensual(oConn, intIdCte, strMes)) {
                    strResp = tmp.CofideDiplomado(oConn, intEjecutivo, strCorreo, strAsunto, 0, strTemplate, intIdPlantillaD);
                }
            }
            if (strTemplate.equals("SEMINARIO")) {
                //funcion de correo de 3 meses
                if (crmm.CteMailMensual(oConn, intIdCte, strMes)) {
                    strResp = tmp.CofideDiplomado(oConn, intEjecutivo, strCorreo, strAsunto, 0, strTemplate, intIdPlantillaD);
                }
            }
            if (strTemplate.equals("DIPLOMADO_MOD")) {
                if (crmm.CteMailMensual(oConn, intIdCte, strMes)) {
                    strResp = tmp.CofideDipMod(oConn, intEjecutivo, strCorreo, strAsunto, 0, strTemplate, intIdPlantillaD);
                }
            }
        }
        return strResp;
    }
// </editor-fold>
// <editor-fold defaultstate="collapsed" desc="MAIL RESERVACION DE CURSO">

    /**
     *
     * @param oConn
     * @param strMailCte el correod e cada uno
     * @param strParticipante participante(s)
     * @param strRazonsocial
     * @param intIdCurso el id del curso que se obtendra con el split
     * @param intIDEjecutivo el id del ejecutivo quien manda la información
     * @param intTipoC tipo de curso vendido, online, retransmision, presencial
     * @param strPrecioVta precio con el que se genero la venta
     * @return
     * @throws SQLException
     * @throws java.text.ParseException
     */
    public String MailReservacion(Conexion oConn, String strMailCte, String strParticipante, String strRazonsocial, int intIdCurso, int intIDEjecutivo, int intTipoC, String strPrecioVta) throws SQLException, ParseException {
//Objetenemso los datos dle curso
        System.out.println("############################ INICIA EL ENVIO DE RESERVACIÓN: \nCORREO:" + strMailCte + " \nRAZÓN SOCIAL: " + strRazonsocial);
//        CRM_Envio_Template tmp = new CRM_Envio_Template();
        String strNombre = ""; //NOMBRE DEL CURSO
        String strFechaIni = "";
        String strHhrsInicio = "";
        String strDuracion = "";
        String strPrecio = "";
        int intTipoCurso = 1;
        String strIdWEb = "";
        String strEsSeminario = "";
        String strEsDiplomado = "";
        String strLigaCurso = "";
        String strFechaOk = "";
        String strSede = "";
        String strOnline = "";
        String strModalidad = "";
        strPrecioVta = tmp.setPrecioOk(strPrecioVta);
        SimpleDateFormat conver = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat format = new SimpleDateFormat("E. dd MMM,YYYY", new Locale("es", "MX"));

        String strSqlCurso = "select CC_ID_WEB, CC_CURSO_ID, CC_NOMBRE_CURSO, CC_FECHA_INICIAL, "
                + "CC_HR_EVENTO_INI, (CC_DURACION_HRS + CC_DURACION_HRS2 + CC_DURACION_HRS3) as Duracion, CC_PRECIO_PRES, CC_IVA_PRES, CC_PRECIO_ON, CC_IVA_ON, CC_PRECIO_VID, CC_IVA_VID, "
                + "CC_ID_WEB, CC_IS_DIPLOMADO, CC_IS_SEMINARIO, CC_IS_ONLINE,CC_IS_VIDEO, "
                + "(select CSH_SEDE from cofide_sede_hotel where CSH_ID = CC_SEDE_ID) as sede  "
                + "from cofide_cursos where cc_curso_id = " + intIdCurso;
        ResultSet rsCurso = oConn.runQuery(strSqlCurso, true);
        while (rsCurso.next()) {
            strNombre = rsCurso.getString("CC_NOMBRE_CURSO");
            strFechaIni = rsCurso.getString("CC_FECHA_INICIAL");
            strHhrsInicio = rsCurso.getString("CC_HR_EVENTO_INI");
            strDuracion = rsCurso.getString("Duracion");
            if (intTipoC == 1) { //presencial
                if (!rsCurso.getString("CC_IVA_PRES").equals("")) {
                    strPrecio = tmp.setPrecioOk(rsCurso.getString("CC_IVA_PRES"));
                }
                strModalidad = "PRESENCIAL";
            }
            if (intTipoC == 2) { //en linea
                if (!rsCurso.getString("CC_IVA_ON").equals("")) {
                    strPrecio = tmp.setPrecioOk(rsCurso.getString("CC_IVA_ON"));
                }
//                strPrecio = rsCurso.getString("CC_IVA_ON");
                strModalidad = "EN LÍNEA";
            }
            if (intTipoC == 3) { //retransmisión
                if (!rsCurso.getString("CC_IVA_VID").equals("")) {
                    strPrecio = tmp.setPrecioOk(rsCurso.getString("CC_IVA_VID"));
                }
//                strPrecio = rsCurso.getString("CC_IVA_VID");
                strModalidad = "VIDEO CURSO";
            }
            strIdWEb = rsCurso.getString("CC_CURSO_ID");
            strEsSeminario = rsCurso.getString("CC_IS_SEMINARIO");
            strEsDiplomado = rsCurso.getString("CC_IS_DIPLOMADO");
            strOnline = rsCurso.getString("CC_IS_ONLINE");
            strSede = rsCurso.getString("sede");
//            strSede = getHtml(strSede);

            Date date = conver.parse(strFechaIni);
            strFechaOk = format.format(date);

            if (strEsSeminario.equals("1")) {
                intTipoCurso = 3;
            }
            if (strEsDiplomado.equals("1")) {
                intTipoCurso = 2;
            }
//                strLigaCurso = fec.FormateaDDMMAAAA(strFechaIni, "") + strIdWEb + intTipoCurso;
            if (rsCurso.getString("CC_ID_WEB").equals("")) {
                strLigaCurso = "http://cofide.org/producto/";
            } else {
                strLigaCurso = rsCurso.getString("CC_ID_WEB");
            }
        }
        rsCurso.close();
        //Objetenemso los datos dle curso
        //obtenemos el contato del agente
        String strAgente = "";
        String strNumero = "";
        String strCorreo = "";
        String strExt = "";
        String strPass = "";
        String strServer = "";
        String strPort = "";
        String strTLS = "";
        String strSTLS = "";
        String strSqlEjecutivo = "select id_usuarios, nombre_usuario, TELEFONO, NUM_EXT,"
                + "SMTP_US, SMTP_PASS, SMTP, PORT, SMTP_USATLS, SMTP_USASTLS "
                + "from usuarios where id_usuarios = " + intIDEjecutivo;
        ResultSet rsAgente = oConn.runQuery(strSqlEjecutivo, true);
        while (rsAgente.next()) {
            strAgente = rsAgente.getString("nombre_usuario");
            strNumero = rsAgente.getString("TELEFONO");
            strCorreo = rsAgente.getString("SMTP_US");
            strExt = rsAgente.getString("NUM_EXT");
            strPass = rsAgente.getString("SMTP_PASS");
            strServer = rsAgente.getString("SMTP");
            strPort = rsAgente.getString("PORT");
            strTLS = rsAgente.getString("SMTP_USATLS");
            strSTLS = rsAgente.getString("SMTP_USASTLS");
        }
        rsAgente.close();
        //obtenemos el contato del agente
        String strResp = "OK";
        String strNomTemplate = "RESERVA"; //obtenemos el template del mail para le supervisor
        String[] lstMail = getMailTemplate(oConn, strNomTemplate);
        String strMsgMail = lstMail[1];
        //  Si estan llenos todos los datos mandamos el mail
        if (!strServer.equals("")
                && !strCorreo.equals("")
                && !strPass.equals("")) {
            //armamos el mail
            Mail mail = new Mail();
            mail.setBolDepuracion(false);
            //Activamos envio de acuse de recibo
            mail.setBolAcuseRecibo(false);
            //Obtenemos los usuarios a los que mandaremos el mail
            String strLstMail = "";
            //Validamos si el mail del cliente es valido.
            System.out.println("\n\n###correo del cliente: " + strMailCte + "\n\n###");
            if (!strMailCte.equals("")) {
                if (mail.isEmail(strMailCte)) {
                    strLstMail = strMailCte;
                    strLstMail += "," + strCorreo;
                } else {
                    strLstMail = strCorreo;
                }
            } else {
                strLstMail = strCorreo;
            }
            System.out.println("######################### correso destinatarios:: " + strLstMail + " ###################");
            //Mandamos mail si hay usuarios
            if (!strLstMail.equals("")) {
                strMsgMail = strMsgMail.replace("%FECHA%", getHtml(strFechaOk));
                strMsgMail = strMsgMail.replace("%CURSO%", getHtml(strNombre));
                strMsgMail = strMsgMail.replace("%HR_INI%", strHhrsInicio);
                strMsgMail = strMsgMail.replace("%DURACION%", strDuracion);
                strMsgMail = strMsgMail.replace("%SEDE%", getHtml(strSede));
                strMsgMail = strMsgMail.replace("%PRECIO%", strPrecioVta);
//                strMsgMail = strMsgMail.replace("%PRECIO%", strPrecio);
                strMsgMail = strMsgMail.replace("%RAZONSOCIAL%", getHtml(strRazonsocial));
                strMsgMail = strMsgMail.replace("%PARTICIPANTE%", getHtml(strParticipante));
                strMsgMail = strMsgMail.replace("%MODALIDAD%", getHtml(strModalidad));
                strMsgMail = strMsgMail.replace("%ID_CURSO_WEB%", strLigaCurso);
                strMsgMail = strMsgMail.replace("%WEB_USUARIO%", getStrUsuario());
                strMsgMail = strMsgMail.replace("%WEB_PASSWORD%", getStrContraseña());
                strMsgMail = strMsgMail.replace("%AGENTE%", getHtml(strAgente));
                strMsgMail = strMsgMail.replace("%EXTENSION%", strNumero);
                strMsgMail = strMsgMail.replace("%CORREO%", strCorreo);
                //Establecemos parametros
                mail.setUsuario(strCorreo);
                mail.setContrasenia(strPass);
                mail.setHost(strServer);
                mail.setPuerto(strPort);
                mail.setAsunto(lstMail[0].replace("%CC_NOMBRE_CURSO%", strModalidad + ": " + strNombre));
                mail.setDestino(strLstMail);
                mail.setMensaje(strMsgMail);
//                System.out.println("#########################cuerpo: \n" + strMsgMail);
                mail.setBolDepuracion(false);
                //Adjuntamos XML y PDF
                if (strTLS.equals("1")) {
                    mail.setBolUsaTls(true);
                }
                if (strSTLS.equals("1")) {
                    mail.setBolUsaStartTls(true);
                }
//                mail.sendMail();
                boolean bol = mail.sendMail();
                if (!bol) {
                    strResp = "Fallo el envio del Mail.";
                } else {
                    System.out.println("############ SE ENVIO EL CORREO ############");
                }
            }
        }
        System.out.println("############################ TERMINO EL ENVIO DE RESERVACIÓN: \nCORREO:" + strMailCte + "\nEJECUTIVO: " + strCorreo);
        return strResp;
    }
//reservación NET

    public void MailCofideNet(Conexion oConn, String strMailCte, String strParticipante, String strRazonsocial, int intIdCurso, int intIDEjecutivo, int intTipoC, String strPrecioVta) {
        //Objetenemso los datos de la Membresia
        System.out.println("############################ INICIA EL ENVIO DE COFIDE.NET: \nCORREO:" + strMailCte + " \nRAZÓN SOCIAL: " + strRazonsocial);
        String strNombre = ""; //NOMBRE DEL CURSO
        String strFechaIni = "";
        String strDuracion = "";
        String strPrecio = "";
        int intTipoCurso = 1;
        String strIdWEb = "";
        String strLigaCurso = "";
        String strFechaOk = "";
        String strSede = "";
        String strOnline = "";
        String strModalidad = "";
        strPrecioVta = tmp.setPrecioOk(strPrecioVta);
        SimpleDateFormat conver = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat format = new SimpleDateFormat("E. dd MMM,YYYY", new Locale("es", "MX"));

        String strSqlCurso = "select * from cofide_membresia where CM_ID = " + intIdCurso;

        try {
            ResultSet rsCurso = oConn.runQuery(strSqlCurso, true);
            while (rsCurso.next()) {
                strNombre = rsCurso.getString("CM_DESCRIPCION");
                strFechaIni = fec.getFechaActual();
                strDuracion = rsCurso.getString("CM_PERIODICIDAD");
                strIdWEb = rsCurso.getString("CM_ID");
            }
            rsCurso.close();
        } catch (SQLException ex) {
            log.error("Error MailCofideNet1.[" + ex.getLocalizedMessage() + "]");
        }

        //obtenemos el contato del agente
        String strAgente = "";
        String strNumero = "";
        String strCorreo = "";
        String strExt = "";
        String strPass = "";
        String strServer = "";
        String strPort = "";
        String strTLS = "";
        String strSTLS = "";
        try {
            String strSqlEjecutivo = "select id_usuarios, nombre_usuario, TELEFONO, NUM_EXT,"
                    + "SMTP_US, SMTP_PASS, SMTP, PORT, SMTP_USATLS, SMTP_USASTLS "
                    + "from usuarios where id_usuarios = " + intIDEjecutivo;
            ResultSet rsAgente = oConn.runQuery(strSqlEjecutivo, true);
            while (rsAgente.next()) {
                strAgente = rsAgente.getString("nombre_usuario");
                strNumero = rsAgente.getString("TELEFONO");
                strCorreo = rsAgente.getString("SMTP_US");
                strExt = rsAgente.getString("NUM_EXT");
                strPass = rsAgente.getString("SMTP_PASS");
                strServer = rsAgente.getString("SMTP");
                strPort = rsAgente.getString("PORT");
                strTLS = rsAgente.getString("SMTP_USATLS");
                strSTLS = rsAgente.getString("SMTP_USASTLS");
            }
            rsAgente.close();
        } catch (SQLException ex) {
            log.error("Error MailCofideNet2.[" + ex.getLocalizedMessage() + "]");
        }

        //obtenemos el contato del agente
        String strResp = "OK";
        String strNomTemplate = "MEMBRESIA_VENDIDA"; //obtenemos el template del mail para le supervisor
        String[] lstMail = getMailTemplate(oConn, strNomTemplate);
        String strMsgMail = lstMail[1];
        //  Si estan llenos todos los datos mandamos el mail
        if (!strServer.equals("")
                && !strCorreo.equals("")
                && !strPass.equals("")) {
            //armamos el mail
            Mail mail = new Mail();
            mail.setBolDepuracion(false);
            //Activamos envio de acuse de recibo
            mail.setBolAcuseRecibo(false);
            //Obtenemos los usuarios a los que mandaremos el mail
            String strLstMail = "";
            //Validamos si el mail del cliente es valido
            if (!strMailCte.equals("")) {
                if (mail.isEmail(strMailCte)) {
                    strLstMail = strMailCte;
                    strLstMail += "," + strCorreo;
                } else {
                    strLstMail = strCorreo;
                }
            } else {
                strLstMail = strCorreo;
            }
            System.out.println("######################### correso destinatarios:: " + strLstMail + " ###################");
            //Mandamos mail si hay usuarios
            if (!strLstMail.equals("")) {
                strMsgMail = strMsgMail.replace("%PRECIO%", strPrecioVta);
                strMsgMail = strMsgMail.replace("%RAZONSOCIAL%", getHtml(strRazonsocial));
                strMsgMail = strMsgMail.replace("%PARTICIPANTE%", getHtml(strParticipante));
                strMsgMail = strMsgMail.replace("%AGENTE%", getHtml(strAgente));
                strMsgMail = strMsgMail.replace("%EXTENSION%", strNumero);
                strMsgMail = strMsgMail.replace("%CORREO%", strCorreo);
                //Establecemos parametros
                mail.setUsuario(strCorreo);
                mail.setContrasenia(strPass);
                mail.setHost(strServer);
                mail.setPuerto(strPort);
                mail.setAsunto(lstMail[0].replace("%CC_NOMBRE_CURSO%", strModalidad + ": " + strNombre));
                mail.setDestino(strLstMail);
                mail.setMensaje(strMsgMail);
//                System.out.println("#########################cuerpo: \n" + strMsgMail);
                mail.setBolDepuracion(false);
                //Adjuntamos XML y PDF
                if (strTLS.equals("1")) {
                    mail.setBolUsaTls(true);
                }
                if (strSTLS.equals("1")) {
                    mail.setBolUsaStartTls(true);
                }
//                mail.sendMail();
                boolean bol = mail.sendMail();
                if (!bol) {
                    strResp = "Fallo el envio del Mail.";
                } else {
                    System.out.println("############ SE ENVIO EL CORREO ############");
                }
            }
        }
        System.out.println("############################ TERMINO EL ENVIO DE RESERVACIÃ“N: \nCORREO:" + strMailCte + "\nEJECUTIVO: " + strCorreo);
    }//Fin MailCofideNet

    public String MailDescargaMaterial(Conexion oConn, String strMailCte, String strParticipante, String strRazonsocial, int intIdCurso, int intIDEjecutivo, int intTipo, String strPrecioVta) throws SQLException, ParseException {
//Objetenemso los datos dle curso
        System.out.println("############################ INICIA EL ENVIO DE CONFIRMACIÓN PRESENCIAL: \nCORREO:" + strMailCte + " \nRAZÓN SOCIAL: " + strRazonsocial);
//        CRM_Envio_Template tmp = new CRM_Envio_Template();
        String strNombre = ""; //NOMBRE DEL CURSO
        String strFechaIni = "";
        String strHhrsInicio = "";
        String strDuracion = "";
        String strPrecio = "";
        int intTipoCurso = 1;
        String strIdWEb = "";
        String strEsSeminario = "";
        String strEsDiplomado = "";
        String strLigaCurso = "";
        String strFechaOk = "";
        String strSede = "";
        String strOnline = "";
        String strModalidad = "";
        String strURLMaterial = "";
        strPrecioVta = tmp.setPrecioOk(strPrecioVta);
        SimpleDateFormat conver = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat format = new SimpleDateFormat("E. dd MMM,YYYY", new Locale("es", "MX"));
        String strSqlCurso = "select CC_ID_WEB, CC_CURSO_ID, CC_NOMBRE_CURSO, CC_FECHA_INICIAL,CC_MATERIAL, "
                + "CC_HR_EVENTO_INI, (CC_DURACION_HRS + CC_DURACION_HRS2 + CC_DURACION_HRS3) as Duracion, CC_PRECIO_PRES,CC_IVA_PRES, CC_PRECIO_ON, CC_IVA_ON, CC_PRECIO_VID, CC_IVA_VID, "
                + "CC_ID_WEB, CC_IS_DIPLOMADO, CC_IS_SEMINARIO, CC_IS_ONLINE,"
                + "(select CSH_SEDE from cofide_sede_hotel where CSH_ID = CC_SEDE_ID) as sede  "
                + "from cofide_cursos where cc_curso_id = " + intIdCurso;
        ResultSet rsCurso = oConn.runQuery(strSqlCurso, true);
        while (rsCurso.next()) {
            strNombre = rsCurso.getString("CC_NOMBRE_CURSO");
            strFechaIni = rsCurso.getString("CC_FECHA_INICIAL");
            strHhrsInicio = rsCurso.getString("CC_HR_EVENTO_INI");
            strDuracion = rsCurso.getString("Duracion");
            strIdWEb = rsCurso.getString("CC_CURSO_ID");
            strEsSeminario = rsCurso.getString("CC_IS_SEMINARIO");
            strEsDiplomado = rsCurso.getString("CC_IS_DIPLOMADO");
            strOnline = rsCurso.getString("CC_IS_ONLINE");
            strSede = rsCurso.getString("sede");
            strURLMaterial = rsCurso.getString("CC_MATERIAL");
            if (intTipo == 1) { //presencial
                if (!rsCurso.getString("CC_IVA_PRES").equals("")) {
                    strPrecio = tmp.setPrecioOk(rsCurso.getString("CC_IVA_PRES"));
                }
//                strPrecio = rsCurso.getString("CC_IVA_PRES");
                strModalidad = "PRESENCIAL";
            }
            if (intTipo == 2) { //en linea
                if (!rsCurso.getString("CC_IVA_ON").equals("0")) {
                    strPrecio = tmp.setPrecioOk(rsCurso.getString("CC_IVA_ON"));
                }
//                strPrecio = rsCurso.getString("CC_IVA_ON");
                strModalidad = "EN LÍNEA";
            }
            if (intTipo == 3) { //retransmisión
                if (!rsCurso.getString("CC_IVA_VID").equals("0")) {
                    strPrecio = tmp.setPrecioOk(rsCurso.getString("CC_IVA_VID"));
                }
//                strPrecio = rsCurso.getString("CC_IVA_VID");
                strModalidad = "VIDEO CURSO";
            }
            Date date = conver.parse(strFechaIni);
            strFechaOk = format.format(date);

            if (strEsSeminario.equals("1")) {
                intTipoCurso = 3;
            }
            if (strEsDiplomado.equals("1")) {
                intTipoCurso = 2;
            }
//                strLigaCurso = fec.FormateaDDMMAAAA(strFechaIni, "") + strIdWEb + intTipoCurso;
            if (rsCurso.getString("CC_ID_WEB").equals("")) {
                strLigaCurso = "http://cofide.org/producto/";
            } else {
                strLigaCurso = rsCurso.getString("CC_ID_WEB");
            }
        }
        rsCurso.close();
        //Objetenemso los datos dle curso
        //obtenemos el contato del agente
        String strAgente = "";
        String strNumero = "";
        String strCorreo = "";
        String strExt = "";
        String strPass = "";
        String strServer = "";
        String strPort = "";
        String strTLS = "";
        String strSTLS = "";
        String strSqlEjecutivo = "select id_usuarios, nombre_usuario, TELEFONO, NUM_EXT,"
                + "SMTP_US, SMTP_PASS, SMTP, PORT, SMTP_USATLS, SMTP_USASTLS "
                + "from usuarios where id_usuarios = " + intIDEjecutivo;
        ResultSet rsAgente = oConn.runQuery(strSqlEjecutivo, true);
        while (rsAgente.next()) {
            strAgente = rsAgente.getString("nombre_usuario");
            strNumero = rsAgente.getString("TELEFONO");
            strCorreo = rsAgente.getString("SMTP_US");
            strExt = rsAgente.getString("NUM_EXT");
            strPass = rsAgente.getString("SMTP_PASS");
            strServer = rsAgente.getString("SMTP");
            strPort = rsAgente.getString("PORT");
            strTLS = rsAgente.getString("SMTP_USATLS");
            strSTLS = rsAgente.getString("SMTP_USASTLS");
        }
        rsAgente.close();
        //obtenemos el contato del agente
        String strResp = "OK";
        String strNomTemplate = "DESCARGA"; //obtenemos el template del mail para le supervisor
        String[] lstMail = getMailTemplate(oConn, strNomTemplate);
        String strMsgMail = lstMail[1];
        //  Si estan llenos todos los datos mandamos el mail
        if (!strServer.equals("")
                && !strCorreo.equals("")
                && !strPass.equals("")) {
            //armamos el mail
            Mail mail = new Mail();
            mail.setBolDepuracion(false);
            //Activamos envio de acuse de recibo
            mail.setBolAcuseRecibo(false);
            //Obtenemos los usuarios a los que mandaremos el mail
            String strLstMail = "";
            //Validamos si el mail del cliente es valido
            if (!strMailCte.equals("")) {
                if (mail.isEmail(strMailCte)) {
                    strLstMail = strMailCte;
                    strLstMail += "," + strCorreo;
                } else {
                    strLstMail = strCorreo;
                }
            } else {
                strLstMail = strCorreo;
            }
            System.out.println("######################### correso destinatarios:: " + strLstMail + " ###################");
            //Mandamos mail si hay usuarios
            if (!strLstMail.equals("")) {
                strMsgMail = strMsgMail.replace("%FECHA%", getHtml(strFechaOk));
                strMsgMail = strMsgMail.replace("%CURSO%", getHtml(strNombre));
                strMsgMail = strMsgMail.replace("%HR_INI%", strHhrsInicio);
                strMsgMail = strMsgMail.replace("%DURACION%", strDuracion);
                strMsgMail = strMsgMail.replace("%SEDE%", getHtml(strSede));
                strMsgMail = strMsgMail.replace("%PRECIO%", strPrecioVta);
//                strMsgMail = strMsgMail.replace("%PRECIO%", strPrecio);
                strMsgMail = strMsgMail.replace("%RAZONSOCIAL%", getHtml(strRazonsocial));
                strMsgMail = strMsgMail.replace("%PARTICIPANTE%", getHtml(strParticipante));
                strMsgMail = strMsgMail.replace("%MODALIDAD%", getHtml(strModalidad));
                strMsgMail = strMsgMail.replace("%ID_CURSO_WEB%", strLigaCurso);
                strMsgMail = strMsgMail.replace("%WEB_USUARIO%", getStrUsuario());
                strMsgMail = strMsgMail.replace("%WEB_PASSWORD%", getStrContraseña());
                strMsgMail = strMsgMail.replace("%URL_MATERIAL%", strURLMaterial);
                strMsgMail = strMsgMail.replace("%AGENTE%", getHtml(strAgente));
                strMsgMail = strMsgMail.replace("%EXTENSION%", strNumero);
                strMsgMail = strMsgMail.replace("%CORREO%", strCorreo);
                //Establecemos parametros
                mail.setUsuario(strCorreo);
                mail.setContrasenia(strPass);
                mail.setHost(strServer);
                mail.setPuerto(strPort);
                mail.setAsunto(lstMail[0].replace("%CC_NOMBRE_CURSO%", strNombre));
                mail.setDestino(strLstMail);
                mail.setMensaje(strMsgMail);
                mail.setBolDepuracion(false);
                //Adjuntamos XML y PDF
                if (strTLS.equals("1")) {
                    mail.setBolUsaTls(true);
                }
                if (strSTLS.equals("1")) {
                    mail.setBolUsaStartTls(true);
                }
//                mail.sendMail();
                boolean bol = mail.sendMail();
                if (!bol) {
                    strResp = "Fallo el envio del Mail.";
                } else {
                    strResp = "envio del Mail correcto.";
                    System.out.println("############ SE ENVIO EL CORREO ############");
                }
            }
        }
        System.out.println("############################ TERMINO EL ENVIO DE CONFIRMACIÓN PRESENCIAL: \nCORREO:" + strMailCte + "\nEJECUTIVO: " + strCorreo + "\nESTATUS: " + strResp);
        return strResp;
    }

    public String MailAccesoOnline(Conexion oConn, String strMailCte, String strParticipante, String strRazonsocial, int intIdCurso, int intIDEjecutivo, int intTipo, String strPrecioVta) throws SQLException, ParseException {
//Objetenemso los datos dle curso
        System.out.println("############################ INICIA EL ENVIO DE ACCESO ONLINE: \nCORREO:" + strMailCte + " \nRAZÓN SOCIAL: " + strRazonsocial);
//        CRM_Envio_Template tmp = new CRM_Envio_Template();
        String strNombre = ""; //NOMBRE DEL CURSO
        String strFechaIni = "";
        String strHhrsInicio = "";
        String strDuracion = "";
        String strPrecio = "";
        int intTipoCurso = 1;
        String strIdWEb = "";
        String strEsSeminario = "";
        String strEsDiplomado = "";
        String strLigaCurso = "";
        String strFechaOk = "";
        String strSede = "";
        String strOnline = "";
        String strModalidad = "";
        String strTitulo = "";
//        strParticipante = getHtml(strParticipante);
//        strRazonsocial = getHtml(strRazonsocial);
        String strURLMaterial = "";

        SimpleDateFormat conver = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat format = new SimpleDateFormat("E. dd MMM,YYYY", new Locale("es", "MX"));
        strPrecioVta = tmp.setPrecioOk(strPrecioVta);
        String strSqlCurso = "select CC_ID_WEB, CC_CURSO_ID, CC_NOMBRE_CURSO, CC_FECHA_INICIAL, CC_MATERIAL, "
                + "CC_HR_EVENTO_INI, (CC_DURACION_HRS + CC_DURACION_HRS2 + CC_DURACION_HRS3) as Duracion, CC_ID_WEB, CC_IS_DIPLOMADO, CC_IS_SEMINARIO, "
                + "CC_IS_ONLINE,CC_PRECIO_ON,CC_IVA_ON,CC_IS_VIDEO, CC_PRECIO_VID, CC_IVA_VID,"
                + "(select CSH_SEDE from cofide_sede_hotel where CSH_ID = CC_SEDE_ID) as sede  "
                + "from cofide_cursos where cc_curso_id = " + intIdCurso;
        ResultSet rsCurso = oConn.runQuery(strSqlCurso, true);
        while (rsCurso.next()) {
            strNombre = rsCurso.getString("CC_NOMBRE_CURSO");
            strFechaIni = rsCurso.getString("CC_FECHA_INICIAL");
            strHhrsInicio = rsCurso.getString("CC_HR_EVENTO_INI");
            strDuracion = rsCurso.getString("Duracion");
            if (intTipo == 2) { //online
                if (!rsCurso.getString("CC_IVA_ON").equals("0")) {
                    strPrecio = tmp.setPrecioOk(rsCurso.getString("CC_IVA_ON"));
                }
//                strPrecio = rsCurso.getString("CC_IVA_ON");
                strModalidad = "EN LÍNEA";
                strTitulo = "CURSO EN LÍNEA.";
            }
            if (intTipo == 3) { //video
                if (!rsCurso.getString("CC_IVA_VID").equals("0")) {
                    strPrecio = tmp.setPrecioOk(rsCurso.getString("CC_IVA_VID"));
                }
//                strPrecio = rsCurso.getString("CC_IVA_VID");
                strModalidad = "VIDEO CURSO";
                strTitulo = "VIDEO CURSO.";
            }
//            strPrecio = rsCurso.getString("CC_PRECIO_IVA");
            strIdWEb = rsCurso.getString("CC_CURSO_ID");
            strEsSeminario = rsCurso.getString("CC_IS_SEMINARIO");
            strEsDiplomado = rsCurso.getString("CC_IS_DIPLOMADO");
//            strOnline = rsCurso.getString("CC_IS_ONLINE");
            strSede = rsCurso.getString("sede");
//            strSede = getHtml(strSede);
            strURLMaterial = rsCurso.getString("CC_MATERIAL");

            Date date = conver.parse(strFechaIni);
            strFechaOk = format.format(date);

            if (strEsSeminario.equals("1")) {
                intTipoCurso = 3;
            }
            if (strEsDiplomado.equals("1")) {
                intTipoCurso = 2;
            }
//                strLigaCurso = fec.FormateaDDMMAAAA(strFechaIni, "") + strIdWEb + intTipoCurso;
            if (rsCurso.getString("CC_ID_WEB").equals("")) {
                strLigaCurso = "http://cofide.org/producto/";
            } else {
                strLigaCurso = rsCurso.getString("CC_ID_WEB");
            }
        }
        rsCurso.close();
        //Objetenemso los datos dle curso
        //obtenemos el contato del agente
        String strAgente = "";
        String strNumero = "";
        String strCorreo = "";
        String strExt = "";
        String strPass = "";
        String strServer = "";
        String strPort = "";
        String strTLS = "";
        String strSTLS = "";
        String strSqlEjecutivo = "select id_usuarios, nombre_usuario, TELEFONO, NUM_EXT,"
                + "SMTP_US, SMTP_PASS, SMTP, PORT, SMTP_USATLS, SMTP_USASTLS "
                + "from usuarios where id_usuarios = " + intIDEjecutivo;
        ResultSet rsAgente = oConn.runQuery(strSqlEjecutivo, true);
        while (rsAgente.next()) {
            strAgente = rsAgente.getString("nombre_usuario");
            strNumero = rsAgente.getString("TELEFONO");
            strCorreo = rsAgente.getString("SMTP_US");
            strExt = rsAgente.getString("NUM_EXT");
            strPass = rsAgente.getString("SMTP_PASS");
            strServer = rsAgente.getString("SMTP");
            strPort = rsAgente.getString("PORT");
            strTLS = rsAgente.getString("SMTP_USATLS");
            strSTLS = rsAgente.getString("SMTP_USASTLS");
        }
        rsAgente.close();
        //obtenemos el contato del agente
        String strResp = "OK";
        String strNomTemplate = "ACCESO_WEB"; //obtenemos el template del mail para le supervisor
        String[] lstMail = getMailTemplate(oConn, strNomTemplate);
        String strMsgMail = lstMail[1];
        //  Si estan llenos todos los datos mandamos el mail
        if (!strServer.equals("")
                && !strCorreo.equals("")
                && !strPass.equals("")) {
            //armamos el mail
            Mail mail = new Mail();
            mail.setBolDepuracion(false);
            //Activamos envio de acuse de recibo
            mail.setBolAcuseRecibo(false);
            //Obtenemos los usuarios a los que mandaremos el mail
            String strLstMail = "";
            //Validamos si el mail del cliente es valido
            if (!strMailCte.equals("")) {
                if (mail.isEmail(strMailCte)) {
                    strLstMail = strMailCte;
                    strLstMail += "," + strCorreo;
                } else {
                    strLstMail = strCorreo;

                }
            } else {
                strLstMail = strCorreo;
            }
            System.out.println("######################### correso destinatarios:: " + strLstMail + " ###################");
            //Mandamos mail si hay usuarios
            if (!strLstMail.equals("")) {
                strMsgMail = strMsgMail.replace("%TITULO%", getHtml(strTitulo));
                strMsgMail = strMsgMail.replace("%FECHA%", getHtml(strFechaOk));
                strMsgMail = strMsgMail.replace("%CURSO%", getHtml(strNombre));
                strMsgMail = strMsgMail.replace("%HR_INI%", strHhrsInicio);
                strMsgMail = strMsgMail.replace("%DURACION%", strDuracion);
                strMsgMail = strMsgMail.replace("%SEDE%", getHtml(strSede));
                strMsgMail = strMsgMail.replace("%PRECIO%", strPrecioVta);
//                strMsgMail = strMsgMail.replace("%PRECIO%", strPrecio);
                strMsgMail = strMsgMail.replace("%RAZONSOCIAL%", getHtml(strRazonsocial));
                strMsgMail = strMsgMail.replace("%PARTICIPANTE%", getHtml(strParticipante));
                strMsgMail = strMsgMail.replace("%MODALIDAD%", getHtml(strModalidad));
                strMsgMail = strMsgMail.replace("%ID_CURSO_WEB%", strLigaCurso);
                strMsgMail = strMsgMail.replace("%WEB_USUARIO%", getStrUsuario());
                strMsgMail = strMsgMail.replace("%WEB_PASSWORD%", getStrContraseña());
                strMsgMail = strMsgMail.replace("%URL_MATERIAL%", strURLMaterial);
                strMsgMail = strMsgMail.replace("%AGENTE%", getHtml(strAgente));
                strMsgMail = strMsgMail.replace("%EXTENSION%", strNumero);
                strMsgMail = strMsgMail.replace("%CORREO%", strCorreo);

                //Establecemos parametros
                mail.setUsuario(strCorreo);
                mail.setContrasenia(strPass);
                mail.setHost(strServer);
                mail.setPuerto(strPort);
                mail.setAsunto(lstMail[0].replace("%CC_NOMBRE_CURSO%", strNombre));
                mail.setDestino(strLstMail);
                mail.setMensaje(strMsgMail);
                mail.setBolDepuracion(false);
                //Adjuntamos XML y PDF
                if (strTLS.equals("1")) {
                    mail.setBolUsaTls(true);
                }
                if (strSTLS.equals("1")) {
                    mail.setBolUsaStartTls(true);
                }
//                mail.sendMail();
                boolean bol = mail.sendMail();
                if (!bol) {
                    strResp = "Fallo el envio del Mail.";
                } else {
                    System.out.println("############ SE ENVIO EL CORREO ############");
                    strResp = "Mandó el correo OKIS";
                }
            }
        }
        System.out.println("############################ TERMINO EL ENVIO DE ACCESO EN LINEA: \nCORREO:" + strMailCte + "\nEJECUTIVO: " + strCorreo + "\nESTATUS: " + strResp);
        return strResp;
    }
// </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="ACCESO DE COFIDE.NET">
    //acceso NET
    public void MailAccesoCofideNet(Conexion oConn, String strMailCte, String strParticipante, String strRazonsocial, int intIdCurso, int intIDEjecutivo, int intTipo, String strPrecioVta) throws ParseException {
        //Objetenemso los datos dle curso
        System.out.println("############################ INICIA EL ENVIO DE ACCESO ONLINE: \nCORREO:" + strMailCte + " \nRAZÓN SOCIAL: " + strRazonsocial);

        String strNombre = ""; //NOMBRE DEL CURSO
        String strFechaIni = "";
        String strHhrsInicio = "";
        String strDuracion = "";
        String strPrecio = "";
        int intTipoCurso = 1;
        String strIdWEb = "";
        String strEsSeminario = "";
        String strEsDiplomado = "";
        String strLigaCurso = "";
        String strFechaOk = "";
        String strFechaFin = "";
        String strFechaFinOk = "";
        String strSede = "";
        String strOnline = "";
        String strModalidad = "";
        String strTitulo = "COFIDE.NET";
        String strURLMaterial = "";

        SimpleDateFormat conver = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat format = new SimpleDateFormat("E. dd MMM,YYYY", new Locale("es", "MX"));
        strPrecioVta = tmp.setPrecioOk(strPrecioVta);

        String strSqlCurso = "select * from cofide_membresia where CM_ID = " + intIdCurso;

        //Obtenemos Informacion de la membresia
        try {
            ResultSet rsCurso = oConn.runQuery(strSqlCurso, true);
            while (rsCurso.next()) {
                strNombre = rsCurso.getString("CM_DESCRIPCION");
                strFechaIni = fec.getFechaActual();
                strDuracion = rsCurso.getString("CM_PERIODICIDAD");
                strIdWEb = rsCurso.getString("CM_ID");
            }
            rsCurso.close();
        } catch (SQLException ex) {
            log.error("Error MailAccesoCofideNet1.[" + ex.getLocalizedMessage() + "]");
        }
        int intPeriodo = Integer.parseInt(strDuracion);
        strFechaFin = fec.addFecha(strFechaIni, 2, intPeriodo);

        Date datefin = conver.parse(strFechaFin);
        strFechaFinOk = format.format(datefin);
        strFechaFinOk = strFechaFinOk.toUpperCase();

        Date date = conver.parse(strFechaIni);
        strFechaOk = format.format(date);
        strFechaOk = strFechaOk.toUpperCase();

//        System.out.println("######################## fecha inicial: " + strFechaOk);
//        System.out.println("######################## fecha final: " + strFechaFinOk);
        //obtenemos el contato del agente
        String strAgente = "";
        String strNumero = "";
        String strCorreo = "";
        String strExt = "";
        String strPass = "";
        String strServer = "";
        String strPort = "";
        String strTLS = "";
        String strSTLS = "";
        String strSqlEjecutivo = "select id_usuarios, nombre_usuario, TELEFONO, NUM_EXT,"
                + "SMTP_US, SMTP_PASS, SMTP, PORT, SMTP_USATLS, SMTP_USASTLS "
                + "from usuarios where id_usuarios = " + intIDEjecutivo;
        try {
            ResultSet rsAgente = oConn.runQuery(strSqlEjecutivo, true);
            while (rsAgente.next()) {
                strAgente = rsAgente.getString("nombre_usuario");
                strNumero = rsAgente.getString("TELEFONO");
                strCorreo = rsAgente.getString("SMTP_US");
                strExt = rsAgente.getString("NUM_EXT");
                strPass = rsAgente.getString("SMTP_PASS");
                strServer = rsAgente.getString("SMTP");
                strPort = rsAgente.getString("PORT");
                strTLS = rsAgente.getString("SMTP_USATLS");
                strSTLS = rsAgente.getString("SMTP_USASTLS");
            }
            rsAgente.close();
        } catch (SQLException ex) {
            log.error("Error MailAccesoCofideNet2.[" + ex.getLocalizedMessage() + "]");
        }

        //obtenemos el contato del agente
        String strResp = "OK";
        String strNomTemplate = "ACCESO_MEMBRESIA"; //obtenemos el template del mail para le supervisor
        String[] lstMail = getMailTemplate(oConn, strNomTemplate);
        String strMsgMail = lstMail[1];
        //  Si estan llenos todos los datos mandamos el mail
        if (!strServer.equals("")
                && !strCorreo.equals("")
                && !strPass.equals("")) {
            //armamos el mail
            Mail mail = new Mail();
            mail.setBolDepuracion(false);
            //Activamos envio de acuse de recibo
            mail.setBolAcuseRecibo(false);
            //Obtenemos los usuarios a los que mandaremos el mail
            String strLstMail = "";
            //Validamos si el mail del cliente es valido
            if (!strMailCte.equals("")) {
                if (mail.isEmail(strMailCte)) {
                    strLstMail = strMailCte;
                    strLstMail += "," + strCorreo;
                    strLstMail += "," + "clave@cofide.org"; //correo de emmanuell
                } else {
                    strLstMail = strCorreo;
                    strLstMail += ", " + "clave@cofide.org"; //correo de emmanuell
                }
            } else {
                strLstMail = strCorreo;
                strLstMail += "," + "clave@cofide.org"; //correo de emmanuell
            }
            System.out.println("######################### correso destinatarios:: " + strLstMail + " ###################");
            //Mandamos mail si hay usuarios
            if (!strLstMail.equals("")) {
//                strMsgMail = strMsgMail.replace("%TITULO%", getHtml(strTitulo));
                strMsgMail = strMsgMail.replace("%FCH_INI%", getHtml(strFechaOk));
                strMsgMail = strMsgMail.replace("%FCH_FIN%", getHtml(strFechaFinOk));
                strMsgMail = strMsgMail.replace("%CURSO%", getHtml(strNombre.replace("COFIDE.NET", "<br><br><br><br><br><br>COFIDE.NET")));
                strMsgMail = strMsgMail.replace("%DURACION%", strDuracion);
                strMsgMail = strMsgMail.replace("%PRECIO%", strPrecioVta);
                strMsgMail = strMsgMail.replace("%PARTICIPANTE%", getHtml(strParticipante));
                strMsgMail = strMsgMail.replace("%WEB_USUARIO%", getStrUsuario());
                strMsgMail = strMsgMail.replace("%WEB_PASSWORD%", getStrContraseña());
                strMsgMail = strMsgMail.replace("%AGENTE%", getHtml(strAgente));
                strMsgMail = strMsgMail.replace("%EXTENSION%", strNumero);
                strMsgMail = strMsgMail.replace("%CORREO%", strCorreo);

                //Establecemos parametros
                mail.setUsuario(strCorreo);
                mail.setContrasenia(strPass);
                mail.setHost(strServer);
                mail.setPuerto(strPort);
                mail.setAsunto(lstMail[0].replace("%CC_NOMBRE_CURSO%", strNombre));
                mail.setDestino(strLstMail);
                mail.setMensaje(strMsgMail);
                mail.setBolDepuracion(false);
                //Adjuntamos XML y PDF
//                System.out.println("##################################################correo: \n" + strMsgMail);
                if (strTLS.equals("1")) {
                    mail.setBolUsaTls(true);
                }
                if (strSTLS.equals("1")) {
                    mail.setBolUsaStartTls(true);
                }
//                mail.sendMail();
                boolean bol = mail.sendMail();
                if (!bol) {
                    strResp = "Fallo el envio del Mail.";
                } else {
                    System.out.println("############ SE ENVIO EL CORREO ############");
                    strResp = "Mando el correo OKISTRIKIS";
                }
                logEnvioNET(getStrUsuario(), getStrContraseña(), strResp, oConn);
            }
        }
        System.out.println("############################ TERMINO EL ENVIO DE ACCESO EN COFIDE.NET: \nCORREO:" + strMailCte + "\nEJECUTIVO: " + strCorreo + "\nESTATUS: " + strResp);
    }
    //</editor-fold>

// <editor-fold defaultstate="collapsed" desc="MAIL DESCARGA DE MATERIAL">
    public String getHtml(String strTxto) {
        strTxto = strTxto.replace("á", "&aacute;");
        strTxto = strTxto.replace("é", "&eacute;");
        strTxto = strTxto.replace("í", "&iacute;");
        strTxto = strTxto.replace("ó", "&oacute;");
        strTxto = strTxto.replace("ú", "&uacute;");
        strTxto = strTxto.replace("Á", "&Aacute;");
        strTxto = strTxto.replace("É", "&Eacute;");
        strTxto = strTxto.replace("Í", "&Iacute;");
        strTxto = strTxto.replace("Ó", "&Oacute;");
        strTxto = strTxto.replace("Ú", "&Uacute;");
        strTxto = strTxto.replace("ñ", "&ntilde;");
        strTxto = strTxto.replace("Ñ", "&Ntilde;");
        /**
         * http://www.forosdelweb.com/f4/acentos-codigo-html-481545/ Re: acentos
         * en el código html a = &aacute
         *
         * é = &eacute
         *
         * í = &iacute
         *
         * ó = &oacute
         *
         * ú = &uacute
         *
         * ñ = &ntilde
         *
         */
        return strTxto;
    }

//    public String createUsuarioNuevoCurso(String UserNom, int intIdUser, String strNombre, String strApellidos, String strTelefono) {
    public String createUsuarioNuevoCurso(String UserNom, int intIdUser, String strNombre, String strApellidos, String strTitulo) {
        Texto_UppLow p = new Texto_UppLow();
        Conexion oConnMGV = new Conexion();
        CatUserW user = new CatUserW();
        oConnMGV.setStrNomPool("jdbc/COFIDE");
        oConnMGV.open();
        int intLenght = 6;
//        int intLenght = UserNom.length();
        String strNomDoc = strTitulo + " " + strNombre + " " + strApellidos;
        generateData getPass = new generateData();
        String strUserNom = UserNom.replace(".", "").replace("@", ""); //quita puntos y el arroba
        strUserNom = p.UpperCaseTexto(strUserNom); //cambia a mayusculas
        String strPass = getPass.getPassword(strUserNom, intLenght);
        user.setFieldString("usr", UserNom);
//        System.out.println("\n\nuser: " + UserNom);
        user.setFieldString("psw", strPass);
//        System.out.println("\n\npassword: " + strPass);
        user.setFieldString("nombre", strNombre);
        user.setFieldString("apellidos", strApellidos);
        user.setFieldString("nombre_doc", strNomDoc);
        user.setFieldString("fch_alta", fec.getFechaActualAAAAMMDDguion());
        user.setFieldString("fch_baja", "0000-00-00 00:00:00");
        user.setFieldInt("id_usr_crm", intIdUser);
        user.setFieldInt("id_status", 1);

        String strKeyExist = strExisteUser(UserNom);
        String strKey = "";

        if (strKeyExist.equals("")) {
            user.Agrega(oConnMGV);
            strKey = user.getValorKey();
            String strSql = "update cat_usr_w set psw = '" + strPass + "' where id_usr_w = " + strKey;
            oConnMGV.runQueryLMD(strSql);
        } else {
            strKey = strKeyExist;
        }
//        oConnMGV.close();
        return strKey;
    }//Fin createUsuarioNuevoCurso

    public String strExisteUser(String strUsr) {
        String strIdUsr = "";
        Conexion oConnMGV = new Conexion();
        oConnMGV.setStrNomPool("jdbc/COFIDE");
        oConnMGV.open();
        String strSql = "select * from cat_usr_w where usr = '" + strUsr + "'";
        try {
            ResultSet rs = oConnMGV.runQuery(strSql, true);
            while (rs.next()) {
                strIdUsr = rs.getString("id_usr_w");
            }
            rs.close();
        } catch (SQLException ex) {
            log.debug("Error Existe cat_usr_w [" + ex.getLocalizedMessage() + "]");
        }
        oConnMGV.close();
        return strIdUsr;
    }

    public void VtaWeb(Conexion oConn, int intIdVta, String strIdWeb, int intTipo, int intMod, String strDoc, String strIdCurso) {
        String strRespuesta = "OK";
        String strSql = "";
        String strSqlInsert = "";
        String strTipo = "0"; //1 = Online , 2 = retransmision
        String strFechaIni = "";
        String strFechaFin = "";
        ResultSet rs;
        Conexion oConnMGV = new Conexion();
        oConnMGV.setStrNomPool("jdbc/COFIDE");
        oConnMGV.open();
        if (intTipo == 2) { //online
            strTipo = "1";
        }
        if (intTipo == 3) { //retransmision
            strTipo = "2";
        }
        if (intTipo == 4) { //COFIDE.NET
            strTipo = "3";
        }
        //VALIDA SOLO EN COFIDE.NET
        if (!strTipo.equals("3")) {
            //obtenemos datos de la venta        
            strSql = "select *,"
                    + "(select CC_FECHA_INICIAL from cofide_cursos where cofide_cursos.CC_CURSO_ID = " + strIdCurso + ") as fch_ini,"
                    + "(select CC_FECHA_INICIAL2 from cofide_cursos where cofide_cursos.CC_CURSO_ID = " + strIdCurso + ") as fch_ini2,"
                    + "(select CC_FECHA_INICIAL3 from cofide_cursos where cofide_cursos.CC_CURSO_ID = " + strIdCurso + ") as fch_ini3 "
                    + "from view_ventasglobales where FAC_ID = " + intIdVta + " and TIPO_DOC = '" + strDoc + "'";
            try {
                rs = oConn.runQuery(strSql, true);
                while (rs.next()) {
                    if (intTipo == 3) {
                        strFechaIni = fec.getFechaActual();
                    } else {
                        strFechaIni = rs.getString("fch_ini");
                    }
                    strFechaFin = fec.addFecha(strFechaIni, 5, 90);
                    strFechaIni = fec.FormateaAAAAMMDD(strFechaIni, "-");
                    if (rs.getString("fch_ini2").isEmpty()) {
                        strFechaFin = fec.FormateaAAAAMMDD(strFechaFin, "-");
                    } else {
                        if (rs.getString("fch_ini3").isEmpty()) {
                            strFechaFin = fec.addFecha(rs.getString("fch_ini2"), 5, 90);
                            strFechaFin = fec.FormateaAAAAMMDD(strFechaFin, "-");
                        } else {
                            strFechaFin = fec.addFecha(rs.getString("fch_ini3"), 5, 90);
                            strFechaFin = fec.FormateaAAAAMMDD(strFechaFin, "-");
                        }
                    }
//                String strDelete = "delete from ventas_crm where tkt_id_crm = " + rs.getString("FAC_ID");
//                oConnMGV.runQueryLMD(strDelete);
                    System.out.println("###################### GUARDA LA VENTA EN LA WEB");
                    strSqlInsert = "INSERT INTO ventas_crm (tkt_id_crm, tkt_folio, tkt_fch, id_usr_web, id_usr_crm, id_evento, id_tipo_evento, id_mod, fch_ini, fch_fin, url_online) VALUES "
                            + "('" + rs.getString("FAC_ID") + "', '" + rs.getString("FAC_FOLIO") + "', '" + fec.getFechaActualAAAAMMDDguion() + "', '" + strIdWeb + "', "
                            + "'" + rs.getString("CT_ID") + "', '" + strIdCurso + "', '" + intMod + "', '" + strTipo + "', '" + strFechaIni + "', '" + strFechaFin + "',' ')";
                    oConnMGV.runQueryLMD(strSqlInsert);
                }

                rs.close();
                System.out.println("termino el query");
            } catch (SQLException ex) {
                System.out.println("ERROR al dar de alta en la web: " + ex.getMessage());
            }
            oConnMGV.close();
        }
    }

    /**
     * guarda los cursos asignados por capital humano
     *
     * @param oConn
     * @param intIdCurso id dle curso
     * @param strIdWeb id del colaborador en la web
     * @param intTipo tipo de curso
     * @param intMod modalidad, siempre sera 1
     */
    public void VtaWebRH(Conexion oConn, int intIdCurso, String strIdWeb, String strIdUsuario, int intTipo, int intMod) {
        String strRespuesta = "OK";
        String strSql = "";
        String strSqlInsert = "";
        String strTipo = "0"; //1 = Online , 2 = retransmision
        String strFechaIni = "";
        String strFechaFin = "";
        ResultSet rs;
        Conexion oConnMGV = new Conexion();
        oConnMGV.setStrNomPool("jdbc/COFIDE");
        oConnMGV.open();
        if (intTipo == 2) { //online
            strTipo = "1";
        }
        if (intTipo == 3) { //retransmision
            strTipo = "2";
        }
        //obtenemos datos de la venta        
        strSql = "select CC_FECHA_INICIAL as fch_ini, CC_FECHA_INICIAL2 as fch_ini2, CC_FECHA_INICIAL3 as fch_ini3 "
                + "from cofide_cursos "
                + "where CC_CURSO_ID  = '" + intIdCurso + "'";
        try {
            rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                if (intTipo == 3) {
                    strFechaIni = fec.getFechaActual();
                } else {
                    strFechaIni = rs.getString("fch_ini");
                }
                strFechaFin = fec.addFecha(strFechaIni, 5, 90);
                strFechaIni = fec.FormateaAAAAMMDD(strFechaIni, "-");
                if (rs.getString("fch_ini2").isEmpty()) {
                    strFechaFin = fec.FormateaAAAAMMDD(strFechaFin, "-");
                } else {
                    if (rs.getString("fch_ini3").isEmpty()) {
                        strFechaFin = fec.addFecha(rs.getString("fch_ini2"), 5, 90);
                        strFechaFin = fec.FormateaAAAAMMDD(strFechaFin, "-");
                    } else {
                        strFechaFin = fec.addFecha(rs.getString("fch_ini3"), 5, 90);
                        strFechaFin = fec.FormateaAAAAMMDD(strFechaFin, "-");
                    }
                }
                System.out.println("###################### GUARDA LA ASIGNACIÓN EN LA WEB");
                strSqlInsert = "INSERT INTO ventas_crm (tkt_id_crm, tkt_folio, tkt_fch, id_usr_web, id_usr_crm, id_evento, id_tipo_evento, id_mod, fch_ini, fch_fin, url_online) VALUES "
                        + "('0', '0', '" + fec.getFechaActualAAAAMMDDguion() + "', '" + strIdWeb + "', "
                        + "'" + strIdUsuario + "', '" + intIdCurso + "', '" + intMod + "', '" + strTipo + "', '" + strFechaIni + "', '" + strFechaFin + "',' ')";
                oConnMGV.runQueryLMD(strSqlInsert);
            }
            rs.close();
            System.out.println("termino el query");
        } catch (SQLException ex) {
            System.out.println("ERROR al dar de alta en la web: " + ex.getMessage());
        }
        oConnMGV.close();
    }

    // </editor-fold>
    /**
     * registra los movimientos de los emmail de acceso a COFIDE.NET para
     * Emmanuell
     *
     * @param strCorreo
     * @param strClave
     * @param strEstatus
     * @param oConn
     */
    public void logEnvioNET(String strCorreo, String strClave, String strEstatus, Conexion oConn) {
        String strSqlInsert = "INSERT INTO cofide_net_acceso (NET_USUARIO, NET_CONTRASENIA, NET_FECHA, NET_HORA, NET_COMENTARIO) VALUES "
                + "('" + strCorreo + "', '" + strClave + "', '" + fec.getFechaActual() + "', '" + fec.getHoraActual() + "', '" + strEstatus + "')";
        oConn.runQueryLMD(strSqlInsert);
        if (oConn.isBolEsError()) {
            System.out.println("oh! ocurrio un problema al guardar el seguimiento de los accesos a COFIDE.NET [" + oConn.getStrMsgError() + "]");
        }
    }

}


// </editor-fold>
