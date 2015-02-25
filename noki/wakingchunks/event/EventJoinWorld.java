package noki.wakingchunks.event;

import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import noki.wakingchunks.ChunkManager;
import noki.wakingchunks.WakingChunksCore;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;


/**********
 * @class EventJoinWorld
 *
 * @description
 * @description_en
 */
public class EventJoinWorld {
	
	//******************************//
	// define member variables.
	//******************************//
	
	
	//******************************//
	// define member methods.
	//******************************//
	@SubscribeEvent
	public void onJoinWorld(EntityJoinWorldEvent event) {
		
		// only in case of EntityPlayer
		if(!(event.entity instanceof EntityPlayer)) {
			return;
		}
		
		if(event.world.isRemote) {
			// notify version update.
			UUID targetID = ((EntityPlayer)event.entity).getGameProfile().getId();
			UUID playerID = Minecraft.getMinecraft().thePlayer.getGameProfile().getId();
			if(targetID.equals(playerID)) {
				WakingChunksCore.versionInfo.notifyUpdate(Side.CLIENT);			
			}

			// clear chunk-border rendering.
			EventWorldRender.loadedChunks = null;
		}
		else {
			// check illegally loaded chunks.
			ChunkManager.checkIllegalChunks(event.world);
		}
		
	}

}
