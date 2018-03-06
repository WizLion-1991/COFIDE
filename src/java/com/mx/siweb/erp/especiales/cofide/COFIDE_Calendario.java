/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.especiales.cofide;

import comSIWeb.Operaciones.Conexion;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Desarrollo_COFIDE
 */
public class COFIDE_Calendario {

    String strSql = "";
    ResultSet rs;
    Conexion oConn;

    public COFIDE_Calendario(Conexion oConn) {
        this.oConn = oConn;
    }

    /**
     * Obtener el numero de pagos del curso y pendientes
     *
     * @param strCursoId
     * @return numero de participantes pagados/no pagados
     */
    public int[] getPagos(String strCursoId) {
        int intPagado_P = 0;
        int intPendiente_P = 0;
        int intPagado_O = 0;
        int intPendiente_O = 0;
        int[] intLugares = new int[4];

        String strCompletaSQL = "";
        String strSql1 = "select (select FACD_CANTIDAD from vta_facturasdeta where vta_facturasdeta.FAC_ID = vta_facturas.FAC_ID) as pagados_pendientes "
                + "from vta_facturas "
                + "where FAC_ANULADA = 0 and FAC_CANCEL = 0 and CC_CURSO_ID = " + strCursoId + " ";
        String strSql2 = "select (select TKTD_CANTIDAD from vta_ticketsdeta where vta_ticketsdeta.TKT_ID = vta_tickets.TKT_ID) as pagados_pendientes "
                + "from vta_tickets "
                + "where TKT_ANULADA = 0 and TKT_CANCEL = 0 and CC_CURSO_ID = " + strCursoId + " ";
        try {
            //presencial pagado factura
            strCompletaSQL = strSql1 + "AND FAC_TIPO_CURSO = 1 and FAC_COFIDE_VALIDA = 1";
            rs = oConn.runQuery(strCompletaSQL, true);
            while (rs.next()) {
                intPagado_P += rs.getInt("pagados_pendientes");
            }
            rs.close();
            //presencial pagado ticket
            strCompletaSQL = strSql2 + "and TKT_TIPO_CURSO = 1 and TKT_COFIDE_VALIDA = 1";
            rs = oConn.runQuery(strCompletaSQL, true);
            while (rs.next()) {
                intPagado_P += rs.getInt("pagados_pendientes");
            }
            rs.close();

            //presencial pendiente factura
            strCompletaSQL = strSql1 + "AND FAC_TIPO_CURSO = 1 and FAC_COFIDE_VALIDA = 0";
            rs = oConn.runQuery(strCompletaSQL, true);
            while (rs.next()) {
                intPendiente_P += rs.getInt("pagados_pendientes");
            }
            rs.close();
            //presencial pendiente ticket
            strCompletaSQL = strSql2 + "and TKT_TIPO_CURSO = 1 and TKT_COFIDE_VALIDA = 0";
            rs = oConn.runQuery(strCompletaSQL, true);
            while (rs.next()) {
                intPendiente_P += rs.getInt("pagados_pendientes");
            }
            rs.close();

            //en línea pagado factura
            strCompletaSQL = strSql1 + "AND FAC_TIPO_CURSO = 2 and FAC_COFIDE_VALIDA = 1";
            rs = oConn.runQuery(strCompletaSQL, true);
            while (rs.next()) {
                intPagado_O += rs.getInt("pagados_pendientes");
            }
            rs.close();
            //en línea pagado ticket
            strCompletaSQL = strSql2 + "and TKT_TIPO_CURSO = 2 and TKT_COFIDE_VALIDA = 1";
            rs = oConn.runQuery(strCompletaSQL, true);
            while (rs.next()) {
                intPendiente_O += rs.getInt("pagados_pendientes");
            }
            rs.close();
            //en línea pendiente factura
            strCompletaSQL = strSql1 + "AND FAC_TIPO_CURSO = 2 and FAC_COFIDE_VALIDA = 0";
            rs = oConn.runQuery(strCompletaSQL, true);
            while (rs.next()) {
                intPagado_O += rs.getInt("pagados_pendientes");
            }
            rs.close();
            //en línea pendiente ticket
            strCompletaSQL = strSql2 + "and TKT_TIPO_CURSO = 2 and TKT_COFIDE_VALIDA = 0";
            rs = oConn.runQuery(strCompletaSQL, true);
            while (rs.next()) {
                intPendiente_O += rs.getInt("pagados_pendientes");
            }
            rs.close();
        } catch (SQLException sql) {
            System.out.println("ERROR: hubó un problema al obtener le numero de registros...  " + sql.getMessage());
        }
        intLugares[0] = intPagado_P;
        intLugares[1] = intPendiente_P;
        intLugares[2] = intPagado_O;
        intLugares[3] = intPendiente_O;
        return intLugares;
    }

    /**
     * obtiene el numero de lugares vendidos, directo de facturas y tickets
     *
     * @param strIdCurso id del curso a obtener ventas
     * @param intOpc opción de consulta: 1 presencial / 2 en línea
     * @return arreglo con la cantidad de lugares vendidos
     */
    public int[] getintVenta(String strIdCurso) {

        int[] intVendido = new int[6];
        int intPresencial = 0;
        int intOnLine = 0;
        int inPagadosPresencial = 0;
        int inPagadosOnLine = 0;
        int inPendientePresencial = 0;
        int inPendienteOnLine = 0;

        String strSqlComplete = "";

        String strSql2 = "select v.FAC_ID, v.TIPO_DOC, vd.FACD_CANTIDAD, vd.PR_ID as IDCURSO, v.FAC_ANULADA , v.FAC_PAGADO, vd.FACD_TIPO_CURSO, v.ID_MASTER "
                + "from view_ventasglobalesdeta as vd, view_ventasglobales as v "
                + "where vd.FAC_ID = v.FAC_ID and vd.TIPO_DOC = v.TIPO_DOC "
                + "and FAC_ANULADA = 0 and CANCEL = 0 and PR_ID = " + strIdCurso
                + " order by v.TIPO_DOC, v.FAC_ID";
        try {
            rs = oConn.runQuery(strSql2, true);
            while (rs.next()) {
                //PRESENCIALES
                if (rs.getInt("FACD_TIPO_CURSO") == 1) {
                    intPresencial += rs.getInt("FACD_CANTIDAD");
                    if (rs.getInt("FAC_PAGADO") == 1) {
                        inPagadosPresencial += rs.getInt("FACD_CANTIDAD");
                    } else {
                        inPendientePresencial += rs.getInt("FACD_CANTIDAD");
                    }
                }
                //EN LINEA
                if (rs.getInt("FACD_TIPO_CURSO") == 2) {
                    intOnLine += rs.getInt("FACD_CANTIDAD");
                    if (rs.getInt("FAC_PAGADO") == 1) {
                        inPagadosOnLine += rs.getInt("FACD_CANTIDAD");
                    } else {
                        inPendienteOnLine += rs.getInt("FACD_CANTIDAD");
                    }
                }
            }
            rs.close();
        } catch (SQLException sql) {
            System.out.println("Error al obtener el numero de lugares vendidos... " + sql.getMessage());
        }

        intVendido[0] = intPresencial;
        intVendido[1] = intOnLine;
        intVendido[2] = inPagadosPresencial;
        intVendido[3] = inPagadosOnLine;
        intVendido[4] = inPendientePresencial;
        intVendido[5] = inPendienteOnLine;

        return intVendido;
    }

    /**
     * obtener la duración de horas del curso en total
     *
     * @param strHr1
     * @param strHr2
     * @param strHr3
     * @return
     */
    public String strGetDuracion(String strHr1, String strHr2, String strHr3) {

        String[] strH1 = new String[2];
        String[] strH2 = new String[2];
        String[] strH3 = new String[2];
        String strDuracionHrs = "";
        int intDuracionHrs = 0;
        int intDuracionMins = 0;
        double dblhrs = 0;
        double dblminutos = 0;

        if (!strHr1.equals("")) {
            if (strHr1.contains(":")) {
                intDuracionMins += 30;
                strH1 = strHr1.split(":");
                intDuracionHrs += Integer.parseInt(strH1[0]);
            } else {
                intDuracionHrs += Integer.parseInt(strHr1);
            }
        }
        if (!strHr2.equals("")) {
            if (strHr2.contains(":")) {
                intDuracionMins += 30;
                strH2 = strHr2.split(":");
                intDuracionHrs += Integer.parseInt(strH2[0]);
            } else {
                intDuracionHrs += Integer.parseInt(strHr2);
            }
        }
        if (!strHr3.equals("")) {
            if (strHr3.contains(":")) {
                intDuracionMins += 30;
                strH3 = strHr3.split(":");
                intDuracionHrs += Integer.parseInt(strH3[0]);
            } else {
                intDuracionHrs += Integer.parseInt(strHr3);
            }
        }

        if (intDuracionMins > 30) {
            dblhrs = intDuracionMins / 60;
            dblminutos = intDuracionMins % 60;
            intDuracionHrs += (int) dblhrs;
            intDuracionMins = (int) dblminutos;
        }
        if (intDuracionMins > 0) {
            strDuracionHrs = intDuracionHrs + ":" + intDuracionMins;
        } else {
            strDuracionHrs = String.valueOf(intDuracionHrs);
        }

        return strDuracionHrs;
    }
}
