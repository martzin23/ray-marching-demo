package ray_marching_demo.body;

import ray_marching_demo.util.Material;
import ray_marching_demo.util.Ray;
import ray_marching_demo.util.Vector;

public abstract class Body {

	String[] tags;
	public Material mat;
	public Vector pos;
	public Vector size;
	public Vector vel;

	public Body() {
		this.mat = new Material();
		this.pos = new Vector(0, 0, 0);
		this.size = new Vector(1, 1, 1);
		this.vel = new Vector(0, 0, 0);
	}

	public Body(Vector pos, Vector size) {
		this.mat = new Material();
		this.pos = pos;
		this.size = size;
		this.vel = new Vector(0, 0, 0);
	}

	public Body(Material mat, Vector pos, Vector size) {
		this.mat = mat;
		this.pos = pos;
		this.size = size;
		this.vel = new Vector(0, 0, 0);
	}

	public Body(Material mat, Vector pos, Vector size, Vector vel) {
		this.mat = mat;
		this.pos = pos;
		this.size = size;
		this.vel = vel;
	}

	public boolean isTag(String tag) {
		for (String thisTags : this.tags)
			if (thisTags.equals(tag)) return true;
		return false;
	}

	public double getDistance(Vector v) {
		return getDistance(v.x, v.y, v.z);
	}

	public double getDistance(double x, double y, double z) {
		return 1e10;
	}

	public Vector evaluateIntersection(Ray ray) {
		return new Vector(-1, 0, 0);
	}

}
