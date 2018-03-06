<%-- 
    Document   : COFIDE_Marketing
    Created on : 15/09/2017, 12:03:52 PM
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
        <title>MARKETING</title>
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
            .colorCampoM{background-color: #F9F6F6; color: black;}
        </style>
    </head>
    <body>
        <div style="width: 1200 px; overflow: scroll; height:690px; padding-left: 20px;" class="form_field">
            <div id='EV_DIV_IN' style="width: 1100px"></div>
            <!-- 								Instalaciones 									-->
            <!-- primera pregunta -->
            <form><div class='input__row'>                    
                    <ul class='buttons' hidden='true'>
                        <li><input id='CEC_M_Q01' class='radiobtn' name='writter' type='radio' value='0'  tabindex='1'><span></span><label for='CEC_M_Q01'>1</label></li>
                        <li><input id='CEC_M_Q02' class='radiobtn' name='writter' type='radio' value='2.5' tabindex='2'><span></span><label for='CEC_M_Q02'>2</label></li>
                        <li><input id='CEC_M_Q03' class='radiobtn' name='writter' type='radio' value='5' tabindex='3'><span></span><label for='CEC_M_Q03'>3</label></li>
                        <li><input id='CEC_M_Q04' class='radiobtn' name='writter' type='radio' value='8' tabindex='4'><span></span><label for='CEC_M_Q04'>4</label></li>
                        <li><input id='CEC_M_Q05' class='radiobtn' name='writter' type='radio' value='10' tabindex='5'><span></span><label for='CEC_M_Q05'>5</label></li>
                    </ul></div></form>

            <form><div class='input__row'>
                    <div style="text-align:center" class="form_field">MARKETING</div>
                    <div><table width='80%'><td class="form_field">¿MEDIANTE QUÉ MEDIO LE GUSTARÍA ENTERARSE DE NUESTROS EVENTOS Y PROMOCIONES?</td></table></div>
                    <ul class='buttons'>
                        <li><input id='CEC_M_Q11' class='radiobtn' name='writter' type='radio' value='1'  tabindex='1' onchange="validaMarketing()" checked/><span></span><label for='CEC_M_Q11'>MENSAJE DE FACEBOOK</label></li>
                        <li><input class="colorCampoM"  id='CEC_M_FB'  type='text' value='' placeholder="CUENTA DE FACEBOOK" size="40px"/></li>
                        <BR>
                        <!--<li><input id='CEC_M_Q12' class='radiobtn' name='writter' type='radio' value='1' tabindex='2' onchange="validaMarketing()" /><span></span><label for='CEC_M_Q12'>PERIÓDICO</label></li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-->
                        <li><input id='CEC_M_Q12' class='radiobtn' name='writter' type='radio' value='1' tabindex='2' onchange="validaMarketing()" /><span></span><label for='CEC_M_Q12'>PERIÓDICO</label></li>
                        <li style="padding-left: 111px"><input class="colorCampoM"  id='CEC_M_PER' type='text' value='' placeholder="¿CUÁL PERIÓDICO?" size="40px"/></li>
                        <li><input id='CEC_M_Q13' class='radiobtn' name='writter' type='radio' value='1' tabindex='3' onchange="validaMarketing()" /><span></span><label for='CEC_M_Q13'>E-MAIL</label></li>
                        <BR>
                        <!--<li><input id='CEC_M_Q14' class='radiobtn' name='writter' type='radio' value='1' tabindex='4' onchange="validaMarketing()" /><span></span><label for='CEC_M_Q14'>OTRO MEDIO</label></li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-->
                        <li><input id='CEC_M_Q14' class='radiobtn' name='writter' type='radio' value='1' tabindex='4' onchange="validaMarketing()" /><span></span><label for='CEC_M_Q14'>OTRO MEDIO</label></li>
                        <li style="padding-left: 95px"><input class="colorCampoM"  id='CEC_M_OTRO'  type='text' value='' placeholder="¿CUÁL MEDIO?" size="40px"/></li>
                        <li><input id='CEC_M_Q15' class='radiobtn' name='writter' type='radio' value='1' tabindex='5' onchange="validaMarketing()" /><span></span><label for='CEC_M_Q15'>LLAMADA TELEFÓNICA</label></li>                        
                    </ul></div></form>

            <!-- segunda pregunta -->
            <form><div class='input__row'>
                    <div class="form_field">¿QUÉ MEJORARÍAS DE NUESTROS CURSOS?</div>
                    <ul class='buttons'>
                        <!--<li><input class="colorCampo"  id='CEC_M2'  type='text' value='' placeholder="MEJORAS..."/></li>-->                        
                        <li><textarea class="colorCampoM"  id='CEC_M2'  rows="2" cols="80" type='text' value='' placeholder="MEJORAS..."/></textarea></li>                        
                    </ul></div></form>

            <!-- tercer pregunta -->
            <form><div class='input__row'>
                    <div class="form_field">¿CONOCE NUESTRA TARJETA DE <B>CLIENTE PREFERENTE COFIDE</b>?</div> 
                    <ul class='buttons'>
                        <li><input id='CEC_M_Q31' class='radiobtn' name='writter' type='radio' value='1' tabindex='1'  checked/><span></span><label for='CEC_M_Q31'>SI</label></li>
                        <li><input id='CEC_M_Q32' class='radiobtn' name='writter' type='radio' value='1' tabindex='2' /><span></span><label for='CEC_M_Q32'>NO</label></li>
                        <br>
                        <br>
                        <li><label>¿LE PARECE ÚTIL?,¿QUÉ LE CAMBIARÍA?</label></li>
                        <br>
                        <li><textarea class="colorCampoM" id='CEC_M3' rows="2" cols="80" type='text' value='' placeholder="OPINIÓN..."></textarea></li>                        
                    </ul></div></form>

            <!-- cuarta pregunta -->
            <form><div class='input__row'>
                    <div class="form_field">¿LE GUSTÓ EL CURSO? POR FAVOR, COMPÁRTANOS LOS DATOS DE PERSONAS A QUIENES PODRÍAN INTERESARLE</div> 
                    <ul class='buttons'>

                        <li><label style="padding-left: 15%">NOMBRE</label></li>                        
                        <li><label style="padding-left: 15%">E-MAIL</label></li>                        
                        <li><label style="padding-left: 18%">TELEFÓNO</label></li>                        
                        <BR>
                        <li><label>1</label></li>
                        <li><input class="colorCampoM"  id='CEC_M411'  type='text' value='' size="30px" placeholder="NOMBRE"/></li>
                        <li><input class="colorCampoM"  id='CEC_M412'  type='text' value='' size="30px" placeholder="E-MAIL"/></li>
                        <li><input class="colorCampoM"  id='CEC_M413'  type='text' value='' size="30px" placeholder="TELEFÓNO"/></li>
                        <BR>
                        <li><label>2</label></li>
                        <li><input class="colorCampoM"  id='CEC_M421'  type='text' value='' size="30px" placeholder="NOMBRE"/></li>
                        <li><input class="colorCampoM"  id='CEC_M422'  type='text' value='' size="30px" placeholder="E-MAIL"/></li>
                        <li><input class="colorCampoM"  id='CEC_M423'  type='text' value='' size="30px" placeholder="TELEFÓNO"/></li>
                        <BR>
                        <li><label>3</label></li>
                        <li><input class="colorCampoM"  id='CEC_M431'  type='text' value='' size="30px" placeholder="NOMBRE"/></li>
                        <li><input class="colorCampoM"  id='CEC_M432'  type='text' value='' size="30px" placeholder="E-MAIL"/></li>
                        <li><input class="colorCampoM"  id='CEC_M433'  type='text' value='' size="30px" placeholder="TELEFÓNO"/></li>
                        <BR>
                        <li><label>4</label></li>
                        <li><input class="colorCampoM"  id='CEC_M441'  type='text' value='' size="30px" placeholder="NOMBRE"/></li>
                        <li><input class="colorCampoM"  id='CEC_M442'  type='text' value='' size="30px" placeholder="E-MAIL"/></li>
                        <li><input class="colorCampoM"  id='CEC_M443'  type='text' value='' size="30px" placeholder="TELEFÓNO"/></li>
                        <BR>
                        <li><label>5</label></li>
                        <li><input class="colorCampoM"  id='CEC_M451'  type='text' value='' size="30px" placeholder="NOMBRE"/></li>
                        <li><input class="colorCampoM"  id='CEC_M452'  type='text' value='' size="30px" placeholder="E-MAIL"/></li>
                        <li><input class="colorCampoM"  id='CEC_M453'  type='text' value='' size="30px" placeholder="TELEFÓNO"/></li>

                    </ul></div></form>

        </div>
    </body>
</html>
