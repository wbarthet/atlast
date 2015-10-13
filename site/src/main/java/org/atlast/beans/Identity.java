package org.atlast.beans;


import java.util.ArrayList;
import java.util.List;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.atlast.beans.descriptors.LanguageDescriptor;
import org.atlast.beans.descriptors.TraitDescriptor;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.ObjectBeanManagerException;
import org.hippoecm.hst.content.beans.manager.ObjectConverter;

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

    public List<TraitDescriptor> getTraits() throws ObjectBeanManagerException, RepositoryException {

        List<TraitDescriptor> traits = new ArrayList<>();

        String[] traitUuids = getProperty("atlast:traits");

        ObjectConverter objectConverter = getObjectConverter();

        Session session = getNode().getSession();

        for (String traitUuid : traitUuids) {
            TraitDescriptor traitDescriptor = (TraitDescriptor) objectConverter.getObject(traitUuid, session);
            traits.add(traitDescriptor);
        }

        return traits;
    }
}
