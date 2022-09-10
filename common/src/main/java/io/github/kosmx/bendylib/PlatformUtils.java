package io.github.kosmx.bendylib;

import dev.architectury.injectables.annotations.ExpectPlatform;

public class PlatformUtils {

    @ExpectPlatform
    public static boolean isSkinLayersPresent() {
        throw new AssertionError();
    }
}
