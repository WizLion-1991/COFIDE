/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
function cofide_login() {

}

function initLogin() {

}

function showMsg(strMensaje) {
    OpnModal(3);
    document.getElementById("contenidoalerta").innerHTML = strMensaje;
}

function ValidField(opc) {

    var strUsuario = "";
    var strPass1 = "";
    var strPass2 = "";

    if (opc == 1) {

        strUsuario = document.getElementById("name").value;
        strPass1 = document.getElementById("psw").value;

        if (strUsuario != "" && strPass1 != "") {
            inicioSesion();
        } else {
            showMsg("ES IMPORTANTE INGRESAR TODOS LOS CAMPOS QUE SE MUESTRAN");
        }

    } else {

        var strNombre = document.getElementById("nombreusuario").value;
        strUsuario = document.getElementById("name1").value;
        strPass1 = document.getElementById("psw1").value;
        strPass2 = document.getElementById("psw2").value;

        if (strUsuario != "" && strPass1 != "" && strPass2 != "") {

            if (strPass1 == strPass2) {
                setUsuarioNew(strUsuario, strPass1, strNombre);
            } else {
                showMsg("LAS CONTRASEÑAS NO COINCIDEN.");
            }

        } else {
            showMsg("ES IMPORTANTE INGRESAR TODOS LOS CAMPOS QUE SE MUESTRAN");
        }
    }

}

function inicioSesion() {
    var strUsuario = "";
    var strPass1 = "";

    strUsuario = document.getElementById("name").value;
    strPass1 = document.getElementById("psw").value;

    var strPost = "";
    strPost = "usuario=" + strUsuario;
    strPost += "&password=" + strPass1;

    $.ajax({
        type: "POST",
        data: strPost,
        scriptCharset: "UTF-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "html",
        url: "cofide_incidencia.jsp?ID=1",
        beforeSend: function () {
            console.log(strPost);
        },
        success: function (datos) {
            datos = trim(datos);
            if (datos.substr(0, 3) != "OK.") {
                showMsg(datos);
            } else {
                showMsg("INICIO DE SESI&Oacute;N CON EXITO");
//                location.href = "Main.jsp?" + datos;
                var strHtml = '<form action="Main.jsp" method="post" id="formSend">';
                strHtml += datos.replace("OK.", "");
                strHtml += "</form>";
                document.getElementById("tmp").innerHTML = strHtml;
                document.getElementById("formSend").submit();
            }
        }, error: function (objeto, quepaso, otroobj) {
            alert(":pto=1:" + objeto + " " + quepaso + " " + otroobj);
        }});
}

function trim(cadena) {
    if (cadena == null) {
        console.log(cadena)
        cadena = "";
    }
    for (i = 0; i < cadena.length; )
    {
        if (cadena.charAt(i) == " " || cadena.charCodeAt(i) == 10 || cadena.charCodeAt(i) == 13)
            cadena = cadena.substring(i + 1, cadena.length);
        else
            break;
    }
    for (i = cadena.length - 1; i >= 0; i = cadena.length - 1)
    {
        if (cadena.charAt(i) == " " || cadena.charCodeAt(i) == 10 || cadena.charCodeAt(i) == 13)
            cadena = cadena.substring(0, i);
        else
            break;
    }
    return cadena;
}