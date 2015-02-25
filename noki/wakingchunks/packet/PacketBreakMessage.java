package noki.wakingchunks.packet;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;


/**********
 * @class PacketBreakMessage
 *
 * @description
 * @description_en
 */
public class PacketBreakMessage implements IMessage {

	//******************************//
	// define member variables.
	//******************************//
	public ByteBuf data;

	
	//******************************//
	// define member methods.
	//******************************//
	public PacketBreakMessage() {
		
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		
		this.data = buf;
		
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		
		buf.writeInt(1);
		
	}

}
