package ray_marching_demo;

import ray_marching_demo.body.Bodies;
import ray_marching_demo.body.Box;
import ray_marching_demo.body.Ellipsoid;
import ray_marching_demo.body.Sphere;
import ray_marching_demo.body.VelocitySphere;
import ray_marching_demo.util.Mapping;
import ray_marching_demo.util.Material;
import ray_marching_demo.util.Ray;
import ray_marching_demo.util.Vector;

public class Update extends Bodies {

	private static int selectedIndex = -1;
	private static int selectedCol;
	private static double selectedDistance;
	private static double movementSpeed = 1;
	private static boolean timeFreeze = true;
	private static final int HIGHLIGHT_COLOR = 0xff0000;

	public static double getMovementSpeed() {
		return movementSpeed;
	}

	public static void setMovementSpeed(double movementSpeed) {
		Update.movementSpeed = movementSpeed;
	}

	public static void switchTime() {
		timeFreeze = !timeFreeze;
	}

	public static void update(double timeFactor) {
		// TODO clean up
		if (!timeFreeze) {
			gravityStep();
        	frictionStep(0.03 * timeFactor);
			collisionBounce(1);
//			collisionMerge();
			moveStep(0.01 * timeFactor);
		}
		pushBody(0.5 * timeFactor);
		Update.moveBody();
		Bodies.getCam().movementStep(movementSpeed * timeFactor);
		if (Input.getKey('B')) Update.deleteBody();
	}

	public static void moveStep(double stepSize) {
		for (int thisIndex = 0; thisIndex < body.size(); thisIndex++)
			if (isTag(thisIndex, "Sphere")) get(thisIndex).pos = Vector.add(get(thisIndex).pos, Vector.scale(get(thisIndex).vel, stepSize));
	}

	public static void gravityStep() {
		for (int thisIndex = 0; thisIndex < body.size(); thisIndex++) {
			if (isTag(thisIndex, "Sphere")) {

				Vector a = new Vector(0, 0, 0);
				double m1 = 4.0 / 3.0 * Math.PI * Math.pow(get(thisIndex).size.x, 3);

				for (int otherIndex = 0; otherIndex < body.size(); otherIndex++) {
					if (!isTag(otherIndex, "Sphere") || thisIndex == otherIndex) continue;

					double m2 = 4.0 / 3.0 * Math.PI * Math.pow(get(otherIndex).size.x, 3);
					double r = Vector.length(Vector.sub(get(otherIndex).pos, get(thisIndex).pos));
					double fAmp = m1 * m2 / (r * r);
					Vector fDir = Vector.norm(Vector.sub(get(otherIndex).pos, get(thisIndex).pos));
					Vector f = Vector.scale(fDir, fAmp);
					a = Vector.add(a, Vector.scale(f, 1 / m1));
				}
				get(thisIndex).vel = Vector.add(get(thisIndex).vel, a);
			}
		}
		return;
	}

	public static void frictionStep(double airFriction) {
		for (int objectIndex = 0; objectIndex < body.size(); objectIndex++) { get(objectIndex).vel = Vector.sub(get(objectIndex).vel, Vector.scale(get(objectIndex).vel, airFriction)); }
		return;
	}

	public static void pushBody(double strength) {
		if (Input.getKey('S') && selectedIndex != -1 && isTag(selectedIndex, "Moveable"))
			get(selectedIndex).vel = Vector.add(get(selectedIndex).vel, Vector.scale(camera.getRayDir(0, 0), strength));
		else if (Input.getKey('A') && isTag(selectedIndex, "Moveable"))
			get(selectedIndex).vel = Vector.sub(get(selectedIndex).vel, Vector.scale(camera.getRayDir(0, 0), strength));
		return;
	}

	public static void collisionBounce(double absorbtion) {
		for (int thisIndex = 0; thisIndex < body.size(); thisIndex++) {
			if (!isTag(thisIndex, "Sphere")) continue;
			for (int otherIndex = 0; otherIndex < body.size(); otherIndex++) {
				if (!isTag(otherIndex, "Sphere") || thisIndex == otherIndex) continue;
				if ((get(thisIndex).size.x + get(otherIndex).size.x) >= Vector.length(Vector.sub(get(otherIndex).pos, get(thisIndex).pos))) {
					Vector normalDir = Vector.norm(Vector.sub(get(thisIndex).pos, get(otherIndex).pos));
					Vector bounceDir = Vector.sub(get(thisIndex).vel, Vector.scale(normalDir, 2.0 * Vector.dot(normalDir, get(thisIndex).vel)));
					get(thisIndex).vel = Vector.scale(Vector.norm(bounceDir), Vector.length(get(thisIndex).vel) * absorbtion);
				}
			}
		}
		return;
	}

	public static void collisionMerge() {
		int maxCounter = body.size();
		for (int thisIndex = 0; thisIndex < maxCounter; thisIndex++) {
			if (!isTag(thisIndex, "Sphere")) continue;
			for (int otherIndex = 0; otherIndex < body.size(); otherIndex++) {
				if (!isTag(otherIndex, "Sphere") || thisIndex == otherIndex) continue;
				if ((get(thisIndex).size.x + get(otherIndex).size.x) >= Vector.length(Vector.sub(get(otherIndex).pos, get(thisIndex).pos))) {

					int newCol = Mapping.toHex(Vector.add(get(thisIndex).mat.getColor().scale(0.5), get(otherIndex).mat.getColor().scale(0.5)));
					Vector newPos = Vector.scale(Vector.add(get(thisIndex).pos, get(otherIndex).pos), 0.5);
					Vector newSize = new Vector(Math.cbrt(Math.pow(get(thisIndex).size.x, 3) + Math.pow(get(otherIndex).size.x, 3)));
					Vector newVel = Vector.scale(Vector.add(Vector.scale(get(thisIndex).vel, Math.pow(get(thisIndex).size.x, 3)), Vector.scale(get(otherIndex).vel, Math.pow(get(otherIndex).size.x, 3))), 1 / (Math.pow(get(thisIndex).size.x, 3) + Math.pow(get(otherIndex).size.x, 3)));

					deselectBody();
					body.remove(thisIndex--);
					body.remove(--otherIndex);
					body.add(new Sphere(new Material(newCol), newPos, newSize, newVel));
					maxCounter--;
					break;
				}
			}
		}
		return;
	}

	public static void moveBody() {
		if (Input.getMouseM() && !Input.getKey('S') && !Input.getKey('A') && selectedIndex != -1 && isTag(selectedIndex, "Moveable")) {
			get(selectedIndex).pos = Vector.add(camera.getPos(), Vector.scale(camera.getRayDir(0, 0), selectedDistance));
			get(selectedIndex).vel = new Vector(0, 0, 0.01);
		}
		return;
	}

	public static void selectBody() {
		if (selectedIndex != -1) get(selectedIndex).mat.setColor(selectedCol);
		Ray ray = Bodies.rayMarch(Bodies.camera.getCameraRay(0, 0));
		if (ray.hit && isTag(ray.hitIndex, "Editable")) {
			selectedIndex = ray.hitIndex;
			selectedDistance = ray.dist + 0.5;
			selectedCol = Mapping.toHex(get(ray.hitIndex).mat.getColor());
			get(ray.hitIndex).mat.setColor(HIGHLIGHT_COLOR);
		} else selectedIndex = -1;
		return;
	}

	public static void deselectBody() {
		if (selectedIndex != -1) get(selectedIndex).mat.setColor(selectedCol);
		selectedIndex = -1;
		return;
	}

	public static void resizeBody(double a, double b, double c) {
		if (selectedIndex == -1 || !isTag(selectedIndex, "Resizable")) return;
		get(selectedIndex).size.x *= a;
		get(selectedIndex).size.y *= b;
		get(selectedIndex).size.z *= c;
		return;
	}

	public static void moveBody(double a, double b, double c) {
		if (selectedIndex == -1 || !isTag(selectedIndex, "Moveable")) return;
		get(selectedIndex).pos.x += a;
		get(selectedIndex).pos.y += b;
		get(selectedIndex).pos.z += c;
		return;
	}

	public static void recolorBody(double x, double y, double z) {
		if (selectedIndex == -1 || !isTag(selectedIndex, "Editable")) return;
		get(selectedIndex).mat.setColor(selectedCol);
		double r = get(selectedIndex).mat.getColor().x;
		double g = get(selectedIndex).mat.getColor().y;
		double b = get(selectedIndex).mat.getColor().z;
		r += x;
		g += y;
		b += z;
		get(selectedIndex).mat.setColor(Mapping.toHex(new Vector(r, g, b)));
		selectedCol = Mapping.toHex(new Vector(r, g, b));
		return;
	}

	public static void scaleEmission(double x) {
		if (selectedIndex == -1 || !isTag(selectedIndex, "Editable")) return;
		get(selectedIndex).mat.setIntensity(get(selectedIndex).mat.getIntensity() + x);
	}

	public static void spawnSphere() {
		Ray ray = Bodies.rayMarch(Bodies.camera.getCameraRay(0, 0));
		body.add(new Sphere(ray.pos, new Vector(1)));
		selectBody();
		return;
	}

	public static void spawnCube() {
		Ray ray = Bodies.rayMarch(Bodies.camera.getCameraRay(0, 0));
		body.add(new Box(ray.pos, new Vector(1, 1, 1)));
		selectBody();
		return;
	}

	public static void spawnEllipsoid() {
		Ray ray = Bodies.rayMarch(Bodies.camera.getCameraRay(0, 0));
		body.add(new Ellipsoid(ray.pos, new Vector(1, 1, 1)));
		selectBody();
		return;
	}

	public static void spawnVelocitySphere() {
		Ray ray = Bodies.rayMarch(Bodies.camera.getCameraRay(0, 0));
		body.add(new VelocitySphere(new Material(), ray.pos, new Vector(1, 1, 1), new Vector(0, 0, 0.01)));
		selectBody();
		return;
	}

	public static void deleteBody() {
		if (selectedIndex == -1) return;
		body.remove(selectedIndex);
		selectedIndex = -1;
		return;
	}

}
