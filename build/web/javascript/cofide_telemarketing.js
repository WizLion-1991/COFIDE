function cofide_telemarketing() {
}
var itemIdCob = 1;
var lastselBco = 0;
var itemIdVta = 0; //historial de ventas
var intContaPendiente = 0;
var lstElemPendiente = new Array();
var timTimer;
var strOpContacto = ""; //operacion de edicion de contactos

//checkbox para clasificar AAA
var estilo = '<style>'
        + '.checkbtn{visibility: hidden;}'
        + '.checkbox-1 {width: 40px; height: 40px; margin-left: 10px; border-square: 50%; position: relative;}'
        + '.checkbox-1 label {background: #D8D8D8; display: block; width: 30px; height: 30px; border-square: 50%; cursor: pointer; position: absolute; top: 10px; left: 10px; z-index: 1; box-shadow: 0 0 5px rgba(0,0,0,0.7) inset;transition: all .5s ease;border: 2px solid #99cc00;}'
        + '.checkbox-1 input[type=checkbox]:checked + label { background: #FF6500; width: 30px; height: 30px; top: 10px; left: 10px;border: 2px solid #99cc00;}'
        + '</style>';
var check = '<section style="padding-left:180px"><div style="padding-left:15px" class="form_field">AAA</div><div class="checkbox-1">'
        + '<input class="checkbtn" type="checkbox" id="CT_AAA" value="1" name="" />'
        + '<label for="CT_AAA"></label>'
        + '</div></section>';
//checkbox para clasificar AAA


//contactos
var tmpID = "";
var tmpNOmbre = "";
var tmpAppat = "";
var tmpApmat = "";
var tmpTitulo = "";
var tmpNoSocio = "";
var tmpArea = "";
var tmpAsoc = "";
var tmpCorreo = "";
var tmpCorreo2 = "";
var tmpTelefono = "";
var tmpTelefono2 = "";
var tmpExt = "";
var tmpAlt = "";
var tmpAlt2 = "";
var tmpMailMes1 = "";
var tmpMailMes2 = "";
var tmpIDContacto = "";
var tmpPuntos = "0";
var tmpOperacion = "";
//contactos
//duplicados
var intDuplicado1 = "0";
var intDuplicado2 = "0";
var bolValida1 = false;
var bolValida2 = false;
//duplicados
function hideContactosTMK() {
    document.getElementById("CCO_NOMBRE").parentNode.parentNode.style.display = "none";
    document.getElementById("CCO_APPATERNO").parentNode.parentNode.style.display = "none";
    document.getElementById("CCO_APMATERNO").parentNode.parentNode.style.display = "none";
    document.getElementById("CCO_TITULO").parentNode.parentNode.style.display = "none";
    document.getElementById("CCO_NOSOCIO").parentNode.parentNode.style.display = "none";
    document.getElementById("CCO_AREA").parentNode.parentNode.style.display = "none";
    document.getElementById("CCO_ASOCIACION").parentNode.parentNode.style.display = "none";
    document.getElementById("CCO_CORREO").parentNode.parentNode.style.display = "none";
    document.getElementById("CCO_CORREO2").parentNode.parentNode.style.display = "none";
    document.getElementById("CCO_TELEFONO").parentNode.parentNode.style.display = "none";
    document.getElementById("CCO_EXTENCION").parentNode.parentNode.style.display = "none";
    document.getElementById("CCO_ALTERNO").parentNode.parentNode.style.display = "none";
    document.getElementById("CT_MAILMES1").parentNode.parentNode.style.display = "none";
    document.getElementById("CT_MAILMES2").parentNode.parentNode.style.display = "none";
    document.getElementById("CCO_NOMBRE").value = "";
    document.getElementById("CCO_APPATERNO").value = "";
    document.getElementById("CCO_APMATERNO").value = "";
    document.getElementById("CCO_TITULO").value = "";
    document.getElementById("CCO_NOSOCIO").value = "";
    document.getElementById("CCO_AREA").value = "";
    document.getElementById("CCO_ASOCIACION").value = "";
    document.getElementById("CCO_CORREO").value = "";
    document.getElementById("CCO_CORREO2").value = "";
    document.getElementById("CCO_TELEFONO").value = "";
    document.getElementById("CCO_EXTENCION").value = "";
    document.getElementById("CCO_ALTERNO").value = "";
    document.getElementById("CT_MAILMES1").value = "";
    document.getElementById("CT_MAILMES2").value = "";
    document.getElementById("CT_FECHA").value = "";
    document.getElementById("CT_HORA").value = "";
    document.getElementById("CT_COMENTARIOS").parentNode.parentNode.style.display = "";
    document.getElementById("BTN_ADD").parentNode.parentNode.style.display = "none";
    document.getElementById("BTN_CANCEL").parentNode.parentNode.style.display = "none";
    document.getElementById("BTN_NEW").parentNode.parentNode.style.display = "";
    document.getElementById("BTN_DEL").parentNode.parentNode.style.display = "";
    document.getElementById("BTN_EDIT").parentNode.parentNode.style.display = "";
    document.getElementById("BTN_RNEW").parentNode.parentNode.style.display = "";
    document.getElementById("BTN_REDIT").parentNode.parentNode.style.display = "";
    document.getElementById("BTN_RDEL").parentNode.parentNode.style.display = "";
    document.getElementById("BTN_RCANCEL").parentNode.parentNode.style.display = "none";
    document.getElementById("BTN_RSAVE").parentNode.parentNode.style.display = "none";
    document.getElementById("CT_ADDRZN").parentNode.parentNode.style.display = "none";
    document.getElementById("CT_CORREO").style["background"] = "#FFFFFF";
    document.getElementById("CT_CORREO").style.borderColor = "#D8D8D8";
    document.getElementById("CCO_CORREO").style["background"] = "#FFFFFF";
    document.getElementById("CCO_CORREO2").style["background"] = "#FFFFFF";
    document.getElementById("CCO_CORREO").style.borderColor = "#D8D8D8";
    document.getElementById("CCO_CORREO2").style.borderColor = "#D8D8D8";
    document.getElementById("CT_CTE_NUEVO").value = "0";
//    document.getElementById("CT_CURSO_INTERES").parentNode.parentNode.style.display = "none";
    document.getElementById("CT_ADDCURSO").style.backgroundColor = "#FF5733";
    document.getElementById("CT_ADDCURSO").style.color = "#FFF";
    intDuplicado1 = "0";
    intDuplicado2 = "0";
    bolValida1 = false;
    bolValida2 = false;
//    console.log("termino de ocultar los campos alv");
}
function initTelem() {

//    document.getElementById("CT_CTE").innerHTML = estilo + "<table>"
//            + "<tr>"
//            + '<td class=\'Prospecto\'><i class = "fa fa-user" title="Prospecto" style="font-size:55px;"></i></td>'
//            + '<td>' + check + '</td>'
//            + "</tr>"
//            + "</table>";

    myLayout.close("west");
    myLayout.close("east");
    myLayout.close("south");
    myLayout.close("north");
    selectBaseCliente();
    hideContactosTMK();
    checkInBound();
//    consultaVta();    
    document.getElementById("CT_ESTATUS").value = "DISPONIBLE"; //inicia disponible
    var strDoMail = "<table border='0' width='0%' align='center'>"
            + "<tr>"
//            + '<td><a href="javascript:EnviaFacturaMail()" ><i class = "fa fa-envelope-o" title="ENVÍO DE PLANTILLAS"  style="font-size:50px; color: #00BFFF"></i></td>'
            + '<td><a href="javascript:opnEnvioPlantillaVenta()" ><i class = "fa fa-envelope-o" title="ENVÍO DE PLANTILLAS"  style="font-size:50px; color: #00BFFF"></i></a></td>'
            + "</tr>";
    "</table>";
    document.getElementById("DO_MAIL").innerHTML = strDoMail;
}

function HtmlBoton(bolBase, telefono, correo, isValid) {
    if (isValid) {
        if (bolBase == "false") {
            var strHtmlCall = "<table align='center'>"
                    + "<tr>"
                    + '<td><a href="javascript:reg_llam()" class= \'cofide_llamada\'><i class = "fa fa-headphones" title="Llamar" style="font-size:40px;"></i></td>'
                    + "<td>&nbsp;</td>"
                    + "<td class= 'cofide_message'><a href=\"mailto:"
                    + correo + '?Subject=Estimado%20" target="_top"><i class = "fa fa-envelope-o" style="font-size:40px"></i></a></td>'
                    + "<td>&nbsp;</td>"
                    + "</tr>";
            "</table>";
            var strHtmlTitle = "<table border='0' width='0%' align='center'>"
                    + "<tr>"
                    + '<td><a href="javascript:OpnMotorBusqueda()" class=\'cofide_search\'><i class = "fa fa-search-plus" title="Motor de Busqueda" style="font-size:30px; width:110px"></i></td>'
                    + "<td>&nbsp;</td>"
                    + '<td><a href="javascript:tmp_Guardar()" class=\'cofide_changei\'><i class = "fa fa-step-forward" title="Cambiar Llamada" style="font-size:30px; width:110px"></i></td>'
                    + "<td>&nbsp;</td>"
                    + '<td><a href="javascript:OpnDiagHCall()" class=\'cofide_histl\'><i class = "fa fa-clock-o" title="Historial de Llamadas" style="font-size:30px; width:110px"></i></td>'
                    + "<td>&nbsp;</td>"
                    + '<td><a href="javascript:OpnDiagNVenta(\'0\')" class=\'cofide_venta\'><i class = "fa fa-cart-plus" title="Ventas" style="font-size:30px; width:110px"></i></td>'
                    + "<td>&nbsp;</td>"
                    + '<td><a href="javascript:OpnDiagHVenta()" class=\'cofide_histv\'><i class = "fa fa-money" title="Historial de Ventas" style="font-size:30px; width:110px"></i></td>'
                    + "<td>&nbsp;</td>"
                    + '<td><a href="javascript:OpnDiagCActivo()" class=\'cofide_calcurso\'><i class = "fa fa-calendar" title="Calendario de Cursos" style="font-size:30px; width:110px"></i></td>'
                    + "<td>&nbsp;</td>"
                    + '<td><a href="javascript:sendMailGroup()" class=\'cofide_message\'><i class = "fa fa-at" title="Mail Group"style="font-size:40px; width: 110px"></i></a></td>'
                    + "<td>&nbsp;</td>"
                    + '<td><a href="javascript:saveDetaTMK()" class=\'cofide_guarda\'><i class = "fa fa-floppy-o" title="Guardar Cambios" style="font-size:30px; width:110px"></i></td>'
                    + "<td>&nbsp;</td>"
                    + '<td><a href="javascript:OpnPausarLlamada()" class=\'cofide_pausa\'><i class = "fa fa-pause" title="Pausar Llamada" style="font-size:30px; width:110px"></i></td>'
                    + "<td>&nbsp;</td>"
                    + '<td><a href="javascript:OpnSalirLlamada()" class=\'cofide_salida\'><i class = "fa fa-sign-out" title="Salir"  style="font-size:30px; width:110px"></i></td>'
                    + "</tr>";
            "</table>";
            document.getElementById("CT_TITLEBTN").innerHTML = strHtmlTitle;
            document.getElementById("CT_CALLBTN").innerHTML = strHtmlCall;
        } else {
            if (bolBase == "true") {
                var strHtmlCall = "<table align='center'>"
                        + "<tr>"
                        + '<td><a href="javascript:reg_llam()" class= \'cofide_llamada\'><i class = "fa fa-headphones" title="Llamar" style="font-size:40px;"></i></td>'
                        + "<td>&nbsp;</td>"
                        + "<td class= 'cofide_message'><a href=\"mailto:" + correo + '?Subject=Estimado%20" target="_top"><i class = "fa fa-envelope-o" style="font-size:40px"></i></a></td>'
                        + "<td>&nbsp;</td>"
                        + "</tr>";
                "</table>";
                var strHtmlTitle = "<table border='0' width='0%' align='center'>"
                        + "<tr>"
                        + '<td><a href="javascript:OpnMotorBusqueda()" class=\'cofide_search\'><i class = "fa fa-search-plus" title="Motor de Busqueda" style="font-size:30px; width:110px"></i></td>'
                        + "<td>&nbsp;</td>"
                        + '<td><a href="javascript:OpnDiagNVenta(\'0\')" class=\'cofide_venta\'><i class = "fa fa-cart-plus" title="Ventas" style="font-size:30px; width:110px">&nbsp; VENTAS</i></td>'
                        + "<td>&nbsp;</td>"
                        + '<td><a href="javascript:OpnDiagCActivo()" class=\'cofide_calcurso\'><i class = "fa fa-calendar" title="Calendario de Cursos" style="font-size:30px; width:110px"></i></td>'
                        + "<td>&nbsp;</td>"
                        + '<td><a href="javascript:saveDetaTMK()" class=\'cofide_guarda\'><i class = "fa fa-floppy-o" title="Guardar Cambios" style="font-size:30px; width:110px"></i></td>'
                        + "<td>&nbsp;</td>"
                        + '<td><a href="javascript:sendMailGroup()" class=\'cofide_message\'><i class = "fa fa-at" title="Mail Group"style="font-size:40px; width: 110px"></i></a></td>'
                        + "<td>&nbsp;</td>"
                        + '<td><a href="javascript:DelCteBase()" class=\'cofide_borrar\'><i class = "fa fa-recycle" style="font-size:30px; width:110px">&nbsp; BORRAR</i></td>'
                        + "<td>&nbsp;</td>"
                        + '<td><a href="javascript:OpnPausarLlamada()" class=\'cofide_pausa\'><i class = "fa fa-pause" title="Pausar Llamada"  style="font-size:30px; width:110px"></i></td>'
                        + "<td>&nbsp;</td>"
                        + '<td><a href="javascript:OpnSalirLlamada()" class=\'cofide_salida\'><i class = "fa fa-sign-out" title="Salir" style="font-size:30px; width:110px"></i></td>'
                        + "</tr>";
                "</table>";
                document.getElementById("CT_TITLEBTN").innerHTML = strHtmlTitle;
                document.getElementById("CT_CALLBTN").innerHTML = strHtmlCall;
            }
        }
    } else {
        var strHtmlCall = "";
        var strHtmlTitle = "<table border='0' width='0%' align='center'>"
                + "<tr>"
                + "<td>&nbsp;</td>"
                + "<tr>"
                + '<td><a href="javascript:OpnMotorBusqueda()" class=\'cofide_search\'><i class = "fa fa-search-plus" title="Motor de Busqueda" style="font-size:30px; width:110px"></i></td>'
                + "<td>&nbsp;</td>"
                + '<td><a href="javascript:OpnSalirLlamada()" class=\'cofide_salida\'><i class = "fa fa-sign-out" title="Salir"  style="font-size:30px; width:110px"></i></td>'
                + "</tr>";
        "</table>";
        document.getElementById("CT_TITLEBTN").innerHTML = strHtmlTitle;
        document.getElementById("CT_CALLBTN").innerHTML = strHtmlCall;
    }
//drawRightPanel();
    var strHtmlBTN = "<center><table border='0' width='0%' align='center'>" //botones de agregar nuevo cliente, error en base, actualizar 
            + "<tr>"
            + "<td>&nbsp;</td>"
            + "<tr>"
            + '<td><a href="javascript:OpnNewCte()" class=\'cofide_venta\'><i class = "fa fa-user-plus" title="Agregar Prospecto" style="font-size:50px; width:110px"></i></td>'
            + "<td>&nbsp;</td>"
            + '<td><a href="javascript:errorbase()" class=\'cofide_err\'><i class = "fa fa-trash" title="Error en Base" style="font-size:50px; width:110px"></i></td>'
            + "<td>&nbsp;</td>"
            + '<td><a href="javascript:UpDateCte()" class=\'updatecte\'><i class = "fa fa-refresh" title="Actualiza Cliente" style="font-size:50px; width:110px"></i></td>'
            + "<td>&nbsp;</td>"
            + "</tr>";
    "</table></center>";
    document.getElementById("btnadd").innerHTML = strHtmlBTN; //botones de marcar y mandar email
    var strHtmlBtnCall = "<table border='0' width='0%' align='center'>"
            + "<tr>"
            + '<td><a href="javascript:LlamarContacto()" class=\'cofide_llamada\'><i class = "fa fa-headphones" title="llamar" style="font-size:30px; width:110px"></i></td>'
            + "</tr>"
            + "</table>";
    var strHtmlMail = "<table border='0' width='0%' align='center'>"
            + "<tr>"
            + '<td><a href="javascript:SendMailContacto()" class=\'cofide_message\'><i class = "fa fa-envelope-o" title="Envio" style="font-size:30px; width:110px"></i></td>'
            + "</tr>"
//            + "<tr>"
//            + '<td><a href="javascript:SendTarjetaCtPreferente()"><i class = "fa fa-credit-card" title="Tarjeta Cliente Preferente" style="font-size:30px; width:110px"></i></td>'
//            + "</tr>"
            + "</table>";

    document.getElementById("CALLBTN2").innerHTML = strHtmlBtnCall;
    document.getElementById("CT_SENDMAIL").innerHTML = strHtmlMail;
}
function reg_llam() {
    document.getElementById("CT_BOLCALL").value = "1";
    LlamadaOK();
    //aqui ira el metodo para mandar la llamada al PBX
}
function _resetLlamadas() {
    if (document.getElementById("CT_AAA") != null) {
        document.getElementById("CT_AAA").checked = false;
    }
    document.getElementById("CT_MKT").value = "0";
    document.getElementById("CT_ID_CLIENTE").value = "0";
    document.getElementById("CT_CLAVEBD").value = "";
    document.getElementById("CT_BDANTERIOR").value = "0";
    document.getElementById("CT_ID").value = "0";
    document.getElementById("CT_NO_CLIENTE").value = "0";
    document.getElementById("CT_RAZONSOCIAL").value = "";
    document.getElementById("CT_RFC").value = "";
    document.getElementById("CT_CONTACTO").value = "";
    document.getElementById("CT_CONTACTO2").value = "";
    document.getElementById("CT_CONMUTADOR").value = "";
    document.getElementById("CT_CORREO").value = "";
    document.getElementById("CT_CORREO2").value = "";
    document.getElementById("CCO_NOMBRE").value = "";
    document.getElementById("CCO_APPATERNO").value = "";
    document.getElementById("CCO_APMATERNO").value = "";
    document.getElementById("CCO_TITULO").value = "";
    document.getElementById("CCO_NOSOCIO").value = "";
    document.getElementById("CCO_CORREO").value = "";
    document.getElementById("CCO_CORREO2").value = "";
    document.getElementById("CCO_TELEFONO").value = "";
    document.getElementById("CCO_EXTENCION").value = "";
    document.getElementById("CCO_ALTERNO").value = "";
//    document.getElementById("CT_RAZONSOCIAL2").value = "";
//    document.getElementById("CT_ID_CLIENTE2").value = "";
//    document.getElementById("CT_RAZONSOCIAL3").value = "";
//    document.getElementById("CT_ID_CLIENTE3").value = "";
//    document.getElementById("CT_RAZONSOCIAL4").value = "";
//    document.getElementById("CT_ID_CLIENTE4").value = "";
//    document.getElementById("CT_RAZONSOCIAL5").value = "";
//    document.getElementById("CT_ID_CLIENTE5").value = "";
    document.getElementById("CT_HORA").value = "";
    document.getElementById("CT_COMENTARIOS").value = "";
    document.getElementById("CT_COMENTARIO").value = "";
//    document.getElementById("CT_RAZONSOCIAL2").value = "";
    document.getElementById("CT_BOLCALL").value = "0";
    document.getElementById("CT_SEDE").value = "";
    document.getElementById("CT_GIRO").value = "";
    document.getElementById("CT_AREA").value = "";
    document.getElementById("CCO_AREA").value = "";
    document.getElementById("CCO_ASOCIACION").value = "";
    document.getElementById("CT_FECHA").value = "";
    document.getElementById("CT_BOLBASE").value = "";
    document.getElementById("CT_COL").value = "";
    document.getElementById("CT_CALLE").value = "";
    document.getElementById("CT_EDO").value = "";
    document.getElementById("CT_MUNI").value = "";
    document.getElementById("CT_NUM").value = "";
    document.getElementById("CT_CP").value = "";
    document.getElementById("CT_LADA").value = "";
    document.getElementById("CT_MAILMES").checked = false;
    document.getElementById("CT_MAILMES1").checked = false;
    document.getElementById("CT_MAILMES2").checked = false;
    document.getElementById("CT_CONTACTO_ENTRADA").value = "";
}
function consultaVta(idCte) {
    hideContactosTMK();
    var strRespuesta = "";
    var strMensaje = "";
    document.getElementById("CT_RESPOND").value = ""; //inicia limpia la llamada!
    $("#tabsC_TELEM").tabs("option", "active", 0);
    _resetLlamadas();
//    setTimeout("llenarColonia()", 1000);
    var strPost = "strCodigoBase=" + document.getElementById("CT_CLAVE_DDBB").value;
    if (idCte != null) {
        strPost += "&cte_manual=" + idCte;
    }
    document.getElementById("CT_ID").disabled = true;
    $.ajax({
        type: "POST",
        data: strPost,
        scriptCharset: "UTF-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "COFIDE_Telemarketing.jsp?ID=1",
        success: function (datos) {
            var lstXml = datos.getElementsByTagName("vta")[0];
            var lstCte = lstXml.getElementsByTagName("datos");
            for (var i = 0; i < lstCte.length; i++) {
                llenarColonia();
                var objcte = lstCte[i];
                if (objcte.getAttribute("CT_ID") != 0) {
                    document.getElementById("CT_ID").value = objcte.getAttribute("CT_ID");
                    document.getElementById("CT_ID_CLIENTE").value = objcte.getAttribute("CT_ID_CLIENTE");
                    document.getElementById("CT_RAZONSOCIAL").value = objcte.getAttribute("CT_RAZONSOCIAL");
                    document.getElementById("CT_NO_CLIENTE").value = objcte.getAttribute("CT_ID");
                    document.getElementById("CT_RFC").value = objcte.getAttribute("CT_RFC");
                    document.getElementById("CT_COL").value = objcte.getAttribute("CT_COLONIA");
                    document.getElementById("CT_CONTACTO").value = objcte.getAttribute("CT_TELEFONO1");
                    document.getElementById("CT_CONTACTO2").value = objcte.getAttribute("CT_TELEFONO2");
                    document.getElementById("CT_CORREO").value = objcte.getAttribute("CT_EMAIL1");
                    document.getElementById("CT_CORREO2").value = objcte.getAttribute("CT_EMAIL2");
                    document.getElementById("CT_BOLBASE").value = objcte.getAttribute("bolBase");
                    document.getElementById("CT_HORA_INI").value = objcte.getAttribute("HoraInicial");
                    document.getElementById("CT_CP").value = objcte.getAttribute("CT_CP");
                    document.getElementById("CT_COL").value = objcte.getAttribute("CT_COL");
                    document.getElementById("CT_COLONIA_DB").value = objcte.getAttribute("CT_COL");
                    document.getElementById("CT_CALLE").value = objcte.getAttribute("CT_CALLE");
                    document.getElementById("CT_EDO").value = objcte.getAttribute("CT_EDO");
                    document.getElementById("CT_MUNI").value = objcte.getAttribute("CT_MUNI");
                    document.getElementById("CT_NUM").value = objcte.getAttribute("CT_NUMERO");
                    document.getElementById("CT_SEDE").value = objcte.getAttribute("CT_SEDE");
                    document.getElementById("CT_GIRO").value = objcte.getAttribute("CT_GIRO");
                    document.getElementById("CT_AREA").value = objcte.getAttribute("CT_AREA");
                    document.getElementById("CT_COMENTARIO").value = objcte.getAttribute("EV_ASUNTO");
                    document.getElementById("CT_ID_LLAMADA").value = objcte.getAttribute("id_llamada");
                    document.getElementById("CT_CONMUTADOR").value = objcte.getAttribute("CT_CONMUTADOR");
                    document.getElementById("CT_CONTACTO_ENTRADA").value = objcte.getAttribute("CT_CONTACTO");
                    document.getElementById("CT_MKT").value = objcte.getAttribute("CT_MKT");
                    if (objcte.getAttribute("CT_AAA") == "1") { // si es AAA el cliente, lo pone en check
//                        document.getElementById("CT_AAA").checked = true;
                        if (document.getElementById("CT_AAA") != null) {
                            document.getElementById("CT_AAA").checked = true;
                        }
                    }
                    //ex participante
                    if (objcte.getAttribute("cte_prosp") == "0") { //verde
                        document.getElementById("exp_pro").value = 0;

                        var strBaseActual = document.getElementById("CT_CLAVE_DDBB").value;
                        var arrBase = ["BE-AAA", ""];
                        if (arrBase.indexOf(strBaseActual) >= 0) {
                            document.getElementById("CT_CTE").innerHTML = estilo + "<table>"
                                    + "<tr>"
                                    + '<td class=\'ExParticipante\'><i class = "fa fa-user" title="EX-Participante" style="font-size:55px;"></i></td>'
                                    + '<td>' + check + '</td>'
                                    + "</tr>"
                                    + "</table>";
                        } else {
                            document.getElementById("CT_CTE").innerHTML = estilo + "<table>"
                                    + "<tr>"
                                    + '<td class=\'ExParticipante\'><i class = "fa fa-user" title="EX-Participante" style="font-size:55px;"></i></td>'
//                                + '<td>' + check + '</td>'
                                    + "</tr>"
                                    + "</table>";
                        }
                    } else { //gris
                        document.getElementById("exp_pro").value = 1;
                        var strBaseActual = document.getElementById("CT_CLAVE_DDBB").value;
                        var arrBase = ["BE-AAA", ""];

                        if (arrBase.indexOf(strBaseActual) >= 0) {

                            document.getElementById("CT_CTE").innerHTML = estilo + "<table>"
                                    + "<tr>"
                                    + '<td class=\'Prospecto\'><i class = "fa fa-user" title="Prospecto" style="font-size:55px;"></i></td>'
                                    + '<td>' + check + '</td>'
                                    + "</tr>"
                                    + "</table>";
                        } else {
                            document.getElementById("CT_CTE").innerHTML = estilo + "<table>"
                                    + "<tr>"
                                    + '<td class=\'Prospecto\'><i class = "fa fa-user" title="Prospecto" style="font-size:55px;"></i></td>'
//                                + '<td>' + check + '</td>'
                                    + "</tr>"
                                    + "</table>";
                        }
                    }
                    var bolMail = false;
                    if (objcte.getAttribute("envio_mail") == 1) {
                        bolMail = true;
                    }
                    if (objcte.getAttribute("CT_AAA") == "1") { // si es AAA el cliente, lo pone en check
//                        document.getElementById("CT_AAA").checked = true;
                        if (document.getElementById("CT_AAA") != null) {
                            document.getElementById("CT_AAA").checked = true;
                        }
                    }
                    document.getElementById("CT_MAILMES").checked = bolMail;
                    strRespuesta = objcte.getAttribute("Respuesta");
                    strMensaje = objcte.getAttribute("Mensaje");
                    HtmlBoton(objcte.getAttribute("bolBase"), objcte.getAttribute("CT_TELEFONO1"), objcte.getAttribute("CT_EMAIL1"), true);
                    LoadContacto(document.getElementById("CT_NO_CLIENTE").value, 0);
                    var intInBound = document.getElementById("CT_CLAVE_DDBB").value;
                    var intPermisoInBound = document.getElementById("CT_PERMISO_INBOUND").value;
                    var bolExterno = false; //inbound / emagister
                    if (intInBound == "BE-INBOUND" || intInBound == "BE-EMAGISTER" || intInBound == "") {
                        bolExterno = true;
                    }
                    var strDB = objcte.getAttribute("CT_CLAVEDDBB");
                    var bolDB = false; //false = agena a inbound y/o emagister
                    if (strDB == "BE-INBOUND" || strDB == "BE-EMAGISTER") {
                        bolDB = true;
                    }
                    if (intPermisoInBound == 0) {
//                        if (intInBound == "BE-INBOUND" && objcte.getAttribute("CT_CLAVEDDBB") != "BE-INBOUND") {
//                        if ((intInBound == "BE-INBOUND" || intInBound == "") && objcte.getAttribute("CT_CLAVEDDBB") != "BE-INBOUND") { //para los que no tienen base, entran como si fueran inbound peros in poder hacer modificaciones
                        // es usuario inbound, emagister, o sin base && el registro no es del usuario
                        if (bolExterno && !bolDB) { //para los que no tienen base, entran como si fueran inbound peros in poder hacer modificaciones
//                            blockDatosInBound();
//                            hiddeFunctionInboundSave();
                            UnlockDatosInBound();
                            showBtnSaveInBound();
                        } else {
                            UnlockDatosInBound();
                            showBtnSaveInBound();
                        }
                    } else {
                        UnlockDatosInBound();
                        showBtnSaveInBound();
                    }
                } else {
                    HtmlBoton("false", "", "", false);
                }
            }
            if (strRespuesta == "true") {
                alert(strRespuesta + ": " + strMensaje); //es el mensaje de cuando hacen la llamada OKs
                clearTimeout(timTimer);
                Timer();
            } else {
                clearTimeout(timTimer);
                Timer();
            }
//            //actualiza el historial de ventas
//            HistorialVtaCte(document.getElementById("CT_ID").value);
            showCursoInteres = 0;
            LoadCursoSugerido(); //cargar cursos de interes
        }
    });
    document.getElementById("BTN_ADD").parentNode.parentNode.style.display = "none";
    document.getElementById("BTN_CANCEL").parentNode.parentNode.style.display = "none";
    document.getElementById("BTN_NEW").parentNode.parentNode.style.display = "";
    document.getElementById("BTN_DEL").parentNode.parentNode.style.display = "";
    document.getElementById("CT_COMENTARIO").value = ""; //limpia comentario despues de guardar
    drawRightPanel();
}
function limpiarTodo() {
    myLayout.open("west");
    myLayout.open("east");
    myLayout.open("south");
    myLayout.open("north");
    document.getElementById("MainPanel").innerHTML = "";
    document.getElementById("rightPanel").innerHTML = "";
    var objMainFacPedi = objMap.getScreen("C_TELEM");
    objMainFacPedi.bolActivo = false;
    objMainFacPedi.bolMain = false;
    objMainFacPedi.bolInit = false;
    objMainFacPedi.idOperAct = 0;
}
function newTelem(opc) {
    if (opc == 1) {
        hideContactosTMK();
        //limpia campos
        if (intRow() < 30) {
            document.getElementById("CCO_NOMBRE").parentNode.parentNode.style.display = "";
            document.getElementById("CCO_APPATERNO").parentNode.parentNode.style.display = "";
            document.getElementById("CCO_APMATERNO").parentNode.parentNode.style.display = "";
            document.getElementById("CCO_TITULO").parentNode.parentNode.style.display = "";
            document.getElementById("CCO_NOSOCIO").parentNode.parentNode.style.display = "";
            document.getElementById("CCO_AREA").parentNode.parentNode.style.display = "";
            document.getElementById("CCO_ASOCIACION").parentNode.parentNode.style.display = "";
            document.getElementById("CCO_CORREO").parentNode.parentNode.style.display = "";
            document.getElementById("CCO_CORREO2").parentNode.parentNode.style.display = "";
            document.getElementById("CCO_TELEFONO").parentNode.parentNode.style.display = "";
            document.getElementById("CCO_EXTENCION").parentNode.parentNode.style.display = "";
            document.getElementById("CCO_ALTERNO").parentNode.parentNode.style.display = "";
            document.getElementById("BTN_ADD").parentNode.parentNode.style.display = "";
            document.getElementById("CT_MAILMES1").parentNode.parentNode.style.display = "";
            document.getElementById("CT_MAILMES1").checked = false;
            document.getElementById("CT_MAILMES2").parentNode.parentNode.style.display = "";
            document.getElementById("CT_MAILMES2").checked = false;
            document.getElementById("BTN_CANCEL").parentNode.parentNode.style.display = "";
            document.getElementById("BTN_NEW").parentNode.parentNode.style.display = "none";
            document.getElementById("BTN_DEL").parentNode.parentNode.style.display = "none";
            document.getElementById("BTN_EDIT").parentNode.parentNode.style.display = "none";
            tmpOperacion = "N";
        } else {
            alert("Ya llegaste a los 30 contactos permitidos");
        }
    }
    if (opc == 2) {
        document.getElementById("BTN_RNEW").parentNode.parentNode.style.display = "none";
        document.getElementById("BTN_REDIT").parentNode.parentNode.style.display = "none";
        document.getElementById("BTN_RDEL").parentNode.parentNode.style.display = "none";
        document.getElementById("BTN_RCANCEL").parentNode.parentNode.style.display = "";
        document.getElementById("BTN_RSAVE").parentNode.parentNode.style.display = "";
        document.getElementById("CT_ADDRZN").parentNode.parentNode.style.display = "";
        document.getElementById("CT_ADDRZN").value = "";
    }
}
function cancelTelem(opc) {
    if (opc == 1) {
        if (tmpOperacion == "C") {
//regresa los tmp al grid
            var datarow = {
                CCO_ID: tmpID,
                CCO_NOMBRE: tmpNOmbre,
                CCO_APPATERNO: tmpAppat,
                CCO_APMATERNO: tmpApmat,
                CCO_PUNTOS: tmpPuntos, //puntos
                CCO_TITULO: tmpTitulo,
                CCO_NOSOCIO: tmpNoSocio,
                CCO_AREA: tmpArea,
                CCO_ASOCIACION: tmpAsoc,
                CCO_CORREO: tmpCorreo,
                CCO_CORREO2: tmpCorreo2,
                CCO_TELEFONO: tmpTelefono,
                CCO_TELEFONO2: tmpTelefono2,
                CCO_EXTENCION: tmpExt,
                CCO_ALTERNO: tmpAlt,
                CCO_ALTERNO2: tmpAlt2,
                CT_MAILMES1: tmpMailMes1,
                CONTACTO_ID: tmpIDContacto, //id principal
                CT_MAILMES2: tmpMailMes2
            };
            jQuery("#GRIDCONTACTOS").addRowData(itemIdCob, datarow, "last");
            hideContactosTMK();
        } else {
            hideContactosTMK();
        }
        tmpOperacion = "";
    }
    if (opc == 2) {
        var strRzn = document.getElementById("CT_ADDRZN").value;
        if (strRzn != "") {
            var datarow = {
                RZN_ID: intRowRzn() + 1,
                RZN_NOMBRE: strRzn
            };
        }
        document.getElementById("CT_ADDRZN").value = "";
        LimpiarGrid(opc);
        hideContactosTMK();
        itemIdCob++;
        jQuery("#GRD_RZN").addRowData(itemIdCob, datarow, "last");
    }
}
function addGridTMK(opc) {

    if (opc == 1) {

        var intPuntos = "<div style=\"text-align:center\"><i title='No aplica Promoción' >\t 0</i></div>";
        var intIdContacto = 0;
        if (tmpPuntos != 0) {
            intPuntos = tmpPuntos;
        }
        if (tmpIDContacto != 0) {
            intIdContacto = tmpIDContacto;
        }
        if (intDuplicado1 != "1" && intDuplicado2 != "1") {
            //Validamo aca correo existente
            var boolContacto = 0;
            var boolCorreo = 0;
            var boolNoSocio = 0;
            var intOK = 0;
            if (document.getElementById("CCO_NOMBRE").value != "") {
                boolContacto = 1;
            } else {
                alert("Capture el Nombre del Contacto");
            }
            if (document.getElementById("CCO_CORREO").value != "" || document.getElementById("CCO_TELEFONO").value != "") {
                boolCorreo = 1;
            } else {
                boolCorreo = 0;
                //Regresamos mensaje para decir que campo viene vacio       
                if (document.getElementById("CCO_CORREO").value == "") {
                    alert("Capture el Correo del Contacto");
                } else {
                    alert("Capture el Teléfono del Contacto");
                }
            }
            if (document.getElementById("CCO_ASOCIACION").value == "CCPM" || document.getElementById("CCO_ASOCIACION").value == "AMCP") {

                boolNoSocio = 1;

            } else {
                boolNoSocio = 1;
            }
            if (boolContacto == 1 && boolCorreo == 1 && boolNoSocio == 1) {
                var strNombre = document.getElementById("CCO_NOMBRE").value;
                var strApPat = document.getElementById("CCO_APPATERNO").value;
                var strApMat = document.getElementById("CCO_APMATERNO").value;
                var strTitulo = document.getElementById("CCO_TITULO").value;
                var strSocio = document.getElementById("CCO_NOSOCIO").value;
                var strArea = document.getElementById("CCO_AREA").value;
                var strAsociacion = document.getElementById("CCO_ASOCIACION").value;
                var strCorreo = document.getElementById("CCO_CORREO").value;
                var strCorreo2 = document.getElementById("CCO_CORREO2").value;
                var strTelefono = document.getElementById("CCO_TELEFONO").value;
                var strExt = document.getElementById("CCO_EXTENCION").value;
                var strAlt = document.getElementById("CCO_ALTERNO").value;
                var bolMail1 = document.getElementById("CT_MAILMES1").checked;
                var bolMail2 = document.getElementById("CT_MAILMES2").checked;
                var intMail1 = "NO";
                if (bolMail1 == true) {
                    intMail1 = "SI";
                }
                var intMail2 = "NO";
                if (bolMail2 == true) {
                    intMail2 = "SI";
                }
                var strTel = "";
                var strTelAlterno = "";

                if (strTelefono != "") {
                    strTel = "<b><a style=\"color:#0B610B\" href=\"javascript:LlamarContacto_('" + strTelefono + "')\"<i class=\"fa fa-headphones\">\t" + strTelefono + "</i></a></b>";
                }
                if (strAlt != "") {
                    strTelAlterno = "<b><a style=\"color:#0B610B\" href=\"javascript:LlamarContacto_('" + strAlt + "')\"<i class=\"fa fa-headphones\">\t" + strAlt + "</i></a></b>";
                }
                if (intRow() < 30) {
                    var datarow = {
                        CCO_ID: intRow() + 1,
                        CONTACTO_ID: intIdContacto,
                        CCO_NOMBRE: strNombre,
                        CCO_APPATERNO: strApPat,
                        CCO_APMATERNO: strApMat,
                        CCO_PUNTOS: intPuntos,
                        CCO_TITULO: strTitulo,
                        CCO_NOSOCIO: intDato(strSocio),
                        CCO_AREA: strArea,
                        CCO_ASOCIACION: strAsociacion,
                        CCO_CORREO: strCorreo,
                        CCO_CORREO2: strCorreo2,
                        CCO_TELEFONO: strTelefono,
                        CCO_TELEFONO2: strTel,
                        CCO_EXTENCION: strExt,
                        CCO_ALTERNO: strAlt,
                        CCO_ALTERNO2: strTelAlterno,
                        CT_MAILMES1: intMail1,
                        CT_MAILMES2: intMail2
                    };
//                    beforeAddContacMail1(3, datarow);
                    hideContactosTMK();
                    intDuplicado1 = "0";
                    intDuplicado2 = "0";
                    //actualiza información directamente a la tabla al guardar
                    UpdateContacto(tmpOperacion, intIdContacto, strNombre, strApPat, strApMat, strTitulo, intDato(strSocio), strArea, strAsociacion, strCorreo, strCorreo2, strTelefono, strExt, strAlt, intMail1, intMail2);
                } else {
                    alert("El Limite son 30 Contactos");
                }
                itemIdCob++;
                jQuery("#GRIDCONTACTOS").addRowData(itemIdCob, datarow, "last");
                tmpPuntos = "<div style=\"text-align:center\"><i title='No aplica Promoción' >\t0</i></div>"; //reinicia la variable
                tmpIDContacto = 0;
            }
        } else {
            alert("Se detecto un conflicto, revisar información");
        }
    }
    if (opc == 2) {
        var intCT_ID = document.getElementById("CT_ID").value;
        var strRzn = document.getElementById("CT_ADDRZN").value;
        if (strRzn != "") {
            var datarow = {
                RZN_ID: intRowRzn() + 1,
                RZN_CTE: intCT_ID,
                RZN_NOMBRE: strRzn
            };
            LimpiarGrid(opc);
            hideContactosTMK();
            itemIdCob++;
            jQuery("#GRD_RZN").addRowData(itemIdCob, datarow, "last");
        } else {
            alert("Ingresa una razón social");
        }
    }
}
function intRow() {
    var intRow;
    var grid = jQuery("#GRIDCONTACTOS");
    var idArr = grid.getDataIDs();
    intRow = idArr.length;
    document.getElementById("GRIDCONTACTOS").value = intRow;
    return intRow;
}
function intRowRzn() {
    var intRow;
    var grid = jQuery("#GRD_RZN");
    var idArr = grid.getDataIDs();
    intRow = idArr.length;
    document.getElementById("GRD_RZN").value = intRow;
    return intRow;
}
function delGridTMK(opc) {
    var grid = "";

    if (opc == 1) {

        grid = jQuery("#GRIDCONTACTOS");

        if (grid.getGridParam("selrow") != null) {

            document.getElementById("SioNO_inside").innerHTML = "<b>¿DESEAS ELIMINAR EL REGISTRO?<br>ESTA ACCIÓN YA NO SE PODRÁ DESHACER</b>";
            $("#SioNO").dialog("open");
            document.getElementById("btnSI").onclick = function () {

                $("#SioNO").dialog("close");
                var lstRow = grid.getRowData(grid.getGridParam("selrow"));
                delContacto(lstRow.CONTACTO_ID);//remueve registro
                grid.delRowData(grid.getGridParam("selrow"));

            };

            document.getElementById("btnNO").onclick = function () {

                $("#SioNO").dialog("close");

            };

        } else {

            alert("Selecciona un contacto");

        }

    }

    if (opc == 2) {
        grid = jQuery("#GRD_RZN");
        if (grid.getGridParam("selrow") != null) {
            grid.delRowData(grid.getGridParam("selrow"));
        } else {
            alert("Selecciona una razón social");
        }
    }
}
function LimpiarGrid(opc) {
    if (opc == 2) {
        document.getElementById("CT_ADDRZN").value = "";
    }
}
function saveDetaTMK() {
    var strFecha = document.getElementById("CT_FECHA").value;
//    var bolDuplicado = document.getElementById("CT_DUPLICADO").value;
    strFecha = trim(strFecha);
    strFecha = strFecha.substring(6, 10) + strFecha.substring(3, 5) + strFecha.substring(0, 2);
//    if (bolDuplicado != "1") {
    if (intDuplicado1 != "1" && intDuplicado2 != "1") {
        if (document.getElementById("CT_COMENTARIOS").value != "") {
            if (document.getElementById("CT_FECHA").value != "" && document.getElementById("CT_HORA").value != "") {

                var strPost = "";
                strPost += "CT_ID=" + document.getElementById("CT_ID").value;
                strPost += "&CT_NO_CLIENTE=" + document.getElementById("CT_ID").value;
                strPost += "&CT_RAZONSOCIAL=" + encodeURIComponent(document.getElementById("CT_RAZONSOCIAL").value);
                strPost += "&CT_RFC=" + encodeURIComponent(document.getElementById("CT_RFC").value);
                strPost += "&CT_SEDE=" + document.getElementById("CT_SEDE").value;
                strPost += "&CT_CONTACTO=" + document.getElementById("CT_CONTACTO").value;
                strPost += "&CT_CONTACTO2=" + document.getElementById("CT_CONTACTO2").value;
                strPost += "&CT_CORREO=" + document.getElementById("CT_CORREO").value;
                strPost += "&CT_CORREO2=" + document.getElementById("CT_CORREO2").value;
                strPost += "&CT_BOLBASE=" + document.getElementById("CT_BOLBASE").value;
                strPost += "&CT_FECHA=" + strFecha;
                strPost += "&CT_HORA=" + document.getElementById("CT_HORA").value;
                strPost += "&CT_COMENTARIOS=" + document.getElementById("CT_COMENTARIOS").value;
                strPost += "&CT_GIRO=" + document.getElementById("CT_GIRO").value;
                strPost += "&CT_AREA=" + document.getElementById("CT_AREA").value;
                strPost += "&CT_CP=" + document.getElementById("CT_CP").value;
                strPost += "&CT_CALLE=" + encodeURIComponent(document.getElementById("CT_CALLE").value);
                strPost += "&CT_COL=" + document.getElementById("CT_COL").value;
                strPost += "&CT_NUM=" + document.getElementById("CT_NUM").value;
                strPost += "&CT_NOMBRE=" + document.getElementById("CT_CONTACTO_ENTRADA").value;
                strPost += "&CT_CONMUTADOR=" + document.getElementById("CT_CONMUTADOR").value;
                var intMailMes = 0;
                if (document.getElementById("CT_MAILMES").checked == true) {
                    intMailMes = 1;
                }
                var intAAA = 0;

                if (document.getElementById("CT_AAA") != null) {
                    if (document.getElementById("CT_AAA").checked) {
                        intAAA = 1;
                    }
                }


                if (document.getElementById("CT_NOMBRE_INVITO") != null) { //almacena los motivos de porque no compra
                    strPost += "&motivo=" + document.getElementById("CT_NOMBRE_INVITO").value;
                }

                strPost += "&CT_AAA=" + intAAA;
                strPost += "&CT_MAILMES=" + intMailMes;
                strPost += "&strCodigoBase=" + document.getElementById("CT_CLAVE_DDBB").value;
                strPost += "&mediopublicidad=" + document.getElementById("CT_MKT").value;
                $("#dialogWait").dialog("open");
                $.ajax({
                    type: "POST",
                    data: encodeURI(strPost),
                    scriptCharset: "UTF-8",
                    contentType: "application/x-www-form-urlencoded;charset=utf-8",
                    cache: false,
                    dataType: "html",
                    url: "COFIDE_Telemarketing.jsp?ID=4",
                    success: function (dato) {
                        dato = trim(dato);
                        if (Left(dato, 2) == "OK") {
                            guardarContactos(dato.replace("OK", ""), 0); //0 es igual a proceso normal, 1 = es uddate
                            ActualizaRzn(document.getElementById("CT_ID").value);
                        } else {
                            alert(dato);
                        }
                        $("#dialogWait").dialog("close");
                    }});
            } else {
                alert("Es necesario fijar una fecha y una hora");
                document.getElementById("CT_FECHA").focus();
            }
        } else {
            alert("No Olvides Dejar tu Comentario");
            document.getElementById("CT_COMENTARIOS").focus();
        }
        intDuplicado1 = "0";
        intDuplicado2 = "0";
    } else {
        alert("Se detecto un conflicto, revisar información");
    }
}
function guardarContactos(idCliente, update) {
    var grid = jQuery("#GRIDCONTACTOS");
    var idArr = grid.getDataIDs();
    var strPost = "";
    strPost += "CT_ID=" + idCliente;
    for (var i = 0; i < idArr.length; i++) {
        var id = idArr[i];
        var lstRow = grid.getRowData(id);
        strPost += "&CCO_NOMBRE" + i + "=" + lstRow.CCO_NOMBRE + "";
        strPost += "&CCO_APPATERNO" + i + "=" + lstRow.CCO_APPATERNO + "";
        strPost += "&CCO_APMATERNO" + i + "=" + lstRow.CCO_APMATERNO + "";
        strPost += "&CCO_TITULO" + i + "=" + lstRow.CCO_TITULO + "";
        strPost += "&CCO_NOSOCIO" + i + "=" + lstRow.CCO_NOSOCIO + "";
        strPost += "&CCO_AREA" + i + "=" + lstRow.CCO_AREA + "";
        strPost += "&CCO_ASOCIACION" + i + "=" + lstRow.CCO_ASOCIACION + "";
        strPost += "&CCO_CORREO_" + i + "=" + lstRow.CCO_CORREO + "";
        strPost += "&CCO_CORREO2_" + i + "=" + lstRow.CCO_CORREO2 + "";
        strPost += "&CCO_TELEFONO" + i + "=" + lstRow.CCO_TELEFONO + "";
        strPost += "&CCO_EXTENCION" + i + "=" + lstRow.CCO_EXTENCION + "";
        strPost += "&CCO_ALTERNO" + i + "=" + lstRow.CCO_ALTERNO + "";
        strPost += "&CT_MAILMES1" + i + "=" + lstRow.CT_MAILMES1 + "";
        strPost += "&CT_MAILMES2" + i + "=" + lstRow.CT_MAILMES2 + "";
        strPost += "&CONTACTO_ID" + i + "=" + lstRow.CONTACTO_ID + "";
        console.log(lstRow.CT_MAILMES1 + " mail1");
        console.log(lstRow.CT_MAILMES2 + " mail2");
    }
    strPost += "&length_contactos=" + idArr.length;
    $.ajax({
        type: "POST",
        data: strPost,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "html",
        url: "COFIDE_Telemarketing.jsp?ID=5",
        success: function (datos) {
            datos = trim(datos);
            if (Left(datos, 2) == "OK") {
                if (update != 1) { //aqui cargara si es o no actualizacion = actualizacion = 1
                    consultaVta();
                    jQuery("#GRIDCONTACTOS").clearGridData();
                } else {
                    consultaVta(idCliente);
                }
            } else {
                alert(datos);
            }
        }, error: function (objeto, quepaso, otroobj) {
            alert(":pto=5: " + objeto + " " + quepaso + " " + otroobj);
        }});
//    }
}
function OpnDiagHCall() {
    var strCte = document.getElementById("CT_NO_CLIENTE").value;
    Abrir_Link("COFIDE_Historial_llamadas.jsp?CT_ID= " + strCte, "_blank", 1000, 600, 0, 0);
}
function OpnDiagHVenta() {
    var objSecModiVta = objMap.getScreen("HVENTA");
    if (objSecModiVta != null) {
        objSecModiVta.bolActivo = false;
        objSecModiVta.bolMain = false;
        objSecModiVta.bolInit = false;
        objSecModiVta.idOperAct = 0;
    }
    OpnOpt("HVENTA", "_ed", "dialogCte", false, false, true);
}
function OpnDiagNVenta(opc) {
    if (opc == "0") {
        document.getElementById("CT_PROMOSION").value = "0";
        document.getElementById("CT_CONTACTO_ID").value = "0";
    }
//limpia los campos para la nueva venta
    document.getElementById("CT_ID_FACTKT").value = ""; // se guarda el id en un hidden           
    document.getElementById("CT_TIPODOC").value = ""; // se guarda el id en un hidden  
    document.getElementById("CT_ESTATUS_VTA").value = ""; // se guarda el estatus de la venta
    document.getElementById("CT_GRID").value = "3"; // para mostrar el boton de guardar venta en general   
    var objSecModiVta = objMap.getScreen("NVENTA2");
    if (objSecModiVta != null) {
        objSecModiVta.bolActivo = false;
        objSecModiVta.bolMain = false;
        objSecModiVta.bolInit = false;
        objSecModiVta.idOperAct = 0;
    }
    OpnOpt("NVENTA2", "_ed", "dialog", false, false, true);
}
function OpnDiagPLlamada() {
    var objSecModiVta = objMap.getScreen("P_LLAMADA");
    if (objSecModiVta != null) {
        objSecModiVta.bolActivo = false;
        objSecModiVta.bolMain = false;
        objSecModiVta.bolInit = false;
        objSecModiVta.idOperAct = 0;
    }
    OpnOpt("P_LLAMADA", "_ed", "dialogTMK1", false, false, true);
}
function OpnDiagCActivo() {
//    Abrir_Link("COFIDE_Calendario_cursos.jsp", "_blank", 800, 600, 0, 0);
    window.open("COFIDE_Calendario_cursos.jsp", '_blank');
}
function DelCteBase() {
    var strCT_ID = document.getElementById("CT_ID").value;
    if (strCT_ID != 0) {
        var strPost = "";
        strPost += "&CT_ID=" + strCT_ID;
        $.ajax({type: "POST", data: strPost, scriptCharset: "UTF-8", contentType: "application/x-www-form-urlencoded;charset=utf-8", cache: false, dataType: "xml", url: "COFIDE_Telemarketing.jsp?ID=6"});
        consultaVta();
    } else {
        alert("No Hay Registro Para Descartar");
    }
}
function ValidaHora() {
    var strHora = document.getElementById("CT_HORA").value;
    strHora = trim(strHora);
    if (Left(strHora, 2) > 25) {
        alert("Solo son 24 hrs");
        document.getElementById("CT_HORA").value = "";
    }
    if (Right(strHora, 2) > 60) {
        alert("el rango es de 0 a 60 minutos");
        document.getElementById("CT_HORA").value = "";
    }
}
function LlamadaOK() {
    document.getElementById("CT_RESPOND").value = ""; //vuelve a marcar y regresa la respuesta en "" y activa el timer para leerlo de nuevo
    var intNo_CTE = document.getElementById("CT_ID").value;
    var bolBase = 0;
    if (document.getElementById("CT_BOLBASE").value == "true") {
        bolBase = 1;
    }
    var strComentario = document.getElementById("CT_COMENTARIOS").value;
    var strContacto = document.getElementById("CT_CONTACTO").value;
    var strCall = document.getElementById("CT_BOLCALL").value;
    var strExito = 0;
    var strDesc = 0;
    if (strCall == "1") {
        strExito = 1;
    } else {
        strDesc = 1;
    }
    var strPost = "";
    if (intNo_CTE == "") {
        intNo_CTE = 0;
    }
    strPost += "CT_ID=" + intNo_CTE;
    strPost += "&CT_BOLBASE=" + bolBase;
    strPost += "&CT_COMENTARIOS=" + strComentario;
    strPost += "&CT_CONTACTO=" + strContacto;
    strPost += "&exito=" + strExito;
    strPost += "&descartado=" + strDesc;
    strPost += "&HoraIni=" + document.getElementById("CT_HORA_INI").value;
    if (strContacto != "") {
        $.ajax({
            type: "POST",
            data: strPost,
            scriptCharset: "UTF-8",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "xml",
            url: "COFIDE_Telemarketing.jsp?ID=3",
            success: function (datos) {
                var lstXml = datos.getElementsByTagName("vta")[0];
                var lstCte = lstXml.getElementsByTagName("datos");
                for (var i = 0; i < lstCte.length; i++) {
                    var objcte = lstCte[i];
                    document.getElementById("CT_ID_LLAMADA").value = objcte.getAttribute("id_llamada"); //recupera el ID de la llamada nueva
                }
                clearTimeout(timTimer);
                Timer(); //valida la llamda actual si ya termino
            }
        });
    } else {
        alert("Asigna un numero de contacto...");
    }
}
function mostrarHrs() {
    var strFecha = document.getElementById("CT_FECHA").value;
    var strPost = "CT_FECHA=" + strFecha;
    $("#dialogWait").dialog("open");
    $.ajax({
        type: "POST",
        data: strPost,
        scriptCharset: "UTF-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "COFIDE_Telemarketing.jsp?ID=7",
        success: function (datos) {
            var objHoraCombo = document.getElementById("CT_HORA");
            select_clear(objHoraCombo);
            var lstXml = datos.getElementsByTagName("horas")[0];
            var lstCte = lstXml.getElementsByTagName("hora");
            for (var i = 0; i < lstCte.length; i++) {
                var objHora = lstCte[i];
                select_add(objHoraCombo, objHora.getAttribute("valor"), objHora.getAttribute("valor"));
            }
            $("#dialogWait").dialog("close");
        }});
}
function OpnSearchCustomer() {
    var objSecModiVta = objMap.getScreen("TELE_CTE");
    if (objSecModiVta != null) {
        objSecModiVta.bolActivo = false;
        objSecModiVta.bolMain = false;
        objSecModiVta.bolInit = false;
        objSecModiVta.idOperAct = 0;
    }
    OpnOpt("TELE_CTE", "grid", "dialogCte", false, false, true);
}
function OpnSearchCustomer2() {
    var objSecModiVta = objMap.getScreen("EXCTE_TMK");
    if (objSecModiVta != null) {
        objSecModiVta.bolActivo = false;
        objSecModiVta.bolMain = false;
        objSecModiVta.bolInit = false;
        objSecModiVta.idOperAct = 0;
    }
    OpnOpt("EXCTE_TMK", "grid", "dialogCte", false, false, true);
}
function OpnChangeCall() {
    if (document.getElementById("CT_NO_CLIENTE").value != "0") {
        intContaPendiente++;
        var objCte = new CtePendiente(
                document.getElementById("CT_NO_CLIENTE").value,
                document.getElementById("CT_RAZONSOCIAL").value,
                document.getElementById("CT_RFC").value,
                document.getElementById("CT_SEDE").value,
                document.getElementById("CT_GIRO").value,
                document.getElementById("CT_CONTACTO").value,
                document.getElementById("CT_AREA").value,
                document.getElementById("CT_CONMUTADOR").value,
                document.getElementById("CT_CORREO").value,
                document.getElementById("CT_CORREO2").value
                );
        lstElemPendiente[intContaPendiente] = objCte;
        document.getElementById("CT_NO_CLIENTE").value = "0";
        document.getElementById("CT_RAZONSOCIAL").value = "";
        document.getElementById("CT_RFC").value = "";
        document.getElementById("CT_SEDE").value = "";
        document.getElementById("CT_GIRO").value = "";
        document.getElementById("CT_CONTACTO").value = "";
        document.getElementById("CT_AREA").value = "";
        document.getElementById("CT_CONMUTADOR").value = "";
        document.getElementById("CT_CORREO").value = "";
        document.getElementById("CT_CORREO2").value = "";
        _drawPendientes();
    }
}
function _drawPendientes() {
    var strRazonSocial = "";
    if (document.getElementById("CT_RAZONSOCIAL").value != "") {

        strRazonSocial = document.getElementById("CT_RAZONSOCIAL").value;

    } else {

        strRazonSocial = "-";

    }
    var strHtml = "<table>";
    strHtml += "<tr>";
    strHtml += "<td><a href=\"javascript:OpnCtePendiente()\">" + strRazonSocial + "</a></td>";
    strHtml += "</tr>";
    strHtml += "</table>";
    document.getElementById("seccion_pendiente").innerHTML = strHtml;
}
function CtePendiente(id, nombre, rfc, sede, giro, contacto, area, conmutador, correo, correo2) {
    this.id = id;
    this.nombre = nombre;
    this.rfc = rfc;
    this.sede = sede;
    this.giro = giro;
    this.contacto = contacto;
    this.area = area;
    this.conmutador = conmutador;
    this.correo = correo;
    this.correo2 = correo2;
}
function drawRightPanel() {
    var divRightPanel = document.getElementById("rightPanel");
    var strHTML = "<table border='0' class='cofide_dashboard' align='center'>";
    strHTML += "<tr>";
    strHTML += '<td valign="top" style="font-size:14px; width:240px;" title="Llamadas Realizadas">';
    strHTML += "Llamadas realizadas:";
    strHTML += "<div id='conteo_llamadas' align='center'>0</div>";
    strHTML += "</td>";
    strHTML += '<td valign="top" style="font-size:14px; width:240px" title="Sesiones Pendientes">';
    strHTML += "Sesiones pendientes:";
    strHTML += "<div id='seccion_pendiente' style='font-size: 22px; padding-left: 5px;'><a href=\"javascript:OpnCtePendiente()\">Ninguna</a></div>";
    strHTML += "</td>";
    strHTML += '<td valign="top" style="font-size:18px; width:160px" title="Meta de Nuevos"><b>';
    strHTML += "Meta nuevos:";
    strHTML += "<div id='seccion_nvos_meta'>0</div>"; //meta nuevos registros
    strHTML += "</b></td>";
    strHTML += '<td valign="top" style="font-size:14px; width:110px" title="Nuevos">';
    strHTML += "Nuevos:";
    strHTML += "<div id='seccion_nvos'>0</div>"; //nuevos registros
    strHTML += "</td>";
    strHTML += '<td valign="top" style="font-size:17px; width:230px" title="Meta de Venta"><b>';
    strHTML += "<i class='fa fa-star-o'></i>";
    strHTML += "<div id='seccion_monto_vtas_meta'>0.0</div>"; //monto meta venta
    strHTML += "</b></td>";
    strHTML += '<td valign="top" style="font-size:14px; width:200px" title="Monto Vendido">';
    strHTML += "<i class='fa fa-shopping-cart'></i>";
    strHTML += "<div id='seccion_monto_vtas'>0.0</div>"; //monto de venta
    strHTML += "</td>";
    strHTML += '<td valign="top" style="font-size:14px; width:180px" title="Monto Cobrado">';
    strHTML += "<i class='fa fa-usd'></i>";
    strHTML += "<div id='seccion_monto_cobrado'>0.0</div>"; //monto cobrado
    strHTML += "</td>";
    strHTML += "</td>";
    strHTML += '<td valign="top" style="font-size:14px; width:180px" title="Monto Facturado">';
    strHTML += "<i class='fa fa-file-text-o '></i>";
    strHTML += "<div id='seccion_monto_facturado'>0.0</div>"; //monto cobrado
    strHTML += "</td>";
    strHTML += '<td valign="top" style="font-size:14px; width:200px" title="Usuario en Sesión">';
    strHTML += "Usuario actual:";
    strHTML += "<div id='seccion_user_actual'>" + strUserName + "</div>";
    strHTML += "</td>";
    strHTML += '<td valign="top" style="font-size:14px; width:180px" title="Fecha del día de hoy">';
    strHTML += "Fecha actual:";
    strHTML += "<div id='seccion_fecha_actual'>" + strHoyFecha + "</div>";
    strHTML += "</td>";
    strHTML += "</tr>";
    strHTML += "</table>";
    divRightPanel.innerHTML = strHTML;
    getIndicadores();
}
function getIndicadores() {
    $("#dialogWait").dialog("open");
    $.ajax({
        type: "POST",
        data: "",
        scriptCharset: "UTF-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "COFIDE_Telemarketing.jsp?ID=8",
        success: function (datos) {
            var lstXml = datos.getElementsByTagName("indicadores")[0];
            var lstCte = lstXml.getElementsByTagName("Indicador");
            for (var i = 0; i < lstCte.length; i++) {
                var objIndicador = lstCte[i];
                /**
                 * seccion_nvos = seccion de cuantos registros nuevos
                 * seccion_monto_vtas = cantidad $ de ventas que se llevan al momento
                 * seccion_monto_cobrado = cantidad $ cobrado
                 * seccion_monto_facturado = cantidad facturado
                 * seccion_nvos_meta = nueva meta de nuevos registros
                 * seccion_monto_vtas_meta = meta de ventas actual
                 * seccion_pendiente = registros pendientes que mandaron a pausa
                 */
                document.getElementById("seccion_pendiente").innerHTML = '<a href=\"javascript:OpnCtePendiente()\">' + objIndicador.getAttribute("pendiente") + '</a>'
                document.getElementById("conteo_llamadas").innerHTML = objIndicador.getAttribute("llamadas");
                document.getElementById("seccion_nvos").innerHTML = objIndicador.getAttribute("nuevo");
                document.getElementById("seccion_monto_vtas").innerHTML = "$ " + FormatNumber(objIndicador.getAttribute("monto"), 2, true, false, true);
                document.getElementById("seccion_monto_cobrado").innerHTML = "$ " + FormatNumber(objIndicador.getAttribute("cobrado"), 2, true, false, true);
                document.getElementById("seccion_monto_facturado").innerHTML = "$ " + FormatNumber(objIndicador.getAttribute("monto_factura"), 2, true, false, true);
                document.getElementById("seccion_nvos_meta").innerHTML = objIndicador.getAttribute("nuevo_meta");
                document.getElementById("seccion_monto_vtas_meta").innerHTML = "$ " + FormatNumber(objIndicador.getAttribute("monto_meta"), 2, true, false, true);
            }
            $("#dialogWait").dialog("close");
            saveCursoSugerido("");
        }});
}
function OpnPausarLlamada() {

    $("#dialog2").dialog({
        dialogClass: "no-close"
    });
//    document.getElementById("CT_ID_PAUSA").value = "";
//    var strHtml = "<table border= 0>";
//    strHtml += "<tr>";
//    strHtml += "<td colspan='5'>Especifique el motivo de la pausa";
//    strHtml += "</td>";
//    strHtml += "</tr>";
//    strHtml += "<tr>";
////    strHtml += "<td><input type='radio' name='tipo_paus' value ='1' id='tipo_pausa1' checked>Capacitación</td>";
//    strHtml += "<td><input type='radio' name='tipo_paus' value ='2' id='tipo_pausa2'>Admin</td>";
//    strHtml += "<td><input type='radio' name='tipo_paus' value ='3' id='tipo_pausa3'>Sanitario</td>";
//    strHtml += "<td><input type='radio' name='tipo_paus' value ='4' id='tipo_pausa4'>Comida</td>";
//    strHtml += "</tr>";
//    strHtml += "<tr>";
//    strHtml += "<td colspan='2'><input type=button name='pausar_llamada' id='pausar_llamada' onclick='comienzaPausaLlamada()' value='Comenzar'>";
//    strHtml += "<td colspan='2'><input type=button name='termina_pausar_llamada' id='termina_pausar_llamada' onclick='terminaPausaLlamada()' value='Terminar'>";
//    strHtml += "</td>";
//    strHtml += "</tr>";
//    strHtml += "</table>";
//    document.getElementById("dialog2_inside").innerHTML = strHtml;
//    $("#dialog2").dialog("option", "title", "Pausa");
//    $("#dialog2").dialog("open");
    comienzaPausaLlamada(); //inicia la pausa al abrir la ventana
    document.getElementById("CT_ID_PAUSA").value = "";
    var strHtml = "<table border= 0 style='text-align: center'>";
    strHtml += "<tr>";
    strHtml += "<td colspan='5'>COMENZÓ LA PAUSA";
    strHtml += "</td>";
    strHtml += "</tr>";
    strHtml += "<tr>";
    strHtml += "<td>";
    strHtml += "<img src='images/ptovta/pause-ajax.gif'/>";
    strHtml += "</td>";
    strHtml += "</tr>";
    strHtml += "<tr>";
    strHtml += "<td colspan='2'><input type=button name='termina_pausar_llamada' id='termina_pausar_llamada' onclick='terminaPausaLlamada()' value='TERMINAR PAUSA'>";
    strHtml += "</td>";
    strHtml += "</tr>";
    strHtml += "</table>";
    document.getElementById("dialog2_inside").innerHTML = strHtml;
    $("#dialog2").dialog("option", "title", "PAUSA");
    $("#dialog2").dialog("open");

}
function comienzaPausaLlamada() {
//    if (document.getElementById("tipo_pausa2").checked || document.getElementById("tipo_pausa3").checked || document.getElementById("tipo_pausa4").checked) {
//        document.getElementById("pausar_llamada").style.display = "none";
    document.getElementById("CT_ESTATUS").value = "PAUSA";
    TiempoPausa("PAUSA");
//    } else {
//        alert("Selecciona un mótivo de pausa.");
//    }
}
function terminaPausaLlamada() {
    alert("Termina la pausa");
    document.getElementById("CT_ESTATUS").value = "DISPONIBLE";
    TiempoPausa("DISPONIBLE");
    $("#dialog2").dialog("close");
}
function OpnEdoCtaCte() {
    var objSecModiVta = objMap.getScreen("VTAS_VIEW");
    if (objSecModiVta != null) {
        objSecModiVta.bolActivo = false;
        objSecModiVta.bolMain = false;
        objSecModiVta.bolInit = false;
        objSecModiVta.idOperAct = 0;
    }
    OpnOpt("VTAS_VIEW", "_ed", "dialogCte", false, false, true);
}
function OpnSalirLlamada() {
    limpiarTodo();
}
function OpnNewCte() {
    IsInBound();
    var objSecModiVta = objMap.getScreen("N_PROSP");
    if (objSecModiVta != null) {
        objSecModiVta.bolActivo = false;
        objSecModiVta.bolMain = false;
        objSecModiVta.bolInit = false;
        objSecModiVta.idOperAct = 0;
    }
    OpnOpt("N_PROSP", "_ed", "dialogCte", false, false, true);
}
function NewProspecto() {
    var strPost = "";
    var strRazon = document.getElementById("NCT_RAZONSOCIAL").value;
    var strEmail = document.getElementById("NCT_EMAIL1").value;
    var strNumero = document.getElementById("NCT_NUMERO").value;
    var strMedio = document.getElementById("NCT_MEDIO").value;
    var bolSave = false; //si es inbound debe de guardar el campo, si no, no lo exige
    if (document.getElementById("CT_CLAVE_DDBB").value == "BE-INBOUND") {
        if (strMedio != "0") {
            bolSave = true;
        } else {
            alert("No olvides capturar el medio por el cual se entero del evento");
        }
    } else {
        bolSave = true;
    }
    if (strRazon != "" && (strEmail != "" || strNumero != "") && bolSave) {
        strPost += "NCT_RAZONSOCIAL=" + strRazon;
        strPost += "&NCT_EMAIL1=" + strEmail;
        strPost += "&NCT_NUMERO=" + strNumero;
        strPost += "&NCT_MEDIO=" + strMedio;
        $.ajax({
            type: "POST",
            data: strPost,
            scriptCharset: "UTF-8",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "xml",
            url: "COFIDE_Telemarketing.jsp?ID=11",
            success: function (datos) {
                var lstXml = datos.getElementsByTagName("vta")[0];
                var lstCte = lstXml.getElementsByTagName("datos");
                for (var i = 0; i < lstCte.length; i++) {
                    var objcte = lstCte[i];
                    document.getElementById("NCT_ID").value = objcte.getAttribute("NCT_ID");
                }
                var intId = document.getElementById("NCT_ID").value;
                document.getElementById("CT_NO_CLIENTE").value = intId;
                document.getElementById("CT_RAZONSOCIAL").value = strRazon;
                document.getElementById("CT_CONTACTO").value = strNumero;
                document.getElementById("CT_CORREO").value = strEmail;
                consultaVta(intId);
                $("#dialogCte").dialog("close");
            }});
    } else {
        alert("Ingresa la razón social con un correo o teléfono");
    }
}
function Validafecha() {
    var strFecha = document.getElementById("CT_FECHA").value;
    var strPost = "FECHA=" + strFecha;
    var resp = "";
    var FechaActual = "";
    $.ajax({
        type: "POST",
        data: strPost,
        scriptCharset: "UTF-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "COFIDE_Telemarketing.jsp?ID=13",
        success: function (datos) {
            var lstXml = datos.getElementsByTagName("vta")[0];
            var lstCte = lstXml.getElementsByTagName("datos");
            for (var i = 0; i < lstCte.length; i++) {
                var objcte = lstCte[i];
                resp = objcte.getAttribute("ok");
                FechaActual = objcte.getAttribute("FechaDiagonal");
                if (resp == "ok") {
                    mostrarHrs();
                } else {
                    alert("La Fecha debe de ser posterior al día de hoy");
                    document.getElementById("CT_FECHA").value = FechaActual;
                }
            }
        }});
}
function llenarColonia() {
    var strCp = document.getElementById("CT_CP").value;
    var objColoniaCombo = document.getElementById("CT_COL");
    if (strCp != "") {
        var strPost = "CT_CP=" + strCp;
//        $("#dialogWait").dialog("open");
        $.ajax({
            type: "POST",
            data: strPost,
            scriptCharset: "UTF-8",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "xml",
            url: "COFIDE_Telemarketing.jsp?ID=14",
            success: function (datos) {
                select_clear(objColoniaCombo);
                var lstXml = datos.getElementsByTagName("General")[0];
                var lstCte = datos.getElementsByTagName("Colonia");
                for (var i = 0; i < lstCte.length; i++) {
                    var objColonia = lstCte[i];
                    select_add(objColoniaCombo, objColonia.getAttribute("CMX_COLONIA"), objColonia.getAttribute("CMX_COLONIA"));
                }
                if (objColonia != document.getElementById("CT_COLONIA_DB").value) {
                    select_add(objColoniaCombo, document.getElementById("CT_COLONIA_DB").value, document.getElementById("CT_COLONIA_DB").value);
                }
                document.getElementById("CT_EDO").value = lstXml.getAttribute("CMX_ESTADO");
                document.getElementById("CT_MUNI").value = lstXml.getAttribute("CMX_MUNICIPIO");
//                $("#dialogWait").dialog("close");
            }});
    } else {
        select_clear(objColoniaCombo);
    }
}
function RevizaFinLlamada() {
//    alert("vamo a revizar el fin de la llamada");
    var bolAlert = false;
    var strStat = document.getElementById("CT_ESTATUS").value;
    if (strStat == "DISPONIBLE") {
        var intIdLlamada = document.getElementById("CT_ID_LLAMADA").value;
        var strRespuesta = "";
        if (intIdLlamada != "") {
            var strPost = "id_llamada=" + intIdLlamada;
            $.ajax({
                type: "POST",
                data: strPost,
                scriptCharset: "UTF-8",
                contentType: "application/x-www-form-urlencoded;charset=utf-8",
                cache: false,
                dataType: "html",
                url: "COFIDE_Telemarketing.jsp?ID=15",
                success: function (datos) {
                    datos = trim(datos);
                    strRespuesta = datos;
                    if (strRespuesta != "") {
                        document.getElementById("CT_RESPOND").value = "OK";
//                        alert("Terminó llamada");
                        Timer();
                    } else {
                        Timer();
                    }
//                    var lstXml = datos.getElementsByTagName("vta")[0];
//                    var lstCte = lstXml.getElementsByTagName("datos");
//                    for (var i = 0; i < lstCte.length; i++) {
//                        var objcte = lstCte[i];
//                        strRespuesta = objcte.getAttribute("idpbx");
//                        if (strRespuesta != "") {
//                            document.getElementById("CT_RESPOND").value = "OK";
//                            alert("Terminó llamada");
//                            Timer();
//                        } else {
//                            Timer();
//                        }
//                    }
                },
                error: function (objeto, quepaso, otroobj) {
                    alert(":pto=15: " + objeto + " " + quepaso + " " + otroobj);
                }
            });
        }
    } else {
        Timer();
    }
}
/**
 * 
 * @returns {none}
 * lee si, ya colgo o no la llamada CT_RESPOND
 * lee si, esta disponible el ejecutivo CT_ESTATUS
 * si esta el CT_ESTATUS en DISPONIBLE, y el CT_RESPOND en OK, manda el aviso al supervisor, si no, vuelve a revizar el estatus de la llamada
 * 
 */
function Timer() {
//    alert("se activó el timer");
    var strResp = document.getElementById("CT_RESPOND").value;
    var strStat = document.getElementById("CT_ESTATUS").value;
    if (strResp == "OK" && strStat == "DISPONIBLE") {
//        EnvioCorreoSuper();
//        timTimer = setTimeout("EnvioCorreoSuper()", 300000);
        timTimer = setTimeout("EnvioCorreoSuper()", 480000);
    } else {
//        RevizaFinLlamada();
        timTimer = setTimeout('RevizaFinLlamada()', 30000);
    }
}
function EnvioCorreoSuper() {
//    alert("entro en el envio de notificacion");
    var strResp = document.getElementById("CT_RESPOND").value;
    var strStat = document.getElementById("CT_ESTATUS").value;
    if (strResp == "OK" && strStat == "DISPONIBLE") {
        alert("No Olvides Guardar Tu Registro");
        $.ajax({
            type: "POST",
            data: "",
            scriptCharset: "UTF-8",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "html",
            url: "COFIDE_Telemarketing.jsp?ID=16",
            success: function (datos) {
//                datos = trim(datos);
//                if (Left(datos, 2) == "OK") {
                Timer();
//                } else {
//                    alert(datos);
//                }
            }, error: function (objeto, quepaso, otroobj) {
                alert(":pto=16: " + objeto + " " + quepaso + " " + otroobj);
            }
        });
    } else {
        Timer();
    }
}
function MonitorioAgentes() {
//    var strRespuesta = "";
//    var strAgentes = "";
//    var intSupervisor = "";
//    $.ajax({
//        type: "POST",
//        data: "",
//        scriptCharset: "UTF-8",
//        contentType: "application/x-www-form-urlencoded;charset=utf-8",
//        cache: false,
//        dataType: "xml",
//        url: "COFIDE_Telemarketing.jsp?ID=17",
//        success: function (datos) {
//            var lstXml = datos.getElementsByTagName("vta")[0];
//            var lstCte = lstXml.getElementsByTagName("datos");
//            for (var i = 0; i < lstCte.length; i++) {
//                var objcte = lstCte[i];
//                strRespuesta = objcte.getAttribute("libres");
//                strAgentes = objcte.getAttribute("agente");
//                intSupervisor = objcte.getAttribute("supervisor");
//                if (strRespuesta == "OK") {
//                    if (intSupervisor == 1) { //unicamente si es supervisor
//                        setTimeout("MonitorioAgentes()", 50000);
//                    }
//                    if (strAgentes != "") {
//                        alert("Alerta!! ejecutivo sin llamada! \n" + strAgentes);
//                    }
//                } else {
//                }
//            }
//        }});
}

function LoadContacto(intIdCte, opc) {
    jQuery("#GRIDCONTACTOS").clearGridData();
    var strParticipantes = [];
    var intPuntos = 0;
    var strPuntosSRC = "";
    var intIterar = 0;
    var enviado = "";
    if (intIdCte > 0) {
        var strPost = "CT_ID=" + intIdCte;
        $.ajax({
            type: "POST",
            data: strPost,
            scriptCharset: "UTF-8",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "xml",
            url: "COFIDE_Telemarketing.jsp?ID=18",
            success: function (datos) {
                var lstXml = datos.getElementsByTagName("vta")[0];
                var lstCte = lstXml.getElementsByTagName("datos");
                for (var i = 0; i < lstCte.length; i++) {
                    var objcte = lstCte[i];
                    intPuntos = parseInt(objcte.getAttribute("CCO_PUNTOS"));

                    if (objcte.getAttribute("enviado") == "1") {
                        enviado = "\t<i title='Enviadó' style=\"color: #99cc00\" class=\"fa fa-check\" ></i>";
                    } else {
                        enviado = "";
                    }

                    if (intPuntos >= 1000) {
                        strPuntosSRC = "<div style=\"text-align:center;\"><a href=\"javascript:opnPromosion(" + objcte.getAttribute("CONTACTO_ID") + ")\"<i title='Promoción' class=\"fa fa-bullseye\" >" + intPuntos + "</i>" + enviado + "</a></div>";
                        strParticipantes[intIterar] = objcte.getAttribute("CCO_NOMBRE") + " " + objcte.getAttribute("CCO_APPATERNO") + " " + objcte.getAttribute("CCO_APMATERNO") + " / " + intPuntos + " pts.";
                        intIterar++;
                    } else {
                        strPuntosSRC = "<div style=\"text-align:center\"><i title='No aplica Promoción'>\t" + intPuntos + "</i>" + enviado + "</div>";
                    }
                    var strTel = "";
                    var strTelAlterno = "";
                    if (objcte.getAttribute("CCO_TELEFONO") != "") {
                        strTel = "<b><a style=\"color:#0B610B\" href=\"javascript:LlamarContacto_('" + objcte.getAttribute("CCO_TELEFONO") + "')\"<i class=\"fa fa-headphones\">\t" + objcte.getAttribute("CCO_TELEFONO") + "</i></a></b>";
                    }
                    if (objcte.getAttribute("CCO_ALTERNO") != "") {
                        strTelAlterno = "<b><a style=\"color:#0B610B\" href=\"javascript:LlamarContacto_('" + objcte.getAttribute("CCO_ALTERNO") + "')\"<i class=\"fa fa-headphones\">\t" + objcte.getAttribute("CCO_ALTERNO") + "</i></a></b>";
                    }
                    var datarow = {
                        CCO_ID: objcte.getAttribute("CCO_ID"),
                        CONTACTO_ID: objcte.getAttribute("CONTACTO_ID"), //ID PRINCIPAL
                        CCO_NOMBRE: objcte.getAttribute("CCO_NOMBRE"),
                        CCO_APPATERNO: objcte.getAttribute("CCO_APPATERNO"),
                        CCO_APMATERNO: objcte.getAttribute("CCO_APMATERNO"),
                        CCO_PUNTOS: strPuntosSRC, //PUNTOS
                        CCO_TITULO: objcte.getAttribute("CCO_TITULO"),
                        CCO_NOSOCIO: objcte.getAttribute("CCO_NOSOCIO"),
                        CCO_AREA: objcte.getAttribute("CCO_AREA"),
                        CCO_ASOCIACION: objcte.getAttribute("CCO_ASOCIACION"),
                        CCO_CORREO: objcte.getAttribute("CCO_CORREO"),
                        CCO_CORREO2: objcte.getAttribute("CCO_CORREO2"),
                        CCO_TELEFONO: objcte.getAttribute("CCO_TELEFONO"),
                        CCO_TELEFONO2: strTel,
                        CCO_EXTENCION: objcte.getAttribute("CCO_EXTENCION"),
                        CCO_ALTERNO: objcte.getAttribute("CCO_ALTERNO"),
                        CCO_ALTERNO2: strTelAlterno,
                        CT_MAILMES1: objcte.getAttribute("CT_MAILMES1"),
                        CT_MAILMES2: objcte.getAttribute("CT_MAILMES2")
                    };
                    itemIdCob++;
                    jQuery("#GRIDCONTACTOS").addRowData(itemIdCob, datarow, "last");
                }
                if (opc == "0") {
                    showGanadores(strParticipantes); //abre un popup con los participantes con puntos >= 1000
//                loadRazonSocial(intIdCte); //razones socuales
//                    loadDatosFact_(intIdCte);
                }
                loadDatosFact_(intIdCte);
            }});
    }
}
function SelectCtePro(id) {
    var strNomMain = objMap.getNomMain();
    var grid = jQuery("#TELE_CTE");
    var lstVal = grid.getRowData(id);
    if (strNomMain == "TELE_CTE") {
        OpnEdit(document.getElementById("Ed" + strNomMain));
    } else {
        if (strNomMain == "C_TELEM") {
            consultaVta(lstVal.CT_ID);
            $("#dialogCte").dialog("close");
            $("#dialogInv").dialog("close"); //cierra la ventana principal del motor
        }
    }
}
function SelectCteEx(id) {
    var strNomMain = objMap.getNomMain();
    var grid = jQuery("#EXCTE_TMK");
    var lstVal = grid.getRowData(id);
    if (strNomMain == "EXCTE_TMK") {
        OpnEdit(document.getElementById("Ed" + strNomMain));
    } else {
        if (strNomMain == "C_TELEM") {
            consultaVta(lstVal.CT_ID);
            $("#dialogCte").dialog("close");
            $("#dialogInv").dialog("close"); //cierra la ventana principal del motor
        }
    }
}
//function LlamarContacto() {
//    document.getElementById("CT_RESPOND").value = ""; //vuelve a marcar y regresa la respuesta en "" y activa el timer para leerlo de nuevo
//    var intCT_ID = document.getElementById("CT_NO_CLIENTE").value;
//    var grid = jQuery("#GRIDCONTACTOS");
//    var ids = grid.getGridParam("selrow");
//    if (ids !== null) {
//        var lstRow1 = grid.getRowData(ids);
//        var strPhone = lstRow1.CCO_TELEFONO;
//        if (strPhone != "undefined" || strPhone != "") {
//            var strPost = "";
//            strPost += "CT_TELEFONO=" + strPhone;
//            strPost += "&CT_ID=" + intCT_ID;
//            $.ajax({
//                type: "POST",
//                data: strPost,
//                scriptCharset: "UTF-8",
//                contentType: "application/x-www-form-urlencoded;charset=utf-8",
//                cache: false,
//                dataType: "xml",
//                url: "COFIDE_Telemarketing.jsp?ID=20",
//                success: function (datos) {
//                    var lstXml = datos.getElementsByTagName("vta")[0];
//                    var lstCte = lstXml.getElementsByTagName("datos");
//                    for (var i = 0; i < lstCte.length; i++) {
//                        var objcte = lstCte[i];
//                        document.getElementById("CT_ID_LLAMADA").value = objcte.getAttribute("id_llamada"); //recupera el ID de la llamada nueva
//                    }
//                    clearTimeout(timTimer);
//                    Timer(); //valida la llamda actual si ya termino
//                }
//            });
//        } else {
//            alert("el Contacto no existe!");
//        }
//    } else {
//        alert("Selecciona un contacto!");
//    }
//}
function editContacto(opc) {
    if (opc == 1) {
        tmpOperacion = "C";
        var grid = jQuery("#GRIDCONTACTOS");
        if (grid.getGridParam("selrow") != null) {
//limpia campos
            hideContactosTMK();
            //limpia campos
            document.getElementById("CCO_NOMBRE").parentNode.parentNode.style.display = "";
            document.getElementById("CCO_APPATERNO").parentNode.parentNode.style.display = "";
            document.getElementById("CCO_APMATERNO").parentNode.parentNode.style.display = "";
            document.getElementById("CCO_TITULO").parentNode.parentNode.style.display = "";
            document.getElementById("CCO_NOSOCIO").parentNode.parentNode.style.display = "";
            document.getElementById("CCO_AREA").parentNode.parentNode.style.display = "";
            document.getElementById("CCO_ASOCIACION").parentNode.parentNode.style.display = "";
            document.getElementById("CCO_CORREO").parentNode.parentNode.style.display = "";
            document.getElementById("CCO_CORREO2").parentNode.parentNode.style.display = "";
            document.getElementById("CCO_TELEFONO").parentNode.parentNode.style.display = "";
            document.getElementById("CCO_EXTENCION").parentNode.parentNode.style.display = "";
            document.getElementById("CCO_ALTERNO").parentNode.parentNode.style.display = "";
            document.getElementById("BTN_ADD").parentNode.parentNode.style.display = "";
            document.getElementById("CT_MAILMES1").parentNode.parentNode.style.display = "";
            document.getElementById("CT_MAILMES1").checked = false;
            document.getElementById("CT_MAILMES2").parentNode.parentNode.style.display = "";
            document.getElementById("CT_MAILMES2").checked = false;
            document.getElementById("BTN_CANCEL").parentNode.parentNode.style.display = "";
            document.getElementById("BTN_NEW").parentNode.parentNode.style.display = "none";
            document.getElementById("BTN_DEL").parentNode.parentNode.style.display = "none";
            document.getElementById("BTN_EDIT").parentNode.parentNode.style.display = "none";
            /**
             * limpia y muestra los campos
             */
            var lstRow = grid.getRowData(grid.getGridParam("selrow"));
            //temporales
            tmpID = lstRow.CCO_ID;
            tmpTitulo = lstRow.CCO_TITULO;
            tmpNOmbre = lstRow.CCO_NOMBRE;
            tmpAppat = lstRow.CCO_APPATERNO;
            tmpApmat = lstRow.CCO_APMATERNO;
            tmpAsoc = lstRow.CCO_ASOCIACION;
            tmpNoSocio = intDato(lstRow.CCO_NOSOCIO);
            tmpCorreo = lstRow.CCO_CORREO;
            tmpCorreo2 = lstRow.CCO_CORREO2;
            tmpArea = lstRow.CCO_AREA;
            tmpTelefono = lstRow.CCO_TELEFONO;
            tmpTelefono2 = lstRow.CCO_TELEFONO2;
            tmpExt = lstRow.CCO_EXTENCION;
            tmpAlt = lstRow.CCO_ALTERNO;
            tmpAlt2 = lstRow.CCO_ALTERNO2;
            tmpMailMes1 = lstRow.CT_MAILMES1;
            tmpMailMes2 = lstRow.CT_MAILMES2;
            tmpIDContacto = lstRow.CONTACTO_ID; //id contacto           
            tmpPuntos = lstRow.CCO_PUNTOS; //puntos            
            if (tmpMailMes1 == "SI") {
                document.getElementById("CT_MAILMES1").checked = true;
            }
            if (tmpMailMes2 == "SI") {
                document.getElementById("CT_MAILMES2").checked = true;
            }
            //edicion
            document.getElementById("CCO_TITULO").value = lstRow.CCO_TITULO;
            document.getElementById("CCO_NOMBRE").value = lstRow.CCO_NOMBRE;
            document.getElementById("CCO_APPATERNO").value = lstRow.CCO_APPATERNO;
            document.getElementById("CCO_APMATERNO").value = lstRow.CCO_APMATERNO;
            document.getElementById("CCO_ASOCIACION").value = lstRow.CCO_ASOCIACION;
            document.getElementById("CCO_NOSOCIO").value = intDato(lstRow.CCO_NOSOCIO);
            document.getElementById("CCO_CORREO").value = lstRow.CCO_CORREO;
            document.getElementById("CCO_CORREO2").value = lstRow.CCO_CORREO2;
            document.getElementById("CCO_AREA").value = lstRow.CCO_AREA;
            document.getElementById("CCO_TELEFONO").value = lstRow.CCO_TELEFONO;
            document.getElementById("CCO_EXTENCION").value = lstRow.CCO_EXTENCION;
            document.getElementById("CCO_ALTERNO").value = lstRow.CCO_ALTERNO;

            //remover
            var grid = "";
            grid = jQuery("#GRIDCONTACTOS");
            grid.delRowData(grid.getGridParam("selrow"));
            //remover

            itemIdCob++;
        } else {
            alert("Selecciona un contacto a editar");
        }
    }
    if (opc == 2) {
        var grid = jQuery("#GRD_RZN");
        if (grid.getGridParam("selrow") != null) {
            newTelem(opc);
            var lstRow = grid.getRowData(grid.getGridParam("selrow"));
            document.getElementById("CT_ADDRZN").value = lstRow.RZN_NOMBRE;
            delGridTMK(opc);
            itemIdCob++;
        }
    }
}
function sendMailGroup() {
    var strIdCte = document.getElementById("CT_ID").value;
    var strPost = "id_cte=" + strIdCte;
    $("#dialogWait").dialog("open");
    $.ajax({
        type: "POST",
        data: strPost,
        scriptCharset: "UTF-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "html",
        url: "COFIDE_Programacionmails.jsp?ID=8",
        success: function (datos) {
            datos = trim(datos);
            if (datos != "FALSE") {
                if (datos == "OK") {
                    alert("MailGroup Enviado");
                    $("#dialogWait").dialog("close");
                } else {
                    alert("ERROR: " + datos);
                    $("#dialogWait").dialog("close");
                }
            } else {
                alert("No hay MailGroup disponible");
                $("#dialogWait").dialog("close");
            }
        }, error: function (objeto, quepaso, otroobj) {
            alert(":pto=8:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }});
}
function SendMailContacto() {
//    var strHtmlBtnMail = "";
    var strPost = "";
    var strCurso = "";
    strCurso = document.getElementById("CC_CURSO_NOMBRE").value;
    var strMensual = "";
    var strTipoMail = "";
    strTipoMail = document.getElementById("CT_TIPO_MAIL").value;
    strMensual = document.getElementById("CT_MAILMENSUAL").value;
    if (strCurso != "" || (strTipoMail != "0" && strMensual != "")) {
        var grid = jQuery("#GRIDCONTACTOS");
        var ids = grid.getGridParam("selrow");
        if (ids !== null) {
            var lstRow1 = grid.getRowData(ids);
            var strCorreo = lstRow1.CCO_CORREO;
            var strCorreo2 = lstRow1.CCO_CORREO2;
            var strNombre = lstRow1.CCO_NOMBRE;
            if (strCorreo != "") {
                if (strCurso != "") {
                    strPost = "curso=" + strCurso.split(" /", 1);
                } else {
                    strPost = "curso=" + strCurso;
                }
                strPost += "&email=" + strCorreo;
                strPost += "&email2=" + strCorreo2;
                strPost += "&tipo_mail=" + strTipoMail;
                strPost += "&id_mes=" + strMensual;
                $("#dialogWait").dialog("open");
                $.ajax({
                    type: "POST",
                    data: strPost,
                    scriptCharset: "UTF-8",
                    contentType: "application/x-www-form-urlencoded;charset=utf-8",
                    cache: false,
                    dataType: "html",
                    url: "COFIDE_Programacionmails.jsp?ID=10",
                    success: function (datos) {
                        datos = trim(datos);
                        if (datos == "OK") {
                            alert("Mensaje Enviado");
                        } else {
                            alert("ERROR: " + datos);
                        }
                        $("#dialogWait").dialog("close");
                    }, error: function (objeto, quepaso, otroobj) {
                        alert(":pto=10:" + objeto + " " + quepaso + " " + otroobj);
                        $("#dialogWait").dialog("close");
                    }});
//                alert("va a mandar algo: " + strCurso + " curso, " + strMensual + " mes, " + strCorreo + " correo");
            } else {
                alert("el Contacto no tiene un correo");
            }
        } else {
            alert("Selecciona un contacto!");
        }
    } else {
        alert("Elige un curso o un Tipo de mail para enviar");
    }
}

function loadRazonSocial(intIdCte) {
    jQuery("#GRD_RZN").clearGridData();
    var strPost = "CT_ID=" + intIdCte;
    $.ajax({
        type: "POST",
        data: strPost,
        scriptCharset: "UTF-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "COFIDE_Telemarketing.jsp?ID=21",
        success: function (datos) {
            var lstXml = datos.getElementsByTagName("vta")[0];
            var lstCte = lstXml.getElementsByTagName("datos");
            for (var i = 0; i < lstCte.length; i++) {
                var objcte = lstCte[i];
                var datarow = {
                    RZN_ID: objcte.getAttribute("RZN_ID"),
                    RZN_CTE: objcte.getAttribute("RZN_CTE"),
                    RZN_NOMBRE: objcte.getAttribute("RZN_NOMBRE")
                };
                itemIdCob++;
                jQuery("#GRD_RZN").addRowData(itemIdCob, datarow, "last");
            }
            //actualiza el historial de ventas
            HistorialVtaCte(document.getElementById("CT_ID").value);
        }});
}
function IsInBound() {
//    var strNomMain = objMap.getNomMain();
//    var bolInBound = false;
//    if (strNomMain == "TELEM_INB") {
//        bolInBound = true;
//    } else {
//        setTimeout('document.getElementById("NCT_MEDIO").parentNode.parentNode.style.display = "none"', 1000);
//    }
//    return bolInBound;
    var strBase = document.getElementById("CT_CLAVE_DDBB").value;
    if (strBase == "BE-INBOUND" || strBase == "") {
        setTimeout('document.getElementById("NCT_MEDIO").parentNode.parentNode.style.display = ""', 1000);
    } else {
        setTimeout('document.getElementById("NCT_MEDIO").parentNode.parentNode.style.display = "none"', 1000);
    }
}
function errorbase() {
    var ct_id = document.getElementById("CT_ID").value;
    if (ct_id != "0") {
        document.getElementById("SioNO_inside").innerHTML = "¿Estas seguro de desechar este registro?";
        $("#SioNO").dialog("open");
        document.getElementById("btnSI").onclick = function () {
            $("#SioNO").dialog("close");
            var strPost = "ct_id=" + ct_id;
            $.ajax({
                type: "POST",
                data: strPost,
                scriptCharset: "utf-8",
                contentType: "application/x-www-form-urlencoded;charset=utf-8",
                cache: false,
                dataType: "html",
                url: "COFIDE_Telemarketing.jsp?ID=22",
                success: function (datos) {
                    consultaVta();
                }});
        };
        document.getElementById("btnNO").onclick = function () {
            $("#SioNO").dialog("close");
        };
    } else {
        alert("No Hay Ningun Cliente a Remover");
    }
}
function UpDateCte() {
//    var bolDuplicado = document.getElementById("CT_DUPLICADO").value;
//    if (bolDuplicado != "1") {
    if (intDuplicado1 != "1" && intDuplicado2 != "1") {
        if (document.getElementById("CT_NO_CLIENTE").value != "0") {
            var strIdCte = document.getElementById("CT_NO_CLIENTE").value;
            var strPost = "";
            strPost += "CT_ID=" + document.getElementById("CT_ID").value;
            strPost += "&CT_RAZONSOCIAL=" + encodeURIComponent(document.getElementById("CT_RAZONSOCIAL").value);
            strPost += "&CT_RFC=" + encodeURIComponent(document.getElementById("CT_RFC").value);
            strPost += "&CT_SEDE=" + document.getElementById("CT_SEDE").value;
            strPost += "&CT_CONTACTO=" + document.getElementById("CT_CONTACTO").value;
            strPost += "&CT_CONTACTO2=" + document.getElementById("CT_CONTACTO2").value;
            strPost += "&CT_CORREO=" + document.getElementById("CT_CORREO").value;
            strPost += "&CT_CORREO2=" + document.getElementById("CT_CORREO2").value;
            strPost += "&CT_COMENTARIOS=" + document.getElementById("CT_COMENTARIOS").value;
            strPost += "&CT_GIRO=" + document.getElementById("CT_GIRO").value;
            strPost += "&CT_AREA=" + document.getElementById("CT_AREA").value;
            strPost += "&CT_CP=" + document.getElementById("CT_CP").value;
            strPost += "&CT_CALLE=" + document.getElementById("CT_CALLE").value;
            strPost += "&CT_COL=" + document.getElementById("CT_COL").value;
            strPost += "&CT_NUM=" + document.getElementById("CT_NUM").value;
            strPost += "&CT_NOMBRE=" + document.getElementById("CT_CONTACTO_ENTRADA").value;
            strPost += "&CT_CONMUTADOR=" + document.getElementById("CT_CONMUTADOR").value;
            var intMailMes = 0;
            if (document.getElementById("CT_MAILMES").checked == true) {
                intMailMes = 1;
            }


            if (document.getElementById("CT_NOMBRE_INVITO") != null) { //almacena los motivos de porque no compra
                strPost += "&motivo=" + document.getElementById("CT_NOMBRE_INVITO").value;
            }

            var intAAA = 0;
            if (document.getElementById("CT_AAA") != null) {
                if (document.getElementById("CT_AAA").checked) {
                    intAAA = 1;
                }
            }
            strPost += "&CT_MAILMES=" + intMailMes;
            strPost += "&CT_AAA=" + intAAA;
            strPost += "&mediopublicidad=" + document.getElementById("CT_MKT").value;
            ActualizaRzn(document.getElementById("CT_ID").value); //guardarazonsocial
            $("#dialogWait").dialog("open");
            $.ajax({
                type: "POST",
                data: encodeURI(strPost),
                scriptCharset: "UTF-8",
                contentType: "application/x-www-form-urlencoded;charset=utf-8",
                cache: false,
                dataType: "html",
                url: "COFIDE_Telemarketing.jsp?ID=23",
                success: function (datos) {
                    $("#dialogWait").dialog("close");
                    guardarContactos(strIdCte, 1); //1 = actualizacion , 0 = proceso normal                    
                }, error: function (objeto, quepaso, otroobj) {
                    alert(":pto=23: " + objeto + " " + quepaso + " " + otroobj);
                    $("#dialogWait").dialog("close");
                }
            });
        } else {
            alert("Revisa bien los datos a actualizar!");
        }
        intDuplicado1 = "0";
        intDuplicado2 = "0";
    } else {
        alert("Se detecto un conflicto, revisar información");
    }
}
function ValidaMailLN(opc) {
    var intIDCte = "0";
    var strCorreo = "";
    var strCorreoValue = "";
    var bolValidado = false;
//    var bolValida1 = false;
//    var bolValida2 = false;
    bolValidado = validaCorreo();
    if (opc == 1) {
        strCorreo = document.getElementById("CT_CORREO").value;
        strCorreoValue = "CT_CORREO";
        intIDCte = document.getElementById("CT_NO_CLIENTE").value;
        if (strCorreo != "") {
            validaFormatoCorreo(strCorreo, strCorreoValue);
        } else {
            intDuplicado1 = "0";
            intDuplicado2 = "0";
            document.getElementById(strCorreoValue).style["background"] = "#FFFFFF";
            document.getElementById(strCorreoValue).style.borderColor = "#D8D8D8";
        }
    }
    if (opc == 2) {
        strCorreo = document.getElementById("CT_CORREO2").value;
        strCorreoValue = "CT_CORREO2";
        intIDCte = document.getElementById("CT_NO_CLIENTE").value;
    }
    if (opc == 3) {
        strCorreo = document.getElementById("CCO_CORREO").value;
        strCorreoValue = "CCO_CORREO";
        intIDCte = document.getElementById("CT_NO_CLIENTE").value;
        if (strCorreo != "") {
            bolValida1 = validaFormatoCorreo(strCorreo, strCorreoValue);
            if (!bolValida1) {
                intDuplicado1 = "0";
                document.getElementById(strCorreoValue).style["background"] = "#FFFFFF";
                document.getElementById("BTN_ADD").disabled = false;
                document.getElementById(strCorreoValue).style.borderColor = "#D8D8D8";
            } else {
                intDuplicado1 = "1";
            }
        } else {
            intDuplicado1 = "0";
            document.getElementById(strCorreoValue).style["background"] = "#FFFFFF";
            document.getElementById("BTN_ADD").disabled = false;
            document.getElementById(strCorreoValue).style.borderColor = "#D8D8D8";
        }
    }
    if (opc == 4) {
        strCorreo = document.getElementById("CCO_CORREO2").value;
        strCorreoValue = "CCO_CORREO2";
        intIDCte = document.getElementById("CT_NO_CLIENTE").value;
        if (strCorreo != "") {
            bolValida2 = validaFormatoCorreo(strCorreo, strCorreoValue);
            if (!bolValida2) {
                intDuplicado2 = "0";
                document.getElementById(strCorreoValue).style["background"] = "#FFFFFF";
                document.getElementById("BTN_ADD").disabled = false;
                document.getElementById(strCorreoValue).style.borderColor = "#D8D8D8";
            } else {
                intDuplicado2 = "1";
            }
        } else {
            intDuplicado2 = "0";
            document.getElementById(strCorreoValue).style["background"] = "#FFFFFF";
            document.getElementById("BTN_ADD").disabled = false;
            document.getElementById(strCorreoValue).style.borderColor = "#D8D8D8";
        }
    }
    if (opc == 5) {
        strCorreo = document.getElementById("NCT_EMAIL1").value;
        strCorreoValue = "NCT_EMAIL1";
        intIDCte = "0";
        if (validaFormatoCorreo(strCorreo, strCorreoValue)) {
            document.getElementById("NCT_SAVEBTN").disabled = true;
        } else {
            document.getElementById("NCT_SAVEBTN").disabled = false;
            document.getElementById(strCorreoValue).style.borderColor = "#D8D8D8";
        }
    }
    if (strCorreo != "") {
        var strPost = "correo=" + strCorreo;
        strPost += "&idcte=" + intIDCte;
        strPost += "&ct_ddbb=" + document.getElementById("CT_CLAVE_DDBB").value;
        var strRespuestaDupbase = "";
        var strRespuestaDup = "";
        var strRespuestaLibre = "";
//        console.log(bolValida1 + " / " + bolValida2);
        if (bolValida1 || bolValida2) {
            document.getElementById("BTN_ADD").disabled = true;
        } else {
            if (!bolValidado) {
                $.ajax({
                    type: "POST",
                    data: strPost,
                    scriptCharset: "UTF-8",
                    contentType: "application/x-www-form-urlencoded;charset=utf-8",
                    cache: false,
                    dataType: "xml",
                    url: "COFIDE_Telemarketing.jsp?ID=24",
                    success: function (datos) {
                        var lstXml = datos.getElementsByTagName("vta")[0];
                        var lstCte = lstXml.getElementsByTagName("datos");
                        for (var i = 0; i < lstCte.length; i++) {
                            var objcte = lstCte[i];
                            strRespuestaDupbase = objcte.getAttribute("duplicadoBase");
                            strRespuestaDup = objcte.getAttribute("duplicado");
                            strRespuestaLibre = objcte.getAttribute("libre");
                            if (strRespuestaLibre == "1" && strRespuestaDup == "0" && strRespuestaDupbase == "0") {
                                intDuplicado1 = "0";
                                intDuplicado2 = "0";
                                document.getElementById(strCorreoValue).style["background"] = "#FFFFFF";
                            } else {
                                if (strRespuestaDup == "1") {
                                    if (strCorreoValue != "CCO_CORREO" && strCorreoValue != "CCO_CORREO2") {
                                        intDuplicado1 = "1";
                                        intDuplicado2 = "1";
                                        document.getElementById(strCorreoValue).style["background"] = "#80FF00";
                                    } else {
                                        if (strCorreoValue == "CCO_CORREO") {
                                            intDuplicado1 = "1";
                                            document.getElementById(strCorreoValue).style["background"] = "#80FF00";
                                        }
                                        if (strCorreoValue == "CCO_CORREO2") {
                                            intDuplicado2 = "1";
                                            document.getElementById(strCorreoValue).style["background"] = "#80FF00";
                                        }
                                    }
                                }
                                if (strRespuestaDupbase == "1") {
                                    if (strCorreoValue != "CCO_CORREO" && strCorreoValue != "CCO_CORREO2") {
                                        intDuplicado1 = "1";
                                        intDuplicado2 = "1";
                                        document.getElementById(strCorreoValue).style["background"] = "#FF0000";
                                    } else {
                                        if (strCorreoValue == "CCO_CORREO") {
                                            intDuplicado1 = "1";
                                            document.getElementById(strCorreoValue).style["background"] = "#FF0000";
                                        }
                                        if (strCorreoValue == "CCO_CORREO2") {
                                            intDuplicado2 = "1";
                                            document.getElementById(strCorreoValue).style["background"] = "#FF0000";
                                        }
                                    }
                                }
                            }
                        }
                    }});
            } else {
                if (strCorreoValue != "CCO_CORREO" && strCorreoValue != "CCO_CORREO2") {
                    intDuplicado1 = "1";
                    intDuplicado2 = "1";
                    document.getElementById(strCorreoValue).style["background"] = "#FFFF00";
                } else {
                    if (strCorreoValue == "CCO_CORREO") {
                        intDuplicado1 = "1";
                        document.getElementById(strCorreoValue).style["background"] = "#FFFF00";
                    }
                    if (strCorreoValue == "CCO_CORREO2") {
                        intDuplicado2 = "1";
                        document.getElementById(strCorreoValue).style["background"] = "#FFFF00";
                    }
                }

            }
        }
    }
}

function BuscaCursoEmail() {
    var strCurso = document.getElementById("CC_CURSO_NOMBRE").value;
    var strPost = "CURSO=" + strCurso;
    $(function () {
        $("#CC_CURSO_NOMBRE").autocomplete({//campo de texto que tendra el autocmplete
            source: "COFIDE_Telemarketing.jsp?ID=40&" + strPost,
            minLength: 2
        });
    });
    MensualIndividual(1);
}
function ActualizaRzn(intIdCte) {
    var strPost = "";
    var gridRzn = jQuery("#GRD_RZN");
    var idArrRzn = gridRzn.getDataIDs();
    strPost += "CT_ID=" + intIdCte;
    for (var ii = 0; ii < idArrRzn.length; ii++) {
        var idRzn = idArrRzn[ii];
        var lstRowRzn = gridRzn.getRowData(idRzn);
        strPost += "&RAZONSOCIAL" + ii + "=" + lstRowRzn.RZN_NOMBRE;
    }
    strPost += "&length=" + idArrRzn.length;
    $.ajax({
        type: "POST",
        data: strPost,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "html",
        url: "COFIDE_Telemarketing.jsp?ID=25",
    });
}

function validaTelefono(opc) { //1 telefono / 2 celular valida los digitos
    var campoTelefono = document.getElementById("CT_CONTACTO");
    var strTelefono = document.getElementById("CT_CONTACTO").value;
    var campoCelular = document.getElementById("CT_CONTACTO2");
    var strCelular = document.getElementById("CT_CONTACTO2").value;
    var strNumLocal_Metro = document.getElementById("MET_LOCAL").checked;
    if (strTelefono != "") {
        if (opc == 1) {
            if (strNumLocal_Metro == true) {
                if (strTelefono.length < 10) {
                    document.getElementById("MET_LOCAL").focus();
                    campoTelefono.style["background"] = "#F5A9A9";
                    alert("verifica el codigo local");
                } else {
                    campoTelefono.style["background"] = "#ffffff";
                }
            } else {
                if (strTelefono.length < 12) {
                    document.getElementById("MET_LOCAL").focus();
                    campoTelefono.style["background"] = "#F5A9A9";
//                    alert("verifica el codigo y la lada");
                } else {
                    campoTelefono.style["background"] = "#ffffff";
                }
            }
        } // fin 1
    }
    if (strCelular != "") {
        if (opc == 2) {
            if (strCelular.length < 13) {
                campoCelular.style["background"] = "#F5A9A9";
                alert("verifica el codigo y la lada");
            } else {
                campoCelular.style["background"] = "#ffffff"
            }
        }
    }
}
function ValidaNumTelefono(opc) {

    var intCT_ID = "0";
    var strTelefono = "";
    var campoTelefono = "";
    if (opc == 1) {
        strTelefono = document.getElementById("NCT_NUMERO").value;
        campoTelefono = "NCT_NUMERO";
        intCT_ID = "0";
    }
    if (opc == 2) {
        strTelefono = document.getElementById("CCO_TELEFONO").value;
        campoTelefono = "CCO_TELEFONO";
        intCT_ID = document.getElementById("CT_ID").value;
    }
    if (opc == 3) {
        strTelefono = document.getElementById("CCO_ALTERNO").value;
        campoTelefono = "CCO_ALTERNO";
        intCT_ID = document.getElementById("CT_ID").value;
    }
    if (strTelefono != "") {
        var strPost = "telefono=" + strTelefono;
        strPost += "&idcte=" + intCT_ID;
        strPost += "&ct_ddbb=" + document.getElementById("CT_CLAVE_DDBB").value;
        var strRespuestaDupbase = "";
        var strRespuestaDup = "";
        var strRespuestaLibre = "";
        $.ajax({
            type: "POST",
            data: strPost,
            scriptCharset: "UTF-8",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "xml",
            url: "COFIDE_Telemarketing.jsp?ID=26",
            success: function (datos) {
                var lstXml = datos.getElementsByTagName("vta")[0];
                var lstCte = lstXml.getElementsByTagName("datos");
                for (var i = 0; i < lstCte.length; i++) {
                    var objcte = lstCte[i];
                    strRespuestaDupbase = objcte.getAttribute("duplicadoBase");
                    strRespuestaDup = objcte.getAttribute("duplicado");
                    strRespuestaLibre = objcte.getAttribute("libre");
                    if (strRespuestaLibre == "1" && strRespuestaDup == "0" && strRespuestaDupbase == "0") {
                        document.getElementById(campoTelefono).style["background"] = "#FFFFFF";
                        document.getElementById("NCT_SAVEBTN").disabled = false;
                    } else {
                        if (strRespuestaDup == "1") {
                            if (opc == 1) {
                                document.getElementById("NCT_SAVEBTN").disabled = true;
                            }
                            document.getElementById(campoTelefono).style["background"] = "#80FF00";
                        } else {
                            document.getElementById(campoTelefono).style["background"] = "#FFFFFF";
                            document.getElementById("NCT_SAVEBTN").disabled = false;
                        }
                        if (strRespuestaDupbase == "1") {
                            document.getElementById(campoTelefono).style["background"] = "#FF0000";
                            if (opc == 1) {
                                document.getElementById("NCT_SAVEBTN").disabled = true;
                            }
                        } else {
                            document.getElementById(campoTelefono).style["background"] = "#FFFFFF";
                            document.getElementById("NCT_SAVEBTN").disabled = false;
                        }
                    }
                }
            }});
    }
}

function ValidaFinesSemana() {
    var strFecha = document.getElementById("CT_FECHA").value;
    var strDia = "";
    var strPost = "dia=" + strFecha;
    if (strFecha != "") {
        $.ajax({
            type: "POST",
            data: strPost,
            scriptCharset: "UTF-8",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "xml",
            url: "COFIDE_Telemarketing.jsp?ID=27",
            success: function (datos) {
                var lstXml = datos.getElementsByTagName("vta")[0];
                var lstCte = lstXml.getElementsByTagName("datos");
                for (var i = 0; i < lstCte.length; i++) {
                    var objcte = lstCte[i];
                    strDia = objcte.getAttribute("dia");
                    if (strDia == "6" || strDia == "7") {
                        alert("No se puede agendar en fin de semana");
                        document.getElementById("CT_FECHA").value = "";
                        document.getElementById("CT_HORA").value = "";
                    } else {
                        Validafecha();
                    }
                }
            }});
    }
}
function OpnMotorBusqueda() {
    var objSecModiVta = objMap.getScreen("MOTOR_CTE");
    if (objSecModiVta != null) {
        objSecModiVta.bolActivo = false;
        objSecModiVta.bolMain = false;
        objSecModiVta.bolInit = false;
        objSecModiVta.idOperAct = 0;
    }
    OpnOpt("MOTOR_CTE", "_ed", "dialogInv", false, false, true);
}
function MotorBusqueda() {

    jQuery("#MB_GRD").clearGridData();
    var strBuscar = document.getElementById("MB_BUSQUEDA").value;
    var strFiltro = document.getElementById("MB_COMBO").value;
    var ctBase = "";
    if (objMap.getNomMain() == "VAL_DATOS") {
        ctBase = "";
    } else {
        ctBase = document.getElementById("CT_CLAVE_DDBB").value;
    }
    var strBaseUser = ctBase;
    var strOkis = "OK";
    var strPost = "buscar=" + encodeURIComponent(strBuscar);
    strPost += "&filtro=" + strFiltro;
    strPost += "&BaseUser=" + strBaseUser;
    if (strBuscar != "") {
        if (strFiltro != "0") {
            if (strFiltro == "1") { //id cliente
                if (!validaNumero(strBuscar)) { //si es falso es diferente a numero 
                    alert("Solo valores numericos");
                    document.getElementById("MB_BUSQUEDA").value = "";
                    strOkis = "NO";
                }
            }
            if (strOkis == "OK") {
                $("#dialogWait").dialog("open");
                $.ajax({
                    type: "POST",
                    data: encodeURI(strPost),
                    scriptCharset: "UTF-8",
                    contentType: "application/x-www-form-urlencoded;charset=utf-8",
                    cache: false,
                    dataType: "xml",
                    url: "COFIDE_Telemarketing.jsp?ID=28",
                    success: function (datos) {
                        var lstXml = datos.getElementsByTagName("vta")[0];
                        var lstCte = lstXml.getElementsByTagName("datos");
                        for (var i = 0; i < lstCte.length; i++) {
                            var objcte = lstCte[i];
                            var exp_prosp = "";
                            if (objcte.getAttribute("CT_ES_PROSPECTO") == "1") {
                                exp_prosp = "PROSPECTO"
                            } else {
                                exp_prosp = "EXPARTICIPANTE";
                            }
                            var datarow = {
                                CT_ID: objcte.getAttribute("CT_ID"),
                                CT_RAZONSOCIAL: objcte.getAttribute("CT_RAZONSOCIAL"),
                                CT_CLAVE_DDBB: objcte.getAttribute("CT_CLAVE_DDBB"),
                                nombre_usuario: objcte.getAttribute("nombre_usuario"),
                                CT_ES_PROSPECTO: exp_prosp
                            };
                            itemIdCob++;
                            jQuery("#MB_GRD").addRowData(itemIdCob, datarow, "last");
                        }
                        $("#dialogWait").dialog("close");
                    }});
            }
        } else {
            alert("Elige el filtro de busqueda");
        }
    } else {
        alert("Ingresa el valor que deseas buscar");
    }
}

function dblClickCTE(id) {
    var strNomMain = objMap.getNomMain();
    var grid = jQuery("#MB_GRD"); //nombre del grid detalle
    var lstVal = grid.getRowData(id);
    if (strNomMain == "MOTOR_CTE") { //pantalla que lo contiene
        OpnEdit(document.getElementById("Ed" + strNomMain));
    } else {
        if (strNomMain == "C_TELEM") { //pantalla principal
            var strCte = lstVal.CT_ID;
            consultaVta(strCte);
            $("#dialogInv").dialog("close");
        }
        if (strNomMain == "VAL_DATOS") {
            var strCte = lstVal.CT_ID;
            document.getElementById("tmp_ct_id").value = strCte;
            //$("#dialogInv").dialog("close");
            opnCteDetalleVald();
        }
    }
}
function TiempoPausa(strStatus) {
    var strTipoPausa = "PAUSA";
//    if (document.getElementById("tipo_pausa1").checked) {
//        strTipoPausa = "CAPACITACION";
//    }
//    if (document.getElementById("tipo_pausa2").checked) {
//        strTipoPausa = "ADMINISTRACION";
//    }
//    if (document.getElementById("tipo_pausa3").checked) {
//        strTipoPausa = "SANITARIO";
//    }
//    if (document.getElementById("tipo_pausa4").checked) {
//        strTipoPausa = "COMIDA";
//    }
    var strPost = "";
    var strIDPausa = "";
    if (strStatus == "DISPONIBLE") {
        strIDPausa = document.getElementById("CT_ID_PAUSA").value;
        if (strIDPausa != "") {
            strPost = "estatus=1" + "&idpausa=" + strIDPausa;
            $.ajax({
                type: "POST",
                data: strPost,
                scriptCharset: "UTF-8",
                contentType: "application/x-www-form-urlencoded;charset=utf-8",
                cache: false,
                dataType: "xml",
                url: "COFIDE_Telemarketing.jsp?ID=29",
            });
        }
    } else {
        strPost = "estatus=0" + "&tipopausa=" + strTipoPausa;
        $.ajax({
            type: "POST",
            data: strPost,
            scriptCharset: "UTF-8",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "xml",
            url: "COFIDE_Telemarketing.jsp?ID=29",
            success: function (datos) {
                var lstXml = datos.getElementsByTagName("vta")[0];
                var lstCte = lstXml.getElementsByTagName("datos");
                for (var i = 0; i < lstCte.length; i++) {
                    var objcte = lstCte[i];
                    strIDPausa = objcte.getAttribute("idpausa");
                }
                document.getElementById("CT_ID_PAUSA").value = strIDPausa;
            }});
    }
}

function tmp_Guardar() {
    clearTimeout(timTimer);
    if (document.getElementById("CT_NO_CLIENTE").value != 0) {
        _drawPendientes();
        var intMailMes = 0;
        if (document.getElementById("CT_MAILMES").checked == true) {
            intMailMes = 1;
        }
        var strRazonSOcial = "";
        if (document.getElementById("CT_RAZONSOCIAL").value != "") {
            strRazonSOcial = document.getElementById("CT_RAZONSOCIAL").value;
        } else {
            strRazonSOcial = "-";
        }

        var strPost = "";
        strPost += "CT_ID=" + document.getElementById("CT_ID").value; // hidden
        strPost += "&CT_NO_CLIENTE=" + document.getElementById("CT_ID").value; // numero del cliente
//        strPost += "&CT_RAZONSOCIAL=" + encodeURIComponent(document.getElementById("CT_RAZONSOCIAL").value);
        strPost += "&CT_RAZONSOCIAL=" + encodeURIComponent(strRazonSOcial);
        strPost += "&CT_RFC=" + encodeURIComponent(document.getElementById("CT_RFC").value);
        strPost += "&CT_GIRO=" + document.getElementById("CT_GIRO").value;
        strPost += "&CT_SEDE=" + document.getElementById("CT_SEDE").value;
        strPost += "&CT_NOMBRE=" + document.getElementById("CT_CONTACTO_ENTRADA").value;
        strPost += "&CT_CONTACTO=" + document.getElementById("CT_CONTACTO").value;
        strPost += "&CT_CONTACTO2=" + document.getElementById("CT_CONTACTO2").value;
        strPost += "&CT_AREA=" + document.getElementById("CT_AREA").value;
        strPost += "&CT_CORREO=" + document.getElementById("CT_CORREO").value;
        strPost += "&CT_CORREO2=" + document.getElementById("CT_CORREO2").value;
        strPost += "&CT_CONMUTADOR=" + document.getElementById("CT_CONMUTADOR").value;
        strPost += "&CT_FECHA=" + document.getElementById("CT_FECHA").value;
        strPost += "&CT_HORA=" + document.getElementById("CT_HORA").value;
        strPost += "&CT_COMENTARIOS=" + document.getElementById("CT_COMENTARIOS").value;
        strPost += "&CT_COMENTARIO=" + document.getElementById("CT_COMENTARIO").value;
        strPost += "&CT_MAILMES=" + intMailMes;
        strPost += "&CT_CP=" + document.getElementById("CT_CP").value;
        strPost += "&CT_CALLE=" + encodeURIComponent(document.getElementById("CT_CALLE").value);
        strPost += "&CT_COL=" + document.getElementById("CT_COL").value;
        strPost += "&CT_MUNI=" + document.getElementById("CT_MUNI").value;
        strPost += "&CT_EDO=" + document.getElementById("CT_EDO").value;
        strPost += "&CT_NUM=" + document.getElementById("CT_NUM").value;
        strPost += "&exp_pro=" + document.getElementById("exp_pro").value;
        strPost += "&coment=" + document.getElementById("CT_COMENTARIOS").value;
        strPost += "&fecha=" + document.getElementById("CT_FECHA").value;
        strPost += "&hora=" + document.getElementById("CT_HORA").value;

        if (document.getElementById("CT_NOMBRE_INVITO") != null) { //almacena los motivos de porque no compra
            strPost += "&motivo=" + document.getElementById("CT_NOMBRE_INVITO").value;
        }

        var grid = jQuery("#GRIDCONTACTOS");
        var idArrC = grid.getDataIDs();
        for (var i = 0; i < idArrC.length; i++) {
            var id = idArrC[i];
            var lstRow = grid.getRowData(id);
            strPost += "&CCO_TITULO" + i + "=" + lstRow.CCO_TITULO + "";
            strPost += "&CCO_NOMBRE" + i + "=" + lstRow.CCO_NOMBRE + "";
            strPost += "&CCO_APPATERNO" + i + "=" + lstRow.CCO_APPATERNO + "";
            strPost += "&CCO_APMATERNO" + i + "=" + lstRow.CCO_APMATERNO + "";
            strPost += "&CCO_NOSOCIO" + i + "=" + lstRow.CCO_NOSOCIO + "";
            strPost += "&CCO_AREA" + i + "=" + lstRow.CCO_AREA + "";
            strPost += "&CCO_ASOCIACION" + i + "=" + lstRow.CCO_ASOCIACION + "";
            strPost += "&CCO_CORREO_" + i + "=" + lstRow.CCO_CORREO + "";
            strPost += "&CCO_CORREO2_" + i + "=" + lstRow.CCO_CORREO2 + "";
            strPost += "&CT_MAILMES1" + i + "=" + lstRow.MAILMES1 + "";
            strPost += "&CT_MAILMES2" + i + "=" + lstRow.MAILMES2 + "";
            strPost += "&CCO_TELEFONO" + i + "=" + lstRow.CCO_TELEFONO + "";
            strPost += "&CCO_EXTENCION" + i + "=" + lstRow.CCO_EXTENCION + "";
            strPost += "&CCO_ALTERNO" + i + "=" + lstRow.CCO_ALTERNO + "";
            strPost += "&CONTACTO_ID" + i + "=" + lstRow.CONTACTO_ID + "";
        }
        strPost += "&length_contactos=" + idArrC.length;
        var grid = jQuery("#GRD_RZN");
        var idArrR = grid.getDataIDs();
        for (var i = 0; i < idArrR.length; i++) {
            var id = idArrR[i];
            var lstRow = grid.getRowData(id);
            strPost += "&RZN_NOMBRE" + i + "=" + lstRow.RZN_NOMBRE + "";
        }
        strPost += "&length_razon=" + idArrR.length;
        $("#dialogWait").dialog("open");
        $.ajax({
            type: "POST",
            data: encodeURI(strPost),
            scriptCharset: "UTF-8",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "html",
            url: "COFIDE_Telemarketing.jsp?ID=30",
            success: function (dato) {
                dato = trim(dato);
                if (dato == "OK") {
                    tmp_Limpiar();
                } else {
                    alert(dato);
                }
                $("#dialogWait").dialog("close");
            }});
    } else {
        alert("Ya tienes un registro pendiente");
    }
}

function tmp_Mostrar(strID_CTE) {
    var strPost = "";
    strPost = "cte_manual=" + strID_CTE;
    $.ajax({
        type: "POST",
        data: strPost,
        scriptCharset: "UTF-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "COFIDE_Telemarketing.jsp?ID=31",
        success: function (datos) {
            var lstXml = datos.getElementsByTagName("vta")[0];
            var lstCte = lstXml.getElementsByTagName("datos");
            for (var i = 0; i < lstCte.length; i++) {
                var objcte = lstCte[i];
                if (objcte.getAttribute("CT_ID") != 0) {
                    document.getElementById("CT_ID").value = objcte.getAttribute("CT_ID");
                    document.getElementById("CT_ID_CLIENTE").value = objcte.getAttribute("CT_ID_CLIENTE");
                    document.getElementById("CT_RAZONSOCIAL").value = objcte.getAttribute("CT_RAZONSOCIAL");
                    document.getElementById("CT_NO_CLIENTE").value = objcte.getAttribute("CT_ID");
                    document.getElementById("CT_RFC").value = objcte.getAttribute("CT_RFC");
                    document.getElementById("CT_COL").value = objcte.getAttribute("CT_COLONIA");
                    document.getElementById("CT_CONTACTO").value = objcte.getAttribute("CT_TELEFONO1");
                    document.getElementById("CT_CONTACTO2").value = objcte.getAttribute("CT_TELEFONO2");
                    document.getElementById("CT_CORREO").value = objcte.getAttribute("CT_EMAIL1");
                    document.getElementById("CT_CORREO2").value = objcte.getAttribute("CT_EMAIL2");
                    document.getElementById("CT_CP").value = objcte.getAttribute("CT_CP");
                    document.getElementById("CT_COL").value = objcte.getAttribute("CT_COL");
                    document.getElementById("CT_COLONIA_DB").value = objcte.getAttribute("CT_COL");
                    document.getElementById("CT_CALLE").value = objcte.getAttribute("CT_CALLE");
                    document.getElementById("CT_EDO").value = objcte.getAttribute("CT_EDO");
                    document.getElementById("CT_MUNI").value = objcte.getAttribute("CT_MUNI");
                    document.getElementById("CT_NUM").value = objcte.getAttribute("CT_NUMERO");
                    document.getElementById("CT_SEDE").value = objcte.getAttribute("CT_SEDE");
                    document.getElementById("CT_GIRO").value = objcte.getAttribute("CT_GIRO");
                    document.getElementById("CT_AREA").value = objcte.getAttribute("CT_AREA");
                    document.getElementById("CT_COMENTARIO").value = objcte.getAttribute("EV_ASUNTO");
                    document.getElementById("CT_CONMUTADOR").value = objcte.getAttribute("CT_CONMUTADOR");
                    document.getElementById("CT_CONTACTO_ENTRADA").value = objcte.getAttribute("CT_CONTACTO");
                    document.getElementById("CT_COMENTARIOS").value = objcte.getAttribute("CT_COMENTARIOS");
                    document.getElementById("CT_FECHA").value = objcte.getAttribute("CT_FECHA");
                    document.getElementById("CT_HORA").value = objcte.getAttribute("CT_HORA");
                    document.getElementById("CT_NOMBRE_INVITO").value = objcte.getAttribute("motivo");
                    //ex participante
                    if (objcte.getAttribute("cte_prosp") == "0") { //verde
                        document.getElementById("exp_pro").value = 0;
                        document.getElementById("CT_CTE").innerHTML = estilo + "<table>"
                                + "<tr>"
                                + '<td class=\'ExParticipante\'><i class = "fa fa-user" title="EX-Participante" style="font-size:55px;"></i></td>'
                                + '<td>' + check + '</td>'
                                + "</tr>"
                                + "</table>";
                    } else { //gris
                        document.getElementById("exp_pro").value = 1;
                        document.getElementById("CT_CTE").innerHTML = estilo + "<table>"
                                + "<tr>"
                                + '<td class=\'Prospecto\'><i class = "fa fa-user" title="Prospecto" style="font-size:55px;"></i></td>'
                                + '<td>' + check + '</td>'
                                + "</tr>"
                                + "</table>";
                    }
                    var bolMail = false;
                    if (objcte.getAttribute("envio_mail") == 1) {
                        bolMail = true;
                    }
                    document.getElementById("CT_MAILMES").checked = bolMail;
                    HtmlBoton(objcte.getAttribute("bolBase"), objcte.getAttribute("CT_TELEFONO1"), objcte.getAttribute("CT_EMAIL1"), true);
                    LoadContactosTmp(strID_CTE, 0);
                } else {
                    HtmlBoton("false", "", "", false);
                }
            }
        }, error: function (objeto, quepaso, otroobj) {
            alert(":pto=31:" + objeto + " " + quepaso + " " + otroobj);
        }});
    document.getElementById("BTN_ADD").parentNode.parentNode.style.display = "none";
    document.getElementById("BTN_CANCEL").parentNode.parentNode.style.display = "none";
    document.getElementById("BTN_NEW").parentNode.parentNode.style.display = "";
    document.getElementById("BTN_DEL").parentNode.parentNode.style.display = "";
    document.getElementById("CT_COMENTARIO").value = ""; //limpia comentario despues de guardar
}

function tmp_Limpiar() {
    document.getElementById("CT_MAILMES").checked == false;
    document.getElementById("CT_MAILMES1").checked == false;
    document.getElementById("CT_MAILMES2").checked == false;
    document.getElementById("CT_ID").value = 0
    document.getElementById("CT_NO_CLIENTE").value = 0
    document.getElementById("CT_RAZONSOCIAL").value = "";
    document.getElementById("CT_RFC").value = "";
    document.getElementById("CT_GIRO").value = "";
    document.getElementById("CT_SEDE").value = "";
    document.getElementById("CT_CONTACTO_ENTRADA").value = "";
    document.getElementById("CT_CONTACTO").value = "";
    document.getElementById("CT_CONTACTO2").value = "";
    document.getElementById("CT_AREA").value = "";
    document.getElementById("CT_CORREO").value = "";
    document.getElementById("CT_CORREO2").value = "";
    document.getElementById("CT_CONMUTADOR").value = "";
    document.getElementById("CT_FECHA").value = "";
    document.getElementById("CT_HORA").value = "";
    document.getElementById("CT_COMENTARIOS").value = "";
    document.getElementById("CT_COMENTARIO").value = "";
    document.getElementById("CT_CP").value = "";
    document.getElementById("CT_CALLE").value = "";
    document.getElementById("CT_COL").value = "";
    document.getElementById("CT_MUNI").value = "";
    document.getElementById("CT_EDO").value = "";
    document.getElementById("CT_NUM").value = "";
    document.getElementById("CT_DUPLICADO").value = "";
    jQuery("#GRIDCONTACTOS").clearGridData();
    jQuery("#GRD_RZN").clearGridData();
    jQuery("#H_VENTA_CTE").clearGridData();
}
function OpnCtePendiente() {
    var objSecModiVta = objMap.getScreen("CTE_PENDIENTE");
    if (objSecModiVta != null) {
        objSecModiVta.bolActivo = false;
        objSecModiVta.bolMain = false;
        objSecModiVta.bolInit = false;
        objSecModiVta.idOperAct = 0;
    }
    OpnOpt("CTE_PENDIENTE", "grid", "dialogCte", false, false, true);
}
function dblClickCteTmp(id) {
    var strNomMain = objMap.getNomMain();
    var grid = jQuery("#CTE_PENDIENTE"); //grid o pantalla
    var lstVal = grid.getRowData(id);
    if (strNomMain == "CTE_PENDIENTE") { //pantalla
        OpnEdit(document.getElementById("Ed" + strNomMain));
    } else {
        if (strNomMain == "C_TELEM") {
            var strCte = lstVal.CT_ID;
            tmp_Mostrar(strCte);
            getIndicadores();
            $("#dialogCte").dialog("close");
        }
    }
}

function LoadContactosTmp(strCT_ID) {
    var strPuntosSRC = "<div style=\"text-align:center\"><i title='No aplica Promoción' >\t0</i></div>";
    var intPuntos = 0;
    jQuery("#GRIDCONTACTOS").clearGridData();
    if (strCT_ID != "") {
        var strPost = "CT_ID=" + strCT_ID;
        $.ajax({
            type: "POST",
            data: strPost,
            scriptCharset: "UTF-8",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "xml",
            url: "COFIDE_Telemarketing.jsp?ID=32",
            success: function (datos) {
                var lstXml = datos.getElementsByTagName("vta")[0];
                var lstCte = lstXml.getElementsByTagName("datos");
                for (var i = 0; i < lstCte.length; i++) {
                    var objcte = lstCte[i];
                    intPuntos = parseInt(objcte.getAttribute("CCO_PUNTOS"));
                    if (intPuntos >= 1000) {
                        strPuntosSRC = "<div style=\"text-align:center;\"><a href=\"javascript:opnPromosion(" + objcte.getAttribute("CONTACTO_ID") + ")\"<i title='Promoción' class=\"fa fa-bullseye\" >\t" + intPuntos + "</i></a></div>";
                    } else {
                        strPuntosSRC = "<div style=\"text-align:center\"><i title='No aplica Promoción' >\t" + intPuntos + "</i></div>";
                    }
//                    console.log(objcte.getAttribute("CCO_TELEFONO"));
//                    console.log(objcte.getAttribute("CCO_ALTERNO"));
                    var strTel = "";
                    var strTelAlterno = "";
                    if (objcte.getAttribute("CCO_TELEFONO") != "") {
                        strTel = "<b><a style=\"color:#0B610B\" href=\"javascript:LlamarContacto_('" + objcte.getAttribute("CCO_TELEFONO") + "')\"<i class=\"fa fa-headphones\">\t" + objcte.getAttribute("CCO_TELEFONO") + "</i></a></b>";
                    }
                    if (objcte.getAttribute("CCO_ALTERNO") != "") {
                        strTelAlterno = "<b><a style=\"color:#0B610B\" href=\"javascript:LlamarContacto_('" + objcte.getAttribute("CCO_ALTERNO") + "')\"<i class=\"fa fa-headphones\">\t" + objcte.getAttribute("CCO_ALTERNO") + "</i></a></b>";
                    }
                    var datarow = {
                        CCO_ID: objcte.getAttribute("CCO_ID"),
                        CCO_NOMBRE: objcte.getAttribute("CCO_NOMBRE"),
                        CCO_APPATERNO: objcte.getAttribute("CCO_APPATERNO"),
                        CCO_APMATERNO: objcte.getAttribute("CCO_APMATERNO"),
                        CCO_TITULO: objcte.getAttribute("CCO_TITULO"),
                        CCO_NOSOCIO: objcte.getAttribute("CCO_NOSOCIO"),
                        CCO_AREA: objcte.getAttribute("CCO_AREA"),
                        CCO_ASOCIACION: objcte.getAttribute("CCO_ASOCIACION"),
                        CCO_CORREO: objcte.getAttribute("CCO_CORREO"),
                        CCO_CORREO2: objcte.getAttribute("CCO_CORREO2"),
                        CCO_TELEFONO: objcte.getAttribute("CCO_TELEFONO"),
                        CCO_TELEFONO2: strTel,
                        CCO_EXTENCION: objcte.getAttribute("CCO_EXTENCION"),
                        CONTACTO_ID: objcte.getAttribute("CONTACTO_ID"),
                        CCO_PUNTOS: strPuntosSRC,
                        CCO_ALTERNO: objcte.getAttribute("CCO_ALTERNO"),
                        CCO_ALTERNO2: strTelAlterno
                    };
                    itemIdCob++;
                    jQuery("#GRIDCONTACTOS").addRowData(itemIdCob, datarow, "last");
                }
                LoadRazonTmp(strCT_ID);
            }, error: function (objeto, quepaso, otroobj) {
                alert(":pto=32: " + objeto + " " + quepaso + " " + otroobj);
            }});
    }
}
function opnPromosion(idContacto) {

    document.getElementById("CT_CONTACTO_ID").value = idContacto;
    document.getElementById("CT_PROMOSION").value = "1";
    OpnDiagNVenta(idContacto);
}
function LoadRazonTmp(strCT_ID) {
    jQuery("#GRD_RZN").clearGridData();
    var strPost = "CT_ID=" + strCT_ID;
    $.ajax({
        type: "POST",
        data: strPost,
        scriptCharset: "UTF-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "COFIDE_Telemarketing.jsp?ID=33",
        success: function (datos) {
            var lstXml = datos.getElementsByTagName("vta")[0];
            var lstCte = lstXml.getElementsByTagName("datos");
            for (var i = 0; i < lstCte.length; i++) {
                var objcte = lstCte[i];
                var datarow = {
                    RZN_ID: objcte.getAttribute("RZN_ID"),
                    RZN_CTE: objcte.getAttribute("RZN_CTE"),
                    RZN_NOMBRE: objcte.getAttribute("RZN_NOMBRE")
                };
                itemIdCob++;
                jQuery("#GRD_RZN").addRowData(itemIdCob, datarow, "last");
            }
            HistorialVtaCte(strCT_ID);
        }, error: function (objeto, quepaso, otroobj) {
            alert(":pto=33:" + objeto + " " + quepaso + " " + otroobj);
        }});
}
//historial de ventas
function dblClickVTA_cte(id) { //abre la venta con doble click desde el cliente!
//    document.getElementById("CT_GRID").value = "0"; //cte
    var strID_FAC_TKT = "";
    var strTipoDoc = "";
    var strNomMain = objMap.getNomMain();
    var grid = jQuery("#H_VENTA_CTE"); //grid detalle
    var lstVal = grid.getRowData(id);
    if (strNomMain == "H_VENTA_CTE") { //pantalla que lo contiene
        OpnEdit(document.getElementById("Ed" + strNomMain));
    } else {
        if (strNomMain == "C_TELEM") { //pantalla base
            if (lstVal.FAC_PAGADO != "PROMOCIÓN") {
                var strID_FAC_TKT = lstVal.PD_ID; //id fac o tkt
                var strTipoDoc = lstVal.DOC_TIPO; // tipo de documento           
                var strEstatus = lstVal.FAC_STATUS; // estatus de documento
                var strIDMOV = lstVal.PD_ID_MOV; // id movimiento
                //funcion
                document.getElementById("CT_ID_FACTKT").value = strID_FAC_TKT; // se guarda el id en un hidden           
                document.getElementById("CT_TIPODOC").value = strTipoDoc; // se guarda el id en un hidden           
                document.getElementById("CT_ESTATUS_VTA").value = strEstatus; // se guarda el estatus de la venta       
                document.getElementById("CT_ID_MOV").value = strIDMOV; // se guarda el estatus de la venta       
                document.getElementById("CT_GRID").value = "0"; //cte
                document.getElementById("CT_PROMOSION").value = "0";
                //Vta_Deta(strID_FAC_TKT, strTipoDoc);
                OpnDiagVentaD();
            } else {
                alert("Es una promoción.");
            }
        }
    }
}
function HistorialVtaCte(strCT_ID) { //carga el historial de venta
    var strPagado = "";
    var strStatus = "";
    var strTipoDoc = "";
    $("#dialogWait").dialog("open");
    var strPOST = "&intCTE=" + strCT_ID;
    $.ajax({
        type: "POST",
        data: strPOST,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "COFIDE_Historial.jsp?ID=3",
        success: function (datos) {
//            console.log("va a recarfar el grid");
            jQuery("#H_VENTA_CTE").clearGridData();
//            console.log("ya recargo el grid");
            var objsc = datos.getElementsByTagName("Ventas")[0];
            var lstVtas = objsc.getElementsByTagName("datos");
            for (var i = 0; i < lstVtas.length; i++) {
                var obj = lstVtas[i];
                //estatus de la venta
                if (obj.getAttribute("FAC_PROMO") == "1") {
                    strPagado = "PROMOCIÓN";
                } else {
                    if (obj.getAttribute("FAC_PAGADO") == 0 && obj.getAttribute("FAC_VALIDA") == 0 && obj.getAttribute("FAC_ARCHIVO") == "") {

                        if (obj.getAttribute("FACTURAR") == "1") {

                            strPagado = "SIN PAGO / P-FACT";
                        } else {

                            strPagado = "SIN PAGO";
                        }
                    }
                    if (obj.getAttribute("FAC_PAGADO") == 1 && obj.getAttribute("FAC_VALIDA") == 0 && obj.getAttribute("FAC_ARCHIVO") != "") {

                        if (obj.getAttribute("FACTURAR") == "1") {

                            strPagado = "REVISIÓN / P-FACT";
                        } else {

                            strPagado = "REVISIÓN";
                        }

                    }
                    if (obj.getAttribute("FAC_PAGADO") == 0 && obj.getAttribute("FAC_VALIDA") == 0 && obj.getAttribute("FAC_ARCHIVO") != "") {

                        if (obj.getAttribute("FACTURAR") == "1") {

                            strPagado = "DENEGADO / P-FACT";
                        } else {

                            strPagado = "DENEGADO";
                        }

                    }
                    if (obj.getAttribute("FAC_PAGADO") == 1 && obj.getAttribute("FAC_VALIDA") == 1 && obj.getAttribute("FAC_ARCHIVO") != "") {

                        if (obj.getAttribute("FACTURAR") == "1") {

                            strPagado = "PAGADO / P-FACT";
                        } else {

                            strPagado = "PAGADO";
                        }
                    }
                }
                if (obj.getAttribute("CANCEL") == 1 && obj.getAttribute("FAC_ANULADA") == 0) {
                    strStatus = "En proceso de cancelación";
                } else {
                    if (obj.getAttribute("FAC_ANULADA") == 1) {
                        strStatus = "Cancelada";
                    } else {
                        strStatus = "Activa";
                    }
                }

                if (obj.getAttribute("DOC_TIPO") == "0") {
                    strTipoDoc = "TICKET";
                }
                if (obj.getAttribute("DOC_TIPO") == "1") {
                    strTipoDoc = "FACTURA";
                }
                if (obj.getAttribute("DOC_TIPO") == "2") {
                    strTipoDoc = "RESERVACIÓN";
//                    strTipoDoc = "RESERVACI&Oacute;N";
                }
                var Row = {
                    HV_CONTADOR: obj.getAttribute("CONTADOR"),
                    PD_ID: obj.getAttribute("FAC_ID"),
                    PD_FECHA: obj.getAttribute("FAC_FECHA"),
                    PD_HORA: obj.getAttribute("FAC_HORA"),
                    PD_RAZONSOCIAL: obj.getAttribute("RAZONSOCIAL"),
                    EMP_ID: obj.getAttribute("AGENTE"),
                    FAC_TOTAL: obj.getAttribute("FAC_TOTAL"),
                    PD_FOLIO: obj.getAttribute("FAC_FOLIO"),
                    FAC_METODODEPAGO: obj.getAttribute("FAC_METODODEPAGO"),
                    PD_TOTAL: obj.getAttribute("FAC_TOTAL"),
                    DOC_TIPO: strTipoDoc,
                    FAC_PAGADO: strPagado,
                    FAC_STATUS: strStatus,
                    CT_ID: obj.getAttribute("CT_ID"),
                    SC_ID: obj.getAttribute("SC_ID"),
                    PD_ID_MOV: obj.getAttribute("FAC_ID_M")
                };
                itemIdVta++;
                jQuery("#H_VENTA_CTE").addRowData(itemIdVta, Row, "last");
            }
            getIndicadores();
            $("#dialogWait").dialog("close");
        }, error: function (objeto, quepaso, otroobj) {
            alert(":pto=3: " + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }});
}

function OpnDiagVentaD() { //abre el detalle de la venta
    var objSecModiVta = objMap.getScreen("NVENTA2");
    if (objSecModiVta != null) {
        objSecModiVta.bolActivo = false;
        objSecModiVta.bolMain = false;
        objSecModiVta.bolInit = false;
        objSecModiVta.idOperAct = 0;
    }
    OpnOpt("NVENTA2", "_ed", "dialog", false, false, true);
    $("#dialog").dialog({
        dialogClass: "no-close"
    });
}

//cancelar movimiento del ejecutivo, cancelar la venta
function CancelMovCte() {
    var strFAC_ID = 0; // id de la venta fac
    var strTipoDoc = ""; //tipo de documento
    var strStat = ""; // estatus
    var strTKT_ID = 0; // id del tkt
    var strPost = "";
    var grid = jQuery("#H_VENTA_CTE");
    if (grid.getGridParam("selrow") != null) {
        var lstRow = grid.getRowData(grid.getGridParam("selrow"));
        strFAC_ID = lstRow.PD_ID;
        strTipoDoc = lstRow.DOC_TIPO;
        strStat = lstRow.FAC_STATUS;
        $("#dialogWait").dialog("open");
        if (strStat == "Activa") {
            document.getElementById("SioNO_inside").innerHTML = "¿Estas seguro de cancelar este movimiento?";
            $("#SioNO").dialog("open");
            document.getElementById("btnSI").onclick = function () {
                $("#SioNO").dialog("close");
                //MANDA LA SOLICUTUD DE CANCELACIÓN DE LA VENTA
                peticionCancel(strFAC_ID, strTipoDoc);
            }; //fin de si o no SI
            document.getElementById("btnNO").onclick = function () {
                $("#SioNO").dialog("close");
            }; //sin de si o no NO
            $("#dialogWait").dialog("close");
        } else {
            if (strStat == "Cancelada") {
                alert("Este documento ya ha sido cancelado");
            } else {
                alert("Este documento se encuentra en proceso de cancelación");
            }
            $("#dialogWait").dialog("close");
        }
    } else {
        alert("Debe seleccionar una partida en la tabla \"HISTORIAL DE VENTAS\"");
    }
}
//historial de ventas


function checkInBound() {
    $("#dialogWait").dialog("open");
    var strPost = "";
    var strBase = "";
    $.ajax({
        type: "POST",
        data: strPost,
        scriptCharset: "UTF-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "html",
        url: "COFIDE_Telemarketing_vta.jsp?ID=11",
        success: function (datos) {
            datos = trim(datos);

            if (datos.substring(0, 2) == "OK") {
                strBase = datos.replace("OK", "");
                document.getElementById("CT_CLAVE_DDBB").value = strBase;

                if (strBase == "BE-INBOUND" || strBase == "BE-CORREO" || strBase == "BE-85" || strBase == "" || strBase == 'BE-AAA' || strBase == 'BE-5') {
//                    document.getElementById("CT_MKT").parentNode.parentNode.style.display = "";
//                    document.getElementById("CT_MKT").value = "0";
                    document.getElementById("CT_MKT").disabled = false;
                    document.getElementById("CT_NOMBRE_INVITO").parentNode.parentNode.style.display = "";

                } else {
//                    document.getElementById("CT_MKT").parentNode.parentNode.style.display = "none"; //solo para telemarketing inbound
                    document.getElementById("CT_NOMBRE_INVITO").parentNode.parentNode.style.display = "none";
                    document.getElementById("CT_MKT").disabled = true;
                }
                $("#dialogWait").dialog("close");
                consultaVta();
            } else {
                alert("GetBaseUsuario: " + datos);
                $("#dialogWait").dialog("close");
            }
            $("#dialogWait").dialog("close");
        }
    }); //fin del ajax
}//Fin isUnBound

function blockDatosInBound() {
    document.getElementById("CT_ID").readOnly = true;
    document.getElementById("CT_ID_CLIENTE").readOnly = true;
    document.getElementById("CT_RAZONSOCIAL").readOnly = true;
    document.getElementById("CT_NO_CLIENTE").readOnly = true;
    document.getElementById("CT_RFC").readOnly = true;
    document.getElementById("CT_COL").readOnly = true;
    document.getElementById("CT_CONTACTO").readOnly = true;
    document.getElementById("CT_CONTACTO2").readOnly = true;
    document.getElementById("CT_CORREO").readOnly = true;
    document.getElementById("CT_CORREO2").readOnly = true;
    document.getElementById("CT_BOLBASE").readOnly = true;
    document.getElementById("CT_HORA_INI").readOnly = true;
    document.getElementById("CT_CP").readOnly = true;
    document.getElementById("CT_COL").disabled = true;
    document.getElementById("CT_COLONIA_DB").readOnly = true;
    document.getElementById("CT_CALLE").readOnly = true;
    document.getElementById("CT_EDO").readOnly = true;
    document.getElementById("CT_MUNI").readOnly = true;
    document.getElementById("CT_NUM").readOnly = true;
    document.getElementById("CT_SEDE").disabled = true;
    document.getElementById("CT_GIRO").disabled = true;
    document.getElementById("CT_AREA").disabled = true;
    document.getElementById("CT_COMENTARIO").readOnly = true;
    document.getElementById("CT_ID_LLAMADA").readOnly = true;
    document.getElementById("CT_CONMUTADOR").readOnly = true;
    document.getElementById("CT_CONTACTO_ENTRADA").readOnly = true;
    document.getElementById("BTN_NEW").style.display = 'none';
    document.getElementById("BTN_EDIT").style.display = 'none';
    document.getElementById("BTN_DEL").style.display = 'none';
    document.getElementById("BTN_ADD").style.display = 'none';
    document.getElementById("BTN_CANCEL").style.display = 'none';
    document.getElementById("BTN_RNEW").style.display = 'none';
    document.getElementById("BTN_REDIT").style.display = 'none';
    document.getElementById("BTN_RDEL").style.display = 'none';
    document.getElementById("BTN_RSAVE").style.display = 'none';
    document.getElementById("BTN_RCANCEL").style.display = 'none';
}//Fin blockDatosInBound

function UnlockDatosInBound() {
    document.getElementById("CT_ID").readOnly = false;
    document.getElementById("CT_ID_CLIENTE").readOnly = false;
    document.getElementById("CT_RAZONSOCIAL").readOnly = false;
    document.getElementById("CT_NO_CLIENTE").readOnly = false;
    document.getElementById("CT_RFC").readOnly = false;
    document.getElementById("CT_COL").readOnly = false;
    document.getElementById("CT_CONTACTO").readOnly = false;
    document.getElementById("CT_CONTACTO2").readOnly = false;
    document.getElementById("CT_CORREO").readOnly = false;
    document.getElementById("CT_CORREO2").readOnly = false;
    document.getElementById("CT_BOLBASE").readOnly = false;
    document.getElementById("CT_HORA_INI").readOnly = false;
    document.getElementById("CT_CP").readOnly = false;
    document.getElementById("CT_COL").disabled = false;
    document.getElementById("CT_COLONIA_DB").readOnly = false;
    document.getElementById("CT_CALLE").readOnly = false;
    document.getElementById("CT_EDO").readOnly = false;
    document.getElementById("CT_MUNI").readOnly = false;
    document.getElementById("CT_NUM").readOnly = false;
    document.getElementById("CT_SEDE").disabled = false;
    document.getElementById("CT_GIRO").disabled = false;
    document.getElementById("CT_AREA").disabled = false;
    document.getElementById("CT_COMENTARIO").readOnly = false;
    document.getElementById("CT_ID_LLAMADA").readOnly = false;
    document.getElementById("CT_CONMUTADOR").readOnly = false;
    document.getElementById("CT_CONTACTO_ENTRADA").readOnly = false;
    document.getElementById("BTN_NEW").style.display = 'block';
    document.getElementById("BTN_EDIT").style.display = 'block';
    document.getElementById("BTN_DEL").style.display = 'block';
    document.getElementById("BTN_ADD").style.display = 'block';
    document.getElementById("BTN_CANCEL").style.display = 'block';
    document.getElementById("BTN_RNEW").style.display = 'block';
    document.getElementById("BTN_REDIT").style.display = 'block';
    document.getElementById("BTN_RDEL").style.display = 'block';
    document.getElementById("BTN_RSAVE").style.display = 'block';
    document.getElementById("BTN_RCANCEL").style.display = 'block';
}//Fin blockDatosInBound

function hiddeFunctionInboundSave() {
//drawRightPanel();
    var strHtmlBTN = "<center><table border='0' width='0%' align='center'>" //botones de agregar nuevo cliente, error en base, actualizar 
            + "<tr>"
            + "<td>&nbsp;</td>"
            + "<tr>"
            + '<td><a href="javascript:OpnNewCte()" class=\'cofide_venta\'><i class = "fa fa-user-plus" title="Agregar Prospecto" style="font-size:50px; width:110px"></i></td>'
            + "</tr>";
    "</table></center>";
    document.getElementById("btnadd").innerHTML = strHtmlBTN; //botones de marcar y mandar email

    var strHtmlTitle = "<table border='0' width='0%' align='center'>"
            + "<tr>"
            + '<td><a href="javascript:OpnMotorBusqueda()" class=\'cofide_search\'><i class = "fa fa-search-plus" title="Motor de Busqueda" style="font-size:30px; width:110px"></i></td>'
            + "<td>&nbsp;</td>"
            + '<td><a href="javascript:tmp_Guardar()" class=\'cofide_changei\'><i class = "fa fa-step-forward" title="Cambiar Llamada" style="font-size:30px; width:110px"></i></td>'
            + "<td>&nbsp;</td>"
            + '<td><a href="javascript:OpnDiagHCall()" class=\'cofide_histl\'><i class = "fa fa-clock-o" title="Historial de Llamadas" style="font-size:30px; width:110px"></i></td>'
            + "<td>&nbsp;</td>"
            + '<td><a href="javascript:OpnDiagHVenta()" class=\'cofide_histv\'><i class = "fa fa-money" title="Historial de Ventas" style="font-size:30px; width:110px"></i></td>'
            + "<td>&nbsp;</td>"
            + '<td><a href="javascript:OpnDiagCActivo()" class=\'cofide_calcurso\'><i class = "fa fa-calendar" title="Calendario de Cursos" style="font-size:30px; width:110px"></i></td>'
            + "<td>&nbsp;</td>"
            + '<td><a href="javascript:sendMailGroup()" class=\'cofide_message\'><i class = "fa fa-at" title="Mail Group"style="font-size:40px; width: 110px"></i></a></td>'
            + "<td>&nbsp;</td>"
            + '<td><a href="javascript:OpnPausarLlamada()" class=\'cofide_pausa\'><i class = "fa fa-pause" title="Pausar Llamada" style="font-size:30px; width:110px"></i></td>'
            + "<td>&nbsp;</td>"
            + '<td><a href="javascript:OpnSalirLlamada()" class=\'cofide_salida\'><i class = "fa fa-sign-out" title="Salir"  style="font-size:30px; width:110px"></i></td>'
            + "</tr>";
    "</table>";
    document.getElementById("CT_TITLEBTN").innerHTML = strHtmlTitle;
}

function showBtnSaveInBound() {
    var strHtmlBTN = "<center><table border='0' width='0%' align='center'>" //botones de agregar nuevo cliente, error en base, actualizar 
            + "<tr>"
            + "<td>&nbsp;</td>"
            + "<tr>"
            + '<td><a href="javascript:OpnNewCte()" class=\'cofide_venta\'><i class = "fa fa-user-plus" title="Agregar Prospecto" style="font-size:50px; width:110px"></i></td>'
            + "<td>&nbsp;</td>";
    if (document.getElementById("exp_pro").value == 1) {
        strHtmlBTN += '<td><a href="javascript:errorbase()" class=\'cofide_err\'><i class = "fa fa-trash" title="Error en Base" style="font-size:50px; width:110px"></i></td>'
                + "<td>&nbsp;</td>";
    }

    strHtmlBTN += '<td><a href="javascript:UpDateCte()" class=\'updatecte\'><i class = "fa fa-refresh" title="Actualiza Cliente" style="font-size:50px; width:110px"></i></td>'
            + "<td>&nbsp;</td>"
            + "</tr>";
    "</table></center>";
    document.getElementById("btnadd").innerHTML = strHtmlBTN; //botones de marcar y mandar email
    var strHtmlTitle = "<table border='0' width='0%' align='center'>"
            + "<tr>"
            + '<td><a href="javascript:OpnMotorBusqueda()" class=\'cofide_search\'><i class = "fa fa-search-plus" title="Motor de Busqueda" style="font-size:30px; width:110px"></i></td>'
            + "<td>&nbsp;</td>"
            + '<td><a href="javascript:tmp_Guardar()" class=\'cofide_changei\'><i class = "fa fa-step-forward" title="Cambiar Llamada" style="font-size:30px; width:110px"></i></td>'
            + "<td>&nbsp;</td>"
            + '<td><a href="javascript:OpnDiagHCall()" class=\'cofide_histl\'><i class = "fa fa-clock-o" title="Historial de Llamadas" style="font-size:30px; width:110px"></i></td>'
            + "<td>&nbsp;</td>"
            + '<td><a href="javascript:OpnDiagNVenta(\'0\')" class=\'cofide_venta\'><i class = "fa fa-cart-plus" title="Ventas" style="font-size:30px; width:110px"></i></td>'
            + "<td>&nbsp;</td>"
            + '<td><a href="javascript:OpnDiagHVenta()" class=\'cofide_histv\'><i class = "fa fa-money" title="Historial de Ventas" style="font-size:30px; width:110px"></i></td>'
            + "<td>&nbsp;</td>"
            + '<td><a href="javascript:OpnDiagCActivo()" class=\'cofide_calcurso\'><i class = "fa fa-calendar" title="Calendario de Cursos" style="font-size:30px; width:110px"></i></td>'
            + "<td>&nbsp;</td>"
            + '<td><a href="javascript:sendMailGroup()" class=\'cofide_message\'><i class = "fa fa-at" title="Mail Group"style="font-size:40px; width: 110px"></i></a></td>'
            + "<td>&nbsp;</td>"
            + '<td><a href="javascript:saveDetaTMK()" class=\'cofide_guarda\'><i class = "fa fa-floppy-o" title="Guardar Cambios" style="font-size:30px; width:110px"></i></td>'
            + "<td>&nbsp;</td>"
            + '<td><a href="javascript:OpnPausarLlamada()" class=\'cofide_pausa\'><i class = "fa fa-pause" title="Pausar Llamada" style="font-size:30px; width:110px"></i></td>'
            + "<td>&nbsp;</td>"
            + '<td><a href="javascript:OpnSalirLlamada()" class=\'cofide_salida\'><i class = "fa fa-sign-out" title="Salir"  style="font-size:30px; width:110px"></i></td>'
            + "</tr>";
    "</table>";
    document.getElementById("CT_TITLEBTN").innerHTML = strHtmlTitle;
}

function selectBaseCliente() {
    $("#dialogWait").dialog("open");
    $.ajax({
        type: "POST",
        data: "keys=758",
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "Acceso.do",
        success: function (datos) {
            var objsc = datos.getElementsByTagName("Access")[0];
            var lstKeys = objsc.getElementsByTagName("key");
            for (i = 0; i < lstKeys.length; i++) {
                var obj = lstKeys[i];
                if (obj.getAttribute('id') == 758 && obj.getAttribute('enabled') == "true") {
                    opnShowBasesClientes();
//                    document.getElementById("CT_MKT").parentNode.parentNode.style.display = "";
                    document.getElementById("CT_PERMISO_INBOUND").value = 1;
                } else {
                    document.getElementById("CT_PERMISO_INBOUND").value = 0;
                }
            }
            $("#dialogWait").dialog("close");
        },
        error: function (objeto, quepaso, otroobj) {
            alert(":pto= Acceso.do:" + objeto + " " + quepaso + " " + otroobj);
        }
    });
}//Fin selectBaseCliente

function opnShowBasesClientes() {
    var objSecModiVta = objMap.getScreen("SELECT_BASE");
    if (objSecModiVta != null) {
        objSecModiVta.bolActivo = false;
        objSecModiVta.bolMain = false;
        objSecModiVta.bolInit = false;
        objSecModiVta.idOperAct = 0;
    }
    OpnOpt('SELECT_BASE', '_ed', 'dialogCte', false, false, true);
}//Fin opnShowBasesClientes

function initSelectBaseCt() {
    var strHtmlBTN = "<center><table border='0' width='0%' align='center'>" //botones de agregar nuevo cliente, error en base, actualizar 
            + "<tr>"
            + "<td>&nbsp;</td>"
            + "<tr>"
            + '<td><a href="javascript:setBaseUsuario()" class=\'cofide_venta\'><i class = "fa fa-check" title="Confirmar" style="font-size:50px; width:110px"></i></td>'
            + "<td>&nbsp;</td>"
            + '<td><a href="javascript:exitSelectBaseCt()" class=\'cofide_err\'><i class = "fa fa-sign-out" title="Salir" style="font-size:50px; width:110px"></i></td>'
            + "<td>&nbsp;</td>"
            + "</tr>";
    "</table></center>";
    document.getElementById("DIV_BASE_USER").innerHTML = strHtmlBTN;
    SeleccionaBaseUsuarioInbound();
}//Fin initSelectBaseCt

function setBaseUsuario() {
    if (document.getElementById("BaseUserSelect").value != "0" && document.getElementById("BaseUserSelect").value != 0) {
        document.getElementById("CT_CLAVE_DDBB").value = document.getElementById("BaseUserSelect").value;
        $("#dialogCte").dialog("close");
    } else {
        alert("Seleccione una Base")
    }
}//Fin setBaseUsuario

function exitSelectBaseCt() {
    $("#dialogCte").dialog("close");
    myLayout.open("west");
    myLayout.open("east");
    myLayout.open("south");
    myLayout.open("north");
    document.getElementById("MainPanel").innerHTML = "";
    //Limpiamos el objeto en el framework para que nos deje cargarlo enseguida
    var objMainFacPedi = objMap.getScreen("C_TELEM");
    objMainFacPedi.bolActivo = false;
    objMainFacPedi.bolMain = false;
    objMainFacPedi.bolInit = false;
    objMainFacPedi.idOperAct = 0;
}//Fin exitSelectBaseCt

function SeleccionaBaseUsuarioInbound() {

    var strOptionSelect = "<option value='0'>Seleccione</option>";
    var strHTML = "<table cellpadding=\"4\" cellspacing=\"1\" border=\"0\" >BODEGA: ";
    strHTML += " <td><select style=\"font-size:15pt\" id=\"BaseUserSelect\" name=\"BaseUserSelect\"  class=\"outEdit\" onblur=\"QuitaFoco(this)\" onfocus=\"PonFoco(this)\" 0=\"\" > " + strOptionSelect + " < /select></td>";
    strHTML += "  </table>";
    document.getElementById("SELECT_BASE_USER").innerHTML = strHTML;
    var strOptionSelect = "<option value='0'>Seleccione</option>";
    var strHTML = "<table style=\"font-size:15pt\" cellpadding=\"4\" cellspacing=\"1\" border=\"0\" >BASE CLIENTES: <br>";
    $.ajax({
        type: "POST",
        data: "",
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "COFIDE_Telemarketing.jsp?ID=34",
        success: function (datos) {
            var lstXml = datos.getElementsByTagName("BasesCt")[0];
            var lstprecio = lstXml.getElementsByTagName("datos");
            for (var i = 0; i < lstprecio.length; i++) {
                var obj = lstprecio[i];
                strOptionSelect += "<option value='" + obj.getAttribute("COFIDE_CODIGO") + "'>" + obj.getAttribute("COFIDE_CODIGO") + "</option>";
            }
            strHTML += " <select style=\"font-size:15pt\" id=\"BaseUserSelect\" name=\"BaseUserSelect\"  class=\"outEdit\" onblur=\"QuitaFoco(this)\" onfocus=\"PonFoco(this)\" 0=\"\" > " + strOptionSelect + " < /select>";
            strHTML += "  </table>";
            document.getElementById("SELECT_BASE_USER").innerHTML = strHTML;
        },
        error: function (objeto, quepaso, otroobj) {
            alert(":pto=34:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }
    });
}//Fin SeleccionaBaseUsuarioInbound
function HtmlButtonCte() {
    var strNomMain = objMap.getNomMain(); // obtenemos la pantalla
    if (strNomMain != "VAL_DATOS") {
        var strCtes = "0";
        var strProsp = "0";
        var strBaseCT = document.getElementById("CT_CLAVE_DDBB").value;
        $("#dialogWait").dialog("open");
        if (strBaseCT != "") {
            $.ajax({
                type: "POST",
                data: "CT_BASE=" + strBaseCT,
                scriptCharset: "utf-8",
                contentType: "application/x-www-form-urlencoded;charset=utf-8",
                cache: false,
                dataType: "xml",
                url: "COFIDE_Telemarketing.jsp?ID=39",
                success: function (datos) {
                    var lstXml = datos.getElementsByTagName("datos")[0];
                    var lstprecio = lstXml.getElementsByTagName("cte");
                    for (var i = 0; i < lstprecio.length; i++) {
                        var obj = lstprecio[i];
                        strCtes = obj.getAttribute("cliente");
                        strProsp = obj.getAttribute("prospecto");
                    }
                    var strHTMLcte = "<table border='0' width='0%' align='center'>"
                            + "<tr>"
                            + '<td><a href="javascript:OpnSearchCustomer()" style=\'color: gray\'><i class = "fa fa-user" title="Buscar Prospectos" style="font-size:30px; width:110px">' + strProsp + '</i></td>'
                            + "<td>&nbsp;</td>"
                            + '<td><a href="javascript:OpnSearchCustomer2()" style=\'color: green\'><i class = "fa fa-user" title="Buscar Ex-Participantes" style="font-size:30px; width:110px"> ' + strCtes + '</i></td>'
                            + "<td>&nbsp;</td>"
                            + "</tr>";
                    "</table>";
                    $("#dialogWait").dialog("close");
                    document.getElementById("MB_CTE").innerHTML = strHTMLcte;
                },
                error: function (objeto, quepaso, otroobj) {
                    alert(":pto=39: " + objeto + " " + quepaso + " " + otroobj);
                    $("#dialogWait").dialog("close");
                }
            });
        }
    }
}
/**
 * valida los correos que estan 
 * dentro del grid de contactos 
 * para no duplicarlos dentro de 
 * un mismo cliente
 * @returns {Boolean}
 */
function validaCorreo() {
    var bolDuplicado = false;
    var strCorreo = document.getElementById("CCO_CORREO").value;
    var strCorreo2 = document.getElementById("CCO_CORREO2").value;
    var grid = jQuery("#GRIDCONTACTOS");
    var idArr = grid.getDataIDs();
    for (var i = 0; i < idArr.length; i++) {
        var id = idArr[i];
        var lstRow = grid.getRowData(id);
        if (strCorreo != "") {
            if (lstRow.CCO_CORREO == strCorreo) {
                bolDuplicado = true;
            }
            if (lstRow.CCO_CORREO2 == strCorreo) {
                bolDuplicado = true;
            }
        }
        if (strCorreo2 != "") {
            if (lstRow.CCO_CORREO == strCorreo2) {
                bolDuplicado = true;
                document.getElementById("CCO_CORREO2").select();
            }
            if (lstRow.CCO_CORREO2 == strCorreo2) {
                bolDuplicado = true;
                document.getElementById("CCO_CORREO2").select();
            }
        }
    }
    return bolDuplicado;
}

function beforeAddContacMail1(opc, row) {
    var intIDCte = "0";
    var strCorreo = "";
    if (opc == 3) {
        if (!validaCorreo()) {
            strCorreo = document.getElementById("CCO_CORREO").value;
            intIDCte = document.getElementById("CT_NO_CLIENTE").value;
        }
    }
    if (opc == 4) {
        if (!validaCorreo()) {
            strCorreo = document.getElementById("CCO_CORREO2").value;
            intIDCte = document.getElementById("CT_NO_CLIENTE").value;
        }
    }
    if (strCorreo != "") {
        var strPost = "correo=" + strCorreo;
        strPost += "&idcte=" + intIDCte;
        var strRespuestaLN = "";
        var strRespuestaDup = "";
        $.ajax({
            type: "POST",
            data: strPost,
            scriptCharset: "UTF-8",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "xml",
            url: "COFIDE_Telemarketing.jsp?ID=24",
            success: function (datos) {
                var lstXml = datos.getElementsByTagName("vta")[0];
                var lstCte = lstXml.getElementsByTagName("datos");
                for (var i = 0; i < lstCte.length; i++) {
                    var objcte = lstCte[i];
                    strRespuestaLN = objcte.getAttribute("listanegra");
                    strRespuestaDup = objcte.getAttribute("duplicado");
                    if (strRespuestaLN == "0" && strRespuestaDup == "0") {
                        beforeAddContacMailAlt(opc, row);
                    } else {
                        if (strRespuestaDup == "1") {
                            alert("El Correo esta Duplicado");
                        }
                        if (strRespuestaLN == "1") {
                            alert("El Correo esta en la lista negra");
                        }
                    }
                }
            }});
    }
}

function beforeAddContacMailAlt(opc, row) {
    var intIDCte = "0";
    var strCorreo = "";
    if (opc == 3) {
        if (!validaCorreo()) {
            strCorreo = document.getElementById("CCO_CORREO").value;
            intIDCte = document.getElementById("CT_NO_CLIENTE").value;
        } else {
            alert("Este correo ya existe en este cliente");
        }
    }
    if (opc == 4) {
        if (!validaCorreo()) {
            strCorreo = document.getElementById("CCO_CORREO2").value;
            intIDCte = document.getElementById("CT_NO_CLIENTE").value;
        } else {
            alert("Este correo ya existe en este cliente");
        }
    }
    if (strCorreo != "") {
        var strPost = "correo=" + strCorreo;
        strPost += "&idcte=" + intIDCte;
        var strRespuestaLN = "";
        var strRespuestaDup = "";
        $.ajax({
            type: "POST",
            data: strPost,
            scriptCharset: "UTF-8",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "xml",
            url: "COFIDE_Telemarketing.jsp?ID=24",
            success: function (datos) {
                var lstXml = datos.getElementsByTagName("vta")[0];
                var lstCte = lstXml.getElementsByTagName("datos");
                for (var i = 0; i < lstCte.length; i++) {
                    var objcte = lstCte[i];
                    strRespuestaLN = objcte.getAttribute("listanegra");
                    strRespuestaDup = objcte.getAttribute("duplicado");
                    if (strRespuestaLN == "0" && strRespuestaDup == "0") {
                        hideContactosTMK();
                        jQuery("#GRIDCONTACTOS").addRowData(getMaxGridContactos("#GRIDCONTACTOS"), row, "last");
                    } else {
                        if (strRespuestaDup == "1") {
                            alert("El Correo esta Duplicado");
                        }
                        if (strRespuestaLN == "1") {
                            alert("El Correo esta en la lista negra");
                        }
                    }
                }
            }});
    }
}

function getMaxGridContactos(strNomGr) {
    var intLenght = jQuery(strNomGr).getDataIDs().length + 1;
    return intLenght;
}//Fin getMaxGridCursosMaterial

function VtaAnulFactCte(FAC_ID) {
    if (FAC_ID != 0) {
        $.ajax({
            type: "POST",
            data: encodeURI("idAnul=" + FAC_ID),
            scriptCharset: "utf-8",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "html",
            url: "VtasMov.do?id=4",
            success: function (dato) {
                dato = trim(dato);
                if (dato != "OK") {
                    alert(dato);
                }
                $("#dialogWait").dialog("close");
            }, error: function (objeto, quepaso, otroobj) {
                alert(":pto=4: " + objeto + " " + quepaso + " " + otroobj);
                $("#dialogWait").dialog("close");
            }});
    }
}

function intDato(strDato) {
//    strDato = parseInt(strDato);
//    if (isNaN(strDato)) {
//        strDato = 0;
//    }
    return strDato;
}

function validaFormatoCorreo(strCorreo, strCampo) {
    var expr = /^([a-zA-Z0-9_Ññ\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
    if (strCorreo != "") {
        if (!expr.test(strCorreo)) {
            if (!strCampo == "CT_CORREO" || !strCorreo == "CT_CORREO2") {
                document.getElementById("BTN_ADD").disabled = true;
            }
//            alert("El formato del correo es incorrecto. ");
            document.getElementById(strCampo).style.borderColor = "#ff0000";
            return true;
        } else {
            return false;
        }
    }
}
function validaNumero(strNumero) {
    if (isNaN(strNumero)) { //si es true, es texto
        return false;
    } else { // si es false, es numero
        return true;
    }
}
/**
 * obtiene el tipo de plantilla que enviara
 * @returns {undefined}
 */
function llenaTemplate() {
    var strPost = document.getElementById("CT_TIPO_MAIL").value;
    if (strPost != "0") {
        $.ajax({
            type: "POST",
            data: "tipo_mail=" + strPost,
            scriptCharset: "UTF-8",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "xml",
            url: "COFIDE_Telemarketing.jsp?ID=42",
            success: function (datos) {
                var objHoraCombo = document.getElementById("CT_MAILMENSUAL");
                select_clear(objHoraCombo);
                var lstXml = datos.getElementsByTagName("tipos")[0];
                var lstCte = lstXml.getElementsByTagName("tipo");
                select_add(objHoraCombo, "Seleccione...", "");
                for (var i = 0; i < lstCte.length; i++) {
                    var objHora = lstCte[i];
                    select_add(objHoraCombo, objHora.getAttribute("NameTipo"), objHora.getAttribute("id_tipo"));
                }
            }});
    } else {
        select_clear(document.getElementById("CT_MAILMENSUAL"));
        select_add(document.getElementById("CT_MAILMENSUAL"), "Seleccione...", "");
    }
}
//Muestra los cursos del Instructor
function tabShowMeses(event, ui) {
    var index = ui.newTab.index();
    if (index === 1) {
//        llenaTemplate(); //seleccionar tipo
    }
}

function MensualIndividual(opc) {
    if (opc == 1) { //selecciona curso individual pone en blanco el tipo de mail
        document.getElementById("CT_TIPO_MAIL").value = "0";
        select_clear(document.getElementById("CT_MAILMENSUAL"));
        select_add(document.getElementById("CT_MAILMENSUAL"), "Seleccione...", "");
    }
    if (opc == 2) { //selecciona mensual, pone en blanco el curso individual
        document.getElementById("CC_CURSO_NOMBRE").value = "";
        llenaTemplate();
    }
    if (opc == 3) { //selecciona tipo mail, pone en blanco el curso individual
        document.getElementById("CC_CURSO_NOMBRE").value = "";
    }
}
//MANDA AL MODULO DE CONTABILIDAD PARA QUE CANCELE LAS FACTURAS
function peticionCancel(strIdFac, strTipoDoc) {
    var strPost = "";
    if (strIdFac != "0") {

        strPost = "idAnul=" + strIdFac;
        strPost += "&tipo_doc=" + strTipoDoc;

        $.ajax({
            type: "POST",
            data: strPost,
            scriptCharset: "utf-8",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "html",
            url: "COFIDE_ValFac_Duplicados.jsp?id=8",
            beforeSend: function () {
                $("#dialogWait").dialog("open");
            },
            success: function (dato) {
                dato = trim(dato);
                if (dato != "OK") {
                    alert("Error: " + dato);
                } else {
                    var grid = jQuery("#H_VENTA_CTE"); //grid detalle
                    var id = grid.getGridParam("selrow");
                    var lstVal = grid.getRowData(id);
                    if (lstVal.FAC_PAGADO != "PROMOCIÓN") {
                        alert("Se solicitó con éxito la cancelación de la venta con ID: " + strIdFac);
                        QuitaParticipantePts(strIdFac, strTipoDoc); //anula los participantes
                    }
                    HistorialVtaCte(document.getElementById("CT_ID").value);
                    LoadContacto(document.getElementById("CT_ID").value, 0);
                }
                $("#dialogWait").dialog("close");
            }, error: function (objeto, quepaso, otroobj) {
                alert(":pto=8: " + objeto + " " + quepaso + " " + otroobj);
                $("#dialogWait").dialog("close");
            }});
    }
}


//Muestra la funcion de facturar
function EnviaFacturaMail(opc) {
    var grid = jQuery("#H_VENTA_CTE");
    var id = grid.getGridParam("selrow");
    var idMovi = "0";

    if (grid.getGridParam("selrow") != null) {
        var lstRow = grid.getRowData(id);
        $("#dialogWait").dialog("open");
        //Construir cadena a enviar
        var strPD_ID = lstRow.PD_ID;
        var idMovi = lstRow.PD_ID_MOV;

        if (opc == 1) { //factura
            if (lstRow.DOC_TIPO == "FACTURA") {

                SendFacturaXML(strPD_ID);
                $("#dialogGen").dialog("close");

            } else {
                alert("Esta venta no es una FACTURA.");
            }
        }
        if (opc == 2) { //ticket
            if (lstRow.DOC_TIPO == "TICKET") {

                SendTicketTMK(strPD_ID);
                $("#dialogGen").dialog("close");

            } else {
                alert("Esta venta no es un TICKET.");
            }
        }
        if (opc == 3) { //RESERVACIÓN

            sendReservaConfirm(strPD_ID, idMovi, 1);
            $("#dialogGen").dialog("close");

        }
        if (opc == 4) { //CONFIRMACIÓN
            if (lstRow.FAC_PAGADO.includes("REVISIÓN") || lstRow.FAC_PAGADO.includes("PAGADO")) {

                sendReservaConfirm(strPD_ID, idMovi, 2);
                $("#dialogGen").dialog("close");

            } else {
                alert("Esta venta aun no ha sido pagada.");
            }

        }
        $("#dialogWait").dialog("close");
    } else {
        alert("Selecciona una venta.");
    }
}

// llamar a telefonos indivuduales
function LlamarContacto_(strTelefono) {
//    console.log(strTelefono);
    document.getElementById("CT_RESPOND").value = ""; //vuelve a marcar y regresa la respuesta en "" y activa el timer para leerlo de nuevo
    var intCT_ID = document.getElementById("CT_NO_CLIENTE").value;
    var strPhone = strTelefono;
    var strPost = "";
    strPost += "CT_TELEFONO=" + strPhone;
    strPost += "&CT_ID=" + intCT_ID;
    $.ajax({
        type: "POST",
        data: strPost,
        scriptCharset: "UTF-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "COFIDE_Telemarketing.jsp?ID=20",
        success: function (datos) {
            var lstXml = datos.getElementsByTagName("vta")[0];
            var lstCte = lstXml.getElementsByTagName("datos");
            for (var i = 0; i < lstCte.length; i++) {
                var objcte = lstCte[i];
                document.getElementById("CT_ID_LLAMADA").value = objcte.getAttribute("id_llamada"); //recupera el ID de la llamada nueva
            }
            clearTimeout(timTimer);
            Timer(); //valida la llamda actual si ya termino
        }
    });
}

// ANULA A LOS PARTICIPANTES Y ELIMINA LOS PUNTOS QUE ACUMULO DURANTE ESA VENTS
function QuitaParticipantePts(strFAC_ID, strTipoDoc) {

    var strPost = "";
    strPost = "fac_id=" + strFAC_ID;
    strPost += "&tipo_doc=" + strTipoDoc;
    $.ajax({
        type: "POST",
        data: strPost,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "html",
        url: "COFIDE_Tmk_vta.jsp?ID=10",
        success: function (datos) {
            datos = trim(datos);
            if (datos == "OK") {
                $("#dialogWait").dialog("close");
            } else {
                alert("hubo un problema con los puntos de los participantes");
                $("#dialogWait").dialog("close");
            }
        }, error: function (objeto, quepaso, otroobj) {
            alert(":pto=10: " + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }});
}


/*
 * Envia por correo
 * la tarjeta de 
 * cliente preferente al contacto
 */
function SendTarjetaCtPreferente() {
    var strPost = "";
    var grid = jQuery("#GRIDCONTACTOS");
    var ids = grid.getGridParam("selrow");
    if (ids !== null) {
        var lstRow1 = grid.getRowData(ids);
        var strContactoId = lstRow1.CONTACTO_ID;
        var strCorreo = lstRow1.CCO_CORREO;
        if (strCorreo != "") {
            strPost += "&strContactoId=" + strContactoId;
            $("#dialogWait").dialog("open");
            $.ajax({
                type: "POST",
                data: strPost,
                scriptCharset: "UTF-8",
                contentType: "application/x-www-form-urlencoded;charset=utf-8",
                cache: false,
                dataType: "html",
                url: "COFIDE_Programacionmails.jsp?ID=12",
                success: function (datos) {
                    datos = trim(datos);
                    if (datos == "OK") {
                        alert("Mensaje Enviado");
                    } else {
                        alert("ERROR: " + datos);
                    }
                    $("#dialogWait").dialog("close");
                }, error: function (objeto, quepaso, otroobj) {
                    alert(":pto=12:" + objeto + " " + quepaso + " " + otroobj);
                    $("#dialogWait").dialog("close");
                }});
        } else {
            alert("el Contacto no tiene un correo");
        }
    } else {
        alert("Selecciona un contacto!");
    }
}//Fin SendTarjetaCtPreferente

/*
 * muestra a los participantes, que ya cuentan con puntos para la promoción
 */
function showGanadores(strParticipante) {

    var strLenght = strParticipante.length; //tamaño del arreglo    
    var strHtml = "<div style=\"width: 400px; height: 150px\">";
    strHtml += "<div style=\"text-align: center\">PARTICIPANTES CON PROMOCIÓN</div><br>";
    strHtml += "<table border= 0>";
    for (var i = 0; i < strLenght; i++) {
        strHtml += "<tr>";
        strHtml += "<td><div style=\"font-size: 12px; bold;\"><i style=\"color: #99cc00; font-size: 30px\"class=\"fa fa-gift\">&nbsp;&nbsp;</i>" + strParticipante[i] + "</div></td>"; //participante
        strHtml += "</tr>";
        strHtml += "<tr><td>&nbsp;</td></tr>";
    }
    strHtml += "</table>";
    strHtml += "<div style=\"text-align: center\">";
    strHtml += "<input type=button onclick='cerrarShowGanadores()' value='¡Enterado!.'>";
    strHtml += "</div>";
    strHtml += "</div>";
    if (strLenght != 0) {
        document.getElementById("dialogPCaptura_inside").innerHTML = strHtml;
        $("#dialogPCaptura").dialog("option", "title", "Promoción");
        $("#dialogPCaptura").dialog("open");
    }
}
function cerrarShowGanadores() {
    $("#dialogPCaptura").dialog("close");
}

function loadDatosFact_(intIdCte) {
    var itemIdFactDat = 0;
//    var intIdCte = document.getElementById("CT_NO_CLIENTE").value;
    var strPost = "";
    strPost += "CT_NO_CLIENTE=" + intIdCte;
    $.ajax({
        type: "POST",
        data: strPost,
        scriptCharset: "UTF-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "COFIDE_Telemarketing_vta.jsp?ID=2",
        success: function (datos) {
            jQuery("#GRD_RZN").clearGridData();
            var lstXml = datos.getElementsByTagName("vta")[0];
            var lstCte = lstXml.getElementsByTagName("datos");
            for (var i = 0; i < lstCte.length; i++) {
                var objcte = lstCte[i];
                var datarow = {
                    CEV_ID_FAC: objcte.getAttribute("CEV_ID"),
                    CEV_RAZONSOCIAL: objcte.getAttribute("CEV_NOMBRE"),
                    CEV_NUMERO: objcte.getAttribute("CEV_NUMERO"),
                    CEV_NUMINT: objcte.getAttribute("CEV_NUMINT"),
                    CEV_RFC: objcte.getAttribute("CEV_RFC"),
                    CEV_CALLE: objcte.getAttribute("CEV_CALLE"),
                    CEV_COLONIA: objcte.getAttribute("CEV_COLONIA"),
                    CEV_MUNICIPIO: objcte.getAttribute("CEV_MUNICIPIO"),
                    CEV_ESTADO: objcte.getAttribute("CEV_ESTADO"),
                    CEV_CP: objcte.getAttribute("CEV_CP"),
                    CEV_EMAIL1: objcte.getAttribute("CEV_EMAIL1"),
                    CEV_EMAIL2: objcte.getAttribute("CEV_EMAIL2"),
                    CEV_TELEFONO: objcte.getAttribute("CEV_TELEFONO")};
                itemIdFactDat++;
                jQuery("#GRD_RZN").addRowData(itemIdFactDat, datarow, "last");
            }
            console.log("historial de ventas");
            HistorialVtaCte(intIdCte);
        }, error: function (objeto, quepaso, otroobj) {
            alert(":pto=2:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }
    });
}

//actualiza directamente al agregar contacto o editarlo
function UpdateContacto(tmpOperacion, intIdContacto, strNombre, strApPat, strApMat, strTitulo, strSocio, strArea, strAsociacion, strCorreo, strCorreo2, strTelefono, strExt, strAlt, intMail1, intMail2) {
    // strEdición = "C" actualiza
    var strPost = "";
    strPost = "edicion=" + tmpOperacion;
    strPost += "&id_contacto=" + intIdContacto;
    strPost += "&nombre=" + strNombre;
    strPost += "&appat=" + strApPat;
    strPost += "&apmat=" + strApMat;
    strPost += "&titulo=" + strTitulo;
    strPost += "&socio=" + strSocio;
    strPost += "&area=" + strArea;
    strPost += "&asoc=" + strAsociacion;
    strPost += "&correo=" + strCorreo;
    strPost += "&correo2=" + strCorreo2;
    strPost += "&telefono=" + strTelefono;
    strPost += "&ext=" + strExt;
    strPost += "&alterno=" + strAlt;
    strPost += "&mail1=" + intMail1;
    strPost += "&mail2=" + intMail2;
    strPost += "&ct_id=" + document.getElementById("CT_ID").value;

    $("#dialogWait").dialog("open");
    $.ajax({
        type: "POST",
        data: strPost,
        scriptCharset: "UTF-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "html",
        url: "COFIDE_Telemarketing.jsp?ID=45",
        success: function (datos) {
            datos = trim(datos);
            if (datos == "OK") {
                LoadContacto(document.getElementById("CT_ID").value, 1);
            } else {
                alert("ERROR[ al actualizar registro ]: " + datos);
            }
            $("#dialogWait").dialog("close");
        }, error: function (objeto, quepaso, otroobj) {
            alert(":pto=45:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }});
}

//dar de baja el contaccto
function delContacto(intIdContacto) {
    var strPost = "";
    strPost = "idcontacto=" + intIdContacto;
    $("#dialogWait").dialog("open");
    $.ajax({
        type: "POST",
        data: strPost,
        scriptCharset: "UTF-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "html",
        url: "COFIDE_Telemarketing.jsp?ID=46",
        success: function (datos) {
            datos = trim(datos);
            if (datos == "OK") {
                LoadContacto(document.getElementById("CT_ID").value, 1);
            } else {
                alert("ERROR[ al afectar registro ]: " + datos);
            }
            $("#dialogWait").dialog("close");
        }, error: function (objeto, quepaso, otroobj) {
            alert(":pto=45:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }});

}


/*
 * Envia por correo
 * la factura con el xml
 */
function SendFacturaXML(strIdFac) {
    var strPost = "";
    strPost += "&id_fac=" + strIdFac;

    $.ajax({
        type: "POST",
        scriptCharset: "UTF-8",
        data: strPost,
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "html",
        url: "COFIDE_Programacionmails.jsp?ID=14",
        beforeSend: function () {
            $("#dialogWait").dialog("open");
        },
        success: function (datos) {
            datos = trim(datos);
            if (datos != "OK") {
                alert("ERROR: al enviar la factura" + datos);
            } else {
                alert("Factura enviada con exito.");
            }
            $("#dialogWait").dialog("close");
        }, error: function (objeto, quepaso, otroobj) {
            alert(":pto=14:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }});
}

/**
 * muestra campo para agregar curso de interes o guarda y oculta
 */
var showCursoInteres = 0;
function addCursoInteres() {
    var strCursoSugerido = document.getElementById("CT_CURSO_INTERES").value;
//    if (showCursoInteres == 0) {
    //muestra campos
//        showCursoInteres = 1;
//        document.getElementById("CT_CURSO_INTERES").parentNode.parentNode.style.display = "";
//    } else {
    //guarda y oculta
//        showCursoInteres = 0;
//        document.getElementById("CT_CURSO_INTERES").parentNode.parentNode.style.display = "none";
//        if (strCursoSugerido != "") {
    saveCursoSugerido(strCursoSugerido);
//        }
//    }
}
/**
 * guarda el curso
 * @returns {undefined}
 */
function saveCursoSugerido(strCursoSugerido) {
    var strPost = "";
    if (strCursoSugerido != "") {

        strPost += "&curso=" + encodeURIComponent(strCursoSugerido);
        strPost += "&idCte=" + document.getElementById("CT_ID").value;

        $.ajax({
            type: "POST",
            scriptCharset: "UTF-8",
            data: encodeURI(strPost),
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "xml",
            url: "COFIDE_Telemarketing.jsp?ID=47",
            beforeSend: function () {
                $("#dialogWait").dialog("open");
            },
            success: function (datos) {
                jQuery("#CT_GRID_CURSOS").clearGridData();
                var lstXml = datos.getElementsByTagName("vta")[0];
                var lstCte = lstXml.getElementsByTagName("datos");
                for (var i = 0; i < lstCte.length; i++) {
                    var objcte = lstCte[i];
                    var datarow = {
                        CCI_ID: objcte.getAttribute("CCI_ID"),
                        CCI_CT_ID: objcte.getAttribute("CCI_CT_ID"),
                        CCI_FECHA: objcte.getAttribute("CCI_FECHA"),
                        CCI_US_ALTA: objcte.getAttribute("CCI_US_ALTA"),
                        CCI_DESCRIPCION: objcte.getAttribute("CCI_DESCRIPCION")
                    };
                    jQuery("#CT_GRID_CURSOS").addRowData(objcte.getAttribute("CCI_ID"), datarow, "last");
                }
                document.getElementById("CT_CURSO_INTERES").value = "";
                showCursoInteres = 0;
                $("#dialogWait").dialog("close");
            }, error: function (objeto, quepaso, otroobj) {
                alert(":pto=14:" + objeto + " " + quepaso + " " + otroobj);
                $("#dialogWait").dialog("close");
            }});
    }
}
/**
 * regresa la sugerencia de cursos
 * @returns {undefined}
 */
function BuscaCursoSugerido() {
    var strCurso = document.getElementById("CT_CURSO_INTERES").value;
    var strPost = "CURSO=" + strCurso;
    $(function () {
        $("#CT_CURSO_INTERES").autocomplete({//campo de texto que tendra el autocmplete
            source: "COFIDE_Telemarketing.jsp?ID=48&" + strPost,
            minLength: 2
        });
    });
}

function opnEnvioPlantillaVenta() {
    var grid = jQuery("#H_VENTA_CTE");
    var id = grid.getGridParam("selrow");
    if (grid.getGridParam("selrow") != null) {

        var strHtml = '<div style="text-align:center; display:flex;">';
        strHtml += '<div style="padding:10px; margin:10px; display:inline-block;">';
        strHtml += '<label>FACTURA</label><br />';
        strHtml += '<a href="javascript:EnviaFacturaMail(1)" ><i class="fa fa-envelope" style="font-size:50px; color:#0CF;" title="ENVÍO DE FACTURA"></i></a></div>';
        strHtml += '<div style="padding:10px; margin:10px; display:inline-block;">';
        strHtml += '<label>TICKET</label><br />';
        strHtml += '<a href="javascript:EnviaFacturaMail(2)" ><i class="fa fa-envelope" style="font-size:50px; color:RED;" title="ENVÍO DE TICKET"></i></a></div>';
        strHtml += '<div style="padding:10px; margin:10px; display:inline-block;">';
        strHtml += '<label>RESERVACIÓN</label><br />';
        strHtml += '<a href="javascript:EnviaFacturaMail(3)" ><i class="fa fa-envelope" style="font-size:50px; color:#03F;" title="ENVÍO DE RESERVACIÓN"></i></a></div>';
        strHtml += '<div style="padding:10px; margin:10px; display:inline-block;">';
        strHtml += '<label>CONFIRMACIÓN</label><br />';
        strHtml += '<a href="javascript:EnviaFacturaMail(4)" ><i class="fa fa-envelope" style="font-size:50px; color:#99cc00;" title="ENVÍO DE CONFIRMACIÓN"></i></a></div>';
        strHtml += '</div>';

        document.getElementById("dialogGen_inside").innerHTML = strHtml;
        $("#dialogGen").dialog("option", "title", "SELECCIONA EL TIPO DE ENVÍO.");
        $("#dialogGen").dialog("open");

    } else {
        alert("Selecciona una venta.");
    }
}


/*
 * Envia por correo
 * el ticket
 */
function SendTicketTMK(strIdFac) {
    var strPost = "";
    strPost += "id_tkt=" + strIdFac;

    $.ajax({
        type: "POST",
        data: strPost,
        scriptCharset: "UTF-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "html",
        url: "COFIDE_Programacionmails.jsp?ID=15",
        beforeSend: function () {
            $("#dialogWait").dialog("open");
            console.log(strPost);
        },
        success: function (datos) {
            datos = trim(datos);
            if (datos != "OK") {
                alert("ERROR: al recibir el ticket: " + datos);
            } else {
                alert("Ticket enviado con exito.");
            }
            $("#dialogWait").dialog("close");
        }, error: function (objeto, quepaso, otroobj) {
            alert(":pto=15:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }});
}

//envio de reservación o confirmación
function sendReservaConfirm(strIdFac, idMov, opc) {
    var strPost = "";
    strPost += "&id_tkt=" + strIdFac;
    strPost += "&IDMOV=" + idMov;
    strPost += "&reenvio=1";

    if (opc == 1) {
        strPost += "&venta_nueva=1";
        strPost += "&CEV_PAGO_OK=0";
    } else {
        strPost += "&venta_nueva=0";
        strPost += "&CEV_PAGO_OK=0";
    }

    $.ajax({
        type: "POST",
        data: strPost,
        scriptCharset: "UTF-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "html",
        url: "COFIDE_Programacionmails.jsp?ID=17",
        beforeSend: function () {
            $("#dialogWait").dialog("open");
            console.log(strPost);
        },
        success: function (datos) {
            datos = trim(datos);
            if (datos != "OK") {
                alert("ERROR: enviar plantilla: " + datos);
            } else {
                alert("Plantilla enviada con Exito!!");
            }
            $("#dialogWait").dialog("close");
        }, error: function (objeto, quepaso, otroobj) {
            alert(":pto=17:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }});
}

function LoadCursoSugerido() {
    var strPost = "";

    strPost += "&curso=";
    strPost += "&idCte=" + document.getElementById("CT_ID").value;

    $.ajax({
        type: "POST",
        scriptCharset: "UTF-8",
        data: encodeURI(strPost),
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "COFIDE_Telemarketing.jsp?ID=47",
        beforeSend: function () {
            $("#dialogWait").dialog("open");
        },
        success: function (datos) {
            jQuery("#CT_GRID_CURSOS").clearGridData();
            var lstXml = datos.getElementsByTagName("vta")[0];
            var lstCte = lstXml.getElementsByTagName("datos");
            for (var i = 0; i < lstCte.length; i++) {
                var objcte = lstCte[i];
                var datarow = {
                    CCI_ID: objcte.getAttribute("CCI_ID"),
                    CCI_CT_ID: objcte.getAttribute("CCI_CT_ID"),
                    CCI_FECHA: objcte.getAttribute("CCI_FECHA"),
                    CCI_US_ALTA: objcte.getAttribute("CCI_US_ALTA"),
                    CCI_DESCRIPCION: objcte.getAttribute("CCI_DESCRIPCION")
                };
                jQuery("#CT_GRID_CURSOS").addRowData(objcte.getAttribute("CCI_ID"), datarow, "last");
            }
            document.getElementById("CT_CURSO_INTERES").value = "";
            showCursoInteres = 0;
            $("#dialogWait").dialog("close");
        }, error: function (objeto, quepaso, otroobj) {
            alert(":pto=47:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }});
}

function OpnDiagIncidencias() {
//    Abrir_Link("COFIDE_Calendario_cursos.jsp", "_blank", 800, 600, 0, 0);
    window.open("incidencia/index.html", '_blank');
}
function OpnDiagSepomex() {
//    Abrir_Link("COFIDE_Calendario_cursos.jsp", "_blank", 800, 600, 0, 0);
    window.open("sepomex/index.html", '_blank');
}