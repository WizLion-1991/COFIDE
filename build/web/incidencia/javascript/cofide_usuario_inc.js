/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function cofide_usuario_inc() {

}

function initUser() {

}

function setEstatusUsr(estatus, idusuario) {

    var strPost = "estatus=" + estatus;
    strPost += "&idusuario=" + idusuario;
//    alert(strPost);
    $.ajax({
        type: "POST",
        scriptCharset: "UTF-8",
        data: strPost,
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "html",
        url: "cofide_incidencia.jsp?ID=11",
        success: function (datos) {
            datos = trim(datos);
            if (datos == "OK") {
                showUsuarios();
            } else {
                alert(datos);
            }
        }, error: function (objeto, quepaso, otroobj) {
            alert(":pto=11:" + objeto + " " + quepaso + " " + otroobj);
        }});

}


function OpnDetalleusr(stridusuario) {
    document.getElementById('id04').style.display = 'block';//abre el modal
    loadProfile();
    document.getElementById("id_usuario").innerHTML = "<h5>USUARIO N°: " + stridusuario + "</h5>";
    document.getElementById("id_usuario_ed").value = stridusuario;
    setTimeout("loadDetallesUsr(" + stridusuario + ")", 1000);
}

function loadProfile() {
    var objPerfil = document.getElementById("perfil");
    $.ajax({
        type: "POST",
        data: "",
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "cofide_incidencia.jsp?ID=12",
        success: function (datos) {
            select_clear(objPerfil);
            var lstXml = datos.getElementsByTagName("vta")[0];
            var lstCte = lstXml.getElementsByTagName("datos");
            for (var i = 0; i < lstCte.length; i++) {
                var objPerfils = lstCte[i];
                select_add(objPerfil, objPerfils.getAttribute("perfil"), objPerfils.getAttribute("id_perfil"));
            }

        }, error: function (objeto, quepaso, otroobj) {
            alert(":pto3:" + objeto + " " + quepaso + " " + otroobj);
        }});
}

function loadDetallesUsr(stridusuario) {

    document.getElementById("nombre").value = "";
    document.getElementById("usuario").value = "";
    document.getElementById("contrasenia").value = "";
    document.getElementById("perfil").value = "0";
    document.getElementById("estatus").value = "0";

    var strPost = "idusuario=" + stridusuario;
    $.ajax({
        type: "POST",
        scriptCharset: "UTF-8",
        data: strPost,
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "cofide_incidencia.jsp?ID=13",
        success: function (datos) {
//            console.log(datos);
            var lstXml = datos.getElementsByTagName("vta")[0];
            var lstCte = lstXml.getElementsByTagName("datos");
            for (var i = 0; i < lstCte.length; i++) {
                var objcte = lstCte[i];

                document.getElementById("nombre").value = objcte.getAttribute("nombre");
                document.getElementById("usuario").value = objcte.getAttribute("usuario");
                document.getElementById("contrasenia").value = objcte.getAttribute("contrasenia");
                document.getElementById("perfil").value = objcte.getAttribute("perfil");
                document.getElementById("estatus").value = objcte.getAttribute("estatus");

            }
        }, error: function (objeto, quepaso, otroobj) {
            alert(":pto=13:" + objeto + " " + quepaso + " " + otroobj);
        }});
}

function updateUsuario() {
    var strPost = "";
    strPost = "nombre=" + document.getElementById("nombre").value;
    strPost += "&usuario=" + document.getElementById("usuario").value;
    strPost += "&contrasenia=" + document.getElementById("contrasenia").value;
    strPost += "&perfil=" + document.getElementById("perfil").value;
    strPost += "&estatus=" + document.getElementById("estatus").value;
    strPost += "&idusuario=" + document.getElementById("id_usuario_ed").value;

    $.ajax({
        type: "POST",
        scriptCharset: "UTF-8",
        data: strPost,
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "html",
        url: "cofide_incidencia.jsp?ID=14",
        success: function (datos) {
            datos = trim(datos);
            if (datos == "OK") {
                showUsuarios();
            } else {
                alert(datos);
            }
        }, error: function (objeto, quepaso, otroobj) {
            alert(":pto=14:" + objeto + " " + quepaso + " " + otroobj);
        }});


}

/**
 * registrar nuevo usuario
 * @returns {undefined}
 */
function setUsuarioNew(strUsuario, strPass, strNombre) {

    var strPost = "usuario=" + strUsuario;
    strPost += "&nombre=" + strNombre;
    strPost += "&pass=" + strPass;

    $.ajax({
        type: "POST",
        scriptCharset: "UTF-8",
        data: strPost,
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "html",
        url: "cofide_incidencia.jsp?ID=15",
        success: function (datos) {
            datos = trim(datos);
            if (datos == "OK") {
                showMsg("EL USUARIO SE HA REGISTRADO CON EXITO.<br>ES NECESARIO COMUNICARTE CON EL ADMINISTRADOR.");
                location.href = 'index.html';
            } else {
                alert(datos);
            }
        }, error: function (objeto, quepaso, otroobj) {
            alert(":pto=15:" + objeto + " " + quepaso + " " + otroobj);
        }});

}