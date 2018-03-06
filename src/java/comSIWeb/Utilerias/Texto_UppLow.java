/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comSIWeb.Utilerias;

/**
 *
 * @author Desarrollo_COFIDE
 */
public class Texto_UppLow {

    public Texto_UppLow() {
    }

    /**
     * convertir a mayusculas
     *
     * @param strCadena cadena a convertir
     * @return cadena en mayuscula
     */
    public String UpperCaseTexto(String strCadena) {
        String strTexto = "";
        int intLargoCadena = strCadena.length();
        char cCaracter = 0;
        for (int i = 0; i < intLargoCadena; i++) {
            cCaracter = (char) strCadena.charAt(i);
            if (cCaracter >= 'A' && cCaracter <= 'Z' || cCaracter == ' ') {
                strTexto += cCaracter;
            } else if (cCaracter >= 'a' && cCaracter <= 'z' || cCaracter == ' ') {
                cCaracter = (char) (cCaracter - 'a' + 'A');
                strTexto += cCaracter;
            }
        }
        return strTexto;
    }

    /**
     * convertir cadena en minuscula
     *
     * @param strCadena cadena a convertir
     * @return cadena en minuscula
     */
    public String LowerCaseTexto(String strCadena) {
        String strTexto = "";
        int intLargoCadena = strCadena.length();
        char cCaracter = 0;
        for (int i = 0; i < intLargoCadena; i++) {
            cCaracter = (char) strCadena.charAt(i);
            if (cCaracter >= 'a' && cCaracter <= 'z' || cCaracter == ' ') {
                strTexto += cCaracter;
            } else if (cCaracter >= 'A' && cCaracter <= 'Z' || cCaracter == ' ') {
                cCaracter = (char) (cCaracter + 'a' - 'A');
                strTexto += cCaracter;
            }
        }
        return strTexto;
    }

}
