
package Interfaz.ContabilidadPlus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para doPolizaAuto complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="doPolizaAuto">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="User" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Pass" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="item1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="item2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="item3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="item4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="item5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Headers" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="item6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="item7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="item8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="item9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="item10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="item11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="item12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="item13" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="item14" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="item15" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="item16" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="item17" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="item18" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="item19" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="item20" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="item21" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="item22" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="item23" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="item24" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="item25" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="item26" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="item27" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="item28" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="item29" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="item30" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NoCheque" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "doPolizaAuto", propOrder = {
    "user",
    "pass",
    "item1",
    "item2",
    "item3",
    "item4",
    "item5",
    "headers",
    "item6",
    "item7",
    "item8",
    "item9",
    "item10",
    "item11",
    "item12",
    "item13",
    "item14",
    "item15",
    "item16",
    "item17",
    "item18",
    "item19",
    "item20",
    "item21",
    "item22",
    "item23",
    "item24",
    "item25",
    "item26",
    "item27",
    "item28",
    "item29",
    "item30",
    "noCheque"
})
public class DoPolizaAuto {

    @XmlElement(name = "User")
    protected String user;
    @XmlElement(name = "Pass")
    protected String pass;
    protected String item1;
    protected String item2;
    protected String item3;
    protected String item4;
    protected String item5;
    @XmlElement(name = "Headers")
    protected String headers;
    protected String item6;
    protected String item7;
    protected String item8;
    protected String item9;
    protected String item10;
    protected String item11;
    protected String item12;
    protected String item13;
    protected String item14;
    protected String item15;
    protected String item16;
    protected String item17;
    protected String item18;
    protected String item19;
    protected String item20;
    protected String item21;
    protected String item22;
    protected String item23;
    protected String item24;
    protected String item25;
    protected String item26;
    protected String item27;
    protected String item28;
    protected String item29;
    protected String item30;
    @XmlElement(name = "NoCheque")
    protected String noCheque;

    /**
     * Obtiene el valor de la propiedad user.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUser() {
        return user;
    }

    /**
     * Define el valor de la propiedad user.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUser(String value) {
        this.user = value;
    }

    /**
     * Obtiene el valor de la propiedad pass.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPass() {
        return pass;
    }

    /**
     * Define el valor de la propiedad pass.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPass(String value) {
        this.pass = value;
    }

    /**
     * Obtiene el valor de la propiedad item1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItem1() {
        return item1;
    }

    /**
     * Define el valor de la propiedad item1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItem1(String value) {
        this.item1 = value;
    }

    /**
     * Obtiene el valor de la propiedad item2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItem2() {
        return item2;
    }

    /**
     * Define el valor de la propiedad item2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItem2(String value) {
        this.item2 = value;
    }

    /**
     * Obtiene el valor de la propiedad item3.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItem3() {
        return item3;
    }

    /**
     * Define el valor de la propiedad item3.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItem3(String value) {
        this.item3 = value;
    }

    /**
     * Obtiene el valor de la propiedad item4.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItem4() {
        return item4;
    }

    /**
     * Define el valor de la propiedad item4.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItem4(String value) {
        this.item4 = value;
    }

    /**
     * Obtiene el valor de la propiedad item5.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItem5() {
        return item5;
    }

    /**
     * Define el valor de la propiedad item5.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItem5(String value) {
        this.item5 = value;
    }

    /**
     * Obtiene el valor de la propiedad headers.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHeaders() {
        return headers;
    }

    /**
     * Define el valor de la propiedad headers.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHeaders(String value) {
        this.headers = value;
    }

    /**
     * Obtiene el valor de la propiedad item6.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItem6() {
        return item6;
    }

    /**
     * Define el valor de la propiedad item6.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItem6(String value) {
        this.item6 = value;
    }

    /**
     * Obtiene el valor de la propiedad item7.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItem7() {
        return item7;
    }

    /**
     * Define el valor de la propiedad item7.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItem7(String value) {
        this.item7 = value;
    }

    /**
     * Obtiene el valor de la propiedad item8.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItem8() {
        return item8;
    }

    /**
     * Define el valor de la propiedad item8.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItem8(String value) {
        this.item8 = value;
    }

    /**
     * Obtiene el valor de la propiedad item9.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItem9() {
        return item9;
    }

    /**
     * Define el valor de la propiedad item9.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItem9(String value) {
        this.item9 = value;
    }

    /**
     * Obtiene el valor de la propiedad item10.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItem10() {
        return item10;
    }

    /**
     * Define el valor de la propiedad item10.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItem10(String value) {
        this.item10 = value;
    }

    /**
     * Obtiene el valor de la propiedad item11.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItem11() {
        return item11;
    }

    /**
     * Define el valor de la propiedad item11.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItem11(String value) {
        this.item11 = value;
    }

    /**
     * Obtiene el valor de la propiedad item12.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItem12() {
        return item12;
    }

    /**
     * Define el valor de la propiedad item12.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItem12(String value) {
        this.item12 = value;
    }

    /**
     * Obtiene el valor de la propiedad item13.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItem13() {
        return item13;
    }

    /**
     * Define el valor de la propiedad item13.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItem13(String value) {
        this.item13 = value;
    }

    /**
     * Obtiene el valor de la propiedad item14.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItem14() {
        return item14;
    }

    /**
     * Define el valor de la propiedad item14.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItem14(String value) {
        this.item14 = value;
    }

    /**
     * Obtiene el valor de la propiedad item15.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItem15() {
        return item15;
    }

    /**
     * Define el valor de la propiedad item15.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItem15(String value) {
        this.item15 = value;
    }

    /**
     * Obtiene el valor de la propiedad item16.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItem16() {
        return item16;
    }

    /**
     * Define el valor de la propiedad item16.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItem16(String value) {
        this.item16 = value;
    }

    /**
     * Obtiene el valor de la propiedad item17.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItem17() {
        return item17;
    }

    /**
     * Define el valor de la propiedad item17.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItem17(String value) {
        this.item17 = value;
    }

    /**
     * Obtiene el valor de la propiedad item18.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItem18() {
        return item18;
    }

    /**
     * Define el valor de la propiedad item18.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItem18(String value) {
        this.item18 = value;
    }

    /**
     * Obtiene el valor de la propiedad item19.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItem19() {
        return item19;
    }

    /**
     * Define el valor de la propiedad item19.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItem19(String value) {
        this.item19 = value;
    }

    /**
     * Obtiene el valor de la propiedad item20.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItem20() {
        return item20;
    }

    /**
     * Define el valor de la propiedad item20.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItem20(String value) {
        this.item20 = value;
    }

    /**
     * Obtiene el valor de la propiedad item21.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItem21() {
        return item21;
    }

    /**
     * Define el valor de la propiedad item21.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItem21(String value) {
        this.item21 = value;
    }

    /**
     * Obtiene el valor de la propiedad item22.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItem22() {
        return item22;
    }

    /**
     * Define el valor de la propiedad item22.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItem22(String value) {
        this.item22 = value;
    }

    /**
     * Obtiene el valor de la propiedad item23.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItem23() {
        return item23;
    }

    /**
     * Define el valor de la propiedad item23.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItem23(String value) {
        this.item23 = value;
    }

    /**
     * Obtiene el valor de la propiedad item24.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItem24() {
        return item24;
    }

    /**
     * Define el valor de la propiedad item24.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItem24(String value) {
        this.item24 = value;
    }

    /**
     * Obtiene el valor de la propiedad item25.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItem25() {
        return item25;
    }

    /**
     * Define el valor de la propiedad item25.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItem25(String value) {
        this.item25 = value;
    }

    /**
     * Obtiene el valor de la propiedad item26.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItem26() {
        return item26;
    }

    /**
     * Define el valor de la propiedad item26.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItem26(String value) {
        this.item26 = value;
    }

    /**
     * Obtiene el valor de la propiedad item27.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItem27() {
        return item27;
    }

    /**
     * Define el valor de la propiedad item27.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItem27(String value) {
        this.item27 = value;
    }

    /**
     * Obtiene el valor de la propiedad item28.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItem28() {
        return item28;
    }

    /**
     * Define el valor de la propiedad item28.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItem28(String value) {
        this.item28 = value;
    }

    /**
     * Obtiene el valor de la propiedad item29.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItem29() {
        return item29;
    }

    /**
     * Define el valor de la propiedad item29.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItem29(String value) {
        this.item29 = value;
    }

    /**
     * Obtiene el valor de la propiedad item30.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItem30() {
        return item30;
    }

    /**
     * Define el valor de la propiedad item30.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItem30(String value) {
        this.item30 = value;
    }

    /**
     * Obtiene el valor de la propiedad noCheque.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNoCheque() {
        return noCheque;
    }

    /**
     * Define el valor de la propiedad noCheque.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNoCheque(String value) {
        this.noCheque = value;
    }

}
