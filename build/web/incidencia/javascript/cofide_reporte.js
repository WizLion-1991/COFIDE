/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function cofide_reporte() {

}

function initReporte() {

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