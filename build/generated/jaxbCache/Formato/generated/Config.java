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
    "totalPagesOrHeightOrWidthOrBottomMarginOrTopMarginOrRightMarginOrLeftMarginOrOrientationOrTypePageOrTitle"
})
@XmlRootElement(name = "Config")
public class Config {

    @XmlElements({
        @XmlElement(name = "TotalPages", type = TotalPages.class),
        @XmlElement(name = "Height", type = Height.class),
        @XmlElement(name = "Width", type = Width.class),
        @XmlElement(name = "BottomMargin", type = BottomMargin.class),
        @XmlElement(name = "TopMargin", type = TopMargin.class),
        @XmlElement(name = "RightMargin", type = RightMargin.class),
        @XmlElement(name = "LeftMargin", type = LeftMargin.class),
        @XmlElement(name = "Orientation", type = Orientation.class),
        @XmlElement(name = "TypePage", type = TypePage.class),
        @XmlElement(name = "Title", type = Title.class)
    })
    protected List<Object> totalPagesOrHeightOrWidthOrBottomMarginOrTopMarginOrRightMarginOrLeftMarginOrOrientationOrTypePageOrTitle;

    /**
     * Gets the value of the totalPagesOrHeightOrWidthOrBottomMarginOrTopMarginOrRightMarginOrLeftMarginOrOrientationOrTypePageOrTitle property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the totalPagesOrHeightOrWidthOrBottomMarginOrTopMarginOrRightMarginOrLeftMarginOrOrientationOrTypePageOrTitle property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTotalPagesOrHeightOrWidthOrBottomMarginOrTopMarginOrRightMarginOrLeftMarginOrOrientationOrTypePageOrTitle().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TotalPages }
     * {@link Height }
     * {@link Width }
     * {@link BottomMargin }
     * {@link TopMargin }
     * {@link RightMargin }
     * {@link LeftMargin }
     * {@link Orientation }
     * {@link TypePage }
     * {@link Title }
     * 
     * 
     */
    public List<Object> getTotalPagesOrHeightOrWidthOrBottomMarginOrTopMarginOrRightMarginOrLeftMarginOrOrientationOrTypePageOrTitle() {
        if (totalPagesOrHeightOrWidthOrBottomMarginOrTopMarginOrRightMarginOrLeftMarginOrOrientationOrTypePageOrTitle == null) {
            totalPagesOrHeightOrWidthOrBottomMarginOrTopMarginOrRightMarginOrLeftMarginOrOrientationOrTypePageOrTitle = new ArrayList<Object>();
        }
        return this.totalPagesOrHeightOrWidthOrBottomMarginOrTopMarginOrRightMarginOrLeftMarginOrOrientationOrTypePageOrTitle;
    }

}
