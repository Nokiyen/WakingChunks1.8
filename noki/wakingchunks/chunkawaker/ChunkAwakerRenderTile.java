package noki.wakingchunks.chunkawaker;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;


/**********
 * @class ChunkAwakerTile
 *
 * @description
 * @description_en
 */
public class ChunkAwakerRenderTile extends TileEntitySpecialRenderer {
	
	//******************************//
	// define member variables.
	//******************************//
	
	
	//******************************//
	// define member methods.
	//******************************//	
	@Override
	public void renderTileEntityAt(TileEntity tile, double posX, double posY, double posZ, float partialTick, int destroy) {
		
		if(!tile.getWorld().isRemote) {
			return;
		}		
		
		if(tile.getBlockMetadata() > 2) {
			return;
		}
		
		GlStateManager.pushMatrix();
		
		float angle = (float)((ChunkAwakerTile)tile).getRotation()*360F/(5F*20F);
		GlStateManager.translate(posX, posY, posZ);
		GlStateManager.translate(0.5, 0.5, 0.5);
		GlStateManager.rotate(90F, 1F, 0F, 0F);
		GlStateManager.rotate(angle, 0F, 0F, 1F);
		GlStateManager.translate(-0.5, -0.5, -0.5);
		GlStateManager.translate(0.5, 0.39, 0.9+Math.sin(angle/180F*Math.PI)*0.075);
		
		ItemRenderer renderer = Minecraft.getMinecraft().getItemRenderer();
		renderer.renderItem(null, new ItemStack(Items.feather), TransformType.THIRD_PERSON);		
		
		GlStateManager.popMatrix();
		
	}

}
