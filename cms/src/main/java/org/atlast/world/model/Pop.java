package org.atlast.world.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.atlast.util.languages.NameGenerator;

/**
 * Created by wbarthet on 7/13/15.
 */
public class Pop extends AtlastObject {


    private static final int MAX_BOUGHT_STUFF = 10;
    private static final double GROW_RATE = 5;
    public static final int MAX_STUFF = 100;
    public static final int CONSUMPTION_RATE = 1;
    private static final double BASE_SKILL_LEVEL_INCREASE = 5.0;
    private static final double LEARNING_INERTIA = 1;
    private static final double BASE_FORGETFULLNESS = 1.0;

    public Pop(Node node) throws RepositoryException {
        super(node);
    }

    public double getCash() {
        return getDoubleProperty("atlast:cash");
    }

    public void getPaid(double wages) {
        Double cash = getDoubleProperty("atlast:cash");

        setDoubleProperty("atlast:cash", cash + wages);
    }

    public void pay(double amount) {
        Double cash = getDoubleProperty("atlast:cash");

        setDoubleProperty("atlast:cash", cash - amount);
    }

    public Player getPlayer() throws RepositoryException {
        return new Player(getNode().getParent().getParent());
    }


    public PopClasses getPopClass() {
        return PopClasses.valueOf(getStringProperty("atlast:class").toUpperCase());
    }


    public boolean consume(Market worldMarket, Market playerStores) throws RepositoryException {

        buy(worldMarket, playerStores, StoreTypes.FOOD);
        buy(worldMarket, playerStores, StoreTypes.GOODS);
        buy(worldMarket, playerStores, StoreTypes.LUXURIES);

        if (eat()) {
            switch (getPopClass()) {
                case MIDDLE:
                    double goods = getGoods();
                    goods -= CONSUMPTION_RATE;
                    goods = goods < 0 ? 0 : goods;
                    setDoubleProperty("atlast:goods", goods);
                case UPPER:
                    double luxuries = getLuxuries();
                    luxuries -= CONSUMPTION_RATE;
                    luxuries = luxuries < 0 ? 0 : luxuries;
                    setDoubleProperty("atlast:luxuries", luxuries);
                    break;
            }
        } else {
            return false;
        }

        return true;
    }

    private boolean eat() throws RepositoryException {
        double food = getFood();

        food -= CONSUMPTION_RATE;

        if (food > 50) {

            double feed = food - 50;

            feed = feed > GROW_RATE ? GROW_RATE : feed;

            Child child = getChild();

            food -= feed;

            child.feed(feed);

            setDoubleProperty("atlast:food", food);

        } else if (food < 0) {
            //die
            //TODO notification
            getNode().remove();
            return false;
        } else {
            setDoubleProperty("atlast:food", food);
        }
        return true;
    }

    private void buy(final Market worldMarket, final Market playerStores, StoreTypes store) throws RepositoryException {
        Double stuff = 0.0;

        switch (store) {
            case FOOD:
                stuff = getFood();
                break;
            case GOODS:
                stuff = getGoods();
                break;
            case LUXURIES:
                stuff = getLuxuries();
                break;
        }

        Store playerStore = playerStores.getStore(store.name().toLowerCase());
        Store market = worldMarket.getStore(store);

        List<String> items = playerStore.getItems();

        List<Double> inverseWeights = new ArrayList();
        List<Double> normalisedInverseWeights = new ArrayList();
        List<Double> cumulativeNormalisedInverseWeights = new ArrayList();
        List<Double> itemPrices = new ArrayList();

        double maxStuff = MAX_STUFF - stuff;

        maxStuff = maxStuff > MAX_BOUGHT_STUFF ? MAX_BOUGHT_STUFF : maxStuff;

        for (String item : items) {

            double price = market.getDoubleProperty(item + "-level");
            itemPrices.add(price);
        }

        double totalInverseWeight = 0.0;
        List<Trait> traits = getIdentity().getTraits();

        for (int i = 0; i < items.size(); i++) {

            double inverseWeight = 100 - itemPrices.get(i);

            totalInverseWeight += inverseWeight;


            String itemName =   items.get(i);

            for (Trait trait : traits) {
                for (Amount effect : trait.getEffects()) {
                    if (itemName.equals(effect.getResource().getName().toLowerCase())) {
                        inverseWeight = inverseWeight + (effect.getQuantity()/100)*inverseWeight;
                    }

                }

            }



            inverseWeights.add(inverseWeight);

        }

        for (int i = 0; i < items.size(); i++) {
            double normalisedInverseWeight = 100 / totalInverseWeight * inverseWeights.get(i);
            normalisedInverseWeights.add(normalisedInverseWeight);
        }

        double cumulativeWeighting = 0.0d;

        for (int i = 0; i < items.size(); i++) {
            cumulativeWeighting += normalisedInverseWeights.get(i);
            cumulativeNormalisedInverseWeights.add(cumulativeWeighting);
        }



        int boughtStuff = 0;
        boolean broke = false;

        while (boughtStuff < maxStuff && !broke) {

            Random r = new Random();

            int random = r.nextInt((int) Math.round(cumulativeWeighting));

            boolean boughtSomething = false;
            int itemIndex = 0;

            for (int i = 0; i < items.size(); i++) {

                if (random > cumulativeNormalisedInverseWeights.get(i)) {
                    itemIndex = i;
                } else {
                    break;
                }

            }


            double cash = getCash();


            if (playerStore.hasItem(items.get(itemIndex), 1.0) && cash >= itemPrices.get(itemIndex)) {

                playerStore.takeItem(items.get(itemIndex), 1.0);

                pay(itemPrices.get(itemIndex));

                getPlayer().getPaid(itemPrices.get(itemIndex));

                stuff++;

                boughtSomething = true;
            }


            broke = !boughtSomething;

        }

        switch (store) {
            case FOOD:
                setDoubleProperty("atlast:food", stuff);
                break;
            case GOODS:
                setDoubleProperty("atlast:goods", stuff);
                break;
            case LUXURIES:
                setDoubleProperty("atlast:luxuries", stuff);
                break;
        }
    }

    public Double getFood() {
        return getDoubleProperty("atlast:food");
    }

    public Double getGoods() {
        return getDoubleProperty("atlast:goods");
    }

    public Double getLuxuries() {
        return getDoubleProperty("atlast:luxuries");
    }

    public Child getChild() throws RepositoryException {


        Node child = null;
        if (!getNode().hasNode("child")) {

            child = getNode().addNode("child", "atlast:child");

            child.setProperty("atlast:player", getStringProperty("atlast:player"));

            child.setProperty("atlast:food", 0.0d);
            child.setProperty("atlast:cash", 0.0d);

            String identity = determineIdentity();
            Node identityNode = getNode().getSession().getNodeByIdentifier(identity);

            child.setProperty("atlast:identity", identity);

            Node languageDescriptorNode = getNode().getSession().getNodeByIdentifier(identityNode.getProperty("atlast:languagedescriptor").getString());

            child.setProperty("atlast:name", NameGenerator.generatePopName(languageDescriptorNode, getName()));
        } else {
            child = getNode().getNode("child");
        }


        return new Child(child);
    }

    public String getName() {
        return getStringProperty("atlast:name");
    }

    public void learn(final String skill) throws RepositoryException {

        List<String> skills = getStringListProperty("atlast:skills");

        if (skills.contains(skill)) {
            double skillLevel = getDoubleProperty("skill-" + skill);
            double skillIncrease = BASE_SKILL_LEVEL_INCREASE;

            double percentageDampener = 0.0;
            if (skillLevel >= 50) {
                percentageDampener = 100 - skillLevel;
            } else if (skillLevel < 50) {
                percentageDampener = skillLevel;
            }

            skillIncrease = (skillIncrease * percentageDampener / 100) / LEARNING_INERTIA;

            setDoubleProperty("skill-" + skill, skillLevel + skillIncrease);

        } else {
            skills.add(skill);

            setStringListProperty("atlast:skills", skills);

            setDoubleProperty("skill-" + skill, BASE_SKILL_LEVEL_INCREASE);
        }


    }


    public void forget() {
        List<String> skills = getStringListProperty("atlast:skills");

        for (String skill : skills) {
            double skillLevel = getDoubleProperty("skill-" + skill);

            double skillDecrease = BASE_FORGETFULLNESS;

            double percentageDampener = 0.0;
            if (skillLevel >= 50) {
                percentageDampener = 100 - skillLevel;
            } else if (skillLevel < 50) {
                percentageDampener = skillLevel;
            }

            skillDecrease = (skillDecrease * percentageDampener / 100) / LEARNING_INERTIA;

            setDoubleProperty("skill-" + skill, skillLevel - skillDecrease);
        }
    }

    public String determineIdentity() {

        double biggestRaceLevel = 0;
        String biggestRace = "none";

        for (String race : getStringListProperty("atlast:races")) {
            if (!"none".equals(race)) {
                double raceLevel = getDoubleProperty("race-" + race);
                biggestRace = raceLevel > biggestRaceLevel ? race : biggestRace;
                biggestRaceLevel = raceLevel > biggestRaceLevel ? raceLevel : biggestRaceLevel;
            }
        }

        double biggestReligionLevel = 0;
        String biggestReligion = "none";

        for (String religion : getStringListProperty("atlast:religions")) {
            if (!"none".equals(religion)) {
                double religionLevel = getDoubleProperty("religion-" + religion);
                biggestReligion = religionLevel > biggestReligionLevel ? religion : biggestReligion;
                biggestReligionLevel = religionLevel > biggestReligionLevel ? religionLevel : biggestReligionLevel;
            }
        }

        String identity = biggestRaceLevel > biggestReligionLevel ? biggestRace : biggestReligion;

        return identity;
    }

    public Identity getIdentity() throws RepositoryException {

        String identityUuid = getStringProperty("atlast:identity");
        if (identityUuid == null) {
            identityUuid = determineIdentity();
            setStringProperty("atlast:identity", identityUuid);
        }

        return  new Identity(getNode().getSession().getNodeByIdentifier(identityUuid));
    }
}
