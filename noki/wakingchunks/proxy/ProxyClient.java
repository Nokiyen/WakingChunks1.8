package noki.wakingchunks.proxy;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import noki.wakingchunks.chunkawaker.ChunkAwakerRenderTile;
import noki.wakingchunks.chunkawaker.ChunkAwakerTile;
import noki.wakingchunks.event.EventWorldRender;
import noki.wakingchunks.proxy.ProxyCommon;


/**********
 * @class ProxyClient
 *
 * @description クライアント用proxyクラス。
 * @description_en Proxy class for Client.
 */
public class ProxyClient extends ProxyCommon {
	
	//******************************//
	// define member variables.
	//******************************//


	//******************************//
	// define member methods.
	//******************************//
	@Override
	public void registerSidedEvent() {
		
		MinecraftForge.EVENT_BUS.register(new EventWorldRender());
		
	}
	
	@Override
	public void registerTileEntities() {
		
		GameRegistry.registerTileEntity(ChunkAwakerTile.class, "ChunkAwakerTile");
		ClientRegistry.bindTileEntitySpecialRenderer(ChunkAwakerTile.class, new ChunkAwakerRenderTile());
		
	}
	
}
