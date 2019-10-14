/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package es.esperanto.dspace.mediafilter;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.dspace.app.mediafilter.MediaFilter;
import org.dspace.content.Item;

/**
 * Filtra los epub y extrae el texto.
 * 
 * @author jtrenado
 */
public class EpubTextFilter extends MediaFilter {

	public String getFilteredName(String oldFilename) {

		String filteredName = oldFilename + ".txt";
		return filteredName;
	}

	/**
	 * @return String bundle name
	 * 
	 */
	public String getBundleName() {
		return "TEXT";
	}

	/**
	 * @return String bitstreamformat
	 */
	public String getFormatString() {
		return "Text";
	}

	/**
	 * @return String description
	 */
	public String getDescription() {
		return "HTML extraido";
	}

	public InputStream getDestinationStream(Item item, InputStream source, boolean verbose) throws Exception {
		try {
			String texto = ConversorEPUB.obtenerTexto(source);

			byte[] b = texto.getBytes(Charset.forName("UTF-8"));

			return new ByteArrayInputStream(b);

		} catch (Exception e) {
			System.out.println("Error");
			e.printStackTrace();
			return null;
		}
	}

}
