package org.atlast.beans;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.RepositoryException;

import org.atlast.beans.descriptors.Amount;
import org.atlast.beans.descriptors.DevelopmentDescriptor;
import org.atlast.beans.descriptors.LandDescriptor;
import org.atlast.beans.descriptors.RecipeDescriptor;
import org.atlast.beans.descriptors.ResourceDescriptor;
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


    public double getWages() {
        return getProperty("atlast:wages");
    }



    public Map<String, Double> getOutputs() throws RepositoryException {
        Map<String, Double> outputs = new HashMap<>();

        RecipeDescriptor recipeDescriptor = getRecipeDescriptor();
        List<Amount> amountList = recipeDescriptor.getOutputs();

        double techLevel = getPlayer().getLibrary().getTechLevel(recipeDescriptor) * 10;

        for (Amount output : amountList) {
            Double amount = Double.valueOf(output.getQuantity() / amountList.size());

            amount += 2 * getSkillLevel(recipeDescriptor.getSkill()) / 100;
            amount += 2 * techLevel / 100;
            amount *= getPops().size() / recipeDescriptor.getLabour();
            outputs.put(output.getResourceDescriptor().getName(), amount);
        }



        return outputs;
    }

    public Player getPlayer() {
        return (Player) getParentBean();
    }

}
