package com.mrbysco.dailydad.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.text2speech.Narrator;
import com.mrbysco.dailydad.DailyDad;
import com.mrbysco.dailydad.config.JokeConfig;
import com.mrbysco.dailydad.config.JokeEnum;
import com.mrbysco.dailydad.jokes.DadAbase;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;

import java.io.IOException;

public class DadCommands {
	public static void initializeCommands (CommandDispatcher<CommandSourceStack> dispatcher) {
		final LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("dailydad");
		root.requires((p_198721_0_) -> p_198721_0_.hasPermission(2))
				.then(Commands.literal("joke").executes((ctx) -> DadCommands.sendJoke(ctx)));
		dispatcher.register(root);
	}

	private static int sendJoke(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
		MutableComponent joke = null;
		try {
			Component jokeComponent = DadAbase.getDadJoke();
			if(jokeComponent != null) {
				if(JokeConfig.CLIENT.jokeType.get() == JokeEnum.TTS) {
					Narrator.getNarrator().say("Daily Dad says: " + jokeComponent.getString(), true);
				}
				joke = new TextComponent("<DailyDad> ").withStyle(ChatFormatting.GOLD).append(jokeComponent);
			}
		} catch(IOException e) {
			//NOOP
			joke = null;
		}

		if(joke == null) {
			DailyDad.LOGGER.info("Getting internal dad joke instead");

			MutableComponent internalJoke = DadAbase.getInternalDadJoke().copy();
			if(JokeConfig.CLIENT.jokeType.get() == JokeEnum.TTS) {
				Narrator.getNarrator().say("Daily Dad says: " + internalJoke.getString(), true);
			}
			joke = new TextComponent("<DailyDad> ").withStyle(ChatFormatting.GOLD).append(internalJoke);
		}
		ctx.getSource().sendSuccess(joke, false);

		return 0;
	}
}
