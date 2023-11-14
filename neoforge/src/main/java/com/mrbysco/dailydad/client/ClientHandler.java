package com.mrbysco.dailydad.client;

import com.mrbysco.dailydad.Constants;
import com.mrbysco.dailydad.commands.DadCommands;
import com.mrbysco.dailydad.handler.JokeHandler;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent.LoggingIn;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientHandler {

	@SubscribeEvent
	public static void onScreenOpen(ScreenEvent.Opening event) {
		JokeHandler.onScreenOpen(event.getScreen());
	}

	@SubscribeEvent
	public static void onDrawScreen(ScreenEvent.Render.Post event) {
		JokeHandler.onDrawScreen(event.getScreen(), event.getGuiGraphics());
	}

	@SubscribeEvent
	public static void onLoggedIn(LoggingIn event) {
		JokeHandler.onLoggedIn(event.getPlayer());
	}

	@SubscribeEvent
	public static void onPlayerRespawn(PlayerEvent.Clone event) {
		JokeHandler.onPlayerRespawn(event.getOriginal(), event.getEntity());
	}

	@SubscribeEvent
	public static void onCommandRegister(RegisterClientCommandsEvent event) {
		DadCommands.initializeCommands(event.getDispatcher());
	}

	@SubscribeEvent
	public static void onServerCommandRegister(RegisterCommandsEvent event) {
		//Only registers in singleplayer
		DadCommands.initializeCommands(event.getDispatcher());
	}
}
