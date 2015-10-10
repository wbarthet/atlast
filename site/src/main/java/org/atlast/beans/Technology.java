package org.atlast.beans;

import javax.jcr.RepositoryException;

import org.atlast.beans.descriptors.RecipeDescriptor;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.ObjectBeanManagerException;

/**
 * Created by wbarthet on 7/13/15.
 */
@Node(jcrType = "atlast:technology")
public class Technology extends AtlastObject {

    public RecipeDescriptor getRecipe() throws RepositoryException, ObjectBeanManagerException {
        if (!isInnovation()) {
            String uuid = getNode().getName();

            return (RecipeDescriptor) getObjectConverter().getObject(uuid, getNode().getSession());
        }
        else {
            return null;
        }
    }

    public double getProgress() {
        double progress = getProperty("atlast:progress");
        double level = getLevel();


        double threshold = Math.pow(2, level) * 10 + getLibrary().getTechnology().size() * 10;

        return (100/threshold)*progress;
    }

    public double getLevel() {
        return getProperty("atlast:level");
    }

    public Library getLibrary() {
        return (Library) getParentBean();
    }

    public boolean isInnovation() throws RepositoryException {
        return "innovation".equals(getNode().getName());
    }
}
