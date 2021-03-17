package com.kosmx.bendylib;

import com.kosmx.bendylib.objects.BendableCuboid;
import com.kosmx.bendylib.objects.ICuboid;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import javax.annotation.Nullable;

/**
 * You can use this to swap a ModelPart to something else.
 * {@link IModelPart#mutate(MutableModelPart)} to do that
 * ((IModelPart)yourModelPart).mutate(yourMutatedModelPart) will do the trick
 *
 * {@link IModelPart#removeMutate(MutableModelPart)} to remove
 * You can use is as the default modelPart in a model.
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

    /*
    @Override
    public Cuboid getRandomCuboid(Random random) {
        if(this.cuboids.size() != 0) return super.getRandomCuboid(random);
        else return new Cuboid()
    }

     *///TODO don't cause crash



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
        this.iCuboids.forEach((cuboid)->{
            cuboid.render(matrices, vertexConsumer, red, green, blue, alpha, light, overlay);
        });
    }

    public void addICuboid(ICuboid cuboid){
        this.iCuboids.add(cuboid);
    }

    /**
     * For Cross-mod compatibility
     * @return the Priority level. If there is a lower level, that will be applied
     * Mods like Mo'bends should use higher e.g. 5
     * Mods like Emotecraft should use lover e.g. 1
     */
    public int getPriority(){
        return 2;
    }

    public boolean isActive(){
        return true;
    }

    /**
     * incompatibility finder tool
     * @return the mod's name or id
     */
    public abstract String modId();


    //The Bendable cuboid generator code
    public BendableCuboid createCuboid(int x, int y, int z, int sizeX, int sizeY, int sizeZ, float extraX, float extraY, float extraZ, Direction direction){
        return BendableCuboid.newBendableCuboid(this.textureOffsetU, this.textureOffsetV, x, y, z, sizeX, sizeY, sizeZ, false, (int) this.textureWidth, (int) this.textureHeight, direction, extraX, extraY, extraZ);
    }
    public MutableModelPart addCuboid(int x, int y, int z, int sizeX, int sizeY, int sizeZ, float extraX, float extraY, float extraZ, Direction direction){
        this.iCuboids.add(this.createCuboid(x, y, z, sizeX, sizeY, sizeZ, extraX, extraY, extraZ, direction));
        return this;
    }

    public BendableCuboid createCuboid(int x, int y, int z, int sizeX, int sizeY, int sizeZ, float extra, Direction direction){
        return this.createCuboid(x, y, z, sizeX, sizeY, sizeZ, extra, extra, extra, direction);
    }
    public MutableModelPart addCuboid(int x, int y, int z, int sizeX, int sizeY, int sizeZ, float extra, Direction direction){
        return this.addCuboid(x, y, z, sizeX, sizeY, sizeZ, extra, extra, extra, direction);
    }
}
