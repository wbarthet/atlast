package org.atlast.services;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;


import org.atlast.beans.ContentDocument;
import org.atlast.beans.Message;
import org.atlast.beans.Player;
import org.atlast.beans.descriptors.Amount;
import org.atlast.beans.descriptors.DevelopmentDescriptor;
import org.atlast.beans.descriptors.LandDescriptor;
import org.atlast.beans.descriptors.LanguageDescriptor;
import org.atlast.beans.descriptors.RecipeDescriptor;
import org.atlast.beans.descriptors.ResourceDescriptor;
import org.atlast.beans.descriptors.TraitDescriptor;
import org.atlast.util.languages.ColourGenerator;
import org.atlast.util.languages.NameGenerator;
import org.hippoecm.hst.content.beans.ObjectBeanManagerException;
import org.hippoecm.hst.content.beans.query.HstQuery;
import org.hippoecm.hst.content.beans.query.HstQueryResult;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.query.filter.Filter;
import org.hippoecm.hst.content.beans.standard.HippoBeanIterator;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.site.HstServices;
import org.hippoecm.repository.PasswordHelper;
import org.hippoecm.repository.api.NodeNameCodec;

/**
 * Created by wbarthet on 7/20/15.
 */
public class SignupService {


    private static final double STARTING_CASH = 0;
    private static final int ROUGH_START_COUNT = 3;
    private static final int EASY_START_COUNT = 7;
    private static final int WORKING_START_COUNT = 6;
    private static final int MIDDLE_START_COUNT = 3;
    private static final int UPPER_START_COUNT = 1;
    public static final double POP_START_CASH = 1000.0;
    public static final double POP_START_FOOD = 100.0;
    public static final double START_TECH_LEVEL = 5.0d;
    public static final String[] TRADE_RECIPES = new String[]{"wagon-trains", "caravans", "trade-ships"};
    public static final String[] CONSTRUCTION_RECIPES = new String[]{"wooden-buildings", "brick-buildings"};

    public String signup(HstRequestContext requestContext, String userName, String password) throws RepositoryException, QueryException, ObjectBeanManagerException {
        Session session = requestContext.getSession();


        if (session.nodeExists("/hippo:configuration/hippo:users/" + userName)) {
            return "User name already exists, please choose a different user name.";
        } else {
            Node userNode = session.getNode("/hippo:configuration/hippo:users").addNode(userName, "hipposys:user");

            userNode.setProperty("hipposys:active", true);
            userNode.setProperty("hipposys:securityprovider", "internal");
            userNode.setProperty("hipposys:password", createPasswordHash(password));

            Node playersGroupNode = session.getNode("/hippo:configuration/hippo:groups/players");

            addValue(playersGroupNode, "hipposys:members", userName);


            Node playerdataFolderNode = session.getNode("/content/documents/atlastserver/playerdata");

            Node userPlayerDataNode = createPlayerData(playerdataFolderNode, userName, requestContext);

            createPlayerTech(userPlayerDataNode, userName);

            createPlayerStores(userPlayerDataNode, requestContext, userName);

            createPlayerStorehouse(userPlayerDataNode, userName, session);

            createPlayerConstruction(userPlayerDataNode, userName, session);

            Node identityNode = userPlayerDataNode.getNode("identity");

            String race = identityNode.getProperty("atlast:race").getBoolean() ? identityNode.getIdentifier() : "none";
            String religion = !identityNode.getProperty("atlast:race").getBoolean() ? identityNode.getIdentifier() : "none";

            //create lands
            createPlayerLands(requestContext, userPlayerDataNode, userName, race, religion);


            ContentDocument welcomeMessage = (ContentDocument) requestContext.getObjectBeanManager().getObject("/content/documents/atlastserver/info/game-texts/welcome-message");

            Player player = (Player) requestContext.getObjectBeanManager().getObjectByUuid(userPlayerDataNode.getIdentifier());

            createPlayerInbox(userPlayerDataNode, userName, requestContext);

            MessageService messageService = HstServices.getComponentManager().getComponent(MessageService.class);

            messageService.sendMessage(requestContext, welcomeMessage, player);

            //create notification

            session.save();
        }


        return "User created";
    }

    private void createPlayerInbox(final Node userPlayerDataNode, final String userName, HstRequestContext requestContext) throws RepositoryException, ObjectBeanManagerException {
        Node inboxNode = userPlayerDataNode.addNode("inbox", "atlast:inbox");

        inboxNode.setProperty("atlast:player", userName);
    }

    private void createPlayerTech(final Node userPlayerDataNode, final String userName) throws RepositoryException {
        Node libraryNode = userPlayerDataNode.addNode("library", "atlast:library");

        libraryNode.setProperty("atlast:player", userName);
        libraryNode.setProperty("atlast:focus", "ascending");

        Node innovationNode = libraryNode.addNode("innovation", "atlast:technology");

        innovationNode.setProperty("atlast:progress", 0.0d);
        innovationNode.setProperty("atlast:level", 0.0d);
    }

    private void createPlayerStorehouse(final Node userPlayerDataNode, final String userName, Session session) throws RepositoryException {
        Node storehouseNode = userPlayerDataNode.addNode("storehouse", "atlast:storehouse");

        storehouseNode.setProperty("atlast:player", userName);

        Random r = new Random();

        int index = r.nextInt(3);

        Node recipeNode = session.getNode("/content/documents/atlastserver/descriptors/recipes/services/" + TRADE_RECIPES[index]);

        storehouseNode.setProperty("atlast:recipedescriptor", recipeNode.getIdentifier());

        storehouseNode.setProperty("atlast:wages", 200.0d);
        storehouseNode.setProperty("atlast:trade", 20.0d);

    }

    private void createPlayerConstruction(final Node userPlayerDataNode, final String userName, Session session) throws RepositoryException {
        Node constructionNode = userPlayerDataNode.addNode("construction", "atlast:construction");

        constructionNode.setProperty("atlast:player", userName);

        Random r = new Random();

        int index = r.nextInt(2);

        Node recipeNode = session.getNode("/content/documents/atlastserver/descriptors/recipes/services/" + CONSTRUCTION_RECIPES[index]);

        constructionNode.setProperty("atlast:recipedescriptor", recipeNode.getIdentifier());

        constructionNode.setProperty("atlast:wages", 200.0d);

    }

    private Node createPlayerStores(final Node userPlayerDataNode, final HstRequestContext requestContext, final String userName) throws RepositoryException, QueryException {

        Node marketNode = userPlayerDataNode.addNode("market", "atlast:market");

        marketNode.setProperty("atlast:player", userName);

        HstQuery query = requestContext.getQueryManager().createQuery(requestContext.getSession().getRootNode(), ResourceDescriptor.class);

        HstQueryResult result = query.execute();

        HippoBeanIterator beanIterator = result.getHippoBeans();

        while (beanIterator.hasNext()) {

            ResourceDescriptor resourceDescriptor = (ResourceDescriptor) beanIterator.nextHippoBean();

            String storeName = resourceDescriptor.getParentBean().getName();

            if (!"services".equals(storeName)) {

                Node storeNode = null;

                if (marketNode.hasNode(storeName)) {
                    storeNode = marketNode.getNode(storeName);
                } else {
                    storeNode = marketNode.addNode(storeName, "atlast:store");
                    storeNode.setProperty("atlast:player", userName);
                    storeNode.setProperty("atlast:name", storeName);
                    storeNode.setProperty("atlast:items", new String[]{});
                }

                String resourceName = resourceDescriptor.getName().toLowerCase();
                storeNode.setProperty(resourceName, 10.0d);
                storeNode.setProperty(resourceName + "-level", 10.0d);


                addValue(storeNode, "atlast:items", resourceName);
            }
        }

        return marketNode;
    }

    private void addValue(final Node node, final String propertyName, final String valueToAdd) throws RepositoryException {

        Value[] valueArray = node.getProperty(propertyName).getValues();

        List<String> values = new ArrayList<>();
        for (Value value : valueArray) {
            values.add(value.getString());
        }
        values.add(valueToAdd);

        node.setProperty(propertyName, values.toArray(new String[]{}));

    }

    private Node createPlayerData(Node playerdataFolderNode, String userName, final HstRequestContext requestContext) throws RepositoryException, QueryException {
        Node playerdataNode = playerdataFolderNode.addNode(userName, "atlast:player");

        playerdataNode.setProperty("atlast:player", userName);
        playerdataNode.setProperty("atlast:cash", STARTING_CASH);

        Node languageNode = pickLanguage(requestContext);


        Node identityNode = playerdataNode.addNode("identity", "atlast:identity");

        boolean isRace = languageNode.getNode(languageNode.getName()).getProperty("atlast:count").getLong() % 2 == 0;
        identityNode.setProperty("atlast:race", isRace);

        identityNode.setProperty("atlast:name", isRace ? NameGenerator.generateRaceName(languageNode) : NameGenerator.generateReligionName(languageNode));

        identityNode.setProperty("atlast:languagedescriptor", languageNode.getIdentifier());

        identityNode.setProperty("atlast:colour", ColourGenerator.generateColour());

        List<String> traits = new ArrayList<>();

        HstQuery query = requestContext.getQueryManager().createQuery(requestContext.getSession().getRootNode(), TraitDescriptor.class);

//        query.setLimit(3);

        query.addOrderByAscending("atlast:count");

        Filter filter = query.createFilter();

        filter.addEqualTo("atlast:race", isRace);

        query.setFilter(filter);

        HstQueryResult result = query.execute();

        HippoBeanIterator beanIterator = result.getHippoBeans();

        Node handle = null;

        List<String> modifiers = new ArrayList<>();

        int count = 0;

        while (beanIterator.hasNext() && count < 3) {

            TraitDescriptor traitDescriptor = (TraitDescriptor) beanIterator.nextHippoBean();

            if (!hasModifier(traitDescriptor, modifiers)) {
                handle = traitDescriptor.getNode().getParent();
                incrementCount(handle);

                traits.add(handle.getIdentifier());
                addModifiers(traitDescriptor, modifiers);
                count++;
            }

        }

        identityNode.setProperty("atlast:traits", traits.toArray(new String[]{}));


        return playerdataNode;
    }

    private void addModifiers(final TraitDescriptor traitDescriptor, List<String> modifiers) {
        for (Amount effect : traitDescriptor.getEffects()) {
            modifiers.add(effect.getResourceDescriptor().getName());
        }

    }

    private boolean hasModifier(final TraitDescriptor traitDescriptor, final List<String> modifiers) {

        boolean found = false;

        for (Amount effect : traitDescriptor.getEffects()) {
            found |= modifiers.contains(effect.getResourceDescriptor().getName());
        }

        return found;
    }

    private Node pickLanguage(HstRequestContext requestContext) throws RepositoryException, QueryException {

        HstQuery query = requestContext.getQueryManager().createQuery(requestContext.getSession().getRootNode(), LanguageDescriptor.class);

        query.setLimit(1);

        query.addOrderByAscending("atlast:count");

        HstQueryResult result = query.execute();

        HippoBeanIterator beanIterator = result.getHippoBeans();

        Node handle = null;

        if (beanIterator.hasNext()) {
            handle = beanIterator.nextHippoBean().getNode().getParent();
            incrementCount(handle);

        }

        return handle;
    }

    private void incrementCount(final Node handle) throws RepositoryException {
        NodeIterator nodeIterator = handle.getNodes(handle.getName());

        while (nodeIterator.hasNext()) {
            Node variantNode = nodeIterator.nextNode();

            Long count = 0L;
            count = variantNode.getProperty("atlast:count").getLong() + 1;

            variantNode.setProperty("atlast:count", count);
        }
    }

    private void createPlayerLands(final HstRequestContext requestContext, final Node userPlayerDataNode, final String userName, final String race, final String religion) throws RepositoryException, QueryException {

        HstQuery query = requestContext.getQueryManager().createQuery(requestContext.getSession().getRootNode(), LandDescriptor.class);

        query.addOrderByAscending("atlast:count");

        HstQueryResult result = query.execute();

        LandDescriptor rough = null;

        HippoBeanIterator landBeans = result.getHippoBeans();

        while (landBeans.hasNext()) {
            LandDescriptor landDescriptor = (LandDescriptor) landBeans.nextHippoBean();

            if (landDescriptor.isRough() && rough == null) {
                rough = landDescriptor;
            }
        }

        incrementCount(rough.getNode().getParent());

        query = requestContext.getQueryManager().createQuery(requestContext.getSession().getRootNode(), LandDescriptor.class);

        query.addOrderByAscending("atlast:count");

        result = query.execute();

        LandDescriptor easy = null;

        landBeans = result.getHippoBeans();

        while (landBeans.hasNext()) {
            LandDescriptor landDescriptor = (LandDescriptor) landBeans.nextHippoBean();

            if (!landDescriptor.isRough() && easy == null) {
                easy = landDescriptor;
            }
        }

        incrementCount(easy.getNode().getParent());

        Node languageDescriptorNode = requestContext.getSession().getNodeByIdentifier(userPlayerDataNode.getNode("identity").getProperty("atlast:languagedescriptor").getString());

        for (int i = 0; i < ROUGH_START_COUNT; i++) {
            String name = NameGenerator.generateLandName(languageDescriptorNode, rough.getNamePatterns());
            Node landNode = userPlayerDataNode.addNode(NodeNameCodec.encode(name) + "-" + rough.getName().toLowerCase(), "atlast:land");

            landNode.setProperty("atlast:player", userName);
            landNode.setProperty("atlast:name", name);
            landNode.setProperty("atlast:landdescriptor", rough.getUuid());

            if (i == 0) {
                developLand(requestContext, userPlayerDataNode, userName, rough, landNode, true, race, religion);
            }
        }

        for (int i = 0; i < EASY_START_COUNT; i++) {
            String name = NameGenerator.generateLandName(languageDescriptorNode, easy.getNamePatterns());
            Node landNode = userPlayerDataNode.addNode(NodeNameCodec.encode(name) + "-" + easy.getName().toLowerCase(), "atlast:land");

            landNode.setProperty("atlast:player", userName);
            landNode.setProperty("atlast:name", name);
            landNode.setProperty("atlast:landdescriptor", easy.getUuid());

            if (i == 0) {
                developLand(requestContext, userPlayerDataNode, userName, easy, landNode, true, race, religion);
            }

            if (i == 1) {
                developLand(requestContext, userPlayerDataNode, userName, easy, landNode, false, race, religion);
            }
        }
    }

    private void developLand(final HstRequestContext requestContext, final Node userPlayerDataNode, final String userName, final LandDescriptor landDescriptor, final Node landNode, boolean primary, final String race, final String religion) throws RepositoryException {
        landNode.addMixin("atlast:development");
        landNode.setProperty("atlast:wages", 100.0d);

        DevelopmentDescriptor development = null;

        for (DevelopmentDescriptor developmentDescriptor : landDescriptor.getAllowedDevelopments()) {
            if (developmentDescriptor.isPrimary() == primary) {
                development = developmentDescriptor;
                break;
            }
        }

        Random random = new Random();

        List<RecipeDescriptor> allowedRecipes = development.getAllAllowedRecipes();
        int recipeIndex = random.nextInt(allowedRecipes.size());
        int alternateRecipeIndex = recipeIndex;
        while (alternateRecipeIndex == recipeIndex) {
            alternateRecipeIndex = random.nextInt(allowedRecipes.size());
        }

        RecipeDescriptor recipe = allowedRecipes.get(recipeIndex);

        createTech(recipe, userPlayerDataNode);

        RecipeDescriptor alternateRecipe = allowedRecipes.get(alternateRecipeIndex);

        createTech(alternateRecipe, userPlayerDataNode);

        String popClass = primary ? "working" : "middle";

        createPlayerPops(requestContext, userPlayerDataNode, userName, landNode, popClass, 3, recipe.getSkill(), race, religion);

        landNode.setProperty("atlast:developmentdescriptor", development.getNode().getParent().getIdentifier());
        landNode.setProperty("atlast:recipedescriptor", recipe.getNode().getParent().getIdentifier());
        landNode.setProperty("atlast:developmentlevel", 1);
        landNode.setProperty("atlast:developmentprogress", 0.0d);
    }

    private void createTech(final RecipeDescriptor recipe, final Node userPlayerDataNode) throws RepositoryException {

        Node libraryNode = userPlayerDataNode.getNode("library");

        if (!libraryNode.hasNode(recipe.getNode().getParent().getIdentifier())) {
            Node techNode = libraryNode.addNode(recipe.getNode().getParent().getIdentifier(), "atlast:technology");

            techNode.setProperty("atlast:progress", 0.0d);
            techNode.setProperty("atlast:level", START_TECH_LEVEL);
        }

        libraryNode.setProperty("atlast:wages", 150.00);

    }

    private void createPlayerPops(final HstRequestContext requestContext, final Node userPlayerDataNode, final String userName, final Node landNode, String popClass, int count, String skill, String race, String religion) throws RepositoryException {


        Node languageDescriptorNode = requestContext.getSession().getNodeByIdentifier(userPlayerDataNode.getNode("identity").getProperty("atlast:languagedescriptor").getString());

        for (int i = 0; i < count; i++) {
            String name = NameGenerator.generatePopName(languageDescriptorNode, NameGenerator.generateName(languageDescriptorNode));
            Node popNode = landNode.addNode(NodeNameCodec.encode(name), "atlast:pop");

            popNode.setProperty("atlast:player", userName);
            popNode.setProperty("atlast:cash", POP_START_CASH);
            popNode.setProperty("atlast:name", name);
            popNode.setProperty("atlast:food", POP_START_FOOD);
            popNode.setProperty("atlast:class", popClass);
            popNode.setProperty("atlast:goods", 10.0d);
            popNode.setProperty("atlast:luxuries", 10.0d);
            popNode.setProperty("atlast:skills", new String[]{skill});
            popNode.setProperty("atlast:races", new String[]{race});
            popNode.setProperty("atlast:religions", new String[]{religion});
            popNode.setProperty("skill-" + skill, 50.0d);
            popNode.setProperty("race-" + race, 100.0d);
            popNode.setProperty("religion-" + religion, 100.0d);
            popNode.setProperty("hippo:availability", new String[]{"live"});

            String identity = "none".equals(race) ? religion : race;
            popNode.setProperty("atlast:identity", identity);
        }

    }


    public static String createPasswordHash(final String password) throws RepositoryException {
        try {
            return PasswordHelper.getHash(password.toCharArray());
        } catch (NoSuchAlgorithmException e) {
            throw new RepositoryException("Unable to hash password", e);
        } catch (IOException e) {
            throw new RepositoryException("Unable to hash password", e);
        }
    }
}
