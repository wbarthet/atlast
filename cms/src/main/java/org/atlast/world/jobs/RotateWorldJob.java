package org.atlast.world.jobs;

import java.util.List;

import javax.jcr.Credentials;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;

import org.atlast.world.model.Development;
import org.atlast.world.model.Market;
import org.atlast.world.model.Player;
import org.atlast.world.model.Pop;
import org.onehippo.repository.scheduling.RepositoryJob;
import org.onehippo.repository.scheduling.RepositoryJobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by wbarthet on 7/13/15.
 */
public class RotateWorldJob implements RepositoryJob {

    private static Logger log = LoggerFactory.getLogger(RotateWorldJob.class);

    @Override
    public void execute(final RepositoryJobExecutionContext context) {

        log.info("Rotating the world");

        Session session = null;
        try {
            session = context.createSession(new SimpleCredentials(context.getAttribute("user"), context.getAttribute("password").toCharArray()));


            Market worldMarket = new Market(session.getNode("/" + context.getAttribute("worlddatalocation")).getNode("market"));

            QueryManager queryManager = session.getWorkspace().getQueryManager();

            sell(context, worldMarket, queryManager);

            buy(context, worldMarket, queryManager);


            produce(context, queryManager);


            consume(context, worldMarket, queryManager);

            worldMarket.rectifyPrices();


            //TODO: leave

            session.save();

        } catch (RepositoryException e) {
            e.printStackTrace();
        } finally {

            session.logout();
        }

    }

    private void sell(final RepositoryJobExecutionContext context, final Market worldMarket, final QueryManager queryManager) throws RepositoryException {
        Query query = queryManager.createQuery(context.getAttribute("playerdatalocation") + "//element(*, atlast:market)", Query.XPATH);
        //TODO: batch processing
        NodeIterator queryResult = query.execute().getNodes();

        while (queryResult.hasNext()) {
            Market market = new Market(queryResult.nextNode());

            market.sell(worldMarket);

        }
    }

    private void buy(final RepositoryJobExecutionContext context, final Market worldMarket, final QueryManager queryManager) throws RepositoryException {
        Query query = queryManager.createQuery(context.getAttribute("playerdatalocation") + "//element(*, atlast:market)", Query.XPATH);
        //TODO: batch processing
        NodeIterator queryResult = query.execute().getNodes();

        while (queryResult.hasNext()) {
            Market market = new Market(queryResult.nextNode());

            market.buy(worldMarket);

        }
    }

    private void consume(final RepositoryJobExecutionContext context, Market worldMarket, final QueryManager queryManager) throws RepositoryException {
        Query query = queryManager.createQuery(context.getAttribute("playerdatalocation") + "//element(*, atlast:pop)", Query.XPATH);
        //TODO: batch processing
        NodeIterator queryResult = query.execute().getNodes();

        while (queryResult.hasNext()) {
            Pop pop = new Pop(queryResult.nextNode());

            pop.consume(worldMarket, pop.getPlayer().getStores());

        }
    }

    private void produce(final RepositoryJobExecutionContext context, final QueryManager queryManager) throws RepositoryException {
        Query query = queryManager.createQuery(context.getAttribute("playerdatalocation") + "//element(*, atlast:development)", Query.XPATH);
        //TODO: batch processing
        NodeIterator queryResult = query.execute().getNodes();

        while (queryResult.hasNext()) {
            Development development = new Development(queryResult.nextNode());

            development.produce();
        }
    }

}