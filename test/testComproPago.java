
import com.siweb.utilerias.json.JSONArray;
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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author ZeusGalindo
 */
public class testComproPago {

   private static final String targetURL = "https://api.compropago.com/v1/charges/";

   public static void main(String[] args) {
      //https://compropago.com/documentacion/api/crear-cargo
      try {

         URL targetUrl = new URL(targetURL);


         HttpURLConnection httpConnection = (HttpURLConnection) targetUrl.openConnection();
         //Basic Authentication...
         String userpass = "sk_test_5214b107977b3bd51" + ":";
         String basicAuth = "Basic " + new String(new Base64().encode(userpass.getBytes()));
         httpConnection.setRequestProperty("Authorization", basicAuth);

         httpConnection.setDoOutput(true);

         httpConnection.setDoInput(true);
         httpConnection.setUseCaches(false);
         httpConnection.setAllowUserInteraction(false);

         httpConnection.setRequestMethod("POST");
         httpConnection.setRequestProperty("Content-Type", "application/json");
         
         //Parametros estaran en una entidad....
         String input = "{\"product_price\": 10000.0,"
                 + " \"product_name\": \"SAMSUNG GOLD CURL\","
                 + "  \"product_id\": \"SMGCURL1\","
                 + " \"image_url\": \"https://test.amazon.com/5f4373\","
                 + " \"customer_name\": \"Alejandra Leyva\","
                 + " \"customer_email\": \"noreply@compropago.com\","
                 + " \"payment_type\": \"OXXO\"}";

         OutputStream outputStream = httpConnection.getOutputStream();
         outputStream.write(input.getBytes());
         outputStream.flush();

         if (httpConnection.getResponseCode() != 200) {
            System.out.println(" " + httpConnection.getResponseMessage());
            throw new RuntimeException("Failed : HTTP error code : "
                    + httpConnection.getResponseCode());
         }

         BufferedReader responseBuffer = new BufferedReader(new InputStreamReader(
                 (httpConnection.getInputStream())));

         String output;
         StringBuilder outputAll = new StringBuilder();
         System.out.println("Output from Server:\n");
         while ((output = responseBuffer.readLine()) != null) {
            outputAll.append(output);
         }

         httpConnection.disconnect();
         //Respuesta estaran en una entidad....
         System.out.println("outputAll:" + outputAll);
         JSONObject objJsonRegreso;
         try {
            objJsonRegreso = new JSONObject(outputAll.toString());
            System.out.println(" Cuantos elementos" + objJsonRegreso.length());
            if (objJsonRegreso.length() > 0) {
               System.out.println("payment_id:" + objJsonRegreso.get("payment_id"));
               System.out.println("short_payment_id:" + objJsonRegreso.get("short_payment_id"));
               System.out.println("payment_status:" + objJsonRegreso.get("payment_status"));
               System.out.println("payment_instructions:" + objJsonRegreso.get("payment_instructions"));
               
               JSONObject objJsonInstrucciones = new JSONObject(objJsonRegreso.get("payment_instructions").toString());
               System.out.println("step_1:" + objJsonInstrucciones.get("step_1"));
               

            }
         } catch (JSONException ex) {
            ex.printStackTrace();
         }

      } catch (MalformedURLException e) {
         System.out.println("Error 1");
         e.printStackTrace();

      } catch (IOException e) {
         System.out.println("Error 2");
         e.printStackTrace();

      }

      /*
       //Consume de un id de pago si funciona 
       try {

       URL restServiceURL = new URL(targetURL);

       HttpURLConnection httpConnection = (HttpURLConnection) restServiceURL.openConnection();
       //Basic Authentication...
       String userpass = "sk_test_5214b107977b3bd51" + ":";
       String basicAuth = "Basic " + new String(new Base64().encode(userpass.getBytes()));
       httpConnection.setRequestProperty("Authorization", basicAuth);
       httpConnection.setRequestMethod("GET");
       httpConnection.setRequestProperty("Accept", "application/json");

       if (httpConnection.getResponseCode() != 200) {
       throw new RuntimeException("HTTP GET Request Failed with Error code : "
       + httpConnection.getResponseCode());
       }

       BufferedReader responseBuffer = new BufferedReader(new InputStreamReader(
       (httpConnection.getInputStream())));

       String output;
       System.out.println("Output from Server:  \n");

       while ((output = responseBuffer.readLine()) != null) {
       System.out.println(output);
       }

       httpConnection.disconnect();

       } catch (MalformedURLException e) {

       e.printStackTrace();

       } catch (IOException e) {

       e.printStackTrace();

       }
       */

   }
}