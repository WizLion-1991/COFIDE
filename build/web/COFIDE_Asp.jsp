<%-- 
    Document   : COFIDE_Asp
    Created on : 10/03/2017, 02:20:55 PM
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
    <body>
        <div style="width: 1200 px; overflow: scroll; height:700px; padding-left: 20px;" class="form_field">
            <div id='EV_DIV' style="width: 1100px"></div>
            <div><input id='CEC_ID_EVALUACION' type="text" value="" hidden="true"/></div>
            <div><input id='CC_CURSO_ID' type="text" value="<%=strIDCurso%>" hidden="true"/></div>            
            <form><div class='input__row'>
                    <div style="text-align:center " class="form_field">ASPECTOS GENERALES</div>
                    <div><table width='80%'><td class="form_field">¿EL CURSO CUMPLIO SUS EXPECTATIVAS?</td><td><input type='text' class='colorCampo' id='CEC_PROM_ASPECTOS' value='' readonly /></td></table></div>
                    <ul class='buttons'>
                        <li><input id='CEC_ASP_Q11' class='radiobtn' name='writter' type='radio' value='0' tabindex='1'  onchange="Calif()" checked/><span></span><label for='CEC_ASP_Q11'>1</label></li>
                        <li><input id='CEC_ASP_Q12' class='radiobtn' name='writter' type='radio' value='2.5' tabindex='2' onchange="Calif()" ><span></span><label for='CEC_ASP_Q12'>2</label></li>
                        <li><input id='CEC_ASP_Q13' class='radiobtn' name='writter' type='radio' value='5' tabindex='3' onchange="Calif()" ><span></span><label for='CEC_ASP_Q13'>3</label></li>
                        <li><input id='CEC_ASP_Q14' class='radiobtn' name='writter' type='radio' value='8' tabindex='4' onchange="Calif()" ><span></span><label for='CEC_ASP_Q14'>4</label></li>
                        <li><input id='CEC_ASP_Q15' class='radiobtn' name='writter' type='radio' value='10' tabindex='5' onchange="Calif()" ><span></span><label for='CEC_ASP_Q15'>5</label></li>
                        <li><input  class="colorCampo" id='CEC_ASP1' type='text' value='' readonly></li>
                        <input type="checkbox" id="CHECK_ASP1" value="1"  onchange="Calif()" checked/><label>APLICA</label>
                    </ul></div></form>
            <!-- segunda pregunta -->
            <form><div class='input__row'>
                    <div class="form_field">¿COMO CALIFICA LA ATENCION TELEFONICA RECIBIDA POR SU EJECUTIVO?</div>
                    <ul class='buttons'>
                        <li><input id='CEC_ASP_Q21' class='radiobtn' name='writter' type='radio' value='0' tabindex='1' onchange="Calif()"  checked/><span></span><label for='CEC_ASP_Q21'>1</label></li>
                        <li><input id='CEC_ASP_Q22' class='radiobtn' name='writter' type='radio' value='2.5' tabindex='2' onchange="Calif()" ><span></span><label for='CEC_ASP_Q22'>2</label></li>
                        <li><input id='CEC_ASP_Q23' class='radiobtn' name='writter' type='radio' value='5' tabindex='3' onchange="Calif()" ><span></span><label for='CEC_ASP_Q23'>3</label></li>
                        <li><input id='CEC_ASP_Q24' class='radiobtn' name='writter' type='radio' value='8' tabindex='4' onchange="Calif()" ><span></span><label for='CEC_ASP_Q24'>4</label></li>
                        <li><input id='CEC_ASP_Q25' class='radiobtn' name='writter' type='radio' value='10' tabindex='5' onchange="Calif()" ><span></span><label for='CEC_ASP_Q25'>5</label></li>
                        <li><input class="colorCampo" id='CEC_ASP2' type='text' value='' readonly></li>
                        <input type="checkbox" id="CHECK_ASP2" value="1" checked/><label>APLICA</label>
                    </ul></div></form>
            <!-- tercer pregunta -->
            <form><div class='input__row'>
                    <div class="form_field">¿COMO CALIFICA LA ATENCION PRESTADA POR NUESTRAS EDECANTES?</div> 
                    <ul class='buttons'>
                        <li><input id='CEC_ASP_Q31' class='radiobtn' name='writter' type='radio' value='0' tabindex='1' onchange="Calif()"  checked/><span></span><label for='CEC_ASP_Q31'>1</label></li>
                        <li><input id='CEC_ASP_Q32' class='radiobtn' name='writter' type='radio' value='2.5' tabindex='2' onchange="Calif()" ><span></span><label for='CEC_ASP_Q32'>2</label></li>
                        <li><input id='CEC_ASP_Q33' class='radiobtn' name='writter' type='radio' value='5' tabindex='3' onchange="Calif()" ><span></span><label for='CEC_ASP_Q33'>3</label></li>
                        <li><input id='CEC_ASP_Q34' class='radiobtn' name='writter' type='radio' value='8' tabindex='4' onchange="Calif()" ><span></span><label for='CEC_ASP_Q34'>4</label></li>
                        <li><input id='CEC_ASP_Q35' class='radiobtn' name='writter' type='radio' value='10' tabindex='5' onchange="Calif()" ><span></span><label for='CEC_ASP_Q35'>5</label></li>
                        <li><input class="colorCampo"  id='CEC_ASP3'  type='text' value='' readonly></li>
                        <input type="checkbox" id="CHECK_ASP3" value="1" onchange="Calif()"  checked/><label>APLICA</label>
                    </ul></div></form>
            <!-- cuarta pregunta -->
            <form><div class='input__row'>
                    <div class="form_field">¿LA DURACION DEL CURSO LE PARECIO ADECUADA?</div> 
                    <ul class='buttons'>
                        <li><input id='CEC_ASP_Q41' class='radiobtn' name='writter' type='radio' value='0' tabindex='1'  onchange="Calif()" checked/><span></span><label for='CEC_ASP_Q41'>1</label></li>
                        <li><input id='CEC_ASP_Q42' class='radiobtn' name='writter' type='radio' value='2.5' tabindex='2' onchange="Calif()" ><span></span><label for='CEC_ASP_Q42'>2</label></li>
                        <li><input id='CEC_ASP_Q43' class='radiobtn' name='writter' type='radio' value='5' tabindex='3' onchange="Calif()" ><span></span><label for='CEC_ASP_Q43'>3</label></li>
                        <li><input id='CEC_ASP_Q44' class='radiobtn' name='writter' type='radio' value='8' tabindex='4' onchange="Calif()" ><span></span><label for='CEC_ASP_Q44'>4</label></li>
                        <li><input id='CEC_ASP_Q45' class='radiobtn' name='writter' type='radio' value='10' tabindex='5' onchange="Calif()" ><span></span><label for='CEC_ASP_Q45'>5</label></li>
                        <li><input class="colorCampo"  id='CEC_ASP4'  type='text' value='' readonly></li>
                        <input type="checkbox" id="CHECK_ASP4" value="1"  onchange="Calif()" checked/><label>APLICA</label>
                    </ul></div></form>
            <hr>
            <div>
                <input id='next1N' type='button' onclick='continuarPaso(0)' value='Regresar' title="Regresar a la pestaña principal"/>
                <input id='next1N' type='button' onclick='continuarPaso(2)' value='Continuar' title="avanzar a pestaña instructores"/>
            </div>
        </div>
    </body>
</html>
<%} else {
%>
<html><body><div style="text-align: center"><h1>NOT FOUND!!</h1></div></body></html>
<%
    }
%>