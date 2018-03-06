/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.especiales.cofide;

import com.mx.siweb.erp.especiales.cofide.entidades.CofideLlamada;
import com.siweb.utilerias.json.JSONException;
import com.siweb.utilerias.json.JSONObject;
import comSIWeb.Operaciones.Conexion;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;

/**
 * Realizas las operaciones con las llamdas del PBX
 *
 * @author ZeusSIWEB
 */
public class LlamadasPBX {

    // <editor-fold defaultstate="collapsed" desc="Propiedades">
    private int intEmpresa;
    private String strRespuesta;

    public String getStrRespuesta() {
        return strRespuesta;
    }

    public void setStrRespuesta(String strRespuesta) {
        this.strRespuesta = strRespuesta;
    }

    public int getIntEmpresa() {
        return intEmpresa;
    }

    public void setIntEmpresa(int intEmpresa) {
        this.intEmpresa = intEmpresa;
    }

    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(LlamadasPBX.class.getName());

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Constructores">
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Metodos">
    public boolean generaNvaLlamada(Conexion oConn, String strPhone, String strExtension, int intTimeOut, int intEmpresa) {
        boolean bolToken = false;
        if (strPhone.length() > 0) {
            //Obtenemos parametros de url y usuario
            ResultSet rs = null;
            String strUrlRestFul = null;
            try {
                rs = oConn.runQuery("select EMP_URL_RESTFUL_PBX "
                        + " from vta_empresas where EMP_ID = " + intEmpresa);
                while (rs.next()) {
                    strUrlRestFul = rs.getString("EMP_URL_RESTFUL_PBX");
                }
                rs.close();
            } catch (SQLException ex) {
                log.debug(ex.getMessage());
            }
            //Abrimos conexion con restful
            try {
                if (strUrlRestFul.isEmpty()) {
//            strUrlRestFul = "http://ventas.siwebmx.com:8080/ContabilidadPlus/";
                    strUrlRestFul = "http://192.168.2.11/api/v1/calls";
                }
                URL url = new URL(strUrlRestFul);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
//            conn.setRequestProperty("Content-Type", "application/json");//multipar/form-data
                conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=---t3gu35m1d105");
//es un post lo que nosotros mandamos, no un json
                //Parametros por enviar
                String input = "-----t3gu35m1d105\n"
                        + "Content-Disposition: form-data; name=\"token\"\n"
                        + "\n"
                        + "49oZ9q73jY7FgVlB59j0aB4Z85N81BCj\n"
                        + "-----t3gu35m1d105\n"
                        + "Content-Disposition: form-data; name=\"phone\"\n"
                        + "\n"
                        + "" + strPhone + "\n"
                        + "-----t3gu35m1d105\n"
                        + "Content-Disposition: form-data; name=\"ext\"\n"
                        + "\n"
                        + "" + strExtension + "\n"
                        //                        + "-----t3gu35m1d105"
                        //                        + "Content-Disposition: form-data; name=\"timeout\"\n"
                        //                        + "\n"
                        //                        + "" + intTimeOut + "\n"
                        + "-----t3gu35m1d105";
//            String input = "{\"token\": " + "\"49oZ9q73jY7FgVlB59j0aB4Z85N81BCj" + "\","
//                    + " \"phone\": \"" + strPhone + "\","
//                    + "  \"ext\": \"" + strExtension + "\" }";
                System.out.println(input); //borrar
                OutputStream outputStream = conn.getOutputStream();
                outputStream.write(input.getBytes());
                outputStream.flush();
                if (conn.getResponseCode() != 200) {
                    throw new RuntimeException("Failed : HTTP error code : "
                            + conn.getResponseCode());
                }
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        (conn.getInputStream())));
                String output;
                StringBuilder strJsonTxt = new StringBuilder();
                while ((output = br.readLine()) != null) {
                    strJsonTxt.append(output);
                    System.out.println("PBX: " + output);
                }
                log.debug("Respuesta..." + strJsonTxt);
                try {
                    JSONObject jsonObj = new JSONObject(strJsonTxt.toString());

                    bolToken = jsonObj.getBoolean("result");
                    strRespuesta = (String) jsonObj.get("msg");
                    if (!bolToken) {
                        bolToken = jsonObj.getBoolean("result");
                        strRespuesta = (String) jsonObj.get("errmsg");
                        System.out.println("Token false");
                    }
                    //Faltaria recuperar el id de cliente
                } catch (JSONException ex) {
                    log.error(ex.getMessage());
                    System.out.println("PBX ERROR: al recibir respuesta");
                }
                conn.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                log.error(e.getMessage());
                System.out.println("PBX ERROR: al mandar llamada: " + e);
            } catch (IOException e) {
                e.printStackTrace();
                log.error(e.getMessage());
                System.out.println("PBX ERROR: al enviar llamada IOEXception: " + e);
            }
        } //fin if valida phone, si viene vacio
        return bolToken;
    }

    /**
     * Termina de procesar la llamada
     *
     * @param llamada Es la llamada
     * @param oConn Es la conexion
     */
    public void terminaLlamada(CofideLlamada llamada, Conexion oConn) {
        //Buscamos la llamada en cuestion
        //Actualizamos el estatus de la llamada
    }
    // </editor-fold>
}
