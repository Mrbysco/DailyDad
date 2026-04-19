package com.mrbysco.dailydad.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.text2speech.Narrator;
import com.mrbysco.dailydad.config.JokeConfig;
import com.mrbysco.dailydad.config.JokeEnum;
import com.mrbysco.dailydad.platform.Services;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;

public class FabricDadCommands {
	public static void initializeCommands() {
		final LiteralArgumentBuilder<FabricClientCommandSource> root = ClientCommands.literal("dailydad");
		root.then(ClientCommands.literal("joke").executes(FabricDadCommands::sendJoke));
		CommandDispatcher<FabricClientCommandSource> dispatcher = ClientCommands.getActiveDispatcher();
		if (dispatcher != null)
			dispatcher.register(root);
	}

	private static int sendJoke(CommandContext<FabricClientCommandSource> ctx) {
		Services.PLATFORM.getJokeAsync((joke, component) -> {
			if (JokeConfig.CLIENT.jokeType.get() == JokeEnum.TTS) {
				Narrator.getNarrator().say("Daily Dad says: " + joke, true, JokeConfig.CLIENT.ttsVolume.get().floatValue());
			}

			MutableComponent finalComponent = Component.literal("<DailyDad> ").withStyle(ChatFormatting.GOLD).append(component);
			if (ctx.getSource().getEntity() instanceof Player player) {
				player.sendSystemMessage(finalComponent);
			}
		});
		return 0;
	}
}
