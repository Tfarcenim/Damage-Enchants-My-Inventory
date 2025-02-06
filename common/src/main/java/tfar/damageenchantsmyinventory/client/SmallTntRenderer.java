package tfar.damageenchantsmyinventory.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.TntMinecartRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;
import tfar.damageenchantsmyinventory.entity.SmallTnt;

public class SmallTntRenderer  extends EntityRenderer<SmallTnt> {
        private final BlockRenderDispatcher blockRenderer;

        public SmallTntRenderer(EntityRendererProvider.Context pContext) {
            super(pContext);
            this.shadowRadius = 0.5F;
            this.blockRenderer = pContext.getBlockRenderDispatcher();
        }

        public void render(SmallTnt pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
            pPoseStack.pushPose();
            pPoseStack.translate(0.0F, 0.5F, 0.0F);
            int fuse = pEntity.getFuse();
            if ((float)fuse - pPartialTicks + 1.0F < 10.0F) {
                float $$7 = 1.0F - ((float)fuse - pPartialTicks + 1.0F) / 10.0F;
                $$7 = Mth.clamp($$7, 0.0F, 1.0F);
                $$7 *= $$7;
                $$7 *= $$7;
                float scale = 1.0F + $$7 * 0.3F;
                pPoseStack.scale(scale, scale, scale);
            }

            pPoseStack.scale(.5f,.5f,.5f);

            pPoseStack.mulPose(Axis.YP.rotationDegrees(-90.0F));
            pPoseStack.translate(-0.5F, -1, 0.5F);
            pPoseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
            TntMinecartRenderer.renderWhiteSolidBlock(this.blockRenderer, Blocks.TNT.defaultBlockState(), pPoseStack, pBuffer, pPackedLight, fuse / 5 % 2 == 0);
            pPoseStack.popPose();
            super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
        }

        public ResourceLocation getTextureLocation(SmallTnt pEntity) {
            return TextureAtlas.LOCATION_BLOCKS;
        }
    }
    
