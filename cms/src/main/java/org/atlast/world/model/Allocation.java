package org.atlast.world.model;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

/**
 * Created by wbarthet on 7/13/15.
 */
public class Allocation {


    Node node;

    public Allocation(Session session, String uuid) throws RepositoryException {
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

        } else if ("atlast:allocation".equals(handle.getPrimaryNodeType().getName())) {

            this.node = handle;

        }
    }


    public LandDescriptor getLand() throws RepositoryException {
        Node handle = node.getSession().getNodeByIdentifier(node.getNode("atlast:land").getProperty("hippo:docbase").getString());
        NodeIterator variants = handle.getNodes(handle.getName());

        Node published = null;

        while (variants.hasNext()) {
            Node variant = variants.nextNode();

            if ("published".equals(variant.getProperty("hippostd:state").getString())) {
                published = variant;
            }
        }
        return new LandDescriptor(node.getSession(), published.getIdentifier());
    }

    public Double getAmount() throws RepositoryException {
        return node.getProperty("atlast:amount").getDouble();
    }

}
