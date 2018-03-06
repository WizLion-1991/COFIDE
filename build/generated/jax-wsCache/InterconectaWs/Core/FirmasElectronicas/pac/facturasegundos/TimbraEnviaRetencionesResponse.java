
package Core.FirmasElectronicas.pac.facturasegundos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para timbraEnviaRetencionesResponse complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="timbraEnviaRetencionesResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="return" type="{http://ws.interconecta.tsp.com/}wsGenericRespExt" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "timbraEnviaRetencionesResponse", propOrder = {
    "_return"
})
public class TimbraEnviaRetencionesResponse {

    @XmlElement(name = "return")
    protected WsGenericRespExt _return;

    /**
     * Obtiene el valor de la propiedad return.
     * 
     * @return
     *     possible object is
     *     {@link WsGenericRespExt }
     *     
     */
    public WsGenericRespExt getReturn() {
        return _return;
    }

    /**
     * Define el valor de la propiedad return.
     * 
     * @param value
     *     allowed object is
     *     {@link WsGenericRespExt }
     *     
     */
    public void setReturn(WsGenericRespExt value) {
        this._return = value;
    }

}
