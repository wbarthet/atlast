package org.atlast.services;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.ItemExistsException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.nodetype.NoSuchNodeTypeException;
import javax.jcr.version.VersionException;


import org.atlast.beans.descriptors.LandDescriptor;
import org.atlast.beans.descriptors.LanguageDescriptor;
import org.atlast.beans.descriptors.ResourceDescriptor;
import org.atlast.util.languages.NameGenerator;
import org.hippoecm.hst.content.beans.query.HstQuery;
import org.hippoecm.hst.content.beans.query.HstQueryResult;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.standard.HippoBeanIterator;
import org.hippoecm.hst.core.request.HstRequestContext;
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
    public static final double POP_START_CASH = 50.0;
    public static final double POP_START_FOOD = 10.0;

    public String signup(HstRequestContext requestContext, String userName, String password) throws RepositoryException, QueryException {
        Session session = requestContext.getSession();


        if (session.nodeExists("/hippo:configuration/hippo:users/" + userName)) {
            return "User name already exists, please choose a different user name.";
        } else {
            Node userNode = session.getNode("/hippo:configuration/hippo:users").addNode(userName, "hipposys:user");

            userNode.setProperty("hipposys:active", true);
            userNode.setProperty("hipposys:securityprovider", "internal");
            userNode.setProperty("hipposys:password", password);

            Node playersGroupNode = session.getNode("/hippo:configuration/hippo:groups/players");

            addValue(playersGroupNode, "hipposys:members", userName);


            Node playerdataFolderNode = session.getNode("/content/documents/atlastserver/playerdata");

            Node userPlayerDataNode = createPlayerData(playerdataFolderNode, userName, requestContext);

            createPlayerStores(userPlayerDataNode, requestContext, userName);

            //create lands
            createPlayerLands(requestContext, userPlayerDataNode, userName);

            //create pops
            createPlayerPops(requestContext, userPlayerDataNode, userName);

            //create notification

            session.save();
        }


        return "User created";
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
            storeNode.setProperty(resourceName, 0.0d);
            storeNode.setProperty(resourceName + "-level", 10.0d);

            addValue(storeNode, "atlast:items", resourceName);
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
        playerdataNode.setProperty("atlast:languagedescriptor", pickLanguage(requestContext));

        return playerdataNode;
    }

    private String pickLanguage(HstRequestContext requestContext) throws RepositoryException, QueryException {

        HstQuery query = requestContext.getQueryManager().createQuery(requestContext.getSession().getRootNode(), LanguageDescriptor.class);

        query.setLimit(1);

        query.addOrderByAscending("atlast:count");

        HstQueryResult result = query.execute();

        HippoBeanIterator beanIterator = result.getHippoBeans();

        String uuid = "";

        if (beanIterator.hasNext()) {
            Node handle = beanIterator.nextHippoBean().getNode().getParent();
            incrementCount(handle);

            uuid = handle.getIdentifier();
        }

        return uuid;
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

    private void createPlayerLands(final HstRequestContext requestContext, final Node userPlayerDataNode, final String userName) throws RepositoryException, QueryException {

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

        Node languageDescriptorNode = requestContext.getSession().getNodeByIdentifier(userPlayerDataNode.getProperty("atlast:languagedescriptor").getString());

        for (int i = 0; i < ROUGH_START_COUNT; i++) {
            String name = NameGenerator.generateLandName(languageDescriptorNode, rough.getNamePatterns());
            Node landNode = userPlayerDataNode.addNode(NodeNameCodec.encode(name) + "-" + rough.getName().toLowerCase(), "atlast:land");

            landNode.setProperty("atlast:player", userName);
            landNode.setProperty("atlast:name", name);
            landNode.setProperty("atlast:landdescriptor", rough.getUuid());
        }

        for (int i = 0; i < EASY_START_COUNT; i++) {
            String name = NameGenerator.generateLandName(languageDescriptorNode, easy.getNamePatterns());
            Node landNode = userPlayerDataNode.addNode(NodeNameCodec.encode(name) + "-" + easy.getName().toLowerCase(), "atlast:land");

            landNode.setProperty("atlast:player", userName);
            landNode.setProperty("atlast:name", name);
            landNode.setProperty("atlast:landdescriptor", easy.getUuid());
        }
    }

    private void createPlayerPops(final HstRequestContext requestContext, final Node userPlayerDataNode, final String userName) throws RepositoryException {

        Node poolNode = userPlayerDataNode.addNode("pool", "atlast:land");

        poolNode.setProperty("atlast:name", "pool");

        Node languageDescriptorNode = requestContext.getSession().getNodeByIdentifier(userPlayerDataNode.getProperty("atlast:languagedescriptor").getString());

        for (int i = 0; i < WORKING_START_COUNT; i++) {
            String name = NameGenerator.generatePopName(languageDescriptorNode, NameGenerator.generateName(languageDescriptorNode));
            Node popNode = poolNode.addNode(NodeNameCodec.encode(name), "atlast:pop");

            popNode.setProperty("atlast:player", userName);
            popNode.setProperty("atlast:cash", POP_START_CASH);
            popNode.setProperty("atlast:name", name);
            popNode.setProperty("atlast:food", POP_START_FOOD);
            popNode.setProperty("atlast:class", "working");
            popNode.setProperty("atlast:goods", 0.0d);
            popNode.setProperty("atlast:luxuries", 0.0d);

        }

        for (int i = 0; i < MIDDLE_START_COUNT; i++) {
            String name = NameGenerator.generatePopName(languageDescriptorNode, NameGenerator.generateName(languageDescriptorNode));
            Node popNode = poolNode.addNode(NodeNameCodec.encode(name), "atlast:pop");

            popNode.setProperty("atlast:player", userName);
            popNode.setProperty("atlast:cash", POP_START_CASH*2);
            popNode.setProperty("atlast:name", name);
            popNode.setProperty("atlast:food", POP_START_FOOD);
            popNode.setProperty("atlast:class", "middle");
            popNode.setProperty("atlast:goods", POP_START_FOOD);
            popNode.setProperty("atlast:luxuries", 0.0d);

        }

        for (int i = 0; i < UPPER_START_COUNT; i++) {
            String name = NameGenerator.generatePopName(languageDescriptorNode, NameGenerator.generateName(languageDescriptorNode));
            Node popNode = poolNode.addNode(NodeNameCodec.encode(name), "atlast:pop");

            popNode.setProperty("atlast:player", userName);
            popNode.setProperty("atlast:cash", POP_START_CASH*3);
            popNode.setProperty("atlast:name", name);
            popNode.setProperty("atlast:food", POP_START_FOOD);
            popNode.setProperty("atlast:class", "upper");
            popNode.setProperty("atlast:goods", POP_START_FOOD);
            popNode.setProperty("atlast:luxuries", POP_START_FOOD);

        }

    }
}
