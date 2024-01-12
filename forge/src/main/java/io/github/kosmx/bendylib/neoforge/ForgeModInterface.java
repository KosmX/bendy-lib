package io.github.kosmx.bendylib.neoforge;

import io.github.kosmx.bendylib.PlatformUtils;
import io.github.kosmx.bendylib.compat.tr7zw.TDSkinCompat;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.loading.FMLLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod("bendylib")
public class ForgeModInterface {

    public static Logger LOGGER = LoggerFactory.getLogger("bendy-lib");
    public ForgeModInterface() {
        if (!FMLLoader.getDist().isClient()) {
            System.out.println("[bendy-lib] You're loading a client-only mod on server-side");
            System.out.println("[bendy-lib] Most likely it won't be a problem");
        }
    }

    @Mod.EventBusSubscriber(modid = "bendylib", bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            if (PlatformUtils.isSkinLayersPresent()) {
                try {
                    TDSkinCompat.init(LOGGER);
                } catch(NoClassDefFoundError|ClassNotFoundException e) {
                    LOGGER.error("Failed to initialize 3D Skin Layers compatibility");
                }
            }
        }
    }
}
