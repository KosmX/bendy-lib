package io.github.kosmx.bendylib.mixin;

import io.github.kosmx.bendylib.MutableCuboid;
import io.github.kosmx.bendylib.impl.ICuboid;
import io.github.kosmx.bendylib.impl.IModelPartAccessor;
import net.minecraft.client.model.ModelPart;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Mixin(ModelPart.class)
public class IModelPartMixin implements IModelPartAccessor {

    @Shadow @Final private Map<String, ModelPart> children;

    @Shadow @Final private List<ModelPart.Cuboid> cuboids;

    @Override
    public List<ModelPart.Cuboid> getCuboids() {
        return cuboids;
    }

    @Override
    public Map<String, ModelPart> getChildren() {
        return children;
    }

    @Inject(method = "copyTransform", at = @At("RETURN"))
    private void copyTransformExtended(ModelPart part, CallbackInfo ci){
        Iterator<ModelPart.Cuboid> iterator0 = ((IModelPartAccessor)part).getCuboids().iterator();
        Iterator<ModelPart.Cuboid> iterator1 = cuboids.iterator();

        while (iterator0.hasNext() && iterator1.hasNext()){
            MutableCuboid cuboid1 = (MutableCuboid) iterator1.next();
            MutableCuboid cuboid0 = (MutableCuboid) iterator0.next();
            cuboid1.copyStateFrom(cuboid0);
        }

    }
}
