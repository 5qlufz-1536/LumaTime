package net.saluf.lumatime;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.KeyMapping.Category;
import net.minecraft.resources.Identifier;
import java.nio.file.Files;
import java.nio.file.Path;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.mojang.blaze3d.platform.InputConstants;

public class LumaTime implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("lumatime");

	public static Boolean timeEnabled = false;
	public static long time = 0L;

	public static Boolean weatherEnabled = false;
	public static Boolean rain = false;
	public static Boolean snow = false;
	public static Boolean thunder = false;

	public static Boolean moonPhaseEnabled = false;
	public static int moonPhase = 0;
	public static KeyMapping menuBind;
	public static boolean menuToggleHandled;

	int ticks = 0;
	final int autoSaveTicks = 20 * 60 * 3; 

	@Override
	public void onInitialize() {
		loadConfig();

		Category bindCategory = Category.register(Identifier.fromNamespaceAndPath("lumatime", "lumatime"));
		menuBind = new KeyMapping("key.lumatime.menu", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN,
				bindCategory);
		KeyMappingHelper.registerKeyMapping(menuBind);

		KeyMapping toggleBind = new KeyMapping("key.lumatime.toggle", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN,
				bindCategory);
		KeyMappingHelper.registerKeyMapping(toggleBind);

		KeyMapping toggleWeatherBind = new KeyMapping("key.lumatime.toggleWeather", InputConstants.Type.KEYSYM,
				GLFW.GLFW_KEY_UNKNOWN, bindCategory);
		KeyMappingHelper.registerKeyMapping(toggleWeatherBind);

		KeyMapping toggleRainBind = new KeyMapping("key.lumatime.toggleRain", InputConstants.Type.KEYSYM,
				GLFW.GLFW_KEY_UNKNOWN, bindCategory);
		KeyMappingHelper.registerKeyMapping(toggleRainBind);

		KeyMapping toggleSnowBind = new KeyMapping("key.lumatime.toggleSnow", InputConstants.Type.KEYSYM,
				GLFW.GLFW_KEY_UNKNOWN, bindCategory);
		KeyMappingHelper.registerKeyMapping(toggleSnowBind);

		KeyMapping toggleThunderBind = new KeyMapping("key.lumatime.toggleThunder", InputConstants.Type.KEYSYM,
				GLFW.GLFW_KEY_UNKNOWN, bindCategory);
		KeyMappingHelper.registerKeyMapping(toggleThunderBind);

		KeyMapping toggleMoonPhaseBind = new KeyMapping("key.lumatime.toggleMoonPhase", InputConstants.Type.KEYSYM,
				GLFW.GLFW_KEY_UNKNOWN, bindCategory);
		KeyMappingHelper.registerKeyMapping(toggleMoonPhaseBind);

		KeyMapping cycleMoonPhaseBind = new KeyMapping("key.lumatime.cycleMoonPhase", InputConstants.Type.KEYSYM,
				GLFW.GLFW_KEY_UNKNOWN, bindCategory);
		KeyMappingHelper.registerKeyMapping(cycleMoonPhaseBind);

		KeyMapping sunriseBind = new KeyMapping("key.lumatime.sunrise", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN,
				bindCategory);
		KeyMappingHelper.registerKeyMapping(sunriseBind);

		KeyMapping noonBind = new KeyMapping("key.lumatime.noon", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN,
				bindCategory);
		KeyMappingHelper.registerKeyMapping(noonBind);

		KeyMapping sunsetBind = new KeyMapping("key.lumatime.sunset", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN,
				bindCategory);
		KeyMappingHelper.registerKeyMapping(sunsetBind);

		KeyMapping midnightBind = new KeyMapping("key.lumatime.midnight", InputConstants.Type.KEYSYM,
				GLFW.GLFW_KEY_UNKNOWN, bindCategory);
		KeyMappingHelper.registerKeyMapping(midnightBind);

		ClientTickEvents.END_CLIENT_TICK.register((client) -> {
			ticks++;
			if(ticks == autoSaveTicks)
			{
				ticks = 0;
				saveConfig();
			}

			if (menuBind.consumeClick() && !menuToggleHandled) {
				if (client.gui.screen() instanceof TimeScreen timeScreen)
					timeScreen.onClose();
				else
					client.gui.setScreen(new TimeScreen(null));
			}
			menuToggleHandled = false;

			if (toggleBind.consumeClick())
				timeEnabled = !timeEnabled;

			if (toggleWeatherBind.consumeClick())
				weatherEnabled = !weatherEnabled;

			if (toggleRainBind.consumeClick()) {
				rain = !rain;
				if (rain)
					snow = false;
			}

			if (toggleSnowBind.consumeClick()) {
				snow = !snow;
				if (snow)
					rain = false;
			}

			if (toggleThunderBind.consumeClick())
				thunder = !thunder;

			if (toggleMoonPhaseBind.consumeClick())
				moonPhaseEnabled = !moonPhaseEnabled;

			if (cycleMoonPhaseBind.consumeClick()) {
				moonPhase++;
				moonPhase %= 8;
			}

			if (sunriseBind.consumeClick())
				time = 0;

			if (noonBind.consumeClick())
				time = 6000;

			if (sunsetBind.consumeClick())
				time = 12000;

			if (midnightBind.consumeClick())
				time = 18000;
		});
	}

	static Path configDir = FabricLoader.getInstance().getConfigDir().resolve("lumatime");
	static Path configFile = configDir.resolve("config.json");

	static void loadConfig() {
		try {
			Files.createDirectories(configDir);
			if (!Files.exists(configFile))
				return;

			String str = Files.readString(configFile);
			JsonObject jo = (JsonObject)JsonParser.parseString(str);

			if(jo.has("timeEnabled")) timeEnabled = jo.get("timeEnabled").getAsBoolean();
			if(jo.has("time")) time = jo.get("time").getAsLong();
			if(jo.has("weatherEnabled")) weatherEnabled = jo.get("weatherEnabled").getAsBoolean();
			if(jo.has("rain")) rain = jo.get("rain").getAsBoolean();
			if(jo.has("snow")) snow = jo.get("snow").getAsBoolean();
			if(jo.has("thunder")) thunder = jo.get("thunder").getAsBoolean();
			if(jo.has("moonCycleEnabled")) moonPhaseEnabled = jo.get("moonCycleEnabled").getAsBoolean();
			if(jo.has("moonCycle")) moonPhase = jo.get("moonCycle").getAsInt();
			if (snow)
				rain = false;

		} catch (Exception e) {
			LOGGER.error("Failed to load config", e);
		}
	}

	static void saveConfig() {
		JsonObject jo = new JsonObject();

		jo.add("timeEnabled", new JsonPrimitive(timeEnabled));
		jo.add("time", new JsonPrimitive(time));
		jo.add("weatherEnabled", new JsonPrimitive(weatherEnabled));
		jo.add("rain", new JsonPrimitive(rain));
		jo.add("snow", new JsonPrimitive(snow));
		jo.add("thunder", new JsonPrimitive(thunder));
		jo.add("moonCycleEnabled", new JsonPrimitive(moonPhaseEnabled));
		jo.add("moonCycle", new JsonPrimitive(moonPhase));

		try {
			Files.createDirectories(configDir);
			Files.writeString(configFile, new Gson().toJson(jo));
		} catch (Exception e) {
			LOGGER.error("Failed to save config", e);
		}
	}
}
