package io.github.racoondog.meteorguiinjector.mixin;

import io.github.racoondog.meteorguiinjector.api.GuiInjector;
import io.github.racoondog.meteorguiinjector.impl.ScreenContainer;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.AddServerScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(AddServerScreen.class)
public abstract class AddServerScreenMixin extends Screen {
    @Nullable @Unique private ScreenContainer container;

    private AddServerScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void injectConstructor(Screen parent, BooleanConsumer callback, ServerInfo server, CallbackInfo ci) {
        this.container = GuiInjector.createContainer(this);
    }

    @Inject(method = "init", at = @At("HEAD"))
    private void injectInit(CallbackInfo ci) {
        if (container != null) container.init();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (container != null && container.mouseClicked(mouseX, mouseY, button)) return true;
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (container != null && container.mouseReleased(mouseX, mouseY, button)) return true;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        if (container != null) container.mouseMoved(mouseX, mouseY);
        super.mouseMoved(mouseX, mouseY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (container != null && container.mouseScrolled(mouseX, mouseY, amount)) return true;
        return super.mouseScrolled(mouseX, mouseY, amount);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (container != null && container.keyReleased(keyCode, scanCode, modifiers)) return true;
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (container != null && container.keyPressed(keyCode, scanCode, modifiers)) return true;
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        if (container != null && container.charTyped(chr, modifiers)) return true;
        return super.charTyped(chr, modifiers);
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void injectRender(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (container != null) container.render(matrices, mouseX, mouseY, delta);
    }

    @Inject(method = "resize", at = @At("TAIL"))
    private void injectResize(MinecraftClient client, int width, int height, CallbackInfo ci) {
        if (container != null) container.resize(client, width, height);
    }

    @Override
    public void removed() {
        if (container != null) container.removed();
        super.removed();
    }
}
