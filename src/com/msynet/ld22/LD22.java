package com.msynet.ld22;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Font;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector2f;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.util.ResourceLoader;

@SuppressWarnings("deprecation")
public class LD22 {
	
	private final int width = 800;
	private final int height = 600;
	
	private Entity player;
	private List<Entity> otherMiners = new ArrayList<Entity>();
	private List<Entity> treasures = new ArrayList<Entity>();
	
	private TextureManager textureManager;

	public long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	
	
	public void DrawBackground() {
		Texture texture = textureManager.map.get(TextureManager.BackgroundTexture);
		texture.bind();
		
		glBegin(GL_QUADS);
		glTexCoord2f(0.0f, 0.0f);
	    glVertex2f(0.0f, height);
	    glTexCoord2f(1.0f, 0.0f);
	    glVertex2f(width, height);
	    glTexCoord2f(1.0f, 1.0f);
	    glVertex2f(width, 0.0f);
	    glTexCoord2f(0.0f, 1.0f);
	    glVertex2f(0.0f, 0.0f);
	    
	    glEnd();
	}
	
	public void DrawEntity(Entity ent) {
		Texture texture = textureManager.map.get(ent.textureName);
		texture.bind();
		
		glPushMatrix();
		glLoadIdentity();
	
		glTranslatef(ent.pos.x, ent.pos.y, 0.0f);
		glRotatef(ent.rotAngle, 0.0f, 0.0f, 1.0f);
		
		glBegin(GL_QUADS);
		glTexCoord2f(0.0f, 0.0f);
	    glVertex2f(-ent.dem.x/2.0f, -ent.dem.y/2.0f);
	    glTexCoord2f(1.0f, 0.0f);
	    glVertex2f(ent.dem.x/2.0f, -ent.dem.y/2.0f);
	    glTexCoord2f(1.0f, 1.0f);
	    glVertex2f(ent.dem.x/2.0f, ent.dem.y/2.0f);
	    glTexCoord2f(0.0f, 1.0f);
	    glVertex2f(-ent.dem.x/2.0f, ent.dem.y/2.0f);
	   
	    glEnd();
	    
	    glPopMatrix();
	}
	
	public void initEntities () {
		player = new Entity();
		player.pos = Entity.getRandomPoint(width, height);
		player.cdem = new Vector2f(48.0f, 64.0f);
		player.textureName = TextureManager.PlayerTextureName;
		
		for(int i=0; i < 1; i++) {	
			Entity miner = new Entity();
			miner.pos = Entity.getRandomPoint(width, height);
			miner.cdem = new Vector2f(34.0f, 64.0f);
			miner.textureName = TextureManager.OtherMinerTexture;
			otherMiners.add(miner);			
		}		
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

		textureManager = new TextureManager();
		textureManager.initTextures();
		
		initEntities();
		
		glEnable(GL_TEXTURE_2D);
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);          
        
    	glEnable(GL_BLEND);
    	glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    
    	glViewport(0,0,width,height);
    	glMatrixMode(GL_MODELVIEW);

    	glMatrixMode(GL_PROJECTION);
    	glLoadIdentity();
    	glOrtho(0, width, height, 0, 1, -1);
    	glMatrixMode(GL_MODELVIEW);
    	
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
    		
    		player.update(delta);
    		for(Entity miner: otherMiners) {
       			miner.update(delta);
       		}
    		
    		if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
    			player.velocity.y = 0f;
    			player.velocity.x = 100f;
    		} else if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
    			player.velocity.y = 0f;
    			player.velocity.x = -100f;
    		}else if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
    			player.velocity.x = 0f;
    			player.velocity.y = -100f;
    		}else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
    			player.velocity.x = 0f;
    			player.velocity.y = 100f;    			
    		}
    		
     		glClear(GL_COLOR_BUFFER_BIT);
     		DrawBackground();
     		
       		DrawEntity(player);
       		
       		for(Entity miner: otherMiners) {
       			DrawEntity(miner);
       			if(player.intersects(miner)) {
       				System.out.println("OUCH");
       			}
       		}
       	  	
      		ttFont.drawString(0, 0, "Lonely Miner", Color.white);
    		
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
