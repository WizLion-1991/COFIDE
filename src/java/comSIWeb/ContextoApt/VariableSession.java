/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package comSIWeb.ContextoApt;

import comSIWeb.Utilerias.Sesiones;
import java.io.Serializable;
import javax.servlet.http.HttpServletRequest;

/**
 * Con este objeto definimos las variables de sesion
 *
 * @author zeus
 */
public class VariableSession implements Serializable {

   private int intNoUser;
   private String strUser;
   private String strNomBase;
   private int intEsCaptura;
   private int intClienteWork;
   private int intIdCliente;
   private int intAnioWork;
   private int intIdioma;
   private int intIdEmpresa;
   private int intIdPerfil;
   private int intSucursalDefault;
   private int intSucursalMaster;
   private String strLstSucursales;
   private String strMesActual;
   private String strMskCta;
   private HttpServletRequest request;
   private int NumDigitos;
   private int NumDecimal;

   /**
    * Inicializa el objeto que contiene todas las variables de sesion que ocupa
    * el sistema
    *
    * @param request
    */
   public VariableSession(HttpServletRequest request) {
      this.request = request;
      intNoUser = 0;
      intEsCaptura = 0;
      intClienteWork = 0;
      intAnioWork = 0;
      strUser = "";
      strNomBase = "";
      intIdioma = 0;
      strLstSucursales = "";
      intSucursalDefault = 0;
      NumDigitos = 4;
      NumDecimal = 2;
      intIdCliente = 0;
      strMesActual = "";
      strMskCta = "";
   }

   /**
    * Inicializa el objeto que contiene todas las variables de sesion que ocupa
    * el sistema
    */
   public VariableSession() {
      intNoUser = 0;
      intEsCaptura = 0;
      intClienteWork = 0;
      intAnioWork = 0;
      strUser = "";
      strNomBase = "";
      intIdioma = 0;
      strLstSucursales = "";
      intSucursalDefault = 0;
      NumDigitos = 4;
      NumDecimal = 2;
      intIdCliente = 0;
   }

   /**
    * Define las variables de sesion, solo al hacer login
    *
    * @param intNoUser Es el numero de usuario
    * @param intEsCaptura Indica si es solo de captura o administrador
    * @param intClienteWork Es el cliente con el que estamos trabajando(Solo
    * contabilidad)
    * @param intAnioWork Es el anio de trabajo(Solo contabilidad)
    * @param strUser Es el nombre del usuario
    * @param strNomBase Es el nombre de la base de datos
    * @param intIdioma Es el id del idioma
    * @param strLstSucursales Es la lista de sucursales
    * @param intSucursalDefault Es la sucursal default
    */
   public void SetVars(int intNoUser, int intEsCaptura, int intClienteWork,
           int intAnioWork, String strUser, String strNomBase,
           int intIdioma, String strLstSucursales, int intSucursalDefault) {
      this.intNoUser = intNoUser;
      this.intEsCaptura = intEsCaptura;
      this.intClienteWork = intClienteWork;
      this.intAnioWork = intAnioWork;
      this.strUser = strUser;
      this.strNomBase = strNomBase;
      this.intIdioma = intIdioma;
      this.strLstSucursales = strLstSucursales;
      this.intSucursalDefault = intSucursalDefault;
      Sesiones.SetSession(request, "NoUser", this.intNoUser + "");
      Sesiones.SetSession(request, "EsCaptura", this.intEsCaptura + "");
      Sesiones.SetSession(request, "ClienteWork", this.intClienteWork + "");
      Sesiones.SetSession(request, "AnioWork", this.intAnioWork + "");
      Sesiones.SetSession(request, "User", this.strUser);
      Sesiones.SetSession(request, "NomBase", this.strNomBase);
      Sesiones.SetSession(request, "Idioma", this.intIdioma + "");
      Sesiones.SetSession(request, "SucursalDefault", this.intSucursalDefault + "");
      Sesiones.SetSession(request, "LstSucursales", this.strLstSucursales);
      Sesiones.SetSession(request, "NumDecimal", this.NumDecimal + "");
      //Escribimos cookie
   }

   /**
    * Define las variables de sesion, solo al hacer login
    *
    * @param intClienteWork Es el cliente con el que estamos trabajando(Solo
    * contabilidad)
    * @param intAnioWork Es el anio de trabajo(Solo contabilidad)
    */
   public void SetVars(int intClienteWork,
           int intAnioWork) {
      this.intClienteWork = intClienteWork;
      this.intAnioWork = intAnioWork;
      Sesiones.SetSession(request, "ClienteWork", this.intClienteWork + "");
      Sesiones.SetSession(request, "AnioWork", this.intAnioWork + "");
   }

   /**
    * Recupera las variables de sesion
    */
   public void getVars() {
      try {
         intNoUser = Integer.valueOf(Sesiones.gerVarSession(request, "NoUser"));
      } catch (NumberFormatException ex) {
         System.out.println("session: " + ex.getLocalizedMessage());
      }
      //Si no hay sesion
      if (intNoUser == 0) {
         Seguridad seg = new Seguridad();
         //Intentamos inicializar en automatico los valores de la sesion
         /*if (this.request.getHeader("X-bSS") != null && seg.ValidaURL(request)) {
          try {
          this.intNoUser = Integer.valueOf(this.request.getHeader("X-bSS"));
          this.strUser = this.request.getHeader("X-aSS");
          this.intEsCaptura = Integer.valueOf(this.request.getHeader("X-cSS"));
          this.intClienteWork = Integer.valueOf(this.request.getHeader("X-dSS"));
          this.intAnioWork = Integer.valueOf(this.request.getHeader("X-eSS"));
          this.NumDigitos = Integer.valueOf(this.request.getHeader("X-fSS"));
          this.intIdPerfil = Integer.valueOf(this.request.getHeader("X-gSS"));
          this.intIdCliente = Integer.valueOf(this.request.getHeader("X-hSS"));
          this.intSucursalDefault = Integer.valueOf(this.request.getHeader("X-iSS"));
          this.strLstSucursales = this.request.getHeader("X-jSS");
          this.intIdEmpresa = Integer.valueOf(this.request.getHeader("X-kSS"));
          this.SetVars(this.intNoUser, this.intEsCaptura, this.intClienteWork, this.intAnioWork, this.strUser,
          "", 0, this.strLstSucursales, this.intSucursalDefault);
          this.setIntIdPerfil(this.intIdPerfil);
          this.setintIdCliente(this.intIdCliente);
          this.setNumDigitos(this.NumDigitos);
          this.setIntIdEmpresa(intIdEmpresa);
          } catch (NumberFormatException e) {
          System.out.println("ERROR: Al intentar recuperar la sesion..." + e.getLocalizedMessage());
          }
          }*/
      } else {
         try {
            intEsCaptura = Integer.valueOf(Sesiones.gerVarSession(request, "EsCaptura"));
         } catch (NumberFormatException ex) {
            System.out.println(ex.getMessage());
         }
         try {
            intClienteWork = Integer.valueOf(Sesiones.gerVarSession(request, "ClienteWork"));
         } catch (NumberFormatException ex) {
            System.out.println(ex.getMessage());
         }
         try {
            intAnioWork = Integer.valueOf(Sesiones.gerVarSession(request, "AnioWork"));
         } catch (NumberFormatException ex) {
            System.out.println(ex.getMessage());
         }
         try {
            NumDigitos = Integer.valueOf(Sesiones.gerVarSession(request, "NumDigitos"));
         } catch (NumberFormatException ex) {
            System.out.println(ex.getMessage());
         }
         try {
            intIdPerfil = Integer.valueOf(Sesiones.gerVarSession(request, "intIdPerfil"));
         } catch (NumberFormatException ex) {
            System.out.println(ex.getMessage());
         }
         try {
            intIdCliente = Integer.valueOf(Sesiones.gerVarSession(request, "IdCliente"));
         } catch (NumberFormatException ex) {
            System.out.println(ex.getMessage());
         }
         try {
            intIdEmpresa = Integer.valueOf(Sesiones.gerVarSession(request, "intIdEmpresa"));
         } catch (NumberFormatException ex) {
            System.out.println(ex.getMessage());
         }
         try {
            intSucursalDefault = Integer.valueOf(Sesiones.gerVarSession(request, "SucursalDefault"));
         } catch (NumberFormatException ex) {
            System.out.println(ex.getMessage());
         }
         try {
            intSucursalMaster = Integer.valueOf(Sesiones.gerVarSession(request, "intSucursalMaster"));
         } catch (NumberFormatException ex) {
            System.out.println(ex.getMessage());
         }
         this.strLstSucursales = Sesiones.gerVarSession(request, "LstSucursales");
         this.strUser = Sesiones.gerVarSession(request, "User");
         this.strMesActual = Sesiones.gerVarSession(request, "MesActual");
         this.strMskCta = Sesiones.gerVarSession(request, "MskCta");
      }
   }

   /**
    * Recuperamos el anio de trabajo
    *
    * @return Regresa un numero entero
    */
   public int getIntAnioWork() {
      return intAnioWork;
   }

   /**
    * Recuperamos el cliente de trabajo
    *
    * @return Regresa un numero entero
    */
   public int getIntClienteWork() {
      return intClienteWork;
   }

   /**
    * Recuperamos si es solo de captura el usuario
    *
    * @return Regresa un numero entero
    */
   public int getIntEsCaptura() {
      return intEsCaptura;
   }

   /**
    * Recuperamos el numero de usuario
    *
    * @return Regresa un numero entero
    */
   public int getIntNoUser() {
      return intNoUser;
   }

   /**
    * Recuperamos el nombre de usuario
    *
    * @return Regresa una cadena
    */
   public String getStrUser() {
      return strUser;
   }

   /**
    * Recuperamos el nombre de base de datos
    *
    * @return Regresa una cadena
    */
   public String getStrNomBase() {
      return strNomBase;
   }

   public int getNumDigitos() {
      return NumDigitos;
   }

   public void setNumDigitos(int NumDigitos) {
      this.NumDigitos = NumDigitos;
      Sesiones.SetSession(request, "NumDigitos", this.NumDigitos + "");
   }

   public int getNumDecimal() {
      return NumDecimal;
   }

   public void setNumDecimal(int NumDecimal) {
      this.NumDecimal = NumDecimal;
      Sesiones.SetSession(request, "NumDecimal", this.NumDecimal + "");
   }

   public int getintIdCliente() {
      return intIdCliente;
   }

   public void setintIdCliente(int intIdCliente) {
      this.intIdCliente = intIdCliente;
      Sesiones.SetSession(request, "IdCliente", this.intIdCliente + "");
   }

   /**
    * Nos regresa el id del perfil del usuario logueado
    *
    * @return Regresa un entero con el id
    */
   public int getIntIdPerfil() {
      return intIdPerfil;
   }

   /**
    * Define el id del perfil del usuario logueado
    *
    * @param intIdPerfil Es un entero con el id
    */
   public void setIntIdPerfil(int intIdPerfil) {
      this.intIdPerfil = intIdPerfil;
      Sesiones.SetSession(request, "intIdPerfil", this.intIdPerfil + "");
   }

   /**
    * Nos regresa el id de la empresa seleccionada
    *
    * @return Regresa un entero con el id
    */
   public int getIntIdEmpresa() {
      return intIdEmpresa;
   }

   /**
    * Definimos el id de la empresa usada por el usuario actual
    *
    * @param intIdEmpresa Nos regresa el id de la empresa default
    */
   public void setIntIdEmpresa(int intIdEmpresa) {
      this.intIdEmpresa = intIdEmpresa;
      Sesiones.SetSession(request, "intIdEmpresa", this.intIdEmpresa + "");
   }

   /**
    * Obtenemos la sucursal default
    *
    * @return Nos regresa un entero con la sucursal default
    */
   public int getIntSucursalDefault() {
      return intSucursalDefault;
   }

   /**
    * Definimos la sucursal default
    *
    * @param intSucursalDefault Es el id de la sucursal default
    */
   public void setIntSucursalDefault(int intSucursalDefault) {
      this.intSucursalDefault = intSucursalDefault;
      Sesiones.SetSession(request, "SucursalDefault", this.intSucursalDefault + "");
   }

   /**
    * Obtenemos la lista de sucursales a las que tenemos acceso
    *
    * @return Nos regresa un cadena con las sucursales separadas por comas
    */
   public String getStrLstSucursales() {
      return strLstSucursales;
   }

   /**
    * Define la lista de sucursales a la que tiene acceso el cliente
    *
    * @param strLstSucursales Es la lista de sucursales
    */
   public void setStrLstSucursales(String strLstSucursales) {
      this.strLstSucursales = strLstSucursales;
      Sesiones.SetSession(request, "LstSucursales", this.strLstSucursales + "");
   }

   public int getIntSucursalMaster() {
      return intSucursalMaster;
   }

   public void setIntSucursalMaster(int intSucursalMaster) {
      this.intSucursalMaster = intSucursalMaster;
      Sesiones.SetSession(request, "intSucursalMaster", this.intSucursalMaster + "");
   }

   /**
    *Regresa el mes actual
    * @return Regresa el mes acctual de operacion (MM)
    */
   public String getStrMesActual() {
      return strMesActual;
   }

   /**
    *Definimos el mes actual
    * @param strMesActual Es el mes actual de operacion (MM)
    */
   public void setStrMesActual(String strMesActual) {
      this.strMesActual = strMesActual;
      Sesiones.SetSession(request, "MesActual", strMesActual);
   }

   /**
    *Nos regresa mascara actual para las cuentas contables
    * @return Es la mascara de las cuentas contables
    */
   public String getStrMskCta() {
      return strMskCta;
   }

   /**
    *Definimos mascara actual para las cuentas contables
    * @param strMskCta
    */
   public void setStrMskCta(String strMskCta) {
      this.strMskCta = strMskCta;
      Sesiones.SetSession(request, "MskCta", strMskCta);
   }

}
