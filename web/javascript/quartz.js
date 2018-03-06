/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var itemQtz = 0;
function quartz() {

}
function initQuartz() {
    agregarItem('MAIL_MASIVO', '');
}

function agregarItem(nombre, estatus) {
    var dataRow1 = {
        QTZ_DESC: nombre,
        QTZ_ACTIVO: estatus
    };
    //Anexamos el registro al GRID
    itemQtz++;
    jQuery("#QUARTZ").addRowData(itemQtz, dataRow1, "last");

}

/**Activa la tarea seleccionada manual*/
function activarTarea() {
    borrarTarea(true);//inicio de la tarea, y si ya esta activa se reinicia
    var grid = jQuery("#QUARTZ");
    if (grid.getGridParam("selrow") != null) {
        var id = grid.getGridParam("selrow");
        var lstRow = grid.getRowData(id);
        var strPost = "nameJob=" + lstRow.QTZ_DESC;
        strPost += "&nameTrigger=trigg_" + lstRow.QTZ_DESC;
        // cron = *(0-59) *(0-23) *(1-31) *(1-12 or Jan-Dec) *(0-6 or Sun-Sat)
        //         Minute   HR      DAY         MONTH               DAY WEEK
//        strPost += "&cron=0 0,3,6,9,12,15,18,21,24,27,30,33,36,39,42,45,48,51,54,57 * * * ?";
        strPost += "&cron=0 0,2,4,6,8,10,12,14,16,18,20,22,24,26,28,30,32,34,36,38,40,42,44,46,48,50,52,54,56,58 * * * ?";
//        strPost += "&cron=0 * * * * ?";
        //peticion por ajax para inicializar un job
        $("#dialogWait").dialog("open");
        $.ajax({
            type: "POST",
            data: strPost,
            scriptCharset: "utf-8",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "html",
            url: "ERP_Quartz.jsp?id=1",
            success: function (datos) {
                //Asignamos valores recuperamos
                if (trim(datos) == "OK") {
                    alert("Tarea inicializada...");
                }
                $("#dialogWait").dialog("close");
            }, error: function (data) {
                $("#dialogWait").dialog("close");
                alert(data);
            }
        });
    }
}

/**Borra la tarea seleccionada*/
function borrarTarea(bolReStart) {
    var grid = jQuery("#QUARTZ");
    if (grid.getGridParam("selrow") != null) {
        var id = grid.getGridParam("selrow");
        var lstRow = grid.getRowData(id);
        var strPost = "nameJob=" + lstRow.QTZ_DESC;
        //peticion por ajax para inicializar un job
        $("#dialogWait").dialog("open");
        $.ajax({
            type: "POST",
            data: strPost,
            scriptCharset: "utf-8",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "html",
            url: "ERP_Quartz.jsp?id=2",
            success: function (datos) {
                //Asignamos valores recuperamos
                if (trim(datos) == "OK") {
                    if (!bolReStart) {
                        alert("Tarea detenida...");
                    }
                }
                $("#dialogWait").dialog("close");
            }, error: function (data) {
                $("#dialogWait").dialog("close");
                alert(data);
            }
        });
    }
}
//
///**
// * activa el quartz en el primer envio masivo
// * @returns {undefined}
// */
//function TurnOnQuartz() {
//    $("#dialogWait").dialog("open");
//    $.ajax({
//        type: "POST",
//        data: strPost,
//        scriptCharset: "utf-8",
//        contentType: "application/x-www-form-urlencoded;charset=utf-8",
//        cache: false,
//        dataType: "html",
//        url: "ERP_Quartz.jsp?id=5",
//        success: function (datos) {
//            datos = trim(datos);
//            if (datos == "OFF") {
//                console.log("Activa el motor");
//                //activa la tarea en automatico
//                activarTareaAuto();
//            } else {
//                if (datos == "ON") {
//                    console.log("Ya se encuentra activo el motor.");
//                } else {
//                    alert(datos);
//                }
//            }
//            $("#dialogWait").dialog("close");
//        }, error: function (data) {
//            $("#dialogWait").dialog("close");
//            alert("hubó un problema con el motor de correos: " + data);
//        }
//    });
//}
//function activarTareaAuto() {
//    borrarTarea(true);//inicio de la tarea, y si ya esta activa se reinicia
//    var strPost = "nameJob=MAIL_MASIVO";
//    strPost += "&nameTrigger=trigg_MAIL_MASIVO";
//    // cron = *(0-59) *(0-23) *(1-31) *(1-12 or Jan-Dec) *(0-6 or Sun-Sat)
//    //         Minute   HR      DAY         MONTH               DAY WEEK
//    strPost += "&cron=10 * * * * ?"; //cada media hora de lunes, miercoles y viernes
//    //peticion por ajax para inicializar un job
//    $("#dialogWait").dialog("open");
//    $.ajax({
//        type: "POST",
//        data: strPost,
//        scriptCharset: "utf-8",
//        contentType: "application/x-www-form-urlencoded;charset=utf-8",
//        cache: false,
//        dataType: "html",
//        url: "ERP_Quartz.jsp?id=1",
//        success: function (datos) {
//            //Asignamos valores recuperamos
//            if (trim(datos) == "OK") {
//                console.log("Tarea inicializada...");
//            }
//            $("#dialogWait").dialog("close");
//        }, error: function (data) {
//            $("#dialogWait").dialog("close");
//            alert(data);
//        }
//    });
//}
