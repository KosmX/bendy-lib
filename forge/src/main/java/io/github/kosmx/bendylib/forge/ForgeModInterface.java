package io.github.kosmx.bendylib.forge;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLLoader;

@Mod("bendylib")
public class ForgeModInterface {
    public ForgeModInterface() {
        if (!FMLLoader.getDist().isClient()) {
            System.out.println("[bendy-lib] You're loading a client-only mod on server-side");
            System.out.println("[bendy-lib] Most likely it won't be a problem");
        }
    }
}
