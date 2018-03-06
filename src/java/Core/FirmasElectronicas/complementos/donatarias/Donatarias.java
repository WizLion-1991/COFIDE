//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-147 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.08.24 at 04:53:20 PM CDT 
//


package Core.FirmasElectronicas.complementos.donatarias;

import Core.FirmasElectronicas.Addendas.DateFormatterDate;
import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
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
 *       &lt;attribute name="version" use="required" type="{http://www.w3.org/2001/XMLSchema}string" fixed="1.1" />
 *       &lt;attribute name="noAutorizacion" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="fechaAutorizacion" use="required" type="{http://www.w3.org/2001/XMLSchema}date" />
 *       &lt;attribute name="leyenda" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
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
@XmlType(name = "")
@XmlRootElement(name = "Donatarias")
public class Donatarias {

    @XmlAttribute(name = "version", required = true)
    protected String version;
    @XmlAttribute(name = "noAutorizacion", required = true)
    protected String noAutorizacion;
    @XmlAttribute(name = "fechaAutorizacion", required = true)
    @XmlSchemaType(name = "date")
    @XmlJavaTypeAdapter(DateFormatterDate.class)
    protected Date fechaAutorizacion;
    @XmlAttribute(name = "leyenda", required = true)
    protected String leyenda;

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersion() {
        if (version == null) {
            return "1.1";
        } else {
            return version;
        }
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersion(String value) {
        this.version = value;
    }

    /**
     * Gets the value of the noAutorizacion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNoAutorizacion() {
        return noAutorizacion;
    }

    /**
     * Sets the value of the noAutorizacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNoAutorizacion(String value) {
        this.noAutorizacion = value;
    }

    /**
     * Gets the value of the fechaAutorizacion property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public Date getFechaAutorizacion() {
        return fechaAutorizacion;
    }

    /**
     * Sets the value of the fechaAutorizacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaAutorizacion(Date value) {
        this.fechaAutorizacion = value;
    }

    /**
     * Gets the value of the leyenda property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLeyenda() {
        return leyenda;
    }

    /**
     * Sets the value of the leyenda property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLeyenda(String value) {
        this.leyenda = value;
    }

}
