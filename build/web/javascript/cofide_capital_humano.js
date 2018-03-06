function cofide_capital_humano() {

}
function initCapH() {
    document.getElementById("CH_ES_CURSO1").checked = true;
    document.getElementById("CH_CURSO_").value = "";
    document.getElementById("CH_CURSO_").disabled = true;
    document.getElementById("CH_CURSO_").style.backgroundColor = '#e0f8e6';
    document.getElementById("CH_CURSO").disabled = false;
    EsCursoCFD();
    muestraCurso();
}
/**
 * guarda los datos de capital humano sobre los cursos que han tomado los colaboradores
 * @returns 
 */
function Save() {
    var strEsCurso = document.getElementById("CH_ES_CURSO1").checked;
    var stridCurso = document.getElementById("CH_CURSO").value;
    var strNomCurso = document.getElementById("CH_CURSO_").value;
    var strIdColaborador = document.getElementById("CH_COLABORADOR").value;
    var strIdTitulo = document.getElementById("CH_TITULO").value;
    var strEmpresa = document.getElementById("CH_EMPRESA").value;
    var strFecha = document.getElementById("CH_FECHA").value;
    var strCosto = document.getElementById("CH_COSTO").value;
    var intTipoCurso = document.getElementById("CH_TIPO").value;
    if (intTipoCurso != "0") {
        var intCofide = 1; // 1 = es cofide, 0 = externo
        var strPost = "";
        if (EsCursoCFD()) { //si es verdadero el curso cofide, manda el id del curso y el nombre del curso vacio
            intCofide = 1;
            strPost += "&id_curso=" + stridCurso;
            strPost += "&nombre_curso=";
        } else { // si es falso, manda el nombre del curso y el id del curso en 0
            intCofide = 0;
            strPost += "&id_curso=0";
            strPost += "&nombre_curso=" + strNomCurso;
        }
        strPost += "&es_cofide=" + intCofide;
        strPost += "&colaborador=" + strIdColaborador;
        strPost += "&titulo=" + strIdTitulo;
        strPost += "&empresa=" + strEmpresa;
        strPost += "&fecha=" + strFecha;
        strPost += "&costo=" + strCosto;
        if (validaCampos()) {
            $("#dialogWait").dialog("open");
            $.ajax({
                type: "POST",
                data: strPost,
                scriptCharset: "utf-8",
                contentType: "application/x-www-form-urlencoded;charset=utf-8",
                cache: false,
                dataType: "html",
                url: "COFIDE_Capital_humano.jsp?id=1",
                success: function (datos) {
                    datos = trim(datos);
                    if (datos == "OK") {
                        if (intCofide == 1) {
                            AddParticipante(strIdTitulo, strIdColaborador, stridCurso, intTipoCurso);
                        } else {
                            ResetPantallaCapH();
                        }
                    } else {
                        alert("ERROR: " + datos);
                    }
                    $("#dialogWait").dialog("close");
                }, error: function (objeto, quepaso, otroobj) {
                    alert(":pto1:" + objeto + " " + quepaso + " " + otroobj);
                    $("#dialogWait").dialog("close");
                }});
        }
    } else {
        alert("Selecciona el tipo de curso a impartir.");
    }
}
/**
 * valida si es un curso de cofide o externo
 * @returns {undefined}
 */
function EsCursoCFD() {
    $("#dialogWait").dialog("open");
    var bolCofide = false;
    var bolEsCursoCFD = document.getElementById("CH_ES_CURSO1").checked;
    if (bolEsCursoCFD) { //es cofide el curso
        document.getElementById("CH_CURSO_").value = "";
        document.getElementById("CH_CURSO_").disabled = true;
        document.getElementById("CH_CURSO_").style.backgroundColor = '#e0f8e6';
        document.getElementById("CH_CURSO").disabled = false;
        document.getElementById("CH_EMPRESA").value = "PERSONAL COFIDE S.C.";
        document.getElementById("CH_COSTO").value = "0";
        document.getElementById("CH_TIPO").disabled = false;
        bolCofide = true;
    } else { //es externo
        document.getElementById("CH_CURSO_").disabled = false;
        document.getElementById("CH_CURSO_").style.backgroundColor = '#ffffff';
        document.getElementById("CH_CURSO").disabled = true;
        document.getElementById("CH_CURSO").value = "0";
        document.getElementById("CH_EMPRESA").value = "";
        document.getElementById("CH_COSTO").value = "";
        document.getElementById("CH_TIPO").checked = false;
        document.getElementById("CH_TIPO").disabled = true;
    }
    $("#dialogWait").dialog("close");
    return bolCofide;
}
/**
 * limpiar campos
 */
function ResetPantallaCapH() {
    document.getElementById("CH_CURSO").value = "";
    document.getElementById("CH_CURSO_").value = "";
    document.getElementById("CH_COLABORADOR").value = "0";
    document.getElementById("CH_TITULO").value = "0";
    document.getElementById("CH_EMPRESA").value = "";
    document.getElementById("CH_FECHA").value = "";
    document.getElementById("CH_COSTO").value = "";
    document.getElementById("CH_TIPO").checked = false;
    EsCursoCFD();
}
function validaCampos() {
    var bolOk = false;
    if (document.getElementById("CH_CURSO").value != "" || document.getElementById("CH_CURSO").value != "") {
        if (document.getElementById("CH_COLABORADOR").value != "0") {
            if (document.getElementById("CH_FECHA").value != "") {
                bolOk = true;
            } else {
                alert("Ingresar la fecha");
            }
        } else {
            alert("Ingresa un colaborador");
        }
    } else {
        alert("Selecciona un curso a impartir");
    }
    return bolOk;
}

/**
 * @strTitulo id titulo del colaborador
 * @strColaborador id del colaborador
 * @strCurso id curso
 * Agrega al colaborador a la lista de participantes del curso
 * @returns {undefined}
 */
function AddParticipante(strTitulo, strColaborador, strCurso, intTipoCurso) {
    var strPost = "";
    strPost += "idTitulo=" + strTitulo;
    strPost += "&idColaborador=" + strColaborador;
    strPost += "&idCurso=" + strCurso;
    strPost += "&tipo_curso=" + intTipoCurso;
    $("#dialogWait").dialog("open");
    $.ajax({
        type: "POST",
        data: strPost,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "html",
        url: "COFIDE_Capital_humano.jsp?id=2",
        success: function (datos) {
            if (trim(datos) == "OK") {
                ResetPantallaCapH();
            } else {
                alert("ERROR: " + datos);
            }
            $("#dialogWait").dialog("close");
        }, error: function (objeto, quepaso, otroobj) {
            alert(":pto2:" + objeto + " " + quepaso + " " + otroobj);
        }});
}

function Exit() {
    myLayout.open("west");
    myLayout.open("east");
    myLayout.open("south");
    myLayout.open("north");
    document.getElementById("MainPanel").innerHTML = "";
    document.getElementById("rightPanel").innerHTML = "";
    var objMainFacPedi = objMap.getScreen("CAP_H");
    objMainFacPedi.bolActivo = false;
    objMainFacPedi.bolMain = false;
    objMainFacPedi.bolInit = false;
    objMainFacPedi.idOperAct = 0;
}

/**
 * muestra los cursos disponibles con base al tipo de curso seleccionado
 * @returns {undefined}
 */
function muestraCurso() {
    var intTipoCurso = document.getElementById("CH_TIPO").value;
    var objCurso = document.getElementById("CH_CURSO");
    var strPost = "tipocurso=" + intTipoCurso;
    $.ajax({
        type: "POST",
        data: strPost,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "COFIDE_Capital_humano.jsp?id=3",
        success: function (datos) {
            select_clear(objCurso);
            var lstXml = datos.getElementsByTagName("vta")[0];
            var lstCte = lstXml.getElementsByTagName("datos");
            for (var i = 0; i < lstCte.length; i++) {
                var objCursos = lstCte[i];
                select_add(objCurso, objCursos.getAttribute("curso"), objCursos.getAttribute("id_curso"));
            }
        }, error: function (objeto, quepaso, otroobj) {
            alert(":pto3:" + objeto + " " + quepaso + " " + otroobj);
        }});
}
