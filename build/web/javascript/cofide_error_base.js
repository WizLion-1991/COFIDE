function cofide_error_base() {

}
function initEditClas() {
    var grid = jQuery("#ERR_GRD");
    var id = grid.getGridParam("selrow");
    var lstRow = grid.getRowData(id);
    document.getElementById("ERRL_NOTA").value = lstRow.ERRD_OBSER;
    document.getElementById("ERRL_CLASSD").value = lstRow.ERRD_CLASS;

}
function opnEditClass() {
    var grid = jQuery("#ERR_GRD");
    var id = grid.getGridParam("selrow");
    if (id != null) {
        var lstRow = grid.getRowData(id);
        var strCerrado = lstRow.ERRD_EDIT;
        if (strCerrado != "1") {
            var objSecModiVta = objMap.getScreen("ERR_EDIT");
            if (objSecModiVta != null) {
                objSecModiVta.bolActivo = false;
                objSecModiVta.bolMain = false;
                objSecModiVta.bolInit = false;
                objSecModiVta.idOperAct = 0;
            }
            OpnOpt('ERR_EDIT', '_ed', 'dialog', false, false, true);
            document.getElementById("ERRL_NOTA").value = lstRow.ERRD_OBSER;
            document.getElementById("ERRL_CLASSD").value = lstRow.ERRD_CLASS;
        } else {
            alert("Este registro ya ha sido cerrado, no se puede editar");
        }
    } else {
        alert("Seleccione un registro!");
    }
}
function consultaLista(intOk) {
    var itemIdCobCofPer = 0;
    var strListado = "";
    var strLlamar = "";
    var strMes = document.getElementById("ERR_MES").value;
    var strAnio = document.getElementById("ERR_ANIO").value;
    var strPost = "";
    if (strAnio != "") {
        strPost = "mes=" + strMes;
        strPost += "&anio=" + strAnio;
        $("#dialogWait").dialog("open");
        jQuery("#ERR_GRD").clearGridData();
        $.ajax({
            type: "POST",
            data: strPost,
            scriptCharset: "utf-8",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "xml",
            url: "COFIDE_Error_base.jsp?id=1",
            success: function (datos) {
                var objsc = datos.getElementsByTagName("vta")[0];
                var lstPart = objsc.getElementsByTagName("datos");
                for (var i = 0; i < lstPart.length; i++) {
                    var obj = lstPart[i];
                    strListado = obj.getAttribute('listado');
                    strLlamar = "<b><a href= 'sip:" + obj.getAttribute('telefono') + "'>" + obj.getAttribute('telefono') + "</a></b>"
                    var dataRow = {
                        ERRD_LIS: obj.getAttribute('listado'),
                        ERRD_EDIT: obj.getAttribute('editado'),
                        ERRD_EJECUTIVO: obj.getAttribute('ejecutivo'),
                        ERRD_ID: obj.getAttribute('id_cte'),
                        ERRD_RAZON: obj.getAttribute('razon'),
                        ERRD_TELEFONO: strLlamar,
                        ERRD_CLASS: obj.getAttribute('clasifica'),
                        ERRD_OBSER: obj.getAttribute('observacion')
                    };
                    itemIdCobCofPer++;
                    jQuery("#ERR_GRD").addRowData(itemIdCobCofPer, dataRow, "last");
                }
                if (intOk != 1) { //si es 1, es solo recarga, si no, si muestra la alerta
                    if (strListado == "1") {
                        alert("Ya ha sido listado");
                    }
                }
                $("#dialogWait").dialog("close");
            },
            error: function (objeto, quepaso, otroobj) {
                alert(":ptoExist:" + objeto + " " + quepaso + " " + otroobj);
                $("#dialogWait").dialog("close");
            }
        });
    } else {
        alert("Ingresa el año que se va a listar");
    }
}
function editRegistro() {
    var strPost = "";
    var grid = jQuery("#ERR_GRD");
    var id = grid.getGridParam("selrow");
    var lstRow = grid.getRowData(id);
    var strIdCte = lstRow.ERRD_ID;
    var strComent = document.getElementById("ERRL_NOTA").value;
    var strClas = document.getElementById("ERRL_CLASSD").value;
    strPost = "idCte=" + strIdCte;
    strPost += "&comentario=" + strComent;
    strPost += "&clasificacion=" + strClas;
    $.ajax({
        type: "POST",
        data: strPost,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "html",
        url: "COFIDE_Error_base.jsp?id=2",
        success: function (datos) {
            $("#dialog").dialog("close");
            consultaLista(1);
        },
        error: function (objeto, quepaso, otroobj) {
            alert(":ptoExist:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialog").dialog("close");
        }
    });
}
function ExitEdit() {
    $("#dialog").dialog("close");
}
function TerminaCap() {
    document.getElementById("SioNO_inside").innerHTML = "¿Estas seguro de cerrar la edición de este periodo?";
    $("#SioNO").dialog("open");
    document.getElementById("btnSI").onclick = function () {
        $("#SioNO").dialog("close");

        var strMes = document.getElementById("ERR_MES").value;
        var strAnio = document.getElementById("ERR_ANIO").value;
        var strPost = "";
        if (strAnio != "") {
            strPost = "mes=" + strMes;
            strPost += "&anio=" + strAnio;
            $("#dialogWait").dialog("open");
            $.ajax({
                type: "POST",
                data: strPost,
                scriptCharset: "utf-8",
                contentType: "application/x-www-form-urlencoded;charset=utf-8",
                cache: false,
                dataType: "html",
                url: "COFIDE_Error_base.jsp?id=3",
                success: function (datos) {
                    $("#dialogWait").dialog("close");
                    consultaLista(1);
                },
                error: function (objeto, quepaso, otroobj) {
                    alert(":ptoExist:" + objeto + " " + quepaso + " " + otroobj);
                    $("#dialogWait").dialog("close");
                }
            });
        } else {
            alert("Ingresa el año que se va a listar");
        }
    };
    document.getElementById("btnNO").onclick = function () {
        $("#SioNO").dialog("close");
    };
}
function printErroBase() {
    var strMes = document.getElementById("ERR_MES").value;
    var strEquipo = "";
    var strAnio = document.getElementById("ERR_ANIO").value;
    $("#dialogWait").dialog("open");
    $.ajax({
        type: "POST",
        data: "",
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "html",
        url: "COFIDE_Error_base.jsp?id=4",
        success: function (datos) {
            $("#dialogWait").dialog("close");
            strEquipo = trim(datos);
            Abrir_Link("JasperReport?REP_ID=517&boton_1=PDF&strGrupo=" + strEquipo + "&strMes=" + strMes + "&strAnio=" + strAnio, '_reporte', 500, 600, 0, 0);
        },
        error: function (objeto, quepaso, otroobj) {
            alert(":ptoExist:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }
    });
}