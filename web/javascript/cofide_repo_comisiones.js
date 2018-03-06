/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function cofide_repo_comisiones() {

}

function initRepoComisiones() {
    getComisiones();
}

//Llama los mÃ©todos para comisiones
function getComisiones() {
//    getComisionEjecutivo();
    getComisionSupervisor();
}//Fin getComisiones

//Obtiene las comisiones de los ejecutivos
function getComisionEjecutivo() {
    var strAnio = document.getElementById("RC_ANIO").value;
    var strMes = document.getElementById("RC_MES").value;
    $("#dialogWait").dialog("open");
    $.ajax({
        type: "POST",
        data: "strMes=" + strMes + "&strAnio=" + strAnio,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "COFIDE_RepoComisiones.jsp?id=1",
        success: function (datos) {
            jQuery("#GR_REPO_COM").clearGridData();
            var lstXml = datos.getElementsByTagName("ReporteComision")[0];
            var lstCom = lstXml.getElementsByTagName("datos");
            for (var i = 0; i < lstCom.length; i++) {
                var obj = lstCom[i];
                var tmpTotal = obj.getAttribute("TOTAL_VENDIDO");
                var tmpTotalComision = 0;
                var tmpTotalNvo = obj.getAttribute("TOTAL_VENDIDO_NVO");
                var tmpTotalExp = obj.getAttribute("TOTAL_VENDIDO_EXP");
                var tmpTotalComisionNvo = obj.getAttribute("TOTAL_VENDIDO_COMISION_NVO");
                var tmpTotalComisionExp = obj.getAttribute("TOTAL_VENDIDO_COMISION_EXP");
                var tmpTotalComisionExpoMayor = obj.getAttribute("TOTAL_VENDIDO_EXP_MAYOR");
                var strBase = "";
                if (obj.getAttribute("CT_BASE") !== null) {
                    strBase = obj.getAttribute("CT_BASE");
                }

                if (strBase === "INBOUND") {
                    tmpTotalComision = tmpTotalComisionExp;
                    tmpTotalComisionExp = 0;
                } else {
                    tmpTotalComision = parseFloat(tmpTotalComisionNvo) + parseFloat(tmpTotalComisionExp) + parseFloat(tmpTotalComisionExpoMayor);
                }

                var rowComision = {
                    RC_CONTADOR: getMaxGridRepoComision("#GR_REPO_COM"),
                    COD_AGENTE: obj.getAttribute("TMK_CLAVE"),
                    US_EQUIPO: obj.getAttribute("EQUIPO"),
                    RC_AGENTE: obj.getAttribute("US_NOMBRE"),
                    TOTAL_VENTA: tmpTotal,
                    TOTAL_NUEVOS: tmpTotalNvo,
                    TOTAL_EXP: tmpTotalExp,
                    TOTAL_COM_NUEVOS: tmpTotalComisionNvo,
                    TOTAL_COM_HASTA: tmpTotalComisionExp,
                    TOTAL_COM_MAS: tmpTotalComisionExpoMayor,
                    TOTAL_COMISION: tmpTotalComision
                };
                jQuery("#GR_REPO_COM").addRowData(getMaxGridRepoComision("#GR_REPO_COM"), rowComision, "last");
            }//Fin FOR
            sumaRepoComisiones("#GR_REPO_COM");
            $("#dialogWait").dialog("close");
        },
        error: function (objeto, quepaso, otroobj) {
            alert(":pto9:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }
    });
}//Fin getComisionEjecutivo

//Obtiene las comisiones de los Supervisores
function getComisionSupervisor() {
    var strAnio = document.getElementById("RC_ANIO").value;
    var strMes = document.getElementById("RC_MES").value;
    $("#dialogWait").dialog("open");
    $.ajax({
        type: "POST",
        data: "strMes=" + strMes + "&strAnio=" + strAnio,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "COFIDE_RepoComisiones.jsp?id=2",
        success: function (datos) {
            jQuery("#GR_REPO_COM_SUP").clearGridData();
            var lstXml = datos.getElementsByTagName("ReporteComision")[0];
            var lstCom = lstXml.getElementsByTagName("datos");
            for (var i = 0; i < lstCom.length; i++) {
                var obj = lstCom[i];

                var tmpTotal = 0;
                var tmpTotalNvo = 0;
                var tmpTotalExp = 0;
                var tmpComNvo = 0;
                var tmoComHasta = 0;
                var tmpComisionMas = 0;
                var tmpTotalComision = 0;

                if (obj.getAttribute("TOTAL_VENDIDO") != null) {
                    tmpTotal = obj.getAttribute("TOTAL_VENDIDO");
                } else {
                    tmpTotal = 0;
                }
                if (obj.getAttribute("TOTAL_VENDIDO_NVO") != null) {
                    tmpTotalNvo = obj.getAttribute("TOTAL_VENDIDO_NVO");
                } else {
                    tmpTotalNvo = 0;
                }
                if (obj.getAttribute("TOTAL_VENDIDO_EXP") != null) {
                    tmpTotalExp = obj.getAttribute("TOTAL_VENDIDO_EXP");
                } else {
                    tmpTotalExp = 0;
                }
                if (obj.getAttribute("COMISION_NVO") != null) {
                    tmpComNvo = obj.getAttribute("COMISION_NVO");
                } else {
                    tmpComNvo = 0;
                }
                if (obj.getAttribute("COM_HASTA") != null) {
                    tmoComHasta = obj.getAttribute("COM_HASTA");
                } else {
                    tmoComHasta = 0;
                }
                if (obj.getAttribute("COM_MASDE") != null) {
                    tmpComisionMas = obj.getAttribute("COM_MASDE");
                } else {
                    tmpComisionMas = 0;
                }
                if (obj.getAttribute("COM_TOT") != null) {
                    tmpTotalComision = obj.getAttribute("COM_TOT");
                } else {
                    tmpTotalComision = 0;
                }

                var rowComision = {
                    RC_CONTADOR: getMaxGridRepoComision("#GR_REPO_COM_SUP"),
                    COD_AGENTE: obj.getAttribute("TMK_CLAVE"),
                    US_EQUIPO: obj.getAttribute("EQUIPO"),
                    RC_AGENTE: obj.getAttribute("US_NOMBRE"),
                    TOTAL_VENTA: tmpTotal,
                    TOTAL_NUEVOS: tmpTotalNvo,
                    TOTAL_EXP: tmpTotalExp,
                    TOTAL_COM_NUEVOS: tmpComNvo,
                    TOTAL_COM_HASTA: tmoComHasta,
                    TOTAL_COM_MAS: tmpComisionMas,
                    TOTAL_COMISION: tmpTotalComision
                };
//                var rowComision = {
//                    RC_CONTADOR: getMaxGridRepoComision("#GR_REPO_COM_SUP"),
//                    COD_AGENTE: obj.getAttribute("TMK_CLAVE"),
//                    US_EQUIPO: obj.getAttribute("EQUIPO"),
//                    RC_AGENTE: obj.getAttribute("US_NOMBRE"),
//                    TOTAL_VENTA: tmpTotal,
//                    TOTAL_NUEVOS: 0,
//                    TOTAL_EXP: tmpTotalExp,
//                    TOTAL_COM_NUEVOS: tmpComNvo,
//                    TOTAL_COM_HASTA: tmoComHasta,
//                    TOTAL_COM_MAS: tmpComisionMas,
//                    TOTAL_COMISION: tmpTotalNvo
//                };
                jQuery("#GR_REPO_COM_SUP").addRowData(getMaxGridRepoComision("#GR_REPO_COM_SUP"), rowComision, "last");
            }//Fin FOR
            sumaRepoComisiones("#GR_REPO_COM_SUP");
            $("#dialogWait").dialog("close");
            getComisionEjecutivo();
        },
        error: function (objeto, quepaso, otroobj) {
            alert(":pto9:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }
    });
}//Fin getComisionSupervisor


function getMaxGridRepoComision(strNomGr) {
    var intLenght = jQuery(strNomGr).getDataIDs().length + 1;
    return intLenght;
}//Fin getMaxGridCursosMaterial

/*Realiza la suma de el reporte de comisiones*/
function sumaRepoComisiones(strNomGrid) {
    var grid = jQuery(strNomGrid);
    var dblTotalVta = 0;
    var dblTotalNvo = 0;
    var dblTotalExp = 0;
    var dblComNvo = 0;
    var dblTotalHasta = 0;
    var dblComMas = 0;
    var dblTotalComision = 0;
    var arr = grid.getDataIDs();
    if (arr != null) {
        for (var i = 0; i < arr.length; i++) {
            var id = arr[i];
            var lstVal = grid.getRowData(id);
            dblTotalVta = dblTotalVta + parseFloat(lstVal.TOTAL_VENTA);
            dblTotalNvo = dblTotalNvo + parseFloat(lstVal.TOTAL_NUEVOS);
            dblTotalExp = dblTotalExp + parseFloat(lstVal.TOTAL_EXP);
            dblComNvo = dblComNvo + parseFloat(lstVal.TOTAL_COM_NUEVOS);
            dblTotalHasta = dblTotalHasta + parseFloat(lstVal.TOTAL_COM_HASTA);
            dblComMas = dblComMas + parseFloat(lstVal.TOTAL_COM_MAS);
            dblTotalComision = dblTotalComision + parseFloat(lstVal.TOTAL_COMISION);
        }
    }
    if (strNomGrid != "GR_REPO_COM_SUP") {
        /*Ponemos el total en el pie de las columnas*/
        grid.footerData('set', {
            RC_AGENTE: "TOTAL:",
            TOTAL_VENTA: dblTotalVta,
            TOTAL_NUEVOS: dblTotalNvo,
            TOTAL_EXP: dblTotalExp,
            TOTAL_COM_NUEVOS: dblComNvo,
            TOTAL_COM_HASTA: dblTotalHasta,
            TOTAL_COM_MAS: dblComMas,
            TOTAL_COMISION: dblTotalComision});
    } else {
        /*Ponemos el total en el pie de las columnas*/
        grid.footerData('set', {
            RC_AGENTE: "TOTAL:",
            TOTAL_VENTA: dblTotalVta,
            TOTAL_COMISION: dblTotalComision});
    }

}//Fin sumaRepoCobranza
