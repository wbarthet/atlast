package org.atlast.components;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.hippoecm.hst.component.support.bean.BaseHstComponent;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by wbarthet on 7/16/15.
 */
@ParametersInfo(type = MenuComponentInfo.class)
public class Info extends BaseHstComponent {

    private static Logger log = LoggerFactory.getLogger(Info.class);

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) {
        super.doBeforeRender(request, response);

        HstRequestContext requestContext = request.getRequestContext();

        HippoBean document = requestContext.getContentBean();

        List<HippoBean> menu = new ArrayList<>();

        if (document != null) {
            if (document.isHippoFolderBean()) {

                menu = document.getChildBeans(HippoBean.class);

                document = document.getBean(document.getName());
            }
            else  {
                menu = document.getParentBean().getChildBeans(HippoBean.class);
            }
        }

        if (document == null) {
            response.setStatus(404);
            try {
                response.sendRedirect("/pagenotfound");
            } catch (IOException e) {
                log.error("Error redirecting to 404", e);
            }
            return;
        }

        request.setAttribute("document", document);
        request.setAttribute("menu", menu);

    }
}
