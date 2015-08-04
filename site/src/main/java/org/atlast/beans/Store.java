package org.atlast.beans;

import java.util.List;

import org.hippoecm.hst.content.beans.Node;

/**
 * Created by wbarthet on 7/13/15.
 */
@Node(jcrType = "atlast:store")
public class Store extends AtlastObject {

    public double getResource(String name) {
        return getProperty(name);
    }

    public String getName() {
        return getProperty("atlast:name");
    }

    public String[] getItems() {
        return getProperty("atlast:items");
    }

    public double getResourceLevel(String name) {
        return getProperty(name+"-level");
    }
}
