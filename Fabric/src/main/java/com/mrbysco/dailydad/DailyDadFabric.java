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
	private Thread watchThread = null;
	public static JokeConfig config;

	@Override
	public void onInitializeClient() {
		CommonClass.init();

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
				ScreenEvents.beforeRender(screen).register((screen2, poseStack, mouseX, mouseY, partialTicks) -> {
					JokeHandler.onDrawScreen(screen2, poseStack);
				});
				ScreenEvents.afterRender(screen).register((screen2, poseStack, mouseX, mouseY, partialTicks) -> {
					JokeHandler.onDrawScreen(screen2, poseStack);
				});
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
