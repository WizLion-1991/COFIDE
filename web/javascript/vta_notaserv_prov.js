var itemIdSv=0;var bolCambioFechaServ=false;var dblTasaVtaSv1=dblTasa1;var dblTasaVtaSv2=dblTasa2;var dblTasaVtaSv3=dblTasa3;var intIdTasaVtaSv1=intIdTasa1;var intIdTasaVtaSv2=intIdTasa2;var intIdTasaVtaSv3=intIdTasa3;var intSImpVta1_2Sv=intSImp1_2;var intSImpVta1_3Sv=intSImp1_3;var intSImpVta2_3Sv=intSImp2_3;var intCT_TIPOPERSSv=0;var intCT_TIPOFACSv=0;var strCT_USOIMBUEBLESv="";function vta_notaserv_prov(){}function InitServNCPROV(){$("#dialogWait").dialog("open");myLayout.close("west");myLayout.close("east");myLayout.close("south");myLayout.close("north");OcultarAvisosServNCPROV();d.getElementById("SC_ID").value=intSucDefa;FormStyleServNCPROV();d.getElementById("FCT_ID").value=intCteDefa;d.getElementById("NC_OPER").value=strUserName;d.getElementById("NC_TASASEL1").value=intIdTasaVtaSv1;d.getElementById("NC_DESC").focus();ObtenNomPvServNC();var strHtml="<ul>"+getMenuItem("CallbtnNC0PROV();","Guardar Venta","images/ptovta/CircleSave.png")+getMenuItem("CallbtnNC1PROV();","Nueva Venta","images/ptovta/VisPlus.png")+getMenuItem("CallbtnNC2PROV();","Consultar Venta","images/ptovta/VisMagnifier.png")+getMenuItem("CallbtnNC7PROV();","Borrar Concepto","images/ptovta/VisClose.png")+getMenuItem("CallbtnNC9PROV();","Salir","images/ptovta/exitBig.png")+"</ul>";document.getElementById("TOOLBAR").innerHTML=strHtml;bolCambioFechaServ=false;$.ajax({type:"POST",data:"keys=85",scriptCharset:"utf-8",contentType:"application/x-www-form-urlencoded;charset=utf-8",cache:false,dataType:"xml",url:"Acceso.do",success:function(datos){var objsc=datos.getElementsByTagName("Access")[0];var lstKeys=objsc.getElementsByTagName("key");for(i=0;i<lstKeys.length;i++){var obj=lstKeys[i];if(obj.getAttribute("id")==85&&obj.getAttribute("enabled")=="true"){bolCambioFechaServ=true;}}if(bolCambioFechaServ){if($("#NC_FECHA").datepicker("isDisabled")){$("#NC_FECHA").datepicker("enable");}var objFecha=document.getElementById("NC_FECHA");objFecha.setAttribute("class","outEdit");objFecha.setAttribute("className","outEdit");objFecha.readOnly=false;}else{$("#NC_FECHA").datepicker("disable");}$("#dialogWait").dialog("close");setTimeout("d.getElementById('NC_DESC').focus();",3000);SelRegDefaNCServPROV();},error:function(objeto,quepaso,otroobj){alert(":pto:"+objeto+" "+quepaso+" "+otroobj);}});document.getElementById("btnSI").onclick=function(){ConfirmaSINCPROV();};document.getElementById("btnNO").onclick=function(){ConfirmaNONCPROV();};}function FormStyleServNCPROV(){d.getElementById("NC_TOT").setAttribute("class","ui-Total");d.getElementById("NC_TOT").setAttribute("className","ui-Total");d.getElementById("btn1").setAttribute("class","Oculto");d.getElementById("btn1").setAttribute("className","Oculto");}function ObtenNomPvServNC(objPedido,lstdeta,strTipoVta,bolPasaPedido){var intCte=document.getElementById("FCT_ID").value;if(bolPasaPedido==undefined){bolPasaPedido=false;}ValidaClean("CT_NOM");$.ajax({type:"POST",data:"CT_ID="+intCte,scriptCharset:"utf-8",contentType:"application/x-www-form-urlencoded;charset=utf-8",cache:false,dataType:"xml",url:"VtasMov.do?id=9",success:function(datoVal){var objCte=datoVal.getElementsByTagName("vta_clientes")[0];if(objCte.getAttribute("CT_ID")==0){document.getElementById("CT_NOM").value="***************";ValidaShow("CT_NOM",lstMsg[28]);}else{document.getElementById("CT_NOM").value=objCte.getAttribute("CT_RAZONSOCIAL");document.getElementById("FCT_LPRECIOS").value=objCte.getAttribute("CT_LPRECIOS");document.getElementById("FCT_DESCUENTO").value=objCte.getAttribute("CT_DESCUENTO");document.getElementById("FCT_DIASCREDITO").value=objCte.getAttribute("CT_DIASCREDITO");document.getElementById("FCT_MONTOCRED").value=objCte.getAttribute("CT_MONTOCRED");intCT_TIPOPERSSv=objCte.getAttribute("CT_TIPOPERS");intCT_TIPOFACSv=objCte.getAttribute("CT_TIPOFAC");strCT_USOIMBUEBLESv=objCte.getAttribute("CT_USOIMBUEBLE");}if(bolPasaPedido){DrawPedidoDetaenVentaSrv(objPedido,lstdeta,strTipoVta);}},error:function(objeto,quepaso,otroobj){document.getElementById("CT_NOM").value="***************";ValidaShow("CT_NOM",lstMsg[28]);alert(":pto:"+objeto+" "+quepaso+" "+otroobj);}});}function ObtenNomVendNCPROV(){var intVend=document.getElementById("VE_ID").value;$.ajax({type:"POST",data:"VE_ID="+intVend,scriptCharset:"utf-8",contentType:"application/x-www-form-urlencoded;charset=utf-8",cache:false,dataType:"xml",url:"CIP_TablaOp.jsp?ID=4&opnOpt=VENDEDOR",success:function(datoVal){var objVend=datoVal.getElementsByTagName("vta_vendedores")[0];document.getElementById("VE_NOM").value=objVend.getAttribute("VE_NOMBRE");},error:function(objeto,quepaso,otroobj){alert(":pto2:"+objeto+" "+quepaso+" "+otroobj);}});}function AddItemNCPROV(){var strConc=UCase(d.getElementById("NC_DESC").value);if(trim(strConc)!=""){$("#dialogWait").dialog("open");var Ct_Id=d.getElementById("NC_ID").value;var strCod=d.getElementById("NC_PROD").value;var dblCantidad=d.getElementById("NC_CANT").value;var dblExistencia=0;AddItemPrecPROV(null,Ct_Id,dblCantidad,strCod,dblExistencia,0);}}function lstRowChangePrecioNCPROV(lstRow,idUpdate,grid){var objImportes=new _ImporteVta();objImportes.dblCantidad=parseFloat(lstRow.FACD_CANTIDAD);objImportes.dblPrecio=parseFloat(lstRow.FACD_PRECIO);objImportes.dblPrecioReal=lstRow.FACD_PRECREAL;objImportes.dblPorcDescGlobal=document.getElementById("FCT_DESCUENTO").value;objImportes.dblPorcDesc=lstRow.FACD_PORDESC;objImportes.dblPrecFijo=lstRow.FACD_PRECFIJO;objImportes.dblExento1=lstRow.FACD_EXENTO1;objImportes.dblExento2=lstRow.FACD_EXENTO2;objImportes.dblExento3=lstRow.FACD_EXENTO3;objImportes.intDevo=lstRow.FACD_ESDEVO;objImportes.CalculaImporte();lstRow.FACD_IMPORTE=objImportes.dblImporte;lstRow.FACD_IMPUESTO1=objImportes.dblImpuesto1;lstRow.FACD_PORDESC=objImportes.dblPorcAplica;grid.setRowData(idUpdate,lstRow);setSum();}function AddItemPrecPROV(objProd,Ct_Id,Cantidad,strCod,dblExist,intDevo){var dblImporteC=document.getElementById("NC_PRECIO").value;var strDesc=document.getElementById("NC_DESC").value;var objImportes=new _ImporteVtaNCPROV();objImportes.dblCantidad=Cantidad;objImportes.dblPrecio=parseFloat(dblImporteC);objImportes.dblPrecioReal=parseFloat(dblImporteC);objImportes.dblPorcDescGlobal=0;objImportes.dblExento1=0;objImportes.dblExento2=0;objImportes.dblExento3=0;objImportes.intDevo=intDevo;objImportes.CalculaImporte();var dblImporte=objImportes.dblImporte;var datarow={FACD_ID:0,FACD_CANTIDAD:Cantidad,FACD_DESCRIPCION:"Servicio",FACD_IMPORTE:dblImporte,FACD_CVE:strCod,FACD_PRECIO:dblImporteC,FACD_TASAIVA1:dblTasa1,FACD_DESGLOSA1:1,FACD_IMPUESTO1:objImportes.dblImpuesto1,FACD_PR_ID:0,FACD_EXENTO1:0,FACD_EXENTO2:0,FACD_EXENTO3:0,FACD_REQEXIST:0,FACD_EXIST:0,FACD_NOSERIE:"",FACD_ESREGALO:0,FACD_IMPORTEREAL:dblImporte,FACD_PRECREAL:dblImporteC,FACD_DESCUENTO:0,FACD_PORDESC:objImportes.dblPorcAplica,FACD_PRECFIJO:0,FACD_ESDEVO:intDevo,FACD_CODBARRAS:"",FACD_NOTAS:strDesc};itemIdSv++;jQuery("#NC_GRID").addRowData(itemIdSv,datarow,"last");d.getElementById("NC_PROD").value="";d.getElementById("NC_DESC").value="";d.getElementById("NC_PRECIO").value="0.0";d.getElementById("NC_DESC").focus();d.getElementById("NC_CANT").value=1;bolFind=true;setSumNCPROV();EvalSucursalNCPROV();$("#dialogWait").dialog("close");}function VtaDropNCPROV(){var grid=jQuery("#NC_GRID");if(grid.getGridParam("selrow")!=null){grid.delRowData(grid.getGridParam("selrow"));document.getElementById("NC_DESC").focus();setSumNCPROV();EvalSucursalNCPROV();}}function setSumNCPROV(){var grid=jQuery("#NC_GRID");var arr=grid.getDataIDs();var dblSuma=0;var dblImpuesto1=0;var dblImporte=0;for(var i=0;i<arr.length;i++){var id=arr[i];var lstRow=grid.getRowData(id);dblSuma+=parseFloat(lstRow.FACD_IMPORTE);if(parseInt(intCT_TIPOFACSv)==1){if(strCT_USOIMBUEBLESv=="HABITACIONAL"){dblSuma-=parseFloat(lstRow.FACD_IMPUESTO1);}else{dblImpuesto1+=parseFloat(lstRow.FACD_IMPUESTO1);}}else{dblImpuesto1+=parseFloat(lstRow.FACD_IMPUESTO1);}dblImporte+=(parseFloat(lstRow.FACD_IMPORTE)-parseFloat(lstRow.FACD_IMPUESTO1));}var dblIEPS=0;if(document.getElementById("NC_USO_IEPS1").checked){if(parseFloat(document.getElementById("NC_TASA_IEPS").value)!=0){try{dblIEPS=dblImporte*(parseFloat(document.getElementById("NC_TASA_IEPS").value)/100);
}catch(err){}}else{alert(lstMsg[62]);document.getElementById("NC_TASA_IEPS").focus();}var tax=new Impuestos(dblTasaVtaSv1,dblTasaVtaSv2,dblTasaVtaSv3,intSImpVta1_2Sv,intSImpVta1_3Sv,intSImpVta2_3Sv);tax.CalculaImpuestoMas(dblIEPS,0,0);dblImpuesto1+=tax.dblImpuesto1;dblSuma+=dblIEPS+tax.dblImpuesto1;}d.getElementById("NC_IMPORTE_IEPS").value=FormatNumber(dblIEPS,intNumdecimal,true);d.getElementById("NC_TOT").value=FormatNumber(dblSuma,intNumdecimal,true);d.getElementById("NC_IMPUESTO1").value=FormatNumber(dblImpuesto1,intNumdecimal,true);d.getElementById("NC_IMPORTE").value=FormatNumber(dblImporte,intNumdecimal,true);document.getElementById("NC_RETISR").parentNode.parentNode.style.display="none";document.getElementById("NC_RETIVA").parentNode.parentNode.style.display="none";document.getElementById("NC_NETO").parentNode.parentNode.style.display="none";}function getLstItemsNCPROV(){}function OpnDiagPvNC(){OpnOpt("PROVEEDOR","grid","dialogCte",false,false);}function OpnDiagVendNCPROV(){OpnOpt("VENDEDOR","grid","dialogVend",false,false);}function SaveVtaSvNCPROV(){if(parseFloat(document.getElementById("NC_TOT").value)==0){alert(lstMsg[56]);}else{if(d.getElementById("TOTALXPAGAR")!=null){d.getElementById("TOTALXPAGAR").value=d.getElementById("NC_TOT").value;}SaveVtaSvDoNCPROV();}}function SaveVtaSvDoNCPROV(){$("#dialogPagos").dialog("close");$("#dialogWait").dialog("open");var strPOST="";var strPrefijoMaster="NC";var strPrefijoDeta="NCD";var strKey="NC_ID";var strNomFormat="NCREDITOSV";strPOST+="SC_ID="+d.getElementById("SC_ID").value;strPOST+="&PV_ID="+d.getElementById("FCT_ID").value;strPOST+="&VE_ID="+d.getElementById("VE_ID").value;strPOST+="&PD_ID="+d.getElementById("PD_ID").value;strPOST+="&"+strPrefijoMaster+"_ESSERV=1";strPOST+="&"+strPrefijoMaster+"_MONEDA="+d.getElementById("NC_MONEDA").value;strPOST+="&"+strPrefijoMaster+"_FECHA="+d.getElementById("NC_FECHA").value;strPOST+="&"+strPrefijoMaster+"_FOLIO="+d.getElementById("NC_FOLIO").value;strPOST+="&"+strPrefijoMaster+"_NOTAS="+d.getElementById("NC_NOTAS").value;strPOST+="&"+strPrefijoMaster+"_TOTAL="+d.getElementById("NC_TOT").value;strPOST+="&"+strPrefijoMaster+"_IMPUESTO1="+d.getElementById("NC_IMPUESTO1").value;strPOST+="&"+strPrefijoMaster+"_IMPUESTO2="+d.getElementById("NC_IMPUESTO2").value;strPOST+="&"+strPrefijoMaster+"_IMPUESTO3="+d.getElementById("NC_IMPUESTO3").value;strPOST+="&"+strPrefijoMaster+"_IMPORTE="+d.getElementById("NC_IMPORTE").value;strPOST+="&"+strPrefijoMaster+"_RETISR="+d.getElementById("NC_RETISR").value;strPOST+="&"+strPrefijoMaster+"_RETIVA="+d.getElementById("NC_RETIVA").value;strPOST+="&"+strPrefijoMaster+"_NETO="+d.getElementById("NC_NETO").value;strPOST+="&"+strPrefijoMaster+"_NOTASPIE="+d.getElementById("NC_NOTASPIE").value;strPOST+="&"+strPrefijoMaster+"_REFERENCIA="+d.getElementById("NC_REFERENCIA").value;strPOST+="&"+strPrefijoMaster+"_CONDPAGO="+d.getElementById("NC_CONDPAGO").value;strPOST+="&"+strPrefijoMaster+"_TASA1="+dblTasaVtaSv1;strPOST+="&"+strPrefijoMaster+"_TASA2="+dblTasaVtaSv2;strPOST+="&"+strPrefijoMaster+"_TASA3="+dblTasaVtaSv3;strPOST+="&"+"TI_ID="+intIdTasaVtaSv1;strPOST+="&"+"TI_ID2="+intIdTasaVtaSv2;strPOST+="&"+"TI_ID3="+intIdTasaVtaSv3;strPOST+="&"+strPrefijoMaster+"_TASAPESO="+d.getElementById("NC_TASAPESO").value;if(document.getElementById("NC_USO_IEPS1").checked){strPOST+="&"+strPrefijoMaster+"_USO_IEPS=1";strPOST+="&"+strPrefijoMaster+"_TASA_IEPS="+d.getElementById("NC_TASA_IEPS").value;strPOST+="&"+strPrefijoMaster+"_IMPORTE_IEPS="+d.getElementById("NC_IMPORTE_IEPS").value;}else{strPOST+="&"+strPrefijoMaster+"_USO_IEPS=0";strPOST+="&"+strPrefijoMaster+"_TASA_IEPS=0";strPOST+="&"+strPrefijoMaster+"_IMPORTE_IEPS=0";}strPOST+="&CXP_ID="+d.getElementById("FAC_IDNC").value;strPOST+="&TKT_ID="+d.getElementById("TKT_IDNC").value;strPOST+="&"+strPrefijoMaster+"_PERIODICIDAD="+d.getElementById("NC_PERIODICIDAD").value;strPOST+="&"+strPrefijoMaster+"_DIAPER="+d.getElementById("NC_DIAPER").value;if(document.getElementById("NC_REGIMENFISCALcount")!=null&&document.getElementById("NC_REGIMENFISCALcount")!=undefined){var intCuantosReg=document.getElementById("NC_REGIMENFISCALcount").value;if(intCuantosReg>0){for(var iRegim=0;iRegim<intCuantosReg;iRegim++){if(d.getElementById("NC_REGIMENFISCAL"+iRegim).checked){strPOST+="&"+strPrefijoMaster+"_REGIMENFISCAL="+d.getElementById("NC_REGIMENFISCAL"+iRegim).value;}}}}var grid=jQuery("#NC_GRID");var arr=grid.getDataIDs();var intC=0;for(var i=0;i<arr.length;i++){var id=arr[i];var lstRow=grid.getRowData(id);intC++;strPOST+="&PR_ID"+intC+"="+lstRow.FACD_PR_ID;strPOST+="&"+strPrefijoDeta+"_EXENTO1"+intC+"="+lstRow.FACD_EXENTO1;strPOST+="&"+strPrefijoDeta+"_EXENTO2"+intC+"="+lstRow.FACD_EXENTO2;strPOST+="&"+strPrefijoDeta+"_EXENTO3"+intC+"="+lstRow.FACD_EXENTO3;strPOST+="&"+strPrefijoDeta+"_CVE"+intC+"="+lstRow.FACD_CVE;strPOST+="&"+strPrefijoDeta+"_DESCRIPCION"+intC+"="+lstRow.FACD_DESCRIPCION;strPOST+="&"+strPrefijoDeta+"_CANTIDAD"+intC+"="+lstRow.FACD_CANTIDAD;if(intPreciosconImp==1){strPOST+="&"+strPrefijoDeta+"_PRECIO"+intC+"="+lstRow.FACD_PRECIO;strPOST+="&"+strPrefijoDeta+"_PRECREAL"+intC+"="+lstRow.FACD_PRECREAL;}else{var dblPrecioConImp=0;var dblPrecioRealConImp=0;if(lstRow.FACD_CANTIDAD>0){dblPrecioConImp=(parseFloat(lstRow.FACD_PRECIO)+parseFloat(lstRow.FACD_IMPUESTO1/lstRow.FACD_CANTIDAD)+parseFloat(lstRow.FACD_IMPUESTO2/lstRow.FACD_CANTIDAD)+parseFloat(lstRow.FACD_IMPUESTO3/lstRow.FACD_CANTIDAD));dblPrecioRealConImp=(parseFloat(lstRow.FACD_PRECREAL)+parseFloat(lstRow.FACD_IMPUESTO1/lstRow.FACD_CANTIDAD)+parseFloat(lstRow.FACD_IMPUESTO2/lstRow.FACD_CANTIDAD)+parseFloat(lstRow.FACD_IMPUESTO3/lstRow.FACD_CANTIDAD));}else{dblPrecioConImp=(parseFloat(lstRow.FACD_PRECIO));dblPrecioRealConImp=(parseFloat(lstRow.FACD_PRECREAL));}strPOST+="&"+strPrefijoDeta+"_PRECIO"+intC+"="+dblPrecioConImp;strPOST+="&"+strPrefijoDeta+"_PRECREAL"+intC+"="+dblPrecioRealConImp;}strPOST+="&"+strPrefijoDeta+"_IMPORTE"+intC+"="+lstRow.FACD_IMPORTE;strPOST+="&"+strPrefijoDeta+"_TASAIVA1"+intC+"="+lstRow.FACD_TASAIVA1;strPOST+="&"+strPrefijoDeta+"_TASAIVA2"+intC+"=0"+lstRow.FACD_TASAIVA2;strPOST+="&"+strPrefijoDeta+"_TASAIVA3"+intC+"="+lstRow.FACD_TASAIVA3;strPOST+="&"+strPrefijoDeta+"_IMPUESTO1"+intC+"="+lstRow.FACD_IMPUESTO1;strPOST+="&"+strPrefijoDeta+"_IMPUESTO2"+intC+"="+lstRow.FACD_IMPUESTO2;strPOST+="&"+strPrefijoDeta+"_IMPUESTO3"+intC+"="+lstRow.FACD_IMPUESTO3;strPOST+="&"+strPrefijoDeta+"_ESREGALO"+intC+"="+lstRow.FACD_ESREGALO;strPOST+="&"+strPrefijoDeta+"_NOSERIE"+intC+"="+lstRow.FACD_NOSERIE;strPOST+="&"+strPrefijoDeta+"_IMPORTEREAL"+intC+"="+lstRow.FACD_IMPORTEREAL;strPOST+="&"+strPrefijoDeta+"_DESCUENTO"+intC+"="+lstRow.FACD_DESCUENTO;strPOST+="&"+strPrefijoDeta+"_PORDESC"+intC+"="+lstRow.FACD_PORDESC;strPOST+="&"+strPrefijoDeta+"_ESDEVO"+intC+"="+lstRow.FACD_ESDEVO;strPOST+="&"+strPrefijoDeta+"_PRECFIJO"+intC+"="+lstRow.FACD_PRECFIJO;strPOST+="&"+strPrefijoDeta+"_ESREGALO"+intC+"="+lstRow.FACD_ESREGALO;strPOST+="&"+strPrefijoDeta+"_NOTAS"+intC+"="+lstRow.FACD_NOTAS;}strPOST+="&COUNT_ITEM="+intC;$.ajax({type:"POST",data:encodeURI(strPOST),scriptCharset:"utf-8",contentType:"application/x-www-form-urlencoded;charset=utf-8",cache:false,dataType:"html",url:"ERP_NCreditoProv.jsp?id=1",success:function(dato){dato=trim(dato);if(Left(dato,3)=="OK."){alert("Voy sin problemas");}else{alert(dato);}$("#dialogWait").dialog("close");},error:function(objeto,quepaso,otroobj){alert(":pto9:"+objeto+" "+quepaso+" "+otroobj);$("#dialogWait").dialog("close");}});}function ConfirmaSINCPROV(){if(d.getElementById("Operac").value=="Nva"){ResetOperaActualSvNCPROV();}d.getElementById("Operac").value="";$("#SioNO").dialog("close");}function ConfirmaNONCPROV(){$("#SioNO").dialog("close");}function CallbtnNC0PROV(){SaveVtaSvNCPROV();}function CallbtnNC1PROV(){$("#SioNO").dialog("option","title","¿Confirma que desea borrar la operacion actual e iniciar una nueva?");d.getElementById("Operac").value="Nva";document.getElementById("SioNO_inside").innerHTML="";
$("#SioNO").dialog("open");}function ResetOperaActualSvNCPROV(bolSelOpera){if(bolSelOpera==undefined){bolSelOpera=true;}if(bolSelOpera){$("#dialogWait").dialog("open");}d.getElementById("FCT_ID").value=intCteDefa;d.getElementById("NC_FOLIO").value="";d.getElementById("NC_NOTAS").value="";d.getElementById("NC_TOT").value="0.0";d.getElementById("NC_RETISR").value="0.0";d.getElementById("NC_RETIVA").value="0.0";d.getElementById("NC_NETO").value="0.0";d.getElementById("NC_IMPUESTO1").value="0.0";d.getElementById("NC_IMPUESTO2").value="0.0";d.getElementById("NC_IMPUESTO3").value="0.0";d.getElementById("NC_IMPORTE").value="0.0";d.getElementById("NC_DESC").value="";d.getElementById("NC_PRECIO").value="0.0";d.getElementById("VE_ID").value="0";d.getElementById("VE_NOM").value="";d.getElementById("NC_DESC").focus();d.getElementById("NC_PERIODICIDAD").value="1";d.getElementById("NC_DIAPER").value="1";OcultarAvisosServNCPROV();if(bolSelOpera){ObtenNomPvServNC();}var grid=jQuery("#NC_GRID");grid.clearGridData();if(objMap.getXml("FORMPAGO")!=null&&d.getElementById("TOTALPAGADO")!=null){d.getElementById("TOTALPAGADO").value=0;d.getElementById("FPago1").value=0;d.getElementById("FPago2").value=0;d.getElementById("FPago3").value=0;d.getElementById("FPago4").value=0;d.getElementById("Ef_1").value="0.0";d.getElementById("Ef_2").value="0.0";d.getElementById("Bc_2").value="";d.getElementById("Bc_3").value="";d.getElementById("Bc_1").value="0.0";d.getElementById("Tj_2").value="";d.getElementById("Tj_3").value="";d.getElementById("Tj_1").value="0.0";d.getElementById("sf_1").value="0.0";}if(bolSelOpera){$("#dialogWait").dialog("close");}EvalSucursalNCPROV();}function CallbtnNC2PROV(){OpnOpt("NC_VIEW","_ed","dialogView",false,false,true);}function CallbtnNC7PROV(){VtaDropNCPROV();}function CallbtnNC9PROV(){myLayout.open("west");myLayout.open("east");myLayout.open("south");myLayout.open("north");document.getElementById("MainPanel").innerHTML="";var objMainFacPedi=objMap.getScreen("NOTA_SRV");objMainFacPedi.bolActivo=false;objMainFacPedi.bolMain=false;objMainFacPedi.bolInit=false;objMainFacPedi.idOperAct=0;}function SelOperFactServNCPROV(){}function SelOperFactServDoNCPROV(){}function getPedidoenVentaSrvNCPROV(intIdPedido,strTipoVta){$.ajax({type:"POST",data:"PD_ID="+intIdPedido,scriptCharset:"utf-8",contentType:"application/x-www-form-urlencoded;charset=utf-8",cache:false,dataType:"xml",url:"VtasMov.do?id=8",success:function(datos){var objPedido=datos.getElementsByTagName("vta_pedido")[0];var lstdeta=objPedido.getElementsByTagName("deta");if(objPedido.getAttribute("PD_ANULADA")==0){if(objPedido.getAttribute("NC_ID")==0||(objPedido.getAttribute("NC_ID")!=0&&objPedido.getAttribute("PD_ESRECU")==1)){if(objPedido.getAttribute("TKT_ID")==0||(objPedido.getAttribute("TKT_ID")!=0&&objPedido.getAttribute("PD_ESRECU")==1)){ResetOperaActualSvNCPROV(false);DrawPedidoenVentaSrv(objPedido,lstdeta,strTipoVta);$("#dialogView").dialog("close");}else{alert(lstMsg[53]);}}else{alert(lstMsg[52]);}}else{alert(lstMsg[51]);}},error:function(objeto,quepaso,otroobj){alert(":ptoExist:"+objeto+" "+quepaso+" "+otroobj);$("#dialogWait").dialog("close");}});}function DrawPedidoenVentaSrvNCPROV(objPedido,lstdeta,strTipoVta){}function DrawPedidoDetaenVentaSrvNCPROV(objPedido,lstdeta,strTipoVta){}function MostrarAvisosNcPROV(strMsg){var label=document.getElementById("LABELAVISOS");label.innerHTML=strMsg;label.setAttribute("class","Mostrar");label.setAttribute("className","Mostrar");label.setAttribute("class","ui-Total");label.setAttribute("className","ui-Total");}function OcultarAvisosServNCPROV(){var label=document.getElementById("LABELAVISOS");label.innerHTML="";label.setAttribute("class","Oculto");label.setAttribute("className","Oculto");}function _ImporteVtaNCPROV(){this.dblImporte=0;this.dblImpuesto1=0;this.dblImpuesto2=0;this.dblImpuesto3=0;this.dblCantidad=0;this.dblPrecio=0;this.dblPorcDesc=0;this.dblPorcDescGlobal=0;this.dblPrecFijo=0;this.dblExento1=0;this.dblExento2=0;this.dblExento3=0;this.dblImporteReal=0;this.dblPrecioReal=0;this.intDevo=0;this.dblPorcAplica=0;this.CalculaImporte=function CalculaImporte(){this.dblPorcDescGlobal=parseFloat(this.dblPorcDescGlobal);this.dblPorcDesc=parseFloat(this.dblPorcDesc);var dblPrecioAplica=parseFloat(this.dblPrecio);if(this.dblPrecFijo==0){this.dblPorcAplica=0;if(this.dblPorcDescGlobal>0&&this.dblPorcDesc>0){if(this.dblPorcDescGlobal>this.dblPorcDesc){this.dblPorcAplica=this.dblPorcDescGlobal;}if(this.dblPorcDesc>this.dblPorcDescGlobal){this.dblPorcAplica=this.dblPorcDesc;}}else{if(this.dblPorcDescGlobal>0){this.dblPorcAplica=this.dblPorcDescGlobal;}if(this.dblPorcDesc>0){this.dblPorcAplica=this.dblPorcDesc;}}if(this.dblPorcAplica>0){dblPrecioAplica=dblPrecioAplica-(dblPrecioAplica*(this.dblPorcAplica/100));}}this.dblImporte=parseFloat(this.dblCantidad)*parseFloat(dblPrecioAplica);if(parseInt(this.intDevo)==1){this.dblImporte=this.dblImporte*-1;}var dblBase1=this.dblImporte;var dblBase2=this.dblImporte;var dblBase3=this.dblImporte;if(parseInt(this.dblExento1)==1){dblBase1=0;}if(parseInt(this.dblExento2)==1){dblBase2=0;}if(parseInt(this.dblExento3)==1){dblBase3=0;}if(parseInt(intCT_TIPOFACSv)==1){if(strCT_USOIMBUEBLESv=="HABITACIONAL"){dblBase1=0;}}var tax=new Impuestos(dblTasaVtaSv1,dblTasaVtaSv2,dblTasaVtaSv3,intSImpVta1_2Sv,intSImpVta1_3Sv,intSImpVta2_3Sv);if(intPreciosconImp==1){tax.CalculaImpuesto(dblBase1,dblBase2,dblBase3);}else{tax.CalculaImpuestoMas(dblBase1,dblBase2,dblBase3);}if(parseInt(this.dblExento1)==0){this.dblImpuesto1=tax.dblImpuesto1;}if(parseInt(this.dblExento2)==0){this.dblImpuesto2=tax.dblImpuesto2;}if(parseInt(this.dblExento3)==0){this.dblImpuesto3=tax.dblImpuesto3;}if(parseInt(intCT_TIPOFACSv)==1){if(strCT_USOIMBUEBLESv=="HABITACIONAL"){this.dblImpuesto1=0;}}this.dblImporteReal=parseFloat(this.dblCantidad)*parseFloat(this.dblPrecioReal);if(intPreciosconImp==0){this.dblImporteReal+=this.dblImpuesto1+this.dblImpuesto2+this.dblImpuesto3;this.dblImporte+=this.dblImpuesto1+this.dblImpuesto2+this.dblImpuesto3;}};}function ValidaSucNCPROV(){if(parseFloat(document.getElementById("SC_ID").value)==0){document.getElementById("SC_ID").value=intSucDefa;InitImpDefault();}else{InitImpSuc();}}function InitImpDefaultNCPROV(){dblTasaVtaSv1=dblTasa1;dblTasaVtaSv2=dblTasa2;dblTasaVtaSv3=dblTasa3;intSImpVta1_2Sv=intSImp1_2;intSImpVta1_3Sv=intSImp1_3;intSImpVta2_3Sv=intSImp2_3;}function InitImpSucNcPROV(){var objSuc=document.getElementById("SC_ID");$("#dialogWait").dialog("open");$.ajax({type:"POST",data:"SC_ID="+objSuc.value,scriptCharset:"utf-8",contentType:"application/x-www-form-urlencoded;charset=utf-8",cache:false,dataType:"xml",url:"VtasMov.do?id=10",success:function(datos){var objPedido=datos.getElementsByTagName("vta_impuesto")[0];var lstdeta=objPedido.getElementsByTagName("vta_impuestos");for(var i=0;i<lstdeta.length;i++){var obj=lstdeta[i];dblTasaVtaSv1=obj.getAttribute("Tasa1");dblTasaVtaSv2=obj.getAttribute("Tasa2");dblTasaVtaSv3=obj.getAttribute("Tasa3");intIdTasaVtaSv1=obj.getAttribute("TI_ID");intIdTasaVtaSv2=obj.getAttribute("TI_ID2");intIdTasaVtaSv3=obj.getAttribute("TI_ID3");d.getElementById("NC_TASASEL1").value=intIdTasaVtaSv1;intSImpVta1_2Sv=obj.getAttribute("SImp1_2");intSImpVta1_3Sv=obj.getAttribute("SImp1_3");intSImpVta2_3Sv=obj.getAttribute("SImp2_3");}$("#dialogWait").dialog("close");},error:function(objeto,quepaso,otroobj){alert(":ptoExist:"+objeto+" "+quepaso+" "+otroobj);$("#dialogWait").dialog("close");}});}function EvalSucursalNCPROV(){var grid=jQuery("#NC_GRID");var arr=grid.getDataIDs();var objSuc=document.getElementById("SC_ID");var objTasaSel=document.getElementById("NC_TASASEL1");if(arr.length>0){objSuc.onmouseout=function(){this.disabled=false;};objSuc.onmouseleave=function(){this.disabled=false;};objSuc.onmouseover=function(){this.disabled=true;};objSuc.setAttribute("class","READONLY");objSuc.setAttribute("className","READONLY");objTasaSel.onmouseout=function(){this.disabled=false;};objTasaSel.onmouseleave=function(){this.disabled=false;
};objTasaSel.onmouseover=function(){this.disabled=true;};objTasaSel.setAttribute("class","READONLY");objTasaSel.setAttribute("className","READONLY");}else{objSuc.disabled=false;objSuc.onmouseout=function(){var g=1;};objSuc.onmouseleave=function(){var g=1;};objSuc.onmouseover=function(){var g=1;};objSuc.setAttribute("class","outEdit");objSuc.setAttribute("className","outEdit");objTasaSel.disabled=false;objTasaSel.onmouseout=function(){var g=1;};objTasaSel.onmouseleave=function(){var g=1;};objTasaSel.onmouseover=function(){var g=1;};objTasaSel.setAttribute("class","outEdit");objTasaSel.setAttribute("className","outEdit");}}function RefreshMonedaSrvNCPROV(){var objMoneda=document.getElementById("NC_MONEDA");var objTipoCambio=document.getElementById("NC_TASAPESO");if(objMoneda.value==1){objTipoCambio.value=1;objTipoCambio.setAttribute("class","READONLY");objTipoCambio.setAttribute("className","READONLY");objTipoCambio.readOnly=true;}else{objTipoCambio.value=1;objTipoCambio.setAttribute("class","outEdit");objTipoCambio.setAttribute("className","outEdit");objTipoCambio.readOnly=false;}}function UpdateTasaImpSrvNCPROV(){var objTasaSel=document.getElementById("NC_TASASEL1");$("#dialogWait").dialog("open");$.ajax({type:"POST",data:"TI_ID="+objTasaSel.value,scriptCharset:"utf-8",contentType:"application/x-www-form-urlencoded;charset=utf-8",cache:false,dataType:"xml",url:"VtasMov.do?id=11",success:function(datos){var objPedido=datos.getElementsByTagName("vta_impuesto")[0];var lstdeta=objPedido.getElementsByTagName("vta_impuestos");for(var i=0;i<lstdeta.length;i++){var obj=lstdeta[i];dblTasaVtaSv1=obj.getAttribute("Tasa1");}$("#dialogWait").dialog("close");},error:function(objeto,quepaso,otroobj){alert(":ptoExist:"+objeto+" "+quepaso+" "+otroobj);$("#dialogWait").dialog("close");}});}function SelRegDefaNCServPROV(){if(document.getElementById("NC_REGIMENFISCALcount")!=null&&document.getElementById("NC_REGIMENFISCALcount")!=undefined){var intCuantosReg=document.getElementById("NC_REGIMENFISCALcount").value;if(intCuantosReg>0){for(var iRegim=0;iRegim<intCuantosReg;iRegim++){d.getElementById("NC_REGIMENFISCAL"+iRegim).checked=true;break;}}}}function ObtenFacIdNSPROV(){}function OpnDiagFacIdNSPROV(){OpnOpt("CXP_VIEW","_ed","dialogDevo",false,false);}function getDetaDevolucionesServPROV(doc){var intIdoPER=0;var strTipoDoc="";var intIdDoc=0;$("#dialogDevo").dialog("close");if(doc=="_fac"){intIdoPER=5;strTipoDoc="CXP_ID";intIdDoc=d.getElementById("FAC_IDNC").value;}if(doc=="_tkt"){intIdoPER=6;strTipoDoc="TKT_ID";intIdDoc=d.getElementById("TKT_IDNC").value;}$("#dialogWait").dialog("open");$.ajax({type:"POST",data:strTipoDoc+"="+intIdDoc,scriptCharset:"utf-8",contentType:"application/x-www-form-urlencoded;charset=utf-8",cache:false,dataType:"xml",url:"ERP_NCreditoProv.jsp?id="+intIdoPER,success:function(datoVal){var objFactura=datoVal.getElementsByTagName("vta_cxpagars")[0];var lstdeta=objFactura.getElementsByTagName("vta_cxpagar");var obj=lstdeta[0];if(obj!=null){if(obj.getAttribute("CXP_ANULADO")==0){DrawFacturaDevServPROV(objFactura,lstdeta,"");$("#dialogDevo").dialog("close");}else{alert(lstMsg[187]);document.getElementById("FAC_IDNC").value="0";document.getElementById("TKT_IDNC").value="0";}}else{alert("La nota de credito no existe");document.getElementById("FAC_IDNC").value="0";document.getElementById("TKT_IDNC").value="0";}$("#dialogWait").dialog("close");},error:function(objeto,quepaso,otroobj){alert(":Devolucion:"+objeto+" "+quepaso+" "+otroobj);$("#dialogWait").dialog("close");}});}function DrawFacturaDevServPROV(objFactura,lstdeta,strTipoVta){$("#dialogWait").dialog("open");var obj=lstdeta[0];document.getElementById("SC_ID").value=obj.getAttribute("SC_ID");document.getElementById("NC_NOTAS").value=obj.getAttribute("CXP_NOTAS");document.getElementById("NC_NOTASPIE").value=obj.getAttribute("CXP_NOTASPIE");document.getElementById("NC_CONDPAGO").value=obj.getAttribute("CXP_CONDPAGO");document.getElementById("NC_REFERENCIA").value=obj.getAttribute("CXP_REFERENCIA");document.getElementById("FCT_ID").value=obj.getAttribute("PV_ID");document.getElementById("CT_NOM").value=obj.getAttribute("NomProv");document.getElementById("NC_TASASEL1").value=obj.getAttribute("TI_ID");document.getElementById("FCT_DESCUENTO").value=obj.getAttribute("CXP_DESCUENTO");dblTasaVta1=obj.getAttribute("CXP_TASA1");}