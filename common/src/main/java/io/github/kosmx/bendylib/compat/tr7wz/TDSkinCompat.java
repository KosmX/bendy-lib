package io.github.kosmx.bendylib.compat.tr7wz;

import dev.tr7zw.skinlayers.api.MeshTransformer;
import dev.tr7zw.skinlayers.api.SkinLayersAPI;
import io.github.kosmx.bendylib.ModelPartAccessor;
import io.github.kosmx.bendylib.MutableCuboid;
import io.github.kosmx.bendylib.impl.IBendable;
import net.minecraft.client.model.ModelPart;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.math.Vector4f;

import java.util.List;

public class TDSkinCompat {
    public static void init() {
        SkinLayersAPI.setupMeshTransformerProvider(modelPart -> new MeshTransformer() {
            @Override
            public void transform(Vec3f vec3f, Vector4f[] vector4fs) {

            }

            @Override
            public void transform(ModelPart.Cuboid cuboid) {
                MutableCuboid mutableCuboid = (MutableCuboid) cuboid;
                var mutator = mutableCuboid.getActiveMutator();
                if (mutator != null) {

                }
            }
        });
    }

    private static class BendyMeshTransformer implements IBendable {
        private final Direction bendDirection;
        private final float bendX, bendY, bendZ;
        private final Plane basePlane, otherSidePlane;

        private BendyMeshTransformer(Direction bendDirection, float bendX, float bendY, float bendZ, Plane basePlane, Plane otherSidePlane) {
            this.bendDirection = bendDirection;
            this.bendX = bendX;
            this.bendY = bendY;
            this.bendZ = bendZ;
            this.basePlane = basePlane;
            this.otherSidePlane = otherSidePlane;
        }

        @Override
        public Direction getBendDirection() {
            return null;
        }

        @Override
        public float getBendX() {
            return 0;
        }

        @Override
        public float getBendY() {
            return 0;
        }

        @Override
        public float getBendZ() {
            return 0;
        }

        @Override
        public Plane getBasePlane() {
            return null;
        }

        @Override
        public Plane getOtherSidePlane() {
            return null;
        }
    }
}
