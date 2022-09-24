package io.github.kosmx.bendylib.fabric;

import net.fabricmc.loader.api.FabricLoader;

public class PlatformUtilsImpl {

    public static boolean isSkinLayersPresent() {
        return FabricLoader.getInstance().isModLoaded("skinlayers");
    }
}
