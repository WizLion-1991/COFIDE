/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.nominas;

import ERP.BusinessEntities.PercepcionesDeduccionesE;
import ERP.CalculaISR;
import ERP.Nominas;
import ERP.PercepcionesDeducciones;
import Tablas.Rhh_Nominas_Master;
import Tablas.rhh_nominas_deta;
import com.mx.siweb.erp.nominas.Entidades.AguinaldoEntidad;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Operaciones.TableMaster;
import comSIWeb.Utilerias.Fechas;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author siweb
 */
public class CalculaAguinaldo {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private Conexion oConn;
   private VariableSession varSesiones;
   private ArrayList<AguinaldoEntidad> lstAguinaldoCalc = new ArrayList<AguinaldoEntidad>();
   private int intDias;
   private int intAnio;
   Fechas fec = new Fechas();
   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(CalculaAguinaldo.class.getName());
    // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Constructor">
   public CalculaAguinaldo(Conexion oConn, VariableSession varSesiones) {
      this.oConn = oConn;
      this.varSesiones = varSesiones;
   }

    // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Métodos">
   public void setoConn(Conexion oConn) {
      this.oConn = oConn;
   }

   public ArrayList<AguinaldoEntidad> getLstAguinaldoCalc() {
      return lstAguinaldoCalc;
   }

   public void setLstAguinaldoCalc(ArrayList<AguinaldoEntidad> lstAguinaldoCalc) {
      this.lstAguinaldoCalc = lstAguinaldoCalc;
   }

   public void setVarSesiones(VariableSession varSesiones) {
      this.varSesiones = varSesiones;
   }

   public Conexion getoConn() {
      return oConn;
   }

   public VariableSession getVarSesiones() {
      return varSesiones;
   }

   public int getIntDias() {
      return intDias;
   }

   public void setIntDias(int intDias) {
      this.intDias = intDias;
   }

   public int getIntAnio() {
      return intAnio;
   }

   public void setIntAnio(int intAnio) {
      this.intAnio = intAnio;
   }

   public void getEmpleados() throws SQLException {
      String strEmpleados = "select * from rhh_empleados where EMP_ACTIVO = '1' and EMP_ID = '" + varSesiones.getIntIdEmpresa() + "';";
      ResultSet rs = oConn.runQuery(strEmpleados);
      while (rs.next()) {
         AguinaldoEntidad agEntidad = new AguinaldoEntidad();
         agEntidad.setIntEmpNum(rs.getInt("EMP_NUM"));
         agEntidad.setStrEmpNombre(rs.getString("EMP_NOMBRE"));
         agEntidad.setStrFecIngreso(rs.getString("EMP_INGRESO"));
         agEntidad.setDblSueldoDia(rs.getDouble("EMP_SALARIO_INTEGRADO"));
         agEntidad.setStrPeriodicidadPago(rs.getString("EMP_PERIODICIDAD_PAGO"));
         lstAguinaldoCalc.add(agEntidad);
      }
   }//Fin getEmpleados

   public double calculaAg(double dblSalario) throws SQLException {
      double dblImporte = 0.0;
      dblImporte = dblSalario * getIntDias();
      return dblImporte;
   }//Fin calculaAg

   public double calculaDiasTrabajados(int EMP_NUM) throws SQLException {
      int intAnio = fec.getAnioActual();
      double totalAnios = 0;
      String strQuery = "select EMP_NUM,EMP_ID,sum(NOM_NUM_DIAS_PAGADOS) as totalDias from rhh_nominas where EMP_NUM = '" + EMP_NUM + "' and EMP_ID = '" + varSesiones.getIntIdEmpresa() + "' and left(NOM_FECHA,4) = '" + getIntAnio() + "';";
      ResultSet rs = oConn.runQuery(strQuery);
      while (rs.next()) {
         totalAnios += rs.getDouble("totalDias");
      }
      rs.close();
      return totalAnios;
   }

   public String getXML() throws SQLException {
      CalculaISR calISR = new CalculaISR(oConn, varSesiones);
      Iterator<AguinaldoEntidad> it = this.lstAguinaldoCalc.iterator();
      StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
      strXML.append("<AguinaldoEmp>");
      while (it.hasNext()) {
         AguinaldoEntidad entidad = it.next();
         double dblSueldoEmp = entidad.getDblSueldoDia();
         String strPeriodo = entidad.getStrPeriodicidadPago();
         double dblAguinaldo = calculaAg(dblSueldoEmp);
         calISR.setDblGravados(dblSueldoEmp);
         double dblISR = calISR.calculaImporte(strPeriodo);
         double dblImpNeto = dblAguinaldo - dblISR;
         strXML.append(" <Empleado");
         strXML.append(" GRNUM_EMP= \"").append(entidad.getIntEmpNum()).append("\" ");
         strXML.append(" GRNOM_EMP= \"").append(entidad.getStrEmpNombre()).append("\" ");
         strXML.append(" GRDIAS_TRAB= \"").append(calculaDiasTrabajados(entidad.getIntEmpNum())).append("\" ");
         strXML.append(" GRSUELDO_DIARIO= \"").append(dblSueldoEmp).append("\" ");
         strXML.append(" GRAGUINALDO= \"").append(dblAguinaldo).append("\" ");
         strXML.append(" GRIMPUESTO= \"").append(dblISR).append("\" ");
         strXML.append(" GRPRECEP_NETA= \"").append(dblImpNeto).append("\" ");
         strXML.append("/>");
         entidad.setDblAguinaldo(dblAguinaldo);
         entidad.setDblImpuesto(dblISR);
         entidad.setDblImporteNeto(dblImpNeto);
      }
      strXML.append("</AguinaldoEmp>");
      return strXML.toString();
   }//Fin getXML

   public String CalculaNomina(ArrayList<AguinaldoEntidad> entidades) {
      PercepcionesDeducciones PercDedu = new PercepcionesDeducciones(oConn, varSesiones.getIntIdEmpresa());
      Map<String, PercepcionesDeduccionesE> HashMapPercepciones = PercDedu.ObtenPercepciones();
      Map<String, PercepcionesDeduccionesE> HashMapDeducciones = PercDedu.ObtenDeducciones();

      log.debug("//////////////GUARDAMOS EL AGUINALDO/////////////////");
      String strResultLast = "OK";

      Rhh_Nominas_Master nominaMaster = new Rhh_Nominas_Master();
      ArrayList<TableMaster> lstEmpleados = null;
      ArrayList<TableMaster> lstIncidencias = null;

      nominaMaster.setFieldString("RHN_DESCRIPCION", "CALCULO AGUINALDO");
      nominaMaster.setFieldString("RHN_FECHA_INICIAL", getIntAnio() + "0101");
      nominaMaster.setFieldString("RHN_FECHA_FINAL", getIntAnio() + "1231");
      nominaMaster.setFieldString("RHN_FECHA_REGISTRO", fec.getFechaActual());
      nominaMaster.setFieldInt("RHN_USUARIO", varSesiones.getIntNoUser());
      nominaMaster.setFieldString("RHN_HORA_REGISTRO", fec.getHoraActualHHMMSS());
      nominaMaster.setFieldInt("EMP_ID", varSesiones.getIntIdEmpresa());
      nominaMaster.setFieldInt("SC_ID", varSesiones.getIntSucursalDefault());
      nominaMaster.setFieldInt("RHN_ES_AGUINALDO", 1);
      nominaMaster.setFieldInt("RHN_NUMERO_DIAS", 365);

      nominaMaster.setBolGetAutonumeric(true);

      nominaMaster.Agrega(oConn);

      int intNominaMast = Integer.parseInt(nominaMaster.getValorKey());

      nominaMaster.ObtenDatos(intNominaMast, oConn);

      Iterator<AguinaldoEntidad> it = entidades.iterator();

      while (it.hasNext()) {

         AguinaldoEntidad entidad = it.next();

         Nominas nomina = new Nominas(oConn, varSesiones);
         nomina.Init();
         nomina.setIntEMP_ID(nominaMaster.getFieldInt("EMP_ID"));
         nomina.getDocument().setFieldInt("EMP_ID", nominaMaster.getFieldInt("EMP_ID"));
         nomina.getDocument().setFieldInt("SC_ID", nominaMaster.getFieldInt("SC_ID"));
         nomina.getDocument().setFieldString("NOM_FECHA", fec.getFechaActual());
         nomina.getDocument().setFieldString("NOM_FECHA_INICIAL_PAGO", nominaMaster.getFieldString("RHN_FECHA_INICIAL"));
         nomina.getDocument().setFieldString("NOM_FECHA_FINAL_PAGO", nominaMaster.getFieldString("RHN_FECHA_FINAL"));
         nomina.getDocument().setFieldInt("EMP_NUM", entidad.getIntEmpNum());
         nomina.getDocument().setFieldInt("NOM_MONEDA", 1);
         nomina.getDocument().setFieldString("NOM_CONCEPTO", nominaMaster.getFieldString("RHN_DESCRIPCION"));
         nomina.getDocument().setFieldDouble("NOM_NUM_DIAS_PAGADOS", nominaMaster.getFieldInt("RHN_NUMERO_DIAS"));
         nomina.getDocument().setFieldDouble("NOM_DESCUENTO", 0.0);

         nomina.getDocument().setFieldInt("RHN_ID", intNominaMast);

         int intTP_ID = HashMapPercepciones.get("2").getIntIdTipo();
         int intIdPerc = HashMapPercepciones.get("2").getIntIdPercDedu();

         rhh_nominas_deta detalle1 = new rhh_nominas_deta();
         detalle1.setFieldInt("TP_ID", intTP_ID);
         detalle1.setFieldInt("PERC_ID", intIdPerc);
         detalle1.setFieldInt("TD_ID", 0);
         detalle1.setFieldInt("DEDU_ID", 0);
         detalle1.setFieldInt("NOMD_CANTIDAD", 1);
         detalle1.setFieldDouble("NOMD_UNITARIO", entidad.getDblAguinaldo());
         detalle1.setFieldDouble("NOMD_GRAVADO", 0);
         nomina.getLstConceptos().add(detalle1);

         int intTD_ID = HashMapDeducciones.get("2").getIntIdTipo();
         int intIdDedu = HashMapDeducciones.get("2").getIntIdPercDedu();

         rhh_nominas_deta detalle2 = new rhh_nominas_deta();
         detalle2.setFieldInt("TP_ID", 0);
         detalle2.setFieldInt("PERC_ID", 0);
         detalle2.setFieldInt("TD_ID", intTD_ID);
         detalle2.setFieldInt("DEDU_ID", intIdDedu);
         detalle2.setFieldInt("NOMD_CANTIDAD", 1);
         detalle2.setFieldDouble("NOMD_UNITARIO", entidad.getDblImpuesto());
         detalle2.setFieldDouble("NOMD_GRAVADO", 0);
         nomina.getLstConceptos().add(detalle2);
         nomina.doTrx();
         String strRes = nomina.getStrResultLast();
         log.debug(strRes);
         if (!strRes.equals("OK")) {
            strResultLast = strRes;
         }

      }//Fin WHILE
      return strResultLast;
   }//Fin Calcula Nomina

    // </editor-fold>
}
