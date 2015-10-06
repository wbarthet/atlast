package org.atlast.world.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

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

        Pop parent = new Pop(getNode().getParent());


        if (food >= MATURITY_FOOD_LEVEL) {

            Player player = getParent().getPlayer();
            if (player.hasStem()) {

                Child stem = new Child(player.getStem().getNode());

                double stemCash = stem.getDoubleProperty("atlast:cash");
                double stemGoods = stem.getDoubleProperty("atlast:goods");
                double stemLuxuries = stem.getDoubleProperty("atlast:luxuries");


                setDoubleProperty("atlast:goods", goods + stemGoods);
                setDoubleProperty("atlast:luxuries", goods + stemLuxuries);
                getPaid(cash + stemCash);


                setAttributeMap(parent, stem, "skill");
                setAttributeMap(parent, stem, "race");
                setAttributeMap(parent, stem, "religion");

                PopClasses popClass = luxuries > 50 ? PopClasses.UPPER : goods > 50 ? PopClasses.MIDDLE : PopClasses.WORKING;

                setStringProperty("atlast:class", popClass.name());

                getNode().setPrimaryType("atlast:pop");

                getNode().setProperty("hippo:availability", new String[]{"live"});
                getNode().setProperty("atlast:player", "global");

                stem.getNode().remove();
                getNode().getSession().move(getNode().getPath(), "/content/documents/atlastserver/worlddata/pool/" + NodeNameCodec.encode(getStringProperty("atlast:name")));

                player.setDoubleProperty("atlast:food", 10.0d);
            } else {

                setAttributeMap(parent, "skill");
                setAttributeMap(parent, "race");
                setAttributeMap(parent, "religion");


                setDoubleProperty("atlast:goods", goods);
                setDoubleProperty("atlast:luxuries", luxuries);
                setStringProperty("atlast:player", player.getStringProperty("atlast:name"));
                getPaid(cash);
                PopClasses popClass = luxuries > 50 ? PopClasses.UPPER : goods > 50 ? PopClasses.MIDDLE : PopClasses.WORKING;

                setStringProperty("atlast:class", popClass.name());


                getNode().getSession().move(getNode().getPath(), player.getNode().getPath() + "/stem");

                player.setDoubleProperty("atlast:food", 10.0d);
            }
        } else {
            setDoubleProperty("atlast:food", food);
        }
    }

    private Set<String> setAttributeMap(final Pop parent, String attributeName) throws RepositoryException {
        Set<String> attributes = new HashSet<>();

        for (String attribute : parent.getStringListProperty("atlast:" + attributeName + "s")) {

            attributes.add(attribute);


            Double attributelevel = parent.getDoubleProperty(attributeName + "-" + attribute) / 2;

            setDoubleProperty(attributeName + "-" + attribute, attributelevel);

        }

        setStringListProperty("atlast:" + attributeName + "s", new ArrayList<>(attributes));

        return attributes;
    }

    private Set<String> setAttributeMap(final Pop parent, final Child stem, final String attributeName) throws RepositoryException {
        Set<String> attribues = new HashSet<>();

        for (String attribute : parent.getStringListProperty("atlast:" + attributeName + "s")) {

            attribues.add(attribute);

            Double stemAttributeLevel = stem.getDoubleProperty(attributeName + "-" + attribute);

            Double attributeLevel = parent.getDoubleProperty(attributeName + "-" + attribute) / 2;

            setDoubleProperty(attributeName + "-" + attribute, attributeLevel + stemAttributeLevel);

        }

        for (String attribute : stem.getStringListProperty("atlast:" + attributeName + "s")) {

            if (!attribues.contains(attribute)) {
                attribues.add(attribute);


                Double stemAttributeLevel = stem.getDoubleProperty(attributeName + "-" + attribute);

                Double skillLevel = getDoubleProperty(attributeName + "-" + attribute);

                setDoubleProperty(attributeName + "-" + attribute, skillLevel + stemAttributeLevel);
            }

        }

        setStringListProperty("atlast:" + attributeName + "s", new ArrayList<>(attribues));
        return attribues;
    }


    public Pop getParent() throws RepositoryException {
        return new Pop(getNode().getParent());
    }
}
