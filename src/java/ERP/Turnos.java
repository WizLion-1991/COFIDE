/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ERP;

import Tablas.vta_turnos;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Utilerias.Fechas;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;

/**
 *Realiza las operaciones con los turnos
 * @author ZeusGalindo
 */
public class Turnos {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private static final  org.apache.logging.log4j.Logger log = LogManager.getLogger(Turnos.class.getName());
   private Conexion oConn;

   public Conexion getoConn() {
      return oConn;
   }

   public void setoConn(Conexion oConn) {
      this.oConn = oConn;
   }
   
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Constructores">
   
   public Turnos(){
   }
   
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Metodos">
   
   /**
    *Obtiene el turno de la sucursal indicada
    * @param intSC_ID Es el id de la sucursal
    * @return regresa el numero de turno actual
    */
   public int getTurn(int intSC_ID){
      int intTurno = 1;
      Fechas fecha = new Fechas();
      String strSql = "select MAX(TU_TURNO) as max_turno from vta_turnos "
              + "where TU_FECHA = '" + fecha.getFechaActual() + "' and SC_ID = " + intSC_ID;
      try {
         ResultSet rs = this.oConn.runQuery(strSql, false);
         while(rs.next()){
            intTurno = rs.getInt("max_turno");
         }
         rs.close();
         intTurno++;
      } catch (SQLException ex) {
         log.error(ex.getMessage());
      }
      if(intTurno == 0)intTurno=1;
      return intTurno;
   }
   
   /**
    *Cierra el turno actual
    * @param intTurno Es el numero de turno actual
    * @param intSC_ID Es el id de la sucursal
    * @param intEMP_ID Es el id de la empresa
    * @param intUsuario Es el id del usuario
    * @return Regresa true si todo fue exitoso
    */
   public boolean closeTurn(int intTurno,int intSC_ID, int intEMP_ID, int intUsuario){
      boolean bolOk = false;
      Fechas fecha = new Fechas();
      vta_turnos turno = new vta_turnos();
      turno.setFieldInt("SC_ID", intSC_ID);
      turno.setFieldInt("EMP_ID", intEMP_ID);
      turno.setFieldInt("TU_TURNO", intTurno);
      turno.setFieldInt("IDUSUARIO", intUsuario);
      turno.setFieldString("TU_FECHA", fecha.getFechaActual());
      turno.setFieldString("TU_HORA", fecha.getHoraActual());
      String strResp = turno.Agrega(oConn);
      if(strResp.equals("OK")){
         bolOk = true;
      }
      return bolOk;
   }
   // </editor-fold>
}
