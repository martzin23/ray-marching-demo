package ray_marching_demo.shader;

import java.io.IOException;

import ray_marching_demo.body.Bodies;
import ray_marching_demo.util.Images;
import ray_marching_demo.util.Mapping;
import ray_marching_demo.util.Ray;
import ray_marching_demo.util.Vector;

public class ShaderVolume extends Shader {

	@SuppressWarnings("unused")
	@Override
	public int getRGB(double u, double v) throws IOException {
		Ray ray = Bodies.getCam().getCameraRay(u, v);
		Vector color = new Vector(0, 0, 0);
		double density = 0.02;
		double scale = 5;

		// XXX has a bunch of temporary stuff
		while (ray.dist < maxDistance) {
			if ((Math.signum(Math.sqrt(ray.pos.x * ray.pos.x + ray.pos.y * ray.pos.y + ray.pos.z * ray.pos.z) - 50) / -2 + 0.5) != 0) {
				double x = ray.pos.x / scale;
				double y = ray.pos.y / scale;
				double z = ray.pos.z / scale;
//				
				// To radial coordiante system
//				double temp = y;
//				y = Math.atan(y / x);
//				x = Math.sqrt(x * x + temp * temp);
//
				// Volume function
//				double value = intersection(x * x + y * y - 1, -x * x - y * y + 4, y - x, x * Math.sqrt(3) - y, z, 1 - z);
//				double value = intersection(y - x * x, -y + 2 * x, z, 2 * x + y - z); // 4.80
//				double value = intersection(y, Math.sqrt(2 * x) - y, 4 - x - y, z, x + y - z); // 11.86
//				double value = intersection(-x * x + y, y - 1, 4 - y, z, y - z); // 24.80
//				double value = intersection(y, x, z, 4 - 2 * x - y - z); // 5.33
//				double value = (x * x * (Math.sin(x) + y) / (Math.sin(y) + x)) - z * y * y;
//				double value = 3 * x * x * y - 2 * x * y + 5 * x - 3 * y - z;
//				value *= Math.round((z / 2 % 1 + 1) % 1 - 0.25); // split volume to layers
//				value += fix(-Math.sqrt(y * y + z * z) + 0.1) + fix(-Math.sqrt(x * x + z * z) + 0.1) + fix(-Math.sqrt(x * x + y * y) + 0.1);
//				color = color.add(fix(value) * density * jumpDistance / 0.4);
				
				// Volume image
				color = color.add(Mapping.toVec(Mapping.mapVolume(Images.volume, ray.pos.scale(1.73 / 100))).scale(density * jumpDistance / 0.4));
//				if (color.length() > 0.1) {
//					color = color.add(Vector.clamp(Vector.lerp(ray.dist, 0, 35, 0, 0.5)));
//					return Mapping.toFilmicHex(color);
//				}

			}
			ray.pos = Vector.add(ray.pos, Vector.scale(ray.dir, jumpDistance));
			ray.dist += jumpDistance;
		}
		return Mapping.toHex(color);
	}

	private static double fix(double value) {
		return value > 0 ? 1 : 0;
	}

	@SuppressWarnings("unused")
	private static double intersection(double... values) {
		double result = 1;
		for (double v : values)
			result *= fix(v);
		return result;
	}
}
