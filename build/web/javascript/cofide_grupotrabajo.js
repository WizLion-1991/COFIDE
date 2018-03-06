function cofide_grupotrabajo() {
}

//Inicializa la pantalla metas cofide
function initMetasCofide() {
    getMetasMensualCofide();
}//Fin initMetasCofide

//Obtiene la consulta de las metas del mes y aÃ±o seleccionados
function getMetasMensualCofide() {
    var strAnio = document.getElementById("MET_ANIO").value;
    var strMes = document.getElementById("MET_MES").value;
    $("#dialogWait").dialog("open");
    $.ajax({
        type: "POST",
        data: "strMes=" + strMes + "&strAnio=" + strAnio,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "COFIDE_GrupoTrabajo.jsp?ID=1",
        success: function (datos) {
            jQuery("#GRD_META").clearGridData();
            var lstXml = datos.getElementsByTagName("ReporteMetasMes")[0];
            var lstCom = lstXml.getElementsByTagName("datos");

            for (var i = 0; i < lstCom.length; i++) {
                var obj = lstCom[i];
                var rowCobranza = {
                    CMU_CONTADOR: getMaxGridRepoMetas("#GRD_META"),
                    TMK_CLAVE: obj.getAttribute("TMK_CLAVE"),
                    EJECUTIVO: obj.getAttribute("EJECUTIVO"),
                    CMU_NUEVO_CTE: obj.getAttribute("CMU_NUEVO_CTE"),
                    ExParticipantes: obj.getAttribute("ExParticipantes"),
                    CMU_IMPORTE: obj.getAttribute("CMU_IMPORTE"),
                    CMU_ID: obj.getAttribute("CMU_ID"),
                    id_usuarios: obj.getAttribute("id_usuarios")
                };
                jQuery("#GRD_META").addRowData(getMaxGridRepoMetas("#GRD_META"), rowCobranza, "last");
            }//Fin FOR
            getMetasMensualSupervisorCofide();
            $("#dialogWait").dialog("close");
        },
        error: function (objeto, quepaso, otroobj) {
            alert(":pto9:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }
    });
}//Fin getMetasMensualCofide

//Obtiene el valor maximo de un GRID
function getMaxGridRepoMetas(strNomGr) {
    var intLenght = jQuery(strNomGr).getDataIDs().length + 1;
    return intLenght;
}//Fin getMaxGridCursosMaterial

//Agrega metas mensuales a ejecutivo
function guardaMetaCofide() {
    var strPost = "";
    strPost += "&MET_ANIO=" + document.getElementById("MET_ANIO").value;
    strPost += "&MET_MES=" + document.getElementById("MET_MES").value;
    strPost += "&nombre_usuario=" + document.getElementById("nombre_usuario").value;
    strPost += "&CMU_IMPORTE=" + document.getElementById("CMU_IMPORTE").value;
    strPost += "&CMU_NUEVO_CTE=" + document.getElementById("CMU_NUEVO_CTE").value;

    if (document.getElementById("CMU_ID").value != 0 || document.getElementById("CMU_ID").value != "") {
        strPost += "&CMU_ID=" + document.getElementById("CMU_ID").value;
    }
    $("#dialogWait").dialog("open");
    if (document.getElementById("MET_MES").value != 0) {
        $.ajax({
            type: "POST",
            data: strPost,
            scriptCharset: "UTF-8",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "html",
            url: "COFIDE_GrupoTrabajo.jsp?ID=2",
            success: function (datos) {
                if (datos.substring(0, 2) == "OK") {
                    getMetasMensualCofide();
                    cleanFieldCofideMetas();
                    document.getElementById("CMU_ID").value = "";
                    $("#dialogWait").dialog("close");
                } else {
                    $("#dialogWait").dialog("close");
                    alert(datos);
                }
                $("#dialogWait").dialog("close");
            }
        }); //fin del ajax
    } else {
        $("#dialogWait").dialog("close");
        alert("Selecciona un Mes.");
        document.getElementById("MET_MES").focus();
    }
}//Fin guardaMetaCofide

//Editar una partida de Metas Mensuales
function getDatosMetaCofide() {
    var grid = jQuery("#GRD_META");
    var ids = grid.getGridParam("selrow");
    if (ids !== null) {
        var lstRow = grid.getRowData(ids);
        var intId = lstRow.CMU_ID;
        $("#dialogWait").dialog("open");
        var strPost = "ID_REGISTRO=" + intId;
        $.ajax({
            type: "POST",
            data: strPost,
            scriptCharset: "utf-8",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "xml",
            url: "COFIDE_GrupoTrabajo.jsp?ID=3",
            success: function (datos) {
                var lstXml = datos.getElementsByTagName("ReporteMetasMesEdita")[0];
                var lstCom = lstXml.getElementsByTagName("datos");

                for (var i = 0; i < lstCom.length; i++) {
                    var obj = lstCom[i];
                    document.getElementById("CMU_IMPORTE").value = obj.getAttribute("CMU_IMPORTE");
                    document.getElementById("CMU_NUEVO_CTE").value = obj.getAttribute("CMU_NUEVO_CTE");
                    document.getElementById("nombre_usuario").value = obj.getAttribute("id_usuarios");
                    document.getElementById("CMU_ID").value = obj.getAttribute("CMU_ID");
                }//Fin FOR

                $("#dialogWait").dialog("close");
            },
            error: function (objeto, quepaso, otroobj) {
                alert(":pto9:" + objeto + " " + quepaso + " " + otroobj);
                $("#dialogWait").dialog("close");
            }
        });
    } else {
        alert("Selecciona Algun Registro!");
    }
}//Fin getDatosMetacofide

//Elimina metas mensuales a ejecutivo
function eliminaMetaCofide() {
    var strPost = "";
    var grid = jQuery("#GRD_META");
    var ids = grid.getGridParam("selrow");
    if (ids !== null) {
        var lstRow = grid.getRowData(ids);
        var intId = lstRow.CMU_ID;
        $("#dialogWait").dialog("open");
        strPost += "&CMU_ID=" + intId;
        $("#dialogWait").dialog("open");
        $.ajax({
            type: "POST",
            data: strPost,
            scriptCharset: "UTF-8",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "html",
            url: "COFIDE_GrupoTrabajo.jsp?ID=5",
            success: function (datos) {
                if (datos.substring(0, 2) == "OK") {
                    getMetasMensualCofide();
                    cleanFieldCofideMetas();
                    document.getElementById("CMU_ID").value = "";
                    $("#dialogWait").dialog("close");
                } else {
                    $("#dialogWait").dialog("close");
                    alert(datos);
                }
                $("#dialogWait").dialog("close");
            }
        }); //fin del ajax
    } else {
        alert("Selecciona Algun Registro!");
    }
}//Fin eliminaMetaCofide

//Consulta las Metas de los Supervisores
function getMetasMensualSupervisorCofide(){
    var strAnio = document.getElementById("MET_ANIO").value;
    var strMes = document.getElementById("MET_MES").value;
    $("#dialogWait").dialog("open");
    $.ajax({
        type: "POST",
        data: "strMes=" + strMes + "&strAnio=" + strAnio,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "COFIDE_GrupoTrabajo.jsp?ID=4",
        success: function (datos) {
            jQuery("#GRD_METAG").clearGridData();
            var lstXml = datos.getElementsByTagName("ReporteMetasMesSupervisor")[0];
            var lstCom = lstXml.getElementsByTagName("datos");

            for (var i = 0; i < lstCom.length; i++) {
                var obj = lstCom[i];
                var rowCobranza = {
                    METD_ID: getMaxGridRepoMetas("#GRD_METAG"),
                    METD_ID_GPO: obj.getAttribute("EJ_BASE"),
                    METD_IMP: obj.getAttribute("MG_IMPORTE"),
                    METD_NVO: obj.getAttribute("MG_NUEVO_CT")
                };
                jQuery("#GRD_METAG").addRowData(getMaxGridRepoMetas("#GRD_METAG"), rowCobranza, "last");
            }//Fin FOR
            $("#dialogWait").dialog("close");
        },
        error: function (objeto, quepaso, otroobj) {
            alert(":pto9:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }
    });
}//Fin getMetasMensualSupervisorCofide


//Limpia los campos de texto en pantalla
function cleanFieldCofideMetas(){
    document.getElementById("nombre_usuario").value = 0;
    document.getElementById("CMU_IMPORTE").value = 0;
    document.getElementById("CMU_NUEVO_CTE").value = 0;
    document.getElementById("CMU_ID").value = "";
}//Fin cleanFieldCofideMetas

/*Realiza la suma de el reporte de metas supervisor*/
function sumaRepoMetasSupervisor() {
   var grid = jQuery("#GRD_METAG");
   var dblTotalImporte = 0;
   var dblTotalNvos = 0;
   var arr = grid.getDataIDs();
   if (arr != null) {
      for (var i = 0; i < arr.length; i++) {
         var id = arr[i];
         var lstVal = grid.getRowData(id);
         dblTotalImporte = dblTotalImporte + parseFloat(lstVal.METD_IMP);
         dblTotalNvos = dblTotalNvos + parseFloat(lstVal.METD_NVO);         

      }
   }
   /*Ponemos el total en el pie de las columnas*/
   grid.footerData('set', {METD_ID_GPO: "TOTAL", METD_IMP: dblTotalImporte, METD_NVO: dblTotalNvos});
}//Fin sumaRepoMetasSupervisor