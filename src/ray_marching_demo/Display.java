package ray_marching_demo;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import ray_marching_demo.body.Bodies;
import ray_marching_demo.shader.*;
import ray_marching_demo.util.Mapping;

public class Display {

	private static final int WIDTH = 1540;
	private static final int HEIGHT = 790;
	private static int frameCounter = 0;
	private static int downSample = 16;

	private static boolean isWindowActive;
	private static double timeFactor;
	private static long timer;
	private static double fpsCounter;
	private static BufferedImage rendered, displayed = null;
	private static JFrame frame = new JFrame("ray_marching_demo");
	private static JLabel label = new JLabel();
	private static Shader shader = new ShaderNormal();

	public static boolean isWindowActive() {
		return isWindowActive;
	}

	public static void exitWindow() {
		isWindowActive = false;
		frame.setEnabled(false);
		frame.dispose();
	}

	public static int getDownSample() {
		return downSample;
	}

	public static void setDownSample(int downSample) {
		Display.downSample = downSample;
	}

	public static int getFrame() {
		return frameCounter;
	}

	public static double getTimeFactor() {
		return timeFactor;
	}

	public static void initDisplay() throws IOException {
		isWindowActive = true;
		timer = System.currentTimeMillis();
		timeFactor = 0;

		rendered = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		displayed = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

		label.setHorizontalTextPosition(JLabel.CENTER);
		label.setVerticalTextPosition(JLabel.TOP);
		label.setForeground(new Color(0xffffff));
		label.setIconTextGap(-50);
		label.setIcon(new ImageIcon(displayed));

		frame.add(label);
		frame.pack();
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.addMouseListener(new Input());
		frame.addKeyListener(new Input());
		frame.addMouseWheelListener(new Input());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		return;
	}

	public static void renderFrame() throws IOException {
		label.setText("FPS: " + String.format("%.2f", fpsCounter) + " XYZ: " + Bodies.getCam().getPos());

		int width = rendered.getWidth() / downSample;
		int height = rendered.getHeight() / downSample;
		double aspectRatio = (double) rendered.getWidth() / rendered.getHeight();

		for (int x = 0; x < width; x++)
			for (int y = 0; y < height; y++)
				rendered.setRGB(x, y, shader.getRGB(((double) x / width - 0.5) * aspectRatio, (double) y / height - 0.5));

		Mapping.resizeImage(rendered, displayed, downSample);
		frame.invalidate();
		frame.validate();
		frame.repaint();

		fpsCounter = 1000.0 / (System.currentTimeMillis() - timer);
		timeFactor = (System.currentTimeMillis() - timer) / 50.0;
		timer = System.currentTimeMillis();
		frameCounter++;
		return;
	}

	public static void renderImage() throws IOException {
		exitWindow();
		Update.deselectBody();

		BufferedImage screenshot = new BufferedImage(1920, 1080, BufferedImage.TYPE_INT_RGB);
		int width = screenshot.getWidth();
		int height = screenshot.getHeight();
		double aspectRatio = (double) screenshot.getWidth() / screenshot.getHeight();
		System.out.println("Rendering-----------------------------------------50%---------------------------------------------Done");

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++)
				screenshot.setRGB(x, y, shader.getRGB(((double) x / width - 0.5) * aspectRatio, (double) y / height - 0.5));
			if (x % ((int) (screenshot.getWidth() * 0.01)) == 0) System.out.print(".");
		}

		System.out.println("");
		File file = new File("out\\" + System.currentTimeMillis() + ".png");
		ImageIO.write(screenshot, "png", file);
//		Desktop.getDesktop().open(file); //FIXME doesn't work
		return;
	}

	public static void setShader(int n) {
		switch (n) {
			case 1:
				shader = new ShaderPhong();
				break;
			case 2:
				shader = new ShaderPBR();
				break;
			case 3:
				shader = new ShaderNormal();
				break;
			case 4:
				shader = new ShaderNormalRayTrace();
				break;
			case 5:
				shader = new ShaderVolume();
				break;
			case 6:
				shader = new ShaderTemp();
				break;
			case 7:
				shader = new ShaderTemp();
				break;
			case 8:
				shader = new ShaderTemp();
				break;
			case 9:
				shader = new ShaderTemp();
				break;
			default:
				shader = new ShaderTemp();
				break;
		}
	}
}
