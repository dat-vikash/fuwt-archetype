package org.fuwt.examples;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * User: chris
 * Date: 5/10/11
 * Time: 7:45 PM
 */
@XmlRootElement(name = "GetInfoByZIP",namespace = "http://www.webserviceX.NET")
@XmlAccessorType(XmlAccessType.FIELD)
public class GetInfoByZipRequest
{
    @XmlElement(name = "USZip")
    private String usZip;

    public GetInfoByZipRequest()
    {
    }

    public GetInfoByZipRequest(final String usZip)
    {
        this.usZip = usZip;
    }

    public String getUsZip()
    {
        return usZip;
    }

    public void setUsZip(final String usZip)
    {
        this.usZip = usZip;
    }


}
