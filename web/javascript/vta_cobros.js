/*
 *Esta libreria realiza todas las operaciones de cobranza
 */
var lastSelCobros = 0;

function vta_cobros() {//Funcion necesaria para que pueda cargarse la libreria en automatico
}

/**Realiza todas las operaciones de inicializacion de cobranza*/
function InitCob() {
    $("#dialogWait").dialog("open");
    myLayout.close("west");
    myLayout.close("east");
    myLayout.close("south");
    myLayout.close("north");
    d.getElementById("COB_TRX").focus();
    d.getElementById("COB_TRX").select();
    document.getElementById("TOOLBAR").innerHTML = _drawMenuNewCobros();
    document.getElementById("COB_MONEDA_PAGO").disabled = false;
    document.getElementById("COB_MONEDA_PAGO").setAttribute("class", "outEdit");
    document.getElementById("COB_MONEDA_PAGO").setAttribute("className", "outEdit");
    document.getElementById("COB_FALTA_MAX").disabled = true;
    document.getElementById("ANTICIPO_TOTAL").disabled = true;
    document.getElementById("COB_NOM_CTE").disabled = true;
    document.getElementById("COB_NOCHEQUE").parentNode.parentNode.style.display = "none";
    EsAnticipoCob();
    document.getElementById("TASA_CAMBIO_PAGO").disabled = true;
    document.getElementById("btn1").style.display = 'none';
    itemIdCob = 0;
    $("#dialogWait").dialog("close");
}
/**Se encarga de enviar la peticion de consulta de las ventas*/
var strNomFormCob = "";
var strKeyCob = "";
var strTipoVtaCob = "";
var strNomOrderCob = "";
var itemIdCob = 0;
var strNomFormatPrintCob = "COBROS";
var dblFaltaXPagar = 0.0;
var idAnticipo = 0;
var MonedaAPagar = 0.0;
/**Anade una operacion por cobrar individual con el boton*/
function AddTrxInd() {
    getTrx(d.getElementById("COB_TRX"));
    //SaveCobDo(0);
}
/**Anade una operacion por cobrar individual*/
function AddTrxEvt(event, obj) {
    //Al pulsar enter valida si la transaccion existe
    if (event.keyCode == 13) {
        getTrx(obj);
        //SaveCobDo(0);
    }
}
/**Valida y recupera los valores de la transaccion por pagar*/
function getTrx(obj) {
    //Prefijos dependiendo del tipo de venta
    var strPrefijoMaster = "TKT";
    var strNomDoc = "TICKET";
    strNomOrderCob = "TKT_ID";
    strNomFormCob = "TICKETVIEW";
    strKeyCob = "TKT_ID";
    strTipoVtaCob = "2";
    var strXmlIni = "vta_ticketss";
    if (document.getElementById("COB_TIPO").value == "1") {
        //Factura
        strPrefijoMaster = "FAC";
        strNomFormCob = "FACTVIE";
        strNomOrderCob = "FAC_ID";
        strKeyCob = "FAC_ID";
        strTipoVtaCob = "1";
        strXmlIni = "vta_facturass";
        strNomDoc = "FACTURA";
    }
    if (document.getElementById("COB_TIPO").value == "3") {
        //Pedido
        strPrefijoMaster = "PD";
        strNomFormCob = "PEDIDOVIEW";
        strNomOrderCob = "PD_ID";
        strKeyCob = "PD_ID";
        strTipoVtaCob = "3";
        strXmlIni = "vta_pedidoss";
        strNomDoc = "PEDIDO";
    }
    //resetCobroInd();//Limpiamos los datos del documento a cobrar
    //Por ajax solicitamos los datos de la operacion a cobrar....  
    $.ajax({
        type: "POST",
        data: strPrefijoMaster + "_ID=" + obj.value + "&" + strPrefijoMaster + "_ANULADA=999&EMP_ID=" + intEMP_ID,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "CIP_TablaOp.jsp?ID=2&opnOpt=" + strNomFormCob,
        success: function (datoVal) {
            var objXML = datoVal.getElementsByTagName(strXmlIni)[0];
            if (objXML != null) {
                if (objXML.getAttribute(strPrefijoMaster + '_ANULADA') == 0) {
                    d.getElementById("COB_NOM").value = objXML.getAttribute(strPrefijoMaster + '_RAZONSOCIAL');
                    //d.getElementById("COB_FECHATRX").value = objXML.getAttribute(strPrefijoMaster + '_FECHA');
                    d.getElementById("COB_FOLIO").value = objXML.getAttribute(strPrefijoMaster + '_FOLIO');
                    d.getElementById("COB_TOTTRX").value = FormatNumber(objXML.getAttribute(strPrefijoMaster + '_TOTAL'), intNumdecimal, true);
                    d.getElementById("COB_TOTAL").value = FormatNumber(objXML.getAttribute(strPrefijoMaster + '_SALDO'), intNumdecimal, true);
                    //Mandamos a llamar a las formas de pago
                    if (d.getElementById("TOTALXPAGAR") != null)
                        d.getElementById("TOTALXPAGAR").value = d.getElementById("COB_TOTAL").value;
                    d.getElementById("FPago1").value = 0.0;
                    SaveCobDo();
                } else {
                    alert("EL " + strNomDoc + " " + obj.value + " con folio " + objXML.getAttribute(strPrefijoMaster + '_FOLIO') + " ESTA ANULADO");
                }
            } else {
                alert("EL " + strNomDoc + " " + obj.value + " NO EXISTE");
            }

        },
        error: function (objeto, quepaso, otroobj) {
            alert(":ptoExist:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }
    });
    d.getElementById("COB_TRX").focus();
    d.getElementById("COB_TRX").select();
}
/**Limpiamos los datos del documento a cobrar*/
function resetCobroInd() {
    //Limpiamos resultados
    //document.getElementById("COB_BCO").value = 0;
    document.getElementById("COB_NOM").value = "";
    document.getElementById("COB_FECHATRX").value = "";
    document.getElementById("COB_CONCEPTO").value = "";
    document.getElementById("FORMPAG").value = "0";
    document.getElementById("COB_FOLIO").value = "";
    document.getElementById("COB_NOCHEQUE").value = "";
    document.getElementById("COB_TOTTRX").value = "0.0";
    document.getElementById("COB_TOTAL").value = "0.0";
    document.getElementById("COB_NOCHEQUE").value = "";
}
/**Limpiamos los datos de la cobranza masiva*/
function resetCobroMas(bolUpdateGrid) {
    d.getElementById("COB_TOTAL").value = 0.0;
    if (bolUpdateGrid) {
        searchTrxMasiva();
    } else {
        jQuery("#COB_GRID1").clearGridData();
    }

}
/**Guarda el pago MOSTRANDO primero las formas de pago*/
function SaveCobSinTimbrar() {
    var Sichecked = document.getElementById("COB_ESANTICIPO1").checked;
    var strFormPago = parseInt(document.getElementById("FORMPAG").value);
    var intMoneda = parseInt(document.getElementById("COB_MONEDA").value);
    var intBancoSel = parseInt(document.getElementById("COB_BCO").value);
    var intMonedaPago = parseInt(document.getElementById("COB_MONEDA_PAGO").value);
    if (strFormPago == "") {
        alert("Falta seleccionar el metodo de pago");
        return false;
    }
    if (intBancoSel != 0) {
        alert(lstMsg[306]);
    }
    if (intMoneda == 0) {
        alert("Falta seleccionar la moneda del documento");
        return false;
    }
    if (intMonedaPago == 0) {
        alert("Falta seleccionar la moneda de pago");
        return false;
    }
    SaveCobDo(0);
    return true;
}
/**Guarda el pago MOSTRANDO primero las formas de pago*/
function SaveCob() {
    var Sichecked = document.getElementById("COB_ESANTICIPO1").checked;
    var strFormPago = parseInt(document.getElementById("FORMPAG").value);
    var intMoneda = parseInt(document.getElementById("COB_MONEDA").value);
    var intBancoSel = parseInt(document.getElementById("COB_BCO").value);
    var intMonedaPago = parseInt(document.getElementById("COB_MONEDA_PAGO").value);
    if (strFormPago == "") {
        alert("Falta seleccionar el metodo de pago");
        return false;
    }
    if (intBancoSel != 0) {
        alert(lstMsg[306]);
    }
    if (intMoneda == 0) {
        alert("Falta seleccionar la moneda del documento");
        return false;
    }
    if (intMonedaPago == 0) {
        alert("Falta seleccionar la moneda de pago");
        return false;
    }
    SaveCobDo(1);
    return true;
}
/**Guarda el pago*/
function SaveCobDo(Timbrar) {
    $("#dialogWait").dialog("open");
    var dblDev = 0;
    var dblPagado = 0;
    var dblxCobrar = 0;
    var strPOST = "";
    var $tabs = $('#tabsCOBROS').tabs();
    var selected = $tabs.tabs('option', 'active');
    //Si el tipo de cobranza es 1 es por transaccion sino
    var intIdOpcionOnly = 0;
    if (document.getElementById("FIRST_ONLY_ONE") != null) {
        intIdOpcionOnly = document.getElementById("FIRST_ONLY_ONE").value;
    }
    if (selected == intIdOpcionOnly) {
        dblDev = 0.0;
        dblPagado = parseFloat(d.getElementById("COB_TOTAL").value) - dblDev;
        //GENERAMOS peticion POST
        strPOST = "idTrx=" + d.getElementById("COB_TRX").value;
        strPOST += "&Timbrar=" + Timbrar;
        strPOST += "&TipoDoc=" + encodeURIComponent(d.getElementById("COB_TIPO").value);
        strPOST += "&MONTOPAGO=" + dblPagado;
        strPOST += "&FECHA=" + (d.getElementById("COB_FECHATRX").value);
        strPOST += "&BC_ID=" + (d.getElementById("COB_BCO").value);
        strPOST += "&NOTAS=" + encodeURIComponent(d.getElementById("COB_CONCEPTO").value);
        strPOST += "&MONEDA=1";
        strPOST += "&TASAPESO=1";
        strPOST += "&MC_METODODEPAGO=PPD";
        strPOST += "&MC_FORMADEPAGO=" + document.getElementById("FORMPAG").value;

        //Pagos Mandamos las 5 formas de pago
        strPOST += "&COUNT_PAGOS=1";
        //efectivo
        strPOST += "&MCD_MONEDA1=1";
        strPOST += "&MCD_FOLIO1=";
        strPOST += "&MCD_FORMAPAGO1=" + document.getElementById("FORMPAG").value;
        if (document.getElementById("FORMPAG") == "02") {
            strPOST += "&MCD_NOCHEQUE1=" + d.getElementById("COB_NOCHEQUE").value;
        } else {
            strPOST += "&MCD_NOCHEQUE1=";
        }
        strPOST += "&MCD_BANCO1=";
        strPOST += "&MCD_NOTARJETA1=";
        strPOST += "&MCD_TIPOTARJETA1=";
        strPOST += "&MCD_IMPORTE1=" + (dblPagado);
        strPOST += "&MCD_TASAPESO1=1";
        strPOST += "&MCD_CAMBIO1=0";

        //Desactivamos los botones
        document.getElementById("COB_BTN4").disabled = true;
        document.getElementById("TOOLBAR").style.display = "none";

        //Hacemos la peticion por POST
        $.ajax({
            type: "POST",
            data: encodeURI(strPOST),
            scriptCharset: "utf-8",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "html",
            url: "Cobros.do?id=1",
            success: function (dato) {
                dato = trim(dato);
                if (Left(dato, 3) == "OK.") {
                    openFormat("COBROS", "PDF", CreaHidden("MC_ID", dato.replace("OK.", "")))
                    resetCobroInd();

                    d.getElementById("COB_TRX").value = 0;
                    d.getElementById("COB_TRX").focus();
                    d.getElementById("COB_TRX").select();

                    $("#dialogPagos").dialog("close");
                } else {
                    alert(dato);
                }
                //Habilitamos botones
                document.getElementById("COB_BTN4").disabled = false;
                document.getElementById("TOOLBAR").style.display = "block";
                $("#dialogWait").dialog("close");
            },
            error: function (objeto, quepaso, otroobj) {
                alert(":PAGOS:" + objeto + " " + quepaso + " " + otroobj);
                $("#dialogWait").dialog("close");
            }
        });
    } else {
        var intIdOper = 3;
        var grid = jQuery("#COB_GRID1");
        var arr = grid.getDataIDs();
        var Sichecked = document.getElementById("COB_ESANTICIPO1").checked;
        ////var arr = grid.getGridParam("selarrrow");
        //alert(arr.length);
        var intContaPays = 0;
        for (var i = 0; i < arr.length; i++) {
            var id = arr[i];
            var lstRow = grid.getRowData(id);
            if (lstRow.COBD_CTAS_IMPORTE > 0.0)
            {
                intContaPays++;
            }

        }
        //alert(intContaPays);
        //Pago masivo de ventas....
        if (Sichecked)
        {
            dblDev = 0.0;
            dblPagado = parseFloat(d.getElementById("COB_TOTAL2").value);
            dblxCobrar = parseFloat(d.getElementById("COB_TOTAL2").value);
        } else
        {
            dblDev = 0.0;
            dblPagado = parseFloat(d.getElementById("COB_TOTAL2").value);
            dblxCobrar = parseFloat(d.getElementById("COB_TOTAL2").value);
        }
        if ((dblPagado == dblxCobrar || ((dblPagado != dblxCobrar) && intContaPays == 1)) && dblxCobrar > 0) {
            //Armamos peticion para guardar los cobros
            strPOST += "FECHA=" + (d.getElementById("COB_FECHA").value);
            strPOST += "&Timbrar=" + Timbrar;
            strPOST += "&BC_ID=" + (d.getElementById("COB_BCO").value);
            strPOST += "&NOTAS=" + encodeURIComponent(d.getElementById("COB_CONCEPTO").value);
            strPOST += "&MONEDA=" + (d.getElementById("TIPO_MONEDA_BANC").value);
            strPOST += "&TASAPESO=" + (d.getElementById("TASA_CAMBIO_PAGO").value);
            strPOST += "&MONTOPAGOTOTAL=" + dblPagado;
            strPOST += "&MC_METODODEPAGO=PPD";
            strPOST += "&MC_FORMADEPAGO=" + document.getElementById("FORMPAG").value;
            strPOST += "&TASAPESO=1";
            strPOST += "&CTE_ID=" + (d.getElementById("COB_CTE").value);

            //strPOST += "&MONTOPAGOTOTALAMONEDA=" + (d.getElementById("COB_CTAS_TOTAL").value);
            strPOST += "&MONTOPAGOTOTALAMONEDA=" + (d.getElementById("COB_TOTAL2").value);
            strPOST += "&MONEDAAPAGAR=" + (d.getElementById("COB_MONEDA_PAGO").value);
            //GENERAMOS peticion POST
            for (var i = 0; i < arr.length; i++) {
                var id = arr[i];
                var lstRow = grid.getRowData(id);
                if (lstRow.COBD_CTAS_IMPORTE > 0.0)
                {

                    strPOST += "&idTrx=" + lstRow.COBD_ID;
                    strPOST += "&TipoDoc=" + encodeURIComponent((lstRow.COBD_TIPO == "FACTURA") ? 1 : 2);
                    strPOST += "&MONTOPAGO=" + lstRow.COBD_CTAS_IMPORTE;
                }
            }
            //Validamos si solo esta pagando una operacion para guardar un pago normal
            var strNomForm = "COBROSMAS";
            var strNomField = "MCM_ID";
            /*if (arr.length == 1) {
             intIdOper = 1;
             strNomForm = "COBROS";
             strNomField = "MC_ID";
             }*/


            if (Sichecked)
            {

                strPOST += "&USA_ANTI=" + 1;
                strPOST += "&ANTI_ID=" + idAnticipo;
                strPOST += "&CANTIDAD_ANTI=" + (d.getElementById("ANTICIPO_USO").value);
                strPOST += "&MCD_IMPORTE1=" + 0.0;
                //Pagos Mandamos las 4 formas de pago
                strPOST += "&COUNT_PAGOS=0";
            } else
            {
                if (d.getElementById("COB_FALTA_MAX").value > 0.0)
                {
                    strPOST += "&CTE_ID=" + (d.getElementById("COB_CTE").value);
                    strPOST += "&CTE_ANTICIPO=" + (d.getElementById("COB_FALTA_MAX").value);
                }
                //Pagos Mandamos las 4 formas de pago
                strPOST += "&COUNT_PAGOS=1";
                //efectivo                
                strPOST += "&MCD_MONEDA1=1";
                strPOST += "&MCD_FOLIO1=";
                strPOST += "&MCD_FORMAPAGO1=" + document.getElementById("FORMPAG").value;
                if (document.getElementById("FORMPAG") == "02") {
                    strPOST += "&MCD_NOCHEQUE1=" + d.getElementById("COB_NOCHEQUE").value;
                } else {
                    strPOST += "&MCD_NOCHEQUE1=";
                }
                strPOST += "&MCD_BANCO1=" + d.getElementById("COB_BCO").value;
                strPOST += "&MCD_NOTARJETA1=";
                strPOST += "&MCD_TIPOTARJETA1=";
                strPOST += "&MCD_IMPORTE1=" + (parseFloat(d.getElementById("COB_TOTAL2").value));
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
                url: "Cobros.do?id=" + intIdOper,
                success: function (dato) {
                    dato = trim(dato);
                    if (Left(dato, 3) == "OK.") {
                        openFormat(strNomForm, "PDF", CreaHidden(strNomField, dato.replace("OK.", "")))
                        jQuery("#COB_GRID1").clearGridData();
                        if (Sichecked)
                        {
                            CargaAnticiposCob();
                            d.getElementById("COB_ESANTICIPO1").checked = true;
                        } else
                        {
                            //d.getElementById("Ef_1").value = 0.0;
                        }
                        document.getElementById("COB_MAX_PAGO").value = "0.0";
                        document.getElementById("COB_FALTA_MAX").value = "0.0";
                        document.getElementById("ANTICIPO_TOTAL").value = "0.0";
                        document.getElementById("ANTICIPO_USO").value = "0.0";
                        document.getElementById("COB_TOTAL2").value = "0.0";
                        itemIdCob = 0;
                        resetCobroMas(true);
                        //LimpiarGrid();
                        $("#dialogPagos").dialog("close");
                        resetCobroInd();
                    } else {
                        alert(dato);
                    }
                    $("#dialogWait").dialog("close");
                },
                error: function (objeto, quepaso, otroobj) {
                    alert(":guada pago:" + objeto + " " + quepaso + " " + otroobj);
                    $("#dialogWait").dialog("close");
                }
            });
        } else {
            alert(lstMsg[10]);
            $("#dialogWait").dialog("close");
        }
    }
}

/**Abre el cuadro de dialogo para buscar cliente o dar de alta uno nuevo*/
function OpnDiagCteCob() {
    OpnOpt('CLIENTES', 'grid', 'dialogCte', false, false);
}

/**Valida y recupera los valores de la transaccion por pagar*/
function searchTrxMasiva(boolBuscaMonedas) {
    /*Evaluamos si se busca la paridad en automatico*/
    var _boolBuscaMonedasCtas = true;
    if (boolBuscaMonedas != null) {
        if (boolBuscaMonedas != undefined) {
            _boolBuscaMonedasCtas = boolBuscaMonedas;
        }
    }
    //Evaluamos si primero se busca las paridades y luego se buscan las operaciones
    if (_boolBuscaMonedasCtas) {
        BuscaTasaCambioCobPago(true);
    } else {
        searchTrxMasivado();
    }
}

function searchTrxMasivado() {
    var Sichecked = document.getElementById("COB_ESANTICIPO1").checked;
    var boolHaceOperacion = true;
    var dblTasaCambio = document.getElementById("TASA_CAMBIO_PAGO").value;
    var strOperacion = document.getElementById("COB_OPERACION").value;
    if (Sichecked)
    {

    } else
    {
        if (d.getElementById("COB_MONEDA_PAGO").value == 0)
        {
            boolHaceOperacion = false;
            alert(lstMsg[305]);
        }
    }
    if (boolHaceOperacion)
    {
        itemIdCob = 0;
        var strCte = d.getElementById("COB_CTE").value;
        var strFecha1 = d.getElementById("COB_FECHAS1").value;
        var strFecha2 = d.getElementById("COB_FECHAS2").value;
        var strMoneda = d.getElementById("COB_MONEDA").value;
        var strPOST = "&TKT_MONEDA=" + strMoneda;
        if (strFecha1 != "")
        {
            strPOST += "&TKT_FECHA1=" + strFecha1;
        }

        if (strFecha2 == "")
        {
            strFecha2 = FechaActualCob();
        }

        strPOST += "&TKT_FECHA2=" + strFecha2;
        ValidaClean("COB_CTE");
        if (strCte == 0) {
            ValidaShow("COB_CTE", "NECESITA SELECCIONAR UN CLIENTE");
        } else {
            $("#dialogWait").dialog("open");
            //limpiamos datos
            strPOST += "&CT_ID=" + strCte;
            resetCobroMas(false);
            $.ajax({
                type: "POST",
                data: strPOST,
                scriptCharset: "utf-8",
                contentType: "application/x-www-form-urlencoded;charset=utf-8",
                cache: false,
                dataType: "xml",
                url: "ERP_Cobros.jsp?id=16",
                success: function (datos) {
                    var objsc = datos.getElementsByTagName("tickets")[0];
                    var lstTiks = objsc.getElementsByTagName("ticket");
                    for (i = 0; i < lstTiks.length; i++) {
                        var obj = lstTiks[i];
                        //Solo mostramos los documentos con saldo
                        var dblPagos = obj.getAttribute('TKT_TOTAL') - obj.getAttribute('TKT_SALDO');
                        var strFecha = obj.getAttribute('TKT_FECHA');
                        strFecha = strFecha.substr(strFecha.length - 2, strFecha.length) + "/" + strFecha.substr(4, 2) + "/" + strFecha.substr(0, 4);
                        var dblSaldoCambio = 0;
                        if (strOperacion == "M") {
                            dblSaldoCambio = parseFloat(obj.getAttribute('TKT_SALDO')) * parseFloat(dblTasaCambio);
                        } else {
                            dblSaldoCambio = parseFloat(obj.getAttribute('TKT_SALDO')) / parseFloat(dblTasaCambio);
                        }
                        var datarow = {
                            COBD_ID: obj.getAttribute('TKT_ID'),
                            COBD_FOLIO: obj.getAttribute('TKT_FOLIO'),
                            COBD_FECHA: strFecha,
                            COBD_TOTAL: obj.getAttribute('TKT_TOTAL'),
                            COBD_PAGOS: dblPagos,
                            COBD_SALDO: obj.getAttribute('TKT_SALDO'),
                            COBD_SALDO_CAMBIO: dblSaldoCambio,
                            COBD_TIPO: "TICKET",
                            COBD_CTAS_IMPORTE: 0.0,
                            COBD_CTAS_IMP_CAMBIO: 0.0,
                            COBD_CTAS_MONEDA: 0
                        };
                        //Anexamos el registro al GRID
                        itemIdCob++;
                        jQuery("#COB_GRID1").addRowData(itemIdCob, datarow, "last");
                    }
                    var objsc = datos.getElementsByTagName("facturas")[0];
                    var lstTiks = objsc.getElementsByTagName("factura");
                    for (i = 0; i < lstTiks.length; i++) {
                        var obj = lstTiks[i];
                        //Solo mostramos los documentos con saldo
                        var dblPagos = obj.getAttribute('FAC_TOTAL') - obj.getAttribute('FAC_SALDO');
                        var strFecha = obj.getAttribute('FAC_FECHA');
                        var dblSaldoCambio = 0;
                        if (strOperacion == "M") {
                            dblSaldoCambio = parseFloat(obj.getAttribute('FAC_SALDO')) * parseFloat(dblTasaCambio);
                        } else {
                            dblSaldoCambio = parseFloat(obj.getAttribute('FAC_SALDO')) / parseFloat(dblTasaCambio);
                        }
                        strFecha = strFecha.substr(strFecha.length - 2, strFecha.length) + "/" + strFecha.substr(4, 2) + "/" + strFecha.substr(0, 4);
                        var datarow = {
                            COBD_ID: obj.getAttribute('FAC_ID'),
                            COBD_FOLIO: obj.getAttribute('FAC_FOLIO'),
                            COBD_FECHA: strFecha,
                            COBD_TOTAL: obj.getAttribute('FAC_TOTAL'),
                            COBD_PAGOS: dblPagos,
                            COBD_SALDO: obj.getAttribute('FAC_SALDO'),
                            COBD_SALDO_CAMBIO: dblSaldoCambio,
                            COBD_TIPO: "FACTURA",
                            COBD_CTAS_IMPORTE: 0.0,
                            COBD_CTAS_IMP_CAMBIO: 0.0,
                            COBD_CTAS_MONEDA: 0
                        };
                        //Anexamos el registro al GRID
                        itemIdCob++;
                        jQuery("#COB_GRID1").addRowData(itemIdCob, datarow, "last");
                    }
                    document.getElementById("ANTICIPO_USO").value = document.getElementById("ANTICIPO_TOTAL").value;
                    document.getElementById("COB_FALTA_MAX").value = document.getElementById("COB_MAX_PAGO").value;
                    document.getElementById("TASA_CAMBIO_PAGO").disabled = false;
                    $("#dialogWait").dialog("close");
                },
                error: function (objeto, quepaso, otroobj) {
                    alert(":ptoExist:" + objeto + " " + quepaso + " " + otroobj);
                    $("#dialogWait").dialog("close");
                }
            });
        }
        document.getElementById("COB_TOTAL2").value = 0.0;
        ValidaMaximoxPagarCob();
    } else
    {

        d.getElementById("COB_MONEDA").focus();
    }

}
/**Se activa cuando seleccionan una operacion y suma el importe*/
function selRowCob(rowid, status) {
    editFila(rowid);
    Recalculo();
}
/**Muestra los pagos hechos*/
function ShowPayments() {
    OpnOpt('COBROSVIEW', 'grid', 'dialog2', false, false);
}

/**Anula el pago seleccionado*/
function PagosAnul() {
    var grid = jQuery("#COBROSVIEW");
    if (grid.getGridParam("selrow") != null) {
        var lstRow = grid.getRowData(grid.getGridParam("selrow"));
        if (lstRow.MC_ANULADO == "NO") {
            $("#SioNO").dialog("open");
            $("#SioNO").dialog('option', 'title', lstMsg[17]);
            _resetSioNoCob("btnSI", "btnNO", "SioNO_inside");
            document.getElementById("btnSI").onclick = function () {
                $("#SioNO").dialog("close");
                PagosAnulDo()
            };
            document.getElementById("btnNO").onclick = function () {
                $("#SioNO").dialog("close")
            };
        }
    }
}
function PagosAnulDo() {
    var grid = jQuery("#COBROSVIEW");
    if (grid.getGridParam("selrow") != null) {
        var lstRow = grid.getRowData(grid.getGridParam("selrow"));
        if (lstRow.MC_ANULADO == "NO") {
            $("#dialogWait").dialog("open");
            var intKey = lstRow.MC_ID;
            var intId = 2;
            var strNomKey = "MC_ID";
            if (lstRow.MCM_ID != 0) {
                intId = 4;
                intKey = lstRow.MCM_ID;
                strNomKey = "MCM_ID";
            }
            //Hacemos la peticion por POST
            $.ajax({
                type: "POST",
                data: encodeURI(strNomKey + "=" + intKey),
                scriptCharset: "utf-8",
                contentType: "application/x-www-form-urlencoded;charset=utf-8",
                cache: false,
                dataType: "html",
                url: "Cobros.do?id=" + intId,
                success: function (dato) {
                    dato = trim(dato);
                    if (Left(dato, 2) == "OK") {
                        grid.trigger("reloadGrid");
                    } else {
                        alert(dato);
                    }
                    $("#dialogWait").dialog("close");
                },
                error: function (objeto, quepaso, otroobj) {
                    alert(":conanul:" + objeto + " " + quepaso + " " + otroobj);
                    $("#dialogWait").dialog("close");
                }
            });
        } else {
            alert(lstMsg[16]);
        }
    }
}
//salir de la pantalla
function SalirCob() {
    $("#dialogWait").dialog("open");
    $("#dialogWait").dialog("close");
    //cerramos los paneles
    myLayout.open("west");
    myLayout.open("east");
    myLayout.open("south");
    myLayout.open("north");
    document.getElementById("MainPanel").innerHTML = "";

    //Limpiamos el objeto en el framework para que nos deje cargarlo enseguida
    var objMainFacPedi = objMap.getScreen("COBROS");
    objMainFacPedi.bolActivo = false;
    objMainFacPedi.bolMain = false;
    objMainFacPedi.bolInit = false;
    objMainFacPedi.idOperAct = 0;
}


//Accion cuando una celda es modifciada del grid
function editGrid(e) {
    if (e.originalEvent.keyCode == 13) {
        actualizarImporteCobro();
    }
}
//Se ejecuta cuando se selecciona un renglon
function editFila(id) {
    var grid = jQuery('#COB_GRID1');
    if (id != lastSelCobros) {
        grid.saveRow(lastSelCobros);
        grid.editRow(id, false);
        lastSelCobros = id;
    }
}
//SE hace el recalculo del total
function Recalculo()
{
    setTimeout(function () {
        var grid = jQuery('#COB_GRID1');
        var arr = grid.getDataIDs();
        var dblTotal = 0.0;
        var dblNumero;
        var dblTasaCambio = parseFloat(document.getElementById("TASA_CAMBIO_PAGO").value);
        var dblTotalAntes = 0.0;
        var strOperacion2 = document.getElementById("COB_OPERACION").value;

        var dblOperacion = 1.0;
        if (strOperacion2 == "M")
        {
            dblOperacion = dblTasaCambio;
            dblTasaCambio = 1 / dblTasaCambio;
        }
        for (var i = 0; i < arr.length; i++) {
            var idRow = arr[i];
            dblNumero = 0.0;
            var lstRow = grid.getRowData(idRow);
//         if (isNaN(lstRow.COBD_CTAS_IMPORTE))
            if (document.getElementById(idRow + "_COBD_CTAS_IMPORTE") != null)
            {
                dblNumero = parseFloat(document.getElementById(idRow + "_COBD_CTAS_IMPORTE").value);
                //alert("OB:"+dblNumero);
                var Sichecked = document.getElementById("COB_ESANTICIPO1").checked;
                if (Sichecked)
                {
                    dblCantidadMaximaPago = document.getElementById("ANTICIPO_TOTAL").value;
                    dblFaltaXPagar = document.getElementById("ANTICIPO_USO").value;
                } else
                {
                    dblCantidadMaximaPago = document.getElementById("COB_MAX_PAGO").value;
                    dblFaltaXPagar = document.getElementById("COB_FALTA_MAX").value;
                }

                var dblImporteAPagar = parseFloat(lstRow.COBD_SALDO) * dblTasaCambio;

                if (dblCantidadMaximaPago <= 0.0)
                {
                    alert("No hay cantidad minima para pagar.");
                } else
                {
                    //Calculamos cuanto sera la cantidad sugerida para pagar
                    //Primero revisamos si lo que nos queda es suficiente para pagar la factura
                    if ((dblFaltaXPagar * dblTasaCambio) > parseFloat(lstRow.COBD_SALDO))
                    {
                        //Hacemos el calculo de lo que tenemos y  lo ponemos de sugerencia
                        if ((dblImporteAPagar > lstRow.COBD_SALDO))
                        {
                            dblImporteAPagar = lstRow.COBD_SALDO / dblTasaCambio * dblOperacion;
                        } else
                        {
                            dblImporteAPagar = lstRow.COBD_SALDO * dblOperacion;
                        }
                    } else
                    {
                        //Si ya no se peude pagar con lo que queda pasa automaticamente
                        dblImporteAPagar = dblFaltaXPagar;
                    }
                }

                document.getElementById(idRow + "_COBD_CTAS_IMPORTE").value = dblImporteAPagar;

            } else
            {
                //dblNumero=100.00;
                if (lstRow.COBD_CTAS_IMPORTE == "")
                {
                    dblNumero = 0.0;
                } else
                {
                    dblNumero = parseFloat(lstRow.COBD_CTAS_IMPORTE);
                }

                //alert(dblNumero);
            }
            dblTotal += dblNumero * (dblTasaCambio);
            dblTotalAntes += dblNumero;
        }

        d.getElementById("COB_TOTAL").value = FormatNumber(dblTotalAntes * dblTasaCambio, 2, true);
        d.getElementById("COB_TOTAL2").value = FormatNumber(dblTotalAntes, 2, true);
        if (dblTotal == 0.0)
        {
            document.getElementById("COB_BCO").disabled = false;
            //document.getElementById("COB_MONEDA_PAGO").disabled = false;
        } else
        {
            //document.getElementById("COB_CTAS_BCO").disabled = true;
            document.getElementById("COB_MONEDA_PAGO").disabled = true;
        }
        /*Calculamos cuanto falta por pagar*/
        if (parseFloat(document.getElementById("COB_MAX_PAGO").value) != 0 && document.getElementById("COB_MAX_PAGO").value != "")
            CalculaFaltaxPagarCob();

        if (parseFloat(document.getElementById("ANTICIPO_TOTAL").value) != 0 && document.getElementById("ANTICIPO_TOTAL").value != "")
            CalculaFaltaxPagarCob();

    }, 300);
}
//SE hace la busqueda del tipo de moneda del banco
function CambiaBanco()
{

    var intBanco = parseInt(document.getElementById("COB_BCO").value);
    //validamos si podemos
    if (intBanco == 0) {
        document.getElementById("COB_MONEDA_PAGO").disabled = false;
        document.getElementById("COB_MONEDA_PAGO").setAttribute("class", "outEdit");
        document.getElementById("COB_MONEDA_PAGO").setAttribute("className", "outEdit");
    } else {
        var strPOST = "&BC_ID=" + intBanco;
        //alert(intBanco);
        $.ajax({
            type: "POST",
            data: strPOST,
            scriptCharset: "utf-8",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "html",
            url: "ERP_Cobros.jsp?id=7",
            success: function (datos) {
                document.getElementById("TIPO_MONEDA_BANC").value = datos;
                document.getElementById("COB_MONEDA_PAGO").value = datos;
                BuscaTasaCambioCobPago();
                document.getElementById("COB_MONEDA_PAGO").disabled = true;
                document.getElementById("COB_MONEDA_PAGO").setAttribute("class", "READONLY");
                document.getElementById("COB_MONEDA_PAGO").setAttribute("className", "READONLY");
            },
            error: function (objeto, quepaso, otroobj) {
                alert(":ptoExist:" + objeto + " " + quepaso + " " + otroobj);
                $("#dialogWait").dialog("close");
            }
        });
    }

}
//Se hace la busqueda de la tasa de cambio de acuerdo al banco y moneda seleccionada
function BuscaTasaCambio()
{
    var intMonedaBanco = document.getElementById("TIPO_MONEDA_BANC").value;
    var intMonedaSeleccionada = document.getElementById("COB_MONEDA").value;
    var strFecha = document.getElementById("COB_FECHA").value;
    var strPOST = "&Moneda_1=" + intMonedaSeleccionada;//intMonedaBanco
    strPOST += "&Moneda_2=" + intMonedaBanco;//intMonedaSeleccionada
    strPOST += "&fecha=" + strFecha;
    if (intMonedaBanco == intMonedaSeleccionada)
    {
        document.getElementById("TASA_CAMBIO_PAGO").value = 1;
        document.getElementById("TASA_CAMBIO_PAGO").disabled = true;
    } else
    {
        document.getElementById("TASA_CAMBIO_PAGO").disabled = false;
        $.ajax({
            type: "POST",
            data: strPOST,
            scriptCharset: "utf-8",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "xml",
            url: "ERP_Cobros.jsp?id=9",
            success: function (datos) {
                //alert(datos);
                var objsc = datos.getElementsByTagName("TasaCambio")[0];
                var lstTiks = objsc.getElementsByTagName("TasaCambios");
                //datos = parseFloat(datos);
                var obj = lstTiks[0];
                var dblTC = obj.getAttribute('TC');
                var strOperacion = obj.getAttribute('Operacion');
                document.getElementById("COB_OPERACION").value = strOperacion;
                //alert(strOperacion);
                if (dblTC == 0.0)
                {
                    dblTC = 1.0;
                }
                document.getElementById("TASA_CAMBIO_PAGO").value = dblTC;
            },
            error: function (objeto, quepaso, otroobj) {
                alert(":ptoExist:" + objeto + " " + quepaso + " " + otroobj);
                $("#dialogWait").dialog("close");
            }
        });
    }
    Recalculo();
}

function actualizarImporteCobro() {
    //var dblTasaCambio = parseFloat(document.getElementById("COB_TASA_CAMBIO").value);
    var grid = jQuery('#COB_GRID1');
    var lstRow = grid.getRowData(lastSelCobros);
    var dblTasaCambio = parseFloat(document.getElementById("TASA_CAMBIO_PAGO").value);
    var strOperacion = document.getElementById("COB_OPERACION").value;
    var dblImportePago = document.getElementById((lastSelCobros) + "_COBD_CTAS_IMPORTE").value;
    var intMonedaPago = parseInt(document.getElementById("COB_MONEDA_PAGO").value);
    var intMonedaDocumento = parseInt(document.getElementById("COB_MONEDA").value);
    if (intMonedaPago != intMonedaDocumento) {
        dblTasaCambio = parseFloat(document.getElementById("TASA_CAMBIO_PAGO").value);
    }
    //alert(dblMonedaAPagar);
    var dblAPagar = 0.0;
    //Validamos el importe del pago
    if ((parseFloat(dblImportePago) < dblFaltaXPagar)) {
        if ((parseFloat(dblImportePago)) <= parseFloat(lstRow.COBD_SALDO_CAMBIO)) {
            dblAPagar = dblImportePago;
        } else {
            //No se puede pagar mas del saldo del documento
            dblAPagar = parseFloat(lstRow.COBD_SALDO_CAMBIO);
        }
    } else {
        //No se puede pagar mas de lo que se puso en importe global de pago
        dblAPagar = dblFaltaXPagar;
    }
    $("#dialogWait").dialog("open");
    lstRow.COBD_CTAS_IMPORTE = dblAPagar;
    if (intMonedaPago == intMonedaDocumento) {
        lstRow.COBD_CTAS_IMP_CAMBIO = dblAPagar;
    } else {
        if (strOperacion == "M") {
            lstRow.COBD_CTAS_IMP_CAMBIO = dblAPagar / dblTasaCambio;
        } else {
            lstRow.COBD_CTAS_IMP_CAMBIO = dblAPagar * dblTasaCambio;
        }
    }
    grid.setRowData(lastSelCobros, lstRow);
    $("#dialogWait").dialog("close");
    grid.saveRow(lastSelCobros);
    Recalculo();
    lastSelCobros = 0;
}


function GuardaAjustaPago()
{
    //alert("Entre a guardar");
    $("#dialogWait").dialog("open");
    var dblDev = 0;
    var dblPagado = 0;
    var dblxCobrar = 0;
    var strPOST = "";


    var intIdOper = 3;
    var grid = jQuery("#COB_GRID1");
    var arr = grid.getDataIDs();//var arr = grid.getGridParam("selarrrow");
    var intContaPays = 0;
    for (var i = 0; i < arr.length; i++) {
        var id = arr[i];
        var lstRow = grid.getRowData(id);
        if (lstRow.COBD_CTAS_IMPORTE > 0.0)
        {
            intContaPays++;
        }

    }
    //Pago masivo de ventas....
    dblDev = 0.0;//parseFloat(d.getElementById("Ef_2").value);
    dblPagado = /*parseFloat(d.getElementById("TOTALPAGADO").value)*/0.0 - dblDev;
    dblxCobrar = parseFloat(d.getElementById("COB_TOTAL").value);
    if (dblxCobrar > 0) {
        //Armamos peticion para guardar los cobros
        var strMoneda = d.getElementById("COB_MONEDA").value;
        strPOST += "FECHA=" + (d.getElementById("COB_FECHA").value);
        //strPOST += "&BC_ID=" + (d.getElementById("COB_BCO").value);
        strPOST += "&NOTAS=" + encodeURIComponent(d.getElementById("COB_CONCEPTO").value);
        strPOST += "&MONEDA=" + strMoneda;
        strPOST += "&TASAPESO=1";
        strPOST += "&MONTOPAGOTOTAL=" + dblPagado;
        //Indicamos que en esta opcion no requerimos el banco con el que es guardado
        strPOST += "&SIN_BANCO=1";
        //GENERAMOS peticion POST
        for (var i = 0; i < arr.length; i++) {
            var id = arr[i];
            var lstRow = grid.getRowData(id);
            if (lstRow.COBD_CTAS_IMPORTE > 0.0)
            {
                strPOST += "&idTrx=" + lstRow.COBD_ID;
                strPOST += "&TipoDoc=" + encodeURIComponent((lstRow.COBD_TIPO == "FACTURA") ? 1 : 2);
                strPOST += "&MONTOPAGO=" + lstRow.COBD_CTAS_IMPORTE;
            }
        }
        //Validamos si solo esta pagando una operacion para guardar un pago normal
        var strNomForm = "COBROSMAS";
        var strNomField = "MCM_ID";
//        if (arr.length == 1) {
//            intIdOper = 1;
//            strNomForm = "COBROS";
//            strNomField = "MC_ID";
//        }
        //Pagos Mandamos las 4 formas de pago
        strPOST += "&COUNT_PAGOS=5";
        //efectivo
        strPOST += "&MCD_MONEDA1=" + strMoneda;
        strPOST += "&MCD_FOLIO1=";
        strPOST += "&MCD_FORMAPAGO1=" + document.getElementById("FORMPAG").value;
        if (document.getElementById("FORMPAG") == "02") {
            strPOST += "&MCD_NOCHEQUE1=" + d.getElementById("COB_NOCHEQUE").value;
        } else {
            strPOST += "&MCD_NOCHEQUE1=";
        }
        strPOST += "&MCD_BANCO1=";
        strPOST += "&MCD_NOTARJETA1=";
        strPOST += "&MCD_TIPOTARJETA1=";
        strPOST += "&MCD_IMPORTE1=" + 0.0;//(parseFloat(d.getElementById("Ef_1").value) - parseFloat(d.getElementById("Ef_2").value));
        strPOST += "&MCD_TASAPESO1=1";
        strPOST += "&MCD_CAMBIO1=" + 0.0;//d.getElementById("Ef_2").value;
        //cheque
        strPOST += "&MCD_MONEDA2=" + strMoneda;
        strPOST += "&MCD_FOLIO2=";
        strPOST += "&MCD_FORMAPAGO2=CHEQUE";
        strPOST += "&MCD_NOCHEQUE2=" + 0.0;//d.getElementById("Bc_2").value;
        strPOST += "&MCD_BANCO2=" + 0.0;//d.getElementById("Bc_3").value;
        strPOST += "&MCD_NOTARJETA2=";
        strPOST += "&MCD_TIPOTARJETA2=";
        strPOST += "&MCD_IMPORTE2=" + 0.0;//d.getElementById("Bc_1").value;
        strPOST += "&MCD_TASAPESO2=1";
        strPOST += "&MCD_CAMBIO2=0";
        //tarjeta de credito
        strPOST += "&MCD_MONEDA3=" + strMoneda;
        strPOST += "&MCD_FOLIO3=";
        strPOST += "&MCD_FORMAPAGO3=TCREDITO";
        strPOST += "&MCD_NOCHEQUE3=";
        strPOST += "&MCD_BANCO3=";
        strPOST += "&MCD_NOTARJETA3=" + 0.0;//d.getElementById("Tj_2").value;
        strPOST += "&MCD_TIPOTARJETA3=" + 0.0;//d.getElementById("Tj_3").value;
        strPOST += "&MCD_IMPORTE3=" + 0.0;//d.getElementById("Tj_1").value;
        strPOST += "&MCD_TASAPESO3=1";
        strPOST += "&MCD_CAMBIO3=0";
        //saldo a favor
        strPOST += "&MCD_MONEDA4=" + strMoneda;
        strPOST += "&MCD_FOLIO4=";
        strPOST += "&MCD_FORMAPAGO4=SALDOFAVOR";
        strPOST += "&MCD_NOCHEQUE4=";
        strPOST += "&MCD_BANCO4=";
        strPOST += "&MCD_NOTARJETA4=";
        strPOST += "&MCD_TIPOTARJETA4=";
        strPOST += "&MCD_IMPORTE4=" + 0.0;//d.getElementById("sf_1").value;
        strPOST += "&MCD_TASAPESO4=1";
        strPOST += "&MCD_CAMBIO4=0";
        //Transferencia
        strPOST += "&MCD_MONEDA5=" + strMoneda;
        strPOST += "&MCD_FOLIO5=";
        strPOST += "&MCD_FORMAPAGO5=TRANSFERENCIA BANCARIA";
        strPOST += "&MCD_NOCHEQUE5=";
        strPOST += "&MCD_BANCO5=" + 0.0;//d.getElementById("tf_2").value;
        strPOST += "&MCD_NOTARJETA5=" + 0.0;//d.getElementById("tf_3").value;
        strPOST += "&MCD_TIPOTARJETA5=";
        strPOST += "&MCD_IMPORTE5=" + 0.0;//d.getElementById("tf_1").value;
        strPOST += "&MCD_TASAPESO5=1";
        strPOST += "&MCD_CAMBIO5=0";
        //Hacemos la peticion por POST

        $.ajax({
            type: "POST",
            data: encodeURI(strPOST),
            scriptCharset: "utf-8",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "html",
            url: "Cobros.do?id=" + intIdOper,
            success: function (dato) {
                dato = trim(dato);
                if (Left(dato, 3) == "OK.") {
                    openFormat(strNomForm, "PDF", CreaHidden(strNomField, dato.replace("OK.", "")))
                    LimpiarGrid();
                    resetCobroMasAjustaPago(true);
                    $("#dialogPagos").dialog("close");
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
        alert(lstMsg[10]);
        $("#dialogWait").dialog("close");
    }
    LimpiarGrid();

}

function CargaAjustaPago()
{
    var strCte = d.getElementById("COB_CTE").value;
    var strFecha1 = d.getElementById("COB_FECHAS1").value;
    var strFecha2 = d.getElementById("COB_FECHAS2").value;
    var strMoneda = d.getElementById("COB_MONEDA").value;
    var strPOST = "&TKT_ANULADA=0&TKT_ESRECU=999&TKT_MONEDA=" + strMoneda;
    if (strFecha1 != "")
    {
        strPOST += "&TKT_FECHA1=" + strFecha1;
    }

    if (strFecha2 == "")
    {
        strFecha2 = FechaActualCob();
    }

    strPOST += "&TKT_FECHA2=" + strFecha2;
    itemIdCob = 0;
    ValidaClean("COB_CTE");
    if (strCte == 0) {
        ValidaShow("COB_CTE", "NECESITA SELECCIONAR UN CLIENTE");
    } else {
        $("#dialogWait").dialog("open");
        //limpiamos datos
        resetCobroMas(false);

        $.ajax({
            type: "POST",
            data: strPOST,
            scriptCharset: "utf-8",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "xml",
            url: "ERP_Cobros.jsp?id=16",
            success: function (datos) {
                var objsc = datos.getElementsByTagName("tickets")[0];
                var lstTiks = objsc.getElementsByTagName("ticket");
                for (i = 0; i < lstTiks.length; i++) {
                    var obj = lstTiks[i];
                    //Solo mostramos los documentos con saldo
                    if (obj.getAttribute('TKT_SALDO') > 0) {
                        var dblPagos = obj.getAttribute('TKT_TOTAL') - obj.getAttribute('TKT_SALDO');
                        var strFecha = obj.getAttribute('TKT_FECHA');
                        strFecha = strFecha.substr(strFecha.length - 2, strFecha.length) + "/" + strFecha.substr(4, 2) + "/" + strFecha.substr(0, 4);
                        var datarow = {
                            COBD_ID: obj.getAttribute('TKT_ID'),
                            COBD_FOLIO: obj.getAttribute('TKT_FOLIO'),
                            COBD_FECHA: strFecha,
                            COBD_TOTAL: obj.getAttribute('TKT_TOTAL'),
                            COBD_PAGOS: dblPagos,
                            COBD_SALDO: obj.getAttribute('TKT_SALDO'),
                            COBD_TIPO: "TICKET"

                        };
                        //Anexamos el registro al GRID
                        itemIdCob++;
                        jQuery("#COB_GRID1").addRowData(itemIdCob, datarow, "last");
                    }
                }
                var objsc = datos.getElementsByTagName("facturas")[0];
                var lstTiks = objsc.getElementsByTagName("factura");
                for (i = 0; i < lstTiks.length; i++) {
                    var obj = lstTiks[i];
                    //Solo mostramos los documentos con saldo
                    if (obj.getAttribute('FAC_SALDO') > 0) {
                        var dblPagos = obj.getAttribute('FAC_TOTAL') - obj.getAttribute('FAC_SALDO');
                        var strFecha = obj.getAttribute('FAC_FECHA');
                        strFecha = strFecha.substr(strFecha.length - 2, strFecha.length) + "/" + strFecha.substr(4, 2) + "/" + strFecha.substr(0, 4);
                        var datarow = {
                            COBD_ID: obj.getAttribute('FAC_ID'),
                            COBD_FOLIO: obj.getAttribute('FAC_FOLIO'),
                            COBD_FECHA: strFecha,
                            COBD_TOTAL: obj.getAttribute('FAC_TOTAL'),
                            COBD_PAGOS: dblPagos,
                            COBD_SALDO: obj.getAttribute('FAC_SALDO'),
                            COBD_TIPO: "FACTURA"
                        };
                        //Anexamos el registro al GRID
                        itemIdCob++;
                        jQuery("#COB_GRID1").addRowData(itemIdCob, datarow, "last");
                    }
                }
                //BuscaTasaCambio();
                $("#dialogWait").dialog("close");

            },
            error: function (objeto, quepaso, otroobj) {
                alert(":ptoExist:" + objeto + " " + quepaso + " " + otroobj);
                $("#dialogWait").dialog("close");
            }
        });


    }
}

function LimpiarGrid()
{
    //alert("Entre");
//   $('#COB_GRID1').empty();
    jQuery("#COB_GRID1").clearGridData();
    itemIdCob = 0;
    //CargaAjustaPago();
    searchTrxMasiva(true);
}

function resetCobroMasAjustaPago(bolUpdateGrid) {
    d.getElementById("COB_TOTAL").value = 0.0;
    if (bolUpdateGrid) {
        //LimpiarGrid();
    } else {
        jQuery("#COB_GRID1").clearGridData();
    }
}

function CargaAnticiposCob() {
    $("#dialogWait").dialog("open");
    var intCT = document.getElementById("COB_CTE").value;
    var intMonPago = document.getElementById("COB_MONEDA_PAGO").value;
    var intMonDoc = document.getElementById("COB_MONEDA").value;
    document.getElementById("TASA_CAMBIO_PAGO").disabled = false;
    document.getElementById("COB_TASA_CAMBIO").disabled = false;
    var strPOST = "CT_ID=" + intCT;
    strPOST += "&MONEDAPAGO=" + intMonPago;
    //Buscamos Si tiene anticipos
    $.ajax({
        type: "POST",
        data: strPOST,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "ERP_Cobros.jsp?id=13",
        success: function (datos) {
            var objsc = datos.getElementsByTagName("Anticipos")[0];
            var lstAnticipos = objsc.getElementsByTagName("Anticipo");
//         $("#GRID_ANTICIPOS").empty();
            jQuery("#GRID_ANTICIPOS").clearGridData();
            document.getElementById("COB_ESANTICIPO1").disabled = false;
            if (lstAnticipos.length != 0) {
                for (var i = 0; i < lstAnticipos.length; i++) {
                    var obj = lstAnticipos[i];
                    //Solo mostramos los documentos con saldo                
                    var strFecha = obj.getAttribute('MC_FECHA');
                    var intMoneda = parseInt(obj.getAttribute('MC_MONEDA'));
                    var txtMONEDA = "";
                    if (intMoneda == 1)
                    {
                        txtMONEDA = "PESOS";
                    } else
                    {
                        if (intMoneda == 2)
                        {
                            txtMONEDA = "DOLARES";
                        } else
                        {
                            if (intMoneda == 3)
                            {
                                txtMONEDA = "EUROS";
                            }
                        }
                    }
                    strFecha = strFecha.substr(strFecha.length - 2, strFecha.length) + "/" + strFecha.substr(4, 2) + "/" + strFecha.substr(0, 4);
                    var datarow = {
                        ANTI_ID: obj.getAttribute('MC_ID'),
                        ANTI_FECHA: strFecha,
                        ANTI_ABONO: obj.getAttribute('MC_SALDO_ANTICIPO'),
                        ANTI_FOLIO: obj.getAttribute('MC_FOLIO'),
                        ANTI_MONEDA: obj.getAttribute('MC_MONEDA'),
                        ANTI_TASAPESO: obj.getAttribute('MC_TASAPESO'),
                        ANTI_ORIGINAL: obj.getAttribute('MC_ANTICIPO_ORIGINAL'),
                        ANTI_MONEDA_LETRA: txtMONEDA
                    };
                    //Anexamos el registro al GRID                
                    jQuery("#GRID_ANTICIPOS").addRowData(i, datarow, "last");

                }
                $("#dialogWait").dialog("close");
            } else {
                alert("El cliente seeleccionado no tiene anticipos");
                document.getElementById("COB_ESANTICIPO1").disabled = true;
                document.getElementById("COB_ESANTICIPO2").checked = true;
                $("#dialogWait").dialog("close");
                $("#GRID_ANTICIPOS").jqGrid('setGridState', 'hidden');
                EsAnticipoCob();
            }

        },
        error: function (objeto, quepaso, otroobj) {
            alert(":ptoExist:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
            //$("#dialogWait").dialog("close");
        }
    });

}

//Revisamos si la moneda del banco es igual a la moneda con la que se hara el pago
function ChecaCopatibilidadMonedaCob()
{
    var intMonPago = document.getElementById("COB_MONEDA_PAGO").value;
    var intMonBanco = document.getElementById("TIPO_MONEDA_BANC").value;

    if (intMonPago == 0 || intMonBanco == 0)
    {

    } else
    {
        if (intMonBanco != intMonPago)
        {
            alert("Se debe Pagar con la misma moneda en la que esta el banco");
            return false;
        }
    }
    return true;
}

/*Pide el tipo de moneda que usa el cliente*/
function PeticionMonedaProvCob()
{
    $("#dialogWait").dialog("open");
    document.getElementById("COB_BCO").value = 0;
    var strCT_ID = document.getElementById("COB_CTE").value;
    var strPOST = "CT_ID=" + strCT_ID;
    $.ajax({
        type: "POST",
        data: encodeURI(strPOST),
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "html",
        url: "ERP_Cobros.jsp?id=14",
        success: function (dato)
        {
            if (!isNaN(dato))
            {
                ObtenNomCteAntCobros();
                document.getElementById("COB_MONEDA").value = dato;
                document.getElementById("COB_MONEDA_PAGO").value = dato;
//            $("#GRID_ANTICIPOS").empty();
//            $("#COB_GRID1").empty();
                jQuery("#GRID_ANTICIPOS").clearGridData();
                jQuery("#COB_GRID1").clearGridData();
                document.getElementById("COB_TOTAL2").value = 0.00;
                document.getElementById("COB_ESANTICIPO2").checked = true;
                document.getElementById("COB_ESANTICIPO1").disabled = false;
                EsAnticipoCob();
            }
            $("#dialogWait").dialog("close");
        },
        error: function (objeto, quepaso, otroobj) {
            alert(":pto9:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }
    });

}

/*Funcion para ocultar o mostrar campos de anticipo o de pago*/
function EsAnticipoCob() {
    var Sichecked = false;
//    var Sichecked = document.getElementById("COB_ESANTICIPO1").checked;
    if (Sichecked) {
        document.getElementById("COB_MAX_PAGO").parentNode.parentNode.style.display = "none";
        document.getElementById("COB_FALTA_MAX").parentNode.parentNode.style.display = "none";
        document.getElementById("COB_MAX_PAGO").value = 0.0;
        document.getElementById("COB_FALTA_MAX").value = 0.0;
        document.getElementById("COB_BCO").parentNode.parentNode.style.display = "none";
        document.getElementById("FORMPAG").parentNode.parentNode.style.display = "none";
        document.getElementById("ANTICIPO_TOTAL").parentNode.parentNode.style.display = "block";
        document.getElementById("ANTICIPO_USO").parentNode.parentNode.style.display = "block";
        document.getElementById("COB_NOCHEQUE").parentNode.parentNode.style.display = "none";
        $("#GRID_ANTICIPOS").jqGrid('setGridState', 'visible');
        CargaAnticiposCob();
    } else {
        document.getElementById("COB_MAX_PAGO").parentNode.parentNode.style.display = "block";
        document.getElementById("COB_FALTA_MAX").parentNode.parentNode.style.display = "block";
        document.getElementById("COB_BCO").parentNode.parentNode.style.display = "block";
        document.getElementById("FORMPAG").parentNode.parentNode.style.display = "block";
        document.getElementById("ANTICIPO_TOTAL").parentNode.parentNode.style.display = "none";
        document.getElementById("ANTICIPO_USO").parentNode.parentNode.style.display = "none";
        document.getElementById("ANTICIPO_TOTAL").value = 0.0;
        document.getElementById("ANTICIPO_USO").value = 0.0;
        //document.getElementById("TASA_CAMBIO_PAGO").disabled = false;
        //document.getElementById("COB_NOCHEQUE").parentNode.parentNode.style.display = "block";
        $("#GRID_ANTICIPOS").jqGrid('setGridState', 'hidden');
//      $("#GRID_ANTICIPOS").empty();
        jQuery("#GRID_ANTICIPOS").clearGridData();
    }
    RevisaMetodoPagoCob();
}
/**
 *Funcion para sacar la fecha actual
 ***/
function FechaActualCob() {
    var fechaActual = new Date();
    var dia = fechaActual.getDate();
    var mes = fechaActual.getMonth() + 1;
    var anio = fechaActual.getYear();
    if (dia < 10)
        dia = "0" + dia;
    if (mes < 10)
        mes = "0" + mes;
    if (anio < 1000)
        anio += 1900;
    var fechaHoy = dia + "/" + mes + "/" + anio;
    return fechaHoy;
}

function selAntiRowCob(rowid, status) {
    var grid = jQuery('#GRID_ANTICIPOS');
    var lstRow = grid.getRowData(rowid);
    document.getElementById("ANTICIPO_TOTAL").value = lstRow.ANTI_ABONO;
    document.getElementById("ANTICIPO_USO").value = lstRow.ANTI_ABONO;
    document.getElementById("COB_MONEDA_PAGO").value = lstRow.ANTI_MONEDA;
    document.getElementById("TIPO_MONEDA_BANC").value = lstRow.ANTI_MONEDA;
    var intMonDoc = document.getElementById("COB_MONEDA").value;
    idAnticipo = lstRow.ANTI_ID;
    document.getElementById("TASA_CAMBIO_PAGO").value = FormatNumber(lstRow.ANTI_TASAPESO, intNumdecimal, true);
    if (lstRow.ANTI_MONEDA == 1 && intMonDoc == 2) {
        document.getElementById("COB_OPERACION").value = "D";
    } else {
        if (lstRow.ANTI_MONEDA == 2 && intMonDoc == 1) {
            document.getElementById("COB_OPERACION").value = "M";
        } else {
            if (lstRow.ANTI_MONEDA == 2 && intMonDoc == 3) {
                document.getElementById("COB_OPERACION").value = "D";
            } else {
                if (lstRow.ANTI_MONEDA == 3 && intMonDoc == 2) {
                    document.getElementById("COB_OPERACION").value = "M";
                }
            }
        }
    }
}


function RevisaMetodoPagoCob() {
    //alert(document.getElementById("FORMPAG").value);
    var Seleccion = parseInt(document.getElementById("FORMPAG").value);
    var Sichecked = document.getElementById("COB_ESANTICIPO1").checked;
    if (Sichecked) {
        document.getElementById("COB_NOCHEQUE").parentNode.parentNode.style.display = "none";
    } else {
        if (Seleccion == "02") {
            document.getElementById("COB_NOCHEQUE").parentNode.parentNode.style.display = "block";
        } else {
            document.getElementById("COB_NOCHEQUE").parentNode.parentNode.style.display = "none";
        }
    }
}


function CalculaFaltaxPagarCob() {
    //alert("Entre");
    var Sichecked = document.getElementById("COB_ESANTICIPO1").checked;
    if (Sichecked) {
        dblCantidadMaximaPago = (document.getElementById("ANTICIPO_TOTAL").value);
    } else {
        dblCantidadMaximaPago = (document.getElementById("COB_MAX_PAGO").value);
    }
    var dblTotal = parseFloat(document.getElementById("COB_TOTAL2").value);
    if (isNaN(dblCantidadMaximaPago)) {
        dblCantidadMaximaPago = 0.0;
    }
    if (isNaN(dblFaltaXPagar)) {
        dblFaltaXPagar = 0.0;
    }
    if (dblCantidadMaximaPago == 0) {
        dblFaltaXPagar = 0.0;
    }
    dblFaltaXPagar = dblCantidadMaximaPago - dblTotal;
    //alert(Sichecked);
    if (Sichecked) {
        //alert("Debo acutalizar");
        document.getElementById("ANTICIPO_TOTAL").value = FormatNumber(dblCantidadMaximaPago, intNumdecimal, true);
        document.getElementById("ANTICIPO_USO").value = FormatNumber(dblFaltaXPagar, intNumdecimal, true);
    } else {
        document.getElementById("COB_MAX_PAGO").value = FormatNumber(dblCantidadMaximaPago, intNumdecimal, true);
        document.getElementById("COB_FALTA_MAX").value = FormatNumber(dblFaltaXPagar, intNumdecimal, true);
    }
    //dblCantidadMaximaPago
    //dblFaltaXPagar
}

function ValidaMaximoxPagarCob() {
    var dblMaxPagar = parseFloat(document.getElementById("COB_MAX_PAGO").value);
    if (dblMaxPagar < 0) {
        alert("El pago minimo debe ser positivo o cero");
        document.getElementById("COB_MAX_PAGO").focus();
    } else {
        CalculaFaltaxPagarCob();
        /*Delimitamos el uso de lo que se puede pagar*/
        var Sichecked = document.getElementById("COB_ESANTICIPO2").checked;
        if (Sichecked) {
            dblCantidadMaximaPago = parseFloat(document.getElementById("COB_MAX_PAGO").value);
        }

    }
}
function BuscaTasaCambioCobPago(bolPasaSearchMasiva)
{
    if (bolPasaSearchMasiva == null) {
        bolPasaSearchMasiva = false;
    }
    var intMonedaBanco = document.getElementById("COB_MONEDA").value;
    var intMonedaSeleccionada = document.getElementById("COB_MONEDA_PAGO").value;
    var strFecha = document.getElementById("COB_FECHA").value;
    var strPOST = "&Moneda_1=" + intMonedaSeleccionada;
    strPOST += "&Moneda_2=" + intMonedaBanco;

    strPOST += "&fecha=" + strFecha;
    $.ajax({
        type: "POST",
        data: strPOST,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "ERP_CobrosCuentas.jsp?id=9",
        success: function (datos) {
            //alert(datos);
            var objsc = datos.getElementsByTagName("TasaCambio")[0];
            var lstTiks = objsc.getElementsByTagName("TasaCambios");
            //datos = parseFloat(datos);
            var obj = lstTiks[0];
            var dblTC = obj.getAttribute('TC');
            var strOperacion = obj.getAttribute('Operacion');
            document.getElementById("COB_OPERACION").value = strOperacion;
            //alert(strOperacion);
            if (dblTC == 0.0)
            {
                dblTC = 1.0;
            }
            document.getElementById("TASA_CAMBIO_PAGO").value = dblTC;
            $("#dialogWait").dialog("close");
            if (bolPasaSearchMasiva) {
                searchTrxMasiva(false);
            }
            //$("#dialogWait").dialog("close");
        },
        error: function (objeto, quepaso, otroobj) {
            alert(":ptoExist:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }
    });
}
function RecalculaGridNuevaTasaCambio()
{
    searchTrxMasiva(false);

}


function ObtenNomCteAntCobros()
{
    var strCT_ID = document.getElementById("COB_CTE").value;
    var strPOST = "&CTE_ID=" + strCT_ID;
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
            if (trim(datos)) {
                var info = datos.split("|");
                document.getElementById("COB_NOM_CTE").value = info[0];
                $("#dialogWait").dialog("close");
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

/**Anade una operacion por cobrar individual*/
function AddTrxEvtMas(event, obj) {
    //Al pulsar enter valida si la transaccion existe
    if (event.keyCode == 13) {
        getTrxMas(obj);
        //SaveCobDo();
    }
}

function AddTrxIndMas() {
    getTrxMas(d.getElementById("COB_TRX"));
    //SaveCobDo();
}
/**Valida y recupera los valores de la transaccion por pagar Masivo*/
function getTrxMas(obj) {
    //Prefijos dependiendo del tipo de venta
    var strPrefijoMaster = "TKT";
    var strNomDoc = "TICKET";
    strNomOrderCob = "TKT_ID";
    strNomFormCob = "TICKETVIEW";
    strKeyCob = "TKT_ID";
    strTipoVtaCob = "2";
    var strXmlIni = "vta_ticketss";
    if (document.getElementById("COB_TIPO").value == "1") {
        //Factura
        strPrefijoMaster = "FAC";
        strNomFormCob = "FACTVIE";
        strNomOrderCob = "FAC_ID";
        strKeyCob = "FAC_FOLIO_C";
        strTipoVtaCob = "1";
        strXmlIni = "vta_facturass";
        strNomDoc = "FACTURA";
    }
    if (document.getElementById("COB_TIPO").value == "3") {
        //Pedido
        strPrefijoMaster = "PD";
        strNomFormCob = "PEDIDOVIEW";
        strNomOrderCob = "PD_ID";
        strKeyCob = "PD_ID";
        strTipoVtaCob = "3";
        strXmlIni = "vta_pedidoss";
        strNomDoc = "PEDIDO";
    }
    //resetCobroInd();//Limpiamos los datos del documento a cobrar
    //Por ajax solicitamos los datos de la operacion a cobrar....  

    $.ajax({
        type: "POST",
        data: strPrefijoMaster + "_FOLIO_C=" + obj.value + "&" + strPrefijoMaster + "_ANULADA=999&EMP_ID=" + intEMP_ID,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "CIP_TablaOp.jsp?ID=2&opnOpt=" + strNomFormCob,
        success: function (datoVal) {
            var objXML = datoVal.getElementsByTagName(strXmlIni)[0];
            if (objXML != null) {
                if (objXML.getAttribute(strPrefijoMaster + '_ANULADA') == 0) {
                    if (parseFloat(objXML.getAttribute(strPrefijoMaster + '_SALDO')) > 0) {
                        d.getElementById("COB_NOM").value = objXML.getAttribute(strPrefijoMaster + '_RAZONSOCIAL');
                        //d.getElementById("COB_FECHATRX").value = objXML.getAttribute(strPrefijoMaster + '_FECHA');
                        d.getElementById("COB_FOLIO").value = objXML.getAttribute(strPrefijoMaster + '_FOLIO_C');
                        d.getElementById("COB_IDPAGO").value = objXML.getAttribute(strPrefijoMaster + '_ID');
                        d.getElementById("COB_TOTTRX").value = parseFloat(FormatNumber(objXML.getAttribute(strPrefijoMaster + '_SALDO'), intNumdecimal, true));
//                        d.getElementById("COB_TOTAL").value = parseFloat(d.getElementById("COB_TOTAL").value) + parseFloat(FormatNumber(objXML.getAttribute(strPrefijoMaster + '_SALDO'), intNumdecimal, true));
                        //Mandamos a llamar a las formas de pago

                        d.getElementById("FPago1").value = 0.0;
                        //AgregarPagoMasivoGridCob(parseFloat(FormatNumber(objXML.getAttribute(strPrefijoMaster + '_TOTAL'), intNumdecimal, true)));   
                    } else {
                        alert("El documento ya no tiene saldo por pagar....");
                    }

                } else {
                    alert("EL " + strNomDoc + " " + obj.value + " con folio " + objXML.getAttribute(strPrefijoMaster + '_FOLIO') + " ESTA ANULADO");
                }
            } else {
                alert("EL " + strNomDoc + " " + obj.value + " NO EXISTE");
            }

        },
        error: function (objeto, quepaso, otroobj) {
            alert(":ptoExist:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }
    });
    d.getElementById("COB_TRX").focus();
    d.getElementById("COB_TRX").select();
}

function AgregarPagoMasivoGridCob()
{
    var intTipoDocIdx = document.getElementById("COB_TIPO").selectedIndex;
    var dblMontoPago = document.getElementById("COB_TOTTRX").value;
    var intIdPago = document.getElementById('COB_IDPAGO').value;
    if (parseFloat(intIdPago) > 0) {
        var txtTipoDoc = d.getElementById("COB_TIPO").options[intTipoDocIdx].text;
        var datarow = {
            COBG_DOC_ID: intIdPago,
            COBG_TIPO_DOCUMENTO: document.getElementById('COB_TIPO').value,
            COBG_TIPO_DOC_TXT: txtTipoDoc,
            COBG_MONEDA: "1",
            COBG_MONTOPAGO: dblMontoPago,
            COBG_FOLIO: document.getElementById('COB_FOLIO').value,
            COBG_CLIENTE: document.getElementById('COB_NOM').value
        };
        //Anexamos el registro al GRID                
        jQuery("#COB_GRID_PAGOS").addRowData(document.getElementById('COB_TRX').value, datarow, "last");
        //Calculamos el total

        var grid = jQuery("#COB_GRID_PAGOS");
        var arr = grid.getDataIDs();
        var dblSuma = 0.0;
        for (var i = 0; i < arr.length; i++) {
            var id = arr[i];
            var lstRow = grid.getRowData(id);
            dblSuma = dblSuma + parseFloat(lstRow.COBG_MONTOPAGO);
        }

        d.getElementById("COB_TOTAL").value = dblSuma;

        //Limpiamos la transaccion
        document.getElementById("COB_TOTTRX").value = 0.0;
        document.getElementById("COB_IDPAGO").value = 0;
        document.getElementById("COB_FOLIO").value = "";
        document.getElementById("COB_NOM").value = "";
        document.getElementById("COB_TRX").value = "";
    } else {
        //Seleccione una venta por pagar con saldo
        alert("Favor de seleccionar un pago");
    }
}

function RemovePagoMasivo()
{
    var grid = jQuery("#COB_GRID_PAGOS");
    if (grid.getGridParam("selrow") != null) {
        var lstRow = grid.getRowData(grid.getGridParam("selrow"));
//        d.getElementById("COB_TOTTRX").value = parseFloat(d.getElementById("COB_TOTTRX").value) - lstRow.COBG_MONTOPAGO;
        d.getElementById("COB_TOTAL").value = parseFloat(d.getElementById("COB_TOTAL").value) - parseFloat(lstRow.COBG_MONTOPAGO);
        grid.delRowData(grid.getGridParam("selrow"));
    }
}

function SaveCobMas() {
    var $tabs = $('#tabsCOBROS').tabs();
    var selected = $tabs.tabs('option', 'active');
    //Si el tipo de cobranza es 1 es por transaccion sino
    if (selected == 0) {
        var boolValidacion = true;

        if (d.getElementById("CON_BANDERAIPARC") != null) {
            if (d.getElementById("CON_BANDERAIPARC").value == 1) {
                if (d.getElementById("COB_BANCO_MAS").value == 0) {
                    alert("Seleccione el Banco");
                    boolValidacion = false;
                }
                if (d.getElementById("COB_FECHATRX").value == "") {
                    alert("Capture la fecha");
                    boolValidacion = false;
                }
            }
        }


        if (boolValidacion) {

            $("#dialogWait").dialog("open");
            var grid = jQuery("#COB_GRID_PAGOS");
            var arr = grid.getDataIDs();
            var strPOST = "";
            strPOST += "&MONEDAAPAGAR=1";
            strPOST += "&MONTOPAGOTOTAL=" + (d.getElementById("COB_TOTAL").value);
            strPOST += "&BC_ID=" + (d.getElementById("COB_BANCO_MAS").value);
            strPOST += "&ANTICIPOAUSAR=0";
            strPOST += "&USA_ANTI=0";
            strPOST += "&ANTI_ID=0";
            strPOST += "&CANTIDAD_ANTI=0";
            strPOST += "&FECHA=" + (d.getElementById("COB_FECHATRX").value);
            strPOST += "&MC_FORMADEPAGO=" + (d.getElementById("COB_METPAGO2").value);
            strPOST += "&NOTAS=";
            strPOST += "&MONEDA=1";
            strPOST += "&TASAPESO=1.0";
            strPOST += "&Timbrar=0";
            //GENERAMOS peticion POST
            for (var i = 0; i < arr.length; i++) {
                var id = arr[i];
                var lstRow = grid.getRowData(id);
                if (lstRow.COBG_MONTOPAGO > 0.0)
                {

                    strPOST += "&idTrx=" + lstRow.COBG_DOC_ID;
                    strPOST += "&TipoDoc=" + lstRow.COBG_TIPO_DOCUMENTO;
                    strPOST += "&MONTOPAGO=" + lstRow.COBG_MONTOPAGO;
                    strPOST += "&MONENDAORIGINAL=" + lstRow.COBG_MONTOPAGO;
                }
            }
            //Validamos si solo esta pagando una operacion para guardar un pago normal
            var strNomForm = "COBROSMAS";
            var strNomField = "MCM_ID";


            //Pagos Mandamos las 4 formas de pago
            strPOST += "&COUNT_PAGOS=1";
            //efectivo                
            strPOST += "&MCD_MONEDA1=1";
            strPOST += "&MCD_FOLIO1=";
            strPOST += "&MCD_FORMAPAGO1=" + document.getElementById("COB_METPAGO2").value;
            if (document.getElementById("COB_METPAGO2") == "02") {
                strPOST += "&MCD_NOCHEQUE1=" + d.getElementById("COB_NOCHEQUE").value;
            } else {
                strPOST += "&MCD_NOCHEQUE1=";
            }

            strPOST += "&MCD_BANCO1=" + d.getElementById("COB_BANCO_MAS").value;
            strPOST += "&MCD_NOTARJETA1=";
            strPOST += "&MCD_TIPOTARJETA1=";
            strPOST += "&MCD_IMPORTE1=" + (parseFloat(d.getElementById("COB_TOTTRX").value));
            strPOST += "&MCD_TASAPESO1=1";
            strPOST += "&MCD_CAMBIO1=0.0";

            //Hacemos la peticion por POST
            $.ajax({
                type: "POST",
                data: encodeURI(strPOST),
                scriptCharset: "utf-8",
                contentType: "application/x-www-form-urlencoded;charset=utf-8",
                cache: false,
                dataType: "html",
                url: "Cobros.do?id=3",
                success: function (dato) {
                    dato = trim(dato);
                    if (Left(dato, 3) == "OK.") {
                        openFormat(strNomForm, "PDF", CreaHidden(strNomField, dato.replace("OK.", "")))
//               $('#COB_GRID_PAGOS').empty();
                        jQuery("#COB_GRID_PAGOS").clearGridData();

                        document.getElementById("COB_TRX").value = "";
                        document.getElementById("COB_FECHATRX").value = "";
                        document.getElementById("COB_NOM").value = "";
                        document.getElementById("COB_FOLIO").value = "";
                        document.getElementById("COB_TOTTRX").value = "0.0";
                        document.getElementById("COB_TOTAL").value = "0.0";

                        grid.clearGridData();
                        itemIdCob = 0;
                        //LimpiarGrid();
                        $("#dialogPagos").dialog("close");
                    } else {
                        alert(dato);
                    }
                    resetCobroInd();
                    $("#dialogWait").dialog("close");
                },
                error: function (objeto, quepaso, otroobj) {
                    alert(":pto9:" + objeto + " " + quepaso + " " + otroobj);
                    $("#dialogWait").dialog("close");
                }
            });
        }
    } else {
        SaveCob();
    }


}
/**Realiza todas las operaciones de inicializacion de cobranza*/
function InitCobMasivos1() {
    $("#dialogWait").dialog("open");
    myLayout.close("west");
    myLayout.close("east");
    myLayout.close("south");
    myLayout.close("north");
    d.getElementById("COB_TRX").focus();
    d.getElementById("COB_TRX").select();
    //Realizamos el menu de botones
//    var strHtml = "<ul>" +
//            getMenuItem("SaveCobMas();", "Guardar Cobro Masivo", "images/ptovta/CircleSave.png") +
//            getMenuItem("SalirCob();", "Salir", "images/ptovta/exitBig.png") +
//            "</ul>";
    document.getElementById("TOOLBAR").innerHTML = _drawMenuNewCobrosMasivos1();
    document.getElementById("COB_MONEDA_PAGO").disabled = true;
    document.getElementById("COB_FALTA_MAX").disabled = true;
    document.getElementById("ANTICIPO_TOTAL").disabled = true;
    document.getElementById("COB_NOM_CTE").disabled = true;
    document.getElementById("COB_NOCHEQUE").parentNode.parentNode.style.display = "none";
    EsAnticipoCob();
    document.getElementById("TASA_CAMBIO_PAGO").disabled = true;
    document.getElementById("btn1").style.display = 'none';
    itemIdCob = 0;
    $("#dialogWait").dialog("close");
}
function _drawMenuNewCobrosMasivos1() {
    var strHtml = "<table>" +
            "<tr>" +
            "<td ><i class = \"fa fa-refresh\" style=\"font-size:40px\" onclick=\"searchTrxMasiva();\" title=\"Actualizar\"></i></td>" +
            "<td>&nbsp;&nbsp;&nbsp;</td>" +
            //GUARDA PAGO
            "<td ><i class = \"fa fa-floppy-o\" style=\"font-size:40px\" onclick=\"SaveCobSinTimbrar();\" title=\"Guardar Cobro\"></i></td>" +
            "<td>&nbsp;&nbsp;&nbsp;</td>" +
            //GUARDA Y TIMBRA PAGO
            "<td ><i class = \"fa fa-hdd-o\" style=\"font-size:40px\" onclick=\"SaveCobMas();\" title=\"Guardar Y Timbrar Cobro\"></i></td>" +
            "<td>&nbsp;&nbsp;&nbsp;</td>" +
            //Salir de la pantalla
            "<td id=\"btn_exit\"><i class = \"fa fa-sign-out\" style=\"font-size:40px\" onclick=\"SalirCob();\" title=\"Salir\"></i></td></tr></table>";
////Realizamos el menu de botones
//   var strHtml = "<ul>" +
//           getMenuItem("SaveCob();", "Guardar Cobro", "images/ptovta/CircleSave.png") +
//           getMenuItem("SalirCob();", "Salir", "images/ptovta/exitBig.png") +
//           "</ul>";
    return strHtml;
}
function _drawMenuNewCobros() {
    var strHtml = "<table>" +
            "<tr>" +
            "<td ><i class = \"fa fa-refresh\" style=\"font-size:40px\" onclick=\"searchTrxMasiva();\" title=\"Actualizar\"></i></td>" +
            "<td>&nbsp;&nbsp;&nbsp;</td>" +
            //GUARDA PAGO
            "<td ><i class = \"fa fa-floppy-o\" style=\"font-size:40px\" onclick=\"SaveCobSinTimbrar();\" title=\"Guardar Cobro\"></i></td>" +
            "<td>&nbsp;&nbsp;&nbsp;</td>" +
            //GUARDA Y TIMBRA PAGO
            "<td ><i class = \"fa fa-hdd-o\" style=\"font-size:40px\" onclick=\"SaveCob();\" title=\"Guardar Y Timbrar Cobro\"></i></td>" +
            "<td>&nbsp;&nbsp;&nbsp;</td>" +
            //Salir de la pantalla
            "<td id=\"btn_exit\"><i class = \"fa fa-sign-out\" style=\"font-size:40px\" onclick=\"SalirCob();\" title=\"Salir\"></i></td></tr></table>";
////Realizamos el menu de botones
//   var strHtml = "<ul>" +
//           getMenuItem("SaveCob();", "Guardar Cobro", "images/ptovta/CircleSave.png") +
//           getMenuItem("SalirCob();", "Salir", "images/ptovta/exitBig.png") +
//           "</ul>";
    return strHtml;
}

function _resetSioNoCob(strNameBtnSi, strNameBtnNo, strNameInside) {
    $(strNameBtnSi).unbind("");
    $(strNameBtnNo).unbind("");
    var div = document.getElementById(strNameInside);
    div.innerHTML = "";
}

/**Muestra el formato del impresion del pago*/
function PagosPrint() {
    var grid = jQuery("#COBROSVIEW");
    if (grid.getGridParam("selrow") != null) {
        var lstRow = grid.getRowData(grid.getGridParam("selrow"));
        if (lstRow.MC_ANULADO == "NO") {
            if (lstRow.MCM_ID != 0) {
                var strPOST = "MCM_ID=" + lstRow.MCM_ID;
                //Hacemos la peticion para ver si es version 3.3 complemento de pagos
                $.ajax({
                    type: "POST",
                    data: encodeURI(strPOST),
                    scriptCharset: "utf-8",
                    contentType: "application/x-www-form-urlencoded;charset=utf-8",
                    cache: false,
                    dataType: "html",
                    url: "vta_procesos_CFDI_3_3.jsp?ID=4",
                    success: function (dato) {
                        if (dato == 1) {
                            Abrir_Link("JasperReport?REP_ID=407&boton_1=PDF&doc_folio1=&doc_folio2=&emp_id=&sc_id=&doc_id=" + lstRow.MCM_ID, '_reporte', 500, 600, 0, 0);
                        } else {
                            openFormat("COBROSMAS", "PDF", CreaHidden("MCM_ID", lstRow.MCM_ID))
                        }
                    }});

            } else {
                openFormat(strNomFormatPrintCob, "PDF", CreaHidden("MC_ID", lstRow.MC_ID))
            }
        }
    }
}
/**
 * COFIDE    salir de complementos
 */
function exitComplemento() {
    $("#dialogTMK1").dialog("close");
}

/**
 * init complementos de pago
 */
function initCompelemento() {
    $("#dialogWait").dialog("open");
    document.getElementById("COB_MONEDA_PAGO").disabled = false;
    document.getElementById("COB_MONEDA_PAGO").setAttribute("class", "outEdit");
    document.getElementById("COB_MONEDA_PAGO").setAttribute("className", "outEdit");
    document.getElementById("COB_FALTA_MAX").disabled = true;
    document.getElementById("ANTICIPO_TOTAL").disabled = true;
    document.getElementById("COB_NOM_CTE").disabled = true;
    document.getElementById("COB_NOCHEQUE").parentNode.parentNode.style.display = "none";
    itemIdCob = 0;

    /*
     * limpia la pantalla
     */
    document.getElementById("COB_MAX_PAGO").parentNode.parentNode.style.display = "block";
    document.getElementById("COB_FALTA_MAX").parentNode.parentNode.style.display = "block";
    document.getElementById("COB_BCO").parentNode.parentNode.style.display = "block";
    document.getElementById("FORMPAG").parentNode.parentNode.style.display = "block";
    document.getElementById("ANTICIPO_TOTAL").parentNode.parentNode.style.display = "none";
    document.getElementById("ANTICIPO_USO").parentNode.parentNode.style.display = "none";
    document.getElementById("COB_FECHAS2").parentNode.parentNode.style.display = "none";
    document.getElementById("ANTICIPO_TOTAL").value = 0.0;
    document.getElementById("ANTICIPO_USO").value = 0.0;
    $("#GRID_ANTICIPOS").jqGrid('setGridState', 'hidden');
    jQuery("#GRID_ANTICIPOS").clearGridData();
    $("#dialogWait").dialog("close");
    loadInfoVta();
}

/**
 * carga información de la venta seleccionada
 */
function loadInfoVta() {
    var grid = jQuery("#GR_VAL_FAC");
    var id = grid.getGridParam("selrow");
    var lstVal = grid.getRowData(id);
    var stridFactura = lstVal.FAC_ID;
    var strPost = "";

    strPost = "fac_id=" + stridFactura;
    $.ajax({
        type: "POST",
        data: strPost,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "ERP_Cobros.jsp?id=19",
        beforeSend: function () {
            $("#dialogWait").dialog("open");
        },
        success: function (datos) {
            //alert(datos);
            var objsc = datos.getElementsByTagName("vta")[0];
            var lstObjs = objsc.getElementsByTagName("datos");
            //datos = parseFloat(datos);
            var obj = lstObjs[0];
            document.getElementById("COB_CTE").value = obj.getAttribute("id_cte");
            document.getElementById("COB_NOM_CTE").value = obj.getAttribute("razonsocial");
            document.getElementById("COB_FECHAS1").value = obj.getAttribute("fecha");
            document.getElementById("COB_FECHAS2").value = obj.getAttribute("fecha2");
            document.getElementById("COB_CONCEPTO").value = obj.getAttribute("concepto");
            document.getElementById("FORMPAG").value = obj.getAttribute("forma");
            document.getElementById("COB_MAX_PAGO").value = obj.getAttribute("importe_total"); //saldo
            document.getElementById("COB_MONEDA").value = obj.getAttribute("moneda1");
            document.getElementById("COB_MONEDA_PAGO").value = obj.getAttribute("moneda2");
            $("#dialogWait").dialog("close");
            ValidaMaximoxPagarCob_();
            searchTrxMasivado_();
        },
        error: function (objeto, quepaso, otroobj) {
            alert(":ptoExist: 19 " + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }
    });
}

/**
 * calcula maximo de pago
 */
function ValidaMaximoxPagarCob_() {
    var dblMaxPagar = parseFloat(document.getElementById("COB_MAX_PAGO").value);
    if (dblMaxPagar < 0) {
        alert("El pago minimo debe ser positivo o cero");
        document.getElementById("COB_MAX_PAGO").focus();
    } else {
        dblCantidadMaximaPago = (document.getElementById("COB_MAX_PAGO").value);

        var dblTotal = parseFloat(document.getElementById("COB_TOTAL2").value);
        if (isNaN(dblCantidadMaximaPago))
        {
            dblCantidadMaximaPago = 0.0;
        }
        if (isNaN(dblFaltaXPagar))
        {
            dblFaltaXPagar = 0.0;
        }
        if (dblCantidadMaximaPago == 0)
        {
            dblFaltaXPagar = 0.0;
        }
        dblFaltaXPagar = dblCantidadMaximaPago - dblTotal;

        document.getElementById("COB_MAX_PAGO").value = FormatNumber(dblCantidadMaximaPago, intNumdecimal, true);
        document.getElementById("COB_FALTA_MAX").value = FormatNumber(dblFaltaXPagar, intNumdecimal, true);


    }
}

/**
 * trae la venta seleccionada para su pago
 */

function searchTrxMasivado_() {
    var Sichecked = false;
    var boolHaceOperacion = true;
    var dblTasaCambio = document.getElementById("TASA_CAMBIO_PAGO").value;
    var strOperacion = document.getElementById("COB_OPERACION").value;
    if (Sichecked) {

    } else {
        if (d.getElementById("COB_MONEDA_PAGO").value == 0) {
            boolHaceOperacion = false;
            alert(lstMsg[305]);
        }
    }
    if (boolHaceOperacion) {
        itemIdCob = 0;
        var strCte = d.getElementById("COB_CTE").value;
        var strFecha1 = d.getElementById("COB_FECHAS1").value;
        var strFecha2 = d.getElementById("COB_FECHAS2").value;
        var strMoneda = d.getElementById("COB_MONEDA").value;
        var strPOST = "&TKT_MONEDA=" + strMoneda;
        if (strFecha1 != "") {
            strPOST += "&TKT_FECHA1=" + strFecha1;
        }

        if (strFecha2 == "") {
            strFecha2 = FechaActualCob();
        }

        strPOST += "&TKT_FECHA2=" + strFecha2;
        ValidaClean("COB_CTE");
        if (strCte == 0) {
            ValidaShow("COB_CTE", "NECESITA SELECCIONAR UN CLIENTE");
        } else {

            //limpiamos datos
            strPOST += "&CT_ID=" + strCte;

            var grid = jQuery("#GR_VAL_FAC");
            var id = grid.getGridParam("selrow");
            var lstVal = grid.getRowData(id);
            var stridFactura = lstVal.FAC_ID;

            strPOST += "&id_vta=" + stridFactura;
            resetCobroMas(false);
            $.ajax({
                type: "POST",
                data: strPOST,
                scriptCharset: "utf-8",
                contentType: "application/x-www-form-urlencoded;charset=utf-8",
                cache: false,
                dataType: "xml",
                url: "ERP_Cobros.jsp?id=20",
                beforeSend: function () {
                    $("#dialogWait").dialog("open");
                },
                success: function (datos) {
                    var objsc = datos.getElementsByTagName("vta")[0];
                    var lstTiks = objsc.getElementsByTagName("datos");
                    for (i = 0; i < lstTiks.length; i++) {
                        var obj = lstTiks[i];
                        //Solo mostramos los documentos con saldo
                        var dblPagos = obj.getAttribute('FAC_TOTAL') - obj.getAttribute('FAC_SALDO');
                        var strFecha = obj.getAttribute('FAC_FECHA');

                        var dblSaldoCambio = 0;
                        if (strOperacion == "M") {
                            dblSaldoCambio = parseFloat(obj.getAttribute('FAC_SALDO')) * parseFloat(dblTasaCambio);
                        } else {
                            dblSaldoCambio = parseFloat(obj.getAttribute('FAC_SALDO')) / parseFloat(dblTasaCambio);
                        }
                        var datarow = {
                            COBD_ID: obj.getAttribute('FAC_ID'),
                            COBD_FOLIO: obj.getAttribute('FOLIO'),
                            COBD_FECHA: strFecha,
                            COBD_TOTAL: obj.getAttribute('FAC_TOTAL'),
                            COBD_PAGOS: dblPagos,
                            COBD_SALDO: obj.getAttribute('FAC_SALDO'),
                            COBD_SALDO_CAMBIO: dblSaldoCambio,
                            COBD_TIPO: obj.getAttribute('TIPO_DOC'),
                            COBD_CTAS_IMPORTE: 0.0,
                            COBD_CTAS_IMP_CAMBIO: 0.0,
                            COBD_CTAS_MONEDA: 0
                        };
                        //Anexamos el registro al GRID
                        itemIdCob++;
                        jQuery("#COB_GRID1").addRowData(itemIdCob, datarow, "last");
                    }
                    document.getElementById("ANTICIPO_USO").value = document.getElementById("ANTICIPO_TOTAL").value;
                    document.getElementById("COB_FALTA_MAX").value = document.getElementById("COB_MAX_PAGO").value;
                    document.getElementById("TASA_CAMBIO_PAGO").disabled = false;
                    $("#dialogWait").dialog("close");
                },
                error: function (objeto, quepaso, otroobj) {
                    alert(":ptoExist:" + objeto + " " + quepaso + " " + otroobj);
                    $("#dialogWait").dialog("close");
                }
            });
        }
        document.getElementById("COB_TOTAL2").value = 0.0;
        var dblMaxPagar = parseFloat(document.getElementById("COB_MAX_PAGO").value);
        if (dblMaxPagar < 0) {
            alert("El pago minimo debe ser positivo o cero");
            document.getElementById("COB_MAX_PAGO").focus();
        } else {
            CalculaFsltaXPagarCob_();
        }
    } else {

        d.getElementById("COB_MONEDA").focus();
    }
}

/**
 * calcula falta por pcgar cob
 */
function CalculaFsltaXPagarCob_() {

    dblCantidadMaximaPago = (document.getElementById("COB_MAX_PAGO").value);

    var dblTotal = parseFloat(document.getElementById("COB_TOTAL2").value);
    if (isNaN(dblCantidadMaximaPago)) {
        dblCantidadMaximaPago = 0.0;
    }
    if (isNaN(dblFaltaXPagar)) {
        dblFaltaXPagar = 0.0;
    }
    if (dblCantidadMaximaPago == 0) {
        dblFaltaXPagar = 0.0;
    }
    dblFaltaXPagar = dblCantidadMaximaPago - dblTotal;

    document.getElementById("COB_MAX_PAGO").value = FormatNumber(dblCantidadMaximaPago, intNumdecimal, true);
    document.getElementById("COB_FALTA_MAX").value = FormatNumber(dblFaltaXPagar, intNumdecimal, true);
}

/**
 * guardar la venta
 */
/**Guarda el pago MOSTRANDO primero las formas de pago*/
function SaveCob_() {

    var strMetPago = parseInt(document.getElementById("FORMPAG").value); //forma de pago
    var intMoneda = parseInt(document.getElementById("COB_MONEDA").value);
    var intBancoSel = parseInt(document.getElementById("COB_BCO").value);
    var intMonedaPago = parseInt(document.getElementById("COB_MONEDA_PAGO").value);
    if (strMetPago == "") {
        alert("Falta seleccionar el metodo de pago");
        return false;
    }
    if (intBancoSel != 0) {
        alert(lstMsg[306]);
    }
    if (intMoneda == 0) {
        alert("Falta seleccionar la moneda del documento");
        return false;
    }
    if (intMonedaPago == 0) {
        alert("Falta seleccionar la moneda de pago");
        return false;
    }
    document.getElementById("SioNO_inside").innerHTML = "¿Confirmar operación?";
    $("#SioNO").dialog("open");
    document.getElementById("btnSI").onclick = function () {
        $("#SioNO").dialog("close");

        SaveCobDo_(1);
        return true;

    };
    document.getElementById("btnNO").onclick = function () {
        $("#SioNO").dialog("close");
    };
}

/**Guarda el pago*/
function SaveCobDo_(Timbrar) {

    $("#dialogWait").dialog("open");
    var dblDev = 0;
    var dblPagado = 0;
    var dblxCobrar = 0;
    var strPOST = "";
    var $tabs = $('#tabsCOBROS').tabs();
    var selected = $tabs.tabs('option', 'active');
    //Si el tipo de cobranza es 1 es por transaccion sino
    var intIdOpcionOnly = 0;

    if (document.getElementById("FIRST_ONLY_ONE") != null) {

        intIdOpcionOnly = document.getElementById("FIRST_ONLY_ONE").value;

    }
    if (selected == intIdOpcionOnly) {

        dblDev = 0.0;
        dblPagado = parseFloat(d.getElementById("COB_TOTAL").value) - dblDev;
        //GENERAMOS peticion POST
        strPOST = "idTrx=" + d.getElementById("COB_TRX").value;
        strPOST += "&Timbrar=" + Timbrar;
        strPOST += "&TipoDoc=" + encodeURIComponent(d.getElementById("COB_TIPO").value);
        strPOST += "&MONTOPAGO=" + dblPagado;
        strPOST += "&FECHA=" + (d.getElementById("COB_FECHATRX").value);
        strPOST += "&BC_ID=" + (d.getElementById("COB_BCO").value);
        strPOST += "&NOTAS=" + encodeURIComponent(d.getElementById("COB_CONCEPTO").value);
        strPOST += "&MONEDA=1";
        strPOST += "&TASAPESO=1";
        strPOST += "&MC_METODODEPAGO=PPD";
        strPOST += "&MC_FORMADEPAGO=" + document.getElementById("FORMPAG").value;


        //Pagos Mandamos las 5 formas de pago
        strPOST += "&COUNT_PAGOS=1";
        //efectivo
        strPOST += "&MCD_MONEDA1=1";
        strPOST += "&MCD_FOLIO1=";
        strPOST += "&MCD_FORMAPAGO1=" + document.getElementById("FORMPAG").value;
        if (document.getElementById("FORMPAG") == "02") {
            strPOST += "&MCD_NOCHEQUE1=" + d.getElementById("COB_NOCHEQUE").value;
        } else {
            strPOST += "&MCD_NOCHEQUE1=";
        }
        strPOST += "&MCD_BANCO1=";
        strPOST += "&MCD_NOTARJETA1=";
        strPOST += "&MCD_TIPOTARJETA1=";
        strPOST += "&MCD_IMPORTE1=" + (dblPagado);
        strPOST += "&MCD_TASAPESO1=1";
        strPOST += "&MCD_CAMBIO1=0";

        //Desactivamos los botones
        document.getElementById("COB_BTN4").disabled = true;
        document.getElementById("TOOLBAR").style.display = "none";

        //Hacemos la peticion por POST
        $.ajax({
            type: "POST",
            data: encodeURI(strPOST),
            scriptCharset: "utf-8",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "html",
            url: "Cobros.do?id=1",
            success: function (dato) {
                dato = trim(dato);
                if (Left(dato, 3) == "OK.") {
//                    openFormat("COBROS", "PDF", CreaHidden("MC_ID", dato.replace("OK.", "")))
                    resetCobroInd();

                    d.getElementById("COB_TRX").value = 0;
                    d.getElementById("COB_TRX").focus();
                    d.getElementById("COB_TRX").select();

                    $("#dialogPagos").dialog("close");

//                    //VALIDA LA FACTURAM, QUE NO TENGA SALDO
//                    validaSaldoFac();
                    checkVta();
                } else {
                    alert(dato);
                }
                //Habilitamos botones
                document.getElementById("COB_BTN4").disabled = false;
                document.getElementById("TOOLBAR").style.display = "block";
                $("#dialogWait").dialog("close");
            },
            error: function (objeto, quepaso, otroobj) {
                alert(":PAGOS:" + objeto + " " + quepaso + " " + otroobj);
                $("#dialogWait").dialog("close");
            }
        });

    } else {

        var intIdOper = 3;
        var grid = jQuery("#COB_GRID1");
        var arr = grid.getDataIDs();

        var intContaPays = 0;
        for (var i = 0; i < arr.length; i++) {
            var id = arr[i];
            var lstRow = grid.getRowData(id);
            if (lstRow.COBD_CTAS_IMPORTE > 0.0)
            {
                intContaPays++;
            }

        }

        dblDev = 0.0;
        dblPagado = parseFloat(d.getElementById("COB_TOTAL2").value);
        dblxCobrar = parseFloat(d.getElementById("COB_TOTAL2").value);
//        }
        if ((dblPagado == dblxCobrar || ((dblPagado != dblxCobrar) && intContaPays == 1)) && dblxCobrar > 0) {
            //Armamos peticion para guardar los cobros
            strPOST += "FECHA=" + (d.getElementById("COB_FECHA").value);
            strPOST += "&Timbrar=" + Timbrar;
            strPOST += "&BC_ID=" + (d.getElementById("COB_BCO").value);
            strPOST += "&NOTAS=" + encodeURIComponent(d.getElementById("COB_CONCEPTO").value);
            strPOST += "&MONEDA=" + (d.getElementById("TIPO_MONEDA_BANC").value);
            strPOST += "&TASAPESO=" + (d.getElementById("TASA_CAMBIO_PAGO").value);
            strPOST += "&MONTOPAGOTOTAL=" + dblPagado;
            strPOST += "&MC_METODODEPAGO=PPD";
            strPOST += "&MC_FORMADEPAGO=" + document.getElementById("FORMPAG").value;
            strPOST += "&TASAPESO=1";
            strPOST += "&CTE_ID=" + (d.getElementById("COB_CTE").value);

            strPOST += "&MONTOPAGOTOTALAMONEDA=" + (d.getElementById("COB_TOTAL2").value);
            strPOST += "&MONEDAAPAGAR=" + (d.getElementById("COB_MONEDA_PAGO").value);
            //GENERAMOS peticion POST
            var strIdTrx = lstRow.COBD_ID;
            for (var i = 0; i < arr.length; i++) {
                var id = arr[i];
                var lstRow = grid.getRowData(id);
                if (lstRow.COBD_CTAS_IMPORTE > 0.0) {

                    strPOST += "&idTrx=" + lstRow.COBD_ID;
                    strPOST += "&TipoDoc=" + encodeURIComponent((lstRow.COBD_TIPO == "FACTURA") ? 1 : 2);
                    strPOST += "&MONTOPAGO=" + lstRow.COBD_CTAS_IMPORTE;
                }
            }
            //Validamos si solo esta pagando una operacion para guardar un pago normal
            var strNomForm = "COBROSMAS";
            var strNomField = "MCM_ID";

            if (d.getElementById("COB_FALTA_MAX").value > 0.0) {
                strPOST += "&CTE_ID=" + (d.getElementById("COB_CTE").value);
                strPOST += "&CTE_ANTICIPO=" + (d.getElementById("COB_FALTA_MAX").value);
            }
            //Pagos Mandamos las 4 formas de pago
            strPOST += "&COUNT_PAGOS=1";
            //efectivo                
            strPOST += "&MCD_MONEDA1=1";
            strPOST += "&MCD_FOLIO1=";
            strPOST += "&MCD_FORMAPAGO1=" + document.getElementById("FORMPAG").value;
            if (document.getElementById("METPAG") == "02") {
                strPOST += "&MCD_NOCHEQUE1=" + d.getElementById("COB_NOCHEQUE").value;
            } else {
                strPOST += "&MCD_NOCHEQUE1=";
            }
            strPOST += "&MCD_BANCO1=" + d.getElementById("COB_BCO").value;
            strPOST += "&MCD_NOTARJETA1=";
            strPOST += "&MCD_TIPOTARJETA1=";
            strPOST += "&MCD_IMPORTE1=" + (parseFloat(d.getElementById("COB_TOTAL2").value));
            strPOST += "&MCD_TASAPESO1=1";
            strPOST += "&MCD_CAMBIO1=0.0";

            //Hacemos la peticion por POST
            $.ajax({
                type: "POST",
                data: encodeURI(strPOST),
                scriptCharset: "utf-8",
                contentType: "application/x-www-form-urlencoded;charset=utf-8",
                cache: false,
                dataType: "html",
                url: "Cobros.do?id=" + intIdOper,
                success: function (dato) {
                    dato = trim(dato);
                    if (Left(dato, 3) == "OK.") {
                        var strIdComplemento = dato.replace("OK.", "");

                        //ENVIA COMPLEMENTO POR CORREO
                        sendComplementoPago(strIdComplemento);

                        console.log("ID DE LA VENTA QUE SE LE HARA EL COMPLEMENTO: " + strIdTrx);
                        //VALIDA LA FACTURAM, QUE NO TENGA SALDO
                        validaSaldoFac(strIdTrx);
//                        openFormat(strNomForm, "PDF", CreaHidden(strNomField, dato.replace("OK.", "")));
//                        openFormat("COBROSMAS", "PDF", CreaHidden("MCM_ID", 1));
                        jQuery("#COB_GRID1").clearGridData();

                        document.getElementById("COB_MAX_PAGO").value = "0.0";
                        document.getElementById("COB_FALTA_MAX").value = "0.0";
                        document.getElementById("ANTICIPO_TOTAL").value = "0.0";
                        document.getElementById("ANTICIPO_USO").value = "0.0";
                        document.getElementById("COB_TOTAL2").value = "0.0";
                        itemIdCob = 0;
                        resetCobroMas(true);
                        $("#dialogTMK1").dialog("close");
                        //LimpiarGrid();
                        $("#dialogPagos").dialog("close");
                        resetCobroInd();
                        exitComplemento();


                    } else {
                        alert(dato);
                    }
                    $("#dialogWait").dialog("close");
                },
                error: function (objeto, quepaso, otroobj) {
                    alert(":guada pago:" + objeto + " " + quepaso + " " + otroobj);
                    $("#dialogWait").dialog("close");
                }
            });
        } else {
            alert(lstMsg[10]);
            $("#dialogWait").dialog("close");
        }
    }
}

/**
 * valida venta al saldarla
 */
function validaSaldoFac(idVta) {

    var strPost = "";
    if (idVta != null) {
        if (idVta != "" || idVta != undefined || idVta != "undefined") {

            strPost = "idTrx=" + idVta;
            $.ajax({
                type: "POST",
                data: strPost,
                scriptCharset: "UTF-8",
                contentType: "application/x-www-form-urlencoded;charset=utf-8",
                cache: false,
                dataType: "html",
                url: "COFIDE_ValidaFactura.jsp?id=9",
                beforeSend: function () {
                    $("#dialogWait").dialog("open");
                },
                success: function (datos) {
                    datos = trim(datos);
                    if (datos != "OK") {
                        alert("Hubó un problema al validar el saldo de la Factura.");
                    } else {
                        getFacturasValidar();
                        $("#dialogTMK1").dialog("close");
                    }

                    $("#dialogWait").dialog("close");
                }, error: function (objeto, quepaso, otroobj) {
                    alert(":pto=9 .:valida saldo:. " + objeto + " " + quepaso + " " + otroobj);
                    $("#dialogWait").dialog("close");
                }
            }); //fin del ajax

        }
    }

}


/*
 * Envia por correo
 * la complemento con el xml
 */
function sendComplementoPago(strIdFac) {
    var strPost = "";
    strPost += "&id_complemento=" + strIdFac;

    $.ajax({
        type: "POST",
        scriptCharset: "UTF-8",
        data: strPost,
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "html",
        url: "COFIDE_Programacionmails.jsp?ID=16",
        beforeSend: function () {
            $("#dialogWait").dialog("open");
        },
        success: function (datos) {
            datos = trim(datos);
            if (datos != "OK") {
                alert("ERROR: al enviar el complemento" + datos);
            }
            $("#dialogWait").dialog("close");
        }, error: function (objeto, quepaso, otroobj) {
            alert(":pto=16:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }});
}