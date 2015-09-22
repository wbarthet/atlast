package org.atlast.world.model;
;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;

/**
 * Created by wbarthet on 9/20/15.
 */
public class Recipe {

    Node node;

    public Recipe(Session session, String uuid) throws RepositoryException {

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

        } else if ("atlast:recipedescriptor".equals(handle.getPrimaryNodeType().getName())) {

            this.node = handle;

        }
    }

    public long getLabour() throws RepositoryException {
        return node.getProperty("atlast:labour").getLong();
    }

    public Map<String, Double> getInputs() throws RepositoryException {
        Map<String, Double> inputs = new HashMap<>();

        Value[] inputValues = node.getProperty("inputs").getValues();

        for (Value value : inputValues) {

            Double amount = node.getProperty("input-" + value.getString()).getDouble();

            inputs.put(value.getString(), amount);
        }

        return inputs;
    }


    public Map<String, Double> getOutputs() throws RepositoryException {
        Map<String, Double> outputs = new HashMap<>();

        Value[] outputValues = node.getProperty("outputs").getValues();

        for (Value value : outputValues) {

            Double amount = node.getProperty("output-" + value.getString()).getDouble();

            outputs.put(value.getString(), amount);
        }

        return outputs;
    }


}
