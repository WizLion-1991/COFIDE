package com.mx.siweb.mlm.utilerias;

import com.mx.siweb.mlm.entidades.NodoRed;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Utilerias.Fechas;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * Clase para manipular las redes
 *
 * @author zeus
 */
public class Redes {

   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(Redes.class.getName());
   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private String strError;
   private String strNomARMADOPUNTA;
   private String strNomARMADONUM;
   private String strNomARMADOINI;
   private String strNomARMADOFIN;
   private String strNomARMADODEEP;
   private String strLadoNew;
   private boolean bolBalanceo;

   public boolean isBolBalanceo() {
      return bolBalanceo;
   }

   /**
    *Balancea las posiciones de las redes binarias
    * @param bolBalanceo Con true hace balanceo
    */
   public void setBolBalanceo(boolean bolBalanceo) {
      this.bolBalanceo = bolBalanceo;
   }

   /**
    * Regresa el lado nuevo del nodo calculado
    *
    * @return Es el nombre de la pata I o D
    */
   public String getStrLadoNew() {
      return strLadoNew;
   }

   /**
    * Regresa el mensaje de error
    *
    * @return Es una cadena con un mensaje de error
    */
   public String getStrError() {
      return strError;
   }

   /**
    * Define el mensaje de error
    *
    * @param strError Es una cadena con un mensaje de error
    */
   public void setStrError(String strError) {
      this.strError = strError;
   }

   public String getStrNomARMADOPUNTA() {
      return strNomARMADOPUNTA;
   }

   public void setStrNomARMADOPUNTA(String strNomARMADOPUNTA) {
      this.strNomARMADOPUNTA = strNomARMADOPUNTA;
   }

   public String getStrNomARMADONUM() {
      return strNomARMADONUM;
   }

   public void setStrNomARMADONUM(String strNomARMADONUM) {
      this.strNomARMADONUM = strNomARMADONUM;
   }

   public String getStrNomARMADOINI() {
      return strNomARMADOINI;
   }

   public void setStrNomARMADOINI(String strNomARMADOINI) {
      this.strNomARMADOINI = strNomARMADOINI;
   }

   public String getStrNomARMADOFIN() {
      return strNomARMADOFIN;
   }

   public void setStrNomARMADOFIN(String strNomARMADOFIN) {
      this.strNomARMADOFIN = strNomARMADOFIN;
   }

   public String getStrNomARMADODEEP() {
      return strNomARMADODEEP;
   }

   public void setStrNomARMADODEEP(String strNomARMADODEEP) {
      this.strNomARMADODEEP = strNomARMADODEEP;
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public Redes() {
      strNomARMADOPUNTA = "ARMADOPUNTA";
      strNomARMADONUM = "ARMADONUM";
      strNomARMADOINI = "ARMADOINI";
      strNomARMADOFIN = "ARMADOFIN";
      strNomARMADODEEP = "ARMADODEEP";
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   /**
    * Este metodo valida que una clave este dentro de la red de otra clave
    *
    * @param strTabla Es el nombre de la tabla
    * @param strCampoPadre Es el nombre del campo padre(upline)
    * @param strCampoLlave Es el nombre del campo llave
    * @param intClavePadre Es el valor del campo padre
    * @param intClaveHijo Es el valor del campo hijo
    * @param oConn Es el objeto que contiene la conexion
    * @return Un valor boolean con true en caso de encontrarse el hijo dentro de
    * la red del padre(existe un circulo)
    * @throws ExceptionRed La excepcion si falla algo en la red
    */
   public static boolean hayUnCirculo(Conexion oConn,
           String strTabla,
           String strCampoPadre,
           String strCampoLlave,
           int intClavePadre,
           int intClaveHijo) throws ExceptionRed {
      boolean bolRespuesta = false;
      int bitFlag = 1;
      int intClaveAux = intClavePadre;

      log.info("Inicia circulos..");

      // <editor-fold defaultstate="collapsed" desc="Contamos el total de nodos">
      int intTotalNodos = 0;
      String strSql2 = "SELECT count(" + strCampoLlave + ") as CUANTOS "
              + " FROM " + strTabla + " ";
      try {
         ResultSet rs = oConn.runQuery(strSql2);
         while (rs.next()) {
            intTotalNodos = rs.getInt("CUANTOS");
         }
          //if(rs.getStatement() != null )rs.getStatement().close(); 
          rs.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage());
      }
      // </editor-fold>

      //Validamos si el upline y la clase es igual
      if (intClavePadre == intClaveHijo) {
         bolRespuesta = true;
      } else {
         ResultSet Rs = null;
         int intContaProcesados = 0;
         while (intClaveHijo != intClaveAux && intClaveAux != 0 && bitFlag == 1) {
            intContaProcesados++;
            //Query por ejecutar
            StringBuilder strSQL = new StringBuilder("");
            strSQL.append("SELECT ").append(strCampoPadre).append(" FROM ").append(strTabla).append(" WHERE ").append(strCampoLlave).append(" = ").append(intClaveAux);
            try {
               Rs = oConn.runQuery(strSQL.toString());
               while (Rs.next()) {
                  //Validamos circulos para evitar loops
                  log.info("Inicia circulos.. " + intClaveHijo + " " + Rs.getInt(strCampoPadre));
                  if (intClaveHijo == Rs.getInt(strCampoPadre) || intClaveAux == Rs.getInt(strCampoPadre)) {
                     Rs.close();
                     throw new ExceptionRed("Encontramos un circulo al validar el nodo con el nodo padre " + intClavePadre + " nodo hijo " + intClaveHijo);
                  } else {
                     intClaveAux = Rs.getInt(strCampoPadre);
                  }
               }
               Rs.close();
               //Validamos si es la clave 1
            } catch (SQLException ex) {
               log.error(ex.getMessage());
               bitFlag = 0;
            }
            //Si el total de items procesados supera el largo de la red hay un circulo
            if (intContaProcesados > intTotalNodos) {
               log.info("intContaProcesados:" + intContaProcesados + " intTotalNodos:" + intTotalNodos);
               throw new ExceptionRed("Encontramos un circulo al validar el nodo con el nodo padre " + intClavePadre + " nodo hijo " + intClaveHijo);
            }
         }
      }
      log.info("Termina circulos..");
      return bolRespuesta;
   }

   /**
    * Este metodo valida que una clave este dentro de la red de otra clave
    *
    * @param strTabla Es el nombre de la tabla
    * @param strCampoPadre Es el nombre del campo padre(upline)
    * @param strCampoLlave Es el nombre del campo llave
    * @param intClavePadre Es el valor del campo padre
    * @param intClaveHijo Es el valor del campo hijo
    * @param oConn Es el objeto que contiene la conexion
    * @return Un valor boolean con true en caso de que el hijo pertenezca a la
    * red
    * @throws ExceptionRed La excepcion si falla algo en la red
    */
   public static boolean esPartedelaRed(Conexion oConn,
           String strTabla,
           String strCampoPadre,
           String strCampoLlave,
           int intClavePadre,
           int intClaveHijo) throws ExceptionRed {
      boolean bolRespuesta = false;
      int bitFlag = 1;
      int intClaveAux = intClavePadre;
      org.apache.logging.log4j.Logger log = LogManager.getLogger(Redes.class.getName());
      log.info("Inicia validar nodo hijo..");

      // <editor-fold defaultstate="collapsed" desc="Contamos el total de nodos">
      int intTotalNodos = 0;
      String strSql2 = "SELECT count(" + strCampoLlave + ") as CUANTOS "
              + " FROM " + strTabla + " ";
      try {
         ResultSet rs = oConn.runQuery(strSql2);
         while (rs.next()) {
            intTotalNodos = rs.getInt("CUANTOS");
         }
          //if(rs.getStatement() != null )rs.getStatement().close(); 
          rs.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage());
      }
      // </editor-fold>

      //Validamos si el upline y la clase es igual
      if (intClavePadre == intClaveHijo) {
         bolRespuesta = false;
      } else {
         ResultSet Rs = null;
         int intContaProcesados = 0;
         while (intClaveHijo != intClaveAux && intClaveAux != 0 && bitFlag == 1) {
            intContaProcesados++;
            //Query por ejecutar
            StringBuilder strSQL = new StringBuilder("");
            strSQL.append("SELECT ").append(strCampoPadre).append(" FROM ").append(strTabla).append(" WHERE ").append(strCampoLlave).append(" = ").append(intClaveAux);
            try {
               Rs = oConn.runQuery(strSQL.toString());
               while (Rs.next()) {
                  //Validamos si pertenece a la red
                  log.info("Evaluamos nodo.. " + intClavePadre + " " + Rs.getInt(strCampoPadre));
                  if (intClavePadre == Rs.getInt(strCampoPadre)) {
                     bolRespuesta = true;
                     break;
                  } else {
                     intClaveAux = Rs.getInt(strCampoPadre);
                  }
               }
               Rs.close();
               //Validamos si es la clave 1
            } catch (SQLException ex) {
               log.error(ex.getMessage());
               bitFlag = 0;
            }
            //Si el total de items procesados supera el largo de la red hay un circulo
            if (intContaProcesados > intTotalNodos) {
               log.info("intContaProcesados:" + intContaProcesados + " intTotalNodos:" + intTotalNodos);
               throw new ExceptionRed("Encontramos un circulo al validar el nodo con el nodo padre " + intClavePadre + " nodo hijo " + intClaveHijo);
            }
         }
      }
      log.info("Termina validar nodo hijo..");
      return bolRespuesta;
   }

   /**
    * Este metodo indica si el nodo padre es valido
    *
    * @param strTabla Es el nombre de la tabla
    * @param strCampoPadre Es el nombre del campo padre(upline)
    * @param strCampoLlave Es el nombre del campo llave
    * @param intClavePadre Es el valor del campo padre
    * @param oConn Es el objeto que contiene la conexion
    * @return Un valor boolean con true en caso de ser un nodo padre valido Se
    * verifican los circulos y que exista el nodo
    */
   public boolean esValidoElNodoPadre(Conexion oConn,
           String strTabla,
           String strCampoPadre,
           String strCampoLlave,
           int intClavePadre) {
      boolean bolValido = false;
      String strSQL = "SELECT " + strCampoLlave + " FROM " + strTabla + " WHERE " + strCampoLlave + " = " + intClavePadre;
      try {
         ResultSet Rs = oConn.runQuery(strSQL);
         while (Rs.next()) {
            bolValido = true;
            break;
         }
         Rs.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage());
      }
      return bolValido;
   }

   /**
    * Este metodo mueve la red de una persona a su upline esto para poder borrar
    * la persona saisfactoriamente Hay que implementar validacion para nodos
    * binarios y matriz forzadas
    *
    * @param strTabla Es el nombre de la tabla
    * @param strNomCampoLLave Es el nombre del campo llave
    * @param strNomCampoPadre Es el nombre del campo que contiene el valor del
    * padre(upline)
    * @param intNodoBorrar Es el valor del campo llave a compactar
    * @param intMatrizForzada Con un valor mayor de 1 validamos matriz forzada o
    * binario
    * @param bolExcepcionRaiz Indica que el nodo raiz tiene excepcion a la
    * matriz forzada en caso de aplicar
    * @param oConn Objeto con la conexion a la base de datos
    * @return Regresa True si se pudo compactar el arbol
    */
   public static boolean compactarArbol(String strTabla,
           String strNomCampoLLave,
           String strNomCampoPadre,
           int intNodoBorrar, int intMatrizForzada,
           boolean bolExcepcionRaiz,
           Conexion oConn) {
      boolean bolMovimientoValido = true;
      org.apache.logging.log4j.Logger log = LogManager.getLogger(Redes.class.getName());
      log.info("Inicia compacta..");
      //Buscamos el papa del cliente a borrar
      String strSQL = "SELECT " + strNomCampoPadre + " FROM " + strTabla + " where " + strNomCampoLLave + " = " + intNodoBorrar;
      try {
         ResultSet Rs = oConn.runQuery(strSQL);
         while (Rs.next()) {
            int intClavePadre = Rs.getInt(strNomCampoPadre);
            // <editor-fold defaultstate="collapsed" desc="Validaciones dependiendo del tipo de red">
            if (intMatrizForzada > 1) {
               //Validamos la excepcion de la matriz forazada
               if (bolExcepcionRaiz && intClavePadre == 1) {
               } else {
                  //Iniciamos la validacion
                  //Contamos cuantos hijos tiene el abuelo
                  int intCuantosHijos = 0;
                  String strSQL2 = "SELECT count(" + strNomCampoLLave + ") as cuantos FROM " + strTabla + " where " + strNomCampoPadre + " = " + intClavePadre;
                  ResultSet Rs2 = oConn.runQuery(strSQL2);
                  while (Rs2.next()) {
                     intCuantosHijos = Rs2.getInt("cuantos");
                     //Restamos el hijo que se va a borrar
                     intCuantosHijos--;
                  }
                  Rs2.close();
                  //Contamos cuantos hijos tiene el nodo por borrar
                  int intCuantosHijosNodoBorrar = 0;
                  strSQL2 = "SELECT count(" + strNomCampoLLave + ") as cuantos FROM " + strTabla + " where " + strNomCampoPadre + " = " + intNodoBorrar;
                  Rs2 = oConn.runQuery(strSQL2);
                  while (Rs2.next()) {
                     intCuantosHijosNodoBorrar = Rs2.getInt("cuantos");
                  }
                  Rs2.close();
                  //Si los hijos del nodo por borrar mas el nodo padre superan la matriz no procede la eliminación
                  if ((intCuantosHijos + intCuantosHijosNodoBorrar) > intMatrizForzada) {
                     bolMovimientoValido = false;
                  }
               }
            }
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Validamos si no se genera un circulo">
            if (bolMovimientoValido) {
               boolean bolHayCirculo = hayUnCirculo(oConn, strTabla, strNomCampoPadre, strNomCampoLLave, intClavePadre, intNodoBorrar);
               if (bolHayCirculo) {
                  Rs.close();
                  bolMovimientoValido = false;
                  throw new ExceptionRed("Encontramos un circulo al validar el nodo con el nodo padre " + intClavePadre + " nodo hijo " + intNodoBorrar);
               }
            }
            // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Si es valido el movimiento hacemos el ajuste">
            if (bolMovimientoValido) {
               //Armamos query para subir la red al papa
               strSQL = "Update " + strTabla + " set "
                       + strNomCampoPadre + " = " + intClavePadre + " where "
                       + strNomCampoPadre + " = " + intNodoBorrar;
               oConn.runQueryLMD(strSQL);
            }
            // </editor-fold>
         }
         Rs.close();
         //Validamos si es la clave 1
      } catch (ExceptionRed ex) {
         log.error(ex.getMessage());
         bolMovimientoValido = false;
      } catch (SQLException ex) {
         log.error(ex.getMessage());
         bolMovimientoValido = false;
      }
      log.info("Inicia compacta..");
      return bolMovimientoValido;
   }

   /**
    * Este metodo valida si un movimiento de un nodo de la red es valido
    *
    * @param strTabla Es el nombre de la tabla
    * @param strNomCampoLLave Es el nombre del campo llave
    * @param strNomCampoPadre Es el nombre del campo que contiene el valor del
    * padre(upline)
    * @param intNodoMover Es el valor del campo llave a compactar
    * @param intNodoPadreNuevo Es el nodo del nuevo padre
    * @param intMatrizForzada Con un valor mayor de 1 validamos matriz forzada o
    * binario
    * @param bolExcepcionRaiz Indica que el nodo raiz tiene excepcion a la
    * matriz forzada en caso de aplicar
    * @param oConn Objeto con la conexion a la base de datos
    * @return Regresa True si es valido el movimiento
    */
   public static boolean moverArbol(String strTabla,
           String strNomCampoLLave,
           String strNomCampoPadre,
           int intNodoMover, int intNodoPadreNuevo, int intMatrizForzada,
           boolean bolExcepcionRaiz,
           Conexion oConn) {
      org.apache.logging.log4j.Logger log = LogManager.getLogger(Redes.class.getName());
      boolean bolMovimientoValido = false;
      log.info("Iniciamos mover arbol");
      try {
         // <editor-fold defaultstate="collapsed" desc="Validamos si existe el nuevo nodo padre">
         String strSQL = "SELECT " + strNomCampoPadre + " FROM " + strTabla + " where " + strNomCampoLLave + " = " + intNodoPadreNuevo;
         ResultSet Rs = oConn.runQuery(strSQL);
         while (Rs.next()) {
            bolMovimientoValido = true;
         }
         Rs.close();
         // </editor-fold>
         //Si existe continuamos
         if (bolMovimientoValido) {
            // <editor-fold defaultstate="collapsed" desc="Buscamos el papa actual del cliente a mover">
            int intClavePadre = 0;
            strSQL = "SELECT " + strNomCampoPadre + " FROM " + strTabla + " where " + strNomCampoLLave + " = " + intNodoMover;
            Rs = oConn.runQuery(strSQL);
            while (Rs.next()) {
               intClavePadre = Rs.getInt(strNomCampoPadre);
            }
            Rs.close();
            // </editor-fold>
            //Validamos si el nodo padre sigue siendo el mismo
            if (intClavePadre != intNodoPadreNuevo) {
               // <editor-fold defaultstate="collapsed" desc="Validaciones dependiendo del tipo de red">
               if (intMatrizForzada > 1) {
                  //Validamos la excepcion de la matriz forazada
                  if (bolExcepcionRaiz && intNodoPadreNuevo == 1) {
                  } else {
                     //Iniciamos la validacion
                     //Contamos cuantos hijos tiene el abuelo
                     int intCuantosHijos = 0;
                     String strSQL2 = "SELECT count(" + strNomCampoLLave + ") as cuantos FROM " + strTabla + " where " + strNomCampoPadre + " = " + intNodoPadreNuevo;
                     ResultSet Rs2 = oConn.runQuery(strSQL2);
                     while (Rs2.next()) {
                        intCuantosHijos = Rs2.getInt("cuantos");
                     }
                     intCuantosHijos++;//Sumamos el nuevo nodo
                     Rs2.close();
                     //Si los hijos del nodo por borrar mas el nodo padre superan la matriz no procede la eliminación
                     if ((intCuantosHijos) > intMatrizForzada) {
                        bolMovimientoValido = false;
                     }
                  }
               }
               // </editor-fold>
               if (bolMovimientoValido) {
                  // <editor-fold defaultstate="collapsed" desc="Validamos si no se genera un circulo">
                  boolean bolHayCirculo = hayUnCirculo(oConn, strTabla, strNomCampoPadre, strNomCampoLLave, intNodoPadreNuevo, intNodoMover);
                  if (bolHayCirculo) {
                     bolMovimientoValido = false;
                  }
                  // </editor-fold>
               }
            }
         }
      } catch (ExceptionRed ex) {
         log.error(ex.getMessage());
         Logger.getLogger(Redes.class.getName()).log(Level.SEVERE, null, ex);
         bolMovimientoValido = false;
      } catch (SQLException ex) {
         log.error(ex.getMessage());
      }
      log.info("Terminamos mover arbol");
      return bolMovimientoValido;
   }

   /**
    * Proceso para armar el arbol
    *
    * @param strTabla Es la tabla
    * @param strCampoLLave Es el campo llave
    * @param strCampoPadre Es el campo padre
    * @param intNodoRaiz Es el id del nodo raiz
    * @param strPrefijo Es el prefijo de los campos
    * @param strCond Es la condición
    * @param strOrden Es el orden
    * @param bolArmarPuntas Indica si se arman las puntas
    * @param bolTransaccional Indica si se usa la transaccionalidad
    * @param oConn Es la conexion
    * @return Regresa true si se armo el arbol
    */
   public boolean armarArbol(String strTabla,
           String strCampoLLave,
           String strCampoPadre,
           int intNodoRaiz,
           String strPrefijo,
           String strCond,
           String strOrden,
           boolean bolArmarPuntas,
           boolean bolTransaccional,
           Conexion oConn) {
      this.strError = "";
      // <editor-fold defaultstate="collapsed" desc="Inicializamos objetos">
      Fechas fecha = new Fechas();
      log.debug("Inicia.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
      //Objeto resulset donde pondremos los resultados de las consultas
      ResultSet Rs = null;
      //Valor booleano que nos indica si hubo un error
      boolean bolHayError = false;
      //Contador para cada cierto numero de registro
      int intContaReg = 0;
      int intContaRegTot = 0;
      //Pilas donde almacenaremos la red
      Stack<NodoRed> Tabla1 = new Stack<NodoRed>();
      Stack<NodoRed> Tabla2 = new Stack<NodoRed>();
      // </editor-fold >

      // <editor-fold defaultstate="collapsed" desc="Validamos si intNodoRaiz es diferente de cero, en caso contrario buscamos el valor minimo en la base de datos">
      if (intNodoRaiz == 0) {
         //Consultamos los datos de la red
         String strSql = "SELECT min(" + strCampoLLave + ") as MIN"
                 + " FROM " + strTabla + " " + strCond;
         try {
            Rs = oConn.runQuery(strSql);
            while (Rs.next()) {
               intNodoRaiz = Rs.getInt("MIN");
            }
            Rs.close();
         } catch (SQLException ex) {
            log.error(ex.getMessage());
         }
      }
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Contamos el total de nodos">
      int intTotalNodos = 0;
      String strSql2 = "SELECT count(" + strCampoLLave + ") as CUANTOS "
              + " FROM " + strTabla + " " + strCond;
      try {
         Rs = oConn.runQuery(strSql2);
         while (Rs.next()) {
            intTotalNodos = Rs.getInt("CUANTOS");
         }
         Rs.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage());
      }
      // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="Inicializamos las pilas">
      log.debug("Comenzamos a llenar la pila...");
      System.out.flush();

      boolean bolErrorCirculo = false;
      Tabla1.push(new NodoRed(intNodoRaiz, 1));
      //Recorremos la primer pila mientras halla datos
      while (!Tabla1.empty()) {
         NodoRed TuplaActual = Tabla1.pop();
         int intPadre = TuplaActual.getIntNodoId();
         int intDeep = TuplaActual.getIntDeep();
         Tabla2.push(new NodoRed(intPadre, intDeep));
         //Consultamos los datos de la red
         String strSql = "SELECT " + strCampoLLave + "," + strCampoPadre
                 + " FROM " + strTabla + " WHERE " + strCampoPadre + " = " + intPadre + " " + strOrden;
         try {
            Rs = oConn.runQuery(strSql);
            while (Rs.next()) {
               int intCveAppend = Rs.getInt(strCampoLLave);
               //Anadimos el hijo a la pila
               Tabla1.push(new NodoRed(intCveAppend, intDeep + 1));
               intContaReg++;
               intContaRegTot++;
               if (intContaRegTot > intTotalNodos) {
                  log.debug("*************Ocurrio un circulo...********* REDES LINE 271");
                  bolErrorCirculo = true;
                  break;
               }
               if (intContaReg == 1000) {
                  if (oConn.isBolMostrarQuerys() == true) {
                     log.debug("Llevamos " + intContaRegTot + " registros procesados...");
                     System.out.flush();
                  }
                  intContaReg = 0;
               }
            }
            Rs.close();
         } catch (SQLException ex) {
            log.error(ex.getMessage());
         }
         //Nos salimos en caso de circulo
         if (bolErrorCirculo) {
            break;
         }
      }
      if (oConn.isBolMostrarQuerys() == true) {
         log.debug("Totals de registro procesados " + intContaRegTot + "...");
      }

      // </editor-fold>
      //Si no hay error por circulo proseguimos
      if (bolErrorCirculo == false) {
         log.debug("COMENZAMOS ACTUALIZAR LOS REGISTROS...");
         System.out.flush();

         // <editor-fold defaultstate="collapsed" desc="Guardamos los nodos">
         //Comienza Transaccion
         if (bolTransaccional == true) {
            String strTrans = "BEGIN";
            oConn.runQueryLMD(strTrans);
         }
         //Contadores para tiempos
         intContaReg = 0;
         intContaRegTot = 0;
         long lgnTiempoTot = 0;
         //Recorremos la pila 2
         Iterator<NodoRed> elem = Tabla2.iterator();
         int intConta = 0;
         //Ciclo para recorrer campos
         while (elem.hasNext()) {
            NodoRed TuplaActual = elem.next();
            int intPadre = TuplaActual.getIntNodoId();
            int intDeep = TuplaActual.getIntDeep();
            int intFinal = 0;
            intConta++;
            int intInicial = intConta;
            // <editor-fold defaultstate="collapsed" desc="Recorremos las tuplas para obtener la profundidad">
            for (int j = intConta; j < Tabla2.size(); j++) {
               NodoRed TuplaDeep = Tabla2.elementAt(j);
               int intDeep2 = TuplaDeep.getIntDeep();
               //Validamos el Deep
               if (intDeep2 <= intDeep) {
                  intFinal = j;
                  //System.out.println("Deeps OK:  " + j + " " + intDeep + " " + intDeep2);
                  break;
               }
               //System.out.println("Deeps 2:  " + intDeep + " " + intDeep2 );
            }
            // </editor-fold>
            //System.out.println("Cve sacada de la pila 2: " + intPadre + " " + intDeep + " " + intConta);
            if (intFinal == 0) {
               intFinal = Tabla2.size();
            }
            Date d1 = new Date();//
            // <editor-fold defaultstate="collapsed" desc="Actualizamos los clientes">
            //System.out.println("Comienza:  " +  intPadre + " " + coreUtileriasdeFechas.RegresaHoraActualHHMMSS());
            String strSql = "UPDATE " + strTabla + " SET  "
                    + strPrefijo + strNomARMADONUM + " = " + intConta + ", "
                    + strPrefijo + strNomARMADOINI + " = " + intInicial + ", "
                    + strPrefijo + strNomARMADOFIN + " = " + intFinal + ", "
                    + strPrefijo + strNomARMADODEEP + " = " + intDeep + " WHERE " + strCampoLLave + " =" + intPadre;
            //System.out.println("Cve sacada de la pila 2: " + intPadre + " " + intDeep + " " + intConta );
            //Actualizamos la tabla
            int intResult = oConn.runQueryLMD(strSql);
            Date d2 = new Date();//
            long lngDif = d2.getTime() - d1.getTime();
            lgnTiempoTot += lngDif;
            //System.out.println("Termina:  " +  intPadre + " " + coreUtileriasdeFechas.RegresaHoraActualHHMMSS() + " dif:" + dif);
            // </editor-fold>
            intContaReg++;
            intContaRegTot++;
            if (intContaReg == 1000) {
               log.debug("Llevamos " + intContaRegTot + " registros procesados...");
               System.out.flush();
               intContaReg = 0;
            }
         }
         //Comienza Transaccion
         if (bolTransaccional == true) {
            String strTrans = "COMMIT";
            oConn.runQueryLMD(strTrans);
         }
         //Tiempos promedio
         Long lgnPromedio = lgnTiempoTot / intContaRegTot;
         log.debug("Tiempo promedio de actualizacion:" + lgnPromedio);
         System.out.flush();
         intContaReg = 0;
         intContaRegTot = 0;
         // </editor-fold>

         // <editor-fold defaultstate="collapsed" desc="Aplica unicamente si seleccionan armar las puntas">
         if (bolArmarPuntas == true) {
            ResultSet Rs2 = null;
            if (oConn.isBolMostrarQuerys() == true) {
               log.debug("ARMAMOS LAS PUNTAS...");
               System.out.flush();
            }
            //Limpiamos las puntas
            String strSql = "UPDATE " + strTabla + " SET " + strPrefijo + strNomARMADOPUNTA + "=0 " + strCond;
            oConn.runQueryLMD(strSql);
            //Buscamos todos los nodos
            strSql = "SELECT " + strCampoLLave + "," + strCampoPadre
                    + " FROM " + strTabla + " " + strCond + " ORDER BY " + strPrefijo + strNomARMADONUM + "";
            try {
               Rs = oConn.runQuery(strSql);
               while (Rs.next()) {
                  int intCveAppend = Rs.getInt(strCampoLLave);
                  //Buscamos si tiene hijos
                  strSql = "SELECT " + strCampoLLave + "," + strCampoPadre
                          + " FROM " + strTabla + " WHERE " + strCampoPadre + " = " + intCveAppend;
                  try {
                     Rs2 = oConn.runQuery(strSql);
                     boolean bolEncontro = false;
                     while (Rs2.next()) {
                        bolEncontro = true;
                        Rs2.afterLast();
                     }
                     Rs2.close();
                     //Si no se encontraron resultados marcalo como punta
                     if (bolEncontro == false) {
                        strSql = "UPDATE " + strTabla + " SET " + strPrefijo + strNomARMADOPUNTA + "=1 WHERE " + strCampoLLave + "= " + intCveAppend;
                        oConn.runQueryLMD(strSql);
                        intContaReg++;
                        intContaRegTot++;
                        if (intContaReg == 1000) {
                           if (oConn.isBolMostrarQuerys() == true) {
                              log.debug("Llevamos " + intContaRegTot + " registros procesados...");
                              System.out.flush();
                           }
                           intContaReg = 0;
                        }
                     }
                  } catch (SQLException ex) {
                     log.error(ex.getMessage());
                  }
               }
               Rs.close();
            } catch (SQLException ex) {
               log.error(ex.getMessage());
            }
         }
         // </editor-fold>
      } else {
         //Hay un circulo hay que marcar error
         this.strError = "ERROR:HAY UN CIRCULO EN LA RED";
         bolHayError = true;
      }
      log.debug("Termina.... " + fecha.getFechaActualDDMMAAAADiagonal() + " " + fecha.getHoraActualHHMMSS());
      return bolHayError;
   }

   /**
    * Nos regresa el primer upline disponible del cliente buscandolo por
    * produndidad, es decir, primero los hijos luego en los nietos luego en los
    * bisnietos etc.
    *
    * @param intCve Es la clave del cliente
    * @param intForzed Es el numero de nodos a forzar(cuantos hijos podemos
    * tener)
    * @param strLado Es el lado en que queremos buscar el upline
    * @param bolUsaBinario Indicamos si usamos la validacin binaria
    * @param oConn Es la conexion
    * @param bolDif
    * @return regresa la clave del upline disponible, o -1 en caso de no existir
    * la clave
    */
   public int calculaUpline(int intCve,
           int intForzed,
           String strLado,
           boolean bolUsaBinario,
           Conexion oConn,
           boolean bolDif) {
      int intUpline = -1;
      int intContaHijos = 0;
      boolean bolLadoIzq = false;
      boolean bolLadoDer = false;
      String strSql = "SELECT CT_ID,CT_LADO "
              + " FROM vta_cliente WHERE "
              + " CT_UPLINE = " + intCve;
      ResultSet rs;
      try {
         rs = oConn.runQuery(strSql);
         while (rs.next()) {
            intContaHijos++;
            if (rs.getString("CT_LADO").equals("D")) {
               bolLadoDer = true;
            }
            if (rs.getString("CT_LADO").equals("I")) {
               bolLadoIzq = true;
            }
         }
          //if(rs.getStatement() != null )rs.getStatement().close(); 
          rs.close();
         /*Validamos si se usa validacion binaria*/
         if (bolUsaBinario) {
            if ((strLado.equals("D") && !bolLadoDer)
                    || (strLado.equals("I") && !bolLadoIzq)
                    || (strLado.equals("") && (!bolLadoDer || !bolLadoIzq))) {
               intUpline = intCve;
               if (strLado.equals("D")) {
                  this.strLadoNew = "D";
               }
               if (strLado.equals("I")) {
                  this.strLadoNew = "I";
               }
               if (strLado.isEmpty() && !bolLadoDer) {
                  this.strLadoNew = "D";
                  strLado = "D";
               }
               if (strLado.isEmpty() && !bolLadoIzq) {
                  this.strLadoNew = "I";
               }
            } else {

               //Buscamos por profundidad en que nivel hay disponibles

               int intDeep = 0;
               boolean bolEncontro = false;
               StringBuilder strListCves = new StringBuilder();
               strListCves.append(intCve);
               log.debug("strListCves:" + strListCves.toString());
               while (!bolEncontro) {
                  intDeep++;
                  int intContaTempo = 0;
                  strSql = "SELECT CT_ID,CT_LADO "
                          + " ,(SELECT COUNT(b.CT_ID) FROM vta_cliente as b where b.CT_UPLINE = vta_cliente.CT_ID and CT_LADO='D') as cuantos_d "
                          + " ,(SELECT COUNT(b.CT_ID) FROM vta_cliente as b where b.CT_UPLINE = vta_cliente.CT_ID and CT_LADO='I') as cuantos_i "
                          + " FROM vta_cliente WHERE "
                          + " CT_UPLINE IN ( " + strListCves.toString() + ") ";//Si lo dejamos ordenado solo por lado balancea las redes
                  if(bolBalanceo){
                     strSql += " order by CT_LADO";
                  }else{
                     strSql += " order by CT_ID,CT_LADO";
                  }
                  log.debug("strSql:" + strSql);
                  rs = oConn.runQuery(strSql);
                  strListCves = new StringBuilder("");
                  while (rs.next()) {
                     intContaTempo++;
                     if (intContaTempo > 1) {
                        strListCves.append(",").append(rs.getInt("CT_ID"));
                     } else {
                        strListCves.append(rs.getInt("CT_ID"));
                     }
                     if(rs.getInt("cuantos_d") == 0){
                        bolEncontro = true;
                        intUpline = rs.getInt("CT_ID");
                        this.strLadoNew = "D";
                        break;
                     }
                     if(rs.getInt("cuantos_i") == 0){
                        bolEncontro = true;
                        intUpline = rs.getInt("CT_ID");
                        this.strLadoNew = "I";
                        break;
                     }
                  }
                   //if(rs.getStatement() != null )rs.getStatement().close(); 
                   rs.close();
               }
               log.debug("Deep:" + intDeep);
            }
         } else {
            if (intContaHijos < intForzed) {
               intUpline = intCve;
            } else {
               boolean bolEncontro = false;
               String strIds = "" + intCve;
               while (bolEncontro == false) {
                  String strIdsTmp = "";
                  strSql = "Select CT_ID,CT_UPLINE From vta_cliente Where CT_UPLINE IN (" + strIds + ") ORDER BY CT_UPLINE,CT_ID";
                  rs = oConn.runQuery(strSql);
                  boolean bolHayNodos = false;
                  while (rs.next()) {
                     bolHayNodos = true;
                     strIdsTmp += rs.getString("CT_ID") + ",";
                     int intCuantos = 0;
                     ResultSet rsH = oConn.runQuery("Select count(CT_ID) As CUANTOS From vta_cliente Where CT_UPLINE=" + rs.getString("CT_ID"));
                     while (rsH.next()) {
                        intCuantos = rsH.getInt("CUANTOS");
                     }
                     rsH.close();
                     //System.out.println("Buscando en el nodo ----" + Rs.getString("CT_ID") + " con upline " + Rs.getString("CT_UPLINE") + " CON HIJO " + intCuantos + " forzado a " + intForzed);
                     if (intCuantos < intForzed) {
                        intUpline = rs.getInt("CT_ID");
                        //System.out.println("::ENCONTRO::**" + Rs.getInt("CT_ID"));
                        bolEncontro = true;
                        break;
                     }
                  }
                   //if(rs.getStatement() != null )rs.getStatement().close(); 
                   rs.close();
                  if (bolEncontro == false) {
                     if (strIdsTmp.endsWith(",")) {
                        strIdsTmp = strIdsTmp.substring(0, strIdsTmp.length() - 1);
                     }
                     strIds = new String(strIdsTmp);
                  }
                  if (bolHayNodos == false) {
                     bolEncontro = true;
                  }
               }
            }
         }
      } catch (SQLException ex) {
         log.error(ex.getMessage());
      }
      //System.out.println("UPLINE RESCATADO: " + intUpline);
      return intUpline;
   }
   // </editor-fold>
}
