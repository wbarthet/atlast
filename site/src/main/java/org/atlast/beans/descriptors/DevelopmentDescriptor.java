package org.atlast.beans.descriptors;
/*
 * Copyright 2014 Hippo B.V. (http://www.onehippo.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.ArrayList;
import java.util.List;

import javax.jcr.RepositoryException;


import org.atlast.beans.Atlastimageset;
import org.atlast.beans.BaseDocument;
import org.atlast.beans.Library;
import org.atlast.beans.Player;
import org.hippoecm.hst.content.beans.Node;



@Node(jcrType="atlast:developmentdescriptor")
public class DevelopmentDescriptor extends BaseDocument {


    public String getName() {
        return getProperty("atlast:name");
    }



    public List<RecipeDescriptor> getAllowedRecipes(Player player) throws RepositoryException {
        Library library = player.getLibrary();

        List<RecipeDescriptor> allowedRecipes = new ArrayList<>();

        List<RecipeDescriptor> recipeDescriptors = getLinkedBeans("atlast:allowedrecipes", RecipeDescriptor.class);

        for (RecipeDescriptor recipeDescriptor : recipeDescriptors) {
            if (library.hasRecipe(recipeDescriptor)) {
                allowedRecipes.add(recipeDescriptor);
            }
        }

        return allowedRecipes;
    }

    public List<RecipeDescriptor> getAllAllowedRecipes() throws RepositoryException {

        List<RecipeDescriptor> allowedRecipes = new ArrayList<>();

        List<RecipeDescriptor> recipeDescriptors = getLinkedBeans("atlast:allowedrecipes", RecipeDescriptor.class);

        for (RecipeDescriptor recipeDescriptor : recipeDescriptors) {

                allowedRecipes.add(recipeDescriptor);
        }

        return allowedRecipes;
    }

    public Atlastimageset getIcon() {
        return getLinkedBean("atlast:icon", Atlastimageset.class);
    }

    public boolean isPrimary() {
        return getProperty("atlast:primary");
    }
}

