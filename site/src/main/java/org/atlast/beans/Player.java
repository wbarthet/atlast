package org.atlast.beans;

import java.util.List;

import javax.jcr.RepositoryException;

import org.atlast.beans.descriptors.DevelopmentDescriptor;
import org.atlast.beans.descriptors.LandDescriptor;
import org.atlast.beans.descriptors.RecipeDescriptor;
import org.hippoecm.hst.content.beans.Node;

/**
 * Created by wbarthet on 7/13/15.
 */
@Node(jcrType = "atlast:player")
public class Player extends AtlastObject {


    public List<Land> getLands() {
        return getChildBeans(Land.class);
    }

    public double getCash() {
        return getProperty("atlast:cash");
    }

    public String getName() {
        try {
            return getNode().getName();
        } catch (RepositoryException e) {
            return "__noplayer__";
        }
    }

}