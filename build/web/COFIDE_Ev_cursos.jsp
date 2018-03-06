<%-- 
    Document   : COFIDE_Evakuacion
    Created on : 9/03/2017, 06:30:14 PM
    Author     : Desarrollo_COFIDE
--%>

<%@page import="comSIWeb.ContextoApt.VariableSession"%>
<%@page import="comSIWeb.Operaciones.Conexion"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    VariableSession varSesiones = new VariableSession(request);
    varSesiones.getVars();
    Conexion oConn = new Conexion(varSesiones.getStrUser(), this.getServletContext());
    oConn.open();

    String strIDCurso = "";
    if (request.getParameter("id_curso") != null) {
        if (!request.getParameter("id_curso").isEmpty()) {
            strIDCurso = request.getParameter("id_curso");
        }
    }
    if (!strIDCurso.equals("")) {

%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript" src="javascript/cofide_evaluacion_cursos.js"></script> <!-- importar archivo javascript--> 
        <title>EVALUACIÓN DE CURSOS</title>
        <style>
            .input__row{margin-top: 10px;}
            .radiobtn {display: none;}
            .buttons {margin-left: -40px;}
            .buttons li {display: inline;padding-right:50px}
            .buttons li label{padding-left: 30px;position: relative;left: -25px;}
            .buttons li label:hover {cursor: pointer;}
            .buttons li span {display: inline-block;position: relative;top: 5px;border: 2px solid #FF6500;width: 18px;height: 18px;background: #fff;}
            .radiobtn:checked + span::before{content: '';border: 2px solid #fff;position: absolute;width: 14px;height: 14px;background-color: #FF6500;}
            .colorCampo{background-color: #e0f8e6; color: black;}
        </style>
    </head>
    <body onload="//initEvPreguntas()">
        <div style="width: 1200 px; overflow: scroll; height:700px; padding-left: 20px;" class="form_field">
            <div id='EV_DIV' style="width: 1100px"></div>
            <div><input id='CEC_ID_EVALUACION' type="text" value="" hidden="false"/></div>
            <div><input id='CC_CURSO_ID' type="text" value="<%=strIDCurso%>" hidden="true"/></div>
            <form><div class='input__row'>
                    <div style="text-align:center " class="form_field">ASPECTOS GENERALES</div>
                    <div><table width='80%'><td class="form_field">¿EL CURSO CUMPLIO SUS EXPECTATIVAS?</td><td><input type='text' class='colorCampo' id='CEC_PROM_ASPECTOS' value='' readonly /></td></table></div>
                    <ul class='buttons'>
                        <li><input id='CEC_ASP_Q11' class='radiobtn' name='writter' type='radio' value='0' tabindex='1' checked/><span></span><label for='CEC_ASP_Q11'>1</label></li>
                        <li><input id='CEC_ASP_Q12' class='radiobtn' name='writter' type='radio' value='2.5' tabindex='2'><span></span><label for='CEC_ASP_Q12'>2</label></li>
                        <li><input id='CEC_ASP_Q13' class='radiobtn' name='writter' type='radio' value='5' tabindex='3'><span></span><label for='CEC_ASP_Q13'>3</label></li>
                        <li><input id='CEC_ASP_Q14' class='radiobtn' name='writter' type='radio' value='8' tabindex='3'><span></span><label for='CEC_ASP_Q14'>4</label></li>
                        <li><input id='CEC_ASP_Q15' class='radiobtn' name='writter' type='radio' value='10' tabindex='3'><span></span><label for='CEC_ASP_Q15'>5</label></li>
                        <li><input  class="colorCampo" id='CEC_ASP1' class='' type='text' value='' readonly></li>
                        <input type="checkbox" id="CHECK_ASP1" value="1" checked/><label>APLICA</label>
                    </ul></div></form>
            <!-- segunda pregunta -->
            <form><div class='input__row'>
                    <div class="form_field">¿COMO CALIFICA LA ATENCION TELEFONICA RECIBIDA POR SU EJECUTIVO?</div>
                    <ul class='buttons'>
                        <li><input id='CEC_ASP_Q21' class='radiobtn' name='writter' type='radio' value='poe' tabindex='1' checked/><span></span><label for='CEC_ASP_Q21'>1</label></li>
                        <li><input id='CEC_ASP_Q22' class='radiobtn' name='writter' type='radio' value='lovecraft' tabindex='2'><span></span><label for='CEC_ASP_Q22'>2</label></li>
                        <li><input id='CEC_ASP_Q23' class='radiobtn' name='writter' type='radio' value='becquer' tabindex='3'><span></span><label for='CEC_ASP_Q23'>3</label></li>
                        <li><input id='CEC_ASP_Q24' class='radiobtn' name='writter' type='radio' value='becquer' tabindex='3'><span></span><label for='CEC_ASP_Q24'>4</label></li>
                        <li><input id='CEC_ASP_Q25' class='radiobtn' name='writter' type='radio' value='becquer' tabindex='3'><span></span><label for='CEC_ASP_Q25'>5</label></li>
                        <li><input class="colorCampo" id='CEC_ASP2' class='' type='text' value='' readonly></li>
                        <input type="checkbox" id="CHECK_ASP2" value="1" checked/><label>APLICA</label>
                    </ul></div></form>
            <!-- tercer pregunta -->
            <form><div class='input__row'>
                    <div class="form_field">¿COMO CALIFICA LA ATENCION PRESTADA POR NUESTRAS EDECANTES?</div> 
                    <ul class='buttons'>
                        <li><input id='CEC_ASP_Q31' class='radiobtn' name='writter' type='radio' value='poe' tabindex='1' checked/><span></span><label for='CEC_ASP_Q31'>1</label></li>
                        <li><input id='CEC_ASP_Q32' class='radiobtn' name='writter' type='radio' value='lovecraft' tabindex='2'><span></span><label for='CEC_ASP_Q32'>2</label></li>
                        <li><input id='CEC_ASP_Q33' class='radiobtn' name='writter' type='radio' value='becquer' tabindex='3'><span></span><label for='CEC_ASP_Q33'>3</label></li>
                        <li><input id='CEC_ASP_Q34' class='radiobtn' name='writter' type='radio' value='becquer' tabindex='3'><span></span><label for='CEC_ASP_Q34'>4</label></li>
                        <li><input id='CEC_ASP_Q35' class='radiobtn' name='writter' type='radio' value='becquer' tabindex='3'><span></span><label for='CEC_ASP_Q35'>5</label></li>
                        <li><input class="colorCampo"  id='CEC_ASP3' class='' type='text' value='' readonly></li>
                        <input type="checkbox" id="CHECK_ASP3" value="1" checked/><label>APLICA</label>
                    </ul></div></form>
            <!-- cuarta pregunta -->
            <form><div class='input__row'>
                    <div class="form_field">¿LA DURACION DEL CURSO LE PARECIO ADECUADA?</div> 
                    <ul class='buttons'>
                        <li><input id='CEC_ASP_Q41' class='radiobtn' name='writter' type='radio' value='poe' tabindex='1' checked/><span></span><label for='CEC_ASP_Q41'>1</label></li>
                        <li><input id='CEC_ASP_Q42' class='radiobtn' name='writter' type='radio' value='lovecraft' tabindex='2'><span></span><label for='CEC_ASP_Q42'>2</label></li>
                        <li><input id='CEC_ASP_Q43' class='radiobtn' name='writter' type='radio' value='becquer' tabindex='3'><span></span><label for='CEC_ASP_Q43'>3</label></li>
                        <li><input id='CEC_ASP_Q44' class='radiobtn' name='writter' type='radio' value='becquer' tabindex='3'><span></span><label for='CEC_ASP_Q44'>4</label></li>
                        <li><input id='CEC_ASP_Q45' class='radiobtn' name='writter' type='radio' value='becquer' tabindex='3'><span></span><label for='CEC_ASP_Q45'>5</label></li>
                        <li><input class="colorCampo"  id='CEC_ASP4' class='' type='text' value='' readonly></li>
                        <input type="checkbox" id="CHECK_ASP4" value="1" checked/><label>APLICA</label>
                    </ul></div></form>

            <!-- 								Instructores 									-->
            <!-- primera pregunta -->
            <hr/>
            <form><div class='input__row'>
                    <div style="text-align:center">INSTRUCTORES </div>
                    <div><table width='73%'><td class="form_field">¿EL INSTRUCTOR CUMPLIO CON LOS OBJETIVOS DEL CURSO??</td><td><input type='text' class='colorCampo' id='CEC_PROM_ASPECTOS' value='' readonly /></td></table></div>
                    <ul class='buttons'>
                        <li><input id='CEC_INS_Q11' class='radiobtn' name='writter' type='radio' value='0' tabindex='1' checked/><span></span><label for='CEC_INS_Q11'>1</label></li>
                        <li><input id='CEC_INS_Q12' class='radiobtn' name='writter' type='radio' value='2.5' tabindex='2'><span></span><label for='CEC_INS_Q12'>2</label></li>
                        <li><input id='CEC_INS_Q13' class='radiobtn' name='writter' type='radio' value='5' tabindex='3'><span></span><label for='CEC_INS_Q13'>3</label></li>
                        <li><input id='CEC_INS_Q14' class='radiobtn' name='writter' type='radio' value='8' tabindex='3'><span></span><label for='CEC_INS_Q14'>4</label></li>
                        <li><input id='CEC_INS_Q15' class='radiobtn' name='writter' type='radio' value='10' tabindex='3'><span></span><label for='CEC_INS_Q15'>5</label></li>
                        <li><input class="colorCampo"  id='CEC_INS1' class='' type='text' value='' readonly></li>
                        <input type="checkbox" id="EI_CHK_Q1" value="1" checked/><label>APLICA</label>
                    </ul></div></form>
            <!-- segunda pregunta -->
            <form><div class='input__row'>
                    <div class="form_field">¿COMO CONSIDERA EL NIVEL DE CONOCMINENTOS DEL INSTRUCTOR?</div>
                    <ul class='buttons'>
                        <li><input id='CEC_INS_Q21' class='radiobtn' name='writter' type='radio' value='poe' tabindex='1' checked/><span></span><label for='CEC_INS_Q21'>1</label></li>
                        <li><input id='CEC_INS_Q22' class='radiobtn' name='writter' type='radio' value='lovecraft' tabindex='2'><span></span><label for='CEC_INS_Q22'>2</label></li>
                        <li><input id='CEC_INS_Q23' class='radiobtn' name='writter' type='radio' value='becquer' tabindex='3'><span></span><label for='CEC_INS_Q23'>3</label></li>
                        <li><input id='CEC_INS_Q24' class='radiobtn' name='writter' type='radio' value='becquer' tabindex='3'><span></span><label for='CEC_INS_Q24'>4</label></li>
                        <li><input id='CEC_INS_Q25' class='radiobtn' name='writter' type='radio' value='becquer' tabindex='3'><span></span><label for='CEC_INS_Q25'>5</label></li>
                        <li><input class="colorCampo"  id='CEC_INS2' class='' type='text' value='' readonly></li>
                        <input type="checkbox" id="EI_CHK_Q2" value="1" checked/><label>APLICA</label>
                    </ul></div></form>
            <!-- tercer pregunta -->
            <form><div class='input__row'>
                    <div class="form_field">¿EL INSTRUCTOR RESPONDIO A TODAS SUS PREGUNTAS Y DUDAS?</div> 
                    <ul class='buttons'>
                        <li><input id='CEC_INS_Q31' class='radiobtn' name='writter' type='radio' value='poe' tabindex='1' checked/><span></span><label for='CEC_INS_Q31'>1</label></li>
                        <li><input id='CEC_INS_Q32' class='radiobtn' name='writter' type='radio' value='lovecraft' tabindex='2'><span></span><label for='CEC_INS_Q32'>2</label></li>
                        <li><input id='CEC_INS_Q33' class='radiobtn' name='writter' type='radio' value='becquer' tabindex='3'><span></span><label for='CEC_INS_Q33'>3</label></li>
                        <li><input id='CEC_INS_Q34' class='radiobtn' name='writter' type='radio' value='becquer' tabindex='3'><span></span><label for='CEC_INS_Q34'>4</label></li>
                        <li><input id='CEC_INS_Q35' class='radiobtn' name='writter' type='radio' value='becquer' tabindex='3'><span></span><label for='CEC_INS_Q35'>5</label></li>
                        <li><input class="colorCampo" id='CEC_INS3' class='' type='text' value='' readonly></li>
                        <input type="checkbox" id="EI_CHK_Q3" value="1" checked/><label>APLICA</label>
                    </ul></div></form>
            <!-- cuarta pregunta -->
            <form><div class='input__row'>
                    <div class="form_field">¿RESULTO CLARO EL LENGUAJE QUE UTILIZO EL INSTRUCTOR?</div> 
                    <ul class='buttons'>
                        <li><input id='CEC_INS_Q41' class='radiobtn' name='writter' type='radio' value='poe' tabindex='1' checked/><span></span><label for='CEC_INS_Q41'>1</label></li>
                        <li><input id='CEC_INS_Q42' class='radiobtn' name='writter' type='radio' value='lovecraft' tabindex='2'><span></span><label for='CEC_INS_Q42'>2</label></li>
                        <li><input id='CEC_INS_Q43' class='radiobtn' name='writter' type='radio' value='becquer' tabindex='3'><span></span><label for='CEC_INS_Q43'>3</label></li>
                        <li><input id='CEC_INS_Q44' class='radiobtn' name='writter' type='radio' value='becquer' tabindex='3'><span></span><label for='CEC_INS_Q44'>4</label></li>
                        <li><input id='CEC_INS_Q45' class='radiobtn' name='writter' type='radio' value='becquer' tabindex='3'><span></span><label for='CEC_INS_Q45'>5</label></li>
                        <li><input class="colorCampo"  id='CEC_INS4' class='' type='text' value='' readonly></li>
                        <input type="checkbox" id="EI_CHK_Q4" value="1" checked/><label>APLICA</label>
                    </ul></div></form>
            <!-- quinta pregunta -->
            <form><div class='input__row'>
                    <div class="form_field">¿SE CUBRIO TOTALMENTE EL TEMARIO DEL CURSO?</div> 
                    <ul class='buttons'>
                        <li><input id='CEC_INS_Q51' class='radiobtn' name='writter' type='radio' value='poe' tabindex='1' checked/><span></span><label for='CEC_INS_Q51'>1</label></li>
                        <li><input id='CEC_INS_Q52' class='radiobtn' name='writter' type='radio' value='lovecraft' tabindex='2'><span></span><label for='CEC_INS_Q52'>2</label></li>
                        <li><input id='CEC_INS_Q53' class='radiobtn' name='writter' type='radio' value='becquer' tabindex='3'><span></span><label for='CEC_INS_Q53'>3</label></li>
                        <li><input id='CEC_INS_Q54' class='radiobtn' name='writter' type='radio' value='becquer' tabindex='3'><span></span><label for='CEC_INS_Q54'>4</label></li>
                        <li><input id='CEC_INS_Q55' class='radiobtn' name='writter' type='radio' value='becquer' tabindex='3'><span></span><label for='CEC_INS_Q55'>5</label></li>
                        <li><input class="colorCampo"  id='CEC_INS5' class='' type='text' value='' readonly></li>
                        <input type="checkbox" id="EI_CHK_Q5" value="1" checked/><label>APLICA</label>
                    </ul></div></form>
            <!-- 								Instalaciones 									-->
            <!-- primera pregunta -->
            <hr/>
            <form><div class='input__row'>
                    <div style="text-align:center" class="form_field">INSTALACIONES </div>
                    <div><table width='73%'><td class="form_field">¿EL AULA DONDE SE IMPARTIO EL CURSO RESULTO COMODA?</td><td><input type='text' class='colorCampo' id='CEC_PROM_ASPECTOS' value='' readonly /></td></table></div>
                    <ul class='buttons'>
                        <li><input id='CEC_IN_Q11' class='radiobtn' name='writter' type='radio' value='0' tabindex='1' checked/><span></span><label for='CEC_IN_Q11'>1</label></li>
                        <li><input id='CEC_IN_Q12' class='radiobtn' name='writter' type='radio' value='2.5' tabindex='2'><span></span><label for='CEC_IN_Q12'>2</label></li>
                        <li><input id='CEC_IN_Q13' class='radiobtn' name='writter' type='radio' value='5' tabindex='3'><span></span><label for='CEC_IN_Q13'>3</label></li>
                        <li><input id='CEC_IN_Q14' class='radiobtn' name='writter' type='radio' value='8' tabindex='3'><span></span><label for='CEC_IN_Q14'>4</label></li>
                        <li><input id='CEC_IN_Q15' class='radiobtn' name='writter' type='radio' value='10' tabindex='3'><span></span><label for='CEC_IN_Q15'>5</label></li>
                        <li><input class="colorCampo"  id='CEC_IN1' class='' type='text' value='' readonly></li>
                        <input type="checkbox" id="CHECK_IN1" value="1" checked/><label>APLICA</label>
                    </ul></div></form>
            <!-- segunda pregunta -->
            <form><div class='input__row'>
                    <div class="form_field">¿LA PROYECCION Y SONIDO FUERON ADECUADOS?</div>
                    <ul class='buttons'>
                        <li><input id='CEC_IN_Q21' class='radiobtn' name='writter' type='radio' value='poe' tabindex='1' checked/><span></span><label for='CEC_IN_Q21'>1</label></li>
                        <li><input id='CEC_IN_Q22' class='radiobtn' name='writter' type='radio' value='lovecraft' tabindex='2'><span></span><label for='CEC_IN_Q22'>2</label></li>
                        <li><input id='CEC_IN_Q23' class='radiobtn' name='writter' type='radio' value='becquer' tabindex='3'><span></span><label for='CEC_IN_Q23'>3</label></li>
                        <li><input id='CEC_IN_Q24' class='radiobtn' name='writter' type='radio' value='becquer' tabindex='3'><span></span><label for='CEC_IN_Q24'>4</label></li>
                        <li><input id='CEC_IN_Q25' class='radiobtn' name='writter' type='radio' value='becquer' tabindex='3'><span></span><label for='CEC_IN_Q25'>5</label></li>
                        <li><input class="colorCampo"  id='CEC_IN2' class='' type='text' value='' readonly></li>
                        <input type="checkbox" id="CHECK_IN2" value="1" checked/><label>APLICA</label>
                    </ul></div></form>
            <!-- tercer pregunta -->
            <form><div class='input__row'>
                    <div class="form_field">¿LOS BAÑOS Y AREAS COMUNES SE ENCONTRABAN LIMPIOS?</div> 
                    <ul class='buttons'>
                        <li><input id='CEC_IN_Q31' class='radiobtn' name='writter' type='radio' value='poe' tabindex='1' checked/><span></span><label for='CEC_IN_Q31'>1</label></li>
                        <li><input id='CEC_IN_Q32' class='radiobtn' name='writter' type='radio' value='lovecraft' tabindex='2'><span></span><label for='CEC_IN_Q32'>2</label></li>
                        <li><input id='CEC_IN_Q33' class='radiobtn' name='writter' type='radio' value='becquer' tabindex='3'><span></span><label for='CEC_IN_Q33'>3</label></li>
                        <li><input id='CEC_IN_Q34' class='radiobtn' name='writter' type='radio' value='becquer' tabindex='3'><span></span><label for='CEC_IN_Q34'>4</label></li>
                        <li><input id='CEC_IN_Q35' class='radiobtn' name='writter' type='radio' value='becquer' tabindex='3'><span></span><label for='CEC_IN_Q35'>5</label></li>
                        <li><input class="colorCampo" id='CEC_IN3' class='' type='text' value='' readonly></li>
                        <input type="checkbox" id="CHECK_IN3" value="1" checked/><label>APLICA</label>
                    </ul></div></form>
            <!-- cuarta pregunta -->
            <form><div class='input__row'>
                    <div class="form_field">¿COMO EVALUA EL SERVICIO Y ALIMENTOS DEL RESTAURANTE?</div> 
                    <ul class='buttons'>
                        <li><input id='CEC_IN_Q41' class='radiobtn' name='writter' type='radio' value='poe' tabindex='1' checked/><span></span><label for='CEC_IN_Q41'>1</label></li>
                        <li><input id='CEC_IN_Q42' class='radiobtn' name='writter' type='radio' value='lovecraft' tabindex='2'><span></span><label for='CEC_IN_Q42'>2</label></li>
                        <li><input id='CEC_IN_Q43' class='radiobtn' name='writter' type='radio' value='becquer' tabindex='3'><span></span><label for='CEC_IN_Q43'>3</label></li>
                        <li><input id='CEC_IN_Q44' class='radiobtn' name='writter' type='radio' value='becquer' tabindex='3'><span></span><label for='CEC_IN_Q44'>4</label></li>
                        <li><input id='CEC_IN_Q45' class='radiobtn' name='writter' type='radio' value='becquer' tabindex='3'><span></span><label for='CEC_IN_Q45'>5</label></li>
                        <li><input class="colorCampo"  id='CEC_IN4' class='' type='text' value='' readonly></li>
                        <input type="checkbox" id="CHECK_IN4" value="1" checked/><label>APLICA</label>
                    </ul></div></form>
            <!-- quinta pregunta -->
            <form><div class='input__row'>
                    <div class="form_field">¡EL PERSONAL DEL VALET PARKING LE ATENDIO DE MANERA EFICAZ Y CORDIAL?</div> 
                    <ul class='buttons'>
                        <li><input id='CEC_IN_Q51' class='radiobtn' name='writter' type='radio' value='poe' tabindex='1' checked/><span></span><label for='CEC_IN_Q51'>1</label></li>
                        <li><input id='CEC_IN_Q52' class='radiobtn' name='writter' type='radio' value='lovecraft' tabindex='2'><span></span><label for='CEC_IN_Q52'>2</label></li>
                        <li><input id='CEC_IN_Q53' class='radiobtn' name='writter' type='radio' value='becquer' tabindex='3'><span></span><label for='CEC_IN_Q53'>3</label></li>
                        <li><input id='CEC_IN_Q54' class='radiobtn' name='writter' type='radio' value='becquer' tabindex='3'><span></span><label for='CEC_IN_Q54'>4</label></li>
                        <li><input id='CEC_IN_Q55' class='radiobtn' name='writter' type='radio' value='becquer' tabindex='3'><span></span><label for='CEC_IN_Q55'>5</label></li>
                        <li><input class="colorCampo"  id='CEC_IN5' class='' type='text' value='' readonly></li>
                        <input type="checkbox" id="CHECK_IN5" value="1" checked/><label>APLICA</label>
                    </ul></div></form>
        </div>
    </body>
</html>
<%} else {
%>
<html><body><div style="text-align: center"><h1>NOT FOUND!!</h1></div></body></html>
<%
    }
%>