package org.atlast.world.model;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Value;
import javax.jcr.ValueFormatException;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.version.VersionException;

import org.hippoecm.hst.content.beans.ObjectBeanManagerException;
import org.hippoecm.hst.content.beans.standard.HippoBean;

/**
 * Created by wbarthet on 7/13/15.
 */
public class AtlastObject {

    private Node node;

    public AtlastObject(Node node) throws RepositoryException {
        if (node.isNodeType("atlast:object")) {
            this.node = node;
        } else {
            throw new RepositoryException("Cannot create object of type " + getClass().getName() + " node is of wrong type: " + node.getPrimaryNodeType().getName());
        }
    }


    protected <T extends AtlastObject> List<T> getChildNodes(Class<T> nodeMappingClass) {
        List<T> childNodes = new ArrayList<T>();
        NodeIterator nodes;
        try {
            nodes = node.getNodes();
            while (nodes.hasNext()) {
                Node child = nodes.nextNode();
                if (child == null) {
                    continue;
                }

                T childModel = null;
                try {
                    childModel = nodeMappingClass.getDeclaredConstructor(Node.class).newInstance(child);
                    childNodes.add(childModel);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        } catch (RepositoryException e) {

            return new ArrayList<T>();
        }
        return childNodes;
    }

    protected long getLongProperty(String name) {
        try {
            return node.getProperty(name).getLong();
        } catch (RepositoryException e) {
            return 0;
        }
    }

    protected double getDoubleProperty(String name) {
        try {
            return node.getProperty(name).getDouble();
        } catch (RepositoryException e) {
            return 0.0;
        }
    }

    protected String getStringProperty(String name) {
        try {
            return node.getProperty(name).getString();
        } catch (RepositoryException e) {
            return null;
        }
    }

    protected List<String> getStringListProperty(String name) {
        List<String> list = new ArrayList();
        try {
            Value[] values = node.getProperty(name).getValues();
            for (Value value : values) {
                list.add(value.getString());
            }
        } catch (RepositoryException e) {

        }
        return list;
    }

    protected Node getNode() {
        return node;
    }

    protected void setLongProperty(final String name, final long value) {
        try {
            node.setProperty(name, value);
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
    }

    protected void setDoubleProperty(final String name, final double value) {
        try {
            node.setProperty(name, value);
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
    }

    protected void setStringProperty(final String propertyName, final String value) throws RepositoryException {
        getNode().setProperty(propertyName, value);
    }

    public void setStringListProperty(final String propertyName, final List<String> strings) throws RepositoryException {
        getNode().setProperty(propertyName, strings.toArray(new String[]{}));
    }

}
