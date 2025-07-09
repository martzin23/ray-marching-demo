package ray_marching_demo;

import java.io.IOException;
import ray_marching_demo.body.Bodies;
import ray_marching_demo.util.Images;

/* TODO checklist
 * FEATURES
 * render mesh with uv mapped textures
 * 
 * BUGS
 * mouse can exit window
 * collision not working when velocity is zero
 * gravity yeet
 * perlin noise lines
 * ray intersection with angle 0 0 not working
 */

public class Main {

	public static void main(String avg[]) throws IOException {
		Images.initImages();
		Bodies.initBodyCollection();
		Display.initDisplay();
		
		while (Display.isWindowActive()) {
			
			Update.update(Display.getTimeFactor());
			
			Display.renderFrame();
		}
	}
	
}
