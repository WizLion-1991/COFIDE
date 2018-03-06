
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
 *         &lt;element name="CancelaCFDResult" type="{http://cancelacfd.sat.gob.mx}Acuse" minOccurs="0"/>
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
    "cancelaCFDResult"
})
@XmlRootElement(name = "CancelaCFDResponse", namespace = "http://cancelacfd.sat.gob.mx")
public class CancelaCFDResponse {

    @XmlElement(name = "CancelaCFDResult", namespace = "http://cancelacfd.sat.gob.mx")
    protected Acuse cancelaCFDResult;

    /**
     * Obtiene el valor de la propiedad cancelaCFDResult.
     * 
     * @return
     *     possible object is
     *     {@link Acuse }
     *     
     */
    public Acuse getCancelaCFDResult() {
        return cancelaCFDResult;
    }

    /**
     * Define el valor de la propiedad cancelaCFDResult.
     * 
     * @param value
     *     allowed object is
     *     {@link Acuse }
     *     
     */
    public void setCancelaCFDResult(Acuse value) {
        this.cancelaCFDResult = value;
    }

}
