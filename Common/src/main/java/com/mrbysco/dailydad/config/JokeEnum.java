package com.mrbysco.dailydad.config;

import org.jetbrains.annotations.Nullable;

public enum JokeEnum {
	LOADING("LOADING"),
	CHAT("CHAT"),
	TTS("TTS");

	private final String name;

	JokeEnum(String name) {
		this.name = name;
	}

	@Nullable
	public static JokeEnum getByName(@Nullable String value) {
		for (JokeEnum jokeEnum : values()) {
			if (jokeEnum.name.equals(value)) {
				return jokeEnum;
			}
		}
		return CHAT;
	}
}
