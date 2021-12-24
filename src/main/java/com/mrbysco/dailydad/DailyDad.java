package com.mrbysco.dailydad;

import com.mrbysco.dailydad.client.JokeHandler;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(DailyDad.MOD_ID)
public class DailyDad {
    public static final String MOD_ID = "dailydad";
    public static final Logger LOGGER = LogManager.getLogger();

    public DailyDad() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, JokeConfig.clientSpec);
        eventBus.register(JokeConfig.class);

        //Make sure the mod being absent on the other network side does not cause the client to display the server as incompatible
        ModLoadingContext.get().registerExtensionPoint(DisplayTest.class,()->
                new IExtensionPoint.DisplayTest(() -> "Trans Rights Are Human Rights",
                        (remoteVersionString,networkBool) -> networkBool));

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            MinecraftForge.EVENT_BUS.register(new JokeHandler());
        });
    }
}
