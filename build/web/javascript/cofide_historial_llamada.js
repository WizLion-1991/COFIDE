function cofide_historial_llamada() {

}
function initHCall() {
    LoadGridParams();
}
function LoadGridParams() {
    var itemIdCob = 0;
    var strCL_ID = "";
    var strCL_FECHA = "";
    var strCL_HORA = "";
    var strCL_USUARIO = "";
    var strID_CLIENTE = "";
    var strID_BASE = "";
    var strCL_EXITOSO = "";
    var strCL_DESCARTADO = "";
    var strCL_COMENTARIO = "";
    var strCL_CONTACTO = "";
    var intCount = 0;
    var intNumContacto = document.getElementById("CT_NO_CLIENTE").value;
    var strPost = "";
    strPost += "CT_NO_CLIENTE=" + intNumContacto;
    $.ajax(
            {
                type: "POST",
                data: strPost,
                scriptCharset: "UTF-8",
                contentType: "application/x-www-form-urlencoded;charset=utf-8",
                cache: false,
                dataType: "xml",
                url: "COFIDE_Historial.jsp?ID=1",
                success: function (datos) {
                    var lstXml = datos.getElementsByTagName("vta")[0];
                    var lstCte = lstXml.getElementsByTagName("datos");
                    for (var i = 0; i < lstCte.length; i++) {
                        var objcte = lstCte[i];
                        strCL_ID = objcte.getAttribute("CL_ID");
                        strCL_FECHA = objcte.getAttribute("CL_FECHA");
                        strCL_HORA = objcte.getAttribute("CL_HORA");
                        strCL_USUARIO = objcte.getAttribute("CL_USUARIO");
                        strID_CLIENTE = objcte.getAttribute("CL_ID_CLIENTE");
                        strCL_COMENTARIO = objcte.getAttribute("CL_COMENTARIO");
                        strCL_CONTACTO = objcte.getAttribute("CL_CONTACTO");
                        strID_BASE = objcte.getAttribute("CL_ID_BASE");
                        strCL_EXITOSO = objcte.getAttribute("CL_EXITOSO");
                        strCL_DESCARTADO = objcte.getAttribute("CL_DESCARTADO");
                        var datarow = {
                            CL_ID: strCL_ID,
                            CL_FECHA: strCL_FECHA,
                            CL_HORA: strCL_HORA,
                            CL_USUARIO: strCL_USUARIO,
                            CL_ID_CLIENTE: strID_CLIENTE,
                            CL_COMENTARIO: strCL_COMENTARIO,
                            CL_CONTACTO: strCL_CONTACTO,
                            CL_ID_BASE: strID_BASE,
                            CL_EXITOSO: strCL_EXITOSO,
                            CL_DESCARTADO: strCL_DESCARTADO
                        };
                        itemIdCob++;
                        jQuery("#GRIDHLLAMADA").addRowData(itemIdCob, datarow, "last");
                    }
                }
            }
    );
}