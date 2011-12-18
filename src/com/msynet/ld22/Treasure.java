package com.msynet.ld22;

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
	
}
