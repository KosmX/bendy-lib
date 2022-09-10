package io.github.kosmx.bendylib.forge;

import io.github.kosmx.bendylib.PlatformUtils;
import io.github.kosmx.bendylib.compat.tr7zw.TDSkinCompat;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.loading.FMLLoader;

@Mod("bendylib")
public class ForgeModInterface {
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
                TDSkinCompat.init();
            }
        }
    }
}
