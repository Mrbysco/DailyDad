package com.mrbysco.dailydad.jokes;

import com.mrbysco.dailydad.DailyDad;
import com.mrbysco.dailydad.JokeConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;

import javax.net.ssl.HttpsURLConnection;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Random;

public class DadAbase {
	private static final Random random = new Random();
	private static final String DAD_JOKE_URL = "https://icanhazdadjoke.com/";

	public static Component getDadJoke() throws IOException {
		HttpsURLConnection connection = null;
		try {
			connection = (HttpsURLConnection) new URL(DAD_JOKE_URL).openConnection();

			connection.setRequestMethod("GET");
			connection.addRequestProperty("Accept", "text/plain");
			connection.addRequestProperty("User-Agent", "Daily Dad Minecraft Mod " +
					"(https://github.com/Mrbysco/DailyDad)");
			connection.connect();

			int responseCode = connection.getResponseCode();
			if (responseCode != 200) {
				DailyDad.LOGGER.throwing(new IOException("Fetching dad Joke failed with: " + responseCode + " " +
						connection.getResponseMessage()));

				return null;
			}

			InputStream is = connection.getInputStream();

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int length;
			while ((length = is.read(buffer)) != -1) {
				outputStream.write(buffer, 0, length);
			}

			String dadJoke = outputStream.toString();
			MutableComponent jokeComponent = TextComponent.EMPTY.copy();
			String[] lines = dadJoke.split("\\R");
			for (int i = 0; i < lines.length; i++) {
				String line = lines[i];
				if(i != (lines.length - 1)) {
					line += "\n";
				}
				jokeComponent = jokeComponent.append(new TextComponent(line).withStyle(ChatFormatting.WHITE));
			}
			return jokeComponent;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	public static Component getInternalDadJoke() {
		List<? extends String> internalDadabase = JokeConfig.CLIENT.internal_dadabase.get();
		if(internalDadabase.isEmpty()) {
			return TextComponent.EMPTY;
		} else {
			int idx = random.nextInt(internalDadabase.size());
			return new TextComponent(internalDadabase.get(idx));
		}
	}
}
