package ray_marching_demo;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.IOException;

import ray_marching_demo.shader.Shader;

public class Input implements MouseListener, KeyListener, MouseWheelListener {

	private static boolean mouseR, mouseL, mouseM;
	private static boolean keySpace, keyShift, keyEnter, keyAlt, keyBackspace;
	private static boolean keyW, keyA, keyS, keyD, keyQ, keyE;
	private static boolean keyT, keyZ, keyU, keyG, keyH, keyJ, keyB, keyN, keyM, keyF;

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			mouseL = true;
			Update.selectBody();
		}
		if (e.getButton() == MouseEvent.BUTTON2) { mouseM = true; }
		if (e.getButton() == MouseEvent.BUTTON3) {
			mouseR = true;
			Update.deselectBody();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) { mouseL = false; }
		if (e.getButton() == MouseEvent.BUTTON2) { mouseM = false; }
		if (e.getButton() == MouseEvent.BUTTON3) { mouseR = false; }
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
			case 87: // W
				keyW = true;
				break;
			case 65: // A
				keyA = true;
				break;
			case 83: // S
				keyS = true;
				break;
			case 68: // D
				keyD = true;
				break;
			case 69: // E
				keyE = true;
				break;
			case 81: // Q
				keyQ = true;
				break;
			case 38: // ^
				Display.setDownSample(Math.max(Display.getDownSample() / 2, 1));
				break;
			case 40: // ˇ
				Display.setDownSample(Math.min(Display.getDownSample() * 2, 32));
				break;
			case 37: // <
				Shader.setJumpDistance(Math.min(Shader.getJumpDistance() * 2, 2));
				Shader.setMarchPrecision(Math.max(Shader.getMarchPrecision() - 1, 1));
				Shader.setMaxSamples(Math.max(Shader.getMaxSamples() - 1, 1));
				break;
			case 39: // >
				Shader.setJumpDistance(Math.max(Shader.getJumpDistance() / 2, 0.001));
				Shader.setMarchPrecision(Math.min(Shader.getMarchPrecision() + 1, 16));
				Shader.setMaxSamples(Shader.getMaxSamples() + 1);
				break;
			case 27: // Esc
				Display.exitWindow();
				break;
			case 10: // Enter
				keyEnter = true;
				try {
					Display.renderImage();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				break;
			case 32: // Space
				Update.switchTime();
				keySpace = true;
				break;
			case 16: // Shift
				keyShift = true;
				break;
			case 18: // Alt
				keyAlt = true;
				break;
			case 8: // Backspace
				keyBackspace = true;
				break;
			case 73: // I
				Update.spawnSphere();
				break;
			case 79: // O
				Update.spawnCube();
				break;
			case 75: // K
				Update.spawnEllipsoid();
				break;
			case 76: // L
				Update.spawnVelocitySphere();
				break;
			case 84: // T
				keyT = true;
				break;
			case 90: // Z
				keyZ = true;
				break;
			case 85: // U
				keyU = true;
				break;
			case 71: // G
				keyG = true;
				break;
			case 72: // H
				keyH = true;
				break;
			case 74: // J
				keyJ = true;
				break;
			case 66: // B
				keyB = true;
				break;
			case 78: // N
				keyN = true;
				break;
			case 77: // M
				keyM = true;
				break;
			case 70: // F
				keyF = true;
				break;
			case 48: // 0
				Display.setShader(0);
				break;
			case 49: // 1
				Display.setShader(1);
				break;
			case 50: // 2
				Display.setShader(2);
				break;
			case 51: // 3
				Display.setShader(3);
				break;
			case 52: // 4
				Display.setShader(4);
				break;
			case 53: // 5
				Display.setShader(5);
				break;
			case 54: // 6
				Display.setShader(6);
				break;
			case 55: // 7
				Display.setShader(7);
				break;
			case 56: // 8
				Display.setShader(8);
				break;
			case 57: // 9
				Display.setShader(9);
				break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
			case 87: // W
				keyW = false;
				break;
			case 65: // A
				keyA = false;
				break;
			case 83: // S
				keyS = false;
				break;
			case 68: // D
				keyD = false;
				break;
			case 69: // E
				keyE = false;
				break;
			case 81: // Q
				keyQ = false;
				break;
			case 10: // Enter
				keyEnter = false;
				break;
			case 32: // Space
				keySpace = false;
				break;
			case 16: // Shift
				keyShift = false;
				break;
			case 18: // Alt
				keyAlt = false;
				break;
			case 8: // Backspace
				keyBackspace = false;
				break;
			case 84: // T
				keyT = false;
				break;
			case 90: // Z
				keyZ = false;
				break;
			case 85: // U
				keyU = false;
				break;
			case 71: // G
				keyG = false;
				break;
			case 72: // H
				keyH = false;
				break;
			case 74: // J
				keyJ = false;
				break;
			case 66: // B
				keyB = false;
				break;
			case 78: // N
				keyN = false;
				break;
			case 77: // M
				keyM = false;
				break;
			case 70: // F
				keyF = false;
				break;
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (e.getWheelRotation() < 0) {
			if (keyT || keyZ || keyU || keyG || keyH || keyJ || keyB || keyN || keyM || keyF) {
				if (keyT) Update.resizeBody(1.1, 1, 1);
				if (keyZ) Update.resizeBody(1, 1.1, 1);
				if (keyU) Update.resizeBody(1, 1, 1.1);
				if (keyG) Update.moveBody(0.1, 0, 0);
				if (keyH) Update.moveBody(0, 0.1, 0);
				if (keyJ) Update.moveBody(0, 0, 0.1);
				if (keyB) Update.recolorBody(0.1, 0, 0);
				if (keyN) Update.recolorBody(0, 0.1, 0);
				if (keyM) Update.recolorBody(0, 0, 0.1);
				if (keyF) Update.scaleEmission(0.5);
			} else {
				Update.setMovementSpeed(Update.getMovementSpeed() * 1.25);
			}
		} else {
			if (keyT || keyZ || keyU || keyG || keyH || keyJ || keyB || keyN || keyM || keyF) {
				if (keyT) Update.resizeBody(0.9, 1, 1);
				if (keyZ) Update.resizeBody(1, 0.9, 1);
				if (keyU) Update.resizeBody(1, 1, 0.9);
				if (keyG) Update.moveBody(-0.1, 0, 0);
				if (keyH) Update.moveBody(0, -0.1, 0);
				if (keyJ) Update.moveBody(0, 0, -0.1);
				if (keyB) Update.recolorBody(-0.1, 0, 0);
				if (keyN) Update.recolorBody(0, -0.1, 0);
				if (keyM) Update.recolorBody(0, 0, -0.1);
				if (keyF) Update.scaleEmission(-0.5);
			} else {
				Update.setMovementSpeed(Update.getMovementSpeed() * 0.75);
			}
		}
	}

	public static boolean getMouseL() {
		return mouseL;
	}

	public static boolean getMouseR() {
		return mouseR;
	}

	public static boolean getMouseM() {
		return mouseM;
	}

	public static boolean getKey(char key) {
		switch (key) {
			case 'w':
				return keyW;
			case 'a':
				return keyA;
			case 's':
				return keyS;
			case 'd':
				return keyD;
			case 'e':
				return keyE;
			case 'q':
				return keyQ;
			case ' ':
				return keySpace;
			case 'S':
				return keyShift;
			case 'E':
				return keyEnter;
			case 'A':
				return keyAlt;
			case 'B':
				return keyBackspace;
			default:
				return false;
		}
	}
}
