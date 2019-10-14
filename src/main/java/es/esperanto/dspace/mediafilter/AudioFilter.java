/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package es.esperanto.dspace.mediafilter;

import java.io.InputStream;

import org.dspace.app.mediafilter.JPEGFilter;
import org.dspace.app.mediafilter.MediaFilter;
import org.dspace.content.Item;

/**
 * Filter image bitstreams, scaling the image to be within the bounds of
 * thumbnail.maxwidth, thumbnail.maxheight, the size we want our thumbnail to be
 * no bigger than. Creates only JPEGs.
 *
 * @author Jason Sherman <jsherman@usao.edu>
 */
public class AudioFilter extends MediaFilter {

	public String getFilteredName(String oldFilename) {

		String filteredName = oldFilename + ".jpg";
		return filteredName;
	}

	/**
	 * @return String bundle name
	 * 
	 */
	public String getBundleName() {
		return "THUMBNAIL";
	}

	/**
	 * @return String bitstreamformat
	 */
	public String getFormatString() {
		return "JPEG";
	}

	/**
	 * @return String description
	 */
	public String getDescription() {
		return "Thumbnail estatico (audio)";
	}

	public InputStream getDestinationStream(Item item, InputStream source, boolean verbose) throws Exception {
		try {
			InputStream aud = ClassLoader.getSystemResourceAsStream("audio.png");

			return new JPEGFilter().getDestinationStream(item, aud, verbose);

		} catch (Exception e) {
			System.out.println("Error");
			e.printStackTrace();
			return null;
		}
	}

}
