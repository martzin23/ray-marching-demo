package ray_marching_demo.util;

import java.io.IOException;

import ray_marching_demo.body.Bodies;

public class Ray {

	public Vector pos;
	public Vector dir;
	public double dist;
	public boolean hit;
	public int jumpCount;

	public int hitIndex;
	public Material mat;
	public Vector normal;
	public Vector barymetric;

	public Ray(Vector pos, Vector dir) {
		this.pos = pos;
		this.dir = dir;
		this.dist = 0;
		this.hit = false;
		
		this.hitIndex = 0;
		this.jumpCount = 0;
		this.barymetric = new Vector();
	}

	public Ray() {
		this.pos = new Vector(0, 0, 0);
		this.dir = new Vector(0, 0, 0);
		this.dist = 0;
		this.hit = false;
		
		this.hitIndex = 0;
		this.jumpCount = 0;
		this.barymetric = new Vector();
	}

	public Ray(Ray other) {
		this.pos = other.pos;
		this.dir = other.dir;
		this.dist = other.dist;
		this.hit = other.hit;
		this.jumpCount = other.jumpCount;
		
		this.hitIndex = other.hitIndex;
		this.mat = other.mat;
		this.normal = other.normal;
		this.barymetric = other.barymetric;
	}
	
	public Vector getColor() throws IOException {
		return mat.getColor(pos, normal);
	}
	
	public Vector getEmission() throws IOException {
		return mat.getEmission(pos, normal);
	}
	
	public double getRoughness() throws IOException {
		return mat.getRoughness(pos, normal);
	}
	
	public double getMetallic() throws IOException {
		return mat.getMetallic(pos, normal);
	}
	
	public void calculateNormal(double epsilon) throws IOException {
		Vector surfaceNormal = Bodies.getNormal(this.pos, this.hitIndex, epsilon);
		Vector textureNormal = this.mat.getNormal(this.pos, surfaceNormal);
		this.normal = Vector.add(surfaceNormal, textureNormal).norm();
	}
	
}
