package com.GameName.Render.Model;

import static org.lwjgl.opengl.GL11.GL_FLAT;
import static org.lwjgl.opengl.GL11.GL_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glShadeModel;

import java.util.ArrayList;
import java.util.HashMap;

import com.GameName.Util.Vectors.Vector2f;
import com.GameName.Util.Vectors.Vector3f;


public class ModelData {

    private ArrayList<Vector3f> vertices;
    private ArrayList<Vector2f> textureCoordinates;
    private ArrayList<Vector3f> normals;
    
    private ArrayList<Face> faces;
    
    private HashMap<String, Material> materials;
    private boolean enableSmoothShading;

    public ModelData() {
    	vertices = new ArrayList<Vector3f>();
    	textureCoordinates = new ArrayList<Vector2f>();
    	normals = new ArrayList<Vector3f>();
    	
    	faces = new ArrayList<Face>();
    	materials = new HashMap<String, Material>();
    	
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

    public ArrayList<Vector3f> getVertices() {
        return vertices;
    }

    public ArrayList<Vector2f> getTextureCoordinates() {
        return textureCoordinates;
    }

    public ArrayList<Vector3f> getNormals() {
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