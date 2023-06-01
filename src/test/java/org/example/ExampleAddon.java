package org.example;

import io.github.racoondog.meteorguiinjector.api.ScreenElement;
import io.github.racoondog.meteorguiinjector.impl.GuiHookRegistry;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.gui.widgets.containers.WHorizontalList;
import meteordevelopment.meteorclient.gui.widgets.containers.WWindow;
import meteordevelopment.meteorclient.gui.widgets.input.WTextBox;
import meteordevelopment.meteorclient.gui.widgets.pressable.WButton;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.CraftingScreen;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class ExampleAddon extends MeteorAddon {
    @Override
    public void onInitialize() {
        GuiHookRegistry.register(CraftingScreen.class, (theme, screen) -> new ScreenElement(screen, theme, "Chat") {
            WTextBox chatBox;

            @Override
            public void initWidgets(WWindow window) {
                WHorizontalList list = window.add(theme.horizontalList()).expandX().widget();
                chatBox = list.add(theme.textBox("Beep boop.")).expandX().widget();
                chatBox.minWidth = 200;
                WButton send = list.add(theme.button("Send")).expandX().widget();
                send.action = this::send;
            }

            private void send() {
                if (!chatBox.get().isEmpty()) {
                    if (chatBox.get().startsWith("/")) MinecraftClient.getInstance().getNetworkHandler().sendChatCommand(chatBox.get().substring(1));
                    else MinecraftClient.getInstance().getNetworkHandler().sendChatMessage(chatBox.get());
                    chatBox.set("");
                }

                if (chatBox.isFocused()) chatBox.setFocused(false);
            }

            @Override
            public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
                if (chatBox.isFocused() && keyCode == GLFW.GLFW_KEY_ENTER) {
                    send();
                    return true;
                } else if (MinecraftClient.getInstance().options.commandKey.matchesKey(keyCode, scanCode)) {
                    chatBox.setFocused(true);
                    if (chatBox.get().isEmpty()) chatBox.set("/");
                    return true;
                } else if (MinecraftClient.getInstance().options.chatKey.matchesKey(keyCode, scanCode)) {
                    chatBox.setFocused(true);
                    return true;
                }

                return super.keyPressed(keyCode, scanCode, modifiers);
            }
        });
    }

    @Override
    public String getPackage() {
        return "org.example";
    }
}
