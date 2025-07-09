package ray_marching_demo.shader;

import java.io.IOException;

import ray_marching_demo.body.Bodies;
import ray_marching_demo.util.Mapping;
import ray_marching_demo.util.Ray;
import ray_marching_demo.util.Vector;

public class ShaderPhong extends Shader{

	@Override
	public int getRGB(double u, double v) throws IOException {
		Ray cameraRay = Bodies.rayMarch(Bodies.getCam().getCameraRay(u, v));
		if (!cameraRay.hit) return 0x000000;
		
		cameraRay.calculateNormal(epsilonNormal);
		double ao = Vector.lerp(cameraRay.jumpCount, 0, Shader.maxMarches * Shader.marchPrecision, 1, 0);
		Vector directLight = Vector.add(cameraRay.getEmission(), evaluateLightingPhong(cameraRay)).scale(ao);

		return Mapping.toFilmicHex(directLight);
	}

}
