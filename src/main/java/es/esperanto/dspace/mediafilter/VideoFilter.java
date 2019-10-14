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
import org.dspace.core.ConfigurationManager;

/**
 * Filtra los videos y captura un frame en una imagen. Luego utiliza el filtro
 * JPEFFilter para redimensionar la imagen segun los parametros de DSpace
 *
 * @author jtrenado
 */
public class VideoFilter extends MediaFilter {

	private int segundo;
	private static String param = "thumbnail.video.segundo";

	public VideoFilter() {
		segundo = ConfigurationManager.getIntProperty(param, -1);
	}

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
		String desc = "Thumbnail de video (segundo " + segundo + ")";
		return desc;
	}

	public InputStream getDestinationStream(Item item, InputStream source, boolean verbose) throws Exception {
		try {
			if (segundo < 0) {
				System.out.println(
						"Hay que definir el segundo del video usado para la miniatura con el parametro " + param);
				return null;
			} else {
				BufferedImage buf = ConversorVID.convertir(source, segundo);

				ByteArrayOutputStream baos = new ByteArrayOutputStream();

				ImageIO.write(buf, "png", baos);

				ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

				return new JPEGFilter().getDestinationStream(item, bais, verbose);
			}

		} catch (Exception e) {
			System.out.println("Error");
			e.printStackTrace();
			return null;
		}

	}

}
