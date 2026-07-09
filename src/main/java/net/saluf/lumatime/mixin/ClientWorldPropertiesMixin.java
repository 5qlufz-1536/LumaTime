package net.saluf.lumatime.mixin;

import net.minecraft.client.world.ClientWorld;
import net.saluf.lumatime.LumaTime;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientWorld.Properties.class)
public class ClientWorldPropertiesMixin {
	@Inject(at = @At("TAIL"), method = "getTimeOfDay", cancellable = true)
	private void getTimeOfDay(CallbackInfoReturnable<Long> ci) {
		if (LumaTime.timeEnabled) {
			ci.setReturnValue(LumaTime.time);
			return;
		}
	}

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
}
