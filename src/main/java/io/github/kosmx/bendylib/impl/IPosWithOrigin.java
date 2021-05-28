package io.github.kosmx.bendylib.impl;

import net.minecraft.util.math.Vec3f;

/**
 * Position vector what is storing its original position
 */
public interface IPosWithOrigin {
    /**
     * Return a COPY of its original position
     * @return this.vector.copy()
     */
    Vec3f getOriginalPos();

    /**
     * @return current position
     */
    Vec3f getPos();

    /**
     * set a new value to current pos
     * @param vector3f new position
     */
    void setPos(Vec3f vector3f);
}
