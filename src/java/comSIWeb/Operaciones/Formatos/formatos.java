package comSIWeb.Operaciones.Formatos;

import comSIWeb.Operaciones.Conexion;
import comSIWeb.Operaciones.TableMaster;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.TreeSet;

/**
 *Representa un formato de impresion
 * @author zeus
 */
public class formatos extends TableMaster {

   /**
    * Instancia un formato
    */
   public formatos() {
      super("formatos", "FM_ID", "", "");
      this.Fields.put("FM_ID", new Integer(0));
      this.Fields.put("FM_NOMBRE", "");
      this.Fields.put("FM_TOP", new Integer(0));
      this.Fields.put("FM_LEFT", new Integer(0));
      this.Fields.put("FM_BOTTOM", new Integer(0));
      this.Fields.put("FM_RIGHT", new Integer(0));
      this.Fields.put("FM_WIDTH", new Integer(0));
      this.Fields.put("FM_HEIGHT", new Integer(0));
      this.Fields.put("FM_TIPOHOJA", "");
      this.Fields.put("FM_ORIENTACION", new Integer(0));
      this.Fields.put("FM_NUMITEMS", new Integer(0));
      this.Fields.put("FM_LLAVE", "");
      this.Fields.put("FM_ABRV", "");
      this.Fields.put("FM_DETAFACTOR", new Integer(0));
      this.Fields.put("FM_COMFACTOR", new Integer(0));
      this.Fields.put("FM_INIMIDDLE", new Integer(0));
      this.Fields.put("FM_INIMIDDLEY", new Integer(0));
      this.Fields.put("FM_NOEVALMAXROWS", new Integer(0));
      this.Fields.put("FM_POS_MIN_IN_Y", new Integer(0));
      this.Fields.put("FM_GEN_PAG_DETA", new Integer(0));
      this.Fields.put("FM_POS_INI_Y_PAG_DETA", new Integer(0));
      this.Fields.put("FM_MONEDA", "");
   }

 /**
    * Carga los datos de un registro en particular
    * @param strOpt Es la llave del formato
    * @param oConn Es la conexion a la base de datos
    * @return Nos regresa Ok en caso de que todo sea exitoso
    */
   public String ObtenDatos(String strOpt, Conexion oConn) {
      String strRes = "OK";
      String strSql = "select * from " + this.NomTabla + " where FM_ABRV = '" + strOpt + "'";
      try {
         ResultSet rs = oConn.runQuery(strSql, true);
         ResultSetMetaData rsmd = rs.getMetaData();
         int numberOfColumns = rsmd.getColumnCount();
         while (rs.next()) {
            /*Recorremos el hash Map con los campos*/
            TreeSet keys = new TreeSet(this.Fields.keySet());
            Iterator it = keys.iterator();
            while (it.hasNext()) {
               String strNomField = (String) it.next();
               boolean bolEncontro = false;
               for (int i = 1; i <= numberOfColumns; i++) {
                  if (rsmd.getColumnName(i).equals(strNomField)) {
                     /*System.out.println("strNomField " + strNomField + " getColumnName " + rsmd.getColumnName(i));
                     System.out.println(rsmd.getColumnTypeName(i) + " " + rsmd.getColumnTypeName(i).substring(0, 3));*/
                     if (rsmd.getColumnTypeName(i).toUpperCase().startsWith("VARCHAR")) {
                        String strValue = rs.getString(strNomField);
                        this.Fields.put(strNomField, strValue);
                     } else {
                        if (rsmd.getColumnTypeName(i).toUpperCase().startsWith("INT")) {
                           int intValue = rs.getInt(strNomField);
                           this.Fields.put(strNomField, intValue);
                        } else {
                           if (rsmd.getColumnTypeName(i).toUpperCase().startsWith("DECIMAL")) {
                              double dblValue = rs.getDouble(strNomField);
                              this.Fields.put(strNomField, dblValue);
                           } else {
                              if (rsmd.getColumnTypeName(i).toUpperCase().startsWith("DOUBLE")) {
                                 double dblValue = rs.getDouble(strNomField);
                                 this.Fields.put(strNomField, dblValue);
                              } else {
                                 if (rsmd.getColumnTypeName(i).toUpperCase().startsWith("SMALLINT")) {
                                    short intValue = rs.getShort(strNomField);
                                    this.Fields.put(strNomField, intValue);
                                 }
                              }
                           }
                        }
                     }
                     bolEncontro = true;
                  }
               }
               if (!bolEncontro) {
                  System.out.println("No encontro " + strNomField);
               }
            }

         }
      } catch (SQLException ex) {
         strRes = "ERROR:" + oConn.getStrMsgError();
         ex.printStackTrace();
         bitacora.GeneraBitacora(oConn.getStrMsgError(), oConn.getStrUsuario(), "", oConn);
      }
      return strRes;
   }
}
