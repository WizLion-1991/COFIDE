package com.mx.siweb.erp.reportes;

import com.mx.siweb.erp.reportes.entities.ClienteAnual;
import comSIWeb.Operaciones.Conexion;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 *Contiene operaciones para generar reportes de ventas
 * @author zeus
 */
public class Ventas {
   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   //Hash Map de los clientes

   private HashMap lstCte;
   private Conexion oConn;
   private int intEmpresa;
   private int intAnio;
   private int intSucursal;
   private String strFiltroEmpresa;
   private String strFiltroSucursal;

   public HashMap getLstCte() {
      return lstCte;
   }

   public void setLstCte(HashMap lstCte) {
      this.lstCte = lstCte;
   }

   public int getIntAnio() {
      return intAnio;
   }

   public void setIntAnio(int intAnio) {
      this.intAnio = intAnio;
   }

   public int getIntEmpresa() {
      return intEmpresa;
   }

   public void setIntEmpresa(int intEmpresa) {
      this.intEmpresa = intEmpresa;
   }

   public int getIntSucursal() {
      return intSucursal;
   }

   public void setIntSucursal(int intSucursal) {
      this.intSucursal = intSucursal;
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   /**
    * Constructor default
    * @param oConn Es la conexion
    * @param intEmpresa Es el id de la empresa
    * @param intAnio Es el año
    * @param intSucursal  Es la sucursal
    */
   public Ventas(Conexion oConn, int intAnio, int intEmpresa, int intSucursal) {
      this.lstCte = new HashMap();
      this.oConn = oConn;
      this.intEmpresa = intEmpresa;
      this.intAnio = intAnio;
      strFiltroEmpresa = "";
      strFiltroSucursal = "";
      if (intEmpresa != 0) {
         strFiltroEmpresa = " AND f.EMP_ID = " + intEmpresa;
      }
      if (intSucursal != 0) {
         strFiltroSucursal = " AND f.SC_ID = " + intSucursal;
      }
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   /**
    * Regresa la lista de ventas anuales
    */
   public void getClientesAnual() {
      String strSql = null;
      // <editor-fold defaultstate="collapsed" desc="Obtener id de empresa que hara el ticket">
      strSql = "SELECT c.CT_ID,c.CT_RAZONSOCIAL "
              + " FROM vta_facturas f, vta_cliente c WHERE f.CT_ID =  c.CT_ID "
              + " AND LEFT(FAC_FECHA,4) = '" + intAnio + "' "
              + strFiltroEmpresa
              + strFiltroSucursal
              + " GROUP BY c.CT_ID,c.CT_RAZONSOCIAL";
      try {
         ResultSet rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            ClienteAnual cte = new ClienteAnual();
            cte.setIdCliente(rs.getInt("CT_ID"));
            cte.setStrNombre(rs.getString("CT_RAZONSOCIAL"));
            lstCte.put(rs.getInt("CT_ID"), cte);
         }
         rs.close();
         System.out.println(" " + lstCte.size());
      } catch (SQLException ex) {
         ex.fillInStackTrace();
      }
      // </editor-fold>
   }

   /**
    * Realice la consulta del reporte generico
    */
   public void doReportAnul() {
      // <editor-fold defaultstate="collapsed" desc="Obtenemos los importes">
      String strSql = "SELECT c.CT_ID,c.CT_RAZONSOCIAL,LEFT(f.FAC_FECHA,6) as Mes, "
              + " sum(FAC_IMPORTE) as timporte, "
              + " sum(FAC_IMPUESTO1) as timpuesto1, "
              + " sum(FAC_TOTAL) as ttotal, "
              + " sum(FAC_SALDO) as tsaldo "
              + " FROM vta_facturas f, vta_cliente c WHERE f.CT_ID =  c.CT_ID "
              + " AND LEFT(FAC_FECHA,4) = '" + intAnio + "' "
              + strFiltroEmpresa
              + strFiltroSucursal
              + " GROUP BY c.CT_ID,c.CT_RAZONSOCIAL,LEFT(f.FAC_FECHA,6);";
      try {
         ResultSet rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            if (lstCte.containsKey(rs.getInt("CT_ID"))) {
               ClienteAnual cte = (ClienteAnual) lstCte.get(rs.getInt("CT_ID"));
               // <editor-fold defaultstate="collapsed" desc="Enero">
               if (rs.getString("Mes").endsWith("01")) {
                  cte.getMap1().put("timporte", rs.getDouble("timporte"));
                  cte.getMap1().put("timpuesto1", rs.getDouble("timpuesto1"));
                  cte.getMap1().put("ttotal", rs.getDouble("ttotal"));
                  cte.getMap1().put("tsaldo", rs.getDouble("tsaldo"));
               }
               // </editor-fold>
               // <editor-fold defaultstate="collapsed" desc="Febrero">
               if (rs.getString("Mes").endsWith("02")) {
                  cte.getMap2().put("timporte", rs.getDouble("timporte"));
                  cte.getMap2().put("timpuesto1", rs.getDouble("timpuesto1"));
                  cte.getMap2().put("ttotal", rs.getDouble("ttotal"));
                  cte.getMap2().put("tsaldo", rs.getDouble("tsaldo"));
               }
               // </editor-fold>
               // <editor-fold defaultstate="collapsed" desc="Marzo">
               if (rs.getString("Mes").endsWith("03")) {
                  cte.getMap3().put("timporte", rs.getDouble("timporte"));
                  cte.getMap3().put("timpuesto1", rs.getDouble("timpuesto1"));
                  cte.getMap3().put("ttotal", rs.getDouble("ttotal"));
                  cte.getMap3().put("tsaldo", rs.getDouble("tsaldo"));
               }
               // </editor-fold>
               // <editor-fold defaultstate="collapsed" desc="Abril">
               if (rs.getString("Mes").endsWith("04")) {
                  cte.getMap4().put("timporte", rs.getDouble("timporte"));
                  cte.getMap4().put("timpuesto1", rs.getDouble("timpuesto1"));
                  cte.getMap4().put("ttotal", rs.getDouble("ttotal"));
                  cte.getMap4().put("tsaldo", rs.getDouble("tsaldo"));
               }
               // </editor-fold>
               // <editor-fold defaultstate="collapsed" desc="Mayo">
               if (rs.getString("Mes").endsWith("05")) {
                  cte.getMap5().put("timporte", rs.getDouble("timporte"));
                  cte.getMap5().put("timpuesto1", rs.getDouble("timpuesto1"));
                  cte.getMap5().put("ttotal", rs.getDouble("ttotal"));
                  cte.getMap5().put("tsaldo", rs.getDouble("tsaldo"));
               }
               // </editor-fold>
               // <editor-fold defaultstate="collapsed" desc="Junio">
               if (rs.getString("Mes").endsWith("06")) {
                  cte.getMap6().put("timporte", rs.getDouble("timporte"));
                  cte.getMap6().put("timpuesto1", rs.getDouble("timpuesto1"));
                  cte.getMap6().put("ttotal", rs.getDouble("ttotal"));
                  cte.getMap6().put("tsaldo", rs.getDouble("tsaldo"));
               }
               // </editor-fold>
               // <editor-fold defaultstate="collapsed" desc="Julio">
               if (rs.getString("Mes").endsWith("07")) {
                  cte.getMap7().put("timporte", rs.getDouble("timporte"));
                  cte.getMap7().put("timpuesto1", rs.getDouble("timpuesto1"));
                  cte.getMap7().put("ttotal", rs.getDouble("ttotal"));
                  cte.getMap7().put("tsaldo", rs.getDouble("tsaldo"));
               }
               // </editor-fold>
               // <editor-fold defaultstate="collapsed" desc="Agosto">
               if (rs.getString("Mes").endsWith("08")) {
                  cte.getMap8().put("timporte", rs.getDouble("timporte"));
                  cte.getMap8().put("timpuesto1", rs.getDouble("timpuesto1"));
                  cte.getMap8().put("ttotal", rs.getDouble("ttotal"));
                  cte.getMap8().put("tsaldo", rs.getDouble("tsaldo"));
               }
               // </editor-fold>
               // <editor-fold defaultstate="collapsed" desc="Septiembre">
               if (rs.getString("Mes").endsWith("09")) {
                  cte.getMap9().put("timporte", rs.getDouble("timporte"));
                  cte.getMap9().put("timpuesto1", rs.getDouble("timpuesto1"));
                  cte.getMap9().put("ttotal", rs.getDouble("ttotal"));
                  cte.getMap9().put("tsaldo", rs.getDouble("tsaldo"));
               }
               // </editor-fold>
               // <editor-fold defaultstate="collapsed" desc="Octubre">
               if (rs.getString("Mes").endsWith("10")) {
                  cte.getMap10().put("timporte", rs.getDouble("timporte"));
                  cte.getMap10().put("timpuesto1", rs.getDouble("timpuesto1"));
                  cte.getMap10().put("ttotal", rs.getDouble("ttotal"));
                  cte.getMap10().put("tsaldo", rs.getDouble("tsaldo"));
               }
               // </editor-fold>
               // <editor-fold defaultstate="collapsed" desc="Noviembre">
               if (rs.getString("Mes").endsWith("11")) {
                  cte.getMap11().put("timporte", rs.getDouble("timporte"));
                  cte.getMap11().put("timpuesto1", rs.getDouble("timpuesto1"));
                  cte.getMap11().put("ttotal", rs.getDouble("ttotal"));
                  cte.getMap11().put("tsaldo", rs.getDouble("tsaldo"));
               }
               // </editor-fold>
               // <editor-fold defaultstate="collapsed" desc="Diciembre">
               if (rs.getString("Mes").endsWith("12")) {
                  cte.getMap12().put("timporte", rs.getDouble("timporte"));
                  cte.getMap12().put("timpuesto1", rs.getDouble("timpuesto1"));
                  cte.getMap12().put("ttotal", rs.getDouble("ttotal"));
                  cte.getMap12().put("tsaldo", rs.getDouble("tsaldo"));
               }
               // </editor-fold>
            }
         }
         rs.close();
      } catch (SQLException ex) {
         ex.fillInStackTrace();
      }
      // </editor-fold>
   }
   // </editor-fold>
}
