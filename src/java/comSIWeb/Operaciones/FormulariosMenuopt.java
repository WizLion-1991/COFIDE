package comSIWeb.Operaciones;

import comSIWeb.ContextoApt.CIP_Permiso;
import comSIWeb.ContextoApt.VariableSession;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * Representa la tabla FormulariosMenuopt
 */
public class FormulariosMenuopt extends TableMaster {

   private VariableSession varSesiones;

   public FormulariosMenuopt() {
      super("formularios_menuopt", "frmn_id", "", "");
      this.Fields.put("frmn_id", new Integer(0));
      this.Fields.put("frmn_titulo", "");
      this.Fields.put("frmn_link", "");
      this.Fields.put("frm_id", new Integer(0));
      this.Fields.put("frmn_icon", "");
      this.Fields.put("frmn_orden", new Integer(0));
      this.Fields.put("frmn_idtitle", "");
      this.Fields.put("fmrn_enabled", new Integer(0));
      this.Fields.put("fmrn_idPermiso", new Integer(0));
   }

   @Override
   public ArrayList<TableMaster> ObtenDatosVarios(String strCond, Conexion oConn) {
      ArrayList<TableMaster> lstObjetos = new ArrayList<TableMaster>();
      String strSql = "select * from " + this.NomTabla + " where " + strCond + "";
      try {
         ResultSet rs = oConn.runQuery(strSql, true);
         ResultSetMetaData rsmd = rs.getMetaData();
         int numberOfColumns = rsmd.getColumnCount();
         while (rs.next()) {
            TableMaster mast = null;
            mast = (TableMaster) this.clone();
            int intIdPermiso = rs.getInt("fmrn_idPermiso");
            /*Recorremos el hash Map con los campos*/
            TreeSet keys = new TreeSet(this.Fields.keySet());
            Iterator it = keys.iterator();
            while (it.hasNext()) {
               String strNomField = (String) it.next();
               boolean bolEncontro = false;
               for (int i = 1; i <= numberOfColumns; i++) {
                  if (rsmd.getColumnName(i).equals(strNomField)) {
                     /*System.out.println("strNomField " + strNomField + " getColumnName " + rsmd.getColumnName(i));
                     System.out.println(rsmd.getColumnTypeName(i) + " ");*/
                     if (rsmd.getColumnTypeName(i).toUpperCase().startsWith("INT")) {
                        int intValue = rs.getInt(strNomField);
                        mast.Fields.put(strNomField, intValue);
                     } else if (rsmd.getColumnTypeName(i).toUpperCase().startsWith("CLOB")) {
                        String strValue = rs.getString(strNomField);
                        mast.Fields.put(strNomField, strValue);
                     } else if (rsmd.getColumnTypeName(i).toUpperCase().startsWith("VARCHAR") || rsmd.getColumnTypeName(i).toUpperCase().startsWith("NVARCHAR")) {
                        String strValue = rs.getString(strNomField);
                        mast.Fields.put(strNomField, strValue);
                     } else if (rsmd.getColumnTypeName(i).toUpperCase().startsWith("DECIMAL") || rsmd.getColumnTypeName(i).toUpperCase().startsWith("NUMERIC") || rsmd.getColumnTypeName(i).toUpperCase().startsWith("BIGINT")) {
                        double dblValue = rs.getDouble(strNomField);
                        mast.Fields.put(strNomField, dblValue);
                     } else if (rsmd.getColumnTypeName(i).toUpperCase().startsWith("DOUBLE")) {
                        double dblValue = rs.getDouble(strNomField);
                        mast.Fields.put(strNomField, dblValue);
                     } else if (rsmd.getColumnTypeName(i).toUpperCase().startsWith("SMALLINT")) {
                        short intValue = rs.getShort(strNomField);
                        mast.Fields.put(strNomField, intValue);
                     } else if (rsmd.getColumnTypeName(i).toUpperCase().startsWith("TEXT") || rsmd.getColumnTypeName(i).toUpperCase().startsWith("NTEXT")) {
                        String strValue = rs.getString(strNomField);
                        mast.Fields.put(strNomField, strValue);
                     } else {
                        String strValue = rs.getString(strNomField);
                        mast.Fields.put(strNomField, strValue);
                     }
                     bolEncontro = true;
                  }
               }
               if (!bolEncontro) {
                  System.out.println("No encontro " + strNomField);
               }
            }
            //Validamos el permiso
            if (intIdPermiso != 0 && this.varSesiones != null) {
               if (!CIP_Permiso.ValidaPermiso(intIdPermiso, this.varSesiones.getIntIdPerfil(), oConn)) {
                  mast = null;
               }
            }
            if (mast != null) {
               lstObjetos.add(mast);
            }
         }
         if (rs.getStatement() != null) {
            //rs.getStatement().close();
         }
         rs.close();
      } catch (SQLException ex) {
         ex.printStackTrace();
         bitacora.GeneraBitacora(oConn.getStrMsgError(), oConn.getStrUsuario(), "", oConn);
      }
      return lstObjetos;
   }

   /**
    * Define el objeto que maneja las variables de sesion
    *
    * @param varSesiones Es el objeto que maneja las variables de sesion
    */
   public void setVarSesiones(VariableSession varSesiones) {
      this.varSesiones = varSesiones;
   }

}
