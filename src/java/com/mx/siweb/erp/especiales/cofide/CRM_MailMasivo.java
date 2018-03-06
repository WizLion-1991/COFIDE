/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.especiales.cofide;

import comSIWeb.Operaciones.Conexion;
import comSIWeb.Utilerias.Fechas;
import comSIWeb.Utilerias.Mail;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

/**
 *
 * @author juliocesar
 */
public class CRM_MailMasivo {

    Fechas fec = new Fechas();
    Mail mail = new Mail();

//    public CRM_MailMasivo() {
//        
//    }
    /**
     * esto va dentro del job
     *
     * @param oConn
     * @throws SQLException
     * @throws java.text.ParseException
     */
    public void EnvioMail(Conexion oConn) throws SQLException, ParseException {
        int intEjecutivo = 0;
        String strResp = "";
        int intProcess = 0;
        int intIdPlantillaD = 0;
        String strFechaActual = fec.getFechaActual();
        CRM_Envio_Template tmp = new CRM_Envio_Template();

        String strSql = "select CT_ID, CRM_TEMPLATE, CRMD_EMAIL, CRM_CURSO, CRM_MENSUAL, CRM_ASUNTO, crm.CRM_ID, CRMD_LISTADO, CRM_PROCESADO "
                + "from crm_envio_masivo as crm, crm_envio_masivo_deta as crmd "
                + "where crm.CRM_ID = crmd.CRM_ID and CRM_FECHA <= '" + strFechaActual + "' and CRM_PROCESADO = 0 and CRMD_LISTADO = 0 "
//                + "ORDER BY crm.CRM_ID ASC, CRMD_EMAIL limit 230;";
                + "ORDER BY crm.CRM_ID ASC, CRMD_EMAIL limit 280;";

        ResultSet rs = oConn.runQuery(strSql, true);

        while (rs.next()) { //destinatarios y plantilla que enviara

            int intIdCte = rs.getInt("CT_ID");
            String strTemplate = rs.getString("CRM_TEMPLATE"); //id del evento
            String strCorreo = rs.getString("CRMD_EMAIL");
            String strCurso = rs.getString("CRM_CURSO");
            String strMes = rs.getString("CRM_MENSUAL");
            String strAsunto = rs.getString("CRM_ASUNTO");
            String strCRM_ID = rs.getString("CRM_ID");

            if (mail.isEmail(strCorreo)) {//es formato de correo

                // pone 1 en el estatus del campo, listado, 
                // para evitar que se lea mas de una vez y se dupliquen los masivos, 
                // asi en cuanto sea leido por primera vez se descarta de los pauetes
//                setListaMail(oConn, strCorreo, strCRM_ID);
                // el cliente debe de tener base de datos para poder continuar con el envío.
                boolean bolBase = false;

                String strSqlUser = "select id_usuarios from usuarios where COFIDE_CODIGO = (select ct_clave_ddbb from vta_cliente where CT_ID = " + intIdCte + " and CT_CLAVE_DDBB <> '') and IS_TMK = 1 limit 1";
                ResultSet rs1 = oConn.runQuery(strSqlUser, true);
                while (rs1.next()) {
                    intEjecutivo = rs1.getInt("id_usuarios");
                    bolBase = true;
                }
                rs1.close();

//                if (bolBase && setListaMail(oConn, strCorreo, strCRM_ID)) { // el id tiene una base de un ejecutivo.
                if (bolBase) { // el id tiene una base de un ejecutivo.

                    String strSql2 = "select DISTINCT crm_correos.CRC_ID, CRC_ID_PLANTILLA "
                            + "from crm_correos,crm_correos_deta "
                            + "where crm_correos.CRC_ID = " + strTemplate;
                    ResultSet rs2 = oConn.runQuery(strSql2, true);
                    while (rs2.next()) {
                        strTemplate = getPlantilla(oConn, rs2.getString("CRC_ID_PLANTILLA"));
                        intIdPlantillaD = rs2.getInt("CRC_ID");
                    }
                    tmp.setIntIdMasivo(Integer.parseInt(strCRM_ID));
                    tmp.setIntIdCte(intIdCte);
                    tmp.setStrCorreoDest(strCorreo);

                    if (strTemplate.equals("PERSONALIZADO")) {
                        //funcion de correo de 3 meses
                        if (CteMailMensual(oConn, intIdCte, strMes)) {
                            strResp = tmp.Cofide3Cursos(oConn, intEjecutivo, strCorreo, strAsunto, 0, strTemplate, intIdPlantillaD);
                        }
                    }

                    if (strTemplate.equals("SEDE")) {
                        //funcion de correo de sede
                        if (CteMailMensual(oConn, intIdCte, strMes)) {
                            String strSqlSede = "select CT_SEDE from vta_cliente where CT_ID = " + intIdCte;
                            ResultSet rsx = oConn.runQuery(strSqlSede, true);
                            while (rsx.next()) {
                                strResp = tmp.CofideSede(oConn, intEjecutivo, strCorreo, rsx.getString("CT_SEDE"), strAsunto, 0, strTemplate, intIdPlantillaD);
                            }
                            rsx.close();
                        }
                    }

                    if (strTemplate.equals("RECURRENTE")) {
                        //funcion de correo de recurrente
                        if (CteMailMensual(oConn, intIdCte, strMes)) {
                            strResp = tmp.CofideRecurrente(oConn, intEjecutivo, strCorreo, strCurso, strAsunto, 0, strTemplate, intIdPlantillaD);
                        }
                    }
                    if (strTemplate.equals("MES")) {
                        //funcion de correo de mes
                        strResp = tmp.CofideMensual(oConn, intEjecutivo, strCorreo, strAsunto, 0, strTemplate, intIdPlantillaD);
                    }

                    if (strTemplate.equals("DIPLOMADO")) {
                        //funcion de correo de diplomado
                        if (CteMailMensual(oConn, intIdCte, strMes)) {
                            strResp = tmp.CofideDiplomado(oConn, intEjecutivo, strCorreo, strAsunto, 0, strTemplate, intIdPlantillaD);
                        }
                    }

                    if (strTemplate.equals("SEMINARIO")) {
                        //funcion de correo de seminario
                        if (CteMailMensual(oConn, intIdCte, strMes)) {
                            strResp = tmp.CofideDiplomado(oConn, intEjecutivo, strCorreo, strAsunto, 0, strTemplate, intIdPlantillaD);
                        }
                    }

                    if (strTemplate.equals("DIPLOMADO_MOD")) {
                        //funcion de correo de diplomado modulos
                        if (CteMailMensual(oConn, intIdCte, strMes)) {
                            strResp = tmp.CofideDipMod(oConn, intEjecutivo, strCorreo, strAsunto, 0, strTemplate, intIdPlantillaD);
                        }
                    }

                    if (strTemplate.equals("PUNTOS DE LEALTAD")) {
                        //funcion de correo de diplomado modulos
                        if (CteMailMensual(oConn, intIdCte, strMes)) {
                            strResp = tmp.CofidePLealtadCursos(oConn, intEjecutivo, strCorreo, strAsunto, 0, strTemplate, intIdPlantillaD);
                        }
                    }

                    if (strTemplate.equals("HTML")) {
                        //funcion de correo de HTML personbalizable
                        if (CteMailMensual(oConn, intIdCte, strMes)) {
                            strResp = tmp.CofideHTML(oConn, intEjecutivo, strCorreo, strAsunto, 0, strTemplate, intIdPlantillaD);
                        }
                    }

                    if (strTemplate.equals("COFIDEnet")) {
                        //funcion de correo de cofidenet
                        if (CteMailMensual(oConn, intIdCte, strMes)) {
                            strResp = tmp.COFIDEnet(oConn, intEjecutivo, strCorreo, strAsunto, 0, strTemplate, intIdPlantillaD);
                        }
                    }

                    if (strResp.equals("OK")) {
                        String strSqlMail = "update crm_envio_masivo_deta set CRMD_ESTATUS = 'CORRECTO', CRM_PROCESADO = 1, CRMD_FECHA = '" + fec.getFechaActual() + "', CRMD_HORA = '" + fec.getHoraActual() + "' "
                                + "where CT_ID = " + intIdCte + " and CRMD_EMAIL = '" + strCorreo + "' and CRM_ID = '" + strCRM_ID + "'";
                        oConn.runQueryLMD(strSqlMail);
                    } else {
                        String strSqlErr2 = "update crm_envio_masivo_deta set CRMD_ESTATUS = 'Verificar el correo', CRM_PROCESADO = 1, CRMD_FECHA = '" + fec.getFechaActual() + "', CRMD_HORA = '" + fec.getHoraActual() + "' "
                                + "where CT_ID = " + intIdCte + " and CRMD_EMAIL = '" + strCorreo + "' and CRM_ID = '" + strCRM_ID + "'";
                        oConn.runQueryLMD(strSqlErr2);
                    }

                } else {

                    System.out.println(" LA BASE DE DATOS DEL ID: " + intIdCte + " NO TIENE RELACIÓN CON ALGUN EJECUTIVO. ");

                }

            } else {

                String strSqlErr3 = "update crm_envio_masivo_deta set CRMD_ESTATUS = 'Formato no valido', CRM_PROCESADO = 1, CRMD_FECHA = '" + fec.getFechaActual() + "', CRMD_HORA = '" + fec.getHoraActual() + "' "
                        + "where CT_ID = " + intIdCte + " and CRMD_EMAIL = '" + strCorreo + "' and CRM_ID = '" + strCRM_ID + "'";

                oConn.runQueryLMD(strSqlErr3);
            }

        }
        rs.close();
    }

    /**
     *
     * @param oConn
     * @param intIdCte cliente a consultar
     * @param strMensual el masivo se programo en 0 solo se lo enviara a los que
     * no tengan @Mensual, si tiene 1 = se le enviara a todos
     * @return bolResp si es true: le envia a todos los clientes, si es falso,
     * unicamente a lso cleinte que no tienen restriccion = 1,
     * @throws java.sql.SQLException
     */
    public boolean CteMailMensual(Conexion oConn, int intIdCte, String strMensual) throws SQLException {
        boolean bolResp = true;
        String strMailMensualCte = "";
        if (strMensual.equals("0")) { //si es mensual = 0, solo le va a mandar a los clientes que no tengan restriccion
            String strSql = "select CT_MAILMES from vta_cliente where CT_ID = " + intIdCte;
            ResultSet rs;
            rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                strMailMensualCte = rs.getString("CT_MAILMES");
            }
            rs.close();
            //valida restriccion para no mandarle a los que tngan restriccion
            if (strMailMensualCte.equals("1")) { //si el cliente = 1, no le va a mandar mail masivo
                bolResp = false;
            }
        }
        return bolResp;
    }

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
            System.out.println("ERROR: " + ex);
        }
        return strPlantilla;
    }

    public boolean setListaMail(Conexion oConn, String strCorreo, String strIdMasivo) {
        boolean bolListado = false;
        String strSql = "";
        strSql = "select CRMD_EMAIL from crm_envio_masivo_deta where CRMD_EMAIL = '" + strCorreo + "' and CRM_ID = " + strIdMasivo + " and CRMD_LISTADO = 0;";
        try {

            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                bolListado = true;
                strSql = "update crm_envio_masivo_deta set CRMD_LISTADO = 1 where CRMD_EMAIL = '" + strCorreo + "' and CRM_ID = '" + strIdMasivo + "'";
                oConn.runQueryLMD(strSql);

                if (oConn.isBolEsError()) {
                    System.out.println("Error al registrar en la lista el mail, para evitar mail duplicados [ " + oConn.getStrMsgError() + " ]");
                }
            }
            rs.close();

        } catch (SQLException sql) {
            System.out.println("Errror al validar el correo: " + sql.getErrorCode());
        }
        return bolListado;
    }
}
