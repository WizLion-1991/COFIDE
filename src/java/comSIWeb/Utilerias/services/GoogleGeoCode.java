package comSIWeb.Utilerias.services;

import com.siweb.utilerias.json.JSONArray;
import com.siweb.utilerias.json.JSONException;
import com.siweb.utilerias.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *Clase que obtiene la latitud y longitud de ubicacion de una dirección en google maps
 * @author zeus
 */
public class GoogleGeoCode {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private String googleURL = "http://maps.google.com/maps/api/geocode/json?"; //note csv output requested
   private String googleQuery = "language=es&sensor=false&address=";
   private float latitude;
   private float longitude;
   private float accuracy;
   private String responseCode;
   /**
    * Regresa la precision
    * @return Es un valor entero largo
    */
   public float getAccuracy() {
      return accuracy;
   }

   /**
    * Define la precision
    * @param accuracy Es un valor entero largo
    */
   public void setAccuracy(float accuracy) {
      this.accuracy = accuracy;
   }

   /**
    * Regresa el query de google a usar
    * @return Es una cadena
    */
   public String getGoogleQuery() {
      return googleQuery;
   }

   /**
    * Define el query de google
    * @param googleQuery Es una cadena
    */
   public void setGoogleQuery(String googleQuery) {
      this.googleQuery = googleQuery;
   }

   /**
    * Regresa la url de google
    * @return Es una cadena
    */
   public String getGoogleURL() {
      return googleURL;
   }

   /**
    * Define la url de google
    * @param googleURL Es una cadena
    */
   public void setGoogleURL(String googleURL) {
      this.googleURL = googleURL;
   }

   /**
    * Regresa latitud
    * @return Regresa un valor entero largo
    */
   public float getLatitude() {
      return latitude;
   }

   /**
    * Define latitud
    * @param latitude Es un valor entero largo
    */
   public void setLatitude(float latitude) {
      this.latitude = latitude;
   }

   /**
    * Regresa la longitud
    * @return Regresa un valor entero largo
    */
   public float getLongitude() {
      return longitude;
   }

   /**
    * Define la longitud
    * @param longitude Es un valor entero largo
    */
   public void setLongitude(float longitude) {
      this.longitude = longitude;
   }

   /**
    * Regresa el codigo de respuesta
    * @return Regresa OK si la respuesta fue correcta
    */
   public String getResponseCode() {
      return responseCode;
   }

   /**
    * Define el codigo de respuesta
    * @param responseCode Con OK definimos que la respuesta fue correcta
    */
   public void setResponseCode(String responseCode) {
      this.responseCode = responseCode;
   }

   // </editor-fold>
   
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   /**
    * Constuctor default
    */
   public GoogleGeoCode() {
   }
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Metodos">
   /**
    * Obtiene 
    * @param geoTarget
    */
   public void geocode(String geoTarget) {
      //encode the geoTarget in case there are any non-URL friendly
      //characters included (such as spaces and quotes)
      String encodedGeoTarget = null;
      try {
         encodedGeoTarget = URLEncoder.encode(geoTarget, "UTF-8");
      } catch (UnsupportedEncodingException uee) {
      }

      //build the geocoding URL
      URL googleGeocodeURL = null;
      try {
         googleGeocodeURL = new URL(googleURL + googleQuery + encodedGeoTarget);
         //log.finer("Complete URL for geocode request : " + googleGeocodeURL.toString());
      } catch (MalformedURLException mue) {
         //do something
      }
      StringBuilder response = new StringBuilder();
      BufferedReader in = null;
      try {
         in = new BufferedReader(new InputStreamReader(googleGeocodeURL.openStream()));

         if (in.ready()) {
            String thisLine;
            while ((thisLine = in.readLine()) != null) { // while loop begins here
               response.append(thisLine);
            } // end while 
            JSONObject json;
            try {
               json = new JSONObject(response.toString());
               responseCode = json.getString("status");
               //Si fue exitoso
               if (responseCode.equals("OK")) {
                  //System.out.println("Exitoso:");
                  if (json.has("results")) {
                     JSONArray jsonArray1 = json.getJSONArray("results");
                     JSONObject json2 = jsonArray1.getJSONObject(0);
                     //Extraemos la localidad
                     JSONObject json3 = json2.getJSONObject("geometry");
//                     String[] lstNames3 = JSONObject.getNames(json3);
                     JSONObject json4 = json3.getJSONObject("location");
//                     String[] lstNames4 = JSONObject.getNames(json4);
//                     for (String strName : lstNames4) {
//                        System.out.println("   strName:" + strName);
//                     }
                     this.latitude = (float) json4.getDouble("lat");
                     this.longitude = (float) json4.getDouble("lng");
                  }
               }
            } catch (JSONException ex) {
               Logger.getLogger(GoogleGeoCode.class.getName()).log(Level.SEVERE, null, ex);
            }


         } else {
            //log.severe("Unable to open URL @ " + googleGeocodeURL.toString());
         }
      } catch (IOException ex) {
         Logger.getLogger(GoogleGeoCode.class.getName()).log(Level.SEVERE, null, ex);
      } finally {
         if (in != null) {
            try {
               in.close();
            } catch (IOException ex) {
               Logger.getLogger(GoogleGeoCode.class.getName()).log(Level.SEVERE, null, ex);
            }
         }
      }
   }
   // </editor-fold>
}
