
package Core.FirmasElectronicas.SAT3_2.cancelacion;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para anonymous complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Cancelacion" type="{http://cancelacfd.sat.gob.mx}Cancelacion" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "cancelacion"
})
@XmlRootElement(name = "CancelaCFD", namespace = "http://cancelacfd.sat.gob.mx")
public class CancelaCFD {

    @XmlElement(name = "Cancelacion", namespace = "http://cancelacfd.sat.gob.mx")
    protected Cancelacion cancelacion;

    /**
     * Obtiene el valor de la propiedad cancelacion.
     * 
     * @return
     *     possible object is
     *     {@link Cancelacion }
     *     
     */
    public Cancelacion getCancelacion() {
        return cancelacion;
    }

    /**
     * Define el valor de la propiedad cancelacion.
     * 
     * @param value
     *     allowed object is
     *     {@link Cancelacion }
     *     
     */
    public void setCancelacion(Cancelacion value) {
        this.cancelacion = value;
    }

}
