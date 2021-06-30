package io.github.kosmx.bendylib.impl.accessors;

import net.minecraft.client.model.ModelPart;

import java.util.List;
import java.util.Map;

/**
 * Basic operation to access cuboid in ModelPart
 */
public interface IModelPartAccessor {

    List<ModelPart.Cuboid> getCuboids();

    Map<String, ModelPart> getChildren(); //easy to search in it :D

}
