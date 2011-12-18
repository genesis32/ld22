package com.msynet.ld22;

public class Player extends Entity {
	
	public static final float Speed = 200.0f;
	
	public long numTreasureMined = 0;

	@Override
	public String getTextureName() {
		return TextureManager.PlayerTextureName;
	}
	
}
