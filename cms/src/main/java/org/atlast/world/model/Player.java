package org.atlast.world.model;

import java.util.List;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;

/**
 * Created by wbarthet on 7/13/15.
 */
public class Player extends AtlastObject {

    public Player(Node node) throws RepositoryException {
        super(node);
    }

    public Market getStores() {
        try {
            return new Market(getNode().getNode("market"));
        } catch (RepositoryException e) {
            return null;
        }
    }

    public void pay(double amount) {
        Double cash = getDoubleProperty("atlast:cash");

        setDoubleProperty("atlast:cash", cash - amount);
    }

    public void getPaid(double amount) {
        Double cash = getDoubleProperty("atlast:cash");

        setDoubleProperty("atlast:cash", cash + amount);
    }

    public boolean hasStem() throws RepositoryException {
        return getNode().hasNode("stem");
    }

    public Child getStem() throws RepositoryException {

        if (hasStem()) {
            return new Child(getNode().getNode("stem"));
        }

        return null;
    }

    public Identity getIdentity() throws RepositoryException {
        return new Identity(getNode().getNode("identity"));
    }
}
