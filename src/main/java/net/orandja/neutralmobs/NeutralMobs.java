package net.orandja.neutralmobs;

import net.fabricmc.api.ModInitializer;
import net.minecraft.world.GameRules;
import net.orandja.mcutils.QuickUtils;

import java.lang.reflect.Method;

public class NeutralMobs implements ModInitializer {

    public static GameRules.Key<GameRules.BooleanRule> PASSIVE_MOBS;

    @Override
    public void onInitialize() {
        Method registerM = QuickUtils.quickStaticMethod(GameRules.class, String.class, GameRules.Category.class, GameRules.Type.class);
        Method createM = QuickUtils.quickStaticMethod(GameRules.BooleanRule.class, boolean.class);
        PASSIVE_MOBS = QuickUtils.<GameRules.Key>quickInvoke(registerM, null, "neutralMobs", GameRules.Category.MOBS, QuickUtils.quickInvoke(createM, null, false));
    }
}
