package noki.wakingchunks;

import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.MinecraftForge;
import noki.wakingchunks.WakingChunksData;
import noki.wakingchunks.event.EventJoinWorld;
import noki.wakingchunks.packet.PacketHandler;
import noki.wakingchunks.proxy.ProxyCommon;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.Mod.Metadata;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.Side;


/**********
 * @ModName WakingChunksCore
 * 
 * @description
 * 
 * @caution ここはコアファイルなので、原則、具体的な処理をしないよう気を付ける。
 */
@Mod(modid = WakingChunksData.ID, name = WakingChunksData.NAME, version = WakingChunksData.VERSION,
	dependencies = WakingChunksData.DEPENDENCY, useMetadata = true)
public class WakingChunksCore {
	
	//******************************//
	// define member variables.
	//******************************//
	//	core.
	@Instance(value = WakingChunksData.ID)
	public static WakingChunksCore instance;
	@Metadata
	public static ModMetadata metadata;
	@SidedProxy(
			clientSide = WakingChunksData.PROXY_LOCATION + "ProxyClient",
			serverSide = WakingChunksData.PROXY_LOCATION + "ProxyCommon"
	)
	public static ProxyCommon proxy;
	
	public static VersionInfo versionInfo;


	
	//******************************//
	// define member methods.
	//******************************//
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		
		// register about chunk loading.
		ForgeChunkManager.setForcedChunkLoadingCallback(instance, new ChunkManager());
		// register about packet.
		PacketHandler.registerPre();
		// register about Block, Item TileEntity etc.
		WakingChunksData.initPre(event);		
		
		// containing about version notification.
		versionInfo = new VersionInfo(metadata.modId.toLowerCase(), metadata.version, metadata.updateUrl);
		
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		
		// register tile entity.
		proxy.registerTileEntities();
		// register sided event.
		proxy.registerSidedEvent();
		//register event.
		MinecraftForge.EVENT_BUS.register(new EventJoinWorld());
		//register about item json and recipe.
		WakingChunksData.init();
				
	}
        
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		
		//	nothing to do.
		
	}
	
	@EventHandler
	public void onServerStart(FMLServerStartingEvent event) {
		
		versionInfo.notifyUpdate(Side.SERVER);
		
	}

}
