function vta_view() {

}
function ViewOpera() {

}
var bolAnularVta = false;
var bolSoyMain = false;
var strNomMain = false;
function InitViewV() {
    try {
        document.getElementById("btn1").style.display = "none";
    } catch (err) {
    }
    strNomMain = objMap.getNomMain();
    if (strNomMain == "VTAS_VIEW") {
        bolSoyMain = true;
    } else {
        if (strNomMain == "C_TELEM") {
            document.getElementById("VIEW_CLIENTE").value = document.getElementById("CT_NO_CLIENTE").value;
        }
    }
    ActivaButtons(false, false, false, false, false, false, !bolSoyMain, false, false, false);
    $("#dialogWait").dialog("open");
    $.ajax({type: "POST", data: "keys=98", scriptCharset: "utf-8", contentType: "application/x-www-form-urlencoded;charset=utf-8", cache: false, dataType: "xml", url: "Acceso.do", success: function (datos) {
            var objsc = datos.getElementsByTagName("Access")[0];
            var lstKeys = objsc.getElementsByTagName("key");
            for (i = 0; i < lstKeys.length; i++) {
                var obj = lstKeys[i];
                if (obj.getAttribute("id") == 98 && obj.getAttribute("enabled") == "true") {
                    bolAnularVta = true;
                }
            }
            $("#dialogWait").dialog("close");
        }, error: function (objeto, quepaso, otroobj) {
            alert(":pto:" + objeto + " " + quepaso + " " + otroobj);
        }});
}
var strNomFormView = "";
var strKeyView = "";
var strNomFormatPrint = "";
var strTipoVtaView = "";
var strNomOrderView = "";
function ViewDo() {
    bolSoyMain = false;
    strNomMain = objMap.getNomMain();
    if (strNomMain == "VTAS_VIEW") {
        bolSoyMain = true;
    }
    var bolPasa = true;
    ValidaClean("VIEW_TIPO");
    if (document.getElementById("VIEW_TIPO").value == "" || document.getElementById("VIEW_TIPO").value == "0") {
        ValidaShow("VIEW_TIPO", "NECESITA SELECCIONAR EL TIPO DE VENTA");
        bolPasa = false;
    }
    if (bolPasa) {
        ActivaButtons(bolAnularVta, true, false, false, false, false, !bolSoyMain, true, false, false);
        var strPrefijoMaster = "TKT";
        strNomOrderView = "TKT_FECHA";
        strNomFormView = "TICKETVIEW";
        strKeyView = "TKT_ID";
        strNomFormatPrint = "TICKET";
        strTipoVtaView = "2";
        if (document.getElementById("VIEW_TIPO").value == "1") {
            strPrefijoMaster = "FAC";
            strNomFormView = "FACTVIE";
            strNomOrderView = "FAC_FECHA";
            strKeyView = "FAC_ID";
            strNomFormatPrint = "FACTURA";
            strTipoVtaView = "1";
            ActivaButtons(bolAnularVta, true, true, false, false, false, !bolSoyMain, true, false, false);
        }
        if (document.getElementById("VIEW_TIPO").value == "3") {
            strPrefijoMaster = "PD";
            strNomFormView = "PEDIDOVIEW";
            strNomOrderView = "PD_ID";
            strKeyView = "PD_ID";
            strNomFormatPrint = "PEDIDO";
            strTipoVtaView = "3";
            if (bolSoyMain) {
                ActivaButtons(bolAnularVta, true, false, true, true, false, !bolSoyMain, true, true, false);
            } else {
                ActivaButtons(bolAnularVta, true, false, true, true, true, !bolSoyMain, true, true, false);
            }
        }
        if (document.getElementById("VIEW_TIPO").value == "5") {
            strPrefijoMaster = "COT";
            strKeyView = "COT_ID";
            strNomFormView = "COTIZAVIEW";
            strNomFormatPrint = "COTIZA";
            strTipoVtaView = "5";
            strNomOrderView = "COT_ID";
            if (bolSoyMain) {
                ActivaButtons(bolAnularVta, true, false, false, false, false, !bolSoyMain, true, false, false, false, false, true);
            } else {
                ActivaButtons(bolAnularVta, true, false, false, false, true, !bolSoyMain, true, true, true, true, true, true);
            }
        }
        var strParams = "&" + strPrefijoMaster + "_ANULADA=999";
        strParams += "&" + strPrefijoMaster + "_ESRECU=999";
        var strFecha1 = document.getElementById("VIEW_FECHA1").value;
        var strFecha2 = document.getElementById("VIEW_FECHA2").value;
        var strId = document.getElementById("VIEW_ID").value;
        if (strId != "0" && strId != "") {
            strParams += "&" + strKeyView + "=" + strId + "";
        } else {
            if (strFecha1 != "" && strFecha2 != "") {
                strParams += "&" + strPrefijoMaster + "_FECHA1=" + strFecha1 + "&" + strPrefijoMaster + "_FECHA2=" + strFecha2 + "";
            }
            if (document.getElementById("VIEW_SUCURSAL").value != "0") {
                strParams += "&SC_ID=" + document.getElementById("VIEW_SUCURSAL").value + "";
            }
            if (document.getElementById("VIEW_CLIENTE").value != "0") {
                strParams += "&CT_ID=" + document.getElementById("VIEW_CLIENTE").value + "";
            }
            if (document.getElementById("VIEW_TIPO").value == "1") {
                if (document.getElementById("VIEW_FOLIO").value != "") {
                    strParams += "&" + strPrefijoMaster + "_FOLIO_C=" + document.getElementById("VIEW_FOLIO").value + "";
                }
            } else {
                if (document.getElementById("VIEW_FOLIO").value != "") {
                    strParams += "&" + strPrefijoMaster + "_FOLIO=" + document.getElementById("VIEW_FOLIO").value + "";
                }
            }
            if (document.getElementById("VIEW_EMP").value != "0") {
                strParams += "&EMP_ID=" + document.getElementById("VIEW_EMP").value + "";
            }
            if (document.getElementById("VIEW_MONTO") != null) {
                if (document.getElementById("VIEW_MONTO").value != "0") {
                    strParams += "&" + strPrefijoMaster + "_TOTAL=" + document.getElementById("VIEW_MONTO").value + "";
                }
            }
        }
        var grid = jQuery("#VIEW_GRID1");
        grid.setGridParam({url: "CIP_TablaOp.jsp?ID=5&opnOpt=" + strNomFormView + "&_search=true" + strParams});
        grid.setGridParam({sortname: strNomOrderView}).trigger("reloadGrid");
    }
}
function ValidaClean(strNomField) {
    var objDivErr = document.getElementById("err_" + strNomField);
    if (objDivErr != null) {
        objDivErr.innerHTML = "";
        objDivErr.setAttribute("class", "");
        objDivErr.setAttribute("className", "");
    }
}
function ValidaShow(strNomField, strMsg) {
    var objDivErr = document.getElementById("err_" + strNomField);
    objDivErr.setAttribute("class", "");
    objDivErr.setAttribute("class", "inError");
    objDivErr.setAttribute("className", "inError");
    objDivErr.innerHTML = "<img src='images/layout/report3_del.gif' border='0'>&nbsp;" + strMsg;
}
function VtaViewPrint() {
    var grid = jQuery("#VIEW_GRID1");
    if (grid.getGridParam("selrow") != null) {
        var lstRow = grid.getRowData(grid.getGridParam("selrow"));
        if (strNomFormatPrint == "FACTURA") {
            if (intImprimeTicket == 1) {
                ImprimeconFolioTicketView(lstRow.TKT_ID, strNomFormatPrint, 5);
            } else {
                var strHtml = CreaHidden("FAC_ID", lstRow.TKT_ID);
                openWhereverFormat("ERP_SendInvoice?id=" + lstRow.TKT_ID, "FACTURA", "PDF", strHtml);
            }
        } else {
            var strHtml2 = CreaHidden(strKeyView, lstRow.TKT_ID);
            if (intImprimeTicket == 1) {
                ImprimeconFolioTicketView(lstRow.TKT_ID, strNomFormatPrint, 4);
            } else {
                openFormat(strNomFormatPrint, "PDF", strHtml2, lstRow.TKT_FOLIO);
            }
        }
    }
}
function VtaViewAnul() {
    var grid = jQuery("#VIEW_GRID1");
    if (grid.getGridParam("selrow") != null && bolAnularVta) {
        document.getElementById("SioNO_inside").innerHTML = "";
        $("#SioNO").dialog("open");
        $("#SioNO").dialog("option", "title", lstMsg[46]);
        document.getElementById("btnSI").onclick = function () {
            $("#SioNO").dialog("close");
            VtaViewAnulDo();
        };
        document.getElementById("btnNO").onclick = function () {
            $("#SioNO").dialog("close");
        };
    }
}
function VtaViewAnulDo() {
    var grid = jQuery("#VIEW_GRID1");
    if (grid.getGridParam("selrow") != null) {
        $("#dialogWait").dialog("open");
        var lstRow = grid.getRowData(grid.getGridParam("selrow"));
        if (strTipoVtaView == 1 && lstRow.FAC_ID != 0) {
            $.ajax({
                type: "POST", 
                data: encodeURI("idAnul=" + lstRow.TKT_ID), 
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
                    grid.setGridParam({sortname: strNomOrderView}).trigger("reloadGrid");
                    $("#dialogWait").dialog("close");
                }, error: function (objeto, quepaso, otroobj) {
                    alert(":pto9:" + objeto + " " + quepaso + " " + otroobj);
                    $("#dialogWait").dialog("close");
                }});
        } else {
            $.ajax({type: "POST", data: encodeURI("TIPOVENTA=" + strTipoVtaView + "&idAnul=" + lstRow.TKT_ID), scriptCharset: "utf-8", contentType: "application/x-www-form-urlencoded;charset=utf-8", cache: false, dataType: "html", url: "VtasMov.do?id=2", success: function (dato) {
                    dato = trim(dato);
                    if (dato != "OK") {
                        alert(dato);
                    }
                    grid.setGridParam({sortname: strNomOrderView}).trigger("reloadGrid");
                    $("#dialogWait").dialog("close");
                }, error: function (objeto, quepaso, otroobj) {
                    alert(":pto9:" + objeto + " " + quepaso + " " + otroobj);
                    $("#dialogWait").dialog("close");
                }});
        }
    }
}
function VtaRemiPedido() {
    strNomMain = objMap.getNomMain();
    var grid = jQuery("#VIEW_GRID1");
    if (grid.getGridParam("selrow") != null) {
        if (strNomMain == "VENTAS") {
            if (document.getElementById("VIEW_TIPO").value == "3") {
                getPedidoenVenta(grid.getGridParam("selrow"), "REMISION");
            }
        }
    }
}
function VtaFactPedido() {
    strNomMain = objMap.getNomMain();
    var grid = jQuery("#VIEW_GRID1");
    if (grid.getGridParam("selrow") != null) {
        if (strNomMain == "VENTAS") {
            if (document.getElementById("VIEW_TIPO").value == "3") {
                getPedidoenVenta(grid.getGridParam("selrow"), "FACTURA");
            }
        }
    }
}
function resetGridView() {
    var grid = jQuery("#VIEW_GRID1");
    grid.clearGridData();
}
function VtaViewSalir() {
    strNomMain = objMap.getNomMain();
    if (strNomMain != "VTAS_VIEW") {
        $("#dialogView").dialog("close");
    }
}
function VtaViewXML() {
    var grid = jQuery("#VIEW_GRID1");
    if (grid.getGridParam("selrow") != null) {
        var lstRow = grid.getRowData(grid.getGridParam("selrow"));
        openXML(lstRow.TKT_ID);
    }
}
function VtaViewEdit() {
    strNomMain = objMap.getNomMain();
    var grid = jQuery("#VIEW_GRID1");
    var idSel = grid.getGridParam("selrow");
    if (idSel != null) {
        if (document.getElementById("VIEW_TIPO").value == "3") {
            var lstRow = grid.getRowData(idSel);
            if (lstRow.TKT_ANULADA == "NO") {
                if (strNomMain == "VENTAS") {
                    getPedidoenVenta(idSel, "PEDIDO");
                } else {
                    if (strNomMain == "SERVICIOS") {
                        getPedidoenVentaSrv(idSel, "PEDIDO");
                    }
                }
            }
        }
    }
}
function ActivaButtons(bolAnular, bolPrint, bolXML, bolRemiPedi, bolFactPedi, bolRecurr, bolExit, bolEditaDato, bolCerrar, bolFactCoti, bolRemiCoti, bolPediCoti, bolEstatusCoti) {
    if (bolAnular) {
        document.getElementById("vv_btnCancel").style.display = "block";
    } else {
        document.getElementById("vv_btnCancel").style.display = "none";
    }
    if (bolCerrar) {
        document.getElementById("bt_Cerrar").style.display = "block";
    } else {
        document.getElementById("bt_Cerrar").style.display = "none";
    }
    if (bolPrint) {
        document.getElementById("vv_btnPrint").style.display = "block";
    } else {
        document.getElementById("vv_btnPrint").style.display = "none";
    }
    if (bolXML) {
        document.getElementById("vv_btnXML").style.display = "block";
    } else {
        document.getElementById("vv_btnXML").style.display = "none";
    }
    if (bolRemiPedi) {
        document.getElementById("vv_btnOrders").style.display = "block";
    } else {
        document.getElementById("vv_btnOrders").style.display = "none";
    }
    if (bolFactPedi) {
        document.getElementById("vv_btnProccess").style.display = "block";
    } else {
        document.getElementById("vv_btnProccess").style.display = "none";
    }
    if (bolRecurr) {
        document.getElementById("vv_btnrecycle").style.display = "block";
    } else {
        document.getElementById("vv_btnrecycle").style.display = "none";
    }
    if (bolExit) {
        document.getElementById("vv_btnExit").style.display = "block";
    } else {
        document.getElementById("vv_btnExit").style.display = "none";
    }
    if (bolEditaDato) {
        document.getElementById("vv_btnEdita").style.display = "block";
    } else {
        document.getElementById("vv_btnEdita").style.display = "none";
    }
    if (bolFactCoti == null) {
        bolFactCoti = false;
    }
    if (bolFactCoti) {
        document.getElementById("btCt_2").style.display = "block";
    } else {
        document.getElementById("btCt_2").style.display = "none";
    }
    if (bolRemiCoti == null) {
        bolRemiCoti = false;
    }
    if (bolRemiCoti) {
        document.getElementById("btCt_3").style.display = "block";
    } else {
        document.getElementById("btCt_3").style.display = "none";
    }
    if (bolPediCoti == null) {
        bolPediCoti = false;
    }
    if (bolPediCoti) {
        document.getElementById("btCt_1").style.display = "block";
    } else {
        document.getElementById("btCt_1").style.display = "none";
    }
    if (bolEstatusCoti == null) {
        bolEstatusCoti = false;
    }
    if (bolEstatusCoti) {
        document.getElementById("btCt_4").style.display = "block";
    } else {
        document.getElementById("btCt_4").style.display = "none";
    }
}
function OpnDiagCteView() {
    if (typeof bolUsaOpnCte != "undefined") {
        if (bolUsaOpnCte != null) {
            bolUsaOpnCte = false;
        }
    }
    OpnOpt("CLIENTES", "grid", "dialogCte", false, false);
}
function openXML(strFacId) {
    var strHtml = '<form action="ERP_XML_Download.jsp" method="post" target="_blank" id="formSend">';
    strHtml += CreaHidden("FAC_ID", strFacId);
    strHtml += "</form>";
    document.getElementById("formHidden").innerHTML = strHtml;
    document.getElementById("formSend").submit();
}
function _onSortView(index, iCol, sortorder) {
    var strNomOrden = index;
    var strOpciona = document.getElementById("VIEW_TIPO").value;
    if (strOpciona == "1") {
        if (strNomOrden == "TKT_ID") {
            strNomOrden = "FAC_ID";
        }
        if (strNomOrden == "TKT_RAZONSOCIAL") {
            strNomOrden = "FAC_RAZONSOCIAL";
        }
        if (strNomOrden == "TKT_FOLIO") {
            strNomOrden = "FAC_FOLIO";
        }
        if (strNomOrden == "TKT_FECHA") {
            strNomOrden = "FAC_FECHA";
        }
        if (strNomOrden == "TKT_ANULADA") {
            strNomOrden = "FAC_ANULADA";
        }
        if (strNomOrden == "TKT_ESRECU") {
            strNomOrden = "FAC_ESRECU";
        }
    } else {
        if (strOpciona == "3") {
            if (strNomOrden == "TKT_ID") {
                strNomOrden = "PD_ID";
            }
            if (strNomOrden == "TKT_RAZONSOCIAL") {
                strNomOrden = "PD_RAZONSOCIAL";
            }
            if (strNomOrden == "TKT_FOLIO") {
                strNomOrden = "PD_FOLIO";
            }
            if (strNomOrden == "TKT_FECHA") {
                strNomOrden = "PD_FECHA";
            }
            if (strNomOrden == "TKT_ANULADA") {
                strNomOrden = "PD_ANULADA";
            }
            if (strNomOrden == "TKT_ESRECU") {
                strNomOrden = "PD_ESRECU";
            }
        } else {
            if (strOpciona == "4") {
                if (strNomOrden == "TKT_ID") {
                    strNomOrden = "COT_ID";
                }
                if (strNomOrden == "TKT_RAZONSOCIAL") {
                    strNomOrden = "COT_RAZONSOCIAL";
                }
                if (strNomOrden == "TKT_FOLIO") {
                    strNomOrden = "COT_FOLIO";
                }
                if (strNomOrden == "TKT_FECHA") {
                    strNomOrden = "COT_FECHA";
                }
                if (strNomOrden == "TKT_ANULADA") {
                    strNomOrden = "COT_ANULADA";
                }
                if (strNomOrden == "TKT_ESRECU") {
                    strNomOrden = "COT_ESRECU";
                }
            }
        }
    }
    $("#VIEW_GRID1").setGridParam({sortname: strNomOrden});
    return"stop";
}
function dblClickFac(id) {
    var strNomMain = objMap.getNomMain();
    var grid = jQuery("#VIEW_GRID1");
    var lstVal = grid.getRowData(id);
    if (strNomMain == "NCREDITO" || strNomMain == "NOTA_SRV") {
        if (document.getElementById("VIEW_TIPO").value == "1") {
            if (document.getElementById("TKT_IDNC").value != "" && document.getElementById("TKT_IDNC").value != "0") {
                alert(lstMsg[185]);
            } else {
                document.getElementById("FAC_IDNC").value = lstVal.TKT_ID;
                if (strNomMain == "NCREDITO") {
                    getDetaDovuluciones("_fac");
                } else {
                    getDetaDevolucionesServ("_fac");
                }
            }
        } else {
            if (document.getElementById("VIEW_TIPO").value == "2") {
                if (document.getElementById("FAC_IDNC").value != "" && document.getElementById("FAC_IDNC").value != "0") {
                    alert(lstMsg[186]);
                } else {
                    document.getElementById("TKT_IDNC").value = lstVal.TKT_ID;
                    if (strNomMain == "NCREDITO") {
                        getDetaDovuluciones("_tkt");
                    } else {
                        getDetaDevolucionesServ("_tkt");
                    }
                }
            }
        }
        $("#dialogCte").dialog("close");
    }
}
function EditaDatosView() {
    var grid = jQuery("#VIEW_GRID1");
    if (grid.getGridParam("selrow") != null) {
        OpnOpt("EDIT_DAT", null, "dialogProv", false, false, true);
    } else {
        alert("Debe seleccionar una Factura");
    }
}
function InitDatosViewModi() {
    var grid = jQuery("#VIEW_GRID1");
    var opDocumento = document.getElementById("VIEW_TIPO").value;
    $("#dialogWait").dialog("open");
    var lstRow = grid.getRowData(grid.getGridParam("selrow"));
    var strFAC_ID = lstRow.TKT_ID;
    var strPOST = "FAC_ID=" + strFAC_ID;
    strPOST += "&Documento=" + opDocumento;
    $.ajax({type: "POST", data: strPOST, scriptCharset: "utf-8", contentType: "application/x-www-form-urlencoded;charset=utf-8", cache: false, dataType: "xml", url: "ERP_Ventas.jsp?id=28", success: function (datos) {
            var objsF = datos.getElementsByTagName("Facturas")[0];
            var lstFac = objsF.getElementsByTagName("Factura");
            $("#dialogWait").dialog("close");
            $("#dialogWait").dialog("open");
            setTimeout(function () {
                for (i = 0; i < lstFac.length; i++) {
                    var obj = lstFac[i];
                    document.getElementById("EDIT_NOTAS").value = obj.getAttribute("FAC_NOTAS");
                    document.getElementById("EDIT_NOTAS_PIE").value = obj.getAttribute("FAC_NOTASPIE");
                    document.getElementById("EDIT_REFERENCIA").value = obj.getAttribute("FAC_REFERENCIA");
                    document.getElementById("EDIT_FECHA_COBRO").value = obj.getAttribute("FAC_FECHA_COBRO");
                    document.getElementById("EDIT_NUM_GUIA").value = obj.getAttribute("FAC_NUM_GUIA");
                    document.getElementById("EDIT_MONEDA").value = obj.getAttribute("FAC_MONEDA");
                    document.getElementById("EDIT_PERIODO").value = obj.getAttribute("MPE_ID");
                    document.getElementById("EDIT_COMPROBANTE").value = obj.getAttribute("FAC_TIPOCOMP");
                    document.getElementById("EDIT_DIAS_CREDITO").value = obj.getAttribute("FAC_DIAS_CREDITO");
                }
                document.getElementById("EDIT_NOTAS").parentNode.parentNode.style.display = "block";
                document.getElementById("EDIT_NOTAS_PIE").parentNode.parentNode.style.display = "block";
                document.getElementById("EDIT_REFERENCIA").parentNode.parentNode.style.display = "block";
                document.getElementById("EDIT_FECHA_COBRO").parentNode.parentNode.style.display = "block";
                document.getElementById("EDIT_NUM_GUIA").parentNode.parentNode.style.display = "block";
                document.getElementById("EDIT_MONEDA").parentNode.parentNode.style.display = "block";
                document.getElementById("EDIT_DIAS_CREDITO").parentNode.parentNode.style.display = "block";
                document.getElementById("EDIT_PERIODO").parentNode.parentNode.style.display = "block";
                document.getElementById("EDIT_COMPROBANTE").parentNode.parentNode.style.display = "block";
                $("#dialogWait").dialog("close");
            }, 1000);
        }, error: function (objeto, quepaso, otroobj) {
            alert(":ptoExist:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }});
}
function AceptaCambiosDatosView() {
    $("#dialogWait").dialog("open");
    var grid = jQuery("#VIEW_GRID1");
    var opDocumento = document.getElementById("VIEW_TIPO").value;
    var lstRow = grid.getRowData(grid.getGridParam("selrow"));
    var strFAC_ID = lstRow.TKT_ID;
    var strNotas = document.getElementById("EDIT_NOTAS").value;
    var strNotasalPie = document.getElementById("EDIT_NOTAS_PIE").value;
    var strReferencia = document.getElementById("EDIT_REFERENCIA").value;
    var strFechaCobro = document.getElementById("EDIT_FECHA_COBRO").value;
    var strNumeroGuia = document.getElementById("EDIT_NUM_GUIA").value;
    var strMoneda = document.getElementById("EDIT_MONEDA").value;
    var strDiasCredito = document.getElementById("EDIT_DIAS_CREDITO").value;
    var strPeriodo = document.getElementById("EDIT_PERIODO").value;
    var strComprobante = document.getElementById("EDIT_COMPROBANTE").value;
    var strPOST = "FAC_ID=" + strFAC_ID;
    strPOST += "&FAC_NOTAS=" + strNotas;
    strPOST += "&FAC_NOTASPIE=" + strNotasalPie;
    strPOST += "&FAC_REFERENCIA=" + strReferencia;
    strPOST += "&FAC_FECHA_COBRO=" + strFechaCobro;
    strPOST += "&FAC_NUM_GUIA=" + strNumeroGuia;
    strPOST += "&FAC_MONEDA=" + strMoneda;
    strPOST += "&Documento=" + opDocumento;
    strPOST += "&MPE_ID=" + strPeriodo;
    strPOST += "&FAC_TIPOCOMP=" + strComprobante;
    strPOST += "&FAC_DIASCREDITO=" + strDiasCredito;
    $.ajax({type: "POST", data: strPOST, scriptCharset: "utf-8", contentType: "application/x-www-form-urlencoded;charset=utf-8", cache: false, dataType: "html", url: "ERP_Ventas.jsp?id=29", success: function (datos) {
            if (datos == "OK") {
                alert("Se guardaron correctamente los cambios");
                var objSecModiVta = objMap.getScreen("EDIT_DAT");
                objSecModiVta.bolActivo = false;
                objSecModiVta.bolMain = false;
                objSecModiVta.bolInit = false;
                objSecModiVta.idOperAct = 0;
                $("#dialogWait").dialog("close");
                $("#dialogProv").dialog("close");
            }
        }, error: function (objeto, quepaso, otroobj) {
            alert(":edit vta:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
            $("#dialogProv").dialog("close");
        }});
}
function CancelaCambiosDatosView() {
    var objSecModiVta = objMap.getScreen("EDIT_DAT");
    objSecModiVta.bolActivo = false;
    objSecModiVta.bolMain = false;
    objSecModiVta.bolInit = false;
    objSecModiVta.idOperAct = 0;
    $("#dialogProv").dialog("close");
}
function OdcViewClose1() {
    var grid = jQuery("#VIEW_GRID1");
    if (grid.getGridParam("selrow") != null) {
        $("#dialogWait").dialog("open");
        var lstRow = grid.getRowData(grid.getGridParam("selrow"));
        $.ajax({type: "POST", data: encodeURI("PD_ID=" + lstRow.TKT_ID), scriptCharset: "utf-8", contentType: "application/x-www-form-urlencoded;charset=utf-8", cache: false, dataType: "html", url: "VtasMov.do?id=16", success: function (dato) {
                dato = trim(dato);
                if (dato != "OK") {
                    alert(dato);
                }
                grid.setGridParam({sortname: strNomOrderView}).trigger("reloadGrid");
                $("#dialogWait").dialog("close");
            }, error: function (objeto, quepaso, otroobj) {
                alert(":CPX Close:" + objeto + " " + quepaso + " " + otroobj);
                $("#dialogWait").dialog("close");
            }});
    }
}
function ImprimeconFolioTicketView(strKey, strNomFormat, intOpt) {
    strNomFormat += "1";
    $.ajax({type: "POST", data: "ID=" + strKey + "&NOM_FORMATO=" + strNomFormat, scriptCharset: "utf-8", contentType: "application/x-www-form-urlencoded;charset=utf-8", cache: false, dataType: "html", url: "ERP_Varios.jsp?id=" + intOpt, success: function (datos) {
            var miapplet = document.getElementById("PrintTickets");
            miapplet.DoImpresion(datos, strCodEscape, strImpresora);
        }, error: function (objeto, quepaso, otroobj) {
            alert(":ImprimeFolio:" + objeto + " " + quepaso + " " + otroobj);
        }});
}
function CotiPedView() {
    strNomMain = objMap.getNomMain();
    var grid = jQuery("#VIEW_GRID1");
    if (grid.getGridParam("selrow") != null) {
        if (strNomMain == "VENTAS") {
            if (document.getElementById("VIEW_TIPO").value == "5") {
                getCotizaenVenta(grid.getGridParam("selrow"), "PEDIDO");
            }
        }
    }
}
function CotiFactView() {
    strNomMain = objMap.getNomMain();
    var grid = jQuery("#VIEW_GRID1");
    if (grid.getGridParam("selrow") != null) {
        if (strNomMain == "VENTAS") {
            if (document.getElementById("VIEW_TIPO").value == "5") {
                getCotizaenVenta(grid.getGridParam("selrow"), "FACTURA");
            }
        }
    }
}
function CotiRemiView() {
    strNomMain = objMap.getNomMain();
    var grid = jQuery("#VIEW_GRID1");
    if (grid.getGridParam("selrow") != null) {
        if (strNomMain == "VENTAS") {
            if (document.getElementById("VIEW_TIPO").value == "5") {
                getCotizaenVenta(grid.getGridParam("selrow"), "REMISION");
            }
        }
    }
}
function CotiEstatusView() {
    var grid = jQuery("#VIEW_GRID1");
    if (grid.getGridParam("selrow") != null) {
        OpnOpt("MOD_EST_COT", "_ed", "dialog2", false, false, true);
    }
}
function CotiEstatusViewInit() {
    var grid = jQuery("#VIEW_GRID1");
    var opDocumento = document.getElementById("VIEW_TIPO").value;
    $("#dialogWait").dialog("open");
    var lstRow = grid.getRowData(grid.getGridParam("selrow"));
    var strCOT_ID = lstRow.TKT_ID;
    var strPOST = "FAC_ID=" + strCOT_ID;
    strPOST += "&Documento=" + opDocumento;
    $.ajax({type: "POST", data: strPOST, scriptCharset: "utf-8", contentType: "application/x-www-form-urlencoded;charset=utf-8", cache: false, dataType: "xml", url: "ERP_Ventas.jsp?id=28", success: function (datos) {
            var objsF = datos.getElementsByTagName("Facturas")[0];
            var lstFac = objsF.getElementsByTagName("Factura");
            $("#dialogWait").dialog("close");
            $("#dialogWait").dialog("open");
            setTimeout(function () {
                for (i = 0; i < lstFac.length; i++) {
                    var obj = lstFac[i];
                    document.getElementById("estatus_1").value = obj.getAttribute("COE_ID");
                }
                document.getElementById("estatus_1").parentNode.parentNode.style.display = "block";
                $("#dialogWait").dialog("close");
            }, 1000);
        }, error: function (objeto, quepaso, otroobj) {
            alert(":ptoExist:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }});
}
function CotiEstatusViewClose() {
    CotiEstatusViewReset();
}
function CotiEstatusViewReset() {
    var objSecModiVta = objMap.getScreen("MOD_EST_COT");
    objSecModiVta.bolActivo = false;
    objSecModiVta.bolMain = false;
    objSecModiVta.bolInit = false;
    objSecModiVta.idOperAct = 0;
    document.getElementById("dialog2_inside").innerHTML = "";
    $("#dialog2").dialog("close");
}
function CotiEstatusViewOK() {
    $("#dialogWait").dialog("open");
    var grid = jQuery("#VIEW_GRID1");
    var opDocumento = document.getElementById("VIEW_TIPO").value;
    var lstRow = grid.getRowData(grid.getGridParam("selrow"));
    var strFAC_ID = lstRow.TKT_ID;
    var strCOE_ID = document.getElementById("estatus_1").value;
    var strPOST = "FAC_ID=" + strFAC_ID;
    strPOST += "&Documento=" + opDocumento;
    strPOST += "&COE_ID=" + strCOE_ID;
    $.ajax({type: "POST", data: strPOST, scriptCharset: "utf-8", contentType: "application/x-www-form-urlencoded;charset=utf-8", cache: false, dataType: "html", url: "ERP_Ventas.jsp?id=34", success: function (datos) {
            if (datos == "OK") {
                alert("Se guardaron correctamente los cambios");
                $("#dialogWait").dialog("close");
                CotiEstatusViewReset();
            }
        }, error: function (objeto, quepaso, otroobj) {
            alert(":edit estatus:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }});
}
function CotiEstatusViewCANCEL() {
    CotiEstatusViewReset();
}