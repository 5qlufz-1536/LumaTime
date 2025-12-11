package xyz.ryhon.clienttime.mixin;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.util.math.Vec3d;
import net.minecraft.world.MoonPhase;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.attribute.WeightedAttributeList;
import net.minecraft.world.attribute.WorldEnvironmentAttributeAccess;
import xyz.ryhon.clienttime.ClientTime;

@Mixin(WorldEnvironmentAttributeAccess.class)
public class WorldEnvironmentAttributeAccessMixin {
	@Inject(at = @At("TAIL"), method = "getAttributeValue(Lnet/minecraft/world/attribute/EnvironmentAttribute;)Ljava/lang/Object;", cancellable = true)
	public <Value> void getAttributeValue(EnvironmentAttribute<Value> attribute, CallbackInfoReturnable<Value> ci) {
		if (attribute != EnvironmentAttributes.MOON_PHASE_VISUAL)
			return;

		if (ClientTime.moonPhaseEnabled) {
			for (MoonPhase phase : MoonPhase.values()) {
				if (phase.index == ClientTime.moonPhase) {
					ci.setReturnValue((Value) phase);
					return;
				}
			}
		}
	}

	@Inject(at = @At("TAIL"), method = "getAttributeValue(Lnet/minecraft/world/attribute/EnvironmentAttribute;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/world/attribute/WeightedAttributeList;)Ljava/lang/Object;", cancellable = true)
	public <Value> void getAttributeValueAtPos(EnvironmentAttribute<Value> attribute, Vec3d pos, @Nullable WeightedAttributeList pool, CallbackInfoReturnable<Value> ci) {
		if (attribute != EnvironmentAttributes.MOON_PHASE_VISUAL)
			return;

		if (ClientTime.moonPhaseEnabled) {
			for (MoonPhase phase : MoonPhase.values()) {
				if (phase.index == ClientTime.moonPhase) {
					ci.setReturnValue((Value) phase);
					return;
				}
			}
		}
	}
}
