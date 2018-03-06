<%-- 
    Document   : ERP_PedidosMak
    Created on : Dec 12, 2015, 10:42:04 AM
    Author     : Vladimir
--%>

<%@page import="java.util.ArrayList"%>
<%@page import="comSIWeb.ContextoApt.Seguridad"%>
<%@page import="comSIWeb.Operaciones.Conexion"%>
<%@page import="comSIWeb.ContextoApt.VariableSession"%>
<%@page import="java.sql.ResultSet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    /*Obtenemos las variables de sesion*/
    VariableSession varSesiones = new VariableSession(request);
    varSesiones.getVars();
    /*
    //Abrimos la coneaxion
    Conexion oConn = new Conexion(varSesiones.getStrUser(), this.getServletContext());
    oConn.open();
    //Instanciamos objeto de seguridad para no permitir el acceso externo a ver los XML
    Seguridad seg = new Seguridad();//Valida que la peticion se halla hecho desde el mismo sitio
    if (varSesiones.getIntNoUser() != 0 && seg.ValidaURL(request)) {

        int identiPantallaMak = Integer.valueOf(request.getParameter("identiPantallaMak"));
        String estatusCotizaEditMak = "";
        if (request.getParameter("estatusCotizaEditMak") != null) {
            estatusCotizaEditMak = request.getParameter("estatusCotizaEditMak");
        }
        String strNomSucursal = "";
        int intSucursal = varSesiones.getIntSucursalDefault();
        String strSql = "Select SM_ID,SM_NOMBRE From vta_sucursales_master Where SM_ID = " + varSesiones.getIntSucursalMaster();
        ResultSet rs = oConn.runQuery(strSql, true);
        while (rs.next()) {
            strNomSucursal = rs.getString("SM_NOMBRE");
        }
        rs.close();
        double dblPorcentajeMax = 0;
        String strSqlPorcMax = "Select US_MAX_DESC from usuarios where id_usuarios =" + varSesiones.getIntNoUser();
        ResultSet rsP = oConn.runQuery(strSqlPorcMax, true);
        while (rsP.next()) {
            dblPorcentajeMax = rsP.getDouble("US_MAX_DESC");
        }
        rsP.close();
        /*Obtenemos los valores de los select* /
        StringBuilder strMoneda = new StringBuilder();
        StringBuilder strBodega = new StringBuilder();
        StringBuilder strTransporte = new StringBuilder();
        StringBuilder strVendedor = new StringBuilder();
        StringBuilder strListPrecio = new StringBuilder();
        StringBuilder strTipoCambio = new StringBuilder();

        String strSqlMonedas = "select MON_ID,MON_DESCRIPCION from vta_monedas";
        ResultSet rs1 = oConn.runQuery(strSqlMonedas, true);
        strMoneda.append("<option value = '0' >Seleccione</option>");
        while (rs1.next()) {
            strMoneda.append("<option value='" + rs1.getInt("MON_ID") + "'>" + rs1.getString("MON_DESCRIPCION") + "</option>");
        }
        rs1.close();

        String strSqlBodega = "SELECT  vta_sucursal.SC_ID,vta_sucursal.SC_NOMBRE  FROM vta_sucursal_master_as,  vta_sucursal where  vta_sucursal_master_as.SC_ID =  vta_sucursal.SC_ID  and vta_sucursal_master_as.SM_ID =" + varSesiones.getIntSucursalMaster();
        ResultSet rs2 = oConn.runQuery(strSqlBodega, true);
        strBodega.append("<option value = '0' >Seleccione</option>");
        while (rs2.next()) {
            strBodega.append("<option value='" + rs2.getInt("SC_ID") + "'>" + rs2.getString("SC_NOMBRE") + "</option>");
        }
        rs2.close();

        String strSqlTransporte = "select TR_ID,TR_TRANSPORTISTA from vta_transportista";
        ResultSet rs3 = oConn.runQuery(strSqlTransporte, true);
        strTransporte.append("<option value = '0' >Seleccione</option>");
        while (rs3.next()) {
            strTransporte.append("<option value='" + rs3.getInt("TR_ID") + "'>" + rs3.getString("TR_TRANSPORTISTA") + "</option>");
        }
        rs3.close();

        String strSqlVendedor = "select VE_ID,VE_NOMBRE from vta_vendedor";
        ResultSet rs4 = oConn.runQuery(strSqlVendedor, true);
        strVendedor.append("<option value = '0' >Seleccione</option>");
        while (rs4.next()) {
            strVendedor.append("<option value='" + rs4.getInt("VE_ID") + "'>" + rs4.getString("VE_NOMBRE") + "</option>");
        }
        rs4.close();

        String strSqlLprecios = "select LP_ID,LP_DESCRIPCION from vta_lprecios";
        ResultSet rs5 = oConn.runQuery(strSqlLprecios, true);
        strListPrecio.append("<option value = '0' >Seleccione</option>");
        while (rs5.next()) {
            strListPrecio.append("<option value='" + rs5.getInt("LP_ID") + "'>" + rs5.getString("LP_DESCRIPCION") + "</option>");
        }
        rs5.close();

        String strSqlTCambio = "select TTC_ID,TC_DESCRIPCION from vta_tipocambio";
        ResultSet rs6 = oConn.runQuery(strSqlTCambio, true);
        strTipoCambio.append("<option value = '0' >Seleccione</option>");
        while (rs6.next()) {
            strTipoCambio.append("<option value='" + rs6.getInt("TTC_ID") + "'>" + rs6.getString("TC_DESCRIPCION") + "</option>");
        }
        rs6.close();
     */
    int identiPantallaMak = 1;
    String strNomSucursal = "prueba";
    String strListPrecio = "";
    String strMoneda = "";
    String strTipoCambio = "";
    String strBodega = "";
    String strVendedor = "";
    String strTransporte = "";
    String estatusCotizaEditMak = "CERRADO";
    double dblPorcentajeMax = 0;
    int intSucursal = 0;
%>
<script>
    function AddGridPediMak() {
        var identiPantallaMak = 1;
        var boolShowBAck = true;
        if (identiPantallaMak == 1) {
            boolShowBAck = false;
        } else {
            boolShowBAck = true;
        }
        jQuery("#FAC_GRID").jqGrid({
            url: "_blank.jsp",
            datatype: 'xml',
            height: "auto",
            colNames: ["CODIGO", "DESCRIPCION", "EXISTENCIA", "DISPONIBLE", "CANTIDAD", "FACD_PUNTOS", "FACD_NEGOCIO", "PRECIO", "FACD_ID", "FAC_ID", "IMPORTE", "DESCUENTO", "CORTESIA", "FACD_PORDESC", "MODIFICO PRECIO", "FACD_TASAIVA2", "FACD_TASAIVA1", "FACD_TASAIVA3", "FACD_DESGLOSA1", "FACD_DESGLOSA2", "FACD_USA_SERIE", "FACD_SERIES", "FACD_SERIES_MPD", "FACD_SERIES_O", "FACD_SERIES_MPD_O", "FACD_DESGLOSA3", "FACD_NEGO_ZERO", "SUC_ID", "FACD_IMPUESTO1", "FACD_IMPUESTO2", "FACD_IMPUESTO3", "ID PRODUCTO", "FACD_NOSERIE", "FACD_ESREGALO", "FACD_EXENTO1", "FACD_IMPORTEREAL", "FACD_EXENTO2", "FACD_EXENTO3", "FACD_REQEXIST", "FACD_NOSERIE", "FACD_ESREGALO", "FACD_PRECREAL", "FACD_ESDEVO", "FACD_NOTAS", "FACD_CODBARRAS", "FACD_CANTPEDIDO", "FACD_RET_ISR", "FACD_RET_IVA", "FACD_SINPRECIO", "FACD_RET_FLETE", "UNIDAD MEDIDA", "FACD_PUNTOS_U", "FACD_NEGOCIO_U", "FACD_PR_CAT1", "FACD_PR_CAT2", "FACD_PR_CAT3", "FACD_PR_CAT4", "FACD_PR_CAT5", "FACD_PR_CAT6", "FACD_PR_CAT7", "FACD_PR_CAT8", "FACD_PR_CAT9", "FACD_PR_CAT10", "FACD_DESC_ORI", "FACD_REGALO", "FACD_ID_PROMO", "FACD_DESC_PREC", "FACD_DESC_PTO", "FACD_DESC_VN", "FACD_DESC_LEAL", "ES BACKORDER", "FACD_TIPO_BACKORDER", "TIPO BACKORDER", "FACD_DETALLE_BACKORDER", "FACD_REQUISICION", "FACD_ES_PAQUETE", "[P]", "FACD_PR_PAQUETE", "FACD_MULTIPLO", "MPV", "FACD_CANTIDAD_MULTIPLOS", "FACD_PRECIO_MULTIPLOS", "FACD_PRECIO_TOT_MULTIPLOS", "FACD_ES_MULTIPLO"],
            colModel: [
                {name: 'FACD_CVE', index: 'CODIGO', hidden: false, editable: false, width: 20, sortable: false, search: false},
                {name: 'FACD_DESCRIPCION', index: 'DESCRIPCION', hidden: false, editable: false, width: 70, sortable: false, search: false},
                {name: 'FACD_EXIST', align: 'right', index: 'EXISTENCIA', hidden: false, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_DISPONIBLE', align: 'right', index: 'DISPONIBLE', hidden: false, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_CANTIDAD', edittype: 'text', editable: true, align: 'right', index: 'CANTIDAD', hidden: false, width: 40, sortable: false, search: false,
                    editoptions: {
                        dataEvents: [
                            /*Evento onkeypress para hacer uso del Enter*/
                            {type: 'keypress', fn: function (e) {
                                    var grid = jQuery('#FAC_GRID');
                                    var id = grid.getGridParam("selrow");
                                    var lstRow = grid.getRowData(id);
                                    if (validaNumerosPediInt(e) == true) {
                                        if (e.originalEvent.keyCode == 13) {
                                            grid.saveRow(id);
                                            CalculaImportesPediMak(id);
                                            grid.jqGrid('resetSelection');
                                            grid.trigger("reloadGrid");
                                            lastSelXghY = 0;
                                        }
                                    } else {
                                        lstRow.FACD_CANTIDAD = '';
                                        grid.setRowData(id, lstRow);
                                        grid.saveRow(id);
                                        grid.jqGrid('resetSelection');
                                        grid.trigger("reloadGrid");
                                        lastSelXghY = 0;
                                    }
                                }
                            }
                        ]}
                },
                {name: 'FACD_PUNTOS', index: 'FACD_PUNTOS', hidden: true, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_NEGOCIO', index: 'FACD_NEGOCIO', hidden: true, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_PRECIO', align: 'right', index: 'PRECIO', hidden: false, editable: false, width: 40, sortable: false, formatter: 'number', search: false},
                {name: 'FACD_ID', index: 'FACD_ID', hidden: true, editable: false, width: 40, sortable: false, search: false},
                {name: 'FAC_ID', index: 'FAC_ID', hidden: true, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_IMPORTE', align: 'right', index: 'IMPORTE', hidden: false, editable: false, width: 40, sortable: false, formatter: 'number', search: false},
                {name: 'FACD_DESCUENTO', align: 'right', index: 'DESCUENTO', hidden: false, editable: false, width: 40, sortable: false, formatter: 'number', search: false},
                {name: 'FACD_CORTESIA', edittype: 'text', editable: true, align: 'right', index: 'CORTESIA', hidden: false, width: 40, sortable: false, search: false,
                    editoptions: {
                        dataEvents: [
                            /*Evento onkeypress para hacer uso del Enter*/
                            {type: 'keypress', fn: function (e) {
                                    var grid = jQuery('#FAC_GRID');
                                    var id = grid.getGridParam("selrow");
                                    var lstRow = grid.getRowData(id);
                                    if (validaNumerosPediInt(e) == true) {
                                        if (e.originalEvent.keyCode == 13) {
                                            if (bolPermConfirmCorteMak) {
                                                grid.saveRow(id);
                                                grid.jqGrid('resetSelection');
                                                grid.trigger("reloadGrid");
                                                lastSelXghY = 0;
                                            } else {
                                                alert("No tiene permisos para agregar cortesias.");
                                                lstRow.FACD_CORTESIA = '0';
                                                lstRow.FACD_CANTIDAD = lstRow.FACD_CANTIDAD;
                                                grid.setRowData(id, lstRow);
                                                grid.saveRow(id);
//                                            grid.jqGrid('resetSelection');
//                                            grid.trigger("reloadGrid");
                                                lastSelXghY = 0;
                                            }
                                        }
                                    } else {
                                        lstRow.FACD_CANTIDAD = '';
                                        grid.setRowData(id, lstRow);
                                        grid.saveRow(id);
                                        grid.jqGrid('resetSelection');
                                        grid.trigger("reloadGrid");
                                        lastSelXghY = 0;
                                    }
                                }
                            }
                        ]}
                },
                {name: 'FACD_PORDESC', index: 'FACD_PORDESC', hidden: true, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_PRECFIJO', index: 'FACD_PRECFIJO', hidden: false, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_TASAIVA2', index: 'FACD_TASAIVA2', hidden: true, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_TASAIVA1', index: 'FACD_TASAIVA1', hidden: true, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_TASAIVA3', index: 'FACD_TASAIVA3', hidden: true, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_DESGLOSA1', index: 'FACD_DESGLOSA1', hidden: true, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_DESGLOSA2', index: 'FACD_DESGLOSA2', hidden: true, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_USA_SERIE', index: 'FACD_USA_SERIE', hidden: true, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_SERIES', index: 'FACD_SERIES', hidden: true, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_SERIES_MPD', index: 'FACD_SERIES_MPD', hidden: true, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_SERIES_O', index: 'FACD_SERIES_O', hidden: true, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_SERIES_MPD_O', index: 'FACD_SERIES_MPD_O', hidden: true, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_DESGLOSA3', index: 'FACD_DESGLOSA3', hidden: true, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_NEGO_ZERO', index: 'FACD_NEGO_ZERO', hidden: true, editable: false, width: 40, sortable: false, search: false},
                {name: 'SUC_ID', index: 'SUC_ID', hidden: true, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_IMPUESTO1', index: 'FACD_IMPUESTO1', hidden: true, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_IMPUESTO2', index: 'FACD_IMPUESTO2', hidden: true, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_IMPUESTO3', index: 'FACD_IMPUESTO3', hidden: true, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_PR_ID', align: 'right', index: 'ID PRODUCTO', hidden: true, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_NOSERIE', index: 'FACD_NOSERIE', hidden: true, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_ESREGALO', index: 'FACD_ESREGALO', hidden: true, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_EXENTO1', index: 'FACD_EXENTO1', hidden: true, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_IMPORTEREAL', index: 'FACD_IMPORTEREAL', hidden: true, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_EXENTO2', index: 'FACD_EXENTO2', hidden: true, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_EXENTO3', index: 'FACD_EXENTO3', hidden: true, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_REQEXIST', index: 'FACD_REQEXIST', hidden: true, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_NOSERIE', index: 'FACD_NOSERIE', hidden: true, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_ESREGALO', index: 'FACD_ESREGALO', hidden: true, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_PRECREAL', index: 'FACD_PRECREAL', hidden: true, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_ESDEVO', index: 'FACD_ESDEVO', hidden: true, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_NOTAS', index: 'FACD_NOTAS', hidden: true, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_CODBARRAS', index: 'FACD_CODBARRAS', hidden: true, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_CANTPEDIDO', index: 'FACD_CANTPEDIDO', hidden: true, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_RET_ISR', index: 'FACD_RET_ISR', hidden: true, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_RET_IVA', index: 'FACD_RET_IVA', hidden: true, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_SINPRECIO', index: 'FACD_SINPRECIO', hidden: true, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_RET_FLETE', index: 'FACD_RET_FLETE', hidden: true, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_UNIDAD_MEDIDA', index: 'UNIDAD MEDIDA', hidden: false, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_PUNTOS_U', index: 'FACD_PUNTOS_U', hidden: true, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_NEGOCIO_U', index: 'FACD_NEGOCIO_U', hidden: true, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_PR_CAT1', index: 'FACD_PR_CAT1', hidden: true, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_PR_CAT2', index: 'FACD_PR_CAT2', hidden: true, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_PR_CAT3', index: 'FACD_PR_CAT3', hidden: true, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_PR_CAT4', index: 'FACD_PR_CAT4', hidden: true, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_PR_CAT5', index: 'FACD_PR_CAT5', hidden: true, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_PR_CAT6', index: 'FACD_PR_CAT6', hidden: true, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_PR_CAT7', index: 'FACD_PR_CAT7', hidden: true, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_PR_CAT8', index: 'FACD_PR_CAT8', hidden: true, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_PR_CAT9', index: 'FACD_PR_CAT9', hidden: true, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_PR_CAT10', index: 'FACD_PR_CAT10', hidden: true, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_DESC_ORI', index: 'FACD_DESC_ORI', hidden: true, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_REGALO', index: 'FACD_REGALO', hidden: true, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_ID_PROMO', index: 'FACD_ID_PROMO', hidden: true, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_DESC_PREC', index: 'FACD_DESC_PREC', hidden: true, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_DESC_PTO', index: 'FACD_DESC_PTO', hidden: true, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_DESC_VN', index: 'FACD_DESC_VN', hidden: true, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_DESC_LEAL', index: 'FACD_DESC_LEAL', hidden: true, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_ES_BACKORDER', index: 'FACD_ES_BACKORDER', hidden: true, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_TIPO_BACKORDER', index: 'FACD_TIPO_BACKORDER', hidden: true, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_TIPO_BACKORDER_LETRA', index: 'FACD_TIPO_BACKORDER_LETRA', hidden: boolShowBAck, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_DETALLE_BACKORDER', index: 'FACD_DETALLE_BACKORDER', hidden: true, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_REQUISICION', index: 'FACD_REQUISICION', hidden: true, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_ES_PAQUETE', index: 'FACD_ES_PAQUETE', hidden: true, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_ES_COMPONENTE', index: '[P]', checkbox: true, hidden: false, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_PR_PAQUETE', index: 'FACD_PR_PAQUETE', hidden: true, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_MULTIPLO', index: 'FACD_MULTIPLO', hidden: true, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_MPV', index: 'MPV', hidden: false, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_CANTIDAD_MULTIPLOS', index: 'FACD_CANTIDAD_MULTIPLOS', hidden: true, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_PRECIO_MULTIPLOS', index: 'FACD_PRECIO_MULTIPLOS', hidden: true, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_PRECIO_TOT_MULTIPLOS', index: 'FACD_PRECIO_TOT_MULTIPLOS', hidden: true, editable: false, width: 40, sortable: false, search: false},
                {name: 'FACD_ES_MULTIPLO', index: 'FACD_ES_MULTIPLO', hidden: true, editable: false, width: 40, sortable: false, search: false}],
            rowNum: 20,
            autowidth: true,
            rowList: [20, 20, 40],
            sortname: 'FACD_CVE',
            viewrecords: true,
            sortorder: "desc",
            caption: "PRODUCTOS",
            /*cellEdit: true, 
             cellsubmit: '_blank.jsp',*/
            editurl: '_blank.jsp',
            onSelectRow: function (id) {
                var grid = jQuery('#FAC_GRID');
                if (id != lastSelXghY) {
                    if (lastSelXghY != 0) {
                        var lstRow = grid.getRowData(id);
                        if (lstRow.FACD_TIPO_BACKORDER_LETRA != "RQS" &&
                                lstRow.FACD_TIPO_BACKORDER_LETRA != "TI" &&
                                lstRow.FACD_TIPO_BACKORDER_LETRA != "RQS,TI" &&
                                lstRow.FACD_TIPO_BACKORDER_LETRA != "AK" && lstRow.FACD_ES_COMPONENTE != "1") {
                            grid.restoreRow(lastSelXghY);
                            grid.editRow(id, false);
                            lastSelXghY = id;
                        } else {
                            grid.restoreRow(lastSelXghY);
                            lastSelXghY = id;
                        }
                    } else {
                        var lstRow = grid.getRowData(id);
                        if (lstRow.FACD_TIPO_BACKORDER_LETRA != "RQS" &&
                                lstRow.FACD_TIPO_BACKORDER_LETRA != "TI" &&
                                lstRow.FACD_TIPO_BACKORDER_LETRA != "RQS,TI" &&
                                lstRow.FACD_TIPO_BACKORDER_LETRA != "AK" && lstRow.FACD_ES_COMPONENTE != "1") {
                            grid.editRow(id, false);
                            lastSelXghY = id;
                        } else {
                            lastSelXghY = id;
                        }
                    }
                }
            }
        }).navGrid('#pager1', {edit: true, add: false, del: false});
    }
</script>
<!DOCTYPE html>
<html>
    <body onload="AddGridPediMak()">    

        <div id="div_principal">

            <div id="div_titulo" class="panel panel-default">
                <div class="panel-heading"></div>
                <table cellpadding="4" cellspacing="1" border="0" >

                    <td><font size=5>Pedido No.</font></td>
                    <td><input type="text" id="FAC_FOLIO" value="" disabled="disabled" style= "background-color:#C2C2C2;color:black;"/></td>
                    <td><font size=5>Sucursal</font></td>

                    <td><input type="text" id="cte_sucursal" value="<%=strNomSucursal%>" disabled="disabled" size="50"  style= "background-color:#C2C2C2;color:black;" /></td>                                        

                    <td style="visibility:hidden"><i class="fa fa-envelope" style="width: 600px; " align='right' onclick="MensajesPediMak();"></i></td>                    
                    <td id="tdIconMensaje" style="right:inherit"><a href="javascript:MensajesPediMak()" class="sf-with-ul" title="Mensajes"><i class="fa fa-envelope" style="font-size:40px" ></i></td></span>&nbsp;&nbsp;
                    <td id="tdIconEstCuenta" style="right:inherit"><a href="javascript:ReporteEstClienteCteMak()" class="sf-with-ul" title="Estado de Cuenta Cliente"><i class="fa fa-file-text-o" style="font-size:40px" ></i></td></span>&nbsp;&nbsp;
                    <td id="tdIconDatosCliente" style="right:inherit"><a href="javascript:DatosClientePediMak()" class="sf-with-ul" title="Datos Cliente"><i class="fa fa-user" style="font-size:40px" ></i></td></span>&nbsp;&nbsp;
                </table>
            </div>            

            <div id="div_infCte" class="panel panel-default">
                <div class="panel-heading"><font size=5>Info Cliente</font></div>
                <table cellpadding="4" cellspacing="1" border="0" >
                    <tr>
                        <td>Nombre:</td>
                        <td><input type="text" id="CT_NOM" value="" disabled="disabled" size="100" style= "background-color:#C2C2C2;color:black;"/></td>
                        <td>ID Cliente</td>
                        <td><input type="text" id="FCT_ID" value="" disabled="disabled" style= "background-color:#C2C2C2;color:black;"/></td>
                        <td>RFC:</td>
                        <td><input type="text" id="cte_rfc" value="" disabled="disabled" style= "background-color:#C2C2C2;color:black;"/></td> 
                        <td id="tdIconBusqClie"><a href="javascript:ObtieneClientePediMak();"><i class="fa fa-search"></i></a></td>

                    </tr>                    
                    <tr>
                        <td>Direcciòn:</td>
                        <td><input type="text" id="cte_direccion" value="" disabled="disabled"  size="100" style= "background-color:#C2C2C2;color:black;"/></td>                                            </tr>
                    <tr>
                        <td><font size=5>Info Subcliente</font></td>
                    </tr>
                    <tr>
                        <td>Nombre Subcliente:</td>
                        <td><input type="text" id="cte_nomSubcte" value="" disabled="disabled" size="100" style= "background-color:#C2C2C2;color:black;" /></td>
                        <td>RFC:</td>
                        <td><input type="text" id="cte_rfcSubcte" value="" disabled="disabled" style= "background-color:#C2C2C2;color:black;" /></td>

                        <td>ID SubCliente</td>
                        <td><input type="text" id="CT_CLIENTEFINAL" value="0" disabled="disabled" style= "background-color:#C2C2C2;color:black;" /></td>                                                                            
                        <td id="tdIconBusqSubcliente"><a href="javascript:ObtieneSubClientePediMak();"><i class="fa fa-search"></i></a></td>
                        <td id="tdIconBorrSubcliente"><a href="javascript:LimpiaSubClientePediMak();"><i class="fa fa-eraser"></i></a></td>
                    </tr>
                    <tr>
                        <td>Direcciòn:</td>
                        <td><input type="text" id="cte_direccionSubCliente" value="" disabled="disabled" size="100" style= "background-color:#C2C2C2;color:black;" /></td>

                        <td>Tel:</td>
                        <td><input type="text" id="cte_telSubcte" value="" disabled="disabled" style= "background-color:#C2C2C2;color:black;" /></td>                                                                            
                    </tr>

                    <!-- Se ocultaron los campos de direcciones de entrega-->
                    <tr>
                        <td><input type="hidden" id="cte_nomDiEn" value="" /></td>                         
                        <td><input type="hidden" id="cte_telDiEn" value="" /></td>                          
                        <td><input type="hidden" id="cte_emailDiEn" value="" /></td>                                                    
                        <td><input type="hidden" id="cte_direccDiEn" value="" /></td>
                    </tr>

                </table>
            </div>
            <center>
                <div id="div_btnMosCte"><td><input id="btn_mosCte" type="button" value="Info Cliente" onclick="MostrarBotonesPediMak(1)"  > </td></div>
                <div id="div_btnOcuCte"><td><input id="btn_ocuCte" type="button" value="Info Cliente" onclick="OcultarBotonesPediMak(1)"  > </td></div>
            </center>

            <div id="div_pedidos" class="panel panel-default">
                <%if (identiPantallaMak == 1) {%>
                <div class="panel-heading"><font size=5>Datos Pedido</font></div>
                    <%} else {%>
                <div class="panel-heading"><font size=5>Datos Cotización</font></div>
                    <%}%>
                <table cellpadding="4" cellspacing="1" border="0" >
                    <tr>
                        <%if (identiPantallaMak == 1) {%>
                        <td>Fecha Pedido:<span class="required">*</span></td>
                        <%} else {%>
                        <td>Fecha Cotización<span class="required">*</span></td>
                        <%}%>
                        <td><input type="text" id="FAC_FECHA" value="" disabled="disabled" style= "background-color:#C2C2C2;color:black;" /></td>
                        <td>Fecha Surtido:<span class="required">*</span></td>
                        <td><input type="text" id="pd_fech2"   onblur="validaFechSurtidoPediMak(1)"/></td>                            
                            <%if (identiPantallaMak == 1) {%>
                        <td>Pedido Manual:</td>
                        <%} else {%>
                        <td>Cotización Manual:</td>
                        <%}%>
                        <td><input type="text" id="pd_pdManual" value=""  /></td>
                    </tr>
                    <tr>
                        <td>Fecha Entrega:</td>
                        <td><input type="text" id="pd_fechValidez" onblur="validaFechSurtidoPediMak(2)"/></td>
                            <%if (identiPantallaMak == 1) {%>
                        <td>Contrato:</td>
                        <td><input type="text" id="pd_contrato" value=""  /></td>  
                            <%} else {%>
                        <td>Fecha Validez</td>
                        <td><input type="text" id="pd_fechValidVer" onblur="validaFechSurtidoPediMak(3)"/></td>
                            <%}%>
                        <td>Lista de precios:</td>
                        <td>&nbsp;
                            <select id="FCT_LPRECIOS"  name="moneda" class="combo1"  disabled="disabled" style= "background-color:#C2C2C2;color:black;">
                                <%=strListPrecio%>
                            </select>                             
                        </td>                         
                    </tr>
                    <tr>
                        <td>Moneda:<span class="required">*</span></td>
                        <td>&nbsp;
                            <select id="FAC_MONEDA" onchange="RefreshMonedaPediMak(this, 0)" name="moneda" class="combo1" disabled="disabled" style= "background-color:#C2C2C2;color:black;" >                                
                                <%=strMoneda%>
                            </select>                             
                        </td>
                        <td>Nombre Solicitante:</td>
                        <td><input type="text" id="pd_nomSolicit" value=""  /></td>
                        <td>Tipo de Cambio:</td>
                        <td>&nbsp;
                            <select id="pd_tipoCam"  name="tipo de cambio" class="combo1"  disabled="disabled" style= "background-color:#C2C2C2;color:black;">                                
                                <%=strTipoCambio%>
                            </select>                             
                        </td>   
                        <td><input type="text" id="pd_tipoCamCantidad" value=""  disabled="disabled" style= "background-color:#C2C2C2;color:black;"/></td>
                    </tr>
                    <tr>
                        <td>Bodega:<span class="required">*</span></td>
                        <td>&nbsp;
                            <select id="pd_bodega" onchange="verifConvPedFacPediMak()" name="bodega" class="combo1"  placeholder="Bodega">                                
                                <%=strBodega%>
                            </select>                             
                        </td>
                        <td>Cotizaciòn Origen:</td>
                        <td><input type="text" id="pd_cotizaOrigen" value="0" disabled="disabled" style= "background-color:#C2C2C2;color:black;" /></td>
                    </tr>
                    <tr>
                        <td>Vendedor:</td>
                        <td>&nbsp;
                            <select id="pd_vendedor" name="pd_vendedor" class="combo1"  placeholder="vendedor">                                  
                                <%=strVendedor%>
                            </select>
                        </td>
                        <td>O.C. Cliente:</td>
                        <td><input type="text" id="pd_ocCliente" value=""  /></td>
                    </tr>
                    <tr>                                
                        <td>Transporte:<span class="required">*</span></td>
                        <td>&nbsp;
                            <select id="TR_ID" name="transporte" class="combo1"  placeholder="Transporte">                                
                                <%=strTransporte%>
                            </select>                                  
                        </td>
                        <%if (identiPantallaMak == 2) {%>
                        <td>Contrato:</td>
                        <td><input type="text" id="pd_contrato" value=""  /></td>  
                            <%}%>                       
                    </tr>
                    <tr>
                        <td>¿Usa Flete?:</td>
                        <td><input id="modlgn-flete1" type="radio" name="modlgn-activacion" value="1" onclick="ShowFletesMak()" >Si </td>
                        <td><input id="modlgn-flete2" type="radio" name="modlgn-activacion" value="0">No<span class="required">*</span></td>
                        <!--<td>Credito Disponible</td>-->
                        <td><input type="hidden" id="FCT_MONTOCRED" value="" disabled="disabled" style= "background-color:#C2C2C2;color:black;"  /></td>  
                    </tr>
                </table>
            </div>
            <center>
                <%if (identiPantallaMak == 1) {%>
                <div id="div_btnMosPed"><td><input id="btn_mosPed" type="button" value="Datos Pedido" onclick="MostrarBotonesPediMak(2)"  > </td></div>
                <div id="div_btnOcuPed"><td><input id="btn_ocuPed" type="button" value="Datos Pedido" onclick="OcultarBotonesPediMak(2)"  > </td></div>
                        <%} else {%>
                <div id="div_btnMosPed"><td><input id="btn_mosPed" type="button" value="Datos Cotizaciòn" onclick="MostrarBotonesPediMak(2)"  > </td></div>
                <div id="div_btnOcuPed"><td><input id="btn_ocuPed" type="button" value="Datos Cotizaciòn" onclick="OcultarBotonesPediMak(2)"  > </td></div>
                        <%}%>
            </center>
            <div id="div_gridpedido" class="panel panel-default">
                <%if (identiPantallaMak == 1) {%>
                <div class="panel-heading"><font size=5>Pedido</font></div>  
                    <%} else {%>
                <div class="panel-heading"><font size=5>Cotizaciòn</font></div>  
                    <%}%>
                <table cellpadding="4" cellspacing="1" border="0" >
                    <tr>
                        <td>Lector/Còdigo</td>
                        <!-- onkeydown="AddItemEvt(event,this)" -->                        
                        <td id="tsTextCaptProductos"><input type="text" id="FAC_PROD" value="" onkeydown="ObtieneProductoKeyPediMak(event)"/><a href="javascript:ObtieneProductoBtnPediMak();"><i class="fa fa-barcode fa-2x" ></i></a><font size=4>Pr</font></td> 
                        <td style="visibility:hidden"><i class="fa fa-envelope" style="width: 900px; " align='right' onclick="MensajesPediMak();"></i></td>
                        <td id="tsIconExist" style="right:inherit"><a href="javascript:ExistenciasPediMak()" class="sf-with-ul" title="Existencias"> <i class="fa fa-cubes" style="font-size:40px" ></i></td></span>&nbsp;&nbsp;
                        <td id="tsIconModifPrecio" style="right:inherit"><a href="javascript:ModificarPrecioPediMak()" class="sf-with-ul" title="Modifica Precio"><i class="fa fa-pencil" style="font-size:40px" ></i></td></span>&nbsp;&nbsp;
                        <td id="tsIconBorrarItem" style="right:inherit"><a href="javascript:BorrarRowPediMak()" class="sf-with-ul" title="Borrar Producto"><i class="fa fa-trash" style="font-size:40px" ></i></td></span>&nbsp;&nbsp;
                        <td id="tsIconInformacion" style="right:inherit"><a href="javascript:InformacionProductoPediMak()" class="sf-with-ul" title="Informacion de Partidas"><i class="fa fa-info-circle" style="font-size:40px" ></i></td></span>&nbsp;&nbsp;
                            <%if (identiPantallaMak == 1) {%>
                        <td id="tsIconHistorial" style="right:inherit"><a href="javascript:HistorialPedidoPediMak()" class="sf-with-ul" title="Historial de Pedidos"><i class="fa fa-history" style="font-size:40px" ></i></td></span>&nbsp;&nbsp;
                            <%} else{%>
                        <td id="tsIconHistorial" style="right:inherit"><a href="javascript:HistorialPedidoPediMak()" class="sf-with-ul" title="Historial de Cotizaciòn"><i class="fa fa-history" style="font-size:40px" ></i></td></span>&nbsp;&nbsp;    
                            <%if (!estatusCotizaEditMak.equals("CERRADO")) {%>
                        <td id="tsIconConvCoti" style="right:inherit;"><a href="javascript:ConvertirCotiaPediMak()" class="sf-with-ul" title="Convertir a Pedido"><i class="fa fa-paper-plane" style="font-size:40px" ></i></td></span>&nbsp;&nbsp;
                            <%} %>
                            <%}%>
                        <td id="tsIconConvMoneda" style="right:inherit"><a href="javascript:ConvertirMonedaMak()" class="sf-with-ul" title="Cambio de Moneda"><i class="fa fa-usd" style="font-size:40px" ></i></td></span>&nbsp;&nbsp;
                            <%if (!estatusCotizaEditMak.equals("CERRADO")) {%>
                        <td id="tsIconActivarPantalla" style="right:inherit;display:none;"><a href="javascript:DesbloqueaPantallaMak()" class="sf-with-ul" title="Activar Pantalla"><i class="fa fa-edge" style="font-size:40px" ></i></td></span>&nbsp;&nbsp;                                                      
                            <%}%>
                        <td id="tsIconPagos" style="right:inherit;display:none;"><a href="javascript:PagosPedidoPediMak()" class="sf-with-ul" title="Pagos"><i class="fa fa-money" style="font-size:40px" ></i></td></span>&nbsp;&nbsp;                            
                            <%//if (identiPantallaMak == 1) {%>
                        <td id="tsIconConvFacPed" style="right:inherit"><a href="javascript:ConvFacPedidoPediMak()" class="sf-with-ul" title="Convertir Pedido a Factura"><i class="fa fa-files-o" style="font-size:40px" ></i></td></span>&nbsp;&nbsp;
                            <%//}%>
                        <td id="tsIconDirEntrega" style="right:inherit"><a href="javascript:showDireccionEntregaPediMak()" class="sf-with-ul" title="Direccion de entrega"><i class="fa fa-truck" style="font-size:40px" ></i></td></span>&nbsp;&nbsp;
                        <td><input type="hidden" id="FAC_CANT" value="0"  /></td>                        
                        <td><input type="hidden" id="FAC_DESC" value=""  disabled="disabled" style= "background-color:#C2C2C2;color:black;"/></td>                        
                        <td><input type="hidden" id="FAC_PRECIO" value=""  disabled="disabled" style= "background-color:#C2C2C2;color:black;"/></td>

                    </tr>
                    <tr>
                    <table id="FAC_GRID" class="scroll" >      
                        <div id="pager1"></div>   
                    </table>
                    </tr>
                    <tr>
                        <td>Descuento Adicional:</td>
                        <td>&nbsp;
                            <select id="FCT_DESCUENTO" name="descuento adicional" class="combo1"  >                                
                            </select>                             
                        </td>                                            
                    </tr>
                </table>                
            </div>
            <center>
                <%if (identiPantallaMak == 1) {%>
                <div id="div_btnMosGPedidos"><td><input id="btn_mosGPedidos" type="button" value="Pedido" onclick="MostrarBotonesPediMak(4)"  > </td></div>
                <div id="div_btnOcuGPedidos"><td><input id="btn_ocuGPedidos" type="button" value="Pedido" onclick="OcultarBotonesPediMak(4)"  > </td></div>
                        <%} else {%>
                <div id="div_btnMosGPedidos"><td><input id="btn_mosGPedidos" type="button" value="Cotizaciòn" onclick="MostrarBotonesPediMak(4)"  > </td></div>
                <div id="div_btnOcuGPedidos"><td><input id="btn_ocuGPedidos" type="button" value="Cotizaciòn" onclick="OcultarBotonesPediMak(4)"  > </td></div>

                <%}%>
            </center>


            <div id="div_totalpedidos" class="panel panel-default">
                <%if (identiPantallaMak == 1) {%>
                <div class="panel-heading"><font size=5>Total Pedidos</font></div>
                    <%} else {%>
                <div class="panel-heading"><font size=5>Total Cotizaciòn</font></div>
                    <%}%>
                <table cellpadding="4" cellspacing="1" border="0" >
                    <tr>
                        <td>Importe:</td>
                        <td><input input align='right' type="text" id="FAC_IMPORTE" value=""  disabled="disabled" style= "background-color:#C2C2C2;color:black;font-size:14px;text-align:right;"/></td>   
                        <td>Descuento</td>
                        <td><input input align='right' type="text" id="FAC_DESCUENTO" value="" disabled="disabled" style= "background-color:#C2C2C2;color:black;font-size:14px;text-align:right;" /></td>                          
                    </tr>
                    <tr>
                        <td>IVA:</td>
                        <td ><input input align='right' type="text" id="FAC_IMPUESTO1" value="" disabled="disabled" style= "background-color:#C2C2C2;color:black;font-size:14px;text-align:right;" /></td>
                        <td>Total:</td>
                        <td><input input align='right' type="text" id="FAC_TOT" value="" disabled="disabled" style= "background-color:#C2C2C2;color:black;font-size:14px;text-align:right;" /></td>                             
                    </tr>
                </table>
            </div>
            <center>
                <%if (identiPantallaMak == 1) {%>
                <div id="div_btnMosTPedidos"><td><input id="btn_mosTPedidos" type="button" value="Total Pedido" onclick="MostrarBotonesPediMak(5)"  > </td></div>
                <div id="div_btnOcuTPedidos"><td><input id="btn_ocuTPedidos" type="button" value="Total Pedido" onclick="OcultarBotonesPediMak(5)"  > </td></div>
                        <%} else {%>
                <div id="div_btnMosTPedidos"><td><input id="btn_mosTPedidos" type="button" value="Total Cotizaciòn" onclick="MostrarBotonesPediMak(5)"  > </td></div>
                <div id="div_btnOcuTPedidos"><td><input id="btn_ocuTPedidos" type="button" value="Total Cotizaciòn" onclick="OcultarBotonesPediMak(5)"  > </td></div>
                        <%}%>
            </center>
            <div id="TOOLBAR"></div>
            <div id="div_camposhidden" class="panel panel-default">
                <div class="panel-heading"></div>
                <table cellpadding="4" cellspacing="1" border="0" >
                    <!--Aqui estan los campos hidden -->
                    <td><input type="hidden" id="SC_ID" value="<%=intSucursal%>" /></td>
                    <td><input type="hidden" id="FAC_TIPO" value="<%=0%>" /></td>
                    <td><input type="hidden" id="BOD_ID" value="<%=varSesiones.getIntSucursalDefault()%>" /></td>
                    <td><input type="hidden" id="EMP_ID" value="<%=varSesiones.getIntIdEmpresa()%>" /></td>
                    <td><input type="hidden" id="FAC_DEVO" value="0" /></td>
                    <td><input type="hidden" id="FAC_TTC_ID" value="0" /></td>
                    <td><input type="hidden" id="FAC_DIASCREDITO" value="0" /></td>
                    <!-- <td><input type="hidden" id="FCT_MONTOCRED" value="0" /></td> -->
                    <td><input type="hidden" id="FAC_METODOPAGO" value="0" /></td>
                    <td><input type="hidden" id="FAC_FORMADEPAGO" value="0" /></td>
                    <td><input type="hidden" id="FAC_NUMCUENTA" value="0" /></td>
                    <td><input type="hidden" id="VE_ID" value="0" /></td>
                    <td><input type="hidden" id="FAC_TASASEL1" value="0" /></td>
                    <td><input type="hidden" id="FAC_USE_IMP1" value="0" /></td>
                    <td><input type="hidden" id="FAC_TASASEL2" value="0" /></td>
                    <td><input type="hidden" id="FAC_USE_IMP2" value="0" /></td>
                    <td><input type="hidden" id="FAC_TASASEL3" value="0" /></td>
                    <td><input type="hidden" id="FAC_USE_IMP3" value="0" /></td>
                    <td><input type="hidden" id="FAC_USO_IEPS1" value="0" /></td>
                    <td><input type="hidden" id="FAC_TASA_IEPS" value="0" /></td>
                    <td><input type="hidden" id="FAC_IMPORTE_IEPS" value="0" /></td>
                    <td><input type="hidden" id="FAC_IMPUESTO2" value="0" /></td>
                    <td><input type="hidden" id="FAC_IMPUESTO3" value="0" /></td>
                    <td><input type="hidden" id="FAC_PUNTOS" value="0" /></td>
                    <td><input type="hidden" id="FAC_NEGOCIO" value="0" /></td>
                    <td><input type="hidden" id="FAC_IMPORTE_REAL" value="0" /></td>
                    <td><input type="hidden" id="FAC_PZAS" value="0" /></td>
                    <td><input type="hidden" id="FAC_PUNTOS_REAL" value="0" /></td>
                    <td><input type="hidden" id="FAC_CREDITOS_REAL" value="0" /></td>
                    <td><input type="hidden" id="FAC_NEGOCIO_REAL" value="0" /></td>
                    <td><input type="hidden" id="FAC_IMPUESTO1_REAL" value="0" /></td>
                    <td><input type="hidden" id="FAC_IMPUESTO2_REAL" value="0" /></td>
                    <td><input type="hidden" id="FAC_IMPUESTO3_REAL" value="0" /></td>
                    <td><input type="hidden" id="FAC_RETISR" value="0" /></td>
                    <td><input type="hidden" id="FAC_RETIVA" value="0" /></td>
                    <td><input type="hidden" id="FAC_NETO" value="0" /></td>
                    <td><input type="hidden" id="PD_ID" value="0" /></td>
                    <td><input type="hidden" id="COT_ID" value="0" /></td>
                    <td><input type="hidden" id="FAC_NOTAS" value="" /></td>
                    <td><input type="hidden" id="FAC_NOTASPIE" value="" /></td>
                    <td><input type="hidden" id="FAC_REFERENCIA"value="0" /></td>
                    <td><input type="hidden" id="FAC_CONDPAGO" value="0"/></td>
                    <td><input type="hidden" id="FAC_NUMPEDI" value="0"/></td>
                    <td><input type="hidden" id="FAC_FECHAPEDI" value="0" /></td>
                    <td><input type="hidden" id="FAC_ADUANA" value="0"/></td>
                    <td><input type="hidden" id="FAC_TIPOCOMP" value="0"/></td>                                        
                    <td><input type="hidden" id="TF_ID" value="0"/></td>
                    <td><input type="hidden" id="CT_DIRENTREGA" value="0"/></td>
                    <td><input type="hidden" id="SYC_ID" value="0"/></td>
                    <td><input type="hidden" id="FAC_NUM_GUIA" value="0"/></td>
                    <td><input type="hidden" id="FAC_CONSIGNACION1" value="0"/></td>
                    <td><input type="hidden" id="FAC_ESRECU1" value="0"/></td>
                    <td><input type="hidden" id="FAC_PERIODICIDAD" value="0"/></td>
                    <td><input type="hidden" id="FAC_DIAPER" value="0"/></td>
                    <td><input type="hidden" id="FAC_NO_EVENTOS" value="0"/></td>
                    <td><input type="hidden" id="ADD_MABE" value="0"/></td>
                    <td><input type="hidden" id="ADD_SANOFI" value="0"/></td>
                    <td><input type="hidden" id="ADD_FEMSA" value="0"/></td>
                    <td><input type="hidden" id="VE_NOM" value="0"/></td>
                    <td><input type="hidden" id="FAC_LPRECIOS" value="0"/></td>
                    <td><input type="hidden" id="FAC_ESRECU2" value="0"/></td>
                    <td><input type="hidden" id="FAC_TASAPESO" value="1" /></td>                                                                            
                    <td><input type="hidden" id="PED_BAN_CODIGOINCOMPLETO" value="0" /></td>
                    <td><input type="hidden" id="PRECIOANTERIOR" value="0" /></td>
                    <td><input type="hidden" id="CANTIDADANTERIOR" value="0" /></td>
                    <td><input type="hidden" id="FACD_SUBTOTAL_A" value="0" /></td>
                    <td><input type="hidden" id="STATUS" value="0" /></td>                                        
                    <td><input type="hidden" id="BandInformacion" value="0" /></td>
                    <td><input type="hidden" id="COT_IDMOTIVO" value="0" /></td>
                    <td><input type="hidden" id="COT_IDDESC" value="0" /></td>
                    <td><input type="hidden" id="COT_BAN_CONV_COTI" value="0" /></td>
                    <td><input type="hidden" id="COT_ID_PEDIDO" value="0" /></td>
                    <td><input type="hidden" id="FAC_TIENE_FLETE" value="0" /></td>
                    <td><input type="hidden" id="FAC_TIENE_SEGURO" value="0" /></td>
                    <td><input type="hidden" id="FAC_MONTO_SEGURO" value="0" /></td>
                    <td><input type="hidden" id="ME_ID" value="0"/></td>
                    <td><input type="hidden" id="FAC_TIPO_FLETE" value="0" /></td>
                    <td><input type="hidden" id="PANT_PAGOS" value="0" /></td>
                    <!--Bandera para identificar si no se valida el credito-->
                    <td><input type="hidden" id="CT_VENTAMOSTRADOR" value="0" /></td>
                    <!--Valor Maximo permitido para descuento del usuario-->
                    <td><input type="hidden" id="US_MAX_DESC" value="<%=dblPorcentajeMax%>" /></td>
                    <!--Bandera para identificar si el cliente es COD-->
                    <td><input type="hidden" id="CT_COD" value="0" /></td>      
                    <!--Bandera para identificar si el pedido se va a convertir a factura-->
                    <td><input type="hidden" id="PD_CONV_FACTURA" value="0" /></td>  
                    <!--Bandera para identificar si la bodega permite convertir a factura-->
                    <td><input type="hidden" id="SC_FAC_PEDIDO" value="0" /></td>  
                    <!--Id de los porcentajes adicionales-->
                    <td><input type="hidden" id="FAC_POR_DESCUENTO" value="0" /></td>  
                    <!--Bandera para identificar Numero de Documentos-->
                    <td><input type="hidden" id="BAN_NUMDOCUMENTOS" value="" /></td>                                                             
                    <!--Se utiliza para Guardar la cantidad del Tipo de Cambio-->
                    <td><input type="hidden" id="FAC_TIPOCAMACT" value="" /></td>                                         
                </table>
            </div>
        </div>    
    </body>
</html>


<%   /* }
    oConn.close();
     */

%>



