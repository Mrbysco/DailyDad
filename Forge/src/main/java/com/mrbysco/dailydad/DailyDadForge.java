package com.mrbysco.dailydad;

import com.mrbysco.dailydad.commands.DadCommands;
import com.mrbysco.dailydad.config.JokeConfig;
import com.mrbysco.dailydad.handler.JokeHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent.LoggedInEvent;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent.RespawnEvent;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.client.event.ScreenEvent.DrawScreenEvent;
import net.minecraftforge.client.event.ScreenOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.IExtensionPoint.DisplayTest;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.MOD_ID)
public class DailyDadForge {

	public DailyDadForge() {
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, JokeConfig.clientSpec);
		eventBus.register(JokeConfig.class);

		CommonClass.init();

		//Make sure the mod being absent on the other network side does not cause the client to display the server as incompatible
		ModLoadingContext.get().registerExtensionPoint(DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> "Trans Rights Are Human Rights", (remoteVersionString, networkBool) -> networkBool));

		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
			MinecraftForge.EVENT_BUS.addListener(this::onScreenOpen);
			MinecraftForge.EVENT_BUS.addListener(this::onDrawScreen);
			MinecraftForge.EVENT_BUS.addListener(this::onLoggedIn);
			MinecraftForge.EVENT_BUS.addListener(this::onPlayerRespawn);
			MinecraftForge.EVENT_BUS.addListener(this::onCommandRegister);
			MinecraftForge.EVENT_BUS.addListener(this::onServerCommandRegister);
		});
	}

	public void onScreenOpen(ScreenOpenEvent event) {
		JokeHandler.onScreenOpen(event.getScreen());
	}

	public void onDrawScreen(DrawScreenEvent event) {
		JokeHandler.onDrawScreen(event.getScreen(), event.getPoseStack());
	}

	public void onLoggedIn(LoggedInEvent event) {
		JokeHandler.onLoggedIn(event.getPlayer());
	}

	public void onPlayerRespawn(RespawnEvent event) {
		JokeHandler.onPlayerRespawn(event.getOldPlayer(), event.getNewPlayer());
	}

	public void onCommandRegister(RegisterClientCommandsEvent event) {
		DadCommands.initializeCommands(event.getDispatcher());
	}

	public void onServerCommandRegister(RegisterCommandsEvent event) {
		//Only registers in singleplayer
		DadCommands.initializeCommands(event.getDispatcher());
	}
}