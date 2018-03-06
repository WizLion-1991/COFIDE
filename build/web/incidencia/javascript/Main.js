/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
function Main() {

}

function init() {
    ClearField(1);
    ClearField(2);
}

function CloseModal(opc) {

    if (opc == 3) {
        document.getElementById('alerta').style.display = 'none';
    } else {
        document.getElementById('id0' + opc).style.display = 'none';
        if (opc != 4) {
            ClearField(opc);
        }
    }
}

function OpnModal(opc) {
    if (opc == 3) {
        document.getElementById('alerta').style.display = 'block';
    } else {
        document.getElementById('id0' + opc).style.display = 'block';
    }
}

function ClearField(opc) {
    if (opc == 1) {
        //limpia campos de login
        document.getElementById("name").value = "";
        document.getElementById("psw").value = "";

    } else {
        //limpia campos de registro
        document.getElementById("name1").value = "";
        document.getElementById("psw1").value = "";
        document.getElementById("psw2").value = "";
    }
}

/**
 * leer perfil, cargar menus
 * @returns {undefined}
 */
function initMain() {

    document.getElementById("menu_boton").innerHTML = "";
    document.getElementById("contenido_pantalla").innerHTML = "<div><center><img id='imagen_tmp' src='http://201.161.14.206:9001/cofide/images/cofide_newlogo_a_MR.png'></center></div>";

    var strPost = "";
    strPost += "&perfil=" + document.getElementById("usuario_perfil").value;
    $.ajax({
        type: "POST",
        scriptCharset: "UTF-8",
        data: strPost,
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "html",
        url: "cofide_incidencia.jsp?ID=3",
        success: function (datos) {
            document.getElementById("menu_boton").innerHTML = datos;
        }, error: function (objeto, quepaso, otroobj) {
            alert(":pto=3:" + objeto + " " + quepaso + " " + otroobj);
        }});
}

/**
 * abre los botones que tiene permiso el usuario
 * @param {type} opc
 * @returns {undefined}
 */
function OpnMainSelection(opc) {
//    alert("opción de boton: " + opc);
    if (opc == 1) { //ADMINISTRACIÓN DE USUARIOS
        showUsuarios();
    }
    if (opc == 2) { //ADMINISTRACIÓN DE PERFILES
//        location.href = 'index.html';
    }
    if (opc == 3) { //ALTA DE INCIDENCIAS
        initIncidencias();
    }
    if (opc == 4) { //ADMINISTRACIÓN DE INCIDENCIAS
        showIncidencia();
    }
    if (opc == 5) { //GRAFICAS

        var strCuerpo = "<center>"
                + "<div><h2>REPORTE DE INCIDENCIAS POR USUARIO</h2></div>"
                + "<div text-align='center'>"
                + "<div><label>FECHA INICIAL</label></div>"
                + "<div><input type='date' id='fecini'></div>"
                + "<div><label>FECHA FINAL</label></div>"
                + "<div><input type='date' id='fecfin'></div>"
                + "<div><input type='button' id='enviar' onclick='abreReporte()' value='Exportar Reporte'></div>"
                + "</div>"
                + "</center>"
        document.getElementById("contenido_pantalla").innerHTML = strCuerpo;
    }
    if (opc == 6) {
        location.href = 'index.html';
    }
}

function test() {
    alert("probando ando");
}

//function Abrir_Link(e, b, d, a, f, c) {
//    popupWin = window.open(e, b, "menubar=no,toolbar=no,location=no,directories=no,status=no,scrollbars,resizable,dependent=no,width=" + d + ",height=" + a + ",left=" + f + ",top=" + c)
//}

function abreReporte() {

    var strFechaIni = document.getElementById("fecini").value;
    var strFechaFin = document.getElementById("fecfin").value;
    var strUsuario = document.getElementById("usuario_id").value;

    if (strFechaFin != "" && strFechaIni != "") {

        strFechaIni = strFechaIni.replace("-", "").replace("-", "");
        strFechaFin = strFechaFin.replace("-", "").replace("-", "");

        Abrir_Link("..\\JasperReport?REP_ID=547&boton_1=PDF&usuario=" + strUsuario + "&fecini=" + strFechaIni + "&fecfin=" + strFechaFin, '_reporte', 500, 600, 0, 0);
    } else {
        alert("Es necesario seleccionar un periodo de fechas");
    }
}
