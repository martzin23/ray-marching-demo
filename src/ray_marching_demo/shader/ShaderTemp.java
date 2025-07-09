package ray_marching_demo.shader;

import java.io.IOException;

import ray_marching_demo.util.Mapping;

public class ShaderTemp extends Shader {
	
	@Override
	public int getRGB(double u, double v) throws IOException {
		
		
		
		return Mapping.toHex(u * 0.5 + 0.5, v * 0.5 + 0.5, 0);
	}

}
