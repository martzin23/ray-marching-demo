package ray_marching_demo.body;

import ray_marching_demo.util.Material;
import ray_marching_demo.util.Vector;

public class VelocitySphere extends Body {

	private String[] tags = { "Sphere", "Moveable", "VelocitySphere", "Editable", "Resizable" };

	public VelocitySphere() {
		super();
		super.tags = tags;
	}

	public VelocitySphere(Vector pos, Vector size) {
		super(pos, size);
		super.tags = tags;
	}

	public VelocitySphere(Material mat, Vector pos, Vector size) {
		super(mat, pos, size);
		super.tags = tags;
	}

	public VelocitySphere(Material mat, Vector pos, Vector size, Vector vel) {
		super(mat, pos, size, vel);
		super.tags = tags;
	}

	@Override
	public double getDistance(double x, double y, double z) {
		x -= this.pos.x;
		y -= this.pos.y;
		z -= this.pos.z;
		double s = Math.sqrt(x * x + y * y + z * z) - this.size.x;

		Vector p1 = Vector.scale(Vector.norm(this.vel), this.size.x);
		Vector p2 = Vector.add(p1, this.vel);
		double c = capsule(new Vector(x, y, z), p1, p2, 0.05);

		return Math.min(s, c);
	}

	private double capsule(Vector p, Vector a, Vector b, double r) {
		Vector pa = Vector.sub(p, a), ba = Vector.sub(b, a);
		double h = Vector.clamp(Vector.dot(pa, ba) / Vector.dot(ba, ba), 0, 1);
		return Vector.length(Vector.sub(pa, Vector.scale(ba, h))) - r;
	}

}
