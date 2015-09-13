package org.atlast.world.model;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.ValueFormatException;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.version.VersionException;

import org.hippoecm.repository.api.NodeNameCodec;

/**
 * Created by wbarthet on 7/13/15.
 */
public class Child extends Pop {


    private static final double MATURITY_FOOD_LEVEL = 100;

    public Child(final Node node) throws RepositoryException {
        super(node);
    }

    public void feed(final double feed) throws RepositoryException {

        double food = getDoubleProperty("atlast:food");

        food += feed;

        double cash = getParent().getCash();

        cash = cash / 2;

        double goods = getParent().getGoods();

        goods = goods / 2;

        double luxuries = getParent().getLuxuries();

        luxuries = luxuries / 2;

        getParent().pay(cash);


        if (food >= MATURITY_FOOD_LEVEL) {

            Player player = getParent().getPlayer();
            if (player.hasStem()) {

                Child stem = new Child(player.getStem().getNode());

                double stemCash = stem.getDoubleProperty("atlast:cash");
                double stemGoods = stem.getDoubleProperty("atlast:goods");
                double stemLuxuries = stem.getDoubleProperty("atlast:luxuries");

                stem.getNode().remove();

                setDoubleProperty("atlast:goods", goods + stemGoods);
                setDoubleProperty("atlast:luxuries", goods + stemLuxuries);
                getPaid(cash + stemCash);

                PopClasses popClass = luxuries > 50 ? PopClasses.UPPER : goods > 50 ? PopClasses.MIDDLE : PopClasses.WORKING;

                setStringProperty("atlast:class", popClass.name());

                getNode().setPrimaryType("atlast:pop");

                getNode().setProperty("hippo:availability", new String[]{"live"});

                getNode().getSession().move(getNode().getPath(), "/content/documents/atlastserver/worlddata/pool/" + NodeNameCodec.encode(getStringProperty("atlast:name")));

                player.setDoubleProperty("atlast:food", 0.0d);
            } else {

                setDoubleProperty("atlast:goods", goods);
                setDoubleProperty("atlast:luxuries", luxuries);
                setStringProperty("atlast:player", player.getStringProperty("atlast:name"));
                getPaid(cash);
                PopClasses popClass = luxuries > 50 ? PopClasses.UPPER : goods > 50 ? PopClasses.MIDDLE : PopClasses.WORKING;

                setStringProperty("atlast:class", popClass.name());


                getNode().getSession().move(getNode().getPath(), player.getNode().getPath() + "/stem");

                player.setDoubleProperty("atlast:food", 0.0d);
            }
        } else {
            setDoubleProperty("atlast:food", food);
        }
    }


    public Pop getParent() throws RepositoryException {
        return new Pop(getNode().getParent());
    }
}
