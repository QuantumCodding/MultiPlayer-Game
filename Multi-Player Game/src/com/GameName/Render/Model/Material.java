package com.GameName.Render.Model;

import com.GameName.Render.Effects.Texture;

public class Material {
	private float specularCoefficient;
	
    private float[] ambientColour;
    private float[] diffuseColour;
    private float[] specularColour;
    
    private Texture texture;

    public Material() {}
    
	public Material(float specularCoefficient, float[] ambientColour, float[] diffuseColour, float[] specularColour, Texture texture) {
		this.specularCoefficient = specularCoefficient;
		this.ambientColour = ambientColour;
		this.diffuseColour = diffuseColour;
		this.specularColour = specularColour;
		this.texture = texture;
	}

	public float getSpecularCoefficient() {
		return specularCoefficient;
	}

	public float[] getAmbientColour() {
		return ambientColour;
	}

	public float[] getDiffuseColour() {
		return diffuseColour;
	}

	public float[] getSpecularColour() {
		return specularColour;
	}

	public Texture getTexture() {
		return texture;
	}

	public void setSpecularCoefficient(float specularCoefficient) {
		this.specularCoefficient = specularCoefficient;
	}

	public void setAmbientColour(float[] ambientColour) {
		this.ambientColour = ambientColour;
	}

	public void setDiffuseColour(float[] diffuseColour) {
		this.diffuseColour = diffuseColour;
	}

	public void setSpecularColour(float[] specularColour) {
		this.specularColour = specularColour;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}
}
