/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.mlm.compensacion;

import com.mx.siweb.estadocuenta.TasaCambio;
import com.mx.siweb.estadocuenta.lstTasaCambio;
import com.mx.siweb.mlm.compensacion.entidades.ClienteIndice;
import com.mx.siweb.mlm.compensacion.entidades.mlm_cliente;
import com.mx.siweb.mlm.compensacion.entidades.mlm_comis_param;
import com.mx.siweb.mlm.compensacion.entidades.mlm_comision;
import com.mx.siweb.mlm.utilerias.Redes;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Operaciones.TableMaster;
import comSIWeb.Utilerias.Fechas;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *Esta clase se encarga de realizar el calculo de comisiones
 * @author aleph_79
 */
public abstract class Comision_MLM {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   protected Conexion oConn;
   protected int intPeriodo;
   protected String strResultLast;
   private boolean EsCorridaDefinitiva;
   private boolean bolConsiderarNodosInactivos;
   private static final  Logger log = LogManager.getLogger(Comision_MLM.class.getName());

   /**
    * Regresa la conexión
    * @return
    */
   public Conexion getoConn() {
      return oConn;
   }

   /**
    * Define la conexion a la base de datos
    * @param oConn
    */
   public void setoConn(Conexion oConn) {
      this.oConn = oConn;
   }

   /**
    * Regresa el periodo
    * @return Es un id con el periodo por procesar
    */
   public int getIntPeriodo() {
      return intPeriodo;
   }

   /**
    * Define el periodo
    * @param intPeriodo Es un id con el periodo por procesar
    */
   public void setIntPeriodo(int intPeriodo) {
      this.intPeriodo = intPeriodo;
   }

   public String getStrResultLast() {
      return strResultLast;
   }

   public void setStrResultLast(String strResultLast) {
      this.strResultLast = strResultLast;
   }

   /**
    * Indica si las comisiones son definitivas
    * @return Con true son definitivas las comisiones
    */
   public boolean isEsCorridaDefinitiva() {
      return EsCorridaDefinitiva;
   }

   /**
    * Define si las comisiones son definitivas
    * @param EsCorridaDefinitiva Con true son definitivas las comisiones
    */
   public void setEsCorridaDefinitiva(boolean EsCorridaDefinitiva) {
      this.EsCorridaDefinitiva = EsCorridaDefinitiva;
   }

   /**
    * Indica si considera nodos inactivos
    * @return Con true considera nodos inactivos(default = true)
    */
   public boolean isBolConsiderarNodosInactivos() {
      return bolConsiderarNodosInactivos;
   }

   /**
    * Indicamos si considera nodos inactivos
    * @param bolConsiderarNodosInactivos Con true considera nodos inactivos(default = true)
    */
   public void setBolConsiderarNodosInactivos(boolean bolConsiderarNodosInactivos) {
      this.bolConsiderarNodosInactivos = bolConsiderarNodosInactivos;
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public Comision_MLM(Conexion oConn, int intPeriodo, boolean EsCorridaDefinitiva) {
      this.oConn = oConn;
      this.intPeriodo = intPeriodo;
      this.EsCorridaDefinitiva = EsCorridaDefinitiva;
   }
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">

   /**
    * Limpieza de tabla de comisiones, evaluacion de que se puedan generar y armado del arbol
    */
   public void doFase1() {
      //Inicializamos valores
      boolean bolPasa = true;
      this.strResultLast = "OK";

      //Validamos si el periodo existe
      String strSqlP = "Select * from mlm_comision_bitacora where MPE_ID = " + this.intPeriodo;
      try {
         ResultSet rs = this.oConn.runQuery(strSqlP, true);
         while (rs.next()) {
            int intDefinitivas = rs.getInt("CCO_DEFINITIVAS");
            if (intDefinitivas == 1) {
               bolPasa = false;
            }
         }
          //if(rs.getStatement() != null )rs.getStatement().close(); 
          rs.close();
      } catch (SQLException ex) {
         log.error("Error al buscar el periodo en comisiones " + ex.getMessage());
      }

      //Validamos si ya se corrieron definitivas
      if (bolPasa) {
         //Borramos comisiones del periodo
         String strDel1 = "delete from mlm_comision where MPE_ID = " + this.intPeriodo;
         this.oConn.runQueryLMD(strDel1);

         String strDel2 = "delete from mlm_comision_deta where MPE_ID = " + this.intPeriodo;
         this.oConn.runQueryLMD(strDel2);

         //Calculamos el arbol
         Redes red = new Redes();
         boolean bolArmo = red.armarArbol("vta_cliente", "CT_ID", "CT_UPLINE", 1, "CT_", "", " ORDER BY CT_ID", false, true, oConn);
         String strRes = "Se ha generado correctamente la red.";
         if (bolArmo) {
            strRes = "ERROR:" + red.getStrError();
         }
         
         //SI SON DEFINITIVAS marcamos el proceso
         if(this.isEsCorridaDefinitiva()){
            
         }
      } else {
         this.strResultLast = "ERROR:";
      }
   }

   /**
    * Calculo de documentos facturas, remisiones y memorandas
    * Puntos y valor negocio, por cliente
    */
   public void doFase2() {
      Fechas fecha = new Fechas();
      log.info("Inicia Fase 2.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());

      //inicializamos valores
      Map mapaClientes = new HashMap();
      this.strResultLast = "OK";

      //Obtenemos las monedas y paridades
      lstTasaCambio lstTasas = new lstTasaCambio();

      String strSql = "SELECT FAC_MONEDA FROM vta_facturas group by FAC_MONEDA";
      try {
         ResultSet rs = this.oConn.runQuery(strSql, true);
         while (rs.next()) {
            if (rs.getInt("FAC_MONEDA") != 1) {
               //Generamos objeto de conversion
               TasaCambio tCambio = new TasaCambio();
               tCambio.setMoneda1(1);
               tCambio.setMoneda2(rs.getInt("FAC_MONEDA"));
               //Buscamos la tasa de cambio de esta moneda
               String strSqlT = "select * from vta_tasacambio where "
                       + " (TC_MONEDA1 = 1 and TC_MONEDA2 = " + tCambio.getMoneda2() + ") || "
                       + " (TC_MONEDA2 = 1 and TC_MONEDA1 = " + tCambio.getMoneda2() + ") "
                       + " order by TC_FECHA DESC LIMIT 0,1";
               ResultSet rs2 = this.oConn.runQuery(strSqlT, true);
               while (rs2.next()) {
                  tCambio.setValor(rs.getDouble("TC_PARIDAD"));
               }
//               if(rs2.getStatement() != null )rs2.getStatement().close(); 
               rs2.close();
               lstTasas.Agrega(tCambio);
            }
         }
          //if(rs.getStatement() != null )rs.getStatement().close(); 
          rs.close();
         //Iniciamos transaccion
         this.oConn.runQueryLMD("BEGIN");
         double dblPuntos = 0;
         double dblNegocio = 0;
         //---------------------------- CARGA DE DOCUMENTOS DE FACTURA
         strSql = "SELECT vta_facturas.CT_ID,vta_facturas.FAC_MONEDA,"
                 + " sum(vta_facturas.FAC_IMPORTE_PUNTOS) AS PPUNTOS, "
                 + " sum(vta_facturas.FAC_IMPORTE_NEGOCIO) AS PNEGOCIO "
                 + " FROM vta_facturas WHERE vta_facturas.MPE_ID =  " + this.intPeriodo
                 + " and vta_facturas.FAC_ANULADA = 0 "
                 + " and vta_facturas.FAC_NO_MLM = 0 "
                 + " GROUP BY vta_facturas.CT_ID,vta_facturas.FAC_MONEDA";
         rs = this.oConn.runQuery(strSql, true);
         while (rs.next()) {
            dblPuntos = rs.getDouble("PPUNTOS");
            dblNegocio = rs.getDouble("PNEGOCIO");
            //Realizamos la conversion de las monedas
            if (rs.getInt("FAC_MONEDA") != 1) {
               Iterator<TasaCambio> it = lstTasas.getLista().iterator();
               while (it.hasNext()) {
                  TasaCambio tasaCambio = it.next();
                  if (tasaCambio.getMoneda2() == rs.getInt("FAC_MONEDA")) {
                     dblNegocio = dblNegocio * tasaCambio.getValor();
                  }
               }
            }
            //Evaluamos si ya existe el objeto en el mapa
            if (mapaClientes.containsKey(rs.getInt("CT_ID"))) {
               mlm_cliente cteMlm = (mlm_cliente) mapaClientes.get(rs.getInt("CT_ID"));
               cteMlm.setDblPuntos(cteMlm.getDblPuntos() + dblPuntos);
               cteMlm.setDblNegocio(cteMlm.getDblNegocio() + dblNegocio);
            } else {
               mlm_cliente cteMlm = new mlm_cliente();
               cteMlm.setIntCliente(rs.getInt("CT_ID"));
               cteMlm.setDblPuntos(dblPuntos);
               cteMlm.setDblNegocio(dblNegocio);
               mapaClientes.put(rs.getInt("CT_ID"), cteMlm);
            }
         }
          //if(rs.getStatement() != null )rs.getStatement().close(); 
          rs.close();

         //CARGA DE DOCUMENTOS DE TICKETS
         strSql = "SELECT vta_tickets.CT_ID,vta_tickets.TKT_MONEDA,"
                 + " sum(vta_tickets.TKT_IMPORTE_PUNTOS) AS PPUNTOS, "
                 + " sum(vta_tickets.TKT_IMPORTE_NEGOCIO) AS PNEGOCIO "
                 + " FROM vta_tickets WHERE vta_tickets.MPE_ID =  " + this.intPeriodo
                 + " and vta_tickets.TKT_ANULADA = 0 "
                 + " and vta_tickets.TKT_NO_MLM = 0 "
                 + " GROUP BY vta_tickets.CT_ID,vta_tickets.TKT_MONEDA";
         rs = this.oConn.runQuery(strSql, true);
         while (rs.next()) {
            dblPuntos = rs.getDouble("PPUNTOS");
            dblNegocio = rs.getDouble("PNEGOCIO");
            //Realizamos la conversion de las monedas
            if (rs.getInt("TKT_MONEDA") != 1) {
               Iterator<TasaCambio> it = lstTasas.getLista().iterator();
               while (it.hasNext()) {
                  TasaCambio tasaCambio = it.next();
                  if (tasaCambio.getMoneda2() == rs.getInt("TKT_MONEDA")) {
                     dblNegocio = dblNegocio * tasaCambio.getValor();
                  }
               }
            }
            //Evaluamos si ya existe el objeto en el mapa
            if (mapaClientes.containsKey(rs.getInt("CT_ID"))) {
               mlm_cliente cteMlm = (mlm_cliente) mapaClientes.get(rs.getInt("CT_ID"));
               cteMlm.setDblPuntos(cteMlm.getDblPuntos() + dblPuntos);
               cteMlm.setDblNegocio(cteMlm.getDblNegocio() + dblNegocio);
            } else {
               mlm_cliente cteMlm = new mlm_cliente();
               cteMlm.setIntCliente(rs.getInt("CT_ID"));
               cteMlm.setDblPuntos(dblPuntos);
               cteMlm.setDblNegocio(dblNegocio);
               mapaClientes.put(rs.getInt("CT_ID"), cteMlm);
            }

         }
          //if(rs.getStatement() != null )rs.getStatement().close(); 
          rs.close();

         //CARGA DE MEMORANDA
         strSql = "SELECT "
                 + "	mlm_bono_ind.CT_ID, 	"
                 + " SUM(mlm_bono_ind.MBI_PUNTOS) AS TPUNTOS, 	"
                 + " SUM(mlm_bono_ind.MBI_NEGOCIO) AS TNEGOCIO  "
                 + " FROM mlm_bono_ind WHERE MPE_ID =   " + this.intPeriodo
                 + " GROUP BY mlm_bono_ind.CT_ID";
         rs = this.oConn.runQuery(strSql, true);
         while (rs.next()) {
            dblPuntos = rs.getDouble("TPUNTOS");
            dblNegocio = rs.getDouble("TNEGOCIO");
            //Evaluamos si ya existe el objeto en el mapa
            if (mapaClientes.containsKey(rs.getInt("CT_ID"))) {
               mlm_cliente cteMlm = (mlm_cliente) mapaClientes.get(rs.getInt("CT_ID"));
               cteMlm.setDblPuntos(cteMlm.getDblPuntos() + dblPuntos);
               cteMlm.setDblNegocio(cteMlm.getDblNegocio() + dblNegocio);
            } else {
               mlm_cliente cteMlm = new mlm_cliente();
               cteMlm.setIntCliente(rs.getInt("CT_ID"));
               cteMlm.setDblPuntos(dblPuntos);
               cteMlm.setDblNegocio(dblNegocio);
               mapaClientes.put(rs.getInt("CT_ID"), cteMlm);
            }
         }
          //if(rs.getStatement() != null )rs.getStatement().close(); 
          rs.close();

         //Guardamos los puntos y valor negocio totales
         int intContaProcess = 0;
         int intContaProcessTot = 0;
         Iterator it = mapaClientes.entrySet().iterator();
         while (it.hasNext()) {
            Map.Entry e = (Map.Entry) it.next();
            intContaProcess++;
            intContaProcessTot++;
            mlm_cliente cteMlm = (mlm_cliente) e.getValue();
            //Guardamos info
            if (cteMlm.getDblPuntos() > 0 || cteMlm.getDblNegocio() > 0) {
               String strUpdate = "update vta_cliente set "
                       + " CT_PPUNTOS = CT_PPUNTOS + " + cteMlm.getDblPuntos() + ", "
                       + " CT_PNEGOCIO = CT_PNEGOCIO + " + cteMlm.getDblNegocio() + " "
                       + " WHERE CT_ID = " + cteMlm.getIntCliente();
               this.oConn.runQueryLMD(strUpdate);
            }
            if (intContaProcess == 1000) {
               intContaProcess = 0;
               log.info("Llevamos " + intContaProcessTot + " registros guardando puntos y vnegocio...");
               System.out.flush();
            }
         }

         //Terminamos transaccion
         if (this.strResultLast.equals("OK")) {
            this.oConn.runQueryLMD("COMMIT");
         } else {
            this.oConn.runQueryLMD("ROLLBACK");
         }

      } catch (SQLException ex) {
         log.error(ex.getMessage() + " " + ex.getSQLState());
      }
      log.info("Termina Fase 2.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());

   }

   /**
    * Calculo de niveles de acuerdo a la tabla de parametros
    * subir informacion por la red
    */
   public void doFase3() {
   }

   /**
    * Calculo de UNILEVEL
    */
   public void doFase4() {
   }

   /**
    * Calculo de DIFERENCIALES
    */
   public void doFase5() {
   }

   /**
    * Calculo de BINARIOS
    */
   public void doFase6() {
   }

   /**
    * Calculo de bonos de acuerdo al cliente
    */
   public abstract void doFaseBonos();

   /**
    * Calculo de impuestos y totales
    */
   public void doFaseImpuestos() {
   }

   /**
    * Calculo final de comisiones, totales, llenado de tabla detalle
    */
   public abstract void doFaseCierre();

   /**
    * Regresa la lista de parametros del plan de comisiones
    * @return Regresa una lista con objetos de la entidad mlm_comis_param
    */
   public ArrayList<TableMaster> getParameters() {
      ArrayList<TableMaster> lstParametros = new ArrayList<TableMaster>();
      mlm_comis_param paramsTmp = new mlm_comis_param();
      //Obtenemos los datos de la base de datos
      lstParametros = paramsTmp.ObtenDatosVarios(" 1=1 ORDER BY CP_ORDEN", oConn);
      return lstParametros;
   }

   /**
    * Nos regresa el cliente que solicitemos
    * @param intNodo Es la clave del cliente
    * @param lstCte Es el arreglo donde buscaremos
    * @param bolDebug Indica si estamos en modo debug
    * @return Nos regresa el cliente que solicitemos
    */
   public int BuscaNodoenArreglo(int intNodo, ArrayList<ClienteIndice> lstCte, boolean bolDebug) {
      int intInicio = 0;
      int intFinal = lstCte.size();
      int intMedio = 0;
      int intContaCiclos = 0;
      int intValorEncontrado = -1;
      while (intInicio <= intFinal) {
         intContaCiclos++;
         intMedio = Math.round((intInicio + intFinal) / 2);
         if (bolDebug) {
            log.info("intInicio: " + intInicio
                    + " intFinal: " + intFinal
                    + " intMedio:" + intMedio
                    + " bus " + intNodo);
            log.info(" cveAct " + lstCte.get(intMedio).getIntCte());
         }
         if (intNodo == lstCte.get(intMedio).getIntCte()) {
            intValorEncontrado = lstCte.get(intMedio).getIntIndice();
            intInicio = intFinal + 1;
         } else {
            if (intNodo > lstCte.get(intMedio).getIntCte()) {
               intInicio = intMedio + 1;
            } else {
               intFinal = intMedio - 1;
            }
         }
      }
      log.debug("Ciclos: " + intContaCiclos);
      int intNodoBusq = intValorEncontrado;
      return intNodoBusq;
   }

   /**
    * Calculo el impuesto correspondiente
    * @param cliente Es el cliente
    * @param comision Es el registro de comision maestro
    */
   public void CalculoImpuestos(mlm_cliente cliente, mlm_comision comision) {
   }
   // </editor-fold>
}
