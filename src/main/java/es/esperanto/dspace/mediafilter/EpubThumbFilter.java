/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package es.esperanto.dspace.mediafilter;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.dspace.app.mediafilter.JPEGFilter;
import org.dspace.app.mediafilter.MediaFilter;
import org.dspace.content.Item;

/**
 *
 * Filtra los epub y convierte la cubierta en una imagen. Luego utiliza el
 * filtro JPEFFilter para redimensionar la imagen segun los parametros de DSpace
 *
 *
 * @author jtrenado
 */
public class EpubThumbFilter extends MediaFilter {

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
		return "Thumbnail EPUB";
	}

	public InputStream getDestinationStream(Item item, InputStream source, boolean verbose) throws Exception {

		try {
			BufferedImage buf = ConversorEPUB.convertir(source, 1);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			ImageIO.write(buf, "jpeg", baos);

			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

			return new JPEGFilter().getDestinationStream(item, bais, verbose);

		} catch (Exception e) {
			System.out.println("Error");
			e.printStackTrace();
			return null;
		}

	}

}
