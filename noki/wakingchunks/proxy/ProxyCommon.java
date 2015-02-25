package noki.wakingchunks.proxy;

import net.minecraftforge.fml.common.registry.GameRegistry;
import noki.wakingchunks.chunkawaker.ChunkAwakerTile;


/**********
 * @class ProxyCommon
 *
 * @description 共通proxyクラス。
 * @description_en Interface of proxy classes.
 */
public class ProxyCommon {
	
	//******************************//
	// define member variables.
	//******************************//

	
	//******************************//
	// define member methods.
	//******************************//
	public void registerSidedEvent() {
		
	}
	
	public void registerTileEntities(){
		
		GameRegistry.registerTileEntity(ChunkAwakerTile.class, "ChunkAwakerTile");
		
	}
	
}
