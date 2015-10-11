package org.atlast.beans;


import javax.jcr.RepositoryException;

import org.atlast.beans.descriptors.LanguageDescriptor;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.ObjectBeanManagerException;

/**
 * Created by wbarthet on 7/13/15.
 */
@Node(jcrType = "atlast:identity")
public class Identity extends AtlastObject {

    public String getName() {
        return getProperty("atlast:name");
    }
    public String getColour() {
        return getProperty("atlast:colour");
    }

    public boolean isRace() {
        return getProperty("atlast:race");
    }


    public LanguageDescriptor getLanguageDescriptor() throws RepositoryException, ObjectBeanManagerException {
        String uuid = getProperty("atlast:languagedescriptor");

        return (LanguageDescriptor) getObjectConverter().getObject(uuid, getNode().getSession());
    }
}
