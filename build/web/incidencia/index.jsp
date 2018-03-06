<%-- 
    Document   : index
    Created on : 5/03/2018, 05:53:17 PM
    Author     : Desarrollo_COFIDE
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<!--
To change this license header, choose License Headers in Project Properties.
To change this template file, choose Tools | Templates
and open the template in the editor.
-->
<html>
    <head>
        <title>INCIDENCIAS</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet" href="css/Main.css" type="text/css" media="all" />
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
        <script src="javascript/Main.js"></script> 
        <script src="javascript/cofide_login.js"></script> 
        <script src="javascript/cofide_usuario_inc.js"></script>                 

        <link href="https://use.fontawesome.com/releases/v5.0.6/css/all.css" rel="stylesheet">
        <!--<link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">-->
        <link href="http://201.161.14.206:9001/cofide/images/cofide.ico" rel="shortcut icon" type="image/vnd.microsoft.icon" />
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>

    </head>
    <body onload="init()">

        <div>
            <img src="http://201.161.14.206:9001/cofide/images/cofide_newlogo_a_MR.png" alt="COFIDE"  id="logocofide">            
        </div>
        <div id="fondo">
            <div>
                <img src="http://201.161.14.206:9001/cofide/images/incidencias.png" alt="LOGO_INCIDENCIA"  width="15%">
            </div>
            <h2>BITACORA DE INCIDENCIAS</h2>            
            <button onclick="OpnModal(1)" style="width:auto;"><i class="fas fa-sign-in-alt">&nbsp;&nbsp;</i>INICIO DE SESIÓN</button>
            <button onclick="OpnModal(2)" style="width:auto;"><i class="fas fa-plus">&nbsp;&nbsp;</i>REGISTRARSE</button>

        </div>
        <!--MODAL DE INICIO DE SESIÓN-->
        <div id="id01" class="modal">

            <form class="modal-content animate">
                <div class="imgcontainer">
                    <span onclick="CloseModal(1)" class="close" title="CERRAR">&times;</span>
                    <img src="http://201.161.14.206:9001/cofide/images/cofide_newlogo_a_MR.png" alt="COFIDE" class="avatar">
                </div>

                <div class="container">                    
                    <label for="uname"><b>USUARIO</b></label>
                    <input type="text" placeholder="INGRESAR USUARIO" name="uname" id="name">

                    <label for="psw"><b>CONTRASEÑA</b></label>
                    <input type="password" placeholder="INGRESAR CONTRASEÑA" name="psw" id="psw" required>

                    <button type="button" onclick="ValidField(1)">INICIAR SESIÓN</button>                   
                </div>

                <div class="container" style="background-color:#f1f1f1">
                    <button type="button" onclick="CloseModal(1)" class="cancelbtn">CANCELAR</button>                    
                </div>
            </form>
        </div>
        <!--MODAL DE INICIO DE SESIÓN-->

        <!--MODAL DE REGISTRO DE USUARIO-->
        <div id="id02" class="modal">

            <form class="modal-content animate" >
                <div class="imgcontainer">
                    <span onclick="CloseModal(2)" class="close" title="CERRAR">&times;</span>
                    <img src="http://201.161.14.206:9001/cofide/images/cofide_newlogo_a_MR.png" alt="COFIDE" class="avatar">
                </div>

                <div class="container">                    
                    <label for="uname"><b>NOMBRE</b></label>
                    <input type="text" placeholder="INGRESAR NOMBRE" name="uname" id="nombreusuario" >

                    <label for="uname"><b>USUARIO</b></label>
                    <input type="text" placeholder="INGRESAR USUARIO" name="uname" id="name1" >

                    <label for="psw"><b>CONTRASEÑA</b></label>
                    <input type="password" placeholder="INGRESAR CONTRASEÑA" name="psw" id="psw1" required>

                    <label for="psw"><b>CONFIRMAR CONTRASEÑA</b></label>
                    <input type="password" placeholder="CONFIRMAR CONTRASEÑA" name="psw" id="psw2" required>

                    <button type="button" onclick="ValidField(2)">REGISTRARSE</button>        

                </div>

                <div class="container" style="background-color:#f1f1f1">
                    <button type="button" onclick="CloseModal(2)" class="cancelbtn">CANCELAR</button>                    
                </div>
            </form>
        </div>
        <!--MODAL DE REGISTRO DE USUARIO-->

        <!--MODAL ALERTA-->
        <div id="alerta" class="modal">

            <form class="modal-content animate" >                             
                <div class="imgcontainer">
                    <span onclick="CloseModal(3)" class="close" title="CERRAR">&times;</span>
                </div>

                <div class="container" style="background-color:#f1f1f1; padding-top: 7%;" id="contenidoalerta">                                    
                </div>
            </form>
        </div>
        <!--MODAL ALERTA-->
        <div id="tmp" style="display: none"></div>

    </body>
</html>
