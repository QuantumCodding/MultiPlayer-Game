/**
 * 
 */
package com.GameName.Main;

import com.GameName.Engine.GameName_New;
import com.GameName.Render.Effects.ShaderRegistry;
import com.GameName.Render.Effects.TextureRegistry;
import com.GameName.Render.Window.Window;

/**
 * @author QuantumCoding
 *
 */

public class Start_New {
	
	private static GameName_New gameName;
	
	public static void main(String... args) {
		gameName = new GameName_New();
		
		gameName.preInit();
		gameName.init();
		gameName.postInit();
		System.out.println("OpenGL Version: " + Window.getOpenGLVersion());
				
		try {
			gameName.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		cleanUp();		
	}
	
	public GameName_New getGameName() {return gameName;}
	
	public static void cleanUp() {
		try {
			gameName.cleanUp();
			
			TextureRegistry.cleanUp();
			ShaderRegistry.cleanUp();
			
		} catch(NullPointerException e) {
			e.printStackTrace();
			
		} finally {		
//			Logger.saveLog(new File());			
			System.exit(0);
		}
	}
}
