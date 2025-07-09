package ray_marching_demo.body;

import java.io.File;
import java.util.ArrayList;

import ray_marching_demo.shader.Shader;
import ray_marching_demo.util.Mapping;
import ray_marching_demo.util.Material;
import ray_marching_demo.util.Ray;
import ray_marching_demo.util.Vector;

@SuppressWarnings("unused")
public class Bodies {

	protected static ArrayList<Body> body = new ArrayList<>();
	protected static Camera camera;

	public static void initBodyCollection() {
		camera = new Camera(new Vector(-15, 0, 0), 1, 0.5, true, false);
		
		// Wall and sphere
		body.add(new Plane(new Material(0xffffff), new Vector(0, 0, 0), new Vector(0, 0, 1)));
		body.add(new Sphere(new Material(10.0), new Vector(4, 0, 2), new Vector(0.1)));
		body.add(new Box(new Material(0xaaffaa, 0, 0, 1), new Vector(-3, 0, 1), new Vector(0.5, 10, 10)));
		body.add(new Sphere(new Material(0xffaaaa), new Vector(0), new Vector(2)));

		// 1 Triangle
//		body.add(new Triangle(new Vector(0, 0, 0), new Vector(1, 0, 0), new Vector(0.5, 0, 1)));

		// Mesh
//		body.add(new Mesh(new File("res\\models\\[ADD YOUR .OBJ]"), new Material()));

		// Graph
//		 body.add(new Sphere(new Material(10.0) ,new Vector(0,0,0), new Vector(1,0,0)));
//		 body.add(new Graph());

		// Heightmap
//		body.add(new Sphere(new Material(50.0) ,new Vector(0, 0, 5), new Vector(0.5)));
//		body.add(new DisplacePlane(20, 0.5));
		
		// PBR spheres
//		body.add(new Sphere(new Material(30.0), new Vector(-2, 0, 4), new Vector(0.5)));
//		body.add(new Plane(new Material(), new Vector(0, 0, -5), new Vector(0, 0, 1)));
//		body.add(new Plane(new Material(), new Vector(0, 0, 5), new Vector(0, 0, -1)));
//		body.add(new Plane(new Material(), new Vector(5, 0, 0), new Vector(-1, 0, 0)));
//		body.add(new Plane(new Material(0xaa0000), new Vector(0, 5, 0), new Vector(0, -1, 0)));
//		body.add(new Plane(new Material(0x00aa00), new Vector(0, -5, 0), new Vector(0, 1, 0)));
//		body.add(new Sphere(new Material(0xffffff, 0, 0.5, 0), new Vector(1, 2.2, -3), new Vector(1)));
//		body.add(new Sphere(new Material(0xffffff, 0, 0.4, 1), new Vector(1, 0, -3), new Vector(1)));
//		body.add(new Sphere(new Material(0xffffff, 0, 0.01, 1), new Vector(1, -2.2, -3), new Vector(1)));

		// Cornell box
//		body.add(new Box(new Material(0xffffff), new Vector(11, 0, 0), new Vector(1, 10, 10)));
//		body.add(new Box(new Material(0xffffff), new Vector(0, 0, -11), new Vector(10, 10, 1)));
//		body.add(new Box(new Material(0xffffff), new Vector(0, 0, 11), new Vector(10, 10, 1)));
//		body.add(new Box(new Material(0xaa0000), new Vector(0, 11, 0), new Vector(10, 1, 10)));
//		body.add(new Box(new Material(0x00aa00), new Vector(0, -11, 0), new Vector(10, 1, 10)));
//		body.add(new Box(new Material(0x0000aa), new Vector(2, -5, -4), new Vector(3, 3, 6)));
//		body.add(new Sphere(new Material(0xffaaff), new Vector(-4, 4, -6), new Vector(4)));
//		body.add(new Sphere(new Material(150.0), new Vector(0, 0, 9.7), new Vector(2.0)));

		// Cube of spheres
//		body.add(new Plane(new Vector(0, 0, 0), new Vector(0, 0, 1)));
//		body.add(new Sphere(new Material(10.0), new Vector(2, 2, 2), new Vector(0.1)));
//		for (double x = 0; x < 1; x += 0.25 / 2)
//			for (double y = 0; y < 1; y += 0.25 / 2)
//				for (double z = 0; z < 1; z += 0.25 / 2)
//					body.add(new Sphere(new Material(Mapping.toHex(new Vector(x, y, z))), new Vector(x, y, z), new Vector(0.1 / 2)));
		
		return;
	}

	public static ArrayList<Body> getList() {
		return body;
	}

	public static Camera getCam() {
		return camera;
	}

	public static Body get(int index) {
		return body.get(index);
	}

	public static boolean isTag(int index, String tag) {
		return body.get(index).isTag(tag);
	}

	public static Vector getNormal(Vector pos, int index, double epsilon) {
		Vector normal = new Vector();
		normal.x = (Bodies.get(index).getDistance(pos.x + epsilon, pos.y, pos.z) - Bodies.get(index).getDistance(pos.x, pos.y, pos.z)) / epsilon;
		normal.y = (Bodies.get(index).getDistance(pos.x, pos.y + epsilon, pos.z) - Bodies.get(index).getDistance(pos.x, pos.y, pos.z)) / epsilon;
		normal.z = (Bodies.get(index).getDistance(pos.x, pos.y, pos.z + epsilon) - Bodies.get(index).getDistance(pos.x, pos.y, pos.z)) / epsilon;
		normal = normal.norm();
		return normal;
	}

	public static Ray rayMarch(int maxMarches, double maxDistance, Ray ray) {
		int objectIndex;
		double closestDistance = 0, distance, epsilon = 0.003;

		for (int marchCount = 0; marchCount < maxMarches && ray.dist < maxDistance; marchCount++) {

			ray.hitIndex = 0;
			closestDistance = body.get(0).getDistance(ray.pos.x, ray.pos.y, ray.pos.z);
			for (objectIndex = 1; objectIndex < body.size(); objectIndex++) {
				distance = body.get(objectIndex).getDistance(ray.pos.x, ray.pos.y, ray.pos.z);
				if (closestDistance > distance) {
					closestDistance = distance;
					ray.hitIndex = objectIndex;
				}
			}

			if (closestDistance < epsilon) {
				ray.hit = true;
				ray.mat = get(ray.hitIndex).mat;
				return ray;
			} else {
				ray.pos = Vector.add(ray.pos, Vector.scale(ray.dir, closestDistance));
				ray.dist += closestDistance;
				ray.jumpCount++;
			}
		}
		return ray;
	}

	public static Ray rayMarch(int maxMarches, double maxDistance, int precision, Ray ray) {
		double closestDistance = 0, distance, epsilon = 0.003;

		for (int marchCount = 0; marchCount < maxMarches * precision && ray.dist < maxDistance; marchCount++) {

			ray.hitIndex = 0;
			closestDistance = body.get(0).getDistance(ray.pos.x, ray.pos.y, ray.pos.z);
			for (int objectIndex = 1; objectIndex < body.size(); objectIndex++) {
				distance = body.get(objectIndex).getDistance(ray.pos.x, ray.pos.y, ray.pos.z);
				if (closestDistance > distance) {
					closestDistance = distance;
					ray.hitIndex = objectIndex;
				}
			}

			if (closestDistance < epsilon) {
				ray.hit = true;
				ray.mat = get(ray.hitIndex).mat;
				return ray;
			} else {
				ray.pos = Vector.add(ray.pos, Vector.scale(ray.dir, closestDistance / precision));
				ray.dist += closestDistance / precision;
				ray.jumpCount++;
			}
		}
		return ray;
	}

	public static Ray rayMarch(Ray ray) {
		return rayMarch(Shader.getMaxMarches(), Shader.getMaxDistance(), Shader.getMarchPrecision(), ray);
	}

	public static Ray rayTrace(double maxDistance, double jumpDistance, Ray ray) {
		double distance, closestDistance = 0, epsilon = 0.003;

		while (ray.dist < maxDistance) {

			ray.hitIndex = 0;
			closestDistance = body.get(0).getDistance(ray.pos.x, ray.pos.y, ray.pos.z);
			for (int objectIndex = 1; objectIndex < body.size(); objectIndex++) {
				distance = body.get(objectIndex).getDistance(ray.pos.x, ray.pos.y, ray.pos.z);
				if (closestDistance > distance) {
					closestDistance = distance;
					ray.hitIndex = objectIndex;
				}
			}

			if (closestDistance < epsilon) {
				ray.hit = true;
				ray.mat = get(ray.hitIndex).mat;
				return ray;
			} else {
				ray.pos = Vector.add(ray.pos, Vector.scale(ray.dir, jumpDistance));
				ray.dist += jumpDistance;
				ray.jumpCount++;
			}
		}
		return ray;
	}

	public static Ray rayIntersect(Ray ray) {
		double closestDistance = 0;
		Vector intersectionInfo;
		Vector closestIntersection = null;
		double epsilon = 0.003;

		for (int objectIndex = 0; objectIndex < body.size(); objectIndex++) {
			intersectionInfo = get(objectIndex).evaluateIntersection(ray);

			if (intersectionInfo.x >= 0) {
				if (!ray.hit) {
					closestDistance = intersectionInfo.x;
					ray.hit = true;
					ray.hitIndex = objectIndex;
					closestIntersection = intersectionInfo;
				} else {
					double distance = intersectionInfo.x;
					if (distance < closestDistance) {
						closestDistance = distance;
						ray.hitIndex = objectIndex;
						closestIntersection = intersectionInfo;
					}
				}
			}
		}
		if (ray.hit) {
			ray.pos = Vector.add(ray.pos, ray.dir.scale(closestDistance - epsilon));
			ray.dist = closestDistance;
			ray.jumpCount = 0;
			ray.barymetric.set(closestIntersection.y, closestIntersection.z, 0);
			ray.mat = get(ray.hitIndex).mat;
		}
		return ray;
	}
}
