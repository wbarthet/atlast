package org.atlast.world.model;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

/**
 * Created by wbarthet on 7/13/15.
 */
public class Identity extends AtlastObject {

    public Identity(Node node) throws RepositoryException {
        super(node);
    }



    public Node getLanguageDescriptorNode() throws RepositoryException {
        return getNode().getSession().getNodeByIdentifier(getStringProperty("atlast:languagedescriptor"));
    }
}
