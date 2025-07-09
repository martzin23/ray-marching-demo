package ray_marching_demo.shader;

import java.io.IOException;

import ray_marching_demo.body.Bodies;
import ray_marching_demo.body.Body;
import ray_marching_demo.util.Ray;
import ray_marching_demo.util.Vector;

public abstract class Shader {

	static int maxMarches = 100;
	static double maxDistance = 100;
	
	static double jumpDistance = 0.4;
	static int marchPrecision = 1;
	
	static double epsilonNormal = 0.05; // 0.05 // 0.0001
	static double epsilonStep = 0.05;

	static int maxSamples = 1;
	static int maxBounces = 3;
	
	public Shader() {
	}
	
	public abstract int getRGB(double u, double v) throws IOException;

	public static int getMaxMarches() {
		return maxMarches;
	}

	public static void setMaxMarches(int maxMarches) {
		Shader.maxMarches = maxMarches;
	}

	public static double getMaxDistance() {
		return maxDistance;
	}

	public static void setMaxDistance(double maxDistance) {
		Shader.maxDistance = maxDistance;
	}

	public static double getJumpDistance() {
		return jumpDistance;
	}

	public static void setJumpDistance(double jumpDistance) {
		Shader.jumpDistance = jumpDistance;
	}

	public static int getMarchPrecision() {
		return marchPrecision;
	}

	public static void setMarchPrecision(int marchPrecision) {
		Shader.marchPrecision = marchPrecision;
	}

	public static double getEpsilonNormal() {
		return epsilonNormal;
	}

	public static void setEpsilonNormal(double epsilonNormal) {
		Shader.epsilonNormal = epsilonNormal;
	}

	public static double getEpsilonStep() {
		return epsilonStep;
	}

	public static void setEpsilonStep(double epsilonStep) {
		Shader.epsilonStep = epsilonStep;
	}

	public static int getMaxSamples() {
		return maxSamples;
	}

	public static void setMaxSamples(int maxSamples) {
		Shader.maxSamples = maxSamples;
	}

	public static int getMaxBounces() {
		return maxBounces;
	}

	public static void setMaxBounces(int maxBounces) {
		Shader.maxBounces = maxBounces;
	}
	
	public static Vector phong(Ray ray, Body light) throws IOException {
		Vector combined = new Vector(0, 0, 0);

		Vector lightDir = Vector.norm(Vector.sub(light.pos, ray.pos));
		Vector reflectionDir = Vector.sub(ray.dir, Vector.scale(ray.normal, 2.0 * Vector.dot(ray.normal, ray.dir)));
		double specularLight = Vector.clamp(Math.pow(Vector.dot(reflectionDir, lightDir), 51)) * 0.5;
		double distanceFactor = 1 / Math.pow(Vector.sub(light.pos, ray.pos).length(), 2);
		double diffuseLight = Math.max(Vector.dot(ray.normal, lightDir), 0);
		Vector emission = ray.getEmission();

		combined.x = emission.x + light.mat.getEmission().x * (ray.getColor().x * diffuseLight + specularLight) * distanceFactor;
		combined.y = emission.y + light.mat.getEmission().y * (ray.getColor().y * diffuseLight + specularLight) * distanceFactor;
		combined.z = emission.z + light.mat.getEmission().z * (ray.getColor().z * diffuseLight + specularLight) * distanceFactor;
		return combined;
	}

	public static Vector pbr(Ray ray, Body light) throws IOException {
		double roughness2 = ray.getRoughness() * ray.getRoughness();
		double reflectivity = (ray.getMetallic() > 0.5) ? 0.95 : 0.04;

		Vector lightDir = Vector.norm(Vector.sub(light.pos, ray.pos));
		Vector half = Vector.norm(Vector.sub(lightDir, ray.dir));
		double intensity = 1 / Math.pow(Vector.sub(light.pos, ray.pos).length(), 2);

		double nDotL = Math.max(Vector.dot(ray.normal, lightDir), 0.0001);
		double nDotV = Math.max(Vector.dot(ray.normal, ray.dir.scale(-1)), 0.0001);
		double nDotH = Math.max(Vector.dot(ray.normal, half), 0.0001);
		double vDotH = Math.max(Vector.dot(half, ray.dir.scale(-1)), 0.0001);

		double fSchlick = reflectivity + (1 - reflectivity) * Math.pow(1 - vDotH, 5);
		double gSmith = (nDotL / (nDotL * (1 - roughness2 / 2) + roughness2 / 2)) * (nDotV / (nDotV * (1 - roughness2 / 2) + roughness2 / 2));
		double dGGX = roughness2 * roughness2 / (Math.PI * (1 + nDotH * nDotH * (roughness2 * roughness2 - 1)));

		double specularBRDF = fSchlick * dGGX * gSmith / (4 * nDotV * nDotL);
		Vector diffuseBRDF = ray.getColor().scale((1 - fSchlick) / Math.PI);

		Vector finalColor = Vector.mul(Vector.add(diffuseBRDF, specularBRDF), light.mat.getEmission().scale(intensity * nDotL));
		return new Vector(finalColor);
	}

	public static Vector evaluateLightingPBR(Ray ray) throws IOException {
		Vector combinedLight = new Vector();
		for (int lightIndex = 0; lightIndex < Bodies.getList().size(); lightIndex++) {
			Body light;
			if ((light = Bodies.get(lightIndex)).mat.getIntensity() != 0) {

				Vector lightDir = Vector.norm(Vector.sub(light.pos, ray.pos));
				Ray shadowRay = Bodies.rayIntersect(new Ray(Vector.add(ray.pos, Vector.scale(ray.normal, epsilonStep)), lightDir));
				if (shadowRay.hitIndex != lightIndex && shadowRay.hit) continue;

				combinedLight = combinedLight.add(pbr(ray, light));
			}
		}
		return combinedLight;
	}

	public static Vector evaluateLightingPhong(Ray ray) throws IOException {
		Vector combinedLight = new Vector();
		for (int lightIndex = 0; lightIndex < Bodies.getList().size(); lightIndex++) {
			Body light;
			if ((light = Bodies.get(lightIndex)).mat.getIntensity() != 0) {

				Vector lightDir = Vector.norm(Vector.sub(light.pos, ray.pos));
				Ray shadowRay = Bodies.rayMarch(new Ray(Vector.add(ray.pos, Vector.scale(ray.normal, epsilonStep)), lightDir));
				if (shadowRay.hitIndex != lightIndex && shadowRay.hit) continue;

				combinedLight = combinedLight.add(phong(ray, light));
			}
		}
		return combinedLight;
	}
	
}
