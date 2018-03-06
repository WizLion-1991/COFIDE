function cofide_telemarketing_ventas() {

}
var bolNuevo = 0; //0 = viejo / 1 = nuevo
var itemIdCob = 0;
var itemIdFactDat = 0;
var strOoperFac = "";
var aux = [];
var bolVtaOk = "";
var bolVta = true; //true = si va concluir venta, false = si va  de regreso
var idContacto = "0";

//edicion de participantes
var titulo = "";
var nombre = "";
var apellidop = "";
var apellidom = "";
var asoc = "";
var asonum = "";
var correo = "";
var contacto_id = "0";
var opcParticipante = "";
var material = "";


//INFORMACIÓN TEMPORAL DE LA FACTURA
var strDescTMP = ""; // DESCUENTO
var strFormaPagoTMP = ""; //FORMA DE PAGO
var strComentTMP = ""; // COMENTARIO
//INFORMACIÓN TEMPORAL DE LA FACTURA


function initDesc() {
    $("#dialogCte").dialog({
        dialogClass: "no-close"
    });
    document.getElementById("BOLDESC").value = "0";
}

function initTVentas() {
//    $("#CEV_TIPO_CURSO3").attr("disabled", "");
//    $("#CEV_TIPO_CURSO3").hide();
    document.getElementById("CEV_MIMP1").parentNode.parentNode.style.display = "none";
//    ////quitar hasta que esten las plantillas
    //verifica la duplicidad del cliente cuando es tkt 
    getInfCte(document.getElementById("CT_ID").value); //arreglo de información del cte  
    //campos de venta a detalle
    var strIDFAC = document.getElementById("CT_ID_FACTKT").value;
    var strTipoDoc = document.getElementById("CT_TIPODOC").value;
    var strEstatus = document.getElementById("CT_ESTATUS_VTA").value;
    //campos de venta a detalle
    cancelTVenta();
    listarCursos();
    hideDatFact();
    $("#tabsNVENTA2").tabs("option", "active", 0);
    TabsMapFactCofide("1,2,3,4", false, "NVENTA2");
    document.getElementById("CEV_BTNGUARDAR").parentNode.parentNode.style.display = "none";
    document.getElementById("CEV_DOFAC").parentNode.parentNode.style.display = "none";
    document.getElementById("CEV_DOFAC_CTE").parentNode.parentNode.style.display = "none";
//    document.getElementById("CEV_RFAC1").parentNode.parentNode.style.display = "none";
    document.getElementById("CC_HORARIO").parentNode.parentNode.style.display = "none";
    document.getElementById("CC_SEDE").parentNode.parentNode.style.display = "none";
    var bolPromosion = document.getElementById("CT_PROMOSION").value;
    var intIdContacto = document.getElementById("CT_CONTACTO_ID").value;

    var strAdd1 = "<table>"
            + "<tr>"
            + '<td><a href="javascript:addParticipanteExistente()" ><i class = "fa fa-plus-square" title="AGREGAR PARTICIPANTE EXISTENTE"  style="font-size:30px; color: #00BFFF"></i></td>'
            + "</tr>"
            + "</table>";
    if (document.getElementById("CT_GRID").value != 3) {
        document.getElementById("div_add1").innerHTML = strAdd1;
        document.getElementById("CC_HORARIO").parentNode.parentNode.style.display = "";
        document.getElementById("CC_SEDE").parentNode.parentNode.style.display = "";
    }
//    alert("ENTRARA A PROMOSIÓN: " + bolPromosion);
    if (bolPromosion != 1) { //venta de promosión si es = 1, si no, venta normal
//        if (strIDFAC == "" && strTipoDoc == "") { //si no hay datos en esos campos, es una venta nueva
        if (document.getElementById("CT_GRID").value == "3") { //si no hay datos en esos campos, es una venta nueva
            loadDatosFact();
            LoadParticipantes(1);
            document.getElementById("CEV_PAGO_OK").value = "0";
        } else {
            // detalle de venta
            if (strTipoDoc == "FACTURA") {
                document.getElementById("CEV_BTNGUARDAR").parentNode.parentNode.style.display = "none";
                BloqCampos();
            }
            Vta_Deta();
            if (document.getElementById("CT_GRID").value == "0") { //venta cliente
                if (strEstatus == "Cancelada" && strTipoDoc != "FACTURA") { //si no hay datos en esos campos, es una venta nueva
                    document.getElementById("CEV_DOFAC_CTE").parentNode.parentNode.style.display = "none";
                    BloqCampos();
                } else {
                    document.getElementById("CEV_DOFAC_CTE").parentNode.parentNode.style.display = "";
                }
                document.getElementById("CEV_BTNGUARDAR").parentNode.parentNode.style.display = "none";
            } else { //vta general
                if (strEstatus == "Cancelada" && strTipoDoc != "FACTURA") { //si no hay datos en esos campos, es una venta nueva
                    document.getElementById("CEV_DOFAC").parentNode.parentNode.style.display = "none";
                    BloqCampos();
                } else {
                    document.getElementById("CEV_DOFAC").parentNode.parentNode.style.display = "";
                }
                document.getElementById("CEV_BTNGUARDAR").parentNode.parentNode.style.display = "none";
            }
        }
        isUnBound();
    } else {
        document.getElementById("CEV_ADDBTN").parentNode.parentNode.style.display = "none";
        document.getElementById("CEV_DELBTN").parentNode.parentNode.style.display = "none";
        document.getElementById("CEV_EDITBTN").parentNode.parentNode.style.display = "none";
        document.getElementById("CEV_TIPO_CURSO0").checked = true; //solo presenciales
        $("#CEV_TIPO_CURSO0").attr("disabled", "");
        $("#CEV_TIPO_CURSO1").attr("disabled", "");
        $("#CEV_TIPO_CURSO2").attr("disabled", "");
        $("#CEV_TIPO_CURSO3").attr("disabled", "");
        //precios
        document.getElementById("CEV_PRECIO_UNIT").value = "0";
//        document.getElementById("CEV_PRECIO_UNIT").parentNode.parentNode.style.display = "none";
        document.getElementById("CEV_DESCRIPCION").parentNode.parentNode.style.display = "none";
        document.getElementById("CEV_DESC").parentNode.parentNode.style.display = "none";
        document.getElementById("CEV_SUB1").value = "0";
        document.getElementById("CEV_SUB2").value = "0";
        document.getElementById("CEV_SUB3").value = "0";
        document.getElementById("CEV_SUB1").parentNode.parentNode.style.display = "none";
        document.getElementById("CEV_SUB2").parentNode.parentNode.style.display = "none";
        document.getElementById("CEV_SUB3").parentNode.parentNode.style.display = "none";
        document.getElementById("CEV_DIGITO").parentNode.parentNode.style.display = "none";
        document.getElementById("CEV_FECHAPAGO").parentNode.parentNode.style.display = "none";
        document.getElementById("CEV_MIMP1").parentNode.parentNode.style.display = "none";
        document.getElementById("CEV_FAC0").parentNode.parentNode.style.display = "none";
        document.getElementById("CEV_COMPROBANTE").parentNode.parentNode.style.display = "none";
        document.getElementById("btn_up_ficha_deposito").parentNode.parentNode.style.display = "none";
        document.getElementById("CEV_NOM_FILE").parentNode.parentNode.style.display = "none";

        //carga el contacto con promosion
        LoadContactoPromo(intIdContacto);
        isUnBound();
    }
    document.getElementById("CT_CTE_NUEVO").value = "0";
}

//Permiso para saber si puede escoger el medio de publicidad al hacer una venta.z
function isUnBound() {
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
                    document.getElementById("CEV_MPUBLICIDAD").parentNode.parentNode.style.display = 'block';
                    document.getElementById("CT_PERMISO_INBOUND").value = 1;
                } else {
                    document.getElementById("CT_PERMISO_INBOUND").value = 0;
                    document.getElementById("CEV_MPUBLICIDAD").parentNode.parentNode.style.display = 'none';
                }
            }
            $("#dialogWait").dialog("close");
        },
        error: function (objeto, quepaso, otroobj) {
            alert(":pto:" + objeto + " " + quepaso + " " + otroobj);
        }
    });
}//Fin isUnBound

/**
 * valida los participantes cuando es venta nueva o edicion
 * @returns {undefined}
 */
function validaParticipante() {
    var intLugaresVendidos = document.getElementById("num_participante").value;
    if (document.getElementById("CEV_FAC2").checked) { // es factura
        if (document.getElementById("CT_GRID").value != "3") { // es una edición de venta
            if (intParticipante() < intLugaresVendidos) { //valida que la cantidad de vendidos no se agreguen de mas
                newTVenta();
            } else {
                alert("No puedes agregar mas participantes de los ya antes facturados.");
            }
        } else { // es una venta nueva, para agregar participantes
            newTVenta();
        }
    } else {
        newTVenta();
    }
}

function newTVenta() {
    document.getElementById("CEV_TITULO").parentNode.parentNode.style.display = "";
    document.getElementById("CEV_NUMERO").parentNode.parentNode.style.display = "";
    document.getElementById("CEV_NOMBRE").parentNode.parentNode.style.display = "";
    document.getElementById("CEV_APPATERNO").parentNode.parentNode.style.display = "";
    document.getElementById("CEV_APMATERNO").parentNode.parentNode.style.display = "";
    document.getElementById("CEV_ASOCIACION").parentNode.parentNode.style.display = "";
    document.getElementById("CEV_CORREO").parentNode.parentNode.style.display = "";
    document.getElementById("material").parentNode.parentNode.style.display = "";
    document.getElementById("CEV_ADDBTN").parentNode.parentNode.style.display = "none";
    document.getElementById("CEV_EDITBTN").parentNode.parentNode.style.display = "none";
    document.getElementById("CEV_DELBTN").parentNode.parentNode.style.display = "none";
    document.getElementById("CEV_CANCELBTN").parentNode.parentNode.style.display = "";
    document.getElementById("CEV_SAVEBTN").parentNode.parentNode.style.display = "";
}

function cancelTVenta() {
    if (opcParticipante == "E") {
        var datarow = {
            CCO_NOMBRE: nombre,
            CCO_APPATERNO: apellidop,
            CCO_APMATERNO: apellidom,
            CCO_TITULO: titulo,
            CCO_NOSOCIO: intDato(asonum),
            CCO_CORREO: correo,
            CONTACTO_ID: contacto_id,
            CCO_ASOCIACION: asoc
        };
        jQuery("#GRIDPARTICIPA").addRowData(itemIdCob, datarow, "last");
    }
    document.getElementById("CEV_TITULO").parentNode.parentNode.style.display = "none";
    document.getElementById("CEV_TITULO").value = "0";
    document.getElementById("CEV_NOMBRE").parentNode.parentNode.style.display = "none";
    document.getElementById("CEV_NOMBRE").value = "";
    document.getElementById("CEV_APPATERNO").parentNode.parentNode.style.display = "none";
    document.getElementById("CEV_APPATERNO").value = "";
    document.getElementById("material").parentNode.parentNode.style.display = "none";
    document.getElementById("material").checked = false;
    document.getElementById("CEV_APMATERNO").parentNode.parentNode.style.display = "none";
    document.getElementById("CEV_APMATERNO").value = "";
    document.getElementById("CEV_ASOCIACION").parentNode.parentNode.style.display = "none";
    document.getElementById("CEV_ASOCIACION").value = "0";
    document.getElementById("CEV_NUMERO").parentNode.parentNode.style.display = "none";
    document.getElementById("CEV_NUMERO").value = "";
    document.getElementById("CEV_CORREO").parentNode.parentNode.style.display = "none";
    document.getElementById("CEV_CORREO").value = "";
    document.getElementById("CEV_ADDBTN").parentNode.parentNode.style.display = "";
    document.getElementById("CEV_EDITBTN").parentNode.parentNode.style.display = "";
    document.getElementById("CEV_DELBTN").parentNode.parentNode.style.display = "";
    document.getElementById("CEV_CANCELBTN").parentNode.parentNode.style.display = "none";
    document.getElementById("CEV_SAVEBTN").parentNode.parentNode.style.display = "none";
    idContacto = "0";
    //limpia variables
    opcParticipante = "";
    nombre = "";
    apellidop = "";
    apellidom = "";
    titulo = "";
    asonum = "";
    correo = "";
    contacto_id = "0";
    asoc = "";
}
var intCola = 0;
function addGridCola() {
    opcParticipante = "";
    var boolBand = 0;
    var intRow = 0;
    if (document.getElementById("CEV_NOMBRE").value != "" && document.getElementById("CEV_CORREO").value != "") {
        boolBand = 1;
    } else {
        boolBand = 0;
        alert("Capture el Nombre y/o el correo");
    }
    if (boolBand == 1) {
        var strTitulo = document.getElementById("CEV_TITULO").value;
        var strNombre = document.getElementById("CEV_NOMBRE").value;
        var strApPat = document.getElementById("CEV_APPATERNO").value;
        var strApMat = document.getElementById("CEV_APMATERNO").value;
        var strAsoc = document.getElementById("CEV_ASOCIACION").value;
        var strNum = document.getElementById("CEV_NUMERO").value;
        var strCorreo = document.getElementById("CEV_CORREO").value;
        var bolMaterial = "NO";
        if (document.getElementById("material").checked) {
            bolMaterial = "SI";
        }
        if (ValidaNumSoc()) {
            var datarow = {
                CCO_NOMBRE: strNombre,
                CCO_APPATERNO: strApPat,
                CCO_APMATERNO: strApMat,
                CCO_TITULO: strTitulo,
                CCO_NOSOCIO: intDato(strNum),
                CCO_CORREO: strCorreo,
                CONTACTO_ID: idContacto,
                CT_ID: strIdCliente,
                MATERIAL: bolMaterial,
                CCO_ASOCIACION: strAsoc
            };
            var intNumSoc = intDato(strNum);
            //actualización general del contacto/participante
            UpDateContactoParticipante(strNombre, strApPat, strApMat, strTitulo, intNumSoc, idContacto, strAsoc, strCorreo, bolMaterial);
            LimpiarGridParticipante();
            idContacto_ = "0";
            jQuery("#GRIDPARTICIPA").addRowData(intCola, datarow, "last");
            intCola++;
//            console.log(intCola + " consecutivo de participante");
            cancelTVenta();
            idContacto = "0";
        } else {
            alert("El Numero o correo del socio no es valido!");
        }
    }
}
function LimpiarGridParticipante() {
    document.getElementById("CEV_TITULO").value = "";
    document.getElementById("CEV_NOMBRE").value = "";
    document.getElementById("CEV_APPATERNO").value = "";
    document.getElementById("CEV_APMATERNO").value = "";
    document.getElementById("CEV_ASOCIACION").value = "";
    document.getElementById("CEV_NUMERO").value = "";
    document.getElementById("CEV_CORREO").value = "";
    document.getElementById("material").checked = false;
}
function consultaCurso() {
    if (!document.getElementById("CEV_TIPO_CURSO3").checked) {
        var strIDFAC = document.getElementById("CT_ID_FACTKT").value;
        if (strIDFAC == "") { // si no hay datos ahi, sera una venta nueva
//        var strCurso = document.getElementById("CEV_NOMCURSO").value;
            var strCurso = document.getElementById("CEV_IDCURSO").value; //id del producto
            if (strCurso == "") {
//                strCurso = document.getElementById("CEV_NOMCURSO").value.split(" /",1);
                strCurso = document.getElementById("CEV_NOMCURSO").value;
                strCurso = strCurso.split(" /", 1); //se obtiene el ID del curso
            }
            if (strCurso != "") {
                var strPost = "";
                strPost += "CEV_NOMCURSO=" + strCurso; //se obtiene el ID del curso
                var strCurso1 = document.getElementById("CEV_TIPO_CURSO0").checked;
                var strCurso2 = document.getElementById("CEV_TIPO_CURSO1").checked;
                var strCurso3 = document.getElementById("CEV_TIPO_CURSO2").checked;
                if (strCurso1) {
                    strPost += "&Clasifica=1";
                }
                if (strCurso2) {
                    strPost += "&Clasifica=2";
                }
                if (strCurso3) {
                    strPost += "&Clasifica=3";
                }
                $.ajax({
                    type: "POST",
                    data: strPost,
                    scriptCharset: "UTF-8",
                    contentType: "application/x-www-form-urlencoded;charset=utf-8",
                    cache: false,
                    dataType: "xml",
                    url: "COFIDE_Telemarketing_vta.jsp?ID=1",
                    success: function (datos) {
                        var lstXml = datos.getElementsByTagName("vta")[0];
                        var lstCte = lstXml.getElementsByTagName("datos");
                        for (var i = 0; i < lstCte.length; i++) {
                            var objcte = lstCte[i];
                            var intOcupado = parseInt(objcte.getAttribute("CEV_OCUPADO"));
                            var intLimite = parseInt(objcte.getAttribute("CEV_LIMITE"));
                            var strFecha = objcte.getAttribute("CEV_FECINICIO");
                            if (strCurso1) { //si es curso presencial, carga información de lugares
                                if (intOcupado < intLimite) {
                                    document.getElementById("CEV_LIMITE").value = objcte.getAttribute("CEV_LIMITE");
                                    document.getElementById("CEV_OCUPADO").value = objcte.getAttribute("CEV_OCUPADO");
                                    document.getElementById("CEV_NOMCURSO_TMP").value = objcte.getAttribute("CEV_NOMCURSO_TMP");
                                    document.getElementById("CEV_PRECIO_UNIT").value = FormatNumber(objcte.getAttribute("CEV_PRECIO_UNIT"), 2, true, false, true);
                                    document.getElementById("CEV_FECINICIO").value = objcte.getAttribute("CEV_FECINICIO");
                                    document.getElementById("CEV_FECHA").value = strFecha;
                                    document.getElementById("CEV_IDCURSO").value = objcte.getAttribute("CEV_IDCURSO");
                                } else {
                                    document.getElementById("CEV_NOMCURSO").value = "";
                                    alert("El curso ya no tiene lugares disponibles");
                                }
                            } else { // en linea o presencial
                                document.getElementById("CEV_NOMCURSO_TMP").value = objcte.getAttribute("CEV_NOMCURSO_TMP");
                                document.getElementById("CEV_PRECIO_UNIT").value = FormatNumber(objcte.getAttribute("CEV_PRECIO_UNIT"), 2, true, false, true);
                                document.getElementById("CEV_FECINICIO").value = objcte.getAttribute("CEV_FECINICIO");
                                document.getElementById("CEV_FECHA").value = strFecha;
                                document.getElementById("CEV_IDCURSO").value = objcte.getAttribute("CEV_IDCURSO");
                            }
                        }
                    }});
            }
        }
    }
}
function listarCursos() {
    document.getElementById("CEV_NOMCURSO").value = "";
    document.getElementById("CEV_LIMITE").value = "";
    document.getElementById("CEV_OCUPADO").value = "";
    document.getElementById("CEV_IDCURSO").value = "";
    document.getElementById("CEV_FECHA").value = "";
    var strCurso1 = document.getElementById("CEV_TIPO_CURSO0").checked;
    var strCurso2 = document.getElementById("CEV_TIPO_CURSO1").checked;
    var strCurso3 = document.getElementById("CEV_TIPO_CURSO2").checked;
    var strCurso4 = document.getElementById("CEV_TIPO_CURSO3").checked;
    var strNomCurso = document.getElementById("CEV_NOMCURSO").value;
    //promosion
    var bolPromosion = document.getElementById("CT_PROMOSION").value;
    var strPost = "";
    if (strCurso1) { //presencial
        strPost += "Clasifica=1";
        document.getElementById("CEV_IDCURSO").parentNode.parentNode.style.display = "";
        document.getElementById("CEV_NOMCURSO").parentNode.parentNode.style.display = "";
        document.getElementById("CEV_FECHA").parentNode.parentNode.style.display = "";
        document.getElementById("CEV_LIMITE").parentNode.parentNode.style.display = "";
        document.getElementById("CEV_OCUPADO").parentNode.parentNode.style.display = "";
        document.getElementById("CEV_MIMP1").parentNode.parentNode.style.display = "";
        document.getElementById("CEV_MEMBRESIA").parentNode.parentNode.style.display = "none";
    }
    if (strCurso2) { //online
        strPost += "Clasifica=2";
        document.getElementById("CEV_IDCURSO").parentNode.parentNode.style.display = "";
        document.getElementById("CEV_NOMCURSO").parentNode.parentNode.style.display = "";
        document.getElementById("CEV_FECHA").parentNode.parentNode.style.display = "";
        document.getElementById("CEV_LIMITE").parentNode.parentNode.style.display = "none";
        document.getElementById("CEV_OCUPADO").parentNode.parentNode.style.display = "none";
        document.getElementById("CEV_MIMP1").parentNode.parentNode.style.display = "none";
        document.getElementById("CEV_MEMBRESIA").parentNode.parentNode.style.display = "none";
    }
    if (strCurso3) { //video curso
        strPost += "Clasifica=3";
        document.getElementById("CEV_IDCURSO").parentNode.parentNode.style.display = "";
        document.getElementById("CEV_NOMCURSO").parentNode.parentNode.style.display = "";
        document.getElementById("CEV_FECHA").parentNode.parentNode.style.display = "";
        document.getElementById("CEV_LIMITE").parentNode.parentNode.style.display = "none";
        document.getElementById("CEV_OCUPADO").parentNode.parentNode.style.display = "none";
        document.getElementById("CEV_MIMP1").parentNode.parentNode.style.display = "none";
        document.getElementById("CEV_MEMBRESIA").parentNode.parentNode.style.display = "none";
    }
    if (strCurso4) { //video curso
        strPost += "Clasifica=4";
        document.getElementById("CEV_LIMITE").parentNode.parentNode.style.display = "none";
        document.getElementById("CEV_OCUPADO").parentNode.parentNode.style.display = "none";
        document.getElementById("CEV_FECHA").parentNode.parentNode.style.display = "none";
        document.getElementById("CEV_IDCURSO").parentNode.parentNode.style.display = "none";
        document.getElementById("CEV_NOMCURSO").parentNode.parentNode.style.display = "none";
        document.getElementById("CEV_MPUBLICIDAD").parentNode.parentNode.style.display = "none";
        document.getElementById("CEV_MEMBRESIA").parentNode.parentNode.style.display = "";
    }
    strPost += "&promosion=" + bolPromosion;
    $(function () {
        $("#CEV_NOMCURSO").autocomplete({source: "COFIDE_Telemarketing.jsp?ID=12&" + strPost, minLength: 2});
    });
}
/**
 * conteo de participantes
 * @returns {.grid@call;getDataIDs.length}
 */
function intRowD() {
    var intRow;
    var grid = jQuery("#GRIDPARTICIPA");
    var idArr = grid.getDataIDs();
    intRow = idArr.length;
    return intRow;
}
/**
 * conteo de participantes
 * @returns {.grid@call;getDataIDs.length}
 */
function intRowCursos() {
    var intRow;
    var grid = jQuery("#CURSOS_GRD");
    var idArr = grid.getDataIDs();
    intRow = idArr.length;
    return intRow;
}
//obtiene el numero de participantes en los # cursos
function getIntParticipante() {
    var grid = jQuery("#CURSOS_GRD");
    var arr = grid.getDataIDs();
    var intParticipantes = 0;
    for (var i = 0; i < arr.length; i++) {
        var id = arr[i];
        var lstRow = grid.getRowData(id);
        intParticipantes += parseInt(lstRow.LUGARES);
    }
    return intParticipantes;
}
//function operaciones() {
//
//    var PrecioUnit = parseFloat(document.getElementById("CEV_PRECIO_UNIT").value.replace(",", ""));
//    document.getElementById("CEV_CURSOS").value = intRowCursos();
//    document.getElementById("CEV_PARTICIPANTE").value = getIntParticipante();
//    var Participantes = getIntParticipante();
//    var SubTotal = PrecioUnit * Participantes;
//    var tax = new Impuestos(dblTasa1, dblTasa2, dblTasa3, intSImp1_2, intSImp1_3, intSImp2_3);
//    tax.CalculaImpuestoMas(SubTotal); //iva por el importe
//    var dblIva = parseFloat(this.dblImpuesto1 = tax.dblImpuesto1);
//    var Total = dblIva + SubTotal;
//    document.getElementById("CEV_SUB1").value = FormatNumber(SubTotal, 2, true, false, true);
//    document.getElementById("CEV_SUB3").value = FormatNumber(Total, 2, true, false, true);
//    document.getElementById("CEV_SUB2").value = FormatNumber(dblIva, 2, true, false, true);
//    CalcDescuento();
//}
function delGridCola() {
    var grid = jQuery("#GRIDPARTICIPA");
    if (grid.getGridParam("selrow") != null) {
        grid.delRowData(grid.getGridParam("selrow"));
        itemIdCob = -1;
    }
}

function cerrarVtaCofide() {
    var objSecModiVta = objMap.getScreen("NVENTA2");
    if (objSecModiVta != null) {
        objSecModiVta.bolActivo = false;
        objSecModiVta.bolMain = false;
        objSecModiVta.bolInit = false;
        objSecModiVta.idOperAct = 0;
    }
    $("#dialog").dialog("close");
    document.getElementById("CT_ID_FACTKT").value = ""; // se guarda el id en un hidden           
    document.getElementById("CT_TIPODOC").value = ""; // se guarda el id en un hidden 
    document.getElementById("CT_PROMOSION").value = "0";
//    if (document.getElementById("CT_GRID").value != "3") {
//        //regresar los cambios
////        triggerTransaction(3);
//    }
}

function agregaDatosFact() {
    showDatFact();
    strOoperFac = "N";
    strOpcionDatosFact = "S"; // MUESTRA LOS CAMPOS Y LOS ALISTA PARA GUARDAR
}
///BAJA LA INFORMACIÓN DEL GRID A LOS CAMPOS PARA MODIFICARLOS
var strIdRazonSocial = "0";
function editDatosFact() {
    var grid = jQuery("#GRD_RAZONES");
    if (grid.getGridParam("selrow") != null) {
        showDatFact();
        var lstRow = grid.getRowData(grid.getGridParam("selrow"));
        document.getElementById("CEV_CP").value = lstRow.CEV_CP;
        llenarColoniaFac();
        document.getElementById("CEV_RAZONSOCIAL").value = lstRow.CEV_RAZONSOCIAL;
        document.getElementById("CEV_NUMERO_FACT").value = lstRow.CEV_NUMERO;
        document.getElementById("CEV_NUMERO_INT").value = lstRow.CEV_NUMINT;
        document.getElementById("CEV_RFC").value = lstRow.CEV_RFC;
        document.getElementById("CEV_TELEFONO").value = lstRow.CEV_TELEFONO;
        document.getElementById("CEV_EMAIL1").value = lstRow.CEV_EMAIL1;
        document.getElementById("CEV_EMAIL2").value = lstRow.CEV_EMAIL2;
        document.getElementById("CEV_CALLE").value = lstRow.CEV_CALLE;
        document.getElementById("CEV_COLONIA").value = lstRow.CEV_COLONIA;
        document.getElementById("CEV_MUNICIPIO").value = lstRow.CEV_MUNICIPIO;
        document.getElementById("CEV_ESTADO").value = lstRow.CEV_ESTADO;
        strIdRazonSocial = lstRow.CEV_ID_FAC;
        strOoperFac = "C";
        strOpcionDatosFact = "S";
    } else {
        alert("Es necesario elegir una razón social a modificar.");
    }
}
function loadDatosFact() {
    var intIdCte = document.getElementById("CT_NO_CLIENTE").value;
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
            jQuery("#CEV_GRID").clearGridData();
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
                jQuery("#CEV_GRID").addRowData(itemIdFactDat, datarow, "last");
            }
        }, error: function (objeto, quepaso, otroobj) {
            alert(":pto=2:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }
    });
}
function showDatFact() {
    document.getElementById("CEV_RAZONSOCIAL").parentNode.parentNode.style.display = "";
    document.getElementById("CEV_NUMERO_FACT").parentNode.parentNode.style.display = "";
    document.getElementById("CEV_NUMERO_INT").parentNode.parentNode.style.display = "";
    document.getElementById("CEV_RFC").parentNode.parentNode.style.display = "";
    document.getElementById("CEV_CALLE").parentNode.parentNode.style.display = "";
    document.getElementById("CEV_COLONIA").parentNode.parentNode.style.display = "";
    document.getElementById("CEV_MUNICIPIO").parentNode.parentNode.style.display = "";
    document.getElementById("CEV_ESTADO").parentNode.parentNode.style.display = "";
    document.getElementById("CEV_CP").parentNode.parentNode.style.display = "";
    document.getElementById("CEV_TELEFONO").parentNode.parentNode.style.display = "";
    document.getElementById("CEV_EMAIL1").parentNode.parentNode.style.display = "";
    document.getElementById("CEV_EMAIL2").parentNode.parentNode.style.display = "";
    document.getElementById("CEV_RAZONSOCIAL").value = "";
    document.getElementById("CEV_NUMERO_FACT").value = "";
    document.getElementById("CEV_NUMERO_INT").value = "";
    document.getElementById("CEV_RFC").value = "";
    document.getElementById("CEV_CALLE").value = "";
    document.getElementById("CEV_COLONIA").value = "";
    document.getElementById("CEV_MUNICIPIO").value = "";
    document.getElementById("CEV_ESTADO").value = "";
    document.getElementById("CEV_CP").value = "";
    document.getElementById("CEV_TELEFONO").value = "";
    document.getElementById("CEV_EMAIL1").value = "";
    document.getElementById("CEV_EMAIL2").value = "";
    document.getElementById("FAC_CLOSE").parentNode.parentNode.style.display = "";
    document.getElementById("FAC_EDIT").parentNode.parentNode.style.display = "none";
}
function hideDatFact() {
    document.getElementById("CEV_RAZONSOCIAL").parentNode.parentNode.style.display = "none";
    document.getElementById("CEV_NUMERO_FACT").parentNode.parentNode.style.display = "none";
    document.getElementById("CEV_NUMERO_INT").parentNode.parentNode.style.display = "none";
    document.getElementById("CEV_RFC").parentNode.parentNode.style.display = "none";
    document.getElementById("CEV_CALLE").parentNode.parentNode.style.display = "none";
    document.getElementById("CEV_COLONIA").parentNode.parentNode.style.display = "none";
    document.getElementById("CEV_MUNICIPIO").parentNode.parentNode.style.display = "none";
    document.getElementById("CEV_ESTADO").parentNode.parentNode.style.display = "none";
    document.getElementById("CEV_CP").parentNode.parentNode.style.display = "none";
    document.getElementById("CEV_TELEFONO").parentNode.parentNode.style.display = "none";
    document.getElementById("CEV_EMAIL1").parentNode.parentNode.style.display = "none";
    document.getElementById("CEV_EMAIL2").parentNode.parentNode.style.display = "none";
    document.getElementById("FAC_CLOSE").parentNode.parentNode.style.display = "none";
    document.getElementById("FAC_EDIT").parentNode.parentNode.style.display = "";
}
function saveDatosFact() {

    if (validateDatosFact()) {
        if (strOoperFac == "N") {
            strIdRazonSocial = "0";
            var datarow = {
                CEV_ID_FAC: 0,
                CEV_RAZONSOCIAL: document.getElementById("CEV_RAZONSOCIAL").value,
                CEV_NUMERO: document.getElementById("CEV_NUMERO_FACT").value,
                CEV_NUMINT: document.getElementById("CEV_NUMERO_INT").value,
                CEV_RFC: document.getElementById("CEV_RFC").value,
                CEV_CALLE: document.getElementById("CEV_CALLE").value.toUpperCase(),
                CEV_COLONIA: document.getElementById("CEV_COLONIA").value.toUpperCase(),
                CEV_MUNICIPIO: document.getElementById("CEV_MUNICIPIO").value.toUpperCase(),
                CEV_ESTADO: document.getElementById("CEV_ESTADO").value.toUpperCase(),
                CEV_CP: document.getElementById("CEV_CP").value,
                CEV_TELEFONO: document.getElementById("CEV_TELEFONO").value,
                CEV_EMAIL1: document.getElementById("CEV_EMAIL1").value,
                CEV_EMAIL2: document.getElementById("CEV_EMAIL2").value
            };
            itemIdFactDat++;
            jQuery("#GRD_RAZONES").addRowData(itemIdFactDat, datarow, "last");
            hideDatFact();
        } else {
            var grid = jQuery("#GRD_RAZONES");
            if (grid.getGridParam("selrow") != null) {
                var lstRow = grid.getRowData(grid.getGridParam("selrow"));
                lstRow.CEV_RAZONSOCIAL = document.getElementById("CEV_RAZONSOCIAL").value;
                lstRow.CEV_NUMERO = document.getElementById("CEV_NUMERO_FACT").value;
                lstRow.CEV_NUMINT = document.getElementById("CEV_NUMERO_INT").value;
                lstRow.CEV_RFC = document.getElementById("CEV_RFC").value;
                lstRow.CEV_CALLE = document.getElementById("CEV_CALLE").value;
                lstRow.CEV_COLONIA = document.getElementById("CEV_COLONIA").value;
                lstRow.CEV_MUNICIPIO = document.getElementById("CEV_MUNICIPIO").value;
                lstRow.CEV_ESTADO = document.getElementById("CEV_ESTADO").value;
                lstRow.CEV_CP = document.getElementById("CEV_CP").value;
                lstRow.CEV_TELEFONO = document.getElementById("CEV_TELEFONO").value;
                lstRow.CEV_EMAIL1 = document.getElementById("CEV_EMAIL1").value;
                lstRow.CEV_EMAIL2 = document.getElementById("CEV_EMAIL2").value;
                lstRow.CEV_ID_FAC = strIdRazonSocial;
                grid.setRowData(grid.getGridParam("selrow"), lstRow);
            }
            hideDatFact();
        }
        strOoperFac = "";
        strOpcionDatosFact = "N";

        var strPost = "&CT_RAZONSOCIAL=" + encodeURIComponent(document.getElementById("CEV_RAZONSOCIAL").value);
        strPost += "&CT_NUM=" + document.getElementById("CEV_NUMERO_FACT").value;
        strPost += "&CT_NUMINT=" + document.getElementById("CEV_NUMERO_INT").value;
        strPost += "&CT_RFC=" + encodeURIComponent(document.getElementById("CEV_RFC").value);
        strPost += "&CT_CALLE=" + encodeURIComponent(document.getElementById("CEV_CALLE").value);
        strPost += "&CT_COL=" + document.getElementById("CEV_COLONIA").value;
        strPost += "&CT_MUNICIPIO=" + document.getElementById("CEV_MUNICIPIO").value;
        strPost += "&CT_ESTADO=" + document.getElementById("CEV_ESTADO").value;
        strPost += "&CT_CP=" + document.getElementById("CEV_CP").value;
        strPost += "&CT_TELEFONO=" + document.getElementById("CEV_TELEFONO").value;
        strPost += "&CT_CORREO=" + document.getElementById("CEV_EMAIL1").value;
        strPost += "&CT_CORREO2=" + document.getElementById("CEV_EMAIL2").value;
        strPost += "&DFA_ID=" + strIdRazonSocial;
        //agrega y actualiza información en pantalla
        Vta_RazonSocial(strPost);
    }
}
function validateDatosFact() {
    var bolValido = true;
    if (document.getElementById("CEV_RAZONSOCIAL").value == "") {
        alert("Se requiere la razon social");
        return false;
    }
    if (document.getElementById("CEV_RFC").value == "") {
        alert("Se requiere el rfc");
        return false;
    } else {
        var bolExpReg = _EvalExpRegCOFIDE1(document.getElementById("CEV_RFC").value, "^[A-Z,Ñ,&,a-z,ñ]{3,4}[0-9]{2}[0-1][0-9][0-3][0-9][A-Z,Ñ,&,0-9,a-z]{3}");
        if (!bolExpReg) {
//            alert("El formato del registro federal de contribuyentes es incorrecto");
            document.getElementById("CEV_RFC").style["background"] = "#FF0000";
            return false;
        } else {
            document.getElementById("CEV_RFC").style["background"] = "#FFFFFF";
        }
    }
    if (document.getElementById("CEV_CALLE").value == "") {
        alert("Se requiere la calle");
        return false;
    }
    if (document.getElementById("CEV_NUMERO_FACT").value == "") {
        alert("Se requiere el numero de exterior");
        return false;
    }
//    if (document.getElementById("CEV_NUMERO_INT").value == "") {
//        alert("Se requiere el numero de interior");
//        return false;
//    }
    if (document.getElementById("CEV_COLONIA").value == "") {
        alert("Se requiere la colonia");
        return false;
    }
    if (document.getElementById("CEV_MUNICIPIO").value == "") {
        alert("Se requiere el municipio");
        return false;
    }
    if (document.getElementById("CEV_ESTADO").value == "") {
        alert("Se requiere el estado");
        return false;
    }
    if (document.getElementById("CEV_CP").value == "") {
        alert("Se requiere el codigo postal");
        return false;
    }
    if (document.getElementById("CEV_EMAIL1").value != "") {
//        var bolExpReg = _EvalExpRegCOFIDE1(document.getElementById("CEV_EMAIL1").value, "^[a-zA-Z][a-zA-Z-_0-9.]+@[a-zA-Z-_=>0-9.]+.[a-zA-Z]{2,3}$");
        var bolExpReg = _EvalExpRegCOFIDE1(document.getElementById("CEV_EMAIL1").value, "^[a-zA-Z0-9_Ññ][a-zA-Z-_0-9.Ññ]+@[a-zA-Z-_=>0-9.]+.[a-zA-Z]{2,3}$");
        if (!bolExpReg) {
            alert("El formato del mail es incorrecto");
            document.getElementById("CEV_EMAIL1").focus();
            return false;
        }
    }
    if (document.getElementById("CEV_EMAIL2").value != "") {
        var bolExpReg = _EvalExpRegCOFIDE1(document.getElementById("CEV_EMAIL2").value, "^[a-zA-Z0-9_Ññ][a-zA-Z-_0-9.Ññ]+@[a-zA-Z-_=>0-9.]+.[a-zA-Z]{2,3}$");
        if (!bolExpReg) {
            alert("El formato del mail es incorrecto");
            document.getElementById("CEV_EMAIL2").focus();
            return false;
        }
    }
    if (document.getElementById("CEV_CP").value != "") {
        var bolExpReg = _EvalExpRegCOFIDE1(document.getElementById("CEV_CP").value, "^([1-9]{2}|[0-9][1-9]|[1-9][0-9])[0-9]{3}$");
        if (!bolExpReg) {
            alert("El formato del codigo postal es incorrecto");
            document.getElementById("CEV_CP").focus();
            return false;
        }
    }
    return bolValido;
}
function cancelDatosFact() {
    hideDatFact();
    strOoperFac = "";
    strOpcionDatosFact = "N";
}
function TabsMapFactCofide(lstTabs, bolActivar, strNomTab) {
    var arrTabs = lstTabs.split(",");
    for (var i = 0; i < arrTabs.length; i++) {
        if (bolActivar) {
            $("#tabs" + strNomTab).tabs("enable", parseInt(arrTabs[i]));
        } else {
            $("#tabs" + strNomTab).tabs("disable", parseInt(arrTabs[i]));
        }
    }
}

function continuarPaso(numPaso, bolVta) {

    var strTipodeVenta = document.getElementById("CT_GRID").value; // 3 = venta nueva/ 0 o 2 , edicioón    
    var strCurso = document.getElementById("CEV_NOMCURSO_TMP").value;
    if (numPaso == 0) {
        TabsMapFactCofide("0", true, "NVENTA2");
        $("#tabsNVENTA2").tabs("option", "active", 0);
        TabsMapFactCofide("1,2,3,4", false, "NVENTA2");
        document.getElementById("BTN_NEXT1").parentNode.parentNode.style.display = "";
        // limpiar pantalla de selección de cursos
        listarCurso_();
    }
    if (numPaso == 1) {
        if (strCurso != "") {
            TabsMapFactCofide("1", true, "NVENTA2");
            $("#tabsNVENTA2").tabs("option", "active", 1);
            TabsMapFactCofide("0,2,3,4", false, "NVENTA2");
            //agrega cursos al grid
            addCurso();
        } else {
            alert("Es Necesario Elegir un Curso!");
        }
    }
    if (numPaso == 2) {
        TabsMapFactCofide("2", true, "NVENTA2");
        $("#tabsNVENTA2").tabs("option", "active", 2);
        TabsMapFactCofide("0,1,3,4", false, "NVENTA2");
        //diferente a resevración
        if (document.getElementById("CT_GRID").value != "3") {
            //si es factura oculta el boton
            if (!document.getElementById("CEV_FAC2").checked) {
                document.getElementById("CEV_BTNGUARDAR").parentNode.parentNode.style.display = "";
            } else {
                document.getElementById("CEV_BTNGUARDAR").parentNode.parentNode.style.display = "none";
            }
        } else {
            document.getElementById("CEV_BTNGUARDAR").parentNode.parentNode.style.display = "none";
        }
    }
    if (numPaso == 3) {
        var bolPromosion = "";
        if (document.getElementById("CT_PROMOSION").value == "1") {
            document.getElementById("CEV_FAC0").checked = true;
            document.getElementById("CEV_BTNGUARDAR").value = "ASIGNAR PROMOCIÓN";
        }
        //selecciona un tipo de documento, en inicio va a ser reservación
        if (document.getElementById("CEV_FAC0").checked || document.getElementById("CEV_FAC1").checked || document.getElementById("CEV_FAC2").checked) {
            if (document.getElementById("CEV_FAC2").checked) { // es factura
                if (document.getElementById("CEV_METODO").value != "" && document.getElementById("CEV_METODO").value != "0") {
                    if (document.getElementById("CEV_METODO").value == "PUE" && (document.getElementById("CEV_DESCRIPCION").value == "99" || document.getElementById("CEV_DESCRIPCION").value == "0")) {
                        alert("El metodo de PAGO EN UNA SOLA EXCIBICIÖN y forma de pago POR FEFINIR o SIN SELECCIONAR no estan permitidos.");
                    } else {
                        //TIPO DE VENTA = EDICIÓN && DETALLES DE VENTA SIN MODIFICACIONES 
                        if (strTipodeVenta != "3" && getValidaInfoFac() && bolValidaFactura()) {
                            TabsMapFactCofide("3", true, "NVENTA2");
                            $("#tabsNVENTA2").tabs("option", "active", 3);
                            TabsMapFactCofide("0,1,2,4", false, "NVENTA2");
                            //carga si es verdadero
                            if (bolVta) {
                                var strPost = "";
                                Vta_RazonSocial(strPost);//  
                            }
                        }
                    }
                } else {
                    alert("Es necesario seleccionar un METODO DE PAGO para continuar con el movimiento.");
                }
            } else {
                if (bolVta) { //brincar a observaciones desde resumen
                    TabsMapFactCofide("4", true, "NVENTA2");
                    $("#tabsNVENTA2").tabs("option", "active", 4);
                    TabsMapFactCofide("0,1,2,3", false, "NVENTA2");
                    document.getElementById("CEV_BTNGUARDAR").parentNode.parentNode.style.display = "";
                } else { //brincar a resumen desde observaciones
                    TabsMapFactCofide("2", true, "NVENTA2");
                    $("#tabsNVENTA2").tabs("option", "active", 2);
                    TabsMapFactCofide("0,1,3,4", false, "NVENTA2");
                }
            }
        } else {
            alert("Seleccione un documento para la Venta");
        }
    }
    if (numPaso == 4) {
        TabsMapFactCofide("4", true, "NVENTA2");
        $("#tabsNVENTA2").tabs("option", "active", 4);
        TabsMapFactCofide("0,1,2,3", false, "NVENTA2");
        document.getElementById("CEV_BTNGUARDAR").parentNode.parentNode.style.display = "";
    }
    if (numPaso == 5) { //continua 1
        TabsMapFactCofide("1", true, "NVENTA2");
        $("#tabsNVENTA2").tabs("option", "active", 1);
        TabsMapFactCofide("0,2,3,4", false, "NVENTA2");
        if (document.getElementById("CT_GRID").value != "3") {
            loadCursos(document.getElementById("id_mov_m").value);
        }
    }
}

/**
 * subir el comprobante de pago
 * @returns {undefined}
 */
function subirComprobantePago() {
    var strDoc = "";
    if (document.getElementById("CEV_NOM_FILE").value != "") {
        strDoc = document.getElementById("CEV_NOM_FILE").value;
    }
    var File = document.getElementById("CEV_COMPROBANTE");
    if (File.value == "") {
        alert("Requiere seleccionar un archivo");
        File.focus();
    } else {
        if (Right(File.value.toUpperCase(), 3) == "PDF" ||
                Right(File.value.toUpperCase(), 3) == "PNG" ||
                Right(File.value.toUpperCase(), 4) == "JPEG" ||
                Right(File.value.toUpperCase(), 3) == "JPG" ||
                Right(File.value.toUpperCase(), 3) == "DOC" ||
                Right(File.value.toUpperCase(), 4) == "DOCX" ||
                Right(File.value.toUpperCase(), 3) == "MSG") {
            $("#dialogWait").dialog("open");
            $.ajaxFileUpload({
                url: "COFIDE_UpComprobante.jsp?documento=" + strDoc,
                secureuri: false,
                fileElementId: "CEV_COMPROBANTE",
                dataType: "json",
                success: function (data, status) {
                    if (typeof (data.error) != "undefined") {
                        if (data.error != "") {
                            alert(data.error);
                        } else {
                            alert("Archivo guardado");
                            document.getElementById("CEV_NOM_FILE").value = data.msg;
                        }
                    }
                    $("#dialogWait").dialog("close");
                }, error: function (data, status, e) {
                    alert(e);
                    $("#dialogWait").dialog("close");
                }});
        } else {
            alert("Se aceptan archivos con extension pdf, png, jpg, doc, docx y Elementos de Outlook(.msg)");
            File.focus();
        }
    }
}
function _EvalExpRegCOFIDE1(YourValue, YourExp) {
    var Template = new RegExp(YourExp);
    return(Template.test(YourValue)) ? 1 : 0;
}

/**
 * 
 * @param {Int} ID_FAC_OLD - id del ticket o reservación que se va a facturar y/o modificar
 * @returns {nadita}
 */
function saveVtaCofide(ID_FAC_OLD) {

    //VARIABLES PARA LOS DATOS DE FACTURACIÓNvar strNumExt = "";    
    var strRazonSocial = "";
    var strRFC = "";
    var strCalle = "";
    var strColonia = "";
    var strMunicipio = "";
    var strEstado = "";
    var strCP = "";
    var strNumExt = "";
    var strNumint = "";
    var strCorreo = "";
    var strCorreo2 = "";
    var strTelefono = "";
    var DFA_ID = "0";
    //VARIABLES PARA LOS DATOS DE FACTURACIÓNvar strNumExt = "";
    if (ID_FAC_OLD == undefined || ID_FAC_OLD == "undefined") {
        ID_FAC_OLD = "";
    }

    var bolPromocion = document.getElementById("CT_PROMOSION").value;
    var strComent = document.getElementById("CEV_COMENT").value;
    var strObservacion = document.getElementById("CEV_COMENTARIO").value;
    var bolReserva = false;
    var strPOST = "";
    //bandera, de si es reservación
    var intEsReservacion = 0;

    // VENTANA DE VENTA QUE SE ABRE, SI ES 1: ES VENTA GENERAL, SI NO, ES DESDE EL CLIENTE ACTUAL
    var strTipoOperacion = document.getElementById("CT_GRID").value;

    /**
     * TIPO DE DOCUMENTO QUE SE VA A GENERAR
     * 1 - RESERVACIÓN
     * 2 - TICKET
     * 3 - FACTURA
     */
    var strTipoDoc = 1;
    if (document.getElementById("CEV_FAC0").checked) {

        //RESERVACIÓN
        strTipoDoc = 1;
        intEsReservacion = 1;
    } else if (document.getElementById("CEV_FAC1").checked) {

        //TICKET
        strTipoDoc = 2;
    } else if (document.getElementById("CEV_FAC2").checked) {

        //FACTURA
        strTipoDoc = 3;
    }

    //DATOS CLIENTE
    strRazonSocial = document.getElementById("CT_RAZONSOCIAL").value;
    strRFC = document.getElementById("CT_RFC").value;
    strCalle = document.getElementById("CT_CALLE").value;
    strColonia = document.getElementById("CT_COL").value;
    strMunicipio = document.getElementById("CT_MUNI").value;
    strEstado = document.getElementById("CT_EDO").value;
    strCP = document.getElementById("CT_CP").value;
    strNumExt = document.getElementById("CT_NUM").value;
    strCorreo = document.getElementById("CT_CORREO").value;
    strCorreo2 = document.getElementById("CT_CORREO2").value;
    strTelefono = document.getElementById("CT_CONTACTO").value;

    //PARA FACTURAR
    var intID_TKT = document.getElementById("id_vta").value;
    var grid = jQuery("#GRD_RAZONES");
    var lstRow = grid.getRowData(grid.getGridParam("selrow"));

    //TICKET O RESERVACIÓN
    var strPrefijoMaster = "TKT";
    var strPrefijoDeta = "TKTD";
    var strKey = "TKT_ID";
    var strNomFormat = "TICKET";

    if (strTipoDoc === 3) {

        //ASIGNA VALORES A LAS VARIABLES, SI, PROVIENE DESDE UNA FACTURACIÓN
        strRazonSocial = lstRow.CEV_RAZONSOCIAL;
        strRFC = lstRow.CEV_RFC;
        strCalle = lstRow.CEV_CALLE;
        strColonia = lstRow.CEV_COLONIA;
        strMunicipio = lstRow.CEV_MUNICIPIO;
        strEstado = lstRow.CEV_ESTADO;
        strCP = lstRow.CEV_CP;
        strNumExt = lstRow.CEV_NUMERO;
        strNumint = lstRow.CEV_NUMINT;
        strTelefono = lstRow.CEV_TELEFONO;
        strCorreo = lstRow.CEV_EMAIL1;
        strCorreo2 = lstRow.CEV_EMAIL2;
        DFA_ID = lstRow.CEV_ID_FAC;

        //FACTURA
        strPrefijoMaster = "FAC";
        strPrefijoDeta = "FACD";
        strKey = "FAC_ID";
        strNomFormat = "FACTURA";
    }
    var intRFac = 0;
    if (document.getElementById("CEV_RFAC1").checked) {
        intRFac = 1;
    }

    if (saveVtaCofideValida()) { //valida que la información este completa        

        var strIdMov = document.getElementById("id_mov_m").value; //id del movimiento donde se encuentran las partidas de la venta      

        console.log("datos maestro inicio");
        var precioreal = document.getElementById("precio_base").value.replace(",", ""); //suma de precios unitarios de lso productos
        var descuento = document.getElementById("CEV_DESCUENTO").value.replace(",", ""); // suma de descuentos de los productos
        var importe = parseFloat(precioreal) - parseFloat(descuento); // importe de la venta
        var impuesto = document.getElementById("CEV_SUB2").value.replace(",", ""); // impuestos de la venta
        var total = document.getElementById("CEV_SUB3").value.replace(",", ""); // monto total
        console.log("datos maestro fin");

        //ID DE FACTURA CANCELADA, PARA NUEVA RESERVACIÓN A FACTURAR
        strPOST += "&ID_FAC_OLD=" + ID_FAC_OLD;

        //MAESTRO
        strPOST += "&IDMOV=" + strIdMov; //ID DE MOVIMIENTO
        strPOST += "&promo=" + bolPromocion;
        strPOST += "&esReservacion=" + intEsReservacion;
        strPOST += "&refactura=" + intRFac;
        strPOST += "&SC_ID=" + intSucDefa;
        strPOST += "&CT_ID=" + document.getElementById("CT_ID").value; //del cliente
        strPOST += "&CEV_MPUBLICIDAD=" + document.getElementById("CEV_MPUBLICIDAD").value;
        strPOST += "&" + strPrefijoMaster + "_MONEDA=1";
        strPOST += "&" + strPrefijoMaster + "_NOTAS=" + encodeURIComponent(strObservacion);
        strPOST += "&CT_COMENTARIO=" + encodeURIComponent(strComent); //comentarios del ejecutivo
        strPOST += "&" + strPrefijoMaster + "_ESSERV=1";

        strPOST += "&" + strPrefijoMaster + "_IMPORTE=" + importe; //importe = suma de importes ((pu * cantidad) - desc)
        strPOST += "&" + strPrefijoMaster + "_DESCUENTO=" + descuento; //descuento
        strPOST += "&" + strPrefijoMaster + "_IMPUESTO1=" + impuesto; //IVA
        strPOST += "&" + strPrefijoMaster + "_TOTAL=" + total; //total

        strPOST += "&" + strPrefijoMaster + "_IMPUESTO2=" + 0;
        strPOST += "&" + strPrefijoMaster + "_IMPUESTO3=" + 0;
        strPOST += "&" + strPrefijoMaster + "_RETISR=0";
        strPOST += "&" + strPrefijoMaster + "_RETIVA=0";
        strPOST += "&" + strPrefijoMaster + "_NETO=0";
        strPOST += "&" + strPrefijoMaster + "_REFERENCIA=" + document.getElementById("CEV_DIGITO").value;
        strPOST += "&" + strPrefijoMaster + "_CONDPAGO=";

        strPOST += "&" + strPrefijoMaster + "_FORMADEPAGO=" + document.getElementById("CEV_DESCRIPCION").value; // PUE / PPD        
        strPOST += "&" + strPrefijoMaster + "_NUMCUENTA=";  //numero de cuenta, pendiente
        strPOST += "&" + strPrefijoMaster + "_METODOPAGO=" + document.getElementById("CEV_METODO").value; //forma efectivo, cheque, transfer
        strPOST += "&CEV_USO_CFDI=" + document.getElementById("CEV_USO_CFDI").value; //uso del cfdi
        strPOST += "&" + strPrefijoMaster + "_TASA1=" + 16;
        strPOST += "&" + strPrefijoMaster + "_TASA2=" + 0;
        strPOST += "&" + strPrefijoMaster + "_TASA3=" + 0;
        strPOST += "&" + "TI_ID=" + 1;
        strPOST += "&" + "TI_ID2=" + 0;
        strPOST += "&" + "TI_ID3=" + 0;

        strPOST += "&CEV_NOM_FILE=" + document.getElementById("CEV_NOM_FILE").value;
        strPOST += "&CEV_FECHAPAGO=" + document.getElementById("CEV_FECHAPAGO").value;

        //DATOS FISCALES
        strPOST += "&CT_RAZONSOCIAL=" + encodeURIComponent(strRazonSocial); //razon social del CLIENTE principal       
        strPOST += "&CT_RFC=" + encodeURIComponent(strRFC); //rfc del cliente seleccionado
        strPOST += "&CT_CORREO=" + strCorreo; //correo principal
        strPOST += "&CT_CORREO2=" + strCorreo2;
        strPOST += "&CT_CP=" + strCP;
        strPOST += "&CT_COL=" + strColonia;
        strPOST += "&CT_NUM=" + strNumExt;
        strPOST += "&CT_NUMINT=" + strNumint;
        strPOST += "&CT_CALLE=" + strCalle;
        strPOST += "&CT_MUNICIPIO=" + strMunicipio;
        strPOST += "&CT_ESTADO=" + strEstado;
        strPOST += "&CT_TELEFONO=" + strTelefono;
        strPOST += "&CT_NOMBRE=" + document.getElementById("CT_CONTACTO_ENTRADA").value;
        strPOST += "&FAC_SERIE=C"; //serie de los ticket y factura
        strPOST += "&" + strPrefijoMaster + "_REGIMENFISCAL=601"; //ID REGIMEN FISCAL  
        strPOST += "&CEV_PAGO_OK=" + document.getElementById("CEV_PAGO_OK").value; //ya se pago? 0 - aun no se paga / 1 - ya tiene un pago   TELEMARKETING.SENDMAIL();

        strPOST += "&TIPOVENTA=" + strTipoDoc;
        strPOST += "&DFA_ID=" + DFA_ID;

        strPOST += "&vta_nvo=" + document.getElementById("CT_CTE_NUEVO").value; //1 es nuevo cliente en la venta
        strPOST += "&pagoOk=" + document.getElementById("CEV_PAGO_OK").value; // si es 1, ya se pago, no enviar nada / si es 0 Y se adjunta pago, envia accesos/material-promosión de presencial +200pts

        //DETALLE
        strPOST += "&" + strPrefijoDeta + "_TASAIVA1=" + 16;
        strPOST += "&" + strPrefijoDeta + "_NOTAS=" + encodeURIComponent(strComent);
        strPOST += "&" + strPrefijoDeta + "_UNIDAD_MEDIDA=" + "678";
        strPOST += "&" + strPrefijoDeta + "_CVE_PRODSERV=" + "86101700";
        strPOST += "&" + strPrefijoDeta + "_CVE=" + "....";

        //la venta es nueva, si viene el id venta en vacio
        if (intID_TKT == "") {
//            alert("es nueva ventaS");
            strPOST += "&venta_nueva=1";
        } else {
//            alert("nooo, asi no!");
            strPOST += "&venta_nueva=0";
        }

        $.ajax({
            type: "POST",
            data: encodeURI(strPOST),
            scriptCharset: "utf-8",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "html",
            url: "COFIDE_Tmk_vta.jsp?ID=9",
            beforeSend: function () {
                $("#dialogWait").dialog("open");
            },
            success: function (dato) {

                dato = trim(dato);

                if (Left(dato, 3) === "OK.") {

                    cerrarVtaCofide();

                    if (strTipoOperacion === "1") {

                        //VENTA DE CLEINTE EN HISTORIAL DE VENTAS GENERAL
                        consultaHistoricoVtas(); //historial de ventas general
                    } else {

                        //VENTA DE CLIENTE ACTUAL
                        HistorialVtaCte(document.getElementById("CT_NO_CLIENTE").value); //actualiza el historial de ventas nuevo
                        LoadContacto(document.getElementById("CT_NO_CLIENTE").value); //recarga los contactos
                    }

                    if (strTipoDoc === 3) {

                        //AL GENERAR LA FACTURA CON EXITO, ELININA LA VENTA ANTERIOR
                        ChnageVta(intID_TKT);
                        //envio de factura por correo con xml adjunto
//                        SendFacturaXML(intID_TKT);
                        console.log("######## RESULTADO DE LA VENTA: \n" + dato.replace("OK.", "") + " \n########");
                        SendFacturaXML(dato.replace("OK.", ""));
                    }

                } else {

                    alert("ERROR: AL INTENTAR GENERAR LA VENTA: " + dato);

                }
                $("#dialogWait").dialog("close");
            }, error: function (objeto, quepaso, otroobj) {

                alert(":Cofide tmk=9:" + objeto + " " + quepaso + " " + otroobj);
                $("#dialogWait").dialog("close");

            }});
    }
}

function saveVtaCofideValida() {
    var bolValido = true;

    if (intRowCursos() == 0) {

        alert("Debe seleccionar por lo menos un curso");
        bolValido = false;

    }

    if (document.getElementById("CEV_FAC2").checked && (document.getElementById("tipo_doc").value != "FACTURA") || document.getElementById("CEV_RFAC1").checked) { //si es factura y/o refactura, debe estar seleccionada una razón social

        var grid = jQuery("#GRD_RAZONES");

        if (grid.getGridParam("selrow") == null) {

            alert("Debe seleccionar por lo menos una razon social para facturar");
            bolValido = false;

        }

    }

    return bolValido;
}

function llenarColoniaFac() {
    var strCp = document.getElementById("CEV_CP").value;
    var objColoniaCombo = document.getElementById("CEV_COLONIA");
    if (strCp != "") {
        var strPost = "CEV_CP=" + strCp;
        $("#dialogWait").dialog("open");
        $.ajax({type: "POST", data: strPost, scriptCharset: "UTF-8"
            , contentType: "application/x-www-form-urlencoded;charset=utf-8"
            , cache: false
            , dataType: "xml"
            , url: "COFIDE_Telemarketing_vta.jsp?ID=3"
            , success: function (datos) {
                select_clear(objColoniaCombo);
                var lstXml = datos.getElementsByTagName("General")[0];
                var lstCte = datos.getElementsByTagName("Colonia");
                for (var i = 0; i < lstCte.length; i++) {
                    var objColonia = lstCte[i];
                    select_add(objColoniaCombo, objColonia.getAttribute("CMX_COLONIA"), objColonia.getAttribute("CMX_COLONIA"));
                }
                if (lstXml != null) {
                    document.getElementById("CEV_ESTADO").value = lstXml.getAttribute("CMX_ESTADO");
                    document.getElementById("CEV_MUNICIPIO").value = lstXml.getAttribute("CMX_MUNICIPIO");
                }
                $("#dialogWait").dialog("close");
            }});
    } else {
        select_clear(objColoniaCombo);
    }
}


var strIdCliente = "0"; //id del cliente
function editParticipante() {
    var grid = jQuery("#GRIDPARTICIPA");
    if (grid.getGridParam("selrow") != null) {
        newTVenta();//muestra los campos
        var lstRow = grid.getRowData(grid.getGridParam("selrow"));
        document.getElementById("CEV_TITULO").value = lstRow.CCO_TITULO;
        document.getElementById("CEV_NOMBRE").value = lstRow.CCO_NOMBRE;
        document.getElementById("CEV_APPATERNO").value = lstRow.CCO_APPATERNO;
        document.getElementById("CEV_APMATERNO").value = lstRow.CCO_APMATERNO;
        document.getElementById("CEV_ASOCIACION").value = lstRow.CCO_ASOCIACION;
        document.getElementById("CEV_NUMERO").value = intDato(lstRow.CCO_NOSOCIO);
        document.getElementById("CEV_CORREO").value = lstRow.CCO_CORREO;
        if (lstRow.MATERIAL == "SI") {
            document.getElementById("material").checked = true;
        } else {
            document.getElementById("material").checked = false;
        }
        idContacto = lstRow.CONTACTO_ID;
        delGridCola();
        itemIdCob = lstRow.CONTACTO_ID; //id PK para guiarse
        //guardar en temporal
        titulo = lstRow.CCO_TITULO;
        nombre = lstRow.CCO_NOMBRE;
        apellidop = lstRow.CCO_APPATERNO;
        apellidom = lstRow.CCO_APMATERNO;
        asoc = lstRow.CCO_ASOCIACION;
        asonum = lstRow.CCO_NOSOCIO;
        correo = lstRow.CCO_CORREO;
        contacto_id = lstRow.CONTACTO_ID;
        strIdCliente = lstRow.CT_ID;
        material = lstRow.MATERIAL;
        // opcion temporal E
        opcParticipante = "E";
    } else {
        alert("Elige un participante");
    }
}
function ValidaNumSoc() {
    var ok = false;

    if (document.getElementById("CEV_ASOCIACION").value == "NINGUNA") {

        ok = true;

    } else {
//en espera de validación de numeros de asociación
        ok = true;

    }

    return ok;
}
//function ValidaDescuento() {
//    var strFechaActual = "";
//    var intDescuento = 0;
//    var strDesc = document.getElementById("CEV_DESC").value;
//    var strFechaIni = document.getElementById("CEV_FECINICIO").value;
//    $.ajax({
//        type: "POST",
//        data: "",
//        ptCharset: "UTF-8",
//        contentType: "application/x-www-form-urlencoded;charset=utf-8",
//        cache: false,
//        dataType: "xml",
//        url: "COFIDE_Telemarketing_vta.jsp?ID=7",
//        success: function (datos) {
//            var lstXml = datos.getElementsByTagName("vta")[0];
//            var lstCte = lstXml.getElementsByTagName("datos");
//            for (var i = 0; i < lstCte.length; i++) {
//                var objcte = lstCte[i];
//                strFechaActual = objcte.getAttribute("fecha");
//            }
//            if (strFechaActual != strFechaIni) {
//                if (strFechaActual.substring(0, 6) == "201705") {
//                    intDescuento = 25; //durante mayo del 2017, permitira dar el 20% de descuento
//                } else {
//                    intDescuento = 15; // pedir permisos
//                }
//                if (strDesc >= intDescuento) {
//                    alert("ingresa contraseña");
//                    OpnDiagSupr();
//                } else {
//                    document.getElementById("BOLDESC").value = "1";
//                    CalcDescuento();
//                }
//            } else {
//                alert("No se puede aplicar descuento el mismo dia del evento");
//                document.getElementById("CEV_DESC").value = "";
//            }
//        }});
//}
function OpnDiagSupr() {
    var objSecModiVta = objMap.getScreen("AUT_SUP");
    if (objSecModiVta != null) {
        objSecModiVta.bolActivo = false;
        objSecModiVta.bolMain = false;
        objSecModiVta.bolInit = false;
        objSecModiVta.idOperAct = 0;
    }
    OpnOpt("AUT_SUP", "_ed", "dialogCte", false, false, true);
}
function ValSupr() {
    var sup = document.getElementById("SC_NOMBRE").value;
    var strPassword = document.getElementById("SC_PASSWORD").value;
    var strPass = "";
    var strPost = "SC_NOMBRE=" + sup;
    if (sup != "") {
        $.ajax({
            type: "POST",
            data: strPost,
            scriptCharset: "UTF-8",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "xml",
            url: "COFIDE_Telemarketing_vta.jsp?ID=6",
            success: function (datos) {
                var lstXml = datos.getElementsByTagName("vta")[0];
                var lstCte = lstXml.getElementsByTagName("datos");
                for (var i = 0; i < lstCte.length; i++) {
                    var objcte = lstCte[i];
                    strPass = objcte.getAttribute("SC_PASSWORD");
                }
                if (strPassword == strPass) {
                    document.getElementById("BOLDESC").value = "1";
                    alert("Autorizado!");
//                    CalcDescuento();
                    $("#dialogCte").dialog("close");
                } else {
                    alert("Contraseña Ivalida, no se Autorizo el descuento");
                    document.getElementById("CEV_DESC").value = "";
                }
            }});
    } else {
        alert("Selecciona a un Supervisor");
    }
}
function LoadParticipantes(opc) {
//    alert("venta nueva: " + opc);
//    if (opc == 1) { //participantes nuevos


    var grid = jQuery("#CURSOS_GRD"); //grid o pantalla
    var lstVal = grid.getRowData(grid.getGridParam("selrow"));
    var strIdMaster = lstVal.CC_MOV_ID;
    var strIdCurso = lstVal.CURSO_ID;
    var strTipoCurso = "1"; //presencial


    if (document.getElementById("CT_GRID").value == "3" || lstVal.LUGARES == "0") { //participantes nuevos

        if (document.getElementById("CT_PROMOSION").value == "0") {
//            alert("carga participantes nuevos");
            var intIdCte = document.getElementById("CT_NO_CLIENTE").value;
            jQuery("#GRIDPARTICIPA").clearGridData();
            var strPost = "CT_ID=" + intIdCte;
            $.ajax({
                type: "POST",
                data: strPost,
                scriptCharset: "UTF-8",
                contentType: "application/x-www-form-urlencoded;charset=utf-8",
                cache: false,
                dataType: "xml",
                url: "COFIDE_Telemarketing.jsp?ID=18",
                beforeSend: function () {
                    $("#dialogWait").dialog("open");
                },
                success: function (datos) {
                    var lstXml = datos.getElementsByTagName("vta")[0];
                    var lstCte = lstXml.getElementsByTagName("datos");
                    for (var i = 0; i < lstCte.length; i++) {
                        var objcte = lstCte[i];
                        var datarow = {

                            CCO_ID: itemIdCob,
                            CCO_NOMBRE: objcte.getAttribute("CCO_NOMBRE"),
                            CCO_APPATERNO: objcte.getAttribute("CCO_APPATERNO"),
                            CCO_APMATERNO: objcte.getAttribute("CCO_APMATERNO"),
                            CCO_TITULO: objcte.getAttribute("CCO_TITULO"),
                            CCO_NOSOCIO: objcte.getAttribute("CCO_NOSOCIO"),
                            CCO_ASOCIACION: objcte.getAttribute("CCO_ASOCIACION"),
                            CCO_CORREO: objcte.getAttribute("CCO_CORREO"),
                            CT_ID: objcte.getAttribute("CT_ID"),
                            MATERIAL: "NO",
                            CONTACTO_ID: objcte.getAttribute("CONTACTO_ID")
                        };
                        itemIdCob++;
                        jQuery("#GRIDPARTICIPA").addRowData(itemIdCob, datarow, "last");
                        $("#dialogWait").dialog("close");
                    }
                    document.getElementById("PAR_DESCUENTO_POR").disabled = false;
                    document.getElementById("ADDBTN_PAR").disabled = false;
                    document.getElementById("DELBTN_PAR").disabled = false;
                }, error: function (objeto, quepaso, otroobj) {
                    alert(":pto=18:" + objeto + " " + quepaso + " " + otroobj);
                    $("#dialogWait").dialog("close");
                }
            });
        } else { //promoción = 1
//            alert("carga participantes con promosion");
            strPost = "contacto_id=" + opc;
            jQuery("#GRIDPARTICIPA").clearGridData();
            $.ajax({
                type: "POST",
                data: strPost,
                scriptCharset: "UTF-8",
                contentType: "application/x-www-form-urlencoded;charset=utf-8",
                cache: false,
                dataType: "xml",
                url: "COFIDE_Tmk_vta.jsp?ID=11",
                beforeSend: function () {
                    $("#dialogWait").dialog("open");
                },
                success: function (datos) {

                    var lstXml = datos.getElementsByTagName("vta")[0];
                    var lstCte = lstXml.getElementsByTagName("datos");
                    for (var i = 0; i < lstCte.length; i++) {
                        var objcte = lstCte[i];
                        var datarow = {

                            CCO_ID: itemIdCob,
                            CCO_NOMBRE: objcte.getAttribute("CCO_NOMBRE"),
                            CCO_APPATERNO: objcte.getAttribute("CCO_APPATERNO"),
                            CCO_APMATERNO: objcte.getAttribute("CCO_APMATERNO"),
                            CCO_TITULO: objcte.getAttribute("CCO_TITULO"),
                            CCO_NOSOCIO: objcte.getAttribute("CCO_NOSOCIO"),
                            CCO_ASOCIACION: objcte.getAttribute("CCO_ASOCIACION"),
                            CCO_CORREO: objcte.getAttribute("CCO_CORREO"),
                            CT_ID: objcte.getAttribute("CT_ID"),
                            MATERIAL: objcte.getAttribute("MATERIAL"),
                            CP_OBSERVACIONES: objcte.getAttribute("CP_OBSERVACIONES"),
                            CONTACTO_ID: objcte.getAttribute("CONTACTO_ID")

                        };
                        itemIdCob++;
                        jQuery("#GRIDPARTICIPA").addRowData(itemIdCob, datarow, "last");
                        $("#dialogWait").dialog("close");
                    }
                    document.getElementById("PAR_DESCUENTO_POR").disabled = true;
                    document.getElementById("ADDBTN_PAR").disabled = true;
                    document.getElementById("DELBTN_PAR").disabled = true;
//                    alert("bloqueo de campos");

                }, error: function (objeto, quepaso, otroobj) {
                    alert(":pto=11:" + objeto + " " + quepaso + " " + otroobj);
                    $("#dialogWait").dialog("close");
                }});

        }
    } else { //participantes registrados en el curso
//        alert("es una edición de venta");
//        alert("carga participantes ya inscritos antes");

//        var grid = jQuery("#CURSOS_GRD"); //grid o pantalla
//        var lstVal = grid.getRowData(grid.getGridParam("selrow"));
//        var strIdMaster = lstVal.CC_MOV_ID;
//        var strIdCurso = lstVal.CURSO_ID;
//        var strTipoCurso = "1"; //presencial

        if (lstVal.TIPO_CURSO == "EN LINEA") {
            strTipoCurso = "2";
        }

        if (lstVal.TIPO_CURSO == "VIDEO CURSO") {
            strTipoCurso = "3";
        }

        if (lstVal.TIPO_CURSO == "COFIDEnet") {
            strTipoCurso = "4";
        }

        var strPost = "";

        strPost = "idmaster=" + strIdMaster;
        strPost += "&idcurso=" + strIdCurso;
        strPost += "&tipocurso=" + strTipoCurso;

        jQuery("#GRIDPARTICIPA").clearGridData();
        $.ajax({
            type: "POST",
            data: strPost,
            scriptCharset: "UTF-8",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "xml",
            url: "COFIDE_Tmk_vta.jsp?ID=6",
            beforeSend: function () {
                $("#dialogWait").dialog("open");
            },
            success: function (datos) {

                var lstXml = datos.getElementsByTagName("vta")[0];
                var lstCte = lstXml.getElementsByTagName("datos");
                for (var i = 0; i < lstCte.length; i++) {
                    var objcte = lstCte[i];
                    var datarow = {

                        CCO_ID: itemIdCob,
                        CCO_NOMBRE: objcte.getAttribute("CCO_NOMBRE"),
                        CCO_APPATERNO: objcte.getAttribute("CCO_APPATERNO"),
                        CCO_APMATERNO: objcte.getAttribute("CCO_APMATERNO"),
                        CCO_TITULO: objcte.getAttribute("CCO_TITULO"),
                        CCO_NOSOCIO: objcte.getAttribute("CCO_NOSOCIO"),
                        CCO_ASOCIACION: objcte.getAttribute("CCO_ASOCIACION"),
                        CCO_CORREO: objcte.getAttribute("CCO_CORREO"),
                        CT_ID: objcte.getAttribute("CT_ID"),
                        MATERIAL: objcte.getAttribute("MATERIAL"),
                        CP_OBSERVACIONES: objcte.getAttribute("CP_OBSERVACIONES"),
                        CONTACTO_ID: objcte.getAttribute("CONTACTO_ID")

                    };
                    itemIdCob++;
                    jQuery("#GRIDPARTICIPA").addRowData(itemIdCob, datarow, "last");
                    $("#dialogWait").dialog("close");
                }
                document.getElementById("PAR_DESCUENTO_POR").disabled = false;
                document.getElementById("ADDBTN_PAR").disabled = false;
                document.getElementById("DELBTN_PAR").disabled = false;
            }, error: function (objeto, quepaso, otroobj) {
                alert(":pto=6:" + objeto + " " + quepaso + " " + otroobj);
                $("#dialogWait").dialog("close");
            }});

    }
}
//SI ES UNA FACTURA, NO SE PODRA EDITAR NINGUN CAMPO
function BloqCampos() {
    document.getElementById("CEV_NOMCURSO").style["background"] = "#e0f8e6";
    document.getElementById("CEV_NOMCURSO").readOnly = true;
    document.getElementById("CEV_DESCRIPCION").disabled = true;
    document.getElementById("CEV_METODO").disabled = true;
    document.getElementById("CEV_USO_CFDI").disabled = true;
}
function Vta_Deta() { //consulta la venta seleccionada    
    var strIDFAC = document.getElementById("CT_ID_FACTKT").value;
    var strTipoDoc = document.getElementById("CT_TIPODOC").value;
    var PorDesc = "";
    var strPost = "";
    strPost = "strFac_Tkt=" + strIDFAC;
    strPost += "&strTipoDoc=" + strTipoDoc;
    if (strTipoDoc == "FACTURA") { //si es factura, deshabilita todas las opciones, ya no hay regreso del documento
        $("#CEV_FAC1").attr("disabled", "disabled");//tkt
        $("#CEV_FAC0").attr("disabled", "disabled");//reserva
        document.getElementById("CEV_RFAC1").parentNode.parentNode.style.display = "";
    } else {
        if (strTipoDoc == "TICKET") { // si es ticket, solo puede pasar a ser factura o seguir en ticket
            $("#CEV_FAC0").attr("disabled", "disabled");
        }
    }
    $.ajax({
        type: "POST",
        data: strPost,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "COFIDE_Historial.jsp?ID=4",
        success: function (datos) {
            //jQuery("#GRDIHVENTA").clearGridData();
            var objsc = datos.getElementsByTagName("Ventas")[0];
            var lstVtas = objsc.getElementsByTagName("datos");
            for (var i = 0; i < lstVtas.length; i++) {
                var obj = lstVtas[i];
                PorDesc = obj.getAttribute("PORDESC");
                PorDesc = PorDesc.replace(".00", "");

                if (PorDesc > "0") {
                    document.getElementById("BOLDESC").value = "1";
                }

                var strNomPago = obj.getAttribute("ARCHIVO");
                if (strNomPago != "") {
                    //pintar campo con pago
                    document.getElementById("CEV_NOM_FILE").parentNode.parentNode.style.display = "";
                    document.getElementById("CEV_NOM_FILE").value = strNomPago;
                    document.getElementById("CEV_PAGO_OK").value = "1"; //ya se encuentra pagado
                } else {
                    document.getElementById("CEV_NOM_FILE").parentNode.parentNode.style.display = "none";
                    document.getElementById("CEV_PAGO_OK").value = "0"; //se va a pagar en la edición
                }
                document.getElementById("CEV_DIGITO").value = obj.getAttribute("DIGITO");
                document.getElementById("CEV_PARTICIPANTE").value = obj.getAttribute("CANTIDAD");
                document.getElementById("num_participante").value = obj.getAttribute("CANTIDAD"); //numero que no se puede superar  para facturar
                document.getElementById("CEV_NOMCURSO").value = obj.getAttribute("CURSO");
                document.getElementById("CEV_NOMCURSO_TMP").value = obj.getAttribute("CURSO");
                document.getElementById("CEV_IDCURSO").value = obj.getAttribute("CURSO_ID");
                document.getElementById("CEV_FECHA").value = obj.getAttribute("CURSO_FECHA");
                document.getElementById("CC_HORARIO").value = obj.getAttribute("HORARIO");
                document.getElementById("CC_SEDE").value = obj.getAttribute("SEDE");
                var strTipo = obj.getAttribute("TIPOCURSO");
                document.getElementById("CEV_PRECIO_UNIT").value = FormatNumber(obj.getAttribute("COSTO_UNIT"), 2, true, false, true);
                document.getElementById("CEV_SUB1").value = obj.getAttribute("COSTO_SUB");
                document.getElementById("CEV_SUB3").value = obj.getAttribute("COSTO_TOT");
                document.getElementById("CEV_SUB2").value = obj.getAttribute("IVA");
                document.getElementById("CEV_DESC").value = PorDesc;
                document.getElementById("CEV_DESCRIPCION").value = obj.getAttribute("METPAGO");
                document.getElementById("CEV_FECHAPAGO").value = obj.getAttribute("FECHA_PAGO");
                if (strTipo == "1") { //presencual
                    document.getElementById("CEV_TIPO_CURSO0").checked = true;
                    document.getElementById("CEV_LIMITE").parentNode.parentNode.style.display = "";
                    document.getElementById("CEV_OCUPADO").parentNode.parentNode.style.display = "";
                    document.getElementById("CEV_MIMP1").parentNode.parentNode.style.display = "";
                    document.getElementById("CEV_LIMITE").value = obj.getAttribute("LIMITE");
                    document.getElementById("CEV_OCUPADO").value = obj.getAttribute("OCUPADOS");
                }
                if (strTipo == "2") { //online
                    document.getElementById("CEV_TIPO_CURSO1").checked = true;
                    document.getElementById("CEV_LIMITE").parentNode.parentNode.style.display = "none";
                    document.getElementById("CEV_OCUPADO").parentNode.parentNode.style.display = "none";
                    document.getElementById("CEV_MIMP1").parentNode.parentNode.style.display = "none";
                }
                if (strTipo == "3") { //video
                    document.getElementById("CEV_TIPO_CURSO2").checked = true;
                    document.getElementById("CEV_LIMITE").parentNode.parentNode.style.display = "none";
                    document.getElementById("CEV_OCUPADO").parentNode.parentNode.style.display = "none";
                    document.getElementById("CEV_MIMP1").parentNode.parentNode.style.display = "none";
                }
                if (strTipo == "4") { //COFIDE.NET
                    document.getElementById("CEV_TIPO_CURSO3").checked = true;
                    document.getElementById("CEV_MEMBRESIA").value = obj.getAttribute("CURSO_ID");
                    document.getElementById("CEV_MEMBRESIA").parentNode.parentNode.style.display = "";
                    document.getElementById("CEV_LIMITE").parentNode.parentNode.style.display = "none";
                    document.getElementById("CEV_OCUPADO").parentNode.parentNode.style.display = "none";
                    document.getElementById("CEV_FECHA").parentNode.parentNode.style.display = "none";
                    document.getElementById("CEV_IDCURSO").parentNode.parentNode.style.display = "none";
                    document.getElementById("CC_HORARIO").parentNode.parentNode.style.display = "none";
                    document.getElementById("CC_SEDE").parentNode.parentNode.style.display = "none";
                    document.getElementById("CEV_NOMCURSO").parentNode.parentNode.style.display = "none";
                    document.getElementById("CEV_MPUBLICIDAD").parentNode.parentNode.style.display = "none";
                    document.getElementById("CEV_MEMBRESIA").parentNode.parentNode.style.display = "";
                }
                if (obj.getAttribute("MAT_IMP") == "1") {
                    $("#CEV_MIMP1").prop("checked", true);
                } else {
                    $("#CEV_MIMP2").prop("checked", true);
                }
                if (obj.getAttribute("tipodocumento") == "0") { //factura
                    $("#CEV_FAC2").prop("checked", true);
                }
                if (obj.getAttribute("tipodocumento") == "1") { //ticket
                    $("#CEV_FAC1").prop("checked", true);
                }
                if (obj.getAttribute("tipodocumento") == "2") { //reservacion
                    $("#CEV_FAC0").prop("checked", true);
                }
                document.getElementById("CEV_COMENT").value = obj.getAttribute("COMENTARIO");
                document.getElementById("CEV_FECINICIO").value = obj.getAttribute("CURSO_FECHA");
            }
            $("#dialogWait").dialog("close");
            Vta_Participantes(strPost);
//            Vta_RazonSocial(strPost);
        }, error: function (objeto, quepaso, otroobj) {
            alert(":ptoExist:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }});
}
function Vta_Participantes(strPost) {
    jQuery("#GRIDPARTICIPA").clearGridData();
    $.ajax({
        type: "POST",
        data: strPost,
        scriptCharset: "UTF-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "COFIDE_Historial.jsp?ID=5",
        success: function (datos) {
            var lstXml = datos.getElementsByTagName("vta")[0];
            var lstCte = lstXml.getElementsByTagName("datos");
            for (var i = 0; i < lstCte.length; i++) {
                var objcte = lstCte[i];
                var datarow = {
                    CCO_ID: objcte.getAttribute("CCO_ID"),
                    CCO_NOMBRE: objcte.getAttribute("CCO_NOMBRE"),
                    CCO_APPATERNO: objcte.getAttribute("CCO_APPATERNO"),
                    CCO_APMATERNO: objcte.getAttribute("CCO_APMATERNO"),
                    CCO_TITULO: objcte.getAttribute("CCO_TITULO"),
                    CCO_NOSOCIO: objcte.getAttribute("CCO_NOSOCIO"),
                    CCO_ASOCIACION: objcte.getAttribute("CCO_ASOCIACION"),
                    CCO_CORREO: objcte.getAttribute("CCO_CORREO"),
                    CP_OBSERVACIONES: objcte.getAttribute("CP_OBSERVACIONES"),
                    CT_ID: objcte.getAttribute("CT_ID"),
                    MATERIAL: objcte.getAttribute("MATERIAL"),
                    CONTACTO_ID: objcte.getAttribute("CONTACTO_ID")
                };
                itemIdCob++;
                jQuery("#GRIDPARTICIPA").addRowData(itemIdCob, datarow, "last");
                Vta_RazonSocial(strPost);
            }
        }});
}

function Vta_RazonSocial(strPost) {
    var strIdVta = document.getElementById("id_vta").value;
    var strCTID = document.getElementById("CT_NO_CLIENTE").value;
    strPost += "&strFac_Tkt=" + strIdVta;
    strPost += "&ct_id=" + strCTID;
    $.ajax({
        type: "POST",
        data: encodeURI(strPost),
        scriptCharset: "UTF-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "COFIDE_Historial.jsp?ID=6",
        success: function (datos) {
            jQuery("#GRD_RAZONES").clearGridData();
            var lstXml = datos.getElementsByTagName("vta")[0];
            var lstCte = lstXml.getElementsByTagName("datos");
            for (var i = 0; i < lstCte.length; i++) {
                var objcte = lstCte[i];
                var datarow = {
                    CEV_ID_FAC: objcte.getAttribute("CEV_ID"), //id de la información fiscal
                    CEV_RAZONSOCIAL: objcte.getAttribute("CEV_NOMBRE"),
                    CEV_NUMERO: objcte.getAttribute("CEV_NUMEROEXT"),
                    CEV_NUMINT: objcte.getAttribute("CEV_NUMEROINT"),
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
                jQuery("#GRD_RAZONES").addRowData(itemIdFactDat, datarow, "last");
            }
        }});
}

/*RESIVAR PROCESOS*/
function doFacturaHistoricoVtas() { //proviene del historial de ventas general

    var strPost = "";
    var grid = jQuery("#GRDIHVENTA");
    var id = grid.getGridParam("selrow");
    var lstRow = grid.getRowData(id);
    var strNomComp = document.getElementById("CEV_NOM_FILE").value;                                     //nombre del archivo

    var strID_TKT_FAC = document.getElementById("id_vta").value; //tipo de documento actual
    var strTipoventaDocumento = document.getElementById("tipo_doc").value; //id de la venta seleccionada

    if (lstRow.FAC_STATUS != "Cancelada") {

        if (strTipoventaDocumento == "FACTURA") {

            //refactura
            if (document.getElementById("CEV_RFAC1").checked) {

                document.getElementById("SioNO_inside").innerHTML = "¿Deseas refacturar?";
                $("#SioNO").dialog("open");

                document.getElementById("btnSI").onclick = function () {
                    $("#SioNO").dialog("close");

                    UpFactura(strID_TKT_FAC, strTipoventaDocumento);

                };

                document.getElementById("btnNO").onclick = function () {
                    $("#SioNO").dialog("close");
                };

            } else {

                //ACTUALIZA INFORMACIÓN DE LA VENTA
                UpPagoFac(strID_TKT_FAC, strNomComp);

            }
        } else { //es ticket o reservacion
            //
            //actuaiza tkt o reservacion
            console.log("entra a la venta general");
            UpDateTKT(strID_TKT_FAC);
        }
    } else {

        alert("Este documento no se puede modificar, ya ha sido cancelado!");
    }
}

function doFacturaHistoricoVtasCte() { //proviene del historial de ventas de cada cliente

    var strPost = "";
    var grid = jQuery("#H_VENTA_CTE");
    var id = grid.getGridParam("selrow");
    var lstRow = grid.getRowData(id);
    var strCT_ID = document.getElementById("CT_ID").value;
    var strNomComp = document.getElementById("CEV_NOM_FILE").value;

    var strID_TKT_FAC = document.getElementById("id_vta").value; //tipo de documento actual
    var strTipoventaDocumento = document.getElementById("tipo_doc").value; //id de la venta seleccionada

    if (lstRow.FAC_STATUS != "Cancelada") {

        //ES FACTURA   
        if (strTipoventaDocumento == "FACTURA") {

            //REFACTURA
            if (document.getElementById("CEV_RFAC1").checked) {

                document.getElementById("SioNO_inside").innerHTML = "¿Deseas refacturar?";
                $("#SioNO").dialog("open");

                document.getElementById("btnSI").onclick = function () {
                    $("#SioNO").dialog("close");

                    UpFactura(strID_TKT_FAC, strTipoventaDocumento);

                };

                document.getElementById("btnNO").onclick = function () {
                    $("#SioNO").dialog("close");
                };

                //ACTUALIZA PAGO// 
            } else {

                UpPagoFac(strID_TKT_FAC, strNomComp);

            }

        } else { //es ticket o reservacion
            //
            //actuaiza tkt o reservacion
            console.log("ACTUALIZA EL TICKET O LA RESERVACIÓN");
            UpDateTKT(strID_TKT_FAC);
        }

    } else {

        alert("Este documento no se puede modificar, ya ha sido cancelado!");
    }
}

function ValidaRFC() {
    var strRFC = document.getElementById("CEV_RFC").value;
    if (strRFC != "") {
        var bolExpReg = _EvalExpRegCOFIDE1(strRFC, "^[A-Z,Ñ,&,a-z,ñ]{3,4}[0-9]{2}[0-1][0-9][0-3][0-9][A-Z,Ñ,&,0-9,a-z]{3}");
        if (!bolExpReg) {
            alert("El formato del registro federal de contribuyentes es incorrecto");
        } else {
            document.getElementById("CEV_RFC").value = strRFC.toUpperCase();
        }
    }
}
function vtaPagarCurso() { //pagar curso al momento de facturarlo
    if (document.getElementById("CT_NO_CLIENTE").value != "0" && document.getElementById("CT_NO_CLIENTE").value != "") {
        var grid = jQuery("#GRDIHVENTA");
        if (grid.getGridParam("selrow") != null) {
            var lstRow = grid.getRowData(grid.getGridParam("selrow"));
            var strFAC_ID = 0;
            var strTKT_ID = 0;
            var DOC_TOTAL = 0;
            var TIPO_DOC = "";
            var strFechaDoc = "";
            var ID_tkt_fac = "";
            if (lstRow.FAC_STATUS == "Activa") {
                if (lstRow.FAC_PAGADO == "Pendiente") {
                    DOC_TOTAL = lstRow.PD_TOTAL;
                    TIPO_DOC = lstRow.DOC_TIPO;
                    strFAC_ID = lstRow.FAC_ID;
                    strTKT_ID = lstRow.PD_FOLIO;
                    strFechaDoc = lstRow.PD_FECHA;
                    $("#dialogPagos").dialog("open");
                    var idCliente = document.getElementById("CT_NO_CLIENTE").value;
                    var dblAnticipo = DOC_TOTAL;
                    var intMonedaBanco = 1; //document.getElementById("BCO_MONEDA").value;
                    var dblTasaCambio = 1; //document.getElementById("PARIDAD").value;
                    var intBancoID = 1; //document.getElementById("BC_ID").value;
                    if (TIPO_DOC == 1) { //0=ticket & 1= factura
                        ID_tkt_fac = strFAC_ID;
                    } else {
                        ID_tkt_fac = strTKT_ID;
                    }
//                var strPOST = "&idTrx=" + strFAC_ID;
                    var strPOST = "&idTrx=" + ID_tkt_fac;
                    strPOST += "&COUNT_PAGOS=" + 0;
                    strPOST += "&TipoDoc=" + TIPO_DOC;
                    strPOST += "&FECHA=" + strFechaDoc;
                    strPOST += "&NOTAS=" + "";
                    strPOST += "&MONEDA=" + intMonedaBanco;
                    strPOST += "&TASAPESO=" + dblTasaCambio;
                    strPOST += "&MONTOPAGO=" + dblAnticipo;
                    strPOST += "&BC_ID=" + intBancoID;
                    strPOST += "&IdCte=" + idCliente;
                    strPOST += "&intAnticipo=" + 1;
                    $.ajax({
                        type: "POST",
                        data: encodeURI(strPOST),
                        scriptCharset: "utf-8",
                        contentType: "application/x-www-form-urlencoded;charset=utf-8",
                        cache: false,
                        dataType: "html",
                        url: "ERP_Cobros.jsp?id=1",
                        success: function (dato) {
                            if (Left(dato, 3) == "OK.") {
                                //envio de mail
                                $.ajax({
                                    type: "POST",
                                    data: "fac_id=" + ID_tkt_fac + "&tipo_doc=" + TIPO_DOC,
                                    scriptCharset: "utf-8",
                                    contentType: "application/x-www-form-urlencoded;charset=utf-8",
                                    cache: false,
                                    dataType: "xml",
                                    url: "COFIDE_Telemarketing_vta.jsp?ID=9",
                                });
                                //envio de mail
                                consultaHistoricoVtas(); //recarga pantalla
                                $("#dialogPagos").dialog("close");
                            } else {
                                alert(dato);
                            }
                            $("#dialogWait").dialog("close");
                        },
                        error: function (objeto, quepaso, otroobj) {
                            alert(":pto9:" + objeto + " " + quepaso + " " + otroobj);
                            $("#dialogWait").dialog("close");
                        }
                    });
                } else {
                    alert("Este Documento ya ha sido pagado");
                }
            } else {
                alert("Este Documento No se puede pagar, porque a sido cancelado");
            }
        } else {
            alert("Debe seleccionar una partida en la tabla \"HISTORIAL DE VENTAS\"");
        }
    } else {
        alert("Debe seleccionar un Cliente");
    }
}

function reemplazaCompPago() {
    document.getElementById("SioNO_inside").innerHTML = "¿Desea Reemplazar el comprobante de pago?";
    $("#SioNO").dialog("open");
    document.getElementById("btnSI").onclick = function () {
        $("#SioNO").dialog("close");
        subirComprobantePago();
    };
    document.getElementById("btnNO").onclick = function () {
        $("#SioNO").dialog("close");
    };
}
/**
 * ACTUALIZA LA INFORMACIÓN DE LA RESERVACIÓN O TICKET
 * @param {type} intID_TKT ID DE LA VENTA SELECCIONADA
 * @param {type} intTMP SI VIENE CON PARAMETRO = 1, ES DUPLICADA
 * @returns {undefined}
 */
function UpDateTKT(intID_TKT, intTMP) {

    var strPOST = "id_tkt=" + intID_TKT;
    var bolPagoOk = document.getElementById("CEV_PAGO_OK").value;
    var strTipoDocumentoVta = document.getElementById("tipo_doc").value;
    // id de movimiento que contiene la información de los detalles del curso
    var strIdMov = document.getElementById("id_mov_m").value;

    console.log("datos maestro inicio EDICIÓN");
    var precioreal = document.getElementById("precio_base").value.replace(",", ""); //suma de precios unitarios de lso productos
    var descuento = document.getElementById("CEV_DESCUENTO").value.replace(",", ""); // suma de descuentos de los productos
    var importe = parseFloat(precioreal) - parseFloat(descuento); // importe de la venta
//    alert(importe + " : importejirijillo");
    var impuesto = document.getElementById("CEV_SUB2").value.replace(",", ""); // impuestos de la venta
    var total = document.getElementById("CEV_SUB3").value.replace(",", ""); // monto total
    var strMetodoPago = document.getElementById("CEV_METODO").value; // METODO DE PAGO
    var strFormaPago = document.getElementById("CEV_DESCRIPCION").value; // FORMA DE PAGO
    var strDescuento = document.getElementById("CEV_DESCUENTO").value.replace(",", ""); // CANTIDAD DE DESCUENTO    
    console.log("datos maestro fin EDICIÓN");

    var strTipoVta = "";
    if (document.getElementById("CEV_FAC0").checked) {
        strTipoVta = "1"; //reservacion
    }
    if (document.getElementById("CEV_FAC1").checked) {
        strTipoVta = "2"; //ticket
    }
    if (document.getElementById("CEV_FAC2").checked) {
        strTipoVta = "3"; //factura
    }
    var strComentarioAgente = document.getElementById("CEV_COMENT").value; //comentario ejecutivo
    var strComentario = document.getElementById("CEV_COMENTARIO").value; //comentario factura
    var strComprobantePago = document.getElementById("CEV_NOM_FILE").value;
    var intEsNuevo = document.getElementById("CT_CTE_NUEVO").value; //cliente nuevo? desde la pantalla de telemarketing
    var stridConsultavta = document.getElementById("CT_GRID").value; // edición de venta o nuevo, para no tomar en cuenta el nuevo valor de si es nuevo o no. 1 o 0
    var strFechaPago = document.getElementById("CEV_FECHAPAGO").value;
    var strReferencia = document.getElementById("CEV_DIGITO").value;
    var boledicionventa = "0";

    if (stridConsultavta != "3") { // es una edición de venta 1 / 0 
        // SI ES UNA EDICIÓN DE VENTA, PASA EL PARAMETRO PARA NO TOMAR EN CUENTA EL CAMPO DE @intEsNuevo  
        boledicionventa = "1";
    }
    strPOST += "&IDMOV=" + strIdMov; //ID DE MOVIMIENTO
    strPOST += "&precio_unit=" + precioreal;
    strPOST += "&importe=" + importe;
    strPOST += "&formapago=" + strFormaPago;
    strPOST += "&metodopago=" + strMetodoPago;
    strPOST += "&iva=" + impuesto;
    strPOST += "&descuento=" + strDescuento;
    strPOST += "&total=" + total;
    strPOST += "&tipo_vta=" + strTipoVta;
    strPOST += "&comentA=" + encodeURIComponent(strComentarioAgente);
    strPOST += "&coment=" + encodeURIComponent(strComentario);
    strPOST += "&comprobante=" + strComprobantePago;
    strPOST += "&pagoOk=" + bolPagoOk; // si es 1, ya se pago, no enviar nada / si es 0 Y se adjunta pago, envia accesos/material    
    strPOST += "&CEV_PAGO_OK=" + bolPagoOk; //ya se pago? 0 - aun no se paga / 1 - ya tiene un pago   
    strPOST += "&fechapago=" + strFechaPago; //ya se pago? 0 - aun no se paga / 1 - ya tiene un pago   
    strPOST += "&digito=" + strReferencia; //ya se pago? 0 - aun no se paga / 1 - ya tiene un pago   
    strPOST += "&venta_nueva=0";

    //ACTUALIZA TICKET O RESERVACIÓN
    if (strTipoVta != "3") {

        strPOST += "&doc=T";

        $.ajax({
            type: "POST",
            data: encodeURI(strPOST),
            scriptCharset: "utf-8",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "html",
            url: "COFIDE_Telemarketing_vta.jsp?ID=13",
            beforeSend: function () {
                $("#dialogWait").dialog("open");
            },
            success: function (datos) {
                datos = trim(datos);
                if (datos == "OK") {

                    alert("Venta Actualizada");
                    $("#dialog").dialog("close");
                    $("#dialogWait").dialog("close");

                    // CONSULTA DE VENTA GENERAL
                    if (stridConsultavta == "1") {

                        //RECARGA LA INFORMACION DE LA ULTIMA CONSULTA DE HISTORIAL GRLA
                        consultaHistoricoVtas();

                    } else {

                        //RECARGA LA INFORMACIÓN DEL CLIENTE ACTUAL
                        HistorialVtaCte(document.getElementById("CT_NO_CLIENTE").value);
                        LoadContacto(document.getElementById("CT_NO_CLIENTE").value); //recarga los contactos

                    }

                    cerrarVtaCofide();
                } else {

                    alert("Ocurrio un problema al actualizar la venta");
                    $("#dialog").dialog("close");
                    $("#dialogWait").dialog("close");

                }
            }, error: function (objeto, quepaso, otroobj) {
                alert(":pto13:" + objeto + " " + quepaso + " " + otroobj);
            }});

        // SE VA A FACTURAR Y MANDA NUEVOS DATOS
    } else {

        var strPost = "fac_id=" + intID_TKT;

        //DATOS DE FACTURACIÓN
        /**
         * SI ES METODO DE PAGO "PPD" SE PONE EN AUTO0MATICO, FORMA DE PAGO PRO DEFINIR
         * FACTURA AL MOMENTO SIN AUTORIZACIÓN
         */
        if (strMetodoPago == "PPD" && intTMP != "1") {

            setPPD();
            saveVtaCofide(intID_TKT);

        } else {

            //CAMBIA SOLAMENTE LA BANDERA A FACTURACIÓN *TKT_FACTURAR*         
            var grid = jQuery("#GRD_RAZONES");
            var lstRow = grid.getRowData(grid.getGridParam("selrow"));

            strPost += "&IDMOV=" + document.getElementById("id_mov_m").value;
            strPost += "&CEV_PAGO_OK=" + document.getElementById("CEV_PAGO_OK").value;

            strPost += "&DFA_ID=" + lstRow.CEV_ID_FAC;
            strPost += "&metodo=" + document.getElementById("CEV_METODO").value;
            strPost += "&forma=" + document.getElementById("CEV_DESCRIPCION").value;
            strPost += "&archivopago=" + document.getElementById("CEV_NOM_FILE").value;
            strPost += "&digref=" + document.getElementById("CEV_DIGITO").value;
            strPost += "&fechapago=" + document.getElementById("CEV_FECHAPAGO").value;
            strPost += "&venta_nueva=0";

            $.ajax({
                type: "POST",
                data: encodeURI(strPost),
                scriptCharset: "utf-8",
                contentType: "application/x-www-form-urlencoded;charset=utf-8",
                cache: false,
                dataType: "html",
                url: "COFIDE_Telemarketing_vta.jsp?ID=10",
                beforeSend: function () {

                    $("#dialogWait").dialog("open");
                },
                success: function (datos) {

                    datos = trim(datos);

                    if (datos == "OK") {

                        //DA LA INSTRUCCIÓN PARA QUE APAREZCA LA VENTA EN CONTABILIDAD PARA FACTURAR                                      
                        if (stridConsultavta == "1") { //actualiza el historial de ventas general, si la venta se manipulo desde ahi

                            consultaHistoricoVtas();

                        } else {

                            HistorialVtaCte(document.getElementById("CT_NO_CLIENTE").value); //se actualiza el historial, si fue desde ventas por cliente o vent anueva                       

                        }
                        cerrarVtaCofide();
                    } else {

                        alert("Ocurrio un problema al facturar la venta");

                    }
                    $("#dialogWait").dialog("close");

                }, error: function (objeto, quepaso, otroobj) {
                    alert(":pto10:" + objeto + " " + quepaso + " " + otroobj);
                }});
        }
    }
}

/**
 * VA A REFACTURAR, MANDA PETICIÓN PARA CANCELAR LA VENTA ACTUAL 
 * Y MUESTRA EN PANTALLA DE CONTABILIDAD PENDIENTE POR FATURAR
 * @param {type} intID_TKT
 * @param {type} strTipoDoc
 * @returns {undefined}
 */
function UpFactura(intID_TKT, strTipoDoc) {
    //ESTA SELECCIONADO LA REFACTURACIÓN
    console.log("MANDA CANCELAR LA FACTURA ACTUAL");
    //MANDA LA SOLICUTUD DE CANCELACIÓN DE LA VENTA
    peticionCancel(intID_TKT, strTipoDoc); //****** PENDIENTE
    console.log("GENERA NUEVA RESERVACIÓN CON LA BANDERA PARA FACTURAR");
    saveVtaCofide();
//    saveVtaCofide(intID_TKT);
}

// GUARDA EL COMPROBANTE DE PAGO DE LA FACTURA O ACTUALIZACIONES
function UpPagoFac(intID_TKT, strNomComprobante) {

    var strTipoCurso = "";
    var strPrecioTotal = document.getElementById("CEV_SUB3").value.replace(",", "");
    var bolPagoOk = document.getElementById("CEV_PAGO_OK").value;
    var intIdMov = document.getElementById("id_mov_m").value;
    var strOperacion = document.getElementById("CT_GRID").value;

    var strPost = "id_fac=" + intID_TKT;
    strPost += "&nom_comp=" + strNomComprobante;
    strPost += "&CEV_PAGO_OK=" + bolPagoOk;
    strPost += "&doc=F";
    strPost += "&FAC_TOTAL=" + strPrecioTotal;
    strPost += "&IDMOV=" + intIdMov;
    strPost += "&venta_nueva=0";

    $.ajax({
        type: "POST",
        data: encodeURI(strPost),
        scriptCharset: "UTF-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "html",
        url: "COFIDE_Telemarketing_vta.jsp?ID=16",
        beforeSend: function () {
            $("#dialogWait").dialog("open");
        },
        success: function (dato) {
            dato = trim(dato);
            if (Left(dato, 2) == "OK") {

                alert("Venta actualizada.");

                if (strOperacion == "1") {

                    consultaHistoricoVtas();

                } else {

                    HistorialVtaCte(document.getElementById("CT_NO_CLIENTE").value);
                    LoadContacto(document.getElementById("CT_NO_CLIENTE").value); //recarga los contactos

                }
                cerrarVtaCofide();
            } else {

                alert("ERROR: " + dato);

            }

            $("#dialogWait").dialog("close");
        },

        error: function (objeto, quepaso, otroobj) {

            alert(":pto16:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");

        }});

}

/***
 * 1 valida si el cliente es nuevo o no, si es nuevo lo agrega
 * 2 valida la duplicidad de la venta, si es factura tocar el registro a facturar, si no, no
 * @param {type} strFact tipo de venta 1 fact / 0 tkt o reservación
 * @param {type} strRFC aplica si el tipo de venta es factura
 * @param {type} strCorreo correo del cliente
 * @param {type} strTelefono telefono del cliente
 * @param {type} opc tipo de venta 1 venta general / 0 venta cliente
 * @param {type} update OK solo actualiza / en blanco, hace todo el proceso
 * @returns {undefined}
 * 
 * VALIDAR RFC, CORREO, TELEFON, QUE SEA PROSPECTO(UNICAMENTE ASÍ, SE INICIA LA VALIDACIÓN DE SI ES CLIENTE NUEVO)
 */
function validaDuplicidad(strFact, strRFC, strCorreo, strTelefono, opc, update) { //update = ok, solo actualiza la factura, si no, hace toda la validación
    console.log("*******************************");
    console.log("TIPO DE VENTA: " + strFact);
    console.log("RFC: " + strRFC);
    console.log("CORREO: " + strCorreo);
    console.log("TELEFONO: " + strTelefono);
    console.log("OPCIÓN: " + opc);
    console.log("SE ACTUALIZA?: " + update);
    console.log("*******************************");

    //unicamente aplica cuando se actualiza una venta
    if (update == "OK") {

        if (opc == "2") {

            //proviene del historial de c/cliente
            doFacturaHistoricoVtasCte();

        }

        if (opc == "3") {

            //proviene del historial de vtas general
            doFacturaHistoricoVtas();

        }

    } else {
        /**
         * HACE LA VALIDACIÓN DE DUPLICIDAD DEL CONTENIDO DE LA VENTA
         * @type String
         */
        // valida la información del cliente
        var strPost = "";
        strPost += "&telefono=" + strTelefono;
        strPost += "&correo=" + strCorreo;
        strPost += "&rfc=" + encodeURIComponent(strRFC);
        strPost += "&tipovta=" + strFact;//factura = 1 , tkt o reserva = 0
        strPost += "&vta_gral=" + document.getElementById("CT_GRID").value;//si es vta_gral = 1, busca la información del cliente para validar
        var strIdVenta = document.getElementById("CT_ID_FACTKT").value; //ID DE LA VENTA GENERAL
        strPost += "&id_vta=" + strIdVenta; //ID DE LA VENTA GENERAL

        $.ajax({
            type: "POST",
            data: encodeURI(strPost),
            scriptCharset: "utf-8",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "html",
            url: "COFIDE_Telemarketing_vta.jsp?ID=17",
            beforeSend: function () {

                $("#dialogWait").dialog("open");
            },
            success: function (dato) {
                dato = trim(dato);

                if (Right(dato, 1) == "1") {

                    document.getElementById("CT_CTE_NUEVO").value = "1"; //campo de venta nueva

                } else {

                    document.getElementById("CT_CTE_NUEVO").value = "0";
                }

                if (Left(dato, 2) == "OK") { //OK la venta no tiene conflicto

                    if (opc == "2") {

                        console.log("venta desde edicion del cliente");
                        doFacturaHistoricoVtasCte(); //proviene del historial de c/cliente
                    }
                    if (opc == "3") {

                        console.log("venta desde edición de ventas gral");
                        doFacturaHistoricoVtas(); //proviene del historial de vtas general
                    }

                } else {

                    // la venta se duplico
                    alert("Cliente duplicado, Espera validación de Logística.");
                    dato = dato.split(",", 1);
                    saveVtaTmp(dato, strTelefono, strCorreo, strRFC, strFact, strIdVenta);
                }

                $("#dialogPagos").dialog("close");
            },

            error: function (objeto, quepaso, otroobj) {
                alert(":pto17:" + objeto + " " + quepaso + " " + otroobj);
                $("#dialogWait").dialog("close");
            }
        });
    }
}

/**
 * GUARDA LA VENTA DUPLICADA
 * @param {type} strIdVtaTmp ID MAESTRO DE LA DUPLICIDAD
 * @param {type} strTelefonoD TELEFONO DEL CLIENTE QUE SE VALIDO
 * @param {type} strCorreoD CORREO DEL CLIENTE QUE SE VALIDO
 * @param {type} strRFCD RFC DEL CLIENTE QUE SE VALIDO
 * @param {type} strTipoD TIPO DE DOCUMENTO 1 FACTURA / 0 TICKET
 * @param {type} strIdVenta // ID DE LA VENTA QUE PROVIENE EL CONFLICTO
 * @returns {undefined}
 */
function saveVtaTmp(strIdVtaTmp, strTelefonoD, strCorreoD, strRFCD, strTipoD, strIdVenta) {

    var strPOST = "id_tkt=" + strIdVtaTmp; //id de la venta temporal
    strPOST += "&telefonod= " + strTelefonoD;
    strPOST += "&correod=" + strCorreoD;
    strPOST += "&rfcd=" + encodeURIComponent(strRFCD);
    strPOST += "&idventa=" + strIdVenta; //id de la venta tkt o fac

    //ACTUALIZA LA VENTA Y 
    //MARCA 1 EN COFIDE_DUPLICIDAD = TKT
    //MARCA 1 EN COFIDE_CUPLICIDAD && TKT_FACTURAR = FAC

    var intDuplicado = 1; //DUPLICADO
    var intFacturar = 0; // FACTURAR

    var strTipoVta = "";
    //RESERVACION
    if (document.getElementById("CEV_FAC0").checked) {
        strTipoVta = "1";
    }
    //TICKET
    if (document.getElementById("CEV_FAC1").checked) {
        strTipoVta = "2";
    }
    //FACTURA
    if (document.getElementById("CEV_FAC2").checked) {
        strTipoVta = "3";
        intFacturar = 1; // se va a facturar
    }

    strPOST += "&tipo_vta=" + strTipoVta;
    strPOST += "&facturar=" + intFacturar;
    strPOST += "&duplicado=" + intDuplicado;
    strPOST += "&documento=" + document.getElementById("tipo_doc").value;
    strPOST += "&forma=" + document.getElementById("CEV_DESCRIPCION").value;
    strPOST += "&metodo=" + document.getElementById("CEV_METODO").value;

    //RAZON SOCIAL A FACTURAR
    $.ajax({
        type: "POST",
        data: encodeURI(strPOST),
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "html",
        url: "COFIDE_Telemarketing_vta.jsp?ID=18",
        beforeSend: function () {
            $("#dialogWait").dialog("open");
        },
        success: function (dato) {
            dato = trim(dato);
            if (dato == "OK") {

                //ACTUALIZA LA VENTA
                UpDateTKT(strIdVenta, "1"); // 1 = TMP

            } else {

                alert("hubó un problema al guardar la venta.");

            }
            $("#dialogWait").dialog("close");
        },
        error: function (objeto, quepaso, otroobj) {
            alert(":pto18:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }
    });
}

//consulta información del cliente
function getInfCte(strIdCte) {
    var setInfoCte = [];
    var strTelefono = "";
    var strCorreo = "";
//    console.log("busca la información del cliente " + strIdCte);
    $.ajax({
        type: "POST",
        data: "ct_id=" + strIdCte,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "COFIDE_Telemarketing_vta.jsp?ID=15",
        success: function (dato) {
            var lstXml = dato.getElementsByTagName("vta")[0];
            var lstCte = lstXml.getElementsByTagName("datos");
            for (var i = 0; i < lstCte.length; i++) {
                var objcte = lstCte[i];
                strTelefono = objcte.getAttribute("telefono");
                strCorreo = objcte.getAttribute("email");
                setInfoCte[0] = strTelefono;
                setInfoCte[1] = strCorreo;
                aux = setInfoCte; //para tomar las variables desde fuera
            }
        },
        error: function (objeto, quepaso, otroobj) {
            alert(":pto15:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }
    });
}

function LoadContactoPromo(intIdContacto) {
    jQuery("#GRIDPARTICIPA").clearGridData();
    var strPost = "CONTACTO_ID=" + intIdContacto;
    $.ajax({
        type: "POST",
        data: strPost,
        scriptCharset: "UTF-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "COFIDE_Telemarketing.jsp?ID=43",
        success: function (datos) {
            var lstXml = datos.getElementsByTagName("vta")[0];
            var lstCte = lstXml.getElementsByTagName("datos");
            for (var i = 0; i < lstCte.length; i++) {
                var objcte = lstCte[i];
                var datarow = {
                    CCO_NOMBRE: objcte.getAttribute("CCO_NOMBRE"),
                    CCO_APPATERNO: objcte.getAttribute("CCO_APPATERNO"),
                    CCO_APMATERNO: objcte.getAttribute("CCO_APMATERNO"),
                    CCO_TITULO: objcte.getAttribute("CCO_TITULO"),
                    CCO_NOSOCIO: objcte.getAttribute("CCO_NOSOCIO"),
                    CCO_ASOCIACION: objcte.getAttribute("CCO_ASOCIACION"),
                    CCO_CORREO: objcte.getAttribute("CCO_CORREO"),
                    CONTACTO_ID: objcte.getAttribute("CONTACTO_ID")
                };
                itemIdCob++;
                document.getElementById("CEV_PARTICIPANTE").value = itemIdCob;
                jQuery("#GRIDPARTICIPA").addRowData(itemIdCob, datarow, "last");
            }
        },
        error: function (objeto, quepaso, otroobj) {
            alert(":pto=43: " + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }});
}

/**
 * valida el numero de participantes
 * @returns {.grid@call;getDataIDs.length}
 */
function intParticipante() {
    var intRow;
    var grid = jQuery("#GRIDPARTICIPA");
    var idArr = grid.getDataIDs();
    intRow = idArr.length;
    return intRow;
}

/**
 * valida si existe una razón social, si se va a factiurar
 * @returns {Boolean}
 */
function bolRazonSelect() {
    if (document.getElementById("CEV_FAC2").checked) { // se va a facturar
        var grid = jQuery("#CEV_GRID");
        if (grid.getGridParam("selrow") != null) { //ya se eligio una razón social
            return true;
        } else {
            return false;
        }
    } else { // no se va a facturar
        return true;
    }
}

/**
 * cambio de venta, cancela la anterior tkt o reservacion por una nueva(factura)
 */
function ChnageVta(strIdVta) {
    $.ajax({
        type: "POST",
        data: "id_vta=" + strIdVta,
        scriptCharset: "UTF-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "html",
        url: "COFIDE_Telemarketing_vta.jsp?ID=19",
        success: function (datos) {
            datos = trim(datos);
            if (datos != "OK") {
                alert("Hubó un problema al actualizar la venta.");
            }
        },
        error: function (objeto, quepaso, otroobj) {
            alert(":pto=19: " + objeto + " " + quepaso + " " + otroobj);
        }});
}

function UpDateContactoParticipante(strNombre, strApPat, strApMat, strTitulo, intDato, id_Contacto, strAsoc, strCorreo, bolMaterial) {
    if (id_Contacto != "0") {
        //proceso de guardado        
        var strPost = "";
        strPost = "idcontacto=" + id_Contacto;
        strPost += "&nombre=" + strNombre;
        strPost += "&appat=" + strApPat;
        strPost += "&apmat=" + strApMat;
        strPost += "&titulo=" + strTitulo;
        strPost += "&numero=" + intDato;
        strPost += "&asoc=" + strAsoc;
        strPost += "&correo=" + strCorreo;
        strPost += "&material=" + bolMaterial;

        $.ajax({
            type: "POST",
            data: strPost,
            scriptCharset: "UTF-8",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "html",
            url: "COFIDE_Telemarketing.jsp?ID=44",
            beforeSend: function () {
                $("#dialogWait").dialog("open");
            },
            success: function (datos) {
                datos = trim(datos);

                if (datos != "OK") {

                    alert("Hubó un problema al actualizar la infOrmación del participante");

                } else {

                    if (document.getElementById("CT_GRID").value != "0") { // <> vta general

                        LoadContacto_(document.getElementById("CT_NO_CLIENTE").value);

                    }

                }

                $("#dialogWait").dialog("close");
            },
            error: function (objeto, quepaso, otroobj) {
                alert(":pto=44: " + objeto + " " + quepaso + " " + otroobj);
                $("#dialogWait").dialog("close");
            }});
    }
}
/**
 * valida lugares para realizar la venta
 * @returns {Boolean}
 */
function getDisponibilidad() {
    consultaCurso();
    var intLimite = document.getElementById("CEV_LIMITE").value;
    var intOcupado = document.getElementById("CEV_OCUPADO").value;
    var bolPresencial = document.getElementById("CEV_TIPO_CURSO0").checked;
    var intParticipantes = intRowD();
    var intDisponibles = intLimite - intOcupado;
    var intExcedente = intParticipantes - intDisponibles;
    if (bolPresencial) { // valida lugares rpesencial
        if (intDisponibles >= intParticipantes) {
            // permite seguir la venta        
            return true;
        } else {
            // se acabarono los lugares            
            alert("Ya no hay mas lugares disponibles\nHay un excedente de: " + intExcedente + " participante(s)\nen la venta");
            return false;
        }
    } else {
        return true;
    }
}
/**
 * valida lugares de la venta, que se van a editar
 * @returns {undefined}
 */
function getDisponibilidad_edit() {
    consultaCurso();
    var intLimite = document.getElementById("CEV_LIMITE").value; // limite del curso
    var intOcupado = document.getElementById("CEV_OCUPADO").value; //lugares ocupados del curso
    var intOcupado_vta = document.getElementById("num_participante").value; //lugares ocupados de la venta
    var bolPresencial = document.getElementById("CEV_TIPO_CURSO0").checked;
    var intParticipantes = intRowD(); // numero actual de participantes
    var intLugares = intOcupado - intOcupado_vta; // edición de lugares, se le resta los actuales a los ocupados para hacer la operación de los nuevos
    var intParticipantes_ = 0;
    var intDisponibles = intLimite - intLugares; // limite del curso menos los lugares ocupados(descuenta los inscritos para recalcularlos)
    if (bolPresencial) {
        var intExcedente = intParticipantes - intDisponibles;
        if (intDisponibles >= intParticipantes) {
            return true;
        } else {
            // se acabarono los lugares            
            alert("Ya no hay mas lugares disponibles\nHay un excedente de: " + intExcedente + " participante(s)\nen la venta");
            return false;
        }
    } else {
        return true;
    }
}

function LoadContacto_(intIdCte) {
    jQuery("#GRIDCONTACTOS").clearGridData();
    var intPuntos = 0;
    var strPuntosSRC = "";
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
                    if (intPuntos >= 1000) {
                        strPuntosSRC = "<div style=\"text-align:center;\"><a href=\"javascript:opnPromosion(" + objcte.getAttribute("CONTACTO_ID") + ")\"<i title='Promoción' class=\"fa fa-bullseye\" >\t" + intPuntos + "</i></a></div>";
                    } else {
                        strPuntosSRC = "<div style=\"text-align:center\"><i title='No aplica Promoción'>\t" + intPuntos + "</i></div>";
                    }
                    var strTel = "";
                    var strTelAlterno = "";
//                    console.log(objcte.getAttribute("CCO_TELEFONO"));
//                    console.log(objcte.getAttribute("CCO_ALTERNO"));
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
            }, error: function (objeto, quepaso, otroobj) {
                alert(":pto=18: " + objeto + " " + quepaso + " " + otroobj);
                $("#dialogWait").dialog("close");
            }
        });
    }
}


function addParticipanteExistente() {
    var objSecModiVta = objMap.getScreen("ADD_PART");
    if (objSecModiVta != null) {
        objSecModiVta.bolActivo = false;
        objSecModiVta.bolMain = false;
        objSecModiVta.bolInit = false;
        objSecModiVta.idOperAct = 0;
    }
    OpnOpt("ADD_PART", "_ed", "dialogParticipante", false, false, true);
}//Fin addParticipanteExistente

//Busca participantes del cliente
function getParticipantesCT() {
    var item = 1;
    var strPost = "";
    var strContactoIds = "";
    var intIdCte = "";

    var grid = jQuery("#GRIDPARTICIPA");
    var arr = grid.getDataIDs();
    for (var i = 0; i < arr.length; i++) {
        var id = arr[i];
        var lstRow = grid.getRowData(id);
        strContactoIds += lstRow.CONTACTO_ID + ",";
    }
    intIdCte = lstRow.CT_ID; //ID DEL CLIENTE DE LA VENTA
    strContactoIds = strContactoIds.substring(0, strContactoIds.length - 1);
    strPost = "CONTACTOS=" + strContactoIds;
    strPost += "&CT_ID=" + intIdCte;
    $("#dialogWait").dialog("open");
    $.ajax({
        type: "POST",
        data: strPost,
        scriptCharset: "UTF-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "COFIDE_Telemarketing_vta.jsp?ID=20",
        success: function (datos) {
            jQuery("#GRIDPART").clearGridData();
            var lstXml = datos.getElementsByTagName("vta")[0];
            var lstCte = lstXml.getElementsByTagName("datos");
            for (var i = 0; i < lstCte.length; i++) {
                var objcte = lstCte[i];
                var datarow = {
                    CCO_ID: item,
                    CCO_PARTICIPANTE: objcte.getAttribute("PARTICIPANTE"),
                    CCO_NOMBRE: objcte.getAttribute("CCO_NOMBRE"),
                    CCO_APPATERNO: objcte.getAttribute("CCO_APPATERNO"),
                    CCO_APMATERNO: objcte.getAttribute("CCO_APMATERNO"),
                    CCO_TITULO: objcte.getAttribute("CCO_TITULO"),
                    CCO_NOSOCIO: objcte.getAttribute("CCO_NOSOCIO"),
                    CCO_ASOCIACION: objcte.getAttribute("CCO_ASOCIACION"),
                    CCO_CORREO: objcte.getAttribute("CCO_CORREO"),
                    CT_ID: objcte.getAttribute("CT_ID"),
                    CONTACTO_ID: objcte.getAttribute("CONTACTO_ID")
                };
                item++;
                jQuery("#GRIDPART").addRowData(item, datarow, "last");
            }
            $("#dialogWait").dialog("close");
        }});
}

//Agrega un participante registrado del cliente
function AddParticipanteExistente() {
    $("#dialogWait").dialog("open");
    var grid = jQuery("#GRIDPART");
    if (grid.getGridParam("selrow") != null) {
        var lstRow = grid.getRowData(grid.getGridParam("selrow"));

        var strTitulo = lstRow.CCO_TITULO;
        var strNombre = lstRow.CCO_NOMBRE;
        var strApPat = lstRow.CCO_APPATERNO;
        var strApMat = lstRow.CCO_APMATERNO;
        var strAsoc = lstRow.CCO_ASOCIACION;
        var strNum = lstRow.CCO_NOSOCIO;
        var strCorreo = lstRow.CCO_CORREO;
        var strContactoID = lstRow.CONTACTO_ID;
        var CT_ID = lstRow.CT_ID;

        var datarow = {
            CCO_NOMBRE: strNombre,
            CCO_APPATERNO: strApPat,
            CCO_APMATERNO: strApMat,
            CCO_TITULO: strTitulo,
            CCO_NOSOCIO: intDato(strNum),
            CCO_CORREO: strCorreo,
            CONTACTO_ID: strContactoID,
            MATERIAL: "NO",
            CT_ID: CT_ID,
            CCO_ASOCIACION: strAsoc
        };

        jQuery("#GRIDPARTICIPA").addRowData(intCola, datarow, "last");
        intCola++;
        console.log(intCola + " consecutivo de participante");

        $("#dialogWait").dialog("close");
        $("#dialogParticipante").dialog("close");
    }
    setOperacionPartida();
}//Fin AddParticipanteExistente

/**
 * genera la venta general
 * @returns {undefined}}
 */
function DoVtagral(strIdVta) {
    console.log("####################### venta general ######################");
    var strPOST = "";
    strPOST = "idvta=" + strIdVta;
    var strCurso = "";
    var strComent = document.getElementById("CEV_COMENT").value;
    if (saveVtaCofideValida()) { //valida que la información este completa
        var strPrefijoMaster = "TKT";
        var strPrefijoDeta = "TKTD";
        var strKey = "TKT_ID";
        var strNomFormat = "TICKET";
        var intEsReservacion = 0;
        strCurso = document.getElementById("CEV_NOMCURSO_TMP").value;
        if (document.getElementById("CEV_FAC2").checked) { //FACTURA
            strPrefijoMaster = "FAC";
            strPrefijoDeta = "FACD";
            strKey = "FAC_ID";
            strNomFormat = "FACTURA";
        }
        if (document.getElementById("CEV_FAC0").checked) { //RESERVACIÓN
            intEsReservacion = 1;
        }
        strPOST += "&SC_ID=" + intSucDefa;
//strPOST += "&CT_ID=" + d.getElementById("CT_NO_CLIENTE").value;                                                         //del cliente
        strPOST += "&CEV_MPUBLICIDAD=" + d.getElementById("CEV_MPUBLICIDAD").value;
        strPOST += "&VE_ID=0";
        strPOST += "&PD_ID=0";
        strPOST += "&" + strPrefijoMaster + "_ESSERV=1";
        strPOST += "&" + strPrefijoMaster + "_MONEDA=1";
        strPOST += "&" + strPrefijoMaster + "_FOLIO=";
        strPOST += "&" + strPrefijoMaster + "_NOTAS=" + encodeURIComponent(document.getElementById("CEV_COMENTARIO").value);
        strPOST += "&" + strPrefijoMaster + "_IMPUESTO1=" + document.getElementById("CEV_SUB2").value.replace(",", "");
        strPOST += "&" + strPrefijoMaster + "_IMPUESTO2=" + 0;
        strPOST += "&" + strPrefijoMaster + "_IMPUESTO3=" + 0;
        strPOST += "&" + strPrefijoMaster + "_IMPORTE=" + document.getElementById("CEV_SUB1").value.replace(",", "");
        strPOST += "&" + strPrefijoMaster + "_RETISR=0";
        strPOST += "&" + strPrefijoMaster + "_RETIVA=0";
        strPOST += "&" + strPrefijoMaster + "_NETO=0";
        strPOST += "&" + strPrefijoMaster + "_NOTASPIE=" + strComent; //comentarios del ejecutivo
        strPOST += "&" + strPrefijoMaster + "_REFERENCIA=";
        strPOST += "&" + strPrefijoMaster + "_CONDPAGO=";
        strPOST += "&" + strPrefijoMaster + "_METODOPAGO=" + d.getElementById("CEV_DESCRIPCION").value;
        strPOST += "&" + strPrefijoMaster + "_NUMCUENTA=";
        strPOST += "&" + strPrefijoMaster + "_FORMADEPAGO=EN UNA SOLA EXHIBICION";
        strPOST += "&" + strPrefijoMaster + "_NUMPEDI=";
        strPOST += "&" + strPrefijoMaster + "_FECHAPEDI=";
        strPOST += "&" + strPrefijoMaster + "_ADUANA=";
        strPOST += "&" + strPrefijoMaster + "_TIPOCOMP=";
        if (document.getElementById("CEV_FAC0").checked) {
            strPOST += "&TIPOVENTA=3"; //reservacion
        }
        if (document.getElementById("CEV_FAC1").checked) {
            strPOST += "&TIPOVENTA=2"; //ticket            
        }
        if (document.getElementById("CEV_FAC2").checked) {
            strPOST += "&TIPOVENTA=1"; //factura            
        }
        strPOST += "&" + strPrefijoMaster + "_TASA1=" + 16;
        strPOST += "&" + strPrefijoMaster + "_TASA2=" + 0;
        strPOST += "&" + strPrefijoMaster + "_TASA3=" + 0;
        strPOST += "&" + "TI_ID=" + 1;
        strPOST += "&" + "TI_ID2=" + 0;
        strPOST += "&" + "TI_ID3=" + 0;
        strPOST += "&" + strPrefijoMaster + "_TASAPESO=1";
        strPOST += "&" + strPrefijoMaster + "_DIASCREDITO=1";
        strPOST += "&" + strPrefijoMaster + "_USO_IEPS=0";
        strPOST += "&" + strPrefijoMaster + "_TASA_IEPS=0";
        strPOST += "&" + strPrefijoMaster + "_IMPORTE_IEPS=0";
        strPOST += "&" + strPrefijoMaster + "_ESRECU=0";
        strPOST += "&" + strPrefijoMaster + "_PERIODICIDAD=0";
        strPOST += "&" + strPrefijoMaster + "_DIAPER=0";
        //DATOS FISCALES
        var grid = jQuery("#CEV_GRID");
        if (grid.getGridParam("selrow") != null) {
            var lstRow = grid.getRowData(grid.getGridParam("selrow"));
            if (document.getElementById("CEV_FAC2").checked) {
                strPOST += "&DFA_ID=0";
                strPOST += "&CEV_RAZONSOCIAL=" + encodeURIComponent(lstRow.CEV_RAZONSOCIAL);
                strPOST += "&CEV_RAZONSOCIAL=" + encodeURIComponent(lstRow.CEV_RAZONSOCIAL);
                strPOST += "&CT_RAZONSOCIAL=" + encodeURIComponent(lstRow.CEV_RAZONSOCIAL);
                strPOST += "&CEV_RFC=" + encodeURIComponent(lstRow.CEV_RFC);
                strPOST += "&CEV_NUMERO_FACT=" + lstRow.CEV_NUMERO;
                strPOST += "&CEV_NUMERO_INT=" + lstRow.CEV_NUMINT;
                strPOST += "&CEV_CALLE=" + lstRow.CEV_CALLE;
                strPOST += "&CEV_COLONIA=" + lstRow.CEV_COLONIA;
                strPOST += "&CEV_MUNICIPIO=" + lstRow.CEV_MUNICIPIO;
                strPOST += "&CEV_RAZONSOCIAL=" + encodeURIComponent(lstRow.CEV_RAZONSOCIAL);
                strPOST += "&CEV_ESTADO=" + lstRow.CEV_ESTADO;
                strPOST += "&CEV_CP=" + lstRow.CEV_CP;
                strPOST += "&CEV_TELEFONO=" + lstRow.CEV_TELEFONO;
                strPOST += "&CEV_EMAIL1=" + lstRow.CEV_EMAIL1;
                strPOST += "&CEV_EMAIL2=" + lstRow.CEV_EMAIL2;
                strPOST += "&CEV_CORREO=" + lstRow.CEV_EMAIL2;
                strPOST += "&CEV_CORREO2=" + lstRow.CEV_EMAIL2;
            } //fin si es if fac
        }
        //DATOS FISCALES
        strPOST += "&ADD_FEMSA=0";
        var txtNomCurso = strCurso;
        var intC = 1;
        strPOST += "&PR_ID" + intC + "=" + document.getElementById("CEV_IDCURSO").value;
        strPOST += "&" + strPrefijoDeta + "_EXENTO1" + intC + "=" + 0;
        strPOST += "&" + strPrefijoDeta + "_EXENTO2" + intC + "=" + 0;
        strPOST += "&" + strPrefijoDeta + "_EXENTO3" + intC + "=" + 0;
        strPOST += "&" + strPrefijoDeta + "_CVE" + intC + "=" + "...";
        strPOST += "&" + strPrefijoDeta + "_DESCRIPCION" + intC + "=" + encodeURIComponent(strCurso);
        strPOST += "&" + strPrefijoDeta + "_CANTIDAD" + intC + "=" + document.getElementById("CEV_PARTICIPANTE").value;
        strPOST += "&" + strPrefijoDeta + "_RET_ISR" + intC + "=" + 0;
        strPOST += "&" + strPrefijoDeta + "_RET_IVA" + intC + "=" + 0;
        strPOST += "&" + strPrefijoDeta + "_RET_FLETE" + intC + "=" + 0;
        strPOST += "&" + strPrefijoDeta + "_CF_ID" + intC + "=" + 0;
        var cuantos = document.getElementById("CEV_PARTICIPANTE").value; //cuantos lugares
        var dblPrecioConImp = 0; //precio unitario
        var dblPrecioRealConImp = 0; //precio unitario
        dblPrecioConImp = parseFloat(document.getElementById("CEV_PRECIO_UNIT").value.replace(",", ""));
        dblPrecioRealConImp = parseFloat(document.getElementById("CEV_PRECIO_UNIT").value.replace(",", ""));
        strPOST += "&" + strPrefijoDeta + "_PRECIO" + intC + "=" + dblPrecioConImp;
        strPOST += "&" + strPrefijoDeta + "_PRECREAL" + intC + "=" + dblPrecioRealConImp;
        var dblImporte = parseFloat(document.getElementById("CEV_SUB1").value.replace(",", "")); //precio unitario * cantidad de personas
        var dblImpuestoIVA = parseFloat(document.getElementById("CEV_SUB2").value.replace(",", "")); //cantidad de iva
        var dblPorcDescuento = document.getElementById("CEV_DESC").value;
        //descuento cantidad
        var dblDescuento = parseFloat(dblPorcDescuento) / 100; //descuento para hacer la operacion % / 100
        var dblDescuentoCantidad = parseFloat(dblImporte) * dblDescuento; //cantidad de descuento
        var dblTotal = parseFloat(document.getElementById("CEV_SUB3").value.replace(",", ""));
        strPOST += "&" + strPrefijoMaster + "_TOTAL=" + dblTotal;
        strPOST += "&" + strPrefijoDeta + "_IMPORTE" + intC + "=" + dblImporte;
        strPOST += "&" + strPrefijoDeta + "_TASAIVA1" + intC + "=" + 16;
        strPOST += "&" + strPrefijoDeta + "_TASAIVA2" + intC + "=0" + 0;
        strPOST += "&" + strPrefijoDeta + "_TASAIVA3" + intC + "=" + 0;
        strPOST += "&" + strPrefijoDeta + "_IMPUESTO1" + intC + "=" + parseFloat(dblImpuestoIVA);
        strPOST += "&" + strPrefijoDeta + "_IMPUESTO2" + intC + "=" + 0;
        strPOST += "&" + strPrefijoDeta + "_IMPUESTO3" + intC + "=" + 0;
        strPOST += "&" + strPrefijoDeta + "_ESREGALO" + intC + "=" + 0;
        strPOST += "&" + strPrefijoDeta + "_NOSERIE" + intC + "=";
        strPOST += "&" + strPrefijoDeta + "_IMPORTEREAL" + intC + "=" + dblImporte;
        strPOST += "&" + strPrefijoDeta + "_DESCUENTO" + intC + "=" + dblDescuentoCantidad;
        strPOST += "&" + strPrefijoDeta + "_PORDESC" + intC + "=" + dblPorcDescuento;
        strPOST += "&" + strPrefijoDeta + "_ESDEVO" + intC + "=" + 0;
        strPOST += "&" + strPrefijoDeta + "_PRECFIJO" + intC + "=" + 0;
        strPOST += "&" + strPrefijoDeta + "_ESREGALO" + intC + "=" + 0;
        strPOST += "&" + strPrefijoDeta + "_NOTAS" + intC + "=" + encodeURIComponent(document.getElementById("CEV_COMENTARIO").value);
        strPOST += "&" + strPrefijoDeta + "_UNIDAD_MEDIDA" + intC + "=" + "CURSO";
        strPOST += "&COUNT_ITEM=" + intC;
        strPOST += "&COUNT_PAGOS=0";
        strPOST += "&MCD_MONEDA1=1";
        strPOST += "&MCD_FOLIO1=";
        strPOST += "&MCD_FORMAPAGO1=EFECTIVO";
        strPOST += "&MCD_NOCHEQUE1=";
        strPOST += "&MCD_BANCO1=";
        strPOST += "&MCD_NOTARJETA1=";
        strPOST += "&MCD_TIPOTARJETA1=";
        strPOST += "&MCD_IMPORTE1=0.0";
        strPOST += "&MCD_TASAPESO1=1";
        strPOST += "&MCD_CAMBIO1=0.0";
        if (document.getElementById("CEV_MIMP1").checked) {
            strPOST += "&CEV_MIMP=1";
        } else {
            strPOST += "&CEV_MIMP=0";
        }
        if (document.getElementById("CEV_FAC2").checked) { //es factura
            strPOST += "&CEV_FAC=1";
        } else {
            strPOST += "&CEV_FAC=0";
        }
        //MODALIDAD DEL CURSO
        if (document.getElementById("CEV_TIPO_CURSO0").checked) {
            strPOST += "&CEV_TIPO_CURSO=1";
        }
        if (document.getElementById("CEV_TIPO_CURSO1").checked) {
            strPOST += "&CEV_TIPO_CURSO=2";
        }
        if (document.getElementById("CEV_TIPO_CURSO2").checked) {
            strPOST += "&CEV_TIPO_CURSO=3";
        }
        if (document.getElementById("CEV_TIPO_CURSO3").checked) {
            strPOST += "&CEV_TIPO_CURSO=4";
        }
        //MODALIDAD DEL CURSO
        strPOST += "&CEV_IDCURSO=" + document.getElementById("CEV_IDCURSO").value;
        strPOST += "&CEV_NOM_FILE=" + document.getElementById("CEV_NOM_FILE").value;
        var strNomFilePago = document.getElementById("CEV_NOM_FILE").value; //nombre del archivo de pago
        strPOST += "&CEV_FECHAPAGO=" + document.getElementById("CEV_FECHAPAGO").value;
        strPOST += "&CEV_FECINICIO=" + document.getElementById("CEV_FECINICIO").value;
        strPOST += "&CEV_DIGITO=" + document.getElementById("CEV_DIGITO").value;
//        strPOST += "&CT_NO_CLIENTE=" + document.getElementById("CT_ID").value;                                      //del cliente OBTENERLO DE UNA CONSULTA, SI ES TKT O RESERVACIÓN
//        strPOST += "&CT_RAZONSOCIAL=" + encodeURIComponent(document.getElementById("CT_RAZONSOCIAL").value); //razon social del CLIENTE principal
        strPOST += "&CEV_COMENTARIO=" + encodeURIComponent(strCurso); //comentario
        strPOST += "&FAC_SERIE=C"; //serie de los ticket y factura
        //PARTICIPANTES
        var strNumSoc = 0;
        var grid = jQuery("#GRIDPARTICIPA");
        var arr = grid.getDataIDs();
        for (var i = 0; i < arr.length; i++) {
            var id = arr[i];
            var lstRow = grid.getRowData(id);
            if (lstRow.CCO_NOSOCIO == "") {
                strNumSoc = 0;
            } else {
                strNumSoc = lstRow.CCO_NOSOCIO;
            }
            strPOST += "&CCO_TITULO" + i + "=" + lstRow.CCO_TITULO;
            strPOST += "&CCO_NOMBRE" + i + "=" + lstRow.CCO_NOMBRE;
            strPOST += "&CCO_APPATERNO" + i + "=" + lstRow.CCO_APPATERNO;
            strPOST += "&CCO_APMATERNO" + i + "=" + lstRow.CCO_APMATERNO;
            strPOST += "&CCO_NOSOCIO" + i + "=" + strNumSoc;
            strPOST += "&CCO_ASOCIACION" + i + "=" + lstRow.CCO_ASOCIACION;
            strPOST += "&CCO_CORREO" + i + "=" + lstRow.CCO_CORREO;
            strPOST += "&CONTACTO_ID" + i + "=" + lstRow.CONTACTO_ID;
            strPOST += "&material" + i + "=" + lstRow.MATERIAL;
        }
        //PARTICIPANTES
        strPOST += "&length_participa=" + arr.length;
        strPOST += "&vta_nvo=" + document.getElementById("CT_CTE_NUEVO").value; //1 es nuevo cliente en la venta
        strPOST += "&pagoOk=" + document.getElementById("CEV_PAGO_OK").value; // si es 1, ya se pago, no enviar nada / si es 0 Y se adjunta pago, envia accesos/material-promosión de presencial +200pts
        strPOST += "&promo=0";
        $("#dialogWait").dialog("open");
        $.ajax({
            type: "POST",
            data: encodeURI(strPOST),
            scriptCharset: "UTF-8",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "html",
            url: "COFIDE_Telemarketing_vta.jsp?ID=21",
            success: function (datos) {
                datos = trim(datos);
                var strIDFAC_TKT = ""; //id de la factura o el ticket
                var strExt = ""; //tipo de documento

                if (Left(datos, 3) == "OK.") {

                    if (strNomFormat == "FACTURA") {

                        var strHtml = CreaHidden(strKey, datos.replace("OK.", ""));
                        openWhereverFormat("ERP_SendInvoice?id=" + datos.replace("OK.", ""), strNomFormat, "PDF", strHtml);
                        strIDFAC_TKT = datos.replace("OK.", ""); //esto es el ID fac
                        strExt = "FAC";

                    } else {

                        if (intEsReservacion != 1) { //si es reservacion no imprimira na de na
                            var strHtml2 = CreaHidden(strKey, datos.replace("OK.", ""));
                            openFormat(strNomFormat, "PDF", strHtml2);
                        }

                        strIDFAC_TKT = datos.replace("OK.", ""); //esto es el ID tkt
                        strExt = "TKT";

                    }

                    cerrarVtaCofide();
                    $("#dialogWait").dialog("close");

                    consultaHistoricoVtas(); //historial de ventas general     

                } else {
                    alert("ERROR: " + datos);
                    $("#dialogWait").dialog("close");
                }
                $("#dialogWait").dialog("close");
            },
            error: function (objeto, quepaso, otroobj) {
                alert(":pto:" + objeto + " " + quepaso + " " + otroobj);
                $("#dialogWait").dialog("close");
            }
        });
    }
}



function resetValorFac() {
    //EDITAR DATOS
    if (document.getElementById("CEV_RFAC1").checked) {
        strDescTMP = document.getElementById("CEV_DESC").value;
        strFormaPagoTMP = document.getElementById("CEV_DESCRIPCION").value;
        strComentTMP = document.getElementById("CEV_COMENT").value;
        document.getElementById("CEV_DESC").disabled = false;
        document.getElementById("CEV_DESCRIPCION").disabled = false;
//        document.getElementById("CEV_COMENT").disabled = false;
        document.getElementById("CEV_BTNADD").disabled = false;
        document.getElementById("CEV_BTNEDIT").disabled = false;
    } else {
        //RESTAURAR DATOS, NO SE VA A EDITAR
        document.getElementById("CEV_DESC").value = strDescTMP;
        document.getElementById("CEV_DESC").disabled = true;
        document.getElementById("CEV_DESCRIPCION").value = strFormaPagoTMP;
        document.getElementById("CEV_DESCRIPCION").disabled = true;
        document.getElementById("CEV_COMENT").value = strComentTMP;
//        document.getElementById("CEV_COMENT").disabled = true;
        document.getElementById("CEV_BTNADD").disabled = true;
        document.getElementById("CEV_BTNEDIT").disabled = true;
    }
}

//Llena la informacion de COFIDE.net
function getCofideNet() {
    var intIdMembresia = document.getElementById("CEV_MEMBRESIA").value;
    if (intIdMembresia != 0) {
        var strPost = "";
        strPost += "MEMBRESIA_ID=" + intIdMembresia; //se obtienen los datos de la membresia
        $.ajax({
            type: "POST",
            data: strPost,
            scriptCharset: "UTF-8",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "xml",
            url: "COFIDE_Telemarketing_vta.jsp?ID=22",
            success: function (datos) {
                var lstXml = datos.getElementsByTagName("Membresia")[0];
                var lstCte = lstXml.getElementsByTagName("datos");
                for (var i = 0; i < lstCte.length; i++) {

                    var objcte = lstCte[i];
                    var dblCosto = parseFloat(objcte.getAttribute("CM_COSTO"));
                    var strDescripcion = objcte.getAttribute("CM_DESCRIPCION");
                    document.getElementById("CEV_IDCURSO").value = intIdMembresia;
                    document.getElementById("CEV_NOMCURSO").value = strDescripcion;
                    document.getElementById("CEV_NOMCURSO_TMP").value = strDescripcion;
                    document.getElementById("CEV_PRECIO_UNITARIO").value = dblCosto;
                }
            }, error: function (objeto, quepaso, otroobj) {
                alert(":pto=TMK_VTA=22: " + objeto + " " + quepaso + " " + otroobj);
            }});
    } else {
        alert("Seleccione una Membresia.");
        document.getElementById("CEV_MEMBRESIA").focus();
    }
}
/**
 * valida si el parcicipante ya existe en ese curso mas de una vez o es nuevo
 * @returns {undefined}
 */
function CheckParticipantExist(strIdCurso) {
    if (document.getElementById("CEV_TIPO_CURSO0").checked) { //presencial
        var strFiltro = ""; // si es nuevo busca un 0, si es edición busca <= 1
//        var strIdCurso = document.getElementById("CEV_IDCURSO").value;
        var strPost = "";
        if (document.getElementById("CT_GRID").value == "3") {
            //nuevo
            strFiltro = "0";
        } else {
            // edición
            strFiltro = "1";
        }
        strPost += "filtro=" + strFiltro;
        strPost += "&curso=" + strIdCurso;

        var strContactoIds = "";
        var grid = jQuery("#GRIDPARTICIPA");
        var arr = grid.getDataIDs();
        for (var i = 0; i < arr.length; i++) {
            var id = arr[i];
            var lstRow = grid.getRowData(id);
            strPost += "&ids" + i + "=" + lstRow.CONTACTO_ID;
        }
        strPost += "&length=" + arr.length;
        $("#dialogWait").dialog("open");
        $.ajax({
            type: "POST",
            data: strPost,
            scriptCharset: "UTF-8",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "xml",
            url: "COFIDE_Telemarketing_vta.jsp?ID=23",
            success: function (datos) {
                //regresa a los que estan mas de una vez ya en el curso  
                var lstXml = datos.getElementsByTagName("vta")[0];
                var lstCte = lstXml.getElementsByTagName("datos");
                for (var i = 0; i < lstCte.length; i++) {
                    var objcte = lstCte[i];
                    if (objcte.getAttribute("existe") === "1") {
//                        continuarPaso(1);
                        alert("El participante: \n\n" + objcte.getAttribute("participante") + "\n\nya esta registrado en este curso.");
                    } else {
                        setparticipante();
                        $("#dialogInv").dialog("close");
//                        continuarPaso(2);
                    }
                }
                $("#dialogWait").dialog("close");
            }, error: function (objeto, quepaso, otroobj) {
                alert(":pto=23:" + objeto + " " + quepaso + " " + otroobj);
                $("#dialogWait").dialog("close");
            }});
    } else {
        setparticipante();
        $("#dialogInv").dialog("close");
//        continuarPaso(2);
    }
}

/*
 * nueva venta ###########
 */
function initVenta() {
    /* acción al presionar ESC*/
//    $(document).keyup(function (event) {
//        if (event.which == 27)
//        {
//            alert("NO OLVIDES COMPLETAR EL PROCESO DE VENTA CON \n\n***GUARDAR VENTA***");
//        }
//    });
    /* acción al presionar ESC*/
    hideDatFact();
    strOpcionDatosFact = "N";

    //inicializar metodo y formas de pago en cero
    document.getElementById("CEV_METODO").value = "0";
    document.getElementById("CEV_DESCRIPCION").value = "0";
    //OCULTA LA REFACTURACIÓN
    document.getElementById("CEV_RFAC1").parentNode.parentNode.style.display = "none";

    //bloqueo de factura y ticket
    if (document.getElementById("id_mov_m").value == "") { // si es una venta nueva, se debe hacer reservación
        document.getElementById("CEV_FAC1").disabled = true;
        document.getElementById("CEV_FAC2").disabled = true;
    } else { // en edición se habilitan
        document.getElementById("CEV_FAC1").disabled = false;
        document.getElementById("CEV_FAC2").disabled = false;
    }
    //bloqueo de factura y ticket

    $("#tabsNVENTA2").tabs("option", "active", 0);
    TabsMapFactCofide("1,2,3,4", false, "NVENTA2");
    document.getElementById("CEV_BTNGUARDAR").parentNode.parentNode.style.display = "none";
    document.getElementById("BTN_NEXT1").parentNode.parentNode.style.display = "none";
    var bolPromosion = document.getElementById("CT_PROMOSION").value;
    var intIdContacto = document.getElementById("CT_CONTACTO_ID").value;

    //LIMPIA LA BUSQUEDA DE CURSOS
    listarCurso_();

    //VALIDA SI ES INBOUND EL VENDEDOR O NO
    isUnBound();

    var strVenta = document.getElementById("CT_GRID").value; // tipo de venta / 3 = venta nueva , 1 o 0 edición

    if (strVenta == "3") { //venta nueva

        document.getElementById("FAC_CLOSE").parentNode.parentNode.style.display = "none"; //oculta boton cerrar
        document.getElementById("FAC_ADD").value = "AGREGAR"; //oculta boton cerrar

        //valida si, es promosión
        if (document.getElementById("CT_PROMOSION").value == "1") {

            document.getElementById("CEV_TIPO_CURSO1").disabled = true;
            document.getElementById("CEV_TIPO_CURSO2").disabled = true;
            document.getElementById("CEV_TIPO_CURSO3").disabled = true;
            document.getElementById("CEV_FAC1").disabled = true;
            document.getElementById("CEV_FAC2").disabled = true;

        }


    } else {// edicion de venta
        //INICIA LA TRANSACCIÓN PARA EL GUARDADO <BEGIN>
//        triggerTransaction(1);
        //campos de venta a detalle
//        document.getElementById("CEV_BTNGUARDAR").parentNode.parentNode.style.display = "";
        var strIdMov = document.getElementById("CT_ID_MOV").value;
        var strIDFAC = document.getElementById("CT_ID_FACTKT").value;
        var strTipoDoc = document.getElementById("CT_TIPODOC").value;
        var strEstatus = document.getElementById("CT_ESTATUS_VTA").value;

        document.getElementById("id_mov_m").value = strIdMov;
        document.getElementById("id_vta").value = strIDFAC;
        document.getElementById("tipo_doc").value = strTipoDoc;

        document.getElementById("BTN_NEXT1").parentNode.parentNode.style.display = "";
        document.getElementById("CEV_FAC1").disabled = false;
        document.getElementById("CEV_FAC2").disabled = false;

        //CAMBIAMOS DE PANTALLA A LOS DETALLES DEL CURSO
        continuarPaso(5);
        switch (strTipoDoc) {

            case 'RESERVACIÓN':
                document.getElementById("CEV_FAC0").checked = true;
                break;
            case 'TICKET':
                document.getElementById("CEV_FAC0").disabled = true;
                document.getElementById("CEV_FAC1").checked = true;
                break;
            case 'FACTURA':
                document.getElementById("CEV_FAC0").disabled = true;
                document.getElementById("CEV_FAC1").disabled = true;
                document.getElementById("CEV_FAC2").checked = true;
                document.getElementById("CEV_RFAC1").parentNode.parentNode.style.display = "";
                BloqCampos();
                break;
        }

        calculaPreciosGeneral(1); //carga información de la venta

    }

    document.getElementById("CT_CTE_NUEVO").value = "0";
}
//consulta curso del autocompletado, llena los campos de la primer pantalla de ventas
function consultaCurso_() {
    if (!document.getElementById("CEV_TIPO_CURSO3").checked) {

//        if (document.getElementById("CT_GRID").value == "3") { // venta nueva

        var strCurso = document.getElementById("CEV_IDCURSO").value; //id del producto/ si es edicion

        if (strCurso == "") { //si es vacio, se toma el id del nombre del curso
            strCurso = document.getElementById("CEV_NOMCURSO").value;
            strCurso = strCurso.split(" /", 1); //se obtiene el ID del curso
        }

        var bolDuplicado = true;
        var grid = jQuery("#CURSOS_GRD");
        var arr = grid.getDataIDs();
        var strTipoCurso = "";

        if (document.getElementById("CEV_TIPO_CURSO0").checked) {
            strTipoCurso = "PRESENCIAL";
        }
        if (document.getElementById("CEV_TIPO_CURSO1").checked) {
            strTipoCurso = "EN LINEA";
        }
        if (document.getElementById("CEV_TIPO_CURSO2").checked) {
            strTipoCurso = "VIDEO CURSO";
        }
        for (var i = 0; i < arr.length; i++) {

            var id = arr[i];
            var lstRow = grid.getRowData(id);

            if ((strCurso == lstRow.CURSO_ID) && (strTipoCurso == lstRow.TIPO_CURSO)) {

                bolDuplicado = false;
                break;

            }

        }

        if (bolDuplicado) {

            var strPost = "";
            strPost += "CEV_NOMCURSO=" + strCurso; //id del curso
            var strCurso1 = document.getElementById("CEV_TIPO_CURSO0").checked;
            var strCurso2 = document.getElementById("CEV_TIPO_CURSO1").checked;
            var strCurso3 = document.getElementById("CEV_TIPO_CURSO2").checked;
            if (strCurso1) { //presencial
                strPost += "&Clasifica=1";
            }
            if (strCurso2) { // en linea
                strPost += "&Clasifica=2";
            }
            if (strCurso3) { // video curso
                strPost += "&Clasifica=3";
            }
            if (strCurso != "" && (strCurso1 != "" || strCurso2 != "" || strCurso3 != "")) {
                $.ajax({
                    type: "POST",
                    data: strPost,
                    scriptCharset: "UTF-8",
                    contentType: "application/x-www-form-urlencoded;charset=utf-8",
                    cache: false,
                    dataType: "xml",
                    url: "COFIDE_Telemarketing_vta.jsp?ID=1",
                    success: function (datos) {
                        var lstXml = datos.getElementsByTagName("vta")[0];
                        var lstCte = lstXml.getElementsByTagName("datos");
                        for (var i = 0; i < lstCte.length; i++) {
                            var objcte = lstCte[i];
                            var intOcupado = parseInt(objcte.getAttribute("CEV_OCUPADO"));
                            var intLimite = parseInt(objcte.getAttribute("CEV_LIMITE"));
                            var strFecha = objcte.getAttribute("CEV_FECINICIO");
                            if (strCurso1) { //si es curso presencial, carga información de lugares
                                if (intOcupado < intLimite) { //MOSTRAMOS LA INFORMACIÓN DEL CURSO
                                    document.getElementById("CEV_LIMITE").value = objcte.getAttribute("CEV_LIMITE");
                                    document.getElementById("CEV_OCUPADO").value = objcte.getAttribute("CEV_OCUPADO");
                                    document.getElementById("CEV_NOMCURSO_TMP").value = objcte.getAttribute("CEV_NOMCURSO_TMP");
                                    document.getElementById("CEV_PRECIO_UNITARIO").value = FormatNumber(objcte.getAttribute("CEV_PRECIO_UNIT"), 2, true, false, true);
                                    document.getElementById("CEV_FECHA").value = strFecha;
                                    document.getElementById("CEV_IDCURSO").value = objcte.getAttribute("CEV_IDCURSO");
                                    document.getElementById("CC_HORARIO").value = objcte.getAttribute("CC_HORARIO");
                                    document.getElementById("CC_SEDE").value = objcte.getAttribute("CC_SEDE");
                                } else {
                                    document.getElementById("CEV_NOMCURSO").value = "";
                                    alert("El curso ya no tiene lugares disponibles");
                                }
                            } else { // en linea o presencial
                                document.getElementById("CEV_NOMCURSO_TMP").value = objcte.getAttribute("CEV_NOMCURSO_TMP");
                                document.getElementById("CEV_PRECIO_UNITARIO").value = FormatNumber(objcte.getAttribute("CEV_PRECIO_UNIT"), 2, true, false, true);
                                document.getElementById("CEV_FECHA").value = strFecha;
                                document.getElementById("CEV_IDCURSO").value = objcte.getAttribute("CEV_IDCURSO");
                                document.getElementById("CC_HORARIO").value = objcte.getAttribute("CC_HORARIO");
                                document.getElementById("CC_SEDE").value = objcte.getAttribute("CC_SEDE");
                            }
                        }
                    }, error: function (objeto, quepaso, otroobj) {
                        alert(":pto=TMK_VTA=1: " + objeto + " " + quepaso + " " + otroobj);
                    }
                });
            }

        } else {

            alert("Este curso ya ha sido seleccionado.");
            listarCurso_();

        }

    }

}

// limpia cursos desde selección
function listarCurso_() {
    //limpia los campos para la siguiente selección
    document.getElementById("CEV_NOMCURSO").value = "";
    document.getElementById("CEV_NOMCURSO_TMP").value = "";
    document.getElementById("CEV_LIMITE").value = "";
    document.getElementById("CEV_OCUPADO").value = "";
    document.getElementById("CEV_IDCURSO").value = "";
    document.getElementById("CEV_FECHA").value = "";
    document.getElementById("CEV_PRECIO_UNITARIO").value = "";
    document.getElementById("CEV_MEMBRESIA").value = "0"

    var strCurso1 = document.getElementById("CEV_TIPO_CURSO0").checked;
    var strCurso2 = document.getElementById("CEV_TIPO_CURSO1").checked;
    var strCurso3 = document.getElementById("CEV_TIPO_CURSO2").checked;
    var strCurso4 = document.getElementById("CEV_TIPO_CURSO3").checked;
    var strNomCurso = document.getElementById("CEV_NOMCURSO").value;
    //promosion
    var bolPromosion = document.getElementById("CT_PROMOSION").value;
    var strPost = "";
    if (strCurso1) { //presencial
        strPost += "Clasifica=1";
        document.getElementById("CEV_IDCURSO").parentNode.parentNode.style.display = "";
        document.getElementById("CEV_NOMCURSO").parentNode.parentNode.style.display = "";
        document.getElementById("CEV_FECHA").parentNode.parentNode.style.display = "";
        document.getElementById("CEV_LIMITE").parentNode.parentNode.style.display = "";
        document.getElementById("CEV_OCUPADO").parentNode.parentNode.style.display = "";
        document.getElementById("CEV_MEMBRESIA").parentNode.parentNode.style.display = "none";
    }
    if (strCurso2) { //online
        strPost += "Clasifica=2";
        document.getElementById("CEV_IDCURSO").parentNode.parentNode.style.display = "";
        document.getElementById("CEV_NOMCURSO").parentNode.parentNode.style.display = "";
        document.getElementById("CEV_FECHA").parentNode.parentNode.style.display = "";
        document.getElementById("CEV_LIMITE").parentNode.parentNode.style.display = "none";
        document.getElementById("CEV_OCUPADO").parentNode.parentNode.style.display = "none";
        document.getElementById("CEV_MEMBRESIA").parentNode.parentNode.style.display = "none";
    }
    if (strCurso3) { //video curso
        strPost += "Clasifica=3";
        document.getElementById("CEV_IDCURSO").parentNode.parentNode.style.display = "";
        document.getElementById("CEV_NOMCURSO").parentNode.parentNode.style.display = "";
        document.getElementById("CEV_FECHA").parentNode.parentNode.style.display = "";
        document.getElementById("CEV_LIMITE").parentNode.parentNode.style.display = "none";
        document.getElementById("CEV_OCUPADO").parentNode.parentNode.style.display = "none";
        document.getElementById("CEV_MEMBRESIA").parentNode.parentNode.style.display = "none";
    }
    if (strCurso4) { //cofide net
        strPost += "Clasifica=4";
        document.getElementById("CEV_LIMITE").parentNode.parentNode.style.display = "none";
        document.getElementById("CEV_OCUPADO").parentNode.parentNode.style.display = "none";
        document.getElementById("CEV_FECHA").parentNode.parentNode.style.display = "none";
        document.getElementById("CEV_IDCURSO").parentNode.parentNode.style.display = "none";
        document.getElementById("CEV_NOMCURSO").parentNode.parentNode.style.display = "none";
        document.getElementById("CEV_MEMBRESIA").parentNode.parentNode.style.display = "";
        document.getElementById("CEV_MEMBRESIA").value = "0";
    }
    strPost += "&promosion=" + bolPromosion;
    $(function () {
        $("#CEV_NOMCURSO").autocomplete({source: "COFIDE_Telemarketing.jsp?ID=12&" + strPost, minLength: 2});
    });
}
//agregar curso al grid de cursos
var intCurso_ = 0;
function addCurso() {

    var strPost = "";
    var strTipoCurso = "";

    var intIdMov = document.getElementById("id_mov_m").value;
    var strCurso1 = document.getElementById("CEV_TIPO_CURSO0").checked;
    var strCurso2 = document.getElementById("CEV_TIPO_CURSO1").checked;
    var strCurso3 = document.getElementById("CEV_TIPO_CURSO2").checked;
    var strCurso4 = document.getElementById("CEV_TIPO_CURSO3").checked;

    if (intIdMov == "") { //si aun no hay id de movimiento, crea uno

        intIdMov = "0";

    }

    var strPreciounitario = document.getElementById("CEV_PRECIO_UNITARIO").value.replace(",", "");


    strPost += "&id_m=" + intIdMov;
    strPost += "&idcurso=" + document.getElementById("CEV_IDCURSO").value;
    strPost += "&curso=" + encodeURIComponent(document.getElementById("CEV_NOMCURSO_TMP").value);
    strPost += "&fecha=" + document.getElementById("CEV_FECHA").value;
    strPost += "&precio=" + strPreciounitario;

    if (strCurso1) { //presencial
        strPost += "&tipocurso=1";
        strTipoCurso = "PRESENCIAL";
    }

    if (strCurso2) { // en linea
        strPost += "&tipocurso=2";
        strTipoCurso = "EN LINEA";
    }

    if (strCurso3) { //video curso
        strPost += "&tipocurso=3";
        strTipoCurso = "VIDEO CURSO";
    }

    if (strCurso4) { // cofide net
        strPost += "&tipocurso=4";
        strTipoCurso = "COFIDEnet";
    }

    $.ajax({
        type: "POST",
        data: encodeURI(strPost),
        scriptCharset: "UTF-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "html",
        url: "COFIDE_Tmk_vta.jsp?ID=1",
        success: function (datos) {
            datos = trim(datos);
            intIdMov = datos;

            if (intIdMov != "0") {

                intIdMov = intIdMov.replace(".OK", "");
                document.getElementById("id_mov_m").value = intIdMov;
                loadCursos(intIdMov);
                setTipoVta();
                listarCurso_();

            } else {

                alet("hubó un problema al guardar el curso.");

            }

        }, error: function (objeto, quepaso, otroobj) {
            alert(":pto=TMK_VTA=1: " + objeto + " " + quepaso + " " + otroobj);
        }
    });
}

//elimina el curso seleccionado en el grid y en la db
function delCurso() {

    var grid = jQuery("#CURSOS_GRD");
    var strIdMaster = "";
    var strIdCurso = "";
    var strTipoCurso = "1";
    var strPost = "";

    if (grid.getGridParam("selrow") != null) {

        document.getElementById("SioNO_inside").innerHTML = "¿Estas seguro de eliminar este curso del movimiento?";
        $("#SioNO").dialog("open");
        document.getElementById("btnSI").onclick = function () {
            $("#SioNO").dialog("close");

            var lstRow = grid.getRowData(grid.getGridParam("selrow"));
            strIdMaster = lstRow.CC_MOV_ID;
            strIdCurso = lstRow.CURSO_ID;
            if (lstRow.TIPO_CURSO == "PRESENCIAL") {
                strTipoCurso = "1";
            }
            if (lstRow.TIPO_CURSO == "EN LINEA") {
                strTipoCurso = "2";
            }
            if (lstRow.TIPO_CURSO == "VIDEO CURSO") {
                strTipoCurso = "3";
            }
            if (lstRow.TIPO_CURSO == "COFIDEnet") {
                strTipoCurso = "4";
            }

            strPost += "id_master=" + strIdMaster;
            strPost += "&id_curso=" + strIdCurso;
            strPost += "&tipocurso=" + strTipoCurso;
            $.ajax({
                type: "POST",
                data: strPost,
                scriptCharset: "UTF-8",
                contentType: "application/x-www-form-urlencoded;charset=utf-8",
                cache: false,
                dataType: "html",
                url: "COFIDE_Tmk_vta.jsp?ID=2",
                success: function (datos) {
                    datos = trim(datos);
                    if (datos != "OK") {

                        alert("Hubó un problema al eliminar el curso de la venta.");

                    } else {
                        //elimina el registro del grid
                        grid.delRowData(grid.getGridParam("selrow"));
                    }

                }, error: function (objeto, quepaso, otroobj) {

                    alert(":pto=TMK_VTA=2: " + objeto + " " + quepaso + " " + otroobj);

                }
            });

        };
        document.getElementById("btnNO").onclick = function () {
            $("#SioNO").dialog("close");
        };

    } else {

        alert("Selecciona un contacto");

    }

}

// valida que todos los cursos tengan por lo menos un lugar o participante
function ValidaCursos() {
    var reserva = document.getElementById("CEV_FAC0").checked;
    var ticket = document.getElementById("CEV_FAC1").checked;
    var factura = document.getElementById("CEV_FAC2").checked;
    if (reserva || ticket || factura) {
        var bolValido = true;
        var grid = jQuery("#CURSOS_GRD");
        var arr = grid.getDataIDs();
        for (var i = 0; i < arr.length; i++) {

            var id = arr[i];
            var lstRow = grid.getRowData(id);

            if (lstRow.LUGARES == "0") {

                alert("El curso: " + lstRow.CURSO_NOMBRE + "\n\nNo cuenta con Participantes aun!.");
                bolValido = false;
                break;

            }

        }

        if (bolValido) {

//            if (document.getElementById("CT_PROMOSION").value == "1") {

//            } else {
            calculaPreciosGeneral(2); //calcula el monto total de las partidas               
//            }

        }
    } else {
        alert("Selecciona un tipo de venta.");
    }
}

//abrir popup para los participantes
function dblClicParticipantes(id) {

    var strNomMain = objMap.getNomMain();

    if (strNomMain == "NVENTA") { //pantalla

        OpnEdit(document.getElementById("Ed" + strNomMain));

    } else {

        if (strNomMain == "C_TELEM") { //abre pantalla

            var objSecModiVta = objMap.getScreen("PARTICIPANTE");

            if (objSecModiVta != null) {
                objSecModiVta.bolActivo = false;
                objSecModiVta.bolMain = false;
                objSecModiVta.bolInit = false;
                objSecModiVta.idOperAct = 0;
            }

            OpnOpt("PARTICIPANTE", "_ed", "dialogInv", false, false, true);

        }

    }

}

//carga participantes
function Participantes() {

    var strVenta = document.getElementById("CT_GRID").value;
    var grid = jQuery("#CURSOS_GRD"); //grid o pantalla
    var lstVal = grid.getRowData(grid.getGridParam("selrow"));

    var strTipoDocumento = document.getElementById("tipo_doc").value;

    //si es factura la edición, se va a bloquear el  descuento    
    if (strTipoDocumento == "FACTURA") {
        document.getElementById("PAR_DESCUENTO_POR").disabled = true;
    }

    if (strPrecioPartidaSeleccionada == "") { // si esta vacio, se le asigna un valor
        strPrecioPartidaSeleccionada = lstVal.P_UNIT; //almacena en una variable global el precio unitario del curso seleccionado
        strDescuentoPartidaSeleccionada = lstVal.P_DESC_PORC; //almacena en una variable global el descuento del curso seleccionado    
    }
    if (document.getElementById("CT_CONTACTO_ID").value == "") {
        if (lstVal.LUGARES == "" || lstVal.LUGARES == "0") {

            LoadParticipantes(1);

        } else {

            // boton para agregar participantes
            var strAdd1 = "<table>"
                    + "<tr>"
                    + '<td><a href="javascript:addParticipanteExistente()" ><i class = "fa fa-plus-square" title="AGREGAR PARTICIPANTE EXISTENTE"  style="font-size:30px; color: #00BFFF"></i></td>'
                    + "</tr>"
                    + "</table>";
            document.getElementById("div_add1").innerHTML = strAdd1;

            LoadParticipantes(2);

        }
    } else {

        LoadParticipantes(document.getElementById("CT_CONTACTO_ID").value);
//        alert("cargar al particioante seleccionado: " + document.getElementById("CT_CONTACTO_ID").value);

    }
    hiddenCamposAlta(1); // oculta campos de particioante        
    setTimeout("setOperacionPartida()", 1000);

}

var bolEstatusParticipante = ""; //se esta en edición
//ocultar campos al abrir
function hiddenCamposAlta(opc) {

    if (opc == 1) { //OCULTAR

        document.getElementById("PAR_TITULO").parentNode.parentNode.style.display = "none";
        document.getElementById("PAR_TITULO").value = "";
        document.getElementById("PAR_NOMBRE").parentNode.parentNode.style.display = "none";
        document.getElementById("PAR_NOMBRE").value = "";
        document.getElementById("PAR_APPAT").parentNode.parentNode.style.display = "none";
        document.getElementById("PAR_APPAT").value = "";
        document.getElementById("PAR_APMAT").parentNode.parentNode.style.display = "none";
        document.getElementById("PAR_APMAT").value = "";
        document.getElementById("PAR_ASOC").parentNode.parentNode.style.display = "none";
        document.getElementById("PAR_ASOC").value = "";
        document.getElementById("PAR_NUMSOC").parentNode.parentNode.style.display = "none";
        document.getElementById("PAR_NUMSOC").value = "";
        document.getElementById("PAR_CORREO").parentNode.parentNode.style.display = "none";
        document.getElementById("PAR_CORREO").value = "";
        document.getElementById("PAR_MATERIAL").parentNode.parentNode.style.display = "none";
        document.getElementById("PAR_MATERIAL").checked = false;
        document.getElementById("CANCELBTN_PAR").parentNode.parentNode.style.display = "none";
        document.getElementById("SAVEBTN_PAR").parentNode.parentNode.style.display = "none";
        document.getElementById("ADDBTN_PAR").parentNode.parentNode.style.display = "";
        document.getElementById("DELBTN_PAR").parentNode.parentNode.style.display = "";
        document.getElementById("EDITBTN_PAR").parentNode.parentNode.style.display = "";
        document.getElementById("CLOSEBTN").parentNode.parentNode.style.display = "";
        bolEstatusParticipante = "";

    }
    if (opc == 2) { //MOSTRAR

        document.getElementById("PAR_TITULO").parentNode.parentNode.style.display = "";
        document.getElementById("PAR_NOMBRE").parentNode.parentNode.style.display = "";
        document.getElementById("PAR_APPAT").parentNode.parentNode.style.display = "";
        document.getElementById("PAR_APMAT").parentNode.parentNode.style.display = "";
        document.getElementById("PAR_ASOC").parentNode.parentNode.style.display = "";
        document.getElementById("PAR_NUMSOC").parentNode.parentNode.style.display = "";
        document.getElementById("PAR_CORREO").parentNode.parentNode.style.display = "";
        document.getElementById("PAR_MATERIAL").parentNode.parentNode.style.display = "";
        document.getElementById("CANCELBTN_PAR").parentNode.parentNode.style.display = "";
        document.getElementById("SAVEBTN_PAR").parentNode.parentNode.style.display = "";
        document.getElementById("ADDBTN_PAR").parentNode.parentNode.style.display = "none";
        document.getElementById("DELBTN_PAR").parentNode.parentNode.style.display = "none";
        document.getElementById("EDITBTN_PAR").parentNode.parentNode.style.display = "none";
        document.getElementById("CLOSEBTN").parentNode.parentNode.style.display = "none";
        bolEstatusParticipante = "E";

    }

}

//cerrar ventana de participante
function closeParticipante() {
    if (validaparticipante()) {
        //GUARDAR EL GRID DE LA VENTA
        if (bolEstatusParticipante != "E") {

            getDisponibilidadCurso();

        } else {

            alert("NO olvides guardar la información del contacto.");

        }
    }

}

// carga cursos
var intCurso = 0;
function loadCursos(strIdMaster) {

    $("#dialogWait").dialog("open");
    $.ajax({
        type: "POST",
        data: "id_master=" + strIdMaster,
        scriptCharset: "UTF-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "COFIDE_Tmk_vta.jsp?ID=3",
        success: function (datos) {

            jQuery("#CURSOS_GRD").clearGridData();
            var lstXml = datos.getElementsByTagName("vta")[0];
            var lstCte = lstXml.getElementsByTagName("datos");
            for (var i = 0; i < lstCte.length; i++) {
                var objcte = lstCte[i];
                var datarow = {

                    CC_MOV_ID: objcte.getAttribute("CV_ID_M"),
                    CURSO_ID: objcte.getAttribute("CV_ID_CURSO"),
                    CURSO_NOMBRE: objcte.getAttribute("CV_CURSO"),
                    TIPO_CURSO: objcte.getAttribute("CV_TIPO_CURSO"),
                    LUGARES: objcte.getAttribute("CV_LUGARES"),
                    P_UNIT: "$ " + FormatNumber(objcte.getAttribute("P_UNIT"), 2, true, false, true),
//                    P_UNIT: objcte.getAttribute("P_UNIT"),
                    P_DESC: "$ " + FormatNumber(objcte.getAttribute("P_DESC"), 2, true, false, true),
//                    P_DESC: objcte.getAttribute("P_DESC"),
                    P_TOTAL: "<b>$ " + FormatNumber(objcte.getAttribute("P_TOTAL"), 2, true, false, true) + "</b>",
//                    P_TOTAL: objcte.getAttribute("P_TOTAL"),
                    P_IVA: "$ " + FormatNumber(objcte.getAttribute("P_IVA"), 2, true, false, true),
                    P_SUBTOTAL: "$ " + FormatNumber(objcte.getAttribute("P_SUBTOTAL"), 2, true, false, true),
                    P_DESC_PORC: objcte.getAttribute("P_DESC_POR") + " % ",
                    FECHA_INICIO: objcte.getAttribute("CV_FECHA_INI")

                };
                intCurso++;
                jQuery("#CURSOS_GRD").addRowData(intCurso, datarow, "last");
            }
            $("#dialogWait").dialog("close");
        }, error: function (objeto, quepaso, otroobj) {
            alert(":pto=TMK_VTA=3: " + objeto + " " + quepaso + " " + otroobj);
        }
    });

}

//crea contacto / participante
var intParticipanteNuevo = 50;
function addParticipante(strNombre, strApPat, strApMat, strTitulo, intNumSoc, strCorreo, strIdCliente, bolMaterial, strAsoc) {

    var strPost = "";
    var strIdMaster = document.getElementById("id_mov_m").value;
    var strResp = "";
    var strIdContactoid = "0";

    strPost = "id_master=" + strIdMaster;
    strPost += "&nombre=" + strNombre;
    strPost += "&appat=" + strApPat;
    strPost += "&apmat=" + strApMat;
    strPost += "&titulo=" + strTitulo;
    strPost += "&numerosoc=" + intNumSoc;
    strPost += "&correo=" + strCorreo;
    strPost += "&ct_id=" + strIdCliente;
    strPost += "&material=" + bolMaterial;
    strPost += "&asociacion=" + strAsoc;
    $("#dialogWait").dialog("open");
    $.ajax({
        type: "POST",
        data: strPost,
        scriptCharset: "UTF-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "html",
        url: "COFIDE_Tmk_vta.jsp?ID=4",
        success: function (datos) {
            datos = trim(datos);
            strResp = Left(datos, 3);

            if (strResp == "OK,") {

                strIdContactoid = datos.replace(strResp, "");

                var datarow = {

                    CCO_NOMBRE: strNombre,
                    CCO_APPATERNO: strApPat,
                    CCO_APMATERNO: strApMat,
                    CCO_TITULO: strTitulo,
                    CCO_NOSOCIO: intNumSoc,
                    CCO_CORREO: strCorreo,
                    CONTACTO_ID: strIdContactoid,
                    CT_ID: strIdCliente,
                    MATERIAL: bolMaterial,
                    CCO_ASOCIACION: strAsoc

                };

                jQuery("#GRIDPARTICIPA").addRowData(intParticipanteNuevo, datarow, "last");
                intParticipanteNuevo++;
                hiddenCamposAlta(1); //oculta campos

            } else {

                alert("Hubó un problema al agregar el participante.");

            }
            $("#dialogWait").dialog("close");
        }, error: function (objeto, quepaso, otroobj) {

            alert(":pto=TMK_VTA=4: " + objeto + " " + quepaso + " " + otroobj);

        }

    });

}

//quitar participante
function delParticipante() {
    var grid = jQuery("#GRIDPARTICIPA");
    if (grid.getGridParam("selrow") != null) {
        grid.delRowData(grid.getGridParam("selrow"));
        setOperacionPartida();
    } else {
        alert("Selecciona un participante a remover.");
    }
}

//editar participante
var strIdCliente = ""; //id del cliente
function editParticipante_() {

    var grid = jQuery("#GRIDPARTICIPA");
    if (grid.getGridParam("selrow") != null) {

        hiddenCamposAlta(2);

        var lstRow = grid.getRowData(grid.getGridParam("selrow"));

        document.getElementById("PAR_TITULO").value = lstRow.CCO_TITULO;
        document.getElementById("PAR_NOMBRE").value = lstRow.CCO_NOMBRE;
        document.getElementById("PAR_APPAT").value = lstRow.CCO_APPATERNO;
        document.getElementById("PAR_APMAT").value = lstRow.CCO_APMATERNO;
        document.getElementById("PAR_ASOC").value = lstRow.CCO_ASOCIACION;
        document.getElementById("PAR_NUMSOC").value = intDato(lstRow.CCO_NOSOCIO);
        document.getElementById("PAR_CORREO").value = lstRow.CCO_CORREO;

        if (lstRow.MATERIAL == "SI") {

            document.getElementById("PAR_MATERIAL").checked = true;

        } else {

            document.getElementById("PAR_MATERIAL").checked = false;

        }

        delParticipante(); //elimina la selección

        //guardar en temporal
        titulo = lstRow.CCO_TITULO;
        nombre = lstRow.CCO_NOMBRE;
        apellidop = lstRow.CCO_APPATERNO;
        apellidom = lstRow.CCO_APMATERNO;
        asoc = lstRow.CCO_ASOCIACION;
        asonum = lstRow.CCO_NOSOCIO;
        correo = lstRow.CCO_CORREO;
        material = lstRow.MATERIAL;
        contacto_id = lstRow.CONTACTO_ID;
        strIdCliente = lstRow.CT_ID;
        // opcion temporal E
        opcParticipante = "E";

    } else {

        delParticipante(); //mensaje de validación de selección

    }

}

//guarda participante
//var intCola = 50;
function savePartitipante() {
    opcParticipante = "";
    var bolDatos = false;
    var intRow = 0;
    var strIdCte = 0;

    if (document.getElementById("PAR_NOMBRE").value != "" && document.getElementById("PAR_CORREO").value != "") {

        bolDatos = true;

    } else {

        alert("El nombre del participante y su correo, son IMPORTANTES!.");

    }

    if (bolDatos) {

        var strTitulo = document.getElementById("PAR_TITULO").value;
        var strNombre = document.getElementById("PAR_NOMBRE").value;
        var strApPat = document.getElementById("PAR_APPAT").value;
        var strApMat = document.getElementById("PAR_APMAT").value;
        var strAsoc = document.getElementById("PAR_ASOC").value;
        var strNum = document.getElementById("PAR_NUMSOC").value;
        var strCorreo = document.getElementById("PAR_CORREO").value;
        var bolMaterial = "NO";
        var intNumSoc = intDato(strNum);

        if (document.getElementById("PAR_MATERIAL").checked) {

            bolMaterial = "SI";

        }

        if (strIdCte == "0") {

            var grid = jQuery("#GRIDPARTICIPA");
            var arr = grid.getDataIDs();

            for (var i = 0; i < arr.length; i++) {
                var id = arr[i];
                var lstRow = grid.getRowData(id);
                if (lstRow.CT_ID != "") {

                    strIdCte = lstRow.CT_ID;
                    break;

                } else {

                    strIdCte = document.getElementById("CT_ID").value;

                }
            }

        }

        if (contacto_id != "0") { //actualiza registro

            var datarow = {

                CCO_NOMBRE: strNombre,
                CCO_APPATERNO: strApPat,
                CCO_APMATERNO: strApMat,
                CCO_TITULO: strTitulo,
                CCO_NOSOCIO: intNumSoc,
                CCO_CORREO: strCorreo,
                CONTACTO_ID: contacto_id,
                CT_ID: strIdCte,
                MATERIAL: bolMaterial,
                CCO_ASOCIACION: strAsoc

            };
            //actualización general del contacto/participante
            UpDateContactoParticipante(strNombre, strApPat, strApMat, strTitulo, intNumSoc, contacto_id, strAsoc, strCorreo, bolMaterial);

            hiddenCamposAlta(1); //oculta campos
            contacto_id = "0";
            jQuery("#GRIDPARTICIPA").addRowData(intCola, datarow, "last");
            intCola++;

        } else { //crea contacto y participante           

            addParticipante(strNombre, strApPat, strApMat, strTitulo, intNumSoc, strCorreo, strIdCte, bolMaterial, strAsoc);

        }
        //calcula los importes
        setOperacionPartida();
    }
    strPrecioPartidaSeleccionada = "";
    strDescuentoPartidaSeleccionada = "0";
}

//cancelar operación
function cancelParticipante() {

    if (opcParticipante == "E") {

        var datarow = {

            CCO_NOMBRE: nombre,
            CCO_APPATERNO: apellidop,
            CCO_APMATERNO: apellidom,
            CCO_TITULO: titulo,
            CCO_NOSOCIO: intDato(asonum),
            CCO_CORREO: correo,
            CONTACTO_ID: contacto_id,
            CT_ID: strIdCliente,
            MATERIAL: material,
            CCO_ASOCIACION: asoc

        };

        jQuery("#GRIDPARTICIPA").addRowData(intCola, datarow, "last");
        intCola++;

    }

    hiddenCamposAlta(1); //oculta campos
    //limpia variables
    opcParticipante = "";
    nombre = "";
    apellidop = "";
    apellidom = "";
    titulo = "";
    asonum = "";
    correo = "";
    contacto_id = "0";
    asoc = "";

}

// convierte a numero o manda cero
function intDato(strDato) {
//    strDato = parseInt(strDato);
//    if (isNaN(strDato)) {
//        strDato = 0;
//    }
    return strDato;
}

//guarda participantes en el curso y la venta
function setparticipante() {

    var strIdMaster = document.getElementById("id_mov_m").value;
    var grid = jQuery("#CURSOS_GRD");
    var lstRow = grid.getRowData(grid.getGridParam("selrow"));
    var stridCurso = lstRow.CURSO_ID;
    var strTipoDoc = document.getElementById("tipo_doc").value; //cuenta unicamente, si es una edición de venta
    var strIdVta = document.getElementById("id_vta").value; //cuenta unicamente, si es una edición de venta
    var strTipoCurso = "";

    var strNumParticipantes = document.getElementById("PAR_PARTICIPANTESNUM").value;
    var strPrecio = document.getElementById("PAR_PRECIO_UNIT").value;
    var strDescuento = document.getElementById("PAR_DESCUENTO").value;
    var strDescuentoPor = document.getElementById("PAR_DESCUENTO_POR").value;
    var strImporte = document.getElementById("PAR_IMPORTE").value;
    var strTotal = document.getElementById("PAR_TOTAL").value;
    var strIVA = document.getElementById("PAR_IVA").value;


    if (lstRow.TIPO_CURSO == "PRESENCIAL") {

        strTipoCurso = "1";

    }

    if (lstRow.TIPO_CURSO == "EN LINEA") {

        strTipoCurso = "2";

    }

    if (lstRow.TIPO_CURSO == "VIDEO CURSO") {

        strTipoCurso = "3";

    }

    if (lstRow.TIPO_CURSO == "COFIDEnet") {

        strTipoCurso = "4";

    }

    var strPost = "";

    var strGridParticipante = jQuery("#GRIDPARTICIPA");
    var intTamañoLista = strGridParticipante.getDataIDs(); //tamaño de registros
    for (var i = 0; i < intTamañoLista.length; i++) {

        var id = intTamañoLista[i];
        var listRowParticipante = strGridParticipante.getRowData(id);

        strPost += "&titulo" + i + "=" + listRowParticipante.CCO_TITULO;
        strPost += "&nombre" + i + "=" + listRowParticipante.CCO_NOMBRE;
        strPost += "&appat" + i + "=" + listRowParticipante.CCO_APPATERNO;
        strPost += "&apmat" + i + "=" + listRowParticipante.CCO_APMATERNO;
        strPost += "&nosocio" + i + "=" + listRowParticipante.CCO_NOSOCIO;
        strPost += "&asociacion" + i + "=" + listRowParticipante.CCO_ASOCIACION;
        strPost += "&correo" + i + "=" + listRowParticipante.CCO_CORREO;
        strPost += "&material" + i + "=" + listRowParticipante.MATERIAL;
        strPost += "&idcontacto" + i + "=" + listRowParticipante.CONTACTO_ID;
        strPost += "&ct_id" + i + "=" + listRowParticipante.CT_ID;

    }

    strPost += "&length=" + intTamañoLista.length;
    strPost += "&idmaster=" + strIdMaster;
    strPost += "&idcurso=" + stridCurso;
    strPost += "&tipocurso=" + strTipoCurso;

    strPost += "&precio=" + strPrecio;
    strPost += "&descuento=" + strDescuento;
    strPost += "&descuentopor=" + strDescuentoPor;
    strPost += "&importe=" + strImporte;
    strPost += "&total=" + strTotal;
    strPost += "&iva=" + strIVA;

    strPost += "&tipodoc=" + strTipoDoc; //edicón de venta
    strPost += "&idvta=" + strIdVta; //edicón de venta

    $.ajax({
        type: "POST",
        data: strPost,
        scriptCharset: "UTF-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "html",
        url: "COFIDE_Tmk_vta.jsp?ID=5",
        beforeSend: function () {

            $("#dialogWait").dialog("open");

        },
        success: function (datos) {
            datos = trim(datos);

            if (datos != "OK") {

                alert(datos);

            } else {

                loadCursos(strIdMaster); // recarga los cursos

            }
            $("#dialogWait").dialog("close");
        }, error: function (objeto, quepaso, otroobj) {
            alert(":pto=TMK_VTA=5: " + objeto + " " + quepaso + " " + otroobj);
        }
    });

}

//cerrar la ventana de participantes
function closePart() {
    $("#dialogInv").dialog("close");
    strPrecioPartidaSeleccionada = ""; //limpia el precio en la variable global
    strDescuentoPartidaSeleccionada = "0";
}

//validar disponibilidad
function getDisponibilidadCurso() {

    var grid = jQuery("#CURSOS_GRD");
    var lstRow = grid.getRowData(grid.getGridParam("selrow"));
    var stridCurso = lstRow.CURSO_ID;
    var strPost = "";
    var intLugares = 0;
    var intDisponibles = 0;
    var intInscritos = parseInt(document.getElementById("CEV_PARTICIPANTE").value);
    strPost = "idcurso=" + stridCurso;

    if (lstRow.TIPO_CURSO == "PRESENCIAL") {

        $.ajax({
            type: "POST",
            data: strPost,
            scriptCharset: "UTF-8",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "html",
            url: "COFIDE_Tmk_vta.jsp?ID=7",
            success: function (datos) {

                datos = trim(datos);
                if (Left(datos, 2) == "OK") {

                    intLugares = parseInt(datos.replace("OK", ""));
                    intDisponibles = intLugares + intInscritos; // lugares disponibles menos lugares registrados

                    if (intDisponibles >= intRowD()) {

//                        setparticipante();
//                        $("#dialogInv").dialog("close");
                        CheckParticipantExist(stridCurso);

                    } else {

                        alert("El curso : " + lstRow.CURSO_NOMBRE + "\nsolo cuenta con: " + intLugares + " lugares disponibles.");

                    }

                } else {

                    alert("Ya no cuenta con mas lugares el curso.");

                }

                $("#dialogWait").dialog("close");
            }, error: function (objeto, quepaso, otroobj) {
                alert(":pto=TMK_VTA=7: " + objeto + " " + quepaso + " " + otroobj);
            }
        });

    } else { //guarda los participantes en linea/ video curso/ COFIDEnet

        setparticipante();
        $("#dialogInv").dialog("close");
//        CheckParticipantExist(stridCurso);

    }
}

// asigna en default una resevración
function setTipoVta() {
    var reserva = document.getElementById("CEV_FAC0").checked;
    var ticket = document.getElementById("CEV_FAC1").checked;
    var factura = document.getElementById("CEV_FAC2").checked;
    if (!reserva && !ticket && !factura) {
        document.getElementById("CEV_FAC0").checked = true;
    }
}

var strPrecioPartidaSeleccionada = "";
var strDescuentoPartidaSeleccionada = "0";

function setOperacionPartida() {
    var dblImpuesto = 0.16;
    var dblIva = 0;

    if (document.getElementById("CEV_FAC1").checked) {
        dblImpuesto = 0.0;
    }

    strDescuentoPartidaSeleccionada = trim(strDescuentoPartidaSeleccionada.replace(".0 %", ""));

    if (strPrecioPartidaSeleccionada != "") { //si esta vacio. no cambia en nada el valor
        strPrecioPartidaSeleccionada = strPrecioPartidaSeleccionada.replace("$", "").replace(",", ""); //limpia el precio
        document.getElementById("PAR_PRECIO_UNIT").value = trim(strPrecioPartidaSeleccionada);

    }

    var strPrecio = document.getElementById("PAR_PRECIO_UNIT").value;
    var strDescuentoPor = "0";

    if (strDescuentoPartidaSeleccionada != "0") {

        document.getElementById("PAR_DESCUENTO_POR").value = strDescuentoPartidaSeleccionada;
        document.getElementById("BOLDESC").value = "1";

    }

    strDescuentoPor = document.getElementById("PAR_DESCUENTO_POR").value;
    var strImporte = intRowD() * parseFloat(strPrecio);
    var strDescuento = (parseInt(strDescuentoPor) / 100) * parseFloat(strImporte);
    var dblSubtotal = parseFloat(strImporte) - parseFloat(strDescuento);
    dblIva = parseFloat(dblSubtotal) * parseFloat(dblImpuesto);
    var strTotal = parseFloat(dblSubtotal) + parseFloat(dblIva);

    document.getElementById("PAR_PARTICIPANTESNUM").value = intRowD();
    document.getElementById("PAR_DESCUENTO").value = strDescuento;
    document.getElementById("PAR_IMPORTE").value = strImporte;
    document.getElementById("PAR_IVA").value = dblIva;
    document.getElementById("PAR_TOTAL").value = strTotal;
    strPrecioPartidaSeleccionada = "";
    strDescuentoPartidaSeleccionada = "0";
}

function validaDescuento() {
    var strDescuentoPor = document.getElementById("PAR_DESCUENTO_POR").value;
    var bolSiguiente = false;
    var intDescuento = 10;
    var grid = jQuery("#CURSOS_GRD");
    var lstRow = grid.getRowData(grid.getGridParam("selrow"));
    var strFechaToday = strHoyFecha;
    strFechaToday = strFechaToday.substring(6, 10) + strFechaToday.substring(3, 5); // fecha actual

    if (lstRow.TIPO_CURSO == "COFIDEnet" && (strFechaToday == "201710" || strFechaToday == "201711" || strFechaToday == "201712")) {
        intDescuento = 20; //si es una membresia, aplicara sin  problemas hasta un 20% de descuento
    }
    if (strDescuentoPor > intDescuento) {
        //solicita contraseña
        console.log("abre password");
        AbreVentanaDescuento();
    } else {
        //aplica el descuento okis
        bolSiguiente = true;
//        console.log("pasa okis");
        document.getElementById("BOLDESC").value = "1";
    }
    //si paso la validación aplica descuentos
    if (bolSiguiente) {
        //si es correcta la contraseña es
        setOperacionPartida();
    }
}
function AbreVentanaDescuento() {
    document.getElementById("BOLDESC").value = "0";
    setOperacionPartida();
    $("#dialogGen").dialog({
        dialogClass: "no-close"
    });
    var strHTML = '<div style="text-align:center; ">';
    strHTML += '<header>Autorizar descuento</header>';
    strHTML += '<select id="usuario" style="width:220px" >';
    strHTML += '<option value="0">Seleccione usuario</option>';
    strHTML += '<option value="1">Autoriza</option>';
    strHTML += '<option value="2">Supervisor</option>';
    strHTML += '<option value="3">Gerente</option>';
    strHTML += '</select>';
    strHTML += '<br />';
    strHTML += '<input type="password" size="30" placeholder="Contraseña" id="password"/>';
    strHTML += '<br />';
    strHTML += '<input type="button" value="¡Autorizar!" onclick="validaUsuarioDescuento()"/>';
    strHTML += '<input type="button" value="Cancelar" onclick="closeDescuento()"/>';
    strHTML += '</div>';
    document.getElementById("dialogGen_inside").innerHTML = strHTML;
    $("#dialogGen").dialog("option", "title", "Autoriación de descuentos!");
    $("#dialogGen").dialog("open");
}

var descuentoOk = "0";
function validaUsuarioDescuento() {
    descuentoOk = "0";
    var usuario = document.getElementById("usuario").value;
    var password = document.getElementById("password").value;
    var bolAutorizado = false;

    if (usuario == "0") {
        alert("Debes elegir un usuario");
    } else {
        if (usuario == "1") {
            if (password == "ae1") {
                alert("Autorizado!");
                bolAutorizado = true;
            }
        }
        if (usuario == "2") {
            if (password == "ae1") {
                alert("Autorizado!");
                bolAutorizado = true;
            }
        }
        if (usuario == "3") {
            if (password == ":v") {
                alert("Autorizado!");
                bolAutorizado = true;
            }
        }
    }
    if (bolAutorizado) {
        $("#dialogGen").dialog("close");
        descuentoOk = "1";
    } else {
        if (usuario != "0") {
            alert("Denegado!!");
        }
    }
    setOperacionPartida();
    document.getElementById("BOLDESC").value = descuentoOk;
}

function closeDescuento() {
    $("#dialogGen").dialog("close");

    if (document.getElementById("BOLDESC").value == "0") {
        document.getElementById("PAR_DESCUENTO_POR").value = "0";
    }
    setOperacionPartida();
}
/**
 * calcula el monto total de las partidas, el resumen de la venta
 */
function calculaPreciosGeneral(opc) {

//tipo de documento (IVA)
    var strTipoDoc = "1"; //reservación
    var intCurso = "0";
    var intParticipante = "0";
    var strIdMaster = document.getElementById("id_mov_m").value;
    var strPost = "";

    if (document.getElementById("CEV_FAC1").checked) {
        strTipoDoc = "2"; //ticket        
    }
    if (document.getElementById("CEV_FAC2").checked) {
        strTipoDoc = "3"; //factura
    }

    strPost = "idMaster=" + strIdMaster;
    strPost += "&tipodoc=" + strTipoDoc;

    $.ajax({
        type: "POST",
        data: strPost,
        scriptCharset: "UTF-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "COFIDE_Tmk_vta.jsp?ID=8",
        beforeSend: function () {
            $("#dialogWait").dialog("open");
        },
        success: function (datos) {

            var lstXml = datos.getElementsByTagName("vta")[0];
            var lstCte = lstXml.getElementsByTagName("datos");
            for (var i = 0; i < lstCte.length; i++) {
                var objcte = lstCte[i];

                document.getElementById("CEV_CURSOS").value = objcte.getAttribute("num_cursos"); //CURSOS
                document.getElementById("CEV_PARTICIPANTE").value = objcte.getAttribute("num_participantes"); //PARTICIPANTES
                document.getElementById("precio_base").value = objcte.getAttribute("precio_base"); //precio base
                document.getElementById("CEV_SUB1").value = FormatNumber(objcte.getAttribute("subtotal"), 2, true, false, true);//SUBTOTAL
                document.getElementById("CEV_DESCUENTO").value = FormatNumber(objcte.getAttribute("descuento"), 2, true, false, true);//SUBTOTAL
                document.getElementById("CEV_SUB2").value = FormatNumber(objcte.getAttribute("iva"), 2, true, false, true);//IVA
                document.getElementById("CEV_SUB3").value = FormatNumber(objcte.getAttribute("total"), 2, true, false, true); //TOTAL
                document.getElementById("CEV_DESCRIPCION").value = objcte.getAttribute("formapago"); //forma de pago
                document.getElementById("CEV_METODO").value = objcte.getAttribute("metodopago");//metodo de pago
                document.getElementById("CEV_NOM_FILE").value = objcte.getAttribute("comprobante");//COMPROBANTE de pago                
                document.getElementById("CEV_DIGITO").value = objcte.getAttribute("referencia");//COMPROBANTE de pago                
                if (objcte.getAttribute("comprobante") != "") {
                    document.getElementById("CEV_PAGO_OK").value = 1;// venta pagada
                } else {
                    document.getElementById("CEV_PAGO_OK").value = 0;//venta sin pago
                }
                document.getElementById("num_participantes").value = objcte.getAttribute("participantesvendidos");// NUMERO DE PARTICIPANTES VENDIDOS
                document.getElementById("num_cursos").value = objcte.getAttribute("cursosvendidos");// NUMERO DE CURSOS VENDIDOS
                document.getElementById("CEV_FECHAPAGO").value = objcte.getAttribute("fechacobro");// fecha de cobro

            }
            if (opc == 2) {

                if (document.getElementById("CT_PROMOSION").value == "1") { //pasar a la pantalla final

                    continuarPaso(4); //cambiamos de pantalla al final
                    document.getElementById("CEV_BTNGUARDAR").value = "GUARDAR PROMOCIÓN";

                } else { //continuar con el proceso
                    continuarPaso(2); //cambiamos de pantalla al resumen de la venta
                }

            }

            $("#dialogWait").dialog("close");

        }, error: function (objeto, quepaso, otroobj) {
            alert(":pto=TMK_VTA=7: " + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }
    });

}
/*
 * validamos que la información con la que se facturo, corresponda con la que se va a guardar nuevamente
 */
function getValidaInfoFac() {
    var intNumParticipanteVendido = document.getElementById("num_participantes").value;
    var intNumCursosVendido = document.getElementById("num_cursos").value;
    var intNumParticipante = document.getElementById("CEV_PARTICIPANTE").value;
    var intNumCursos = document.getElementById("CEV_CURSOS").value;
    var strTipoDocumento = document.getElementById("tipo_doc").value;
    var strComentario = "";

    //si es una factura la que se va a modificar, se hará la validación
    if (strTipoDocumento == "FACTURA") {
        if (intNumCursosVendido == intNumCursos) {

            if (intNumParticipanteVendido == intNumParticipante) {

                return true;

            } else {

                if (intNumParticipanteVendido > intNumParticipante) {

                    strComentario = "<i class='fa fa-exclamation-triangle' style='color:red' >&nbsp;</i>";
                    strComentario += "El numero de participantes facturados no coincide.&nbsp;";
                    strComentario += "<i class='fa fa-exclamation-triangle' style='color:red' ></i>";
                    strComentario += "<br />Hace falta :" + (intNumParticipanteVendido - intNumParticipante) + " participante(s)";

                }

                if (intNumParticipanteVendido < intNumParticipante) {

                    strComentario = "<i class='fa fa-exclamation-triangle' style='color:red' >&nbsp;</i>";
                    strComentario += "El numero de participantes facturados no coincide.&nbsp;";
                    strComentario += "<i class='fa fa-exclamation-triangle' style='color:red' ></i>";
                    strComentario += "<br />Se excede :" + (intNumParticipante - intNumParticipanteVendido) + " participante(s)";

                }

                showCommentSale(strComentario);
                return false;

            }

        } else {

            if (intNumCursosVendido > intNumCursos) {

                strComentario = "<i class='fa fa-exclamation-triangle' style='color:red' >&nbsp;</i>";
                strComentario += "El numero de cursos facturados no coincide.&nbsp;";
                strComentario += "<i class='fa fa-exclamation-triangle' style='color:red' ></i>";
                strComentario += "<br />Hace falta :" + (intNumCursosVendido - intNumCursos) + " curso(s)";

            }

            if (intNumCursosVendido < intNumCursos) {

                strComentario = "<i class='fa fa-exclamation-triangle' style='color:red' >&nbsp;</i>";
                strComentario += "El numero de cursos facturados no coincide.&nbsp;";
                strComentario += "<i class='fa fa-exclamation-triangle' style='color:red' ></i>";
                strComentario += "<br />Se excede :" + (intNumCursos - intNumCursosVendido) + " curso(s)";

            }

            showCommentSale(strComentario);
            return false;

        }
    } else {
        //es una reservación o ticket, estas se pueden modificar, sin problemas
        return true;
    }
}

function showCommentSale(strComentario) {
    $("#dialogGen").dialog({
        dialogClass: "no-close"
    });
    var strHTML = '<div style="text-align:center; ">';
    strHTML += '<header>INFORMACIÓN IMPORTANTE DE LA VENTA</header>';
    strHTML += '<br />';
    strHTML += '<label>';
    strHTML += strComentario;
    strHTML += '</label>';
    strHTML += '<br />';
    strHTML += '<br />';
    strHTML += '<input type="button" value="¡Enterado!" onclick="closeDescuento()"/>';
    strHTML += '</div>';
    document.getElementById("dialogGen_inside").innerHTML = strHTML;
    $("#dialogGen").dialog("option", "title", "Alerta!");
    $("#dialogGen").dialog("open");
}

//funcion de venta principal
//opc = 3 tipos de venta, 1-vta normal, 2-facturar un tkt, 3-refacturar
function GuardaVta(opc) { //boton de guardar - 3 botones

    //1 - venta general
    //0 - venta cliente
    //3 - venta nueva
    var strOperacion = document.getElementById("CT_GRID").value;

    document.getElementById("SioNO_inside").innerHTML = "¿Desea guardar la venta?.";
    $("#SioNO").dialog("open");

    document.getElementById("btnSI").onclick = function () {
        $("#SioNO").dialog("close");

        //tipo de venta
        if (strOperacion == "1") { //toma la información del grid de historial de ventas general

            var grid = jQuery("#GRDIHVENTA");
            var id = grid.getGridParam("selrow");
            var lstRow_ = grid.getRowData(id);

        } else { //toma la información del grid de historial de ventas de cleinte

            var grid = jQuery("#H_VENTA_CTE");
            var id = grid.getGridParam("selrow");
            var lstRow_ = grid.getRowData(id);

        }
        //tipo de venta

        if (document.getElementById("CEV_FAC0").checked) { //RESERVACIÓN

            console.log("ES RESERVACION");

            if (strOperacion == "0" || strOperacion == "1") { //edicion de vta cliente o vta gral

                console.log("ES RESERVACION/EDICIÓN");
                validaDuplicidad("", "", "", "", opc, "OK"); //edita la venta

            } else {

                console.log("ES RESERVACION/NUEVA");
                saveVtaCofide(); // reservación (nueva)

            }

        } else { //TICKET O FACTURA

            console.log("ES TICKET O FACTURA");
            var strTipoVta = "0";
            var strCorreo = "";
            var strTelefono = "";
            var strRFC = "";
            var UpdateVtaVal = "";
            var infCte = [];//información del cte
            var strIdCte = document.getElementById("CT_ID").value;

            if (document.getElementById("CEV_FAC2").checked) { //factura

                console.log("ES FACTURA");
                strTipoVta = "1";

                if (opc != 1) { //opcion 2 o 3 / edición de venta

                    console.log("ES FACTURA/EDICIÓN");

                    if (document.getElementById("CEV_RFAC1").checked) { //se va a REfacturar

                        console.log("ES FACTURA/EDICIÓN/REFACTURA");

                        if (saveVtaCofideValida()) {

                            console.log("ES FACTURA/EDICIÓN/REFACTURA/VALIDARAZONSOCIAL");
                            validaDuplicidad("", "", "", "", opc, "OK");

                        }

                    } else { //opcion 2 o 3, se va a actualizar a facturar

                        console.log("ES FACTURA/EDICIÓN/EDICIÓN");

                        if (lstRow_.DOC_TIPO == "RESERVACIÓN") {

                            console.log("ES FACTURA/EDICIÓN/EDICIÓN/DESDE RESERVACIÓN");
                            var grid = jQuery("#CEV_GRID");

                            if (grid.getGridParam("selrow") != null) {

                                var lstRow = grid.getRowData(grid.getGridParam("selrow"));
                                strRFC = lstRow.CEV_RFC;
                                strTelefono = lstRow.CEV_TELEFONO;
                                strCorreo = lstRow.CEV_EMAIL1;
                                //VALIDACIÓN DE DATOS
                                if (saveVtaCofideValida()) {

                                    validaDuplicidad(strTipoVta, strRFC, strCorreo, strTelefono, opc, "");
                                }

                            } else {

                                alert("Es necesario elegir una razón social.");

                            }

                        } else {

                            console.log("ES FACTURA/EDICIÓN/EDICIÓN/ACTUALIZA");
//                                    //ACTUALIZA EL TICKET Y FACTURA SIN VALIDACIÓN
                            validaDuplicidad("", "", "", "", opc, "OK");
                        }
                    }

                } else { //opcion 1 - venta nueva - se va a facturar


                }

            } else { //actualizar unicamente la venta ticket

                console.log("ES TICKET");
                strTelefono = aux[0];
                strCorreo = aux[1];
                console.log(lstRow_.DOC_TIPO); //edicion de venta

                if (lstRow_.DOC_TIPO == "RESERVACIÓN") {

                    console.log("ES TICKET/RESERVACIÓN");
                    //consultar la informacion del cliente //VALIDACIÓN DE DATOS                           
                    console.log("#############################cambia la reservación por un ticket o una factura");
                    validaDuplicidad(strTipoVta, "", strCorreo, strTelefono, opc, "");

                } else {

                    if (lstRow_.DOC_TIPO == "TICKET" || lstRow_.DOC_TIPO == "FACTURA") {

                        console.log("ES TICKET/EDICIÓN");
                        //ACTUALIZA EL TICKET Y FACTURA SIN VALIDACIÓN
                        console.log("#############################actualiza un ticket o una factura");
                        validaDuplicidad("", "", "", "", opc, "OK");

                    } else {

//                        console.log("ES TICKET/NUEVO");
//                        validaDuplicidad(strIdCte, strTipoVta, "", strCorreo, strTelefono, opc, "");

                    }

                }

            }

        }

        UpdateVtaVal = "";

    };
    document.getElementById("btnNO").onclick = function () {
        $("#SioNO").dialog("close");
    };
}

/**
 * valida si es factura, que no haya cambios en los detalles de la venta
 */
function validaVtaFac() {

    var NumParticipantesVendidos = document.getElementById("num_participantes").value; //PARTICIPANTES VENDIDOS
    var NumeroCursosVendidos = document.getElementById("num_cursos").value; //CURSOS VENDIDOS

    var intParticipante = document.getElementById("CEV_PARTICIPANTE").value; //PARTICIPANTES ACTUALES
    var intCurso = document.getElementById("CEV_CURSOS").value; //CURSOS ACTUALES

    if (document.getElementById("tipo_doc").value == "FACTURA") {
        //deben de coincidir las cantidades vendidas ya facturadas
        if (NumParticipantesVendidos == intParticipante) {

            if (NumeroCursosVendidos == intCurso) {

                console.log("Todo corresponde con la factura realizada.");
                continuarPaso(3, true);
            } else {

                alert("El numero de cursos facturados no corresponde.");
            }
        } else {

            alert("Los participantes no corresponden con los facturados.");
        }

    } else {
        // no importa si no coincide
        continuarPaso(3, true);
    }
}
/*
 * proceso de venta, segun l origen de la venta GRID
 * 0 - venta desde historial de ventas cliente
 * 1 - venta desde historial de ventas general
 * 3 - venta nueva
 */
function doVenta() {

    var strOperacion = document.getElementById("CT_GRID").value;
    var opc = "1"; //reservación

    document.getElementById("SioNO_inside").innerHTML = "¿Desea guardar la venta?.";
    $("#SioNO").dialog("open");

    document.getElementById("btnSI").onclick = function () {
        $("#SioNO").dialog("close");

        //venta nueva 
        if (strOperacion == "3") {
            /***************SON SOLO RESERVACIONES*****************/
            console.log("ES RESERVACION/NUEVA");
            saveVtaCofide(); // reservación (nueva)

            // edición de ventas
        } else {

            //toma la información del grid de historial de ventas general
            if (strOperacion == "1") {
                opc = "3";
                //toma la información del grid de historial de ventas de cliente
            } else {

                opc = "2";
            }

            /**
             * ACTUALIZA LOS TIPOS DE VENTA CON BASE A LA OPCIÓN, SI ES DE EDICIÓN DESDE EL CLIENTE O DEL HISTORIAL DE VENTAS GENERAL
             * EL 'OK' EVITA LA VALIDACIÓN DE DUPLICIDAD PARA SU GUARDADO DIRECTO
             * AL FINAL SE ACTUALIZA LA INFORMACIÓN DE LA VENTA TICKET O RERSERVACIÓN CON 'UpDateTKT(ID_VENTA);'
             */

            //tipo de documento que se trae desde el grid, documento origen de la venta
            var strTipoDocGrid = document.getElementById("tipo_doc").value;
            var strRFC = document.getElementById("CT_RFC").value; //rfc a validar
            var strTelefono = document.getElementById("CT_CONTACTO").value; //telefono a validar
            var strCorreo = document.getElementById("CT_CORREO").value; //correo a validar
//            var strRFC = ""; //rfc a validar
//            var strTelefono = ""; //telefono a validar
//            var strCorreo = ""; //correo a validar


            //RESERVACIÓN && RESERVACIÓN
            if (document.getElementById("CEV_FAC0").checked && strTipoDocGrid == "RESERVACIÓN") {

                console.log("ES RESERVACION/EDICIÓN DE RESERVACIÓN");
                validaDuplicidad("", "", "", "", opc, "OK"); //EDITA LA RESERVACIÓN

                //EDICIÓN DE VENTAS 
            } else {

                // SE CREA FACTURA O TICKET A PARTIR DE UNA RESERVACIÓN
                console.log("ES TICKET O FACTURA DESDE EDICIÓN");

                // RESERVACIÓN && TICKET
                if (document.getElementById("CEV_FAC1").checked && strTipoDocGrid == "RESERVACIÓN") {
                    //CONVIERTE LA RESERVACIÓN A TICKET
                    //OBTENDRA LA INFORMACIÓN DEL CLIENTE EN LA VALIDACIÓN
                    console.log("ES RESERVACIÓN/EDICIÓN/TICKET");
                    //VALIDA INFORMACIÓN, PARA EL TICKET
                    validaDuplicidad(0, "", "", "", opc, "");

                    //( RESERVACIÓN && FACTURA ) || ( TICKET && FACTURA )
                } else if ((document.getElementById("CEV_FAC2").checked && strTipoDocGrid == "RESERVACIÓN") || (document.getElementById("CEV_FAC2").checked && strTipoDocGrid == "TICKET")) {

                    //CONVIERTE LA RESERVACIÓN A FACTURA || TICKET A FACTURA
                    console.log("ES RESERVACIÓN || TICKET/EDICIÓN/FACTURA");
                    var grid = jQuery("#GRD_RAZONES");

                    if (grid.getGridParam("selrow") != null) {

                        var lstRow = grid.getRowData(grid.getGridParam("selrow"));
                        strRFC = lstRow.CEV_RFC;
                        strTelefono = lstRow.CEV_TELEFONO;
                        strCorreo = lstRow.CEV_EMAIL1;
                        //VALIDACIÓN DE DATOS SELECCIONADOS DE LAS RAZONES SOCIALES
                        if (saveVtaCofideValida()) {

                            //VALIDA LA DUPLICIDAD
                            //OBTENDRA LOS DATOS DEL CLIENTE DESDE LA PANTALLA, LO QUE MANDE EL EJECUTIVO
                            validaDuplicidad(1, strRFC, strCorreo, strTelefono, opc, "");

                        }

                    } else {

                        alert("Es necesario elegir una razón social.");

                    }

                    // ( TICKET && TICKET ) || ( FACTURA && FACTURA )
                } else if ((document.getElementById("CEV_FAC1").checked && strTipoDocGrid == "TICKET") || (document.getElementById("CEV_FAC2").checked && strTipoDocGrid == "FACTURA")) {

                    //REFACTURACIÓN
                    if (document.getElementById("CEV_RFAC1").checked) {
                        var grid = jQuery("#GRD_RAZONES");
                        if (grid.getGridParam("selrow") != null) {

                            var lstRow = grid.getRowData(grid.getGridParam("selrow"));
                            strRFC = lstRow.CEV_RFC;
                            strTelefono = lstRow.CEV_TELEFONO;
                            strCorreo = lstRow.CEV_EMAIL1;
                            //VALIDACIÓN DE DATOS SELECCIONADOS DE LAS RAZONES SOCIALES
                            if (saveVtaCofideValida()) {

                                //VALIDA LA DUPLICIDAD
                                //OBTENDRA LOS DATOS DEL CLIENTE DESDE LA PANTALLA, LO QUE MANDE EL EJECUTIVO
                                console.log("ES FACTUERA A RE-FACTURA");
                                validaDuplicidad(1, strRFC, strCorreo, strTelefono, opc, "OK");

                            }

                        } else {

                            alert("Es necesario elegir una razón social.");

                        }

                    } else {

                        //ACTUALIZA EL TICKET || ACTUALIZA LA FACTURA
                        console.log("ES TICKET || FACTURA/EDICIÓN/TICKET||FACTURA");
                        validaDuplicidad("", "", "", "", opc, "OK");

                    }

                }

            }

        }

    };
    document.getElementById("btnNO").onclick = function () {
        $("#SioNO").dialog("close");
    };
}

/**
 * valida que se haya seleccionado una razón social
 */
function checkRazonSocialSelected() {
    //pasa a la sigueinte pantalla
    if (saveVtaCofideValida()) {
        continuarPaso(4);
    }
}

/*
 * 1 - MUESTRA LOS CAMPOS PARA AGREGAR DATOS FISCALES
 * 2 - GUARDA Y OCULTA LOS DATOS FISCALES
 */
var strOpcionDatosFact = "N";
function addDatosFiscales() {
    //MUESTRA LOS CAMPOS PARA AGREGAR LOS DATOS FISCALES
    console.log("va a entrar a la validación de agregar o guardar datos fiscales");
    if (strOpcionDatosFact == "N") {
        console.log("entro a mostrar campos");
        agregaDatosFact();
    } else {
        // OCULTA LOS CAMPOS Y ALMACENA LOS DATOS FISCALES EN EL GRID
        console.log("guarda información");
        saveDatosFact();
    }
}

/**
 * poner en pago por definir cuando sea el metodo de pago PPD
 * @returns {undefined}
 */
function setPPD() {

    if (document.getElementById("CEV_METODO").value == "PPD") {

        document.getElementById("CEV_DESCRIPCION").value = "99";
        document.getElementById("CEV_DESCRIPCION").disabled = true;
        document.getElementById("CEV_DIGITO").disabled = true;
        document.getElementById("CEV_DIGITO").style.backgroundColor = "#E0F8E6";
        document.getElementById("CEV_DIGITO").value = "";
        document.getElementById("CEV_FECHAPAGO").disabled = true;
        document.getElementById("CEV_FECHAPAGO").style.backgroundColor = "#E0F8E6";
        document.getElementById("CEV_FECHAPAGO").value = "";
        document.getElementById("CEV_FECHAPAGO").placeholder = "__/__/____";

    } else {

        document.getElementById("CEV_DESCRIPCION").value = "0";
        document.getElementById("CEV_DESCRIPCION").disabled = false;
        document.getElementById("CEV_DIGITO").disabled = false;
        document.getElementById("CEV_DIGITO").style.backgroundColor = "#FFF";
        document.getElementById("CEV_FECHAPAGO").disabled = false;
        document.getElementById("CEV_FECHAPAGO").style.backgroundColor = "#FFF";

    }
}


// valida metodo de pago, con su comprobante de pago
function bolValidaFactura() {
    var bolContinuaFac = false;
    // metodo de pago PUE, es necesario llevar comprobante de pago
    if (document.getElementById("CEV_METODO").value == "PUE") {
        if (document.getElementById("CEV_NOM_FILE").value != "") {
            bolContinuaFac = true;
        } else {
            //MP = PUE, debe llevar un comprobante de pago
            alert("Para facturar con el METODO DE PAGO (PUE), \nes necesario adjuntar el COMPROBANTE DE PAGO CORRECTO");
        }
    }
    // metodo de pago PPD, no impora si trae o no trae comprobante de pago
    if (document.getElementById("CEV_METODO").value == "PPD") {
        bolContinuaFac = true;
    }
    return bolContinuaFac;
}

/*
 * Envia por correo
 * el ticket
 */
function SendTicket(strIdFac) {
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
            }
            $("#dialogWait").dialog("close");
        }, error: function (objeto, quepaso, otroobj) {
            alert(":pto=15:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }});
}

function validaparticipante() {
    var grid = jQuery("#GRIDPARTICIPA");
    var arr = grid.getDataIDs();
    var bolResult = false;
    for (var i = 0; i < arr.length; i++) {
        var id = arr[i];
        var lstRow = grid.getRowData(id);
        if (lstRow.CCO_CORREO != "") {
            if (validaCorreoParticipante(lstRow.CCO_CORREO)) {

                alert("El participante: " + lstRow.CCO_NOMBRE + " " + lstRow.CCO_APPATERNO + " no cuenta con un correo valido.");
                bolResult = false;
                break;
            } else {
                bolResult = true;
            }
        } else {
            alert("El participante: " + lstRow.CCO_NOMBRE + " " + lstRow.CCO_APPATERNO + " necesita un correo.");
            bolResult = false;
            break;
        }
    }
    return bolResult;
}

function validaCorreoParticipante(strCorreo) {

    var expr = /^([a-zA-Z0-9_Ññ\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
    if (!expr.test(strCorreo)) {
        return true;
    } else {
        return false;
    }

}

/**
 * si es una promoción, no dejara subir mas de un curso
 * @returns {undefined}
 */
function validaPromoCursos() {
    if (document.getElementById("CT_PROMOSION").value == "1") {
        if (intRowCursos() >= 1) {
            alert("Esto es una promoción, unicamente selecciona un curso.");
        } else {
            continuarPaso(0);
        }
    } else {
        continuarPaso(0);
    }
}