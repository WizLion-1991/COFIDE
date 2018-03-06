package ERP.BusinessEntities;

/**
 * Representa un item de una partida contable
 *
 * @author zeus
 */
public class PoliCtas {

   private String strCuenta;
   private String strFolioRef;
   private double dblImporte;
   private boolean bolEsCargo;
   private String strUUID;
   private String strRFC;
   private String strNotas;
   private double dblMonto;

   /**
    * Contructor inicializando
    *
    * @param strCuenta Cuenta
    * @param dblImporte Importe
    * @param bolEsCargo Cargo
    * @param strFolioRef Es el folio o referencia
    */
   public PoliCtas(String strCuenta, double dblImporte, boolean bolEsCargo, String strFolioRef) {
      this.strCuenta = strCuenta;
      this.strFolioRef = strFolioRef;
      this.dblImporte = dblImporte;
      this.bolEsCargo = bolEsCargo;
      this.strNotas = "";
   }

   /**
    * Constructor vacio
    */
   public PoliCtas() {
      this.strCuenta = "";
      this.strFolioRef = "";
      this.dblImporte = 0.0;
      this.bolEsCargo = false;
      this.strNotas = "";
   }

   /**
    * Regresa el importe de la cuenta contable
    *
    * @return Es el importe
    */
   public double getDblImporte() {
      return dblImporte;
   }

   /**
    * Define el importe de la cuenta contable
    *
    * @param dblImporte Es el importe
    */
   public void setDblImporte(double dblImporte) {
      this.dblImporte = dblImporte;
   }

   /**
    * Regresa la cuenta contable
    *
    * @return String con el valor de la cuenta contable
    */
   public String getStrCuenta() {
      return strCuenta;
   }

   /**
    * Es la cuenta contable
    *
    * @param strCuenta String con el valor de la cuenta contable
    */
   public void setStrCuenta(String strCuenta) {
      this.strCuenta = strCuenta;
   }

   /**
    * Regresa si es un cargo
    *
    * @return Regresa un boolean
    */
   public boolean isBolEsCargo() {
      return bolEsCargo;
   }

   /**
    * Define si es un cargo
    *
    * @param bolEsCargo Define un boolean
    */
   public void setBolEsCargo(boolean bolEsCargo) {
      this.bolEsCargo = bolEsCargo;
   }

   /**
    * Regresa el folio o referencia
    *
    * @return Es un string
    */
   public String getStrFolioRef() {
      return strFolioRef;
   }

   /**
    * Es el folio o referencia
    *
    * @param strFolioRef Es un string
    */
   public void setStrFolioRef(String strFolioRef) {
      this.strFolioRef = strFolioRef;
   }

   /**
    *Regresa el uuid
    * @return
    */
   public String getStrUUID() {
      return strUUID;
   }

   /**
    *Es el uuid
    * @param strUUID
    */
   public void setStrUUID(String strUUID) {
      this.strUUID = strUUID;
   }

   /**
    *Regresa el rfc
    * @return
    */
   public String getStrRFC() {
      return strRFC;
   }

   /**
    *Define el rfc
    * @param strRFC
    */
   public void setStrRFC(String strRFC) {
      this.strRFC = strRFC;
   }

   /**
    *Regresa el monto pagado
    * @return
    */
   public double getDblMonto() {
      return dblMonto;
   }

   /**
    *Define el monto pagado
    * @param dblMonto
    */
   public void setDblMonto(double dblMonto) {
      this.dblMonto = dblMonto;
   }

   /**
    *Regresa las notas
    * @return Es un texto con las notas
    */
   public String getStrNotas() {
      return strNotas;
   }

   /**
    *Define las notas del detalle
    * @param strNotas Es un texto con las notas
    */
   public void setStrNotas(String strNotas) {
      this.strNotas = strNotas;
   }
   
}
