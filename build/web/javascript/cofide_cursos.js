function cofide_cursos() {

}
function initCurso2() {
    LoadDatosCurso();
    initValCursoSemDip();
}
function colorPiker() {
    var strColorPiker = '<script src="jscolor.js"></script>'
            + '<input size= "30px" id="CSH_COLOR" class="jscolor" value="ab2567">';
    document.getElementById("CSH_COLOR").innerHTML = strColorPiker;
}

function initCofCursos() {
    document.getElementById("LBL_FECHA2").parentNode.parentNode.style.display = 'none';
    document.getElementById("CC_FECHA_INICIAL2").parentNode.parentNode.style.display = 'none';
    document.getElementById("CC_SEDE_ID2").parentNode.parentNode.style.display = 'none';
    document.getElementById("CC_ALIAS_FEC2").parentNode.parentNode.style.display = 'none';
    document.getElementById("CC_SALON2").parentNode.parentNode.style.display = 'none';
    document.getElementById("CC_SESION2").parentNode.parentNode.style.display = 'none';
    document.getElementById("CC_HR_EVENTO_INI2").parentNode.parentNode.style.display = 'none';
    document.getElementById("CC_HR_EVENTO_FIN2").parentNode.parentNode.style.display = 'none';
    document.getElementById("CC_DURACION_HRS2").parentNode.parentNode.style.display = 'none';
    document.getElementById("CC_TIPO_ALIMENTO2").parentNode.parentNode.style.display = 'none';
    document.getElementById("CC_ALIMENTO2").parentNode.parentNode.style.display = 'none';

    document.getElementById("LBL_FECHA3").parentNode.parentNode.style.display = 'none';
    document.getElementById("CC_FECHA_INICIAL3").parentNode.parentNode.style.display = 'none';
    document.getElementById("CC_SEDE_ID3").parentNode.parentNode.style.display = 'none';
    document.getElementById("CC_ALIAS_FEC3").parentNode.parentNode.style.display = 'none';
    document.getElementById("CC_SALON3").parentNode.parentNode.style.display = 'none';
    document.getElementById("CC_SESION3").parentNode.parentNode.style.display = 'none';
    document.getElementById("CC_HR_EVENTO_INI3").parentNode.parentNode.style.display = 'none';
    document.getElementById("CC_HR_EVENTO_FIN3").parentNode.parentNode.style.display = 'none';
    document.getElementById("CC_DURACION_HRS3").parentNode.parentNode.style.display = 'none';
    document.getElementById("CC_TIPO_ALIMENTO3").parentNode.parentNode.style.display = 'none';
    document.getElementById("CC_ALIMENTO3").parentNode.parentNode.style.display = 'none';
}

function beginModiCursos() {
    if (document.getElementById("CC_FECHA_INICIAL2").value == "") {
        document.getElementById("LBL_FECHA2").parentNode.parentNode.style.display = 'none';
        document.getElementById("CC_FECHA_INICIAL2").parentNode.parentNode.style.display = 'none';
        document.getElementById("CC_SEDE_ID2").parentNode.parentNode.style.display = 'none';
        document.getElementById("CC_ALIAS_FEC2").parentNode.parentNode.style.display = 'none';
        document.getElementById("CC_SALON2").parentNode.parentNode.style.display = 'none';
        document.getElementById("CC_SESION2").parentNode.parentNode.style.display = 'none';
        document.getElementById("CC_HR_EVENTO_INI2").parentNode.parentNode.style.display = 'none';
        document.getElementById("CC_HR_EVENTO_FIN2").parentNode.parentNode.style.display = 'none';
        document.getElementById("CC_DURACION_HRS2").parentNode.parentNode.style.display = 'none';
        document.getElementById("CC_TIPO_ALIMENTO2").parentNode.parentNode.style.display = 'none';
        document.getElementById("CC_ALIMENTO2").parentNode.parentNode.style.display = 'none';

        document.getElementById("LBL_FECHA3").parentNode.parentNode.style.display = 'none';
        document.getElementById("CC_FECHA_INICIAL3").parentNode.parentNode.style.display = 'none';
        document.getElementById("CC_SEDE_ID3").parentNode.parentNode.style.display = 'none';
        document.getElementById("CC_ALIAS_FEC3").parentNode.parentNode.style.display = 'none';
        document.getElementById("CC_SALON3").parentNode.parentNode.style.display = 'none';
        document.getElementById("CC_SESION3").parentNode.parentNode.style.display = 'none';
        document.getElementById("CC_HR_EVENTO_INI3").parentNode.parentNode.style.display = 'none';
        document.getElementById("CC_HR_EVENTO_FIN3").parentNode.parentNode.style.display = 'none';
        document.getElementById("CC_DURACION_HRS3").parentNode.parentNode.style.display = 'none';
        document.getElementById("CC_TIPO_ALIMENTO3").parentNode.parentNode.style.display = 'none';
        document.getElementById("CC_ALIMENTO3").parentNode.parentNode.style.display = 'none';
    }
}

function initCursoEtapas() {
    $('#CC_FECHA_INICIAL').datepicker("disable");
}
function OpnDiagptsSed() {
    var objSecModiVta = objMap.getScreen("H_SEDES");
    if (objSecModiVta != null) {
        objSecModiVta.bolActivo = false;
        objSecModiVta.bolMain = false;
        objSecModiVta.bolInit = false;
        objSecModiVta.idOperAct = 0;
    }
    OpnOpt("H_SEDES", "grid", "dialogCte", false, false);
}
function OpnDiagptsInts() {
    var objSecModiVta = objMap.getScreen("C_INSTRUCTORES");
    if (objSecModiVta != null) {
        objSecModiVta.bolActivo = false;
        objSecModiVta.bolMain = false;
        objSecModiVta.bolInit = false;
        objSecModiVta.idOperAct = 0;
    }
    OpnOpt("C_INSTRUCTORES", "grid", "dialogCte", false, false);
}

var itemIdCof = 0;
function validaCurso(opc) {
    var strHoraInicio = "";
    var strHoraFin = "";
    var strDuracion = "";
    var intHorasTotal = 0;
    var intHoraInicial = 0;
    var intHoraFinal = 0;
    var strSeparador = " a ";
    var strSesion = "";
    var strSesionF = "";
    var tfinal = new Date();
    var tinicial = new Date();
    var tDiferecial = new Date();
    if (opc == 1) {
        strHoraInicio = document.getElementById("CC_HR_EVENTO_INI").value;
        strHoraFin = document.getElementById("CC_HR_EVENTO_FIN").value;
        strDuracion = "CC_DURACION_HRS";
        strSesionF = "CC_SESION";
    }
    if (opc == 2) {
        strHoraInicio = document.getElementById("CC_HR_EVENTO_INI2").value;
        strHoraFin = document.getElementById("CC_HR_EVENTO_FIN2").value;
        strDuracion = "CC_DURACION_HRS2";
        strSesionF = "CC_SESION2";
    }
    if (opc == 3) {
        strHoraInicio = document.getElementById("CC_HR_EVENTO_INI3").value;
        strHoraFin = document.getElementById("CC_HR_EVENTO_FIN3").value;
        strDuracion = "CC_DURACION_HRS3";
        strSesionF = "CC_SESION3";
    }
    if (strHoraInicio != strHoraFin) {
        intHoraInicial = parseInt(strHoraInicio);
        intHoraFinal = parseInt(strHoraFin);

        strHoraInicio = strHoraInicio.split(":");
        strHoraFin = strHoraFin.split(":");

        tfinal.setHours(strHoraFin[0], strHoraFin[1]);
        tinicial.setHours(strHoraInicio[0], strHoraInicio[1]);
        tDiferecial.setHours(tfinal.getHours() - tinicial.getHours(), tfinal.getMinutes() - tinicial.getMinutes());

        var intHrsDiferencia = tDiferecial.getHours();
        if (tDiferecial.getHours() >= "8") { //si el curso es mayor a 8 horas, se le resta la hora de comida.  
            intHrsDiferencia = tDiferecial.getHours() - 1;
        }

        if (tDiferecial.getMinutes() != "0" || tDiferecial.getMinutes() != "00") {
            intHorasTotal = intHrsDiferencia + ":" + tDiferecial.getMinutes(); //HH:MM
        } else {
            intHorasTotal = intHrsDiferencia;
        }

        strSesion = strHoraInicio[0] + ":" + strHoraInicio[1] + strSeparador + strHoraFin[0] + ":" + strHoraFin[1] + " Hrs."

        document.getElementById(strDuracion).value = intHorasTotal;
        document.getElementById(strSesionF).value = strSesion;
    } else {
        alert("No puede ser a la misma");
    }
}
function confirmaCurso() {
    var grid = jQuery("#V_CURSOS");
    var ids = grid.getGridParam("selrow");
    if (ids !== null) {
        var lstRow1 = grid.getRowData(ids);
        var intIdCC = lstRow1.CC_CURSO_ID;
        var intActivo = lstRow1.CC_ACTIVO;
        if (intActivo == "SI") {
            var strPost = "";
            strPost += "&CC_CURSO_ID=" + intIdCC;
            $.ajax({
                type: "POST",
                data: strPost,
                scriptCharset: "utf-8",
                contentType: "application/x-www-form-urlencoded;charset=utf-8",
                cache: false,
                dataType: "xml",
                url: "COFIDE_Cursos.jsp?id=1",
                success: function (datos) {
                    jQuery("#V_CURSOS").clearGridData();
                    var objsc = datos.getElementsByTagName("Confirmar")[0];
                    var lstProds = objsc.getElementsByTagName("Confirmar_deta");
                    for (var i = 0; i < lstProds.length; i++) {
                        var obj = lstProds[i];
                        if (obj.getAttribute("respuesta") != "true") {
                            alert("Hubo un error al Confirmar");
                        } else {
                            grid.trigger("reloadGrid");
                        }
                    }
                    grid.trigger("reloadGrid");
                    alert("Confirmado");
                }, error: function () {
                    alert("No hay Datos");
                    grid.trigger("reloadGrid");
                }});
        } else {
            alert("El Curso Debe Estar Activo!");
        }
    } else {
        alert("Selecciona un elemento en el grid");
    }
}
function autorizaCurso() {
    var grid = jQuery("#V_CURSOS");
    var ids = grid.getGridParam("selrow");
    if (ids !== null) {
        var lstRow1 = grid.getRowData(ids);
        var intIdCC = lstRow1.CC_CURSO_ID;
        var intConfirmado = lstRow1.CC_CONFIRMAR;
        if (intConfirmado == "SI") {
            var strPost = "";
            strPost += "&CC_CURSO_ID=" + intIdCC;
            strPost += "&publica=0";
            $.ajax({
                type: "POST",
                data: strPost,
                scriptCharset: "utf-8",
                contentType: "application/x-www-form-urlencoded;charset=utf-8",
                cache: false,
                dataType: "xml",
                url: "COFIDE_Cursos.jsp?id=2",
                success: function (datos) {
                    jQuery("#V_CURSOS").clearGridData();
                    var objsc = datos.getElementsByTagName("Autorizar")[0];
                    var lstProds = objsc.getElementsByTagName("Autorizar_deta");
                    for (var i = 0; i < lstProds.length; i++) {
                        var obj = lstProds[i];
                        if (obj.getAttribute("respuesta") != "true") {
                            alert("Hubo un error al Autorizar");
                        } else {
                            grid.trigger("reloadGrid");
                        }
                    }
                    grid.trigger("reloadGrid");
                    alert("Autorizado");
                }, error: function () {
                    alert("No hay Datos");
                    grid.trigger("reloadGrid");
                }});
        } else {
            alert("El Curso Debe Ser Confirmado Antes de Autorizar");
        }
    } else {
        alert("Selecciona un elemento en el grid");
    }
}
function CalcIvaPres() {
    var strPrecioUnitPres = d.getElementById("CC_PRECIO_PRES").value;
    var tax = new Impuestos(dblTasa1, dblTasa2, dblTasa3, intSImp1_2, intSImp1_3, intSImp2_3);
    tax.CalculaImpuestoMas(strPrecioUnitPres);
    var dblIva = parseFloat(this.dblImpuesto1 = tax.dblImpuesto1) + parseFloat(strPrecioUnitPres);
    document.getElementById("CC_IVA_PRES").value = dblIva;
}
function CalcIvaOn() {
    var strPrecioUnitOn = d.getElementById("CC_PRECIO_ON").value;
    var tax = new Impuestos(dblTasa1, dblTasa2, dblTasa3, intSImp1_2, intSImp1_3, intSImp2_3);
    tax.CalculaImpuestoMas(strPrecioUnitOn);
    var dblIva = parseFloat(this.dblImpuesto1 = tax.dblImpuesto1) + parseFloat(strPrecioUnitOn);
    document.getElementById("CC_IVA_ON").value = dblIva;
}
function CalcIvaVid() {
    var strPrecioUnitVid = d.getElementById("CC_PRECIO_VID").value;
    var tax = new Impuestos(dblTasa1, dblTasa2, dblTasa3, intSImp1_2, intSImp1_3, intSImp2_3);
    tax.CalculaImpuestoMas(strPrecioUnitVid);
    var dblIva = parseFloat(this.dblImpuesto1 = tax.dblImpuesto1) + parseFloat(strPrecioUnitVid);
    document.getElementById("CC_IVA_VID").value = dblIva;
}
function OpnDiagCursos() {
    var objSecModiVta = objMap.getScreen("CAT_CURSO");
    if (objSecModiVta != null) {
        objSecModiVta.bolActivo = false;
        objSecModiVta.bolMain = false;
        objSecModiVta.bolInit = false;
        objSecModiVta.idOperAct = 0;
    }
    OpnOpt("CAT_CURSO", "grid", "dialog", false, false, true);
}
function dblClickCurso(id) {
    var strNomMain = objMap.getNomMain();
    var grid = jQuery("#CAT_CURSO");
    var lstVal = grid.getRowData(id);
    if (strNomMain == "CAT_CURSO") {
        OpnEdit(document.getElementById("Ed" + strNomMain));
    } else {
        if (strNomMain == "E2_CURSOS") {
            document.getElementById("CC_NOMBRE_CURSO").value = lstVal.CCU_CURSO;
            document.getElementById("CC_CLAVES").value = lstVal.CCU_CLAVE;
            $("#dialog").dialog("close");
        }
    }
}
function OpnDiagGiro() {
    var objSecModiVta = objMap.getScreen("CAT_GIRO");
    if (objSecModiVta != null) {
        objSecModiVta.bolActivo = false;
        objSecModiVta.bolMain = false;
        objSecModiVta.bolInit = false;
        objSecModiVta.idOperAct = 0;
    }
    OpnOpt("CAT_GIRO", "grid", "dialog2", false, false, true);
}
function dblClickGiro(id) {
    var strNomMain = objMap.getNomMain();
    var grid = jQuery("#CAT_GIRO");
    var lstVal = grid.getRowData(id);
    if (strNomMain == "CAT_GIRO") {
        OpnEdit(document.getElementById("Ed" + strNomMain));
    } else {
        if (strNomMain == "V_CURSOS") {
            document.getElementById("CC_GIRO").value = lstVal.CG_GIRO;
            $("#dialog2").dialog("close");
        } else {
            if (strNomMain == "CAT_CURSO") {
                document.getElementById("CC_GIRO").value = lstVal.CG_GIRO;
                $("#dialog2").dialog("close");
            }
        }
    }
}
function OpnDiagArea() {
    var objSecModiVta = objMap.getScreen("CAT_SEG");
    if (objSecModiVta != null) {
        objSecModiVta.bolActivo = false;
        objSecModiVta.bolMain = false;
        objSecModiVta.bolInit = false;
        objSecModiVta.idOperAct = 0;
    }
    OpnOpt("CAT_SEG", "grid", "dialogTMK1", false, false, true);
}
function dblClickArea(id) {
    var strNomMain = objMap.getNomMain();
    var grid = jQuery("#CAT_SEG");
    var lstVal = grid.getRowData(id);
    if (strNomMain == "CAT_SEG") {
        OpnEdit(document.getElementById("Ed" + strNomMain));
    } else {
        if (strNomMain == "V_CURSOS") {
            document.getElementById("CC_SEGM").value = lstVal.CS_AREA;
            $("#dialogTMK1").dialog("close");
        } else {
            if (strNomMain == "CAT_CURSO") {
                document.getElementById("CC_SEGM").value = lstVal.CS_AREA;
                $("#dialogTMK1").dialog("close");
            }
        }
    }
}
function cancelarCurso() {
    var grid = jQuery("#V_CURSOS");
    var ids = grid.getGridParam("selrow");
    if (ids !== null) {
        var lstRow1 = grid.getRowData(ids);
        var intIdCC = lstRow1.CC_CURSO_ID;
        var strPost = "";
        strPost += "&CC_CURSO_ID=" + intIdCC;
        $.ajax({type: "POST", data: strPost, scriptCharset: "utf-8", contentType: "application/x-www-form-urlencoded;charset=utf-8", cache: false, dataType: "xml", url: "COFIDE_Cursos.jsp?id=3", success: function (datos) {
                jQuery("#V_CURSOS").clearGridData();
                var objsc = datos.getElementsByTagName("Cancelar")[0];
                var lstProds = objsc.getElementsByTagName("Cancelar_deta");
                for (var i = 0; i < lstProds.length; i++) {
                    var obj = lstProds[i];
                    var intInscritos = parseInt(obj.getAttribute("inscritos"));
                    if (intInscritos > 0) {
                        alert("Aun hay " + intInscritos + " Participante(s) inscrito(s) en este curso ");
                    }
                    if (obj.getAttribute("respuesta") != "true") {
                        alert("Hubo un error al Cancelar");
                    } else {
                        grid.trigger("reloadGrid");
                    }
                }
                grid.trigger("reloadGrid");
                alert("Curso Cancelado");
            }, error: function () {
                alert("No hay Datos");
                grid.trigger("reloadGrid");
            }});
    } else {
        alert("Selecciona un Curso");
    }
}
function LlenaDetaCurso() {
    document.getElementById("CCU_CLAVE").value = document.getElementById("CCU_ID_M").value;
    var intId_M = document.getElementById("CCU_ID_M").value;
    var intId = 0;
    var strCursoNew = "";
    var strCursoOld = "";
    var strFecha = "";
    var strClave = "";
    var strHora = "";
    var strUsr = "";
    var strPost = "CCU_ID_M=" + intId_M;
    $.ajax({
        type: "POST",
        data: strPost,
        scriptCharset: "UTF-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "COFIDE_Cursos.jsp?id=4",
        success: function (datos) {
            var lstXml = datos.getElementsByTagName("cofide_cursos")[0];
            var lstCte = lstXml.getElementsByTagName("datos");
            for (var i = 0; i < lstCte.length; i++) {
                var objcte = lstCte[i];
                intId = objcte.getAttribute("CUD_ID");
                strCursoNew = objcte.getAttribute("CUD_CURSO");
                strCursoOld = objcte.getAttribute("CUD_CURSO_OLD");
                strClave = objcte.getAttribute("CUD_CLAVE");
                strFecha = objcte.getAttribute("CUD_FECHA");
                strHora = objcte.getAttribute("CUD_HORA");
                strUsr = objcte.getAttribute("CUD_USUARIO");
                var datarow = {
                    CUD_ID: intId,
                    CUD_CURSO: strCursoNew,
                    CUD_CURSO_OLD: strCursoOld,
                    CUD_CLAVE: strClave,
                    CUD_FECHA: strFecha,
                    CUD_HORA: strHora,
                    CUD_USUARIO: strUsr
                };
                itemIdCof++;
                jQuery("#CCU_GRD").addRowData(itemIdCof, datarow, "last");
            }
//            LlenarDetallesCat();
            LlenaAreaCat();
        }});
}
function LlenaDetaGiro() {
    var intId_M = document.getElementById("CG_ID_M").value;
    var intId = 0;
    var strGiroNew = "";
    var strGiroOld = "";
    var strFecha = "";
    var strHora = "";
    var strUsr = "";
    var strPost = "CG_ID_M=" + intId_M;
    $.ajax({type: "POST", data: strPost, scriptCharset: "UTF-8", contentType: "application/x-www-form-urlencoded;charset=utf-8", cache: false, dataType: "xml", url: "COFIDE_Cursos.jsp?id=5", success: function (datos) {
            var lstXml = datos.getElementsByTagName("cofide_cursos")[0];
            var lstCte = lstXml.getElementsByTagName("datos");
            for (var i = 0; i < lstCte.length; i++) {
                var objcte = lstCte[i];
                intId = objcte.getAttribute("CGD_ID");
                strGiroNew = objcte.getAttribute("CGD_GIRO");
                strGiroOld = objcte.getAttribute("CGD_GIRO_OLD");
                strFecha = objcte.getAttribute("CGD_FECHA");
                strHora = objcte.getAttribute("CGD_HORA");
                strUsr = objcte.getAttribute("CGD_USUARIO");
                var datarow = {CGD_ID: intId, CGD_GIRO: strGiroNew, CGD_GIRO_OLD: strGiroOld, CGD_FECHA: strFecha, CGD_HORA: strHora, CGD_USUARIO: strUsr};
                itemIdCof++;
                jQuery("#CG_GRD").addRowData(itemIdCof, datarow, "last");
            }
        }});
}
function LlenaDetaArea() {
    var intId_M = document.getElementById("CS_ID_M").value;
    var intId = 0;
    var strAreaNew = "";
    var strAreaOld = "";
    var strFecha = "";
    var strHora = "";
    var strUsr = "";
    var strPost = "CS_ID_M=" + intId_M;
    $.ajax({type: "POST", data: strPost, scriptCharset: "UTF-8", contentType: "application/x-www-form-urlencoded;charset=utf-8", cache: false, dataType: "xml", url: "COFIDE_Cursos.jsp?id=6", success: function (datos) {
            var lstXml = datos.getElementsByTagName("cofide_cursos")[0];
            var lstCte = lstXml.getElementsByTagName("datos");
            for (var i = 0; i < lstCte.length; i++) {
                var objcte = lstCte[i];
                intId = objcte.getAttribute("CSD_ID");
                strAreaNew = objcte.getAttribute("CSD_AREA");
                strAreaOld = objcte.getAttribute("CSD_AREA_OLD");
                strFecha = objcte.getAttribute("CSD_FECHA");
                strHora = objcte.getAttribute("CSD_HORA");
                strUsr = objcte.getAttribute("CSD_USUARIO");
                var datarow = {CSD_ID: intId, CSD_AREA: strAreaNew, CSD_AREA_OLD: strAreaOld, CSD_FECHA: strFecha, CSD_HORA: strHora, CSD_USUARIO: strUsr};
                itemIdCof++;
                jQuery("#CS_GRD").addRowData(itemIdCof, datarow, "last");
            }
        }});
}
function AddGiro() {
    var giro = document.getElementById("CC_GIRO").value;
    if (giro != "") {
        var datarow = {CCG_ID: itemIdCof, CC_CURSO_ID: document.getElementById("CC_CURSO_ID").value, CC_GIRO: giro};
        itemIdCof++;
        jQuery("#CC_GRD_GIRO").addRowData(itemIdCof, datarow, "last");
        document.getElementById("CC_GIRO").value = "";
    } else {
        alert("Seleccione Una Opcion");
    }
}
function AddGiroCat() {
    var giro = document.getElementById("CC_GIRO").value;
    if (giro != "") {
        var datarow = {CCG_ID: itemIdCof, CC_CURSO_ID: document.getElementById("CCU_ID_M").value, CC_GIRO: giro};
        itemIdCof++;
        jQuery("#CC_GRD_GIRO").addRowData(itemIdCof, datarow, "last");
        document.getElementById("CC_GIRO").value = "";
    } else {
        alert("Seleccione Una Opcion");
    }
}
function AddArea() {
    var area = document.getElementById("CC_SEGM").value;
    if (area != "") {
        var datarow = {CCS_ID: itemIdCof, CC_CURSO_ID: document.getElementById("CC_CURSO_ID").value, CC_AREA: area};
        itemIdCof++;
        jQuery("#CC_GRD_AREA").addRowData(itemIdCof, datarow, "last");
        document.getElementById("CC_SEGM").value = "";
    } else {
        alert("Selecciona una Opcion");
    }
}
function AddAreaCat() {
    var area = document.getElementById("CC_SEGM").value;
    if (area != "") {
        var datarow = {CCS_ID: itemIdCof, CC_CURSO_ID: document.getElementById("CCU_ID_M").value, CC_AREA: area};
        itemIdCof++;
        jQuery("#CC_GRD_AREA").addRowData(itemIdCof, datarow, "last");
        document.getElementById("CC_SEGM").value = "";
    } else {
        alert("Selecciona una Opcion");
    }
}
function DelGiro() {
    var grid = jQuery("#CC_GRD_GIRO");
    if (grid.getGridParam("selrow") != null) {
        grid.delRowData(grid.getGridParam("selrow"));
    }
}
function DelArea() {
    var grid = jQuery("#CC_GRD_AREA");
    if (grid.getGridParam("selrow") != null) {
        grid.delRowData(grid.getGridParam("selrow"));
    }
}
function saveGiro() {
    var strPost = "";
    var intIdSave = 0;
    intIdSave = document.getElementById("CC_CURSO_ID").value;
    var grid = jQuery("#CC_GRD_GIRO");
    var idArr = grid.getDataIDs();
    strPost += "CC_CURSO_ID=" + intIdSave;
    for (var i = 0; i < idArr.length; i++) {
        var id = idArr[i];
        var lstRow = grid.getRowData(id);
        strPost += "&CC_GIRO" + i + "=" + lstRow.CC_GIRO + "";
    }
    strPost += "&length=" + idArr.length;
    $.ajax({type: "POST", data: strPost, scriptCharset: "utf-8", contentType: "application/x-www-form-urlencoded;charset=utf-8", cache: false, dataType: "html", url: "COFIDE_Cursos.jsp?id=7"});
    jQuery("#CC_GRD_GIRO").clearGridData();
}
function saveArea() {
    var strPost = "";
    var intIdSave = 0;
    intIdSave = document.getElementById("CC_CURSO_ID").value;
    var grid = jQuery("#CC_GRD_AREA");
    var idArr = grid.getDataIDs();
    strPost += "CC_CURSO_ID=" + intIdSave;
    for (var i = 0; i < idArr.length; i++) {
        var id = idArr[i];
        var lstRow = grid.getRowData(id);
        strPost += "&CC_AREA" + i + "=" + lstRow.CC_AREA + "";
    }
    strPost += "&length=" + idArr.length;
    $.ajax({type: "POST", data: strPost, scriptCharset: "utf-8", contentType: "application/x-www-form-urlencoded;charset=utf-8", cache: false, dataType: "html", url: "COFIDE_Cursos.jsp?id=8"});
    jQuery("#CC_GRD_AREA").clearGridData();
}
function saveAreaCatalogoCurso() {
    var strPost = "";
    var intIdSave = 0;
    intIdSave = document.getElementById("CCU_CLAVE").value;
    var grid = jQuery("#CC_GRD_AREA");
    var idArr = grid.getDataIDs();
    strPost += "CC_CURSO_ID=" + intIdSave;
    for (var i = 0; i < idArr.length; i++) {
        var id = idArr[i];
        var lstRow = grid.getRowData(id);
        strPost += "&CC_AREA" + i + "=" + lstRow.CC_AREA + "";
    }
    strPost += "&length=" + idArr.length;
    $.ajax({type: "POST", data: strPost, scriptCharset: "utf-8", contentType: "application/x-www-form-urlencoded;charset=utf-8", cache: false, dataType: "html", url: "COFIDE_Cursos.jsp?id=8"});
    jQuery("#CC_GRD_AREA").clearGridData();
}
function saveGiroCatalogoCurso() {
    var strPost = "";
    var intIdSave = 0;
    intIdSave = document.getElementById("CCU_CLAVE").value;
    var grid = jQuery("#CC_GRD_GIRO");
    var idArr = grid.getDataIDs();
    strPost += "CC_CURSO_ID=" + intIdSave;
    for (var i = 0; i < idArr.length; i++) {
        var id = idArr[i];
        var lstRow = grid.getRowData(id);
        strPost += "&CC_GIRO" + i + "=" + lstRow.CC_GIRO + "";
    }
    strPost += "&length=" + idArr.length;
    $.ajax({type: "POST", data: strPost, scriptCharset: "utf-8", contentType: "application/x-www-form-urlencoded;charset=utf-8", cache: false, dataType: "html", url: "COFIDE_Cursos.jsp?id=7"});
    jQuery("#CC_GRD_GIRO").clearGridData();
}
function DoSave() {
    saveArea();
    saveGiro();
    SaveModulo();
    _objSc.RestoreSave();
    _objSc = null;
}
function LlenaGiro() {
    var intId = 0;
    var strGiro = "";
    var intCC_CURSO_ID = document.getElementById("CC_CURSO_ID").value;
    var strPost = "CC_CURSO_ID=" + intCC_CURSO_ID;
    $.ajax({
        type: "POST",
        data: strPost,
        scriptCharset: "UTF-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "COFIDE_Cursos.jsp?id=9",
        success: function (datos) {
            var lstXml = datos.getElementsByTagName("cofide_cursos")[0];
            var lstCte = lstXml.getElementsByTagName("datos");
            for (var i = 0; i < lstCte.length; i++) {
                var objcte = lstCte[i];
                intId = objcte.getAttribute("CCG_ID");
                strGiro = objcte.getAttribute("CC_GIRO");
                var datarow = {CCG_ID: intId, CC_GIRO: strGiro, CC_CURSO_ID: intCC_CURSO_ID};
                itemIdCof++;
                jQuery("#CC_GRD_GIRO").addRowData(itemIdCof, datarow, "last");
            }
            LlenaModulos();
        }});
}
function LlenaGiroCat() {
    var intId = 0;
    var strGiro = "";
    var intCC_CURSO_ID = document.getElementById("CCU_ID_M").value;
    var strPost = "CCU_ID_M=" + intCC_CURSO_ID;
    $.ajax({
        type: "POST",
        data: strPost,
        scriptCharset: "UTF-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "COFIDE_Cursos.jsp?id=15",
        success: function (datos) {
            var lstXml = datos.getElementsByTagName("cofide_cursos")[0];
            var lstCte = lstXml.getElementsByTagName("datos");
            for (var i = 0; i < lstCte.length; i++) {
                var objcte = lstCte[i];
                intId = objcte.getAttribute("CCG_ID");
                strGiro = objcte.getAttribute("CC_GIRO");
                var datarow = {
                    CCG_ID: intId,
                    CC_GIRO: strGiro,
                    CC_CURSO_ID: intCC_CURSO_ID
                };
                itemIdCof++;
                jQuery("#CC_GRD_GIRO").addRowData(itemIdCof, datarow, "last");
            }
        }});
}
function LlenaArea() {
    var intId = 0;
    var strArea = "";
    var intCC_CURSO_ID = document.getElementById("CC_CURSO_ID").value;
    var strPost = "CC_CURSO_ID=" + intCC_CURSO_ID;
    $.ajax({
        type: "POST",
        data: strPost,
        scriptCharset: "UTF-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "COFIDE_Cursos.jsp?id=10",
        success: function (datos) {
            var lstXml = datos.getElementsByTagName("cofide_cursos")[0];
            var lstCte = lstXml.getElementsByTagName("datos");
            for (var i = 0; i < lstCte.length; i++) {
                var objcte = lstCte[i];
                intId = objcte.getAttribute("CCS_ID");
                strArea = objcte.getAttribute("CC_AREA");
                var datarow = {CCS_ID: intId, CC_AREA: strArea, CC_CURSO_ID: intCC_CURSO_ID};
                itemIdCof++;
                jQuery("#CC_GRD_AREA").addRowData(itemIdCof, datarow, "last");
            }
            LlenaGiro();
        }});
}
function LlenaAreaCat() {
    var intId = 0;
    var strArea = "";
    var intCC_CURSO_ID = document.getElementById("CCU_ID_M").value;
    var strPost = "CCU_ID_M=" + intCC_CURSO_ID;
    $.ajax({
        type: "POST",
        data: strPost,
        scriptCharset: "UTF-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "COFIDE_Cursos.jsp?id=16",
        success: function (datos) {
            var lstXml = datos.getElementsByTagName("cofide_cursos")[0];
            var lstCte = lstXml.getElementsByTagName("datos");
            for (var i = 0; i < lstCte.length; i++) {
                var objcte = lstCte[i];
                intId = objcte.getAttribute("CCS_ID");
                strArea = objcte.getAttribute("CC_AREA");
                var datarow = {CCS_ID: intId, CC_AREA: strArea, CC_CURSO_ID: intCC_CURSO_ID};
                itemIdCof++;
                jQuery("#CC_GRD_AREA").addRowData(itemIdCof, datarow, "last");
            }
            LlenaGiroCat();
        }});
}
//function LlenarDetalles() {
////    LlenaArea();
////    setTimeout("LlenaArea()", 1000);
////    setTimeout("LlenaGiro()", 1000);
////    setTimeout("LlenaModulos()", 1000);
//}
//function LlenarDetallesCat() {
////    setTimeout("LlenaAreaCat()", 1000);
////    setTimeout("LlenaGiroCat()", 1000);
//}
function OpnDiagCursosMod() {
    var objSecModiVta = objMap.getScreen("MOD_CURSO");
    if (objSecModiVta != null) {
        objSecModiVta.bolActivo = false;
        objSecModiVta.bolMain = false;
        objSecModiVta.bolInit = false;
        objSecModiVta.idOperAct = 0;
    }
    OpnOpt("MOD_CURSO", "grid", "dialogInv", false, false, true);
}
function dblClickModCurso(id) {
    var strNomMain = objMap.getNomMain();
    var grid = jQuery("#MOD_CURSO");
    var lstVal = grid.getRowData(id);
    if (strNomMain == "MOD_CURSO") {
        OpnEdit(document.getElementById("Ed" + strNomMain));
    } else {
        if (strNomMain == "V_CURSOS") {
            document.getElementById("CCD_CURSO").value = lstVal.CC_NOMBRE_CURSO;
            document.getElementById("CCD_ID").value = lstVal.CC_CURSO_ID;
            $("#dialogInv").dialog("close");
        } else {
            if (strNomMain == "CAT_CURSO") {
                document.getElementById("CCD_CURSO").value = lstVal.CC_NOMBRE_CURSO;
                document.getElementById("CCD_ID").value = lstVal.CC_CURSO_ID;
                $("#dialogInv").dialog("close");
            }
        }
    }
}
function AddModulo() {
    var curso = document.getElementById("CCD_CURSO").value;
    if (curso != "") {
        var datarow = {CCD_ID: document.getElementById("CCD_ID").value, CC_CURSO_ID: document.getElementById("CC_CURSO_ID").value, CCD_CURSO: curso};
        itemIdCof++;
        jQuery("#GRD_SEMIN").addRowData(itemIdCof, datarow, "last");
        document.getElementById("CCD_CURSO").value = "";
    } else {
        alert("Selecciona una Opcion");
    }
}
function DelMod() {
    var grid = jQuery("#GRD_SEMIN");
    if (grid.getGridParam("selrow") != null) {
        grid.delRowData(grid.getGridParam("selrow"));
    }
}
function SaveModulo() {
    var strPost = "";
    var intIdSave = 0;
    intIdSave = document.getElementById("CC_CURSO_ID").value;
    var grid = jQuery("#GRD_SEMIN");
    var idArr = grid.getDataIDs();
    strPost += "CC_CURSO_ID=" + intIdSave;
    for (var i = 0; i < idArr.length; i++) {
        var id = idArr[i];
        var lstRow = grid.getRowData(id);
        strPost += "&CCD_CURSO" + i + "=" + lstRow.CCD_CURSO + "";
        strPost += "&CCD_ID" + i + "=" + lstRow.CCD_ID + "";
    }
    strPost += "&length=" + idArr.length;
    $.ajax({
        type: "POST",
        data: strPost,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "html",
        url: "COFIDE_Cursos.jsp?id=11"
    });
    jQuery("#GRD_SEMIN").clearGridData();
//    alert("cursos que se van a guardar en el papá");
//    alert(strPost);
}
function LlenaModulos() {
    var intId = 0;
    var strCurso = "";
    var intCC_CURSO_ID = document.getElementById("CC_CURSO_ID").value;
    var strPost = "CC_CURSO_ID=" + intCC_CURSO_ID;
    $.ajax({
        type: "POST",
        data: strPost,
        scriptCharset: "UTF-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "COFIDE_Cursos.jsp?id=12",
        success: function (datos) {
            var lstXml = datos.getElementsByTagName("cofide_cursos")[0];
            var lstCte = lstXml.getElementsByTagName("datos");
            for (var i = 0; i < lstCte.length; i++) {
                var objcte = lstCte[i];
                intId = objcte.getAttribute("CM_ID");
                strCurso = objcte.getAttribute("CC_NOMBRE_CURSO");
                var datarow = {
                    CCD_ID: intId,
                    CCD_CURSO: strCurso,
                    CC_CURSO_ID: intCC_CURSO_ID
                };
                itemIdCof++;
                jQuery("#GRD_SEMIN").addRowData(itemIdCof, datarow, "last");
            }
            LoadDatosCurso();
        }});
}
function initValCurso() {
    NomCurso();
//    setTimeout("NomCurso()", 1000);
//    setTimeout("Template()", 1000);
    Template();
//    setTimeout("LlenarDetalles()", 1000);
}
function NomCurso() {
    document.getElementById("CC_NOMBRE_CURSOV").value = document.getElementById("CC_NOMBRE_CURSO").value;
}
function initValCursoSemDip() {
    if (d.getElementById("CC_IS_DIPLOMADO1").checked) {
        ValidaDiploma();
    }
    if (d.getElementById("CC_IS_SEMINARIO1").checked) {
        ValidaSemin();
    }
}
function ValidaDiploma() {
    var fieldDuracion = document.getElementById("CC_DURACION_HRS");
    if (d.getElementById("CC_IS_DIPLOMADO1").checked) {
        $("#CC_IS_SEMINARIO2").prop("checked", true);
        fieldDuracion.readOnly = false;
        fieldDuracion.style.backgroundColor = "#FFFFFF";
        fieldDuracion.style.color = "#2E2E2E";
    } else {
        fieldDuracion.readOnly = true;
        fieldDuracion.style.color = "#BDBDBD";
        fieldDuracion.style.backgroundColor = "#e0f8e6";
        validaCurso(1);
    }
}
function ValidaSemin() {
    var fieldDuracion = document.getElementById("CC_DURACION_HRS");
    if (d.getElementById("CC_IS_SEMINARIO1").checked) {
        $("#CC_IS_DIPLOMADO2").prop("checked", true);
        fieldDuracion.readOnly = false;
        fieldDuracion.style.backgroundColor = "#FFFFFF";
        fieldDuracion.style.color = "#2E2E2E";
    } else {
        fieldDuracion.readOnly = true;
        fieldDuracion.style.color = "#BDBDBD";
        fieldDuracion.style.backgroundColor = "#e0f8e6";
        validaCurso(1);
    }
}
function Template() {
    var IdCurso = document.getElementById("CC_CURSO_ID").value;
    var strFecha = document.getElementById("CC_FECHA_INICIAL").value;
    var strPost = "CC_CURSO_ID=" + IdCurso;
    strPost += "&CC_FECHA_INICIAL=" + strFecha;
    $("#dialogWait").dialog("open");
    $.ajax({
        type: "POST",
        data: strPost,
        scriptCharset: "UTF-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "html",
        url: "COFIDE_Cursos.jsp?id=14",
        success: function (datos) {
            if (trim(datos) == "OK") {
//                LlenarDetalles();
                LlenaArea();
            } else {
                alert(trim(datos));
            }
            $("#dialogWait").dialog("close");
        },
        error: function (objeto, quepaso, otroobj) {
            alert(":pto9:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }
    });
}
function SubirMaterial() {
    var intIdCurso = document.getElementById("CC_CURSO_ID").value;
    var File = document.getElementById("CC_MATERIAL_");
    if (File.value == "") {
        alert("Es necesario elegir un material para el curso");
        File.focus();
    } else {
        if (Right(File.value.toUpperCase(), 3) == "PDF") {
            $("#dialogWait").dialog("open");
            $.ajaxFileUpload({
//                url: "COFIDE_UpMaterialCurso.jsp?id_curso=" + intIdCurso,
                url: "COFIDE_UpMaterial.jsp?ID=1&id_curso=" + intIdCurso,
                secureuri: false,
                fileElementId: "CC_MATERIAL_",
                dataType: "json",
                success: function (data, status) {
                    if (typeof (data.error) != "undefined") {
                        if (data.error != "") {
                            alert(data.error);
                        } else {
                            alert("Material Guardado");
                            document.getElementById("CC_MATERIAL").value = data.msg;
                        }
                    }
                    $("#dialogWait").dialog("close");
                }, error: function (data, status, e) {
                    alert("ERROR al subir el material: " + e);
                    $("#dialogWait").dialog("close");
                }});
        } else {
            alert("Se aceptan archivos con extension PDF");
            File.focus();
        }
    }
}
/**
 * Al elegir una sede, se llena el alias de la sede
 * @returns {undefined}
 */
function fillAlias(opc) {
    var intIdSede = "";
    var strAlias = "";
    var strSedeF = "";
    var strAliasF = "";
    if (opc == 1) {
        strSedeF = "CC_SEDE_ID";
        strAliasF = "CC_ALIAS";
    }
    if (opc == 2) {
        strSedeF = "CC_SEDE_ID2";
        strAliasF = "CC_ALIAS_FEC2";
    }
    if (opc == 3) {
        strSedeF = "CC_SEDE_ID3";
        strAliasF = "CC_ALIAS_FEC3";
    }
    intIdSede = document.getElementById(strSedeF).value;
    if (intIdSede != "") {
        $("#dialogWait").dialog("open");
        $.ajax({
            type: "POST",
            data: "id_sede=" + intIdSede,
            scriptCharset: "utf-8",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "xml",
            url: "COFIDE_Cursos.jsp?id=17",
            success: function (datos) {
                var lstXml = datos.getElementsByTagName("datos")[0];
                var lstprecio = lstXml.getElementsByTagName("cte");
                for (var i = 0; i < lstprecio.length; i++) {
                    var obj = lstprecio[i];
                    strAlias = obj.getAttribute("alias");
                }
                document.getElementById(strAliasF).value = strAlias;
            },
            error: function (objeto, quepaso, otroobj) {
                alert(":pto9:" + objeto + " " + quepaso + " " + otroobj);
                $("#dialogWait").dialog("close");
            }
        });
        $("#dialogWait").dialog("close");
    }
}
function FillHrAlimento(opc) {
    var intAlimento = "";
    var strAlimento = "";
    var strHorarioAlimento = "";
    if (opc == 1) {
        intAlimento = document.getElementById("CC_TIPO_ALIMENTO").value;
        strAlimento = document.getElementById("CC_ALIMENTO");
    }
    if (opc == 2) {
        intAlimento = document.getElementById("CC_TIPO_ALIMENTO2").value;
        strAlimento = document.getElementById("CC_ALIMENTO2");
    }
    if (opc == 3) {
        intAlimento = document.getElementById("CC_TIPO_ALIMENTO3").value;
        strAlimento = document.getElementById("CC_ALIMENTO3");
    }
    if (intAlimento != "") {
        if (intAlimento == "6") {
            strAlimento.readOnly = false;
            strAlimento.style["background"] = "none";
            strAlimento.style["color"] = "#000000";
        } else {
            strAlimento.readOnly = true;
            strAlimento.style["background"] = "#e0f8e6";
            $("#dialogWait").dialog("open");
            $.ajax({
                type: "POST",
                data: "id_alimento=" + intAlimento,
                scriptCharset: "utf-8",
                contentType: "application/x-www-form-urlencoded;charset=utf-8",
                cache: false,
                dataType: "xml",
                url: "COFIDE_Cursos.jsp?id=18",
                success: function (datos) {
                    var lstXml = datos.getElementsByTagName("datos")[0];
                    var lstprecio = lstXml.getElementsByTagName("cte");
                    for (var i = 0; i < lstprecio.length; i++) {
                        var obj = lstprecio[i];
                        strHorarioAlimento = obj.getAttribute("horario");
                    }
                    strAlimento.value = strHorarioAlimento;
                },
                error: function (objeto, quepaso, otroobj) {
                    alert(":pto9:" + objeto + " " + quepaso + " " + otroobj);
                    $("#dialogWait").dialog("close");
                }
            });
            $("#dialogWait").dialog("close");
        }
    }
}
function validaDispSede() {
    var intIdSede = document.getElementById("CC_SEDE_ID").value;
    if (intIdSede != "") {
        $("#dialogWait").dialog("open");
        $.ajax({
            type: "POST",
            data: "id_sede=" + intIdSede,
            scriptCharset: "utf-8",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "xml",
            url: "COFIDE_Cursos.jsp?id=19",
            success: function (datos) {
                var lstXml = datos.getElementsByTagName("datos")[0];
                var lstprecio = lstXml.getElementsByTagName("cte");
                for (var i = 0; i < lstprecio.length; i++) {
                    var obj = lstprecio[i];
                    if (obj.getAttribute("disponible") == true) {
                        alert("Esta sede ya tiene ocupado este horario");
                    }
                }
            },
            error: function (objeto, quepaso, otroobj) {
                alert(":pto9:" + objeto + " " + quepaso + " " + otroobj);
                $("#dialogWait").dialog("close");
            }
        });
        $("#dialogWait").dialog("close");
    }
}
/**
 * trae la clave del curso seleccionado
 * @returns {undefined}
 */
function FindClave() {
    var strCurso = document.getElementById("CC_NOMBRE_CURSO").value;
    var intIdClave = "";
    if (strCurso != "") {
        $("#dialogWait").dialog("open");
        $.ajax({
            type: "POST",
            data: "caso=1&curso=" + strCurso,
            scriptCharset: "utf-8",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "xml",
            url: "COFIDE_Cursos.jsp?id=19",
            success: function (datos) {
                var lstXml = datos.getElementsByTagName("datos")[0];
                var lstprecio = lstXml.getElementsByTagName("cte");
                for (var i = 0; i < lstprecio.length; i++) {
                    var obj = lstprecio[i];
                    intIdClave = obj.getAttribute("id");
                }
                document.getElementById("CC_CLAVES").value = intIdClave;
            },
            error: function (objeto, quepaso, otroobj) {
                alert(":pto9:" + objeto + " " + quepaso + " " + otroobj);
                $("#dialogWait").dialog("close");
            }
        });
        $("#dialogWait").dialog("close");
    }
}
/**
 * trae el id del instructor
 */
function FindIdInstructor() {
    var strInstructor = document.getElementById("CC_INSTRUCTOR").value;
    var intIdInstructor = "";
    if (strInstructor != "") {
        $("#dialogWait").dialog("open");
        $.ajax({
            type: "POST",
            data: "caso=2&instructor=" + strInstructor,
            scriptCharset: "utf-8",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "xml",
            url: "COFIDE_Cursos.jsp?id=19",
            success: function (datos) {
                var lstXml = datos.getElementsByTagName("datos")[0];
                var lstprecio = lstXml.getElementsByTagName("cte");
                for (var i = 0; i < lstprecio.length; i++) {
                    var obj = lstprecio[i];
                    intIdInstructor = obj.getAttribute("id");
                }
                document.getElementById("CC_INSTRUCTOR_ID").value = intIdInstructor;
            },
            error: function (objeto, quepaso, otroobj) {
                alert(":pto9:" + objeto + " " + quepaso + " " + otroobj);
                $("#dialogWait").dialog("close");
            }
        });
        $("#dialogWait").dialog("close");
    }
}

function showFieldFechaExtra() {
    if (document.getElementById("CC_FECHA_INICIAL2").parentNode.parentNode.style.display == '') {
        document.getElementById("LBL_FECHA2").parentNode.parentNode.style.display = 'none';
        document.getElementById("CC_FECHA_INICIAL2").parentNode.parentNode.style.display = 'none';
        document.getElementById("CC_SEDE_ID2").parentNode.parentNode.style.display = 'none';
        document.getElementById("CC_ALIAS_FEC2").parentNode.parentNode.style.display = 'none';
        document.getElementById("CC_SALON2").parentNode.parentNode.style.display = 'none';
        document.getElementById("CC_SESION2").parentNode.parentNode.style.display = 'none';
        document.getElementById("CC_HR_EVENTO_INI2").parentNode.parentNode.style.display = 'none';
        document.getElementById("CC_HR_EVENTO_FIN2").parentNode.parentNode.style.display = 'none';
        document.getElementById("CC_DURACION_HRS2").parentNode.parentNode.style.display = 'none';
        document.getElementById("CC_TIPO_ALIMENTO2").parentNode.parentNode.style.display = 'none';
        document.getElementById("CC_ALIMENTO2").parentNode.parentNode.style.display = 'none';
        //Campos FECHA 3
        document.getElementById("LBL_FECHA3").parentNode.parentNode.style.display = 'none';
        document.getElementById("CC_FECHA_INICIAL3").parentNode.parentNode.style.display = 'none';
        document.getElementById("CC_SEDE_ID3").parentNode.parentNode.style.display = 'none';
        document.getElementById("CC_ALIAS_FEC3").parentNode.parentNode.style.display = 'none';
        document.getElementById("CC_SALON3").parentNode.parentNode.style.display = 'none';
        document.getElementById("CC_SESION3").parentNode.parentNode.style.display = 'none';
        document.getElementById("CC_HR_EVENTO_INI3").parentNode.parentNode.style.display = 'none';
        document.getElementById("CC_HR_EVENTO_FIN3").parentNode.parentNode.style.display = 'none';
        document.getElementById("CC_DURACION_HRS3").parentNode.parentNode.style.display = 'none';
        document.getElementById("CC_TIPO_ALIMENTO3").parentNode.parentNode.style.display = 'none';
        document.getElementById("CC_ALIMENTO3").parentNode.parentNode.style.display = 'none';
    } else {
        document.getElementById("LBL_FECHA2").parentNode.parentNode.style.display = '';
        document.getElementById("CC_FECHA_INICIAL2").parentNode.parentNode.style.display = '';
        document.getElementById("CC_SEDE_ID2").parentNode.parentNode.style.display = '';
        document.getElementById("CC_ALIAS_FEC2").parentNode.parentNode.style.display = '';
        document.getElementById("CC_SALON2").parentNode.parentNode.style.display = '';
        document.getElementById("CC_SESION2").parentNode.parentNode.style.display = '';
        document.getElementById("CC_HR_EVENTO_INI2").parentNode.parentNode.style.display = '';
        document.getElementById("CC_HR_EVENTO_FIN2").parentNode.parentNode.style.display = '';
        document.getElementById("CC_DURACION_HRS2").parentNode.parentNode.style.display = '';
        document.getElementById("CC_TIPO_ALIMENTO2").parentNode.parentNode.style.display = '';
        document.getElementById("CC_ALIMENTO2").parentNode.parentNode.style.display = '';
        //Campos FECHA 3
        document.getElementById("LBL_FECHA3").parentNode.parentNode.style.display = '';
        document.getElementById("CC_FECHA_INICIAL3").parentNode.parentNode.style.display = '';
        document.getElementById("CC_SEDE_ID3").parentNode.parentNode.style.display = '';
        document.getElementById("CC_ALIAS_FEC3").parentNode.parentNode.style.display = '';
        document.getElementById("CC_SALON3").parentNode.parentNode.style.display = '';
        document.getElementById("CC_SESION3").parentNode.parentNode.style.display = '';
        document.getElementById("CC_HR_EVENTO_INI3").parentNode.parentNode.style.display = '';
        document.getElementById("CC_HR_EVENTO_FIN3").parentNode.parentNode.style.display = '';
        document.getElementById("CC_DURACION_HRS3").parentNode.parentNode.style.display = '';
        document.getElementById("CC_TIPO_ALIMENTO3").parentNode.parentNode.style.display = '';
        document.getElementById("CC_ALIMENTO3").parentNode.parentNode.style.display = '';
    }
}
/**
 * carga la informacion de sede e instructor(es)
 * @returns {undefined}
 */
function LoadDatosCurso() {
    var strPost = "";
    var strId_Curso = document.getElementById("CC_CURSO_ID").value;
    var strId_Sede = document.getElementById("CC_SEDE_ID").value;
    strPost = "id_curso=" + strId_Curso;
    strPost += "&id_sede=" + strId_Sede;
    var strInstructor = document.getElementById("CC_INSTRUCTOR");
    var strSede = document.getElementById("CC_SEDE");
    $.ajax({
        type: "POST",
        data: strPost,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "COFIDE_Cursos.jsp?id=20",
        success: function (datos) {
            var objsc = datos.getElementsByTagName("datos")[0];
            var lstProds = objsc.getElementsByTagName("cte");
            for (var i = 0; i < lstProds.length; i++) {
                var obj = lstProds[i];
                strSede.value = obj.getAttribute("sede");
                strInstructor.value = obj.getAttribute("instructor");
            }
        }, error: function () {
            alert("No hay Datos");
        }});
}
function getClaveCurso() {
    $.ajax({
        type: "POST",
        data: "",
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "html",
        url: "COFIDE_Cursos.jsp?id=21",
        success: function (datos) {
            datos = trim(datos);
            document.getElementById("CCU_CLAVE").value = datos;
        }, error: function () {
            alert("Hubo un problema al recuperar la clave del curso.");
        }});
}
function UpdateFichaCurso(id) {
    var strIdCurso = document.getElementById("CCU_ID_M").value;
    var strDetalle = document.getElementById("CCU_DETALLE").value;
    var strTemario = document.getElementById("CCU_TEMARIO").value;
    var strRequisito = document.getElementById("CCU_REQUISITO").value;
    var strObjetivo = document.getElementById("CCU_OBJETIVO").value;
    var strDirigido = document.getElementById("CCU_DIRIGIDO").value;
    var strNombre = document.getElementById("CCU_CURSO").value;
    var strPost = "id_curso=" + strIdCurso; //id del catalogo de cursos
    strPost += "&detalle=" + encodeURIComponent(strDetalle);
    strPost += "&temario=" + encodeURIComponent(strTemario);
    strPost += "&requisito=" + encodeURIComponent(strRequisito);
    strPost += "&objetivo=" + encodeURIComponent(strObjetivo);
    strPost += "&dirigido=" + encodeURIComponent(strDirigido);
    strPost += "&nombre=" + encodeURIComponent(strNombre);
    $.ajax({
        type: "POST",
        data: encodeURI(strPost),
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "html",
        url: "COFIDE_Cursos.jsp?id=22",
        success: function (datos) {
            datos = trim(datos);
            if (datos != "OK") {
                alert("error al actualizar los cursos del calendario");
            } else {
                saveAreaCatalogoCurso();
                saveGiroCatalogoCurso();
                _objSc.RestoreSave();
                _objSc = null;
            }
        }, error: function () {
            alert("Hubo un problema al recuperar la clave del curso.");
        }});
}
function UpdateVideo() {
    var strNomVideo = "";
    var strIdCte = document.getElementById("CC_CURSO_ID").value;
    strNomVideo = document.getElementById("CC_VIDEO").value;
    strNomVideo = trim(strNomVideo);
    $.ajax({
        type: "POST",
        data: "video=" + strNomVideo + "&id_curso=" + strIdCte,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "html",
        url: "COFIDE_Cursos.jsp?id=23",
        success: function (datos) {

        }, error: function () {
            alert("Hubo un problema al enviar el nombre del video curso.");
        }});
}

/*
 * catalogo de nombres de videos
 */
function OpnVideo() {
    var objSecModiVta = objMap.getScreen("CAT_VID");
    if (objSecModiVta != null) {
        objSecModiVta.bolActivo = false;
        objSecModiVta.bolMain = false;
        objSecModiVta.bolInit = false;
        objSecModiVta.idOperAct = 0;
    }
    OpnOpt("CAT_VID", "_ed", "dialogCte", false, false, true);
}

/*
 * ABC DE LOS NOMBRES PARA LOS VIDEOS
 */
var strNombreVid = "";
var strPartVid = "";
var strFecha = "";
var intConteo = 0;
var intOpc = ""; //opcion que se quedo

function ABCVideo(opc) {
    if (opc == 1) { //ADD

        if (document.getElementById("NVIDEO").value != "") {
            intOpc = "0";
            var datarow = {
                NOMBRE: document.getElementById("NVIDEO").value,
                N_PART: document.getElementById("NPART").value
            };
            intConteo++;
            jQuery("#GRID_VIDEO").addRowData(intConteo, datarow, "last");
            SaveVideo();
        } else {
            alert("ES NECESARIO UN NOMBRE PARA EL VIDEO.");
        }

    }
    if (opc == 2) { //DEL
        var grid = jQuery("#GRID_VIDEO");
        if (grid.getGridParam("selrow") != null) {
            grid.delRowData(grid.getGridParam("selrow"));
        } else {
            alert("SELECCIONA UN REGISTRO.");
        }
    }
    if (opc == 3) { //MOD
        intOpc = "1"; //se va a editar        
        var grid = jQuery("#GRID_VIDEO");
        if (grid.getGridParam("selrow") != null) {
            var lstRow = grid.getRowData(grid.getGridParam("selrow"));
            document.getElementById("NVIDEO").value = lstRow.NOMBRE;
            document.getElementById("NPART").value = lstRow.N_PART;
            strNombreVid = lstRow.NOMBRE;
            strPartVid = lstRow.N_PART;
            ABCVideo(2);
        } else {
            alert("SELECCIONA UN REGISTRO.");
        }
    }
    if (opc == 4) { //CANCEL
        if (intOpc == "1") {

            var datarow = {
                NOMBRE: strNombreVid,
                N_PART: strPartVid
            };
            intConteo++;
            jQuery("#GRID_VIDEO").addRowData(intConteo, datarow, "last");

            document.getElementById("NVIDEO").value = "";
            document.getElementById("NPART").value = "";

        } else {
            document.getElementById("NVIDEO").value = "";
            document.getElementById("NPART").value = "";
        }
        intOpc = "0";
    }
}

/*
 * ajax de guardado
 */
function SaveVideo() {
    document.getElementById("NVIDEO").value = "";
    document.getElementById("NPART").value = "";
    var strPost = "";
    var grid = jQuery("#GRID_VIDEO");
    var idArr = grid.getDataIDs();
    strPost += "CC_CURSO_ID=" + document.getElementById("CC_CURSO_ID").value;
    for (var i = 0; i < idArr.length; i++) {
        var id = idArr[i];
        var lstRow = grid.getRowData(id);
        var strParte = "";
        if (lstRow.N_PART == "") {
            strParte = "0";
        } else {
            strParte = lstRow.N_PART;
        }
        strPost += "&CC_NOMBRE" + i + "=" + lstRow.NOMBRE + "";
        strPost += "&CC_PARTE" + i + "=" + strParte + "";
    }
    strPost += "&LENGTH_VIDEO=" + idArr.length;
    $.ajax({
        type: "POST",
        data: strPost,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "html",
        url: "COFIDE_Cursos.jsp?id=24",
        success: function (datos) {
            datos = trim(datos);
            if (datos == "OK") {
                LoadVideo();
            } else {
                alert(datos);
            }
        }, error: function (objeto, quepaso, otroobj) {
            alert(":pto=5: " + objeto + " " + quepaso + " " + otroobj);
        }
    });
}

/*
 * llena el grid
 */
function LoadVideo() {
    jQuery("#GRID_VIDEO").clearGridData();
    var stridCurso = document.getElementById("CC_CURSO_ID").value;
    $("#dialogWait").dialog("open");
    var strPost = "CC_CURSO_ID=" + stridCurso;
    $.ajax({
        type: "POST",
        data: strPost,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "COFIDE_Cursos.jsp?id=25",
        success: function (datos) {
            var lstXml = datos.getElementsByTagName("vta")[0];
            var lstCte = lstXml.getElementsByTagName("datos");
            for (var i = 0; i < lstCte.length; i++) {
                var objcte = lstCte[i];
                var datarow = {
                    NOMBRE: objcte.getAttribute("CC_NOMBRE"),
                    FECHA: objcte.getAttribute("CC_FECHA"),
                    N_PART: objcte.getAttribute("CC_PARTE")
                };
                itemIdCob++;
                jQuery("#GRID_VIDEO").addRowData(itemIdCob, datarow, "last");
            }
        }, error: function (objeto, quepaso, otroobj) {
            alert(":pto=25: " + objeto + " " + quepaso + " " + otroobj);
        }
    });
    $("#dialogWait").dialog("close");
}

function SalirVid() {
    $("#dialogCte").dialog("close");
}

/**
 * subir codigo QR personalizado
 * @returns {undefined}
 */
function UpQR() {

    var intIdCurso = document.getElementById("CC_CURSO_ID").value;
    var File = document.getElementById("CC_CODIGO_QR_D");
    if (File.value == "") {
        alert("Es necesariocargar una imagen QR para el curso");
        File.focus();
    } else {
        if (Right(File.value.toUpperCase(), 3) == "PNG") {

            $.ajaxFileUpload({
                url: "COFIDE_UpMaterial.jsp?ID=5&id_curso=" + intIdCurso,
                secureuri: false,
                fileElementId: "CC_CODIGO_QR_D",
                dataType: "json",
                beforeSend() {
                    $("#dialogWait").dialog("open");
                },
                success: function (data, status) {
                    if (typeof (data.error) != "undefined") {
                        if (data.error != "") {
                            alert(data.error);
                        } else {
                            alert("Codigo QR guardado con exito!");
                            document.getElementById("CC_CODIGO_QR").value = data.msg;
                        }
                    }
                    $("#dialogWait").dialog("close");
                }, error: function (data, status, e) {
                    alert("ERROR al subir el codigo QR: " + e);
                    $("#dialogWait").dialog("close");
                }});
        } else {
            alert("Se aceptan archivos con extension PNG");
            File.focus();
        }
    }
}
