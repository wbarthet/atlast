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
        return getChildNodes(Pop.class);
    }

    public long getLabour() {
        return getLongProperty("atlast:labour");
    }

    public String getSkill() {
        return getStringProperty("atlast:skill");
    }

    public List<String> getInputs() {
        return getStringListProperty("atlast:inputs");
    }

    public List<String> getOutputs() {
        return getStringListProperty("atlast:outputs");
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

        final long labour = getLabour();
        String skillName = getSkill();
        Player player = getPlayer();
        Market market = player.getStores();

        Recipe recipe = getRecipe();

        Map<String, Double> inputs = recipe.getInputs();
        Map<String, Double> outputs = recipe.getOutputs();

        double skill = 0;

        for (Pop pop : pops) {
            skill += pop.getDoubleProperty("skill-"+skillName);
        }

        skill = skill / pops.size();

        if (pops.size() >= labour) {
            for (int i = 0; i < pops.size(); i += labour) {

                if (market.hasItems(inputs)) {
                    market.takeItems(inputs);

                    for (String output : outputs.keySet()) {
                        Double amount = outputs.get(output) / outputs.size();

                        amount += 2 * skill / 100;
                        outputs.put(output, amount);
                    }


                    market.addItems(outputs);
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
}
