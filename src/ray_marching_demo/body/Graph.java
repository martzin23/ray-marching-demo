package ray_marching_demo.body;

import ray_marching_demo.util.Material;
import ray_marching_demo.util.Vector;

public class Graph extends Body {

	private String[] tags = { "Graph", "Editable", "Moveable", "Resizable" };

	public Graph() {
		super();
		super.tags = tags;
	}

	public Graph(Vector size) {
		super(new Vector(0, 0, 0), size);
		super.tags = tags;
	}

	public Graph(Vector pos, Vector size) {
		super(pos, size);
		super.tags = tags;
	}

	public Graph(Material mat, Vector pos, Vector size) {
		super(mat, pos, size);
		super.tags = tags;
	}

	@Override
	public double getDistance(double x, double y, double z) {
		double s = Math.sqrt(x * x + y * y + z * z) - 5;
		x -= this.pos.x;
		y -= this.pos.y;
		z -= this.pos.z;
		x /= this.size.x;
		y /= this.size.y;
		z /= -this.size.z;
		
		// XXX editable graph function
		
		// x = (int)(x*10)/10.0;
		// y = (int)(y*10)/10.0;
		// z = (int)(z*10)/10.0;
		double g = 3 * x * x * y - 2 * x * y + 5 * x - 3 * y - z;
		return Math.max(s, g);
//		return g;
		
	}

}
