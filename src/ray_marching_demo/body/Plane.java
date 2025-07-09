package ray_marching_demo.body;

import ray_marching_demo.util.Material;
import ray_marching_demo.util.Ray;
import ray_marching_demo.util.Vector;

public class Plane extends Body {

	private String[] tags = { "Plane", "Editable", "Moveable" };

	public Plane() {
		super(new Vector(0, 0, 0), new Vector(0, 0, 1));
		super.tags = tags;
	}

	public Plane(Vector pos, Vector size) {
		super(pos, size);
		super.tags = tags;
	}

	public Plane(Material mat, Vector pos, Vector size) {
		super(mat, pos, size);
		super.tags = tags;
	}

	@Override
	public double getDistance(double x, double y, double z) {
		x -= this.pos.x;
		y -= this.pos.y;
		z -= this.pos.z;
		return x * this.size.x + y * this.size.y + z * this.size.z;
	}

	@Override
	public Vector evaluateIntersection(Ray ray) {
		Vector offsetPos = Vector.sub(ray.pos, pos);
		double d = -Vector.dot(offsetPos, this.size) / Vector.dot(ray.dir, this.size);
		return new Vector(d, 0, 0);
	}
	
}
