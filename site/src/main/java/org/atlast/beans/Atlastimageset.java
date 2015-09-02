package org.atlast.beans;

import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoGalleryImageSet;
import org.hippoecm.hst.content.beans.standard.HippoGalleryImageBean;

@HippoEssentialsGenerated(internalName = "atlast:atlastimageset")
@Node(jcrType = "atlast:atlastimageset")
public class Atlastimageset extends HippoGalleryImageSet {
	@HippoEssentialsGenerated(internalName = "atlast:mediumicon")
	public HippoGalleryImageBean getMediumicon() {
		return getBean("atlast:mediumicon", HippoGalleryImageBean.class);
	}

	@HippoEssentialsGenerated(internalName = "atlast:smallicon")
	public HippoGalleryImageBean getSmallicon() {
		return getBean("atlast:smallicon", HippoGalleryImageBean.class);
	}
}
