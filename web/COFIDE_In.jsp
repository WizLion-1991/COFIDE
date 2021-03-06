<%-- 
    Document   : COFIDE_In
    Created on : 10/03/2017, 02:22:27 PM
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
    <body>
        <div style="width: 1200 px; overflow: scroll; height:700px; padding-left: 20px;" class="form_field">
            <div id='EV_DIV_IN' style="width: 1100px"></div>
            <!-- 								Instalaciones 									-->
            <!-- primera pregunta -->
            <form><div class='input__row'>                    
                    <ul class='buttons' hidden='true'>
                        <li><input id='CEC_IN_Q01' class='radiobtn' name='writter' type='radio' value='0'  tabindex='1'><span></span><label for='CEC_IN_Q01'>1</label></li>
                        <li><input id='CEC_IN_Q02' class='radiobtn' name='writter' type='radio' value='2.5' tabindex='2'><span></span><label for='CEC_IN_Q02'>2</label></li>
                        <li><input id='CEC_IN_Q03' class='radiobtn' name='writter' type='radio' value='5' tabindex='3'><span></span><label for='CEC_IN_Q03'>3</label></li>
                        <li><input id='CEC_IN_Q04' class='radiobtn' name='writter' type='radio' value='8' tabindex='4'><span></span><label for='CEC_IN_Q04'>4</label></li>
                        <li><input id='CEC_IN_Q05' class='radiobtn' name='writter' type='radio' value='10' tabindex='5'><span></span><label for='CEC_IN_Q05'>5</label></li>
                    </ul></div></form>
            <form><div class='input__row'>
                    <div style="text-align:center" class="form_field">INSTALACIONES </div>
                    <div><table width='73%'><td class="form_field">¿EL AULA DONDE SE IMPARTIO EL CURSO RESULTO COMODA?</td><td><input type='text' class='colorCampo' id='CEC_PROM_INSTALACION' value='' readonly /></td></table></div>
                    <ul class='buttons'>
                        <li><input id='CEC_IN_Q11' class='radiobtn' name='writter' type='radio' value='0'  tabindex='1'  onchange="Calif()" checked/><span></span><label for='CEC_IN_Q11'>1</label></li>
                        <li><input id='CEC_IN_Q12' class='radiobtn' name='writter' type='radio' value='2.5' tabindex='2' onchange="Calif()" ><span></span><label for='CEC_IN_Q12'>2</label></li>
                        <li><input id='CEC_IN_Q13' class='radiobtn' name='writter' type='radio' value='5' tabindex='3' onchange="Calif()" ><span></span><label for='CEC_IN_Q13'>3</label></li>
                        <li><input id='CEC_IN_Q14' class='radiobtn' name='writter' type='radio' value='8' tabindex='4' onchange="Calif()" ><span></span><label for='CEC_IN_Q14'>4</label></li>
                        <li><input id='CEC_IN_Q15' class='radiobtn' name='writter' type='radio' value='10' tabindex='5' onchange="Calif()" ><span></span><label for='CEC_IN_Q15'>5</label></li>
                        <li><input class="colorCampo"  id='CEC_IN1'  type='text' value='' readonly></li>
                        <input type="checkbox" id="CHECK_IN1" value="1"  onchange="Calif()" checked/><label>APLICA</label>
                    </ul></div></form>
            <!-- segunda pregunta -->
            <form><div class='input__row'>
                    <div class="form_field">¿LA PROYECCION Y SONIDO FUERON ADECUADOS?</div>
                    <ul class='buttons'>
                        <li><input id='CEC_IN_Q21' class='radiobtn' name='writter' type='radio' value='0' tabindex='1'  onchange="Calif()" checked/><span></span><label for='CEC_IN_Q21'>1</label></li>
                        <li><input id='CEC_IN_Q22' class='radiobtn' name='writter' type='radio' value='2.5' tabindex='2' onchange="Calif()" ><span></span><label for='CEC_IN_Q22'>2</label></li>
                        <li><input id='CEC_IN_Q23' class='radiobtn' name='writter' type='radio' value='5' tabindex='3' onchange="Calif()" ><span></span><label for='CEC_IN_Q23'>3</label></li>
                        <li><input id='CEC_IN_Q24' class='radiobtn' name='writter' type='radio' value='8' tabindex='4' onchange="Calif()" ><span></span><label for='CEC_IN_Q24'>4</label></li>
                        <li><input id='CEC_IN_Q25' class='radiobtn' name='writter' type='radio' value='10' tabindex='5' onchange="Calif()" ><span></span><label for='CEC_IN_Q25'>5</label></li>
                        <li><input class="colorCampo"  id='CEC_IN2'  type='text' value='' readonly></li>
                        <input type="checkbox" id="CHECK_IN2" value="1"  onchange="Calif()" checked/><label>APLICA</label>
                    </ul></div></form>
            <!-- tercer pregunta -->
            <form><div class='input__row'>
                    <div class="form_field">¿LOS BAÑOS Y AREAS COMUNES SE ENCONTRABAN LIMPIOS?</div> 
                    <ul class='buttons'>
                        <li><input id='CEC_IN_Q31' class='radiobtn' name='writter' type='radio' value='0' tabindex='1'  onchange="Calif()" checked/><span></span><label for='CEC_IN_Q31'>1</label></li>
                        <li><input id='CEC_IN_Q32' class='radiobtn' name='writter' type='radio' value='2.5' tabindex='2' onchange="Calif()" ><span></span><label for='CEC_IN_Q32'>2</label></li>
                        <li><input id='CEC_IN_Q33' class='radiobtn' name='writter' type='radio' value='5' tabindex='3' onchange="Calif()" ><span></span><label for='CEC_IN_Q33'>3</label></li>
                        <li><input id='CEC_IN_Q34' class='radiobtn' name='writter' type='radio' value='8' tabindex='4' onchange="Calif()" ><span></span><label for='CEC_IN_Q34'>4</label></li>
                        <li><input id='CEC_IN_Q35' class='radiobtn' name='writter' type='radio' value='10' tabindex='5' onchange="Calif()" ><span></span><label for='CEC_IN_Q35'>5</label></li>
                        <li><input class="colorCampo" id='CEC_IN3'  type='text' value='' readonly></li>
                        <input type="checkbox" id="CHECK_IN3" value="1" onchange="Calif()"  checked/><label>APLICA</label>
                    </ul></div></form>
            <!-- cuarta pregunta -->
            <form><div class='input__row'>
                    <div class="form_field">¿COMO EVALUA EL SERVICIO Y ALIMENTOS DEL RESTAURANTE?</div> 
                    <ul class='buttons'>
                        <li><input id='CEC_IN_Q41' class='radiobtn' name='writter' type='radio' value='0' tabindex='1'  onchange="Calif()" checked/><span></span><label for='CEC_IN_Q41'>1</label></li>
                        <li><input id='CEC_IN_Q42' class='radiobtn' name='writter' type='radio' value='2.5' tabindex='2' onchange="Calif()" ><span></span><label for='CEC_IN_Q42'>2</label></li>
                        <li><input id='CEC_IN_Q43' class='radiobtn' name='writter' type='radio' value='5' tabindex='3' onchange="Calif()" ><span></span><label for='CEC_IN_Q43'>3</label></li>
                        <li><input id='CEC_IN_Q44' class='radiobtn' name='writter' type='radio' value='8' tabindex='4' onchange="Calif()" ><span></span><label for='CEC_IN_Q44'>4</label></li>
                        <li><input id='CEC_IN_Q45' class='radiobtn' name='writter' type='radio' value='10' tabindex='5' onchange="Calif()" ><span></span><label for='CEC_IN_Q45'>5</label></li>
                        <li><input class="colorCampo"  id='CEC_IN4'  type='text' value='' readonly></li>
                        <input type="checkbox" id="CHECK_IN4" value="1" onchange="Calif()"  checked/><label>APLICA</label>
                    </ul></div></form>
            <!-- quinta pregunta -->
            <form><div class='input__row'>
                    <div class="form_field">¡EL PERSONAL DEL VALET PARKING LE ATENDIO DE MANERA EFICAZ Y CORDIAL?</div> 
                    <ul class='buttons'>
                        <li><input id='CEC_IN_Q51' class='radiobtn' name='writter' type='radio' value='0' tabindex='1'  onchange="Calif()" checked/><span></span><label for='CEC_IN_Q51'>1</label></li>
                        <li><input id='CEC_IN_Q52' class='radiobtn' name='writter' type='radio' value='2.5' tabindex='2' onchange="Calif()" ><span></span><label for='CEC_IN_Q52'>2</label></li>
                        <li><input id='CEC_IN_Q53' class='radiobtn' name='writter' type='radio' value='5' tabindex='3' onchange="Calif()" ><span></span><label for='CEC_IN_Q53'>3</label></li>
                        <li><input id='CEC_IN_Q54' class='radiobtn' name='writter' type='radio' value='8' tabindex='4' onchange="Calif()" ><span></span><label for='CEC_IN_Q54'>4</label></li>
                        <li><input id='CEC_IN_Q55' class='radiobtn' name='writter' type='radio' value='10' tabindex='5' onchange="Calif()" ><span></span><label for='CEC_IN_Q55'>5</label></li>
                        <li><input class="colorCampo"  id='CEC_IN5'  type='text' value='' readonly></li>
                        <input type="checkbox" id="CHECK_IN5" value="1"  onchange="Calif()" checked/><label>APLICA</label>
                    </ul></div></form>
            <div>
                <input id='next3P' type='button' onclick='continuarPaso(2)' value='Regresar' title="Regresar a la pestaña de Instructor"/>
                <input id='next3N' type='button' onclick='continuarPaso(4)' value='Continuar' title="Avanzar a pestaña Cursos adicionales"/>
            </div>
        </div>
    </body>
</html>
