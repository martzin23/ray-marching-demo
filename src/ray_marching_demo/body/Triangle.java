package ray_marching_demo.body;

import ray_marching_demo.util.Ray;
import ray_marching_demo.util.Vector;

public class Triangle extends Body {

	private String[] tags = { "Triangle", "Moveable", "Editable" };
	private Vector v0, v1, v2;
	private Vector vn0, vn1, vn2;
	private Vector vt0, vt1, vt2;
	
	private Vector up = new Vector(0,0,1);
	private Vector empty = new Vector();

	public Triangle() {
		super();
		super.tags = tags;
		this.v0 = new Vector(0, 0, 0);
		this.v1 = new Vector(1, 0, 0);
		this.v2 = new Vector(1, 1, 1);
		this.vn0 = up;
		this.vn1 = up;
		this.vn2 = up;
		this.vt0 = empty;
		this.vt1 = empty;
		this.vt2 = empty;
	}
	
	public Triangle(Vector v0, Vector v1, Vector v2) {
		super();
		super.tags = tags;
		this.v0 = v0;
		this.v1 = v1;
		this.v2 = v2;
		this.vn0 = up;
		this.vn1 = up;
		this.vn2 = up;
		this.vt0 = empty;
		this.vt1 = empty;
		this.vt2 = empty;
	}
	
	public Triangle(Vector v0, Vector v1, Vector v2, Vector vn0, Vector vn1, Vector vn2) {
		super();
		super.tags = tags;
		this.v0 = v0;
		this.v1 = v1;
		this.v2 = v2;
		this.vn0 = vn0;
		this.vn1 = vn1;
		this.vn2 = vn2;
		this.vt0 = empty;
		this.vt1 = empty;
		this.vt2 = empty;
	}
	
	public Triangle(Vector v0, Vector v1, Vector v2, Vector vn0, Vector vn1, Vector vn2, Vector vt0, Vector vt1, Vector vt2) {
		super();
		super.tags = tags;
		this.v0 = v0;
		this.v1 = v1;
		this.v2 = v2;
		this.vn0 = vn0;
		this.vn1 = vn1;
		this.vn2 = vn2;
		this.vt0 = vt0;
		this.vt1 = vt1;
		this.vt2 = vt2;
	}

	@Override
	public double getDistance(double x, double y, double z) {
		Vector pos = Vector.sub(new Vector(x, y, z), this.pos);
		Vector ba = Vector.sub(v1, v0);
		Vector pa = Vector.sub(pos, v0);
		Vector cb = Vector.sub(v2, v1);
		Vector pb = Vector.sub(pos, v1);
		Vector ac = Vector.sub(v0, v2);
		Vector pc = Vector.sub(pos, v2);
		Vector nor = Vector.cross(ba, ac);
		return Math.sqrt((Math.signum(Vector.dot(Vector.cross(ba, nor), pa)) + Math.signum(Vector.dot(Vector.cross(cb, nor), pb)) + Math.signum(Vector.dot(Vector.cross(ac, nor), pc)) < 2.0)
				? Math.min(Math.min(Vector.dot2(Vector.scale(ba, Vector.clamp(Vector.dot(ba, pa) / Vector.dot2(ba), 0.0, 1.0)).sub(pa)), Vector.dot2(Vector.scale(cb, Vector.clamp(Vector.dot(cb, pb) / Vector.dot2(cb), 0.0, 1.0)).sub(pb))), Vector.dot2(Vector.scale(ac, Vector.clamp(Vector.dot(ac, pc) / Vector.dot2(ac), 0.0, 1.0)).sub(pc)))
				: Vector.dot(nor, pa) * Vector.dot(nor, pa) / Vector.dot(nor, nor));
	}

	@Override
	public Vector evaluateIntersection(Ray ray) {
		Vector pos = Vector.sub(ray.pos, this.pos);
		Vector v1v0 = Vector.sub(v1, v0);
		Vector v2v0 = Vector.sub(v2, v0);
		Vector rov0 = Vector.sub(pos, v0);
		Vector n = Vector.cross(v1v0, v2v0);
		Vector q = Vector.cross(rov0, ray.dir);
		double d = 1.0 / Vector.dot(ray.dir, n);
		double u = d * Vector.dot(q.scale(-1), v2v0);
		double v = d * Vector.dot(q, v1v0);
		double t = d * Vector.dot(n.scale(-1), rov0);
		if (u < 0.0 || v < 0.0 || (u + v) > 1.0) t = -1.0;
		return new Vector(t, u, v);
	}
	
	public Vector getUV(double u, double v) {
		Vector ab = Vector.lerp(u, vt1, vt0);
		Vector abc = Vector.lerp(v, ab, vt2);
		return abc;
	}
	
	public Vector getNormal() {
		return Vector.add(vn0, vn1, vn2).scale(1/3.0).norm();
	}
	
	public Vector getMin() {
		return new Vector(Math.min(Math.min(v0.x, v1.x), v2.x), Math.min(Math.min(v0.y, v1.y), v2.y), Math.min(Math.min(v0.z, v1.z), v2.z));
	}
	
	public Vector getMax() {
		return new Vector(Math.max(Math.max(v0.x, v1.x), v2.x), Math.max(Math.max(v0.y, v1.y), v2.y), Math.max(Math.max(v0.z, v1.z), v2.z));
	}

}
