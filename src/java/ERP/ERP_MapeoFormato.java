package ERP;

/**
 * Esta clase genera el mapeo de los formatos de impresion de facturas
 *
 * @author zeus
 */
public class ERP_MapeoFormato {

    private int intTipoComp = 0;
    /**
     * Es la constante para el tipo de comprobante Factura Comercial
     */
    public static final int FACTURA_COMERCIAL = 1;
    /**
     * Es la constante para el tipo de comprobante Factura de Servicios
     */
    public static final int FACTURA_SERVICIOS = 2;
    /**
     * Es la constante para el tipo de comprobante Recibo de Honorarios
     */
    public static final int RECIBO_HONORARIOS_ARRENDAMIENTO = 3;
    /**
     * Es la constante para el tipo de comprobante Factura de Importacion
     */
    public static final int IMPORTACION = 4;
    /**
     * Es la constante para el tipo de comprobante Nota de credito comercial
     */
    public static final int NCREDITO_COMERCIAL = 5;
    /**
     * Es la constante para el tipo de comprobante Nota de credito de servicios
     */
    public static final int NCREDITO_SERVICIOS = 6;
    /**
     * Es la constante para el tipo de comprobante Nota de credito de servicios
     */
    public static final int RECIBO_NOMINA = 7;
    /**
     * Es la constante para el tipo de comprobante Nota de cargo
     */
    public static final int NOTA_DE_CARGO = 8;
    /**
     * Es la constante para el tipo de comprobante Nota de cargo
     */
    public static final int NOTA_DE_CARGO_PROVEEDOR = 9;
    /**
     * Es la constante para TICKETS MASIVOS
     */
    public static final int TICKET = 10;
    private String strNomFormato;
    private String strNomArchivo;

    /**
     * Constructor del tipo de comprobante
     *
     * @param intTipoComp Es el id del tipo de comprobante, use las constantes
     */
    public ERP_MapeoFormato(int intTipoComp) {
        this.intTipoComp = intTipoComp;
        this.strNomFormato = "";
        this.strNomArchivo = "";
        MapeaFormatoDo();

    }

    /**
     * Regresa el tipo de comprobante mapeado
     *
     * @return Regresa el numero que identifica el tipo de comprobante
     */
    public int getIntTipoComp() {
        return intTipoComp;
    }

    /**
     * Establece el tipo de comprobante
     *
     * @param intTipoComp Es el numero del tipo de comprobante
     */
    public void setIntTipoComp(int intTipoComp) {
        this.intTipoComp = intTipoComp;
        MapeaFormatoDo();
    }

    /**
     * Mapea los formatos de acuerdo al tipo de comprobante
     */
    private void MapeaFormatoDo() {
        this.strNomFormato = "";
        this.strNomArchivo = "";
        if (this.intTipoComp == ERP_MapeoFormato.FACTURA_COMERCIAL) {
            this.strNomFormato = "FACTURA";
            this.strNomArchivo = "Factura";
        }
        //FACTURA DE SERVICIOS
        if (this.intTipoComp == ERP_MapeoFormato.FACTURA_SERVICIOS) {
            this.strNomFormato = "SERVICIO";
            this.strNomArchivo = "FacturaS";
        }
        //FACTURA DE HONORARIOS ARRENDAMIENTO
        if (this.intTipoComp == ERP_MapeoFormato.RECIBO_HONORARIOS_ARRENDAMIENTO) {
            this.strNomFormato = "HONORARIO";
            this.strNomArchivo = "FacturaH";
        }
        //FACTURA DE IMPORTACION
        if (this.intTipoComp == ERP_MapeoFormato.IMPORTACION) {
            this.strNomFormato = "IMPORTA";
            this.strNomArchivo = "FacturaI";
        }
        //NOTA DE CREDITO COMERCIAL
        if (this.intTipoComp == ERP_MapeoFormato.NCREDITO_COMERCIAL) {
            this.strNomFormato = "NCREDITO";
            this.strNomArchivo = "NCreditoC";
        }
        //NOTA DE CREDITO DE SERVICIOS
        if (this.intTipoComp == ERP_MapeoFormato.NCREDITO_SERVICIOS) {
            this.strNomFormato = "NCREDITOSV";
            this.strNomArchivo = "NcreditoS";
        }
        //NOTA DE CARGO
        if (this.intTipoComp == ERP_MapeoFormato.NOTA_DE_CARGO) {
            this.strNomFormato = "NOTACARGO";
            this.strNomArchivo = "NCargo";
        }
        //NOTA DE CARGO PROVEEDOR
        if (this.intTipoComp == ERP_MapeoFormato.NOTA_DE_CARGO_PROVEEDOR) {
            this.strNomFormato = "NOTACARGOPROV";
            this.strNomArchivo = "NCargoProv";
        }
        //TICKETS Masivos
        if (this.intTipoComp == ERP_MapeoFormato.TICKET) {
            this.strNomFormato = "TICKET";
            this.strNomArchivo = "Ticket";
        }
    }

    /**
     * Regresa el nombre del archivo del comprobante
     *
     * @return Es una cadena de texto
     */
    public String getStrNomArchivo() {
        return strNomArchivo;
    }

    /**
     * Regresa el nombre del formato de impresion del tipo de comprobante
     *
     * @return Es una cadena de texto
     */
    public String getStrNomFormato() {
        return strNomFormato;
    }

    /**
     * Regresa el nombre del archivo XML
     *
     * @param strTipo Es el tipo de XML
     * @return Es una cadena de texto
     */
    public String getStrNomXML(String strTipo) {
        String strNomFormatoXmlTmp = null;
        if (strTipo.equals("FACTURA")) {
            strNomFormatoXmlTmp = "CFDI_%nombre_receptor%_%fecha%_%UUID%.xml";
        }
        if (strTipo.equals("NCREDITO")) {
            strNomFormatoXmlTmp = "CFDI_%nombre_receptor%_%fecha%_%UUID%.xml";
        }
        if (strTipo.equals("NOMINA")) {
            strNomFormatoXmlTmp = "CFDI_%nombre_receptor%_%fecha%_%UUID%.xml";
        }
        if (strTipo.equals("NOTACARGO")) {
            strNomFormatoXmlTmp = "CFDI_NotaCargo_%nombre_receptor%_%fecha%_%UUID%.xml";
        }
        if (strTipo.equals("NOTACARGOPROV")) {
            strNomFormatoXmlTmp = "CFDI_NotaCargoProveedor_%nombre_receptor%_%fecha%_%UUID%.xml";
        }
        return strNomFormatoXmlTmp;
    }

    /**
     * Regresa el nombre del archivo PDF por generar
     *
     * @param strTipo Es el tipo de XML
     * @return Es una cadena de texto
     */
    public String getStrNomPDF(String strTipo) {
        String strNomFormatoPDFtmp = null;
        if (strTipo.equals("FACTURA")) {
            strNomFormatoPDFtmp = "CFDI_%nombre_receptor%_%fecha%_%UUID%.pdf";
        }
        if (strTipo.equals("NCREDITO")) {
            strNomFormatoPDFtmp = "CFDI_%nombre_receptor%_%fecha%_%UUID%.pdf";
        }
        if (strTipo.equals("NOMINA")) {
            strNomFormatoPDFtmp = "CFDI_%nombre_receptor%_%fecha%_%UUID%.pdf";
        }
        if (strTipo.equals("RECIBO")) {
            strNomFormatoPDFtmp = "RECIBO_%nombre_receptor%_%fecha%_%UUID%.pdf";
        }
        if (strTipo.equals("NOTACARGO")) {
            strNomFormatoPDFtmp = "CFDI_NotaCargo_%nombre_receptor%_%fecha%_%UUID%.pdf";
        }
        return strNomFormatoPDFtmp;
    }
}
