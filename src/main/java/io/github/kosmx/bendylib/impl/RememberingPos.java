package io.github.kosmx.bendylib.impl;


import net.minecraft.util.math.Vec3f;

import java.util.Objects;

public class RememberingPos implements IPosWithOrigin{
    final Vec3f originPos;
    Vec3f currentPos = null;

    public RememberingPos(Vec3f originPos) {
        this.originPos = originPos;
    }

    public RememberingPos(float x, float y, float z){
        this(new Vec3f(x, y, z));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RememberingPos)) return false;

        RememberingPos that = (RememberingPos) o;

        if (!originPos.equals(that.originPos)) return false;
        return Objects.equals(currentPos, that.currentPos);
    }

    @Override
    public int hashCode() {
        int result = originPos.hashCode();
        result = 31 * result + (currentPos != null ? currentPos.hashCode() : 0);
        return result;
    }

    /**
     * It will return with a copy
     * @return copy of the original pos
     */
    @Override
    public Vec3f getOriginalPos() {
        return originPos.copy(); //I won't let anyone to change it.
    }

    @Override
    public Vec3f getPos() {
        return currentPos;
    }

    @Override
    public void setPos(Vec3f vector3f) {
        this.currentPos = vector3f;
    }
}
