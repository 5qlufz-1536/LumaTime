package net.saluf.lumatime;

import java.util.function.Consumer;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Button.OnPress;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.level.MoonPhase;
import org.joml.Math;

public class TimeScreen extends Screen {
	Screen parent;

	StringWidget timePresetsText;
	ButtonWithIcon sunriseButton;
	ButtonWithIcon noonButton;
	ButtonWithIcon sunsetButton;
	ButtonWithIcon midnightButton;

	StringWidget timeText;
	SimpleSlider timeSlider;
	CheckboxButton timeEnabledButton;

	StringWidget moonPhaseText;
	SimpleSlider moonPhaseSlider;
	CheckboxButton moonPhaseEnabledButton;

	StringWidget weatherText;
	CheckboxButton weatherEnabledButton;
	CheckboxButton rainButton;
	CheckboxButton snowButton;
	CheckboxButton thunderButton;

	static final int CheckboxPadding = 4;

	public TimeScreen(Screen parent) {
		super(Component.empty());
		this.parent = parent;
	}

	@Override
	public void extractBackground(GuiGraphicsExtractor context, int mouseX, int mouseY, float deltaTicks) {
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	@Override
	public boolean keyPressed(KeyEvent input) {
		if (LumaTime.menuBind.matches(input)) {
			return closeWithMenuBind();
		}
		return super.keyPressed(input);
	}

	@Override
	public boolean mouseClicked(MouseButtonEvent input, boolean doubled) {
		if (LumaTime.menuBind.matchesMouse(input)) {
			return closeWithMenuBind();
		}
		return super.mouseClicked(input, doubled);
	}

	private boolean closeWithMenuBind() {
		LumaTime.menuToggleHandled = true;
		onClose();
		return true;
	}

	@Override
	protected void init() {
		timeSlider = new SimpleSlider(0, 24000);
		timeSlider.setIValue(LumaTime.time);
		timeSlider.onValue = (Long l) -> {
			LumaTime.time = l;
		};
		timeSlider.setWidth(width / 4);
		timeSlider.setHeight(32);
		timeSlider.setPosition((width / 2) - (timeSlider.getWidth() / 2), java.lang.Math.max(60, height - 96));

		timeText = new StringWidget(Component.translatable("lumatime.timeScreen.time"), font);
		timeText.setPosition(timeSlider.getX(), timeSlider.getY() - font.lineHeight);
		addRenderableWidget(timeText);

		timePresetsText = new StringWidget(Component.translatable("lumatime.timeScreen.timePresets"), font);
		timePresetsText.setPosition(timeSlider.getX() + timeSlider.getWidth() + 72, timeText.getY());
		addRenderableWidget(timePresetsText);

		sunriseButton = new ButtonWithIcon(Identifier.parse("textures/item/clock_48.png"), 32,
				timePresetsText.getX(), timeSlider.getY(),
				32, 32,
				Component.empty(), (Button b) -> {
					LumaTime.time = 0;
					timeSlider.setIValue(0);
				});
		noonButton = new ButtonWithIcon(Identifier.parse("textures/item/clock_00.png"), 32,
				sunriseButton.getX() + 32, sunriseButton.getY(),
				32, 32,
				Component.empty(), (Button b) -> {
					LumaTime.time = 6000;
					timeSlider.setIValue(6000);
				});
		sunsetButton = new ButtonWithIcon(Identifier.parse("textures/item/clock_15.png"), 32,
				noonButton.getX() + 32, noonButton.getY(),
				32, 32,
				Component.empty(), (Button b) -> {
					LumaTime.time = 12000;
					timeSlider.setIValue(12000);
				});
		midnightButton = new ButtonWithIcon(Identifier.parse("textures/item/clock_32.png"), 32,
				sunsetButton.getX() + 32, sunsetButton.getY(),
				32, 32,
				Component.empty(), (Button b) -> {
					LumaTime.time = 18000;
					timeSlider.setIValue(18000);
				});

		addRenderableOnly(sunriseButton);
		addWidget(sunriseButton);
		addRenderableOnly(noonButton);
		addWidget(noonButton);
		addRenderableOnly(sunsetButton);
		addWidget(sunsetButton);
		addRenderableOnly(midnightButton);
		addWidget(midnightButton);

		timeEnabledButton = new CheckboxButton(LumaTime.timeEnabled,
				Identifier.fromNamespaceAndPath("lumatime", "textures/gui/checkmark.png"), 16,
				timeSlider.getX() - 32 + CheckboxPadding, timeSlider.getY() + CheckboxPadding,
				32 - (CheckboxPadding * 2), 32 - (CheckboxPadding * 2),
				Component.empty(), (boolean b) -> {
					LumaTime.timeEnabled = b;
				});
		addRenderableOnly(timeSlider);
		addWidget(timeSlider);
		addRenderableOnly(timeEnabledButton);
		addWidget(timeEnabledButton);

		moonPhaseText = new StringWidget(Component.translatable("lumatime.timeScreen.moonPhase"), font);
		moonPhaseText.setPosition(timeSlider.getX(), timeSlider.getY() + timeSlider.getHeight());
		addRenderableWidget(moonPhaseText);

		moonPhaseSlider = new SimpleSlider(0, 7);
		moonPhaseSlider.setIValue(LumaTime.moonPhase);
		moonPhaseSlider.onValue = (Long i) -> {
			long j = i;
			LumaTime.moonPhase = (int) j;
		};
		moonPhaseSlider.setWidth(timeSlider.getWidth());
		moonPhaseSlider.setHeight(timeSlider.getHeight());
		moonPhaseSlider.setPosition(moonPhaseText.getX(), moonPhaseText.getY() + moonPhaseText.getHeight());

		moonPhaseEnabledButton = new CheckboxButton(LumaTime.moonPhaseEnabled,
				Identifier.fromNamespaceAndPath("lumatime", "textures/gui/checkmark.png"), 16,
				moonPhaseSlider.getX() - 32 + CheckboxPadding, moonPhaseSlider.getY() + CheckboxPadding,
				32 - (CheckboxPadding * 2), 32 - (CheckboxPadding * 2),
				Component.empty(), (boolean b) -> {
					LumaTime.moonPhaseEnabled = b;
				});
		addRenderableOnly(moonPhaseSlider);
		addWidget(moonPhaseSlider);
		addRenderableOnly(moonPhaseEnabledButton);
		addWidget(moonPhaseEnabledButton);

		weatherText = new StringWidget(Component.translatable("lumatime.timeScreen.weather"), font);
		int weatherX = moonPhaseSlider.getX() + moonPhaseSlider.getWidth() + 72;
		weatherText.setPosition(weatherX, moonPhaseText.getY());
		addRenderableWidget(weatherText);

		rainButton = new CheckboxButton(LumaTime.rain,
				Identifier.fromNamespaceAndPath("lumatime", "textures/gui/rain.png"), 8,
				weatherText.getX(), moonPhaseSlider.getY(),
				32, 32,
				Component.empty(), (boolean b) -> {
					LumaTime.rain = b;
					if (b) {
						LumaTime.snow = false;
						snowButton.checked = false;
					}
				});

		snowButton = new CheckboxButton(LumaTime.snow,
				Identifier.fromNamespaceAndPath("lumatime", "textures/gui/snow.png"), 16,
				rainButton.getX() + 32, rainButton.getY(),
				32, 32,
				Component.empty(), (boolean b) -> {
					LumaTime.snow = b;
					if (b) {
						LumaTime.rain = false;
						rainButton.checked = false;
					}
				});

		thunderButton = new CheckboxButton(LumaTime.thunder,
				Identifier.fromNamespaceAndPath("lumatime", "textures/gui/thunder.png"), 8,
				snowButton.getX() + 32, snowButton.getY(),
				32, 32,
				Component.empty(), (boolean b) -> {
					LumaTime.thunder = b;
				});

		weatherEnabledButton = new CheckboxButton(LumaTime.weatherEnabled,
				Identifier.fromNamespaceAndPath("lumatime", "textures/gui/checkmark.png"), 16,
				rainButton.getX() - 32 + CheckboxPadding, rainButton.getY() + CheckboxPadding,
				32 - (CheckboxPadding * 2), 32 - (CheckboxPadding * 2),
				Component.empty(), (boolean b) -> {
					LumaTime.weatherEnabled = b;
				});

		addRenderableOnly(weatherEnabledButton);
		addWidget(weatherEnabledButton);
		addRenderableOnly(rainButton);
		addWidget(rainButton);
		addRenderableOnly(snowButton);
		addWidget(snowButton);
		addRenderableOnly(thunderButton);
		addWidget(thunderButton);

		addRenderableOnly(new Renderable() {
			@Override
			public void extractRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
				int clocktex = ((int) (LumaTime.time - 6000) * 62 / 24000);
				if (clocktex < 0)
					clocktex += 62;
				context.blit(RenderPipelines.GUI_TEXTURED,
						Identifier.parse(String.format("textures/item/clock_%02d.png", clocktex)),
						timeSlider.getX() + timeSlider.getWidth(), timeSlider.getY(),
						0, 0,
						32, 32,
						16, 16,
						16, 16);

				int col = LumaTime.moonPhase / 4;
				int row = LumaTime.moonPhase % 4;

				MoonPhase setPhase = null;
				for (MoonPhase phase : MoonPhase.values()) {
					if (phase.index() == LumaTime.moonPhase) {
						setPhase = phase;
						break;
					}
				}

				context.blit(RenderPipelines.GUI_TEXTURED, Identifier.parse("textures/environment/celestial/moon/" + setPhase.getSerializedName() + ".png"),
						moonPhaseSlider.getX() + moonPhaseSlider.getWidth(), moonPhaseSlider.getY(),
						32, 32,
						32, 32,
						32, 32,
						32, 32);
			}
		});
	}

	public static class SimpleSlider extends AbstractSliderButton {
		long min, max;
		long iValue;
		public Consumer<Long> onValue;

		public SimpleSlider(long min, long max) {
			super(0, 0, 0, 0, Component.empty(), 0);
			this.min = min;
			this.max = max;
			updateMessage();
		}

		public void setIValue(long v) {
			iValue = v;
			setValue((v - min) / (double) (max - min));
		}

		@Override
		public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
			if (!isMouseOver(mouseX, mouseY) || verticalAmount == 0)
				return false;

			long step = 1;
			long nextValue = iValue + (verticalAmount > 0 ? step : -step);
			setIValue(java.lang.Math.max(min, java.lang.Math.min(max, nextValue)));
			if (onValue != null)
				onValue.accept(iValue);
			return true;
		}

		@Override
		protected void applyValue() {
			iValue = (long) Math.round(value * (max - min)) + min;
			setIValue(iValue);

			updateMessage();
			if (onValue != null)
				onValue.accept(iValue);
		}

		@Override
		protected void updateMessage() {
			setMessage(Component.literal(iValue + " / " + max));
		}
	}

	public static class ButtonWithIcon extends AbstractButton {
		Identifier texture;
		int texSize;
		OnPress onPressAction;

		protected ButtonWithIcon(Identifier texture, int texSize, int x, int y, int width, int height, Component message,
				OnPress onPress) {
			super(x, y, width, height, message);

			onPressAction = onPress;
			this.texSize = texSize;
			this.texture = texture;
		}

		@Override
		protected void extractContents(GuiGraphicsExtractor context, int mouseX, int mouseY, float deltaTicks) {
			extractDefaultSprite(context);
			context.blit(RenderPipelines.GUI_TEXTURED, texture,
					getX() + (getWidth() / 4), getY() + (getHeight() / 4),
					0, 0,
					texSize / 2, texSize / 2,
					texSize, texSize,
					texSize, texSize);
		}

		@Override
		public void onPress(InputWithModifiers input) {
			onPressAction.onPress(null);
		}

		@Override
		protected void updateWidgetNarration(NarrationElementOutput builder) {
		}
	}

	public static class CheckboxButton extends AbstractButton {
		private static final Identifier TEXTURE = Identifier.parse("widget/checkbox");
		Identifier checkTexture;
		int texSize;
		public boolean checked;
		Action onPressAction;

		public interface Action {
			void onPress(boolean checked);
		}

		protected CheckboxButton(boolean checked, Identifier checkTexture, int texSize, int x, int y, int width,
				int height, Component message, Action onPress) {
			super(x, y, width, height, message);
			onPressAction = onPress;

			this.checkTexture = checkTexture;
			this.texSize = texSize;
			this.checked = checked;
		}

		@Override
		protected void extractContents(GuiGraphicsExtractor context, int mouseX, int mouseY, float deltaTicks) {
			extractDefaultSprite(context);
			context.blit(RenderPipelines.GUI_TEXTURED, checkTexture,
					getX() + (getWidth() / 4), getY() + (getHeight() / 4),
					0, 0,
					getWidth() / 2, getWidth() / 2,
					texSize, texSize,
					texSize, texSize, ARGB.white(checked ? 1.0f : 0.1f));
		}

		@Override
		public void onPress(InputWithModifiers input) {
			checked = !checked;
			onPressAction.onPress(checked);
		}

		@Override
		protected void updateWidgetNarration(NarrationElementOutput builder) {
		}
	}

	@Override
	public void onClose() {
		LumaTime.saveConfig();
		minecraft.setScreen(parent);
	}
}
