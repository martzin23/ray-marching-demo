package ray_marching_demo.body;

import java.io.IOException;

import ray_marching_demo.util.Images;
import ray_marching_demo.util.Mapping;
import ray_marching_demo.util.Material;
import ray_marching_demo.util.Vector;

public class DisplacePlane extends Body {

	private String[] tags = { "DisplacePlane", "Editable", "Moveable", "Resizable" };

	public DisplacePlane() {
		super();
		super.tags = tags;
	}

	public DisplacePlane(Vector pos) {
		super(pos, new Vector(0, 0, 0));
		super.tags = tags;
	}

	public DisplacePlane(Material mat, Vector pos) {
		super(mat, pos, new Vector(0, 0, 0));
		super.tags = tags;
	}

	public DisplacePlane(double size, double height) {
		super(new Vector(0, 0, 0), new Vector(size, 0, height));
		super.tags = tags;
	}

	@Override
	public double getDistance(double x, double y, double z) {
		x -= this.pos.x;
		y -= this.pos.y;
		z -= this.pos.z;
		double h = 0;
		try {
			h = this.size.z * Vector.length(Mapping.toVec(Mapping.mapTextureBasic(Images.height, new Vector(x, y, z), this.size.x)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return z - (h * 2 - 1);
	}

}
