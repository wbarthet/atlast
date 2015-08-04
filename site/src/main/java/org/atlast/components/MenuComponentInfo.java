package org.atlast.components;

import org.hippoecm.hst.core.parameters.Parameter;
import org.onehippo.cms7.essentials.components.info.EssentialsMenuComponentInfo;

/**
 * Created by wbarthet on 7/16/15.
 */
public interface MenuComponentInfo extends EssentialsMenuComponentInfo {

    @Parameter(name = "secureMenu", defaultValue = "secure", displayName = "Secure Menu")
    String getSecureMenu();

}
