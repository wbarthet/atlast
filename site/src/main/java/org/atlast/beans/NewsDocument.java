package org.atlast.beans;

import java.util.Calendar;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoDocument;
import org.hippoecm.hst.content.beans.standard.HippoGalleryImageSet;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import org.atlast.beans.Atlastimageset;

@HippoEssentialsGenerated(internalName = "atlastserver:newsdocument")
@Node(jcrType = "atlastserver:newsdocument")
public class NewsDocument extends HippoDocument {
	/** 
	 * The document type of the news document.
	 */
	public final static String DOCUMENT_TYPE = "atlastserver:newsdocument";
	private final static String TITLE = "atlastserver:title";
	private final static String DATE = "atlastserver:date";
	private final static String INTRODUCTION = "atlastserver:introduction";
	private final static String IMAGE = "atlastserver:image";
	private final static String CONTENT = "atlastserver:content";
	private final static String LOCATION = "atlastserver:location";
	private final static String AUTHOR = "atlastserver:author";
	private final static String SOURCE = "atlastserver:source";

	/** 
	 * Get the title of the document.
	 * @return the title
	 */
	@HippoEssentialsGenerated(internalName = "atlastserver:title")
	public String getTitle() {
		return getProperty(TITLE);
	}

	/** 
	 * Get the date of the document.
	 * @return the date
	 */
	@HippoEssentialsGenerated(internalName = "atlastserver:date")
	public Calendar getDate() {
		return getProperty(DATE);
	}

	/** 
	 * Get the introduction of the document.
	 * @return the introduction
	 */
	@HippoEssentialsGenerated(internalName = "atlastserver:introduction")
	public String getIntroduction() {
		return getProperty(INTRODUCTION);
	}

	/** 
	 * Get the main content of the document.
	 * @return the content
	 */
	@HippoEssentialsGenerated(internalName = "atlastserver:content")
	public HippoHtml getContent() {
		return getHippoHtml(CONTENT);
	}

	/** 
	 * Get the location of the document.
	 * @return the location
	 */
	@HippoEssentialsGenerated(internalName = "atlastserver:location")
	public String getLocation() {
		return getProperty(LOCATION);
	}

	/** 
	 * Get the author of the document.
	 * @return the author
	 */
	@HippoEssentialsGenerated(internalName = "atlastserver:author")
	public String getAuthor() {
		return getProperty(AUTHOR);
	}

	/** 
	 * Get the source of the document.
	 * @return the source
	 */
	@HippoEssentialsGenerated(internalName = "atlastserver:source")
	public String getSource() {
		return getProperty(SOURCE);
	}

	@HippoEssentialsGenerated(internalName = "atlastserver:image")
	public Atlastimageset getImage() {
		return getLinkedBean("atlastserver:image", Atlastimageset.class);
	}
}
