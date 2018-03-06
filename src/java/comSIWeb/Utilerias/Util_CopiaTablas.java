package comSIWeb.Utilerias;

import comSIWeb.Operaciones.Conexion;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

//Representa un campo
/**
 * Esta clase copia tablas
 *
 * @author zeus
 */
public class Util_CopiaTablas {

    private int intTipo;
    private int intRows;
    public static final int CONSOLA = 0;
    public static final int STRING = 1;
    public static final int WEB = 2;
    public static final int TXT = 3;
    private ArrayList<String> strSqls;
    private String strPathTxt;
    private File file;
    FileWriter fw;
    BufferedWriter bw;
    private boolean bolSentenciaDelete = false;

    public String getStrPathTxt() {
        return strPathTxt;
    }

    public boolean getBolSentenciaDelete() {
        return bolSentenciaDelete;
    }

    public void setBolSentenciaDelete(boolean bolSentenciaDelete) {
        this.bolSentenciaDelete = bolSentenciaDelete;
    }

    /**
     * Definimos el path base para guardar el archivo txt
     *
     * @param strPathTxt Es la ruta donde se generara el archivo txt
     */
    public void setStrPathTxt(String strPathTxt) {
        this.strPathTxt = strPathTxt;
    }

    /**
     * Copia las tablas
     */
    public Util_CopiaTablas() {
        this.intRows = 30;
        this.intTipo = 0;
        this.strSqls = new ArrayList<String>();
    }

    /**
     * Realiza la copia de una tabla
     *
     * @param strNomTabla Es el nombre de la tabla a copia
     * @param strCondicion Es la condicion de lo que se va a copiar
     * @param lstCamposNo Es la lista de campos que no se copiaran
     * @param oConn Es la conexion
     */
    public void CopiaTabla(String strNomTabla, String strCondicion, ArrayList<String> lstCamposNo, Conexion oConn) {
//        System.out.println("delete from " + strNomTabla + " " + strCondicion);
        ArrayList<Util_CopiaCampo> lst = new ArrayList<Util_CopiaCampo>();
        //Preparacion para el caso de txt
        if (this.intTipo == Util_CopiaTablas.TXT) {
            preparaTxt(strNomTabla);
        }
        //Ejecutamos la instruccion describe para obtener todos los campos
        String strSql = "describe " + strNomTabla;
        ResultSet rs;
        try {
            rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                String strName = rs.getString("Field");
                String strType = rs.getString("Type");
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
            //if(rs.getStatement() != null )rs.getStatement().close(); 
            rs.close();
        } catch (SQLException ex) {
            ex.fillInStackTrace();
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
//      if(this.intTipo == Util_CopiaTablas.CONSOLA){
        strSqlDev += "\n";
//      }
        if (getBolSentenciaDelete()) {
            MandaDeleteSQL(strNomTabla, strCondicion);
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
                    if (strValor != null) {
                        strValor = strValor.replace("'", "\\'");
                    }
                    if (field.strTipo.startsWith("varchar") || field.strTipo.startsWith("text")) {
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
                if (intConta == intRows) {
                    MandaSQL(strSqlDev, strInsert, true);
                    strSqlDev = new String(strInsert + "\n");
                    intConta = 0;
                } else {
                    strSqlDev += ",";
                    //Si es consola ponemos unos separadores
//               if(this.intTipo == Util_CopiaTablas.CONSOLA){
                    strSqlDev += "\n";
//               }
                }
            }
            //if(rs.getStatement() != null )rs.getStatement().close(); 
            rs.close();
            MandaSQL(strSqlDev, strInsert, true);
            if (this.intTipo == Util_CopiaTablas.TXT) {
                cierraTxt();
            }

        } catch (SQLException ex) {
            ex.fillInStackTrace();
        }
    }

    public void exportFunctionSp(String strNomFunctionSP, boolean bolEsFunction, Conexion oConn) {
        /*Si bolEsFunction es true es una funcion si es false es un SP*/
        String strTipo = "";
        if (bolEsFunction) {
            strTipo = "FUNCTION";
        } else {
            strTipo = "PROCEDURE";
        }
        String strCreateFunction = "";
        String strSql = "SHOW CREATE " + strTipo + " " + strNomFunctionSP + ";";
        ResultSet rs;
        try {
            rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                strCreateFunction = "DROP " + strTipo + " IF EXISTS " + strNomFunctionSP + " ;\n";
                strCreateFunction += "DELIMITER ;;\n";
                strCreateFunction += rs.getString("Create " + strTipo);
                strCreateFunction = strCreateFunction.replaceAll("`", "");
                strCreateFunction = strCreateFunction.trim();
                if (strCreateFunction.endsWith("END")) {
                    strCreateFunction = strCreateFunction.substring(0, strCreateFunction.length() - 3);
                }
                strCreateFunction += "END ;;\n";
                strCreateFunction += "DELIMITER ;";

                System.out.println(strCreateFunction);
            }
            //if(rs.getStatement() != null )rs.getStatement().close(); 
            rs.close();
        } catch (SQLException ex) {
            ex.fillInStackTrace();
        }
    }

    private void preparaTxt(String strNomTabla) {

        file = new File(this.strPathTxt + "copia_tablas" + strNomTabla + ".txt");

        // if file doesnt exists, then create it
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(Util_CopiaTablas.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        try {
            fw = new FileWriter(file.getAbsoluteFile());
            bw = new BufferedWriter(fw);
        } catch (IOException ex) {
            Logger.getLogger(Util_CopiaTablas.class.getName()).log(Level.SEVERE, null, ex);
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
            if (this.intTipo == Util_CopiaTablas.CONSOLA) {
                System.out.println(strSql);
            }
            if (this.intTipo == Util_CopiaTablas.STRING) {
                this.strSqls.add(strSql);
            }
            if (this.intTipo == Util_CopiaTablas.TXT) {
                try {
                    bw.write(strSql);
                } catch (IOException ex) {
                    Logger.getLogger(Util_CopiaTablas.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
    }

    public void MandaDeleteSQL(String strNomTabla, String strCondicion) {
        String strQueryDelete = "";
        strQueryDelete = "delete from " + strNomTabla + " " + strCondicion + ";";
        System.out.println(strQueryDelete);
    }

    private void cierraTxt() {
        try {
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(Util_CopiaTablas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Nos regresa el numero de filas por query
     *
     * @return Es el numero de filas por query
     */
    public int getIntRows() {
        return intRows;
    }

    /**
     * Define el numero de filas por query
     *
     * @param intRows Es el numero de filas por query
     */
    public void setIntRows(int intRows) {
        this.intRows = intRows;
    }

    /**
     * Nos regresa el tipo de salida
     *
     * @return Regresa el tipo de salida de los sql
     */
    public int getIntTipo() {
        return intTipo;
    }

    /**
     * Define el tipo de salida
     *
     * @param intTipo Regresa el tipo de salida de los sql
     */
    public void setIntTipo(int intTipo) {
        this.intTipo = intTipo;
    }

    /**
     * Nos regresa la lista de querys generados
     *
     * @return Regresa una lista de querys
     */
    public ArrayList<String> getStrSqls() {
        return strSqls;

    }

}

/**
 * Representa un campo
 */
class Util_CopiaCampo {

    /**
     * Es el nombre del campo
     */
    public String strNombre;
    /**
     * Es el tipo de datos
     */
    public String strTipo;

    /**
     * Construye el objeto campo
     */
    public Util_CopiaCampo(String strNombre, String strTipo) {
        this.strNombre = strNombre;
        this.strTipo = strTipo;
    }

}
