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
public class Construction extends AtlastObject {


    private static final Double BASE_CONSTRUCTION = 5.0;

    public Construction(Node node) throws RepositoryException {
        super(node);
    }

    public List<Pop> getPops() {
        return getChildNodes(Pop.class, "atlast:pop");
    }

    public Recipe getRecipe() throws RepositoryException {
        return new Recipe(getNode().getSession(), getStringProperty("atlast:recipedescriptor"));
    }

    public Player getPlayer() {
        try {
            return new Player(getNode().getParent());
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
        return null;
    }


    public double getWages() {
        return getDoubleProperty("atlast:wages");
    }

    public void build(QueryManager queryManager, RepositoryJobExecutionContext context) throws RepositoryException {

        List<Pop> pops = getPops();

        Player player = getPlayer();
        Market market = player.getStores();

        double skill = 0;

        double construction;

        Recipe recipe = getRecipe();
        long labour = recipe.getLabour();
        Map<String, Double> inputs = recipe.getInputs();

        for (Pop pop : pops) {
            skill += pop.getDoubleProperty("skill-builder");
        }

        if (pops.size() > 0) {
            skill = skill / pops.size();
        }

        construction = BASE_CONSTRUCTION;

        if (pops.size() >= labour && labour != 0) {
            for (int i = 0; i < pops.size(); i += labour) {

                if (market.hasItems(inputs)) {
                    market.takeItems(inputs);

                    construction +=  2 + 2 * skill / 100;
                }
            }

        }
;

        Query query = queryManager.createQuery(context.getAttribute("playerdatalocation") + "//element(*, atlast:land)[(@atlast:developmentlevel < 5)]", Query.XPATH);

        NodeIterator queryResult = query.execute().getNodes();

        List<Land> lands = new ArrayList<>();

        while (queryResult.hasNext()) {
            Land land = new Land(queryResult.nextNode());

            lands.add(land);
        }

        construction = construction / lands.size();

        for (Land land : lands) {
            land.build(construction);
        }


        for (Pop pop : pops) {
            final double wages = getWages();
            player.pay(wages);
            pop.getPaid(wages);
            pop.learn("builder");
        }
    }


}
