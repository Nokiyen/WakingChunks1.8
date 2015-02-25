package noki.wakingchunks;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
//import dan200.computercraft.api.ComputerCraftAPI;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
//import noki.wakingchunks.cc.PeripheralChunkAwaker;
//import noki.wakingchunks.cc.PeripheralProvider;
//import noki.wakingchunks.cc.TurtleChunkAwaker;
import noki.wakingchunks.chunkawaker.ChunkAwakerBlock;
import noki.wakingchunks.chunkawaker.ChunkAwakerItemBlock;


/**********
 * @class WakingChunksData
 *
 * @description
 * @description_en
 */
public class WakingChunksData {

	//******************************//
	// define member variables.
	//******************************//
	//	mod info.
	public static final String ID = "WakingChunks";
	public static final String NAME = "Waking Chunks";
	public static final String VERSION = "1.1.0";
	public static final String CHANNEL = ID;
	public static final String DEPENDENCY = "after:ComputerCraft";
	public static final String PROXY_LOCATION = "noki.wakingchunks.proxy.";
	
	//	creative tab.
	public static CreativeTabs tab;
	
	//	block.
	public static String chunkAwakerName = "chunk_awaker";
	public static Block chunkAwaker;
	public static Block testBlock;
	
	//	cc.
	public static boolean ccLoaded = false;
	public static int chunkAwakerTurtlePID;
	public static Block cc_turtle = null;
	public static Block cc_turtleExpanded = null;
	public static Block cc_turtleAdvanced = null;

	//	debug.
	public static boolean debug = true;

	
	//******************************//
	// define member methods.
	//******************************//
	public static void initPre(FMLPreInitializationEvent event) {
		
		// check cc.
		ccLoaded = Loader.isModLoaded("ComputerCraft");

		// load configs.
		Property prop;
		Configuration cfg = new Configuration(event.getSuggestedConfigurationFile());
		cfg.load();
		prop = cfg.get("upgrade", "chunkAwakerTurtlePID", 1109);
		chunkAwakerTurtlePID = prop.getInt();
		cfg.save();
		
		// create tab.
		tab = new TabWakingChunks();
		
		// register block and tileentity.
		chunkAwaker = new ChunkAwakerBlock(chunkAwakerName);
		GameRegistry.registerBlock(chunkAwaker, ChunkAwakerItemBlock.class, chunkAwakerName);
		
	}

	public static void init() {
	
		// register item json.
		if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {		
			ModelBakery.addVariantName(Item.getItemFromBlock(chunkAwaker),
					ID.toLowerCase() + ":" + "level1_chunk_awaker",
					ID.toLowerCase() + ":" + "level1_off_chunk_awaker",
					ID.toLowerCase() + ":" + "level2_chunk_awaker",
					ID.toLowerCase() + ":" + "level2_off_chunk_awaker",
					ID.toLowerCase() + ":" + "level3_chunk_awaker",
					ID.toLowerCase() + ":" + "level3_off_chunk_awaker");
			registerBlockRender(0, "level1");
			registerBlockRender(1, "level2");
			registerBlockRender(2, "level3");
			registerBlockRender(3, "level1_off");
			registerBlockRender(4, "level2_off");
			registerBlockRender(5, "level3_off");
		}
		
		// register recipes.
		GameRegistry.addRecipe(new ItemStack(chunkAwaker, 1, 0),
				"xyx", "yzy", "xyx", 'x', Blocks.glass, 'y', Items.iron_ingot, 'z', Items.feather);
		GameRegistry.addRecipe(new ItemStack(chunkAwaker, 1, 1),
				" y ", "yzy", " y ", 'y', Items.gold_ingot, 'z', new ItemStack(chunkAwaker, 1, 0));
		GameRegistry.addRecipe(new ItemStack(chunkAwaker, 1, 2),
				" y ", "yzy", " y ", 'y', Items.diamond, 'z', new ItemStack(chunkAwaker, 1, 1));
		GameRegistry.addShapelessRecipe(new ItemStack(chunkAwaker, 1, 1), new ItemStack(chunkAwaker, 1, 2));
		GameRegistry.addShapelessRecipe(new ItemStack(chunkAwaker, 1, 0), new ItemStack(chunkAwaker, 1, 1));		
		
		if(ccLoaded) {
/*			cc_turtle = GameRegistry.findBlock("ComputerCraft", "CC-Turtle");
			cc_turtleExpanded = GameRegistry.findBlock("ComputerCraft", "CC-TurtleExpanded");
			cc_turtleAdvanced = GameRegistry.findBlock("ComputerCraft", "CC-TurtleAdvanced");
			
			GameRegistry.registerTileEntity(PeripheralChunkAwaker.class, "ChunkAwakerTileCC");			
			ComputerCraftAPI.registerPeripheralProvider(new PeripheralProvider());
			ComputerCraftAPI.registerTurtleUpgrade(new TurtleChunkAwaker());*/
		}

	}
	
	public static void registerBlockRender(int metadata, String name) {
		
		ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
		ModelResourceLocation resource =
				new ModelResourceLocation(ID.toLowerCase() + ":" + name + "_" + chunkAwakerName, "inventory");
		mesher.register(Item.getItemFromBlock(chunkAwaker), metadata, resource);
	
	}
	
	public static class TabWakingChunks extends CreativeTabs {
		
		//******************************//
		// define member variables.
		//******************************//
		public static String label = "WakingChunks";

		
		//******************************//
		// define member methods.
		//******************************//
		public TabWakingChunks() {
			
			super(label);
			
		}
		
		@Override
		@SideOnly(Side.CLIENT)
		public Item getTabIconItem() {
			
			return Item.getItemFromBlock(chunkAwaker);

		}
		
	}

}
