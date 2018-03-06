package comSIWeb.ContextoApt;

import comSIWeb.ContextoApt.entidades.Permiso;
import comSIWeb.Operaciones.Conexion;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;

/**
 * Esta clase valida si un perfil tiene acceso a cierta opcion del sistema
 *
 * @author zeus
 */
public class CIP_Permiso {

   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(CIP_Permiso.class.getName());

   /**
    * Valida si el perfil de usuario tiene acceso al permiso
    *
    * @param intIdPermiso Es el id del permiso
    * @param intIdPerfil Es el Id del perfil
    * @param oConn Es la conexion a la base de datos
    * @return Nos regresa true si tenemos acceso
    */
   public static boolean ValidaPermiso(int intIdPermiso, int intIdPerfil, Conexion oConn) {
      boolean bolIsOk = false;
      ResultSet rs;
      String strSql = "select PFP_ID from perfiles_permisos "
              + "where PS_ID ='" + intIdPermiso + "'  and PF_ID = '" + intIdPerfil + "'";
      try {
         rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            bolIsOk = true;
         }
         //if(rs.getStatement() != null )rs.getStatement().close(); 
         rs.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage());
      }
      return bolIsOk;
   }

   /**
    * Nos regresa todos los permisos de un perfil
    *
    * @param intIdPerfil Es el Id del perfil
    * @param oConn Es la conexion a la base de datos
    * @return Nos regresa true si tenemos acceso
    */
   public static ArrayList<Integer> getPermiso(int intIdPerfil, Conexion oConn) {
      ArrayList<Integer> lstPermisos = new ArrayList<Integer>();
      ResultSet rs;
      String strSql = "select PS_ID from perfiles_permisos "
              + "where PF_ID = '" + intIdPerfil + "'";
      try {
         rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            lstPermisos.add(rs.getInt("PS_ID"));
         }
         //if(rs.getStatement() != null )rs.getStatement().close(); 
         rs.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage());
      }
      return lstPermisos;
   }

   /**
    * Nos regresa todos los permisos de un perfil
    *
    * @param intIdPerfil Es el Id del perfil
    * @param oConn Es la conexion a la base de datos
    * @return Nos regresa true si tenemos acceso
    */
   public ArrayList<Permiso> getListaPermisos(int intIdPerfil, Conexion oConn) {
      ArrayList<Permiso> lstPermisos = new ArrayList<Permiso>();
      ResultSet rs;
      String strSql = "SELECT permisos_sistema.PS_ID,perfiles.PF_ID,permisos_sistema.PS_DESCRIPCION, \n"
              + "	permisos_sistema.PS_ESPERMISO, \n"
              + "	permisos_sistema.PS_ESMENU, \n"
              + "	perfiles.PF_DESCRIPCION\n"
              + "FROM perfiles_permisos INNER JOIN perfiles ON perfiles_permisos.PF_ID = perfiles.PF_ID\n"
              + "	 INNER JOIN permisos_sistema ON perfiles_permisos.PS_ID = permisos_sistema.PS_ID\n"
              + "WHERE perfiles_permisos.PF_ID IN ( " + intIdPerfil + " )\n"
              + "\n"
              + "ORDER BY perfiles.PF_DESCRIPCION ASC, permisos_sistema.PS_SECCION ASC, permisos_sistema.PS_SUBSECCION ASC, permisos_sistema.PS_ORDEN ASC";
      try {
         rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            Permiso permiso = new Permiso();
            permiso.setIntIdPerfil(intIdPerfil);
            permiso.setIntIdPermiso(rs.getInt("PS_ID"));
            permiso.setStrDescripcionPermiso(rs.getString("PS_DESCRIPCION"));
            permiso.setStrDescripcionPerfil(rs.getString("PF_DESCRIPCION"));
            permiso.setBolEsMenu(false);
            permiso.setBolEsPermiso(false);
            if (rs.getInt("PS_ESPERMISO") == 1) {
               permiso.setBolEsPermiso(true);
            }
            if (rs.getInt("PS_ESMENU") == 1) {
               permiso.setBolEsMenu(true);
            }
            lstPermisos.add(permiso);
         }
         //if(rs.getStatement() != null )rs.getStatement().close(); 
         rs.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage());
      }
      return lstPermisos;
   }
   /**
    * Nos regresa todos los perfiles que usan un permiso en particular
    *
    * @param intIdPermiso Es el Id del permiso
    * @param oConn Es la conexion a la base de datos
    * @return Nos regresa true si tenemos acceso
    */
   public ArrayList<Permiso> getListaPermisosPerfiles(int intIdPermiso, Conexion oConn) {
      ArrayList<Permiso> lstPermisos = new ArrayList<Permiso>();
      ResultSet rs;
      String strSql = "SELECT permisos_sistema.PS_ID,perfiles.PF_ID,permisos_sistema.PS_DESCRIPCION, \n"
              + "	permisos_sistema.PS_ESPERMISO, \n"
              + "	permisos_sistema.PS_ESMENU, \n"
              + "	perfiles.PF_DESCRIPCION\n"
              + "FROM perfiles_permisos INNER JOIN perfiles ON perfiles_permisos.PF_ID = perfiles.PF_ID\n"
              + "	 INNER JOIN permisos_sistema ON perfiles_permisos.PS_ID = permisos_sistema.PS_ID\n"
              + "WHERE permisos_sistema.PS_ID IN ( " + intIdPermiso + " )\n"
              + "\n"
              + "ORDER BY perfiles.PF_DESCRIPCION ASC, permisos_sistema.PS_SECCION ASC, permisos_sistema.PS_SUBSECCION ASC, permisos_sistema.PS_ORDEN ASC";
      try {
         rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            Permiso permiso = new Permiso();
            permiso.setIntIdPerfil(rs.getInt("PF_ID"));
            permiso.setIntIdPermiso(intIdPermiso);
            permiso.setStrDescripcionPermiso(rs.getString("PS_DESCRIPCION"));
            permiso.setStrDescripcionPerfil(rs.getString("PF_DESCRIPCION"));
            permiso.setBolEsMenu(false);
            permiso.setBolEsPermiso(false);
            if (rs.getInt("PS_ESPERMISO") == 1) {
               permiso.setBolEsPermiso(true);
            }
            if (rs.getInt("PS_ESMENU") == 1) {
               permiso.setBolEsMenu(true);
            }
            lstPermisos.add(permiso);
         }
         //if(rs.getStatement() != null )rs.getStatement().close(); 
         rs.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage());
      }
      return lstPermisos;
   }
   
   /**
    * Nos regresa todos los usuariios que usan un permiso en particular
    *
    * @param intIdPermiso Es el Id del permiso
    * @param oConn Es la conexion a la base de datos
    * @return Nos regresa true si tenemos acceso
    */
   public ArrayList<Permiso> getListaPermisosPerfilesUsuarios(int intIdPermiso, Conexion oConn) {
      ArrayList<Permiso> lstPermisos = new ArrayList<Permiso>();
      ResultSet rs;
      String strSql = "SELECT permisos_sistema.PS_ID,perfiles.PF_ID,permisos_sistema.PS_DESCRIPCION, \n"
              + "	permisos_sistema.PS_ESPERMISO, \n"
              + "	permisos_sistema.PS_ESMENU, \n"
              + "	perfiles.PF_DESCRIPCION\n"
              + " ,usuarios.nombre_usuario"
              + "FROM perfiles_permisos INNER JOIN perfiles ON perfiles_permisos.PF_ID = perfiles.PF_ID\n"
              + "	 INNER JOIN permisos_sistema ON perfiles_permisos.PS_ID = permisos_sistema.PS_ID\n"
              + " INNER JOIN usuarios on usuarios.PERF_ID = perfiles.PF_ID "
              + "WHERE permisos_sistema.PS_ID IN ( " + intIdPermiso + " )\n"
              + "\n"
              + "ORDER BY perfiles.PF_DESCRIPCION ASC, permisos_sistema.PS_SECCION ASC, permisos_sistema.PS_SUBSECCION ASC, permisos_sistema.PS_ORDEN ASC";
      try {
         rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            Permiso permiso = new Permiso();
            permiso.setIntIdPerfil(rs.getInt("PF_ID"));
            permiso.setIntIdPermiso(intIdPermiso);
            permiso.setStrDescripcionPermiso(rs.getString("PS_DESCRIPCION"));
            permiso.setStrDescripcionPerfil(rs.getString("PF_DESCRIPCION"));
            permiso.setBolEsMenu(false);
            permiso.setBolEsPermiso(false);
            if (rs.getInt("PS_ESPERMISO") == 1) {
               permiso.setBolEsPermiso(true);
            }
            if (rs.getInt("PS_ESMENU") == 1) {
               permiso.setBolEsMenu(true);
            }
            lstPermisos.add(permiso);
         }
         //if(rs.getStatement() != null )rs.getStatement().close(); 
         rs.close();
      } catch (SQLException ex) {
         log.error(ex.getMessage());
      }
      return lstPermisos;
   }
}
