package org.atlast.world.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;

/**
 * Created by wbarthet on 7/13/15.
 */
public class Trait {

    Node node;

    public Trait(Session session, String uuid) throws RepositoryException {
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

        } else if ("atlast:traitdescriptor".equals(handle.getPrimaryNodeType().getName())) {

            this.node = handle;

        }
    }

    public List<Amount> getEffects() throws RepositoryException {

        List<Amount> amounts = new ArrayList<>();
        NodeIterator effectNodes = node.getNodes("atlast:effects");

        while (effectNodes.hasNext()) {

            amounts.add(new Amount(node.getSession(), effectNodes.nextNode().getIdentifier()));
        }

        return amounts;
    }



}
