package com.mrbysco.dailydad.jokes;

import net.minecraft.network.chat.MutableComponent;

public interface JokeResolved {
	void onResolve(String joke, MutableComponent component);
}
