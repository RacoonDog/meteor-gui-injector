package io.github.racoondog.meteorguiinjector.impl;

import io.github.racoondog.meteorguiinjector.api.ScreenElement;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import meteordevelopment.meteorclient.gui.GuiTheme;
import meteordevelopment.meteorclient.gui.GuiThemes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;

import java.util.List;
import java.util.Map;

@SuppressWarnings({"unchecked", "rawtypes"})
@Environment(EnvType.CLIENT)
public final class GuiHookRegistry {
    private static final Map<Class<? extends Screen>, List<ScreenElementSupplier<? extends Screen>>> ELEMENTS_REGISTRY = new Object2ObjectOpenHashMap<>();

    public static <T extends Screen> List<ScreenElement> getElements(T screen) {
        Class<T> rootClass = (Class<T>) screen.getClass();
        List<ScreenElement> screenElements = new ObjectArrayList<>();

        for (var entry : ELEMENTS_REGISTRY.entrySet()) {
            if (entry.getKey().isAssignableFrom(rootClass)) {
                for (ScreenElementSupplier element : entry.getValue()) {
                    screenElements.add(element.create(GuiThemes.get(), screen));
                }
            }
        }

        return screenElements;
    }

    public static <T extends Screen> void register(Class<T> screenClass, ScreenElementSupplier<T> renderer) {
        ELEMENTS_REGISTRY.computeIfAbsent(screenClass, o -> new ObjectArrayList<>()).add(renderer);
    }

    @FunctionalInterface
    public interface ScreenElementSupplier<T extends Screen> {
        ScreenElement create(GuiTheme theme, T screen);
    }
}
