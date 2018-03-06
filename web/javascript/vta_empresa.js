var itemIdSv = 0;
function vta_empresa() {

}
function InitEmp() {
    document.getElementById("BTN_GUARDAR").parentNode.parentNode.style.display = "none";
    document.getElementById("BTN_CANCEL").parentNode.parentNode.style.display = "none";
    $("#tabsEMPRESAS").tabs("option", "disabled", [2]);
}
function EditEmp() {
    if (document.getElementById("EMP_PATHIMG").value == "") {
        document.getElementById("EMP_IMAGE1").src = "images/image_PNG.png";
    } else {
        ImgLoad(document.getElementById("EMP_PATHIMG").value, "EMP_IMAGE1");
    }
    if (document.getElementById("EMP_ID") != null) {
        var intEMP_ID = document.getElementById("EMP_ID").value;
        ImgLoad("images/codBar" + intEMP_ID + ".png", "EMP_IMAGE2");
    }
    if (document.getElementById("PATHIMAGE3").value == "") {
        document.getElementById("EMP_IMAGE3").src = "images/image_PNG.png";
    } else {
        ImgLoad(document.getElementById("PATHIMAGE3").value, "EMP_IMAGE3");
    }
    if (document.getElementById("PATHIMAGE4").value == "") {
        document.getElementById("EMP_IMAGE4").src = "images/image_PNG.png";
    } else {
        ImgLoad(document.getElementById("PATHIMAGE4").value, "EMP_IMAGE4");
    }
    if (document.getElementById("PATHIMAGE5").value == "") {
        document.getElementById("EMP_IMAGE5").src = "images/image_PNG.png";
    } else {
        ImgLoad(document.getElementById("PATHIMAGE5").value, "EMP_IMAGE5");
    }
    if (document.getElementById("PATHIMAGE6").value == "") {
        document.getElementById("EMP_IMAGE6").src = "images/image_PNG.png";
    } else {
        ImgLoad(document.getElementById("PATHIMAGE6").value, "EMP_IMAGE6");
    }
    if (document.getElementById("EMP_FIRMA_ELECTRONICA").value == "") {
        document.getElementById("EMP_IMAGE_FIRMA").src = "images/image_PNG.png";
    } else {
        ImgLoad(document.getElementById("EMP_FIRMA_ELECTRONICA").value, "EMP_IMAGE_FIRMA");
    }
    document.getElementById("BTN_GUARDAR").parentNode.parentNode.style.display = "none";
    document.getElementById("BTN_CANCEL").parentNode.parentNode.style.display = "none";
}
function ImgLoad(sSrc, strNomImg) {
    var oImg = new Image();
    oImg.onload = function () {
        document.getElementById(strNomImg).src = oImg.src;
    };
    oImg.onerror = function () {
        document.getElementById(strNomImg).src = "images/image_PNG.png";
    };
    oImg.src = sSrc + "#";
}
function UpFileEmp(strNomFile) {
    ValidaClean(strNomFile);
    if (document.getElementById(strNomFile).value == "") {
        ValidaShow(strNomFile, lstMsg[13]);
        return false;
    }
    if (Right(document.getElementById(strNomFile).value.toUpperCase(), 3) != "PNG") {
        ValidaShow(strNomFile, lstMsg[60]);
        return false;
    }
    ajaxFileUpload(strNomFile, document.getElementById("EMP_ID").value);
    return true;
}
function ajaxFileUpload(strNomFile, intEMP_ID) {
    $("#dialogWait").dialog("open");
    $.ajaxFileUpload({
        url: "ERP_UPFileEmp.jsp?EMP_ID=" + intEMP_ID,
        secureuri: false,
        fileElementId: strNomFile,
        dataType: "json",
        success: function (data, status) {
            if (typeof (data.error) != "undefined") {
                if (data.error != "") {
                    alert(data.error);
                } else {
                    alert(data.msg);
                    if (strNomFile == "EMP_UP_IMG1") {
                        document.getElementById("EMP_PATHIMG").value = "images/ptovta/" + document.getElementById("EMP_UP_IMG1").value;
                        ImgLoad(document.getElementById("EMP_PATHIMG").value, "EMP_IMAGE1");
                    }
                    if (strNomFile == "EMP_UP_IMG2") {
                        document.getElementById("PATHIMAGE2").value = "images/codBar" + intEMP_ID + ".png";
                        ImgLoad("images/codBar" + intEMP_ID + ".png", "EMP_IMAGE2");
                    }
                    if (strNomFile == "EMP_UP_IMG3") {
                        document.getElementById("PATHIMAGE3").value = "images/" + document.getElementById("EMP_UP_IMG3").value;
                        ImgLoad(document.getElementById("PATHIMAGE3").value, "EMP_IMAGE3");
                    }
                    if (strNomFile == "EMP_UP_IMG4") {
                        document.getElementById("PATHIMAGE4").value = "images/" + document.getElementById("EMP_UP_IMG4").value;
                        ImgLoad(document.getElementById("PATHIMAGE4").value, "EMP_IMAGE4");
                    }
                    if (strNomFile == "EMP_UP_IMG5") {
                        document.getElementById("PATHIMAGE5").value = "images/" + document.getElementById("EMP_UP_IMG5").value;
                        ImgLoad(document.getElementById("PATHIMAGE5").value, "EMP_IMAGE5");
                    }
                    if (strNomFile == "EMP_UP_IMG6") {
                        document.getElementById("PATHIMAGE6").value = "images/" + document.getElementById("EMP_UP_IMG6").value;
                        ImgLoad(document.getElementById("PATHIMAGE6").value, "EMP_IMAGE6");
                    }
                    if (strNomFile == "EMP_LAB_FIRMA") {
                        document.getElementById("EMP_FIRMA_ELECTRONICA").value = "images/" + document.getElementById("EMP_LAB_FIRMA").value;
                        ImgLoad(document.getElementById("EMP_FIRMA_ELECTRONICA").value, "EMP_IMAGE_FIRMA");
                    }
                }
            }
            $("#dialogWait").dialog("close");
        }, error: function (data, status, e) {
            alert(e);
            $("#dialogWait").dialog("close");
        }});
    return false;
}
function tabShowEmpresas(event, ui) {
    var idx = document.getElementById("EMP_ID").value;
    if (ui.newTab.index() == 2) {
        LoadRegimen(idx);
    }
}
function LoadRegimen(idx) {
    var grid = jQuery("#GRID_REGFIS");
    grid.setGridParam({url: "CIP_TablaOp.jsp?ID=5&opnOpt=LISTREG&_search=true&EMP_ID=" + idx});
    grid.trigger("reloadGrid");
}
function nuevoRegimen() {
    document.getElementById("BTN_GUARDAR").parentNode.parentNode.style.display = "";
    document.getElementById("BTN_CANCEL").parentNode.parentNode.style.display = "";
    document.getElementById("BTN_NEWREG").parentNode.parentNode.style.display = "none";
    document.getElementById("BTN_DELREG").parentNode.parentNode.style.display = "none";
}
function cancelRegimen() {
    document.getElementById("BTN_GUARDAR").parentNode.parentNode.style.display = "none";
    document.getElementById("BTN_CANCEL").parentNode.parentNode.style.display = "none";
    document.getElementById("BTN_NEWREG").parentNode.parentNode.style.display = "";
    document.getElementById("BTN_DELREG").parentNode.parentNode.style.display = "";
}
function delRegimen() {
    var grid = jQuery("#GRID_REGFIS");
    if (grid.getGridParam("selrow") != null) {
        var id = grid.getGridParam("selrow");
        var lstRow = grid.getRowData(id);
        var IdRen = lstRow.EMPR_ID;
        if (confirm("¿Confirma borrar el regimen fiscal " + lstRow.REGF_ID + "?")) {
            grid.delRowData(grid.getGridParam("selrow"));
            $.ajax({type: "POST", data: "idReg=" + IdRen, scriptCharset: "utf-8", contentType: "application/x-www-form-urlencoded;charset=utf-8", cache: false, dataType: "html", url: "ERP_RegimenFiscal.jsp?ID=2", success: function (datos) {
                    if (datos.substring(0, 2) == "OK") {
                        alert("Regimen Fiscal Borrado!!");
                    } else {
                        alert(datos);
                    }
                    $("#dialogWait").dialog("close");
                }, error: function (objeto, quepaso, otroobj) {
                    alert(":pto:" + objeto + " " + quepaso + " " + otroobj);
                }});
        }
    }
}
function saveRegimen() {
    if (document.getElementById("CMB_REGFIS").value != 0) {
        var strPost = "&idReg=" + encode(document.getElementById("CMB_REGFIS").value);
        strPost += "&idEmp=" + document.getElementById("EMP_ID").value;
        $.ajax({type: "POST", data: strPost, scriptCharset: "utf-8", contentType: "application/x-www-form-urlencoded;charset=utf-8", cache: false, dataType: "html", url: "ERP_RegimenFiscal.jsp?ID=3", success: function (datos) {
                if (datos.substring(0, 2) == "OK") {
                    var grid = jQuery("#GRID_REGFIS");
                    grid.trigger("reloadGrid");
                    cancelRegimen();
                    alert("Alta exitosa!!");
                } else {
                    if (datos.substring(0, 2) == "NO") {
                        alert("El regimen Fiscal ya existe, elija otro!");
                    } else {
                        alert(datos);
                    }
                }
                $("#dialogWait").dialog("close");
            }, error: function (objeto, quepaso, otroobj) {
                alert(":pto:" + objeto + " " + quepaso + " " + otroobj);
            }});
    } else {
        alert("ELIJA EL REGIMEN FISCAL QUE DESEA DAR DE ALTA PARA ESTA EMPRESA.");
    }
}
function DownloadFileEmp(strIdenImg) {
    var strNomImg = "";
    if (strIdenImg == "EMP_PATHIMG") {
        strNomImg = document.getElementById("EMP_PATHIMG").value;
    }
    if (strIdenImg == "PATHIMAGE2") {
        strNomImg = document.getElementById("PATHIMAGE2").value;
    }
    if (strIdenImg == "PATHIMAGE3") {
        strNomImg = document.getElementById("PATHIMAGE3").value;
    }
    if (strIdenImg == "PATHIMAGE4") {
        strNomImg = document.getElementById("PATHIMAGE4").value;
    }
    if (strIdenImg == "PATHIMAGE5") {
        strNomImg = document.getElementById("PATHIMAGE5").value;
    }
    if (strIdenImg == "PATHIMAGE6") {
        strNomImg = document.getElementById("PATHIMAGE6").value;
    }
    Abrir_Link(strNomImg, 500, 600, 0, 0);
}