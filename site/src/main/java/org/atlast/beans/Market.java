package org.atlast.beans;

import java.util.List;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoItem;

/**
 * Created by wbarthet on 7/13/15.
 */
@Node(jcrType = "atlast:market")
public class Market extends AtlastObject {

    public Store getStore(String name) {
        return getBean(name);
    }

    public List<Store> getStores() {
        return getChildBeans(Store.class);
    }

}
