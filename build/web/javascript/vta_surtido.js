var bolSoyMainSurtMerc=false;var strNomMainSurteMerc=false;var strNomFormViewSurtMerc="";var strKeyViewSurtMerc="";var strTipoVtaViewSurtMerc="";var strNomOrderViewMerc="";var bolCambioFechaSurtMerc=false;var bolCamCantSurtMerc=false;var bolOpenFechas=false;var bolNomTabsSurtRecep="#tabsSURT_MER";var bolSurteMasivo=false;var bolModificaCantidad=false;var bolConfirmaCajas=false;var lstCajasSurte=null;var lstCajasMaster=null;intContaCajas1=-1;intContaCajas2=-1;var lastselBco=0;var lastselSurte=0;function vta_surtido(){}function InitSurtido(){document.getElementById("btn1").parentNode.setAttribute("class","Oculto");strNomMainSurteMerc=objMap.getNomMain();if(strNomMainSurteMerc=="SURT_MER"||strNomMainSurteMerc=="CONSIG_A"){bolSoyMainSurtMerc=true;}if(strNomMainSurteMerc=="CONSIG_A"){bolNomTabsSurtRecep="#tabsCONSIG_A";}else{}if(bolSoyMainSurtMerc){myLayout.close("west");myLayout.close("east");myLayout.close("south");myLayout.close("north");}if(strNomMainSurteMerc=="SURT_MER"){document.getElementById("VIEW_SURTIDA2").checked=true;}ActivaButtonsSurtido(false,false);$(bolNomTabsSurtRecep).tabs("disable",1);$("#dialogWait").dialog("open");$.ajax({type:"POST",data:"keys=69,171,439,440,441",scriptCharset:"utf-8",contentType:"application/x-www-form-urlencoded;charset=utf-8",cache:false,dataType:"xml",url:"Acceso.do",success:function(datos){var objsc=datos.getElementsByTagName("Access")[0];var lstKeys=objsc.getElementsByTagName("key");for(i=0;i<lstKeys.length;i++){var obj=lstKeys[i];if(obj.getAttribute("id")==69&&obj.getAttribute("enabled")=="true"){bolCambioFechaSurtMerc=true;}if(obj.getAttribute("id")==171&&obj.getAttribute("enabled")=="true"){bolCamCantSurtMerc=true;}if(obj.getAttribute("id")==439&&obj.getAttribute("enabled")=="true"){bolSurteMasivo=true;}if(obj.getAttribute("id")==440&&obj.getAttribute("enabled")=="true"){bolModificaCantidad=true;}if(obj.getAttribute("id")==441&&obj.getAttribute("enabled")=="true"){bolConfirmaCajas=true;}}if(bolCambioFechaSurtMerc){if($("#PD_FECHA").datepicker("isDisabled")){$("#PD_FECHA").datepicker("enable");}var objFecha=document.getElementById("PD_FECHA");objFecha.setAttribute("class","outEdit");objFecha.setAttribute("className","outEdit");objFecha.readOnly=false;}else{$("#PD_FECHA").datepicker("disable");}if(!bolCamCantSurtMerc){document.getElementById("PD_CANT").readOnly=true;document.getElementById("PD_CANT").readonly=true;document.getElementById("PD_CANT").setAttribute("class","READONLY");document.getElementById("PD_CANT").setAttribute("className","READONLY");}if(!bolSurteMasivo){document.getElementById("btn_RecAll1").style.display="none";}var strHtml="<ul>"+getMenuItem("CallbtnSurtMerc1();","Guardar Surtido","images/ptovta/CircleSave.png");if(bolModificaCantidad){strHtml+=getMenuItem("CallbtnSurtMerc3();","Editar cantidad","images/ptovta/VisQty.png");}strHtml+=getMenuItem("CallbtnSurtMerc2();","Salir","images/ptovta/exitBig.png");strHtml+="</ul>";document.getElementById("TOOLBARCOM").innerHTML=strHtml;$("#dialogWait").dialog("close");setTimeout("ViewDoSurtMerc();",3000);},error:function(objeto,quepaso,otroobj){alert(":Surtido Pedido:"+objeto+" "+quepaso+" "+otroobj);}});document.getElementById("btnSI").onclick=function(){ConfirmaSISurteMerca1();};document.getElementById("btnNO").onclick=function(){ConfirmaSISurteMerca2();};}function ConfirmaSISurteMerca1(){if(d.getElementById("Operac").value=="CHANGE_QTY"){CambioQtySurteMerca();}$("#SioNO").dialog("close");}function ConfirmaSISurteMerca2(){$("#SioNO").dialog("close");}function CambioQtySurteMerca(){var grid=jQuery("#PD_GRID");var id=grid.getGridParam("selrow");if(id!=null){var dblNvoQty=document.getElementById("_NvoQty").value;var lstRow=grid.getRowData(id);lstRow.PDD_RECIBIDO=dblNvoQty;grid.setRowData(id,lstRow);SetSumSurtMerc();}}function ViewDoSurtMerc(){bolSoyMainSurtMerc=false;strNomMainSurteMerc=objMap.getNomMain();if(strNomMainSurteMerc=="SURT_MER"||strNomMainSurteMerc=="CONSIG_A"){bolSoyMainSurtMerc=true;}var bolPasa=true;if(bolPasa){ActivaButtonsSurtido(true,true);var strPrefijoMasterOdc="PD";strNomOrderViewMerc="PD_ID";strNomFormViewSurtMerc="PEDSURT";strKeyViewSurtMerc="PD_ID";if(strNomMainSurteMerc=="CONSIG_A"){strNomFormViewSurtMerc="PEDCONS7";}var strParams="&"+strPrefijoMasterOdc+"_ANULADO=999";var strFecha1=document.getElementById("VIEWC_FECHA1").value;var strFecha2=document.getElementById("VIEWC_FECHA2").value;var strId=document.getElementById("VIEWC_ID").value;if(strId!="0"&&strId!=""){strParams+="&"+strKeyViewSurtMerc+"="+strId+"";}else{if(strFecha1!=""&&strFecha2!=""){strParams+="&"+strPrefijoMasterOdc+"_FECHA1="+strFecha1+"&"+strPrefijoMasterOdc+"_FECHA2="+strFecha2+"";}if(document.getElementById("VIEWC_SUCURSAL").value!="0"){strParams+="&SC_ID="+document.getElementById("VIEWC_SUCURSAL").value+"";}if(document.getElementById("VIEWC_CT_ID").value!="0"){strParams+="&PV_ID="+document.getElementById("VIEWC_CT_ID").value+"";}if(document.getElementById("VIEWC_FOLIO").value!=""){strParams+="&"+strPrefijoMasterOdc+"_FOLIO="+document.getElementById("VIEWC_FOLIO").value+"";}if(document.getElementById("VIEWC_EMP").value!="0"){strParams+="&EMP_ID="+document.getElementById("VIEWC_EMP").value+"";}if(strNomMainSurteMerc=="SURT_MER"){if(document.getElementById("VIEW_SURTIDA1").checked){strParams+="&PD_ES_SURTIDO=1";}else{strParams+="&PD_ES_SURTIDO=0";}if(document.getElementById("VIEW_CONSIGNACION1").checked){strParams+="&PD_CONSIGNACION=1";}else{strParams+="&PD_CONSIGNACION=0";}}}var grid=jQuery("#VIEWC_GRID1");grid.setGridParam({url:"CIP_TablaOp.jsp?ID=5&opnOpt="+strNomFormViewSurtMerc+"&_search=true"+strParams});grid.setGridParam({sortname:strNomOrderViewMerc}).trigger("reloadGrid");}}function resetGridViewSurtido(){var grid=jQuery("#VIEW_GRID1");grid.clearGridData();}function ActivaButtonsSurtido(bolSurte,bolPrint){if(bolSurte){document.getElementById("bt_SurtMercV2").style.display="block";}else{document.getElementById("bt_SurtMercV2").style.display="none";}if(bolPrint){document.getElementById("bt_SurtMercV1").style.display="block";}else{document.getElementById("bt_SurtMercV1").style.display="none";}}function SurtidoPrint(){var grid=jQuery("#VIEWC_GRID1");if(grid.getGridParam("selrow")!=null){var lstRow=grid.getRowData(grid.getGridParam("selrow"));var strHtml=CreaHidden("PD_ID",lstRow.PD_ID);if(strNomMainSurteMerc=="CONSIG_A"){openFormat("PEDIDO","PDF",strHtml);}else{openFormat("PED_CHECK","PDF",strHtml);}}}function Surtido(){var grid=jQuery("#VIEWC_GRID1");if(grid.getGridParam("selrow")!=null){var grid2=jQuery("#PD_GRID");grid2.clearGridData();document.getElementById("PD_TOT").value=0;$.ajax({type:"POST",data:"PD_ID="+grid.getGridParam("selrow"),scriptCharset:"utf-8",contentType:"application/x-www-form-urlencoded;charset=utf-8",cache:false,dataType:"xml",url:"VtasMov.do?id=8",success:function(datos){var objPedido=datos.getElementsByTagName("vta_pedido")[0];var lstdeta=objPedido.getElementsByTagName("deta");if(objPedido.getAttribute("PD_ANULADA")==0){if(objPedido.getAttribute("PD_ES_SURTIDO")==0){$(bolNomTabsSurtRecep).tabs("enable",1);$(bolNomTabsSurtRecep).tabs("option","active",1);DrawSurtPed(objPedido,lstdeta);$("#dialogView").dialog("close");}else{alert(lstMsg[114]);}}else{alert(lstMsg[51]);}},error:function(objeto,quepaso,otroobj){alert(":surtido 1:"+objeto+" "+quepaso+" "+otroobj);$("#dialogWait").dialog("close");}});}}function DrawSurtPed(objPedido,lstdeta){$("#dialogWait").dialog("open");document.getElementById("PD_ID").value=objPedido.getAttribute("PD_ID");document.getElementById("SC_ID").value=objPedido.getAttribute("SC_ID");document.getElementById("PD_FOLIO").value=objPedido.getAttribute("PD_FOLIO");document.getElementById("PD_OPER").value=strUserName;document.getElementById("PD_CANT").value="1";document.getElementById("PD_PROD").value="";lstCajasSurte=new Array();intContaCajas1=-1;ObtenNomCteSurtido(objPedido,lstdeta,true);}function ObtenNomCteSurtido(objPedido,lstdeta,bolPasaPedido){var intCte=objPedido.getAttribute("CT_ID");if(bolPasaPedido==undefined){bolPasaPedido=false;
}ValidaClean("PD_CLIE");$.ajax({type:"POST",data:"CT_ID="+intCte,scriptCharset:"utf-8",contentType:"application/x-www-form-urlencoded;charset=utf-8",cache:false,dataType:"xml",url:"VtasMov.do?id=9",success:function(datoVal){var objPov=datoVal.getElementsByTagName("vta_clientes")[0];if(objPov.getAttribute("CT_ID")==0){document.getElementById("PD_CLIE").value="***************";ValidaShow("CT_NOM",lstMsg[28]);}else{document.getElementById("PD_CLIE").value=objPov.getAttribute("CT_RAZONSOCIAL");}if(bolPasaPedido){DrawSurtPedDeta(objPedido,lstdeta);}},error:function(objeto,quepaso,otroobj){document.getElementById("PD_CLIE").value="***************";alert("Surtido 2:"+objeto+" "+quepaso+" "+otroobj);}});}function DrawSurtPedDeta(objPedido,lstdeta){for(i=0;i<lstdeta.length;i++){var obj=lstdeta[i];if(strNomMainSurteMerc=="CONSIG_A"){var dblCantidadRecibir=parseFloat(obj.getAttribute("PDD_CANTIDADSURTIDA"))-parseFloat(obj.getAttribute("PDD_CANT_FACT"));if(dblCantidadRecibir<0){dblCantidadRecibir=0;}if(dblCantidadRecibir>0){var datarowR={PDD_ID:obj.getAttribute("PDD_ID"),PDD_CVE:obj.getAttribute("PDD_CVE"),PDD_DESCRIPCION:obj.getAttribute("PDD_DESCRIPCION"),PDD_CANTIDAD:dblCantidadRecibir,PDD_RECIBIDO:0,PDD_COSTO:obj.getAttribute("PDD_COSTO"),PDD_NOTAS:obj.getAttribute("PDD_NOTAS"),PDD_PR_ID:obj.getAttribute("PR_ID"),PDD_FACTOR:0,PDD_USA_SERIE:0,PDD_SERIES:""};jQuery("#PD_GRID").addRowData(obj.getAttribute("PDD_ID"),datarowR,"last");}}else{var dblCantidadXSurtir=obj.getAttribute("PDD_CANTIDAD")-obj.getAttribute("PDD_CANTIDADSURTIDA");if(dblCantidadXSurtir<0){dblCantidadXSurtir=0;}if(dblCantidadXSurtir>0){var datarow={PDD_ID:obj.getAttribute("PDD_ID"),PDD_CVE:obj.getAttribute("PDD_CVE"),PDD_DESCRIPCION:obj.getAttribute("PDD_DESCRIPCION"),PDD_CANTIDAD:dblCantidadXSurtir,PDD_RECIBIDO:0,PDD_COSTO:obj.getAttribute("PDD_COSTO"),PDD_NOTAS:obj.getAttribute("PDD_NOTAS"),PDD_PR_ID:obj.getAttribute("PR_ID"),PDD_FACTOR:0,PDD_USA_SERIE:0,PDD_SERIES:""};jQuery("#PD_GRID").addRowData(obj.getAttribute("PDD_ID"),datarow,"last");}}}SurteExisteNumSerie();}function CallbtnSurtMerc1(){SaveSurtMerc();}function CallbtnSurtMerc2(){var dblTot=parseFloat(d.getElementById("PD_TOT").value);var bolSalir=true;if(dblTot>0){if(strNomMainSurteMerc=="CONSIG_A"){bolSalir=confirm(lstMsg[164]);}else{bolSalir=confirm(lstMsg[121]);}}if(bolSalir){$(bolNomTabsSurtRecep).tabs("option","active",0);$(bolNomTabsSurtRecep).tabs("disable",1);}}function CallbtnSurtMerc3(){var grid=jQuery("#PD_GRID");if(grid.getGridParam("selrow")!=null){var lstRow=grid.getRowData(grid.getGridParam("selrow"));document.getElementById("Operac").value="CHANGE_QTY";$("#SioNO").dialog("option","title",lstMsg[79]);var div=document.getElementById("SioNO_inside");var strHtml=CreaTexto(lstMsg[8],"_NvoQty",lstRow.PDD_RECIBIDO,10,10,true,false,"","left",0,"","","",false,1);div.innerHTML=strHtml;$("#SioNO").dialog("open");}}function AddItemEvtSurtMerc(event,obj){if(event.keyCode==13){AddItemSurtMerc();}}function AddItemSurtMerc(){var strCod=UCase(d.getElementById("PD_PROD").value);var strCant=UCase(d.getElementById("PD_CANT").value);var intCant=0;var grid=jQuery("#PD_GRID");try{intCant=parseFloat(strCant);}catch(err){}if(trim(strCod)!=""){if(strPrefCodBar!=""){strCod=strCod.replace(strPrefCodBar,"");}$("#dialogWait").dialog("open");var bolEncontro=false;var idProd=0;var arr=grid.getDataIDs();for(var i=0;i<arr.length;i++){var id=arr[i];var lstRowAct=grid.getRowData(id);if(lstRowAct.PDD_CVE==strCod){var dblDiff=parseFloat(lstRowAct.PDD_CANTIDAD)-(parseFloat(lstRowAct.PDD_RECIBIDO)+parseFloat(intCant));bolEncontro=true;if(dblDiff>=0){idProd=id;break;}}}if(bolEncontro){var idUpdate=0;if(idProd==0){alert(lstMsg[115]);}else{idUpdate=idProd;}if(idUpdate!=0){var lstRowUpdate=grid.getRowData(idUpdate);var bolCumpleExist=false;if(strNomMainSurteMerc=="CONSIG_A"){bolCumpleExist=true;}else{if(parseInt(lstRowUpdate.PDD_REQEXIST)==1){if((parseFloat(lstRowUpdate.PDD_RECIBIDO)+parseFloat(intCant))<=parseFloat(lstRowUpdate.PDD_EXIST)){bolCumpleExist=true;}else{alert(lstMsg[135]);}}else{bolCumpleExist=true;}}if(bolCumpleExist){if(lstRowUpdate.PDD_USA_SERIE==1){var Pr_Id=lstRowUpdate.PDD_PR_ID;var dblCantidad=document.getElementById("PD_CANT").value;var strCod=lstRowUpdate.PDD_CVE;var dblExistencia=lstRowUpdate.PDD_EXIST;var Ct_Id="algo";var strSeries=lstRowUpdate.PDD_SERIES;if(document.getElementById("PD_ESCANEO1").checked==false){_drawScNoSerieSurtido(Pr_Id,Ct_Id,dblCantidad,strCod,dblExistencia,idUpdate,strSeries);}else{DrawCapturaSerieSurtido(lstRowUpdate.PDD_CVE,lstRowUpdate.PDD_DESCRIPCION,intCant,idUpdate);}}else{lstRowUpdate.PDD_RECIBIDO=parseFloat(lstRowUpdate.PDD_RECIBIDO)+intCant;grid.setRowData(idUpdate,lstRowUpdate);d.getElementById("PD_PROD").value="";d.getElementById("PD_CANT").value="1";SetSumSurtMerc();d.getElementById("PD_PROD").focus();intContaCajas1++;var objCaja=new _ClassSurtidoCaja();objCaja.PDD_ID=lstRowUpdate.PDD_ID;objCaja.PDD_PR_ID=lstRowUpdate.PDD_PR_ID;objCaja.PDD_CANTIDAD=intCant;objCaja.PDD_CAJA=d.getElementById("PD_CAJA").value;objCaja.PDD_SERIES="";lstCajasSurte[intContaCajas1]=objCaja;document.getElementById("PD_PROD").focus();}}}}else{alert(lstMsg[81]);d.getElementById("PD_PROD").focus();}$("#dialogWait").dialog("close");}else{alert(lstMsg[82]);d.getElementById("PD_PROD").focus();}}function SetSumSurtMerc(){var grid=jQuery("#PD_GRID");var arr=grid.getDataIDs();var dblSuma=0;var dblSumaEsperado=0;for(var i=0;i<arr.length;i++){var id=arr[i];var lstRow=grid.getRowData(id);if(parseFloat(lstRow.PDD_RECIBIDO)>parseFloat(lstRow.PDD_CANTIDAD)){alert("La cantidad es mayor a la solicitada en el pedido");var datarow={PDD_RECIBIDO:0};grid.setRowData(lstRow.PDD_ID,datarow);}else{dblSuma+=parseFloat(lstRow.PDD_RECIBIDO);dblSumaEsperado+=parseFloat(lstRow.PDD_CANTIDAD);}}d.getElementById("PD_TOT").value=dblSuma;if(dblSuma>=dblSumaEsperado){alert(lstMsg[123]);}}function dblClickSurtido(id){Surtido();}function SaveSurtMerc(){var dblTot=parseFloat(d.getElementById("PD_TOT").value);if(dblTot>0){var bolGuardar_=false;if(strNomMainSurteMerc=="CONSIG_A"){bolGuardar_=confirm(lstMsg[163]);}else{bolGuardar_=confirm(lstMsg[122]);}if(bolGuardar_){lstCajasMaster=new Array();intContaCajas2=-1;if(strNomMainSurteMerc!="CONSIG_A"&&bolConfirmaCajas){DrawCajasConfirma();}else{if(!bolConfirmaCajas){var intMaxCaja=0;for(var ik=0;ik<=intContaCajas1;ik++){var objCaja=lstCajasSurte[ik];if(parseInt(objCaja.PDD_CAJA)>intMaxCaja){intMaxCaja=parseInt(objCaja.PDD_CAJA);}}intContaCajas2=-1;for(var ik2=1;ik2<=intMaxCaja;ik2++){intContaCajas2++;var objCaja2=new _ClassSurtidoCajaM();objCaja2.PD_ID=document.getElementById("PD_ID").value;objCaja2.NUMERO=ik2;objCaja2.ALTO=0;objCaja2.ANCHO=0;objCaja2.LARGO=0;objCaja2.PESO=0;lstCajasMaster[intContaCajas2]=objCaja2;}}SaveSurtMercDo();}}}else{alert(lstMsg[85]);}}function SaveSurtMercDo(){$("#dialogWait").dialog("open");var strPOST="";strPOST+="PD_ID="+d.getElementById("PD_ID").value;strPOST+="&PD_FECHA="+d.getElementById("PD_FECHA").value;var grid=jQuery("#PD_GRID");var arr=grid.getDataIDs();var intC=0;for(var i=0;i<arr.length;i++){var id=arr[i];var lstRow=grid.getRowData(id);intC++;strPOST+="&PDD_ID"+intC+"="+lstRow.PDD_ID;strPOST+="&PR_ID"+intC+"="+lstRow.PDD_PR_ID;strPOST+="&PDD_CVE"+intC+"="+lstRow.PDD_CVE;strPOST+="&PDD_COSTO"+intC+"="+lstRow.PDD_COSTO;strPOST+="&PDD_CANTIDADSURTIDA"+intC+"="+lstRow.PDD_RECIBIDO;strPOST+="&PDD_FACTOR"+intC+"="+lstRow.PDD_FACTOR;strPOST+="&PDD_SERIES"+intC+"="+lstRow.PDD_SERIES;strPOST+="&PDD_USASERIE"+intC+"="+lstRow.PDD_USA_SERIE;}if(strNomMainSurteMerc!="CONSIG_A"){var intContaCajasT=0;for(var ik=0;ik<=intContaCajas1;ik++){var objCaja=lstCajasSurte[ik];intContaCajasT++;strPOST+="&cPDD_ID"+ik+"="+objCaja.PDD_ID;strPOST+="&cPDD_PR_ID"+ik+"="+objCaja.PDD_PR_ID;strPOST+="&cPDD_CAJA"+ik+"="+objCaja.PDD_CAJA;strPOST+="&cPDD_CANTIDAD"+ik+"="+objCaja.PDD_CANTIDAD;strPOST+="&cPDD_SERIES"+ik+"="+objCaja.PDD_SERIES;}strPOST+="&cPDD_CONTA_CAJAS="+intContaCajasT;var intContaCajasM=0;for(var ik2=0;ik2<=intContaCajas2;
ik2++){var objCaja2=lstCajasMaster[ik2];intContaCajasM++;strPOST+="&mPD_ID"+ik2+"="+objCaja2.PD_ID;strPOST+="&mNUMERO"+ik2+"="+objCaja2.NUMERO;strPOST+="&mALTO"+ik2+"="+objCaja2.ALTO;strPOST+="&mANCHO"+ik2+"="+objCaja2.ANCHO;strPOST+="&mLARGO"+ik2+"="+objCaja2.LARGO;strPOST+="&mPESO"+ik2+"="+objCaja2.PESO;}strPOST+="&mPDD_CONTA_CAJAS="+intContaCajasM;}if(strNomMainSurteMerc=="CONSIG_A"){strPOST+="&ES_RECEP_CONS=1";}else{strPOST+="&ES_RECEP_CONS=0";}strPOST+="&COUNT_ITEM="+intC;$.ajax({type:"POST",data:encodeURI(strPOST),scriptCharset:"utf-8",contentType:"application/x-www-form-urlencoded;charset=utf-8",cache:false,dataType:"html",url:"VtasMov.do?id=20",success:function(dato){dato=trim(dato);if(Left(dato,3)=="OK."){d.getElementById("PD_TOT").value="0";CallbtnSurtMerc2();jQuery("#VIEWC_GRID1").trigger("reloadGrid");var strHtml2=CreaHidden("MP_ID",dato.replace("OK.",""));if(strNomMainSurteMerc=="CONSIG_A"){openFormat("PEDRECP1","PDF",strHtml2,document.getElementById("PD_FOLIO").value);}else{openFormat("PEDSURT","PDF",strHtml2,document.getElementById("PD_FOLIO").value);openFormat("PEDPACK","PDF",strHtml2,document.getElementById("PD_FOLIO").value);}}else{alert(dato);}$("#dialogWait").dialog("close");},error:function(objeto,quepaso,otroobj){alert(":Surtido Save:"+objeto+" "+quepaso+" "+otroobj);$("#dialogWait").dialog("close");}});}function SurteExit(){myLayout.open("west");myLayout.open("east");myLayout.open("south");myLayout.open("north");document.getElementById("MainPanel").innerHTML="";var objMainFacPedi=objMap.getScreen(strNomMainSurteMerc);objMainFacPedi.bolActivo=false;objMainFacPedi.bolMain=false;objMainFacPedi.bolInit=false;objMainFacPedi.idOperAct=0;}function EvalItemEvtSurtMerc(event,obj){if(event.keyCode==13){EvalitemSurtMerc();}}function EvalitemSurtMerc(){$("#dialogWait").dialog("open");var strCod=UCase(d.getElementById("PD_PROD").value);if(strPrefCodBar!=""){strCod=strCod.replace(strPrefCodBar,"");}$.ajax({type:"POST",data:"PR_CODIGO="+strCod+"&SC_ID="+d.getElementById("SC_ID").value,scriptCharset:"utf-8",contentType:"application/x-www-form-urlencoded;charset=utf-8",cache:false,dataType:"xml",url:"VtasMov.do?id=5",success:function(datoVal){var objProd=datoVal.getElementsByTagName("vta_productos")[0];var Pr_Id=0;if(objProd!=undefined){Pr_Id=objProd.getAttribute("PR_ID");d.getElementById("PD_DESC").value=objProd.getAttribute("PR_DESCRIPCION");d.getElementById("PD_PROD").value=objProd.getAttribute("PR_CODIGO");}if(Pr_Id==0){alert(lstMsg[0]);document.getElementById("PD_PROD").focus();}else{if(objProd.getAttribute("PR_INVENTARIO")==1){AddItemSurtMerc();}else{alert(lstMsg[196]);}}$("#dialogWait").dialog("close");},error:function(objeto,quepaso,otroobj){alert(":Surtido Eval 4:"+objeto+" "+quepaso+" "+otroobj);$("#dialogWait").dialog("close");}});}function DrawCapturaSerieSurtido(strCodigo,strDescripcion,intCantidad,idUpdate){$("#dialogInv").dialog("open");$("#dialogInv").dialog("option","title",lstMsg[107]);var strHTML="<table border=0 cellpadding=0>";strHTML+="<tr>";strHTML+="<td colspan=2>"+lstMsg[110]+"</td>";strHTML+="</tr>";strHTML+="<tr>";var intContaF=0;for(var i=1;i<=intCantidad;i++){if(i==intCantidad){strHTML+="<td>"+CreaTexto("","_Serie"+i,"",30,30,false,false,"ConfirmNoSerie1","left",0,"",""," onkeydown='EvalSerieNextSurtMerc(event,this,"+idUpdate+","+intCantidad+")'",false,3,"")+"</td>";}else{strHTML+="<td>"+CreaTexto("","_Serie"+i,"",30,30,false,false,"_Serie"+(i+1),"left",0,"",""," onblur='EvalSerieSurtMercScNext(event,this,"+idUpdate+","+intCantidad+")'",false,3,"")+"</td>";}intContaF++;if(intContaF==10){strHTML+="</tr>";strHTML+="<tr>";intContaF=0;}}strHTML+="</tr>";strHTML+="<tr>";strHTML+="<td>"+CreaBoton("","ConfirmNoSerie1",lstMsg[111],"SurtMercConfirmNumSerie("+idUpdate+","+intCantidad+");","left",false,false)+"</td>";strHTML+="<td>&nbsp;</td>";strHTML+="<td>"+CreaBoton("","CancelNoSerie2",lstMsg[112],"SurtMercCancelNumSerie("+idUpdate+","+intCantidad+");","left",false,false)+"</td>";strHTML+="</tr>";strHTML+="</table>";document.getElementById("dialogInv_inside").innerHTML=strHTML;setTimeout("document.getElementById('_Serie1').focus();",1000);}function EvalSerieNextSurtMerc(event,obj,idUpdate,intCantidad){if(event.keyCode==13){SurtMercConfirmNumSerie(idUpdate,intCantidad);}}function EvalSerieSurtMercScNext(event,obj,idUpdate,intCantidad){if(event.keyCode==13){SurtMercEvalNumSerie(idUpdate,intCantidad);}}function SurteExisteNumSerie(){$("#dialogWait").dialog("open");var lstIds="";var grid=jQuery("#PD_GRID");var arr=grid.getDataIDs();for(var i=0;i<arr.length;i++){var id=arr[i];var lstRow=grid.getRowData(id);lstIds+=lstRow.PDD_PR_ID+",";}$.ajax({type:"POST",data:"lstIds="+lstIds,scriptCharset:"utf-8",contentType:"application/x-www-form-urlencoded;charset=utf-8",cache:false,dataType:"xml",url:"InvMov.do?id=18",success:function(datos){var grid=jQuery("#PD_GRID");var arr=grid.getDataIDs();var objCodBar=datos.getElementsByTagName("codigo_barras")[0];var lstProd=objCodBar.getElementsByTagName("cb");for(i=0;i<lstProd.length;i++){var obj=lstProd[i];for(var j=0;j<arr.length;j++){var id=arr[j];var lstRow=grid.getRowData(id);if(parseInt(lstRow.PDD_PR_ID)==parseInt(obj.getAttribute("PR_ID"))){if(parseInt(obj.getAttribute("USO"))==1){lstRow.PDD_USA_SERIE=1;}lstRow.PDD_EXIST=obj.getAttribute("EXIST");lstRow.PDD_REQEXIST=obj.getAttribute("REQEXIST");grid.setRowData(id,lstRow);}}}$("#dialogWait").dialog("close");document.getElementById("PD_PROD").focus();$("#dialogWait").dialog("close");},error:function(objeto,quepaso,otroobj){alert(":Surtido Serie:"+objeto+" "+quepaso+" "+otroobj);$("#dialogWait").dialog("close");}});}function SurtMercConfirmNumSerie(idUpdate,intCantidad){var grid=jQuery("#PD_GRID");var lstRow=grid.getRowData(idUpdate);var intCont=0;var strlstSeries="";for(var i=1;i<=intCantidad;i++){if(document.getElementById("_Serie"+i).value==""||document.getElementById("_Serie"+i).value.length==0){}else{strlstSeries+=document.getElementById("_Serie"+i).value+",";intCont++;}}trim(strlstSeries);intCantidad=intCont;$("#dialogWait").dialog("open");if(strNomMainSurteMerc=="CONSIG_A"){if(strlstSeries!=""){$.ajax({type:"POST",data:"PR_ID="+lstRow.PDD_PR_ID+"&"+"SERIE="+strlstSeries,scriptCharset:"utf-8",contentType:"application/x-www-form-urlencoded;charset=utf-8",cache:false,dataType:"html",url:"InvMov.do?id=19",success:function(dato){dato=trim(dato);$("#dialogWait").dialog("close");if(Left(dato,2)=="OK"){lstRow.PDD_RECIBIDO=parseFloat(lstRow.PDD_RECIBIDO)+intCantidad;lstRow.PDD_SERIES=lstRow.PDD_SERIES+strlstSeries;grid.setRowData(idUpdate,lstRow);d.getElementById("PD_PROD").value="";d.getElementById("PD_CANT").value="1";SetSumSurtMerc();d.getElementById("PD_PROD").focus();$("#dialogInv").dialog("close");return true;}else{alert(dato);return false;}},error:function(objeto,quepaso,otroobj){alert(":Surtido Eval4:"+objeto+" "+quepaso+" "+otroobj);$("#dialogWait").dialog("close");return false;}});}else{$("#dialogWait").dialog("close");$("#dialogInv").dialog("close");d.getElementById("PD_PROD").value="";d.getElementById("PD_CANT").value="1";d.getElementById("PD_PROD").focus();}}else{if(strlstSeries!=""){$.ajax({type:"POST",data:"PR_ID="+lstRow.PDD_PR_ID+"&"+"SERIE="+strlstSeries,scriptCharset:"utf-8",contentType:"application/x-www-form-urlencoded;charset=utf-8",cache:false,dataType:"html",url:"InvMov.do?id=20",success:function(dato){dato=trim(dato);$("#dialogWait").dialog("close");if(Left(dato,2)=="OK"){lstRow.PDD_RECIBIDO=parseFloat(lstRow.PDD_RECIBIDO)+intCantidad;lstRow.PDD_SERIES=lstRow.PDD_SERIES+strlstSeries;grid.setRowData(idUpdate,lstRow);d.getElementById("PD_PROD").value="";d.getElementById("PD_CANT").value="1";SetSumSurtMerc();d.getElementById("PD_PROD").focus();$("#dialogInv").dialog("close");intContaCajas1++;var objCaja=new _ClassSurtidoCaja();objCaja.PDD_ID=lstRow.PDD_ID;objCaja.PDD_PR_ID=lstRow.PDD_PR_ID;objCaja.PDD_CANTIDAD=intCantidad;objCaja.PDD_CAJA=d.getElementById("PD_CAJA").value;objCaja.PDD_SERIES=strlstSeries;lstCajasSurte[intContaCajas1]=objCaja;
return true;}else{alert(dato);return false;}},error:function(objeto,quepaso,otroobj){alert(":Surtido Eval4:"+objeto+" "+quepaso+" "+otroobj);$("#dialogWait").dialog("close");return false;}});}else{$("#dialogWait").dialog("close");$("#dialogInv").dialog("close");d.getElementById("PD_PROD").value="";d.getElementById("PD_CANT").value="1";d.getElementById("PD_PROD").focus();}}return false;}function SurtMercCancelNumSerie(){$("#dialogInv").dialog("close");}function SurtMercEvalNumSerie(idUpdate,intCantidad){var grid=jQuery("#PD_GRID");var lstRow=grid.getRowData(idUpdate);var strlstSeries="";for(var i=1;i<=intCantidad;i++){strlstSeries+=document.getElementById("_Serie"+i).value+",";if(document.getElementById("_Serie"+i).value!=""){for(var j=1;j<=intCantidad;j++){if(i!=j){if(document.getElementById("_Serie"+i).value==document.getElementById("_Serie"+j).value){alert(lstMsg[116]+":"+document.getElementById("_Serie"+j).value);return false;}}}}}$("#dialogWait").dialog("open");$.ajax({type:"POST",data:"PR_ID="+lstRow.PDD_PR_ID+"&SERIE="+strlstSeries,scriptCharset:"utf-8",contentType:"application/x-www-form-urlencoded;charset=utf-8",cache:false,dataType:"html",url:"InvMov.do?id=20",success:function(datos){if(trim(datos)!="OK"){alert(datos);$("#dialogWait").dialog("close");return false;}$("#dialogWait").dialog("close");},error:function(objeto,quepaso,otroobj){alert(":Recepcion Compras{Recep Prod3}:"+objeto+" "+quepaso+" "+otroobj);$("#dialogWait").dialog("close");}});}function FechasSurt(){var bolSelecciono=false;var grid=jQuery("#VIEWC_GRID1");if(grid.getGridParam("selrow")!=null){bolSelecciono=true;}if(bolSelecciono){var id=grid.getGridParam("selrow");var lstRow=grid.getRowData(id);document.getElementById("HD_PD_ID").value=lstRow.PD_ID;if(bolOpenFechas){setTimeout("InitFechas()",1000);$("#dialogCancelar").dialog("open");}else{setTimeout("InitFechas()",1000);OpnOpt("FECSURT",null,"dialogCancelar",false,false,true);}}else{alert("SELECCIONA UN PEDIDO!");}}function RecepcionPed7(){var grid=jQuery("#VIEWC_GRID1");if(grid.getGridParam("selrow")!=null){var grid2=jQuery("#PD_GRID");grid2.clearGridData();$.ajax({type:"POST",data:"PD_ID="+grid.getGridParam("selrow"),scriptCharset:"utf-8",contentType:"application/x-www-form-urlencoded;charset=utf-8",cache:false,dataType:"xml",url:"VtasMov.do?id=8",success:function(datos){var objPedido=datos.getElementsByTagName("vta_pedido")[0];var lstdeta=objPedido.getElementsByTagName("deta");if(objPedido.getAttribute("PD_ANULADA")==0){var bolCumpleEstatus=true;if(objPedido.getAttribute("PD_STATUS")!=2&&objPedido.getAttribute("PD_STATUS")!=3&&objPedido.getAttribute("PD_STATUS")!=9){bolCumpleEstatus=false;alert(lstMsg[124]);}if(bolCumpleEstatus){$(bolNomTabsSurtRecep).tabs("enable",1);$(bolNomTabsSurtRecep).tabs("option","active",1);DrawSurtPed(objPedido,lstdeta);$("#dialogView").dialog("close");}else{alert(lstMsg[114]);}}else{alert(lstMsg[51]);}},error:function(objeto,quepaso,otroobj){alert(":surtido 1:"+objeto+" "+quepaso+" "+otroobj);$("#dialogWait").dialog("close");}});}}function DrawCajasConfirma(){var intMaxCaja=0;for(var ik=0;ik<=intContaCajas1;ik++){var objCaja=lstCajasSurte[ik];if(parseInt(objCaja.PDD_CAJA)>intMaxCaja){intMaxCaja=parseInt(objCaja.PDD_CAJA);}}$("#dialogInv").dialog("open");$("#dialogInv").dialog("option","title",lstMsg[166]);var strHTML="<table border=0 cellpadding=0>";strHTML+="<tr>";strHTML+="<td >"+lstMsg[167]+"</td>";strHTML+="<td >"+lstMsg[168]+"</td>";strHTML+="<td >"+lstMsg[169]+"</td>";strHTML+="<td >"+lstMsg[170]+"</td>";strHTML+="<td >"+lstMsg[171]+"</td>";strHTML+="</tr>";strHTML+="<tr>";for(var i=1;i<=intMaxCaja;i++){strHTML+="<tr>";strHTML+="<td >"+i+"</td>";strHTML+="<td >"+CreaTexto("","_AltoC"+i,"0",10,10,true,false,"_AnchoC"+(i),"left",0,"","","",false,3,"")+"</td>";strHTML+="<td >"+CreaTexto("","_AnchoC"+i,"0",10,10,true,false,"_LargoC"+(i),"left",0,"","","",false,3,"")+"</td>";strHTML+="<td >"+CreaTexto("","_LargoC"+i,"0",10,10,true,false,"_PesoC"+(i),"left",0,"","","",false,3,"")+"</td>";if(i==intMaxCaja){strHTML+="<td >"+CreaTexto("","_PesoC"+i,"0",10,10,true,false,"ConfirmCajas1","left",0,"","","",false,3,"")+"</td>";}else{strHTML+="<td >"+CreaTexto("","_PesoC"+i,"0",10,10,true,false,"_AltoC"+(i+1),"left",0,"","","",false,3,"")+"</td>";}strHTML+="</tr>";}strHTML+="</tr>";strHTML+="<tr>";strHTML+="<td>"+CreaBoton("","ConfirmCajas1",lstMsg[111],"CajasConfirm1();","left",false,false)+"</td>";strHTML+="<td>&nbsp;</td>";strHTML+="<td>"+CreaBoton("","ConfirmCajas2",lstMsg[112],"CajasConfirm3();","left",false,false)+"</td>";strHTML+="</tr>";strHTML+="</table>";document.getElementById("dialogInv_inside").innerHTML=strHTML;setTimeout("document.getElementById('_AltoC1').focus();",1000);}function CajasConfirm1(){var intMaxCaja=0;for(var ik=0;ik<=intContaCajas1;ik++){var objCaja=lstCajasSurte[ik];if(parseInt(objCaja.PDD_CAJA)>intMaxCaja){intMaxCaja=parseInt(objCaja.PDD_CAJA);}}for(var ik2=1;ik2<=intMaxCaja;ik2++){if(parseFloat(document.getElementById("_AltoC"+ik2).value)==0){alert("Es Necesario capturar el alto");document.getElementById("_AltoC"+ik2).focus();return false;}if(parseFloat(document.getElementById("_AnchoC"+ik2).value)==0){alert("Es Necesario capturar el ancho");document.getElementById("_AnchoC"+ik2).focus();return false;}if(parseFloat(document.getElementById("_LargoC"+ik2).value)==0){alert("Es Necesario capturar el largo");document.getElementById("_LargoC"+ik2).focus();return false;}if(parseFloat(document.getElementById("_PesoC"+ik2).value)==0){alert("Es Necesario capturar el peso");document.getElementById("_PesoC"+ik2).focus();return false;}}intContaCajas2=-1;for(var ik2=1;ik2<=intMaxCaja;ik2++){intContaCajas2++;var objCaja2=new _ClassSurtidoCajaM();objCaja2.PD_ID=document.getElementById("PD_ID").value;objCaja2.NUMERO=ik2;objCaja2.ALTO=document.getElementById("_AltoC"+ik2).value;objCaja2.ANCHO=document.getElementById("_AnchoC"+ik2).value;objCaja2.LARGO=document.getElementById("_LargoC"+ik2).value;objCaja2.PESO=document.getElementById("_PesoC"+ik2).value;lstCajasMaster[intContaCajas2]=objCaja2;}$("#dialogInv").dialog("close");SaveSurtMercDo();return true;}function CajasConfirm3(){$("#dialogInv").dialog("close");}function _ClassSurtidoCaja(){this.PDD_ID=0;this.PDD_PR_ID=0;this.PDD_CANTIDAD=0;this.PDD_CAJA=0;this.PDD_SERIES=0;}function _ClassSurtidoCajaM(){this.PD_ID=0;this.NUMERO=0;this.ALTO=0;this.ANCHO=0;this.LARGO=0;this.PESO=0;}function _drawScNoSerieSurtido(Pr_Id,Ct_Id,intCantidad,strCod,dblExist,idProd,strSeries){if(strSeries==null){strSeries="";}var lstSeries=strSeries.split(",");$.ajax({type:"POST",data:"PR_ID="+Pr_Id,scriptCharset:"utf-8",contentType:"application/x-www-form-urlencoded;charset=utf-8",cache:false,dataType:"xml",url:"InvMov.do?id=21",success:function(datos){var objCodBar=datos.getElementsByTagName("num_series")[0];var lstProd=objCodBar.getElementsByTagName("serie");var strOptionSelect="";for(i=0;i<lstProd.length;i++){var obj=lstProd[i];var boolExisteSerie=false;for(var y=0;y<lstSeries.length;y++){var objSerie=lstSeries[y];if(objSerie==obj.getAttribute("NO_SERIE")){boolExisteSerie=true;}}if(!boolExisteSerie){strOptionSelect+="<option value='"+obj.getAttribute("NO_SERIE")+"'>"+obj.getAttribute("NO_SERIE")+"</option>";}}$("#dialog2").dialog("open");$("#dialog2").dialog("option","title",lstMsg[107]);var strHTML="<input type='hidden' id='_Pr_Id' value='"+Pr_Id+"'>";strHTML+="<input type='hidden' id='_Ct_Id' value='"+Ct_Id+"'>";strHTML+="<input type='hidden' id='_Cantidad' value='"+intCantidad+"'>";strHTML+="<input type='hidden' id='_strCod' value='"+strCod+"'>";strHTML+="<input type='hidden' id='_dblExist' value='"+dblExist+"'>";strHTML+="<input type='hidden' id='_intDevo' value='"+""+"'>";strHTML+="<input type='hidden' id='_idProd' value='"+idProd+"'>";strHTML+="<table border=0 cellpadding=0>";strHTML+="<tr>";strHTML+="<td colspan=3>"+lstMsg[110]+"</td>";strHTML+="</tr>";strHTML+="<tr>";strHTML+="<td nowrap>&nbsp;"+lstMsg[172]+"<input type='text' id='search_cant' value='"+intCantidad+"' size='8' readonly disabled></td>";
strHTML+="<td nowrap>&nbsp;</td>";strHTML+="<td nowrap>&nbsp;"+lstMsg[173]+"<input type='text' id='search_cant_sel' value='0' size='8' readonly disabled></td>";strHTML+="</tr>";strHTML+="<tr>";strHTML+="<td >"+lstMsg[174]+"<br><select id='series_origen' multiple>"+strOptionSelect+"</select></td>";strHTML+="<td ><input type='button' id='Agregar' value='"+lstMsg[176]+"' onclick='AgregaSerieX()'><br><input type='button' id='Quitar' value='"+lstMsg[177]+"' onClick='RemueveSerieX()'></td>";strHTML+="<td >"+lstMsg[175]+"<br><select id='series_destino' multiple ></select></td>";strHTML+="</tr>";strHTML+="<tr>";strHTML+="<td>"+CreaBoton("","ConfirmNoSerie1",lstMsg[111],"ConfirmNumSerie();","left",false,false)+"</td>";strHTML+="<td>&nbsp;</td>";strHTML+="<td>"+CreaBoton("","CancelNoSerie2",lstMsg[112],"CancelNumSerie();","left",false,false)+"</td>";strHTML+="</tr>";strHTML+="</table>";document.getElementById("dialog2_inside").innerHTML=strHTML;},error:function(objeto,quepaso,otroobj){alert(":Series pto:"+objeto+" "+quepaso+" "+otroobj);}});}function CancelNumSerie(){$("#dialog2").dialog("close");}function AgregaSerieX(){var objSelOr=document.getElementById("series_origen");var objSelDest=document.getElementById("series_destino");var intXSurtir=parseInt(document.getElementById("search_cant").value);var intSurtido=parseInt(document.getElementById("search_cant_sel").value);for(var x=0;x<objSelOr.length;x++){if(objSelOr[x].selected){if(objSelDest.length+1<=intXSurtir){intSurtido++;document.getElementById("search_cant_sel").value=intSurtido;select_add(objSelDest,objSelOr[x].value,objSelOr[x].value);objSelOr.remove(x);}else{alert(lstMsg[178]);}}}}function RemueveSerieX(){var objSelOr=document.getElementById("series_origen");var objSelDest=document.getElementById("series_destino");var intSurtido=parseInt(document.getElementById("search_cant_sel").value);for(var x=0;x<objSelDest.length;x++){if(objSelDest[x].selected){intSurtido--;document.getElementById("search_cant_sel").value=intSurtido;select_add(objSelOr,objSelDest[x].value,objSelDest[x].value);objSelDest.remove(x);}}}function ConfirmNumSerie(){var intXSurtir=parseInt(document.getElementById("search_cant").value);var intSurtido=parseInt(document.getElementById("search_cant_sel").value);if(intXSurtir>=intSurtido){var _Cantidad=intSurtido;var _idProd=document.getElementById("_idProd").value;var objSelDest=document.getElementById("series_destino");var _strSeries="";for(var x=0;x<objSelDest.length;x++){_strSeries+=objSelDest[x].value+",";}$("#dialog2").dialog("close");var gridD=jQuery("#PD_GRID");var lstRow=gridD.getRowData(_idProd);lstRow.PDD_RECIBIDO=parseFloat(lstRow.PDD_RECIBIDO)+parseFloat(_Cantidad);lstRow.PDD_SERIES=lstRow.PDD_SERIES+_strSeries;gridD.setRowData(_idProd,lstRow);intContaCajas1++;var objCaja=new _ClassSurtidoCaja();objCaja.PDD_ID=lstRow.PDD_ID;objCaja.PDD_PR_ID=lstRow.PDD_PR_ID;objCaja.PDD_CANTIDAD=_Cantidad;objCaja.PDD_CAJA=d.getElementById("PD_CAJA").value;objCaja.PDD_SERIES=_strSeries;lstCajasSurte[intContaCajas1]=objCaja;document.getElementById("PD_PROD").value="";document.getElementById("PD_PROD").focus();d.getElementById("PD_CANT").value=1;SetSumSurtMerc();$("#dialogWait").dialog("close");}else{alert(lstMsg[179]);}}function RecibirAllSurte(){var grid=jQuery("#PD_GRID");$("#dialogWait").dialog("open");var arr=grid.getDataIDs();for(var i=0;i<arr.length;i++){var id=arr[i];var lstRowUpdate=grid.getRowData(id);var intCant=parseFloat(lstRowUpdate.PDD_CANTIDAD)-(parseFloat(lstRowUpdate.PDD_RECIBIDO));var bolCumpleExist=false;if(strNomMainSurteMerc=="CONSIG_A"){bolCumpleExist=true;}else{if(parseInt(lstRowUpdate.PDD_REQEXIST)==1){if((parseFloat(lstRowUpdate.PDD_RECIBIDO)+parseFloat(intCant))<=parseFloat(lstRowUpdate.PDD_EXIST)){bolCumpleExist=true;}else{alert(lstMsg[135]);}}else{bolCumpleExist=true;}}if(bolCumpleExist){if(lstRowUpdate.PDD_USA_SERIE==1){alert(lstMsg[183]);}else{lstRowUpdate.PDD_RECIBIDO=parseFloat(lstRowUpdate.PDD_RECIBIDO)+intCant;grid.setRowData(id,lstRowUpdate);intContaCajas1++;var objCaja=new _ClassSurtidoCaja();objCaja.PDD_ID=lstRowUpdate.PDD_ID;objCaja.PDD_PR_ID=lstRowUpdate.PDD_PR_ID;objCaja.PDD_CANTIDAD=intCant;objCaja.PDD_CAJA=d.getElementById("PD_CAJA").value;objCaja.PDD_SERIES="";lstCajasSurte[intContaCajas1]=objCaja;}}}d.getElementById("PD_PROD").value="";d.getElementById("PD_CANT").value="1";SetSumSurtMerc();d.getElementById("PD_PROD").focus();$("#dialogWait").dialog("close");}function editGridSurte(e){if(e.originalEvent.keyCode==13){var grid=jQuery("#PD_GRID");grid.saveRow(lastselSurte);lastselSurte=0;setTimeout("SetSumSurtMerc()",1000);}}function editFilaSurte(id){var grid=jQuery("#PD_GRID");if(id!=lastselSurte){grid.saveRow(lastselSurte);grid.editRow(id,false);lastselSurte=id;}}