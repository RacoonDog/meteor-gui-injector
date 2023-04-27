package io.github.racoondog.meteorguiinjector;

import meteordevelopment.meteorclient.gui.GuiThemes;
import meteordevelopment.meteorclient.gui.utils.Cell;
import meteordevelopment.meteorclient.gui.widgets.containers.WContainer;
import meteordevelopment.meteorclient.gui.widgets.containers.WSection;
import meteordevelopment.meteorclient.gui.widgets.containers.WWindow;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;

import static meteordevelopment.meteorclient.utils.Utils.getWindowHeight;
import static meteordevelopment.meteorclient.utils.Utils.getWindowWidth;

@Environment(EnvType.CLIENT)
public class InjectedScreenContainer<T extends Screen> extends ScreenContainer {
    private final T parentScreen;
    private final GuiHookRegistry.ScreenInfo screenInfo;

    public InjectedScreenContainer(T parentScreen, GuiHookRegistry.ScreenInfo screenInfo) {
        super(GuiThemes.get());

        this.parentScreen = parentScreen;
        this.screenInfo = screenInfo;
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
    public void init() {
        super.init();
    }

    protected class WWindowController extends WContainer {
        @SuppressWarnings("unchecked")
        @Override
        public void init() {
            WWindow mainWindow = null;

            for (var container : screenInfo.containers) {
                if (container.uniqueWindow() || !screenInfo.subdivideMain()) {
                    WWindow window = createWindow(this, container.name);
                    container.initWidgets(theme, window, parentScreen);
                } else {
                    if (mainWindow == null) mainWindow = createWindow(this, "Main");
                    WSection section = mainWindow.add(theme.section(container.name)).expandX().widget();
                    container.initWidgets(theme, section, parentScreen);
                }
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