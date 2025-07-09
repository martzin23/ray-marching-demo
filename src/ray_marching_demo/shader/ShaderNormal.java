package ray_marching_demo.shader;

import java.io.IOException;

import ray_marching_demo.body.Bodies;
import ray_marching_demo.util.Mapping;
import ray_marching_demo.util.Ray;
import ray_marching_demo.util.Vector;

public class ShaderNormal extends Shader {

	@Override
	public int getRGB(double u, double v) throws IOException {
		Ray ray = Bodies.rayMarch(maxMarches, maxDistance, marchPrecision, Bodies.getCam().getCameraRay(u, v));

		if (!ray.hit) return 0x000000;
		
		Vector normal = Bodies.getNormal(ray.pos, ray.hitIndex, epsilonNormal);
		double ao = Vector.lerp(ray.jumpCount, 0, maxMarches * marchPrecision, 1, 0);
		
		return Mapping.toHex(Vector.scale(Vector.add(Vector.norm(normal), 1), 0.5 * ao));
	}

}
