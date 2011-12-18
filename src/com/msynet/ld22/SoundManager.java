package com.msynet.ld22;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;

public class SoundManager {

	public static final String MinerKillSound = "minerkill";	
	public static final String MiningSound = "mining";
	public static final String MinedSound = "mined";
	public static final String HitTreasureSound = "hittreasure";
	
	public Map<String, Audio> map = new HashMap<String, Audio>();

	public void initSounds() {
		try {

			map.put(MinerKillSound,  AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("ugh.wav")));
			map.put(MiningSound,  AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("mining.wav")));
			map.put(HitTreasureSound,  AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("hittreasure.wav")));

		} catch (IOException e) {

			e.printStackTrace();
		}	
	}
	
	public void playSound(String sound) {
		map.get(sound).playAsSoundEffect(1.0f, 1.0f, false);
	}

}
