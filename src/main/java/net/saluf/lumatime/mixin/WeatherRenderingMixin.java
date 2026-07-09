package net.saluf.lumatime.mixin;

import net.minecraft.client.render.WeatherRendering;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome.Precipitation;
import net.saluf.lumatime.LumaTime;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WeatherRendering.class)
public class WeatherRenderingMixin {
	@Inject(at = @At("HEAD"), method = "getPrecipitationAt", cancellable = true)
	private void getPrecipitationAt(World world, BlockPos pos, CallbackInfoReturnable<Precipitation> ci) {
		if (LumaTime.weatherEnabled) {
			if (LumaTime.snow) {
				ci.setReturnValue(Precipitation.SNOW);
			} else if (!LumaTime.rain) {
				ci.setReturnValue(Precipitation.NONE);
			}
		}
	}
}