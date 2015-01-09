package com.GameName.Render.Model;

import static org.lwjgl.opengl.GL11.GL_FLAT;
import static org.lwjgl.opengl.GL11.GL_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glShadeModel;

import java.util.ArrayList;
import java.util.HashMap;

import javax.vecmath.TexCoord2f;

import com.GameName.Util.Vectors.MathVec3f;


public class ModelData {

    private ArrayList<MathVec3f> vertices;
    private ArrayList<TexCoord2f> textureCoordinates;
    private ArrayList<MathVec3f> normals;
    
    private ArrayList<Face> faces;
    
    private HashMap<String, Material> materials;
    private boolean enableSmoothShading;

    public ModelData() {
    	vertices = new ArrayList<>();
    	textureCoordinates = new ArrayList<>();
    	normals = new ArrayList<>();
    	
    	faces = new ArrayList<>();
    	materials = new HashMap<>();
    	
    	enableSmoothShading = true;
    }
    
    public void enableStates() {
        if (hasTextureCoordinates()) {
            glEnable(GL_TEXTURE_2D);
        }
        
        if (isSmoothShadingEnabled()) {
            glShadeModel(GL_SMOOTH);
        } else {
            glShadeModel(GL_FLAT);
        }
    }

    public boolean hasTextureCoordinates() {
        return getTextureCoordinates().size() > 0;
    }

    public boolean hasNormals() {
        return getNormals().size() > 0;
    }

    public ArrayList<MathVec3f> getVertices() {
        return vertices;
    }

    public ArrayList<TexCoord2f> getTextureCoordinates() {
        return textureCoordinates;
    }

    public ArrayList<MathVec3f> getNormals() {
        return normals;
    }

    public ArrayList<Face> getFaces() {
        return faces;
    }

    public boolean isSmoothShadingEnabled() {
        return enableSmoothShading;
    }

    public void setSmoothShadingEnabled(boolean smoothShadingEnabled) {
        this.enableSmoothShading = smoothShadingEnabled;
    }

    public HashMap<String, Material> getMaterials() {
        return materials;
    }
}