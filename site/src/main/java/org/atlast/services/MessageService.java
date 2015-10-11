package org.atlast.services;

import java.util.List;
import java.util.Random;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.atlast.beans.ContentDocument;
import org.atlast.beans.Message;
import org.atlast.beans.Player;
import org.atlast.beans.TextList;
import org.atlast.beans.descriptors.Amount;
import org.atlast.beans.descriptors.RecipeDescriptor;
import org.atlast.beans.descriptors.ResourceDescriptor;
import org.atlast.util.languages.ColourGenerator;
import org.atlast.util.languages.NameGenerator;
import org.hippoecm.hst.content.beans.ObjectBeanManagerException;
import org.hippoecm.hst.core.request.HstRequestContext;

/**
 * Created by wbarthet on 7/20/15.
 */
public class MessageService {


    public void sendMessage(HstRequestContext requestContext, ContentDocument messageContentDocument, Player player) throws RepositoryException, ObjectBeanManagerException {
        Session session = requestContext.getSession();

        Node messageNode = player.getInbox().getNode().addNode("message", "atlast:message");

        String title = processMessage(requestContext, messageContentDocument.getTitle(), player);

        messageNode.setProperty("atlast:title", title);

        String text = processMessage(requestContext, messageContentDocument.getContent().getContent(), player);

        messageNode.setProperty("atlast:text", text);

        session.save();
    }

    private String processMessage(final HstRequestContext requestContext, String messageContent, final Player player) throws RepositoryException, ObjectBeanManagerException {
        
        String processed = messageContent;

        while (processed.contains("${")) {
            processed = processed.replaceAll("\\$\\{IDENTITY\\}", player.getIdentity().getName());
            processed = processed.replaceAll("\\$\\{NAME1\\}", NameGenerator.generateName(player.getIdentity().getLanguageDescriptor().getNode()));
            processed = processed.replaceAll("\\$\\{NAME2\\}", NameGenerator.generateName(player.getIdentity().getLanguageDescriptor().getNode()));
            processed = processed.replaceAll("\\$\\{TRAIT1\\}", NameGenerator.generateName(player.getIdentity().getLanguageDescriptor().getNode()));
            processed = processed.replaceAll("\\$\\{TRAIT2\\}", NameGenerator.generateName(player.getIdentity().getLanguageDescriptor().getNode()));
            processed = processed.replaceAll("\\$\\{TRAIT3\\}", NameGenerator.generateName(player.getIdentity().getLanguageDescriptor().getNode()));
            processed = processed.replaceAll("\\$\\{COLOUR\\}", ColourGenerator.generateColourName());

            if (processed.contains("${")) {
                String word = processed.substring(processed.indexOf('{') + 1, processed.indexOf('}'));
                processed = processed.replace("${" + word + "}", getRandomMessageElement(word, requestContext, player));
            }
        }


        return processed;
    }

    private String getRandomMessageElement(final String word, HstRequestContext requestContext, Player player) throws ObjectBeanManagerException {

        String path = "/content/documents/atlastserver/info/game-texts/" + word.toLowerCase();

        path = player.getIdentity().isRace() ? path + "-race" : path + "-religion";

        TextList variants = (TextList) requestContext.getObjectBeanManager().getObject(path);

        if (variants != null) {
            String[] entries = variants.getEntries();

            Random r = new Random();

            int index = r.nextInt(entries.length);

            return entries[index];
        }
        
        return word;
    }


}
