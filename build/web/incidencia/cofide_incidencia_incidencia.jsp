<%-- 
    Document   : cofide_incidencia_incidencia
    Created on : 16/02/2018, 04:42:25 PM
    Author     : Desarrollo_COFIDE
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="cofide_incidencia.html" %>
<!DOCTYPE html>
<html>
    <body>
        <div id="container">
            <div id="contenedor_incidencia">
                <div>
                    <label>CLIENTE</label>
                    <input type="text" placeholder="INFORMACIÓN DEL CLIENTE" id="cliente" class="form-control">
                </div>
                <div>
                    <label>CORREO</label>
                    <input type="email" placeholder="CORREO@DOMINIO.ORG" id="correo" class="form-control">
                </div>
                <br>
                <div>
                    <label>CURSO DE REFERENCIA</label>
                    <br>
                    <select id="cursoref" class="form-control">
                        <option value="0">SELECCIONE UNA OPCIÓN</option>
                    </select>
                </div>
                <br>
                <div>
                    <label>TIPO DE PROBLEMA</label>
                    <br>
                    <select id="tipoproblema" class="form-control" onchange="hidemodulo()">
                        <option value="0">SELECCIONE UNA OPCIÓN</option>
                    </select>
                </div>
                <br>
                <div>
                    <label>ORIGEN DEL PROBLEMA</label>
                    <br>
                    <select id="origenproblema" class="form-control">
                        <option value="">SELECCIONE UNA OPCIÓN</option>
                        <option value="CLIENTE EXT.">CLIENTE EXTERNO</option>
                        <option value="CLIENTE INT.">CLIENTE INTERNO</option>
                        <option value="SISTEMA">SISTEMA</option>
                    </select>
                </div>
                <br>
                <div>
                    <label>MODULO</label>
                    <br>
                    <select id="modulocrm" class="form-control">
                        <option value="0">SELECCIONE UNA OPCIÓN</option>
                    </select>
                </div>                                           
                <div>
                    <label>COMENTARIO</label>
                    <textarea placeholder="COMENTARIOS DEL CLIENTE" id="comentario" class="form-control"></textarea>                    
                </div>                
                <div>
                    <label>OBSERVACIÓN</label>
                    <textarea placeholder="OBSERVACIONES SOBRE EL PROBLEMA" id="observacion" class="form-control"></textarea>                    
                </div>
                <br>
                <div>
                    <label>ESTATUS</label>
                    <div class="panel panel-default">
                        <div class="panel-body">
                            <label class="radio-inline">
                                <input type="radio" name="Tipo" id="radio1" value=1>&nbsp;CONCLUIDO
                            </label>&nbsp;
                            <label class="radio-inline">
                                <input type="radio" name="Tipo" id="radio2" value=2>&nbsp;PENDIENTE
                            </label>&nbsp;
                            <label class="radio-inline">
                                <input type="radio" name="Tipo" id="radio3" value=3 checked="true">&nbsp;SIN ATENDER
                            </label>                                                        
                        </div>
                    </div>
                </div>  
                <br>
                <div>
                    <button type="button" onclick="SaveIncidencia()" class="btn btn-block btn-info">GUARDAR</button>
                </div>
            </div>
        </div>
    </body>
</html>
