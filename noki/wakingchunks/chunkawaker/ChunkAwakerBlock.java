package noki.wakingchunks.chunkawaker;

import java.util.List;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;
import noki.wakingchunks.ChunkManager;
import noki.wakingchunks.WakingChunksData;
//import noki.wakingchunks.cc.PeripheralChunkAwaker;
import noki.wakingchunks.event.EventWorldRender;
import noki.wakingchunks.packet.PacketBreakMessage;
import noki.wakingchunks.packet.PacketHandler;
import noki.wakingchunks.packet.PacketRequestMessage;


/**********
 * @class ChunkAwakerBlock
 *
 * @description
 * @description_en
 */
public class ChunkAwakerBlock extends BlockContainer {
	
	//******************************//
	// define member variables.
	//******************************//
//	private IIcon[] icons;
	private static final PropertyInteger METADATA = PropertyInteger.create("metadata", 0, 5);


	//******************************//
	// define member methods.
	//******************************//
	public ChunkAwakerBlock(String unlocalizedName) {
		
		super(Material.glass);
		this.setUnlocalizedName(unlocalizedName);
		this.setHardness(0.3F);
		this.setStepSound(soundTypeGlass);
		this.setDefaultState(this.blockState.getBaseState().withProperty(METADATA, 0));
		this.setCreativeTab(WakingChunksData.tab);
		
	}

	@Override
	public TileEntity createNewTileEntity(World world, int i) {
		
/*		if(WakingChunksData.ccLoaded) {
			return new PeripheralChunkAwaker(world);
		}*/
		return new ChunkAwakerTile();
		
	}
	
	public int getRenderType() {
		
		return 3;
		
	}
	
/*	@Override
	public int damageDropped(IBlockState state) {
		
//		return ((ELocalState)state.getValue(VARIANT)).getMetaFromState();
		
	}*/
	
	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({ "rawtypes", "unchecked" })	//about List.
	public void getSubBlocks(Item item, CreativeTabs tab, List list) {
		
		for(int i=0; i<=2; i++) {
			list.add(new ItemStack(item, 1, i));
		}
		
	}
	
	@Override
	public boolean isOpaqueCube() {
		
		return false;
		
	}
	
	@Override
	public boolean isFullCube() {
		
		return false;
		
	}
	
	@SideOnly(Side.CLIENT)
	public EnumWorldBlockLayer getBlockLayer() {
		
		return EnumWorldBlockLayer.TRANSLUCENT;
		
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		
		return (Integer)state.getValue(METADATA);
		
	}
	
	@Override
	public IBlockState getStateFromMeta(int metadata) {
		
		return this.getDefaultState().withProperty(METADATA, metadata);
		
	}
	
	@Override
	protected BlockState createBlockState() {
		
		return new BlockState(this, METADATA);
		
	}
	
	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
		
		if(world.isRemote) {
			return;
		}
		
		int metadata = this.getMetaFromState(state);
		if(metadata > 2) {
			return;
		}
		
		String label = ((ChunkAwakerTile)world.getTileEntity(pos)).getLabel(world);
		if(label == null) {
			return;
		}
		
		boolean flag = ChunkManager.loadChunks(world, pos.getX(), pos.getY(), pos.getZ(), metadata, label);
		if(flag == false) {
//			world.setBlockMetadataWithNotify(posX, posY, posZ, metadata+3, 3);
			world.setBlockState(pos, this.getStateFromMeta(metadata+3), 2);
		}
		
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state,
			EntityPlayer player, EnumFacing facing, float f1, float f2, float f3) {

		//	turn on or off for showing chunks.
		if(player.isSneaking()) {
			if(world.isRemote) {
				if(EventWorldRender.loadedChunks != null) {
					EventWorldRender.loadedChunks = null;
				}
				else {
					PacketHandler.instance.sendToServer(new PacketRequestMessage(
							player.worldObj.provider.getDimensionId(),
							player.chunkCoordX, player.chunkCoordZ,
							Minecraft.getMinecraft().gameSettings.renderDistanceChunks));
				}
			}
			return true;
		}
		//	turn on or off for loading chunks.
		else {
			if(world.isRemote) {
				return true;
			}
						
			String label = ((ChunkAwakerTile)world.getTileEntity(pos)).getLabel(world);
			if(label == null) {
				return false;
			}

			int metadata = this.getMetaFromState(state);
			
			if(metadata < 3) {
				ChunkManager.unloadChunks(world, label);
				world.setBlockState(pos, this.getStateFromMeta(metadata+3), 2);
			}
			else {
				//	World.setBlockState()でbreakBlock()が呼ばれるので、仕方なく処理変更。
				world.setBlockState(pos, this.getStateFromMeta(metadata-3), 2);
				label = ((ChunkAwakerTile)world.getTileEntity(pos)).getLabel(world);
				boolean flag =  ChunkManager.loadChunks(world, pos.getX(), pos.getY(), pos.getZ(), metadata-3, label);
				if(flag == false) {
					world.setBlockState(pos, this.getStateFromMeta(metadata), 2);
				}
/*				boolean flag =  ChunkManager.loadChunks(world, pos.getX(), pos.getY(), pos.getZ(), metadata-3, label);
				if(flag == true) {
					world.setBlockState(pos, this.getStateFromMeta(metadata-3), 2);
				}
				else {
					world.setBlockState(pos, this.getStateFromMeta(metadata), 2);
				}*/
			}
			return true;
		}
		
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {

		if(world.isRemote) {
			//	seems to be uncalled on client...
			EventWorldRender.loadedChunks = null;
		}
		else {
			String label = ((ChunkAwakerTile)world.getTileEntity(pos)).getLabel(world);
			if(label != null) {
				ChunkManager.releaseTicket(world, label);
			}
			PacketHandler.instance.sendToAll(new PacketBreakMessage());
		}
		world.removeTileEntity(pos);
		
	}
	
/*	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int metadata) {
		
		metadata = MathHelper.clamp_int(metadata, 0, 5);
		return this.icons[metadata];
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister icon) {
		
		this.icons = new IIcon[6];
		for(int i=0; i<=5; i++) {
			this.icons[i] = icon.registerIcon(WakingChunksData.ID.toLowerCase()+":ChunkAwaker_"+i);
		}
		
	}*/

}
