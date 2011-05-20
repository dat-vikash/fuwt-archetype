package org.fuwt.examples;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * User: cbarrese
 * Date: 5/20/11
 * Time: 12:45 PM
 */
@XmlJavaTypeAdapter(AbstractFoo.Adapter.class)
public interface Foo {
}
