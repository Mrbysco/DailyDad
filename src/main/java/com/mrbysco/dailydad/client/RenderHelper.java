package com.mrbysco.dailydad.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;
import java.util.stream.Collectors;

public class RenderHelper {
	public static void renderJoke(PoseStack poseStack, Component joke, int x, int y) {
		renderJoke(poseStack, List.of(joke.getVisualOrderText()), x, y);
	}

	private static void renderJoke(PoseStack poseStack, List<? extends FormattedCharSequence> formattedCharSequences, int x, int y) {
		renderJokeInternal(poseStack, formattedCharSequences.stream().map(ClientTooltipComponent::create).collect(Collectors.toList()), x, y);
	}

	private static void renderJokeInternal(PoseStack stack, List<ClientTooltipComponent> tooltipComponents, int x, int y) {
		final Minecraft minecraft = Minecraft.getInstance();
		final Screen screen = minecraft.screen;
		assert screen != null;
		final Font font = minecraft.font;
		if (!tooltipComponents.isEmpty()) {
			int i = 0;
			int j = tooltipComponents.size() == 1 ? -2 : 0;

			for(ClientTooltipComponent clienttooltipcomponent : tooltipComponents) {
				int k = clienttooltipcomponent.getWidth(font);
				if (k > i) {
					i = k;
				}

				j += clienttooltipcomponent.getHeight();
			}

			int j2 = x;
			int k2 = y;
			if (j2 + i > screen.width) {
				j2 -= 28 + i;
			}

			if (k2 + j + 6 > screen.height) {
				k2 = screen.height - j - 6;
			}

			stack.pushPose();

			Matrix4f matrix4f = stack.last().pose();
			MultiBufferSource.BufferSource bufferSource = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
			stack.translate(0.0D, 0.0D, 400.0D);
			int l1 = k2;

			for(int i2 = 0; i2 < tooltipComponents.size(); ++i2) {
				ClientTooltipComponent clientTooltipComponent = tooltipComponents.get(i2);
				clientTooltipComponent.renderText(font, j2, l1, matrix4f, bufferSource);

				l1 += clientTooltipComponent.getHeight() + (i2 == 0 ? 2 : 0);
			}

			bufferSource.endBatch();
			stack.popPose();
		}
	}
}
