package org.atlast.beans;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.RepositoryException;

import org.atlast.beans.descriptors.Amount;
import org.atlast.beans.descriptors.RecipeDescriptor;
import org.hippoecm.hst.content.beans.Node;

/**
 * Created by wbarthet on 7/13/15.
 */
@Node(jcrType = "atlast:storehouse")
public class Storehouse extends AtlastObject {

    public Player getPlayer() {
        return (Player) getParentBean();
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

        for (Amount output : amountList) {
            Double amount = Double.valueOf(output.getQuantity() / amountList.size());

            amount = 2 + getPops().size() * (2 + 2 * getSkillLevel(recipeDescriptor.getSkill()) / 100);
            
            amount *= 10;
            outputs.put(output.getResourceDescriptor().getName(), amount);
        }


        return outputs;
    }

    public double getTrade() {
        return getProperty("atlast:trade");
    }

}
