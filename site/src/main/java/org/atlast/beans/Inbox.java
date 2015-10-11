package org.atlast.beans;

import java.util.List;

import javax.jcr.RepositoryException;

import org.atlast.beans.descriptors.RecipeDescriptor;
import org.hippoecm.hst.content.beans.Node;

/**
 * Created by wbarthet on 7/13/15.
 */
@Node(jcrType = "atlast:inbox")
public class Inbox extends AtlastObject {


    public List<Message> getMessages() {
        return getChildBeans(Message.class);
    }


}
