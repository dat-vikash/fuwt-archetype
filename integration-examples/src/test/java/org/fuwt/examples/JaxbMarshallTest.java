package org.fuwt.examples;

import org.junit.Test;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.xml.transform.StringResult;

/**
 *
 * Test to show how you would implement an xsi:type mapping using JAXB
 *
 * User: cbarrese
 * Date: 5/20/11
 * Time: 12:52 PM
 */
public class JaxbMarshallTest {

    @Test
    public void shouldMarshallXsiTypeToXML()
    {
        Jaxb2Marshaller marshaller=new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(ConcreteFoo.class,
                                       FooContainer.class);


        FooContainer container=new FooContainer();
        container.setFoo(new ConcreteFoo("hdkjahdkj"));

        StringResult result=new StringResult();
        marshaller.marshal(container,result);

        System.out.println(result.toString());
    }

}
