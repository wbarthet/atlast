package org.atlast.world.model;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;


/**
 * Created by wbarthet on 7/13/15.
 */
public class Technology extends AtlastObject {


    public Technology(Node node) throws RepositoryException {
        super(node);
    }

    public Library getLibrary() throws RepositoryException {
        return new Library(getNode().getParent());
    }


    public void improve(final double learning, final QueryManager queryManager) throws RepositoryException {

        double level = getDoubleProperty("atlast:level");
        double progress = getDoubleProperty("atlast:progress");

        progress += learning;

        List<Technology> technologies = getLibrary().getTechnology();

        double threshold = Math.pow(2, level) * 10 + technologies.size() * 10;

        if (progress >= threshold) {
            progress = 0;

            if (level == 0) {

                Set<String> knownRecipes = new HashSet<>();

                for (Technology technology : technologies) {
                    knownRecipes.add(technology.getName());
                }

                Query query = queryManager.createQuery("content/documents//element(*, atlast:recipedescriptor)", Query.XPATH);

                NodeIterator queryResult = query.execute().getNodes();

                while (queryResult.hasNext()) {

                    String uuid = queryResult.nextNode().getParent().getIdentifier();

                    if (!knownRecipes.contains(uuid)) {

                        Node newTechNode = getLibrary().getNode().addNode(uuid, "atlast:technology");

                        newTechNode.setProperty("atlast:level", 1.0d);
                        newTechNode.setProperty("atlast:progress", 1.0d);
                        break;
                    }
                }


            } else {
                level++;

                setDoubleProperty("atlast:level", level);
            }

        }

        setDoubleProperty("atlast:progress", progress);

    }

    public String getName() throws RepositoryException {
        return getNode().getName();
    }
}
