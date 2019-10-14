package es.esperanto.dspace.mediafilter;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.imageio.ImageIO;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;

public class ConversorVID {

	public static BufferedImage convertir(InputStream is, int segundo) throws IOException {

		FileOutputStream fos = null;
		String tmp = null;

		try {

			// Vamos a grabar el stream en un fichero temporal
			tmp = System.getProperty("java.io.tmpdir") + "/" + ConversorVID.class.getCanonicalName()
					+ (new Date().getTime()) + ".tmp";

			fos = new FileOutputStream(tmp);
			while (is.available() > 0) {
				byte[] b = new byte[1024];
				int n = is.read(b);
				fos.write(b, 0, n);
			}
			fos.close();
			fos = null;

			FFmpegFrameGrabber g = new FFmpegFrameGrabber(new File(tmp));

			g.start();

			int frameRate = (int) g.getFrameRate();

			int totalFrame = frameRate * segundo;

			Java2DFrameConverter fc = new Java2DFrameConverter();

			Frame frame = null;
			int i = 0;

			while (i < totalFrame) {

				frame = g.grabFrame(false, true, true, false);
				if (frame.imageHeight > 0) {
					i++;
				}

			}

			BufferedImage image = fc.getBufferedImage(frame, 2.);

			g.stop();

			return image;

		} catch (FrameGrabber.Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (fos != null) {
				fos.close();
			}

			File temporal = new File(tmp);
			if (temporal.exists()) {
				temporal.delete();
			}

		}

	}

	/**
	 * Prueba de la clase que convierte de Video a JPG
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws Throwable {

		int segundo = 50;

		File file = new File(args[0]);
		FileInputStream fis = new FileInputStream(file);
		System.setProperty("java.io.tmpdir", "c:/workspace");

		BufferedImage image = convertir(fis, segundo);

		ImageIO.write(image, "png", new File(args[1]));

	}
}
