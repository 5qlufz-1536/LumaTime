package xyz.ryhon.clienttime.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.world.World;
import xyz.ryhon.clienttime.ClientTime;

@Mixin(World.class)
public class WorldMixin {
	@Inject(at = @At("TAIL"), method = "isRaining", cancellable = true)
	private void isRaining(CallbackInfoReturnable<Boolean> ci) {
		if (ClientTime.weatherEnabled) {
			ci.setReturnValue(ClientTime.rain);
			return;
		}
	}

	@Inject(at = @At("TAIL"), method = "isThundering", cancellable = true)
	private void isThundering(CallbackInfoReturnable<Boolean> ci) {
		if (ClientTime.weatherEnabled) {
			ci.setReturnValue(ClientTime.thunder);
			return;
		}
	}

	@Inject(at = @At("TAIL"), method = "getRainGradient", cancellable = true)
	private void getRainGradient(float progress, CallbackInfoReturnable<Float> ci) {
		if (ClientTime.weatherEnabled) {
			ci.setReturnValue(ClientTime.rain ? 1.0f : 0.0f);
			return;
		}
	}

	@Inject(at = @At("TAIL"), method = "getThunderGradient", cancellable = true)
	private void getThunderGradient(float progress, CallbackInfoReturnable<Float> ci) {
		if (ClientTime.weatherEnabled) {
			ci.setReturnValue(ClientTime.thunder ? 1.0f : 0.0f);
			return;
		}
	}
}
