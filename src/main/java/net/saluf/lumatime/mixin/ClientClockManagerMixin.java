package net.saluf.lumatime.mixin;

import net.minecraft.client.ClientClockManager;
import net.minecraft.core.Holder;
import net.minecraft.world.clock.WorldClock;
import net.saluf.lumatime.LumaTime;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientClockManager.class)
public class ClientClockManagerMixin {
	@Inject(at = @At("HEAD"), method = "getTotalTicks", cancellable = true)
	private void getTotalTicks(Holder<WorldClock> definition, CallbackInfoReturnable<Long> ci) {
		if (LumaTime.timeEnabled) {
			ci.setReturnValue(LumaTime.time);
		}
	}
}
