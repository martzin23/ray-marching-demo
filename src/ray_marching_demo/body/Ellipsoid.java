package ray_marching_demo.body;

import ray_marching_demo.util.Material;
import ray_marching_demo.util.Vector;

public class Ellipsoid extends Body {

	private String[] tags = { "Ellipsoid", "Moveable", "Editable", "Resizable" };

	public Ellipsoid() {
		super();
		super.tags = tags;
	}

	public Ellipsoid(Material mat, Vector pos, Vector size, Vector vel) {
		super(mat, pos, size, vel);
		super.tags = tags;
	}

	public Ellipsoid(Material mat, Vector pos, Vector size) {
		super(mat, pos, size);
		super.tags = tags;
	}

	public Ellipsoid(Vector pos, Vector size) {
		super(pos, size);
		super.tags = tags;
	}

	@Override
	public double getDistance(double x, double y, double z) {
		x -= this.pos.x;
		y -= this.pos.y;
		z -= this.pos.z;
		double r = Math.min(Math.min(size.x, size.y), size.z);
		x /= this.size.x / r;
		y /= this.size.y / r;
		z /= this.size.z / r;
		return Math.sqrt(x * x + y * y + z * z) - r;
	}
	
}
