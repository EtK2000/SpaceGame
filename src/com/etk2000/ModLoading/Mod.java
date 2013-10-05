package com.etk2000.ModLoading;

public class Mod {
	private final String name;
	private final float version;

	public Mod(String modName, float modVer) {
		this.name = modName;
		this.version = modVer;
	}

	public void Init() throws Exception {//TODO: see if "throws" is needed
		// Override This In Your Mod, Run ALL The Functions/Setup From Here!
	}
	
	public void Exit() throws Exception {//TODO: see if "throws" is needed
		// Override This In Your Mod, Run ALL The Functions/Setup From Here!
	}

	/** Getters **/
	public final String getName() {
		return name;
	}
	
	public final float getVersion() {
		return version;
	}
}