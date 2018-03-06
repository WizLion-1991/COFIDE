/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ERP;

import ERP.BusinessEntities.PoliCtas;
import Tablas.VtaNotasCargos;
import Tablas.rhh_nominas;
import Tablas.vta_cxpagar;
import Tablas.vta_cxpagardetalle;
import Tablas.vta_facturas;
import Tablas.vta_mov_cta_bcos;
import Tablas.vta_mov_cta_bcos_deta;
import Tablas.vta_mov_cte;
import Tablas.vta_mov_prov;
import Tablas.vta_ncredito;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Operaciones.TableMaster;
import comSIWeb.Operaciones.bitacorausers;
import comSIWeb.Utilerias.Fechas;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * Esta clase realiza algunas funciones con la contabilidad
 *
 * @author ZeusGalindo
 */
public class ContabilidadUtil {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(ContabilidadUtil.class.getName());
   private Conexion oConn;
   private VariableSession varSesiones;
   protected String strCtaEmpty;
   protected int intMonedaBase = 1;
   private Monedas monedas;
   private int intEsAnticipo = 0;
   private double dblTmp1 = 0;
   private double dblTmp2 = 0;
   private double dblTmp3 = 0;
   private double dblTmp4 = 0;
   private double dblTmp5 = 0;
   private double dblTmp6 = 0;
   private double dblTmp7 = 0;
   private double dblTmp8 = 0;
   private double dblTmp9 = 0;
   private double dblTmp10 = 0;
   //Objetos para pagos masivos
   private PagosMasivos pagosMasivos;
   private PagosMasivosCtas pagosMasivosCtas;
   boolean bolContaFacturas = true;
   boolean bolContaCobros = true;
   boolean bolContaCXP = true;
   boolean bolContaPagos = true;
   boolean bolContaBco = true;
   boolean bolContaNominas = true;
   boolean bolContaNCredito = true;
   private String strFiltroFacturas;
   private String strFiltroCobros;
   private String strFiltroCXP;
   private String strFiltroPagos;
   private String strFiltroBancos;
   private String strFiltroNCredito;
   private int intSucessFacturas;
   private int intFailFacturas;
   private int intSucessCobros;
   private int intFailCobros;
   private int intSucessCXP;
   private int intFailCXP;
   private int intSucessPagos;
   private int intFailPagos;
   private int intSucessBcos;
   private int intFailBcos;
   private int intSucessNominas;
   private int intFailNominas;
   private int intSucessNcredito;
   private int intFailNcredito;
   private ArrayList<String> lstFailsFacturas;
   private ArrayList<String> lstFailsCobros;
   private ArrayList<String> lstFailsCXP;
   private ArrayList<String> lstFailsPagos;
   private ArrayList<String> lstFailsBancos;
   private ArrayList<String> lstFailsNominas;
   private ArrayList<String> lstFailsNcredito;
   private String strFiltroPagosMasivos;
   private String strFiltroCobrosMasivos;
   private String strFiltroNominas;
   private String strPathBaseXMLCXP;
   private String strPathBaseXMLFacturas;

   public String getStrPathBaseXMLFacturas() {
      return strPathBaseXMLFacturas;
   }

   public void setStrPathBaseXMLFacturas(String strPathBaseXMLFacturas) {
      this.strPathBaseXMLFacturas = strPathBaseXMLFacturas;
   }

   public double getDblTmp1() {
      return dblTmp1;
   }

   public void setDblTmp1(double dblTmp1) {
      this.dblTmp1 = dblTmp1;
   }

   public double getDblTmp4() {
      return dblTmp4;
   }

   public void setDblTmp4(double dblTmp4) {
      this.dblTmp4 = dblTmp4;
   }

   public double getDblTmp5() {
      return dblTmp5;
   }

   public void setDblTmp5(double dblTmp5) {
      this.dblTmp5 = dblTmp5;
   }

   public double getDblTmp6() {
      return dblTmp6;
   }

   public void setDblTmp6(double dblTmp6) {
      this.dblTmp6 = dblTmp6;
   }

   public double getDblTmp7() {
      return dblTmp7;
   }

   public void setDblTmp7(double dblTmp7) {
      this.dblTmp7 = dblTmp7;
   }

   public double getDblTmp8() {
      return dblTmp8;
   }

   public void setDblTmp8(double dblTmp8) {
      this.dblTmp8 = dblTmp8;
   }

   public double getDblTmp9() {
      return dblTmp9;
   }

   public void setDblTmp9(double dblTmp9) {
      this.dblTmp9 = dblTmp9;
   }

   public double getDblTmp10() {
      return dblTmp10;
   }

   public void setDblTmp10(double dblTmp10) {
      this.dblTmp10 = dblTmp10;
   }

   public String getStrPathBaseXML() {
      return strPathBaseXMLCXP;
   }

   /**
    * Define el path base donde se ubicaran todos los xml de las cuentas x pagar
    *
    * @param strPathBaseXMLCXP
    */
   public void setStrPathBaseXML(String strPathBaseXMLCXP) {
      this.strPathBaseXMLCXP = strPathBaseXMLCXP;
   }

   public ArrayList<String> getLstFailsFacturas() {
      return lstFailsFacturas;
   }

   public void setLstFailsFacturas(ArrayList<String> lstFailsFacturas) {
      this.lstFailsFacturas = lstFailsFacturas;
   }

   public ArrayList<String> getLstFailsCobros() {
      return lstFailsCobros;
   }

   public void setLstFailsCobros(ArrayList<String> lstFailsCobros) {
      this.lstFailsCobros = lstFailsCobros;
   }

   public ArrayList<String> getLstFailsCXP() {
      return lstFailsCXP;
   }

   public void setLstFailsCXP(ArrayList<String> lstFailsCXP) {
      this.lstFailsCXP = lstFailsCXP;
   }

   public ArrayList<String> getLstFailsPagos() {
      return lstFailsPagos;
   }

   public void setLstFailsPagos(ArrayList<String> lstFailsPagos) {
      this.lstFailsPagos = lstFailsPagos;
   }

   public ArrayList<String> getLstFailsBancos() {
      return lstFailsBancos;
   }

   public void setLstFailsBancos(ArrayList<String> lstFailsBancos) {
      this.lstFailsBancos = lstFailsBancos;
   }

   public int getIntSucessFacturas() {
      return intSucessFacturas;
   }

   public void setIntSucessFacturas(int intSucessFacturas) {
      this.intSucessFacturas = intSucessFacturas;
   }

   public int getIntFailFacturas() {
      return intFailFacturas;
   }

   public void setIntFailFacturas(int intFailFacturas) {
      this.intFailFacturas = intFailFacturas;
   }

   public int getIntSucessCobros() {
      return intSucessCobros;
   }

   public void setIntSucessCobros(int intSucessCobros) {
      this.intSucessCobros = intSucessCobros;
   }

   public int getIntFailCobros() {
      return intFailCobros;
   }

   public void setIntFailCobros(int intFailCobros) {
      this.intFailCobros = intFailCobros;
   }

   public int getIntSucessCXP() {
      return intSucessCXP;
   }

   public void setIntSucessCXP(int intSucessCXP) {
      this.intSucessCXP = intSucessCXP;
   }

   public int getIntFailCXP() {
      return intFailCXP;
   }

   public void setIntFailCXP(int intFailCXP) {
      this.intFailCXP = intFailCXP;
   }

   public int getIntSucessPagos() {
      return intSucessPagos;
   }

   public void setIntSucessPagos(int intSucessPagos) {
      this.intSucessPagos = intSucessPagos;
   }

   public int getIntFailPagos() {
      return intFailPagos;
   }

   public void setIntFailPagos(int intFailPagos) {
      this.intFailPagos = intFailPagos;
   }

   public int getIntSucessBcos() {
      return intSucessBcos;
   }

   public void setIntSucessBcos(int intSucessBcos) {
      this.intSucessBcos = intSucessBcos;
   }

   public int getIntFailBcos() {
      return intFailBcos;
   }

   public void setIntFailBcos(int intFailBcos) {
      this.intFailBcos = intFailBcos;
   }

   public String getStrFiltroFacturas() {
      return strFiltroFacturas;
   }

   public void setStrFiltroFacturas(String strFiltroFacturas) {
      this.strFiltroFacturas = strFiltroFacturas;
   }

   public String getStrFiltroCobros() {
      return strFiltroCobros;
   }

   public void setStrFiltroCobros(String strFiltroCobros) {
      this.strFiltroCobros = strFiltroCobros;
   }

   public String getStrFiltroCXP() {
      return strFiltroCXP;
   }

   public void setStrFiltroCXP(String strFiltroCXP) {
      this.strFiltroCXP = strFiltroCXP;
   }

   public String getStrFiltroPagos() {
      return strFiltroPagos;
   }

   public void setStrFiltroPagos(String strFiltroPagos) {
      this.strFiltroPagos = strFiltroPagos;
   }

   public String getStrFiltroBancos() {
      return strFiltroBancos;
   }

   public void setStrFiltroBancos(String strFiltroBancos) {
      this.strFiltroBancos = strFiltroBancos;
   }

   public boolean isBolContaFacturas() {
      return bolContaFacturas;
   }

   public void setBolContaFacturas(boolean bolContaFacturas) {
      this.bolContaFacturas = bolContaFacturas;
   }

   public boolean isBolContaCobros() {
      return bolContaCobros;
   }

   public void setBolContaCobros(boolean bolContaCobros) {
      this.bolContaCobros = bolContaCobros;
   }

   public boolean isBolContaCXP() {
      return bolContaCXP;
   }

   public void setBolContaCXP(boolean bolContaCXP) {
      this.bolContaCXP = bolContaCXP;
   }

   public boolean isBolContaPagos() {
      return bolContaPagos;
   }

   public void setBolContaPagos(boolean bolContaPagos) {
      this.bolContaPagos = bolContaPagos;
   }

   public boolean isBolContaBco() {
      return bolContaBco;
   }

   public void setBolContaBco(boolean bolContaBco) {
      this.bolContaBco = bolContaBco;
   }

   public PagosMasivos getPagosMasivos() {
      return pagosMasivos;
   }

   public String getStrFiltroPagosMasivos() {
      return strFiltroPagosMasivos;
   }

   public void setStrFiltroPagosMasivos(String strFiltroPagosMasivos) {
      this.strFiltroPagosMasivos = strFiltroPagosMasivos;
   }

   public String getStrFiltroCobrosMasivos() {
      return strFiltroCobrosMasivos;
   }

   public void setStrFiltroCobrosMasivos(String strFiltroCobrosMasivos) {
      this.strFiltroCobrosMasivos = strFiltroCobrosMasivos;
   }

   public boolean isBolContaNominas() {
      return bolContaNominas;
   }

   public void setBolContaNominas(boolean bolContaNominas) {
      this.bolContaNominas = bolContaNominas;
   }

   public int getIntSucessNominas() {
      return intSucessNominas;
   }

   public void setIntSucessNominas(int intSucessNominas) {
      this.intSucessNominas = intSucessNominas;
   }

   public int getIntFailNominas() {
      return intFailNominas;
   }

   public void setIntFailNominas(int intFailNominas) {
      this.intFailNominas = intFailNominas;
   }

   public ArrayList<String> getLstFailsNominas() {
      return lstFailsNominas;
   }

   public void setLstFailsNominas(ArrayList<String> lstFailsNominas) {
      this.lstFailsNominas = lstFailsNominas;
   }

   public String getStrFiltroNominas() {
      return strFiltroNominas;
   }

   public void setStrFiltroNominas(String strFiltroNominas) {
      this.strFiltroNominas = strFiltroNominas;
   }

   public String getStrFiltroNCredito() {
      return strFiltroNCredito;
   }

   public void setStrFiltroNCredito(String strFiltroNCredito) {
      this.strFiltroNCredito = strFiltroNCredito;
   }

   public int getIntSucessNcredito() {
      return intSucessNcredito;
   }

   public void setIntSucessNcredito(int intSucessNcredito) {
      this.intSucessNcredito = intSucessNcredito;
   }

   public int getIntFailNcredito() {
      return intFailNcredito;
   }

   public void setIntFailNcredito(int intFailNcredito) {
      this.intFailNcredito = intFailNcredito;
   }

   public ArrayList<String> getLstFailsNcredito() {
      return lstFailsNcredito;
   }

   public void setLstFailsNcredito(ArrayList<String> lstFailsNcredito) {
      this.lstFailsNcredito = lstFailsNcredito;
   }

   public boolean isBolContaNCredito() {
      return bolContaNCredito;
   }

   public void setBolContaNCredito(boolean bolContaNCredito) {
      this.bolContaNCredito = bolContaNCredito;
   }

   /**
    * Objeto para obtener los pagos masivos de los cobros
    *
    * @param pagosMasivos
    */
   public void setPagosMasivos(PagosMasivos pagosMasivos) {
      this.pagosMasivos = pagosMasivos;
   }

   public PagosMasivosCtas getPagosMasivosCtas() {
      return pagosMasivosCtas;
   }

   /**
    * Objetos para pagos masivos de pagos de cuentas por pagar
    *
    * @param pagosMasivosCtas
    */
   public void setPagosMasivosCtas(PagosMasivosCtas pagosMasivosCtas) {
      this.pagosMasivosCtas = pagosMasivosCtas;
   }

   public double getDblTmp2() {
      return dblTmp2;
   }

   /**
    * Importe tempooral 2
    *
    * @param dblTmp2
    */
   public void setDblTmp2(double dblTmp2) {
      this.dblTmp2 = dblTmp2;
   }

   public double getDblTmp3() {
      return dblTmp3;
   }

   /**
    * Importe temporal 3
    *
    * @param dblTmp3
    */
   public void setDblTmp3(double dblTmp3) {
      this.dblTmp3 = dblTmp3;
   }

   public int getIntEsAnticipo() {
      return intEsAnticipo;
   }

   public void setIntEsAnticipo(int intEsAnticipo) {
      this.intEsAnticipo = intEsAnticipo;
   }

   public int getIntMonedaBase() {
      return intMonedaBase;
   }

   public void setIntMonedaBase(int intMonedaBase) {
      this.intMonedaBase = intMonedaBase;
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public ContabilidadUtil(Conexion oConn, VariableSession varSesiones) {
      this.oConn = oConn;
      this.varSesiones = varSesiones;
      this.strCtaEmpty = "0000";
      this.monedas = new Monedas(this.oConn);
      //Filtros personalizados para consulta en las migraciones
      strFiltroFacturas = "";
      strFiltroCobros = "";
      strFiltroCXP = "";
      strFiltroPagos = "";
      strFiltroBancos = "";
      strFiltroPagosMasivos = "";
      strFiltroCobrosMasivos = "";
      strFiltroNCredito = "";
      strFiltroNominas = "";
      //Inicializamos las listas para guardar los errores
      lstFailsFacturas = new ArrayList<String>();
      lstFailsCobros = new ArrayList<String>();
      lstFailsCXP = new ArrayList<String>();
      lstFailsPagos = new ArrayList<String>();
      lstFailsBancos = new ArrayList<String>();
      this.lstFailsNominas = new ArrayList<String>();
      this.lstFailsNcredito = new ArrayList<String>();
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   /**
    * Genera toda la contabilidad de un periodo(AAAMM)
    *
    * @param intEMP_ID Es el id de la empresa
    * @param strMesAnio Es el mes y anio
    */
   public void GeneraConta(int intEMP_ID, String strMesAnio) {
      String strEMP_PASSCP = "";
      String strEMP_USERCP = "";
      String strEMP_URLCP = "";
      String strCtaVtasGlobal = "";
      String strCtaVtasIVAGlobal = "";
      String strCtaVtasIVATasa = "";
      String strCtaVtasCteGlobal = "";
      String strCXPCteGlobal = "";
      String strSql = "SELECT * FROM vta_empresas where EMP_ID=" + intEMP_ID + "";
      try {
         ResultSet rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            strEMP_URLCP = rs.getString("EMP_URLCP");
            strEMP_USERCP = rs.getString("EMP_USERCP");
            strEMP_PASSCP = rs.getString("EMP_PASSCP");
            strCtaVtasGlobal = rs.getString("EMP_CTAVTA");
            strCtaVtasIVAGlobal = rs.getString("EMP_CTAIVA");
            strCtaVtasCteGlobal = rs.getString("EMP_CTACTE");
            strCXPCteGlobal = rs.getString("EMP_CTAPROV");
         }
         rs.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage());
      }
      //Generamos bitacora de corrida de proceso
      Fechas fecha = new Fechas();
      bitacorausers logUser = new bitacorausers();
      logUser.setFieldString("BTU_FECHA", fecha.getFechaActual());
      logUser.setFieldString("BTU_HORA", fecha.getHoraActual());
      logUser.setFieldString("BTU_NOMMOD", "CONTA_MAS");
      logUser.setFieldString("BTU_NOMACTION", "PROCESS");
      logUser.setFieldInt("BTU_IDOPER", 0);
      logUser.setFieldInt("BTU_IDUSER", this.varSesiones.getIntNoUser());
      logUser.setFieldString("BTU_NOMUSER", this.varSesiones.getStrUser());
      logUser.Agrega(oConn);
      //Facturas
      if (bolContaFacturas) {
         GeneraContaFacturas(intEMP_ID, strMesAnio, strEMP_PASSCP, strEMP_USERCP, strEMP_URLCP, strCtaVtasGlobal, strCtaVtasIVAGlobal, strCtaVtasIVATasa, strCtaVtasCteGlobal);
      }
      //Cobros de contabilidad
      if (bolContaCobros) {
         GeneraContaCobros(intEMP_ID, strMesAnio, strEMP_PASSCP, strEMP_USERCP, strEMP_URLCP);
      }
//      //Cuentas por pagar
      if (bolContaCXP) {
         GeneraContaCXP(intEMP_ID, strMesAnio, strEMP_PASSCP, strEMP_USERCP, strEMP_URLCP, strCtaVtasGlobal, strCXPCteGlobal, strCtaVtasIVAGlobal);
      }
//      //Pagos para de contabilidad
      if (bolContaPagos) {
         GeneraContaPagos(intEMP_ID, strMesAnio, strEMP_PASSCP, strEMP_USERCP, strEMP_URLCP);
      }
      //Genera contabilidad
      if (bolContaBco) {
         GeneraContaBco(intEMP_ID, strMesAnio, strEMP_PASSCP, strEMP_USERCP, strEMP_URLCP);
      }
      //Genera contabilidad para las nominas
      if (bolContaNominas) {
         GeneraContaNominas(intEMP_ID, strMesAnio, strEMP_PASSCP, strEMP_USERCP, strEMP_URLCP);
      }
      //Genera contabilidad para las notas de credito
      if (bolContaNCredito) {
         GeneraContaNCredito(intEMP_ID, strMesAnio, strEMP_PASSCP, strEMP_USERCP, strEMP_URLCP, strCtaVtasGlobal, strCtaVtasIVAGlobal, strCtaVtasIVATasa, strCtaVtasCteGlobal);
      }
   }

   /**
    *
    * @param intEMP_ID Es el id de empresa
    * @param strMesAnio Es el periodo de donde se tomaran los movimientos por
    * contabilizar(AAAAMM)
    * @param strEMP_PASSCP Es el password para la contabilidad
    * @param strEMP_USERCP Es el usuario para la contabilidad
    * @param strEMP_URLCP Es la url para la contabilidad
    * @param strCtaVtasGlobal Es la cuenta de ventas global
    * @param strCtaVtasIVAGlobal Es la cuenta de iva global
    * @param strCtaVtasIVATasa Es la cuenta de tasa de iva global
    * @param strCtaVtasCteGlobal Es la cuenta del cliente glonal
    */
   public void GeneraContaFacturas(int intEMP_ID, String strMesAnio,
      String strEMP_PASSCP, String strEMP_USERCP, String strEMP_URLCP, String strCtaVtasGlobal, String strCtaVtasIVAGlobal, String strCtaVtasIVATasa, String strCtaVtasCteGlobal) {
      String strNomTablaMaster = "vta_facturas";
      String strPrefijoMaster = "FAC";
      try {
         //Consultamos tas las operac ion bd local
         String strsqlmaster = "SELECT FAC_ID,TI_ID,FAC_MONEDA FROM vta_facturas where left(FAC_FECHA,6)='" + strMesAnio + "' "
            + " and EMP_ID = " + intEMP_ID + " "
            + " and FAC_ANULADA = 0 " + strFiltroFacturas;
         ResultSet rsM = oConn.runQuery(strsqlmaster);
         while (rsM.next()) {
            int intId = rsM.getInt("FAC_ID");
            int intTI_ID = rsM.getInt("TI_ID");
            int intMonedaOpera = rsM.getInt("FAC_MONEDA");

            TableMaster document = new vta_facturas();
            document.ObtenDatos(intId, oConn);
            //Calcula la poliza para facturas
            CalculaPolizaContableFacturas(intEMP_ID,
               strEMP_PASSCP, strEMP_USERCP, strEMP_URLCP, strCtaVtasGlobal,
               strCtaVtasIVAGlobal, strCtaVtasIVATasa, strCtaVtasCteGlobal,
               intMonedaOpera, document, intTI_ID, intId, strNomTablaMaster, strPrefijoMaster, "NEW");
         }
         rsM.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage());
      }
   }

   /**
    * Calcula la poliza contable para las facturas
    *
    * @param intEMP_ID Es el id de empresa
    * @param strEMP_PASSCP Es el password para la contabilidad
    * @param strEMP_USERCP Es el usuario para la contabilidad
    * @param strEMP_URLCP Es la url para la contabilidad
    * @param strCtaVtasGlobal Es la cuenta de ventas global
    * @param strCtaVtasIVAGlobal Es la cuenta de iva global
    * @param strCtaVtasIVATasa Es la cuenta de tasa de iva global
    * @param strCtaVtasCteGlobal Es la cuenta del cliente glonal
    * @param intMonedaOpera Es el id de la moneda de operacion
    * @param document Es el documento
    * @param intTI_ID Es el id de la tasa de iva
    * @param intId Es el id de la operacion
    * @param strNomTablaMaster Es el nombre de la tabla principal
    * @param strPrefijoMaster Es el prefijo de la tabla principal
    * @param strTipoOper Es el tipo de operacion de la poliza
    */
   public void CalculaPolizaContableFacturas(int intEMP_ID,
      String strEMP_PASSCP, String strEMP_USERCP, String strEMP_URLCP, String strCtaVtasGlobal,
      String strCtaVtasIVAGlobal, String strCtaVtasIVATasa, String strCtaVtasCteGlobal,
      int intMonedaOpera, TableMaster document, int intTI_ID, int intId, String strNomTablaMaster,
      String strPrefijoMaster, String strTipoOper) {
      boolean bolComplementarias = false;
      double dblComplementaria = 0;
      String strCtaComp1 = "";
      String strCtaComp1Prov = "";
      //Evaluamos si tenemos que realizar conversion de monedas
      if (intMonedaOpera != this.intMonedaBase) {
         bolComplementarias = true;
         //Aplica conversion
         this.monedas.setBoolConversionAutomatica(true);
         double dblFactor = this.monedas.GetFactorConversion(document.getFieldString("FAC_FECHA"), 4, intMonedaOpera, intMonedaBase);

         dblComplementaria = document.getFieldDouble("FAC_TOTAL") * dblFactor;
         dblComplementaria -= document.getFieldDouble("FAC_TOTAL");

         document.setFieldDouble("FAC_IMPORTE", document.getFieldDouble("FAC_IMPORTE") * dblFactor);
         document.setFieldDouble("FAC_IMPUESTO1", document.getFieldDouble("FAC_IMPUESTO1") * dblFactor);
         document.setFieldDouble("FAC_TOTAL", document.getFieldDouble("FAC_TOTAL"));
      }
      //Valores para contabilidad
      String strCtaVtas = "";
      String strCtaVtasCte = "";
      String strCT_CONTA_RET_ISR = "";
      String strCT_CONTA_RET_IVA = "";
      int intCT_CATEGORIA1 = 0;
      //Buscamos cuentas para los ivas
      String strSql = "SELECT TI_CTA_CONT,TI_CTA_CONT_COB FROM vta_tasaiva "
         + " where TI_ID=" + intTI_ID + "";
      try {
         ResultSet rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            strCtaVtasIVATasa = rs.getString("TI_CTA_CONT");
         }
         rs.close();
      } catch (SQLException ex) {
         log.debug(ex.getMessage());

      }
      //Buscamos cuentas a la medida del cliente
      strSql = "SELECT CT_CONTAVTA,CT_CONTACTE,CT_CTA_ANTICIPO,"
         + "CT_CTACTE_COMPL_ANTI,CT_CONTACTE_COMPL,"
         + "CT_CONTA_RET_ISR,CT_CONTA_RET_IVA,CT_CATEGORIA1 FROM vta_cliente "
         + " where CT_ID=" + document.getFieldInt("CT_ID");
      try {
         ResultSet rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            strCtaVtas = rs.getString("CT_CONTAVTA");
            strCtaVtasCte = rs.getString("CT_CONTACTE");
            strCtaComp1Prov = rs.getString("CT_CONTACTE_COMPL");
            strCT_CONTA_RET_ISR = rs.getString("CT_CONTA_RET_ISR");
            strCT_CONTA_RET_IVA = rs.getString("CT_CONTA_RET_IVA");
            intCT_CATEGORIA1 = rs.getInt("CT_CATEGORIA1");
         }
         rs.close();
      } catch (SQLException ex) {
         log.debug(ex.getMessage());

      }

      // <editor-fold defaultstate="collapsed" desc="Consultamos la info de la empresa">
      strSql = "SELECT EMP_CTACTE_COMPL,EMP_USERCP,EMP_PASSCP,EMP_URLCP "
         + " FROM vta_empresas "
         + " WHERE EMP_ID = " + intEMP_ID + "";
      try {
         ResultSet rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            strCtaComp1 = rs.getString("EMP_CTACTE_COMPL");
            strEMP_USERCP = rs.getString("EMP_USERCP");
            strEMP_PASSCP = rs.getString("EMP_PASSCP");
            strEMP_URLCP = rs.getString("EMP_URLCP");
         }
         rs.close();
      } catch (SQLException ex) {
         log.debug("ERROR:" + ex.getMessage());

      }
      // </editor-fold>
      //Actualizamos la poliza contable
      PolizasContables poli = new PolizasContables(oConn, varSesiones, null);
      poli.setStrOper(strTipoOper);
      int intValOper = PolizasContables.TICKET;
      intValOper = PolizasContables.FACTURA;
      if (!strEMP_URLCP.trim().isEmpty()) {
         poli.setStrURLServicio(strEMP_URLCP);
      }
      poli.setStrUserCte(strEMP_USERCP);
      poli.setStrPassCte(strEMP_PASSCP);
      poli.setDocumentMaster(document);
      //Validamos las cuentas a usar
      //Si el detalle de ventas NO es por producto

      //Obtenemos el Id de la Categoria del cliente
      int intCC1_ID = document.getFieldInt("CC1_ID");

      if (intCC1_ID == 0) {
         intCC1_ID = intCT_CATEGORIA1;
      }

      String strCTA_VENTAS = "";
      String strCTA_CLIENTE = "";
      String strCTA_RET_ISR = "";
      String strCTA_RET_IVA = "";

      if (intCC1_ID != 0) {

         try {
            //Consultamos  las operaciones
            String strsql = "select * from vta_cliecat1 where CC1_ID =" + intCC1_ID;
            ResultSet rs = oConn.runQuery(strsql);
            while (rs.next()) {
               strCTA_VENTAS = rs.getString("CTA_VENTAS");
               strCTA_CLIENTE = rs.getString("CTA_CLIENTE");
               strCTA_RET_ISR = rs.getString("CTA_RET_ISR");
               strCTA_RET_IVA = rs.getString("CTA_RET_IVA");
            }
            rs.close();
         } catch (SQLException ex) {
            log.error(ex.getMessage());
         }
      }
      log.debug("Ventas");
      //Ventas
      if (strCtaVtas.isEmpty()) {
         if (strCTA_VENTAS.equals("")) {
            log.debug("vta_empresas.3.." + strCtaVtasGlobal);
            poli.getLstCuentas().add(strCtaVtasGlobal);
         } else {
            log.debug("vta_cliecat1.2.." + strCTA_VENTAS);
            poli.getLstCuentas().add(strCTA_VENTAS);
         }
      } else {
         log.debug("vta_clientes.1.." + strCtaVtas);
         poli.getLstCuentas().add(strCtaVtas);
      }

      //IVA
      if (strCtaVtasIVATasa.isEmpty()) {
         poli.getLstCuentas().add(strCtaVtasIVAGlobal);
      } else {
         poli.getLstCuentas().add(strCtaVtasIVATasa);
      }

      //Cte
      log.debug("Cte");
      if (strCtaVtasCte.isEmpty()) {
         if (strCTA_CLIENTE.equals("")) {
            log.debug("vta_empresas.3.." + strCtaVtasCteGlobal);
            poli.getLstCuentas().add(strCtaVtasCteGlobal);
         } else {
            log.debug("vta_cliecat1.2.." + strCTA_CLIENTE);
            poli.getLstCuentas().add(strCTA_CLIENTE);
         }
      } else {
         log.debug("vta_clientes.1.." + strCtaVtasCte);
         poli.getLstCuentas().add(strCtaVtasCte);
      }
//            //Validamos si la cuenta de ventas se genera a partir de productos
//            if (intEMP_VTA_DETA == 1) {
//               poli.setBolVTA_DETA(true);
//               //Barremos productos para obtener cuentas agrupadas
//               this.getPoliDetaProd(poli, false, this.document.getFieldString("FAC_FOLIO"));
//            }
      //anadimos mas cuentas
      ArrayList<PoliCtas> lstCuentasAG = null;
      if (poli.getLstCuentasAG() != null) {
         lstCuentasAG = poli.getLstCuentasAG();
      } else {
         lstCuentasAG = new ArrayList<PoliCtas>();
      }
      if (document.getFieldInt("FAC_ESSERV") == 1) {
         //El detalle de las ventas
         int intCuantosItems = ObtenDetalleServicio(document, lstCuentasAG);
         if (intCuantosItems > 0) {
            poli.setBolVTA_DETA(true);
         }
      }
      //RETENCIONES
      log.debug("RETENCIONES");
      if (document.getFieldDouble("FAC_RETISR") > 0 || document.getFieldDouble("FAC_RETIVA") > 0) {
         if (document.getFieldDouble("FAC_RETISR") > 0) {
            if (strCTA_RET_ISR.equals("")) {
               log.debug("vta_empresas.3.." + strCT_CONTA_RET_ISR);
               poli.getLstCuentas().add(strCT_CONTA_RET_ISR);
            } else {
               log.debug("vta_cliecat1.2.." + strCTA_RET_ISR);
               poli.getLstCuentas().add(strCTA_RET_ISR);
            }
         } else {
            log.debug("vta_clientes.1.." + "");
            poli.getLstCuentas().add("");
         }
         if (document.getFieldDouble("FAC_RETIVA") > 0) {
            if (strCTA_RET_IVA.equals("")) {
               log.debug("vta_empresas.3.." + strCT_CONTA_RET_IVA);
               poli.getLstCuentas().add(strCT_CONTA_RET_IVA);
            } else {
               log.debug("vta_cliecat1.2.." + strCTA_RET_IVA);
               poli.getLstCuentas().add(strCTA_RET_IVA);
            }
         } else {
            log.debug("vta_clientes.1.." + "");
            poli.getLstCuentas().add("");
         }
      } else {
         poli.getLstCuentas().add("");
         poli.getLstCuentas().add("");
      }

      //Complementarios
      if (bolComplementarias) {

         //Complemento clientes
         PoliCtas pol = new PoliCtas();
         if (strCtaComp1Prov.isEmpty()) {
            pol.setStrCuenta(strCtaComp1);
         } else {
            pol.setStrCuenta(strCtaComp1Prov);
         }
         pol.setBolEsCargo(true);
         pol.setDblImporte(dblComplementaria);
         pol.setStrFolioRef(document.getFieldString("FAC_FOLIO_C"));
         lstCuentasAG.add(pol);

         //Solo lo asignamos si no tiene un listado de cuentas
         if (poli.getLstCuentasAG() == null) {
            poli.setLstCuentasAG(lstCuentasAG);
         }
      }

      //Buscamos documentos relacionados con la cuenta por pagar para enviarlos
      CargarArchivosFacturas(poli, document.getFieldInt("FAC_ID"), document);

      try {
         poli.callRemote(intId, intValOper);
         if (poli.strResultLast.startsWith("OK")) {
            this.intSucessFacturas++;
            //Marcamos la venta como procesada
            this.oConn.runQueryLMD("UPDATE " + strNomTablaMaster + " "
               + " SET " + strPrefijoMaster + "_EXEC_INTER_CP =  " + poli.strResultLast.replace("OK.", "")
               + " WHERE " + strPrefijoMaster + "_ID = " + intId);
         } else {
            //Marcamos la venta NO como procesada
            this.oConn.runQueryLMD("UPDATE " + strNomTablaMaster + " "
               + " SET " + strPrefijoMaster + "_EXEC_INTER_CP =  0 "
               + " WHERE " + strPrefijoMaster + "_ID = " + intId);
            this.intFailFacturas++;
            log.debug("strResultLast?" + poli.strResultLast);
            this.lstFailsFacturas.add("Error al generar la poliza para la factura con folio " + document.getFieldString("FAC_FOLIO_C") + " " + poli.strResultLast);
         }
      } catch (Exception ex) {
         //Marcamos la venta NO como procesada
         this.oConn.runQueryLMD("UPDATE " + strNomTablaMaster + " "
            + " SET " + strPrefijoMaster + "_EXEC_INTER_CP =  0 "
            + " WHERE " + strPrefijoMaster + "_ID = " + intId);
         this.intFailFacturas++;
         this.lstFailsFacturas.add("Error al generar la poliza para la factura con folio " + document.getFieldString("FAC_FOLIO_C") + " " + poli.strResultLast);
         log.error("Error calling..." + ex.getLocalizedMessage() + " " + ex.getMessage() + " ");

      }
   }

   /**
    * Genera las polizas contables de cuentas por pagar de un periodo en
    * particular
    *
    * @param intEMP_ID Es el id de empresa
    * @param strMesAnio Es le periodo donde tomara los movimientos(AAAAMM)
    * @param strEMP_PASSCP Es el password para la contabilidad
    * @param strEMP_USERCP Es el usuario para la contabilidad
    * @param strEMP_URLCP Es la url para la contabilidad
    * @param strCXPGlobal El la cuenta global de cxpagar
    * @param strCXPProvGlobal Es la cuenta de proveedor global
    * @param strCXPIVAGlobal Es la cuenta global de iva
    */
   public void GeneraContaCXP(int intEMP_ID, String strMesAnio, String strEMP_PASSCP, String strEMP_USERCP, String strEMP_URLCP,
      String strCXPGlobal, String strCXPProvGlobal, String strCXPIVAGlobal) {
      //Consultamos cuentas por pagar no anulados
      try {
         //Consultamos  las operaciones
         String strsqlmaster = "SELECT CXP_ID,TI_ID,CXP_MONEDA FROM vta_cxpagar where CXP_ANULADO = 0 "
            + " AND left(CXP_FECHA_PROVISION,6)='" + strMesAnio + "' and EMP_ID = " + intEMP_ID + " " + strFiltroCXP
            + " order by CXP_FECHA_PROVISION";
         ResultSet rsM = oConn.runQuery(strsqlmaster);
         while (rsM.next()) {
            //Recuperamos el documento
            int intId = rsM.getInt("CXP_ID");
            int intTI_ID = rsM.getInt("TI_ID");
            int intMonedaOpera = rsM.getInt("CXP_MONEDA");
            TableMaster document = new vta_cxpagar();
            document.ObtenDatos(intId, oConn);
            //Calcula la poliza contable
            CalculaPolizaContableCXP(intEMP_ID, strEMP_PASSCP, strEMP_USERCP, strEMP_URLCP,
               strCXPGlobal, strCXPProvGlobal, strCXPIVAGlobal, intMonedaOpera, document, intTI_ID, intId, "NEW");

         }
         rsM.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage());
      }
   }

   /**
    * Genera la poliza contable de una cuenta por pagar
    *
    * @param intEMP_ID Es el id de empresa
    * @param strEMP_PASSCP Es el password para la contabilidad
    * @param strEMP_USERCP Es el usuario para la contabilidad
    * @param strEMP_URLCP Es la url para la contabilidad
    * @param strCXPGlobal El la cuenta global de cxpagar
    * @param strCXPProvGlobal Es la cuenta de proveedor global
    * @param strCXPIVAGlobal Es la cuenta global de iva
    * @param intMonedaOpera Es el id de la moneda de operacion
    * @param document Es el documento
    * @param intTI_ID Es el id de la tasa de iva
    * @param intId Es el id de la operacion
    * @param strTipoOper Indica con NEW nueva poliza y con CANCEL poliza de
    * cancelacion
    */
   public void CalculaPolizaContableCXP(int intEMP_ID, String strEMP_PASSCP, String strEMP_USERCP, String strEMP_URLCP,
      String strCXPGlobal, String strCXPProvGlobal, String strCXPIVAGlobal, int intMonedaOpera, TableMaster document,
      int intTI_ID, int intId, String strTipoOper) {
      boolean bolComplementarias = false;
      double dblComplementaria = 0;
      String strCtaComp1 = "";
      String strCtaComp1Prov = "";
      //Evaluamos si tenemos que realizar conversion de monedas
      if (intMonedaOpera != this.intMonedaBase) {
         bolComplementarias = true;
         //Aplica conversion
         this.monedas.setBoolConversionAutomatica(true);
         double dblFactor = this.monedas.GetFactorConversion(document.getFieldString("CXP_FECHA"), 4, intMonedaOpera, intMonedaBase);

         dblComplementaria = document.getFieldDouble("CXP_TOTAL") * dblFactor;
         dblComplementaria -= document.getFieldDouble("CXP_TOTAL");

         document.setFieldDouble("CXP_IMPORTE", document.getFieldDouble("CXP_IMPORTE") * dblFactor);
         document.setFieldDouble("CXP_IMPUESTO1", document.getFieldDouble("CXP_IMPUESTO1") * dblFactor);
         document.setFieldDouble("CXP_TOTAL", document.getFieldDouble("CXP_TOTAL"));
      }

      String strCXPIVATasa = "";
      String strCXPVtas = "";
      String strCXPVtasProv = "";
      String strPV_CONTAPROVUSD = "";
      String strPV_CONTAPROVE = "";
      String strPV_CONTAPROVDC = "";
      String strPV_CONTA_RET_ISR = "";
      String strPV_CONTA_RET_IVA = "";
      String strPV_RFC = "";
      String strPV_RAZONSOCIAL = "";
      //Validamos la tasa de iva en base al detalle
      if (intTI_ID != 1) {
         boolean bolDiversosIvas = false;
         double dblTasaIvaDeta = 0;
         String strSqlT = "select CXPD_TASAIVA1 from vta_cxpagardetalle where CXP_ID = " + intId + " AND CXPD_IMPUESTO1>0 ";
         try {
            ResultSet rs = oConn.runQuery(strSqlT, true);
            while (rs.next()) {
               if (rs.getDouble("CXPD_TASAIVA1") != document.getFieldDouble("CXP_TASA1")) {
                  bolDiversosIvas = true;
                  dblTasaIvaDeta = rs.getDouble("CXPD_TASAIVA1");
                  break;
               }
            }
            rs.close();
            //Buscamos el id de la tasa del iva usado
            if (bolDiversosIvas) {
               strSqlT = "select TI_ID from vta_tasaiva where ti_tasa = " + dblTasaIvaDeta;
               rs = oConn.runQuery(strSqlT, true);
               while (rs.next()) {
                  intTI_ID = rs.getInt("TI_ID");
               }
               rs.close();
            }
         } catch (SQLException ex) {
            log.debug("ERROR:" + ex.getMessage());
         }
      }
      //Buscamos cuentas para los ivas
      String strSql = "SELECT TI_CTA_CONT,TI_CTA_CONT_COB,TI_CTA_CON_ACRE FROM vta_tasaiva "
         + " where TI_ID=" + intTI_ID + "";
      try {
         ResultSet rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            strCXPIVATasa = rs.getString("TI_CTA_CON_ACRE");
         }
         rs.close();
      } catch (SQLException ex) {
         log.debug("ERROR:" + ex.getMessage());

      }
      //Buscamos cuentas para los proveedores
      strSql = "SELECT PV_RAZONSOCIAL,PV_RFC,PV_CALLE,PV_COLONIA,PV_LOCALIDAD,PV_CONTAPROV_COMPL,"
         + " PV_MUNICIPIO,PV_ESTADO,PV_NUMERO,PV_NUMINT,PV_CONTAPROV,PV_CONTACOMP,"
         + " PV_CP,PV_DIASCREDITO,PV_LPRECIOS,PV_TIPOPERS"
         + " ,PV_CONTA_RET_ISR,PV_CONTA_RET_IVA"
         + " ,PV_CONTAPROVUSD,PV_CONTAPROVE,PV_CONTAPROVDC"
         + " FROM vta_proveedor "
         + " where PV_ID=" + document.getFieldInt("PV_ID") + "";
      try {
         ResultSet rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            strCXPVtas = rs.getString("PV_CONTACOMP");
            strCXPVtasProv = rs.getString("PV_CONTAPROV");
            strPV_CONTA_RET_ISR = rs.getString("PV_CONTA_RET_ISR");
            strPV_CONTA_RET_IVA = rs.getString("PV_CONTA_RET_IVA");
            strCtaComp1Prov = rs.getString("PV_CONTAPROV_COMPL");
            strPV_CONTAPROVUSD = rs.getString("PV_CONTAPROVUSD");
            strPV_CONTAPROVE = rs.getString("PV_CONTAPROVE");
            strPV_CONTAPROVDC = rs.getString("PV_CONTAPROVDC");
            strPV_RFC = rs.getString("PV_RFC");
            strPV_RAZONSOCIAL = rs.getString("PV_RAZONSOCIAL");
         }
         rs.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage());
      }
      // <editor-fold defaultstate="collapsed" desc="Consultamos la info de la empresa">
      strSql = "SELECT EMP_CTAPROV_COMPL,EMP_USERCP,EMP_PASSCP,EMP_URLCP "
         + " FROM vta_empresas "
         + " WHERE EMP_ID = " + intEMP_ID + "";
      try {
         ResultSet rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            strCtaComp1 = rs.getString("EMP_CTAPROV_COMPL");
            strEMP_USERCP = rs.getString("EMP_USERCP");
            strEMP_PASSCP = rs.getString("EMP_PASSCP");
            strEMP_URLCP = rs.getString("EMP_URLCP");
         }
         rs.close();
      } catch (SQLException ex) {
         log.debug("ERROR:" + ex.getMessage());

      }
      // </editor-fold>
      //Actualizamos la poliza contable
      PolizasContables poli = new PolizasContables(this.oConn, this.varSesiones, null);
      poli.setStrOper(strTipoOper);
      int intValOper = PolizasContables.CUENTASXPAGAR;
      if (!strEMP_URLCP.trim().isEmpty()) {
         poli.setStrURLServicio(strEMP_URLCP);
      }
      poli.setStrUserCte(strEMP_USERCP);
      poli.setStrPassCte(strEMP_PASSCP);
      poli.setDocumentMaster(document);
      //Validamos las cuentas a usar
      //Si el detalle de ventas NO es por producto
      //Buscamos los centros de gastos de cada partida para generarlos

      //Ventas
      if (strCXPVtas.isEmpty()) {
         poli.getLstCuentas().add(strCXPGlobal);
      } else {
         poli.getLstCuentas().add(strCXPVtas);
      }
      //IVA
      if (strCXPIVATasa.isEmpty()) {
         poli.getLstCuentas().add(strCXPIVAGlobal);
      } else {
         poli.getLstCuentas().add(strCXPIVATasa);
      }
      //Proveedor
      if (strCXPVtasProv.isEmpty()) {
         poli.getLstCuentas().add(strCXPProvGlobal);
      } else // <editor-fold defaultstate="collapsed" desc="Validacion de la cuenta de proveedor de acuerdo a la moneda">
      //Pesos
      {
         if (intMonedaOpera == 1) {
            poli.getLstCuentas().add(strCXPVtasProv);
         } else //USD
         {
            if (intMonedaOpera == 2) {
               if (!strPV_CONTAPROVUSD.isEmpty()) {
                  poli.getLstCuentas().add(strPV_CONTAPROVUSD);
               } else {
                  poli.getLstCuentas().add(strCXPVtasProv);
               }
            } else if (intMonedaOpera == 3) {
               if (!strPV_CONTAPROVE.isEmpty()) {
                  poli.getLstCuentas().add(strPV_CONTAPROVE);
               } else {
                  poli.getLstCuentas().add(strCXPVtasProv);
               }
            } else if (intMonedaOpera == 4) {
               if (!strPV_CONTAPROVDC.isEmpty()) {
                  poli.getLstCuentas().add(strPV_CONTAPROVDC);
               } else {
                  poli.getLstCuentas().add(strCXPVtasProv);
               }
            } else {
               poli.getLstCuentas().add(strCXPVtasProv);
            } // </editor-fold>
         }      //RETENCIONES
      }
      if (document.getFieldDouble("CXP_RETISR") > 0 || document.getFieldDouble("CXP_RETIVA") > 0) {
         if (document.getFieldDouble("CXP_RETISR") > 0) {
            poli.getLstCuentas().add(strPV_CONTA_RET_ISR);
         } else {
            poli.getLstCuentas().add("");
         }
         if (document.getFieldDouble("CXP_RETIVA") > 0) {
            poli.getLstCuentas().add(strPV_CONTA_RET_IVA);
         } else {
            poli.getLstCuentas().add("");
         }
      } else {
         poli.getLstCuentas().add("");
         poli.getLstCuentas().add("");
      }
      //Validamos si la cuenta de ventas se genera a partir de productos
      //if (intEMP_VTA_DETA == 1) {
      poli.setBolVTA_DETA(true);
      //Barremos gastos para obtener cuentas agrupadas
      //Ponemos el UUID en el folio...
      String strCXP_UUID_ = document.getFieldString("CXP_UUID");
      if (strCXP_UUID_.length() > 4) {
         strCXP_UUID_ = strCXP_UUID_.substring(strCXP_UUID_.length() - 4, strCXP_UUID_.length());
      }
      if (strCXP_UUID_.length() == 0) {
         strCXP_UUID_ = document.getFieldString("CXP_FOLIO");
      }
      this.getPoliDetaGasto(document, poli, true, strCXP_UUID_, intMonedaOpera, strPV_RFC);
      //Complementarios
      if (bolComplementarias) {
         //anadimos mas cuentas
         ArrayList<PoliCtas> lstCuentasAG = null;
         if (poli.getLstCuentasAG() != null) {
            lstCuentasAG = poli.getLstCuentasAG();
         } else {
            lstCuentasAG = new ArrayList<PoliCtas>();
         }

         //Complemento proveedores
         PoliCtas pol = new PoliCtas();
         if (strCtaComp1Prov.isEmpty()) {
            pol.setStrCuenta(strCtaComp1);
         } else {
            pol.setStrCuenta(strCtaComp1Prov);
         }
         pol.setBolEsCargo(false);
         pol.setDblImporte(dblComplementaria);
         pol.setStrRFC(strPV_RFC);
         pol.setStrUUID(document.getFieldString("CXP_UUID"));
         pol.setStrFolioRef(strCXP_UUID_);
         lstCuentasAG.add(pol);
         //Solo lo asignamos si no tiene un listado de cuentas
         if (poli.getLstCuentasAG() == null) {
            poli.setLstCuentasAG(lstCuentasAG);
         }

      }
      poli.setDocumentMaster(document);
      //Definimos el rfc del proveedor
      poli.setStrRFCProveedor(strPV_RFC);

      //}
      //Buscamos documentos relacionados con la cuenta por pagar para enviarlos
      CargarArchivos(poli, document.getFieldInt("CXP_ID"), document);
      //Llamamos al webservice
      try {
         poli.callRemote(intId, intValOper, strPV_RAZONSOCIAL, strCXP_UUID_);
         if (poli.strResultLast.startsWith("OK")) {
            this.intSucessCXP++;
            //Marcamos la CXP como procesada 
            this.oConn.runQueryLMD("UPDATE vta_cxpagar "
               + " SET " + "CXP_EXEC_INTER_CP =  " + poli.strResultLast.replace("OK.", "")
               + " WHERE " + "CXP_ID = " + intId);
         } else {
            this.intFailCXP++;
            log.debug("strResultLast?" + poli.strResultLast);
            this.lstFailsCXP.add("Error al generar la poliza para la cuentas por pagar con folio " + document.getFieldString("CXP_FOLIO") + " " + poli.strResultLast);
            //Marcamos la CXP como NO procesada 
            this.oConn.runQueryLMD("UPDATE vta_cxpagar "
               + " SET " + "CXP_EXEC_INTER_CP =  0"
               + " WHERE " + "CXP_ID = " + intId);

         }
      } catch (Exception ex) {
         this.intFailCXP++;
         this.lstFailsCXP.add("Error al generar la poliza para la cuentas por pagar con folio " + document.getFieldString("CXP_FOLIO") + " " + poli.strResultLast);
         log.debug("Error in call webservice?" + ex.getMessage() + " cxp_id:" + intId);

         //Marcamos la CXP como NO procesada 
         this.oConn.runQueryLMD("UPDATE vta_cxpagar "
            + " SET " + "CXP_EXEC_INTER_CP =  0"
            + " WHERE " + "CXP_ID = " + intId);
      }
   }

   /**
    *
    * Genera la polizacontable de un cobro de una factura o ticket de un periodo
    * de tiempo
    *
    * @param intEMP_ID Es el id de la empresa
    * @param strMesAnio Es el periodo(AAAAMM)
    * @param strEMP_PASSCP Es el password para la contabilidad
    * @param strEMP_USERCP Es el usuario para la contabilidad
    * @param strEMP_URLCP Es la url para la contabilidad
    */
   public void GeneraContaCobros(int intEMP_ID, String strMesAnio, String strEMP_PASSCP, String strEMP_USERCP, String strEMP_URLCP) {
      //Consultamos cobros no anulados
      try {
         //Consultamos tas las operac ion bd local que no sean masivos
         String strsqlmaster = "SELECT MC_ID,MC_MONEDA  FROM vta_mov_cte where  "
            + "  left(MC_FECHA,6)='" + strMesAnio + "' AND MC_ANULADO = 0 and MC_ESPAGO = 1 AND EMP_ID = " + intEMP_ID + " "
            + " AND NC_ID = 0 AND MCM_ID = 0 " + strFiltroCobros;
         ResultSet rsM = oConn.runQuery(strsqlmaster);
         while (rsM.next()) {
            //Recuperamos el documento
            int intId = rsM.getInt("MC_ID");
            int intMonedaOpera = rsM.getInt("MC_MONEDA");
            TableMaster document = new vta_mov_cte();
            document.ObtenDatos(intId, oConn);
            this.dblTmp1 = 0;
            this.dblTmp2 = 0;
            this.dblTmp3 = 0;
            CalculaPolizaContableCobros(intMonedaOpera, document, intId, "NEW");
         }
         rsM.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage());
      }
      //Intentamos los pagos masivos
      //Consultamos cobros no anulados
      try {
         //Consultamos tas las operac ion bd local que no sean masivos
         String strsqlmaster = "SELECT DISTINCT MCM_ID  FROM vta_mov_cte where  "
            + "  left(MC_FECHA,6)='" + strMesAnio + "' AND MC_ANULADO = 0 and MC_ESPAGO = 1 AND EMP_ID = " + intEMP_ID + " AND MCM_ID <> 0 " + strFiltroCobrosMasivos;
         ResultSet rsM = oConn.runQuery(strsqlmaster);
         while (rsM.next()) {
            //Recuperamos el documento
            int intIdMCM = rsM.getInt("MCM_ID");
            PagosMasivos pagos = new PagosMasivos(oConn, varSesiones, null);
            pagos.getMasivo().setFieldInt("MCM_ID", intIdMCM);
            pagos.Init();
            //Realizamos la sumatoria
            Iterator<movCliente> it = pagos.lstPagos.iterator();
            double dblTotalPago = 0;
            double dblTotalImpuesto1 = 0;
            while (it.hasNext()) {
               movCliente deta = it.next();
               dblTotalPago += deta.getCta_clie().getFieldDouble("MC_ABONO");
               dblTotalImpuesto1 += deta.getCta_clie().getFieldDouble("MC_IMPUESTO1");
            }
            this.dblTmp1 = 0;
            this.dblTmp2 = 0;
            this.dblTmp3 = 0;
            this.setDblTmp2(dblTotalPago);
            this.setDblTmp3(dblTotalImpuesto1);
            this.pagosMasivos = pagos;
            //Proceso masivo
            CalculaPolizaContableCobros(pagos.lstPagos.get(0).getCta_clie().getFieldInt("MC_MONEDA"), pagos.lstPagos.get(0).getCta_clie(), pagos.lstPagos.get(0).getCta_clie().getFieldInt("MC_ID"), "NEW");

         }
         rsM.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage());
      }
   }

   /**
    * Genera la polizacontable de un cobro de una factura o ticket
    *
    * @param intMonedaOpera
    * @param document Es el documento
    * @param intId Es el id de la operacion
    * @param strTipoOper Indica con NEW nueva poliza y con CANCEL poliza de
    * cancelacion
    */
   public void CalculaPolizaContableCobros(int intMonedaOpera, TableMaster document, int intId, String strTipoOper) {
      // <editor-fold defaultstate="collapsed" desc="Declaracion de variables">
      boolean bolComplementarias = false;
      double dblComplementaria = 0;
      double dblComplementaria2 = 0;
      String strCtaComp1 = "";
      String strCtaComp2 = "";
      String strCtaComp1Prov = "";
      String strCtaGlobalAnticipo = "";
      String strCtaGlobalAnticipoComp = "";
      double dblImportePagoDoc = document.getFieldDouble("MC_ABONO");
      //Asignamos importe de pago global
      if (this.dblTmp2 != 0) {
         dblImportePagoDoc = this.dblTmp2;
      }
      // </editor-fold>
      // <editor-fold defaultstate="collapsed" desc="Evaluamos si tenemos que realizar conversion de monedas">
      if (intMonedaOpera != this.intMonedaBase) {
         bolComplementarias = true;
         //Validamos si hay una conversion a pesos
         if (document.getFieldDouble("MC_TASAPESO") != 1 && document.getFieldDouble("MC_TASAPESO") != 0) {
            double dblFactor = document.getFieldDouble("MC_TASAPESO");
//            document.setFieldDouble("MC_ABONO", dblImportePagoDoc * dblFactor);
            dblComplementaria2 = dblImportePagoDoc * dblFactor;
            dblComplementaria = dblImportePagoDoc * dblFactor;
            dblComplementaria -= dblImportePagoDoc;
            dblComplementaria2 -= dblImportePagoDoc;
            document.setFieldDouble("MC_IMPUESTO1", document.getFieldDouble("MC_IMPUESTO1") * dblFactor);
            //Impuesto global
            if (this.dblTmp3 != 0) {
               this.dblTmp3 = this.dblTmp3 * dblFactor;
            }
         } else {
            //Aplica conversion
            this.monedas.setBoolConversionAutomatica(true);
            double dblFactor = this.monedas.GetFactorConversion(document.getFieldString("MC_FECHA"), 4, intMonedaOpera, intMonedaBase);
//            document.setFieldDouble("MC_ABONO", dblImportePagoDoc * dblFactor);
            dblComplementaria2 = dblImportePagoDoc * dblFactor;
            dblComplementaria = dblImportePagoDoc * dblFactor;
            dblComplementaria -= dblImportePagoDoc;
            dblComplementaria2 -= dblImportePagoDoc;
            document.setFieldDouble("MC_IMPUESTO1", document.getFieldDouble("MC_IMPUESTO1") * dblFactor);
            //Impuesto global
            if (this.dblTmp3 != 0) {
               this.dblTmp3 = this.dblTmp3 * dblFactor;
            }
         }
      }
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Obtenemos los datos globales de contabilidad">
      int intFAC_ID = document.getFieldInt("FAC_ID");
      int intTKT_ID = document.getFieldInt("TKT_ID");
      int intTI_ID = 0;
      int intEMP_ID = 0;
      double dblFactorTasa = 0;
      String strFolioVta = "";
      String strNomFolioVta = "";
      String strNomMonedaVta = "";
      String strClieVta = "Anticipo";
      String strNomClieVta = "";
      String strSql = "";
      String strEMP_PASSCP = "";
      String strEMP_USERCP = "";
      String strEMP_URLCP = "";
      int intFAC_MONEDA = 0;
      if (intFAC_ID != 0 || intTKT_ID != 0) {
         if (intFAC_ID != 0) {
            strSql = "select EMP_ID,TI_ID,TI_ID2,TI_ID3,FAC_FOLIO,FAC_FOLIO_C,FAC_RAZONSOCIAL,FAC_MONEDA from vta_facturas where FAC_ID = " + intFAC_ID;
            strNomFolioVta = "FAC_FOLIO_C";
            strNomClieVta = "FAC_RAZONSOCIAL";
            strNomMonedaVta = "FAC_MONEDA";
         } else {
            strSql = "select EMP_ID,TI_ID,TI_ID2,TI_ID3,TKT_FOLIO,TKT_RAZONSOCIAL,TKT_MONEDA from vta_tickets where TKT_ID = " + intTKT_ID;
            strNomFolioVta = "TKT_FOLIO";
            strNomClieVta = "TKT_RAZONSOCIAL";
            strNomMonedaVta = "TKT_MONEDA";
         }
         try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
               intEMP_ID = rs.getInt("EMP_ID");
               intTI_ID = rs.getInt("TI_ID");
               strFolioVta = rs.getString(strNomFolioVta);
               strClieVta = rs.getString(strNomClieVta);
               intFAC_MONEDA = rs.getInt(strNomMonedaVta);
            }
            rs.close();
         } catch (SQLException ex) {
            log.debug("ERROR:" + ex.getMessage());

         }
      } else {
         intEMP_ID = document.getFieldInt("EMP_ID");
         //Anticipos y movimientos no vinculados
         strFolioVta = document.getFieldString("MC_FOLIO");
         strSql = "select CT_RAZONSOCIAL,TI_ID from vta_cliente where CT_ID = " + document.getFieldInt("CT_ID");
         try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
               strClieVta = rs.getString("CT_RAZONSOCIAL");
               intTI_ID = rs.getInt("TI_ID");
            }
            rs.close();
         } catch (SQLException ex) {
            log.debug("ERROR:" + ex.getMessage());
         }
      }
      // </editor-fold>
      // <editor-fold defaultstate="collapsed" desc="Obtenemos datos de la cuenta de la empresa">
      int intEMP_USECONTA = 0;
      String strCtaVtasIVAGlobal = "";
      String strCtaVtasIVATasa = "";
      String strCtaVtasIVATasaCob = "";
      String strCtaVtasCteGlobal = "";
      String strCtaVtasCte = "";
      String strCtaVtasCteAnticipo = "";
      String strCtaVtasCteAnticipoCompl = "";
      String strCtaBcos = "";

      // </editor-fold>
      // <editor-fold defaultstate="collapsed" desc="Consultamos la info de la empresa">
      strSql = "SELECT EMP_NOAPROB,EMP_FECHAPROB,EMP_NOSERIECERT,EMP_TIPOCOMP,EMP_URLCP,"
         + " EMP_USECONTA,EMP_CTAVTA,EMP_CTAIVA,EMP_CTACTE,EMP_PASSCP,EMP_USERCP,EMP_CTACTE_COMPL"
         + " ,EMP_CTA_CTE_ANTICIPO,EMP_CTACTE_COMPL_ANTI "
         + " FROM vta_empresas "
         + " WHERE EMP_ID = " + intEMP_ID + "";
      try {
         ResultSet rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            intEMP_USECONTA = rs.getInt("EMP_USECONTA");
            strCtaVtasIVAGlobal = rs.getString("EMP_CTAIVA");
            strCtaVtasCteGlobal = rs.getString("EMP_CTACTE");
            strEMP_PASSCP = rs.getString("EMP_PASSCP");
            strEMP_USERCP = rs.getString("EMP_USERCP");
            strEMP_URLCP = rs.getString("EMP_URLCP");
            strCtaComp1 = rs.getString("EMP_CTACTE_COMPL");
            strCtaGlobalAnticipo = rs.getString("EMP_CTA_CTE_ANTICIPO");
            strCtaGlobalAnticipoComp = rs.getString("EMP_CTACTE_COMPL_ANTI");
         }
         rs.close();
      } catch (SQLException ex) {
         log.debug("ERROR:" + ex.getMessage());

      }
      // </editor-fold>
      // <editor-fold defaultstate="collapsed" desc="Solo aplica si esta configurada la contabilidad">
      log.debug("intEMP_USECONTA:" + intEMP_USECONTA);
      if (intEMP_USECONTA == 1) {
         // <editor-fold defaultstate="collapsed" desc="Buscamos cuenta para bancos">
         int intIdBanco = 0;
         strSql = "SELECT BC_ID "
            + " FROM vta_mov_cta_bcos "
            + " WHERE MC_ID = " + document.getFieldInt("MC_ID") + "";
         try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
               intIdBanco = rs.getInt("BC_ID");
            }
            rs.close();
         } catch (SQLException ex) {
            log.debug("ERROR:" + ex.getMessage());

         }
         //Buscamos el link del pago ya que es masivo
         if (intIdBanco == 0) {
            strSql = "select a.BC_ID,a.MCB_NOCHEQUE from vta_mov_cta_bcos a, vta_mov_cta_bcos_rela b\n"
               + "where a.MCB_ID = b.MCB_ID and b.MC_ID = \n" + intId;
            try {
               ResultSet rs = oConn.runQuery(strSql, true);
               while (rs.next()) {
                  intIdBanco = rs.getInt("BC_ID");
               }
               rs.close();
            } catch (SQLException ex) {
               log.debug("ERROR:" + ex.getMessage());

            }
         }
         //Evaluamos la moneda para detectar el banco
         if (intIdBanco == 0) {
            if (document.getFieldInt("MC_MONEDA") == 1) {
               intIdBanco = 1;
            } else {
               intIdBanco = 2;
            }
         }
         strSql = "SELECT BC_CTA_CONT,BC_CTA_CONT_COMPL "
            + " FROM vta_bcos "
            + " WHERE BC_ID = " + intIdBanco + "";
         try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
               strCtaBcos = rs.getString("BC_CTA_CONT");
               strCtaComp2 = rs.getString("BC_CTA_CONT_COMPL");
            }
            rs.close();
         } catch (SQLException ex) {
            log.debug("ERROR:" + ex.getMessage());

         }
         // </editor-fold>
         // <editor-fold defaultstate="collapsed" desc="Buscamos cuenta para clientes">
         strSql = "SELECT CT_CONTACTE,CT_CONTACTE_COMPL,CT_CTA_ANTICIPO,CT_CTACTE_COMPL_ANTI FROM vta_cliente "
            + " where CT_ID=" + document.getFieldInt("CT_ID") + "";
         try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
               strCtaVtasCte = rs.getString("CT_CONTACTE");
               strCtaComp1Prov = rs.getString("CT_CONTACTE_COMPL");
               strCtaVtasCteAnticipo = rs.getString("CT_CTA_ANTICIPO");
               strCtaVtasCteAnticipoCompl = rs.getString("CT_CTACTE_COMPL_ANTI");
            }
            rs.close();
         } catch (SQLException ex) {
            log.debug("ERROR:" + ex.getMessage());

         }
         // </editor-fold>
         // <editor-fold defaultstate="collapsed" desc="Buscamos cuentas para los ivas">
         strSql = "SELECT TI_CTA_CONT,TI_CTA_CONT_COB,TI_TASA FROM vta_tasaiva "
            + " where TI_ID=" + intTI_ID + "";
         try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
               strCtaVtasIVATasa = rs.getString("TI_CTA_CONT");
               strCtaVtasIVATasaCob = rs.getString("TI_CTA_CONT_COB");
               dblFactorTasa = rs.getDouble("TI_TASA");
            }
            rs.close();
         } catch (SQLException ex) {
            log.debug("ERROR:" + ex.getMessage());

         }
         // </editor-fold>

         // <editor-fold defaultstate="collapsed" desc="Actualizamos la poliza contable">
         PolizasContables poli = new PolizasContables(this.oConn, this.varSesiones, null);
         poli.setStrOper(strTipoOper);
         poli.setStrUserCte(strEMP_USERCP);
         poli.setStrPassCte(strEMP_PASSCP);
         poli.setStrURLServicio(strEMP_URLCP);
         // <editor-fold defaultstate="collapsed" desc="Pagos masivos">
         if (pagosMasivos != null) {
            poli.setPagosMasivos(pagosMasivos);
            //Buscamos el id del pago masivo
            strSql = "SELECT MCM_EXEC_INTER_CP FROM vta_mov_cte_mas "
               + " where MCM_ID=" + pagosMasivos.getMasivo().getFieldInt("MCM_ID") + "";
            try {
               ResultSet rs = oConn.runQuery(strSql, true);
               while (rs.next()) {
                  int intPoliMasivo = rs.getInt("MCM_EXEC_INTER_CP");
                  poli.setIntIdPoliMasivo(intPoliMasivo);
               }
               rs.close();
            } catch (SQLException ex) {
               log.debug("ERROR:" + ex.getMessage());

            }
         }
         // </editor-fold>
         // </editor-fold>

         // <editor-fold defaultstate="collapsed" desc="Caso 1 pago normal">
         if (document.getFieldInt("MC_ANTICIPO") == 0
            && document.getFieldInt("MC_USA_ANTICIPO") == 0) {
            //Validamos las cuentas a usar
            //Bancos
            poli.getLstCuentas().add(strCtaBcos);
            //Cte
            if (strCtaVtasCte.isEmpty()) {
               poli.getLstCuentas().add(strCtaVtasCteGlobal);
            } else {
               poli.getLstCuentas().add(strCtaVtasCte);
            }
            //IVA
            if (strCtaVtasIVATasa.isEmpty()) {
               poli.getLstCuentas().add(strCtaVtasIVAGlobal);
            } else {
               poli.getLstCuentas().add(strCtaVtasIVATasa);
            }
            //IVA COBRADO
            poli.getLstCuentas().add(strCtaVtasIVATasaCob);

            // <editor-fold defaultstate="collapsed" desc="Complementarios">
            if (bolComplementarias) {
               //anadimos mas cuentas
               ArrayList<PoliCtas> lstCuentasAG = new ArrayList<PoliCtas>();
               //Complemento clientes
               PoliCtas pol = new PoliCtas();
               //Solo si el cliente es extranjero
               if (intFAC_MONEDA != 1) {
                  if (strCtaComp1Prov.isEmpty()) {
                     pol.setStrCuenta(strCtaComp1);
                  } else {
                     pol.setStrCuenta(strCtaComp1Prov);
                  }
                  pol.setBolEsCargo(false);
                  pol.setDblImporte(dblComplementaria);
                  pol.setStrFolioRef(strFolioVta);
                  lstCuentasAG.add(pol);
               } else {
                  log.debug("Se hace ajuste por el cliente " + dblComplementaria2);
                  poli.setBolUsaComplementoAjuste(true);
                  poli.setDblComplementoAjuste(dblComplementaria2);
               }
               //Complemento bancos
               pol = new PoliCtas();
               pol.setStrCuenta(strCtaComp2);
               pol.setBolEsCargo(true);
               pol.setDblImporte(dblComplementaria);
               pol.setStrFolioRef(strFolioVta);
               lstCuentasAG.add(pol);

               poli.setLstCuentasAG(lstCuentasAG);
            }
            // </editor-fold>
         }
         // </editor-fold>

         // <editor-fold defaultstate="collapsed" desc="Caso 2 anticipo">
         if (document.getFieldInt("MC_ANTICIPO") == 1) {
            //Asignamos el folio del pago
            strFolioVta = document.getFieldString("MC_FOLIO");
            poli.setBolEsAnticipo(true);
            //Validamos las cuentas a usar
            //Bancos
            poli.getLstCuentas().add(strCtaBcos);
            //Cte
            if (strCtaVtasCteAnticipo.isEmpty()) {
               poli.getLstCuentas().add(strCtaGlobalAnticipo);
            } else {
               poli.getLstCuentas().add(strCtaVtasCteAnticipo);
            }

            // <editor-fold defaultstate="collapsed" desc="Complementarios">
            if (bolComplementarias) {
               //anadimos mas cuentas
               ArrayList<PoliCtas> lstCuentasAG = new ArrayList<PoliCtas>();
               //Complemento proveedores
               PoliCtas pol = new PoliCtas();
               if (strCtaVtasCteAnticipoCompl.isEmpty()) {
                  pol.setStrCuenta(strCtaGlobalAnticipoComp);
               } else {
                  pol.setStrCuenta(strCtaVtasCteAnticipoCompl);
               }
               pol.setBolEsCargo(false);
               pol.setDblImporte(dblComplementaria);
               pol.setStrFolioRef(strFolioVta);
               lstCuentasAG.add(pol);
               //Complemento bancos
               pol = new PoliCtas();
               pol.setStrCuenta(strCtaComp2);
               pol.setBolEsCargo(true);
               pol.setDblImporte(dblComplementaria);
               pol.setStrFolioRef(strFolioVta);
               lstCuentasAG.add(pol);

               poli.setLstCuentasAG(lstCuentasAG);
            }
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="CALCULO DEL IVA de los anticipos">
            if (intTI_ID > 0) {
               if (intTI_ID != 3 && intTI_ID != 4) {
                  ArrayList<PoliCtas> lstCuentasAG = null;
                  //del monto que estan pagando entre 1.16 y luego * .16
                  double dblImpuesto1 = dblImportePagoDoc / (1 + (dblFactorTasa / 100));
                  dblImpuesto1 = dblImpuesto1 * (dblFactorTasa / 100);
                  //Evaluamos si hay listado para agregar
                  if (poli.getLstCuentasAG() == null) {
                     lstCuentasAG = new ArrayList<PoliCtas>();
                     poli.setLstCuentasAG(lstCuentasAG);
                  } else {
                     lstCuentasAG = poli.getLstCuentasAG();
                  }
                  //Objeto polizas deta
                  PoliCtas pol = new PoliCtas();
                  if (strCtaVtasIVATasa.isEmpty()) {
                     pol.setStrCuenta(strCtaVtasIVAGlobal);
                  } else {
                     pol.setStrCuenta(strCtaVtasIVATasa);
                  }
                  pol.setBolEsCargo(true);
                  pol.setDblImporte(dblImpuesto1);
                  pol.setStrFolioRef(strFolioVta);
                  lstCuentasAG.add(pol);

                  //Complemento IVAS
                  pol = new PoliCtas();
                  pol.setStrCuenta(strCtaVtasIVATasaCob);
                  pol.setBolEsCargo(false);
                  pol.setDblImporte(dblImpuesto1);
                  pol.setStrFolioRef(strFolioVta);
                  lstCuentasAG.add(pol);
               }
            }
            // </editor-fold>
         }
         // </editor-fold>

         // <editor-fold defaultstate="collapsed" desc="Caso 3 uso de anticipo">
         if (document.getFieldInt("MC_USA_ANTICIPO") == 1) {
            poli.setBolUsaAnticipo(true);
            if (strFolioVta.isEmpty()) {
               strFolioVta = document.getFieldString("MC_FOLIO");
            }
            //Validamos las cuentas a usar
            //Anticipo
            if (strCtaVtasCteAnticipo.isEmpty()) {
               poli.getLstCuentas().add(strCtaGlobalAnticipo);
            } else {
               poli.getLstCuentas().add(strCtaVtasCteAnticipo);
            }
            //Cliente
            //Cte
            if (strCtaVtasCte.isEmpty()) {
               poli.getLstCuentas().add(strCtaVtasCteGlobal);
            } else {
               poli.getLstCuentas().add(strCtaVtasCte);
            }
            //IVA
            if (strCtaVtasIVATasa.isEmpty()) {
               poli.getLstCuentas().add(strCtaVtasIVAGlobal);
            } else {
               poli.getLstCuentas().add(strCtaVtasIVATasa);
            }
            //IVA COBRADO
            poli.getLstCuentas().add(strCtaVtasIVATasaCob);
         }
         // </editor-fold>
         //Asignamos documento
         poli.setDocumentMaster(document);
         poli.setStrURLServicio(strEMP_URLCP);
         // <editor-fold defaultstate="collapsed" desc="Guardamos la poliza">
         try {
            //Enviamos importe global para pagos...
            if (this.dblTmp2 != 0) {
               poli.setDblTmp2(dblTmp2);
               poli.setDblTmp3(dblTmp3);
            }
            poli.callRemote(document.getFieldInt("MC_ID"), PolizasContables.RECIBOS, strClieVta, strFolioVta);
            if (poli.strResultLast.startsWith("OK")) {
               this.intSucessCobros++;
               if (pagosMasivos == null) {
                  this.oConn.runQueryLMD("UPDATE vta_mov_cte "
                     + " SET MC_EXEC_INTER_CP =  " + poli.strResultLast.replace("OK.", "")
                     + " WHERE MC_ID = " + intId);

               } else {
                  //Marcamos el pago como procesada
                  this.oConn.runQueryLMD("UPDATE vta_mov_cte_mas "
                     + " SET MCM_EXEC_INTER_CP =  " + poli.strResultLast.replace("OK.", "")
                     + " WHERE MCM_ID = " + pagosMasivos.getMasivo().getFieldInt("MCM_ID"));
               }
            } else {
               this.intFailCobros++;
               this.lstFailsCobros.add("Error al generar la poliza para el cobro con folio " + strFolioVta + " " + poli.strResultLast);
               //Marcamos el pago NO como procesada
               if (pagosMasivos == null) {
                  this.oConn.runQueryLMD("UPDATE vta_mov_cte "
                     + " SET MC_EXEC_INTER_CP =  0 "
                     + " WHERE MC_ID = " + intId);

               } else {
                  //Marcamos el pago como procesada
                  this.oConn.runQueryLMD("UPDATE vta_mov_cte_mas "
                     + " SET MCM_EXEC_INTER_CP =  0 "
                     + " WHERE MCM_ID = " + pagosMasivos.getMasivo().getFieldInt("MCM_ID"));
               }
            }
         } catch (Exception ex) {
            log.error("Error in call webservice?" + ex.getMessage());
            this.lstFailsCobros.add("Error al generar la poliza para el cobro con folio " + strFolioVta + " " + poli.strResultLast);
            this.intFailCobros++;
            //Marcamos el pago NO como procesada
            if (pagosMasivos == null) {
               this.oConn.runQueryLMD("UPDATE vta_mov_cte "
                  + " SET MC_EXEC_INTER_CP =  0 "
                  + " WHERE MC_ID = " + intId);

            } else {
               //Marcamos el pago como procesada
               this.oConn.runQueryLMD("UPDATE vta_mov_cte_mas "
                  + " SET MCM_EXEC_INTER_CP =  0 "
                  + " WHERE MCM_ID = " + pagosMasivos.getMasivo().getFieldInt("MCM_ID"));
            }
         }
         // </editor-fold>

      }
      // </editor-fold>    
   }

   /**
    * Genera la poliza contable de un pago de una cuenta por pagar, por un
    * periodo determinado
    *
    * @param intEMP_ID Es el id de la empresa
    * @param strMesAnio Es el periodo(AAAAMM)
    * @param strEMP_PASSCP Es el password para la contabilidad
    * @param strEMP_USERCP Es el usuario para la contabilidad
    * @param strEMP_URLCP Es la url para la contabilidad
    */
   public void GeneraContaPagos(int intEMP_ID, String strMesAnio, String strEMP_PASSCP, String strEMP_USERCP, String strEMP_URLCP) {
      //Consultamos pagos no anulados
      try {
         //Consultamos tas las operac ion bd local
         String strsqlmaster = "SELECT *  FROM vta_mov_prov where  "
            + "  left(MP_FECHA,6)='" + strMesAnio + "' AND MP_ANULADO = 0 and MP_ESPAGO = 1 and EMP_ID = " + intEMP_ID + " AND MPM_ID = 0 " + strFiltroPagos;
         ResultSet rsM = oConn.runQuery(strsqlmaster);
         while (rsM.next()) {
            //Recuperamos el documento
            int intId = rsM.getInt("MP_ID");
            int intMonedaOpera = rsM.getInt("MP_MONEDA");
            TableMaster document = new vta_mov_prov();
            document.ObtenDatos(intId, oConn);
            this.dblTmp1 = 0;
            this.dblTmp2 = 0;
            this.dblTmp3 = 0;
            this.dblTmp4 = 0;
            this.dblTmp5 = 0;
            this.dblTmp6 = 0;
            this.dblTmp7 = 0;
            this.dblTmp8 = 0;
            this.dblTmp9 = 0;
            this.dblTmp10 = 0;
            CalculaPolizaContablePagos(intMonedaOpera, document, intId, "NEW");
         }
         rsM.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage());
      }
      //Consultamos pagos no anulados
      try {
         //Consultamos tas las operac ion bd local
         String strsqlmaster = "SELECT DISTINCT MPM_ID  FROM vta_mov_prov where  "
            + "  left(MP_FECHA,6)='" + strMesAnio + "' AND MP_ANULADO = 0 and MP_ESPAGO = 1 and EMP_ID = " + intEMP_ID + " AND MPM_ID <> 0" + strFiltroPagosMasivos;
         ResultSet rsM = oConn.runQuery(strsqlmaster);
         while (rsM.next()) {
            //Recuperamos el documento
            int intIdMCM = rsM.getInt("MPM_ID");
            PagosMasivosCtas pagos = new PagosMasivosCtas(oConn, varSesiones, null);
            pagos.getMasivo().setFieldInt("MPM_ID", intIdMCM);
            pagos.Init();
            //Realizamos la sumatoria
            Iterator<MovProveedor> it = pagos.lstPagos.iterator();
            double dblTotalPago = 0;
            double dblTotalImpuesto1 = 0;
            while (it.hasNext()) {
               MovProveedor deta = it.next();
               dblTotalPago += deta.getCta_prov().getFieldDouble("MP_ABONO");
               dblTotalImpuesto1 += deta.getCta_prov().getFieldDouble("MP_IMPUESTO1");
            }
            this.dblTmp1 = 0;
            this.dblTmp2 = 0;
            this.dblTmp3 = 0;
            this.dblTmp4 = 0;
            this.dblTmp5 = 0;
            this.dblTmp6 = 0;
            this.dblTmp7 = 0;
            this.dblTmp8 = 0;
            this.dblTmp9 = 0;
            this.dblTmp10 = 0;
            this.setDblTmp2(dblTotalPago);
            this.setDblTmp3(dblTotalImpuesto1);
            this.pagosMasivosCtas = pagos;
            CalculaPolizaContablePagos(pagos.lstPagos.get(0).getCta_prov().getFieldInt("MP_MONEDA"), pagos.lstPagos.get(0).getCta_prov(), pagos.lstPagos.get(0).getCta_prov().getFieldInt("MP_ID"), "NEW");
         }
         rsM.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage());
      }
   }

   /**
    * Calcula la poliza contable de un pago de una cuenta por pagar
    *
    * @param intMonedaOpera Es la moneda base
    * @param document Es el documento
    * @param intId Es el id de la operacion
    * @param strTipoOper
    */
   public void CalculaPolizaContablePagos(
      int intMonedaOpera, TableMaster document, int intId, String strTipoOper) {
      // <editor-fold defaultstate="collapsed" desc="Declaracion de variables">
      boolean bolComplementarias = false;
      double dblComplementaria = 0;
      double dblComplementariaProv = 0;
      boolean bolComplementarias2 = false;
      boolean bolComplementariasEUROS = false;
      double dblComplementaria2 = 0;
      double dblComplementaria3 = 0;
      String strCtaComp1 = "";
      String strCtaComp2 = "";
      String strCtaComp1Prov = "";
      String strPV_CTA_ANTICIPO = "";
      String strPV_CTAPROV_COMPL_ANTI = "";
      String strPV_CTA_ANTICIPO_Global = "";
      String strPV_CTAPROV_COMPL_ANTI_Global = "";
      String strPV_RFCDIOT = "";
      String strPV_NOMBREDIOT = "";
      String strPV_CONTAPROVUSD = "";
      String strPV_CONTAPROVE = "";
      String strPV_CONTAPROVDC = "";
      String strCuentaOrigen = "";
      String strBancoOrigen = "";
      String strCuentaDestino = "";
      String strBancoDestino = "";
      String strRFCTerceros = "";
      double dblFactorTasa = 0;
      double dblImportePagoDoc = document.getFieldDouble("MP_ABONO");
      //Asignamos importe de pago global
      if (this.dblTmp2 != 0) {
         dblImportePagoDoc = this.dblTmp2;
      }
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Evaluamos si tenemos que realizar conversion de monedas">
      if (intMonedaOpera != this.intMonedaBase) {

         // <editor-fold defaultstate="collapsed" desc="Buscamos la moneda original del movimiento">
         int intMonedaCXP = 0;
         if (document.getFieldInt("CXP_ID") != 0) {
            //Es un pago
            String strSqlCXP = "select CXP_MONEDA from vta_cxpagar WHERE CXP_ID = " + document.getFieldInt("CXP_ID");
            try {
               ResultSet rs = oConn.runQuery(strSqlCXP, true);
               while (rs.next()) {
                  intMonedaCXP = rs.getInt("CXP_MONEDA");
               }
               rs.close();
            } catch (SQLException ex) {
               log.debug("ERROR:" + ex.getMessage());

            }
         } else {
            //Es un anticipo
            String strSqlCXP = "select MON_ID from vta_proveedor WHERE PV_ID = " + document.getFieldInt("PV_ID");
            try {
               ResultSet rs = oConn.runQuery(strSqlCXP, true);
               while (rs.next()) {
                  intMonedaCXP = rs.getInt("MON_ID");
               }
               rs.close();
            } catch (SQLException ex) {
               log.debug("ERROR:" + ex.getMessage());

            }
         }

         // </editor-fold>
         bolComplementarias = true;
         // <editor-fold defaultstate="collapsed" desc="Buscamos la conversion de  USD a la moneda original">
         if ((intMonedaOpera == 2 && intMonedaCXP == 3) || (intMonedaOpera == 2 && intMonedaCXP == 4)) {
            bolComplementariasEUROS = true;

            if (document.getFieldDouble("MP_TASAPESO") != 1 && document.getFieldDouble("MP_TASAPESO") != 0) {
               double dblFactor = document.getFieldDouble("MP_TASAPESO");
               this.dblTmp1 = dblImportePagoDoc / dblFactor;

            } else {
               //Aplica conversion
               this.monedas.setBoolConversionAutomatica(true);
               double dblFactor = this.monedas.GetFactorConversion(document.getFieldString("MP_FECHA"), 4, intMonedaOpera, intMonedaBase);
               this.dblTmp1 = dblImportePagoDoc / dblFactor;
            }

         }
         // </editor-fold>
         // <editor-fold defaultstate="collapsed" desc="CONVERSION DIRECTA A PESOS">
         //Validamos si hay una conversion a pesos
         if (document.getFieldDouble("MP_TASAPESO") != 1 && document.getFieldDouble("MP_TASAPESO") != 0 && bolComplementariasEUROS == false) {
            double dblFactor = document.getFieldDouble("MP_TASAPESO");
            dblComplementaria = dblImportePagoDoc * dblFactor;
            dblComplementaria -= dblImportePagoDoc;
            dblComplementariaProv = dblComplementaria;
//            document.setFieldDouble("MP_ABONO", dblImportePagoDoc * dblFactor);
            document.setFieldDouble("MP_IMPUESTO1", document.getFieldDouble("MP_IMPUESTO1") * dblFactor);
            //Impuesto global
            if (this.dblTmp3 != 0) {
               this.dblTmp3 = this.dblTmp3 * dblFactor;
            }
         } else {
            //Aplica conversion
            this.monedas.setBoolConversionAutomatica(true);
            double dblFactor = this.monedas.GetFactorConversion(document.getFieldString("MP_FECHA"), 4, intMonedaOpera, intMonedaBase);
            dblComplementaria = dblImportePagoDoc * dblFactor;
            dblComplementaria -= dblImportePagoDoc;
            dblComplementariaProv = dblComplementaria;
//            document.setFieldDouble("MP_ABONO", dblImportePagoDoc * dblFactor);
            document.setFieldDouble("MP_IMPUESTO1", document.getFieldDouble("MP_IMPUESTO1") * dblFactor);
            //Impuesto global
            if (this.dblTmp3 != 0) {
               this.dblTmp3 = this.dblTmp3 * dblFactor;
            }
            //Calculamos el complemento en base al monto en la moneda original
            if (bolComplementariasEUROS) {
               dblComplementariaProv = dblImportePagoDoc + dblComplementaria - this.dblTmp1;
            }
         }
         // </editor-fold>

      }
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Obtenemos los datos globales de contabilidad">
      int intCXP_ID = document.getFieldInt("CXP_ID");
      int intTI_ID = 0;
      int intEMP_ID = 0;
      int intCXP_MONEDA = 0;
      String strFolioCXP = "";
      String strNomFolioCXP = "";
      String strProvCXP = "";
      String strProvRazonSocial = ".";
      String strNomProvCXP = "";
      String strEMP_PASSCP = "";
      String strEMP_USERCP = "";
      String strEMP_URLCP = "";
      String strCXP_UUID = "";
      String strProvAnti = "";
      if (intCXP_ID != 0) {
         String strSql = "select EMP_ID,TI_ID,TI_ID2,TI_ID3,CXP_FOLIO,CXP_RAZONSOCIAL,CXP_MONEDA,CXP_UUID"
            + " from vta_cxpagar where CXP_ID = " + intCXP_ID;
         strNomFolioCXP = "CXP_FOLIO";
         strNomProvCXP = "CXP_RAZONSOCIAL";
         try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
               intEMP_ID = rs.getInt("EMP_ID");
               intTI_ID = rs.getInt("TI_ID");
               intCXP_MONEDA = rs.getInt("CXP_MONEDA");
               strFolioCXP = rs.getString(strNomFolioCXP);
               strProvCXP = rs.getString(strNomProvCXP);
               strCXP_UUID = rs.getString("CXP_UUID");
               //Ponemos el UUID en el folio...
               if (strCXP_UUID.length() > 4) {
                  strFolioCXP = strCXP_UUID.substring(strCXP_UUID.length() - 4, strCXP_UUID.length());
               }
            }
            rs.close();
         } catch (SQLException ex) {
            log.debug("ERROR:" + ex.getMessage());

         }
      } else {
         intEMP_ID = document.getFieldInt("EMP_ID");
         //Anticipos y movimientos no vinculados
         String strSql = "select PV_RAZONSOCIAL,TI_ID from vta_proveedor where PV_ID = " + document.getFieldInt("PV_ID");
         try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
               strProvAnti = rs.getString("PV_RAZONSOCIAL");
               intTI_ID = rs.getInt("TI_ID");
            }
            rs.close();
         } catch (SQLException ex) {
            log.debug("ERROR:" + ex.getMessage());
         }
      }
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Evaluamos si estan pagando un documento extranjero con moneda nacional">
      if (!bolComplementarias) {
         if (intCXP_MONEDA != 1) {
            //Activamos el segundo complemento
            //Calculamos el importe en la moneda original
            bolComplementarias2 = true;
            double dblFactor = 0;
            //Validamos si hay una conversion a pesos
            if (document.getFieldDouble("MP_TASAPESO") != 1 && document.getFieldDouble("MP_TASAPESO") != 0) {
               dblFactor = document.getFieldDouble("MP_TASAPESO");
               dblComplementaria3 = dblImportePagoDoc / dblFactor;
               dblComplementaria2 = dblImportePagoDoc - dblComplementaria3;
//               document.setFieldDouble("MP_ABONO", dblImportePagoDoc * dblFactor);
               document.setFieldDouble("MP_IMPUESTO1", document.getFieldDouble("MP_IMPUESTO1") / dblFactor);
               //Solo para el detalle
               dblFactor = 1 / dblFactor;
            } else {
               //Aplica conversion
               this.monedas.setBoolConversionAutomatica(true);
               dblFactor = this.monedas.GetFactorConversion(document.getFieldString("MP_FECHA"), 4, intMonedaOpera, intMonedaBase);
               dblComplementaria3 = dblImportePagoDoc * dblFactor;
               dblComplementaria2 = dblImportePagoDoc - dblComplementaria3;
//               document.setFieldDouble("MP_ABONO", dblImportePagoDoc * dblFactor);
               document.setFieldDouble("MP_IMPUESTO1", document.getFieldDouble("MP_IMPUESTO1") * dblFactor);

            }
            //Guardamos el factor si es masivo
            if (this.pagosMasivosCtas != null) {
               this.pagosMasivosCtas.setDblFactorConv(dblFactor);
            }
         }
      }
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Obtenemos datos de la cuenta de la empresa">
      int intEMP_USECONTA = 0;
      String strCtaVtasIVAGlobal = "";
      String strCtaVtasIVATasa = "";
      String strCtaVtasIVATasaCob = "";
      String strCtaVtasCteGlobal = "";
      String strCtaVtasProv = "";
      String strCtaBcos = "";

      // </editor-fold>
      // <editor-fold defaultstate="collapsed" desc="Consultamos la info de la empresa">
      String strSql = "SELECT EMP_NOAPROB,EMP_FECHAPROB,EMP_NOSERIECERT,EMP_TIPOCOMP,EMP_URLCP,"
         + " EMP_USECONTA,EMP_CTAVTA,EMP_CTAIVA,EMP_CTAPROV,EMP_PASSCP,EMP_USERCP,EMP_CTAPROV_COMPL"
         + " ,EMP_CTA_PROV_ANTICIPO,EMP_CTAPROV_COMPL_ANTI,EMP_CONTAPROV_RET_ISR,EMP_CONTAPROV_RET_IVA"
         + " FROM vta_empresas "
         + " WHERE EMP_ID = " + intEMP_ID + "";
      try {
         ResultSet rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            intEMP_USECONTA = rs.getInt("EMP_USECONTA");
            strCtaVtasIVAGlobal = rs.getString("EMP_CTAIVA");
            strCtaVtasCteGlobal = rs.getString("EMP_CTAPROV");
            strCtaComp1 = rs.getString("EMP_CTAPROV_COMPL");
            strEMP_PASSCP = rs.getString("EMP_PASSCP");
            strEMP_USERCP = rs.getString("EMP_USERCP");
            strEMP_URLCP = rs.getString("EMP_URLCP");
            strPV_CTA_ANTICIPO_Global = rs.getString("EMP_CTA_PROV_ANTICIPO");
            strPV_CTAPROV_COMPL_ANTI_Global = rs.getString("EMP_CTAPROV_COMPL_ANTI");
//            strPV_CONTA_RET_ISR_Global = rs.getString("EMP_CONTAPROV_RET_ISR");
//            strPV_CONTA_RET_IVA_Global = rs.getString("EMP_CONTAPROV_RET_IVA");
         }
         rs.close();
      } catch (SQLException ex) {
         log.debug("ERROR:" + ex.getMessage());

      }
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Solo aplica si esta configurada la contabilidad">
      if (intEMP_USECONTA == 1) {
         // <editor-fold defaultstate="collapsed" desc="Buscamos cuenta para bancos">
         int intIdMovBco = 0;
         boolean bolUsaCheque = false;
         String strNumCheque = "";
         int intIdBanco = 0;
         strSql = "SELECT BC_ID,MCB_NOCHEQUE,MCB_ID "
            + " FROM vta_mov_cta_bcos "
            + " WHERE MP_ID = " + intId + "";
         try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
               intIdBanco = rs.getInt("BC_ID");
               intIdMovBco = rs.getInt("MCB_ID");
               if (!rs.getString("MCB_NOCHEQUE").isEmpty()) {
                  bolUsaCheque = true;
                  strNumCheque = rs.getString("MCB_NOCHEQUE");
               }
            }
            rs.close();
         } catch (SQLException ex) {
            log.debug("ERROR:" + ex.getMessage());

         }
         //Buscamos el link del pago ya que es masivo
         if (intIdBanco == 0) {
            strSql = "select a.MCB_ID,a.BC_ID,a.MCB_NOCHEQUE from vta_mov_cta_bcos a, vta_mov_cta_bcos_rela b\n"
               + "where a.MCB_ID = b.MCB_ID and b.MP_ID = \n" + intId;
            try {
               ResultSet rs = oConn.runQuery(strSql, true);
               while (rs.next()) {
                  intIdBanco = rs.getInt("BC_ID");
                  intIdMovBco = rs.getInt("MCB_ID");
                  if (!rs.getString("MCB_NOCHEQUE").isEmpty()) {
                     bolUsaCheque = true;
                     strNumCheque = rs.getString("MCB_NOCHEQUE");
                  }
               }
               rs.close();
            } catch (SQLException ex) {
               log.debug("ERROR:" + ex.getMessage());

            }
         }
         strSql = "SELECT BC_CTA_CONT,BC_CTA_CONT_COMPL,BC_CTA_BANC,RBK_CVE,BC_BANCO "
            + " FROM vta_bcos "
            + " WHERE BC_ID = " + intIdBanco + "";
         try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
               strCtaBcos = rs.getString("BC_CTA_CONT");
               strCtaComp2 = rs.getString("BC_CTA_CONT_COMPL");
               strCuentaOrigen = rs.getString("BC_CTA_BANC");
               strBancoOrigen = rs.getString("BC_BANCO");
            }
            rs.close();
         } catch (SQLException ex) {
            log.debug("ERROR:" + ex.getMessage());

         }
         // </editor-fold>
         // <editor-fold defaultstate="collapsed" desc="Buscamos cuenta para proveedores">
         strSql = "SELECT PV_CONTAPROV,PV_RAZONSOCIAL,PV_RFC,PV_CONTAPROV_COMPL,"
            + " PV_CTA_ANTICIPO,PV_CTAPROV_COMPL_ANTI,PV_CONTA_RET_ISR,PV_CONTA_RET_IVA "
            + " ,PV_CONTAPROVUSD,PV_CONTAPROVE,PV_CONTAPROVDC"
            + " FROM vta_proveedor "
            + " where PV_ID=" + document.getFieldInt("PV_ID") + "";
         try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
               strCtaVtasProv = rs.getString("PV_CONTAPROV");
               strCtaComp1Prov = rs.getString("PV_CONTAPROV_COMPL");
               strProvRazonSocial = rs.getString("PV_RAZONSOCIAL");
               strPV_CTA_ANTICIPO = rs.getString("PV_CTA_ANTICIPO");
               strPV_CTAPROV_COMPL_ANTI = rs.getString("PV_CTAPROV_COMPL_ANTI");
//               strPV_CONTA_RET_ISR = rs.getString("PV_CONTA_RET_ISR");
//               strPV_CONTA_RET_IVA = rs.getString("PV_CONTA_RET_IVA");
               strPV_RFCDIOT = rs.getString("PV_RFC");
               strPV_NOMBREDIOT = rs.getString("PV_RAZONSOCIAL");
               strPV_CONTAPROVUSD = rs.getString("PV_CONTAPROVUSD");
               strPV_CONTAPROVE = rs.getString("PV_CONTAPROVE");
               strPV_CONTAPROVDC = rs.getString("PV_CONTAPROVDC");
            }
            rs.close();
            //Buscamos el banco destino y cuenta destino del pago
            strSql = "select MPD_BANCO_DEST,MPD_CUENTA_DEST,MPD_RFC_TERCEROS from vta_mov_prov_deta where MP_ID = " + intId;
            rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
               strCuentaDestino = rs.getString("MPD_CUENTA_DEST");
               strBancoDestino = rs.getString("MPD_BANCO_DEST");
               strRFCTerceros = rs.getString("MPD_RFC_TERCEROS");
            }
            rs.close();
         } catch (SQLException ex) {
            log.debug("ERROR:" + ex.getMessage());

         }

         // </editor-fold>
         // <editor-fold defaultstate="collapsed" desc="Buscamos cuentas para los ivas">
         if (document.getFieldDouble("MP_IMPUESTO1") > 0) {
            //Si el iva es diferente al default  y hay impuesto seleccionado la tasa 1(IVA )
            if (intTI_ID != 1) {
               intTI_ID = 1;

            }
         }

         strSql = "SELECT TI_CTA_CON_ACRE,TI_CTA_CON_ACRE_COB,TI_TASA FROM vta_tasaiva "
            + " where TI_ID=" + intTI_ID + "";
         try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
               strCtaVtasIVATasa = rs.getString("TI_CTA_CON_ACRE");
               strCtaVtasIVATasaCob = rs.getString("TI_CTA_CON_ACRE_COB");
               dblFactorTasa = rs.getDouble("TI_TASA");
               document.setFieldDouble("MP_TASAIMPUESTO1", dblFactorTasa);
            }
            rs.close();
         } catch (SQLException ex) {
            log.debug("ERROR:" + ex.getMessage());

         }
         // </editor-fold>
         // <editor-fold defaultstate="collapsed" desc="Actualizamos la poliza contable">
         PolizasContables poli = new PolizasContables(this.oConn, this.varSesiones, null);
         poli.setStrOper(strTipoOper);
         poli.setStrUserCte(strEMP_USERCP);
         poli.setStrPassCte(strEMP_PASSCP);
         poli.setStrURLServicio(strEMP_URLCP);

         ArrayList<PoliCtas> lstCuentasAG = new ArrayList<PoliCtas>();
         poli.setLstCuentasAG(lstCuentasAG);
         // <editor-fold defaultstate="collapsed" desc="Pagos masivos">
         if (this.pagosMasivosCtas != null) {
            poli.setPagosMasivosCtas(pagosMasivosCtas);
            //Buscamos el id del pago masivo
            strSql = "SELECT MPM_EXEC_INTER_CP FROM vta_mov_prov_mas "
               + " where MPM_ID=" + pagosMasivosCtas.getMasivo().getFieldInt("MPM_ID") + "";
            try {
               ResultSet rs = oConn.runQuery(strSql, true);
               while (rs.next()) {
                  int intPoliMasivo = rs.getInt("MPM_EXEC_INTER_CP");
                  poli.setIntIdPoliMasivo(intPoliMasivo);
               }
               rs.close();
            } catch (SQLException ex) {
               log.debug("ERROR:" + ex.getMessage());

            }
         }
         // </editor-fold>

         //Evaluamos el tipo de pago
         // <editor-fold defaultstate="collapsed" desc="Caso 1 pago normal">
         if (document.getFieldInt("MP_ANTICIPO") == 0
            && document.getFieldInt("MP_USA_ANTICIPO") == 0) {
            //Validamos las cuentas a usar
            //Bancos
            poli.getLstCuentas().add(strCtaBcos);
            //Proveedor
            if (strCtaVtasProv.isEmpty()) {
               poli.getLstCuentas().add(strCtaVtasCteGlobal);
            } else // <editor-fold defaultstate="collapsed" desc="Validacion de la cuenta de proveedor de acuerdo a la moneda">
            //Pesos
            {
               if (intMonedaOpera == 1) {
                  poli.getLstCuentas().add(strCtaVtasProv);
               } else //USD
               {
                  if (intMonedaOpera == 2) {
                     if (!strPV_CONTAPROVUSD.isEmpty()) {
                        poli.getLstCuentas().add(strPV_CONTAPROVUSD);
                     } else {
                        poli.getLstCuentas().add(strCtaVtasProv);
                     }
                  } else if (intMonedaOpera == 3) {
                     if (!strPV_CONTAPROVE.isEmpty()) {
                        poli.getLstCuentas().add(strPV_CONTAPROVE);
                     } else {
                        poli.getLstCuentas().add(strCtaVtasProv);
                     }
                  } else if (intMonedaOpera == 4) {
                     if (!strPV_CONTAPROVDC.isEmpty()) {
                        poli.getLstCuentas().add(strPV_CONTAPROVDC);
                     } else {
                        poli.getLstCuentas().add(strCtaVtasProv);
                     }
                  } else {
                     poli.getLstCuentas().add(strCtaVtasProv);
                  } // </editor-fold>
               }            //IVA
            }
            if (strCtaVtasIVATasa.isEmpty()) {
               poli.getLstCuentas().add(strCtaVtasIVAGlobal);
            } else {
               poli.getLstCuentas().add(strCtaVtasIVATasa);
            }
            //IVA COBRADO
            poli.getLstCuentas().add(strCtaVtasIVATasaCob);
            // <editor-fold defaultstate="collapsed" desc="Complementarios">
            if (bolComplementarias) {
               //anadimos mas cuentas
               //Complemento proveedores
               PoliCtas pol = new PoliCtas();
               if (strCtaComp1Prov.isEmpty()) {
                  pol.setStrCuenta(strCtaComp1);
               } else {
                  pol.setStrCuenta(strCtaComp1Prov);
               }
               pol.setBolEsCargo(true);
               pol.setDblImporte(dblComplementariaProv);
               pol.setStrFolioRef(strFolioCXP);
               lstCuentasAG.add(pol);

               //Complemento bancos
               pol = new PoliCtas();
               pol.setStrCuenta(strCtaComp2);
               pol.setBolEsCargo(false);
               pol.setDblImporte(dblComplementaria);
               pol.setStrFolioRef(strFolioCXP);
               lstCuentasAG.add(pol);

            }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Complementarias de pago nacional a proveedor extranjero">
            if (bolComplementarias2) {
               //anadimos mas cuentas
               //Complemento proveedores
               PoliCtas pol = new PoliCtas();
               if (strCtaComp1Prov.isEmpty()) {
                  pol.setStrCuenta(strCtaComp1);
               } else {
                  pol.setStrCuenta(strCtaComp1Prov);
               }
               pol.setBolEsCargo(true);
               pol.setDblImporte(dblComplementaria2);
               pol.setStrFolioRef(strFolioCXP);
               lstCuentasAG.add(pol);
               log.debug("dblComplementaria2:" + dblComplementaria2);

               poli.setBolUsaComplementoAjuste(true);
               poli.setDblComplementoAjuste(dblComplementaria3);
               log.debug("dblComplementaria2:" + dblComplementaria3);
            }
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Añadimos comisiones generadas">
            log.debug("Verificamos si hay una comision....");
            this.getPoliDetaComi(document, poli, true, strFolioCXP, intMonedaOpera, 0, intIdMovBco, intTI_ID, dblFactorTasa);
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Calculo del IVA de la comision">
            if (intTI_ID > 0) {
               if (intTI_ID != 3 && intTI_ID != 4) {
                  if (dblTmp4 > 0) {
                     //del monto que estan pagando entre 1.16 y luego * .16
                     double dblImpuesto1 = dblTmp4 / (1 + (dblFactorTasa / 100));
                     dblImpuesto1 = dblImpuesto1 * (dblFactorTasa / 100);
                     //Obtenemos la lista de movimientos manuales
                     lstCuentasAG = poli.getLstCuentasAG();
                     //Objeto polizas deta
                     PoliCtas pol = new PoliCtas();
                     pol.setStrCuenta(strCtaVtasIVATasaCob);
                     pol.setBolEsCargo(true);
                     pol.setDblImporte(dblImpuesto1);
                     pol.setStrFolioRef(strProvAnti);
                     lstCuentasAG.add(pol);
                  }
               }
            }
            // </editor-fold>
         }
         // </editor-fold>

         // <editor-fold defaultstate="collapsed" desc="Caso 2 anticipo">
         if (document.getFieldInt("MP_ANTICIPO") == 1) {
            poli.setBolEsAnticipo(true);
            strFolioCXP = document.getFieldString("MP_FOLIO");
            //Validamos las cuentas a usar
            //Bancos
            poli.getLstCuentas().add(strCtaBcos);
            //Cte
            if (strPV_CTA_ANTICIPO.isEmpty()) {
               poli.getLstCuentas().add(strPV_CTA_ANTICIPO_Global);
            } else {
               poli.getLstCuentas().add(strPV_CTA_ANTICIPO);
            }
            //Complementarios
            if (bolComplementarias) {
               //anadimos mas cuentas
               log.debug("strPV_CTAPROV_COMPL_ANTI_Global: " + strPV_CTAPROV_COMPL_ANTI_Global);
               log.debug("strPV_CTAPROV_COMPL_ANTI: " + strPV_CTAPROV_COMPL_ANTI);
               log.debug("strCtaComp2: " + strCtaComp2);
               //Complemento proveedores
               PoliCtas pol = new PoliCtas();
               if (strPV_CTAPROV_COMPL_ANTI.isEmpty()) {
                  pol.setStrCuenta(strPV_CTAPROV_COMPL_ANTI_Global);
               } else {
                  pol.setStrCuenta(strPV_CTAPROV_COMPL_ANTI);
               }
               pol.setBolEsCargo(true);
               log.debug("dblComplementariaProv:" + pol.getStrCuenta() + " " + dblComplementariaProv + " " + strFolioCXP);
               pol.setDblImporte(dblComplementariaProv);
               pol.setStrFolioRef(strFolioCXP);
               lstCuentasAG.add(pol);

               //Complemento bancos
               pol = new PoliCtas();
               pol.setStrCuenta(strCtaComp2);
               pol.setBolEsCargo(false);
               pol.setDblImporte(dblComplementaria);
               log.debug("dblComplementaria:" + pol.getStrCuenta() + " " + dblComplementaria + " " + strFolioCXP);
               pol.setStrFolioRef(strFolioCXP);
               lstCuentasAG.add(pol);

            }
            // <editor-fold defaultstate="collapsed" desc="Añadimos comisiones generadas">
            log.debug("Verificamos si hay un anticipo....");
            this.getPoliDetaComi(document, poli, true, strFolioCXP, intMonedaOpera, 0, intIdMovBco, intTI_ID, dblFactorTasa);
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Calculo del IVA de la comision">
            if (intTI_ID > 0) {
               if (intTI_ID != 3 && intTI_ID != 4) {
                  if (dblTmp4 > 0) {
                     //del monto que estan pagando entre 1.16 y luego * .16
                     double dblImpuesto1 = dblTmp4 / (1 + (dblFactorTasa / 100));
                     dblImpuesto1 = dblImpuesto1 * (dblFactorTasa / 100);
                     //Obtenemos la lista de movimientos manuales
                     lstCuentasAG = poli.getLstCuentasAG();
                     //Objeto polizas deta
                     PoliCtas pol = new PoliCtas();
                     pol.setStrCuenta(strCtaVtasIVATasaCob);
                     pol.setBolEsCargo(true);
                     pol.setDblImporte(dblImpuesto1);
                     pol.setStrFolioRef(strProvAnti);
                     lstCuentasAG.add(pol);
                  }
               }
            }
            // </editor-fold>

         }
         // </editor-fold>

         // <editor-fold defaultstate="collapsed" desc="Caso 3 uso de anticipo">
         if (document.getFieldInt("MP_USA_ANTICIPO") == 1) {
            poli.setBolUsaAnticipo(true);
            if (strFolioCXP.isEmpty()) {
               strFolioCXP = document.getFieldString("MP_FOLIO");
            }
            //Validamos las cuentas a usar
            //Anticipo
            if (strPV_CTA_ANTICIPO.isEmpty()) {
               poli.getLstCuentas().add(strPV_CTA_ANTICIPO_Global);
            } else {
               poli.getLstCuentas().add(strPV_CTA_ANTICIPO);
            }
            //Proveedor
            if (strCtaVtasProv.isEmpty()) {
               poli.getLstCuentas().add(strCtaVtasCteGlobal);
            } else // <editor-fold defaultstate="collapsed" desc="Validacion de la cuenta de proveedor de acuerdo a la moneda">
            //Pesos
            {
               if (intMonedaOpera == 1) {
                  poli.getLstCuentas().add(strCtaVtasProv);
               } else //USD
               {
                  if (intMonedaOpera == 2) {
                     if (!strPV_CONTAPROVUSD.isEmpty()) {
                        poli.getLstCuentas().add(strPV_CONTAPROVUSD);
                     } else {
                        poli.getLstCuentas().add(strCtaVtasProv);
                     }
                  } else if (intMonedaOpera == 3) {
                     if (!strPV_CONTAPROVE.isEmpty()) {
                        poli.getLstCuentas().add(strPV_CONTAPROVE);
                     } else {
                        poli.getLstCuentas().add(strCtaVtasProv);
                     }
                     log.debug("strPV_CONTAPROVE:" + strPV_CONTAPROVE);
                     log.debug("strCtaVtasProv:" + strCtaVtasProv);
                  } else if (intMonedaOpera == 4) {
                     if (!strPV_CONTAPROVDC.isEmpty()) {
                        poli.getLstCuentas().add(strPV_CONTAPROVDC);
                     } else {
                        poli.getLstCuentas().add(strCtaVtasProv);
                     }
                  } else {
                     poli.getLstCuentas().add(strCtaVtasProv);
                  } // </editor-fold>
               }            //IVA
            }
            if (strCtaVtasIVATasa.isEmpty()) {
               poli.getLstCuentas().add(strCtaVtasIVAGlobal);
            } else {
               poli.getLstCuentas().add(strCtaVtasIVATasa);
            }
            //IVA COBRADO
            poli.getLstCuentas().add(strCtaVtasIVATasaCob);

            // <editor-fold defaultstate="collapsed" desc="Complementarios">
            if (bolComplementarias) {
               //anadimos mas cuentas
               //Complemento proveedores
               PoliCtas pol = new PoliCtas();
               if (strCtaComp1Prov.isEmpty()) {
                  pol.setStrCuenta(strCtaComp1);
               } else {
                  pol.setStrCuenta(strCtaComp1Prov);
               }
               pol.setBolEsCargo(true);
               pol.setDblImporte(dblComplementariaProv);
               pol.setStrFolioRef(strFolioCXP);
               lstCuentasAG.add(pol);

               //Complemento ANTICIPOS
               pol = new PoliCtas();
               pol.setStrCuenta(strPV_CTAPROV_COMPL_ANTI);
               pol.setBolEsCargo(false);
               pol.setDblImporte(dblComplementaria);
               pol.setStrFolioRef(strFolioCXP);
               lstCuentasAG.add(pol);

            }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Complementarias de pago nacional a proveedor extranjero">
            if (bolComplementarias2) {
               //anadimos mas cuentas
               //Complemento proveedores
               PoliCtas pol = new PoliCtas();
               if (strCtaComp1Prov.isEmpty()) {
                  pol.setStrCuenta(strCtaComp1);
               } else {
                  pol.setStrCuenta(strCtaComp1Prov);
               }
               pol.setBolEsCargo(true);
               pol.setDblImporte(dblComplementaria2);
               pol.setStrFolioRef(strFolioCXP);
               lstCuentasAG.add(pol);
               log.debug("dblComplementaria2:" + dblComplementaria2);
               poli.setBolUsaComplementoAjuste(true);
               poli.setDblComplementoAjuste(dblComplementaria3);
               log.debug("dblComplementaria2:" + dblComplementaria3);
            }
            // </editor-fold>
         }
         // </editor-fold>

         //Asignamos documento
         poli.setDocumentMaster(document);
         poli.setStrURLServicio(strEMP_URLCP);

         //Anexamos los archivos 
         if (document.getFieldInt("CXP_ID") != 0) {
            CargarArchivos(poli, document.getFieldInt("CXP_ID"), null);
         }

         // <editor-fold defaultstate="collapsed" desc="Guardamos la poliza">
         try {
            log.debug("bolUsaCheque: " + bolUsaCheque + " " + strNumCheque + " dblTmp2:" + dblTmp2 + " dblTmp3:" + dblTmp3);
            //Enviamos importe global para pagos...
            if (this.dblTmp1 != 0) {
               poli.setDblTmp1(dblTmp1);
            }
            if (this.dblTmp2 != 0) {
               poli.setDblTmp2(dblTmp2);
               poli.setDblTmp3(dblTmp3);
            }
            //Enviamos importe de comisiones
            log.debug("Importe comision dblTmp4: " + dblTmp4);
            if (this.dblTmp4 != 0) {
               poli.setDblTmp4(dblTmp4);
            }
            poli.setBolUsaCheque(bolUsaCheque);
            poli.setStrNumCheque(strNumCheque);
            poli.setStrRfcDIOT(strPV_RFCDIOT);
            poli.setStrPV_NOMBREDIOT(strPV_NOMBREDIOT);
            poli.setStrCXP_UUID(strCXP_UUID);
            poli.setStrBancoOrigen((strBancoOrigen == null ? "" : strBancoOrigen));
            poli.setStrCuentaOrigen((strCuentaOrigen == null ? "" : strCuentaOrigen));
            poli.setStrBancoDestino((strBancoDestino == null ? "" : strBancoDestino));
            poli.setStrRfcTerceros((strRFCTerceros == null ? "" : strRFCTerceros));
            poli.setStrCuentaDestino((strCuentaDestino == null ? "" : strCuentaDestino));
            poli.callRemote(document.getFieldInt("MP_ID"), PolizasContables.RECIBOS_PROV, strProvRazonSocial, strFolioCXP);
            log.debug("Resp WS:" + poli.strResultLast);
            if (poli.strResultLast.startsWith("OK")) {
               this.intSucessPagos++;
               if (pagosMasivosCtas == null) {
                  //Marcamos la venta como procesada
                  this.oConn.runQueryLMD("UPDATE vta_mov_prov "
                     + " SET MP_EXEC_INTER_CP =  " + poli.strResultLast.replace("OK.", "")
                     + " WHERE MP_ID = " + intId);

               } else {
                  //Marcamos el pago como procesada
                  this.oConn.runQueryLMD("UPDATE vta_mov_prov_mas "
                     + " SET MPM_EXEC_INTER_CP =  " + poli.strResultLast.replace("OK.", "")
                     + " WHERE MPM_ID = " + pagosMasivosCtas.getMasivo().getFieldInt("MPM_ID"));
               }

            } else {
               this.intFailPagos++;
               this.lstFailsPagos.add("Error al generar la poliza para el pago con folio " + strFolioCXP + " " + poli.strResultLast);
               //Marcamos movimiento como NO procesado
               if (pagosMasivosCtas == null) {
                  //Marcamos la venta como procesada
                  this.oConn.runQueryLMD("UPDATE vta_mov_prov "
                     + " SET MP_EXEC_INTER_CP =  0 "
                     + " WHERE MP_ID = " + intId);

               } else {
                  //Marcamos el pago como procesada
                  this.oConn.runQueryLMD("UPDATE vta_mov_prov_mas "
                     + " SET MPM_EXEC_INTER_CP = 0 "
                     + " WHERE MPM_ID = " + pagosMasivosCtas.getMasivo().getFieldInt("MPM_ID"));
               }
            }
         } catch (Exception ex) {
            this.intFailPagos++;
            this.lstFailsPagos.add("Error al generar la poliza para el pago con folio " + strFolioCXP + " " + poli.strResultLast);
            log.error("Error in call webservice?" + ex.getMessage());
            //Marcamos movimiento como NO procesado
            if (pagosMasivosCtas == null) {
               //Marcamos la venta como procesada
               this.oConn.runQueryLMD("UPDATE vta_mov_prov "
                  + " SET MP_EXEC_INTER_CP =  0 "
                  + " WHERE MP_ID = " + intId);

            } else {
               //Marcamos el pago como procesada
               this.oConn.runQueryLMD("UPDATE vta_mov_prov_mas "
                  + " SET MPM_EXEC_INTER_CP = 0 "
                  + " WHERE MPM_ID = " + pagosMasivosCtas.getMasivo().getFieldInt("MPM_ID"));
            }
         }
         // </editor-fold>
         // </editor-fold>
      }
      // </editor-fold>   

   }

   /**
    * Calcula la poliza contable para movimientos de bancos
    *
    * @param intEMP_ID Es el id de la empresa
    * @param strMesAnio Es periodo donde se obtendran los documentos(AAAAMM)
    * @param strEMP_PASSCP Es el password para la contabilidad
    * @param strEMP_USERCP Es el usuario para la contabilidad
    * @param strEMP_URLCP Es la url para la contabilidad
    */
   public void GeneraContaBco(int intEMP_ID, String strMesAnio, String strEMP_PASSCP, String strEMP_USERCP, String strEMP_URLCP) {
      //Consultamos movimientos de bancos no anulados
      try {
         //Consultamos tas las operac ion bd local
         String strsqlmaster = "SELECT vta_mov_cta_bcos.MCB_ID,vta_mov_cta_bcos.BC_ID,vta_mov_cta_bcos.MCB_MONEDA  "
            + " FROM vta_bcos,vta_mov_cta_bcos where "
            + " vta_bcos.BC_ID = vta_mov_cta_bcos.BC_ID  "
            + " and  left(MCB_FECHA,6)='" + strMesAnio + "' AND MCB_ANULADO = 0 and vta_bcos.EMP_ID = " + intEMP_ID + " "
            + " AND FAC_ID = 0 AND TKT_ID = 0 AND (MC_ID = 0 OR (MCB_DEV_CLIE = 1 AND MC_ID <> 0) ) "
            + " AND (MP_ID = 0 OR (MCB_DEV_PROV = 1 AND MP_ID <> 0 ) ) AND MCB_TRAS_ORIGEN = 0  "
            + " " + strFiltroBancos;
         ResultSet rsM = oConn.runQuery(strsqlmaster);
         while (rsM.next()) {
            int intIdBanco = rsM.getInt("BC_ID");
//            int intIdGasto = rsM.getInt("GT_ID");
            int intId = rsM.getInt("MCB_ID");
            TableMaster document = new vta_mov_cta_bcos();
            document.ObtenDatos(intId, oConn);
            log.debug("Generando contabilidad movimiento con id: " + intId);
            CalculaPolizaContableBco(intEMP_ID, strEMP_PASSCP, strEMP_USERCP, strEMP_URLCP, document.getFieldInt("MCB_MONEDA"), document, intId, intIdBanco, 0, "NEW");
         }
         rsM.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage());
      }
   }

   /**
    * Calcula la poliza contable para movimientos de bancos
    *
    * @param intEMP_ID Es el id de la empresa
    * @param strEMP_PASSCP Es el password para la contabilidad
    * @param strEMP_USERCP Es el usuario para la contabilidad
    * @param strEMP_URLCP Es la url para la contabilidad
    * @param intMonedaOpera Es la moneda base
    * @param document Es el documento
    * @param intId Es el id de la operacion
    * @param intIdBanco Es el id del banco
    * @param intIdGasto Es el id del gasto
    * @param strTipoOper Indica con NEW nueva poliza y con CANCEL poliza de
    * cancelacion
    */
   public void CalculaPolizaContableBco(int intEMP_ID, String strEMP_PASSCP, String strEMP_USERCP, String strEMP_URLCP,
      int intMonedaOpera, TableMaster document, int intId, int intIdBanco, int intIdGasto, String strTipoOper) {
      // <editor-fold defaultstate="collapsed" desc="Declaramos variables">
      String strCtaBcos = "";
      String strComplementario = "";
      String strCuentaOrigen = "";
      String strBancoOrigen = "";
      String strCtaBcosDestino = "";
      String strComplementarioDestino = "";
      String strDepositoXIdentificar = "";
      boolean bolDepositoXIdentificar = false;
      String strCuentaDestino = document.getFieldString("MCB_CTA_DESTINO");
      String strBancoDestino = document.getFieldString("RBK_CVE");
      dblTmp1 = 0;
      double dblComplementaria = 0;
      boolean bolComplementarias = false;
      int intMonedaBcoDestino = 0;
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Consultamos cuenta de deposito por identificar">
      if (document.getFieldInt("MCB_TIPO1") != 0) {
         bolDepositoXIdentificar = true;
         String strSql = "SELECT EMP_CUENTA_DEP_IDENTIFICAR "
            + " FROM vta_empresas "
            + " WHERE EMP_ID = " + document.getFieldInt("EMP_ID") + "";
         try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
               strDepositoXIdentificar = rs.getString("EMP_CUENTA_DEP_IDENTIFICAR");
            }
            rs.close();
         } catch (SQLException ex) {
            log.debug("ERROR:" + ex.getMessage());

         }
      }
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Consultamos informacion de bancos">
      String strSql = "SELECT BC_CTA_CONT,BC_CTA_CONT_COMPL,BC_CTA_BANC,BC_BANCO "
         + " FROM vta_bcos "
         + " WHERE BC_ID = " + intIdBanco + "";
      try {
         ResultSet rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            strCtaBcos = rs.getString("BC_CTA_CONT");
            strComplementario = rs.getString("BC_CTA_CONT_COMPL");
            strCuentaOrigen = rs.getString("BC_CTA_BANC");
            strBancoOrigen = rs.getString("BC_BANCO");
         }
         rs.close();
      } catch (SQLException ex) {
         log.debug("ERROR:" + ex.getMessage());

      }
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Consultamos informacion de banco destino en caso de traspaso">
      if (document.getFieldInt("MCB_TRASPASO") == 1) {
         strSql = "SELECT BC_CTA_CONT,BC_CTA_CONT_COMPL,BC_CTA_BANC,BC_BANCO,BC_MONEDA "
            + " FROM vta_bcos "
            + " WHERE BC_ID = " + document.getFieldInt("BC_ID2") + "";
         try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
               strCtaBcosDestino = rs.getString("BC_CTA_CONT");
               strComplementarioDestino = rs.getString("BC_CTA_CONT_COMPL");
               strCuentaDestino = rs.getString("BC_CTA_BANC");
               strBancoDestino = rs.getString("BC_BANCO");
               intMonedaBcoDestino = rs.getInt("BC_MONEDA");
            }
            rs.close();
         } catch (SQLException ex) {
            log.debug("ERROR:" + ex.getMessage());

         }
      }

      // </editor-fold>
      // <editor-fold defaultstate="collapsed" desc="Declaramos variablesEvaluamos si tenemos que realizar conversion de monedas">
      double dblFactor = 1;
      if (intMonedaOpera != this.intMonedaBase) {
         bolComplementarias = true;
         double dblImportePagoDoc = 0;
         if (document.getFieldDouble("MCB_RETIRO") > 0) {
            dblImportePagoDoc = document.getFieldDouble("MCB_RETIRO");
         } else {
            dblImportePagoDoc = document.getFieldDouble("MCB_DEPOSITO");
         }
         //Calculamos el importe del complemento
         if (document.getFieldDouble("MCB_PARIDAD") != 1 && document.getFieldDouble("MCB_PARIDAD") != 0) {
            dblFactor = document.getFieldDouble("MCB_PARIDAD");
            dblComplementaria = dblImportePagoDoc * dblFactor;
            dblComplementaria -= dblImportePagoDoc;
         } else {
            //Aplica conversion
            this.monedas.setBoolConversionAutomatica(true);
            dblFactor = this.monedas.GetFactorConversion(document.getFieldString("MCB_FECHA"), 4, intMonedaOpera, intMonedaBase);
            dblComplementaria = dblImportePagoDoc * dblFactor;
            dblComplementaria -= dblImportePagoDoc;
         }
      }
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Generamos objeto de poliza">
      PolizasContables poli = new PolizasContables(this.oConn, this.varSesiones, null);
      poli.setStrOper(strTipoOper);
      poli.setStrUserCte(strEMP_USERCP);
      poli.setStrPassCte(strEMP_PASSCP);
      poli.setStrURLServicio(strEMP_URLCP);
      if (!document.getFieldString("MCB_NOCHEQUE").isEmpty()) {
         poli.setStrNumCheque(document.getFieldString("MCB_NOCHEQUE"));
         poli.setBolUsaCheque(true);
      }
      //Validamos las cuentas a usar
      //Bancos
      poli.getLstCuentas().add(strCtaBcos);
      //Gastos
//      poli.getLstCuentas().add(strCtaGastos);
      //Ingresos
      //Asignamos documento
      poli.setDocumentMaster(document);
      poli.setBolVTA_DETA(true);
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Barremos gastos para obtener cuentas agrupadas">
      boolean bolCargo = false;
      if (document.getFieldDouble("MCB_RETIRO") > 0) {
         bolCargo = true;
      }
      this.getPoliDetaBancosGasto(document, poli, bolCargo, "", intMonedaOpera, dblFactor);
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Si es un traspaso ">
      if (document.getFieldInt("MCB_TRASPASO") == 1) {
         //Generamos la cuenta por la diferencia en el traspaso
         if (poli.lstCuentasAG != null) {
            if (poli.lstCuentasAG.isEmpty()) {
               double dblComplementariaTmp;
               if (bolCargo) {
                  dblComplementariaTmp = dblTmp1;
               } else {
                  dblComplementariaTmp = dblTmp1;
               }
               //Complemento bancos
               PoliCtas pol = new PoliCtas();
               pol.setStrCuenta(strCtaBcosDestino);
               if (bolCargo) {
                  pol.setBolEsCargo(true);
               } else {
                  pol.setBolEsCargo(false);
               }
               pol.setDblImporte(dblComplementariaTmp);
               pol.setStrFolioRef("");
               pol.setStrNotas(document.getFieldString("MCB_CONCEPTO"));
               poli.lstCuentasAG.add(pol);

            }
         }
      }
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Si aplica la cuenta complementaria del banco procedemos">
      if (bolComplementarias) {
         //Complemento bancos
         PoliCtas pol = new PoliCtas();
         pol.setStrCuenta(strComplementario);
         if (bolCargo) {
            pol.setBolEsCargo(false);
         } else {
            pol.setBolEsCargo(true);
         }
         pol.setDblImporte(dblComplementaria);
         pol.setStrFolioRef("");
         pol.setStrNotas(document.getFieldString("MCB_CONCEPTO"));
         poli.lstCuentasAG.add(pol);
         if (document.getFieldInt("MCB_TRASPASO") == 1 && (intMonedaBcoDestino != this.intMonedaBase)) {
            //Complemento bancos
            pol = new PoliCtas();
            pol.setStrCuenta(strComplementarioDestino);
            if (bolCargo) {
               pol.setBolEsCargo(true);
            } else {
               pol.setBolEsCargo(false);
            }
            pol.setDblImporte(dblComplementaria);
            pol.setStrFolioRef("");
            pol.setStrNotas(document.getFieldString("MCB_CONCEPTO"));
            poli.lstCuentasAG.add(pol);
         }
      }
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Generamos movimiento por identificar">
      if (bolDepositoXIdentificar) {
         ArrayList<PoliCtas> lstCuentasAG = poli.getLstCuentasAG();
         if (lstCuentasAG == null) {
            lstCuentasAG = new ArrayList<PoliCtas>();
         }
         //Calculamos el importe
         double dblImporte = document.getFieldDouble("MCB_DEPOSITO");
         if (document.getFieldDouble("MCB_RETIRO") > 0) {
            dblImporte = document.getFieldDouble("MCB_RETIRO");
         }
         PoliCtas pol = new PoliCtas();
         pol.setStrCuenta(strDepositoXIdentificar);
         pol.setBolEsCargo(bolCargo);
         pol.setDblImporte(dblImporte);
         pol.setStrFolioRef("");
         pol.setStrNotas(document.getFieldString("MCB_CONCEPTO"));
         lstCuentasAG.add(pol);
         // <editor-fold defaultstate="collapsed" desc="Buscamos cuentas para los ivas">
         String strCtaVtasIVATasa = "";
         String strCtaVtasIVATasaCob = "";
         double dblFactorTasa = 0;
         strSql = "SELECT TI_CTA_CONT,TI_CTA_CONT_COB,TI_TASA FROM vta_tasaiva "
            + " where TI_ID= 1";
         try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
               strCtaVtasIVATasa = rs.getString("TI_CTA_CONT");
               strCtaVtasIVATasaCob = rs.getString("TI_CTA_CONT_COB");
               dblFactorTasa = rs.getDouble("TI_TASA");
            }
            rs.close();
         } catch (SQLException ex) {
            log.debug("ERROR:" + ex.getMessage());

         }
         //del monto que estan pagando entre 1.16 y luego * .16
         double dblImpuesto1 = dblImporte / (1 + (dblFactorTasa / 100));
         dblImpuesto1 = dblImpuesto1 * (dblFactorTasa / 100);
         //Evaluamos si hay listado para agregar
         if (poli.getLstCuentasAG() == null) {
            lstCuentasAG = new ArrayList<PoliCtas>();
            poli.setLstCuentasAG(lstCuentasAG);
         } else {
            lstCuentasAG = poli.getLstCuentasAG();
         }
         //Objeto polizas deta
         pol = new PoliCtas();
         pol.setStrCuenta(strCtaVtasIVATasa);
         pol.setBolEsCargo(true);
         pol.setDblImporte(dblImpuesto1);
         pol.setStrFolioRef("");
         pol.setStrNotas(document.getFieldString("MCB_CONCEPTO"));
         lstCuentasAG.add(pol);

         //Complemento IVAS
         pol = new PoliCtas();
         pol.setStrCuenta(strCtaVtasIVATasaCob);
         pol.setBolEsCargo(false);
         pol.setDblImporte(dblImpuesto1);
         pol.setStrFolioRef("");
         pol.setStrNotas(document.getFieldString("MCB_CONCEPTO"));
         lstCuentasAG.add(pol);
         // </editor-fold>
      }
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Guardamos la poliza">
      poli.setStrBancoOrigen((strBancoOrigen == null ? "" : strBancoOrigen));
      poli.setStrCuentaOrigen((strCuentaOrigen == null ? "" : strCuentaOrigen));
      poli.setStrBancoDestino((strBancoDestino == null ? "" : strBancoDestino));
      poli.setStrCuentaDestino((strCuentaDestino == null ? "" : strCuentaDestino));
      poli.setStrRfcDIOT(document.getFieldString("MCB_RFCBENEFICIARIO"));
      try {
         poli.callRemote(document.getFieldInt("MP_ID"), PolizasContables.BANCOS,
            document.getFieldString("MCB_BENEFICIARIO"), document.getFieldString("MCB_CONCEPTO"));
         if (poli.strResultLast.startsWith("OK")) {
            this.intSucessBcos++;
            //Marcamos el movimiento como procesada
            this.oConn.runQueryLMD("UPDATE vta_mov_cta_bcos "
               + " SET MCB_EXEC_INTER_CP =  " + poli.strResultLast.replace("OK.", "")
               + " WHERE MCB_ID = " + intId);
         } else {
            this.intFailBcos++;
            this.lstFailsBancos.add("Error al generar la poliza para el movimiento de banco con concepto " + document.getFieldString("MCB_CONCEPTO") + " " + poli.strResultLast);
            //Marcamos el movimiento como NO procesada
            this.oConn.runQueryLMD("UPDATE vta_mov_cta_bcos "
               + " SET MCB_EXEC_INTER_CP = 0 "
               + " WHERE MCB_ID = " + intId);

         }
      } catch (Exception ex) {
         this.intFailBcos++;
         this.lstFailsBancos.add("Error al generar la poliza para el movimiento de banco con concepto " + document.getFieldString("MCB_CONCEPTO") + " " + poli.strResultLast);
         log.error("Error in call webservice?" + ex.getMessage());
         //Marcamos el movimiento como NO procesada
         this.oConn.runQueryLMD("UPDATE vta_mov_cta_bcos "
            + " SET MCB_EXEC_INTER_CP = 0 "
            + " WHERE MCB_ID = " + intId);
      }
      // </editor-fold>
   }

   /**
    * Genera los items para las partidas contables cuando el detalle de la
    * compras es de acuerdo a los centros de gasto
    *
    * @param document
    * @param poli Es la poliza
    * @param bolEsCargo Es el tipo de movimiento(cargo u abono)
    * @param strFolio Es el folio
    * @param intMonedaOpera
    * @param strPvRfc
    */
   protected void getPoliDetaGasto(TableMaster document, PolizasContables poli, boolean bolEsCargo, String strFolio, int intMonedaOpera, String strPvRfc) {
      //Lista de los cuentas agrupadas
      ArrayList<PoliCtas> lstCuentasAG = new ArrayList<PoliCtas>();
      vta_cxpagardetalle detalle = new vta_cxpagardetalle();
      ArrayList<TableMaster> lstMovs = detalle.ObtenDatosVarios(" CXP_ID = " + document.getFieldInt("CXP_ID"), oConn);
      double dblFactor = 1;
      if (intMonedaOpera != this.intMonedaBase) {
         //Aplica conversion
         this.monedas.setBoolConversionAutomatica(true);
         dblFactor = this.monedas.GetFactorConversion(document.getFieldString("CXP_FECHA"), 4, intMonedaOpera, intMonedaBase);
      }
      //Asignamos los valores de las partidas
      Iterator<TableMaster> it = lstMovs.iterator();
      while (it.hasNext()) {
         TableMaster deta = it.next();
         double dblImporte = ((deta.getFieldDouble("CXPD_IMPORTE") - deta.getFieldDouble("CXPD_IMPUESTO1")) * dblFactor);
         //Consultamos en la tabla de productos a que cuenta pertenecen...
         String strSql = "select GT_CUENTA_CONTABLE from vta_cgastos where GT_ID = " + deta.getFieldInt("GT_ID");
         ResultSet rs3;
         try {
            rs3 = this.oConn.runQuery(strSql);
            while (rs3.next()) {
               String strPR_CTA_GASTO = rs3.getString("GT_CUENTA_CONTABLE");
               if (strPR_CTA_GASTO.isEmpty()) {
                  strPR_CTA_GASTO = this.strCtaEmpty;
               }
               //Validamos si existe
               boolean bolExiste = false;
               Iterator<PoliCtas> it2 = lstCuentasAG.iterator();
               while (it2.hasNext()) {
                  PoliCtas polDeta = it2.next();
                  if (polDeta.getStrCuenta().equals(strPR_CTA_GASTO)) {
                     bolExiste = true;
                     //Ya existe la cuenta solo sumamos el importe
                     if (dblImporte < 0) {
                        if (polDeta.isBolEsCargo()) {
                           bolExiste = false;
                        } else {
                           //No es cargo lo sumamos
                           polDeta.setDblImporte(polDeta.getDblImporte() + dblImporte);
                        }
                     } else if (polDeta.isBolEsCargo()) {
                        polDeta.setDblImporte(polDeta.getDblImporte() + dblImporte);
                     } else {
                        bolExiste = false;
                     }

                  }
               }
               //Si no existe lo agregamos
               if (!bolExiste) {
                  PoliCtas pol = new PoliCtas();
                  pol.setStrCuenta(strPR_CTA_GASTO);
                  pol.setBolEsCargo(bolEsCargo);
                  pol.setDblImporte(dblImporte);
                  pol.setStrRFC(strPvRfc);
                  pol.setStrUUID(document.getFieldString("CXP_UUID"));
                  if (dblImporte < 0) {
                     pol.setBolEsCargo(!bolEsCargo);
                     pol.setDblImporte(dblImporte * -1);
                  }
                  pol.setStrFolioRef(strFolio);
                  lstCuentasAG.add(pol);
               }
            }
            rs3.close();
         } catch (SQLException ex) {
            Logger.getLogger(CuentasxPagar.class.getName()).log(Level.SEVERE, null, ex);
         }
      }
      //Vaciamos el listado de cuentas en las polizas contables
      poli.setLstCuentasAG(lstCuentasAG);
   }

   /**
    * Genera los items para las partidas contables cuando el detalle de la
    * compras es de acuerdo a los centros de gasto
    *
    * @param document
    * @param poli Es la poliza
    * @param bolEsCargo Es el tipo de movimiento(cargo u abono)
    * @param strFolio Es el folio
    * @param intMonedaOpera
    * @param dblFactorConv
    */
   protected void getPoliDetaBancosGasto(TableMaster document, PolizasContables poli,
      boolean bolEsCargo, String strFolio, int intMonedaOpera, double dblFactorConv) {
      //Lista de los cuentas agrupadas
      dblTmp1 = 0;
      ArrayList<PoliCtas> lstCuentasAG = new ArrayList<PoliCtas>();
      vta_mov_cta_bcos_deta detalle = new vta_mov_cta_bcos_deta();
      ArrayList<TableMaster> lstMovs = detalle.ObtenDatosVarios(" MCB_ID = " + document.getFieldInt("MCB_ID"), oConn);
//      double dblFactor = 1;
      boolean bolComplemento = false;
      if (intMonedaOpera != this.intMonedaBase) {
         bolComplemento = true;
//         //Buscamos la tasa de cambio
//         if (document.getFieldDouble("MCB_PARIDAD2") > 0 && document.getFieldDouble("MCB_PARIDAD2") != 1) {
//            dblFactor = document.getFieldDouble("MCB_PARIDAD2");
//            //Evaluamos si hay que realizar una operacion para aplicar el factor de cambio
//            if (this.intMonedaBase != 1) {
//               dblFactor = (1 / document.getFieldDouble("MCB_PARIDAD2"));
//            }
//         } else {
//            //Aplica conversion
//            this.monedas.setBoolConversionAutomatica(true);
//            dblFactor = this.monedas.GetFactorConversion(document.getFieldString("MCB_FECHA"), 4, intMonedaOpera, intMonedaBase);
//         }
//         if(dblFactor == 0){
//            dblFactor = 1;
//         }

      }
      //Asignamos los valores de las partidas
      Iterator<TableMaster> it = lstMovs.iterator();
      while (it.hasNext()) {
         TableMaster deta = it.next();
         double dblImporte = ((deta.getFieldDouble("MCBD_IMPORTE")));
         double dblComplemento = 0;
         log.debug(" " + bolComplemento + " " + deta.getFieldDouble("MCBD_IMPORTE") + " " + dblFactorConv);
         if (bolComplemento) {
            dblComplemento = dblImporte * dblFactorConv;
            dblComplemento -= dblImporte;
         }
         log.debug("dblComplemento:" + dblComplemento);
         dblTmp1 += dblImporte;
         //Consultamos en la tabla de productos a que cuenta pertenecen...
         String strSql = "select GT_CUENTA_CONTABLE,GT_CUENTA_CONTABLE_COMP from vta_cgastos where GT_ID = " + deta.getFieldInt("GT_ID");
         ResultSet rs3;
         try {
            rs3 = this.oConn.runQuery(strSql);
            while (rs3.next()) {
               String strPR_CTA_GASTO = rs3.getString("GT_CUENTA_CONTABLE");
               String strPR_CTA_GASTOComplemento = rs3.getString("GT_CUENTA_CONTABLE_COMP");
               if (strPR_CTA_GASTO.isEmpty()) {
                  strPR_CTA_GASTO = this.strCtaEmpty;
               }
               //Validamos si existe
               boolean bolExiste = false;
               Iterator<PoliCtas> it2 = lstCuentasAG.iterator();
               while (it2.hasNext()) {
                  PoliCtas polDeta = it2.next();
                  if (polDeta.getStrCuenta().equals(strPR_CTA_GASTO)) {
                     bolExiste = true;
                     //Ya existe la cuenta solo sumamos el importe
                     polDeta.setDblImporte(polDeta.getDblImporte() + dblImporte);
                  }
               }

               //Si no existe lo agregamos
               if (!bolExiste) {
                  log.debug("Centro de gasto " + strPR_CTA_GASTO + " " + dblImporte);
                  PoliCtas pol = new PoliCtas();
                  pol.setStrCuenta(strPR_CTA_GASTO);
                  pol.setBolEsCargo(bolEsCargo);
                  pol.setDblImporte(dblImporte);
                  pol.setStrFolioRef(strFolio);
                  pol.setStrNotas(document.getFieldString("MCB_CONCEPTO"));
                  lstCuentasAG.add(pol);
               }

               //Buscamos el complemento
               if (bolComplemento) {
                  //Validamos si existe
                  bolExiste = false;
                  it2 = lstCuentasAG.iterator();
                  while (it2.hasNext()) {
                     PoliCtas polDeta = it2.next();
                     if (polDeta.getStrCuenta().equals(strPR_CTA_GASTOComplemento)) {
                        bolExiste = true;
                        //Ya existe la cuenta solo sumamos el importe
                        polDeta.setDblImporte(polDeta.getDblImporte() + dblComplemento);
                     }
                  }

                  //Si no existe lo agregamos
                  if (!bolExiste) {
                     log.debug("Centro de gasto complemento " + strPR_CTA_GASTOComplemento + " " + dblComplemento);
                     PoliCtas pol = new PoliCtas();
                     pol.setStrCuenta(strPR_CTA_GASTOComplemento);
                     pol.setBolEsCargo(bolEsCargo);
                     pol.setDblImporte(dblComplemento);
                     pol.setStrFolioRef(strFolio);
                     pol.setStrNotas(document.getFieldString("MCB_CONCEPTO"));
                     lstCuentasAG.add(pol);
                  }
               }
            }
            rs3.close();
         } catch (SQLException ex) {
            log.error(ex.getMessage());
         }
      }
      //Vaciamos el listado de cuentas en las polizas contables
      poli.setLstCuentasAG(lstCuentasAG);
   }

   /**
    * Obtiene el detalle de la factura de servicio y le asigna los valores
    * correspondientes
    *
    * @param document
    * @param lstCuentasAG
    * @return
    */
   public int ObtenDetalleServicio(TableMaster document, ArrayList<PoliCtas> lstCuentasAG) {
      int intCuantos = 0;
      String strSql = "select CF_ID,FACD_IMPORTE,FACD_IMPUESTO1,FACD_TASAIVA1"
         + " ,(SELECT a.CF_CUENTA_CONTABLE FROM vta_conceptofactura a WHERE a.CF_ID = vta_facturasdeta.CF_ID ) as CCONTABLE"
         + "  from vta_facturasdeta where FAC_ID = " + document.getFieldInt("FAC_ID");
      try {
         ResultSet rs3 = this.oConn.runQuery(strSql);
         while (rs3.next()) {
            int intCF_ID = rs3.getInt("CF_ID");
            String strCCONTABLE = rs3.getString("CCONTABLE");
            if (intCF_ID != 0) {
               if (strCCONTABLE != null) {
                  if (!strCCONTABLE.isEmpty()) {
                     intCuantos++;
                     double dblVentaImporte = rs3.getDouble("FACD_IMPORTE") - rs3.getDouble("FACD_IMPUESTO1");
                     PoliCtas pol = new PoliCtas();
                     pol.setStrCuenta(strCCONTABLE);
                     pol.setBolEsCargo(false);
                     pol.setDblImporte(dblVentaImporte);
                     pol.setStrFolioRef(document.getFieldString("FAC_FOLIO_C"));
                     lstCuentasAG.add(pol);
                  }
               }
            }
         }
         rs3.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage());
      }
      return intCuantos;
   }

   /**
    * Carga los archivos relacionados con esta cuenta por pagar
    *
    * @param poli Es la poliza contable
    * @param intCXP_ID Es el id de la cuenta por pagar
    * @param document
    */
   public void CargarArchivos(PolizasContables poli, int intCXP_ID, TableMaster document) {
      log.debug("Inicia carga de documentos....");
      try {
         boolean bolFound = false;
         String strSql = "select CPR_DESCRIPCION,CPR_PATH,CPR_TAMANIO from vta_cxp_rep where CXP_ID = " + intCXP_ID;
         ResultSet rs = this.oConn.runQuery(strSql);
         while (rs.next()) {
            log.debug("Doc encontrado " + rs.getString("CPR_DESCRIPCION"));
            bolFound = true;
            poli.AgregaArchivo(rs.getString("CPR_DESCRIPCION"), rs.getString("CPR_PATH"), rs.getInt("CPR_TAMANIO"));
         }
         rs.close();
         //Si no encontro buscamos si tiene el UUID
         if (!bolFound && document != null && this.strPathBaseXMLCXP != null) {
            String strUUID = document.getFieldString("CXP_UUID");
            //Buscamos el uuid en los xml
            String files;
            File folder = new File(this.strPathBaseXMLCXP);
            File[] listOfFiles = folder.listFiles();

            for (File listOfFile : listOfFiles) {
               if (listOfFile.isFile()) {
                  files = listOfFile.getName();
                  if (files.toUpperCase().endsWith(".XML")) {
                     MovProveedor mov = new MovProveedor(oConn, null, null);
                     mov.CargaDatosXML(listOfFile.getAbsolutePath());
                     if (mov.getStrUUID().equals(strUUID)) {
                        Fechas fecha = new Fechas();
                        CuentasXPagarDoc CPR = new CuentasXPagarDoc();
                        CPR.getCPR().setFieldString("CPR_DESCRIPCION", files);
                        CPR.getCPR().setFieldString("CPR_PATH", listOfFile.getAbsolutePath());
                        CPR.getCPR().setFieldInt("CXP_ID", document.getFieldInt("CXP_ID"));
                        CPR.getCPR().setFieldDouble("CPR_TAMANIO", 0);
                        CPR.getCPR().setFieldString("CPR_FECHA", fecha.getFechaActual());
                        CPR.getCPR().setFieldString("CPR_HORA", fecha.getHoraActual());
                        CPR.getCPR().setFieldInt("CPR_USUARIO", varSesiones.getIntNoUser());
                        CPR.saveCXPDoc(oConn);
                        //Insertamos el registro vinculandolo
                        poli.AgregaArchivo(files, listOfFile.getAbsolutePath(), 0);
                     }
                  }
               }
            }

         }
      } catch (SQLException ex) {
         log.error(ex.getMessage());
      }

   }

   /**
    * Carga los archivos relacionados con esta cuenta por pagar
    *
    * @param poli Es la poliza contable
    * @param intCXP_ID Es el id de la cuenta por pagar
    * @param document
    */
   public void CargarArchivosFacturas(PolizasContables poli, int intCXP_ID, TableMaster document) {
      log.debug("Inicia carga de documentos....");
//      try {
      boolean bolFound = false;
//         String strSql = "select CPR_DESCRIPCION,CPR_PATH,CPR_TAMANIO from vta_cxp_rep where CXP_ID = " + intCXP_ID;
//         ResultSet rs = this.oConn.runQuery(strSql);
//         while (rs.next()) {
//            log.debug("Doc encontrado " + rs.getString("CPR_DESCRIPCION"));
//            bolFound = true;
//            poli.AgregaArchivo(rs.getString("CPR_DESCRIPCION"), rs.getString("CPR_PATH"), rs.getInt("CPR_TAMANIO"));
//         }
//         rs.close();
      //Si no encontro buscamos si tiene el UUID
      if (!bolFound && document != null && this.strPathBaseXMLFacturas != null) {
         String strUUID = document.getFieldString("FAC_UUID");
         //Buscamos el uuid en los xml
         String files;
         File folder = new File(this.strPathBaseXMLCXP);
         File[] listOfFiles = folder.listFiles();

         for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()) {
               files = listOfFile.getName();
               if (files.toUpperCase().endsWith(".XML")) {
                  MovProveedor mov = new MovProveedor(oConn, null, null);
                  mov.CargaDatosXML(listOfFile.getAbsolutePath());
                  if (mov.getStrUUID().equals(strUUID)) {
                     Fechas fecha = new Fechas();
                     CuentasXPagarDoc CPR = new CuentasXPagarDoc();
                     CPR.getCPR().setFieldString("CPR_DESCRIPCION", files);
                     CPR.getCPR().setFieldString("CPR_PATH", listOfFile.getAbsolutePath());
                     CPR.getCPR().setFieldInt("CXP_ID", document.getFieldInt("CXP_ID"));
                     CPR.getCPR().setFieldDouble("CPR_TAMANIO", 0);
                     CPR.getCPR().setFieldString("CPR_FECHA", fecha.getFechaActual());
                     CPR.getCPR().setFieldString("CPR_HORA", fecha.getHoraActual());
                     CPR.getCPR().setFieldInt("CPR_USUARIO", varSesiones.getIntNoUser());
                     CPR.saveCXPDoc(oConn);
                     //Insertamos el registro vinculandolo
                     poli.AgregaArchivo(files, listOfFile.getAbsolutePath(), 0);
                  }
               }
            }
         }

      }
//      } catch (SQLException ex) {
//         log.error(ex.getMessage());
//      }

   }

   /**
    * Limpia las polizas de cierto periodo
    *
    * @param intEMP_ID
    * @param strMesAnio
    * @return Regreso la respuesta del servicio
    */
   public String doCleanPolizaAuto(int intEMP_ID, String strMesAnio) {
      //Obtenemos los datos de la empresa
      String strEMP_PASSCP = "";
      String strEMP_USERCP = "";
      String strEMP_URLCP = "";
      String strSql = "SELECT EMP_URLCP,EMP_USERCP,EMP_PASSCP FROM vta_empresas where EMP_ID=" + intEMP_ID + "";
      try {
         ResultSet rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            strEMP_URLCP = rs.getString("EMP_URLCP");
            strEMP_USERCP = rs.getString("EMP_USERCP");
            strEMP_PASSCP = rs.getString("EMP_PASSCP");
         }
         rs.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage());
      }
      String strResp = "OK";
      //Llamamaos al objeto
      PolizasContables poli = new PolizasContables(this.oConn, this.varSesiones, null);
      if (!strEMP_URLCP.trim().isEmpty()) {
         poli.setStrURLServicio(strEMP_URLCP);
      }
      strResp = poli.doCleanPolizaAuto(strEMP_USERCP, strEMP_PASSCP, strMesAnio);

      return strResp;
   }

   /**
    * Genera las polizas de las nominas
    *
    * @param intEMP_ID Es el id de la empresa
    * @param strMesAnio Es le periodo
    * @param strEMP_PASSCP Es el password de contabilidad
    * @param strEMP_USERCP Es el usuario
    * @param strEMP_URLCP Es la url para la contabilidad
    */
   public void GeneraContaNominas(int intEMP_ID, String strMesAnio, String strEMP_PASSCP, String strEMP_USERCP, String strEMP_URLCP) {
      //Consultamos movimientos de nominas
      try {
         //Consultamos tas las operac ion bd local
         String strsqlmaster = "SELECT NOM_ID,NOM_MONEDA "
            + " FROM rhh_nominas where "
            + "   left(NOM_FECHA,6)='" + strMesAnio + "' AND NOM_ANULADA = 0 and EMP_ID = " + intEMP_ID + " "
            + " " + strFiltroNominas;
         ResultSet rsM = oConn.runQuery(strsqlmaster);
         while (rsM.next()) {
            int intId = rsM.getInt("NOM_ID");
            TableMaster document = new rhh_nominas();
            document.ObtenDatos(intId, oConn);
            CalculaPolizaContablenNominas(intEMP_ID, strEMP_PASSCP, strEMP_USERCP, strEMP_URLCP, document.getFieldInt("NOM_MONEDA"), document, intId, 0, "NEW");
         }
         rsM.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage());
      }
   }

   /**
    * Calcula la poliza de nominas
    *
    * @param intEMP_ID Es el id de la empresa
    * @param strEMP_PASSCP Es el password de contabilidad
    * @param strEMP_USERCP Es el usuario
    * @param strEMP_URLCP Es la url para la contabilidad
    * @param fieldInt Id de la moneda
    * @param document Es el documento
    * @param intNomId
    * @param i
    * @param aNEW Tipo de documento
    */
   public void CalculaPolizaContablenNominas(int intEMP_ID, String strEMP_PASSCP, String strEMP_USERCP, String strEMP_URLCP, int fieldInt, TableMaster document, int intNomId, int i, String aNEW) {
      // <editor-fold defaultstate="collapsed" desc="Generamos objeto de poliza">
      PolizasContables poli = new PolizasContables(this.oConn, this.varSesiones, null);
      poli.setStrOper(aNEW);
      poli.setStrUserCte(strEMP_USERCP);
      poli.setStrPassCte(strEMP_PASSCP);
      poli.setStrURLServicio(strEMP_URLCP);

      //Asignamos documento
      poli.setDocumentMaster(document);
      poli.setBolVTA_DETA(true);
      // </editor-fold>

      //Lista de cuentas
      ArrayList<PoliCtas> lstCuentasAG = poli.getLstCuentasAG();
      if (lstCuentasAG == null) {
         lstCuentasAG = new ArrayList<PoliCtas>();
      }
      // <editor-fold defaultstate="collapsed" desc="Percepciones">
      String strSql = "SELECT SUM(NOMD_CANTIDAD*NOMD_UNITARIO) AS TOT,rhh_percepciones.PERC_ID ,"
         + "rhh_departamento_cuentas.DESC_CONTA_CUENTA,NOMD_NOTAS \n"
         + "from rhh_nominas,rhh_nominas_deta, rhh_percepciones ,rhh_departamento_cuentas \n"
         + "WHERE rhh_nominas.NOM_ID = rhh_nominas_deta.NOM_ID and \n"
         + "rhh_nominas_deta.PERC_ID =  rhh_percepciones.PERC_ID \n"
         + " and rhh_departamento_cuentas.DP_ID = rhh_nominas.DP_ID \n"
         + " and rhh_departamento_cuentas.PERC_ID = rhh_percepciones.PERC_ID \n"
         + "AND rhh_nominas.NOM_ID = " + intNomId + " AND rhh_nominas_deta.PERC_ID <> 0\n"
         + "GROUP BY rhh_percepciones.PERC_ID ,PERC_CONTA_CUENTA,NOMD_NOTAS;";
      try {
         ResultSet rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            double dblImporte = rs.getDouble("TOT");
            PoliCtas pol = new PoliCtas();
            pol.setStrCuenta(rs.getString("DESC_CONTA_CUENTA"));
            pol.setBolEsCargo(true);
            pol.setDblImporte(dblImporte);
            pol.setStrFolioRef(document.getFieldString("NOM_FOLIO"));
            pol.setStrNotas(rs.getString("NOMD_NOTAS"));
            lstCuentasAG.add(pol);
         }
         rs.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage());
      }
      // </editor-fold>
      // <editor-fold defaultstate="collapsed" desc="Deducciones">
      strSql = "select NOMD_CANTIDAD,NOMD_UNITARIO,NOMD_GRAVADO,DEDU_CONTA_CUENTA,NOMD_NOTAS from rhh_nominas_deta, rhh_deducciones "
         + " WHERE rhh_nominas_deta.DEDU_ID =  rhh_deducciones.DEDU_ID AND NOM_ID = " + intNomId + " AND PERC_ID = 0";
      try {
         ResultSet rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            double dblImporte = rs.getDouble("NOMD_UNITARIO") * rs.getDouble("NOMD_CANTIDAD");
            PoliCtas pol = new PoliCtas();
            pol.setStrCuenta(rs.getString("DEDU_CONTA_CUENTA"));
            pol.setBolEsCargo(false);
            pol.setDblImporte(dblImporte);
            pol.setStrFolioRef(document.getFieldString("NOM_FOLIO"));
            pol.setStrNotas(rs.getString("NOMD_NOTAS"));
            lstCuentasAG.add(pol);
         }
         rs.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage());
      }
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Sueldos por pagar">
      String strSueldosPagar = "";
      strSql = "SELECT EMP_CONTA_SUELDOS FROM vta_empresas where EMP_ID=" + intEMP_ID + "";
      try {
         ResultSet rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            strSueldosPagar = rs.getString("EMP_CONTA_SUELDOS");
         }
         rs.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage());
      }
      //Añadimos el elemento
      //Calculamos el importe
      double dblImporte = document.getFieldDouble("NOM_PERCEPCION_TOTAL");
      PoliCtas pol = new PoliCtas();
      pol.setStrCuenta(strSueldosPagar);
      pol.setBolEsCargo(false);
      pol.setDblImporte(dblImporte);
      pol.setStrFolioRef(document.getFieldString("NOM_FOLIO"));
      pol.setStrNotas(document.getFieldString("NOM_CONCEPTO"));
      lstCuentasAG.add(pol);
      // </editor-fold>

      try {
         poli.callRemote(intNomId, PolizasContables.NOMINAS,
            document.getFieldString("NOM_FOLIO"), document.getFieldString("NOM_CONCEPTO"));
         if (poli.strResultLast.startsWith("OK")) {
            this.intSucessNominas++;
            //Marcamos el movimiento como procesada
            this.oConn.runQueryLMD("UPDATE rhh_nominas "
               + " SET NOM_EXEC_INTER_CP =  " + poli.strResultLast.replace("OK.", "")
               + " WHERE NOM_ID = " + intNomId);
         } else {
            this.intFailNominas++;
            this.lstFailsNominas.add("Error al generar la poliza para el movimiento de nominas con folio " + document.getFieldString("NOM_FOLIO") + " " + poli.strResultLast);
            //Marcamos el movimiento como NO procesada
            this.oConn.runQueryLMD("UPDATE rhh_nominas "
               + " SET NOM_EXEC_INTER_CP = 0 "
               + " WHERE NOM_ID = " + intNomId);

         }
      } catch (Exception ex) {
         this.intFailNominas++;
         this.lstFailsNominas.add("Error al generar la poliza para el movimiento de nominas con folio " + document.getFieldString("NOM_FOLIO") + " " + poli.strResultLast);
         log.error("Error in call webservice Nominas?" + ex.getMessage());
         //Marcamos el movimiento como NO procesada
         this.oConn.runQueryLMD("UPDATE vta_mov_cta_bcos "
            + " SET MCB_EXEC_INTER_CP = 0 "
            + " WHERE MCB_ID = " + intNomId);
      }
   }

   /**
    * Calcula los movimientos de las notas de credito del periodo
    *
    * @param intEMP_ID Es el id de la empresa
    * @param strMesAnio Es el mes de operacion
    * @param strEMP_PASSCP Es el password
    * @param strEMP_USERCP Es el usuario
    * @param strEMP_URLCP Es la url del sistema de contabilidad
    * @param strCtaVtasGlobal Es la cuenta de ventas
    * @param strCtaVtasIVAGlobal Es la cuenta de IVA global
    * @param strCtaVtasIVATasa Es la cuenta de la tasa de IVA
    * @param strCtaVtasCteGlobal Es la cuenta del cliente
    */
   public void GeneraContaNCredito(int intEMP_ID, String strMesAnio,
      String strEMP_PASSCP, String strEMP_USERCP, String strEMP_URLCP, String strCtaVtasGlobal, String strCtaVtasIVAGlobal, String strCtaVtasIVATasa, String strCtaVtasCteGlobal) {
      String strNomTablaMaster = "vta_ncredito";
      String strPrefijoMaster = "NC";
      try {
         //Consultamos tas las operac ion bd local
         String strsqlmaster = "SELECT NC_ID,TI_ID,NC_MONEDA FROM vta_ncredito where left(NC_FECHA,6)='" + strMesAnio + "' "
            + " and EMP_ID = " + intEMP_ID + " "
            + " and NC_ANULADA = 0 " + strFiltroNCredito;
         ResultSet rsM = oConn.runQuery(strsqlmaster);
         while (rsM.next()) {
            int intId = rsM.getInt("NC_ID");
            int intTI_ID = rsM.getInt("TI_ID");
            int intMonedaOpera = rsM.getInt("NC_MONEDA");

            TableMaster document = new vta_ncredito();
            document.ObtenDatos(intId, oConn);
            //Calcula la poliza para notas de credito
            CalculaPolizaContablenNCredito(intEMP_ID,
               strEMP_PASSCP, strEMP_USERCP, strEMP_URLCP, strCtaVtasGlobal,
               strCtaVtasIVAGlobal, strCtaVtasIVATasa, strCtaVtasCteGlobal,
               intMonedaOpera, document, intTI_ID, intId, strNomTablaMaster, strPrefijoMaster, "NEW");
         }
         rsM.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage());
      }
   }

   /**
    * Calcula la poliza contable de la nota de credito
    *
    * @param intEMP_ID Es el id de empresa
    * @param strEMP_PASSCP Es el password para la contabilidad
    * @param strEMP_USERCP Es el usuario para la contabilidad
    * @param strEMP_URLCP Es la url para la contabilidad
    * @param strCtaVtasGlobal Es la cuenta de ventas global
    * @param strCtaVtasIVAGlobal Es la cuenta de iva global
    * @param strCtaVtasIVATasa Es la cuenta de tasa de iva global
    * @param strCtaVtasCteGlobal Es la cuenta del cliente glonal
    * @param intMonedaOpera Es el id de la moneda de operacion
    * @param document Es el documento
    * @param intTI_ID Es el id de la tasa de iva
    * @param intId Es el id de la operacion
    * @param strNomTablaMaster Es el nombre de la tabla principal
    * @param strPrefijoMaster Es el prefijo de la tabla principal
    * @param strTipoOper Es el tipo de operacion de la poliza
    */
   public void CalculaPolizaContablenNCredito(int intEMP_ID,
      String strEMP_PASSCP, String strEMP_USERCP, String strEMP_URLCP, String strCtaVtasGlobal,
      String strCtaVtasIVAGlobal, String strCtaVtasIVATasa, String strCtaVtasCteGlobal,
      int intMonedaOpera, TableMaster document, int intTI_ID, int intId, String strNomTablaMaster,
      String strPrefijoMaster, String strTipoOper) {
      boolean bolComplementarias = false;
      double dblComplementaria = 0;
      String strCtaComp1 = "";
      String strCtaComp1Prov = "";
      //Evaluamos si tenemos que realizar conversion de monedas
      if (intMonedaOpera != this.intMonedaBase) {
         bolComplementarias = true;
         //Aplica conversion
         this.monedas.setBoolConversionAutomatica(true);
         double dblFactor = this.monedas.GetFactorConversion(document.getFieldString("NC_FECHA"), 4, intMonedaOpera, intMonedaBase);

         dblComplementaria = document.getFieldDouble("NC_TOTAL") * dblFactor;
         dblComplementaria -= document.getFieldDouble("NC_TOTAL");

         document.setFieldDouble("NC_IMPORTE", document.getFieldDouble("NC_IMPORTE") * dblFactor);
         document.setFieldDouble("NC_IMPUESTO1", document.getFieldDouble("NC_IMPUESTO1") * dblFactor);
         document.setFieldDouble("NC_TOTAL", document.getFieldDouble("NC_TOTAL"));
      }
      //Valores para contabilidad
      String strCtaVtas = "";
      String strCtaVtasCte = "";
      String strCT_CONTA_RET_ISR = "";
      String strCT_CONTA_RET_IVA = "";
      //Buscamos cuentas para los ivas
      String strSql = "SELECT TI_CTA_CONT,TI_CTA_CONT_COB FROM vta_tasaiva "
         + " where TI_ID=" + intTI_ID + "";
      try {
         ResultSet rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            strCtaVtasIVATasa = rs.getString("TI_CTA_CONT");
         }
         rs.close();
      } catch (SQLException ex) {
         log.debug(ex.getMessage());

      }
      //Buscamos cuentas a la medida del cliente
      strSql = "SELECT CT_CONTAVTA,CT_CONTACTE,CT_CTA_ANTICIPO,"
         + "CT_CTACTE_COMPL_ANTI,CT_CONTACTE_COMPL,"
         + "CT_CONTA_RET_ISR,CT_CONTA_RET_IVA FROM vta_cliente "
         + " where CT_ID=" + document.getFieldInt("CT_ID");
      try {
         ResultSet rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            strCtaVtas = rs.getString("CT_CONTAVTA");
            strCtaVtasCte = rs.getString("CT_CONTACTE");
            strCtaComp1Prov = rs.getString("CT_CONTACTE_COMPL");
            strCT_CONTA_RET_ISR = rs.getString("CT_CONTA_RET_ISR");
            strCT_CONTA_RET_IVA = rs.getString("CT_CONTA_RET_IVA");
         }
         rs.close();
      } catch (SQLException ex) {
         log.debug(ex.getMessage());

      }

      // <editor-fold defaultstate="collapsed" desc="Consultamos la info de la empresa">
      strSql = "SELECT EMP_CTACTE_COMPL,EMP_USERCP,EMP_PASSCP,EMP_URLCP "
         + " FROM vta_empresas "
         + " WHERE EMP_ID = " + intEMP_ID + "";
      try {
         ResultSet rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            strCtaComp1 = rs.getString("EMP_CTACTE_COMPL");
            strEMP_USERCP = rs.getString("EMP_USERCP");
            strEMP_PASSCP = rs.getString("EMP_PASSCP");
            strEMP_URLCP = rs.getString("EMP_URLCP");
         }
         rs.close();
      } catch (SQLException ex) {
         log.debug("ERROR:" + ex.getMessage());

      }
      // </editor-fold>
      //Actualizamos la poliza contable
      PolizasContables poli = new PolizasContables(oConn, varSesiones, null);
      poli.setStrOper(strTipoOper);
      if (!strEMP_URLCP.trim().isEmpty()) {
         poli.setStrURLServicio(strEMP_URLCP);
      }
      log.debug("strEMP_URLCP:" + strEMP_URLCP);
      poli.setStrUserCte(strEMP_USERCP);
      poli.setStrPassCte(strEMP_PASSCP);
      poli.setDocumentMaster(document);
      //Validamos las cuentas a usar
      //Si el detalle de ventas NO es por producto
      //Ventas
      if (strCtaVtas.isEmpty()) {
         poli.getLstCuentas().add(strCtaVtasGlobal);
      } else {
         poli.getLstCuentas().add(strCtaVtas);
      }
      //IVA
      if (strCtaVtasIVATasa.isEmpty()) {
         poli.getLstCuentas().add(strCtaVtasIVAGlobal);
      } else {
         poli.getLstCuentas().add(strCtaVtasIVATasa);
      }
      //Cte
      if (strCtaVtasCte.isEmpty()) {
         poli.getLstCuentas().add(strCtaVtasCteGlobal);
      } else {
         poli.getLstCuentas().add(strCtaVtasCte);
      }
      //anadimos mas cuentas
      ArrayList<PoliCtas> lstCuentasAG = null;
      if (poli.getLstCuentasAG() != null) {
         lstCuentasAG = poli.getLstCuentasAG();
      } else {
         lstCuentasAG = new ArrayList<PoliCtas>();
      }
      //RETENCIONES
      if (document.getFieldDouble("NC_RETISR") > 0 || document.getFieldDouble("NC_RETIVA") > 0) {
         if (document.getFieldDouble("NC_RETISR") > 0) {
            poli.getLstCuentas().add(strCT_CONTA_RET_ISR);
         } else {
            poli.getLstCuentas().add("");
         }
         if (document.getFieldDouble("NC_RETIVA") > 0) {
            poli.getLstCuentas().add(strCT_CONTA_RET_IVA);
         } else {
            poli.getLstCuentas().add("");
         }
      } else {
         poli.getLstCuentas().add("");
         poli.getLstCuentas().add("");
      }

      //Complementarios
      if (bolComplementarias) {

         //Complemento clientes
         PoliCtas pol = new PoliCtas();
         if (strCtaComp1Prov.isEmpty()) {
            pol.setStrCuenta(strCtaComp1);
         } else {
            pol.setStrCuenta(strCtaComp1Prov);
         }
         pol.setBolEsCargo(false);
         pol.setDblImporte(dblComplementaria);
         pol.setStrFolioRef(document.getFieldString("NC_FOLIO_C"));
         lstCuentasAG.add(pol);

         //Solo lo asignamos si no tiene un listado de cuentas
         if (poli.getLstCuentasAG() == null) {
            poli.setLstCuentasAG(lstCuentasAG);
         }
      }

      try {
         poli.callRemote(intId, PolizasContables.NCREDITO);
         if (poli.strResultLast.startsWith("OK")) {
            this.intSucessNcredito++;
            //Marcamos la venta como procesada
            this.oConn.runQueryLMD("UPDATE " + strNomTablaMaster + " "
               + " SET " + strPrefijoMaster + "_EXEC_INTER_CP =  " + poli.strResultLast.replace("OK.", "")
               + " WHERE " + strPrefijoMaster + "_ID = " + intId);
         } else {
            //Marcamos la venta NO como procesada
            this.oConn.runQueryLMD("UPDATE " + strNomTablaMaster + " "
               + " SET " + strPrefijoMaster + "_EXEC_INTER_CP =  0 "
               + " WHERE " + strPrefijoMaster + "_ID = " + intId);
            this.intFailNcredito++;
            log.debug("strResultLast?" + poli.strResultLast);
            this.lstFailsNcredito.add("Error al generar la poliza para la nota de credito con folio " + document.getFieldString("NC_FOLIO_C") + " " + poli.strResultLast);
         }
      } catch (Exception ex) {
         //Marcamos la venta NO como procesada
         this.oConn.runQueryLMD("UPDATE " + strNomTablaMaster + " "
            + " SET " + strPrefijoMaster + "_EXEC_INTER_CP =  0 "
            + " WHERE " + strPrefijoMaster + "_ID = " + intId);
         this.intFailNcredito++;
         this.lstFailsNcredito.add("Error al generar la poliza para la nota de credito con folio " + document.getFieldString("NC_FOLIO_C") + " " + poli.strResultLast);
         log.error("Error calling..." + ex.getLocalizedMessage() + " " + ex.getMessage() + " ");

      }
   }

   /**
    * Calcula las polizas contables de las notas de cargo
    *
    * @param intEMP_ID Es el id de empresa
    * @param strMesAnio Es el periodo de donde se tomaran los movimientos por
    * contabilizar(AAAAMM)
    * @param strEMP_PASSCP Es el password para la contabilidad
    * @param strEMP_USERCP Es el usuario para la contabilidad
    * @param strEMP_URLCP Es la url para la contabilidad
    * @param strCtaVtasGlobal Es la cuenta de ventas global
    * @param strCtaVtasIVAGlobal Es la cuenta de iva global
    * @param strCtaVtasIVATasa Es la cuenta de tasa de iva global
    * @param strCtaVtasCteGlobal Es la cuenta del cliente glonal
    */
   public void GeneraContaNotasCargo(int intEMP_ID, String strMesAnio,
      String strEMP_PASSCP, String strEMP_USERCP, String strEMP_URLCP, String strCtaVtasGlobal, String strCtaVtasIVAGlobal, String strCtaVtasIVATasa, String strCtaVtasCteGlobal) {
      String strNomTablaMaster = "vta_notas_cargos";
      String strPrefijoMaster = "NCA";
      try {
         //Consultamos tas las operac ion bd local
         String strsqlmaster = "SELECT NCA_ID,TI_ID,NCA_MONEDA FROM vta_notas_cargos where left(NCA_FECHA,6)='" + strMesAnio + "' "
            + " and EMP_ID = " + intEMP_ID + " "
            + " and NCA_ANULADA = 0 " + strFiltroFacturas;
         ResultSet rsM = oConn.runQuery(strsqlmaster);
         while (rsM.next()) {
            int intId = rsM.getInt("NCA_ID");
            int intTI_ID = rsM.getInt("TI_ID");
            int intMonedaOpera = rsM.getInt("NCA_MONEDA");

            TableMaster document = new VtaNotasCargos();
            document.ObtenDatos(intId, oConn);
            //Calcula la poliza para facturas
            CalculaPolizaContableNotasCargo(intEMP_ID,
               strEMP_PASSCP, strEMP_USERCP, strEMP_URLCP, strCtaVtasGlobal,
               strCtaVtasIVAGlobal, strCtaVtasIVATasa, strCtaVtasCteGlobal,
               intMonedaOpera, document, intTI_ID, intId, strNomTablaMaster, strPrefijoMaster, "NEW");
         }
         rsM.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage());
      }
   }

   /**
    * Calcula la poliza contable para las facturas
    *
    * @param intEMP_ID Es el id de empresa
    * @param strEMP_PASSCP Es el password para la contabilidad
    * @param strEMP_USERCP Es el usuario para la contabilidad
    * @param strEMP_URLCP Es la url para la contabilidad
    * @param strCtaVtasGlobal Es la cuenta de ventas global
    * @param strCtaVtasIVAGlobal Es la cuenta de iva global
    * @param strCtaVtasIVATasa Es la cuenta de tasa de iva global
    * @param strCtaVtasCteGlobal Es la cuenta del cliente glonal
    * @param intMonedaOpera Es el id de la moneda de operacion
    * @param document Es el documento
    * @param intTI_ID Es el id de la tasa de iva
    * @param intId Es el id de la operacion
    * @param strNomTablaMaster Es el nombre de la tabla principal
    * @param strPrefijoMaster Es el prefijo de la tabla principal
    * @param strTipoOper Es el tipo de operacion de la poliza
    */
   public void CalculaPolizaContableNotasCargo(int intEMP_ID,
      String strEMP_PASSCP, String strEMP_USERCP, String strEMP_URLCP, String strCtaVtasGlobal,
      String strCtaVtasIVAGlobal, String strCtaVtasIVATasa, String strCtaVtasCteGlobal,
      int intMonedaOpera, TableMaster document, int intTI_ID, int intId, String strNomTablaMaster,
      String strPrefijoMaster, String strTipoOper) {
      boolean bolComplementarias = false;
      double dblComplementaria = 0;
      String strCtaComp1 = "";
      String strCtaComp1Prov = "";
      //Evaluamos si tenemos que realizar conversion de monedas
      if (intMonedaOpera != this.intMonedaBase) {
         bolComplementarias = true;
         //Aplica conversion
         this.monedas.setBoolConversionAutomatica(true);
         double dblFactor = this.monedas.GetFactorConversion(document.getFieldString("NCA_FECHA"), 4, intMonedaOpera, intMonedaBase);

         dblComplementaria = document.getFieldDouble("NCA_TOTAL") * dblFactor;
         dblComplementaria -= document.getFieldDouble("NCA_TOTAL");

         document.setFieldDouble("NCA_IMPORTE", document.getFieldDouble("NCA_IMPORTE") * dblFactor);
         document.setFieldDouble("NCA_IMPUESTO1", document.getFieldDouble("NCA_IMPUESTO1") * dblFactor);
         document.setFieldDouble("NCA_TOTAL", document.getFieldDouble("NCA_TOTAL"));
      }
      //Valores para contabilidad
      String strCtaVtas = "";
      String strCtaVtasCte = "";
      String strCT_CONTA_RET_ISR = "";
      String strCT_CONTA_RET_IVA = "";
      int intCT_CATEGORIA1 = 0;
      //Buscamos cuentas para los ivas
      String strSql = "SELECT TI_CTA_CONT,TI_CTA_CONT_COB FROM vta_tasaiva "
         + " where TI_ID=" + intTI_ID + "";
      try {
         ResultSet rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            strCtaVtasIVATasa = rs.getString("TI_CTA_CONT");
         }
         rs.close();
      } catch (SQLException ex) {
         log.debug(ex.getMessage());

      }
      //Buscamos cuentas a la medida del cliente
      strSql = "SELECT CT_CONTAVTA,CT_CONTACTE,CT_CTA_ANTICIPO,"
         + "CT_CTACTE_COMPL_ANTI,CT_CONTACTE_COMPL,"
         + "CT_CONTA_RET_ISR,CT_CONTA_RET_IVA,CT_CATEGORIA1 FROM vta_cliente "
         + " where CT_ID=" + document.getFieldInt("CT_ID");
      try {
         ResultSet rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            strCtaVtas = rs.getString("CT_CONTAVTA");
            strCtaVtasCte = rs.getString("CT_CONTACTE");
            strCtaComp1Prov = rs.getString("CT_CONTACTE_COMPL");
            strCT_CONTA_RET_ISR = rs.getString("CT_CONTA_RET_ISR");
            strCT_CONTA_RET_IVA = rs.getString("CT_CONTA_RET_IVA");
            intCT_CATEGORIA1 = rs.getInt("CT_CATEGORIA1");
         }
         rs.close();
      } catch (SQLException ex) {
         log.debug(ex.getMessage());

      }

      // <editor-fold defaultstate="collapsed" desc="Consultamos la info de la empresa">
      strSql = "SELECT EMP_CTACTE_COMPL,EMP_USERCP,EMP_PASSCP,EMP_URLCP "
         + " FROM vta_empresas "
         + " WHERE EMP_ID = " + intEMP_ID + "";
      try {
         ResultSet rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            strCtaComp1 = rs.getString("EMP_CTACTE_COMPL");
            strEMP_USERCP = rs.getString("EMP_USERCP");
            strEMP_PASSCP = rs.getString("EMP_PASSCP");
            strEMP_URLCP = rs.getString("EMP_URLCP");
         }
         rs.close();
      } catch (SQLException ex) {
         log.debug("ERROR:" + ex.getMessage());

      }
      // </editor-fold>
      //Actualizamos la poliza contable
      PolizasContables poli = new PolizasContables(oConn, varSesiones, null);
      poli.setStrOper(strTipoOper);
      int intValOper = PolizasContables.NOTAS_CARGO;
      if (!strEMP_URLCP.trim().isEmpty()) {
         poli.setStrURLServicio(strEMP_URLCP);
      }
      poli.setStrUserCte(strEMP_USERCP);
      poli.setStrPassCte(strEMP_PASSCP);
      poli.setDocumentMaster(document);
      //Validamos las cuentas a usar
      //Si el detalle de ventas NO es por producto

      //Obtenemos el Id de la Categoria del cliente
      int intCC1_ID = document.getFieldInt("CC1_ID");

      if (intCC1_ID == 0) {
         intCC1_ID = intCT_CATEGORIA1;
      }

      String strCTA_VENTAS = "";
      String strCTA_CLIENTE = "";
      String strCTA_RET_ISR = "";
      String strCTA_RET_IVA = "";

      if (intCC1_ID != 0) {

         try {
            //Consultamos  las operaciones
            String strsql = "select * from vta_cliecat1 where CC1_ID =" + intCC1_ID;
            ResultSet rs = oConn.runQuery(strsql);
            while (rs.next()) {
               strCTA_VENTAS = rs.getString("CTA_VENTAS");
               strCTA_CLIENTE = rs.getString("CTA_CLIENTE");
               strCTA_RET_ISR = rs.getString("CTA_RET_ISR");
               strCTA_RET_IVA = rs.getString("CTA_RET_IVA");
            }
            rs.close();
         } catch (SQLException ex) {
            log.error(ex.getMessage());
         }
      }
      log.debug("Ventas");
      //Ventas
      if (strCtaVtas.isEmpty()) {
         if (strCTA_VENTAS.equals("")) {
            log.debug("vta_empresas.3.." + strCtaVtasGlobal);
            poli.getLstCuentas().add(strCtaVtasGlobal);
         } else {
            log.debug("vta_cliecat1.2.." + strCTA_VENTAS);
            poli.getLstCuentas().add(strCTA_VENTAS);
         }
      } else {
         log.debug("vta_clientes.1.." + strCtaVtas);
         poli.getLstCuentas().add(strCtaVtas);
      }

      //IVA
      if (strCtaVtasIVATasa.isEmpty()) {
         poli.getLstCuentas().add(strCtaVtasIVAGlobal);
      } else {
         poli.getLstCuentas().add(strCtaVtasIVATasa);
      }

      //Cte
      log.debug("Cte");
      if (strCtaVtasCte.isEmpty()) {
         if (strCTA_CLIENTE.equals("")) {
            log.debug("vta_empresas.3.." + strCtaVtasCteGlobal);
            poli.getLstCuentas().add(strCtaVtasCteGlobal);
         } else {
            log.debug("vta_cliecat1.2.." + strCTA_CLIENTE);
            poli.getLstCuentas().add(strCTA_CLIENTE);
         }
      } else {
         log.debug("vta_clientes.1.." + strCtaVtasCte);
         poli.getLstCuentas().add(strCtaVtasCte);
      }
//            //Validamos si la cuenta de ventas se genera a partir de productos
//            if (intEMP_VTA_DETA == 1) {
//               poli.setBolVTA_DETA(true);
//               //Barremos productos para obtener cuentas agrupadas
//               this.getPoliDetaProd(poli, false, this.document.getFieldString("NCA_FOLIO"));
//            }
      //anadimos mas cuentas
      ArrayList<PoliCtas> lstCuentasAG = null;
      if (poli.getLstCuentasAG() != null) {
         lstCuentasAG = poli.getLstCuentasAG();
      } else {
         lstCuentasAG = new ArrayList<PoliCtas>();
      }
      if (document.getFieldInt("NCA_ESSERV") == 1) {
         //El detalle de las ventas
         int intCuantosItems = ObtenDetalleServicio(document, lstCuentasAG);
         if (intCuantosItems > 0) {
            poli.setBolVTA_DETA(true);
         }
      }
      //RETENCIONES
      log.debug("RETENCIONES");
      if (document.getFieldDouble("NCA_RETISR") > 0 || document.getFieldDouble("NCA_RETIVA") > 0) {
         if (document.getFieldDouble("NCA_RETISR") > 0) {
            if (strCTA_RET_ISR.equals("")) {
               log.debug("vta_empresas.3.." + strCT_CONTA_RET_ISR);
               poli.getLstCuentas().add(strCT_CONTA_RET_ISR);
            } else {
               log.debug("vta_cliecat1.2.." + strCTA_RET_ISR);
               poli.getLstCuentas().add(strCTA_RET_ISR);
            }
         } else {
            log.debug("vta_clientes.1.." + "");
            poli.getLstCuentas().add("");
         }
         if (document.getFieldDouble("NCA_RETIVA") > 0) {
            if (strCTA_RET_IVA.equals("")) {
               log.debug("vta_empresas.3.." + strCT_CONTA_RET_IVA);
               poli.getLstCuentas().add(strCT_CONTA_RET_IVA);
            } else {
               log.debug("vta_cliecat1.2.." + strCTA_RET_IVA);
               poli.getLstCuentas().add(strCTA_RET_IVA);
            }
         } else {
            log.debug("vta_clientes.1.." + "");
            poli.getLstCuentas().add("");
         }
      } else {
         poli.getLstCuentas().add("");
         poli.getLstCuentas().add("");
      }

      //Complementarios
      if (bolComplementarias) {

         //Complemento clientes
         PoliCtas pol = new PoliCtas();
         if (strCtaComp1Prov.isEmpty()) {
            pol.setStrCuenta(strCtaComp1);
         } else {
            pol.setStrCuenta(strCtaComp1Prov);
         }
         pol.setBolEsCargo(true);
         pol.setDblImporte(dblComplementaria);
         pol.setStrFolioRef(document.getFieldString("NCA_FOLIO_C"));
         lstCuentasAG.add(pol);

         //Solo lo asignamos si no tiene un listado de cuentas
         if (poli.getLstCuentasAG() == null) {
            poli.setLstCuentasAG(lstCuentasAG);
         }
      }

      //Buscamos documentos relacionados con la cuenta por pagar para enviarlos
      CargarArchivosFacturas(poli, document.getFieldInt("NCA_ID"), document);

      try {
         poli.callRemote(intId, intValOper);
         if (poli.strResultLast.startsWith("OK")) {
            this.intSucessFacturas++;
            //Marcamos la venta como procesada
            this.oConn.runQueryLMD("UPDATE " + strNomTablaMaster + " "
               + " SET " + strPrefijoMaster + "_EXEC_INTER_CP =  " + poli.strResultLast.replace("OK.", "")
               + " WHERE " + strPrefijoMaster + "_ID = " + intId);
         } else {
            //Marcamos la venta NO como procesada
            this.oConn.runQueryLMD("UPDATE " + strNomTablaMaster + " "
               + " SET " + strPrefijoMaster + "_EXEC_INTER_CP =  0 "
               + " WHERE " + strPrefijoMaster + "_ID = " + intId);
            this.intFailFacturas++;
            log.debug("strResultLast?" + poli.strResultLast);
            this.lstFailsFacturas.add("Error al generar la poliza para la factura con folio " + document.getFieldString("NCA_FOLIO_C") + " " + poli.strResultLast);
         }
      } catch (Exception ex) {
         //Marcamos la venta NO como procesada
         this.oConn.runQueryLMD("UPDATE " + strNomTablaMaster + " "
            + " SET " + strPrefijoMaster + "_EXEC_INTER_CP =  0 "
            + " WHERE " + strPrefijoMaster + "_ID = " + intId);
         this.intFailFacturas++;
         this.lstFailsFacturas.add("Error al generar la poliza para la factura con folio " + document.getFieldString("NCA_FOLIO_C") + " " + poli.strResultLast);
         log.error("Error calling..." + ex.getLocalizedMessage() + " " + ex.getMessage() + " ");

      }
   }
// </editor-fold>

   private void getPoliDetaComi(TableMaster document, PolizasContables poli,
      boolean bolEsCargo, String strFolio, int intMonedaOpera, double dblFactorConv,
      int intIdMovBco, int intTI_ID, double dblFactorTasa) {
      log.debug("Buscamos el detalle de movimientos...");
      //Lista de los cuentas agrupadas
      this.dblTmp4 = 0;
      vta_mov_cta_bcos_deta detalle = new vta_mov_cta_bcos_deta();
      ArrayList<TableMaster> lstMovs = detalle.ObtenDatosVarios(" MCB_ID = " + intIdMovBco, oConn);
      log.debug("Encontramos..." + lstMovs.size());
      boolean bolComplemento = false;
//      if (intMonedaOpera != this.intMonedaBase) {
//         bolComplemento = true;
//
//      }
      //Asignamos los valores de las partidas
      Iterator<TableMaster> it = lstMovs.iterator();
      while (it.hasNext()) {
         TableMaster deta = it.next();
         double dblImporte = ((deta.getFieldDouble("MCBD_IMPORTE")));
         double dblComplemento = 0;
         log.debug(" " + bolComplemento + " " + deta.getFieldDouble("MCBD_IMPORTE") + " " + dblFactorConv);
         if (bolComplemento) {
            dblComplemento = dblImporte * dblFactorConv;
            dblComplemento -= dblImporte;
         }
         log.debug("dblComplemento:" + dblComplemento);

         dblTmp4 += dblImporte;
         log.debug("dblImporte:" + dblImporte);
         //Consultamos en la tabla de productos a que cuenta pertenecen...
         String strSql = "select GT_CUENTA_CONTABLE,GT_CUENTA_CONTABLE_COMP from vta_cgastos where GT_ID = " + deta.getFieldInt("GT_ID");
         ResultSet rs3;
         try {
            rs3 = this.oConn.runQuery(strSql);
            while (rs3.next()) {
               String strPR_CTA_GASTO = rs3.getString("GT_CUENTA_CONTABLE");
               String strPR_CTA_GASTOComplemento = rs3.getString("GT_CUENTA_CONTABLE_COMP");
               if (strPR_CTA_GASTO.isEmpty()) {
                  strPR_CTA_GASTO = this.strCtaEmpty;
               }
               //Validamos si existe
               boolean bolExiste = false;
               Iterator<PoliCtas> it2 = poli.getLstCuentasAG().iterator();
               while (it2.hasNext()) {
                  PoliCtas polDeta = it2.next();
                  if (polDeta.getStrCuenta().equals(strPR_CTA_GASTO)) {
                     bolExiste = true;
                     //Ya existe la cuenta solo sumamos el importe
                     polDeta.setDblImporte(polDeta.getDblImporte() + dblImporte);
                  }
               }

               //Si no existe lo agregamos
               if (!bolExiste) {
                  log.debug("Centro de gasto " + strPR_CTA_GASTO + " " + dblImporte);

                  if (intTI_ID > 0) {
                     if (intTI_ID != 3 && intTI_ID != 4) {
                        //del monto que estan pagando entre 1.16 y luego * .16
                        dblImporte = dblImporte / (1 + (dblFactorTasa / 100));
                        log.debug("dblImporte:" + dblImporte);
                     }
                  }
                  log.debug("dblImporte:" + dblImporte);

                  PoliCtas pol = new PoliCtas();
                  pol.setStrCuenta(strPR_CTA_GASTO);
                  pol.setBolEsCargo(bolEsCargo);
                  pol.setDblImporte(dblImporte);
                  pol.setStrFolioRef(strFolio);
//                  pol.setStrNotas(document.getFieldString("MCB_CONCEPTO"));
                  poli.getLstCuentasAG().add(pol);
               }

               //Buscamos el complemento
               if (bolComplemento) {
                  //Validamos si existe
                  bolExiste = false;
                  it2 = poli.getLstCuentasAG().iterator();
                  while (it2.hasNext()) {
                     PoliCtas polDeta = it2.next();
                     if (polDeta.getStrCuenta().equals(strPR_CTA_GASTOComplemento)) {
                        bolExiste = true;
                        //Ya existe la cuenta solo sumamos el importe
                        polDeta.setDblImporte(polDeta.getDblImporte() + dblComplemento);
                     }
                  }

                  //Si no existe lo agregamos
                  if (!bolExiste) {
                     log.debug("Centro de gasto complemento " + strPR_CTA_GASTOComplemento + " " + dblComplemento);
                     PoliCtas pol = new PoliCtas();
                     pol.setStrCuenta(strPR_CTA_GASTOComplemento);
                     pol.setBolEsCargo(bolEsCargo);
                     pol.setDblImporte(dblComplemento);
                     pol.setStrFolioRef(strFolio);
                     pol.setStrNotas(document.getFieldString("MCB_CONCEPTO"));
                     poli.getLstCuentasAG().add(pol);
                  }
               }
            }
            rs3.close();
         } catch (SQLException ex) {
            log.error(ex.getMessage());
         }
      }
   }

   /**
    * Genera las polizas contables de Notas de cargo de proveedores de un
    * periodo en particular
    *
    * @param intEMP_ID Es el id de empresa
    * @param strMesAnio Es le periodo donde tomara los movimientos(AAAAMM)
    * @param strEMP_PASSCP Es el password para la contabilidad
    * @param strEMP_USERCP Es el usuario para la contabilidad
    * @param strEMP_URLCP Es la url para la contabilidad
    * @param strCXPGlobal El la cuenta global de cxpagar
    * @param strCXPProvGlobal Es la cuenta de proveedor global
    * @param strCXPIVAGlobal Es la cuenta global de iva
    */
   public void GeneraContaCXPNotasCargo(int intEMP_ID, String strMesAnio, String strEMP_PASSCP, String strEMP_USERCP, String strEMP_URLCP,
      String strCXPGlobal, String strCXPProvGlobal, String strCXPIVAGlobal) {
      //Consultamos cuentas por pagar no anulados
      try {
         //Consultamos  las operaciones
         String strsqlmaster = "SELECT NCA_ID,TI_ID,NCA_MONEDA FROM vta_notas_cargosprov where NCA_ANULADA = 0 "
            + " AND left(NCA_FECHA,6)='" + strMesAnio + "' and EMP_ID = " + intEMP_ID + " " + strFiltroCXP
            + " order by NCA_FECHA";
         ResultSet rsM = oConn.runQuery(strsqlmaster);
         while (rsM.next()) {
            //Recuperamos el documento
            int intId = rsM.getInt("NCA_ID");
            int intTI_ID = rsM.getInt("TI_ID");
            int intMonedaOpera = rsM.getInt("NCA_MONEDA");
            TableMaster document = new vta_cxpagar();
            document.ObtenDatos(intId, oConn);
            //Calcula la poliza contable
            CalculaPolizaContableCXPNotaCargo(intEMP_ID, strEMP_PASSCP, strEMP_USERCP, strEMP_URLCP,
               strCXPGlobal, strCXPProvGlobal, strCXPIVAGlobal, intMonedaOpera, document, intTI_ID, intId, "NEW");

         }
         rsM.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage());
      }
   }

   /**
    * Genera la poliza contable de una nota de cargo de proveedor
    *
    * @param intEMP_ID Es el id de empresa
    * @param strEMP_PASSCP Es el password para la contabilidad
    * @param strEMP_USERCP Es el usuario para la contabilidad
    * @param strEMP_URLCP Es la url para la contabilidad
    * @param strCXPGlobal El la cuenta global de cxpagar
    * @param strCXPProvGlobal Es la cuenta de proveedor global
    * @param strCXPIVAGlobal Es la cuenta global de iva
    * @param intMonedaOpera Es el id de la moneda de operacion
    * @param document Es el documento
    * @param intTI_ID Es el id de la tasa de iva
    * @param intId Es el id de la operacion
    * @param strTipoOper Indica con NEW nueva poliza y con CANCEL poliza de
    * cancelacion
    */
   public void CalculaPolizaContableCXPNotaCargo(int intEMP_ID, String strEMP_PASSCP, String strEMP_USERCP, String strEMP_URLCP,
      String strCXPGlobal, String strCXPProvGlobal, String strCXPIVAGlobal, int intMonedaOpera, TableMaster document,
      int intTI_ID, int intId, String strTipoOper) {
      boolean bolComplementarias = false;
      double dblComplementaria = 0;
      String strCtaComp1 = "";
      String strCtaComp1Prov = "";
      //Evaluamos si tenemos que realizar conversion de monedas
      if (intMonedaOpera != this.intMonedaBase) {
         bolComplementarias = true;
         //Aplica conversion
         this.monedas.setBoolConversionAutomatica(true);
         double dblFactor = this.monedas.GetFactorConversion(document.getFieldString("NCA_FECHA"), 4, intMonedaOpera, intMonedaBase);

         dblComplementaria = document.getFieldDouble("NCA_TOTAL") * dblFactor;
         dblComplementaria -= document.getFieldDouble("NCA_TOTAL");

         document.setFieldDouble("NCA_IMPORTE", document.getFieldDouble("NCA_IMPORTE") * dblFactor);
         document.setFieldDouble("NCA_IMPUESTO1", document.getFieldDouble("NCA_IMPUESTO1") * dblFactor);
         document.setFieldDouble("NCA_TOTAL", document.getFieldDouble("NCA_TOTAL"));
      }

      String strCXPIVATasa = "";
      String strCXPVtas = "";
      String strCXPVtasProv = "";
      String strPV_CONTAPROVUSD = "";
      String strPV_CONTAPROVE = "";
      String strPV_CONTAPROVDC = "";
      String strPV_CONTA_RET_ISR = "";
      String strPV_CONTA_RET_IVA = "";
      String strPV_RFC = "";
      String strPV_RAZONSOCIAL = "";
      //Validamos la tasa de iva en base al detalle
      if (intTI_ID != 1) {
         boolean bolDiversosIvas = false;
         double dblTasaIvaDeta = 0;
         String strSqlT = "select NCAD_TASAIVA1 from vta_notas_cargosprovdeta where NCA_ID = " + intId + " AND CXPD_IMPUESTO1>0 ";
         try {
            ResultSet rs = oConn.runQuery(strSqlT, true);
            while (rs.next()) {
               if (rs.getDouble("NCAD_TASAIVA1") != document.getFieldDouble("NCA_TASA1")) {
                  bolDiversosIvas = true;
                  dblTasaIvaDeta = rs.getDouble("NCAD_TASAIVA1");
                  break;
               }
            }
            rs.close();
            //Buscamos el id de la tasa del iva usado
            if (bolDiversosIvas) {
               strSqlT = "select TI_ID from vta_tasaiva where ti_tasa = " + dblTasaIvaDeta;
               rs = oConn.runQuery(strSqlT, true);
               while (rs.next()) {
                  intTI_ID = rs.getInt("TI_ID");
               }
               rs.close();
            }
         } catch (SQLException ex) {
            log.debug("ERROR:" + ex.getMessage());
         }
      }
      //Buscamos cuentas para los ivas
      String strSql = "SELECT TI_CTA_CONT,TI_CTA_CONT_COB,TI_CTA_CON_ACRE FROM vta_tasaiva "
         + " where TI_ID=" + intTI_ID + "";
      try {
         ResultSet rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            strCXPIVATasa = rs.getString("TI_CTA_CON_ACRE");
         }
         rs.close();
      } catch (SQLException ex) {
         log.debug("ERROR:" + ex.getMessage());

      }
      //Buscamos cuentas para los proveedores
      strSql = "SELECT PV_RAZONSOCIAL,PV_RFC,PV_CALLE,PV_COLONIA,PV_LOCALIDAD,PV_CONTAPROV_COMPL,"
         + " PV_MUNICIPIO,PV_ESTADO,PV_NUMERO,PV_NUMINT,PV_CONTAPROV,PV_CONTACOMP,"
         + " PV_CP,PV_DIASCREDITO,PV_LPRECIOS,PV_TIPOPERS"
         + " ,PV_CONTA_RET_ISR,PV_CONTA_RET_IVA"
         + " ,PV_CONTAPROVUSD,PV_CONTAPROVE,PV_CONTAPROVDC"
         + " FROM vta_proveedor "
         + " where PV_ID=" + document.getFieldInt("PV_ID") + "";
      try {
         ResultSet rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            strCXPVtas = rs.getString("PV_CONTACOMP");
            strCXPVtasProv = rs.getString("PV_CONTAPROV");
            strPV_CONTA_RET_ISR = rs.getString("PV_CONTA_RET_ISR");
            strPV_CONTA_RET_IVA = rs.getString("PV_CONTA_RET_IVA");
            strCtaComp1Prov = rs.getString("PV_CONTAPROV_COMPL");
            strPV_CONTAPROVUSD = rs.getString("PV_CONTAPROVUSD");
            strPV_CONTAPROVE = rs.getString("PV_CONTAPROVE");
            strPV_CONTAPROVDC = rs.getString("PV_CONTAPROVDC");
            strPV_RFC = rs.getString("PV_RFC");
            strPV_RAZONSOCIAL = rs.getString("PV_RAZONSOCIAL");
         }
         rs.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage());
      }
      // <editor-fold defaultstate="collapsed" desc="Consultamos la info de la empresa">
      strSql = "SELECT EMP_CTAPROV_COMPL,EMP_USERCP,EMP_PASSCP,EMP_URLCP "
         + " FROM vta_empresas "
         + " WHERE EMP_ID = " + intEMP_ID + "";
      try {
         ResultSet rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            strCtaComp1 = rs.getString("EMP_CTAPROV_COMPL");
            strEMP_USERCP = rs.getString("EMP_USERCP");
            strEMP_PASSCP = rs.getString("EMP_PASSCP");
            strEMP_URLCP = rs.getString("EMP_URLCP");
         }
         rs.close();
      } catch (SQLException ex) {
         log.debug("ERROR:" + ex.getMessage());

      }
      // </editor-fold>
      //Actualizamos la poliza contable
      PolizasContables poli = new PolizasContables(this.oConn, this.varSesiones, null);
      poli.setStrOper(strTipoOper);
      int intValOper = PolizasContables.CUENTASXPAGAR;
      if (!strEMP_URLCP.trim().isEmpty()) {
         poli.setStrURLServicio(strEMP_URLCP);
      }
      poli.setStrUserCte(strEMP_USERCP);
      poli.setStrPassCte(strEMP_PASSCP);
      poli.setDocumentMaster(document);
      //Validamos las cuentas a usar
      //Si el detalle de ventas NO es por producto
      //Buscamos los centros de gastos de cada partida para generarlos

      //Ventas
      if (strCXPVtas.isEmpty()) {
         poli.getLstCuentas().add(strCXPGlobal);
      } else {
         poli.getLstCuentas().add(strCXPVtas);
      }
      //IVA
      if (strCXPIVATasa.isEmpty()) {
         poli.getLstCuentas().add(strCXPIVAGlobal);
      } else {
         poli.getLstCuentas().add(strCXPIVATasa);
      }
      //Proveedor
      if (strCXPVtasProv.isEmpty()) {
         poli.getLstCuentas().add(strCXPProvGlobal);
      } else // <editor-fold defaultstate="collapsed" desc="Validacion de la cuenta de proveedor de acuerdo a la moneda">
      //Pesos
      {
         if (intMonedaOpera == 1) {
            poli.getLstCuentas().add(strCXPVtasProv);
         } else //USD
         {
            switch (intMonedaOpera) {
               case 2:
                  if (!strPV_CONTAPROVUSD.isEmpty()) {
                     poli.getLstCuentas().add(strPV_CONTAPROVUSD);
                  } else {
                     poli.getLstCuentas().add(strCXPVtasProv);
                  }  break;
               case 3:
                  if (!strPV_CONTAPROVE.isEmpty()) {
                     poli.getLstCuentas().add(strPV_CONTAPROVE);
                  } else {
                     poli.getLstCuentas().add(strCXPVtasProv);
                  }  break;
               case 4:
                  if (!strPV_CONTAPROVDC.isEmpty()) {
                     poli.getLstCuentas().add(strPV_CONTAPROVDC);
                  } else {
                     poli.getLstCuentas().add(strCXPVtasProv);
                  }  break;
               default:
                  poli.getLstCuentas().add(strCXPVtasProv);
                  break; // </editor-fold>
            }
         }      //RETENCIONES
      }
      if (document.getFieldDouble("NCA_RETISR") > 0 || document.getFieldDouble("NCA_RETIVA") > 0) {
         if (document.getFieldDouble("NCA_RETISR") > 0) {
            poli.getLstCuentas().add(strPV_CONTA_RET_ISR);
         } else {
            poli.getLstCuentas().add("");
         }
         if (document.getFieldDouble("NCA_RETIVA") > 0) {
            poli.getLstCuentas().add(strPV_CONTA_RET_IVA);
         } else {
            poli.getLstCuentas().add("");
         }
      } else {
         poli.getLstCuentas().add("");
         poli.getLstCuentas().add("");
      }
      //Validamos si la cuenta de ventas se genera a partir de productos
      //if (intEMP_VTA_DETA == 1) {
      poli.setBolVTA_DETA(true);
      //Barremos gastos para obtener cuentas agrupadas
      //Ponemos el UUID en el folio...
      String strNCA_UUID_ = document.getFieldString("NCA_UUID");
      if (strNCA_UUID_.length() > 4) {
         strNCA_UUID_ = strNCA_UUID_.substring(strNCA_UUID_.length() - 4, strNCA_UUID_.length());
      }
      if (strNCA_UUID_.length() == 0) {
         strNCA_UUID_ = document.getFieldString("NCA_FOLIO");
      }
      this.getPoliDetaGasto(document, poli, true, strNCA_UUID_, intMonedaOpera, strPV_RFC);
      //Complementarios
      if (bolComplementarias) {
         //anadimos mas cuentas
         ArrayList<PoliCtas> lstCuentasAG = null;
         if (poli.getLstCuentasAG() != null) {
            lstCuentasAG = poli.getLstCuentasAG();
         } else {
            lstCuentasAG = new ArrayList<PoliCtas>();
         }

         //Complemento proveedores
         PoliCtas pol = new PoliCtas();
         if (strCtaComp1Prov.isEmpty()) {
            pol.setStrCuenta(strCtaComp1);
         } else {
            pol.setStrCuenta(strCtaComp1Prov);
         }
         pol.setBolEsCargo(false);
         pol.setDblImporte(dblComplementaria);
         pol.setStrRFC(strPV_RFC);
         pol.setStrUUID(document.getFieldString("NCA_UUID"));
         pol.setStrFolioRef(strNCA_UUID_);
         lstCuentasAG.add(pol);
         //Solo lo asignamos si no tiene un listado de cuentas
         if (poli.getLstCuentasAG() == null) {
            poli.setLstCuentasAG(lstCuentasAG);
         }

      }
      poli.setDocumentMaster(document);
      //Definimos el rfc del proveedor
      poli.setStrRFCProveedor(strPV_RFC);

      //}
      //Buscamos documentos relacionados con la cuenta por pagar para enviarlos
//      CargarArchivos(poli, document.getFieldInt("NCA_ID"), document);
      //Llamamos al webservice
      try {
         poli.callRemote(intId, intValOper, strPV_RAZONSOCIAL, strNCA_UUID_);
         if (poli.strResultLast.startsWith("OK")) {
            this.intSucessCXP++;
            //Marcamos la CXP como procesada 
            this.oConn.runQueryLMD("UPDATE vta_cxpagar "
               + " SET " + "NCA_EXEC_INTER_CP =  " + poli.strResultLast.replace("OK.", "")
               + " WHERE " + "NCA_ID = " + intId);
         } else {
            this.intFailCXP++;
            log.debug("strResultLast?" + poli.strResultLast);
            this.lstFailsCXP.add("Error al generar la poliza para la cuentas por pagar con folio " + document.getFieldString("NCA_FOLIO") + " " + poli.strResultLast);
            //Marcamos la CXP como NO procesada 
            this.oConn.runQueryLMD("UPDATE vta_cxpagar "
               + " SET " + "NCA_EXEC_INTER_CP =  0"
               + " WHERE " + "NCA_ID = " + intId);

         }
      } catch (Exception ex) {
         this.intFailCXP++;
         this.lstFailsCXP.add("Error al generar la poliza para la cuentas por pagar con folio " + document.getFieldString("NCA_FOLIO") + " " + poli.strResultLast);
         log.debug("Error in call webservice?" + ex.getMessage() + " cxp_id:" + intId);

         //Marcamos la CXP como NO procesada 
         this.oConn.runQueryLMD("UPDATE vta_cxpagar "
            + " SET " + "NCA_EXEC_INTER_CP =  0"
            + " WHERE " + "NCA_ID = " + intId);
      }
   }

}
