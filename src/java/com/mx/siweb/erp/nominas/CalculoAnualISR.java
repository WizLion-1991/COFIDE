
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.nominas;

import ERP.CalculaISR;
import com.mx.siweb.erp.nominas.Entidades.CalculoAnualISRBachE;
import com.mx.siweb.erp.nominas.Entidades.CalculoAnualISRE;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Utilerias.Fechas;
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
import java.util.Locale;
import java.util.Map;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
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
 * @author CasaJosefa
 */
public class CalculoAnualISR {

   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(CalculoAnualISR.class.getName());
   private Conexion oConn;
   String strAnio;
   String strTipoNomina;
   VariableSession varSesiones;
   private int emp_id;
   private ArrayList<CalculoAnualISRE> entidades;
   private ArrayList<CalculoAnualISRBachE> entidades1;

   public CalculoAnualISR(Conexion oConn, VariableSession varSesiones, String strAnio, String strTipoNomina) {

      this.oConn = oConn;
      this.strAnio = strAnio;
      this.varSesiones = varSesiones;
      this.strTipoNomina = strTipoNomina;
      this.emp_id = varSesiones.getIntIdEmpresa();
      this.entidades = new ArrayList<CalculoAnualISRE>();
      this.entidades1 = new ArrayList<CalculoAnualISRBachE>();
   }

   public void CalculoISR() {

      String strTipoNominaDesc = "";
      String strSql = "select RHPP_DESCRIPCION from rhh_periodicidad_pago where RHPP_ID = " + strTipoNomina + ";";

      ResultSet rs;
      try {
         rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            strTipoNominaDesc = rs.getString("RHPP_DESCRIPCION");
         }
         rs.close();
      } catch (SQLException ex) {
         log.error(ex);
      }

      String strSql1 = " select "
              + " rhh_nominas.EMP_NUM,(select rhh_empleados.EMP_NOMBRE from rhh_empleados where rhh_nominas.EMP_NUM = rhh_empleados.EMP_NUM) as EMP_NOMBRE, "
              + " (select rhh_empleados.EMP_PERIODICIDAD_PAGO from rhh_empleados where rhh_nominas.EMP_NUM = rhh_empleados.EMP_NUM) as EMP_PERIODICIDAD, "
              + " (select SUM(NOMD_UNITARIO) from rhh_nominas_deta where rhh_nominas_deta.NOM_ID = rhh_nominas.NOM_ID and NOMD_GRAVADO = 0 ) as TotalExento, "
              + " (select SUM(NOMD_UNITARIO) from rhh_nominas_deta where rhh_nominas_deta.NOM_ID = rhh_nominas.NOM_ID and NOMD_GRAVADO = 1 ) as TotalGravado, "
              + " (select SUM(NOMD_UNITARIO) from rhh_nominas_deta where rhh_nominas_deta.NOM_ID = rhh_nominas.NOM_ID and  TD_ID = 2 ) as TotalIsrRetenido, "
              + " SUM(rhh_nominas.NOM_PERCEPCIONES) as TotalRemuneraciones , "
              + " SUM(NOM_PERCEPCION_TOTAL) as TotalSueldoDiario,  "
              + " SUM(rhh_nominas.NOM_NUM_DIAS_PAGADOS) as TotalDiasPagados  "
              + " from  rhh_nominas,rhh_empleados "
              + " where  rhh_nominas.EMP_NUM = rhh_empleados.EMP_NUM and  left(NOM_FECHA,4) = '" + strAnio + "' and EMP_PERIODICIDAD_PAGO = '" + strTipoNominaDesc + "' "
              + " group by rhh_nominas.EMP_NUM ;";

      ResultSet rs1;
      try {
         rs1 = oConn.runQuery(strSql1, true);
         while (rs1.next()) {

            int intIdEmpleado = rs1.getInt("EMP_NUM");
            String strNombreEmpleado = rs1.getString("EMP_NOMBRE");
            String strPeriodicidad = rs1.getString("EMP_PERIODICIDAD");
            double dblTotalExento = rs1.getDouble("TotalExento");
            double dblTotalGravado = rs1.getDouble("TotalGravado");
            double dblTotalIsrRetenido = rs1.getDouble("TotalIsrRetenido");
            double dblTotalRemuneraciones = rs1.getDouble("TotalRemuneraciones");
            double dblTotalSueldoDiario = rs1.getDouble("TotalSueldoDiario");
            double intTotalDiasPagados = rs1.getDouble("TotalDiasPagados");

            CalculaISR calcuIsr = new CalculaISR(oConn, varSesiones);
            calcuIsr.setDblGravados(dblTotalSueldoDiario);
            double dblImpuesoCalculado = calcuIsr.calculaImporte("ANUAL");

            double dblDiferencia = dblImpuesoCalculado - dblTotalIsrRetenido;

            CalculoAnualISRE e = new CalculoAnualISRE();

            e.setTotalRemuneraciones(dblTotalRemuneraciones);
            e.setTotalPercepcionesExentas(dblTotalExento);
            e.setTotalPercepcionesGravadas(dblTotalGravado);
            e.setImpuestoCalculado(dblImpuesoCalculado);
            e.setImpuestoRetenidoAnio(dblTotalIsrRetenido);
            e.setDiferencia(dblDiferencia);
            e.setIdEmpleado(intIdEmpleado);
            e.setNombreEmpleado(strNombreEmpleado);

            entidades.add(e);
         }
         rs1.close();

      } catch (SQLException ex) {
         log.error(ex);
      }
   }

   public String GeneraXmlISR() {
      Iterator<CalculoAnualISRE> it = entidades.iterator();
      UtilXml util = new UtilXml();
      StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
      strXML.append("<ISR>");
      while (it.hasNext()) {
         CalculoAnualISRE entidad = it.next();
         strXML.append(" <ISR_Deta");
         strXML.append(" TotalRemuneraciones= \"").append(entidad.getTotalRemuneraciones()).append("\" ");
         strXML.append(" TotalPercepcionesExentas= \"").append(entidad.getTotalPercepcionesExentas()).append("\" ");
         strXML.append(" TotalPercepcionesGravadas= \"").append(entidad.getTotalPercepcionesGravadas()).append("\" ");
         strXML.append(" ImpuestoCalculado= \"").append(entidad.getImpuestoCalculado()).append("\" ");
         strXML.append(" ImpuestoRetenidoAnio= \"").append(entidad.getImpuestoRetenidoAnio()).append("\" ");
         strXML.append(" Diferencia= \"").append(entidad.getDiferencia()).append("\" ");
         strXML.append(" IdEmpleado= \"").append(entidad.getIdEmpleado()).append("\" ");
         strXML.append(" NombreEmpleado= \"").append(entidad.getNombreEmpleado()).append("\" ");
         strXML.append("/>");
      }
      strXML.append("</ISR>");
      return strXML.toString();
   }

   public void GeneraBach() {

      String strTipoNominaDesc = "";
      String strSql = "select RHPP_DESCRIPCION from rhh_periodicidad_pago where RHPP_ID = " + strTipoNomina + ";";

      ResultSet rs;
      try {
         rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            strTipoNominaDesc = rs.getString("RHPP_DESCRIPCION");
         }
         rs.close();
      } catch (SQLException ex) {
         log.error(ex);
      }

      String strSql1 = " select rhh_empleados.EMP_RFC,rhh_empleados.EMP_CURP,rhh_empleados.EMP_NOMBRE "
              + " from  rhh_nominas,rhh_empleados  "
              + " where  rhh_nominas.EMP_NUM = rhh_empleados.EMP_NUM and  left(NOM_FECHA,4) = '" + strAnio + "' and EMP_PERIODICIDAD_PAGO = '" + strTipoNominaDesc + "'  "
              + " group by rhh_nominas.EMP_NUM ; ";

      ResultSet rs1;
      try {
         rs1 = oConn.runQuery(strSql1, true);
         while (rs1.next()) {

            CalculoAnualISRBachE e = new CalculoAnualISRBachE();

            e.setMes(1);
            e.setMesFinal(2);
            e.setRfc("3");
            e.setCurp("4");
            e.setApellidoPaterno("5");
            e.setApellidoMaterno(rs1.getString("EMP_NOMBRE"));
            e.setNombres("6");
            e.setAreaGeo(7);
            e.setCalAnual(8);
            e.setTarifa(9);
            e.setTarifa91(10);
            e.setProporc(11);
            e.setSindicato(12);
            e.setAsimilado(13);
            e.setEntidad(14);
            e.setRfc1Pat("15");
            e.setRfc2pat("16");
            e.setRfc3pat("17");
            e.setRfc4pat("18");
            e.setRfc5pat("19");
            e.setRfc6pat("20");
            e.setRfc7pat("21");
            e.setRfc8pat("22");
            e.setRfc9pat("23");
            e.setRfc10pat("24");
            e.setMontoAportacionesVoluntarias(25);
            e.setAplicoMontoAportVoluntarioas("26");
            e.setMontoAportDeduciblesTrabDecl(27);
            e.setMontoAportVoluDeduPatron(28);
            e.setPagosSeparacion(29);
            e.setAsimiladosSalarios(30);
            e.setPagosEfectuadosTrabajadores(31);
            e.setTotalesPagosParciales(32);
            e.setMontoDiarioJubilaciones(33);
            e.setCantidadPercibidaPeriodo(34);
            e.setMontoTotalPagoUnaExhibi(35);
            e.setNumeroDias(36);
            e.setIngresosExcentos(37);
            e.setIngresosGravables(38);
            e.setIngresosAcomulables(39);
            e.setIngresosNoAcomulables(40);
            e.setImpRetenido(41);
            e.setMontoTotalOtrosPagosSep(42);
            e.setNumeroAniosServicio(43);
            e.setIngresosExcentos1(44);
            e.setIngresosGravados(45);
            e.setAcomulablesUltimoSdoMensual(46);
            e.setCorrespUltimoSdoMensual(47);
            e.setNoAcomulables(48);
            e.setImpRetenido1(49);
            e.setIngresosAsimiladosSalarios(50);
            e.setImpuestoRetenidoEjercicio(51);
            e.setOpcEmpleadorAdquirirTitulos(52);
            e.setValorMercadoAcionesAddTitulos(53);
            e.setPrecEstablIngreTitulos(54);
            e.setIngresoAcomulable(55);
            e.setImpuestoRetenidoEjercicio1(56);
            e.setSueldoSalariosRayasJornales(57);
            e.setSueldoExcento(58);
            e.setGratificacionAnual(59);
            e.setAguinaldoExento(60);
            e.setViaticosGastosViaje(61);
            e.setViaticosGastosViaje1(62);
            e.setTiempoExtraOrdinario(63);
            e.setTiempoExtraExento(64);
            e.setPrimaVacacional(65);
            e.setPrimaVacaExento(66);
            e.setPrimaDominical(67);
            e.setPrimaDominical1(68);
            e.setParticipacionTrabajUtilidades(69);
            e.setPtuExenta(70);
            e.setReembolsoGastMedDentHosp(71);
            e.setReembolsoGastMedDentHosp1(72);
            e.setFondoAhorro(73);
            e.setFondoAhorro1(74);
            e.setCajaAhorro(75);
            e.setCajaAhorro1(76);
            e.setValesDespensa(77);
            e.setValesDespensa1(78);
            e.setAyudaGastosFuneral(79);
            e.setAyudaGastosFuneral1(80);
            e.setContibuCargoTrabajPagPatron(81);
            e.setContibuCargoTrabajPagPatron1(82);
            e.setPremioPuntualidad(83);
            e.setPremioPuntualidad1(84);
            e.setPrimaSeguroVida(85);
            e.setPrimaSeguroVida1(86);
            e.setSeguroGastosMedicosMayores(87);
            e.setSeguroGastosMedicosMayores1(88);
            e.setValesRestaurante(89);
            e.setValesRestaurante1(90);
            e.setValesGasolina(91);
            e.setValesGasolina1(92);
            e.setValesRopa(93);
            e.setValesRopa1(94);
            e.setAyudaRenta(95);
            e.setAyudaRenta1(96);
            e.setAyudaArticulosEscolares(97);
            e.setAyudaArticulosEscolares1(98);
            e.setAyudaAnteojos(99);
            e.setAyudaAnteojos1(100);
            e.setAyudaTransporte(101);
            e.setAyudaTransporte1(102);
            e.setCuotasSindicalesPagadasPatron(103);
            e.setCuotasSindicalesPagadasPatron1(104);
            e.setSubsidioIncapacidad(105);
            e.setSubsidioIncapacidad1(106);
            e.setBecasTrabajadoresHijos(107);
            e.setBecasTrabajadoresHijos1(108);
            e.setPagoOtroEmpleador(109);
            e.setPagoOtroEmpleador1(110);
            e.setOtrosIngresosSalarios(111);
            e.setOtrosIngresosSalarios1(112);
            e.setSumaIngresoGravadiSuelSal(113);
            e.setSumaIngresoExentoSuieldSal(114);
            e.setImpuestoRetenEjercicio(115);
            e.setImpuestoRetenOtrosPatronEjer(116);
            e.setSaldoFavorEjercPatrCompSiguiEjer(117);
            e.setSaldoFavorEjercAntNoComp(118);
            e.setSumaCantConcCredit(119);
            e.setCreditoSalarioEntrEfecTrabEje(120);
            e.setMontoTotalIngrObtenPrevisionSocial(121);
            e.setSumaIngreExcentosConceptoPresta(122);
            e.setSumaIngresosSueldosSalarios(123);
            e.setMontoImpuestoLocPrestServSocial(124);
            e.setMontoSubEmplEfectTrabEjer(125);
            e.setTotalAportVoluntDedu(126);
            e.setIsrTarifAnual(127);
            e.setSubsAcreditable(128);
            e.setSubsNoAcreditable(129);
            e.setImpuestoIngresosAcomulables(130);
            e.setImpuestoIngresosNoAcomulables(131);
            e.setSubsidioEntregadoTrabajador(132);
            e.setSubsidioNivelacionIngresoEntregado(133);

            entidades1.add(e);
         }
         rs1.close();

      } catch (SQLException ex) {
         log.error(ex);
      }
   }

   public void RepContrato_getReportPrintBach(String sourceFileName, String targetFileName, VariableSession varSesiones,
           ByteArrayOutputStream byteArrayOutputStream, int frmt,
           String strPeriodo) {

      String strEmpresa = "";
      String strRFC = "";

      String strSql2 = "select EMP_RFC,EMP_RAZONSOCIAL from vta_empresas where EMP_ID  =" + varSesiones.getIntIdEmpresa();
      ResultSet rs2;
      try {
         rs2 = this.oConn.runQuery(strSql2, true);
         while (rs2.next()) {
            strEmpresa = rs2.getString("EMP_RAZONSOCIAL");
            strRFC = rs2.getString("EMP_RFC");
         }
         rs2.close();
      } catch (SQLException ex) {
         log.error(ex);
      }

      Fechas fecha = new Fechas();

      //Generamos el pdf
      InputStream reportStream = null;
      try {
         //Parametros
         Map parametersMap = new HashMap();

         parametersMap.put("strEmpresa", strEmpresa);
         parametersMap.put("strRFC", strRFC);

         Locale locale = new Locale("es", "MX");
         //Definicion estandard localidad(MX mientras no implementemos algo en el extranjero)

         parametersMap.put(JRParameter.REPORT_LOCALE, locale);

         reportStream = new FileInputStream(sourceFileName);
         // Bing the datasource with the collection
         JRDataSource datasource = new JRBeanCollectionDataSource(this.entidades1, true);
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
         log.error(ex.getLocalizedMessage() + " " + ex.getMessage());
      } catch (JRException ex) {
         log.error(ex.getLocalizedMessage() + " " + ex.getMessageKey() + " " + ex.getMessage() + " " + ex.getArgs() + " ");
      } catch (IOException ex) {
         log.error(ex.getLocalizedMessage());
      }
   }

}
