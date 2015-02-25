package noki.wakingchunks.packet;

import noki.wakingchunks.event.EventWorldRender;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;


/**********
 * @class PacketBreakMessageHandler
 *
 * @description
 * @description_en
 */
public class PacketBreakMessageHandler implements IMessageHandler<PacketBreakMessage, IMessage> {

	//******************************//
	// define member variables.
	//******************************//
	
	
	//******************************//
	// define member methods.
	//******************************//
	@Override
	public IMessage onMessage(PacketBreakMessage message, MessageContext ctx) {
		
		EventWorldRender.loadedChunks = null;

		return null;
		
	}

}
