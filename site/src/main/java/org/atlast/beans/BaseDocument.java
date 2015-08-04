package org.atlast.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoDocument;

@Node(jcrType="atlastserver:basedocument")
public class BaseDocument extends HippoDocument {

    public String getUuid() {
        return getCanonicalUUID();
    }

}
