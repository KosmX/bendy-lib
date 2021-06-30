package io.github.kosmx.bendylib;

import io.github.kosmx.bendylib.impl.accessors.IModelPartAccessor;
import net.minecraft.client.model.ModelPart;

import java.util.*;

/**
 * Access to children and cuboids in {@link ModelPart}
 * Don't have to reinterpret the object...
 */
public final class ModelPartAccessor {

    public static Map<String,ModelPart> getChildren(ModelPart modelPart){
        return ((IModelPartAccessor)modelPart).getChildren();
    }

    /**
     * Get a cuboid, and cast ist to {@link MutableCuboid}
     * @param modelPart
     * @param index
     * @return
     */
    public static MutableCuboid getCuboid(ModelPart modelPart, int index){
        return (MutableCuboid) getCuboids(modelPart).get(index);
    }

    public static List<ModelPart.Cuboid> getCuboids(ModelPart modelPart){
        return ((IModelPartAccessor)modelPart).getCuboids();
    }

    /**
     * Different workarounds to fix shared mod incompatibilities
     * If needed, I advice using {@link Workaround#VanillaDraw}. that is the most stable in any modded environment.
     */
    public enum Workaround {
        ExportQuads, VanillaDraw, None;
    }
}
