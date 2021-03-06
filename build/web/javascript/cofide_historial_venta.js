function cofide_historial_venta() {

}
function initHistorialVenta() {
    // llena el combo para el filtro
    var objHoraCombo = document.getElementById("HV_FILTRO");
    select_clear(objHoraCombo);
    select_add(objHoraCombo, "Seleccione una opción", "0");
    select_add(objHoraCombo, "RAZÖN SOCIAL", "1");
    select_add(objHoraCombo, "FOLIO FACTURA", "2");
    select_add(objHoraCombo, "FOLIO TICKET", "3");
    select_add(objHoraCombo, "NOMBRE CURSO", "4");
    select_add(objHoraCombo, "ID CURSO", "5");
}

function onChangeFiltroHistVta() {
    console.log("VALOR : " + document.getElementById("HV_FILTRO").value);
    console.log("VALOR : " + document.getElementById("HV_SEARCH").value);
    var strPost = "";
    if (document.getElementById("HV_FILTRO").value == 4) {
        console.log("ENTRO ...");
        $(function () {
            $('#HV_SEARCH').autocomplete({disabled: false});
            strPost = document.getElementById("HV_SEARCH").value;
            console.log("busca...: " + strPost);
            $("#HV_SEARCH").autocomplete({source: "COFIDE_Historial.jsp?ID=7&term=" + strPost, minLength: 2});
        });
    } else {
        strPost = "";
        console.log("DESACTIVA");
        $('#HV_SEARCH').autocomplete({disabled: true});
    }
}

var itemIdCob = 0;
function consultaHistoricoVtas() { //historial de ventas general
    var strPagado = "";
    var strStatus = "";
    var strTipoDoc = "";
    $("#dialogWait").dialog("open");
    var strSearch = document.getElementById("HV_SEARCH").value;
    var strFiltro = document.getElementById("HV_FILTRO").value;
    var strFecIni = document.getElementById("HV_FECINI").value;
    var strFecFin = document.getElementById("HV_FECFIN").value;
    if (document.getElementById("HV_FILTRO").value == 4) {
        strSearch = document.getElementById("HV_SEARCH").value.split(" /", 1);
    }
    var strPOST = "&strFechaInicial=" + strFecIni;
    strPOST += "&strFechaFinal=" + strFecFin;
    strPOST += "&busqueda=" + strSearch;
    strPOST += "&filtro=" + strFiltro;
    $.ajax({
        type: "POST",
        data: strPOST,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "COFIDE_Historial.jsp?ID=2",
        success: function (datos) {
            jQuery("#GRDIHVENTA").clearGridData();
            var objsc = datos.getElementsByTagName("Ventas")[0];
            var lstVtas = objsc.getElementsByTagName("datos");
            for (var i = 0; i < lstVtas.length; i++) {
                var obj = lstVtas[i];
                //estatus de la venta
                if (obj.getAttribute("FAC_PROMO") == "1") {
                    strPagado = "PROMOCIÓN";
                } else {
                    if (obj.getAttribute("FAC_PAGADO") == 0 && obj.getAttribute("FAC_VALIDA") == 0 && obj.getAttribute("FAC_ARCHIVO") == "") {

                        if (obj.getAttribute("FACTURAR") == "1") {

                            strPagado = "SIN PAGO / P-FACT";
                        } else {

                            strPagado = "SIN PAGO";
                        }
                    }
                    if (obj.getAttribute("FAC_PAGADO") == 1 && obj.getAttribute("FAC_VALIDA") == 0 && obj.getAttribute("FAC_ARCHIVO") != "") {

                        if (obj.getAttribute("FACTURAR") == "1") {

                            strPagado = "REVISIÓN / P-FACT";
                        } else {

                            strPagado = "REVISIÓN";
                        }
                    }
                    if (obj.getAttribute("FAC_PAGADO") == 0 && obj.getAttribute("FAC_VALIDA") == 0 && obj.getAttribute("FAC_ARCHIVO") != "") {

                        if (obj.getAttribute("FACTURAR") == "1") {

                            strPagado = "DENEGADO / P-FACT";
                        } else {

                            strPagado = "DENEGADO";
                        }
                    }
                    if (obj.getAttribute("FAC_PAGADO") == 1 && obj.getAttribute("FAC_VALIDA") == 1 && obj.getAttribute("FAC_ARCHIVO") != "") {

                        if (obj.getAttribute("FACTURAR") == "1") {

                            strPagado = "PAGADO / P-FACT";
                        } else {

                            strPagado = "PAGADO";
                        }
                    }
                }
                //estatus del documento
                if (obj.getAttribute("CANCEL") == 1 && obj.getAttribute("FAC_ANULADA") == 0) {
                    strStatus = "En proceso de cancelación";
                } else {
                    if (obj.getAttribute("FAC_ANULADA") == 1) {
                        strStatus = "Cancelada";
                    } else {
                        strStatus = "Activa";
                    }
                }
                if (obj.getAttribute("DOC_TIPO") == "0") {
                    strTipoDoc = "TICKET";
                }
                if (obj.getAttribute("DOC_TIPO") == "1") {
                    strTipoDoc = "FACTURA";
                }
                if (obj.getAttribute("DOC_TIPO") == "2") {
                    strTipoDoc = "RESERVACIÓN";
                }
                var Row = {
                    HV_CONTADOR: obj.getAttribute("CONTADOR"),
                    PD_ID: obj.getAttribute("FAC_ID"),
                    PD_FECHA: obj.getAttribute("FAC_FECHA"),
                    PD_HORA: obj.getAttribute("FAC_HORA"),
                    PD_FOLIO: obj.getAttribute("FAC_FOLIO"),
                    PD_RAZONSOCIAL: obj.getAttribute("RAZONSOCIAL"),
                    EMP_ID: obj.getAttribute("AGENTE"),
                    FAC_TOTAL: obj.getAttribute("FAC_TOTAL"),
                    FAC_METODODEPAGO: obj.getAttribute("FAC_METODODEPAGO"),
                    PD_TOTAL: obj.getAttribute("FAC_TOTAL"),
                    DOC_TIPO: strTipoDoc,
                    FAC_PAGADO: strPagado,
                    FAC_STATUS: strStatus,
                    FAC_ID: obj.getAttribute("FAC_FOLIO"),
                    FAC_SALDO: "0.00",
                    FAC_IMP_P: "0.00",
                    CT_ID: obj.getAttribute("CT_ID"),
                    FAC_ID_MASTER: obj.getAttribute("FAC_ID_M"),
                    SC_ID: obj.getAttribute("SC_ID")
                };
                itemIdCob++;
                jQuery("#GRDIHVENTA").addRowData(itemIdCob, Row, "last");
            }
            getIndicadores();
            $("#dialogWait").dialog("close");
        }, error: function (objeto, quepaso, otroobj) {
            alert(":ptoExist:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }});
}

//function doFacturaHistoricoVtas() {
//    var strPost = "";
//    var grid = jQuery("#GRDIHVENTA");
//    if (grid.getGridParam("selrow") != null) {
//        var id = grid.getGridParam("selrow");
//        var lstRow = grid.getRowData(id);
//        strPost += "&SC_ID=" + lstRow.SC_ID + "";
//        strPost += "&CT_ID=" + lstRow.CT_ID + "";
//        strPost += "&FAC_FECHA=" + lstRow.PD_FECHA + "";
//        strPost += "&FAC_FOLIO=" + lstRow.PD_FOLIO + "";
//        strPost += "&FAC_TOTAL=" + lstRow.PD_TOTAL + "";
//        strPost += "&FAC_NOTAS=";
//        strPost += "&TKT_ID=" + lstRow.PD_ID + "";
//        strPost += "&FAC_METODODEPAGO=" + lstRow.FAC_METODODEPAGO + "";
//        strPost += "&FAC_NUMCUENTA=";
//        strPost += "&FAC_FORMADEPAGO=NO IDENTIFICADO";
//        if (lstRow.PD_FOLIO == 0) {
//            alert("NO SE PUEDE RE-FACTURAR UNA FACTURA. SELECCIONE UN TICKET");
//        } else {
//            $.ajax({
//                type: "POST",
//                data: strPost,
//                scriptCharset: "utf-8",
//                contentType: "application/x-www-form-urlencoded;charset=utf-8",
//                cache: false,
//                dataType: "html",
//                url: "VtasMov.do?id=3",
//                success: function (dato) {
//                    dato = trim(dato);
//                    if (Left(dato, 3) == "OK.") {
//                        var strHtml = CreaHidden("FAC_ID", dato.replace("OK.", ""));
//                        openWhereverFormat("ERP_SendInvoice?id=" + dato.replace("OK.", ""), "FACTURA", "PDF", strHtml);
//                    } else {
//                        alert(dato);
//                    }
//                    $("#dialogWait").dialog("close");
//                }, error: function (objeto, quepaso, otroobj) {
//                    alert(":ptotFact:" + objeto + " " + quepaso + " " + otroobj);
//                    $("#dialogWait").dialog("close");
//                }});
//        }
//    } else {
//        alert("Debe seleccionar un ticket");
//    }
//}
//function doFacturaHistoricoVtasCte() {
//    var strPost = "";
//    var grid = jQuery("#H_VENTA_CTE");
//    if (grid.getGridParam("selrow") != null) {
//        var id = grid.getGridParam("selrow");
//        var lstRow = grid.getRowData(id);
//        strPost += "&SC_ID=" + lstRow.SC_ID + "";
//        strPost += "&CT_ID=" + lstRow.CT_ID + "";
//        strPost += "&FAC_FECHA=" + lstRow.PD_FECHA + "";
//        strPost += "&FAC_FOLIO=" + lstRow.PD_FOLIO + "";
//        strPost += "&FAC_TOTAL=" + lstRow.PD_TOTAL + "";
//        strPost += "&FAC_NOTAS=";
//        strPost += "&TKT_ID=" + lstRow.PD_ID + "";
//        strPost += "&FAC_METODODEPAGO=" + lstRow.FAC_METODODEPAGO + "";
//        strPost += "&FAC_NUMCUENTA=";
//        strPost += "&FAC_FORMADEPAGO=NO IDENTIFICADO";
//        if (lstRow.PD_FOLIO == 0) {
//            alert("NO SE PUEDE RE-FACTURAR UNA FACTURA. SELECCIONE UN TICKET");
//        } else {
//            $.ajax({
//                type: "POST",
//                data: strPost,
//                scriptCharset: "utf-8",
//                contentType: "application/x-www-form-urlencoded;charset=utf-8",
//                cache: false,
//                dataType: "html",
//                url: "VtasMov.do?id=3",
//                success: function (dato) {
//                    dato = trim(dato);
//                    if (Left(dato, 3) == "OK.") {
//                        var strHtml = CreaHidden("FAC_ID", dato.replace("OK.", ""));
//                        openWhereverFormat("ERP_SendInvoice?id=" + dato.replace("OK.", ""), "FACTURA", "PDF", strHtml);
//                    } else {
//                        alert(dato);
//                    }
//                    $("#dialogWait").dialog("close");
//                }, error: function (objeto, quepaso, otroobj) {
//                    alert(":ptotFact:" + objeto + " " + quepaso + " " + otroobj);
//                    $("#dialogWait").dialog("close");
//                }});
//        }
//    } else {
//        alert("Debe seleccionar un ticket");
//    }
//}

function MMSendMailHistoricoVtas() {
    var grid = jQuery("#GRDHIVENTA");
    var id = grid.getGridParam("selrow");
    var usaTempl = 0;
    if (grid.getGridParam("selrow") == null) {
        alert(lstMsg[58]);
    } else {
        var strPD_ID = "";
        var lstRow = grid.getRowData(id);
        strPD_ID += lstRow.FAC_ID;
        if (lstRow.FAC_ID == 0) {
            alert("NO SE PUEDE ENVIAR UN TICKET. SELECCIONE UNA FACTURA");
        } else {
            $("#dialogWait").dialog("open");
            var intVIEW_CC = 0;
            $.ajax({type: "POST", data: encodeURI("LST_FAC_ID=" + strPD_ID + "&VIEW_COPIA=0" + "&VIEW_ASUNTO=" + "&VIEW_MAIL=" + "&VIEW_CC=" + intVIEW_CC + "&VIEW_TEMPLETE=" + usaTempl), scriptCharset: "utf-8", contentType: "application/x-www-form-urlencoded;charset=utf-8", cache: false, dataType: "xml", url: "ERP_MailMasivo.jsp?id=1", success: function (datos) {
                    var objsc = datos.getElementsByTagName("vta_mails")[0];
                    var vta_mail = objsc.getElementsByTagName("vta_mail");
                    for (i = 0; i < vta_mail.length; i++) {
                        var obj = vta_mail[i];
                        if (obj.getAttribute("res") != "OK") {
                            alert(obj.getAttribute("id") + " " + obj.getAttribute("res"));
                        }
                    }
                    alert("Mails Enviados....");
                    $("#dialogWait").dialog("close");
                }, error: function (objeto, quepaso, otroobj) {
                    alert(":pto9:" + objeto + " " + quepaso + " " + otroobj);
                    $("#dialogWait").dialog("close");
                }});
        }
    }
}

//function vtaPagarCurso() {
//    if (document.getElementById("CT_NO_CLIENTE").value != "0" && document.getElementById("CT_NO_CLIENTE").value != "") {
//        var grid = jQuery("#GRDIHVENTA");
//        if (grid.getGridParam("selrow") != null) {
//            var lstRow = grid.getRowData(grid.getGridParam("selrow"));
//            var strFAC_ID = 0;
//            var strTKT_ID = 0;
//            var DOC_TOTAL = 0;
//            var TIPO_DOC = "";
//            var strFechaDoc = "";
//            var ID_tkt_fac = "";
//            if (lstRow.FAC_STATUS == "Activa") {
//                if (lstRow.FAC_PAGADO == "Pendiente") {
//                    DOC_TOTAL = lstRow.PD_TOTAL;
//                    TIPO_DOC = lstRow.DOC_TIPO;
//                    strFAC_ID = lstRow.FAC_ID;
//                    strTKT_ID = lstRow.PD_FOLIO;
//                    strFechaDoc = lstRow.PD_FECHA;
//                    $("#dialogPagos").dialog("open");
//                    var idCliente = document.getElementById("CT_NO_CLIENTE").value;
//                    var dblAnticipo = DOC_TOTAL;
//                    var intMonedaBanco = 1; //document.getElementById("BCO_MONEDA").value;
//                    var dblTasaCambio = 1; //document.getElementById("PARIDAD").value;
//                    var intBancoID = 1; //document.getElementById("BC_ID").value;
//                    if (TIPO_DOC == 1) { //0=ticket & 1= factura
//                        ID_tkt_fac = strFAC_ID;
//                    } else {
//                        ID_tkt_fac = strTKT_ID;
//                    }
////                var strPOST = "&idTrx=" + strFAC_ID;
//                    var strPOST = "&idTrx=" + ID_tkt_fac;
//                    strPOST += "&COUNT_PAGOS=" + 0;
//                    strPOST += "&TipoDoc=" + TIPO_DOC;
//                    strPOST += "&FECHA=" + strFechaDoc;
//                    strPOST += "&NOTAS=" + "";
//                    strPOST += "&MONEDA=" + intMonedaBanco;
//                    strPOST += "&TASAPESO=" + dblTasaCambio;
//                    strPOST += "&MONTOPAGO=" + dblAnticipo;
//                    strPOST += "&BC_ID=" + intBancoID;
//                    strPOST += "&IdCte=" + idCliente;
//                    strPOST += "&intAnticipo=" + 1;
//                    $.ajax({
//                        type: "POST",
//                        data: encodeURI(strPOST),
//                        scriptCharset: "utf-8",
//                        contentType: "application/x-www-form-urlencoded;charset=utf-8",
//                        cache: false,
//                        dataType: "html",
//                        url: "ERP_Cobros.jsp?id=1",
//                        success: function (dato) {
//                            if (Left(dato, 3) == "OK.") {
//                                //envio de mail
//                                $.ajax({
//                                    type: "POST",
//                                    data: "fac_id=" + ID_tkt_fac + "&tipo_doc=" + TIPO_DOC,
//                                    scriptCharset: "utf-8",
//                                    contentType: "application/x-www-form-urlencoded;charset=utf-8",
//                                    cache: false,
//                                    dataType: "xml",
//                                    url: "COFIDE_Telemarketing_vta.jsp?ID=9",
//                                });
//                                //envio de mail
//                                consultaHistoricoVtas(); //recarga pantalla
//                                $("#dialogPagos").dialog("close");
//                            } else {
//                                alert(dato);
//                            }
//                            $("#dialogWait").dialog("close");
//                        },
//                        error: function (objeto, quepaso, otroobj) {
//                            alert(":pto9:" + objeto + " " + quepaso + " " + otroobj);
//                            $("#dialogWait").dialog("close");
//                        }
//                    });
//                } else {
//                    alert("Este Documento ya ha sido pagado");
//                }
//            } else {
//                alert("Este Documento No se puede pagar, porque a sido cancelado");
//            }
//        } else {
//            alert("Debe seleccionar una partida en la tabla \"HISTORIAL DE VENTAS\"");
//        }
//    } else {
//        alert("Debe seleccionar un Cliente");
//    }
//}
//function CancelMov() {
//    var strFAC_ID = 0;
//    var strTipoDoc = "";
//    var strTKT_ID = 0;
//    var strPost = "";
//
//    var grid = jQuery("#GRDIHVENTA");
//    if (grid.getGridParam("selrow") != null) {
//        var lstRow = grid.getRowData(grid.getGridParam("selrow"));
//        strFAC_ID = lstRow.PD_ID;
//        strTipoDoc = lstRow.DOC_TIPO;
//        $("#dialogWait").dialog("open");
//        if (lstRow.FAC_PAGADO == "Pendiente") {
//
//            //inicio de si o no
//            document.getElementById("SioNO_inside").innerHTML = "¿Estas seguro de cancelar este documento!?";
//            $("#SioNO").dialog("open");
//            document.getElementById("btnSI").onclick = function () {
//                $("#SioNO").dialog("close");
//
//                strPost = "fac_id=" + strFAC_ID;
//                strPost += "&tipo_doc=" + strTipoDoc;
//                $.ajax({
//                    type: "POST",
//                    data: strPost,
//                    scriptCharset: "utf-8",
//                    contentType: "application/x-www-form-urlencoded;charset=utf-8",
//                    cache: false,
//                    dataType: "xml",
//                    url: "COFIDE_Telemarketing_vta.jsp?ID=10",
//                });
//                consultaHistoricoVtas();
//            };// fin si o no SI
//            document.getElementById("btnNO").onclick = function () {
//                $("#SioNO").dialog("close");
//            }; // fin de si o no NO
//
//        } else {
//            alert("Este Documento ya a sido pagado");
//        }
//        $("#dialogWait").dialog("close");
//    } else {
//        alert("Debe seleccionar una partida en la tabla \"HISTORIAL DE VENTAS\"");
//    }
//}

function CancelMov() {
    var strFAC_ID = 0;
    var strTipoDoc = "";
    var strStat = "";
    var strTKT_ID = 0;
    var strPost = "";
    var grid = jQuery("#GRDIHVENTA");
    if (grid.getGridParam("selrow") != null) {
        var lstRow = grid.getRowData(grid.getGridParam("selrow"));
        strFAC_ID = lstRow.PD_ID;
        strTipoDoc = lstRow.DOC_TIPO;
        strStat = lstRow.FAC_STATUS;
        $("#dialogWait").dialog("open");
        if (strStat == "Activa") {
            document.getElementById("SioNO_inside").innerHTML = "¿Estas seguro de cancelar este documento!?";
            $("#SioNO").dialog("open");
            document.getElementById("btnSI").onclick = function () {
                $("#SioNO").dialog("close");
                peticionCancelGral(strFAC_ID, strTipoDoc); //manda la solicutud de cancelación de la venta
            }; //fin de si o no SI
            document.getElementById("btnNO").onclick = function () {
                $("#SioNO").dialog("close");
            }; //sin de si o no NO
            $("#dialogWait").dialog("close");
        } else {
            if (strStat == "Cancelada") {
                alert("Este documento ya ha sido cancelado");
            } else {
                alert("Este documento se encuentra en proceso de cancelación");
            }
            $("#dialogWait").dialog("close");
        }
    } else {
        alert("Debe seleccionar una partida en la tabla \"HISTORIAL DE VENTAS\"");
    }
}

function dblClickVTA(id) {
//    document.getElementById("CT_GRID").value = "1"; //gral
    var strID_FAC_TKT = "";
    var strTipoDoc = "";
    var strNomMain = objMap.getNomMain();
    var grid = jQuery("#GRDIHVENTA"); //grid detalle
    var lstVal = grid.getRowData(id);
    if (strNomMain == "HVENTA") { //pantalla que lo contiene
        OpnEdit(document.getElementById("Ed" + strNomMain));
    } else {
        if (strNomMain == "C_TELEM") { //pantalla base
            if (lstVal.FAC_PAGADO != "PROMOCIÓN") {
                var strID_FAC_TKT = lstVal.PD_ID; //id fac o tkt
                var strTipoDoc = lstVal.DOC_TIPO; // tipo de documento
                var strEstatus = lstVal.FAC_STATUS; // estatus de documento
                var strIDMOV = lstVal.FAC_ID_MASTER; // id movimiento

                document.getElementById("CT_ID_FACTKT").value = strID_FAC_TKT; // se guarda el id en un hidden           
                document.getElementById("CT_TIPODOC").value = strTipoDoc; // se guarda el id en un hidden       
                document.getElementById("CT_ESTATUS_VTA").value = strEstatus; // se guarda el estatus de la venta  
                document.getElementById("CT_ID_MOV").value = strIDMOV; // se guarda el estatus de la venta      
                document.getElementById("CT_GRID").value = "1"; //gral
                OpnDiagVentaD();
            } else {
                alert("Es una promoción.");
            }
        }
    }
}

function OpnDiagVentaD() { //abrir los detalles de la venta
    var objSecModiVta = objMap.getScreen("NVENTA2");
    if (objSecModiVta != null) {
        objSecModiVta.bolActivo = false;
        objSecModiVta.bolMain = false;
        objSecModiVta.bolInit = false;
        objSecModiVta.idOperAct = 0;
    }
    OpnOpt("NVENTA2", "_ed", "dialog", false, false, true);
}

function CloseDialogHV() {
    $("#dialogCte").dialog("close");
}

function VtaAnulFact(FAC_ID) {
    if (FAC_ID != 0) {
        $.ajax({
            type: "POST",
            data: encodeURI("idAnul=" + FAC_ID),
            scriptCharset: "utf-8",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "html",
            url: "VtasMov.do?id=4",
            success: function (dato) {
                dato = trim(dato);
                if (dato != "OK") {
                    alert(dato);
                }
                $("#dialogWait").dialog("close");
            }, error: function (objeto, quepaso, otroobj) {
                alert(":pto9:" + objeto + " " + quepaso + " " + otroobj);
                $("#dialogWait").dialog("close");
            }});
    }
}
//manda al modulo de contabilidad para que cancele las facturas
function peticionCancelGral(strIdFac, strTipoDoc) {
    var strPost = "";
    if (strIdFac != "0") {

        strPost = "idAnul=" + strIdFac;
        strPost += "&tipo_doc=" + strTipoDoc;

        $.ajax({
            type: "POST",
            data: strPost,
            scriptCharset: "utf-8",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "html",
            url: "COFIDE_ValFac_Duplicados.jsp?id=8",
            beforeSend: function () {
                $("#dialogWait").dialog("open");
            },
            success: function (dato) {
                dato = trim(dato);
                if (dato != "OK") {
                    alert("Error: " + dato);
                } else {
                    alert("Se solicitó con éxito la cancelación de la venta con ID: " + strIdFac);
                    QuitaParticipantePts(strIdFac, strTipoDoc);
                    consultaHistoricoVtas();
                }
                $("#dialogWait").dialog("close");
            }, error: function (objeto, quepaso, otroobj) {
                alert(":pto=8: " + objeto + " " + quepaso + " " + otroobj);
                $("#dialogWait").dialog("close");
            }});
    }
}