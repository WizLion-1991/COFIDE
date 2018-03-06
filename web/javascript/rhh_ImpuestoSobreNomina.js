function rhh_ImpuestoSobreNomina() {

}
function Init_rhh_ImpuestoSobreNomina() {
    document.getElementById("btn1").style.display = "none";
}
function ConsultaNominas() {
    if (document.getElementById("ISN_FECHA_INI").value != "" && document.getElementById("ISN_FECHA_FIN").value != "") {
        $("#dialogWait").dialog("open");
        var itemIdCob = 0;
        var strPost = "";
        var intFechaIni = document.getElementById("ISN_FECHA_INI").value;
        var intFechaFin = document.getElementById("ISN_FECHA_FIN").value;
        strPost += "&intFechaIni=" + intFechaIni;
        strPost += "&intFechaFin=" + intFechaFin;
        $.ajax({
            type: "POST", data: strPost, scriptCharset: "utf-8", contentType: "application/x-www-form-urlencoded;charset=utf-8", cache: false, dataType: "xml", url: "ERP_ImpuestoSobreNomina.jsp?id=1", success: function (datos) {
                jQuery("#GRID_NOMINA").clearGridData();
                var objsc = datos.getElementsByTagName("nominas")[0];
                var lstProds = objsc.getElementsByTagName("nominas_deta");
                for (var i = 0; i < lstProds.length; i++) {
                    var obj = lstProds[i];
                    var datarow = {GRD_NOM_DESCRIPCION: obj.getAttribute("strDescripcion"), GRD_NOM_F_INICIAL: obj.getAttribute("strFechaIni"), GRD_NOM_F_FIN: obj.getAttribute("strFechaFin"), GRD_ID_NOMINA: obj.getAttribute("intNominaId")};
                    itemIdCob++;
                    jQuery("#GRID_NOMINA").addRowData(itemIdCob, datarow, "last");
                }
                $("#dialogWait").dialog("close");
            }, error: function () {
                jQuery("#GRID_NOMINA").clearGridData();
                alert("No hay productos con esas caracteristicas");
                $("#dialogWait").dialog("close");
            }});
    } else {
        alert("seleccione el periodo Inicial y Final");
    }
}
function CalculaNominas() {
    $("#dialogWait").dialog("open");
    var itemIdCob = 0;
    var strPost = "";
    var intSucId = document.getElementById("SUC_ID").value;
    var grid1 = jQuery("#GRID_NOMINA");
    var idArr = grid1.getGridParam("selarrrow");
    if (idArr.length == 0) {
        alert("No selecciono Nominas");
    } else {
        for (var i = 0; i < idArr.length; i++) {
            var id = idArr[i];
            var lstRow = grid1.getRowData(id);
            strPost += "&intNominaId" + i + "=" + lstRow.GRD_ID_NOMINA + "";
        }
        strPost += "&intlenghtArr=" + idArr.length;
        strPost += "&intSucId=" + intSucId;
        $.ajax({type: "POST", data: strPost, contentType: "application/x-www-form-urlencoded;charset=utf-8", cache: false, dataType: "xml", url: "ERP_ImpuestoSobreNomina.jsp?id=2", success: function (datos) {
                jQuery("#GRD_PERCEPCIONES").clearGridData();
                var TotalTrabajadores = 0;
                var TotalPercepGen = 0;
                var isnTasa = "";
                var TotalPerc = 0;
                var objsc1 = datos.getElementsByTagName("ImpuestoSobreNomina")[0];
                var lstProds1 = objsc1.getElementsByTagName("PreNomina_Deta");
                for (var i = 0; i < lstProds1.length; i++) {
                    var obj = lstProds1[i];
                    var lstProds = objsc1.getElementsByTagName("PreNomina_Deta");
                    for (var i = 0; i < lstProds.length; i++) {
                        var obj = lstProds[i];
                        TotalTrabajadores = TotalTrabajadores + 1;
                        var consNom = 0;
                        var clave = 0;
                        var centroDeCostos = 0;
                        var nombre = 0;
                        var sdi = 0;
                        var sdn = 0;
                        var diasTrabajados = 0;
                        var domingosTrabajados = 0;
                        var diasDeDescansoTrabajados = 0;
                        var netoApagar = 0;
                        var totalPercepciones = 0;
                        var sueldo = 0;
                        var aguinaldo = 0;
                        var ptu = 0;
                        var reembolsoGastosMedicosDentHosp = 0;
                        var aportacionFondoDeAhorro = 0;
                        var cajaAhorro = 0;
                        var vales = 0;
                        var ayudas = 0;
                        var contribucuionesTrabajadorPagadasPatron = 0;
                        var vacaciones = 0;
                        var primaSeguroVida = 0;
                        var seguroGastosMedicosMayores = 0;
                        var cuotasSindicalesPagadasPatron = 0;
                        var subsidioIncapacidad = 0;
                        var becasTrabajadoresHijos = 0;
                        var otros = 0;
                        var subsidioAlEmpleo = 0;
                        var fomentoPrimerEmpleo = 0;
                        var horasExtras = 0;
                        var primaDominical = 0;
                        var primaVacacional = 0;
                        var primaAntiguedad = 0;
                        var pagoSeparacion = 0;
                        var seguroRetiro = 0;
                        var imdemnizaciones = 0;
                        var reembolsoFuneral = 0;
                        var cuotasSeguridadSocialPagadas = 0;
                        var comisiones = 0;
                        var anticipoCuentaUtilidades = 0;
                        var premioPuntualidad = 0;
                        var premioAsistencia = 0;
                        var bonoyGratificaciones = 0;
                        var diaFestivo = 0;
                        var bonoDeCumplimiento = 0;
                        var retroActivo = 0;
                        var sueldoSuma = 0;
                        var aguinaldoSuma = 0;
                        var ptuSuma = 0;
                        var reembolsoGastosMedicosDentHospSuma = 0;
                        var aportacionFondoDeAhorroSuma = 0;
                        var cajaAhorroSuma = 0;
                        var valesSuma = 0;
                        var ayudasSuma = 0;
                        var contribucuionesTrabajadorPagadasPatronSuma = 0;
                        var vacacionesSuma = 0;
                        var primaSeguroVidaSuma = 0;
                        var seguroGastosMedicosMayoresSuma = 0;
                        var cuotasSindicalesPagadasPatronSuma = 0;
                        var subsidioIncapacidadSuma = 0;
                        var becasTrabajadoresHijosSuma = 0;
                        var otrosSuma = 0;
                        var subsidioAlEmpleoSuma = 0;
                        var fomentoPrimerEmpleoSuma = 0;
                        var horasExtrasSuma = 0;
                        var primaDominicalSuma = 0;
                        var primaVacacionalSuma = 0;
                        var primaAntiguedadSuma = 0;
                        var pagoSeparacionSuma = 0;
                        var seguroRetiroSuma = 0;
                        var imdemnizacionesSuma = 0;
                        var reembolsoFuneralSuma = 0;
                        var cuotasSeguridadSocialPagadasSuma = 0;
                        var comisionesSuma = 0;
                        var anticipoCuentaUtilidadesSuma = 0;
                        var premioPuntualidadSuma = 0;
                        var premioAsistenciaSuma = 0;
                        var bonoyGratificacionesSuma = 0;
                        var diaFestivoSuma = 0;
                        var bonoDeCumplimientoSuma = 0;
                        var retroActivoSuma = 0;
                        isnTasa = obj.getAttribute("isntasa");
                        if (obj.getAttribute("consNom") == "null") {
                            consNom = 0;
                        } else {
                            consNom = obj.getAttribute("consNom");
                        }
                        if (obj.getAttribute("clave") == "null") {
                            clave = 0;
                        } else {
                            clave = obj.getAttribute("clave");
                        }
                        if (obj.getAttribute("centroDeCostos") == "null") {
                            centroDeCostos = 0;
                        } else {
                            centroDeCostos = obj.getAttribute("centroDeCostos");
                        }
                        if (obj.getAttribute("nombre") == "null") {
                            nombre = 0;
                        } else {
                            nombre = obj.getAttribute("nombre");
                        }
                        if (obj.getAttribute("sdi") == "null") {
                            sdi = 0;
                        } else {
                            sdi = obj.getAttribute("sdi");
                        }
                        if (obj.getAttribute("sdn") == "null") {
                            sdn = 0;
                        } else {
                            sdn = obj.getAttribute("sdn");
                        }
                        if (obj.getAttribute("diasTrabajados") == "null") {
                            diasTrabajados = 0;
                        } else {
                            diasTrabajados = obj.getAttribute("diasTrabajados");
                        }
                        if (obj.getAttribute("sueldo") == "null") {
                            sueldo = 0;
                        } else {
                            sueldo = obj.getAttribute("sueldo");
                        }
                        if (obj.getAttribute("retroActivo") == "null") {
                            retroActivo = 0;
                        } else {
                            retroActivo = obj.getAttribute("retroActivo");
                        }
                        if (obj.getAttribute("bonoyGratificaciones") == "null") {
                            bonoyGratificaciones = 0;
                        } else {
                            bonoyGratificaciones = obj.getAttribute("bonoyGratificaciones");
                        }
                        if (obj.getAttribute("diaFestivo") == "null") {
                            diaFestivo = 0;
                        } else {
                            diaFestivo = obj.getAttribute("diaFestivo");
                        }
                        if (obj.getAttribute("bonoDeCumplimiento") == "null") {
                            bonoDeCumplimiento = 0;
                        } else {
                            bonoDeCumplimiento = obj.getAttribute("bonoDeCumplimiento");
                        }
                        if (obj.getAttribute("domingosTrabajados") == "null") {
                            domingosTrabajados = 0;
                        } else {
                            domingosTrabajados = obj.getAttribute("domingosTrabajados");
                        }
                        if (obj.getAttribute("primaDominical") == "null") {
                            primaDominical = 0;
                        } else {
                            primaDominical = obj.getAttribute("primaDominical");
                        }
                        if (obj.getAttribute("aportacionFondoDeAhorro") == "null") {
                            aportacionFondoDeAhorro = 0;
                        } else {
                            aportacionFondoDeAhorro = obj.getAttribute("aportacionFondoDeAhorro");
                        }
                        if (obj.getAttribute("totalPercepciones") == "null") {
                            totalPercepciones = 0;
                        } else {
                            totalPercepciones = obj.getAttribute("totalPercepciones");
                        }
                        if (obj.getAttribute("subsidioAlEmpleo") == "null") {
                            subsidioAlEmpleo = 0;
                        } else {
                            subsidioAlEmpleo = obj.getAttribute("subsidioAlEmpleo");
                        }
                        if (obj.getAttribute("aguinaldo") == "null") {
                            aguinaldo = 0;
                        } else {
                            aguinaldo = obj.getAttribute("aguinaldo");
                        }
                        if (obj.getAttribute("primaVacacional") == "null") {
                            primaVacacional = 0;
                        } else {
                            primaVacacional = obj.getAttribute("primaVacacional");
                        }
                        if (obj.getAttribute("diasDeDescansoTrabajados") == "null") {
                            diasDeDescansoTrabajados = 0;
                        } else {
                            diasDeDescansoTrabajados = obj.getAttribute("diasDeDescansoTrabajados");
                        }
                        if (obj.getAttribute("netoApagar") == "null") {
                            netoApagar = 0;
                        } else {
                            netoApagar = obj.getAttribute("netoApagar");
                        }
                        if (obj.getAttribute("ptu") == "null") {
                            ptu = 0;
                        } else {
                            ptu = obj.getAttribute("ptu");
                        }
                        if (obj.getAttribute("reembolsoGastosMedicosDentHosp") == "null") {
                            reembolsoGastosMedicosDentHosp = 0;
                        } else {
                            reembolsoGastosMedicosDentHosp = obj.getAttribute("reembolsoGastosMedicosDentHosp");
                        }
                        if (obj.getAttribute("cajaAhorro") == "null") {
                            cajaAhorro = 0;
                        } else {
                            cajaAhorro = obj.getAttribute("cajaAhorro");
                        }
                        if (obj.getAttribute("vales") == "null") {
                            vales = 0;
                        } else {
                            vales = obj.getAttribute("vales");
                        }
                        if (obj.getAttribute("ayudas") == "null") {
                            ayudas = 0;
                        } else {
                            ayudas = obj.getAttribute("ayudas");
                        }
                        if (obj.getAttribute("contribucuionesTrabajadorPagadasPatron") == "null") {
                            contribucuionesTrabajadorPagadasPatron = 0;
                        } else {
                            contribucuionesTrabajadorPagadasPatron = obj.getAttribute("contribucuionesTrabajadorPagadasPatron");
                        }
                        if (obj.getAttribute("vacaciones") == "null") {
                            vacaciones = 0;
                        } else {
                            vacaciones = obj.getAttribute("vacaciones");
                        }
                        if (obj.getAttribute("primaSeguroVida") == "null") {
                            primaSeguroVida = 0;
                        } else {
                            primaSeguroVida = obj.getAttribute("primaSeguroVida");
                        }
                        if (obj.getAttribute("seguroGastosMedicosMayores") == "null") {
                            seguroGastosMedicosMayores = 0;
                        } else {
                            seguroGastosMedicosMayores = obj.getAttribute("seguroGastosMedicosMayores");
                        }
                        if (obj.getAttribute("cuotasSindicalesPagadasPatron") == "null") {
                            cuotasSindicalesPagadasPatron = 0;
                        } else {
                            cuotasSindicalesPagadasPatron = obj.getAttribute("cuotasSindicalesPagadasPatron");
                        }
                        if (obj.getAttribute("subsidioIncapacidad") == "null") {
                            subsidioIncapacidad = 0;
                        } else {
                            subsidioIncapacidad = obj.getAttribute("subsidioIncapacidad");
                        }
                        if (obj.getAttribute("becasTrabajadoresHijos") == "null") {
                            becasTrabajadoresHijos = 0;
                        } else {
                            becasTrabajadoresHijos = obj.getAttribute("becasTrabajadoresHijos");
                        }
                        if (obj.getAttribute("otros") == "null") {
                            otros = 0;
                        } else {
                            otros = obj.getAttribute("otros");
                        }
                        if (obj.getAttribute("fomentoPrimerEmpleo") == "null") {
                            fomentoPrimerEmpleo = 0;
                        } else {
                            fomentoPrimerEmpleo = obj.getAttribute("fomentoPrimerEmpleo");
                        }
                        if (obj.getAttribute("horasExtras") == "null") {
                            horasExtras = 0;
                        } else {
                            horasExtras = obj.getAttribute("horasExtras");
                        }
                        if (obj.getAttribute("primaAntiguedad") == "null") {
                            primaAntiguedad = 0;
                        } else {
                            primaAntiguedad = obj.getAttribute("primaAntiguedad");
                        }
                        if (obj.getAttribute("pagoSeparacion") == "null") {
                            pagoSeparacion = 0;
                        } else {
                            pagoSeparacion = obj.getAttribute("pagoSeparacion");
                        }
                        if (obj.getAttribute("seguroRetiro") == "null") {
                            seguroRetiro = 0;
                        } else {
                            seguroRetiro = obj.getAttribute("seguroRetiro");
                        }
                        if (obj.getAttribute("imdemnizaciones") == "null") {
                            imdemnizaciones = 0;
                        } else {
                            imdemnizaciones = obj.getAttribute("imdemnizaciones");
                        }
                        if (obj.getAttribute("reembolsoFuneral") == "null") {
                            reembolsoFuneral = 0;
                        } else {
                            reembolsoFuneral = obj.getAttribute("reembolsoFuneral");
                        }
                        if (obj.getAttribute("cuotasSeguridadSocialPagadas") == "null") {
                            cuotasSeguridadSocialPagadas = 0;
                        } else {
                            cuotasSeguridadSocialPagadas = obj.getAttribute("cuotasSeguridadSocialPagadas");
                        }
                        if (obj.getAttribute("comisiones") == "null") {
                            comisiones = 0;
                        } else {
                            comisiones = obj.getAttribute("comisiones");
                        }
                        if (obj.getAttribute("anticipoCuentaUtilidades") == "null") {
                            anticipoCuentaUtilidades = 0;
                        } else {
                            anticipoCuentaUtilidades = obj.getAttribute("anticipoCuentaUtilidades");
                        }
                        if (obj.getAttribute("premioPuntualidad") == "null") {
                            premioPuntualidad = 0;
                        } else {
                            premioPuntualidad = obj.getAttribute("premioPuntualidad");
                        }
                        if (obj.getAttribute("premioAsistencia") == "null") {
                            premioAsistencia = 0;
                        } else {
                            premioAsistencia = obj.getAttribute("premioAsistencia");
                        }
                        if (obj.getAttribute("sueldoSuma") == "null") {
                            sueldoSuma = 0;
                        } else {
                            sueldoSuma = obj.getAttribute("sueldoSuma");
                        }
                        if (obj.getAttribute("aguinaldoSuma") == "null") {
                            aguinaldoSuma = 0;
                        } else {
                            aguinaldoSuma = obj.getAttribute("aguinaldoSuma");
                        }
                        if (obj.getAttribute("ptuSuma") == "null") {
                            ptuSuma = 0;
                        } else {
                            ptuSuma = obj.getAttribute("ptuSuma");
                        }
                        if (obj.getAttribute("reembolsoGastosMedicosDentHospSuma") == "null") {
                            reembolsoGastosMedicosDentHospSuma = 0;
                        } else {
                            reembolsoGastosMedicosDentHospSuma = obj.getAttribute("reembolsoGastosMedicosDentHospSuma");
                        }
                        if (obj.getAttribute("aportacionFondoDeAhorroSuma") == "null") {
                            aportacionFondoDeAhorroSuma = 0;
                        } else {
                            aportacionFondoDeAhorroSuma = obj.getAttribute("aportacionFondoDeAhorroSuma");
                        }
                        if (obj.getAttribute("cajaAhorroSuma") == "null") {
                            cajaAhorroSuma = 0;
                        } else {
                            cajaAhorroSuma = obj.getAttribute("cajaAhorroSuma");
                        }
                        if (obj.getAttribute("valesSuma") == "null") {
                            valesSuma = 0;
                        } else {
                            valesSuma = obj.getAttribute("valesSuma");
                        }
                        if (obj.getAttribute("ayudasSuma") == "null") {
                            ayudasSuma = 0;
                        } else {
                            ayudasSuma = obj.getAttribute("ayudasSuma");
                        }
                        if (obj.getAttribute("contribucuionesTrabajadorPagadasPatronSuma") == "null") {
                            contribucuionesTrabajadorPagadasPatronSuma = 0;
                        } else {
                            contribucuionesTrabajadorPagadasPatronSuma = obj.getAttribute("contribucuionesTrabajadorPagadasPatronSuma");
                        }
                        if (obj.getAttribute("vacacionesSuma") == "null") {
                            vacacionesSuma = 0;
                        } else {
                            vacacionesSuma = obj.getAttribute("vacacionesSuma");
                        }
                        if (obj.getAttribute("primaSeguroVidaSuma") == "null") {
                            primaSeguroVidaSuma = 0;
                        } else {
                            primaSeguroVidaSuma = obj.getAttribute("primaSeguroVidaSuma");
                        }
                        if (obj.getAttribute("seguroGastosMedicosMayoresSuma") == "null") {
                            seguroGastosMedicosMayoresSuma = 0;
                        } else {
                            seguroGastosMedicosMayoresSuma = obj.getAttribute("seguroGastosMedicosMayoresSuma");
                        }
                        if (obj.getAttribute("cuotasSindicalesPagadasPatronSuma") == "null") {
                            cuotasSindicalesPagadasPatronSuma = 0;
                        } else {
                            cuotasSindicalesPagadasPatronSuma = obj.getAttribute("cuotasSindicalesPagadasPatronSuma");
                        }
                        if (obj.getAttribute("subsidioIncapacidadSuma") == "null") {
                            subsidioIncapacidadSuma = 0;
                        } else {
                            subsidioIncapacidadSuma = obj.getAttribute("subsidioIncapacidadSuma");
                        }
                        if (obj.getAttribute("becasTrabajadoresHijosSuma") == "null") {
                            becasTrabajadoresHijosSuma = 0;
                        } else {
                            becasTrabajadoresHijosSuma = obj.getAttribute("becasTrabajadoresHijosSuma");
                        }
                        if (obj.getAttribute("otrosSuma") == "null") {
                            otrosSuma = 0;
                        } else {
                            otrosSuma = obj.getAttribute("otrosSuma");
                        }
                        if (obj.getAttribute("subsidioAlEmpleoSuma") == "null") {
                            subsidioAlEmpleoSuma = 0;
                        } else {
                            subsidioAlEmpleoSuma = obj.getAttribute("subsidioAlEmpleoSuma");
                        }
                        if (obj.getAttribute("fomentoPrimerEmpleoSuma") == "null") {
                            fomentoPrimerEmpleoSuma = 0;
                        } else {
                            fomentoPrimerEmpleoSuma = obj.getAttribute("fomentoPrimerEmpleoSuma");
                        }
                        if (obj.getAttribute("horasExtrasSuma") == "null") {
                            horasExtrasSuma = 0;
                        } else {
                            horasExtrasSuma = obj.getAttribute("horasExtrasSuma");
                        }
                        if (obj.getAttribute("primaDominicalSuma") == "null") {
                            primaDominicalSuma = 0;
                        } else {
                            primaDominicalSuma = obj.getAttribute("primaDominicalSuma");
                        }
                        if (obj.getAttribute("primaVacacionalSuma") == "null") {
                            primaVacacionalSuma = 0;
                        } else {
                            primaVacacionalSuma = obj.getAttribute("primaVacacionalSuma");
                        }
                        if (obj.getAttribute("primaAntiguedadSuma") == "null") {
                            primaAntiguedadSuma = 0;
                        } else {
                            primaAntiguedadSuma = obj.getAttribute("primaAntiguedadSuma");
                        }
                        if (obj.getAttribute("pagoSeparacionSuma") == "null") {
                            pagoSeparacionSuma = 0;
                        } else {
                            pagoSeparacionSuma = obj.getAttribute("pagoSeparacionSuma");
                        }
                        if (obj.getAttribute("seguroRetiroSuma") == "null") {
                            seguroRetiroSuma = 0;
                        } else {
                            seguroRetiroSuma = obj.getAttribute("seguroRetiroSuma");
                        }
                        if (obj.getAttribute("imdemnizacionesSuma") == "null") {
                            imdemnizacionesSuma = 0;
                        } else {
                            imdemnizacionesSuma = obj.getAttribute("imdemnizacionesSuma");
                        }
                        if (obj.getAttribute("reembolsoFuneralSuma") == "null") {
                            reembolsoFuneralSuma = 0;
                        } else {
                            reembolsoFuneralSuma = obj.getAttribute("reembolsoFuneralSuma");
                        }
                        if (obj.getAttribute("cuotasSeguridadSocialPagadasSuma") == "null") {
                            cuotasSeguridadSocialPagadasSuma = 0;
                        } else {
                            cuotasSeguridadSocialPagadasSuma = obj.getAttribute("cuotasSeguridadSocialPagadasSuma");
                        }
                        if (obj.getAttribute("comisionesSuma") == "null") {
                            comisionesSuma = 0;
                        } else {
                            comisionesSuma = obj.getAttribute("comisionesSuma");
                        }
                        if (obj.getAttribute("anticipoCuentaUtilidadesSuma") == "null") {
                            anticipoCuentaUtilidadesSuma = 0;
                        } else {
                            anticipoCuentaUtilidadesSuma = obj.getAttribute("anticipoCuentaUtilidadesSuma");
                        }
                        if (obj.getAttribute("premioPuntualidadSuma") == "null") {
                            premioPuntualidadSuma = 0;
                        } else {
                            premioPuntualidadSuma = obj.getAttribute("premioPuntualidadSuma");
                        }
                        if (obj.getAttribute("premioAsistenciaSuma") == "null") {
                            premioAsistenciaSuma = 0;
                        } else {
                            premioAsistenciaSuma = obj.getAttribute("premioAsistenciaSuma");
                        }
                        if (obj.getAttribute("bonoyGratificacionesSuma") == "null") {
                            bonoyGratificacionesSuma = 0;
                        } else {
                            bonoyGratificacionesSuma = obj.getAttribute("bonoyGratificacionesSuma");
                        }
                        if (obj.getAttribute("diaFestivoSuma") == "null") {
                            diaFestivoSuma = 0;
                        } else {
                            diaFestivoSuma = obj.getAttribute("diaFestivoSuma");
                        }
                        if (obj.getAttribute("bonoDeCumplimientoSuma") == "null") {
                            bonoDeCumplimientoSuma = 0;
                        } else {
                            bonoDeCumplimientoSuma = obj.getAttribute("bonoDeCumplimientoSuma");
                        }
                        if (obj.getAttribute("retroActivoSuma") == "null") {
                            retroActivoSuma = 0;
                        } else {
                            retroActivoSuma = obj.getAttribute("retroActivoSuma");
                        }
                        TotalPerc = parseFloat(TotalPerc) + parseFloat(sueldoSuma);
                        TotalPerc = parseFloat(TotalPerc) + parseFloat(aguinaldoSuma);
                        TotalPerc = parseFloat(TotalPerc) + parseFloat(ptuSuma);
                        TotalPerc = parseFloat(TotalPerc) + parseFloat(reembolsoGastosMedicosDentHospSuma);
                        TotalPerc = parseFloat(TotalPerc) + parseFloat(aportacionFondoDeAhorroSuma);
                        TotalPerc = parseFloat(TotalPerc) + parseFloat(cajaAhorroSuma);
                        TotalPerc = parseFloat(TotalPerc) + parseFloat(valesSuma);
                        TotalPerc = parseFloat(TotalPerc) + parseFloat(ayudasSuma);
                        TotalPerc = parseFloat(TotalPerc) + parseFloat(contribucuionesTrabajadorPagadasPatronSuma);
                        TotalPerc = parseFloat(TotalPerc) + parseFloat(vacacionesSuma);
                        TotalPerc = parseFloat(TotalPerc) + parseFloat(primaSeguroVidaSuma);
                        TotalPerc = parseFloat(TotalPerc) + parseFloat(seguroGastosMedicosMayoresSuma);
                        TotalPerc = parseFloat(TotalPerc) + parseFloat(cuotasSindicalesPagadasPatronSuma);
                        TotalPerc = parseFloat(TotalPerc) + parseFloat(subsidioIncapacidadSuma);
                        TotalPerc = parseFloat(TotalPerc) + parseFloat(becasTrabajadoresHijosSuma);
                        TotalPerc = parseFloat(TotalPerc) + parseFloat(otrosSuma);
                        TotalPerc = parseFloat(TotalPerc) + parseFloat(subsidioAlEmpleoSuma);
                        TotalPerc = parseFloat(TotalPerc) + parseFloat(fomentoPrimerEmpleoSuma);
                        TotalPerc = parseFloat(TotalPerc) + parseFloat(horasExtrasSuma);
                        TotalPerc = parseFloat(TotalPerc) + parseFloat(primaDominicalSuma);
                        TotalPerc = parseFloat(TotalPerc) + parseFloat(primaVacacionalSuma);
                        TotalPerc = parseFloat(TotalPerc) + parseFloat(primaAntiguedadSuma);
                        TotalPerc = parseFloat(TotalPerc) + parseFloat(pagoSeparacionSuma);
                        TotalPerc = parseFloat(TotalPerc) + parseFloat(seguroRetiroSuma);
                        TotalPerc = parseFloat(TotalPerc) + parseFloat(imdemnizacionesSuma);
                        TotalPerc = parseFloat(TotalPerc) + parseFloat(reembolsoFuneralSuma);
                        TotalPerc = parseFloat(TotalPerc) + parseFloat(cuotasSeguridadSocialPagadasSuma);
                        TotalPerc = parseFloat(TotalPerc) + parseFloat(comisionesSuma);
                        TotalPerc = parseFloat(TotalPerc) + parseFloat(anticipoCuentaUtilidadesSuma);
                        TotalPerc = parseFloat(TotalPerc) + parseFloat(premioPuntualidadSuma);
                        TotalPerc = parseFloat(TotalPerc) + parseFloat(premioAsistenciaSuma);
                        TotalPerc = parseFloat(TotalPerc) + parseFloat(bonoyGratificacionesSuma);
                        TotalPerc = parseFloat(TotalPerc) + parseFloat(diaFestivoSuma);
                        TotalPerc = parseFloat(TotalPerc) + parseFloat(bonoDeCumplimientoSuma);
                        TotalPerc = parseFloat(TotalPerc) + parseFloat(retroActivoSuma);
                        TotalPercepGen = parseFloat(TotalPercepGen) + parseFloat(TotalPerc);
                        var datarow = {GRD_PER_CONSNOM: consNom, GRD_PER_CLAVE: clave, GRD_PER_CENTROC: centroDeCostos, GRD_PER_NOMBRE: nombre, GRD_PER_SDI: sdi, GRD_PER_SDN: sdn, GRD_PER_DIAS_TRAB: diasTrabajados, GRD_PER_DOMINGOS_TRA: domingosTrabajados, GRD_PER_DIAS_DESC: diasDeDescansoTrabajados, GRD_PER_NETO_PAGAR: netoApagar, GRD_PER_SUELDO: sueldo, GRD_PER_AGUINALDO: aguinaldo, GRD_PER_PTU: ptu, GRD_PER_REEM_GAST_MED: reembolsoGastosMedicosDentHosp, GRD_PER_FONDO_AHORRO: aportacionFondoDeAhorro, GRD_PER_CAJA_AHORRO: cajaAhorro, GRD_PER_VALES: vales, GRD_PER_AYUDAS: ayudas, GRD_PER_CARG_TRAB_PAG_PATR: contribucuionesTrabajadorPagadasPatron, GRD_PER_VACACIONES: vacaciones, GRD_PER_PRIMA_SEGURO_VIDA: primaSeguroVida, GRD_PER_SEG_GASTMED_MAY: seguroGastosMedicosMayores, GRD_PER_CUE_SIND_PPPATRON: cuotasSindicalesPagadasPatron, GRD_PER_SUB_INCAPACIDAD: subsidioIncapacidad, GRD_PER_BECA_TRAB_HIJO: becasTrabajadoresHijos, GRD_PER_OTROS: otros, GRD_PER_SUBSIDIO_EMPLEO: subsidioAlEmpleo, GRD_PER_FOM_PRIMER_EMPL: fomentoPrimerEmpleo, GRD_PER_HORAS_EXTRA: horasExtras, GRD_PER_PRIMA_DOM: primaDominical, GRD_PER_PRIMA_VAC: primaVacacional, GRD_PER_PRIMA_ANTIGUEDAD: primaAntiguedad, GRD_PER_PAGO_SEPARACION: pagoSeparacion, GRD_PER_SEG_RETIRO: seguroRetiro, GRD_PER_INDEMINIZACIONES: imdemnizaciones, GRD_PER_REEM_FUNERAL: reembolsoFuneral, GRD_PER_CUO_SEGSOC_PAG: cuotasSeguridadSocialPagadas, GRD_PER_COMISIONES: comisiones, GRD_PER_ANT_CUE_UTI: anticipoCuentaUtilidades, GRD_PER_PREM_PUNT: premioPuntualidad, GRD_PER_PREM_ASIS: premioAsistencia, GRD_PER_BONOYGRA: bonoyGratificaciones, GRD_PER_FESTIVO: diaFestivo, GRD_PER_BONCUMPLIM: bonoDeCumplimiento, GRD_PER_RETROACTIVO: retroActivo, GRD_PER_TOTAL_PERC: TotalPerc};
                        itemIdCob++;
                        jQuery("#GRD_PERCEPCIONES").addRowData(itemIdCob, datarow, "last");
                    }
                }
                document.getElementById("ISN_TOT_PERC").value = FormatNumber(TotalPercepGen, 2, true, false, true, false);
                document.getElementById("ISN_PORC_IMPUESTO").value = isnTasa;
                if (isnTasa == "Tarifa") {
                    isnTasa = 0;
                }
                var PorImpuesto = parseFloat(isnTasa / 100);
                var dblImpuesto = parseFloat(TotalPercepGen) * parseFloat(PorImpuesto);
                document.getElementById("ISN_IMPUESTO").value = FormatNumber(dblImpuesto, 2, true, false, true, false);
                document.getElementById("ISN_NUM_TRABAJ").value = TotalTrabajadores;
                $("#dialogWait").dialog("close");
            }, error: function () {
                jQuery("#GRD_PERCEPCIONES").clearGridData();
                alert("No hay productos con esas caracteristicas");
                $("#dialogWait").dialog("close");
            }});
    }
}