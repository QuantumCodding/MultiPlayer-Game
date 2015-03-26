package com.GameName.Cube.Cubes;

import com.GameName.Cube.Cube;
import com.GameName.Cube.Render.WaterCubeRender;
import com.GameName.Engine.ResourceManager.Materials;
import com.GameName.Render.Effects.RenderProperties.RenderPropertiesBuilder;
import com.GameName.Render.Effects.RenderStep;

public class WaterCube extends Cube {

	public WaterCube() {
		super("WaterCube");

		setLiquid(true);
		setOpacity(0.75f);
		
		setDisplayName("Water");
		
		setMaterial(Materials.Water);
		setRender(new WaterCubeRender());
		setRenderProperties(new RenderPropertiesBuilder()
			.enableTexture3D().disableCullFace().setRenderStep(RenderStep.Transparent).build()
		);
	}
}
