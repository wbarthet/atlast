package org.atlast.beans.descriptors;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;

/**
 * Created by wbarthet on 7/17/15.
 */
@Node(jcrType = "atlast:amount")
public class Amount extends HippoCompound {

    public ResourceDescriptor getResourceDescriptor() {
        return getLinkedBean("atlast:resource", ResourceDescriptor.class);
    }

    public Long getQuantity() {
        return getProperty("atlast:quantity");
    }
}
