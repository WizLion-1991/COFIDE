/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function cofide_sorteo() {

}

/**
 * guarda sus archivos
 * @returns {undefined}
 */
//function sendFiles(opc) {
//    var strOpcion = "";
//    var strOpcionFile = "";
//    if (opc == 1) {
//        strOpcion = "file1";
//        strOpcionFile = "unofile";
//    }
//    if (opc == 2) {
//        strOpcion = "file2";
//        strOpcionFile = "dosfile";
//    }
//    if (opc == 3) {
//        strOpcion = "file3";
//        strOpcionFile = "tresfile";
//    }
//    var fileOpcion = document.getElementById(strOpcion).value;
//    if (fileOpcion != "") {
////        document.getElementById(strOpcionFile).value = fileOpcion;
//        //ajax para guardar archivos
//        alert("se ejecuta el ajax para guardar archivos: " + fileOpcion);
//        UpImg(strOpcion, strOpcionFile);
//    }
//}
/**
 * guarda los datos del ejecutivo
 * @returns {undefined}
 */
function SaveData() {

    var strNombre = document.getElementById("nombre").value;
    var strCorreo = document.getElementById("correo").value;
    var strOpcion1 = document.getElementById("uno").value;
    var strOpcion2 = document.getElementById("dos").value;
    var strOpcion3 = document.getElementById("tres").value;
//    var strOpcion1File = document.getElementById("unofile").value;
//    var strOpcion2File = document.getElementById("dosfile").value;
//    var strOpcion3File = document.getElementById("tresfile").value;

    var strPost = "";
    strPost = "nombre=" + strNombre;
    strPost += "&correo=" + strCorreo;
    strPost += "&uno=" + encodeURIComponent(strOpcion1);
    strPost += "&dos=" + encodeURIComponent(strOpcion2);
    strPost += "&tres=" + encodeURIComponent(strOpcion3);
//    strPost += "&nuno=" + strOpcion1File;
//    strPost += "&ndos=" + strOpcion2File;
//    strPost += "&ntres=" + strOpcion3File;
    $.ajax({
        type: "POST",
        data: encodeURI(strPost),
        scriptCharset: "UTF-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "html",
        url: "cofide_sorteo.jsp?ID=1",
        success: function (datos) {
            datos = trim(datos);
            if (datos == "OK") {
                alert("Gracias por participar\nEl día 8 de Diciembre\nSabras quien es tu AMIGO SECRETO");
                cleanFields();
            } else {
                alert("ERROR[ al guardar la información ]: " + datos);
            }
        }, error: function (objeto, quepaso, otroobj) {
            alert(":pto=1:" + objeto + " " + quepaso + " " + otroobj);
        }});
}

function validateData() {
    var strNombre = document.getElementById("nombre").value;
    var strCorreo = document.getElementById("correo").value;
    var strOpcion1 = document.getElementById("uno").value;
    var strOpcion2 = document.getElementById("dos").value;
    var strOpcion3 = document.getElementById("tres").value;
//    var fileOpcion1 = document.getElementById("file1").value;
//    var fileOpcion2 = document.getElementById("file2").value;
//    var fileOpcion3 = document.getElementById("file3").value;


    if (strNombre != "" && strCorreo != "") {
        if (validaFormatoCorreo()) {
            if (strOpcion1 != "" || strOpcion2 != "" || strOpcion3 != "") {

//                if (fileOpcion1 != "") {
//                    sendFiles(1);
//
//                }
//                if (fileOpcion1 != "") {
//                    sendFiles(2);
//
//                }
//                if (fileOpcion1 != "") {
//                    sendFiles(3);
//                }

                SaveData();

            } else {
                alert("Es muy importante que por lo menos envies una opcion!.");
                document.getElementById("uno").focus();
            }
        }
    } else {
        alert("Es muy importante que proporciones tu nombre y correo!.");
        document.getElementById("nombre").focus();
    }

}

/**
 * limpia campos
 */
function cleanFields() {

    document.getElementById("nombre").value = "";
    document.getElementById("correo").value = "";
    document.getElementById("uno").value = "";
    document.getElementById("dos").value = "";
    document.getElementById("tres").value = "";
    document.getElementById('id01').style.display = 'none';
//    document.getElementById("unofile").value = "";
////    document.getElementById("file1").value = "";
//    document.getElementById("dosfile").value = "";
////    document.getElementById("file2").value = "";
//    document.getElementById("tresfile").value = "";
//    document.getElementById("file3").value = "";
}

//Esta funcion quita los espacios y saltos de linea de una cadena
function trim(cadena)
{
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

/**
 * valida formato del correo
 */
function validaFormatoCorreo() {
    var strCorreo = document.getElementById("correo").value;
    var expr = /^([a-zA-Z0-9_Ññ\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
    if (strCorreo != "") {
        if (!expr.test(strCorreo)) {
            alert("El formato del correo es incorrecto. ");
            document.getElementById("correo").focus();
            return false;
        } else {
            return true;
        }
    }
}

function OpnModal() {
    document.getElementById('id01').style.display = 'block';
}

/**
 * sorteo
 */
function randomSorteo() {
    $.ajax({
        type: "POST",
        data: "",
        scriptCharset: "UTF-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "html",
        url: "cofide_sorteo.jsp?ID=2",
        success: function (datos) {
            datos = trim(datos);
            if (datos == "OK") {
                alert("Sorteo terminado!");

            } else {
                alert("ERROR[ Ya no hay participantes disponibles]: " + datos);

            }
            printResult();
        }, error: function (objeto, quepaso, otroobj) {
            alert(":pto=2:" + objeto + " " + quepaso + " " + otroobj);
        }});
}
/**
 * abre la liga del listado de amiguitos secretos
 */
function printResult() {

    $.ajax({
        type: "POST",
        data: "",
        scriptCharset: "UTF-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "html",
        url: "cofide_sorteo.jsp?ID=3",
        success: function (datos) {
            datos = trim(datos);

            document.getElementById("tabla_sorteo").innerHTML = datos;

        }, error: function (objeto, quepaso, otroobj) {
            alert(":pto=3:" + objeto + " " + quepaso + " " + otroobj);
        }});
}

/**
 * envia correo a cada participante con su amio secreto
 */
function sendAmigoSecreto() {
    $.ajax({
        type: "POST",
        data: "",
        scriptCharset: "UTF-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "html",
        url: "cofide_sorteo.jsp?ID=4",
        success: function (datos) {
            datos = trim(datos);
            if (datos == "OK") {
                alert("Ya se han enviado los correos.!");
            } else {
                alert("Ocurrio un error al enviar los correos");
            }
        }, error: function (objeto, quepaso, otroobj) {
            alert(":pto=4:" + objeto + " " + quepaso + " " + otroobj);
        }});
}