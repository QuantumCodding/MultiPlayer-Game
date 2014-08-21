/**
 * 
 */
package com.GameName.Main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Controllers;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.openal.AL;

import com.GameName.World.Cube.Cube;
//import static org.lwjgl.opengl.GL15.*;

/**
 * @author Soren
 *
 */

public class Start {
	public static final String vertion = "0.00.3";
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	
	public static final InputStream defualt_in = System.in;
	public static final PrintStream defualt_err = System.err;
	public static final PrintStream defualt_out = System.out;
	
	public static void main(String[] args) {
		Start start = new Start();
		
		if(args.length > 0) {
			try {
				new File(args[0] + "/ErrorOut.txt").createNewFile();
				new File(args[0] + "/Out.txt").createNewFile();
				new File(args[0] + "/In.txt").createNewFile();
				
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			try {
				System.setErr(new PrintStream(new File(args[0] + "/ErrorOut.txt")));
				System.setOut(new PrintStream(new File(args[0] + "/Out.txt")));
				System.setIn(new FileInputStream(new File(args[0] + "/In.txt")));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		initDisplay(WIDTH, HEIGHT);
		start.intiController();
		
		try {
			new GameName().gameLoop();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		cleanUp();		
	}
	
	private void intiController() {
		try {
			Controllers.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}
	
	protected static void initDisplay(int width, int height) {
		try {
			Display.destroy();
			AL.destroy();
			Mouse.destroy();
			Keyboard.destroy();
			
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.setTitle("Multi-Player Game V: " + vertion);
//			Display.setResizable(true);
			
//			ByteBuffer[] icon = null;//			
//			Display.setIcon(icon);
			
			Display.create();
			AL.create();
			Mouse.create();
			Keyboard.create();
			
			if(GameName.render != null) {
				GameName.render.setUpOpenGL();
			}
		} catch (LWJGLException e) {			
			e.printStackTrace();
			cleanUp();
		}
	}
	
	public static void cleanUp() {	
		
		if(GameName.sound != null)
			GameName.sound.cleanUp();
		
		if(GameName.render != null) 
			GameName.render.cleanUp();
		
		if(Cube.isConcluded())
			Cube.cleanUp();
		
		Display.destroy();
		
		Controllers.destroy();
		Mouse.destroy();
		Keyboard.destroy();
		
		AL.destroy();
		
		System.exit(1);
	}
}
