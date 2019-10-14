package es.esperanto.dspace.mediafilter;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.jpedal.PdfDecoder;
import org.jpedal.exception.PdfException;
import org.jpedal.fonts.FontMappings;

public class ConversorPDF {

	/**
	 * Recibe un PDF en un {@link InputStream} y un número de página.
	 * 
	 * @param is
	 *            {@link InputStream}
	 * @param pagina
	 *            Número de página
	 * @return {@link BufferedImage} con la imagen de la página del PDF indicada
	 * @throws IOException
	 *             Hubo un error de IO
	 */
	public static BufferedImage convertir(InputStream is, int pagina) throws IOException {

		List<BufferedImage> res = convertir(is, pagina, pagina);
		if (res.size() == 1) {
			return res.get(0);
		} else {
			return null;
		}
	}

	/**
	 * Recibe un PDF en un {@link InputStream} y un número de página inicial y
	 * final.
	 * 
	 * @param is
	 *            {@link InputStream}
	 * @param paginaInicial
	 *            Primera página a convertir
	 * @param paginaFinal
	 *            Última página a convertir
	 * @return Lista de {@link BufferedImage} con las imagenes correspondientes
	 * @throws IOException
	 *             Hubo un error de IO
	 */
	public static List<BufferedImage> convertir(InputStream is, int paginaInicio, int paginaFinal) throws IOException {

		System.setProperty("org.jpedal.jai", "true");

		List<BufferedImage> res = new ArrayList<BufferedImage>();

		/** instance of PdfDecoder to convert PDF into image */
		PdfDecoder decode_pdf = new PdfDecoder(true);

		/** set mappings for non-embedded fonts to use */
		FontMappings.setFontReplacements();

		/** open the PDF file - can also be a URL or a byte array */
		try {
			decode_pdf.openPdfFileFromInputStream(is, false); // file

			for (int i = paginaInicio; i <= paginaFinal; i++) {
				BufferedImage img = decode_pdf.getPageAsImage(i);
				res.add(img);
			}

			/** close the pdf file */
			decode_pdf.closePdfFile();

		} catch (PdfException e) {
			e.printStackTrace();
		}

		return res;
	}

	/**
	 * Prueba de la clase que convierte de PDF a JPG
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			FileInputStream is = new FileInputStream(new File(args[0]));
			BufferedImage nuevoIS = convertir(is, 1);
			FileOutputStream fos = new FileOutputStream(new File(args[1]));

			ImageIO.write(nuevoIS, "jpeg", fos);

		} catch (IOException e) {

		}

	}

}
