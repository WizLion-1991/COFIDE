//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-147 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.11.23 at 11:09:20 AM CST 
//


package Core.FirmasElectronicas.SAT_SONOCO;

import Core.FirmasElectronicas.Addendas.ShortDateFormatterGuion;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Moneda">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="tipoMoneda" use="required">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                       &lt;length value="3"/>
 *                       &lt;enumeration value="MXN"/>
 *                       &lt;enumeration value="USD"/>
 *                       &lt;enumeration value="XEU"/>
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Proveedor">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="codigo" use="required">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                       &lt;length value="9"/>
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Entrega">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="plantaEntrega" use="required">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *                       &lt;totalDigits value="3"/>
 *                       &lt;enumeration value="001"/>
 *                       &lt;enumeration value="002"/>
 *                       &lt;enumeration value="003"/>
 *                       &lt;enumeration value="004"/>
 *                       &lt;enumeration value="005"/>
 *                       &lt;enumeration value="006"/>
 *                       &lt;enumeration value="007"/>
 *                       &lt;enumeration value="008"/>
 *                       &lt;enumeration value="009"/>
 *                       &lt;enumeration value="010"/>
 *                       &lt;enumeration value="011"/>
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *                 &lt;attribute name="codigoPostal" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="version" use="required" fixed="1.0">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="tipoDocumento" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;minLength value="1"/>
 *             &lt;maxLength value="15"/>
 *             &lt;enumeration value="FACTURA"/>
 *             &lt;enumeration value="NOTA CREDITO"/>
 *             &lt;enumeration value="NOTA CARGO"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="folio" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;minLength value="1"/>
 *             &lt;maxLength value="20"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="fecha" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}date">
 *             &lt;whiteSpace value="collapse"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "moneda",
    "proveedor",
    "entrega"
})
@XmlRootElement(name = "Factura")
public class Factura {

    @XmlElement(name = "Moneda", required = true)
    protected Factura.Moneda moneda;
    @XmlElement(name = "Proveedor", required = true)
    protected Factura.Proveedor proveedor;
    @XmlElement(name = "Entrega", required = true)
    protected Factura.Entrega entrega;
    @XmlAttribute(name = "version", required = true)
    protected BigDecimal version;
    @XmlAttribute(name = "tipoDocumento", required = true)
    protected String tipoDocumento;
    @XmlAttribute(name = "folio", required = true)
    protected String folio;
    @XmlAttribute(name = "fecha", required = true)
    @XmlSchemaType(name = "date")
    @XmlJavaTypeAdapter(ShortDateFormatterGuion.class)
    protected Date fecha;

    /**
     * Gets the value of the moneda property.
     * 
     * @return
     *     possible object is
     *     {@link Factura.Moneda }
     *     
     */
    public Factura.Moneda getMoneda() {
        return moneda;
    }

    /**
     * Sets the value of the moneda property.
     * 
     * @param value
     *     allowed object is
     *     {@link Factura.Moneda }
     *     
     */
    public void setMoneda(Factura.Moneda value) {
        this.moneda = value;
    }

    /**
     * Gets the value of the proveedor property.
     * 
     * @return
     *     possible object is
     *     {@link Factura.Proveedor }
     *     
     */
    public Factura.Proveedor getProveedor() {
        return proveedor;
    }

    /**
     * Sets the value of the proveedor property.
     * 
     * @param value
     *     allowed object is
     *     {@link Factura.Proveedor }
     *     
     */
    public void setProveedor(Factura.Proveedor value) {
        this.proveedor = value;
    }

    /**
     * Gets the value of the entrega property.
     * 
     * @return
     *     possible object is
     *     {@link Factura.Entrega }
     *     
     */
    public Factura.Entrega getEntrega() {
        return entrega;
    }

    /**
     * Sets the value of the entrega property.
     * 
     * @param value
     *     allowed object is
     *     {@link Factura.Entrega }
     *     
     */
    public void setEntrega(Factura.Entrega value) {
        this.entrega = value;
    }

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getVersion() {
        if (version == null) {
            return new BigDecimal("1.0");
        } else {
            return version;
        }
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setVersion(BigDecimal value) {
        this.version = value;
    }

    /**
     * Gets the value of the tipoDocumento property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoDocumento() {
        return tipoDocumento;
    }

    /**
     * Sets the value of the tipoDocumento property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoDocumento(String value) {
        this.tipoDocumento = value;
    }

    /**
     * Gets the value of the folio property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFolio() {
        return folio;
    }

    /**
     * Sets the value of the folio property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFolio(String value) {
        this.folio = value;
    }

    /**
     * Gets the value of the fecha property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * Sets the value of the fecha property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFecha(Date value) {
        this.fecha = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;attribute name="plantaEntrega" use="required">
     *         &lt;simpleType>
     *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
     *             &lt;totalDigits value="3"/>
     *             &lt;enumeration value="001"/>
     *             &lt;enumeration value="002"/>
     *             &lt;enumeration value="003"/>
     *             &lt;enumeration value="004"/>
     *             &lt;enumeration value="005"/>
     *             &lt;enumeration value="006"/>
     *             &lt;enumeration value="007"/>
     *             &lt;enumeration value="008"/>
     *             &lt;enumeration value="009"/>
     *             &lt;enumeration value="010"/>
     *             &lt;enumeration value="011"/>
     *           &lt;/restriction>
     *         &lt;/simpleType>
     *       &lt;/attribute>
     *       &lt;attribute name="codigoPostal" type="{http://www.w3.org/2001/XMLSchema}integer" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Entrega {

        @XmlAttribute(name = "plantaEntrega", required = true)
        protected BigInteger plantaEntrega;
        @XmlAttribute(name = "codigoPostal")
        protected BigInteger codigoPostal;

        /**
         * Gets the value of the plantaEntrega property.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getPlantaEntrega() {
            return plantaEntrega;
        }

        /**
         * Sets the value of the plantaEntrega property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setPlantaEntrega(BigInteger value) {
            this.plantaEntrega = value;
        }

        /**
         * Gets the value of the codigoPostal property.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getCodigoPostal() {
            return codigoPostal;
        }

        /**
         * Sets the value of the codigoPostal property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setCodigoPostal(BigInteger value) {
            this.codigoPostal = value;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;attribute name="tipoMoneda" use="required">
     *         &lt;simpleType>
     *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *             &lt;length value="3"/>
     *             &lt;enumeration value="MXN"/>
     *             &lt;enumeration value="USD"/>
     *             &lt;enumeration value="XEU"/>
     *           &lt;/restriction>
     *         &lt;/simpleType>
     *       &lt;/attribute>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Moneda {

        @XmlAttribute(name = "tipoMoneda", required = true)
        protected String tipoMoneda;

        /**
         * Gets the value of the tipoMoneda property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTipoMoneda() {
            return tipoMoneda;
        }

        /**
         * Sets the value of the tipoMoneda property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTipoMoneda(String value) {
            this.tipoMoneda = value;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;attribute name="codigo" use="required">
     *         &lt;simpleType>
     *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *             &lt;length value="9"/>
     *           &lt;/restriction>
     *         &lt;/simpleType>
     *       &lt;/attribute>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Proveedor {

        @XmlAttribute(name = "codigo", required = true)
        protected String codigo;

        /**
         * Gets the value of the codigo property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCodigo() {
            return codigo;
        }

        /**
         * Sets the value of the codigo property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCodigo(String value) {
            this.codigo = value;
        }

    }

}
