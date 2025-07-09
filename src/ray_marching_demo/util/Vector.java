package ray_marching_demo.util;

public class Vector {
	public double x, y, z;

	public Vector() {
	}

	public Vector(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector(double[] vec) {
		this.x = vec[0];
		this.y = vec[1];
		this.z = vec[2];
	}

	public Vector(Vector other) {
		this.x = other.x;
		this.y = other.y;
		this.z = other.z;
	}

	public Vector(double s) {
		this.x = s;
		this.y = s;
		this.z = s;
	}
	
	public void set(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public String toString() {
		return "(" + String.format("%.2f", x) + "," + String.format("%.2f", y) + "," + String.format("%.2f", z) + ")";
	}

	public static double clamp(double value, double min, double max) {
		return Math.min(Math.max(value, min), max);
	}

	public static double clamp(double value) {
		return Math.min(Math.max(value, 0), 1);
	}

	public static double lerp(double t, double tmin, double tmax, double xmin, double xmax) {
		return ((t - tmin) / (tmax - tmin)) * (xmax - xmin) + xmin;
	}

	public static double lerp(double t, double xmin, double xmax) {
		return t * (xmax - xmin) + xmin;
	}

	public static Vector lerp(double t, Vector v1, Vector v2) {
		Vector r = new Vector();
		r.x = lerp(t, v1.x, v2.x);
		r.y = lerp(t, v1.y, v2.y);
		r.z = lerp(t, v1.z, v2.z);
		return r;
	}

	public static double dot(Vector v1, Vector v2) {
		return (v1.x * v2.x) + (v1.y * v2.y) + (v1.z * v2.z);
	}

	public static double dot2(Vector v) {
		return (v.x * v.x) + (v.y * v.y) + (v.z * v.z);
	}

	public static double length(Vector v) {
		return Math.sqrt(Math.pow(v.x, 2) + Math.pow(v.y, 2) + Math.pow(v.z, 2));
	}

	public double length() {
		return length(this);
	}

	public static Vector add(Vector v1, Vector v2) {
		Vector r = new Vector();
		r.x = v1.x + v2.x;
		r.y = v1.y + v2.y;
		r.z = v1.z + v2.z;
		return r;
	}

	public Vector add(Vector other) {
		return Vector.add(this, other);
	}

	public static Vector add(Vector... v) {
		Vector r = new Vector(0);
		for (Vector i : v)
			r.add(i);
		return r;
	}

	public static Vector sub(Vector v1, Vector v2) {
		Vector r = new Vector();
		r.x = v1.x - v2.x;
		r.y = v1.y - v2.y;
		r.z = v1.z - v2.z;
		return r;
	}

	public Vector sub(Vector other) {
		return Vector.sub(this, other);
	}

	public static Vector mul(Vector v1, Vector v2) {
		Vector r = new Vector();
		r.x = v1.x * v2.x;
		r.y = v1.y * v2.y;
		r.z = v1.z * v2.z;
		return r;
	}

	public Vector mul(Vector other) {
		return Vector.mul(this, other);
	}

	public static Vector mul(Vector... v) {
		Vector r = new Vector(1, 1, 1);
		for (Vector i : v)
			r.mul(i);
		return r;
	}

	public static Vector div(Vector v1, Vector v2) {
		Vector r = new Vector();
		r.x = v1.x / v2.x;
		r.y = v1.y / v2.y;
		r.z = v1.z / v2.z;
		return r;
	}

	public Vector div(Vector other) {
		return Vector.div(this, other);
	}

	public static Vector add(Vector v, double s) {
		Vector r = new Vector();
		r.x = v.x + s;
		r.y = v.y + s;
		r.z = v.z + s;
		return r;
	}

	public Vector add(double s) {
		return Vector.add(this, s);
	}

	public static Vector scale(Vector v, double s) {
		Vector r = new Vector();
		r.x = v.x * s;
		r.y = v.y * s;
		r.z = v.z * s;
		return r;
	}

	public Vector scale(double s) {
		return Vector.scale(this, s);
	}

	public static Vector norm(Vector v) {
		Vector r = new Vector();
		double sum = Math.pow(v.x, 2) + Math.pow(v.y, 2) + Math.pow(v.z, 2);
		r.x = v.x / Math.sqrt(sum);
		r.y = v.y / Math.sqrt(sum);
		r.z = v.z / Math.sqrt(sum);
		return r;
	}

	public Vector norm() {
		return Vector.norm(this);
	}

	public static Vector cross(Vector v1, Vector v2) {
		Vector r = new Vector();
		r.x = v1.y * v2.z - v1.z * v2.y;
		r.y = v1.z * v2.x - v1.x * v2.z;
		r.z = v1.x * v2.y - v1.y * v2.x;
		return r;
	}

	public static Vector rotX(Vector v, double angle) {
		if (angle == 0) return v;
		Vector vecAxis = new Vector(1, 0, 0);
		Vector vecCross = new Vector(0, -vecAxis.x * v.z, vecAxis.x * v.y);
		Vector r = new Vector(v.x * Math.cos(Math.toRadians(angle)) + (1 - Math.cos(Math.toRadians(angle))) * Vector.dot(v, vecAxis) * vecAxis.x, v.y * Math.cos(Math.toRadians(angle)) + Math.sin(Math.toRadians(angle)) * vecCross.y, v.z * Math.cos(Math.toRadians(angle)) + Math.sin(Math.toRadians(angle)) * vecCross.z);
		return r;
	}

	public static Vector rotY(Vector v, double angle) {
		if (angle == 0) return v;
		Vector vecAxis = new Vector(0, 1, 0);
		Vector vecCross = new Vector(vecAxis.y * v.z, 0, -vecAxis.y * v.x);
		Vector r = new Vector(v.x * Math.cos(Math.toRadians(angle)) + Math.sin(Math.toRadians(angle)) * vecCross.x, v.y * Math.cos(Math.toRadians(angle)) + (1 - Math.cos(Math.toRadians(angle))) * Vector.dot(v, vecAxis) * vecAxis.y, v.z * Math.cos(Math.toRadians(angle)) + Math.sin(Math.toRadians(angle)) * vecCross.z);
		return r;
	}

	public static Vector rotZ(Vector v, double angle) {
		if (angle == 0) return v;
		Vector vecAxis = new Vector(0, 0, 1);
		Vector vecCross = new Vector(-vecAxis.z * v.y, vecAxis.z * v.x, 0);
		Vector r = new Vector(v.x * Math.cos(Math.toRadians(angle)) + Math.sin(Math.toRadians(angle)) * vecCross.x, 
				v.y * Math.cos(Math.toRadians(angle)) + Math.sin(Math.toRadians(angle)) * vecCross.y, 
				v.z * Math.cos(Math.toRadians(angle)) + (1 - Math.cos(Math.toRadians(angle))) * Vector.dot(v, vecAxis) * vecAxis.z);
		return r;
	}
	
	public static Vector toDir(double horizontalAngle, double verticalAngle) {
		return new Vector(Math.cos(Math.toRadians(horizontalAngle)) * Math.sin(Math.toRadians(verticalAngle)), Math.sin(Math.toRadians(horizontalAngle)) * Math.sin(Math.toRadians(verticalAngle)), Math.cos(Math.toRadians(verticalAngle)));
	}

}