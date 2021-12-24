package com.mrbysco.dailydad;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.apache.commons.lang3.tuple.Pair;

public class JokeConfig {
	public static class Client {
		public final BooleanValue onLoading;

		Client(ForgeConfigSpec.Builder builder) {
			builder.comment("Client settings")
					.push("client");

			onLoading = builder
					.comment("When enabled it will display a dad joke on world loading instead of when loading is finished [default: false]")
					.define("onLoading", false);

			builder.pop();
		}
	}

	public static final ForgeConfigSpec clientSpec;
	public static final Client CLIENT;
	static {
		final Pair<Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Client::new);
		clientSpec = specPair.getRight();
		CLIENT = specPair.getLeft();
	}

	@SubscribeEvent
	public static void onLoad(final ModConfigEvent.Loading configEvent) {
		DailyDad.LOGGER.debug("Loaded Daily Dad's config file {}", configEvent.getConfig().getFileName());
	}

	@SubscribeEvent
	public static void onFileChange(ModConfigEvent.Reloading configEvent) {
		DailyDad.LOGGER.debug("Daily Dad's config just got changed on the file system!");
	}
}
