package org.fuwt.examples;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "list")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class XmlList
{
    @XmlElements(value = {@XmlElement(name = "list-item", type = SampleModel.class)})
    private List listItems = new ArrayList();

    public List listItems()
    {
        return listItems;
    }

}
