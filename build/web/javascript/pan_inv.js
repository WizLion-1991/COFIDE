var itemIdInvPAN=0;function pan_inv(){}function initProdPan(){myLayout.close("west");myLayout.close("east");myLayout.close("south");myLayout.close("north");loadScrenProd();}function initSalidaPan(){myLayout.close("west");myLayout.close("east");myLayout.close("south");myLayout.close("north");loadScrenSalPan();}function loadScrenProd(){$.ajax({url:"PtoVtaInventarios.jsp",dataType:"html",scriptCharset:"utf-8",contentType:"application/x-www-form-urlencoded;charset=utf-8",success:function(datos){document.getElementById("MainPanel").innerHTML=datos;loadCategoriasInvP();InitJQGridInvP();}});}function loadScrenSalPan(){$.ajax({url:"PtoVtaInventarios2.jsp",dataType:"html",scriptCharset:"utf-8",contentType:"application/x-www-form-urlencoded;charset=utf-8",success:function(datos){document.getElementById("MainPanel").innerHTML=datos;loadCategoriasInvP();InitJQGridInvP();}});}function loadCategoriasInvP(){$.ajax({type:"POST",data:"",scriptCharset:"utf-8",contentType:"application/x-www-form-urlencoded;charset=utf-8",cache:false,dataType:"xml",url:"PtoVtaPanResource.jsp?id=1",success:function(datoVal){var objCte=datoVal.getElementsByTagName("categorias")[0];var lstdeta=objCte.getElementsByTagName("categoria");var strHtml="";for(i=0;i<lstdeta.length;i++){var obj=lstdeta[i];strHtml+='<button id="'+obj.getAttribute("id")+'" name="'+obj.getAttribute("id")+'" class="big_sel" onClick="viewCategoriaInvP('+obj.getAttribute("id")+')">'+obj.getAttribute("name")+"</button>";}document.getElementById("comand_button").innerHTML=strHtml;},error:function(objeto,quepaso,otroobj){alert(":categorias:"+objeto+" "+quepaso+" "+otroobj);}});}function viewCategoriaInvP(idValor){$.ajax({type:"POST",data:"categoria="+idValor+"&numCat=1",scriptCharset:"utf-8",contentType:"application/x-www-form-urlencoded;charset=utf-8",cache:false,dataType:"xml",url:"PtoVtaPanResource.jsp?id=10",success:function(datoVal){var objCte=datoVal.getElementsByTagName("categorias")[0];var lstdeta=objCte.getElementsByTagName("categoria");var strHtml="";var intSep=0;for(i=0;i<lstdeta.length;i++){var obj=lstdeta[i];intSep++;strHtml+='<button id="'+obj.getAttribute("id")+'" name="'+obj.getAttribute("id")+'" class="big_sel" onClick="AgregaInvP(this,'+obj.getAttribute("id")+","+obj.getAttribute("precio")+",'"+obj.getAttribute("name")+"')\">"+obj.getAttribute("name")+"</button>";if(intSep==8){strHtml+="<br>";intSep=0;}}strHtml+='<br><button id="Regresar_" name="Regresar_" class="big_sel" onClick="loadCategoriasInvP()">Regresar</button>';document.getElementById("comand_button").innerHTML=strHtml;},error:function(objeto,quepaso,otroobj){alert(":categorias:"+objeto+" "+quepaso+" "+otroobj);}});}function InitJQGridInvP(){jQuery("#list_deta").jqGrid({datatype:"clientSide",editurl:"_blank.jsp",height:"auto",colNames:["","Id","Descripcion","Cantidad",""],colModel:[{name:"TKTD_ID",index:"TKTD_ID",hidden:true,editable:false,sortable:false,search:false},{name:"TKTD_PR_ID",index:"TKTD_PR_ID",width:50,align:"left",sortable:false,search:true},{name:"TKTD_DESCRIPCION",index:"TKTD_DESCRIPCION",width:150,align:"left",sortable:false,search:true},{name:"TKTD_CANTIDAD",index:"TKTD_CANTIDAD",width:80,align:"right",editable:true,formatter:"number",sortable:false,search:false},{name:"TKTD_COSTO",index:"TKTD_COSTO",hidden:true,editable:false,sortable:false,search:false}],sortname:"TKTD_ID",sortorder:"desc",viewrecords:true,caption:"Items vendidos",footerrow:true,userDataOnFooter:true,imgpath:"jqGrid/themes/steel/images",onSelectRow:function(id){}});}function SalirInvP(){myLayout.open("west");myLayout.open("east");myLayout.open("south");myLayout.open("north");document.getElementById("MainPanel").innerHTML="";var strNomMain=objMap.getNomMain();var objMainFacPedi=objMap.getScreen(strNomMain);objMainFacPedi.bolActivo=false;objMainFacPedi.bolMain=false;objMainFacPedi.bolInit=false;objMainFacPedi.idOperAct=0;}function plusCantInvP(){document.getElementById("Cantidad").value=parseFloat(document.getElementById("Cantidad").value)+1;}function minusCantInvP(){document.getElementById("Cantidad").value=parseFloat(document.getElementById("Cantidad").value)-1;}function CancelarInvP(){d.getElementById("Total2").value="0";d.getElementById("Cantidad").value="1";loadCategoriasInvP();var grid=jQuery("#list_deta");grid.clearGridData();}function NewInvP(){d.getElementById("Total1").value="0";d.getElementById("Cantidad").value="1";loadCategoriasInvP();var grid=jQuery("#list_deta");grid.clearGridData();}function AgregaInvP(objeto,id,precio,descripcion){var datarow={TKTD_ID:id,TKTD_PR_ID:id,TKTD_DESCRIPCION:descripcion,TKTD_CANTIDAD:document.getElementById("Cantidad").value,TKTD_COSTO:precio};itemIdInvPAN++;jQuery("#list_deta").addRowData(itemIdInvPAN,datarow,"last");sumaImportesInv();document.getElementById("Cantidad").value=1;}function sumaImportesInv(){var dblSumaCant=0;var grid=jQuery("#list_deta");var arr=grid.getDataIDs();for(var i=0;i<arr.length;i++){var id=arr[i];var lstRow=grid.getRowData(id);dblSumaCant+=parseFloat(lstRow.TKTD_CANTIDAD);}document.getElementById("Total2").value=FormatNumber(dblSumaCant,1,true);}function BorrarItemInvP(){var grid=jQuery("#list_deta");var id=grid.getGridParam("selrow");if(id!=null){grid.delRowData(grid.getGridParam("selrow"));sumaImportesInv();}}function SaveInvP(tipo){var intTipo=0;var intSucSave=intSucDefa;var strFechaActual=trim(strHoyFecha);strFechaActual=Right(strFechaActual,2)+"/"+Mid(strFechaActual,5,2)+"/"+Left(strFechaActual,4);if(tipo=="1"){intTipo=1;}else{intTipo=2;}$("#dialogWait").dialog("open");var strPOST="INV_TIPO="+intTipo;strPOST+="&SC_ID="+intSucSave;strPOST+="&SC_ID2=0";strPOST+="&INV_FECHA="+strFechaActual;strPOST+="&INV_FOLIO=";strPOST+="&INV_NOTAS=";strPOST+="&INV_TRASORIGEN=0";strPOST+="&INV_TOT="+d.getElementById("Total2").value;strPOST+="&INV_TURNO="+d.getElementById("Turno").value;var grid=jQuery("#list_deta");var arr=grid.getDataIDs();var intC=0;for(var i=0;i<arr.length;i++){var id=arr[i];var lstRow=grid.getRowData(id);intC++;strPOST+="&MOVD_CANTIDAD"+intC+"="+lstRow.TKTD_CANTIDAD;strPOST+="&MOVD_DESCRIPCION"+intC+"="+(lstRow.TKTD_DESCRIPCION);strPOST+="&MOVD_IMPORTE"+intC+"="+(lstRow.TKTD_COSTO*lstRow.TKTD_CANTIDAD);strPOST+="&MOVD_CVE"+intC+"="+(lstRow.TKTD_DESCRIPCION);strPOST+="&MOVD_COSTO"+intC+"="+lstRow.TKTD_COSTO;strPOST+="&MOVD_PR_ID"+intC+"="+lstRow.TKTD_PR_ID;strPOST+="&MOVD_REQEXIST"+intC+"=0";strPOST+="&MOVD_EXIST"+intC+"=0";strPOST+="&MOVD_NOSERIE"+intC+"=";strPOST+="&MOVD_IMPORTEREAL"+intC+"=0";strPOST+="&MOVD_NOTAS"+intC+"=";}strPOST+="&COUNT_ITEM="+intC;$.ajax({type:"POST",data:encodeURI(strPOST),scriptCharset:"utf-8",contentType:"application/x-www-form-urlencoded;charset=utf-8",cache:false,dataType:"html",url:"InvMov.do?id=2",success:function(dato){dato=trim(dato);if(Left(dato,3)=="OK."){alert("Movimiento guardado...");$("#dialogWait").dialog("close");CancelarInvP();}else{alert(dato);$("#dialogWait").dialog("close");}},error:function(objeto,quepaso,otroobj){alert(":inventarios:"+objeto+" "+quepaso+" "+otroobj);$("#dialogWait").dialog("close");}});}