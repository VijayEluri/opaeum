//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-520 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.01.11 at 12:44:07 PM CST 
//


package org.hibernate.internal.jaxb.mapping.hbm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for query-element complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="query-element">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *         &lt;element name="query-param" type="{http://www.hibernate.org/xsd/hibernate-mapping}query-param-element"/>
 *       &lt;/sequence>
 *       &lt;attribute name="cache-mode" type="{http://www.hibernate.org/xsd/hibernate-mapping}cache-mode-attribute" />
 *       &lt;attribute name="cache-region" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="cacheable" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="comment" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="fetch-size" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="flush-mode" type="{http://www.hibernate.org/xsd/hibernate-mapping}flush-mode-attribute" />
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="read-only" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="timeout" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "query-element", propOrder = {
    "content"
})
public class JaxbQueryElement {

    @XmlElementRef(name = "query-param", namespace = "http://www.hibernate.org/xsd/hibernate-mapping", type = JAXBElement.class)
    @XmlMixed
    protected List<Serializable> content;
    @XmlAttribute(name = "cache-mode")
    protected JaxbCacheModeAttribute cacheMode;
    @XmlAttribute(name = "cache-region")
    protected String cacheRegion;
    @XmlAttribute
    protected Boolean cacheable;
    @XmlAttribute
    protected String comment;
    @XmlAttribute(name = "fetch-size")
    protected String fetchSize;
    @XmlAttribute(name = "flush-mode")
    protected JaxbFlushModeAttribute flushMode;
    @XmlAttribute(required = true)
    protected String name;
    @XmlAttribute(name = "read-only")
    protected Boolean readOnly;
    @XmlAttribute
    protected String timeout;

    /**
     * Gets the value of the content property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the content property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContent().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link JaxbQueryParamElement }{@code >}
     * {@link String }
     * 
     * 
     */
    public List<Serializable> getContent() {
        if (content == null) {
            content = new ArrayList<Serializable>();
        }
        return this.content;
    }

    /**
     * Gets the value of the cacheMode property.
     * 
     * @return
     *     possible object is
     *     {@link JaxbCacheModeAttribute }
     *     
     */
    public JaxbCacheModeAttribute getCacheMode() {
        return cacheMode;
    }

    /**
     * Sets the value of the cacheMode property.
     * 
     * @param value
     *     allowed object is
     *     {@link JaxbCacheModeAttribute }
     *     
     */
    public void setCacheMode(JaxbCacheModeAttribute value) {
        this.cacheMode = value;
    }

    /**
     * Gets the value of the cacheRegion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCacheRegion() {
        return cacheRegion;
    }

    /**
     * Sets the value of the cacheRegion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCacheRegion(String value) {
        this.cacheRegion = value;
    }

    /**
     * Gets the value of the cacheable property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isCacheable() {
        if (cacheable == null) {
            return false;
        } else {
            return cacheable;
        }
    }

    /**
     * Sets the value of the cacheable property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCacheable(Boolean value) {
        this.cacheable = value;
    }

    /**
     * Gets the value of the comment property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComment() {
        return comment;
    }

    /**
     * Sets the value of the comment property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComment(String value) {
        this.comment = value;
    }

    /**
     * Gets the value of the fetchSize property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFetchSize() {
        return fetchSize;
    }

    /**
     * Sets the value of the fetchSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFetchSize(String value) {
        this.fetchSize = value;
    }

    /**
     * Gets the value of the flushMode property.
     * 
     * @return
     *     possible object is
     *     {@link JaxbFlushModeAttribute }
     *     
     */
    public JaxbFlushModeAttribute getFlushMode() {
        return flushMode;
    }

    /**
     * Sets the value of the flushMode property.
     * 
     * @param value
     *     allowed object is
     *     {@link JaxbFlushModeAttribute }
     *     
     */
    public void setFlushMode(JaxbFlushModeAttribute value) {
        this.flushMode = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the readOnly property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isReadOnly() {
        return readOnly;
    }

    /**
     * Sets the value of the readOnly property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setReadOnly(Boolean value) {
        this.readOnly = value;
    }

    /**
     * Gets the value of the timeout property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTimeout() {
        return timeout;
    }

    /**
     * Sets the value of the timeout property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTimeout(String value) {
        this.timeout = value;
    }

}
