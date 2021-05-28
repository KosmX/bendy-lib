package io.github.kosmx.bendylib.impl;

import net.minecraft.util.math.Vec3f;

public class Vertex implements IVertex{

    protected final Vec3f pos;
    public final float u;
    public final float v;

    public Vertex(float x, float y, float z, float u, float v){
        this(new Vec3f(x, y, z), u, v);
    }
    public Vertex(Vec3f pos, float u, float v){
        this.pos = pos;
        this.u = u;
        this.v = v;
    }

    @Override
    public Vec3f getPos() {
        return this.pos;
    }

    @Override
    public float getU() {
        return this.u;
    }

    @Override
    public float getV() {
        return this.v;
    }

    @Override
    public IVertex remap(float u, float v) {
        return new Vertex(this.pos, u, v);
    }
}
