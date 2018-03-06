/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function cofide_pago_instructor() {
}

function initPagoIns() {
}

function pagoInstructorActualizar() {
    var strFecha1 = document.getElementById("REPO_FECHA1").value;
    var strFecha2 = document.getElementById("REPO_FECHA2").value;
    var grid = jQuery("#gridDeta");
    grid.setGridParam({
        url: "COFIDE_Pago_Instructores.jsp?ID=1&FECHA_1=" + strFecha1 + "&FECHA_2=" + strFecha2
    });
    grid.trigger('reloadGrid');
}//Fin pagoInstructorActualizar

function opnSetFechaPagoInstructor() {
    var grid = jQuery("#gridDeta");
    if (grid.getGridParam("selrow")) {
        var objSecModiVta = objMap.getScreen("PAGO_INSTRUCTOR");
        if (objSecModiVta != null) {
            objSecModiVta.bolActivo = false;
            objSecModiVta.bolMain = false;
            objSecModiVta.bolInit = false;
            objSecModiVta.idOperAct = 0;
        }
        OpnOpt("PAGO_INSTRUCTOR", "_ed", "dialog", false, false, true);
    } else {
        alert("Seleccione una Registro");
    }
}//Fin opnSetFechaPagoInstructor

//Agrega Fecha de pago al calculo de los instructores
function setFechaPagoInstructor() {
    var grid = jQuery("#gridDeta");
    var idRow = grid.getGridParam("selrow");
    var lstRow1 = grid.getRowData(idRow);
    var strInstructorID = lstRow1.CI_INSTRUCTOR_ID;
    var strCursoID = lstRow1.CC_CURSO_ID;
    var strFechaPago = document.getElementById("FECHA_PAGO").value;
    var strNumFactura = document.getElementById("NUM_FACTURA").value;
    var strImporteExtra = document.getElementById("IMP_EXTRA").value;

    var strPost = "";
    strPost += "INSTRUCTOR_ID=" + strInstructorID;
    strPost += "&CURSO_ID=" + strCursoID;
    strPost += "&FECHA_PAGO=" + strFechaPago;
    strPost += "&IMPORTE_EXTRA=" + strImporteExtra;
    strPost += "&NUM_FACTURA=" + strNumFactura;
    $("#dialogWait").dialog("open");
    $.ajax({
        type: "POST",
        data: encodeURI(strPost),
        scriptCharset: "UTF-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "html",
        url: "COFIDE_Pago_Instructores.jsp?ID=2",
        success: function (datos) {
            if (datos.trim() == "OK") {
                $("#dialogWait").dialog("close");
                $("#dialog").dialog("close");
                pagoInstructorActualizar();
            } else {
                $("#dialogWait").dialog("close");
                alert(datos);
            }
            $("#dialogWait").dialog("close");
            $("#dialog").dialog("close");
        }
    });
}//Fin setFechaPagoInstructor

//Abre la pantalla de Observaciones a los Pagos de los instructores
function opnSetObservaciones() {
    var grid = jQuery("#gridDeta");
    if (grid.getGridParam("selrow") != null) {
        var lstRow = grid.getRowData(grid.getGridParam("selrow"));
        $("#dialog").dialog("open");
        var strHTML = "<textarea id=\"strObservaciones\" rows=\"4\" cols=\"50\"></textarea><br><br>";
        strHTML += "<button type=\"button\" onclick=\"setObservacionInstructor()\">Aceptar</button>";
//        strHTML += "<button type=\"button\" onclick=\"closeDatePicher()\">Cerrar</button>";
        document.getElementById("dialog_inside").innerHTML = strHTML;
    } else {
        alert("Seleccione una Pago");
    }
}//Fin opnSetObservaciones

//Agrega las observaciones a los pagos del instructor
function setObservacionInstructor() {
    var grid = jQuery("#gridDeta");
    var idRow = grid.getGridParam("selrow");
    var lstRow1 = grid.getRowData(idRow);
    var strInstructorID = lstRow1.CI_INSTRUCTOR_ID;
    var strCursoID = lstRow1.CC_CURSO_ID;
    var strObservaciones = document.getElementById("strObservaciones").value;

    var strPost = "";
    strPost += "INSTRUCTOR_ID=" + strInstructorID;
    strPost += "&CURSO_ID=" + strCursoID;
    strPost += "&OBSERVACIONES=" + strObservaciones;
    $("#dialogWait").dialog("open");
    $.ajax({
        type: "POST",
        data: encodeURI(strPost),
        scriptCharset: "UTF-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "html",
        url: "COFIDE_Pago_Instructores.jsp?ID=3",
        success: function (datos) {
            if (datos.trim() == "OK") {
                $("#dialogWait").dialog("close");
                $("#dialog").dialog("close");
                pagoInstructorActualizar();
            } else {
                $("#dialogWait").dialog("close");
                alert(datos);
            }
            $("#dialogWait").dialog("close");
            $("#dialog").dialog("close");
        }
    });
}//Fin setObservacionInstructor