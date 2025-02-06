package tfar.damageenchantsmyinventory.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;
import tfar.damageenchantsmyinventory.init.ModBlocks;
import tfar.damageenchantsmyinventory.init.ModEntityTypes;
import tfar.damageenchantsmyinventory.network.server.C2SBlinkPacket;
import tfar.damageenchantsmyinventory.platform.Services;

public class ModClientForge {


    public static void init(IEventBus bus) {
        bus.addListener(ModClientForge::setup);
        MinecraftForge.EVENT_BUS.addListener(ModClientForge::keyPress);
    }

    static void setup(FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.INFERNAL_FIRE, RenderType.cutout());
        EntityRenderers.register(ModEntityTypes.SMALL_TNT,SmallTntRenderer::new);
    }

    static void keyPress(InputEvent.Key event) {
        boolean matches = Minecraft.getInstance().options.keyShift.matches(event.getKey(), event.getScanCode());
        if (matches && event.getAction() == GLFW.GLFW_PRESS) {
            Services.PLATFORM.sendToServer(new C2SBlinkPacket());
        }
    }

}
