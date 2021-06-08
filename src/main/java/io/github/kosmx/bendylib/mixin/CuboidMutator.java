package io.github.kosmx.bendylib.mixin;


import io.github.kosmx.bendylib.ICuboidBuilder;
import io.github.kosmx.bendylib.MutableCuboid;
import io.github.kosmx.bendylib.impl.ICuboid;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.ApiStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.HashMap;

@Mixin(ModelPart.Cuboid.class)
@ApiStatus.Experimental
public class CuboidMutator implements MutableCuboid {

    //Store the mutators and the mutator builders.
    HashMap<String, ICuboid> mutators = new HashMap<>();
    HashMap<String, ICuboidBuilder> mutatorBuilders = new HashMap<>();

    ICuboidBuilder.Data partData;

    @Nullable
    ICuboid activeMutator;
    @Nullable
    String activeMutatorID;

    @Inject(method = "<init>", at = @At(value = "RETURN"))
    private void constructor(int u, int v, float x, float y, float z, float sizeX, float sizeY, float sizeZ, float extraX, float extraY, float extraZ, boolean mirror, float textureWidth, float textureHeight, CallbackInfo ci){
        partData = new ICuboidBuilder.Data(u, v, x, y, z, sizeX, sizeY, sizeZ, extraX, extraY, extraZ, mirror, textureWidth, textureHeight);
    }


    @Override
    public boolean registerMutator(String name, ICuboidBuilder<ICuboid> builder) {
        if(mutatorBuilders.containsKey(name)) return false;
        if(builder == null) throw new NullPointerException("builder can not be null");
        mutatorBuilders.put(name, builder);
        return true;
    }

    @Override
    public boolean unregisterMutator(String name) {
        if(mutatorBuilders.remove(name) != null){
            if(name.equals(activeMutatorID)){
                activeMutator = null;
                activeMutatorID = null;
            }
            mutators.remove(name);

            return true;
        }
        return false;
    }

    @Nullable
    @Override
    public Pair<String, ICuboid> getActiveMutator() {
        return activeMutator == null ? null : new Pair<>(activeMutatorID, activeMutator);
    }

    @Nullable
    @Override
    public ICuboid getMutator(String name) {
        return mutators.get(name);
    }

    @Nullable
    @Override
    public ICuboid getAndActivateMutator(String name) {
        if(name == null){
            activeMutatorID = null;
            activeMutator = null;
            return null;
        }
        if(mutatorBuilders.containsKey(name)){
            if(!mutators.containsKey(name)){
                mutators.put(name, mutatorBuilders.get(name).build(partData));
            }
            activeMutatorID = name;
            return activeMutator = mutators.get(name);
        }
        return null;
    }

    @Override
    public void copyStateFrom(MutableCuboid other) {
        if(other.getActiveMutator() == null){
            activeMutator = null;
            activeMutatorID = null;
        }
        else {
            if(this.getAndActivateMutator(other.getActiveMutator().getLeft()) != null){
                activeMutator.copyState(other.getActiveMutator().getRight());
            }
        }
    }

    @Inject(method = "renderCuboid", at = @At(value = "HEAD"), cancellable = true)
    private void renderRedirect(MatrixStack.Entry entry, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha, CallbackInfo ci){
        if(getActiveMutator() != null){
            getActiveMutator().getRight().render(entry, vertexConsumer, red, green, blue, alpha, light, overlay);
            if(getActiveMutator().getRight().disableAfterDraw()) {
                activeMutator = null; //mutator lives only for one render cycle
                activeMutatorID = null;
            }
            ci.cancel();
        }
    }
}
