package com.mrbysco.dailydad.client;

import com.mrbysco.dailydad.commands.DadCommands;
import com.mrbysco.dailydad.handler.JokeHandler;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent.LoggingIn;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class ClientHandler {

	public static void onScreenOpen(ScreenEvent.Opening event) {
		JokeHandler.onScreenOpen(event.getScreen());
	}

	public static void onDrawScreen(ScreenEvent.Render event) {
		JokeHandler.onDrawScreen(event.getScreen(), event.getPoseStack());
	}

	public static void onLoggedIn(LoggingIn event) {
		JokeHandler.onLoggedIn(event.getPlayer());
	}

	public static void onPlayerRespawn(PlayerEvent.Clone event) {
		JokeHandler.onPlayerRespawn(event.getOriginal(), event.getEntity());
	}

	public static void onCommandRegister(RegisterClientCommandsEvent event) {
		DadCommands.initializeCommands(event.getDispatcher());
	}

	public static void onServerCommandRegister(RegisterCommandsEvent event) {
		//Only registers in singleplayer
		DadCommands.initializeCommands(event.getDispatcher());
	}
}
