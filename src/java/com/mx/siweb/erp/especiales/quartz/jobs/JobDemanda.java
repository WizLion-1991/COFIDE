///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.mx.siweb.erp.especiales.quartz.jobs;
//
//import com.siweb.pfizer.panama.interfases.entrada.Demanda;
//import comSIWeb.Operaciones.Conexion;
//import org.quartz.Job;
//import org.quartz.JobExecutionContext;
//import org.quartz.JobExecutionException;
//
///**
// *
// * @author ZeusSIWEB
// */
//public class JobDemanda implements Job {
//
//   // <editor-fold defaultstate="collapsed" desc="Propiedades">
//    // </editor-fold>
//    // <editor-fold defaultstate="collapsed" desc="Constructores">
//    // </editor-fold>
//    // <editor-fold defaultstate="collapsed" desc="Metodos">
//    @Override
//    public void execute(JobExecutionContext context) throws JobExecutionException {
//        Conexion oConn = new Conexion();
//        //Detecta cambios en el archivo de demanda y lo actualiza
//        Demanda demanda = new Demanda();
//        demanda.setoConn(oConn);
//        demanda.doLeeXLS("/Users/ZeusSIWEB/Desktop/tmp_pfizer/Panama/20160426/Forecasts por Codigo-Por Pais.xlsx");
//        //Genera la OTC en base a la demanda
//        oConn.close();
//    }
//
//   // </editor-fold>
//}
