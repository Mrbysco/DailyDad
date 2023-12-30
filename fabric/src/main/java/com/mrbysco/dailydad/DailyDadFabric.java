package com.mrbysco.dailydad;

import com.mrbysco.dailydad.callback.ClientEventsCallback;
import com.mrbysco.dailydad.commands.DadCommands;
import com.mrbysco.dailydad.commands.FabricDadCommands;
import com.mrbysco.dailydad.config.JokeConfig;
import com.mrbysco.dailydad.handler.JokeHandler;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.commands.Commands.CommandSelection;
import net.minecraft.world.InteractionResult;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;

public class DailyDadFabric implements ClientModInitializer {
	public static final String[] dadabase = new String[]
			{
					"I invented a new word! Plagiarism!",
					"Whoever invented the knock-knock joke should get a no bell prize.",
					"My pet mouse 'Elvis' died last night. He was caught in a trap..",
					"Why should you never trust a pig with a secret? Because it's bound to squeal.",
					"How do you organize a space party? You planet.",
					"Somebody stole my Microsoft Office and they're going to pay - you have my Word.",
					"What do you call a nervous javelin thrower? Shakespeare.",
					"I went to the zoo the other day, there was only one dog in it. It was a shitzu.",
					"'Hold on, I have something in my shoe'  'I’m pretty sure it’s a foot'",
					"What's black and white and read all over? The newspaper.",
					"Do you know where you can get chicken broth in bulk? The stock market.",
					"What kind of dinosaur loves to sleep? A stega-snore-us.",
					"I used to be a banker, but I lost interest.",
					"What kind of music do planets listen to? Nep-tunes.",
					"My friend said to me: \"What rhymes with orange\" I said: \"no it doesn't\"",
					"I asked the surgeon if I could administer my own anesthetic, they said: go ahead, knock yourself out.",
					"Why did the worker get fired from the orange juice factory? Lack of concentration.",
					"Why did the cookie cry? Because his mother was a wafer so long",
					"Where did Captain Hook get his hook? From a second hand store.",
					"I wish I could clean mirrors for a living. It's just something I can see myself doing.",
					"Yesterday a clown held a door open for me. I thought it was a nice jester.",
					"How many bones are in the human hand? A handful of them.",
					"A Sandwich walks into a bar, the bartender says 'Sorry, we don’t serve food here'",
					"The invention of the wheel was what got things rolling",
					"Geology rocks, but Geography is where it's at!",
					"Did you know you should always take an extra pair of pants golfing? Just in case you get a hole in one.",
					"What do you get if you put a duck in a cement mixer? Quacks in the pavement.",
					"Why did the opera singer go sailing? They wanted to hit the high Cs.",
					"How does a French skeleton say hello? Bone-jour.",
					"What did the dog say to the two trees? Bark bark."
			};

	private Thread watchThread = null;
	public static JokeConfig config;

	@Override
	public void onInitializeClient() {
		ConfigHolder<JokeConfig> holder = AutoConfig.register(JokeConfig.class, Toml4jConfigSerializer::new);
		config = holder.getConfig();
		try {
			var watchService = FileSystems.getDefault().newWatchService();
			Paths.get("config").register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
			watchThread = new Thread(() -> {
				WatchKey key;
				try {
					while ((key = watchService.take()) != null) {
						if (Thread.currentThread().isInterrupted()) {
							watchService.close();
							break;
						}
						for (WatchEvent<?> event : key.pollEvents()) {
							if (event.kind() == StandardWatchEventKinds.OVERFLOW) {
								continue;
							}
							if (((Path) event.context()).endsWith("dailydad.toml")) {
								Constants.LOGGER.info("Reloading Daily Dad config");
								if (holder.load()) {
									config = holder.getConfig();
								}
							}
						}
						key.reset();
					}
				} catch (InterruptedException ignored) {
				} catch (IOException e) {
					Constants.LOGGER.error("Failed to close filesystem watcher", e);
				}
			}, "Daily Dad Config Watcher");
			watchThread.start();
		} catch (IOException e) {
			Constants.LOGGER.error("Failed to create filesystem watcher for configs", e);
		}

		ScreenEvents.BEFORE_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
			if (screen instanceof ConnectScreen || screen instanceof LevelLoadingScreen) {
				JokeHandler.onScreenOpen(screen);
				ScreenEvents.beforeRender(screen).register((screen2, guiGraphics, mouseX, mouseY, partialTicks) -> JokeHandler.onDrawScreen(screen2, guiGraphics));
				ScreenEvents.afterRender(screen).register((screen2, guiGraphics, mouseX, mouseY, partialTicks) -> JokeHandler.onDrawScreen(screen2, guiGraphics));
			}
		});

		ClientEventsCallback.LOGIN_EVENT.register((player) -> {
			JokeHandler.onLoggedIn(player);
			return InteractionResult.PASS;
		});

		ClientEventsCallback.RESPAWN_EVENT.register((oldPlayer, newPlayer) -> {
			JokeHandler.onPlayerRespawn(oldPlayer, newPlayer);
			return InteractionResult.PASS;
		});

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			if (environment == CommandSelection.DEDICATED) {
				FabricDadCommands.initializeCommands();
			} else {
				//Only registers in singleplayer
				DadCommands.initializeCommands(dispatcher);
			}
		});
	}
}
