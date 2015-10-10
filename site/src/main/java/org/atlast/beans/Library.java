package org.atlast.beans;

import java.util.List;

import javax.jcr.RepositoryException;

import org.atlast.beans.descriptors.RecipeDescriptor;
import org.hippoecm.hst.content.beans.Node;

/**
 * Created by wbarthet on 7/13/15.
 */
@Node(jcrType = "atlast:library")
public class Library extends AtlastObject {


    public boolean hasRecipe(final RecipeDescriptor recipeDescriptor) throws RepositoryException {

        List<Technology> technologies = getChildBeans(Technology.class);

        for (Technology technology : technologies) {
            if (recipeDescriptor.getNode().getParent().getIdentifier().equals(technology.getNode().getName())) {
                return true;
            }
        }

        return false;
    }

    public double getTechLevel(RecipeDescriptor recipeDescriptor) throws RepositoryException {
        List<Technology> technologies = getChildBeans(Technology.class);

        for (Technology technology : technologies) {
            if (recipeDescriptor.getNode().getParent().getIdentifier().equals(technology.getNode().getName())) {
                return technology.getProperty("atlast:level");
            }
        }

        return 0.0;
    }
}
