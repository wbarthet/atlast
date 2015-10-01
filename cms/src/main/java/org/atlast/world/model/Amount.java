package org.atlast.world.model;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

/**
 * Created by wbarthet on 7/13/15.
 */
public class Amount {


    Node node;

    public Amount(Session session, String uuid) throws RepositoryException {
        Node handle = session.getNodeByIdentifier(uuid);

        if ("hippo:handle".equals(handle.getPrimaryNodeType().getName())) {

            NodeIterator nodes = handle.getNodes(handle.getName());

            while (nodes.hasNext()) {
                Node node = nodes.nextNode();
                if ("published".equals(node.getProperty("hippostd:state").getString())) {
                    this.node = node;
                    break;
                }
            }

        } else if ("atlast:amount".equals(handle.getPrimaryNodeType().getName())) {

            this.node = handle;

        }
    }


    public Resource getResource() throws RepositoryException {
        Node handle = node.getSession().getNodeByIdentifier(node.getNode("atlast:resource").getProperty("hippo:docbase").getString());
        return new Resource(node.getSession(), handle.getNode(handle.getName()).getIdentifier());
    }

    public Long getQuantity() throws RepositoryException {
        return node.getProperty("atlast:quantity").getLong();
    }

}
