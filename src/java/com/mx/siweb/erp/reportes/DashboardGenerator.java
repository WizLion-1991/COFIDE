/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.reportes;

import Tablas.Dashboard;
import Tablas.Dashboard_Params;
import Tablas.Dashboard_deta;
import Tablas.Dashboard_sql;
import com.mx.siweb.erp.reportes.entities.DashBoardSerie;
import com.siweb.utilerias.json.JSONArray;
import com.siweb.utilerias.json.JSONException;
import com.siweb.utilerias.json.JSONObject;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Operaciones.TableMaster;
import comSIWeb.Utilerias.Fechas;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;

/**
 * Genera el xml del dashboard para se grafica con algun motor
 *
 * @author aleph_79
 */
public class DashboardGenerator {

    // <editor-fold defaultstate="collapsed" desc="Propiedades">
    protected Conexion oConn;
    protected Dashboard dashboard;
    protected ArrayList<TableMaster> lstDeta;
    protected ArrayList<TableMaster> lstSql;
    protected ArrayList<TableMaster> lstParams;
    protected ArrayList<DashBoardSerie> lstSeries;
    private Fechas fecha;
    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(DashboardGenerator.class.getName());

    public ArrayList<TableMaster> getLstParams() {
        return lstParams;
    }

    public void setLstParams(ArrayList<TableMaster> lstParams) {
        this.lstParams = lstParams;
    }

    public Dashboard getDashboard() {
        return dashboard;
    }

    /**
     * Define el objeto dashboard
     *
     * @param dashboard Es una entidad dashboard
     */
    public void setDashboard(Dashboard dashboard) {
        this.dashboard = dashboard;
    }

    public ArrayList<TableMaster> getLstDeta() {
        return lstDeta;
    }

    public void setLstDeta(ArrayList<TableMaster> lstDeta) {
        this.lstDeta = lstDeta;
    }

    public ArrayList<TableMaster> getLstSql() {
        return lstSql;
    }

    public void setLstSql(ArrayList<TableMaster> lstSql) {
        this.lstSql = lstSql;
    }

    public Conexion getoConn() {
        return oConn;
    }

    public void setoConn(Conexion oConn) {
        this.oConn = oConn;
    }

   // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Constructores">
    public DashboardGenerator(Conexion oConn) {
        this.oConn = oConn;
        dashboard = new Dashboard();
        lstDeta = new ArrayList<TableMaster>();
        lstSql = new ArrayList<TableMaster>();
        lstParams = new ArrayList<TableMaster>();
        lstSeries = new ArrayList<DashBoardSerie>();
        fecha = new Fechas();
    }
   // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Metodos">
    /**
     * Inicializa los objetos del dashboard
     *
     * @param strAbrev Es el nombre del dashboard
     */
    public void Init(String strAbrev) {
        int intDB_ID = 0;
        String strSql = "select DB_ID FROM dashboard where DB_ABRV= '" + strAbrev + "'";
        try {
            ResultSet rs = this.oConn.runQuery(strSql, true);
            while (rs.next()) {
                intDB_ID = rs.getInt("DB_ID");
            }
            rs.close();
        } catch (SQLException ex) {
            log.error(ex.getMessage());
        }
        //obtenemos los datos del dashboard
        this.dashboard.ObtenDatos(intDB_ID, oConn);
        Dashboard_deta detaTmp = new Dashboard_deta();
        this.lstDeta = detaTmp.ObtenDatosVarios("  DB_ID = " + intDB_ID, oConn);
        Dashboard_sql dSqlTmp = new Dashboard_sql();
        this.lstSql = dSqlTmp.ObtenDatosVarios("  DB_ID = " + intDB_ID, oConn);
        Dashboard_Params dParamsTmp = new Dashboard_Params();
        this.lstParams = dParamsTmp.ObtenDatosVarios("  DB_ID = " + intDB_ID + " order by DBP_ORDEN", oConn);
    }

    /**
     * Inicializa todos los dashboard activos
     *
     * @param oConn Es la conexion a la base de datos
     * @param intPerfilId Es el id del perfil por mostrar
     * @return Regresa una lista de dashboards que cumplan con el perfil
     */
    public static ArrayList<DashboardGenerator> InitAll(Conexion oConn, int intPerfilId) {
        ArrayList<DashboardGenerator> lst = new ArrayList<DashboardGenerator>();
        String strSql = "select DB_ABRV FROM dashboard where DB_ACTIVO= 1 and PF_ID = " + intPerfilId;
        if (intPerfilId == 0) {
            strSql = "select DB_ABRV FROM dashboard where DB_ACTIVO= 1 ";
        }
        try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                String strDB_ABRV = rs.getString("DB_ABRV");
                DashboardGenerator dash = new DashboardGenerator(oConn);
                dash.Init(strDB_ABRV);
                lst.add(dash);

            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DashboardGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lst;
    }

    /**
     * Inicializa todos los dashboard activos
     *
     * @param oConn Es la conexion a la base de datos
     * @param strAbrvTipo Es el tipo de dashboard(clasificacion)
     * @return Regresa una lista de dashboards que cumplan con el perfil
     */
    public static ArrayList<DashboardGenerator> InitAllAbrvTipo(Conexion oConn, String strAbrvTipo) {
        ArrayList<DashboardGenerator> lst = new ArrayList<DashboardGenerator>();
        String strSql = "select DB_ABRV FROM dashboard where DB_ACTIVO= 1 and DB_TIPO = '" + strAbrvTipo + "'";
        try {
            ResultSet rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                String strDB_ABRV = rs.getString("DB_ABRV");
                DashboardGenerator dash = new DashboardGenerator(oConn);
                dash.Init(strDB_ABRV);
                lst.add(dash);

            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DashboardGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lst;
    }

    public void GetParams(HttpServletRequest request) {
        //Obtenemos los parametros
        Iterator<TableMaster> itParams = this.lstParams.iterator();
        while (itParams.hasNext()) {
            TableMaster param = (Dashboard_Params) itParams.next();
            String strNameTmp = param.getFieldInt("DBD_ID") + param.getFieldString("DBP_NAME");
            String strValorTmp = request.getParameter(strNameTmp);
            log.debug(strNameTmp + " " + strValorTmp);
            if (strValorTmp != null) {
                if (param.getFieldString("DBP_TIPO").equals("date")) {
                    Fechas fecha = new Fechas();
                    param.setFieldString("DBP_VALOR_DEFA", fecha.FormateaBD(strValorTmp, "/"));
                } else {
                    param.setFieldString("DBP_VALOR_DEFA", strValorTmp);
                }

            }
        }
    }

    /**
     * Genera el json del dashboard
     *
     * @return Regresa un cadena con el json del dashoard
     */
    public JSONObject DoDashboard() {
        log.debug("Do series...");
        GeneraSeries();
        log.debug("End Do series...");
        //Objeto json para almacenar los objetos
        JSONObject objJson = new JSONObject();
        try {
//         JSONArray jsonChild = new JSONArray();
            JSONObject objJsonNodo1 = new JSONObject();
            objJsonNodo1.put("Titulo", this.dashboard.getFieldString("DB_NOMBRE"));
            objJsonNodo1.put("Abrv", this.dashboard.getFieldString("DB_ABRV"));
            objJsonNodo1.put("Ancho", this.dashboard.getFieldString("DB_WIDTH"));
            objJsonNodo1.put("Alto", this.dashboard.getFieldString("DB_HEIGHT"));
            objJsonNodo1.put("Estilo", this.dashboard.getFieldString("DB_STYLE"));
            objJsonNodo1.put("Tipo", this.dashboard.getFieldString("DB_TIPO"));
            objJsonNodo1.put("Id", this.dashboard.getFieldInt("DB_ID"));
//         jsonChild.put(objJsonNodo1);
            objJson.put("DashBoard", objJsonNodo1);
            log.debug("Json 1...");
            //Graficas...
            JSONArray jsonGraficas = new JSONArray();
            Iterator<TableMaster> itDeta = this.lstDeta.iterator();
            while (itDeta.hasNext()) {
                TableMaster deta = itDeta.next();
                //Solo visualizamos las graficas marcadas como activas...
                if (deta.getFieldInt("DB_ACTIVO") == 1) {
                    log.debug("Activo..(deta)." + deta.getFieldInt("DBD_ID"));
                    JSONObject objJsonNodoGrafica = new JSONObject();
                    objJsonNodoGrafica.put("Titulo", deta.getFieldString("DB_TITULO"));
                    objJsonNodoGrafica.put("Subtitulo", deta.getFieldString("DB_SUBTITULO"));
                    objJsonNodoGrafica.put("NombreAxis", deta.getFieldString("DB_TITULO_AXIS"));
                    objJsonNodoGrafica.put("Alto", deta.getFieldString("DB_HEIGHT"));
                    objJsonNodoGrafica.put("Ancho", deta.getFieldString("DB_WIDTH"));
                    //Agregamos las series por grafica
                    JSONArray jsonSeries = new JSONArray();
                    Iterator<DashBoardSerie> itSeries = lstSeries.iterator();
                    while (itSeries.hasNext()) {
                        DashBoardSerie serie = itSeries.next();
                        log.debug("Series:" + serie.getIntIdGrafica());
                        if (serie.getIntIdGrafica() == deta.getFieldInt("DBD_ID")) {
                            //Definimos la serie
                            JSONObject objJsonSerie = new JSONObject();
                            objJsonSerie.put("Nombre_serie", serie.getStrNomSerie());
                     //Enviamos los nombres
                            //Enviamos las series
                            if (!serie.getLstNomCategorias().isEmpty()) {
                                JSONArray jsonNomCateg = new JSONArray();
                                Iterator<String> itNomCat = serie.getLstNomCategorias().iterator();
                                while (itNomCat.hasNext()) {
                                    String strNomCat = itNomCat.next();
                                    jsonNomCateg.put(strNomCat);
                                }
                                objJsonSerie.put("Listado_categorias", jsonNomCateg);
                            }
                            if (!serie.getLstValorSeries().isEmpty()) {
                                JSONArray jsonValoresSerie = new JSONArray();
                                Iterator<Double> itSeriesi = serie.getLstValorSeries().iterator();
                                while (itSeriesi.hasNext()) {
                                    double dblValor = itSeriesi.next();
                                    jsonValoresSerie.put(dblValor);
                                }
                                objJsonSerie.put("Listado series", jsonValoresSerie);
                            }
                            if (!serie.getLstValorSeriesInt().isEmpty()) {
                                JSONArray jsonValoresSerie = new JSONArray();
                                Iterator<Integer> itSeriesi = serie.getLstValorSeriesInt().iterator();
                                while (itSeriesi.hasNext()) {
                                    int intValor = itSeriesi.next();
                                    jsonValoresSerie.put(intValor);
                                }
                                objJsonSerie.put("Listado series", jsonValoresSerie);
                            }
                            jsonSeries.put(objJsonSerie);
                        }

                    }
                    objJsonNodoGrafica.put("series", jsonSeries);
                    jsonGraficas.put(objJsonNodoGrafica);
                }
            }
            objJsonNodo1.put("Graficas", jsonGraficas);

//      StringBuilder strXML = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
//      strXML.append("<DashBoard ");
//      strXML.append(this.dashboard.getFieldPar());
//      strXML.append(">");
//      //Ponemos la lista de detalle
//      
//      strXML.append("</DashBoard>");
//      return strXML.toString();
        } catch (JSONException ex) {
            log.debug(ex.getMessage());
        }
        return objJson;
    }

    public void GeneraSeries() {
        //Iteramos por todas las series para realizar el calculo
        Iterator<TableMaster> itDeta = this.lstSql.iterator();
        while (itDeta.hasNext()) {
            TableMaster deta = itDeta.next();
            //Solo procesamos los activos
            if (deta.getFieldInt("DBS_ACTIVO") == 1) {
                ExecuteSerie(deta.getFieldString("DBS_NOMBRE"), deta.getFieldString("DBS_SQL"), deta.getFieldInt("DBD_ID"),
                        deta.getFieldString("DBS_CAMPO_CATEGORIA"), deta.getFieldString("DBS_CAMPO_SERIE"), deta.getFieldString("DBS_TIPO_DATO"));
            }
        }
    }

    /**
     * Ejecuta la consulta para obtener las series por mostrar
     *
     * @param strNombre Es el nombre de la serie
     * @param strSpExecute Es la cadena por ejecutar
     * @param intDeta Es el id del detalle
     * @param strNomCategoria Es el nombre del campo del que se obtiene la
     * categoria
     * @param strNomSerie Es el nombre del campo del que se obtiene la serie
     * @param strTipoDato Es el tipo de datos que regresara el reporte
     */
    public void ExecuteSerie(String strNombre, String strSpExecute, int intDeta,
            String strNomCategoria, String strNomSerie,
            String strTipoDato) {

        CallableStatement cStmt;

        try {
            DashBoardSerie serie = new DashBoardSerie();
            cStmt = this.oConn.getConexion().prepareCall(strSpExecute);
            //Obtenemos los parametros
            int intCntaParams = 0;
            Iterator<TableMaster> itParams = this.lstParams.iterator();
            while (itParams.hasNext()) {
                TableMaster param = itParams.next();
                //Validamos que sean los parametros de la grafica que estamos ejecutando
                if (param.getFieldInt("DBD_ID") == intDeta) {
                    intCntaParams++;
                    String strValorTmp = param.getFieldString("DBP_VALOR_DEFA");
                    log.debug(intCntaParams + ":" + param.getFieldString("DBP_NAME") + " " + strValorTmp);
                    if (param.getFieldString("DBP_TIPO_DATO").equals("text")) {
                        cStmt.setString(intCntaParams, strValorTmp);

                    }
                    if (param.getFieldString("DBP_TIPO_DATO").equals("integer")) {
                        int intValorTmp = 0;
                        try {
                            intValorTmp = Integer.valueOf(strValorTmp);
                            cStmt.setInt(intCntaParams, intValorTmp);
                        } catch (NumberFormatException ex) {
                            log.error("Error al convertir el numero... " + ex.getMessage());
                        } catch (Exception ex) {
                            log.error("Error adicional... " + ex.getMessage());
                        }

                    }
                    if (param.getFieldString("DBP_TIPO_DATO").equals("double")) {
                        double dblValorTmp = 0;
                        try {
                            dblValorTmp = Double.valueOf(strValorTmp);
                        } catch (NumberFormatException ex) {
                        }
                        cStmt.setDouble(intCntaParams, dblValorTmp);
                    }
                }
            }
            boolean hadResults = cStmt.execute();
            while (hadResults) {
                ResultSet rs = cStmt.getResultSet();

                // process result set
                while (rs.next()) {
                    if (!strNomCategoria.isEmpty()) {
                        String strValor = rs.getString(strNomCategoria);
                        serie.getLstNomCategorias().add(strValor);
                    }
                    if (strTipoDato.equals("integer")) {
                        if (!strNomSerie.isEmpty()) {
                            int intValor = rs.getInt(strNomSerie);
                            serie.getLstValorSeriesInt().add(intValor);
                        }
                    }
                    if (strTipoDato.equals("double")) {
                        if (!strNomSerie.isEmpty()) {
                            double dblValor = rs.getDouble(strNomSerie);
                            serie.getLstValorSeries().add(dblValor);
                        }
                    }

                }
                rs.close();

                hadResults = cStmt.getMoreResults();
            }

            //agregamos la serie
            serie.setIntIdGrafica(intDeta);
            serie.setStrNomSerie(strNombre);
            this.lstSeries.add(serie);

        } catch (SQLException ex) {
            Logger.getLogger(ReporteEV_Comp_Anual.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Regresa el valor por default en base a comodines
     *
     * @param strDefault Es el valor por default del parametro
     * @param varSesiones Son las variables de sesion
     * @return Regresa el valor por default sustituido por los comodines
     */
    public String GetValorDefault(String strDefault, VariableSession varSesiones) {

        //Reemplazamos variables de sesion
        if (strDefault.equals("[FECHA]")) {
            strDefault = fecha.getFechaActualDDMMAAAADiagonal();
        }
        if (strDefault.equals("[FECHA_INI]")) {
            strDefault = "01" + fecha.getFechaActualDDMMAAAADiagonal().substring(2, fecha.getFechaActualDDMMAAAADiagonal().length());
        }
        if (strDefault.equals("[HORA]")) {
            strDefault = fecha.getHoraActual();
        }
        if (strDefault.equals("[no_user]")) {
            strDefault = varSesiones.getIntNoUser() + "";
            varSesiones.getStrUser();
        }
        if (strDefault.equals("[anio]")) {
            strDefault = varSesiones.getIntAnioWork() + "";
        }
        if (strDefault.equals("[ANIO]")) {
            strDefault = fecha.getAnioActual() + "";
        }
        if (strDefault.equals("[MES]")) {
            int intTmpMes = fecha.getMesActual() + 1;
            String strMes = "0" + intTmpMes;
            if (strMes.length() == 3) {
                strMes = strMes.substring(1, 3);
            }
            strDefault = strMes;
        }
        if (strDefault.equals("[Empresa]")) {
            strDefault = varSesiones.getIntIdEmpresa() + "";
        }
        if (strDefault.equals("[idcliente]")) {
            strDefault = varSesiones.getintIdCliente() + "";
        }
        if (strDefault.equals("[SUCURSAL]")) {
            strDefault = varSesiones.getIntSucursalDefault() + "";
        }
        if (strDefault.equals("[EmpresaDef]")) {
            strDefault = varSesiones.getIntIdEmpresa() + "";
        }
        return strDefault;
    }
   // </editor-fold>
}
