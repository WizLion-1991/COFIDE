//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-147 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.11.23 at 11:09:20 AM CST 
//


package Core.FirmasElectronicas.SAT_SONOCO;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the Sonoco package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: Sonoco
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Factura.Proveedor }
     * 
     */
    public Factura.Proveedor createFacturaProveedor() {
        return new Factura.Proveedor();
    }

    /**
     * Create an instance of {@link Factura.Moneda }
     * 
     */
    public Factura.Moneda createFacturaMoneda() {
        return new Factura.Moneda();
    }

    /**
     * Create an instance of {@link Factura }
     * 
     */
    public Factura createFactura() {
        return new Factura();
    }

    /**
     * Create an instance of {@link Factura.Entrega }
     * 
     */
    public Factura.Entrega createFacturaEntrega() {
        return new Factura.Entrega();
    }

}
