package org.atlast.beans;

import java.util.List;

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

    public List<Pop> getPops() {
        return getChildBeans(Pop.class);
    }

    public double getSkillLevel(String skillName) throws RepositoryException {
        Double skillLevel = 0.0;

        List<Pop> pops = getPops();

        if (pops.size()>0) {
            for (Pop pop : pops) {
                skillLevel += pop.getSkill(skillName);
            }

            skillLevel /= pops.size();
        }
        return skillLevel;
    }


    public String getUuid() {
        return getCanonicalUUID();
    }

}
