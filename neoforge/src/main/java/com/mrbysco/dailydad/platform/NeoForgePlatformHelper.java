package com.mrbysco.dailydad.platform;

import com.mrbysco.dailydad.jokes.DadAbase;
import com.mrbysco.dailydad.jokes.JokeResolved;
import com.mrbysco.dailydad.platform.services.IPlatformHelper;

public class NeoForgePlatformHelper implements IPlatformHelper {

	@Override
	public void getJokeAsync(JokeResolved resolved) {
		new Thread(() -> {
			String joke = DadAbase.getDadJoke();
			resolved.onResolve(joke, DadAbase.convertJokeToComponent(joke));
		}).start();
	}
}
