package io.github.racoondog.meteorguiinjector;

import meteordevelopment.meteorclient.gui.GuiTheme;
import meteordevelopment.meteorclient.gui.widgets.containers.WContainer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;

@Environment(EnvType.CLIENT)
public abstract class GuiContainer<T extends Screen> {
    public final String name;

    public GuiContainer(String name) {
        this.name = name;
    }

    /**
     * @param container either {@link meteordevelopment.meteorclient.gui.widgets.containers.WWindow} or {@link meteordevelopment.meteorclient.gui.widgets.containers.WSection}.
     */
    public abstract void initWidgets(GuiTheme theme, WContainer container, T screen);

    /**
     * Override and return {@code true} if this container should have its own unique window. Returning {@code true} will make {@link GuiContainer#initWidgets(GuiTheme, WContainer, Screen)}'s parameter always be of type {@link meteordevelopment.meteorclient.gui.widgets.containers.WWindow}.
     */
    public boolean uniqueWindow() {
        return false;
    }
}
