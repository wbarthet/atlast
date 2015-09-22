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

    public List<Pop> getPops() {
        return getChildBeans(Pop.class);
    }

    public double getWages() {
        return getProperty("atlast:wages");
    }

    public double getSkillLevel(String skillName) throws RepositoryException {
        Double skillLevel = 0.0;

        List<Pop> pops = getPops();
        for (Pop pop : pops) {
            skillLevel += pop.getSkill(skillName);
        }

        skillLevel /= pops.size();

        return skillLevel;
    }

    public Map<String, Double> getOutputs() throws RepositoryException {
        Map<String, Double> outputs = new HashMap<>();

        List<Amount> amountList = getRecipeDescriptor().getOutputs();
        for (Amount output : amountList) {
            Double amount = Double.valueOf(output.getQuantity() / amountList.size());

            amount += getPops().size() * 2 * getSkillLevel(getRecipeDescriptor().getSkill()) / 100;
            outputs.put(output.getResourceDescriptor().getName(), amount);
        }


        return outputs;
    }

}
