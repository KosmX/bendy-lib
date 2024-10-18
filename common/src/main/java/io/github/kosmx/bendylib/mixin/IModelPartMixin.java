package io.github.kosmx.bendylib.mixin;

import io.github.kosmx.bendylib.ModelPartAccessor;
import io.github.kosmx.bendylib.MutableCuboid;
import io.github.kosmx.bendylib.impl.accessors.CuboidSideAccessor;
import io.github.kosmx.bendylib.impl.accessors.IModelPartAccessor;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPart.Cuboid;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.client.util.math.Vector4f;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;

import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;

@Mixin(ModelPart.class)
public abstract class IModelPartMixin implements IModelPartAccessor {

    @Shadow @Final private ObjectList<ModelPart> children;

    @Shadow @Final private ObjectList<Cuboid> cuboids;

    @Unique
    private boolean hasMutatedCuboid = false;
    /**
     * VanillaDraw won't cause slowdown in vanilla and will fix many issues.
     * If needed, use {@link IModelPartAccessor#setWorkaround(ModelPartAccessor.Workaround)} to set the workaround function
     * {@link ModelPartAccessor.Workaround#None} to do nothing about it. It will work in Vanilla, but not with Sodium/OF
     */
    private ModelPartAccessor.Workaround workaround = ModelPartAccessor.Workaround.VanillaDraw;

    private MatrixStack.Entry matrices;
    private ModelPart.Cuboid cuboid;

    @Shadow protected abstract void renderCuboids(MatrixStack.Entry entry, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha);

    @Override
    public ObjectList<Cuboid> getCuboids() {
        hasMutatedCuboid = true;
        return cuboids;
    }

    @Override
    public ObjectList<ModelPart> getChildren() {
        return children;
    }

    @Inject(method = "copyPositionAndRotation", at = @At("RETURN"))
    private void copyPositionAndRotationExtended(ModelPart part, CallbackInfo ci){
        if(((IModelPartAccessor)part).getCuboids() == null || cuboids == null) return; // Not copying state
        Iterator<ModelPart.Cuboid> iterator0 = ((IModelPartAccessor)part).getCuboids().iterator();
        Iterator<ModelPart.Cuboid> iterator1 = cuboids.iterator();

        while (iterator0.hasNext() && iterator1.hasNext()){
            MutableCuboid cuboid1 = (MutableCuboid) iterator1.next();
            MutableCuboid cuboid0 = (MutableCuboid) iterator0.next();
            cuboid1.copyStateFrom(cuboid0);
        }
    }

    @Inject(method = "renderCuboids", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/model/ModelPart$Cuboid;method_22704(Lnet/minecraft/client/model/ModelPart$Cuboid;)[Lnet/minecraft/client/model/ModelPart$Quad;"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void captureData(MatrixStack.Entry matrices, VertexConsumer _vertexConsumer, int _light, int _overlay, float _red, float _green, float _blue, float _alpha, CallbackInfo ci, Matrix4f _matrix4f, Matrix3f _matrix3f, ObjectListIterator _var11, ModelPart.Cuboid cuboid){
        this.matrices = matrices;
        this.cuboid = cuboid;
    }
    @Redirect(method = "renderCuboids", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/VertexConsumer;vertex(FFFFFFFFFIIFFF)V"))
    private void renderRedirect(VertexConsumer vertexConsumer, float x, float y, float z, float red, float green, float blue, float alpha, float u, float v, int overlay, int light, float normalX, float normalY, float normalZ){
        MutableCuboid mutCuboid = ((MutableCuboid) (Object) cuboid);
        if(mutCuboid.getActiveMutator() != null){
            mutCuboid.getActiveMutator().getRight().render(matrices, vertexConsumer, red, green, blue, alpha, light, overlay);
            if(mutCuboid.getActiveMutator().getRight().disableAfterDraw()) {
                mutCuboid.clearActiveMutator(); //mutator lives only for one render cycle
            }
        } else {
            vertexConsumer.vertex(x, y, z, red, green, blue, alpha, u, v, overlay, light, normalX, normalY, normalZ);
        }
    }

    @Redirect(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;IIFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelPart;renderCuboids(Lnet/minecraft/client/util/math/MatrixStack$Entry;Lnet/minecraft/client/render/VertexConsumer;IIFFFF)V"), require = 0) //It might not find anything if OF already broke the game
    private void redirectRenderCuboids(ModelPart modelPart, MatrixStack.Entry entry, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha){
        redirectedFunction(modelPart, entry, vertexConsumer, light, overlay, red, green, blue, alpha);
    }

    @Dynamic("render function is replaced with this by Optifine")
    @Redirect(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;IIFFFFZ)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelPart;renderCuboids(Lnet/minecraft/client/util/math/MatrixStack$Entry;Lnet/minecraft/client/render/VertexConsumer;IIFFFF)V"), require = 0)
    private void redirectOF(ModelPart modelPart, MatrixStack.Entry entry, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        redirectedFunction(modelPart, entry, vertexConsumer, light, overlay, red, green, blue, alpha);
    }

    @Unique
    private void redirectedFunction(ModelPart modelPart, MatrixStack.Entry entry, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        if(workaround == ModelPartAccessor.Workaround.ExportQuads){
            for(ModelPart.Cuboid cuboid:cuboids){
                ((CuboidSideAccessor)cuboid).doSideSwapping(); //:D
            }
            renderCuboids(entry, vertexConsumer, light, overlay, red, green, blue, alpha);
            for(ModelPart.Cuboid cuboid:cuboids){
                ((CuboidSideAccessor)cuboid).resetSides(); //:D
            }
        }
        else if(workaround == ModelPartAccessor.Workaround.VanillaDraw){
            if(!hasMutatedCuboid || cuboids.size() == 1 && ((MutableCuboid)cuboids.get(0)).getActiveMutator() == null){
                renderCuboids(entry, vertexConsumer, light, overlay, red, green, blue, alpha);
            }
            else {
                for(ModelPart.Cuboid cuboid:cuboids){
                    // cuboid.renderCuboid reimplementation for 1.16
                    for (int i = 0; i < cuboid.sides.length; i++) {
                        ModelPart.Quad side = cuboid.sides[i];

                        Vector3f dir = side.direction.copy();
                        dir.transform(entry.getNormal());

                        float x = dir.getX();
                        float y = dir.getY();
                        float z = dir.getZ();

                        for (int j = 0; j < side.vertices.length; j++) {
                            ModelPart.Vertex vert = side.vertices[j];

                            float x4 = vert.pos.getX() / 16.0F;
                            float y4 = vert.pos.getY() / 16.0F;
                            float z4 = vert.pos.getZ() / 16.0F;

                            Vector4f vec4f = new Vector4f(x4, y4, z4, 1.0F);
                            vec4f.transform(entry.getModel());

                            vertexConsumer.vertex(vec4f.getX(), vec4f.getY(), vec4f.getZ(), red, green, blue, alpha, vert.u, vert.v, overlay, light, x, y, z);
                        }
                    }
                }
            }
        }
        else {
            renderCuboids(entry, vertexConsumer, light, overlay, red, green, blue, alpha);
        }
    }

    @Override
    public void setWorkaround(ModelPartAccessor.Workaround workaround) {
        this.workaround = workaround;
    }
}
