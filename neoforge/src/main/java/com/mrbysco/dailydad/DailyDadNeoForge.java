package com.mrbysco.dailydad;

import com.mrbysco.dailydad.config.JokeConfig;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

@Mod(Constants.MOD_ID)
public class DailyDadNeoForge {

	public DailyDadNeoForge(IEventBus eventBus, Dist dist, ModContainer container) {
		if (dist.isClient()) {
			container.registerConfig(ModConfig.Type.CLIENT, JokeConfig.clientSpec);
			eventBus.register(JokeConfig.class);
		}
	}
}