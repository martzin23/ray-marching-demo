package ray_marching_demo.shader;

import java.io.IOException;

import ray_marching_demo.body.Bodies;
import ray_marching_demo.util.Mapping;
import ray_marching_demo.util.Ray;
import ray_marching_demo.util.Vector;

public class ShaderNormalRayTrace extends Shader {

	@Override
	public int getRGB(double u, double v) throws IOException {
		Ray ray = Bodies.rayTrace(maxDistance, jumpDistance, Bodies.getCam().getCameraRay(u, v));

		if (!ray.hit) return 0x000000;

		Vector normal = Bodies.getNormal(ray.pos.sub(ray.dir.scale(20 * Shader.jumpDistance * epsilonStep)), ray.hitIndex, epsilonNormal);
		return Mapping.toHex(Vector.scale(Vector.add(Vector.norm(normal), 1), 0.5));
	}

}
