package org.atlast.components.player;

import org.atlast.beans.Player;
import org.atlast.components.world.BaseSecuredComponent;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by wbarthet on 7/13/15.
 */
public class FinancialOverview extends BaseSecuredComponent {

    private static Logger log = LoggerFactory.getLogger(FinancialOverview.class);

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {

        super.doBeforeRender(request, response);

        HippoBean hippoBean = request.getRequestContext().getContentBean();

        if (!(hippoBean instanceof Player)) {
            hippoBean = hippoBean.getBean(getComponentParameter("userName"));
        }

        request.setAttribute("player", hippoBean);


    }
}
