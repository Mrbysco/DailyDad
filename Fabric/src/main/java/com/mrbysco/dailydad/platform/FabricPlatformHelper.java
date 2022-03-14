package com.mrbysco.dailydad.platform;

import com.mrbysco.dailydad.DailyDadFabric;
import com.mrbysco.dailydad.config.JokeConfig;
import com.mrbysco.dailydad.config.JokeEnum;
import com.mrbysco.dailydad.jokes.DadAbase;
import com.mrbysco.dailydad.jokes.JokeResolved;
import com.mrbysco.dailydad.platform.services.IPlatformHelper;
import me.shedaniel.autoconfig.AutoConfig;

import java.util.List;

public class FabricPlatformHelper implements IPlatformHelper {

	@Override
	public List<? extends String> getInternalDadabase() {
		if (DailyDadFabric.config == null)
			DailyDadFabric.config = AutoConfig.getConfigHolder(JokeConfig.class).getConfig();
		return DailyDadFabric.config.general.internal_dadabase;
	}

	@Override
	public JokeEnum getJokeType() {
		if (DailyDadFabric.config == null)
			DailyDadFabric.config = AutoConfig.getConfigHolder(JokeConfig.class).getConfig();
		return JokeEnum.getByName(DailyDadFabric.config.general.jokeType);
	}

	@Override
	public boolean getJokeUponRespawn() {
		if (DailyDadFabric.config == null)
			DailyDadFabric.config = AutoConfig.getConfigHolder(JokeConfig.class).getConfig();
		return DailyDadFabric.config.general.jokeUponRespawn;
	}

	@Override
	public void getJokeAsync(JokeResolved resolved) {
		new Thread(() -> {
			String theJoke = DadAbase.getDadJoke();
			resolved.onResolve(theJoke, DadAbase.convertJokeToComponent(theJoke));
		}).start();
	}
}
