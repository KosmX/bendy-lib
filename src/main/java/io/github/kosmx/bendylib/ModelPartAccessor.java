package io.github.kosmx.bendylib;

import io.github.kosmx.bendylib.impl.IModelPartAccessor;
import net.minecraft.client.model.ModelPart;

import java.util.List;
import java.util.Map;

/**
 * Access to children and cuboids in {@link ModelPart}
 * Don't have to reinterpret the object...
 */
public final class ModelPartAccessor {

    public static Map<String,ModelPart> getChildren(ModelPart modelPart){
        return ((IModelPartAccessor)modelPart).getChildren();
    }

    public static MutableCuboid getCuboid(ModelPart modelPart, int index){
        return (MutableCuboid) getCuboids(modelPart).get(index);
    }

    public static List<ModelPart.Cuboid> getCuboids(ModelPart modelPart){
        return ((IModelPartAccessor)modelPart).getCuboids();
    }
}
