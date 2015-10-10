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

    public List<Pop> getPops() {
        return getChildBeans(Pop.class);
    }

    public String getFocus() {
        String focus = getProperty("atlast:focus");

        focus = "ascending".equals(focus) ? "Innovation" : "Improvement";

        return focus;
    }

    public double getWages() {
        return getProperty("atlast:wages");
    }


    public double getOutput() throws RepositoryException {
        double amount = 0.0;

        List<Pop> pops = getPops();

        double skill = getSkillLevel("scientist");

        amount +=  2 + pops.size() * (2 + 2 * skill / 100);

        return amount;
    }

    public Player getPlayer() {
        return (Player) getParentBean();
    }

    public List<Technology> getTechnology() {
        return getChildBeans(Technology.class);
    }
}
