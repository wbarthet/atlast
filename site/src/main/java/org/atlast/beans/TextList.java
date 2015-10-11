package org.atlast.beans;

import org.hippoecm.hst.content.beans.Node;

/**
 * Created by wbarthet on 7/13/15.
 */
@Node(jcrType = "atlast:textlist")
public class TextList extends AtlastObject {

    public String[] getEntries() {
        return getProperty("atlast:entries");
    }


}
