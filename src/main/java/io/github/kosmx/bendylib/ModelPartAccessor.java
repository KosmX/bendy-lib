package io.github.kosmx.bendylib;

import io.github.kosmx.bendylib.impl.accessors.IModelPartAccessor;
import net.minecraft.client.model.ModelPart;

import java.util.*;

/**
 * Access to children and cuboids in {@link ModelPart}
 * Don't have to reinterpret the object...
 */
public final class ModelPartAccessor {

    private static final Set<Workaround> compatibilityLevel = new HashSet<>();

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
     * Used to edit or just see the list.
     * @return set containing active workarounds
     */
    public static Set<Workaround> getWorkaroundSet(){
        return compatibilityLevel;
    }

    public static boolean isWorkaroundActive(Workaround workaround){
        return compatibilityLevel.contains(workaround);
    }

    /**
     * Different workarounds to fix shared mod incompatibilities
     */
    public enum Workaround {
        ExportQuads, None;
    }
}
