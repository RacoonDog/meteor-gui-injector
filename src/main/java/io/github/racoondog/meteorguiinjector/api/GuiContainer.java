package io.github.racoondog.meteorguiinjector.api;

import meteordevelopment.meteorclient.gui.GuiTheme;
import meteordevelopment.meteorclient.gui.widgets.containers.WWindow;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;

@Environment(EnvType.CLIENT)
public abstract class GuiContainer<T extends Screen> {
    public final String name;

    public GuiContainer(String name) {
        this.name = name;
    }

    public abstract void initWidgets(GuiTheme theme, WWindow window, T screen);
}
