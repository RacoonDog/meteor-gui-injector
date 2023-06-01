package io.github.racoondog.meteorguiinjector.impl;

import io.github.racoondog.meteorguiinjector.api.ScreenElement;
import meteordevelopment.meteorclient.gui.GuiThemes;
import meteordevelopment.meteorclient.gui.utils.Cell;
import meteordevelopment.meteorclient.gui.widgets.containers.WContainer;
import meteordevelopment.meteorclient.gui.widgets.containers.WWindow;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

import java.util.List;

import static meteordevelopment.meteorclient.utils.Utils.getWindowHeight;
import static meteordevelopment.meteorclient.utils.Utils.getWindowWidth;

@Environment(EnvType.CLIENT)
public class InjectedScreenContainer extends ScreenContainer {
    private final List<ScreenElement> elements;

    public InjectedScreenContainer(List<ScreenElement> elements) {
        super(GuiThemes.get());

        this.elements = elements;
    }

    @Override
    public void initWidgets() {
        add(new WWindowController());
    }

    private WWindow createWindow(WContainer container, String name) {
        WWindow window = theme.window(name);
        window.id = name;
        window.padding = 0;
        window.spacing = 0;

        container.add(window);
        window.view.scrollOnlyWhenMouseOver = true;
        window.view.hasScrollBar = false;
        window.view.spacing = 0;

        return window;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (var element : elements) {
            if (element.mouseClicked(mouseX, mouseY, button)) return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        for (var element : elements) {
            if (element.mouseReleased(mouseX, mouseY, button)) return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        elements.forEach(e -> e.mouseMoved(mouseX, mouseY));
        super.mouseMoved(mouseX, mouseY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        for (var element : elements) {
            if (element.mouseScrolled(mouseX, mouseY, amount)) return true;
        }
        return super.mouseScrolled(mouseX, mouseY, amount);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        for (var element : elements) {
            if (element.keyReleased(keyCode, scanCode, modifiers)) return true;
        }
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        for (var element : elements) {
            if (element.keyPressed(keyCode, scanCode, modifiers)) return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int keyCode) {
        for (var element : elements) {
            if (element.charTyped(chr, keyCode)) return true;
        }
        return super.charTyped(chr, keyCode);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        elements.forEach(e -> e.render(matrices, mouseX, mouseY, delta));
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        elements.forEach(e -> e.resize(client, width, height));
        super.resize(client, width, height);
    }

    @Override
    public void close() {
        elements.forEach(ScreenElement::close);
        super.close();
    }

    @Override
    public void removed() {
        elements.forEach(ScreenElement::removed);
        super.removed();
    }

    protected class WWindowController extends WContainer {
        @Override
        public void init() {
            for (var element : elements) {
                WWindow window = createWindow(this, element.name);
                element.initWidgets(window);
            }
        }

        @Override
        protected void onCalculateWidgetPositions() {
            double pad = theme.scale(4);
            double h = theme.scale(40);

            double x = this.x + pad;
            double y = this.y;

            for (Cell<?> cell : cells) {
                double windowWidth = getWindowWidth();
                double windowHeight = getWindowHeight();

                if (x + cell.width > windowWidth) {
                    x = x + pad;
                    y += h;
                }

                if (x > windowWidth) {
                    x = windowWidth / 2.0 - cell.width / 2.0;
                    if (x < 0) x = 0;
                }
                if (y > windowHeight) {
                    y = windowHeight / 2.0 - cell.height / 2.0;
                    if (y < 0) y = 0;
                }

                cell.x = x;
                cell.y = y;

                cell.width = cell.widget().width;
                cell.height = cell.widget().height;

                cell.alignWidget();

                x += cell.width + pad;
            }
        }
    }
}
