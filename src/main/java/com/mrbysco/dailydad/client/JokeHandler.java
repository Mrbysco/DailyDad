package com.mrbysco.dailydad.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.text2speech.Narrator;
import com.mrbysco.dailydad.config.JokeConfig;
import com.mrbysco.dailydad.config.JokeEnum;
import com.mrbysco.dailydad.jokes.DadAbase;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent.LoggedInEvent;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent.RespawnEvent;
import net.minecraftforge.client.event.ScreenEvent.DrawScreenEvent;
import net.minecraftforge.client.event.ScreenOpenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class JokeHandler {
	private MutableComponent joke = null;

	@SubscribeEvent
	public void onScreenOpen(ScreenOpenEvent event) {
		JokeEnum jokeEnum = JokeConfig.CLIENT.jokeType.get();
		if (jokeEnum == JokeEnum.LOADING) {
			final Screen screen = event.getScreen();
			if (screen instanceof ConnectScreen || screen instanceof LevelLoadingScreen) {
				DadAbase.getJokeAsync((joke, component) -> this.joke = component);
			}
		}
	}

	@SubscribeEvent
	public void onDrawScreen(DrawScreenEvent event) {
		if (joke == null) {
			return;
		}
		if (JokeConfig.CLIENT.jokeType.get() == JokeEnum.LOADING) {
			final Screen screen = event.getScreen();
			if (screen instanceof ConnectScreen || screen instanceof LevelLoadingScreen) {
				final Font font = Minecraft.getInstance().font;
				PoseStack poseStack = event.getPoseStack();

				int height = font.lineHeight;
				RenderHelper.renderJoke(poseStack, joke, 6, height);
			}
		}
	}

	@SubscribeEvent
	public void onLoggedIn(LoggedInEvent event) {
		if (event.getPlayer() != null) {
			JokeEnum jokeEnum = JokeConfig.CLIENT.jokeType.get();
			if (jokeEnum == JokeEnum.CHAT || jokeEnum == JokeEnum.TTS) {
				DadAbase.getJokeAsync((joke, component) -> {
					if (jokeEnum == JokeEnum.TTS) {
						Narrator.getNarrator().say("Daily Dad says: " + joke, true);
					}
					event.getPlayer().sendMessage(new TextComponent("<DailyDad> ").withStyle(ChatFormatting.GOLD).append(component), Util.NIL_UUID);
				});
			}
			//Reset
			joke = null;
		}
	}

	@SubscribeEvent
	public void onPlayerRespawn(RespawnEvent event) {
		if (JokeConfig.CLIENT.jokeUponRespawn.get()) {
			if (event.getOldPlayer().isDeadOrDying()) {
				JokeEnum jokeEnum = JokeConfig.CLIENT.jokeType.get();
				if (jokeEnum == JokeEnum.CHAT || jokeEnum == JokeEnum.TTS) {
					DadAbase.getJokeAsync((joke, component) -> {
						if (jokeEnum == JokeEnum.TTS) {
							Narrator.getNarrator().say("Daily Dad says: " + joke, true);
						}
						event.getPlayer().sendMessage(new TextComponent("<DailyDad> ").withStyle(ChatFormatting.GOLD).append(component), Util.NIL_UUID);
					});
				}
				//Reset
				joke = null;
			}
		}
	}
}
