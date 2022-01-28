package com.mrbysco.dailydad.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.text2speech.Narrator;
import com.mrbysco.dailydad.DailyDad;
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
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent.LoggedInEvent;
import net.minecraftforge.client.event.ScreenEvent.DrawScreenEvent;
import net.minecraftforge.client.event.ScreenOpenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.io.IOException;

public class JokeHandler {
	private MutableComponent joke = null;

	@SubscribeEvent
	public void onScreenOpen(ScreenOpenEvent event) {
		JokeEnum jokeEnum = JokeConfig.CLIENT.jokeType.get();
		if(jokeEnum == JokeEnum.LOADING) {
			final Screen screen = event.getScreen();
			if (screen instanceof ConnectScreen || screen instanceof LevelLoadingScreen) {
				generateJoke(jokeEnum);
			}
		}
	}

	@SubscribeEvent
	public void onDrawScreen(DrawScreenEvent event) {
		if(JokeConfig.CLIENT.jokeType.get() == JokeEnum.LOADING) {
			final Screen screen = event.getScreen();
			if (screen instanceof ConnectScreen || screen instanceof LevelLoadingScreen) {
				if (joke != null) {
					final Font font = Minecraft.getInstance().font;
					PoseStack poseStack = event.getPoseStack();

					int height = font.lineHeight;
					RenderHelper.renderJoke(poseStack, joke, 6, height);
				}
			}
		}
	}

	@SubscribeEvent
	public void onLoggedIn(LoggedInEvent event) {
		if(event.getPlayer() != null) {
			JokeEnum jokeEnum = JokeConfig.CLIENT.jokeType.get();
			if(jokeEnum == JokeEnum.CHAT) {
				generateJoke(jokeEnum);

				event.getPlayer().sendMessage(joke, Util.NIL_UUID);
			} else if(jokeEnum == JokeEnum.TTS) {
				generateJoke(jokeEnum);
				Narrator.getNarrator().say("Daily Dad says: " + joke.getString(), true);
				event.getPlayer().sendMessage(new TextComponent("<DailyDad> ").withStyle(ChatFormatting.GOLD).append(joke), Util.NIL_UUID);
			}

			//Reset
			joke = null;
		}
	}

	private void generateJoke(JokeEnum jokeEnum) {
		this.joke = null;

		try {
			Component jokeComponent = DadAbase.getDadJoke();
			if(jokeComponent != null) {
				joke = getFinalComponent(jokeComponent, jokeEnum.isWithName());
			}
		} catch(IOException e) {
			//NOOP
			joke = null;
		}

		if(joke == null) {
			DailyDad.LOGGER.info("Getting internal dad joke instead");

			MutableComponent internalJoke = DadAbase.getInternalDadJoke().copy();

			joke = getFinalComponent(internalJoke, jokeEnum.isWithName());
		}
	}

	private MutableComponent getFinalComponent(Component component, boolean withName) {
		if(withName) {
			return new TextComponent("<DailyDad> ").withStyle(ChatFormatting.GOLD).append(component);
		} else {
			return component.copy();
		}
	}
}
