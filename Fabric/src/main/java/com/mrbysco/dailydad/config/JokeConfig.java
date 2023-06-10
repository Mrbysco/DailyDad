package com.mrbysco.dailydad.config;

import com.mrbysco.dailydad.Constants;
import com.mrbysco.dailydad.DailyDadFabric;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.CollapsibleObject;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

import java.util.List;

@Config(name = Constants.MOD_ID)
public class JokeConfig implements ConfigData {

	@CollapsibleObject
	public General general = new General();

	public static class General {
		@Comment("Defines when a joke will be told [default: CHAT]")
		public JokeEnum jokeType = JokeEnum.CHAT;

		@Comment("The internal dad-abase of jokes for in case the mod is unable to reach the API")
		public List<String> internal_dadabase = List.of(DailyDadFabric.dadabase);

		@Comment("Should a joke be told upon death [default: false]")
		public boolean jokeUponRespawn = false;
	}
}