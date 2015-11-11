package org.atlast.world.model;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Value;

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

    public List<Trait> getTraits() throws RepositoryException {
        List<Trait> traits = new ArrayList<>();

        Value[] traitValues = getNode().getProperty("atlast:traits").getValues();

        for (Value value : traitValues) {
            traits.add(new Trait(getNode().getSession(), value.getString()));
        }

        return traits;
    }

    public boolean isRace() throws RepositoryException {
        return getNode().getProperty("atlast:race").getBoolean();
    }
}
