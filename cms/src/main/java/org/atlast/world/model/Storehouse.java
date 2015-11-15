package org.atlast.world.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;


/**
 * Created by wbarthet on 7/13/15.
 */
public class Storehouse extends AtlastObject {


    private static final Double BASE_TRADE = 2.0;

    public Storehouse(Node node) throws RepositoryException {
        super(node);
    }

    public List<Pop> getPops() {
        return getChildNodes(Pop.class, "atlast:pop");
    }

    public List<Technology> getTechnology() {
        return getChildNodes(Technology.class, "atlast:technology");
    }

    public Player getPlayer() {
        try {
            return new Player(getNode().getParent());
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Recipe getRecipe() throws RepositoryException {
        return new Recipe(getNode().getSession(), getStringProperty("atlast:recipedescriptor"));
    }

    public double getWages() {
        return getDoubleProperty("atlast:wages");
    }

    public void trade() throws RepositoryException {

        List<Pop> pops = getPops();

        Player player = getPlayer();
        Market market = player.getStores();

        double skill = 0;

        double trade;

        Recipe recipe = getRecipe();
        long labour = recipe.getLabour();
        Map<String, Double> inputs = recipe.getInputs();

        for (Pop pop : pops) {
            skill += pop.getDoubleProperty("skill-merchant");
        }

        if (pops.size() > 0) {
            skill = skill / pops.size();
        }

        trade = BASE_TRADE;

        if (pops.size() >= labour && labour != 0) {
            for (int i = 0; i < pops.size(); i += labour) {

                if (market.hasItems(inputs)) {
                    market.takeItems(inputs);

                    trade +=  2 + 2 * skill / 100;
                }
            }

        }

        trade *= 10;

        setDoubleProperty("atlast:trade", trade);


        for (Pop pop : pops) {
            final double wages = getWages();
            player.pay(wages);
            pop.getPaid(wages);
            pop.learn("merchant");
        }
    }


}
