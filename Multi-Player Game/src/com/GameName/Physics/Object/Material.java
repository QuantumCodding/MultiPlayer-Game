package com.GameName.Physics.Object;

public class Material {
	private float density;		
	private float elasticity;
	
	private float meltingPoint, boilingPoint;
	private Material meltsTo, boilsTo;
	
	private boolean isConductor;
	private float resistance;
	
	/** Coefficient Of Friction*/
	private float cof;
	
	private Phase phase;	
	private String name;

	public Material(float density, float cof, Phase phase, String name) {
		this.density = density;
		this.cof = cof;		
		this.phase = phase;
		this.name = name;
	}

	public Material setElasticity(float elasticity) {
		this.elasticity = elasticity; return this;
	}	
	
	public Material addMeltingPoint(float meltingPoint, Material meltsTo) {
		this.meltingPoint = meltingPoint; this.meltsTo = meltsTo; return this;
	}
	
	public Material addBoilingPoint(float boilingPoint, Material boilsTo) {
		this.boilingPoint = boilingPoint; this.boilsTo = boilsTo; return this;
	}
	
	public Material makeConductor(float resistance) {
		this.resistance = resistance; isConductor = true; return this;
	}
	
	public float getDensity() {return density;}
	public float getElasticity() {return elasticity;}
	
	public float getMeltingPoint() {return meltingPoint;}
	public float getBoilingPoint() {return boilingPoint;}

	public Material melt() {return meltsTo;}
	public Material boil() {return boilsTo;}
	
	public boolean isConductor() {return isConductor;}
	public float getResistance() {return resistance;}
	
	public float getFrictionalCoefficient() {return cof;}
	
	public Phase getPhase() {return phase;}
	public String getName() {return name;}
	
	@Override
	public String toString() {
		return "Material [density=" + density + ", elasticity=" + elasticity
				+ ", meltingPoint=" + meltingPoint + ", boilingPoint="
				+ boilingPoint + ", meltsTo=" + meltsTo + ", boilsTo="
				+ boilsTo + ", isConductor=" + isConductor + ", resistance="
				+ resistance + ", cof=" + cof + ", phase=" + phase + ", name="
				+ name + "]";
	}

	public enum Phase {
		Solid, Liquid, Gas;
	}
}
