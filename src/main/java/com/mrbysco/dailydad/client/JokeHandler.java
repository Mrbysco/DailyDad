package com.mrbysco.dailydad.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mrbysco.dailydad.DailyDad;
import com.mrbysco.dailydad.JokeConfig;
import com.mrbysco.dailydad.jokes.DadAbase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.ConnectingScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.WorldLoadProgressScreen;
import net.minecraft.util.Util;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent.LoggedInEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.io.IOException;

public class JokeHandler {
	private ITextComponent joke = null;

	@SubscribeEvent
	public void onScreenOpen(GuiOpenEvent event) {
		final Screen screen = event.getGui();
		if (screen instanceof ConnectingScreen || screen instanceof WorldLoadProgressScreen) {
			generateJoke(false);
		}
	}

	@SubscribeEvent
	public void onDrawScreen(DrawScreenEvent event) {
		if(JokeConfig.CLIENT.onLoading.get()) {
			final Screen screen = event.getGui();
			if (screen instanceof ConnectingScreen || screen instanceof WorldLoadProgressScreen) {
				if (joke != null) {
					final FontRenderer font = Minecraft.getInstance().font;
					MatrixStack poseStack = event.getMatrixStack();

					int height = font.lineHeight;
					RenderHelper.renderJoke(poseStack, joke, 6, height);
				}
			}
		}
	}

	@SubscribeEvent
	public void onLoggedIn(LoggedInEvent event) {
		if(!JokeConfig.CLIENT.onLoading.get() && event.getPlayer() != null) {
			generateJoke(true);

			event.getPlayer().sendMessage(joke, Util.NIL_UUID);

			//Reset
			joke = null;
		}
	}

	private void generateJoke(boolean withName) {
		this.joke = null;

		try {
			ITextComponent jokeComponent = DadAbase.getDadJoke();
			if(jokeComponent != null) {
				joke = getFinalComponent(jokeComponent, withName);
			}
		} catch(IOException e) {
			//NOOP
			joke = null;
		}

		if(joke == null) {
			DailyDad.LOGGER.info("Getting internal dad joke instead");

			ITextComponent internalJoke = DadAbase.getInternalDadJoke().copy();

			joke = getFinalComponent(internalJoke, withName);
		}
	}

	private IFormattableTextComponent getFinalComponent(ITextComponent component, boolean withName) {
		if(withName) {
			return new StringTextComponent("<DailyDad> ").withStyle(TextFormatting.GOLD).append(component);
		} else {
			return component.copy();
		}
	}
}
