/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var strDBEjecutivo = "";
var timerLoad;

function cofide_valida_datos() {
}

function initValidaDuplicados() {
    getVtasDuplicadas();
}
function initTelemINB() { //consulta de logistica
    myLayout.close("west");
    myLayout.close("east");
    myLayout.close("south");
    myLayout.close("north");
    var strIdcte = document.getElementById("tmp_ct_id").value;
    if (strIdcte != "") {
        consultaVtaLogs(strIdcte);
    } else {
        alert("Vuelve a seleccionar el registro a consultar");
    }
}
function opnSelectBase() {
    var grid = jQuery("#GR_VTAS_DP");
    var ids = grid.getGridParam("selrow");
    if (ids != null) {
        var objSecModiVta = objMap.getScreen("CT_DUPLICADO");
        if (objSecModiVta != null) {
            objSecModiVta.bolActivo = false;
            objSecModiVta.bolMain = false;
            objSecModiVta.bolInit = false;
            objSecModiVta.idOperAct = 0;
        }
        OpnOpt("CT_DUPLICADO", "_ed", "dialog", false, false, true);
    } else {
        alert("Seleccione una venta en la lista.");
    }
}

function initSetCtBase(opc) {
    var grid = jQuery("#GR_VTAS_DP");
    var ids = grid.getGridParam("selrow");
    if (ids != null) {
        var lstRow = grid.getRowData(ids);
        var intIdTkt = lstRow.VL_ID;
        strDBEjecutivo = lstRow.VL_CT_BASE;

        $.ajax({
            type: "POST",
            data: "intTkt=" + intIdTkt,
            scriptCharset: "utf-8",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "xml",
            url: "COFIDE_ValFac_Duplicados.jsp?id=2",
            beforeSend: function () {
                $("#dialogWait").dialog("open");
            },
            success: function (datos) {
                jQuery("#GRD_DUPLICADOS").clearGridData();
                var lstXml = datos.getElementsByTagName("CtCoincidencia")[0];
                var lstCom = lstXml.getElementsByTagName("datos");
                for (var i = 0; i < lstCom.length; i++) {
                    var obj = lstCom[i];
                    var rowComision = {
                        VL_CONTADOR: getMaxGridDuplicados("#GRD_DUPLICADOS"),
                        VL_ID: obj.getAttribute("CT_ID"),
                        VL_NOMBRE: obj.getAttribute("CT_NOMBRE"),
                        VL_CT_BASE: obj.getAttribute("CT_CLAVE_DDBB"),
                        VL_RAZONSOCIAL: obj.getAttribute("CT_RAZONSOCIAL"),
                        VL_EMAIL: obj.getAttribute("CT_EMAIL1"),
                        VL_RFC: obj.getAttribute("CT_RFC"),
                        VL_AGENTE: obj.getAttribute("strAgente")
                    };
                    jQuery("#GRD_DUPLICADOS").addRowData(getMaxGridDuplicados("#GRD_DUPLICADOS"), rowComision, "last");
                }//Fin FOR
                $("#dialogWait").dialog("close");
            },
            error: function (objeto, quepaso, otroobj) {
                alert(":pto9:" + objeto + " " + quepaso + " " + otroobj);
                $("#dialogWait").dialog("close");
            }
        });
    } else {
        if (opc == 1) { //carga desde init
            alert("Seleccione un Ticket en la lista.");
        }
    }
}//Fin opnDenegarPago

function getVtasDuplicadas() {
    clearTimeout(timerLoad);

    $.ajax({
        type: "POST",
        data: "",
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "COFIDE_ValFac_Duplicados.jsp?id=1",
        beforeSend: function () {
            $("#dialogWait").dialog("open");
        },
        success: function (datos) {
            jQuery("#GR_VTAS_DP").clearGridData();
            var lstXml = datos.getElementsByTagName("VtasDuplicadas")[0];
            var lstCom = lstXml.getElementsByTagName("datos");
            for (var i = 0; i < lstCom.length; i++) {
                var obj = lstCom[i];
                var rowComision = {
                    VL_CONTADOR: getMaxGridDuplicados("#GR_VTAS_DP"),
                    VL_ID: obj.getAttribute("VL_ID"),
                    VL_ID_OLD: obj.getAttribute("COFIDE_DUPLICIDAD_ID"),
                    VL_CT_BASE: obj.getAttribute("CT_CLAVE_DDBB"),
                    VL_RAZONSOCIAL: obj.getAttribute("CT_RAZONSOCIAL"),
                    VL_FECHA: obj.getAttribute("FECHA"),
                    VL_HORA: obj.getAttribute("HORA"),
                    VL_MOTIVO1: obj.getAttribute("CORREO"),
                    VL_MOTIVO2: obj.getAttribute("TELEFONO"),
                    VL_MOTIVO3: obj.getAttribute("RFC"),
                    VL_AGENTE: obj.getAttribute("AGENTE")
                };
                jQuery("#GR_VTAS_DP").addRowData(getMaxGridDuplicados("#GR_VTAS_DP"), rowComision, "last");
            }//Fin FOR
            $("#dialogWait").dialog("close");
            timerLoadVta();
        },
        error: function (objeto, quepaso, otroobj) {
            alert(":pto9:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }
    });
}
function timerLoadVta() {
    timerLoad = setTimeout('getVtasDuplicadas()', 90000);
}


function getMaxGridDuplicados(strNomGr) {
    var intLenght = jQuery(strNomGr).getDataIDs().length + 1;
    return intLenght;
}//Fin getMaxGridCursosMaterial

//confirma la venta con la base seleccionada
function setConfirmCliente() {
    var strDb = document.getElementById("SEL_BASE").value;
    var gridCte = jQuery("#GRD_DUPLICADOS"); //grid de los clientes
    var grid = jQuery("#GR_VTAS_DP"); //grid de ventas duplicadas
    var ids = grid.getGridParam("selrow");
    var idArr = gridCte.getDataIDs();
    if (idArr.length >= 1) {
        if (ids != null) {
            if (strDb != "0") {
                document.getElementById("SioNO_inside").innerHTML = "¿Confirmar asignación con la base seleccionada?";
                $("#SioNO").dialog("open");
                document.getElementById("btnSI").onclick = function () {
                    $("#SioNO").dialog("close");
                    var lstRow = grid.getRowData(ids);
                    var intCtId = lstRow.VL_ID; //id de la venta
                    var strPost = "";
                    strPost += "&CT_BASE=" + strDb;
                    strPost += "&ID_VTA=" + intCtId;

                    $.ajax({
                        type: "POST",
                        data: strPost,
                        scriptCharset: "utf-8",
                        contentType: "application/x-www-form-urlencoded;charset=utf-8",
                        cache: false,
                        dataType: "html",
                        url: "COFIDE_ValFac_Duplicados.jsp?id=3",
                        beforeSend: function () {
                            $("#dialogWait").dialog("open");
                        },
                        success: function (datos) {
                            if (datos.substring(0, 2) == "OK") {
                                $("#dialogWait").dialog("close");
                                getVtasDuplicadas();
                                $("#dialog").dialog("close");
                            } else {
                                alert(datos);
                                $("#dialogWait").dialog("close");
                            }
                        },
                        error: function (objeto, quepaso, otroobj) {
                            alert(":pto=3:" + objeto + " " + quepaso + " " + otroobj);
                        }
                    });
                };
                document.getElementById("btnNO").onclick = function () {
                    $("#SioNO").dialog("close");
                };
            } else {
                alert("selecciona una base, para asignar la venta");
            }
        } else {
            alert("Seleccione un Cliente en la lista.");
        }
    } else {
        alert("No se encontraron coincidencias en la venta, \nfavor de revisar la información");
    }
}



function setAnulTkt() {
    var grid = jQuery("#GR_VTAS_DP");
    var ids = grid.getGridParam("selrow");
    var intIdTkt = 0;
    if (ids != null) {
        document.getElementById("SioNO_inside").innerHTML = "¿Estas seguro de anular esta venta?";
        $("#SioNO").dialog("open");
        document.getElementById("btnSI").onclick = function () {
            $("#SioNO").dialog("close");
            var lstRow = grid.getRowData(ids);
            intIdTkt = lstRow.VL_ID;
            var strPost = "TKT_ID=" + intIdTkt;

            $.ajax({
                type: "POST",
                data: strPost,
                scriptCharset: "utf-8",
                contentType: "application/x-www-form-urlencoded;charset=utf-8",
                cache: false,
                dataType: "html",
                url: "COFIDE_ValFac_Duplicados.jsp?id=4",
                beforeSend: function () {
                    $("#dialogWait").dialog("open");
                },
                success: function (datos) {
                    if (datos.substring(0, 2) == "OK") {
                        $("#dialogWait").dialog("close");
                        getVtasDuplicadas();
                    } else {
                        alert(datos);
                        $("#dialogWait").dialog("close");
                    }
                },
                error: function (objeto, quepaso, otroobj) {
                    alert(":pto=4:" + objeto + " " + quepaso + " " + otroobj);
                }
            });
        };
        document.getElementById("btnNO").onclick = function () {
            $("#SioNO").dialog("close");
        };
    } else {
        alert("Seleccione una venta en la lista.");
    }

}

function delClienteCofide() {
    var grid = jQuery("#GRD_DUPLICADOS");
    var ids = grid.getGridParam("selrow");
    var intIdCt = 0;
    if (ids != null) {
        var lstRow = grid.getRowData(ids);
        intIdCt = lstRow.VL_ID;
        var strPost = "CT_ID=" + intIdCt;

        $.ajax({
            type: "POST",
            data: strPost,
            scriptCharset: "utf-8",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "html",
            url: "COFIDE_ValFac_Duplicados.jsp?id=5",
            beforeSend: function () {
                $("#dialogWait").dialog("open");
            },
            success: function (datos) {
                if (datos.substring(0, 2) == "OK") {
                    $("#dialogWait").dialog("close");
                    initSetCtBase();
                } else {
                    alert(datos);
                    $("#dialogWait").dialog("close");
                }
            },
            error: function (objeto, quepaso, otroobj) {
                alert(":pto:" + objeto + " " + quepaso + " " + otroobj);
            }
        });
    } else {
        alert("Seleccione un Cliente en la lista.");
    }
}

function setAprobadoTkt() {
    var grid = jQuery("#GR_VTAS_DP");
    var ids = grid.getGridParam("selrow");
    var intIdTkt = 0;
    if (ids != null) {
        document.getElementById("SioNO_inside").innerHTML = "¿Confirmar venta?";
        $("#SioNO").dialog("open");
        document.getElementById("btnSI").onclick = function () {
            $("#SioNO").dialog("close");
            var lstRow = grid.getRowData(ids);
            intIdTkt = lstRow.VL_ID; //id master
            var strPost = "TKT_ID=" + intIdTkt; //id master de la venta

            $.ajax({
                type: "POST",
                data: strPost,
                scriptCharset: "utf-8",
                contentType: "application/x-www-form-urlencoded;charset=utf-8",
                cache: false,
                dataType: "html",
                url: "COFIDE_ValFac_Duplicados.jsp?id=6",
                beforeSend: function () {
                    $("#dialogWait").dialog("open");
                },
                success: function (datos) {
                    if (datos.substring(0, 2) == "OK") {
                        $("#dialogWait").dialog("close");
//                        ConfirmaVta(intIdTkt);
                        getVtasDuplicadas();
                    } else {
                        alert(datos);
                        $("#dialogWait").dialog("close");
                    }
                },
                error: function (objeto, quepaso, otroobj) {
                    alert(":pto:" + objeto + " " + quepaso + " " + otroobj);
                }
            });
        };
        document.getElementById("btnNO").onclick = function () {
            $("#SioNO").dialog("close");
        };
    } else {
        alert("Seleccione una venta en la lista.");
    }
}

function OpnMotorBusquedaValidaD() {
    var objSecModiVta = objMap.getScreen("MOTOR_CTE");
    if (objSecModiVta != null) {
        objSecModiVta.bolActivo = false;
        objSecModiVta.bolMain = false;
        objSecModiVta.bolInit = false;
        objSecModiVta.idOperAct = 0;
    }
    OpnOpt("MOTOR_CTE", "_ed", "dialogInv", false, false, true);
}
function opnCteDetalleVald() {
    var objSecModiVta = objMap.getScreen("TELEM_INB");
    if (objSecModiVta != null) {
        objSecModiVta.bolActivo = false;
        objSecModiVta.bolMain = false;
        objSecModiVta.bolInit = false;
        objSecModiVta.idOperAct = 0;
    }
    OpnOpt("TELEM_INB", "_ed", "dialogCte", false, false, true);
}
function consultaVtaLogs(strIdcte) {
//    console.log(strIdcte + " id seleccionado de logistica");
//bloquea campos 'combos'
    document.getElementById("CT_SEDE").disabled = true;
    document.getElementById("CT_GIRO").disabled = true;
    document.getElementById("CT_AREA").disabled = true;
    document.getElementById("CT_CP").disabled = true;
    document.getElementById("CT_COL").disabled = true;
    //inicia consulta del cliente
    var strHtmlTitle = "<table border='0' width='0%' align='center'>"
            + "<tr>"
            + '<td><a href="javascript:OpnDiagHCallD()" class=\'cofide_histl\'><i class = "fa fa-clock-o" title="Historial de Llamadas" style="font-size:30px; width:110px"></i></td>'
            + "<td>&nbsp;</td>"
            + '<td><a href="javascript:OpnSalir()" class=\'cofide_salida\'><i class = "fa fa-sign-out" title="Salir"  style="font-size:30px; width:110px"></i></td>'
            + "</tr>";
    "</table>";
    document.getElementById("CT_TITLEBTN").innerHTML = strHtmlTitle;
    //datos a recuperar en pantalla
    var strPost = "";
//    console.log(strIdcte + " id que vamos a buscar");
    strPost = "cte_manual=" + strIdcte;
    $.ajax({
        type: "POST",
        data: strPost,
        scriptCharset: "UTF-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "COFIDE_Telemarketing.jsp?ID=1",
        success: function (datos) {
            var lstXml = datos.getElementsByTagName("vta")[0];
            var lstCte = lstXml.getElementsByTagName("datos");
            for (var i = 0; i < lstCte.length; i++) {
                var objcte = lstCte[i];
                document.getElementById("CT_ID").value = objcte.getAttribute("CT_ID");
                document.getElementById("CT_ID_CLIENTE").value = objcte.getAttribute("CT_ID_CLIENTE");
                document.getElementById("CT_RAZONSOCIAL").value = objcte.getAttribute("CT_RAZONSOCIAL");
                document.getElementById("CT_NO_CLIENTE").value = objcte.getAttribute("CT_ID");
                document.getElementById("CT_RFC").value = objcte.getAttribute("CT_RFC");
                document.getElementById("CT_COL").value = objcte.getAttribute("CT_COLONIA");
                document.getElementById("CT_CONTACTO").value = objcte.getAttribute("CT_TELEFONO1");
                document.getElementById("CT_CONTACTO2").value = objcte.getAttribute("CT_TELEFONO2");
                document.getElementById("CT_CORREO").value = objcte.getAttribute("CT_EMAIL1");
                document.getElementById("CT_CORREO2").value = objcte.getAttribute("CT_EMAIL2");
                document.getElementById("CT_BOLBASE").value = objcte.getAttribute("bolBase");
                document.getElementById("CT_HORA_INI").value = objcte.getAttribute("HoraInicial");
                document.getElementById("CT_CP").value = objcte.getAttribute("CT_CP");
                document.getElementById("CT_COL").value = objcte.getAttribute("CT_COL");
                document.getElementById("CT_COLONIA_DB").value = objcte.getAttribute("CT_COL");
                document.getElementById("CT_CALLE").value = objcte.getAttribute("CT_CALLE");
                document.getElementById("CT_EDO").value = objcte.getAttribute("CT_EDO");
                document.getElementById("CT_MUNI").value = objcte.getAttribute("CT_MUNI");
                document.getElementById("CT_NUM").value = objcte.getAttribute("CT_NUMERO");
                document.getElementById("CT_SEDE").value = objcte.getAttribute("CT_SEDE");
                document.getElementById("CT_GIRO").value = objcte.getAttribute("CT_GIRO");
                document.getElementById("CT_AREA").value = objcte.getAttribute("CT_AREA");
                document.getElementById("CT_COMENTARIO").value = objcte.getAttribute("EV_ASUNTO");
                document.getElementById("CT_ID_LLAMADA").value = objcte.getAttribute("id_llamada");
                document.getElementById("CT_CONMUTADOR").value = objcte.getAttribute("CT_CONMUTADOR");
                document.getElementById("CT_CONTACTO_ENTRADA").value = objcte.getAttribute("CT_CONTACTO");
                llenarColonia();
                //ex participante
                if (objcte.getAttribute("cte_prosp") == "0") { //verde
                    document.getElementById("CT_CTE").innerHTML = "<table>"
                            + "<tr>"
                            + '<td class=\'ExParticipante\'><i class = "fa fa-user" title="EX-Participante" style="font-size:55px;"></i></td>'
                            + "</tr>"
                            + "</table>";
                } else { //gris
                    document.getElementById("CT_CTE").innerHTML = "<table>"
                            + "<tr>"
                            + '<td class=\'Prospecto\'><i class = "fa fa-user" title="Prospecto" style="font-size:55px;"></i></td>'
                            + "</tr>"
                            + "</table>";
                }
                LoadContacto(strIdcte);
            }
        }});
}
function OpnSalir() {
    $("#dialogCte").dialog("close");
}
function OpnDiagHCallD() {
    var strCte = document.getElementById("CT_NO_CLIENTE").value;
    Abrir_Link("COFIDE_Historial_llamadas.jsp?CT_ID= " + strCte, "_blank", 1000, 600, 0, 0);
}
function ReasignaDB(opc) {
    var strDb = document.getElementById("CT_DB").value;
    var strIdCte = document.getElementById("CT_NO_CLIENTE").value;
    if (opc == 1) { //reasigna
        if (strDb != "0") {
            document.getElementById("SioNO_inside").innerHTML = "¿Seguro de RE-ASIGNAR el registro?";
            $("#SioNO").dialog("open");
            document.getElementById("btnSI").onclick = function () {
                $("#SioNO").dialog("close");
                ReasignaElimina(opc, strIdCte, strDb)
                OpnSalir();
            };
            document.getElementById("btnNO").onclick = function () {
                $("#SioNO").dialog("close");
            };
        } else {
            alert("Selecciona la BASE destino");
        }
    }
    if (opc == 2) { //desactiva
        document.getElementById("SioNO_inside").innerHTML = "¿Seguro de ELIMINAR el registro?";
        $("#SioNO").dialog("open");
        document.getElementById("btnSI").onclick = function () {
            $("#SioNO").dialog("close");
            ReasignaElimina(opc, strIdCte)
            OpnSalir();
        };
        document.getElementById("btnNO").onclick = function () {
            $("#SioNO").dialog("close");
        };
    }
}
function ReasignaElimina(opc, strIdCte, strDB) {
    var strOpc = "";
    if (opc == 1) {
        strOpc = "REASIGNAR";
    } else {
        strOpc = "ELIMINAR";
    }
    var strPost = "";
    strPost = "opcion=" + opc;
    strPost += "&id_cte=" + strIdCte;
    if (opc == 1) {
        strPost += "&db_usr=" + strDB;
    }

    $.ajax({
        type: "POST",
        data: strPost,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "html",
        url: "COFIDE_ValFac_Duplicados.jsp?id=7",
        beforeSend: function () {
            $("#dialogWait").dialog("open");
        },
        success: function (datos) {
            datos = trim(datos);
            if (datos == "OK") {
                if (opc == 1) {
                    alert("Exito al " + strOpc + " el ID: " + strIdCte + " de la base: " + strDB);
                } else {
                    alert("Exito al " + strOpc + " el ID: " + strIdCte);
                }
                initSetCtBase();
//                getVtasDuplicadas();
            } else {
                alert("Ocurrio un problema al " + strOpc + " el registro \nintentalo nuevamente");
            }
            $("#dialogWait").dialog("close");
        },
        error: function (objeto, quepaso, otroobj) {
            alert(":pto=7:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }
    });
}
function opnDetalleCte() {
    var grid = jQuery("#GRD_DUPLICADOS");
    var ids = grid.getGridParam("selrow");
    var intIdTkt = 0;
    if (ids != null) {
        var lstRow = grid.getRowData(ids);
        document.getElementById("tmp_ct_id").value = lstRow.VL_ID;
        opnCteDetalleVald();
    } else {
        alert("Seleccione un registro en la lista.");
    }
}
function SalirVta() {
    $("#dialog").dialog("close");
    getVtasDuplicadas();
}

function salir() { //salir a menu principal, valida pagos duplicados
    myLayout.open("west");
    myLayout.open("east");
    myLayout.open("south");
    myLayout.open("north");
    document.getElementById("MainPanel").innerHTML = "";
    document.getElementById("rightPanel").innerHTML = "";
    var objMainFacPedi = objMap.getScreen("VAL_DATOS");
    objMainFacPedi.bolActivo = false;
    objMainFacPedi.bolMain = false;
    objMainFacPedi.bolInit = false;
    objMainFacPedi.idOperAct = 0;
    clearTimeout(timerLoad);
}

//function validaDetalleVta() {
//    var bolBase = false; // no es la misma base    
//    var grid = jQuery("#GRD_DUPLICADOS");
//    var idArr = grid.getDataIDs();
//    for (var i = 0; i < idArr.length; i++) {
//        //valida las bases con conflicto, si es la misma, manda true
//        var id = idArr[i];
//        var lstRow = grid.getRowData(id);
//        console.log(lstRow.VL_CT_BASE + " vs " + strDBEjecutivo);
//        if (lstRow.VL_CT_BASE != strDBEjecutivo) {
//            bolBase = true;
//            break;
//        }
//    }
//    if (bolBase) {
//        msgAlert("Antes de confirmar la venta, verifica los registros con duplicidad.", "La Venta pertenece a la BASE: \" " + strDBEjecutivo + " \".", "");
//    } else {
//        //confirma vta
//        alert("confirma la venta");
//        strDBEjecutivo = "";
//    }
//}

function dblClickCTE_Deta(id) {
    var strNomMain = objMap.getNomMain();
    var grid = jQuery("#GR_VTAS_DP"); //nombre del grid detalle
    var lstVal = grid.getRowData(id);
    if (strNomMain == "GR_VAL_DATOS") { //pantalla que lo contiene
        OpnEdit(document.getElementById("Ed" + strNomMain));
    } else {
        if (strNomMain == "VAL_DATOS") {
            window.open("COFIDE_cte_deta.jsp?id_cte=" + lstVal.VL_ID_OLD, '_blank');
        }
    }
}
