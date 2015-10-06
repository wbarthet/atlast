package org.atlast.world.model;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;


/**
 * Created by wbarthet on 7/13/15.
 */
public class Library extends AtlastObject {


    private static final Double BASE_RESEARCH = 2.0;
    private static final long RESEARCH_LIMIT = 10;

    public Library(Node node) throws RepositoryException {
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


    public double getWages() {
        return getDoubleProperty("atlast:wages");
    }

    public void learn(final QueryManager queryManager) throws RepositoryException {

        List<Pop> pops = getPops();

        Player player = getPlayer();

        double skill = 0;

        double learning;

        for (Pop pop : pops) {
            skill += pop.getDoubleProperty("skill-scientist");
        }

        if (pops.size() > 0) {
            skill = skill / pops.size();
        }

        learning = BASE_RESEARCH + pops.size() * (BASE_RESEARCH + 2 * skill / 100);

        Query query = queryManager.createQuery(getNode().getPath().substring(1) + "//element(*, atlast:technology) order by @atlast:level " + getStringProperty("atlast:focus"), Query.XPATH);

        query.setLimit(getTechnology().size()/2);

        NodeIterator queryResult = query.execute().getNodes();

        List<Technology> technologies = new ArrayList<>();

        while (queryResult.hasNext()) {
            Technology technology = new Technology(queryResult.nextNode());

            technologies.add(technology);
        }

        learning = learning / technologies.size();

        for (Technology technology : technologies) {
            technology.improve(learning, queryManager);
        }

        for (Pop pop : pops) {
            final double wages = getWages();
            player.pay(wages);
            pop.getPaid(wages);
            pop.learn("scientist");
        }
    }
}
