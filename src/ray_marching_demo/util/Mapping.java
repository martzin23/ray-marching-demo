package ray_marching_demo.util;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class Mapping {

	public static int toHex(double r, double g, double b) {
		return ((int) (Vector.clamp(r, 0, 1) * 255) << 16) | ((int) (Vector.clamp(g, 0, 1) * 255) << 8) | ((int) (Vector.clamp(b, 0, 1) * 255));
	}

	public static int toHex(Vector v) {
		return toHex(v.x, v.y, v.z);
	}

	public static int toHex(double s) {
		return toHex(s, s, s);
	}

	public static Vector toVec(int hex) {
		return new Vector(((hex & 0xff0000) >> 16) / 255.0, ((hex & 0x00ff00) >> 8) / 255.0, (hex & 0x0000ff) / 255.0);
	}

	public static double toVal(int hex) {
		return (((hex & 0xff0000) >> 16) / 255.0 + ((hex & 0x00ff00) >> 8) / 255.0 + (hex & 0x0000ff) / 255.0) / 3;
	}

	public static int toFilmicHex(Vector v) {
		return toHex(new Vector(luminosityTransform(v.x), luminosityTransform(v.y), luminosityTransform(v.z)));
	}

	public static int toFilmicHex(double s) {
		return toHex(new Vector(luminosityTransform(s)));
	}
	
	private static double luminosityTransform(double x) {
		return 1 - Math.pow(Math.E, -1.7*x);
	}

	public static void resizeImage(BufferedImage input, BufferedImage output, double scale) throws IOException {
		for (int x = 0; x < input.getWidth(); x++)
			for (int y = 0; y < input.getHeight(); y++)
				output.setRGB(x, y, input.getRGB((int) (x / scale), (int) (y / scale)));
		return;
	}

	public static int evaluateUV(BufferedImage img, double u, double v) throws IOException {
		return img.getRGB((int) (Vector.clamp(u, 0, 1) * (img.getWidth() - 1)), (int) (Vector.clamp(v, 0, 1) * (img.getHeight() - 1)));
	}

	public static int mapTextureBasic(BufferedImage img, Vector pos, double scale) throws IOException {
		double u = Math.abs(pos.x / scale * ((double) img.getHeight() / img.getWidth())) % 1.0;
		double v = Math.abs(-pos.y / scale) % 1.0;
		return evaluateUV(img, u, v);
	}

	public static int mapTextureTriplanarSmooth(BufferedImage img, Vector pos, Vector normal, double scale) throws IOException {
		Vector colorX, colorY, colorZ, res;
		double u, v;
		u = Math.abs(pos.y / scale * ((double) img.getHeight() / img.getWidth())) % 1.0;
		v = Math.abs(-pos.z / scale) % 1.0;
		colorX = toVec(evaluateUV(img, u, v));
		u = Math.abs(pos.x / scale * ((double) img.getHeight() / img.getWidth())) % 1.0;
		v = Math.abs(-pos.z / scale) % 1.0;
		colorY = toVec(evaluateUV(img, u, v));
		u = Math.abs(pos.x / scale * ((double) img.getHeight() / img.getWidth())) % 1.0;
		v = Math.abs(-pos.y / scale) % 1.0;
		colorZ = toVec(evaluateUV(img, u, v));
		res = Vector.lerp(Math.abs(normal.x), colorY, colorX);
		res = Vector.lerp(Math.abs(normal.z), res, colorZ);
		return toHex(res);
	}

	public static int mapTextureTriplanarSharp(BufferedImage img, Vector pos, Vector normal, double scale) throws IOException {
		Vector colorX, colorY, colorZ, res;
		double u, v;
		u = Math.abs(pos.y / scale * ((double) img.getHeight() / img.getWidth())) % 1.0;
		v = Math.abs(-pos.z / scale) % 1.0;
		colorX = toVec(evaluateUV(img, u, v));
		u = Math.abs(pos.x / scale * ((double) img.getHeight() / img.getWidth())) % 1.0;
		v = Math.abs(-pos.z / scale) % 1.0;
		colorY = toVec(evaluateUV(img, u, v));
		u = Math.abs(pos.x / scale * ((double) img.getHeight() / img.getWidth())) % 1.0;
		v = Math.abs(-pos.y / scale) % 1.0;
		colorZ = toVec(evaluateUV(img, u, v));
		res = Vector.lerp(Math.round(Math.abs(normal.x)), colorY, colorX);
		res = Vector.lerp(Math.round(Math.abs(normal.z)), res, colorZ);
		return toHex(res);
	}

	public static int mapEnvironment(BufferedImage img, Vector dir) throws IOException {
		double u = dir.y < 0 ? Math.acos(-dir.x / 2 - 0.5) / Math.PI : (Math.PI - Math.acos(dir.x / 2 + 0.5)) / Math.PI;
		double v = Math.acos(dir.z) / Math.PI;
		return evaluateUV(img, u, v);
	}

	public static int mapVolume(BufferedImage img, Vector v) {
		v = v.add(0.5).scale(img.getWidth());
		if (v.x < 0 || v.y < 0 || v.z < 0) return 0x000000;
		if (v.x > img.getWidth() || v.y > img.getWidth() || v.z > img.getHeight() / img.getWidth()) return 0x000000;
		return img.getRGB((int) v.x, (int) v.y + (int) v.z * img.getWidth());
	}

	public static int mapVolumeLerp(BufferedImage img, Vector v) {
		v = v.add(0.5).scale(img.getWidth());
		if (v.x < 0 || v.y < 0 || v.z < 0) return 0x000000;
		if (v.x + 1 > img.getWidth() || v.y + 1 > img.getWidth() || v.z + 1 > img.getHeight() / img.getWidth()) return 0x000000;

		Vector f = new Vector(v.x - fract(v.x), v.y - fract(v.y), v.z - fract(v.z));
		Vector ddd = toVec(img.getRGB((int) f.x, (int) f.y + (int) f.z * img.getWidth()));
		Vector ddu = toVec(img.getRGB((int) f.x, (int) f.y + ((int) f.z + 1) * img.getWidth()));
		Vector dud = toVec(img.getRGB((int) f.x, (int) f.y + 1 + (int) f.z * img.getWidth()));
		Vector duu = toVec(img.getRGB((int) f.x, (int) f.y + 1 + ((int) f.z + 1) * img.getWidth()));
		Vector udd = toVec(img.getRGB((int) f.x + 1, (int) f.y + (int) f.z * img.getWidth()));
		Vector udu = toVec(img.getRGB((int) f.x + 1, (int) f.y + ((int) f.z + 1) * img.getWidth()));
		Vector uud = toVec(img.getRGB((int) f.x + 1, (int) f.y + 1 + (int) f.z * img.getWidth()));
		Vector uuu = toVec(img.getRGB((int) f.x + 1, (int) f.y + 1 + ((int) f.z + 1) * img.getWidth()));
		Vector lineU1 = Vector.lerp(fract(v.x), ddu, udu);
		Vector lineU2 = Vector.lerp(fract(v.x), duu, uuu);
		Vector layerU = Vector.lerp(fract(v.y), lineU1, lineU2);
		Vector lineD1 = Vector.lerp(fract(v.x), ddd, udd);
		Vector lineD2 = Vector.lerp(fract(v.x), dud, uud);
		Vector layerD = Vector.lerp(fract(v.y), lineD1, lineD2);
		return toHex(Vector.lerp(fract(v.z), layerD, layerU));
	}

	private static double fract(double a) {
		return (a % 1 + 1) % 1;
	}
}
