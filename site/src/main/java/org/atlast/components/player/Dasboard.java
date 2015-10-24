package org.atlast.components.player;

import java.io.IOException;

import org.atlast.beans.Player;
import org.atlast.components.world.BaseSecuredComponent;
import org.hippoecm.hst.component.support.bean.BaseHstComponent;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by wbarthet on 7/13/15.
 */
public class Dasboard extends BaseHstComponent {

    private static Logger log = LoggerFactory.getLogger(Dasboard.class);

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) throws HstComponentException {

        super.doBeforeRender(request, response);

        HippoBean hippoBean = request.getRequestContext().getContentBean();

        if (hippoBean == null) {
            response.setStatus(404);
            try {
                response.forward("/pagenotfound");
            } catch (IOException e) {
                log.error("Error redirecting to 404 page", e);
            }
            return;
        }

        if (!(hippoBean instanceof Player)) {
            hippoBean = hippoBean.getBean(getComponentParameter("userName"));
        }

        request.setAttribute("player", hippoBean);
        request.setAttribute("loggedinuser", request.getRequestContext().getServletRequest().getUserPrincipal());


    }
}
