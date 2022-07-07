package com.mrbysco.dailydad.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.text2speech.Narrator;
import com.mrbysco.dailydad.config.JokeEnum;
import com.mrbysco.dailydad.platform.Services;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;

public class DadCommands {
	public static void initializeCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
		final LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("dailydad");
		root.then(Commands.literal("joke").executes(DadCommands::sendJoke));
		dispatcher.register(root);
	}

	private static int sendJoke(CommandContext<CommandSourceStack> ctx) {
		Services.PLATFORM.getJokeAsync((joke, component) -> {
			if (Services.PLATFORM.getJokeType() == JokeEnum.TTS) {
				Narrator.getNarrator().say("Daily Dad says: " + joke, true);
			}

			MutableComponent finalComponent = Component.literal("<DailyDad> ").withStyle(ChatFormatting.GOLD).append(component);
			if (ctx.getSource().getEntity() instanceof Player player) {
				player.sendSystemMessage(finalComponent);
			}
		});
		return 0;
	}
}
