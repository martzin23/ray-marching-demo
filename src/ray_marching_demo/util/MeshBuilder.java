package ray_marching_demo.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import ray_marching_demo.body.Triangle;

public class MeshBuilder {

	public static ArrayList<Triangle> readObjFile(File path) throws IOException {

		String[] file = null;
		ArrayList<Triangle> mesh = new ArrayList<>();
		ArrayList<Vector> position = new ArrayList<>();
		ArrayList<Vector> normal = new ArrayList<>();
		ArrayList<Vector> texture = new ArrayList<>();

		try (Scanner sc = new Scanner(path)) {
			StringBuilder strBuild = new StringBuilder();
			while (sc.hasNextLine()) {
				strBuild.append(sc.nextLine());
				strBuild.append("\n");
			}
			file = strBuild.toString().split("\n");
		} catch (IOException e) {
			throw new MeshBuildingException("Failed to read file!");
		}

		for (String line : file) {
			if (line.contains("v ")) position.add(readPosition(line));
			else if (line.contains("vn ")) normal.add(readNormal(line));
			else if (line.contains("vt ")) texture.add(readTexture(line));
		}
		boolean hasUVData = texture.size() != 0;

		for (String line : file) {
			if (line.contains("f ")) {

				String[] seperatedLine = line.split(" ");
				if (seperatedLine.length < 4) throw new MeshBuildingException("Mesh building failed, found face with less than 3 verticies!");
				if (seperatedLine.length > 4) System.err.println("WARNING: Found face with more than 3 verticies!");

				if (hasUVData) {
					int v1Index, v2Index, v3Index, vn1Index, vn2Index, vn3Index, vt1Index, vt2Index, vt3Index;

					try {
						String[] vertexData1 = seperatedLine[1].split("/");
						v1Index = Integer.parseInt(vertexData1[0]);
						vt1Index = Integer.parseInt(vertexData1[1]);
						vn1Index = Integer.parseInt(vertexData1[2]);

						String[] vertexData2 = seperatedLine[2].split("/");
						v2Index = Integer.parseInt(vertexData2[0]);
						vt2Index = Integer.parseInt(vertexData2[1]);
						vn2Index = Integer.parseInt(vertexData2[2]);

						String[] vertexData3 = seperatedLine[3].split("/");
						v3Index = Integer.parseInt(vertexData3[0]);
						vt3Index = Integer.parseInt(vertexData3[1]);
						vn3Index = Integer.parseInt(vertexData3[2]);
					} catch (NumberFormatException e) {
						throw new MeshBuildingException("Couldn't read face data!");
					}

					Vector v1 = null, v2 = null, v3 = null, vn1 = null, vn2 = null, vn3 = null, vt1 = null, vt2 = null, vt3 = null;
					v1 = position.get(v1Index - 1);
					v2 = position.get(v2Index - 1);
					v3 = position.get(v3Index - 1);
					vt1 = texture.get(vt1Index - 1);
					vt2 = texture.get(vt2Index - 1);
					vt3 = texture.get(vt3Index - 1);
					vn1 = normal.get(vn1Index - 1);
					vn2 = normal.get(vn2Index - 1);
					vn3 = normal.get(vn3Index - 1);

					mesh.add(new Triangle(v1, v2, v3, vn1, vn2, vn3, vt1, vt2, vt3));
				} else {
					int v1Index, v2Index, v3Index, vn1Index, vn2Index, vn3Index;

					try {
						String[] vertexData1 = seperatedLine[1].split("/");
						v1Index = Integer.parseInt(vertexData1[0]);
						vn1Index = Integer.parseInt(vertexData1[2]);

						String[] vertexData2 = seperatedLine[2].split("/");
						v2Index = Integer.parseInt(vertexData2[0]);
						vn2Index = Integer.parseInt(vertexData2[2]);

						String[] vertexData3 = seperatedLine[3].split("/");
						v3Index = Integer.parseInt(vertexData3[0]);
						vn3Index = Integer.parseInt(vertexData3[2]);
					} catch (NumberFormatException e) {
						throw new MeshBuildingException("Couldn't read face data!");
					}

					Vector v1 = null, v2 = null, v3 = null, vn1 = null, vn2 = null, vn3 = null;
					v1 = position.get(v1Index - 1);
					v2 = position.get(v2Index - 1);
					v3 = position.get(v3Index - 1);
					vn1 = normal.get(vn1Index - 1);
					vn2 = normal.get(vn2Index - 1);
					vn3 = normal.get(vn3Index - 1);

					mesh.add(new Triangle(v1, v2, v3, vn1, vn2, vn3));
				}
			}
		}
		return mesh;
	}

	private static Vector readPosition(String line) throws MeshBuildingException {
		String[] seperatedLine = line.split(" ");
		if (seperatedLine.length != 4) throw new MeshBuildingException("Wrong vertex position data!");
		Vector v = new Vector();
		try {
			v.x = Double.parseDouble(seperatedLine[1]);
			v.y = Double.parseDouble(seperatedLine[2]);
			v.z = Double.parseDouble(seperatedLine[3]);
		} catch (NumberFormatException e) {
			throw new MeshBuildingException("Couldn't read vertex position data!");
		}
		return v;
	}

	private static Vector readNormal(String line) throws MeshBuildingException {
		String[] seperatedLine = line.split(" ");
		if (seperatedLine.length != 4) throw new MeshBuildingException("Wrong vertex normal data!");
		Vector vn = new Vector();
		try {
			vn.x = Double.parseDouble(seperatedLine[1]);
			vn.y = Double.parseDouble(seperatedLine[2]);
			vn.z = Double.parseDouble(seperatedLine[3]);
		} catch (NumberFormatException e) {
			throw new MeshBuildingException("Couldn't read vertex normal data!");
		}
		return vn;
	}

	private static Vector readTexture(String line) throws MeshBuildingException {
		String[] seperatedLine = line.split(" ");
		if (seperatedLine.length != 3) throw new MeshBuildingException("Wrong vertex texture data!");
		Vector vt = new Vector();
		try {
			vt.x = Double.parseDouble(seperatedLine[1]);
			vt.y = Double.parseDouble(seperatedLine[2]);
			vt.z = 0;
		} catch (NumberFormatException e) {
			throw new MeshBuildingException("Couldn't read vertex texture data!");
		}
		return vt;
	}

}
