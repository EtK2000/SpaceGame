package com.etk2000.GameHandling;

import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.util.ResourceLoader;

public class SoundHandler {
	private static ArrayList<Audio> sounds = new ArrayList<Audio>();
	/** The ogg sound effect */
	private Audio oggEffect;
	/** The wav sound effect */
	private Audio wavEffect;
	/** The aif source effect */
	private Audio aifEffect;
	/** The ogg stream thats been loaded */
	private Audio oggStream;
	/** The mod stream thats been loaded */
	private Audio modStream;

	public static Audio playWav(String wavFile, boolean loop) {
		try {
			AudioLoader.update();// JIC
			Audio wav = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream(wavFile));
			sounds.add(wav);
			wav.playAsMusic(1.0f, 1.0f, loop);
			return wav;
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Initialise resources
	 */
	public void init() {
		try {
			wavEffect = AudioLoader.getAudio("WAV",
					ResourceLoader.getResourceAsStream("com/etk2000/SpaceGame/Music/MainMenuMusic.wav"));
			// @param: something, something, loop
			wavEffect.playAsMusic(1.0f, 1.0f, true);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}

		/*
		 * try {
		 * // you can play oggs by loading the complete thing into
		 * // a sound
		 * oggEffect = AudioLoader.getAudio("OGG",
		 * ResourceLoader.getResourceAsStream("testdata/restart.ogg"));
		 * 
		 * // or setting up a stream to read from. Note that the argument becomes
		 * // a URL here so it can be reopened when the stream is complete. Probably
		 * // should have reset the stream by thats not how the original stuff worked
		 * oggStream = AudioLoader.getStreamingAudio("OGG",
		 * ResourceLoader.getResource("testdata/bongos.ogg"));
		 * 
		 * // can load mods (XM, MOD) using ibxm which is then played through OpenAL. MODs
		 * // are always streamed based on the way IBXM works
		 * modStream = AudioLoader.getStreamingAudio("MOD",
		 * ResourceLoader.getResource("testdata/SMB-X.XM"));
		 * 
		 * // playing as music uses that reserved source to play the sound. The first
		 * // two arguments are pitch and gain, the boolean is whether to loop the content
		 * modStream.playAsMusic(1.0f, 1.0f, true);
		 * 
		 * // you can play aifs by loading the complete thing into
		 * // a sound
		 * aifEffect = AudioLoader.getAudio("AIF",
		 * ResourceLoader.getResourceAsStream("testdata/burp.aif"));
		 * 
		 * // you can play wavs by loading the complete thing into
		 * // a sound
		 * wavEffect = AudioLoader.getAudio("WAV",
		 * ResourceLoader.getResourceAsStream("testdata/cbrown01.wav"));
		 * } catch (Exception e) {
		 * e.printStackTrace();
		 * }
		 */
	}

	/**
	 * Game loop update
	 */
	public void update() {
		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				if (Keyboard.getEventKey() == Keyboard.KEY_Q) {
					// play as a one off sound effect
					oggEffect.playAsSoundEffect(1.0f, 1.0f, false);
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_W) {
					// replace the music thats curretly playing with
					// the ogg
					oggStream.playAsMusic(1.0f, 1.0f, true);
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_E) {
					// replace the music thats curretly playing with
					// the mod
					modStream.playAsMusic(1.0f, 1.0f, true);
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_R) {
					// play as a one off sound effect
					aifEffect.playAsSoundEffect(1.0f, 1.0f, false);
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_T) {
					// play as a one off sound effect
					wavEffect.playAsSoundEffect(1.0f, 1.0f, false);
				}
			}
		}

		// polling is required to allow streaming to get a chance to
		// queue buffers.
		SoundStore.get().poll(0);
	}
}