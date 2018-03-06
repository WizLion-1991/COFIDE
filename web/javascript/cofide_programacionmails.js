/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
function cofide_programacionmails() {

}
var timTimerMail;
var strIdMTMP;
function initProgramacionCofide() {
//    LlenaAreaProg();
//    LlenaGiroProg();
    LoadMailCursos();
    LoadMailCursos5();
    setDialogGiros();
}


function LlenaAreaProg() { //llena el detalle del segmento en cursos
    var intId = 0;
    var strArea = "";
    var itemIdCof = 0;
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
            jQuery("#CC_GRD_AREA").clearGridData();
            var lstXml = datos.getElementsByTagName("cofide_cursos")[0]; //dato padre
            var lstCte = lstXml.getElementsByTagName("datos"); //dato detalle
            for (var i = 0; i < lstCte.length; i++) {
                var objcte = lstCte[i];
                //recuperamos datos del jsp
                intId = objcte.getAttribute("CCS_ID");
                strArea = objcte.getAttribute("CC_AREA");
                //llenamos el grid
                var datarow = {
                    CCS_ID: intId,
                    CC_AREA: strArea,
                    CC_CURSO_ID: intCC_CURSO_ID,
                }; //fin del grid
                itemIdCof = lstCte.length + 1;
                jQuery("#CC_GRD_AREA").addRowData(itemIdCof, datarow, "last");
            }
        } //fin de la funcion que recupera los datos
    }); //fin del ajax
}

function LlenaGiroProg() { //llena el detalle del giro en cursos
    var intId = 0;
    var itemIdCof = 0;
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
            jQuery("#CC_GRD_GIRO").clearGridData();
            var lstXml = datos.getElementsByTagName("cofide_cursos")[0]; //dato padre
            var lstCte = lstXml.getElementsByTagName("datos"); //dato detalle
            for (var i = 0; i < lstCte.length; i++) {
                var objcte = lstCte[i];
                //recuperamos datos del jsp
                intId = objcte.getAttribute("CCG_ID");
                strGiro = objcte.getAttribute("CC_GIRO");
                //llenamos el grid
                var datarow = {
                    CCG_ID: intId,
                    CC_GIRO: strGiro,
                    CC_CURSO_ID: intCC_CURSO_ID,
                }; //fin del grid
                itemIdCof = lstCte.length + 1;
                jQuery("#CC_GRD_GIRO").addRowData(itemIdCof, datarow, "last");
            }
        } //fin de la funcion que recupera los datos
    }); //fin del ajax
}
function LoadMailCursos() { //llena la sugerencia de mail de cursos masivos
    var CountRow = 0;
    $("#dialogWait").dialog("open");
    $.ajax({
        type: "POST",
        data: "",
        scriptCharset: "UTF-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "COFIDE_Programacionmails.jsp?ID=1",
        success: function (datos) {
            jQuery("#GRID_MAIL").clearGridData();
            var lstXml = datos.getElementsByTagName("Mail")[0]; //dato padre
            var lstCte = lstXml.getElementsByTagName("datos"); //dato detalle
            for (var i = 0; i < lstCte.length; i++) {
                var objcte = lstCte[i];
                //llenamos el grid
                var datarow = {
                    CC_CURSO_ID: objcte.getAttribute("id"),
                    CC_SEDE_ID: objcte.getAttribute("id_sede"),
                    CC_CLAVES: objcte.getAttribute("clave"),
                    CC_FECHA_INICIAL: objcte.getAttribute("fecini"),
                    CC_TEMPLATE1: objcte.getAttribute("t1"),
                    CC_TEMPLATE2: objcte.getAttribute("t2"),
                    CC_TEMPLATE3: objcte.getAttribute("t3"),
                    CC_AREAS: objcte.getAttribute("areas"),
                    CC_GIROS: objcte.getAttribute("giros"),
                    CC_SEDE: objcte.getAttribute("sede"),
                    CC_NOMBRE_CURSO: objcte.getAttribute("nombre"),
                    CC_MASIVOS: objcte.getAttribute("masivo"),
                    CC_MAILGROUP: objcte.getAttribute("grupo"),
                    CC_CONFIRMA_MAIL: objcte.getAttribute("confirma")
                }; //fin del grid
                CountRow++;
                jQuery("#GRID_MAIL").addRowData(CountRow, datarow, "last");
            }
        } //fin de la funcion que recupera los datos
    }); //fin del ajax
    $("#dialogWait").dialog("close");
} //fin clase
function TemplatePreview() {
    var strPlantilla = "";
    var grid = jQuery("#GRID_MAIL");
    var ids = grid.getGridParam("selrow");
    if (ids !== null) {
        var lstRow1 = grid.getRowData(ids);
        var intIdCC = lstRow1.CC_CURSO_ID;
        var intTemp1 = lstRow1.CC_TEMPLATE1;
        var strSede = getNumerosSede(); //obtiene las sedes del div de sedes
        strSede = strSede.substring(0, strSede.length - 1);
        if (intTemp1 != "0") {
            var strPost = "&id_curso=" + intIdCC;
            strPost += "&sede=" + strSede.split(",", 1);
            strPost += "&Template1=" + intTemp1;
            $.ajax({
                type: "POST",
                data: strPost,
                url: "COFIDE_Correo.jsp",
                dataType: "html",
                scriptCharset: "utf-8",
                cache: false,
                contentType: "application/x-www-form-urlencoded;charset=utf-8",
                success: function (datos) {
                    strPlantilla = trim(datos);
                    document.getElementById("TEMPLATE_").value = strPlantilla;
                    OpnTemplatePreview();
                }
            });
        } else {
            alert("No hay ninguna plantilla seleccionada para este Curso");
        }
    } else {
        alert("Selecciona Alguna Plantilla para Previsualizar");
    }
}
function TemplateConfirm() {

    TurnOnQuartz();
    var strAsunto = document.getElementById("MAIL_ASUNTO_MM").value;
    var intIdPlantilla = document.getElementById("plantillapreview1").value;
    var intPLealtad = document.getElementById("MAIL_PLEALTAD").value;
    var idPlantilla = document.getElementById("MAIL_TEMPLATE10").value;
    var MailMes = "0";
    var intLength = 0;
    var strGiro = getNumerosGiro();
    var strSede = getNumerosSede();
    var strArea = getNumerosSegmentos();
    var strTipoCliente = "";

    var bolGiro = 1;
    var bolArea = 1;
    var bolSede = 1;

    if (getNumerosGiro().length < 1) {
        bolGiro = 0;
    }
    if (getNumerosSede().length < 1) {
        bolSede = 0;
    }
    if (getNumerosSegmentos().length < 1) {
        bolArea = 0;
    }
    strGiro = strGiro.substring(0, strGiro.length - 1);
    strSede = strSede.substring(0, strSede.length - 1);
    strArea = strArea.substring(0, strArea.length - 1);
    if (document.getElementById("MAIL_MES_MM").checked) {
        MailMes = "1";
    }

    if (document.getElementById("MAIL_SEGMENTO").value == "0") {
        strTipoCliente = "1";
    } else {
        strTipoCliente = document.getElementById("MAIL_SEGMENTO").value;
    }
    if (strAsunto != "") {
        var grid = jQuery("#GRID_MAIL");
        var ids = grid.getGridParam("selarrrow");
        intLength = ids.length;
        if (intLength > 0 || document.getElementById("MAIL_TEMPLATE10").value == "8" || document.getElementById("MAIL_TEMPLATE10").value == "9" || document.getElementById("MAIL_TEMPLATE10").value == "10") {

            document.getElementById("SioNO_inside").innerHTML = "¿CONFIRMA ENVÍO DE MASIVO?";
            $("#SioNO").dialog("open");
            document.getElementById("btnSI").onclick = function () {
                $("#SioNO").dialog("close");

                var strPost = "id_plantilla=" + intIdPlantilla;
                strPost += "&giro=" + strGiro;
                strPost += "&sede=" + strSede;
                strPost += "&area=" + strArea;
                strPost += "&asunto=" + encodeURIComponent(strAsunto);
                strPost += "&mailmes=" + MailMes;
                strPost += "&segmento=" + strTipoCliente;
                strPost += "&p_lealtad=" + intPLealtad;
                strPost += "&plantilla_puntos=" + idPlantilla;
                $.ajax({
                    type: "POST",
                    data: encodeURI(strPost),
                    scriptCharset: "UTF-8",
                    contentType: "application/x-www-form-urlencoded;charset=utf-8",
                    cache: false,
                    dataType: "html",
                    url: "COFIDE_Programacionmails.jsp?ID=2",
                    beforeSend: function () {
                        $("#dialogWait").dialog("open");
                    },
                    success: function (datos) {
                        datos = trim(datos);
                        if (datos == "OK") {
                            alert("Plantilla Confirmada!");
                            setDialogGiros(); //restablecer los catalogos de sede,giro y area
                            document.getElementById("MAIL_ASUNTO_MM").value = "";
                            $("#MAIL_MES_MM").prop("checked", false);
                            LoadMailCursos();
                        } else {
                            alert("No se encontrarón correos con la selección de filtros.");
                        }
                        $("#dialogWait").dialog("close");
                    }, error: function (data) {
                        $("#dialogWait").dialog("close");
                        alert(data + " Hubó un problema al enviar el masivo");
                    }
                });

            };
            document.getElementById("btnNO").onclick = function () {
                $("#SioNO").dialog("close");
            };


        } else {
            alert("Selecciona Alguna Plantilla para Confirmar");
        }
    } else {
        alert("Falta incluir el asunto del mail");
        document.getElementById("MAIL_ASUNTO_MM").focus();
    }
}
function TemplateConfirm2() {
    var intIdPlantilla = document.getElementById("plantillapreview2").value;
    var MailMes = "0";
    var intLength = 0;
    var strAsunto = document.getElementById("MAIL_ASUNTO_MG").value;
    if (document.getElementById("MAIL_MES_MG").checked) {
        MailMes = "1";
    }
    if (strAsunto != "") {
        var grid = jQuery("#GRID_MAIL5");
        var ids = grid.getGridParam("selarrrow");
        intLength = ids.length;
        if (intLength > 0) {
            var strPost = "id_plantilla=" + intIdPlantilla;
            strPost += "&mailmes=" + MailMes;
            strPost += "&asunto=" + encodeURIComponent(strAsunto);
            $("#dialogWait").dialog("open");
            $.ajax({
                type: "POST",
                data: encodeURI(strPost),
                scriptCharset: "UTF-8",
                contentType: "application/x-www-form-urlencoded;charset=utf-8",
                cache: false,
                dataType: "html",
                url: "COFIDE_Programacionmails.jsp?ID=9",
                success: function (dato) {
                    dato = trim(dato);
                    if (dato == "OK") {
                        alert("Plantilla Confirmada!");
                        document.getElementById("MAIL_ASUNTO_MG").value = "";
                        $("#MAIL_MES_MG").prop("checked", false);
                        LoadMailCursos5();
                    } else {
                        alert(dato);
                    }
                    $("#dialogWait").dialog("close");
                },
                error: function (objeto, quepaso, otroobj) {
                    alert(":pto16:" + objeto + " " + quepaso + " " + otroobj);
                    $("#dialogWait").dialog("close");
                }});
        } else {
            alert("Selecciona un curso para Confirmar");
        }
    } else {
        alert("Falta incluir el asunto del mail");
        document.getElementById("MAIL_ASUNTO_MG").focus();
    }
}
//abre la vista previa de la plantilla
function OpnTemplatePreview() {
    var objSecModiVta = objMap.getScreen("TEMP_PREVIEW");
    if (objSecModiVta != null) {
        objSecModiVta.bolActivo = false;
        objSecModiVta.bolMain = false;
        objSecModiVta.bolInit = false;
        objSecModiVta.idOperAct = 0;
    }
    OpnOpt("TEMP_PREVIEW", "_ed", "dialogCte", false, false, true);
}
function CerrarTemplate() {
    $("#dialogCte").dialog("close");
}

function HistorialMails() {
    var CountRow = 0;
    var strFecIni = document.getElementById("MAIL_FECINI").value;
    var strFecFin = document.getElementById("MAIL_FECFIN").value;
    if (strFecIni != "" && strFecFin != "") {
        var strPost = "fecini=" + strFecIni + "&fecfin=" + strFecFin;
        $("#dialogWait").dialog("open");
        $.ajax({
            type: "POST",
            data: strPost,
            scriptCharset: "UTF-8",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "xml",
            url: "COFIDE_Programacionmails.jsp?ID=3",
            success: function (datos) {
                jQuery("#GRID_HMAIL").clearGridData();
                var lstXml = datos.getElementsByTagName("Mail")[0]; //dato padre
                var lstCte = lstXml.getElementsByTagName("datos"); //dato detalle
                for (var i = 0; i < lstCte.length; i++) {
                    var objcte = lstCte[i];
                    //llenamos el grid
                    var datarow = {
                        CRM_ID: objcte.getAttribute("id"), //id del masivo
                        CRM_FECHA: objcte.getAttribute("fecha"),
                        CRM_HORA: objcte.getAttribute("hora"),
                        CRM_USUARIO: objcte.getAttribute("usuario"),
                        CRM_TEMPLATE: objcte.getAttribute("template"),
                        CRM_AUNTO: objcte.getAttribute("asunto"),
                        CRM_CURSO: objcte.getAttribute("curso")
                    }; //fin del grid
                    CountRow++;
                    jQuery("#GRID_HMAIL").addRowData(CountRow, datarow, "last");
                }
            } //fin de la funcion que recupera los datos
        }); //fin del ajax
        $("#dialogWait").dialog("close");
    } else {
        alert("Elije un rango de fechas para el historial!");
    }
}
function ShowDetaMail() {
    var CountRow = 0;
    var grid = jQuery("#GRID_HMAIL");
    var ids = grid.getGridParam("selrow");
    if (ids !== null) {
        var lstRow1 = grid.getRowData(ids);
        var intId = lstRow1.CRM_ID;
        var strPost = "idm=" + intId;
        alert("Esta operación podria demorar unos minutos");
        $("#dialogWait").dialog("open");
        $.ajax({
            type: "POST",
            data: strPost,
            scriptCharset: "UTF-8",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "xml",
            url: "COFIDE_Programacionmails.jsp?ID=4",
            success: function (datos) {
                jQuery("#GRID_MAILD").clearGridData();
                var lstXml = datos.getElementsByTagName("Mail")[0]; //dato padre
                var lstCte = lstXml.getElementsByTagName("datos"); //dato detalle
                for (var i = 0; i < lstCte.length; i++) {
                    var objcte = lstCte[i];
                    var datarow = {
                        CRMD_ID: objcte.getAttribute("id"),
                        CRM_ID: objcte.getAttribute("idm"),
                        CT_ID: objcte.getAttribute("id_cte"),
                        CRMD_EMAIL: objcte.getAttribute("mail"),
                        CRM_PROCESADO: objcte.getAttribute("procesado")
                    }; //fin del grid
                    CountRow++;
                    jQuery("#GRID_MAILD").addRowData(CountRow, datarow, "last");
                }
            } //fin de la funcion que recupera los datos
        }); //fin del ajax
        $("#dialogWait").dialog("close");
    } else {
        alert("Selecciona Alguna Plantilla para ver detalles");
    }
}
function ExportDetaMail() { //crear xls
    var grid = jQuery("#GRID_HMAIL");
    var ids = grid.getGridParam("selrow");
    if (ids !== null) {
        var lstRow1 = grid.getRowData(ids);
        var intId = lstRow1.CRM_ID;
        $("#dialogWait").dialog("open");
        alert(intId);
        Abrir_Link("JasperReport?REP_ID=511&boton_1=XLS&CRM_ID=" + intId, '_reporte', 500, 600, 0, 0);
        $("#dialogWait").dialog("close");
    } else {
        alert("Selecciona Alguna Plantilla para Exportar detalles");
    }
}
function LoadMailCursos5() { //mail de 5 dias mailgroup 
    var CountRow = 0;
    $("#dialogWait").dialog("open");
    $.ajax({
        type: "POST",
        data: "",
        scriptCharset: "UTF-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "COFIDE_Programacionmails.jsp?ID=6",
        success: function (datos) {
            jQuery("#GRID_MAIL5").clearGridData();
            var lstXml = datos.getElementsByTagName("Mail")[0]; //dato padre
            var lstCte = lstXml.getElementsByTagName("datos"); //dato detalle
            for (var i = 0; i < lstCte.length; i++) {
                var objcte = lstCte[i];
                //llenamos el grid
                var datarow = {
                    CC_CURSO_ID: objcte.getAttribute("id"),
                    CC_SEDE_ID: objcte.getAttribute("id_sede"),
                    CC_CLAVES: objcte.getAttribute("clave"),
                    CC_FECHA_INICIAL: objcte.getAttribute("fecini"),
                    CC_TEMPLATE2: objcte.getAttribute("t2"),
                    CC_AREAS: objcte.getAttribute("areas"),
                    CC_GIROS: objcte.getAttribute("giros"),
                    CC_SEDE: objcte.getAttribute("sede"),
                    CC_NOMBRE_CURSO: objcte.getAttribute("nombre"),
                    CC_MAILGROUP: objcte.getAttribute("grupo"),
                    CC_CONFIRMA_MAIL: objcte.getAttribute("confirma")
                }; //fin del grid
                CountRow++;
                jQuery("#GRID_MAIL5").addRowData(CountRow, datarow, "last");
            }
        } //fin de la funcion que recupera los datos
    }); //fin del ajax
    $("#dialogWait").dialog("close");
}

function AsignaTemplate10() { //asigna el template a cursos masivos de 10 dias
    var strTemp = document.getElementById("MAIL_TEMPLATE10").value;
    var intLength = 0;
    if (strTemp != "0") { //valida si es manual para no pedir cursos
        var grid = jQuery("#GRID_MAIL");
        var ids = grid.getGridParam("selarrrow");
        intLength = ids.length;
        if (intLength > 0 || strTemp == "1" || strTemp == "8" || strTemp == "9" || strTemp == "10") {
            if (strTemp == "8") {
                if (document.getElementById("MAIL_PLEALTAD").value == 0) {
                    alert("Seleccione los puntos de Lealtad.");
                    document.getElementById("MAIL_PLEALTAD").focus();
                } else {
                    AsignaTemplateDo();
                }
            } else {
                AsignaTemplateDo();
            }
        } else {
            alert("Es necesario elegir algun curso para la plantilla");
        }
    } else {
        alert("Selecciona una Plantilla");
    }
}

function cleanProgMailMasivo() {
    var strTemp = document.getElementById("MAIL_TEMPLATE10").value;
    //BLOQUEO DE PLANTILLAS TEMPORAL / PLANTILLA DE PUNTOS DE LEALTAD
    if (strTemp == "8") {
        jQuery("#GRID_MAIL").jqGrid('resetSelection');
//        document.getElementById("MAIL_SEGMENTO").value = 0;
//        setDialogGiros();
        alert("Esta plantilla aun no esta disponible, falta el contenido HTML");
        document.getElementById("MAIL_TEMPLATE10").value = "0";
    }
    if (strTemp == "9") { //HTML
        jQuery("#GRID_MAIL").jqGrid('resetSelection');
        showInputPlantilla();
    }
    if (strTemp == "10") { //COFIDEnet
        jQuery("#GRID_MAIL").jqGrid('resetSelection');
    }
}

function AsignaTemplateDo() {
    var strTemp = document.getElementById("MAIL_TEMPLATE10").value;
    var intLength = 0;
    var grid = jQuery("#GRID_MAIL");
    var ids = grid.getGridParam("selarrrow");
    intLength = ids.length;
    var strPost = "template=" + strTemp;
    strPost += "&length=" + intLength;
    strPost += "&tipo=1"; //masivo = 1

    for (var i = 0; i < intLength; i++) {
        var id = ids[i];
        var lstRow = grid.getRowData(id);
        strPost += "&id_curso" + i + "=" + lstRow.CC_CURSO_ID;
    }
    if (strTemp == "8") {
        strPost += "&id_curso=" + document.getElementById("MAIL_PLEALTAD").value;
    }
    if (strTemp == "9" || strTemp == "10") {
        strPost += "&id_curso=0";
    }
    $.ajax({
        type: "POST",
        data: strPost,
        scriptCharset: "UTF-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "html",
        url: "COFIDE_Programacionmails.jsp?ID=7",
        success: function (datos) {
            datos = trim(datos);
            document.getElementById("plantillapreview1").value = datos;
            alert("Plantilla creada!");
        },
        error: function (objeto, quepaso, otroobj, datos) {
            alert(":pto9:" + objeto + " " + quepaso + " " + otroobj + " ERROR: " + trim(datos));
            $("#dialogWait").dialog("close");
        }});
}

function AsignaTemplate5() {
    var strTemp = document.getElementById("MAIL_TEMPLATE5").value;
    var intLength = 0;
    if (strTemp != "0") {
        var grid = jQuery("#GRID_MAIL5");
        var ids = grid.getGridParam("selarrrow");
        intLength = ids.length;
        if (intLength > 0 || strTemp == "1") {
            var strPost = "template=" + strTemp;
            strPost += "&length=" + intLength;
            strPost += "&tipo=2"; //mailgroup = 2
            for (var i = 0; i < intLength; i++) {
                var id = ids[i];
                var lstRow = grid.getRowData(id);
                strPost += "&id_curso" + i + "=" + lstRow.CC_CURSO_ID;
            }
            $.ajax({
                type: "POST",
                data: strPost,
                scriptCharset: "UTF-8",
                contentType: "application/x-www-form-urlencoded;charset=utf-8",
                cache: false,
                dataType: "html",
                url: "COFIDE_Programacionmails.jsp?ID=7",
                success: function (datos) {
                    datos = trim(datos);
                    document.getElementById("plantillapreview2").value = datos;
                    alert("Plantilla creada!");
                },
                error: function (objeto, quepaso, otroobj, datos) {
                    alert(":pto9:" + objeto + " " + quepaso + " " + otroobj + " ERROR: " + trim(datos));
                    $("#dialogWait").dialog("close");
                }});
//                LoadMailCursos5();
        } else {
            alert("Es necesario elegir un curso para asigrnar una plantilla");
        }
    } else {
        alert("Selecciona una Plantilla");
    }
}

function setDialogGiros() {
    var strOptionGiro = "";
    var strOptionSede = "";
    var strOptionSegmento = "";
    $.ajax({
        type: "POST",
        data: "",
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "COFIDE_Telemarketing.jsp?ID=38",
        success: function (datos) {
            var lstXmlGiro = datos.getElementsByTagName("C_GIROS")[0];
            var lstGiros = lstXmlGiro.getElementsByTagName("datosGiro");
            strOptionGiro = "<option value=''>TODOS LOS GIROS</option>";
            for (var i = 0; i < lstGiros.length; i++) {
                var obj = lstGiros[i];
                strOptionGiro += "<option value='" + obj.getAttribute("CG_ID_M") + "'>" + obj.getAttribute("CG_GIRO") + "</option>";
            }

            var lstXmlSegMento = datos.getElementsByTagName("C_SEGMENTOS")[0];
            var lstSegmento = lstXmlSegMento.getElementsByTagName("datosSeg");
            strOptionSegmento = "<option value=''>TODOS LAS ÁREAS</option>";
            for (var i = 0; i < lstSegmento.length; i++) {
                var obj = lstSegmento[i];
                strOptionSegmento += "<option value='" + obj.getAttribute("CS_ID_M") + "'>" + obj.getAttribute("CS_AREA") + "</option>";
            }

            var lstXmlSede = datos.getElementsByTagName("C_SEDE")[0];
            var lstSede = lstXmlSede.getElementsByTagName("datosSede");
            strOptionSede = "<option value=''>TODOS LAS SEDES</option>";
            for (var i = 0; i < lstSede.length; i++) {
                var obj = lstSede[i];
                strOptionSede += "<option value='" + obj.getAttribute("CS_SEDE_ID") + "'>" + obj.getAttribute("CS_SEDE") + "</option>";
            }

            var strHTMLGiro = "";
            strHTMLGiro += "<table border=0 cellpadding=0>";
            strHTMLGiro += "<tr>";
            strHTMLGiro += "<td colspan=3>" + "SELECCIONAR GIROS" + "</td>";
            strHTMLGiro += "</tr>";
            strHTMLGiro += "<tr>";
            strHTMLGiro += "<td >" + "" + "<br><select id='origen_giro' multiple>" + strOptionGiro + "</select></td>";
            strHTMLGiro += "<td ><input type='button' id='Agregar' value='" + lstMsg[176] + "' onclick='AgregaGiroX()'><br><input type='button' id='Quitar' value='" + lstMsg[177] + "' onClick='deleteGiroNum()'></td>";
            strHTMLGiro += "<td >" + "" + "<br><select id='destino_giro' multiple ></select></td>";
            strHTMLGiro += "</tr>";
            strHTMLGiro += "</table>";
            document.getElementById("DIV_GIROS").innerHTML = strHTMLGiro;

            var strHTMLSegmento = "";
            strHTMLSegmento += "<table border=0 cellpadding=0>";
            strHTMLSegmento += "<tr>";
            strHTMLSegmento += "<td colspan=3>" + "SELECCIONAR ÁREAS" + "</td>";
            strHTMLSegmento += "</tr>";
            strHTMLSegmento += "<tr>";
            strHTMLSegmento += "<td >" + "" + "<br><select id='origen_segmento' multiple>" + strOptionSegmento + "</select></td>";
            strHTMLSegmento += "<td ><input type='button' id='Agregar' value='" + lstMsg[176] + "' onclick='AgregaSegmentoX()'><br><input type='button' id='Quitar' value='" + lstMsg[177] + "' onClick='deleteSegmentoNum()'></td>";
            strHTMLSegmento += "<td >" + "" + "<br><select id='destino_segmento' multiple ></select></td>";
            strHTMLSegmento += "</tr>";
            strHTMLSegmento += "</table>";
            document.getElementById("DIV_SEGMENTOS").innerHTML = strHTMLSegmento;

            var strHTMLSede = "";
            strHTMLSede += "<table border=0 cellpadding=0>";
            strHTMLSede += "<tr>";
            strHTMLSede += "<td colspan=3>" + "SELECCIONAR SEDES" + "</td>";
            strHTMLSede += "</tr>";
            strHTMLSede += "<tr>";
            strHTMLSede += "<td >" + "" + "<br><select id='origen_sede' multiple>" + strOptionSede + "</select></td>";
            strHTMLSede += "<td ><input type='button' id='Agregar' value='" + lstMsg[176] + "' onclick='AgregaSedeX()'><br><input type='button' id='Quitar' value='" + lstMsg[177] + "' onClick='deleteSedeNum()'></td>";
            strHTMLSede += "<td >" + "" + "<br><select id='destino_sede' multiple ></select></td>";
            strHTMLSede += "</tr>";
            strHTMLSede += "</table>";
            document.getElementById("DIV_SEDE").innerHTML = strHTMLSede;
        },
        error: function (objeto, quepaso, otroobj) {
            alert(":pto9:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }
    });
}//Fin setDialogGiros

/**Agrega GIROS seleccionados  */
function AgregaGiroX() {
    var objSelOr = document.getElementById("origen_giro");
    var objSelDest = document.getElementById("destino_giro");
    var intGiroIdx = d.getElementById("origen_giro").selectedIndex;
    var txtGiro = d.getElementById("origen_giro").options[intGiroIdx].text;
    for (var x = 0; x < objSelOr.length; x++) {
        if (objSelOr[x].selected) {
            select_add(objSelDest, txtGiro, objSelOr[x].value);
            objSelOr.remove(x);
        }
    }
}//Fin AgregaGiroX

/**Agrega SEGMENTOS seleccionados  */
function AgregaSegmentoX() {
    var objSelOr = document.getElementById("origen_segmento");
    var objSelDest = document.getElementById("destino_segmento");
    var intGiroIdx = d.getElementById("origen_segmento").selectedIndex;
    var txtGiro = d.getElementById("origen_segmento").options[intGiroIdx].text;
    for (var x = 0; x < objSelOr.length; x++) {
        if (objSelOr[x].selected) {
            select_add(objSelDest, txtGiro, objSelOr[x].value);
            objSelOr.remove(x);
        }
    }
}//Fin AgregaSegmentoX

/**Agrega SEDE seleccionados  */
function AgregaSedeX() {
    var objSelOr = document.getElementById("origen_sede");
    var objSelDest = document.getElementById("destino_sede");
    var intGiroIdx = d.getElementById("origen_sede").selectedIndex;
    var txtGiro = d.getElementById("origen_sede").options[intGiroIdx].text;
    for (var x = 0; x < objSelOr.length; x++) {
        if (objSelOr[x].selected) {
            select_add(objSelDest, txtGiro, objSelOr[x].value);
            objSelOr.remove(x);
        }
    }
}//Fin AgregaSedeX

//Elimina Giro de la lista Seleccionada
function deleteGiroNum() {
    var objSelOrigen = document.getElementById("destino_giro");
    var objSelDestino = document.getElementById("origen_giro");
    var intGiroIdx = d.getElementById("destino_giro").selectedIndex;
    var txtGiro = d.getElementById("destino_giro").options[intGiroIdx].text;
    for (var x = 0; x < objSelOrigen.length; x++) {
        if (objSelOrigen[x].selected) {
            select_add(objSelDestino, txtGiro, objSelOrigen[x].value);
            objSelOrigen.remove(x);
        }
    }
}//Fin deleteGiroNum

//Elimina Segmento de la lista Seleccionada
function deleteSegmentoNum() {
    var objSelOrigen = document.getElementById("destino_segmento");
    var objSelDestino = document.getElementById("origen_segmento");
    var intAreaIdx = d.getElementById("destino_segmento").selectedIndex;
    var txtArea = d.getElementById("destino_segmento").options[intAreaIdx].text;
    for (var x = 0; x < objSelOrigen.length; x++) {
        if (objSelOrigen[x].selected) {
            select_add(objSelDestino, txtArea, objSelOrigen[x].value);
            objSelOrigen.remove(x);
        }
    }
}//Fin deleteSegmentoNum

//Elimina Sede de la lista Seleccionada
function deleteSedeNum() {
    var objSelOrigen = document.getElementById("destino_sede");
    var objSelDestino = document.getElementById("origen_sede");
    var intSedeIdx = d.getElementById("destino_sede").selectedIndex;
    var txtSede = d.getElementById("destino_sede").options[intSedeIdx].text;
    for (var x = 0; x < objSelOrigen.length; x++) {
        if (objSelOrigen[x].selected) {
            select_add(objSelDestino, txtSede, objSelOrigen[x].value);
            objSelOrigen.remove(x);
        }
    }
}//Fin deleteSedeNum

//Obtiene numeros de GIROS seleccionados
function getNumerosGiro() {
    var _strSeries = "";
    var objSelDest = document.getElementById("destino_giro");
    for (var x = 0; x < objSelDest.length; x++) {
        _strSeries += objSelDest[x].value + ",";
    }
    return _strSeries;
}

//Obtiene numeros de Segmentos seleccionados
function getNumerosSegmentos() {
    var _strSeries = "";
    var objSelDest = document.getElementById("destino_segmento");
    for (var x = 0; x < objSelDest.length; x++) {
        _strSeries += objSelDest[x].value + ",";
    }
    return _strSeries;
}

//Obtiene numeros de Sede seleccionados
function getNumerosSede() {
    var _strSeries = "";
    var objSelDest = document.getElementById("destino_sede");
    for (var x = 0; x < objSelDest.length; x++) {
        _strSeries += objSelDest[x].value + ",";
    }
    return _strSeries;
}
function LoadPlantilla() {
    document.getElementById("TEMPLATE").innerHTML = document.getElementById("TEMPLATE_").value;
}
/**
 * 
 * @param {int} opc
 * 1 en el caso de vista previa de mail masivo
 * 2 en el caso de vista previa de mail group
 */
function VistaPreviaPlantilla(opc) {
    var strPlantilla = "";
    var strPost = "";
    if (opc == 1) {
        strPlantilla = document.getElementById("plantillapreview1").value;
    }
    if (opc == 2) {
        strPlantilla = document.getElementById("plantillapreview2").value;
    }
//    $("#dialogWait").dialog("open");
    Plantillavista(strPlantilla);
//    $("#dialogWait").dialog("close");
}
function Plantillavista(strPlantilla) {
    window.open("COFIDE_Mail.jsp?idPlantilla=" + strPlantilla, '_blank');
}

function dblClikShowStatus(id) {
    var strNomMain = objMap.getNomMain();
    var grid = jQuery("#GRID_HMAIL"); //nombre del grid detalle
    var lstVal = grid.getRowData(id);

    if (strNomMain == "GRID_HMAIL") { //pantalla que lo contiene
        OpnEdit(document.getElementById("Ed" + strNomMain));
    } else {
        if (strNomMain == "CofMailMasivo") { //pantalla principal
            var strIdmasivo = lstVal.CRM_ID;
            strIdMTMP = lstVal.CRM_ID; //guarda el id para consultas posteriroes sin elegir masivo
            clearTimeout(timTimerMail); //reinicia el timer
            ShowStatus(strIdMTMP);
        }
    }
}
function ShowStatus(strIdmasivo) {
    $("#dialogWait").dialog("open");
    $.ajax({
        type: "POST",
        data: "idMasivo=" + strIdmasivo,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "COFIDE_Programacionmails.jsp?ID=11",
        success: function (datos) {
            var lstXml = datos.getElementsByTagName("Mail")[0]; //dato padre
            var lstCte = lstXml.getElementsByTagName("datos"); //dato detalle
            for (var i = 0; i < lstCte.length; i++) {
                var objcte = lstCte[i];
                document.getElementById("MAIL_TOTAL").value = objcte.getAttribute("proceso");
                document.getElementById("MAIL_OK").value = objcte.getAttribute("exito");
                document.getElementById("MAIL_DELIVERY").value = objcte.getAttribute("delivery");
            }
            $("#dialogWait").dialog("close");
            timTimerMail = setTimeout("ShowStatus(strIdMTMP)", 60000); //inicia el timer
        }, error: function (objeto, quepaso, otroobj) {
            alert(":pto=11: " + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }});
}


/**
 * activa el quartz en el primer envio masivo
 * @returns {undefined}
 */
function TurnOnQuartz() {
    $("#dialogWait").dialog("open");
    $.ajax({
        type: "POST",
        data: "",
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "html",
        url: "ERP_Quartz.jsp?id=5",
        success: function (datos) {
            datos = trim(datos);
            if (datos == "OFF") {
                console.log("Activa el motor");
                //activa la tarea en automatico
                activarTareaAuto();
            } else {
                if (datos == "ON") {
                    console.log("Ya se encuentra activo el motor.");
                } else {
                    alert(datos);
                }
            }
            $("#dialogWait").dialog("close");
        }, error: function (data) {
            $("#dialogWait").dialog("close");
            alert("hubó un problema con el motor de correos: " + data);
        }
    });
}
function activarTareaAuto() {
//    borrarTarea(true);//inicio de la tarea, y si ya esta activa se reinicia
    var strPost = "nameJob=MAIL_MASIVO";
    strPost += "&nameTrigger=trigg_MAIL_MASIVO";
    // cron = *(0-59) *(0-23) *(1-31) *(1-12 or Jan-Dec) *(0-6 or Sun-Sat)
    //         Minute   HR      DAY         MONTH               DAY WEEK
//    strPost += "&cron=10 * * * * ?"; //cada media hora de lunes, miercoles y viernes
    strPost += "&cron=0 0,2,4,6,8,10,12,14,16,18,20,22,24,26,28,30,32,34,36,38,40,42,44,46,48,50,52,54,56,58 * * * ?";
    //peticion por ajax para inicializar un job
    $("#dialogWait").dialog("open");
    $.ajax({
        type: "POST",
        data: strPost,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "html",
        url: "ERP_Quartz.jsp?id=1",
        success: function (datos) {
            //Asignamos valores recuperamos
            if (trim(datos) == "OK") {
                console.log("Tarea inicializada...");
            }
            $("#dialogWait").dialog("close");
        }, error: function (data) {
            $("#dialogWait").dialog("close");
            alert(data);
        }
    });
}

//funcion para ingresar el codogp HTML de una plantilla personalizada
function showInputPlantilla() {
    var strHtml = "    <div style=\"width:300px; \">";
    strHtml += "        <div>";
    strHtml += "            <label>DIGITOS DE REFERENCIA</label>";
    strHtml += "            <BR />";
    strHtml += "            <input id=\"FAC_DIGITO\" type=\"text\" placeholder=\"DIGITO DE REFERENCIA\" size=\"50\" />";
    strHtml += "        </div>";
    strHtml += "        <div>";
    strHtml += "            <div>";
    strHtml += "                <label>METODO DE PAGO</label>";
    strHtml += "                <BR />";
    strHtml += "                <select id=\"FAC_METODO\" >";
    strHtml += "                    <option value=\"0\">SELECCIONAR...</option>";
    strHtml += "                </select>";
    strHtml += "            </div>";
    strHtml += "            <div>";
    strHtml += "                <div>";
    strHtml += "                    <label>FORMA DE PAGO</label>";
    strHtml += "                    <BR />";
    strHtml += "                    <select id=\"FAC_FORMA\" >";
    strHtml += "                        <option value=\"0\">SELECCIONAR...</option>";
    strHtml += "                    </select>";
    strHtml += "                </div>";
    strHtml += "                <div style=\"text-align:center\">";
    strHtml += "                    <input type=\"button\" onclick=\"\" value=\"GUARDAR CAMBIO\" />";
    strHtml += "                    <input type=\"button\" onclick=\"\" value=\"SALIR SIN GUARDAR\" />";
    strHtml += "                </div>";
    strHtml += "            </div>";
    strHtml += "        </div>";
    strHtml += "    </div>";

    document.getElementById("dialogGen_inside").innerHTML = strHtml;
    $("#dialogGen").dialog("option", "title", "EDITAR INFORMAICÓN DE FACTURACIÓN.");
    $("#dialogGen").dialog("open");
}

function CerrarinputPlantilla() {
    $("#dialogGen").dialog("close");
}

//subir plantilla
function subirPlantilla() {
    var strDoc = "";
    var strArchivo = document.getElementById("archivo_plantilla");
    if (strArchivo.value != "") {
        strDoc = strArchivo.value.split(".", 1);
    }
    var File = document.getElementById("archivo_plantilla");
    if (File.value == "") {
        alert("Requiere seleccionar un archivo");
        File.focus();
    } else {
        if (Right(File.value.toUpperCase(), 3) == "TXT" || Right(File.value.toUpperCase(), 4) == "HTML") {
            $("#dialogWait").dialog("open");
            $.ajaxFileUpload({
                url: "COFIDE_GetSetPlantilla.jsp?name=" + strDoc,
                secureuri: false,
                fileElementId: "archivo_plantilla",
                dataType: "json",
                success: function (data, status) {
                    if (typeof (data.error) != "undefined") {
                        if (data.error != "") {
                            alert(data.error);
                        } else {
                            alert("Plantilla guardada: " + data.msg);
                            document.getElementById("plantilla").value = data.msg;
                            strArchivo.value = "";
                            CerrarinputPlantilla();
                        }
                    }
                    $("#dialogWait").dialog("close");
                }, error: function (data, status, e) {
                    alert(e);
                    $("#dialogWait").dialog("close");
                }});
        } else {
            alert("Se aceptan archivos con extension txt u html");
            File.focus();
        }
    }
}