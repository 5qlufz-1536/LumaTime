package net.saluf.lumatime;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.KeyBinding.Category;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;

import java.nio.file.Files;
import java.nio.file.Path;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

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
	public static KeyBinding menuBind;
	public static boolean menuToggleHandled;

	int ticks = 0;
	final int autoSaveTicks = 20 * 60 * 3; 

	@Override
	public void onInitialize() {
		loadConfig();

		Category bindCategory = Category.create(Identifier.of("lumatime", "lumatime"));
		menuBind = new KeyBinding("key.lumatime.menu", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN,
				bindCategory);
		KeyBindingHelper.registerKeyBinding(menuBind);

		KeyBinding toggleBind = new KeyBinding("key.lumatime.toggle", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN,
				bindCategory);
		KeyBindingHelper.registerKeyBinding(toggleBind);

		KeyBinding toggleWeatherBind = new KeyBinding("key.lumatime.toggleWeather", InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_UNKNOWN, bindCategory);
		KeyBindingHelper.registerKeyBinding(toggleWeatherBind);

		KeyBinding toggleRainBind = new KeyBinding("key.lumatime.toggleRain", InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_UNKNOWN, bindCategory);
		KeyBindingHelper.registerKeyBinding(toggleRainBind);

		KeyBinding toggleSnowBind = new KeyBinding("key.lumatime.toggleSnow", InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_UNKNOWN, bindCategory);
		KeyBindingHelper.registerKeyBinding(toggleSnowBind);

		KeyBinding toggleThunderBind = new KeyBinding("key.lumatime.toggleThunder", InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_UNKNOWN, bindCategory);
		KeyBindingHelper.registerKeyBinding(toggleThunderBind);

		KeyBinding toggleMoonPhaseBind = new KeyBinding("key.lumatime.toggleMoonPhase", InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_UNKNOWN, bindCategory);
		KeyBindingHelper.registerKeyBinding(toggleMoonPhaseBind);

		KeyBinding cycleMoonPhaseBind = new KeyBinding("key.lumatime.cycleMoonPhase", InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_UNKNOWN, bindCategory);
		KeyBindingHelper.registerKeyBinding(cycleMoonPhaseBind);

		KeyBinding sunriseBind = new KeyBinding("key.lumatime.sunrise", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN,
				bindCategory);
		KeyBindingHelper.registerKeyBinding(sunriseBind);

		KeyBinding noonBind = new KeyBinding("key.lumatime.noon", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN,
				bindCategory);
		KeyBindingHelper.registerKeyBinding(noonBind);

		KeyBinding sunsetBind = new KeyBinding("key.lumatime.sunset", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN,
				bindCategory);
		KeyBindingHelper.registerKeyBinding(sunsetBind);

		KeyBinding midnightBind = new KeyBinding("key.lumatime.midnight", InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_UNKNOWN, bindCategory);
		KeyBindingHelper.registerKeyBinding(midnightBind);

		ClientTickEvents.END_CLIENT_TICK.register((client) -> {
			ticks++;
			if(ticks == autoSaveTicks)
			{
				ticks = 0;
				saveConfig();
			}

			if (menuBind.wasPressed() && !menuToggleHandled) {
				if (client.currentScreen instanceof TimeScreen timeScreen)
					timeScreen.close();
				else
					client.setScreen(new TimeScreen(null));
			}
			menuToggleHandled = false;

			if (toggleBind.wasPressed())
				timeEnabled = !timeEnabled;

			if (toggleWeatherBind.wasPressed())
				weatherEnabled = !weatherEnabled;

			if (toggleRainBind.wasPressed()) {
				rain = !rain;
				if (rain)
					snow = false;
			}

			if (toggleSnowBind.wasPressed()) {
				snow = !snow;
				if (snow)
					rain = false;
			}

			if (toggleThunderBind.wasPressed())
				thunder = !thunder;

			if (toggleMoonPhaseBind.wasPressed())
				moonPhaseEnabled = !moonPhaseEnabled;

			if (cycleMoonPhaseBind.wasPressed()) {
				moonPhase++;
				moonPhase %= 8;
			}

			if (sunriseBind.wasPressed())
				time = 0;

			if (noonBind.wasPressed())
				time = 6000;

			if (sunsetBind.wasPressed())
				time = 12000;

			if (midnightBind.wasPressed())
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
