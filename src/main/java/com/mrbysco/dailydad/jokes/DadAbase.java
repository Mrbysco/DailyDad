package com.mrbysco.dailydad.jokes;

import com.mrbysco.dailydad.DailyDad;
import com.mrbysco.dailydad.config.JokeConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.fml.LogicalSide;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.zip.GZIPInputStream;

public class DadAbase {
	private static final Random random = new Random();
	private static final String DAD_JOKE_URL = "https://icanhazdadjoke.com/";

	public static HttpClient CLIENT = HttpClient.newBuilder().connectTimeout(Duration.ofMillis(5 * 1000)).version(HttpClient.Version.HTTP_2).build();

	public static void getJokeAsync(JokeResolved resolved) {
		new Thread(() -> {
			String joke = DadAbase.getDadJoke();
			LogicalSidedProvider.WORKQUEUE.get(LogicalSide.SERVER).execute(() -> resolved.onResolve(joke, convertJokeToComponent(joke)));
		}).start();
	}

	public static String getDadJoke() {
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(DAD_JOKE_URL)).header("Accept-Encoding", "gzip").header("Accept", "text/plain").header("User-Agent", "Daily Dad Minecraft Mod (https://github.com/Mrbysco/DailyDad)").timeout(Duration.ofMinutes(1)).GET().build();

		String dadJoke = getResponseAsString(request);

		if (dadJoke.isEmpty()) {
			DailyDad.LOGGER.info("Getting internal dad joke instead");
			dadJoke = getInternalDadJoke();
		}
		return dadJoke;
	}

	public static MutableComponent convertJokeToComponent(String joke) {
		MutableComponent jokeComponent = TextComponent.EMPTY.copy();
		String[] lines = joke.split("\\R");
		for (int i = 0; i < lines.length; i++) {
			jokeComponent.append(new TextComponent(lines[i] + (i != lines.length - 1 ? "\n" : "")).withStyle(ChatFormatting.WHITE));
		}
		return jokeComponent;
	}

	public static String getResponseAsString(HttpRequest request) {
		try {
			HttpResponse<InputStream> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofInputStream());
			String encoding = response.headers().firstValue("Content-Encoding").orElse("");
			return switch (encoding) {
				case "" -> convertToString(response.body());
				case "gzip" -> convertToString(new GZIPInputStream(response.body()));
				default -> throw new UnsupportedOperationException("Unexpected Content-Encoding: " + encoding);
			};
		} catch (Exception e) {
			DailyDad.LOGGER.throwing(new IOException("Fetching dad Joke failed!" + e.getMessage()));
		}
		return "";
	}

	public static String convertToString(InputStream inputStream) throws IOException {
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		for (int length; (length = inputStream.read(buffer)) != -1; )
			result.write(buffer, 0, length);
		return result.toString(StandardCharsets.UTF_8);
	}

	public static String getInternalDadJoke() {
		List<? extends String> internalDadabase = JokeConfig.CLIENT.internal_dadabase.get();
		return internalDadabase.isEmpty() ? "" : internalDadabase.get(random.nextInt(internalDadabase.size()));
	}

	public interface JokeResolved {
		void onResolve(String joke, MutableComponent component);
	}
}
