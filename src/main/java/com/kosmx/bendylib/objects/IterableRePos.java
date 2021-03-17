package com.kosmx.bendylib.objects;

import java.util.function.Consumer;

public interface IterableRePos {
    void iteratePositions(Consumer<IPosWithOrigin> consumer);
}
