/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var strCursoEdita = 0;
var strCursoClave = 0;
var strCursoDescripcion = 0;
var strMesConsulta = 0;
var strAnioConsulta = 0;
var strNomScreen = "";

function cofide_cursos_impartir() {
}

function initCursosImp() {
    getCatalogoCursosImp();
}
//obtiene los cursos con 3 meses de antiguedad
function getCatalogoCursosImp() {
    var strAnio = document.getElementById("CI_ANIO").value;
    var strMes = document.getElementById("CI_MES").value;
    $("#dialogWait").dialog("open");
    $.ajax({
        type: "POST",
        data: "strMes=" + strMes + "&strAnio=" + strAnio,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "COFIDE_CursosImpartir.jsp?id=1",
        success: function (datos) {
            var intDisponibles = 0;
            jQuery("#CI_GR").clearGridData();
            var lstXml = datos.getElementsByTagName("CursosImpartir")[0];
            var lstCI = lstXml.getElementsByTagName("datos");
            for (var i = 0; i < lstCI.length; i++) {
                var obj = lstCI[i];
                var rowCursos = {
                    CI_CONTADOR: getMaxGridCursosImpartir("#CI_GR"),
                    CCU_ID_M: obj.getAttribute("CCU_ID_M"),
                    CCU_CURSO: obj.getAttribute("CCU_CURSO"),
                    CCU_CURSO_VIEW: obj.getAttribute("CCU_CURSO"),
                    SEDE_ASIGNADA: obj.getAttribute("SEDE_ASIGNADA"),
                    FECHA_ASIGNADA: obj.getAttribute("FECHA_ASIGNADA"),
                    CC_SUGERENCIA: obj.getAttribute("CC_SUGERENCIA")
                };
                jQuery("#CI_GR").addRowData(getMaxGridCursosImpartir("#CI_GR"), rowCursos, "last");
                if (obj.getAttribute("SEDE_ASIGNADA") == "") {
                    intDisponibles++;
                }
            }
            document.getElementById("CI_SEDES_DISP").value = intDisponibles;
            if (objMap.getNomMain() == "CRS_IMPARTIR") {
                DrawGridCursosImp();
            }
            $("#dialogWait").dialog("close");
        },
        error: function (objeto, quepaso, otroobj) {
            alert(":pto9:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }
    });
}

function getMaxGridCursosImpartir(strNomGr) {
    var intLenght = jQuery(strNomGr).getDataIDs().length + 1;
    return intLenght;
}//Fin getMaxGridFletePedido

function getMaxGridCursosImpartirValue(strNomGr) {
    var grid = jQuery(strNomGr);
    var arr = grid.getDataIDs();
    var intValue = 0;
    //recorremos cada fila del row
    for (var i = 0; i < arr.length; i++) {
        var id = arr[i];
        var lstRow = grid.getRowData(id);
        intValue = parseInt(intValue) + 1;
        lstRow.CII_CONTADOR = intValue;
        grid.setRowData(id, lstRow);
        grid.trigger("reloadGrid");
    }
}//Fin getMaxGridFletePedido

function OpnSeleccionaInstructor() {
    var strMes = document.getElementById("CI_MES").value;
    if (strMes > 0) {
//        var grid = jQuery("#CI_GR");
//        if (grid.getGridParam("selrow")) {
//            var id = grid.getGridParam("selrow");
//            var lstVal = grid.getRowData(id);
//            if (lstVal.SEDE_ASIGNADA == "") {
//                alert("No se ha asignado sede al curso");
//            } else {
        var objSecModiVta = objMap.getScreen("SEL_INSTRUCTOR");
        if (objSecModiVta != null) {
            objSecModiVta.bolActivo = false;
            objSecModiVta.bolMain = false;
            objSecModiVta.bolInit = false;
            objSecModiVta.idOperAct = 0;
        }
        OpnOpt("SEL_INSTRUCTOR", "_ed", "dialog", false, false, true);
//            }
//        } else {
//            alert("Selecciona una fila en el tab Detalles");
//        }
    } else {
        alert("Selecciona un Mes.");
    }
}

function OpnProgramCurso() {

    strCursoEdita = 0;
    strCursoClave = 0;
    strCursoDescripcion = 0;
    strMesConsulta = 0;
    strAnioConsulta = 0;
    if (document.getElementById("CI_MES").value > 0 || document.getElementById("CI_MES").value != "0") {
        var grid = jQuery("#CI_GR");
        if (grid.getGridParam("selrow")) {
            var id = grid.getGridParam("selrow");
            var lstVal = grid.getRowData(grid.getGridParam("selrow"));
            strCursoDescripcion = lstVal.CCU_CURSO_VIEW;
            strCursoClave = lstVal.CCU_ID_M;
            strMesConsulta = document.getElementById("CI_MES").value;
            strAnioConsulta = document.getElementById("CI_ANIO").value;
            myLayout.open("west");
            myLayout.open("east");
            myLayout.open("south");
            myLayout.open("north");
            document.getElementById("MainPanel").innerHTML = "";
            //Limpiamos el objeto en el framework para que nos deje cargarlo enseguida
            var objMainFacPedi = objMap.getScreen("CRS_IMPARTIR");
            objMainFacPedi.bolActivo = false;
            objMainFacPedi.bolMain = false;
            objMainFacPedi.bolInit = false;
            objMainFacPedi.idOperAct = 0;
            strNomScreen = "CRS_IMPARTIR";
            OpnOpt('PROG_CURSO', '_ed', null, true, true, true);
        } else {
            alert("Selecciona una fila en el tab Detalles");
        }
    } else {
        alert("Selecciona un Mes");
    }
}


function initProgramarCurso() {
    document.getElementById("CURSO_DESCRIPCION").value = "";
    document.getElementById("CURSO_CLAVE").value = "";
    document.getElementById("MES_CONSULTA").value = "";
    document.getElementById("ANIO_CONSULTA").value = "";
    var strOptionSelect = "<option value='0'>Seleccione</option>";
    var strHTML = "<table cellpadding=\"4\" cellspacing=\"1\" border=\"0\" >SEDE/FECHA DISPONIBLE: ";
    strHTML += " <td><select id=\"bodegasSelect\" name=\"bodegasSelect\"  class=\"outEdit\" onblur=\"QuitaFoco(this)\" onfocus=\"PonFoco(this)\" 0=\"\" > " + strOptionSelect + " < /select></td>";
    strHTML += "  </table>";
    document.getElementById("PRC_SEDE_FEC_DISP").innerHTML = strHTML;

    var strOptionSelectInst = "<option value='0'>Seleccione</option>";
    var strHTMLInst = "<table cellpadding=\"4\" cellspacing=\"1\" border=\"0\" >INSTRUCTOR: ";
    strHTMLInst += " <td><select id=\"instructorSelect\" name=\"instructorSelect\"  class=\"outEdit\" onblur=\"QuitaFoco(this)\" onfocus=\"PonFoco(this)\" 0=\"\" > " + strOptionSelectInst + " < /select></td>";
    strHTMLInst += "  </table>";
    document.getElementById("PRC_INSTRUCTOR_ID").innerHTML = strHTMLInst;
    document.getElementById("CURSO_DESCRIPCION").value = "";
    document.getElementById("CURSO_CLAVE").value = "";
    document.getElementById("MES_CONSULTA").value = "";
    document.getElementById("ANIO_CONSULTA").value = "";
    jQuery("#GR_CURSO").clearGridData();
//    if(typeof(strCursoEdita) != "undefined"){
    setCursoEditar();
//    }
    setSedeDisponible();
    setInstructorDisponible();
}//Fin initProgramarCurso

function setCursoEditar() {
    if (typeof (strCnfCursoClave) != "undefined" && strNomScreen != "CRS_IMPARTIR") {
        document.getElementById("CURSO_DESCRIPCION").value = strCnfCursoDescripcion;
        document.getElementById("CURSO_CLAVE").value = strCnfCursoClave;
        document.getElementById("MES_CONSULTA").value = strCnfMesConsulta;
        document.getElementById("ANIO_CONSULTA").value = strCnfAnioConsulta;
        strCursoEdita = strCnfCursoEdita;
        getCursoMesesanteriores();
    } else {
        document.getElementById("CURSO_DESCRIPCION").value = strCursoDescripcion;
        document.getElementById("CURSO_CLAVE").value = strCursoClave;
        document.getElementById("MES_CONSULTA").value = strMesConsulta;
        document.getElementById("ANIO_CONSULTA").value = strAnioConsulta;
        getCursoMesesanteriores();
    }
}//Fin setCursoEditar  

function setSedeDisponible() {
    var strAnio = document.getElementById("ANIO_CONSULTA").value;
    var strMes = document.getElementById("MES_CONSULTA").value;
    var strOptionSelect = "<option value='0'>Seleccione</option>";
    var strHTML = "<table cellpadding=\"4\" cellspacing=\"1\" border=\"0\" >SEDE/FECHA DISPONIBLE: <br>";
    $("#dialogWait").dialog("open");
    $.ajax({
        type: "POST",
        data: "strMes=" + strMes + "&strAnio=" + strAnio + "&CursoEditar=" + strCursoEdita,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "COFIDE_CursosImpartir.jsp?id=2",
        success: function (datos) {
            var lstXml = datos.getElementsByTagName("SedeDisponible")[0];
            var lstprecio = lstXml.getElementsByTagName("datos");
//            if (lstprecio.length == 0) {
//                $("#dialogWait").dialog("close");
//                alert("No hay Sedes Disponibles.");
//                callExitProgramCurso();
//            } else {
            for (var i = 0; i < lstprecio.length; i++) {
                var obj = lstprecio[i];
//                    if (strCursoEdita != null || strCursoEdita != "undefined") {
                if (typeof (strCursoEdita) != "undefined") {
                    if (strCursoEdita == obj.getAttribute("CC_CURSO_ID")) {
                        strOptionSelect += "<option value='" + obj.getAttribute("CC_CURSO_ID") + "' selected>" + obj.getAttribute("CCU_CURSO") + "</option>";
                    } else {
                        strOptionSelect += "<option value='" + obj.getAttribute("CC_CURSO_ID") + "' >" + obj.getAttribute("CCU_CURSO") + "</option>";
                    }
                } else {
                    strOptionSelect += "<option value='" + obj.getAttribute("CC_CURSO_ID") + "' >" + obj.getAttribute("CCU_CURSO") + "</option>";
                }
            }
            strHTML += " <select id=\"SedeSelect\" name=\"SedeSelect\"  class=\"outEdit\" onblur=\"QuitaFoco(this)\" onfocus=\"PonFoco(this)\" 0=\"\" > " + strOptionSelect + " < /select>";
            strHTML += "  </table>";
            document.getElementById("PRC_SEDE_FEC_DISP").innerHTML = strHTML;

//                if (strCursoEdita != null || strCursoEdita != "undefined" || strCursoEdita != "0" || strCursoEdita != 0) {
            if (typeof (strCursoEdita) != "undefined") {
                var lstXmlEdit = datos.getElementsByTagName("CursoEdit")[0];
                var lstCEdit = lstXmlEdit.getElementsByTagName("datos");
                for (var i = 0; i < lstCEdit.length; i++) {
                    var obj = lstCEdit[i];

                    if (obj.getAttribute("CC_PROGRAMAR") == 1) {
                        document.getElementById("PRC_PROGRAMAR1").checked = true;
                    } else {
                        document.getElementById("PRC_PROGRAMAR2").checked = true;
                    }
                    if (obj.getAttribute("CC_IS_PRESENCIAL") == 1) {
                        document.getElementById("PRC_PRESENCIAL1").checked = true;
                    } else {
                        document.getElementById("PRC_PRESENCIAL2").checked = true;
                    }
                    if (obj.getAttribute("CC_IS_ONLINE") == 1) {
                        document.getElementById("PRC_ONLINE1").checked = true;
                    } else {
                        document.getElementById("PRC_ONLINE2").checked = true;
                    }
                    document.getElementById("PRC_INSTRUCTOR_ID").value = obj.getAttribute("CC_INSTRUCTOR_ID");
                }
            }
//            }
        },
        error: function (objeto, quepaso, otroobj) {
            alert(":pto9:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }
    });
}//Fin setSedeDisponible

function getCursoMesesanteriores() {
    var strAnio = document.getElementById("ANIO_CONSULTA").value;
    var strMes = document.getElementById("MES_CONSULTA").value;
    var strClave = document.getElementById("CURSO_CLAVE").value;
    $("#dialogWait").dialog("open");
    $.ajax({
        type: "POST",
        data: "strMes=" + strMes + "&strAnio=" + strAnio + "&Clave=" + strClave,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "COFIDE_CursosImpartir.jsp?id=3",
        success: function (datos) {
            jQuery("#GR_CURSO").clearGridData();
            var lstXml = datos.getElementsByTagName("MesesAnteriores")[0];
            var lstCI = lstXml.getElementsByTagName("datos");
            for (var i = 0; i < lstCI.length; i++) {
                var obj = lstCI[i];

                var rowCursosImp = {
                    CCU_CONTADOR: getMaxGridCursosImpartir("#GR_CURSO"),
                    CCU_CURSO: document.getElementById("CURSO_DESCRIPCION").value,
                    CCU_MES1: obj.getAttribute("CCU_MES1"),
                    CCU_MES2: obj.getAttribute("CCU_MES2"),
                    CCU_MES3: obj.getAttribute("CCU_MES3"),
                    CCU_MES4: obj.getAttribute("CCU_MES4"),
                    CCU_MES5: obj.getAttribute("CCU_MES5"),
                    CCU_MES6: obj.getAttribute("CCU_MES6"),
                    CCU_MES7: obj.getAttribute("CCU_MES7"),
                    CCU_MES8: obj.getAttribute("CCU_MES8"),
                    CCU_MES9: obj.getAttribute("CCU_MES9"),
                    CCU_MES10: obj.getAttribute("CCU_MES10"),
                    CCU_MES11: obj.getAttribute("CCU_MES11"),
                    CCU_MES12: obj.getAttribute("CCU_MES12")
                };
                jQuery("#GR_CURSO").addRowData(getMaxGridCursosImpartir("#GR_CURSO"), rowCursosImp, "last");
                jQuery("#GR_CURSO").setLabel("CCU_MES1", obj.getAttribute("CCU_MES_DESC1"));
                jQuery("#GR_CURSO").setLabel("CCU_MES2", obj.getAttribute("CCU_MES_DESC2"));
                jQuery("#GR_CURSO").setLabel("CCU_MES3", obj.getAttribute("CCU_MES_DESC3"));
                jQuery("#GR_CURSO").setLabel("CCU_MES4", obj.getAttribute("CCU_MES_DESC4"));
                jQuery("#GR_CURSO").setLabel("CCU_MES5", obj.getAttribute("CCU_MES_DESC5"));
                jQuery("#GR_CURSO").setLabel("CCU_MES6", obj.getAttribute("CCU_MES_DESC6"));
                jQuery("#GR_CURSO").setLabel("CCU_MES7", obj.getAttribute("CCU_MES_DESC7"));
                jQuery("#GR_CURSO").setLabel("CCU_MES8", obj.getAttribute("CCU_MES_DESC8"));
                jQuery("#GR_CURSO").setLabel("CCU_MES9", obj.getAttribute("CCU_MES_DESC9"));
                jQuery("#GR_CURSO").setLabel("CCU_MES10", obj.getAttribute("CCU_MES_DESC10"));
                jQuery("#GR_CURSO").setLabel("CCU_MES11", obj.getAttribute("CCU_MES_DESC11"));
                jQuery("#GR_CURSO").setLabel("CCU_MES12", obj.getAttribute("CCU_MES_DESC12"));
            }
            $("#dialogWait").dialog("close");
        },
        error: function (objeto, quepaso, otroobj) {
            alert(":pto9:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }
    });
}//Fin getCursoMesesanteriores

function saveCursoProgramacion() {
    var intSedeSelect = document.getElementById("SedeSelect").value;
    var strInstructor = getInstructoresCurso();
    var strHrsInstructor = getHorasInstructor();
    var intCursoClave = document.getElementById("CURSO_CLAVE").value;
    var intCursoDesc = document.getElementById("CURSO_DESCRIPCION").value;
//    var textSelect = d.getElementById("PRC_INSTRUCTOR_ID").options[intInstructor].text;
    if (intSedeSelect > 0) {
        if (strInstructor != "") {
            $("#dialogWait").dialog("open");
            var strPost = "SEDE=" + intSedeSelect;
            strPost += "&INSTRUCTOR=" + strInstructor;
            strPost += "&HORAS_INSTRUCTOR=" + strHrsInstructor;
            strPost += "&CursoClave=" + intCursoClave;
            strPost += "&CursoDesc=" + encodeURIComponent(intCursoDesc);
            if (document.getElementById("PRC_PROGRAMAR1").checked) {
                strPost += "&SePrograma=1";
            } else {
                strPost += "&SePrograma=0";
            }
            if (document.getElementById("PRC_PRESENCIAL1").checked) {
                strPost += "&Presencial=1";
            } else {
                strPost += "&Presencial=0";
            }
            if (document.getElementById("PRC_ONLINE1").checked) {
                strPost += "&Online=1";
            } else {
                strPost += "&Online=0";
            }
            $.ajax({
                type: "POST",
                data: encodeURI(strPost),
                scriptCharset: "UTF-8",
                contentType: "application/x-www-form-urlencoded;charset=utf-8",
                cache: false,
                dataType: "html",
                url: "COFIDE_CursosImpartir.jsp?id=4",
                success: function (datos) {
                    if (datos.substring(0, 2) == "OK") {
                        callExitProgramCurso();
                        $("#dialogWait").dialog("close");
                    } else {
                        $("#dialogWait").dialog("close");
                        alert(datos);
                    }
                    $("#dialogWait").dialog("close");
                }
            }); //fin del ajax
        } else {
            alert("Selecciona Instructor!");
        }
    } else {
        alert("Selecciona Sede!");
    }
}//Fin saveCursoProgramacion

function callExitProgramCurso() {
    myLayout.open("west");
    myLayout.open("east");
    myLayout.open("south");
    myLayout.open("north");
    document.getElementById("MainPanel").innerHTML = "";
    //Limpiamos el objeto en el framework para que nos deje cargarlo enseguida
    var objMainFacPedi = objMap.getScreen("PROG_CURSO");
    objMainFacPedi.bolActivo = false;
    objMainFacPedi.bolMain = false;
    objMainFacPedi.bolInit = false;
    objMainFacPedi.idOperAct = 0;
    javascript:OpnOpt('CRS_IMPARTIR', '_ed', null, true, true, true);
}//Fin callExitProgramCurso

function sendMailInstructor() {
    var strAnio = document.getElementById("CI_ANIO").value;
    var strMes = document.getElementById("CI_MES").value;
    var intInstructor = document.getElementById("INSTRUCTOR_ID").value;
    if (intInstructor > 0) {
        $("#dialogWait").dialog("open");
        var strPost = "IdInstructor=" + intInstructor;
        strPost += "&Anio=" + strAnio;
        strPost += "&Mes=" + strMes;
        $.ajax({
            type: "POST",
            data: strPost,
            scriptCharset: "UTF-8",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "html",
            url: "COFIDE_CursosImpartir.jsp?id=5",
            success: function (datos) {
                if (datos.substring(0, 2) == "OK") {
                    $("#dialogWait").dialog("close");
                    $("#dialog").dialog("close");
                } else {
                    $("#dialogWait").dialog("close");
                    $("#dialog").dialog("close");
                    alert(datos);
                }
                $("#dialogWait").dialog("close");
            }
        }); //fin del ajax
    } else {
        alert("Selecciona Instructor!");
    }
}//Fin sendMailInstructor


function OpnEvaluacionInstructor() {
    var intInstructor = document.getElementById("instructorSelect").value;
    if (intInstructor > 0) {
        var objSecModiVta = objMap.getScreen("CALIF_INSTR");
        if (objSecModiVta != null) {
            objSecModiVta.bolActivo = false;
            objSecModiVta.bolMain = false;
            objSecModiVta.bolInit = false;
            objSecModiVta.idOperAct = 0;
        }
        OpnOpt("CALIF_INSTR", "_ed", "dialog", false, false, true);
    } else {
        alert("Selecciona una Instructor");
        document.getElementById("instructorSelect").focus();
    }
}//Fin OpnEvaluacionInstructor

function initEvalInstructor() {
    getInfoInstructor();
}//Fin initEvalInstructor

function getInfoInstructor() {
    var strAnio = document.getElementById("INSTR_ANIO").value;
    var strMes = document.getElementById("INSTR_MES").value;
    var strInstructor = document.getElementById("instructorSelect").value;
    $("#dialogWait").dialog("open");
    $.ajax({
        type: "POST",
        data: "strMes=" + strMes + "&strAnio=" + strAnio + "&Instructor=" + strInstructor + "&opc=1", //opc = 1, viene desde programacion de cursos
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "COFIDE_CursosImpartir.jsp?id=6",
        success: function (datos) {
            var idSelect = d.getElementById("instructorSelect").selectedIndex;
            var textSelect = d.getElementById("instructorSelect").options[idSelect].text;
            jQuery("#GR_INSTRUCTOR").clearGridData();
            var lstXml = datos.getElementsByTagName("EvaluacionIns")[0];
            var lstCI = lstXml.getElementsByTagName("datos");
            for (var i = 0; i < lstCI.length; i++) {
                var obj = lstCI[i];
                var rowCursos = {
                    GR_CONTADOR: getMaxGridCursosImpartir("#GR_INSTRUCTOR"),
                    GR_CURSODESC: obj.getAttribute("GR_CURSODESC"),
                    GR_CURSODPROM: obj.getAttribute("GR_CURSODPROM"),
                    GR_CURSOFECHA: obj.getAttribute("GR_CURSOFECHA"),
                    GR_CURSONOEVAL: obj.getAttribute("GR_CURSONOEVAL"),
                    GR_CURSONOASIST: obj.getAttribute("GR_CURSONOASIST"),
                    GR_CURSOSEDE: obj.getAttribute("GR_CURSOSEDE")
                };
                jQuery("#GR_INSTRUCTOR").addRowData(getMaxGridCursosImpartir("#GR_INSTRUCTOR"), rowCursos, "last");
            }
            document.getElementById("INSTR_NOMBRE").value = textSelect;
            $("#dialogWait").dialog("close");
        },
        error: function (objeto, quepaso, otroobj) {
            alert(":pto9:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }
    });
}//Fin getInfoInstructor

function closeEvalInstructor() {
    $("#dialog").dialog("close");
}


function setInstructorDisponible() {
    var strOptionSelect = "<option value='0'>Seleccione</option>";
    var strHTML = "<table cellpadding=\"4\" cellspacing=\"1\" border=\"0\" >INSTRUCTOR: <br>";
    $("#dialogWait").dialog("open");
    $.ajax({
        type: "POST",
        data: "Curso=" + document.getElementById("CURSO_CLAVE").value,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "COFIDE_CursosImpartir.jsp?id=7",
        success: function (datos) {
            var lstXml = datos.getElementsByTagName("InstructorDisponible")[0];
            var lstprecio = lstXml.getElementsByTagName("datos");
//            if (lstprecio.length == 0) {
//                $("#dialogWait").dialog("close");
//                alert("No hay Instructores Asignados a este curso.");
//                callExitProgramCurso();
//            } else {
            for (var i = 0; i < lstprecio.length; i++) {
                var obj = lstprecio[i];
//                if (document.getElementById("PRC_INSTRUCTOR_ID").value != "undefined" || document.getElementById("PRC_INSTRUCTOR_ID").value != 0) {
//                    if (document.getElementById("PRC_INSTRUCTOR_ID").value == obj.getAttribute("CI_INSTRUCTOR_ID")) {
//                        strOptionSelect += "<option value='" + obj.getAttribute("CI_INSTRUCTOR_ID") + "' selected>" + obj.getAttribute("CI_INSTRUCTOR") + "</option>";
//                    } else {
                strOptionSelect += "<option value='" + obj.getAttribute("CI_INSTRUCTOR_ID") + "' >" + obj.getAttribute("CI_INSTRUCTOR") + "</option>";
//                    }
//                }

//                    strOptionSelect += "<option value='" + obj.getAttribute("CI_INSTRUCTOR_ID") + "' >" + obj.getAttribute("CI_INSTRUCTOR") + "</option>";
            }
            strHTML += " <select id=\"instructorSelect\" name=\"instructorSelect\"  class=\"outEdit\" onblur=\"QuitaFoco(this)\" onfocus=\"PonFoco(this)\" 0=\"\" > " + strOptionSelect + " < /select>";
            if (typeof (strCursoEdita) != "undefined" && (strCursoEdita != 0 || strCursoEdita != "0")) {
                getInstructoresImpartir();
            }
            document.getElementById("PRC_INSTRUCTOR_ID").innerHTML = strHTML;
            $("#dialogWait").dialog("close");
//            }
        },
        error: function (objeto, quepaso, otroobj) {
            alert(":pto9:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }
    });
}//Fin setInstructorDisponible



function DrawGridCursosImp() {
//Obtenemos los id's del grid
    var grid = jQuery("#CI_GR");
    var arr = grid.getDataIDs();
    //recorremos cada fila del row
    for (var i = 0; i < arr.length; i++) {
        var id = arr[i];
        //por cada fila extraemos las fechas
        var lstRow = grid.getRowData(id);
        if (lstRow.CC_SUGERENCIA == "NO") {
            lstRow.CCU_CURSO = "<div style=\"background-color:yellow;\"><font color=\"red\">" + lstRow.CCU_CURSO + "</font></div>";
        }
        grid.setRowData(id, lstRow);
        grid.trigger("reloadGrid");
    }
}

function addInstructorCursoImp() {
    var intIdInstructor = document.getElementById("instructorSelect").value;
    var dblNumHrs = document.getElementById("II_NUM_HORAS").value;

    if (intIdInstructor != 0) {
        if (dblNumHrs > 0) {
            if (!ExistInstructor(intIdInstructor)) {
                $("#dialogWait").dialog("open");
                $.ajax({
                    type: "POST",
                    data: "IdInstructor=" + intIdInstructor,
                    scriptCharset: "utf-8",
                    contentType: "application/x-www-form-urlencoded;charset=utf-8",
                    cache: false,
                    dataType: "xml",
                    url: "COFIDE_CursosImpartir.jsp?id=8",
                    success: function (datos) {
                        jQuery("#CI_GR").clearGridData();
                        var lstXml = datos.getElementsByTagName("NombreInstructor")[0];
                        var lstCI = lstXml.getElementsByTagName("datos");
                        for (var i = 0; i < lstCI.length; i++) {
                            var obj = lstCI[i];
                            var strNombreInstructor = obj.getAttribute("CI_INSTRUCTOR");
                            var rowCursos = {
                                CII_CONTADOR: getMaxGridCursosImpartir("#GR_INSTRUCTORES"),
                                CII_ID_INSTR: intIdInstructor,
                                CCI_INSTRUCTOR: strNombreInstructor,
                                II_NUM_HORAS: dblNumHrs
                            };
                            jQuery("#GR_INSTRUCTORES").addRowData(getMaxGridCursosImpartir("#GR_INSTRUCTORES"), rowCursos, "last");
                            getMaxGridCursosImpartirValue("#GR_INSTRUCTORES");
                        }
                        $("#dialogWait").dialog("close");
                    },
                    error: function (objeto, quepaso, otroobj) {
                        alert(":pto9:" + objeto + " " + quepaso + " " + otroobj);
                        $("#dialogWait").dialog("close");
                    }
                });
            } else {
                alert("Ya se agrego el Instructor.");
            }
        } else {
            alert("Agregar Numero de Horas para el Instructor.");
            document.getElementById("II_NUM_HORAS").focus();
        }
    } else {
        alert("Seleccione un instructor.");
    }
}

function ExistInstructor(intNvoInst) {
    var grid = jQuery("#GR_INSTRUCTORES");
    var arr = grid.getDataIDs();
    var blExiste = false;
    //recorremos cada fila del row
    for (var i = 0; i < arr.length; i++) {
        var id = arr[i];
        //por cada fila extraemos las fechas
        var lstRow = grid.getRowData(id);
        if (intNvoInst == lstRow.CII_ID_INSTR) {
            blExiste = true;
        }
    }
    return blExiste;
}

function delInstructorCursoImp() {
    var grid = jQuery("#GR_INSTRUCTORES");
    if (grid.getGridParam("selrow")) {
        var id = grid.getGridParam("selrow");
        grid.delRowData(id);
//        grid.trigger("reloadGrid");
        getMaxGridCursosImpartirValue("#GR_INSTRUCTORES");
    } else {
        alert("Seleccione un Instructor.");
    }
}

//Obtiene los ID de los instructores para el curso
function getInstructoresCurso() {
    var grid = jQuery("#GR_INSTRUCTORES");
    var arr = grid.getDataIDs();
    var strIds = "";
    var strHrs = "";
    //recorremos cada fila del row
    for (var i = 0; i < arr.length; i++) {
        var id = arr[i];
        //por cada fila extraemos las fechas
        var lstRow = grid.getRowData(id);
        strIds += lstRow.CII_ID_INSTR + ","
        strHrs += lstRow.II_NUM_HORAS + ","
    }
    return strIds;
}//Fin GetInstructores Curso

//Obtiene los ID de los instructores para el curso
function getHorasInstructor() {
    var grid = jQuery("#GR_INSTRUCTORES");
    var arr = grid.getDataIDs();
    var strHrs = "";
    //recorremos cada fila del row
    for (var i = 0; i < arr.length; i++) {
        var id = arr[i];
        //por cada fila extraemos las fechas
        var lstRow = grid.getRowData(id);
        strHrs += lstRow.II_NUM_HORAS + ","
    }
    return strHrs;
}//Fin GetHoras Instructor

//Obtiene los instructores que impartiran dicho curso
function getInstructoresImpartir() {
    var strCurso = strCursoEdita;
    $("#dialogWait").dialog("open");
    $.ajax({
        type: "POST",
        data: "strCurso=" + strCurso,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "COFIDE_CursosImpartir.jsp?id=9",
        success: function (datos) {
            jQuery("#GR_INSTRUCTORES").clearGridData();
            var lstXml = datos.getElementsByTagName("InstructorImparte")[0];
            var lstCI = lstXml.getElementsByTagName("datos");
            for (var i = 0; i < lstCI.length; i++) {
                var obj = lstCI[i];
                var rowCursos = {
                    CII_CONTADOR: getMaxGridCursosImpartir("#GR_INSTRUCTORES"),
                    CII_ID_INSTR: obj.getAttribute("CII_ID_INSTR"),
                    CC_CURSO_ID: obj.getAttribute("CC_CURSO_ID"),
                    CCI_INSTRUCTOR: obj.getAttribute("CCI_INSTRUCTOR"),
                    II_NUM_HORAS: obj.getAttribute("II_NUM_HORAS")
                };
                jQuery("#GR_INSTRUCTORES").addRowData(getMaxGridCursosImpartir("#GR_INSTRUCTORES"), rowCursos, "last");
            }
            $("#dialogWait").dialog("close");
        },
        error: function (objeto, quepaso, otroobj) {
            alert(":ID 9 :" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }
    });
}// FIN getInstructoresImpartir

function consultaCalif() {
    var strFecIni = document.getElementById("INS_FEC_INI").value;
    var strFecIni = document.getElementById("INS_FEC_FIN").value;

}