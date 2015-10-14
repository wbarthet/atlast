package org.atlast.services;

import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.atlast.beans.descriptors.Amount;
import org.atlast.beans.descriptors.RecipeDescriptor;
import org.atlast.beans.descriptors.ResourceDescriptor;
import org.hippoecm.hst.content.beans.ObjectBeanManagerException;
import org.hippoecm.hst.core.request.HstRequestContext;

/**
 * Created by wbarthet on 7/20/15.
 */
public class CouncilService {


    public void fire(HstRequestContext requestContext, String popId) throws RepositoryException {
        Session session = requestContext.getSession();

        Node popNode = session.getNodeByIdentifier(popId);

        Node poolNode = requestContext.getSiteContentBaseBean().getNode().getNode("worlddata/pool");

        popNode.setProperty("atlast:player", "global");

        session.move(popNode.getPath(), poolNode.getPath() + "/" + popNode.getName());

        session.save();
    }


    public void hire(HstRequestContext requestContext, String popId, String player) throws RepositoryException {
        Session session = requestContext.getSession();

        Node popNode = session.getNodeByIdentifier(popId);

        Node libraryNode = requestContext.getContentBean().getNode();

        popNode.setProperty("atlast:player", player);

        session.move(popNode.getPath(), libraryNode.getPath()+"/"+popNode.getName());

        session.save();
    }

    public void saveLibrary(final HstRequestContext requestContext, String libId, Double wages, String focus) throws RepositoryException, ObjectBeanManagerException {

            Session session = requestContext.getSession();

            final Node libraryNode = session.getNodeByIdentifier(libId);

            libraryNode.setProperty("atlast:wages", wages);

            libraryNode.setProperty("atlast:focus", focus);


            session.save();
    }

    public void saveStorehouse(final HstRequestContext requestContext, String storehouseId, Double wages, String recipe) throws RepositoryException, ObjectBeanManagerException {

        Session session = requestContext.getSession();

        final Node storehouseNode = session.getNodeByIdentifier(storehouseId);

        storehouseNode.setProperty("atlast:wages", wages);

        storehouseNode.setProperty("atlast:recipedescriptor", recipe);


        session.save();
    }



}
