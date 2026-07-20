package net.saluf.lumatime.mixin;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.attribute.EnvironmentAttributeSystem;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.attribute.SpatialAttributeInterpolator;
import net.minecraft.world.level.MoonPhase;
import net.minecraft.world.phys.Vec3;
import net.saluf.lumatime.LumaTime;

@Mixin(EnvironmentAttributeSystem.class)
public class WorldEnvironmentAttributeAccessMixin {
	@Inject(at = @At("TAIL"), method = "getDimensionValue(Lnet/minecraft/world/attribute/EnvironmentAttribute;)Ljava/lang/Object;", cancellable = true)
	public <Value> void getAttributeValue(EnvironmentAttribute<Value> attribute, CallbackInfoReturnable<Value> ci) {
		if (attribute != EnvironmentAttributes.MOON_PHASE)
			return;

		if (LumaTime.moonPhaseEnabled) {
			for (MoonPhase phase : MoonPhase.values()) {
				if (phase.index() == LumaTime.moonPhase) {
					ci.setReturnValue((Value) phase);
					return;
				}
			}
		}
	}

	@Inject(at = @At("TAIL"), method = "getValue(Lnet/minecraft/world/attribute/EnvironmentAttribute;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/attribute/SpatialAttributeInterpolator;)Ljava/lang/Object;", cancellable = true)
	public <Value> void getAttributeValueAtPos(EnvironmentAttribute<Value> attribute, Vec3 pos, @Nullable SpatialAttributeInterpolator pool, CallbackInfoReturnable<Value> ci) {
		if (attribute != EnvironmentAttributes.MOON_PHASE)
			return;

		if (LumaTime.moonPhaseEnabled) {
			for (MoonPhase phase : MoonPhase.values()) {
				if (phase.index() == LumaTime.moonPhase) {
					ci.setReturnValue((Value) phase);
					return;
				}
			}
		}
	}
}
