function openReport(strAbrev,strTipo,strHtmlControl){var strHtml='<form action="Reportes_GeneralesShow" method="post" target="_blank" id="formSend">';strHtml+=CreaHidden("Abrev",strAbrev);strHtml+=CreaHidden("report",strTipo);strHtml+=strHtmlControl;strHtml+="</form>";document.getElementById("formHidden").innerHTML=strHtml;document.getElementById("formSend").submit();}function openWhereverReport(strUrl,strAbrev,strTipo,strHtmlControl){var strHtml='<form action="'+strUrl+'" method="post" target="_blank" id="formSend">';strHtml+=CreaHidden("Abrev",strAbrev);strHtml+=CreaHidden("report",strTipo);strHtml+=strHtmlControl;strHtml+="</form>";document.getElementById("formHidden").innerHTML=strHtml;document.getElementById("formSend").submit();}function openFormat(strNomForm,strTipo,strHtmlControl,strMaskFolio){var strHtml='<form action="Formatos" method="post" target="_blank" id="formSend">';strHtml+=CreaHidden("NomForm",strNomForm);if(strMaskFolio!=undefined){strHtml+=CreaHidden("MASK_FOLIO",strMaskFolio);}strHtml+=CreaHidden("report",strTipo);strHtml+=strHtmlControl;strHtml+="</form>";document.getElementById("formHidden").innerHTML=strHtml;document.getElementById("formSend").submit();}function openWhereverFormat(strUrl,strNomForm,strTipo,strHtmlControl){var strHtml='<form action="'+strUrl+'" method="post" target="_blank" id="formSend">';strHtml+=CreaHidden("NomForm",strNomForm);strHtml+=CreaHidden("report",strTipo);strHtml+=strHtmlControl;strHtml+="</form>";document.getElementById("formHidden").innerHTML=strHtml;document.getElementById("formSend").submit();}