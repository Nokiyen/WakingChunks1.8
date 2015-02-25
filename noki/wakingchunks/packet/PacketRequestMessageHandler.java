package noki.wakingchunks.packet;

import java.util.ArrayList;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import noki.wakingchunks.ChunkManager;
import noki.wakingchunks.WakingChunksData;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;


/**********
 * @class PacketRequestMessageHandler
 *
 * @description
 * @description_en
 */
public class PacketRequestMessageHandler implements IMessageHandler<PacketRequestMessage, PacketResponseMessage> {
	
	//******************************//
	// define member variables.
	//******************************//

	
	//******************************//
	// define member methods.
	//******************************//
	@Override
	public PacketResponseMessage onMessage(PacketRequestMessage message, MessageContext ctx) {
		
		ByteBuf data = message.getData();
		int worldID = data.readInt();
		int chunkX = data.readInt();
		int chunkZ = data.readInt();
		int range = data.readInt();
		
		World world = MinecraftServer.getServer().worldServerForDimension(worldID);
		ImmutableSetMultimap<ChunkCoordIntPair, Ticket> chunks = ForgeChunkManager.getPersistentChunksFor(world);
		ArrayList<ChunkData> loaded = new ArrayList<ChunkData>();
		
		for(int i = chunkX-range; i <= chunkX+range; i++) {
			for(int j = chunkZ-range; j <= chunkZ+range; j++) {
				ImmutableSet<Ticket> targetTickets = chunks.get(new ChunkCoordIntPair(i, j));
				if(targetTickets.size() == 0) {
					continue;
				}
				
				ChunkManager.log("Starts to check chunk's tickets.");
				boolean awaker = false;
				boolean other = false;
				for(Ticket ticket: targetTickets) {
					ChunkManager.log("The mod id is %s/%s/%s.", ticket.getModId(), i, j);
					if(ticket.getModId().equals(WakingChunksData.ID)) {
						awaker = true;
					}
					else {
						other = true;
					}
				}
				loaded.add(new ChunkData(i, j, awaker, other));
				ChunkManager.log("Ends of checking chunk's tickets.");
			}
		}

		return new PacketResponseMessage(loaded);
		
	}
	
	public static class ChunkData {
		
		public boolean awaker;
		public boolean other;
		public int chunkX;
		public int chunkZ;
		
		public ChunkData(int chunkX, int chunkZ, boolean awaker, boolean other) {
			
			this.chunkX = chunkX;
			this.chunkZ = chunkZ;
			this.awaker = awaker;
			this.other = other;
			
		}
		
	}

}
