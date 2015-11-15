package org.atlast.world.model;

import java.util.List;
import java.util.Random;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;

import org.atlast.util.languages.NameGenerator;
import org.hippoecm.repository.api.NodeNameCodec;

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

    public Library getLibrary() throws RepositoryException {
        return new Library(getNode().getNode("library"));
    }

    public void addLand(final Recipe recipe) throws RepositoryException {

        LandDescriptor landDescriptor = null;

        List<Allocation> landQuality = recipe.getLandQuality();

        Random r = new Random();

        for (int i = 0; i < landQuality.size() - 1; i++) {
            Allocation allocation = landQuality.get(i);
            double amount = allocation.getAmount();

            int chance = r.nextInt((int) Math.round(amount));

            if (chance < amount) {
                landDescriptor = allocation.getLand();
            }
        }

        if (landDescriptor == null) {
            landDescriptor = landQuality.get(landQuality.size() - 1).getLand();
        }

        String name = NameGenerator.generateLandName(getIdentity().getLanguageDescriptorNode(), landDescriptor.getNamePatterns());
        Node landNode = getNode().addNode(NodeNameCodec.encode(name) + "-" + landDescriptor.getName().toLowerCase(), "atlast:land");

        landNode.setProperty("atlast:player", getStringProperty("atlast:player"));
        landNode.setProperty("atlast:name", name);
        landNode.setProperty("atlast:landdescriptor", landDescriptor.node.getIdentifier());

    }
}
