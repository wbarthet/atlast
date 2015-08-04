package org.atlast.beans;

import java.util.List;

import org.hippoecm.hst.content.beans.Node;

/**
 * Created by wbarthet on 7/13/15.
 */
@Node(jcrType = "atlast:pop")
public class Pop extends AtlastObject {

    public String getName() {
        return getProperty("atlast:name");
    }

    public String getPopClass() {
        return getProperty("atlast:class");
    }

    public Double getFood() {
        return getProperty("atlast:food");
    }

    public Double getLuxuries() {
        return getProperty("atlast:luxuries");
    }

    public Double getGoods() {
        return getProperty("atlast:goods");
    }

    public Double getCash() {
        return getProperty("atlast:cash");
    }

    public Land getLand() {
        return (Land) getParentBean();
    }

}