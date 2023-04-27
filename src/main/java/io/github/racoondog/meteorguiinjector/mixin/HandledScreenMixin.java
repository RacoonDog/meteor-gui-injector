package io.github.racoondog.meteorguiinjector.mixin;

import io.github.racoondog.meteorguiinjector.GuiHookRegistry;
import io.github.racoondog.meteorguiinjector.ScreenContainer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin extends Screen {
    @Nullable @Unique private ScreenContainer container;

    private HandledScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void injectConstructor(ScreenHandler handler, PlayerInventory inventory, Text title, CallbackInfo ci) {
        this.container = GuiHookRegistry.createContainer(this);
    }

    @Inject(method = "init()V", at = @At("TAIL"))
    private void injectInit(CallbackInfo ci) {
        if (container != null) container.init();
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void injectMouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        if (container != null && container.mouseClicked(mouseX, mouseY, button)) {
            cir.setReturnValue(true);
            cir.cancel();
        }
    }

    @Inject(method = "mouseReleased", at = @At("HEAD"), cancellable = true)
    private void injectMouseReleased(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        if (container != null && container.mouseReleased(mouseX, mouseY, button)) {
            cir.setReturnValue(true);
            cir.cancel();
        }
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

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void injectKeyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (container != null && container.keyPressed(keyCode, scanCode, modifiers)) {
            cir.setReturnValue(true);
            cir.cancel();
        }
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

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        if (container != null) container.resize(client, width, height);
        super.resize(client, width, height);
    }

    @Override
    public void removed() {
        if (container != null) container.removed();
        super.removed();
    }
}
