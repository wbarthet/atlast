package org.atlast.beans;

import javax.jcr.RepositoryException;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.ObjectBeanManagerException;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoDocument;
import org.hippoecm.hst.content.beans.standard.HippoItem;

/**
 * Created by wbarthet on 7/13/15.
 */
@Node(jcrType = "atlast:object")
public class AtlastObject extends HippoItem {


    public <T extends BaseDocument> T getDescriptor(String name, Class<T> beanmappingClass) {
        String descriptorUuid = getProperty(name);

        if (!"none".equals(descriptorUuid)) {

            try {
                return (T) objectConverter.getObject(descriptorUuid, getNode().getSession());
            } catch (ObjectBeanManagerException e) {
                e.printStackTrace();
            } catch (RepositoryException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public String getUuid() {
        return getCanonicalUUID();
    }

}
