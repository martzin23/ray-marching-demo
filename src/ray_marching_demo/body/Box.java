package ray_marching_demo.body;

import ray_marching_demo.util.Material;
import ray_marching_demo.util.Ray;
import ray_marching_demo.util.Vector;

public class Box extends Body {

	private String[] tags = { "Cube", "Moveable", "Editable", "Resizable" };

	public Box() {
		super();
		super.tags = tags;
	}

	public Box(Material mat, Vector pos, Vector size, Vector vel) {
		super(mat, pos, size, vel);
		super.tags = tags;
	}

	public Box(Material mat, Vector pos, Vector size) {
		super(mat, pos, size);
		super.tags = tags;
	}

	public Box(Vector pos, Vector size) {
		super(pos, size);
		super.tags = tags;
	}

	@Override
	public double getDistance(double x, double y, double z) {
		x -= this.pos.x;
		y -= this.pos.y;
		z -= this.pos.z;
		return Math.sqrt(Math.pow(Math.max(Math.abs(x) - this.size.x, 0.0), 2) + Math.pow(Math.max(Math.abs(y) - this.size.y, 0.0), 2) + Math.pow(Math.max(Math.abs(z) - this.size.z, 0.0), 2));
	}

	@Override
	public Vector evaluateIntersection(Ray ray) {
		Vector offsetPos = Vector.sub(ray.pos, pos);
		Vector n = Vector.mul(offsetPos, Vector.div(new Vector(1), ray.dir));
		Vector k = Vector.mul(this.size, new Vector(Math.abs(1/ray.dir.x), Math.abs(1/ray.dir.y), Math.abs(1/ray.dir.z)));
		Vector t1 = Vector.add(n, k).scale(-1);
		Vector t2 = Vector.sub(k, n);
		double tN = Math.max(Math.max(t1.x, t1.y), t1.z);
		double tF = Math.min(Math.min(t2.x, t2.y), t2.z);
		if (tN > tF || tF < 0.0) return new Vector(-1, 0, 0);
		return new Vector(tN, tF, 0);
	}

}
