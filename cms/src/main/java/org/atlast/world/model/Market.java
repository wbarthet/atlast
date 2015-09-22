package org.atlast.world.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

/**
 * Created by wbarthet on 7/13/15.
 */
public class Market extends AtlastObject {


    public Market(Node node) throws RepositoryException {
        super(node);
    }

    public boolean hasItems(final Map<String, Double> itemsMap) throws RepositoryException {
        boolean hasItems = true;
        for (String item : itemsMap.keySet()) {
            if (item.contains("-")) {
                String storeName = item.split("-")[0];
                String itemName = item.split("-")[1];
                Double qty = itemsMap.get(item);

                Store store = getStore(storeName);
                if (store != null) {
                    hasItems &= store.hasItem(itemName, qty);
                }
            }
        }

        return hasItems;
    }

    public Store getStore(String name) throws RepositoryException {

        if (getNode().hasNode(name)) {
            return new Store(getNode().getNode(name));
        }


        return null;
    }

    public Store getStore(StoreTypes storeType) throws RepositoryException {
        String name = storeType.name().toLowerCase();
        if (getNode().hasNode(name)) {
            return new Store(getNode().getNode(name));
        }


        return null;
    }

    public List<Store> getStores() {

        return getChildNodes(Store.class);
    }

    public void takeItems(final Map<String, Double> itemsMap) throws RepositoryException {
        for (String item : itemsMap.keySet()) {
            if (item.contains("-")) {
                String storeName = item.split("-")[0];
                String itemName = item.split("-")[1];
                Double qty = itemsMap.get(item);

                Store store = getStore(storeName);
                if (store != null) {
                    store.takeItem(itemName, qty);
                }
            }
        }
    }

    public void addItems(final Map<String, Double> itemsMap) throws RepositoryException {
        for (String item : itemsMap.keySet()) {
            if (item.contains("-")) {
                String storeName = item.split("-")[0];
                String itemName = item.split("-")[1];
                Double qty = itemsMap.get(item);

                Store store = getStore(storeName);
                if (store != null) {
                    store.addItem(itemName, qty);
                }
            }
        }
    }

    public void buy(Market worldMarket) throws RepositoryException {
        for (Store store : getStores()) {
            store.buy(worldMarket);
        }
    }

    public void sell(Market worldMarket) throws RepositoryException {
        for (Store store : getStores()) {
            store.sell(worldMarket);
        }
    }

    public void rectifyPrices() {

        for (Store store : getStores()) {
            store.rectify();
        }

    }
}
