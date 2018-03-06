/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.quartz.jobs;

import com.mx.siweb.erp.especiales.cofide.CRM_MailMasivo;
import comSIWeb.Operaciones.Conexion;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Ejecuta los mails masivos en automatico
 *
 * @author ZeusSIWEB
 */
public class JobMailMasivo implements Job {

    // <editor-fold defaultstate="collapsed" desc="Propiedades">
//    CRM_MailMasivo crm = new CRM_MailMasivo();
    public int IntEjecutvo = 0;

    public int getIntEjecutvo() {
        return IntEjecutvo;
    }

    public void setIntEjecutvo(int IntEjecutvo) {
        this.IntEjecutvo = IntEjecutvo;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Constructores">
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Metodos">
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        CRM_MailMasivo crm = new CRM_MailMasivo();
        Conexion oConn = new Conexion();
        oConn.open();
        try {
            crm.EnvioMail(oConn); //funcion que manda los correos
            System.out.println("Comienza el envio de masivos...");
        } catch (SQLException ex) {
            Logger.getLogger(JobMailMasivo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(JobMailMasivo.class.getName()).log(Level.SEVERE, null, ex);
        }
        oConn.close();
    }
    // </editor-fold>
}
