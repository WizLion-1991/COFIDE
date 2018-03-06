package ERP;


import comSIWeb.Operaciones.Conexion;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Repara los lotes de los productos...
 *
 * @author ZeusGalindo
 */
public class ProductosLoteRepair {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   public void doAjuste(Conexion oConn) {
      String strSql = "select PR_ID,\n"
              + "(SELECT SUM(l.PL_EXISTENCIA) from vta_prodlote l where l.PR_ID = vta_movproddeta.PR_ID) AS TLOTE,\n"
              + "sum(MPD_ENTRADAS - MPD_SALIDAS) as TMOVIMIENTOS from vta_movprod,vta_movproddeta \n"
              + "where \n"
              + "vta_movprod.MP_ID = vta_movproddeta.MP_ID AND MP_ANULADO = 0 GROUP BY PR_ID\n"
              + "having TLOTE<>TMOVIMIENTOS;";

      try {
         ResultSet rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            int intPrdId = rs.getInt("PR_ID");
            //Si es negativo vaciamos
            if (rs.getDouble("TMOVIMIENTOS") <= 0) {
               String strUpdate = "update vta_prodlote set PL_EXISTENCIA = 0 where PR_ID = " + intPrdId;
               oConn.runQueryLMD(strUpdate);
            } else {
               //Buscamos los lotes que tienen existencia para disminuirlos
               double dblPorTener = rs.getDouble("TMOVIMIENTOS");
               double dblEntregados = 0;
               String strSql2 = "SELECT * FROM vta_prodlote WHERE PR_ID = " + intPrdId + " and PL_EXISTENCIA> 0 order by PL_ID desc";
               ResultSet rs2 = oConn.runQuery(strSql2, true);
               while (rs2.next()) {
                  int intPlId = rs2.getInt("PL_ID");
                  double dblExist = rs2.getDouble("PL_EXISTENCIA");
                  if (dblExist > dblPorTener) {
                     dblEntregados = dblPorTener;
                  } else {
                     dblEntregados = dblExist;
                  }
                  //Ya terminamos de ajustar
                  System.out.println("intPlId:" + intPlId);
                  System.out.println("dblEntregados:" + dblEntregados);
                  System.out.println("dblPorTener:" + dblPorTener);
                  String strUpdate = "update vta_prodlote set PL_EXISTENCIA = " + dblEntregados + " where PL_ID = " + intPlId;
                  System.out.println("strUpdate:" + strUpdate);
                  oConn.runQueryLMD(strUpdate);
                  if (dblEntregados == dblPorTener) {
                     System.out.println("Ya se ajusto el total ahora sea solo en ceros...");
                  }
                  dblPorTener = dblPorTener - dblEntregados;
                  dblEntregados = 0;

               }
               rs2.close();
            }

         }
         rs.close();
      } catch (SQLException ex) {
         Logger.getLogger(ProductosLoteRepair.class.getName()).log(Level.SEVERE, null, ex);
      }
   }
   // </editor-fold>
}
