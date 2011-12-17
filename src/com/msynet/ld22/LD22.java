package com.msynet.ld22;

import java.awt.Font;
import java.io.IOException;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;

@SuppressWarnings("deprecation")
public class LD22 {
	private final int width = 800;
	private final int height = 600;
	
	public long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	
	public void start() {
		try {
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.create();
			Display.setVSyncEnabled(true);
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}

		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);          
        
    	GL11.glEnable(GL11.GL_BLEND);
    	GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    
    	GL11.glViewport(0,0,width,height);
    	GL11.glMatrixMode(GL11.GL_MODELVIEW);

    	GL11.glMatrixMode(GL11.GL_PROJECTION);
    	GL11.glLoadIdentity();
    	GL11.glOrtho(0, width, height, 0, 1, -1);
    	GL11.glMatrixMode(GL11.GL_MODELVIEW);
    	
    	Font awtFont = new Font("Arial", Font.BOLD, 24);
		TrueTypeFont ttFont = new TrueTypeFont(awtFont, true);
	
		Audio wavEffect = null;
		try {
			wavEffect = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("hurt.wav"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	long lastTime, currTime;
    	lastTime = currTime = getTime();
    	while (!Display.isCloseRequested()) {
    		
    		currTime = getTime();
    		long delta = currTime - lastTime;
    		lastTime = currTime;
    		
    		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
    		ttFont.drawString(100, 50, "ABC", Color.yellow);
    		if(Keyboard.isKeyDown(Keyboard.KEY_UP)) {
    			GL11.glClearColor(1.0f, 0.0f, 0.0f, 0.0f);      
    			wavEffect.playAsSoundEffect(1f, 1f, false);
    		}
    		
    		Display.update();
    		Display.sync(60);
    	}
		
    	Display.destroy();
    }
	
    public static void main(String[] argv) {
    	LD22 display = new LD22();
    	display.start();
    }

}
