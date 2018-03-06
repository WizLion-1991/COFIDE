/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
function GenericAlert() {

}
function GenQuestion() {

}
/**
 * 
 * @param {String} strTxt: cadena de texto que mostrara el mensaje
 * @param {String} strBtn: cadena de texto que mostrara el boton
 * @returns {null}
 */
function GenAlert(strTxt, strBtn, strTitle) {
    var strHtml = "<table border= 0>";
    strHtml += "<tr>";
    strHtml += "<td>" + strTxt + "</td>";
    strHtml += "</tr>";
    strHtml += "<tr>";
    strHtml += "<td><input type=button id='ACEPTAR' onclick='GenExitAlert()' value='" + strBtn + "'>";
    strHtml += "</td>";
    strHtml += "</tr>";
    strHtml += "</table>";
    console.log(strHtml);
    document.getElementById("dialogGen_inside").innerHTML = strHtml;
    $("#dialogGen").dialog("option", "title", strTitle);
    $("#dialogGen").dialog("open");
}

function GenExitAlert() {
    $("#dialogGen").dialog("close");
}