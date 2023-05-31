package io.github.racoondog.meteorguiinjector.impl;

import io.github.racoondog.meteorguiinjector.api.GuiContainer;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import meteordevelopment.meteorclient.gui.GuiTheme;
import meteordevelopment.meteorclient.gui.widgets.containers.WContainer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;

import java.util.List;
import java.util.Map;

@SuppressWarnings({"unchecked", "rawtypes"})
@Environment(EnvType.CLIENT)
public final class GuiHookRegistry {
    private static final Map<Class<? extends Screen>, List<GuiContainer<? extends Screen>>> CONTAINER_REGISTRY = new Object2ObjectOpenHashMap<>();
    private static final Map<Class<? extends Screen>, List<DirectScreenRenderer<? extends Screen>>> EXTRAS_REGISTRY = new Object2ObjectOpenHashMap<>();

    public static <T extends Screen> List<GuiContainer<T>> getContainers(T screen) {
        Class<T> rootClass = (Class<T>) screen.getClass();
        List<GuiContainer<T>> containers = new ObjectArrayList<>();

        for (var entry : CONTAINER_REGISTRY.entrySet()) {
            if (entry.getKey().isAssignableFrom(rootClass)) containers.addAll((List) entry.getValue());
        }

        return containers;
    }

    public static <T extends Screen> List<DirectScreenRenderer<T>> getExtras(T screen) {
        Class<T> rootClass = (Class<T>) screen.getClass();
        List<DirectScreenRenderer<T>> extras = new ObjectArrayList<>();

        for (var entry : EXTRAS_REGISTRY.entrySet()) {
            if (entry.getKey().isAssignableFrom(rootClass)) extras.addAll((List) entry.getValue());
        }

        return extras;
    }

    public static <T extends Screen> void register(Class<T> screenClass, GuiContainer<T> container) {
        CONTAINER_REGISTRY.computeIfAbsent(screenClass, o -> new ObjectArrayList<>()).add(container);
    }

    public static <T extends Screen> void register(Class<T> screenClass, DirectScreenRenderer<T> renderer) {
        EXTRAS_REGISTRY.computeIfAbsent(screenClass, o -> new ObjectArrayList<>()).add(renderer);
    }

    @FunctionalInterface
    public interface DirectScreenRenderer<T extends Screen> {
        void initWidgets(GuiTheme theme, WContainer container, T screen);
    }
}
