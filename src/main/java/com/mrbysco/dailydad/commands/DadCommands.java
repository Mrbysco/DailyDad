package com.mrbysco.dailydad.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.text2speech.Narrator;
import com.mrbysco.dailydad.config.JokeConfig;
import com.mrbysco.dailydad.config.JokeEnum;
import com.mrbysco.dailydad.jokes.DadAbase;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;

public class DadCommands {
    public static void initializeCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        final LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("dailydad");
        root.requires((p_198721_0_) -> p_198721_0_.hasPermission(2))
                .then(Commands.literal("joke").executes(DadCommands::sendJoke));
        dispatcher.register(root);
    }

    private static int sendJoke(CommandContext<CommandSourceStack> ctx) {
        DadAbase.getJokeAsync((joke, component) -> {
            if(JokeConfig.CLIENT.jokeType.get() == JokeEnum.TTS) {
                Narrator.getNarrator().say("Daily Dad says: " + joke, true);
            }
            ctx.getSource().sendSuccess(new TextComponent("<DailyDad> ").withStyle(ChatFormatting.GOLD).append(component), false);
        });
        return 0;
    }
}
