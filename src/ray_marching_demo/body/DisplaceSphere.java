package ray_marching_demo.body;

import java.io.IOException;

import ray_marching_demo.util.Images;
import ray_marching_demo.util.Mapping;
import ray_marching_demo.util.Material;
import ray_marching_demo.util.Vector;

public class DisplaceSphere extends Body {

	private String[] tags = { "DisplaceSphere", "Moveable", "Editable", "Resizable" };

	public DisplaceSphere() {
		super();
		super.tags = tags;
	}

	public DisplaceSphere(Material mat, Vector pos, Vector size, Vector vel) {
		super(mat, pos, size, vel);
		super.tags = tags;
	}

	public DisplaceSphere(Material mat, Vector pos, Vector size) {
		super(mat, pos, size);
		super.tags = tags;
	}

	public DisplaceSphere(Vector pos, Vector size) {
		super(pos, size);
		super.tags = tags;
	}

	@Override
	public double getDistance(double x, double y, double z) {
		x -= this.pos.x;
		y -= this.pos.y;
		z -= this.pos.z;
		double h = 0;
		double d = Math.sqrt(x * x + y * y + z * z) - this.size.x;
		try {
			h = Vector.length(Mapping.toVec(Mapping.mapTextureTriplanarSmooth(Images.height, new Vector(x, y, z), Vector.sub(this.pos, new Vector(x, y, z)).norm(), 10)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return d - h;
	}

}
