package ERP;

import comSIWeb.Operaciones.Conexion;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;

/**
 * Esta clase realiza las operaciones de calculo de conversiones emtre monedas
 *
 * @author aleph_79
 */
public class Monedas {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private Conexion oConn;
   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(Monedas.class.getName());
   private int intMonedaBase;
   private boolean boolConversionAutomatica;

    public boolean isBoolConversionAutomatica() {
        return boolConversionAutomatica;
    }

    public void setBoolConversionAutomatica(boolean boolConversionAutomatica) {
        this.boolConversionAutomatica = boolConversionAutomatica;
    }

    public int getIntMonedaBase() {
        return intMonedaBase;
    }

    public void setIntMonedaBase(int intMonedaBase) {
        this.intMonedaBase = intMonedaBase;
    }

   public Conexion getoConn() {
      return oConn;
   }

   public void setoConn(Conexion oConn) {
      this.oConn = oConn;
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   public Monedas(Conexion oConn) {
      this.oConn = oConn;
      this.intMonedaBase = 0;
      this.boolConversionAutomatica = true;
   }
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Metodos">
   /**
    *
    * @param intTipoConversion Es el tipo de paridad cambiar 1, contado, 2.., 4
    * diario oficial
    * @param intMonedaBase Es la moneda base de la conversion(origen)
    * @param intMonedaFinal Es la moneda destino
    * @return Regresa el factor de conversion
    */
   public double GetFactorConversion(int intTipoConversion, int intMonedaBase, int intMonedaFinal) {
      return GetFactorConversion("", intTipoConversion, intMonedaBase, intMonedaFinal);
   }

   /**
    *
    * @param strFecha Es la fecha de la operacion
    * @param intTipoConversion Es el tipo de paridad cambiar 1, contado, 2.., 4
    * diario oficial
    * @param intMonedaBase Es la moneda base de la conversion(origen)
    * @param intMonedaFinal Es la moneda destino
    * @return Regresa el factor de conversion
    */
   public double GetFactorConversion(String strFecha, int intTipoConversion, int intMonedaBase, int intMonedaFinal) {
      double dblFactor = 0;
      
      //Construimos consulta
      StringBuilder strSql = new StringBuilder("select TC_PARIDAD,TC_MONEDA1,TC_MONEDA2,TC_FECHA from vta_tasacambio where ");
      strSql.append("((TC_MONEDA1 = ").append(intMonedaBase).append(" and TC_MONEDA2=").append(intMonedaFinal).append(") or ");
      strSql.append("(TC_MONEDA2 = ").append(intMonedaBase).append(" and TC_MONEDA1=").append(intMonedaFinal);
      strSql.append(")) " + " and TTC_ID = ").append(intTipoConversion);  
      
      //si la fecha no esta vacia
      if (strFecha != null) {
         if (!strFecha.isEmpty()) {
            strSql.append(" and TC_FECHA <= ").append(strFecha);
         }
      }
      strSql.append(" order by TC_FECHA DESC limit 0,2");
      try {
         ResultSet rs = this.oConn.runQuery(strSql.toString(), true);
         while (rs.next()) {
            //Factor para conversión de izquierda a derecha, Moneda extranjera moneda nacional
            dblFactor = rs.getDouble("TC_PARIDAD");
            log.debug(rs.getString("TC_FECHA") + " " + dblFactor);
            //Validamos si es una conversion a la inversa
            //es decir que de moneda nacional a moneda extranjera
            //derecha a izquierda , dividimos 1 entre el factor
            if (rs.getInt("TC_MONEDA1") != intMonedaBase && this.boolConversionAutomatica) {
               dblFactor = 1 / dblFactor;
            }
            this.intMonedaBase = rs.getInt("TC_MONEDA1");
            break;
         }
         rs.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage());
      }
      //si el factor es cero y tenemos como parametro una fecha buscamos en todas las fechas a las mas recientes
      if (dblFactor == 0 && !strFecha.isEmpty()) {
         strSql = new StringBuilder("select TC_PARIDAD,TC_MONEDA1,TC_MONEDA2 from vta_tasacambio where ");
         strSql.append("((TC_MONEDA1 = ").append(intMonedaBase).append(" and TC_MONEDA2=").append(intMonedaFinal).append(") or ");
         strSql.append("(TC_MONEDA2 = ").append(intMonedaBase).append(" and TC_MONEDA1=").append(intMonedaFinal);
         strSql.append(")) " + " and TTC_ID = ").append(intTipoConversion);
         //si la fecha no esta vacia
         if (!strFecha.isEmpty()) {
            strSql.append(" and TC_FECHA >= ").append(strFecha);
         }
         strSql.append(" order by TC_FECHA ASC limit 0,2");
         try {
            ResultSet rs = this.oConn.runQuery(strSql.toString(), true);
            while (rs.next()) {
               //Factor para conversión de izquierda a derecha, Moneda extranjera moneda nacional
               dblFactor = rs.getDouble("TC_PARIDAD");
               //Validamos si es una conversion a la inversa
               //es decir que de moneda nacional a moneda extranjera
               //derecha a izquierda , dividimos 1 entre el factor
               if (rs.getInt("TC_MONEDA1") != intMonedaBase && this.boolConversionAutomatica) {
                  dblFactor = 1 / dblFactor;
               }
               this.intMonedaBase = rs.getInt("TC_MONEDA1");

               break;
            }
            rs.close();
         } catch (SQLException ex) {
            log.error(ex.getMessage());
         }
      }
      //si el factor es cero buscamos nuevamente en todos los tipos de cambio
      if (dblFactor == 0) {
         strSql = new StringBuilder("select TC_PARIDAD,TC_MONEDA1,TC_MONEDA2 from vta_tasacambio where ");
         strSql.append("((TC_MONEDA1 = ").append(intMonedaBase).append(" and TC_MONEDA2=").append(intMonedaFinal).append(") or ");
         strSql.append("(TC_MONEDA2 = ").append(intMonedaBase).append(" and TC_MONEDA1=").append(intMonedaFinal);
         strSql.append(")) order by TC_FECHA DESC limit 0,2");
         try {
            ResultSet rs = this.oConn.runQuery(strSql.toString(), true);
            while (rs.next()) {
               //Factor para conversión de izquierda a derecha, Moneda extranjera moneda nacional
               dblFactor = rs.getDouble("TC_PARIDAD");
               //Validamos si es una conversion a la inversa
               //es decir que de moneda nacional a moneda extranjera
               //derecha a izquierda , dividimos 1 entre el factor
               if (rs.getInt("TC_MONEDA1") != intMonedaBase && this.boolConversionAutomatica) {
                  dblFactor = 1 / dblFactor;
               }
               this.intMonedaBase = rs.getInt("TC_MONEDA1");
               break;
            }
            rs.close();
         } catch (SQLException ex) {
            log.error(ex.getMessage());
         }
      }
      //si el factor es cero buscamos la moneda en con otra moneda
      if (dblFactor == 0) {
         //Buscamos con que monedas tiene conversion la moneda destino
         String strSql3 = "select TC_MONEDA1,TC_MONEDA2,TC_PARIDAD from vta_tasacambio "
                 + " where TC_MONEDA1 = " + intMonedaFinal + " OR TC_MONEDA2 =  " + intMonedaFinal + " limit 0,2";
         ResultSet rs3;
         log.debug("Buscamos con que monedas tiene conversion ---");
         try {
            rs3 = oConn.runQuery(strSql3, true);
            while (rs3.next()) {
               int intMonedaOrigen3 = rs3.getInt("TC_MONEDA1");
               int intMonedaDest3 = rs3.getInt("TC_MONEDA2");
               double dblFactorExtra = rs3.getDouble("TC_PARIDAD");
               //Recorremos cada moneda compatible para buscar conversion con la moneda origen
               if (intMonedaFinal != intMonedaOrigen3) {
                  log.debug("La moneda destino " + intMonedaFinal + " es compatible con la moneda  " + intMonedaOrigen3);
                  //Buscamos si hay conversion con la moneda original
                  strSql = new StringBuilder("select TC_PARIDAD,TC_MONEDA1,TC_MONEDA2 from vta_tasacambio where ");
                  strSql.append("((TC_MONEDA1 = ").append(intMonedaBase).append(" and TC_MONEDA2=").append(intMonedaOrigen3).append(") or ");
                  strSql.append("(TC_MONEDA2 = ").append(intMonedaBase).append(" and TC_MONEDA1=").append(intMonedaOrigen3);
                  strSql.append(")) order by TC_FECHA DESC limit 0,2");
                  ResultSet rs4;
                  rs4 = oConn.runQuery(strSql.toString(), true);
                  while (rs4.next()) {
                     //Si hay conversion
                     log.debug("Si hay conversion con la moneda ");
                     dblFactor = rs4.getDouble("TC_PARIDAD");
                     if (rs4.getInt("TC_MONEDA1") != intMonedaBase && this.boolConversionAutomatica) {
                        dblFactor = 1 / dblFactor;
                     }
                     log.debug("Factor de " + intMonedaOrigen3 + " a " + intMonedaBase + " " + dblFactor);
                     if (rs3.getInt("TC_MONEDA1") != intMonedaOrigen3 && this.boolConversionAutomatica) {
                        dblFactorExtra = 1 / dblFactorExtra;
                     }
                     this.intMonedaBase = rs3.getInt("TC_MONEDA1");
                     log.debug("Factor de " + intMonedaFinal + " a " + intMonedaOrigen3 + " " + dblFactorExtra);
                     dblFactor = dblFactorExtra * dblFactor;
                     rs3.afterLast();
                     break;
                  }
                  rs4.close();
               } else {
                  log.debug("La moneda destino " + intMonedaFinal + " es compatible con la moneda  " + intMonedaDest3);
                  //Buscamos si hay conversion con la moneda original
                  strSql = new StringBuilder("select TC_PARIDAD,TC_MONEDA1,TC_MONEDA2 from vta_tasacambio where ");
                  strSql.append("((TC_MONEDA1 = ").append(intMonedaBase).append(" and TC_MONEDA2=").append(intMonedaDest3).append(") or ");
                  strSql.append("(TC_MONEDA2 = ").append(intMonedaBase).append(" and TC_MONEDA1=").append(intMonedaDest3);
                  strSql.append(")) order by TC_FECHA DESC limit 0,2");
                  ResultSet rs4;
                  rs4 = oConn.runQuery(strSql.toString(), true);
                  while (rs4.next()) {
                     //Si hay conversion
                     log.debug("Si hay conversion con la moneda tercera");
                     dblFactor = rs4.getDouble("TC_PARIDAD");
                     if (rs4.getInt("TC_MONEDA1") != intMonedaBase && this.boolConversionAutomatica) {
                        dblFactor = 1 / dblFactor;
                     }
                     log.debug("Factor de " + intMonedaDest3 + " a " + intMonedaBase + " " + dblFactor);
                     if (rs3.getInt("TC_MONEDA1") != intMonedaDest3 && this.boolConversionAutomatica) {
                        dblFactorExtra = 1 / dblFactorExtra;
                     }
                     this.intMonedaBase = rs3.getInt("TC_MONEDA1");
                     log.debug("Factor de " + intMonedaFinal + " a " + intMonedaOrigen3 + " " + dblFactorExtra);
                     dblFactor = dblFactorExtra * dblFactor;
                     rs3.afterLast();
                     break;
                  }
                  rs4.close();
               }
            }
            rs3.close();
         } catch (SQLException ex) {
            log.error(ex.getMessage());
         }


      }
      return dblFactor;
   }
   // </editor-fold>
}
