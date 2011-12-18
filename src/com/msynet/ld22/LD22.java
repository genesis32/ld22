package com.msynet.ld22;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Font;
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
import org.newdawn.slick.opengl.Texture;

import com.msynet.ld22.Miner.MinerAction;
import com.msynet.ld22.Player.PlayerAction;

@SuppressWarnings("deprecation")
public class LD22 {
	
	private final int width = 800;
	private final int height = 600;
	
	TrueTypeFont statFont = null;
	TrueTypeFont minerFont = null;
	
	private Player player;
	private List<Miner> otherMiners = new ArrayList<Miner>();
	private List<Treasure> treasures = new ArrayList<Treasure>();
	
	private Treasure superTreasure;
	
	private long elapsedTime = 0;
	public static final long MaxGameTime = 30000;
	
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
		glColor3f(1.0f, 1.0f, 1.0f);		
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
	    
	    if(ent instanceof Miner) {
	    	Miner miner = (Miner)ent;
	    	minerFont.drawString(ent.pos.x, ent.pos.y, "NT: " + miner.treasureMined, Color.red);
	    	
	    	if(miner.hasKey) {
	    		minerFont.drawString(ent.pos.x, ent.pos.y+20, "K", Color.yellow);
	    	}
	    	
	    } else if(ent instanceof Player) {
	    	Player player = (Player)ent;
	    	minerFont.drawString(ent.pos.x, ent.pos.y, "NT: " + player.treasureMined, Color.green);
	    }
	}
	
	public void initEntities () {
		player = new Player();
		player.alive = true;
		player.pos = Entity.getRandomPoint(width, height);
		player.currentAction = PlayerAction.Searching;
		player.cdem = new Vector2f(64.0f, 64.0f);
	
		
		for(int i=0; i < 4; i++) {	
			Miner miner = new Miner();
			miner.alive = true;
			miner.speed = 160.0f;
			miner.pos = Entity.getRandomPoint(width, height);
			miner.cdem = new Vector2f(34.0f, 64.0f);
		
			miner.currentAction = MinerAction.Searching;
			otherMiners.add(miner);			
		}		
		
		for(int i=0; i < 17; i++) {	
			Treasure treasure = new Treasure();
			treasure.pos = Entity.getRandomPoint(width, height);
			treasure.showAfterMs = 0;
			treasure.cdem = new Vector2f(34.0f, 64.0f);
			
			treasures.add(treasure);			
		}	
		
		superTreasure = new Treasure();
		superTreasure.pos = Entity.getRandomPoint(width, height);
		superTreasure.superTreasure = true;
		superTreasure.showAfterMs = 20000;
		treasures.add(superTreasure);
	}
	
	public Treasure collidingWithTreasure(Entity ent) {
		for(Treasure treasure: treasures) {
			if(treasure.shown && ent.intersects(treasure)) {
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
	
	public int numMinersAlive() {
		int res = 0;
		for(Miner miner: otherMiners) {
			if(miner.alive) {
				res++;
			}
		}
		return res;
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
    	
    	Font awtFont1 = new Font("Arial", Font.BOLD, 24);
    	statFont = new TrueTypeFont(awtFont1, true);
    	
    	Font awtFont2 = new Font("Arial", Font.PLAIN, 8);
    	minerFont = new TrueTypeFont(awtFont2, true);
		
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
    			player.currentAction = PlayerAction.Searching;
    		}
    		else if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
    			player.heading = 180.0f;
    			player.speed = Player.Speed;
    			player.currentAction = PlayerAction.Searching;
    		}
    		else if(Keyboard.isKeyDown(Keyboard.KEY_UP)) {
    			player.heading = 270.0f;
    			player.speed = Player.Speed;
    			player.currentAction = PlayerAction.Searching;
    		}
    		else if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
    			player.heading = 90.0f;
    			player.speed = Player.Speed;
    			player.currentAction = PlayerAction.Searching;
    		}
    		
    		while(Keyboard.next()) {
    			
    			if(Keyboard.getEventKey() == Keyboard.KEY_SPACE && Keyboard.getEventKeyState()) {
    				Miner miner = this.collidingWithMiner(player);
    				if(miner != null && miner.alive) {
    					miner.alive = false;
    					player.treasureMined += miner.treasureMined;
    					miner.treasureMined = 0;
    					player.hasKey = miner.hasKey;
    					soundManager.playSound(SoundManager.MinerKillSound);
    				} else {
    					
    					Treasure treasure = this.collidingWithTreasure(player);
    					if(treasure != null && (!treasure.mined || !treasure.mining) && !treasure.superTreasure) {
    					
    						if(player.currentAction == PlayerAction.Searching) {
    							player.msMining = 0;
    							player.miningTreasure = treasure;
    							player.currentAction = PlayerAction.Mining;
    							treasure.mining = true;
    							soundManager.playSound(SoundManager.HitTreasureSound);

    						}
    					}
    					
    				}
    				
    			}    			
    		}
    		
    		player.update(delta);
    		if (player.currentAction == PlayerAction.Mining) {
				System.out.println(player.msMining);
				if(player.msMining >= 2000) {
					System.out.println("mined");
					player.miningTreasure.mined = true;
					player.miningTreasure = null;
					player.treasureMined++;
					player.currentAction = PlayerAction.Searching;
					soundManager.playSound(SoundManager.MiningSound);
				}
			}
    		    		
    		for(Miner miner: otherMiners) {
    			if(miner.alive) {
    				miner.update(delta);
    				if(miner.currentAction == MinerAction.Searching) {
        				Treasure treasure = this.collidingWithTreasure(miner);
    					if(treasure != null && (!treasure.mined || !treasure.mining)) {
    						miner.currentAction = MinerAction.Mining;
    						miner.msMined = 0;
    						miner.miningTreasure = treasure;
							treasure.mining = true;
							soundManager.playSound(SoundManager.HitTreasureSound);
    					}
    				} else if(miner.currentAction == MinerAction.Mining) {
    					if(miner.miningTreasure.mined) {
    						miner.currentAction = MinerAction.Searching;
    						miner.miningTreasure = null;
    					} else if(miner.msMined >= 2000) {
    						miner.miningTreasure.mined = true;
    						if(miner.miningTreasure.superTreasure) {
        						miner.treasureMined += 3;
        						miner.hasKey = true;
    						} else {
        						miner.treasureMined++;    							
    						}
    						miner.miningTreasure = null;
    						
    						miner.currentAction = MinerAction.Searching;
    						soundManager.playSound(SoundManager.MiningSound);
    					}
    				}
    			} else { 
    				if(miner.miningTreasure != null) {
    					miner.miningTreasure.mining = false;
    					miner.miningTreasure = null;
    				}
    			}
       		}
    		
     		glClear(GL_COLOR_BUFFER_BIT);
     		DrawBackground();
       		
       		for(Treasure treasure: treasures) {
       			if(elapsedTime > treasure.showAfterMs) {
       				treasure.shown = true;
       				if(treasure.superTreasure) {
       					for(Miner miner: otherMiners) {
       						miner.gotoPoint(treasure.pos);
       					}
       				}
       				DrawEntity(treasure);
       			}
       		}
       		
       		DrawEntity(player);
       		
       		for(Miner miner: otherMiners) {
       			DrawEntity(miner);
       		}
       		
       		if(elapsedTime > MaxGameTime) {
       			if(this.numMinersAlive() == 0 && !player.hasKey && superTreasure.shown) {
       				statFont.drawString(0, 440, "You Lost! - You are trapped and have no skills to open the key box!", Color.white);       				
       			} else {
       				boolean won = true;
       				for(Miner miner: otherMiners) {
       					if(miner.alive && miner.treasureMined > player.treasureMined) {
       						won = false;
       						statFont.drawString(0, 440, "You Lost! - Someone escaped with more treasure than you!", Color.white); 
       					}
       				}
       				
       				if(won) {
       					statFont.drawString(0, 440, "You Won! - Sometimes it's best to not be alone!", Color.white);
       				}
       			}
       		} else {
       			long secRemaining = (MaxGameTime - elapsedTime) / 1000;
          		statFont.drawString(0, 0, "!Alone - Time Remaining: " + Long.toString(secRemaining), Color.white);
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
