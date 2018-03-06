/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import static nl.captcha.Captcha.NAME;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import nl.captcha.Captcha;
import nl.captcha.servlet.CaptchaServletUtil;
import nl.captcha.servlet.StickyCaptchaServlet;

/**
 *
 * @author ZeusGalindo
 */
public class CustomCaptchaServlet extends StickyCaptchaServlet {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private static final long serialVersionUID = 1L;

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Constructores">
   /**
    * @see StickyCaptchaServlet#StickyCaptchaServlet()
    */
   public CustomCaptchaServlet() {
      super();
      // TODO Auto-generated constructor stub
   }

   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   @Override
   public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

      String _width = getServletConfig().getInitParameter("width");
      String _height = getServletConfig().getInitParameter("height");
      HttpSession session = request.getSession();
      Captcha captcha;
//      if (session.getAttribute(NAME) == null) {
         captcha = new Captcha.Builder(Integer.parseInt(_width), Integer.parseInt(_height))
                 .addText()
                 .gimp()
                 .addBorder()
                 .addNoise()
                 .addBackground()
                 .build();
         session.setAttribute(NAME, captcha);
         CaptchaServletUtil.writeImage(response, captcha.getImage());
         return;
//      }
//      captcha = (Captcha) session.getAttribute(NAME);
//      CaptchaServletUtil.writeImage(response, captcha.getImage());
   }

   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

   }
   // </editor-fold>
}
