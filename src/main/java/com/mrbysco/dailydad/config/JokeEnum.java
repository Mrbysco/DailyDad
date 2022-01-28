package com.mrbysco.dailydad.config;

public enum JokeEnum {
	LOADING(false),
	CHAT(true),
	TTS(false);

	private final boolean withName;

	JokeEnum(boolean withName) {
		this.withName = withName;
	}

	public boolean isWithName() {
		return withName;
	}
}
