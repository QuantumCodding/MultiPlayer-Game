package com.GameName.Render.Model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.GameName.Render.Effects.Texture2D;
import com.GameName.Util.Vectors.Vector2f;
import com.GameName.Util.Vectors.Vector3f;

public class OBJLoader {
	 public static ModelData loadTexturedModel(File f) throws IOException {
	        BufferedReader reader = new BufferedReader(new FileReader(f));
	        ModelData m = new ModelData();
	        Material currentMaterial = new Material();
	        
	        String line;
	        while ((line = reader.readLine()) != null) {
	            if (line.startsWith("#")) {
	                continue;
	            }
	            
	            if (line.startsWith("mtllib ")) {
	                String materialFileName = line.split(" ")[1];
	                
	                File materialFile = new File(f.getParentFile().getAbsolutePath() + "/" + materialFileName);
	                BufferedReader materialFileReader = new BufferedReader(new FileReader(materialFile));
	                String materialLine;
	                
	                Material parseMaterial = new Material();
	                String parseMaterialName = "";
	                
	                while ((materialLine = materialFileReader.readLine()) != null) {
	                    if (materialLine.startsWith("#")) {
	                        continue;
	                    }
	                    
	                    if (materialLine.startsWith("newmtl ")) {
	                        if (!parseMaterialName.equals("")) {
	                            m.getMaterials().put(parseMaterialName, parseMaterial);
	                        }
	                        
	                        parseMaterialName = materialLine.split(" ")[1];
	                        parseMaterial = new Material();
	                        
	                    } else if (materialLine.startsWith("Ns ")) {                    	
	                        parseMaterial.setSpecularCoefficient(Float.valueOf(materialLine.split(" ")[1]));
	                        
	                    } else if (materialLine.startsWith("Ka ")) {
	                        String[] rgb = materialLine.split(" ");
	                        
	                        parseMaterial.getAmbientColour()[0] = Float.valueOf(rgb[1]);
	                        parseMaterial.getAmbientColour()[1] = Float.valueOf(rgb[2]);
	                        parseMaterial.getAmbientColour()[2] = Float.valueOf(rgb[3]);
	                        
	                    } else if (materialLine.startsWith("Ks ")) {
	                        String[] rgb = materialLine.split(" ");
	                        
	                        parseMaterial.getSpecularColour()[0] = Float.valueOf(rgb[1]);
	                        parseMaterial.getSpecularColour()[1] = Float.valueOf(rgb[2]);
	                        parseMaterial.getSpecularColour()[2] = Float.valueOf(rgb[3]);
	                        
	                    } else if (materialLine.startsWith("Kd ")) {
	                        String[] rgb = materialLine.split(" ");
	                        
	                        parseMaterial.getDiffuseColour()[0] = Float.valueOf(rgb[1]);
	                        parseMaterial.getDiffuseColour()[1] = Float.valueOf(rgb[2]);
	                        parseMaterial.getDiffuseColour()[2] = Float.valueOf(rgb[3]);
	                        
	                    } else if (materialLine.startsWith("map_Kd")) {
	                        parseMaterial.setTexture(new Texture2D(
	                        		ImageIO.read(new File(f.getParentFile().getAbsolutePath() + "/" + materialLine.split(" ")[1])), false
	                        	));
	                        
	                    } else {
	                        System.err.println("[MTL] Unknown Line: " + materialLine);
	                    }
	                }
	                
	                m.getMaterials().put(parseMaterialName, parseMaterial);
	                materialFileReader.close();
	                
	            } else if (line.startsWith("usemtl ")) {
	                currentMaterial = m.getMaterials().get(line.split(" ")[1]);
	                
	            } else if (line.startsWith("v ")) {
	                String[] xyz = line.split(" ");
	                
	                float x = Float.valueOf(xyz[1]);
	                float y = Float.valueOf(xyz[2]);
	                float z = Float.valueOf(xyz[3]);
	                
	                m.getVertices().add(new Vector3f(x, y, z));
	                
	            } else if (line.startsWith("vn ")) {
	                String[] xyz = line.split(" ");
	                
	                float x = Float.valueOf(xyz[1]);
	                float y = Float.valueOf(xyz[2]);
	                float z = Float.valueOf(xyz[3]);
	                
	                m.getNormals().add(new Vector3f(x, y, z));
	                
	            } else if (line.startsWith("vt ")) {
	                String[] xyz = line.split(" ");
	                
	                float s = Float.valueOf(xyz[1]);
	                float t = Float.valueOf(xyz[2]);
	                
	                m.getTextureCoordinates().add(new Vector2f(s, t));
	                
	            } else if (line.startsWith("f ")) {
	                String[] faceIndices = line.split(" ");
	                
	                int[] vertexIndicesArray = {
	                		Integer.parseInt(faceIndices[1].split("/")[0]),
	                		Integer.parseInt(faceIndices[2].split("/")[0]), 
	                		Integer.parseInt(faceIndices[3].split("/")[0])
	                	};
	                int[] textureCoordinateIndicesArray = {-1, -1, -1};
	                
	                if (m.hasTextureCoordinates()) {
	                    textureCoordinateIndicesArray[0] = Integer.parseInt(faceIndices[1].split("/")[1]);
	                    textureCoordinateIndicesArray[1] = Integer.parseInt(faceIndices[2].split("/")[1]);
	                    textureCoordinateIndicesArray[2] = Integer.parseInt(faceIndices[3].split("/")[1]);
	                }
	                
	                int[] normalIndicesArray = {0, 0, 0};
	                
	                if (m.hasNormals()) {
	                    normalIndicesArray[0] = Integer.parseInt(faceIndices[1].split("/")[2]);
	                    normalIndicesArray[1] = Integer.parseInt(faceIndices[2].split("/")[2]);
	                    normalIndicesArray[2] = Integer.parseInt(faceIndices[3].split("/")[2]);
	                }
	               
	                m.getFaces().add(
	                		new Face(vertexIndicesArray, normalIndicesArray, textureCoordinateIndicesArray, currentMaterial)
	                	);
	                
	            } else if (line.startsWith("s ")) {
	                boolean enableSmoothShading = !line.contains("off");
	                m.setSmoothShadingEnabled(enableSmoothShading);
	                
	            } else {
	                System.err.println("[OBJ] Unknown Line: " + line);
	            }
	        }
	        
	        reader.close();
	        return m;
	    }
}
