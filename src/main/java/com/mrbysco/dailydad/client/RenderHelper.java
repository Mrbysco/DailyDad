package com.mrbysco.dailydad.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.client.gui.GuiUtils;

import java.util.Collections;

public class RenderHelper {
	public static void renderJoke(MatrixStack poseStack, ITextComponent joke, int x, int y) {
		final Minecraft minecraft = Minecraft.getInstance();
		final Screen screen = minecraft.screen;

		GuiUtils.drawHoveringText(poseStack, Collections.singletonList(joke), x, y + 6, screen.width, screen.height, -1, minecraft.font);
	}
}
