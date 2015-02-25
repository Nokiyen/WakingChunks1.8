package noki.wakingchunks.packet;

import java.util.ArrayList;

import noki.wakingchunks.packet.PacketRequestMessageHandler.ChunkData;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;


/**********
 * @class PacketResponseMessage
 *
 * @description
 * @description_en
 */
public class PacketResponseMessage implements IMessage {

	//******************************//
	// define member variables.
	//******************************//
	private ArrayList<ChunkData> loadedChunks;
	private ByteBuf data;

	
	//******************************//
	// define member methods.
	//******************************//
	public PacketResponseMessage() {
		
	}
	
	public PacketResponseMessage(ArrayList<ChunkData> chunks) {
		
		this.loadedChunks = chunks;
		
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		
		this.data = buf;
		
		
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		
		buf.writeInt(this.loadedChunks.size());
		for(ChunkData chunk: this.loadedChunks) {
			buf.writeInt(chunk.chunkX);
			buf.writeInt(chunk.chunkZ);
			buf.writeBoolean(chunk.awaker);
			buf.writeBoolean(chunk.other);
		}
		
	}
	
	public ArrayList<ChunkData> getData() {
		
		int times = this.data.readInt();
		ArrayList<ChunkData> read = new ArrayList<ChunkData>();
		for(int i=1; i<=times; i++) {
			int chunkX = this.data.readInt();
			int chunkZ = this.data.readInt();
			boolean awaker = this.data.readBoolean();
			boolean other = this.data.readBoolean();
			read.add(new ChunkData(chunkX, chunkZ, awaker, other));
		}
		return read;
		
	}

}
