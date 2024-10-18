package io.github.kosmx.bendylib;


import io.github.kosmx.bendylib.impl.ICuboid;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.Nullable;

/**
 * ModelPart to support ICuboids
 *
 * If you want to mutate existing Cuboids, see {@link ModelPartAccessor} and {@link MutableCuboid}
 *
 * This can be used with {@link ICuboid}.
 */
public abstract class MutableModelPart extends ModelPart {

    @Nullable
    @Deprecated
    private MutableModelPart last = null;

    protected final ObjectList<ICuboid> iCuboids = new ObjectArrayList<>();

    public MutableModelPart(Model model) {
        super(model);
    }

    public MutableModelPart(Model model, int textureOffsetU, int textureOffsetV) {
        super(model, textureOffsetU, textureOffsetV);
    }

    public MutableModelPart(int textureWidth, int textureHeight, int textureOffsetU, int textureOffsetV) {
        super(textureWidth, textureHeight, textureOffsetU, textureOffsetV);
    }

    public MutableModelPart(ModelPart modelPart){
        this((int)modelPart.textureWidth, (int)modelPart.textureHeight, modelPart.textureOffsetU, modelPart.textureOffsetV);
    }


    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        super.render(matrices, vertices, light, overlay, red, green, blue, alpha);
        if(!iCuboids.isEmpty()){
            matrices.push();
            this.rotate(matrices);
            this.renderICuboids(matrices.peek(), vertices, light, overlay, red, green, blue, alpha);
            matrices.pop();
        }
    }

    protected void renderICuboids(MatrixStack.Entry matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        this.iCuboids.forEach((cuboid)-> cuboid.render(matrices, vertexConsumer, red, green, blue, alpha, light, overlay));
    }

    public void addICuboid(ICuboid cuboid){
        this.iCuboids.add(cuboid);
    }

}
