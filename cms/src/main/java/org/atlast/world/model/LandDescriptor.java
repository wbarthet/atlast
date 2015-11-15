package org.atlast.world.model;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;

/**
 * Created by wbarthet on 7/13/15.
 */
public class LandDescriptor {

    Node node;

    public LandDescriptor(Session session, String uuid) throws RepositoryException {
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

        } else if ("atlast:landdescriptor".equals(handle.getPrimaryNodeType().getName())) {

            this.node = handle;

        }
    }


    public String[] getNamePatterns() throws RepositoryException {
        Value[] values = node.getProperty("atlast:namePatterns").getValues();

        String[] strings = new String[values.length];

        for (int i = 0; i < values.length; i++) {
            strings[i] = values[i].getString();
        }

        return strings;
    }

    public String getName() throws RepositoryException {
        return node.getProperty("atlast:name").getString();
    }

}
