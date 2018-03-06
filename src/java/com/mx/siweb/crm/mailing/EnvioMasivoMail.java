/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.crm.mailing;

import Tablas.CrmMailMasivoBitacora;
import com.mx.siweb.crm.mailing.entidades.TablaAdicional;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Utilerias.Fechas;
import comSIWeb.Utilerias.Mail;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.logging.log4j.LogManager;

/**
 * Esta clase se encarga de realizar el envio masivo de mails...
 *
 * @author ZeusSIWEB
 */
public class EnvioMasivoMail {

    // <editor-fold defaultstate="collapsed" desc="Propiedades">
    private Conexion oConn = null;
    private String strNombreTemplate;
    private String strResultLast;
    private boolean bolActivoDebug;
    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(EnvioMasivoMail.class.getName());
    private VariableSession sesion;
    private Fechas fecha = null;
    private int intMailUsar;
    private Mail mail = null;
    private String strMsgGlobal = null;
    private String strAsuntoGlobal = null;
    private ArrayList<TablaAdicional> listaAdicionales = null;

    public ArrayList<TablaAdicional> getListaAdicionales() {
        if (this.listaAdicionales == null) {
            this.listaAdicionales = new ArrayList<TablaAdicional>();
        }
        return listaAdicionales;
    }

    public void setListaAdicionales(ArrayList<TablaAdicional> listaAdicionales) {
        this.listaAdicionales = listaAdicionales;
    }

    public int getIntMailUsar() {
        return intMailUsar;
    }

    public void setIntMailUsar(int intMailUsar) {
        this.intMailUsar = intMailUsar;
    }

    public String getStrResultLast() {
        return strResultLast;
    }

    public void setStrResultLast(String strResultLast) {
        this.strResultLast = strResultLast;
    }

    public VariableSession getSesion() {
        return sesion;
    }

    public void setSesion(VariableSession sesion) {
        this.sesion = sesion;
    }

    public boolean isBolActivoDebug() {
        return bolActivoDebug;
    }

    public void setBolActivoDebug(boolean bolActivoDebug) {
        this.bolActivoDebug = bolActivoDebug;
    }

    public String getStrNombreTemplate() {
        return strNombreTemplate;
    }

    public void setStrNombreTemplate(String strNombreTemplate) {
        this.strNombreTemplate = strNombreTemplate;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Constructores">
    public EnvioMasivoMail(Conexion oConn, String strNombreTemplate, boolean bolActivoDebug, VariableSession sesion) {
        this.oConn = oConn;
        this.strNombreTemplate = strNombreTemplate;
        this.bolActivoDebug = bolActivoDebug;
        this.sesion = sesion;
        this.fecha = new Fechas();
        this.intMailUsar = 1;
        this.mail = new Mail();
        //Obtenemos el mail
        mail.setBolDepuracion(this.bolActivoDebug);
        mail.getTemplate(this.strNombreTemplate, oConn);
        this.strMsgGlobal = mail.getMensaje();
        this.strAsuntoGlobal = mail.getAsunto();
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Metodos">
    public void envioCorreoMasivo(String strFiltro) {
        System.out.println(strFiltro);
        this.strResultLast = "OK";
        this.mail.setBolMantenerConexion(true);
        //Buscamos todos los clientes que cumplan el filtro
        String strSql = "select CT_ID,CT_EMAIL1,CT_EMAIL2,CT_EMAIL3,CT_EMAIL4,CT_EMAIL5"
                + " ,CT_EMAIL6,CT_EMAIL7,CT_EMAIL8,CT_EMAIL9,CT_EMAIL10"
                + "  from vta_cliente " + strFiltro;
        try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                int intIdCliente = rs.getInt("CT_ID");
                String strCorreo1 = null;
                if (intMailUsar == 1) {
                    strCorreo1 = rs.getString("CT_EMAIL1");
                }
                if (intMailUsar == 2) {
                    strCorreo1 = rs.getString("CT_EMAIL2");
                }
                if (intMailUsar == 3) {
                    strCorreo1 = rs.getString("CT_EMAIL3");
                }
                if (intMailUsar == 4) {
                    strCorreo1 = rs.getString("CT_EMAIL4");
                }
                if (intMailUsar == 5) {
                    strCorreo1 = rs.getString("CT_EMAIL5");
                }
                if (intMailUsar == 6) {
                    strCorreo1 = rs.getString("CT_EMAIL6");
                }
                if (intMailUsar == 7) {
                    strCorreo1 = rs.getString("CT_EMAIL7");
                }
                if (intMailUsar == 8) {
                    strCorreo1 = rs.getString("CT_EMAIL8");
                }
                if (intMailUsar == 9) {
                    strCorreo1 = rs.getString("CT_EMAIL9");
                }
                if (intMailUsar == 10) {
                    strCorreo1 = rs.getString("CT_EMAIL10");
                }
//                envioMail(intIdCliente, strCorreo1);
                int intProcesado = 0;
                if (envioMail(intIdCliente, strCorreo1)) {
                    intProcesado = 1;
                }
                String strSqlProc = "update crm_envio_masivo_deta set CRM_PROCESADO = " + intProcesado + " where CT_ID = " + intIdCliente;
                oConn.runQueryLMD(strSqlProc);
            }
            rs.close();
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            this.strResultLast = "Error:" + ex.getMessage();
        }
        //Cerramos la conexion
        mail.cerrarConexion();
    }

    /**
     * Envia el correo a un cliente y correo especificado
     *
     * @param intIdCliente Es el cliente
     * @param strEmail1 Es el correo
     */
    public boolean envioMail(int intIdCliente, String strEmail1) {
//    public void envioMail(int intIdCliente, String strEmail1) {
        boolean bol = false;

        CrmMailMasivoBitacora bitacora = new CrmMailMasivoBitacora();
        bitacora.setFieldString("BM_FECHA", fecha.getFechaActual());
        bitacora.setFieldString("BM_HORA", fecha.getHoraActualHHMMSS());
        bitacora.setFieldInt("BM_USUARIO", this.sesion.getIntNoUser());
        bitacora.setFieldString("BM_TEMPLATE", this.strNombreTemplate);
        //Anexamos el mail
        bitacora.setFieldString("BM_MAIL", strEmail1);
        //validamos que hallan puesto el mail
        if (mail.isEmail(strEmail1)) {
            //Validamos que no este en una lista negra
            if (!bolEstaEnListaNegra(strEmail1)) {

                mail.setMensaje(new String(this.strMsgGlobal));
                mail.setAsunto(new String(this.strAsuntoGlobal));
                //Intentamos mandar el mail
                String strSqlEmp = "SELECT * FROM vta_cliente"
                        + " where CT_ID=" + intIdCliente + "";
                try {
                    ResultSet rs = oConn.runQuery(strSqlEmp);
                    mail.setReplaceContent(rs);
                    //rs.getStatement().close();
                    rs.close();
                } catch (SQLException ex) {
                    log.error(ex.getMessage());
                }
                ReemplazaTablasAdicionales(intIdCliente);
                mail.setDestino(strEmail1);
                bol = mail.sendMail();
//                boolean bol = mail.sendMail();
                if (bol) {
                    //strResp = "MAIL ENVIADO.";
                    bitacora.setFieldInt("BM_ENVIO", 1);
                    String strSqlMail = "update crm_envio_masivo_deta set CRMD_ESTATUS = 'CORRECTO' where CT_ID = " + intIdCliente;
                    oConn.runQueryLMD(strSqlMail);
                } else {
                    //strResp = "FALLO EL ENVIO DEL MAIL.";
                    bitacora.setFieldInt("BM_ENVIO", 0);
                    bitacora.setFieldString("BM_RESPUESTA", mail.getErrMsg());
                }
            } else {
                bitacora.setFieldString("BM_RESPUESTA", "Error: el correo esta en la lista negra");
                String errorLN = "Error: el correo esta en la lista negra";
                String strSqlErr1 = "update crm_envio_masivo_deta set CRMD_ESTATUS = '" + errorLN + "' where CT_ID = " + intIdCliente;
                oConn.runQueryLMD(strSqlErr1);
            }
        } else {
            bitacora.setFieldInt("BM_ENVIO", 0);
            bitacora.setFieldString("BM_RESPUESTA", "Error: el correo no es valido no tiene un formato correcto");
            String errorM = "Error: el correo no es valido no tiene un formato correcto";
            String strSqlErr2 = "update crm_envio_masivo_deta set CRMD_ESTATUS = '" + errorM + "' where CT_ID = " + intIdCliente;
            oConn.runQueryLMD(strSqlErr2);
        }
        bitacora.Agrega(oConn);
        return bol; //regresa la respuesta de, sí, si mando el correo teue o false
    }

    /**
     * Indica si el correo esta en la lista negra
     *
     * @param strCorreo Es el correo
     * @return True si se encontro
     */
    public boolean bolEstaEnListaNegra(String strCorreo) {
        boolean bolSiEsta = false;
        //cofide_lista_negra
        String strSqlEmp = "SELECT CLN_ID FROM cofide_lista_negra"
                + " where CB_CT_CORREO='" + strCorreo + "'";
        try {
            ResultSet rs = oConn.runQuery(strSqlEmp);
            while (rs.next()) {
                bolSiEsta = true;
                break;
            }
            //rs.getStatement().close();
            rs.close();
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            this.strResultLast = "Error:" + ex.getMessage();
        }
        return bolSiEsta;
    }

    private void ReemplazaTablasAdicionales(int intIdCliente) {
        if (this.listaAdicionales != null) {
            Iterator<TablaAdicional> it = this.listaAdicionales.iterator();
            while (it.hasNext()) {
                TablaAdicional tbn = it.next();
                String strSqlEmp = "SELECT * FROM " + tbn.getStrNombreTabla()
                        + " where " + tbn.getStrNombreCampoLink() + "=" + intIdCliente + "";
                try {
                    ResultSet rs = oConn.runQuery(strSqlEmp);
                    mail.setReplaceContent(rs);
                    //rs.getStatement().close();
                    rs.close();
                } catch (SQLException ex) {
                    log.error(ex.getMessage());
                }
            }
        }
    }
// </editor-fold>
}
