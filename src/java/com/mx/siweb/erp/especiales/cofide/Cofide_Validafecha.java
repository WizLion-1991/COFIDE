/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.especiales.cofide;

/**
 *
 * @author siweb
 */
import comSIWeb.Operaciones.Conexion;
import comSIWeb.Utilerias.Fechas;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;

public class Cofide_Validafecha {

    Conexion oConn;
    String strLastResult;
    Fechas fec = new Fechas();

    public String getStrLastResult() {
        return strLastResult;
    }

    public void setStrLastResult(String strLastResult) {
        this.strLastResult = strLastResult;
    }

    public Cofide_Validafecha(Conexion oConn) {
        this.oConn = oConn;
    }

    public boolean validaFecha(String strFecIniC, String strFecFinC, int IdInstructor, String strHrIniCurso, String strHrFinCurso) throws ParseException {
        this.strLastResult = "OK";
        String strHrIniCursoExis = "";
        String strHrFinCursoExis = "";
        boolean blExistCursos = true;

        String strQuery = "select CC_CURSO_ID,CC_INSTRUCTOR_ID, CC_FECHA_INICIAL, CC_FECHA_FINAL, "
                + "CC_HR_EVENTO_INI, CC_HR_EVENTO_FIN "
                + "from cofide_cursos where "
                + "(CC_FECHA_INICIAL between " + strFecIniC + " and " + strFecFinC + " or "
                + "CC_FECHA_FINAL between " + strFecIniC + " and " + strFecFinC + ") "
                + "and CC_INSTRUCTOR_ID = " + IdInstructor;
        try {
            ResultSet result = oConn.runQuery(strQuery, true);
            while (result.next()) {
                strHrIniCursoExis = result.getString("CC_HR_EVENTO_INI");
                strHrFinCursoExis = result.getString("CC_HR_EVENTO_FIN");

                blExistCursos = compHrs(strHrIniCurso, strHrFinCurso, strHrIniCursoExis, strHrFinCursoExis);
                System.out.println(strHrIniCursoExis + " a " + strHrFinCursoExis);
            }
            //fin while
            result.getStatement().close();
            result.close();
        } catch (SQLException e) {
            System.out.println("error de sql " + e);
        }
        return true;
    }

    /*
     Primer parametro Hora Inicial Tentativa
     Segundo parametro Hora final tentativa
     Tercer parametro Hora inicial Existente
     Cuato Parametro Hora final existente
     */
    public boolean compHrs(String iniciop, String finp, String inicioDB, String finDB) {
        boolean blResult = false;

        if (fec.theHourIsLess(iniciop, inicioDB) && fec.theHourIsPlus(finp, inicioDB) && fec.theHourIsLess(iniciop, finDB) && fec.theHourIsPlus(finp, finDB)) {
            blResult = false;
            System.out.println("Las hrs estan dentro del horario");
//            System.exit(0);
        } else if (fec.theHourIsPlus(iniciop, inicioDB) && fec.theHourIsLess(iniciop, finDB)) {
            System.out.println("La Hr inical tiene problemas");
            blResult = false;
//            System.exit(0);
        } else if (fec.theHourIsPlus(finp, inicioDB) && fec.theHourIsLess(finp, finDB)) {
            System.out.println("La Hr final tiene problemas");
            blResult = false;
//            System.exit(0);
        } else {
            blResult = true;
        }
        if(blResult!=true){
            JOptionPane.showMessageDialog(null, "error de "+iniciop+ " a "+finp);
            System.out.println("Horario Disponible.." + blResult);
            System.exit(0);
        }else{
        System.out.println("Horario Disponible.." + blResult);
        }
        return blResult;
    }
    public void prueba() throws ParseException{
        if(validaFecha("20151104", "20151119", 1, "06:00", "07:00") != true){
       JOptionPane.showMessageDialog(null,"Ocupado!???????????????????????????????????????");
        }else{
       JOptionPane.showMessageDialog(null,"libre!!");     
        }
    }
}
