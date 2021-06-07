/*package io.github.kosmx.bendylib.mixin;


import io.github.kosmx.bendylib.IModelPart;
import io.github.kosmx.bendylib.MutableModelPart;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(ModelPart.class)
public class ModelPartMixin implements IModelPart {

    @Shadow public boolean visible;

    @Shadow
    public void rotate(MatrixStack matrix) {}

    @Shadow public float textureWidth;
    @Shadow public float textureHeight;
    @Shadow public int textureOffsetU;
    @Shadow public int textureOffsetV;

    protected final ObjectList<MutableModelPart> mutatedParts = new ObjectArrayList<>();

    /**
     * @author KosmX - bendy-lib
     * @param part part, what ypu want to use
     * @return true, if it isn't assigned already.
     *
    @Override
    public boolean mutate(MutableModelPart part) {
        if(mutatedParts.contains(part))return false;
        mutatedParts.add(part);
        return true;
    }

    /**
     * @param part remove, if this is the active mutated.
     * @return is the action success
     *
    @Override
    public boolean removeMutate(MutableModelPart part) {
        return mutatedParts.remove(part);
    }


    /**
     * @author KosmX - bendy-lib
     * @return Active, highest priority mutated part, null if no active or empty

    @Nullable
    @Override
    public MutableModelPart getActiveMutatedPart() {
        MutableModelPart part = null;
        for(MutableModelPart i:this.mutatedParts){
            if(i.isActive() && (part == null || !part.isActive() || part.getPriority() <= i.getPriority())){
                if(part != null && part.getPriority() == i.getPriority()){
                    System.out.println("[bendy-lib] " + part.modId() + " is possibly incompatible with " + i.modId() + ".");
                    return null;
                }else {
                    part = i;
                }
            }
        }
        return part;
    }

    @Override
    public float getTextureWidth() {
        return this.textureWidth;
    }

    @Override
    public float getTextureHeight() {
        return this.textureHeight;
    }

    @Override
    public int getU() {
        return this.textureOffsetU;
    }

    @Override
    public int getV() {
        return this.textureOffsetV;
    }

    /**
     * modified render function. it will be inherited.
     *
    @Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;IIFFFF)V", at = @At(value = "HEAD" ), cancellable = true)
    private void renderInject(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha, CallbackInfo callbackInfo){
        MutableModelPart mutatedPart = this.getActiveMutatedPart();
        if(this.visible && mutatedPart != null){
            matrices.push();
            this.rotate(matrices);
            mutatedPart.render(matrices, vertices, light, overlay, red, green, blue, alpha);
            matrices.pop();
            callbackInfo.cancel(); //if mutate active, don't render the original
        }
    }

}
*/