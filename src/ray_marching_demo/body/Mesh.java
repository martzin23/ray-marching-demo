package ray_marching_demo.body;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import ray_marching_demo.util.Material;
import ray_marching_demo.util.MeshBuilder;
import ray_marching_demo.util.Ray;
import ray_marching_demo.util.Vector;

public class Mesh extends Body {

	private String[] tags = { "Mesh", "Moveable", "Editable", "Resizable" };
	private ArrayList<Triangle> mesh = new ArrayList<>();
	private Box bBox;

	public Mesh(File path, Material mat) {
		super();
		super.tags = tags;
		super.mat = mat;

		try {
			mesh = MeshBuilder.readObjFile(path);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}

		if (mesh.size() != 0) {
			Vector min = mesh.get(0).getMin();
			Vector max = mesh.get(0).getMax();
			for (int i = 1; i < mesh.size(); i++) {
				min.set(Math.min(min.x, mesh.get(i).getMin().x), Math.min(min.y, mesh.get(i).getMin().y), Math.min(min.z, mesh.get(i).getMin().z));
				max.set(Math.max(max.x, mesh.get(i).getMin().x), Math.max(max.y, mesh.get(i).getMin().y), Math.max(max.z, mesh.get(i).getMin().z));
			}
			bBox = new Box(Vector.add(max, min).scale(0.5), new Vector((max.x - min.x)/2.0, (max.y - min.y)/2.0, (max.z - min.z)/2.0));
		}
		else System.err.println("WARNING: Mesh object is empty!");
	}

	@Override
	public double getDistance(double x, double y, double z) {
		if (mesh.size() == 0) return 1e10;
		
		Vector pos = Vector.sub(new Vector(x, y, z).mul(this.size), this.pos);
		double closestDistance = mesh.get(0).getDistance(pos);
		for (int i = 1; i < mesh.size(); i++) { closestDistance = Math.min(closestDistance, mesh.get(i).getDistance(pos)); }
		return closestDistance;
	}

	@Override
	public Vector evaluateIntersection(Ray ray) {
		if (mesh.size() == 0) return new Vector(-1, 0, 0);
		Ray transformedRay = new Ray(ray.pos.mul(this.size).sub(this.pos), ray.dir);
		if (bBox.evaluateIntersection(transformedRay).y < 0) return new Vector(-1);

		Vector intersection;
		Vector closestIntersection = new Vector(-1);
		boolean hit = false;

		for (int triangleIndex = 0; triangleIndex < mesh.size(); triangleIndex++) {
			intersection = mesh.get(triangleIndex).evaluateIntersection(transformedRay);
			
			if (intersection.x >= 0) {
				if (!hit) {
					hit = true;
					closestIntersection = intersection;
				} else if (intersection.x < closestIntersection.x) {
					closestIntersection = intersection;
				}
			}
		}
		return closestIntersection;
	}

}
