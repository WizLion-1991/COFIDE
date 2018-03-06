/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var itemIdCont = 0;


function cofide_valida_facturas() {

}

function initValidaFacturas() {
    getFacturasValidar();
}

function getFacturasValidar() {
    var strAnio = document.getElementById("VF_ANIO").value;
    var strMes = document.getElementById("VF_MES").value;
    var strValidos = document.getElementById("VF_VALIDOS1").checked;
    var intTipoDoc = document.getElementById("VIEW_TIPO").value;
    var intCtId = document.getElementById("VIEW_CLIENTE").value;
    var strFolio = document.getElementById("VIEW_FOLIO").value;
    var intIdVenta = document.getElementById("VIEW_ID").value;
    var intValidos = 0;

    if (strValidos) {
        intValidos = 1;
    }
    var strPost = "strMes=" + strMes;
    strPost += "&strAnio=" + strAnio + "&Validos=" + intValidos;
    strPost += "&Validos=" + intValidos;
    strPost += "&intTipoDoc=" + intTipoDoc;
    strPost += "&intCtId=" + intCtId;
    strPost += "&strFolio=" + strFolio;
    strPost += "&intIdVenta=" + intIdVenta;

    document.getElementById("VF_EDITA_METODO").style.backgroundColor = "#ff6c00";
    document.getElementById("VF_EDITA_METODO").style.color = "#FFF";
    document.getElementById("VF_DOTIMBRADO").style.backgroundColor = "#ff6c00";
    document.getElementById("VF_DOTIMBRADO").style.color = "#FFF";
    document.getElementById("VF_COMPLEMENTO").style.backgroundColor = "#ff6c00";
    document.getElementById("VF_COMPLEMENTO").style.color = "#FFF";

    //RESERVACIÓN
    if (intTipoDoc == "6") {
        document.getElementById("VF_EDITA_METODO").parentNode.parentNode.style.display = "";
        document.getElementById("VF_DOTIMBRADO").parentNode.parentNode.style.display = "";
        document.getElementById("VF_COMPLEMENTO").parentNode.parentNode.style.display = "none";
        document.getElementById("VF_EDIT_FECHA").parentNode.parentNode.style.display = "";
    } else {
        //TICKET / FACTURA
        if (strValidos) {
            document.getElementById("VF_COMPLEMENTO").parentNode.parentNode.style.display = "none";
//            document.getElementById("VF_EDIT_FECHA").parentNode.parentNode.style.display = "none";
            document.getElementById("VF_DENEGAR").parentNode.parentNode.style.display = "none";
        } else {
            if (intTipoDoc == "2") {
                document.getElementById("VF_COMPLEMENTO").parentNode.parentNode.style.display = "none";
                document.getElementById("VF_AUTORIZA").parentNode.parentNode.style.display = "";
            } else {
                document.getElementById("VF_COMPLEMENTO").parentNode.parentNode.style.display = "";
//                document.getElementById("VF_AUTORIZA").parentNode.parentNode.style.display = "none";
            }
            document.getElementById("VF_EDIT_FECHA").parentNode.parentNode.style.display = "";
            document.getElementById("VF_DENEGAR").parentNode.parentNode.style.display = "";
        }
        document.getElementById("VF_EDITA_METODO").parentNode.parentNode.style.display = "none";
        document.getElementById("VF_DOTIMBRADO").parentNode.parentNode.style.display = "none";

    }

    if (intTipoDoc != 0) {

        $.ajax({
            type: "POST",
            data: strPost,
            scriptCharset: "utf-8",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "xml",
            url: "COFIDE_ValidaFactura.jsp?id=1",
            beforeSend: function () {
                $("#dialogWait").dialog("open");
            },
            success: function (datos) {
                jQuery("#GR_VAL_FAC").clearGridData();
                var lstXml = datos.getElementsByTagName("ValidaFacturas")[0];
                var lstCom = lstXml.getElementsByTagName("datos");
                for (var i = 0; i < lstCom.length; i++) {
                    var obj = lstCom[i];
                    var strIdOld = "0";
                    var strFolioOld = "0";
                    if (obj.getAttribute("FAC_ID_OLD") != "0") {
                        strIdOld = "<div style='color: #FF0000'><b>" + obj.getAttribute("FAC_ID_OLD") + "</b></div>";
                        strFolioOld = "<div style='color: #FF0000'><b>" + obj.getAttribute("FAC_FOLIO_OLD") + "</b></div>";
                    }
                    var rowValFac = {
                        VF_CONTADOR: getMaxGridValidaFac("#GR_VAL_FAC"),
                        FAC_FECHA: obj.getAttribute("FAC_FECHA"),
                        FAC_FECHA_PAGO: obj.getAttribute("FAC_FECHA_PAGO"),
                        FAC_SERIE: obj.getAttribute("FAC_SERIE"),
                        FAC_FOLIO: obj.getAttribute("FAC_FOLIO"),
                        FAC_FOLIO_C: obj.getAttribute("FAC_FOLIO_C"),
                        FAC_RAZONSOCIAL: obj.getAttribute("FAC_RAZONSOCIAL"),
                        FAC_NOMPAGO: obj.getAttribute("FAC_NOMPAGO"),
                        FAC_IMPORTE: obj.getAttribute("FAC_IMPORTE"),
                        FAC_IMPUESTO1: obj.getAttribute("FAC_IMPUESTO1"),
                        FAC_TOTAL: obj.getAttribute("FAC_TOTAL"),
                        FAC_COFIDE_VALIDA: obj.getAttribute("FAC_COFIDE_VALIDA"),
                        FAC_COFIDE_PAGADO: obj.getAttribute("FAC_COFIDE_PAGADO"),
                        FAC_TIPO: obj.getAttribute("FAC_TIPO"),
                        FAC_EJECUTIVO: obj.getAttribute("AGENTE"),
                        FAC_FORMADEPAGO: obj.getAttribute("FAC_FORMADEPAGO"),
                        FAC_METODODEPAGO: obj.getAttribute("FAC_METODODEPAGO"),
                        FAC_REFERENCIA: obj.getAttribute("FAC_REFERENCIA"),
                        FAC_ID_OLD: strIdOld,
                        FAC_FOLIO_OLD: strFolioOld,
                        FAC_ID: obj.getAttribute("FAC_ID")
                    };
                    jQuery("#GR_VAL_FAC").addRowData(getMaxGridValidaFac("#GR_VAL_FAC"), rowValFac, "last");
                }//Fin FOR
                sumaRepoValidaFacturas();
                getFacCancel();//TRAE LAS FACTURAS A CANCELAR
                $("#dialogWait").dialog("close");
            },
            error: function (objeto, quepaso, otroobj) {
                alert(":pto9:" + objeto + " " + quepaso + " " + otroobj);
                $("#dialogWait").dialog("close");
            }
        });

    } else {
        alert("Selecciona Tipo de Curso");
        document.getElementById("VIEW_TIPO").focus();
    }

}//Fin getFacturasValidar


function getMaxGridValidaFac(strNomGr) {
    var intLenght = jQuery(strNomGr).getDataIDs().length + 1;
    return intLenght;
}//Fin getMaxGridCursosMaterial

function printArchivoPago(opc) {
    if (opc == 1) {
        var grid = jQuery("#GR_VAL_FAC");
    } else {
        var grid = jQuery("#GR_FAC_CANCEL");
    }
    if (grid.getGridParam("selrow")) {

        var id = grid.getGridParam("selrow");
        var lstVal = grid.getRowData(id);
        var strPost = "FAC_ID=" + lstVal.FAC_ID;

        $.ajax({
            type: "POST",
            data: strPost,
            scriptCharset: "UTF-8",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "html",
            url: "COFIDE_ValidaFactura.jsp?id=3",
            beforeSend: function () {
                $("#dialogWait").dialog("open");
            },
            success: function (datos) {
                if (datos.substring(0, 2) == "OK") {
                    var strPathDocumento = datos.replace("OK.", "");
                    Abrir_Link(strPathDocumento, "_new", 800, 600, 0, 0);
                    $("#dialogWait").dialog("close");
                } else {
                    $("#dialogWait").dialog("close");
                    alert(datos);
                }
                $("#dialogWait").dialog("close");
            }
        }); //fin del ajax
    } else {
        alert("Selecciona una venta.")
    }
}//Fin printArchivoPago

function AutorizarPago() {
    var grid = jQuery("#GR_VAL_FAC");
    if (grid.getGridParam("selrow")) {

        var id = grid.getGridParam("selrow");
        var lstVal = grid.getRowData(id);
        if (lstVal.FAC_COFIDE_PAGADO == "SI") {
            var strPost = "FAC_ID=" + lstVal.FAC_ID;
            strPost += "&FAC_TIPO=" + lstVal.FAC_TIPO;
            strPost += "&strOpc=1";
            $.ajax({
                type: "POST",
                data: strPost,
                scriptCharset: "UTF-8",
                contentType: "application/x-www-form-urlencoded;charset=utf-8",
                cache: false,
                dataType: "html",
                url: "COFIDE_ValidaFactura.jsp?id=2",
                beforeSend: function () {
                    $("#dialogWait").dialog("open");
                },
                success: function (datos) {
                    if (datos.substring(0, 2) == "OK") {
                        getFacturasValidar();
                        $("#dialogWait").dialog("close");
                    } else {
                        $("#dialogWait").dialog("close");
                        alert(datos);
                    }
                    $("#dialogWait").dialog("close");
                }
            }); //fin del ajax
        } else {
            $("#dialogWait").dialog("close");
            alert("El documento debe estar pagado para autorizarlo.");
        }
    } else {
        alert("Selecciona una Factura.")
    }
}//Fin AutorizarPago

function opnDenegarPago() {
    var grid = jQuery("#GR_VAL_FAC");
    if (grid.getGridParam("selrow")) {
        OpnOpt('DOC_DENIED', '_ed', 'dialogCte', false, false);
    } else {
        alert("Seleccione una Factura");
    }
}//Fin opnDenegarPago

function DenegarPagoDo() {
    var grid = jQuery("#GR_VAL_FAC");
    if (grid.getGridParam("selrow")) {

        var id = grid.getGridParam("selrow");
        var lstVal = grid.getRowData(id);
        var strPost = "FAC_ID=" + lstVal.FAC_ID;
        strPost += "&FAC_TIPO=" + lstVal.FAC_TIPO;
        strPost += "&strOpc=2";
        strPost += "&strMotivoDen=" + document.getElementById("SEL_OPCIOND").value;
        $.ajax({
            type: "POST",
            data: strPost,
            scriptCharset: "UTF-8",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "html",
            url: "COFIDE_ValidaFactura.jsp?id=2",
            beforeSend: function () {
                $("#dialogWait").dialog("open");
            },
            success: function (datos) {
                if (datos.substring(0, 2) == "OK") {
                    getFacturasValidar();
                    $("#dialogWait").dialog("close");
//                    closeDatePicher();
                    closeDeniegaPago();
                } else {
                    getFacturasValidar();
                    $("#dialogWait").dialog("close");
//                    closeDatePicher();
                    closeDeniegaPago();
                    alert(datos);
                }
                $("#dialogWait").dialog("close");
            }
        }); //fin del ajax
    } else {
        alert("Selecciona una Factura.")
    }
}//Fin DenegarPago

function EditFechaPago() {
    var grid = jQuery("#GR_VAL_FAC");
    if (grid.getGridParam("selrow")) {

        var id = grid.getGridParam("selrow");
        var lstVal = grid.getRowData(id);
        var strPost = "FAC_ID=" + lstVal.FAC_ID;
        strPost += "&FechaPago=" + document.getElementById("departing").value;
        strPost += "&strOpc=3";
        strPost += "&FAC_TIPO=" + lstVal.FAC_TIPO;
        $.ajax({
            type: "POST",
            data: strPost,
            scriptCharset: "UTF-8",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "html",
            url: "COFIDE_ValidaFactura.jsp?id=2",
            beforeSend: function () {
                $("#dialogWait").dialog("open");
            },
            success: function (datos) {
                if (datos.substring(0, 2) == "OK") {
                    getFacturasValidar();
                    $("#dialogWait").dialog("close");
                    closeDatePicher();
                } else {
                    $("#dialogWait").dialog("close");
                    alert(datos);
                }
                $("#dialogWait").dialog("close");
            }
        }); //fin del ajax
    } else {
        alert("Selecciona una Factura.")
    }
}//Fin EditFechaPago

function opnSetFechaPago() {
    var grid = jQuery("#GR_VAL_FAC");
    if (grid.getGridParam("selrow")) {

        var strHTML = "<input type=\"text\" id=\"departing\"><br><br>";
        strHTML += "<button type=\"button\" onclick=\"EditFechaPago()\">Aceptar</button>";
        strHTML += "<button type=\"button\" onclick=\"closeDatePicher()\">Cerrar</button>";
        document.getElementById("dialog_inside").innerHTML = strHTML;
        $("#dialog").dialog("open");
        setTimeout(setDatePicker(), 1000);
    } else {
        alert("Seleccione una Factura");
    }
}//Fin opnSetFechaPago

function setDatePicker() {
    $("#departing").datepicker();
}//Fin setDatePicker

function closeDatePicher() {
    $("#dialog").dialog("close");
}//Fin closeDatePicher

function closeDeniegaPago() {
    $("#dialogCte").dialog("close");
}//Fin closeDeniegaPago


/*Realiza la suma de el reporte de validad Facturas*/
function sumaRepoValidaFacturas() {
    var grid = jQuery("#GR_VAL_FAC");
    var dblTotalImporte = 0;
    var dblTotalIva = 0;
    var dblTotal = 0;
    var arr = grid.getDataIDs();
    if (arr != null) {
        for (var i = 0; i < arr.length; i++) {
            var id = arr[i];
            var lstVal = grid.getRowData(id);
            dblTotalImporte = dblTotalImporte + parseFloat(lstVal.FAC_IMPORTE);
            dblTotalIva = dblTotalIva + parseFloat(lstVal.FAC_IMPUESTO1);
            dblTotal = dblTotal + parseFloat(lstVal.FAC_TOTAL);
        }
    }
    /*Ponemos el total en el pie de las columnas*/
    grid.footerData('set', {FAC_NOMPAGO: "TOTAL", FAC_IMPORTE: dblTotalImporte, FAC_IMPUESTO1: dblTotalIva, FAC_TOTAL: dblTotal});
}//Fin sumaRepoValidaFacturas

//muestra las facturas por cancelar y canceladas
function getFacCancel() {
    var intCancel = 0;
    var strMes = document.getElementById("CF_MES").value;
    var strAnio = document.getElementById("CF_AÑO").value;
    var intTipoDoc = document.getElementById("VIEW_TIPO2").value;
    var intCtId = document.getElementById("VIEW_CLIENTE2").value;
    var strFolio = document.getElementById("VIEW_FOLIO2").value;
    var intIdVenta = document.getElementById("VIEW_ID2").value;
    var strPost = "";
    if (document.getElementById("CF_CANCEL1").checked) {
        intCancel = 1; //cancelados
    }
    strPost = "estatus=" + intCancel;
    strPost += "&mes=" + strMes;
    strPost += "&anio=" + strAnio;
    strPost += "&intTipoDoc=" + intTipoDoc;
    strPost += "&intCtId=" + intCtId;
    strPost += "&strFolio=" + strFolio;
    strPost += "&intIdVenta=" + intIdVenta;

    $.ajax({
        type: "POST",
        data: strPost,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "COFIDE_ValFac_Duplicados.jsp?id=9",
        beforeSend: function () {
            $("#dialogWait").dialog("open");
        },
        success: function (datos) {
            jQuery("#GR_FAC_CANCEL").clearGridData();
            var lstXml = datos.getElementsByTagName("Cancelar")[0];
            var lstCom = lstXml.getElementsByTagName("datos");
            for (var i = 0; i < lstCom.length; i++) {
                var obj = lstCom[i];
                var rowValFac = {
                    FAC_ID: obj.getAttribute("FAC_ID"),
                    FAC_FECHA: obj.getAttribute("FAC_FECHA"),
                    FAC_HORA: obj.getAttribute("FAC_HORA"),
                    FAC_SERIE: obj.getAttribute("FAC_SERIE"),
                    FAC_FOLIO: obj.getAttribute("FAC_FOLIO"),
                    FAC_FOLIO_C: obj.getAttribute("FAC_FOLIO_C"),
                    FAC_IMPORTE: obj.getAttribute("FAC_IMPORTE"),
                    FAC_IMPORTE_IVA: obj.getAttribute("FAC_IVA"),
                    FAC_TOTAL: obj.getAttribute("FAC_TOTAL"),
                    FAC_RAZONSOCIAL: obj.getAttribute("FAC_RAZONSOCIAL"),
                    FAC_USER_ANUL: obj.getAttribute("FAC_USUARIO"),
                    FAC_EJECUTIVO: obj.getAttribute("FAC_EJECUTIVO"),
                    FAC_ESTATUS: obj.getAttribute("FAC_ESTATUS"),
                    FAC_TIPO: obj.getAttribute("FAC_TIPO"),
                    FAC_NOMPAGO: obj.getAttribute("FAC_NOMPAGO")
                };
                itemIdCont++;
                jQuery("#GR_FAC_CANCEL").addRowData(itemIdCont, rowValFac, "last");
            }//Fin FOR
            $("#dialogWait").dialog("close");
        }, error: function (objeto, quepaso, otroobj) {
            alert(":pto=9: " + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }});
}

//cancela las facturas
function CancelaFac() {
    var strTipoDoc = "";
    var strIdVta = "";
    document.getElementById("SioNO_inside").innerHTML = "¿Estas seguro de anular este documento?";
    $("#SioNO").dialog("open");
    document.getElementById("btnSI").onclick = function () {
        $("#SioNO").dialog("close");
        var grid = jQuery("#GR_FAC_CANCEL");
        if (grid.getGridParam("selrow")) {

            var id = grid.getGridParam("selrow");
            var lstVal = grid.getRowData(id);
            strIdVta = lstVal.FAC_ID;
            strTipoDoc = lstVal.FAC_TIPO;
            //anula a los participantes de la venta y les resta puntos obtenidos en esa venta
//            QuitaParticipantePts(strIdVta, strTipoDoc);
            var strPost = "idAnul=" + lstVal.FAC_ID;
            if (lstVal.FAC_ESTATUS != "ANULADA") {
                if (strTipoDoc == "F") { //cancela factura
                    $.ajax({type: "POST",
                        data: strPost,
                        scriptCharset: "utf-8",
                        contentType: "application/x-www-form-urlencoded;charset=utf-8",
                        cache: false,
                        dataType: "html",
                        url: "VtasMov.do?id=4",
                        beforeSend: function () {
                            $("#dialogWait").dialog("open");
                        },
                        success: function (dato) {
                            dato = trim(dato);
                            if (dato != "OK") {
                                alert(dato);
                            } else {
                                alert("Venta ID: " + lstVal.FAC_ID + " ha sido Cancelada.");
                                MailCancel(lstVal.FAC_ID); //notifica al ejecutivo que ha sido cancelada su factura
                                getFacCancel();
                            }
                            $("#dialogWait").dialog("close");
                        }, error: function (objeto, quepaso, otroobj) {
                            alert(":pto=4: " + objeto + " " + quepaso + " " + otroobj);
                            $("#dialogWait").dialog("close");
                        }});
                } else { // cancela ticket o reservación
                    $.ajax({type: "POST",
                        data: strPost,
                        scriptCharset: "utf-8",
                        contentType: "application/x-www-form-urlencoded;charset=utf-8",
                        cache: false,
                        dataType: "html",
                        url: "VtasMov.do?id=2",
                        beforeSend: function (xhr) {
                            $("#dialogWait").dialog("open");
                        },
                        success: function (dato) {
                            dato = trim(dato);
                            if (dato != "OK") {
                                alert(dato);
                            } else {
                                alert("Venta ID: " + lstVal.FAC_ID + " ha sido Cancelada.");
                                MailCancel(lstVal.FAC_ID); //notifica al ejecutivo que ha sido cancelada su factura
                                getFacCancel();
                            }
                            $("#dialogWait").dialog("close");
                        }, error: function (objeto, quepaso, otroobj) {
                            alert(":pto2:" + objeto + " " + quepaso + " " + otroobj);
                            $("#dialogWait").dialog("close");
                        }});
                }
            } else {
                alert("Esta venta ya ha sido Cancelada.");
                $("#dialogWait").dialog("close");
            }
        } else {
            alert("Selecciona una Venta.")
        }
    };
    document.getElementById("btnNO").onclick = function () {
        $("#SioNO").dialog("close");
    };
}

function MailCancel(strIdFac) {
    var strPost = "id_fac=" + strIdFac;
    $.ajax({
        type: "POST",
        data: strPost,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "html",
        url: "COFIDE_ValFac_Duplicados.jsp?id=10",
        success: function (dato) {
            dato = trim(dato);
            if (dato != "OK") {
                alert("ERROR: " + dato);
            } else {
                alert("Se notificara al ejecutivo la cancelación exitosa.");
            }
            $("#dialogWait").dialog("close");
        }, error: function (objeto, quepaso, otroobj) {
            alert(":pto=10: " + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }});
}


//Imprime la Factura o ticket de la venta
function PrintDocument(opc) {
    if (opc == 1) {
        var grid = jQuery("#GR_VAL_FAC");
    } else {
        var grid = jQuery("#GR_FAC_CANCEL");
    }
    if (grid.getGridParam("selrow") != null) {
        var lstRow = grid.getRowData(grid.getGridParam("selrow"));
        if (lstRow.FAC_TIPO == "F") {
//            var strHtml = CreaHidden("FAC_ID", lstRow.FAC_ID);
//            openWhereverFormat("ERP_SendInvoice?id=" + lstRow.FAC_ID, "FACTURA", "PDF", strHtml);
            downloadDoc("FAC_ID", lstRow.FAC_ID);
        } else {
//            var strHtml2 = CreaHidden("TKT_ID", lstRow.FAC_ID);
//            openFormat("TICKET", "PDF", strHtml2);
            downloadDoc("TKT_ID", lstRow.FAC_ID);
        }

    }
}//Fin PrintDocument

function initDenegarFactura() {
    document.getElementById("btn1").parentNode.parentNode.style.display = 'none';
    document.getElementById("btn2").parentNode.parentNode.style.display = 'none';
}


function setDatosTimbrado() {

    var grid = jQuery("#GR_VAL_FAC");

    if (grid.getGridParam("selrow")) {
        document.getElementById("SioNO_inside").innerHTML = "¿CONFIRMAR TIMBRADO DE LA FACTURA?";
        $("#SioNO").dialog("open");
        document.getElementById("btnSI").onclick = function () {
            $("#SioNO").dialog("close");

            var id = grid.getGridParam("selrow");
            var lstVal = grid.getRowData(id);
            var strPost = "FAC_ID=" + lstVal.FAC_ID;
            $.ajax({
                type: "POST",
                data: strPost,
                scriptCharset: "UTF-8",
                contentType: "application/x-www-form-urlencoded;charset=utf-8",
                cache: false,
                dataType: "html",
                url: "COFIDE_ValidaFactura.jsp?id=4",
                beforeSend: function () {
                    $("#dialogWait").dialog("open");
                },
                success: function (datos) {
                    if (datos.substring(0, 2) == "OK") {
                        $("#dialogWait").dialog("close");
                        getFacturasValidar();
                        datos = trim(datos);
                        SendFacturaXML(datos.replace("OK.", ""));
                    } else {
                        $("#dialogWait").dialog("close");
                        alert(datos);
                    }
                    $("#dialogWait").dialog("close");
                }
            }); //fin del ajax
        };
        document.getElementById("btnNO").onclick = function () {
            $("#SioNO").dialog("close");
        };
    } else {
        alert("Selecciona una venta.")
    }

}//Fin setDatosTimbrado

/**
 * edita el metodo de pago
 * edita forma de pago
 * edita referencia de pago
 * @returns {undefined}
 */
function editMetFormDigito() {

    var grid = jQuery("#GR_VAL_FAC");

    if (grid.getGridParam("selrow")) {

        var id = grid.getGridParam("selrow");
        var lstVal = grid.getRowData(id);
        var strForma = document.getElementById("FAC_FORMA").value;
        var strMetodo = document.getElementById("FAC_METODO").value;
        var strDigito = document.getElementById("FAC_DIGITO").value;
        var strPost = "FAC_ID=" + lstVal.FAC_ID;
        strPost += "&forma=" + strForma;
        strPost += "&metodo=" + strMetodo;
        strPost += "&digito=" + strDigito;
        $.ajax({
            type: "POST",
            data: strPost,
            scriptCharset: "UTF-8",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "html",
            url: "COFIDE_ValidaFactura.jsp?id=5",
            beforeSend: function () {
                $("#dialogWait").dialog("open");
            },
            success: function (datos) {
                datos = trim(datos);
                if (datos == "OK") {
                    $("#dialogWait").dialog("close");
                    getFacturasValidar();
                    closeEditFac();
                } else {
                    $("#dialogWait").dialog("close");
                    alert(datos);
                }
                $("#dialogWait").dialog("close");
            }, error: function (objeto, quepaso, otroobj) {
                alert(":pto=5: " + objeto + " " + quepaso + " " + otroobj);
                $("#dialogWait").dialog("close");
            }
        }); //fin del ajax
    } else {
        alert("Selecciona una venta.")
    }
}

function opnEditFac() {

    var grid = jQuery("#GR_VAL_FAC");

    if (grid.getGridParam("selrow")) {

        var strHtml = "    <div style=\"width:450px; \">";
        strHtml += "        <div>";
        strHtml += "            <label>DIGITOS DE REFERENCIA</label>";
        strHtml += "            <BR />";
        strHtml += "            <input id=\"FAC_DIGITO\" type=\"text\" placeholder=\"DIGITO DE REFERENCIA\" size=\"50\" />";
        strHtml += "        </div>";
        strHtml += "        <div>";
        strHtml += "            <div>";
        strHtml += "                <label>METODO DE PAGO</label>";
        strHtml += "                <BR />";
        strHtml += "                <select id=\"FAC_METODO\" onchange=\"changeMetodoForma()\" >";
        strHtml += "                    <option value=\"0\">SELECCIONAR...</option>";
        strHtml += "                </select>";
        strHtml += "            </div>";
        strHtml += "            <div>";
        strHtml += "                <div>";
        strHtml += "                    <label>FORMA DE PAGO</label>";
        strHtml += "                    <BR />";
        strHtml += "                    <select id=\"FAC_FORMA\" >";
        strHtml += "                        <option value=\"0\">SELECCIONAR...</option>";
        strHtml += "                    </select>";
        strHtml += "                </div>";
        strHtml += "                <hr />";
        strHtml += "                <div style=\"text-align:center\">";
        strHtml += "                    <input type=\"button\" onclick=\"editMetFormDigito()\" value=\"GUARDAR CAMBIO\" />";
        strHtml += "                    <input type=\"button\" onclick=\"closeEditFac()\" value=\"SALIR SIN GUARDAR\" />";
        strHtml += "                </div>";
        strHtml += "            </div>";
        strHtml += "        </div>";
        strHtml += "    </div>";

        document.getElementById("dialog2_inside").innerHTML = strHtml;
        $("#dialog2").dialog("option", "title", "EDITAR INFORMAICÓN DE FACTURACIÓN.");
        $("#dialog2").dialog("open");

        //llena campos para su edicion
        getMetodo();
    } else {
        alert("Selecciona una venta!.");
    }
}

/**
 * obtener metodos de pago dle castalgoo
 */
function getMetodo() {
    $.ajax({
        type: "POST",
        data: "",
        scriptCharset: "UTF-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "COFIDE_ValidaFactura.jsp?id=7",
        success: function (datos) {
            var objHoraCombo = document.getElementById("FAC_METODO");
            select_clear(objHoraCombo);
            var lstXml = datos.getElementsByTagName("vta")[0];
            var lstCte = lstXml.getElementsByTagName("datos");
            select_add(objHoraCombo, "SELECCIONE...", "");
            for (var i = 0; i < lstCte.length; i++) {
                var objHora = lstCte[i];
                select_add(objHoraCombo, objHora.getAttribute("DESCRIPCION"), objHora.getAttribute("CLAVE"));
            }
            getForma();
        }, error: function (objeto, quepaso, otroobj) {
            alert(":pto=6: " + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }});
}

/**
 * obtener formas de pago del catalogo
 */
function getForma() {
    $.ajax({
        type: "POST",
        data: "",
        scriptCharset: "UTF-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "COFIDE_ValidaFactura.jsp?id=8",
        success: function (datos) {
            var objHoraCombo = document.getElementById("FAC_FORMA");
            select_clear(objHoraCombo);
            var lstXml = datos.getElementsByTagName("vta")[0];
            var lstCte = lstXml.getElementsByTagName("datos");
            select_add(objHoraCombo, "SELECCIONE...", "");
            for (var i = 0; i < lstCte.length; i++) {
                var objHora = lstCte[i];
                select_add(objHoraCombo, objHora.getAttribute("DESCRIPCION"), objHora.getAttribute("CLAVE"));
            }
            getInfVta();
        }, error: function (objeto, quepaso, otroobj) {
            alert(":pto=6: " + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }});
}

/**
 * obtiene la informacion de la venta seleccionada
 */
function getInfVta() {
    var grid = jQuery("#GR_VAL_FAC");
    var id = grid.getGridParam("selrow");
    var lstVal = grid.getRowData(id);
    var strPost = "FAC_ID=" + lstVal.FAC_ID;

    $.ajax({
        type: "POST",
        data: strPost,
        scriptCharset: "UTF-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "COFIDE_ValidaFactura.jsp?id=6",
        beforeSend: function () {
            $("#dialogWait").dialog("open");
        },
        success: function (datos) {
            var lstXml = datos.getElementsByTagName("vta")[0];
            var lstCte = lstXml.getElementsByTagName("datos");
            for (var i = 0; i < lstCte.length; i++) {
                var objHora = lstCte[i];
                document.getElementById("FAC_DIGITO").value = objHora.getAttribute("DIGITO");
                document.getElementById("FAC_METODO").value = objHora.getAttribute("METODO");
                document.getElementById("FAC_FORMA").value = objHora.getAttribute("FORMA");
                document.getElementById("FAC_METODO").disabled = true;
            }
            $("#dialogWait").dialog("close");
        }, error: function (objeto, quepaso, otroobj) {
            alert(":pto=6: " + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }
    }); //fin del ajax

}

/**
 * cerrar ventana
 */
function closeEditFac() {
    $("#dialog2").dialog("close");
}

/**
 * al ser PPD, poner forma de pago, por definir
 * @returns {undefined}
 */
function changeMetodoForma() {
    var strMetodo = document.getElementById("FAC_METODO").value;
    if (strMetodo == "PPD") {
        document.getElementById("FAC_FORMA").value = '99';
        document.getElementById("FAC_FORMA").disabled = true;
        document.getElementById("FAC_DIGITO").disabled = true;
    } else {
        document.getElementById("FAC_FORMA").disabled = false;
        document.getElementById("FAC_DIGITO").disabled = false;
    }
}

/**
 * abrir Pop Up para los complementos
 * @returns {undefined}
 */

function opnComplemento() {
    var grid = jQuery("#GR_VAL_FAC");

    if (grid.getGridParam("selrow")) {
        var objSecModiVta = objMap.getScreen("COMPLEMENTO");
        if (objSecModiVta != null) {
            objSecModiVta.bolActivo = false;
            objSecModiVta.bolMain = false;
            objSecModiVta.bolInit = false;
            objSecModiVta.idOperAct = 0;
        }
        OpnOpt("COMPLEMENTO", "_ed", "dialogTMK1", false, false, true);
    } else {
        alert("Selecciona una factura para agregar su complemento de pago.");
    }
}

/**
 * descarga de pdf ticket / factura
 */
function downloadDoc(strTipoDoc, strFacId) {

    var strHtml = '<form action="COFIDE_PDF_Download.jsp" method="post" target="_blank" id="formSend">';
    strHtml += CreaHidden(strTipoDoc, strFacId);
    strHtml += "</form>";
    document.getElementById("formHidden").innerHTML = strHtml;
    document.getElementById("formSend").submit();

}