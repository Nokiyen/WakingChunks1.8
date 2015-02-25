package noki.wakingchunks.packet;

import noki.wakingchunks.event.EventWorldRender;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;


/**********
 * @class PacketResponseMessageHandler
 *
 * @description
 * @description_en
 */
public class PacketResponseMessageHandler implements IMessageHandler<PacketResponseMessage, IMessage> {

	//******************************//
	// define member variables.
	//******************************//
	
	
	//******************************//
	// define member methods.
	//******************************//
	@Override
	public IMessage onMessage(PacketResponseMessage message, MessageContext ctx) {
		
		EventWorldRender.loadedChunks = message.getData();

		return null;
		
	}

}
