/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.especiales.cofide;

import comSIWeb.Operaciones.Conexion;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import org.apache.log4j.LogManager;
//import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author juliomondragon
 */
public class COFIDE_LeerXlsDDBB {

    // <editor-fold defaultstate="collapsed" desc="Propiedades">
    Conexion oConn;
    static final Logger log = LogManager.getLogger(COFIDE_LeerXlsDDBB.class.getName());
    String strResultLast;

    public COFIDE_LeerXlsDDBB(Conexion oConn) {
        this.oConn = oConn;
    }

    // </editor-fold>
    public String importaXLS(String strPathexcel) {
        this.strResultLast = "OK";
        try {
            InputStream myxls = null;
            myxls = new FileInputStream(strPathexcel);
            HSSFWorkbook archivoPresupuestos = new HSSFWorkbook(myxls);
            Iterator<Row> it = archivoPresupuestos.getSheetAt(0).rowIterator();
            it.next();
            while (it.hasNext()) {
                Row row = it.next();
                Cell cell_Contacto = row.getCell(0);
                Cell cell_Razon = row.getCell(1);
                Cell cell_Telefono1 = row.getCell(2);
                Cell cell_Telefono2 = row.getCell(3);
                Cell cell_Email1 = row.getCell(4);
                Cell cell_Email2 = row.getCell(5);
                Cell cell_Estado = row.getCell(6);

                vta_clientes vta = new vta_clientes();
                vta.setFieldString("CT_CONTACTO1", cell_Contacto.getStringCellValue());
                vta.setFieldString("CT_RAZONSOCIAL", cell_Razon.getStringCellValue());
                vta.setFieldInt("CT_TELEFONO1", (int) cell_Telefono1.getNumericCellValue());
                vta.setFieldInt("CT_TELEFONO2", (int) cell_Telefono2.getNumericCellValue());
                vta.setFieldString("CT_EMAIL1", cell_Email1.getStringCellValue());
                vta.setFieldString("CT_EMAIL2", cell_Email2.getStringCellValue());
                vta.setFieldString("CT_ESTADO", cell_Estado.getStringCellValue());
                vta.setFieldString("CT_CLAVE_DDBB", "LIBRE");
                vta.setFieldInt("PA_ID", 1);
                vta.Agrega(oConn);
//                System.out.println(cell_Contacto.getStringCellValue());
            }
        } catch (IOException ex) {
            System.out.println("Error: " + ex);
        }
        return this.strResultLast;
    }

    public String importaXLSTest(String strPathexcel) {
        this.strResultLast = "OK";
        try {
            InputStream myxls = null;
            myxls = new FileInputStream(strPathexcel);
            HSSFWorkbook archivoPresupuestos = new HSSFWorkbook(myxls);
            Iterator<Row> it = archivoPresupuestos.getSheetAt(0).rowIterator();
            it.next();
            while (it.hasNext()) {
                Row row = it.next();
//                Cell cell_intIdCte = row.getCell(2);
//                Cell cell_strRazonSocial = row.getCell(3);
//                Cell cell_intNoCte = row.getCell(4);
//                Cell cell_strCp = row.getCell(10);
//
//                Cell cell_strEmail1 = row.getCell(11);
//                Cell cell_strEmail2 = row.getCell(12);
//                Cell cell_strEmail3 = row.getCell(13);
//                Cell cell_strEmail4 = row.getCell(14);
//                Cell cell_strEmail5 = row.getCell(15);
//                Cell cell_strEmail6 = row.getCell(16);
//                Cell cell_strEmail7 = row.getCell(17);
//                Cell cell_strEmail8 = row.getCell(18);
//
//                Cell cell_strTelefono8 = row.getCell(20);
//                Cell cell_strTelefono7 = row.getCell(21);
//                Cell cell_strTelefono6 = row.getCell(22);
//                Cell cell_strTelefono5 = row.getCell(23);
//                Cell cell_strTelefono4 = row.getCell(24);
//                Cell cell_strTelefono3 = row.getCell(25);
//                Cell cell_strTelefono2 = row.getCell(26);
//                Cell cell_strTelefono1 = row.getCell(27);
//
//                Cell cell_strEdo = row.getCell(28);
//                Cell cell_strDeleg = row.getCell(29);
//                Cell cell_strCol = row.getCell(30);
//                Cell cell_strCalle = row.getCell(31);
//
//                Cell cell_strNomCont8 = row.getCell(33);
//                Cell cell_strNomCont7 = row.getCell(34);
//                Cell cell_strNomCont6 = row.getCell(35);
//                Cell cell_strNomCont5 = row.getCell(36);
//                Cell cell_strNomCont4 = row.getCell(37);
//                Cell cell_strNomCont3 = row.getCell(38);
//                Cell cell_strNomCont2 = row.getCell(39);
//                Cell cell_strNomCont1 = row.getCell(40);
//
//                Cell cell_strAp1 = row.getCell(41);
//                Cell cell_strAp2 = row.getCell(42);
//                Cell cell_strAp3 = row.getCell(43);
//                Cell cell_strAp4 = row.getCell(44);
//                Cell cell_strAp5 = row.getCell(45);
//                Cell cell_strAp6 = row.getCell(46);
//                Cell cell_strAp7 = row.getCell(47);
//                Cell cell_strAp8 = row.getCell(48);
//
//                Cell cell_strAm1 = row.getCell(49);
//                Cell cell_strAm2 = row.getCell(50);
//                Cell cell_strAm3 = row.getCell(51);
//                Cell cell_strAm4 = row.getCell(52);
//                Cell cell_strAm5 = row.getCell(53);
//                Cell cell_strAm6 = row.getCell(54);
//                Cell cell_strAm7 = row.getCell(55);
//                Cell cell_strAm8 = row.getCell(56);
//
//                Cell cell_strRFC = row.getCell(58);
//
//                Cell cell_strTitle8 = row.getCell(59);
//                Cell cell_strTitle7 = row.getCell(60);
//                Cell cell_strTitle6 = row.getCell(61);
//                Cell cell_strTitle5 = row.getCell(62);
//                Cell cell_strTitle4 = row.getCell(63);
//                Cell cell_strTitle3 = row.getCell(64);
//                Cell cell_strTitle2 = row.getCell(65);
//                Cell cell_strTitle1 = row.getCell(66);
//
//                Cell cell_strArea8 = row.getCell(67);
//                Cell cell_strArea7 = row.getCell(68);
//                Cell cell_strArea6 = row.getCell(69);
//                Cell cell_strArea5 = row.getCell(70);
//                Cell cell_strArea4 = row.getCell(71);
//                Cell cell_strArea3 = row.getCell(72);
//                Cell cell_strArea2 = row.getCell(73);
//                Cell cell_strArea1 = row.getCell(74);
//
//                Cell cell_strGiro = row.getCell(75);
//
//                Cell cell_strClaveDDBB = row.getCell(84);
//
//                Cell cell_strSede = row.getCell(109);
//
//                Cell cell_strExt1 = row.getCell(110);
//                Cell cell_strExt2 = row.getCell(111);
//                Cell cell_strExt3 = row.getCell(112);
//                Cell cell_strExt4 = row.getCell(113);
//                Cell cell_strExt5 = row.getCell(114);
//                Cell cell_strExt6 = row.getCell(115);
//                Cell cell_strExt7 = row.getCell(116);
//                Cell cell_strExt8 = row.getCell(117);
//
//                Cell cell_strConctactoIn = row.getCell(118);
//                Cell cell_strConmutador = row.getCell(120);
//                Cell cell_strMailIn = row.getCell(121);
//
//                Cell cell_strNoSoc1 = row.getCell(122);
//                Cell cell_strNoSoc2 = row.getCell(123);
//                Cell cell_strNoSoc3 = row.getCell(124);
//                Cell cell_strNoSoc4 = row.getCell(125);
//                Cell cell_strNoSoc5 = row.getCell(126);
//                Cell cell_strNoSoc6 = row.getCell(127);
//                Cell cell_strNoSoc7 = row.getCell(128);
//                Cell cell_strNoSoc8 = row.getCell(129);
//
//                Cell cell_strOrg1 = row.getCell(130);
//                Cell cell_strOrg2 = row.getCell(131);
//                Cell cell_strOrg3 = row.getCell(132);
//                Cell cell_strOrg4 = row.getCell(133);
//                Cell cell_strOrg5 = row.getCell(134);
//                Cell cell_strOrg6 = row.getCell(135);
//                Cell cell_strOrg7 = row.getCell(136);
//                Cell cell_strOrg8 = row.getCell(137);
//
//                Cell cell_strRazon2 = row.getCell(138);
//                Cell cell_strRazon3 = row.getCell(139);
//                Cell cell_strRazon4 = row.getCell(140);
//                Cell cell_strRazon5 = row.getCell(141);
//
//                Cell cell_intCteProsp = row.getCell(163);
//
//                Cell cell_strCell1 = row.getCell(164);
//                Cell cell_strCell2 = row.getCell(165);
//                Cell cell_strCell3 = row.getCell(166);
//                Cell cell_strCell4 = row.getCell(167);
//                Cell cell_strCell5 = row.getCell(168);
//                Cell cell_strCell6 = row.getCell(169);
//                Cell cell_strCell7 = row.getCell(170);
//                Cell cell_strCell8 = row.getCell(171);

//                System.out.println("test " + cell_strRazonSocial.getStringCellValue() + " " + cell_strRFC.getStringCellValue() + "\n");
            }
        } catch (IOException ex) {
            System.out.println("Error: " + ex);
        }
        return this.strResultLast;
    }

//    public String ImpDDBB_XLS(String strPathexcel) {
//        this.strResultLast = "OK";
//        try {
//            InputStream myxls = null;
//            myxls = new FileInputStream(strPathexcel);
//            HSSFWorkbook archivoPresupuestos = new HSSFWorkbook(myxls);
//            Iterator<Row> it = archivoPresupuestos.getSheetAt(0).rowIterator();
//            it.next();
//            while (it.hasNext()) {
//                Row row = it.next();
//                Cell cell_intIdCte = row.getCell(2);
//                Cell cell_strRazonSocial = row.getCell(3);
//                Cell cell_intNoCte = row.getCell(4);
//                Cell cell_strCp = row.getCell(10);
//
//                Cell cell_strEmail1 = row.getCell(11);
//                Cell cell_strEmail2 = row.getCell(12);
//                Cell cell_strEmail3 = row.getCell(13);
//                Cell cell_strEmail4 = row.getCell(14);
//                Cell cell_strEmail5 = row.getCell(15);
//                Cell cell_strEmail6 = row.getCell(16);
//                Cell cell_strEmail7 = row.getCell(17);
//                Cell cell_strEmail8 = row.getCell(18);
//
//                Cell cell_strTelefono8 = row.getCell(20);
//                Cell cell_strTelefono7 = row.getCell(21);
//                Cell cell_strTelefono6 = row.getCell(22);
//                Cell cell_strTelefono5 = row.getCell(23);
//                Cell cell_strTelefono4 = row.getCell(24);
//                Cell cell_strTelefono3 = row.getCell(25);
//                Cell cell_strTelefono2 = row.getCell(26);
//                Cell cell_strTelefono1 = row.getCell(27);
//
//                Cell cell_strEdo = row.getCell(28);
//                Cell cell_strDeleg = row.getCell(29);
//                Cell cell_strCol = row.getCell(30);
//                Cell cell_strCalle = row.getCell(31);
//
//                Cell cell_strNomCont8 = row.getCell(33);
//                Cell cell_strNomCont7 = row.getCell(34);
//                Cell cell_strNomCont6 = row.getCell(35);
//                Cell cell_strNomCont5 = row.getCell(36);
//                Cell cell_strNomCont4 = row.getCell(37);
//                Cell cell_strNomCont3 = row.getCell(38);
//                Cell cell_strNomCont2 = row.getCell(39);
//                Cell cell_strNomCont1 = row.getCell(40);
//
//                Cell cell_strAp1 = row.getCell(41);
//                Cell cell_strAp2 = row.getCell(42);
//                Cell cell_strAp3 = row.getCell(43);
//                Cell cell_strAp4 = row.getCell(44);
//                Cell cell_strAp5 = row.getCell(45);
//                Cell cell_strAp6 = row.getCell(46);
//                Cell cell_strAp7 = row.getCell(47);
//                Cell cell_strAp8 = row.getCell(48);
//
//                Cell cell_strAm1 = row.getCell(49);
//                Cell cell_strAm2 = row.getCell(50);
//                Cell cell_strAm3 = row.getCell(51);
//                Cell cell_strAm4 = row.getCell(52);
//                Cell cell_strAm5 = row.getCell(53);
//                Cell cell_strAm6 = row.getCell(54);
//                Cell cell_strAm7 = row.getCell(55);
//                Cell cell_strAm8 = row.getCell(56);
//
//                Cell cell_strRFC = row.getCell(58);
//
//                Cell cell_strTitle8 = row.getCell(59);
//                Cell cell_strTitle7 = row.getCell(60);
//                Cell cell_strTitle6 = row.getCell(61);
//                Cell cell_strTitle5 = row.getCell(62);
//                Cell cell_strTitle4 = row.getCell(63);
//                Cell cell_strTitle3 = row.getCell(64);
//                Cell cell_strTitle2 = row.getCell(65);
//                Cell cell_strTitle1 = row.getCell(66);
//
//                Cell cell_strArea8 = row.getCell(67);
//                Cell cell_strArea7 = row.getCell(68);
//                Cell cell_strArea6 = row.getCell(69);
//                Cell cell_strArea5 = row.getCell(70);
//                Cell cell_strArea4 = row.getCell(71);
//                Cell cell_strArea3 = row.getCell(72);
//                Cell cell_strArea2 = row.getCell(73);
//                Cell cell_strArea1 = row.getCell(74);
//
//                Cell cell_strGiro = row.getCell(75);
//
//                Cell cell_strClaveDDBB = row.getCell(84);
//
//                Cell cell_strSede = row.getCell(109);
//
//                Cell cell_strExt1 = row.getCell(110);
//                Cell cell_strExt2 = row.getCell(111);
//                Cell cell_strExt3 = row.getCell(112);
//                Cell cell_strExt4 = row.getCell(113);
//                Cell cell_strExt5 = row.getCell(114);
//                Cell cell_strExt6 = row.getCell(115);
//                Cell cell_strExt7 = row.getCell(116);
//                Cell cell_strExt8 = row.getCell(117);
//
//                Cell cell_strConctactoIn = row.getCell(118);
//                Cell cell_strConmutador = row.getCell(120);
//                Cell cell_strMailIn = row.getCell(121);
//
//                Cell cell_strNoSoc1 = row.getCell(122);
//                Cell cell_strNoSoc2 = row.getCell(123);
//                Cell cell_strNoSoc3 = row.getCell(124);
//                Cell cell_strNoSoc4 = row.getCell(125);
//                Cell cell_strNoSoc5 = row.getCell(126);
//                Cell cell_strNoSoc6 = row.getCell(127);
//                Cell cell_strNoSoc7 = row.getCell(128);
//                Cell cell_strNoSoc8 = row.getCell(129);
//
//                Cell cell_strOrg1 = row.getCell(130);
//                Cell cell_strOrg2 = row.getCell(131);
//                Cell cell_strOrg3 = row.getCell(132);
//                Cell cell_strOrg4 = row.getCell(133);
//                Cell cell_strOrg5 = row.getCell(134);
//                Cell cell_strOrg6 = row.getCell(135);
//                Cell cell_strOrg7 = row.getCell(136);
//                Cell cell_strOrg8 = row.getCell(137);
//
//                Cell cell_strRazon2 = row.getCell(138);
//                Cell cell_strRazon3 = row.getCell(139);
//                Cell cell_strRazon4 = row.getCell(140);
//                Cell cell_strRazon5 = row.getCell(141);
//
//                Cell cell_intCteProsp = row.getCell(163);
//
//                Cell cell_strCell1 = row.getCell(164);
//                Cell cell_strCell2 = row.getCell(165);
//                Cell cell_strCell3 = row.getCell(166);
//                Cell cell_strCell4 = row.getCell(167);
//                Cell cell_strCell5 = row.getCell(168);
//                Cell cell_strCell6 = row.getCell(169);
//                Cell cell_strCell7 = row.getCell(170);
//                Cell cell_strCell8 = row.getCell(171);
//
//                System.out.println("test " + cell_strRazonSocial.getStringCellValue() + " " + cell_strRFC.getStringCellValue() + "\n");
//            }
//
//        } catch (IOException ex) {
//            System.out.println("Error: " + ex);
//        }
//        return this.strResultLast;
//    }
    public String ImpDDBB_XLS(String strPathexcel) {
        this.strResultLast = "OK";
        try {
            InputStream myxls = null;
            myxls = new FileInputStream(strPathexcel);
            HSSFWorkbook archivoPresupuestos = new HSSFWorkbook(myxls);
            Iterator<Row> it = archivoPresupuestos.getSheetAt(0).rowIterator();
            it.next();
            while (it.hasNext()) {
                Row row = it.next();

                Cell cell_intIdCte = row.getCell(1);
                Cell cell_strRazonSocial = row.getCell(2);
                Cell cell_intNoCte = row.getCell(3);
                Cell cell_strCp = row.getCell(9);

                Cell cell_strEmail1 = row.getCell(10);
                Cell cell_strEmail2 = row.getCell(11);
                Cell cell_strEmail3 = row.getCell(12);
                Cell cell_strEmail4 = row.getCell(13);
                Cell cell_strEmail5 = row.getCell(14);
                Cell cell_strEmail6 = row.getCell(15);
                Cell cell_strEmail7 = row.getCell(16);
                Cell cell_strEmail8 = row.getCell(17);

                Cell cell_strTelefono8 = row.getCell(19);
                Cell cell_strTelefono7 = row.getCell(20);
                Cell cell_strTelefono6 = row.getCell(21);
                Cell cell_strTelefono5 = row.getCell(22);
                Cell cell_strTelefono4 = row.getCell(23);
                Cell cell_strTelefono3 = row.getCell(24);
                Cell cell_strTelefono2 = row.getCell(25);
                Cell cell_strTelefono1 = row.getCell(26);

                Cell cell_strEdo = row.getCell(27);
                Cell cell_strDeleg = row.getCell(28);
                Cell cell_strCol = row.getCell(29);
                Cell cell_strCalle = row.getCell(30);

                Cell cell_strNomCont8 = row.getCell(32);
                Cell cell_strNomCont7 = row.getCell(33);
                Cell cell_strNomCont6 = row.getCell(34);
                Cell cell_strNomCont5 = row.getCell(35);
                Cell cell_strNomCont4 = row.getCell(36);
                Cell cell_strNomCont3 = row.getCell(37);
                Cell cell_strNomCont2 = row.getCell(38);
                Cell cell_strNomCont1 = row.getCell(39);

                Cell cell_strAp1 = row.getCell(40);
                Cell cell_strAp2 = row.getCell(41);
                Cell cell_strAp3 = row.getCell(42);
                Cell cell_strAp4 = row.getCell(43);
                Cell cell_strAp5 = row.getCell(44);
                Cell cell_strAp6 = row.getCell(45);
                Cell cell_strAp7 = row.getCell(46);
                Cell cell_strAp8 = row.getCell(47);

                Cell cell_strAm1 = row.getCell(48);
                Cell cell_strAm2 = row.getCell(49);
                Cell cell_strAm3 = row.getCell(50);
                Cell cell_strAm4 = row.getCell(51);
                Cell cell_strAm5 = row.getCell(52);
                Cell cell_strAm6 = row.getCell(53);
                Cell cell_strAm7 = row.getCell(54);
                Cell cell_strAm8 = row.getCell(55);

                Cell cell_strRFC = row.getCell(57);

                Cell cell_strTitle8 = row.getCell(58);
                Cell cell_strTitle7 = row.getCell(59);
                Cell cell_strTitle6 = row.getCell(60);
                Cell cell_strTitle5 = row.getCell(61);
                Cell cell_strTitle4 = row.getCell(62);
                Cell cell_strTitle3 = row.getCell(63);
                Cell cell_strTitle2 = row.getCell(64);
                Cell cell_strTitle1 = row.getCell(65);

                Cell cell_strArea8 = row.getCell(66);
                Cell cell_strArea7 = row.getCell(67);
                Cell cell_strArea6 = row.getCell(68);
                Cell cell_strArea5 = row.getCell(69);
                Cell cell_strArea4 = row.getCell(70);
                Cell cell_strArea3 = row.getCell(71);
                Cell cell_strArea2 = row.getCell(72);
                Cell cell_strArea1 = row.getCell(73);

                Cell cell_strGiro = row.getCell(74);

                Cell cell_strClaveDDBB = row.getCell(83);

                Cell cell_strSede = row.getCell(108);

                Cell cell_strExt1 = row.getCell(109);
                Cell cell_strExt2 = row.getCell(110);
                Cell cell_strExt3 = row.getCell(111);
                Cell cell_strExt4 = row.getCell(112);
                Cell cell_strExt5 = row.getCell(113);
                Cell cell_strExt6 = row.getCell(114);
                Cell cell_strExt7 = row.getCell(115);
                Cell cell_strExt8 = row.getCell(116);

                Cell cell_strConctactoIn = row.getCell(117);
                Cell cell_strConmutador = row.getCell(119);
                Cell cell_strMailIn = row.getCell(120);

                Cell cell_strNoSoc1 = row.getCell(121);
                Cell cell_strNoSoc2 = row.getCell(122);
                Cell cell_strNoSoc3 = row.getCell(123);
                Cell cell_strNoSoc4 = row.getCell(124);
                Cell cell_strNoSoc5 = row.getCell(125);
                Cell cell_strNoSoc6 = row.getCell(126);
                Cell cell_strNoSoc7 = row.getCell(127);
                Cell cell_strNoSoc8 = row.getCell(128);

                Cell cell_strOrg1 = row.getCell(129);
                Cell cell_strOrg2 = row.getCell(130);
                Cell cell_strOrg3 = row.getCell(131);
                Cell cell_strOrg4 = row.getCell(132);
                Cell cell_strOrg5 = row.getCell(133);
                Cell cell_strOrg6 = row.getCell(134);
                Cell cell_strOrg7 = row.getCell(135);
                Cell cell_strOrg8 = row.getCell(136);

                Cell cell_strRazon2 = row.getCell(137);
                Cell cell_strRazon3 = row.getCell(138);
                Cell cell_strRazon4 = row.getCell(139);
                Cell cell_strRazon5 = row.getCell(140);

                Cell cell_intCteProsp = row.getCell(162);

                Cell cell_strCell1 = row.getCell(163);
                Cell cell_strCell2 = row.getCell(164);
                Cell cell_strCell3 = row.getCell(165);
                Cell cell_strCell4 = row.getCell(166);
                Cell cell_strCell5 = row.getCell(167);
                Cell cell_strCell6 = row.getCell(168);
                Cell cell_strCell7 = row.getCell(169);
                Cell cell_strCell8 = row.getCell(170);
                //String strCPprueba = getstrCP(cell_strCp);
                String strCPprueba = getValorCelda(cell_strCp);
                System.out.println("razon " + cell_strRazonSocial.getStringCellValue() + " cp " + strCPprueba);
                //
            }
        } catch (IOException ex) {
            System.out.println("Error: " + ex);
        }
        return this.strResultLast;
    }

    public String getstrCP(Cell celda) {
        String strCP = "";
        if (celda.getCellType() == Cell.CELL_TYPE_BLANK) {
            strCP = "";
        } else if (celda.getCellType() == Cell.CELL_TYPE_STRING) {
            strCP = celda.getStringCellValue();
        } else if (celda.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            double dblCP = celda.getNumericCellValue();
            strCP = Double.toString(dblCP);
        }
        return strCP;
    }

    public String getValorCelda(Cell celda) {
        String strValor = "";
        if (celda != null) {
            switch (celda.getCellType()) {
                case Cell.CELL_TYPE_STRING:
                    strValor = celda.getStringCellValue();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    strValor = String.valueOf(celda.getNumericCellValue()).replace(".0", "");
                    break;
            }
        } else {
            strValor = "";
        }
        return strValor;
    }
}
