/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.restful.client;

import com.mx.siweb.erp.restful.client.entity.ComproPagoRest;
import com.siweb.utilerias.json.JSONException;
import com.siweb.utilerias.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.commons.ssl.Base64;
import org.apache.logging.log4j.LogManager;

/**
 *Cliente restful para comprobago
 * @author ZeusGalindo
 */
public class ComproPago {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private static final String targetURLCharges = "https://api.compropago.com/v1/charges/";
   private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(ComproPago.class.getName());
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Constructores">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   /**
    *Agrega un cargo a compropago
    * @param strUserPass Usuario
    * @param product_price Precio
    * @param product_name Nombre de producto
    * @param product_id Id de producto
    * @param image_url url de imagen
    * @param customer_name Nombre
    * @param customer_email Email
    * @param payment_type Tipo de Pago(ejemplo OXXO)
    */
   public ComproPagoRest AgregarCargo(String strUserPass,
           String product_price,
           String product_name,
           String product_id,
           String image_url,
           String customer_name,
           String customer_email,
           String payment_type) {
      ComproPagoRest respuesta = new ComproPagoRest();
      try {

         URL targetUrl = new URL(targetURLCharges);


         HttpURLConnection httpConnection = (HttpURLConnection) targetUrl.openConnection();
         //Basic Authentication...
         String userpass = strUserPass + ":";
         String basicAuth = "Basic " + new String(new Base64().encode(userpass.getBytes()));
         httpConnection.setRequestProperty("Authorization", basicAuth);

         httpConnection.setDoOutput(true);

         httpConnection.setDoInput(true);
         httpConnection.setUseCaches(false);
         httpConnection.setAllowUserInteraction(false);

         httpConnection.setRequestMethod("POST");
         httpConnection.setRequestProperty("Content-Type", "application/json");

         //Parametros estaran en una entidad....
         String input = "{\"product_price\": " + product_price + ","
                 + " \"product_name\": \"" + product_name + "\","
                 + "  \"product_id\": \"" + product_id + "\","
                 + " \"image_url\": \"" + image_url + "\","
                 + " \"customer_name\": \"" + customer_name + "\","
                 + " \"customer_email\": \"" + customer_email + "\","
                 + " \"payment_type\": \"" + payment_type + "\"}";

         OutputStream outputStream = httpConnection.getOutputStream();
         outputStream.write(input.getBytes());
         outputStream.flush();

         if (httpConnection.getResponseCode() != 200) {
            log.error(" " + httpConnection.getResponseMessage());
            throw new RuntimeException("Failed : HTTP error code : "
                    + httpConnection.getResponseCode());
         }

         BufferedReader responseBuffer = new BufferedReader(new InputStreamReader(
                 (httpConnection.getInputStream())));

         String output;
         StringBuilder outputAll = new StringBuilder();
         log.debug("Output from Server:\n");
         while ((output = responseBuffer.readLine()) != null) {
            outputAll.append(output);
         }

         httpConnection.disconnect();
         //Respuesta estaran en una entidad....
         log.debug("outputAll:" + outputAll);
         JSONObject objJsonRegreso;
         try {
            objJsonRegreso = new JSONObject(outputAll.toString());
            log.debug(" Cuantos elementos" + objJsonRegreso.length());
            if (objJsonRegreso.length() > 0) {
               respuesta.setPayment_id(objJsonRegreso.get("payment_id").toString());
               respuesta.setShort_payment_id(objJsonRegreso.get("short_payment_id").toString());
               respuesta.setPayment_status(objJsonRegreso.get("payment_status").toString());
               respuesta.setPayment_instructions(objJsonRegreso.get("payment_instructions").toString());
               
               log.debug("payment_id:" + objJsonRegreso.get("payment_id"));
               log.debug("short_payment_id:" + objJsonRegreso.get("short_payment_id"));
               log.debug("payment_status:" + objJsonRegreso.get("payment_status"));
               log.debug("payment_instructions:" + objJsonRegreso.get("payment_instructions"));

               JSONObject objJsonInstrucciones = new JSONObject(objJsonRegreso.get("payment_instructions").toString());
               log.debug("step_1:" + objJsonInstrucciones.get("step_1"));
               respuesta.setStep_1(objJsonInstrucciones.get("step_1").toString());
               respuesta.setStep_2(objJsonInstrucciones.get("step_2").toString());
               respuesta.setStep_3(objJsonInstrucciones.get("step_3").toString());
               respuesta.setDescription(objJsonInstrucciones.get("description").toString());
               respuesta.setNote_extra_comition(objJsonInstrucciones.get("note_extra_comition").toString());
               respuesta.setNote_expiration_date(objJsonInstrucciones.get("note_expiration_date").toString());
               respuesta.setNote_confirmation(objJsonInstrucciones.get("note_confirmation").toString());


            }
         } catch (JSONException ex) {
            ex.printStackTrace();
         }

      } catch (MalformedURLException e) {
         log.error("Error 1");
         e.printStackTrace();

      } catch (IOException e) {
         log.error("Error 2");
         e.printStackTrace();

      }
      return respuesta;
   }
   // </editor-fold>
}
