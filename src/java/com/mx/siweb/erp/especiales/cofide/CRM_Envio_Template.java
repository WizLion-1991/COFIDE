/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.especiales.cofide;

import comSIWeb.Operaciones.Conexion;
import comSIWeb.Utilerias.Fechas;
import comSIWeb.Utilerias.Mail;
import comSIWeb.Utilerias.UtilXml;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author COFIDE
 */
public class CRM_Envio_Template {

    UtilXml util = new UtilXml();
    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(COFIDE_Mail_cursos.class.getName());

    Fechas fec = new Fechas();
    Mail mail = new Mail();

    int intIdMasivo = 0;
    int intIdCte = 0;
    String strCorreoDest = "";

    public String getStrCorreoDest() {
        return strCorreoDest;
    }

    public void setStrCorreoDest(String strCorreoDest) {
        this.strCorreoDest = strCorreoDest;
    }

    public int getIntIdMasivo() {
        return intIdMasivo;
    }

    public void setIntIdMasivo(int intIdMasivo) {
        this.intIdMasivo = intIdMasivo;
    }

    public int getIntIdCte() {
        return intIdCte;
    }

    public void setIntIdCte(int intIdCte) {
        this.intIdCte = intIdCte;
    }

    public CRM_Envio_Template() {
    }

    /**
     *
     * @param oConn
     * @param intIDEjecutivo
     * @param strCRMEmail
     * @param strAsunto
     * @param intPreview 0 = manda mail, 1 = preview
     * @param strPlantilla
     * @param intIdPlantilla
     * @param strCT_ID id del cliente
     * @return
     * @throws java.sql.SQLException
     * @throws java.text.ParseException mail masivo de 3 cursos
     */
    public String Cofide3Cursos(Conexion oConn, int intIDEjecutivo, String strCRMEmail, String strAsunto,
            int intPreview, String strPlantilla, int intIdPlantilla) throws SQLException, ParseException {
        //Objetenemso los datos del curso
        String strNombreContacto = getStrNombreContactoCofide(oConn, strCRMEmail);

        String strNombre = ""; //NOMBRE DEL CURSO
        String strFechaIni = ""; //fecha inicial ddbb
        String strFecha = ""; //fecha inicial formateada
        String strNomMes = ""; //mes nombre
        String strHhrsInicio = ""; //hora de inicio
        String strDuracion = ""; //duracion del curso
        String strSede = ""; //precio
        String strMes = ""; //mes
        int intTipoCurso = 1; //tipo del curso
        String strIdWEb = ""; //id web con el que se liga con la pagina web
        String strEsSeminario = ""; //es seminario ?
        String strEsDiplomado = ""; //es diplomado ? 
        String strTipo = "PRESENCIAL"; //es presencial 
        String strLigaCurso = ""; //liga del curso
        String strCuerpo = "";
        int intCursos = 0;
        String strSQL = "select CRCD_ID_CURSO from crm_correos_deta where CRC_ID = " + intIdPlantilla;
        ResultSet rs = oConn.runQuery(strSQL, true);
        while (rs.next()) {
            intCursos = rs.getInt("CRCD_ID_CURSO");
            String strSqlCurso = "select CC_ID_WEB,CC_CURSO_ID, CC_NOMBRE_CURSO, CC_FECHA_INICIAL, CC_SEDE_ID, "
                    + "CC_HR_EVENTO_INI, (CC_DURACION_HRS + CC_DURACION_HRS2 + CC_DURACION_HRS3) as Duracion, CC_PRECIO_IVA, CC_ID_WEB, CC_IS_DIPLOMADO, CC_IS_SEMINARIO, CC_IS_PRESENCIAL, CC_IS_ONLINE "
                    + "from cofide_cursos where CC_ACTIVO = 1 and CC_CURSO_ID = " + intCursos + " order by CC_FECHA_INICIAL ASC";
            ResultSet rsCurso = oConn.runQuery(strSqlCurso, true);
            while (rsCurso.next()) {
                strNombre = rsCurso.getString("CC_NOMBRE_CURSO");
                strFechaIni = rsCurso.getString("CC_FECHA_INICIAL");
                strHhrsInicio = rsCurso.getString("CC_HR_EVENTO_INI");
                strDuracion = rsCurso.getString("Duracion");
                strSede = getSede(oConn, rsCurso.getString("CC_SEDE_ID"));
                strIdWEb = rsCurso.getString("CC_CURSO_ID");
                strEsSeminario = rsCurso.getString("CC_IS_SEMINARIO");
                strEsDiplomado = rsCurso.getString("CC_IS_DIPLOMADO");

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

                strCuerpo += "        <table width=\"700\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" style=\"padding-bottom: 10px;\">"
                        + "          <tr>"
                        + "            <td width=\"490\" style=\"background-color: #F5FAE5; border: 1px solid #99cc00; padding: 10px; -webkit-border-radius: 2px; border-radius: 2px;\">"
                        + "            "
                        + "                <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"
                        + "                  <tr>"
                        + "                    <td style=\"font-family: Helvetica, Tahoma, sans-serif; font-size: 22px; font-weight: 100; text-transform: none; border-right: 1px solid #99cc00; padding-right: 10px;\">" + strNombre + "</td>"
                        + "                    <td width=\"200\" style=\"padding-left: 10px;\" valign=\"top\">"
                        + "                    "
                        + "                        <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"2\" align=\"center\">"
                        + "                          <tr>"
                        + "                            <td colspan=\"2\" style=\"font-family: Helvetica, Tahoma, sans-serif; font-size: 16px; font-weight: 700; text-transform: none; padding-bottom: 10px;\">" + getFecha(oConn, strIdWEb) + "</td>"
                        + "                          </tr>"
                        + "                          <tr>"
                        + "                            <td style=\"font-family: Helvetica, Tahoma, sans-serif; font-size: 12px; font-weight: 700; text-transform: none;\">Inicio:</td>"
                        + "                            <td style=\"font-family: Helvetica, Tahoma, sans-serif; font-size: 12px; font-weight: 100; text-transform: none;\">" + strHhrsInicio + " HRS.</td>"
                        + "                          </tr>"
                        + "                          <tr>"
                        + "                            <td style=\"font-family: Helvetica, Tahoma, sans-serif; font-size: 12px; font-weight: 700; text-transform: none;\">Duraci&oacute;n:</td>"
                        + "                            <td style=\"font-family: Helvetica, Tahoma, sans-serif; font-size: 12px; font-weight: 100; text-transform: none;\">" + strDuracion + " HRS.</td>"
                        + "                          </tr>"
                        + "                          <tr>"
                        + "                            <td style=\"font-family: Helvetica, Tahoma, sans-serif; font-size: 12px; font-weight: 700; text-transform: none;\">Sede:</td>"
                        + "                            <td style=\"font-family: Helvetica, Tahoma, sans-serif; font-size: 12px; font-weight: 100; text-transform: none;\">" + strSede + "</td>"
                        + "                          </tr>"
                        + "                          <tr>"
                        + "                            <td style=\"font-family: Helvetica, Tahoma, sans-serif; font-size: 12px; font-weight: 700; text-transform: none;\">Modalidad:</td>"
                        + "                            <td style=\"font-family: Helvetica, Tahoma, sans-serif; font-size: 12px; font-weight: 100; text-transform: none;\">" + strTipo + "</td>"
                        + "                          </tr>";
                if (rsCurso.getInt("CC_IS_ONLINE") == 1) {
                    strCuerpo += "                 <tr>"
                            + "                            <td style=\"font-family: Helvetica, Tahoma, sans-serif; font-size: 12px; font-weight: 700; text-transform: none;\">&nbsp;</td>"
                            + "                            <td style=\"font-family: Helvetica, Tahoma, sans-serif; font-size: 12px; font-weight: 100; text-transform: none;\">ONLINE</td> <!-- IF SI ES ONLINE, IMPRIMIR, SI NO, DEJAR EN BLANCO -->"
                            + "                          </tr>";
                }
                strCuerpo += "             </table>"
                        + "                    "
                        + "                    </td>"
                        + "                  </tr>"
                        + "                </table>"
                        + ""
                        + "            </td>"
                        + "            <td width=\"10\">&nbsp;</td>"
                        + "            <td width=\"100\" style=\"background-color: #99cc00; font-family: Helvetica, Tahoma, sans-serif; font-size: 12px; color: #fff; text-decoration: none; font-weight: 700; -webkit-border-radius: 2px; border-radius: 2px; padding: 5px;\" align=\"center\" valign=\"middle\"><a href=\"" + strLigaCurso + "\" target=\"_blank\" style=\"color: #fff; text-decoration: none;\">[+] INFO.<br />TEMARIO</a></td>"
                        + "          </tr>"
                        + "        </table>";
            }
            rsCurso.close();
        }
        rs.close();
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
//        String strNomTemplate = strPlantilla; //obtenemos el template del mail para le supervisor
        String[] lstMail = getMailTemplate(oConn, strPlantilla);
        String strMsgMail = lstMail[1];
        //  Si estan llenos todos los datos mandamos el mail
        if (!strServer.equals("") && !strCorreo.equals("") && !strPass.equals("")) {
            //armamos el mail
//            Mail mail = new Mail();
            mail.setBolDepuracion(false);
//            //Activamos envio de acuse de recibo
            mail.setBolAcuseRecibo(false);
            //Obtenemos los usuarios a los que mandaremos el mail
            String strLstMail = "";
            //Validamos si el mail del cliente es valido
            if (mail.isEmail(strCRMEmail)) {
                strLstMail = strCRMEmail;
            }
            //Mandamos mail si hay usuarios
            if (!strLstMail.equals("")) {
                //cuerpo
                strMsgMail = strMsgMail.replace("%CUERPO%", getHtml(strCuerpo));
                strMsgMail = strMsgMail.replace("%AGENTE%", getHtml(strAgente));
                strMsgMail = strMsgMail.replace("%EXTENSION%", strNumero);
                strMsgMail = strMsgMail.replace("%CORREO%", strCorreo);
                strMsgMail = strMsgMail.replace("%CORREO_DESTINO%", getStrCorreoDest());
                strMsgMail = strMsgMail.replace("%ID_MASIVO%", String.valueOf(getIntIdMasivo()));
                strMsgMail = strMsgMail.replace("%ID_CTE%", String.valueOf(getIntIdCte()));

                if (!strNombreContacto.equals("")) {
                    strMsgMail = strMsgMail.replace("%NOMBRE_CONTACTO%", "APRECIABLE: " + strNombreContacto);
                } else {
                    strMsgMail = strMsgMail.replace("%NOMBRE_CONTACTO%", "");
                }
                //manda el mail = 0, o muestra preview = 1
                if (intPreview == 0) {
                    //Establecemos parametros
                    mail.setUsuario(strCorreo);
                    mail.setContrasenia(strPass);
                    mail.setHost(strServer);
                    mail.setPuerto(strPort);
                    mail.setAsunto(lstMail[0].replace("%ASUNTO%", strAsunto));
                    mail.setDestino(strLstMail);
                    mail.setMensaje(strMsgMail);
                    mail.setBolDepuracion(false);
//                    System.out.println(strMsgMail);
                    mail.setBolMantenerConexion(false); // cierra conexión de correo
                    //Adjuntamos XML y PDF
                    if (strTLS.equals("1")) {
                        mail.setBolUsaTls(true);
                    }
                    if (strSTLS.equals("1")) {
                        mail.setBolUsaStartTls(true);
                    }
                    boolean bol = mail.sendMail();
                    if (!bol) {
                        strResp = "Fallo el envio del Mail.";
                    } else {
                        regMail(getIntIdMasivo(), getIntIdCte(), getStrCorreoDest(), oConn);
                    }
                } else {
                    strResp = strMsgMail; // manda el cuerpo del mail
                    System.out.println("" + strMsgMail);
                }
            }
        }
        return strResp;
    }

    /**
     *
     * @param oConn
     * @param intIDEjecutivo
     * @param strCRMEmail
     * @param strAsunto
     * @param intPreview 0 = manda mail, 1 = preview
     * @param strPlantilla
     * @param intIdPlantilla
     * @return
     * @throws java.sql.SQLException
     * @throws java.text.ParseException correo para enviar la plantilla de los
     * diplomados
     */
    public String CofideDiplomado(Conexion oConn, int intIDEjecutivo, String strCRMEmail, String strAsunto,
            int intPreview, String strPlantilla, int intIdPlantilla) throws SQLException, ParseException {
        //Objetenemso los datos del curso
        String strNombre = ""; //NOMBRE DEL CURSO
        String strFechaIni = ""; //fecha inicial ddbb
        String strHhrsInicio = ""; //hora de inicio
        String strDuracion = ""; //duracion del curso
        String strSede = ""; //precio
        int intTipoCurso = 1; //tipo del curso
        String strIdWEb = ""; //id web con el que se liga con la pagina web
        String strEsSeminario = ""; //es seminario ?
        String strEsDiplomado = ""; //es diplomado ? 
        String strPrecio = "";
        String strPrecioOn = "";
        String strObjetivo = "";
        String strDirigido = "";
        String strIncluye = "";
        String strTipo = "PRESENCIAL"; //es presencial 
        String strLigaCurso = ""; //liga del curso
        String strNombreContacto = getStrNombreContactoCofide(oConn, strCRMEmail);

        int intCursos = 0;
        String strSQL = "select CRCD_ID_CURSO from crm_correos_deta where CRC_ID = " + intIdPlantilla;
        ResultSet rs = oConn.runQuery(strSQL, true);
        while (rs.next()) {
            intCursos = rs.getInt("CRCD_ID_CURSO");
            String strSqlCurso = "select CC_ID_WEB,CC_CURSO_ID, CC_NOMBRE_CURSO, CC_FECHA_INICIAL, CC_SEDE_ID, "
                    + "CC_HR_EVENTO_INI, (CC_DURACION_HRS + CC_DURACION_HRS2 + CC_DURACION_HRS3) as Duracion,CC_PRECIO_PRES, CC_PRECIO_IVA, CC_ID_WEB, CC_IS_DIPLOMADO, CC_IS_SEMINARIO, CC_IS_PRESENCIAL, CC_IS_ONLINE,"
                    + "CC_DIRIGIDO, CC_DETALLE, CC_OBJETIVO "
                    + "from cofide_cursos where CC_ACTIVO = 1 and CC_CURSO_ID = " + intCursos + " order by CC_FECHA_INICIAL ASC";
            ResultSet rsCurso = oConn.runQuery(strSqlCurso, true);
            while (rsCurso.next()) {
                strNombre = rsCurso.getString("CC_NOMBRE_CURSO");
                strFechaIni = rsCurso.getString("CC_FECHA_INICIAL");
                strHhrsInicio = rsCurso.getString("CC_HR_EVENTO_INI");
                strDuracion = rsCurso.getString("Duracion");
                strSede = getSede(oConn, rsCurso.getString("CC_SEDE_ID"));
                strSede = getHtml(strSede);
                strIdWEb = rsCurso.getString("CC_CURSO_ID");
                strEsSeminario = rsCurso.getString("CC_IS_SEMINARIO");
                strEsDiplomado = rsCurso.getString("CC_IS_DIPLOMADO");
                if (!rsCurso.getString("CC_PRECIO_PRES").equals("")) {
                    strPrecio = setPrecioOk(rsCurso.getString("CC_PRECIO_PRES"));
                }
                if (!rsCurso.getString("CC_PRECIO_ON").equals("0")) {
                    strPrecioOn = setPrecioOk(rsCurso.getString("CC_PRECIO_ON"));
                }
//                strPrecio = rsCurso.getString("CC_PRECIO_PRES");
//                strPrecioOn = rsCurso.getString("CC_PRECIO_ON");
                strObjetivo = rsCurso.getString("CC_OBJETIVO");
                strDirigido = rsCurso.getString("CC_DIRIGIDO");
                strIncluye = rsCurso.getString("CC_DETALLE");

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
        }
        rs.close();
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
//        String strNomTemplate = strPlantilla; //obtenemos el template del mail para le supervisor
        String[] lstMail = getMailTemplate(oConn, strPlantilla);
        String strMsgMail = lstMail[1];
        //  Si estan llenos todos los datos mandamos el mail
        if (!strServer.equals("") && !strCorreo.equals("") && !strPass.equals("")) {
            //armamos el mail
//            Mail mail = new Mail();
            mail.setBolDepuracion(false);
            //Activamos envio de acuse de recibo
            mail.setBolAcuseRecibo(false);
            //Obtenemos los usuarios a los que mandaremos el mail
            String strLstMail = "";
            //Validamos si el mail del cliente es valido
            if (mail.isEmail(strCRMEmail)) {
//                strLstMail += "," + strCRMEmail;
                strLstMail = strCRMEmail;
            }
            //Mandamos mail si hay usuarios
            if (!strLstMail.equals("")) {
//                System.out.println("cuerpo: " + strMsgMail);
                //cuerpo
                strMsgMail = strMsgMail.replace("%CURSO%", getHtml(strNombre));
                strMsgMail = strMsgMail.replace("%FECHA%", getHtml(getFecha(oConn, strIdWEb)));
                strMsgMail = strMsgMail.replace("%HORA%", strHhrsInicio);
                strMsgMail = strMsgMail.replace("%DURACION%", strDuracion);
                strMsgMail = strMsgMail.replace("%SEDE%", getHtml(strSede));
                strMsgMail = strMsgMail.replace("%TIPO_CURSO%", "PRESENCIAL");
                strMsgMail = strMsgMail.replace("%LIGA%", strLigaCurso);
                strMsgMail = strMsgMail.replace("%PRECOP_PRES%", strPrecio);
                strMsgMail = strMsgMail.replace("%PRECIO_ON%", strPrecioOn);
                strMsgMail = strMsgMail.replace("%OBJETIVO%", getHtml(strObjetivo));
                strMsgMail = strMsgMail.replace("%DIRIGIDO_A%", getHtml(strDirigido));
                strMsgMail = strMsgMail.replace("%INCLUYE%", getHtml(strIncluye));
                strMsgMail = strMsgMail.replace("%AGENTE%", getHtml(strAgente));
                strMsgMail = strMsgMail.replace("%EXTENSION%", strNumero);
                strMsgMail = strMsgMail.replace("%CORREO%", strCorreo);
                strMsgMail = strMsgMail.replace("%CORREO_DESTINO%", getStrCorreoDest());
                strMsgMail = strMsgMail.replace("%ID_MASIVO%", String.valueOf(getIntIdMasivo()));
                strMsgMail = strMsgMail.replace("%ID_CTE%", String.valueOf(getIntIdCte()));

                if (!strNombreContacto.equals("")) {
                    strMsgMail = strMsgMail.replace("%NOMBRE_CONTACTO%", "APRECIABLE: " + strNombreContacto);
                } else {
                    strMsgMail = strMsgMail.replace("%NOMBRE_CONTACTO%", "");
                }
                //Establecemos parametros
                //manda el mail = 0, o muestra preview = 1
                if (intPreview == 0) {
                    mail.setUsuario(strCorreo);
                    mail.setContrasenia(strPass);
                    mail.setHost(strServer);
                    mail.setPuerto(strPort);
                    mail.setAsunto(lstMail[0].replace("%ASUNTO%", strAsunto));
                    mail.setDestino(strLstMail);
                    mail.setMensaje(strMsgMail);
                    mail.setBolDepuracion(false);
                    mail.setBolMantenerConexion(false); // cierra conexión de correo
                    //Adjuntamos XML y PDF
                    if (strTLS.equals("1")) {
                        mail.setBolUsaTls(true);
                    }
                    if (strSTLS.equals("1")) {
                        mail.setBolUsaStartTls(true);
                    }
                    boolean bol = mail.sendMail();
                    if (!bol) {
                        strResp = "Fallo el envio del Mail.";
                    } else {
                        regMail(getIntIdMasivo(), getIntIdCte(), getStrCorreoDest(), oConn);
                    }
                } else {
                    strResp = strMsgMail;
                }
            }
        }
        return strResp;
    }

    /**
     *
     * @param oConn conexión
     * @param intIDEjecutivo id del ejecutvo quien sera el remitente
     * @param strCRMEmail emial del cliente a quien se le enviara
     * @param strAsunto
     * @param intPreview 0 = manda mail, 1 = preview = mandar mail
     * @param strPlantilla
     * @param intIdPlantilla
     * @return regresa un OK
     * @throws java.sql.SQLException
     * @throws java.text.ParseException manda el dimplomado por modulos
     */
    public String CofideDipMod(Conexion oConn, int intIDEjecutivo, String strCRMEmail, String strAsunto,
            int intPreview, String strPlantilla, int intIdPlantilla) throws SQLException, ParseException {
        //Objetenemso los datos del curso
        String strNombre = ""; //NOMBRE DEL CURSO
        String strFechaIni = ""; //fecha inicial ddbb
        String strHhrsInicio = ""; //hora de inicio
        String strDuracion = ""; //duracion del curso
        String strSede = ""; //precio
        int intTipoCurso = 1; //tipo del curso
        String strIdWEb = ""; //id web con el que se liga con la pagina web
        String strEsSeminario = ""; //es seminario ?
        String strEsDiplomado = ""; //es diplomado ? 
        String strPrecio = "";
        String strPrecioOn = "";
        String strObjetivo = "";
        String strDirigido = "";
        String strIncluye = "";
        String strModulos = "";
        String strPrecioOK = "";
        String strDescOnOk = "";
        int intMOdulo = 1;
        String strTipo = "PRESENCIAL"; //es presencial 
        String strLigaCurso = ""; //liga del curso
        String strIDCurso = "";
        String strNombreContacto = getStrNombreContactoCofide(oConn, strCRMEmail);

        int intCursos = 0;
        String strSQL = "select CRCD_ID_CURSO from crm_correos_deta where CRC_ID = " + intIdPlantilla;
        ResultSet rs = oConn.runQuery(strSQL, true);
        while (rs.next()) {
            intCursos = rs.getInt("CRCD_ID_CURSO");
            String strSqlCurso = "select CC_ID_WEB, CC_CURSO_ID, CC_NOMBRE_CURSO, CC_FECHA_INICIAL, CC_SEDE_ID, "
                    + "CC_HR_EVENTO_INI, (CC_DURACION_HRS + CC_DURACION_HRS2 + CC_DURACION_HRS3) as Duracion, CC_PRECIO_PRES,CC_PRECIO_ON, CC_IS_DIPLOMADO, CC_IS_SEMINARIO, CC_IS_PRESENCIAL, CC_IS_ONLINE,"
                    + "CC_DIRIGIDO, CC_DETALLE, CC_OBJETIVO "
                    + "from cofide_cursos where CC_ACTIVO = 1 and CC_CURSO_ID = " + intCursos + " order by CC_FECHA_INICIAL ASC";
            ResultSet rsCurso = oConn.runQuery(strSqlCurso, true);
            while (rsCurso.next()) {
                strIDCurso = rsCurso.getString("CC_CURSO_ID");
                strNombre = rsCurso.getString("CC_NOMBRE_CURSO");
                strFechaIni = rsCurso.getString("CC_FECHA_INICIAL");
                strHhrsInicio = rsCurso.getString("CC_HR_EVENTO_INI");
                strDuracion = rsCurso.getString("Duracion");
                strSede = getSede(oConn, rsCurso.getString("CC_SEDE_ID"));
                strIdWEb = rsCurso.getString("CC_CURSO_ID");
                strEsSeminario = rsCurso.getString("CC_IS_SEMINARIO");
                strEsDiplomado = rsCurso.getString("CC_IS_DIPLOMADO");
                if (!rsCurso.getString("CC_PRECIO_PRES").equals("")) {
                    strPrecio = setPrecioOk(rsCurso.getString("CC_PRECIO_PRES"));
                }
                if (!rsCurso.getString("CC_PRECIO_ON").equals("0")) {
                    strPrecioOn = setPrecioOk(rsCurso.getString("CC_PRECIO_ON"));
                    strPrecioOK = "En l&iacute;nea: $ " + strPrecioOn + " + IVA";
                    strDescOnOk = "Seminario completo";
                }
//                strPrecio = rsCurso.getString("CC_PRECIO_PRES");
//                strPrecioOn = rsCurso.getString("CC_PRECIO_ON");
                strObjetivo = rsCurso.getString("CC_OBJETIVO");
                strDirigido = rsCurso.getString("CC_DIRIGIDO");
                strIncluye = rsCurso.getString("CC_DETALLE");
                if (strEsSeminario.equals("1")) {
                    intTipoCurso = 3;
                }
                if (strEsDiplomado.equals("1")) {
                    intTipoCurso = 2;
                }
//                if(rsCurso.getInt("CC_IS_ONLINE") == 1){
//                    strTipo += " / EN LINEA";
//                }
//                strLigaCurso = fec.FormateaDDMMAAAA(strFechaIni, "") + strIdWEb + intTipoCurso;
                if (rsCurso.getString("CC_ID_WEB").equals("")) {
                    strLigaCurso = "http://cofide.org/producto/";
                } else {
                    strLigaCurso = rsCurso.getString("CC_ID_WEB");
                }
            }
            rsCurso.close();
            String strSqlMod = "select * from cofide_cursos "
                    + "where CC_CURSO_ID in "
                    + "(select CC_CURSO_ID "
                    + "from cofide_modulo_curso "
                    + "where CC_CURSO_IDD = " + strIDCurso + ")"
                    + " order by CC_FECHA_INICIAL ASC";

            ResultSet rsMod = oConn.runQuery(strSqlMod, true);
            while (rsMod.next()) {
                System.out.println("entro a la cosulta");
                strModulos += "<table width=\"700\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" style=\"padding-bottom: 10px;\">"
                        + "<tr>"
                        + "<td width=\"490\" style=\"background-color: #99cc00; padding: 10px; font-family: Helvetica, Tahoma; font-size: 12px; color: #FFF; font-weight: 700; sans-serif; -webkit-border-radius: 2px; border-radius: 2px;\">M&Oacute;DULO " + intMOdulo + ".</td>"
                        + "</tr>"
                        + "<tr>"
                        + "<td>"
                        + "<table width=\"700\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" style=\"padding-bottom: 10px;\">"
                        + "<tr>"
                        + "<td width=\"490\" style=\"background-color: #F5FAE5; border: 1px solid #99cc00; padding: 10px; -webkit-border-radius: 2px; border-radius: 2px;\">"
                        + "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"
                        + "<tr>"
                        + "<td width=\"260\" style=\"font-family: Helvetica, Tahoma, sans-serif; font-size: 30px; font-weight: 100; text-transform: none; border-right: 1px solid #E4E8D3; padding-bottom: 10px;\">" + getFecha(oConn, rsMod.getString("CC_CURSO_ID")) + "</td>"
                        + "<td style=\"padding-left: 10px;\" valign=\"top\">"
                        + "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"2\" align=\"center\">"
                        + "<tr>"
                        + "<td width=\"80\" style=\"font-family: Helvetica, Tahoma, sans-serif; font-size: 12px; font-weight: 700; text-transform: none; padding-bottom: 10px;\">Horario de:</td>"
                        + "<td style=\"font-family: Helvetica, Tahoma, sans-serif; font-size: 12px; font-weight: 100; text-transform: none; padding-bottom: 10px;\">" + rsMod.getString("CC_HR_EVENTO_INI") + " a " + rsMod.getString("CC_HR_EVENTO_FIN") + " hrs.</td>"
                        + "</tr>"
                        + "<tr>"
                        + "<td colspan=\"2\" style=\"font-family: Helvetica, Tahoma, sans-serif; font-size: 12px; font-weight: 700; text-transform: none;\">" + rsMod.getString("CC_NOMBRE_CURSO") + "</td> <!-- NOMBRE DEL CURSO / MODULO -->"
                        + "</tr>"
                        + "</table>"
                        + "</td>"
                        + "</tr>"
                        + "</table>"
                        + "</td>"
                        + "</tr>"
                        + "</table>"
                        + "</td>"
                        + "</tr>"
                        + "</table>";
                intMOdulo++;
            }
            System.out.println("cuerpo del diplomado " + strModulos);
            rsMod.close();
        }
        rs.close();
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
//        String strNomTemplate = "DIPLOMADO"; //obtenemos el template del mail para le supervisor
        String[] lstMail = getMailTemplate(oConn, strPlantilla);
        String strMsgMail = lstMail[1];
        //  Si estan llenos todos los datos mandamos el mail

        if (!strServer.equals("") && !strCorreo.equals("") && !strPass.equals("")) {
            //armamos el mail
//            Mail mail = new Mail();
            mail.setBolDepuracion(false);
            //Activamos envio de acuse de recibo
            mail.setBolAcuseRecibo(false);
            //Obtenemos los usuarios a los que mandaremos el mail
            String strLstMail = "";
            //Validamos si el mail del cliente es valido
            if (mail.isEmail(strCRMEmail)) {
                strLstMail = strCRMEmail;
            }
            //Mandamos mail si hay usuarios
            if (!strLstMail.equals("")) {
//                System.out.println("cuerpo: " + strMsgMail);
                //cuerpo
                strMsgMail = strMsgMail.replace("%CURSO%", getHtml(strNombre));
                strMsgMail = strMsgMail.replace("%FECHA%", getFecha(oConn, strIdWEb));
                strMsgMail = strMsgMail.replace("%HRA%", strHhrsInicio);
                strMsgMail = strMsgMail.replace("%DURACION%", strDuracion);
                strMsgMail = strMsgMail.replace("%SEDE%", getHtml(strSede));
                strMsgMail = strMsgMail.replace("%TIPO_CURSO%", strTipo);
                strMsgMail = strMsgMail.replace("%LIGA%", strLigaCurso);
                strMsgMail = strMsgMail.replace("%PRECIO_PRES%", strPrecio);
//                strMsgMail = strMsgMail.replace("%PRECIO_ON%", strPrecioOn);
                strMsgMail = strMsgMail.replace("%PRECIO_ON%", strPrecioOK);
                strMsgMail = strMsgMail.replace("%DESC_ON%", strDescOnOk);
                strMsgMail = strMsgMail.replace("%OBJETIVO%", getHtml(strObjetivo));
                strMsgMail = strMsgMail.replace("%DIRIGIDO_A%", getHtml(strDirigido));
                strMsgMail = strMsgMail.replace("%INCLUYE%", getHtml(strIncluye));
                strMsgMail = strMsgMail.replace("%MODULOS%", getHtml(strModulos));
                strMsgMail = strMsgMail.replace("%AGENTE%", getHtml(strAgente));
                strMsgMail = strMsgMail.replace("%EXTENSION%", strNumero);
                strMsgMail = strMsgMail.replace("%CORREO%", strCorreo);
                strMsgMail = strMsgMail.replace("%CORREO_DESTINO%", getStrCorreoDest());
                strMsgMail = strMsgMail.replace("%ID_MASIVO%", String.valueOf(getIntIdMasivo()));
                strMsgMail = strMsgMail.replace("%ID_CTE%", String.valueOf(getIntIdCte()));

                if (!strNombreContacto.equals("")) {
                    strMsgMail = strMsgMail.replace("%NOMBRE_CONTACTO%", "APRECIABLE: " + strNombreContacto);
                } else {
                    strMsgMail = strMsgMail.replace("%NOMBRE_CONTACTO%", "");
                }
                //manda el mail = 0, o muestra preview = 1
                if (intPreview == 0) {
                    //Establecemos parametros
                    mail.setUsuario(strCorreo);
                    mail.setContrasenia(strPass);
                    mail.setHost(strServer);
                    mail.setPuerto(strPort);
                    mail.setAsunto(lstMail[0].replace("%ASUNTO%", strAsunto));
                    mail.setDestino(strLstMail);
                    mail.setMensaje(strMsgMail);
                    mail.setBolDepuracion(false);
                    mail.setBolMantenerConexion(false); // cierra conexión de correo
                    //Adjuntamos XML y PDF
                    if (strTLS.equals("1")) {
                        mail.setBolUsaTls(true);
                    }
                    if (strSTLS.equals("1")) {
                        mail.setBolUsaStartTls(true);
                    }
                    boolean bol = mail.sendMail();
                    if (!bol) {
                        strResp = "Fallo el envio del Mail.";
                    } else {
                        regMail(getIntIdMasivo(), getIntIdCte(), getStrCorreoDest(), oConn);
                    }
                } else {
                    strResp = strMsgMail;
                }
            }
        }
        return strResp;
    }

    /**
     *
     *
     * @param oConn
     * @param intIDEjecutivo
     * @param strCRMEmail
     * @param strIDCurso
     * @param strAsunto
     * @param intPreview 0 = manda mail, 1 = preview
     * @param strPlantilla
     * @param intIdPlantilla
     * @return
     * @throws java.sql.SQLException
     * @throws java.text.ParseException
     */
    public String CofideRecurrente(Conexion oConn, int intIDEjecutivo, String strCRMEmail, String strIDCurso, String strAsunto,
            int intPreview, String strPlantilla, int intIdPlantilla) throws SQLException, ParseException {
        //Objetenemso los datos del curso
        String strNombre = ""; //NOMBRE DEL CURSO
        String strFechaIni = ""; //fecha inicial ddbb
        String strHhrsInicio = ""; //hora de inicio
        String strDuracion = ""; //duracion del curso
        String strSede = ""; //precio
        int intTipoCurso = 1; //tipo del curso
        String strIdWEb = ""; //id web con el que se liga con la pagina web
        String strClaveCurso = "";
        String strCuerpo = "";
        String strLigaCurso = ""; //liga del curso
        String strNombreContacto = getStrNombreContactoCofide(oConn, strCRMEmail);

        int intCursos = 0;
        String strSQL = "select CRCD_ID_CURSO from crm_correos_deta where CRC_ID = " + intIdPlantilla;
        ResultSet rs = oConn.runQuery(strSQL, true);
        while (rs.next()) {
            intCursos = rs.getInt("CRCD_ID_CURSO");

            String strSqlCurso = "select CC_ID_WEB,CC_CURSO_ID, CC_NOMBRE_CURSO, CC_FECHA_INICIAL, CC_SEDE_ID, "
                    + "CC_HR_EVENTO_INI, CC_ID_WEB, CC_CLAVES "
                    + "from cofide_cursos where CC_ACTIVO = 1 and CC_CURSO_ID = " + intCursos + " order by CC_FECHA_INICIAL ASC";
            ResultSet rsCurso = oConn.runQuery(strSqlCurso, true);
            while (rsCurso.next()) {
                strNombre = rsCurso.getString("CC_NOMBRE_CURSO");
//            strNombre = getHtml(strNombre);
                strClaveCurso = rsCurso.getString("CC_CLAVES");
            }
            rsCurso.close();
            String strSqlMod = "select * from cofide_cursos where CC_CLAVES = " + strClaveCurso + " "
                    + "and CC_FECHA_INICIAL >= " + fec.getFechaActual() + " order by CC_FECHA_INICIAL ASC";
            ResultSet rsMod = oConn.runQuery(strSqlMod, true);
            while (rsMod.next()) {
                strFechaIni = rsMod.getString("CC_FECHA_INICIAL");
                strHhrsInicio = rsMod.getString("CC_HR_EVENTO_INI");
                strSede = getSede(oConn, rsMod.getString("CC_SEDE_ID"));
                strIdWEb = rsMod.getString("CC_CURSO_ID");
//                strLigaCurso = fec.FormateaDDMMAAAA(strFechaIni, "") + strIdWEb + intTipoCurso;
                if (rsCurso.getString("CC_ID_WEB").equals("")) {
                    strLigaCurso = "http://cofide.org/producto/";
                } else {
                    strLigaCurso = rsCurso.getString("CC_ID_WEB");
                }
                strCuerpo += " "
                        + "        <table width=\"700\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" style=\"padding-bottom: 10px;\">"
                        + "          <tr>"
                        + "            <td width=\"490\" style=\"background-color: #F5FAE5; border: 1px solid #99cc00; padding: 10px; -webkit-border-radius: 2px; border-radius: 2px;\">"
                        + "            "
                        + "                <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"
                        + "                  <tr>"
                        + "                    <td style=\"font-family: Helvetica, Tahoma, sans-serif; font-size: 30px; font-weight: 100; text-transform: none; border-right: 1px solid #99cc00; padding-bottom: 10px;\">" + getFecha(oConn, strIdWEb) + "</td> <!-- Vie. 10 Ago. 2016 -->"
                        + "                    <td width=\"200\" style=\"padding-left: 10px;\" valign=\"top\">"
                        + "                    "
                        + "                        <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"2\" align=\"center\">"
                        + "                          <tr>"
                        + "                            <td style=\"font-family: Helvetica, Tahoma, sans-serif; font-size: 12px; font-weight: 700; text-transform: none;\">Inicio:</td>"
                        + "                            <td style=\"font-family: Helvetica, Tahoma, sans-serif; font-size: 12px; font-weight: 100; text-transform: none;\">" + strHhrsInicio + " HRS.</td>"
                        + "                          </tr>"
                        + "                          <tr>"
                        + "                            <td style=\"font-family: Helvetica, Tahoma, sans-serif; font-size: 12px; font-weight: 700; text-transform: none;\">Sede:</td>"
                        + "                            <td style=\"font-family: Helvetica, Tahoma, sans-serif; font-size: 12px; font-weight: 100; text-transform: none;\">" + strSede + "</td> <!-- SEDE DONDE SE PRESENTARA -->"
                        + "                          </tr>"
                        + "                        </table>"
                        + "                    "
                        + "                    </td>"
                        + "                  </tr>"
                        + "                </table>"
                        + ""
                        + "            </td>"
                        + "            <td width=\"10\">&nbsp;</td>"
                        + "            <td width=\"100\" style=\"background-color: #99cc00; font-family: Helvetica, Tahoma, sans-serif; font-size: 12px; color: #fff; text-decoration: none; font-weight: 700; -webkit-border-radius: 2px; border-radius: 2px; padding: 5px;\" align=\"center\" valign=\"middle\"><a href=\"" + strLigaCurso + "\" target=\"_blank\" style=\"color: #fff; text-decoration: none;\">[+] INFO."
                        + "            <!-- LIGA INFORMACION DEL CURSO-->"
                        + "            <br />TEMARIO</a></td>"
                        + "          </tr>"
                        + "        </table>"
                        + "        ";
            }
            rsMod.close();
        }
        rs.close();
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
//        String strNomTemplate = "RECURRENTE"; //obtenemos el template del mail para le supervisor
        String[] lstMail = getMailTemplate(oConn, strPlantilla);
        String strMsgMail = lstMail[1];
        //  Si estan llenos todos los datos mandamos el mail

        if (!strServer.equals(
                "") && !strCorreo.equals("") && !strPass.equals("")) {
            //armamos el mail
//            Mail mail = new Mail();
            mail.setBolDepuracion(false);
            //Activamos envio de acuse de recibo
            mail.setBolAcuseRecibo(false);
            //Obtenemos los usuarios a los que mandaremos el mail
            String strLstMail = "";
            //Validamos si el mail del cliente es valido
            if (mail.isEmail(strCRMEmail)) {
                strLstMail = strCRMEmail;
            }
            //Mandamos mail si hay usuarios
            if (!strLstMail.equals("")) {
                //System.out.println("cuerpo: " + strMsgMail);
                //cuerpo
                strMsgMail = strMsgMail.replace("%CURSO%", getHtml(strNombre));
                strMsgMail = strMsgMail.replace("%CUERPO%", getHtml(strCuerpo));
                strMsgMail = strMsgMail.replace("%AGENTE%", getHtml(strAgente));
                strMsgMail = strMsgMail.replace("%EXTENSION%", strNumero);
                strMsgMail = strMsgMail.replace("%CORREO%", strCorreo);
                strMsgMail = strMsgMail.replace("%CORREO_DESTINO%", getStrCorreoDest());
                strMsgMail = strMsgMail.replace("%ID_MASIVO%", String.valueOf(getIntIdMasivo()));
                strMsgMail = strMsgMail.replace("%ID_CTE%", String.valueOf(getIntIdCte()));

                if (!strNombreContacto.equals("")) {
                    strMsgMail = strMsgMail.replace("%NOMBRE_CONTACTO%", "APRECIABLE: " + strNombreContacto);
                } else {
                    strMsgMail = strMsgMail.replace("%NOMBRE_CONTACTO%", "");
                }
                if (intPreview == 0) {
                    //Establecemos parametros
                    mail.setUsuario(strCorreo);
                    mail.setContrasenia(strPass);
                    mail.setHost(strServer);
                    mail.setPuerto(strPort);
                    mail.setAsunto(lstMail[0].replace("%ASUNTO%", strAsunto));
                    mail.setDestino(strLstMail);
                    mail.setMensaje(strMsgMail);
                    mail.setBolDepuracion(false);
                    mail.setBolMantenerConexion(false); // cierra conexión de correo
                    //Adjuntamos XML y PDF
                    if (strTLS.equals("1")) {
                        mail.setBolUsaTls(true);
                    }
                    if (strSTLS.equals("1")) {
                        mail.setBolUsaStartTls(true);
                    }
                    boolean bol = mail.sendMail();
                    if (!bol) {
                        strResp = "Fallo el envio del Mail.";
                    } else {
                        regMail(getIntIdMasivo(), getIntIdCte(), getStrCorreoDest(), oConn);
                    }
                } else {
                    strResp = strMsgMail;
                }
            }
        }
        return strResp;
    }

    /**
     *
     * @param oConn
     * @param intIDEjecutivo id del ejecutivo
     * @param strCRMEmail ail del lciente
     * @param strSede el nombre de la sede
     * @param strAsunto
     * @param intPreview 0 = manda mail, 1 = preview
     * @param strPlantilla
     * @param intIdPlantilla
     * @return
     * @throws java.sql.SQLException
     * @throws java.text.ParseException
     */
    public String CofideSede(Conexion oConn, int intIDEjecutivo, String strCRMEmail, String strSede, String strAsunto,
            int intPreview, String strPlantilla, int intIdPlantilla) throws SQLException, ParseException {
        //Objetenemso los datos del curso
        String strNombre = ""; //NOMBRE DEL CURSO
        String strFechaIni = ""; //fecha inicial ddbb
        String strHhrsInicio = ""; //hora de inicio
        String strDuracion = ""; //duracion del curso
        int intTipoCurso = 1; //tipo del curso
        String strIdWEb = ""; //id web con el que se liga con la pagina web
        String strClaveCurso = "";
        String strCuerpo = "";
        String strLigaCurso = ""; //liga del curso
        String strAlias = ""; //alias
        String strNombreContacto = getStrNombreContactoCofide(oConn, strCRMEmail);

        int intCursos = 0;
        String strSQL = "select CRCD_ID_CURSO from crm_correos_deta where CRC_ID = " + intIdPlantilla;
        ResultSet rs = oConn.runQuery(strSQL, true);
        while (rs.next()) {
            intCursos = rs.getInt("CRCD_ID_CURSO");
            String strSqlCurso = "select *, (CC_DURACION_HRS + CC_DURACION_HRS2 + CC_DURACION_HRS3) as Duracion from cofide_cursos where CC_ACTIVO = 1 and CC_CURSO_ID = " + intCursos + " order by CC_FECHA_INICIAL ASC";
            ResultSet rsCurso = oConn.runQuery(strSqlCurso, true);
            while (rsCurso.next()) {
                strNombre = rsCurso.getString("CC_NOMBRE_CURSO");
                strClaveCurso = rsCurso.getString("CC_CLAVES");
                strFechaIni = rsCurso.getString("CC_FECHA_INICIAL");
                strHhrsInicio = rsCurso.getString("CC_HR_EVENTO_INI");
                strSede = getSede(oConn, rsCurso.getString("CC_SEDE_ID"));
                strAlias = getAlias(oConn, rsCurso.getString("CC_SEDE_ID"));
                strIdWEb = rsCurso.getString("CC_CURSO_ID");
                strDuracion = rsCurso.getString("Duracion");
//                strLigaCurso = fec.FormateaDDMMAAAA(strFechaIni, "") + strIdWEb + intTipoCurso;
                if (rsCurso.getString("CC_ID_WEB").equals("")) {
                    strLigaCurso = "http://cofide.org/producto/";
                } else {
                    strLigaCurso = rsCurso.getString("CC_ID_WEB");
                }
                strCuerpo += "<tr>"
                        + "<td width=\"490\" style=\"background-color: #F5FAE5; border: 1px solid #99cc00; padding: 10px; -webkit-border-radius: 2px; border-radius: 2px;\">"
                        + "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"
                        + "<tr>"
                        + "<td style=\"font-family: Helvetica, Tahoma, sans-serif; font-size: 22px; font-weight: 100; text-transform: none; border-right: 1px solid #99cc00; padding-right: 10px;\">" + strNombre + "</td> <!-- NOMBRE DEL CURSO -->"
                        + "<td width=\"200\" style=\"padding-left: 10px;\" valign=\"top\">"
                        + "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"2\" align=\"center\">"
                        + "<tr>"
                        + "<td colspan=\"2\" style=\"font-family: Helvetica, Tahoma, sans-serif; font-size: 16px; font-weight: 700; text-transform: none; padding-bottom: 10px;\">" + getFecha(oConn, strIdWEb) + "</td> <!-- DIA. NDIA MES. AÑO -->"
                        + "</tr>"
                        + "<tr>"
                        + "<td style=\"font-family: Helvetica, Tahoma, sans-serif; font-size: 12px; font-weight: 700; text-transform: none;\">Inicio:</td>"
                        + "<td style=\"font-family: Helvetica, Tahoma, sans-serif; font-size: 12px; font-weight: 100; text-transform: none;\">" + strHhrsInicio + " HRS.</td> <!-- HORA DE INICIO -->"
                        + "</tr>"
                        + "<tr>"
                        + "<td style=\"font-family: Helvetica, Tahoma, sans-serif; font-size: 12px; font-weight: 700; text-transform: none;\">Duraci&oacute;n:</td>"
                        + "<td style=\"font-family: Helvetica, Tahoma, sans-serif; font-size: 12px; font-weight: 100; text-transform: none;\">" + strDuracion + " HRS.</td> <!-- DURACION -->"
                        + "</tr>"
                        + "</table>"
                        + "</td>"
                        + "</tr>"
                        + "</table>"
                        + "</td>"
                        + "<td width=\"10\">&nbsp;</td>"
                        + "<td width=\"100\" style=\"background-color: #99cc00; font-family: Helvetica, Tahoma, sans-serif; font-size: 12px; color: #fff; text-decoration: none; font-weight: 700; -webkit-border-radius: 2px; border-radius: 2px; padding: 5px;\" align=\"center\" valign=\"middle\"><a href=\"" + strLigaCurso + "\" target=\"_blank\" style=\"color: #fff; text-decoration: none;\">[+] INFO.<br />TEMARIO</a></td> <!-- LIGA DIRECTA AL CURSO DESC-->"
                        + "</tr>";
            }
            rsCurso.close();
        }
        rs.close();
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
//        String strNomTemplate = "SEDE"; //obtenemos el template del mail para le supervisor
        String[] lstMail = getMailTemplate(oConn, strPlantilla);
        String strMsgMail = lstMail[1];
        //  Si estan llenos todos los datos mandamos el mail

        if (!strServer.equals(
                "") && !strCorreo.equals("") && !strPass.equals("")) {
            //armamos el mail
//            Mail mail = new Mail();.
            mail.setBolDepuracion(false);
            //Activamos envio de acuse de recibo
            mail.setBolAcuseRecibo(false);
            //Obtenemos los usuarios a los que mandaremos el mail
            String strLstMail = "";
            //Validamos si el mail del cliente es valido
            if (mail.isEmail(strCRMEmail)) {
                strLstMail = strCRMEmail;
            }
            //Mandamos mail si hay usuarios
            if (!strLstMail.equals("")) {
                //System.out.println("cuerpo: " + strMsgMail);
                //cuerpo
                strMsgMail = strMsgMail.replace("%ALIAS%", getHtml(strAlias));
                strMsgMail = strMsgMail.replace("%SEDE%", getHtml(strSede));
                strMsgMail = strMsgMail.replace("%CUERPO%", getHtml(strCuerpo));
                strMsgMail = strMsgMail.replace("%AGENTE%", getHtml(strAgente));
                strMsgMail = strMsgMail.replace("%EXTENSION%", strNumero);
                strMsgMail = strMsgMail.replace("%CORREO%", strCorreo);
                strMsgMail = strMsgMail.replace("%CORREO_DESTINO%", getStrCorreoDest());
                strMsgMail = strMsgMail.replace("%ID_MASIVO%", String.valueOf(getIntIdMasivo()));
                strMsgMail = strMsgMail.replace("%ID_CTE%", String.valueOf(getIntIdCte()));

                if (!strNombreContacto.equals("")) {
                    strMsgMail = strMsgMail.replace("%NOMBRE_CONTACTO%", "APRECIABLE: " + strNombreContacto);
                } else {
                    strMsgMail = strMsgMail.replace("%NOMBRE_CONTACTO%", "");
                }
                if (intPreview == 0) {
                    //Establecemos parametros
                    mail.setUsuario(strCorreo);
                    mail.setContrasenia(strPass);
                    mail.setHost(strServer);
                    mail.setPuerto(strPort);
                    mail.setAsunto(lstMail[0].replace("%ASUNTO%", strAsunto));
                    mail.setDestino(strLstMail);
                    mail.setMensaje(strMsgMail);
                    mail.setBolDepuracion(false);
                    mail.setBolMantenerConexion(false); // cierra conexión de correo
                    if (strTLS.equals("1")) {
                        mail.setBolUsaTls(true);
                    }
                    if (strSTLS.equals("1")) {
                        mail.setBolUsaStartTls(true);
                    }
                    boolean bol = mail.sendMail();
                    if (!bol) {
                        strResp = "Fallo el envio del Mail.";
                    } else {
                        regMail(getIntIdMasivo(), getIntIdCte(), getStrCorreoDest(), oConn);
                    }
                } else {
                    strResp = strMsgMail;
                }
            }
        }
        return strResp;
    }

    public String MailGroup(Conexion oConn, int intIDEjecutivo, String strCRMEmail, String strAsunto,
            int intPreview, String strPlantilla, int intIdPlantilla) throws SQLException, ParseException {
        //Objetenemso los datos del curso
        String strNombre = ""; //NOMBRE DEL CURSO
        String strFechaIni = ""; //fecha inicial ddbb
        String strFecha = ""; //fecha inicial formateada
        String strNomMes = ""; //mes nombre
        String strHhrsInicio = ""; //hora de inicio
        String strDuracion = ""; //duracion del curso
        String strSede = ""; //precio
        String strMes = ""; //mes
        int intTipoCurso = 1; //tipo del curso
        String strIdWEb = ""; //id web con el que se liga con la pagina web
        String strEsSeminario = ""; //es seminario ?
        String strEsDiplomado = ""; //es diplomado ? 
        String strTipo = "PRESENCIAL"; //es presencial 
        String strLigaCurso = ""; //liga del curso
        String strCuerpo = "";
        int intCursos = 0;
        String strNombreContacto = getStrNombreContactoCofide(oConn, strCRMEmail);
        String strSQL = "select CRCD_ID_CURSO from crm_correos_deta where CRC_ID = " + intIdPlantilla + " and CRCD_TIPO = 2 and CRCD_ESTATUS = 0";
        ResultSet rs = oConn.runQuery(strSQL, true);
        while (rs.next()) {
            intCursos = rs.getInt("CRCD_ID_CURSO");
            String strSqlCurso = "select CC_ID_WEB,CC_CURSO_ID, CC_NOMBRE_CURSO, CC_FECHA_INICIAL, CC_SEDE_ID, "
                    + "CC_HR_EVENTO_INI, (CC_DURACION_HRS + CC_DURACION_HRS2 + CC_DURACION_HRS3) as Duracion, CC_PRECIO_IVA, CC_ID_WEB, CC_IS_DIPLOMADO, CC_IS_SEMINARIO, CC_IS_PRESENCIAL, CC_IS_ONLINE "
                    + "from cofide_cursos where CC_ACTIVO = 1 and CC_CURSO_ID = " + intCursos + " order by CC_FECHA_INICIAL ASC";
            ResultSet rsCurso = oConn.runQuery(strSqlCurso, true);
            while (rsCurso.next()) {
                strNombre = rsCurso.getString("CC_NOMBRE_CURSO");
                strFechaIni = rsCurso.getString("CC_FECHA_INICIAL");
                strHhrsInicio = rsCurso.getString("CC_HR_EVENTO_INI");
                strDuracion = rsCurso.getString("Duracion");
                strSede = getSede(oConn, rsCurso.getString("CC_SEDE_ID"));
                strIdWEb = rsCurso.getString("CC_CURSO_ID");
                strEsSeminario = rsCurso.getString("CC_IS_SEMINARIO");
                strEsDiplomado = rsCurso.getString("CC_IS_DIPLOMADO");

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

                strCuerpo += "        <table width=\"700\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" style=\"padding-bottom: 10px;\">"
                        + "          <tr>"
                        + "            <td width=\"490\" style=\"background-color: #F5FAE5; border: 1px solid #99cc00; padding: 10px; -webkit-border-radius: 2px; border-radius: 2px;\">"
                        + "            "
                        + "                <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"
                        + "                  <tr>"
                        + "                    <td style=\"font-family: Helvetica, Tahoma, sans-serif; font-size: 22px; font-weight: 100; text-transform: none; border-right: 1px solid #99cc00; padding-right: 10px;\">" + strNombre + "</td>"
                        + "                    <td width=\"200\" style=\"padding-left: 10px;\" valign=\"top\">"
                        + "                    "
                        + "                        <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"2\" align=\"center\">"
                        + "                          <tr>"
                        + "                            <td colspan=\"2\" style=\"font-family: Helvetica, Tahoma, sans-serif; font-size: 16px; font-weight: 700; text-transform: none; padding-bottom: 10px;\">" + getFecha(oConn, strIdWEb) + "</td>"
                        + "                          </tr>"
                        + "                          <tr>"
                        + "                            <td style=\"font-family: Helvetica, Tahoma, sans-serif; font-size: 12px; font-weight: 700; text-transform: none;\">Inicio:</td>"
                        + "                            <td style=\"font-family: Helvetica, Tahoma, sans-serif; font-size: 12px; font-weight: 100; text-transform: none;\">" + strHhrsInicio + " HRS.</td>"
                        + "                          </tr>"
                        + "                          <tr>"
                        + "                            <td style=\"font-family: Helvetica, Tahoma, sans-serif; font-size: 12px; font-weight: 700; text-transform: none;\">Duraci&oacute;n:</td>"
                        + "                            <td style=\"font-family: Helvetica, Tahoma, sans-serif; font-size: 12px; font-weight: 100; text-transform: none;\">" + strDuracion + " HRS.</td>"
                        + "                          </tr>"
                        + "                          <tr>"
                        + "                            <td style=\"font-family: Helvetica, Tahoma, sans-serif; font-size: 12px; font-weight: 700; text-transform: none;\">Sede:</td>"
                        + "                            <td style=\"font-family: Helvetica, Tahoma, sans-serif; font-size: 12px; font-weight: 100; text-transform: none;\">" + strSede + "</td>"
                        + "                          </tr>"
                        + "                          <tr>"
                        + "                            <td style=\"font-family: Helvetica, Tahoma, sans-serif; font-size: 12px; font-weight: 700; text-transform: none;\">Modalidad:</td>"
                        + "                            <td style=\"font-family: Helvetica, Tahoma, sans-serif; font-size: 12px; font-weight: 100; text-transform: none;\">" + strTipo + "</td>"
                        + "                          </tr>";
                if (rsCurso.getInt("CC_IS_ONLINE") == 1) {
                    strCuerpo += "                 <tr>"
                            + "                            <td style=\"font-family: Helvetica, Tahoma, sans-serif; font-size: 12px; font-weight: 700; text-transform: none;\">&nbsp;</td>"
                            + "                            <td style=\"font-family: Helvetica, Tahoma, sans-serif; font-size: 12px; font-weight: 100; text-transform: none;\">ONLINE</td> <!-- IF SI ES ONLINE, IMPRIMIR, SI NO, DEJAR EN BLANCO -->"
                            + "                          </tr>";
                }
                strCuerpo += "             </table>"
                        + "                    "
                        + "                    </td>"
                        + "                  </tr>"
                        + "                </table>"
                        + ""
                        + "            </td>"
                        + "            <td width=\"10\">&nbsp;</td>"
                        + "            <td width=\"100\" style=\"background-color: #99cc00; font-family: Helvetica, Tahoma, sans-serif; font-size: 12px; color: #fff; text-decoration: none; font-weight: 700; -webkit-border-radius: 2px; border-radius: 2px; padding: 5px;\" align=\"center\" valign=\"middle\"><a href=\"" + strLigaCurso + "\" target=\"_blank\" style=\"color: #fff; text-decoration: none;\">[+] INFO.<br />TEMARIO</a></td>"
                        + "          </tr>"
                        + "        </table>";
            }
            rsCurso.close();
        }
        rs.close();
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
//        String strNomTemplate = strPlantilla; //obtenemos el template del mail para le supervisor
        String[] lstMail = getMailTemplate(oConn, strPlantilla);
        String strMsgMail = lstMail[1];
        //  Si estan llenos todos los datos mandamos el mail
        if (!strServer.equals("") && !strCorreo.equals("") && !strPass.equals("")) {
            //armamos el mail
//            Mail mail = new Mail();
            mail.setBolDepuracion(false);
            //Activamos envio de acuse de recibo
            mail.setBolAcuseRecibo(false);
            //Obtenemos los usuarios a los que mandaremos el mail
            String strLstMail = "";
            //Validamos si el mail del cliente es valido
            if (mail.isEmail(strCRMEmail)) {
                strLstMail = strCRMEmail;
            }
            //Mandamos mail si hay usuarios
            if (!strLstMail.equals("")) {
//                System.out.println("cuerpo: " + strMsgMail);
                //cuerpo
                strMsgMail = strMsgMail.replace("%CUERPO%", getHtml(strCuerpo));
                strMsgMail = strMsgMail.replace("%AGENTE%", getHtml(strAgente));
                strMsgMail = strMsgMail.replace("%EXTENSION%", strNumero);
                strMsgMail = strMsgMail.replace("%CORREO%", strCorreo);
                if (!strNombreContacto.equals("")) {
                    strMsgMail = strMsgMail.replace("%NOMBRE_CONTACTO%", "APRECIABLE: " + strNombreContacto);
                } else {
                    strMsgMail = strMsgMail.replace("%NOMBRE_CONTACTO%", "");
                }
                //manda el mail = 0, o muestra preview = 1
                if (intPreview == 0) {
                    //Establecemos parametros
                    mail.setUsuario(strCorreo);
                    mail.setContrasenia(strPass);
                    mail.setHost(strServer);
                    mail.setPuerto(strPort);
                    mail.setAsunto(lstMail[0].replace("%ASUNTO%", strAsunto));
                    mail.setDestino(strLstMail);
                    mail.setMensaje(strMsgMail);
                    mail.setBolDepuracion(false);
                    mail.setBolMantenerConexion(false); // cierra conexión de correo
                    //Adjuntamos XML y PDF
                    if (strTLS.equals("1")) {
                        mail.setBolUsaTls(true);
                    }
                    if (strSTLS.equals("1")) {
                        mail.setBolUsaStartTls(true);
                    }
                    boolean bol = mail.sendMail();
                    if (!bol) {
                        strResp = "Fallo el envio del Mail.";
                    } else {
                        //si fue exitoso el envio, pone la plantilla como okis = 1
                        strSQL = "update crm_correos_deta set CRCD_ESTATUS = 1 where CRC_ID = " + intIdPlantilla;
                        oConn.runQueryLMD(strSQL);
                    }
                } else {
                    strResp = strMsgMail; // manda el cuerpo del mail
                }
            }
        }
        return strResp;
    }

    public String MailDirecto(Conexion oConn, String strMailCte, int intIdCurso, int intIDEjecutivo) throws SQLException, ParseException {
        String strRespuesta = "OK";
        //Objetenemso los datos del curso
        String strNombre = ""; //NOMBRE DEL CURSO
        String strFechaIni = ""; //fecha inicial ddbb
        String strHhrsInicio = ""; //hora de inicio
        String strDuracion = ""; //duracion del curso
        String strSede = ""; //precio
        int intTipoCurso = 1; //tipo del curso
        String strIdWEb = ""; //id web con el que se liga con la pagina web
        String strEsSeminario = ""; //es seminario ?
        String strEsDiplomado = ""; //es diplomado ? 
        String strTipo = "PRESENCIAL"; //es presencial 
        String strLigaCurso = ""; //liga del curso
        String strObjetivo = "";
        String strDirigido = "";
        String strIncluye = "";
        String strPrecio = "0.00";
        String strPrecioOnline = "0.00";
        String strTemario = "";
        String strTipoCurso = "";
        String strPrecioOK = "";
        String strNombreContacto = getStrNombreContactoCofide(oConn, strMailCte);
        String strFechaAlias = ""; //para cuando son para mas de un día
        String strSqlCurso = "select CC_ID_WEB, CC_CURSO_ID, CC_NOMBRE_CURSO, CC_FECHA_INICIAL, CC_SEDE_ID, "
                + "CC_HR_EVENTO_INI, (CC_DURACION_HRS + CC_DURACION_HRS2 + CC_DURACION_HRS3) as Duracion, CC_PRECIO_PRES, CC_PRECIO_ON, CC_ID_WEB, CC_IS_DIPLOMADO, CC_IS_SEMINARIO, CC_IS_PRESENCIAL, CC_IS_ONLINE,"
                + "CC_OBJETIVO, CC_DIRIGIDO, CC_DETALLE, CC_TEMARIO "
                + "from cofide_cursos where CC_ACTIVO = 1 and CC_CURSO_ID = " + intIdCurso + " order by CC_FECHA_INICIAL ASC";
        ResultSet rsCurso = oConn.runQuery(strSqlCurso, true);
        while (rsCurso.next()) {
            strNombre = rsCurso.getString("CC_NOMBRE_CURSO");
            strFechaIni = rsCurso.getString("CC_FECHA_INICIAL");
            strHhrsInicio = rsCurso.getString("CC_HR_EVENTO_INI");
            strDuracion = rsCurso.getString("Duracion");
            strSede = getSede(oConn, rsCurso.getString("CC_SEDE_ID"));
            strIdWEb = rsCurso.getString("CC_CURSO_ID");
            strEsSeminario = rsCurso.getString("CC_IS_SEMINARIO");
            strEsDiplomado = rsCurso.getString("CC_IS_DIPLOMADO");
            strObjetivo = rsCurso.getString("CC_OBJETIVO");
            strDirigido = rsCurso.getString("CC_DIRIGIDO");
            strIncluye = rsCurso.getString("CC_DETALLE");
            strIncluye = rsCurso.getString("CC_DETALLE");
            System.out.println("precio presencial : " + rsCurso.getString("CC_PRECIO_PRES"));
            if (!rsCurso.getString("CC_PRECIO_PRES").equals("")) {
                strPrecio = setPrecioOk(rsCurso.getString("CC_PRECIO_PRES"));
            }
            System.out.println("precio presencial : " + strPrecio);
            if (!rsCurso.getString("CC_PRECIO_ON").equals("0")) {
                strPrecioOnline = setPrecioOk(rsCurso.getString("CC_PRECIO_ON"));
                strPrecioOK = "En l&iacute;nea: $ " + strPrecioOnline + " + IVA";
            }
//            strPrecio = rsCurso.getString("CC_PRECIO_PRES");
//            strPrecioOnline = rsCurso.getString("CC_PRECIO_ON");
            strTemario = rsCurso.getString("CC_TEMARIO");
            if (strEsSeminario.equals("1")) {
                intTipoCurso = 3;
                strTipoCurso = "Seminario Completo";
            }
            if (strEsDiplomado.equals("1")) {
                intTipoCurso = 2;
                strTipoCurso = "Diplomado Completo";
            }
//            strLigaCurso = fec.FormateaDDMMAAAA(strFechaIni, "") + strIdWEb + intTipoCurso;
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
        String strNomTemplate = "INDIVIDUAL"; //obtenemos el template del mail para le supervisor
        String[] lstMail = getMailTemplate(oConn, strNomTemplate);
        String strMsgMail = lstMail[1];
        //  Si estan llenos todos los datos mandamos el mail
        if (!strServer.equals("") && !strCorreo.equals("") && !strPass.equals("")) {
            //armamos el mail
//            Mail mail = new Mail();
            mail.setBolDepuracion(false);
            //Activamos envio de acuse de recibo
            mail.setBolAcuseRecibo(false);
            //Obtenemos los usuarios a los que mandaremos el mail
            String strLstMail = "";
            //Validamos si el mail del cliente es valido
            if (mail.isEmail(strMailCte)) {
                strLstMail = strMailCte;
            }
            //Mandamos mail si hay usuarios
            if (!strLstMail.equals("")) {
                //cuerpo
                strMsgMail = strMsgMail.replace("%CURSO%", getHtml(strNombre));
                strMsgMail = strMsgMail.replace("%FECHA%", getHtml(getFecha(oConn, strIdWEb)));
                strMsgMail = strMsgMail.replace("%HORA%", strHhrsInicio);
                strMsgMail = strMsgMail.replace("%DURACION%", strDuracion);
                strMsgMail = strMsgMail.replace("%SEDE%", getHtml(strSede));
                strMsgMail = strMsgMail.replace("%PRECIO_PRES%", strPrecio);
//                strMsgMail = strMsgMail.replace("%PRES_ONLINE%", strPrecioOnline);
                strMsgMail = strMsgMail.replace("%PRES_ONLINE%", strPrecioOK);
                strMsgMail = strMsgMail.replace("%OBJETIVO%", getHtml(strObjetivo));
                strMsgMail = strMsgMail.replace("%DIRIGIDO%", getHtml(strDirigido));
                strMsgMail = strMsgMail.replace("%INCLUYE%", getHtml(strIncluye));
                strMsgMail = strMsgMail.replace("%AGENTE%", getHtml(strAgente));
                strMsgMail = strMsgMail.replace("%EXTENSION%", strNumero);
                strMsgMail = strMsgMail.replace("%CORREO%", strCorreo);
                strMsgMail = strMsgMail.replace("%LINK%", strLigaCurso);
                strMsgMail = strMsgMail.replace("%TEMARIO%", getHtml(strTemario));
                strMsgMail = strMsgMail.replace("%SEM_DIP%", strTipoCurso);
                if (!strNombreContacto.equals("")) {
                    strMsgMail = strMsgMail.replace("%NOMBRE_CONTACTO%", "APRECIABLE: " + strNombreContacto);
                } else {
                    strMsgMail = strMsgMail.replace("%NOMBRE_CONTACTO%", "");
                }
                //Establecemos parametros
                mail.setUsuario(strCorreo);
                mail.setContrasenia(strPass);
                mail.setHost(strServer);
                mail.setPuerto(strPort);
//                mail.setAsunto(lstMail[0].replace("%ASUNTO%", strAsunto));
                mail.setAsunto(lstMail[0].replace("%CURSO%", strNombre));
                mail.setDestino(strLstMail);
                mail.setMensaje(strMsgMail);
                mail.setBolDepuracion(false);
                mail.setBolMantenerConexion(false); // cierra conexión de correo
                //Adjuntamos XML y PDF
                if (strTLS.equals("1")) {
                    mail.setBolUsaTls(true);
                }
                if (strSTLS.equals("1")) {
                    mail.setBolUsaStartTls(true);
                }
                boolean bol = mail.sendMail();
                if (!bol) {
                    strRespuesta = "Fallo el envio del Mail.";
                }
            }
        }
//        System.out.println("correo: \n" + strMsgMail);
        return strRespuesta;
    }

    //mensual
    public String CofideMensual(Conexion oConn, int intIDEjecutivo, String strCRMEmail, String strAsunto,
            int intPreview, String strPlantilla, int intIdPlantilla) throws SQLException, ParseException {
        String strNombre = ""; //NOMBRE DEL CURSO
        String strFechaIni = ""; //fecha inicial ddbb
        String strHhrsInicio = ""; //hora de inicio
        String strDuracion = ""; //duracion del curso
        String strSede = ""; //precio
        int intTipoCurso = 1; //tipo del curso
        String strIdWEb = ""; //id web con el que se liga con la pagina web
        String strEsSeminario = ""; //es seminario ?
        String strEsDiplomado = ""; //es diplomado ? 
        String strTipo = "PRESENCIAL"; //es presencial 
        String strLigaCurso = ""; //liga del curso
        String strCabeza = "";
        String strCuerpo = "";
        String strNombreContacto = getStrNombreContactoCofide(oConn, strCRMEmail);
        ResultSet rs;

        String strFecha = "";
//        String strSqlFecha = "select substring(replace(DATE_ADD(CURDATE(),INTERVAL 1 MONTH),'-',''),1,6) as strFecha";
        String strSqlFecha = "select CRC_ID, CRCD_ID_CURSO,"
                + "(select left(CC_FECHA_INICIAL,6) from cofide_cursos where CC_CURSO_ID = CRCD_ID_CURSO) as strFecha "
                + "from crm_correos_deta "
                + "where CRC_ID = " + intIdPlantilla
                + " order by CRCD_ID desc "
                + "limit 1";
        rs = oConn.runQuery(strSqlFecha, true);
        while (rs.next()) {
            strFecha = rs.getString("strFecha");
        }
        rs.close();

        String strSqlCursos = "select *,(CC_DURACION_HRS + CC_DURACION_HRS2 + CC_DURACION_HRS3) as Duracion "
                + "from cofide_cursos "
                + "where CC_ACTIVO = 1 AND CC_CLAVES <> '' AND left(CC_FECHA_INICIAL,6)= '" + strFecha + "'  and CC_CURSO_ID <> 1 "
                + "order by CC_FECHA_INICIAL ASC";
        rs = oConn.runQuery(strSqlCursos, true);
        while (rs.next()) {
            strNombre = rs.getString("CC_NOMBRE_CURSO");
            strFechaIni = rs.getString("CC_FECHA_INICIAL");
            strHhrsInicio = rs.getString("CC_HR_EVENTO_INI");
            strDuracion = rs.getString("Duracion");
            strSede = getSede(oConn, rs.getString("CC_SEDE_ID"));
            strIdWEb = rs.getString("CC_CURSO_ID"); //
            if (strEsSeminario.equals("1")) {
                intTipoCurso = 3;
            }
            if (strEsDiplomado.equals("1")) {
                intTipoCurso = 2;
            }
//            strLigaCurso = fec.FormateaDDMMAAAA(strFechaIni, "") + strIdWEb + intTipoCurso;
            if (rs.getString("CC_ID_WEB").equals("")) {
                strLigaCurso = "http://cofide.org/producto/";
            } else {
                strLigaCurso = rs.getString("CC_ID_WEB");
            }
            strCuerpo += "<table width=\"700\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" style=\"padding-bottom: 10px;\">"
                    + "          <tr>"
                    + "            <td width=\"490\" style=\"background-color: #F5FAE5; border: 1px solid #99cc00; padding: 10px; -webkit-border-radius: 2px; border-radius: 2px;\">"
                    + "            "
                    + "                <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"
                    + "                  <tr>"
                    + "                    <td style=\"font-family: Helvetica, Tahoma, sans-serif; font-size: 22px; font-weight: 100; text-transform: none; border-right: 1px solid #99cc00; padding-right: 10px;\">" + strNombre + "</td> <!-- NOMBRE DEL CURSO -->"
                    + "                    <td width=\"200\" style=\"padding-left: 10px;\" valign=\"top\">"
                    + "                    "
                    + "                        <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"2\" align=\"center\">"
                    + "                          <tr>"
                    + "                            <td colspan=\"2\" style=\"font-family: Helvetica, Tahoma, sans-serif; font-size: 16px; font-weight: 700; text-transform: none; padding-bottom: 10px;\">" + getFecha(oConn, strIdWEb) + "</td>"
                    + "                          </tr>"
                    + "                          <tr>"
                    + "                            <td style=\"font-family: Helvetica, Tahoma, sans-serif; font-size: 12px; font-weight: 700; text-transform: none;\">Inicio:</td>"
                    + "                            <td style=\"font-family: Helvetica, Tahoma, sans-serif; font-size: 12px; font-weight: 100; text-transform: none;\">" + strHhrsInicio + " HRS.</td>"
                    + "                          </tr>"
                    + "                          <tr>"
                    + "                            <td style=\"font-family: Helvetica, Tahoma, sans-serif; font-size: 12px; font-weight: 700; text-transform: none;\">Duraci&oacute;n:</td>"
                    + "                            <td style=\"font-family: Helvetica, Tahoma, sans-serif; font-size: 12px; font-weight: 100; text-transform: none;\">" + strDuracion + " HRS.</td>"
                    + "                          </tr>"
                    + "                          <tr>"
                    + "                            <td style=\"font-family: Helvetica, Tahoma, sans-serif; font-size: 12px; font-weight: 700; text-transform: none;\">Sede:</td>"
                    + "                            <td style=\"font-family: Helvetica, Tahoma, sans-serif; font-size: 12px; font-weight: 100; text-transform: none;\">" + strSede + "</td>"
                    + "                          </tr>"
                    + "                          <tr>"
                    + "                            <td style=\"font-family: Helvetica, Tahoma, sans-serif; font-size: 12px; font-weight: 700; text-transform: none;\">Modalidad:</td>"
                    + "                            <td style=\"font-family: Helvetica, Tahoma, sans-serif; font-size: 12px; font-weight: 100; text-transform: none;\">" + strTipo + "</td>"
                    + "                          </tr>";
            if (rs.getString("CC_IS_ONLINE").equals("1")) {
                strCuerpo += "                          <tr>"
                        + "                            <td style=\"font-family: Helvetica, Tahoma, sans-serif; font-size: 12px; font-weight: 700; text-transform: none;\">&nbsp;</td>"
                        + "                            <td style=\"font-family: Helvetica, Tahoma, sans-serif; font-size: 12px; font-weight: 100; text-transform: none;\">ONLINE</td> <!-- IF ES ONLINE IMPRIMIR ESTO, SI NO NO -->"
                        + "                          </tr>";
            }
            strCuerpo += "                        </table>"
                    + "                    "
                    + "                    </td>"
                    + "                  </tr>"
                    + "                </table>"
                    + ""
                    + "            </td>"
                    + "            <td width=\"10\">&nbsp;</td>"
                    + "            <td width=\"100\" style=\"background-color: #99cc00; font-family: Helvetica, Tahoma, sans-serif; font-size: 12px; color: #fff; text-decoration: none; font-weight: 700; -webkit-border-radius: 2px; border-radius: 2px; padding: 5px;\" align=\"center\" valign=\"middle\"><a href=\"" + strLigaCurso + "\" target=\"_blank\" style=\"color: #fff; text-decoration: none;\">[+] INFO.<br />TEMARIO</a></td>"
                    + "          </tr>"
                    + "        </table>";
        }
        rs.close();
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
        String strSqlEjecutivo = "select id_usuarios, CONCAT(nombre_usuario,' ',apellido_paterno) as nombre_usuario , TELEFONO, NUM_EXT,"
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
        String[] lstMail = getMailTemplate(oConn, strPlantilla);
        String strMsgMail = lstMail[1];
        //  Si estan llenos todos los datos mandamos el mail
        if (!strServer.equals("") && !strCorreo.equals("") && !strPass.equals("")) {
            //armamos el mail
//            Mail mail = new Mail();
            mail.setBolDepuracion(false);
            //Activamos envio de acuse de recibo
            mail.setBolAcuseRecibo(false);
            //Obtenemos los usuarios a los que mandaremos el mail
            String strLstMail = "";
            //Validamos si el mail del cliente es valido
            if (mail.isEmail(strCRMEmail)) {
                strLstMail = strCRMEmail;
            }
            //Mandamos mail si hay usuarios
            if (!strLstMail.equals("")) {
                //cuerpo
                strMsgMail = strMsgMail.replace("%FECHA_MES%", getHtml(getFechaMens(strFechaIni)));
                strMsgMail = strMsgMail.replace("%AGENTE%", getHtml(strAgente));
                strMsgMail = strMsgMail.replace("%EXTENSION%", strNumero);
                strMsgMail = strMsgMail.replace("%CUERPO%", getHtml(strCuerpo));
                strMsgMail = strMsgMail.replace("%CORREO%", strCorreo);
                strMsgMail = strMsgMail.replace("%CORREO_DESTINO%", getStrCorreoDest());
                strMsgMail = strMsgMail.replace("%ID_MASIVO%", String.valueOf(getIntIdMasivo()));
                strMsgMail = strMsgMail.replace("%ID_CTE%", String.valueOf(getIntIdCte()));

                if (!strNombreContacto.equals("")) {
                    strMsgMail = strMsgMail.replace("%NOMBRE_CONTACTO%", "APRECIABLE: " + strNombreContacto);
                } else {
                    strMsgMail = strMsgMail.replace("%NOMBRE_CONTACTO%", "");
                }
                //manda el mail = 0, o muestra preview = 1
                if (intPreview == 0) {
                    //Establecemos parametros
                    mail.setUsuario(strCorreo);
                    mail.setContrasenia(strPass);
                    mail.setHost(strServer);
                    mail.setPuerto(strPort);
                    mail.setAsunto(lstMail[0].replace("%ASUNTO%", strAsunto));
                    mail.setDestino(strLstMail);
                    mail.setMensaje(strMsgMail);
                    mail.setBolDepuracion(false);
                    mail.setBolMantenerConexion(false); // cierra conexión de correo
                    //Adjuntamos XML y PDF
                    if (strTLS.equals("1")) {
                        mail.setBolUsaTls(true);
                    }
                    if (strSTLS.equals("1")) {
                        mail.setBolUsaStartTls(true);
                    }
                    boolean bol = mail.sendMail();
                    if (!bol) {
                        strResp = "Fallo el envio del Mail.";
                    } else {
                        regMail(getIntIdMasivo(), getIntIdCte(), getStrCorreoDest(), oConn);
                    }
                } else {
                    strResp = strMsgMail;
                }
            }
        }
        return strResp;
    }

    public String CofideMensualDirecto(Conexion oConn, int intIDEjecutivo, String strCRMEmail, String strAsunto, String strMes) throws SQLException, ParseException {

        String strNombre = ""; //NOMBRE DEL CURSO
        String strFechaIni = ""; //fecha inicial ddbb
        String strHhrsInicio = ""; //hora de inicio
        String strDuracion = ""; //duracion del curso
        String strSede = ""; //precio
        int intTipoCurso = 1; //tipo del curso
        String strIdWEb = ""; //id web con el que se liga con la pagina web
        String strEsSeminario = ""; //es seminario ?
        String strEsDiplomado = ""; //es diplomado ? 
        String strTipo = "PRESENCIAL"; //es presencial 
        String strLigaCurso = ""; //liga del curso
        String strCabeza = "";
        String strCuerpo = "";
        ResultSet rs;
        String strFechaInicio = "";
        String strFechaFinal = "";
        String strNombreContacto = getStrNombreContactoCofide(oConn, strCRMEmail);
        //si manda fecha actual se hace esta fecha, si no, manda el mes completo
        if (strMes.equals(fec.getFechaActual().substring(4, 6))) { //mes actual = mes de consulta

            strFechaInicio = fec.getAnioActual() + strMes + fec.getFechaActual().substring(6, 8);//año,mes,día      
            strFechaFinal = fec.getAnioActual() + strMes + "31";

        } else { //mes de consulta

            // si el mes es menor al mes actual, va a mandar el mes del año que viene
            if (Integer.parseInt(strMes) < fec.getMesActual()) {

                strFechaInicio = (fec.getAnioActual() + 1) + strMes + "01";
                strFechaFinal = (fec.getAnioActual() + 1) + strMes + "31";

                //si el mes es igual o mayor al actual, va a mandar del año en curso
            } else {

                strFechaInicio = fec.getAnioActual() + strMes + "01";
                strFechaFinal = fec.getAnioActual() + strMes + "31";

            }
//            strFechaInicio = fec.getAnioActual() + strMes + "01";
        }
//        strFechaFinal = fec.getAnioActual() + strMes + "31";
        System.out.println("\n\n######################### fechas: " + strFechaInicio + " / " + strFechaFinal + "\n\n");

        String strSqlCursos = "select *, (CC_DURACION_HRS + CC_DURACION_HRS2 + CC_DURACION_HRS3) as Duracion"
                + " from cofide_cursos "
                + "where CC_ACTIVO = 1 AND CC_CLAVES <> ''  and CC_CURSO_ID <> 1 "
                + "AND CC_FECHA_INICIAL between '" + strFechaInicio + "' and '" + strFechaFinal + "' order by CC_FECHA_INICIAL ASC";
        rs = oConn.runQuery(strSqlCursos, true);
        while (rs.next()) {
            strNombre = rs.getString("CC_NOMBRE_CURSO");
            strFechaIni = rs.getString("CC_FECHA_INICIAL");
            strHhrsInicio = rs.getString("CC_HR_EVENTO_INI");
            strDuracion = rs.getString("Duracion");
            strSede = getSede(oConn, rs.getString("CC_SEDE_ID"));
            strIdWEb = rs.getString("CC_CURSO_ID"); //
            if (strEsSeminario.equals("1")) {
                intTipoCurso = 3;
            }
            if (strEsDiplomado.equals("1")) {
                intTipoCurso = 2;
            }
//            strLigaCurso = fec.FormateaDDMMAAAA(strFechaIni, "") + strIdWEb + intTipoCurso;
            if (rs.getString("CC_ID_WEB").equals("")) {
                strLigaCurso = "http://cofide.org/producto/";
            } else {
                strLigaCurso = rs.getString("CC_ID_WEB");
            }
            strCuerpo += "<table width=\"700\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" style=\"padding-bottom: 10px;\">"
                    + "          <tr>"
                    + "            <td width=\"490\" style=\"background-color: #F5FAE5; border: 1px solid #99cc00; padding: 10px; -webkit-border-radius: 2px; border-radius: 2px;\">"
                    + "            "
                    + "                <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"
                    + "                  <tr>"
                    + "                    <td style=\"font-family: Helvetica, Tahoma, sans-serif; font-size: 22px; font-weight: 100; text-transform: none; border-right: 1px solid #99cc00; padding-right: 10px;\">" + strNombre + "</td> <!-- NOMBRE DEL CURSO -->"
                    + "                    <td width=\"200\" style=\"padding-left: 10px;\" valign=\"top\">"
                    + "                    "
                    + "                        <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"2\" align=\"center\">"
                    + "                          <tr>"
                    + "                            <td colspan=\"2\" style=\"font-family: Helvetica, Tahoma, sans-serif; font-size: 16px; font-weight: 700; text-transform: none; padding-bottom: 10px;\">" + getFecha(oConn, strIdWEb) + "</td>"
                    + "                          </tr>"
                    + "                          <tr>"
                    + "                            <td style=\"font-family: Helvetica, Tahoma, sans-serif; font-size: 12px; font-weight: 700; text-transform: none;\">Inicio:</td>"
                    + "                            <td style=\"font-family: Helvetica, Tahoma, sans-serif; font-size: 12px; font-weight: 100; text-transform: none;\">" + strHhrsInicio + " HRS.</td>"
                    + "                          </tr>"
                    + "                          <tr>"
                    + "                            <td style=\"font-family: Helvetica, Tahoma, sans-serif; font-size: 12px; font-weight: 700; text-transform: none;\">Duraci&oacute;n:</td>"
                    + "                            <td style=\"font-family: Helvetica, Tahoma, sans-serif; font-size: 12px; font-weight: 100; text-transform: none;\">" + strDuracion + " HRS.</td>"
                    + "                          </tr>"
                    + "                          <tr>"
                    + "                            <td style=\"font-family: Helvetica, Tahoma, sans-serif; font-size: 12px; font-weight: 700; text-transform: none;\">Sede:</td>"
                    + "                            <td style=\"font-family: Helvetica, Tahoma, sans-serif; font-size: 12px; font-weight: 100; text-transform: none;\">" + strSede + "</td>"
                    + "                          </tr>"
                    + "                          <tr>"
                    + "                            <td style=\"font-family: Helvetica, Tahoma, sans-serif; font-size: 12px; font-weight: 700; text-transform: none;\">Modalidad:</td>"
                    + "                            <td style=\"font-family: Helvetica, Tahoma, sans-serif; font-size: 12px; font-weight: 100; text-transform: none;\">" + strTipo + "</td>"
                    + "                          </tr>";
            if (rs.getString("CC_IS_ONLINE").equals("1")) {
                strCuerpo += "                          <tr>"
                        + "                            <td style=\"font-family: Helvetica, Tahoma, sans-serif; font-size: 12px; font-weight: 700; text-transform: none;\">&nbsp;</td>"
                        + "                            <td style=\"font-family: Helvetica, Tahoma, sans-serif; font-size: 12px; font-weight: 100; text-transform: none;\">ONLINE</td> <!-- IF ES ONLINE IMPRIMIR ESTO, SI NO NO -->"
                        + "                          </tr>";
            }
            strCuerpo += "                        </table>"
                    + "                    "
                    + "                    </td>"
                    + "                  </tr>"
                    + "                </table>"
                    + ""
                    + "            </td>"
                    + "            <td width=\"10\">&nbsp;</td>"
                    + "            <td width=\"100\" style=\"background-color: #99cc00; font-family: Helvetica, Tahoma, sans-serif; font-size: 12px; color: #fff; text-decoration: none; font-weight: 700; -webkit-border-radius: 2px; border-radius: 2px; padding: 5px;\" align=\"center\" valign=\"middle\"><a href=\"" + strLigaCurso + "\" target=\"_blank\" style=\"color: #fff; text-decoration: none;\">[+] INFO.<br />TEMARIO</a></td>"
                    + "          </tr>"
                    + "        </table>";
        }
        rs.close();
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
        String strSqlEjecutivo = "select id_usuarios, CONCAT(nombre_usuario,' ',apellido_paterno) as nombre_usuario , TELEFONO, NUM_EXT,"
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
        String[] lstMail = getMailTemplate(oConn, "MES");
        String strMsgMail = lstMail[1];
        //  Si estan llenos todos los datos mandamos el mail
        if (!strServer.equals("") && !strCorreo.equals("") && !strPass.equals("")) {
            //armamos el mail
//            Mail mail = new Mail();
            mail.setBolDepuracion(false);
            //Activamos envio de acuse de recibo
            mail.setBolAcuseRecibo(false);
            //Obtenemos los usuarios a los que mandaremos el mail
            String strLstMail = "";
            //Validamos si el mail del cliente es valido
            if (mail.isEmail(strCRMEmail)) {
                strLstMail = strCRMEmail;
            }
            //Mandamos mail si hay usuarios
            if (!strLstMail.equals("")) {
                //cuerpo
                strMsgMail = strMsgMail.replace("%FECHA_MES%", getHtml(getFechaMens(strFechaIni)));
                strMsgMail = strMsgMail.replace("%AGENTE%", getHtml(strAgente));
                strMsgMail = strMsgMail.replace("%EXTENSION%", strNumero);
                strMsgMail = strMsgMail.replace("%CUERPO%", getHtml(strCuerpo));
                strMsgMail = strMsgMail.replace("%CORREO%", strCorreo);
                if (!strNombreContacto.equals("")) {
                    strMsgMail = strMsgMail.replace("%NOMBRE_CONTACTO%", "APRECIABLE: " + strNombreContacto);
                } else {
                    strMsgMail = strMsgMail.replace("%NOMBRE_CONTACTO%", "");
                }
                //manda el mail = 0, o muestra preview = 1               
                //Establecemos parametros
                mail.setUsuario(strCorreo);
                mail.setContrasenia(strPass);
                mail.setHost(strServer);
                mail.setPuerto(strPort);
                mail.setAsunto(lstMail[0].replace("%ASUNTO%", strAsunto));
                mail.setDestino(strLstMail);
                mail.setMensaje(strMsgMail);
                mail.setBolDepuracion(false);
                mail.setBolMantenerConexion(false); // cierra conexión de correo
                //Adjuntamos XML y PDF
                if (strTLS.equals("1")) {
                    mail.setBolUsaTls(true);
                }
                if (strSTLS.equals("1")) {
                    mail.setBolUsaStartTls(true);
                }
                boolean bol = mail.sendMail();
                if (!bol) {
                    strResp = "Fallo el envio del Mail.";
                }
            }
        }
        return strResp;
    }

    /**
     *
     * @param oConn
     * @param intIDEjecutivo
     * @param strCRMEmail
     * @param strSede
     * @param strAsunto
     * @param intPreview
     * @param strPlantilla
     * @param intIdPlantilla
     * @return
     * @throws SQLException
     * @throws ParseException
     */
    public String CofideSedeDirecto(Conexion oConn, int intIDEjecutivo, String strCRMEmail, String strSede, String strAsunto) throws SQLException, ParseException {
        //Objetenemso los datos del curso
        String strNombre = ""; //NOMBRE DEL CURSO
        String strFechaIni = ""; //fecha inicial ddbb
        String strHhrsInicio = ""; //hora de inicio
        String strDuracion = ""; //duracion del curso
        int intTipoCurso = 1; //tipo del curso
        String strIdWEb = ""; //id web con el que se liga con la pagina web
        String strClaveCurso = "";
        String strCuerpo = "";
        String strLigaCurso = ""; //liga del curso
        String strAlias = ""; //alias
        String strNombreContacto = getStrNombreContactoCofide(oConn, strCRMEmail);

        int intCursos = 0;
        String strSqlCurso = "select *, "
                + "(CC_DURACION_HRS + CC_DURACION_HRS2 + CC_DURACION_HRS3) as Duracion "
                + "from cofide_cursos where CC_ACTIVO = 1  and CC_CURSO_ID <> 1 "
                + "and CC_FECHA_INICIAL BETWEEN '" + fec.getFechaActual() + "' and '" + fec.addFecha(fec.getFechaActual(), 2, 3) + "' "
                + " and CC_SEDE = '" + strSede + "' "
                + "order by CC_FECHA_INICIAL ASC";
        ResultSet rsCurso = oConn.runQuery(strSqlCurso, true);
        while (rsCurso.next()) {
            strNombre = rsCurso.getString("CC_NOMBRE_CURSO");
            strClaveCurso = rsCurso.getString("CC_CLAVES");
            strFechaIni = rsCurso.getString("CC_FECHA_INICIAL");
            strHhrsInicio = rsCurso.getString("CC_HR_EVENTO_INI");
            strSede = getSede(oConn, rsCurso.getString("CC_SEDE_ID"));
            strAlias = getAlias(oConn, rsCurso.getString("CC_SEDE_ID"));
            strIdWEb = rsCurso.getString("CC_CURSO_ID");
            strDuracion = rsCurso.getString("Duracion");
//            strLigaCurso = fec.FormateaDDMMAAAA(strFechaIni, "") + strIdWEb + intTipoCurso;
            if (rsCurso.getString("CC_ID_WEB").equals("")) {
                strLigaCurso = "http://cofide.org/producto/";
            } else {
                strLigaCurso = rsCurso.getString("CC_ID_WEB");
            }
            strCuerpo += "<tr>"
                    + "<td width=\"490\" style=\"background-color: #F5FAE5; border: 1px solid #99cc00; padding: 10px; -webkit-border-radius: 2px; border-radius: 2px;\">"
                    + "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"
                    + "<tr>"
                    + "<td style=\"font-family: Helvetica, Tahoma, sans-serif; font-size: 22px; font-weight: 100; text-transform: none; border-right: 1px solid #99cc00; padding-right: 10px;\">" + strNombre + "</td> <!-- NOMBRE DEL CURSO -->"
                    + "<td width=\"200\" style=\"padding-left: 10px;\" valign=\"top\">"
                    + "<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"2\" align=\"center\">"
                    + "<tr>"
                    + "<td colspan=\"2\" style=\"font-family: Helvetica, Tahoma, sans-serif; font-size: 16px; font-weight: 700; text-transform: none; padding-bottom: 10px;\">" + getFecha(oConn, strIdWEb) + "</td> <!-- DIA. NDIA MES. AÑO -->"
                    + "</tr>"
                    + "<tr>"
                    + "<td style=\"font-family: Helvetica, Tahoma, sans-serif; font-size: 12px; font-weight: 700; text-transform: none;\">Inicio:</td>"
                    + "<td style=\"font-family: Helvetica, Tahoma, sans-serif; font-size: 12px; font-weight: 100; text-transform: none;\">" + strHhrsInicio + " HRS.</td> <!-- HORA DE INICIO -->"
                    + "</tr>"
                    + "<tr>"
                    + "<td style=\"font-family: Helvetica, Tahoma, sans-serif; font-size: 12px; font-weight: 700; text-transform: none;\">Duraci&oacute;n:</td>"
                    + "<td style=\"font-family: Helvetica, Tahoma, sans-serif; font-size: 12px; font-weight: 100; text-transform: none;\">" + strDuracion + " HRS.</td> <!-- DURACION -->"
                    + "</tr>"
                    + "</table>"
                    + "</td>"
                    + "</tr>"
                    + "</table>"
                    + "</td>"
                    + "<td width=\"10\">&nbsp;</td>"
                    + "<td width=\"100\" style=\"background-color: #99cc00; font-family: Helvetica, Tahoma, sans-serif; font-size: 12px; color: #fff; text-decoration: none; font-weight: 700; -webkit-border-radius: 2px; border-radius: 2px; padding: 5px;\" align=\"center\" valign=\"middle\"><a href=\"" + strLigaCurso + "\" target=\"_blank\" style=\"color: #fff; text-decoration: none;\">[+] INFO.<br />TEMARIO</a></td> <!-- LIGA DIRECTA AL CURSO DESC-->"
                    + "</tr>";
        }
        rsCurso.close();

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

        String[] lstMail = getMailTemplate(oConn, "SEDE");
        String strMsgMail = lstMail[1];
        //  Si estan llenos todos los datos mandamos el mail

        if (!strServer.equals(
                "") && !strCorreo.equals("") && !strPass.equals("")) {
            //armamos el mail
//            Mail mail = new Mail();.
            mail.setBolDepuracion(false);
            //Activamos envio de acuse de recibo
            mail.setBolAcuseRecibo(false);
            //Obtenemos los usuarios a los que mandaremos el mail
            String strLstMail = "";
            //Validamos si el mail del cliente es valido
            if (mail.isEmail(strCRMEmail)) {
                strLstMail = strCRMEmail;
            }
            //Mandamos mail si hay usuarios
            if (!strLstMail.equals("")) {
                //System.out.println("cuerpo: " + strMsgMail);
                //cuerpo
                strMsgMail = strMsgMail.replace("%ALIAS%", getHtml(strAlias));
                strMsgMail = strMsgMail.replace("%SEDE%", getHtml(strSede));
                strMsgMail = strMsgMail.replace("%CUERPO%", getHtml(strCuerpo));
                strMsgMail = strMsgMail.replace("%AGENTE%", getHtml(strAgente));
                strMsgMail = strMsgMail.replace("%EXTENSION%", strNumero);
                strMsgMail = strMsgMail.replace("%CORREO%", strCorreo);
                if (!strNombreContacto.equals("")) {
                    strMsgMail = strMsgMail.replace("%NOMBRE_CONTACTO%", "APRECIABLE: " + strNombreContacto);
                } else {
                    strMsgMail = strMsgMail.replace("%NOMBRE_CONTACTO%", "");
                }
                //Establecemos parametros
                mail.setUsuario(strCorreo);
                mail.setContrasenia(strPass);
                mail.setHost(strServer);
                mail.setPuerto(strPort);
                mail.setAsunto(lstMail[0].replace("%ASUNTO%", strAsunto));
                mail.setDestino(strLstMail);
                mail.setMensaje(strMsgMail);
                mail.setBolDepuracion(false);
                mail.setBolMantenerConexion(false); // cierra conexión de correo
                if (strTLS.equals("1")) {
                    mail.setBolUsaTls(true);
                }
                if (strSTLS.equals("1")) {
                    mail.setBolUsaStartTls(true);
                }
                boolean bol = mail.sendMail();
                if (!bol) {
                    strResp = "Fallo el envio del Mail.";
                }
//                System.out.println("###############################cuarpo: \n" + strMsgMail);
            }
        }
        return strResp;
    }

    /**
     *
     * @param strTxto
     * @return
     */
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
        strTxto = strTxto.replace("'", "&#39;");
        strTxto = strTxto.replace("´", "&#39;");
        strTxto = strTxto.replace("`", "&#96;");
        return strTxto;
    }

    /**
     *
     * @param oConn
     * @param strNom
     * @return
     *
     */
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

    /**
     *
     * @param oConn
     * @param strIdCurso
     * @return
     * @throws ParseException convierte el formato de la fecha, acorde cofide
     */
//    public String getFecha(String strFecha) throws ParseException {
//        String strFechaFormat = "";
//        SimpleDateFormat conver = new SimpleDateFormat("yyyyMMdd");
//        SimpleDateFormat format = new SimpleDateFormat("E. dd MMM,YYYY", new Locale("es", "MX")); //Lun. 20 Jun. 2016
//        Date date = conver.parse(strFecha);
//        strFechaFormat = format.format(date);
//        return strFechaFormat;
//    }
    public String getFecha(Conexion oConn, String strIdCurso) throws ParseException {
        String strFecha = "";
        String strFech2 = "";
        String strFech3 = "";
        String strAlias = "";
        String strSql = "select CC_FECHA_INICIAL, CC_FECHA_INICIAL2, CC_FECHA_INICIAL3, CC_ALIAS_FEC  from cofide_cursos where cc_curso_id = " + strIdCurso;
        try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                strFecha = rs.getString("CC_FECHA_INICIAL");
                strFech2 = rs.getString("CC_FECHA_INICIAL2");
                strFech3 = rs.getString("CC_FECHA_INICIAL3");
                strAlias = rs.getString("CC_ALIAS_FEC");
            }
            rs.close();
        } catch (SQLException ex) {
            System.out.println("Error al obtener las fechas del curso: " + ex.getMessage());
        }
        String strFechaFormat = "";
        SimpleDateFormat conver = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat format = new SimpleDateFormat("E. dd MMM,YYYY", new Locale("es", "MX")); //Lun. 20 Jun. 2016
        SimpleDateFormat format2 = new SimpleDateFormat(" MMM,YYYY", new Locale("es", "MX")); // Jun. 2016
        SimpleDateFormat format3 = new SimpleDateFormat("dd MMM", new Locale("es", "MX")); //20 Jun.
        SimpleDateFormat format4 = new SimpleDateFormat("dd MMM, YYYY", new Locale("es", "MX")); //20 Jun. 2016
        if (strFech2.equals("")) { //unica fecha                
            Date date = conver.parse(strFecha);
            strFechaFormat = format.format(date);
        } else {
            if (strFech3.equals("")) { //fechas juntas, seguidas                    
                if (strFecha.substring(0, 6).equals(strFech2.substring(0, 6))) {
                    Date date = conver.parse(strFecha);
                    strFechaFormat = strAlias + format2.format(date);
                } else {
                    Date date = conver.parse(strFecha);
                    Date date2 = conver.parse(strFech2);
                    strFechaFormat = format3.format(date) + " y " + format4.format(date2);
                }
            } else { //fechas con mas de dos días
                if (strFecha.substring(0, 6).equals(strFech3.substring(0, 6))) {
                    Date date = conver.parse(strFecha);
                    strFechaFormat = strAlias + format2.format(date);
                } else {
                    Date date = conver.parse(strFecha);
                    Date date2 = conver.parse(strFech3);
                    strFechaFormat = format3.format(date) + " al " + format4.format(date2);
                }
            }
        }
        return strFechaFormat;
    }

    /**
     * Obtiene el mes
     *
     * @param strFecha
     * @return el nombre del mes
     * @throws ParseException
     */
    public String getFechaMens(String strFecha) throws ParseException {
        String strFechaFormat = "";
        SimpleDateFormat conver = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat format = new SimpleDateFormat("MMMM", new Locale("es", "MX")); //Lun. 20 Jun. 2016
        Date date = conver.parse(strFecha);
        strFechaFormat = format.format(date);
        return strFechaFormat;
    }

    /**
     * obtiene el nombre del hotel
     *
     * @param oConn
     * @param strIdSede
     * @return
     */
    public String getSede(Conexion oConn, String strIdSede) {
        String strNombreSede = "";
        String strSql = "select CSH_ALIAS, CSH_SEDE from cofide_sede_hotel where CSH_ID = " + strIdSede;
        try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                strNombreSede = rs.getString("CSH_SEDE");
            }
            rs.close();
        } catch (SQLException ex) {
            System.out.println("Error al obtener la sede: " + ex);
        }
        return strNombreSede;
    }

    /**
     * obtiene el alias de la sede y el nombre del hotel
     *
     * @param oConn
     * @param strIdSede
     * @return
     */
    public String getAlias(Conexion oConn, String strIdSede) {
        String strAliasSede = "";
        String strSql = "select CSH_ALIAS from cofide_sede_hotel where CSH_ID = " + strIdSede;
        try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                strAliasSede = rs.getString("CSH_ALIAS");
            }
            rs.close();
        } catch (SQLException ex) {
            System.out.println("Error al obtener la sede: " + ex);
        }
        return strAliasSede;
    }

    /**
     * corrige el formato del precio
     *
     * @param strPrecio
     * @return devuelve el formato correcto del precio 00.00
     */
    public String setPrecioOk(String strPrecio) {
        DecimalFormat formatNumber = new DecimalFormat("###,###.##");
        strPrecio = formatNumber.format(Double.parseDouble(strPrecio));
        String strPrecioOk = "";
        String strPrecioReal = "";
        String strPrecioRealOK = "";
        if (!strPrecio.equals("0")) {
            int intlargo = strPrecio.length();
            if (intlargo < 3) {
                strPrecioOk = strPrecio;
            } else {
                strPrecioReal = strPrecio.substring(intlargo - 2, intlargo);
                strPrecioRealOK = strPrecio.substring(intlargo - 2, intlargo);
                if (strPrecioReal.contains(".")) {
                    if (strPrecioReal.equals(".0")) {
                        strPrecioReal = strPrecioReal.replace(".0", ".00");
                    } else {
                        if (strPrecioReal.substring(strPrecioReal.length() - 1, strPrecioReal.length()).equals(".")) {
                            strPrecioReal += "00";
                        } else {
                            strPrecioReal += "0";
                        }
                    }
                } else {
                    if (strPrecio.substring(intlargo - 3, intlargo).contains(".")) {
                    } else {
                        strPrecio += ".00";
                    }
                }
                strPrecioOk = strPrecio.replace(strPrecioRealOK, strPrecioReal);
            }
        } else {
            strPrecioOk = "0.00";
        }

        return strPrecioOk;
    }

    /*
    * Obtiene el nombre del contacto por correo 
    * Regresa el Nombre y apellidos concatenados
     */
    public String getStrNombreContactoCofide(Conexion oConn, String strCorreo) {
        String strSql = "";
        String strNombreCont = "";
        ResultSet rs = null;
        strSql = "select *,concat(CCO_NOMBRE,' ',CCO_APPATERNO,' ',CCO_APMATERNO) as NOMBRE_CONTACTO from cofide_contactos where CCO_CORREO like '%" + strCorreo + "%'";
        try {
            rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                if (rs.getString("CCO_NOMBRE") != null || !rs.getString("CCO_NOMBRE").trim().equals("")) {
                    strNombreCont = util.Sustituye(rs.getString("NOMBRE_CONTACTO"));
                }
            }
        } catch (SQLException ex) {
            log.error("ERROR [getStrNombreContactoCofide]: " + ex.getLocalizedMessage());
        }
        strNombreCont = getHtml(strNombreCont);
        return strNombreCont;
    }//Fin getStrNombreContactoCofide

    /*
    * Obtiene el ID del contacto por correo 
    * Regresa el ID
     */
    public int getIdContactoCofide(Conexion oConn, String strCorreo) {
        String strSql = "";
        int intContactoId = 0;
        ResultSet rs = null;
        strSql = "select * from cofide_contactos where CCO_CORREO like '%" + strCorreo + "%'";
        try {
            rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                intContactoId = rs.getInt("CONTACTO_ID");
            }
        } catch (SQLException ex) {
            log.error("ERROR [getStrNombreContactoCofide]: " + ex.getLocalizedMessage());
        }

        return intContactoId;
    }//Fin getIdContactoCofide

    /**
     * registro de correos enviados
     *
     * @param intIdMasivo id maestro del masivo
     * @param intIdCte id del cliente
     * @param strCorreo correo del cliente
     * @param oConn
     */
    public void regMail(int intIdMasivo, int intIdCte, String strCorreo, Conexion oConn) {

        String strSql = "INSERT INTO cofide_mailing (MAIL_ID_MASIVO, MAIL_CT_ID, MAIL_CORREO, MAIL_FECHA_INI, MAIL_HORA_INI) VALUES "
                + "('" + intIdMasivo + "', '" + intIdCte + "', '" + strCorreo + "', '" + fec.getFechaActual() + "', '" + fec.getHoraActual() + "')";
        oConn.runQueryLMD(strSql);
        if (oConn.isBolEsError()) {
            System.out.println("Error al registrar mail: [" + oConn.getStrMsgError() + "]");
        }
    }

    /**
     *
     * @param oConn
     * @param intIDEjecutivo
     * @param strCRMEmail
     * @param strAsunto
     * @param intPreview 0 = manda mail, 1 = preview
     * @param strPlantilla
     * @param intIdPlantilla
     * @return
     * @throws java.sql.SQLException
     * @throws java.text.ParseException mail masivo de 3 cursos
     */
    public String CofidePLealtadCursos(Conexion oConn, int intIDEjecutivo, String strCRMEmail, String strAsunto,
            int intPreview, String strPlantilla, int intIdPlantilla) throws SQLException, ParseException {
        //Objetenemso los datos del curso
        String strNombreContacto = getStrNombreContactoCofide(oConn, strCRMEmail);

        String strNombre = ""; //NOMBRE DEL CURSO
        String strFechaIni = ""; //fecha inicial ddbb
        String strFecha = ""; //fecha inicial formateada
        String strNomMes = ""; //mes nombre
        String strHhrsInicio = ""; //hora de inicio
        String strDuracion = ""; //duracion del curso
        String strSede = ""; //precio
        String strMes = ""; //mes
        int intTipoCurso = 1; //tipo del curso
        String strIdWEb = ""; //id web con el que se liga con la pagina web
        String strEsSeminario = ""; //es seminario ?
        String strEsDiplomado = ""; //es diplomado ? 
        String strTipo = "PRESENCIAL"; //es presencial 
        String strLigaCurso = ""; //liga del curso
        String strCuerpo = "";
        int intCursos = 0;
        int intContactoId = 0;
        int intPtos = 0;
        int intPtosRestante = 0;
        String strSQL = "select CRCD_ID_CURSO from crm_correos_deta where CRC_ID = " + intIdPlantilla;
        ResultSet rs = oConn.runQuery(strSQL, true);
        while (rs.next()) {
            intCursos = rs.getInt("CRCD_ID_CURSO");
        }
        rs.close();

//        strCuerpo = strGetTemplateCuetpoPLealtad(oConn, intCursos);
//        System.out.println("correo: " + strCRMEmail);
        intContactoId = getIdContactoCofide(oConn, strCRMEmail);
//        System.out.println("contacto id " + intContactoId);
        intPtos = intGetPtosContacto(oConn, intContactoId);
//        System.out.println(" puntos: " + intPtos);
        String strTablaHistorial = strGetHistorialContactoVta(oConn, "" + intContactoId);
//        System.out.println(" tabla: \n" + strTablaHistorial);

        intPtosRestante = 1000 - intPtos;
        String strPtoRestante = "";

        if (intPtosRestante > 999) {
            strPtoRestante = "Puedes llevarte un curso";
        } else {
            strPtoRestante = "y estás a solo "
                    + "<strong style=\"font-size: 26px; color: #97c325\">" + intPtosRestante + "</strong> de llevarte un curso";
        }

        strPtoRestante = getHtml(strPtoRestante);
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
//        String strNomTemplate = strPlantilla; //obtenemos el template del mail para le supervisor
        String[] lstMail = getMailTemplate(oConn, strPlantilla);
        String strMsgMail = lstMail[1];
        //  Si estan llenos todos los datos mandamos el mail
        if (!strServer.equals("") && !strCorreo.equals("") && !strPass.equals("")) {
            //armamos el mail
//            Mail mail = new Mail();
            mail.setBolDepuracion(false);
//            //Activamos envio de acuse de recibo
            mail.setBolAcuseRecibo(false);
            //Obtenemos los usuarios a los que mandaremos el mail
            String strLstMail = "";
            //Validamos si el mail del cliente es valido
            if (mail.isEmail(strCRMEmail)) {
                strLstMail = strCRMEmail;
            }
            //Mandamos mail si hay usuarios
            if (!strLstMail.equals("")) {
                //cuerpo
                strMsgMail = strMsgMail.replace("%TOTAL_PTOS%", "" + intPtos);
                strMsgMail = strMsgMail.replace("%PTOS_RESTANTE%", strPtoRestante);
                strMsgMail = strMsgMail.replace("%TablaHistorial%", strTablaHistorial);
//                strMsgMail = strMsgMail.replace("%CUERPO%", getHtml(strCuerpo));
                strMsgMail = strMsgMail.replace("%AGENTE%", getHtml(strAgente));
                strMsgMail = strMsgMail.replace("%EXTENSION%", strNumero);
                strMsgMail = strMsgMail.replace("%CORREO%", strCorreo);
                strMsgMail = strMsgMail.replace("%CORREO_DESTINO%", getStrCorreoDest());
                strMsgMail = strMsgMail.replace("%ID_MASIVO%", String.valueOf(getIntIdMasivo()));
                strMsgMail = strMsgMail.replace("%ID_CTE%", String.valueOf(getIntIdCte()));

                if (!strNombreContacto.equals("")) {
                    strMsgMail = strMsgMail.replace("%NOMBRE_CONTACTO%", "APRECIABLE: " + strNombreContacto);
                } else {
                    strMsgMail = strMsgMail.replace("%NOMBRE_CONTACTO%", "");
                }
                //manda el mail = 0, o muestra preview = 1
                if (intPreview == 0) {
                    //Establecemos parametros
                    mail.setUsuario(strCorreo);
                    mail.setContrasenia(strPass);
                    mail.setHost(strServer);
                    mail.setPuerto(strPort);
                    mail.setAsunto(lstMail[0].replace("%ASUNTO%", strAsunto));
                    mail.setDestino(strLstMail);
                    mail.setMensaje(strMsgMail);
                    mail.setBolDepuracion(false);
//                    System.out.println(strMsgMail);
                    mail.setBolMantenerConexion(false); // cierra conexión de correo
                    //Adjuntamos XML y PDF
                    if (strTLS.equals("1")) {
                        mail.setBolUsaTls(true);
                    }
                    if (strSTLS.equals("1")) {
                        mail.setBolUsaStartTls(true);
                    }
                    System.out.println("cuerpo: \n\n\n\n\n" + strMsgMail);
                    boolean bol = mail.sendMail();
                    if (!bol) {
                        strResp = "Fallo el envio del Mail.";
                    } else {
                        regMail(getIntIdMasivo(), getIntIdCte(), getStrCorreoDest(), oConn);
                    }
                } else {
                    strResp = strMsgMail; // manda el cuerpo del mail                   
                }
            }
        }

        return strResp;
    }

    public String strGetTemplateCuetpoPLealtad(Conexion oConn, int intPLEaltad) {
        String strSQL = "select CMT_CONTENIDO from cofide_mailtemplate where CMT_ABRV = 'PLEALTAD_" + intPLEaltad + "'";
        String strMailCuerpo = "";
        try {
            ResultSet rs = oConn.runQuery(strSQL, true);
            while (rs.next()) {
                strMailCuerpo = rs.getString("CMT_CONTENIDO");
            }
            rs.close();
        } catch (SQLException ex) {
            log.error("strGetTemplateCuetpoPLealtad ERROR [" + ex.getLocalizedMessage() + "]");
        }
        return strMailCuerpo;
    }

    /**
     * plantilla
     *
     * @param oConn
     * @param intIDEjecutivo
     * @param strCRMEmail
     * @param strAsunto
     * @param intPreview 0 = manda mail, 1 = preview
     * @param strPlantilla
     * @param intIdPlantilla
     * @param strCT_ID id del cliente
     * @return
     * @throws java.sql.SQLException
     * @throws java.text.ParseException mail masivo de 3 cursos
     */
    public String CofideHTML(Conexion oConn, int intIDEjecutivo, String strCRMEmail, String strAsunto,
            int intPreview, String strPlantilla, int intIdPlantilla) throws SQLException, ParseException {

//        String strLigaMasivo = "http://www.cofide.org/correos_/cofide_mail.php?correo=" + getStrCorreoDest() + "&id_masivo=" + getIntIdMasivo() + "&id_cte=" + getIntIdCte();
//        String strLigaMasivo = "http://201.161.14.206/correos_/cofide_mail.php?correo=" + getStrCorreoDest() + "&id_masivo=" + getIntIdMasivo() + "&id_cte=" + getIntIdCte();
        String strLigaMasivo = "http://201.161.14.206:9001/cofide/cofide_mailing_.jsp?opc=2&correo=" + getStrCorreoDest() + "&idm=" + getIntIdMasivo() + "&idcte=" + getIntIdCte();

//        String strLigaMasivo = "<td width=\"150\" rowspan=\"2\" align=\"left\">"
//                + "<a href=\"http://www.cofide.org\" target=\"_blank\">"
//                + "<img src=\"http://www.cofide.org/correos_/cofide_mail.php?correo=" + getStrCorreoDest() + "&id_masivo=" + getIntIdMasivo() + "&id_cte=" + getIntIdCte() + "\">"
//                + "</a>"
//                + "</td>";
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
            strCorreo = rsAgente.getString("SMTP_US");
            strPass = rsAgente.getString("SMTP_PASS");
            strServer = rsAgente.getString("SMTP");
            strPort = rsAgente.getString("PORT");
            strTLS = rsAgente.getString("SMTP_USATLS");
            strSTLS = rsAgente.getString("SMTP_USASTLS");
        }
        rsAgente.close();
        //obtenemos el contato del agente
        String strResp = "OK";
        String[] lstMail = getMailTemplate(oConn, strPlantilla);
        String strMsgMail = lstMail[1];
        //  Si estan llenos todos los datos mandamos el mail
        if (!strServer.equals("") && !strCorreo.equals("") && !strPass.equals("")) {
            //armamos el mail
            mail.setBolDepuracion(false);
//            //Activamos envio de acuse de recibo
            mail.setBolAcuseRecibo(false);
            //Obtenemos los usuarios a los que mandaremos el mail
            String strLstMail = "";
            //Validamos si el mail del cliente es valido
            if (mail.isEmail(strCRMEmail)) {
                strLstMail = strCRMEmail;
            }
            //Mandamos mail si hay usuarios
            if (!strLstMail.equals("")) {
                //cuerpo

//                strMsgMail = strMsgMail.replace("%CORREO_DESTINO%", getStrCorreoDest());
//                strMsgMail = strMsgMail.replace("%ID_MASIVO%", String.valueOf(getIntIdMasivo()));
//                strMsgMail = strMsgMail.replace("%ID_CTE%", String.valueOf(getIntIdCte()));
                strMsgMail = strMsgMail.replace("%LIGA_MASIVO%", strLigaMasivo);
                System.out.println("liga de masivo: " + strLigaMasivo);

                //manda el mail = 0, o muestra preview = 1
                System.out.println("##############cuerpo: " + strMsgMail);
                if (intPreview == 0) {
                    //Establecemos parametros
                    mail.setUsuario(strCorreo);
                    mail.setContrasenia(strPass);
                    mail.setHost(strServer);
                    mail.setPuerto(strPort);
                    mail.setAsunto(lstMail[0].replace("%ASUNTO%", strAsunto));
                    mail.setDestino(strLstMail);
                    mail.setMensaje(strMsgMail);
                    mail.setBolDepuracion(false);
                    mail.setBolMantenerConexion(false); // cierra conexión de correo
                    //Adjuntamos XML y PDF
                    if (strTLS.equals("1")) {
                        mail.setBolUsaTls(true);
                    }
                    if (strSTLS.equals("1")) {
                        mail.setBolUsaStartTls(true);
                    }
                    boolean bol = mail.sendMail();
                    if (!bol) {
                        strResp = "Fallo el envio del Mail.";
                    } else {
                        regMail(getIntIdMasivo(), getIntIdCte(), getStrCorreoDest(), oConn);
                    }
                } else {
                    strResp = strMsgMail; // manda el cuerpo del mail                   
                }
            }
        }
        return strResp;
    }

    /**
     * plantilla
     *
     * @param oConn
     * @param intIDEjecutivo
     * @param strCRMEmail
     * @param strAsunto
     * @param intPreview 0 = manda mail, 1 = preview
     * @param strPlantilla
     * @param intIdPlantilla
     * @param strCT_ID id del cliente
     * @return
     * @throws java.sql.SQLException
     * @throws java.text.ParseException mail masivo de 3 cursos
     */
    public String COFIDEnet(Conexion oConn, int intIDEjecutivo, String strCRMEmail, String strAsunto,
            int intPreview, String strPlantilla, int intIdPlantilla) throws SQLException, ParseException {

//        String strLigaMasivo = "http://www.cofide.org/correos_/cofide_mail.php?correo=" + getStrCorreoDest() + "&id_masivo=" + getIntIdMasivo() + "&id_cte=" + getIntIdCte();
//        String strLigaMasivo = "http://www.cofide.org/correos_/cofide_mail_net.php?correo=" + getStrCorreoDest() + "&id_masivo=" + getIntIdMasivo() + "&id_cte=" + getIntIdCte();
//        String strLigaMasivo = "https://201.161.14.206/correos_/cofide_mail_net.php?correo=" + getStrCorreoDest() + "&id_masivo=" + getIntIdMasivo() + "&id_cte=" + getIntIdCte();
        String strLigaMasivo = "http://201.161.14.206:9001/cofide/cofide_mailing_.jsp?opc=2&correo=" + getStrCorreoDest() + "&idm=" + getIntIdMasivo() + "&idcte=" + getIntIdCte();

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
            strCorreo = rsAgente.getString("SMTP_US");
            strPass = rsAgente.getString("SMTP_PASS");
            strServer = rsAgente.getString("SMTP");
            strPort = rsAgente.getString("PORT");
            strTLS = rsAgente.getString("SMTP_USATLS");
            strSTLS = rsAgente.getString("SMTP_USASTLS");
            strExt = rsAgente.getString("NUM_EXT");

        }
        rsAgente.close();
        //obtenemos el contato del agente
        String strResp = "OK";
        String[] lstMail = getMailTemplate(oConn, strPlantilla);
        String strMsgMail = lstMail[1];
        //  Si estan llenos todos los datos mandamos el mail
        if (!strServer.equals("") && !strCorreo.equals("") && !strPass.equals("")) {
            //armamos el mail
            mail.setBolDepuracion(false);
//            //Activamos envio de acuse de recibo
            mail.setBolAcuseRecibo(false);
            //Obtenemos los usuarios a los que mandaremos el mail
            String strLstMail = "";
            //Validamos si el mail del cliente es valido
            if (mail.isEmail(strCRMEmail)) {
                strLstMail = strCRMEmail;
            }
            //Mandamos mail si hay usuarios
            if (!strLstMail.equals("")) {
                //cuerpo

                strMsgMail = strMsgMail.replace("%LIGA_MASIVO%", strLigaMasivo);
                strMsgMail = strMsgMail.replace("%AGENTE%", strAgente);
                strMsgMail = strMsgMail.replace("%CORREO%", strCorreo);
                strMsgMail = strMsgMail.replace("%EXTENSION%", strExt);
                System.out.println("liga de masivo: " + strLigaMasivo);

                //manda el mail = 0, o muestra preview = 1
//                System.out.println("##############cuerpo: " + strMsgMail);
                if (intPreview == 0) {
                    //Establecemos parametros
                    mail.setUsuario(strCorreo);
                    mail.setContrasenia(strPass);
                    mail.setHost(strServer);
                    mail.setPuerto(strPort);
                    mail.setAsunto(lstMail[0].replace("%ASUNTO%", strAsunto));
                    mail.setDestino(strLstMail);
                    mail.setMensaje(strMsgMail);
                    mail.setBolDepuracion(false);
                    mail.setBolMantenerConexion(false); // cierra conexión de correo
                    //Adjuntamos XML y PDF
                    if (strTLS.equals("1")) {
                        mail.setBolUsaTls(true);
                    }
                    if (strSTLS.equals("1")) {
                        mail.setBolUsaStartTls(true);
                    }
                    boolean bol = mail.sendMail();
                    if (!bol) {
                        strResp = "Fallo el envio del Mail.";
                    } else {
                        regMail(getIntIdMasivo(), getIntIdCte(), getStrCorreoDest(), oConn);
                    }
                } else {
                    strResp = strMsgMail; // manda el cuerpo del mail        
//                    System.out.println(strResp);
                }
            }
        }
        return strResp;
    }

    /**
     * OBTENEMOS EL CONTENIDO DEL ARCHIVO
     *
     * @param stPathUsado
     * @return
     */
    public String getContainTemplate(String stPathUsado) {
        String strContenido = "";
        System.out.println("############### rutijirijilla: \n" + stPathUsado + "\n##################################");
        if (!stPathUsado.equals("")) {
            try {
                // Abrimos el archivo
//                FileInputStream fstream = new FileInputStream("C:/Users/Desarrollo_COFIDE/Documents/NetBeansProjects/COFIDE/build/web/document/Plantilla/Plantilla_cuacu.html");
                FileInputStream fstream = new FileInputStream(stPathUsado);
//                FileInputStream fstream = new FileInputStream("C:\\Users\\Desarrollo_COFIDE\\Desktop\\HTML\\archivo.txt");
                // Creamos el objeto de entrada                
                DataInputStream entrada = new DataInputStream(fstream);
                // Creamos el Buffer de Lectura                
                BufferedReader buffer = new BufferedReader(new InputStreamReader(entrada));
                String strLinea;
                // Leer el archivo linea por linea
                while ((strLinea = buffer.readLine()) != null) {
                    // Imprimimos la línea por pantalla
                    strContenido += getHtml(strLinea) + "\n";
                }
                // Cerramos el archivo
                entrada.close();
            } catch (IOException e) { //Catch de excepciones
                System.err.println("Ocurrio un error: " + e.getMessage());
            }
        } else {
            strContenido = "No se encontro el contenido...\nFavor de subirlo nuevamente.";
        }
        System.out.println("##plantilla desde la funcion:;\n" + strContenido);
        return strContenido;
    }

    /**
     * ENVIO DE CORREO CON LOS RESULTADOS DEL SORTEO
     *
     * @param oConn
     * @param strCRMEmail
     * @param strAsunto
     * @return
     * @throws SQLException
     * @throws ParseException
     */
    public String CofideIntercambio(Conexion oConn, String strCRMEmail, String strAsunto, String strCuerpo) throws SQLException, ParseException {

        //obtenemos el contato del agente        
        String strCorreo = "info@cofide.org";
        String strPass = "123.S0p0rt3";
        String strServer = "192.168.2.242";
        String strPort = "465";
        String strTLS = "1";
        String strSTLS = "0";

        String strResp = "OK";
        //OBTENEMOS LA PLANTILLA
        String[] lstMail = getMailTemplate(oConn, "SORTEO");
        String strMsgMail = lstMail[1];
        strMsgMail = strMsgMail.replace("%CUERPO%", strCuerpo);
        //  Si estan llenos todos los datos mandamos el mail
        if (!strServer.equals("") && !strCorreo.equals("") && !strPass.equals("")) {

            //Activamos envio de acuse de recibo
            mail.setBolAcuseRecibo(false);
            //Obtenemos los usuarios a los que mandaremos el mail
            String strLstMail = "";
            //Validamos si el mail del cliente es valido
            if (mail.isEmail(strCRMEmail)) {
                strLstMail = strCRMEmail;
                //Mandamos mail si hay usuarios
                if (!strLstMail.equals("")) {

//                    System.out.println("######################################## correo destino: \n\n" + strCRMEmail);
                    //Establecemos parametros
                    mail.setUsuario(strCorreo);
                    mail.setContrasenia(strPass);
                    mail.setHost(strServer);
                    mail.setPuerto(strPort);
                    mail.setAsunto(lstMail[0].replace("%ASUNTO%", strAsunto));
                    mail.setDestino(strLstMail);
                    mail.setMensaje(strMsgMail);
                    mail.setBolDepuracion(false);
                    mail.setBolMantenerConexion(false); // cierra conexión de correo

//                    System.out.println("######################################## cuerpo: \n\n" + strMsgMail);
                    if (strTLS.equals("1")) {
                        mail.setBolUsaTls(true);
                    }
                    if (strSTLS.equals("1")) {
                        mail.setBolUsaStartTls(true);
                    }
                    boolean bol = mail.sendMail();
                    if (!bol) {
                        strResp = "Fallo el envio del Mail.";
                    }
                }
            }
        }
        return strResp;
    }//Fin CofideIntercambio

    public int intGetPtosContacto(Conexion oConn, int intContactoId) {
        int intTotalPto = 0;
        String strSql = "SELECT * from cofide_puntos_contacto where CPC_ACTIVO = 1 and CONTACTO_ID = " + intContactoId;
//        String strSql = "SELECT SUM(CPC_PUNTOS) as SUMA from cofide_puntos_contacto where CPC_ACTIVO = 1 and CONTACTO_ID = " + intContactoId;
        try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
//                intTotalPto = rs.getInt("SUMA");
                intTotalPto = intTotalPto + 200;
            }
            rs.close();
        } catch (SQLException ex) {
            System.out.println("[intGetPtosContacto]: " + ex.getLocalizedMessage());
        }
        return intTotalPto;
    }//Fin intGetPtosContacto

    /*
    Regresa un String con el historial de ventas de un cliente
     */
    public String strGetHistorialContactoVta(Conexion oConn, String strContactoId) {
        StringBuilder strTable = new StringBuilder();

        String strSql = "select CONTACTO_ID, CP_ID_CURSO, CP_NOMBRE, CP_FAC_ID, CP_TKT_ID,fac_anulada, cancel "
                + "from cofide_participantes LEFT JOIN view_ventasglobales "
                + "on IF( CP_TKT_ID = 0 , view_ventasglobales.FAC_ID = cofide_participantes.CP_FAC_ID , view_ventasglobales.FAC_ID = cofide_participantes.CP_TKT_ID) "
                + "where view_ventasglobales.FAC_ANULADA = 0 "
                + "and view_ventasglobales.CANCEL = 0 "
                + "and CP_TIPO_CURSO = 1 "
                + "and(CP_TKT_ID <> 0 or CP_FAC_ID <> 0) AND CONTACTO_ID = " + strContactoId;

        String strFacId = "";

        //Id de la Venta
        try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                if (rs.getString("CP_FAC_ID").equals("0")) {
                    strFacId += rs.getString("CP_TKT_ID");
                } else {
                    strFacId += rs.getString("CP_FAC_ID");
                }
                if (!strFacId.equals("")) {
                    strFacId = strFacId + ",";
                }
            }
            rs.close();
        } catch (SQLException ex) {
            System.out.println("ERROR[strGetHistorialContactoVta] : " + ex.getLocalizedMessage());
        }

        if (strFacId.endsWith(",")) {
            strFacId = strFacId.substring(0, strFacId.length() - 1);
        }

        strTable.append("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" style=\"padding-bottom: 10px;\">\n");
        strTable.append("<tr>\n");
        strTable.append("<td width=\"20%\" style=\" font-size:14px; background-color: #99cc00; color:#ffffff; border: 1px solid #99cc00; padding: 10px; -webkit-border-radius: 2px; border-radius: 2px;\">\n");
        strTable.append("Fecha del Curso\n");
        strTable.append("</td>\n");
        strTable.append("<td width=\"80%\" style=\" font-size:14px; background-color: #99cc00; color:#ffffff; border: 1px solid #99cc00; padding: 10px; -webkit-border-radius: 2px; border-radius: 2px;\">\n");
        strTable.append("Descripci&oacute;n\n");
        strTable.append("</td>\n");
        strTable.append("</tr>\n");

        strSql = "";

        if (!strFacId.equals("")) {
            try {
                strSql = "select "
                        + "CC_FECHA_INICIAL, "
                        + "CC_NOMBRE_CURSO "
                        + "from view_ventasglobalesdeta, cofide_cursos "
                        + "where PR_ID = CC_CURSO_ID "
                        + "AND FAC_ID in (" + strFacId + ")";

                ResultSet rsDeta = oConn.runQuery(strSql, true);

                while (rsDeta.next()) {

                    strTable.append("<tr style=\"background-color: #F5FAE5; border: 1px solid #99cc00; padding: 10px; -webkit-border-radius: 2px; border-radius: 2px;\">\n");
                    strTable.append("<td>\n");
                    strTable.append("<div>\n");
                    strTable.append("<table>\n");
                    strTable.append("<tr>\n");
                    strTable.append("<td width=\"20%\" style=\"font-family: Helvetica, Tahoma, sans-serif; font-size:16px; font-weight: 100; text-transform: none; border-right: 1px solid #99cc00; padding-right: 10px; padding: 10px;\">\n");
                    strTable.append(fec.FormateaDDMMAAAA(rsDeta.getString("CC_FECHA_INICIAL"), "/"));
                    strTable.append("</td>\n");
                    strTable.append("</tr>\n");
                    strTable.append("</table>\n");
                    strTable.append("</div>\n");
                    strTable.append("</td>\n");
                    strTable.append("<td>\n");
                    strTable.append("<div>\n");
                    strTable.append("<table>\n");
                    strTable.append("<tr>\n");
                    strTable.append("<td width=\"80%\" style=\"font-family: Helvetica, Tahoma, sans-serif; font-size:16px; font-weight: 100; text-transform: none; border-right: 1px solid #99cc00; padding-right: 10px; padding: 10px;\">\n");
                    strTable.append(rsDeta.getString("CC_NOMBRE_CURSO"));
                    strTable.append("</td>\n");
                    strTable.append("</tr>\n");
                    strTable.append("</table>\n");
                    strTable.append("</div>\n");
                    strTable.append("</td>\n");
                    strTable.append("</tr>\n");

                }
                rsDeta.close();

            } catch (SQLException ex) {
                System.out.println("2.[strGetHistorialContactoVta]: " + ex.getLocalizedMessage());
            }
        }
        strTable.append("</table>\n");
        String strTableHtml = getHtml(strTable.toString());
        return strTableHtml;
    }//Fin strGetHistorialContactoVta

    public static void main(String[] args) {

        String[] ConexionURL = new String[4];
        ConexionURL[0] = "jdbc:mysql://192.168.2.246:3306/vta_cofide";//nombre de la base de datos
//        ConexionURL[0] = "jdbc:mysql://localhost:3306/COFIDE";
        ConexionURL[1] = "root";
        ConexionURL[2] = "Adm1n.01.C0f1d3";
        ConexionURL[3] = "mysql";

        Conexion oConn;
        try {
            oConn = new Conexion(ConexionURL, null);
            oConn.open();
            oConn.setBolMostrarQuerys(true);

            CRM_Envio_Template crm = new CRM_Envio_Template();
            System.out.println(": " + crm.strGetHistorialContactoVta(oConn, "1660592"));

            oConn.close();
        } catch (Exception ex) {
            System.out.println("ERROR: " + ex.getLocalizedMessage());
        }
    }
}
