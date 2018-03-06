/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.reportes;

import ERP.BusinessEntities.PercepcionesDeduccionesE;
import ERP.PercepcionesDeducciones;
import com.mx.siweb.erp.reportes.entities.RepoPreNominaE;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Utilerias.UtilXml;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author siweb
 */
public class RepoPreNomina {

    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(RepoPreNomina.class.getName());
    private ArrayList<RepoPreNominaE> entidades;
    private Conexion oConn;
    private int intTipoNom;
    private boolean bolImpreso;
    private ResultSet rs2;
    private String emp_nom;
    private String emp_rfc;

    private final int emp_id;

    public RepoPreNomina(Conexion oConn, VariableSession varSesiones, int intTipoNom) {
        this.oConn = oConn;
        this.intTipoNom = intTipoNom;
        this.emp_id = varSesiones.getIntIdEmpresa();

        this.entidades = new ArrayList<RepoPreNominaE>();

    }

    public void RepoPreNomina_HacerReporte() {

        log.debug("///////////////////////////  COMENZAMOS A CREAR EL REPORTE /////////////////////////////////////");
        /*Es solamente un numero consecutivo para el campo ConsNom*/
        int intConsNom = 0;

        /*Aqui obtenemos los Id,clave de las percepciones y deducciones */
        PercepcionesDeducciones PercDedu = new PercepcionesDeducciones(oConn, emp_id);
        Map<String, PercepcionesDeduccionesE> HashMapPercepciones = PercDedu.ObtenPercepciones();
        Map<String, PercepcionesDeduccionesE> HashMapDeducciones = PercDedu.ObtenDeducciones();

        /* Primer ciclo Obtenemos datos de las tablas rhh_nominas_master,rhh_nominas,rhh_empleados*/
        String strSql = "select rhh_nominas.EMP_NUM,rhh_nominas.NOM_ID,rhh_nominas_master.RHN_NUMERO_DIAS,rhh_nominas.NOM_PERCEPCION_TOTAL,rhh_empleados.EMP_CLABE,rhh_empleados.EMP_NOMBRE,rhh_empleados.EMP_SALARIO_DIARIO,rhh_empleados.EMP_SALARIO_INTEGRADO,rhh_nominas.NOM_PERCEPCIONES,rhh_nominas.NOM_DEDUCCIONES,rhh_nominas.NOM_FOLIO,"
                + "(select DP_DESCRIPCION from rhh_departamento where rhh_empleados.DP_ID = rhh_departamento.DP_ID) as centroDeCostos"
                + " from rhh_nominas_master,rhh_nominas,rhh_empleados"
                + " where rhh_nominas_master.RHN_ID = rhh_nominas.RHN_ID "
                + " and rhh_nominas.EMP_NUM = rhh_empleados.EMP_NUM"
                + " and rhh_nominas_master.RHN_ID = " + intTipoNom;
        ResultSet rs;
        try {
            rs = oConn.runQuery(strSql, true);
            while (rs.next()) {

                int intNom_ID = rs.getInt("NOM_ID");
                int intEmp_NUM = rs.getInt("EMP_NUM");
                log.debug("///////////////////////////  Nomina = " + intNom_ID + " /////////////////////////////////////");

                ArrayList<Integer> arrPercepciones = new ArrayList<Integer>();
                ArrayList<Integer> arrDeducciones = new ArrayList<Integer>();

                RepoPreNominaE e = new RepoPreNominaE();

                e.setDiasTrabajados(rs.getString("RHN_NUMERO_DIAS"));
                e.setTotalPercepciones(rs.getInt("NOM_PERCEPCIONES"));
                e.setTotalDeducciones(rs.getInt("NOM_DEDUCCIONES"));
                e.setNetoApagar(rs.getInt("NOM_PERCEPCION_TOTAL"));
                e.setClave(rs.getString("EMP_CLABE"));
                e.setNombre(rs.getString("EMP_NOMBRE"));
                e.setSdi(rs.getInt("EMP_SALARIO_DIARIO"));
                e.setSdn(rs.getInt("EMP_SALARIO_INTEGRADO"));
                intConsNom++;
                e.setConsNom(String.valueOf(intConsNom));
                e.setCentroDeCostos(rs.getString("centroDeCostos"));
                e.setNoDeRecibido(rs.getInt("NOM_FOLIO"));

                log.debug("ID Nomina=" + intNom_ID);
                log.debug("NUM Empleado=" + intEmp_NUM);

                /*Obtenemos Los Id de las percepciones de la nomia actual y las guardamos en un arrayList*/
                String strSql1 = "select DISTINCT PERC_ID"
                        + " from rhh_nominas_deta"
                        + " where NOM_ID =" + intNom_ID;
                ResultSet rs1;
                try {
                    rs1 = oConn.runQuery(strSql1, true);
                    while (rs1.next()) {
                        if (rs1.getInt("PERC_ID") != 0) {
                            arrPercepciones.add(rs1.getInt("PERC_ID"));
                        }
                    }
                    rs1.close();
                } catch (SQLException ex) {
                    log.error(ex);
                }
                /*Obtenemos Los Id de las deducciones de la nomia actual y las guardamos en un arrayList*/
                String strSql2 = "select DISTINCT DEDU_ID"
                        + " from rhh_nominas_deta"
                        + " where NOM_ID =" + intNom_ID;
                ResultSet rs2;
                try {
                    rs2 = oConn.runQuery(strSql2, true);
                    while (rs2.next()) {
                        if (rs2.getInt("DEDU_ID") != 0) {
                            arrDeducciones.add(rs2.getInt("DEDU_ID"));
                        }
                    }
                    rs2.close();
                } catch (SQLException ex) {
                    log.error(ex);
                }

                log.debug("Numero de Percepciones=" + arrPercepciones.size());
                log.debug("Numero de Deducciones=" + arrDeducciones.size());

                log.debug("-----------------------------------------Percepciones de la nomina");
                /*En este ciclo obtenemos los datos de nomina_deta Percepciones*/
                for (int i = 0; i < arrPercepciones.size(); i++) {
                    String strSql3 = "select NOMD_UNITARIO"
                            + " from rhh_nominas_deta"
                            + " where NOM_ID =" + intNom_ID + " and PERC_ID= " + arrPercepciones.get(i) + " and NOMD_GRAVADO=0";
                    ResultSet rs3;
                    try {
                        rs3 = oConn.runQuery(strSql3, true);
                        while (rs3.next()) {

                            if (arrPercepciones.get(i) == HashMapPercepciones.get("1").getIntIdPercDedu()) {
                                e.setSueldo(Double.toString(rs3.getDouble("NOMD_UNITARIO")));
                                log.debug("Sueldos, Salarios Rayas y Jornales " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("1").getIntIdPercDedu());
                            }
                            if (arrPercepciones.get(i) == HashMapPercepciones.get("2").getIntIdPercDedu()) {
                                e.setAguinaldo(rs3.getDouble("NOMD_UNITARIO"));
                                log.debug("Gratificación Anual (Aguinaldo) " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("2").getIntIdPercDedu());
                            }
                            if (arrPercepciones.get(i) == HashMapPercepciones.get("3").getIntIdPercDedu()) {
                                e.setPtu(rs3.getDouble("NOMD_UNITARIO"));
                                log.debug("Participación de los Trabajadores en las Utilidades PTU " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("3").getIntIdPercDedu());
                            }
                            if (arrPercepciones.get(i) == HashMapPercepciones.get("4").getIntIdPercDedu()) {
                                e.setReembolsoGastosMedicosDentHosp(rs3.getDouble("NOMD_UNITARIO"));
                                log.debug("Reembolso de Gastos Médicos Dentales y Hospitalarios " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("4").getIntIdPercDedu());
                            }
                            if (arrPercepciones.get(i) == HashMapPercepciones.get("5").getIntIdPercDedu()) {
                                e.setAportacionFondoDeAhorro(rs3.getDouble("NOMD_UNITARIO"));
                                log.debug("Fondo de Ahorro " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("5").getIntIdPercDedu());
                            }
                            if (arrPercepciones.get(i) == HashMapPercepciones.get("6").getIntIdPercDedu()) {
                                e.setCajaAhorro(rs3.getDouble("NOMD_UNITARIO"));
                                log.debug("Caja de ahorro " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("6").getIntIdPercDedu());
                            }
                            if (arrPercepciones.get(i) == HashMapPercepciones.get("7").getIntIdPercDedu()) {
                                e.setVales(rs3.getDouble("NOMD_UNITARIO"));
                                log.debug("Vales " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("7").getIntIdPercDedu());
                            }
                            if (arrPercepciones.get(i) == HashMapPercepciones.get("8").getIntIdPercDedu()) {
                                e.setAyudas(rs3.getDouble("NOMD_UNITARIO"));
                                log.debug("Ayudas " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("8").getIntIdPercDedu());
                            }
                            if (arrPercepciones.get(i) == HashMapPercepciones.get("9").getIntIdPercDedu()) {
                                e.setContribucuionesTrabajadorPagadasPatron(rs3.getDouble("NOMD_UNITARIO"));
                                log.debug("Contribuciones a Cargo del Trabajador Pagadas por el Patrón " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("9").getIntIdPercDedu());
                            }
                            if (arrPercepciones.get(i) == HashMapPercepciones.get("30").getIntIdPercDedu()) {
                                e.setVacaciones(rs3.getDouble("NOMD_UNITARIO"));
                                log.debug("Vacaciones " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("30").getIntIdPercDedu());
                            }
                            if (arrPercepciones.get(i) == HashMapPercepciones.get("11").getIntIdPercDedu()) {
                                e.setPrimaSeguroVida(rs3.getDouble("NOMD_UNITARIO"));
                                log.debug("Prima de Seguro de vida " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("11").getIntIdPercDedu());
                            }
                            if (arrPercepciones.get(i) == HashMapPercepciones.get("12").getIntIdPercDedu()) {
                                e.setSeguroGastosMedicosMayores(rs3.getDouble("NOMD_UNITARIO"));
                                log.debug("Seguro de Gastos Medicos Mayores " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("12").getIntIdPercDedu());
                            }
                            if (arrPercepciones.get(i) == HashMapPercepciones.get("13").getIntIdPercDedu()) {
                                e.setCuotasSindicalesPagadasPatron(rs3.getDouble("NOMD_UNITARIO"));
                                log.debug("Cuotas Sindicales Pagadas por el Patrón " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("13").getIntIdPercDedu());
                            }
                            if (arrPercepciones.get(i) == HashMapPercepciones.get("14").getIntIdPercDedu()) {
                                e.setSubsidioIncapacidad(rs3.getDouble("NOMD_UNITARIO"));
                                log.debug("Subsidios por incapacidad " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("14").getIntIdPercDedu());
                            }
                            if (arrPercepciones.get(i) == HashMapPercepciones.get("15").getIntIdPercDedu()) {
                                e.setBecasTrabajadoresHijos(rs3.getDouble("NOMD_UNITARIO"));
                                log.debug("Becas para trabajadores y/o hijos " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("15").getIntIdPercDedu());
                            }
                            if (arrPercepciones.get(i) == HashMapPercepciones.get("16").getIntIdPercDedu()) {
                                e.setOtros(rs3.getDouble("NOMD_UNITARIO"));
                                log.debug("Otros " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("Otros").getIntIdPercDedu());
                            }
                            if (arrPercepciones.get(i) == HashMapPercepciones.get("17").getIntIdPercDedu()) {
                                e.setSubsidioAlEmpleo(rs3.getDouble("NOMD_UNITARIO"));
                                log.debug("Subsidio para el empleo " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("17").getIntIdPercDedu());
                            }
                            if (arrPercepciones.get(i) == HashMapPercepciones.get("18").getIntIdPercDedu()) {
                                e.setFomentoPrimerEmpleo(rs3.getDouble("NOMD_UNITARIO"));
                                log.debug("Fomento al primer empleo " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("18").getIntIdPercDedu());
                            }
                            if (arrPercepciones.get(i) == HashMapPercepciones.get("19").getIntIdPercDedu()) {
                                e.setHorasExtras(rs3.getDouble("NOMD_UNITARIO"));
                                log.debug("Horas extra " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("19").getIntIdPercDedu());
                            }
                            if (arrPercepciones.get(i) == HashMapPercepciones.get("20").getIntIdPercDedu()) {
                                e.setPrimaDominical(rs3.getDouble("NOMD_UNITARIO"));
                                log.debug("Prima dominical " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("20").getIntIdPercDedu());
                            }
                            if (arrPercepciones.get(i) == HashMapPercepciones.get("21").getIntIdPercDedu()) {
                                e.setPrimaVacacional(rs3.getDouble("NOMD_UNITARIO"));
                                log.debug("Prima vacacional " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("21").getIntIdPercDedu());
                            }
                            if (arrPercepciones.get(i) == HashMapPercepciones.get("22").getIntIdPercDedu()) {
                                e.setPrimaAntiguedad(rs3.getDouble("NOMD_UNITARIO"));
                                log.debug("Prima por antigüedad " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("22").getIntIdPercDedu());
                            }
                            if (arrPercepciones.get(i) == HashMapPercepciones.get("23").getIntIdPercDedu()) {
                                e.setPagoSeparacion(rs3.getDouble("NOMD_UNITARIO"));
                                log.debug("Pagos por separación " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("23").getIntIdPercDedu());
                            }
                            if (arrPercepciones.get(i) == HashMapPercepciones.get("24").getIntIdPercDedu()) {
                                e.setSeguroRetiro(rs3.getDouble("NOMD_UNITARIO"));
                                log.debug("Seguro de retiro " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("24").getIntIdPercDedu());
                            }
                            if (arrPercepciones.get(i) == HashMapPercepciones.get("25").getIntIdPercDedu()) {
                                e.setImdemnizaciones(rs3.getDouble("NOMD_UNITARIO"));
                                log.debug("Indeminizaciones " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("25").getIntIdPercDedu());
                            }
                            if (arrPercepciones.get(i) == HashMapPercepciones.get("26").getIntIdPercDedu()) {
                                e.setReembolsoFuneral(rs3.getDouble("NOMD_UNITARIO"));
                                log.debug("Reembolso por funeral " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("26").getIntIdPercDedu());
                            }
                            if (arrPercepciones.get(i) == HashMapPercepciones.get("27").getIntIdPercDedu()) {
                                e.setCuotasSeguridadSocialPagadas(rs3.getDouble("NOMD_UNITARIO"));
                                log.debug("Cuotas de seguridad social pagadas\n"
                                        + "por el patrón " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("27").getIntIdPercDedu());
                            }
                            if (arrPercepciones.get(i) == HashMapPercepciones.get("28").getIntIdPercDedu()) {
                                e.setComisiones(rs3.getDouble("NOMD_UNITARIO"));
                                log.debug("Comisiones " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("28").getIntIdPercDedu());
                            }
                            if (arrPercepciones.get(i) == HashMapPercepciones.get("29").getIntIdPercDedu()) {
                                e.setAnticipoCuentaUtilidades(rs3.getDouble("NOMD_UNITARIO"));
                                log.debug("Anticipo a Cuenta de Utilidades " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("29").getIntIdPercDedu());
                            }
                            if (arrPercepciones.get(i) == HashMapPercepciones.get("31").getIntIdPercDedu()) {
                                e.setPremioPuntualidad(rs3.getDouble("NOMD_UNITARIO"));
                                log.debug("Premio de puntualidad " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("31").getIntIdPercDedu());
                            }
                            if (arrPercepciones.get(i) == HashMapPercepciones.get("32").getIntIdPercDedu()) {
                                e.setPremioAsistencia(rs3.getDouble("NOMD_UNITARIO"));
                                log.debug("Premio de asistencia " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("32").getIntIdPercDedu());
                            }
                            if (arrPercepciones.get(i) == HashMapPercepciones.get("33").getIntIdPercDedu()) {
                                e.setBonoyGratificaciones(rs3.getDouble("NOMD_UNITARIO"));
                                log.debug("Bono y gratificaciones " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("33").getIntIdPercDedu());
                            }
                            if (arrPercepciones.get(i) == HashMapPercepciones.get("34").getIntIdPercDedu()) {
                                e.setDiaFestivo(rs3.getDouble("NOMD_UNITARIO"));
                                log.debug("Dia Festivo " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("34").getIntIdPercDedu());
                            }
                            if (arrPercepciones.get(i) == HashMapPercepciones.get("35").getIntIdPercDedu()) {
                                e.setBonoDeCumplimiento(Double.toString(rs3.getDouble("NOMD_UNITARIO")));
                                log.debug("Bono de cumplimiento " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("35").getIntIdPercDedu());
                            }
                            if (arrPercepciones.get(i) == HashMapPercepciones.get("36").getIntIdPercDedu()) {
                                e.setRetroActivo(rs3.getDouble("NOMD_UNITARIO"));
                                log.debug("Retroactivo " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("36").getIntIdPercDedu());
                            }

                        }
                        rs3.close();
                    } catch (SQLException ex) {
                        log.error(ex);
                    }
                }
                /*En este ciclo obtenemos los datos de nomina_deta Deducciones*/
                log.debug("-----------------------------------------Deducciones de la nomina");
                for (int i = 0; i < arrDeducciones.size(); i++) {
                    String strSql3 = "select NOMD_UNITARIO"
                            + " from rhh_nominas_deta"
                            + " where NOM_ID =" + intNom_ID + " and DEDU_ID= " + arrDeducciones.get(i) + " and NOMD_GRAVADO=0";
                    ResultSet rs3;
                    try {
                        rs3 = oConn.runQuery(strSql3, true);
                        while (rs3.next()) {
                            if (arrDeducciones.get(i) == HashMapDeducciones.get("7").getIntIdPercDedu()) {
                                e.setPensionAlimenticia(rs3.getDouble("NOMD_UNITARIO"));
                                log.debug("Pensión alimenticia " + arrDeducciones.get(i) + " == " + HashMapDeducciones.get("7").getIntIdPercDedu());
                            }
                            if (arrDeducciones.get(i) == HashMapDeducciones.get("2").getIntIdPercDedu()) {
                                e.setIsrRetenido(rs3.getDouble("NOMD_UNITARIO"));
                                log.debug("Retención de ISR " + arrDeducciones.get(i) + " == " + HashMapDeducciones.get("2").getIntIdPercDedu());
                            }
                            if (arrDeducciones.get(i) == HashMapDeducciones.get("1").getIntIdPercDedu()) {
                                e.setImss(rs3.getDouble("NOMD_UNITARIO"));
                                log.debug("Seguridad social " + arrDeducciones.get(i) + " == " + HashMapDeducciones.get("1").getIntIdPercDedu());
                            }
                            if (arrDeducciones.get(i) == HashMapDeducciones.get("5").getIntIdPercDedu()) {
                                e.setRetencionInfonavit(rs3.getDouble("NOMD_UNITARIO"));
                                log.debug("Aportaciones a Fondo de vivienda " + arrDeducciones.get(i) + " == " + HashMapDeducciones.get("5").getIntIdPercDedu());
                            }
                            if (arrDeducciones.get(i) == HashMapDeducciones.get("22").getIntIdPercDedu()) {
                                e.setOtrosDescuentos(rs3.getDouble("NOMD_UNITARIO"));
                                log.debug("Otros Descuentos " + arrDeducciones.get(i) + " == " + HashMapDeducciones.get("22").getIntIdPercDedu());
                            }
                            if (arrDeducciones.get(i) == HashMapDeducciones.get("23").getIntIdPercDedu()) {
                                e.setOtrosAnticipos(rs3.getDouble("NOMD_UNITARIO"));
                                log.debug("Otros Anticipos " + arrDeducciones.get(i) + " == " + HashMapDeducciones.get("23").getIntIdPercDedu());
                            }
                        }
                        rs3.close();
                    } catch (SQLException ex) {
                        log.error(ex);
                    }
                }

                log.debug("-----------------------------------------Incidencias");
                /*En este ciclo obtenemos los datos de rhh_incidencias Incidencias*/
                String strSql4 = "select *,"
                        + "(select count(RHIN_ID) from rhh_prima_dominical where rhh_incidencias.RHIN_ID = rhh_prima_dominical.RHIN_ID) as domingosTrabajados"
                        + " from rhh_incidencias"
                        + " where RHN_ID =" + intTipoNom + " and EMP_NUM =" + intEmp_NUM;
                ResultSet rs4;
                try {
                    rs4 = oConn.runQuery(strSql4, true);
                    while (rs4.next()) {
                        e.setFaltaJustificada(rs4.getInt("RHIN_NUM_FALTAS"));
                        e.setFaltaInjustificada(rs4.getInt("RHIN_NUM_FALTAS"));

                        e.setIncapacidad(rs4.getString("RHIN_DIAS_INCAPACIDAD"));

                        e.setRetardos(rs4.getString("RHIN_RETARDOS"));
                        e.setDomingosTrabajados(rs4.getInt("domingosTrabajados"));
                    }
                    rs4.close();

                } catch (SQLException ex) {
                    log.error(ex);
                }

                log.debug("-----------------------------------------Gravado y Excento");
                /*Aqui obtenemos los datos de Gravado y Excento*/
                double dblTotalExcento = 0.0;
                String strSql3 = "select SUM(NOMD_UNITARIO) as excento from rhh_nominas_deta where NOM_ID = " + intNom_ID + " and NOMD_GRAVADO = 0;";
                ResultSet rs3;
                try {
                    rs3 = oConn.runQuery(strSql3, true);
                    while (rs3.next()) {
                        dblTotalExcento = rs3.getDouble("excento");
                    }
                    rs3.close();
                } catch (SQLException ex) {
                    log.error(ex);
                }
                log.debug("Total Excento.... " + dblTotalExcento);

                double dblTotalGravado = 0.0;
                String strSql5 = "select SUM(NOMD_UNITARIO) as gravado from rhh_nominas_deta where NOM_ID = " + intNom_ID + " and NOMD_GRAVADO = 1;";
                ResultSet rs5;
                try {
                    rs5 = oConn.runQuery(strSql5, true);
                    while (rs5.next()) {
                        dblTotalGravado = rs5.getDouble("gravado");
                    }
                    rs5.close();
                } catch (SQLException ex) {
                    log.error(ex);
                }
                log.debug("Total Gravado.... " + dblTotalGravado);

                e.setGravado(dblTotalGravado);
                e.setExento(dblTotalExcento);

                log.debug("-----------------------------------------percepciones Gravado");
                /*Sacamos los datos agravados y excentos de las percepciones*/
                for (int i = 0; i < arrPercepciones.size(); i++) {
                    String strSql6 = "select NOMD_UNITARIO"
                            + " from rhh_nominas_deta"
                            + " where NOM_ID =" + intNom_ID + " and PERC_ID= " + arrPercepciones.get(i) + " and NOMD_GRAVADO=1";
                    ResultSet rs6;
                    try {
                        rs6 = oConn.runQuery(strSql6, true);
                        while (rs6.next()) {
                            if (arrPercepciones.get(i) == HashMapPercepciones.get("34").getIntIdPercDedu()) {
                                e.setDiaFestivoGravado(rs6.getDouble("NOMD_UNITARIO"));
                                log.debug("Dia Festivo GRAVADO  " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("34").getIntIdPercDedu());
                            }
                            if (arrPercepciones.get(i) == HashMapPercepciones.get("20").getIntIdPercDedu()) {
                                e.setPrimaDomicicalGravado(rs6.getDouble("NOMD_UNITARIO"));
                                log.debug("Prima dominical GRAVADO  " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("20").getIntIdPercDedu());
                            }
                            if (arrPercepciones.get(i) == HashMapPercepciones.get("2").getIntIdPercDedu()) {
                                e.setAguinaldoGravado(rs6.getDouble("NOMD_UNITARIO"));
                                log.debug("Gratificación Anual (Aguinaldo) GRAVADO  " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("2").getIntIdPercDedu());
                            }
                            if (arrPercepciones.get(i) == HashMapPercepciones.get("21").getIntIdPercDedu()) {
                                e.setPrimaVacacionalGravado(rs6.getDouble("NOMD_UNITARIO"));
                                log.debug("Prima vacacional GRAVADO  " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("21").getIntIdPercDedu());
                            }
                        }
                        rs6.close();
                    } catch (SQLException ex) {
                        log.error(ex);
                    }
                }
                log.debug("-----------------------------------------percepciones Excento");
                for (int i = 0; i < arrPercepciones.size(); i++) {
                    String strSql6 = "select NOMD_UNITARIO"
                            + " from rhh_nominas_deta"
                            + " where NOM_ID =" + intNom_ID + " and PERC_ID= " + arrPercepciones.get(i) + " and NOMD_GRAVADO=0";
                    ResultSet rs6;
                    try {
                        rs6 = oConn.runQuery(strSql6, true);
                        while (rs6.next()) {
                            if (arrPercepciones.get(i) == HashMapPercepciones.get("34").getIntIdPercDedu()) {
                                e.setDiaFestivoExento(rs6.getDouble("NOMD_UNITARIO"));
                                log.debug("Dia Festivo Excento  " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("34").getIntIdPercDedu());
                            }
                            if (arrPercepciones.get(i) == HashMapPercepciones.get("20").getIntIdPercDedu()) {
                                e.setPrimaDominicalExento(rs6.getDouble("NOMD_UNITARIO"));
                                log.debug("Prima dominical Excento  " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("20").getIntIdPercDedu());
                            }
                            if (arrPercepciones.get(i) == HashMapPercepciones.get("2").getIntIdPercDedu()) {
                                e.setAguinaldoExento(rs6.getDouble("NOMD_UNITARIO"));
                                log.debug("Gratificación Anual (Aguinaldo) Excento  " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("2").getIntIdPercDedu());
                            }
                            if (arrPercepciones.get(i) == HashMapPercepciones.get("21").getIntIdPercDedu()) {
                                e.setPrimaVacacionalExento(rs6.getDouble("NOMD_UNITARIO"));
                                log.debug("Prima vacacional Excento  " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("21").getIntIdPercDedu());
                            }
                        }
                        rs6.close();
                    } catch (SQLException ex) {
                        log.error(ex);
                    }
                }

                /*Sacamos los datos agravados y excentos de las deducciones*/
                e.setImporteBone("");
                e.setAportacionFondoDeAhorroEmpresa(0);
                e.setDiasDeDescansoTrabajados(0);
                e.setDiasDeDescansoGravado(0);
                e.setDiasDeDescansoExento(0);

                entidades.add(e);

            }
            rs.close();

        } catch (SQLException ex) {
            log.error(ex);
        }
    }

    public String GeneraXml() {
        Iterator<RepoPreNominaE> it = entidades.iterator();
        UtilXml util = new UtilXml();
        StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
        strXML.append("<PreNomina>");
        while (it.hasNext()) {
            RepoPreNominaE entidad = it.next();
            strXML.append(" <PreNomina_Deta");
            strXML.append(" consNom= \"").append(entidad.getConsNom()).append("\" ");
            strXML.append(" clave= \"").append(entidad.getClave()).append("\" ");
            strXML.append(" centroDeCostos= \"").append(entidad.getCentroDeCostos()).append("\" ");
            strXML.append(" nombre= \"").append(entidad.getNombre()).append("\" ");
            strXML.append(" sdi= \"").append(entidad.getSdi()).append("\" ");
            strXML.append(" sdn= \"").append(entidad.getSdn()).append("\" ");
            strXML.append(" faltaJustificada= \"").append(entidad.getFaltaJustificada()).append("\" ");
            strXML.append(" faltaInjustificada= \"").append(entidad.getFaltaInjustificada()).append("\" ");
            strXML.append(" incapacidad= \"").append(entidad.getIncapacidad()).append("\" ");
            strXML.append(" diasTrabajados= \"").append(entidad.getDiasTrabajados()).append("\" ");
            strXML.append(" sueldo= \"").append(entidad.getSueldo()).append("\" ");
            strXML.append(" retroActivo= \"").append(entidad.getRetroActivo()).append("\" ");
            strXML.append(" pensionAlimenticia= \"").append(entidad.getPensionAlimenticia()).append("\" ");
            strXML.append(" bonoyGratificaciones= \"").append(entidad.getBonoyGratificaciones()).append("\" ");
            strXML.append(" diaFestivo= \"").append(entidad.getDiaFestivo()).append("\" ");
            strXML.append(" bonoDeCumplimiento= \"").append(entidad.getBonoDeCumplimiento()).append("\" ");
            strXML.append(" importeBone= \"").append(entidad.getImporteBone()).append("\" ");
            strXML.append(" retardos= \"").append(entidad.getRetardos()).append("\" ");
            strXML.append(" domingosTrabajados= \"").append(entidad.getDomingosTrabajados()).append("\" ");
            strXML.append(" primaDominical= \"").append(entidad.getPrimaDominical()).append("\" ");
            strXML.append(" aportacionFondoDeAhorro= \"").append(entidad.getAportacionFondoDeAhorro()).append("\" ");
            strXML.append(" aportacionFondoDeAhorroEmpresa= \"").append(entidad.getAportacionFondoDeAhorroEmpresa()).append("\" ");
            strXML.append(" totalPercepciones= \"").append(entidad.getTotalPercepciones()).append("\" ");
            strXML.append(" totalDeducciones= \"").append(entidad.getTotalDeducciones()).append("\" ");
            strXML.append(" gravado= \"").append(entidad.getGravado()).append("\" ");
            strXML.append(" exento= \"").append(entidad.getExento()).append("\" ");
            strXML.append(" subsidioAlEmpleo= \"").append(entidad.getSubsidioAlEmpleo()).append("\" ");
            strXML.append(" isrRetenido= \"").append(entidad.getIsrRetenido()).append("\" ");
            strXML.append(" imss= \"").append(entidad.getImss()).append("\" ");
            strXML.append(" retencionInfonavit= \"").append(entidad.getRetencionInfonavit()).append("\" ");
            strXML.append(" otrosDescuentos= \"").append(entidad.getOtrosDescuentos()).append("\" ");
            strXML.append(" otrosAnticipos= \"").append(entidad.getOtrosAnticipos()).append("\" ");
            strXML.append(" aguinaldo= \"").append(entidad.getAguinaldo()).append("\" ");
            strXML.append(" primaVacacional= \"").append(entidad.getPrimaVacacional()).append("\" ");
            strXML.append(" diasDeDescansoTrabajados= \"").append(entidad.getDiasDeDescansoTrabajados()).append("\" ");
            strXML.append(" netoApagar= \"").append(entidad.getNetoApagar()).append("\" ");
            strXML.append(" noDeRecibido= \"").append(entidad.getNoDeRecibido()).append("\" ");
            strXML.append(" diaFestivoGravado= \"").append(entidad.getDiaFestivoGravado()).append("\" ");
            strXML.append(" diaFestivoExento= \"").append(entidad.getDiaFestivoExento()).append("\" ");
            strXML.append(" primaDomicicalGravado= \"").append(entidad.getPrimaDomicicalGravado()).append("\" ");
            strXML.append(" primaDominicalExento= \"").append(entidad.getPrimaDominicalExento()).append("\" ");
            strXML.append(" aguinaldoGravado= \"").append(entidad.getAguinaldoGravado()).append("\" ");
            strXML.append(" aguinaldoExento= \"").append(entidad.getAguinaldoExento()).append("\" ");
            strXML.append(" primaVacacionalGravado= \"").append(entidad.getPrimaVacacionalGravado()).append("\" ");
            strXML.append(" primaVacacionalExento= \"").append(entidad.getPrimaVacacionalExento()).append("\" ");
            strXML.append(" diasDeDescansoGravado= \"").append(entidad.getDiasDeDescansoGravado()).append("\" ");
            strXML.append(" diasDeDescansoExento= \"").append(entidad.getDiasDeDescansoExento()).append("\" ");
            strXML.append(" ptu= \"").append(entidad.getPtu()).append("\" ");
            strXML.append(" reembolsoGastosMedicosDentHosp= \"").append(entidad.getReembolsoGastosMedicosDentHosp()).append("\" ");
            strXML.append(" cajaAhorro= \"").append(entidad.getCajaAhorro()).append("\" ");
            strXML.append(" vales= \"").append(entidad.getVales()).append("\" ");
            strXML.append(" ayudas= \"").append(entidad.getAyudas()).append("\" ");
            strXML.append(" contribucuionesTrabajadorPagadasPatron= \"").append(entidad.getContribucuionesTrabajadorPagadasPatron()).append("\" ");
            strXML.append(" vacaciones= \"").append(entidad.getVacaciones()).append("\" ");
            strXML.append(" primaSeguroVida= \"").append(entidad.getPrimaSeguroVida()).append("\" ");
            strXML.append(" seguroGastosMedicosMayores= \"").append(entidad.getSeguroGastosMedicosMayores()).append("\" ");
            strXML.append(" cuotasSindicalesPagadasPatron= \"").append(entidad.getCuotasSindicalesPagadasPatron()).append("\" ");
            strXML.append(" subsidioIncapacidad= \"").append(entidad.getSubsidioIncapacidad()).append("\" ");
            strXML.append(" becasTrabajadoresHijos= \"").append(entidad.getBecasTrabajadoresHijos()).append("\" ");
            strXML.append(" otros= \"").append(entidad.getOtros()).append("\" ");
            strXML.append(" fomentoPrimerEmpleo= \"").append(entidad.getFomentoPrimerEmpleo()).append("\" ");
            strXML.append(" horasExtras= \"").append(entidad.getHorasExtras()).append("\" ");
            strXML.append(" primaAntiguedad= \"").append(entidad.getPrimaAntiguedad()).append("\" ");
            strXML.append(" pagoSeparacion= \"").append(entidad.getPagoSeparacion()).append("\" ");
            strXML.append(" seguroRetiro= \"").append(entidad.getSeguroRetiro()).append("\" ");
            strXML.append(" imdemnizaciones= \"").append(entidad.getImdemnizaciones()).append("\" ");
            strXML.append(" reembolsoFuneral= \"").append(entidad.getReembolsoFuneral()).append("\" ");
            strXML.append(" cuotasSeguridadSocialPagadas= \"").append(entidad.getCuotasSeguridadSocialPagadas()).append("\" ");
            strXML.append(" comisiones= \"").append(entidad.getComisiones()).append("\" ");
            strXML.append(" anticipoCuentaUtilidades= \"").append(entidad.getAnticipoCuentaUtilidades()).append("\" ");
            strXML.append(" premioPuntualidad= \"").append(entidad.getPremioPuntualidad()).append("\" ");
            strXML.append(" premioAsistencia= \"").append(entidad.getPremioAsistencia()).append("\" ");

            strXML.append("/>");
        }
        strXML.append("</PreNomina>");
        return strXML.toString();
    }

    public void getReportPrint(String sourceFileName, String targetFileName, VariableSession varSesiones,
            ByteArrayOutputStream byteArrayOutputStream, int frmt, String strPathLogoWeb) {
        this.bolImpreso = true;

        //Logo de la empresa
        String strSql2 = "select EMP_PATHIMG from vta_empresas where EMP_ID  =" + varSesiones.getIntIdEmpresa();
        try {
            rs2 = this.oConn.runQuery(strSql2, true);
            while (rs2.next()) {
                strPathLogoWeb += System.getProperty("file.separator") + rs2.getString("EMP_PATHIMG");
            }
            rs2.close();
        } catch (SQLException ex) {
            log.error(ex);
        }

        strSql2 = "select EMP_PATHIMGFORM,EMP_RAZONSOCIAL,EMP_RFC from vta_empresas where EMP_ID  =" + varSesiones.getIntIdEmpresa();
        try {
            rs2 = this.oConn.runQuery(strSql2, true);
            while (rs2.next()) {
                emp_nom = rs2.getString("EMP_RAZONSOCIAL");
                emp_rfc = rs2.getString("EMP_RFC");
            }
            rs2.close();
        } catch (SQLException ex) {
            log.error(ex);
        }

        //Generamos el pdf
        InputStream reportStream = null;
        try {
            //Parametros
            Map parametersMap = new HashMap();

            parametersMap.put("empresa", emp_nom);
            parametersMap.put("rfc", emp_nom);
            reportStream = new FileInputStream(sourceFileName);
            // Bing the datasource with the collection
            JRDataSource datasource = new JRBeanCollectionDataSource(this.entidades, true);
            // Compile and print the jasper report
            JasperReport report = JasperCompileManager.compileReport(reportStream);
            //JasperPrint print = JasperFillManager.fillReport(report, parameters, datasource);
            JasperPrint print = JasperFillManager.fillReport(report, parametersMap, datasource);

            reportStream.close();

            switch (frmt) {
                case 1:
                    if (byteArrayOutputStream == null) {
                        JasperExportManager.exportReportToPdfFile(print, targetFileName);
                    } else {
                        JasperExportManager.exportReportToPdfStream(print, byteArrayOutputStream);
                    }
                    break;
                case 2:
                    JRXlsExporter exporterXLS = new JRXlsExporter();
                    exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, print);
                    exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
                    exporterXLS.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
                    exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
                    exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
                    exporterXLS.exportReport();
                    break;
            }

        } catch (FileNotFoundException ex) {
            log.error(ex);
        } catch (JRException ex) {
            log.error(ex);
        } catch (IOException ex) {
            log.error(ex);
        }
    }

    public String GeneraXml_ImpSobreNomina(int intNumeroEmpleados, int intSucId) {
        /*Aqui obtenemos los Id,clave de las percepciones y deducciones */
        PercepcionesDeducciones PercDedu = new PercepcionesDeducciones(oConn, emp_id);
        Map<String, PercepcionesDeduccionesE> HashMapPercepciones1 = PercDedu.ObtenPercepciones();

        String tasa = "";

        boolean boolTienePercepcionesExcentas = false;
        Iterator<RepoPreNominaE> it = entidades.iterator();
        UtilXml util = new UtilXml();
        StringBuilder strXML = new StringBuilder();
        strXML.append("<PreNomina>");
        while (it.hasNext()) {
            RepoPreNominaE entidad = it.next();
            strXML.append(" <PreNomina_Deta");

            /*Obtenemos el Impuesto sobre la nomina*/
            String strSql2 = "select SC_ESTADO from vta_sucursal where SC_ID = " + intSucId;
            ResultSet rs22;
            try {
                rs22 = oConn.runQuery(strSql2, true);
                while (rs22.next()) {

                    String strSql3 = "select EST_ISN_TASA from rhh_impuesto_nomina where EST_ISN_NOMBRE ='" + rs22.getString("SC_ESTADO") + "';";
                    ResultSet rs3;
                    try {
                        rs3 = oConn.runQuery(strSql3, true);
                        while (rs3.next()) {
                            if (rs3.getDouble("EST_ISN_TASA") == 0.0) {
                                tasa = "Tarifa";
                            } else {
                                tasa = Double.toString(rs3.getDouble("EST_ISN_TASA"));
                            }
                        }
                        rs3.close();
                    } catch (SQLException ex) {
                        log.error(ex);
                    }

                }
                rs22.close();
            } catch (SQLException ex) {
                log.error(ex);
            }

            
            strXML.append(" isntasa= \"").append(tasa).append("\" ");
            
            /*Obtenemos Los Id de las percepciones de la nomia actual y las guardamos en un arrayList*/
            String strSql1 = "select * from rhh_percepciones_no_impuesto where PERNO_IMP_IDSUCURSAL = " + intSucId;
            ResultSet rs1;
            try {
                rs1 = oConn.runQuery(strSql1, true);
                while (rs1.next()) {

                    boolTienePercepcionesExcentas = true;

                    strXML.append(" consNom= \"").append(entidad.getConsNom()).append("\" ");
                    strXML.append(" clave= \"").append(entidad.getClave()).append("\" ");
                    strXML.append(" centroDeCostos= \"").append(entidad.getCentroDeCostos()).append("\" ");
                    strXML.append(" nombre= \"").append(entidad.getNombre()).append("\" ");
                    strXML.append(" sdi= \"").append(entidad.getSdi()).append("\" ");
                    strXML.append(" sdn= \"").append(entidad.getSdn()).append("\" ");
                    strXML.append(" diasTrabajados= \"").append(entidad.getDiasTrabajados()).append("\" ");
                    strXML.append(" domingosTrabajados= \"").append(entidad.getDomingosTrabajados()).append("\" ");
                    strXML.append(" diasDeDescansoTrabajados= \"").append(entidad.getDiasDeDescansoTrabajados()).append("\" ");
                    strXML.append(" netoApagar= \"").append(entidad.getNetoApagar()).append("\" ");
                    strXML.append(" totalPercepciones= \"").append(entidad.getTotalPercepciones()).append("\" ");



                    

                    if (rs1.getInt("PERNO_IMP_IDPERCEPCION") == HashMapPercepciones1.get("1").getIntIdPercDedu()) {

                        strXML.append(" sueldo= \"").append(entidad.getSueldo()).append("\" ");
                        strXML.append(" sueldoSuma= \"").append(0.0).append("\" ");

                    } else {

                        strXML.append(" sueldo= \"").append(entidad.getSueldo()).append("\" ");
                        strXML.append(" sueldoSuma= \"").append(entidad.getSueldo()).append("\" ");

                    }

                    if (rs1.getInt("PERNO_IMP_IDPERCEPCION") == HashMapPercepciones1.get("2").getIntIdPercDedu()) {
                        strXML.append(" aguinaldo= \"").append(entidad.getAguinaldo()).append("\" ");
                        strXML.append(" aguinaldoSuma= \"").append(0.0).append("\" ");
                    } else {
                        strXML.append(" aguinaldo= \"").append(entidad.getAguinaldo()).append("\" ");
                        strXML.append(" aguinaldoSuma= \"").append(entidad.getAguinaldo()).append("\" ");
                    }

                    if (rs1.getInt("PERNO_IMP_IDPERCEPCION") == HashMapPercepciones1.get("3").getIntIdPercDedu()) {
                        strXML.append(" ptu= \"").append(entidad.getPtu()).append("\" ");
                        strXML.append(" ptuSuma= \"").append(0.0).append("\" ");
                    } else {
                        strXML.append(" ptu= \"").append(entidad.getPtu()).append("\" ");
                        strXML.append(" ptuSuma= \"").append(entidad.getPtu()).append("\" ");
                    }

                    if (rs1.getInt("PERNO_IMP_IDPERCEPCION") == HashMapPercepciones1.get("4").getIntIdPercDedu()) {
                        strXML.append(" reembolsoGastosMedicosDentHosp= \"").append(entidad.getReembolsoGastosMedicosDentHosp()).append("\" ");
                        strXML.append(" reembolsoGastosMedicosDentHospSuma= \"").append(0.0).append("\" ");
                    } else {
                        strXML.append(" reembolsoGastosMedicosDentHosp= \"").append(entidad.getReembolsoGastosMedicosDentHosp()).append("\" ");

                        strXML.append(" reembolsoGastosMedicosDentHospSuma= \"").append(entidad.getReembolsoGastosMedicosDentHosp()).append("\" ");
                    }

                    if (rs1.getInt("PERNO_IMP_IDPERCEPCION") == HashMapPercepciones1.get("5").getIntIdPercDedu()) {
                        strXML.append(" aportacionFondoDeAhorro= \"").append(entidad.getAportacionFondoDeAhorro()).append("\" ");
                        strXML.append(" aportacionFondoDeAhorroSuma= \"").append(0.0).append("\" ");
                    } else {
                        strXML.append(" aportacionFondoDeAhorro= \"").append(entidad.getAportacionFondoDeAhorro()).append("\" ");

                        strXML.append(" aportacionFondoDeAhorroSuma= \"").append(entidad.getAportacionFondoDeAhorro()).append("\" ");
                    }

                    if (rs1.getInt("PERNO_IMP_IDPERCEPCION") == HashMapPercepciones1.get("6").getIntIdPercDedu()) {
                        strXML.append(" cajaAhorro= \"").append(entidad.getCajaAhorro()).append("\" ");
                        strXML.append(" cajaAhorroSuma= \"").append(0.0).append("\" ");
                    } else {
                        strXML.append(" cajaAhorro= \"").append(entidad.getCajaAhorro()).append("\" ");

                        strXML.append(" cajaAhorroSuma= \"").append(entidad.getCajaAhorro()).append("\" ");
                    }

                    if (rs1.getInt("PERNO_IMP_IDPERCEPCION") == HashMapPercepciones1.get("7").getIntIdPercDedu()) {
                        strXML.append(" vales= \"").append(entidad.getVales()).append("\" ");
                        strXML.append(" valesSuma= \"").append(0.0).append("\" ");
                    } else {
                        strXML.append(" vales= \"").append(entidad.getVales()).append("\" ");

                        strXML.append(" valesSuma= \"").append(entidad.getVales()).append("\" ");
                    }

                    if (rs1.getInt("PERNO_IMP_IDPERCEPCION") == HashMapPercepciones1.get("8").getIntIdPercDedu()) {
                        strXML.append(" ayudas= \"").append(entidad.getAyudas()).append("\" ");
                        strXML.append(" ayudasSuma= \"").append(0.0).append("\" ");
                    } else {
                        strXML.append(" ayudas= \"").append(entidad.getAyudas()).append("\" ");

                        strXML.append(" ayudasSuma= \"").append(entidad.getAyudas()).append("\" ");
                    }

                    if (rs1.getInt("PERNO_IMP_IDPERCEPCION") == HashMapPercepciones1.get("9").getIntIdPercDedu()) {
                        strXML.append(" contribucuionesTrabajadorPagadasPatron= \"").append(entidad.getContribucuionesTrabajadorPagadasPatron()).append("\" ");
                        strXML.append(" contribucuionesTrabajadorPagadasPatronSuma= \"").append(0.0).append("\" ");
                    } else {
                        strXML.append(" contribucuionesTrabajadorPagadasPatron= \"").append(entidad.getContribucuionesTrabajadorPagadasPatron()).append("\" ");

                        strXML.append(" contribucuionesTrabajadorPagadasPatronSuma= \"").append(entidad.getContribucuionesTrabajadorPagadasPatron()).append("\" ");
                    }

                    if (rs1.getInt("PERNO_IMP_IDPERCEPCION") == HashMapPercepciones1.get("30").getIntIdPercDedu()) {
                        strXML.append(" vacaciones= \"").append(entidad.getVacaciones()).append("\" ");
                        strXML.append(" vacacionesSuma= \"").append(0.0).append("\" ");
                    } else {
                        strXML.append(" vacaciones= \"").append(entidad.getVacaciones()).append("\" ");

                        strXML.append(" vacacionesSuma= \"").append(entidad.getVacaciones()).append("\" ");
                    }

                    if (rs1.getInt("PERNO_IMP_IDPERCEPCION") == HashMapPercepciones1.get("11").getIntIdPercDedu()) {
                        strXML.append(" primaSeguroVida= \"").append(entidad.getPrimaSeguroVida()).append("\" ");
                        strXML.append(" primaSeguroVidaSuma= \"").append(0.0).append("\" ");
                    } else {
                        strXML.append(" primaSeguroVida= \"").append(entidad.getPrimaSeguroVida()).append("\" ");

                        strXML.append(" primaSeguroVidaSuma= \"").append(entidad.getPrimaSeguroVida()).append("\" ");
                    }

                    if (rs1.getInt("PERNO_IMP_IDPERCEPCION") == HashMapPercepciones1.get("12").getIntIdPercDedu()) {
                        strXML.append(" seguroGastosMedicosMayores= \"").append(entidad.getSeguroGastosMedicosMayores()).append("\" ");
                        strXML.append(" seguroGastosMedicosMayoresSuma= \"").append(0.0).append("\" ");
                    } else {
                        strXML.append(" seguroGastosMedicosMayores= \"").append(entidad.getSeguroGastosMedicosMayores()).append("\" ");

                        strXML.append(" seguroGastosMedicosMayoresSuma= \"").append(entidad.getSeguroGastosMedicosMayores()).append("\" ");
                    }

                    if (rs1.getInt("PERNO_IMP_IDPERCEPCION") == HashMapPercepciones1.get("13").getIntIdPercDedu()) {
                        strXML.append(" cuotasSindicalesPagadasPatron= \"").append(entidad.getCuotasSindicalesPagadasPatron()).append("\" ");
                        strXML.append(" cuotasSindicalesPagadasPatronSuma= \"").append(0.0).append("\" ");
                    } else {
                        strXML.append(" cuotasSindicalesPagadasPatron= \"").append(entidad.getCuotasSindicalesPagadasPatron()).append("\" ");

                        strXML.append(" cuotasSindicalesPagadasPatronSuma= \"").append(entidad.getCuotasSindicalesPagadasPatron()).append("\" ");
                    }

                    if (rs1.getInt("PERNO_IMP_IDPERCEPCION") == HashMapPercepciones1.get("14").getIntIdPercDedu()) {
                        strXML.append(" subsidioIncapacidad= \"").append(entidad.getSubsidioIncapacidad()).append("\" ");
                        strXML.append(" subsidioIncapacidadSuma= \"").append(0.0).append("\" ");
                    } else {
                        strXML.append(" subsidioIncapacidad= \"").append(entidad.getSubsidioIncapacidad()).append("\" ");

                        strXML.append(" subsidioIncapacidadSuma= \"").append(entidad.getSubsidioIncapacidad()).append("\" ");
                    }

                    if (rs1.getInt("PERNO_IMP_IDPERCEPCION") == HashMapPercepciones1.get("15").getIntIdPercDedu()) {
                        strXML.append(" becasTrabajadoresHijos= \"").append(entidad.getBecasTrabajadoresHijos()).append("\" ");
                        strXML.append(" becasTrabajadoresHijosSuma= \"").append(0.0).append("\" ");
                    } else {
                        strXML.append(" becasTrabajadoresHijos= \"").append(entidad.getBecasTrabajadoresHijos()).append("\" ");

                        strXML.append(" becasTrabajadoresHijosSuma= \"").append(entidad.getBecasTrabajadoresHijos()).append("\" ");
                    }

                    if (rs1.getInt("PERNO_IMP_IDPERCEPCION") == HashMapPercepciones1.get("16").getIntIdPercDedu()) {
                        strXML.append(" otros= \"").append(entidad.getOtros()).append("\" ");
                        strXML.append(" otrosSuma= \"").append(0.0).append("\" ");
                    } else {
                        strXML.append(" otros= \"").append(entidad.getOtros()).append("\" ");

                        strXML.append(" otrosSuma= \"").append(entidad.getOtros()).append("\" ");
                    }

                    if (rs1.getInt("PERNO_IMP_IDPERCEPCION") == HashMapPercepciones1.get("17").getIntIdPercDedu()) {
                        strXML.append(" subsidioAlEmpleo= \"").append(entidad.getSubsidioAlEmpleo()).append("\" ");
                        strXML.append(" subsidioAlEmpleoSuma= \"").append(0.0).append("\" ");
                    } else {
                        strXML.append(" subsidioAlEmpleo= \"").append(entidad.getSubsidioAlEmpleo()).append("\" ");

                        strXML.append(" subsidioAlEmpleoSuma= \"").append(entidad.getSubsidioAlEmpleo()).append("\" ");
                    }

                    if (rs1.getInt("PERNO_IMP_IDPERCEPCION") == HashMapPercepciones1.get("18").getIntIdPercDedu()) {
                        strXML.append(" fomentoPrimerEmpleo= \"").append(entidad.getFomentoPrimerEmpleo()).append("\" ");
                        strXML.append(" fomentoPrimerEmpleoSuma= \"").append(0.0).append("\" ");
                    } else {
                        strXML.append(" fomentoPrimerEmpleo= \"").append(entidad.getFomentoPrimerEmpleo()).append("\" ");

                        strXML.append(" fomentoPrimerEmpleoSuma= \"").append(entidad.getFomentoPrimerEmpleo()).append("\" ");
                    }

                    if (rs1.getInt("PERNO_IMP_IDPERCEPCION") == HashMapPercepciones1.get("19").getIntIdPercDedu()) {
                        strXML.append(" horasExtras= \"").append(entidad.getHorasExtras()).append("\" ");
                        strXML.append(" horasExtrasSuma= \"").append(0.0).append("\" ");
                    } else {
                        strXML.append(" horasExtras= \"").append(entidad.getHorasExtras()).append("\" ");

                        strXML.append(" horasExtrasSuma= \"").append(entidad.getHorasExtras()).append("\" ");
                    }

                    if (rs1.getInt("PERNO_IMP_IDPERCEPCION") == HashMapPercepciones1.get("20").getIntIdPercDedu()) {
                        strXML.append(" primaDominical= \"").append(entidad.getPrimaDominical()).append("\" ");
                        strXML.append(" primaDominicalSuma= \"").append(0.0).append("\" ");
                    } else {
                        strXML.append(" primaDominical= \"").append(entidad.getPrimaDominical()).append("\" ");

                        strXML.append(" primaDominicalSuma= \"").append(entidad.getPrimaDominical()).append("\" ");
                    }

                    if (rs1.getInt("PERNO_IMP_IDPERCEPCION") == HashMapPercepciones1.get("21").getIntIdPercDedu()) {
                        strXML.append(" primaVacacional= \"").append(entidad.getPrimaVacacional()).append("\" ");
                        strXML.append(" primaVacacionalSuma= \"").append(0.0).append("\" ");
                    } else {
                        strXML.append(" primaVacacional= \"").append(entidad.getPrimaVacacional()).append("\" ");

                        strXML.append(" primaVacacionalSuma= \"").append(entidad.getPrimaVacacional()).append("\" ");
                    }

                    if (rs1.getInt("PERNO_IMP_IDPERCEPCION") == HashMapPercepciones1.get("22").getIntIdPercDedu()) {
                        strXML.append(" primaAntiguedad= \"").append(entidad.getPrimaAntiguedad()).append("\" ");
                        strXML.append(" primaAntiguedadSuma= \"").append(0.0).append("\" ");
                    } else {
                        strXML.append(" primaAntiguedad= \"").append(entidad.getPrimaAntiguedad()).append("\" ");

                        strXML.append(" primaAntiguedadSuma= \"").append(entidad.getPrimaAntiguedad()).append("\" ");
                    }

                    if (rs1.getInt("PERNO_IMP_IDPERCEPCION") == HashMapPercepciones1.get("23").getIntIdPercDedu()) {
                        strXML.append(" pagoSeparacion= \"").append(entidad.getPagoSeparacion()).append("\" ");
                        strXML.append(" pagoSeparacionSuma= \"").append(0.0).append("\" ");
                    } else {
                        strXML.append(" pagoSeparacion= \"").append(entidad.getPagoSeparacion()).append("\" ");

                        strXML.append(" pagoSeparacionSuma= \"").append(entidad.getPagoSeparacion()).append("\" ");
                    }

                    if (rs1.getInt("PERNO_IMP_IDPERCEPCION") == HashMapPercepciones1.get("24").getIntIdPercDedu()) {
                        strXML.append(" seguroRetiro= \"").append(entidad.getSeguroRetiro()).append("\" ");
                        strXML.append(" seguroRetiroSuma= \"").append(0.0).append("\" ");
                    } else {
                        strXML.append(" seguroRetiro= \"").append(entidad.getSeguroRetiro()).append("\" ");

                        strXML.append(" seguroRetiroSuma= \"").append(entidad.getSeguroRetiro()).append("\" ");
                    }

                    if (rs1.getInt("PERNO_IMP_IDPERCEPCION") == HashMapPercepciones1.get("25").getIntIdPercDedu()) {
                        strXML.append(" imdemnizaciones= \"").append(entidad.getImdemnizaciones()).append("\" ");
                        strXML.append(" imdemnizacionesSuma= \"").append(0.0).append("\" ");
                    } else {
                        strXML.append(" imdemnizaciones= \"").append(entidad.getImdemnizaciones()).append("\" ");

                        strXML.append(" imdemnizacionesSuma= \"").append(entidad.getImdemnizaciones()).append("\" ");
                    }

                    if (rs1.getInt("PERNO_IMP_IDPERCEPCION") == HashMapPercepciones1.get("26").getIntIdPercDedu()) {
                        strXML.append(" reembolsoFuneral= \"").append(entidad.getReembolsoFuneral()).append("\" ");
                        strXML.append(" reembolsoFuneralSuma= \"").append(0.0).append("\" ");
                    } else {
                        strXML.append(" reembolsoFuneral= \"").append(entidad.getReembolsoFuneral()).append("\" ");

                        strXML.append(" reembolsoFuneralSuma= \"").append(entidad.getReembolsoFuneral()).append("\" ");
                    }

                    if (rs1.getInt("PERNO_IMP_IDPERCEPCION") == HashMapPercepciones1.get("27").getIntIdPercDedu()) {
                        strXML.append(" cuotasSeguridadSocialPagadas= \"").append(entidad.getCuotasSeguridadSocialPagadas()).append("\" ");
                        strXML.append(" cuotasSeguridadSocialPagadasSuma= \"").append(0.0).append("\" ");
                    } else {
                        strXML.append(" cuotasSeguridadSocialPagadas= \"").append(entidad.getCuotasSeguridadSocialPagadas()).append("\" ");

                        strXML.append(" cuotasSeguridadSocialPagadasSuma= \"").append(entidad.getCuotasSeguridadSocialPagadas()).append("\" ");
                    }

                    if (rs1.getInt("PERNO_IMP_IDPERCEPCION") == HashMapPercepciones1.get("28").getIntIdPercDedu()) {
                        strXML.append(" comisiones= \"").append(entidad.getComisiones()).append("\" ");
                        strXML.append(" comisionesSuma= \"").append(0.0).append("\" ");
                    } else {
                        strXML.append(" comisiones= \"").append(entidad.getComisiones()).append("\" ");

                        strXML.append(" comisionesSuma= \"").append(entidad.getComisiones()).append("\" ");
                    }

                    if (rs1.getInt("PERNO_IMP_IDPERCEPCION") == HashMapPercepciones1.get("29").getIntIdPercDedu()) {
                        strXML.append(" anticipoCuentaUtilidades= \"").append(entidad.getAnticipoCuentaUtilidades()).append("\" ");
                        strXML.append(" anticipoCuentaUtilidadesSuma= \"").append(0.0).append("\" ");
                    } else {
                        strXML.append(" anticipoCuentaUtilidades= \"").append(entidad.getAnticipoCuentaUtilidades()).append("\" ");

                        strXML.append(" anticipoCuentaUtilidadesSuma= \"").append(entidad.getAnticipoCuentaUtilidades()).append("\" ");
                    }

                    if (rs1.getInt("PERNO_IMP_IDPERCEPCION") == HashMapPercepciones1.get("31").getIntIdPercDedu()) {
                        strXML.append(" premioPuntualidad= \"").append(entidad.getPremioPuntualidad()).append("\" ");
                        strXML.append(" premioPuntualidadSuma= \"").append(0.0).append("\" ");
                    } else {
                        strXML.append(" premioPuntualidad= \"").append(entidad.getPremioPuntualidad()).append("\" ");

                        strXML.append(" premioPuntualidadSuma= \"").append(entidad.getPremioPuntualidad()).append("\" ");
                    }

                    if (rs1.getInt("PERNO_IMP_IDPERCEPCION") == HashMapPercepciones1.get("32").getIntIdPercDedu()) {
                        strXML.append(" premioAsistencia= \"").append(entidad.getPremioAsistencia()).append("\" ");
                        strXML.append(" premioAsistenciaSuma= \"").append(0.0).append("\" ");
                    } else {
                        strXML.append(" premioAsistencia= \"").append(entidad.getPremioAsistencia()).append("\" ");

                        strXML.append(" premioAsistenciaSuma= \"").append(entidad.getPremioAsistencia()).append("\" ");
                    }

                    if (rs1.getInt("PERNO_IMP_IDPERCEPCION") == HashMapPercepciones1.get("33").getIntIdPercDedu()) {
                        strXML.append(" bonoyGratificaciones= \"").append(entidad.getBonoyGratificaciones()).append("\" ");
                        strXML.append(" bonoyGratificacionesSuma= \"").append(0.0).append("\" ");
                    } else {
                        strXML.append(" bonoyGratificaciones= \"").append(entidad.getBonoyGratificaciones()).append("\" ");

                        strXML.append(" bonoyGratificacionesSuma= \"").append(entidad.getBonoyGratificaciones()).append("\" ");
                    }

                    if (rs1.getInt("PERNO_IMP_IDPERCEPCION") == HashMapPercepciones1.get("34").getIntIdPercDedu()) {
                        strXML.append(" diaFestivo= \"").append(entidad.getDiaFestivo()).append("\" ");
                        strXML.append(" diaFestivoSuma= \"").append(0.0).append("\" ");
                    } else {
                        strXML.append(" diaFestivo= \"").append(entidad.getDiaFestivo()).append("\" ");

                        strXML.append(" diaFestivoSuma= \"").append(entidad.getDiaFestivo()).append("\" ");
                    }

                    if (rs1.getInt("PERNO_IMP_IDPERCEPCION") == HashMapPercepciones1.get("35").getIntIdPercDedu()) {
                        strXML.append(" bonoDeCumplimiento= \"").append(entidad.getBonoDeCumplimiento()).append("\" ");
                        strXML.append(" bonoDeCumplimientoSuma= \"").append(0.0).append("\" ");
                    } else {
                        strXML.append(" bonoDeCumplimiento= \"").append(entidad.getBonoDeCumplimiento()).append("\" ");

                        strXML.append(" bonoDeCumplimientoSuma= \"").append(entidad.getBonoDeCumplimiento()).append("\" ");
                    }

                    if (rs1.getInt("PERNO_IMP_IDPERCEPCION") == HashMapPercepciones1.get("36").getIntIdPercDedu()) {
                        strXML.append(" retroActivo= \"").append(entidad.getRetroActivo()).append("\" ");
                        strXML.append(" retroActivoSuma= \"").append(0.0).append("\" ");
                    } else {
                        strXML.append(" retroActivo= \"").append(entidad.getRetroActivo()).append("\" ");

                        strXML.append(" retroActivoSuma= \"").append(entidad.getRetroActivo()).append("\" ");
                    }

                    strXML.append(" numerotrabajadores= \"").append(intNumeroEmpleados).append("\" ");

                }
                rs1.close();
            } catch (SQLException ex) {
                log.error(ex);
            }

            if (boolTienePercepcionesExcentas == false) {

                strXML.append(" consNom= \"").append(entidad.getConsNom()).append("\" ");
                strXML.append(" clave= \"").append(entidad.getClave()).append("\" ");
                strXML.append(" centroDeCostos= \"").append(entidad.getCentroDeCostos()).append("\" ");
                strXML.append(" nombre= \"").append(entidad.getNombre()).append("\" ");
                strXML.append(" sdi= \"").append(entidad.getSdi()).append("\" ");
                strXML.append(" sdn= \"").append(entidad.getSdn()).append("\" ");
                strXML.append(" diasTrabajados= \"").append(entidad.getDiasTrabajados()).append("\" ");
                strXML.append(" domingosTrabajados= \"").append(entidad.getDomingosTrabajados()).append("\" ");
                strXML.append(" diasDeDescansoTrabajados= \"").append(entidad.getDiasDeDescansoTrabajados()).append("\" ");
                strXML.append(" netoApagar= \"").append(entidad.getNetoApagar()).append("\" ");
                strXML.append(" totalPercepciones= \"").append(entidad.getTotalPercepciones()).append("\" ");
                strXML.append(" sueldo= \"").append(entidad.getSueldo()).append("\" ");
                strXML.append(" sueldoSuma= \"").append(entidad.getSueldo()).append("\" ");
                strXML.append(" aguinaldo= \"").append(entidad.getAguinaldo()).append("\" ");
                strXML.append(" aguinaldoSuma= \"").append(entidad.getAguinaldo()).append("\" ");
                strXML.append(" ptu= \"").append(entidad.getPtu()).append("\" ");
                strXML.append(" ptuSuma= \"").append(entidad.getPtu()).append("\" ");
                strXML.append(" reembolsoGastosMedicosDentHosp= \"").append(entidad.getReembolsoGastosMedicosDentHosp()).append("\" ");
                strXML.append(" reembolsoGastosMedicosDentHospSuma= \"").append(entidad.getReembolsoGastosMedicosDentHosp()).append("\" ");
                strXML.append(" aportacionFondoDeAhorro= \"").append(entidad.getAportacionFondoDeAhorro()).append("\" ");
                strXML.append(" aportacionFondoDeAhorroSuma= \"").append(entidad.getAportacionFondoDeAhorro()).append("\" ");
                strXML.append(" cajaAhorro= \"").append(entidad.getCajaAhorro()).append("\" ");
                strXML.append(" cajaAhorroSuma= \"").append(entidad.getCajaAhorro()).append("\" ");
                strXML.append(" vales= \"").append(entidad.getVales()).append("\" ");
                strXML.append(" valesSuma= \"").append(entidad.getVales()).append("\" ");
                strXML.append(" ayudas= \"").append(entidad.getAyudas()).append("\" ");
                strXML.append(" ayudasSuma= \"").append(entidad.getAyudas()).append("\" ");
                strXML.append(" contribucuionesTrabajadorPagadasPatron= \"").append(entidad.getContribucuionesTrabajadorPagadasPatron()).append("\" ");
                strXML.append(" contribucuionesTrabajadorPagadasPatronSuma= \"").append(entidad.getContribucuionesTrabajadorPagadasPatron()).append("\" ");
                strXML.append(" vacaciones= \"").append(entidad.getVacaciones()).append("\" ");
                strXML.append(" vacacionesSuma= \"").append(entidad.getVacaciones()).append("\" ");
                strXML.append(" primaSeguroVida= \"").append(entidad.getPrimaSeguroVida()).append("\" ");
                strXML.append(" primaSeguroVidaSuma= \"").append(entidad.getPrimaSeguroVida()).append("\" ");
                strXML.append(" seguroGastosMedicosMayores= \"").append(entidad.getSeguroGastosMedicosMayores()).append("\" ");
                strXML.append(" seguroGastosMedicosMayoresSuma= \"").append(entidad.getSeguroGastosMedicosMayores()).append("\" ");
                strXML.append(" cuotasSindicalesPagadasPatron= \"").append(entidad.getCuotasSindicalesPagadasPatron()).append("\" ");
                strXML.append(" cuotasSindicalesPagadasPatronSuma= \"").append(entidad.getCuotasSindicalesPagadasPatron()).append("\" ");
                strXML.append(" subsidioIncapacidad= \"").append(entidad.getSubsidioIncapacidad()).append("\" ");
                strXML.append(" subsidioIncapacidadSuma= \"").append(entidad.getSubsidioIncapacidad()).append("\" ");
                strXML.append(" becasTrabajadoresHijos= \"").append(entidad.getBecasTrabajadoresHijos()).append("\" ");
                strXML.append(" becasTrabajadoresHijosSuma= \"").append(entidad.getBecasTrabajadoresHijos()).append("\" ");
                strXML.append(" otros= \"").append(entidad.getOtros()).append("\" ");
                strXML.append(" otrosSuma= \"").append(entidad.getOtros()).append("\" ");
                strXML.append(" subsidioAlEmpleo= \"").append(entidad.getSubsidioAlEmpleo()).append("\" ");
                strXML.append(" subsidioAlEmpleoSuma= \"").append(entidad.getSubsidioAlEmpleo()).append("\" ");
                strXML.append(" fomentoPrimerEmpleo= \"").append(entidad.getFomentoPrimerEmpleo()).append("\" ");
                strXML.append(" fomentoPrimerEmpleoSuma= \"").append(entidad.getFomentoPrimerEmpleo()).append("\" ");
                strXML.append(" horasExtras= \"").append(entidad.getHorasExtras()).append("\" ");
                strXML.append(" horasExtrasSuma= \"").append(entidad.getHorasExtras()).append("\" ");
                strXML.append(" primaDominical= \"").append(entidad.getPrimaDominical()).append("\" ");
                strXML.append(" primaDominicalSuma= \"").append(entidad.getPrimaDominical()).append("\" ");
                strXML.append(" primaVacacional= \"").append(entidad.getPrimaVacacional()).append("\" ");
                strXML.append(" primaVacacionalSuma= \"").append(entidad.getPrimaVacacional()).append("\" ");
                strXML.append(" primaAntiguedad= \"").append(entidad.getPrimaAntiguedad()).append("\" ");
                strXML.append(" primaAntiguedadSuma= \"").append(entidad.getPrimaAntiguedad()).append("\" ");
                strXML.append(" pagoSeparacion= \"").append(entidad.getPagoSeparacion()).append("\" ");
                strXML.append(" pagoSeparacionSuma= \"").append(entidad.getPagoSeparacion()).append("\" ");
                strXML.append(" seguroRetiro= \"").append(entidad.getSeguroRetiro()).append("\" ");
                strXML.append(" seguroRetiroSuma= \"").append(entidad.getSeguroRetiro()).append("\" ");
                strXML.append(" imdemnizaciones= \"").append(entidad.getImdemnizaciones()).append("\" ");
                strXML.append(" imdemnizacionesSuma= \"").append(entidad.getImdemnizaciones()).append("\" ");
                strXML.append(" reembolsoFuneral= \"").append(entidad.getReembolsoFuneral()).append("\" ");
                strXML.append(" reembolsoFuneralSuma= \"").append(entidad.getReembolsoFuneral()).append("\" ");
                strXML.append(" cuotasSeguridadSocialPagadas= \"").append(entidad.getCuotasSeguridadSocialPagadas()).append("\" ");
                strXML.append(" cuotasSeguridadSocialPagadasSuma= \"").append(entidad.getCuotasSeguridadSocialPagadas()).append("\" ");
                strXML.append(" comisiones= \"").append(entidad.getComisiones()).append("\" ");
                strXML.append(" comisionesSuma= \"").append(entidad.getComisiones()).append("\" ");
                strXML.append(" anticipoCuentaUtilidades= \"").append(entidad.getAnticipoCuentaUtilidades()).append("\" ");
                strXML.append(" anticipoCuentaUtilidadesSuma= \"").append(entidad.getAnticipoCuentaUtilidades()).append("\" ");
                strXML.append(" premioPuntualidad= \"").append(entidad.getPremioPuntualidad()).append("\" ");
                strXML.append(" premioPuntualidadSuma= \"").append(entidad.getPremioPuntualidad()).append("\" ");
                strXML.append(" premioAsistencia= \"").append(entidad.getPremioAsistencia()).append("\" ");
                strXML.append(" premioAsistenciaSuma= \"").append(entidad.getPremioAsistencia()).append("\" ");
                strXML.append(" bonoyGratificaciones= \"").append(entidad.getBonoyGratificaciones()).append("\" ");
                strXML.append(" bonoyGratificacionesSuma= \"").append(entidad.getBonoyGratificaciones()).append("\" ");
                strXML.append(" diaFestivo= \"").append(entidad.getDiaFestivo()).append("\" ");
                strXML.append(" diaFestivoSuma= \"").append(entidad.getDiaFestivo()).append("\" ");
                strXML.append(" bonoDeCumplimiento= \"").append(entidad.getBonoDeCumplimiento()).append("\" ");
                strXML.append(" bonoDeCumplimientoSuma= \"").append(entidad.getBonoDeCumplimiento()).append("\" ");
                strXML.append(" retroActivo= \"").append(entidad.getRetroActivo()).append("\" ");
                strXML.append(" retroActivoSuma= \"").append(entidad.getRetroActivo()).append("\" ");

                strXML.append(" numerotrabajadores= \"").append(intNumeroEmpleados).append("\" ");
            }

            strXML.append("/>");
        }
        strXML.append("</PreNomina>");
        return strXML.toString();
    }

    public void RepoPreNomina_HacerReporte_ISN() {

        log.debug("///////////////////////////  COMENZAMOS A CREAR EL REPORTE /////////////////////////////////////");

        /*Es solamente un numero consecutivo para el campo ConsNom*/
        int intConsNom = 0;

        /*Aqui obtenemos los Id,clave de las percepciones y deducciones */
        PercepcionesDeducciones PercDedu = new PercepcionesDeducciones(oConn, emp_id);
        Map<String, PercepcionesDeduccionesE> HashMapPercepciones = PercDedu.ObtenPercepciones();
        Map<String, PercepcionesDeduccionesE> HashMapDeducciones = PercDedu.ObtenDeducciones();

        /* Primer ciclo Obtenemos datos de las tablas rhh_nominas_master,rhh_nominas,rhh_empleados*/
        String strSql = "select rhh_nominas.EMP_NUM,rhh_nominas_master.RHN_ID AS  NOM_ID,SUM(rhh_nominas_master.RHN_NUMERO_DIAS) AS RHN_NUMERO_DIAS,SUM(rhh_nominas.NOM_PERCEPCION_TOTAL) AS NOM_PERCEPCION_TOTAL, "
                + "rhh_empleados.EMP_CLABE,rhh_empleados.EMP_NOMBRE,SUM(rhh_empleados.EMP_SALARIO_DIARIO) AS EMP_SALARIO_DIARIO, "
                + " SUM(rhh_empleados.EMP_SALARIO_INTEGRADO) AS EMP_SALARIO_INTEGRADO,SUM(rhh_nominas.NOM_PERCEPCIONES) AS NOM_PERCEPCIONES,SUM(rhh_nominas.NOM_DEDUCCIONES) AS NOM_DEDUCCIONES,"
                + " rhh_nominas.NOM_FOLIO,(select DP_DESCRIPCION from rhh_departamento where rhh_empleados.DP_ID = rhh_departamento.DP_ID) as centroDeCostos  "
                + " from rhh_nominas_master,rhh_nominas,rhh_empleados "
                + " where rhh_nominas_master.RHN_ID = rhh_nominas.RHN_ID  and rhh_nominas.EMP_NUM = rhh_empleados.EMP_NUM and rhh_nominas_master.RHN_ID =" + intTipoNom + " "
                + " group by rhh_nominas.EMP_NUM";

        ResultSet rs;
        try {
            rs = oConn.runQuery(strSql, true);
            while (rs.next()) {

                Double Sueldo = 0.0;
                Double Aguinaldo = 0.0;
                Double Ptu = 0.0;
                Double ReembolsoGastosMedicosDentHosp = 0.0;
                Double AportacionFondoDeAhorro = 0.0;
                Double CajaAhorro = 0.0;
                Double Vales = 0.0;
                Double Ayudas = 0.0;
                Double ContribucuionesTrabajadorPagadasPatron = 0.0;
                Double Vacaciones = 0.0;
                Double PrimaSeguroVida = 0.0;
                Double SeguroGastosMedicosMayores = 0.0;
                Double CuotasSindicalesPagadasPatron = 0.0;
                Double SubsidioIncapacidad = 0.0;
                Double BecasTrabajadoresHijos = 0.0;
                Double Otros = 0.0;
                Double SubsidioAlEmpleo = 0.0;
                Double FomentoPrimerEmpleo = 0.0;
                Double HorasExtras = 0.0;
                Double PrimaDominical = 0.0;
                Double PrimaVacacional = 0.0;
                Double PrimaAntiguedad = 0.0;
                Double PagoSeparacion = 0.0;
                Double SeguroRetiro = 0.0;
                Double Imdemnizaciones = 0.0;
                Double ReembolsoFuneral = 0.0;
                Double CuotasSeguridadSocialPagadas = 0.0;
                Double Comisiones = 0.0;
                Double AnticipoCuentaUtilidades = 0.0;
                Double PremioPuntualidad = 0.0;
                Double PremioAsistencia = 0.0;
                Double BonoyGratificaciones = 0.0;
                Double DiaFestivo = 0.0;
                Double BonoDeCumplimiento = 0.0;
                Double RetroActivo = 0.0;

                Double PensionAlimenticia = 0.0;
                Double IsrRetenido = 0.0;
                Double Imss = 0.0;
                Double RetencionInfonavit = 0.0;
                Double OtrosDescuentos = 0.0;
                Double OtrosAnticipos = 0.0;

                int FaltaJustificada = 0;
                int FaltaInjustificada = 0;
                int Incapacidad = 0;
                Double Retardos = 0.0;
                int DomingosTrabajados = 0;

                Double Gravado = 0.0;
                Double Exento = 0.0;

                Double DiaFestivoGravado = 0.0;
                Double PrimaDomicicalGravado = 0.0;
                Double AguinaldoGravado = 0.0;
                Double PrimaVacacionalGravado = 0.0;

                Double DiaFestivoExento = 0.0;
                Double PrimaDominicalExento = 0.0;
                Double AguinaldoExento = 0.0;
                Double PrimaVacacionalExento = 0.0;

                Double ImporteBone = 0.0;
                Double AportacionFondoDeAhorroEmpresa = 0.0;
                Double DiasDeDescansoTrabajados = 0.0;
                Double DiasDeDescansoGravado = 0.0;
                Double DiasDeDescansoExento = 0.0;

                int intNom_ID1 = rs.getInt("NOM_ID");
                int intEmp_NUM1 = rs.getInt("EMP_NUM");
                log.debug("///////////////////////////  Nomina = " + intNom_ID1 + " /////////////////////////////////////");

                RepoPreNominaE e = new RepoPreNominaE();

                e.setDiasTrabajados(rs.getString("RHN_NUMERO_DIAS"));
                e.setTotalPercepciones(rs.getInt("NOM_PERCEPCIONES"));
                e.setTotalDeducciones(rs.getInt("NOM_DEDUCCIONES"));
                e.setNetoApagar(rs.getInt("NOM_PERCEPCION_TOTAL"));
                e.setClave(rs.getString("EMP_CLABE"));
                e.setNombre(rs.getString("EMP_NOMBRE"));
                e.setSdi(rs.getInt("EMP_SALARIO_DIARIO"));
                e.setSdn(rs.getInt("EMP_SALARIO_INTEGRADO"));
                intConsNom++;
                e.setConsNom(String.valueOf(intConsNom));
                e.setCentroDeCostos(rs.getString("centroDeCostos"));
                e.setNoDeRecibido(rs.getInt("NOM_FOLIO"));

                String strSql2 = "select rhh_nominas.EMP_NUM,rhh_nominas.NOM_ID,rhh_nominas_master.RHN_NUMERO_DIAS,rhh_nominas.NOM_PERCEPCION_TOTAL,rhh_empleados.EMP_CLABE,rhh_empleados.EMP_NOMBRE,rhh_empleados.EMP_SALARIO_DIARIO,rhh_empleados.EMP_SALARIO_INTEGRADO,rhh_nominas.NOM_PERCEPCIONES,rhh_nominas.NOM_DEDUCCIONES,rhh_nominas.NOM_FOLIO, "
                        + " (select DP_DESCRIPCION from rhh_departamento where rhh_empleados.DP_ID = rhh_departamento.DP_ID) as centroDeCostos "
                        + " from rhh_nominas_master,rhh_nominas,rhh_empleados "
                        + " where rhh_nominas_master.RHN_ID = rhh_nominas.RHN_ID "
                        + " and rhh_nominas.EMP_NUM = rhh_empleados.EMP_NUM "
                        + " and rhh_nominas_master.RHN_ID = " + intTipoNom + " and rhh_nominas.EMP_NUM =" + intEmp_NUM1;
                ResultSet rs2;
                try {
                    rs2 = oConn.runQuery(strSql2, true);
                    while (rs2.next()) {

                        ArrayList<Integer> arrPercepciones = new ArrayList<Integer>();
                        ArrayList<Integer> arrDeducciones = new ArrayList<Integer>();

                        int intNom_ID = rs2.getInt("NOM_ID");
                        int intEmp_NUM = rs2.getInt("EMP_NUM");

                        log.debug("ID Nomina=" + intNom_ID);
                        log.debug("NUM Empleado=" + intEmp_NUM);

                        /*Obtenemos Los Id de las percepciones de la nomia actual y las guardamos en un arrayList*/
                        String strSql1 = "select DISTINCT PERC_ID"
                                + " from rhh_nominas_deta"
                                + " where NOM_ID =" + intNom_ID;
                        ResultSet rs1;
                        try {
                            rs1 = oConn.runQuery(strSql1, true);
                            while (rs1.next()) {
                                if (rs1.getInt("PERC_ID") != 0) {
                                    arrPercepciones.add(rs1.getInt("PERC_ID"));
                                }
                            }
                            rs1.close();
                        } catch (SQLException ex) {
                            log.error(ex);
                        }
                        /*Obtenemos Los Id de las deducciones de la nomia actual y las guardamos en un arrayList*/
                        String strSql33 = "select DISTINCT DEDU_ID"
                                + " from rhh_nominas_deta"
                                + " where NOM_ID =" + intNom_ID;
                        ResultSet rs33;
                        try {
                            rs33 = oConn.runQuery(strSql33, true);
                            while (rs33.next()) {
                                if (rs33.getInt("DEDU_ID") != 0) {
                                    arrDeducciones.add(rs33.getInt("DEDU_ID"));
                                }
                            }
                            rs33.close();
                        } catch (SQLException ex) {
                            log.error(ex);
                        }

                        log.debug("Numero de Percepciones=" + arrPercepciones.size());
                        log.debug("Numero de Deducciones=" + arrDeducciones.size());

                        log.debug("-----------------------------------------Percepciones de la nomina");
                        /*En este ciclo obtenemos los datos de nomina_deta Percepciones*/
                        for (int i = 0; i < arrPercepciones.size(); i++) {
                            String strSql3 = "select NOMD_UNITARIO"
                                    + " from rhh_nominas_deta"
                                    + " where NOM_ID =" + intNom_ID + " and PERC_ID= " + arrPercepciones.get(i) + " ;";
                            ResultSet rs3;
                            try {
                                rs3 = oConn.runQuery(strSql3, true);
                                while (rs3.next()) {

                                    if (arrPercepciones.get(i) == HashMapPercepciones.get("1").getIntIdPercDedu()) {
                                        Sueldo = Sueldo + rs3.getDouble("NOMD_UNITARIO");

                                        log.debug("Sueldos, Salarios Rayas y Jornales " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("1").getIntIdPercDedu() + " Valor:" + rs3.getDouble("NOMD_UNITARIO") + " Suma:" + Sueldo);

                                    }
                                    if (arrPercepciones.get(i) == HashMapPercepciones.get("2").getIntIdPercDedu()) {
                                        Aguinaldo = Aguinaldo + rs3.getDouble("NOMD_UNITARIO");
                                        log.debug("Gratificación Anual (Aguinaldo) " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("2").getIntIdPercDedu() + " Valor:" + rs3.getDouble("NOMD_UNITARIO") + " Suma:" + Aguinaldo);
                                    }
                                    if (arrPercepciones.get(i) == HashMapPercepciones.get("3").getIntIdPercDedu()) {
                                        Ptu = Ptu + rs3.getDouble("NOMD_UNITARIO");
                                        log.debug("Participación de los Trabajadores en las Utilidades PTU " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("3").getIntIdPercDedu() + " Valor:" + rs3.getDouble("NOMD_UNITARIO") + " Suma:" + Ptu);
                                    }
                                    if (arrPercepciones.get(i) == HashMapPercepciones.get("4").getIntIdPercDedu()) {
                                        ReembolsoGastosMedicosDentHosp = ReembolsoGastosMedicosDentHosp + rs3.getDouble("NOMD_UNITARIO");
                                        log.debug("Reembolso de Gastos Médicos Dentales y Hospitalarios " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("4").getIntIdPercDedu() + " Valor:" + rs3.getDouble("NOMD_UNITARIO") + " Suma:" + ReembolsoGastosMedicosDentHosp);
                                    }
                                    if (arrPercepciones.get(i) == HashMapPercepciones.get("5").getIntIdPercDedu()) {
                                        AportacionFondoDeAhorro = AportacionFondoDeAhorro + rs3.getDouble("NOMD_UNITARIO");
                                        log.debug("Fondo de Ahorro " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("5").getIntIdPercDedu() + " Valor:" + rs3.getDouble("NOMD_UNITARIO") + " Suma:" + AportacionFondoDeAhorro);
                                    }
                                    if (arrPercepciones.get(i) == HashMapPercepciones.get("6").getIntIdPercDedu()) {
                                        CajaAhorro = CajaAhorro + rs3.getDouble("NOMD_UNITARIO");
                                        log.debug("Caja de ahorro " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("6").getIntIdPercDedu() + " Valor:" + rs3.getDouble("NOMD_UNITARIO") + " Suma:" + CajaAhorro);
                                    }
                                    if (arrPercepciones.get(i) == HashMapPercepciones.get("7").getIntIdPercDedu()) {
                                        Vales = Vales + rs3.getDouble("NOMD_UNITARIO");
                                        log.debug("Vales " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("7").getIntIdPercDedu() + " Valor:" + rs3.getDouble("NOMD_UNITARIO") + " Suma:" + Vales);
                                    }
                                    if (arrPercepciones.get(i) == HashMapPercepciones.get("8").getIntIdPercDedu()) {
                                        Ayudas = Ayudas + rs3.getDouble("NOMD_UNITARIO");
                                        log.debug("Ayudas " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("8").getIntIdPercDedu() + " Valor:" + rs3.getDouble("NOMD_UNITARIO") + " Suma:" + Ayudas);
                                    }
                                    if (arrPercepciones.get(i) == HashMapPercepciones.get("9").getIntIdPercDedu()) {
                                        ContribucuionesTrabajadorPagadasPatron = ContribucuionesTrabajadorPagadasPatron + rs3.getDouble("NOMD_UNITARIO");
                                        log.debug("Contribuciones a Cargo del Trabajador Pagadas por el Patrón " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("9").getIntIdPercDedu() + " Valor:" + rs3.getDouble("NOMD_UNITARIO") + " Suma:" + ContribucuionesTrabajadorPagadasPatron);
                                    }
                                    if (arrPercepciones.get(i) == HashMapPercepciones.get("30").getIntIdPercDedu()) {
                                        Vacaciones = Vacaciones + rs3.getDouble("NOMD_UNITARIO");
                                        log.debug("Vacaciones " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("30").getIntIdPercDedu() + " Valor:" + rs3.getDouble("NOMD_UNITARIO") + " Suma:" + Vacaciones);
                                    }
                                    if (arrPercepciones.get(i) == HashMapPercepciones.get("11").getIntIdPercDedu()) {
                                        PrimaSeguroVida = PrimaSeguroVida + rs3.getDouble("NOMD_UNITARIO");
                                        log.debug("Prima de Seguro de vida " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("11").getIntIdPercDedu() + " Valor:" + rs3.getDouble("NOMD_UNITARIO") + " Suma:" + PrimaSeguroVida);
                                    }
                                    if (arrPercepciones.get(i) == HashMapPercepciones.get("12").getIntIdPercDedu()) {
                                        SeguroGastosMedicosMayores = SeguroGastosMedicosMayores + rs3.getDouble("NOMD_UNITARIO");
                                        log.debug("Seguro de Gastos Medicos Mayores " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("12").getIntIdPercDedu() + " Valor:" + rs3.getDouble("NOMD_UNITARIO") + " Suma:" + SeguroGastosMedicosMayores);
                                    }
                                    if (arrPercepciones.get(i) == HashMapPercepciones.get("13").getIntIdPercDedu()) {
                                        CuotasSindicalesPagadasPatron = CuotasSindicalesPagadasPatron + rs3.getDouble("NOMD_UNITARIO");
                                        log.debug("Cuotas Sindicales Pagadas por el Patrón " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("13").getIntIdPercDedu() + " Valor:" + rs3.getDouble("NOMD_UNITARIO") + " Suma:" + CuotasSindicalesPagadasPatron);
                                    }
                                    if (arrPercepciones.get(i) == HashMapPercepciones.get("14").getIntIdPercDedu()) {
                                        SubsidioIncapacidad = SubsidioIncapacidad + rs3.getDouble("NOMD_UNITARIO");
                                        log.debug("Subsidios por incapacidad " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("14").getIntIdPercDedu() + " Valor:" + rs3.getDouble("NOMD_UNITARIO") + " Suma:" + SubsidioIncapacidad);
                                    }
                                    if (arrPercepciones.get(i) == HashMapPercepciones.get("15").getIntIdPercDedu()) {
                                        BecasTrabajadoresHijos = BecasTrabajadoresHijos + rs3.getDouble("NOMD_UNITARIO");
                                        log.debug("Becas para trabajadores y/o hijos " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("15").getIntIdPercDedu() + " Valor:" + rs3.getDouble("NOMD_UNITARIO") + " Suma:" + BecasTrabajadoresHijos);
                                    }
                                    if (arrPercepciones.get(i) == HashMapPercepciones.get("16").getIntIdPercDedu()) {
                                        Otros = Otros + rs3.getDouble("NOMD_UNITARIO");
                                        log.debug("Otros " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("16").getIntIdPercDedu() + " Valor:" + rs3.getDouble("NOMD_UNITARIO") + " Suma:" + Otros);
                                    }
                                    if (arrPercepciones.get(i) == HashMapPercepciones.get("17").getIntIdPercDedu()) {
                                        SubsidioAlEmpleo = SubsidioAlEmpleo + rs3.getDouble("NOMD_UNITARIO");
                                        log.debug("Subsidio para el empleo " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("17").getIntIdPercDedu() + " Valor:" + rs3.getDouble("NOMD_UNITARIO") + " Suma:" + SubsidioAlEmpleo);
                                    }
                                    if (arrPercepciones.get(i) == HashMapPercepciones.get("18").getIntIdPercDedu()) {
                                        FomentoPrimerEmpleo = FomentoPrimerEmpleo + rs3.getDouble("NOMD_UNITARIO");
                                        log.debug("Fomento al primer empleo " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("18").getIntIdPercDedu() + " Valor:" + rs3.getDouble("NOMD_UNITARIO") + " Suma:" + FomentoPrimerEmpleo);
                                    }
                                    if (arrPercepciones.get(i) == HashMapPercepciones.get("19").getIntIdPercDedu()) {
                                        HorasExtras = HorasExtras + rs3.getDouble("NOMD_UNITARIO");
                                        log.debug("Horas extra " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("19").getIntIdPercDedu() + " Valor:" + rs3.getDouble("NOMD_UNITARIO") + " Suma:" + HorasExtras);
                                    }
                                    if (arrPercepciones.get(i) == HashMapPercepciones.get("20").getIntIdPercDedu()) {
                                        PrimaDominical = PrimaDominical + rs3.getDouble("NOMD_UNITARIO");
                                        log.debug("Prima dominical " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("20").getIntIdPercDedu() + " Valor:" + rs3.getDouble("NOMD_UNITARIO") + " Suma:" + PrimaDominical);
                                    }
                                    if (arrPercepciones.get(i) == HashMapPercepciones.get("21").getIntIdPercDedu()) {
                                        PrimaVacacional = PrimaVacacional + rs3.getDouble("NOMD_UNITARIO");
                                        log.debug("Prima vacacional " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("21").getIntIdPercDedu() + " Valor:" + rs3.getDouble("NOMD_UNITARIO") + " Suma:" + PrimaVacacional);
                                    }
                                    if (arrPercepciones.get(i) == HashMapPercepciones.get("22").getIntIdPercDedu()) {
                                        PrimaAntiguedad = PrimaAntiguedad + rs3.getDouble("NOMD_UNITARIO");
                                        log.debug("Prima por antigüedad " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("22").getIntIdPercDedu() + " Valor:" + rs3.getDouble("NOMD_UNITARIO") + " Suma:" + PrimaAntiguedad);
                                    }
                                    if (arrPercepciones.get(i) == HashMapPercepciones.get("23").getIntIdPercDedu()) {
                                        PagoSeparacion = PagoSeparacion + rs3.getDouble("NOMD_UNITARIO");
                                        log.debug("Pagos por separación " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("23").getIntIdPercDedu() + " Valor:" + rs3.getDouble("NOMD_UNITARIO") + " Suma:" + PagoSeparacion);
                                    }
                                    if (arrPercepciones.get(i) == HashMapPercepciones.get("24").getIntIdPercDedu()) {
                                        SeguroRetiro = SeguroRetiro + rs3.getDouble("NOMD_UNITARIO");
                                        log.debug("Seguro de retiro " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("24").getIntIdPercDedu() + " Valor:" + rs3.getDouble("NOMD_UNITARIO") + " Suma:" + SeguroRetiro);
                                    }
                                    if (arrPercepciones.get(i) == HashMapPercepciones.get("25").getIntIdPercDedu()) {
                                        Imdemnizaciones = Imdemnizaciones + rs3.getDouble("NOMD_UNITARIO");
                                        log.debug("Indeminizaciones " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("25").getIntIdPercDedu() + " Valor:" + rs3.getDouble("NOMD_UNITARIO") + " Suma:" + Imdemnizaciones);
                                    }
                                    if (arrPercepciones.get(i) == HashMapPercepciones.get("26").getIntIdPercDedu()) {
                                        ReembolsoFuneral = ReembolsoFuneral + rs3.getDouble("NOMD_UNITARIO");
                                        log.debug("Reembolso por funeral " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("26").getIntIdPercDedu() + " Valor:" + rs3.getDouble("NOMD_UNITARIO") + " Suma:" + ReembolsoFuneral);
                                    }
                                    if (arrPercepciones.get(i) == HashMapPercepciones.get("27").getIntIdPercDedu()) {
                                        CuotasSeguridadSocialPagadas = CuotasSeguridadSocialPagadas + rs3.getDouble("NOMD_UNITARIO");
                                        log.debug("Cuotas de seguridad social pagadas\n"
                                                + "por el patrón " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("27").getIntIdPercDedu() + " Valor:" + rs3.getDouble("NOMD_UNITARIO") + " Suma:" + CuotasSeguridadSocialPagadas);
                                    }
                                    if (arrPercepciones.get(i) == HashMapPercepciones.get("28").getIntIdPercDedu()) {
                                        Comisiones = Comisiones + rs3.getDouble("NOMD_UNITARIO");
                                        log.debug("Comisiones " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("28").getIntIdPercDedu() + " Valor:" + rs3.getDouble("NOMD_UNITARIO") + " Suma:" + Comisiones);
                                    }
                                    if (arrPercepciones.get(i) == HashMapPercepciones.get("29").getIntIdPercDedu()) {
                                        AnticipoCuentaUtilidades = AnticipoCuentaUtilidades + rs3.getDouble("NOMD_UNITARIO");
                                        log.debug("Anticipo a Cuenta de Utilidades " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("29").getIntIdPercDedu() + " Valor:" + rs3.getDouble("NOMD_UNITARIO") + " Suma:" + AnticipoCuentaUtilidades);
                                    }
                                    if (arrPercepciones.get(i) == HashMapPercepciones.get("31").getIntIdPercDedu()) {
                                        PremioPuntualidad = PremioPuntualidad + rs3.getDouble("NOMD_UNITARIO");
                                        log.debug("Premio de puntualidad " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("31").getIntIdPercDedu() + " Valor:" + rs3.getDouble("NOMD_UNITARIO") + " Suma:" + PremioPuntualidad);
                                    }
                                    if (arrPercepciones.get(i) == HashMapPercepciones.get("32").getIntIdPercDedu()) {
                                        PremioAsistencia = PremioAsistencia + rs3.getDouble("NOMD_UNITARIO");
                                        log.debug("Premio de asistencia " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("32").getIntIdPercDedu() + " Valor:" + rs3.getDouble("NOMD_UNITARIO") + " Suma:" + PremioAsistencia);
                                    }
                                    if (arrPercepciones.get(i) == HashMapPercepciones.get("33").getIntIdPercDedu()) {
                                        BonoyGratificaciones = BonoyGratificaciones + rs3.getDouble("NOMD_UNITARIO");
                                        log.debug("Bono y gratificaciones " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("33").getIntIdPercDedu() + " Valor:" + rs3.getDouble("NOMD_UNITARIO") + " Suma:" + BonoyGratificaciones);
                                    }
                                    if (arrPercepciones.get(i) == HashMapPercepciones.get("34").getIntIdPercDedu()) {
                                        DiaFestivo = DiaFestivo + rs3.getDouble("NOMD_UNITARIO");
                                        log.debug("Dia Festivo " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("34").getIntIdPercDedu() + " Valor:" + rs3.getDouble("NOMD_UNITARIO") + " Suma:" + DiaFestivo);
                                    }
                                    if (arrPercepciones.get(i) == HashMapPercepciones.get("35").getIntIdPercDedu()) {
                                        BonoDeCumplimiento = BonoDeCumplimiento + rs3.getDouble("NOMD_UNITARIO");
                                        log.debug("Bono de cumplimiento " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("35").getIntIdPercDedu() + " Valor:" + rs3.getDouble("NOMD_UNITARIO") + " Suma:" + BonoDeCumplimiento);
                                    }
                                    if (arrPercepciones.get(i) == HashMapPercepciones.get("36").getIntIdPercDedu()) {
                                        RetroActivo = RetroActivo + rs3.getDouble("NOMD_UNITARIO");
                                        log.debug("Retroactivo " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("36").getIntIdPercDedu() + " Valor:" + rs3.getDouble("NOMD_UNITARIO") + " Suma:" + RetroActivo);
                                    }

                                }
                                rs3.close();
                            } catch (SQLException ex) {
                                log.error(ex);
                            }
                        }
                        /*En este ciclo obtenemos los datos de nomina_deta Deducciones*/
                        log.debug("-----------------------------------------Deducciones de la nomina");
                        for (int i = 0; i < arrDeducciones.size(); i++) {
                            String strSql3 = "select NOMD_UNITARIO"
                                    + " from rhh_nominas_deta"
                                    + " where NOM_ID =" + intNom_ID + " and DEDU_ID= " + arrDeducciones.get(i) + " ;";
                            ResultSet rs3;
                            try {
                                rs3 = oConn.runQuery(strSql3, true);
                                while (rs3.next()) {
                                    if (arrDeducciones.get(i) == HashMapDeducciones.get("7").getIntIdPercDedu()) {
                                        PensionAlimenticia = PensionAlimenticia + rs3.getDouble("NOMD_UNITARIO");
                                        log.debug("Pensión alimenticia " + arrDeducciones.get(i) + " == " + HashMapDeducciones.get("7").getIntIdPercDedu());
                                    }
                                    if (arrDeducciones.get(i) == HashMapDeducciones.get("2").getIntIdPercDedu()) {
                                        IsrRetenido = IsrRetenido + rs3.getDouble("NOMD_UNITARIO");
                                        log.debug("Retención de ISR " + arrDeducciones.get(i) + " == " + HashMapDeducciones.get("2").getIntIdPercDedu());
                                    }
                                    if (arrDeducciones.get(i) == HashMapDeducciones.get("1").getIntIdPercDedu()) {
                                        Imss = Imss + rs3.getDouble("NOMD_UNITARIO");
                                        log.debug("Seguridad social " + arrDeducciones.get(i) + " == " + HashMapDeducciones.get("1").getIntIdPercDedu());
                                    }
                                    if (arrDeducciones.get(i) == HashMapDeducciones.get("5").getIntIdPercDedu()) {
                                        RetencionInfonavit = RetencionInfonavit + rs3.getDouble("NOMD_UNITARIO");
                                        log.debug("Aportaciones a Fondo de vivienda " + arrDeducciones.get(i) + " == " + HashMapDeducciones.get("5").getIntIdPercDedu());
                                    }
                                    if (arrDeducciones.get(i) == HashMapDeducciones.get("22").getIntIdPercDedu()) {
                                        OtrosDescuentos = OtrosDescuentos + rs3.getDouble("NOMD_UNITARIO");
                                        log.debug("Otros Descuentos " + arrDeducciones.get(i) + " == " + HashMapDeducciones.get("22").getIntIdPercDedu());
                                    }
                                    if (arrDeducciones.get(i) == HashMapDeducciones.get("23").getIntIdPercDedu()) {
                                        OtrosAnticipos = OtrosAnticipos + rs3.getDouble("NOMD_UNITARIO");
                                        log.debug("Otros Anticipos " + arrDeducciones.get(i) + " == " + HashMapDeducciones.get("23").getIntIdPercDedu());
                                    }
                                }
                                rs3.close();
                            } catch (SQLException ex) {
                                log.error(ex);
                            }
                        }

                        log.debug("-----------------------------------------Incidencias");
                        /*En este ciclo obtenemos los datos de rhh_incidencias Incidencias*/
                        String strSql4 = "select *,"
                                + "(select count(RHIN_ID) from rhh_prima_dominical where rhh_incidencias.RHIN_ID = rhh_prima_dominical.RHIN_ID) as domingosTrabajados"
                                + " from rhh_incidencias"
                                + " where RHN_ID =" + intTipoNom + " and EMP_NUM =" + intEmp_NUM;
                        ResultSet rs4;
                        try {
                            rs4 = oConn.runQuery(strSql4, true);
                            while (rs4.next()) {
                                FaltaJustificada = FaltaJustificada + rs4.getInt("RHIN_NUM_FALTAS");
                                FaltaInjustificada = FaltaInjustificada + rs4.getInt("RHIN_NUM_FALTAS");
                                Incapacidad = Incapacidad + rs4.getInt("RHIN_DIAS_INCAPACIDAD");
                                Retardos = Retardos + rs4.getInt("RHIN_RETARDOS");
                                DomingosTrabajados = DomingosTrabajados + rs4.getInt("domingosTrabajados");
                            }
                            rs4.close();

                        } catch (SQLException ex) {
                            log.error(ex);
                        }

                        log.debug("-----------------------------------------Gravado y Excento");
                        /*Aqui obtenemos los datos de Gravado y Excento*/
                        double dblTotalExcento = 0.0;
                        String strSql3 = "select SUM(NOMD_UNITARIO) as excento from rhh_nominas_deta where NOM_ID = " + intNom_ID + " and NOMD_GRAVADO = 0;";
                        ResultSet rs3;
                        try {
                            rs3 = oConn.runQuery(strSql3, true);
                            while (rs3.next()) {
                                dblTotalExcento = rs3.getDouble("excento");
                            }
                            rs3.close();
                        } catch (SQLException ex) {
                            log.error(ex);
                        }
                        log.debug("Total Excento.... " + dblTotalExcento);

                        double dblTotalGravado = 0.0;
                        String strSql5 = "select SUM(NOMD_UNITARIO) as gravado from rhh_nominas_deta where NOM_ID = " + intNom_ID + " and NOMD_GRAVADO = 1;";
                        ResultSet rs5;
                        try {
                            rs5 = oConn.runQuery(strSql5, true);
                            while (rs5.next()) {
                                dblTotalGravado = rs5.getDouble("gravado");
                            }
                            rs5.close();
                        } catch (SQLException ex) {
                            log.error(ex);
                        }
                        log.debug("Total Gravado.... " + dblTotalGravado);

                        Gravado = Gravado + dblTotalGravado;
                        Exento = Exento + dblTotalExcento;

                        log.debug("-----------------------------------------percepciones Gravado");
                        /*Sacamos los datos agravados y excentos de las percepciones*/
                        for (int i = 0; i < arrPercepciones.size(); i++) {
                            String strSql6 = "select NOMD_UNITARIO"
                                    + " from rhh_nominas_deta"
                                    + " where NOM_ID =" + intNom_ID + " and PERC_ID= " + arrPercepciones.get(i) + " and NOMD_GRAVADO=1";
                            ResultSet rs6;
                            try {
                                rs6 = oConn.runQuery(strSql6, true);
                                while (rs6.next()) {
                                    if (arrPercepciones.get(i) == HashMapPercepciones.get("34").getIntIdPercDedu()) {
                                        DiaFestivoGravado = DiaFestivoGravado + rs6.getDouble("NOMD_UNITARIO");
                                        log.debug("Dia Festivo GRAVADO  " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("34").getIntIdPercDedu());
                                    }
                                    if (arrPercepciones.get(i) == HashMapPercepciones.get("20").getIntIdPercDedu()) {
                                        PrimaDomicicalGravado = PrimaDomicicalGravado + rs6.getDouble("NOMD_UNITARIO");
                                        log.debug("Prima dominical GRAVADO  " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("20").getIntIdPercDedu());
                                    }
                                    if (arrPercepciones.get(i) == HashMapPercepciones.get("2").getIntIdPercDedu()) {
                                        AguinaldoGravado = AguinaldoGravado + rs6.getDouble("NOMD_UNITARIO");
                                        log.debug("Gratificación Anual (Aguinaldo) GRAVADO  " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("2").getIntIdPercDedu());
                                    }
                                    if (arrPercepciones.get(i) == HashMapPercepciones.get("21").getIntIdPercDedu()) {
                                        PrimaVacacionalGravado = PrimaVacacionalGravado + rs6.getDouble("NOMD_UNITARIO");
                                        log.debug("Prima vacacional GRAVADO  " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("21").getIntIdPercDedu());
                                    }
                                }
                                rs6.close();
                            } catch (SQLException ex) {
                                log.error(ex);
                            }
                        }
                        log.debug("-----------------------------------------percepciones Excento");
                        for (int i = 0; i < arrPercepciones.size(); i++) {
                            String strSql6 = "select NOMD_UNITARIO"
                                    + " from rhh_nominas_deta"
                                    + " where NOM_ID =" + intNom_ID + " and PERC_ID= " + arrPercepciones.get(i) + " and NOMD_GRAVADO=0";
                            ResultSet rs6;
                            try {
                                rs6 = oConn.runQuery(strSql6, true);
                                while (rs6.next()) {
                                    if (arrPercepciones.get(i) == HashMapPercepciones.get("34").getIntIdPercDedu()) {
                                        DiaFestivoExento = DiaFestivoExento + rs6.getDouble("NOMD_UNITARIO");
                                        log.debug("Dia Festivo Excento  " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("34").getIntIdPercDedu());
                                    }
                                    if (arrPercepciones.get(i) == HashMapPercepciones.get("20").getIntIdPercDedu()) {
                                        PrimaDominicalExento = PrimaDominicalExento + rs6.getDouble("NOMD_UNITARIO");
                                        log.debug("Prima dominical Excento  " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("20").getIntIdPercDedu());
                                    }
                                    if (arrPercepciones.get(i) == HashMapPercepciones.get("2").getIntIdPercDedu()) {
                                        AguinaldoExento = AguinaldoExento + rs6.getDouble("NOMD_UNITARIO");
                                        log.debug("Gratificación Anual (Aguinaldo) Excento  " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("2").getIntIdPercDedu());
                                    }
                                    if (arrPercepciones.get(i) == HashMapPercepciones.get("21").getIntIdPercDedu()) {
                                        PrimaVacacionalExento = PrimaVacacionalExento + rs6.getDouble("NOMD_UNITARIO");
                                        log.debug("Prima vacacional Excento  " + arrPercepciones.get(i) + " == " + HashMapPercepciones.get("21").getIntIdPercDedu());
                                    }
                                }
                                rs6.close();
                            } catch (SQLException ex) {
                                log.error(ex);
                            }
                        }

                        /*Sacamos los datos agravados y excentos de las deducciones*/
                        ImporteBone = ImporteBone + 0;
                        AportacionFondoDeAhorroEmpresa = AportacionFondoDeAhorroEmpresa + 0;
                        DiasDeDescansoTrabajados = DiasDeDescansoTrabajados + 0;
                        DiasDeDescansoGravado = DiasDeDescansoGravado + 0;
                        DiasDeDescansoExento = DiasDeDescansoExento + 0;

                    }
                    rs2.close();

                } catch (SQLException ex) {
                    log.error(ex);
                }

                e.setSueldo(Double.toString(Sueldo));
                e.setAguinaldo(Aguinaldo);
                e.setPtu(Ptu);
                e.setReembolsoGastosMedicosDentHosp(ReembolsoGastosMedicosDentHosp);
                e.setAportacionFondoDeAhorro(AportacionFondoDeAhorro);
                e.setCajaAhorro(CajaAhorro);
                e.setAyudas(Ayudas);
                e.setContribucuionesTrabajadorPagadasPatron(ContribucuionesTrabajadorPagadasPatron);
                e.setVacaciones(Vacaciones);
                e.setPrimaSeguroVida(PrimaSeguroVida);
                e.setSeguroGastosMedicosMayores(SeguroGastosMedicosMayores);
                e.setCuotasSindicalesPagadasPatron(CuotasSindicalesPagadasPatron);
                e.setSubsidioIncapacidad(SubsidioIncapacidad);
                e.setBecasTrabajadoresHijos(BecasTrabajadoresHijos);
                e.setOtros(Otros);
                e.setSubsidioAlEmpleo(SubsidioAlEmpleo);
                e.setFomentoPrimerEmpleo(FomentoPrimerEmpleo);
                e.setHorasExtras(HorasExtras);
                e.setPrimaDominical(PrimaDominical);
                e.setPrimaVacacional(PrimaVacacional);
                e.setPrimaAntiguedad(PrimaAntiguedad);
                e.setPagoSeparacion(PagoSeparacion);
                e.setSeguroRetiro(SeguroRetiro);
                e.setImdemnizaciones(Imdemnizaciones);
                e.setReembolsoFuneral(ReembolsoFuneral);
                e.setCuotasSeguridadSocialPagadas(CuotasSeguridadSocialPagadas);
                e.setComisiones(Comisiones);
                e.setAnticipoCuentaUtilidades(AnticipoCuentaUtilidades);
                e.setPremioPuntualidad(PremioPuntualidad);
                e.setPremioAsistencia(PremioAsistencia);
                e.setBonoyGratificaciones(BonoyGratificaciones);
                e.setDiaFestivo(DiaFestivo);
                e.setBonoDeCumplimiento(Double.toString(BonoDeCumplimiento));
                e.setRetroActivo(RetroActivo);

                e.setPensionAlimenticia(PensionAlimenticia);
                e.setIsrRetenido(IsrRetenido);
                e.setImss(Imss);
                e.setRetencionInfonavit(RetencionInfonavit);
                e.setOtrosDescuentos(OtrosDescuentos);
                e.setOtrosAnticipos(OtrosAnticipos);

                e.setFaltaJustificada(FaltaJustificada);
                e.setFaltaInjustificada(FaltaInjustificada);
                e.setIncapacidad(Integer.toString(Incapacidad));
                e.setRetardos(Double.toString(Retardos));
                e.setDomingosTrabajados(DomingosTrabajados);

                e.setGravado(Gravado);
                e.setExento(Exento);

                e.setDiaFestivoGravado(DiaFestivoGravado);
                e.setPrimaDomicicalGravado(PrimaDomicicalGravado);
                e.setAguinaldoGravado(AguinaldoGravado);
                e.setPrimaVacacionalGravado(PrimaVacacionalGravado);

                e.setDiaFestivoExento(DiaFestivoExento);
                e.setPrimaDominicalExento(PrimaDominicalExento);
                e.setAguinaldoExento(AguinaldoExento);
                e.setPrimaVacacionalExento(PrimaVacacionalExento);

                e.setImporteBone(Double.toString(ImporteBone));
                e.setAportacionFondoDeAhorroEmpresa(AportacionFondoDeAhorroEmpresa);
                e.setDiasDeDescansoTrabajados(DiasDeDescansoTrabajados);
                e.setDiasDeDescansoGravado(DiasDeDescansoGravado);
                e.setDiasDeDescansoExento(DiasDeDescansoExento);

                entidades.add(e);
            }
            rs.close();

        } catch (SQLException ex) {
            log.error(ex);
        }
    }
}
