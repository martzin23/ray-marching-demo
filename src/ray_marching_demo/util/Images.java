package ray_marching_demo.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Images {
	
	public static BufferedImage volume, environment, height, diffuse, normal, roughness;

	public static void initImages() throws IOException {
		// XXX loaded images
		volume = ImageIO.read(new File("res\\volumes\\vol1.png"));
		height = ImageIO.read(new File("res\\heightmaps\\heightmap7.png"));
		
		// NOT USED
//		environment = ImageIO.read(new File("res\\environments\\env1.jpg"));
	
//		diffuse = ImageIO.read(new File("res\\PBR\\rock_islands\\diffuse.png"));
//		normal = ImageIO.read(new File("res\\PBR\\rock_islands\\normal.png"));
//		roughness = ImageIO.read(new File("res\\PBR\\rock_islands\\roughness.png"));
//		height = ImageIO.read(new File("res\\PBR\\rock_islands\\height.png"));

//		diffuse = ImageIO.read(new File("res\\PBR\\earth\\8081_earthmap4k.jpg"));
//		specular = ImageIO.read(new File("res\\PBR\\earth\\8081_earthspec4k.jpg"));
//		height = ImageIO.read(new File("res\\PBR\\earth\\8081_earthbump4k.jpg"));
	
//		diffuse = ImageIO.read(new File("res\\PBR\\spaceship_panels\\diffuse.png"));
//		normal = ImageIO.read(new File("res\\PBR\\spaceship_panels\\normal.png"));
//		roughness = ImageIO.read(new File("res\\PBR\\spaceship_panels\\metallic.png"));
//		height = ImageIO.read(new File("res\\PBR\\spaceship_panels\\height.png"));
		return;
	}
}
