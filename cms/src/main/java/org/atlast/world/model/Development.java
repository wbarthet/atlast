package org.atlast.world.model;

import java.util.List;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

/**
 * Created by wbarthet on 7/13/15.
 */
public class Development extends AtlastObject {


    public Development(Node node) throws RepositoryException {
        super(node);
    }

    public List<Pop> getPops() {
        return getChildNodes(Pop.class, "atlast:pop");
    }


    public Recipe getRecipe() throws RepositoryException {
        return new Recipe(getNode().getSession(), getStringProperty("atlast:recipedescriptor"));
    }

    public double getWages() {
        return getDoubleProperty("atlast:wages");
    }


    public Player getPlayer() {
        try {
            return new Player(getNode().getParent());
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void produce() throws RepositoryException {
        List<Pop> pops = getPops();

        Player player = getPlayer();
        Market market = player.getStores();

        Recipe recipe = getRecipe();
        final long labour = recipe.getLabour();
        final String skillName = recipe.getSkill();

        Map<String, Double> inputs = recipe.getInputs();
        Map<String, Double> outputs = recipe.getOutputs();

        double skill = 0;

        for (Pop pop : pops) {
            skill += pop.getDoubleProperty("skill-"+skillName);
        }

        skill = skill / pops.size();

        double techLevel = player.getLibrary().getTechLevel(recipe) * 10;

        if (pops.size() >= labour && labour != 0) {
            for (int i = 0; i < pops.size(); i += labour) {

                if (market.hasItems(inputs)) {
                    market.takeItems(inputs);

                    for (String output : outputs.keySet()) {
                        Double amount = outputs.get(output) / outputs.size();

                        amount += 2 * skill / 100;
                        amount += 2 * techLevel / 100;
                        outputs.put(output, amount);
                    }

                    market.addItems(outputs);
                }
            }

        }

        for (Pop pop : pops) {
            final double wages = getWages();
            player.pay(wages);
            pop.getPaid(wages);
            pop.learn(skillName);
        }
    }
}
