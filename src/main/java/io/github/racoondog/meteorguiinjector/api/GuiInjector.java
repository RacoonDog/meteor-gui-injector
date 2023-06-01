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
        List<ScreenElement> screenElements = GuiHookRegistry.getElements(rootScreen);

        return screenElements.isEmpty() ? null : new InjectedScreenContainer(screenElements);
    }

    public static <T extends Screen> void hookScreen(Class<T> screenClass, GuiHookRegistry.ScreenElementSupplier<T> elementSupplier) {
        GuiHookRegistry.register(screenClass, elementSupplier);
    }
}
