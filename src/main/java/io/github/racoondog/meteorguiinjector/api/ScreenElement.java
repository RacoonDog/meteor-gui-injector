package io.github.racoondog.meteorguiinjector.api;

import meteordevelopment.meteorclient.gui.GuiTheme;
import meteordevelopment.meteorclient.gui.widgets.containers.WWindow;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;

@Environment(EnvType.CLIENT)
public abstract class ScreenElement {
    protected final Screen parentScreen;
    protected final GuiTheme theme;
    public final String name;

    public ScreenElement(Screen parentScreen, GuiTheme theme, String name) {
        this.parentScreen = parentScreen;
        this.theme = theme;
        this.name = name;
    }

    public void initWidgets(WWindow window) {}

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return false;
    }

    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return false;
    }

    public void mouseMoved(double mouseX, double mouseY) {}

    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        return false;
    }

    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    public boolean charTyped(char chr, int keyCode) {
        return false;
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {}

    public void resize(MinecraftClient client, int width, int height) {}

    public void close() {}

    public void removed() {}
}
