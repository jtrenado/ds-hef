package es.esperanto.dspace.mediafilter;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.poi.POIXMLException;
import org.apache.poi.hslf.usermodel.HSLFSlideShow;
import org.apache.poi.sl.usermodel.Slide;
import org.apache.poi.sl.usermodel.SlideShow;
import org.apache.poi.xslf.usermodel.XMLSlideShow;

public class ConversorPPT {

	public static BufferedImage convertir(InputStream is, int pagina)
			throws IOException {

		@SuppressWarnings("rawtypes")
		SlideShow ppt = null;
		FileInputStream fis = null;
		FileOutputStream fos = null;
		String tmp = null;

		try {

			// Vamos a grabar el stream en un fichero temporal
			tmp = System.getProperty("java.io.tmpdir") + "/"
					+ ConversorPPT.class.getCanonicalName()
					+ (new Date().getTime()) + ".tmp";

			fos = new FileOutputStream(tmp);
			while (is.available() > 0) {
				byte[] b = new byte[1024];
				int n = is.read(b);
				fos.write(b, 0, n);
			}
			fos.close();
			fos = null;

			try {
				fis = new FileInputStream(new File(tmp));
				ppt = new XMLSlideShow(fis);

			} catch (POIXMLException e) {

				if (fis != null) {
					fis.close();
				}

				fis = new FileInputStream(new File(tmp));
				ppt = new HSLFSlideShow(fis);

			}

			return convertir(ppt, pagina);

		} catch (IOException e) {
			return null;
		} finally {
			if (fos != null) {
				fos.close();
			}

			if (fis != null) {
				fis.close();
			}

			File temporal = new File(tmp);
			if (temporal.exists()) {
				temporal.delete();
			}

		}

	}

	private static BufferedImage convertir(
			@SuppressWarnings("rawtypes") SlideShow ppt, int pagina) {

		Dimension pgsize = ppt.getPageSize();

		@SuppressWarnings("rawtypes")
		Slide slide = buscaSlide(ppt, pagina);

		BufferedImage img = new BufferedImage(pgsize.width, pgsize.height, 1);

		Graphics2D graphics = img.createGraphics();
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
				RenderingHints.VALUE_FRACTIONALMETRICS_ON);

		graphics.setColor(Color.white);
		graphics.clearRect(0, 0, pgsize.width, pgsize.height);
		graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));

		// render
		slide.draw(graphics);

		return img;
	}

	@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
	private static Slide buscaSlide(SlideShow ppt, int pagina) {
		List<Slide> slides = ppt.getSlides();
		Slide slide = null;
		for (Slide s : slides) {
			if (s.getSlideNumber() == pagina) {
				return s;
			}
		}
		return null;
	}

	/**
	 * Prueba de la clase que convierte de PPT a png
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws Throwable {

		int pagina = 1;

		File file = new File(args[0]);
		FileInputStream fis = new FileInputStream(file);
		System.setProperty("java.io.tmpdir", "c:/workspace");

		BufferedImage image = convertir(fis, pagina);

		ImageIO.write(image, "png", new File(args[1]));

	}
}
