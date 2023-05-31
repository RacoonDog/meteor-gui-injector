package org.example;

import io.github.racoondog.meteorguiinjector.api.GuiContainer;
import io.github.racoondog.meteorguiinjector.api.GuiInjector;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.gui.GuiTheme;
import meteordevelopment.meteorclient.gui.widgets.containers.WWindow;
import meteordevelopment.meteorclient.gui.widgets.pressable.WButton;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.AddServerScreen;

@Environment(EnvType.CLIENT)
public class ExampleAddon extends MeteorAddon {
    @Override
    public void onInitialize() {
        GuiInjector.hookScreen(AddServerScreen.class, new GuiContainer<>("Wow this is a window!") {
            @Override
            public void initWidgets(GuiTheme theme, WWindow window, AddServerScreen screen) {
                window.add(theme.label("And this is text inside the window!"));
                window.add(theme.horizontalSeparator());
                WButton button = window.add(theme.button("click me to crash")).expandX().widget();
                button.action = () -> System.exit(-1);
            }
        });
    }

    @Override
    public String getPackage() {
        return "org.example";
    }
}
