package com.kosmx.bendylib.objects;

import net.minecraft.client.util.math.Vector3f;

public interface IPosWithOrigin {
    Vector3f getOriginalPos();
    Vector3f getPos();
    void setPos(Vector3f vector3f);
}
