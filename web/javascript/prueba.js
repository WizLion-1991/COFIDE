/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//Variables globales
var itemIdPedMak = 0; //indice para los items del grid
var lstPorcDesc = null; //Arreglo con los porcentajes de descuento
var bolPrice = false;
var bolPorc = false;
var bolCambioFecha = false;
var bolDevol = false;
var bolModiImpuesto = false;
var bolPerPedido = false;
var bolPerTicket = false;
var bolPerFactura = false;
var bolPerCotiza = false;
var bolNolMLM = false;
/*Promociones*/
var bolPromociones = false;
var intSucOfertas = intSucDefOfertas;
var bolCargaPromociones = false;
var bolDebugPtoVta = false;
var bolSaveVta = true;
//Inicializamos impuestos con sucursal default
var dblTasaVta1 = dblTasa1;
var dblTasaVta2 = dblTasa2;
var dblTasaVta3 = dblTasa3;
var intIdTasaVta1 = intIdTasa1;
var intIdTasaVta2 = intIdTasa2;
var intIdTasaVta3 = intIdTasa3;
var intSImpVta1_2 = intSImp1_2;
var intSImpVta1_3 = intSImp1_3;
var intSImpVta2_3 = intSImp2_3;
var intCT_TIPOPERS = 0;
var intCT_TIPOFAC = 0;
var strCT_USOIMBUEBLE = "";
var intRET_ISR = 0;
var intRET_IVA = 0;
var intRET_FLETE = 1;
var _objProdTmpz = null;
var objTabla = null;
var numFilas = 0;
var idT = 0;
var intContaIdGrid = 0;
var bolUsaOpnCte = false;
var ContGridHistPediMak = 0;
var lastSelXghY = 0;
var lastSelBakOrder = 0;
var canAntGrdMak = 0;
var identiPantallaMak = 0;
var locestatusCotizaEditMak = "";
var locidCotizaEditMak = "0";

var bolPermConfirmMak = false;
var bolPermConfirmCorteMak = false;
var bolPermCambiarPrecMak = false;
var bolPermDescAdicMak = false;
//Permiso para activar el pedido para facturar
var bolPermActPediFact = false;
//Permisos para modificar el tipo de cambio
var bolPermModTipocambioCotizacion = false;
var bolPermModTipocambioPedido = false;
//contador para indicar si fue la primera vez que se entro a las direcciones de entrega
var intIngDirEnt = 0;

function vta_pedidosmak() {
}

/**Funcion inicial de la pantalla Principal*/
function initPediMak() {
    myLayout.close("west");
    myLayout.close("east");
    myLayout.close("south");
    myLayout.close("north");

    /*Identificamos si estamos realizando un pedido o Cotizacion*/
    var strNomMainpr = objMap.getNomMain();
    if (strNomMainpr == "VER_COTIZAMAK" || strNomMainpr == "COTIZACION_MAK") {
        identiPantallaMak = 2;
    } else {
        if (strNomMainpr == "PEDIDOS_MAK") {
            identiPantallaMak = 1;
        }
    }
    /*Se identifica con que estatus llega la cotizacion*/
    if (typeof estatusCotizaEditMak != "undefined") {
        locestatusCotizaEditMak = estatusCotizaEditMak;
    }
    /*Se identifica el id de la cotizacion a editar*/
    if (typeof idCotizaEditMak != "undefined") {
        locidCotizaEditMak = idCotizaEditMak;
    }
    loadClientesScrenPediMak();
}

/**Funcion inicial pop up Productos*/
function initProdMak() {
    /*Cargamos el grid de los sublclientes*/
    CargaGridSubCtePediMak();
}

/**Carga la pantalla inicial...*/
function loadClientesScrenPediMak() {

    var strPost = "&identiPantallaMak=" + identiPantallaMak;
    if (locestatusCotizaEditMak != "") {
        strPost += "&estatusCotizaEditMak=" + locestatusCotizaEditMak;
    }

    $.ajax({
        url: "ERP_PedidosMak.jsp",
        data: strPost,
        dataType: "html",
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        success: function (datos) {
            document.getElementById("MainPanel").innerHTML = datos;
            /*Ocultamos los botones de mostrar*/
            document.getElementById('div_btnMosCte').style.display = 'none';
            document.getElementById('div_btnMosPed').style.display = 'none';
            document.getElementById('div_btnMosGPedidos').style.display = 'none';
            document.getElementById('div_btnMosTPedidos').style.display = 'none';
            document.getElementById('div_titulo').style.display = 'block';

            if (identiPantallaMak == 1) {
                d.getElementById("FAC_TIPO").value = 3;
            } else {
                d.getElementById("FAC_TIPO").value = 5;
            }
            /*Agregamos Permisos*/
            AddPermisosMak();
            /*Obtenemos el consecutivo*/
            ObtieneNumPedidoPediMak();
            /*Obtenemos ola fecha cuando se creo o edito*/
            obtieneFechaPediMak();
            /*Agregamos el grid principal*/
            AddGridPediMak();

            /*Pintamos los botones finalesa*/
            var strHtml = "<table>";
            strHtml += "<tr>";
            if (identiPantallaMak == 2 && locestatusCotizaEditMak != "CERRADO") {
                strHtml += "<td id=\"tsIconGuardarSaveConfirm\" \"><a href=\"javascript:CerrarPediMak()\" class=\"sf-with-ul\" title=\"Cerrar\"><i class = \"fa fa-times-circle\" style=\"font-size:50px\" ></i></td></span>&nbsp;&nbsp;";
            }

            if (locestatusCotizaEditMak != "CERRADO") {
                strHtml += "<td style=\"visibility:hidden\"id=\"11\"><i class = \"fa fa-hdd-o\" style=\"font-size:50px\" ></i></td>";
                strHtml += "<td id=\"tsIconGuardar\" class=\"btn_libretaped\"><a href=\"javascript:Callbtn0PediMak()\" class=\"sf-with-ul\" title=\"Guardar\"><i class = \"fa fa-hdd-o\" style=\"font-size:50px\" ></i></td></span>&nbsp;&nbsp;";
                strHtml += "<td style=\"visibility:hidden\"id=\"11\"><i class = \"fa fa-hdd-o\" style=\"font-size:50px\" ></i></td>";
                strHtml += "<td id=\"tsIconSaveConfirm\" class=\"btn_floppy\"><a href=\"javascript:SavePediMak()\" class=\"sf-with-ul\" title=\"Guardar y Confirmar\"><i class = \"fa fa-floppy-o\" style=\"font-size:50px\" ></i></td></span>&nbsp;&nbsp;";
            }
            strHtml += "<td style=\"visibility:hidden\"id=\"22\"><i class = \"fa fa-hdd-o\" style=\"font-size:50px\" ></i></td>";
            strHtml += "<td  id=\"tsIconSalir\" class=\"btn_salirped\"><a href=\"javascript:SalirScreenPediMak()\" class=\"sf-with-ul\" title=\"Salir\"><i class = \"fa fa-sign-out\" style=\"font-size:50px\" ></i></td></span>&nbsp;&nbsp;";
            strHtml += "</tr>";
            strHtml += "</table>";
            document.getElementById("TOOLBAR").innerHTML = strHtml;


            /*idPedidoEditConsultMak es el id de el pedido que se esta editando*/
            if (typeof idPedidoEditConsultMak != "undefined") {
                if (idPedidoEditConsultMak != "" && idPedidoEditConsultMak != "0") {
                    getPedidoenVentaPediMak(idPedidoEditConsultMak);
                    $("#dialogCte").dialog("close");
                } else {
                    if (locidCotizaEditMak != "" && locidCotizaEditMak != "0") {
                        getCotizaenVentaPediMak(locidCotizaEditMak);
                    } else {
                        PopupCtesPediMak();
                    }
                }
            } else {
                if (locidCotizaEditMak != "" && locidCotizaEditMak != "0") {
                    getCotizaenVentaPediMak(locidCotizaEditMak);
                } else {
                    PopupCtesPediMak();
                }
            }
            //Llenamos el combo de Porcentaje adicional
            setTimeout("comboPorcPediMak(" + d.getElementById("US_MAX_DESC").value + ");", 1000);
            //Verifica los permisos Iniciales
            setTimeout("veriPermisosIniPediMak();", 1000);


        }
    });
}

function comboPorcPediMak(dblDescMaximo) {
    //dblDescMaximo
    if (bolPermDescAdicMak) {
        $.ajax({
            type: "POST",
            data: "&dblDescMaximo=" + 100,
            scriptCharset: "utf-8",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "xml",
            url: "ERP_PedidosMakProcs.jsp?id=44",
            success: function (datos) {
                var objsc = datos.getElementsByTagName("descuento")[0];
                var lstProds = objsc.getElementsByTagName("descuento_deta");
                select_clear(document.getElementById("FCT_DESCUENTO"));
                select_add(document.getElementById("FCT_DESCUENTO"), 'Seleccione', '0');
                for (var i = 0; i < lstProds.length; i++) {
                    var obj = lstProds[i];
                    select_add(document.getElementById("FCT_DESCUENTO"), obj.getAttribute('PCD_PORC'), obj.getAttribute('PCD_PORC'));
                }
                document.getElementById("FCT_DESCUENTO").value = document.getElementById("FAC_POR_DESCUENTO").value;
            },
            error: function (objeto, quepaso, otroobj) {
                alert(":inventarios:" + objeto + " " + quepaso + " " + otroobj);
                $("#dialogWait").dialog("close");
            }
        });
    } else {
        document.getElementById('FCT_DESCUENTO').style.display = 'none';
    }
}

function PopupCtesPediMak() {
    var objSecModiVta = objMap.getScreen("CLIENT_MAK");
    if (objSecModiVta != null) {
        objSecModiVta.bolActivo = false;
        objSecModiVta.bolMain = false;
        objSecModiVta.bolInit = false;
        objSecModiVta.idOperAct = 0;
    }
    OpnOpt('CLIENT_MAK', '_ed', 'dialogCte', false, false, true);
}

function ObtieneProductoKeyPediMak(event) {
    if (event.keyCode == 13) {
        if (VerificaClientePediMak() == "true") {
            var strCodSearch = d.getElementById("FAC_PROD").value;
            if (strCodSearch == "?" || strCodSearch.length >= 3) {
                ObtieneProductoPediMak(d.getElementById("FAC_PROD").value);
            } else {
                alert("El codigo debe de tener minimo 3 caracteres");
            }
        } else {
            alert("Seleccione un cliente!");
        }
    }
}

function VerificaClientePediMak() {

    var boolBandera = "";
    if (document.getElementById('FCT_ID').value != "" && document.getElementById('FCT_ID').value != "0") {
        boolBandera = "true";
    } else {
        boolBandera = "false";
    }
    return boolBandera;
}

function autoCompleteCtePediMak() {

    var IdSuc = document.getElementById('SC_ID').value;
    var IdTipo = document.getElementById('CC1_ID').value;
    var strFunction = new Function("event", "ui", "{" +
            "document.getElementById('SEARCHCTE').value = ui.item.id;" +
            "}");
    $("#SEARCHCTE").autocomplete({
        source: "autoctemak.jsp?ID=1&IdSuc=" + IdSuc + "&IdTipo=" + IdTipo,
        minLength: 1,
        select: strFunction
    });
}

function MostrarBotonesPediMak(opc) {
    if (opc == 1) {
        document.getElementById('div_infCte').style.display = 'block';
        document.getElementById('div_btnMosCte').style.display = 'none';
        document.getElementById('btn_mosCte').style.display = 'block';
        document.getElementById('btn_ocuCte').style.display = 'block';
    }
    if (opc == 2) {
        document.getElementById('div_pedidos').style.display = 'block';
        document.getElementById('div_btnMosPed').style.display = 'none';
        document.getElementById('btn_mosPed').style.display = 'block';
        document.getElementById('btn_ocuPed').style.display = 'block';
    }
    if (opc == 3) {
        document.getElementById('div_datosgenerales').style.display = 'block';
        document.getElementById('btn_mosDatGen').style.display = 'block';
        document.getElementById('btn_ocuDatGen').style.display = 'block';
    }
    if (opc == 4) {
        document.getElementById('div_gridpedido').style.display = 'block';
        document.getElementById('div_btnMosGPedidos').style.display = 'none';
        document.getElementById('btn_mosGPedidos').style.display = 'block';
        document.getElementById('btn_ocuGPedidos').style.display = 'block';
    }
    if (opc == 5) {
        document.getElementById('div_totalpedidos').style.display = 'block';
        document.getElementById('div_btnMosTPedidos').style.display = 'none';
        document.getElementById('btn_mosTPedidos').style.display = 'block';
        document.getElementById('btn_ocuTPedidos').style.display = 'block';
    }
}

function OcultarBotonesPediMak(opc) {
    if (opc == 1) {
        document.getElementById('div_infCte').style.display = 'none';
        document.getElementById('div_btnMosCte').style.display = 'block';
        document.getElementById('btn_ocuCte').style.display = 'none';
    }
    if (opc == 2) {
        document.getElementById('div_pedidos').style.display = 'none';
        document.getElementById('div_btnMosPed').style.display = 'block';
        document.getElementById('btn_ocuPed').style.display = 'none';
    }
    if (opc == 3) {
        document.getElementById('div_datosgenerales').style.display = 'none';
        document.getElementById('btn_ocuDatGen').style.display = 'none';
    }
    if (opc == 4) {
        document.getElementById('div_gridpedido').style.display = 'none';
        document.getElementById('div_btnMosGPedidos').style.display = 'block';
        document.getElementById('btn_ocuGPedidos').style.display = 'none';
    }
    if (opc == 5) {
        document.getElementById('div_totalpedidos').style.display = 'none';
        document.getElementById('div_btnMosTPedidos').style.display = 'block';
        document.getElementById('btn_ocuTPedidos').style.display = 'none';
    }
}

function ObtieneNumPedidoPediMak() {
    var intIdType = 3;
    if (identiPantallaMak == 1) {
        intIdType = 3;
    } else {
        intIdType = 5;
    }
    $.ajax({
        type: "POST",
        data: "TYPE_ID=" + intIdType,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "VtasMov.do?id=14",
        success: function (datos) {
            var objsc = datos.getElementsByTagName("vta_folios")[0];
            var strFolioT = objsc.getAttribute("FOLIO");
            document.getElementById("FAC_FOLIO").value = strFolioT;
            $("#dialogInv").dialog("close");
        },
        error: function (objeto, quepaso, otroobj) {
            alert(":Obten folio siguiente:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogInv").dialog("close");
        }
    });
}

function ObtieneClientePediMak() {
    /*Validamos que no halla partidas en el grid de pedidos */
    var grid = jQuery("#FAC_GRID");
    var arr = grid.getDataIDs();
    if (arr.length == 0) {
        PopupCtesPediMak();
    } else {
        alert("No se puede modificar el cliente con partidas agregadas.");
    }
}

function AddGridPediMak() {

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

function validaNumerosPediInt(e) {
    var tecla = (document.all) ? e.keyCode : e.which;
    //Tecla de retroceso para borrar, siempre la permite
    if (tecla == 8) {
        return true;
    }
    if (tecla == 13) {
        return true;
    }
    if (tecla == 9) {
        return true;
    }
    // Patron de entrada, en este caso solo acepta numeros
    var patron = /[0-9]/;
    var tecla_final = String.fromCharCode(tecla);
    return patron.test(tecla_final);
}

function SaveCantAnterior(grid, id) {
    //Actualiza la cantidad confirmadoa
    var lstRow = grid.getRowData(id);
    document.getElementById("CANTIDADANTERIOR").value = lstRow.FACD_CANTIDAD;
}

function CalculaImportesPediMak(rowid) {
    VtaModificaCantDoPediMak(rowid);

}

/**Modifica la cantidad pedida en la factura*/
function VtaModificaCant() {
    var grid = jQuery("#FAC_GRID");
    var id = grid.getGridParam("selrow");
    if (id != null) {
//Abrimos pop Up para mostrar la cantidad
        var lstRow = grid.getRowData(id);
        document.getElementById("Operac").value = "CHANGE_CANTPD";
        $("#SioNO").dialog('option', 'title', lstMsg[132]);
        var div = document.getElementById("SioNO_inside");
        var strHtml = CreaTexto(lstMsg[89], "_NvoCant", lstRow.FACD_CANTIDAD, 10, 10, true, false, "", "left", 0, "", "", "", false, 1);
        div.innerHTML = strHtml;
        $("#SioNO").dialog("open");
    }
}

function ObtieneSubClientePediMak() {
    if (document.getElementById("FCT_ID").value != 0 || document.getElementById("FCT_ID").value != "") {
        /*Cerramos La pantalla */
        var objSecModiVta = objMap.getScreen("SUBCLIENTES_MAK");
        if (objSecModiVta != null) {
            objSecModiVta.bolActivo = false;
            objSecModiVta.bolMain = false;
            objSecModiVta.bolInit = false;
            objSecModiVta.idOperAct = 0;
        }
        OpnOpt('SUBCLIENTES_MAK', '_ed', 'dlgMakSubCte', false, false, true);
    } else {
        alert("Seleccione a un cliente!!");
    }
}

/*Sirve para preparar los campos para agregar un nuevo cliente final*/
function newDirCteFinalPediMak() {
    var objSecModiVta = objMap.getScreen("NVO_SUBCLIENTE");
    if (objSecModiVta != null) {
        objSecModiVta.bolActivo = false;
        objSecModiVta.bolMain = false;
        objSecModiVta.bolInit = false;
        objSecModiVta.idOperAct = 0;
    }
    OpnOpt('NVO_SUBCLIENTE', '_ed', 'dialog2', false, false, true);
}

/**Borra el item seleccionado*/
function delDireccionCteFinalPediMak() {
    var grid = jQuery("#grd_DireccionesDFA");
    if (grid.getGridParam("selrow") != null) {
        var id = grid.getGridParam("selrow");
        var lstRow = grid.getRowData(id);
        var idDir = lstRow.DFA_ID;
        if (getUsoCteFinalPediMak(idDir) != true) {
            if (confirm("Atención: Confirma que desea eliminar la dirección")) {
                grid.delRowData(grid.getGridParam("selrow"));
                deleteDireccionCteFinalPediMak(idDir);
            }
        } else {
            alert("Atención: Esta dirección no puede ser borrada, ya se uso en algunos documentos");
        }
    } else {
        alert("Selecciona un registro el la tabla");
    }
}

/*Sirve para guardar la nueva direcion de entrega*/
function saveDirCteFinalPediMak() {
    if (document.getElementById("FCT_ID").value == "" || document.getElementById("FCT_ID").value == "0") {
        alert("ATENCIÓN: Primero debes seleccionar al cliente para asignarle mas direcciones de entrega.");
    } else {
        $.ajax({
            type: "POST",
            data: "&strRfc=" + document.getElementById("DFA_RFC").value,
            scriptCharset: "utf-8",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "html",
            url: "ERP_PedidosMakProcs.jsp?id=43",
            success: function (datos) {
                datos = trim(datos);
                if (datos != "OK") {
                    alert(datos);
                } else {
                    var strPost = "";
                    if (validaDatosCteFinalPediMak() != 0) {
                        strPost = "&DFA_CALLE=" + document.getElementById("DFA_CALLE").value;
                        strPost += "&DFA_COLONIA=" + document.getElementById("DFA_COLONIA").value;
                        strPost += "&DFA_LOCALIDAD=" + document.getElementById("DFA_LOCALIDAD").value;
                        strPost += "&DFA_MUNICIPIO=" + document.getElementById("DFA_MUNICIPIO").value;
                        strPost += "&DFA_ESTADO=" + document.getElementById("DFA_ESTADO").value;
                        strPost += "&DFA_CP=" + document.getElementById("DFA_CP").value;
                        strPost += "&DFA_NUMERO=" + document.getElementById("DFA_NUMERO").value;
                        strPost += "&DFA_NUMINT=" + document.getElementById("DFA_NUMINT").value;
                        strPost += "&DFA_TELEFONO=" + document.getElementById("DFA_TELEFONO").value;
                        strPost += "&DFA_RAZONSOCIAL=" + document.getElementById("DFA_RAZONSOCIAL").value;
                        strPost += "&DFA_RFC=" + document.getElementById("DFA_RFC").value;
                        strPost += "&DFA_EMAIL=" + document.getElementById("DFA_EMAIL").value;
                        strPost += "&CT_ID=" + document.getElementById("FCT_ID").value;
                        strPost += "&DFA_PAIS=" + document.getElementById("DFA_PAIS").value;
                        strPost += "&DFA_CIUDAD=" + document.getElementById("DFA_CIUDAD").value;
                        if (document.getElementById("DFA_VISIBLE1").checked)
                        {
                            strPost += "&DFA_VISIBLE=" + 1;
                        } else
                        {
                            strPost += "&DFA_VISIBLE=" + 0;
                        }

                        $.ajax({
                            type: "POST",
                            data: strPost,
                            scriptCharset: "utf-8",
                            contentType: "application/x-www-form-urlencoded;charset=utf-8",
                            cache: false,
                            dataType: "html",
                            url: "ERP_PedidosMakProcs.jsp?id=3",
                            success: function (datos) {

                                var strdireccion = document.getElementById("DFA_CALLE").value + " " +
                                        document.getElementById("DFA_COLONIA").value + " " +
                                        document.getElementById("DFA_MUNICIPIO").value + " " +
                                        document.getElementById("DFA_ESTADO").value;
                                document.getElementById("cte_nomSubcte").value = document.getElementById("DFA_RAZONSOCIAL").value;
                                document.getElementById("cte_rfcSubcte").value = document.getElementById("DFA_RFC").value;
                                document.getElementById("CT_CLIENTEFINAL").value = datos;
                                document.getElementById("cte_direccionSubCliente").value = strdireccion;
                                document.getElementById("cte_telSubcte").value = document.getElementById("DFA_TELEFONO").value;


                                $("#dialogWait").dialog("close");
                                $("#dialog2").dialog("close");
                                $("#dlgMakSubCte").dialog("close");
                            },
                            error: function (objeto, quepaso, otroobj) {
                                alert(":pto:" + objeto + " " + quepaso + " " + otroobj);
                            }
                        });
                    }
                }
            },
            error: function (objeto, quepaso, otroobj) {
                alert(":pto:" + objeto + " " + quepaso + " " + otroobj);
            }
        });
    }

}

/*Sirve para cancelar la operacion y oculatr los campos de Cliente Final*/
function cancelDirCteFinalPediMak() {
    $("#dialog2").dialog("close");
}

/*SIRVE PARA BORRAR UNA DIRECCION DE ENTREGA DE LA BD*/
function deleteDireccionCteFinalPediMak(idDir) {
    $.ajax({
        type: "POST",
        data: "dir_id=" + idDir,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "html",
        url: "ERP_PedidosMakProcs.jsp?id=4",
        success: function (datos) {
            if (datos.substring(0, 2) == "OK") {
                alert("Dirección de entrega Eliminada...");
            } else {
                alert(datos);
            }
            $("#dialogWait").dialog("close");
        },
        error: function (objeto, quepaso, otroobj) {
            alert(":pto:" + objeto + " " + quepaso + " " + otroobj);
        }
    });
}

/*Nos regresa true o false de si la direccion ya fue usada*/
function getUsoCteFinalPediMak(idDir) {
    $.ajax({
        type: "POST",
        data: "id_dir=" + idDir,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "html",
        url: "ERP_PedidosMakProcs.jsp?id=5",
        success: function (datos) {
            if (datos.substring(0, 2) == "OK") {
                return true;
            } else {
                return false;
                alert(datos);
            }
            $("#dialogWait").dialog("close");
        },
        error: function (objeto, quepaso, otroobj) {
            alert(":pto:" + objeto + " " + quepaso + " " + otroobj);
        }
    });
}

function validaDatosCteFinalPediMak() {
    var strCalle = document.getElementById("DFA_CALLE").value;
    var strColonia = document.getElementById("DFA_COLONIA").value;
    var strMunicipio = document.getElementById("DFA_MUNICIPIO").value;
    var strEstado = document.getElementById("DFA_ESTADO").value;
    var strCp = document.getElementById("DFA_CP").value;
    var strNumero = document.getElementById("DFA_NUMERO").value;
    var strRazonS = document.getElementById("DFA_RAZONSOCIAL").value;
    var strRFC = document.getElementById("DFA_RFC").value;
    var strPais = document.getElementById("DFA_PAIS").value;
    if (strCalle == "") {
        alert("Atención: Captura la calle");
        strCalle.focus();
        return 0;
    }
    if (strColonia == "") {
        alert("Atención: Captura la Colonia");
        strColonia.focus();
        return 0;
    }

    if (strMunicipio == "") {
        alert("Atención: Captura el municipio o Delegación");
        strMunicipio.focus();
        return 0;
    }
    if (strEstado == "") {
        alert("Atención: Captura el Estado");
        strEstado.focus();
        return 0;
    }
    if (strCp == "") {
        alert("Atención: Captura el Codigo Postal");
        strCp.focus();
        return 0;
    }

    if (strRazonS == "") {
        alert("Atención: Captura la Razon social");
        strRazonS.focus();
        return 0;
    }
    if (strRFC == "") {
        alert("Atención: Captura el RFC");
        strRFC.focus();
        return 0;
    }
    if (strNumero == "") {
        alert("Atención: Captura el Numero");
        strNumero.focus();
        return 0;
    }
    if (strPais == 0) {
        alert("Atención: Captura el Pais");
        strPais.focus();
        return 0;
    }

    return 1;
}

function CargaGridSubCtePediMak() {

    var idCte = 0;
    var strNomMain = objMap.getNomMain();
    if (strNomMain == "VER_COTIZAMAK" || strNomMain == "VER_PEDIDOMAK") {
        document.getElementById("BTN_DFA_NEW").style.display = 'none';
        document.getElementById("BTN_DFA_DEL").style.display = 'none';
        idCte = document.getElementById("PAN_CLIENTE").value;
    } else {
        idCte = document.getElementById("FCT_ID").value;
    }
    var itemIdCob = 0;
    $.ajax({
        type: "POST",
        data: "idCte=" + idCte,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "XML",
        url: "ERP_PedidosMakProcs.jsp?id=6",
        success: function (datos) {
            jQuery("#grd_DireccionesDFA").clearGridData();
            var objsc = datos.getElementsByTagName("Subcliente")[0];
            var lstProds = objsc.getElementsByTagName("Subcliente_deta");
            for (var i = 0; i < lstProds.length; i++) {
                var obj = lstProds[i];
                var datarow = {
                    DFA_ID: obj.getAttribute("DFA_ID"),
                    DFA_RAZONSOCIAL: obj.getAttribute("DFA_RAZONSOCIAL"),
                    DFA_RFC: obj.getAttribute("DFA_RFC"),
                    DFA_CALLE: obj.getAttribute("DFA_CALLE"),
                    DFA_COLONIA: obj.getAttribute("DFA_COLONIA"),
                    DFA_LOCALIDAD: obj.getAttribute("DFA_LOCALIDAD"),
                    DFA_MUNICIPIO: obj.getAttribute("DFA_MUNICIPIO"),
                    DFA_ESTADO: obj.getAttribute("DFA_ESTADO"),
                    DFA_CP: obj.getAttribute("DFA_CP"),
                    DFA_NUMERO: obj.getAttribute("DFA_NUMERO"),
                    DFA_NUMINT: obj.getAttribute("DFA_NUMINT"),
                    DFA_TELEFONO: obj.getAttribute("DFA_TELEFONO"),
                    DFA_RAZONSOCIAL1: obj.getAttribute("DFA_RAZONSOCIAL"),
                    DFA_EMAIL: obj.getAttribute("DFA_EMAIL"),
                    CT_ID: obj.getAttribute("CT_ID"),
                    DFA_CIUDAD: obj.getAttribute("DFA_CIUDAD")
                };
                itemIdCob++;
                jQuery("#grd_DireccionesDFA").addRowData(itemIdCob, datarow, "last");
            }
            $("#dialogWait").dialog("close");
        },
        error: function (objeto, quepaso, otroobj) {
            alert(":pto:" + objeto + " " + quepaso + " " + otroobj);
        }
    });

}

function salirPanSubCtePediMak() {
    $("#dlgMakSubCte").dialog("close");
}

function aceptarPanSubCtePediMak() {
    var grid = jQuery('#grd_DireccionesDFA');
    var id = grid.getGridParam("selrow");
    var lstRow = grid.getRowData(id);
    var strCalle = lstRow.DFA_CALLE;
    var strColonia = lstRow.DFA_COLONIA;
    var strLocalidad = lstRow.DFA_LOCALIDAD;
    var strMunicipio = lstRow.DFA_MUNICIPIO;
    var strEstado = lstRow.DFA_ESTADO;
    if (grid.getGridParam("selrow") != null) {
        var strNomMain = objMap.getNomMain();
        if (strNomMain == "VER_COTIZAMAK") {
            document.getElementById("PAN_SUBCLIENTE").value = lstRow.DFA_ID;
            $("#dlgMakSubCte").dialog("close");
        } else {
            document.getElementById("cte_nomSubcte").value = lstRow.DFA_RAZONSOCIAL;
            document.getElementById("cte_rfcSubcte").value = lstRow.DFA_RFC;
            document.getElementById("cte_direccionSubCliente").value = strCalle + " " + strColonia + " " + strLocalidad + " " + strMunicipio + " " + strEstado;
            document.getElementById("cte_telSubcte").value = lstRow.DFA_TELEFONO;
            document.getElementById("CT_CLIENTEFINAL").value = lstRow.DFA_ID;
            jQuery("#grd_DireccionesDFA").clearGridData();
            $("#dlgMakSubCte").dialog("close");
        }
    } else {
        alert("Selecciona un Subcliente de la tabla");
    }
}

function ObtieneProductoBtnPediMak() {
    if (VerificaClientePediMak() == "true") {
        ObtieneProductoPediMak(d.getElementById("FAC_PROD").value);
    } else {
        alert("Seleccione un cliente!");
    }
}

function ObtieneProductoPediMak(strcodProd) {
    var strCod = UCase(strcodProd);
    //Validamos que hallan capturado un codigo
    if (trim(strCod) != "") {
        var intDevo = d.getElementById("FAC_DEVO").value;
        //Bandera para indicar si no se agrupan los items
        var bolAgrupa = true;
        $("#dialogWait").dialog("open");
        var bolNvo = true;
        var idProd = 0;
        //Si esta activada la funcionalidad de agrupar validamos si existe
        if (bolAgrupa) {
//Revisamos si existe el item en el grid
            var grid = jQuery("#FAC_GRID");
            var arr = grid.getDataIDs();
            for (var i = 0; i < arr.length; i++) {
                var id = arr[i];
                var lstRowAct = grid.getRowData(id);
                if (lstRowAct.FACD_CVE == strCod ||
                        lstRowAct.FACD_CODBARRAS == strCod) {
                    if (intDevo == 1) {
                        if (lstRow.FACD_ESDEVO == 1) {
                            idProd = id;
                            bolNvo = false;
                            break;
                        }
                    } else {
                        //No aplica con componentes de paquetes([p])
                        if (lstRowAct.FACD_ES_COMPONENTE == 0) {
                            idProd = id;
                            bolNvo = false;
                            break;
                        }
                    }
                }
            }
        }
        //Validamos si es un producto nuevo
        if (bolNvo) {
            //Buscamos los importes ya que es un producto nuevo
            $.ajax({
                type: "POST",
                data: encodeURI("PR_CODIGO=" + strCod + "&SC_ID=" + d.getElementById("pd_bodega").value + "&EMP_ID=" + d.getElementById("EMP_ID").value),
                scriptCharset: "utf-8",
                contentType: "application/x-www-form-urlencoded;charset=utf-8",
                cache: false,
                dataType: "xml",
                url: "VtasMov.do?id=5",
                success: function (datoVal) {
                    var objProd = datoVal.getElementsByTagName("vta_productos")[0];
                    var Pr_Id = 0;
                    if (objProd != undefined) {
                        Pr_Id = objProd.getAttribute('PR_ID');
                        d.getElementById("FAC_DESC").value = objProd.getAttribute('PR_DESCRIPCION');
                        $("#dialogWait").dialog("close");
                        //Reemplazamos el codigo del producto por el de la bd
                        if (Pr_Id != 0) {
                            strCod = objProd.getAttribute('PR_CODIGO');
                        }
                    }
                    var Ct_Id = d.getElementById("FCT_ID").value; //d.getElementById("FCT_ID").value;
                    var dblCantidad = d.getElementById("FAC_CANT").value;
                    if (intDevo == 1) {
                        dblCantidad = d.getElementById("FAC_CANT").value//d.getElementById("DEVO_CANTIDAD").value;
                    }
                    //Validamos si nos regreso un ID de producto valido
                    if (Pr_Id != 0) {
                        //Validamos que no se encuentre en un conteo ciclico
                        if (objProd.getAttribute('PR_ES_CICLICO') == 0) {
                            //Validamos la existencia
                            var dblExistencia = objProd.getAttribute('PR_EXISTENCIA');
                            if (objProd.getAttribute('PR_REQEXIST') == 1 &&
                                    document.getElementById("FAC_TIPO").value != 3 &&
                                    document.getElementById("FAC_TIPO").value != 5) {
                                //Obtenemos la existencia del producto
                                $.ajax({
                                    type: "POST",
                                    data: "PR_ID=" + Pr_Id, scriptCharset: "utf-8",
                                    contentType: "application/x-www-form-urlencoded;charset=utf-8",
                                    cache: false, dataType: "html",
                                    url: "InvMov.do?id=1",
                                    success: function (datoExist) {
                                        //Validamos que no estemos pidiendo mas de la existencia
                                        if (parseFloat(dblCantidad) > dblExistencia) {
                                            //Back Order
                                            var objSecModiVta = objMap.getScreen("BACKORDER_MAK");
                                            if (objSecModiVta != null) {
                                                objSecModiVta.bolActivo = false;
                                                objSecModiVta.bolMain = false;
                                                objSecModiVta.bolInit = false;
                                                objSecModiVta.idOperAct = 0;
                                            }
                                            OpnOpt('BACKORDER_MAK', '_ed', 'dlgMakBackOrder', false, false, true);
                                        } else {                                         //Validamos si requiere numero de serie
                                            if (objProd.getAttribute('PR_USO_NOSERIE') == 1) {
//                                        _drawScNoSeriePediMak(objProd, Pr_Id, Ct_Id, dblCantidad, strCod, dblExistencia, intDevo);
                                            } else {
                                                AddItemPrecPediMak(objProd, Pr_Id, Ct_Id, dblCantidad, strCod, dblExistencia, intDevo);
                                            }
                                        }
                                    },
                                    error: function (objeto, quepaso, otroobj) {
                                        alert(":ptoExist:" + objeto + " " + quepaso + " " + otroobj);
                                        $("#dialogWait").dialog("close");
                                    }
                                });
                            } else {
                                AddItemPrecPediMak(objProd, Pr_Id, Ct_Id, dblCantidad, strCod, dblExistencia, intDevo);
                            }
                        } else {
                            alert("Producto en conteo ciclico.");
                        }
                    } else {
                        var objSecModiVta = objMap.getScreen("PRODUCTOS_MAK");
                        if (objSecModiVta != null) {
                            objSecModiVta.bolActivo = false;
                            objSecModiVta.bolMain = false;
                            objSecModiVta.bolInit = false;
                            objSecModiVta.idOperAct = 0;
                        }
                        OpnOpt('PRODUCTOS_MAK', '_ed', 'dlgMakProductos', false, false, true);
                    }
                },
                error: function (objeto, quepaso, otroobj) {
                    alert(":pto3:" + objeto + " " + quepaso + " " + otroobj);
                    $("#dialogWait").dialog("close");
                }
            });
        } else {
            //Ya existe el item
            alert("Ya existe el producto: " + strCod);
//            var Cantidad = d.getElementById("FAC_CANT").value; //Cantidad solicitada
//            //Recuperamos los valores del producto
//            var gridD = jQuery("#FAC_GRID");
//            var lstRow = gridD.getRowData(idProd);
//            //Validamos si maneja numero de serie para confirmar las cantidades
//            if (lstRow.FACD_USA_SERIE == 1) {
//                // _drawScNoSeriePediMak(null, lstRow.FACD_PR_ID, 0, parseFloat(Cantidad), "", 0, 0, idProd, lstRow.FACD_SERIES);
//            } else {
//                //Recalculamos la cantidad
//                lstRow.FACD_CANTIDAD = parseFloat(lstRow.FACD_CANTIDAD) + parseFloat(Cantidad);
//                //Validamos existencias en caso de que aplique
//                if (lstRow.FACD_REQEXIST == 1 &&
//                        document.getElementById("FAC_TIPO").value != 3) {
//                    if (parseFloat(lstRow.FACD_CANTIDAD) > parseFloat(lstRow.FACD_EXIST)) {
//                        alert(lstMsg[3] + " " + lstRow.FACD_CVE + " " + lstMsg[4]);
//                        if (parseFloat(lstRow.FACD_EXIST) > 0) {
//                            lstRow.FACD_CANTIDAD = lstRow.FACD_EXIST;
//                        } else {
//                            lstRow.FACD_CANTIDAD = '';
//                        }
//                    }
//                }
//                //En caso de paquete actualizamos las cantidades y validamos existencias([p])
//                if (lstRow.FACD_ES_PAQUETE == 1) {
//                    actualizaComponentesPaquetePedMak(gridD, idProd, Cantidad);
//                }
//                //Limpiamos los regalos
//                if (intSucOfertas && bolCargaPromociones)
//                    _LimpiaRegalosPromosPediMak(jQuery("#FAC_GRID"));
//                //Recalculamos el importe y actualizamos la fila
//                lstRowChangePrecioPediMak(lstRow, idProd, gridD);
//                //Ponemos foco en el control
//                document.getElementById("FAC_PROD").value = "";
//                document.getElementById("FAC_PROD").focus();
//                d.getElementById("FAC_CANT").value = 0;
////                d.getElementById("FAC_DEVO").value = 0;
//                //Sumamos todos los items
//                //setSumPediMak();
//                //_PromocionProdPediMak(idProd);
            $("#dialogWait").dialog("close");
//                //Producto sin numero de serie
//            }
        }
    }
}

function obtieneFechaPediMak() {

    $("#pd_fech2").datepicker({
        onSelect: function (textoFecha, objDatepicker) {
            validaFechSurtidoPediMak(1);
        }
    });
    $("#pd_fechValidez").datepicker({
        onSelect: function (textoFecha, objDatepicker) {
            validaFechSurtidoPediMak(2);
        }
    });
    if (identiPantallaMak == 2) {
        $("#pd_fechValidVer").datepicker({
            onSelect: function (textoFecha, objDatepicker) {
                validaFechSurtidoPediMak(3);
            }
        });
    }

    $.ajax({
        type: "POST",
        data: "",
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "ERP_PedidosMakProcs.jsp?id=8",
        success: function (datos) {
            var lstXml = datos.getElementsByTagName("fecha")[0];
            var lstprecio = lstXml.getElementsByTagName("fecha_deta");
            for (var i = 0; i < lstprecio.length; i++) {
                var obj = lstprecio[i];
                document.getElementById("FAC_FECHA").value = obj.getAttribute("fecha_pedido");
                document.getElementById("pd_fechValidez").value = obj.getAttribute("fecha_entrega");
                document.getElementById("pd_fech2").value = obj.getAttribute("fecha_surtido");
                if (identiPantallaMak == 2) {
                    document.getElementById("pd_fechValidVer").value = obj.getAttribute("fecha_validez");
                }
            }
        },
        error: function (objeto, quepaso, otroobj) {
            alert(":pto9:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }
    });
}

function validaFechSurtidoPediMak(bandera) {

    var strPost = "";
    var strFechaPedido = document.getElementById("FAC_FECHA").value;
    var strFechaSurtido = document.getElementById("pd_fech2").value;
    var strFechaEntrega = document.getElementById("pd_fechValidez").value;
    if (identiPantallaMak == 2) {
        var strFechaValidez = document.getElementById("pd_fechValidVer").value;
    }
    var boolPasa = true;
    if (strFechaPedido.trim() == "") {
        boolPasa = false;
        alert("Capture la Fecha Pedido!");
    }
    if (strFechaSurtido.trim() == "") {
        boolPasa = false;
        alert("Capture la Fecha Surtido!");
    }
    if (strFechaEntrega.trim() == "") {
        boolPasa = false;
        alert("Capture la Fecha Entrega!");
    }
    if (identiPantallaMak == 2) {
        if (strFechaValidez.trim() == "") {
            boolPasa = false;
            alert("Capture la Fecha Validez!");
        }
    }
    if (boolPasa) {
        strPost += "&FechaSurtido=" + strFechaSurtido;
        strPost += "&FechaPedido=" + strFechaPedido;
        strPost += "&FechaEntrega=" + strFechaEntrega;
        if (identiPantallaMak == 2) {
            strPost += "&FechaValidez=" + strFechaValidez;
        }
        strPost += "&boolBandera=" + bandera;
        $.ajax({
            type: "POST",
            data: strPost,
            scriptCharset: "utf-8",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "xml",
            url: "ERP_PedidosMakProcs.jsp?id=9",
            success: function (datos) {
                var lstXml = datos.getElementsByTagName("fecha")[0];
                var lstprecio = lstXml.getElementsByTagName("fecha_deta");
                for (var i = 0; i < lstprecio.length; i++) {
                    var obj = lstprecio[i];
                    if (obj.getAttribute("respuesta") != "OK") {
                        alert(obj.getAttribute("respuesta"));
                        if (bandera == "1") {
                            document.getElementById("pd_fech2").value = obj.getAttribute("fecha");
                        }
                        if (bandera == "2") {
                            document.getElementById("pd_fechValidez").value = obj.getAttribute("fecha");
                        }
                        if (identiPantallaMak == 2) {
                            if (bandera == "3") {
                                document.getElementById("pd_fechValidVer").value = obj.getAttribute("fecha");
                            }
                        }
                    }
                }
            },
            error: function (objeto, quepaso, otroobj) {
                alert(":pto9:" + objeto + " " + quepaso + " " + otroobj);
                $("#dialogWait").dialog("close");
            }
        });
    }
}

/**Añade una nueva partida al GRID*/
function AddItemPrecPediMak(objProd, Pr_Id, Ct_Id, Cantidad, strCod, dblExist, intDevo, strSeries) {
    if (strSeries == null)
        strSeries = "";
    //Bloqueamos el item de sucursales*/
    document.getElementById("pd_bodega").disabled = true;
    //Bloqueamos el selct de porcentajes*/
    document.getElementById("FCT_DESCUENTO").disabled = true;
    //Bloqueamos el tipo de cambio
    document.getElementById("pd_tipoCamCantidad").disabled = true;
    document.getElementById("pd_tipoCamCantidad").style.backgroundColor = "#C2C2C2";
    //Consultamos el precio del producto
    $.ajax({
        type: "POST",
        data: "PR_ID=" + Pr_Id + "&CT_LPRECIOS=" + document.getElementById("FCT_LPRECIOS").value +
                "&CANTIDAD=" + Cantidad + "&FAC_MONEDA=" + document.getElementById("FAC_MONEDA").value +
                "&CT_TIPO_CAMBIO=" + document.getElementById("pd_tipoCam").value,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "DamePrecio.do?id=4",
        success: function (datoPrec) {
            var bolFind = false;
            //Procesamos el XML y lo anadimos al GRID
            var lstXml = datoPrec.getElementsByTagName("Precios")[0];
            var lstprecio = lstXml.getElementsByTagName("Precio");
            for (var i = 0; i < lstprecio.length; i++) {
                var obj2 = lstprecio[i];
                var objImportes = new _ImporteVtaPediMak();
                objImportes.dblCantidad = Cantidad;
                //Aqui hacemos las validaciones o conversiones dependiendo de la moneda
                var dblPrecio = obj2.getAttribute('precioUsar');
                var dblPrecioMul = obj2.getAttribute('precioUsarMul');
                objImportes.dblPuntos = parseFloat(obj2.getAttribute('puntos'));
                objImportes.dblVNegocio = parseFloat(obj2.getAttribute('negocio'));
                objImportes.dblPrecio = parseFloat(dblPrecio);
                objImportes.dblPrecioReal = parseFloat(dblPrecio);
                /*Obtenemos el valor del descuento*/
                var intDescIdx = d.getElementById("FCT_DESCUENTO").selectedIndex;
                var dblDesc = 0;
                if (intDescIdx > 0) {
                    dblDesc = parseFloat(d.getElementById("FCT_DESCUENTO").options[intDescIdx].text);
                }

                objImportes.dblPorcDescGlobal = dblDesc;
                objImportes.dblExento1 = objProd.getAttribute('PR_EXENTO1');
                objImportes.dblExento2 = objProd.getAttribute('PR_EXENTO2');
                objImportes.dblExento3 = objProd.getAttribute('PR_EXENTO3');
                objImportes.intDevo = intDevo;
                if (parseInt(obj2.getAttribute('descuento')) == 0)
                    objImportes.bolAplicDescPrec = false;
                if (parseInt(obj2.getAttribute('desc_pto')) == 0)
                    objImportes.bolAplicDescPto = false;
                if (parseInt(obj2.getAttribute('desc_nego')) == 0)
                    objImportes.bolAplicDescVNego = false;
                //if(lstRow.FACD_DESC_LEAL == 0)objImportes.bolAplicDescPrec= false;
                //Evaluamos si aplican los puntos y valor negocio de multinivel
                var bolAplicaMLM = true;
                if (document.getElementById("FAC_ES_MLM1") != null && document.getElementById("FAC_ES_MLM2") != null) {
                    if (document.getElementById("FAC_ES_MLM2").checked)
                        bolAplicaMLM = false;
                }
                objImportes.bolUsoMLM = bolAplicaMLM;
                //Evaluamos si aplican los puntos y valor negocio de multinivel
                objImportes.CalculaImportePediMak();
                var dblDescuento = objImportes.dblImporteDescuento;
                var dblImporte = objImportes.dblImporte;

                if (Cantidad == '0') {
                    Cantidad = '';
                }


                var datarow = {
                    FACD_ID: 0,
                    FACD_CANTIDAD: Cantidad,
                    FACD_DESCRIPCION: objProd.getAttribute('PR_DESCRIPCION'),
                    FACD_IMPORTE: dblImporte,
                    FACD_CVE: strCod,
                    FACD_PRECIO: dblPrecio,
                    FACD_TASAIVA1: dblTasaVta1,
                    FACD_TASAIVA2: dblTasaVta2,
                    FACD_TASAIVA3: dblTasaVta3,
                    FACD_DESGLOSA1: 1,
                    FACD_IMPUESTO1: objImportes.dblImpuesto1,
                    FACD_IMPUESTO2: objImportes.dblImpuesto2,
                    FACD_IMPUESTO3: objImportes.dblImpuesto3,
                    FACD_PR_ID: Pr_Id,
                    FACD_EXENTO1: objProd.getAttribute('PR_EXENTO1'),
                    FACD_EXENTO2: objProd.getAttribute('PR_EXENTO2'),
                    FACD_EXENTO3: objProd.getAttribute('PR_EXENTO3'),
                    FACD_REQEXIST: objProd.getAttribute('PR_REQEXIST'),
                    FACD_EXIST: dblExist,
                    FACD_NOSERIE: strSeries,
                    FACD_ESREGALO: 0,
                    FACD_IMPORTEREAL: dblImporte,
                    FACD_PRECREAL: dblPrecio,
                    FACD_DESCUENTO: FormatNumber(dblDescuento, 2, true, false, true, false),
                    FACD_PORDESC: objImportes.dblPorcAplica,
                    FACD_PRECFIJO: 0,
                    FACD_ESDEVO: intDevo,
                    FACD_CODBARRAS: objProd.getAttribute('PR_CODBARRAS'),
                    FACD_NOTAS: "",
                    FACD_RET_ISR: intRET_ISR,
                    FACD_RET_IVA: intRET_IVA,
                    FACD_RET_FLETE: intRET_FLETE,
                    FACD_UNIDAD_MEDIDA: objProd.getAttribute('PR_UNIDADMEDIDA'),
                    FACD_PUNTOS_U: objImportes.dblPuntos,
                    FACD_NEGOCIO_U: objImportes.dblVNegocio,
                    FACD_PUNTOS: objImportes.dblPuntosImporte,
                    FACD_NEGOCIO: objImportes.dblVNegocioImporte,
                    FACD_PR_CAT1: objProd.getAttribute('PR_CAT1'),
                    FACD_PR_CAT2: objProd.getAttribute('PR_CAT2'),
                    FACD_PR_CAT3: objProd.getAttribute('PR_CAT3'),
                    FACD_PR_CAT4: objProd.getAttribute('PR_CAT4'),
                    FACD_PR_CAT5: objProd.getAttribute('PR_CAT5'),
                    FACD_PR_CAT6: objProd.getAttribute('PR_CAT6'),
                    FACD_PR_CAT7: objProd.getAttribute('PR_CAT7'),
                    FACD_PR_CAT8: objProd.getAttribute('PR_CAT8'),
                    FACD_PR_CAT9: objProd.getAttribute('PR_CAT9'),
                    FACD_PR_CAT10: objProd.getAttribute('PR_CAT10'),
                    FACD_DESC_ORI: 0,
                    FACD_REGALO: 0,
                    FACD_ID_PROMO: 0,
                    FACD_DESC_PREC: parseInt(obj2.getAttribute('descuento')),
                    FACD_DESC_PTO: parseInt(obj2.getAttribute('desc_pto')),
                    FACD_DESC_VN: parseInt(obj2.getAttribute('desc_nego')),
                    FACD_DESC_LEAL: parseInt(obj2.getAttribute('desc_nego')),
                    FACD_USA_SERIE: objProd.getAttribute('PR_USO_NOSERIE'),
                    FACD_SERIES: strSeries,
                    FACD_SERIES_MPD: "",
                    FACD_SERIES_O: "",
                    FACD_SERIES_MPD_O: "",
                    FACD_CORTESIA: "0",
                    FACD_DISPONIBLE: FormatNumber(objProd.getAttribute('PR_DISPONIBLE'), 2, true, false, true, false),
                    FACD_ES_PAQUETE: objProd.getAttribute('PR_ESKIT'),
                    FACD_ES_COMPONENTE: 0,
                    FACD_PR_PAQUETE: 0,
                    FACD_MULTIPLO: 0,
                    FACD_MPV: objProd.getAttribute('PR_NUM_EMPAQU_VENT'), /*Es la cantidad de multiplos que esta en la ficha de el producto*/
                    FACD_PRECIO_MULTIPLOS: dblPrecioMul, /*El el precio indicado en la ficha de productos para Multiplos*/
                    FACD_ES_MULTIPLO: objProd.getAttribute('PR_ESMULTIPLO'), /*Bandera para indicar que usa Multiplos*/
                    FACD_CANTIDAD_MULTIPLOS: 0, /*Es la cantidad de multiplos posibles en la cantidad capturada*/
                    FACD_PRECIO_TOT_MULTIPLOS: 0, /*Es el precio solo de los multiplos posibes*/
                };
                //Anexamos el registro al GRID
                itemIdPedMak++;
                jQuery("#FAC_GRID").addRowData(itemIdPedMak, datarow, "last");


                //si es kit anadimos los componentes([p])
//                if (objProd.getAttribute('PR_ESKIT') == 1) {
//                    var lstComponente = objProd.getElementsByTagName("vta_componentes");
//                    for (var f = 0; f < lstComponente.length; f++) {
//                        var objComp = lstComponente[f];
//
//                        if (Cantidad == '') {
//                            Cantidad = '0';
//                        }
//                        var dblCantidadPq = parseFloat(Cantidad) * parseFloat(objComp.getAttribute('PAQ_CANTIDAD'));
//                        var datarow1 = {
//                            FACD_ID: 0,
//                            FACD_CANTIDAD: dblCantidadPq,
//                            FACD_DESCRIPCION: "[p]" + objComp.getAttribute('PR_DESCRIPCION'),
//                            FACD_IMPORTE: 0,
//                            FACD_CVE: objComp.getAttribute('PR_CODIGO'),
//                            FACD_PRECIO: 0,
//                            FACD_TASAIVA1: 0,
//                            FACD_TASAIVA2: 0,
//                            FACD_TASAIVA3: 0,
//                            FACD_DESGLOSA1: 1,
//                            FACD_IMPUESTO1: 0,
//                            FACD_IMPUESTO2: 0,
//                            FACD_IMPUESTO3: 0,
//                            FACD_PR_ID: objComp.getAttribute('PR_ID'),
//                            FACD_EXENTO1: objComp.getAttribute('PR_EXENTO1'),
//                            FACD_EXENTO2: objComp.getAttribute('PR_EXENTO2'),
//                            FACD_EXENTO3: objComp.getAttribute('PR_EXENTO3'),
//                            FACD_REQEXIST: objComp.getAttribute('PR_REQEXIST'),
//                            FACD_EXIST: objComp.getAttribute('PR_EXISTENCIA'),
//                            FACD_NOSERIE: "",
//                            FACD_ESREGALO: 0,
//                            FACD_IMPORTEREAL: 0,
//                            FACD_PRECREAL: 0,
//                            FACD_DESCUENTO: 0,
//                            FACD_PORDESC: 0,
//                            FACD_PRECFIJO: 0,
//                            FACD_ESDEVO: 0,
//                            FACD_CODBARRAS: objComp.getAttribute('PR_CODBARRAS'),
//                            FACD_NOTAS: "",
//                            FACD_RET_ISR: 0,
//                            FACD_RET_IVA: 0,
//                            FACD_RET_FLETE: 0,
//                            FACD_UNIDAD_MEDIDA: objComp.getAttribute('PR_NOMUMEDIDA'),
//                            FACD_PUNTOS_U: 0,
//                            FACD_NEGOCIO_U: 0,
//                            FACD_PUNTOS: 0,
//                            FACD_NEGOCIO: 0,
//                            FACD_PR_CAT1: objComp.getAttribute('PR_CAT1'),
//                            FACD_PR_CAT2: objComp.getAttribute('PR_CAT2'),
//                            FACD_PR_CAT3: objComp.getAttribute('PR_CAT3'),
//                            FACD_PR_CAT4: objComp.getAttribute('PR_CAT4'),
//                            FACD_PR_CAT5: objComp.getAttribute('PR_CAT5'),
//                            FACD_PR_CAT6: objComp.getAttribute('PR_CAT6'),
//                            FACD_PR_CAT7: objComp.getAttribute('PR_CAT7'),
//                            FACD_PR_CAT8: objComp.getAttribute('PR_CAT8'),
//                            FACD_PR_CAT9: objComp.getAttribute('PR_CAT9'),
//                            FACD_PR_CAT10: objComp.getAttribute('PR_CAT10'),
//                            FACD_DESC_ORI: 0,
//                            FACD_REGALO: 0,
//                            FACD_ID_PROMO: 0,
//                            FACD_DESC_PREC: 0,
//                            FACD_DESC_PTO: 0,
//                            FACD_DESC_VN: 0,
//                            FACD_DESC_LEAL: 0,
//                            FACD_USA_SERIE: objComp.getAttribute('PR_USO_NOSERIE'),
//                            FACD_SERIES: "",
//                            FACD_SERIES_MPD: "",
//                            FACD_SERIES_O: "",
//                            FACD_SERIES_MPD_O: "",
//                            FACD_CORTESIA: "0",
//                            FACD_DISPONIBLE: 0,
//                            FACD_ES_PAQUETE: 0,
//                            FACD_ES_COMPONENTE: 1,
//                            FACD_PR_PAQUETE: Pr_Id,
//                            FACD_MULTIPLO: objComp.getAttribute('PAQ_CANTIDAD')
//                        };
//                        //Anexamos el registro al GRID
//                        itemIdPedMak++;
//                        jQuery("#FAC_GRID").addRowData(itemIdPedMak, datarow1, "last");
//                    }
//                }

                d.getElementById("FAC_PRECIO").value = dblPrecio;
                d.getElementById("FAC_PROD").value = "";
                d.getElementById("FAC_PROD").focus();
                d.getElementById("FAC_CANT").value = 0;
                d.getElementById("FAC_DESC").value = 0;
                bolFind = true;
                if (intSucOfertas && bolCargaPromociones)
                    _LimpiaRegalosPromosPediMak(jQuery("#FAC_GRID"));
                //Sumamos todos los items
                _PromocionProdPediMak(itemIdPedMak);
                //Validamos el cambio de sucursal
//                EvalSucursal();
            }
            //Validamos si no nos devolvieron precio es porque el CLIENTE no existe
            if (!bolFind) {
//                ObtenNomCtePediMak();
            }
            $("#dialogWait").dialog("close");
        },
        error: function (objeto, quepaso, otroobj) {
            alert(":pto4:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }
    });
}


/*
 *lista de teclas abreviadas en pantalla
 *Espacio para colocar banners de publicidad.
 ***/
/**
 *Representa un importe calculado para la venta
 *@dblImporte es el importe de venta
 **/
function _ImporteVtaPediMak() {
    this.dblImporte = 0;
    this.dblImpuesto1 = 0;
    this.dblImpuesto2 = 0;
    this.dblImpuesto3 = 0;
    this.dblImpuestoReal1 = 0;
    this.dblImpuestoReal2 = 0;
    this.dblImpuestoReal3 = 0;
    this.dblCantidad = 0;
    this.dblPrecio = 0;
    this.dblPorcDesc = 0;
    this.dblPorcDescGlobal = 0;
    this.dblPrecFijo = 0;
    this.dblExento1 = 0;
    this.dblExento2 = 0;
    this.dblExento3 = 0;
    this.dblImporteReal = 0;
    this.dblPrecioReal = 0;
    this.intDevo = 0;
    this.dblPorcAplica = 0;
    this.intPrecioZeros = 0;
    this.dblImporteDescuento = 0;
    //MLM
    this.dblPuntos = 0;
    this.dblVNegocio = 0;
    this.dblPuntosAplica = 0;
    this.dblVNegocioAplica = 0;
    this.dblPuntosImporte = 0;
    this.dblVNegocioImporte = 0;
    this.bolAplicDescPrec = true;
    this.bolAplicDescPto = true;
    this.bolAplicDescVNego = true;
    this.bolUsoMLM = true;
    //MLM
    this.CalculaImportePediMak = function CalculaImportePediMak() {
        //Calculamos el importe
        this.dblPorcDescGlobal = parseFloat(this.dblPorcDescGlobal);
        this.dblPorcDesc = parseFloat(this.dblPorcDesc);
        var dblPrecioAplica = parseFloat(this.dblPrecio);
        //MLM
        this.dblPuntosAplica = this.dblPuntos;
        this.dblVNegocioAplica = this.dblVNegocio;
        //MLM
        //if(this.dblPrecFijo == 0 || this.intPrecioZeros == 1){
        this.dblPorcAplica = 0;
        if (this.dblPorcDescGlobal > 0 && this.dblPorcDesc > 0) {
            if (this.dblPorcDescGlobal > this.dblPorcDesc)
                this.dblPorcAplica = this.dblPorcDescGlobal;
            if (this.dblPorcDesc > this.dblPorcDescGlobal)
                this.dblPorcAplica = this.dblPorcDesc;
            if (this.dblPorcDesc == this.dblPorcDescGlobal)
                this.dblPorcAplica = this.dblPorcDesc;
        } else {
            if (this.dblPorcDescGlobal > 0)
                this.dblPorcAplica = this.dblPorcDescGlobal;
            if (this.dblPorcDesc > 0)
                this.dblPorcAplica = this.dblPorcDesc;
        }
        if (this.dblPorcAplica > 0) {
            if (this.bolAplicDescPrec) {
                dblPrecioAplica = dblPrecioAplica - (dblPrecioAplica * (this.dblPorcAplica / 100));
            }
            //Calculo de descuento en MLM
            if (this.bolAplicDescPto) {
                this.dblPuntosAplica = this.dblPuntosAplica - (this.dblPuntosAplica * (this.dblPorcAplica / 100));
            }
            if (this.bolAplicDescVNego) {
                this.dblVNegocioAplica = this.dblVNegocioAplica - (this.dblVNegocioAplica * (this.dblPorcAplica / 100));
            }
            //Calculo de descuento en MLM
        }
        //}
        this.dblImporte = parseFloat(this.dblCantidad) * parseFloat(dblPrecioAplica);
        this.dblImporteReal = parseFloat(this.dblCantidad) * parseFloat(this.dblPrecioReal);
        //Calculamos el descuento
        if (this.dblImporteReal > 0 && (this.dblImporteReal > this.dblImporte)) {
            this.dblImporteDescuento = this.dblImporteReal - this.dblImporte;
        }
        //Si es una devolucion
        if (parseInt(this.intDevo) == 1) {
            this.dblImporte = this.dblImporte * -1;
        }
        //MLM
        if (this.bolUsoMLM) {
            this.dblPuntosImporte = parseFloat(this.dblCantidad) * parseFloat(this.dblPuntosAplica);
            this.dblVNegocioImporte = parseFloat(this.dblCantidad) * parseFloat(this.dblVNegocioAplica);
        }
        //MLM
        //Validamos si aplica o no el impuesto
        var dblBase1 = this.dblImporte;
        var dblBase2 = this.dblImporte;
        var dblBase3 = this.dblImporte;
        if (parseInt(this.dblExento1) == 1)
            dblBase1 = 0;
        if (parseInt(this.dblExento2) == 1)
            dblBase2 = 0;
        if (parseInt(this.dblExento3) == 1)
            dblBase3 = 0;
        //Calculamos el impuesto
        var tax = new Impuestos(dblTasaVta1, dblTasaVta2, dblTasaVta3, intSImpVta1_2, intSImpVta1_3, intSImpVta2_3); //Objeto para calculo de impuestos
        //Validamos si los precios incluyen impuestos
        if (intPreciosconImp == 1) {
            tax.CalculaImpuesto(dblBase1, dblBase2, dblBase3);
        } else {
            tax.CalculaImpuestoMas(dblBase1, dblBase2, dblBase3);
        }
        if (parseInt(this.dblExento1) == 0)
            this.dblImpuesto1 = tax.dblImpuesto1;
        if (parseInt(this.dblExento2) == 0)
            this.dblImpuesto2 = tax.dblImpuesto2;
        if (parseInt(this.dblExento3) == 0)
            this.dblImpuesto3 = tax.dblImpuesto3;
        //Calculamos impuestos de los importes reales
        //Validamos si aplica o no el impuesto para el importe REAL
        var dblBaseReal1 = this.dblImporteReal;
        var dblBaseReal2 = this.dblImporteReal;
        var dblBaseReal3 = this.dblImporteReal;
        if (parseInt(this.dblExento1) == 1)
            dblBaseReal1 = 0;
        if (parseInt(this.dblExento2) == 1)
            dblBaseReal2 = 0;
        if (parseInt(this.dblExento3) == 1)
            dblBaseReal3 = 0;
        //Calculamos el impuesto
        //Validamos si los precios incluyen impuestos
        if (intPreciosconImp == 1) {
            tax.CalculaImpuesto(dblBaseReal1, dblBaseReal2, dblBaseReal3);
        } else {
            tax.CalculaImpuestoMas(dblBaseReal1, dblBaseReal2, dblBaseReal3);
        }
        if (parseInt(this.dblExento1) == 0)
            this.dblImpuestoReal1 = tax.dblImpuesto1;
        if (parseInt(this.dblExento2) == 0)
            this.dblImpuestoReal2 = tax.dblImpuesto2;
        if (parseInt(this.dblExento3) == 0)
            this.dblImpuestoReal3 = tax.dblImpuesto3;
        if (this.intPrecioZeros == 1) {
            this.dblImporteReal = parseFloat(this.dblCantidad) * parseFloat(this.dblPrecio);
        }
        if (intPreciosconImp == 0) {
            this.dblImporteReal += this.dblImpuestoReal1 + this.dblImpuestoReal2 + this.dblImpuestoReal3;
            this.dblImporte += this.dblImpuesto1 + this.dblImpuesto2 + this.dblImpuesto3;
        }
        //Quitamos el impuesto al descuento
        if (intPreciosconImp == 1) {
            if (this.dblImporteReal > 0) {
                var dblTotImpuesto = tax.dblImpuesto1 + tax.dblImpuesto2 + tax.dblImpuesto3;
                var dblTotImpuestoReal = tax.dblImpuestoReal1 + tax.dblImpuestoReal2 + tax.dblImpuestoReal3;
                if (this.dblImporteReal > 0 && (this.dblImporteReal > this.dblImporte)) {
                    this.dblImporteDescuento = (this.dblImporteReal - dblTotImpuestoReal) - (this.dblImporte - dblTotImpuesto);
                }
            }
        }
    }
}

/*Si esta activo el motor de promociones carga las variables del producto*/
function _PromocionProdPediMak(idItem) {
    if (intSucOfertas && bolCargaPromociones) {
//Obtenemos los valores del elemento
        var gridD = jQuery("#FAC_GRID");
        _LimpiaRegalosPromosPediMak(gridD);
        var lstRow = gridD.getRowData(idItem);
        _CargaVarsProdPediMak(lstRow);
        setSumPediMak();
    } else {
        setSumPediMak();
    }
}

/**Limpia los regalos otorgados*/
function _LimpiaRegalosPromosPediMak(grid) {
    var arr = grid.getDataIDs();
    for (var t = 0; t < arr.length; t++) {
        var id = arr[t];
        var lstRowAct = grid.getRowData(id);
        if (parseInt(lstRowAct.FACD_REGALO) == 1) {
            grid.delRowData(id);
        }
    }
}

/**Carga las variables del producto*/
function _CargaVarsProdPediMak(lstRow) {
    _InitVarsPediMak();
    if (_debugPromos)
        _LogPromosPediMak("Carga de variables del producto....");
    //Recorremos todas las variables
    var strLstCteVarsPost = "";
    var strLstCteVarsPostKeys = "";
    var strLstCteVarsPostProm = "";
    for (var i = 0; i < lstVars.length; i++) {
        var objVar = lstVars[i];
        //Solo los del tipo 2 son con el producto
        if (objVar.intSec == 2) {
//Las variables de clasificacion se consideran de una vez desde esta pantalla
            if (objVar.strNombre.indexOf("$cumpleClas") != -1) {
//Si aun no se cumple la variable seguimos evaluando
                if (!objVar.boolValor) {
                    if (_debugPromos)
                        _LogPromosPediMak(objVar.strNombre + "Valida clasificaciones...");
                    var strNomClas = objVar.strNombre.replace("$cumpleClas", "");
                    //Recorremos las 10 clasificaciones
                    for (var iClas = 1; iClas <= 10; iClas++) {
                        if (strNomClas.indexOf(iClas + "Prod(") != -1) {
                            strNomClas = strNomClas.replace(iClas + "Prod(", "");
                            strNomClas = strNomClas.replace(")", "");
                            try {
                                var _intValorClasRow = 0;
                                if (iClas == 1)
                                    _intValorClasRow = parseInt(lstRow.FACD_PR_CAT1);
                                if (iClas == 2)
                                    _intValorClasRow = parseInt(lstRow.FACD_PR_CAT2);
                                if (iClas == 3)
                                    _intValorClasRow = parseInt(lstRow.FACD_PR_CAT3);
                                if (iClas == 4)
                                    _intValorClasRow = parseInt(lstRow.FACD_PR_CAT4);
                                if (iClas == 5)
                                    _intValorClasRow = parseInt(lstRow.FACD_PR_CAT5);
                                if (iClas == 6)
                                    _intValorClasRow = parseInt(lstRow.FACD_PR_CAT6);
                                if (iClas == 7)
                                    _intValorClasRow = parseInt(lstRow.FACD_PR_CAT7);
                                if (iClas == 8)
                                    _intValorClasRow = parseInt(lstRow.FACD_PR_CAT8);
                                if (iClas == 9)
                                    _intValorClasRow = parseInt(lstRow.FACD_PR_CAT9);
                                if (iClas == 10)
                                    _intValorClasRow = parseInt(lstRow.FACD_PR_CAT10);
                                if (_debugPromos)
                                    _LogPromosPediMak(strNomClas + "vs " + _intValorClasRow);
                                if (parseInt(strNomClas) == _intValorClasRow)
                                    objVar.boolValor = true;
                            } catch (err) {
                                alert(err);
                            }
                        }

                    }
                    if (_debugPromos)
                        _LogPromosPediMak("Cumple clas:" + objVar.boolValor);
                }

            } else {
                if (objVar.strNombre.indexOf("$codigoProducto(") != -1) {
//Si aun no se cumple la variable seguimos evaluando
                    if (!objVar.boolValor) {
                        if (_debugPromos)
                            _LogPromosPediMak(objVar.strNombre + "Buscamos producto...");
                        var strNvaLstProdFind = objVar.strNombre.replace("$codigoProducto(", "");
                        strNvaLstProdFind = strNvaLstProdFind.replace(")", "");
                        strNvaLstProdFind = strNvaLstProdFind.replace(/"/g, ""); //Remplaza todas las comillas  dobles
                        var bolCumple = false;
                        if (strNvaLstProdFind != "") {
                            var _lstRecCve = strNvaLstProdFind.split(",");
                            for (var km = 0; km < _lstRecCve.length; km++) {
                                if (_debugPromos)
                                    _LogPromosPediMak("buscando producto " + _lstRecCve[km] + " vs " + lstRow.FACD_CVE);
                                if (trim(_lstRecCve[km]) == trim(lstRow.FACD_CVE)) {
                                    bolCumple = true;
                                }
                            }
//Asignamos el valor
                            objVar.boolValor = bolCumple;
                        }
                    }

                } else {
                    if (_debugPromos)
                        _LogPromosPediMak(objVar.strNombre);
                    //Generamos lista de variables para enviarlas por post
                    strLstCteVarsPostKeys += objVar.intId + ",";
                    strLstCteVarsPost += objVar.strNombre + ",";
                    strLstCteVarsPostProm += objVar.intPromoId + ",";
                }
            }
        }
    }
//Si existe alguna variable por enviar
    if (strLstCteVarsPost != "") {
//Enviamos el id del producto
//peticion ajax
        var strPost = "CT_ID=" + document.getElementById("FCT_ID").value;
        strPost += "&SC_ID=" + document.getElementById("SC_ID").value;
        strPost += "&PR_ID=" + lstRow.FACD_PR_ID;
        strPost += "&VARS_ID=" + strLstCteVarsPostKeys;
        strPost += "&VARS_VAR=" + strLstCteVarsPost;
        strPost += "&VARS_PROM=" + strLstCteVarsPostProm;
        $("#dialogWait2").dialog("open");
        $.ajax({
            type: "POST",
            data: strPost,
            scriptCharset: "utf-8",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "xml",
            url: _urlPromoMotor + "?id=3",
            success: function (datoVal) {
                //Parseamos el xml
                var objVariables = datoVal.getElementsByTagName("variables")[0];
                var _lstVariable = objVariables.getElementsByTagName("variable");
                for (var h = 0; h < _lstVariable.length; h++) {
                    var objVariable = _lstVariable[h];
                    //Recorremos todas las variables
                    for (var k = 0; k < lstVars.length; k++) {
                        var objVar = lstVars[k];
                        //Solo los del tipo 2 son de producto
                        if (objVar.intSec == 2 &&
                                objVar.intId == objVariable.getAttribute("var_id") &&
                                objVar.intPromoId == objVariable.getAttribute("promo_id")) {

                            //Asigna el valor a la variable
                            _AsignaVars(objVar, objVariable);
                        }
                    }
                }
                $("#dialogWait2").dialog("close");
            },
            error: function (objeto, quepaso, otroobj) {
                $("#dialogWait2").dialog("close");
                alert(":promo ini:" + objeto + " " + quepaso + " " + otroobj);
            }
        });
    }
}

/**Funcion para pintar campos para seleccionar el numero de serie*/
function _drawScNoSeriePediMak(objProd, Pr_Id, Ct_Id, intCantidad, strCod, dblExist, intDevo, idProd, strSeries) {
    _objProdTmpz = objProd;
    if (strSeries == null)
        strSeries = "";
    $.ajax({
        type: "POST",
        data: "PR_ID=" + Pr_Id,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "InvMov.do?id=21",
        success: function (datos) {
            var objCodBar = datos.getElementsByTagName("num_series")[0];
            var lstProd = objCodBar.getElementsByTagName("serie");
            var strOptionSelect = "";
            for (i = 0; i < lstProd.length; i++) {
                var obj = lstProd[i];
                strOptionSelect += "<option value='" + obj.getAttribute("NO_SERIE") + "'>" + obj.getAttribute("NO_SERIE") + "</option>";
            }
            //Abrimos un cuadro de dialogo y dibujamos en el los items
            // por capturar el codigo de barras
            $("#dialog2").dialog("open");
            $('#dialog2').dialog('option', 'title', lstMsg[107]);
            var strHTML = "<input type='hidden' id='_Pr_Id' value='" + Pr_Id + "'>";
            strHTML += "<input type='hidden' id='_Ct_Id' value='" + Ct_Id + "'>";
            strHTML += "<input type='hidden' id='_Cantidad' value='" + intCantidad + "'>";
            strHTML += "<input type='hidden' id='_strCod' value='" + strCod + "'>";
            strHTML += "<input type='hidden' id='_dblExist' value='" + dblExist + "'>";
            strHTML += "<input type='hidden' id='_intDevo' value='" + intDevo + "'>";
            strHTML += "<input type='hidden' id='_idProd' value='" + idProd + "'>";
            strHTML += "<table border=0 cellpadding=0>";
            strHTML += "<tr>";
            strHTML += "<td colspan=3>" + lstMsg[110] + "</td>";
            strHTML += "</tr>";
            strHTML += "<tr>";
            strHTML += "<td nowrap>&nbsp;" + lstMsg[172] + "<input type='text' id='search_cant' value='" + intCantidad + "' size='8' readonly disabled></td>";
            strHTML += "<td nowrap>&nbsp;</td>";
            strHTML += "<td nowrap>&nbsp;" + lstMsg[173] + "<input type='text' id='search_cant_sel' value='0' size='8' readonly disabled></td>";
            strHTML += "</tr>";
            strHTML += "<tr>";
            strHTML += "<td >" + lstMsg[174] + "<br><select id='series_origen' multiple>" + strOptionSelect + "</select></td>";
            strHTML += "<td ><input type='button' id='Agregar' value='" + lstMsg[176] + "' onclick='AgregaSerieX()'><br><input type='button' id='Quitar' value='" + lstMsg[177] + "' onClick='RemueveSerieX()'></td>";
            strHTML += "<td >" + lstMsg[175] + "<br><select id='series_destino' multiple ></select></td>";
            strHTML += "</tr>";
            strHTML += "<tr>";
            strHTML += "<td>" + CreaBoton("", "ConfirmNoSerie1", lstMsg[111], "ConfirmNumSerie();", "left", false, false) + "</td>";
            strHTML += "<td>&nbsp;</td>";
            strHTML += "<td>" + CreaBoton("", "CancelNoSerie2", lstMsg[112], "CancelNumSerie();", "left", false, false) + "</td>";
            strHTML += "</tr>"
            strHTML += "</table>";
            document.getElementById("dialog2_inside").innerHTML = strHTML;
        },
        error: function (objeto, quepaso, otroobj) {
            alert(":Series pto:" + objeto + " " + quepaso + " " + otroobj);
        }
    });
}

/**Obtiene el nombre del cliente al que se le esta haciendo la venta*/
function ObtenNomCtePediMak(objPedido, lstdeta, strTipoVta, bolPasaPedido, bolPasaCotiza) {
    var intCte = document.getElementById("FCT_ID").value;
    if (bolPasaPedido == undefined)
        bolPasaPedido = false;
//    ValidaClean("CT_NOM");
    $.ajax({
        type: "POST",
        data: "CT_ID=" + intCte,
        scriptCharset: "utf-8",
        contentType: "applicatƒion/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "VtasMov.do?id=9",
        success: function (datoVal) {
            var objCte = datoVal.getElementsByTagName("vta_clientes")[0];
            if (objCte.getAttribute('CT_ID') == 0) {
                document.getElementById("CT_NOM").value = "***************";
                ValidaShowPediMak("CT_NOM", lstMsg[28]);
            } else {
                document.getElementById("CT_NOM").value = objCte.getAttribute('CT_RAZONSOCIAL');
                document.getElementById("FCT_LPRECIOS").value = objCte.getAttribute('CT_LPRECIOS');
                document.getElementById("FCT_DIASCREDITO").value = objCte.getAttribute('CT_DIASCREDITO');
                document.getElementById("FAC_DIASCREDITO").value = objCte.getAttribute('CT_DIASCREDITO');
                document.getElementById("FCT_MONTOCRED").value = FormatNumber(objCte.getAttribute('CT_MONTOCRED'), 2, true, false, true, false);
                document.getElementById("FAC_TTC_ID").value = objCte.getAttribute('TTC_ID');
                document.getElementById("FAC_METODOPAGO").value = objCte.getAttribute('CT_METODODEPAGO');
                document.getElementById("FAC_FORMADEPAGO").value = objCte.getAttribute('CT_FORMADEPAGO');
                document.getElementById("FAC_NUMCUENTA").value = objCte.getAttribute('CT_CTABANCO1');
                document.getElementById("pd_vendedor").value = objCte.getAttribute('CT_VENDEDOR');
                intCT_TIPOPERS = objCte.getAttribute('CT_TIPOPERS');
                intCT_TIPOFAC = objCte.getAttribute('CT_TIPOFAC');
                strCT_USOIMBUEBLE = objCte.getAttribute('CT_USOIMBUEBLE');
                //Validamos iva y moneda por default unicamente si no estamos editando un pedido
                if (!bolPasaPedido) {
                    if (parseInt(objCte.getAttribute('MON_ID')) != 0) {
                        document.getElementById("FAC_MONEDA").value = objCte.getAttribute('MON_ID');
                    }
                    if (parseInt(objCte.getAttribute('TI_ID')) != 0) {
                        document.getElementById("FAC_TASASEL1").value = objCte.getAttribute('TI_ID');
                        document.getElementById("FAC_USE_IMP1").value = 1;
                        UpdateTasaImp();
                    }
                    if (parseInt(objCte.getAttribute('TI2_ID')) != 0) {
                        if (document.getElementById("FAC_TASASEL2") != null) {
                            document.getElementById("FAC_TASASEL2").value = objCte.getAttribute('TI2_ID');
                            document.getElementById("FAC_USE_IMP2").value = 1;
                            UpdateTasaImp2PediMak();
                        }
                    }
                    if (parseInt(objCte.getAttribute('TI3_ID')) != 0) {
                        if (document.getElementById("FAC_TASASEL3") != null) {
                            document.getElementById("FAC_TASASEL3").value = objCte.getAttribute('TI3_ID');
                            document.getElementById("FAC_USE_IMP3").value = 1;
                            UpdateTasaImp2PediMak();
                        }
                    }
                    //Direcciones de envio.
                    ObtenDirecionesPediMak();
                    ObtenClienteFinalPediMak();
                    //Moyor de promociones
                    _PromocionCtePediMak();
                }
            }
            //Si esta activa la bandera nos manda a la funcion para mostrar el detalle
            if (bolPasaPedido) {
                DrawPedidoDetaenVentaPediMak(objPedido, lstdeta, strTipoVta);
            }
            if (bolPasaCotiza == null)
                bolPasaCotiza = false;
            //Si esta activa la bandera nos manda a la funcion para mostrar el detalle
            if (bolPasaCotiza) {
                DrawCotizaDetaenVenta(objPedido, lstdeta, strTipoVta);
            }
        },
        error: function (objeto, quepaso, otroobj) {
            document.getElementById("CT_NOM").value = "***************";
            ValidaShowPediMak("CT_NOM", lstMsg[28]);
            alert(":pto:" + objeto + " " + quepaso + " " + otroobj);
        }
    });
}

//Muestra el error de la validacion
function ValidaShowPediMak(strNomField, strMsg) {
//    var objDivErr = document.getElementById("err_" + strNomField);
//    objDivErr.setAttribute("class", "");
//    objDivErr.setAttribute("class", "inError");
//    objDivErr.setAttribute("className", "inError");
//    objDivErr.innerHTML = "<img src='images/layout/report3_del.gif' border='0'>&nbsp;" + strMsg;
}
/**Actualiza la tasa de acuerdo al impuesto seleccionado*/
function UpdateTasaImp2PediMak() {
    var objTasaSel = document.getElementById("FAC_TASASEL2");
    $("#dialogWait").dialog("open");
    //Mandamos la peticion por ajax para que nos den el XML del pedido
    $.ajax({
        type: "POST",
        data: "TI2_ID=" + objTasaSel.value,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "VtasMov.do?id=17",
        success: function (datos) {
            var objPedido = datos.getElementsByTagName("vta_impuesto")[0];
            var lstdeta = objPedido.getElementsByTagName("vta_impuestos");
            for (var i = 0; i < lstdeta.length; i++) {
                var obj = lstdeta[i];
                intIdTasaVta2 = objTasaSel.value;
                dblTasaVta2 = obj.getAttribute('Tasa2');
            }
            $("#dialogWait").dialog("close");
        },
        error: function (objeto, quepaso, otroobj) {
            alert(":Impuestos 2:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }
    });
}

/**SIRVE PARA OBTENER LAS DIRECCIONES DE ENTREGA**/
function  ObtenDirecionesPediMak() {
    var bolExist = false;
    //Peticion por ajax para mostrar info
    $.ajax({
        type: "POST",
        data: "ct_id=" + document.getElementById("FCT_ID").value, //id de cliente seleccionado
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "ERP_DireccionesEntrega.jsp?ID=1",
        success: function (datos) {
            var lstDirecciones = datos.getElementsByTagName("Direcciones")[0];
            var lstDireccion = lstDirecciones.getElementsByTagName("direcciones");
            var objDir = document.getElementById("CT_DIRENTREGA");
            select_clear(objDir);
            for (j = 0; j < lstDireccion.length; j++) {
                bolExist = true;
                var objP = lstDireccion[j];
                if (j == 0) {
                    select_add(objDir, "<-Seleccione->", 0);
                }
                select_add(objDir, objP.getAttribute('dir_cliente'), objP.getAttribute('id_dir'));
            }
            if (bolExist == false) {
                select_add(objDir, '<--No existen mas Direcciones-->', 0);
            }

        },
        error: function (objeto, quepaso, otroobj) {
            alert(":pto:" + objeto + " " + quepaso + " " + otroobj);
        }
    });
}
function  ObtenClienteFinalPediMak() {
    var bolExist = false;
    //Peticion por ajax para mostrar info
    $.ajax({
        type: "POST",
        data: "ct_id=" + document.getElementById("FCT_ID").value, //id de cliente seleccionado
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "ERP_ClienteFacturacion.jsp?ID=5",
        success: function (datos) {
            var lstDirecciones = datos.getElementsByTagName("Direcciones")[0];
            var lstDireccion = lstDirecciones.getElementsByTagName("direcciones");
            var objDir = document.getElementById("CT_CLIENTEFINAL");
            select_clear(objDir);
            for (j = 0; j < lstDireccion.length; j++) {
                bolExist = true;
                var objP = lstDireccion[j];
                if (j == 0) {
                    select_add(objDir, "<-Seleccione->", 0);
                }
                select_add(objDir, objP.getAttribute('dir_cliente'), objP.getAttribute('id_dir'));
            }
            if (bolExist == false) {
                select_add(objDir, '<--No existen mas Clientes-->', 0);
            }

        },
        error: function (objeto, quepaso, otroobj) {
            alert(":pto:" + objeto + " " + quepaso + " " + otroobj);
        }
    });
}

/*Si esta activo el motor de promociones carga las variables del cliente*/
function _PromocionCtePediMak() {
    if (intSucOfertas && bolCargaPromociones) {
        _CargaVarsCte();
    }
}

/*Suma todos los items de la venta y nos da el total**/
function setSumPediMak(bolPromos) {
    var grid = jQuery("#FAC_GRID");
    //Limpiamos el grid de los regalos antes del calculo del nuevo 

    var arr = grid.getDataIDs();
    var dblSuma = 0;
    var dblImpuesto1 = 0;
    var dblImpuesto2 = 0;
    var dblImpuesto3 = 0;
    var dblImporte = 0;
    var dblImporteDesc = 0;
    var dblImportePto = 0;
    var dblImporteVn = 0;
    var dblImporteReal = 0;
    var dblImportePzas = 0;
    var dblImportePtoReal = 0;
    var dblImporteNegoReal = 0;
    var dblImporteCredReal = 0;
    var dblImporteImpuesto1Real = 0;
    var dblImporteImpuesto2Real = 0;
    var dblImporteImpuesto3Real = 0;
    for (var i = 0; i < arr.length; i++) {
        var id = arr[i];
        var lstRow = grid.getRowData(id);
        dblSuma += parseFloat(lstRow.FACD_IMPORTE.replace(",", ""));
        dblImpuesto1 += parseFloat(lstRow.FACD_IMPUESTO1);
        dblImpuesto2 += parseFloat(lstRow.FACD_IMPUESTO2);
        dblImpuesto3 += parseFloat(lstRow.FACD_IMPUESTO3);
        dblImporte += (parseFloat(lstRow.FACD_IMPORTE.replace(",", "")) - parseFloat(lstRow.FACD_IMPUESTO1) - parseFloat(lstRow.FACD_IMPUESTO2) - parseFloat(lstRow.FACD_IMPUESTO3));
        dblImporteDesc += parseFloat(lstRow.FACD_DESCUENTO.replace(",", ""));
        dblImportePto += parseFloat(lstRow.FACD_PUNTOS);
        dblImporteVn += parseFloat(lstRow.FACD_NEGOCIO);
        //Calculo de totales adicionales utiles para las promociones
        dblImportePzas += parseFloat(lstRow.FACD_CANTIDAD);
        dblImportePtoReal += parseFloat(lstRow.FACD_CANTIDAD) * parseFloat(lstRow.FACD_PUNTOS_U);
        dblImporteNegoReal += parseFloat(lstRow.FACD_CANTIDAD) * parseFloat(lstRow.FACD_NEGOCIO_U);
        dblImporteCredReal += parseFloat(lstRow.FACD_CANTIDAD) * 1;
        //Calculo de totales reales
        var dblTotlImpReal = parseFloat(lstRow.FACD_CANTIDAD) * parseFloat(lstRow.FACD_PRECREAL);
        var dblBase1 = dblTotlImpReal;
        var dblBase2 = dblTotlImpReal;
        var dblBase3 = dblTotlImpReal;
        if (parseInt(lstRow.FACD_EXENTO1) == 1)
            dblBase1 = 0;
        if (parseInt(lstRow.FACD_EXENTO2) == 1)
            dblBase2 = 0;
        if (parseInt(lstRow.FACD_EXENTO3) == 1)
            dblBase3 = 0;
        var taxReal = new Impuestos(dblTasaVta1, dblTasaVta2, dblTasaVta3, intSImpVta1_2, intSImpVta1_3, intSImpVta2_3); //Objeto para calculo de impuestos
        //Validamos si los precios incluyen impuestos
        if (intPreciosconImp == 1) {
            taxReal.CalculaImpuesto(dblBase1, dblBase2, dblBase3);
        } else {
            taxReal.CalculaImpuestoMas(dblBase1, dblBase2, dblBase3);
        }
        if (parseInt(lstRow.FACD_EXENTO1) == 0)
            dblImporteImpuesto1Real = taxReal.dblImpuesto1;
        if (parseInt(lstRow.FACD_EXENTO2) == 0)
            dblImporteImpuesto2Real = taxReal.dblImpuesto2;
        if (parseInt(lstRow.FACD_EXENTO3) == 0)
            dblImporteImpuesto3Real = taxReal.dblImpuesto3;
        if (intPreciosconImp == 1) {
            dblImporteReal += dblTotlImpReal - dblImporteImpuesto1Real - dblImporteImpuesto2Real - dblImporteImpuesto3Real;
        } else {
            dblImporteReal += dblTotlImpReal;
        }
//Calculo de totales adicionales utiles para las promociones
    }
//Anadimos IEPS
    var dblIEPS = 0;
    if (document.getElementById("FAC_USO_IEPS1").checked) {
        if (parseFloat(document.getElementById("FAC_TASA_IEPS").value) != 0) {
            try {
                dblIEPS = dblImporte * (parseFloat(document.getElementById("FAC_TASA_IEPS").value) / 100);
            } catch (err) {
            }
        } else {
            alert(lstMsg[62]);
            document.getElementById("FAC_TASA_IEPS").focus();
        }
//Aumentamos el IVA
        var tax = new Impuestos(dblTasaVta1, dblTasaVta2, dblTasaVta3, intSImpVta1_2, intSImpVta1_3, intSImpVta2_3); //Objeto para calculo de impuestos
        tax.CalculaImpuestoMas(dblIEPS, 0, 0);
        dblImpuesto1 += tax.dblImpuesto1;
        dblSuma += dblIEPS + tax.dblImpuesto1;
    }
    d.getElementById("FAC_IMPORTE_IEPS").value = FormatNumber(dblIEPS, intNumdecimal, true);
    d.getElementById("FAC_TOT").value = FormatNumber(dblSuma, 2, true, false, true, false);
    d.getElementById("FAC_IMPUESTO1").value = FormatNumber(dblImpuesto1, 2, true, false, true, false);
    d.getElementById("FAC_IMPUESTO2").value = FormatNumber(dblImpuesto2, intNumdecimal, true);
    d.getElementById("FAC_IMPUESTO3").value = FormatNumber(dblImpuesto3, intNumdecimal, true);
    d.getElementById("FAC_IMPORTE").value = FormatNumber(dblImporte, 2, true, false, true, false);
    d.getElementById("FAC_DESCUENTO").value = FormatNumber(dblImporteDesc, 2, true, false, true, false);
    //MLM
    d.getElementById("FAC_PUNTOS").value = FormatNumber(dblImportePto, intNumdecimal, true);
    d.getElementById("FAC_NEGOCIO").value = FormatNumber(dblImporteVn, intNumdecimal, true);
    d.getElementById("FAC_IMPORTE_REAL").value = FormatNumber(dblImporteReal, intNumdecimal, true);
    d.getElementById("FAC_PZAS").value = FormatNumber(dblImportePzas, intNumdecimal, true);
    d.getElementById("FAC_PUNTOS_REAL").value = FormatNumber(dblImportePtoReal, intNumdecimal, true);
    d.getElementById("FAC_NEGOCIO_REAL").value = FormatNumber(dblImporteNegoReal, intNumdecimal, true);
    d.getElementById("FAC_CREDITOS_REAL").value = FormatNumber(dblImporteCredReal, intNumdecimal, true);
    d.getElementById("FAC_IMPUESTO1_REAL").value = FormatNumber(dblImporteImpuesto1Real, intNumdecimal, true);
    d.getElementById("FAC_IMPUESTO2_REAL").value = FormatNumber(dblImporteImpuesto2Real, intNumdecimal, true);
    d.getElementById("FAC_IMPUESTO3_REAL").value = FormatNumber(dblImporteImpuesto3Real, intNumdecimal, true);
    //MLM
    //Activamos recibos de honorarios si proceden SOLO EN CASO DE FACTURAS
    if (parseInt(intEMP_TIPOPERS) == 2
            && parseInt(d.getElementById("FAC_TIPO").value) == 1) {
        if (intCT_TIPOPERS == 1) {
            var dblRetIsr = dblImporte * (dblFacRetISR / 100);
            var dblRetIVA = 0;
            if (dblImpuesto1 > 0) {
                dblRetIVA = (dblImpuesto1 / 3) * 2;
            }
//Exento retencion ISR
            if (parseInt(intEMP_NO_ISR) == 1) {
                dblRetIsr = 0;
            }
//Exento retencion IVA
            if (parseInt(intEMP_NO_IVA) == 1) {
                dblRetIVA = 0;
            }
            var dblImpNeto = dblSuma - dblRetIsr - dblRetIVA;
            document.getElementById("FAC_RETISR").value = FormatNumber(dblRetIsr, intNumdecimal, true);
            document.getElementById("FAC_RETIVA").value = FormatNumber(dblRetIVA, intNumdecimal, true);
            document.getElementById("FAC_NETO").value = FormatNumber(dblImpNeto, intNumdecimal, true);
            //Activamos los recibos de honorarios
            document.getElementById("FAC_RETISR").parentNode.parentNode.style.display = 'block';
            document.getElementById("FAC_RETIVA").parentNode.parentNode.style.display = 'block';
            document.getElementById("FAC_NETO").parentNode.parentNode.style.display = 'block';
        } else {
//Activamos los recibos de honorarios
            document.getElementById("FAC_RETISR").parentNode.parentNode.style.display = 'none';
            document.getElementById("FAC_RETIVA").parentNode.parentNode.style.display = 'none';
            document.getElementById("FAC_NETO").parentNode.parentNode.style.display = 'none';
            document.getElementById("FAC_RETISR").value = FormatNumber(0, intNumdecimal, true);
            document.getElementById("FAC_RETIVA").value = FormatNumber(0, intNumdecimal, true);
            document.getElementById("FAC_NETO").value = FormatNumber(dblSuma, intNumdecimal, true);
        }

    } else {
//Activamos los recibos de honorarios
        document.getElementById("FAC_RETISR").parentNode.parentNode.style.display = 'none';
        document.getElementById("FAC_RETIVA").parentNode.parentNode.style.display = 'none';
        document.getElementById("FAC_NETO").parentNode.parentNode.style.display = 'none';
    }
    if (bolPromos == null)
        bolPromos = true;
    //Promociones
    _PromocionTotPediMak(bolPromos);
}

/*Si esta activo el motor de promociones carga las variables de los totales*/
function _PromocionTotPediMak(bolPromosExec) {
    if (bolPromosExec) {
//Validamos si procede
        if (intSucOfertas && bolCargaPromociones) {
            var grid = jQuery("#FAC_GRID");
            var arr = grid.getDataIDs();
            if (arr.length > 0) {
//Cargamos las variables de los totales
                _CargaVarsTot("_PromocionFormulasPediMak();");
            } else {
//Resetamos las variables de producto
                _ResetVarsProd();
            }

        }
    }

}
/*Si esta activo el motor de promociones calcula la formula*/
function _PromocionFormulasPediMak() {
    if (intSucOfertas && bolCargaPromociones) {
        _CargaCalculaFormulasPediMak(2);
    }
}

/**Calculas las formulas*/
function _CargaCalculaFormulasPediMak(intFase) {
    if (_debugPromos)
        _LogPromosPediMak("Vamos a calcular las formulas....");
    $("#dialogWaitProm").dialog("open");
    //Resetamos las ofertas aplicadas
    _ResetAplicaOfertas(intFase);
    //Variable para conocer si aplico una formula
    var bolAplicaPromo = false;
    //Recorremos todas las variables y las cargamos para poderlas aplicar posteriormente
    var _lstPromo = objPromos.getElementsByTagName("promocion");
    for (var h = 0; h < _lstPromo.length; h++) {
        var objPromo = _lstPromo[h];
        //Evaluamos que la fase de la promocion sea la correcta
        if (objPromo.getAttribute("PROM_FASE") == intFase) {
            bolAplicaPromo = true;
            var strFormulaAplica = objPromo.getAttribute("PROM_FORMULA");
            if (_debugPromos)
                _LogPromosPediMak("(1)strFormulaAplica:" + strFormulaAplica);
            //Reemplazamos las y por &&
            strFormulaAplica = strFormulaAplica.replace(/ y /g, " && ");
            strFormulaAplica = strFormulaAplica.replace(/ o /g, " || ");
            strFormulaAplica = strFormulaAplica.replace("$Hoy", strHoyFecha);
            //Reemplazamos las o por ||
            //Recorremos todas las variables
            for (var i = 0; i < lstVars.length; i++) {
                var objVar = lstVars[i];
                if (parseInt(objVar.intPromoId) == parseInt(objPromo.getAttribute("PROM_ID"))) {
                    var strValor = objVar.strValor;
                    if (objVar.strTipoDato == "boolean")
                        strValor = objVar.boolValor;
                    if (objVar.strTipoDato == "integer")
                        strValor = objVar.intValor;
                    if (objVar.strTipoDato == "double")
                        strValor = objVar.dblValor;
                    if (objVar.strTipoDato == "date")
                        strValor = objVar.dateValor;
                    while (strFormulaAplica.indexOf(objVar.strNombre) != -1) {
                        strFormulaAplica = strFormulaAplica.replace(objVar.strNombre, strValor);
                    }
                }
            }
//Evaluamos la formula
            var bolPasaPromo = false;
            strFormulaAplica = "if(" + strFormulaAplica + ")bolPasaPromo=true;";
            try {
                eval(strFormulaAplica);
            } catch (error) {
                alert("Error al ejecutar la formula:" + error);
            }
            if (_debugPromos)
                _LogPromosPediMak("(3)strFormulaAplica:" + strFormulaAplica + " " + bolPasaPromo);
            //Activar ofertas
            if (bolPasaPromo) {
                if (lstOfertasActivas == null)
                    lstOfertasActivas = Array();
                var objOferta = new _ClassOferta();
                objOferta.intIdOferta = objPromo.getAttribute("PROM_ID");
                objOferta.strNombre = objPromo.getAttribute("PROM_NOMBRE");
                objOferta.strDesc = objPromo.getAttribute("PROM_DESCRIPCION");
                _intContaOfertas++;
                lstOfertasActivas[_intContaOfertas] = objOferta;
            }
        }


    }
//Dibujamos y aplicamos las ofertas en caso de haberse ejecutado alguna 
    if (bolAplicaPromo) {
        _drawOfertasPediMak();
        _AplicaOfertas();
    } else {
        $("#dialogWaitProm").dialog("close");
    }
}

/**Vuelva a calcular el precio para una fila del grid*/
function lstRowChangePrecioPediMak(lstRow, idUpdate, grid, bolSum) {
    //@@@@@@@@@@@@@@@@@@@@@@ Si es multiplo y sobran generar nueva partida
    //Es multiplo
    //Cuantas parejas se hacen
    //5 = cantidad /5 de esto cuantas sobran
    // si sobra añadir partida


    var objImportes = new _ImporteVtaPediMak();
    objImportes.dblCantidad = parseFloat(lstRow.FACD_CANTIDAD);

    objImportes.dblPrecio = parseFloat(lstRow.FACD_PRECIO.replace(",", ""));
    objImportes.dblPrecioReal = parseFloat(lstRow.FACD_PRECREAL);
    objImportes.dblPuntos = parseFloat(lstRow.FACD_PUNTOS_U);
    objImportes.dblVNegocio = parseFloat(lstRow.FACD_NEGOCIO_U);
    /*Obtenemos el valor del descuento*/
    var intDescIdx = d.getElementById("FCT_DESCUENTO").selectedIndex;
    var dblDesc = 0;
    if (intDescIdx > 0) {
        dblDesc = parseFloat(d.getElementById("FCT_DESCUENTO").options[intDescIdx].text);
    }
    objImportes.dblPorcDescGlobal = dblDesc;
    objImportes.dblPorcDesc = lstRow.FACD_PORDESC;
    objImportes.dblPrecFijo = lstRow.FACD_PRECFIJO;
    objImportes.dblExento1 = lstRow.FACD_EXENTO1;
    objImportes.dblExento2 = lstRow.FACD_EXENTO2;
    objImportes.dblExento3 = lstRow.FACD_EXENTO3;
    objImportes.intDevo = lstRow.FACD_ESDEVO;
    objImportes.intPrecioZeros = lstRow.FACD_SINPRECIO;
    if (lstRow.FACD_DESC_PREC == 0)
        objImportes.bolAplicDescPrec = false;
    if (lstRow.FACD_DESC_PTO == 0)
        objImportes.bolAplicDescPto = false;
    if (lstRow.FACD_DESC_VN == 0)
        objImportes.bolAplicDescVNego = false;
    //if(lstRow.FACD_DESC_LEAL == 0)objImportes.bolAplicDescPrec= false;
    //Evaluamos si aplican los puntos y valor negocio de multinivel
    var bolAplicaMLM = true;
    if (document.getElementById("FAC_ES_MLM1") != null && document.getElementById("FAC_ES_MLM2") != null) {
        if (document.getElementById("FAC_ES_MLM2").checked)
            bolAplicaMLM = false;
    }
    objImportes.bolUsoMLM = bolAplicaMLM;
    //Evaluamos si aplican los puntos y valor negocio de multinivel
    objImportes.CalculaImportePediMak();
    //Asignamos nuevos importes
    lstRow.FACD_IMPORTE = objImportes.dblImporte;
    lstRow.FACD_IMPUESTO1 = objImportes.dblImpuesto1;
    lstRow.FACD_IMPUESTO2 = objImportes.dblImpuesto2;
    lstRow.FACD_IMPUESTO3 = objImportes.dblImpuesto3;
    lstRow.FACD_PORDESC = objImportes.dblPorcAplica;
    lstRow.FACD_DESCUENTO = FormatNumber(objImportes.dblImporteDescuento, 2, true, false, true, false);
    lstRow.FACD_IMPORTEREAL = objImportes.dblImporteReal;
    lstRow.FACD_PUNTOS = objImportes.dblPuntosImporte;
    lstRow.FACD_NEGOCIO = objImportes.dblVNegocioImporte;
    if (lstRow.FACD_NEGO_ZERO == 1)
        lstRow.FACD_NEGOCIO = 0;
    /*Multiplicamos la cantidad [P]*/
    if (lstRow.FACD_ES_PAQUETE == "1") {
        SumaCantComponentes(lstRow);
    }
    /*Multiplos*/
    calculaMultiplos(lstRow, 0);
    //Actualizamos el grid
    grid.setRowData(idUpdate, lstRow);
    //Sumamos todos los items
    if (bolSum == null)
        bolSum = true;
    if (bolSum)
        setSumPediMak();
}

/**
 *Representa un importe calculado para la venta
 *@dblImporte es el importe de venta
 **/
function _ImporteVtaPediMak() {
    this.dblImporte = 0;
    this.dblImpuesto1 = 0;
    this.dblImpuesto2 = 0;
    this.dblImpuesto3 = 0;
    this.dblImpuestoReal1 = 0;
    this.dblImpuestoReal2 = 0;
    this.dblImpuestoReal3 = 0;
    this.dblCantidad = 0;
    this.dblPrecio = 0;
    this.dblPorcDesc = 0;
    this.dblPorcDescGlobal = 0;
    this.dblPrecFijo = 0;
    this.dblExento1 = 0;
    this.dblExento2 = 0;
    this.dblExento3 = 0;
    this.dblImporteReal = 0;
    this.dblPrecioReal = 0;
    this.intDevo = 0;
    this.dblPorcAplica = 0;
    this.intPrecioZeros = 0;
    this.dblImporteDescuento = 0;
    //MLM
    this.dblPuntos = 0;
    this.dblVNegocio = 0;
    this.dblPuntosAplica = 0;
    this.dblVNegocioAplica = 0;
    this.dblPuntosImporte = 0;
    this.dblVNegocioImporte = 0;
    this.bolAplicDescPrec = true;
    this.bolAplicDescPto = true;
    this.bolAplicDescVNego = true;
    this.bolUsoMLM = true;
    //MLM
    this.CalculaImportePediMak = function CalculaImportePediMak() {
        //Calculamos el importe
        this.dblPorcDescGlobal = parseFloat(this.dblPorcDescGlobal);
        this.dblPorcDesc = parseFloat(this.dblPorcDesc);
        var dblPrecioAplica = parseFloat(this.dblPrecio);
        //MLM
        this.dblPuntosAplica = this.dblPuntos;
        this.dblVNegocioAplica = this.dblVNegocio;
        //MLM
        //if(this.dblPrecFijo == 0 || this.intPrecioZeros == 1){
        this.dblPorcAplica = 0;
        if (this.dblPorcDescGlobal > 0 && this.dblPorcDesc > 0) {
            if (this.dblPorcDescGlobal > this.dblPorcDesc)
                this.dblPorcAplica = this.dblPorcDescGlobal;
            if (this.dblPorcDesc > this.dblPorcDescGlobal)
                this.dblPorcAplica = this.dblPorcDesc;
            if (this.dblPorcDesc == this.dblPorcDescGlobal)
                this.dblPorcAplica = this.dblPorcDesc;
        } else {
            if (this.dblPorcDescGlobal > 0)
                this.dblPorcAplica = this.dblPorcDescGlobal;
            if (this.dblPorcDesc > 0)
                this.dblPorcAplica = this.dblPorcDesc;
        }
        if (this.dblPorcAplica > 0) {
            if (this.bolAplicDescPrec) {
                dblPrecioAplica = dblPrecioAplica - (dblPrecioAplica * (this.dblPorcAplica / 100));
            }
            //Calculo de descuento en MLM
            if (this.bolAplicDescPto) {
                this.dblPuntosAplica = this.dblPuntosAplica - (this.dblPuntosAplica * (this.dblPorcAplica / 100));
            }
            if (this.bolAplicDescVNego) {
                this.dblVNegocioAplica = this.dblVNegocioAplica - (this.dblVNegocioAplica * (this.dblPorcAplica / 100));
            }
            //Calculo de descuento en MLM
        }
        //}
        this.dblImporte = parseFloat(this.dblCantidad) * parseFloat(dblPrecioAplica);
        this.dblImporteReal = parseFloat(this.dblCantidad) * parseFloat(this.dblPrecioReal);
        //Calculamos el descuento
        if (this.dblImporteReal > 0 && (this.dblImporteReal > this.dblImporte)) {
            this.dblImporteDescuento = this.dblImporteReal - this.dblImporte;
        }
        //Si es una devolucion
        if (parseInt(this.intDevo) == 1) {
            this.dblImporte = this.dblImporte * -1;
        }
        //MLM
        if (this.bolUsoMLM) {
            this.dblPuntosImporte = parseFloat(this.dblCantidad) * parseFloat(this.dblPuntosAplica);
            this.dblVNegocioImporte = parseFloat(this.dblCantidad) * parseFloat(this.dblVNegocioAplica);
        }
        //MLM
        //Validamos si aplica o no el impuesto
        var dblBase1 = this.dblImporte;
        var dblBase2 = this.dblImporte;
        var dblBase3 = this.dblImporte;
        if (parseInt(this.dblExento1) == 1)
            dblBase1 = 0;
        if (parseInt(this.dblExento2) == 1)
            dblBase2 = 0;
        if (parseInt(this.dblExento3) == 1)
            dblBase3 = 0;
        //Calculamos el impuesto
        var tax = new Impuestos(dblTasaVta1, dblTasaVta2, dblTasaVta3, intSImpVta1_2, intSImpVta1_3, intSImpVta2_3); //Objeto para calculo de impuestos
        //Validamos si los precios incluyen impuestos
        if (intPreciosconImp == 1) {
            tax.CalculaImpuesto(dblBase1, dblBase2, dblBase3);
        } else {
            tax.CalculaImpuestoMas(dblBase1, dblBase2, dblBase3);
        }
        if (parseInt(this.dblExento1) == 0)
            this.dblImpuesto1 = tax.dblImpuesto1;
        if (parseInt(this.dblExento2) == 0)
            this.dblImpuesto2 = tax.dblImpuesto2;
        if (parseInt(this.dblExento3) == 0)
            this.dblImpuesto3 = tax.dblImpuesto3;
        //Calculamos impuestos de los importes reales
        //Validamos si aplica o no el impuesto para el importe REAL
        var dblBaseReal1 = this.dblImporteReal;
        var dblBaseReal2 = this.dblImporteReal;
        var dblBaseReal3 = this.dblImporteReal;
        if (parseInt(this.dblExento1) == 1)
            dblBaseReal1 = 0;
        if (parseInt(this.dblExento2) == 1)
            dblBaseReal2 = 0;
        if (parseInt(this.dblExento3) == 1)
            dblBaseReal3 = 0;
        //Calculamos el impuesto
        //Validamos si los precios incluyen impuestos
        if (intPreciosconImp == 1) {
            tax.CalculaImpuesto(dblBaseReal1, dblBaseReal2, dblBaseReal3);
        } else {
            tax.CalculaImpuestoMas(dblBaseReal1, dblBaseReal2, dblBaseReal3);
        }
        if (parseInt(this.dblExento1) == 0)
            this.dblImpuestoReal1 = tax.dblImpuesto1;
        if (parseInt(this.dblExento2) == 0)
            this.dblImpuestoReal2 = tax.dblImpuesto2;
        if (parseInt(this.dblExento3) == 0)
            this.dblImpuestoReal3 = tax.dblImpuesto3;
        if (this.intPrecioZeros == 1) {
            this.dblImporteReal = parseFloat(this.dblCantidad) * parseFloat(this.dblPrecio);
        }
        if (intPreciosconImp == 0) {
            this.dblImporteReal += this.dblImpuestoReal1 + this.dblImpuestoReal2 + this.dblImpuestoReal3;
            this.dblImporte += this.dblImpuesto1 + this.dblImpuesto2 + this.dblImpuesto3;
        }
        //Quitamos el impuesto al descuento
        if (intPreciosconImp == 1) {
            if (this.dblImporteReal > 0) {
                var dblTotImpuesto = tax.dblImpuesto1 + tax.dblImpuesto2 + tax.dblImpuesto3;
                var dblTotImpuestoReal = tax.dblImpuestoReal1 + tax.dblImpuestoReal2 + tax.dblImpuestoReal3;
                if (this.dblImporteReal > 0 && (this.dblImporteReal > this.dblImporte)) {
                    this.dblImporteDescuento = (this.dblImporteReal - dblTotImpuestoReal) - (this.dblImporte - dblTotImpuesto);
                }
            }
        }
    }
}

/**Realizar la operacion de guardado de la venta mostrando primero la pantalla de pago*/
function SaveVtaPediMak() {
    var bolPasa = true;
    //Validamos si el total es igual a cero
    /*if(parseFloat(document.getElementById("FAC_TOT").value)== 0){
     alert(lstMsg[56]);
     bolPasa =false;
     }else{
     */
    if (d.getElementById("TOTALXPAGAR") != null) {
        if (parseInt(intEMP_TIPOPERS) == 2 && intCT_TIPOPERS == 1
                && parseInt(d.getElementById("FAC_TIPO").value) == 1) {
            d.getElementById("TOTALXPAGAR").value = d.getElementById("FAC_NETO").value;
        } else {
            d.getElementById("TOTALXPAGAR").value = d.getElementById("FAC_TOT").value.replace(",", "");
        }
    }
//Validamos ADDENDA MABE
    if (d.getElementById("ADD_MABE") != null) {
        if (d.getElementById("ADD_MABE").value != undefined) {
            if (d.getElementById("ADD_MABE").value != 0) {
                if (d.getElementById("USA_MABE1").checked) {
//Validamos campos
                    if (!ValidaMABE()) {
                        alert(lstMsg[63]);
                        bolPasa = false;
                    }
                }
            }
        }
    }
//    Validamos ADDENDA AMECE
    if (d.getElementById("ADD_AMECE") != null) {
        if (d.getElementById("ADD_AMECE").value != undefined) {
            if (d.getElementById("ADD_AMECE").value != 0) {
                if (d.getElementById("USA_AMECE1").checked) {
//Validamos campos
                    if (!ValidaAMECE()) {
                        alert(lstMsg[155]);
                        bolPasa = false;
                    }
                }
            }
        }
    }

//Validamos ADDENDA SANOFI ORDEN DE COMPRA
    if (document.getElementById("ADD_SANOFI") != null) {
        if (document.getElementById("ADD_SANOFI").value != undefined) {
            if (document.getElementById("ADD_SANOFI").value != 0) {
                if (document.getElementById("USA_SANOFI1").checked) {
//Validamos campos
                    if (!ValidaSANOFI()) {//HACER MÉTODO QUE VALIDE CAMPOS SANOFI
                        alert(lstMsg[153]);
                        bolPasa = false;
                    }
                }
            }
        }
    }

//Validamos ADDENDA FEMSA
    if (d.getElementById("ADD_FEMSA") != null) {
        if (d.getElementById("ADD_FEMSA").value != undefined) {
            if (d.getElementById("ADD_FEMSA").value != 0) {
                if (d.getElementById("USA_FEMSA1").checked) {
//Validamos campos
                    if (!ValidaFEMSA()) {
                        alert(lstMsg[155]);
                        bolPasa = false;
                    }
                }
            }
        }
    }

    if (validaDatosPediMak() == false) {
        bolPasa = false;
    }

    if (VerificaClientePediMak() == "false") {
        bolPasa = false;
    }
    /*Evaluamos que ningun producto en el grid contenga valor 0 o vacio en la cantidad*/
    if (EvaluaCantProdPediMak() == "false") {
        bolPasa = false;
    }
    /**evaluamos el porcentaje de credito*/
    if (parseFloat(document.getElementById("FCT_DESCUENTO").value) > 0 && document.getElementById("STATUS").value == 11) {
        if (parseFloat(document.getElementById("FCT_DESCUENTO").value) > parseFloat(document.getElementById("US_MAX_DESC").value)) {
            alert("No puede confirmar el pedido debido a que rebasa el % de descuento que tiene autorizado");
            bolPasa = false;
        }
    }

//Si pasa
    if (bolPasa) {
//Validamos el tipo de venta
        if (document.getElementById("FAC_TIPO").value != "3" && document.getElementById("FAC_TIPO").value != "5") {
            if (parseFloat(document.getElementById("FAC_TOT").value.replace(",", "")) == 0) {
                var aRespC = confirm(lstMsg[184]);
                if (aRespC)
                    SaveVtaDoPediMak();
            } else {
//Evaluamos si los dias de credito son mayores a cero
                if (parseFloat(document.getElementById("FAC_DIASCREDITO").value) == 0) {
                    OpnOpt('FORMPAGO', '_ed', 'dialogPagos', false, false, true);
                    var objMainFacPedi = objMap.getScreen("FORMPAGO");
                    objMainFacPedi.bolActivo = false;
                    objMainFacPedi.bolMain = false;
                    objMainFacPedi.bolInit = false;
                    objMainFacPedi.idOperAct = 0;
                } else {
                    SaveVtaDoPediMak();
                }
            }
        } else {
            SaveVtaDoPediMak();
        }
    }
//}
}

/**Guardar operacion*/
function Callbtn0PediMak() {
    if (bolSaveVta) {
        if (identiPantallaMak == 1) {
            document.getElementById("STATUS").value = 10;
        } else {
            if (identiPantallaMak == 2) {
                document.getElementById("STATUS").value = 8;
            }
        }
        SaveVtaPediMak();
    } else {
        alert(lstMsg[161]);
    }
}

/**Guarda y Autoriza la operacion */
function SavePediMak() {
    if (bolSaveVta) {
        if (VerificaCreditoPediMak() == "true" || identiPantallaMak == 2) {
            if (identiPantallaMak == 1) {
                //Se guardara confirmado 
                if (bolPermConfirmMak) {
                    document.getElementById("STATUS").value = 11;
                    SaveVtaPediMak();
                } else {
                    alert("No tienes permisos para confirmar.");
                }
            } else {
                if (d.getElementById("COT_BAN_CONV_COTI").value == 1 || d.getElementById("COT_BAN_CONV_COTI").value == 4) {
                    //Solo se cerrara la cotizacion
                    document.getElementById("STATUS").value = 5;
                    SaveVtaPediMak();
                } else {
                    //Se guardara abierto   
                    if (bolPermConfirmMak) {
                        document.getElementById("STATUS").value = 1;
                        SaveVtaPediMak();
                    } else {
                        alert("No tienes permisos para confirmar.");
                    }
                }
            }
        }
    } else {
        alert(lstMsg[161]);
    }

}

/**Cierra la operacion */
function CerrarPediMak() {
    if (bolSaveVta) {
        if (VerificaCreditoPediMak() == "true" || identiPantallaMak == 2) {
            document.getElementById("STATUS").value = 5;

            var objSecModiVta = objMap.getScreen("MOT_CIEMAK");
            if (objSecModiVta != null) {
                objSecModiVta.bolActivo = false;
                objSecModiVta.bolMain = false;
                objSecModiVta.bolInit = false;
                objSecModiVta.idOperAct = 0;
            }
            OpnOpt('MOT_CIEMAK', '_ed', 'dlgMakBusqProd', false, false, true);
        }
    } else {
        alert(lstMsg[161]);
    }
}

/**Guarda la venta*/
function SaveVtaDoPediMak() {
    $("#dialogPagos").dialog("close");
    $("#dialogWait").dialog("open");
    //Armamos el POST a enviar
    var strPOST = "";
    //Prefijos dependiendo del tipo de venta
    var strPrefijoMaster = "TKT";
    var strPrefijoDeta = "TKTD";
    var strKey = "TKT_ID";
    var strNomFormat = "TICKET";
    if (d.getElementById("FAC_TIPO").value == "1") {
//Factura
        strPrefijoMaster = "FAC";
        strPrefijoDeta = "FACD";
        strKey = "FAC_ID";
        strNomFormat = "FACTURA";
    }
    if (d.getElementById("FAC_TIPO").value == "3") {
//Pedido
        strPrefijoMaster = "PD";
        strPrefijoDeta = "PDD";
        strKey = "PD_ID";
        strNomFormat = "PEDIDO";
    }
    if (d.getElementById("FAC_TIPO").value == "5") {
//Cotizacion
        strPrefijoMaster = "COT";
        strPrefijoDeta = "COTD";
        strKey = "COT_ID";
        strNomFormat = "COTIZA";
    }

//Master
    strPOST += "SC_ID=" + d.getElementById("pd_bodega").value;
    strPOST += "&CT_ID=" + d.getElementById("FCT_ID").value;
    strPOST += "&VE_ID=" + d.getElementById("pd_vendedor").value;
    strPOST += "&PD_ID=" + d.getElementById("PD_ID").value;
    strPOST += "&COT_ID=" + d.getElementById("COT_ID").value;
    strPOST += "&" + strPrefijoMaster + "_ESSERV=0";
    strPOST += "&" + strPrefijoMaster + "_MONEDA=" + d.getElementById("FAC_MONEDA").value;
    strPOST += "&" + strPrefijoMaster + "_FECHA=" + d.getElementById("FAC_FECHA").value;
    strPOST += "&" + strPrefijoMaster + "_FECHA_ENTREGA=" + d.getElementById("pd_fechValidez").value;
    strPOST += "&" + strPrefijoMaster + "_FECHA_SURTIDO=" + d.getElementById("pd_fech2").value;
    if (identiPantallaMak == 2) {
        strPOST += "&" + strPrefijoMaster + "_FECHA_VALIDEZ=" + d.getElementById("pd_fechValidVer").value;
    }
    strPOST += "&" + strPrefijoMaster + "_PEDMANUAL=" + d.getElementById("pd_pdManual").value;
    strPOST += "&" + strPrefijoMaster + "_CONTRATO=" + d.getElementById("pd_contrato").value;
    strPOST += "&" + strPrefijoMaster + "_SOLICITANTE=" + d.getElementById("pd_nomSolicit").value;
    if (d.getElementById("COT_BAN_CONV_COTI").value == 2) {
        strPOST += "&" + strPrefijoMaster + "_COT_ORIG=" + d.getElementById("COT_ID_PEDIDO").value;
    } else {
        strPOST += "&" + strPrefijoMaster + "_COT_ORIG=" + d.getElementById("pd_cotizaOrigen").value;
    }
    strPOST += "&" + strPrefijoMaster + "_OC_CLIENTE=" + d.getElementById("pd_ocCliente").value;
    //19/06/2013
    //Zeus Galindo
    //Evaluamos si es la modificación de un pedido para mantener el folio
    if (d.getElementById("FAC_TIPO").value == "3") {
        if (parseFloat(d.getElementById("PD_ID").value) > 0) {
            strPOST += "&" + strPrefijoMaster + "_FOLIO=" + d.getElementById("FAC_FOLIO").value;
        } else {
            strPOST += "&" + strPrefijoMaster + "_FOLIO=" /*+ d.getElementById("FAC_FOLIO").value*/;
        }
    } else {
        //Validamos que es una edicion de cotizacion
        if (d.getElementById("FAC_TIPO").value == "5") {
            if (parseFloat(d.getElementById("COT_ID").value) > 0) {
                strPOST += "&" + strPrefijoMaster + "_FOLIO=" + d.getElementById("FAC_FOLIO").value;
            } else {
                strPOST += "&" + strPrefijoMaster + "_FOLIO=" /*+ d.getElementById("FAC_FOLIO").value*/;
            }
        } else {
            strPOST += "&" + strPrefijoMaster + "_FOLIO=" /*+ d.getElementById("FAC_FOLIO").value*/;
        }
    }
    strPOST += "&" + strPrefijoMaster + "_NOTAS=" + encodeURIComponent(d.getElementById("FAC_NOTAS").value);
    strPOST += "&" + strPrefijoMaster + "_TOTAL=" + d.getElementById("FAC_TOT").value.replace(",", "");
    strPOST += "&" + strPrefijoMaster + "_IMPUESTO1=" + d.getElementById("FAC_IMPUESTO1").value.replace(",", "");
    strPOST += "&" + strPrefijoMaster + "_IMPUESTO2=" + d.getElementById("FAC_IMPUESTO2").value;
    strPOST += "&" + strPrefijoMaster + "_IMPUESTO3=" + d.getElementById("FAC_IMPUESTO3").value;
    strPOST += "&" + strPrefijoMaster + "_IMPORTE=" + d.getElementById("FAC_IMPORTE").value.replace(",", "");
    strPOST += "&" + strPrefijoMaster + "_RETISR=" + d.getElementById("FAC_RETISR").value;
    strPOST += "&" + strPrefijoMaster + "_RETIVA=" + d.getElementById("FAC_RETIVA").value;
    strPOST += "&" + strPrefijoMaster + "_NETO=" + d.getElementById("FAC_NETO").value;
    strPOST += "&" + strPrefijoMaster + "_NOTASPIE=" + encodeURIComponent(d.getElementById("FAC_NOTASPIE").value);
    strPOST += "&" + strPrefijoMaster + "_REFERENCIA=" + d.getElementById("FAC_REFERENCIA").value;
    strPOST += "&" + strPrefijoMaster + "_CONDPAGO=" + d.getElementById("FAC_CONDPAGO").value;
    strPOST += "&" + strPrefijoMaster + "_METODOPAGO=" + d.getElementById("FAC_METODOPAGO").value;
    strPOST += "&" + strPrefijoMaster + "_NUMCUENTA=" + d.getElementById("FAC_NUMCUENTA").value;
    strPOST += "&" + strPrefijoMaster + "_FORMADEPAGO=" + d.getElementById("FAC_FORMADEPAGO").value;
    strPOST += "&" + strPrefijoMaster + "_NUMPEDI=" + d.getElementById("FAC_NUMPEDI").value;
    strPOST += "&" + strPrefijoMaster + "_FECHAPEDI=" + d.getElementById("FAC_FECHAPEDI").value;
    strPOST += "&" + strPrefijoMaster + "_ADUANA=" + d.getElementById("FAC_ADUANA").value;
    strPOST += "&" + strPrefijoMaster + "_TIPOCOMP=" + d.getElementById("FAC_TIPOCOMP").value;
    strPOST += "&TIPOVENTA=" + d.getElementById("FAC_TIPO").value;
    strPOST += "&" + strPrefijoMaster + "_TASA1=" + dblTasaVta1;
    strPOST += "&" + strPrefijoMaster + "_TASA2=" + dblTasaVta2;
    strPOST += "&" + strPrefijoMaster + "_TASA3=" + dblTasaVta3;
    strPOST += "&" + "TI_ID=" + intIdTasaVta1;
    strPOST += "&" + "TI_ID2=" + intIdTasaVta2;
    strPOST += "&" + "TI_ID3=" + intIdTasaVta3;
    strPOST += "&" + strPrefijoMaster + "_TASAPESO=" + d.getElementById("pd_tipoCamCantidad").value;
    strPOST += "&" + strPrefijoMaster + "_DIASCREDITO=" + d.getElementById("FAC_DIASCREDITO").value;
    /*Obtenemos el valor del descuento*/
    var intDescIdx = d.getElementById("FCT_DESCUENTO").selectedIndex;
    var dblDesc = 0;
    if (intDescIdx > 0) {
        dblDesc = parseFloat(d.getElementById("FCT_DESCUENTO").options[intDescIdx].text);
    }
    strPOST += "&" + strPrefijoMaster + "_POR_DESC=" + dblDesc;
    strPOST += "&" + "STATUS=" + d.getElementById("STATUS").value;
    if (identiPantallaMak == 1) {
        strPOST += "&" + strPrefijoMaster + "_SE_FACTURA_LOGISTICA=" + d.getElementById("PD_CONV_FACTURA").value;
    }
    if (identiPantallaMak == 2) {
        strPOST += "&" + "COT_IDMOTIVO=" + d.getElementById("COT_IDMOTIVO").value;
        strPOST += "&" + "COT_IDDESC=" + d.getElementById("COT_IDDESC").value;
    }

    //TRASPORTE Y FLETE
    strPOST += "&TR_ID=" + d.getElementById("TR_ID").value;
    strPOST += "&ME_ID=" + d.getElementById("ME_ID").value;
    strPOST += "&TF_ID=" + d.getElementById("TF_ID").value;
    if (d.getElementById("CT_DIRENTREGA") != null) {
        if (d.getElementById("CT_DIRENTREGA").value != "0" && d.getElementById("CT_DIRENTREGA").value != "") {
            strPOST += "&CDE_ID=" + d.getElementById("CT_DIRENTREGA").value;
        }
    }
    if (d.getElementById("CT_CLIENTEFINAL") != null) {
        if (d.getElementById("CT_CLIENTEFINAL").value != "0" && d.getElementById("CT_CLIENTEFINAL").value != "") {
            strPOST += "&DFA_ID=" + d.getElementById("CT_CLIENTEFINAL").value;
        }
    }
    if (d.getElementById("SYC_ID") != null) {
        strPOST += "&SYC_ID=" + d.getElementById("SYC_ID").value;
    }
    strPOST += "&" + strPrefijoMaster + "_NUM_GUIA=" + d.getElementById("FAC_NUM_GUIA").value;
    //MLM
    strPOST += "&" + strPrefijoMaster + "_PUNTOS=" + d.getElementById("FAC_PUNTOS").value;
    strPOST += "&" + strPrefijoMaster + "_NEGOCIO=" + d.getElementById("FAC_NEGOCIO").value;
    //Validamos IEPS
    if (document.getElementById("FAC_USO_IEPS1").checked) {
        strPOST += "&" + strPrefijoMaster + "_USO_IEPS=1";
        strPOST += "&" + strPrefijoMaster + "_TASA_IEPS=" + d.getElementById("FAC_TASA_IEPS").value;
        strPOST += "&" + strPrefijoMaster + "_IMPORTE_IEPS=" + d.getElementById("FAC_IMPORTE_IEPS").value;
    } else {
        strPOST += "&" + strPrefijoMaster + "_USO_IEPS=0";
        strPOST += "&" + strPrefijoMaster + "_TASA_IEPS=0";
        strPOST += "&" + strPrefijoMaster + "_IMPORTE_IEPS=0";
    }
//Validamos _CONSIGNACION
    if (document.getElementById("FAC_CONSIGNACION1") != null) {
        if (document.getElementById("FAC_CONSIGNACION1").checked) {
            strPOST += "&" + strPrefijoMaster + "_CONSIGNACION=1";
        } else {
            strPOST += "&" + strPrefijoMaster + "_CONSIGNACION=0";
        }
    } else {
        strPOST += "&" + strPrefijoMaster + "_CONSIGNACION=0";
    }
//Datos Flete   
    if (document.getElementById("modlgn-flete1").checked == true) {
        strPOST += "&" + strPrefijoMaster + "_TIENE_FLETE=" + d.getElementById("FAC_TIENE_FLETE").value;
        strPOST += "&" + strPrefijoMaster + "_TIENE_SEGURO=" + d.getElementById("FAC_TIENE_SEGURO").value;
        strPOST += "&" + strPrefijoMaster + "_MONTO_SEGURO=" + d.getElementById("FAC_MONTO_SEGURO").value;
        strPOST += "&" + strPrefijoMaster + "ME_ID=" + d.getElementById("ME_ID").value;
        strPOST += "&" + strPrefijoMaster + "_TIPO_FLETE=" + d.getElementById("FAC_TIPO_FLETE").value;
    } else {
        strPOST += "&" + strPrefijoMaster + "_TIENE_FLETE=0";
        strPOST += "&" + strPrefijoMaster + "_TIENE_SEGURO=0";
        strPOST += "&" + strPrefijoMaster + "_MONTO_SEGURO=0";
        strPOST += "&" + strPrefijoMaster + "ME_ID=0";
        strPOST += "&" + strPrefijoMaster + "_TIPO_FLETE=0";

    }

//Agregamos campos ADDENDA MABE
    if (d.getElementById("ADD_MABE").value != undefined) {
        if (d.getElementById("ADD_MABE").value != 0) {
            if (d.getElementById("USA_MABE1").checked) {
                strPOST += "&ADD_MABE=1";
                strPOST += "&MB_CODIGOPROVEEDOR=" + d.getElementById("MB_CODIGOPROVEEDOR").value;
                strPOST += "&MB_PLANTA=" + d.getElementById("MB_PLANTA").value;
                strPOST += "&MB_CALLE=" + d.getElementById("MB_CALLE").value;
                strPOST += "&MB_NO_EXT=" + d.getElementById("MB_NO_EXT").value;
                strPOST += "&MB_NO_INT=" + d.getElementById("MB_NO_INT").value;
                strPOST += "&MB_ORDENCOMPRA=" + d.getElementById("MB_ORDENCOMPRA").value;
                strPOST += "&MB_REFERENCIA1=" + d.getElementById("MB_REFERENCIA1").value;
            }
        }
    } else {
        strPOST += "&ADD_MABE=0";
    }

//Agregamos campos ADDENDA SANOFI ORDEN DE COMPRA
    if (d.getElementById("ADD_SANOFI") != null) {
        if (d.getElementById("ADD_SANOFI").value != undefined) {
            if (d.getElementById("ADD_SANOFI").value != 0) {
                if (d.getElementById("USA_SANOFI1").checked) {
                    if (d.getElementById("SNF_ODC_SP1").checked) {
                        strPOST += "&ADD_SANOFI=1";
                        strPOST += "&USA_SANOFI1=1";
                        strPOST += "&SNF_NUM_PROV=" + d.getElementById("SNF_NUM_PROV").value;
                        strPOST += "&SNF_NUM_ODC=" + d.getElementById("SNF_NUM_ODC").value;
                        strPOST += "&SNF_NUM_SOL=" + d.getElementById("SNF_NUM_SOL").value;
                    }
                    if (d.getElementById("SNF_ODC_SP2").checked) {
                        strPOST += "&ADD_SANOFI=1";
                        strPOST += "&USA_SANOFI2=1";
                        strPOST += "&SNF_NUM_PROV=" + d.getElementById("SNF_NUM_PROV").value;
                        strPOST += "&SNF_NUM_SOL=" + d.getElementById("SNF_NUM_SOL").value;
                        strPOST += "&SNF_CUENTA_PUENTE=" + d.getElementById("SNF_CUENTA_PUENTE").value;
                    }
                }
            }
        } else {
            strPOST += "&ADD_SANOFI=0";
        }
    } else {
        strPOST += "&ADD_SANOFI=0";
    }


//Agregamos campos ADDENDA AMECE
    if (d.getElementById("ADD_AMECE") != null) {
        if (d.getElementById("ADD_AMECE").value != undefined) {
            if (d.getElementById("ADD_AMECE").value != 0) {
                if (d.getElementById("USA_AMECE1").checked) {
                    strPOST += "&ADD_AMECE=1";
                    strPOST += "&HM_DIV=" + d.getElementById("HM_DIV").value;
                    strPOST += "&HM_SOC=" + d.getElementById("HM_SOC").value;
                    strPOST += "&HM_ON=" + d.getElementById("HM_ON").value;
                    strPOST += "&HM_REFERENCEDATE=" + d.getElementById("HM_REFERENCEDATE").value;
                    strPOST += "&HM_SELLER_ASSIGNED_IDENTIFIER_FOR_A_PARTY=" + d.getElementById("HM_SELLER_ASSIGNED_IDENTIFIER_FOR_A_PARTY").value;
                    strPOST += "&HM_NAME=" + d.getElementById("HM_NAME").value;
                    strPOST += "&HM_STREET=" + d.getElementById("HM_STREET").value;
                    strPOST += "&HM_CITY=" + d.getElementById("HM_CITY").value;
                    strPOST += "&HM_POSTALCODE=" + d.getElementById("HM_POSTALCODE").value;
                }
            }
        } else {
            strPOST += "&ADD_AMECE=0";
        }
    } else {
        strPOST += "&ADD_AMECE=0";
    }
//Agregamos campos ADDENDA FEMSA
    if (d.getElementById("ADD_FEMSA") != null) {
        if (d.getElementById("ADD_FEMSA").value != undefined) {
            if (d.getElementById("ADD_FEMSA").value != 0) {
                if (d.getElementById("USA_FEMSA1").checked) {
                    strPOST += "&ADD_FEMSA=1";
                    strPOST += "&FEM_TIPO=" + d.getElementById("FEM_TIPO").value;
                    strPOST += "&FEM_SOC=" + d.getElementById("FEM_SOC").value;
                    strPOST += "&FEM_NUM_PROV=" + d.getElementById("FEM_NUM_PROV").value;
                    strPOST += "&FEM_NUM_PED=" + d.getElementById("FEM_NUM_PED").value;
                    strPOST += "&FEM_MONEDA=" + d.getElementById("FEM_MONEDA").value;
                    strPOST += "&FEM_NUM_ENTR_SAP=" + d.getElementById("FEM_NUM_ENTR_SAP").value;
                    strPOST += "&FEM_NUM_REMI=" + d.getElementById("FEM_NUM_REMI").value;
                    strPOST += "&FEM_RET=" + d.getElementById("FEM_RET").value;
                    strPOST += "&FEM_CORREO=" + d.getElementById("FEM_CORREO").value;
                }
            }
        } else {
            strPOST += "&ADD_FEMSA=0";
        }
    } else {
        strPOST += "&ADD_FEMSA=0";
    }
//Recurrentes
    if (d.getElementById("FAC_ESRECU1").checked) {
        strPOST += "&" + strPrefijoMaster + "_ESRECU=1";
    } else {
        strPOST += "&" + strPrefijoMaster + "_ESRECU=0";
    }
    strPOST += "&" + strPrefijoMaster + "_PERIODICIDAD=" + d.getElementById("FAC_PERIODICIDAD").value;
    strPOST += "&" + strPrefijoMaster + "_DIAPER=" + d.getElementById("FAC_DIAPER").value;
    strPOST += "&" + strPrefijoMaster + "_NO_EVENTOS=" + d.getElementById("FAC_NO_EVENTOS").value;
    //Validacion regimen fiscal
    if (document.getElementById("FAC_REGIMENFISCALcount") != null &&
            document.getElementById("FAC_REGIMENFISCALcount") != undefined) {
        var intCuantosReg = document.getElementById("FAC_REGIMENFISCALcount").value;
        if (intCuantosReg > 0) {
//Obtenemos el valor seleccionado
            for (var iRegim = 0; iRegim < intCuantosReg; iRegim++) {
                if (d.getElementById("FAC_REGIMENFISCAL" + iRegim).checked) {
                    strPOST += "&" + strPrefijoMaster + "_REGIMENFISCAL=" + d.getElementById("FAC_REGIMENFISCAL" + iRegim).value;
                }
            }
        }
    }
//Items productos
    var grid = jQuery("#FAC_GRID");
    var arr = grid.getDataIDs();
    var intC = 0;
    for (var i = 0; i < arr.length; i++) {
        var id = arr[i];
        var lstRow = grid.getRowData(id);
        intC++;
        strPOST += "&PR_ID" + intC + "=" + lstRow.FACD_PR_ID;
        strPOST += "&" + strPrefijoDeta + "_EXENTO1" + intC + "=" + lstRow.FACD_EXENTO1;
        strPOST += "&" + strPrefijoDeta + "_EXENTO2" + intC + "=" + lstRow.FACD_EXENTO2;
        strPOST += "&" + strPrefijoDeta + "_EXENTO3" + intC + "=" + lstRow.FACD_EXENTO3;
        strPOST += "&" + strPrefijoDeta + "_CVE" + intC + "=" + lstRow.FACD_CVE;
        strPOST += "&" + strPrefijoDeta + "_DESCRIPCION" + intC + "=" + encodeURIComponent(lstRow.FACD_DESCRIPCION);
        strPOST += "&" + strPrefijoDeta + "_CANTIDAD" + intC + "=" + lstRow.FACD_CANTIDAD;
        strPOST += "&" + strPrefijoDeta + "_RET_ISR" + intC + "=" + lstRow.FACD_RET_ISR;
        strPOST += "&" + strPrefijoDeta + "_RET_IVA" + intC + "=" + lstRow.FACD_RET_IVA;
        strPOST += "&" + strPrefijoDeta + "_RET_FLETE" + intC + "=" + lstRow.FACD_RET_FLETE;
        //MLM
        strPOST += "&" + strPrefijoDeta + "_IMP_PUNTOS" + intC + "=" + lstRow.FACD_PUNTOS;
        strPOST += "&" + strPrefijoDeta + "_IMP_VNEGOCIO" + intC + "=" + lstRow.FACD_NEGOCIO;
        strPOST += "&" + strPrefijoDeta + "_PUNTOS" + intC + "=" + lstRow.FACD_PUNTOS_U;
        strPOST += "&" + strPrefijoDeta + "_VNEGOCIO" + intC + "=" + lstRow.FACD_NEGOCIO_U;
        strPOST += "&" + strPrefijoDeta + "_DESC_PREC" + intC + "=" + lstRow.FACD_DESC_PREC;
        strPOST += "&" + strPrefijoDeta + "_DESC_PTO" + intC + "=" + lstRow.FACD_DESC_PTO;
        strPOST += "&" + strPrefijoDeta + "_DESC_VN" + intC + "=" + lstRow.FACD_DESC_VN;
        //      strPOST += "&" + strPrefijoDeta + "_DESC_LEAL" + intC + "=" + lstRow.FACD_DESC_LEAL;
        strPOST += "&" + strPrefijoDeta + "_DESC_ORI" + intC + "=" + lstRow.FACD_DESC_ORI;
        strPOST += "&" + strPrefijoDeta + "_REGALO" + intC + "=" + lstRow.FACD_REGALO;
        strPOST += "&" + strPrefijoDeta + "_ID_PROMO" + intC + "=" + lstRow.FACD_ID_PROMO;
        //MLM
        //Validamos si los precios incluyen o no impuestos para guardarlos incluyendo impuestos
        if (intPreciosconImp == 1) {
            strPOST += "&" + strPrefijoDeta + "_PRECIO" + intC + "=" + lstRow.FACD_PRECIO.replace(",", "");
            if (lstRow.FACD_SINPRECIO == 0) {
                strPOST += "&" + strPrefijoDeta + "_PRECREAL" + intC + "=" + lstRow.FACD_PRECREAL;
            } else {
                strPOST += "&" + strPrefijoDeta + "_PRECREAL" + intC + "=" + 0;
            }
        } else {
            var dblPrecioConImp = 0;
            var dblPrecioRealConImp = 0;
            if (lstRow.FACD_CANTIDAD > 0) {
//Calculamos el impuesto
                var dblBase1 = 0;
                var dblBase2 = 0;
                var dblBase3 = 0;
                var dblBaseReal1 = 0;
                var dblBaseReal2 = 0;
                var dblBaseReal3 = 0;
                var dblImpuesto1 = 0;
                var dblImpuesto2 = 0;
                var dblImpuesto3 = 0;
                var dblImpuestoReal1 = 0;
                var dblImpuestoReal2 = 0;
                var dblImpuestoReal3 = 0;
                if (parseInt(lstRow.FACD_EXENTO1) == 0)
                    dblBase1 = lstRow.FACD_PRECIO.replace(",", "");
                if (parseInt(lstRow.FACD_EXENTO2) == 0)
                    dblBase2 = lstRow.FACD_PRECIO.replace(",", "");
                if (parseInt(lstRow.FACD_EXENTO3) == 0)
                    dblBase3 = lstRow.FACD_PRECIO.replace(",", "");
                if (parseInt(lstRow.FACD_EXENTO1) == 0)
                    dblBaseReal1 = lstRow.FACD_PRECREAL;
                if (parseInt(lstRow.FACD_EXENTO2) == 0)
                    dblBaseReal2 = lstRow.FACD_PRECREAL;
                if (parseInt(lstRow.FACD_EXENTO3) == 0)
                    dblBaseReal3 = lstRow.FACD_PRECREAL;
                var tax = new Impuestos(dblTasaVta1, dblTasaVta2, dblTasaVta3, intSImpVta1_2, intSImpVta1_3, intSImpVta2_3); //Objeto para calculo de impuestos
                tax.CalculaImpuestoMas(dblBase1, dblBase2, dblBase3);
                var tax2 = new Impuestos(dblTasaVta1, dblTasaVta2, dblTasaVta3, intSImpVta1_2, intSImpVta1_3, intSImpVta2_3); //Objeto para calculo de impuestos
                tax2.CalculaImpuestoMas(dblBaseReal1, dblBaseReal2, dblBaseReal3);
                if (parseInt(lstRow.FACD_EXENTO1) == 0)
                    dblImpuesto1 = tax.dblImpuesto1;
                if (parseInt(lstRow.FACD_EXENTO2) == 0)
                    dblImpuesto2 = tax.dblImpuesto2;
                if (parseInt(lstRow.FACD_EXENTO3) == 0)
                    dblImpuesto3 = tax.dblImpuesto3;
                if (parseInt(lstRow.FACD_EXENTO1) == 0)
                    dblImpuestoReal1 = tax2.dblImpuesto1;
                if (parseInt(lstRow.FACD_EXENTO2) == 0)
                    dblImpuestoReal2 = tax2.dblImpuesto2;
                if (parseInt(lstRow.FACD_EXENTO3) == 0)
                    dblImpuestoReal3 = tax2.dblImpuesto3;
                dblPrecioConImp = (parseFloat(lstRow.FACD_PRECIO.replace(",", "")) +
                        dblImpuesto1 +
                        dblImpuesto2 +
                        dblImpuesto3);
                //Si se definio el precio
                if (lstRow.FACD_SINPRECIO == 0) {
                    dblPrecioRealConImp = (parseFloat(lstRow.FACD_PRECREAL) +
                            dblImpuestoReal1 +
                            dblImpuestoReal2 +
                            dblImpuestoReal3);
                } else {
//Se definio manualmente el precio
                    dblPrecioRealConImp = (parseFloat(lstRow.FACD_PRECIO.replace(",", "")) +
                            dblImpuesto1 +
                            dblImpuesto2 +
                            dblImpuesto3);
                    lstRow.FACD_IMPORTEREAL = dblPrecioRealConImp * lstRow.FACD_CANTIDAD;
                }
            } else {
                dblPrecioConImp = (parseFloat(lstRow.FACD_PRECIO.replace(",", "")));
                dblPrecioRealConImp = (parseFloat(lstRow.FACD_PRECREAL));
            }
            strPOST += "&" + strPrefijoDeta + "_PRECIO" + intC + "=" + dblPrecioConImp;
            if (lstRow.FACD_SINPRECIO == 0) {
                strPOST += "&" + strPrefijoDeta + "_PRECREAL" + intC + "=" + dblPrecioRealConImp;
            } else {
                strPOST += "&" + strPrefijoDeta + "_PRECREAL" + intC + "=" + 0;
            }
        }

        strPOST += "&" + strPrefijoDeta + "_IMPORTE" + intC + "=" + lstRow.FACD_IMPORTE.replace(",", "");
        strPOST += "&" + strPrefijoDeta + "_TASAIVA1" + intC + "=" + lstRow.FACD_TASAIVA1;
        strPOST += "&" + strPrefijoDeta + "_TASAIVA2" + intC + "=0" + lstRow.FACD_TASAIVA2;
        strPOST += "&" + strPrefijoDeta + "_TASAIVA3" + intC + "=" + lstRow.FACD_TASAIVA3;
        strPOST += "&" + strPrefijoDeta + "_IMPUESTO1" + intC + "=" + lstRow.FACD_IMPUESTO1;
        strPOST += "&" + strPrefijoDeta + "_IMPUESTO2" + intC + "=" + lstRow.FACD_IMPUESTO2;
        strPOST += "&" + strPrefijoDeta + "_IMPUESTO3" + intC + "=" + lstRow.FACD_IMPUESTO3;
        strPOST += "&" + strPrefijoDeta + "_ESREGALO" + intC + "=" + lstRow.FACD_ESREGALO;
        strPOST += "&" + strPrefijoDeta + "_NOSERIE" + intC + "=" + lstRow.FACD_NOSERIE;
        strPOST += "&" + strPrefijoDeta + "_IMPORTEREAL" + intC + "=" + lstRow.FACD_IMPORTEREAL;
        strPOST += "&" + strPrefijoDeta + "_DESCUENTO" + intC + "=" + lstRow.FACD_DESCUENTO.replace(",", "");
        strPOST += "&" + strPrefijoDeta + "_PORDESC" + intC + "=" + lstRow.FACD_PORDESC;
        strPOST += "&" + strPrefijoDeta + "_ESDEVO" + intC + "=" + lstRow.FACD_ESDEVO;
        strPOST += "&" + strPrefijoDeta + "_PRECFIJO" + intC + "=" + lstRow.FACD_PRECFIJO;
        strPOST += "&" + strPrefijoDeta + "_NOTAS" + intC + "=" + encodeURIComponent(lstRow.FACD_NOTAS);
        strPOST += "&" + strPrefijoDeta + "_UNIDAD_MEDIDA" + intC + "=" + lstRow.FACD_UNIDAD_MEDIDA;
        //BackOrder

        if (lstRow.FACD_DETALLE_BACKORDER != "") {
            strPOST += "&strbackorder" + intC + "=" + lstRow.FACD_DETALLE_BACKORDER;
        } else {
            strPOST += "&strbackorder" + intC + "=0";
        }
        if (lstRow.FACD_ES_BACKORDER != "") {
            strPOST += "&strEsbackorder" + intC + "=" + lstRow.FACD_ES_BACKORDER;
        } else {
            strPOST += "&strEsbackorder" + intC + "=0";
        }

        strPOST += "&strTipoBackorder" + intC + "=" + lstRow.FACD_TIPO_BACKORDER;
        //Requisicion
        if (lstRow.FACD_REQUISICION != "") {
            strPOST += "&dblRequisicion" + intC + "=" + lstRow.FACD_REQUISICION;
        } else {
            strPOST += "&dblRequisicion" + intC + "=0";
        }

        //[p] Agregamos nuevos campos para el manejo de los paquetes
        strPOST += "&" + strPrefijoDeta + "_ES_PAQUETE" + intC + "=" + lstRow.FACD_ES_PAQUETE;
        strPOST += "&" + strPrefijoDeta + "_ES_COMPONENTE" + intC + "=" + lstRow.FACD_ES_COMPONENTE;
        strPOST += "&" + strPrefijoDeta + "_PR_PAQUETE" + intC + "=" + lstRow.FACD_PR_PAQUETE;
        strPOST += "&" + strPrefijoDeta + "_MULTIPLO" + intC + "=" + lstRow.FACD_MULTIPLO;

        strPOST += "&" + strPrefijoDeta + "_CANTIDAD_IMPRIMIR" + intC + "=" + (parseInt(lstRow.FACD_CANTIDAD) - parseInt(lstRow.FACD_CANTIDAD_MULTIPLOS));
        strPOST += "&" + strPrefijoDeta + "_CANT_MULTIPLOS" + intC + "=" + lstRow.FACD_CANTIDAD_MULTIPLOS;
        strPOST += "&" + strPrefijoDeta + "_PRECIO_MULTIPLO" + intC + "=" + lstRow.FACD_PRECIO_MULTIPLOS.replace(",", "");
    }
    /*Recorremos otra vez el grid para sacar los multiplos*/
    for (var i = 0; i < arr.length; i++) {
        var id = arr[i];
        var lstRow = grid.getRowData(id);
        /*Agregamos los Multiplos*/
        if (lstRow.FACD_CANTIDAD_MULTIPLOS != 0) {
            intC++;
            strPOST += "&PR_ID" + intC + "=" + lstRow.FACD_PR_ID;
            strPOST += "&" + strPrefijoDeta + "_ES_MULTIPLO" + intC + "=1";
            strPOST += "&" + strPrefijoDeta + "_CANTIDAD" + intC + "=" + lstRow.FACD_CANTIDAD_MULTIPLOS;
            strPOST += "&" + strPrefijoDeta + "_PRECIO" + intC + "=" + lstRow.FACD_PRECIO_MULTIPLOS.replace(",", "");
            strPOST += "&" + strPrefijoDeta + "_IMPORTE" + intC + "=" + lstRow.FACD_PRECIO_TOT_MULTIPLOS.replace(",", "");
            strPOST += "&" + strPrefijoDeta + "_CANTIDAD_IMPRIMIR" + intC + "=" + lstRow.FACD_CANTIDAD_MULTIPLOS.replace(",", "");
            strPOST += "&" + strPrefijoDeta + "_ES_VIRTUAL" + intC + "=1";
            strPOST += "&" + strPrefijoDeta + "_CANT_MULTIPLOS" + intC + "=" + lstRow.FACD_CANTIDAD_MULTIPLOS;
            strPOST += "&" + strPrefijoDeta + "_CANT_CORTESIAS" + intC + "=0";
            strPOST += "&" + strPrefijoDeta + "_PRECIO_MULTIPLO" + intC + "=" + lstRow.FACD_PRECIO_MULTIPLOS.replace(",", "");

            strPOST += "&" + strPrefijoDeta + "_EXENTO1" + intC + "=" + lstRow.FACD_EXENTO1;
            strPOST += "&" + strPrefijoDeta + "_EXENTO2" + intC + "=" + lstRow.FACD_EXENTO2;
            strPOST += "&" + strPrefijoDeta + "_EXENTO3" + intC + "=" + lstRow.FACD_EXENTO3;
            strPOST += "&" + strPrefijoDeta + "_CVE" + intC + "=" + lstRow.FACD_CVE;
            strPOST += "&" + strPrefijoDeta + "_DESCRIPCION" + intC + "=" + encodeURIComponent(lstRow.FACD_DESCRIPCION);
            strPOST += "&" + strPrefijoDeta + "_RET_ISR" + intC + "=" + lstRow.FACD_RET_ISR;
            strPOST += "&" + strPrefijoDeta + "_RET_IVA" + intC + "=" + lstRow.FACD_RET_IVA;
            strPOST += "&" + strPrefijoDeta + "_RET_FLETE" + intC + "=" + lstRow.FACD_RET_FLETE;
            strPOST += "&" + strPrefijoDeta + "_IMP_PUNTOS" + intC + "=" + lstRow.FACD_PUNTOS;
            strPOST += "&" + strPrefijoDeta + "_IMP_VNEGOCIO" + intC + "=" + lstRow.FACD_NEGOCIO;
            strPOST += "&" + strPrefijoDeta + "_PUNTOS" + intC + "=" + lstRow.FACD_PUNTOS_U;
            strPOST += "&" + strPrefijoDeta + "_VNEGOCIO" + intC + "=" + lstRow.FACD_NEGOCIO_U;
            strPOST += "&" + strPrefijoDeta + "_DESC_PREC" + intC + "=" + lstRow.FACD_DESC_PREC;
            strPOST += "&" + strPrefijoDeta + "_DESC_PTO" + intC + "=" + lstRow.FACD_DESC_PTO;
            strPOST += "&" + strPrefijoDeta + "_DESC_VN" + intC + "=" + lstRow.FACD_DESC_VN;
            strPOST += "&" + strPrefijoDeta + "_DESC_ORI" + intC + "=" + lstRow.FACD_DESC_ORI;
            strPOST += "&" + strPrefijoDeta + "_REGALO" + intC + "=" + lstRow.FACD_REGALO;
            strPOST += "&" + strPrefijoDeta + "_ID_PROMO" + intC + "=" + lstRow.FACD_ID_PROMO;
            //MLM
            //Validamos si los precios incluyen o no impuestos para guardarlos incluyendo impuestos
            if (intPreciosconImp == 1) {
                strPOST += "&" + strPrefijoDeta + "_PRECIO" + intC + "=" + lstRow.FACD_PRECIO.replace(",", "");
                if (lstRow.FACD_SINPRECIO == 0) {
                    strPOST += "&" + strPrefijoDeta + "_PRECREAL" + intC + "=" + lstRow.FACD_PRECREAL;
                } else {
                    strPOST += "&" + strPrefijoDeta + "_PRECREAL" + intC + "=" + 0;
                }
            } else {
                var dblPrecioConImp = 0;
                var dblPrecioRealConImp = 0;
                if (lstRow.FACD_CANTIDAD > 0) {
//Calculamos el impuesto
                    var dblBase1 = 0;
                    var dblBase2 = 0;
                    var dblBase3 = 0;
                    var dblBaseReal1 = 0;
                    var dblBaseReal2 = 0;
                    var dblBaseReal3 = 0;
                    var dblImpuesto1 = 0;
                    var dblImpuesto2 = 0;
                    var dblImpuesto3 = 0;
                    var dblImpuestoReal1 = 0;
                    var dblImpuestoReal2 = 0;
                    var dblImpuestoReal3 = 0;
                    if (parseInt(lstRow.FACD_EXENTO1) == 0)
                        dblBase1 = lstRow.FACD_PRECIO.replace(",", "");
                    if (parseInt(lstRow.FACD_EXENTO2) == 0)
                        dblBase2 = lstRow.FACD_PRECIO.replace(",", "");
                    if (parseInt(lstRow.FACD_EXENTO3) == 0)
                        dblBase3 = lstRow.FACD_PRECIO.replace(",", "");
                    if (parseInt(lstRow.FACD_EXENTO1) == 0)
                        dblBaseReal1 = lstRow.FACD_PRECREAL;
                    if (parseInt(lstRow.FACD_EXENTO2) == 0)
                        dblBaseReal2 = lstRow.FACD_PRECREAL;
                    if (parseInt(lstRow.FACD_EXENTO3) == 0)
                        dblBaseReal3 = lstRow.FACD_PRECREAL;
                    var tax = new Impuestos(dblTasaVta1, dblTasaVta2, dblTasaVta3, intSImpVta1_2, intSImpVta1_3, intSImpVta2_3); //Objeto para calculo de impuestos
                    tax.CalculaImpuestoMas(dblBase1, dblBase2, dblBase3);
                    var tax2 = new Impuestos(dblTasaVta1, dblTasaVta2, dblTasaVta3, intSImpVta1_2, intSImpVta1_3, intSImpVta2_3); //Objeto para calculo de impuestos
                    tax2.CalculaImpuestoMas(dblBaseReal1, dblBaseReal2, dblBaseReal3);
                    if (parseInt(lstRow.FACD_EXENTO1) == 0)
                        dblImpuesto1 = tax.dblImpuesto1;
                    if (parseInt(lstRow.FACD_EXENTO2) == 0)
                        dblImpuesto2 = tax.dblImpuesto2;
                    if (parseInt(lstRow.FACD_EXENTO3) == 0)
                        dblImpuesto3 = tax.dblImpuesto3;
                    if (parseInt(lstRow.FACD_EXENTO1) == 0)
                        dblImpuestoReal1 = tax2.dblImpuesto1;
                    if (parseInt(lstRow.FACD_EXENTO2) == 0)
                        dblImpuestoReal2 = tax2.dblImpuesto2;
                    if (parseInt(lstRow.FACD_EXENTO3) == 0)
                        dblImpuestoReal3 = tax2.dblImpuesto3;
                    dblPrecioConImp = (parseFloat(lstRow.FACD_PRECIO.replace(",", "")) +
                            dblImpuesto1 +
                            dblImpuesto2 +
                            dblImpuesto3);
                    //Si se definio el precio
                    if (lstRow.FACD_SINPRECIO == 0) {
                        dblPrecioRealConImp = (parseFloat(lstRow.FACD_PRECREAL) +
                                dblImpuestoReal1 +
                                dblImpuestoReal2 +
                                dblImpuestoReal3);
                    } else {
//Se definio manualmente el precio
                        dblPrecioRealConImp = (parseFloat(lstRow.FACD_PRECIO.replace(",", "")) +
                                dblImpuesto1 +
                                dblImpuesto2 +
                                dblImpuesto3);
                        lstRow.FACD_IMPORTEREAL = dblPrecioRealConImp * lstRow.FACD_CANTIDAD;
                    }
                } else {
                    dblPrecioConImp = (parseFloat(lstRow.FACD_PRECIO.replace(",", "")));
                    dblPrecioRealConImp = (parseFloat(lstRow.FACD_PRECREAL));
                }
                strPOST += "&" + strPrefijoDeta + "_PRECIO" + intC + "=" + dblPrecioConImp;
                if (lstRow.FACD_SINPRECIO == 0) {
                    strPOST += "&" + strPrefijoDeta + "_PRECREAL" + intC + "=" + dblPrecioRealConImp;
                } else {
                    strPOST += "&" + strPrefijoDeta + "_PRECREAL" + intC + "=" + 0;
                }
            }
            strPOST += "&" + strPrefijoDeta + "_TASAIVA1" + intC + "=" + lstRow.FACD_TASAIVA1;
            strPOST += "&" + strPrefijoDeta + "_TASAIVA2" + intC + "=0" + lstRow.FACD_TASAIVA2;
            strPOST += "&" + strPrefijoDeta + "_TASAIVA3" + intC + "=" + lstRow.FACD_TASAIVA3;
            strPOST += "&" + strPrefijoDeta + "_IMPUESTO1" + intC + "=" + lstRow.FACD_IMPUESTO1;
            strPOST += "&" + strPrefijoDeta + "_IMPUESTO2" + intC + "=" + lstRow.FACD_IMPUESTO2;
            strPOST += "&" + strPrefijoDeta + "_IMPUESTO3" + intC + "=" + lstRow.FACD_IMPUESTO3;
            strPOST += "&" + strPrefijoDeta + "_ESREGALO" + intC + "=" + lstRow.FACD_ESREGALO;
            strPOST += "&" + strPrefijoDeta + "_NOSERIE" + intC + "=" + lstRow.FACD_NOSERIE;
            strPOST += "&" + strPrefijoDeta + "_IMPORTEREAL" + intC + "=" + lstRow.FACD_IMPORTEREAL;
            strPOST += "&" + strPrefijoDeta + "_DESCUENTO" + intC + "=" + lstRow.FACD_DESCUENTO.replace(",", "");
            strPOST += "&" + strPrefijoDeta + "_PORDESC" + intC + "=" + lstRow.FACD_PORDESC;
            strPOST += "&" + strPrefijoDeta + "_ESDEVO" + intC + "=" + lstRow.FACD_ESDEVO;
            strPOST += "&" + strPrefijoDeta + "_PRECFIJO" + intC + "=" + lstRow.FACD_PRECFIJO;
            strPOST += "&" + strPrefijoDeta + "_NOTAS" + intC + "=" + encodeURIComponent(lstRow.FACD_NOTAS);
            strPOST += "&" + strPrefijoDeta + "_UNIDAD_MEDIDA" + intC + "=" + lstRow.FACD_UNIDAD_MEDIDA;
            //[p] Agregamos nuevos campos para el manejo de los paquetes
            strPOST += "&" + strPrefijoDeta + "_ES_PAQUETE" + intC + "=0";
            strPOST += "&" + strPrefijoDeta + "_ES_COMPONENTE" + intC + "=0";
            strPOST += "&" + strPrefijoDeta + "_PR_PAQUETE" + intC + "=0";
            strPOST += "&" + strPrefijoDeta + "_MULTIPLO" + intC + "=0";
            strPOST += "&strbackorder" + intC + "=0";
            strPOST += "&strEsbackorder" + intC + "=0";
            strPOST += "&strTipoBackorder" + intC + "=";
            strPOST += "&dblRequisicion" + intC + "=0";

        }
    }

    /*Recorremos otra vez el grid para sacar los multiplos*/
    for (var i = 0; i < arr.length; i++) {
        var id = arr[i];
        var lstRow = grid.getRowData(id);
        /*Agregamos los productos de regalo los cuales no llevan importe*/
        if (lstRow.FACD_CORTESIA != 0) {
            intC++;
            strPOST += "&PR_ID" + intC + "=" + lstRow.FACD_PR_ID;
            strPOST += "&" + strPrefijoDeta + "_ES_CORTESIA" + intC + "=1";
            strPOST += "&" + strPrefijoDeta + "_CANTIDAD" + intC + "=" + lstRow.FACD_CORTESIA;
            strPOST += "&" + strPrefijoDeta + "_PRECIO" + intC + "=0";
            strPOST += "&" + strPrefijoDeta + "_IMPORTE" + intC + "=0";
            strPOST += "&" + strPrefijoDeta + "_CANTIDAD_IMPRIMIR" + intC + "=" + lstRow.FACD_CORTESIA.replace(",", "");
            strPOST += "&" + strPrefijoDeta + "_ES_VIRTUAL" + intC + "=1";
            strPOST += "&" + strPrefijoDeta + "_CANT_MULTIPLOS" + intC + "=0";
            strPOST += "&" + strPrefijoDeta + "_CANT_CORTESIAS" + intC + "=" + lstRow.FACD_CORTESIA.replace(",", "");
            strPOST += "&" + strPrefijoDeta + "_PRECIO_MULTIPLO" + intC + "=0";


            strPOST += "&" + strPrefijoDeta + "_EXENTO1" + intC + "=" + lstRow.FACD_EXENTO1;
            strPOST += "&" + strPrefijoDeta + "_EXENTO2" + intC + "=" + lstRow.FACD_EXENTO2;
            strPOST += "&" + strPrefijoDeta + "_EXENTO3" + intC + "=" + lstRow.FACD_EXENTO3;
            strPOST += "&" + strPrefijoDeta + "_CVE" + intC + "=" + lstRow.FACD_CVE;
            strPOST += "&" + strPrefijoDeta + "_DESCRIPCION" + intC + "=" + encodeURIComponent(lstRow.FACD_DESCRIPCION);
            strPOST += "&" + strPrefijoDeta + "_RET_ISR" + intC + "=" + lstRow.FACD_RET_ISR;
            strPOST += "&" + strPrefijoDeta + "_RET_IVA" + intC + "=" + lstRow.FACD_RET_IVA;
            strPOST += "&" + strPrefijoDeta + "_RET_FLETE" + intC + "=" + lstRow.FACD_RET_FLETE;
            strPOST += "&" + strPrefijoDeta + "_IMP_PUNTOS" + intC + "=" + lstRow.FACD_PUNTOS;
            strPOST += "&" + strPrefijoDeta + "_IMP_VNEGOCIO" + intC + "=" + lstRow.FACD_NEGOCIO;
            strPOST += "&" + strPrefijoDeta + "_PUNTOS" + intC + "=" + lstRow.FACD_PUNTOS_U;
            strPOST += "&" + strPrefijoDeta + "_VNEGOCIO" + intC + "=" + lstRow.FACD_NEGOCIO_U;
            strPOST += "&" + strPrefijoDeta + "_DESC_PREC" + intC + "=" + lstRow.FACD_DESC_PREC;
            strPOST += "&" + strPrefijoDeta + "_DESC_PTO" + intC + "=" + lstRow.FACD_DESC_PTO;
            strPOST += "&" + strPrefijoDeta + "_DESC_VN" + intC + "=" + lstRow.FACD_DESC_VN;
            strPOST += "&" + strPrefijoDeta + "_DESC_ORI" + intC + "=" + lstRow.FACD_DESC_ORI;
            strPOST += "&" + strPrefijoDeta + "_REGALO" + intC + "=" + lstRow.FACD_REGALO;
            strPOST += "&" + strPrefijoDeta + "_ID_PROMO" + intC + "=" + lstRow.FACD_ID_PROMO;
            //MLM
            //Validamos si los precios incluyen o no impuestos para guardarlos incluyendo impuestos
            if (intPreciosconImp == 1) {
                strPOST += "&" + strPrefijoDeta + "_PRECIO" + intC + "=" + lstRow.FACD_PRECIO.replace(",", "");
                if (lstRow.FACD_SINPRECIO == 0) {
                    strPOST += "&" + strPrefijoDeta + "_PRECREAL" + intC + "=" + lstRow.FACD_PRECREAL;
                } else {
                    strPOST += "&" + strPrefijoDeta + "_PRECREAL" + intC + "=" + 0;
                }
            } else {
                var dblPrecioConImp = 0;
                var dblPrecioRealConImp = 0;
                if (lstRow.FACD_CANTIDAD > 0) {
//Calculamos el impuesto
                    var dblBase1 = 0;
                    var dblBase2 = 0;
                    var dblBase3 = 0;
                    var dblBaseReal1 = 0;
                    var dblBaseReal2 = 0;
                    var dblBaseReal3 = 0;
                    var dblImpuesto1 = 0;
                    var dblImpuesto2 = 0;
                    var dblImpuesto3 = 0;
                    var dblImpuestoReal1 = 0;
                    var dblImpuestoReal2 = 0;
                    var dblImpuestoReal3 = 0;
                    if (parseInt(lstRow.FACD_EXENTO1) == 0)
                        dblBase1 = lstRow.FACD_PRECIO.replace(",", "");
                    if (parseInt(lstRow.FACD_EXENTO2) == 0)
                        dblBase2 = lstRow.FACD_PRECIO.replace(",", "");
                    if (parseInt(lstRow.FACD_EXENTO3) == 0)
                        dblBase3 = lstRow.FACD_PRECIO.replace(",", "");
                    if (parseInt(lstRow.FACD_EXENTO1) == 0)
                        dblBaseReal1 = lstRow.FACD_PRECREAL;
                    if (parseInt(lstRow.FACD_EXENTO2) == 0)
                        dblBaseReal2 = lstRow.FACD_PRECREAL;
                    if (parseInt(lstRow.FACD_EXENTO3) == 0)
                        dblBaseReal3 = lstRow.FACD_PRECREAL;
                    var tax = new Impuestos(dblTasaVta1, dblTasaVta2, dblTasaVta3, intSImpVta1_2, intSImpVta1_3, intSImpVta2_3); //Objeto para calculo de impuestos
                    tax.CalculaImpuestoMas(dblBase1, dblBase2, dblBase3);
                    var tax2 = new Impuestos(dblTasaVta1, dblTasaVta2, dblTasaVta3, intSImpVta1_2, intSImpVta1_3, intSImpVta2_3); //Objeto para calculo de impuestos
                    tax2.CalculaImpuestoMas(dblBaseReal1, dblBaseReal2, dblBaseReal3);
                    if (parseInt(lstRow.FACD_EXENTO1) == 0)
                        dblImpuesto1 = tax.dblImpuesto1;
                    if (parseInt(lstRow.FACD_EXENTO2) == 0)
                        dblImpuesto2 = tax.dblImpuesto2;
                    if (parseInt(lstRow.FACD_EXENTO3) == 0)
                        dblImpuesto3 = tax.dblImpuesto3;
                    if (parseInt(lstRow.FACD_EXENTO1) == 0)
                        dblImpuestoReal1 = tax2.dblImpuesto1;
                    if (parseInt(lstRow.FACD_EXENTO2) == 0)
                        dblImpuestoReal2 = tax2.dblImpuesto2;
                    if (parseInt(lstRow.FACD_EXENTO3) == 0)
                        dblImpuestoReal3 = tax2.dblImpuesto3;
                    dblPrecioConImp = (parseFloat(lstRow.FACD_PRECIO.replace(",", "")) +
                            dblImpuesto1 +
                            dblImpuesto2 +
                            dblImpuesto3);
                    //Si se definio el precio
                    if (lstRow.FACD_SINPRECIO == 0) {
                        dblPrecioRealConImp = (parseFloat(lstRow.FACD_PRECREAL) +
                                dblImpuestoReal1 +
                                dblImpuestoReal2 +
                                dblImpuestoReal3);
                    } else {
//Se definio manualmente el precio
                        dblPrecioRealConImp = (parseFloat(lstRow.FACD_PRECIO.replace(",", "")) +
                                dblImpuesto1 +
                                dblImpuesto2 +
                                dblImpuesto3);
                        lstRow.FACD_IMPORTEREAL = dblPrecioRealConImp * lstRow.FACD_CANTIDAD;
                    }
                } else {
                    dblPrecioConImp = (parseFloat(lstRow.FACD_PRECIO.replace(",", "")));
                    dblPrecioRealConImp = (parseFloat(lstRow.FACD_PRECREAL));
                }
                strPOST += "&" + strPrefijoDeta + "_PRECIO" + intC + "=" + dblPrecioConImp;
                if (lstRow.FACD_SINPRECIO == 0) {
                    strPOST += "&" + strPrefijoDeta + "_PRECREAL" + intC + "=" + dblPrecioRealConImp;
                } else {
                    strPOST += "&" + strPrefijoDeta + "_PRECREAL" + intC + "=" + 0;
                }
            }
            strPOST += "&" + strPrefijoDeta + "_TASAIVA1" + intC + "=" + lstRow.FACD_TASAIVA1;
            strPOST += "&" + strPrefijoDeta + "_TASAIVA2" + intC + "=0" + lstRow.FACD_TASAIVA2;
            strPOST += "&" + strPrefijoDeta + "_TASAIVA3" + intC + "=" + lstRow.FACD_TASAIVA3;
            strPOST += "&" + strPrefijoDeta + "_IMPUESTO1" + intC + "=" + lstRow.FACD_IMPUESTO1;
            strPOST += "&" + strPrefijoDeta + "_IMPUESTO2" + intC + "=" + lstRow.FACD_IMPUESTO2;
            strPOST += "&" + strPrefijoDeta + "_IMPUESTO3" + intC + "=" + lstRow.FACD_IMPUESTO3;
            strPOST += "&" + strPrefijoDeta + "_ESREGALO" + intC + "=" + lstRow.FACD_ESREGALO;
            strPOST += "&" + strPrefijoDeta + "_NOSERIE" + intC + "=" + lstRow.FACD_NOSERIE;
            strPOST += "&" + strPrefijoDeta + "_IMPORTEREAL" + intC + "=" + lstRow.FACD_IMPORTEREAL;
            strPOST += "&" + strPrefijoDeta + "_DESCUENTO" + intC + "=" + lstRow.FACD_DESCUENTO.replace(",", "");
            strPOST += "&" + strPrefijoDeta + "_PORDESC" + intC + "=" + lstRow.FACD_PORDESC;
            strPOST += "&" + strPrefijoDeta + "_ESDEVO" + intC + "=" + lstRow.FACD_ESDEVO;
            strPOST += "&" + strPrefijoDeta + "_PRECFIJO" + intC + "=" + lstRow.FACD_PRECFIJO;
            strPOST += "&" + strPrefijoDeta + "_NOTAS" + intC + "=" + encodeURIComponent(lstRow.FACD_NOTAS);
            strPOST += "&" + strPrefijoDeta + "_UNIDAD_MEDIDA" + intC + "=" + lstRow.FACD_UNIDAD_MEDIDA;
            //[p] Agregamos nuevos campos para el manejo de los paquetes
            strPOST += "&" + strPrefijoDeta + "_ES_PAQUETE" + intC + "=0";
            strPOST += "&" + strPrefijoDeta + "_ES_COMPONENTE" + intC + "=0";
            strPOST += "&" + strPrefijoDeta + "_PR_PAQUETE" + intC + "=0";
            strPOST += "&" + strPrefijoDeta + "_MULTIPLO" + intC + "=0";
            strPOST += "&strbackorder" + intC + "=0";
            strPOST += "&strEsbackorder" + intC + "=0";
            strPOST += "&strTipoBackorder" + intC + "=";
            strPOST += "&dblRequisicion" + intC + "=0";
        }
    }

    strPOST += "&COUNT_ITEM=" + intC;
    //Pagos Mandamos las 4 formas de pago
    //Validamos el tipo de venta
    if (document.getElementById("FAC_TIPO").value != "3"
            && document.getElementById("FAC_TIPO").value != "5"
            && parseFloat(document.getElementById("FAC_TOT").value.replace(",", "")) > 0
            && parseFloat(document.getElementById("FAC_DIASCREDITO").value) == 0) {
        strPOST += "&COUNT_PAGOS=5";
        //efectivo
        strPOST += "&MCD_MONEDA1=1";
        strPOST += "&MCD_FOLIO1=";
        strPOST += "&MCD_FORMAPAGO1=EFECTIVO";
        strPOST += "&MCD_NOCHEQUE1=";
        strPOST += "&MCD_BANCO1=";
        strPOST += "&MCD_NOTARJETA1=";
        strPOST += "&MCD_TIPOTARJETA1=";
        strPOST += "&MCD_IMPORTE1=" + (parseFloat(d.getElementById("Ef_1").value) - parseFloat(d.getElementById("Ef_2").value));
        strPOST += "&MCD_TASAPESO1=1";
        strPOST += "&MCD_CAMBIO1=" + d.getElementById("Ef_2").value;
        //cheque
        strPOST += "&MCD_MONEDA2=1";
        strPOST += "&MCD_FOLIO2=";
        strPOST += "&MCD_FORMAPAGO2=CHEQUE";
        strPOST += "&MCD_NOCHEQUE2=" + d.getElementById("Bc_2").value;
        strPOST += "&MCD_BANCO2=" + d.getElementById("Bc_3").value;
        strPOST += "&MCD_NOTARJETA2=";
        strPOST += "&MCD_TIPOTARJETA2=";
        strPOST += "&MCD_IMPORTE2=" + d.getElementById("Bc_1").value;
        strPOST += "&MCD_TASAPESO2=1";
        strPOST += "&MCD_CAMBIO2=0";
        //tarjeta de credito
        strPOST += "&MCD_MONEDA3=1";
        strPOST += "&MCD_FOLIO3=";
        strPOST += "&MCD_FORMAPAGO3=TCREDITO";
        strPOST += "&MCD_NOCHEQUE3=";
        strPOST += "&MCD_BANCO3=";
        strPOST += "&MCD_NOTARJETA3=" + d.getElementById("Tj_2").value;
        strPOST += "&MCD_TIPOTARJETA3=" + d.getElementById("Tj_3").value;
        strPOST += "&MCD_IMPORTE3=" + d.getElementById("Tj_1").value;
        strPOST += "&MCD_TASAPESO3=1";
        strPOST += "&MCD_CAMBIO3=0";
        //saldo a favor
        strPOST += "&MCD_MONEDA4=1";
        strPOST += "&MCD_FOLIO4=";
        strPOST += "&MCD_FORMAPAGO4=SALDOFAVOR";
        strPOST += "&MCD_NOCHEQUE4=";
        strPOST += "&MCD_BANCO4=";
        strPOST += "&MCD_NOTARJETA4=";
        strPOST += "&MCD_TIPOTARJETA4=";
        strPOST += "&MCD_IMPORTE4=" + d.getElementById("sf_1").value;
        strPOST += "&MCD_TASAPESO4=1";
        strPOST += "&MCD_CAMBIO4=0";
        var grid1 = jQuery("#GRID_ANTIV");
        var lista = grid1.getGridParam("selarrrow");
        var dblTotalPagar = parseFloat(d.getElementById("sf_1").value);
        strPOST += "&MCD_NUMANTICIPOS=" + lista.length;
        for (i = 0; i < lista.length; i++) {
            var idlast = lista[i];
            var lstRow = grid1.getRowData(idlast);
            if (parseFloat(lstRow.ANTI_ABONO) >= dblTotalPagar)
            {
                strPOST += "&MCD_CANTUSAR" + (i + 1) + "=" + dblTotalPagar;
                strPOST += "&MCD_IDANTICIPO" + (i + 1) + "=" + lstRow.ANTI_ID;
                break;
            } else {
                strPOST += "&MCD_CANTUSAR" + (i + 1) + "=" + lstRow.ANTI_ABONO;
                strPOST += "&MCD_IDANTICIPO" + (i + 1) + "=" + lstRow.ANTI_ID
                dblTotalPagar = dblTotalPagar - parseFloat(lstRow.ANTI_ABONO);
            }
//lstRow.IMPD_MONTO_CARGO
        }
//Transferencia
        strPOST += "&MCD_MONEDA5=1";
        strPOST += "&MCD_FOLIO5=";
        strPOST += "&MCD_FORMAPAGO5=TRANSFERENCIA BANCARIA";
        strPOST += "&MCD_NOCHEQUE5=";
        strPOST += "&MCD_BANCO5=" + d.getElementById("tf_2").value;
        strPOST += "&MCD_NOTARJETA5=" + d.getElementById("tf_3").value;
        strPOST += "&MCD_TIPOTARJETA5=";
        strPOST += "&MCD_IMPORTE5=" + d.getElementById("tf_1").value;
        strPOST += "&MCD_TASAPESO5=1";
        strPOST += "&MCD_CAMBIO5=0";
    } else {
        strPOST += "&COUNT_PAGOS=1";
        //efectivo
        strPOST += "&MCD_MONEDA1=1";
        strPOST += "&MCD_FOLIO1=";
        strPOST += "&MCD_FORMAPAGO1=EFECTIVO";
        strPOST += "&MCD_NOCHEQUE1=";
        strPOST += "&MCD_BANCO1=";
        strPOST += "&MCD_NOTARJETA1=";
        strPOST += "&MCD_TIPOTARJETA1=";
        strPOST += "&MCD_IMPORTE1=0.0";
        strPOST += "&MCD_TASAPESO1=1";
        strPOST += "&MCD_CAMBIO1=0.0";
    }
//Hacemos la peticion por POST
    $.ajax({
        type: "POST",
        data: encodeURI(strPOST),
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "html",
        url: "VtasMov.do?id=1",
        success: function (dato) {
            dato = trim(dato);
            if (Left(dato, 3) == "OK.") {
                if (strNomFormat == "FACTURA") {
                    if (intImprimeTicket == 1) {
                        ImprimeconFolioTicket(dato.replace("OK.", ""));
                    } else {
                        var strHtml = CreaHidden(strKey, dato.replace("OK.", ""));
                        openWhereverFormat("ERP_SendInvoice?id=" + dato.replace("OK.", ""), strNomFormat, "PDF", strHtml);
                    }
                    ResetOperaActualPediMak();
                } else {
                    var bolImprimeDoc1 = true;
                    //Evaluamos si es un pedido recurrente para preguntar si
                    //desea generar la siguiente factura recurrente
                    if (document.getElementById("FAC_TIPO").value == 3) {
                        if (document.getElementById("FAC_ESRECU1") != null) {
                            if (document.getElementById("FAC_ESRECU1").checked) {
                                var aRec = confirm(lstMsg[182]);
                                if (aRec) {
                                    var aRec2 = confirm(lstMsg[194]);
                                    var strFechaUser = "";
                                    var intFechaUserSel = 0;
                                    if (aRec2) {
                                        var aRec3 = window.prompt(lstMsg[195], d.getElementById("FAC_FECHA").value);
                                        if (aRec3 != null && aRec3 != "") {
                                            strFechaUser = aRec3;
                                            intFechaUserSel = 1;
                                        }

                                    }
                                    var strFiltro = "&USA_FECHAUSER=" + intFechaUserSel + "&FAC_FECHA_US=" + strFechaUser;
                                    bolImprimeDoc1 = false;
                                    //Hacemos peticion por AJAX para generar las facturas
                                    $.ajax({
                                        type: "POST",
                                        data: encodeURI("LST_PD_ID=" + document.getElementById("PD_ID").value + strFiltro),
                                        scriptCharset: "utf-8",
                                        contentType: "application/x-www-form-urlencoded;charset=utf-8",
                                        cache: false,
                                        dataType: "html",
                                        url: "ERP_FacRecu.jsp?id=1",
                                        success: function (dato) {
                                            dato = trim(dato);
                                            if (Left(dato, 3) == "OK.") {
                                                alert(lstMsg[59] + " " + dato.replace("OK.", ""));
                                            } else {
                                                alert(dato);
                                            }
                                            ResetOperaActualPediMak();
                                            $("#dialogWait").dialog("close");
                                        },
                                        error: function (objeto, quepaso, otroobj) {
                                            alert(":pto9:" + objeto + " " + quepaso + " " + otroobj);
                                            $("#dialogWait").dialog("close");
                                        }
                                    });
                                }
                            }
                        }
                    }
                    //Si imprime el documento y reseteamos la operacion
                    if (bolImprimeDoc1) {
                        SaveMensajes(dato.replace("OK.", ""));
                        if (identiPantallaMak == 1 && document.getElementById("COT_BAN_CONV_COTI").value == 0) {
                            if (intImprimeTicket == 1) {
                                ImprimeconFolioTicket(dato.replace("OK.", ""));
                            } else {
                                idPedidoEditConsultMak = 0;
                                $("#SioNO1").dialog("open");
                                $("#SioNO1").dialog('option', 'title', "¿Desea Imprimir Formato con Imagenes?");
                                document.getElementById("btnSI1").onclick = function () {
                                    ImprimeconFolioImagenesPediMak(strKey, dato, strNomFormat);
                                    $("#SioNO1").dialog("close");
                                };
                                document.getElementById("btnNO1").onclick = function () {
                                    ImprimeconFolioPediMak(strKey, dato, strNomFormat);
                                    $("#SioNO1").dialog("close");
                                };
                            }
                        } else {
                            if (document.getElementById("COT_BAN_CONV_COTI").value == 0 || document.getElementById("COT_BAN_CONV_COTI").value == 4) {
                                $("#SioNO1").dialog("open");
                                $("#SioNO1").dialog('option', 'title', "¿Desea Imprimir Formato con Imagenes?");
                                document.getElementById("btnSI1").onclick = function () {
                                    ImprimeconFolioImagenesPediMak(strKey, dato, strNomFormat);
                                    $("#SioNO1").dialog("close");
                                };
                                document.getElementById("btnNO1").onclick = function () {
                                    ImprimeconFolioPediMak(strKey, dato, strNomFormat);
                                    $("#SioNO1").dialog("close");
                                };
                            } else {
                                if (document.getElementById("COT_BAN_CONV_COTI").value == 2) {
                                    d.getElementById("FAC_TIPO").value = 5;
                                    identiPantallaMak = 2;
                                    alert("Su cotizacion # " + document.getElementById("FAC_FOLIO").value + " se convirtio en el pedido # " + dato.replace("OK.", "") + "");
                                    locidCotizaEditMak = 0;
                                    Callbtn9PediMak();
                                } else {
                                    ConviertePediMak(dato.replace("OK.", ""));
//                                    $("#dialogWait").dialog("close");
                                }
                            }
                        }
                    }
                }
            } else {
                alert(dato);
            }
        },
        error: function (objeto, quepaso, otroobj) {
            alert(":pto9:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }
    });
}

function SaveMensajes(IdPedido) {
    var strSeEdita = 0
    if (typeof idPedidoEditConsultMak != "undefined") {
        if (idPedidoEditConsultMak != "" && idPedidoEditConsultMak != "0") {
            strSeEdita = 1;
        }
    }
    if (typeof idCotizaEditMak != "undefined") {
        if (idCotizaEditMak != "" && idCotizaEditMak != "0") {
            strSeEdita = 1;
        }
    }
    /*Si la bandera de convertir a pedido esta activada tambien se tienen que guardar los mensajes*/
    if (document.getElementById("COT_BAN_CONV_COTI").value == 2) {
        strSeEdita = 0;
    }
    if (strSeEdita == 0) {
        var intIdEnvio = 0;
        if (identiPantallaMak == 1) {
            intIdEnvio = 24;
        } else {
            intIdEnvio = 36;
        }
        var strPost = "";
        var gridMsj = jQuery("#GRD_MENSAJES");
        var arrMsj = gridMsj.getDataIDs();
        for (var i = 0; i < arrMsj.length; i++) {
            var id1 = arrMsj[i];
            var lstRowAct = gridMsj.getRowData(id1);
            strPost += "&MSJ_ID_PD" + i + "=" + lstRowAct.MSJG_ID_PD;
            strPost += "&MSJ_ID_USUARIO" + i + "=" + lstRowAct.MSJG_ID_USUARIO;
            strPost += "&MSJ_USUARIO" + i + "=" + lstRowAct.MSJG_USUARIO;
            strPost += "&MSJ_EMP_ID" + i + "=" + lstRowAct.MSJG_EMP_ID;
            strPost += "&MSJ_SC_ID" + i + "=" + lstRowAct.MSJG_SC_ID;
            strPost += "&MSJ_AREA" + i + "=" + lstRowAct.MSJG_AREA;
            strPost += "&MSJ_NOTA" + i + "=" + lstRowAct.MSJG_NOTA;
            strPost += "&MSJ_FECHA" + i + "=" + lstRowAct.MSJG_FECHA;
            strPost += "&MSJ_HORA" + i + "=" + lstRowAct.MSJG_HORA;
            strPost += "&MSJ_AREAID" + i + "=" + lstRowAct.MSJG_AREAID;
        }
        strPost += "&contador=" + arrMsj.length;
        strPost += "&IdPedido=" + IdPedido;
        $.ajax({
            type: "POST",
            data: strPost,
            scriptCharset: "utf-8",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "XML",
            url: "ERP_PedidosMakProcs.jsp?id=" + intIdEnvio,
            success: function (datos) {
                var objsc = datos.getElementsByTagName("vta_mensajes")[0];
                var lstProds = objsc.getElementsByTagName("vta_mensajes_deta");
//            $("#dialog").dialog("close");
//            $("#dialogWait").dialog("close");
            },
            error: function (objeto, quepaso, otroobj) {
                alert(":pto:" + objeto + " " + quepaso + " " + otroobj);
            }
        });
    }
}

function SaveHistorialPediMak() {
    var strPost = "";
    var gridMsj = jQuery("#GRD_MENSAJES");
    var arrMsj = gridMsj.getDataIDs();
    for (var i = 0; i < arrMsj.length; i++) {
        var id1 = arrMsj[i];
        var lstRowAct = gridMsj.getRowData(id1);
        strPost += "&MSJ_ID_PD" + i + "=" + lstRowAct.MSJG_ID_PD;
        strPost += "&MSJ_ID_USUARIO" + i + "=" + lstRowAct.MSJG_ID_USUARIO;
        strPost += "&MSJ_USUARIO" + i + "=" + lstRowAct.MSJG_USUARIO;
        strPost += "&MSJ_EMP_ID" + i + "=" + lstRowAct.MSJG_EMP_ID;
        strPost += "&MSJ_SC_ID" + i + "=" + lstRowAct.MSJG_SC_ID;
        strPost += "&MSJ_AREA" + i + "=" + lstRowAct.MSJG_AREA;
        strPost += "&MSJ_NOTA" + i + "=" + lstRowAct.MSJG_NOTA;
        strPost += "&MSJ_FECHA" + i + "=" + lstRowAct.MSJG_FECHA;
        strPost += "&MSJ_HORA" + i + "=" + lstRowAct.MSJG_HORA;
    }
    strPost += "&contador=" + arrMsj.length;
    $.ajax({
        type: "POST",
        data: strPost,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "XML",
        url: "ERP_PedidosMakProcs.jsp?id=24",
        success: function (datos) {
            var objsc = datos.getElementsByTagName("vta_mensajes")[0];
            var lstProds = objsc.getElementsByTagName("vta_mensajes_deta");
            for (var i = 0; i < lstProds.length; i++) {
                var obj = lstProds[i];
            }
            $("#dialog").dialog("close");
            $("#dialogWait").dialog("close");
        },
        error: function (objeto, quepaso, otroobj) {
            alert(":pto:" + objeto + " " + quepaso + " " + otroobj);
        }
    });
}

function ReiniciaPedidoPediMak() {

    if (document.getElementById("PD_ID").value == 0) {
        var strMensaje = "";
        if (identiPantallaMak == 1) {
            strMensaje = "pedido";
        } else {
            $("#dialogWait").dialog("close");
            strMensaje = "cotizacion";
        }
        $("#SioNO").dialog("open");
        $("#SioNO").dialog('option', 'title', "¿Realizar otro " + strMensaje + " ?");
        document.getElementById("btnSI").onclick = function () {
            ResetOperaActualPediMak();
            /*Obtenemos el consecutivo*/
            ObtieneNumPedidoPediMak();
            /*Obtenemos ola fecha cuando se creo o edito*/
            obtieneFechaPediMak();
            /*Pintamos el pop up de clientes*/
            PopupCtesPediMak();
            $("#SioNO").dialog("close");
        };
        document.getElementById("btnNO").onclick = function () {
            Callbtn9PediMak();
            $("#SioNO").dialog("close");
        };
    } else {
        Callbtn9PediMak();
    }
}

function ImprimeconFolioPediMak(strKey, dato, strNomFormat) {
    var strTipo = "1";
    if (d.getElementById("FAC_TIPO").value == "3") {
        strTipo = "2";
    }
    if (d.getElementById("FAC_TIPO").value == "4") {
        strTipo = "3";
    }
    if (d.getElementById("FAC_TIPO").value == "5") {
        strTipo = "5";
    }
    $.ajax({
        type: "POST",
        data: "KEY_ID=" + strKey + "&TYPE_ID=" + strTipo,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "VtasMov.do?id=12",
        success: function (datos) {
            var objsc = datos.getElementsByTagName("vta_folios")[0];
            var strFolioT = objsc.getAttribute('FOLIO');
            var strHtml2 = CreaHidden(strKey, dato.replace("OK.", ""));
            openFormat(strNomFormat, "PDF", strHtml2, strFolioT);
        },
        error: function (objeto, quepaso, otroobj) {
            alert(":ImprimeFolio:" + objeto + " " + quepaso + " " + otroobj);
        }
    });
    ReiniciaPedidoPediMak();
}

function ImprimeconFolioImagenesPediMak(strKey, dato, strNomFormat) {
    if (identiPantallaMak == 1) {
        Abrir_Link("JasperReport?REP_ID=89&boton_1=PDF&PD_ID=" + dato.replace("OK.", ""), '_reporte', 500, 600, 0, 0);
    } else {
        if (identiPantallaMak == 2) {
            Abrir_Link("JasperReport?REP_ID=103&boton_1=PDF&COT_ID=" + dato.replace("OK.", ""), '_reporte', 500, 600, 0, 0);
        }
    }
    ReiniciaPedidoPediMak();
}

/**Inicializa los impuestos por default*/
function InitImpDefaultPediMak() {
    dblTasaVta1 = dblTasa1;
    dblTasaVta2 = dblTasa2;
    dblTasaVta3 = dblTasa3;
    intSImpVta1_2 = intSImp1_2;
    intSImpVta1_3 = intSImp1_3;
    intSImpVta2_3 = intSImp2_3;
}

/**Inicializa los eventos*/
function InitSIoNoEventsPediMak() {
//Definimos acciones para el dialogo SI/NO
    document.getElementById("btnSI").onclick = function () {
        ConfirmaSIPediMak();
    };
    document.getElementById("btnNO").onclick = function () {
        ConfirmaNOPediMak();
    };
}

/**Funciones para el cuadro de dialogo SI/NO*/
function ConfirmaSIPediMak() {
    if (d.getElementById("Operac").value == "Nva") {
//Llamamos metodo para limpiar pantallas
        ResetOperaActualPediMak();
    }
    if (d.getElementById("Operac").value == "PORC_DESC") {
//Llamamos metodo para asignar el porcentaje de descuento
        PorcDescSendPediMak();
    }
    if (d.getElementById("Operac").value == "CHANGE_PRICE") {
//Llamamos metodo para cambiar el precio del articulo
        CambioPrecSendPediMak();
    }
    if (d.getElementById("Operac").value == "CHANGE_CANTPD") {
//Llamamos metodo para cambiar el precio del articulo
        VtaModificaCantDoPediMak();
    }
    d.getElementById("Operac").value = "";
    $("#SioNO").dialog("close");
}

/**Ejecuta la modificacion de la cantidad por recibir*/
function VtaModificaCantDoPediMak(idModi) {
    var grid = jQuery("#FAC_GRID");
//Actualiza la cantidad confirmadoa
    var lstRow = grid.getRowData(idModi);
    //Validamos si nos regreso un ID de producto valido
    if (lstRow.FACD_PR_ID != 0) {
        if (lstRow.FACD_CANTIDAD == "") {
            lstRow.FACD_CANTIDAD = 0;
            grid.setRowData(idModi, lstRow);
//            grid.trigger("reloadGrid");
        }
        addHistorialPediMak("2", d.getElementById("CANTIDADANTERIOR").value, lstRow.FACD_CANTIDAD, lstRow.FACD_CVE);
        //Validamos la dispponibilidad

        var dbldisponibilidad = 0.0;
        var strdisponibilidad = lstRow.FACD_DISPONIBLE.toString();

        if (strdisponibilidad.indexOf(",") == -1) {
            dbldisponibilidad = parseFloat(lstRow.FACD_DISPONIBLE);
        } else {
            dbldisponibilidad = parseFloat(strdisponibilidad.replace(",", ""));
        }

        if (lstRow.FACD_REQEXIST == 1 &&
                document.getElementById("FAC_TIPO").value != 5) {
//Obtenemos la existencia del producto
            $.ajax({
                type: "POST",
                data: "PR_ID=" + lstRow.FACD_PR_ID,
                scriptCharset: "utf-8",
                contentType: "application/x-www-form-urlencoded;charset=utf-8",
                cache: false,
                dataType: "html",
                url: "InvMov.do?id=1",
                success: function (datoExist) {
                    //YA NO SE VALIDARA LA EXISTENCIA SI NO LO DISPONIBLE
                    if (lstRow.FACD_ES_BACKORDER == 0 || lstRow.FACD_ES_BACKORDER == "") {
                        if (parseFloat(lstRow.FACD_CANTIDAD) > dbldisponibilidad) {
                            if (identiPantallaMak == 1) {
                                var objSecModiVta = objMap.getScreen("BACKORDER_MAK");
                                if (objSecModiVta != null) {
                                    objSecModiVta.bolActivo = false;
                                    objSecModiVta.bolMain = false;
                                    objSecModiVta.bolInit = false;
                                    objSecModiVta.idOperAct = 0;
                                }
                                lastSelBakOrder = idModi;
                                OpnOpt('BACKORDER_MAK', '_ed', 'dlgMakBackOrder', false, false, true);
                            } else {
                                lstRowChangePrecioPediMak(lstRow, idModi, grid);
                            }
                        } else {
                            if (lstRow.FACD_REQEXIST == 1) {
                                if (parseFloat(lstRow.FACD_CANTIDAD) > parseFloat(lstRow.FACD_EXIST)) {
                                    alert(lstMsg[3] + " " + lstRow.FACD_CVE + " " + lstMsg[4]);
                                    if (parseFloat(lstRow.FACD_EXIST) > 0) {
                                        lstRow.FACD_CANTIDAD = lstRow.FACD_EXIST;
                                    } else {
                                        lstRow.FACD_CANTIDAD = '';
                                    }
                                }
                            }
                            lstRowChangePrecioPediMak(lstRow, idModi, grid);
                        }
                    }
                },
                error: function (objeto, quepaso, otroobj) {
                    alert(":ptoExist:" + objeto + " " + quepaso + " " + otroobj);
                    $("#dialogWait").dialog("close");
                }
            });
        } else {
            lstRowChangePrecioPediMak(lstRow, idModi, grid);
        }
    }
}
function ConfirmaNOPediMak() {
    $("#SioNO").dialog("close");
}

/**Limpia la aplicacion de ofertas*/
function _ResetPromosAllPediMak() {
    if (intSucOfertas && bolCargaPromociones) {
        _ResetVarsAllPediMak();
    }
}

/**Resetea las promociones(en caso de nuevo o que quiten los productos)*/
function _ResetVarsAllPediMak() {
    _InitVarsPediMak();
    if (_debugPromos)
        _LogPromosPediMak("Reseteamos TODAS las promociones....");
    //Recorremos todas las variables
    for (var i = 0; i < lstVars.length; i++) {
        var objVar = lstVars[i];
        objVar.dblValor = 0;
        objVar.intValor = 0;
        objVar.strValor = "";
        objVar.boolValor = false;
        objVar.dateValor = null;
    }
    lstOfertasActivas = null;
    _intContaOfertas = -1;
    _drawOfertasPediMak();
    //Desactivamos la edicion del cliente
    var objCte = document.getElementById("FCT_ID");
    objCte.setAttribute("class", "outEdit");
    objCte.setAttribute("className", "outEdit");
    objCte.readOnly = false;
    objCte.disabled = false;
}

/**Inicializa el arreglo de variables*/
function _InitVarsPediMak() {
    bolSaveVta = true;
    if (lstVars == null) {
        lstVars = Array();
        if (_debugPromos)
            _LogPromosPediMak("Iniciamos arreglo....");
        var intContaPromos = -1;
        //Recorremos todas las variables y las cargamos para poderlas aplicar posteriormente
        var _lstPromo = objPromos.getElementsByTagName("promocion");
        for (var h = 0; h < _lstPromo.length; h++) {
            var objPromo = _lstPromo[h];
            var objVariables = objPromo.getElementsByTagName("variables")[0];
            var _lstVariables = objVariables.getElementsByTagName("variable");
            if (_debugPromos)
                _LogPromosPediMak(objPromo.getAttribute("PROM_NOMBRE") + "_lstVariables:" + _lstVariables.length);
            for (var i = 0; i < _lstVariables.length; i++) {
                intContaPromos++;
                var _objVariable = new _ClassVariable();
                _objVariable.intId = _lstVariables[i].getAttribute('PVAR_ID');
                _objVariable.strNombre = _lstVariables[i].getAttribute('PVAR_VARIABLE');
                _objVariable.intTipo = _lstVariables[i].getAttribute('PVAR_TIPO');
                _objVariable.intSec = _lstVariables[i].getAttribute('PVAR_SQL_CTE_PROD');
                _objVariable.intPromoId = objPromo.getAttribute('PROM_ID');
                _objVariable.strTipoDato = _lstVariables[i].getAttribute('PVAR_DATO');
                _objVariable.strScript = _lstVariables[i].getAttribute('PVAR_SCRIPT');
                lstVars[intContaPromos] = _objVariable;
            }
        }
    }
}
/**Imprime el log para debuguear las promociones*/
function _LogPromosPediMak(strMsg) {
    if (navigator.userAgent.indexOf("Firefox")) {
    } else {
        alert(strMsg);
    }
}

/**Muestra visualmente cuales ofertas estan aplicando..*/
function _drawOfertasPediMak() {
    if (lstOfertasActivas != null) {
//Desactivamos la edicion del cliente
        var objCte = document.getElementById("FCT_ID");
        objCte.setAttribute("class", "READONLY");
        objCte.setAttribute("className", "READONLY");
        objCte.readOnly = true;
        objCte.disabled = true;
        //Html del div
        var strHMTL = "<div id='Ofertas_activas'><img src='" + _urlImg1 + "' border='0' id='OfertasGift1' /></br>";
        var strLstOfActivas = "";
        for (var i = 0; i < lstOfertasActivas.length; i++) {
            var objOferta = lstOfertasActivas[i];
            strHMTL += "<div id='Oferta'>";
            strHMTL += objOferta.strNombre;
            strHMTL += "&nbsp;<span class= 'Desc_promo'>";
            strHMTL += objOferta.strDesc;
            strHMTL += "</span>";
            strHMTL += "</div>";
            strLstOfActivas += objOferta.intIdOferta + ",";
        }
        var strHMTL2 = "<div id='Ofertas_activas_list'>" + lstMsg[160] + "</div>" + strHMTL;
        strHMTL += "<input type='button' name='aplica_oferta' id='aplica_oferta' value='Continuar' onClick='_CierraDrawOfertas();'>";
        strHMTL2 += "<input type='hidden' name='lst_ofertas' id='lst_ofertas' value='" + strLstOfActivas + "' >";
        strHMTL += "</div>";
        strHMTL2 += "</div>";
//      $("#dialogPromociones").dialog('option', 'title', lstMsg[159]);
//      document.getElementById("dialogPromociones_inside").innerHTML = strHMTL;
        document.getElementById("PROMOS_LST").innerHTML = strHMTL2;
//      $("#dialogPromociones").dialog("open");
    } else {
        document.getElementById("PROMOS_LST").innerHTML = "&nbsp;";
    }

}

function MensajesPediMak() {
    OpnOpt('MENSAJES_MAK', '_ed', 'dlgMakMjs', false, false, true);
}

/**Nos salimos de la pantalla*/
function Callbtn9PediMak() {
    $("#dialogWait").dialog("close");
    myLayout.open("west");
    myLayout.open("east");
    myLayout.open("south");
    myLayout.open("north");
    document.getElementById("MainPanel").innerHTML = "";
    //Limpiamos el objeto en el framework para que nos deje cargarlo enseguida
    var objMainFacPedi = "";
    if (identiPantallaMak == 1) {
        objMainFacPedi = objMap.getScreen("PEDIDOS_MAK");
    } else {
        if (locidCotizaEditMak != "0") {
            objMainFacPedi = objMap.getScreen("COTIZACION_MAK");
        } else {
            objMainFacPedi = objMap.getScreen("COTIZACION_MAK");
        }
    }
    objMainFacPedi.bolActivo = false;
    objMainFacPedi.bolMain = false;
    objMainFacPedi.bolInit = false;
    objMainFacPedi.idOperAct = 0;
    idPedidoEditConsultMak = 0;
    idCotizaEditMak = 0;
    estatusCotizaEditMak = "";
    locidCotizaEditMak = 0;
    locestatusCotizaEditMak = "";
}//Borramos item

/**Pregunta si desea salir de la pantalla*/
function SalirScreenPediMak() {
    $("#dialogWait").dialog("close");
    $("#SioNO1").dialog("open");
    $("#SioNO1").dialog('option', 'title', "¿Seguro que quiere salir?");
    document.getElementById("btnSI1").onclick = function () {
        Callbtn9PediMak();
        $("#SioNO1").dialog("close");
    };
    document.getElementById("btnNO1").onclick = function () {
        $("#SioNO1").dialog("close");
    };
}

function DatosClientePediMak() {
    if (document.getElementById("FCT_ID").value != "" || document.getElementById("FCT_ID").value != "0") {
        var objSecModiVta = objMap.getScreen("CLIE_GEN_MAK");
        if (objSecModiVta != null) {
            objSecModiVta.bolActivo = false;
            objSecModiVta.bolMain = false;
            objSecModiVta.bolInit = false;
            objSecModiVta.idOperAct = 0;
        }
        OpnOpt('CLIE_GEN_MAK', '_ed', 'dialogCte', false, false, true);
    } else {
        alert("Seleccione un cliente");
    }
}

function ExistenciasPediMak() {
    var grid = jQuery("#FAC_GRID");
    var id = grid.getGridParam("selrow");
    var lstRow = grid.getRowData(id);
    if (grid.getGridParam("selrow") != null) {
        var objSecModiVta = objMap.getScreen("EXISTENCIAS_MAK");
        if (objSecModiVta != null) {
            objSecModiVta.bolActivo = false;
            objSecModiVta.bolMain = false;
            objSecModiVta.bolInit = false;
            objSecModiVta.idOperAct = 0;
        }
        d.getElementById("BandInformacion").value = "0";
        OpnOpt('EXISTENCIAS_MAK', '_ed', 'dlgMakExist', false, false, true);

    } else {
        alert("Selecciona un producto en la tabla");
    }
}

/**Abrimos ventana para indicar el nuevo precio del articulo*/
function ModificarPrecioPediMak() {
    if (bolPermCambiarPrecMak) {
        var grid = jQuery("#FAC_GRID");
        var ids = grid.getGridParam("selrow");
        if (ids != null) {
            var lstRow = grid.getRowData(ids);
            if (lstRow.FACD_ES_COMPONENTE == 1) {
                alert("El item no se puede modificar el precio porque es un componente de un paquete");
            } else {
                document.getElementById("Operac").value = "CHANGE_PRICE";
                $("#SioNO").dialog('option', 'title', lstMsg[7]);
                SavePrecAnt(lstRow.FACD_PRECIO.replace(",", ""));
                var div = document.getElementById("SioNO_inside");
                var strHtml = CreaTexto(lstMsg[8], "_NvoPrecio", lstRow.FACD_PRECIO.replace(",", ""), 10, 10, true, false, "", "left", 0, "", "", "", false, 1);
                strHtml += CreaRadio(lstMsg[9], "_NvoClean", 0, false, " ");
                div.innerHTML = strHtml;
                //Inicializamos eventos para el cuadro de dialogo SI/NO sino se puede sobreescribir con el de otra pantalla
                InitSIoNoEventsPediMak();
                $("#SioNO").dialog("open");
            }
        }
    } else {
        alert("No tiene permisos para cambiar los precios.");
    }
}

function SavePrecAnt(strPrecioAnterior) {

    if (document.getElementById("PRECIOANTERIOR").value != strPrecioAnterior) {
        document.getElementById("PRECIOANTERIOR").value = strPrecioAnterior;
    }

}

/**Aplica el porcentaje de descuento al producto*/
function PorcDescSendPediMak() {
    var grid = jQuery("#FAC_GRID");
    var ids = grid.getGridParam("selrow");
    if (ids != null) {
        var porDesc = d.getElementById("MiPordcDesc").value;
        //Calculamos el descuento
        PorcDescDoPediMak(ids, porDesc);
    }
}

/**Asigna el nuevo porcentaje de descuento seleccionado por el usuario*/
function PorcDescDoPediMak(id, dblPorc) {
    var grid = jQuery("#FAC_GRID");
    //Calculamos nuevo importe
    var lstRow = grid.getRowData(id);
    //Recalculamos el importe y actualizamos la fila
    lstRow.FACD_PORDESC = dblPorc;
    lstRowChangePrecioPediMak(lstRow, id, grid);
    //ponemos el foco para seguir capturando
    document.getElementById("FAC_PROD").focus();
}

/**Aplica el porcentaje de descuento al producto*/
function CambioPrecSendPediMak() {

    var grid = jQuery("#FAC_GRID");
    var ids = grid.getGridParam("selrow");
    if (ids != null) {
        var lstRow = grid.getRowData(ids);
        addHistorialPediMak("3", d.getElementById("PRECIOANTERIOR").value, d.getElementById("_NvoPrecio").value, lstRow.FACD_CVE);
        var dblNvoPrec = d.getElementById("_NvoPrecio").value;
        var bolClean = false;
        if (d.getElementById("_NvoClean1").checked)
            bolClean = true;
        //Calculamos el nuevo precio
        CambioPrecDoPediMak(ids, dblNvoPrec, bolClean);
    }
}

function CambioPrecDoPediMak(id, dblNvoPrec, bolClean) {
    var grid = jQuery("#FAC_GRID");
    //Calculamos nuevo importe
    var lstRow = grid.getRowData(id);
    //Recalculamos el importe y actualizamos la fila
    if (bolClean) {
        lstRow.FACD_PRECIO = lstRow.FACD_PRECREAL;
        lstRow.FACD_PRECFIJO = 0;
        lstRow.FACD_SINPRECIO = 0;
    } else {
        lstRow.FACD_PRECIO = dblNvoPrec;
        lstRow.FACD_PRECFIJO = 1;
        //El precio real siempre sera el que capture el usuario
        lstRow.FACD_PRECREAL = lstRow.FACD_PRECIO.replace(",", "");
        //Si el precio real es cero el nuevo precio sera el real
        if (lstRow.FACD_PRECREAL == 0) {
            lstRow.FACD_SINPRECIO = 1;
            lstRow.FACD_PRECREAL = lstRow.FACD_PRECIO.replace(",", "");
        }
    }
    lstRowChangePrecioPediMak(lstRow, id, grid);
    //ponemos el foco para seguir capturando
    document.getElementById("FAC_PROD").focus();
}

function LimpiaSubClientePediMak() {
    document.getElementById("cte_nomSubcte").value = "";
    document.getElementById("cte_rfcSubcte").value = "";
    document.getElementById("CT_CLIENTEFINAL").value = "0";
    document.getElementById("cte_direccionSubCliente").value = "";
    document.getElementById("cte_telSubcte").value = "";
}
function BorrarRowPediMak() {
    var grid = jQuery("#FAC_GRID");
    var _idDelVta = grid.getGridParam("selrow");
    if (_idDelVta != null) {
        var lstRow = grid.getRowData(_idDelVta);
        if (lstRow.FACD_ES_COMPONENTE == 1) {
            alert("El item no se puede borrar porque es un componente de un paquete");
        } else {
            if (lstRow.FACD_ES_PAQUETE == 1) {
                //Borramos las partidas
                borraComponentesPaquetePediMak(grid, _idDelVta);
            }
            grid.delRowData(_idDelVta);
            document.getElementById("FAC_PROD").focus();
            var arr = grid.getDataIDs();
            if (arr.length == 0) {
                document.getElementById("pd_bodega").disabled = false;
                document.getElementById("FCT_DESCUENTO").disabled = false;

                var bolDisable = false;
                if (identiPantallaMak == 1) {
                    if (bolPermModTipocambioPedido) {
                        bolDisable = true;
                    }
                } else {
                    if (identiPantallaMak == 2) {
                        if (bolPermModTipocambioCotizacion) {
                            bolDisable = true;
                        }
                    }
                }
                if (bolDisable) {
                    var inpCantidadTipCamb = document.getElementById("pd_tipoCamCantidad");
                    inpCantidadTipCamb.disabled = false;
                    inpCantidadTipCamb.style.backgroundColor = "";
                }
            }
            //Sumamos todos los items
            setSumPediMak();
        }
    }
}

function InformacionProductoPediMak() {
    var grid = jQuery("#FAC_GRID");
    if (grid.getGridParam("selrow") != null) {
        var objSecModiVta = objMap.getScreen("INFORMACION_MAK");
        if (objSecModiVta != null) {
            objSecModiVta.bolActivo = false;
            objSecModiVta.bolMain = false;
            objSecModiVta.bolInit = false;
            objSecModiVta.idOperAct = 0;
        }
        OpnOpt('INFORMACION_MAK', '_ed', 'dlgMakInformacion', false, false, true);
    } else {
        alert("Selecciona un producto en la tabla");
    }
}

function validaDatosPediMak() {
    if (document.getElementById("FAC_FECHA").value == "0" || document.getElementById("FAC_FECHA").value == "" || document.getElementById("FAC_FECHA").value == 0
            || document.getElementById("FAC_FECHA").value == "Seleccione") {
        alert("Capture la Fecha Pedido");
        return false;
    }
    if (document.getElementById("pd_fech2").value == "0" || document.getElementById("pd_fech2").value == "" || document.getElementById("pd_fech2").value == 0
            || document.getElementById("pd_fech2").value == "Seleccione") {
        alert("Capture la Fecha Pedido");
        return false;
    }
    if (document.getElementById("FAC_MONEDA").value == "0" || document.getElementById("FAC_MONEDA").value == "" || document.getElementById("FAC_MONEDA").value == 0
            || document.getElementById("FAC_MONEDA").value == "Seleccione") {
        alert("Capture la Moneda");
        return false;
    }
    if (document.getElementById("pd_bodega").value == "0" || document.getElementById("pd_bodega").value == "" || document.getElementById("pd_bodega").value == 0
            || document.getElementById("pd_bodega").value == "Seleccione") {
        alert("Capture la Bodega");
        return false;
    }
    if (document.getElementById("modlgn-flete1").checked == false &&
            document.getElementById("modlgn-flete2").checked == false) {
        alert("Capture el flete");
        return false;
    }
    if (document.getElementById("TR_ID").value == "0" || document.getElementById("TR_ID").value == "" || document.getElementById("TR_ID").value == 0
            || document.getElementById("TR_ID").value == "Seleccione") {
        alert("Capture el transporte");
        return false;
    }
    return true;
}

function addHistorialPediMak(strIdMovimiento, strValorAnterior, strValorNuevo, IdProducto) {
    var strPost = "";
    var strPD_ID = document.getElementById("FAC_FOLIO").value;
    strPost += "&strIdMovimiento=" + strIdMovimiento;
    strPost += "&strPD_ID=" + strPD_ID;
    strPost += "&strValorAnterior=" + strValorAnterior;
    strPost += "&strValorNuevo=" + strValorNuevo;
    strPost += "&strIdProducto=" + IdProducto;
    $.ajax({
        type: "POST",
        data: strPost,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "XML",
        url: "ERP_PedidosMakProcs.jsp?id=25",
        success: function (datos) {
            var objsc = datos.getElementsByTagName("vta_historial")[0];
            var lstProds = objsc.getElementsByTagName("vta_historial_deta");
            for (var i = 0; i < lstProds.length; i++) {
                var obj = lstProds[i];
                var datarowInfHisMak = {
                    GRDH_IDUSUARIO: obj.getAttribute("PEHIS_IDUSUARIO"),
                    GRDH_NOMBRE: obj.getAttribute("PEHIS_NOMBRE"),
                    GRDH_MOVIMIENTO: obj.getAttribute("PEHIS_MOVIMIENTO"),
                    GRDH_DESCRIPCION: obj.getAttribute("PEHIS_DESCRIPCION"),
                    GRDH_SUCURSAL: obj.getAttribute("SC_ID"),
                    GRDH_EMPRESA: obj.getAttribute("EMP_ID"),
                    GRDH_NUEVO: "1",
                    GRDH_PEDIDOID: obj.getAttribute("PD_ID")
                };
                //Anexamos el fregistro al GRID
                ContGridHistPediMak++;
                jQuery("#GRID_HISTORIAL").addRowData(ContGridHistPediMak, datarowInfHisMak, "last");
//                $("#dialog2").dialog("close");
            }
        },
        error: function (objeto, quepaso, otroobj) {
            alert(":pto:" + objeto + " " + quepaso + " " + otroobj);
        }
    });
}

function HistorialPedidoPediMak() {
    OpnOpt('HISTPAN_MAK', '_ed', 'dlgMakHistrial', false, false, true);
}

function ObtenSubCliente(DFA_ID) {
    $.ajax({
        type: "POST",
        data: "DFA_ID=" + DFA_ID,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "XML",
        url: "ERP_PedidosMakProcs.jsp?id=29",
        success: function (datos) {
            var objsc = datos.getElementsByTagName("Subcliente")[0];
            var lstProds = objsc.getElementsByTagName("Subcliente_deta");
            for (var i = 0; i < lstProds.length; i++) {
                var obj = lstProds[i];
                document.getElementById("cte_nomSubcte").value = obj.getAttribute('DFA_RAZONSOCIAL');
                document.getElementById("cte_rfcSubcte").value = obj.getAttribute('DFA_RFC');
                document.getElementById("cte_direccionSubCliente").value = obj.getAttribute('DFA_CALLE') + " " + obj.getAttribute('DFA_COLONIA') + " " + obj.getAttribute('DFA_LOCALIDAD') + " " + obj.getAttribute('DFA_MUNICIPIO') + " " + obj.getAttribute('DFA_ESTADO');
                document.getElementById("cte_telSubcte").value = obj.getAttribute('DFA_TELEFONO');
                document.getElementById("CT_CLIENTEFINAL").value = obj.getAttribute('DFA_ID');
            }
        },
        error: function (objeto, quepaso, otroobj) {
            alert(":pto:" + objeto + " " + quepaso + " " + otroobj);
        }
    });
}

function LimpiaCamposPediMak() {

    d.getElementById("FAC_FOLIO").value = "";
    d.getElementById("cte_sucursal").value = "";
    d.getElementById("CT_NOM").value = "";
    d.getElementById("FCT_ID").value = "";
    d.getElementById("cte_rfc").value = "";
    d.getElementById("cte_direccion").value = "";
    LimpiaSubClientePediMak();
    d.getElementById("FAC_PROD").value = "";
    d.getElementById("FACD_SUBTOTAL_A").value = "";
    d.getElementById("FAC_IMPORTE").value = "";
    d.getElementById("FAC_DESCUENTO").value = "";
    d.getElementById("FAC_IMPUESTO1").value = "";
    d.getElementById("FAC_TOT").value = "";
}

/**Limpia la operacion actual*/
function ResetOperaActualPediMak(bolSelOpera) {
    if (bolSelOpera == undefined)
        bolSelOpera = true;
    if (bolSelOpera)
        $("#dialogWait").dialog("open");
    bolSaveVta = true; //Activamos el guardado
    //Primer Div
    d.getElementById("FAC_FOLIO").value = "";
//    d.getElementById("cte_sucursal").value = "";
    //Segundo Div
    d.getElementById("CT_NOM").value = "";
    d.getElementById("FCT_ID").value = "";
    d.getElementById("cte_rfc").value = "";
    d.getElementById("cte_direccion").value = "";
    d.getElementById("cte_nomSubcte").value = "";
    d.getElementById("cte_rfcSubcte").value = "";
    d.getElementById("CT_CLIENTEFINAL").value = "";
    d.getElementById("cte_direccionSubCliente").value = "";
    d.getElementById("cte_telSubcte").value = "";
    //Limpiamos los campos de direccion de entrega
    LimpiaDireccionEntrega();
    //Tercer Div    
    d.getElementById("FAC_FECHA").value = "";
    d.getElementById("pd_fech2").value = "";
    d.getElementById("pd_pdManual").value = "";
    d.getElementById("pd_fechValidez").value = "";
//    if (identiPantallaMak == 1) {
    d.getElementById("pd_contrato").value = "";
//    }
    d.getElementById("FCT_LPRECIOS").value = "0";
    d.getElementById("FAC_MONEDA").value = "0";
    d.getElementById("pd_nomSolicit").value = "0";
    d.getElementById("pd_bodega").value = "0";
    document.getElementById("pd_bodega").disabled = false;
    d.getElementById("pd_cotizaOrigen").value = "";
    d.getElementById("pd_vendedor").value = "0";
    d.getElementById("pd_ocCliente").value = "";
    d.getElementById("TR_ID").value = "0";
    //Cuarto Div    
    d.getElementById("FAC_PROD").value = "";
    d.getElementById("FAC_CANT").value = "0";
    d.getElementById("FAC_DESC").value = "0";
    d.getElementById("FAC_PRECIO").value = "0";
    jQuery("#FAC_GRID").clearGridData();
    //Quinto Div   
    d.getElementById("FAC_IMPORTE").value = "";
    d.getElementById("FAC_DESCUENTO").value = "";
    d.getElementById("FAC_IMPUESTO1").value = "";
    d.getElementById("FAC_TOT").value = "";
    //Quinto Div   
//    d.getElementById("SC_ID").value = "";
//    d.getElementById("FAC_TIPO").value = "";
//    d.getElementById("BOD_ID").value = "";
//    d.getElementById("EMP_ID").value = "";

    d.getElementById("FAC_DEVO").value = "0";
    d.getElementById("FAC_TTC_ID").value = "0";
    d.getElementById("FCT_DESCUENTO").value = "0";
    document.getElementById("FCT_DESCUENTO").disabled = false;
    d.getElementById("FAC_DIASCREDITO").value = "0";
    d.getElementById("FCT_MONTOCRED").value = "0";
    d.getElementById("CT_VENTAMOSTRADOR").value = "0";
    d.getElementById("FAC_METODOPAGO").value = "0";
    d.getElementById("FAC_FORMADEPAGO").value = "0";
    d.getElementById("FAC_NUMCUENTA").value = "0";
    d.getElementById("VE_ID").value = "0";
    d.getElementById("FAC_TASASEL1").value = "0";
    d.getElementById("FAC_USE_IMP1").value = "0";
    d.getElementById("FAC_TASASEL2").value = "0";
    d.getElementById("FAC_USE_IMP2").value = "0";
    d.getElementById("FAC_TASASEL3").value = "0";
    d.getElementById("FAC_USE_IMP3").value = "0";
    d.getElementById("FAC_USO_IEPS1").value = "0";
    d.getElementById("FAC_TASA_IEPS").value = "0";
    d.getElementById("FAC_IMPORTE_IEPS").value = "0";
    d.getElementById("FAC_IMPUESTO2").value = "0";
    d.getElementById("FAC_IMPUESTO3").value = "0";
    d.getElementById("FAC_PUNTOS").value = "0";
    d.getElementById("FAC_NEGOCIO").value = "0";
    d.getElementById("FAC_IMPORTE_REAL").value = "0";
    d.getElementById("FAC_PZAS").value = "0";
    d.getElementById("FAC_PUNTOS_REAL").value = "0";
    d.getElementById("FAC_CREDITOS_REAL").value = "0";
    d.getElementById("FAC_NEGOCIO_REAL").value = "0";
    d.getElementById("FAC_IMPUESTO1_REAL").value = "0";
    d.getElementById("FAC_IMPUESTO2_REAL").value = "0";
    d.getElementById("FAC_IMPUESTO3_REAL").value = "0";
    d.getElementById("FAC_RETISR").value = "0";
    d.getElementById("FAC_RETIVA").value = "0";
    d.getElementById("FAC_NETO").value = "0";
    d.getElementById("PD_ID").value = "0";
    d.getElementById("COT_ID").value = "0";
    d.getElementById("FAC_NOTAS").value = "0";
    d.getElementById("FAC_NOTASPIE").value = "0";
    d.getElementById("FAC_REFERENCIA").value = "0";
    d.getElementById("FAC_CONDPAGO").value = "0";
    d.getElementById("FAC_NUMPEDI").value = "0";
    d.getElementById("FAC_FECHAPEDI").value = "0";
    d.getElementById("FAC_ADUANA").value = "0";
    d.getElementById("ME_ID").value = "0";
    d.getElementById("TF_ID").value = "0";
    d.getElementById("CT_DIRENTREGA").value = "0";
    d.getElementById("SYC_ID").value = "0";
    d.getElementById("FAC_NUM_GUIA").value = "0";
    d.getElementById("FAC_CONSIGNACION1").value = "0";
    d.getElementById("FAC_ESRECU1").value = "0";
    d.getElementById("FAC_PERIODICIDAD").value = "0";
    d.getElementById("FAC_DIAPER").value = "0";
    d.getElementById("FAC_NO_EVENTOS").value = "0";
    d.getElementById("ADD_MABE").value = "0";
    d.getElementById("ADD_SANOFI").value = "0";
    d.getElementById("ADD_FEMSA").value = "0";
    d.getElementById("VE_NOM").value = "0";
    d.getElementById("FAC_LPRECIOS").value = "0";
    d.getElementById("FAC_ESRECU2").value = "0";
    d.getElementById("FAC_TASAPESO").value = "0";
    d.getElementById("PED_BAN_CODIGOINCOMPLETO").value = "0";
    d.getElementById("PRECIOANTERIOR").value = "0";
    d.getElementById("CANTIDADANTERIOR").value = "0";
    d.getElementById("FACD_SUBTOTAL_A").value = "0";
    d.getElementById("STATUS").value = "0";
    d.getElementById("BandInformacion").value = "0";
    d.getElementById("COT_IDMOTIVO").value = "0";
    d.getElementById("COT_IDDESC").value = "0";
    d.getElementById("COT_BAN_CONV_COTI").value = "0";
    d.getElementById("COT_ID_PEDIDO").value = "0";
    d.getElementById("FAC_TIENE_FLETE").value = "0";
    d.getElementById("FAC_TIENE_SEGURO").value = "0";
    d.getElementById("FAC_MONTO_SEGURO").value = "0";
    d.getElementById("ME_ID").value = "0";
    d.getElementById("FAC_TIPO_FLETE").value = "0";
    d.getElementById("PANT_PAGOS").value = "0";
    d.getElementById("PD_CONV_FACTURA").value = "0";
    document.getElementById("FAC_POR_DESCUENTO").value = 0;
    document.getElementById("modlgn-flete1").checked = false;
    document.getElementById("modlgn-flete2").checked = false;
    intIngDirEnt = 0;


    intSucOfertas = intSucDefOfertas;
    //Actualizamos impuestos
    InitImpDefaultPediMak();
    d.getElementById("FAC_TASASEL1").value = intIdTasaVta1;
    if (d.getElementById("FAC_TASASEL2") != null) {
        d.getElementById("FAC_TASASEL2").value = intIdTasaVta2;
    }
    if (d.getElementById("FAC_TASASEL3") != null) {
        d.getElementById("FAC_TASASEL3").value = intIdTasaVta3;
    }
    if (bolSelOpera)
        //ObtenNomCtePediMak();////
        //Limpiamos el GRID
        var grid = jQuery("#FAC_GRID");
    grid.clearGridData();
    //Inicializamos eventos para el cuadro de dialogo SI/NO sino se puede sobreescribir con el de otra pantalla
    InitSIoNoEventsPediMak();
    //Limpiamos PAGOS
    if (objMap.getXml("FORMPAGO") != null && d.getElementById("TOTALPAGADO") != null) {
        d.getElementById("TOTALPAGADO").value = 0;
        d.getElementById("FPago1").value = 0;
        d.getElementById("FPago2").value = 0;
        d.getElementById("FPago3").value = 0;
        d.getElementById("FPago4").value = 0;
        d.getElementById("Ef_1").value = "0.0";
        d.getElementById("Ef_2").value = "0.0";
        d.getElementById("Bc_2").value = "";
        d.getElementById("Bc_3").value = "";
        d.getElementById("Bc_1").value = "0.0";
        d.getElementById("Tj_2").value = "";
        d.getElementById("Tj_3").value = "";
        d.getElementById("Tj_1").value = "0.0";
        d.getElementById("sf_1").value = "0.0";
    }
    if (bolSelOpera)
        $("#dialogWait").dialog("close");
    //Seleccionamos el tipo de operacion
    if (bolSelOpera)
        //Evalua el motor de promociones
        _ResetPromosAllPediMak();
}

function EvaluaCantProdPediMak() {
    var bool = "true";
    var grid = jQuery("#FAC_GRID");
    var arr = grid.getDataIDs();
    for (var i = 0; i < arr.length; i++) {
        var id = arr[i];
        var lstRow = grid.getRowData(id);

        if (parseInt(lstRow.FACD_CANTIDAD) > 0) {
            bool = "true";
        } else {
            if (lstRow.FACD_ES_COMPONENTE != "1") {
                bool = "false";
                alert("El producto " + lstRow.FACD_CVE + " no tiene cantidad o la cantidad es 0");
                break;
            }
        }

    }
    return bool;
}

function InitMotPediMak() {
    var strHtml = "<table>" +
            "<tr>" +
            "<td ><a href=\"javascript:SaliMotPediMak()\" class=\"sf-with-ul\" title=\"Salir\"><i class =\"fa fa-times\" style=\"font-size:40px\" ></i></td></span>&nbsp;&nbsp;" +
            "<td style=\"visibility:hidden\"id=\"22\"><i class = \"fa fa-hdd-o\" style=\"font-size:40px\" ></i></td>" +
            "<td ><a href=\"javascript:AceptarMotPediMak()\" class=\"sf-with-ul\" title=\"Aceptar\"><i class = \"fa fa-check\" style=\"font-size:40px\" ></i></td></span>&nbsp;&nbsp;" +
            "</tr>" +
            "</table>";
    document.getElementById("PAN_DIV").innerHTML = strHtml;

}

function SaliMotPediMak() {
    $("#dlgMakBusqProd").dialog("close");
}

function AceptarMotPediMak() {
    d.getElementById("COT_IDMOTIVO").value = d.getElementById("PAN_MOTIVOS").value;
    d.getElementById("COT_IDDESC").value = d.getElementById("PAN_DESCR").value;
    //Opcion 4 COT_BAN_CONV_COTI Se guaradar el pedido pero con el status de cerrrado
    d.getElementById("COT_BAN_CONV_COTI").value = 4;
    $("#dlgMakBusqProd").dialog("close");
    if (locidCotizaEditMak == 0) {
        //Se guarda la cotizacion pero con el status de cerrado       
        SavePediMak();
    } else {
        //Se cambia el status a cerrado solo cuando se edita
        CierraCotiPediMak();
    }
}

function CierraCotiPediMak() {
    $.ajax({
        type: "POST",
        data: "COT_ID=" + d.getElementById("COT_ID").value,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "html",
        url: "ERP_PedidosMakProcs.jsp?id=40",
        success: function (datos) {
            if (datos.substring(0, 2) == "OK") {
                Callbtn9PediMak();
            } else {
                alert(datos);
            }
            $("#dialogWait").dialog("close");
        },
        error: function (objeto, quepaso, otroobj) {
            alert(":pto:" + objeto + " " + quepaso + " " + otroobj);
        }
    });
}

var idEditCoti = 0;
function ConvertirCotiaPediMak() {
    if (bolPermConfirmMak) {
        if (validaDatosPediMak() == true) {
            if (VerificaCreditoPediMak() == "true") {
                if (idEditCoti == 0) {
                    var grid = jQuery("#FAC_GRID");
                    var arr = grid.getDataIDs();
                    var cont = 0;
                    for (var i = 0; i < arr.length; i++) {
                        var id = arr[i];
                        var lstRow = grid.getRowData(id);

                        var dbldisponibilidad = 0.0;
                        var strdisponibilidad = lstRow.FACD_DISPONIBLE.toString();
                        if (strdisponibilidad.indexOf(",") == -1) {
                            dbldisponibilidad = parseFloat(lstRow.FACD_DISPONIBLE);
                        } else {
                            dbldisponibilidad = parseFloat(strdisponibilidad.replace(",", ""));
                        }
                        if (lstRow.FACD_REQEXIST == 1) {
                            if (lstRow.FACD_TIPO_BACKORDER_LETRA != "RQS" &&
                                    lstRow.FACD_TIPO_BACKORDER_LETRA != "TI" &&
                                    lstRow.FACD_TIPO_BACKORDER_LETRA != "RQS,TI" &&
                                    lstRow.FACD_TIPO_BACKORDER_LETRA != "AK") {
                                if (parseFloat(lstRow.FACD_CANTIDAD) > dbldisponibilidad) {
                                    var objSecModiVta = objMap.getScreen("BACKORDER_MAK");
                                    if (objSecModiVta != null) {
                                        objSecModiVta.bolActivo = false;
                                        objSecModiVta.bolMain = false;
                                        objSecModiVta.bolInit = false;
                                        objSecModiVta.idOperAct = 0;
                                    }
                                    lastSelBakOrder = id;
                                    OpnOpt('BACKORDER_MAK', '_ed', 'dlgMakBackOrder', false, false, true);
                                    break;
                                }
                            }
                        }
                        cont++;
                    }
                    if (cont == arr.length) {
                        d.getElementById("COT_BAN_CONV_COTI").value = 1;
                        SavePediMak();
                    }
                }
            }
        }
    } else {
        alert("No tiene permisos para confirmar.");
    }

}

function ConviertePediMak(idCotizacion) {
    d.getElementById("COT_BAN_CONV_COTI").value = 2;
    d.getElementById("COT_ID_PEDIDO").value = idCotizacion;
    /*Cambiamos la bandera a pedidos para que tambien realize euna copia pero para pedidos*/
    d.getElementById("FAC_TIPO").value = 3;
    identiPantallaMak = 1;
    SavePediMak();
}

/**Carga la informacion de una operacion de REMISION FACTURA O PEDIDO*/
function getPedidoenVentaPediMak(intIdPedido) {
    var strTipoVta = "PEDIDO";
    //Mandamos la peticion por ajax para que nos den el XML del pedido
    $.ajax({
        type: "POST",
        data: "PD_ID=" + intIdPedido,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "VtasMov.do?id=8",
        success: function (datos) {
            var objPedido = datos.getElementsByTagName("vta_pedido")[0];
            var lstdeta = objPedido.getElementsByTagName("deta");
            //Validamos que sea un pedido correcto
            if (objPedido.getAttribute('PD_ANULADA') == 0) {
                //No facturado
                if (objPedido.getAttribute("FAC_ID") == 0 || (objPedido.getAttribute("FAC_ID") != 0)) {
                    //No remisionado
                    if (objPedido.getAttribute("TKT_ID") == 0 || (objPedido.getAttribute("TKT_ID") != 0)) {
                        //Esta Confirmado
                        if (objPedido.getAttribute("PD_IMPRESO") != "1") {
                            //Limpiamos la operacion actual.
                            ResetOperaActualPediMak();
                            //Llenamos la pantalla con los valores del bd
                            DrawPedidoenVentaPediMak(objPedido, lstdeta, strTipoVta);
                            $("#dialogView").dialog("close");
                        } else {
                            alert("Pedido en proceso de surtido");
                        }
                    } else {
                        alert("El pedido ya fue Remisionado");
                    }
                } else {
                    alert("El pedido ya fue Facturado");
                }
            } else {
                //No anulado
                alert("El pedido fue anulado!");
            }
        },
        error: function (objeto, quepaso, otroobj) {
            alert(":ptoExist:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }
    });
}

/**
 *Establece los parametros del pedido original
 **/
function DrawPedidoenVentaPediMak(objPedido, lstdeta, strTipoVta) {
    $("#dialogWait").dialog("open");
    document.getElementById("PD_ID").value = objPedido.getAttribute('PD_ID');
    if (strTipoVta == "REMISION") {
        document.getElementById("FAC_TIPO").value = 2;
    } else {
        if (strTipoVta == "FACTURA") {
            document.getElementById("FAC_TIPO").value = 1;
        } else {
            document.getElementById("FAC_TIPO").value = 3;
        }
    }
    document.getElementById("FAC_MONEDA").value = objPedido.getAttribute('PD_MONEDA');
    document.getElementById("FAC_FOLIO").value = objPedido.getAttribute('PD_FOLIO');
    document.getElementById("SC_ID").value = objPedido.getAttribute('SC_ID');
    document.getElementById("FCT_ID").value = objPedido.getAttribute('CT_ID');
    document.getElementById("FAC_NOTAS").value = objPedido.getAttribute('PD_NOTAS');
    document.getElementById("FAC_NOTASPIE").value = objPedido.getAttribute('PD_NOTASPIE');
    document.getElementById("FAC_CONDPAGO").value = objPedido.getAttribute('PD_CONDPAGO');
    document.getElementById("FAC_REFERENCIA").value = objPedido.getAttribute('PD_REFERENCIA');
    document.getElementById("FAC_NUMPEDI").value = objPedido.getAttribute('PD_NUMPEDI');
    document.getElementById("FAC_FECHAPEDI").value = objPedido.getAttribute('PD_FECHAPEDI');
    document.getElementById("FAC_ADUANA").value = objPedido.getAttribute('PD_ADUANA');
    document.getElementById("FAC_POR_DESCUENTO").value = objPedido.getAttribute('PD_POR_DESCUENTO');
    if (objPedido.getAttribute('PD_ESRECU') == 1) {
        document.getElementById("FAC_ESRECU1").checked = true;
    } else {
        document.getElementById("FAC_ESRECU2").checked = false;
    }
    document.getElementById("FAC_PERIODICIDAD").value = objPedido.getAttribute('PD_PERIODICIDAD');
    document.getElementById("FAC_DIAPER").value = objPedido.getAttribute('PD_DIAPER');
    document.getElementById("VE_ID").value = objPedido.getAttribute('VE_ID');
    document.getElementById("pd_tipoCamCantidad").value = objPedido.getAttribute('PD_TASAPESO');
    document.getElementById("FAC_TIPOCAMACT").value = objPedido.getAttribute('PD_TASAPESO');

    var fechaPost = "";
    fechaPost += "&FAC_FECHA=" + objPedido.getAttribute('PD_FECHA');
    fechaPost += "&pd_fech2=" + objPedido.getAttribute('PD_FECHA_SURTIDO');
    fechaPost += "&pd_fechValidez=" + objPedido.getAttribute('PD_FECHA_ENTREGA');
    $.ajax({
        type: "POST",
        data: fechaPost,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "ERP_PedidosMakProcs.jsp?id=32",
        success: function (datos) {
            var lstXml = datos.getElementsByTagName("pedidos")[0];
            var lstprecio = lstXml.getElementsByTagName("pedidos_deta");
            for (var i = 0; i < lstprecio.length; i++) {
                var objfe = lstprecio[i];

                document.getElementById("FAC_FECHA").value = objfe.getAttribute('FAC_FECHA');
                document.getElementById("pd_fech2").value = objfe.getAttribute('pd_fech2');
                document.getElementById("pd_fechValidez").value = objfe.getAttribute('pd_fechValidez');

            }
        },
        error: function (objeto, quepaso, otroobj) {
            alert(":pto9:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }
    });
    document.getElementById("pd_pdManual").value = objPedido.getAttribute('PD_PEDMANUAL');
    document.getElementById("pd_contrato").value = objPedido.getAttribute('PD_CONTRATO');
    document.getElementById("pd_nomSolicit").value = objPedido.getAttribute('PD_SOLICITANTE');
    document.getElementById("pd_cotizaOrigen").value = objPedido.getAttribute('PD_COT_ORIG');
    document.getElementById("pd_ocCliente").value = objPedido.getAttribute('PD_OC_CLIENTE');
    document.getElementById("TR_ID").value = objPedido.getAttribute('TR_ID');
    document.getElementById("pd_vendedor").value = objPedido.getAttribute('VE_ID');
    document.getElementById("FAC_TIENE_FLETE").value = objPedido.getAttribute('PD_TIENE_FLETE');
    document.getElementById("FAC_TIENE_SEGURO").value = objPedido.getAttribute('PD_TIENE_SEGURO');
    document.getElementById("FAC_MONTO_SEGURO").value = objPedido.getAttribute('PD_MONTO_SEGURO');
    document.getElementById("ME_ID").value = objPedido.getAttribute('ME_ID');
    document.getElementById("FAC_TIPO_FLETE").value = objPedido.getAttribute('PD_TIPO_FLETE');
    document.getElementById("PD_CONV_FACTURA").value = objPedido.getAttribute('PD_CONV_FACTURA');


    if (document.getElementById("FAC_TIENE_FLETE").value == 1) {
        document.getElementById("modlgn-flete1").checked = true;
    } else {
        document.getElementById("modlgn-flete2").checked = true;
    }
    //Obtenemos el cliente
    cargaClientePediMak(objPedido, lstdeta, strTipoVta);
}

function cargaClientePediMak(objPedido, lstdeta, strTipoVta) {
    var intCte = document.getElementById("FCT_ID").value;
    $.ajax({
        type: "POST",
        data: "CT_ID=" + intCte,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "VtasMov.do?id=9",
        success: function (datoVal) {
            var objCte = datoVal.getElementsByTagName("vta_clientes")[0];
            document.getElementById("CT_NOM").value = objCte.getAttribute('CT_RAZONSOCIAL');
            document.getElementById("cte_rfc").value = objCte.getAttribute('CT_RFC');
            document.getElementById("cte_direccion").value = objCte.getAttribute('CT_DIRECCION');
            document.getElementById("pd_bodega").value = objPedido.getAttribute('SC_ID');


            /*Cuando es una edicion no activamos el boton de convertir pedido a factura*/
            if (typeof idPedidoEditConsultMak != "undefined") {
                if (idPedidoEditConsultMak != "" && idPedidoEditConsultMak != "0") {
                } else {
                    verifConvPedFacPediMak();
                }
            } else {
                verifConvPedFacPediMak();
            }

            document.getElementById("FCT_LPRECIOS").value = objCte.getAttribute('CT_LPRECIOS');
            document.getElementById("FCT_MONTOCRED").value = FormatNumber(objCte.getAttribute('CT_MONTOCRED'), 2, true, false, true, false);
            document.getElementById("CT_VENTAMOSTRADOR").value = objCte.getAttribute('CT_VENTAMOSTRADOR');
            document.getElementById("FAC_DIASCREDITO").value = objCte.getAttribute('CT_DIASCREDITO');
            document.getElementById("FAC_TTC_ID").value = objCte.getAttribute('TTC_ID');
            document.getElementById("pd_tipoCam").value = objCte.getAttribute('TTC_ID');
            document.getElementById("FAC_TIPOCAMACT").value = FormatNumber(objCte.getAttribute('dblParidad'), 2, true, false, true, false);
            document.getElementById("FAC_METODOPAGO").value = objCte.getAttribute('CT_METODODEPAGO');
            document.getElementById("FAC_FORMADEPAGO").value = objCte.getAttribute('CT_FORMADEPAGO');
            document.getElementById("FAC_NUMCUENTA").value = objCte.getAttribute('CT_CTABANCO1');
            intCT_TIPOPERS = objCte.getAttribute('CT_TIPOPERS');
            intCT_TIPOFAC = objCte.getAttribute('CT_TIPOFAC');
            strCT_USOIMBUEBLE = objCte.getAttribute('CT_USOIMBUEBLE');
            ObtenSubCliente(objPedido.getAttribute('DFA_ID'));
            ObtenDirEntrega(objPedido.getAttribute('CDE_ID'));
            validaNumDocuPediMak();
            if (identiPantallaMak == 1) {
                DrawPedidoDetaenVentaPediMak(objPedido, lstdeta, strTipoVta);
            }
            if (identiPantallaMak == 2) {
                DrawCotiDetaenVentaPediMak(objPedido, lstdeta, strTipoVta);
            }
            BloquePantallaMak();

        },
        error: function (objeto, quepaso, otroobj) {
            alert(":pto:" + objeto + " " + quepaso + " " + otroobj);
        }
    });
}

/**
 *Llenamos el grid con los datos del pedido
 **/
function DrawPedidoDetaenVentaPediMak(objPedido, lstdeta, strTipoVta) {
    //Generamos el detalle
    for (i = 0; i < lstdeta.length; i++) {
        document.getElementById("pd_bodega").disabled = true;
        document.getElementById("FCT_DESCUENTO").disabled = true;
        var obj = lstdeta[i];
        var objImportes = new _ImporteVtaPediMak();
        objImportes.dblCantidad = obj.getAttribute('PDD_CANTIDAD');
        if (parseInt(obj.getAttribute('PDD_EXENTO1')) == 0) {
            objImportes.dblPrecio = parseFloat(obj.getAttribute('PDD_PRECIO')) / (1 + (obj.getAttribute('PDD_TASAIVA1') / 100));
            objImportes.dblPrecioReal = parseFloat(obj.getAttribute('PDD_PRECREAL')) / (1 + (obj.getAttribute('PDD_TASAIVA1') / 100));
        } else {
            objImportes.dblPrecio = parseFloat(obj.getAttribute('PDD_PRECIO'));
            objImportes.dblPrecioReal = parseFloat(obj.getAttribute('PDD_PRECREAL'));
        }
        objImportes.dblPorcDescGlobal = obj.getAttribute('PDD_PORDESC');
        objImportes.dblPorcDesc = obj.getAttribute('PDD_PORDESC');
        objImportes.dblExento1 = obj.getAttribute('PDD_EXENTO1');
        objImportes.dblExento2 = obj.getAttribute('PDD_EXENTO2');
        objImportes.dblExento3 = obj.getAttribute('PDD_EXENTO3');
        objImportes.intDevo = 0;
        objImportes.dblPuntos = parseFloat(obj.getAttribute('PDD_PUNTOS'));
        objImportes.dblVNegocio = parseFloat(obj.getAttribute('PDD_VNEGOCIO'));
        //Banderas de descuento
        if (obj.getAttribute('PDD_DESC_PREC') == 0)
            objImportes.bolAplicDescPrec = false;
        if (obj.getAttribute('PDD_DESC_PUNTOS') == 0)
            objImportes.bolAplicDescPto = false;
        if (obj.getAttribute('PDD_DESC_VNEGOCIO') == 0)
            objImportes.bolAplicDescVNego = false;
        //Validamos existencias en caso de que aplique
        if (obj.getAttribute('PR_REQEXIST') == 1 &&
                (strTipoVta == "FACTURA" || strTipoVta == "REMISION")) {
            if (parseFloat(objImportes.dblCantidad) > parseFloat(obj.getAttribute('PR_EXISTENCIA'))) {
                alert(lstMsg[3] + " " + obj.getAttribute('PDD_CVE') + " " + lstMsg[4]);
                if (parseFloat(obj.getAttribute('PR_EXISTENCIA')) > 0) {
                    objImportes.dblCantidad = obj.getAttribute('PR_EXISTENCIA');
                } else {
                    objImportes.dblCantidad = 0;
                }
            }
        }
        //Calculamos el importe de la venta
        //Evaluamos si aplican los puntos y valor negocio de multinivel
        var bolAplicaMLM = true;
        if (document.getElementById("FAC_ES_MLM1") != null && document.getElementById("FAC_ES_MLM2") != null) {
            if (document.getElementById("FAC_ES_MLM2").checked)
                bolAplicaMLM = false;
        }

        objImportes.bolUsoMLM = bolAplicaMLM;
        //Evaluamos si aplican los puntos y valor negocio de multinivel
        objImportes.CalculaImportePediMak();
        var strRequisicion = obj.getAttribute('PDD_CANT_REQU');
        var strbackorder = obj.getAttribute('PDD_BACKORDER');
        var StrTIPO_BACKORDER = "";
        var StrTIPO_BACKORDER_LETRA = "";
        var StrDETALLE_BACKORDER = "";
        var StrES_BACKORDER = "0";
        if (strbackorder != "" && strbackorder != "0") {
            StrTIPO_BACKORDER = "2";
            StrTIPO_BACKORDER_LETRA = "TI";
            StrDETALLE_BACKORDER = strbackorder;
            StrES_BACKORDER = "1";
        }
        if (strRequisicion != "" && strRequisicion != "0" && strRequisicion != 0) {
            StrTIPO_BACKORDER = "1";
            StrTIPO_BACKORDER_LETRA = "RQS";
            StrDETALLE_BACKORDER = "";
            StrES_BACKORDER = "0";
        }

        if (strbackorder != "" && strbackorder != "0" && strRequisicion != "" && strRequisicion != "0" && strRequisicion != 0) {
            StrTIPO_BACKORDER = "4";
            StrTIPO_BACKORDER_LETRA = "RQS,TI";
            StrDETALLE_BACKORDER = strbackorder;
            StrES_BACKORDER = "1";
        }

        var dblPrecio = objImportes.dblPrecio;
        var dblImporte = objImportes.dblImporte;

        var datarow = {
            FACD_ID: 0,
            FACD_CANTIDAD: objImportes.dblCantidad,
            FACD_CANTPEDIDO: obj.getAttribute('PDD_CANTIDAD'),
            FACD_DESCRIPCION: obj.getAttribute('PDD_DESCRIPCION'),
            FACD_IMPORTE: dblImporte,
            FACD_CVE: obj.getAttribute('PDD_CVE'),
            FACD_PRECIO: dblPrecio,
            FACD_TASAIVA1: obj.getAttribute('PDD_TASAIVA1'),
            FACD_TASAIVA2: obj.getAttribute('PDD_TASAIVA2'),
            FACD_TASAIVA3: obj.getAttribute('PDD_TASAIVA3'),
            FACD_DESGLOSA1: 1,
            FACD_IMPUESTO1: objImportes.dblImpuesto1,
            FACD_IMPUESTO2: objImportes.dblImpuesto2,
            FACD_IMPUESTO3: objImportes.dblImpuesto3,
            FACD_PR_ID: obj.getAttribute('PR_ID'),
            FACD_EXENTO1: obj.getAttribute('PDD_EXENTO1'),
            FACD_EXENTO2: obj.getAttribute('PDD_EXENTO2'),
            FACD_EXENTO3: obj.getAttribute('PDD_EXENTO3'),
            FACD_REQEXIST: obj.getAttribute('PR_REQEXIST'),
            FACD_EXIST: obj.getAttribute('PR_EXISTENCIA'),
            FACD_NOSERIE: "",
            FACD_ESREGALO: obj.getAttribute('PDD_ESREGALO'),
            FACD_IMPORTEREAL: objImportes.dblImporteReal,
            FACD_PRECREAL: objImportes.dblPrecioReal,
            FACD_DESCUENTO: FormatNumber(obj.getAttribute('PDD_DESCUENTO'), 2, true, false, true, false),
            FACD_PORDESC: objImportes.dblPorcAplica,
            FACD_PRECFIJO: obj.getAttribute('PDD_PRECFIJO'),
            FACD_ESDEVO: 0,
            FACD_CODBARRAS: obj.getAttribute('PR_CODBARRAS'),
            FACD_NOTAS: obj.getAttribute('PDD_COMENTARIO'),
            FACD_PUNTOS_U: objImportes.dblPuntos,
            FACD_NEGOCIO_U: objImportes.dblVNegocio,
            FACD_PUNTOS: objImportes.dblPuntosImporte,
            FACD_NEGOCIO: objImportes.dblVNegocioImporte,
            FACD_DESC_ORI: obj.getAttribute('PDD_DESC_ORI'),
            FACD_REGALO: obj.getAttribute('PDD_REGALO'),
            FACD_ID_PROMO: obj.getAttribute('PDD_ID_PROMO'),
            FACD_DESC_PREC: obj.getAttribute('PDD_DESC_PREC'),
            FACD_DESC_PTO: obj.getAttribute('PDD_DESC_PUNTOS'),
            FACD_DESC_VN: obj.getAttribute('PDD_DESC_VNEGOCIO'),
            FACD_DESC_LEAL: 0,
            FACD_ES_BACKORDER: StrES_BACKORDER,
            FACD_TIPO_BACKORDER: StrTIPO_BACKORDER,
            FACD_TIPO_BACKORDER_LETRA: StrTIPO_BACKORDER_LETRA,
            FACD_DETALLE_BACKORDER: StrDETALLE_BACKORDER,
            FACD_REQUISICION: strRequisicion,
            FACD_DISPONIBLE: obj.getAttribute('DISPONIBILIDAD'),
            FACD_UNIDAD_MEDIDA: obj.getAttribute('PDD_UNIDAD_MEDIDA'),
            FACD_ES_PAQUETE: obj.getAttribute('PDD_ES_PAQUETE'),
            FACD_ES_COMPONENTE: obj.getAttribute('PDD_ES_COMPONENTE'),
            FACD_PR_PAQUETE: obj.getAttribute('PDD_PR_PAQUETE'),
            FACD_MULTIPLO: obj.getAttribute('PDD_MULTIPLO'),
            FACD_MPV: obj.getAttribute('PR_NUM_EMPAQU_VENT'),
            FACD_PRECIO_MULTIPLOS: obj.getAttribute('PDD_PRECIO_MULTIPLO'),
            FACD_ES_MULTIPLO: obj.getAttribute('PR_ESMULTIPLO'),
            FACD_CANTIDAD_MULTIPLOS: obj.getAttribute('PDD_CANT_MULTIPLOS'),
            FACD_PRECIO_TOT_MULTIPLOS: 0,
            FACD_CORTESIA: obj.getAttribute('PDD_CANT_CORTESIAS')
        };
        //Anexamos el registro al GRID
        jQuery("#FAC_GRID").addRowData(obj.getAttribute('PDD_ID'), datarow, "last");
    }
    /*Recorremos para actualizar los multiplos*/
    var grid = jQuery("#FAC_GRID");
    var arr = grid.getDataIDs();
    for (var i = 0; i < arr.length; i++) {
        var id = arr[i];
        var lstRow = grid.getRowData(id);
        /*Multiplos*/
        calculaMultiplos(lstRow);
        //Actualizamos el grid
        grid.setRowData(id, lstRow);
    }

//Realizamos la sumatoria
    setSumPediMak();
//Activamos la bandera que Se esta editando un pedido
    $("#dialogCte").dialog("close");
    $("#dlgMakConsultaPed").dialog("close");
    $("#dialogWait").dialog("close");
}

function DrawCotiDetaenVentaPediMak(objPedido, lstdeta, strTipoVta) {
//Generamos el detalle
    for (var i = 0; i < lstdeta.length; i++) {
        document.getElementById("pd_bodega").disabled = true;
        document.getElementById("FCT_DESCUENTO").disabled = true;
        var obj = lstdeta[i];
        var objImportes = new _ImporteVtaPediMak();
        objImportes.dblCantidad = obj.getAttribute('COTD_CANTIDAD');
        if (parseInt(obj.getAttribute('COTD_EXENTO1')) == 0) {
            objImportes.dblPrecio = parseFloat(obj.getAttribute('COTD_PRECIO')) / (1 + (parseFloat(obj.getAttribute('COTD_TASAIVA1')) / 100));
            objImportes.dblPrecioReal = parseFloat(obj.getAttribute('COTD_PRECREAL')) / (1 + (parseFloat(obj.getAttribute('COTD_TASAIVA1')) / 100));
        } else {
            objImportes.dblPrecio = parseFloat(obj.getAttribute('COTD_PRECIO'));
            objImportes.dblPrecioReal = parseFloat(obj.getAttribute('COTD_PRECREAL'));
        }

        objImportes.dblPorcDescGlobal = obj.getAttribute('COTD_PORDESC');
        objImportes.dblPorcDesc = obj.getAttribute('COTD_PORDESC');
        objImportes.dblExento1 = obj.getAttribute('COTD_EXENTO1');
        objImportes.dblExento2 = obj.getAttribute('COTD_EXENTO2');
        objImportes.dblExento3 = obj.getAttribute('COTD_EXENTO3');
        objImportes.intDevo = 0;
        objImportes.dblPuntos = parseFloat(obj.getAttribute('COTD_PUNTOS'));
        objImportes.dblVNegocio = parseFloat(obj.getAttribute('COTD_VNEGOCIO'));
        //Banderas de descuento
        if (obj.getAttribute('COTD_DESC_PREC') == 0)
            objImportes.bolAplicDescPrec = false;
        if (obj.getAttribute('COTD_DESC_PUNTOS') == 0)
            objImportes.bolAplicDescPto = false;
        if (obj.getAttribute('COTD_DESC_VNEGOCIO') == 0)
            objImportes.bolAplicDescVNego = false;
        //if(lstRow.FACD_DESC_LEAL == 0)objImportes.bolAplicDescPrec= false;
        //Validamos existencias en caso de que aplique
        if (obj.getAttribute('PR_REQEXIST') == 1 &&
                (strTipoVta == "FACTURA" || strTipoVta == "REMISION")) {
            if (parseFloat(objImportes.dblCantidad) > parseFloat(obj.getAttribute('PR_EXISTENCIA'))) {
                alert(lstMsg[3] + " " + obj.getAttribute('COTD_CVE') + " " + lstMsg[4]);
                if (parseFloat(obj.getAttribute('PR_EXISTENCIA')) > 0) {
                    objImportes.dblCantidad = obj.getAttribute('PR_EXISTENCIA');
                } else {
                    objImportes.dblCantidad = 0;
                }
            }
        }
        //Calculamos el importe de la venta
        //Evaluamos si aplican los puntos y valor negocio de multinivel
        var bolAplicaMLM = true;
        if (document.getElementById("FAC_ES_MLM1") != null && document.getElementById("FAC_ES_MLM2") != null) {
            if (document.getElementById("FAC_ES_MLM2").checked)
                bolAplicaMLM = false;
        }
        objImportes.bolUsoMLM = bolAplicaMLM;
        //Evaluamos si aplican los puntos y valor negocio de multinivel
        objImportes.CalculaImportePediMak();
        var dblImporte = objImportes.dblImporte;
        var datarow = {
            FACD_ID: 0,
            FACD_CANTIDAD: objImportes.dblCantidad,
            FACD_CANTPEDIDO: obj.getAttribute('COTD_CANTIDAD'),
            FACD_DESCRIPCION: obj.getAttribute('COTD_DESCRIPCION'),
            FACD_IMPORTE: dblImporte,
            FACD_CVE: obj.getAttribute('COTD_CVE'),
            FACD_PRECIO: FormatNumber(objImportes.dblPrecio, 2, true, false, true, false),
            FACD_TASAIVA1: obj.getAttribute('COTD_TASAIVA1'),
            FACD_TASAIVA2: obj.getAttribute('COTD_TASAIVA2'),
            FACD_TASAIVA3: obj.getAttribute('COTD_TASAIVA3'),
            FACD_DESGLOSA1: 1,
            FACD_IMPUESTO1: objImportes.dblImpuesto1,
            FACD_IMPUESTO2: objImportes.dblImpuesto2,
            FACD_IMPUESTO3: objImportes.dblImpuesto3,
            FACD_PR_ID: obj.getAttribute('PR_ID'),
            FACD_EXENTO1: obj.getAttribute('COTD_EXENTO1'),
            FACD_EXENTO2: obj.getAttribute('COTD_EXENTO2'),
            FACD_EXENTO3: obj.getAttribute('COTD_EXENTO3'),
            FACD_REQEXIST: obj.getAttribute('PR_REQEXIST'),
            FACD_EXIST: obj.getAttribute('PR_EXISTENCIA'),
            FACD_NOSERIE: "",
            FACD_ESREGALO: obj.getAttribute('COTD_ESREGALO'),
            FACD_IMPORTEREAL: objImportes.dblImporteReal,
            FACD_PRECREAL: objImportes.dblPrecioReal,
            FACD_DESCUENTO: FormatNumber(obj.getAttribute('COTD_DESCUENTO'), 2, true, false, true, false),
            FACD_PORDESC: objImportes.dblPorcAplica,
            FACD_PRECFIJO: obj.getAttribute('COTD_PRECFIJO'),
            FACD_ESDEVO: 0,
            FACD_CODBARRAS: obj.getAttribute('PR_CODBARRAS'),
            FACD_NOTAS: obj.getAttribute('COTD_COMENTARIO'),
            FACD_PUNTOS_U: objImportes.dblPuntos,
            FACD_NEGOCIO_U: objImportes.dblVNegocio,
            FACD_PUNTOS: objImportes.dblPuntosImporte,
            FACD_NEGOCIO: objImportes.dblVNegocioImporte,
            FACD_DESC_ORI: obj.getAttribute('COTD_DESC_ORI'),
            FACD_REGALO: obj.getAttribute('COTD_REGALO'),
            FACD_ID_PROMO: obj.getAttribute('COTD_ID_PROMO'),
            FACD_DESC_PREC: obj.getAttribute('COTD_DESC_PREC'),
            FACD_DESC_PTO: obj.getAttribute('COTD_DESC_PUNTOS'),
            FACD_DESC_VN: obj.getAttribute('COTD_DESC_VNEGOCIO'),
            FACD_DESC_LEAL: 0,
            FACD_DISPONIBLE: obj.getAttribute('DISPONIBILIDAD'),
            FACD_UNIDAD_MEDIDA: obj.getAttribute('COTD_UNIDAD_MEDIDA'),
            FACD_ES_PAQUETE: obj.getAttribute('COTD_ES_PAQUETE'),
            FACD_ES_COMPONENTE: obj.getAttribute('COTD_ES_COMPONENTE'),
            FACD_PR_PAQUETE: obj.getAttribute('COTD_PR_PAQUETE'),
            FACD_MULTIPLO: obj.getAttribute('COTD_MULTIPLO'),
            FACD_MPV: obj.getAttribute('PR_NUM_EMPAQU_VENT'),
            FACD_PRECIO_MULTIPLOS: obj.getAttribute('COTD_PRECIO_MULTIPLO'),
            FACD_ES_MULTIPLO: obj.getAttribute('PR_ESMULTIPLO'),
            FACD_CANTIDAD_MULTIPLOS: obj.getAttribute('COTD_CANT_MULTIPLOS'),
            FACD_PRECIO_TOT_MULTIPLOS: 0,
            FACD_CORTESIA: obj.getAttribute('COTD_CANT_CORTESIAS')
        };
        //Anexamos el registro al GRID
        jQuery("#FAC_GRID").addRowData(obj.getAttribute('COTD_ID'), datarow, "last");
    }
    /*Recorremos para actualizar los multiplos*/
    var grid = jQuery("#FAC_GRID");
    var arr = grid.getDataIDs();
    for (var i = 0; i < arr.length; i++) {
        var id = arr[i];
        var lstRow = grid.getRowData(id);
        /*Multiplos*/
        calculaMultiplos(lstRow);
        //Actualizamos el grid
        grid.setRowData(id, lstRow);
    }
//Realizamos la sumatoria
    setSumPediMak();
    $("#dialogWait").dialog("close");
}

/**Carga la informacion de una operacion de COTIZACION*/
function getCotizaenVentaPediMak(intIdCotizacion) {
    var strTipoVta = "COTIZACION";
    //Mandamos la peticion por ajax para que nos den el XML del pedido
    $.ajax({
        type: "POST",
        data: "COT_ID=" + intIdCotizacion,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "VtasMov.do?id=33",
        success: function (datos) {
            var objPedido = datos.getElementsByTagName("vta_cotiza")[0];
            var lstdeta = objPedido.getElementsByTagName("deta");
            //Validamos que sea un pedido correcto
            if (objPedido.getAttribute('COT_ANULADA') == 0) {
                //No facturado
                if (objPedido.getAttribute("FAC_ID") == 0 || (objPedido.getAttribute("FAC_ID") != 0) && objPedido.getAttribute('COT_ESRECU') == 1) {
                    //No remisionado
                    if (objPedido.getAttribute("TKT_ID") == 0 || (objPedido.getAttribute("TKT_ID") != 0) && objPedido.getAttribute('COT_ESRECU') == 1) {
                        //Limpiamos la operacion actual.
                        ResetOperaActualPediMak();
                        //Llenamos la pantalla con los valores del bd
                        DrawCotienVentaPediMak(objPedido, lstdeta, strTipoVta);
                        $("#dialogView").dialog("close");

                    } else {
                        alert("Cotizacion ya fue Remisionado");
                    }
                } else {
                    alert("Cotizacion ya fue Facturado");
                }
            } else {
                //No anulado
                alert("Cotizacion fue anulado!");
            }
        },
        error: function (objeto, quepaso, otroobj) {
            alert(":ptoExist:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }
    });
}

function DrawCotienVentaPediMak(objPedido, lstdeta, strTipoVta) {
    $("#dialogWait").dialog("open");
    document.getElementById("COT_ID").value = objPedido.getAttribute('COT_ID');
    if (strTipoVta == "REMISION") {
        document.getElementById("FAC_TIPO").value = 2;
    } else {
        if (strTipoVta == "FACTURA") {
            document.getElementById("FAC_TIPO").value = 1;
        } else {
            if (strTipoVta == "PEDIDO") {
                document.getElementById("FAC_TIPO").value = 3;
            } else {
                document.getElementById("FAC_TIPO").value = 5;
            }
        }
    }
    document.getElementById("FAC_MONEDA").value = objPedido.getAttribute('COT_MONEDA');
    document.getElementById("FAC_FOLIO").value = objPedido.getAttribute('COT_FOLIO');
    document.getElementById("SC_ID").value = objPedido.getAttribute('SC_ID');
    document.getElementById("FCT_ID").value = objPedido.getAttribute('CT_ID');
    document.getElementById("FAC_NOTAS").value = objPedido.getAttribute('COT_NOTAS');
    document.getElementById("FAC_NOTASPIE").value = objPedido.getAttribute('COT_NOTASPIE');
    document.getElementById("FAC_CONDPAGO").value = objPedido.getAttribute('COT_CONDPAGO');
    document.getElementById("FAC_REFERENCIA").value = objPedido.getAttribute('COT_REFERENCIA');
    document.getElementById("FAC_NUMPEDI").value = objPedido.getAttribute('COT_NUMPEDI');
    document.getElementById("FAC_FECHAPEDI").value = objPedido.getAttribute('COT_FECHAPEDI');
    document.getElementById("FAC_ADUANA").value = objPedido.getAttribute('COT_ADUANA');
    document.getElementById("FAC_POR_DESCUENTO").value = objPedido.getAttribute('COT_POR_DESCUENTO');
    if (objPedido.getAttribute('COT_ESRECU') == 1) {
        document.getElementById("FAC_ESRECU1").checked = true;
    } else {
        document.getElementById("FAC_ESRECU2").checked = false;
    }
    document.getElementById("FAC_PERIODICIDAD").value = objPedido.getAttribute('COT_PERIODICIDAD');
    document.getElementById("FAC_DIAPER").value = objPedido.getAttribute('COT_DIAPER');
    document.getElementById("VE_ID").value = objPedido.getAttribute('VE_ID');

    var fechaPost = "";
    fechaPost += "&FAC_FECHA=" + objPedido.getAttribute('COT_FECHA');
    fechaPost += "&pd_fech2=" + objPedido.getAttribute('COT_FECHA_SURTIDO');
    fechaPost += "&pd_fechValidez=" + objPedido.getAttribute('COT_FECHA_ENTREGA');
    fechaPost += "&pd_fechValidVer=" + objPedido.getAttribute('COT_FECHA_VALIDEZ');

    $.ajax({
        type: "POST",
        data: fechaPost,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "ERP_PedidosMakProcs.jsp?id=32",
        success: function (datos) {
            var lstXml = datos.getElementsByTagName("pedidos")[0];
            var lstprecio = lstXml.getElementsByTagName("pedidos_deta");
            for (var i = 0; i < lstprecio.length; i++) {
                var objfe = lstprecio[i];

                document.getElementById("FAC_FECHA").value = objfe.getAttribute('FAC_FECHA');
                document.getElementById("pd_fech2").value = objfe.getAttribute('pd_fech2');
                document.getElementById("pd_fechValidez").value = objfe.getAttribute('pd_fechValidez');
                document.getElementById("pd_fechValidVer").value = objfe.getAttribute('pd_fechValidVer');
            }
        },
        error: function (objeto, quepaso, otroobj) {
            alert(":pto9:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }
    });
    document.getElementById("pd_pdManual").value = objPedido.getAttribute('COT_PEDMANUAL');
    document.getElementById("pd_contrato").value = objPedido.getAttribute('COT_CONTRATO');
    document.getElementById("pd_nomSolicit").value = objPedido.getAttribute('COT_SOLICITANTE');
    document.getElementById("pd_cotizaOrigen").value = objPedido.getAttribute('COT_COT_ORIG');
    document.getElementById("pd_ocCliente").value = objPedido.getAttribute('COT_OC_CLIENTE');
    document.getElementById("TR_ID").value = objPedido.getAttribute('TR_ID');
    document.getElementById("pd_vendedor").value = objPedido.getAttribute('VE_ID');
    document.getElementById("FAC_TIENE_FLETE").value = objPedido.getAttribute('COT_TIENE_FLETE');
    document.getElementById("FAC_TIENE_SEGURO").value = objPedido.getAttribute('COT_TIENE_SEGURO');
    document.getElementById("FAC_MONTO_SEGURO").value = objPedido.getAttribute('COT_MONTO_SEGURO');
    document.getElementById("ME_ID").value = objPedido.getAttribute('ME_ID');
    document.getElementById("FAC_TIPO_FLETE").value = objPedido.getAttribute('COT_TIPO_FLETE');

    if (document.getElementById("FAC_TIENE_FLETE").value == 1) {
        document.getElementById("modlgn-flete1").checked = true
    } else {
        document.getElementById("modlgn-flete2").checked = true;
    }

    //Obtenemos el cliente
    cargaClientePediMak(objPedido, lstdeta, strTipoVta);
}

//Actualiza la cantidad de los componentes del paquete([p])
function actualizaComponentesPaquete(grid, idPaqueteM, dblCantidad) {
    var arr = grid.getDataIDs();
    var bolEncontro = false;
    for (var y = 0; y < arr.length; y++) {
        var id = arr[y];
        //Aplica en caso de que encontremos el paquete maestro
        if (bolEncontro) {
            var lstRow = grid.getRowData(id);
            if (lstRow.FACD_ES_COMPONENTE == 0) {
                break;
            } else {
                //Actualizamos la cantidad
                lstRow.FACD_CANTIDAD = parseFloat(lstRow.FACD_CANTIDAD) + (parseFloat(dblCantidad) * lstRow.FACD_MULTIPLO);
                grid.setRowData(id, lstRow);
            }
        } else {
            if (idPaqueteM == id) {
                bolEncontro = true;
            }
        }
    }
}

//Actualiza la cantidad de los componentes del paquete([p])
function borraComponentesPaquetePediMak(grid, idPaqueteM) {
    var arr = grid.getDataIDs();
    var bolEncontro = false;
    for (var y = 0; y < arr.length; y++) {
        var id = arr[y];
        //Aplica en caso de que encontremos el paquete maestro
        if (bolEncontro) {
            var lstRow = grid.getRowData(id);
            if (lstRow.FACD_ES_COMPONENTE == 0) {
                break;
            } else {
                //Borramos partida
                grid.delRowData(id);
            }
        } else {
            if (idPaqueteM == id) {
                bolEncontro = true;
            }
        }
    }
}

function SumaCantComponentes(lstRowPrin) {
    /*Recorremos el grid buscando los componentes*/
    var grid = jQuery("#FAC_GRID");
    var arr = grid.getDataIDs();
    for (var i = 0; i < arr.length; i++) {
        var id = arr[i];
        var lstRowAct = grid.getRowData(id);
        if (lstRowPrin.FACD_PR_ID == lstRowAct.FACD_PR_PAQUETE) {
            lstRowAct.FACD_CANTIDAD = parseFloat(lstRowAct.FACD_MULTIPLO) * parseFloat(lstRowPrin.FACD_CANTIDAD);
            grid.setRowData(id, lstRowAct);
            grid.trigger("reloadGrid");
        }
    }
}

function BloquePantallaMak() {

    document.getElementById('tdIconMensaje').style.display = 'none';
    document.getElementById('tdIconEstCuenta').style.display = 'none';
    document.getElementById('tdIconDatosCliente').style.display = 'none';
    document.getElementById('tdIconBusqClie').style.display = 'none';
    document.getElementById('tdIconBusqSubcliente').style.display = 'none';
    document.getElementById('tdIconBorrSubcliente').style.display = 'none';
//    document.getElementById('tdIconBusqDireccionEntr').style.display = 'none';
//    document.getElementById('tdIconBorrDireccionEntr').style.display = 'none';
    document.getElementById("pd_fechValidez").disabled = true;
    document.getElementById('FAC_MONEDA').disabled = true;
    document.getElementById('pd_bodega').disabled = true;
    document.getElementById('pd_vendedor').disabled = true;
    document.getElementById('TR_ID').disabled = true;
    document.getElementById('pd_fech2').disabled = true;
    document.getElementById('pd_contrato').disabled = true;
    document.getElementById('pd_nomSolicit').disabled = true;
    document.getElementById('pd_ocCliente').disabled = true;
    document.getElementById('pd_pdManual').disabled = true;
    document.getElementById('modlgn-flete1').disabled = true;
    document.getElementById('modlgn-flete2').disabled = true;
    document.getElementById('FAC_PROD').disabled = true;
    document.getElementById('FCT_DESCUENTO').disabled = true;
    document.getElementById('tsTextCaptProductos').style.display = 'none';
    document.getElementById('tsIconExist').style.display = 'none';
    document.getElementById('tsIconModifPrecio').style.display = 'none';
    document.getElementById('tsIconBorrarItem').style.display = 'none';
    //document.getElementById('tsIconInformacion').style.display = 'none';
    //document.getElementById('tsIconHistorial').style.display = 'none';
    document.getElementById('tsIconConvMoneda').style.display = 'none';

    if (identiPantallaMak == 2 && locestatusCotizaEditMak != "CERRADO") {
        /*Cuando se edita una cotizacion  y no s epuede convertir a pedido ni se puede editar*/
        document.getElementById('tsIconConvCoti').style.display = '';
        document.getElementById('tsIconActivarPantalla').style.display = '';
    }
    if (identiPantallaMak == 1) {
        document.getElementById('tsIconActivarPantalla').style.display = '';
        document.getElementById('tsIconConvFacPed').style.display = 'none';
    }
}



function DesbloqueaPantallaMak() {
    $("#dialogWait").dialog("open");
    //Asignamos la cantidad de Tipo de cambio Mas actual 
    document.getElementById('pd_tipoCamCantidad').value = document.getElementById('FAC_TIPOCAMACT').value;

    document.getElementById('tsIconActivarPantalla').style.display = 'none';
    document.getElementById('tdIconMensaje').style.display = '';
    document.getElementById('tdIconEstCuenta').style.display = '';
    document.getElementById('tdIconDatosCliente').style.display = '';
    document.getElementById('tdIconBusqClie').style.display = '';
    document.getElementById('tdIconBusqSubcliente').style.display = '';
    document.getElementById('tdIconBorrSubcliente').style.display = '';
    document.getElementById("pd_fechValidez").disabled = false;
    var grid = jQuery("#FAC_GRID");
    var arr = grid.getDataIDs();
    if (arr.length == 0) {
        document.getElementById("pd_bodega").disabled = false;
        document.getElementById("FCT_DESCUENTO").disabled = false;
    }
    document.getElementById('pd_vendedor').disabled = false;
    document.getElementById('TR_ID').disabled = false;
    document.getElementById('pd_fech2').disabled = false;
    document.getElementById('pd_contrato').disabled = false;
    document.getElementById('pd_nomSolicit').disabled = false;
    document.getElementById('pd_ocCliente').disabled = false;
    document.getElementById('pd_pdManual').disabled = false;
    document.getElementById('modlgn-flete1').disabled = false;
    document.getElementById('modlgn-flete2').disabled = false;
    document.getElementById('FAC_PROD').disabled = false;
    document.getElementById('tsTextCaptProductos').style.display = '';
    document.getElementById('tsIconExist').style.display = '';
    document.getElementById('tsIconModifPrecio').style.display = '';
    document.getElementById('tsIconBorrarItem').style.display = '';
    document.getElementById('tsIconInformacion').style.display = '';
    document.getElementById('tsIconHistorial').style.display = '';
    document.getElementById('tsIconConvMoneda').style.display = '';

    if (identiPantallaMak == 2 && locestatusCotizaEditMak != "CERRADO") {
        document.getElementById('tsIconConvCoti').style.display = '';
    }

    /*Verificamos que se este editando un pedido para mostrar el boton de pagos*/
    if (typeof idPedidoEditConsultMak != "undefined") {
        if (idPedidoEditConsultMak != "" && idPedidoEditConsultMak != "0") {
            document.getElementById('tsIconPagos').style.display = '';
        }
    }
    /*Este boton solo se muestra cuando es pedidos*/
    if (identiPantallaMak == 1) {
        /*Verificamos el boton facturar pedido*/
        verifConvPedFacPediMak();
    }
    RecalculaPreciosMak();
}

function RecalculaPreciosMak() {
    /*
     * 
     -  Recorrer todas las partidas y guardamos en un campo separado por comas el id del producto, en otro la cantidad, excepto componentes de paquete
     - Mandar post a precios opción 8
     -  Recorrer todas las partidas 
     - Leer xml
     - Buscar el precio
     - Recalcular los precios lstRowRecaculaPrecioPediMak(lstRow, idUpdate, grid)
     
     * 
     */

    var IdsProductos = "";
    var CantidadesProductos = "";
    var strComa = "";
    var strPost = "";
    var grid = jQuery("#FAC_GRID");
    var arr = grid.getDataIDs();
    for (var i = 0; i < arr.length; i++) {
        var id = arr[i];
        var lstRowAct = grid.getRowData(id);
        if (lstRowAct.FACD_ES_COMPONENTE == "0") {
            strComa = ",";
            IdsProductos += lstRowAct.FACD_PR_ID + strComa;
            CantidadesProductos += lstRowAct.FACD_CANTIDAD + strComa;
        }
    }

    strPost += "&PR_ID=" + IdsProductos;
    strPost += "&CANTIDAD=" + CantidadesProductos;
    strPost += "&CT_LPRECIOS=" + document.getElementById("FCT_LPRECIOS").value;
    strPost += "&FAC_MONEDA=" + document.getElementById("FAC_MONEDA").value;
    strPost += "&CT_TIPO_CAMBIO=" + document.getElementById("pd_tipoCam").value;
    $.ajax({
        type: "POST",
        data: strPost,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "DamePrecio.do?id=8",
        success: function (datos) {
            var lstXml = datos.getElementsByTagName("PreciosMaster")[0];
            var lstprecio = lstXml.getElementsByTagName("Precios");
            for (var j = 0; j < lstprecio.length; j++) {
                var obj = lstprecio[j].getElementsByTagName("Precio")[0];
                var grid = jQuery("#FAC_GRID");
                var arr = grid.getDataIDs();
                for (var i = 0; i < arr.length; i++) {
                    var id = arr[i];
                    var lstRowAct = grid.getRowData(id);
                    if (lstRowAct.FACD_PR_ID == obj.getAttribute("pr_id")) {
                        lstRowRecaculaPrecioPediMak(lstRowAct, id, grid, obj.getAttribute("precioUsar"));
                    }
                }
            }
            $("#dialogWait").dialog("close");
            $("#dlgMakFletes").dialog("close");
        },
        error: function (objeto, quepaso, otroobj) {
            alert(":ptoExist:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }
    });
}

/**Vuelva a calcular el precio para una fila del grid*/
function lstRowRecaculaPrecioPediMak(lstRow, idUpdate, grid, dblPrecioNvo) {
    lstRow.FACD_PRECIO = dblPrecioNvo;
    lstRow.FACD_PRECREAL = dblPrecioNvo;
    var objImportes = new _ImporteVtaPediMak();
    objImportes.dblCantidad = parseFloat(lstRow.FACD_CANTIDAD);

    objImportes.dblPrecio = parseFloat(lstRow.FACD_PRECIO.replace(",", ""));
    objImportes.dblPrecioReal = parseFloat(lstRow.FACD_PRECREAL.replace(",", ""));
    objImportes.dblPuntos = parseFloat(lstRow.FACD_PUNTOS_U);
    objImportes.dblVNegocio = parseFloat(lstRow.FACD_NEGOCIO_U);
    /*Obtenemos el valor del descuento*/
    var intDescIdx = d.getElementById("FCT_DESCUENTO").selectedIndex;
    var dblDesc = 0;
    if (intDescIdx > 0) {
        dblDesc = parseFloat(d.getElementById("FCT_DESCUENTO").options[intDescIdx].text);
    }
    objImportes.dblPorcDescGlobal = dblDesc;
    objImportes.dblPorcDesc = lstRow.FACD_PORDESC;
    objImportes.dblPrecFijo = lstRow.FACD_PRECFIJO;
    objImportes.dblExento1 = lstRow.FACD_EXENTO1;
    objImportes.dblExento2 = lstRow.FACD_EXENTO2;
    objImportes.dblExento3 = lstRow.FACD_EXENTO3;
    objImportes.intDevo = lstRow.FACD_ESDEVO;
    objImportes.intPrecioZeros = lstRow.FACD_SINPRECIO;
    if (lstRow.FACD_DESC_PREC == 0)
        objImportes.bolAplicDescPrec = false;
    if (lstRow.FACD_DESC_PTO == 0)
        objImportes.bolAplicDescPto = false;
    if (lstRow.FACD_DESC_VN == 0)
        objImportes.bolAplicDescVNego = false;
    //if(lstRow.FACD_DESC_LEAL == 0)objImportes.bolAplicDescPrec= false;
    //Evaluamos si aplican los puntos y valor negocio de multinivel
    var bolAplicaMLM = true;
    if (document.getElementById("FAC_ES_MLM1") != null && document.getElementById("FAC_ES_MLM2") != null) {
        if (document.getElementById("FAC_ES_MLM2").checked)
            bolAplicaMLM = false;
    }
    objImportes.bolUsoMLM = bolAplicaMLM;
    //Evaluamos si aplican los puntos y valor negocio de multinivel
    objImportes.CalculaImportePediMak();
    //Asignamos nuevos importes
    lstRow.FACD_IMPORTE = objImportes.dblImporte;
    lstRow.FACD_IMPUESTO1 = objImportes.dblImpuesto1;
    lstRow.FACD_IMPUESTO2 = objImportes.dblImpuesto2;
    lstRow.FACD_IMPUESTO3 = objImportes.dblImpuesto3;
    lstRow.FACD_PORDESC = objImportes.dblPorcAplica;
    lstRow.FACD_DESCUENTO = FormatNumber(objImportes.dblImporteDescuento, 2, true, false, true, false);
    lstRow.FACD_IMPORTEREAL = objImportes.dblImporteReal;
    lstRow.FACD_PUNTOS = objImportes.dblPuntosImporte;
    lstRow.FACD_NEGOCIO = objImportes.dblVNegocioImporte;
    if (lstRow.FACD_NEGO_ZERO == 1)
        lstRow.FACD_NEGOCIO = 0;


    /*Multiplos*/
    /*Verificamos que el producto tenga la bandera de multiplos*/
    calculaMultiplos(lstRow);

    //Actualizamos el grid
    grid.setRowData(idUpdate, lstRow);

    setSumPediMak();
}

function calculaMultiplos(lstRow) {
    if (lstRow.FACD_ES_MULTIPLO == 1) {
        var dblPrecioMul = parseFloat(lstRow.FACD_PRECIO_MULTIPLOS.replace(",", ""));
        var cantidad = lstRow.FACD_CANTIDAD;
        var dblPrecio = lstRow.FACD_PRECREAL;
        var cantMultiplo = lstRow.FACD_MPV;
        var cantMultPos = 0;
        var cantNormales = 0;
        if (cantMultiplo != 0) {
            cantMultPos = cantidad / cantMultiplo;
            cantNormales = cantidad % cantMultiplo;
        } else {
            cantMultPos = cantidad;
        }
        cantMultPos = Math.floor(cantMultPos);
        var precioMultiplo = dblPrecioMul * cantMultPos;
        var precioNormal = cantNormales * dblPrecio;
        var precProrrateo = precioMultiplo + precioNormal;//IMPORTE
        var precMostrar = precProrrateo / cantidad;

        lstRow.FACD_PRECIO = precMostrar;
        lstRow.FACD_CANTIDAD_MULTIPLOS = cantMultPos;
        lstRow.FACD_PRECIO_TOT_MULTIPLOS = precioMultiplo;
        lstRow.FACD_IMPORTE = precProrrateo;

    }
}

function ConvertirMonedaMak() {

    var intOpcMoneda = document.getElementById('FAC_MONEDA').value;
    if (intOpcMoneda != "0") {
        if (intOpcMoneda == "1" || intOpcMoneda == "2") {
            if (intOpcMoneda == "1") {
                var objSecModiVta = objMap.getScreen("SEL_MONEDA2MAK");
                if (objSecModiVta != null) {
                    objSecModiVta.bolActivo = false;
                    objSecModiVta.bolMain = false;
                    objSecModiVta.bolInit = false;
                    objSecModiVta.idOperAct = 0;
                }
                OpnOpt('SEL_MONEDA2MAK', '_ed', 'dlgMakFletes', false, false, true);
            } else {
                var objSecModiVta = objMap.getScreen("SEL_MONEDA3MAK");
                if (objSecModiVta != null) {
                    objSecModiVta.bolActivo = false;
                    objSecModiVta.bolMain = false;
                    objSecModiVta.bolInit = false;
                    objSecModiVta.idOperAct = 0;
                }
                OpnOpt('SEL_MONEDA3MAK', '_ed', 'dlgMakFletes', false, false, true);
            }

        } else {
            alert("Solo se puede convertir de dolares a pesos o viceversa.");
            $("#dlgMakFletes").dialog("close");
        }
    } else {
        alert("No tiene seleccionado ninguna moneda.");
        $("#dlgMakFletes").dialog("close");
    }
}

function CambiaMonCierrMak() {
    $("#dlgMakFletes").dialog("close");
}

function CambiaMonedaMak() {
    $("#dialogWait").dialog("open");
    var grid = jQuery("#FAC_GRID");
    var arr = grid.getDataIDs();
    if (arr.length != 0) {

        var intMonConv = 0;
        if (document.getElementById("FAC_MONEDA").value == "1") {
            intMonConv = 2;
        } else {
            if (document.getElementById("FAC_MONEDA").value == "2") {
                intMonConv = 1;
            }
        }

        var strPost = "&CT_ID=" + document.getElementById("FCT_ID").value;
        strPost += "&strMoneda=" + intMonConv;
        $.ajax({
            type: "POST",
            data: strPost,
            scriptCharset: "utf-8",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "XML",
            url: "ERP_PedidosMakProcs.jsp?id=46",
            success: function (datos) {
                var objsc = datos.getElementsByTagName("credito")[0];
                var lstProds = objsc.getElementsByTagName("credito_deta");
                var dblMontoCredito = 0;
                for (var i = 0; i < lstProds.length; i++) {
                    var obj = lstProds[i];
                    dblMontoCredito = obj.getAttribute('dblCreditoTotal');
                }
                document.getElementById("FCT_MONTOCRED").value = FormatNumber(dblMontoCredito, 2, true, false, true, false);

                document.getElementById("FAC_MONEDA").value = intMonConv;
                document.getElementById("FAC_MONEDA").disabled = true;
                document.getElementById("FAC_MONEDA").style.background = "#C2C2C2";
                document.getElementById("FAC_MONEDA").style.color = "black";
                setTimeout("RecalculaPreciosMak();", 1000);
                $("#dlgMakFletes").dialog("open");
            },
            error: function (objeto, quepaso, otroobj) {
                alert(":pto:" + objeto + " " + quepaso + " " + otroobj);
            }
        });
    } else {
        alert("No hay elementos agregados al grid.");
        $("#dialogWait").dialog("close");
    }
}

function ShowFletesMak() {
    var objSecModiVta = objMap.getScreen("FLETES_MAK");
    if (objSecModiVta != null) {
        objSecModiVta.bolActivo = false;
        objSecModiVta.bolMain = false;
        objSecModiVta.bolInit = false;
        objSecModiVta.idOperAct = 0;
    }
    OpnOpt('FLETES_MAK', '_ed', 'dlgMakFletes', false, false, true);
}

function initFletesMak() {

    //Obtenemos la moneda del documento
    document.getElementById("PF_MONEDA").value = document.getElementById("FAC_MONEDA").value;
    //Obtenemos el subtotal del documento
    if (document.getElementById("FAC_IMPORTE").value == "") {
        document.getElementById("PF_MONTO").value = "0.0";
    } else {
        document.getElementById("PF_MONTO").value = document.getElementById("FAC_IMPORTE").value;
    }


    var strSeEdita = 0;
    if (typeof idPedidoEditConsultMak != "undefined") {
        if (idPedidoEditConsultMak != "" && idPedidoEditConsultMak != "0") {
            strSeEdita = 1;
        }
    }
    if (typeof idCotizaEditMak != "undefined") {
        if (idCotizaEditMak != "" && idCotizaEditMak != "0") {
            strSeEdita = 1;
        }
    }
    if (strSeEdita == 1) {
        if (document.getElementById("FAC_TIENE_SEGURO").value == 1) {
            document.getElementById("PF_ASEGURADO1").value == 1;
            document.getElementById("PF_ASEGURADO2").value == 0;
        } else {
            document.getElementById("PF_ASEGURADO1").value == 0;
            document.getElementById("PF_ASEGURADO2").value == 1;
        }
        document.getElementById("FAC_MONTO_SEGURO").value = document.getElementById("PF_MONTO").value;
        document.getElementById("ME_ID").value = document.getElementById("PF_ME_ID").value;
        document.getElementById("FAC_TIPO_FLETE").value = document.getElementById("PF_TIPO_FLETE").value;
    }
}

function GuardaFletesMak() {
    if (ValidaFlete()) {
        var intAsegurado = 0;
        if (document.getElementById("PF_ASEGURADO1").checked == true) {
            intAsegurado = 1;
        } else {
            intAsegurado = 0;
        }
        document.getElementById("FAC_TIENE_FLETE").value = 1;
        document.getElementById("FAC_TIENE_SEGURO").value = intAsegurado;
        document.getElementById("FAC_MONTO_SEGURO").value = document.getElementById("PF_MONTO").value;
        document.getElementById("ME_ID").value = document.getElementById("PF_ME_ID").value;
        document.getElementById("FAC_TIPO_FLETE").value = document.getElementById("PF_TIPO_FLETE").value;
        $("#dlgMakFletes").dialog("close");
    }
}

function ValidaFlete() {
    if (document.getElementById("PF_ASEGURADO1").checked == true) {
        if (document.getElementById("PF_MONTO").value == 0 || document.getElementById("PF_MONTO").value == "") {
            alert("Capture el Monto Seguro.");
            return false;
        } else {
            return true;
        }
    } else {
        if (document.getElementById("PF_ASEGURADO2").checked == true) {
            return true;
        } else {
            alert("Capture el campo ¿ESTA ASEGURADO? .");
            return false;
        }
    }
    return true;
}

function AddPermisosMak() {
    var idsPermisos = "";
    if (identiPantallaMak == 1) {
        idsPermisos = "921,922,923,930,932,990";
    } else {
        if (identiPantallaMak == 2) {
            idsPermisos = "924,925,926,931,989";
        }
    }
    bolPermConfirmMak = false;
    bolPermConfirmCorteMak = false;
    bolPermCambiarPrecMak = false;
    bolPermDescAdicMak = false;
    bolPermActPediFact = false;
    bolPermModTipocambioCotizacion = false;
    bolPermModTipocambioPedido = false;

    $.ajax({
        type: "POST",
        data: "keys=" + idsPermisos,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "Acceso.do",
        success: function (datos) {
            var objsc = datos.getElementsByTagName("Access")[0];
            var lstKeys = objsc.getElementsByTagName("key");
            for (var i = 0; i < lstKeys.length; i++) {
                var obj = lstKeys[i];
                if (identiPantallaMak == 1) {
                    if (obj.getAttribute('id') == "921" && obj.getAttribute('enabled') == "true") {
                        bolPermConfirmMak = true;
                    }
                    if (obj.getAttribute('id') == "922" && obj.getAttribute('enabled') == "true") {
                        bolPermConfirmCorteMak = true;
                    }
                    if (obj.getAttribute('id') == "923" && obj.getAttribute('enabled') == "true") {
                        bolPermCambiarPrecMak = true;
                    }
                    if (obj.getAttribute('id') == "930" && obj.getAttribute('enabled') == "true") {
                        bolPermDescAdicMak = true;
                    }
                    if (obj.getAttribute('id') == "932" && obj.getAttribute('enabled') == "true") {
                        bolPermActPediFact = true;
                    }
                    if (obj.getAttribute('id') == "990" && obj.getAttribute('enabled') == "true") {
                        bolPermModTipocambioPedido = true;
                    }
                } else {
                    if (identiPantallaMak == 2) {
                        if (obj.getAttribute('id') == "924" && obj.getAttribute('enabled') == "true") {
                            bolPermConfirmMak = true;
                        }
                        if (obj.getAttribute('id') == "925" && obj.getAttribute('enabled') == "true") {
                            bolPermConfirmCorteMak = true;
                        }
                        if (obj.getAttribute('id') == "926" && obj.getAttribute('enabled') == "true") {
                            bolPermCambiarPrecMak = true;
                        }
                        if (obj.getAttribute('id') == "931" && obj.getAttribute('enabled') == "true") {
                            bolPermDescAdicMak = true;
                        }
                        if (obj.getAttribute('id') == "989" && obj.getAttribute('enabled') == "true") {
                            bolPermModTipocambioCotizacion = true;
                        }
                    }
                }
            }

        }
    });
}

function ObtieneDireccionEntrega() {
    if (document.getElementById("FCT_ID").value != 0 || document.getElementById("FCT_ID").value != "") {
        /*Cerramos La pantalla */
        var objSecModiVta = objMap.getScreen("DIRENTREGA_MAK");
        if (objSecModiVta != null) {
            objSecModiVta.bolActivo = false;
            objSecModiVta.bolMain = false;
            objSecModiVta.bolInit = false;
            objSecModiVta.idOperAct = 0;
        }
        OpnOpt('DIRENTREGA_MAK', '_ed', 'dlgMakSubCte', false, false, true);
    } else {
        alert("Seleccione a un cliente.");
    }
}

function initDirEntrMak() {
//    var grid = jQuery("#grd_Direcciones1");
//    grid.setGridParam({
//      url: "CIP_TablaOp.jsp?ID=5&opnOpt=DIRENT_MAK&_search=true&CT_ID=1"
//    });
//    grid.trigger("reloadGrid");

    var itemDirEntPedMak = 0;
    var strPost = "&CT_ID=" + document.getElementById("FCT_ID").value;
    $.ajax({
        type: "POST",
        data: strPost,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "XML",
        url: "ERP_PedidosMakProcs.jsp?id=41",
        success: function (datos) {
            var objsc = datos.getElementsByTagName("cliente")[0];
            var lstprecio = objsc.getElementsByTagName("cliente_deta");
            for (var i = 0; i < lstprecio.length; i++) {
                var obj = lstprecio[i];
                var datarow = {
                    CDE_ID: obj.getAttribute('CDE_ID'),
                    CDE_CALLE: obj.getAttribute('CDE_CALLE'),
                    CDE_COLONIA: obj.getAttribute('CDE_COLONIA'),
                    CDE_LOCALIDAD: obj.getAttribute('CDE_LOCALIDAD'),
                    CDE_MUNICIPIO: obj.getAttribute('CDE_MUNICIPIO'),
                    CDE_ESTADO: obj.getAttribute('CDE_ESTADO'),
                    CDE_CP: obj.getAttribute('CDE_CP'),
                    CDE_NUMERO: obj.getAttribute('CDE_NUMERO'),
                    CDE_NUMINT: obj.getAttribute('CDE_NUMINT'),
                    CDE_TELEFONO1: obj.getAttribute('CDE_TELEFONO1'),
                    CDE_NOMBRE: obj.getAttribute('CDE_NOMBRE'),
                    CDE_EMAIL: obj.getAttribute('CDE_EMAIL'),
                    CDE_DESCRIPCION: obj.getAttribute('CDE_DESCRIPCION'),
                    CT_ID: obj.getAttribute('CT_ID')
                };
                //Anexamos el registro al GRID
                itemDirEntPedMak++;
                jQuery("#grd_Direcciones1").addRowData(itemDirEntPedMak, datarow, "last");
            }
        },
        error: function (objeto, quepaso, otroobj) {
            alert(":pto:" + objeto + " " + quepaso + " " + otroobj);
        }
    });



}

/*Sirve para guardar la nueva direcion de entrega*/
function saveDirEntPediMak() {
    if (document.getElementById("FCT_ID").value == "" || document.getElementById("FCT_ID").value == "0") {
        alert("ATENCIÓN: Primero debes seleccionar al cliente para asignarle mas direcciones de entrega.");
    } else {
        var strPost = "";
        if (validaDatosDirEntr() != 0) {
            $("#dialogWait").dialog("open");
            strPost = "&CDE_CALLE=" + document.getElementById("CDE_CALLE").value;
            strPost += "&CDE_COLONIA=" + document.getElementById("CDE_COLONIA").value;
            strPost += "&CDE_LOCALIDAD=" + document.getElementById("CDE_LOCALIDAD").value;
            strPost += "&CDE_MUNICIPIO=" + document.getElementById("CDE_MUNICIPIO").value;
            strPost += "&CDE_ESTADO=" + document.getElementById("CDE_ESTADO").value;
            strPost += "&CDE_CP=" + document.getElementById("CDE_CP").value;
            strPost += "&CDE_NUMERO=" + document.getElementById("CDE_NUMERO").value;
            strPost += "&CDE_NUMINT=" + document.getElementById("CDE_NUMINT").value;
            strPost += "&CDE_TELEFONO1=" + document.getElementById("CDE_TELEFONO1").value;
            strPost += "&CDE_NOMBRE=" + document.getElementById("CDE_NOMBRE").value;
            strPost += "&CDE_EMAIL=" + document.getElementById("CDE_EMAIL").value;
            strPost += "&CDE_DESCRIPCION=" + document.getElementById("CDE_DESCRIPCION").value;
            strPost += "&CT_ID=" + document.getElementById("FCT_ID").value;
            strPost += "&CDE_PAIS=" + document.getElementById("CDE_PAIS").value;
            $.ajax({
                type: "POST",
                data: strPost,
                scriptCharset: "utf-8",
                contentType: "application/x-www-form-urlencoded;charset=utf-8",
                cache: false,
                dataType: "html",
                url: "ERP_DireccionesEntrega.jsp?ID=2",
                success: function (datos) {
                    if (datos.substring(0, 2) == "OK") {
                        var intIdDirEnt = datos.replace('OK.', '');
                        intIdDirEnt = intIdDirEnt.trim();
                        var strdireccion = document.getElementById("CDE_CALLE").value + " " +
                                document.getElementById("CDE_COLONIA").value + " " +
                                document.getElementById("CDE_MUNICIPIO").value + " " +
                                document.getElementById("CDE_ESTADO").value;
                        document.getElementById("cte_nomDiEn1").value = document.getElementById("CDE_NOMBRE").value;
                        document.getElementById("cte_telDiEn1").value = document.getElementById("CDE_TELEFONO1").value;
                        document.getElementById("cte_emailDiEn1").value = document.getElementById("CDE_EMAIL").value;
                        document.getElementById("cte_direccDiEn1").value = strdireccion;
                        document.getElementById("CT_DIRENTREGA").value = intIdDirEnt;
                    } else {
                        alert(datos);
                    }
                    $("#dialogWait").dialog("close");
                    $("#dialog2").dialog("close");
                    $("#dlgMakSubCte").dialog("close");
                },
                error: function (objeto, quepaso, otroobj) {
                    alert(":pto:" + objeto + " " + quepaso + " " + otroobj);
                }
            });
        }
    }
}

function newDirEntrPediMak() {
    var objSecModiVta = objMap.getScreen("NVO_DIRENTRE");
    if (objSecModiVta != null) {
        objSecModiVta.bolActivo = false;
        objSecModiVta.bolMain = false;
        objSecModiVta.bolInit = false;
        objSecModiVta.idOperAct = 0;
    }
    OpnOpt('NVO_DIRENTRE', '_ed', 'dialog2', false, false, true);
}

function validaDatosDirEntr() {
    var strCalle = document.getElementById("CDE_CALLE");
    var strColonia = document.getElementById("CDE_COLONIA");
    var strLocalidad = document.getElementById("CDE_LOCALIDAD");
    var strMunicipio = document.getElementById("CDE_MUNICIPIO");
    var strEstado = document.getElementById("CDE_ESTADO");
    var strCp = document.getElementById("CDE_CP");
    var strNumero = document.getElementById("CDE_NUMERO");
    var strNumInt = document.getElementById("CDE_NUMINT");
    var strTelefono = document.getElementById("CDE_TELEFONO1");
    var strNombre = document.getElementById("CDE_NOMBRE");
    var strEmail = document.getElementById("CDE_EMAIL");
    var strDesc = document.getElementById("CDE_DESCRIPCION");
    var strCiud = document.getElementById("CDE_CIUDAD");
    if (strCalle.value == "") {
        alert("Atención: Captura la calle");
        strCalle.focus();
        return 0;
    }
    if (strColonia.value == "") {
        alert("Atención: Captura la Colonia");
        strColonia.focus();
        return 0;
    }

    if (strMunicipio.value == "") {
        alert("Atención: Captura el municipio o Delegación");
        strMunicipio.focus();
        return 0;
    }
    if (strEstado.value == "") {
        alert("Atención: Captura el Estado");
        strEstado.focus();
        return 0;
    }
    if (strCp.value == "") {
        alert("Atención: Captura el Codigo Postal");
        strCp.focus();
        return 0;
    }

    if (strCp.strTelefono == "") {
        alert("Atención: Captura algun número telefonico");
        strTelefono.focus();
        return 0;
    }
    if (strCiud.strCiud == "") {
        alert("Atención: Captura la ciudad");
        strCiud.focus();
        return 0;
    }
    return 1;
}

function cancelDirEntPediMak() {
    $("#dialog2").dialog("close");
}

function delDirEntPediMak() {
    var grid = jQuery("#grd_Direcciones1");
    if (grid.getGridParam("selrow") != null) {
        var id = grid.getGridParam("selrow");
        var lstRow = grid.getRowData(id);
        var idDir = lstRow.CDE_ID;
        if (getUsoEntPed(idDir) != true) {
            if (confirm("Atención: Confirma que desea eliminar la dirección")) {
                grid.delRowData(grid.getGridParam("selrow"));
                deleteDirEntDireccion(idDir);
            }
        } else {

            alert("Atención: Esta dirección no puede ser borrada, ya se uso en algunos documentos");
        }
    } else {
        alert("Selecciona un registro el la tabla");
    }
}

/*Nos regresa true o false de si la direccion ya fue usada*/
function getUsoEntPed(idDir) {

    $.ajax({
        type: "POST",
        data: "id_dir=" + idDir,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "html",
        url: "ERP_DireccionesEntrega.jsp?ID=3",
        success: function (datos) {
            if (datos.substring(0, 2) == "OK") {
                return true;
            } else {
                return false;
                alert(datos);
            }
            $("#dialogWait").dialog("close");
        },
        error: function (objeto, quepaso, otroobj) {
            alert(":pto:" + objeto + " " + quepaso + " " + otroobj);
        }
    });
}

function deleteDirEntDireccion(idDir) {
    $.ajax({
        type: "POST",
        data: "dir_id=" + idDir,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "html",
        url: "ERP_DireccionesEntrega.jsp?ID=4",
        success: function (datos) {
            if (datos.substring(0, 2) == "OK") {
                alert("Dirección de entrega Eliminada...");
            } else {

                alert(datos);
            }
            $("#dialogWait").dialog("close");
        },
        error: function (objeto, quepaso, otroobj) {
            alert(":pto:" + objeto + " " + quepaso + " " + otroobj);
        }
    });
}

function aceptarDirEntPediMak() {
    var grid = jQuery('#grd_Direcciones1');
    var id = grid.getGridParam("selrow");
    var lstRow = grid.getRowData(id);
    var strCalle = lstRow.CDE_CALLE;
    var strColonia = lstRow.CDE_COLONIA;
    var strLocalidad = lstRow.CDE_LOCALIDAD;
    var strMunicipio = lstRow.CDE_MUNICIPIO;
    var strEstado = lstRow.CDE_ESTADO;
    if (grid.getGridParam("selrow") != null) {
        document.getElementById("cte_nomDiEn1").value = lstRow.CDE_NOMBRE;
        document.getElementById("cte_telDiEn1").value = lstRow.CDE_TELEFONO1;
        document.getElementById("cte_direccDiEn1").value = strCalle + " " + strColonia + " " + strLocalidad + " " + strMunicipio + " " + strEstado;
        document.getElementById("cte_emailDiEn1").value = lstRow.CDE_EMAIL;
        document.getElementById("CT_DIRENTREGA").value = lstRow.CDE_ID;
        jQuery("#grd_Direcciones").clearGridData();
        $("#dlgMakSubCte").dialog("close");
    } else {
        alert("Selecciona un Subcliente de la tabla");
    }
}

function salirDirEntPediMak() {
    $("#dlgMakSubCte").dialog("close");
}

function LimpiaDireccionEntrega() {
    document.getElementById("cte_nomDiEn").value = "";
    document.getElementById("cte_telDiEn").value = "";
    document.getElementById("cte_emailDiEn").value = "";
    document.getElementById("cte_direccDiEn").value = "";
    document.getElementById("CT_DIRENTREGA").value = "0";
}

function LimpiaDireccionEntregaPop() {
    document.getElementById("cte_nomDiEn1").value = "";
    document.getElementById("cte_telDiEn1").value = "";
    document.getElementById("cte_emailDiEn1").value = "";
    document.getElementById("cte_direccDiEn1").value = "";
}

function ObtenDirEntrega(CDE_ID) {
    intIngDirEnt++;
    $.ajax({
        type: "POST",
        data: "CDE_ID=" + CDE_ID,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "XML",
        url: "ERP_PedidosMakProcs.jsp?id=42",
        success: function (datos) {
            var objsc = datos.getElementsByTagName("cliente")[0];
            var lstProds = objsc.getElementsByTagName("cliente_deta");
            for (var i = 0; i < lstProds.length; i++) {
                var obj = lstProds[i];
                var strCalle = obj.getAttribute('CDE_CALLE');
                var strColonia = obj.getAttribute('CDE_COLONIA');
                var strLocalidad = obj.getAttribute('CDE_LOCALIDAD');
                var strMunicipio = obj.getAttribute('CDE_MUNICIPIO');
                var strEstado = obj.getAttribute('CDE_ESTADO');

                document.getElementById("cte_nomDiEn").value = obj.getAttribute('CDE_NOMBRE');
                document.getElementById("cte_telDiEn").value = obj.getAttribute('CDE_TELEFONO1');
                document.getElementById("cte_direccDiEn").value = strCalle + " " + strColonia + " " + strLocalidad + " " + strMunicipio + " " + strEstado;
                document.getElementById("cte_emailDiEn").value = obj.getAttribute('CDE_EMAIL');
                document.getElementById("CT_DIRENTREGA").value = obj.getAttribute('CDE_ID');

            }
        },
        error: function (objeto, quepaso, otroobj) {
            alert(":pto:" + objeto + " " + quepaso + " " + otroobj);
        }
    });
}

function PagosPedidoPediMak() {
    var objSecModiVta = objMap.getScreen("PAGOSPED_MAK");
    if (objSecModiVta != null) {
        objSecModiVta.bolActivo = false;
        objSecModiVta.bolMain = false;
        objSecModiVta.bolInit = false;
        objSecModiVta.idOperAct = 0;
    }
    OpnOpt('PAGOSPED_MAK', '_ed', 'dlgMakMjs', false, false, true);
}

function InitPagosMak() {
    var strHtml = "<table>";
    strHtml += "<tr>";
    strHtml += "<td class=\"btn_floppy\" ><a href=\"javascript:PagosPedidosMak()\" class=\"sf-with-ul\" title=\"Guardar\"><i class = \"fa fa-floppy-o\" style=\"font-size:50px\" ></i></td></span>&nbsp;&nbsp;";
    strHtml += "<td style=\"visibility:hidden\"id=\"11\"><i class = \"fa fa-hdd-o\" style=\"font-size:50px\" ></i></td>";
    strHtml += "<td class=\"btn_salirped\"><a href=\"javascript:SalirPagosPedMak()\" class=\"sf-with-ul\" title=\"Salir\"><i class = \"fa fa-sign-out\" style=\"font-size:50px\" ></i></td></span>&nbsp;&nbsp;";
    strHtml += "</tr>";
    strHtml += "</table>";
    document.getElementById("DIVPAGOS").innerHTML = strHtml;

    strHtml = "<table>";
    strHtml += "<tr>";
    strHtml += "<td ><a href=\"javascript:BuscClientePagoMak()\" class=\"sf-with-ul\" title=\"Buscar Cliente\"><i class = \"fa fa-search\" style=\"font-size:20px\" ></i></td></span>&nbsp;&nbsp;";
    strHtml += "</tr>";
    strHtml += "</table>";
    document.getElementById("BTNBUSCCLIENTPEDMAK").innerHTML = strHtml;
}

function CambiaBancoCteMakPed() {
    var intBanco = parseInt(document.getElementById("BC_ID").value);
    var strPOST = "&BC_ID=" + intBanco;
    $.ajax({
        type: "POST",
        data: strPOST,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "html",
        url: "ERP_Cobros.jsp?id=7",
        success: function (datos) {
            document.getElementById("MONEDA_BANCO").value = datos;
            BuscaTasaCambioCteMakPedi();
        },
        error: function (objeto, quepaso, otroobj) {
            alert(":ptoExist:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }
    });
}

function ObtenNomCteMakPed() {
    var strPV_ID = document.getElementById("CTE_ID").value;
    var strPOST = "&CTE_ID=" + strPV_ID;
    $("#dialogWait").dialog("open");
    $.ajax({
        type: "POST",
        data: strPOST,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "html",
        url: "ERP_Cobros.jsp?id=6",
        success: function (datos) {
            if (trim(datos) != "") {
                var info = datos.split("|");
                document.getElementById("CT_RAZONSOCIAL").value = info[0];
                document.getElementById("MONEDA_CLIENTE").value = info[1];
                $("#dialogWait").dialog("close");
                BuscaTasaCambioPagoClienteMak();
            } else {
                $("#dialogWait").dialog("close");
            }
        },
        error: function (objeto, quepaso, otroobj) {
            alert(":ptoExist:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }
    });
}

function BuscaTasaCambioCteMakPedi() {
    var intMonedaBanco = document.getElementById("BCO_MONEDA").value;
    var intMonedaSeleccionada = document.getElementById("MONEDA_CLIENTE").value;
    var strfecha = document.getElementById("MC_FECHA").value;
    var strPOST = "&Moneda_1=" + intMonedaBanco;
    strPOST += "&Moneda_2=" + intMonedaSeleccionada;
    strPOST += "&fecha=" + strfecha;
    $.ajax({
        type: "POST",
        data: strPOST,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "ERP_Cobros.jsp?id=9",
        success: function (datos) {
            var objsc = datos.getElementsByTagName("TasaCambio")[0];
            var lstTiks = objsc.getElementsByTagName("TasaCambios");
            var obj = lstTiks[0];
            var dblTC = obj.getAttribute('TC');
            var strOperacion = obj.getAttribute('Operacion');
            document.getElementById("COB_OPERACION").value = strOperacion;
            if (dblTC == 0.0) {
                dblTC = 1.0;
            }
            document.getElementById("TIPO_CAMBIO").value = dblTC;
            BuscaTasaCambioPagoClienteMak();
            $("#dialogWait").dialog("close");
        },
        error: function (objeto, quepaso, otroobj) {
            alert(":ptoExist:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }
    });
}

function SalirPagosPedMak() {
    $("#dlgMakMjs").dialog("close");
}

function  PagosPedidosMak() {
    if (document.getElementById("BC_ID").value != 0) {
        if (document.getElementById("CTE_ID").value != "0" && document.getElementById("CTE_ID").value != "") {
            if (document.getElementById("BCO_MONEDA").value != 0) {
                $("#dialogPagos").dialog("open");
                var strFecha = document.getElementById("MC_FECHA").value;
                var idCliente = document.getElementById("CTE_ID").value;
                var dblAnticipo = document.getElementById("MC_ANTICIPO").value;
                var intMonedaBanco = document.getElementById("BCO_MONEDA").value;
                var dblTasaCambio = document.getElementById("PARIDAD").value;
                var intBancoID = document.getElementById("BC_ID").value;

                var strPOST = "&idTrx=" + 0;
                strPOST += "&COUNT_PAGOS=" + 0;
                strPOST += "&FECHA=" + strFecha;
                strPOST += "&NOTAS=" + "";
                strPOST += "&MONEDA=" + intMonedaBanco;
                strPOST += "&TASAPESO=" + dblTasaCambio;
                strPOST += "&MONTOPAGO=" + dblAnticipo;
                strPOST += "&BC_ID=" + intBancoID;
                strPOST += "&IdCte=" + idCliente;
                strPOST += "&intAnticipo=" + 1;

                $.ajax({
                    type: "POST",
                    data: encodeURI(strPOST),
                    scriptCharset: "utf-8",
                    contentType: "application/x-www-form-urlencoded;charset=utf-8",
                    cache: false,
                    dataType: "html",
                    url: "ERP_Cobros.jsp?id=1",
                    success: function (dato) {
                        if (Left(dato, 3) == "OK.") {
                            var strFechaActual = trim(strHoyFecha);
                            strFechaActual = Right(strFechaActual, 2) + "/" + Mid(strFechaActual, 5, 2) + "/" + Left(strFechaActual, 4);
                            document.getElementById("MC_FECHA").value = strFechaActual;
                            document.getElementById("CTE_ID").value = 0;
                            document.getElementById("BCO_MONEDA").value = 0;
                            document.getElementById("PARIDAD").value = 1;
                            document.getElementById("BC_ID").value = 0;
                            document.getElementById("MC_ANTICIPO").value = 0.0;
                            document.getElementById("CT_RAZONSOCIAL").value = "";
                            alert("Pago Guardado");
                            $("#dialogPagos").dialog("close");
                            SalirPagosPedMak();
                        } else {
                            alert(dato);
                        }
                        $("#dialogWait").dialog("close");
                    },
                    error: function (objeto, quepaso, otroobj) {
                        alert(":pto9:" + objeto + " " + quepaso + " " + otroobj);
                        $("#dialogWait").dialog("close");
                    }
                });
            } else {
                alert("Debes seleccionar una Moneda");
                document.getElementById("BCO_MONEDA").focus();
            }
        } else {
            alert("Debe seleccionar un Proveedor");
            document.getElementById("PV_ID").focus();
        }
    } else {
        alert("Debe seleccionar un banco");
        document.getElementById("BC_ID").focus();
    }
}

function BuscClientePagoMak() {
    /*Activamos esta bandera para detectar en la pantalla de clientes que se esta llamando desde pagos*/
    document.getElementById("PANT_PAGOS").value = 1;
    PopupCtesPediMak();
}

function BuscaTasaCambioPagoClienteMak() {
    $("#dialogWait").dialog("open");
    var intMonedaBanco = document.getElementById("BCO_MONEDA").value;
    var intMonedaSeleccionada = document.getElementById("MONEDA_CLIENTE").value;
    var strfecha = document.getElementById("MC_FECHA").value;
    var strPOST = "&Moneda_1=" + intMonedaBanco;
    strPOST += "&Moneda_2=" + intMonedaSeleccionada;
    strPOST += "&fecha=" + strfecha;
    if (intMonedaBanco == intMonedaSeleccionada) {
        document.getElementById("PARIDAD").disabled = true;
    } else {
        document.getElementById("PARIDAD").disabled = false;
    }
    $.ajax({
        type: "POST",
        data: strPOST,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "ERP_CobrosCuentas.jsp?id=9",
        success: function (datos) {
            var objsc = datos.getElementsByTagName("TasaCambio")[0];
            var lstTiks = objsc.getElementsByTagName("TasaCambios");
            var obj = lstTiks[0];
            var dblTC = obj.getAttribute('TC');
            var strOperacion = obj.getAttribute('Operacion');
            document.getElementById("COB_OPERACION").value = strOperacion;
            datos = parseFloat(datos);
            if (dblTC == 0.0) {
                datos = 1.0;
            }
            document.getElementById("PARIDAD").value = dblTC;
            $("#dialogWait").dialog("close");
        },
        error: function (objeto, quepaso, otroobj) {
            alert(":ptoExist:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }
    });
}

function ConvFacPedidoPediMak() {
    if (document.getElementById("SC_FAC_PEDIDO").value == 1) {
        if (document.getElementById("PD_CONV_FACTURA").value == 0) {
            alert("El pedido se puede Facturar");
            document.getElementById("PD_CONV_FACTURA").value = 1;
        } else {
            if (document.getElementById("PD_CONV_FACTURA").value == 1) {
                alert("El pedido No se puede Facturar");
                document.getElementById("PD_CONV_FACTURA").value = 0;
            }
        }
    } else {
        alert("La bodega no tiene permisos para Convertir a factura");
    }
}

function verifConvPedFacPediMak() {
    if (identiPantallaMak == 1) {
        $.ajax({
            type: "POST",
            data: "&SC_ID=" + document.getElementById("pd_bodega").value,
            scriptCharset: "utf-8",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "xml",
            url: "ERP_PedidosMakProcs.jsp?id=45",
            success: function (datos) {
                var objsc = datos.getElementsByTagName("sucursal")[0];
                var lstProds = objsc.getElementsByTagName("sucursal_deta");
                for (var i = 0; i < lstProds.length; i++) {
                    var obj = lstProds[i];
                    if (obj.getAttribute('SC_FAC_PEDIDO') == 1 && bolPermActPediFact == true) {
                        document.getElementById("SC_FAC_PEDIDO").value = 1;
                        document.getElementById('tsIconConvFacPed').style.display = '';
                        document.getElementById("PD_CONV_FACTURA").value = 0;
                    } else {
                        document.getElementById("SC_FAC_PEDIDO").value = 0;
                        document.getElementById('tsIconConvFacPed').style.display = 'none';
                        document.getElementById("PD_CONV_FACTURA").value = 0;
                    }
                }
                $("#dialogWait").dialog("close");
            },
            error: function (objeto, quepaso, otroobj) {
                alert(":ptoExist:" + objeto + " " + quepaso + " " + otroobj);
                $("#dialogWait").dialog("close");
            }
        });
    }
}

function calcCredIniPediMak() {

    var strPost = "&CT_ID=" + document.getElementById("FCT_ID").value;
    strPost += "&strMoneda=" + document.getElementById("FAC_MONEDA").value;
    $.ajax({
        type: "POST",
        data: strPost,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "XML",
        url: "ERP_PedidosMakProcs.jsp?id=46",
        success: function (datos) {
            var objsc = datos.getElementsByTagName("credito")[0];
            var lstProds = objsc.getElementsByTagName("credito_deta");
            var dblMontoCredito = 0;
            for (var i = 0; i < lstProds.length; i++) {
                var obj = lstProds[i];
                dblMontoCredito = obj.getAttribute('dblCreditoTotal');
            }
            document.getElementById("FCT_MONTOCRED").value = FormatNumber(dblMontoCredito, 2, true, false, true, false);
        },
        error: function (objeto, quepaso, otroobj) {
            alert(":pto:" + objeto + " " + quepaso + " " + otroobj);
        }
    });
}

function VerificaCreditoPediMak() {
    var boolFlag = "true";
    /*Validamos el numero de documentos*/
    if (document.getElementById("BAN_NUMDOCUMENTOS").value == "OK") {
        /*Si el cliente es venta Mostrador no se valida el credito*/
        if (document.getElementById("CT_VENTAMOSTRADOR").value != "1") {
            /*Si el cliente es COD no se valida el credito*/
            if (document.getElementById("CT_COD").value != "1") {
                /*Validamos El numero de Documentos */
                var dblImporte = document.getElementById("FAC_TOT").value;
                dblImporte = dblImporte.replace(",", "");
                var dblCredito = document.getElementById("FCT_MONTOCRED").value;
                dblCredito = dblCredito.replace(",", "");
                if (parseFloat(dblCredito) >= parseFloat(dblImporte)) {
                    boolFlag = "true";
                } else {
                    /*Si aun tiene documentos disponibles no valide el credito*/
                    if (document.getElementById("BAN_NUMDOCUMENTOS").value == "OK") {
                        boolFlag = "true";
                    } else {
                        alert("El credito de el cliente es:" + document.getElementById("FCT_MONTOCRED").value + " No es suficiente para el pedido");
                        boolFlag = "false";
                    }
                }
            } else {
                boolFlag = "true";
            }
        } else {
            boolFlag = "true";
        }
    } else {
        alert("Se excedió el numero de documentos.");
        boolFlag = "false";
    }
    return boolFlag;
}

function validaNumDocuPediMak() {
    /*Validamos el numero de documentos*/
    $.ajax({
        type: "POST",
        data: "&CT_ID=" + document.getElementById("FCT_ID").value,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "ERP_PedidosMakProcs.jsp?id=51",
        success: function (datos) {
            var objsc = datos.getElementsByTagName("pedidos")[0];
            var lstProds = objsc.getElementsByTagName("pedidos_deta");
            for (var i = 0; i < lstProds.length; i++) {
                var obj = lstProds[i];
                document.getElementById("BAN_NUMDOCUMENTOS").value = obj.getAttribute('respuesta');
            }
        },
        error: function (objeto, quepaso, otroobj) {
            alert(":ptoExist:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }
    });
}

function veriPermisosIniPediMak() {
    var bolDisable = false;
    if (identiPantallaMak == 1) {
        if (bolPermModTipocambioPedido) {
            bolDisable = true;
        }
    } else {
        if (identiPantallaMak == 2) {
            if (bolPermModTipocambioCotizacion) {
                bolDisable = true;
            }
        }
    }
    if (bolDisable) {
        var inpCantidadTipCamb = document.getElementById("pd_tipoCamCantidad");
        inpCantidadTipCamb.disabled = false;
        inpCantidadTipCamb.style.backgroundColor = "";
    }
}

//Muestra la pantalla de direcciones de entrega
function showDireccionEntregaPediMak() {
    var objSecModiVta = objMap.getScreen("POP_DIRENTMAK");
    if (objSecModiVta != null) {
        objSecModiVta.bolActivo = false;
        objSecModiVta.bolMain = false;
        objSecModiVta.bolInit = false;
        objSecModiVta.idOperAct = 0;
    }
    OpnOpt('POP_DIRENTMAK', '_ed', 'dialogCte', false, false, true);
}

//Metodo init de la pantalla emergente de direcciones de entrega
function InitDirEntregaPediMak() {

    var strHtml1 = "<table>" +
            "<tr>" +
            "<td ><a href=\"javascript:ObtieneDireccionEntrega();\" class=\"sf-with-ul\" title=\"Busca Direcciones de Entrga\"><i class = \"fa fa-search\" style=\"font-size:20px\" ></i></td></span>&nbsp;&nbsp;" +
            "<td ><a href=\"javascript:LimpiaDireccionEntregaPop();\" class=\"sf-with-ul\" title=\"Limpia Campos\"><i class = \"fa fa-eraser\" style=\"font-size:20px\" ></i></td></span>&nbsp;&nbsp;" +
            "</tr>" +
            "</table>";
    document.getElementById("divButtons1").innerHTML = strHtml1;

    if (intIngDirEnt == 0) {
        intIngDirEnt++;
        $.ajax({
            type: "POST",
            data: "&CT_ID=" + document.getElementById("FCT_ID").value,
            scriptCharset: "utf-8",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "xml",
            url: "ERP_PedidosMakProcs.jsp?id=52",
            success: function (datos) {
                var objsc = datos.getElementsByTagName("cliente")[0];
                var lstProds = objsc.getElementsByTagName("cliente_deta");
                for (var i = 0; i < lstProds.length; i++) {
                    var obj = lstProds[i];
                    document.getElementById("cte_nomDiEn1").value = document.getElementById("CT_NOM").value;
                    document.getElementById("cte_telDiEn1").value = obj.getAttribute('CT_TELEFONO1');
                    document.getElementById("cte_emailDiEn1").value = obj.getAttribute('CT_EMAIL1');
                    document.getElementById("cte_direccDiEn1").value = document.getElementById("cte_direccion").value;
                }
            },
            error: function (objeto, quepaso, otroobj) {
                alert(":ptoExist:" + objeto + " " + quepaso + " " + otroobj);
                $("#dialogWait").dialog("close");
            }
        });
    } else {
        document.getElementById("cte_nomDiEn1").value = document.getElementById("cte_nomDiEn").value;
        document.getElementById("cte_telDiEn1").value = document.getElementById("cte_telDiEn").value;
        document.getElementById("cte_emailDiEn1").value = document.getElementById("cte_emailDiEn").value;
        document.getElementById("cte_direccDiEn1").value = document.getElementById("cte_direccDiEn").value;
    }
}

function aceptarDirEntregaPediMak() {
    document.getElementById("cte_nomDiEn").value = document.getElementById("cte_nomDiEn1").value;
    document.getElementById("cte_telDiEn").value = document.getElementById("cte_telDiEn1").value;
    document.getElementById("cte_emailDiEn").value = document.getElementById("cte_emailDiEn1").value;
    document.getElementById("cte_direccDiEn").value = document.getElementById("cte_direccDiEn1").value;
    salirDirEntregaPediMak();
}

function salirDirEntregaPediMak() {
    $("#dialogCte").dialog("close");
}