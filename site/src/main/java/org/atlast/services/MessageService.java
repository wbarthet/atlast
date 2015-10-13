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
import org.atlast.beans.descriptors.TraitDescriptor;
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
            processed = processed.replace("${IDENTITY}", player.getIdentity().getName());
            processed = processed.replace("${NAME1}", NameGenerator.generateName(player.getIdentity().getLanguageDescriptor().getNode()));
            processed = processed.replace("${NAME2}", NameGenerator.generateName(player.getIdentity().getLanguageDescriptor().getNode()));

            List<TraitDescriptor> traits = player.getIdentity().getTraits();

            for (int i = 0; i < traits.size(); i++) {
                TraitDescriptor trait = traits.get(i);
                processed = processed.replace("${TRAIT"+(i+1)+"}", trait.getDescription().getContent() + " " + getTraitSummary(trait));
            }

            processed = processed.replace("${COLOUR}", ColourGenerator.generateColourName());

            if (processed.contains("${")) {
                String word = processed.substring(processed.indexOf('{') + 1, processed.indexOf('}'));
                processed = processed.replace("${" + word + "}", getRandomMessageElement(word, requestContext, player));
            }
        }


        return processed;
    }

    private String getTraitSummary(final TraitDescriptor trait) {
        String summary = "";

        for (Amount effect : trait.getEffects()) {
            summary += " " + effect.getQuantity() + "% ";
            summary += effect.getResourceDescriptor().getName();
        }

        return summary;
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
