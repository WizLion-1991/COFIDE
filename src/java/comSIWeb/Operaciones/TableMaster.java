package comSIWeb.Operaciones;

import comSIWeb.Utilerias.UtilXml;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *Objeto que representa todas las tablas de la base de datos, nos permite
 * insertar, actualizar, borrar y realizar mas tareas con las tablas de la base de datos
 * @author zeus
 */
public class TableMaster implements Cloneable {
   /*Arreglo asociativo con los campos*/

   protected HashMap Fields;
   public HashMap lstData;
   protected String NomTabla;
   protected String NomKey;
   protected String NomKey2;
   protected String NomKey3;
   protected String ValorKey;
   protected boolean bolGetAutonumeric;
   protected Bitacora bitacora;
   protected boolean bolStrict;//Indica si la generacion de SQL es estricto
   protected String strSepara;//Indica si la generacion de SQL es estricto
   protected String strComodinComillaSimple;
   private static final Logger log = LogManager.getLogger(TableMaster.class.getName());
   /*Construye el objeto generico que nos genera los querys*/

   public TableMaster(String NomTabla, String NomKey, String NomKey2, String NomKey3) {
      this.Fields = new HashMap();
      this.NomTabla = NomTabla;
      this.NomKey = NomKey;
      this.NomKey2 = NomKey2;
      this.NomKey3 = NomKey3;
      this.bolGetAutonumeric = false;
      this.bitacora = new Bitacora();
      this.ValorKey = "0";
      this.lstData = new HashMap();
      this.bolStrict = false;
      this.strSepara = "'";
      this.strComodinComillaSimple = "\\'";
   }
   /*Recupera el valor del campo numerico*/

   public int getFieldInt(String strName) {
      int intValor = 0;
      try {
         String strValor = String.valueOf(this.Fields.get(strName));
         intValor = Integer.valueOf(strValor);
      } catch (NumberFormatException e) {
      }
      return intValor;
   }
   /*Recupera el valor del campo numerico*/

   public long getFieldLong(String strName) {
      long lngValor = 0;
      try {
         String strValor = String.valueOf(this.Fields.get(strName));
         lngValor = Long.valueOf(strValor);
      } catch (NumberFormatException e) {
      }
      return lngValor;
   }
   /*Recupera el valor del campo numerico*/

   public int getFieldShort(String strName) {
      short intValor = 0;
      try {
         String strValor = String.valueOf(this.Fields.get(strName));
         intValor = Short.valueOf(strValor);
      } catch (NumberFormatException e) {
      }
      return intValor;
   }
   /*Recupera el valor del campo string*/

   public String getFieldString(String strName) {
      String strValor = "";
      try {
         strValor = String.valueOf(this.Fields.get(strName));
      } catch (NumberFormatException e) {
      }
      return strValor;
   }
   /*Recupera el valor del campo double*/

   public double getFieldDouble(String strName) {
      double dblValor = 0.0;
      try {
         String strValor = String.valueOf(this.Fields.get(strName));
         dblValor = Double.valueOf(strValor);
      } catch (NumberFormatException e) {
      }
      return dblValor;
   }

   /*Establece el valor del campo String*/
   public void setFieldString(String strName, String strValue) {
      this.Fields.put(strName, strValue);
   }

   /**Establece el valor del campo Int
    * @param strName Es el nombre del campo
    * @param intValue Es el valor del campo
    */
   public void setFieldInt(String strName, int intValue) {
      this.Fields.put(strName, intValue);
   }

   /**
    * Establece el valor del campo Long
    * @param strName Es el nombre del campo
    * @param lngValue Es el valor del campo
    */
   public void setFieldLong(String strName, long lngValue) {
      this.Fields.put(strName, lngValue);
   }
   /*Establece el valor del campo short*/

   public void setFieldShort(String strName, short intValue) {
      this.Fields.put(strName, intValue);
   }
   /*Establece el valor del campo Int*/

   public void setFieldDouble(String strName, double dblValue) {
      this.Fields.put(strName, dblValue);
   }
   /*Genera el strRes de insercion*/

   public String getSQLInsert() {
      String strSQL = "";
      String strSQLFields = "";
      String strSQLValues = "";
      /*Recorremos el hash Map con los campos*/
      TreeSet keys = new TreeSet(this.Fields.keySet());
      Iterator it = keys.iterator();
      while (it.hasNext()) {
         String strNomField = (String) it.next();
         if (!strNomField.toLowerCase().equals(this.NomKey.toLowerCase())) {
            strSQLFields += strNomField + ",";
            if (this.bolStrict) {
               try {
                  if (this.Fields.get(strNomField).getClass().getCanonicalName().equals("java.lang.String")) {
                     strSQLValues += "'" + String.valueOf(this.Fields.get(strNomField)).replace("'", strComodinComillaSimple) + "',";
                  } else {
                     strSQLValues += "" + String.valueOf(this.Fields.get(strNomField)).replace("'", strComodinComillaSimple) + ",";
                  }
               } catch (java.lang.NullPointerException ex) {
                  System.out.println("Null getInsert in " + strNomField + " " + ex.getMessage());
                  strSQLValues += "" + "" + ",";
               }

            } else {
               strSQLValues += "'" + String.valueOf(this.Fields.get(strNomField)).replace("'", strComodinComillaSimple) + "',";
            }
         }
      }

      if (!strSQLFields.equals("")) {
         strSQLFields = strSQLFields.substring(0, strSQLFields.length() - 1);
      }
      if (!strSQLValues.equals("")) {
         strSQLValues = strSQLValues.substring(0, strSQLValues.length() - 1);
      }
      strSQL = "insert into " + this.NomTabla + "(" + strSQLFields;
      strSQL += ")values(" + strSQLValues;
      strSQL += ")";
      return strSQL;
   }

   /**
    * Genera el SQL de actualizacion
    * @return Nos regresa una cadena con el SQL
    */
   public String getSQLUpdate() {
      String strSQL = "";
      String strSQLFields = "";
      String strKeyValue = "";

      /*Recorremos el hash Map con los campos*/
      TreeSet keys = new TreeSet(this.Fields.keySet());
      Iterator it = keys.iterator();
      while (it.hasNext()) {
         String strNomField = (String) it.next();
         if (!strNomField.toLowerCase().equals(this.NomKey.toLowerCase())) {
            //Validamos el tipo de dato
            if (this.bolStrict) {
               if (this.Fields.get(strNomField).getClass().getCanonicalName().equals("java.lang.String")) {
                  strSQLFields += " " + strNomField + " = '" + String.valueOf(this.Fields.get(strNomField)).replace("'", strComodinComillaSimple) + "',";
               } else {
                  strSQLFields += " " + strNomField + " = " + String.valueOf(this.Fields.get(strNomField)).replace("'", strComodinComillaSimple) + ",";
               }
            } else {
               strSQLFields += " " + strNomField + " = '" + String.valueOf(this.Fields.get(strNomField)).replace("'", strComodinComillaSimple) + "',";
            }
         } else {
            strKeyValue = String.valueOf(this.Fields.get(strNomField));
         }
      }
      if (!strSQLFields.equals("")) {
         strSQLFields = strSQLFields.substring(0, strSQLFields.length() - 1);
      }
      //Concatenamos instruccion de strRes
      strSQL = "update " + this.NomTabla + " set " + strSQLFields;
      strSQL += " where  " + this.NomKey + " = " + strSepara + strKeyValue + strSepara + "";
      return strSQL;
   }

   /**
    *Genera el SQL de borrado 
    * @return Nos regresa una cadena con el SQL
    */
   public String getSQLDelete() {
      String strSQL = "";
      String strKeyValue = "";

      /*Recorremos el hash Map con los campos*/
      TreeSet keys = new TreeSet(this.Fields.keySet());
      Iterator it = keys.iterator();
      while (it.hasNext()) {
         String strNomField = (String) it.next();
         if (strNomField.toLowerCase().equals(this.NomKey.toLowerCase())) {
            strKeyValue = String.valueOf(this.Fields.get(strNomField));
         }
      }
      //Concatenamos instruccion de strRes
      strSQL = "delete from  " + this.NomTabla + " ";
      strSQL += " where  " + this.NomKey + " = " + strSepara + strKeyValue + strSepara + "";
      return strSQL;
   }
   /*Genera el strRes de consultas*/

   /**
    * Inserta el registro en la base de datos
    * @param oConn Es la conexion
    * @return Nos regresa OK si todo fue satisfactorio
    */
   public String Agrega(Conexion oConn) {
      String strRes = "OK";
      String strSql = this.getSQLInsert();
      oConn.runQueryLMD(strSql);
      if (oConn.isBolEsError()) {
         strRes = "ERROR:" + oConn.getStrMsgError();
      } else {
         /*Recuperamos el auto incremental*/
         if (this.bolGetAutonumeric) {
            ResultSet rs;
            try {
               rs = oConn.runQuery("select @@identity", true);
               while (rs.next()) {
                  /*Recorremos el hash Map con los campos*/
                  TreeSet keys = new TreeSet(this.Fields.keySet());
                  Iterator it = keys.iterator();
                  while (it.hasNext()) {
                     String strNomField = (String) it.next();
                     if (strNomField.toLowerCase().equals(this.NomKey.toLowerCase())) {
                        String strValorKey = rs.getString(1);//new String(String.valueOf(this.Fields.get(strNomField)))
                        int intKeyValue = rs.getInt(1);
                        this.Fields.put(this.NomKey, intKeyValue);
                        this.ValorKey = strValorKey;
                     }
                  }
               }
               //if(rs.getStatement() != null )rs.getStatement().close(); 
               rs.close();
            } catch (SQLException ex) {
               ex.fillInStackTrace();
               bitacora.GeneraBitacora(ex.getMessage(), oConn.getStrUsuario(), "", oConn);
            }
         }
      }
      return strRes;
   }

   /**
    * Actualiza el registro en la base de datos
    * @param oConn Es la conexion
    * @return Nos regresa OK si todo fue satisfactorio
    */
   public String Modifica(Conexion oConn) {
      String strRes = "OK";
      String strSql = this.getSQLUpdate();
      oConn.runQueryLMD(strSql);
      if (oConn.isBolEsError()) {
         strRes = "ERROR:" + oConn.getStrMsgError();
      }
      return strRes;
   }

   /**
    * Borra el registro en la base de datos
    * @param oConn Es la conexion
    * @return Nos regresa OK si todo fue satisfactorio
    */
   public String Borra(Conexion oConn) {
      String strRes = "OK";
      String strSql = this.getSQLDelete();
      oConn.runQueryLMD(strSql);
      if (oConn.isBolEsError()) {
         strRes = "ERROR:" + oConn.getStrMsgError();
      }
      return strRes;
   }

   /**
    * Carga los datos de un registro en particular
    * @param oConn Es la conexion a la base de datos
    * @return Nos regresa Ok en caso de que todo sea exitoso
    */
   public String ObtenDatos(Conexion oConn) {
      int intValorKey = 0;
      try {
         intValorKey = Integer.valueOf(this.ValorKey);
      } catch (NumberFormatException ex) {
      }
      return this.ObtenDatos(intValorKey, oConn);
   }

   /**
    * Carga los datos de un registro en particular
    * @param intIdValueKey Es el valor del campo llave
    * @param oConn Es la conexion a la base de datos
    * @return Nos regresa Ok en caso de que todo sea exitoso
    */
   public String ObtenDatos(int intIdValueKey, Conexion oConn) {
      String strRes = "OK";
      String strSql = "select * from " + this.NomTabla + " where " + this.NomKey + " = " + this.strSepara + intIdValueKey + this.strSepara + "";
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
                     log.debug("Intro->strNomField " + strNomField + " getColumnName " + rsmd.getColumnName(i));
                     log.debug(rsmd.getColumnTypeName(i) + " " + rs.getString(strNomField));
                     if (rsmd.getColumnTypeName(i).toUpperCase().startsWith("INT")) {
                        int intValue = rs.getInt(strNomField);
                        this.Fields.put(strNomField, intValue);
                     } else {
                        if (rsmd.getColumnTypeName(i).toUpperCase().startsWith("DOUBLE")) {
                           double dblValue = rs.getDouble(strNomField);
                           this.Fields.put(strNomField, dblValue);
                        } else {
                           if (rsmd.getColumnTypeName(i).toUpperCase().startsWith("DECIMAL")|| rsmd.getColumnTypeName(i).toUpperCase().startsWith("NUMERIC") || rsmd.getColumnTypeName(i).toUpperCase().startsWith("BIGINT")) {
                              double dblValue = rs.getDouble(strNomField);
                              this.Fields.put(strNomField, dblValue);
                           } else {
                              if (rsmd.getColumnTypeName(i).toUpperCase().startsWith("VARCHAR") || rsmd.getColumnTypeName(i).toUpperCase().startsWith("NVARCHAR")) {
                                 String strValue = rs.getString(strNomField);
                                 this.Fields.put(strNomField, strValue);
                              } else {
                                 if (rsmd.getColumnTypeName(i).toUpperCase().startsWith("SMALLINT")) {
                                    short intValue = rs.getShort(strNomField);
                                    this.Fields.put(strNomField, intValue);
                                 } else {
                                    if (rsmd.getColumnTypeName(i).toUpperCase().startsWith("TEXT") || rsmd.getColumnTypeName(i).toUpperCase().startsWith("NTEXT")) {
                                       String strValue = rs.getString(strNomField);
                                       this.Fields.put(strNomField, strValue);
                                    } else {
                                       if (rsmd.getColumnTypeName(i).toUpperCase().startsWith("CLOB")) {
                                          String strValue = rs.getString(strNomField);
                                          this.Fields.put(strNomField, strValue);
                                       }
                                    }
                                 }
                              }
                           }
                        }
                     }
                     bolEncontro = true;
                     break;
                  }
                  //End for
               }
               if (!bolEncontro) {
                  System.out.println("No encontro " + strNomField);
               }
            }//End while

         }
         //if(rs.getStatement() != null )rs.getStatement().close(); 
         rs.close();
      } catch (SQLException ex) {
         strRes = "ERROR:" + oConn.getStrMsgError();
         bitacora.GeneraBitacora(oConn.getStrMsgError(), oConn.getStrUsuario(), "", oConn);
      }
      return strRes;
   }

   /**
    * Obtiene la estructura de la tabla desde la base de datos
    * @param oConn Es la conexion
    */
   public void ObtenEstructura(Conexion oConn) {
      //Ejecutamos la instruccion describe para obtener todos los campos
      String strSql = "describe " + this.NomTabla;
      ResultSet rs;
      try {
         rs = oConn.runQuery(strSql, true);
         while (rs.next()) {
            String strName = rs.getString("Field");
            String strType = rs.getString("Type");
            //System.out.println(strName + " " + strType);
            if (strType.equals("text")) {
               this.Fields.put(strName, "");
            } else {
               if (strType.startsWith("varchar")) {
                  this.Fields.put(strName, "");
               } else {
                  if (strType.startsWith("smallint") || strType.startsWith("int")) {
                     //System.out.println(" es numerico");
                     this.Fields.put(strName, new Integer(0));
                  } else {
                     if (strType.startsWith("decimal")) {
                        this.Fields.put(strName, new Double(0));
                     }
                  }

               }
            }
         }
         //if(rs.getStatement() != null )rs.getStatement().close(); 
         rs.close();
      } catch (SQLException ex) {
         ex.fillInStackTrace();
         bitacora.GeneraBitacora(oConn.getStrMsgError(), oConn.getStrUsuario(), "", oConn);
      }
   }

   /**
    * Carga los datos de varios registros dependiendo de la condicion
    * @param strCond Es la condicion de los datos por obtener
    * @param oConn Es la conexion a la base de datos
    * @return Nos regresa un arrayList con los datos obtenidos
    */
   public ArrayList<TableMaster> ObtenDatosVarios(String strCond, Conexion oConn) {
      ArrayList<TableMaster> lstObjetos = new ArrayList<TableMaster>();
      String strSql = "select * from " + this.NomTabla + " where " + strCond + "";
      try {
         ResultSet rs = oConn.runQuery(strSql, true);
         ResultSetMetaData rsmd = rs.getMetaData();
         int numberOfColumns = rsmd.getColumnCount();
         /*System.out.println("numberOfColumns " + numberOfColumns);
         System.out.println("strNomField " + rsmd);*/
         while (rs.next()) {
            TableMaster mast = null;
            mast = (TableMaster) this.clone();
            /*Recorremos el hash Map con los campos*/
            TreeSet keys = new TreeSet(this.Fields.keySet());
            Iterator it = keys.iterator();
            while (it.hasNext()) {
               String strNomField = (String) it.next();
               boolean bolEncontro = false;
               //System.out.println("strNomField " + strNomField);
               for (int i = 1; i <= numberOfColumns; i++) {
                  if (rsmd.getColumnName(i).equals(strNomField)) {
                     log.debug("strNomField " + strNomField + " getColumnName " + rsmd.getColumnName(i));
                     log.debug(rsmd.getColumnTypeName(i) + " ");
                     if (rsmd.getColumnTypeName(i).toUpperCase().startsWith("INT")) {
                        int intValue = rs.getInt(strNomField);
                        mast.Fields.put(strNomField, intValue);
                     } else {
                        if (rsmd.getColumnTypeName(i).toUpperCase().startsWith("CLOB")) {
                           String strValue = rs.getString(strNomField);
                           mast.Fields.put(strNomField, strValue);
                        } else {
                           if (rsmd.getColumnTypeName(i).toUpperCase().startsWith("VARCHAR") || rsmd.getColumnTypeName(i).toUpperCase().startsWith("NVARCHAR")) {
                              String strValue = rs.getString(strNomField);
                              mast.Fields.put(strNomField, strValue);
                           } else {
                              if (rsmd.getColumnTypeName(i).toUpperCase().startsWith("DECIMAL")|| rsmd.getColumnTypeName(i).toUpperCase().startsWith("NUMERIC") || rsmd.getColumnTypeName(i).toUpperCase().startsWith("BIGINT")) {
                                 double dblValue = rs.getDouble(strNomField);
                                 mast.Fields.put(strNomField, dblValue);
                              } else {
                                 if (rsmd.getColumnTypeName(i).toUpperCase().startsWith("DOUBLE")) {
                                    double dblValue = rs.getDouble(strNomField);
                                    mast.Fields.put(strNomField, dblValue);
                                 } else {
                                    if (rsmd.getColumnTypeName(i).toUpperCase().startsWith("SMALLINT")) {
                                       short intValue = rs.getShort(strNomField);
                                       mast.Fields.put(strNomField, intValue);
                                    } else {
                                       if (rsmd.getColumnTypeName(i).toUpperCase().startsWith("TEXT")|| rsmd.getColumnTypeName(i).toUpperCase().startsWith("NTEXT")) {
                                          String strValue = rs.getString(strNomField);
                                          mast.Fields.put(strNomField, strValue);
                                       }
                                    }
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
            if (mast != null) {
               lstObjetos.add(mast);
            }
         }
         //if(rs.getStatement() != null )rs.getStatement().close(); 
         rs.close();
      } catch (SQLException ex) {
         bitacora.GeneraBitacora(oConn.getStrMsgError(), oConn.getStrUsuario(), "", oConn);
      }
      return lstObjetos;
   }

   @Override
   public Object clone() {
      Object obj = null;
      try {
         obj = super.clone();
      } catch (CloneNotSupportedException ex) {
         System.out.println(" no se puede duplicar");
      }
      TableMaster tab = (TableMaster) obj;
      tab.Fields = (HashMap) tab.Fields.clone();
      tab.lstData = (HashMap) tab.lstData.clone();
      TreeSet keys = new TreeSet(this.Fields.keySet());
      Iterator it = keys.iterator();
      while (it.hasNext()) {
         String strNomField = (String) it.next();
         int intValor = 0;
         double dblValor = 0;
         String strValor = "";
         try {
            if(this.Fields.get(strNomField) != null){
               strValor = new String((String) this.Fields.get(strNomField));
               tab.Fields.put(strNomField, new String(strValor));
            }else{
               tab.Fields.put(strNomField, null);
            }
            
         } catch (java.lang.ClassCastException ex) {
            try {
               intValor = new Integer(this.getFieldInt(strNomField));
               tab.Fields.put(strNomField, intValor);
            } catch (NumberFormatException exq) {
               try {
                  dblValor = new Double(this.getFieldDouble(strNomField));
                  tab.Fields.put(strNomField, dblValor);
               } catch (NumberFormatException exq2) {
               }
            }
         }

      }
      return obj;
   }

   /**
    * Nos regresa una cadena con el par nombre de campo = "Valor campo"
    * @return Regresa una cadena
    */
   public String getFieldPar() {
      UtilXml utilXML = new UtilXml();
      String strRes = "";
      /*Recorremos el hash Map con los campos*/
      TreeSet keys = new TreeSet(this.Fields.keySet());
      Iterator it = keys.iterator();
      while (it.hasNext()) {
         String strNomField = (String) it.next();
         String strValor = String.valueOf(this.Fields.get(strNomField));
         strValor = strValor.replace("&&", "##");
         strRes += strNomField + " = \"" + utilXML.Sustituye(strValor) + "\" ";
      }
      return strRes;
   }

   /**
    * Nos regresa el valor de la llave
    * @return Regresa una cadena con el valor de la llave
    */
   public String getValorKey() {
      return ValorKey;
   }

   /**
    * Define el valor de la llave
    * @param ValorKey Es una cadena con el valor de la llave
    */
   public void setValorKey(String ValorKey) {
      this.ValorKey = ValorKey;
   }

   /**
    * Nos dice si al hacer un insert nos regresa el valor autonumerico
    * @return Es un boolean con true si queremos que nos regrese el valor autonumerico
    */
   public boolean isBolGetAutonumeric() {
      return bolGetAutonumeric;
   }

   /**
    * Definimos si nos regresa el valor autonumerico
    * @param bolGetAutonumeric Es un boolean con true si queremos que nos regrese el valor autonumerico
    */
   public void setBolGetAutonumeric(boolean bolGetAutonumeric) {
      this.bolGetAutonumeric = bolGetAutonumeric;
   }

   /**
    * Regresa una lista de los nombres de los campos de la tabla
    * @return Es una lista con los nombres de campo definidos
    */
   public ArrayList<String> getNomFields() {
      ArrayList<String> lstFieldsSend = new ArrayList<String>();
      /*Recorremos el hash Map con los campos*/
      TreeSet keys = new TreeSet(this.Fields.keySet());
      Iterator it = keys.iterator();
      while (it.hasNext()) {
         String strNomField = (String) it.next();
         lstFieldsSend.add(strNomField);
      }
      return lstFieldsSend;
   }

   /**
    * Obtiene el valor del campo
    * @param strName Es el nombre del campo
    * @return Es el valor del campo
    */
   public Object getFieldObj(String strName) {
      return this.Fields.get(strName);
   }

   /**
    * Nos regresa true si la generacion de querys es estricta
    * es decir cuando sean valores numericos no se usara '
    * @return Es una valor boolean
    */
   public boolean isBolStrict() {
      return bolStrict;
   }

   /**
    * Definimos si la generacion de querys es estricta
    * es decir cuando sean valores numericos no se usara '
    * @param bolStrict Es una valor boolean
    */
   public void setBolStrict(boolean bolStrict) {
      this.bolStrict = bolStrict;
      if (this.bolStrict) {
         this.strSepara = "";
      } else {
         this.strSepara = "'";
      }
   }

   /**
    * Es el comodin para separa las comillas simples
    * @return Regresa el comodin
    */
   public String getStrComodinComillaSimple() {
      return strComodinComillaSimple;
   }

   /**
    * Es el comodin para separa las comillas simples
    * @param strComodinComillaSimple Define el comodin
    */
   public void setStrComodinComillaSimple(String strComodinComillaSimple) {
      this.strComodinComillaSimple = strComodinComillaSimple;
   }
}
