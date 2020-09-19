package com.kosmx.bendylib.objects;

import com.kosmx.bendylib.math.Matrix4;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.client.util.math.Vector4f;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Quaternion;

/**
 * Bendable cuboid literally...
 * If you don't know the math behind it
 * (Vectors, matrices, quaternions)
 * don't try to edit.
 *
 * Use {@link BendableCuboid#setRotationDeg(float, float)} to bend the cube
 */
public class BendableCuboid implements ICuboid {
    protected final Quad[] sides = new Quad[10];
    //protected final Matrix4f matrix; - Shouldn't use... Change the moveVec instead of this.
    protected Matrix4f lastPosMatrix;
    protected final RepositionableVertex.Pos3f[] positions = new RepositionableVertex.Pos3f[8];
    protected final Vector3f[] origins = new Vector3f[4];
    public final float minX;
    public final float minY;
    public final float minZ;
    public final float maxX;
    public final float maxY;
    public final float maxZ;
    protected final float size;
    protected Vector3f moveVec;
    //to shift the matrix to the center axis
    protected float fixX;
    protected float fixY;
    protected float fixZ;
    protected final Direction direction;


    /**
     *
     * @param u texture U
     * @param v texture V
     * @param x min X
     * @param y min Y
     * @param z min Z
     * @param sizeX X size
     * @param sizeY Y size
     * @param sizeZ Z size
     * @param mirror is mirrored possibly not working
     * @param textureWidth texture width
     * @param textureHeight texture height
     * @param direction Bend direction
     * @param fixX bend axis offset X
     * @param fixY bend axis offset Y
     * @param fixZ bend axis offset Z
     * @param extraX extra X size
     * @param extraY extra Y size
     * @param extraZ extra Z size
     */
    public BendableCuboid(int u, int v, float x, float y, float z, float sizeX, float sizeY, float sizeZ, boolean mirror, float textureWidth, float textureHeight, Direction direction, float fixX, float fixY, float fixZ, float extraX, float extraY, float extraZ) {
        this.direction = direction;
        //this.matrix = new Matrix4f();
        //this.matrix.loadIdentity();
        this.fixX = fixX;
        this.fixY = fixY;
        this.fixZ = fixZ;
        //this.matrix.multiply(direction.getRotationQuaternion()); //Rotate it?
        this.minX = x;
        this.minY = y;
        this.minZ = z;
        this.maxX = x + sizeX;
        this.maxY = y + sizeY;
        this.maxZ = z + sizeZ;
        this.size = direction.getAxis() == Direction.Axis.X ? sizeX + 2*extraX : direction.getAxis() == Direction.Axis.Y ? sizeY + 2*extraY: sizeZ + 2*extraZ;
        //I did a TERRIBLE job with translating the matrix... There must be an issue somewhere else
        switch (direction){
            case DOWN:
                moveVec = new Vector3f(0, -size/2, 0);
                break;
            case NORTH:
                moveVec = new Vector3f(0, 0, -size/2);
                break;
            case SOUTH:
                this.moveVec = new Vector3f(0, 0, size/2);
                break;
            case WEST:
                moveVec = new Vector3f(-size/2, 0, 0);
                break;
            case EAST:
                moveVec = new Vector3f(size/2, 0, 0);
                break;
            default:
                this.moveVec = new Vector3f(0, size/2, 0);
                break;
        }
        float f = x + sizeX;
        float g = y + sizeY;
        float h = z + sizeZ;
        x -= extraX;
        y -= extraY;
        z -= extraZ;
        f += extraX;
        g += extraY;
        h += extraZ;
        if (mirror) {
            float i = f;
            f = x;
            x = i;
        }

        float j = (float)u;
        float k = (float)u + sizeZ;
        float l = (float)u + sizeZ + sizeX;
        float m = (float)u + sizeZ + sizeX + sizeX;
        float n = (float)u + sizeZ + sizeX + sizeZ;
        float o = (float)u + sizeZ + sizeX + sizeZ + sizeX;
        float p = (float)v;
        float q = (float)v + sizeZ;
        float r = (float)v + sizeZ + sizeY;

        //creating the origin vertices
        IVertex[] iVertices = new IVertex[4];

        //create pos suppliers
        for(int i = 0; i < positions.length; i++){
            positions[i] = new RepositionableVertex.Pos3f(new Vector3f());
        }
        //create repositionableVertices and assign pos
        IVertex[] repVertices = new IVertex[8];
        for(int i = 0; i < repVertices.length; i++){
            repVertices[i] = new RepositionableVertex(0, 0, positions[i]);
        }

        //Every direction has a different net

        //for swapping
        IVertex[] swap = new IVertex[4];

        switch (direction){
            case UP:
                origins[0] = new Vector3f(x, y, z);
                origins[1] = new Vector3f(f, y, z);
                origins[2] = new Vector3f(f, y, h);
                origins[3] = new Vector3f(x, y, h);
                iVertices[0] = new Vertex(origins[0], 0, 0);
                iVertices[1] = new Vertex(origins[1], 0, 0);
                iVertices[2] = new Vertex(origins[2], 0, 0);
                iVertices[3] = new Vertex(origins[3], 0, 0);
                this.sides[0] = new Quad(new IVertex[]{iVertices[2], iVertices[3], iVertices[0], iVertices[1]}, k, p, l, q, textureWidth, textureHeight, mirror);
                this.sides[1] = new Quad(new IVertex[]{repVertices[5], repVertices[4], repVertices[7], repVertices[6]}, l, q, m, p, textureWidth, textureHeight, mirror);
                this.sides[2] = new Quad(new IVertex[]{iVertices[0], iVertices[3], repVertices[3], repVertices[0]}, j, q, k, (q + r)/2, textureWidth, textureHeight, mirror);
                this.sides[3] = new Quad(new IVertex[]{repVertices[0], repVertices[3], repVertices[7], repVertices[4]}, j, (q + r)/2, k, r, textureWidth, textureHeight, mirror);
                this.sides[4] = new Quad(new IVertex[]{iVertices[1], iVertices[0], repVertices[0], repVertices[1]}, k, q, l, (q + r)/2, textureWidth, textureHeight, mirror);
                this.sides[5] = new Quad(new IVertex[]{repVertices[1], repVertices[0], repVertices[4], repVertices[5]}, k, (q + r)/2, l, r, textureWidth, textureHeight, mirror);
                this.sides[6] = new Quad(new IVertex[]{iVertices[2], iVertices[1], repVertices[1], repVertices[2]}, l, q, n, (q + r)/2, textureWidth, textureHeight, mirror);
                this.sides[7] = new Quad(new IVertex[]{repVertices[2], repVertices[1], repVertices[5], repVertices[6]}, l, (q + r)/2, n, r, textureWidth, textureHeight, mirror);
                this.sides[8] = new Quad(new IVertex[]{iVertices[3], iVertices[2], repVertices[2], repVertices[3]}, n, q, o, (q + r)/2, textureWidth, textureHeight, mirror);
                this.sides[9] = new Quad(new IVertex[]{repVertices[3], repVertices[2], repVertices[6], repVertices[7]}, n, (q + r)/2, o, r, textureWidth, textureHeight, mirror);
                break;
            case DOWN:
                origins[0] = new Vector3f(x, g, z);
                origins[1] = new Vector3f(f, g, z);
                origins[2] = new Vector3f(f, g, h);
                origins[3] = new Vector3f(x, g, h);
                iVertices[0] = new Vertex(origins[0], 0, 0);
                iVertices[1] = new Vertex(origins[1], 0, 0);
                iVertices[2] = new Vertex(origins[2], 0, 0);
                iVertices[3] = new Vertex(origins[3], 0, 0);
                swap[0] = iVertices[0];
                swap[1] = iVertices[1];
                swap[2] = iVertices[2];
                swap[3] = iVertices[3];
                iVertices[0] = repVertices[4];
                iVertices[1] = repVertices[5];
                iVertices[2] = repVertices[6];
                iVertices[3] = repVertices[7];
                repVertices[4] = swap[0];
                repVertices[5] = swap[1];
                repVertices[6] = swap[2];
                repVertices[7] = swap[3];
                this.sides[0] = new Quad(new IVertex[]{iVertices[2], iVertices[3], iVertices[0], iVertices[1]}, k, p, l, q, textureWidth, textureHeight, mirror);
                this.sides[1] = new Quad(new IVertex[]{repVertices[5], repVertices[4], repVertices[7], repVertices[6]}, l, q, m, p, textureWidth, textureHeight, mirror);
                this.sides[2] = new Quad(new IVertex[]{iVertices[0], iVertices[3], repVertices[3], repVertices[0]}, j, q, k, (q + r)/2, textureWidth, textureHeight, mirror);
                this.sides[3] = new Quad(new IVertex[]{repVertices[0], repVertices[3], repVertices[7], repVertices[4]}, j, (q + r)/2, k, r, textureWidth, textureHeight, mirror);
                this.sides[4] = new Quad(new IVertex[]{iVertices[1], iVertices[0], repVertices[0], repVertices[1]}, k, q, l, (q + r)/2, textureWidth, textureHeight, mirror);
                this.sides[5] = new Quad(new IVertex[]{repVertices[1], repVertices[0], repVertices[4], repVertices[5]}, k, (q + r)/2, l, r, textureWidth, textureHeight, mirror);
                this.sides[6] = new Quad(new IVertex[]{iVertices[2], iVertices[1], repVertices[1], repVertices[2]}, l, q, n, (q + r)/2, textureWidth, textureHeight, mirror);
                this.sides[7] = new Quad(new IVertex[]{repVertices[2], repVertices[1], repVertices[5], repVertices[6]}, l, (q + r)/2, n, r, textureWidth, textureHeight, mirror);
                this.sides[8] = new Quad(new IVertex[]{iVertices[3], iVertices[2], repVertices[2], repVertices[3]}, n, q, o, (q + r)/2, textureWidth, textureHeight, mirror);
                this.sides[9] = new Quad(new IVertex[]{repVertices[3], repVertices[2], repVertices[6], repVertices[7]}, n, (q + r)/2, o, r, textureWidth, textureHeight, mirror);
                break;
            case EAST:
                origins[0] = new Vector3f(x, y, z);
                origins[1] = new Vector3f(x, g, z);
                origins[2] = new Vector3f(x, g, h);
                origins[3] = new Vector3f(x, y, h);
                iVertices[0] = new Vertex(origins[0], 0, 0);
                iVertices[1] = new Vertex(origins[1], 0, 0);
                iVertices[2] = new Vertex(origins[2], 0, 0);
                iVertices[3] = new Vertex(origins[3], 0, 0);
                //west
                this.sides[0] = new Quad(new IVertex[]{iVertices[0], iVertices[3], iVertices[2], iVertices[1]}, j, q, k, r, textureWidth, textureHeight, mirror);
                //down
                this.sides[1] = new Quad(new IVertex[]{repVertices[3], iVertices[3], iVertices[0], repVertices[0]}, k, p, (k + l)/2, q, textureWidth, textureHeight, mirror);
                this.sides[2] = new Quad(new IVertex[]{repVertices[7], repVertices[3], repVertices[0], repVertices[4]}, (k + l)/2, p, l, q, textureWidth, textureHeight, mirror);
                //up
                this.sides[3] = new Quad(new IVertex[]{repVertices[1], iVertices[1], iVertices[2], repVertices[2]}, l, q, (m + l)/2, p, textureWidth, textureHeight, mirror);
                this.sides[4] = new Quad(new IVertex[]{repVertices[5], repVertices[1], repVertices[2], repVertices[6]}, (m + l)/2, q, m, p, textureWidth, textureHeight, mirror);
                //north
                this.sides[5] = new Quad(new IVertex[]{repVertices[0], iVertices[0], iVertices[1], repVertices[1]}, k, q, (k + l)/2, r, textureWidth, textureHeight, mirror);
                this.sides[6] = new Quad(new IVertex[]{repVertices[4], repVertices[0], repVertices[1], repVertices[5]}, (k + l)/2, q, l, r, textureWidth, textureHeight, mirror);
                //east
                this.sides[7] = new Quad(new IVertex[]{repVertices[7], repVertices[4], repVertices[5], repVertices[6]}, l, q, n, r, textureWidth, textureHeight, mirror);
                //south
                this.sides[8] = new Quad(new IVertex[]{iVertices[3], repVertices[3], repVertices[2], iVertices[2]}, (n + o)/2, q, o, r, textureWidth, textureHeight, mirror);
                this.sides[9] = new Quad(new IVertex[]{repVertices[3], repVertices[7], repVertices[6], repVertices[2]}, n, q, (n + o)/2, r, textureWidth, textureHeight, mirror);
                break;
            case WEST:
                origins[0] = new Vector3f(f, y, z);
                origins[1] = new Vector3f(f, g, z);
                origins[2] = new Vector3f(f, g, h);
                origins[3] = new Vector3f(f, y, h);
                iVertices[0] = new Vertex(origins[0], 0, 0);
                iVertices[1] = new Vertex(origins[1], 0, 0);
                iVertices[2] = new Vertex(origins[2], 0, 0);
                iVertices[3] = new Vertex(origins[3], 0, 0);
                swap[0] = iVertices[0];
                swap[1] = iVertices[1];
                swap[2] = iVertices[2];
                swap[3] = iVertices[3];
                iVertices[0] = repVertices[4];
                iVertices[1] = repVertices[5];
                iVertices[2] = repVertices[6];
                iVertices[3] = repVertices[7];
                repVertices[4] = swap[0];
                repVertices[5] = swap[1];
                repVertices[6] = swap[2];
                repVertices[7] = swap[3];
                //west
                this.sides[0] = new Quad(new IVertex[]{iVertices[0], iVertices[3], iVertices[2], iVertices[1]}, j, q, k, r, textureWidth, textureHeight, mirror);
                //down
                this.sides[1] = new Quad(new IVertex[]{repVertices[3], iVertices[3], iVertices[0], repVertices[0]}, k, p, (k + l)/2, q, textureWidth, textureHeight, mirror);
                this.sides[2] = new Quad(new IVertex[]{repVertices[7], repVertices[3], repVertices[0], repVertices[4]}, (k + l)/2, p, l, q, textureWidth, textureHeight, mirror);
                //up
                this.sides[3] = new Quad(new IVertex[]{repVertices[1], iVertices[1], iVertices[2], repVertices[2]}, l, q, (m + l)/2, p, textureWidth, textureHeight, mirror);
                this.sides[4] = new Quad(new IVertex[]{repVertices[5], repVertices[1], repVertices[2], repVertices[6]}, (m + l)/2, q, m, p, textureWidth, textureHeight, mirror);
                //north
                this.sides[5] = new Quad(new IVertex[]{repVertices[0], iVertices[0], iVertices[1], repVertices[1]}, k, q, (k + l)/2, r, textureWidth, textureHeight, mirror);
                this.sides[6] = new Quad(new IVertex[]{repVertices[4], repVertices[0], repVertices[1], repVertices[5]}, (k + l)/2, q, l, r, textureWidth, textureHeight, mirror);
                //east
                this.sides[7] = new Quad(new IVertex[]{repVertices[7], repVertices[4], repVertices[5], repVertices[6]}, l, q, n, r, textureWidth, textureHeight, mirror);
                //south
                this.sides[8] = new Quad(new IVertex[]{iVertices[3], repVertices[3], repVertices[2], iVertices[2]}, (n + o)/2, q, o, r, textureWidth, textureHeight, mirror);
                this.sides[9] = new Quad(new IVertex[]{repVertices[3], repVertices[7], repVertices[6], repVertices[2]}, n, q, (n + o)/2, r, textureWidth, textureHeight, mirror);
                break;
            case SOUTH:
                origins[0] = new Vector3f(x, y, z);
                origins[1] = new Vector3f(f, y, z);
                origins[2] = new Vector3f(f, g, z);
                origins[3] = new Vector3f(x, g, z);
                iVertices[0] = new Vertex(origins[0], 0, 0);
                iVertices[1] = new Vertex(origins[1], 0, 0);
                iVertices[2] = new Vertex(origins[2], 0, 0);
                iVertices[3] = new Vertex(origins[3], 0, 0);
                //down
                this.sides[0] = new Quad(new IVertex[]{repVertices[1], repVertices[0], iVertices[0], iVertices[1]}, k, (p + q)/2, l, q, textureWidth, textureHeight, mirror);
                this.sides[1] = new Quad(new IVertex[]{repVertices[5], repVertices[4], repVertices[0], repVertices[1]}, k, p, l, (q + p)/2, textureWidth, textureHeight, mirror);
                //up
                this.sides[2] = new Quad(new IVertex[]{iVertices[2], iVertices[3], repVertices[3], repVertices[2]}, l, (q + p)/2, m, p, textureWidth, textureHeight, mirror);
                this.sides[3] = new Quad(new IVertex[]{repVertices[2], repVertices[3], repVertices[7], repVertices[6]}, l, q, m, (q + p)/2, textureWidth, textureHeight, mirror);
                //west
                this.sides[4] = new Quad(new IVertex[]{iVertices[0], repVertices[0], repVertices[3], iVertices[3]}, (j + k)/2, q, k, r, textureWidth, textureHeight, mirror);
                this.sides[5] = new Quad(new IVertex[]{repVertices[0], repVertices[4], repVertices[7], repVertices[3]}, j, q, (j + k)/2, r, textureWidth, textureHeight, mirror);
                //north
                this.sides[6] = new Quad(new IVertex[]{iVertices[1], iVertices[0], iVertices[3], iVertices[2]}, k, q, l, r, textureWidth, textureHeight, mirror);
                //south
                this.sides[7] = new Quad(new IVertex[]{repVertices[4], repVertices[5], repVertices[6], repVertices[7]}, n, q, o, r, textureWidth, textureHeight, mirror);
                //east
                this.sides[8] = new Quad(new IVertex[]{repVertices[1], iVertices[1], iVertices[2], repVertices[2]}, l, q, (l + n)/2, r, textureWidth, textureHeight, mirror);
                this.sides[9] = new Quad(new IVertex[]{repVertices[5], repVertices[1], repVertices[2], repVertices[6]}, (l + n)/2, q, n, r, textureWidth, textureHeight, mirror);
                break;
            case NORTH:
                origins[0] = new Vector3f(x, y, h);
                origins[1] = new Vector3f(f, y, h);
                origins[2] = new Vector3f(f, g, h);
                origins[3] = new Vector3f(x, g, h);
                iVertices[0] = new Vertex(origins[0], 0, 0);
                iVertices[1] = new Vertex(origins[1], 0, 0);
                iVertices[2] = new Vertex(origins[2], 0, 0);
                iVertices[3] = new Vertex(origins[3], 0, 0);
                swap[0] = iVertices[0];
                swap[1] = iVertices[1];
                swap[2] = iVertices[2];
                swap[3] = iVertices[3];
                iVertices[0] = repVertices[4];
                iVertices[1] = repVertices[5];
                iVertices[2] = repVertices[6];
                iVertices[3] = repVertices[7];
                repVertices[4] = swap[0];
                repVertices[5] = swap[1];
                repVertices[6] = swap[2];
                repVertices[7] = swap[3];
                //down
                this.sides[0] = new Quad(new IVertex[]{repVertices[1], repVertices[0], iVertices[0], iVertices[1]}, k, (p + q)/2, l, q, textureWidth, textureHeight, mirror);
                this.sides[1] = new Quad(new IVertex[]{repVertices[5], repVertices[4], repVertices[0], repVertices[1]}, k, p, l, (q + p)/2, textureWidth, textureHeight, mirror);
                //up
                this.sides[2] = new Quad(new IVertex[]{iVertices[2], iVertices[3], repVertices[3], repVertices[2]}, l, (q + p)/2, m, p, textureWidth, textureHeight, mirror);
                this.sides[3] = new Quad(new IVertex[]{repVertices[2], repVertices[3], repVertices[7], repVertices[6]}, l, q, m, (q + p)/2, textureWidth, textureHeight, mirror);
                //west
                this.sides[4] = new Quad(new IVertex[]{iVertices[0], repVertices[0], repVertices[3], iVertices[3]}, (j + k)/2, q, k, r, textureWidth, textureHeight, mirror);
                this.sides[5] = new Quad(new IVertex[]{repVertices[0], repVertices[4], repVertices[7], repVertices[3]}, j, q, (j + k)/2, r, textureWidth, textureHeight, mirror);
                //north
                this.sides[6] = new Quad(new IVertex[]{iVertices[1], iVertices[0], iVertices[3], iVertices[2]}, k, q, l, r, textureWidth, textureHeight, mirror);
                //south
                this.sides[7] = new Quad(new IVertex[]{repVertices[4], repVertices[5], repVertices[6], repVertices[7]}, n, q, o, r, textureWidth, textureHeight, mirror);
                //east
                this.sides[8] = new Quad(new IVertex[]{repVertices[1], iVertices[1], iVertices[2], repVertices[2]}, l, q, (l + n)/2, r, textureWidth, textureHeight, mirror);
                this.sides[9] = new Quad(new IVertex[]{repVertices[5], repVertices[1], repVertices[2], repVertices[6]}, (l + n)/2, q, n, r, textureWidth, textureHeight, mirror);
                break;
            default:
                System.out.println("[Bendy-lib] Error while creating cuboid: unknown direction: " + direction + "." );
                throw new IllegalArgumentException("unknown direction:" + direction);
        }
        Matrix4f matrix4f = new Matrix4f();
        matrix4f.loadIdentity();
        setRotation(matrix4f);
        //setRotationDeg(-45, -17); //debug stuff
    }

    public Matrix4f setRotation(Matrix4f rotation){
        Matrix4f shift = new Matrix4f();
        shift.loadIdentity();
        Matrix4f length = Matrix4f.translate(this.moveVec.getX(), this.moveVec.getY(), this.moveVec.getZ());
        shift.multiply(length);
        shift.multiply(Matrix4f.translate(this.fixX, this.fixY, this.fixZ));
        shift.multiply(rotation);
        shift.multiply(Matrix4f.translate(-this.fixX, -this.fixY, -this.fixZ));
        Matrix4f midpoint = shift.copy();
        setMiddle(midpoint);
        setPoints(midpoint, 0);
        shift.multiply(Matrix4f.translate(this.fixX, this.fixY, this.fixZ));
        shift.multiply(rotation);
        shift.multiply(Matrix4f.translate(-this.fixX, -this.fixY, -this.fixZ));
        shift.multiply(length);
        setPoints(shift, 4);
        this.lastPosMatrix = shift; //Store it for later use
        return shift;
    }

    private void setMiddle(Matrix4f matrix){

        /*setup vectors
        heightAxis, vector in the bend direction
        moveSample, originally copy of heightAxis, 3f: 3D form; 4f: 4D form in Floats

         */
        Vector3f heightAxis = moveVec.copy();
        heightAxis.normalize();
        Vector4f moveSample4f = new Vector4f(this.moveVec.getX(), moveVec.getY(), moveVec.getZ(), 0);
        moveSample4f.transform(matrix);
        Vector3f moveSample3f = new Vector3f(moveSample4f.getX(), moveSample4f.getY(), moveSample4f.getZ());
        moveSample3f.normalize();
        Vector3f cross = this.moveVec.copy();
        cross.normalize();

        /*
            Sample the rotation, calculate the required scale with 1/cos(A)

         */


        float cosh = 1/cross.dot(moveSample3f); // 1/cos
        cross.cross(moveSample3f);
        if(!cross.normalize()) return; //To not try to transform with zero vectors.
        Vector3f rotated = cross.copy();
        rotated.transform(new Matrix3f(heightAxis.getDegreesQuaternion(-90)));
        //rotated.scale(cosh);
        Matrix4 transformation = new Matrix4();
        transformation.fromEigenVector(cross, heightAxis, rotated, 1, 1, cosh);
        matrix.multiply(transformation);
    }

    private void setPoints(Matrix4f matrix, int n){
        for (int i = 0; i != 4; i++){
            Vector4f vector4f = new Vector4f(origins[i].getX(), origins[i].getY(), origins[i].getZ(), 1);
            vector4f.transform(matrix);
            positions[i + n].set(new Vector3f(vector4f.getX(), vector4f.getY(), vector4f.getZ()));
        }
    }

    public Matrix4f setRotation(Quaternion quaternion){
        Matrix4f matrix4f = new Matrix4f();
        matrix4f.loadIdentity();
        matrix4f.multiply(quaternion);
        return setRotation(matrix4f);
    }

    public Matrix4f setRotationRad(float axisf, float value){
        value = value/2;
        Vector3f axis = new Vector3f((float) Math.cos(axisf), 0, (float) Math.sin(axisf));
        Matrix3f matrix3f = new Matrix3f(direction.getRotationQuaternion());
        axis.transform(matrix3f);
        return this.setRotation(axis.getRadialQuaternion(value));
    }

    /**
     * Set the bend's rotation
     * @param axis rotation axis in deg
     * @param val rotation's value in deg
     * @return Rotated Matrix4f
     */
    public Matrix4f setRotationDeg(float axis, float val){
        return this.setRotationRad(axis * 0.0174533f, val * 0.0174533f);
    }

    @Override
    public void render(MatrixStack.Entry matrices, VertexConsumer vertexConsumer, float red, float green, float blue, float alpha, int light, int overlay) {
        for(Quad quad:sides){
            quad.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        }
    }

    public Matrix4f getLastPosMatrix(){
        return this.lastPosMatrix.copy();
    }

    /**
     * A replica of {@link ModelPart.Quad}
     * with IVertex and render()
     */
    public static class Quad{
        public final IVertex[] vertices;

        public Quad(IVertex[] vertices, float u1, float v1, float u2, float v2, float squishU, float squishV, boolean flip){
            float f = 0/squishU;
            float g = 0/squishV;
            this.vertices = new IVertex[4];
            this.vertices[0] = vertices[0].remap(u2 / squishU - f, v1 / squishV + g);
            this.vertices[1] = vertices[1].remap(u1 / squishU + f, v1 / squishV + g);
            this.vertices[2] = vertices[2].remap(u1 / squishU + f, v2 / squishV - g);
            this.vertices[3] = vertices[3].remap(u2 / squishU - f, v2 / squishV - g);
            if(flip){
                int i = vertices.length;

                for(int j = 0; j < i / 2; ++j) {
                    IVertex vertex = vertices[j];
                    vertices[j] = vertices[i - 1 - j];
                    vertices[i - 1 - j] = vertex;
                }
            }
        }
        public void render(MatrixStack.Entry matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha){
            Vector3f direction = this.getDirection();
            direction.transform(matrices.getNormal());

            for (int i = 0; i != 4; ++i){
                IVertex vertex = this.vertices[i];
                Vector3f vertexPos = vertex.getPos();
                Vector4f pos = new Vector4f(vertexPos.getX()/16f, vertexPos.getY()/16f, vertexPos.getZ()/16f, 1);
                pos.transform(matrices.getModel());
                vertexConsumer.vertex(pos.getX(), pos.getY(), pos.getZ(), red, green, blue, alpha, vertex.getU(), vertex.getV(), overlay, light, direction.getX(), direction.getY(), direction.getZ());
            }
        }

        /**
         * calculate the normal vector from the vertices' coordinates with cross product
         * @return the normal vector (direction)
         */
        private Vector3f getDirection(){
            Vector3f buf = vertices[3].getPos().copy();
            buf.scale(-1);
            Vector3f vecB = vertices[1].getPos().copy();
            vecB.add(buf);
            buf = vertices[2].getPos().copy();
            buf.scale(-1);
            Vector3f vecA = vertices[0].getPos().copy();
            vecA.add(buf);
            vecA.cross(vecB);
            //Return the cross product, if it's zero then return anything non-zero to not cause crash...
            return vecA.normalize() ? vecA : Direction.NORTH.getUnitVector();
        }
    }
}
