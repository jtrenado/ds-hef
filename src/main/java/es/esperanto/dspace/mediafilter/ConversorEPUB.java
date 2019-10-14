package es.esperanto.dspace.mediafilter;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubReader;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;

public class ConversorEPUB {

	public static String obtenerTexto(InputStream is) {
		try {

			EpubReader reader = new EpubReader();

			Book book = reader.readEpub(is);

			StringBuilder sb = new StringBuilder();
			List<Resource> secciones = book.getContents();
			for (Resource res : secciones) {
				sb.append(new String(res.getData(), res.getInputEncoding()));
			}

			String texto = cleanTagPerservingLineBreaks(sb.toString());
			texto = StringEscapeUtils.unescapeHtml4(texto);

			return texto;

		} catch (Throwable e) {
			return null;
		} finally {

		}
	}

	public static String cleanTagPerservingLineBreaks(String html) {
		String result = "";
		if (html == null)
			return html;
		Document document = Jsoup.parse(html);
		document.outputSettings(new Document.OutputSettings()
				.prettyPrint(false));// makes html() preserve linebreaks and
										// spacing
		document.select("br").append("\\n");
		document.select("p").prepend("\\n\\n");
		result = document.html().replaceAll("\\\\n", "\n");
		result = Jsoup.clean(result, "", Whitelist.none(),
				new Document.OutputSettings().prettyPrint(false));
		return result;
	}

	public static BufferedImage convertir(InputStream is, int segundo)
			throws IOException {

		try {

			EpubReader reader = new EpubReader();

			Book book = reader.readEpub(is);

			Resource cover = book.getCoverImage();
			InputStream isCover = cover.getInputStream();

			return ImageIO.read(isCover);

		} catch (Throwable e) {
			return null;
		} finally {

		}

	}

	/**
	 * Prueba de la clase que convierte de Video a JPG
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws Throwable {

		int pagina = 1;

		File file = new File(args[0]);
		FileInputStream fis = new FileInputStream(file);

		BufferedImage image = convertir(fis, pagina);

		ImageIO.write(image, "png", new File(args[1]));

		File file2 = new File(args[0]);
		FileInputStream fis2 = new FileInputStream(file2);

		System.out.print(obtenerTexto(fis2));

	}
}
