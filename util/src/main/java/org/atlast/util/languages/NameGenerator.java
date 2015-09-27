package org.atlast.util.languages;

import java.util.Random;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Value;

/**
 * Created by wbarthet on 7/20/15.
 */
public class NameGenerator {

    public static String generateName(Node languageDescriptorNode) throws RepositoryException {

        if ("hippo:handle".equals(languageDescriptorNode.getPrimaryNodeType().getName())) {
            languageDescriptorNode = languageDescriptorNode.getNode(languageDescriptorNode.getName());
        }

        String name = "";

        Value[] k = languageDescriptorNode.getProperty("atlast:k").getValues();
        Value[] u = languageDescriptorNode.getProperty("atlast:u").getValues();
        Value[] tt = languageDescriptorNode.getProperty("atlast:tt").getValues();
        Value[] ee = languageDescriptorNode.getProperty("atlast:ee").getValues();
        Value[] oa = languageDescriptorNode.getProperty("atlast:oa").getValues();
        Value[] sp = languageDescriptorNode.getProperty("atlast:sp").getValues();
        Value[] ri = languageDescriptorNode.getProperty("atlast:ri").getValues();
        Value[] an = languageDescriptorNode.getProperty("atlast:an").getValues();
        Value[] names = languageDescriptorNode.getProperty("atlast:names").getValues();

        Random r = new Random();

        int index = r.nextInt(names.length);

        name = names[index].getString();

        while (name.contains("K")) {
            index = r.nextInt(k.length);
            name = name.replaceFirst("K", k[index].getString());
        }
        while (name.contains("U")) {
            index = r.nextInt(u.length);
            name = name.replaceFirst("U", u[index].getString());
        }
        while (name.contains("TT")) {
            index = r.nextInt(tt.length);
            name = name.replaceFirst("TT", tt[index].getString());
        }
        while (name.contains("EE")) {
            index = r.nextInt(ee.length);
            name = name.replaceFirst("EE", ee[index].getString());
        }
        while (name.contains("OA")) {
            index = r.nextInt(oa.length);
            name = name.replaceFirst("OA", oa[index].getString());
        }
        while (name.contains("SP")) {
            index = r.nextInt(sp.length);
            name = name.replaceFirst("SP", sp[index].getString());
        }
        while (name.contains("RI")) {
            index = r.nextInt(ri.length);
            name = name.replaceFirst("RI", ri[index].getString());
        }
        while (name.contains("AN")) {
            index = r.nextInt(an.length);
            name = name.replaceFirst("AN", an[index].getString());
        }

        name = name.replaceFirst(name.charAt(0)+"", Character.toUpperCase(name.charAt(0))+"");

        return name;

    }


    public static String generateLandName(final Node languageDescriptorNode, final String[] namePatterns) throws RepositoryException {

        Random r = new Random();

        int index = r.nextInt(namePatterns.length);

        String name = namePatterns[index];

        name = name.replace("NAME", generateName(languageDescriptorNode));

        return name;
    }


    public static String generatePopName(Node languageDescriptorNode, String parentName) throws RepositoryException {

        if ("hippo:handle".equals(languageDescriptorNode.getPrimaryNodeType().getName())) {
            languageDescriptorNode = languageDescriptorNode.getNode(languageDescriptorNode.getName());
        }

        String f1 = parentName;
        String f2 = parentName;

        if (parentName.contains(" ")) {
            f1 = parentName.split(" ")[0];
            f2 = parentName.split(" ")[1];
        }

        String name = generateName(languageDescriptorNode);

        Value[] lastNames = languageDescriptorNode.getProperty("atlast:patronyms").getValues();

        Random r = new Random();

        int index = r.nextInt(lastNames.length);

        String lastName = lastNames[index].getString();

        lastName = lastName.replace("F1", f1);
        lastName = lastName.replace("F2", f2);

        return name + " " + lastName;
    }

    public static String generateReligionName(Node languageDescriptorNode) throws RepositoryException {

        if ("hippo:handle".equals(languageDescriptorNode.getPrimaryNodeType().getName())) {
            languageDescriptorNode = languageDescriptorNode.getNode(languageDescriptorNode.getName());
        }

        Random r = new Random();

        Value[] namePatterns = languageDescriptorNode.getProperty("atlast:religions").getValues();

        int index = r.nextInt(namePatterns.length);

        String name = namePatterns[index].getString();

        name = name.replace("${NAME}", generateName(languageDescriptorNode));

        return name;
    }

    public static String generateRaceName(Node languageDescriptorNode) throws RepositoryException {

        if ("hippo:handle".equals(languageDescriptorNode.getPrimaryNodeType().getName())) {
            languageDescriptorNode = languageDescriptorNode.getNode(languageDescriptorNode.getName());
        }

        Random r = new Random();

        Value[] namePatterns = languageDescriptorNode.getProperty("atlast:races").getValues();

        int index = r.nextInt(namePatterns.length);

        String name = namePatterns[index].getString();

        name = name.replace("${NAME}", generateName(languageDescriptorNode));

        return name;
    }
}
