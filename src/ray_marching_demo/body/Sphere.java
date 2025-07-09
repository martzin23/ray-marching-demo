package ray_marching_demo.body;

import ray_marching_demo.util.Material;
import ray_marching_demo.util.Ray;
import ray_marching_demo.util.Vector;

public class Sphere extends Body {

	private String[] tags = { "Sphere", "Moveable", "Editable", "Resizable" };

	public Sphere() {
		super();
		super.tags = tags;
	}

	public Sphere(Vector pos, Vector size) {
		super(pos, size);
		super.tags = tags;
	}

	public Sphere(Material mat, Vector pos, Vector size) {
		super(mat, pos, size);
		super.tags = tags;
	}

	public Sphere(Material mat, Vector pos, Vector size, Vector vel) {
		super(mat, pos, size, vel);
		super.tags = tags;
	}

	@Override
	public double getDistance(double x, double y, double z) {
		x -= this.pos.x;
		y -= this.pos.y;
		z -= this.pos.z;
		return Math.sqrt(x * x + y * y + z * z) - this.size.x;
	}

	@Override
	public Vector evaluateIntersection(Ray ray) {
		Vector offsetPos = Vector.sub(ray.pos, pos);
		double d = Math.pow(Vector.dot(offsetPos, ray.dir), 2) - ((Vector.dot(offsetPos, offsetPos) - Math.pow(this.size.x, 2)));
		if (d < 0) return new Vector(-1, 0, 0);
		else return new Vector(-Vector.dot(offsetPos, ray.dir) - Math.sqrt(d), 0, 0);
	}

}
