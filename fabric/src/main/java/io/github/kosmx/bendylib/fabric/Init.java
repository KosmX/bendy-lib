package io.github.kosmx.bendylib.fabric;

import io.github.kosmx.bendylib.compat.tr7wz.TDSkinCompat;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class Init implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        if (FabricLoader.getInstance().isModLoaded("skinlayers")) {
            TDSkinCompat.init();
        }
    }
}
