/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function cofide_repo_diplomadoseminario() {
    
}

function initRepoDipSem() {
    getSeminarios();
}

function getSeminarios() {
    var strAnio = document.getElementById("DS_ANIO").value;
    var strMes = document.getElementById("DS_MES").value;
    
    $("#dialogWait").dialog("open");
    $.ajax({
        type: "POST",
        data: "strMes=" + strMes + "&strAnio=" + strAnio,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "COFIDE_DiplomadoSeminario.jsp?id=1",
        success: function (datos) {
            jQuery("#GR_REPO_DS").clearGridData();
            var lstXml = datos.getElementsByTagName("DiplomadoSeminario")[0];
            var lstCom = lstXml.getElementsByTagName("datos");

            for (var i = 0; i < lstCom.length; i++) {
                var obj = lstCom[i];
                var rowDS = {
                    DP_CONTADOR: getMaxGridDipSem("#GR_REPO_DS"),
                    TIPO_CURSO: obj.getAttribute("TIPO_CURSO"),
                    CC_NOMBRE_CURSO: obj.getAttribute("CC_NOMBRE_CURSO"),
                    CC_FECHA_INICIAL: obj.getAttribute("CC_FECHA_INICIAL"),
                    TMK_CLAVE: obj.getAttribute("TMK_CLAVE"),
                    EQUIPO: obj.getAttribute("EQUIPO"),
                    nombre_usuario: obj.getAttribute("nombre_usuario"),
                    TIPO_DOC: obj.getAttribute("TIPO_DOC"),
                    FAC_FOLIO: obj.getAttribute("FAC_FOLIO"),
                    IMPORTE: obj.getAttribute("IMPORTE"),
                    FAC_DESCUENTO: obj.getAttribute("FAC_DESCUENTO"),
                    TOTAL: obj.getAttribute("TOTAL"),
                    FAC_FECHA_COBRO: obj.getAttribute("FAC_FECHA_COBRO"),
                    FAC_IMP_PAGADO: obj.getAttribute("FAC_IMP_PAGADO"),
                    IMP_BONO: obj.getAttribute("IMP_BONO"),
                    FAC_ID: obj.getAttribute("FAC_ID"),
                    APLICA_BONO: obj.getAttribute("APLICA_BONO")
                };
                
                jQuery("#GR_REPO_DS").addRowData(getMaxGridDipSem("#GR_REPO_DS"), rowDS, "last");
            }//Fin FOR
            sumaRepoValidaFacturas();
            $("#dialogWait").dialog("close");
        },
        error: function (objeto, quepaso, otroobj) {
            alert(":pto9:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }
    });
}//Fin getSeminarios

function getMaxGridDipSem(strNomGr) {
    var intLenght = jQuery(strNomGr).getDataIDs().length + 1;
    return intLenght;
}//Fin getMaxGridCursosMaterial

/*Realiza la suma de el reporte de Facturas Cursos Diplomados Seminarios*/
function sumaRepoValidaFacturas() {
    var grid = jQuery("#GR_REPO_DS");
    var dblTotalBono = 0;
    var dblTotal = 0;
    var arr = grid.getDataIDs();
    if (arr != null) {
        for (var i = 0; i < arr.length; i++) {
            var id = arr[i];
            var lstVal = grid.getRowData(id);
            dblTotalBono = dblTotalBono + parseFloat(lstVal.FAC_BONO);
            dblTotal = dblTotal + parseFloat(lstVal.FAC_IMPORTE);
        }
    }
    /*Ponemos el total en el pie de las columnas*/
    grid.footerData('set', {FAC_FECHAPAGO: "TOTAL", FAC_IMPORTE: dblTotal, FAC_BONO: dblTotalBono});
}//Fin sumaRepoValidaFacturas

/*APRUEBA LOS BONOS POR SEMINARIO*/
function validaBonoSeminario(strAplica) {
    var strFAC_IDs = "";
    var grid1 = jQuery("#GR_REPO_DS");
    var lista = grid1.getGridParam("selarrrow");
    if (lista != null) {
        for (var i = 0; i < lista.length; i++) {
            var idlast = lista[i];
            var lstRow = grid1.getRowData(idlast);
            strFAC_IDs += lstRow.FAC_ID + ",";
        }
        var strPost = "FAC_ID=" + strFAC_IDs;
        strPost += "&APLICA=" + strAplica;
        $("#dialogWait").dialog("open");
        $.ajax({
            type: "POST",
            data: encodeURI(strPost),
            scriptCharset: "UTF-8",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "html",
            url: "COFIDE_DiplomadoSeminario.jsp?id=2",
            success: function (datos) {
                if (datos.substring(0, 2) == "OK") {
                    getSeminarios();
                    $("#dialogWait").dialog("close");
                } else {
                    $("#dialogWait").dialog("close");
                    alert(datos);
                }
                $("#dialogWait").dialog("close");
            }
        }); //fin del ajax
    } else {
        alert("Seleccione almenos un registro.");
    }
}