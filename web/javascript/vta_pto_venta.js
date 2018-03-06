var lastselPtoVta = 0;
var itemId = 0;
var dblTasaPto1 = dblTasa1;
var dblTasaPto2 = dblTasa2;
var dblTasaPto3 = dblTasa3;
var intIdTasaPto1 = intIdTasa1;
var intIdTasaPto2 = intIdTasa2;
var intIdTasaPto3 = intIdTasa3;
var intSImpPto1_2 = intSImp1_2;
var intSImpPto1_3 = intSImp1_3;
var intSImpPto2_3 = intSImp2_3;
var _selPaquete = null;
function vta_pto_venta() {}
function initAperCaja() {
    document.getElementById("btn2").parentNode.parentNode.style.display = "none";
    showApertura();
    $("#dialog").on("dialogclose", function (event, ui) {
        _validaCierreCaja();
    });
}
function initPtoVenta() {
    $("#dialogWait").dialog("open");
    myLayout.close("west");
    myLayout.close("east");
    myLayout.close("south");
    myLayout.close("north");
    creaBotonPaquetePtoVta();
    llenaVtasMostrador();
    setFolioPtoVta();
    TipoCambioPtoVta();
    TasaCambioPago();
    var strHtmlPTO = "<table>" + "<tr>" + '<td ><i class = "fa fa-minus-square" style="font-size:40px" onclick="del_Producto();"></i></td>' + "<td>&nbsp;&nbsp;&nbsp;</td>" + '<td ><i class = "fa fa-search" style="font-size:40px" onclick="opnPROD();"></i></td>' + "<td>&nbsp;&nbsp;&nbsp;</td>" + "</tr>" + "</table>";
    document.getElementById("PTO_TOOLBAR").innerHTML = strHtmlPTO;
    var strHtmlPay = "<table>" + "<tr>" + '<td id="btn_money" style="visibility:hidden"><i class = "fa fa-money" style="font-size:40px" onclick="SaveVtaPto();"></i></td>' + '<td id="btn_money"><i class = "fa fa-money" style="font-size:40px" onclick="SaveVtaPto();"></i></td>' + "</tr>";
    "</table>";
    document.getElementById("TOOLBAR_PAY").innerHTML = strHtmlPay;
    var strHtmlOut = "<table>" + "<tr>" + '<td id="btn_exit" style="visibility:hidden"><i class = "fa fa-sign-out" style="font-size:40px" onclick="CallExitPtoVenta();"></i></td>' + '<td id="btn_exit"><i class = "fa fa-sign-out" style="font-size:40px" onclick="CallExitPtoVenta();"></i></td>' + "</tr>";
    "</table>";
    document.getElementById("TOOLBAR_OUT").innerHTML = strHtmlOut;
    $("#dialogWait").dialog("close");
    existSesion();
}
function opnApertura() {
    OpnOpt("APER_CAJA", "_ed", "dialog", false, false);
}
function sumCaja() {
    var itemIdCob = 0;
    var strTotal = "TOTAL:";
    var sumPesos = 0;
    var sumDolares = 0;
    var grid = jQuery("#GR_SALDOS_INI");
    var idArr = grid.getDataIDs();
    if (idArr.length == 0) {
    } else {
        var dblValor = 0;
        for (var i = 0; i < idArr.length; i++) {
            var id = idArr[i];
            var lstRow = grid.getRowData(id);
            dblValor = parseFloat(lstRow.SI_VALOR);
            if (lstRow.SI_PESOS != "") {
                sumPesos = sumPesos + (parseFloat(lstRow.SI_PESOS) * dblValor);
            } else {
                sumPesos = sumPesos + 0;
            }
            if (lstRow.SI_DOLARES != "") {
                sumDolares = sumDolares + (parseFloat(lstRow.SI_DOLARES) * dblValor);
            } else {
                sumDolares = sumDolares + 0;
            }
        }
        jQuery("#GR_SALDOS_INI").footerData("set", {SI_VALOR: strTotal, SI_PESOS: sumPesos, SI_DOLARES: sumDolares, EDITA_PESO: 1, EDITA_DOLAR: 1});
    }
}
function validaMontos() {
    var strMsg = "";
    var dblPesos = 0;
    var dblUSD = 0;
    var grid = jQuery("#GR_SALDOS_INI");
    var idArr = grid.getDataIDs();
    if (idArr.length == 0) {
    } else {
        for (var i = 0; i < idArr.length; i++) {
            var id = idArr[i];
            var lstRow = grid.getRowData(id);
            dblPesos = dblPesos + parseFloat(lstRow.SI_PESOS);
            dblUSD = dblUSD + parseFloat(lstRow.SI_DOLARES);
            if (lstRow.EDITA_PESO == 0 && lstRow.SI_PESOS > 0) {
                strMsg += "El monto : " + lstRow.SI_VALOR + " No se puede editar en la moneda Pesos.\n";
            }
            if (lstRow.EDITA_DOLAR == 0 && lstRow.SI_DOLARES > 0) {
                strMsg += "El monto : " + lstRow.SI_VALOR + " No se puede editar en la moneda Dolares.\n";
            }
        }
        if (dblPesos == 0 || dblUSD == 0) {
            strMsg = "Agrega saldos iniciales";
        }
    }
    return strMsg;
}
function editGridPtoVta(e) {
    var strNomMain = objMap.getNomMain();
    if (strNomMain == "PTO_VENTA") {
        if (e.originalEvent.keyCode == 13) {
            var grid = jQuery("#GR_SALDOS_INI");
            grid.saveRow(lastselPtoVta);
            sumCaja();
            lastselPtoVta = 0;
        }
    }
}
function closeApertura() {
    var strRes = validaMontos();
    if (strRes == "") {
        var strPost = "";
        strPost += "&APC_FECHA=" + document.getElementById("APC_FECHA").value;
        var grid = jQuery("#GR_SALDOS_INI");
        var idArr = grid.getDataIDs();
        if (idArr.length == 0) {
        } else {
            strPost += "&LENGTH=" + (idArr.length - 1);
            for (var i = 0; i < idArr.length - 1; i++) {
                var id = idArr[i];
                var lstRow = grid.getRowData(id);
                strPost += "&SI_VALOR_" + i + "=" + lstRow.SI_VALOR + "";
                if (lstRow.SI_PESOS != "") {
                    strPost += "&SI_VALPESOS_" + i + "=" + lstRow.SI_PESOS + "";
                } else {
                    strPost += "&SI_VALPESOS_" + i + "=0";
                }
                if (lstRow.SI_DOLARES != "") {
                    strPost += "&SI_VALUSD_" + i + "=" + lstRow.SI_DOLARES + "";
                } else {
                    strPost += "&SI_VALUSD_" + i + "=0";
                }
                strPost += "&EDITA_PESOS_" + i + "=" + lstRow.EDITA_PESO + "";
                strPost += "&EDITA_DOLARES_" + i + "=" + lstRow.EDITA_DOLAR + "";
                if (idArr.length == i + 1) {
                    strPost += "&TOTAL_PESOS=" + lstRow.SI_PESOS + "";
                    strPost += "&TOTAL_DOLARES=" + lstRow.SI_DOLARES + "";
                }
            }
        }
        $.ajax({type: "POST", data: strPost, scriptCharset: "utf-8", contentType: "application/x-www-form-urlencoded;charset=utf-8", cache: false, dataType: "html", url: "ERP_Pto_Venta.jsp?id=6", success: function (datos) {
                if (datos.substring(0, 2) == "OK") {
                    $("#dialogWait").dialog("close");
                } else {
                    alert(datos);
                    $("#dialogWait").dialog("close");
                }
            }, error: function (objeto, quepaso, otroobj) {
                alert(":pto:" + objeto + " " + quepaso + " " + otroobj);
            }});
        $("#dialog").dialog("close");
    } else {
        alert(strRes);
    }
}
function callCMB(intCMB) {
    var intCmb = intCMB;
    _selPaquete = intCmb;
    var objMainFacPedi = objMap.getScreen("SELECT_PRODS");
    if (objMainFacPedi != null) {
        objMainFacPedi.bolActivo = false;
        objMainFacPedi.bolMain = false;
        objMainFacPedi.bolInit = false;
        objMainFacPedi.idOperAct = 0;
    }
    OpnOpt("SELECT_PRODS", "_ed", "dialog", false, false, true);
}
function selectPaquete() {
    var itemIdCob = 0;
    var strPost = "IdCmb=" + _selPaquete;
    $("#dialogWait").dialog("open");
    $.ajax({type: "POST", data: strPost, scriptCharset: "utf-8", contentType: "application/x-www-form-urlencoded;charset=utf-8", cache: false, dataType: "xml", url: "ERP_Combo.jsp?id=1", success: function (datos) {
            jQuery("#GR_PROD1").clearGridData();
            jQuery("#GR_PROD2").clearGridData();
            jQuery("#GR_PROD3").clearGridData();
            jQuery("#GR_PROD4").clearGridData();
            jQuery("#GR_PROD5").clearGridData();
            jQuery("#GR_PROD6").clearGridData();
            var objofrt = datos.getElementsByTagName("Combo_deta")[0];
            var lstProms = objofrt.getElementsByTagName("datos");
            for (var i = 0; i < lstProms.length; i++) {
                var obj = lstProms[i];
                if (obj.getAttribute("CMBD_PR_NUM") == 1) {
                    var dataRow1 = {CMBD_PR_ID: obj.getAttribute("CMBD_PR_ID"), CMBD_PR_CODIGO: obj.getAttribute("CMBD_PR_CODIGO"), CMBD_PR_DESCRIPCION: obj.getAttribute("CMBD_PR_DESCRIPCION"), CMBD_PR_CANTIDAD: obj.getAttribute("CMBD_PR_CANTIDAD"), CMBD_CONTADOR: obj.getAttribute("CMBD_CONTADOR"), CMBD_PR_NUM: obj.getAttribute("CMBD_PR_NUM")};
                    itemIdCob++;
                    jQuery("#GR_PROD1").addRowData(itemIdCob, dataRow1, "last");
                }
                if (obj.getAttribute("CMBD_PR_NUM") == "2") {
                    var dataRow2 = {CMBD_PR_ID: obj.getAttribute("CMBD_PR_ID"), CMBD_PR_CODIGO: obj.getAttribute("CMBD_PR_CODIGO"), CMBD_PR_DESCRIPCION: obj.getAttribute("CMBD_PR_DESCRIPCION"), CMBD_PR_CANTIDAD: obj.getAttribute("CMBD_PR_CANTIDAD"), CMBD_CONTADOR: obj.getAttribute("CMBD_CONTADOR"), CMBD_PR_NUM: obj.getAttribute("CMBD_PR_NUM")};
                    itemIdCob++;
                    jQuery("#GR_PROD2").addRowData(itemIdCob, dataRow2, "last");
                }
                if (obj.getAttribute("CMBD_PR_NUM") == 3) {
                    var dataRow3 = {CMBD_PR_ID: obj.getAttribute("CMBD_PR_ID"), CMBD_PR_CODIGO: obj.getAttribute("CMBD_PR_CODIGO"), CMBD_PR_DESCRIPCION: obj.getAttribute("CMBD_PR_DESCRIPCION"), CMBD_PR_CANTIDAD: obj.getAttribute("CMBD_PR_CANTIDAD"), CMBD_CONTADOR: obj.getAttribute("CMBD_CONTADOR"), CMBD_PR_NUM: obj.getAttribute("CMBD_PR_NUM")};
                    itemIdCob++;
                    jQuery("#GR_PROD3").addRowData(itemIdCob, dataRow3, "last");
                }
                if (obj.getAttribute("CMBD_PR_NUM") == 4) {
                    var dataRow4 = {CMBD_PR_ID: obj.getAttribute("CMBD_PR_ID"), CMBD_PR_CODIGO: obj.getAttribute("CMBD_PR_CODIGO"), CMBD_PR_DESCRIPCION: obj.getAttribute("CMBD_PR_DESCRIPCION"), CMBD_PR_CANTIDAD: obj.getAttribute("CMBD_PR_CANTIDAD"), CMBD_CONTADOR: obj.getAttribute("CMBD_CONTADOR"), CMBD_PR_NUM: obj.getAttribute("CMBD_PR_NUM")};
                    itemIdCob++;
                    jQuery("#GR_PROD4").addRowData(itemIdCob, dataRow4, "last");
                }
                if (obj.getAttribute("CMBD_PR_NUM") == 5) {
                    var dataRow5 = {CMBD_PR_ID: obj.getAttribute("CMBD_PR_ID"), CMBD_PR_CODIGO: obj.getAttribute("CMBD_PR_CODIGO"), CMBD_PR_DESCRIPCION: obj.getAttribute("CMBD_PR_DESCRIPCION"), CMBD_PR_CANTIDAD: obj.getAttribute("CMBD_PR_CANTIDAD"), CMBD_CONTADOR: obj.getAttribute("CMBD_CONTADOR"), CMBD_PR_NUM: obj.getAttribute("CMBD_PR_NUM")};
                    itemIdCob++;
                    jQuery("#GR_PROD5").addRowData(itemIdCob, dataRow5, "last");
                }
                if (obj.getAttribute("CMBD_PR_NUM") == 6) {
                    var dataRow6 = {CMBD_PR_ID: obj.getAttribute("CMBD_PR_ID"), CMBD_PR_CODIGO: obj.getAttribute("CMBD_PR_CODIGO"), CMBD_PR_DESCRIPCION: obj.getAttribute("CMBD_PR_DESCRIPCION"), CMBD_PR_CANTIDAD: obj.getAttribute("CMBD_PR_CANTIDAD"), CMBD_CONTADOR: obj.getAttribute("CMBD_CONTADOR"), CMBD_PR_NUM: obj.getAttribute("CMBD_PR_NUM")};
                    itemIdCob++;
                    jQuery("#GR_PROD6").addRowData(itemIdCob, dataRow6, "last");
                }
            }
            document.getElementById("CMB_CODIGO").value = obj.getAttribute("CMB_CODIGO");
            var grid1 = jQuery("#GR_PROD1");
            var idArr1 = grid1.getDataIDs();
            if (idArr1.length == 0) {
                $("#GR_PROD1").jqGrid("setGridState", "hidden");
            }
            var grid2 = jQuery("#GR_PROD2");
            var idArr2 = grid2.getDataIDs();
            if (idArr2.length == 0) {
                $("#GR_PROD2").jqGrid("setGridState", "hidden");
            }
            var grid3 = jQuery("#GR_PROD3");
            var idArr3 = grid3.getDataIDs();
            if (idArr3.length == 0) {
                $("#GR_PROD3").jqGrid("setGridState", "hidden");
            }
            var grid4 = jQuery("#GR_PROD4");
            var idArr4 = grid4.getDataIDs();
            if (idArr4.length == 0) {
                $("#GR_PROD4").jqGrid("setGridState", "hidden");
            }
            var grid5 = jQuery("#GR_PROD5");
            var idArr5 = grid5.getDataIDs();
            if (idArr5.length == 0) {
                $("#GR_PROD5").jqGrid("setGridState", "hidden");
            }
            var grid6 = jQuery("#GR_PROD6");
            var idArr6 = grid6.getDataIDs();
            if (idArr6.length == 0) {
                $("#GR_PROD6").jqGrid("setGridState", "hidden");
            }
            $("#dialogWait").dialog("close");
        }});
}
function agregaCombo(event, obj) {
    var strPR_COD = document.getElementById("PR_COD").value;
    var blCodigo = false;
    if (event.keyCode == 13) {
        var grid = jQuery("#GR_PROD1");
        var idArr = grid.getDataIDs();
        if (idArr.length != 0) {
            for (var i = 0; i < idArr.length; i++) {
                var id = idArr[i];
                var lstRow = grid.getRowData(id);
                if (strPR_COD == lstRow.CMBD_PR_CODIGO) {
                    blCodigo = true;
                    var intContadorGr = lstRow.CMBD_CONTADOR - 1;
                    var dataGr1 = {CMBD_PR_ID: lstRow.CMBD_PR_ID, CMBD_PR_CODIGO: lstRow.CMBD_PR_CODIGO, CMBD_PR_DESCRIPCION: lstRow.CMBD_PR_DESCRIPCION, CMBD_PR_CANTIDAD: lstRow.CMBD_PR_CANTIDAD, CMBD_CONTADOR: intContadorGr, CMBD_PR_NUM: lstRow.CMBD_PR_NUM};
                    grid.delRowData(id);
                    jQuery("#GR_PROD1").addRowData(i, dataGr1, "last");
                    if (intContadorGr <= 0) {
                        $("#GR_PROD1").jqGrid("setGridState", "hidden");
                        _endAddCombo(document.getElementById("CMB_CODIGO").value);
                    }
                }
            }
        }
        if (!blCodigo) {
            var grid2 = jQuery("#GR_PROD2");
            var idArr2 = grid2.getDataIDs();
            if (idArr2.length != 0) {
                for (var i = 0; i < idArr2.length; i++) {
                    var id = idArr2[i];
                    var lstRow = grid2.getRowData(id);
                    if (strPR_COD == lstRow.CMBD_PR_CODIGO) {
                        blCodigo = true;
                        var intContadorGr = lstRow.CMBD_CONTADOR - 1;
                        var dataGr2 = {CMBD_PR_ID: lstRow.CMBD_PR_ID, CMBD_PR_CODIGO: lstRow.CMBD_PR_CODIGO, CMBD_PR_DESCRIPCION: lstRow.CMBD_PR_DESCRIPCION, CMBD_PR_CANTIDAD: lstRow.CMBD_PR_CANTIDAD, CMBD_CONTADOR: intContadorGr, CMBD_PR_NUM: lstRow.CMBD_PR_NUM};
                        grid2.delRowData(id);
                        jQuery("#GR_PROD2").addRowData(i, dataGr2, "last");
                        if (intContadorGr <= 0) {
                            $("#GR_PROD2").jqGrid("setGridState", "hidden");
                            _endAddCombo(document.getElementById("CMB_CODIGO").value);
                        }
                    }
                }
            }
        }
        if (!blCodigo) {
            var grid3 = jQuery("#GR_PROD3");
            var idArr3 = grid3.getDataIDs();
            if (idArr3.length != 0) {
                for (var i = 0; i < idArr3.length; i++) {
                    var id = idArr3[i];
                    var lstRow = grid3.getRowData(id);
                    if (strPR_COD == lstRow.CMBD_PR_CODIGO) {
                        blCodigo = true;
                        var intContadorGr = lstRow.CMBD_CONTADOR - 1;
                        var dataGr3 = {CMBD_PR_ID: lstRow.CMBD_PR_ID, CMBD_PR_CODIGO: lstRow.CMBD_PR_CODIGO, CMBD_PR_DESCRIPCION: lstRow.CMBD_PR_DESCRIPCION, CMBD_PR_CANTIDAD: lstRow.CMBD_PR_CANTIDAD, CMBD_CONTADOR: intContadorGr, CMBD_PR_NUM: lstRow.CMBD_PR_NUM};
                        grid3.delRowData(id);
                        jQuery("#GR_PROD3").addRowData(i, dataGr3, "last");
                        if (intContadorGr <= 0) {
                            $("#GR_PROD3").jqGrid("setGridState", "hidden");
                            _endAddCombo(document.getElementById("CMB_CODIGO").value);
                        }
                    }
                }
            }
        }
        if (!blCodigo) {
            var grid4 = jQuery("#GR_PROD4");
            var idArr4 = grid4.getDataIDs();
            if (idArr4.length != 0) {
                for (var i = 0; i < idArr4.length; i++) {
                    var id = idArr4[i];
                    var lstRow = grid4.getRowData(id);
                    if (strPR_COD == lstRow.CMBD_PR_CODIGO) {
                        blCodigo = true;
                        var intContadorGr = lstRow.CMBD_CONTADOR - 1;
                        var dataGr4 = {CMBD_PR_ID: lstRow.CMBD_PR_ID, CMBD_PR_CODIGO: lstRow.CMBD_PR_CODIGO, CMBD_PR_DESCRIPCION: lstRow.CMBD_PR_DESCRIPCION, CMBD_PR_CANTIDAD: lstRow.CMBD_PR_CANTIDAD, CMBD_CONTADOR: intContadorGr, CMBD_PR_NUM: lstRow.CMBD_PR_NUM};
                        grid4.delRowData(id);
                        jQuery("#GR_PROD4").addRowData(i, dataGr4, "last");
                        if (intContadorGr <= 0) {
                            $("#GR_PROD4").jqGrid("setGridState", "hidden");
                            _endAddCombo(document.getElementById("CMB_CODIGO").value);
                        }
                    }
                }
            }
        }
        if (!blCodigo) {
            var grid5 = jQuery("#GR_PROD5");
            var idArr5 = grid5.getDataIDs();
            if (idArr5.length != 0) {
                for (var i = 0; i < idArr5.length; i++) {
                    var id = idArr5[i];
                    var lstRow = grid5.getRowData(id);
                    if (strPR_COD == lstRow.CMBD_PR_CODIGO) {
                        blCodigo = true;
                        var intContadorGr = lstRow.CMBD_CONTADOR - 1;
                        var dataGr5 = {CMBD_PR_ID: lstRow.CMBD_PR_ID, CMBD_PR_CODIGO: lstRow.CMBD_PR_CODIGO, CMBD_PR_DESCRIPCION: lstRow.CMBD_PR_DESCRIPCION, CMBD_PR_CANTIDAD: lstRow.CMBD_PR_CANTIDAD, CMBD_CONTADOR: intContadorGr, CMBD_PR_NUM: lstRow.CMBD_PR_NUM};
                        grid5.delRowData(id);
                        jQuery("#GR_PROD5").addRowData(i, dataGr5, "last");
                        if (intContadorGr <= 0) {
                            $("#GR_PROD5").jqGrid("setGridState", "hidden");
                            _endAddCombo(document.getElementById("CMB_CODIGO").value);
                        }
                    }
                }
            }
        }
        if (!blCodigo) {
            var grid6 = jQuery("#GR_PROD6");
            var idArr6 = grid6.getDataIDs();
            if (idArr6.length != 0) {
                for (var i = 0; i < idArr6.length; i++) {
                    var id = idArr6[i];
                    var lstRow = grid6.getRowData(id);
                    if (strPR_COD == lstRow.CMBD_PR_CODIGO) {
                        blCodigo = true;
                        var intContadorGr = lstRow.CMBD_CONTADOR - 1;
                        var dataGr6 = {CMBD_PR_ID: lstRow.CMBD_PR_ID, CMBD_PR_CODIGO: lstRow.CMBD_PR_CODIGO, CMBD_PR_DESCRIPCION: lstRow.CMBD_PR_DESCRIPCION, CMBD_PR_CANTIDAD: lstRow.CMBD_PR_CANTIDAD, CMBD_CONTADOR: intContadorGr, CMBD_PR_NUM: lstRow.CMBD_PR_NUM};
                        grid6.delRowData(id);
                        jQuery("#GR_PROD6").addRowData(i, dataGr6, "last");
                        if (intContadorGr <= 0) {
                            $("#GR_PROD6").jqGrid("setGridState", "hidden");
                            _endAddCombo(document.getElementById("CMB_CODIGO").value);
                        }
                    }
                }
            }
        }
    }
    document.getElementById("PR_COD").value = "";
}
function _endAddCombo(strCodigo) {
    var intCantProductos = 0;
    var grid1 = jQuery("#GR_PROD1");
    var idArr1 = grid1.getDataIDs();
    if (idArr1.length != 0) {
        for (var i = 0; i < idArr1.length; i++) {
            var id = idArr1[i];
            var lstRow = grid1.getRowData(id);
            intCantProductos = intCantProductos + lstRow.CMBD_CONTADOR;
        }
    }
    var grid2 = jQuery("#GR_PROD2");
    var idArr2 = grid2.getDataIDs();
    if (idArr2.length != 0) {
        for (var i = 0; i < idArr2.length; i++) {
            var id = idArr2[i];
            var lstRow = grid2.getRowData(id);
            intCantProductos = intCantProductos + lstRow.CMBD_CONTADOR;
        }
    }
    var grid3 = jQuery("#GR_PROD3");
    var idArr3 = grid3.getDataIDs();
    if (idArr3.length != 0) {
        for (var i = 0; i < idArr3.length; i++) {
            var id = idArr3[i];
            var lstRow = grid3.getRowData(id);
            intCantProductos = intCantProductos + lstRow.CMBD_CONTADOR;
        }
    }
    var grid4 = jQuery("#GR_PROD4");
    var idArr4 = grid4.getDataIDs();
    if (idArr4.length != 0) {
        for (var i = 0; i < idArr4.length; i++) {
            var id = idArr4[i];
            var lstRow = grid4.getRowData(id);
            intCantProductos = intCantProductos + lstRow.CMBD_CONTADOR;
        }
    }
    var grid5 = jQuery("#GR_PROD5");
    var idArr5 = grid5.getDataIDs();
    if (idArr5.length != 0) {
        for (var i = 0; i < idArr5.length; i++) {
            var id = idArr5[i];
            var lstRow = grid5.getRowData(id);
            intCantProductos = intCantProductos + lstRow.CMBD_CONTADOR;
        }
    }
    var grid6 = jQuery("#GR_PROD6");
    var idArr6 = grid6.getDataIDs();
    if (idArr6.length != 0) {
        for (var i = 0; i < idArr6.length; i++) {
            var id = idArr6[i];
            var lstRow = grid6.getRowData(id);
            intCantProductos = intCantProductos + lstRow.CMBD_CONTADOR;
        }
    }
    if (intCantProductos <= 0) {
        AddCombo(strCodigo);
        $("#dialog").dialog("close");
    }
}
function cancelaCombo() {
    document.getElementById("PTO_PR_CODIGO").value = "";
}
function showApertura() {
    var itemIdCob = 0;
    $("#dialogWait").dialog("open");
    var strPost = "";
    $.ajax({type: "POST", data: strPost, scriptCharset: "utf-8", contentType: "application/x-www-form-urlencoded;charset=utf-8", cache: false, dataType: "xml", url: "ERP_Pto_Venta.jsp?id=1", success: function (datos) {
            var objofrt = datos.getElementsByTagName("Datos_User")[0];
            var lstProms = objofrt.getElementsByTagName("datos");
            for (var i = 0; i < lstProms.length; i++) {
                var obj = lstProms[i];
                document.getElementById("APC_ID").value = obj.getAttribute("APC_ID");
                document.getElementById("SC_ID").value = obj.getAttribute("BODEGA");
                document.getElementById("NUM_CAJA").value = obj.getAttribute("SC_CAJA");
                document.getElementById("USER").value = obj.getAttribute("ID_USER");
                document.getElementById("NOM_USER").value = obj.getAttribute("NOMBRE_USER");
            }
            jQuery("#GR_SALDOS_INI").clearGridData();
            var objofrt = datos.getElementsByTagName("Datos_User")[0];
            var lstProms = objofrt.getElementsByTagName("aper_caja");
            for (var i = 0; i < lstProms.length; i++) {
                var obj = lstProms[i];
                var dataRow = {SI_VALOR: obj.getAttribute("SI_VALOR"), EDITA_PESO: obj.getAttribute("SI_PESOS"), EDITA_DOLAR: obj.getAttribute("SI_DOLARES")};
                itemIdCob++;
                jQuery("#GR_SALDOS_INI").addRowData(itemIdCob, dataRow, "last");
            }
            $("#dialogWait").dialog("close");
        }, error: function () {
            alert("Error Datos de Usuario!");
            $("#dialogWait").dialog("close");
        }});
}
function selRowCajaAper(rowid, status) {
    var strNomMain = objMap.getNomMain();
    if (strNomMain == "PTO_VENTA") {
        editFilaCaja(rowid);
        sumCaja();
    }
}
function editFilaCaja(id) {
    var grid = jQuery("#GR_SALDOS_INI");
    if (id != lastselPtoVta) {
        grid.saveRow(lastselPtoVta);
        grid.editRow(id, false);
        lastselPtoVta = id;
    }
}
function opnCobro() {
    var objMainFacPedi = objMap.getScreen("form_Pago");
    if (objMainFacPedi != null) {
        objMainFacPedi.bolActivo = false;
        objMainFacPedi.bolMain = false;
        objMainFacPedi.bolInit = false;
        objMainFacPedi.idOperAct = 0;
    }
    OpnOpt("form_Pago", "_ed", "dialog", false, false, true);
}
function closeCobro() {
    $("#dialog").dialog("close");
}
function closeCobroMtl() {
    $("#dialog2").dialog("close");
}
function llenaCobro() {
    $("#dialogWait").dialog("open");
    var strPost = "";
    $.ajax({type: "POST", data: strPost, scriptCharset: "utf-8", contentType: "application/x-www-form-urlencoded;charset=utf-8", cache: false, dataType: "xml", url: "ERP_Pto_Venta.jsp?id=2", success: function (datos) {
            var strFormPago = "";
            var objofrt = datos.getElementsByTagName("Formas_Pago")[0];
            var lstProms = objofrt.getElementsByTagName("datos");
            for (var i = 0; i < lstProms.length; i++) {
                var obj = lstProms[i];
                strFormPago += '<input type="button" style="font-size:11px" onclick="setMPago(\'' + obj.getAttribute("Descripcion") + '\');" value="' + obj.getAttribute("Descripcion") + '" id="' + obj.getAttribute("Descripcion") + '" name="' + obj.getAttribute("Descripcion") + '">';
            }
            document.getElementById("BOTONES_PAGOS").innerHTML = strFormPago;
            $("#dialogWait").dialog("close");
        }, error: function () {
            alert("Error Datos de Usuario!");
            $("#dialogWait").dialog("close");
        }});
    var strHtmlOut = "<table>" + "<tr>" + '<td ><i class = "fa fa-print" style="font-size:20px" onclick="SavePtoVtaDo();">Imprimir</i></td>' + "<td>&nbsp;&nbsp;&nbsp;</td>" + '<td ><i class = "fa fa-minus-square" style="font-size:20px" onclick="closeCobro();">Cancelar</i></td>' + "<td>&nbsp;&nbsp;&nbsp;</td>" + '<td ><i class = "fa fa-check" style="font-size:20px" onclick="SavePtoVtaDo();">Finalizar</i></td>' + "<td>&nbsp;&nbsp;&nbsp;</td>" + "</tr>" + "</table>";
    document.getElementById("BTN_FINAL").innerHTML = strHtmlOut;
    if (document.getElementById("PTO_MONEDA").value == 0) {
        document.getElementById("COB_MONEDA").value = 1;
    } else {
        document.getElementById("COB_MONEDA").value = document.getElementById("PTO_MONEDA").value;
    }
    setMPago("EFECTIVO");
    document.getElementById("COB_MONTO_PAGO").value = document.getElementById("PTO_TOTAL").value;
    document.getElementById("COB_RECARGO_MONTO").value = document.getElementById("CT_DESCUENTO").value;
    document.getElementById("CT_NOMBRE").value = document.getElementById("PTO_CLIENTE").value;
    var strMonto = "MONTO=" + document.getElementById("COB_MONTO_PAGO").value;
    $.ajax({type: "POST", data: strMonto, scriptCharset: "utf-8", contentType: "application/x-www-form-urlencoded;charset=utf-8", cache: false, dataType: "xml", url: "ERP_Pto_Venta.jsp?id=9", success: function (datos) {
            var strMontoMon = "";
            var objMon = datos.getElementsByTagName("Monedas")[0];
            var lstMon = objMon.getElementsByTagName("datos");
            for (var i = 0; i < lstMon.length; i++) {
                var obj = lstMon[i];
                strMontoMon += '<input type="button" style="font-size:11px" onclick="setMPago(\'' + obj.getAttribute("Descripcion") + '\');" value="' + obj.getAttribute("Descripcion") + '" id="' + obj.getAttribute("Descripcion") + '" name="' + obj.getAttribute("Descripcion") + '">';
            }
            document.getElementById("BTN_MONEDAS").innerHTML = strMontoMon;
            $("#dialogWait").dialog("close");
        }, error: function () {
            alert("Error Datos de Usuario!");
            $("#dialogWait").dialog("close");
        }});
}
function setMPago(strValor) {
    document.getElementById("LBL_FORM_PAGO").value = strValor;
}
function setMPagoMult(strValor) {
    document.getElementById("text_empty2").value = strValor;
    _drawFieldsMltp();
}
function CallExitPtoVenta() {
    myLayout.open("west");
    myLayout.open("east");
    myLayout.open("south");
    myLayout.open("north");
    document.getElementById("MainPanel").innerHTML = "";
    var objMainFacPedi = objMap.getScreen("PTO_VENTA");
    objMainFacPedi.bolActivo = false;
    objMainFacPedi.bolMain = false;
    objMainFacPedi.bolInit = false;
    objMainFacPedi.idOperAct = 0;
}
function opnPROD() {
    OpnOpt("PROD_MAK", "grid", "dialog2", false, false);
}
function opnCtePtoVenta() {
    var objSecModiVta = objMap.getScreen("CLIENT_MAK");
    if (objSecModiVta != null) {
        objSecModiVta.bolActivo = false;
        objSecModiVta.bolMain = false;
        objSecModiVta.bolInit = false;
        objSecModiVta.idOperAct = 0;
    }
    OpnOpt("CLIENT_MAK", "_ed", "dialogCte", false, false, true);
}
function creaBotonPaquetePtoVta() {
    $("#dialogWait").dialog("open");
    var strPost = "";
    $.ajax({type: "POST", data: strPost, scriptCharset: "utf-8", contentType: "application/x-www-form-urlencoded;charset=utf-8", cache: false, dataType: "html", url: "ERP_Pto_Venta.jsp?id=3", success: function (datos) {
            document.getElementById("CMB1").innerHTML = datos;
            $("#dialogWait").dialog("close");
        }, error: function () {
            alert("Error Datos de Usuario!");
            $("#dialogWait").dialog("close");
        }});
}
function existSesion() {
    $("#dialogWait").dialog("open");
    var strPost = "";
    $.ajax({type: "POST", data: strPost, scriptCharset: "utf-8", contentType: "application/x-www-form-urlencoded;charset=utf-8", cache: false, dataType: "html", url: "ERP_Pto_Venta.jsp?id=8", success: function (datos) {
            if (datos.trim() == "NO EXISTE") {
                opnApertura();
            }
            $("#dialogWait").dialog("close");
        }, error: function () {
            alert("Error Datos de Usuario!");
            $("#dialogWait").dialog("close");
        }});
}
function opnCombobyCodigo(strCodigo) {
    $("#dialogWait").dialog("open");
    var strPost = "PR_CODIGO=" + strCodigo;
    $.ajax({type: "POST", data: strPost, scriptCharset: "utf-8", contentType: "application/x-www-form-urlencoded;charset=utf-8", cache: false, dataType: "xml", url: "ERP_Pto_Venta.jsp?id=10", success: function (datos) {
            var intCombo = 0;
            var objofrt = datos.getElementsByTagName("ID_CMB")[0];
            var lstProms = objofrt.getElementsByTagName("datos");
            for (var i = 0; i < lstProms.length; i++) {
                var obj = lstProms[i];
                intCombo = obj.getAttribute("CMB_ID");
            }
            callCMB(intCombo);
            $("#dialogWait").dialog("close");
        }, error: function () {
            alert("Error Datos de Usuario!");
            $("#dialogWait").dialog("close");
        }});
}
function llenaVtasMostrador() {
    $("#dialogWait").dialog("open");
    var strPost = "";
    $.ajax({type: "POST", data: strPost, scriptCharset: "utf-8", contentType: "application/x-www-form-urlencoded;charset=utf-8", cache: false, dataType: "xml", url: "ERP_Pto_Venta.jsp?id=7", success: function (datos) {
            document.getElementById("PTO_CT_ID").value = intCteDefa;
            var objofrt = datos.getElementsByTagName("Ventas_M")[0];
            var lstProms = objofrt.getElementsByTagName("datos");
            for (var i = 0; i < lstProms.length; i++) {
                var obj = lstProms[i];
                document.getElementById("PTO_SUC").value = obj.getAttribute("PTO_SUCURSAL");
                document.getElementById("PTO_BODEGA").value = obj.getAttribute("PTO_BODEGA");
                document.getElementById("PTO_SC_ID").value = obj.getAttribute("SC_ID");
                document.getElementById("PTO_VENDEDOR").value = obj.getAttribute("PTO_VENDEDOR");
                document.getElementById("PTO_VE_ID").value = obj.getAttribute("PTO_VE_ID");
            }
            ObtenNomCtePtoVta();
            $("#dialogWait").dialog("close");
        }, error: function () {
            alert("Error Datos de Usuario!");
            $("#dialogWait").dialog("close");
        }});
}
function AddItemPtoVenta(event, obj) {
    if (event.keyCode == 13) {
        AddItemPtoVta();
    }
}
function AddItemPtoVta() {
    var strCodigo = document.getElementById("PTO_PR_CODIGO").value;
    $("#dialogWait").dialog("open");
    var strPost = "PR_CODIGO=" + strCodigo;
    $.ajax({type: "POST", data: strPost, scriptCharset: "utf-8", contentType: "application/x-www-form-urlencoded;charset=utf-8", cache: false, dataType: "html", url: "ERP_Pto_Venta.jsp?id=4", success: function (datos) {
            if (datos.trim() == "PRODUCTO") {
                addRegistro();
                document.getElementById("PTO_PR_CODIGO").focus();
            } else {
                if (datos.trim() == "COMBO") {
                    opnCombobyCodigo(strCodigo);
                    document.getElementById("PTO_PR_CODIGO").focus();
                } else {
                    if (datos.trim() != "COMBO" && datos.trim() != "PRODUCTO") {
                        ObtieneProductoPtoVta(d.getElementById("PTO_PR_CODIGO").value);
                    }
                }
            }
            $("#dialogWait").dialog("close");
        }, error: function () {
            alert("Error Datos de Usuario!");
            $("#dialogWait").dialog("close");
        }});
}
function AddProd(strCodigo) {
    var itemIdCob = 0;
    var grid = jQuery("#GR_PTO_VTA");
    var idArr = grid.getDataIDs();
    itemIdCob = idArr.length;
    var strCodigo = strCodigo;
    $("#dialogWait").dialog("open");
    var strPost = "PR_CODIGO=" + strCodigo;
    $.ajax({type: "POST", data: strPost, scriptCharset: "utf-8", contentType: "application/x-www-form-urlencoded;charset=utf-8", cache: false, dataType: "xml", url: "ERP_Pto_Venta.jsp?id=5", success: function (datos) {
            var objofrt = datos.getElementsByTagName("Producto")[0];
            var lstProms = objofrt.getElementsByTagName("Descripcion");
            for (var i = 0; i < lstProms.length; i++) {
                var obj = lstProms[i];
                document.getElementById("PTO_PR_ID").value = obj.getAttribute("PR_ID");
                document.getElementById("PTO_PR_DESC").value = obj.getAttribute("PR_DESC");
                document.getElementById("PTO_PR_EXIST").value = obj.getAttribute("PR_EXISTENCIA");
                document.getElementById("PTO_PREC_U").value = parseFloat(obj.getAttribute("PR_PRECU"));
                var dataRow = {GR_ID: itemIdCob, GR_PR_ID: document.getElementById("PTO_PR_ID").value, GR_CODIGO: strCodigo, GR_DESCRIPCION: document.getElementById("PTO_PR_DESC").value, GR_EXISTENCIA: document.getElementById("PTO_PR_EXIST").value, GR_DISPONIBLE: "0", GR_CANTIDAD: "1", GR_PU: document.getElementById("PTO_PREC_U").value, GR_TOTAL: document.getElementById("PTO_PREC_U").value};
                itemIdCob++;
                jQuery("#GR_PTO_VTA").addRowData(itemIdCob, dataRow, "last");
            }
            document.getElementById("PTO_PR_CODIGO").value = "";
            setTotales();
            sumTotal();
            $("#dialogWait").dialog("close");
        }, error: function () {
            alert("Error Datos de Usuario!");
            $("#dialogWait").dialog("close");
        }});
}
function AddCombo(strCodigo) {
    var itemIdCob = 0;
    var grid = jQuery("#GR_PTO_VTA");
    var idArr = grid.getDataIDs();
    itemIdCob = idArr.length;
    var strCodigo = strCodigo;
    $("#dialogWait").dialog("open");
    var strPost = "PR_CODIGO=" + strCodigo;
    $.ajax({type: "POST", data: strPost, scriptCharset: "utf-8", contentType: "application/x-www-form-urlencoded;charset=utf-8", cache: false, dataType: "xml", url: "ERP_Pto_Venta.jsp?id=5", success: function (datos) {
            var objofrt = datos.getElementsByTagName("Producto")[0];
            var lstProms = objofrt.getElementsByTagName("Descripcion");
            for (var i = 0; i < lstProms.length; i++) {
                var obj = lstProms[i];
                var dataRow = {FACD_ID: 0, FACD_CANTIDAD: obj.getAttribute("PR_CANTIDAD"), FACD_DESCRIPCION: obj.getAttribute("PR_DESC"), FACD_IMPORTE: 0, FACD_CVE: obj.getAttribute("PR_CODIGO"), FACD_PRECIO: 0, FACD_TASAIVA1: 0, FACD_TASAIVA2: 0, FACD_TASAIVA3: 0, FACD_DESGLOSA1: 1, FACD_IMPUESTO1: 0, FACD_IMPUESTO2: 0, FACD_IMPUESTO3: 0, FACD_PR_ID: obj.getAttribute("PR_ID"), FACD_EXENTO1: 0, FACD_EXENTO2: 0, FACD_EXENTO3: 0, FACD_REQEXIST: 0, FACD_EXIST: obj.getAttribute("PR_EXISTENCIA"), FACD_NOSERIE: "", FACD_ESREGALO: 0, FACD_IMPORTEREAL: 0, FACD_PRECREAL: 0, FACD_DESCUENTO: 0, FACD_PORDESC: 0, FACD_PRECFIJO: 0, FACD_ESDEVO: 0, FACD_CODBARRAS: "", FACD_NOTAS: "", FACD_RET_ISR: 0, FACD_RET_IVA: 0, FACD_RET_FLETE: 0, FACD_UNIDAD_MEDIDA: "", FACD_PUNTOS_U: 0, FACD_NEGOCIO_U: 0, FACD_PUNTOS: 0, FACD_NEGOCIO: 0, FACD_PR_CAT1: 0, FACD_PR_CAT2: 0, FACD_PR_CAT3: 0, FACD_PR_CAT4: 0, FACD_PR_CAT5: 0, FACD_PR_CAT6: 0, FACD_PR_CAT7: 0, FACD_PR_CAT8: 0, FACD_PR_CAT9: 0, FACD_PR_CAT10: 0, FACD_DESC_ORI: 0, FACD_REGALO: 0, FACD_ID_PROMO: 0, FACD_DESC_PREC: 0, FACD_DESC_PTO: 0, FACD_DESC_VN: 0, FACD_DESC_LEAL: 0, FACD_USA_SERIE: 0, FACD_SERIES: "", FACD_SERIES_MPD: "", FACD_SERIES_O: "", FACD_SERIES_MPD_O: ""};
                itemIdCob++;
                jQuery("#GR_PTO_VTA").addRowData(itemIdCob, dataRow, "last");
            }
            document.getElementById("PTO_PR_CODIGO").value = "";
            setTotales();
            sumTotal();
            $("#dialogWait").dialog("close");
        }, error: function () {
            alert("Error Datos de Usuario!");
            $("#dialogWait").dialog("close");
        }});
}
function del_Producto() {
    var grid = jQuery("#GR_PTO_VTA");
    var idCmb = grid.getGridParam("selrow");
    if (idCmb != null) {
        grid.delRowData(idCmb);
    } else {
        alert("Debe seleccionar un producto");
    }
    setTotales();
    sumTotal();
}
function sumTotal() {
    var dblSubTotal = 0;
    var dblDescuento = 0;
    var tmpDescuento = 0;
    var grid1 = jQuery("#GR_PTO_VTA");
    var idArr1 = grid1.getDataIDs();
    if (idArr1.length == 0) {
    } else {
        for (var i = 0; i < idArr1.length; i++) {
            var id = idArr1[i];
            var lstRow = grid1.getRowData(id);
            dblSubTotal = dblSubTotal + parseFloat(lstRow.FACD_IMPORTE);
        }
    }
    document.getElementById("PTO_SUBTOTAL").value = dblSubTotal;
    dblDescuento = document.getElementById("PTO_DESCUENTO").value;
    tmpDescuento = (dblSubTotal / 100) * dblDescuento;
    dblSubTotal = dblSubTotal - tmpDescuento;
    document.getElementById("CT_DESCUENTO").value = tmpDescuento;
    document.getElementById("PTO_TOTAL").value = dblSubTotal;
    clalcTasasPtoVta();
}
function SaveVtaPto() {
    if (document.getElementById("PTO_SUBTOTAL") != null) {
        var grid = jQuery("#GR_PTO_VTA");
        var idArr = grid.getDataIDs();
        if (idArr.length > 0) {
            opnCobro();
        } else {
            alert(lstMsg[161]);
        }
    }
}
function addRegistro() {
    var strPrefijoMaster = "";
    var strPrefijoDeta = "";
    var strKey = "";
    var strNomFormat = "";
    var strTipoDocumentoPto = 0;
    if (document.getElementById("PTO_DOCUMENTO0").checked) {
        strPrefijoMaster = "TKT";
        strPrefijoDeta = "TKTD";
        strKey = "TKT_ID";
        strNomFormat = "TICKET";
        strTipoDocumentoPto = 2;
    } else {
        strPrefijoMaster = "FAC";
        strPrefijoDeta = "FACD";
        strKey = "FAC_ID";
        strNomFormat = "FACTURA";
        strTipoDocumentoPto = 1;
    }
    var strCod = UCase(d.getElementById("PTO_PR_CODIGO").value);
    if (trim(strCod) != "") {
        var intDevo = 0;
        var bolAgrupa = true;
        $("#dialogWait").dialog("open");
        var bolNvo = true;
        var idProd = 0;
        if (bolAgrupa) {
            var grid = jQuery("#GR_PTO_VTA");
            var arr = grid.getDataIDs();
            for (var i = 0; i < arr.length; i++) {
                var id = arr[i];
                var lstRowAct = grid.getRowData(id);
                if (lstRowAct.FACD_CVE == strCod || lstRowAct.FACD_CODBARRAS == strCod) {
                    if (intDevo == 1) {
                        if (lstRow.FACD_ESDEVO == 1) {
                            idProd = id;
                            bolNvo = false;
                            break;
                        }
                    } else {
                        idProd = id;
                        bolNvo = false;
                        break;
                    }
                }
            }
        }
        if (bolNvo) {
            $.ajax({type: "POST", data: encodeURI("PR_CODIGO=" + strCod + "&SC_ID=" + d.getElementById("PTO_SC_ID").value), scriptCharset: "utf-8", contentType: "application/x-www-form-urlencoded;charset=utf-8", cache: false, dataType: "xml", url: "VtasMov.do?id=5", success: function (datoVal) {
                    var objProd = datoVal.getElementsByTagName("vta_productos")[0];
                    var Pr_Id = 0;
                    if (objProd != undefined) {
                        Pr_Id = objProd.getAttribute("PR_ID");
                        d.getElementById("PTO_PR_DESC").value = objProd.getAttribute("PR_DESCRIPCION");
                        if (Pr_Id != 0) {
                            strCod = objProd.getAttribute("PR_CODIGO");
                        }
                    }
                    var Ct_Id = d.getElementById("PTO_CT_ID").value;
                    var dblCantidad = 1;
                    if (intDevo == 1) {
                        dblCantidad = 0;
                    }
                    if (Pr_Id != 0) {
                        var dblExistencia = objProd.getAttribute("PR_EXISTENCIA");
                        if (dblExistencia > 0) {
                            if (objProd.getAttribute("PR_REQEXIST") == 1 && strTipoDocumentoPto != 3 && strTipoDocumentoPto != 5) {
                                $.ajax({type: "POST", data: "PR_ID=" + Pr_Id, scriptCharset: "utf-8", contentType: "application/x-www-form-urlencoded;charset=utf-8", cache: false, dataType: "html", url: "InvMov.do?id=1", success: function (datoExist) {
                                        dblExistencia = parseFloat(datoExist);
                                        if (parseFloat(dblCantidad) > dblExistencia) {
                                            alert(lstMsg[3] + "(" + dblCantidad + ") " + lstMsg[34] + strCod + "(" + dblExistencia + ") " + lstMsg[4]);
                                            if (parseFloat(dblExistencia) > 0) {
                                                dblCantidad = dblExistencia;
                                            } else {
                                                dblCantidad = 0;
                                            }
                                        }
                                        if (objProd.getAttribute("PR_USO_NOSERIE") == 1) {
                                            _drawScNoSeriePtoVta(objProd, Pr_Id, Ct_Id, dblCantidad, strCod, dblExistencia, intDevo);
                                        } else {
                                            AddProdPrec(objProd, Pr_Id, Ct_Id, dblCantidad, strCod, dblExistencia, intDevo);
                                        }
                                    }, error: function (objeto, quepaso, otroobj) {
                                        alert(":ptoExist:" + objeto + " " + quepaso + " " + otroobj);
                                        $("#dialogWait").dialog("close");
                                    }});
                            } else {
                                AddProdPrec(objProd, Pr_Id, Ct_Id, dblCantidad, strCod, dblExistencia, intDevo);
                            }
                        } else {
                            alert("NO HAY EXISTENCIA DEL PRODUCTO: " + strCod);
                            document.getElementById("PTO_PR_CODIGO").value = "";
                            document.getElementById("PTO_PR_CODIGO").focus();
                        }
                    } else {
                        alert(lstMsg[0]);
                        document.getElementById("PTO_PR_CODIGO").focus();
                        $("#dialogWait").dialog("close");
                    }
                }, error: function (objeto, quepaso, otroobj) {
                    alert(":pto3:" + objeto + " " + quepaso + " " + otroobj);
                    $("#dialogWait").dialog("close");
                }});
        } else {
            var Cantidad = 1;
            var gridD = jQuery("#GR_PTO_VTA");
            var lstRow = gridD.getRowData(idProd);
            if (lstRow.FACD_USA_SERIE == 1) {
                _drawScNoSeriePtoVta(null, lstRow.FACD_PR_ID, 0, parseFloat(Cantidad), "", 0, 0, idProd, lstRow.FACD_SERIES);
            } else {
                lstRow.FACD_CANTIDAD = parseFloat(lstRow.FACD_CANTIDAD) + parseFloat(Cantidad);
                if (lstRow.FACD_REQEXIST == 1 && strTipoDocumentoPto != 3) {
                    if (parseFloat(lstRow.FACD_CANTIDAD) > parseFloat(lstRow.FACD_EXIST)) {
                        alert(lstMsg[3] + " " + lstRow.FACD_CVE + " " + lstMsg[4]);
                        if (parseFloat(lstRow.FACD_EXIST) > 0) {
                            lstRow.FACD_CANTIDAD = lstRow.FACD_EXIST;
                        } else {
                            lstRow.FACD_CANTIDAD = 0;
                        }
                    }
                }
                lstRowChangePrecioPtoVenta(lstRow, idProd, gridD);
                document.getElementById("PTO_PR_CODIGO").value = "";
                document.getElementById("PTO_PR_CODIGO").focus();
                $("#dialogWait").dialog("close");
            }
        }
    }
}
function AddProdPrec(objProd, Pr_Id, Ct_Id, Cantidad, strCod, dblExist, intDevo, strSeries) {
    if (strSeries == null) {
        strSeries = "";
    }
    $.ajax({type: "POST", data: "PR_ID=" + Pr_Id + "&CT_LPRECIOS=" + document.getElementById("PTO_LPRECIOS").value + "&CANTIDAD=" + Cantidad + "&FAC_MONEDA=" + document.getElementById("PTO_MONEDA").value + "&CT_TIPO_CAMBIO=" + document.getElementById("PTO_TASA_PESO").value, scriptCharset: "utf-8", contentType: "application/x-www-form-urlencoded;charset=utf-8", cache: false, dataType: "xml", url: "DamePrecio.do?id=4", success: function (datoPrec) {
            var bolFind = false;
            var lstXml = datoPrec.getElementsByTagName("Precios")[0];
            var lstprecio = lstXml.getElementsByTagName("Precio");
            for (var i = 0; i < lstprecio.length; i++) {
                var obj2 = lstprecio[i];
                var ptoVtaImportes = new _ImportePto();
                ptoVtaImportes.dblCantidad = Cantidad;
                var dblPrecio = obj2.getAttribute("precioUsar");
                ptoVtaImportes.dblPuntos = parseFloat(obj2.getAttribute("puntos"));
                ptoVtaImportes.dblVNegocio = parseFloat(obj2.getAttribute("negocio"));
                ptoVtaImportes.dblPrecio = parseFloat(dblPrecio);
                ptoVtaImportes.dblPrecioReal = parseFloat(dblPrecio);
                ptoVtaImportes.dblPorcDescGlobal = document.getElementById("PTO_DESCUENTO").value;
                ptoVtaImportes.dblExento1 = objProd.getAttribute("PR_EXENTO1");
                ptoVtaImportes.dblExento2 = objProd.getAttribute("PR_EXENTO2");
                ptoVtaImportes.dblExento3 = objProd.getAttribute("PR_EXENTO3");
                ptoVtaImportes.intDevo = intDevo;
                if (parseInt(obj2.getAttribute("descuento")) == 0) {
                    ptoVtaImportes.bolAplicDescPrec = false;
                }
                if (parseInt(obj2.getAttribute("desc_pto")) == 0) {
                    ptoVtaImportes.bolAplicDescPto = false;
                }
                if (parseInt(obj2.getAttribute("desc_nego")) == 0) {
                    ptoVtaImportes.bolAplicDescVNego = false;
                }
                var bolAplicaMLM = true;
                if (document.getElementById("FAC_ES_MLM1") != null && document.getElementById("FAC_ES_MLM2") != null) {
                    if (document.getElementById("FAC_ES_MLM2").checked) {
                        bolAplicaMLM = false;
                    }
                }
                ptoVtaImportes.bolUsoMLM = bolAplicaMLM;
                ptoVtaImportes.CalculaImporte();
                var dblDescuento = ptoVtaImportes.dblImporteDescuento;
                var dblImporte = ptoVtaImportes.dblImporte;
                var datarow = {FACD_ID: 0, FACD_CANTIDAD: Cantidad, FACD_DESCRIPCION: objProd.getAttribute("PR_DESCRIPCION"), FACD_IMPORTE: dblImporte, FACD_CVE: strCod, FACD_PRECIO: dblPrecio, FACD_TASAIVA1: dblTasaPto1, FACD_TASAIVA2: dblTasaPto2, FACD_TASAIVA3: dblTasaPto3, FACD_DESGLOSA1: 1, FACD_IMPUESTO1: ptoVtaImportes.dblImpuesto1, FACD_IMPUESTO2: ptoVtaImportes.dblImpuesto2, FACD_IMPUESTO3: ptoVtaImportes.dblImpuesto3, FACD_PR_ID: Pr_Id, FACD_EXENTO1: objProd.getAttribute("PR_EXENTO1"), FACD_EXENTO2: objProd.getAttribute("PR_EXENTO2"), FACD_EXENTO3: objProd.getAttribute("PR_EXENTO3"), FACD_REQEXIST: objProd.getAttribute("PR_REQEXIST"), FACD_EXIST: dblExist, FACD_NOSERIE: strSeries, FACD_ESREGALO: 0, FACD_IMPORTEREAL: dblImporte, FACD_PRECREAL: dblPrecio, FACD_DESCUENTO: dblDescuento, FACD_PORDESC: ptoVtaImportes.dblPorcAplica, FACD_PRECFIJO: 0, FACD_ESDEVO: intDevo, FACD_CODBARRAS: objProd.getAttribute("PR_CODBARRAS"), FACD_NOTAS: "", FACD_RET_ISR: 0, FACD_RET_IVA: 0, FACD_RET_FLETE: 0, FACD_UNIDAD_MEDIDA: objProd.getAttribute("PR_UNIDADMEDIDA"), FACD_PUNTOS_U: ptoVtaImportes.dblPuntos, FACD_NEGOCIO_U: ptoVtaImportes.dblVNegocio, FACD_PUNTOS: ptoVtaImportes.dblPuntosImporte, FACD_NEGOCIO: ptoVtaImportes.dblVNegocioImporte, FACD_PR_CAT1: objProd.getAttribute("PR_CAT1"), FACD_PR_CAT2: objProd.getAttribute("PR_CAT2"), FACD_PR_CAT3: objProd.getAttribute("PR_CAT3"), FACD_PR_CAT4: objProd.getAttribute("PR_CAT4"), FACD_PR_CAT5: objProd.getAttribute("PR_CAT5"), FACD_PR_CAT6: objProd.getAttribute("PR_CAT6"), FACD_PR_CAT7: objProd.getAttribute("PR_CAT7"), FACD_PR_CAT8: objProd.getAttribute("PR_CAT8"), FACD_PR_CAT9: objProd.getAttribute("PR_CAT9"), FACD_PR_CAT10: objProd.getAttribute("PR_CAT10"), FACD_DESC_ORI: 0, FACD_REGALO: 0, FACD_ID_PROMO: 0, FACD_DESC_PREC: parseInt(obj2.getAttribute("descuento")), FACD_DESC_PTO: parseInt(obj2.getAttribute("desc_pto")), FACD_DESC_VN: parseInt(obj2.getAttribute("desc_nego")), FACD_DESC_LEAL: parseInt(obj2.getAttribute("desc_nego")), FACD_USA_SERIE: objProd.getAttribute("PR_USO_NOSERIE"), FACD_SERIES: strSeries, FACD_SERIES_MPD: "", FACD_SERIES_O: "", FACD_SERIES_MPD_O: ""};
                itemId++;
                jQuery("#GR_PTO_VTA").addRowData(itemId, datarow, "last");
                d.getElementById("PTO_SUBTOTAL").value = dblPrecio;
                setTotales();
                sumTotal();
                d.getElementById("PTO_PR_CODIGO").value = "";
                d.getElementById("PTO_PR_CODIGO").focus();
            }
            $("#dialogWait").dialog("close");
        }, error: function (objeto, quepaso, otroobj) {
            alert(":pto4:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }});
}
function lstRowChangePrecioPtoVenta(lstRow, idUpdate, grid, bolSum) {
    var ptoVtaImportes = new _ImportePto();
    ptoVtaImportes.dblCantidad = parseFloat(lstRow.FACD_CANTIDAD);
    ptoVtaImportes.dblPrecio = parseFloat(lstRow.FACD_PRECIO);
    ptoVtaImportes.dblPrecioReal = parseFloat(lstRow.FACD_PRECREAL);
    ptoVtaImportes.dblPuntos = parseFloat(lstRow.FACD_PUNTOS_U);
    ptoVtaImportes.dblVNegocio = parseFloat(lstRow.FACD_NEGOCIO_U);
    ptoVtaImportes.dblPorcDescGlobal = document.getElementById("PTO_DESCUENTO").value;
    ptoVtaImportes.dblPorcDesc = lstRow.FACD_PORDESC;
    ptoVtaImportes.dblPrecFijo = lstRow.FACD_PRECFIJO;
    ptoVtaImportes.dblExento1 = lstRow.FACD_EXENTO1;
    ptoVtaImportes.dblExento2 = lstRow.FACD_EXENTO2;
    ptoVtaImportes.dblExento3 = lstRow.FACD_EXENTO3;
    ptoVtaImportes.intDevo = lstRow.FACD_ESDEVO;
    ptoVtaImportes.intPrecioZeros = lstRow.FACD_SINPRECIO;
    if (lstRow.FACD_DESC_PREC == 0) {
        ptoVtaImportes.bolAplicDescPrec = false;
    }
    if (lstRow.FACD_DESC_PTO == 0) {
        ptoVtaImportes.bolAplicDescPto = false;
    }
    if (lstRow.FACD_DESC_VN == 0) {
        ptoVtaImportes.bolAplicDescVNego = false;
    }
    var bolAplicaMLM = true;
    if (document.getElementById("FAC_ES_MLM1") != null && document.getElementById("FAC_ES_MLM2") != null) {
        if (document.getElementById("FAC_ES_MLM2").checked) {
            bolAplicaMLM = false;
        }
    }
    ptoVtaImportes.bolUsoMLM = bolAplicaMLM;
    ptoVtaImportes.CalculaImporte();
    lstRow.FACD_IMPORTE = ptoVtaImportes.dblImporte;
    lstRow.FACD_IMPUESTO1 = ptoVtaImportes.dblImpuesto1;
    lstRow.FACD_IMPUESTO2 = ptoVtaImportes.dblImpuesto2;
    lstRow.FACD_IMPUESTO3 = ptoVtaImportes.dblImpuesto3;
    lstRow.FACD_DESCUENTO = ptoVtaImportes.dblImporteDescuento;
    lstRow.FACD_IMPORTEREAL = ptoVtaImportes.dblImporteReal;
    lstRow.FACD_PUNTOS = ptoVtaImportes.dblPuntosImporte;
    lstRow.FACD_NEGOCIO = ptoVtaImportes.dblVNegocioImporte;
    if (lstRow.FACD_NEGO_ZERO == 1) {
        lstRow.FACD_NEGOCIO = 0;
    }
    grid.setRowData(idUpdate, lstRow);
    if (bolSum == null) {
        bolSum = true;
    }
    if (bolSum) {
        setTotales();
    }
}
function _drawScNoSeriePtoVta(objProd, Pr_Id, Ct_Id, intCantidad, strCod, dblExist, intDevo, idProd, strSeries) {
    _objProdTmpz = objProd;
    if (strSeries == null) {
        strSeries = "";
    }
    $.ajax({type: "POST", data: "PR_ID=" + Pr_Id, scriptCharset: "utf-8", contentType: "application/x-www-form-urlencoded;charset=utf-8", cache: false, dataType: "xml", url: "InvMov.do?id=21", success: function (datos) {
            var objCodBar = datos.getElementsByTagName("num_series")[0];
            var lstProd = objCodBar.getElementsByTagName("serie");
            var strOptionSelect = "";
            for (i = 0; i < lstProd.length; i++) {
                var obj = lstProd[i];
                strOptionSelect += "<option value='" + obj.getAttribute("NO_SERIE") + "'>" + obj.getAttribute("NO_SERIE") + "</option>";
            }
            $("#dialog2").dialog("open");
            $("#dialog2").dialog("option", "title", lstMsg[107]);
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
            strHTML += "</tr>";
            strHTML += "</table>";
            document.getElementById("dialog2_inside").innerHTML = strHTML;
        }, error: function (objeto, quepaso, otroobj) {
            alert(":Series pto:" + objeto + " " + quepaso + " " + otroobj);
        }});
}
function _ImportePto() {
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
    this.CalculaImporte = function CalculaImporte() {
        this.dblPorcDescGlobal = parseFloat(this.dblPorcDescGlobal);
        this.dblPorcDesc = parseFloat(this.dblPorcDesc);
        var dblPrecioAplica = parseFloat(this.dblPrecio);
        this.dblPuntosAplica = this.dblPuntos;
        this.dblVNegocioAplica = this.dblVNegocio;
        this.dblPorcAplica = 0;
        if (this.dblPorcDescGlobal > 0 && this.dblPorcDesc > 0) {
            if (this.dblPorcDescGlobal > this.dblPorcDesc) {
                this.dblPorcAplica = this.dblPorcDescGlobal;
            }
            if (this.dblPorcDesc > this.dblPorcDescGlobal) {
                this.dblPorcAplica = this.dblPorcDesc;
            }
            if (this.dblPorcDesc == this.dblPorcDescGlobal) {
                this.dblPorcAplica = this.dblPorcDesc;
            }
        } else {
            if (this.dblPorcDescGlobal > 0) {
                this.dblPorcAplica = this.dblPorcDescGlobal;
            }
            if (this.dblPorcDesc > 0) {
                this.dblPorcAplica = this.dblPorcDesc;
            }
        }
        if (this.dblPorcAplica > 0) {
            if (this.bolAplicDescPrec) {
                dblPrecioAplica = dblPrecioAplica - (dblPrecioAplica * (this.dblPorcAplica / 100));
            }
            if (this.bolAplicDescPto) {
                this.dblPuntosAplica = this.dblPuntosAplica - (this.dblPuntosAplica * (this.dblPorcAplica / 100));
            }
            if (this.bolAplicDescVNego) {
                this.dblVNegocioAplica = this.dblVNegocioAplica - (this.dblVNegocioAplica * (this.dblPorcAplica / 100));
            }
        }
        this.dblImporte = parseFloat(this.dblCantidad) * parseFloat(dblPrecioAplica);
        this.dblImporteReal = parseFloat(this.dblCantidad) * parseFloat(this.dblPrecioReal);
        if (this.dblImporteReal > 0 && (this.dblImporteReal > this.dblImporte)) {
            this.dblImporteDescuento = this.dblImporteReal - this.dblImporte;
        }
        if (parseInt(this.intDevo) == 1) {
            this.dblImporte = this.dblImporte * -1;
        }
        if (this.bolUsoMLM) {
            this.dblPuntosImporte = parseFloat(this.dblCantidad) * parseFloat(this.dblPuntosAplica);
            this.dblVNegocioImporte = parseFloat(this.dblCantidad) * parseFloat(this.dblVNegocioAplica);
        }
        var dblBase1 = this.dblImporte;
        var dblBase2 = this.dblImporte;
        var dblBase3 = this.dblImporte;
        if (parseInt(this.dblExento1) == 1) {
            dblBase1 = 0;
        }
        if (parseInt(this.dblExento2) == 1) {
            dblBase2 = 0;
        }
        if (parseInt(this.dblExento3) == 1) {
            dblBase3 = 0;
        }
        var tax = new Impuestos(dblTasaPto1, dblTasaPto2, dblTasaPto3, intSImpPto1_2, intSImpPto1_3, intSImpPto2_3);
        if (intPreciosconImp == 1) {
            tax.CalculaImpuesto(dblBase1, dblBase2, dblBase3);
        } else {
            tax.CalculaImpuestoMas(dblBase1, dblBase2, dblBase3);
        }
        if (parseInt(this.dblExento1) == 0) {
            this.dblImpuesto1 = tax.dblImpuesto1;
        }
        if (parseInt(this.dblExento2) == 0) {
            this.dblImpuesto2 = tax.dblImpuesto2;
        }
        if (parseInt(this.dblExento3) == 0) {
            this.dblImpuesto3 = tax.dblImpuesto3;
        }
        var dblBaseReal1 = this.dblImporteReal;
        var dblBaseReal2 = this.dblImporteReal;
        var dblBaseReal3 = this.dblImporteReal;
        if (parseInt(this.dblExento1) == 1) {
            dblBaseReal1 = 0;
        }
        if (parseInt(this.dblExento2) == 1) {
            dblBaseReal2 = 0;
        }
        if (parseInt(this.dblExento3) == 1) {
            dblBaseReal3 = 0;
        }
        if (intPreciosconImp == 1) {
            tax.CalculaImpuesto(dblBaseReal1, dblBaseReal2, dblBaseReal3);
        } else {
            tax.CalculaImpuestoMas(dblBaseReal1, dblBaseReal2, dblBaseReal3);
        }
        if (parseInt(this.dblExento1) == 0) {
            this.dblImpuestoReal1 = tax.dblImpuesto1;
        }
        if (parseInt(this.dblExento2) == 0) {
            this.dblImpuestoReal2 = tax.dblImpuesto2;
        }
        if (parseInt(this.dblExento3) == 0) {
            this.dblImpuestoReal3 = tax.dblImpuesto3;
        }
        if (this.intPrecioZeros == 1) {
            this.dblImporteReal = parseFloat(this.dblCantidad) * parseFloat(this.dblPrecio);
        }
        if (intPreciosconImp == 0) {
            this.dblImporteReal += this.dblImpuestoReal1 + this.dblImpuestoReal2 + this.dblImpuestoReal3;
            this.dblImporte += this.dblImpuesto1 + this.dblImpuesto2 + this.dblImpuesto3;
        }
        if (intPreciosconImp == 1) {
            if (this.dblImporteReal > 0) {
                var dblTotImpuesto = tax.dblImpuesto1 + tax.dblImpuesto2 + tax.dblImpuesto3;
                var dblTotImpuestoReal = tax.dblImpuestoReal1 + tax.dblImpuestoReal2 + tax.dblImpuestoReal3;
                if (this.dblImporteReal > 0 && (this.dblImporteReal > this.dblImporte)) {
                    this.dblImporteDescuento = (this.dblImporteReal - dblTotImpuestoReal) - (this.dblImporte - dblTotImpuesto);
                }
            }
        }
    };
}
function setTotales() {
    var grid = jQuery("#GR_PTO_VTA");
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
        dblSuma += parseFloat(lstRow.FACD_IMPORTE);
        dblImpuesto1 += parseFloat(lstRow.FACD_IMPUESTO1);
        dblImpuesto2 += parseFloat(lstRow.FACD_IMPUESTO2);
        dblImpuesto3 += parseFloat(lstRow.FACD_IMPUESTO3);
        dblImporte += (parseFloat(lstRow.FACD_IMPORTE) - parseFloat(lstRow.FACD_IMPUESTO1) - parseFloat(lstRow.FACD_IMPUESTO2) - parseFloat(lstRow.FACD_IMPUESTO3));
        dblImporteDesc += parseFloat(lstRow.FACD_DESCUENTO);
        dblImportePto += parseFloat(lstRow.FACD_PUNTOS);
        dblImporteVn += parseFloat(lstRow.FACD_NEGOCIO);
        dblImportePzas += parseFloat(lstRow.FACD_CANTIDAD);
        dblImportePtoReal += parseFloat(lstRow.FACD_CANTIDAD) * parseFloat(lstRow.FACD_PUNTOS_U);
        dblImporteNegoReal += parseFloat(lstRow.FACD_CANTIDAD) * parseFloat(lstRow.FACD_NEGOCIO_U);
        dblImporteCredReal += parseFloat(lstRow.FACD_CANTIDAD) * 1;
        var dblTotlImpReal = parseFloat(lstRow.FACD_CANTIDAD) * parseFloat(lstRow.FACD_PRECREAL);
        var dblBase1 = dblTotlImpReal;
        var dblBase2 = dblTotlImpReal;
        var dblBase3 = dblTotlImpReal;
        if (parseInt(lstRow.FACD_EXENTO1) == 1) {
            dblBase1 = 0;
        }
        if (parseInt(lstRow.FACD_EXENTO2) == 1) {
            dblBase2 = 0;
        }
        if (parseInt(lstRow.FACD_EXENTO3) == 1) {
            dblBase3 = 0;
        }
        var taxReal = new Impuestos(dblTasaPto1, dblTasaPto2, dblTasaPto3, intSImpPto1_2, intSImpPto1_3, intSImpPto2_3);
        if (intPreciosconImp == 1) {
            taxReal.CalculaImpuesto(dblBase1, dblBase2, dblBase3);
        } else {
            taxReal.CalculaImpuestoMas(dblBase1, dblBase2, dblBase3);
        }
        if (parseInt(lstRow.FACD_EXENTO1) == 0) {
            dblImporteImpuesto1Real = taxReal.dblImpuesto1;
        }
        if (parseInt(lstRow.FACD_EXENTO2) == 0) {
            dblImporteImpuesto2Real = taxReal.dblImpuesto2;
        }
        if (parseInt(lstRow.FACD_EXENTO3) == 0) {
            dblImporteImpuesto3Real = taxReal.dblImpuesto3;
        }
        if (intPreciosconImp == 1) {
            dblImporteReal += dblTotlImpReal - dblImporteImpuesto1Real - dblImporteImpuesto2Real - dblImporteImpuesto3Real;
        } else {
            dblImporteReal += dblTotlImpReal;
        }
    }
    var dblIEPS = 0;
    if (document.getElementById("PTO_USO_IEPS1").checked) {
        if (parseFloat(document.getElementById("FAC_TASA_IEPS").value) != 0) {
            try {
                dblIEPS = dblImporte * (parseFloat(document.getElementById("FAC_TASA_IEPS").value) / 100);
            } catch (err) {
            }
        } else {
            alert(lstMsg[62]);
            document.getElementById("FAC_TASA_IEPS").focus();
        }
        var tax = new Impuestos(dblTasaPto1, dblTasaPto2, dblTasaPto3, intSImpPto1_2, intSImpPto1_3, intSImpPto2_3);
        tax.CalculaImpuestoMas(dblIEPS, 0, 0);
        dblImpuesto1 += tax.dblImpuesto1;
        dblSuma += dblIEPS + tax.dblImpuesto1;
    }
    d.getElementById("PTO_TOTAL").value = FormatNumber(dblSuma, intNumdecimal, true);
    d.getElementById("PTO_IMPUESTO1").value = FormatNumber(dblImpuesto1, intNumdecimal, true);
    d.getElementById("PTO_IMPUESTO2").value = FormatNumber(dblImpuesto2, intNumdecimal, true);
    d.getElementById("PTO_IMPUESTO3").value = FormatNumber(dblImpuesto3, intNumdecimal, true);
    d.getElementById("CT_DESCUENTO").value = FormatNumber(dblImporteDesc, intNumdecimal, true);
    var strPrefijoMaster = "";
    var strPrefijoDeta = "";
    var strKey = "";
    var strNomFormat = "";
    var strTipoDocumentoPto = 0;
    if (document.getElementById("PTO_DOCUMENTO0").checked) {
        strPrefijoMaster = "TKT";
        strPrefijoDeta = "TKTD";
        strKey = "TKT_ID";
        strNomFormat = "TICKET";
        strTipoDocumentoPto = 2;
    } else {
        strPrefijoMaster = "FAC";
        strPrefijoDeta = "FACD";
        strKey = "FAC_ID";
        strNomFormat = "FACTURA";
        strTipoDocumentoPto = 1;
    }
    if (parseInt(intEMP_TIPOPERS) == 2 && parseInt(strTipoDocumentoPto) == 1) {
        if (intCT_TIPOPERS == 1) {
            var dblRetIsr = dblImporte * (dblFacRetISR / 100);
            var dblRetIVA = 0;
            if (dblImpuesto1 > 0) {
                dblRetIVA = (dblImpuesto1 / 3) * 2;
            }
            if (parseInt(intEMP_NO_ISR) == 1) {
                dblRetIsr = 0;
            }
            if (parseInt(intEMP_NO_IVA) == 1) {
                dblRetIVA = 0;
            }
            var dblImpNeto = dblSuma - dblRetIsr - dblRetIVA;
            document.getElementById("PTO_RETISR").value = FormatNumber(dblRetIsr, intNumdecimal, true);
            document.getElementById("PTO_IVA").value = FormatNumber(dblRetIVA, intNumdecimal, true);
            document.getElementById("PTO_NETO").value = FormatNumber(dblImpNeto, intNumdecimal, true);
            document.getElementById("PTO_RETISR").parentNode.parentNode.style.display = "block";
            document.getElementById("PTO_IVA").parentNode.parentNode.style.display = "block";
            document.getElementById("PTO_NETO").parentNode.parentNode.style.display = "block";
        } else {
            document.getElementById("PTO_RETISR").parentNode.parentNode.style.display = "none";
            document.getElementById("PTO_IVA").parentNode.parentNode.style.display = "none";
            document.getElementById("PTO_NETO").parentNode.parentNode.style.display = "none";
            document.getElementById("PTO_RETISR").value = FormatNumber(0, intNumdecimal, true);
            document.getElementById("PTO_IVA").value = FormatNumber(0, intNumdecimal, true);
            document.getElementById("PTO_NETO").value = FormatNumber(dblSuma, intNumdecimal, true);
        }
    } else {
        document.getElementById("PTO_RETISR").parentNode.parentNode.style.display = "none";
        document.getElementById("PTO_IVA").parentNode.parentNode.style.display = "none";
        document.getElementById("PTO_NETO").parentNode.parentNode.style.display = "none";
    }
}
function SavePtoVtaDo() {
    if (document.getElementById("LBL_FORM_PAGO").value == "") {
        alert("Seleccione un método de pago");
    } else {
        $("#dialogPagos").dialog("close");
        $("#dialogWait").dialog("open");
        var strPOST = "";
        var strPrefijoMaster = "";
        var strPrefijoDeta = "";
        var strKey = "";
        var strNomFormat = "";
        var strTipoDocumentoPto = 0;
        if (document.getElementById("PTO_DOCUMENTO0").checked) {
            strPrefijoMaster = "TKT";
            strPrefijoDeta = "TKTD";
            strKey = "TKT_ID";
            strNomFormat = "TICKET";
            strTipoDocumentoPto = 2;
        } else {
            strPrefijoMaster = "FAC";
            strPrefijoDeta = "FACD";
            strKey = "FAC_ID";
            strNomFormat = "FACTURA";
            strTipoDocumentoPto = 1;
        }
        strPOST += "SC_ID=" + d.getElementById("PTO_SC_ID").value;
        strPOST += "&CT_ID=" + d.getElementById("PTO_CT_ID").value;
        strPOST += "&VE_ID=" + d.getElementById("PTO_VE_ID").value;
        strPOST += "&PD_ID=" + d.getElementById("PD_ID").value;
        strPOST += "&COT_ID=" + d.getElementById("COT_ID").value;
        strPOST += "&" + strPrefijoMaster + "_ESSERV=0";
        strPOST += "&" + strPrefijoMaster + "_MONEDA=" + d.getElementById("PTO_MONEDA").value;
        strPOST += "&" + strPrefijoMaster + "_FECHA=" + d.getElementById("PTO_FECHA").value;
        if (d.getElementById("CC1_ID") != null) {
            strPOST += "&CC1_ID=" + d.getElementById("CC1_ID").value;
        }
        if (strTipoDocumentoPto == "3") {
            if (parseFloat(d.getElementById("PD_ID").value) > 0) {
                strPOST += "&" + strPrefijoMaster + "_FOLIO=" + d.getElementById("FAC_FOLIO").value;
            } else {
                strPOST += "&" + strPrefijoMaster + "_FOLIO=";
            }
        } else {
            strPOST += "&" + strPrefijoMaster + "_FOLIO=";
        }
        strPOST += "&" + strPrefijoMaster + "_NOTAS=" + encodeURIComponent(d.getElementById("PTO_NOTAS").value);
        strPOST += "&" + strPrefijoMaster + "_TOTAL=" + d.getElementById("PTO_TOTAL").value;
        strPOST += "&" + strPrefijoMaster + "_IMPUESTO1=" + d.getElementById("PTO_IMPUESTO1").value;
        strPOST += "&" + strPrefijoMaster + "_IMPUESTO2=" + d.getElementById("PTO_IMPUESTO2").value;
        strPOST += "&" + strPrefijoMaster + "_IMPUESTO3=" + d.getElementById("PTO_IMPUESTO3").value;
        strPOST += "&" + strPrefijoMaster + "_IMPORTE=" + d.getElementById("PTO_IMPORTE").value;
        strPOST += "&" + strPrefijoMaster + "_RETISR=" + d.getElementById("PTO_RETISR").value;
        strPOST += "&" + strPrefijoMaster + "_RETIVA=" + d.getElementById("PTO_IVA").value;
        strPOST += "&" + strPrefijoMaster + "_NETO=" + d.getElementById("PTO_NETO").value;
        strPOST += "&" + strPrefijoMaster + "_NOTASPIE=" + encodeURIComponent(d.getElementById("PTO_NOTASPIE").value);
        strPOST += "&" + strPrefijoMaster + "_REFERENCIA=" + d.getElementById("PTO_REFERENCIA").value;
        strPOST += "&" + strPrefijoMaster + "_CONDPAGO=" + d.getElementById("PTO_CONDPAGO").value;
        strPOST += "&" + strPrefijoMaster + "_METODOPAGO=" + d.getElementById("PTO_METODOPAGO").value;
        strPOST += "&" + strPrefijoMaster + "_NUMCUENTA=" + d.getElementById("PTO_NUMCUENTA").value;
        strPOST += "&" + strPrefijoMaster + "_FORMADEPAGO=" + d.getElementById("PTO_FORMADEPAGO").value;
        strPOST += "&" + strPrefijoMaster + "_NUMPEDI=" + d.getElementById("PTO_NUMPEDI").value;
        strPOST += "&" + strPrefijoMaster + "_FECHAPEDI=" + d.getElementById("PTO_FECHAPEDI").value;
        strPOST += "&" + strPrefijoMaster + "_ADUANA=" + d.getElementById("PTO_ADUANA").value;
        strPOST += "&" + strPrefijoMaster + "_TIPOCOMP=" + d.getElementById("PTO_TIPOCOMP").value;
        strPOST += "&TIPOVENTA=" + strTipoDocumentoPto;
        strPOST += "&" + strPrefijoMaster + "_TASA1=" + dblTasaPto1;
        strPOST += "&" + strPrefijoMaster + "_TASA2=" + dblTasaPto2;
        strPOST += "&" + strPrefijoMaster + "_TASA3=" + dblTasaPto3;
        strPOST += "&" + "TI_ID=" + intIdTasaPto1;
        strPOST += "&" + "TI_ID2=" + intIdTasaPto2;
        strPOST += "&" + "TI_ID3=" + intIdTasaPto3;
        strPOST += "&" + strPrefijoMaster + "_TASAPESO=" + d.getElementById("PTO_TASA_PESO").value;
        strPOST += "&" + strPrefijoMaster + "_DIASCREDITO=" + d.getElementById("PTO_DIASCREDITO").value;
        strPOST += "&" + strPrefijoMaster + "_POR_DESC=" + d.getElementById("CT_DESCUENTO").value;
        strPOST += "&TR_ID=" + d.getElementById("TR_ID").value;
        strPOST += "&ME_ID=" + d.getElementById("ME_ID").value;
        strPOST += "&TF_ID=" + d.getElementById("TF_ID").value;
        if (d.getElementById("CT_DIRENTREGA") != null) {
            strPOST += "&CDE_ID=" + d.getElementById("CT_DIRENTREGA").value;
        }
        if (d.getElementById("CT_CLIENTEFINAL") != null) {
            strPOST += "&DFA_ID=" + d.getElementById("CT_CLIENTEFINAL").value;
        }
        if (d.getElementById("SYC_ID") != null) {
            strPOST += "&SYC_ID=" + d.getElementById("SYC_ID").value;
        }
        if (document.getElementById("FAC_SERIE") != null) {
            strPOST += "&" + strPrefijoMaster + "_SERIE=" + d.getElementById("FAC_SERIE").value;
        }
        strPOST += "&" + strPrefijoMaster + "_NUM_GUIA=" + d.getElementById("PTO_NUM_GUIA").value;
        strPOST += "&" + strPrefijoMaster + "_PUNTOS=" + d.getElementById("PTO_PUNTOS").value;
        strPOST += "&" + strPrefijoMaster + "_NEGOCIO=" + d.getElementById("PTO_NEGOCIO").value;
        if (document.getElementById("PTO_USO_IEPS1").checked) {
            strPOST += "&" + strPrefijoMaster + "_USO_IEPS=1";
            strPOST += "&" + strPrefijoMaster + "_TASA_IEPS=" + d.getElementById("FAC_TASA_IEPS").value;
            strPOST += "&" + strPrefijoMaster + "_IMPORTE_IEPS=" + d.getElementById("FAC_IMPORTE_IEPS").value;
        } else {
            strPOST += "&" + strPrefijoMaster + "_USO_IEPS=0";
            strPOST += "&" + strPrefijoMaster + "_TASA_IEPS=0";
            strPOST += "&" + strPrefijoMaster + "_IMPORTE_IEPS=0";
        }
        if (document.getElementById("FAC_CONSIGNACION1") != null) {
            if (document.getElementById("FAC_CONSIGNACION1").checked) {
                strPOST += "&" + strPrefijoMaster + "_CONSIGNACION=1";
            } else {
                strPOST += "&" + strPrefijoMaster + "_CONSIGNACION=0";
            }
        } else {
            strPOST += "&" + strPrefijoMaster + "_CONSIGNACION=0";
        }
        strPOST += "&ADD_MABE=0";
        strPOST += "&ADD_SANOFI=0";
        strPOST += "&ADD_AMECE=0";
        strPOST += "&ADD_FEMSA=0";
        strPOST += "&" + strPrefijoMaster + "_ESRECU=0";
        strPOST += "&" + strPrefijoMaster + "_PERIODICIDAD=" + d.getElementById("PTO_PERIODICIDAD").value;
        strPOST += "&" + strPrefijoMaster + "_DIAPER=" + d.getElementById("PTO_DIAPER").value;
        strPOST += "&" + strPrefijoMaster + "_NO_EVENTOS=" + d.getElementById("PTO_NO_EVENTOS").value;
        if (document.getElementById("FAC_REGIMENFISCALcount") != null && document.getElementById("FAC_REGIMENFISCALcount") != undefined) {
            var intCuantosReg = document.getElementById("FAC_REGIMENFISCALcount").value;
            if (intCuantosReg > 0) {
                for (var iRegim = 0; iRegim < intCuantosReg; iRegim++) {
                    if (d.getElementById("FAC_REGIMENFISCAL" + iRegim).checked) {
                        strPOST += "&" + strPrefijoMaster + "_REGIMENFISCAL=" + d.getElementById("FAC_REGIMENFISCAL" + iRegim).value;
                    }
                }
            }
        }
        var grid = jQuery("#GR_PTO_VTA");
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
            if (intPreciosconImp == 1) {
                strPOST += "&" + strPrefijoDeta + "_PRECIO" + intC + "=" + lstRow.FACD_PRECIO;
                if (lstRow.FACD_SINPRECIO == 0) {
                    strPOST += "&" + strPrefijoDeta + "_PRECREAL" + intC + "=" + lstRow.FACD_PRECREAL;
                } else {
                    strPOST += "&" + strPrefijoDeta + "_PRECREAL" + intC + "=" + 0;
                }
            } else {
                var dblPrecioConImp = 0;
                var dblPrecioRealConImp = 0;
                if (lstRow.FACD_CANTIDAD > 0) {
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
                    if (parseInt(lstRow.FACD_EXENTO1) == 0) {
                        dblBase1 = lstRow.FACD_PRECIO;
                    }
                    if (parseInt(lstRow.FACD_EXENTO2) == 0) {
                        dblBase2 = lstRow.FACD_PRECIO;
                    }
                    if (parseInt(lstRow.FACD_EXENTO3) == 0) {
                        dblBase3 = lstRow.FACD_PRECIO;
                    }
                    if (parseInt(lstRow.FACD_EXENTO1) == 0) {
                        dblBaseReal1 = lstRow.FACD_PRECREAL;
                    }
                    if (parseInt(lstRow.FACD_EXENTO2) == 0) {
                        dblBaseReal2 = lstRow.FACD_PRECREAL;
                    }
                    if (parseInt(lstRow.FACD_EXENTO3) == 0) {
                        dblBaseReal3 = lstRow.FACD_PRECREAL;
                    }
                    var tax = new Impuestos(dblTasaPto1, dblTasaPto2, dblTasaPto3, intSImpPto1_2, intSImpPto1_3, intSImpPto2_3);
                    tax.CalculaImpuestoMas(dblBase1, dblBase2, dblBase3);
                    var tax2 = new Impuestos(dblTasaPto1, dblTasaPto2, dblTasaPto3, intSImpPto1_2, intSImpPto1_3, intSImpPto2_3);
                    tax2.CalculaImpuestoMas(dblBaseReal1, dblBaseReal2, dblBaseReal3);
                    if (parseInt(lstRow.FACD_EXENTO1) == 0) {
                        dblImpuesto1 = tax.dblImpuesto1;
                    }
                    if (parseInt(lstRow.FACD_EXENTO2) == 0) {
                        dblImpuesto2 = tax.dblImpuesto2;
                    }
                    if (parseInt(lstRow.FACD_EXENTO3) == 0) {
                        dblImpuesto3 = tax.dblImpuesto3;
                    }
                    if (parseInt(lstRow.FACD_EXENTO1) == 0) {
                        dblImpuestoReal1 = tax2.dblImpuesto1;
                    }
                    if (parseInt(lstRow.FACD_EXENTO2) == 0) {
                        dblImpuestoReal2 = tax2.dblImpuesto2;
                    }
                    if (parseInt(lstRow.FACD_EXENTO3) == 0) {
                        dblImpuestoReal3 = tax2.dblImpuesto3;
                    }
                    dblPrecioConImp = (parseFloat(lstRow.FACD_PRECIO) + dblImpuesto1 + dblImpuesto2 + dblImpuesto3);
                    if (lstRow.FACD_SINPRECIO == 0) {
                        dblPrecioRealConImp = (parseFloat(lstRow.FACD_PRECREAL) + dblImpuestoReal1 + dblImpuestoReal2 + dblImpuestoReal3);
                    } else {
                        dblPrecioRealConImp = (parseFloat(lstRow.FACD_PRECIO) + dblImpuesto1 + dblImpuesto2 + dblImpuesto3);
                        lstRow.FACD_IMPORTEREAL = dblPrecioRealConImp * lstRow.FACD_CANTIDAD;
                    }
                } else {
                    dblPrecioConImp = (parseFloat(lstRow.FACD_PRECIO));
                    dblPrecioRealConImp = (parseFloat(lstRow.FACD_PRECREAL));
                }
                strPOST += "&" + strPrefijoDeta + "_PRECIO" + intC + "=" + dblPrecioConImp;
                if (lstRow.FACD_SINPRECIO == 0) {
                    strPOST += "&" + strPrefijoDeta + "_PRECREAL" + intC + "=" + dblPrecioRealConImp;
                } else {
                    strPOST += "&" + strPrefijoDeta + "_PRECREAL" + intC + "=" + 0;
                }
            }
            strPOST += "&" + strPrefijoDeta + "_IMPORTE" + intC + "=" + lstRow.FACD_IMPORTE;
            strPOST += "&" + strPrefijoDeta + "_TASAIVA1" + intC + "=" + lstRow.FACD_TASAIVA1;
            strPOST += "&" + strPrefijoDeta + "_TASAIVA2" + intC + "=0" + lstRow.FACD_TASAIVA2;
            strPOST += "&" + strPrefijoDeta + "_TASAIVA3" + intC + "=" + lstRow.FACD_TASAIVA3;
            strPOST += "&" + strPrefijoDeta + "_IMPUESTO1" + intC + "=" + lstRow.FACD_IMPUESTO1;
            strPOST += "&" + strPrefijoDeta + "_IMPUESTO2" + intC + "=" + lstRow.FACD_IMPUESTO2;
            strPOST += "&" + strPrefijoDeta + "_IMPUESTO3" + intC + "=" + lstRow.FACD_IMPUESTO3;
            strPOST += "&" + strPrefijoDeta + "_ESREGALO" + intC + "=" + lstRow.FACD_ESREGALO;
            strPOST += "&" + strPrefijoDeta + "_NOSERIE" + intC + "=" + lstRow.FACD_NOSERIE;
            strPOST += "&" + strPrefijoDeta + "_IMPORTEREAL" + intC + "=" + lstRow.FACD_IMPORTEREAL;
            strPOST += "&" + strPrefijoDeta + "_DESCUENTO" + intC + "=" + lstRow.FACD_DESCUENTO;
            strPOST += "&" + strPrefijoDeta + "_PORDESC" + intC + "=" + lstRow.FACD_PORDESC;
            strPOST += "&" + strPrefijoDeta + "_ESDEVO" + intC + "=" + lstRow.FACD_ESDEVO;
            strPOST += "&" + strPrefijoDeta + "_PRECFIJO" + intC + "=" + lstRow.FACD_PRECFIJO;
            strPOST += "&" + strPrefijoDeta + "_NOTAS" + intC + "=" + encodeURIComponent(lstRow.FACD_NOTAS);
            strPOST += "&" + strPrefijoDeta + "_UNIDAD_MEDIDA" + intC + "=" + lstRow.FACD_UNIDAD_MEDIDA;
        }
        strPOST += "&COUNT_ITEM=" + intC;
        if (document.getElementById("ES_MULTIPLE_PTO_VTA").value == 1) {
            var grid = jQuery("#gr_multipago");
            var idArr = grid.getDataIDs();
            if (idArr.length != 0) {
                strPOST += "&COUNT_PAGOS=" + idArr.length;
                for (var i = 0; i < idArr.length; i++) {
                    var id = idArr[i];
                    var lstRow = grid.getRowData(id);
                    if (lstRow.gr_mlt_SubTipo == "EFECTIVO") {
                        strPOST += "&MCD_MONEDA" + (i + 1) + "=1";
                        strPOST += "&MCD_FOLIO=" + (i + 1) + "";
                        strPOST += "&MCD_FORMAPAGO" + (i + 1) + "=EFECTIVO";
                        strPOST += "&MCD_NOCHEQUE" + (i + 1) + "=";
                        strPOST += "&MCD_BANCO" + (i + 1) + "=";
                        strPOST += "&MCD_NOTARJETA" + (i + 1) + "=";
                        strPOST += "&MCD_TIPOTARJETA" + (i + 1) + "=";
                        strPOST += "&MCD_IMPORTE" + (i + 1) + "=" + lstRow.gr_mlt_valorMXN;
                        strPOST += "&MCD_TASAPESO" + (i + 1) + "=1";
                        strPOST += "&MCD_CAMBIO" + (i + 1) + "=" + d.getElementById("diferencia_MXN").value;
                    } else {
                        if (lstRow.gr_mlt_SubTipo == "CHEQUE") {
                            strPOST += "&MCD_MONEDA" + (i + 1) + "=1";
                            strPOST += "&MCD_FOLIO" + (i + 1) + "=";
                            strPOST += "&MCD_FORMAPAGO" + (i + 1) + "=CHEQUE";
                            strPOST += "&MCD_NOCHEQUE" + (i + 1) + "=" + lstRow.gr_mlt_cheque;
                            strPOST += "&MCD_BANCO" + (i + 1) + "=" + lstRow.gr_mlt_banco;
                            strPOST += "&MCD_NOTARJETA" + (i + 1) + "=";
                            strPOST += "&MCD_TIPOTARJETA" + (i + 1) + "=";
                            strPOST += "&MCD_IMPORTE" + (i + 1) + "=" + lstRow.gr_mlt_valorMXN;
                            strPOST += "&MCD_TASAPESO" + (i + 1) + "=1";
                            strPOST += "&MCD_CAMBIO" + (i + 1) + "=0";
                        } else {
                            if (lstRow.gr_mlt_SubTipo == "AMERICAN EXPRESS" || strTipoPago == "T/CRÉDITO" || strTipoPago == "T/DÉBITO" || strTipoPago == "T/AMEX") {
                                strPOST += "&MCD_MONEDA" + (i + 1) + "=1";
                                strPOST += "&MCD_FOLIO" + (i + 1) + "=";
                                strPOST += "&MCD_FORMAPAGO" + (i + 1) + "=TCREDITO";
                                strPOST += "&MCD_NOCHEQUE" + (i + 1) + "=";
                                strPOST += "&MCD_BANCO" + (i + 1) + "=";
                                strPOST += "&MCD_NOTARJETA" + (i + 1) + "=" + lstRow.gr_mlt_lote;
                                strPOST += "&MCD_TIPOTARJETA" + (i + 1) + "=" + lstRow.gr_mlt_banco;
                                strPOST += "&MCD_IMPORTE" + (i + 1) + "=" + lstRow.gr_mlt_valorMXN;
                                strPOST += "&MCD_TASAPESO" + (i + 1) + "=1";
                                strPOST += "&MCD_CAMBIO" + (i + 1) + "=0";
                            }
                        }
                    }
                }
            }
        } else {
            strPOST += "&COUNT_PAGOS=1";
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
        $.ajax({type: "POST", data: encodeURI(strPOST), scriptCharset: "utf-8", contentType: "application/x-www-form-urlencoded;charset=utf-8", cache: false, dataType: "html", url: "VtasMov.do?id=1", success: function (dato) {
                dato = trim(dato);
                if (Left(dato, 3) == "OK.") {
                    if (strNomFormat == "FACTURA") {
                        if (intImprimeTicket == 1) {
                            ImprimeconFolioTicketPtoVta(dato.replace("OK.", ""));
                        } else {
                            var strHtml = CreaHidden(strKey, dato.replace("OK.", ""));
                            openWhereverFormat("ERP_SendInvoice?id=" + dato.replace("OK.", ""), strNomFormat, "PDF", strHtml);
                        }
                        ResetOperaActualPtoVta();
                    } else {
                        var bolImprimeDoc1 = true;
                        if (strTipoDocumentoPto == 3) {
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
                                        $.ajax({type: "POST", data: encodeURI("LST_PD_ID=" + document.getElementById("PD_ID").value + strFiltro), scriptCharset: "utf-8", contentType: "application/x-www-form-urlencoded;charset=utf-8", cache: false, dataType: "html", url: "ERP_FacRecu.jsp?id=1", success: function (dato) {
                                                dato = trim(dato);
                                                if (Left(dato, 3) == "OK.") {
                                                    alert(lstMsg[59] + " " + dato.replace("OK.", ""));
                                                } else {
                                                    alert(dato);
                                                }
                                                ResetOperaActualPtoVta();
                                                $("#dialogWait").dialog("close");
                                            }, error: function (objeto, quepaso, otroobj) {
                                                alert(":pto9:" + objeto + " " + quepaso + " " + otroobj);
                                                $("#dialogWait").dialog("close");
                                            }});
                                    }
                                }
                            }
                        }
                        if (bolImprimeDoc1) {
                            if (intImprimeTicket == 1) {
                                ImprimeconFolioTicketPtoVta(dato.replace("OK.", ""));
                            } else {
                                ImprimeconFolioPtoVta(strKey, dato, strNomFormat);
                            }
                            ResetOperaActualPtoVta();
                        }
                    }
                } else {
                    alert(dato);
                }
                ResetOperaActualPtoVta();
                $("#dialogWait").dialog("close");
            }, error: function (objeto, quepaso, otroobj) {
                alert(":pto9:" + objeto + " " + quepaso + " " + otroobj);
                $("#dialogWait").dialog("close");
            }});
    }
}
function ImprimeconFolioPtoVta(strKey, dato, strNomFormat) {
    var strTipo = "";
    if (d.getElementById("PTO_DOCUMENTO0").checked) {
        strTipo = "2";
    }
    if (d.getElementById("PTO_DOCUMENTO1").checked) {
        strTipo = "1";
    }
    $.ajax({type: "POST", data: "KEY_ID=" + strKey + "&TYPE_ID=" + strTipo, scriptCharset: "utf-8", contentType: "application/x-www-form-urlencoded;charset=utf-8", cache: false, dataType: "xml", url: "VtasMov.do?id=12", success: function (datos) {
            var objsc = datos.getElementsByTagName("vta_folios")[0];
            var strFolioT = objsc.getAttribute("FOLIO");
            var strHtml2 = CreaHidden(strKey, dato.replace("OK.", ""));
            openFormat(strNomFormat, "PDF", strHtml2, strFolioT);
        }, error: function (objeto, quepaso, otroobj) {
            alert(":ImprimeFolio:" + objeto + " " + quepaso + " " + otroobj);
        }});
}
function ImprimeconFolioTicketPtoVta(strKey) {
    var strNomFormat = "TICKET1";
    var intOp = 4;
    if (d.getElementById("FAC_TIPO").value == "1") {
        strNomFormat = "FACTURA1";
        intOp = 5;
    }
    if (d.getElementById("FAC_TIPO").value == "3") {
        strNomFormat = "PEDIDO1";
        intOp = 6;
    }
    $.ajax({type: "POST", data: "ID=" + strKey + "&NOM_FORMATO=" + strNomFormat, scriptCharset: "utf-8", contentType: "application/x-www-form-urlencoded;charset=utf-8", cache: false, dataType: "html", url: "ERP_Varios.jsp?id=" + intOp, success: function (datos) {
            var miapplet = document.getElementById("PrintTickets");
            miapplet.DoImpresion(datos, strCodEscape, strImpresora);
        }, error: function (objeto, quepaso, otroobj) {
            alert(":ImprimeFolio:" + objeto + " " + quepaso + " " + otroobj);
        }});
}
function ObtenNomCtePtoVta() {
    var intCte = document.getElementById("PTO_CT_ID").value;
    $.ajax({type: "POST", data: "CT_ID=" + intCte, scriptCharset: "utf-8", contentType: "application/x-www-form-urlencoded;charset=utf-8", cache: false, dataType: "xml", url: "VtasMov.do?id=9", success: function (datoVal) {
            var objCte = datoVal.getElementsByTagName("vta_clientes")[0];
            if (objCte.getAttribute("CT_ID") == 0) {
                document.getElementById("PTO_CLIENTE").value = "***************";
            } else {
                document.getElementById("PTO_CLIENTE").value = objCte.getAttribute("CT_RAZONSOCIAL");
                document.getElementById("PTO_CTE_RFC").value = objCte.getAttribute("CT_RFC");
                document.getElementById("PTO_LPRECIOS").value = objCte.getAttribute("CT_LPRECIOS");
                document.getElementById("PTO_DIASCREDITO").value = objCte.getAttribute("CT_DIASCREDITO");
                document.getElementById("PTO_TASA_PESO").value = objCte.getAttribute("TTC_ID");
                document.getElementById("PTO_METODOPAGO").value = objCte.getAttribute("CT_METODODEPAGO");
                document.getElementById("PTO_FORMADEPAGO").value = objCte.getAttribute("CT_FORMADEPAGO");
                document.getElementById("PTO_NUMCUENTA").value = objCte.getAttribute("CT_CTABANCO1");
                document.getElementById("PTO_MONEDA").value = objCte.getAttribute("MON_ID");
                document.getElementById("PTO_DIRECCION_CTE").value = objCte.getAttribute("CT_DIRECCION");
            }
        }, error: function (objeto, quepaso, otroobj) {
            document.getElementById("PTO_CLIENTE").value = "***************";
            ValidaShow("PTO_CLIENTE", lstMsg[28]);
            alert(":pto:" + objeto + " " + quepaso + " " + otroobj);
        }});
}
function ResetOperaActualPtoVta() {
    jQuery("#GR_PTO_VTA").clearGridData();
    sumTotal();
    closeCobro();
    setFolioPtoVta();
    closeCobroMtl();
}
function setFolioPtoVta() {
    $("#dialogWait").dialog("open");
    var strPost = "";
    if (d.getElementById("PTO_DOCUMENTO0").checked) {
        strPost = "TYPE_ID=2";
    }
    if (d.getElementById("PTO_DOCUMENTO1").checked) {
        strPost = "TYPE_ID=1";
    }
    $.ajax({type: "POST", data: strPost, scriptCharset: "utf-8", contentType: "application/x-www-form-urlencoded;charset=utf-8", cache: false, dataType: "xml", url: "VtasMov.do?id=14", success: function (datos) {
            var objsc = datos.getElementsByTagName("vta_folios")[0];
            var strFolioT = objsc.getAttribute("FOLIO");
            document.getElementById("FOLIO_FAC").value = strFolioT;
            $("#dialogWait").dialog("close");
        }, error: function (objeto, quepaso, otroobj) {
            alert(":Obten folio siguiente:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }});
}
function TipoCambioPtoVta() {
    $("#dialogWait").dialog("open");
    var intMonedaBanco = 1;
    var intMonedaSeleccionada = 2;
    var strPOST = "&Moneda_1=" + intMonedaBanco;
    strPOST += "&Moneda_2=" + intMonedaSeleccionada;
    $.ajax({type: "POST", data: strPOST, scriptCharset: "utf-8", contentType: "application/x-www-form-urlencoded;charset=utf-8", cache: false, dataType: "xml", url: "ERP_Combo.jsp?id=3", success: function (datos) {
            var objsc = datos.getElementsByTagName("TasaCambio")[0];
            var lstTiks = objsc.getElementsByTagName("TasaCambios");
            var obj = lstTiks[0];
            var dblUSD = parseFloat(obj.getAttribute("USD"));
            document.getElementById("PTO_TIPO_CAMBIO").value = dblUSD;
            $("#dialogWait").dialog("close");
        }, error: function (objeto, quepaso, otroobj) {
            alert(":ptoExist:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }});
}
function ObtieneProductoPtoVta(strcodProd) {
    var strPrefijoMaster = "";
    var strPrefijoDeta = "";
    var strKey = "";
    var strNomFormat = "";
    var strTipoDocumentoPto = 0;
    if (document.getElementById("PTO_DOCUMENTO0").checked) {
        strPrefijoMaster = "TKT";
        strPrefijoDeta = "TKTD";
        strKey = "TKT_ID";
        strNomFormat = "TICKET";
        strTipoDocumentoPto = 2;
    } else {
        strPrefijoMaster = "FAC";
        strPrefijoDeta = "FACD";
        strKey = "FAC_ID";
        strNomFormat = "FACTURA";
        strTipoDocumentoPto = 1;
    }
    var strCod = UCase(strcodProd);
    if (trim(strCod) != "") {
        var intDevo = 0;
        var bolAgrupa = true;
        $("#dialogWait").dialog("open");
        var bolNvo = true;
        var idProd = 0;
        if (bolAgrupa) {
            var grid = jQuery("#GR_PTO_VTA");
            var arr = grid.getDataIDs();
            for (var i = 0; i < arr.length; i++) {
                var id = arr[i];
                var lstRowAct = grid.getRowData(id);
                if (lstRowAct.FACD_CVE == strCod || lstRowAct.FACD_CODBARRAS == strCod) {
                    if (intDevo == 1) {
                        if (lstRow.FACD_ESDEVO == 1) {
                            idProd = id;
                            bolNvo = false;
                            break;
                        }
                    } else {
                        idProd = id;
                        bolNvo = false;
                        break;
                    }
                }
            }
        }
        if (bolNvo) {
            $.ajax({type: "POST", data: encodeURI("PR_CODIGO=" + strCod + "&SC_ID=" + d.getElementById("PTO_SC_ID").value + "&EMP_ID=" + d.getElementById("EMP_ID").value), scriptCharset: "utf-8", contentType: "application/x-www-form-urlencoded;charset=utf-8", cache: false, dataType: "xml", url: "ERP_PedidosMakProcs.jsp?id=10", success: function (datoVal) {
                    var objProd = datoVal.getElementsByTagName("vta_productos")[0];
                    var Pr_Id = 0;
                    if (objProd != undefined) {
                        Pr_Id = objProd.getAttribute("PR_ID");
                        d.getElementById("PTO_PR_DESC").value = objProd.getAttribute("PR_DESCRIPCION");
                        $("#dialogWait").dialog("close");
                        if (Pr_Id != 0) {
                            strCod = objProd.getAttribute("PR_CODIGO");
                        }
                    }
                    var Ct_Id = d.getElementById("PTO_CT_ID").value;
                    var dblCantidad = 1;
                    if (intDevo == 1) {
                        dblCantidad = 0;
                    }
                    if (Pr_Id != 0) {
                        var dblExistencia = objProd.getAttribute("PR_EXISTENCIA");
                        if (objProd.getAttribute("PR_REQEXIST") == 1 && strTipoDocumentoPto != 3 && strTipoDocumentoPto != 5) {
                            $.ajax({type: "POST", data: "PR_ID=" + Pr_Id, scriptCharset: "utf-8", contentType: "application/x-www-form-urlencoded;charset=utf-8", cache: false, dataType: "html", url: "InvMov.do?id=1", success: function (datoExist) {
                                    dblExistencia = parseFloat(datoExist);
                                    if (parseFloat(dblCantidad) > dblExistencia) {
                                        alert(lstMsg[3] + "(" + dblCantidad + ") " + lstMsg[34] + strCod + "(" + dblExistencia + ") " + lstMsg[4]);
                                        OpnOpt("BACKORDER_MAK", "_ed", "dialog2", false, false, true);
                                    } else {
                                        if (objProd.getAttribute("PR_USO_NOSERIE") == 1) {
                                        } else {
                                            AddProdPrec(objProd, Pr_Id, Ct_Id, dblCantidad, strCod, dblExistencia, intDevo);
                                        }
                                    }
                                }, error: function (objeto, quepaso, otroobj) {
                                    alert(":ptoExist:" + objeto + " " + quepaso + " " + otroobj);
                                    $("#dialogWait").dialog("close");
                                }});
                        } else {
                            AddProdPrec(objProd, Pr_Id, Ct_Id, dblCantidad, strCod, dblExistencia, intDevo);
                        }
                    } else {
                        var objSecModiVta = objMap.getScreen("PRODUCTOS_MAK");
                        if (objSecModiVta != null) {
                            objSecModiVta.bolActivo = false;
                            objSecModiVta.bolMain = false;
                            objSecModiVta.bolInit = false;
                            objSecModiVta.idOperAct = 0;
                        }
                        OpnOpt("PRODUCTOS_MAK", "_ed", "dialog", false, false, true);
                    }
                }, error: function (objeto, quepaso, otroobj) {
                    alert(":pto3:" + objeto + " " + quepaso + " " + otroobj);
                    $("#dialogWait").dialog("close");
                }});
        } else {
            var Cantidad = 1;
            var gridD = jQuery("#GR_PTO_VTA");
            var lstRow = gridD.getRowData(idProd);
            if (lstRow.FACD_USA_SERIE == 1) {
            } else {
                lstRow.FACD_CANTIDAD = parseFloat(lstRow.FACD_CANTIDAD) + parseFloat(Cantidad);
                if (lstRow.FACD_REQEXIST == 1 && document.getElementById("FAC_TIPO").value != 3) {
                    if (parseFloat(lstRow.FACD_CANTIDAD) > parseFloat(lstRow.FACD_EXIST)) {
                        alert(lstMsg[3] + " " + lstRow.FACD_CVE + " " + lstMsg[4]);
                        if (parseFloat(lstRow.FACD_EXIST) > 0) {
                            lstRow.FACD_CANTIDAD = lstRow.FACD_EXIST;
                        } else {
                            lstRow.FACD_CANTIDAD = 0;
                        }
                    }
                }
                lstRowChangePrecioPediMak(lstRow, idProd, gridD);
                document.getElementById("PTO_PR_CODIGO").value = "";
                document.getElementById("PTO_PR_CODIGO").focus();
                $("#dialogWait").dialog("close");
            }
        }
    }
}
function _validaCierreCaja() {
    var strRespuesta = validaMontos();
    if (strRespuesta != "") {
        alert(strRespuesta);
        $("#dialog").dialog("open");
    }
}
function changeMultipago() {
    closeCobro();
    var objMainFacPedi = objMap.getScreen("pago_mult");
    if (objMainFacPedi != null) {
        objMainFacPedi.bolActivo = false;
        objMainFacPedi.bolMain = false;
        objMainFacPedi.bolInit = false;
        objMainFacPedi.idOperAct = 0;
    }
    OpnOpt("pago_mult", "_ed", "dialog2", false, false, true);
}
function initMultipago() {
    document.getElementById("ES_MULTIPLE_PTO_VTA").value = 1;
    $("#dialogWait").dialog("open");
    var strPost = "";
    $.ajax({type: "POST", data: strPost, scriptCharset: "utf-8", contentType: "application/x-www-form-urlencoded;charset=utf-8", cache: false, dataType: "xml", url: "ERP_Pto_Venta.jsp?id=2", success: function (datos) {
            var strFormPago = "";
            var objofrt = datos.getElementsByTagName("Formas_Pago")[0];
            var lstProms = objofrt.getElementsByTagName("datos");
            for (var i = 0; i < lstProms.length; i++) {
                var obj = lstProms[i];
                strFormPago += '<input type="button" style="font-size:11px" onclick="setMPagoMult(\'' + obj.getAttribute("Descripcion") + '\');" value="' + obj.getAttribute("Descripcion") + '" id="' + obj.getAttribute("Descripcion") + '" name="' + obj.getAttribute("Descripcion") + '">';
            }
            document.getElementById("text_empty1").innerHTML = strFormPago;
            $("#dialogWait").dialog("close");
        }, error: function () {
            alert("Error Datos de Usuario!");
            $("#dialogWait").dialog("close");
        }});
    $("#dialogWait").dialog("open");
    $.ajax({type: "POST", data: strPost, scriptCharset: "utf-8", contentType: "application/x-www-form-urlencoded;charset=utf-8", cache: false, dataType: "xml", url: "ERP_Pto_Venta.jsp?id=1", success: function (datos) {
            var objofrt = datos.getElementsByTagName("Datos_User")[0];
            var lstProms = objofrt.getElementsByTagName("datos");
            for (var i = 0; i < lstProms.length; i++) {
                var obj = lstProms[i];
                document.getElementById("txt_Caja").value = obj.getAttribute("SC_CAJA");
                document.getElementById("txt_Usuario").value = obj.getAttribute("NOMBRE_USER");
            }
            $("#dialogWait").dialog("close");
        }, error: function () {
            alert("Error Datos de Usuario!");
            $("#dialogWait").dialog("close");
        }});
    document.getElementById("total_USD").value = document.getElementById("PTO_TASA_USD").value;
    document.getElementById("total_MXN").value = document.getElementById("PTO_TASA_MNX").value;
    var strHtmlOut = "<table>" + "<tr>" + '<td ><i class = "fa fa-print" style="font-size:20px" onclick="SavePtoVtaDo();">Imprimir</i></td>' + "<td>&nbsp;&nbsp;&nbsp;</td>" + '<td ><i class = "fa fa-minus-square" style="font-size:20px" onclick="closeCobroMtl();">Cancelar</i></td>' + "<td>&nbsp;&nbsp;&nbsp;</td>" + '<td ><i class = "fa fa-check" style="font-size:20px" onclick="SavePtoVtaDo();">Finalizar</i></td>' + "<td>&nbsp;&nbsp;&nbsp;</td>" + "</tr>" + "</table>";
    document.getElementById("btn_finMultipago").innerHTML = strHtmlOut;
    setMPagoMult("EFECTIVO");
    _resetPMltp();
}
function TasaCambioPago() {
    $("#dialogWait").dialog("open");
    var intMonedaBanco = 1;
    var intMonedaSeleccionada = 1;
    var strPOST = "&Moneda_1=" + intMonedaBanco;
    strPOST += "&Moneda_2=" + intMonedaSeleccionada;
    $.ajax({type: "POST", data: strPOST, scriptCharset: "utf-8", contentType: "application/x-www-form-urlencoded;charset=utf-8", cache: false, dataType: "xml", url: "ERP_Combo.jsp?id=3", success: function (datos) {
            var objsc = datos.getElementsByTagName("TasaCambio")[0];
            var lstTiks = objsc.getElementsByTagName("TasaCambios");
            var obj = lstTiks[0];
            var dblUSD = parseFloat(obj.getAttribute("USD"));
            document.getElementById("PTO_VTA_USD").value = dblUSD;
            $("#dialogWait").dialog("close");
        }, error: function (objeto, quepaso, otroobj) {
            alert(":ptoExist:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }});
}
function clalcTasasPtoVta() {
    var tmpMPX = parseFloat(document.getElementById("PTO_TOTAL").value);
    var tmpUSD = parseFloat(document.getElementById("PTO_VTA_USD").value);
    tmpUSD = tmpMPX / tmpUSD;
    document.getElementById("PTO_TASA_USD").value = parseFloat(tmpUSD);
    document.getElementById("PTO_TASA_MNX").value = parseFloat(tmpMPX);
}
function returnPUnicoPtoVta() {
    $("#dialog2").dialog("close");
    opnCobro();
    document.getElementById("ES_MULTIPLE_PTO_VTA").value = 0;
}
function _drawFieldsMltp() {
    document.getElementById("EF_MULT").parentNode.parentNode.style.display = "none";
    document.getElementById("EF_MULT_CAMBIO").parentNode.parentNode.style.display = "none";
    document.getElementById("CHE_MUTL_IMP").parentNode.parentNode.style.display = "none";
    document.getElementById("CHE_MUTL_NUM").parentNode.parentNode.style.display = "none";
    document.getElementById("CHE_MUTL_BCO").parentNode.parentNode.style.display = "none";
    document.getElementById("TJ_MUTL_IMP").parentNode.parentNode.style.display = "none";
    document.getElementById("TJ_MUTL_NUM").parentNode.parentNode.style.display = "none";
    document.getElementById("TJ_MUTL_TIPO").parentNode.parentNode.style.display = "none";
    var strTipoPago = document.getElementById("text_empty2").value;
    if (strTipoPago == "EFECTIVO") {
        document.getElementById("EF_MULT").parentNode.parentNode.style.display = "";
        document.getElementById("EF_MULT_CAMBIO").parentNode.parentNode.style.display = "";
    } else {
        if (strTipoPago == "CHEQUE") {
            document.getElementById("CHE_MUTL_IMP").parentNode.parentNode.style.display = "";
            document.getElementById("CHE_MUTL_NUM").parentNode.parentNode.style.display = "";
            document.getElementById("CHE_MUTL_BCO").parentNode.parentNode.style.display = "";
        } else {
            if (strTipoPago == "AMERICAN EXPRESS" || strTipoPago == "T/CRÉDITO" || strTipoPago == "T/DÉBITO" || strTipoPago == "T/AMEX") {
                document.getElementById("TJ_MUTL_IMP").parentNode.parentNode.style.display = "";
                document.getElementById("TJ_MUTL_NUM").parentNode.parentNode.style.display = "";
                document.getElementById("TJ_MUTL_TIPO").parentNode.parentNode.style.display = "";
            }
        }
    }
}
function addGrMultiple() {
    var itemIdCob = 0;
    var RowMltp;
    var dblTasaUSD = parseFloat(document.getElementById("PTO_VTA_USD").value);
    var dblImporteUSD = 0;
    var strFecha = document.getElementById("PTO_FECHA").value;
    var strTipoPago = document.getElementById("text_empty2").value;
    if (strTipoPago == "EFECTIVO") {
        dblImporteUSD = document.getElementById("EF_MULT").value / dblTasaUSD;
        RowMltp = {gr_mlt_SubTipo: strTipoPago, gr_mlt_MON_ID: "PESOS", gr_mlt_valorUSD: dblImporteUSD, gr_mlt_cheque: "", gr_mlt_lote: "", gr_mlt_banco: "", gr_mlt_fecha: "", gr_mlt_codigo: "", gr_mlt_memo: "", gr_mlt_plazo: "", gr_mlt_valorMXN: document.getElementById("EF_MULT").value};
        itemIdCob++;
        jQuery("#gr_multipago").addRowData(itemIdCob, RowMltp, "last");
    } else {
        if (strTipoPago == "CHEQUE") {
            dblImporteUSD = document.getElementById("CHE_MUTL_IMP").value / dblTasaUSD;
            var ImpChe = document.getElementById("CHE_MUTL_IMP").value;
            var NumChe = document.getElementById("CHE_MUTL_NUM").value;
            var BcoChe = document.getElementById("CHE_MUTL_BCO").value;
            RowMltp = {gr_mlt_SubTipo: strTipoPago, gr_mlt_MON_ID: "PESOS", gr_mlt_valorUSD: dblImporteUSD, gr_mlt_cheque: NumChe, gr_mlt_lote: "", gr_mlt_banco: BcoChe, gr_mlt_fecha: strFecha, gr_mlt_codigo: "", gr_mlt_memo: "", gr_mlt_plazo: "", gr_mlt_valorMXN: document.getElementById("CHE_MUTL_IMP").value};
            itemIdCob++;
            jQuery("#gr_multipago").addRowData(itemIdCob, RowMltp, "last");
        } else {
            if (strTipoPago == "AMERICAN EXPRESS" || strTipoPago == "T/CRÉDITO" || strTipoPago == "T/DÉBITO" || strTipoPago == "T/AMEX") {
                dblImporteUSD = document.getElementById("TJ_MUTL_IMP").value / dblTasaUSD;
                RowMltp = {gr_mlt_SubTipo: strTipoPago, gr_mlt_MON_ID: "PESOS", gr_mlt_valorUSD: dblImporteUSD, gr_mlt_cheque: "", gr_mlt_lote: document.getElementById("TJ_MUTL_NUM").value, gr_mlt_banco: document.getElementById("TJ_MUTL_TIPO").value, gr_mlt_fecha: strFecha, gr_mlt_codigo: "", gr_mlt_memo: "", gr_mlt_plazo: "", gr_mlt_valorMXN: document.getElementById("TJ_MUTL_IMP").value};
                itemIdCob++;
                jQuery("#gr_multipago").addRowData(itemIdCob, RowMltp, "last");
            }
        }
    }
    _resetPMltp();
    _CalculaMltp();
}
function _resetPMltp() {
    document.getElementById("EF_MULT").value = 0;
    document.getElementById("EF_MULT_CAMBIO").value = 0;
    document.getElementById("CHE_MUTL_IMP").value = 0;
    document.getElementById("CHE_MUTL_NUM").value = "";
    document.getElementById("CHE_MUTL_BCO").value = 0;
    document.getElementById("TJ_MUTL_IMP").value = 0;
    document.getElementById("TJ_MUTL_NUM").value = "";
    document.getElementById("TJ_MUTL_TIPO").value = 0;
}
function _CalculaMltp() {
    var dblTmpTotalMXN = 0;
    var dblTmpTotalUSD = 0;
    var grid = jQuery("#gr_multipago");
    var idArr = grid.getDataIDs();
    if (idArr.length != 0) {
        for (var i = 0; i < idArr.length; i++) {
            var id = idArr[i];
            var lstRow = grid.getRowData(id);
            dblTmpTotalMXN = dblTmpTotalMXN + parseFloat(lstRow.gr_mlt_valorMXN);
            dblTmpTotalUSD = dblTmpTotalUSD + parseFloat(lstRow.gr_mlt_valorUSD);
        }
    }
    var dblRestoUSD = document.getElementById("total_USD").value - dblTmpTotalUSD;
    var dblRestoMXN = document.getElementById("total_MXN").value - dblTmpTotalMXN;
    document.getElementById("pagado_USD").value = dblTmpTotalUSD;
    document.getElementById("pagado_MXN").value = dblTmpTotalMXN;
    document.getElementById("diferencia_USD").value = dblRestoUSD;
    document.getElementById("diferencia_MXN").value = dblRestoMXN;
}