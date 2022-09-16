package com.mrbysco.dailydad.client;

import com.mrbysco.dailydad.commands.DadCommands;
import com.mrbysco.dailydad.handler.JokeHandler;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent.LoggedInEvent;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent.RespawnEvent;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.client.event.ScreenEvent.DrawScreenEvent;
import net.minecraftforge.client.event.ScreenOpenEvent;
import net.minecraftforge.event.RegisterCommandsEvent;

public class ClientHandler {

	public static void onScreenOpen(ScreenOpenEvent event) {
		JokeHandler.onScreenOpen(event.getScreen());
	}

	public static void onDrawScreen(DrawScreenEvent event) {
		JokeHandler.onDrawScreen(event.getScreen(), event.getPoseStack());
	}

	public static void onLoggedIn(LoggedInEvent event) {
		JokeHandler.onLoggedIn(event.getPlayer());
	}

	public static void onPlayerRespawn(RespawnEvent event) {
		JokeHandler.onPlayerRespawn(event.getOldPlayer(), event.getNewPlayer());
	}

	public static void onCommandRegister(RegisterClientCommandsEvent event) {
		DadCommands.initializeCommands(event.getDispatcher());
	}

	public static void onServerCommandRegister(RegisterCommandsEvent event) {
		//Only registers in singleplayer
		DadCommands.initializeCommands(event.getDispatcher());
	}
}
