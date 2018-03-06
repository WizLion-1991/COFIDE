package comSIWeb.Operaciones;

import com.SIWeb.struts.SelEmpresaAction;
import comSIWeb.ContextoApt.VariableSession;
import comSIWeb.Utilerias.Fechas;
import comSIWeb.Utilerias.UtilXml;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;
import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts.util.*;

/**
 * Representa la tabla FormulariosDeta
 */
public class FormulariosDeta extends TableMaster {

    private UtilXml utilXML;
    private boolean bolesDetaGrid;
    private String strValorAdi2;
    private ArrayList<String> lstElements;
    private String[] lstFieldsNoScreen;
    private static final Logger log = LogManager.getLogger(FormulariosDeta.class.getName());
    private int intIdColXls;

    public FormulariosDeta() {
        super("formularios_deta", "frmd_id", "", "");
        this.Fields.put("frmd_id", 0);
        this.Fields.put("frmd_nombre", "");
        this.Fields.put("frmd_titulo", "");
        this.Fields.put("frmd_valor", "");
        this.Fields.put("frmd_tipo", "");
        this.Fields.put("frmd_dato", "");
        this.Fields.put("frmd_val_num", 0);
        this.Fields.put("frmd_val_txt", 0);
        this.Fields.put("frmd_val_mail", 0);
        this.Fields.put("frm_id", 0);
        this.Fields.put("frmd_orden", 0);
        this.Fields.put("frmd_ancho", 0);
        this.Fields.put("frmd_renglon", 0);
        this.Fields.put("frmd_javascript", "");
        this.Fields.put("frmd_tablaext", "");
        this.Fields.put("frmd_tabla_envio", "");
        this.Fields.put("frmd_tabla_mostrar", "");
        this.Fields.put("frmd_tabla_pre", "");
        this.Fields.put("frmd_tabla_post", "");
        this.Fields.put("frmd_idtitulo", 0);
        this.Fields.put("frmd_maxlen", 0);
        this.Fields.put("frmd_align", "");
        this.Fields.put("frmd_obligatorio", 0);
        this.Fields.put("frmd_nuevo", 0);
        this.Fields.put("frmd_modif", 0);
        this.Fields.put("frmd_consulta", 0);
        this.Fields.put("frmd_savelabel", 0);
        this.Fields.put("frmd_iconalt", "");
        this.Fields.put("frmd_iconlink", "");
        this.Fields.put("frmd_impresion", 0);
        this.Fields.put("frmd_xmlNodoIni", "");
        this.Fields.put("frmd_xmlNodoSec", "");
        this.Fields.put("frmd_alias_envio", "");
        this.Fields.put("frmd_alias_mostrar", "");
        this.Fields.put("frmd_expreg", "");
        this.Fields.put("frmd_msg_expreg", "");
        this.Fields.put("frmd_cuantosagrupo", 0);
        this.Fields.put("frmd_autocomplete", 0);
        this.Fields.put("frmd_autoURL", "");
        this.Fields.put("frmd_autoValor", "");
        this.Fields.put("frmd_oculto", 0);
        this.Fields.put("frmd_duplicidad", 0);
        this.Fields.put("frmd_letras", 0);
        this.Fields.put("frmd_titcorto", "");
        this.Fields.put("frmd_mask", "");
        this.Fields.put("frmd_separator", "");
        this.Fields.put("frmd_esgrid", 0);
        this.Fields.put("frmd_gridid", 0);
        this.Fields.put("frmd_numtab", 0);
        this.Fields.put("frmd_encripta", 0);
        this.Fields.put("frmd_ongridblur", "");
        this.Fields.put("frmd_ongridfocus", "");
        this.Fields.put("frmd_ongridkeypress", "");
        this.Fields.put("frmd_ongridkeydown", "");
        this.Fields.put("frmd_ongridchange", "");
        this.Fields.put("frmd_ongridclick", "");
        this.Fields.put("frmd_IsEditGrid", 0);
        this.Fields.put("frmd_dataInit", "");
        this.Fields.put("frmd_TabNum", 0);
        this.Fields.put("frmd_rowspan", 0);
        this.Fields.put("frmd_colspan", 0);
        this.Fields.put("frmd_nowrap", 0);
        this.Fields.put("frmd_search", 0);
        this.Fields.put("frmd_tabauto", 0);
        this.Fields.put("frmd_readonly", 0);
        this.Fields.put("frmd_nousetd", 0);
        this.Fields.put("frmd_search_unique", 0);
        this.Fields.put("frmd_classdiv", "");
        this.Fields.put("frmd_classdivctrl", "");
        this.Fields.put("frmd_classctrl", "");
        this.Fields.put("frmd_options", "");
        this.Fields.put("frmd_edit_url", "");
        this.Fields.put("frmd_placeholder", "");
        this.Fields.put("frmd_formula_sql", "");
        this.Fields.put("frmd_formula_alias", "");
        this.Fields.put("frmd_grid_decimal_places", 0);
        this.Fields.put("frmd_grid_formatter", "");
        this.Fields.put("frmd_grid_unformat", "");
        this.Fields.put("frmd_importa", 0);
        this.Fields.put("frmd_se_importa", 0);
        this.lstElements = new ArrayList<String>();
        utilXML = new UtilXml();
        bolesDetaGrid = false;
        this.strValorAdi2 = "";
        //Lista de campos que no se envian para generar formularios
        this.lstFieldsNoScreen = new String[12];
        this.lstFieldsNoScreen[0] = "frmd_tablaext";
        this.lstFieldsNoScreen[1] = "frmd_tabla_envio";
        this.lstFieldsNoScreen[2] = "frmd_tabla_mostrar";
        this.lstFieldsNoScreen[3] = "frmd_tabla_pre";
        this.lstFieldsNoScreen[4] = "frmd_tabla_post";
        this.lstFieldsNoScreen[5] = "frmd_idtitulo";
        this.lstFieldsNoScreen[6] = "frmd_alias_envio";
        this.lstFieldsNoScreen[7] = "frmd_alias_mostrar";
        this.lstFieldsNoScreen[8] = "frmd_duplicidad";
        this.lstFieldsNoScreen[9] = "frmd_encripta";
        this.lstFieldsNoScreen[10] = "frmd_id";
        this.lstFieldsNoScreen[11] = "frmd_savelabel";
        this.lstFieldsNoScreen[11] = "frmd_search_unique";
    }

    /**
     * Nos regresa una cadena con el par nombre de campo = "Valor campo"
     *
     * @param rs Es un resultset con Datos
     * @param varSesiones Contiene las variables de sesion de la aplicacion
     * @param oConn Es la conexion a la base de datos
     * @param objUtilXml Este objeto nos permitara formatear el XML
     * @param request Es el objeto request
     * @return Regresa una cadena
     */
    public String getFieldPar(ResultSet rs, VariableSession varSesiones, Conexion oConn, UtilXml objUtilXml, HttpServletRequest request) {
        String strRes = "";
        Fechas fecha = new Fechas();
        //Variables a usar en el Loop de los campos
        String strNombre = "";
        boolean bolEncontroCombo = false;
        String strTabla = "";
        String strTBEnvio = "";
        String strTBMuestro = "";
        String strTBPre = "";
        String strTBPost = "";
        String strfrmd_alias_envio = "";
        String strfrmd_alias_mostrar = "";
        char chr10 = 10;
        boolean bolEsGrid = false;
        int intIdGrid = 0;
        String strNomElementXml = "ctrl";//Es el nombre con el que se presentara en el XML
        if (bolesDetaGrid) {
            strNomElementXml = "formdeta";
        }
        boolean bolEditar = false;
        /*Recorremos el hash Map con los campos*/
        TreeSet keys = new TreeSet(this.Fields.keySet());
        Iterator it = keys.iterator();
        while (it.hasNext()) {
            String strName = (String) it.next();
            String strValue = String.valueOf(this.Fields.get(strName));
            strValue = strValue.replace("&&", "##");

            if (strName.equals("frmd_nombre")) {//Obtenemos el nombre del campo
                strNombre = new String(strValue);
            }
            //Validamos si el campo se usara para editar bolEditar = false;
            if (strName.equals("frmd_nuevo") || strName.equals("frmd_modif")) {
                if (strValue.equals("1")) {
                    bolEditar = true;
                }
            }
            if (strName.equals("frmd_valor")//Obtenemos el valor del campo
                    /*&& !String.valueOf(this.Fields.get("frmd_dato")).toLowerCase().equals("none")*/) {

                if (strValue.equals("[FECHA]")) {
                    strValue = fecha.getFechaActualDDMMAAAADiagonal();
                }
                if (strValue.equals("[HORA]")) {
                    strValue = fecha.getHoraActual();
                }
                if (strValue.equals("[no_user]")) {
                    strValue = varSesiones.getIntNoUser() + "";
                }
                if (strValue.equals("[anio]")) {
                    strValue = varSesiones.getIntAnioWork() + "";
                }
                if (strValue.equals("[ANIO]")) {
                    strValue = fecha.getAnioActual() + "";
                }
                if (strValue.equals("[MES]")) {
                    int intTmpMes = fecha.getMesActual() + 1;
                    String strMes = "0" + intTmpMes;
                    if (strMes.length() == 3) {
                        strMes = strMes.substring(1, 3);
                    }
                    strValue = strMes;
                }
                if (strValue.equals("[Empresa]")) {
                    strValue = varSesiones.getIntClienteWork() + "";
                }
                if (strValue.equals("[idcliente]")) {
                    strValue = varSesiones.getintIdCliente() + "";
                }
                if (strValue.equals("[SUCURSAL]")) {
                    strValue = varSesiones.getIntSucursalDefault() + "";
                }
                if (strValue.equals("[EmpresaDef]")) {
                    strValue = varSesiones.getIntIdEmpresa() + "";
                }
                if (rs != null) {
                    try {
                        strValue = rs.getString(strNombre);
                        if (String.valueOf(this.Fields.get("frmd_dato")).toLowerCase().equals("date")
                                && String.valueOf(this.Fields.get("frmd_tipo")).toLowerCase().equals("label")) {
                            if (strValue.length() == 8) {
                                strValue = strValue.substring(strValue.length() - 2, strValue.length()) + "/"
                                        + strValue.substring(4, 6) + "/"
                                        + strValue.substring(0, 4);
                            }
                        }
                    } catch (SQLException ex) {
                        log.error(ex.getMessage());
                    }
                }
            }
            //Validamos las mascaras para las que se hagan de manera dinamicas
            if (strName.equals("frmd_mask")) {
                if (strValue.equals("[MskCta]")) {//Mascara automatica para los numero de cuenta contables
                    //9999?-9999-9999
                    strValue = varSesiones.getStrMskCta();
//               String strdigt = "";
//               for (int cMsk = 1; cMsk < varSesiones.getNumDigitos(); cMsk++) {
//                  strdigt += "9";
//               }
//               strValue = "";
//               for (int cMsk = 1; cMsk <= 4; cMsk++) {
//                  if (cMsk == 1) {
//                     strValue += strdigt + "?-";
//                  } else {
//                     strValue += strdigt + "-";
//                  }
//               }
//               if (strValue.endsWith("-")) {//Quitamos el - que sobra
//                  strValue = new String(strValue.substring(0, strValue.length() - 1));
//               }
                }
            }

            //Validamos si es un combo
            if (strName.equals("frmd_tablaext")) {
                bolEncontroCombo = true;
                strTabla = new String(strValue.trim());
                strValue = "";
            }
            if (strName.equals("frmd_tabla_envio")) {
                strTBEnvio = new String(strValue);
                strValue = "";
            }
            if (strName.equals("frmd_tabla_mostrar")) {
                strTBMuestro = new String(strValue);
                strValue = "";
            }
            if (strName.equals("frmd_tabla_pre")) {
                strTBPre = new String(strValue);
                strValue = "";
            }
            if (strName.equals("frmd_tabla_post")) {
                strTBPost = new String(strValue);
                strValue = "";
            }
            if (strName.equals("frmd_alias_envio")) {
                strfrmd_alias_envio = new String(strValue);
                strValue = "";
            }
            if (strName.equals("frmd_alias_mostrar")) {
                strfrmd_alias_mostrar = new String(strValue);
                strValue = "";
            }
            if (strName.equals("frmd_esgrid")) {
                if (strValue.equals("1")) {
                    bolEsGrid = true;
                }
            }
            if (strName.equals("frmd_gridid")) {
                intIdGrid = Integer.valueOf(strValue);
            }
            //El atributo y el Valor
            boolean bolAplica = true;
            //Validamos si el campo aplica para mostrarlo en el generador de formularios
            for (String lstFieldsNoScreen1 : this.lstFieldsNoScreen) {
                if (lstFieldsNoScreen1.equals(strName)) {
                    bolAplica = false;
                }
            }
            if (bolAplica) {
                strValue = this.utilXML.Sustituye(strValue);
                strRes += strName + " = \"" + strValue + "\" ";
            }
        }
        /*Validamos si es un combo para pintar las opciones que contendra*/
        if (bolEncontroCombo && !strTabla.equals("") && bolEditar) {
            strRes += ">" + chr10;
            strRes += "<elements>" + chr10;
            //Obtenemos los datos externos
            if (strTabla.equals("[gen_anio]")) {
                for (int i = 2000; i < fecha.getAnioActual(); i++) {
                    strRes += "<element send=\"" + i + "\" show=\"" + i + "\" />";
                }
            } else {
                if (!strTBEnvio.equals("") && !strTBMuestro.equals("")) {
                    strTBPost = strTBPost.replace("[no_user]", varSesiones.getIntNoUser() + "");
                    strTBPost = strTBPost.replace("[no_cliente]", varSesiones.getIntClienteWork() + "");
                    strTBPost = strTBPost.replace("[anio]", varSesiones.getIntAnioWork() + "");
                    strTBPost = strTBPost.replace("[SUCURSAL]", varSesiones.getIntSucursalDefault() + "");
                    strTBPost = strTBPost.replace("[LSTSUCURSAL]", varSesiones.getStrLstSucursales() + "");
                    strTBPost = strTBPost.replace("[EmpresaDef]", varSesiones.getIntIdEmpresa() + "");
                    MessageResources messageResources = null;
                    //Validamos si la tabla es la de permisos recuperamos la tabla de mensajes
                    String strAppend = "";
                    if (strTabla.equals("permisos_sistema")) {
                        SelEmpresaAction sel = new SelEmpresaAction();
                        if (request != null) {
                            strAppend = ",PS_ORDEN";
                            try {
                                messageResources = sel.getmessageResources(request);
                            } catch (Exception ex) {
                                System.out.println("No se encontro el repositorio de recursos struts");
                            }
                        }
                    }

                    String strSql = "select " + strTBPre + " " + strTBEnvio + "," + strTBMuestro + strAppend + " from " + strTabla + " " + strTBPost;
                    ResultSet rsCombo;
                    try {
                        rsCombo = oConn.runQuery(strSql, true);
                        ResultSetMetaData rsMeta = rsCombo.getMetaData();
                        while (rsCombo.next()) {
                            if (!strfrmd_alias_envio.trim().equals("")) {
                                strTBEnvio = strfrmd_alias_envio;
                            }
                            if (!strfrmd_alias_mostrar.trim().equals("")) {
                                strTBMuestro = strfrmd_alias_mostrar;
                            }
                            int intTipoData1 = rsMeta.getColumnType(1);
                            int intTipoData2 = rsMeta.getColumnType(2);
                            String strMostrar = null;
                            String strEnviar = null;
                            if (intTipoData1 == 8) {
                                strEnviar = String.valueOf(rsCombo.getDouble(strTBEnvio));
                            } else {
                                strEnviar = rsCombo.getString(strTBEnvio);
                            }
                            if (intTipoData2 == 8) {
                                strMostrar = String.valueOf(rsCombo.getDouble(strTBMuestro));
                            } else {
                                strMostrar = rsCombo.getString(strTBMuestro);
                            }
                            //Validamos si la tabla es la de permisos recuperamos la tabla de mensajes
                            if (strTabla.equals("permisos_sistema") && (strTBMuestro.equals("PS_DESCRIPCION")
                                    || strTBMuestro.equals("PERM"))) {
                                if (messageResources != null) {
                                    try {
                                        strMostrar = messageResources.getMessage(strMostrar, request);
                                    } catch (Exception ex) {
                                        System.out.println("No se encontro el mensaje en struts " + strMostrar);
                                    }
                                    if (strMostrar == null) {
                                        strMostrar = "";
                                    }
                                    int intOrden = rsCombo.getInt("PS_ORDEN");
                                    if (intOrden == 0) {
                                        strMostrar = "[-]" + strMostrar.toUpperCase();
                                    }
                                }
                            }
                            strMostrar = objUtilXml.Sustituye(strMostrar);
                            strEnviar = objUtilXml.Sustituye(strEnviar);
                            strRes += "<element send=\"" + strEnviar + "\" show=\"" + strMostrar + "\" />";
                        }
//                  if(rsCombo.getStatement() != null )rsCombo.getStatement().close(); 
                        rsCombo.close();
                    } catch (SQLException ex) {
                        ex.fillInStackTrace();
                        bitacora.GeneraBitacora(oConn.getStrMsgError(), oConn.getStrUsuario(), "", oConn);
                    }
                }
            }
            strRes += "</elements>" + chr10;
            strRes += "</" + strNomElementXml + ">" + chr10;
        } else {
            /*Validamos si es un grid para pintar el detalle de la misma pantalla*/
            if (bolEsGrid) {
                strRes += ">" + chr10;
                strRes += "<formGrid>" + chr10;
            //Obtenemos datos de la otra pantalla
                //Obtenemos el formulario
                Formularios form = new Formularios();
                form.setBolStrict(true);
                form.ObtenDatos(intIdGrid, oConn);

                strRes += "<form ";
                strRes += form.getFieldPar();
                strRes += ">" + chr10;
                /*Obtenemos los datos de detalle del formulario*/
                FormulariosDeta form_deta = new FormulariosDeta();
                ArrayList<TableMaster> lst = form_deta.ObtenDatosVarios(" frm_id = " + form.getFieldInt("frm_id") + " order by frmd_orden", oConn);
                Iterator it2 = lst.iterator();
                while (it2.hasNext()) {
                    FormulariosDeta formDeta = (FormulariosDeta) it2.next();
                    formDeta.setBolesDetaGrid(true);
                    strRes += "<formdeta ";
                    strRes += formDeta.getFieldPar(rs, varSesiones, oConn, this.utilXML, request);
                }
                //Menus personalizados
                FormulariosMenuopt form_Menu = new FormulariosMenuopt();
                ArrayList<TableMaster> lstMenu = form_Menu.ObtenDatosVarios(" frm_id = " + form.getFieldInt("frm_id") + " order by frmn_orden", oConn);
                it = lstMenu.iterator();
                while (it.hasNext()) {
                    FormulariosMenuopt formMenu = (FormulariosMenuopt) it.next();
                    strRes += "<Menudeta ";
                    strRes += formMenu.getFieldPar();
                    strRes += "/>";
                }
                strRes += "</form>" + chr10;
                strRes += "</formGrid>" + chr10;
                strRes += "</" + strNomElementXml + ">" + chr10;
            } else {
                strRes += " />" + chr10;
            }
        }

        return strRes;
    }

    /**
     * Nos regresa true si es el detalle de un grid
     *
     * @return Regresa un valor boleano
     */
    public boolean isBolesDetaGrid() {
        return bolesDetaGrid;
    }

    /**
     * Definimos si es el detalle de un grid
     *
     * @param bolesDetaGrid Define un valor boleano
     */
    public void setBolesDetaGrid(boolean bolesDetaGrid) {
        this.bolesDetaGrid = bolesDetaGrid;
    }

    /**
     * Nos regresa la lista de elementos del campo
     *
     * @return Regresa un array list de cadenas
     */
    public ArrayList<String> getLstElements() {
        return lstElements;
    }

    /**
     * Regresa el valor de la fecha 2(en caso de date_period)
     *
     * @return Es una cadena con una fecha
     */
    public String getStrValorAdi2() {
        return strValorAdi2;
    }

    /**
     * Establece el valor de la fecha 2(en caso de date_period)
     *
     * @param strValorAdi2 Es una cadena con una fecha
     */
    public void setStrValorAdi2(String strValorAdi2) {
        this.strValorAdi2 = strValorAdi2;
    }

    /**
     * Carga los datos de varios registros dependiendo de la condicion
     *
     * @param strCond Es la condicion de los datos por obtener
     * @param oConn Es la conexion a la base de datos
     * @return Nos regresa un arrayList con los datos obtenidos
     */
    @Override
    public ArrayList<TableMaster> ObtenDatosVarios(String strCond, Conexion oConn) {
        ArrayList<TableMaster> lstObjetos = new ArrayList<TableMaster>();
        String strSql = "select * from " + this.NomTabla + " where " + strCond + "";
        try {
            ResultSet rs = oConn.runQuery(strSql, true);
            ResultSetMetaData rsmd = rs.getMetaData();
            int numberOfColumns = rsmd.getColumnCount();
            while (rs.next()) {
                FormulariosDeta mast = null;
                mast = (FormulariosDeta) this.clone();
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
                            } else {
                                if (rsmd.getColumnTypeName(i).toUpperCase().startsWith("CLOB")) {
                                    String strValue = rs.getString(strNomField);
                                    mast.Fields.put(strNomField, strValue);
                                } else {
                                    if (rsmd.getColumnTypeName(i).toUpperCase().startsWith("VARCHAR") || rsmd.getColumnTypeName(i).toUpperCase().startsWith("NVARCHAR")) {
                                        String strValue = rs.getString(strNomField);
                                        mast.Fields.put(strNomField, strValue);
                                    } else {
                                        if (rsmd.getColumnTypeName(i).toUpperCase().startsWith("DECIMAL") || rsmd.getColumnTypeName(i).toUpperCase().startsWith("NUMERIC") || rsmd.getColumnTypeName(i).toUpperCase().startsWith("BIGINT")) {
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
                                                    if (rsmd.getColumnTypeName(i).toUpperCase().startsWith("TEXT") || rsmd.getColumnTypeName(i).toUpperCase().startsWith("NTEXT")) {
                                                        String strValue = rs.getString(strNomField);
                                                        mast.Fields.put(strNomField, strValue);
                                                    } else {
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
        obj = super.clone();
        FormulariosDeta tab = (FormulariosDeta) obj;
        tab.lstElements = (ArrayList) tab.lstElements.clone();
        TreeSet keys = new TreeSet(this.Fields.keySet());
        Iterator it = keys.iterator();
        while (it.hasNext()) {
            String strNomField = (String) it.next();
            int intValor = 0;
            double dblValor = 0;
            String strValor = "";
            try {
                strValor = new String((String) this.Fields.get(strNomField));
                tab.Fields.put(strNomField, new String(strValor));
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

    public int getIntIdColXls() {
        return intIdColXls;
    }

    /**
     * Define el numero de columna en el archivo xls(Importacion)
     *
     * @param intIdColXls
     */
    public void setIntIdColXls(int intIdColXls) {
        this.intIdColXls = intIdColXls;
    }
}
