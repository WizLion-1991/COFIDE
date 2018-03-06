package ERP;

import comSIWeb.Operaciones.Bitacora;
import comSIWeb.Operaciones.Conexion;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Esta clase genera en automatico los folios para cada operacion
 *
 * @author zeus
 */
public class Folios {
   // <editor-fold defaultstate="collapsed" desc="Propiedades">

   /**
    * Constante para identificar que el tipo de operacion es un ticket
    */
   public static final int TICKET = 0;
   /**
    * Constante para identificar que el tipo de operacion es una factura
    */
   public static final int FACTURA = 1;
   /**
    * Constante para identificar que el tipo de operacion es un recibo
    */
   public static final int RECIBOS = 2;
   /**
    * Constante para identificar que el tipo de operacion es un recibo de pago a
    * proveedores
    */
   public static final int RECIBOS_PROV = 3;
   /**
    * Constante para identificar que el tipo de operacion es una orden de compra
    */
   public static final int OCOMPRA = 4;
   /**
    * Constante para identificar que el tipo de operacion es un movimiento al
    * inventario
    */
   public static final int INVENTARIO = 5;
   /**
    * Constante para identificar que el tipo de operacion es una nota de credito
    */
   public static final int NCREDITO = 6;
   /**
    * Constante para identificar que el tipo de operacion es una cuenta por
    * pagar
    */
   public static final int CXPAGAR = 7;
   /**
    * Constante para identificar que el tipo de operacion es un pedido
    */
   public static final int PEDIDOS = 8;
   /**
    * Constante para identificar que el tipo de operacion es una cotizacion
    */
   public static final int COTIZACIONES = 9;
   /**
    * Constante para identificar que el tipo de operacion es un recibo de
    * nominas
    */
   public static final int RECIBO_NOMINA = 10;
   /**
    * Constante para identificar que el tipo de operacion es un recibo de
    * nominas
    */
   public static final int RETENCIONES = 11;
   /**
    * Constante para identificar que el tipo de operacion es una nota de cargo
    */
   public static final int NOTA_CARGO = 12;
   /**
    * Constante para identificar que el tipo de operacion es una nota de cargo de proveedor
    */
   public static final int NOTA_CARGO_PROV = 13;
   
   /**
    * Generar Folio para la impresion de diplomas para COFIDE
    */
   public static final int COFIDE_FOLIO = 14;
   
   protected int intNumDigitos = 7;
   protected Bitacora bitacora;
   protected boolean bolEsLocal = false;
   protected boolean bolEsFolioSucursal = false;
   protected int intSucId = 0;
   public String strSerie;

   /**
    * Nos regresa el numero de digitos a usar para rellenar el folio
    *
    * @return regresa un entero con el numero de digitos del folio
    */
   public int getIntNumDigitos() {
      return intNumDigitos;
   }

   public String getStrSerie() {
      return strSerie;
   }

   public void setStrSerie(String strSerie) {
      this.strSerie = strSerie;
   }

   /**
    * Define el numero de digitos a usar para rellenar el folio
    *
    * @param intNumDigitos es un entero con el numero de digitos del folio
    */
   public void setIntNumDigitos(int intNumDigitos) {
      this.intNumDigitos = intNumDigitos;
   }

   /**
    * Nos dice si estamos en modo local
    *
    * @return Es un valor boolean
    */
   public boolean isBolEsLocal() {
      return bolEsLocal;
   }

   /**
    * Define si esta en modo local
    *
    * @param bolEsLocal Es un valor boolean
    */
   public void setBolEsLocal(boolean bolEsLocal) {
      this.bolEsLocal = bolEsLocal;
   }

   public boolean isBolEsFolioSucursal() {
      return bolEsFolioSucursal;
   }

   /**
    * Indica que se generara un folio por sucursal
    *
    * @param bolEsFolioSucursal
    */
   public void setBolEsFolioSucursal(boolean bolEsFolioSucursal) {
      this.bolEsFolioSucursal = bolEsFolioSucursal;
   }

   public int getIntSucId() {
      return intSucId;
   }

   /**
    * Indica el id de la sucursal para los folios
    *
    * @param intSucId
    */
   public void setIntSucId(int intSucId) {
      this.intSucId = intSucId;
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   /**
    * Constructor de los folios
    */
   public Folios() {
      this.bitacora = new Bitacora();
      this.bolEsFolioSucursal = false;
   }
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Metodos">
   /**
    * Este metodo genera los folios
    *
    * @param oConn Es la conexion
    * @param intTipo Es el tipo de folio a generar TICKET FACTURA RECIBO DE
    * COBROS RECIBO DE PAGOS ETC
    * @param bolGlobal Indica si es un folio global
    * @param intIdEmp Es el id de la empresa/o es el id de la sucursal en los
    * otros folios(tickets, almacen, etc)
    * @return Regresa una cadena con el folio nuevo
    */
   public String doFolio(Conexion oConn, int intTipo, boolean bolGlobal, int intIdEmp) {
      String strFolio = "";
      String strNomTablaFolios = getNomTabla(intTipo, intIdEmp, bolGlobal);
      //Buscamos si existe la tabla de folios
      if (!this.bolEsLocal) {
         ValidaExiste(strNomTablaFolios, oConn);
      }
      //show tables like "vta_folioticket";
      String strSqlNva = "insert into " + strNomTablaFolios + "(FOL_TRX)values(0)";
      int intNvoFolio = 0;
      if (this.bolEsLocal) {
         oConn.runQueryLMD(strSqlNva);
         strSqlNva = "values IDENTITY_VAL_LOCAL()";
         try {
            ResultSet rs = oConn.runQuery(strSqlNva, true);
            while (rs.next()) {
               intNvoFolio = rs.getInt(1);
            }
            rs.close();
         } catch (SQLException ex) {
            System.out.println(" " + ex.getMessage());
         }
      } else {
         oConn.runQueryLMD(strSqlNva);
         strSqlNva = "select @@identity";
         try {
            ResultSet rs = oConn.runQuery(strSqlNva, true);
            while (rs.next()) {
               intNvoFolio = rs.getInt(1);
            }
            rs.close();
         } catch (SQLException ex) {
            System.out.println(" " + ex.getMessage());
         }
      }

      strFolio = formateaFolio(intNvoFolio, this.intNumDigitos);
      return strFolio;
   }

   /**
    * Formatea el folio
    */
   private String formateaFolio(int intFolio, int intDigitos) {
      String strNvoFolio = "";
      for (int i = 1; i <= intDigitos; i++) {
         strNvoFolio += "0";
      }
      strNvoFolio += intFolio;
      strNvoFolio = strNvoFolio.substring(strNvoFolio.length() - intDigitos, strNvoFolio.length());
      return strNvoFolio;
   }

   /**
    * Valida si existe la tabla de folios
    */
   private void ValidaExiste(String strNomTablaFolios, Conexion oConn) {
      String strSql = "show tables like '" + strNomTablaFolios + "'";
      boolean bolFind = false;
      try {
         ResultSet rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            bolFind = true;
         }
         rs.close();
      } catch (SQLException ex) {
         ex.printStackTrace();
      }
      if (!bolFind) {
         String strCreate = "CREATE TABLE  " + strNomTablaFolios + " (  "
            + "FOL_ID int(10) unsigned NOT NULL AUTO_INCREMENT,  "
            + "FOL_TRX int(10) unsigned NOT NULL,  "
            + "PRIMARY KEY (FOL_ID)"
            + ") ENGINE=InnoDB DEFAULT CHARSET=utf8";
         oConn.runQueryLMD(strCreate);
      }
   }

   /**
    * Regresa el nombre de la tabla de folios por aplicar
    */
   private String getNomTabla(int intTipo, int intIdEmp, boolean bolGlobal) {
      String strNomTablaFolios = "vta_folio";
      switch (intTipo) {
         case Folios.TICKET:
            strNomTablaFolios += "ticket";
            break;
         case Folios.FACTURA:
            bolGlobal = false;
            strNomTablaFolios += "factura";
            break;
         case Folios.INVENTARIO:
            strNomTablaFolios += "inventario";
            break;
         case Folios.RECIBOS:
            strNomTablaFolios += "recibos";
            break;
         case Folios.OCOMPRA:
            strNomTablaFolios += "ocompra";
            break;
         case Folios.RECIBOS_PROV:
            strNomTablaFolios += "recibosprov";
            break;
         case Folios.NCREDITO:
            strNomTablaFolios += "ncredito";
            break;
         case Folios.CXPAGAR:
            strNomTablaFolios += "cxpagar";
            break;
         case Folios.PEDIDOS:
            strNomTablaFolios += "pedidos";
            break;
         case Folios.COTIZACIONES:
            strNomTablaFolios += "cotiza";
            break;
         case Folios.RECIBO_NOMINA:
            strNomTablaFolios += "nomina";
            break;
         case Folios.RETENCIONES:
            strNomTablaFolios += "retenciones";
            break;
         case Folios.NOTA_CARGO:
            strNomTablaFolios += "notacargo";
            break;
         case Folios.NOTA_CARGO_PROV:
            strNomTablaFolios += "notacargoprov";
            break;
         case Folios.COFIDE_FOLIO:
            strNomTablaFolios += "cofide_folio";
            break;
      }
      if (!bolGlobal) {
         strNomTablaFolios += intIdEmp;
      }
      if (this.bolEsFolioSucursal) {
         strNomTablaFolios += "_" + this.intSucId;
      }
      if (strSerie != null) {
         strNomTablaFolios += getStrSerie();
      }
      return strNomTablaFolios;
   }

   /**
    * Este metodo resetea los folios
    *
    * @param oConn Es la conexion
    * @param intTipo Es el tipo de folio a generar TICKET FACTURA RECIBO DE
    * COBROS RECIBO DE PAGOS ETC
    * @param bolGlobal Indica si es un folio global
    * @param intIdEmp Es el id de la sucursal
    * @return Regresa una cadena con el folio nuevo
    */
   public String resetFolio(Conexion oConn, int intTipo, boolean bolGlobal, int intIdEmp) {
      String strRes = "OK";
      String strNomTablaFolios = getNomTabla(intTipo, intIdEmp, bolGlobal);
      //Buscamos si existe la tabla de folios
      if (!this.bolEsLocal) {
         ValidaExiste(strNomTablaFolios, oConn);
      }
      String strInit = "truncate " + strNomTablaFolios + ";";
      oConn.runQueryLMD(strInit);
      return strRes;
   }

   /**
    * Este metodo actualiza el consecutivo del folio una vez tecleado por el
    * usuario
    *
    * @param oConn Es la conexion
    * @param intTipo Es el tipo de folio a generar TICKET FACTURA RECIBO DE
    * COBROS RECIBO DE PAGOS ETC
    * @param strNvoFolio Es el nuevo folio
    * @param bolGlobal Indica si es un folio global
    * @param intIdEmp Es el id de la sucursal
    * @return Regresa una cadena con el folio nuevo
    */
   public String updateFolio(Conexion oConn, int intTipo, String strNvoFolio, boolean bolGlobal, int intIdEmp) {
      String strRes = "OK";
      String strNomTablaFolios = getNomTabla(intTipo, intIdEmp, bolGlobal);
      if (!this.bolEsLocal) {
         //Buscamos si existe la tabla de folios
         ValidaExiste(strNomTablaFolios, oConn);
      }
      String strInit = "truncate " + strNomTablaFolios + ";";
      oConn.runQueryLMD(strInit);
      int intFolio = 0;
      try {
         intFolio = Integer.valueOf(strNvoFolio);
         strInit = "alter table " + strNomTablaFolios + " auto_increment = " + intFolio;
         oConn.runQueryLMD(strInit);
      } catch (NumberFormatException ex) {
         this.bitacora.GeneraBitacora(strInit + ex.getMessage(), oConn.getStrUsuario(), "updateFolio", oConn);
      }
      return strRes;
   }

   /**
    * Este metodo borra el folio indicado y regresa el autoincremental al valor
    * anterior
    *
    * @param oConn Es la conexion
    * @param intTipo Es el tipo de folio a generar TICKET FACTURA RECIBO DE
    * COBROS RECIBO DE PAGOS ETC
    * @param strLostFolio Es el folio perdido
    * @param bolGlobal Indica si es un folio global
    * @param intIdEmp Es el id de la sucursal
    * @return Regresa una cadena con el folio nuevo
    */
   public String recoveryFolioLost(Conexion oConn, int intTipo, String strLostFolio, boolean bolGlobal, int intIdEmp) {
      String strRes = "OK";
      String strNomTablaFolios = getNomTabla(intTipo, intIdEmp, bolGlobal);
      int intFolio = 0;
      try {
         intFolio = Integer.valueOf(strLostFolio);
         //Borramos el registro
         String strInit = "delete from " + strNomTablaFolios + " where FOL_ID = " + intFolio;
         oConn.runQueryLMD(strInit);
         intFolio--;
         //Cambiamos el autoincremental al folio anterior
         strInit = "alter table " + strNomTablaFolios + " auto_increment = " + intFolio;
         oConn.runQueryLMD(strInit);
      } catch (NumberFormatException ex) {
         this.bitacora.GeneraBitacora(ex.getMessage(), oConn.getStrUsuario(), "recoveryFolio", oConn);
      }
      return strRes;
   }

   /**
    * Obtiene el siguiente folio de la operación
    *
    * @param oConn Es la conexión
    * @param intTipo Es el tipo de documento
    * @param bolGlobal Indica si el folio es global
    * @param intIdEmp Es el id de la empresa
    * @return Regresa el siguiente numero de folio
    */
   public String getNextFolio(Conexion oConn, int intTipo, boolean bolGlobal, int intIdEmp) {
      String strFolio = "";
      String strNomTablaFolios = getNomTabla(intTipo, intIdEmp, bolGlobal);
      //Buscamos si existe la tabla de folios
      if (!this.bolEsLocal) {
         ValidaExiste(strNomTablaFolios, oConn);
      }
      //show tables like "vta_folioticket";
      String strSqlNva = "select max(FOL_ID) as maximo FROM  " + strNomTablaFolios + "";
      int intNvoFolio = 0;
      try {
         ResultSet rs = oConn.runQuery(strSqlNva, true);
         while (rs.next()) {
            intNvoFolio = rs.getInt("maximo");
         }
         rs.close();
      } catch (SQLException ex) {
         System.out.println(" " + ex.getMessage());
      }

      //Aumentamos en 1 el ultimo folio
      intNvoFolio++;
      strFolio = formateaFolio(intNvoFolio, this.intNumDigitos);
      return strFolio;
   }
   // </editor-fold>
}
