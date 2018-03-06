package comSIWeb.Utilerias;

import comSIWeb.Operaciones.Conexion;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *Copia las tablas en sql server
 * @author zeus
 */
public class Util_CopiaTablaMSSQL extends Util_CopiaTablas {

   /**
    * Realiza la copia de una tabla
    * @param strNomTabla Es el nombre de la tabla a copia
    * @param strCondicion Es la condicion de lo que se va a copiar
    * @param lstCamposNo Es la lista de campos que no se copiaran
    * @param oConn Es la conexion
    */
   @Override
   public void CopiaTabla(String strNomTabla, String strCondicion, ArrayList<String> lstCamposNo, Conexion oConn) {
      ArrayList<Util_CopiaCampo> lst = new ArrayList<Util_CopiaCampo>();

      String strSql = "select top 1 * from " + strNomTabla;
      ResultSet rs;
      try {
         rs = oConn.runQuery(strSql, true);
         ResultSetMetaData rsmd = rs.getMetaData();
         int numberOfColumns = rsmd.getColumnCount();
         for (int i = 1; i <= numberOfColumns; i++) {
            //if (rsmd.getColumnName(i).equals(strNomField)) {
            String strName = rsmd.getColumnName(i);
            String strType = rsmd.getColumnTypeName(i);
            boolean bolPasa = true;
            //Si el campo existe en el arreglo de lo que debemos evitar no procede
            Iterator<String> it = lstCamposNo.iterator();
            while (it.hasNext()) {
               String strField = it.next();
               if (strField.toUpperCase().equals(strName.toUpperCase())) {
                  bolPasa = false;
               }
            }
            if (bolPasa) {
               Util_CopiaCampo field = new Util_CopiaCampo(strName, strType);
               lst.add(field);
            }

         }
      } catch (SQLException ex) {
         Logger.getLogger(Util_CopiaTablaMSSQL.class.getName()).log(Level.SEVERE, null, ex);
      }
      //Generamos insert inicial
      String strInsert = "insert into " + strNomTabla + "(";
      Iterator<Util_CopiaCampo> it = lst.iterator();
      while (it.hasNext()) {
         Util_CopiaCampo field = it.next();
         strInsert += field.strNombre + ",";
      }
      if (strInsert.endsWith(",")) {
         strInsert = strInsert.substring(0, strInsert.length() - 1);
      }
      strInsert += ")values ";
      //Creamos string temporal que contendra los querys
      String strSqlDev = "";
      strSqlDev = new String(strInsert);
      //Si es consola ponemos unos separadores
      if (this.getIntTipo() == Util_CopiaTablas.CONSOLA) {
         strSqlDev += "\n";
      }
      //Copiamos los datos
      strSql = "select * from  " + strNomTabla + " " + strCondicion;
      try {
         rs = oConn.runQuery(strSql, true);
         int intConta = 0;
         while (rs.next()) {
            intConta++;
            strSqlDev += "(";
            //Recorremos todos los campos
            it = lst.iterator();
            while (it.hasNext()) {
               Util_CopiaCampo field = it.next();
               String strValor = rs.getString(field.strNombre);
               strValor = strValor.replace("'", "''");
               if (field.strTipo.contains("varchar") || field.strTipo.contains("text")) {
                  strSqlDev += "'" + strValor + "',";
               } else {
                  strSqlDev += strValor + ",";
               }

            }
            //Limpiamos la ultima coma
            if (strSqlDev.endsWith(",")) {
               strSqlDev = strSqlDev.substring(0, strSqlDev.length() - 1);
            }
            strSqlDev += ")";
            //System.out.println(strSqlDev);
            //Si el contador ya fue alcanzado mandamos el sql
            if (intConta == this.getIntRows()) {
               MandaSQL(strSqlDev, strInsert, true);
               strSqlDev = new String(strInsert + "\n");
               intConta = 0;
            } else {
               strSqlDev += ",";
               //Si es consola ponemos unos separadores
               if (this.getIntTipo() == Util_CopiaTablas.CONSOLA) {
                  strSqlDev += "\n";
               }
            }
         }
         rs.close();
         MandaSQL(strSqlDev, strInsert, true);
      } catch (SQLException ex) {
         ex.fillInStackTrace();
      }
   }

   /**
    * Manda a pantalla el sql
    */
   private void MandaSQL(String strSql, String strSQLini, boolean bolFin) {
      if (!strSql.equals(strSQLini)) {
         if (bolFin) {
            if (strSql.endsWith(",\n")) {
               strSql = strSql.substring(0, strSql.length() - 2);
            }
            strSql += ";";
         }
         if (this.getIntTipo() == Util_CopiaTablas.CONSOLA) {
            System.out.println(strSql);
         }
         if (this.getIntTipo() == Util_CopiaTablas.STRING) {
            this.getStrSqls().add(strSql);
         }
      }

   }
}


