package com.mrbysco.dailydad.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.text2speech.Narrator;
import com.mrbysco.dailydad.config.JokeEnum;
import com.mrbysco.dailydad.platform.Services;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextComponent;

public class FabricDadCommands {
	public static void initializeCommands() {
		final LiteralArgumentBuilder<FabricClientCommandSource> root = ClientCommandManager.literal("dailydad");
		root.requires((sourceStack) -> sourceStack.hasPermission(2))
				.then(ClientCommandManager.literal("joke").executes(FabricDadCommands::sendJoke));
		ClientCommandManager.DISPATCHER.register(root);
	}

	private static int sendJoke(CommandContext<FabricClientCommandSource> ctx) {
		Services.PLATFORM.getJokeAsync((joke, component) -> {
			if (Services.PLATFORM.getJokeType() == JokeEnum.TTS) {
				Narrator.getNarrator().say("Daily Dad says: " + joke, true);
			}
			ctx.getSource().sendFeedback(new TextComponent("<DailyDad> ").withStyle(ChatFormatting.GOLD).append(component));
		});
		return 0;
	}
}
