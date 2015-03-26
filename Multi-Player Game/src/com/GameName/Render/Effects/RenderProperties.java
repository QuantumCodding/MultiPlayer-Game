package com.GameName.Render.Effects;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_BLEND_DST;
import static org.lwjgl.opengl.GL11.GL_BLEND_SRC;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE_MODE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_FRONT_FACE;
import static org.lwjgl.opengl.GL11.GL_SHADE_MODEL;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_1D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glFrontFace;
import static org.lwjgl.opengl.GL11.glGetInteger;
import static org.lwjgl.opengl.GL11.glIsEnabled;
import static org.lwjgl.opengl.GL11.glShadeModel;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_3D;

public class RenderProperties {
	private RenderStep renderStep;
	
	private Modification useCullFace;
	private int frontFace;
	private int cullFace;
	
	private Modification depthTest;
	
	private Modification useTexture1D;
	private Modification useTexture2D;
	private Modification useTexture3D;
	
	private Modification useBlend;
	private int blendSFactor;
	private int blendDFactor;
	
	private Modification shaderModel;
	private int shaderModelMode;
	
	private boolean applied;
	private RenderProperties beforeUse;
	
	private RenderProperties() {
		renderStep = RenderStep.Undeclared;
		
		useCullFace = Modification.Ingnore;
		depthTest = Modification.Ingnore;
		useBlend = Modification.Ingnore;
		shaderModel = Modification.Ingnore;
		
		useTexture1D = Modification.Ingnore;
		useTexture2D = Modification.Ingnore;
		useTexture3D = Modification.Ingnore;
	}
	
	public void apply() {
		if(!applied)
			beforeUse = getCurrentProperties();
		else return;
		
		if(useTexture1D == Modification.Enable)
			glEnable(GL_TEXTURE_1D);
		else 
			glDisable(GL_TEXTURE_1D);

		if(useTexture2D == Modification.Enable)
			glEnable(GL_TEXTURE_2D);
		else 
			glDisable(GL_TEXTURE_2D);
		
		if(useTexture3D == Modification.Enable)
			glEnable(GL_TEXTURE_3D);
		else 
			glDisable(GL_TEXTURE_3D);
		
		if(useCullFace == Modification.Enable) {
			glEnable(GL_CULL_FACE);
			glFrontFace(frontFace);
			glCullFace(cullFace);
		
		} else if(useCullFace == Modification.Disable) {
			glDisable(GL_CULL_FACE);
		}
		
		if(depthTest == Modification.Enable) 
			glEnable(GL_DEPTH_TEST);
		else if(depthTest == Modification.Disable)
			glDisable(GL_DEPTH_TEST);
		
		if(useBlend == Modification.Enable) {
			glEnable(GL_BLEND);
			glBlendFunc(blendSFactor, blendDFactor);
		
		} else if(useBlend == Modification.Disable) {
			glDisable(GL_BLEND);
		}
		
		if(shaderModel == Modification.Enable) {
			glShadeModel(shaderModelMode);
		}
		
		applied = true;
	}
	
	public void reset() {
		beforeUse.apply();
		applied = false;
	}
	
	public static class RenderPropertiesBuilder {
		private RenderProperties properties;

		public RenderPropertiesBuilder() {properties = new RenderProperties();}
		public RenderPropertiesBuilder(RenderProperties properties) {
			this.properties = properties.clone();
		}
		
		//Render Step
		public RenderPropertiesBuilder setRenderStep(RenderStep step) {
			properties.renderStep = step;
			return this;
		}
		
		//Texture
		public RenderPropertiesBuilder disableTexture1D() {
			properties.useTexture1D = Modification.Disable;
			return this;
		}
		
		public RenderPropertiesBuilder enableTexture1D() {
			properties.useTexture1D = Modification.Enable;
			return this;
		}
		
		public RenderPropertiesBuilder disableTexture2D() {
			properties.useTexture2D = Modification.Disable;
			return this;
		}
		
		public RenderPropertiesBuilder enableTexture2D() {
			properties.useTexture2D = Modification.Enable;
			return this;
		}
		
		public RenderPropertiesBuilder disableTexture3D() {
			properties.useTexture3D = Modification.Disable;
			return this;
		}
		
		public RenderPropertiesBuilder enableTexture3D() {
			properties.useTexture3D = Modification.Enable;
			return this;
		}
		
		//Depth Test
		public RenderPropertiesBuilder disableDepthTest() {
			properties.depthTest = Modification.Disable;
			return this;
		}
		
		public RenderPropertiesBuilder enableDepthTest() {
			properties.depthTest = Modification.Enable;
			return this;
		}
		
		//Cull Face
		public RenderPropertiesBuilder disableCullFace() {
			properties.useCullFace = Modification.Disable;
			return this;
		}
		
		public RenderPropertiesBuilder enableCullFace(int frontFace, int cullFace) {
			properties.useCullFace = Modification.Enable;
			properties.frontFace = frontFace;
			properties.cullFace = cullFace;
			
			return this;
		}
		
		//Blend
		public RenderPropertiesBuilder disableBlend() {
			properties.useBlend = Modification.Disable;
			return this;
		}
		
		public RenderPropertiesBuilder enableBlend(int sFactor, int dFactor) {
			properties.useBlend = Modification.Enable;
			properties.blendSFactor = sFactor;
			properties.blendDFactor = dFactor;
			
			return this;
		} 
		
		//Shader Model
		public RenderPropertiesBuilder enableShaderModel(int mode) {
			properties.shaderModel = Modification.Enable;
			properties.shaderModelMode = mode;
			return this;
		}
		
		public RenderProperties build() {
			return properties;
		}
	} 
	
	private enum Modification {
		Enable, Disable, Ingnore;
		
		public static Modification get(boolean in) {
			return in ? Enable : Disable;
		}
	}
	
	public static RenderProperties getCurrentProperties() {
		RenderProperties properties = new RenderProperties();
		
		properties.useCullFace = Modification.get(glIsEnabled(GL_CULL_FACE));
		properties.frontFace = glGetInteger(GL_FRONT_FACE);
		properties.cullFace = glGetInteger(GL_CULL_FACE_MODE);
		
		properties.depthTest = Modification.get(glIsEnabled(GL_DEPTH_TEST));
		
		properties.useTexture1D = Modification.get(glIsEnabled(GL_TEXTURE_1D));
		properties.useTexture2D = Modification.get(glIsEnabled(GL_TEXTURE_2D));
		properties.useTexture3D = Modification.get(glIsEnabled(GL_TEXTURE_3D));
		
		properties.useBlend = Modification.get(glIsEnabled(GL_BLEND));
		properties.blendSFactor = glGetInteger(GL_BLEND_SRC);
		properties.blendDFactor = glGetInteger(GL_BLEND_DST);
		
		properties.shaderModel = Modification.Enable;
		properties.shaderModelMode = glGetInteger(GL_SHADE_MODEL);
		
		return properties;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + blendDFactor;
		result = prime * result + blendSFactor;
		result = prime * result + frontFace;
		result = prime * result + cullFace;
		result = prime * result
				+ ((depthTest == null) ? 0 : depthTest.hashCode());
		result = prime * result
				+ ((useBlend == null) ? 0 : useBlend.hashCode());
		result = prime * result
				+ ((useCullFace == null) ? 0 : useCullFace.hashCode());
		result = prime * result
				+ ((useTexture1D == null) ? 0 : useTexture1D.hashCode());
		result = prime * result
				+ ((useTexture2D == null) ? 0 : useTexture2D.hashCode());
		result = prime * result
				+ ((useTexture3D == null) ? 0 : useTexture3D.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null) return false;
		if(!(obj instanceof RenderProperties)) return false;
		
		RenderProperties other = (RenderProperties) obj;
		if(renderStep != other.renderStep) return false;
		
		if(useBlend != other.useBlend) return false;
		if(blendDFactor != other.blendDFactor) return false;
		if(blendSFactor != other.blendSFactor) return false;
		
		if(depthTest != other.depthTest) return false;
		
		if(useCullFace != other.useCullFace) return false;
		if(frontFace != other.frontFace) return false;
		if(cullFace != other.cullFace) return false;
		
		if(useTexture1D != other.useTexture1D) return false;
		if(useTexture2D != other.useTexture2D) return false;
		if(useTexture3D != other.useTexture3D) return false;
		
		if(shaderModel != other.shaderModel) return false;
		if(shaderModelMode != other.shaderModelMode) return false;
		
		return true;
	}
	
	public String toString() {
		return "RenderProperties [renderStep=" + renderStep + ", useCullFace=" + useCullFace + ", depthTest="
				+ depthTest + ", useTexture1D=" + useTexture1D
				+ ", useTexture2D=" + useTexture2D + ", useTexture3D="
				+ useTexture3D + ", useBlend=" + useBlend + "]";
	}

	protected RenderProperties clone() {
		RenderProperties clone = new RenderProperties();
		clone.renderStep = renderStep;
		
		clone.useBlend = useBlend;
		clone.blendDFactor = blendDFactor;		
		clone.blendSFactor = blendSFactor;
		
		clone.depthTest = depthTest;
		clone.useTexture1D = useTexture1D;
		clone.useTexture2D = useTexture2D;
		clone.useTexture3D = useTexture3D;
		
		clone.useCullFace = useCullFace;
		clone.frontFace = frontFace;
		clone.cullFace = cullFace;
		
		clone.shaderModel = shaderModel;
		clone.shaderModelMode = shaderModelMode;
		
		return clone;
	}
	
	public RenderStep getRenderStep() { return renderStep; }
}
