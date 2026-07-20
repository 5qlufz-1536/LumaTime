package net.saluf.lumatime.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biome.Precipitation;
import net.saluf.lumatime.LumaTime;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Biome.class)
public class BiomeMixin {
	@Inject(at = @At("HEAD"), method = "getPrecipitationAt", cancellable = true)
	private void getPrecipitation(BlockPos pos, int seaLevel, CallbackInfoReturnable<Precipitation> ci) {
		if (LumaTime.weatherEnabled && LumaTime.snow) {
			ci.setReturnValue(Precipitation.RAIN);
		}
	}

	@Inject(at = @At("HEAD"), method = "hasPrecipitation", cancellable = true)
	private void hasPrecipitation(CallbackInfoReturnable<Boolean> ci) {
		if (LumaTime.weatherEnabled && LumaTime.snow) {
			ci.setReturnValue(true);
		}
	}
}
