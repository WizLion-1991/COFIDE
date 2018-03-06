//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de enlace (JAXB) XML v2.2.5-2 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: PM.11.23 a las 07:16:55 PM CST 
//


package generated;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "footerOrBodyOrHeaderOrConfig"
})
@XmlRootElement(name = "Formato")
public class Formato {

    @XmlElements({
        @XmlElement(name = "Footer", type = Footer.class),
        @XmlElement(name = "Body", type = Body.class),
        @XmlElement(name = "Header", type = Header.class),
        @XmlElement(name = "Config", type = Config.class)
    })
    protected List<Object> footerOrBodyOrHeaderOrConfig;

    /**
     * Gets the value of the footerOrBodyOrHeaderOrConfig property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the footerOrBodyOrHeaderOrConfig property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFooterOrBodyOrHeaderOrConfig().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Footer }
     * {@link Body }
     * {@link Header }
     * {@link Config }
     * 
     * 
     */
    public List<Object> getFooterOrBodyOrHeaderOrConfig() {
        if (footerOrBodyOrHeaderOrConfig == null) {
            footerOrBodyOrHeaderOrConfig = new ArrayList<Object>();
        }
        return this.footerOrBodyOrHeaderOrConfig;
    }

}
