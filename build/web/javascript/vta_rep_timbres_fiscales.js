function vta_rep_timbres_fiscales(){}function Init_vta_rep_timbres_fiscales(){d.getElementById("btn1").style.display="none";}function reptibresfiscales(){itemIdCob=0;var selected=$("#tabsUSOTIMFIS").tabs("option","selected");var strPost="";$("#dialogWait").dialog("open");switch(selected){case 0:var periodo=document.getElementById("USOTIM_PERIODO_ANIO").value;strPost+="&Parametro="+periodo;$.ajax({type:"POST",data:strPost,scriptCharset:"utf-8",contentType:"application/x-www-form-urlencoded;charset=utf-8",cache:false,dataType:"xml",url:"vta_rep_timbres_fiscales.jsp?id=1",success:function(datos){jQuery("#USOTIM_GRID_ANIO").clearGridData();var objsc=datos.getElementsByTagName("Total_por_Anio")[0];var lstProds=objsc.getElementsByTagName("Total_por_Anio");var total1=0;for(var i=0;i<lstProds.length;i++){var obj=lstProds[i];var datarow={GRID_AÑO_ANIO:obj.getAttribute("Anio"),GRID_AÑO_ANUAL:FormatNumber(obj.getAttribute("Cuantos_anual"),2,true,false,true,false)};total1=total1+parseFloat(obj.getAttribute("Cuantos_anual"));itemIdCob++;jQuery("#USOTIM_GRID_ANIO").addRowData(itemIdCob,datarow,"last");}jQuery("#USOTIM_GRID_ANIO").footerData("set",{GRID_AÑO_ANIO:"TOTAL",GRID_AÑO_ANUAL:FormatNumber(total1,2,true,false,true,false)});$("#dialogWait").dialog("close");},error:function(){jQuery("#Total_por_Anio").clearGridData();alert("No hay productos con esas caracteristicas");$("#dialogWait").dialog("close");}});break;case 1:var periodo=document.getElementById("USOTIM_PERIODO_MES").value;strPost+="&Parametro="+periodo;$.ajax({type:"POST",data:strPost,scriptCharset:"utf-8",contentType:"application/x-www-form-urlencoded;charset=utf-8",cache:false,dataType:"xml",url:"vta_rep_timbres_fiscales.jsp?id=3",success:function(datos){jQuery("#USOTIM_GRID_MES").clearGridData();var objsc=datos.getElementsByTagName("Total_por_Mes")[0];var lstProds=objsc.getElementsByTagName("Total_por_Mes");var total2=0;for(var i=0;i<lstProds.length;i++){var obj=lstProds[i];var datarow={GRID_MES_ANIO:obj.getAttribute("Anio"),GRID_MES_MES:obj.getAttribute("Mes"),GRID_MES_CANTIDAD:FormatNumber(obj.getAttribute("CuantosMes"),2,true,false,true,false)};total2=total2+parseFloat(obj.getAttribute("CuantosMes"));itemIdCob++;jQuery("#USOTIM_GRID_MES").addRowData(itemIdCob,datarow,"last");}jQuery("#USOTIM_GRID_MES").footerData("set",{GRID_MES_MES:"TOTAL",GRID_MES_CANTIDAD:FormatNumber(total2,2,true,false,true,false)});$("#dialogWait").dialog("close");},error:function(){jQuery("#USOTIM_GRID_MES").clearGridData();alert("No hay productos con esas caracteristicas");$("#dialogWait").dialog("close");}});break;case 2:var idCliente=document.getElementById("USOTIM_IDCTE_CTE").value;var periodo=document.getElementById("USOTIM_IDCTE_ANIO").value;strPost+="&Parametro="+idCliente;strPost+="&Parametro1="+periodo;$.ajax({type:"POST",data:strPost,scriptCharset:"utf-8",contentType:"application/x-www-form-urlencoded;charset=utf-8",cache:false,dataType:"xml",url:"vta_rep_timbres_fiscales.jsp?id=5",success:function(datos){jQuery("#USOTIM_GRID_CTE").clearGridData();var objsc=datos.getElementsByTagName("Total_por_Cliente")[0];var lstProds=objsc.getElementsByTagName("Total_por_Cliente");var total3=0;for(var i=0;i<lstProds.length;i++){var obj=lstProds[i];var datarow={GRID_CTE_ID:obj.getAttribute("IdCliente"),GRID_CTE_NOMBRE:obj.getAttribute("Nombre"),GRID_CTE_ANIO:obj.getAttribute("Anio"),GRID_CTE_CANTIDAD:FormatNumber(obj.getAttribute("CuantosAnio"),2,true,false,true,false)};total3=total3+parseFloat(obj.getAttribute("CuantosAnio"));itemIdCob++;jQuery("#USOTIM_GRID_CTE").addRowData(itemIdCob,datarow,"last");}jQuery("#USOTIM_GRID_CTE").footerData("set",{GRID_CTE_ANIO:"TOTAL",GRID_CTE_CANTIDAD:FormatNumber(total3,2,true,false,true,false)});$("#dialogWait").dialog("close");},error:function(){jQuery("#USOTIM_GRID_CTE").clearGridData();alert("No hay productos con esas caracteristicas");$("#dialogWait").dialog("close");}});break;case 3:var idContrato=document.getElementById("USOTIM_IDCTOA_CTOA").value;var periodo=document.getElementById("USOTIM_IDCTOA_ANIO").value;strPost+="&Parametro="+idContrato;strPost+="&Parametro1="+periodo;$.ajax({type:"POST",data:strPost,scriptCharset:"utf-8",contentType:"application/x-www-form-urlencoded;charset=utf-8",cache:false,dataType:"xml",url:"vta_rep_timbres_fiscales.jsp?id=7",success:function(datos){jQuery("#USOTIM_GRID_CTOA").clearGridData();var objsc=datos.getElementsByTagName("Total_por_Contrato")[0];var lstProds=objsc.getElementsByTagName("Total_por_Contrato");var total4=0;for(var i=0;i<lstProds.length;i++){var obj=lstProds[i];var datarow={GRID_CTOA_ID:obj.getAttribute("IdContrato"),GRID_CTOA_CONTRATO_DE_ARRENDAMIENTO:obj.getAttribute("ContratoArrendamiento"),GRID_CTOA_ID_CTE:obj.getAttribute("IdCliente"),GRID_CTOA_NOMBRE:obj.getAttribute("NombreCte"),GRID_CTOA_ANIO:obj.getAttribute("Anio"),GRID_CTOA_CANTIDAD:FormatNumber(obj.getAttribute("CuantosAnio"),2,true,false,true,false),GRID_CTOA_INICIO:obj.getAttribute("FechaInicio"),GRID_CTOA_VENCIMIENTO:obj.getAttribute("FechaCierre"),GRID_cuantos_desde:obj.getAttribute("CuantosDesde"),GRID_cuantos_contrato:obj.getAttribute("CuantosContrato")};total4=total4+parseFloat(obj.getAttribute("CuantosAnio"));itemIdCob++;jQuery("#USOTIM_GRID_CTOA").addRowData(itemIdCob,datarow,"last");}jQuery("#USOTIM_GRID_CTOA").footerData("set",{GRID_CTOA_ANIO:"TOTAL",GRID_CTOA_CANTIDAD:FormatNumber(total4,2,true,false,true,false)});$("#dialogWait").dialog("close");},error:function(){jQuery("#USOTIM_GRID_CTOA").clearGridData();alert("No hay productos con esas caracteristicas");$("#dialogWait").dialog("close");}});break;case 4:var periodo=document.getElementById("USOTIM_PERIODO_CLMES").value;var idCte=document.getElementById("USOTIM_IDCTE_CLMES").value;strPost+="&Parametro="+periodo;strPost+="&Parametro1="+idCte;$.ajax({type:"POST",data:strPost,scriptCharset:"utf-8",contentType:"application/x-www-form-urlencoded;charset=utf-8",cache:false,dataType:"xml",url:"vta_rep_timbres_fiscales.jsp?id=9",success:function(datos){jQuery("#USOTIM_GRID_CLMES").clearGridData();var objsc=datos.getElementsByTagName("Total_Cliente_X_Mes")[0];var lstProds=objsc.getElementsByTagName("Total_Cliente_X_Mes");var total5=0;for(var i=0;i<lstProds.length;i++){var obj=lstProds[i];var datarow={GRID_CTEMES_NOMBRE:obj.getAttribute("nombre"),GRID_CTEMES_ANIO:obj.getAttribute("anio"),GRID_CTEMES_MES:obj.getAttribute("mes"),GRID_CTEMES_CANTIDAD:FormatNumber(obj.getAttribute("cuantos_anio"),2,true,false,true,false)};total5=total5+parseFloat(obj.getAttribute("cuantos_anio"));itemIdCob++;jQuery("#USOTIM_GRID_CLMES").addRowData(itemIdCob,datarow,"last");}jQuery("#USOTIM_GRID_CLMES").footerData("set",{GRID_CTEMES_MES:"TOTAL",GRID_CTEMES_CANTIDAD:FormatNumber(total5,2,true,false,true,false)});$("#dialogWait").dialog("close");},error:function(){jQuery("#USOTIM_GRID_CLMES").clearGridData();alert("No hay productos con esas caracteristicas");$("#dialogWait").dialog("close");}});break;case 5:var idContrato=document.getElementById("USOTIM_IDCTOA_CTOAXMES").value;var periodo=document.getElementById("USOTIM_IDCTOA_ANIOXMES").value;strPost+="&Parametro="+idContrato;strPost+="&Parametro1="+periodo;$.ajax({type:"POST",data:strPost,scriptCharset:"utf-8",contentType:"application/x-www-form-urlencoded;charset=utf-8",cache:false,dataType:"xml",url:"vta_rep_timbres_fiscales.jsp?id=11",success:function(datos){jQuery("#USTI_GRD_CNXMS").clearGridData();var objsc=datos.getElementsByTagName("Total_por_ContratoXMes")[0];var lstProds=objsc.getElementsByTagName("Total_por_ContratoXMes");var total6=0;for(var i=0;i<lstProds.length;i++){var obj=lstProds[i];var datarow={GRID_CTOAXM_ID:obj.getAttribute("IdContrato"),GRID_CTOAXM_CONTRATO_DE_ARRENDAMIENTO:obj.getAttribute("ContratoArrendamiento"),GRID_CTOAXM_ID_CTE:obj.getAttribute("IdCliente"),GRID_CTOAXM_NOMBRE:obj.getAttribute("NombreCte"),GRID_CTOAXM_ANIO:obj.getAttribute("Anio"),GRID_CTOAXM_CANTIDAD:FormatNumber(obj.getAttribute("CuantosAnio"),2,true,false,true,false),GRID_CTOAXM_MES:obj.getAttribute("Mes")};
total6=total6+parseFloat(obj.getAttribute("CuantosAnio"));itemIdCob++;jQuery("#USTI_GRD_CNXMS").addRowData(itemIdCob,datarow,"last");}jQuery("#USTI_GRD_CNXMS").footerData("set",{GRID_CTOAXM_ANIO:"TOTAL",GRID_CTOAXM_CANTIDAD:FormatNumber(total6,2,true,false,true,false)});$("#dialogWait").dialog("close");},error:function(){jQuery("#USTI_GRD_CNXMS").clearGridData();alert("No hay productos con esas caracteristicas");$("#dialogWait").dialog("close");}});break;}}function BotonImprimirTF(){OpnOpt("PPOL_CONT",null,"dialogProv",false,false,true);}function impRepPoLGeneradas(frmt){var selected=$("#tabsUSOTIMFIS").tabs("option","selected");var strPost="";switch(selected){case 0:var periodo=document.getElementById("USOTIM_PERIODO_ANIO").value;strPost+="&Periodo="+periodo;if(frmt==1){Abrir_Link("vta_rep_timbres_fiscales.jsp?id=2&boton_1=PDF"+strPost,500,600,0,0);}else{Abrir_Link("vta_rep_timbres_fiscales.jsp?id=2&boton_1=XLS"+strPost,500,600,0,0);}break;case 1:var periodo=document.getElementById("USOTIM_PERIODO_MES").value;strPost+="&Periodo="+periodo;if(frmt==1){Abrir_Link("vta_rep_timbres_fiscales.jsp?id=4&boton_1=PDF"+strPost,500,600,0,0);}else{Abrir_Link("vta_rep_timbres_fiscales.jsp?id=4&boton_1=XLS"+strPost,500,600,0,0);}break;case 2:var idCliente=document.getElementById("USOTIM_IDCTE_CTE").value;var periodo=document.getElementById("USOTIM_IDCTE_ANIO").value;strPost+="&Parametro="+idCliente;strPost+="&Parametro1="+periodo;if(frmt==1){Abrir_Link("vta_rep_timbres_fiscales.jsp?id=6&boton_1=PDF"+strPost,500,600,0,0);}else{Abrir_Link("vta_rep_timbres_fiscales.jsp?id=6&boton_1=XLS"+strPost,500,600,0,0);}break;case 3:var idContrato=document.getElementById("USOTIM_IDCTOA_CTOA").value;var periodo=document.getElementById("USOTIM_IDCTOA_ANIO").value;strPost+="&Parametro="+idContrato;strPost+="&Parametro1="+periodo;if(frmt==1){Abrir_Link("vta_rep_timbres_fiscales.jsp?id=8&boton_1=PDF"+strPost,500,600,0,0);}else{Abrir_Link("vta_rep_timbres_fiscales.jsp?id=8&boton_1=XLS"+strPost,500,600,0,0);}break;case 4:var periodo=document.getElementById("USOTIM_PERIODO_CLMES").value;var idCte=document.getElementById("USOTIM_IDCTE_CLMES").value;strPost+="&Parametro="+periodo;strPost+="&Parametro1="+idCte;if(frmt==1){Abrir_Link("vta_rep_timbres_fiscales.jsp?id=10&boton_1=PDF"+strPost,500,600,0,0);}else{Abrir_Link("vta_rep_timbres_fiscales.jsp?id=10&boton_1=XLS"+strPost,500,600,0,0);}break;case 5:var idContrato=document.getElementById("USOTIM_IDCTOA_CTOAXMES").value;var periodo=document.getElementById("USOTIM_IDCTOA_ANIOXMES").value;strPost+="&Parametro="+idContrato;strPost+="&Parametro1="+periodo;if(frmt==1){Abrir_Link("vta_rep_timbres_fiscales.jsp?id=12&boton_1=PDF"+strPost,500,600,0,0);}else{Abrir_Link("vta_rep_timbres_fiscales.jsp?id=12&boton_1=XLS"+strPost,500,600,0,0);}break;}$("#dialogProv").dialog("close");}function Rep_Tim_Fis_OpnDiagCte(){OpnOpt("CLIENTES","grid","dialogCte",false,false);}