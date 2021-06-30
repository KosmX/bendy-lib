package io.github.kosmx.bendylib.mixin;

import io.github.kosmx.bendylib.impl.accessors.DirectionMutator;
import net.minecraft.client.model.ModelPart;
import net.minecraft.util.math.Vec3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ModelPart.Quad.class)
public class QuadMixin implements DirectionMutator {
    @Mutable
    @Shadow @Final public Vec3f direction;

    @Override
    public void setDirection(Vec3f vec3f) {
        this.direction = vec3f;
    }
}
