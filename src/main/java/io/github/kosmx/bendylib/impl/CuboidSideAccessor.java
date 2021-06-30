package io.github.kosmx.bendylib.impl;

import net.minecraft.client.model.ModelPart;

/**
 * For a shader fix. see {@link io.github.kosmx.bendylib.ModelPartAccessor.Workaround}
 */
public interface CuboidSideAccessor {
    ModelPart.Quad[] getSides();

    void setSides(ModelPart.Quad[] sides);

    void resetSides();

    void doSideSwapping();
}
