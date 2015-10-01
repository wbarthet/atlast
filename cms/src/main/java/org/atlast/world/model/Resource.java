package org.atlast.world.model;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

/**
 * Created by wbarthet on 7/13/15.
 */
public class Resource {

    Node node;

    public Resource(Session session, String uuid) throws RepositoryException {
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

        } else if ("atlast:resourcedescriptor".equals(handle.getPrimaryNodeType().getName())) {

            this.node = handle;

        }
    }




    public String getName() throws RepositoryException {
        return node.getProperty("atlast:name").getString();
    }

}
