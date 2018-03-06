/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function sepomex() {

}

function enviarCp() {
    var strCp = document.getElementById("cp").value;
    if (strCp != "") {
        if (strCp.length == 5) {
            //busca el codigo postal y lista el resultado
            sendCP(strCp);
        } else {
            alert("El CP debe de contener 5 caracteres.");
        }
    }
}

function validaNumero(e) {
    var tecla = (document.all) ? e.keyCode : e.which;
    //Tecla de retroceso para borrar, siempre la permite
    if (tecla == 8) {
        return true;
    }
    if (tecla == 13) {
        if (document.getElementById("cpn").value == "") {
            enviarCp();
        }
    }
    // Patron de entrada, en este caso solo acepta numeros
    var patron = /[0-9]/;
    var tecla_final = String.fromCharCode(tecla);
    return patron.test(tecla_final);
}

function keySend(e) {
    var tecla = (document.all) ? e.keyCode : e.which;
    var strColonia = document.getElementById("col").value;
    if (tecla == 13) {
        Save();
    } else {
        document.getElementById("col").value = strColonia.toUpperCase();
    }
}

function sendCP(strCp) {
    $.ajax({
        type: "POST",
        data: "cp=" + strCp,
        scriptCharset: "UTF-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "html",
        url: "sepomex.jsp?ID=1",
        success: function (datos) {
            init();
            document.getElementById("tabla").innerHTML = datos;
        }, error: function (objeto, quepaso, otroobj) {
            alert(":pto=1:" + objeto + " " + quepaso + " " + otroobj);
        }});
}

function addCp(strCp) {
    var strColonia = document.getElementById("col").value;
    if (strColonia != "") {
        $.ajax({
            type: "POST",
            data: "cp=" + strCp + "&colonia=" + strColonia.toUpperCase(),
            scriptCharset: "UTF-8",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "html",
            url: "sepomex.jsp?ID=2",
            success: function (datos) {
                if (datos != "OK") {
                    sendCP(strCp);
                } else {
                    alert("Ocurrio un problema.");
                }
            }, error: function (objeto, quepaso, otroobj) {
                alert(":pto=2:" + objeto + " " + quepaso + " " + otroobj);
            }});
    } else {
        strCp = document.getElementById("col").focus();
        alert("Ingresa la colonia.");
    }

}

function validaCp() {
    document.getElementById("col").value = "";
    document.getElementById("col").style.display = "";
    document.getElementById("guarda").style.display = "";
    document.getElementById("valida").style.display = "none";
    document.getElementById("send").style.display = "none";
    document.getElementById("cpsave").style.display = "";
    document.getElementById("nuevo").style.display = "none";
}

function Save() {
    var strCp = document.getElementById("cp").value;
    if (strCp != "") {
        addCp(strCp);
    } else {
        strCp = document.getElementById("cp").focus();
        alert("Ingresa un Codigo Postal.");
    }
}

function init() {
    document.getElementById("col").value = "";
    document.getElementById("valida").style.display = "";
    document.getElementById("send").style.display = "";
    document.getElementById("cpnew").style.display = "";
    document.getElementById("cpsave").style.display = "none";
    document.getElementById("cpnuevo").style.display = "none";
    document.getElementById("nuevo").style.display = "";
    document.getElementById("cpn").value = "";
    document.getElementById("colnew").value = "";
    document.getElementById("munnew").value = "";
    document.getElementById("edonew").value = "";


    document.getElementById("valida").style.backgroundColor = "#99cc00";
    document.getElementById("nuevo").style.backgroundColor = "#99cc00";
    document.getElementById("send").style.backgroundColor = "#99cc00";
    document.getElementById("guarda").style.backgroundColor = "#99cc00";
    document.getElementById("cancelar").style.backgroundColor = "#99cc00";
    document.getElementById("guardanew").style.backgroundColor = "#99cc00";
    document.getElementById("cancelnew").style.backgroundColor = "#99cc00";
}

function SaveNew() {
    var strCp = document.getElementById("cpn").value;
    var strColonia = document.getElementById("colnew").value;
    var strMunicipio = document.getElementById("munnew").value;
    var strEstado = document.getElementById("edonew").value;

    var strPost = "";
    if (strCp != "" && strColonia != "" && strMunicipio != "" && strEstado != "") {
        // success

        strPost = "cp=" + strCp;
        strPost += "&colonia=" + strColonia.toUpperCase();
        strPost += "&municipio=" + strMunicipio.toUpperCase();
        strPost += "&estado=" + strEstado.toUpperCase();

        $.ajax({
            type: "POST",
            data: strPost,
            scriptCharset: "UTF-8",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "html",
            url: "sepomex.jsp?ID=3",
            beforeSend: function () {
                init();
            },
            success: function (datos) {
                if (datos != "OK") {
                    sendCP(strCp);
                } else {
                    alert("Ocurrio un problema.");
                }
            }, error: function (objeto, quepaso, otroobj) {
                alert(":pto=2:" + objeto + " " + quepaso + " " + otroobj);
            }});

    } else {
        alert("Debes llenar todos los campos, son importantes.");
    }
}

function showNew() {
    document.getElementById("cpnuevo").style.display = "";
    document.getElementById("cpnew").style.display = "none";
    document.getElementById("cpsave").style.display = "none";
}