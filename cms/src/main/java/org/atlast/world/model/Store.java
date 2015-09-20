package org.atlast.world.model;

import java.util.List;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

/**
 * Created by wbarthet on 7/13/15.
 */
public class Store extends AtlastObject {


    private static final double MARKET_INERTIA = 10;

    public Store(Node node) throws RepositoryException {
        super(node);
    }


    public boolean hasItem(final String itemName, final Double qty) {
        return getDoubleProperty(itemName) >= qty;
    }


    public void addItem(final String itemName, final Double qty) {
        Double inStore = getDoubleProperty(itemName);
        setDoubleProperty(itemName, inStore + qty);
    }

    public void takeItem(final String itemName, final Double qty) {
        Double inStore = getDoubleProperty(itemName);
        setDoubleProperty(itemName, inStore - qty);
    }


    public void increaseItemLevel(final String itemName, final Double amount) {
        Double inStore = getDoubleProperty(itemName);
        setDoubleProperty(itemName, inStore + amount);
    }

    public void decreaseItemLevel(final String itemName, final Double amount) {
        Double inStore = getDoubleProperty(itemName + "-level");
        setDoubleProperty(itemName + "-level", inStore - amount);
    }

    public List<String> getItems() {
        return getStringListProperty("atlast:items");
    }

    public void buy(Market worldMarket) throws RepositoryException {

        List<String> itemsList = getStringListProperty("atlast:items");

        Player player = getPlayer();

        for (String item : itemsList) {

            double qty = getDoubleProperty(item);
            double level = getDoubleProperty(item + "-level");

            if (level > qty) {
                double price = worldMarket.getStore(getNode().getName()).getDoubleProperty(item + "-level");

                double amount = level - qty;

                player.pay(price * amount);

                setDoubleProperty(item, level);

                worldMarket.getStore(getNode().getName()).addItem(item, amount);
            }

        }

    }

    public void sell(Market worldMarket) throws RepositoryException {

        List<String> itemsList = getStringListProperty("atlast:items");

        Player player = getPlayer();

        for (String item : itemsList) {

            double qty = getDoubleProperty(item);
            double level = getDoubleProperty(item + "-level");

            if (level < qty) {
                double price = worldMarket.getStore(getNode().getName()).getDoubleProperty(item + "-level");

                double amount = qty - level;

                player.getPaid(price * amount);

                setDoubleProperty(item, level);

                worldMarket.getStore(getNode().getName()).takeItem(item, amount);
            }

        }

    }


    public Player getPlayer() {
        try {
            return new Player(getNode().getParent().getParent());
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void rectify() {

        List<String> itemsList = getStringListProperty("atlast:items");


        for (String item : itemsList) {

            double level = getDoubleProperty(item + "-level");
            double qty = getDoubleProperty(item);

            double percentageDampener = 0.0;
            if (level >= 50) {
                percentageDampener = 100 - level;


            } else if (level < 50) {

                percentageDampener = level;


            }

            qty = (qty * percentageDampener / 100) / MARKET_INERTIA;

            setDoubleProperty(item, 0);


            setDoubleProperty(item + "-level", level + qty);


        }


    }
}
