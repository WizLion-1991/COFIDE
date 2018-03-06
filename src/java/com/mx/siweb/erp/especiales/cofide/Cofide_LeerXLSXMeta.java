/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.especiales.cofide;

import Tablas.cofide_metas_usuario;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Utilerias.Fechas;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.util.Iterator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/**
 *
 * @author juliocesar
 */
public class Cofide_LeerXLSXMeta {

    // <editor-fold defaultstate="collapsed" desc="Propiedades">
    Conexion oConn;
    static final Logger log = LogManager.getLogger(Cofide_LeerXLSXMeta.class.getName());
    String strResultLast;
    boolean bolLimpiarInfo = false;
    int intSC_ID;
    int intEmpId;
    String strAnio, strPR_CODIGO, strPR_DESCIRPCION;
    VariableSession varSesiones;
    String strSql;
    ResultSet rs = null;
    int page, rows;
    Fechas fec = new Fechas();

    public Cofide_LeerXLSXMeta(Conexion oConn) {
        this.oConn = oConn;
    }

    // </editor-fold>
    public String importaXLSXMetas(String strPathexcel) {
        this.strResultLast = "OK";
        try {
            InputStream myxls = null;
            myxls = new FileInputStream(strPathexcel);
            HSSFWorkbook archivoPresupuestos = new HSSFWorkbook(myxls);
            Iterator<Row> it = archivoPresupuestos.getSheetAt(0).rowIterator();
            it.next();
            while (it.hasNext()) {
                Row row = it.next();
                Cell nombre = row.getCell(0);
                Cell segunname = row.getCell(1);
                Cell appat = row.getCell(2);
                Cell apmat = row.getCell(3);

                System.out.println((nombre != null ? nombre : "")
                        + " " + (segunname != null ? segunname : "")
                        + " " + (appat != null ? appat : "")
                        + " " + (apmat != null ? apmat : ""));

            }
        } catch (IOException ex) {
            System.out.println("Error: " + ex);
        }
        return this.strResultLast;
    }
}
