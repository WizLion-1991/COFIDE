function ctrlTabla(objPadre,borde,cellpadding,cellspacing,claseCss,name){this.objPadre=objPadre;this.borde=borde;this.cellpadding=cellpadding;this.cellspacing=cellspacing;this.claseCss=claseCss;this.strName=name;this.objTbody=null;this.objTabla=null;this.lstRenglones=new Array();this.size=-1;this.Medida="px";this.Ancho=-1;this.CreaTabla=function CreaTabla(){if(objPadre!=null){var tableContainer=document.createElement("table");var tableContainerTbody=document.createElement("tbody");tableContainer.border=this.borde;tableContainer.cellPadding=this.cellpadding;tableContainer.cellSpacing=this.cellspacing;tableContainer.setAttribute("class",this.claseCss);tableContainer.setAttribute("className",this.claseCss);tableContainer.setAttribute("id",this.strName);tableContainer.setAttribute("name",this.strName);if(this.Ancho!=-1&&(this.Medida=="px"||this.Medida=="%")){tableContainer.width=this.Ancho+this.Medida;}tableContainer.appendChild(tableContainerTbody);document.body.appendChild(tableContainer);objPadre.appendChild(tableContainer);this.objTbody=tableContainerTbody;this.objTabla=tableContainer;return tableContainerTbody;}else{alert("Falta definir objeto padre");return null;}};this.aniadeAtribute=function aniadeAtribute(){};this.AnadeFila=function AnadeFila(strName,strClase,strAlign,strJavaScript){var FilaNueva=new ctrlFila(strName,strClase,strAlign,strJavaScript);FilaNueva.AnadeFila();this.size++;this.lstRenglones[this.size]=FilaNueva;this.objTbody.appendChild(FilaNueva.objTR);return FilaNueva;};this.BorraFila=function BorraFila(intfila){if(intfila>this.size){alert("El indice esta fuera de rango");}else{this.lstRenglones.splice(intfila,1);this.size--;this.objTabla.deleteRow(intfila);}};this.BorraFilas=function BorraFilas(){var intTamanio=this.objTabla.rows.length;for(var i=0;i<intTamanio;i++){try{this.objTabla.deleteRow(0);}catch(err){alert("Error"+err.description);}}this.lstRenglones=new Array();this.size=-1;};this.DefineAncho=function DefineAncho(strTipoMedida,intValor){this.Medida=strTipoMedida;this.Ancho=intValor;};}function ctrlFila(strName,strClase,strAlign,strJavaScript){this.strName=strName;this.strClase=strClase;this.strAlineacion=strAlign;this.strJavaScript=strJavaScript;this.lstCeldas=new Array();this.size=-1;this.objTR=null;this.AnadeFila=function AnadeFila(){var nuevaFila=document.createElement("tr");if(this.strClase.length!=0){nuevaFila.setAttribute("id",this.strName);nuevaFila.setAttribute("name",this.strName);nuevaFila.setAttribute("class",this.strClase);nuevaFila.setAttribute("className",this.strClase);}this.objTR=nuevaFila;return nuevaFila;};this.AnadeCelda=function AnadeCelda(strName,strClase,strAlign,strJavaScript){var CeldaN=new ctrCelda(strName,strClase,strAlign,strJavaScript);CeldaN.AnadeCelda();this.size++;this.lstCeldas[this.size]=CeldaN;this.objTR.appendChild(CeldaN.objTD);return CeldaN;};this.BorrarRenglon=function BorrarRenglon(){for(var i=0;i<this.size;i++){var Celda=this.lstCeldas[i];alert(Celda.objTD.innerHTML);}};this.MuestraContenido=function MuestraContenido(){alert(this.objTR.innerHTML);};}function ctrCelda(strName,strClase,strAlign,strJavaScript){this.strName=strName;this.strClase=strClase;this.strAlineacion=strAlign;this.strJavaScript=strJavaScript;this.colSpan=0;this.objTD=null;this.AnadeCelda=function AnadeCelda(){var nuevaCelda=document.createElement("td");nuevaCelda.setAttribute("id",this.strName);nuevaCelda.setAttribute("name",this.strName);if(this.strClase.length!=0){nuevaCelda.setAttribute("class",this.strClase);nuevaCelda.setAttribute("className",this.strClase);}if(this.strAlineacion.length!=0){nuevaCelda.align=this.strAlineacion;}if(this.colSpan!=0){nuevaCelda.colSpan=this.colSpan;}this.objTD=nuevaCelda;return nuevaCelda;};this.AnadeCeldaContenido=function AnadeCeldaContenido(contenido){this.objTD.innerHTML=contenido;};this.AnadeCeldaContenidoApp=function AnadeCeldaContenidoApp(contenido){this.objTD.innerHTML=this.objTD.innerHTML+contenido;};this.AnadeCeldaControl=function AnadeCeldaControl(control){this.objTD.appendChild(control);};}