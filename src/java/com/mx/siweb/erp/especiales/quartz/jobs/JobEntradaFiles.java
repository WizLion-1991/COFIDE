///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.mx.siweb.erp.especiales.quartz.jobs;
//
//import com.siweb.pfizer.panama.interfases.entrada.Demanda;
//import com.siweb.pfizer.panama.interfases.entrada.InvProdTerminado;
//import com.siweb.pfizer.panama.interfases.utils.ParserCols;
//import comSIWeb.Operaciones.Conexion;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import org.apache.logging.log4j.LogManager;
//import org.quartz.Job;
//import org.quartz.JobExecutionContext;
//import org.quartz.JobExecutionException;
//
///**
// * Job para procesar los archivos de entrada
// *
// * @author ZeusSIWEB
// */
//public class JobEntradaFiles implements Job {
//
//   // <editor-fold defaultstate="collapsed" desc="Propiedades">
//   private static final org.apache.logging.log4j.Logger LOG = LogManager.getLogger(JobEntradaFiles.class.getName());
//
//   // </editor-fold>
//   // <editor-fold defaultstate="collapsed" desc="Constructores">
//   // </editor-fold>
//   // <editor-fold defaultstate="collapsed" desc="Metodos">
//   @Override
//   public void execute(JobExecutionContext context) throws JobExecutionException {
//      try {
//         Conexion oConn = new Conexion();
//         LOG.debug("Revisamos paths por revisar");
//         //Paths
//         String strPathInvTerminado = "";
//         //Detecta cambios en el archivo de demanda y lo actualiza
//         String strSql = "select * from pfi_directory_base";
//         ResultSet rs = oConn.runQuery(strSql);
//         while (rs.next()) {
//            if (rs.getString("FI_ABRV").equals("PROD_TERMINADO")) {
//               strPathInvTerminado = rs.getString("FI_PATH");
//            }
//         }
//         rs.close();
//         //Procesamos por cada tipo de archivo
//         if (!strPathInvTerminado.isEmpty()) {
//            LOG.debug("Producto terminado...");
//            InvProdTerminado prodTerm = new InvProdTerminado(oConn);
//            prodTerm.processFile(strPathInvTerminado, strPathInvTerminado + "/finished/", oConn);
//         }
//         oConn.close();
//      } catch (SQLException ex) {
//         Logger.getLogger(JobEntradaFiles.class.getName()).log(Level.SEVERE, null, ex);
//      }
//   }
//   // </editor-fold>
//}
