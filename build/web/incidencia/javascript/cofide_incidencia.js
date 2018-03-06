/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function cofide_incidencia() {

}

/**
 * carga pantalla para agregar incidencias
 * @returns {undefined}
 */
function initIncidencias() {

    $.ajax({
        type: "POST",
        scriptCharset: "UTF-8",
        data: "",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "html",
        url: "cofide_incidencia_incidencia.jsp",
        success: function (datos) {
            document.getElementById("contenido_pantalla").innerHTML = datos;
            clearDisplay();
            loadCursos();
        }, error: function (objeto, quepaso, otroobj) {
            alert(":pto=mostrar_pantalla:" + objeto + " " + quepaso + " " + otroobj);
        }});
}

/**
 * cursos de sugerencias
 * @returns {undefined}
 */
function loadCursos() {
    var objCurso = document.getElementById("cursoref");
    $.ajax({
        type: "POST",
        data: "",
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "cofide_incidencia.jsp?ID=4",
        success: function (datos) {
            select_clear(objCurso);
            var lstXml = datos.getElementsByTagName("vta")[0];
            var lstCte = lstXml.getElementsByTagName("datos");
            for (var i = 0; i < lstCte.length; i++) {
                var objCursos = lstCte[i];
                select_add(objCurso, objCursos.getAttribute("curso"), objCursos.getAttribute("id_curso"));
            }

            loadTipoProblema();

        }, error: function (objeto, quepaso, otroobj) {
            alert(":pto3:" + objeto + " " + quepaso + " " + otroobj);
        }});
}

/**
 * tipo de problema
 * @returns {undefined}
 */
function loadTipoProblema() {
    var objProblema = document.getElementById("tipoproblema");
    $.ajax({
        type: "POST",
        data: "",
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "cofide_incidencia.jsp?ID=5",
        success: function (datos) {
            select_clear(objProblema);
            var lstXml = datos.getElementsByTagName("vta")[0];
            var lstCte = lstXml.getElementsByTagName("datos");
            for (var i = 0; i < lstCte.length; i++) {
                var objCursos = lstCte[i];
                select_add(objProblema, objCursos.getAttribute("curso"), objCursos.getAttribute("id_curso"));
            }

            loadModulo();

        }, error: function (objeto, quepaso, otroobj) {
            alert(":pto5:" + objeto + " " + quepaso + " " + otroobj);
        }});
}

/**
 * si se selecciona CRm en problema, mostrara el modulo
 * @returns {undefined}
 */
function loadModulo() {
    var objModulo = document.getElementById("modulocrm");
    $.ajax({
        type: "POST",
        data: "",
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "cofide_incidencia.jsp?ID=6",
        success: function (datos) {
            select_clear(objModulo);
            var lstXml = datos.getElementsByTagName("vta")[0];
            var lstCte = lstXml.getElementsByTagName("datos");
            for (var i = 0; i < lstCte.length; i++) {
                var objCursos = lstCte[i];
                select_add(objModulo, objCursos.getAttribute("curso"), objCursos.getAttribute("id_curso"));
            }
        }, error: function (objeto, quepaso, otroobj) {
            alert(":pto6:" + objeto + " " + quepaso + " " + otroobj);
        }});
}

function select_clear(a) {
    var b = a.length;
    for (i = 0; i < b; i++) {
        a.remove(0)
    }
}

function select_add(e, a, c) {
    var d = document.createElement("option");
    d.text = a;
    d.value = c;
    try {
        e.add(d, null)
    } catch (b) {
        e.add(d)
    }
}

/**
 * limpiar pantalla
 * @returns {undefined}
 */
function clearDisplay() {
    document.getElementById("cliente").value = "";
    document.getElementById("correo").value = "";
    document.getElementById("cursoref").value = "0";
    document.getElementById("origenproblema").value = "";
    document.getElementById("tipoproblema").value = "0";
    document.getElementById("modulocrm").value = "0";
    document.getElementById("comentario").value = "";
    document.getElementById("observacion").value = "";
    if (document.getElementById("radio3") != null) {
        document.getElementById("radio3").checked = true;
    }
    if (document.getElementById("nombreusuario") != null) {
        document.getElementById("nombreusuario").value = "";
    }
}

/**
 * validar campos requeridos
 * @returns {undefined}
 */
function validaFields() {
    var strRespuesta = false;
    if (document.getElementById("cliente").value != "") {
        strRespuesta = true;
    } else {
        alert("Capturar el cliente es importante.");
    }
    if (document.getElementById("correo").value != "") {
        strRespuesta = true;
    } else {
        alert("Capturar el correo es importante.");
    }
    if (document.getElementById("origenproblema").value != "") {
        strRespuesta = true;
    } else {
        alert("Elige responsable del problema.");
    }
    if (document.getElementById("tipoproblema").value != "") {
        strRespuesta = true;
    } else {
        alert("Elige el tipo del problema.");
    }
    if (document.getElementById("comentario").value != "") {
        strRespuesta = true;
    } else {
        alert("Capturar el comentario es importante.");
    }
    if (document.getElementById("observacion").value != "") {
        strRespuesta = true;
    } else {
        alert("Capturar las observaciones es importante.");
    }

    return strRespuesta;
}

/**
 * guardar incidencia
 * @returns {undefined}
 */
function SaveIncidencia() {
    if (confirm("¿Seguro del contenido de la incidencia?")) {
        var strPost = "";
        strPost = "cliente=" + encodeURIComponent(document.getElementById("cliente").value);
        strPost += "&usuario_id=" + document.getElementById("usuario_id").value;
        strPost += "&correo=" + document.getElementById("correo").value;
        strPost += "&cursoid=" + document.getElementById("cursoref").value;
        strPost += "&origen=" + document.getElementById("origenproblema").value;
        strPost += "&problema=" + document.getElementById("tipoproblema").value;
        strPost += "&modulo=" + document.getElementById("modulocrm").value;
        strPost += "&estatus=" + getEstatus();
        strPost += "&comentario=" + encodeURIComponent(document.getElementById("comentario").value);
        strPost += "&observacion=" + encodeURIComponent(document.getElementById("observacion").value);

        if (validaFields()) {

            $.ajax({
                type: "POST",
                scriptCharset: "UTF-8",
                data: encodeURI(strPost),
                contentType: "application/x-www-form-urlencoded;charset=utf-8",
                cache: false,
                dataType: "html",
                url: "cofide_incidencia.jsp?ID=7",
                success: function (datos) {
                    datos = trim(datos);
                    if (datos == "OK") {
                        alert("Incidencia agregada con exito.");
                        initIncidencias();
                    } else {
                        alert("Valida la información ingresada.\n" + datos);
                    }
                }, error: function (objeto, quepaso, otroobj) {
                    alert(":pto=7:" + objeto + " " + quepaso + " " + otroobj);
                }});
        }
    }
}

/**
 * obtener el estatus
 * @returns {undefined}
 */
function getEstatus() {
    var strEstatus = "3"; //inconcluso
    if (document.getElementById("radio1").checked) {
        strEstatus = "1"; //concluido
    }
    if (document.getElementById("radio2").checked) {
        strEstatus = "2"; //pendiente
    }
    return strEstatus;
}

/**
 * muestra pantalla donde se listan las incidencias
 * @returns {undefined}
 */
function showIncidencia() {
    var strIdUsr = document.getElementById("usuario_id").value;
    $.ajax({
        type: "POST",
        scriptCharset: "UTF-8",
        data: "idusuario=" + strIdUsr,
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "html",
        url: "cofide_incidencia_list.jsp",
        success: function (datos) {
            document.getElementById("contenido_pantalla").innerHTML = datos;
        }, error: function (objeto, quepaso, otroobj) {
            alert(":pto=mostrar_pantalla:" + objeto + " " + quepaso + " " + otroobj);
        }});
}

/**
 * abre el modal para ver lso detalles de la incidencia
 * @param {type} idIncidencia
 * @returns {undefined}
 */
function OpnDetalle(idIncidencia) {
    document.getElementById('id04').style.display = 'block';//abre el modal
    clearDisplay();
    loadCursos();
    document.getElementById("id_incidencia_ed").innerHTML = "<h5>INCIDENCIA N°: " + idIncidencia + "</h5>";
    document.getElementById("id_incidencia_edicion").value = idIncidencia;
    setTimeout("loadDetallesInc(" + idIncidencia + ")", 1000);

}

/**
 * actualiza el estatus de la incidencia
 * @param {type} idIncidencia
 * @returns {undefined}
 */
function setEstatus(opc, idIncidencia) {
//    alert("vamo a enviar datos: estatus: " + opc);
//    alert("vamo a enviar datos: id incidencia: " + idIncidencia);
    var strPost = "estatus=" + opc;
    strPost += "&incidencia=" + idIncidencia;
//    alert(strPost);
    $.ajax({
        type: "POST",
        scriptCharset: "UTF-8",
        data: strPost,
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "html",
        url: "cofide_incidencia.jsp?ID=8",
        success: function (datos) {
            datos = trim(datos);
            if (datos == "OK") {
                showIncidencia();
            } else {
                alert(datos);
            }
        }, error: function (objeto, quepaso, otroobj) {
            alert(":pto=8:" + objeto + " " + quepaso + " " + otroobj);
        }});
}

/**
 * oculta el combo para seleccionar el modulo del CRM
 * @returns {undefined}
 */
function hidemodulo() {
    if (document.getElementById("tipoproblema").value != "3") {
        document.getElementById('modulocrm').style.display = 'none';//abre el modal
    } else {
        document.getElementById('modulocrm').style.display = 'block';//abre el modal
        loadModulo();
    }
}

/**
 * carga los detalles de la incidencia.
 * @param {type} idIncidencia
 * @returns {undefined}
 */
function loadDetallesInc(idIncidencia) {
    var strPost = "idincidencia=" + idIncidencia;
    $.ajax({
        type: "POST",
        scriptCharset: "UTF-8",
        data: strPost,
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "cofide_incidencia.jsp?ID=9",
        success: function (datos) {
//            console.log(datos);
            var lstXml = datos.getElementsByTagName("vta")[0];
            var lstCte = lstXml.getElementsByTagName("datos");
            for (var i = 0; i < lstCte.length; i++) {
                var objcte = lstCte[i];

                document.getElementById("id_incidencia_edicion").value = objcte.getAttribute("idincidencia");
                document.getElementById("correo").value = objcte.getAttribute("correo");
                document.getElementById("cursoref").value = objcte.getAttribute("curso");
                document.getElementById("origenproblema").value = objcte.getAttribute("origen");
                document.getElementById("tipoproblema").value = objcte.getAttribute("tipo");
                document.getElementById("modulocrm").value = objcte.getAttribute("modulo");
                document.getElementById("cliente").value = objcte.getAttribute("cliente");
                document.getElementById("comentario").value = objcte.getAttribute("comentario");
                document.getElementById("observacion").value = objcte.getAttribute("observacion");
            }
        }, error: function (objeto, quepaso, otroobj) {
            alert(":pto=9:" + objeto + " " + quepaso + " " + otroobj);
        }});
}

/**
 * actualiza la incidencia
 * @returns {undefined}
 */
function UpdateInc() {
    var strPost = "";

    strPost = "cliente=" + encodeURIComponent(document.getElementById("cliente").value);
    strPost += "&incidencia_id=" + document.getElementById("id_incidencia_edicion").value;
    strPost += "&correo=" + document.getElementById("correo").value;
    strPost += "&cursoid=" + document.getElementById("cursoref").value;
    strPost += "&origen=" + document.getElementById("origenproblema").value;
    strPost += "&problema=" + document.getElementById("tipoproblema").value;
    strPost += "&modulo=" + document.getElementById("modulocrm").value;
    strPost += "&comentario=" + encodeURIComponent(document.getElementById("comentario").value);
    strPost += "&observacion=" + encodeURIComponent(document.getElementById("observacion").value);

    $.ajax({
        type: "POST",
        scriptCharset: "UTF-8",
        data: encodeURI(strPost),
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "html",
        url: "cofide_incidencia.jsp?ID=10",
        success: function (datos) {
            datos = trim(datos);
            if (datos == "OK") {
                alert("Incidencia actualizada con exito.");
                showIncidencia();
                CloseModal(4);
            } else {
                alert("Valida la información ingresada.\n" + datos);
            }
        }, error: function (objeto, quepaso, otroobj) {
            alert(":pto=10:" + objeto + " " + quepaso + " " + otroobj);
        }});

}

/**
 * muestra la pantalla de usuarios
 * @returns {undefined}
 */
function showUsuarios() {
    var strIdUsr = "1";
    if (document.getElementById("usuario_id") != null) {
        strIdUsr = document.getElementById("usuario_id").value;
    }
    $.ajax({
        type: "POST",
        scriptCharset: "UTF-8",
        data: "idusuario=" + strIdUsr,
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "html",
        url: "cofide_incidencia_usuario.jsp",
        success: function (datos) {
            if (document.getElementById("contenido_pantalla") != null) {
                document.getElementById("contenido_pantalla").innerHTML = datos;
            }
        }, error: function (objeto, quepaso, otroobj) {
            alert(":pto=mostrar_pantalla:" + objeto + " " + quepaso + " " + otroobj);
        }});
}