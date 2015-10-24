package org.atlast.services;

import java.util.List;
import java.util.Random;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.lang.ArrayUtils;
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
import org.atlast.util.languages.NumberGenerator;
import org.hippoecm.hst.content.beans.ObjectBeanManagerException;
import org.hippoecm.hst.core.request.HstRequestContext;

/**
 * Created by wbarthet on 7/20/15.
 */
public class MessageService {

    private static String[] RESERVED_WORDS = new String[]{"NAME1", "NAME2", "TRAIT1", "TRAIT2", "TRAIT3", "COLOUR"};

    public void sendMessage(HstRequestContext requestContext, ContentDocument messageContentDocument, Player player) throws RepositoryException, ObjectBeanManagerException {
        Session session = requestContext.getSession();

        Node messageNode = player.getInbox().getNode().addNode("message", "atlast:message");

        String title = processReservedWords(messageContentDocument.getTitle(), player);

        messageNode.setProperty("atlast:title", title);

        String text = processMessage(requestContext, messageContentDocument.getContent().getContent(), player);

        messageNode.setProperty("atlast:text", text);

        session.save();
    }

    private String processMessage(final HstRequestContext requestContext, String messageContent, final Player player) throws RepositoryException, ObjectBeanManagerException {

        String processed = messageContent;

        int count = 0;


        List<TraitDescriptor> traits = player.getIdentity().getTraits();

        for (int i = 0; i < traits.size(); i++) {
            TraitDescriptor trait = traits.get(i);
            processed = processed.replaceFirst("~TRAIT" + (i + 1) + "`", trait.getDescription().getContent() + " <p>" + getTraitSummary(trait) + "</p>");

            processed = processReservedWords(processed, player);
        }

        while (processed.contains("~") || count > 1000) {

            if (processed.contains("~")) {
                String word = processed.substring(processed.indexOf('~') + 1, processed.indexOf('`'));
                if (!ArrayUtils.contains(RESERVED_WORDS, word)) {
                    processed = processed.replaceFirst("~" + word + "`", getRandomMessageElement(word, requestContext, player));
                    processed = processReservedWords(processed, player);
                }
            }

            count++;
        }


        return processed;
    }

    private String processReservedWords(String messageContent, final Player player) throws RepositoryException, ObjectBeanManagerException {

        String processed = messageContent;
        
        processed = processed.replace("~IDENTITY`", player.getIdentity().getName());
        processed = processed.replace("~NAME1`", NameGenerator.generateName(player.getIdentity().getLanguageDescriptor().getNode()));
        processed = processed.replace("~NAME2`", NameGenerator.generateName(player.getIdentity().getLanguageDescriptor().getNode()));
        processed = processed.replace("~COLOUR`", ColourGenerator.generateColourName());
        processed = processed.replace("~NUMBER`", NumberGenerator.generateNumber());


        return processed;
    }

    private String getTraitSummary(final TraitDescriptor trait) {
        String summary = "Your people consume ";

        List<Amount> effects = trait.getEffects();
        for (int i = 0; i < effects.size(); i++) {
            Amount effect = effects.get(i);
            if (effect.getQuantity() > 0) {
                summary += effect.getQuantity() + "% more ";
            } else {
                summary += effect.getQuantity() * -1 + "% less ";
            }

            summary += effect.getResourceDescriptor().getName();
            if (i == effects.size() -1) {
                summary += ".";
            } else {
                summary += " and ";
            }
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
