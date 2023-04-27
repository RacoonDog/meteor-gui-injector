package io.github.racoondog.meteorguiinjector;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import meteordevelopment.meteorclient.gui.GuiTheme;
import meteordevelopment.meteorclient.gui.GuiThemes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"unchecked", "rawtypes"})
@Environment(EnvType.CLIENT)
public final class GuiHookRegistry {
    private static final Map<Class<? extends Screen>, GuiInfo> REGISTRY = new Object2ObjectOpenHashMap<>();

    @Nullable
    public static <T extends Screen> ScreenContainer createContainer(T rootScreen) {
        Class<T> rootClass = (Class<T>) rootScreen.getClass();
        ScreenInfo info = new ScreenInfo();
        for (var entry : REGISTRY.entrySet()) {
            if (entry.getKey().isAssignableFrom(rootClass)) info.append(entry.getValue());
        }

        return info.isEmpty() ? null : new InjectedScreenContainer<>(rootScreen, info);
    }

    public static <T extends Screen> void hookScreen(Class<T> screenClass, GuiContainer<T> container) {
        GuiInfo<T> info = REGISTRY.get(screenClass);
        if (info == null) {
            info = new GuiInfo<>();
            REGISTRY.put(screenClass, info);
        }
        info.append(container);
    }

    public static class GuiInfo<T extends Screen> {
        public final List<GuiContainer<T>> containers = new ObjectArrayList<>();
        public byte mainSections = 0;
        public void append(GuiContainer<T> container) {
            containers.add(container);
            if (!container.uniqueWindow()) {
                mainSections++;
            }
        }
    }

    public static class ScreenInfo {
        public final List<GuiContainer> containers = new ArrayList<>();
        public byte mainSections = 0;

        public void append(GuiInfo guiInfo) {
            containers.addAll(guiInfo.containers);
            mainSections += guiInfo.mainSections;
        }

        public boolean isEmpty() {
            return containers.isEmpty();
        }

        public boolean subdivideMain() {
            return mainSections >= 2;
        }
    }
}
