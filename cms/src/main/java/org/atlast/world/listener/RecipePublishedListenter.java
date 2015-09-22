package org.atlast.world.listener;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.onehippo.cms7.event.HippoEvent;
import org.onehippo.cms7.event.HippoEventConstants;
import org.onehippo.cms7.services.HippoServiceRegistry;
import org.onehippo.cms7.services.eventbus.HippoEventBus;
import org.onehippo.cms7.services.eventbus.HippoEventListener;
import org.onehippo.cms7.services.eventbus.Subscribe;
import org.onehippo.repository.events.HippoWorkflowEvent;
import org.onehippo.repository.modules.DaemonModule;

/**
 * Created by wbarthet on 9/20/15.
 */
public class RecipePublishedListenter implements DaemonModule {


    Session session;

    @Subscribe
    public void handleEvent(HippoEvent event) throws RepositoryException {
        if (HippoEventConstants.CATEGORY_WORKFLOW.equals(event.category())) {
            HippoWorkflowEvent workflowEvent = new HippoWorkflowEvent(event);
            if ("publish".equals(workflowEvent.methodName())) {

                Node handleNode = session.getNodeByIdentifier(workflowEvent.subjectId());


                NodeIterator variantNodes = handleNode.getNodes(handleNode.getName());

                while (variantNodes.hasNext()) {
                    Node node = variantNodes.nextNode();


                    if (!node.isCheckedOut()) {
                        session.getWorkspace().getVersionManager().checkout(node.getPath());
                    }


                    if ("atlast:recipedescriptor".equals(workflowEvent.documentType())) {

                        List<String> inputs = new ArrayList<>();
                        List<String> outputs = new ArrayList<>();

                        NodeIterator nodes = node.getNodes("atlast:inputs");

                        while (nodes.hasNext()) {
                            Node inputNode = nodes.nextNode();

                            Node resourceNode = session.getNodeByIdentifier(inputNode.getNode("atlast:resource").getProperty("hippo:docbase").getString());

                            inputs.add(resourceNode.getParent().getName() + "-" + resourceNode.getName());

                            String resourceString = resourceNode.getParent().getName() + "-" + resourceNode.getName();
                            node.setProperty("input-" + resourceString, inputNode.getProperty("atlast:quantity").getLong());
                        }


                        nodes = node.getNodes("atlast:outputs");

                        while (nodes.hasNext()) {
                            Node outputNode = nodes.nextNode();

                            Node resourceNode = session.getNodeByIdentifier(outputNode.getNode("atlast:resource").getProperty("hippo:docbase").getString());

                            outputs.add(resourceNode.getParent().getName() + "-" + resourceNode.getName());

                            String resouceString = resourceNode.getParent().getName() + "-" + resourceNode.getName();
                            node.setProperty("output-" + resouceString, outputNode.getProperty("atlast:quantity").getLong());
                        }

                        node.setProperty("inputs", inputs.toArray(new String[]{}));
                        node.setProperty("outputs", outputs.toArray(new String[]{}));

                        session.save();

                    }
                }

            }
        }
    }


    @Override
    public void initialize(final Session session) throws RepositoryException {
        this.session = session;
        HippoServiceRegistry.registerService(this, HippoEventBus.class);
    }

    @Override
    public void shutdown() {
        HippoServiceRegistry.unregisterService(this, HippoEventBus.class);
    }
}
