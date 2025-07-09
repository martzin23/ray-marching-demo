package ray_marching_demo.util;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class Material {

	private Object color = 0xffffff;
	private Object intensity = 0.0;
	private Object roughness = 0.5;
	private Object metallic = 0.0;
	private Object normal = null;

	private double textureScale = 1;
	private double normalStrength = 0.5;

	public Material() {
	}

	public Material(Integer color) {
		this.color = color;
	}

	public Material(Number intensity) {
		this.intensity = intensity;
	}

	public Material(Object color, Object intensity, Object roughness, Object metallic) {
		this.color = color;
		this.intensity = intensity;
		this.roughness = roughness;
		this.metallic = metallic;
	}
	
	public Material(Object color, Object intensity, Object roughness, Object metallic, double textureScale) {
		this.color = color;
		this.intensity = intensity;
		this.roughness = roughness;
		this.metallic = metallic;
		this.textureScale = textureScale;
	}

	public Material(Object color, Object intensity, Object roughness, Object metallic, Object normal, double normalStrength, double textureScale) {
		this.color = color;
		this.intensity = intensity;
		this.roughness = roughness;
		this.metallic = metallic;
		this.normalStrength = normalStrength;
		this.textureScale = textureScale;
	}

	public Vector getColor(Vector position, Vector normal) throws IOException {
		if (color instanceof Number) return Mapping.toVec(((Number)color).intValue());
		else if ( color instanceof BufferedImage) return Mapping.toVec(Mapping.mapTextureTriplanarSmooth((BufferedImage)color, position, normal, textureScale));
		else throw new IllegalArgumentException("Color isn't of type Number nor BufferedImage!");
	}

	public Vector getColor() {
		if (color instanceof Number) return Mapping.toVec(((Number)color).intValue());
		else throw new IllegalArgumentException("Color isn't of type Number!");
	}

	public Vector getEmission(Vector position, Vector normal) throws IOException {
		if (((Number)intensity).doubleValue() == 0) return new Vector(0);
		if (color instanceof Number) return Mapping.toVec(((Number)color).intValue()).scale(((Number)intensity).doubleValue());
		else return this.getColor(position, normal).scale(((Number)intensity).doubleValue());
	}

	public Vector getEmission() {
		if (((Number)intensity).doubleValue() == 0) return new Vector(0);
		return Mapping.toVec(((Number)color).intValue()).scale(((Number)intensity).doubleValue());
	}

	public double getIntensity() {
		return ((Number)intensity).doubleValue();
	}

	public double getRoughness(Vector position, Vector normal) throws IOException {
		if (roughness instanceof Number) return ((Number)roughness).doubleValue();
		else if (roughness instanceof BufferedImage) return Mapping.toVal(Mapping.mapTextureTriplanarSmooth((BufferedImage)roughness, position, normal, textureScale));
		else throw new IllegalArgumentException("Roughness isn't of type Number nor BufferedImage!");
	}

	public double getMetallic(Vector position, Vector normal) throws IOException {
		if (metallic instanceof Number) return ((Number)metallic).doubleValue();
		else if (metallic instanceof BufferedImage) return Mapping.toVal(Mapping.mapTextureTriplanarSmooth((BufferedImage)metallic, position, normal, textureScale));
		else throw new IllegalArgumentException("Metallic isn't of type Number nor BufferedImage!");
	}

	public Vector getNormal(Vector position, Vector normal) throws IOException {
		if (this.normal == null) return new Vector(0);
		else if (this.normal instanceof BufferedImage) return Mapping.toVec(Mapping.mapTextureTriplanarSmooth((BufferedImage)this.normal, position, normal, textureScale)).add(-0.5).scale(2 * normalStrength);
		else throw new IllegalArgumentException("Normal isn't of type BufferedImage!");
	}

	public void setColor(int col) {
		this.color = col;
	}

	public void setIntensity(double emission) {
		this.intensity = emission;
	}

	public void setRoughness(double roughness) {
		this.roughness = roughness;
	}

	public void setMetallic(double metallic) {
		this.metallic = metallic;
	}

	public void setTextureScale(double textureScale) {
		this.textureScale = textureScale;
	}

	public void setNormalStrength(double normalStrength) {
		this.normalStrength = normalStrength;
	}

}
