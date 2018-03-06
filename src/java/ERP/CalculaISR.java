/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ERP;

import Tablas.Rhh_Nominas_Master;
import Tablas.rhh_empleados;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author siweb
 */
public class CalculaISR {

    // <editor-fold defaultstate="collapsed" desc="Propiedades">
    Conexion oConn;
    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(CalculaISR.class.getName());
    private rhh_empleados empleadoActual;
    private Rhh_Nominas_Master nominaActual;
    private double dblImporteISR = 0;
    private double dblretencion = 0;
    private double dblGravados = 0.0;
    private double dblDiasMes = 0;
    private boolean isWWPG;
    VariableSession varSesiones;

    public double getDblDiasMes() {
        return dblDiasMes;
    }

    public void setDblDiasMes(double dblDiasMes) {
        this.dblDiasMes = dblDiasMes;
    }

    public boolean isIsWWPG() {
        return isWWPG;
    }

    public void setIsWWPG(boolean isWWPG) {
        this.isWWPG = isWWPG;
    }

    public rhh_empleados getEmpleadoActual() {
        return empleadoActual;
    }

    public void setDblGravados(double dblGravados) {
        this.dblGravados = dblGravados;
    }

    public Rhh_Nominas_Master getNominaActual() {
        return nominaActual;
    }

    public void setEmpleadoActual(rhh_empleados empleadoActual) {
        this.empleadoActual = empleadoActual;
    }

    public void setNominaActual(Rhh_Nominas_Master nominaActual) {
        this.nominaActual = nominaActual;
    }

    public double getDblImporteISR() {
        return dblImporteISR;
    }

    public double getDblGravados() {
        return dblGravados;
    }

    public void setDblImporteISR(double dblImporteISR) {
        this.dblImporteISR = dblImporteISR;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructor">
    public CalculaISR(Conexion oConn, VariableSession varSesiones) {
        this.oConn = oConn;
        this.varSesiones = varSesiones;
        this.isWWPG = false;
    }
    // </editor-fold>

    public double CalculaISR(rhh_empleados empleadoActual, Rhh_Nominas_Master nominaActual) {
        this.empleadoActual = empleadoActual;
        this.nominaActual = nominaActual;
        String strPeriodicidad = this.empleadoActual.getFieldString("EMP_PERIODICIDAD_PAGO");
        dblImporteISR = calculaImporte(strPeriodicidad);
        return dblImporteISR;
    }

    public double calculaImporte(String strPeriodicidad) {
        String strQuery = "";
        ResultSet rs = null;
        double dblSueldoNomina = getDblGravados();
        double dblImporte = 0;
        int opcion = 0;
        String strTipoNomina = strPeriodicidad;
        if (strTipoNomina.toUpperCase().equals("SEMANAL")) {
            opcion = 1;
        } else {
            if (strTipoNomina.toUpperCase().equals("QUINCENAL")) {
                opcion = 2;
            } else {
                if (strTipoNomina.toUpperCase().equals("MENSUAL")) {
                    opcion = 3;
                } else {
                    if (strTipoNomina.toUpperCase().equals("ANUAL")) {
                        opcion = 4;
                    }
                }
            }
        }
        switch (opcion) {
            case 1:
                strQuery = "select * from rhh_tari_semanal";
                try {
                    rs = oConn.runQuery(strQuery);
                    while (rs.next()) {
                        if (dblSueldoNomina >= rs.getDouble("TARSEM_LIM_INF") && dblSueldoNomina <= rs.getDouble("TARSEM_LIM_SUP")) {
                            dblImporte = dblSueldoNomina - rs.getDouble("TARSEM_LIM_INF");
                            double ImpPorciento = dblImporte * (rs.getDouble("TARSEM_PORCIENTO") / 100);
                            double impoCuota = ImpPorciento + rs.getDouble("TARSEM_CUOTA_FIJA");
                            dblImporteISR = CalculaSubsido(1, dblSueldoNomina, impoCuota);
                        }//Fin IF
                    }//Fin WHILE
                    rs.close();
                } catch (SQLException ex) {
                    log.error(ex.getMessage());
                }
                break;
            case 2:
                strQuery = "select * from rhh_tari_quincenal";
                try {
                    rs = oConn.runQuery(strQuery);
                    while (rs.next()) {
                        if (dblSueldoNomina >= rs.getDouble("TARQUIN_LIM_INF") && dblSueldoNomina <= rs.getDouble("TARQUIN_LIM_SUP")) {
                            dblImporte = dblSueldoNomina - rs.getDouble("TARQUIN_LIM_INF");
                            double ImpPorciento = dblImporte * (rs.getDouble("TARQUIN_PORCIENTO") / 100);
                            double impoCuota = ImpPorciento + rs.getDouble("TARQUIN_CUOTA_FIJA");
                            dblImporteISR = CalculaSubsido(2, dblSueldoNomina, impoCuota);
                        }//Fin IF
                    }//Fin WHILE
                    rs.close();
                } catch (SQLException ex) {
                    log.error(ex.getMessage());
                }
                break;
            case 3:
                strQuery = "select * from rhh_tari_mensual";
                try {
                    rs = oConn.runQuery(strQuery);
                    while (rs.next()) {
                        if (dblSueldoNomina >= rs.getDouble("TARMEN_LIM_INF") && dblSueldoNomina <= rs.getDouble("TARMEN_LIM_SUP")) {
                            dblImporte = dblSueldoNomina - rs.getDouble("TARMEN_LIM_INF");
                            double ImpPorciento = dblImporte * (rs.getDouble("TARMEN_PORCIENTO") / 100);
                            double impoCuota = ImpPorciento + rs.getDouble("TARMEN_CUOTA_FIJA");
                            dblImporteISR = CalculaSubsido(3, dblSueldoNomina, impoCuota);
                        }//Fin IF
                    }//Fin WHILE
                    rs.close();
                } catch (SQLException ex) {
                    log.error(ex.getMessage());
                }
                break;
            case 4:
                strQuery = "select * from rhh_tari_anual";
                try {
                    rs = oConn.runQuery(strQuery);
                    while (rs.next()) {
                        if (dblSueldoNomina >= rs.getDouble("TARANU_LIM_INF") && dblSueldoNomina <= rs.getDouble("TARANU_LIM_SUP")) {
                            dblImporte = dblSueldoNomina - rs.getDouble("TARANU_LIM_INF");
                            double ImpPorciento = dblImporte * rs.getDouble("TARANU_PORCIENTO");
                            double impoCuota = ImpPorciento + rs.getDouble("TARANU_CUOTA_FIJA");
                            dblImporteISR = impoCuota;
                        }//Fin IF
                    }//Fin WHILE
                    rs.close();
                } catch (SQLException ex) {
                    log.error(ex.getMessage());
                }
                break;
        }
        return dblImporteISR;
    }//Fin double calcula Importe

    public double CalculaSubsido(int intTipoNom, double dblSueldoNomina, double Importe) {
        String strQuery = "";
        ResultSet rs = null;
        double dblSubsidio = 0;
        double dblSubsidioTotal = 0;
        switch (intTipoNom) {
            case 1:
                strQuery = "select * from rhh_tari_subsem";
                try {
                    rs = oConn.runQuery(strQuery);
                    while (rs.next()) {
                        if (dblSueldoNomina >= rs.getDouble("TARSUBSEM_PARA") && dblSueldoNomina <= rs.getDouble("TARSUBSEM_HASTA")) {
                                dblSubsidio = rs.getDouble("TARSUBSEM_CANTIDAD");
                                dblSubsidioTotal = Importe - dblSubsidio;
                        }//Fin IF
                    }//Fin WHILE
                    rs.close();
                } catch (SQLException ex) {
                    log.error(ex.getMessage());
                }
                break;
            case 2:
                strQuery = "select * from rhh_tari_subquin";
                try {
                    rs = oConn.runQuery(strQuery);
                    while (rs.next()) {
                        if (dblSueldoNomina >= rs.getDouble("TARSUBQUIN_PARA") && dblSueldoNomina <= rs.getDouble("TARSUBQUIN_HASTA")) {
                            dblSubsidio = rs.getDouble("TARSUBQUIN_CANTIDAD");
                            dblSubsidioTotal = Importe - dblSubsidio;
                        }//Fin IF
                    }//Fin WHILE
                    rs.close();
                } catch (SQLException ex) {
                    log.error(ex.getMessage());
                }
                break;
            case 3:
                strQuery = "select * from rhh_tari_submen";
                try {
                    rs = oConn.runQuery(strQuery);
                    while (rs.next()) {
                        if (dblSueldoNomina >= rs.getDouble("TARSUBMEN_PARA") && dblSueldoNomina <= rs.getDouble("TARSUBMEN_HASTA")) {
                            if (isIsWWPG()) {
                                double tmpCantidad = rs.getDouble("TARSUBMEN_CANTIDAD");
                                dblSubsidio = (tmpCantidad / 30) * getDblDiasMes();
                                dblSubsidioTotal = Importe - dblSubsidio;
                            } else {
                                dblSubsidio = rs.getDouble("TARSUBMEN_CANTIDAD");
                                dblSubsidioTotal = Importe - dblSubsidio;
                            }
                        }//Fin IF
                    }//Fin WHILE
                    rs.close();
                } catch (SQLException ex) {
                    log.error(ex.getMessage());
                }
                break;
        }
        return dblSubsidioTotal;
    }//Fin CALCULO SUBCIDIO
}//Fin Clase Calcula ISR

