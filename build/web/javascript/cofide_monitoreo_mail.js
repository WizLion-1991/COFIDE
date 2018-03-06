/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function cofide_monitoreo_mail() {

}

function init() {

}

function exitScreen() {
    myLayout.open("west");
    myLayout.open("east");
    myLayout.open("south");
    myLayout.open("north");
    document.getElementById("MainPanel").innerHTML = "";
    document.getElementById("rightPanel").innerHTML = "";
    var objMainFacPedi = objMap.getScreen("EST_MAIL");
    objMainFacPedi.bolActivo = false;
    objMainFacPedi.bolMain = false;
    objMainFacPedi.bolInit = false;
    objMainFacPedi.idOperAct = 0;
}

//muestra los masivos con base al rango
function showMails() {
    var strPost = "";
    var strFechaIni = "";
    var strFechaFin = "";
    var item = 0;
    strFechaIni = document.getElementById("MAIL_FCH_INI").value;
    strFechaFin = document.getElementById("MAIL_FCH_FIN").value;
    if (strFechaIni != "" && strFechaFin != "") {
        strPost = "fech_ini=" + strFechaIni;
        strPost += "&fech_fin=" + strFechaFin;
        $("#dialogWait").dialog("open");
        jQuery("#MAIL_GRID").clearGridData();
        $.ajax({
            type: "POST",
            data: strPost,
            scriptCharset: "UTF-8",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "xml",
            url: "COFIDE_MonitorMailing.jsp?ID=1",
            success: function (datos) {
                //regresa a los que estan mas de una vez ya en el curso  
                var lstXml = datos.getElementsByTagName("vta")[0];
                var lstCte = lstXml.getElementsByTagName("datos");
                for (var i = 0; i < lstCte.length; i++) {
                    var objcte = lstCte[i];

                    var datarow = {
                        MAILD_ID: objcte.getAttribute("idmasivo"),
                        MAILD_ASUNTO: objcte.getAttribute("asunto"),
                        MAILD_AREA: objcte.getAttribute("area"),
                        MAILD_SEDE: objcte.getAttribute("sede"),
                        MAILD_GIRO: objcte.getAttribute("giro"),
                        MAILD_PLANTILLA: objcte.getAttribute("plantilla"),
                        MAILD_FECHA: objcte.getAttribute("fecha"),
                        MAILD_HORA: objcte.getAttribute("hora"),
                        MAILD_TOTAL: objcte.getAttribute("total"),
                        MAILD_TOTAL_POR: FormatNumber(((parseInt(objcte.getAttribute("total")) / parseInt(objcte.getAttribute("total"))) * 100), 2, true, false, true) + " %",
                        MAILD_ENVIO: objcte.getAttribute("enviado"),
                        MAILD_ENVIO_POR: FormatNumber(((parseInt(objcte.getAttribute("enviado")) / parseInt(objcte.getAttribute("total"))) * 100), 2, true, false, true) + " %",
                        MAILD_ERROR: objcte.getAttribute("error"),
                        MAILD_ERROR_POR: FormatNumber(((parseInt(objcte.getAttribute("error")) / parseInt(objcte.getAttribute("total"))) * 100), 2, true, false, true) + " %",
                        MAILD_READ: objcte.getAttribute("leido"),
                        MAILD_READ_POR: FormatNumber(((parseInt(objcte.getAttribute("leido")) / parseInt(objcte.getAttribute("total"))) * 100), 2, true, false, true) + " %"
                    };
                    item++;
                    jQuery("#MAIL_GRID").addRowData(item, datarow, "last");

                }
                $("#dialogWait").dialog("close");
            }, error: function (objeto, quepaso, otroobj) {
                alert(":rep_masivo=1:" + objeto + " " + quepaso + " " + otroobj);
                $("#dialogWait").dialog("close");
            }});
    } else {
        alert("Seleccione un rango de fechas");
    }
}

//muestra la grafica que se ha seleccionado del masivo
function OpnGraphic(id) {

    var strAsunto = "";
    var strTotal = "";
    var strEnviado = "";
    var strError = "";
    var strLeido = "";

    var strNomMain = objMap.getNomMain();
    var grid = jQuery("#MAIL_GRID"); //grid detalle
    var lstVal = grid.getRowData(id);
    if (strNomMain == "MAIL_GRID") { //pantalla que lo contiene
        OpnEdit(document.getElementById("Ed" + strNomMain));
    } else {
        if (strNomMain == "EST_MAIL") { //pantalla base

            strAsunto = lstVal.MAILD_ASUNTO;
            strAsunto = getHTML(strAsunto);
            console.log(strAsunto);
            strTotal = lstVal.MAILD_TOTAL;
            strEnviado = lstVal.MAILD_ENVIO;
            strError = lstVal.MAILD_ERROR;
            strLeido = lstVal.MAILD_READ;

            strAsunto = strAsunto.replace(" ", "_");
            open("COFIDE_mailing.jsp?total=" + strTotal + "&enviado=" + strEnviado + "&error= " + strError + "&leido=" + strLeido + "&nombre=" + strAsunto, "", "top=50,left=200,width=1000,height=500");

        }
    }
}

function DrawGraphicMailing(intEnviado, IntError) {
    Highcharts.chart('cofide_grafica', {
        chart: {
            plotBackgroundColor: null,
            plotBorderWidth: 0,
            plotShadow: false
        },
        title: {
            text: 'Mailing',
            align: 'center',
            verticalAlign: 'middle',
            y: 40
        },
        tooltip: {
            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
        },
        plotOptions: {
            pie: {
                dataLabels: {
                    enabled: true,
                    distance: -50,
                    style: {
                        fontWeight: 'bold',
                        color: 'white'
                    }
                },
                startAngle: -90,
                endAngle: 90,
                center: ['50%', '75%']
            }
        },
        series: [{
                type: 'pie',
                name: 'Mailing',
                innerSize: '50%',
                data: [
                    ['Enviados', intEnviado],
                    ['Error', IntError],
                    {
                        name: 'Proprietary or Undetectable',
                        y: 0.2,
                        dataLabels: {
                            enabled: false
                        }
                    }
                ], zones: [{
                        value: intEnviado,
                        color: '#99CC00'
                    }, {
                        value: IntError,
                        color: '#99CC00'
                    }, {
                        color: '#FF9700'
                    }
                ]
            }]
    });
}
function DrawGraphicMailing2(intTotalMail, IntLeido) {
    var intSinLeer = (parseInt(intTotalMail) - parseInt(IntLeido))
    Highcharts.chart('cofide_grafica2', {
        chart: {
            plotBackgroundColor: null,
            plotBorderWidth: 0,
            plotShadow: false,
        },
        title: {
            text: 'Estatus',
            align: 'center',
            verticalAlign: 'middle',
            y: 40
        },
        tooltip: {
            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
        },
        plotOptions: {
            pie: {
                dataLabels: {
                    enabled: true,
                    distance: -50,
                    style: {
                        fontWeight: 'bold',
                        color: 'white'
                    }
                },
                startAngle: -90,
                endAngle: 90,
                center: ['50%', '75%']
            }
        },
        series: [{
                type: 'pie',
                name: 'Mailing',
                innerSize: '50%',
                data: [
                    ['Leidos', IntLeido],
                    ['Sin Leer', intSinLeer],
                    {
                        name: 'Proprietary or Undetectable',
                        y: 0.2,
                        dataLabels: {
                            enabled: false
                        }
                    }
                ], zones: [{
                        value: IntLeido,
                        color: '#99CC00'
                    }, {
                        value: intSinLeer,
                        color: '#99CC00'
                    }, {
                        color: '#FF9700'
                    }
                ]
            }]
    });
}
function printGraphic(intTotal, intEnviado, intError, intLeido) {
    DrawGraphicMailing(intEnviado, intError);
    DrawGraphicMailing2(intTotal, intLeido);
}
/**
 * remplaza los acentos por formato html
 * @param {type} strCadena
 * @returns {unresolved}
 */
function getHTML(strCadena) {

    strCadena = strCadena.replace(new RegExp(/\s/g), "_");
    strCadena = strCadena.replace(new RegExp(/[àáâãäå]/g), "a");
    strCadena = strCadena.replace(new RegExp(/[èéêë]/g), "e");
    strCadena = strCadena.replace(new RegExp(/[ìíîï]/g), "i");
    strCadena = strCadena.replace(new RegExp(/[òóôõö]/g), "o");
    strCadena = strCadena.replace(new RegExp(/[ùúûü]/g), "u");

    return strCadena;
}