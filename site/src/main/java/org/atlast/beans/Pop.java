package org.atlast.beans;


import javax.jcr.RepositoryException;

import org.hippoecm.hst.content.beans.Node;

/**
 * Created by wbarthet on 7/13/15.
 */
@Node(jcrType = "atlast:pop")
public class Pop extends AtlastObject {

    public String getReligionName() {
        return getProperty("atlast:religionname");
    }

    public String getRaceName() {
        return getProperty("atlast:racename");
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

    public Identity getRace() {

        String identityUuid = getProperty("atlast:race");

        if ("none".equals(identityUuid)) {
            return null;
        }

        return getBeanByUUID(identityUuid, Identity.class);
    }

    public Identity getReligion() {

        String identityUuid = getProperty("atlast:religion");

        if ("none".equals(identityUuid)) {
            return null;
        }

        return getBeanByUUID(identityUuid, Identity.class);
    }

    public Identity getIdentity(Player player) {
        if (player.getIdentity().isRace()) {
            return getRace();
        }
        else {
            return getReligion();
        }
    }

    public String getIdentifiedName(Player player) {
        if (player.getIdentity().isRace()) {
            return getRaceName();
        }
        else {
            return getReligionName();
        }
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
