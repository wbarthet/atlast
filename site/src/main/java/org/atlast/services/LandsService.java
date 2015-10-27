package org.atlast.services;

import java.util.List;

import javax.jcr.LoginException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.atlast.beans.Land;
import org.atlast.beans.Pop;
import org.atlast.beans.descriptors.Amount;
import org.atlast.beans.descriptors.RecipeDescriptor;
import org.atlast.beans.descriptors.ResourceDescriptor;
import org.hippoecm.hst.content.beans.ObjectBeanManagerException;
import org.hippoecm.hst.core.request.HstRequestContext;

/**
 * Created by wbarthet on 7/20/15.
 */
public class LandsService {


    public void fire(HstRequestContext requestContext, String popId) throws RepositoryException {
        Session session = requestContext.getSession();

        Node popNode = session.getNodeByIdentifier(popId);

        Node poolNode = requestContext.getSiteContentBaseBean().getNode().getNode("worlddata/pool");

        popNode.setProperty("atlast:player", "global");

        session.move(popNode.getPath(), poolNode.getPath() + "/" + popNode.getName());

        session.save();
    }


    public void hire(HstRequestContext requestContext, String popId, String player) throws RepositoryException, ObjectBeanManagerException {
        Session session = requestContext.getSession();

        Node popNode = session.getNodeByIdentifier(popId);

        Node landNode = requestContext.getContentBean().getNode();

        Land land = (Land) requestContext.getObjectBeanManager().getObjectByUuid(landNode.getIdentifier());

        if (!((land.getPops().size()+1) / 3 > land.getDevelopmentLevel())) {

            popNode.setProperty("atlast:player", player);

            session.move(popNode.getPath(), landNode.getPath() + "/" + popNode.getName());

            session.save();
        }
    }

    public void save(final HstRequestContext requestContext, String landId, Double wages, String recipeUuid) throws RepositoryException, ObjectBeanManagerException {

            Session session = requestContext.getSession();

            final Node landNode = session.getNodeByIdentifier(landId);

            landNode.setProperty("atlast:wages", wages);

            if (!landNode.getProperty("atlast:recipedescriptor").getString().equals(recipeUuid)) {
                RecipeDescriptor recipeDescriptor = (RecipeDescriptor) requestContext.getContentBeansTool().getObjectConverter().getObject(session.getNodeByIdentifier(recipeUuid));

                landNode.setProperty("atlast:recipedescriptor", recipeUuid);
                landNode.setProperty("atlast:skill", recipeDescriptor.getSkill());

                final List<Amount> inputDescriptors = recipeDescriptor.getInputs();
                final int inputsSize = inputDescriptors.size();
                String[] inputs = new String[inputsSize];

                for (int i = 0; i < inputsSize; i++) {
                    Amount amount = inputDescriptors.get(i);
                    ResourceDescriptor resourceDescriptor = amount.getResourceDescriptor();
                    inputs[i] = resourceDescriptor.getParentBean().getName() + "-" + resourceDescriptor.getNode().getName() + "-" + amount.getQuantity();
                }

                landNode.setProperty("atlast:inputs", inputs);

                final List<Amount> outputDescriptors = recipeDescriptor.getOutputs();
                final int outputsSize = outputDescriptors.size();
                String[] outputs = new String[outputsSize];

                for (int i = 0; i < outputsSize; i++) {
                    Amount amount = outputDescriptors.get(i);
                    ResourceDescriptor resourceDescriptor = amount.getResourceDescriptor();
                    outputs[i] = resourceDescriptor.getParentBean().getName() + "-" + resourceDescriptor.getNode().getName() + "-" + amount.getQuantity();
                }

                landNode.setProperty("atlast:outputs", outputs);

                landNode.setProperty("atlast:labour", recipeDescriptor.getLabour());

            }

            session.save();
    }

    public void build(final HstRequestContext requestContext, final String landId, final String developmentDescriptorUuid) throws RepositoryException {
        if (developmentDescriptorUuid != null) {
            Session session = requestContext.getSession();

            final Node landNode = session.getNodeByIdentifier(landId);

            landNode.addMixin("atlast:development");


            landNode.setProperty("atlast:developmentdescriptor", developmentDescriptorUuid);
            landNode.setProperty("atlast:wages", 50.0d);
            landNode.setProperty("atlast:recipedescriptor", "none");
            landNode.setProperty("atlast:developmentlevel", 0);
            landNode.setProperty("atlast:developmentprogress", 0.0d);

            session.save();
        }
    }

    public void destroy(final HstRequestContext requestContext, String landId) throws RepositoryException {
        Session session = requestContext.getSession();

        final Node landNode = session.getNodeByIdentifier(landId);

        landNode.removeMixin("atlast:development");

        NodeIterator popNodes = landNode.getNodes();
        while (popNodes.hasNext()) {
            Node popNode = popNodes.nextNode();
            fire(requestContext, popNode.getIdentifier());
        }

        landNode.setProperty("atlast:developmentdescriptor", "empty");
        landNode.setProperty("atlast:developmentlevel", 0);
        landNode.setProperty("atlast:developmentprogress", 0.0d);

        session.save();
    }


}
