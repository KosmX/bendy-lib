package io.github.kosmx.bendylib.impl.math;

import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.Matrix4f;

public class Matrix4 extends Matrix4f {

    public Matrix4(Matrix4 matrix4) {
        super(matrix4);
    }

    public Matrix4(){}

    /**
     *
     * create a matrix from eigenvectors.
     * vectors can have any non-zero length, the eigenvalues will scale.
     *
     *
     * @param vec1 1st Eigenvector
     * @param vec2 2nd Eigenvector
     * @param vec3 3rd Eigenvector
     * @param val1 1st vector's Eigenvalue
     * @param val2 2nd vector's Eigenvalue
     * @param val3 3rd vector's Eigenvalue
     * The identity eigenvalues are 1! (does not make change)
     */
    public void fromEigenVector(Vector3f vec1, Vector3f vec2, Vector3f vec3, float val1, float val2, float val3){
        this.a00 = vec1.getX();
        this.a10 = vec1.getY();
        this.a20 = vec1.getZ();
        this.a30 = 0;
        this.a01 = vec2.getX();
        this.a11 = vec2.getY();
        this.a21 = vec2.getZ();
        this.a31 = 0;
        this.a02 = vec3.getX();
        this.a12 = vec3.getY();
        this.a22 = vec3.getZ();
        this.a32 = 0;
        this.a03 = 0;
        this.a13 = 0;
        this.a23 = 0;
        this.a33 = 1;

        Matrix4f matrix4f = this.copy();

        /*
            Minecraft matrix multiplication:
            A.multiply(B) equals with A*B
         */

        Matrix4f inverse = new Matrix4(this);
        inverse.invert();

        this.multiply(Matrix4f.scale(val1, val2, val3));
        this.multiply(inverse);
    }

    /**
     *
     * create a matrix from eigenvectors.
     * The eigen values are the length of the vectors
     * (just to make things easier)
     *
     * @param vec1 1st Eigenvector
     * @param vec2 2nd Eigenvector
     * @param vec3 3rd Eigenvector
     */
    public void fromEigenVector(Vector3f vec1, Vector3f vec2, Vector3f vec3){
        fromEigenVector(vec1, vec2, vec3, length(vec1), length(vec2), length(vec3));
    }

    /*
    public static float length(Vector4f vector4f){
        return Math.rt(vector4f.getX() * vector4f.getX() + vector4f.getY() * vector4f.getY() + vector4f.getZ() * vector4f.getZ() + vector4f.getW() * vector4f.getW();
    }

     */
    public static float length(Vector3f vector4f){
        return (float) Math.sqrt(vector4f.getX() * vector4f.getX() + vector4f.getY() * vector4f.getY() + vector4f.getZ() * vector4f.getZ());
    }

}
