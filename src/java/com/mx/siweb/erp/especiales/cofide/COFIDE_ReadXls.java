/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.especiales.cofide;

import comSIWeb.Operaciones.Conexion;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/**
 *
 * @author Desarrollo_COFIDE
 */
public class COFIDE_ReadXls {

    Conexion oConn;
    String strRespuesta = "";

    public COFIDE_ReadXls(Conexion oConn) {
        this.oConn = oConn;
    }

    public String readXLS(String strPathexcel) {
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
        strRespuesta = "OK";
        return strRespuesta;
    }
}
