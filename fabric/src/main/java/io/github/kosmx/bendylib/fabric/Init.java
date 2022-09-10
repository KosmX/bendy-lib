package io.github.kosmx.bendylib.fabric;

import io.github.kosmx.bendylib.PlatformUtils;
import io.github.kosmx.bendylib.compat.tr7zw.TDSkinCompat;
import net.fabricmc.api.ClientModInitializer;

public class Init implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        if (PlatformUtils.isSkinLayersPresent()) {
            TDSkinCompat.init();
        }
    }
}
