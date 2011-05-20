package org.fuwt.examples;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Created by IntelliJ IDEA.
 * User: cbarrese
 * Date: 5/20/11
 * Time: 12:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class AbstractFoo implements Foo {

    static class Adapter extends XmlAdapter<AbstractFoo, Foo> {
        public Foo unmarshal(AbstractFoo v) {
            return v;
        }

        public AbstractFoo marshal(Foo v) {
            return (AbstractFoo) v;
        }
    }
}
