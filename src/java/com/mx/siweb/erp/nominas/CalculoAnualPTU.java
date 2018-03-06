/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.nominas;

import ERP.BusinessEntities.PercepcionesDeduccionesE;
import ERP.Nominas;
import ERP.PercepcionesDeducciones;
import Tablas.Rhh_Nominas_Master;
import Tablas.rhh_nominas_deta;
import com.mx.siweb.erp.nominas.Entidades.CalculoAnualPTUE;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Operaciones.TableMaster;
import comSIWeb.Utilerias.Fechas;
import comSIWeb.Utilerias.UtilXml;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author CasaJosefa
 */
public class CalculoAnualPTU {

   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(CalculoAnualPTU.class.getName());
   private ArrayList<CalculoAnualPTUE> entidades;
   private Conexion oConn;
   String strAnio;
   String strTipoNomina;
   double strMontoDistribuir;
   String strTipoNominaDesc;
   double dblFactorDias;
   double dblFactorSueldo;
   VariableSession varSesiones;

   ArrayList<Double> ValoresPTU;

   public CalculoAnualPTU(Conexion oConn, VariableSession varSesiones, String strAnio, String strTipoNomina, double strMontoDistribuir) {

      this.oConn = oConn;
      this.strAnio = strAnio;
      this.strTipoNomina = strTipoNomina;
      this.strMontoDistribuir = strMontoDistribuir;
      this.entidades = new ArrayList<CalculoAnualPTUE>();
      this.ValoresPTU = new ArrayList<Double>();
      this.varSesiones = varSesiones;
   }

   public void CalculoPTU() {

      String strSql = "select RHPP_DESCRIPCION from rhh_periodicidad_pago where RHPP_ID = " + strTipoNomina + ";";

      ResultSet rs;
      try {
         rs = oConn.runQuery(strSql, true);
         while (rs.next()) {

            this.strTipoNominaDesc = rs.getString("RHPP_DESCRIPCION");

         }
         rs.close();

      } catch (SQLException ex) {
         log.error(ex);
      }

      double TotalDiasLaborados = 0;
      Double TotalSalarioPercibido = 0.0;

      String strSql1 = "select *,rhh_empleados.EMP_PERIODICIDAD_PAGO,SUM(NOM_NUM_DIAS_PAGADOS) as SumaDias,"
              + "SUM(NOM_PERCEPCION_TOTAL) as SumaSueldo,COUNT(DISTINCT rhh_nominas.EMP_NUM) AS cantidadEmpleados"
              + " from rhh_nominas,rhh_empleados where left(NOM_FECHA,4) = " + strAnio + " and EMP_PERIODICIDAD_PAGO ='" + strTipoNominaDesc + "'";
      ResultSet rs1;
      try {
         rs1 = oConn.runQuery(strSql1, true);
         while (rs1.next()) {

            TotalDiasLaborados = rs1.getDouble("SumaDias");
            TotalSalarioPercibido = rs1.getDouble("SumaSueldo");

         }
         rs1.close();

      } catch (SQLException ex) {
         log.error(ex);
      }

      double dblMontoRepartir = strMontoDistribuir / 2;

      this.dblFactorDias = dblMontoRepartir / TotalDiasLaborados;
      this.dblFactorSueldo = dblMontoRepartir / TotalSalarioPercibido;

      ValoresPTU.add(dblFactorDias);
      ValoresPTU.add(dblFactorSueldo);

      String strSql2 = "select rhh_nominas.EMP_NUM, "
              + "(select rhh_empleados.EMP_NOMBRE from rhh_empleados where rhh_nominas.EMP_NUM = rhh_empleados.EMP_NUM) as EMP_NOMBRE, "
              + "(SUM(rhh_nominas.NOM_NUM_DIAS_PAGADOS)) as DiasLaborados ,(SUM(rhh_nominas.NOM_PERCEPCION_TOTAL)) as SalarioPercibido ,SUM(NOM_NUM_DIAS_PAGADOS) as SumaDias ,SUM(NOM_PERCEPCION_TOTAL) as SumaSueldo"
              + " from  rhh_nominas,rhh_empleados  "
              + "where left(NOM_FECHA,4) = " + strAnio + " and EMP_PERIODICIDAD_PAGO = '" + strTipoNominaDesc + "' group by rhh_nominas.EMP_NUM;";
      ResultSet rs2;
      try {
         rs2 = oConn.runQuery(strSql2, true);
         while (rs2.next()) {

            double intDiasTrabajados = rs2.getDouble("DiasLaborados");
            double dblSalarioPercibido = rs2.getDouble("SalarioPercibido");

            double dblPTUDias = intDiasTrabajados * dblFactorDias;
            double dblPTUSalario = dblSalarioPercibido * dblFactorSueldo;

            double dblTotalPTU = dblPTUDias + dblPTUSalario;

            CalculoAnualPTUE obj = new CalculoAnualPTUE();

            obj.setNumero(rs2.getInt("EMP_NUM"));
            obj.setNombre(rs2.getString("EMP_NOMBRE"));
            obj.setPtuDias(dblPTUDias);
            obj.setPtuSalario(dblPTUSalario);
            obj.setPtuTotal(dblTotalPTU);
            obj.setDiasLaborados(rs2.getDouble("SumaDias"));
            obj.setSueldoPrecibido(rs2.getInt("SumaSueldo"));

            entidades.add(obj);
         }
         rs2.close();

      } catch (SQLException ex) {
         log.error(ex);
      }

   }

   public String GeneraXmlPTU() {
      Iterator<CalculoAnualPTUE> it = entidades.iterator();
      UtilXml util = new UtilXml();
      StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
      strXML.append("<calculoPtu>");

      String strFactor = "";
      for (int i = 0; i <= ValoresPTU.size() - 1; i++) {
         if (i == 0) {
            strFactor = "Factor por dias trabajados";
         } else {
            strFactor = "Factor por salario percibido";
         }
         strXML.append("<calculoPtu_deta ");
         strXML.append(" strFactor= \"").append(strFactor).append("\" ");
         strXML.append(" dblImporte= \"").append(ValoresPTU.get(i)).append("\" ");
         strXML.append("/>");

      }

      while (it.hasNext()) {
         CalculoAnualPTUE entidad = it.next();
         strXML.append(" <calculoPtu_deta_1");
         strXML.append(" numero= \"").append(entidad.getNumero()).append("\" ");
         strXML.append(" nombre= \"").append(entidad.getNombre()).append("\" ");
         strXML.append(" PtuDias= \"").append(entidad.getPtuDias()).append("\" ");
         strXML.append(" PtuSalario= \"").append(entidad.getPtuSalario()).append("\" ");
         strXML.append(" PtuTotal= \"").append(entidad.getPtuTotal()).append("\" ");
         strXML.append(" TotalDiasLaborados= \"").append(entidad.getDiasLaborados()).append("\" ");
         strXML.append(" TotalSalarioPercibido= \"").append(entidad.getSueldoPrecibido()).append("\" ");
         strXML.append("/>");
      }
      strXML.append("</calculoPtu>");
      return strXML.toString();
   }

   public String GuardaPtu(String strAnio, ArrayList<CalculoAnualPTUE> entidades) {
      Fechas fecha = new Fechas();
      PercepcionesDeducciones PercDedu = new PercepcionesDeducciones(oConn, varSesiones.getIntIdEmpresa());
      Map<String, PercepcionesDeduccionesE> HashMapPercepciones = PercDedu.ObtenPercepciones();

      int intTP_ID = 0;
      int intIdPerc = 0;

      log.debug("//////////////GUARDAMOS EL PTU/////////////////");
      String strResultLast = "OK";

      Rhh_Nominas_Master nominaMaster = new Rhh_Nominas_Master();
      ArrayList<TableMaster> lstEmpleados = null;
      ArrayList<TableMaster> lstIncidencias = null;

      nominaMaster.setFieldString("RHN_DESCRIPCION", "CALCULO PTU");
      nominaMaster.setFieldString("RHN_FECHA_INICIAL", strAnio + "0101");
      nominaMaster.setFieldString("RHN_FECHA_FINAL", strAnio + "1231");
      nominaMaster.setFieldString("RHN_FECHA_REGISTRO", fecha.getFechaActual());
      nominaMaster.setFieldInt("RHN_USUARIO", varSesiones.getIntNoUser());
      nominaMaster.setFieldString("RHN_HORA_REGISTRO", fecha.getHoraActualHHMMSS());
      nominaMaster.setFieldInt("EMP_ID", varSesiones.getIntIdEmpresa());
      nominaMaster.setFieldInt("SC_ID", varSesiones.getIntSucursalDefault());
      nominaMaster.setFieldInt("RHN_ES_AGUINALDO", 0);
      nominaMaster.setFieldInt("RHN_NUMERO_DIAS", 365);

      nominaMaster.setBolGetAutonumeric(true);

      nominaMaster.Agrega(oConn);

      int intNominaMast = Integer.parseInt(nominaMaster.getValorKey());

      nominaMaster.ObtenDatos(intNominaMast, oConn);

      Iterator<CalculoAnualPTUE> it = entidades.iterator();
      while (it.hasNext()) {

         CalculoAnualPTUE entidad = it.next();

         Nominas nomina = new Nominas(oConn, varSesiones);
         nomina.Init();
         nomina.setIntEMP_ID(nominaMaster.getFieldInt("EMP_ID"));
         nomina.getDocument().setFieldInt("EMP_ID", nominaMaster.getFieldInt("EMP_ID"));
         nomina.getDocument().setFieldInt("SC_ID", nominaMaster.getFieldInt("SC_ID"));
         nomina.getDocument().setFieldString("NOM_FECHA", fecha.getFechaActual());
         nomina.getDocument().setFieldString("NOM_FECHA_INICIAL_PAGO", nominaMaster.getFieldString("RHN_FECHA_INICIAL"));
         nomina.getDocument().setFieldString("NOM_FECHA_FINAL_PAGO", nominaMaster.getFieldString("RHN_FECHA_FINAL"));
         nomina.getDocument().setFieldInt("EMP_NUM", entidad.getNumero());
         nomina.getDocument().setFieldInt("NOM_MONEDA", 1);
         nomina.getDocument().setFieldString("NOM_CONCEPTO", nominaMaster.getFieldString("RHN_DESCRIPCION"));
         nomina.getDocument().setFieldDouble("NOM_NUM_DIAS_PAGADOS", nominaMaster.getFieldInt("RHN_NUMERO_DIAS"));
         nomina.getDocument().setFieldDouble("NOM_DESCUENTO", 0.0);

         nomina.getDocument().setFieldInt("RHN_ID", intNominaMast);

         intTP_ID = HashMapPercepciones.get("3").getIntIdTipo();
         intIdPerc = HashMapPercepciones.get("3").getIntIdPercDedu();

         rhh_nominas_deta detalle1 = new rhh_nominas_deta();
         detalle1.setFieldInt("TP_ID", intTP_ID);
         detalle1.setFieldInt("PERC_ID", intIdPerc);
         detalle1.setFieldInt("TD_ID", 0);
         detalle1.setFieldInt("DEDU_ID", 0);
         detalle1.setFieldInt("NOMD_CANTIDAD", 1);
         detalle1.setFieldDouble("NOMD_UNITARIO", entidad.getPtuTotal());
         detalle1.setFieldDouble("NOMD_GRAVADO", 0);
         nomina.getLstConceptos().add(detalle1);

         nomina.doTrx();
         String strRes = nomina.getStrResultLast();
         log.debug(strRes);
         if (!strRes.equals("OK")) {
            strResultLast = strRes;
         }
      }
      return strResultLast;
   }
}
