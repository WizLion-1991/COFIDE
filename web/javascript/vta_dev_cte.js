function vta_dev_cte() {

}
function initDevCte() {
    var strHtml = "<ul>" + getMenuItem("GuardaDevCte();", "Guardar", "images/ptovta/CircleSave.png") + getMenuItem("SalirDevCte();", "Salir", "images/ptovta/exitBig.png") + "</ul>";
    document.getElementById("TOOLBAR").innerHTML = strHtml;
    d.getElementById("btn1").setAttribute("class", "Oculto");
    d.getElementById("btn1").setAttribute("className", "Oculto");
}
function GuardaDevCte() {
    var strBC_ID = document.getElementById("BC_ID").value;
    var strCT_ID = document.getElementById("CT_ID").value;
    var strFECHA = document.getElementById("FECHA").value;
    var strNOTAS = document.getElementById("NOTAS").value;
    var strMONEDA = document.getElementById("MONEDA").value;
    var strTASAPESO = document.getElementById("TASAPESO").value;
    var strMONTOPAGO = document.getElementById("MONTOPAGO").value;
    var strCHEQUE = document.getElementById("CHEQUE").value;
    var strPOST = "BC_ID=" + strBC_ID;
    strPOST += "&CT_ID=" + strCT_ID;
    strPOST += "&FECHA=" + strFECHA;
    strPOST += "&NOTAS=" + strNOTAS;
    strPOST += "&MONEDA=" + strMONEDA;
    strPOST += "&TASAPESO=" + strTASAPESO;
    strPOST += "&MONTOPAGO=" + strMONTOPAGO;
    strPOST += "&CHEQUE=" + strCHEQUE;
    $.ajax({type: "POST", data: encodeURI(strPOST), scriptCharset: "utf-8", contentType: "application/x-www-form-urlencoded;charset=utf-8", cache: false, dataType: "html", url: "ERP_Cobros.jsp?id=12", success: function (dato) {
            if (Left(dato, 3) == "OK.") {
                alert("Los cambios fueron hechos satisfactoriamente");
            } else {
                alert(dato);
            }
        }, error: function (objeto, quepaso, otroobj) {
            alert(":pto9:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }});
}
function SalirDevCte() {
    myLayout.open("west");
    myLayout.open("east");
    myLayout.open("south");
    myLayout.open("north");
    var objMainFacPedi = objMap.getScreen("DEV_CTE1");
    objMainFacPedi.bolActivo = false;
    objMainFacPedi.bolMain = false;
    objMainFacPedi.bolInit = false;
    objMainFacPedi.idOperAct = 0;
    document.getElementById("MainPanel").innerHTML = "";
}
function OpnDiagCte() {
    OpnOpt("CLIENTES", "grid", "dialogCte", false, false);
}
function MonedaCte() {
    $("#dialogWait").dialog("open");
    var strCT_ID = document.getElementById("CT_ID").value;
    var strPOST = "CT_ID=" + strCT_ID;
    if (strCT_ID == "") {
        document.getElementById("CT_ID").value = 0;
        document.getElementById("MONEDA_CTE").value = 0;
        $("#dialogWait").dialog("close");
    } else {
        $.ajax({type: "POST", data: encodeURI(strPOST), scriptCharset: "utf-8", contentType: "application/x-www-form-urlencoded;charset=utf-8", cache: false, dataType: "html", url: "ERP_DevCte.jsp?id=1", success: function (dato) {
                document.getElementById("MONEDA_CTE").value = dato;
                $("#dialogWait").dialog("close");
                CalculaTasaCambioCte();
            }, error: function (objeto, quepaso, otroobj) {
                alert(":pto9:" + objeto + " " + quepaso + " " + otroobj);
                $("#dialogWait").dialog("close");
            }});
    }
}
function CalculaTasaCambioCte() {
    $("#dialogWait").dialog("open");
    var intMonedaBanco = document.getElementById("MONEDA_CTE").value;
    var intMonedaSeleccionada = document.getElementById("MONEDA").value;
    var strFecha = document.getElementById("FECHA").value;
    var strPOST = "&Moneda_1=" + intMonedaSeleccionada;
    strPOST += "&Moneda_2=" + intMonedaBanco;
    strPOST += "&fecha=" + strFecha;
    $.ajax({type: "POST", data: strPOST, scriptCharset: "utf-8", contentType: "application/x-www-form-urlencoded;charset=utf-8", cache: false, dataType: "xml", url: "ERP_CobrosCuentas.jsp?id=9", success: function (datos) {
            var objsc = datos.getElementsByTagName("TasaCambio")[0];
            var lstTiks = objsc.getElementsByTagName("TasaCambios");
            var obj = lstTiks[0];
            var dblTC = obj.getAttribute("TC");
            var strOperacion = obj.getAttribute("Operacion");
            document.getElementById("COB_OPERACION").value = strOperacion;
            if (dblTC == 0) {
                dblTC = 1;
            }
            document.getElementById("TASAPESO").value = dblTC;
            $("#dialogWait").dialog("close");
        }, error: function (objeto, quepaso, otroobj) {
            alert(":ptoExist:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }});
}