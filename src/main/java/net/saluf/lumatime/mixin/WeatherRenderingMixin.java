package net.saluf.lumatime.mixin;

import net.minecraft.client.renderer.WeatherEffectRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome.Precipitation;
import net.saluf.lumatime.LumaTime;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WeatherEffectRenderer.class)
public class WeatherRenderingMixin {
	@Inject(at = @At("HEAD"), method = "getPrecipitationAt", cancellable = true)
	private void getPrecipitationAt(Level world, BlockPos pos, CallbackInfoReturnable<Precipitation> ci) {
		if (LumaTime.weatherEnabled) {
			if (LumaTime.snow) {
				ci.setReturnValue(Precipitation.SNOW);
			} else if (!LumaTime.rain) {
				ci.setReturnValue(Precipitation.NONE);
			}
		}
	}
}
