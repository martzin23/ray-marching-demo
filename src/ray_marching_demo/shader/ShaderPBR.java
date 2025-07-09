package ray_marching_demo.shader;

import java.io.IOException;

import ray_marching_demo.body.Bodies;
import ray_marching_demo.util.Mapping;
import ray_marching_demo.util.Noise;
import ray_marching_demo.util.Ray;
import ray_marching_demo.util.Vector;

public class ShaderPBR extends Shader {

	@Override
	public int getRGB(double u, double v) throws IOException {
		Vector directLight;
		Vector indirectLight = new Vector();
		
		Ray cameraRay = Bodies.rayIntersect(Bodies.getCam().getCameraRay(u, v));
		if (!cameraRay.hit) return 0x000000;
		cameraRay.calculateNormal(epsilonNormal);
		directLight = Vector.add(cameraRay.getEmission(), evaluateLightingPBR(cameraRay));

		for (int sampleCounter = 1; sampleCounter < maxSamples; sampleCounter++) {
			Ray indirectRay = new Ray(cameraRay);
			for (int bounceCounter = 0; bounceCounter < maxBounces; bounceCounter++) {

				Vector reflectionDir = Vector.sub(indirectRay.dir, Vector.scale(indirectRay.normal, 2.0 * Vector.dot(indirectRay.normal, indirectRay.dir)));
				indirectRay.dir = Noise.randomDir(Noise.random(u, v) + bounceCounter + sampleCounter);
				if (Vector.dot(indirectRay.dir, indirectRay.normal) < 0) indirectRay.dir = indirectRay.dir.scale(-1);
				if (indirectRay.getMetallic() > 0.5) indirectRay.dir = Vector.lerp(indirectRay.getRoughness(), reflectionDir, indirectRay.dir).norm();
				indirectRay = Bodies.rayIntersect(new Ray(Vector.add(indirectRay.pos, Vector.scale(indirectRay.normal, epsilonStep)), indirectRay.dir));

				if (!indirectRay.hit) break;

				indirectRay.calculateNormal(epsilonNormal);
				indirectLight = indirectLight.add(evaluateLightingPBR(indirectRay));
			}
		}
		indirectLight = indirectLight.mul(cameraRay.getColor()).scale(1.0 / maxSamples);
		return Mapping.toFilmicHex(Vector.add(directLight, indirectLight));
	}
	
}
