package io.github.kosmx.bendylib.impl;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

import java.util.List;

/**
 * Minecraft Cuboid object
 * define it as you wish, render it as you wish!
 * You can use {@link BendableCuboid} to bend parts.
 */
public interface ICuboid {

    /**
     * See {@link BendableCuboid#render(MatrixStack.Entry, VertexConsumer, float, float, float, float, int, int)} how to do it
     * Or you can check the original MC code {@link net.minecraft.client.model.ModelPart#render(MatrixStack, VertexConsumer, int, int, float, float, float, float)}
     *
     * @param matrices Minecraft's Matrix transformation
     * @param vertexConsumer Minecraft Vertex consumer, add vertices to render
     * @param red red
     * @param green green
     * @param blue blue
     * @param alpha alpha
     * @param light light
     * @param overlay overlay
     */
    void render(MatrixStack.Entry matrices, VertexConsumer vertexConsumer, float red, float green, float blue, float alpha, int light, int overlay);

    /**
     * Copy custom state from another cuboid
     * @param other other ICuboid
     */
    void copyState(ICuboid other);

    /**
     * Disable mutation after invoking {@link ICuboid#render(MatrixStack.Entry, VertexConsumer, float, float, float, float, int, int)}
     * @return true or false...
     */
    default boolean disableAfterDraw(){
        return true;
    }

    /**
     * Convert custom Quads to {@link net.minecraft.client.model.ModelPart.Quad}
     * Needed for Shader fix
     * @return list of converted quads
     */
    default List<ModelPart.Quad> getQuads(){
        return null;
    }
}
