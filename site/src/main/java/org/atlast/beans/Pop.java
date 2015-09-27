package org.atlast.beans;


import javax.jcr.RepositoryException;

import org.hippoecm.hst.content.beans.Node;

/**
 * Created by wbarthet on 7/13/15.
 */
@Node(jcrType = "atlast:pop")
public class Pop extends AtlastObject {

    public String getName() {
        return getProperty("atlast:name");
    }

    public String getPopClass() {
        return getProperty("atlast:class");
    }

    public Double getFood() {
        return getProperty("atlast:food");
    }

    public Double getLuxuries() {
        return getProperty("atlast:luxuries");
    }

    public Double getGoods() {
        return getProperty("atlast:goods");
    }

    public Double getCash() {
        return getProperty("atlast:cash");
    }

    public Land getLand() {
        return (Land) getParentBean();
    }

    public Double getSkill(String skill) throws RepositoryException {
        if (getNode().hasProperty("skill-" + skill)) {
            return getProperty("skill-" + skill);
        }
        return 0.0;
    }

    public Identity getIdentity() {
        return getBeanByUUID(getProperty("atlast:identity"), Identity.class);
    }


    public Double getIdentityLevel(String identity) {

        double level = 0;

        Double raceLevel = getProperty("race-" + identity);
        Double religionLevel = getProperty("religion-" + identity);

        if (raceLevel != null && raceLevel > 0) {
            level = raceLevel;
        }
        else if (religionLevel != null && religionLevel > 0) {
            level = religionLevel;
        }

        return level;
    }


}
