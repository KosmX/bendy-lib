package io.github.kosmx.bendylib.neoforge;

import io.github.kosmx.bendylib.compat.tr7zw.TDSkinCompat;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod("bendylib")
public class ForgeModInterface {
    public static Logger LOGGER = LoggerFactory.getLogger("bendy-lib");

    public ForgeModInterface() {
        if (!FMLLoader.getDist().isClient()) {
            LOGGER.warn("You're loading a client-only mod on server-side");
            LOGGER.warn("Most likely it won't be a problem");
        }

        if (ModList.get().isLoaded("skinlayers3d")) {
            LOGGER.info("Initializing 3D Skin Layers compatibility");

            try {
                TDSkinCompat.init();
            } catch(NoClassDefFoundError|ClassNotFoundException e) {
                LOGGER.error("Failed to initialize 3D Skin Layers compatibility");
            }
        }
    }
}
