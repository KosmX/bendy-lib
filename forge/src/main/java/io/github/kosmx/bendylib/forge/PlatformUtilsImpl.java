package io.github.kosmx.bendylib.forge;

import net.neoforged.fml.ModList;

public class PlatformUtilsImpl {
    public static boolean isSkinLayersPresent() {
        return ModList.get().isLoaded("skinlayers3d");
    }
}
