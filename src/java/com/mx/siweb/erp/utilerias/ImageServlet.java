/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mx.siweb.erp.utilerias;

import comSIWeb.Utilerias.Sesiones;
import java.io.IOException;
import java.io.OutputStream;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.captcha.Captcha;

/**
 *Servlet para imagenes captcha
 * @author aleph_79
 */
public class ImageServlet extends HttpServlet {
// Constants ----------------------------------------------------------------------------------

   private static final int DEFAULT_BUFFER_SIZE = 10240; // 10KB.
   // Properties ---------------------------------------------------------------------------------
   private String imagePath;

   /**
    * Processes requests for both HTTP
    * <code>GET</code> and
    * <code>POST</code> methods.
    *
    * @param request servlet request
    * @param response servlet response
    * @throws ServletException if a servlet-specific error occurs
    * @throws IOException if an I/O error occurs
    */
   protected void processRequest(HttpServletRequest request, HttpServletResponse response)
           throws ServletException, IOException {
      System.out.println("estmaos aqui....");
      doGet( request,  response);
   }

   // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
   /**
    * Handles the HTTP
    * <code>GET</code> method.
    *
    * @param request servlet request
    * @param response servlet response
    * @throws ServletException if a servlet-specific error occurs
    * @throws IOException if an I/O error occurs
    */
   @Override
   protected void doGet(HttpServletRequest request, HttpServletResponse response)
           throws ServletException, IOException {
      System.out.println("estmaos aqui..W..");
      //Cpatcha
      Captcha captcha = new Captcha.Builder(200, 50)
              .addText()
              .build();
      
      Sesiones.SetSession(request, "CaptchaAnswer", captcha.getAnswer());
      System.out.println(" " + captcha.getAnswer());

      // Get content type by filename.
      String contentType = "image/jpeg";

      // Check if file is actually an image (avoid download of other files by hackers!).
      // For all content types, see: http://www.w3schools.com/media/media_mimeref.asp
      if (contentType == null || !contentType.startsWith("image")) {
         // Do your thing if the file appears not being a real image.
         // Throw an exception, or send 404, or show default/warning image, or just ignore it.
         response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
         return;
      }

      // Init servlet response.
      response.reset();
      response.setBufferSize(DEFAULT_BUFFER_SIZE);
      response.setContentType(contentType);
      response.setHeader("Content-Disposition", "inline; filename=\"Captcha.jpg\"");
      OutputStream outputStream = response.getOutputStream();

      ImageIO.write(captcha.getImage(), "jpeg", outputStream);
   }

   /**
    * Handles the HTTP
    * <code>POST</code> method.
    *
    * @param request servlet request
    * @param response servlet response
    * @throws ServletException if a servlet-specific error occurs
    * @throws IOException if an I/O error occurs
    */
   @Override
   protected void doPost(HttpServletRequest request, HttpServletResponse response)
           throws ServletException, IOException {
      System.out.println("estmaos aqui....WS");
      processRequest(request, response);
   }

   /**
    * Returns a short description of the servlet.
    *
    * @return a String containing servlet description
    */
   @Override
   public String getServletInfo() {
      return "Short description";
   }// </editor-fold>
}
