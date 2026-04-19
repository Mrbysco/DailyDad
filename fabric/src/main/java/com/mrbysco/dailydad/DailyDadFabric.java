package com.mrbysco.dailydad;

import com.mrbysco.dailydad.callback.ClientEventsCallback;
import com.mrbysco.dailydad.commands.DadCommands;
import com.mrbysco.dailydad.commands.FabricDadCommands;
import com.mrbysco.dailydad.config.JokeConfig;
import com.mrbysco.dailydad.handler.JokeHandler;
import fuzs.forgeconfigapiport.fabric.api.v5.ConfigRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.commands.Commands.CommandSelection;
import net.minecraft.world.InteractionResult;
import net.neoforged.fml.config.ModConfig;

public class DailyDadFabric implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ConfigRegistry.INSTANCE.register(Constants.MOD_ID, ModConfig.Type.CLIENT, JokeConfig.clientSpec);

		ScreenEvents.BEFORE_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
			if (screen instanceof ConnectScreen || screen instanceof LevelLoadingScreen) {
				JokeHandler.onScreenOpen(screen);
				ScreenEvents.beforeExtract(screen).register((screen2, guiGraphics, mouseX, mouseY, partialTicks) -> JokeHandler.onDrawScreen(screen2, guiGraphics));
				ScreenEvents.afterExtract(screen).register((screen2, guiGraphics, mouseX, mouseY, partialTicks) -> JokeHandler.onDrawScreen(screen2, guiGraphics));
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
