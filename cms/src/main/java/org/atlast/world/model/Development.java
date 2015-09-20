package org.atlast.world.model;

import java.util.List;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

/**
 * Created by wbarthet on 7/13/15.
 */
public class Development extends AtlastObject {


    public Development(Node node) throws RepositoryException {
        super(node);
    }

    public List<Pop> getPops() {
        return getChildNodes(Pop.class);
    }

    public long getLabour() {
        return getLongProperty("atlast:labour");
    }

    public String getSkill() {
        return getStringProperty("atlast:skill");
    }

    public List<String> getInputs() {
        return getStringListProperty("atlast:inputs");
    }

    public List<String> getOutputs() {
        return getStringListProperty("atlast:outputs");
    }

    public double getWages() {
        return getDoubleProperty("atlast:wages");
    }


    public Player getPlayer() {
        try {
            return new Player(getNode().getParent());
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
        return null;
    }



    public void produce() throws RepositoryException {
        List<Pop> pops = getPops();

        final long labour = getLabour();
        String skill = getSkill();
        Player player = getPlayer();
        Market market = player.getStores();
        if (pops.size() >= labour) {
            for (int i = 0; i < labour; i += labour) {
                if (market.hasItems(getInputs())) {
                    market.takeItems(getInputs());
                    market.addItems(getOutputs());
                }
            }
            for (Pop pop : pops) {
                final double wages = getWages();
                player.pay(wages);
                pop.getPaid(wages);
                pop.learn(skill);
            }
        }
    }
}
