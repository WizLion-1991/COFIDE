/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
function test() {

}

function envia() {
    var strPost = "";
    $.ajax({
        type: "POST",
        data: strPost,
        scriptCharset: "UTF-8",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        cache: false,
        dataType: "html",
        url: "ERP_SendInvoice?Oper=TKT_M&CC_CURSO_ID=331", //ENVIA A UNA CLASE JAVA
        success: function (datos) {
            alert(datos);
//NO RECIBE RESPUESTA, VER COMO RECIBIR RESPUESTA DESDE UN SERVLET
        }
    });
}