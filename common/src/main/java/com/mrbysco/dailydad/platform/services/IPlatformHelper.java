package com.mrbysco.dailydad.platform.services;

import com.mrbysco.dailydad.config.JokeEnum;
import com.mrbysco.dailydad.jokes.JokeResolved;

import java.util.List;

public interface IPlatformHelper {

	/**
	 * Gets the internal database of jokes for in case no connection is available.
	 *
	 * @return A list of jokes from the config
	 */
	List<? extends String> getInternalDadabase();

	/**
	 * Gets the configured JokeType from the config
	 *
	 * @return The configured JokeType config option
	 */
	JokeEnum getJokeType();

	/**
	 * Should a joke be told upon death?
	 *
	 * @return the jokeUponRespawn config option
	 */
	boolean getJokeUponRespawn();

	/**
	 * Gets a joke from the online dadabase (Asynchronous)
	 *
	 * @param resolved
	 */
	void getJokeAsync(JokeResolved resolved);
}
