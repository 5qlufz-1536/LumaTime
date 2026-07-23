package net.saluf.lumatime.mixin;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.biome.Biome.Precipitation;
import net.saluf.lumatime.LumaTime;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientLevel.class)
public class ClientLevelMixin {
	@Inject(at = @At("HEAD"), method = "getPrecipitationAt", cancellable = true)
	private void getPrecipitationAt(BlockPos pos, CallbackInfoReturnable<Precipitation> ci) {
		if (LumaTime.weatherEnabled) {
			if (LumaTime.snow) {
				ci.setReturnValue(Precipitation.SNOW);
			} else if (!LumaTime.rain) {
				ci.setReturnValue(Precipitation.NONE);
			}
		}
	}
}
