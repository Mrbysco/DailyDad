package com.mrbysco.dailydad.handler;

import com.mojang.text2speech.Narrator;
import com.mrbysco.dailydad.client.RenderHelper;
import com.mrbysco.dailydad.config.JokeConfig;
import com.mrbysco.dailydad.config.JokeEnum;
import com.mrbysco.dailydad.platform.Services;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public class JokeHandler {
	private static MutableComponent joke = null;

	public static void onScreenOpen(Screen screen) {
		JokeEnum jokeEnum = JokeConfig.CLIENT.jokeType.get();
		if (jokeEnum == JokeEnum.LOADING) {
			if (screen instanceof ConnectScreen || screen instanceof LevelLoadingScreen) {
				Services.PLATFORM.getJokeAsync((joke, component) -> {
					JokeHandler.joke = component;
				});
			}
		}
	}

	public static void onDrawScreen(Screen screen, GuiGraphicsExtractor guiGraphics) {
		if (joke == null) {
			return;
		}
		if (JokeConfig.CLIENT.jokeType.get() == JokeEnum.LOADING) {
			if (screen instanceof ConnectScreen || screen instanceof LevelLoadingScreen) {
				final Font font = Minecraft.getInstance().font;

				int height = font.lineHeight;
				RenderHelper.renderJoke(guiGraphics, joke, 6, height);
			}
		}
	}

	public static void onLoggedIn(@Nullable Player player) {
		if (player != null) {
			JokeEnum jokeEnum = JokeConfig.CLIENT.jokeType.get();
			if (jokeEnum == JokeEnum.CHAT || jokeEnum == JokeEnum.TTS) {
				Services.PLATFORM.getJokeAsync((joke, component) -> {
					Minecraft.getInstance().execute(() -> {
						// Ensure this runs on the main thread
						if (jokeEnum == JokeEnum.TTS) {
							Narrator.getNarrator().say("Daily Dad says: " + joke, true, JokeConfig.CLIENT.ttsVolume.get().floatValue());
						}
						player.sendSystemMessage(Component.literal("<DailyDad> ").withStyle(ChatFormatting.GOLD).append(component));
					});
				});
			}
			//Reset
			joke = null;
		}
	}

	public static void onPlayerRespawn(Player oldPlayer, Player newPlayer) {
		if (JokeConfig.CLIENT.jokeUponRespawn.get()) {
			if (oldPlayer.isDeadOrDying()) {
				JokeEnum jokeEnum = JokeConfig.CLIENT.jokeType.get();
				if (jokeEnum != JokeEnum.LOADING) {
					Services.PLATFORM.getJokeAsync((joke, component) -> {
						Minecraft.getInstance().execute(() -> {
							if (jokeEnum == JokeEnum.TTS) {
								Narrator.getNarrator().say("Daily Dad says: " + joke, true, JokeConfig.CLIENT.ttsVolume.get().floatValue());
							}
							newPlayer.sendSystemMessage(Component.literal("<DailyDad> ").withStyle(ChatFormatting.GOLD).append(component));
						});
					});
				}
				//Reset
				joke = null;
			}
		}
	}
}
