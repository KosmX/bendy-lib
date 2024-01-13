package io.github.kosmx.bendylib.fabric;

import io.github.kosmx.bendylib.compat.tr7zw.TDSkinCompat;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Init implements ClientModInitializer {
    public static Logger LOGGER = LoggerFactory.getLogger("bendy-lib");

    @Override
    public void onInitializeClient() {
        if (FabricLoader.getInstance().isModLoaded("skinlayers")) {
            LOGGER.info("Initializing 3D Skin Layers compatibility");

            try {
                TDSkinCompat.init();
            } catch(NoClassDefFoundError|ClassNotFoundException e) {
                LOGGER.error("Failed to initialize 3D Skin Layers compatibility");
            }
        }
    }
}
