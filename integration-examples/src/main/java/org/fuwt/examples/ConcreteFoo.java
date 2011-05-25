package org.fuwt.examples;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * User: cbarrese
 * Date: 5/20/11
 * Time: 12:50 PM
 */
@XmlAccessorType(value = XmlAccessType.FIELD)
public class ConcreteFoo extends AbstractFoo{
    @XmlElement
    private String name;


    public ConcreteFoo() {
    }

    public ConcreteFoo(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
