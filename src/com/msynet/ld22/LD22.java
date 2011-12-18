package com.msynet.ld22;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Font;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

import com.msynet.ld22.Miner.Action;

@SuppressWarnings("deprecation")
public class LD22 {
	
	private final int width = 640;
	private final int height = 480;
	
	private Player player;
	private List<Miner> otherMiners = new ArrayList<Miner>();
	private List<Treasure> treasures = new ArrayList<Treasure>();
	
	private Treasure superTreasure;
	
	private long elapsedTime = 0;
	public static final long MaxGameTime = 20000;
	
	private TextureManager textureManager;
	private SoundManager soundManager;

	public long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	
	
	public void DrawBackground() {
		Texture texture = textureManager.map.get(TextureManager.BackgroundTexture);
		texture.bind();
		
		glBegin(GL_QUADS);
		glTexCoord2f(0.0f, 0.0f);
	    glVertex3f(0.0f, height, -0.1f);
	    glTexCoord2f(0.625f, 0.0f);
	    glVertex3f(width, height, -0.1f);
	    glTexCoord2f(0.625f, 0.46875f);
	    glVertex3f(width, 0.0f, -0.1f);
	    glTexCoord2f(0.0f, 0.46875f);
	    glVertex2f(0.0f, 0.0f);
	    
	    glEnd();
	}
	
	public void DrawEntity(Entity ent) {
		Texture texture = textureManager.map.get(ent.getTextureName());
		texture.bind();
		
		glPushMatrix();
		glLoadIdentity();
	
		glTranslatef(ent.pos.x, ent.pos.y, 0.0f);
	//	glRotatef(ent.heading, 0.0f, 0.0f, 1.0f);
		
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
		player = new Player();
		player.alive = true;
		player.pos = Entity.getRandomPoint(width, height);
		player.cdem = new Vector2f(48.0f, 64.0f);
	
		
		for(int i=0; i < 5; i++) {	
			Miner miner = new Miner();
			miner.alive = true;
			miner.speed = 160.0f;
			miner.pos = Entity.getRandomPoint(width, height);
			miner.cdem = new Vector2f(34.0f, 64.0f);
		
			miner.currentAction = Action.Searching;
			otherMiners.add(miner);			
		}		
		
		for(int i=0; i < 9; i++) {	
			Treasure treasure = new Treasure();
			treasure.pos = Entity.getRandomPoint(width, height);
			treasure.showAfterMs = 0;
			treasure.cdem = new Vector2f(34.0f, 64.0f);
			
			treasures.add(treasure);			
		}	
		
		superTreasure = new Treasure();
		superTreasure.pos = Entity.getRandomPoint(width, height);
		superTreasure.superTreasure = true;
		superTreasure.showAfterMs = 15000;
	}
	
	public Treasure collidingWithTreasure(Entity ent) {
		for(Treasure treasure: treasures) {
			if(ent.intersects(treasure)) {
				return treasure;
			}
		}
		return null;
	}
	
	public Miner collidingWithMiner(Entity ent) {
		for(Miner miner: otherMiners) {
			if(ent.intersects(miner)) {
				return miner;
			}
		}
		return null;
	}

	public boolean collidingWithPlayer(Entity ent) {		
		return player.intersects(ent);
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
		
		soundManager = new SoundManager();
		soundManager.initSounds();
		
		initEntities();
		
		glEnable(GL_TEXTURE_2D);
		glTexParameterf( GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP );
		glTexParameterf( GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP );
		
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
		
    	long lastTime, currTime, startTime; 
    	lastTime = currTime = startTime = getTime();
    	while (!Display.isCloseRequested()) {
    		    		
    		currTime = getTime();
    		long delta = currTime - lastTime;
    		lastTime = currTime;
    		
    		elapsedTime = currTime - startTime;
    		
    		player.speed = 0.0f;
    		if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
    			player.heading = 0.0f;
    			player.speed = Player.Speed;
    		}
    		else if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
    			player.heading = 180.0f;
    			player.speed = Player.Speed;
    		}
    		else if(Keyboard.isKeyDown(Keyboard.KEY_UP)) {
    			player.heading = 270.0f;
    			player.speed = Player.Speed;
    		}
    		else if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
    			player.heading = 90.0f;
    			player.speed = Player.Speed;
    		}
    		
    		while(Keyboard.next()) {
    			if(Keyboard.getEventKey() == Keyboard.KEY_SPACE && Keyboard.getEventKeyState()) {
    				System.out.println("SHANK!");
    				Miner miner = this.collidingWithMiner(player);
    				if(miner != null) {
    					miner.alive = false;
    					soundManager.playSound(SoundManager.MinerKillSound);
    				}
    			}    			
    		}
    		
    		player.update(delta);
    		for(Miner miner: otherMiners) {
    			
    			if(miner.alive) {
    				miner.update(delta);
    				if(miner.currentAction == Action.Searching) {
        				Treasure treasure = this.collidingWithTreasure(miner);
    					if(treasure != null && !treasure.mined) {
    						miner.currentAction = Action.Mining;
    						miner.miningTreasure = treasure;
    					
    					}
    				} else if(miner.currentAction == Action.Mining) {
    					if(miner.msMined >= 2000) {
    						miner.miningTreasure.mined = true;
    						miner.msMined = 0;
    						miner.currentAction = Action.Searching;
    						soundManager.playSound(SoundManager.MiningSound);
    					}
    				}
    			}
       		}
    		
     		glClear(GL_COLOR_BUFFER_BIT);
     		DrawBackground();
       		
       		for(Treasure treasure: treasures) {
       			if(elapsedTime > treasure.showAfterMs) {
       				DrawEntity(treasure);
       			}
       		}
       		
       		if(elapsedTime >= superTreasure.showAfterMs) {
       			DrawEntity(superTreasure);
       		}
       		
       		DrawEntity(player);
       		
       		for(Miner miner: otherMiners) {
       			DrawEntity(miner);
       			if(player.intersects(miner)) {
       			
       			}
       		}
       		
       		if(elapsedTime > MaxGameTime) {
       			
       			ttFont.drawString(0, 0, "Game Over", Color.white);
       		}
       		
       		long secRemaining = (MaxGameTime - elapsedTime) / 1000;
       	  	
      		ttFont.drawString(0, 0, "!Alone - Time Remaining: " + Long.toString(secRemaining), Color.white);
    		
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
