package com.mrbysco.dailydad;

import com.mrbysco.dailydad.config.JokeConfig;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = Constants.MOD_ID, dist = Dist.CLIENT)
public class DailyDadNeoForge {

	public DailyDadNeoForge(ModContainer container) {
		container.registerConfig(ModConfig.Type.CLIENT, JokeConfig.clientSpec);
		container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
	}
}