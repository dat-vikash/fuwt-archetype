package org.fuwt.examples;

import org.apache.camel.component.jpa.Consumed;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Example of a model class that is annotated for DB persistence using JPA,
 * as well as XML marshalling using JAXB.
 * <p/>
 * <p/>
 * User: chris
 * Date: 5/8/11
 * Time: 1:13 AM
 */
@XmlRootElement(name = "sample")
@Entity
@NamedQuery(name = "getAllDirtySamples", query = "select s from SampleModel s where s.syncd=false ")
public class SampleModel
{


    private String id;
    private String name;
    private boolean syncd;


    public SampleModel()
    {
    }

    public SampleModel(final String name, final boolean syncd)
    {
        this.name = name;
        this.syncd = syncd;
    }

    @Column
    @Id
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @GeneratedValue(generator = "system-uuid")
    @XmlAttribute
    public String getId()
    {
        return id;
    }

    public void setId(final String id)
    {
        this.id = id;
    }

    @Column
    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    @Column
    public boolean isSyncd()
    {
        return syncd;
    }

    public void setSyncd(final boolean syncd)
    {
        this.syncd = syncd;
    }

    @Consumed
    public void markAsSynchronized()
    {
        this.syncd = true;
    }
}
