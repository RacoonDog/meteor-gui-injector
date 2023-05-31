package io.github.racoondog.meteorguiinjector.api;

import io.github.racoondog.meteorguiinjector.impl.GuiHookRegistry;
import io.github.racoondog.meteorguiinjector.impl.InjectedScreenContainer;
import io.github.racoondog.meteorguiinjector.impl.ScreenContainer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Environment(EnvType.CLIENT)
public final class GuiInjector {
    @Nullable
    public static <T extends Screen> ScreenContainer createContainer(T rootScreen) {
        List<GuiContainer<T>> containers = GuiHookRegistry.getContainers(rootScreen);
        List<GuiHookRegistry.DirectScreenRenderer<T>> extras = GuiHookRegistry.getExtras(rootScreen);

        if (!containers.isEmpty() && !extras.isEmpty()) return new InjectedScreenContainer<>(rootScreen, containers, extras);
        return null;
    }

    public static <T extends Screen> void hookScreen(Class<T> screenClass, GuiContainer<T> container) {
        GuiHookRegistry.register(screenClass, container);
    }
}
