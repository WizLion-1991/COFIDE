/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.especiales.quartz.jobs;

import comSIWeb.Operaciones.Conexion;
import org.apache.logging.log4j.LogManager;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Realiza las operaciones de sincronizacion por ftp
 *
 * @author ZeusSIWEB
 */
public class JobFtp implements Job {

    // <editor-fold defaultstate="collapsed" desc="Propiedades">
    private static final org.apache.logging.log4j.Logger LOG = LogManager.getLogger(JobFtp.class.getName());
    private Conexion oConn;

    public Conexion getoConn() {
        return oConn;
    }

    public void setoConn(Conexion oConn) {
        this.oConn = oConn;
    }

   // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Constructores">
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Metodos">
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        //Descarga los archivos de ftp y los sincroniza
        LOG.debug("Iniciando tarea...");
    }
   // </editor-fold>
}


