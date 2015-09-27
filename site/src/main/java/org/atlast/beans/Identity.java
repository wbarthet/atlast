package org.atlast.beans;


import org.hippoecm.hst.content.beans.Node;

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


}
