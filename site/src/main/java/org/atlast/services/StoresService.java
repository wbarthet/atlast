package org.atlast.services;

import java.util.List;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;

import org.atlast.beans.descriptors.Amount;
import org.atlast.beans.descriptors.RecipeDescriptor;
import org.atlast.beans.descriptors.ResourceDescriptor;
import org.hippoecm.hst.content.beans.ObjectBeanManagerException;
import org.hippoecm.hst.core.request.HstRequestContext;

/**
 * Created by wbarthet on 7/20/15.
 */
public class StoresService {


    public void save(final HstRequestContext requestContext, String storeId, Map<String, Double> levels) throws RepositoryException, ObjectBeanManagerException {

        Session session = requestContext.getSession();
        final Node storeNode = session.getNodeByIdentifier(storeId);

        for (String item : levels.keySet()) {
            Double level = levels.get(item);

            storeNode.setProperty(item+"-level", level);
        }

        session.save();
    }




}
