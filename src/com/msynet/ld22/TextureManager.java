package com.msynet.ld22;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class TextureManager {
	
	public static final String PlayerTextureName = "player";	
	public static final String OtherMinerTexture = "otherminer";
	public static final String DeadMinerTexture = "deadminer";

	public static final String BackgroundTexture = "background";
	public static final String TreasureTexture = "treasure";
	public static final String MinedTreasureTexture = "minedtreasure";
	public static final String SuperTreasureTexture = "supertreasure";
	
	public Map<String, Texture> map = new HashMap<String, Texture>();
	
	public void initTextures() {
		try {
		
			map.put(PlayerTextureName, TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("player.png")));
			map.put(OtherMinerTexture, TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("otherminer.png")));
			map.put(BackgroundTexture, TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("bg.png")));
			map.put(TreasureTexture, TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("treasure.png")));
			map.put(SuperTreasureTexture, TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("supertreasure.png")));
			map.put(DeadMinerTexture, TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("deadminer.png")));
			map.put(MinedTreasureTexture, TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("minedtreasure.png")));

					
		} catch (IOException e) {
			
			e.printStackTrace();
		}	
	}
	
}
