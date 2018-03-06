/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comSIWeb.ContextoApt.entidades;

/**
 * Representa un permiso
 *
 * @author ZeusGalindo
 */
public class Permiso {

   // <editor-fold defaultstate="collapsed" desc="Propiedades">
   private int intIdPermiso = 0;
   private int intIdPerfil = 0;
   private String strDescripcionPermiso;
   private String strDescripcionPerfil;
   private String strNombreUsuario;
   private boolean bolEsPermiso;
   private boolean bolEsMenu;

   public int getIntIdPermiso() {
      return intIdPermiso;
   }

   public void setIntIdPermiso(int intIdPermiso) {
      this.intIdPermiso = intIdPermiso;
   }

   public int getIntIdPerfil() {
      return intIdPerfil;
   }

   public void setIntIdPerfil(int intIdPerfil) {
      this.intIdPerfil = intIdPerfil;
   }

   public String getStrDescripcionPermiso() {
      return strDescripcionPermiso;
   }

   public void setStrDescripcionPermiso(String strDescripcionPermiso) {
      this.strDescripcionPermiso = strDescripcionPermiso;
   }

   public String getStrDescripcionPerfil() {
      return strDescripcionPerfil;
   }

   public void setStrDescripcionPerfil(String strDescripcionPerfil) {
      this.strDescripcionPerfil = strDescripcionPerfil;
   }

   public boolean isBolEsPermiso() {
      return bolEsPermiso;
   }

   public void setBolEsPermiso(boolean bolEsPermiso) {
      this.bolEsPermiso = bolEsPermiso;
   }

   public boolean isBolEsMenu() {
      return bolEsMenu;
   }

   public void setBolEsMenu(boolean bolEsMenu) {
      this.bolEsMenu = bolEsMenu;
   }

   public String getStrNombreUsuario() {
      return strNombreUsuario;
   }

   public void setStrNombreUsuario(String strNombreUsuario) {
      this.strNombreUsuario = strNombreUsuario;
   }
   // </editor-fold>

   // <editor-fold defaultstate="collapsed" desc="Constructores">
   // </editor-fold>
   // <editor-fold defaultstate="collapsed" desc="Metodos">
   // </editor-fold>
}
