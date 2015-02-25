package noki.wakingchunks.packet;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;


/**********
 * @class PacketRequestMessage
 *
 * @description
 * @description_en
 */
public class PacketRequestMessage implements IMessage {

	//******************************//
	// define member variables.
	//******************************//
	private ByteBuf data;
	private int worldID;
	private int chunkX;
	private int chunkZ;
	private int range;

	
	//******************************//
	// define member methods.
	//******************************//
	public PacketRequestMessage() {
		
	}
	
	public PacketRequestMessage(int worldID, int chunkX, int chunkZ, int range) {
		
		this.worldID = worldID;
		this.chunkX = chunkX;
		this.chunkZ = chunkZ;
		this.range = range;
		
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		
		this.data = buf;
		
	}

	@Override
	public void toBytes(ByteBuf buf) {
		
		buf.writeInt(this.worldID);
		buf.writeInt(this.chunkX);
		buf.writeInt(this.chunkZ);
		buf.writeInt(this.range);
		
	}
	
	public ByteBuf getData() {
		
		return this.data;
		
	}

}
