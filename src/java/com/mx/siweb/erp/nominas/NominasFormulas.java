/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.nominas;

import ERP.Nominas;
import Tablas.RhhIncidencias;
import Tablas.Rhh_Nominas_Master;
import Tablas.rhh_empleados;
import com.mx.siweb.erp.nominas.Entidades.ItemConcepto;
import comSIWeb.Operaciones.Conexion; 
import comSIWeb.Scripting.scriptBase;
import comSIWeb.Utilerias.Fechas;
import comSIWeb.Utilerias.NumberString;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import org.apache.logging.log4j.LogManager;

/**
 * Contiene y calcula las formulas de las percepciones y deducciones
 *
 * @author ZeusGalindo
 */
public class NominasFormulas extends scriptBase {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   ArrayList<ItemConcepto> lstConceptos;
   Conexion oConn;
   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(NominasFormulas.class.getName());
   private rhh_empleados empleadoActual;
   private RhhIncidencias incidenciaActual;
   private Rhh_Nominas_Master nominaActual;
   private Nominas nomina;
   private double dblImporteRegresa = 0;

   public rhh_empleados getEmpleadoActual() {
      return empleadoActual;
   }

   public void setEmpleadoActual(rhh_empleados empleadoActual) {
      this.empleadoActual = empleadoActual;
   }

   public RhhIncidencias getIncidenciaActual() {
      return incidenciaActual;
   }

   public void setIncidenciaActual(RhhIncidencias incidenciaActual) {
      this.incidenciaActual = incidenciaActual;
   }

   public double getDblImporteRegresa() {
      return dblImporteRegresa;
   }

   public void setDblImporteRegresa(double dblImporteRegresa) {
      this.dblImporteRegresa = dblImporteRegresa;
   }

   public Rhh_Nominas_Master getNominaActual() {
      return nominaActual;
   }

   public void setNominaActual(Rhh_Nominas_Master nominaActual) {
      this.nominaActual = nominaActual;
   }

    public Nominas getNomina() {
        return nomina;
    }

    public void setNomina(Nominas nomina) {
        this.nomina = nomina;
    }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public NominasFormulas(Conexion oConn) {
      lstConceptos = new ArrayList<ItemConcepto>();
      this.oConn = oConn;
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   /**
    * Inicializa los valores
    */
   public void Init() {
      //Cargamos percepciones
      String strSql = "select PERC_ID,PERC_CVE,TP_ID,PERC_FORMULA  from rhh_percepciones";
      ResultSet rs;
      try {
         rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            ItemConcepto item = new ItemConcepto();
            item.setIntId(rs.getInt("PERC_ID"));
            item.setIntTipoId(rs.getInt("TP_ID"));
            item.setStrCve(rs.getString("PERC_CVE"));
            item.setStrFormula(rs.getString("PERC_FORMULA"));
            item.setBolEsPercepcion(true);
            this.lstConceptos.add(item);
         }
         rs.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage());
      }
      //Cargamos deducciones 
      strSql = "select DEDU_ID,DEDU_CVE,TD_ID,DEDU_FORMULA  from rhh_deducciones";
      try {
         rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            ItemConcepto item = new ItemConcepto();
            item.setIntId(rs.getInt("DEDU_ID"));
            item.setIntTipoId(rs.getInt("TD_ID"));
            item.setStrCve(rs.getString("DEDU_CVE"));
            item.setStrFormula(rs.getString("DEDU_FORMULA"));
            item.setBolEsPercepcion(false);
            this.lstConceptos.add(item);
         }
         rs.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage());
      }
   }

   /**
    *Calcula el importe de este rubro
    * @param intIdRubro Es el id del rubro
    * @param bolEsPercepcionX Con 1 indica que es una percepcion
    * @param empleadoActual Es el empleado por analizar
    * @param incidenciaActual Es la incidencia por analizar
    * @return Regresa el objeto ItemConcepto
    */
   public ItemConcepto CalculaImporte(int intIdRubro, boolean bolEsPercepcionX,
           rhh_empleados empleadoActual, RhhIncidencias incidenciaActual
   ) {
      ItemConcepto itemSel = null;
//Buscamos el rubro en el listado
      Iterator<ItemConcepto> it = this.lstConceptos.iterator();
      while (it.hasNext()) {
         ItemConcepto item = it.next();
         if (item.getIntId() == intIdRubro
                 && item.isBolEsPercepcion() == bolEsPercepcionX) {
            dblImporteRegresa = 0;
            log.debug(item.getIntId()  + " " + item.isBolEsPercepcion() + " " + item.getStrCve() + " " + item.getStrFormula());
            item.setDblImporte(0);
            this.empleadoActual = empleadoActual;
            this.incidenciaActual = incidenciaActual;
            //Regresamos el rubro encontrado
            boolean bolSeEjecuto = EjecutaSentencia(item);
            item.setBolSeEjecutoOK(bolSeEjecuto);
            if (!bolSeEjecuto) {
               item.setStrResultEjecucion(strMsgERROR);
            }
            itemSel = item;
         }
      }
      return itemSel;
   }

   /**
    * Este metodo ejecuta la sentencia a evaluar
    *
    * @param item Es el rubro(percepcion/deduccion) por calcular
    * @return Regresa true si se ejecuto bien la sentencia
    */
   protected boolean EjecutaSentencia(ItemConcepto item) {
      //Nos indica si la ejecucion de la sentencia fue exitosa
      boolean bolResulSentencia = false;

      //Objeto del recordset
      ResultSet Rs = null;
      //Objeto para fechas
      Fechas Fecha = new Fechas();
      //Objeto para numeros
      NumberString Numbers = new NumberString();
      //Objetos para la ejecucion de scripting usamos el SCRIPT default
      ScriptEngineManager mgr = new ScriptEngineManager();
      ScriptEngine jsEngine = mgr.getEngineByName("JavaScript");
      //Anadimos objetos para que los tome en cuenta el scripting
      jsEngine.put("Fecha", Fecha);
      jsEngine.put("Numbers", Numbers);
      jsEngine.put("request", request);
      jsEngine.put("Rs", Rs);
      jsEngine.put("oConn", oConn);
      
      jsEngine.put("empleado", empleadoActual);
      jsEngine.put("incidencia", this.incidenciaActual);
      jsEngine.put("nomina", this.nominaActual);
      jsEngine.put("nominaempleado", this.nomina);
      
      jsEngine.put("obj", this);
      jsEngine.put("bolResulSentencia", bolResulSentencia);
      jsEngine.put("dblImporteRegresa", dblImporteRegresa);

      log.debug("**Program to execute**");
      log.debug(item.getStrFormula());
      log.debug("**End Program to execute**");

      try {
         jsEngine.eval(item.getStrFormula() + ";");
      } catch (ScriptException ex) {
         ex.printStackTrace();
         this.bitacora.GeneraBitacora(ex.getMessage(), "System", "scriptNominaFormula", this.oConn);
         log.error(ex.getMessage());
      }
      //Recuperamos el valor booleano que se halla parseado
      String strValorBoolean = jsEngine.get("bolResulSentencia").toString();
      if (strValorBoolean.equals("true")) {
         bolResulSentencia = true;
      } else {
         bolResulSentencia = false;
      }
      //Recuperamos el valor double que se halla parseado
      String strValorDouble = jsEngine.get("dblImporteRegresa").toString();
      dblImporteRegresa = Double.valueOf(strValorDouble);
      item.setDblImporte(dblImporteRegresa);
      log.debug(item.getStrCve() + "  " + item.getIntId() + " " + item.isBolEsPercepcion()  +  "**dblImporteRegresa**" + dblImporteRegresa);

      return bolResulSentencia;
   }
   // </editor-fold>

}
