package com.mrbysco.dailydad;

import com.mrbysco.dailydad.config.JokeConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
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
			MinecraftForge.EVENT_BUS.addListener(com.mrbysco.dailydad.client.ClientHandler::onScreenOpen);
			MinecraftForge.EVENT_BUS.addListener(com.mrbysco.dailydad.client.ClientHandler::onDrawScreen);
			MinecraftForge.EVENT_BUS.addListener(com.mrbysco.dailydad.client.ClientHandler::onLoggedIn);
			MinecraftForge.EVENT_BUS.addListener(com.mrbysco.dailydad.client.ClientHandler::onPlayerRespawn);
			MinecraftForge.EVENT_BUS.addListener(com.mrbysco.dailydad.client.ClientHandler::onCommandRegister);
			MinecraftForge.EVENT_BUS.addListener(com.mrbysco.dailydad.client.ClientHandler::onServerCommandRegister);
		});
	}
}