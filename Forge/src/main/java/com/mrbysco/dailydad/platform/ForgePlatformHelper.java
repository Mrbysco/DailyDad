package com.mrbysco.dailydad.platform;

import com.mrbysco.dailydad.config.JokeConfig;
import com.mrbysco.dailydad.config.JokeEnum;
import com.mrbysco.dailydad.jokes.DadAbase;
import com.mrbysco.dailydad.jokes.JokeResolved;
import com.mrbysco.dailydad.platform.services.IPlatformHelper;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;

import java.util.List;

public class ForgePlatformHelper implements IPlatformHelper {

	@Override
	public List<? extends String> getInternalDadabase() {
		return JokeConfig.CLIENT.internal_dadabase.get();
	}

	@Override
	public JokeEnum getJokeType() {
		return JokeConfig.CLIENT.jokeType.get();
	}

	@Override
	public boolean getJokeUponRespawn() {
		return JokeConfig.CLIENT.jokeUponRespawn.get();
	}

	@Override
	public void getJokeAsync(JokeResolved resolved) {
		new Thread(() -> {
			String joke = DadAbase.getDadJoke();
			LogicalSidedProvider.WORKQUEUE.get(LogicalSide.CLIENT).execute(() -> resolved.onResolve(joke, DadAbase.convertJokeToComponent(joke)));
		}).start();
	}
}
