package io.github.kosmx.bendylib.impl.accessors;

import io.github.kosmx.bendylib.ModelPartAccessor;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPart.Cuboid;

/**
 * Basic operation to access cuboid in ModelPart
 */
public interface IModelPartAccessor {

    ObjectList<Cuboid> getCuboids();

    ObjectList<ModelPart> getChildren(); //easy to search in it :D

    void setWorkaround(ModelPartAccessor.Workaround workaround);

}
