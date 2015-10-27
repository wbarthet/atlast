package org.atlast.world.model;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;

import org.onehippo.repository.scheduling.RepositoryJobExecutionContext;


/**
 * Created by wbarthet on 7/13/15.
 */
public class Land extends AtlastObject {



    public Land(Node node) throws RepositoryException {
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


    public double getWages() {
        return getDoubleProperty("atlast:wages");
    }


    public void build(final double construction) {

        double progress = getDoubleProperty("atlast:developmentprogress");
        long level = getLongProperty("atlast:developmentlevel");

        progress += construction;

        if (progress > 100 * (level+1)) {
            progress = 0;
            level++;

            setLongProperty("atlast:developmentlevel", level);
        }

        setDoubleProperty("atlast:developmentprogress", progress);

    }
}
