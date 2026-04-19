package com.mrbysco.dailydad.platform;

import com.mrbysco.dailydad.jokes.DadAbase;
import com.mrbysco.dailydad.jokes.JokeResolved;
import com.mrbysco.dailydad.platform.services.IPlatformHelper;

public class FabricPlatformHelper implements IPlatformHelper {

	@Override
	public void getJokeAsync(JokeResolved resolved) {
		new Thread(() -> {
			String theJoke = DadAbase.getDadJoke();
			resolved.onResolve(theJoke, DadAbase.convertJokeToComponent(theJoke));
		}).start();
	}
}
