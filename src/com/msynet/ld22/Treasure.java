package com.msynet.ld22;

import javax.vecmath.Vector3f;

public class Treasure extends Entity {

	public boolean superTreasure = false;
	public long    showAfterMs = 0;
	public boolean mined = false;
	public boolean mining = false;
	public boolean shown = false;
	
	@Override
	public String getTextureName() {
		if(mined) { 			
			return TextureManager.MinedTreasureTexture;
		} else {

			if(superTreasure) {
				return TextureManager.SuperTreasureTexture;
			} else {
				return TextureManager.TreasureTexture;
			}
		}
	}
	
	@Override
	public Vector3f getColor() {
		return new Vector3f(1.0f, 1.0f, 1.0f);
	}

	
}
