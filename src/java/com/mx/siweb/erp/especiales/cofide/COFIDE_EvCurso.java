/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.especiales.cofide;

import comSIWeb.Operaciones.Conexion;
import comSIWeb.Utilerias.Fechas;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Desarrollo_COFIDE
 */
public class COFIDE_EvCurso {

    String strSql = "";
    ResultSet rs;
    Conexion oConn;
    Fechas fec = new Fechas();

    public COFIDE_EvCurso(Conexion oConn) {
        this.oConn = oConn;
    }

    /**
     *
     * @param strBase base del ejecutivo
     * @return numero de registros 0/prospectos , 1/ exparticipantes, 2/ total
     * de registros
     */
    public int[] getRegistros(String strBase) {
        int[] intCuantos = new int[3];
        int intCuantosP = 0;
        int intCuantosE = 0;
        int intCuantosTotal = 0;
        String strSqlComplete = "";
        strSql = "select CT_ID, CT_ES_PROSPECTO from vta_cliente where CT_ACTIVO = 1 and CT_CLAVE_DDBB = '" + strBase + "' ";

        try {
            //conteo total
            strSqlComplete = strSql;
            rs = oConn.runQuery(strSqlComplete, true);
            while (rs.next()) {
                intCuantosTotal++;
                if (rs.getInt("CT_ES_PROSPECTO") == 0) {
                    intCuantosE++;
                } else {
                    intCuantosP++;
                }
            }
            rs.close();
        } catch (SQLException sql) {
            System.out.println("Error al obtener el conteo de registros: " + sql.getMessage());
        }
        intCuantos[0] = intCuantosP;
        intCuantos[1] = intCuantosE;
        intCuantos[2] = intCuantosTotal;
        return intCuantos;
    }

    /**
     *
     * @param intEjecutovo id del ejecutivo
     * @return el numero de llamadas
     */
    public int getLlamada(int intEjecutovo) {
        int intCuantos = 0;
        strSql = "select CL_ID_CLIENTE  from cofide_llamada where CL_FECHA = '" + fec.getFechaActual() + "' and CL_COMPLETO = 1 and CL_USUARIO = " + intEjecutovo;
        try {
            rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                intCuantos++;
            }
            rs.close();
        } catch (SQLException sql) {
            System.out.println(" Error al contabilizar las llamadas del ejecutivo: " + sql.getMessage());
        }
        return intCuantos;
    }

    /**
     * OBTENER EL MONTO DE VENTA GENERAL
     *
     * @param intIdUser
     * @return
     */
    public double getReserva(int intIdUser) {
        double dblReserva = 0.0;
        String strSql = "";
        ResultSet rs;
        //Cuantas ventas tickets y facturas en total y reservaciones
//        System.out.println("############################### getReservado()");
        strSql = "select FAC_TOTAL, FAC_IMPUESTO1 from view_ventasglobales "
                + "where FAC_ANULADA = 0 and left(FAC_FECHA,6) = '" + fec.getFechaActual().substring(0, 6) + "' "
                + "and FAC_US_ALTA = " + intIdUser + " AND CANCEL = 0 AND FAC_PROMO = 0";
        try {
            rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
//                dblReserva += rs.getDouble("TOT");
                dblReserva += (rs.getDouble("FAC_TOTAL") - rs.getDouble("FAC_IMPUESTO1"));
            }
            rs.close();
        } catch (SQLException ex) {
            System.out.println("Error: " + ex);
        }
        return dblReserva;
    }

    public double getCobro(int intIdUser) {
        double dblCobro = 0;
        String strSql = "";
        ResultSet rs;
        try {
            //Cuantos Cobrado
//            System.out.println("############################### getCobro()");
            strSql = "select FAC_TOTAL, FAC_IMPUESTO1 from view_ventasglobales "
                    + "where FAC_ANULADA = 0 and FAC_PAGADO = 1 AND FAC_COFIDE_VALIDA = 1 "
                    + "and left(FAC_FECHA_COBRO,6) = '" + fec.getFechaActual().substring(0, 6) + "' "
                    + "and FAC_US_ALTA = " + intIdUser + " and FAC_PROMO = 0 AND CANCEL = 0 AND FAC_ES_RESERVACION = 0";
            rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
//                dblCobro += rs.getDouble("COBRADO");
                dblCobro += (rs.getDouble("FAC_TOTAL") - rs.getDouble("FAC_IMPUESTO1"));;
            }
            rs.close();

        } catch (SQLException ex) {
            System.out.println("Error: " + ex);
        }
        return dblCobro;
    }

    public double getMeta(int intIdUser) {
        double dblMeta = 0;
        String strSql = "";
        ResultSet rs;
        try {
            //meta montos, nuevos participantes
            strSql = "select CMU_IMPORTE from cofide_metas_usuario where id_usuarios = " + intIdUser;
            rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                dblMeta = rs.getDouble("CMU_IMPORTE");
            }
            rs.close();
        } catch (SQLException ex) {
            System.out.println("Error: " + ex);
        }
        return dblMeta;
    }

    public double getFacturado(int intIdUser) {
        double dblFactura = 0;
        String strSql = "";
        ResultSet rs;
        try {
//            System.out.println("############################### getFacturado()");
            strSql = "select FAC_IMPORTE, FAC_TOTAL, FAC_IMPUESTO1 from view_ventasglobales "
                    + "where TIPO_DOC = 'F' and FAC_ANULADA = 0 "
                    + "AND left(FAC_FECHA,6) = '" + fec.getFechaActual().substring(0, 6) + "' AND FAC_US_ALTA = " + intIdUser
                    + " AND FAC_PROMO = 0 AND CANCEL = 0";
            rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
//                dblFactura += rs.getDouble("FACTURADO");
                dblFactura += (rs.getDouble("FAC_TOTAL") - rs.getDouble("FAC_IMPUESTO1"));
            }
            rs.close();
        } catch (SQLException ex) {
            System.out.println("Error: " + ex);
        }
        return dblFactura;
    }

    public double[] getFacturado_Vendido(int intIdUser) {
        double dblFactura = 0;
        double dblVendido = 0;
        double[] dblRespuesta = new double[2];
        String strSql = "";
        ResultSet rs;
        try {
//            System.out.println("############################### getFacturado() y Vendido");
            strSql = "select FAC_TOTAL, FAC_IMPUESTO1,TIPO_DOC "
                    + "from view_ventasglobales "
                    + "where FAC_ANULADA = 0 "
                    + "AND left(FAC_FECHA,6) = '" + fec.getFechaActual().substring(0, 6) + "' "
                    + "AND FAC_US_ALTA = " + intIdUser + " "
                    + "AND FAC_PROMO = 0 "
                    + "AND CANCEL = 0";
            rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                if (rs.getString("TIPO_DOC").equals("F")) {
                    dblFactura += (rs.getDouble("FAC_TOTAL") - rs.getDouble("FAC_IMPUESTO1"));
                }
                dblVendido += (rs.getDouble("FAC_TOTAL") - rs.getDouble("FAC_IMPUESTO1"));
            }
            rs.close();
        } catch (SQLException ex) {
            System.out.println("Error: " + ex);
        }
        dblRespuesta[0] = dblFactura;
        dblRespuesta[1] = dblVendido;
        return dblRespuesta;
    }

    public int getNotas(int intIdUser) {
        int intNotas = 0;
        String strSql = "select EV_CT_ID  from crm_eventos where EV_ESTADO = 1 and EV_FECHA_INICIO < " + fec.getFechaActual() + " and EV_ASIGNADO_A = " + intIdUser;
        try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                intNotas++;
            }
            rs.close();
        } catch (SQLException ex) {
            System.out.println("Error: " + ex);
        }
        return intNotas;
    }
    
}
