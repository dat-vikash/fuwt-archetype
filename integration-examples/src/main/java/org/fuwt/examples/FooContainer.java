package org.fuwt.examples;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Container to show how to maarshall fields defined as an
 * interface using JAXB. This makes use of xsi:type in the resulting
 * xml which will have the name of the concrete type.
 *
 * User: cbarrese
 * Date: 5/20/11
 * Time: 12:52 PM
 */
@XmlRootElement(name = "request")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class FooContainer {

    @XmlElement
    private Foo foo;

    public Foo getFoo() {
        return foo;
    }

    public void setFoo(Foo foo) {
        this.foo = foo;
    }
}
