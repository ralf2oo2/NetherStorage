package net.minecraft.src;

import org.lwjgl.opengl.GL11;

import java.awt.image.BufferedImage;

public class R2TextureAnimationBlock extends ModTextureAnimation{
    public R2TextureAnimationBlock(int slot, int size, BufferedImage source, int rate) {
        super(slot, size, 1, source, rate);
    }

    @Override
    public void bindImage(RenderEngine renderEngine1) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, renderEngine1.getTexture("/netherstorage/terrain.png"));
    }
}
