package comSIWeb.Operaciones;

import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Scripting.scriptBase;
import comSIWeb.Utilerias.Fechas;
import comSIWeb.Utilerias.UtilXml;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
//import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/**
 * Representa una tabla definida en los formularios
 *
 * @author zeus
 */
public class CIP_Tabla extends TableMaster {
    // <editor-fold defaultstate="collapsed" desc="Propiedades">

    protected VariableSession varSesiones;
    protected UtilXml utilXML;
    protected Formularios form;
    protected ArrayList<TableMaster> lst;
    protected ArrayList<String> lstCeldas = new ArrayList<String>();
    protected ArrayList<String> lstFormulariosD = new ArrayList<String>();
    protected HttpServletRequest request;
    //Definimos variables a usar en el GRID
    protected String sidx, sord, searchField, searchString, searchOper, _search;
    protected int page, rows;
    protected final String strXMLHEAD = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
    protected boolean bolTransaccionalidad;
    protected boolean bolValidaSeguridad;
    protected boolean bolExecScripting;
    protected boolean bolAltas;
    protected boolean bolCambios;
    protected boolean bolConsultas;
    protected boolean bolImporta;
    protected String strMsgERROR;
    private static final Logger log = LogManager.getLogger(CIP_Tabla.class.getName());

    public String getSidx() {
        return sidx;
    }

    public void setSidx(String sidx) {
        this.sidx = sidx;
    }

    public String getSord() {
        return sord;
    }

    public void setSord(String sord) {
        this.sord = sord;
    }

    public String getSearchField() {
        return searchField;
    }

    public void setSearchField(String searchField) {
        this.searchField = searchField;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public String getSearchOper() {
        return searchOper;
    }

    public void setSearchOper(String searchOper) {
        this.searchOper = searchOper;
    }

    public String getSearch() {
        return _search;
    }

    public void setSearch(String _search) {
        this._search = _search;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    /**
     * Nos dice si validamos la seguridad
     *
     * @return Regresa un valor boolean
     */
    public boolean isBolValidaSeguridad() {
        return bolValidaSeguridad;
    }

    /**
     * Define si validamos la segurdiad
     *
     * @param bolValidaSeguridad Es un valor boolean
     */
    public void setBolValidaSeguridad(boolean bolValidaSeguridad) {
        this.bolValidaSeguridad = bolValidaSeguridad;
    }

    /**
     * Regresa si se ejecuta el scripting
     *
     * @return Regresa con un boolean si se ejecutan(default = true)
     */
    public boolean isBolExecScripting() {
        return bolExecScripting;
    }

    /**
     * Define si se ejecuta el scripting
     *
     * @param bolExecScripting Indica con un boolean si se ejecutan(default =
     * true)
     */
    public void setBolExecScripting(boolean bolExecScripting) {
        this.bolExecScripting = bolExecScripting;
    }

    /**
     * Regresa el objeto que representa el cabezal del formulario
     *
     * @return Regresa el formulario
     */
    public Formularios getForm() {
        return form;
    }

    /**
     * Nos dice si la tabla ocupara transaccionalidad
     *
     * @return Regresa un valor boolean
     */
    public boolean isBolTransaccionalidad() {
        return bolTransaccionalidad;
    }

    /**
     * Definimos si la tabla ocupara transaccionalidad
     *
     * @param bolTransaccionalidad Definimos un valor boolean
     */
    public void setBolTransaccionalidad(boolean bolTransaccionalidad) {
        this.bolTransaccionalidad = bolTransaccionalidad;
    }

    /**
     * Nos regresa los campos de la tabla actual
     *
     * @return Regresa un objeto Iterator para poder ver los campos de la tabla
     * actual
     */
    public Iterator getFields() {
        return this.lst.iterator();
    }

    /**
     * Nos regresa el campo completo solicitado
     *
     * @param strName Es el nombre del campo
     * @return Regresa el nombre del campo solicitado
     */
    public FormulariosDeta getField(String strName) {
        FormulariosDeta formget = null;
        //Buscamos el campo solicitado
        Iterator it = this.lst.iterator();
        while (it.hasNext()) {
            FormulariosDeta formDeta = (FormulariosDeta) it.next();
            if (formDeta.getFieldString("frmd_nombre").equals(strName)) {
                formget = formDeta;
            }
        }
        return formget;
    }

    /**
     * Nos regresa el objeto Request
     *
     * @return Regresa un objeto request o null si no esta definido aun
     */
    public HttpServletRequest getRequest() {
        return request;
    }

    /**
     * Nos regresa el objeto que manipula las sesiones
     *
     * @return Regresa un objeto VariableSession o null si no esta definido aun
     */
    public VariableSession getVarSesiones() {
        return varSesiones;
    }

    public boolean isBolImporta() {
        return bolImporta;
    }

    /**
     * Define si se estan importando por excel para recuperar parametros
     * personalizados
     *
     * @param bolImporta
     */
    public void setBolImporta(boolean bolImporta) {
        this.bolImporta = bolImporta;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Constructores">

    /**
     * Constructor del objeto que procesa las peticiones de las pantallas en
     * automatico
     *
     * @param NomTabla Es el nombre de la tabla
     * @param NomKey Es la llave primaria de la tabla
     * @param NomKey2 Es la llave secundaria de la tabla
     * @param NomKey3 Es la llave terciaria de la tabla
     * @param varSesiones Es el objeto con las variables de sesion
     */
    public CIP_Tabla(String NomTabla, String NomKey, String NomKey2, String NomKey3, VariableSession varSesiones) {
        super(NomTabla, NomKey, NomKey2, NomKey3);
        this.page = 0;
        this.rows = 0;
        this.sidx = "1";
        this.sord = "";
        this.searchField = "";
        this.searchString = "";
        this.searchOper = "";
        this.varSesiones = varSesiones;
        this.utilXML = new UtilXml();
        this._search = "";
        this.bolTransaccionalidad = false;
        this.bolValidaSeguridad = false;
        this.bolExecScripting = true;
        this.bolAltas = true;
        this.bolCambios = true;
        this.bolConsultas = true;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Metodos">
    /**
     * Inicializamos los datos de la tabla obteniendo los datos de los
     * formularios y formularios_deta
     *
     * @param strOpt Es el nombre corto de la pantalla por dibujar
     * @param oConn Es la conexion a la base de datos
     */
    public void Init(String strOpt, Conexion oConn) {
        this.Init(strOpt, true, true, true, oConn);
    }

    /**
     * Inicializamos los datos de la tabla obteniendo los datos de los
     * formularios y formularios_deta
     *
     * @param strOpt Es el nombre corto de la pantalla por dibujar
     * @param bolAlta Indica si usamos los controles para la alta
     * @param bolModi Indica si usamos los controles para la modificacion
     * @param bolConsulta Indica si usamos los controles para la consulta
     * @param oConn Es la conexion a la base de datos
     */
    public void Init(String strOpt, boolean bolAlta, boolean bolModi, boolean bolConsulta, Conexion oConn) {
        /*Obtenemos los datos del formulario*/
        this.form = new Formularios();
        //Validamos si activamos la validacion de la seguridad
        this.form.setBolValidaSeguridad(this.bolValidaSeguridad);
        this.form.setVarSesiones(varSesiones);
        this.form.ObtenDatos(strOpt, oConn);
        /*Definimos la tabla y las llaves*/
        this.NomTabla = this.form.getFieldString("frm_table");
        this.NomKey = this.form.getFieldString("frm_key");
        if (this.form.getFieldInt("frm_recupera_auto_increment") == 1) {
            this.setBolGetAutonumeric(true);
        }
        //Comodines para campos del formulario
        //Condicion especial
        if (!this.form.getFieldString("frm_gridCondicion").isEmpty()) {
            if (this.form.getFieldString("frm_gridCondicion").contains("[no_user]")) {
                this.form.setFieldString("frm_gridCondicion", this.form.getFieldString("frm_gridCondicion").replace("[no_user]", this.varSesiones.getIntNoUser() + ""));
            }
            if (this.form.getFieldString("frm_gridCondicion").contains("[no_cliente]")) {
                this.form.setFieldString("frm_gridCondicion", this.form.getFieldString("frm_gridCondicion").replace("[no_cliente]", this.varSesiones.getIntClienteWork() + ""));
            }
            if (this.form.getFieldString("frm_gridCondicion").contains("[Empresa]")) {
                this.form.setFieldString("frm_gridCondicion", this.form.getFieldString("frm_gridCondicion").replace("[Empresa]", this.varSesiones.getIntClienteWork() + ""));
            }
            if (this.form.getFieldString("frm_gridCondicion").contains("[anio]")) {
                this.form.setFieldString("frm_gridCondicion", this.form.getFieldString("frm_gridCondicion").replace("[anio]", this.varSesiones.getIntAnioWork() + ""));
            }
            if (this.form.getFieldString("frm_gridCondicion").contains("[SUCURSAL]")) {
                this.form.setFieldString("frm_gridCondicion", this.form.getFieldString("frm_gridCondicion").replace("[SUCURSAL]", this.varSesiones.getIntSucursalDefault() + ""));
            }
            if (this.form.getFieldString("frm_gridCondicion").contains("[EmpresaDef]")) {
                this.form.setFieldString("frm_gridCondicion", this.form.getFieldString("frm_gridCondicion").replace("[EmpresaDef]", this.varSesiones.getIntIdEmpresa() + ""));
            }
            if (this.form.getFieldString("frm_gridCondicion").contains("[LSTSUCURSAL]")) {
                this.form.setFieldString("frm_gridCondicion", this.form.getFieldString("frm_gridCondicion").replace("[LSTSUCURSAL]", varSesiones.getStrLstSucursales() + ""));
            }
        }
        /*Obtenemos los datos de detalle del formulario*/
        FormulariosDeta form_deta = new FormulariosDeta();
        this.lst = form_deta.ObtenDatosVarios(" frm_id = '" + this.form.getFieldInt("frm_id") + "' order by frmd_orden", oConn);
        /*Iteramos y llenamos el arreglo de los campos*/
        Iterator it = this.lst.iterator();
        while (it.hasNext()) {
            FormulariosDeta formDeta = (FormulariosDeta) it.next();
            String strfrmd_nombre = formDeta.getFieldString("frmd_nombre");
            String strfrmd_dato = formDeta.getFieldString("frmd_dato");
            String strfrmd_valor = formDeta.getFieldString("frmd_valor");
            String strfrmd_nuevo = formDeta.getFieldString("frmd_nuevo");
            String strfrmd_modif = formDeta.getFieldString("frmd_modif");
            String strfrmd_consulta = formDeta.getFieldString("frmd_consulta");
            //Recuperamos los valores del campo
            if ((bolAlta && strfrmd_nuevo.equals("1")
                    || bolModi && strfrmd_modif.equals("1")
                    || bolConsulta && strfrmd_consulta.equals("1"))
                    && !strfrmd_dato.equals("none")) {
                strfrmd_valor = strfrmd_valor.replace("[no_user]", this.varSesiones.getIntNoUser() + "");
                strfrmd_valor = strfrmd_valor.replace("[no_cliente]", this.varSesiones.getIntClienteWork() + "");
                strfrmd_valor = strfrmd_valor.replace("[Empresa]", this.varSesiones.getIntClienteWork() + "");
                strfrmd_valor = strfrmd_valor.replace("[anio]", this.varSesiones.getIntAnioWork() + "");
                strfrmd_valor = strfrmd_valor.replace("[SUCURSAL]", this.varSesiones.getIntSucursalDefault() + "");
                strfrmd_valor = strfrmd_valor.replace("[EmpresaDef]", this.varSesiones.getIntIdEmpresa() + "");
                strfrmd_valor = strfrmd_valor.replace("[LSTSUCURSAL]", varSesiones.getStrLstSucursales() + "");
                this.DefinirValor(strfrmd_nombre, strfrmd_dato, strfrmd_valor, formDeta, oConn, false);
            }
        }
    }

    /**
     * Indica si el campo Obtiene informacion de otra tabla
     *
     * @param objFormDeta nos dice si obtendremos datos de otra tabla
     * @return regresa un valor boolean
     */
    protected boolean _ObtieneInfoOtraTabla(FormulariosDeta objFormDeta) {
        boolean bolEscierto = false;
        if (!objFormDeta.getFieldString("frmd_tablaext").trim().equals("")
                && !objFormDeta.getFieldString("frmd_tabla_envio").trim().equals("")
                && !objFormDeta.getFieldString("frmd_tabla_mostrar").trim().equals("")
                && !objFormDeta.getFieldString("frmd_tipo").trim().equals("select")) {
            bolEscierto = true;
        }
        return bolEscierto;
    }

    /**
     * Obtiene todos los id de una tabla externa
     *
     * @param strValor Es valor externoa buscar
     * @param objFormDeta Es el objeto formilario deta
     * @param oConn Es la conexion a la base de datosa
     * @param strOperador Es el operador de busqueda
     */
    protected void _BuscaDatosExternoSearch(String strValor,
            FormulariosDeta objFormDeta, Conexion oConn, String strOperador) {
        //Buscamos los datos externos
        String strfrmd_nombre = objFormDeta.getFieldString("frmd_nombre");
        String strfrmd_tablaext = objFormDeta.getFieldString("frmd_tablaext");
        String strfrmd_tabla_envio = objFormDeta.getFieldString("frmd_tabla_envio");
        String strfrmd_tabla_mostrar = objFormDeta.getFieldString("frmd_tabla_mostrar");
        String strfrmd_tabla_pre = objFormDeta.getFieldString("frmd_tabla_pre");
        String strfrmd_tabla_post = objFormDeta.getFieldString("frmd_tabla_post");
        String strfrmd_alias_envio = objFormDeta.getFieldString("frmd_alias_envio");
        String strfrmd_alias_mostrar = objFormDeta.getFieldString("frmd_alias_mostrar");
        String strfrmd_consulta = objFormDeta.getFieldString("frmd_consulta");
        String strfrmd_dato = objFormDeta.getFieldString("frmd_dato");
        //Validamos si es un campo a mostrar en las consultas
        if (strfrmd_consulta.equals("1") && !strfrmd_dato.equals("none")) {
            //Validamos nombre de los campos por enviar
            String strNomEnvio = new String(strfrmd_tabla_envio);
            String strNomMostrar = new String(strfrmd_tabla_mostrar);
            if (!strfrmd_alias_envio.trim().equals("")) {
                strNomEnvio = new String(strfrmd_alias_envio);
            }
            if (!strfrmd_alias_mostrar.trim().equals("")) {
                strNomMostrar = strNomMostrar.replace(" as " + strfrmd_alias_mostrar, "");
            }
            String strTBPost = new String(strfrmd_tabla_post);
            strTBPost = strTBPost.replace("[no_user]", this.varSesiones.getIntNoUser() + "");
            strTBPost = strTBPost.replace("[no_cliente]", this.varSesiones.getIntClienteWork() + "");
            strTBPost = strTBPost.replace("[Empresa]", this.varSesiones.getIntClienteWork() + "");
            strTBPost = strTBPost.replace("[anio]", this.varSesiones.getIntAnioWork() + "");
            strTBPost = strTBPost.replace("[SUCURSAL]", this.varSesiones.getIntSucursalDefault() + "");
            strTBPost = strTBPost.replace("[LSTSUCURSAL]", this.varSesiones.getStrLstSucursales() + "");
            strTBPost = strTBPost.replace("[EmpresaDef]", this.varSesiones.getIntIdEmpresa() + "");
            if (strTBPost.contains("where") || strTBPost.contains("WHERE") || strTBPost.contains("Where")) {
                if (strTBPost.contains("order") || strTBPost.contains("ORDER") || strTBPost.contains("Order")) {
                    int intPos = strTBPost.toLowerCase().indexOf("order");
                    String strTBPost1 = strTBPost.substring(0, intPos);
                    String strTBPost2 = strTBPost.substring(intPos, strTBPost.length());
                    if (strOperador.equals("bw")) {
                        strTBPost = strTBPost1 + " AND " + strNomMostrar + " like '" + strValor + "%' " + strTBPost2;
                    }
                    if (strOperador.equals("eq")) {
                        strTBPost = strTBPost1 + " AND " + strNomMostrar + " = '" + strValor + "' " + strTBPost2;
                    }
                    if (strOperador.equals("ne")) {
                        strTBPost = strTBPost1 + " AND " + strNomMostrar + " <> '" + strValor + "' " + strTBPost2;
                    }
                    if (strOperador.equals("ew")) {
                        strTBPost = strTBPost1 + " AND " + strNomMostrar + " like '%" + strValor + "' " + strTBPost2;
                    }
                    if (strOperador.equals("cn")) {
                        strTBPost = strTBPost1 + " AND " + strNomMostrar + " like '%" + strValor + "%' " + strTBPost2;
                    }
                } else {
                    if (strOperador.equals("bw")) {
                        strTBPost = " AND  " + strNomMostrar + " like '" + strValor + "%' ";
                    }
                    if (strOperador.equals("eq")) {
                        strTBPost = " AND  " + strNomMostrar + " = '" + strValor + "' ";
                    }
                    if (strOperador.equals("ne")) {
                        strTBPost = " AND  " + strNomMostrar + " <> '" + strValor + "' ";
                    }
                    if (strOperador.equals("ew")) {
                        strTBPost = " AND  " + strNomMostrar + " like '%" + strValor + "' ";
                    }
                    if (strOperador.equals("cn")) {
                        strTBPost = " AND  " + strNomMostrar + " like '%" + strValor + "%' ";
                    }
                }
            } else {
                if (strOperador.equals("bw")) {
                    strTBPost = " where  " + strNomMostrar + " like '" + strValor + "%' " + strTBPost;
                }
                if (strOperador.equals("eq")) {
                    strTBPost = " where  " + strNomMostrar + " = '" + strValor + "' " + strTBPost;
                }
                if (strOperador.equals("ne")) {
                    strTBPost = " where  " + strNomMostrar + " <> '" + strValor + "' " + strTBPost;
                }
                if (strOperador.equals("ew")) {
                    strTBPost = " where  " + strNomMostrar + " like '%" + strValor + "' " + strTBPost;
                }
                if (strOperador.equals("cn")) {
                    strTBPost = " where  " + strNomMostrar + " like '%" + strValor + "%' " + strTBPost;
                }
            }
            String strSqlCombo = "select " + strfrmd_tabla_pre + " " + strfrmd_tabla_envio + ","
                    + strfrmd_tabla_mostrar + " from " + strfrmd_tablaext + " " + strTBPost
                    + "  ";
            log.debug("Debug aqui..." + strSqlCombo);
            try {
                ResultSet rs2 = oConn.runQuery(strSqlCombo, true);
                while (rs2.next()) {
                    String strValorOtra = rs2.getInt(strNomEnvio) + "";
                    objFormDeta.getLstElements().add(strValorOtra);
                }
//            if(rs2.getStatement() != null )rs2.getStatement().close(); 
                rs2.close();
            } catch (SQLException ex) {
                log.error(ex.getMessage());
                bitacora.GeneraBitacora(oConn.getStrMsgError(), oConn.getStrUsuario(), "", oConn);
            }
        }
    }

    /**
     * Definimos los valores de un campo de la tabla
     *
     * @param strfrmd_nombre Es el nombre del campo
     * @param strfrmd_valor Es el valor del campo
     * @param strfrmd_dato Es el tipo de dato
     * @param objFormDeta Es el objeto de formularios deta
     * @param oConn Es la conexion
     * @param bolRequest Indica si hay que buscar el campo en otro lado
     */
    public void DefinirValor(String strfrmd_nombre, String strfrmd_dato,
            String strfrmd_valor, FormulariosDeta objFormDeta, Conexion oConn, boolean bolRequest) {
        //Recuperamos los valores para consultar un registro
        if (strfrmd_dato.toLowerCase().equals("text")) {
            this.Fields.put(strfrmd_nombre, new String(strfrmd_valor));
        } else {
            if (strfrmd_dato.toLowerCase().equals("integer")) {
                if (strfrmd_valor.equals("")) {
                    strfrmd_valor = "0";
                }
                //Si es un obketo numerico de tipo panel check no hay que realziar conversion
                if (!objFormDeta.getFieldString("frmd_tipo").equals("PanelCheck")) {
                    //Convertimos el valor en numerico
                    int intValor = 0;
                    try {
                        intValor = Integer.valueOf(strfrmd_valor);
                        this.Fields.put(strfrmd_nombre, intValor);
                    } catch (NumberFormatException ex) {
                        //Si estamos haciendo el request vemos si el dato hay que buscarlo de otro lado
                        if (bolRequest) {
                            this.Fields.put(strfrmd_nombre, 0);
                            //El valor no es numerico entonces buscamos si es un campo de tipo text configurado para mostrar datos de otra tabla
                            if (_ObtieneInfoOtraTabla(objFormDeta)) {
                                //Obtenemos en la tabla externa el id a buscar
                                _BuscaDatosExternoSearch(strfrmd_valor, objFormDeta, oConn, "eq");
                            } else {
                                log.error("Error al recuperar valor numerico..." + this.NomTabla + " strfrmd_nombre " + strfrmd_nombre + " strfrmd_valor " + strfrmd_valor);
                            }
                        } else {
                            log.error("Error al recuperar valor numerico..." + this.NomTabla + " strfrmd_nombre " + strfrmd_nombre + " strfrmd_valor " + strfrmd_valor);
                        }
                    }

                } else {
                    //Aplica solo al panel check
                    this.Fields.put(strfrmd_nombre, new String(strfrmd_valor));
                }
            } else {
                if (strfrmd_dato.toLowerCase().equals("double")) {
                    double dblValor = 0;
                    try {
                        dblValor = Double.valueOf(strfrmd_valor);
                    } catch (NumberFormatException ex) {
                        log.error(this.NomTabla + " strfrmd_nombre " + strfrmd_nombre + " strfrmd_valor " + strfrmd_valor);
                    }
                    this.Fields.put(strfrmd_nombre, new Double(dblValor));
                }
            }
        }
    }

    /**
     * Obtiene los parametros enviados por el formulario definido en pantalla
     *
     * @param bolAlta Indica que es una pantalla de alta
     * @param bolModi Indica que es una pantalla de modificacion
     * @param bolConsulta Indica que es una pantalla de consulta
     * @param bolGRID Indica que recuperamos los valores del GRID
     * @param request Es el objeto con la peticion HTTP del usuario
     */
    public void ObtenParams(boolean bolAlta, boolean bolModi, boolean bolConsulta,
            boolean bolGRID, HttpServletRequest request) {
        this.ObtenParams(bolAlta, bolModi, bolConsulta,
                bolGRID, request, null);
    }

    /**
     * Obtiene los parametros enviados por el formulario definido en pantalla
     *
     * @param bolAlta Indica que es una pantalla de alta
     * @param bolModi Indica que es una pantalla de modificacion
     * @param bolConsulta Indica que es una pantalla de consulta
     * @param bolGRID Indica que recuperamos los valores del GRID
     * @param request Es el objeto con la peticion HTTP del usuario
     * @param oConn Es la conexion a la base de datos
     */
    public void ObtenParams(boolean bolAlta, boolean bolModi, boolean bolConsulta,
            boolean bolGRID, HttpServletRequest request, Conexion oConn) {
        /*Objeto para manipular las fechas*/
        Fechas fecha = new Fechas();

        //Evaluamos que el objeto request sea diferente de nulo
        if (request != null) {
            this.request = request;
            /*Iteramos y llenamos el arreglo de los campos*/
            Iterator it = this.lst.iterator();
            while (it.hasNext()) {
                FormulariosDeta formDeta = (FormulariosDeta) it.next();
                String strfrmd_nombre = formDeta.getFieldString("frmd_nombre");
                String strfrmd_dato = formDeta.getFieldString("frmd_dato");
                String strfrmd_tipo = formDeta.getFieldString("frmd_tipo");
                String strfrmd_nuevo = formDeta.getFieldString("frmd_nuevo");
                String strfrmd_modif = formDeta.getFieldString("frmd_modif");
                String strfrmd_consulta = formDeta.getFieldString("frmd_consulta");
                String strfrmd_importa = formDeta.getFieldString("frmd_importa");
                //Recuperamos los valores del campo
                if ((bolAlta && strfrmd_nuevo.equals("1")
                        || bolModi && strfrmd_modif.equals("1")
                        || bolConsulta && strfrmd_consulta.equals("1")
                        || this.bolImporta && strfrmd_importa.equals("1"))
                        && (!strfrmd_dato.equals("none")
                        || (strfrmd_dato.equals("none") && strfrmd_tipo.equals("text"))
                        || (strfrmd_dato.equals("none") && strfrmd_tipo.equals("select")))) {
                    //Obtenemos el parametro por recuperar
                    String strValor = request.getParameter(strfrmd_nombre);
                    if (strValor == null) {
                        strValor = "";
                        if (strfrmd_dato.equals("integer") || strfrmd_dato.equals("double")) {
                            strValor = "0";
                        }
                    }
                    //fecha
                    if (strfrmd_tipo.equals("date")) {
                        if (strValor.contains("/") && strValor.length() >= 10) {
                            strValor = fecha.FormateaBD(strValor, "/");
                        }
                    }
                    //periodo de fechas
                    if (strfrmd_tipo.equals("date_per")) {
                        strValor = request.getParameter(strfrmd_nombre + "1");
                        if (strValor != null) {
                            if (strValor.contains("/") && strValor.length() >= 10) {
                                strValor = fecha.FormateaBD(strValor, "/");
                            }
                            String strValor2 = request.getParameter(strfrmd_nombre + "2");
                            if (strValor2.contains("/") && strValor2.length() >= 10) {
                                strValor2 = fecha.FormateaBD(strValor2, "/");
                                formDeta.setStrValorAdi2(strValor2);
                            }
                        } else {
                            strValor = "";
                        }
                    }
                    //periodo de numeros
                    if (strfrmd_tipo.equals("num_per")) {
                        strValor = request.getParameter(strfrmd_nombre + "1");
                        if (strValor != null) {
                            String strValor2 = request.getParameter(strfrmd_nombre + "2");
                            formDeta.setStrValorAdi2(strValor2);
                        } else {
                            strValor = "";
                        }
                    }
                    //double
                    if (strfrmd_dato.equals("double")) {
                        strValor = strValor.replace(",", "");
                    }
                    this.DefinirValor(strfrmd_nombre, strfrmd_dato, strValor, formDeta, oConn, true);
                } else {
                    if ((bolAlta && strfrmd_nuevo.equals("1")
                            || bolModi && strfrmd_modif.equals("1"))
                            && strfrmd_dato.equals("none") && strfrmd_tipo.equals("PanelCheck")) {
                        if (request.getParameter(strfrmd_nombre) != null) {
                            //Definimos que si nos regresa el autonumerico
                            this.setBolGetAutonumeric(true);
                            this.bolTransaccionalidad = true;
                            //Recuperamos los valores
                            String[] lstVal = request.getParameter(strfrmd_nombre).split(",");
                            for (String strValor : lstVal) {
                                if (strValor != null) {
                                    formDeta.getLstElements().add(strValor);
                                }
                            }
                        }
                    }
                }
                //Recuperamos el valor del Id
                if (strfrmd_nombre.equals(this.NomKey) && request.getParameter(strfrmd_nombre) != null) {
                    this.ValorKey = request.getParameter(strfrmd_nombre);
                }
                /*Obtenemos parametros extras para el GRID*/
                if (bolGRID) {
                    if (request.getParameter("page") == null) {
                        page = 1;
                    } else {
                        page = Integer.valueOf(request.getParameter("page"));
                    }
                    if (request.getParameter("rows") == null) {
                        rows = 10;
                    } else {
                        rows = Integer.valueOf(request.getParameter("rows"));
                    }
                    sidx = request.getParameter("sidx");
                    if (sidx == null) {
                        sidx = "";
                    }
                    sord = request.getParameter("sord");
                    if (sord == null) {
                        sord = "";
                    }
                    searchField = request.getParameter("searchField");
                    if (searchField == null) {
                        searchField = "";
                    }
                    searchString = request.getParameter("searchString");
                    if (searchString == null) {
                        searchString = "";
                    }
                    searchOper = request.getParameter("searchOper");
                    if (searchOper == null) {
                        searchOper = "";
                    }
                    if (sidx.equals("")) {
                        sidx = "1";
                    }
                    this._search = request.getParameter("_search");
                    if (this._search == null) {
                        this._search = "";
                    }
                }
            }
        }
    }

    //Realiza una consulta y devuelve un XML para el GRID
    public String ConsultaXML(Conexion oConn) {
        StringBuilder strXML = new StringBuilder("");
        StringBuilder strFiltro = new StringBuilder();
        Fechas fecha = new Fechas();
        /**
         * ***********************FILTRO***************
         */
        String strSql = "SELECT * FROM " + this.NomTabla + " ";
//      if (this.form.getFieldString("frm_grid_sub_querys") != null) {
//         if (!this.form.getFieldString("frm_grid_sub_querys").isEmpty()) {
//            if (!this.form.getFieldString("frm_grid_sub_querys").equals("null")) {
//               strSql = "SELECT *," + this.form.getFieldString("frm_grid_sub_querys") + " FROM " + this.NomTabla + " ";
//            }
//         }
//      }
        if (!this.ValorKey.trim().equals("0") && !this.ValorKey.trim().equals("")) {
            strFiltro.append(this.NomKey).append(" = '").append(this.ValorKey).append("'");
        }
        //Validacion para cuando la tabla de consulta contengan el campo sucursales
        //Recorremos campo por campo
        Iterator it = this.lst.iterator();
        while (it.hasNext()) {
            FormulariosDeta formDeta = (FormulariosDeta) it.next();
            String strfrmd_nombre = formDeta.getFieldString("frmd_nombre");
            String strfrmd_consulta = formDeta.getFieldString("frmd_consulta");
            if (strfrmd_consulta.equals("1") && strfrmd_nombre.equals("SC_ID") && !this.NomTabla.equals("usuarios")
                    && !this.NomTabla.equals("vta_sucursal")) {
                strFiltro.append(" AND  ").append(strfrmd_nombre).append(" in (").append(this.varSesiones.getStrLstSucursales()).append(") ");
                break;
            }
        }
        //Validacion para cuando la tabla de consulta contengan el campo Empresa
        //Recorremos campo por campo
        it = this.lst.iterator();
        while (it.hasNext()) {
            FormulariosDeta formDeta = (FormulariosDeta) it.next();
            String strfrmd_nombre = formDeta.getFieldString("frmd_nombre");
            String strfrmd_consulta = formDeta.getFieldString("frmd_consulta");
            if (strfrmd_consulta.equals("1") && strfrmd_nombre.equals("EMP_ID")
                    && !this.NomTabla.equals("usuarios")
                    && !this.NomTabla.equals("vta_foliosempresa")
                    /*&& !this.NomTabla.equals("vta_sucursal")*/
                    && !this.NomTabla.equals("vta_empresas")) {
                if (this.varSesiones.getIntIdEmpresa() != 0) {
                    strFiltro.append(" AND  ").append(strfrmd_nombre).append(" = ").append(this.varSesiones.getIntIdEmpresa()).append(" ");
                }
                break;
            }
        }
        //Validacion para cuando la tabla de consulta contengan el campo Empresa
        //Recorremos campo por campo
        it = this.lst.iterator();
        while (it.hasNext()) {
            FormulariosDeta formDeta = (FormulariosDeta) it.next();
            String strfrmd_nombre = formDeta.getFieldString("frmd_nombre");
            String strfrmd_consulta = formDeta.getFieldString("frmd_consulta");
            if (strfrmd_consulta.equals("1")
                    && (strfrmd_nombre.toUpperCase().equals("IDCLIENTE") || strfrmd_nombre.toUpperCase().equals("PO_CLIENTE"))
                    && !this.NomTabla.equals("usuarios")) {
                if (this.varSesiones.getIntClienteWork() != 0) {
                    strFiltro.append(" AND  ").append(strfrmd_nombre).append(" = ").append(this.varSesiones.getIntClienteWork()).append(" ");
                }
                break;
            }
        }
        //Validamos si nos mandaron un campo para hacer un filtro
        if (this.searchField.trim().length() > 0) {
            if (searchString.trim().length() > 0) {
                //Buscamos si el dato a buscar es numerico y si el dato es numerico
                //Validamos si nos mandaron algun otro valor
                it = this.lst.iterator();
                while (it.hasNext()) {
                    FormulariosDeta formDeta = (FormulariosDeta) it.next();
                    String strfrmd_nombre = formDeta.getFieldString("frmd_nombre");
                    String strfrmd_consulta = formDeta.getFieldString("frmd_consulta");
                    String strfrmd_dato = formDeta.getFieldString("frmd_dato");
                    //Buscamos el campo
                    if (strfrmd_consulta.equals("1") && !strfrmd_dato.equals("none")
                            && strfrmd_nombre.equals(this.searchField)) {
                        if (strfrmd_dato.toLowerCase().equals("integer")) {
                            //Convertimos el valor en numerico
                            int intValor = 0;
                            try {
                                intValor = Integer.valueOf(searchString);
                                StringBuilder strFiltroTmp = new StringBuilder(" AND  " + this.searchField + " ");
                                if (this.searchOper.equals("eq")) {
                                    strFiltroTmp.append(" = ").append(searchString).append("");
                                }
                                if (this.searchOper.equals("ne")) {
                                    strFiltroTmp.append(" <> ").append(searchString).append("");
                                }
                                if (this.searchOper.equals("lt")) {
                                    strFiltroTmp.append(" < ").append(searchString).append("");
                                }
                                if (this.searchOper.equals("le")) {
                                    strFiltroTmp.append(" <= ").append(searchString).append("");
                                }
                                if (this.searchOper.equals("gt")) {
                                    strFiltroTmp.append(" >").append(searchString).append("");
                                }
                                if (this.searchOper.equals("ge")) {
                                    strFiltroTmp.append(" >=").append(searchString).append("");
                                }
                                strFiltro.append(strFiltroTmp).append(" ").append(strFiltro);
                            } catch (NumberFormatException ex) {
                                //El valor no es numerico entonces buscamos si es un campo de tipo text configurado para mostrar datos de otra tabla
                                if (_ObtieneInfoOtraTabla(formDeta)) {
                                    //Obtenemos en la tabla externa el id a buscar
                                    _BuscaDatosExternoSearch(searchString, formDeta, oConn, this.searchOper);
                                    //Validamos si hubo id's externos para usarlos como parte del filtro
                                    if (formDeta.getLstElements().size() > 0) {
                                        String strFiltroTmp = " AND  " + this.searchField + " ";
                                        strFiltroTmp += " IN (";
                                        Iterator<String> it3 = formDeta.getLstElements().iterator();
                                        while (it3.hasNext()) {
                                            String strValorFiltroM = it3.next();
                                            strFiltroTmp += strValorFiltroM + ",";
                                        }
                                        strFiltroTmp = strFiltroTmp.substring(0, strFiltroTmp.length() - 1);
                                        strFiltroTmp += ")";
                                        strFiltro.append(strFiltroTmp).append(" ").append(strFiltro);
                                    }
                                } else {
                                    log.error("Error al recuperar valor numerico..." + this.NomTabla + " strfrmd_nombre " + strfrmd_nombre + " strfrmd_valor " + searchString);
                                }
                            }
                            break;
                        } else {
                            //Debe ser texto
                            String strFiltroTmp = " AND  " + this.searchField + " ";
                            if (this.searchOper.equals("bw")) {
                                strFiltroTmp += " like'" + searchString + "%'";
                            }
                            if (this.searchOper.equals("eq")) {
                                strFiltroTmp += " = '" + searchString + "'";
                            }
                            if (this.searchOper.equals("ne")) {
                                strFiltroTmp += " <> '" + searchString + "'";
                            }
                            if (this.searchOper.equals("lt")) {
                                strFiltroTmp += " < '" + searchString + "'";
                            }
                            if (this.searchOper.equals("le")) {
                                strFiltroTmp += " <= '" + searchString + "'";
                            }
                            if (this.searchOper.equals("gt")) {
                                strFiltroTmp += " >'" + searchString + "'";
                            }
                            if (this.searchOper.equals("ge")) {
                                strFiltroTmp += " >='" + searchString + "'";
                            }
                            if (this.searchOper.equals("ew")) {
                                strFiltroTmp += " like'%" + searchString + "'";
                            }
                            if (this.searchOper.equals("cn")) {
                                strFiltroTmp += " like'%" + searchString + "%'";
                            }
                            strFiltro.append(strFiltroTmp).append(" ").append(strFiltro);
                            break;
                        }
                    }
                }
            }
        } else {
            // <editor-fold defaultstate="collapsed" desc="Busquedas con el filtro en el encabezado">
            if (this._search.equals("true")) {
                StringBuilder strFiltroTmp2 = new StringBuilder();
                //Validamos si nos mandaron algun otro valor
                it = this.lst.iterator();
                while (it.hasNext()) {
                    FormulariosDeta formDeta = (FormulariosDeta) it.next();
                    String strfrmd_nombre = formDeta.getFieldString("frmd_nombre");
                    String strfrmd_consulta = formDeta.getFieldString("frmd_consulta");
                    String strfrmd_search = formDeta.getFieldString("frmd_search");
                    String strfrmd_search_unique = formDeta.getFieldString("frmd_search_unique");
                    String strfrmd_dato = formDeta.getFieldString("frmd_dato");
                    String strfrmd_tipo = formDeta.getFieldString("frmd_tipo");
                    String strfrmd_formula_sql = formDeta.getFieldString("frmd_formula_sql");
                    String strValor = String.valueOf(this.Fields.get(strfrmd_nombre));
                    if (strfrmd_consulta.equals("1") && !strfrmd_dato.equals("none") && strfrmd_search.equals("1")) {
                        // <editor-fold defaultstate="collapsed" desc="Busqueda con formulas en el query">
                        if (!strfrmd_formula_sql.isEmpty() && !strValor.isEmpty()) {
                            if (strfrmd_dato.equals("integer") || strfrmd_dato.equals("double")) {
                                strFiltroTmp2.append(" AND  ").append(strfrmd_formula_sql).append(" = ").append(strValor).append(" ");
                            } else {
                                strFiltroTmp2.append(" AND  ").append(strfrmd_formula_sql).append(" = '").append(strValor).append("' ");
                            }
                        } else {
                            //Inicia filtro busqueda normal
                            if (strfrmd_dato.equals("integer")) {
                                if (strfrmd_tipo.equals("radio")) {
                                    if (!strValor.equals("999")) {
                                        strFiltroTmp2.append(" AND  ").append(strfrmd_nombre).append(" = ").append(strValor).append(" ");
                                    }
                                } else {
                                    if (strfrmd_tipo.equals("num_per")) {
                                        strFiltroTmp2.append(" AND ").append(strfrmd_nombre).append(" >= '").append(strValor).append("' AND ").append(strfrmd_nombre).append(" <= '").append(formDeta.getStrValorAdi2()).append("' ");
                                    } else {
                                        if (strfrmd_tipo.equals("PanelCheck")) {
                                            // <editor-fold defaultstate="collapsed" desc="Lista de opciones">
                                            String[] listOpciones = strValor.split(",");
                                            log.debug(strfrmd_nombre + "strValor:" + strValor);
                                            strFiltroTmp2.append(" AND  ").append(strfrmd_nombre).append(" in ( ");
                                            log.debug("listOpciones.length:" + listOpciones.length);
                                            for (int qk = 0; qk < listOpciones.length; qk++) {

                                                if (qk == 0) {
                                                    strFiltroTmp2.append(listOpciones[qk]);
                                                } else {
                                                    strFiltroTmp2.append(",").append(listOpciones[qk]);
                                                }
                                            }
                                            strFiltroTmp2.append(" )  ");
                                            // </editor-fold>
                                        } else {
                                            if (Integer.valueOf(strValor) != 0) {
                                                // <editor-fold defaultstate="collapsed" desc="Valor entero">
                                                strFiltroTmp2.append(" AND  ").append(strfrmd_nombre).append(" = ").append(strValor).append(" ");
                                                if (strfrmd_search_unique.equals("1")) {
                                                    strFiltroTmp2 = new StringBuilder("");
                                                    strFiltroTmp2.append(" AND  ").append(strfrmd_nombre).append(" = ").append(strValor).append(" ");
                                                    break;
                                                }
                                                // </editor-fold>
                                            } else {
                                                // <editor-fold defaultstate="collapsed" desc="El valor no es numerico entonces buscamos si es un campo de tipo text configurado para mostrar datos de otra tabla">
                                                if (_ObtieneInfoOtraTabla(formDeta)) {
                                                    if (formDeta.getLstElements().size() > 0) {
                                                        StringBuilder strFiltroTmp = new StringBuilder(" AND  " + strfrmd_nombre + " ");
                                                        strFiltroTmp.append(" IN (");
                                                        Iterator<String> it3 = formDeta.getLstElements().iterator();
                                                        while (it3.hasNext()) {
                                                            String strValorFiltroM = it3.next();
                                                            strFiltroTmp.append(strValorFiltroM).append(",");
                                                        }
                                                        strFiltroTmp.append(strFiltroTmp.substring(0, strFiltroTmp.length() - 1));
                                                        strFiltroTmp.append(")");
                                                        strFiltroTmp2.append(strFiltroTmp).append(" ").append(strFiltroTmp2);
                                                    }
                                                }
                                                // </editor-fold>
                                            }
                                        }
                                    }
                                }
                            } else {
                                if (strfrmd_dato.equals("double")) {
                                    if (Double.valueOf(strValor) != 0) {
                                        strFiltroTmp2.append(" AND  ").append(strfrmd_nombre).append(" = ").append(strValor).append(" ");
                                    }
                                } else {
                                    if (strfrmd_tipo.equals("date_per")) {
                                        if (!strValor.equals("") && !strValor.equals("null")) {
                                            strFiltroTmp2.append(" AND ").append(strfrmd_nombre).append(" >= '").append(strValor).append("' AND ").append(strfrmd_nombre).append(" <= '").append(formDeta.getStrValorAdi2()).append("' ");
                                        }
                                    } else {
                                        if (strfrmd_tipo.equals("date")) {
                                            if (!strValor.equals("") && !strValor.equals("null")) {
                                                strFiltroTmp2.append(" AND  ").append(strfrmd_nombre).append(" = '").append(strValor).append("' ");
                                            }
                                        } else {
                                            if (!strValor.equals("") && !strValor.equals("null")) {
                                                strFiltroTmp2.append(" AND  ").append(strfrmd_nombre).append(" like '%").append(strValor).append("%' ");
                                                if (strfrmd_search_unique.equals("1")) {
                                                    strFiltroTmp2 = new StringBuilder("");
                                                    strFiltroTmp2.append(" AND  ").append(strfrmd_nombre).append(" like '%").append(strValor).append("%' ");
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            //Termina filtro busqueda normal
                        }
                        // </editor-fold>

                    }
                }
                strFiltro.append(strFiltroTmp2);
            }
            // </editor-fold>
        }
        // <editor-fold defaultstate="collapsed" desc="Condicion especial">
        String strfrm_gridCondicion = this.form.getFieldString("frm_gridCondicion");
        if (!strfrm_gridCondicion.equals("")) {
            if (strfrm_gridCondicion.contains("[no_user]")) {
                strfrm_gridCondicion = strfrm_gridCondicion.replace("[no_user]", this.varSesiones.getIntNoUser() + "");
            }
            if (strfrm_gridCondicion.contains("[no_cliente]")) {
                strfrm_gridCondicion = strfrm_gridCondicion.replace("[no_cliente]", this.varSesiones.getIntClienteWork() + "");
            }
            if (strfrm_gridCondicion.contains("[Empresa]")) {
                strfrm_gridCondicion = strfrm_gridCondicion.replace("[Empresa]", this.varSesiones.getIntClienteWork() + "");
            }
            if (strfrm_gridCondicion.contains("[anio]")) {
                strfrm_gridCondicion = strfrm_gridCondicion.replace("[anio]", this.varSesiones.getIntAnioWork() + "");
            }
            if (strfrm_gridCondicion.contains("[SUCURSAL]")) {
                strfrm_gridCondicion = strfrm_gridCondicion.replace("[SUCURSAL]", this.varSesiones.getIntSucursalDefault() + "");
            }
            if (strfrm_gridCondicion.contains("[LSTSUCURSAL]")) {
                strfrm_gridCondicion = strfrm_gridCondicion.replace("[LSTSUCURSAL]", this.varSesiones.getStrLstSucursales() + "");
            }
            if (strfrm_gridCondicion.contains("[EmpresaDef]")) {
                strfrm_gridCondicion = strfrm_gridCondicion.replace("[EmpresaDef]", this.varSesiones.getIntIdEmpresa() + "");
            }
            strFiltro.append(strfrm_gridCondicion);
        }
        // </editor-fold>
        String strFiltroOK = null;
        if (strFiltro.toString().trim().length() > 0) {
            if (strFiltro.toString().startsWith(" AND")) {
                strFiltroOK = " WHERE " + strFiltro.substring(4, strFiltro.length());
            } else {
                strFiltroOK = " WHERE " + strFiltro;
            }
        } else {
            strFiltroOK = "";
        }
        strSql += strFiltroOK;
        /**
         * ***********************FILTRO***************
         */
        // <editor-fold defaultstate="collapsed" desc="PAGINACION">
        //if this is the first query - get total number of records in the query result
        String strSqlCount = "";
        double dblCount = 0;
        long inttotal_pages = 0;
        if (this.rows == 0) {
            this.rows = 1;
        }
        strSqlCount = "Select count(*) as cnt from (" + strSql.replace("SELECT * FROM", "SELECT " + this.NomKey + " FROM") + ") as tbl";
        ResultSet rs;
        try {
            rs = oConn.runQuery(strSqlCount, true);
            while (rs.next()) {
                dblCount = rs.getDouble("cnt");
            }
            rs.close();
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            bitacora.GeneraBitacora(oConn.getStrMsgError(), oConn.getStrUsuario(), "", oConn);
        }
        if (dblCount > 0) {
            inttotal_pages = (long) this.round(dblCount / rows, 0);
        } else {
            inttotal_pages = 0;
        }
        /*if for some reasons the requested page is greater than the total
       set the requested page to total page*/
        if (this.page > inttotal_pages) {
            page = (int) inttotal_pages;
        }
        //Calculate the starting position of the rows
        int start = this.rows * this.page - this.rows;
        /*if for some reasons start position is negative set it to 0
       'typical case is that the user type 0 for the requested page*/
        if (start < 0) {
            start = 0;
        }
        //Despues de la paginacion anadimos los subquerys
        log.debug("Agregamos los subquerys...");
        strSql = "SELECT * FROM " + this.NomTabla + " ";
        if (this.form.getFieldString("frm_grid_sub_querys") != null) {
            if (!this.form.getFieldString("frm_grid_sub_querys").isEmpty()) {
                if (!this.form.getFieldString("frm_grid_sub_querys").equals("null")) {
                    // <editor-fold defaultstate="collapsed" desc="Sustituimos variables">
                    if (this.form.getFieldString("frm_grid_sub_querys").contains("[no_user]")) {
                        this.form.setFieldString("frm_grid_sub_querys", this.form.getFieldString("frm_grid_sub_querys").replace("[no_user]", this.varSesiones.getIntNoUser() + ""));
                    }
                    if (this.form.getFieldString("frm_grid_sub_querys").contains("[no_cliente]")) {
                        this.form.setFieldString("frm_grid_sub_querys", this.form.getFieldString("frm_grid_sub_querys").replace("[no_cliente]", this.varSesiones.getIntClienteWork() + ""));
                    }
                    if (this.form.getFieldString("frm_grid_sub_querys").contains("[Empresa]")) {
                        this.form.setFieldString("frm_grid_sub_querys", this.form.getFieldString("frm_grid_sub_querys").replace("[Empresa]", this.varSesiones.getIntClienteWork() + ""));
                    }
                    if (this.form.getFieldString("frm_grid_sub_querys").contains("[anio]")) {
                        this.form.setFieldString("frm_grid_sub_querys", this.form.getFieldString("frm_grid_sub_querys").replace("[anio]", this.varSesiones.getIntAnioWork() + ""));
                    }
                    if (this.form.getFieldString("frm_grid_sub_querys").contains("[SUCURSAL]")) {
                        this.form.setFieldString("frm_grid_sub_querys", this.form.getFieldString("frm_grid_sub_querys").replace("[SUCURSAL]", this.varSesiones.getIntSucursalDefault() + ""));
                    }
                    if (this.form.getFieldString("frm_grid_sub_querys").contains("[EmpresaDef]")) {
                        this.form.setFieldString("frm_grid_sub_querys", this.form.getFieldString("frm_grid_sub_querys").replace("[EmpresaDef]", this.varSesiones.getIntIdEmpresa() + ""));
                    }
                    if (this.form.getFieldString("frm_grid_sub_querys").contains("[LSTSUCURSAL]")) {
                        this.form.setFieldString("frm_grid_sub_querys", this.form.getFieldString("frm_grid_sub_querys").replace("[LSTSUCURSAL]", varSesiones.getStrLstSucursales() + ""));
                    }
                    // </editor-fold> 

                    // <editor-fold defaultstate="collapsed" desc="Obtenemos parametros de las Busquedas con el filtro en el encabezado">
                    if (this._search.equals("true")) {
                        StringBuilder strFiltroTmp2 = new StringBuilder();
                        //Validamos si nos mandaron algun otro valor
                        it = this.lst.iterator();
                        while (it.hasNext()) {
                            FormulariosDeta formDeta = (FormulariosDeta) it.next();
                            String strfrmd_nombre = formDeta.getFieldString("frmd_nombre");
                            String strfrmd_consulta = formDeta.getFieldString("frmd_consulta");
                            String strfrmd_dato = formDeta.getFieldString("frmd_dato");
                            String strValor = String.valueOf(this.Fields.get(strfrmd_nombre));
                            if (strfrmd_consulta.equals("1") && !strfrmd_dato.equals("none")) {
                                log.debug(strfrmd_nombre + " " + strValor);
                                if (this.form.getFieldString("frm_grid_sub_querys").contains("[$" + strfrmd_nombre + "]")) {
                                    this.form.setFieldString("frm_grid_sub_querys", this.form.getFieldString("frm_grid_sub_querys").replace("[$" + strfrmd_nombre + "]", strValor));
                                }
                            }
                        }
                        strFiltro.append(strFiltroTmp2);
                    }
                    // </editor-fold>
                    strSql = "SELECT *," + this.form.getFieldString("frm_grid_sub_querys") + " FROM " + this.NomTabla + " ";
                }
            }
        }
        strSql += strFiltroOK;
        //Anadimos el orden
        if (!this.sidx.equals("") && !this.sord.equals("")) {
            strSql += " ORDER BY " + this.sidx + " " + this.sord;
        }
        //add limits to query to get only rows necessary for output
        strSql += " LIMIT " + start + "," + this.rows;
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="ENCABEZADO">
        strXML.append(strXMLHEAD);
        strXML.append("<rows>" + "");
        strXML.append("<page>").append(page).append("</page>");
        strXML.append("<total>").append(inttotal_pages).append("</total>");
        strXML.append("<records>").append((int) dblCount).append("</records>");
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Campos con formula">
        StringBuilder strListaCamposFormula = new StringBuilder();
        it = this.lst.iterator();
        while (it.hasNext()) {
            FormulariosDeta formDeta = (FormulariosDeta) it.next();
            String strfrmd_consulta = formDeta.getFieldString("frmd_consulta");
            String strfrmd_dato = formDeta.getFieldString("frmd_dato");
            String strfrmd_tipo = formDeta.getFieldString("frmd_tipo");
            String strfrmd_formula_sql = formDeta.getFieldString("frmd_formula_sql");
            String strfrmd_formula_alias = formDeta.getFieldString("frmd_formula_alias");
            if (strfrmd_consulta.equals("1") && !strfrmd_dato.equals("none")
                    && !strfrmd_formula_sql.isEmpty() && !strfrmd_formula_alias.isEmpty()) {
                strListaCamposFormula.append(",").append(strfrmd_formula_sql).append(" as ").append(strfrmd_formula_alias);
            }
        }
        if (strListaCamposFormula.length() != 0) {
            strSql = strSql.replace("*", "*" + strListaCamposFormula.toString());
        }
        // </editor-fold>
        /**
         * ***********************OBTENEMOS LISTA DE CAMPOS A
         * MOSTRAR************
         */
        try {
            rs = oConn.runQuery(strSql, true);
            int intContaRows = 0;
            while (rs.next()) {
                String strXfrm_Valorkey = rs.getString(this.NomKey);
                intContaRows++;
                strXML.append("<row id='").append(strXfrm_Valorkey).append("'>");
                //Recorremos campo por campo
                it = this.lst.iterator();
                while (it.hasNext()) {
                    FormulariosDeta formDeta = (FormulariosDeta) it.next();
                    String strfrmd_nombre = formDeta.getFieldString("frmd_nombre");
                    String strfrmd_dato = formDeta.getFieldString("frmd_dato");
                    String strfrmd_tipo = formDeta.getFieldString("frmd_tipo");
                    String strfrmd_consulta = formDeta.getFieldString("frmd_consulta");
                    String strfrmd_tablaext = formDeta.getFieldString("frmd_tablaext");
                    String strfrmd_tabla_envio = formDeta.getFieldString("frmd_tabla_envio");
                    String strfrmd_tabla_mostrar = formDeta.getFieldString("frmd_tabla_mostrar");
                    String strfrmd_tabla_pre = formDeta.getFieldString("frmd_tabla_pre");
                    String strfrmd_tabla_post = formDeta.getFieldString("frmd_tabla_post");
                    String strfrmd_alias_envio = formDeta.getFieldString("frmd_alias_envio");
                    String strfrmd_alias_mostrar = formDeta.getFieldString("frmd_alias_mostrar");
                    //Validamos nombre de los campos por enviar
                    String strNomEnvio = new String(strfrmd_tabla_envio);
                    String strNomMostrar = new String(strfrmd_tabla_mostrar);
                    if (!strfrmd_alias_envio.trim().equals("")) {
                        strNomEnvio = new String(strfrmd_alias_envio);
                    }
                    if (!strfrmd_alias_mostrar.trim().equals("")) {
                        strNomMostrar = new String(strfrmd_alias_mostrar);
                    }

                    //Validamos si es un campo a mostrar en las consultas
                    if (strfrmd_consulta.equals("1") && !strfrmd_dato.equals("none")) {
                        //Verificamos si hay que sacar la informacion de otra tabla
                        boolean bolOtraTabla = false;
                        String strValorOtra = "";
                        if (!strfrmd_tablaext.trim().equals("")
                                && !strfrmd_tabla_envio.trim().equals("")
                                && !strfrmd_tabla_mostrar.trim().equals("")) {
                            //Validamos si los datos externos los tenemos en la lista temporal
                            if (formDeta.lstData.containsKey(rs.getString(strfrmd_nombre))) {
                                strValorOtra = new String((String) formDeta.lstData.get(rs.getString(strfrmd_nombre)));
                                bolOtraTabla = true;
                            } else {
                                //Validamos si es factible hacer la busqueda
                                //evitamos buscar id's numericos igual a cero
                                //o cadenas de texto igual a vacias
                                boolean bolBusqueda = true;
                                if (strfrmd_dato.equals("integer")) {
                                    if (rs.getString(strfrmd_nombre).equals("0")) {
                                        bolBusqueda = false;
                                    }
                                } else {
                                    if (strfrmd_dato.equals("text")) {
                                        if (rs.getString(strfrmd_nombre).equals("")) {
                                            bolBusqueda = false;
                                        }
                                    }
                                }
                                if (bolBusqueda) {
                                    //Buscamos los datos externos
                                    String strTBPost = new String(strfrmd_tabla_post);
                                    strTBPost = strTBPost.replace("[no_user]", this.varSesiones.getIntNoUser() + "");
                                    strTBPost = strTBPost.replace("[no_cliente]", this.varSesiones.getIntClienteWork() + "");
                                    strTBPost = strTBPost.replace("[Empresa]", this.varSesiones.getIntClienteWork() + "");
                                    strTBPost = strTBPost.replace("[anio]", this.varSesiones.getIntAnioWork() + "");
                                    strTBPost = strTBPost.replace("[SUCURSAL]", this.varSesiones.getIntSucursalDefault() + "");
                                    strTBPost = strTBPost.replace("[LSTSUCURSAL]", this.varSesiones.getStrLstSucursales() + "");
                                    strTBPost = strTBPost.replace("[EmpresaDef]", this.varSesiones.getIntIdEmpresa() + "");
                                    if (strTBPost.contains("where") || strTBPost.contains("WHERE") || strTBPost.contains("Where")) {
                                        if (strTBPost.contains("order") || strTBPost.contains("ORDER") || strTBPost.contains("Order")) {
                                            int intPos = strTBPost.toLowerCase().indexOf("order");
                                            String strTBPost1 = strTBPost.substring(0, intPos);
                                            String strTBPost2 = strTBPost.substring(intPos, strTBPost.length());
                                            strTBPost = strTBPost1 + " AND " + strNomEnvio + " = '" + rs.getString(strfrmd_nombre).replace("'", "\\'") + "' " + strTBPost2;
                                        } else {
                                            strTBPost += " AND " + strNomEnvio + " = '" + rs.getString(strfrmd_nombre).replace("'", "\\'") + "'";
                                        }
                                    } else {
                                        strTBPost = " where  " + strNomEnvio + " = '" + rs.getString(strfrmd_nombre).replace("'", "\\'") + "'" + strTBPost;
                                    }
                                    String strSqlCombo = "select " + strfrmd_tabla_pre + " " + strfrmd_tabla_envio + ","
                                            + strfrmd_tabla_mostrar + " from " + strfrmd_tablaext + " " + strTBPost
                                            + "  ";
                                    try {
                                        ResultSet rs2 = oConn.runQuery(strSqlCombo, true);
                                        while (rs2.next()) {
                                            bolOtraTabla = true;
                                            String strMostrar = rs2.getString(strNomMostrar);
                                            strValorOtra = new String(strMostrar);
                                            formDeta.lstData.put(rs.getString(strfrmd_nombre).replace("'", "\\'"), strValorOtra);
                                        }
//                              if(rs2.getStatement() != null )rs2.getStatement().close(); 
                                        rs2.close();
                                    } catch (SQLException ex) {
                                        log.error(ex.getMessage());
                                        bitacora.GeneraBitacora(oConn.getStrMsgError(), oConn.getStrUsuario(), ex.getMessage() + " " + ex.getSQLState() + " " + ex.getErrorCode(), oConn);
                                    }
                                }
                            }
                        }
                        if (strfrmd_tipo.equals("radio")) {
                            if (formDeta.lstData.size() > 0) {
                                String strxValor = this.utilXML.Sustituye(rs.getString(strfrmd_nombre));
                                strXML.append("<cell>").append(strxValor).append("</cell>");
                            } else {
                                if (rs.getString(strfrmd_nombre).equals("1")) {
                                    strXML.append("<cell>SI</cell>");
                                } else {
                                    strXML.append("<cell>NO</cell>");
                                }
                            }
                        } else {
                            //Mostramos info de otra tabla
                            if (bolOtraTabla) {
                                String strxValor = this.utilXML.Sustituye(strValorOtra);
                                strXML.append("<cell>").append(strxValor).append("</cell>");
                            } else {
                                                                                                
                                String strxValor = this.utilXML.Sustituye(rs.getString(strfrmd_nombre));
                                if (strfrmd_tipo.equals("date") || strfrmd_tipo.equals("date_per")) {
                                    if (strxValor.length() >= 8) {
                                        strXML.append("<cell>").append(fecha.Formatea(strxValor, "/")).append("</cell>");
                                    } else {
                                        strXML.append("<cell>").append(strxValor).append("</cell>");
                                    }
                                } else {
                                    if (strfrmd_dato.equals("double")) {
                                        try {
                                            double dblValor = Double.valueOf(strxValor);
                                            //strXML += "<cell>" + NumberString.FormatearDecimal(dblValor, this.varSesiones.getNumDecimal()) + "</cell>";
                                            strXML.append("<cell>").append(strxValor).append("</cell>");
                                        } catch (NumberFormatException ex) {
                                            strXML.append("<cell>").append(strxValor).append("</cell>");
                                        }
                                    } else {
                                        strXML.append("<cell>").append(strxValor).append("</cell>");
                                    }
                                }
                            }
                        }

                    }
                }
                strXML.append(" </row>");
            }
            if (rs.getStatement() != null) {
                //rs.getStatement().close();
            }
            rs.close();
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            bitacora.GeneraBitacora(oConn.getStrMsgError(), oConn.getStrUsuario(), ex.getMessage() + " " + ex.getErrorCode() + " " + ex.getLocalizedMessage(), oConn);
        }
        strXML.append("</rows>");
        /**
         * ***********************OBTENEMOS LISTA DE CAMPOS A
         * MOSTRAR************
         */
        return strXML.toString();
    }

    /*Redondea el double*/
    protected double round(double d, int decimalPlace) {
        // see the Javadoc about why we use a String in the constructor
        // http://java.sun.com/j2se/1.5.0/docs/api/java/math/BigDecimal.html#BigDecimal(double)
        BigDecimal bd = new BigDecimal(Double.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_CEILING);
        return bd.doubleValue();
    }

    /**
     * Obtiene el XML del registro actual
     *
     * @return Nos regresa una cadena con el xml del registro actual
     */
    public String DameXml() {
        Fechas fecha = new Fechas();
        String strXML = strXMLHEAD;
        strXML += "<" + this.form.getFieldString("frm_xmlNodoIni") + ">";
        strXML += "<" + this.form.getFieldString("frm_xmlNodoSec") + " ";
        //Recorremos campo por campo
        Iterator it = this.lst.iterator();
        while (it.hasNext()) {
            FormulariosDeta formDeta = (FormulariosDeta) it.next();
            String strfrmd_nombre = formDeta.getFieldString("frmd_nombre");
            String strfrmd_modif = formDeta.getFieldString("frmd_modif");
            String strfrmd_dato = formDeta.getFieldString("frmd_dato");
            String strfrmd_tipo = formDeta.getFieldString("frmd_tipo");
            if (strfrmd_modif.equals("1") && !strfrmd_dato.equals("none")) {
                String strfrmd_valor = this.utilXML.Sustituye(this.getFieldString(strfrmd_nombre));
                if (strfrmd_tipo.equals("date")) {
                    if (strfrmd_valor.length() >= 8) {
                        strXML += strfrmd_nombre + " = \"" + fecha.FormateaDDMMAAAA(strfrmd_valor, "/") + "\" ";
                    } else {
                        strXML += strfrmd_nombre + " = \"" + strfrmd_valor + "\" ";
                    }
                } else {
                    if (strfrmd_dato.equals("double")) {
                        strXML += strfrmd_nombre + " = \"" + strfrmd_valor + "\" ";
                    } else {
                        strXML += strfrmd_nombre + " = \"" + strfrmd_valor + "\" ";
                    }
                }
            }
        }
        strXML += "/>";
        //Ponemos lista de elementos en caso de PanelCheck
        it = this.lst.iterator();
        while (it.hasNext()) {
            FormulariosDeta formDeta = (FormulariosDeta) it.next();
            String strfrmd_nombre = formDeta.getFieldString("frmd_nombre");
            String strfrmd_modif = formDeta.getFieldString("frmd_modif");
            String strfrmd_dato = formDeta.getFieldString("frmd_dato");
            String strfrmd_tipo = formDeta.getFieldString("frmd_tipo");
            String strfrmd_xmlNodoIni = formDeta.getFieldString("frmd_xmlNodoIni");
            String strfrmd_xmlNodoSec = formDeta.getFieldString("frmd_xmlNodoSec");
            if (strfrmd_modif.equals("1") && strfrmd_dato.equals("none") && strfrmd_tipo.equals("PanelCheck")) {
                String strfrmd_valor = "";
                //Insertamos lista de elementos
                strXML += "<" + strfrmd_xmlNodoIni + ">";
                Iterator<String> itF = formDeta.getLstElements().iterator();
                while (itF.hasNext()) {
                    String strElem = itF.next();
                    strXML += "<" + strfrmd_xmlNodoSec + " id=\"" + strElem + "\" />";
                }
                strXML += "</" + strfrmd_xmlNodoIni + ">";
            }
        }
        strXML += "</" + this.form.getFieldString("frm_xmlNodoIni") + ">";
        return strXML;
    }

    /**
     * Consulta los datos de uno o todos los registros de la pantalla
     *
     * @param oConn Es la conexion a la base de datos
     * @return Nos regresa una cadena con el XML de los datos
     */
    public String Consulta(Conexion oConn) {
        Fechas fecha = new Fechas();
        String strXML = strXMLHEAD;
        strXML += "<" + this.form.getFieldString("frm_xmlNodoIni") + ">";
        /*Abrimos recordSet para obtener los datos a mostrar*/
        String strFiltro = "";
        String strSql = "SELECT * FROM " + this.NomTabla + " ";
        boolean bolSearchKey = false;
        if (!this.ValorKey.trim().equals("0") && !this.ValorKey.trim().equals("")) {
            strFiltro += this.NomKey + " = '" + this.ValorKey + "'";
            bolSearchKey = true;
        }
        //Condicion especial
        if (!this.form.getFieldString("frm_gridCondicion").isEmpty()) {
            if (this.form.getFieldString("frm_gridCondicion").contains("[no_user]")) {
                this.form.setFieldString("frm_gridCondicion", this.form.getFieldString("frm_gridCondicion").replace("[no_user]", this.varSesiones.getIntNoUser() + ""));
            }
            if (this.form.getFieldString("frm_gridCondicion").contains("[no_cliente]")) {
                this.form.setFieldString("frm_gridCondicion", this.form.getFieldString("frm_gridCondicion").replace("[no_cliente]", this.varSesiones.getIntClienteWork() + ""));
            }
            if (this.form.getFieldString("frm_gridCondicion").contains("[Empresa]")) {
                this.form.setFieldString("frm_gridCondicion", this.form.getFieldString("frm_gridCondicion").replace("[Empresa]", this.varSesiones.getIntClienteWork() + ""));
            }
            if (this.form.getFieldString("frm_gridCondicion").contains("[anio]")) {
                this.form.setFieldString("frm_gridCondicion", this.form.getFieldString("frm_gridCondicion").replace("[anio]", this.varSesiones.getIntAnioWork() + ""));
            }
            if (this.form.getFieldString("frm_gridCondicion").contains("[SUCURSAL]")) {
                this.form.setFieldString("frm_gridCondicion", this.form.getFieldString("frm_gridCondicion").replace("[SUCURSAL]", this.varSesiones.getIntSucursalDefault() + ""));
            }
            if (this.form.getFieldString("frm_gridCondicion").contains("[EmpresaDef]")) {
                this.form.setFieldString("frm_gridCondicion", this.form.getFieldString("frm_gridCondicion").replace("[EmpresaDef]", this.varSesiones.getIntIdEmpresa() + ""));
            }
            if (this.form.getFieldString("frm_gridCondicion").contains("[LSTSUCURSAL]")) {
                this.form.setFieldString("frm_gridCondicion", this.form.getFieldString("frm_gridCondicion").replace("[LSTSUCURSAL]", varSesiones.getStrLstSucursales() + ""));
            }
        }

        if (!this.form.getFieldString("frm_gridCondicion").equals("")) {
            if (strFiltro.isEmpty()) {
                if (this.form.getFieldString("frm_gridCondicion").startsWith(" AND ")) {
                    strFiltro += " " + this.form.getFieldString("frm_gridCondicion").substring(this.form.getFieldString("frm_gridCondicion").indexOf(" AND ") + 5, this.form.getFieldString("frm_gridCondicion").length());
                } else {
                    strFiltro += " " + this.form.getFieldString("frm_gridCondicion");
                }

            } else {
                strFiltro += " " + this.form.getFieldString("frm_gridCondicion");
            }

        }

        //Validamos si nos mandaron algun otro valor
        Iterator it = this.lst.iterator();
        while (it.hasNext()) {
            FormulariosDeta formDeta = (FormulariosDeta) it.next();
            String strfrmd_nombre = formDeta.getFieldString("frmd_nombre");
            String strfrmd_consulta = formDeta.getFieldString("frmd_consulta");
            String strfrmd_dato = formDeta.getFieldString("frmd_dato");
            String strfrmd_tipo = formDeta.getFieldString("frmd_tipo");
            String strfrmd_search = formDeta.getFieldString("frmd_search");
            String strValor = String.valueOf(this.Fields.get(strfrmd_nombre));
            String strValorDefa = formDeta.getFieldString("frmd_valor");
            log.debug("strfrmd_nombre:" + strfrmd_nombre + " " + strValor);
            if (!bolSearchKey) {

                if (strfrmd_consulta.equals("1") && !strfrmd_dato.equals("none")) {
                    if (strfrmd_dato.equals("integer")) {
                        if (strfrmd_tipo.equals("radio")) {
                            if (!strValorDefa.equals("999") && strfrmd_search.equals("1")) {
                                strFiltro += " AND  " + strfrmd_nombre + " = " + strValor + " ";
                            }
                        } else {
                            if (Integer.valueOf(strValor) != 0) {
                                strFiltro += " AND  " + strfrmd_nombre + " = " + strValor + " ";
                            }
                        }
                    } else {
                        if (strfrmd_dato.equals("double")) {
                            if (Double.valueOf(strValor) != 0) {
                                strFiltro += " AND  " + strfrmd_nombre + " = " + strValor + " ";
                            }
                        } else {
                            if (strfrmd_tipo.equals("date_per")) {
                                if (!strValor.equals("") && !strValor.equals("null")) {
                                    strFiltro += " AND " + strfrmd_nombre + " >= '" + strValor + "' AND " + strfrmd_nombre + " <= '" + formDeta.getStrValorAdi2() + "' ";
                                }
                            } else {
                                if (strfrmd_tipo.equals("date")) {
                                    if (!strValor.equals("") && !strValor.equals("null")) {
                                        strFiltro += " AND  " + strfrmd_nombre + " = '" + strValor + "' ";
                                    }
                                } else {
                                    if (!strValor.equals("") && !strValor.equals("null")) {
                                        strFiltro += " AND  " + strfrmd_nombre + " like '%" + strValor + "%' ";
                                    }
                                }
                            }

                        }
                    }
                }
                //Solo buscan por los otros campos sino buscan por key
            }
        }
        if (strFiltro.trim().length() > 0) {
            if (strFiltro.startsWith(" AND")) {
                strFiltro = " WHERE " + strFiltro.substring(4, strFiltro.length());
            } else {
                strFiltro = " WHERE " + strFiltro;
            }

        }
        strSql = strSql + strFiltro;
        ResultSet rs;
        try {
            rs = oConn.runQuery(strSql, true);
            while (rs.next()) {
                strXML += "<" + this.form.getFieldString("frm_xmlNodoSec") + " ";
                it = this.lst.iterator();
                while (it.hasNext()) {
                    FormulariosDeta formDeta = (FormulariosDeta) it.next();
                    String strfrmd_nombre = formDeta.getFieldString("frmd_nombre");
                    String strfrmd_impresion = formDeta.getFieldString("frmd_impresion");
                    String strfrmd_dato = formDeta.getFieldString("frmd_dato");
                    String strfrmd_tipo = formDeta.getFieldString("frmd_tipo");
                    String strfrmd_tablaext = formDeta.getFieldString("frmd_tablaext");
                    String strfrmd_tabla_envio = formDeta.getFieldString("frmd_tabla_envio");
                    String strfrmd_tabla_mostrar = formDeta.getFieldString("frmd_tabla_mostrar");
                    String strfrmd_tabla_pre = formDeta.getFieldString("frmd_tabla_pre");
                    String strfrmd_tabla_post = formDeta.getFieldString("frmd_tabla_post");
                    String strfrmd_alias_envio = formDeta.getFieldString("frmd_alias_envio");
                    String strfrmd_alias_mostrar = formDeta.getFieldString("frmd_alias_mostrar");
                    //Validamos nombre de los campos por enviar
                    String strNomEnvio = new String(strfrmd_tabla_envio);
                    String strNomMostrar = new String(strfrmd_tabla_mostrar);
                    if (!strfrmd_alias_envio.trim().equals("")) {
                        strNomEnvio = new String(strfrmd_alias_envio);
                    }
                    if (!strfrmd_alias_mostrar.trim().equals("")) {
                        strNomMostrar = new String(strfrmd_alias_mostrar);
                    }
                    if (strfrmd_impresion.equals("1") && !strfrmd_dato.equals("none")) {
                        //Verificamos si hay que sacar la informacion de otra tabla
                        boolean bolOtraTabla = false;
                        String strValorOtra = "";
                        if (!strfrmd_tablaext.trim().equals("")
                                && !strfrmd_tabla_envio.trim().equals("")
                                && !strfrmd_tabla_mostrar.trim().equals("")) {
                            String strTBPost = new String(strfrmd_tabla_post);
                            strTBPost = strTBPost.replace("[no_user]", this.varSesiones.getIntNoUser() + "");
                            strTBPost = strTBPost.replace("[no_cliente]", this.varSesiones.getIntClienteWork() + "");
                            strTBPost = strTBPost.replace("[Empresa]", this.varSesiones.getIntClienteWork() + "");
                            strTBPost = strTBPost.replace("[anio]", this.varSesiones.getIntAnioWork() + "");
                            strTBPost = strTBPost.replace("[SUCURSAL]", this.varSesiones.getIntSucursalDefault() + "");
                            strTBPost = strTBPost.replace("[LSTSUCURSAL]", this.varSesiones.getStrLstSucursales() + "");
                            strTBPost = strTBPost.replace("[EmpresaDef]", this.varSesiones.getIntIdEmpresa() + "");
                            if (strTBPost.contains("where") || strTBPost.contains("WHERE") || strTBPost.contains("Where")) {
                                if (strTBPost.contains("order") || strTBPost.contains("ORDER") || strTBPost.contains("Order")) {
                                    int intPos = strTBPost.toLowerCase().indexOf("order");
                                    String strTBPost1 = strTBPost.substring(0, intPos);
                                    String strTBPost2 = strTBPost.substring(intPos, strTBPost.length());
                                    strTBPost = strTBPost1 + " AND " + strNomEnvio + " = '" + rs.getString(strfrmd_nombre) + "' " + strTBPost2;
                                } else {
                                    strTBPost += " AND " + strNomEnvio + " = '" + rs.getString(strfrmd_nombre) + "'";
                                }
                            } else {
                                strTBPost = " where  " + strNomEnvio + " = '" + rs.getString(strfrmd_nombre) + "'" + strTBPost;
                            }
                            String strSqlCombo = "select " + strfrmd_tabla_pre + " " + strfrmd_tabla_envio + ","
                                    + strfrmd_tabla_mostrar + " from " + strfrmd_tablaext + " " + strTBPost
                                    + "  ";
                            try {
                                ResultSet rs2 = oConn.runQuery(strSqlCombo, true);
                                while (rs2.next()) {
                                    bolOtraTabla = true;
                                    String strMostrar = rs2.getString(strNomMostrar);
                                    strValorOtra = new String(strMostrar);
                                }
//                        if(rs2.getStatement() != null )rs2.getStatement().close(); 
                                rs2.close();
                            } catch (SQLException ex) {
                                log.error(ex.getMessage());
                                bitacora.GeneraBitacora(oConn.getStrMsgError(), oConn.getStrUsuario(), "", oConn);
                            }
                        }
                        //Mostramos info de otra tabla
                        if (bolOtraTabla) {
                            String strxValor = this.utilXML.Sustituye(strValorOtra);
                            strXML += strfrmd_nombre + " = \"" + strxValor + "\" ";
                        } else {
                            String strfrmd_valor = this.utilXML.Sustituye(rs.getString(strfrmd_nombre));
                            if (strfrmd_tipo.equals("date")) {
                                if (strfrmd_valor.length() >= 8) {
                                    strXML += strfrmd_nombre + " = \"" + fecha.FormateaDDMMAAAA(strfrmd_valor, "/") + "\" ";
                                } else {
                                    strXML += strfrmd_nombre + " = \"" + strfrmd_valor + "\" ";
                                }
                            } else {
                                strXML += strfrmd_nombre + " = \"" + strfrmd_valor + "\" ";
                            }
                        }
                    }
                }
                strXML += "/>";
            }
            if (rs.getStatement() != null) {
                //rs.getStatement().close();
            }
            rs.close();
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            bitacora.GeneraBitacora(oConn.getStrMsgError(), oConn.getStrUsuario(), "", oConn);
        }
        strXML += "</" + this.form.getFieldString("frm_xmlNodoIni") + ">";
        return strXML;
    }

    /**
     * Nos regresa el valor del campo del encabezado del formulario
     *
     * @param strName Es el nombre del campo a buscar
     * @return Nos regresa un texto con el valor del campo
     */
    public String getDataHead(String strName) {
        return new String(this.form.getFieldString(strName));
    }

    @Override
    public String Agrega(Conexion oConn) {
        //Validamos duplicidad del registro
        boolean bolDuplicidad = false;
        //Validamos la duplicidad
        String strFiltroDuplicidad = "";
        String strLstDuplica = "";
        String strResp = "";
        /*Recorremos el hash Map con los campos*/
        Iterator it = this.lst.iterator();
        while (it.hasNext()) {
            FormulariosDeta formDeta = (FormulariosDeta) it.next();
            String strfrmd_nombre = formDeta.getFieldString("frmd_nombre");
            String strfrmd_titulo = formDeta.getFieldString("frmd_titulo");
            String strfrmd_dato = formDeta.getFieldString("frmd_dato");
            String strfrmd_nuevo = formDeta.getFieldString("frmd_nuevo");
            String strfrmd_duplicidad = formDeta.getFieldString("frmd_duplicidad");
            String strfrmd_valor = this.getFieldString(strfrmd_nombre);
            if (strfrmd_nuevo.equals("1") && !strfrmd_dato.equals("none")) {
                if (strfrmd_duplicidad.equals("1")) {
                    strFiltroDuplicidad += " AND " + strfrmd_nombre + " = '" + strfrmd_valor + "'";
                    strLstDuplica += " " + strfrmd_titulo + " : " + strfrmd_valor + " \n";
                }
            }
        }
        //Armamos query con el campo  a buscar que no este duplicado...
        if (!strFiltroDuplicidad.equals("")) {
            strFiltroDuplicidad = " WHERE " + strFiltroDuplicidad.substring(4, strFiltroDuplicidad.length());
            String strSql = "select * from " + this.NomTabla + " " + strFiltroDuplicidad;
            ResultSet rs;
            try {
                rs = oConn.runQuery(strSql, true);
                while (rs.next()) {
                    strResp = "ERROR:YA EXISTE UN " + this.form.getFieldString("frm_title") + " CON LA SIGUIENTE INFORMACION " + strLstDuplica;
                    bolDuplicidad = true;
                }
                if (rs.getStatement() != null) {
                    //rs.getStatement().close();
                }
                rs.close();
            } catch (SQLException ex) {
                log.error(ex.getMessage());
                bitacora.GeneraBitacora(oConn.getStrMsgError(), oConn.getStrUsuario(), "", oConn);
            }
        }
        //Si no hay duplicidad procedemos almacenar
        if (!bolDuplicidad) {
            //*************Acciones posteriores
            boolean bolEjecuta = true;
            if (this.bolExecScripting) {
                bolEjecuta = Acciones(oConn, "frm_scriptBeforeAdd", null);
            }
            //Si no hay errores en las validaciones se guarda la operacion
            if (bolEjecuta) {
                //*************Acciones posteriores
                strResp = super.Agrega(oConn);
                if (strResp.equals("OK")) {
                    strResp = GuardaPanelCheck(oConn);
                }
                //*************Acciones posteriores
                if (this.bolExecScripting) {
                    bolEjecuta = Acciones(oConn, "frm_scriptAfterAdd", null);
                }
                //*************Acciones posteriores
                //Guardamos la bitacora de acciones
                this.saveBitacora(this.form.getFieldString("frm_abrv"), "NUEVA", this.getFieldInt(this.NomKey), oConn);
            } else {
                strResp = this.strMsgERROR;
            }
        }
        return strResp;
    }

    @Override
    public String Modifica(Conexion oConn) {
        //Validamos duplicidad del registro
        boolean bolDuplicidad = false;
        //Validamos la duplicidad
        String strFiltroDuplicidad = "";
        String strLstDuplica = "";
        String strResp = "";
        /*Recorremos el hash Map con los campos*/
        Iterator it = this.lst.iterator();
        while (it.hasNext()) {
            FormulariosDeta formDeta = (FormulariosDeta) it.next();
            String strfrmd_nombre = formDeta.getFieldString("frmd_nombre");
            String strfrmd_titulo = formDeta.getFieldString("frmd_titulo");
            String strfrmd_dato = formDeta.getFieldString("frmd_dato");
            String strfrmd_nuevo = formDeta.getFieldString("frmd_nuevo");
            String strfrmd_duplicidad = formDeta.getFieldString("frmd_duplicidad");
            String strfrmd_valor = this.getFieldString(strfrmd_nombre);
            if (strfrmd_nuevo.equals("1") && !strfrmd_dato.equals("none")) {
                if (strfrmd_duplicidad.equals("1")) {
                    strFiltroDuplicidad += " AND " + strfrmd_nombre + " = '" + strfrmd_valor + "'";
                    strLstDuplica += " " + strfrmd_titulo + " : " + strfrmd_valor + " \n";
                }
            }
        }
        //Armamos query con el campo  a buscar que no este duplicado...
        if (!strFiltroDuplicidad.equals("")) {
            strFiltroDuplicidad = " WHERE " + strFiltroDuplicidad.substring(4, strFiltroDuplicidad.length());
            String strSql = "select * from " + this.NomTabla + " " + strFiltroDuplicidad + " and  "
                    + this.NomKey + " <> '" + this.ValorKey + "'";
            ResultSet rs;
            try {
                rs = oConn.runQuery(strSql, true);
                while (rs.next()) {
                    strResp = "ERROR:YA EXISTE UN " + this.form.getFieldString("frm_title") + " CON LA SIGUIENTE INFORMACION " + strLstDuplica;
                    bolDuplicidad = true;
                }
                if (rs.getStatement() != null) {
                    //rs.getStatement().close();
                }
                rs.close();
            } catch (SQLException ex) {
                log.error(ex.getMessage());
                bitacora.GeneraBitacora(oConn.getStrMsgError(), oConn.getStrUsuario(), "", oConn);
            }
        }
        if (!bolDuplicidad) {
            //*************Acciones posteriores
            boolean bolEjecuta = true;
            if (this.bolExecScripting) {
                bolEjecuta = Acciones(oConn, "frm_scriptBeforeMod", null);
            }
            //Si no hay errores en las validaciones se guarda la operacion
            if (bolEjecuta) {
                //*************Acciones posteriores
                strResp = super.Modifica(oConn);
                if (strResp.equals("OK")) {
                    strResp = GuardaPanelCheck(oConn);
                }
                //*************Acciones posteriores
                if (this.bolExecScripting) {
                    bolEjecuta = Acciones(oConn, "frm_scriptAfterMod", null);
                }
                //*************Acciones posteriores
                //Guardamos la bitacora de acciones
                this.saveBitacora(this.form.getFieldString("frm_abrv"), "MODIFICA", this.getFieldInt(this.NomKey), oConn);
            } else {
                strResp = this.strMsgERROR;
            }
        }
        return strResp;
    }

    /**
     * Ejecuta las acciones por scripting
     */
    private boolean Acciones(Conexion oConn, String strNomCampo, HSSFSheet sheetXLS) {
        boolean bolEjecuta = true;
        //Obtenemos el valor del script por ejecutar
        String strBefore = this.form.getFieldString(strNomCampo);
        //Si hay un script lo ejecutamos
        if (!strBefore.trim().equals("")) {
            scriptBase script = new scriptBase();
            script.setTabla(this);
            script.setVarSesiones(this.varSesiones);
            if (sheetXLS != null) {
                script.setSheet(sheetXLS);
            }
            bolEjecuta = script.EjecutarFormula(strBefore, oConn);
            if (!bolEjecuta) {
                if (script.getStrMsgERROR() != null) {
                    this.strMsgERROR = script.getStrMsgERROR();
                } else {
                    this.strMsgERROR = "ERROR:DESCONOCIDO";
                }
            }
        }
        //*************Acciones posteriores
        return bolEjecuta;
    }

    @Override
    public String Borra(Conexion oConn) {
        //*************Acciones posteriores
        boolean bolEjecuta = true;
        if (this.bolExecScripting) {
            bolEjecuta = Acciones(oConn, "frm_scriptBeforeDel", null);
        }
        //Si no hay errores en las validaciones se guarda la operacion
        if (bolEjecuta) {
            //Guardamos la bitacora de acciones
            this.saveBitacora(this.form.getFieldString("frm_abrv"), "BORRA", this.getFieldInt(this.NomKey), oConn);
            //*************Acciones posteriores
            return super.Borra(oConn);
        } else {
            return this.strMsgERROR;
        }
    }

    /**
     * Se encarga de guardar los panel check en la tabla definida
     */
    private String GuardaPanelCheck(Conexion oConn) {
        String strResp = "OK";
        //Ponemos lista de elementos en caso de PanelCheck
        Iterator it = this.lst.iterator();
        while (it.hasNext()) {
            FormulariosDeta formDeta = (FormulariosDeta) it.next();
            String strfrmd_modif = formDeta.getFieldString("frmd_modif");
            String strfrmd_dato = formDeta.getFieldString("frmd_dato");
            String strfrmd_tipo = formDeta.getFieldString("frmd_tipo");
            String strfrmd_tabla_envio = formDeta.getFieldString("frmd_tabla_envio");
            if (strfrmd_modif.equals("1") && strfrmd_dato.equals("none") && strfrmd_tipo.equals("PanelCheck")) {
                //Consultamos el nombre de la tabla secundaria
                String strNomTabSec = "";
                String strSql = "Select frmd_tabsec from formularios_deta where frmd_id = '" + formDeta.getFieldString("frmd_id") + "'";
                ResultSet rs;
                try {
                    rs = oConn.runQuery(strSql, true);
                    while (rs.next()) {
                        strNomTabSec = rs.getString("frmd_tabsec");
                    }
                    if (rs.getStatement() != null) {
                        //rs.getStatement().close();
                    }
                    rs.close();
                } catch (SQLException ex) {
                    log.error(ex.getMessage());
                    bitacora.GeneraBitacora(oConn.getStrMsgError(), oConn.getStrUsuario(), "", oConn);
                }
                //Solo procede si el usuario configuro una tabla secundario en caso contrario no hacemos nada
                if (!strNomTabSec.equals("")) {
                    //Limpiamos los valores de la tabla
                    String strSqlDML = "delete from " + strNomTabSec + " where " + this.NomKey + " = '" + this.ValorKey + "'";
                    oConn.runQueryLMD(strSqlDML);
                    Iterator<String> itF = formDeta.getLstElements().iterator();
                    while (itF.hasNext()) {
                        String strElem = itF.next();
                        //Anadimos el elemento
                        strSqlDML = "insert into " + strNomTabSec + "(" + this.NomKey + "," + strfrmd_tabla_envio + ")"
                                + "values"
                                + "('" + this.ValorKey + "','" + strElem + "')";
                        oConn.runQueryLMD(strSqlDML);
                    }
                }
            }
        }
        return strResp;
    }

    @Override
    public String ObtenDatos(Conexion oConn) {
        String strResp = super.ObtenDatos(oConn);
        //Obtenemos datos de los controles PanelCheck
        if (strResp.equals("OK")) {
            strResp = ObtenDatosPanelCheck(oConn);
        }
        return strResp;
    }

    /**
     * Se encarga de guardar los panel check en la tabla definida
     */
    private String ObtenDatosPanelCheck(Conexion oConn) {
        String strResp = "OK";
        //Ponemos lista de elementos en caso de PanelCheck
        Iterator it = this.lst.iterator();
        while (it.hasNext()) {
            FormulariosDeta formDeta = (FormulariosDeta) it.next();
            String strfrmd_modif = formDeta.getFieldString("frmd_modif");
            String strfrmd_dato = formDeta.getFieldString("frmd_dato");
            String strfrmd_tipo = formDeta.getFieldString("frmd_tipo");
            String strfrmd_tabla_envio = formDeta.getFieldString("frmd_tabla_envio");
            if (strfrmd_modif.equals("1") && strfrmd_dato.equals("none") && strfrmd_tipo.equals("PanelCheck")) {
                //Consultamos el nombre de la tabla secundaria
                String strNomTabSec = "";
                String strSql = "Select frmd_tabsec from formularios_deta where frmd_id = '" + formDeta.getFieldString("frmd_id") + "'";
                ResultSet rs;
                try {
                    rs = oConn.runQuery(strSql, true);
                    while (rs.next()) {
                        strNomTabSec = rs.getString("frmd_tabsec");
                    }
                    if (rs.getStatement() != null) {
                        //rs.getStatement().close();
                    }
                    rs.close();
                } catch (SQLException ex) {
                    log.error(ex.getMessage());
                    bitacora.GeneraBitacora(oConn.getStrMsgError(), oConn.getStrUsuario(), "", oConn);
                }
                //Solo recuperamos datos si el usuario configuro la tabla externa
                if (!strNomTabSec.equals("")) {
                    //Recuperamos los valores
                    strSql = "select " + strfrmd_tabla_envio + " from  " + strNomTabSec + " where " + this.NomKey + " = "
                            + "'" + this.ValorKey + "'";
                    try {
                        rs = oConn.runQuery(strSql, true);
                        while (rs.next()) {
                            String strValor = rs.getString(strfrmd_tabla_envio);
                            formDeta.getLstElements().add(strValor);
                        }
                        if (rs.getStatement() != null) {
                            //rs.getStatement().close();
                        }
                        rs.close();
                    } catch (SQLException ex) {
                        log.error(ex.getMessage());
                        bitacora.GeneraBitacora(oConn.getStrMsgError(), oConn.getStrUsuario(), "", oConn);
                    }
                }
            }
        }
        return strResp;
    }

    /**
     * Guarda la bitacora de usuario del proceso
     *
     * @param strModulo Es el nombre del modulo
     * @param strAction Es la accion a realizar
     * @param intIdOper Es el id de la operacion afectada
     * @param oConn Es la conexion a la base de datos
     */
    protected void saveBitacora(String strModulo, String strAction, int intIdOper, Conexion oConn) {
        Fechas fecha = new Fechas();
        bitacorausers logUser = new bitacorausers();
        logUser.setFieldString("BTU_FECHA", fecha.getFechaActual());
        logUser.setFieldString("BTU_HORA", fecha.getHoraActual());
        logUser.setFieldString("BTU_NOMMOD", strModulo);
        logUser.setFieldString("BTU_NOMACTION", strAction);
        logUser.setFieldInt("BTU_IDOPER", intIdOper);
        logUser.setFieldInt("BTU_IDUSER", varSesiones.getIntNoUser());
        logUser.setFieldString("BTU_NOMUSER", varSesiones.getStrUser());
        logUser.Agrega(oConn);
    }

    /**
     * Carga la informacion en base a un archivo de excel
     *
     * @param oConn Es la conexion a la base de datos
     * @param fileXLS Es el file usado
     * @return Regresa OK si todo salio bien.
     */
    public String ImportaXLS(Conexion oConn, File fileXLS) {
        String strMsgRes = "OK";
        //Evaluamos que el archivo exista
        if (fileXLS.exists()) {
            //Abrimos el XLSFileInputStream myxls = null;
            FileInputStream myxls = null;
            try {
                myxls = new FileInputStream(fileXLS.getAbsolutePath());
                HSSFWorkbook wb = new HSSFWorkbook(myxls);
                strMsgRes = ValidaXLSImporta(oConn, wb);
                if (strMsgRes.equals("OK")) {
                    //Se ejecuta script antes de guardar
                    //*************Acciones posteriores
                    boolean bolEjecuta = true;
                    if (this.bolExecScripting) {
                        bolEjecuta = Acciones(oConn, "frm_script_before_import", wb.getSheetAt(0));
                    }

                    //Si no hay errores en las validaciones se guarda la operacion
                    if (bolEjecuta) {
                        //Comenzamos el proceso de importacion
                        String strResp = GuardaInformacionImporta(oConn, wb.getSheetAt(0));
                        if (strResp.equals("OK")) {
                            //*************Acciones posteriores
                            //Se ejecuta scrip despues de guardar
                            if (this.bolExecScripting) {
                                bolEjecuta = Acciones(oConn, "frm_script_after_import", wb.getSheetAt(0));
                            }
                            //*************Acciones posteriores
                            //Guardamos la bitacora de acciones
                            this.saveBitacora(this.form.getFieldString("frm_abrv"), "IMPORTA", this.getFieldInt(this.NomKey), oConn);
                        } else {
                            strMsgRes = strResp;
                        }
                    } else {
                        strMsgRes = this.strMsgERROR;
                    }
                }
            } catch (IOException ex) {
                log.error(ex.getMessage());
            } finally {
                try {
                    myxls.close();
                } catch (IOException ex) {
                    log.error(ex.getMessage());
                }
            }
        } else {
            strMsgRes = "El archivo no existe, favor de usar un archivo valido.";
        }
        return strMsgRes;
    }

    protected String ValidaXLSImporta(Conexion oConn, HSSFWorkbook wb) {
        String strMsgRes = "OK";
        String strMsgError = "FALTA EL CAMPO: ";
        boolean bolCumple = true;
        HSSFSheet sheet = wb.getSheetAt(0);
        Iterator rows = sheet.rowIterator();
        for (int i = 0; i <= 1; i++) {
            HSSFRow row = (HSSFRow) rows.next();
            Iterator cells = row.cellIterator();
            while (cells.hasNext()) {
                HSSFCell cell = (HSSFCell) cells.next();
                String newString = cell.getStringCellValue();
                int NumColumna = cell.getCellNum();
                lstCeldas.add(newString);
            }//Fin while CELLS
            break;
        }
        //Analizamos el listado de objetos por importar
        Iterator it = this.lst.iterator();
        while (it.hasNext()) {
            FormulariosDeta formDeta = (FormulariosDeta) it.next();
            int intImporta = formDeta.getFieldInt("frmd_se_importa");
            String strFrmd_nombre = formDeta.getFieldString("frmd_nombre");
            if (intImporta == 1) {
                lstFormulariosD.add(strFrmd_nombre);
            }
        }

        if (lstFormulariosD.isEmpty()) {
            strMsgError = "NO SE HAN CONFIGURADO CAMPOS A IMPORTAR";
            bolCumple = false;
        } else {
            for (int f = 1; f <= lstFormulariosD.size(); f++) {
                if (lstCeldas.contains(lstFormulariosD.get(f - 1)) == true) {
                    log.debug("SE ENCONTRO EL CAMPO:    " + lstFormulariosD.get(f - 1));
                }
                if (lstCeldas.contains(lstFormulariosD.get(f - 1)) == false) {
                    strMsgError += "  \"" + lstFormulariosD.get(f - 1) + "\"  ";
                    bolCumple = false;
                }
            }//Fin FOR formularios
        }
        if (bolCumple == true) {
            return strMsgRes;
        } else {
            return strMsgRes = strMsgError;
        }
    }

    /**
     * Guarda la informacion del XLS
     *
     * @param sheet es la hoja de la cual revisaremos los datos
     * @return Regresa Ok si todo salio bien
     */
    protected String GuardaInformacionImporta(Conexion oConn, HSSFSheet sheet) {
        String strMsgRes = "OK";
        String strNomTable = this.NomTabla;

        String strSqliNSERT = "";
        int intImporta = 0;
        String strInsert = "insert into " + strNomTable + "(";
        String strUpdate = "update " + strNomTable + " set ";
        Iterator it = this.lst.iterator();
        String strNomKey = this.form.getFieldString("frm_key");
        boolean bolUpdate = false;

        while (it.hasNext()) {
            FormulariosDeta formDeta = (FormulariosDeta) it.next();
            String strNombreCampo = formDeta.getFieldString("frmd_nombre");
            intImporta = formDeta.getFieldInt("frmd_se_importa");
            formDeta.setIntIdColXls(lstCeldas.indexOf(strNombreCampo));
            if (intImporta == 1) {
                strInsert += strNombreCampo + ",";
            }

        }//Fin WHILE
        if (strInsert.endsWith(",")) {
            strInsert = strInsert.substring(0, strInsert.length() - 1);
        }
        strInsert += ")values ";

        //Recorres filas
        Iterator<Row> rowIterator = sheet.iterator();

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if (row.getRowNum() >= 1) {
                bolUpdate = false;
                //Validamos si es insert o update
                it = this.lst.iterator();
                while (it.hasNext()) {
                    FormulariosDeta formDeta = (FormulariosDeta) it.next();
                    int numColumna = formDeta.getIntIdColXls();
                    String strNombreCampo = formDeta.getFieldString("frmd_nombre");
                    String strTipoDato = formDeta.getFieldString("frmd_dato");
                    //Buscar si viene el id
                    if (strNombreCampo.equals(strNomKey)) {
                        Cell cell = row.getCell(numColumna);
                        String valor1 = "";
                        //convesion
                        if (strTipoDato.startsWith("varchar") || strTipoDato.startsWith("text")) {
                            valor1 += "'" + cell.getStringCellValue() + "'";
                        }
                        if (strTipoDato.startsWith("double") || strTipoDato.startsWith("float")) {
                            double tmpdblValor = cell.getNumericCellValue();
                            valor1 = Double.toString(tmpdblValor);
                        }
                        if (strTipoDato.startsWith("integer")) {
                            Double tmpdblValor = cell.getNumericCellValue();
                            int tmpintValor = (int) tmpdblValor.doubleValue();
                            valor1 = Integer.toString(tmpintValor);
                        }
                        if (valor1.trim().isEmpty() || valor1.equals("0")) {
                            bolUpdate = false;
                        } else {
                            bolUpdate = true;
                        }

                    }

                }//Fin WHILE

                //Recorres formularios deta
                StringBuilder strBufferInsert2 = new StringBuilder();
                StringBuilder strBufferUpdate1 = new StringBuilder();
                StringBuilder strBufferUpdate2 = new StringBuilder();
                int intContaCols = 0;
                Iterator itfrmd = this.lst.iterator();
                while (itfrmd.hasNext()) {
                    FormulariosDeta formDeta = (FormulariosDeta) itfrmd.next();
                    int numColumna = formDeta.getIntIdColXls();
                    String strTipoDato = formDeta.getFieldString("frmd_dato");
                    String strNombreCampo = formDeta.getFieldString("frmd_nombre");
                    Cell cell = row.getCell(numColumna);
                    intImporta = formDeta.getFieldInt("frmd_se_importa");
                    if (intImporta == 1) {
                        String valor1 = "";
                        //convesion
                        if (strTipoDato.startsWith("varchar") || strTipoDato.startsWith("text")) {
                            valor1 += "'" + cell.getStringCellValue() + "'";
                        }
                        if (strTipoDato.startsWith("double") || strTipoDato.startsWith("float")) {
                            double tmpdblValor = cell.getNumericCellValue();
                            valor1 = Double.toString(tmpdblValor);
                        }
                        if (strTipoDato.startsWith("integer")) {
                            Double tmpdblValor = cell.getNumericCellValue();
                            int tmpintValor = (int) tmpdblValor.doubleValue();
                            valor1 = Integer.toString(tmpintValor);
                        }

                        if (bolUpdate) {
                            if (!strNombreCampo.equals(strNomKey)) {
                                if (intContaCols == 0) {
                                    strBufferUpdate1.append(" ").append(strNombreCampo).append(" = ").append(valor1);
                                } else {
                                    strBufferUpdate1.append(" ,").append(strNombreCampo).append(" = ").append(valor1);
                                }
                                intContaCols++;
                            } else {
                                strBufferUpdate2.append("  where " + strNombreCampo + " = " + valor1 + ";");
                            }

                        } else {
                            if (intContaCols == 0) {
                                strBufferInsert2.append("(" + valor1);
                            } else {
                                strBufferInsert2.append("," + valor1);
                            }
                            intContaCols++;
                        }

                    }
                }//Fin while que recorre formularios deta
                if (bolUpdate) {
                    strUpdate = "update " + strNomTable + " set " + strBufferUpdate1 + strBufferUpdate2;
                    //Ejecutamos la instruccion
                    oConn.runQueryLMD(strUpdate);
                } else {
                    strSqliNSERT = strInsert + strBufferInsert2.toString() + ");";
                    //Ejecutamos la instruccion
                    oConn.runQueryLMD(strSqliNSERT);
                }

            }//Fin Valida el numero de la fila
        }
        return strMsgRes;
    }

    public String exportaXLS(String strRuta) throws FileNotFoundException, IOException {
        String strRes = "OK";
        String strNomTable = this.NomTabla;
        String excelFileName = strRuta + "Layout " + strNomTable + ".xls";
        String strNomFile = "Layout " + strNomTable + ".xls";
        //name of excel file
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet(strNomTable);
        HSSFRow filaNom = sheet.createRow(0);
        HSSFRow filaPlaceholder = sheet.createRow(1);
        HSSFRow filaTipoDato = sheet.createRow(2);
        int contador = 0;
        HSSFCellStyle style = wb.createCellStyle();
        style = wb.createCellStyle();
        style.setFillForegroundColor(HSSFColor.RED.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderTop((short) 1); // single line border
        style.setBorderBottom((short) 1); // single line border
        Iterator itfrmd = this.lst.iterator();
        while (itfrmd.hasNext()) {
            FormulariosDeta formDeta = (FormulariosDeta) itfrmd.next();
            String strTipoDato = formDeta.getFieldString("frmd_dato");
            String strPlaceholder = formDeta.getFieldString("frmd_placeholder");
            String strNombreCampo = formDeta.getFieldString("frmd_nombre");
            int Importa = formDeta.getFieldInt("frmd_se_importa");
            int obligatorio = formDeta.getFieldInt("frmd_obligatorio");
            if (Importa == 1) {
                HSSFCell cellNombre = filaNom.createCell(contador);
                HSSFCell cellPlaceholder = filaPlaceholder.createCell(contador);
                HSSFCell cellTipoDato = filaTipoDato.createCell(contador);
                cellNombre.setCellValue(strNombreCampo);
                if (obligatorio == 1) {
                    cellNombre.setCellStyle(style);
                }
                cellPlaceholder.setCellValue(strPlaceholder);
                cellTipoDato.setCellValue(strTipoDato);
                sheet.autoSizeColumn(contador);
                contador++;
            }//Fin IF importa
        }//Fin WHILE
        if (contador == 0) {
            strRes = "NO SE HAN CONFIGURADO CAMPOS A IMPORTAR";
        } else {
            strRes = "Archivo exportado correctamente";
        }
        FileOutputStream fileOut = new FileOutputStream(excelFileName);
        //write this workbook to an Outputstream.
        wb.write(fileOut);
        fileOut.flush();
        fileOut.close();
        return strNomFile;
    }//Fin exportaXLS

    // </editor-fold>
}
