package org.fuwt.examples;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * User: chris
 * Date: 5/10/11
 * Time: 9:19 PM
 */
@XmlRootElement
public class GetInfoByZipResponse
{

    @XmlElement
    private String city;
    @XmlElement
    private String state;

    public void setCity(final String city)
    {
        this.city = city;
    }

    public void setState(final String state)
    {
        this.state = state;
    }

    public String getCity()
    {
        return city;
    }

    public String getState()
    {
        return state;
    }
}
