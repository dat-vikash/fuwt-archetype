package org.fuwt.examples;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * User: chris
 * Date: 5/10/11
 * Time: 9:19 PM
 */
@XmlRootElement
public class GetInfoByZipResponse {

    private String city;
    private String state;

    public void setCity(final String city) {
        this.city = city;
    }

    public void setState(final String state) {
        this.state = state;
    }

    @XmlElement
    public String getCity() {
        return city;
    }

    @XmlElement
    public String getState() {
        return state;
    }
}
