package net.saluf.lumatime.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.world.level.Level;
import net.saluf.lumatime.LumaTime;

@Mixin(Level.class)
public class WorldMixin {
	@Inject(at = @At("TAIL"), method = "isRaining", cancellable = true)
	private void isRaining(CallbackInfoReturnable<Boolean> ci) {
		if (LumaTime.weatherEnabled) {
			ci.setReturnValue(LumaTime.rain || LumaTime.snow);
			return;
		}
	}

	@Inject(at = @At("TAIL"), method = "isThundering", cancellable = true)
	private void isThundering(CallbackInfoReturnable<Boolean> ci) {
		if (LumaTime.weatherEnabled) {
			ci.setReturnValue(LumaTime.thunder);
			return;
		}
	}

	@Inject(at = @At("TAIL"), method = "getRainLevel", cancellable = true)
	private void getRainGradient(float progress, CallbackInfoReturnable<Float> ci) {
		if (LumaTime.weatherEnabled) {
			ci.setReturnValue(LumaTime.rain || LumaTime.snow ? 1.0f : 0.0f);
			return;
		}
	}

	@Inject(at = @At("TAIL"), method = "getThunderLevel", cancellable = true)
	private void getThunderGradient(float progress, CallbackInfoReturnable<Float> ci) {
		if (LumaTime.weatherEnabled) {
			ci.setReturnValue(LumaTime.thunder ? 1.0f : 0.0f);
			return;
		}
	}
}
