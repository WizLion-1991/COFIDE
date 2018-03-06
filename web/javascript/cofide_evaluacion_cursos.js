
function cofide_evaluacion_cursos() {

}
function initEvCurso() {
    $('#CC_FECHA_INICIAL').datepicker("disable");
    getDatosCursoEvaluar();
}

function OpnDiagCursosEv() {
    var objSecModiVta = objMap.getScreen("EV_C_NEW");
    if (objSecModiVta != null) {
        objSecModiVta.bolActivo = false;
        objSecModiVta.bolMain = false;
        objSecModiVta.bolInit = false;
        objSecModiVta.idOperAct = 0;
    }
    OpnOpt("EV_C_NEW", "_ed", "dialog", false, false, true);
//    var strIdCurso = document.getElementById("CC_CURSO_ID").value;
//    Abrir_Link("COFIDE_Ev_cursos.jsp?id_curso=" + strIdCurso, "_blank", 1000, 600, 0, 0);
}
function recueprapantalla() { // pinta las preguntas en cada pestaña
    continuarPaso(0); //bloquea pestañas, solo permite la priemra
    var strPost = "id_curso=" + document.getElementById("CC_CURSO_ID").value;
    GetAspectos(strPost);
    GetInstructor();
    GetInstalacion();
    GetMarketing();
}
//evaluaciones
function GetAspectos(strPost) {
    var strScreen = "";
    $.ajax({
        type: "POST",
        data: strPost,
        url: "COFIDE_Asp.jsp",
        dataType: "html",
        scriptCharset: "utf-8",
        cache: false,
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        success: function (datos) {
            strScreen = trim(datos);
            document.getElementById("EV_DIV").innerHTML = strScreen;
        },
        error: function (objeto, quepaso, otroobj) {
            alert(": aspectos :" + objeto + " " + quepaso + " " + otroobj);
        }
    });
}
function GetInstructor() {
    var strScreen = "";
    $.ajax({
        type: "POST",
        data: "",
        url: "COFIDE_Ins.jsp",
        dataType: "html",
        scriptCharset: "utf-8",
        cache: false,
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        success: function (datos) {
            strScreen = trim(datos);
            document.getElementById("EV_DIV_INS").innerHTML = strScreen;
        },
        error: function (objeto, quepaso, otroobj) {
            alert(": instructor :" + objeto + " " + quepaso + " " + otroobj);
        }
    });
}
function GetInstalacion() {
    var strScreen = "";
    $.ajax({
        type: "POST",
        data: "",
        url: "COFIDE_In.jsp",
        dataType: "html",
        scriptCharset: "utf-8",
        cache: false,
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        success: function (datos) {
            strScreen = trim(datos);
            document.getElementById("EV_DIV_IN").innerHTML = strScreen;
        },
        error: function (objeto, quepaso, otroobj) {
            alert(": instalaciones :" + objeto + " " + quepaso + " " + otroobj);
        }
    });
}
function GetMarketing() {
    var strScreen = "";
    $.ajax({
        type: "POST",
        data: "",
        url: "COFIDE_Marketing.jsp",
        dataType: "html",
        scriptCharset: "utf-8",
        cache: false,
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        success: function (datos) {
            strScreen = trim(datos);
            document.getElementById("CEC_MARKETING").innerHTML = strScreen;
            validaMarketing();
        },
        error: function (objeto, quepaso, otroobj) {
            alert(": marketing :" + objeto + " " + quepaso + " " + otroobj);
        }
    });
}
//evaluaciones
function EvCurso() {
    $("#dialogWait").dialog("open");
    validaMarketing();
    var strIdCurso = document.getElementById("CC_CURSO_ID").value;
    var strAsp1 = document.getElementById("CEC_ASP1").value;
    var strAsp2 = document.getElementById("CEC_ASP2").value;
    var strAsp3 = document.getElementById("CEC_ASP3").value;
    var strAsp4 = document.getElementById("CEC_ASP4").value;
    var strPromAsp = document.getElementById("CEC_PROM_ASPECTOS").value;
    var strIdMasterEv = document.getElementById("CEC_ID_EVALUACION").value;

    var strAplAsp1 = 0;
    var strAplAsp2 = 0;
    var strAplAsp3 = 0;
    var strAplAsp4 = 0;

    var strAplIn1 = 0;
    var strAplIn2 = 0;
    var strAplIn3 = 0;
    var strAplIn4 = 0;
    var strAplIn5 = 0;

    if (document.getElementById("CHECK_ASP1").checked) {
        strAplAsp1 = 1;
    }
    if (document.getElementById("CHECK_ASP2").checked) {
        strAplAsp2 = 1;
    }
    if (document.getElementById("CHECK_ASP3").checked) {
        strAplAsp3 = 1;
    }
    if (document.getElementById("CHECK_ASP4").checked) {
        strAplAsp4 = 1;
    }

    if (document.getElementById("CHECK_IN1").checked) {
        strAplIn1 = 1;
    }
    if (document.getElementById("CHECK_IN2").checked) {
        strAplIn2 = 1;
    }
    if (document.getElementById("CHECK_IN3").checked) {
        strAplIn3 = 1;
    }
    if (document.getElementById("CHECK_IN4").checked) {
        strAplIn4 = 1;
    }
    if (document.getElementById("CHECK_IN5").checked) {
        strAplIn5 = 1;
    }

    var strIn1 = document.getElementById("CEC_IN1").value;
    var strIn2 = document.getElementById("CEC_IN2").value;
    var strIn3 = document.getElementById("CEC_IN3").value;
    var strIn4 = document.getElementById("CEC_IN4").value;
    var strIn5 = document.getElementById("CEC_IN5").value;
    var strPromIn = document.getElementById("CEC_PROM_INSTALACION").value;

    var strCurso1 = document.getElementById("CEC_CURSO1").value;
    var strCurso2 = document.getElementById("CEC_CURSO2").value;

    //marketing
    var strNombreParticipante = document.getElementById("CEC_NAME").value;
    var strEmpresa = document.getElementById("CEC_EMPRESA").value;
    var strEmail = document.getElementById("CEC_MAIL").value;
    var strPhone = document.getElementById("CEC_PHONE").value;

    if (strNombreParticipante == "") {
        strNombreParticipante = "";
    }
    if (strEmpresa == "") {
        strEmpresa = "";
    }
    if (strEmail == "") {
        strEmail = "";
    }
    if (strPhone == "") {
        strPhone = "";
    }

    var strMejoras = document.getElementById("CEC_M2").value;

    var strPuntos = "";
    if (document.getElementById("CEC_M_Q31").checked) {
        strPuntos = "1";
    }
    if (document.getElementById("CEC_M_Q32").checked) {
        strPuntos = "0";
    }
    var strPuntos_desc = document.getElementById("CEC_M2").value;
    var strRecomendacion = document.getElementById("CEC_M3").value;

    var strNom = [5];
    var strCorreo = [5];
    var strTelefono = [5];

    strNom[0] = document.getElementById("CEC_M411").value;
    strCorreo[0] = document.getElementById("CEC_M412").value;
    strTelefono[0] = document.getElementById("CEC_M413").value;

    strNom[1] = document.getElementById("CEC_M421").value;
    strCorreo[1] = document.getElementById("CEC_M422").value;
    strTelefono[1] = document.getElementById("CEC_M423").value;

    strNom[2] = document.getElementById("CEC_M431").value;
    strCorreo[2] = document.getElementById("CEC_M432").value;
    strTelefono[2] = document.getElementById("CEC_M433").value;

    strNom[3] = document.getElementById("CEC_M441").value;
    strCorreo[3] = document.getElementById("CEC_M442").value;
    strTelefono[3] = document.getElementById("CEC_M443").value;

    strNom[4] = document.getElementById("CEC_M451").value;
    strCorreo[4] = document.getElementById("CEC_M452").value;
    strTelefono[4] = document.getElementById("CEC_M453").value;


    if (strAsp1 != '') {
        var strPost = "";
        strPost += "CC_CURSO_ID=" + strIdCurso;
        strPost += "&CEC_ASP1=" + strAsp1;
        strPost += "&CEC_ASP2=" + strAsp2;
        strPost += "&CEC_ASP3=" + strAsp3;
        strPost += "&CEC_ASP4=" + strAsp4;
        strPost += "&CEC_PROM_ASPECTOS=" + strPromAsp;
        strPost += "&CEC_IN1=" + strIn1;
        strPost += "&CEC_IN2=" + strIn2;
        strPost += "&CEC_IN3=" + strIn3;
        strPost += "&CEC_IN4=" + strIn4;
        strPost += "&CEC_IN5=" + strIn5;
        strPost += "&CEC_PROM_INSTALACION=" + strPromIn;
        strPost += "&CEC_CURSO1=" + encodeURIComponent(strCurso1);
        strPost += "&CEC_CURSO2=" + encodeURIComponent(strCurso2);
        //Aplica Preguntas 
        strPost += "&CHECK_ASP1=" + strAplAsp1;
        strPost += "&CHECK_ASP2=" + strAplAsp2;
        strPost += "&CHECK_ASP3=" + strAplAsp3;
        strPost += "&CHECK_ASP4=" + strAplAsp4;

        strPost += "&CHECK_IN1=" + strAplIn1;
        strPost += "&CHECK_IN2=" + strAplIn2;
        strPost += "&CHECK_IN3=" + strAplIn3;
        strPost += "&CHECK_IN4=" + strAplIn4;
        strPost += "&CHECK_IN5=" + strAplIn5;
        //marketing
        strPost += "&TMK_MEDIO=" + strMedio;
        strPost += "&TMK_MEDIO_DESC=" + encodeURIComponent(strMedioDesc);
        strPost += "&TMK_PUNTOS=" + strPuntos;
        strPost += "&TMK_PUNTOS_DESC=" + encodeURIComponent(strPuntos_desc);
        strPost += "&TMK_RECOMENDACION=" + encodeURIComponent(strRecomendacion);

        for (var i = 0; i < 5; i++) {

            strPost += "&TMK_NOMBRE" + i + "=" + strNom[i];
            strPost += "&TMK_EMAIL" + i + "=" + strCorreo[i];
            strPost += "&TMK_TELEFONO" + i + "=" + strTelefono[i];

        }

        strPost += "&TMK_NAME=" + encodeURIComponent(strNombreParticipante);
        strPost += "&TMK_EMPRESA=" + encodeURIComponent(strEmpresa)
        strPost += "&TMK_MAIL=" + strEmail;
        strPost += "&TMK_PHONE=" + strPhone;

        if (strIdMasterEv != "") {
            strPost += "&idMasterEv=" + strIdMasterEv;
            $.ajax({
                type: "POST",
                data: encodeURI(strPost),
                scriptCharset: "UTF-8",
                contentType: "application/x-www-form-urlencoded;charset=utf-8",
                cache: false,
                dataType: "html",
                url: "COFIDE_Auto_curso.jsp?ID=3",
                success: function (datos) {
                    if (datos.trim() == "OK") {
                        $("#dialogWait").dialog("close");
                        $("#dialog").dialog("close");
//                        LlenaDetaCalif();
                        getInstructoresCurso();
                        resetCampos();
                        //GUARDAR CURSOS SUGERIDOS
                        saveCursoSugerido(strCurso1);
                        saveCursoSugerido(strCurso2);
                        //GUARDAR CURSOS SUGERIDOS
                    } else {
                        $("#dialogWait").dialog("close");
                        alert(datos);
                    }
                    $("#dialogWait").dialog("close");
                    $("#dialog").dialog("close");
                }
            });
        } else {
            alert("Falta evaluación de instructor.");
        }
    } else {
        alert("Necesitas Evaluar El Curso!");
        $("#dialogWait").dialog("close");
    }
}
function Calif() {
    CalificacionAspectos();
    CalificacionInstitucion();
    CalificacionInstructor();
}
//function Promedio() {
//}
function CalificacionAspectos() {
    //aspectos
    var strAplAsp = 0;
    var dblAsp1 = 0;
    var dblAsp2 = 0;
    var dblAsp3 = 0;
    var dblAsp4 = 0;
    //primer pregunta
    if (document.getElementById("CHECK_ASP1").checked) {
        strAplAsp += 1;
        if (document.getElementById("CEC_ASP_Q11").checked) {
            dblAsp1 = 0;
        }
        if (document.getElementById("CEC_ASP_Q12").checked) {
            dblAsp1 = 2.5;
        }
        if (document.getElementById("CEC_ASP_Q13").checked) {
            dblAsp1 = 5;
        }
        if (document.getElementById("CEC_ASP_Q14").checked) {
            dblAsp1 = 8;
        }
        if (document.getElementById("CEC_ASP_Q15").checked) {
            dblAsp1 = 10;
        }
        document.getElementById("CEC_ASP1").value = dblAsp1;
    } else {
        document.getElementById("CEC_ASP1").value = dblAsp1;
    }
    //pregunta 2
    if (document.getElementById("CHECK_ASP2").checked) {
        strAplAsp += 1;
        if (document.getElementById("CEC_ASP_Q21").checked) {
            dblAsp2 = 0;
        }
        if (document.getElementById("CEC_ASP_Q22").checked) {
            dblAsp2 = 2.5;
        }
        if (document.getElementById("CEC_ASP_Q23").checked) {
            dblAsp2 = 5;
        }
        if (document.getElementById("CEC_ASP_Q24").checked) {
            dblAsp2 = 8;
        }
        if (document.getElementById("CEC_ASP_Q25").checked) {
            dblAsp2 = 10;
        }
        document.getElementById("CEC_ASP2").value = dblAsp2;
    } else {
        document.getElementById("CEC_ASP2").value = dblAsp2;
    }
    //pregunta 3
    if (document.getElementById("CHECK_ASP3").checked) {
        strAplAsp += 1;
        if (document.getElementById("CEC_ASP_Q31").checked) {
            dblAsp3 = 0;
        }
        if (document.getElementById("CEC_ASP_Q32").checked) {
            dblAsp3 = 2.5;
        }
        if (document.getElementById("CEC_ASP_Q33").checked) {
            dblAsp3 = 5;
        }
        if (document.getElementById("CEC_ASP_Q34").checked) {
            dblAsp3 = 8;
        }
        if (document.getElementById("CEC_ASP_Q35").checked) {
            dblAsp3 = 10;
        }
        document.getElementById("CEC_ASP3").value = dblAsp3;
    } else {
        document.getElementById("CEC_ASP3").value = dblAsp3;
    }
    //pregunta 4
    if (document.getElementById("CHECK_ASP4").checked) {
        strAplAsp += 1;
        if (document.getElementById("CEC_ASP_Q41").checked) {
            dblAsp4 = 0;
        }
        if (document.getElementById("CEC_ASP_Q42").checked) {
            dblAsp4 = 2.5;
        }
        if (document.getElementById("CEC_ASP_Q43").checked) {
            dblAsp4 = 5;
        }
        if (document.getElementById("CEC_ASP_Q44").checked) {
            dblAsp4 = 8;
        }
        if (document.getElementById("CEC_ASP_Q45").checked) {
            dblAsp4 = 10;
        }
        document.getElementById("CEC_ASP4").value = dblAsp4;
    } else {
        document.getElementById("CEC_ASP4").value = dblAsp4;
    }
    var dblPromAsp = (dblAsp1 + dblAsp2 + dblAsp3 + dblAsp4);
    if (dblPromAsp != 0) { // si es diferente a cero se hace la operación, si no, se pasa el cero
        dblPromAsp = dblPromAsp / strAplAsp;
    }
    document.getElementById("CEC_PROM_ASPECTOS").value = dblPromAsp;
}
function CalificacionInstructor() {
    //instructor
    var dblInst1 = 0;
    var dblInst2 = 0;
    var dblInst3 = 0;
    var dblInst4 = 0;
    var dblInst5 = 0;
    var strAplIns = 0;
    if (document.getElementById("EI_CHK_Q1").checked) {
        strAplIns += 1;
        if (document.getElementById("CEC_INS_Q11").checked) {
            dblInst1 = 0;
        }
        if (document.getElementById("CEC_INS_Q12").checked) {
            dblInst1 = 2.5;
        }
        if (document.getElementById("CEC_INS_Q13").checked) {
            dblInst1 = 5;
        }
        if (document.getElementById("CEC_INS_Q14").checked) {
            dblInst1 = 8;
        }
        if (document.getElementById("CEC_INS_Q15").checked) {
            dblInst1 = 10;
        }
        document.getElementById("CEC_INS1").value = dblInst1;
    } else {
        document.getElementById("CEC_INS1").value = dblInst1;
    }
    if (document.getElementById("EI_CHK_Q2").checked) {
        strAplIns += 1;
        if (document.getElementById("CEC_INS_Q21").checked) {
            dblInst2 = 0;
        }
        if (document.getElementById("CEC_INS_Q22").checked) {
            dblInst2 = 2.5;
        }
        if (document.getElementById("CEC_INS_Q23").checked) {
            dblInst2 = 5;
        }
        if (document.getElementById("CEC_INS_Q24").checked) {
            dblInst2 = 8;
        }
        if (document.getElementById("CEC_INS_Q25").checked) {
            dblInst2 = 10;
        }
        document.getElementById("CEC_INS2").value = dblInst2;
    } else {
        document.getElementById("CEC_INS2").value = dblInst2;
    }
    if (document.getElementById("EI_CHK_Q3").checked) {
        strAplIns += 1;
        if (document.getElementById("CEC_INS_Q31").checked) {
            dblInst3 = 0;
        }
        if (document.getElementById("CEC_INS_Q32").checked) {
            dblInst3 = 2.5;
        }
        if (document.getElementById("CEC_INS_Q33").checked) {
            dblInst3 = 5;
        }
        if (document.getElementById("CEC_INS_Q34").checked) {
            dblInst3 = 8;
        }
        if (document.getElementById("CEC_INS_Q35").checked) {
            dblInst3 = 10;
        }
        document.getElementById("CEC_INS3").value = dblInst3;
    } else {
        document.getElementById("CEC_INS3").value = dblInst3;
    }
    if (document.getElementById("EI_CHK_Q4").checked) {
        strAplIns += 1;
        if (document.getElementById("CEC_INS_Q41").checked) {
            dblInst4 = 0;
        }
        if (document.getElementById("CEC_INS_Q42").checked) {
            dblInst4 = 2.5;
        }
        if (document.getElementById("CEC_INS_Q43").checked) {
            dblInst4 = 5;
        }
        if (document.getElementById("CEC_INS_Q44").checked) {
            dblInst4 = 8;
        }
        if (document.getElementById("CEC_INS_Q45").checked) {
            dblInst4 = 10;
        }
        document.getElementById("CEC_INS4").value = dblInst4;
    } else {
        document.getElementById("CEC_INS4").value = dblInst4;
    }
    if (document.getElementById("EI_CHK_Q5").checked) {
        strAplIns += 1;
        if (document.getElementById("CEC_INS_Q51").checked) {
            dblInst5 = 0;
        }
        if (document.getElementById("CEC_INS_Q52").checked) {
            dblInst5 = 2.5;
        }
        if (document.getElementById("CEC_INS_Q53").checked) {
            dblInst5 = 5;
        }
        if (document.getElementById("CEC_INS_Q54").checked) {
            dblInst5 = 8;
        }
        if (document.getElementById("CEC_INS_Q55").checked) {
            dblInst5 = 10;
        }
        document.getElementById("CEC_INS5").value = dblInst5;
    } else {
        document.getElementById("CEC_INS5").value = dblInst5;
    }
    var dblPromInst = (dblInst1 + dblInst2 + dblInst3 + dblInst4 + dblInst5);
    if (dblPromInst != 0) { // si es diferente a cero, se va a dividir entre el numero de evaluaciones, si no, se va a pasar el cero, para evitar el NaN
        dblPromInst = dblPromInst / strAplIns;
    }
    document.getElementById("CEC_PROM_INSTRUCTOR").value = dblPromInst;
}
function CalificacionInstitucion() {
    //institucion
    var strAplIn = 0;
    var dblIn1 = 0;
    var dblIn2 = 0;
    var dblIn3 = 0;
    var dblIn4 = 0;
    var dblIn5 = 0;
    if (document.getElementById("CHECK_IN1").checked) {
        strAplIn += 1;
        if (document.getElementById("CEC_IN_Q11").checked) {
            dblIn1 = 0;
        }
        if (document.getElementById("CEC_IN_Q12").checked) {
            dblIn1 = 2.5;
        }
        if (document.getElementById("CEC_IN_Q13").checked) {
            dblIn1 = 5;
        }
        if (document.getElementById("CEC_IN_Q14").checked) {
            dblIn1 = 8;
        }
        if (document.getElementById("CEC_IN_Q15").checked) {
            dblIn1 = 10;
        }
        document.getElementById("CEC_IN1").value = dblIn1;
    } else {
        document.getElementById("CEC_IN1").value = dblIn1;
    }
    if (document.getElementById("CHECK_IN2").checked) {
        strAplIn += 1;
        if (document.getElementById("CEC_IN_Q21").checked) {
            dblIn2 = 0;
        }
        if (document.getElementById("CEC_IN_Q22").checked) {
            dblIn2 = 2.5;
        }
        if (document.getElementById("CEC_IN_Q23").checked) {
            dblIn2 = 5;
        }
        if (document.getElementById("CEC_IN_Q24").checked) {
            dblIn2 = 8;
        }
        if (document.getElementById("CEC_IN_Q25").checked) {
            dblIn2 = 10;
        }
        document.getElementById("CEC_IN2").value = dblIn2;
    } else {
        document.getElementById("CEC_IN2").value = dblIn2;
    }
    if (document.getElementById("CHECK_IN3").checked) {
        strAplIn += 1;
        if (document.getElementById("CEC_IN_Q31").checked) {
            dblIn3 = 0;
        }
        if (document.getElementById("CEC_IN_Q32").checked) {
            dblIn3 = 2.5;
        }
        if (document.getElementById("CEC_IN_Q33").checked) {
            dblIn3 = 5;
        }
        if (document.getElementById("CEC_IN_Q34").checked) {
            dblIn3 = 8;
        }
        if (document.getElementById("CEC_IN_Q35").checked) {
            dblIn3 = 10;
        }
        document.getElementById("CEC_IN3").value = dblIn3;
    } else {
        document.getElementById("CEC_IN3").value = dblIn3;
    }
    if (document.getElementById("CHECK_IN4").checked) {
        strAplIn += 1;
        if (document.getElementById("CEC_IN_Q41").checked) {
            dblIn4 = 0;
        }
        if (document.getElementById("CEC_IN_Q42").checked) {
            dblIn4 = 2.5;
        }
        if (document.getElementById("CEC_IN_Q43").checked) {
            dblIn4 = 5;
        }
        if (document.getElementById("CEC_IN_Q44").checked) {
            dblIn4 = 8;
        }
        if (document.getElementById("CEC_IN_Q45").checked) {
            dblIn4 = 10;
        }
        document.getElementById("CEC_IN4").value = dblIn4;
    } else {
        document.getElementById("CEC_IN4").value = dblIn4;
    }
    if (document.getElementById("CHECK_IN5").checked) {
        strAplIn += 1;
        if (document.getElementById("CEC_IN_Q51").checked) {
            dblIn5 = 0;
        }
        if (document.getElementById("CEC_IN_Q52").checked) {
            dblIn5 = 2.5;
        }
        if (document.getElementById("CEC_IN_Q53").checked) {
            dblIn5 = 5;
        }
        if (document.getElementById("CEC_IN_Q54").checked) {
            dblIn5 = 8;
        }
        if (document.getElementById("CEC_IN_Q55").checked) {
            dblIn5 = 10;
        }
        document.getElementById("CEC_IN5").value = dblIn5;
    } else {
        document.getElementById("CEC_IN5").value = dblIn5;
    }
    var dblPromIn = (dblIn1 + dblIn2 + dblIn3 + dblIn4 + dblIn5);
    if (dblPromIn != 0) { // si es diferente a cero, se va a dividir entre el numero de evaluaciones, si no, solo se manda el cero
        dblPromIn = dblPromIn / strAplIn;
    }
    document.getElementById("CEC_PROM_INSTALACION").value = dblPromIn;
}

function LlenaDetaCalif() {
    var itemIdCob = 0;
    var strAsp1 = "";
    var strAsp2 = "";
    var strAsp3 = "";
    var strAsp4 = "";
    var strInst1 = "";
    var strInst2 = "";
    var strInst3 = "";
    var strInst4 = "";
    var strInst5 = "";
    var strIn1 = "";
    var strIn2 = "";
    var strIn3 = "";
    var strIn4 = "";
    var strIn5 = "";
    var strPromAsp = "";
    var strPromIns = "";
    var strPromIn = "";
    var strCurso1 = "";
    var strCurso2 = "";
    var strCEC_ID = "";
    var strID_CURSO = ""; //id del curso
    var strIdEv = ""; //id master
    var strCC_CURSO_ID = document.getElementById("CC_CURSO_ID").value;
    var strPost = "";
    strPost += "CC_CURSO_ID=" + strCC_CURSO_ID;
    $("#dialogWait").dialog("open");
    $.ajax({
        type: "POST",
        data: strPost,
        scriptCharset: 'UTF-8',
        contentType: 'application/x-www-form-urlencoded;charset=utf-8',
        cache: false,
        dataType: 'xml',
        url: "COFIDE_Auto_curso.jsp?ID=4",
        success: function (datos) {
            var lstXml = datos.getElementsByTagName("vta")[0]; //dato padre
            var lstCte = lstXml.getElementsByTagName("datos"); //dato detalle
            jQuery("#GRID_CURSOEVALUAR").clearGridData();
            for (var i = 0; i < lstCte.length; i++) {
                var objcte = lstCte[i];
                //recuperamos datos del jsp
                strAsp1 = objcte.getAttribute("CEC_ASP_Q1");
                strAsp2 = objcte.getAttribute("CEC_ASP_Q2");
                strAsp3 = objcte.getAttribute("CEC_ASP_Q3");
                strAsp4 = objcte.getAttribute("CEC_ASP_Q4");
                strInst1 = objcte.getAttribute("CEC_INS_Q1");
                strInst2 = objcte.getAttribute("CEC_INS_Q2");
                strInst3 = objcte.getAttribute("CEC_INS_Q3");
                strInst4 = objcte.getAttribute("CEC_INS_Q4");
                strInst5 = objcte.getAttribute("CEC_INS_Q5");
                strIn1 = objcte.getAttribute("CEC_IN_Q1");
                strIn2 = objcte.getAttribute("CEC_IN_Q2");
                strIn3 = objcte.getAttribute("CEC_IN_Q3");
                strIn4 = objcte.getAttribute("CEC_IN_Q4");
                strIn5 = objcte.getAttribute("CEC_IN_Q5");
                strPromAsp = objcte.getAttribute("CEC_PROM_ASPECTOS");
                strPromIns = objcte.getAttribute("CEC_PROM_INSTRUCTOR");
                strPromIn = objcte.getAttribute("CEC_PROM_INSTALACION");
                strCurso1 = objcte.getAttribute("CEC_CURSO1");
                strCurso2 = objcte.getAttribute("CEC_CURSO2");
                strCEC_ID = objcte.getAttribute("CEC_ID");
                strID_CURSO = objcte.getAttribute("CC_CURSO_ID");
                strIdEv = objcte.getAttribute("ID_MASTER");
                //llenamos el grid
                var datarow = {
                    LEV_ASP1: strAsp1,
                    LEV_ASP2: strAsp2,
                    LEV_ASP3: strAsp3,
                    LEV_ASP4: strAsp4,
                    LEV_INS1: strInst1,
                    LEV_INS2: strInst2,
                    LEV_INS3: strInst3,
                    LEV_INS4: strInst4,
                    LEV_INS5: strInst5,
                    LEV_IN1: strIn1,
                    LEV_IN2: strIn2,
                    LEV_IN3: strIn3,
                    LEV_IN4: strIn4,
                    LEV_IN5: strIn5,
                    LEV_P1: strPromAsp,
                    LEV_P2: strPromIns,
                    LEV_P3: strPromIn,
                    LEV_CURSOS1: strCurso1,
                    LEV_CURSOS2: strCurso2,
                    CEC_ID: strCEC_ID,
                    ID_EVMASTER: strIdEv,
                    CC_CURSO_ID: strID_CURSO
                }; //fin del grid
                itemIdCob++;
                jQuery("#GRID_CURSOEVALUAR").addRowData(itemIdCob, datarow, "last");
            }
            document.getElementById("LEV_PROM").value = jQuery("#GRID_CURSOEVALUAR").getDataIDs().length
            $("#dialogWait").dialog("close");
        } //fin de la funcion que recupera los datos
    }); //fin del ajax
//    getInstructoresCurso();
} //fin clase
function EditCalif() {
    var strPost = "";
    strPost += "CC_CURSO_ID=" + document.getElementById("CC_CURSO_ID").value;
    strPost += "&LEV_P1=" + document.getElementById("LEV_P1").value;
    strPost += "&LEV_P2=" + document.getElementById("LEV_P2").value;
    strPost += "&LEV_P3=" + document.getElementById("LEV_P3").value;
    strPost += "&LEV_ASP1=" + document.getElementById("LEV_ASP1").value;
    strPost += "&LEV_ASP2=" + document.getElementById("LEV_ASP2").value;
    strPost += "&LEV_ASP3=" + document.getElementById("LEV_ASP3").value;
    strPost += "&LEV_ASP4=" + document.getElementById("LEV_ASP4").value;
    strPost += "&LEV_INS1=" + document.getElementById("LEV_INS1").value;
    strPost += "&LEV_INS2=" + document.getElementById("LEV_INS2").value;
    strPost += "&LEV_INS3=" + document.getElementById("LEV_INS3").value;
    strPost += "&LEV_INS4=" + document.getElementById("LEV_INS4").value;
    strPost += "&LEV_INS5=" + document.getElementById("LEV_INS5").value;
    strPost += "&LEV_IN1=" + document.getElementById("LEV_IN1").value;
    strPost += "&LEV_IN2=" + document.getElementById("LEV_IN2").value;
    strPost += "&LEV_IN3=" + document.getElementById("LEV_IN3").value;
    strPost += "&LEV_IN4=" + document.getElementById("LEV_IN4").value;
    strPost += "&LEV_IN5=" + document.getElementById("LEV_IN5").value;
    strPost += "&LEV_CURSOS1=" + document.getElementById("LEV_CURSOS1").value;
    strPost += "&LEV_CURSOS2=" + document.getElementById("LEV_CURSOS2").value;
    $("#dialogWait").dialog("open");
    //alert(strPost);
    $.ajax({
        type: "POST",
        data: strPost,
        scriptCharset: "UTF-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "COFIDE_Auto_curso.jsp?ID=5",
    });
    setTimeout('$("#dialogWait").dialog("close")', 1000);
    $("#dialog").dialog("close");
}
function OpnDiagGridCalif() {
    var objSecModiVta = objMap.getScreen("_LC_EV");
    if (objSecModiVta != null) {
        objSecModiVta.bolActivo = false;
        objSecModiVta.bolMain = false;
        objSecModiVta.bolInit = false;
        objSecModiVta.idOperAct = 0;
    }
    OpnOpt("GR_LC_EV", "_ed", "dialog", false, false, true);
}
function initEditCal() {
    var grid = jQuery("#GRID_CURSOEVALUAR");
    var idPart = grid.getGridParam("selrow");
    if (idPart != null) {
        OpnDiagGridCalif();
    } else {
        alert("Debe seleccionar una Evaluación");
    }
}

//Elimina una Evaluacion
function delEvaluacion() {

    var grid = jQuery("#GRID_CURSOEVALUAR");
    var idPart = grid.getGridParam("selrow");
    if (idPart != null) {
        if (confirm("Seguro de eliminar la evaluación?")) {
            var lstVal = grid.getRowData(idPart);
            var strID = lstVal.CEC_ID;
            var strIdMaster = lstVal.ID_EVMASTER;
            var strIDCurso = document.getElementById("CC_CURSO_ID").value;
//            var strIDCurso = lstVal.CC_CURSO_ID;
            var strPost = "CEC_ID=" + strID;
            strPost += "&MASTER_ID=" + strIdMaster;
            strPost += "&IDCURSO=" + strIDCurso;
            $.ajax({
                type: "POST",
                data: encodeURI(strPost),
                scriptCharset: "UTF-8",
                contentType: "application/x-www-form-urlencoded;charset=utf-8",
                cache: false,
                dataType: "html",
                url: "COFIDE_Auto_curso.jsp?ID=10",
                success: function (datos) {
                    if (datos.substring(0, 2) == "OK") {
//                    LlenaDetaCalif();
                        getInstructoresCurso();
                        $("#dialogWait").dialog("close");
                    } else {
                        $("#dialogWait").dialog("close");
                        alert(datos);
                    }
                    $("#dialogWait").dialog("close");
                }
            }); //fin del ajax
        }
    } else {
        alert("Debe seleccionar una Evaluación");
    }
}//Fin delEvaluacion

function initGrCalificacion() {
    var grid = jQuery("#GRID_CURSOEVALUAR");
    var idPart = grid.getGridParam("selrow");
    if (idPart != null) {
        for (var i = 0; i < idPart.length; i++) {
            var id1 = idPart[i];
            var lstRow1 = grid.getRowData(id1);
            document.getElementById("LEV_P1").value = lstRow1.LEV_P1;
            document.getElementById("LEV_P2").value = lstRow1.LEV_P2;
            document.getElementById("LEV_P3").value = lstRow1.LEV_P3;
            document.getElementById("LEV_ASP1").value = lstRow1.LEV_ASP1;
            document.getElementById("LEV_ASP2").value = lstRow1.LEV_ASP2;
            document.getElementById("LEV_ASP3").value = lstRow1.LEV_ASP3;
            document.getElementById("LEV_ASP4").value = lstRow1.LEV_ASP4;
            document.getElementById("LEV_INS1").value = lstRow1.LEV_INS1;
            document.getElementById("LEV_INS2").value = lstRow1.LEV_INS2;
            document.getElementById("LEV_INS3").value = lstRow1.LEV_INS3;
            document.getElementById("LEV_INS4").value = lstRow1.LEV_INS4;
            document.getElementById("LEV_INS5").value = lstRow1.LEV_INS5;
            document.getElementById("LEV_IN1").value = lstRow1.LEV_IN1;
            document.getElementById("LEV_IN2").value = lstRow1.LEV_IN2;
            document.getElementById("LEV_IN3").value = lstRow1.LEV_IN3;
            document.getElementById("LEV_IN4").value = lstRow1.LEV_IN4;
            document.getElementById("LEV_IN5").value = lstRow1.LEV_IN5;
            document.getElementById("LEV_CURSOS1").value = lstRow1.LEV_CURSOS1;
            document.getElementById("LEV_CURSOS2").value = lstRow1.LEV_CURSOS2;
        }
    } else {
        alert("Debe seleccionar un producto");
    }
}

//Obtiene los instructores que impartiran dicho curso
function getInstructoresCurso() {
    var strCurso = document.getElementById("CC_CURSO_ID").value;
    $("#dialogWait").dialog("open");
    $.ajax({
        type: "POST",
        data: "strCurso=" + strCurso,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "COFIDE_Auto_curso.jsp?ID=8",
        success: function (datos) {
            jQuery("#GR_CAPACITADOR").clearGridData();
            var lstXml = datos.getElementsByTagName("InstructorImparte")[0];
            var lstCI = lstXml.getElementsByTagName("datos");
            for (var i = 0; i < lstCI.length; i++) {
                var obj = lstCI[i];
                var rowCursos = {
                    CII_CONTADOR: getMaxGridCapacitador("#GR_CAPACITADOR"),
                    CII_ID_INSTR: obj.getAttribute("CII_ID_INSTR"),
                    CC_CURSO_ID: obj.getAttribute("CC_CURSO_ID"),
                    CCI_INSTRUCTOR: obj.getAttribute("CCI_INSTRUCTOR"),
                    CCI_PROMEDIO: obj.getAttribute("CCI_PROMEDIO")
                };
                jQuery("#GR_CAPACITADOR").addRowData(getMaxGridCapacitador("#GR_CAPACITADOR"), rowCursos, "last");
            }
            LlenaDetaCalif();
            $("#dialogWait").dialog("close");
        },
        error: function (objeto, quepaso, otroobj) {
            alert(":ID 9 :" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }
    });
}// FIN getInstructoresCurso

function getMaxGridCapacitador(strNomGr) {
    var intLenght = jQuery(strNomGr).getDataIDs().length + 1;
    return intLenght;
}//Fin getMaxGridFletePedido

//Obtiene los instructores a Evaluar por curso
function getInstructoresEvaluar() {
    var strOptionSelect = "<option value='0'>Seleccione</option>";
    var strHTML = "<table cellpadding=\"4\" cellspacing=\"1\" border=\"0\" >INSTRUCTOR: <br>";
    var strPost = "CURSO_ID=" + document.getElementById("CC_CURSO_ID").value;
    $("#dialogWait").dialog("open");
    var intCuantos = 0; //cuantos instructores llegaron de la consulta
    $.ajax({
        type: "POST",
        data: strPost,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "COFIDE_Auto_curso.jsp?ID=6",
        success: function (datos) {
            var lstXml = datos.getElementsByTagName("InstructorDisponible")[0];
            var lstprecio = lstXml.getElementsByTagName("datos");
            for (var i = 0; i < lstprecio.length; i++) {
                var obj = lstprecio[i];
                intCuantos = obj.getAttribute("cuantos");
                strOptionSelect += "<option value='" + obj.getAttribute("CI_INSTRUCTOR_ID") + "' >" + obj.getAttribute("CI_INSTRUCTOR") + "</option>";
            }
            strHTML += " <select id=\"instructorSelect\" name=\"instructorSelect\"  class=\"outEdit\" onblur=\"QuitaFoco(this)\" onfocus=\"PonFoco(this)\" 0=\"\" > " + strOptionSelect + " < /select>";

            document.getElementById("EV_CUANTOS").value = intCuantos;
            document.getElementById("CEV_INSTRUCTOR_DIV").innerHTML = strHTML;
            $("#dialogWait").dialog("close");
        },
        error: function (objeto, quepaso, otroobj) {
            alert(":pto9:" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }
    });
}//Fin getInstructoresEvaluar()

function saveCalificacionInstructor() {
    var strResValida = validaCalifInstructor();
    if (strResValida == "OK") {
        $("#dialogWait").dialog("open");
        var IdMasterEvaluacion = document.getElementById("CEC_ID_EVALUACION").value; //Id de la evaluacion que ligara las evaluaciones de instructor con la del curso
        var strIns1 = document.getElementById("CEC_INS1").value;
        var strIns2 = document.getElementById("CEC_INS2").value;
        var strIns3 = document.getElementById("CEC_INS3").value;
        var strIns4 = document.getElementById("CEC_INS4").value;
        var strIns5 = document.getElementById("CEC_INS5").value;
        var strIns1Aplica = document.getElementById("EI_CHK_Q1").checked;
        var strIns2Aplica = document.getElementById("EI_CHK_Q2").checked;
        var strIns3Aplica = document.getElementById("EI_CHK_Q3").checked;
        var strIns4Aplica = document.getElementById("EI_CHK_Q4").checked;
        var strIns5Aplica = document.getElementById("EI_CHK_Q5").checked;
        var strPromIns = document.getElementById("CEC_PROM_INSTRUCTOR").value;

        if (strIns1 == "") {
            strIns1 = 0;
        }

        if (strIns2 == "") {
            strIns2 = 0;
        }

        if (strIns3 == "") {
            strIns3 = 0;
        }

        if (strIns4 == "") {
            strIns4 = 0;
        }

        if (strIns5 == "") {
            strIns5 = 0;
        }

        if (strPromIns == "") {
            strPromIns = 0;
        }
        if (strIns1Aplica) {
            strIns1Aplica = 1;
        } else {
            strIns1Aplica = 0;
        }
        if (strIns2Aplica) {
            strIns2Aplica = 1;
        } else {
            strIns2Aplica = 0;
        }
        if (strIns3Aplica) {
            strIns3Aplica = 1;
        } else {
            strIns3Aplica = 0;
        }
        if (strIns4Aplica) {
            strIns4Aplica = 1;
        } else {
            strIns4Aplica = 0;
        }
        if (strIns5Aplica) {
            strIns5Aplica = 1;
        } else {
            strIns5Aplica = 0;
        }
        var strPost = "";
        strPost += "CC_CURSO_ID=" + document.getElementById("CC_CURSO_ID").value;
        strPost += "&CEC_INS1=" + strIns1;
        strPost += "&CEC_INS2=" + strIns2;
        strPost += "&CEC_INS3=" + strIns3;
        strPost += "&CEC_INS4=" + strIns4;
        strPost += "&CEC_INS5=" + strIns5;
        strPost += "&CHK_INS1=" + strIns1Aplica;
        strPost += "&CHK_INS2=" + strIns2Aplica;
        strPost += "&CHK_INS3=" + strIns3Aplica;
        strPost += "&CHK_INS4=" + strIns4Aplica;
        strPost += "&CHK_INS5=" + strIns5Aplica;
        strPost += "&CEC_PROM_INSTRUCTOR=" + strPromIns;
        if (IdMasterEvaluacion != "") {
            strPost += "&CEC_ID_EVALUACION=" + IdMasterEvaluacion; //id evaluacion maestra
        } else {
            strPost += "&CEC_ID_EVALUACION=0"; //no hay id maestro, es evaluacion por primera vez
        }
        strPost += "&CI_INSTRUCTOR_ID=" + document.getElementById("instructorSelect").value;
        $.ajax({
            type: "POST",
            data: encodeURI(strPost),
            scriptCharset: "UTF-8",
            contentType: "application/x-www-form-urlencoded;charset=utf-8",
            cache: false,
            dataType: "html",
            url: "COFIDE_Auto_curso.jsp?ID=7",
            success: function (datos) {
                datos = trim(datos);
                if (datos.substring(0, 2) == "OK") {
                    document.getElementById("CEC_ID_EVALUACION").value = datos.replace("OK", "");
                    getInstructoresEvaluar();
                    //instructores reset
                    document.getElementById("CEC_INS_Q11").checked = true;
                    document.getElementById("CEC_INS_Q21").checked = true;
                    document.getElementById("CEC_INS_Q31").checked = true;
                    document.getElementById("CEC_INS_Q41").checked = true;
                    document.getElementById("CEC_INS_Q51").checked = true;
                    document.getElementById("EI_CHK_Q1").checked = true;
                    document.getElementById("EI_CHK_Q2").checked = true;
                    document.getElementById("EI_CHK_Q3").checked = true;
                    document.getElementById("EI_CHK_Q4").checked = true;
                    document.getElementById("EI_CHK_Q5").checked = true;
                    document.getElementById("CEC_PROM_INSTRUCTOR").value = "0";
                    $("#dialogWait").dialog("close");
                } else {
                    $("#dialogWait").dialog("close");
                    alert(datos);
                }
                $("#dialogWait").dialog("close");
            }
        }); //fin del ajax
    } else {
        alert(strResValida);
    }
}

//Validamos que los datosde la calificacion del isntructor se envien correctamente
function validaCalifInstructor() {
    var strRes = "";
    if (document.getElementById("instructorSelect").value != 0) {
        strRes = "OK";
    } else {
        strRes = "Seleccione un Instructor.";
    }
    return strRes;
}//Fin validaCalifInstructor

//Obtiene los datos del curso
function getDatosCursoEvaluar() {
    var strCurso = document.getElementById("CC_CURSO_ID").value;
    $("#dialogWait").dialog("open");
    $.ajax({
        type: "POST",
        data: "strCurso=" + strCurso,
        scriptCharset: "utf-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "xml",
        url: "COFIDE_Auto_curso.jsp?ID=9",
        success: function (datos) {
            var lstXml = datos.getElementsByTagName("InfoCurso")[0];
            var lstCI = lstXml.getElementsByTagName("datos");
            for (var i = 0; i < lstCI.length; i++) {
                var obj = lstCI[i];
                document.getElementById("CC_INSCRITOS").value = obj.getAttribute("P_ASISTIERON");
                document.getElementById("DescSede").value = obj.getAttribute("SEDEDESC");
            }
            setTimeout(document.getElementById("LEV_PROM").value = jQuery("#GRID_CURSOEVALUAR").getDataIDs().length, 1000);
            getInstructoresCurso();
            $("#dialogWait").dialog("close");
        },
        error: function (objeto, quepaso, otroobj) {
            alert(":ID 9 :" + objeto + " " + quepaso + " " + otroobj);
            $("#dialogWait").dialog("close");
        }
    });
}//Fin getDatosCursoEvaluar
//activar pestañas
function TabsMapEvCofide(lstTabs, bolActivar, strNomTab) {
    var arrTabs = lstTabs.split(",");
    for (var i = 0; i < arrTabs.length; i++) {
        if (bolActivar) {
            $("#tabs" + strNomTab).tabs("enable", parseInt(arrTabs[i]));
        } else {
            $("#tabs" + strNomTab).tabs("disable", parseInt(arrTabs[i]));
        }
    }
}
/**Avanza al siguiente paso*/
function continuarPaso(numPaso) {
    if (numPaso == 0) { //primera activa
        TabsMapEvCofide("0", true, "EV_C_NEW");
        $("#tabsEV_C_NEW").tabs("option", "active", 0);
        TabsMapEvCofide("1,2,3,4,5", false, "EV_C_NEW");
    }
    if (numPaso == 1) { //primera activa
        if (validaDatosGranl()) {
            TabsMapEvCofide("1", true, "EV_C_NEW");
            $("#tabsEV_C_NEW").tabs("option", "active", 1);
            TabsMapEvCofide("0,2,3,4,5", false, "EV_C_NEW");
        } else {
            alert("Es necesario capturar por lo menos un campo.");
        }
    }
    if (numPaso == 2) { //segunda activa
        if (validaAsp()) {
            TabsMapEvCofide("2", true, "EV_C_NEW");
            $("#tabsEV_C_NEW").tabs("option", "active", 2);
            TabsMapEvCofide("0,1,3,4,5", false, "EV_C_NEW");
            getInstructoresEvaluar(); //llena al instructor
        } else {
            alert("Verifica las evaluaciones.");
        }
    }
    if (numPaso == 3) { //tercera activa
        if (document.getElementById("EV_CUANTOS").value == "0") { //para permitir pasar a la siguiente ventana
            TabsMapEvCofide("3", true, "EV_C_NEW");
            $("#tabsEV_C_NEW").tabs("option", "active", 3);
            TabsMapEvCofide("0,1,2,4,5", false, "EV_C_NEW");
            //validar N instructores, hasta terminar, dejar pasar
        } else {
            if (ValidaIns()) {
                saveCalificacionInstructor();
            } else {
                alert("Verifica las evaluaciones.");
            }
        }
    }
    if (numPaso == 4) { //cuarta activa
        if (validaIn()) {
            TabsMapEvCofide("4", true, "EV_C_NEW");
            $("#tabsEV_C_NEW").tabs("option", "active", 4);
            TabsMapEvCofide("0,1,2,3,5", false, "EV_C_NEW");
        } else {
            alert("Verifica las evaluaciones.");
        }
    }
    if (numPaso == 5) { //cuarta activa

        TabsMapEvCofide("5", true, "EV_C_NEW");
        $("#tabsEV_C_NEW").tabs("option", "active", 5);
        TabsMapEvCofide("0,1,2,3,4", false, "EV_C_NEW");
    }
}
function validaAsp() {
    var chk1 = document.getElementById("CEC_ASP_Q11").checked;
    var chk2 = document.getElementById("CEC_ASP_Q21").checked;
    var chk3 = document.getElementById("CEC_ASP_Q31").checked;
    var chk4 = document.getElementById("CEC_ASP_Q41").checked;
    if (chk1 && chk2 && chk3 && chk4) {
        return false;
        console.log("asigna una evaluacion aspectos: " + false);
    } else {
        return true;
        console.log("se asigno una evaluacion OKS aspectos " + true);
    }
}
function ValidaIns() {
    var chk1 = document.getElementById("CEC_INS_Q11").checked;
    var chk2 = document.getElementById("CEC_INS_Q21").checked;
    var chk3 = document.getElementById("CEC_INS_Q31").checked;
    var chk4 = document.getElementById("CEC_INS_Q41").checked;
    var chk5 = document.getElementById("CEC_INS_Q51").checked;
    if (chk1 && chk2 && chk3 && chk4 && chk5) {
        return false;
        console.log("asigna una evaluacion instructores: " + false);
    } else {
        return true;
        console.log("se asigno una evaluacion OKS instructores " + true);
    }
}
function validaIn() {
    var chk1 = document.getElementById("CEC_IN_Q11").checked;
    var chk2 = document.getElementById("CEC_IN_Q21").checked;
    var chk3 = document.getElementById("CEC_IN_Q31").checked;
    var chk4 = document.getElementById("CEC_IN_Q41").checked;
    var chk5 = document.getElementById("CEC_IN_Q51").checked;
    if (chk1 && chk2 && chk3 && chk4 && chk5) {
        return false;
        console.log("asigna una evaluacion instalaciones : " + false);
    } else {
        return true;
        console.log("se asigno una evaluacion OKS instalaciones " + true);
    }
}
function resetCampos() {
    //aspectos
    document.getElementById("CEC_ASP_Q11").checked = true;
    document.getElementById("CEC_ASP_Q21").checked = true;
    document.getElementById("CEC_ASP_Q31").checked = true;
    document.getElementById("CEC_ASP_Q41").checked = true;
    document.getElementById("CHECK_ASP1").checked = true;
    document.getElementById("CHECK_ASP2").checked = true;
    document.getElementById("CHECK_ASP3").checked = true;
    document.getElementById("CHECK_ASP4").checked = true;

    //instructores
    document.getElementById("CEC_INS_Q11").checked = true;
    document.getElementById("CEC_INS_Q21").checked = true;
    document.getElementById("CEC_INS_Q31").checked = true;
    document.getElementById("CEC_INS_Q41").checked = true;
    document.getElementById("CEC_INS_Q51").checked = true;
    document.getElementById("EI_CHK_Q1").checked = true;
    document.getElementById("EI_CHK_Q2").checked = true;
    document.getElementById("EI_CHK_Q3").checked = true;
    document.getElementById("EI_CHK_Q4").checked = true;
    document.getElementById("EI_CHK_Q5").checked = true;
    //instalaciones
    document.getElementById("CEC_IN_Q11").checked = true;
    document.getElementById("CEC_IN_Q21").checked = true;
    document.getElementById("CEC_IN_Q31").checked = true;
    document.getElementById("CEC_IN_Q41").checked = true;
    document.getElementById("CEC_IN_Q51").checked = true;
    document.getElementById("CHECK_IN1").checked = true;
    document.getElementById("CHECK_IN2").checked = true;
    document.getElementById("CHECK_IN3").checked = true;
    document.getElementById("CHECK_IN4").checked = true;
    document.getElementById("CHECK_IN5").checked = true;
}
/**
 * valida informaicón principal del registro
 */
function validaDatosGranl() {
    var bolValido = false;
    var strNombre = document.getElementById("CEC_NAME");
    var strEmpresa = document.getElementById("CEC_EMPRESA");
    var strCorreo = document.getElementById("CEC_MAIL");
    var strTelefono = document.getElementById("CEC_PHONE");
    if (strNombre.value != "" || strEmpresa.value != "" || strCorreo.value != "" || strTelefono.value != "") {
        bolValido = true;
    }
    return bolValido;
}
/**
 * valida respuesta de telemarketing
 */
var strMedio = "";
var strMedioDesc = "";
function validaMarketing() {

    var chckFB = document.getElementById("CEC_M_Q11");
    var chckPeriodico = document.getElementById("CEC_M_Q12");
    var chckOtro = document.getElementById("CEC_M_Q14");
    var chckEmail = document.getElementById("CEC_M_Q13");
    var chckTelefono = document.getElementById("CEC_M_Q15");

    var txtFB = document.getElementById("CEC_M_FB");
    var txtPeriodico = document.getElementById("CEC_M_PER");
    var txtOtro = document.getElementById("CEC_M_OTRO");

    if (chckFB.checked) {
        txtFB.style.backgroundColor = "#F9F6F6";
        txtFB.readOnly = false;

        txtPeriodico.style.backgroundColor = "#e0f8e6";
        txtPeriodico.value = "";
        txtPeriodico.readOnly = true;

        txtOtro.style.backgroundColor = "#e0f8e6";
        txtOtro.value = "";
        txtOtro.readOnly = true;

        strMedio = "FACEBOOK";
        strMedioDesc = txtFB.value;
    }
    if (chckPeriodico.checked) {
        txtFB.style.backgroundColor = "#e0f8e6";
        txtFB.value = "";
        txtFB.readOnly = true;

        txtPeriodico.style.backgroundColor = "#F9F6F6";
        txtPeriodico.readOnly = false;

        txtOtro.style.backgroundColor = "#e0f8e6";
        txtOtro.value = "";
        txtOtro.readOnly = true;

        strMedio = "PERIODICO";
        strMedioDesc = txtPeriodico.value;
    }
    if (chckOtro.checked) {
        txtFB.style.backgroundColor = "#e0f8e6";
        txtFB.value = "";
        txtFB.readOnly = true;

        txtPeriodico.style.backgroundColor = "#e0f8e6";
        txtPeriodico.value = "";
        txtPeriodico.readOnly = true;

        txtOtro.style.backgroundColor = "#F9F6F6";
        txtOtro.readOnly = false;

        strMedio = "OTRO";
        strMedioDesc = txtOtro.value;
    }
    if (chckEmail.checked || chckTelefono.checked) {
        txtFB.style.backgroundColor = "#e0f8e6";
        txtFB.value = "";
        txtFB.readOnly = true;

        txtPeriodico.style.backgroundColor = "#e0f8e6";
        txtPeriodico.value = "";
        txtPeriodico.readOnly = true;

        txtOtro.style.backgroundColor = "#e0f8e6";
        txtOtro.value = "";
        txtOtro.readOnly = true;

        if (chckEmail.checked) {
            strMedio = "E-MAIL";
            strMedioDesc = "";
        }
        if (chckTelefono.checked) {
            strMedio = "TELEFONO";
            strMedioDesc = "";
        }
    }
}

/**
 * guarda el curso
 * @returns {undefined}
 */
function saveCursoSugerido(strCursoSugerido) {
    var strPost = "";
    if (strCursoSugerido != "") {

        strPost += "&curso=" + encodeURIComponent(strCursoSugerido);
        strPost += "&idCte=0";

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
                $("#dialogWait").dialog("close");
            }, error: function (objeto, quepaso, otroobj) {
                alert(":pto=47:" + objeto + " " + quepaso + " " + otroobj);
                $("#dialogWait").dialog("close");
            }});
    }
}