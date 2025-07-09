package ray_marching_demo.body;

import java.awt.MouseInfo;

import ray_marching_demo.Input;
import ray_marching_demo.util.Ray;
import ray_marching_demo.util.Vector;

public class Camera {

	private Vector pos;
	private Vector dir;
	private double[] rot = new double[2];
	private double fov;
	private double sensitivity;
	private boolean moveable;
	private boolean orthographic;

	public Camera() {
		this.pos = new Vector(0, 0, 0);
		this.dir = new Vector(1, 0, 0);
		this.fov = 1;
		this.sensitivity = 0.5;
		this.moveable = true;
		this.orthographic = false;
	}

	public Camera(Vector pos, double fov, double sensitivity, boolean moveable, boolean orthographic) {
		this.pos = pos;
		this.dir = new Vector(1, 0, 0);
		this.fov = fov;
		this.sensitivity = sensitivity;
		this.moveable = moveable;
		this.orthographic = orthographic;
	}
	
	public Vector getPos() {
		return this.pos;
	}
	
	public double getFov() {
		return fov;
	}

	public void setFov(double fov) {
		this.fov = fov;
	}

	public void setSensitivity(double sensitivity) {
		this.sensitivity = sensitivity;
	}

	public void setMoveable(boolean moveable) {
		this.moveable = moveable;
	}

	public void setOrthographic(boolean orthographic) {
		this.orthographic = orthographic;
	}

	public Vector getRayDir(double u, double v) {
		if (orthographic) return this.dir;
		else {
			Vector direction = new Vector(1, -this.fov * u, -this.fov * v);
			direction = Vector.rotY(direction, this.rot[1]);
			direction = Vector.rotZ(direction, this.rot[0]);
			return Vector.norm(direction);
		}
	}

	public Vector getRayPos(double u, double v) {
		if (orthographic) {
			Vector offset = new Vector(0, -this.fov * u * 30, -this.fov * v * 30);
			offset = Vector.rotY(offset, this.rot[1]);
			offset = Vector.rotZ(offset, this.rot[0]);
			return Vector.add(this.pos, offset);
		} else return this.pos;
	}

	public Ray getCameraRay(double u, double v) {
		return new Ray(getRayPos(u, v), getRayDir(u, v));
	}

	public void movementStep(double stepSize) {
		if (!moveable) return;
		this.rot[0] = -MouseInfo.getPointerInfo().getLocation().getX() * sensitivity;
		this.rot[1] = Math.clamp(MouseInfo.getPointerInfo().getLocation().getY() * sensitivity - 200, -90, 90);

		Vector camDir = Vector.rotZ(new Vector(1, 0, 0), this.rot[0]);
		Vector moveDir = new Vector(0, 0, 0);

		if (Input.getKey('w')) moveDir = Vector.add(moveDir, camDir);
		if (Input.getKey('s')) moveDir = Vector.sub(moveDir, camDir);
		if (Input.getKey('a')) moveDir = Vector.sub(moveDir, Vector.cross(camDir, new Vector(0, 0, 1)));
		if (Input.getKey('d')) moveDir = Vector.add(moveDir, Vector.cross(camDir, new Vector(0, 0, 1)));
		if (Input.getKey('e')) moveDir = Vector.add(moveDir, new Vector(0, 0, 1));
		if (Input.getKey('q')) moveDir = Vector.add(moveDir, new Vector(0, 0, -1));

		this.pos = Vector.add(this.pos, Vector.scale(moveDir, stepSize));
		this.dir = Vector.rotZ(Vector.rotY(new Vector(1, 0, 0), this.rot[1]), this.rot[0]);
	}
}
