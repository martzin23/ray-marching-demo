package ray_marching_demo.util;

import java.util.Random;

public class Noise {

	public static double random(double x) {
		int seed = (int) (956334 * x);
		Random rng = new Random(seed);
		return rng.nextDouble();
	}

	public static double random(double x, double y) {
		int seed = (int) (329472 * x) ^ (int) (247631 * y);
		Random rng = new Random(seed);
		return rng.nextDouble();
	}

	public static double random(double x, double y, double z) {
		int seed = (int) (423634 * x) ^ (int) (345542 * y) ^ (int) (567546 * z);
		Random rng = new Random(seed);
		return rng.nextDouble();
	}

	public static double random(Vector v) {
		return random(v.x, v.y, v.z);
	}

	public static Vector randomVec(double seed, double min, double max) {
		Vector r = new Vector();
		r.x = Noise.random(seed + 1) * (max - min) + min;
		r.y = Noise.random(seed + 2) * (max - min) + min;
		r.z = Noise.random(seed + 3) * (max - min) + min;
		return r;
	}
	
	public static Vector randomDir(double seed) { 
		return randomVec(seed, -1, 1).norm();
	}
	
	public static Vector blurVec(Vector v, double seed, double strength) {
		return v.add(randomDir(seed).scale(strength));
	}
	
	private static double smoothStep(double t, double xmin, double xmax) {
		return (3 * t * t - 2 * t * t * t) * (xmax - xmin) + xmin;
	}

	private static double fract(double a) {
		return (a % 1 + 1) % 1;
	}

	public static double simplex2D(Vector v, double size) {
		v = v.scale(size);
		Vector f = new Vector(v.x - fract(v.x), v.y - fract(v.y), 0);
		double du = random(f.x, f.y);
		double uu = random(f.x + 1, f.y);
		double dd = random(f.x, f.y + 1);
		double ud = random(f.x + 1, f.y + 1);
		double line1 = smoothStep(fract(v.x), dd, ud);
		double line2 = smoothStep(fract(v.x), du, uu);
		return smoothStep(fract(v.y), line2, line1);
	}

	public static double simplex2D(Vector v, double size, int octave) {
		double sum = 0, scaleFactor = 1, strengthFactor = 1;
		for (int i = 0; i < octave; i++) {
			sum += simplex2D(v, size * scaleFactor) * strengthFactor;
			scaleFactor *= 0.5;
			strengthFactor *= 0.5;
		}
		return sum / 2 * (1 + 1.0 / octave);
	}

	public static double simplex3D(Vector v, double size) {
		v = v.scale(size);
		Vector f = new Vector(v.x - fract(v.x), v.y - fract(v.y), v.z - fract(v.z));
		double ddd = random(f.x, f.y, f.z);
		double ddu = random(f.x, f.y, f.z + 1);
		double dud = random(f.x, f.y + 1, f.z);
		double duu = random(f.x, f.y + 1, f.z + 1);
		double udd = random(f.x + 1, f.y, f.z);
		double udu = random(f.x + 1, f.y, f.z + 1);
		double uud = random(f.x + 1, f.y + 1, f.z);
		double uuu = random(f.x + 1, f.y + 1, f.z + 1);
		double lineU1 = smoothStep(fract(v.x), ddu, udu);
		double lineU2 = smoothStep(fract(v.x), duu, uuu);
		double layerU = smoothStep(fract(v.y), lineU1, lineU2);
		double lineD1 = smoothStep(fract(v.x), ddd, udd);
		double lineD2 = smoothStep(fract(v.x), dud, uud);
		double layerD = smoothStep(fract(v.y), lineD1, lineD2);
		return smoothStep(fract(v.z), layerD, layerU);
	}

	public static double simplex3D(Vector v, double size, int octave) {
		double sum = 0, scaleFactor = 1, strengthFactor = 1;
		for (int i = 0; i < octave; i++) {
			sum += simplex3D(v.add(new Vector(random(i), random(i + 1), random(i + 2))), size * scaleFactor) * strengthFactor;
			scaleFactor *= 2;
			strengthFactor *= 0.5;
		}
		return sum / 2 * (1 + 1.0 / octave);
	}

	public static double perlin3D(Vector v, double size) {
		v = v.scale(size);
		Vector f = new Vector(v.x - fract(v.x), v.y - fract(v.y), v.z - fract(v.z));
		double ddd = perlinValue(v, f.x, f.y, f.z);
		double ddu = perlinValue(v, f.x, f.y, f.z + 1);
		double dud = perlinValue(v, f.x, f.y + 1, f.z);
		double duu = perlinValue(v, f.x, f.y + 1, f.z + 1);
		double udd = perlinValue(v, f.x + 1, f.y, f.z);
		double udu = perlinValue(v, f.x + 1, f.y, f.z + 1);
		double uud = perlinValue(v, f.x + 1, f.y + 1, f.z);
		double uuu = perlinValue(v, f.x + 1, f.y + 1, f.z + 1);
		double lineU1 = smoothStep(fract(v.x), ddu, udu);
		double lineU2 = smoothStep(fract(v.x), duu, uuu);
		double layerU = smoothStep(fract(v.y), lineU1, lineU2);
		double lineD1 = smoothStep(fract(v.x), ddd, udd);
		double lineD2 = smoothStep(fract(v.x), dud, uud);
		double layerD = smoothStep(fract(v.y), lineD1, lineD2);
		return smoothStep(fract(v.z), layerD, layerU);
	}

	private static double perlinValue(Vector v, double x, double y, double z) {
		Vector corner = new Vector(x, y, z);
		Vector rand = randomDir(random(corner));
		Vector pos = Vector.sub(v, corner);
		return Vector.dot(rand, pos) / 2 + 0.5;
	}

	public static double perlin3D(Vector v, double size, int octave) {
		double sum = 0, scaleFactor = 1, strengthFactor = 1;
		for (int i = 0; i < octave; i++) {
			sum += perlin3D(v, size * scaleFactor) * strengthFactor;
			scaleFactor *= 2;
			strengthFactor *= 0.5;
		}
		return sum / 2 * (1 + 1.0 / octave);
	}

	public static double white3D(Vector v, double size) {
		v = v.scale(size);
		return random(v.x - fract(v.x), v.y - fract(v.y), v.z - fract(v.z));
	}

}
