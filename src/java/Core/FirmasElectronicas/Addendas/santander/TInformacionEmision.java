//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-147 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.09.04 at 12:15:04 PM CDT 
//


package Core.FirmasElectronicas.Addendas.santander;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Tipo para Informaci�n de Emisi�n.
 * 			
 * 
 * <p>Java class for TInformacionEmision complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TInformacionEmision">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="InformacionFactoraje" type="{http://www.santander.com.mx/schemas/xsd/AddendaSantanderV1}TFactoraje" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="codigoCliente" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="contrato" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="periodo" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="centroCostos" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="folioInterno" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="claveSantander" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TInformacionEmision", propOrder = {
    "informacionFactoraje"
})
public class TInformacionEmision {

    @XmlElement(name = "InformacionFactoraje")
    protected List<TFactoraje> informacionFactoraje;
    @XmlAttribute(name = "codigoCliente")
    protected String codigoCliente;
    @XmlAttribute(name = "contrato")
    protected String contrato;
    @XmlAttribute(name = "periodo")
    protected String periodo;
    @XmlAttribute(name = "centroCostos")
    protected String centroCostos;
    @XmlAttribute(name = "folioInterno")
    protected String folioInterno;
    @XmlAttribute(name = "claveSantander")
    protected String claveSantander;

    /**
     * Gets the value of the informacionFactoraje property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the informacionFactoraje property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInformacionFactoraje().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TFactoraje }
     * 
     * 
     */
    public List<TFactoraje> getInformacionFactoraje() {
        if (informacionFactoraje == null) {
            informacionFactoraje = new ArrayList<TFactoraje>();
        }
        return this.informacionFactoraje;
    }

    /**
     * Gets the value of the codigoCliente property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoCliente() {
        return codigoCliente;
    }

    /**
     * Sets the value of the codigoCliente property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoCliente(String value) {
        this.codigoCliente = value;
    }

    /**
     * Gets the value of the contrato property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContrato() {
        return contrato;
    }

    /**
     * Sets the value of the contrato property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContrato(String value) {
        this.contrato = value;
    }

    /**
     * Gets the value of the periodo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPeriodo() {
        return periodo;
    }

    /**
     * Sets the value of the periodo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPeriodo(String value) {
        this.periodo = value;
    }

    /**
     * Gets the value of the centroCostos property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCentroCostos() {
        return centroCostos;
    }

    /**
     * Sets the value of the centroCostos property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCentroCostos(String value) {
        this.centroCostos = value;
    }

    /**
     * Gets the value of the folioInterno property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFolioInterno() {
        return folioInterno;
    }

    /**
     * Sets the value of the folioInterno property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFolioInterno(String value) {
        this.folioInterno = value;
    }

    /**
     * Gets the value of the claveSantander property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClaveSantander() {
        return claveSantander;
    }

    /**
     * Sets the value of the claveSantander property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClaveSantander(String value) {
        this.claveSantander = value;
    }

}
