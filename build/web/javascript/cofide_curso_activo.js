function cofide_curso_activo() {

}
function initCActivo() {
//document.getElementById("btn1").parentNode.parentNode.style.display = "none";
    myLayout.close("west");
    myLayout.close("east");
    myLayout.close("south");
    myLayout.close("north");
    loadTodoScreen();
    llenaSelectMes();
}
function loadTodoScreen() { //TODO
    $.ajax({
        url: "COFIDE_curso_activo.jsp",
        dataType: "html",
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        success: function (datos) {
            document.getElementById("CACT_TODO1").innerHTML = datos;
        }
    });
}
function loadMesScreen(strMesNum, strAnio) { //MES && ANIO

    var strPost = "CACT_MES2=" + strMesNum;
    strPost += "&CACT_ANIO2=" + strAnio;
    $.ajax({
        type: "POST",
        data: strPost,
        scriptCharset: "UTF-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "html",
        url: "COFIDE_CursoMes.jsp",
        success: function (datos) {
            document.getElementById("CACT_MES1").innerHTML = datos;
        }});
}

function llenaSelectMes() {
    $.ajax({
        type: "POST",
        data: "",
        scriptCharset: "UTF-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "COFIDE_Cursos.jsp?id=13",
        success: function (datos) {
            var objMesCombo = document.getElementById("CACT_MES2");
            select_clear(objMesCombo);
            var lstXml = datos.getElementsByTagName("meses")[0];
            var lstCte = lstXml.getElementsByTagName("mes");
            for (var i = 0; i < lstCte.length; i++) {
                var objMes = lstCte[i];
                select_add(objMesCombo, objMes.getAttribute("valor1"), objMes.getAttribute("valor2"));
            }
        }
    });
}
function loadSedeScreen(strSede) { //SEDE
    //var strSede = document.getElementById("CACT_SEDE2").value;
    var strPost = "CACT_SEDE2=" + strSede;
    console.log(strPost);
    $.ajax({
        type: "POST",
        data: strPost,
        scriptCharset: "UTF-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "html",
        url: "COFIDE_CursoSede.jsp",
//            url: "COFIDE_curso_sede.jsp",
        success: function (datos) {
            document.getElementById("CACT_SEDE1").innerHTML = datos;
        }});
}
function loadTipoScreen(strTipoCurso) {
    var strPost = "CACT_TIPO2=" + strTipoCurso;
    $.ajax({
        type: "POST",
        data: strPost,
        url: "COFIDE_CursoTipo.jsp",
        dataType: "html",
        scriptCharset: "utf-8",
        cache: false,
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        success: function (datos) {
            document.getElementById("CACT_TIPO1").innerHTML = datos;
        }
    });
}
function llenaSelectTipo() {
    var strMes = document.getElementById("CACT_TIPO2");
    select_clear(strMes);
    select_add(strMes, "PRESENCIAL", " CC_IS_PRESENCIAL = 1");
    select_add(strMes, "ONLINE", " CC_IS_ONLINE = 1");
    select_add(strMes, "VIDEO", " CC_IS_VIDEO = 1");
    loadTipoScreen();
}
function loadDiplomadoScreen() { //diplomados
    $.ajax({
        url: "COFIDE_Curso_diplomado.jsp",
        dataType: "html",
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        success: function (datos) {
            document.getElementById("CACT_DIPLO1").innerHTML = datos;
        }
    });
}
function loadSeminarioScreen() { //seminario
    $.ajax({
        url: "COFIDE_Curso_seminario.jsp",
        dataType: "html",
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        success: function (datos) {
            document.getElementById("CACT_SEMI1").innerHTML = datos;
        }
    });
}
function loadSeminarioDiplomadoScreen() { //seminario
    $.ajax({
        url: "COFIDE_CursoDipSem.jsp",
        dataType: "html",
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        success: function (datos) {
            document.getElementById("CACT_SEMI1").innerHTML = datos;
        }
    });
}
//Controla el flujo de los tabs
function tabShowRepo(event, ui) {
//    var idx = document.getElementById("EMP_ID").value;
    if (ui.newTab.index() == 3) {
        llenaSelectTipo();
    }
    if (ui.newTab.index() == 4) {
        loadSeminarioScreen();
    }
    if (ui.newTab.index() == 5) {
        loadDiplomadoScreen();
    }
}
function BuscarCurso() {
    var strCurso = document.getElementById("Buscar").value;
//    var strPost = strCurso;
    var strPost2 = "CURSO=" + strCurso;
//    $(function () {
//        $("#Buscar").autocomplete({//campo de texto que tendra el autocmplete
//            source: "COFIDE_Telemarketing.jsp?ID=12&" + strPost,
//            minLength: 2
//        });
//    });
    if (strCurso != "") {
        $.ajax({
            type: "POST",
            data: strPost2,
            scriptCharset: "UTF-8",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "html",
            url: "COFIDE_CursoFind.jsp",
            success: function (datos) {
                document.getElementById("CACT_CURSOS").innerHTML = datos;
            }});
    } else {
        alert("Ingresa tu busqueda");
    }
}
//pinta la grafica con los parametros cursos activos
function DrawGraphic(idCurso, intEquipoA, intEquipoB, intEquipoC) {
    $(function () {
// Create the chart
        $('#cofide_grafica' + idCurso).highcharts({
            chart: {type: 'pie'},
            title: {text: 'curso'},
            series: [{
                    name: 'Ventas',
                    data: [
                        {name: 'Equipo A', y: intEquipoA, },
                        {name: 'Equipo B', y: intEquipoB, },
                        {name: 'Equipo C', y: intEquipoC, }
                    ]
                }],
        });
    });
}
//pinta la grafica con los parametros cursos mes
function DrawGraphicMes() {
    $(function () {
        $(document).ready(function () {
// Build the chart
//            $('#cofide_grafica_mes' + idCurso).highcharts({
            $('#cofide_grafica_mes').highcharts({
                chart: {type: 'pie'},
                series: [{
                        data: [{
                                name: 'Vendidos',
//                                y: intVen,
                                y: 40,
                            }, {
                                name: 'DIsponibles',
//                                y: intDisp
                                y: 10
                            }]
                    }]
            });
        });
    });
}
function DrawGraphicSede(idCurso, intVen, intDisp) {
    $(function () {
        $(document).ready(function () {
// Build the chart
            $('#cofide_grafica_sede' + idCurso).highcharts({
                chart: {type: 'pie'},
                series: [{
                        data: [{
                                name: 'Vendidos',
                                y: intVen,
//                                y: 40,
                            }, {
                                name: 'DIsponibles',
                                y: intDisp
//                                y: 10
                            }]
                    }]
            });
        });
    });
}
function DrawGraphicTipo(idCurso, intVen, intDisp) {
    $(function () {
        $(document).ready(function () {
// Build the chart
            $('#cofide_grafica_tipo' + idCurso).highcharts({
                chart: {type: 'pie'},
                series: [{
                        data: [{
                                name: 'Vendidos',
                                y: intVen,
//                                y: 40,
                            }, {
                                name: 'DIsponibles',
                                y: intDisp
//                                y: 10
                            }]
                    }]
            });
        });
    });
}
function OpnDetalleCurso(strIdCurso) {
    open('COFIDE_CursoPopUp.jsp?id=' + strIdCurso, '', 'top=50,left=200,width=1300,height=800');
}
function openRepCurso_P(intIdCurso) {
    Abrir_Link("JasperReport?REP_ID=534&boton_1=XLS&id_curso=" + intIdCurso + "&tipo_curso=1", '_reporte', 500, 600, 0, 0);
//    Abrir_Link("JasperReport?REP_ID=512&boton_1=XLS&ID_CURSO=" + intIdCurso, '_reporte', 500, 600, 0, 0);
}
function openRepCurso_O(intIdCurso) {
    Abrir_Link("JasperReport?REP_ID=534&boton_1=XLS&id_curso=" + intIdCurso + "&tipo_curso=2", '_reporte', 500, 600, 0, 0);
//    Abrir_Link("JasperReport?REP_ID=512&boton_1=XLS&ID_CURSO=" + intIdCurso, '_reporte', 500, 600, 0, 0);
}

////pinta la grafica con los parametros cursos activos
//function DrawGraphicMailing(intTotalMail, intEnviado, IntError, IntLeido) {
//    Highcharts.chart('cofide_grafica', {
//        chart: {
//            plotBackgroundColor: null,
//            plotBorderWidth: 0,
//            plotShadow: false
//        },
//        title: {
//            text: 'Mailing',
//            align: 'center',
//            verticalAlign: 'middle',
//            y: 40
//        },
//        tooltip: {
//            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
//        },
//        plotOptions: {
//            pie: {
//                dataLabels: {
//                    enabled: true,
//                    distance: -50,
//                    style: {
//                        fontWeight: 'bold',
//                        color: 'white'
//                    }
//                },
//                startAngle: -90,
//                endAngle: 90,
//                center: ['50%', '75%']
//            }
//        },
//        series: [{
//                type: 'pie',
//                name: 'Mailing',
//                innerSize: '50%',
//                data: [
//                    ['Enviados', 110.38],
//                    ['Error', 56.33],
//                    {
//                        name: 'Proprietary or Undetectable',
//                        y: 0.2,
//                        dataLabels: {
//                            enabled: false
//                        }
//                    }
//                ]
//            }]
//    });
//}
//function DrawGraphicMailing2(intTotalMail, intEnviado, IntError, IntLeido) {
//
//    Highcharts.chart('cofide_grafica2', {
//        chart: {
//            plotBackgroundColor: null,
//            plotBorderWidth: 0,
//            plotShadow: false
//        },
//        title: {
//            text: 'Mailing Atendidos',
//            align: 'center',
//            verticalAlign: 'middle',
//            y: 40
//        },
//        tooltip: {
//            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
//        },
//        plotOptions: {
//            pie: {
//                dataLabels: {
//                    enabled: true,
//                    distance: -50,
//                    style: {
//                        fontWeight: 'bold',
//                        color: 'white'
//                    }
//                },
//                startAngle: -90,
//                endAngle: 90,
//                center: ['50%', '75%']
//            }
//        },
//        series: [{
//                type: 'pie',
//                name: 'Mailing',
//                innerSize: '50%',
//                data: [
//                    ['Leidos', 10.38],
//                    ['Sin Leer', 56.33],
//                    {
//                        name: 'Proprietary or Undetectable',
//                        y: 0.2,
//                        dataLabels: {
//                            enabled: false
//                        }
//                    }
//                ]
//            }]
//    });
//}