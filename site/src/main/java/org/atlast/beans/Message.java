package org.atlast.beans;

import java.util.List;

import org.hippoecm.hst.content.beans.Node;

/**
 * Created by wbarthet on 7/13/15.
 */
@Node(jcrType = "atlast:message")
public class Message extends AtlastObject {

    public String getTitle() {
        return getProperty("atlast:title");
    }


    public String getText() {
        return getProperty("atlast:text");
    }


}
