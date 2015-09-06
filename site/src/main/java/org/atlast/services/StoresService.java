package org.atlast.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;
import javax.jcr.query.Query;

import org.atlast.beans.descriptors.Amount;
import org.atlast.beans.descriptors.RecipeDescriptor;
import org.atlast.beans.descriptors.ResourceDescriptor;
import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.beans.ObjectBeanManagerException;
import org.hippoecm.hst.content.beans.query.HstQuery;
import org.hippoecm.hst.content.beans.query.HstQueryResult;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.standard.HippoBeanIterator;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by wbarthet on 7/20/15.
 */
public class StoresService {

    private static Logger log = LoggerFactory.getLogger(StoresService.class);


    public void save(final HstRequestContext requestContext, String storeId, Map<String, Double> levels) throws RepositoryException, ObjectBeanManagerException {

        Session session = requestContext.getSession();
        final Node storeNode = session.getNodeByIdentifier(storeId);

        for (String item : levels.keySet()) {
            Double level = levels.get(item);

            storeNode.setProperty(item+"-level", level);
        }

        session.save();
    }

    public Map<String, ResourceDescriptor> getResources() {
        Map<String, ResourceDescriptor> resourceDescriptors;

        resourceDescriptors = new HashMap<>();

            HstRequestContext requestContext = RequestContextProvider.get();

            try {
                HstQuery query = requestContext.getQueryManager().createQuery(requestContext.getSiteContentBaseBean(), ResourceDescriptor.class);

                HstQueryResult result = query.execute();

                HippoBeanIterator hippoBeans = result.getHippoBeans();

                while (hippoBeans.hasNext()) {

                    ResourceDescriptor resource = (ResourceDescriptor) hippoBeans.nextHippoBean();

                    resourceDescriptors.put(resource.getName(), resource);
                }
            } catch (QueryException e) {
                log.error("error creating query", e);
            }

        return resourceDescriptors;
    }

    public ResourceDescriptor getResource(String name) {
        Map<String, ResourceDescriptor> resourceDescriptors;

            resourceDescriptors = getResources();
       
        return resourceDescriptors.get(name);
    }




}
