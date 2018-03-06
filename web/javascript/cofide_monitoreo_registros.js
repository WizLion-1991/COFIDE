/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
function cofide_monitoreo_registros() {

}

function initMonitoreoRegistros() {

}

function getConsultaMonitoreo() {
    var strPost = document.getElementById("SELECT_FILTRO").value;
    if (strPost != "0") {
        $("#dialogWait").dialog("open");
        $.ajax({
            type: "POST",
            data: "tipo_filtro=" + strPost,
            scriptCharset: "UTF-8",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "xml",
            url: "COFIDE_MonitoreoRegistro.jsp?ID=2",
            success: function (datos) {
                jQuery("#VIEW_MONITOREO").clearGridData();
                var objHoraCombo = document.getElementById("TEXT_FILTRO");
                select_clear(objHoraCombo);
                var lstXml = datos.getElementsByTagName("tipo_filtro")[0];
                var lstCte = lstXml.getElementsByTagName("datos");
                select_add(objHoraCombo, "Seleccione...", "");
                for (var i = 0; i < lstCte.length; i++) {
                    var objHora = lstCte[i];
                    if (objHora.getAttribute("TEXT_FILTRO") != "") {
                        select_add(objHoraCombo, objHora.getAttribute("TEXT_FILTRO"), objHora.getAttribute("TEXT_FILTRO"));
                    }
                }
                $("#dialogWait").dialog("close");
            }});
    } else {
        select_clear(document.getElementById("TEXT_FILTRO"));
        select_add(document.getElementById("TEXT_FILTRO"), "Seleccione...", "");
    }
}

function searchMonitoreo() {
    var itemIdCob = 0;
    var strTextoBusqueda = document.getElementById("TEXT_FILTRO").value;
    var strFiltro = document.getElementById("SELECT_FILTRO").value;
    var strPOST = "&strTextoBusqueda=" + strTextoBusqueda;
    strPOST += "&filtro=" + strFiltro;
    if (strFiltro != "0") {
        if (strTextoBusqueda != "") {
            $("#dialogWait").dialog("open");
            $.ajax({
                type: "POST",
                data: strPOST,
                scriptCharset: "utf-8",
                contentType: "application/x-www-form-urlencoded;charset=utf-8",
                cache: false,
                dataType: "xml",
                url: "COFIDE_MonitoreoRegistro.jsp?ID=1",
                success: function (datos) {
                    jQuery("#VIEW_MONITOREO").clearGridData();
                    var objsc = datos.getElementsByTagName("MonitoreoRegistros")[0];
                    var lstVtas = objsc.getElementsByTagName("datos");
                    for (var i = 0; i < lstVtas.length; i++) {
                        var obj = lstVtas[i];
                        var Row = {
                            nombre_usuario: obj.getAttribute("nombre_usuario"),
                            COFIDE_CODIGO: obj.getAttribute("COFIDE_CODIGO"),
                            PROSPECTOS: obj.getAttribute("PROSPECTOS"),
                            EX_PARTICIPANTES: obj.getAttribute("EX_PARTICIPANTES")
                        };
                        itemIdCob++;
                        jQuery("#VIEW_MONITOREO").addRowData(itemIdCob, Row, "last");
                    }
                    $("#dialogWait").dialog("close");
                }, error: function (objeto, quepaso, otroobj) {
                    alert(":searchMonitoreo:" + objeto + " " + quepaso + " " + otroobj);
                    $("#dialogWait").dialog("close");
                }});
        } else {
            alert("Seleccione una descripcion.");
            document.getElementById("TEXT_FILTRO").focus();
        }
    } else {
        alert("Seleccione un Filtro.");
        document.getElementById("SELECT_FILTRO").focus();
    }
}