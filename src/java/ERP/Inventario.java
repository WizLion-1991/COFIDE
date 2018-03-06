package ERP;

import Tablas.vta_movprod;
import Tablas.vta_movproddeta;
import Tablas.vta_prodlote;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Operaciones.TableMaster;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;

/**
 * Realiza las operaciones adicionales de guardado de los productos como por
 * ejemplo - Guardar precios - Ajustar las existencias - generar o adicionar los
 * lotes
 *
 * @author zeus
 */
public class Inventario extends ProcesoMaster implements ProcesoInterfaz {
   // <editor-fold defaultstate="collapsed" desc="Propiedades">

   protected vta_movprod movProd;//representa el documento de bancos
   protected ArrayList<vta_movproddeta> lstMovs;//representa el detalle del movimiento de bancos
   protected double dblEntradas = 0;
   protected double dblSalidas = 0;
   public static final int ENTRADA = 1;
   public static final int SALIDA = 2;
   public static final int TRASPASO = 3;
   public static final int TRASPASO_SALIDA = 4;
   public static final int TRASPASO_ENTRADA = 5;
   protected int intTipoOperacion;
   public static final int INV_PEPS = 1;
   public static final int INV_UEPS = 2;
   public static final int INV_PROMEDIO = 0;
   protected int intTipoCosteo;
   protected String strFechaAnul;
   protected int intNumIdTraspasoSalida = 0;
   protected int intEMP_ID = 0;
   protected int intMonedaDefa = 1;
   private double dblLastCostoPromedio = 0;
   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(Inventario.class.getName());
   private boolean bolControlEstrictoInv = false;

   public boolean isBolControlEstrictoInv() {
      return bolControlEstrictoInv;
   }

   /**
    *Indica que llevamos un control de estrictor de inventarios es decir no podemos
    * hacer movimientos de productos que no tengan la bandera de inventarios
    * @param bolControlEstrictoInv
    */
   public void setBolControlEstrictoInv(boolean bolControlEstrictoInv) {
      this.bolControlEstrictoInv = bolControlEstrictoInv;
   }
   
   /**
    * Nos regresa el tipo de operacion de inventario
    * ENTRADA,SALIDA,TRASPASO,TRASPASO_ENTRADA,TRASPASO_SALIDA
    *
    * @return regresa un entero con el tipo de operacion
    */
   public int getIntTipoOperacion() {
      return intTipoOperacion;
   }

   /**
    * Define el tipo de operacion a realizar
    * ENTRADA,SALIDA,TRASPASO,TRASPASO_ENTRADA,TRASPASO_SALIDA
    *
    * @param intTipoOperacion Es un entero con el tipo de operacion
    */
   public void setIntTipoOperacion(int intTipoOperacion) {
      this.intTipoOperacion = intTipoOperacion;
   }

   /**
    * Nos regresa el tipo de costeo PEPS UEPS Y PROMEDIO
    *
    * @return Nos regresa un entero con el tipo de costeo
    */
   public int getIntTipoCosteo() {
      return intTipoCosteo;
   }

   /**
    * Definimos el tipo de costeo PEPS UEPS Y PROMEDIO
    *
    * @param intTipoCosteo Es un entero con el tipo de costeo
    */
   public void setIntTipoCosteo(int intTipoCosteo) {
      this.intTipoCosteo = intTipoCosteo;
   }

   /**
    * Regresa el movimiento maestro de inventarios
    *
    * @return Nos regresa el movimiento de inventarios
    */
   public vta_movprod getMovProd() {
      return this.movProd;
   }

   /**
    * Anade un objeto de detalle al arreglo de objetos
    *
    * @param deta Es el objeto de detalle
    */
   public void AddDetalle(vta_movproddeta deta) {
      deta.setFieldInt("ID_USUARIOS", this.movProd.getFieldInt("ID_USUARIOS"));
      this.lstMovs.add(deta);

   }

   /**
    * Nos regresa el numero de traspaso de Salida que genero el traspado de
    * Entrada
    *
    * @return Es un valor numerico
    */
   public int getIntNumIdTraspasoSalida() {
      return intNumIdTraspasoSalida;
   }

   /**
    * Definimos el numero de traspaso de salida de la operacion de Traspaso de
    * Entrada
    *
    * @param intNumIdTraspasoSalida Es un valor numerico
    */
   public void setIntNumIdTraspasoSalida(int intNumIdTraspasoSalida) {
      this.intNumIdTraspasoSalida = intNumIdTraspasoSalida;
   }

   /**
    * Nos regresa el id de la empresa default
    *
    * @return Es un numero con el id de la empresa
    */
   public int getIntEMP_ID() {
      return intEMP_ID;
   }

   /**
    * Definimos el id de la empresa
    *
    * @param intEMP_ID Es un numero con el id de la empresa
    */
   public void setIntEMP_ID(int intEMP_ID) {
      this.intEMP_ID = intEMP_ID;
   }

   /**
    * Regresa la moneda default de la sucursal
    *
    * @return Es un entero con el id de la moneda default
    */
   public int getIntMonedaDefa() {
      return intMonedaDefa;
   }

   /**
    * Define la moneda default de la sucursal
    *
    * @param intMonedaDefa Es un entero con el id de la moneda default
    */
   public void setIntMonedaDefa(int intMonedaDefa) {
      this.intMonedaDefa = intMonedaDefa;
   }

   /**
    * Regresa el ultimo costo promedio
    *
    * @return Es el ultimo costo promedio del producto
    */
   public double getDblLastCostoPromedio() {
      return dblLastCostoPromedio;
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   /**
    * Constructor de la clase Inventario o Inventarios
    *
    * @param oConn Es la conexion
    * @param varSesiones Son las variables de sesion
    * @param request Es la peticion request
    */
   public Inventario(Conexion oConn, VariableSession varSesiones, HttpServletRequest request) {
      super(oConn, varSesiones, request);
      this.movProd = new vta_movprod();
      this.lstMovs = new ArrayList<vta_movproddeta>();
      this.intTipoOperacion = 0;
      this.bolFolioGlobal = false;
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   @Override
   public void Init() {
      this.movProd.setFieldInt("ID_USUARIOS", varSesiones.getIntNoUser());
      //Si nos pasan el id del movimiento recuperamos todos los datos
      if (this.movProd.getFieldInt("MP_ID") != 0) {
         this.strFechaAnul = this.movProd.getFieldString("MP_FECHAANUL");
         this.movProd.ObtenDatos(this.movProd.getFieldInt("MP_ID"), oConn);
         /*vta_movproddeta movdeta = new vta_movproddeta();
          movdeta.ObtenDatosVarios(" where MP_ID = " + this.movProd.getFieldInt("MP_ID"), oConn);*/
      }
   }

   
   
   
   @Override
   public void doTrx() {
      // <editor-fold defaultstate="collapsed" desc="Validaciones">
      this.strResultLast = "OK";
      this.movProd.setFieldString("MP_FECHACREATE", this.fecha.getFechaActual());
      this.movProd.setFieldString("MP_HORA", this.fecha.getHoraActual());
      //Validamos que todos los campos basico se encuentren
      if (this.movProd.getFieldInt("SC_ID") == 0) {
         this.strResultLast = "ERROR:Falta definir la sucursal";
      }
      if (this.movProd.getFieldString("MP_FECHA").equals("")) {
         this.strResultLast = "ERROR:Falta definir la fecha de la operacion";
      }
      // <editor-fold defaultstate="collapsed" desc="Validaciones especiales dependiendo del tipo de movimiento">
      switch (intTipoOperacion) {
         case Inventario.ENTRADA://ENTRADA
            //Validaciones de numeros de serie

            //Recorremos items con numeros de serie
            doEvaluaSeriesEntrada();
            break;
         case Inventario.SALIDA://SALIDA
            doEvaluaSeriesSalida();
            break;
         case Inventario.TRASPASO://TRASPASO
            //Validamos sucursal de entrada
            if (this.movProd.getFieldInt("SC_ID2") == 0) {
               this.strResultLast = "ERROR:Falta definir la sucursal DESTINO";
            }
            doEvaluaSeriesSalida();
            // <editor-fold defaultstate="collapsed" desc="Validacion de sucursal o almacen destino">
            if (!this.strResultLast.startsWith("ERROR:")) {
               //Validaciones de numeros de serie
               Iterator<vta_movproddeta> it4 = this.lstMovs.iterator();
               while (it4.hasNext()) {
                  vta_movproddeta deta = it4.next();
                  if (deta.isBolUsaSeries()) {
                     //Verificamos que no exista en el almacen destino
                     int intPR_ID2 = 0;
                     String strSqlSerie2 = "SELECT PR_ID FROM "
                             + " vta_producto where PR_CODIGO = '" + deta.getFieldInt("PR_CODIGO") + "'"
                             + " AND SC_ID = " + this.movProd.getFieldInt("SC_ID2") + " ";
                     try {
                        ResultSet rs = oConn.runQuery(strSqlSerie2, true);
                        while (rs.next()) {
                           intPR_ID2 = rs.getInt("PR_ID");
                        }
                        rs.close();

                        //Validamos si existe el numero de serie en el almacen destino
                        double dblExistencia = 0;
                        String strSqlSerie = "SELECT PL_EXISTENCIA FROM "
                                + " vta_prodlote where PR_ID = " + deta.getFieldInt("PR_ID")
                                + " AND PL_NUMLOTE = '" + deta.getFieldString("PL_NUMLOTE") + "'";

                        rs = oConn.runQuery(strSqlSerie, true);
                        while (rs.next()) {
                           dblExistencia = rs.getDouble("PL_EXISTENCIA");
                        }
                        rs.close();

                        if (dblExistencia > 0) {
                           this.strResultLast = "ERROR:EL NUMERO DE SERIE (" + deta.getFieldString("PL_NUMLOTE") + ") YA EXISTE";
                           break;
                        }
                     } catch (SQLException ex) {
                        this.strResultLast = "ERROR:" + ex.getMessage();
                        log.error(ex.getMessage());
                     }
                  }
               }
            }
            // </editor-fold>
            break;
         case Inventario.TRASPASO_ENTRADA://TRASPASO DE ENTRADA
            if (this.intNumIdTraspasoSalida == 0) {
               this.strResultLast = "ERROR:Falta El numero de traspaso de Salida";
            } else {
               this.movProd.setFieldInt("MP_IDORIGEN", intNumIdTraspasoSalida);
            }
            doEvaluaSeriesEntrada();
            break;
         case Inventario.TRASPASO_SALIDA://TRASPASO DE SALIDA
            //Validamos sucursal de entrada
            if (this.movProd.getFieldInt("SC_ID2") == 0) {
               this.strResultLast = "ERROR:Falta definir la sucursal";
            }
            doEvaluaSeriesSalida();
            break;
      }
      // </editor-fold>
      //Evaluamos la fecha de cierre
      boolean bolEvalCierre = evaluaFechaCierre(this.movProd.getFieldString("MP_FECHA"), this.movProd.getFieldInt("EMP_ID"), this.movProd.getFieldInt("SC_ID"));
      if (!bolEvalCierre) {
         this.strResultLast = "ERROR:El periodo al que corresponde el movimiento ya ha sido cerrado";
      }
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Obtenemos la paridad en caso de que la moneda sea diferente a la moneda 1">
      //Obtenemos la moneda de la sucursal
      String strSqlSuc = "SELECT MON_ID "
              + "FROM vta_sucursal WHERE SC_ID = " + this.movProd.getFieldInt("SC_ID") + "";
      try {
         ResultSet rs = oConn.runQuery(strSqlSuc, true);
         while (rs.next()) {
            this.intMonedaDefa = rs.getInt("MON_ID");

         }
         rs.close();
      } catch (SQLException ex) {
         this.strResultLast = "ERROR:" + ex.getMessage();
         log.error(ex.getMessage());
      }
      //Ajustes para productos que no se inventarian, kits y paridad
      ajustesPartidasVarios();
      // </editor-fold>
      // <editor-fold defaultstate="collapsed" desc="Validamos si pasamos las validaciones">
      if (this.strResultLast.equals("OK")) {

         // <editor-fold defaultstate="collapsed" desc="Inicializamos la operacion">
         if (this.bolTransaccionalidad) {
            this.oConn.runQueryLMD("BEGIN");
         }
         // </editor-fold>
         this.movProd.setBolGetAutonumeric(true);
         // <editor-fold defaultstate="collapsed" desc="Definimos el folio de la operacion">
         Folios folio = new Folios();
         if (this.movProd.getFieldString("MP_FOLIO").equals("")) {
            String strFolio = folio.doFolio(oConn, Folios.INVENTARIO, this.bolFolioGlobal, this.movProd.getFieldInt("SC_ID"));
            this.movProd.setFieldString("MP_FOLIO", strFolio);
         } else {
            folio.updateFolio(oConn, Folios.INVENTARIO, this.movProd.getFieldString("MP_FOLIO"),
                    this.bolFolioGlobal, this.movProd.getFieldInt("SC_ID"));
         }
         //Si se definio el id de la empresa independiente de la sucursal
         if (this.intEMP_ID != 0) {
            this.movProd.setFieldInt("EMP_ID", this.intEMP_ID);
         }
         // </editor-fold>
         String strRes1 = this.movProd.Agrega(oConn);
         if (strRes1.equals("OK")) {
            //Sumas de entradas y salidas
            double dblTmpSalidasX = 0;
            double dblTmpEntradasX = 0;
            ArrayList<vta_movproddeta> lstMovsAdi = new ArrayList<vta_movproddeta>();//Lista de elementos adicionales
            int intId = Integer.valueOf(this.movProd.getValorKey());
            Iterator<vta_movproddeta> it = this.lstMovs.iterator();
            while (it.hasNext()) {
               vta_movproddeta deta = it.next();
               deta.setFieldInt("MP_ID", intId);
               dblTmpSalidasX += deta.getFieldDouble("MPD_SALIDAS");
               dblTmpEntradasX += deta.getFieldDouble("MPD_ENTRADAS");
                
              
               lstMovsAdi.clear();
               log.debug("Hacemos primero el moviiento...." + deta.getFieldDouble("MPD_SALIDAS") + " " + deta.getFieldDouble("MPD_ENTRADAS"));
               // <editor-fold defaultstate="collapsed" desc="Realizamos el primero movimiento de inventarios para ajustar existencia">
               //SOLO hay otro movimiento en caso de traspaso
               String strRes = this.AjustaExistencia(this.movProd, deta, lstMovsAdi);
               if (!strRes.equals("OK")) {
                  this.strResultLast = strRes;
                  break;
               } else {
                  String strRes2 = deta.Agrega(oConn);
                  //Validamos si todo fue satisfactorio
                  if (!strRes2.equals("OK")) {
                     this.strResultLast = strRes2;
                  } else {
                     log.debug("Revisamos si hubo movimientos adicionales:" + lstMovsAdi.size());
                     //Revisamos si hubo movimientos adicionales
                     //(SOLO en caso de que saquemos la existencia de masde 1 lote)
                     if (!lstMovsAdi.isEmpty()) {
                        Iterator<vta_movproddeta> it2 = lstMovsAdi.iterator();
                        while (it2.hasNext()) {
                           vta_movproddeta deta2 = it2.next();
                           String strRes3 = deta2.Agrega(oConn);
                           //Validamos si todo fue satisfactorio
                           if (!strRes3.equals("OK")) {
                              this.strResultLast = strRes3;
                           }
                        }
                     }
                  }
               }
               // </editor-fold>
               //Si no hubo errores proseguimos
               if (this.strResultLast.equals("OK")) {

                  // <editor-fold defaultstate="collapsed" desc="Realizamos las operaciones especiales dependiendo del tipo de movimiento">
                  switch (intTipoOperacion) {
                     case Inventario.TRASPASO://TRASPASO

                        // <editor-fold defaultstate="collapsed" desc="Traspaso">
                        double dblTmpSalidas = deta.getFieldDouble("MPD_SALIDAS");
                        double dblTmpEntradas = deta.getFieldDouble("MPD_ENTRADAS");
                        int intNEWPR_ID = 0;
                        //Buscamos EL PR_ID del almacen de entrada
                        String strSQLProd = "SELECT PR_ID from vta_producto where PR_CODIGO = '" + deta.getFieldString("PR_CODIGO") + "' "
                                + " AND SC_ID = '" + this.movProd.getFieldInt("SC_ID2") + "'";
                        try {
                           ResultSet rs = oConn.runQuery(strSQLProd, true);
                           while (rs.next()) {
                              intNEWPR_ID = rs.getInt("PR_ID");
                           }
                           rs.close();
                        } catch (SQLException ex) {
                           this.strResultLast = "ERROR:" + ex.getMessage();
                        }
                        deta.setFieldInt("PR_ID", intNEWPR_ID);
                        deta.setFieldInt("SC_ID", this.movProd.getFieldInt("SC_ID2"));
                        //Invertimos el movimiento para que las salidas se conviertan en entradas en el otro almacen
                        deta.setFieldDouble("MPD_ENTRADAS", dblTmpSalidas);
                        deta.setFieldDouble("MPD_SALIDAS", dblTmpEntradas);
                       
                        
                        
                        strRes = this.AjustaExistencia(this.movProd, deta, lstMovsAdi);
                        if (!strRes.equals("OK")) {
                           this.strResultLast = strRes;
                        } else {
                           String strRes2 = deta.Agrega(oConn);
                           //Validamos si todo fue satisfactorio
                           if (!strRes2.equals("OK")) {
                              this.strResultLast = strRes2;
                           }
                        }
                        //Hubos movimientos adicionales
                        log.debug("Revisamos si hubo movimientos adicionales para ajustar traspaso " + lstMovsAdi.size());
                        if (!lstMovsAdi.isEmpty()) {
                           Iterator<vta_movproddeta> it2 = lstMovsAdi.iterator();
                           while (it2.hasNext()) {
                              vta_movproddeta deta2 = it2.next();
                              dblTmpSalidas = deta2.getFieldDouble("MPD_SALIDAS");
                              dblTmpEntradas = deta2.getFieldDouble("MPD_ENTRADAS");
                              intNEWPR_ID = 0;
                              //Buscamos EL PR_ID del almacen de entrada
                              strSQLProd = "SELECT PR_ID from vta_producto where PR_CODIGO = '" + deta2.getFieldString("PR_CODIGO") + "' "
                                      + " AND SC_ID = '" + this.movProd.getFieldInt("SC_ID2") + "'";
                              try {
                                 ResultSet rs = oConn.runQuery(strSQLProd, true);
                                 while (rs.next()) {
                                    intNEWPR_ID = rs.getInt("PR_ID");
                                 }
                                 rs.close();
                              } catch (SQLException ex) {
                                 this.strResultLast = "ERROR:" + ex.getMessage();
                              }
                              deta2.setFieldInt("PR_ID", intNEWPR_ID);
                              deta2.setFieldInt("SC_ID", this.movProd.getFieldInt("SC_ID2"));
                              //Invertimos el movimiento para que las salidas se conviertan en entradas en el otro almacen
                              deta2.setFieldDouble("MPD_ENTRADAS", dblTmpSalidas);
                              deta2.setFieldDouble("MPD_SALIDAS", dblTmpEntradas);
                              
                              
                              strRes = this.AjustaExistencia(this.movProd, deta2, lstMovsAdi);
                              if (!strRes.equals("OK")) {
                                 this.strResultLast = strRes;
                              } else {
                                 String strRes2 = deta2.Agrega(oConn);
                                 //Validamos si todo fue satisfactorio
                                 if (!strRes2.equals("OK")) {
                                    this.strResultLast = strRes2;
                                 }
                              }
                           }
                        }
                        break;
                     // </editor-fold>
                     //Traspaso de entrada
                     case Inventario.TRASPASO_ENTRADA:
                        lstMovsAdi.clear();
                        // <editor-fold defaultstate="collapsed" desc="Traspaso de entrada">
                        //Sumamos la cantidad de piezas de la salida origen
                        double dblSalidasTraspaso = 0;
                        strSQLProd = "SELECT SUM(MPD_SALIDAS)  AS TSALIDA "
                                + " from vta_movproddeta "
                                + " where MP_ID = " + intNumIdTraspasoSalida;
                        try {
                           ResultSet rs = oConn.runQuery(strSQLProd, true);
                           while (rs.next()) {
                              dblSalidasTraspaso = rs.getDouble("TSALIDA");
                           }
                           rs.close();
                        } catch (SQLException ex) {
                           this.strResultLast = "ERROR:" + ex.getMessage();
                        }
                        //Actualizamos el traspaso de SALIDA
                        double dblDiff = dblSalidasTraspaso - dblTmpEntradasX;
                        String strUpdate = "Update vta_movprod set "
                                + " MP_RECIBIDO = 1, "
                                + " MP_IDRECEP = " + this.movProd.getValorKey() + ", "
                                + " MP_FECHARECEP = '" + this.fecha.getFechaActual() + "', "
                                + " MP_HORARECEP = '" + this.fecha.getHoraActual() + "', "
                                + " MP_CANT_RECEP = " + dblTmpEntradasX + ", "
                                + " MP_CANT_FALT = " + dblDiff + " "
                                + " WHERE MP_ID = " + intNumIdTraspasoSalida;
                        oConn.runQueryLMD(strUpdate);
                        break;
                     // </editor-fold>
                  }
                  // </editor-fold>
               }
            }
            // <editor-fold defaultstate="collapsed" desc="SI TODO RESULTO OK HACEMOS LA PETICION AL WEB SERVCIE PARA GENERAR LA POLIZA CONTABLE">
            if (this.strResultLast.equals("OK")) {
               //Actualizamos la poliza contable
               PolizasContables poli = new PolizasContables(this.oConn, this.varSesiones, this.request);
               poli.setStrOper("NEW");
               poli.callRemote(this.movProd.getFieldInt("MP_ID"), PolizasContables.INVENTARIO);
            }
            // </editor-fold>
            this.saveBitacora("INVENTARIO", "NUEVA", intId);
         } else {
            this.strResultLast = strRes1;
         }
         // <editor-fold defaultstate="collapsed" desc="Terminamos la operacion">
         if (this.bolTransaccionalidad) {
            if (this.strResultLast.equals("OK")) {
               this.oConn.runQueryLMD("COMMIT");
            } else {
               this.oConn.runQueryLMD("ROLLBACK");
            }
         }
         // </editor-fold>
      }
      // </editor-fold>
   }

   /**
    * Realiza el ajuste de existencias del producto
    */
   private String AjustaExistencia(vta_movprod movProd, vta_movproddeta deta,
           ArrayList<vta_movproddeta> lstMovsAdi) {
      String strRes = "OK";
      //Es el numero de posiciones maximo que tendran los num de serie genericos
      int intPos = 15;
      double dblEntrada = deta.getFieldDouble("MPD_ENTRADAS");
      double dblSalida = deta.getFieldDouble("MPD_SALIDAS");
      if (deta.getFieldString("PL_NUMLOTE").equals("null")) {
         deta.setFieldString("PL_NUMLOTE", "");
      }
      //VALIDAMOS si es una ENTRADA o SALIDA (no puede haber ambos al mismo tiempo)
      if (dblEntrada == dblSalida) {
         log.debug("ERROR: NO PUEDE HABER ENTRADA Y SALIDA EN EL MISMO ITEM " + deta.getFieldString("PR_CODIGO") + " " + dblEntrada);
         strRes = "ERROR: NO PUEDE HABER ENTRADA Y SALIDA EN EL MISMO ITEM " + deta.getFieldString("PR_CODIGO") + " " + dblEntrada;
      } else {
         //Validamos si es una entrada o salida
         if (dblEntrada > 0) {
            // <editor-fold defaultstate="collapsed" desc="SI ES UNA ENTRADA">
            // <editor-fold defaultstate="collapsed" desc="Validamos si el numero de lote esta vacio entonces generamos un numero de lote en automatico">
            int intAuto = 0;
            if (deta.getFieldString("PL_NUMLOTE").equals("")) {
               String strSql = "SELECT MAX(PL_NUMLOTE) AS MXLOTE FROM vta_prodlote "
                       + " where PR_ID='" + deta.getFieldInt("PR_ID") + "' AND PL_NUMLOTE REGEXP '^[0-9]+$' "
                       + " AND PL_AUTO = 1 ";
               try {
                  ResultSet rs = oConn.runQuery(strSql, true);
                  while (rs.next()) {
                     String strMaxLote = rs.getString("MXLOTE");
                     int intConsec = 1;
                     String strNewNumSerie = "";
                     if (strMaxLote != null) {
                        intConsec = Integer.valueOf(strMaxLote);
                        intConsec++;
                     }
                     //Acompletamos con ceros el numero de serie
                     for (int iL = 1; iL <= intPos; iL++) {
                        strNewNumSerie += "0";
                     }
                     strNewNumSerie += intConsec;
                     strNewNumSerie = strNewNumSerie.substring(strNewNumSerie.length() - intPos, strNewNumSerie.length());
                     intAuto = 1;
                     deta.setFieldString("PL_NUMLOTE", strNewNumSerie);//Definimos el nuevo numero de lote
                  }
                  rs.close();
               } catch (SQLException ex) {
                  this.strResultLast = "ERROR:" + ex.getMessage();
                  log.error(ex.getMessage());
               }
            }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Validamos si el lote existe">
            int intPL_Id = 0;
            double dblExistencia = 0.0;
            String strSql = "SELECT PL_ID,PL_EXISTENCIA FROM vta_prodlote "
                    + " where PR_ID='" + deta.getFieldInt("PR_ID") + "' "
                    + " AND PL_NUMLOTE ='" + deta.getFieldString("PL_NUMLOTE") + "' ";
            try {
               ResultSet rs = oConn.runQuery(strSql, true);
               while (rs.next()) {
                  intPL_Id = rs.getInt("PL_ID");
                  dblExistencia = rs.getDouble("PL_EXISTENCIA");
               }
               rs.close();
            } catch (SQLException ex) {
               this.strResultLast = "ERROR:" + ex.getMessage();
               log.error(ex.getMessage());
            }
            // </editor-fold>
            //Si existe el lote hacemos un update a la existencia
            if (intPL_Id != 0) {
               dblExistencia += dblEntrada;
               //Actualizamos la existencia de la entrada
               String strUpdate = "UPDATE vta_prodlote "
                       + "SET PL_EXISTENCIA = " + dblExistencia + " "
                       + "WHERE PL_ID = " + intPL_Id;
               oConn.runQueryLMD(strUpdate);
            } else {
               // <editor-fold defaultstate="collapsed" desc="Si no existe el lote insertamos un nuevo lote">
               vta_prodlote Lote = new vta_prodlote();
               Lote.setFieldString("PL_NUMLOTE", deta.getFieldString("PL_NUMLOTE"));
               Lote.setFieldString("PL_ORIGEN", movProd.getFieldString("MP_ORIGENLOTE"));
               Lote.setFieldString("PL_FECHA", movProd.getFieldString("MP_FECHALOTE"));
               Lote.setFieldString("PL_PEDIMENTO", movProd.getFieldString("PL_NUMLOTE"));
               Lote.setFieldString("PL_CADFECHA", movProd.getFieldString("MP_CADFECHA"));
               Lote.setFieldInt("PR_ID", deta.getFieldInt("PR_ID"));
               Lote.setFieldDouble("PL_EXISTENCIA", dblEntrada);
               Lote.setFieldInt("SC_ID", deta.getFieldInt("SC_ID"));
               Lote.setFieldInt("PL_AUTO", intAuto);
               Lote.setFieldDouble("PL_COSTO", deta.getFieldDouble("MPD_COSTO"));
              
               Lote.setFieldString("PL_CADFECHA", deta.getFieldString("MPD_CADFECHA"));
               
//               Lote.setFieldString("PL_CADFECHA", deta.getFieldString("MPD_CADFECHA"));
               String strRes1 = Lote.Agrega(oConn);
               if (!strRes1.equals("OK")) {
                  strRes = strRes1;
               }
               // </editor-fold>
            }
            //Actualizamos la existencia global del producto en la sucursal
            if (strRes.equals("OK")) {
               //Validamos el sistema de costos para actualizar el costo global
               // <editor-fold defaultstate="collapsed" desc="UEPS">
               if (this.intTipoCosteo == Inventario.INV_UEPS) {
                  //Actualizamos la existencia de la entrada
                  String strUpdate = "UPDATE vta_producto "
                          + "SET PR_EXISTENCIA = PR_EXISTENCIA + " + dblEntrada + " "
                          + ",PR_COSTOUEPS = " + deta.getFieldDouble("MPD_COSTO") + " "
                          + ",PR_COSTOREPOSICION = " + deta.getFieldDouble("MPD_COSTO") + " "
                          + "WHERE PR_ID = " + deta.getFieldInt("PR_ID");
                  oConn.runQueryLMD(strUpdate);
               }
               // </editor-fold>
               // <editor-fold defaultstate="collapsed" desc="PEPS">
               if (this.intTipoCosteo == Inventario.INV_PEPS) {
                  //Actualizamos la existencia de la entrada
                  String strUpdate = "UPDATE vta_producto "
                          + "SET PR_EXISTENCIA = PR_EXISTENCIA + " + dblEntrada + " "
                          + ",PR_COSTOPEPS = " + deta.getFieldDouble("MPD_COSTO") + " "
                          + ",PR_COSTOREPOSICION = " + deta.getFieldDouble("MPD_COSTO") + " "
                          + "WHERE PR_ID = " + deta.getFieldInt("PR_ID");
                  oConn.runQueryLMD(strUpdate);
               }
               // </editor-fold>
               // <editor-fold defaultstate="collapsed" desc="PROMEDIO">
               if (this.intTipoCosteo == Inventario.INV_PROMEDIO) {
//                  String strUpdate = "UPDATE vta_producto "
//                          + "SET PR_COSTOREPOSICION = " + deta.getFieldDouble("MPD_COSTO") + " "
//                          + "WHERE PR_ID = " + deta.getFieldInt("PR_ID");
//                  oConn.runQueryLMD(strUpdate);
                  String strResulProm = doCalculaPromedio(deta.getFieldInt("PR_ID"),
                          deta.getFieldDouble("MPD_COSTO"), dblEntrada,
                          false, false, 0);
                  if (!strResulProm.equals("OK")) {
                     //Hubo un error desencadenamos arbol de errores
                     strRes = strResulProm;
                  }
               }
               // </editor-fold>

            }
            // </editor-fold>
         } else {
            // <editor-fold defaultstate="collapsed" desc="SI ES UNA SALIDA">
            // <editor-fold defaultstate="collapsed" desc="Revisamos si el producto requiere existencia para su venta">
            //En caso de que no podemos hacer movimientos en negativo
            int intReqExist = 0;
            int intPR_USO_NOSERIE = 0;
            String strSQLProd = "SELECT PR_REQEXIST,PR_USO_NOSERIE from vta_producto "
                    + "where PR_ID = '" + deta.getFieldInt("PR_ID") + "'";
            try {
               ResultSet rs = oConn.runQuery(strSQLProd, true);
               while (rs.next()) {
                  intReqExist = rs.getInt("PR_REQEXIST");
                  intPR_USO_NOSERIE = rs.getInt("PR_USO_NOSERIE");
               }
               rs.close();
            } catch (SQLException ex) {
               strRes = "ERROR:" + ex.getMessage();
               log.error(ex.getMessage());
            }
            // </editor-fold>
            //Validamos si el numero de lote esta vacio entonces
            //tomamos los items de los lotes disponibles
            if (deta.getFieldString("PL_NUMLOTE").equals("")) {
               //Consultamos todos
               int intPL_Id = 0;
               double dblSalidaAplicar = dblSalida;
               log.debug("Ciclo..." + dblSalidaAplicar);
               int intCiclos = 0;
               double dblCostoPromedio = 0;
               String strTipoCosteo = "";
               // <editor-fold defaultstate="collapsed" desc="Validamos el tipo de busqueda en base al tipo de costeo">
               if (this.intTipoCosteo == Inventario.INV_PEPS) {
                  strTipoCosteo = " PL_FECHA,PL_ID DESC";
               }
               if (this.intTipoCosteo == Inventario.INV_UEPS) {
                  strTipoCosteo = " PL_FECHA,PL_ID ASC";
               }
               if (this.intTipoCosteo == Inventario.INV_PROMEDIO) {
                  strTipoCosteo = " PL_FECHA,PL_ID ASC";
                  // <editor-fold defaultstate="collapsed" desc="Obtenemos el costo promedio">
                  String strSQLProd2 = "SELECT PR_EXISTENCIA,PR_COSTOPROM from vta_producto "
                          + "where PR_ID = '" + deta.getFieldInt("PR_ID") + "'";
                  try {
                     ResultSet rs = oConn.runQuery(strSQLProd2, true);
                     while (rs.next()) {
                        dblCostoPromedio = rs.getDouble("PR_COSTOPROM");
                     }
                     rs.close();
                  } catch (SQLException ex) {
                     strRes = "ERROR:" + ex.getMessage();
                     System.out.println("Error en salida y costo promedio " + ex.getMessage());
                  }
                  // </editor-fold>
               }
               // </editor-fold>

               // <editor-fold defaultstate="collapsed" desc="Aplicamos la existencia">
               String strSql = "SELECT PL_ID,PL_EXISTENCIA,PL_NUMLOTE,PL_COSTO FROM vta_prodlote "
                       + " where PR_ID='" + deta.getFieldInt("PR_ID") + "' "
                       + " AND PL_EXISTENCIA >0 ORDER BY  " + strTipoCosteo;
               try {
                  ResultSet rs = oConn.runQuery(strSql, true);
                  while (rs.next()) {
                     intCiclos++;
                     intPL_Id = rs.getInt("PL_ID");
                     double dblExistencia = rs.getDouble("PL_EXISTENCIA");
                     String strNumLote = rs.getString("PL_NUMLOTE");
                     double dblSalidaAplica = 0;
                     if (dblSalidaAplicar > dblExistencia) {
                        dblSalidaAplica = dblExistencia;
                        dblSalidaAplicar = dblSalidaAplicar - dblSalidaAplica;
                     } else {
                        dblSalidaAplica = dblSalidaAplicar;
                        dblSalidaAplicar = 0;
                     }
                     log.debug("Ciclo..." + intCiclos + " " + strNumLote + " " + dblSalidaAplica + " x aplicar " + dblSalidaAplicar);
                     if (intCiclos > 1) {
                        // <editor-fold defaultstate="collapsed" desc="Copiamos el item en el arreglo temporal ya que hay que generar mas partidas">
                        if (dblSalidaAplica > 0) {
                           vta_movproddeta copy = Copiamov(deta);
                           copy.setFieldString("PL_NUMLOTE", strNumLote);
                           if (this.intTipoCosteo == Inventario.INV_PROMEDIO) {
                              copy.setFieldDouble("MPD_COSTO", dblCostoPromedio);
                           } else {
                              copy.setFieldDouble("MPD_COSTO", rs.getDouble("PL_COSTO"));
                           }
                           copy.setFieldDouble("MPD_SALIDAS", dblSalidaAplica);
                           log.debug("Copia Item..." + strNumLote + " " + dblSalidaAplica);
                           lstMovsAdi.add(copy);
                        }
                        // </editor-fold>
                     } else {
                        // <editor-fold defaultstate="collapsed" desc="Definimos la cantidad que esta saliendo">
                        deta.setFieldString("PL_NUMLOTE", strNumLote);
                        if (this.intTipoCosteo == Inventario.INV_PROMEDIO) {
                           deta.setFieldDouble("MPD_COSTO", dblCostoPromedio);
                        } else {
                           deta.setFieldDouble("MPD_COSTO", rs.getDouble("PL_COSTO"));
                        }
                        deta.setFieldDouble("MPD_SALIDAS", dblSalidaAplica);
                        // </editor-fold>
                     }
                     // <editor-fold defaultstate="collapsed" desc="Actualizamos la existencia">
                     if (strRes.equals("OK")) {
                        dblExistencia -= dblSalidaAplica;
                        //Actualizamos la existencia de la salida
                        String strUpdate = "UPDATE vta_prodlote "
                                + "SET PL_EXISTENCIA = " + dblExistencia + " "
                                + "WHERE PL_ID = " + intPL_Id;
                        oConn.runQueryLMD(strUpdate);
                        //Actualizamos la existencia de la salida
                        strUpdate = "UPDATE vta_producto "
                                + "SET PR_EXISTENCIA = PR_EXISTENCIA - " + dblSalidaAplica + " "
                                + "WHERE PR_ID = " + deta.getFieldInt("PR_ID");
                        oConn.runQueryLMD(strUpdate);
                     }
                     // </editor-fold>
                  }
                  rs.close();
               } catch (SQLException ex) {
                  strRes = "ERROR:" + ex.getMessage();
                  log.error(ex.getMessage());
               }
               // </editor-fold>
               //Si aun queda una cantidad por salir marcamos error
               if (dblSalidaAplicar > 0) {
                  log.debug("Aun queda pendiente por surtir");
                  if (intReqExist == 1) {
                     strRes = "ERROR:NO HAY SUFICIENTE EXISTENCIA PARA EL PRODUCTO" + deta.getFieldString("PR_CODIGO");
                  } else {
                     // <editor-fold defaultstate="collapsed" desc="Hacemos la salida del primer lote que encontremos aunque este vacio">
                     //Aplicamos la existencia
                     strSql = "SELECT PL_ID,PL_EXISTENCIA,PL_NUMLOTE,PL_COSTO FROM vta_prodlote "
                             + " where PR_ID='" + deta.getFieldInt("PR_ID") + "' "
                             + " AND PL_EXISTENCIA = 0 ORDER BY  " + strTipoCosteo;
                     try {
                        ResultSet rs = oConn.runQuery(strSql, true);
                        while (rs.next()) {
                           intCiclos++;
                           intPL_Id = rs.getInt("PL_ID");
                           double dblExistencia = rs.getDouble("PL_EXISTENCIA");
                           String strNumLote = rs.getString("PL_NUMLOTE");
                           double dblSalidaAplica = 0;
                           dblSalidaAplica = dblSalidaAplicar;
                           dblSalidaAplicar = 0;
                           log.debug("Ciclo(B)..." + intCiclos + " " + strNumLote + " " + dblSalidaAplica);
                           // <editor-fold defaultstate="collapsed" desc="Evaluamos si tenemos mas de un ciclo para copiar el item">
                           if (intCiclos > 1) {
                              //Copiamos el item en el arreglo temporal ya que hay que generar mas partidas
                              vta_movproddeta copy = Copiamov(deta);
                              copy.setFieldString("PL_NUMLOTE", strNumLote);
                              if (this.intTipoCosteo == Inventario.INV_PROMEDIO) {
                                 copy.setFieldDouble("MPD_COSTO", dblCostoPromedio);
                              } else {
                                 copy.setFieldDouble("MPD_COSTO", rs.getDouble("PL_COSTO"));
                              }
                              copy.setFieldDouble("MPD_SALIDAS", dblSalidaAplica);
                              log.debug("Copia Item..." + strNumLote + " " + dblSalidaAplica);
                              lstMovsAdi.add(copy);
                           } else {
                              deta.setFieldString("PL_NUMLOTE", strNumLote);
                              deta.setFieldDouble("MPD_COSTO", rs.getDouble("PL_COSTO"));
                           }
                           // </editor-fold>
                           // <editor-fold defaultstate="collapsed" desc="Actualizamos la existencia">
                           if (strRes.equals("OK")) {
                              dblExistencia -= dblSalidaAplica;
                              //Actualizamos la existencia de la salida
                              String strUpdate = "UPDATE vta_prodlote "
                                      + "SET PL_EXISTENCIA = " + dblExistencia + " "
                                      + "WHERE PL_ID = " + intPL_Id;
                              oConn.runQueryLMD(strUpdate);
                              //Actualizamos la existencia de la salida
                              strUpdate = "UPDATE vta_producto "
                                      + "SET PR_EXISTENCIA = PR_EXISTENCIA - " + dblSalidaAplica + " "
                                      + "WHERE PR_ID = " + deta.getFieldInt("PR_ID");
                              oConn.runQueryLMD(strUpdate);
                           }
                           // </editor-fold>
                           //Si ya se surtio todo cerramos.
                           if (dblSalidaAplicar == 0) {
                              break;
                           }
                        }
                        rs.close();
                     } catch (SQLException ex) {
                        strRes = "ERROR:" + ex.getMessage();
                        log.error(ex.getMessage());
                     }
                     // </editor-fold>
                     // <editor-fold defaultstate="collapsed" desc="Si aun queda cantidad por salir es que no tiene existencias"> 
                     //y estamos surtiendo sin existencias así que sacamos en negativo
                     if (dblSalidaAplicar > 0) {
                        double dblSalidaAplica = dblSalidaAplicar;
                        //Actualizamos la existencia de la salida
                        String strUpdate = "UPDATE vta_producto "
                                + "SET PR_EXISTENCIA = PR_EXISTENCIA - " + dblSalidaAplica + " "
                                + "WHERE PR_ID = " + deta.getFieldInt("PR_ID");
                        oConn.runQueryLMD(strUpdate);
                     }
                     // </editor-fold>
                  }
               }
            } else {

               // <editor-fold defaultstate="collapsed" desc="Hay lote definido">
               int intPL_Id = 0;
               double dblExistencia = 0.0;
               String strSql = "SELECT PL_ID,PL_EXISTENCIA,PL_COSTO FROM vta_prodlote "
                       + " where PR_ID='" + deta.getFieldInt("PR_ID") + "' "
                       + " AND PL_NUMLOTE ='" + deta.getFieldString("PL_NUMLOTE") + "' ";
               try {
                  ResultSet rs = oConn.runQuery(strSql, true);
                  while (rs.next()) {
                     intPL_Id = rs.getInt("PL_ID");
                     dblExistencia = rs.getDouble("PL_EXISTENCIA");
                     deta.setFieldDouble("MPD_COSTO", rs.getDouble("PL_COSTO"));
                  }
                  rs.close();
               } catch (SQLException ex) {
                  strRes = "ERROR:" + ex.getMessage();
                  log.error(ex.getMessage());
               }
               if (intPL_Id != 0) {
                  //Validamos si el lote existe
                  //Si el lote existe tomamos la existencia de ese lote validando que no dejemos en negativo el lote
                  //en caso de que requiera existencia para su venta
                  boolean bolBuscarMasLotes = false;
                  double dblSalidasRest = 0;
                  // <editor-fold defaultstate="collapsed" desc="Validamos si requiere existencias">
                  if (intReqExist == 1) {
                     if (dblSalida > dblExistencia) {
                        log.debug("No hay suficiente existencia en el lote original " + intPL_Id + " producto " + deta.getFieldInt("PR_ID"));
                        //Validamos si se controla por lote sino tomamos el siguiente lote disponible
                        if (intPR_USO_NOSERIE == 0) {
                           //Sacamos de otros lotes
                           dblSalidasRest = dblSalida - dblExistencia;
                           dblSalida = dblExistencia;
                           bolBuscarMasLotes = true;
                           log.debug(" No usa serie ajustamos con otro lote " + dblSalidasRest);
                        } else {
                           strRes = "ERROR:LA SALIDA ES MAYOR A LA EXISTENCIA DEL PRODUCTO " + deta.getFieldString("PR_CODIGO");
                        }
                     }
                  }
                  // </editor-fold>
                  // <editor-fold defaultstate="collapsed" desc="Ajustamos las existencias de lote original">
                  if (strRes.equals("OK")) {
                     dblExistencia -= dblSalida;
                     //Actualizamos la existencia de la salida
                     String strUpdate = "UPDATE vta_prodlote "
                             + "SET PL_EXISTENCIA = " + dblExistencia + " "
                             + "WHERE PL_ID = " + intPL_Id;
                     oConn.runQueryLMD(strUpdate);
                     //Actualizamos la existencia de la salida
                     strUpdate = "UPDATE vta_producto "
                             + "SET PR_EXISTENCIA = PR_EXISTENCIA - " + dblSalida + " "
                             + "WHERE PR_ID = " + deta.getFieldInt("PR_ID");
                     oConn.runQueryLMD(strUpdate);
                  }
                  // </editor-fold>
                  // <editor-fold defaultstate="collapsed" desc="Buscamos descargar de otros lotes">
                  if (bolBuscarMasLotes) {
                     log.debug("Hay ajustes en lotes adicionales " + dblSalidasRest);
                     //Consultamos todos
                     intPL_Id = 0;
                     double dblSalidaAplicar = dblSalidasRest;
                     int intCiclos = 1;
                     double dblCostoPromedio = 0;
                     String strTipoCosteo = "";
                     // <editor-fold defaultstate="collapsed" desc="Validamos el tipo de busqueda en base al tipo de costeo">
                     if (this.intTipoCosteo == Inventario.INV_PEPS) {
                        strTipoCosteo = " PL_FECHA,PL_ID DESC";
                     }
                     if (this.intTipoCosteo == Inventario.INV_UEPS) {
                        strTipoCosteo = " PL_FECHA,PL_ID ASC";
                     }
                     if (this.intTipoCosteo == Inventario.INV_PROMEDIO) {
                        strTipoCosteo = " PL_FECHA,PL_ID ASC";
                        // <editor-fold defaultstate="collapsed" desc="Obtenemos el costo promedio">
                        String strSQLProd2 = "SELECT PR_EXISTENCIA,PR_COSTOPROM from vta_producto "
                                + "where PR_ID = '" + deta.getFieldInt("PR_ID") + "'";
                        try {
                           ResultSet rs = oConn.runQuery(strSQLProd2, true);
                           while (rs.next()) {
                              dblCostoPromedio = rs.getDouble("PR_COSTOPROM");
                           }
                           rs.close();
                        } catch (SQLException ex) {
                           strRes = "ERROR:" + ex.getMessage();
                           System.out.println("Error en salida y costo promedio " + ex.getMessage());
                        }
                        // </editor-fold>
                     }
                     // </editor-fold>

                     // <editor-fold defaultstate="collapsed" desc="Aplicamos la existencia">
                     strSql = "SELECT PL_ID,PL_EXISTENCIA,PL_NUMLOTE,PL_COSTO FROM vta_prodlote "
                             + " where PR_ID='" + deta.getFieldInt("PR_ID") + "' "
                             + " AND PL_EXISTENCIA >0 ORDER BY  " + strTipoCosteo;
                     try {
                        ResultSet rs = oConn.runQuery(strSql, true);
                        while (rs.next()) {
                           intCiclos++;
                           intPL_Id = rs.getInt("PL_ID");
                           double dblExistenciaP = rs.getDouble("PL_EXISTENCIA");
                           String strNumLote = rs.getString("PL_NUMLOTE");
                           double dblSalidaAplica = 0;
                           if (dblSalidaAplicar > dblExistenciaP) {
                              dblSalidaAplica = dblExistenciaP;
                              dblSalidaAplicar = dblSalidaAplicar - dblSalidaAplica;
                           } else {
                              dblSalidaAplica = dblSalidaAplicar;
                              dblSalidaAplicar = 0;
                           }
                           log.debug("Ciclo..." + intCiclos + " " + strNumLote + " " + dblSalidaAplica);
                           // <editor-fold defaultstate="collapsed" desc="Copiamos el item en el arreglo temporal ya que hay que generar mas partidas">
                           if (dblSalidaAplica > 0) {
                              vta_movproddeta copy = Copiamov(deta);
                              copy.setFieldString("PL_NUMLOTE", strNumLote);
                              if (this.intTipoCosteo == Inventario.INV_PROMEDIO) {
                                 copy.setFieldDouble("MPD_COSTO", dblCostoPromedio);
                              } else {
                                 copy.setFieldDouble("MPD_COSTO", rs.getDouble("PL_COSTO"));
                              }
                              copy.setFieldDouble("MPD_SALIDAS", dblSalidaAplica);
                              log.debug("Copia Item..." + strNumLote + " " + dblSalidaAplica);
                              lstMovsAdi.add(copy);
                           }
                           // </editor-fold>

                           // <editor-fold defaultstate="collapsed" desc="Actualizamos la existencia">
                           if (strRes.equals("OK")) {
                              dblExistenciaP -= dblSalidaAplica;
                              //Actualizamos la existencia de la salida
                              String strUpdate = "UPDATE vta_prodlote "
                                      + "SET PL_EXISTENCIA = " + dblExistenciaP + " "
                                      + "WHERE PL_ID = " + intPL_Id;
                              oConn.runQueryLMD(strUpdate);
                              //Actualizamos la existencia de la salida
                              strUpdate = "UPDATE vta_producto "
                                      + "SET PR_EXISTENCIA = PR_EXISTENCIA - " + dblSalidaAplica + " "
                                      + "WHERE PR_ID = " + deta.getFieldInt("PR_ID");
                              oConn.runQueryLMD(strUpdate);
                           }
                           // </editor-fold>
                        }
                        rs.close();
                     } catch (SQLException ex) {
                        strRes = "ERROR:" + ex.getMessage();
                        log.error(ex.getMessage());
                     }
                     // </editor-fold>
                     //Si aun queda una cantidad por salir marcamos error
                     log.debug("dblSalidaAplicar " + dblSalidaAplicar);
                     if (dblSalidaAplicar > 0) {
                        log.debug("Aun queda pendiente por surtir");
                        strRes = "ERROR:NO HAY SUFICIENTE EXISTENCIA PARA EL PRODUCTO" + deta.getFieldString("PR_CODIGO");
                     }
                  }
                  // </editor-fold>
               } else {
                  //Si el lote no existe marcamos error ya que hay una incongruencia
                  strRes = "ERROR:EL LOTE " + deta.getFieldString("PL_NUMLOTE") + " NO EXISTE PARA EL PRODUCTO" + deta.getFieldString("PR_CODIGO");
               }
               // </editor-fold>
            }
            // </editor-fold>
         }
      }
      return strRes;
   }

   /**
    * Copia un movimiento
    */
   private vta_movproddeta Copiamov(vta_movproddeta deta) {
      vta_movproddeta detaCopy = new vta_movproddeta();
      detaCopy.setFieldString("PL_NUMLOTE", new String(deta.getFieldString("PL_NUMLOTE")));
      detaCopy.setFieldString("MPD_FECHA", new String(deta.getFieldString("MPD_FECHA")));
      detaCopy.setFieldString("PR_CODIGO", new String(deta.getFieldString("PR_CODIGO")));
      detaCopy.setFieldInt("PR_ID", deta.getFieldInt("PR_ID"));
      detaCopy.setFieldInt("ID_USUARIOS", deta.getFieldInt("ID_USUARIOS"));
      detaCopy.setFieldInt("SC_ID", deta.getFieldInt("SC_ID"));
      detaCopy.setFieldInt("MPD_IDORIGEN", deta.getFieldInt("MPD_IDORIGEN"));
      detaCopy.setFieldDouble("MPD_ENTRADAS", deta.getFieldDouble("MPD_ENTRADAS"));
      detaCopy.setFieldDouble("MPD_SALIDAS", deta.getFieldDouble("MPD_SALIDAS"));
      detaCopy.setFieldDouble("MPD_COSTO", deta.getFieldDouble("MPD_COSTO"));

      detaCopy.setFieldDouble("MPD_COSTO_PRORRATEO", deta.getFieldDouble("MPD_COSTO_PRORRATEO"));
      detaCopy.setFieldDouble("MPD_PROPOR_PRORRATEO", deta.getFieldDouble("MPD_PROPOR_PRORRATEO"));
      detaCopy.setFieldDouble("MPD_CANT_CONF", deta.getFieldDouble("MPD_CANT_CONF"));
      detaCopy.setFieldDouble("MPD_PARIDAD", deta.getFieldDouble("MPD_PARIDAD"));
      detaCopy.setFieldString("MPD_NOTAS", new String(deta.getFieldString("MPD_NOTAS")));
      detaCopy.setFieldInt("CXP_ID", deta.getFieldInt("CXP_ID"));
      detaCopy.setFieldInt("MON_ID", deta.getFieldInt("MON_ID"));
      detaCopy.setFieldInt("MP_ID", deta.getFieldInt("MP_ID"));
      detaCopy.setFieldInt("PR_ID_MASTER", deta.getFieldInt("PR_ID_MASTER"));

      return detaCopy;
   }

   /**
    * Calcula el costo promedio de un producto
    *
    * @param intPr_id Es el id del producto
    * @param dblCostoNuevo Es el costo de la entrada
    * @param dblCantidadNueva Es la cantidad que esta entrando
    * @param dblCostoActual Es el costo promedio actual
    * @param dblCantidadActual Es la cantidad actual
    * @return
    */
   protected double CalculoCostoPromedio(int intPr_id, double dblCostoNuevo, double dblCantidadNueva,
           double dblCostoActual, double dblCantidadActual) {
      double dblInvActual = dblCostoActual * dblCantidadActual;
      double dblInvEntrada = dblCostoNuevo * dblCantidadNueva;
      double dblInvNuevo = dblCantidadActual + dblCantidadNueva;
      double dblCostoTmp = 0;
      /**
       * Importante este ultimo COSNVO lo dejamos sin convertir con Dbl() para
       * que no se le ocurra redondear el COSTO CALCULADO
       */
      if (dblInvNuevo == 0) {
         if (dblCostoActual > 0) {
            dblCostoTmp = dblCostoActual;
         } else {
            dblCostoTmp = dblCostoNuevo;
         }
      } else {
         double dblCalcular = ((dblInvActual + dblInvEntrada) / dblInvNuevo);
         dblCostoTmp = dblCalcular;
      }
      return dblCostoTmp;
   }

   public void doTrxAnul() {
      this.strResultLast = "OK";
      // <editor-fold defaultstate="collapsed" desc="Validamos que todos los campos basico se encuentren">
      if (this.movProd.getFieldInt("MP_ID") == 0) {
         this.strResultLast = "ERROR:Falta definir la operacion por anular";
      }
      //SI ES TRASPASO DE SALIDA VERIFICAMOS QUE NO HALLA ENTRADAS REALIZADAS
      if (this.movProd.getFieldInt("TIN_ID") == 3
              && this.movProd.getFieldInt("MP_RECIBIDO") == 1) {
         this.strResultLast = "ERROR:El traspaso ya tiene realizada una entrada "
                 + "con el numero de movimiento " + this.movProd.getFieldInt("MP_IDRECEP");
      }
      //Evaluamos la fecha de cierre
      boolean bolEvalCierre = evaluaFechaCierre(this.movProd.getFieldString("MP_FECHA"), this.movProd.getFieldInt("EMP_ID"), this.movProd.getFieldInt("SC_ID"));
      if (!bolEvalCierre) {
         this.strResultLast = "ERROR:El periodo al que corresponde el movimiento ya ha sido cerrado";
      }
      // </editor-fold>

      //Validamos si pasamos las validaciones
      if (this.strResultLast.equals("OK")) {
         // <editor-fold defaultstate="collapsed" desc="Validamos que no este anulado">
         if (this.movProd.getFieldInt("MP_ANULADO") == 0) {
            // <editor-fold defaultstate="collapsed" desc="Inicializamos la operacion">
            if (this.bolTransaccionalidad) {
               this.oConn.runQueryLMD("BEGIN");
            }
            // </editor-fold>
            // <editor-fold defaultstate="collapsed" desc="Ajustamos la existencia de los productos">
            vta_movproddeta movTmp = new vta_movproddeta();
            ArrayList<vta_movproddeta> lstMovsAdi = new ArrayList<vta_movproddeta>();//Lista de elementos adicionales
            ArrayList<TableMaster> lstmovsTmp = movTmp.ObtenDatosVarios(" MP_ID = " + this.movProd.getFieldInt("MP_ID"), oConn);
            Iterator<TableMaster> it = lstmovsTmp.iterator();
            while (it.hasNext()) {
               vta_movproddeta deta = (vta_movproddeta) it.next();
               lstMovsAdi.clear();
               //Invertimos los movimientos
               double dblEntradaTmp = deta.getFieldDouble("MPD_ENTRADAS");
               double dblSalidaTmp = deta.getFieldDouble("MPD_SALIDAS");
               deta.setFieldDouble("MPD_SALIDAS", dblEntradaTmp);
               deta.setFieldDouble("MPD_ENTRADAS", dblSalidaTmp);
               //Realizamos el movimiento de inventarios para ajustar existencia
               String strRes = this.AjustaExistencia(this.movProd, deta, lstMovsAdi);
               if (!strRes.equals("OK")) {
                  this.strResultLast = strRes;
                  break;
               } else {
                  //Revisamos si hubo movimientos adicionales para añadirlos
                  if (!lstMovsAdi.isEmpty()) {
                     log.debug("Hubo movimientos adicionales afectando otros lotes....");
//                  Iterator<vta_movproddeta> it2 = lstMovsAdi.iterator();
//                  while (it2.hasNext()) {
//                     vta_movproddeta deta2 = it2.next();
//                     String strRes3 = deta2.Agrega(oConn);
//                     //Validamos si todo fue satisfactorio
//                     if (!strRes3.equals("OK")) {
//                        this.strResultLast = strRes3;
//                     }
//                  }
                  }
               }
            }
            // </editor-fold>
            if (this.strResultLast.equals("OK")) {
               // <editor-fold defaultstate="collapsed" desc="Actualizamos campos documento principal">
               this.movProd.setFieldInt("ID_USUARIOSANUL", this.varSesiones.getIntNoUser());
               this.movProd.setFieldInt("MP_ANULADO", 1);
               this.movProd.setFieldString("MP_HORANUL", this.fecha.getHoraActual());
               if (strFechaAnul.equals("")) {
                  this.movProd.setFieldString("MP_FECHAANUL", this.fecha.getFechaActual());
               } else {
                  this.movProd.setFieldString("MP_FECHAANUL", strFechaAnul);
               }
               // </editor-fold>
               String strResp1 = this.movProd.Modifica(oConn);
               if (!strResp1.equals("OK")) {
                  this.strResultLast = strResp1;
               } else {
                  // <editor-fold defaultstate="collapsed" desc="SI ES TRASPASO DE ENTRADA ACTUALIZAMOS LA ESTADISTICA">
                  if (this.movProd.getFieldInt("TIN_ID") == 4) {
                     String strUpdate = "Update vta_movprod set "
                             + " MP_RECIBIDO = 0, "
                             + " MP_IDRECEP = 0, "
                             + " MP_FECHARECEP = '', "
                             + " MP_HORARECEP = '', "
                             + " MP_CANT_RECEP = 0, "
                             + " MP_CANT_FALT = 0, "
                             + " WHERE MP_ID = " + this.movProd.getFieldInt("MP_IDORIGEN");
                     oConn.runQueryLMD(strUpdate);
                  }
                  // </editor-fold>
                  // <editor-fold defaultstate="collapsed" desc="SI TODO RESULTO OK HACEMOS LA PETICION AL WEB SERVCIE PARA GENERAR LA POLIZA CONTABLE">
                  if (this.strResultLast.equals("OK")) {
                     //Actualizamos la poliza contable
                     PolizasContables poli = new PolizasContables(this.oConn, this.varSesiones, this.request);
                     poli.setStrOper("CANCEL");
                     poli.callRemote(this.movProd.getFieldInt("MP_ID"), PolizasContables.INVENTARIO);
                  }
                  // </editor-fold>
                  this.saveBitacora("INVENTARIO", "ANULAR", this.movProd.getFieldInt("MP_ID"));
               }
            }
            // <editor-fold defaultstate="collapsed" desc="Terminamos la operacion">
            if (this.bolTransaccionalidad) {
               if (this.strResultLast.equals("OK")) {
                  this.oConn.runQueryLMD("COMMIT");
               } else {
                  this.oConn.runQueryLMD("ROLLBACK");
               }
            }
            // </editor-fold>
         } else {
            this.strResultLast = "ERROR:La operacion ya fue anulada";
         }
         // </editor-fold>
      }

   }

   @Override
   public void doTrxRevive() {
      this.strResultLast = "OK";
      //Validamos que todos los campos basico se encuentren
      if (this.movProd.getFieldInt("MP_ID") == 0) {
         this.strResultLast = "ERROR:Falta definir la operacion por revivir";
      }
      //Inicializamos la operacion
      if (this.bolTransaccionalidad) {
         this.oConn.runQueryLMD("BEGIN");
      }
      //Validamos que no este anulado
      if (this.movProd.getFieldInt("MP_ANULADO") != 0) {
         //Ajustamos la existencia de los productos
         vta_movproddeta movTmp = new vta_movproddeta();
         ArrayList<TableMaster> lstmovsTmp = movTmp.ObtenDatosVarios(" MP_ID = " + this.movProd.getFieldInt("MP_ID"), oConn);
         Iterator<TableMaster> it = lstmovsTmp.iterator();
         while (it.hasNext()) {
            vta_movproddeta deta = (vta_movproddeta) it.next();
            //Realizamos el movimiento de inventarios para ajustar existencia
            String strRes = this.AjustaExistencia(this.movProd, deta, null);
            if (!strRes.equals("OK")) {
               this.strResultLast = strRes;
               break;
            }
         }
         if (this.strResultLast.equals("OK")) {
            //Definimos campos
            this.movProd.setFieldInt("ID_USUARIOSANUL", 0);
            this.movProd.setFieldInt("MP_ANULADO", 0);
            this.movProd.setFieldString("MP_FECHAANUL", "");
            this.movProd.setFieldString("MP_HORANUL", "");
            String strResp1 = this.movProd.Modifica(oConn);
            if (!strResp1.equals("OK")) {
               this.strResultLast = strResp1;
            }
            //SI TODO RESULTO OK HACEMOS LA PETICION AL WEB SERVCIE PARA GENERAR LA POLIZA CONTABLE
            if (this.strResultLast.equals("OK")) {
               //Actualizamos la poliza contable
               PolizasContables poli = new PolizasContables(this.oConn, this.varSesiones, this.request);
               poli.setStrOper("REVIVE");
               poli.callRemote(this.movProd.getFieldInt("MP_ID"), PolizasContables.INVENTARIO);
            }
            this.saveBitacora("INVENTARIO", "REVIVE", this.movProd.getFieldInt("MP_ID"));
         }
      } else {
         this.strResultLast = "ERROR:La operacion no esta anulada";
      }
      //Terminamos la operacion
      if (this.bolTransaccionalidad) {
         if (this.strResultLast.equals("OK")) {
            this.oConn.runQueryLMD("COMMIT");
         } else {
            this.oConn.runQueryLMD("ROLLBACK");
         }
      }
   }

   @Override
   public void doTrxSaldo() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   @Override
   public void doTrxMod() {
      /*No pueden realizarse modificaciones a los productos*/
      throw new UnsupportedOperationException("Not supported yet.");
   }

   /**
    * Realizas las siguientes validaciones y ajustes Quita los elementos que no
    * se inventarian Añade el contenido de los paquetes Actualiza la paridad si
    * la moneda es diferente a la default de la sucursal
    */
   public void ajustesPartidasVarios() {
      //HashMap para las monedas
      Map mapMonedas = new HashMap();
      ArrayList<vta_movproddeta> lstMovsTmpPaq = new ArrayList<vta_movproddeta>();
      //Iteramos por los productos
      Iterator<vta_movproddeta> itMon = this.lstMovs.iterator();
      while (itMon.hasNext()) {
         vta_movproddeta deta = itMon.next();
         //Consultamos los productos
         String strSQLProd = "SELECT PR_ID,PR_ESKIT,MON_ID,PR_INVENTARIO from vta_producto "
                 + "where PR_CODIGO = '" + deta.getFieldString("PR_CODIGO") + "' "
                 + " AND SC_ID = '" + this.movProd.getFieldInt("SC_ID") + "'";
         try {
            ResultSet rs = oConn.runQuery(strSQLProd, true);
            while (rs.next()) {
               int intPRD_IDPaq = rs.getInt("PR_ID");
               deta.setFieldInt("MON_ID", rs.getInt("MON_ID"));
               //Validamos si aun no existe la moneda para añadirla en el listado
               if (rs.getInt("MON_ID") != this.intMonedaDefa && !mapMonedas.containsKey(rs.getInt("MON_ID"))) {
                  mapMonedas.put(rs.getInt("MON_ID"), 1);
               }
               //Si no descarga de inventario lo quitamos
               if (rs.getInt("PR_INVENTARIO") == 0) {
                  //this.lstMovs.remove(deta);
                  if(this.bolControlEstrictoInv){
                     this.strResultLast = "ERROR: El producto con codigo " + deta.getFieldString("PR_CODIGO") + " no tiene la bandera para afectar inventarios";
                     break;
                  }
                  deta.setBolMarca(true);
               } else {
                  //Si es kit buscamos sus elementos
                  if (rs.getInt("PR_ESKIT") == 1
                          && intTipoOperacion != Inventario.TRASPASO_ENTRADA) {
                     deta.setIntEsPaquete(1);
                     //Consultamos el contenido del kit
                     strSQLProd = "SELECT PR_ID2,PAQ_CANTIDAD,PAQ_COD from vta_paquetes "
                             + "where PR_ID = '" + intPRD_IDPaq + "'";
                     ResultSet rs2 = oConn.runQuery(strSQLProd, true);
                     while (rs2.next()) {
                        //Nueva partida
                        vta_movproddeta detaPaquete = new vta_movproddeta();
                        detaPaquete.setFieldInt("MP_ID", deta.getFieldInt("MP_ID"));
                        //detaPaquete.setFieldInt("PR_ID", rs2.getInt("PR_ID2"));
                        detaPaquete.setFieldInt("PR_ID_MASTER", intPRD_IDPaq);
                        detaPaquete.setFieldInt("ID_USUARIOS", this.movProd.getFieldInt("ID_USUARIOS"));
                        detaPaquete.setFieldInt("SC_ID", this.movProd.getFieldInt("SC_ID"));
                        detaPaquete.setFieldInt("CXP_ID", this.movProd.getFieldInt("CXP_ID"));
                        detaPaquete.setFieldInt("MPD_IDORIGEN", deta.getFieldInt("MPD_IDORIGEN"));
                        detaPaquete.setFieldString("MPD_FECHA", deta.getFieldString("MPD_FECHA"));
                        detaPaquete.setFieldString("MPD_NOTAS", deta.getFieldString("MPD_NOTAS"));
                        detaPaquete.setFieldString("PR_CODIGO", rs2.getString("PAQ_COD"));
                        if (intTipoOperacion == Inventario.ENTRADA || intTipoOperacion == Inventario.TRASPASO_ENTRADA) {
                           detaPaquete.setFieldDouble("MPD_ENTRADAS", deta.getFieldDouble("MPD_ENTRADAS") * rs2.getDouble("PAQ_CANTIDAD"));
                        } else {
                           detaPaquete.setFieldDouble("MPD_SALIDAS", deta.getFieldDouble("MPD_SALIDAS") * rs2.getDouble("PAQ_CANTIDAD"));
                        }
                        //Consultamos el producto para obtener el costo y la moneda
                        String strSQLProd3 = "SELECT PR_ID,PR_COSTOPROM,MON_ID from vta_producto "
                                + "where PR_CODIGO = '" + rs2.getString("PAQ_COD") + "' "
                                + " AND SC_ID = '" + this.movProd.getFieldInt("SC_ID") + "'";
                        try {
                           ResultSet rs3 = oConn.runQuery(strSQLProd3, true);
                           while (rs3.next()) {
                              detaPaquete.setFieldInt("PR_ID", rs3.getInt("PR_ID"));
                              detaPaquete.setFieldDouble("MPD_COSTO", rs3.getDouble("PR_COSTOPROM"));
                              detaPaquete.setFieldInt("MON_ID", rs3.getInt("MON_ID"));
                              detaPaquete.setFieldInt("MPD_PARIDAD", 1);
                              //Validamos si aun no existe la moneda para añadirla en el listado
                              if (rs.getInt("MON_ID") != this.intMonedaDefa && !mapMonedas.containsKey(rs3.getInt("MON_ID"))) {
                                 mapMonedas.put(rs3.getInt("MON_ID"), 1);
                              }
                           }
                           rs3.close();
                        } catch (SQLException ex) {
                           this.strResultLast = "ERROR:" + ex.getMessage();
                        }
                        //Agregamos el nuevo item
                        lstMovsTmpPaq.add(detaPaquete);
                     }
                     rs2.close();
                     //Borramos el item maestro
                     //this.lstMovs.remove(deta);
//                     deta.setBolMarca(true);
                  }
               }

            }
            rs.close();
         } catch (SQLException ex) {
            this.strResultLast = "ERROR:" + ex.getMessage();
         }
      }
      //Actualizamos la lista con el detalle de paquetes
      Iterator<vta_movproddeta> itPaq = lstMovsTmpPaq.iterator();
      while(itPaq.hasNext()){
         vta_movproddeta movPaq = itPaq.next();
         this.lstMovs.add(movPaq);
      }
      
      //Limpiamos la lista de los productos que no se inventarian
      ArrayList<vta_movproddeta> lstMovsTmp = new ArrayList<vta_movproddeta>();
      itMon = this.lstMovs.iterator();
      while (itMon.hasNext()) {
         vta_movproddeta deta = itMon.next();
         if (!deta.isBolMarca()) {
            lstMovsTmp.add(deta);
         }else{
            log.debug("deta " + deta.getFieldString("PR_ID"));
         }
      }
      this.lstMovs.clear();
      this.lstMovs = lstMovsTmp;

      //Obtenemos la paridad de cada moneda
      Iterator it = mapMonedas.entrySet().iterator();
      while (it.hasNext()) {
         Map.Entry e = (Map.Entry) it.next();
         //Buscamos la ultima paridad
         String strSqlTC = "select  TC_PARIDAD from vta_tasacambio "
                 + " where (TC_MONEDA1=" + this.intMonedaDefa + " AND TC_MONEDA2 = " + e.getKey() + ")"
                 + " OR (TC_MONEDA1=" + e.getKey() + " AND TC_MONEDA2 = " + this.intMonedaDefa + ") order by TC_FECHA desc";
         try {
            ResultSet rs3 = oConn.runQuery(strSqlTC, true);
            while (rs3.next()) {
               e.setValue(rs3.getDouble("TC_PARIDAD"));
               break;
            }
            rs3.close();
         } catch (SQLException ex) {
            this.strResultLast = "ERROR:" + ex.getMessage();
         }
      }

      //Recorremos todas las partidas para validar la paridad
      //Iteramos por los productos
      itMon = this.lstMovs.iterator();
      while (itMon.hasNext()) {
         vta_movproddeta deta = itMon.next();
         //Si la moneda es diferente buscamos su paridad en el hashmap
         if (deta.getFieldInt("MON_ID") != this.intMonedaDefa) {
            double dblParidad = 1;
            if (mapMonedas.get(deta.getFieldInt("MON_ID")) != null) {
               try {
                  dblParidad = Double.valueOf(mapMonedas.get(deta.getFieldInt("MON_ID")).toString());
               } catch (NumberFormatException ex) {
                  System.out.println("Error:Faltal conversión de monedas " + deta.getFieldInt("MON_ID") + " " + mapMonedas.get(deta.getFieldInt("MON_ID")));
               } catch (Exception ex) {
               }
            }

            deta.setFieldDouble("MPD_PARIDAD", dblParidad);
         }
      }
   }

   /**
    * De una lista de id de productos determina cuales se controlan por numero
    * de serie
    *
    * @param strLista Es la lista de productos por validar
    * @return Regresa un xml con los productos y el atributo de si se manejan
    * por codigo de barras
    * <?xml version=\"1.0\" encoding=\"UTF-8\" ?>
    * <codigo_barras>
    * <cb PR_ID="1020" USO="0" EXIST="32" REQEXIST="1" />
    * <cb PR_ID="1023" USO="1" EXIST="140" REQEXIST="1" />
    * <cb PR_ID="2034" USO="0" EXIST="0" REQEXIST="0" />
    * </codigo_barras>
    */
   public String ObtenProductoConSerie(String strLista) {
      //Limpiamos coma al inicio o final
      if (strLista.startsWith(",")) {
         strLista = strLista.substring(1, strLista.length());
      }
      if (strLista.endsWith(",")) {
         strLista = strLista.substring(0, strLista.length() - 1);
      }
      //Definimos el cabecero del XML
      StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
      try {
         strXML.append("<codigo_barras>");
         if (!strLista.isEmpty()) {
            String strSql = "select PR_ID,PR_USO_NOSERIE,PR_EXISTENCIA,PR_REQEXIST FROM vta_producto where PR_ID IN (" + strLista + ")";
            ResultSet rs = this.oConn.runQuery(strSql);
            while (rs.next()) {
               strXML.append("<cb PR_ID=\"").append(rs.getInt("PR_ID")).
                       append("\" USO=\"").append(rs.getInt("PR_USO_NOSERIE")).
                       append("\"  EXIST=\"").append(rs.getInt("PR_EXISTENCIA")).
                       append("\" REQEXIST=\"").append(rs.getInt("PR_REQEXIST")).append("\" "
                       + "/>");
            }
            rs.close();
         }
         strXML.append("</codigo_barras>");
      } // </editor-fold>
      catch (SQLException ex) {
         Logger.getLogger(Inventario.class.getName()).log(Level.SEVERE, null, ex);
      }
      return strXML.toString();
   }

   /**
    * Evalua si los numeros de serie de entrada son validos(no existen o estan
    * en ceros)
    */
   public void doEvaluaSeriesEntrada() {
      Iterator<vta_movproddeta> it = this.lstMovs.iterator();
      while (it.hasNext()) {
         vta_movproddeta deta = it.next();
         if (deta.isBolUsaSeries()) {
            //Validamos si existe el numero de serie
            double dblExistencia = 0;
            String strSqlSerie = "SELECT PL_EXISTENCIA FROM "
                    + " vta_prodlote where PR_ID = " + deta.getFieldInt("PR_ID")
                    + " AND PL_NUMLOTE = '" + deta.getFieldString("PL_NUMLOTE") + "'";
            try {
               ResultSet rs = oConn.runQuery(strSqlSerie, true);
               while (rs.next()) {
                  dblExistencia = rs.getDouble("PL_EXISTENCIA");
               }
               rs.close();
            } catch (SQLException ex) {
               this.strResultLast = "ERROR:" + ex.getMessage();
            }
            if (dblExistencia > 0) {
               this.strResultLast = "ERROR:EL NUMERO DE SERIE (" + deta.getFieldString("PL_NUMLOTE") + ") YA EXISTE";
               break;
            }
         }
      }
   }

   /**
    * Valida si los numeros de serie de salida son validos(que existan)
    */
   public void doEvaluaSeriesSalida() {
      //Validaciones de numeros de serie
      //Recorremos items con numeros de serie
      Iterator<vta_movproddeta> it3 = this.lstMovs.iterator();
      while (it3.hasNext()) {
         vta_movproddeta deta = it3.next();
         if (deta.isBolUsaSeries()) {
            //Validamos si existe el numero de serie
            double dblExistencia = 0;
            String strSqlSerie = "SELECT PL_EXISTENCIA FROM "
                    + " vta_prodlote where PR_ID = " + deta.getFieldInt("PR_ID")
                    + " AND PL_NUMLOTE = '" + deta.getFieldString("PL_NUMLOTE") + "'";
            try {
               ResultSet rs = oConn.runQuery(strSqlSerie, true);
               while (rs.next()) {
                  dblExistencia = rs.getDouble("PL_EXISTENCIA");
               }
               rs.close();
            } catch (SQLException ex) {
               this.strResultLast = "ERROR:" + ex.getMessage();
               log.error(ex.getMessage());
            }
            if (dblExistencia == 0) {
               this.strResultLast = "ERROR:EL NUMERO DE SERIE(" + deta.getFieldString("PL_NUMLOTE") + ") NO EXISTE O NO CUENTA CON EXISTENCIA";
               break;
            }
         }
      }
   }

   /**
    * Evalua si los numeros de serie de entrada son validos(no existen o estan
    * en ceros)
    *
    * @param intPR_ID Es el id del producto
    * @param strLote Es el lote
    * @return Regresa true si es valido el numero de serie
    */
   public boolean EsValidaSerieEntrada(int intPR_ID, String strLote) {
      boolean bolEsValido = true;

      String[] lstSerie = strLote.split(",");
      for (String lstSerie1 : lstSerie) {
         //Validamos si existe el numero de serie
         double dblExistencia = 0;
         String strSqlSerie = "SELECT PL_EXISTENCIA FROM "
                 + " vta_prodlote where PR_ID = " + intPR_ID
                 + " AND PL_NUMLOTE = '" + lstSerie1 + "'";
         try {
            ResultSet rs = oConn.runQuery(strSqlSerie, true);
            while (rs.next()) {
               dblExistencia = rs.getDouble("PL_EXISTENCIA");
            }
            rs.close();
         } catch (SQLException ex) {
            this.strResultLast = "ERROR:" + ex.getMessage();
         }
         if (dblExistencia > 0) {
            this.strResultLast = "ERROR:EL NUMERO DE SERIE (" + lstSerie1 + ") YA EXISTE";
            bolEsValido = false;
         }
      }


      return bolEsValido;
   }

   /**
    * Valida si los numeros de serie de salida son validos(que existan)
    *
    * @param intPR_ID Es el id del producto
    * @param strLote Es el lote
    * @return Regresa true si es valido el numero de serie
    */
   public boolean EsValidaSerieSalida(int intPR_ID, String strLote) {
      boolean bolEsValido = true;

      String[] lstSerie = strLote.split(",");
      for (String lstSerie1 : lstSerie) {
         //Validamos si existe el numero de serie
         double dblExistencia = 0;
         String strSqlSerie = "SELECT PL_EXISTENCIA FROM "
                 + " vta_prodlote where PR_ID = " + intPR_ID
                 + " AND PL_NUMLOTE = '" + lstSerie1 + "'";
         try {
            ResultSet rs = oConn.runQuery(strSqlSerie, true);
            while (rs.next()) {
               dblExistencia = rs.getDouble("PL_EXISTENCIA");
            }
            rs.close();
         } catch (SQLException ex) {
            this.strResultLast = "ERROR:" + ex.getMessage();
            log.error(ex.getMessage());
         }
         if (dblExistencia == 0) {
            this.strResultLast = "ERROR:EL NUMERO DE SERIE(" + lstSerie1 + ") NO EXISTE O NO CUENTA CON EXISTENCIA";
            bolEsValido = false;
         }
      }

      return bolEsValido;
   }

   /**
    * Regresa todos los numeros de serie de un producto en particular
    *
    * @param intPR_ID Es el id del producto
    * @return Regresa en un xml la lista de numeros de serie
    */
   public String RegresaNoSeries(int intPR_ID) {
      StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
      try {
         strXML.append("<num_series>");
         String strSql = "SELECT "
                 + "	vta_prodlote.PL_NUMLOTE "
                 + "FROM vta_prodlote "
                 + " WHERE vta_prodlote.PR_ID = " + intPR_ID + " AND PL_EXISTENCIA > 0";
         ResultSet rs = this.oConn.runQuery(strSql);
         while (rs.next()) {
            strXML.append("<serie PR_ID=\"").append(intPR_ID).
                    append("\" NO_SERIE=\"").append(rs.getString("PL_NUMLOTE")).
                    append("\" "
                    + "/>");
         }
         rs.close();
         strXML.append("</num_series>");
      } // </editor-fold>
      catch (SQLException ex) {
         Logger.getLogger(Inventario.class.getName()).log(Level.SEVERE, null, ex);
      }
      return strXML.toString();
   }

   /**
    * Calcula el costo promedio de una partida de una entrada
    *
    * @param intPR_ID Es el id de producto
    * @param dblCostoEntrada Es el costo de la entrada
    * @param dblEntrada Es la cantidad que esta ingresando
    * @param bolEvaluaMoneda Evalua si aplica para moneda extranjera
    * @param bolEvaluHist Indica si evalua los historicos
    * @param intIdMov Es el id del movimiento
    * @return Regresa OK si funciono
    */
   public String doCalculaPromedio(int intPR_ID, double dblCostoEntrada, double dblEntrada, boolean bolEvaluaMoneda, boolean bolEvaluHist, int intIdMov) {
      String strRes = "OK";
      //Obtenemos el costo promedio actual y las existencias actuales
      double dblCostoActual = 0;
      double dblExistenciaActual = 0;
      int intMonedaCosto = 0;
      String strSQLProd = "SELECT PR_EXISTENCIA,PR_COSTOPROM,PR_MONEDA_COSTO from vta_producto "
              + "where PR_ID = '" + intPR_ID + "'";
      try {
         ResultSet rs = oConn.runQuery(strSQLProd, true);
         while (rs.next()) {
            dblCostoActual = rs.getDouble("PR_COSTOPROM");
            dblExistenciaActual = rs.getDouble("PR_EXISTENCIA");
            intMonedaCosto = rs.getInt("PR_MONEDA_COSTO");
         }
         rs.close();
         //Si evalua historico buscamos la existencia hasta x movimiento y el costo promedio que tenia en ese momento
         if (bolEvaluHist) {
         }
      } catch (SQLException ex) {
         strRes = "ERROR:" + ex.getMessage();
         System.out.println("Error en calculo de costo promedio " + ex.getMessage());
      }
      //Calculamos el costo promedio
      double dblCostoPromedio = 0;
      //Calculamos el promedio solo en caso de moneda nacional(1) o no definida(0)
      if (intMonedaCosto == 0 || intMonedaCosto == 1 || bolEvaluaMoneda) {
         dblCostoPromedio = CalculoCostoPromedio(intPR_ID, dblCostoEntrada, dblEntrada,
                 dblCostoActual, dblExistenciaActual);
      }
      //Actualizamos la existencia de la entrada
      StringBuilder strUpdate = new StringBuilder("UPDATE vta_producto ");
      if (bolEvaluaMoneda) {
         strUpdate.append(" SET PR_COSTOPROM = ").append(dblCostoPromedio).append(" ");
      } else {
         strUpdate.append(" SET PR_EXISTENCIA = PR_EXISTENCIA + ").append(dblEntrada).append(" ");
         if (intMonedaCosto == 0 || intMonedaCosto == 1 || bolEvaluaMoneda) {
            strUpdate.append(",PR_COSTOPROM = ").append(dblCostoPromedio).append(" ");
         }
      }
      strUpdate.append(" WHERE PR_ID = ").append(intPR_ID);
      oConn.runQueryLMD(strUpdate.toString());
      //Asignamos valor calculado a la propiedad
      dblLastCostoPromedio = dblCostoPromedio;
      return strRes;
   }

   public void CalculaHistoricoPromedio() {
   }

   /**
    * Regresa todos los numeros de serie del detalle de un pedido surtido
    *
    * @param intPR_ID Es el id del producto
    * @param intPDD Es el id del detalle de pedidos
    * @return Regresa en un xml la lista de numeros de serie
    */
   public String RegresaNoSeriesPedidos(int intPR_ID, int intPDD) {
      StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
      try {
         strXML.append("<num_series>");
         String strSql = "select vta_movproddeta.PL_NUMLOTE,vta_pedidosdeta.PDD_ID \n"
                 + "from vta_pedidosdeta,vta_movproddeta \n"
                 + "where \n"
                 + " vta_movproddeta.MPD_IDORIGEN = vta_pedidosdeta.PDD_ID\n"
                 + " and vta_pedidosdeta.PDD_ID = " + intPDD + " AND MPD_SERIE_FACTURA = 0;";
         ResultSet rs = this.oConn.runQuery(strSql);
         while (rs.next()) {
            strXML.append("<serie PR_ID=\"").append(intPR_ID).
                    append("\" NO_SERIE=\"").append(rs.getString("PL_NUMLOTE")).
                    append("\" "
                    + "/>");
         }
         rs.close();
         strXML.append("</num_series>");
      } // </editor-fold>
      catch (SQLException ex) {
         Logger.getLogger(Inventario.class.getName()).log(Level.SEVERE, null, ex);
      }
      return strXML.toString();
   }
   // </editor-fold>
}
