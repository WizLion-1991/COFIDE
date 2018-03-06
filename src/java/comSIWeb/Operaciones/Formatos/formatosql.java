package comSIWeb.Operaciones.Formatos;

import comSIWeb.Operaciones.Conexion;
import comSIWeb.Operaciones.TableMaster;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * Representa el sql del formato
 *
 * @author zeus
 */
public class formatosql extends TableMaster {

    private ResultSet Rs;
    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(formatosql.class.getName());

    public formatosql() {
        super("formatosql", "CAMPOLLAVE", "", "");
        this.Fields.put("FS_ID", new Integer(0));
        this.Fields.put("FM_ID", new Integer(0));
        this.Fields.put("FS_ESMASTER", new Integer(0));
        this.Fields.put("FS_LLAVE", "");
        this.Fields.put("FS_SQL", "");
        this.Fields.put("FS_PARAM1", "");
        this.Fields.put("FS_PARAM2", "");
        this.Fields.put("FS_PARAM3", "");
        this.Fields.put("FS_PARAM4", "");
        this.Fields.put("FS_PARAM5", "");
        this.Fields.put("FS_ESFOOT", new Integer(0));
    }

    /**
     * Ejecuta el SQL Maestro
     *
     * @param oConn Es la conexion
     * @param intIdKey Es el campo llave
     * @return Nos regresa los resultados
     */
    public String ExecuteSQLMaster(Conexion oConn, int intIdKey) {
        String strRes = "OK";
        if (this.getFieldInt("FS_ID") != 0) {
            if (!this.getFieldString("FS_SQL").equals("")) {
                String strSQL = this.getFieldString("FS_SQL");
                strSQL = strSQL.replace("[KEY]", intIdKey + "");
                log.debug("Ejecuta sql maestro " + strSQL);
                try {
                    this.Rs = oConn.runQuery(strSQL);
                    log.debug("SI Ejecuto el query");
                } catch (SQLException ex) {
                    this.Rs = null;
                    ex.printStackTrace();
                    log.error(ex.getMessage() + " " + ex.getSQLState());
                    strRes = "ERROR:Al ejecutar el SQL " + strSQL;
                }
            } else {
                strRes = "ERROR:No existe instruccion sql por ejecutar";
                log.error(strRes);
            }
        } else {
            strRes = "ERROR:No se cargado el registro";
            log.error(strRes);
        }
        return strRes;
    }

    /**
     * Ejecuta el SQL Maestro(QUITANDO la instruccion de por Id)
     *
     * @param oConn Es la conexion
     * @param strFiltro Es el filtro de la consulta mavisa
     * @return Nos regresa los resultados
     */
    public String ExecuteSQLMasterMasivo(Conexion oConn, String strFiltro) {
        String strRes = "OK";
        if (this.getFieldInt("FS_ID") != 0) {
            if (!this.getFieldString("FS_SQL").equals("")) {
                String strSQL = this.getFieldString("FS_SQL");
                strSQL = strSQL.replace(this.getFieldString("FS_PARAM1"), strFiltro);
                strSQL += this.getFieldString("FS_PARAM2");
                log.debug("Ejecuta sql maestro(MASIVO) " + strSQL);                
                try {
                    this.Rs = oConn.runQuery(strSQL);
                    log.debug("SI Ejecuto el query");
                } catch (SQLException ex) {
                    this.Rs = null;
                    log.error(ex.getMessage() + " " + ex.getSQLState());
                    strRes = "ERROR:Al ejecutar el SQL " + strSQL;
                }
            } else {
                strRes = "ERROR:No existe instruccion sql por ejecutar";
                log.error(strRes);
            }
        } else {
            strRes = "ERROR:No se cargado el registro";
            log.error(strRes);
        }
        return strRes;
    }

    /**
     * Ejecuta el SQL detalle
     *
     * @param oConn Es la conexion
     * @param intIdKey Es el campo llave
     * @param fMaster Es el SQl maestro
     * @return Nos regresa los resultados
     */
    public String ExecuteSQL(Conexion oConn, int intIdKey, formatosql fMaster) {
        String strRes = "OK";
        if (this.getFieldInt("FS_ID") != 0) {
            if (!this.getFieldString("FS_SQL").equals("")) {
                String strSQL = this.getFieldString("FS_SQL");
                strSQL = strSQL.replace("[KEY]", intIdKey + "");
                //Usamos el sql Master para reemplazar todos los datos
                strSQL = SustituyeMaster(strSQL, fMaster);
                log.debug("SQL detalle:" + strSQL);
                //Ejecutamos el SQL
                try {
                    this.Rs = oConn.runQuery(strSQL);
                } catch (SQLException ex) {
                    this.Rs = null;
                    ex.getMessage();
                    strRes = "ERROR:Al ejecutar el SQL " + strSQL;
                    log.error(ex.getMessage() + " " + ex.getSQLState());
                }
            } else {
                strRes = "ERROR:No existe instruccion sql por ejecutar";
                log.error(strRes);
            }
        } else {
            strRes = "ERROR:No se cargado el registro";
            log.error(strRes);
        }
        return strRes;
    }

    /**
     * Ejecuta el SQL detalle
     *
     * @param oConn Es la conexion
     * @param fMaster Es el SQl maestro
     * @return Nos regresa los resultados
     */
    public String ExecuteSQL(Conexion oConn, formatosql fMaster) {
        String strRes = "OK";
        if (this.getFieldInt("FS_ID") != 0) {
            if (!this.getFieldString("FS_SQL").equals("")) {
                String strSQL = new String(this.getFieldString("FS_SQL"));
                //Obtenemos el campo llave
                ResultSet rs = fMaster.getRs();
                try {
                    log.debug(fMaster.getFieldInt("FS_ID") + " " + fMaster.getFieldString("FS_SQL"));
                    if (rs != null) {
                        log.debug("SQL detalle verificamos que este abierto:" + strSQL);
                        //if (!rs.isClosed()) {
                        log.debug("No esta abierto.");
                        if (!fMaster.getRs().isBeforeFirst() && !fMaster.getRs().isAfterLast()) {
                            log.debug("Verificamos que no este al final");
                            //Obtenemos el campo llave
                            int intIdKey = rs.getInt(fMaster.getFieldString("FS_PARAM3"));
                            strSQL = strSQL.replace("[KEY]", intIdKey + "");
                            //Usamos el sql Master para reemplazar todos los datos
                            strSQL = SustituyeMaster(strSQL, fMaster);
                            log.debug("strSQL:" + strSQL);
                            //Ejecutamos el SQL
                            try {
                                this.Rs = oConn.runQuery(strSQL);
                            } catch (SQLException ex) {
                                ex.getMessage();
                                log.error(ex.getMessage() + " " + ex.getSQLState());
                                strRes = "ERROR:Al ejecutar el SQL " + strSQL;
                            }
                        }
                        //}
                    }

                } catch (SQLException ex) {
                    log.error("(A)" + ex.getMessage() + " " + ex.getSQLState());
                    Logger.getLogger(formatosql.class.getName()).log(Level.SEVERE, null, ex);
                } catch (AbstractMethodError ex) {
                    log.error("(B)" + ex.getMessage() + " ");
                } catch (Exception ex) {
                    log.error("(C)" + ex.getMessage() + " ");
                }
            } else {
                strRes = "ERROR:No existe instruccion sql por ejecutar";
            }
        } else {
            strRes = "ERROR:No se cargado el registro";
        }
        return strRes;
    }

    /**
     * Sustituye los valores del SQL master
     */
    private String SustituyeMaster(String strSql, formatosql fMaster) {

        TreeSet keys = new TreeSet(fMaster.Fields.keySet());
        Iterator it = keys.iterator();
        while (it.hasNext()) {
            String strNomField = (String) it.next();
            String strValor = String.valueOf(fMaster.Fields.get(strNomField));
            if (strSql.contains("[" + strNomField + "]")) {
                strSql = strSql.replace("[" + strNomField + "]", strValor);
            }
            //Recuepramos un valor del resultset
            if (strSql.contains("{" + strValor + "}")) {
                if (fMaster.Rs != null) {
                    try {
                        fMaster.Rs.first();
                        strSql = strSql.replace("{" + strValor + "}", fMaster.Rs.getString(strValor));
                    } catch (SQLException ex) {
                        Logger.getLogger(formatosql.class.getName()).log(Level.SEVERE, null, ex);
                        log.error(ex.getMessage() + " " + ex.getSQLState());
                    }
                }
            }
        }
        return strSql;
    }

    /**
     * Obtenemos el resultSet del Sql
     *
     * @return Es el resultSet
     */
    public ResultSet getRs() {
        return Rs;
    }

    /**
     * Definimos el resultSet del Sql
     *
     * @param Rs Es el resultSet
     */
    public void setRs(ResultSet Rs) {
        this.Rs = Rs;
    }
}
