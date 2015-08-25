package org.atlast.beans;

import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.hippoecm.hst.content.beans.standard.HippoGalleryImageSet;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.atlast.beans.Atlastimageset;

@HippoEssentialsGenerated(internalName = "atlastserver:bannerdocument")
@Node(jcrType = "atlastserver:bannerdocument")
public class Banner extends BaseDocument {
	@HippoEssentialsGenerated(internalName = "atlastserver:title")
	public String getTitle() {
		return getProperty("atlastserver:title");
	}

	@HippoEssentialsGenerated(internalName = "atlastserver:content")
	public HippoHtml getContent() {
		return getHippoHtml("atlastserver:content");
	}

	@HippoEssentialsGenerated(internalName = "atlastserver:link")
	public HippoBean getLink() {
		return getLinkedBean("atlastserver:link", HippoBean.class);
	}

	@HippoEssentialsGenerated(internalName = "atlastserver:image")
	public Atlastimageset getImage() {
		return getLinkedBean("atlastserver:image", Atlastimageset.class);
	}
}
