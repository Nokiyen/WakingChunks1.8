package noki.wakingchunks.chunkawaker;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import noki.wakingchunks.WakingChunksData;


/**********
 * @class ChunkAwakerItemBlock
 *
 * @description
 * @description_en
 */
public class ChunkAwakerItemBlock extends ItemBlock {
	
	//******************************//
	// define member variables.
	//******************************//

	
	//******************************//
	// define member methods.
	//******************************//
	public ChunkAwakerItemBlock(Block block) {
		
		super(block);
		this.setHasSubtypes(true);
		this.setUnlocalizedName(WakingChunksData.chunkAwakerName);
		
	}
	
	@Override
	public int getMetadata(int metadata) {
		
		return MathHelper.clamp_int(metadata, 0, 5);
		
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		
		return this.getUnlocalizedName() + "." + itemstack.getItemDamage();
		
	}
	
/*	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister) {
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int metadata) {
		
		return this.block.getIcon(0, metadata);
	
	}*/
	
}
