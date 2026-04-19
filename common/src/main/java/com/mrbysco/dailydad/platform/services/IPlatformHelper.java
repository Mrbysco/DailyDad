package com.mrbysco.dailydad.platform.services;

import com.mrbysco.dailydad.jokes.JokeResolved;

public interface IPlatformHelper {
	/**
	 * Gets a joke from the online dadabase (Asynchronous)
	 *
	 * @param resolved The callback to be called when the joke is resolved
	 */
	void getJokeAsync(JokeResolved resolved);
}
