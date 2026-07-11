package net.saluf.lumatime.mixin;

import net.minecraft.world.LunarWorldView;
import net.saluf.lumatime.LumaTime;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LunarWorldView.class)
public interface LunarWorldViewMixin {
	@Inject(method = "getMoonPhase", at = @At("HEAD"), cancellable = true)
	private void lumatime$overrideMoonPhase(CallbackInfoReturnable<Integer> ci) {
		if (LumaTime.moonPhaseEnabled) {
			ci.setReturnValue(LumaTime.moonPhase);
		}
	}
}
