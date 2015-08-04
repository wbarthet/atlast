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
@Node(jcrType = "atlast:land")
public class Land extends AtlastObject {


    public LandDescriptor getLandDescriptor() {
        return getDescriptor("atlast:landdescriptor", LandDescriptor.class);
    }


    public String getName() {
        return getProperty("atlast:name");
    }

    public DevelopmentDescriptor getDevelopmentDescriptor() {
        return getDescriptor("atlast:developmentdescriptor", DevelopmentDescriptor.class);
    }

    public boolean isDevelopment() {
        try {
            return getNode().isNodeType("atlast:development");
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
        return false;
    }

    public RecipeDescriptor getRecipeDescriptor() {
        return getDescriptor("atlast:recipedescriptor", RecipeDescriptor.class);
    }

    public List<Pop> getPops() {
        return getChildBeans(Pop.class);
    }

    public double getWages() {
        return getProperty("atlast:wages");
    }



}
