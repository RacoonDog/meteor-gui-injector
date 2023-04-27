package io.github.racoondog.meteorguiinjector;

import io.github.racoondog.meteorsharedaddonutils.features.TitleScreenCredits;
import meteordevelopment.meteorclient.addons.GithubRepo;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class MeteorGuiInjector extends MeteorAddon {
    @Override
    public void onInitialize() {
        TitleScreenCredits.registerEmptyDrawFunction(this);
    }

    @Override
    public String getPackage() {
        return "io.github.racoondog.meteorguiinjector";
    }

    @Override
    public GithubRepo getRepo() {
        return new GithubRepo("RacoonDog", "meteor-gui-injector", "main");
    }
}
