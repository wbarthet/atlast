package org.atlast.world.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;

import org.onehippo.repository.scheduling.RepositoryJobExecutionContext;


/**
 * Created by wbarthet on 7/13/15.
 */
public class Barracks extends AtlastObject {


    private static final Double BASE_CONQUEST = 10.0;

    public Barracks(Node node) throws RepositoryException {
        super(node);
    }

    public List<Pop> getPops() {
        return getChildNodes(Pop.class, "atlast:pop");
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

    public void fight(QueryManager queryManager, RepositoryJobExecutionContext context) throws RepositoryException {

        List<Pop> pops = getPops();

        Player player = getPlayer();
        Market market = player.getStores();

        double skill = 0;

        double conquest;

        Recipe recipe = getRecipe();
        long labour = recipe.getLabour();
        Map<String, Double> inputs = recipe.getInputs();

        for (Pop pop : pops) {
            skill += pop.getDoubleProperty("skill-warrior");
        }

        if (pops.size() > 0) {
            skill = skill / pops.size();
        }

        conquest = BASE_CONQUEST;

        if (pops.size() >= labour && labour != 0) {
            for (int i = 0; i < pops.size(); i += labour) {

                if (market.hasItems(inputs)) {
                    market.takeItems(inputs);

                    conquest +=  2 + 2 * skill / 100;
                }
            }

        }

        Query query = queryManager.createQuery(context.getAttribute("playerdatalocation") + "//element(*, atlast:land)", Query.XPATH);

        NodeIterator queryResult = query.execute().getNodes();

        List<Land> lands = new ArrayList<>();

        while (queryResult.hasNext()) {
            Land land = new Land(queryResult.nextNode());

            lands.add(land);
        }

        conquest = conquest - lands.size();
        
        if (conquest > 0) {
            double currentConquest = getDoubleProperty("conquest-" + recipe.getName());

            conquest += currentConquest;

            setDoubleProperty("conquest-" + recipe.getName(), conquest);

            if (conquest > 100) {
                setDoubleProperty("conquest-" + recipe.getName(), 0.0d);

                player.addLand(recipe);
            }


        }

        for (Pop pop : pops) {
            final double wages = getWages();
            player.pay(wages);
            pop.getPaid(wages);
            pop.learn("warrior");
        }
    }


}
