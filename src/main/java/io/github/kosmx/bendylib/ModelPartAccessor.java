package io.github.kosmx.bendylib;

import net.minecraft.client.model.ModelPart;

import java.util.List;
import java.util.Map;

/**
 * Basic operation to access cuboid in ModelPart
 */
public interface ModelPartAccessor {

    List<ModelPart.Cuboid> getCuboids();

    Map<String, ModelPart> getChildren(); //easy to search in it :D

}
