package com.mrbysco.dailydad.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import org.joml.Matrix3x2fStack;

import java.util.List;
import java.util.stream.Collectors;

public class RenderHelper {
	public static void renderJoke(GuiGraphicsExtractor guiGraphics, Component joke, int x, int y) {
		renderJoke(guiGraphics, List.of(joke.getVisualOrderText()), x, y);
	}

	private static void renderJoke(GuiGraphicsExtractor guiGraphics, List<? extends FormattedCharSequence> formattedCharSequences, int x, int y) {
		renderJokeInternal(guiGraphics, formattedCharSequences.stream().map(ClientTooltipComponent::create).collect(Collectors.toList()), x, y);
	}

	private static void renderJokeInternal(GuiGraphicsExtractor guiGraphics, List<ClientTooltipComponent> tooltipComponents, int x, int y) {
		final Matrix3x2fStack stack = guiGraphics.pose();
		final Minecraft minecraft = Minecraft.getInstance();
		final Screen screen = minecraft.gui.screen();
		if (screen == null)
			return;
		final Font font = minecraft.font;
		if (!tooltipComponents.isEmpty()) {
			int i = 0;
			int j = tooltipComponents.size() == 1 ? -2 : 0;

			for (ClientTooltipComponent clienttooltipcomponent : tooltipComponents) {
				int k = clienttooltipcomponent.getWidth(font);
				if (k > i) {
					i = k;
				}

				j += clienttooltipcomponent.getHeight(font);
			}

			int j2 = x;
			int k2 = y;
			if (j2 + i > screen.width) {
				j2 -= 28 + i;
			}

			if (k2 + j + 6 > screen.height) {
				k2 = screen.height - j - 6;
			}

			stack.pushMatrix();

			int l1 = k2;

			for (int i2 = 0; i2 < tooltipComponents.size(); ++i2) {
				ClientTooltipComponent clientTooltipComponent = tooltipComponents.get(i2);
				clientTooltipComponent.extractText(guiGraphics, font, j2, l1);

				l1 += clientTooltipComponent.getHeight(font) + (i2 == 0 ? 2 : 0);
			}

			stack.popMatrix();
		}
	}
}
