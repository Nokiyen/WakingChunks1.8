package noki.wakingchunks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;

import net.minecraftforge.fml.common.FMLLog;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.LoadingCallback;
import net.minecraftforge.common.ForgeChunkManager.Ticket;


/**********
 * @class ChunkManager
 *
 * @description
 * @description_en
 */
public class ChunkManager implements LoadingCallback {

	//******************************//
	// define member variables.
	//******************************//
	private static HashMap<World, HashMap<String, Ticket>> bundledTicketsLists =
			new HashMap<World, HashMap<String, Ticket>>();

	
	//******************************//
	// define member methods.
	//******************************//
	//--Instance Method.---------//
	@Override
	public void ticketsLoaded(List<Ticket> tickets, World world) {
		
		log("Starts to load chunks when world loading / %s.", world.provider.getDimensionId());
				
		HashMap<String, Ticket> ticketsList = new HashMap<String, Ticket>();
		
		for(Ticket each: tickets) {
			NBTTagCompound nbt = each.getModData();
			int posX = nbt.getInteger("posX");
			int posZ = nbt.getInteger("posZ");
			int range = nbt.getInteger("range");
			String label = nbt.getString("label");
			
			log("Chunk Loaded with %s.", label);
			
			int chunkX = posX>>4;
			int chunkZ = posZ>>4;
			//	if range is -1, no chunks are loaded.
			for(int i = chunkX + range; i >= chunkX - range; --i) {
				for(int j = chunkZ + range; j >= chunkZ - range; --j) {
					ForgeChunkManager.forceChunk(each, new ChunkCoordIntPair(i, j));
					log("load %s/%s/%s.", i, j, range);
				}
			}
			
			ticketsList.put(label, each);
		}
	
		bundledTicketsLists.put(world, ticketsList);

		log("Ends of loading chunks when world loading / %s.", world.provider.getDimensionId());

	}
	
	
	//--Static Method.---------//
	/**
	 * Returns a (almost) unique label for tickets.
	 */
	public static String makeLabel() {
		
		return RandomStringUtils.randomAlphanumeric(8);
		
	}
	
	public static String requestLabel(World world) {
		
		log("Starts to release a new ticket of %s.", world.provider.getDimensionId());
		
		//	solve about a ticket.
		Ticket newTicket = ForgeChunkManager.requestTicket(WakingChunksCore.instance, world, ForgeChunkManager.Type.NORMAL);
		if(newTicket == null) {
			log("No available ticket.");
			log("Ends of releasing a new ticket of %s", world.provider.getDimensionId());
			return null;
		}
		newTicket.getModData().setInteger("range", -1);
		String newLabel = makeLabel();
		newTicket.getModData().setString("label", newLabel);

		//	add the ticket to ticketsList.
		HashMap<String, Ticket> ticketsList = bundledTicketsLists.get(world);
		if(ticketsList == null) {
			ticketsList = new HashMap<String, Ticket>();
		}		
		ticketsList.put(newLabel, newTicket);
		bundledTicketsLists.put(world, ticketsList);
		
		//	return.
		log("New ticket is %s.", newLabel);
		log("Ends of releasing a new ticket of %s", world.provider.getDimensionId());
		return newLabel;
		
	}
	
	public static boolean existLabel(World world, String label) {
		
		HashMap<String, Ticket> ticketsList = bundledTicketsLists.get(world);
		if(ticketsList == null) {
			return false;
		}
		
		Ticket targetTicket = ticketsList.get(label);
		if(targetTicket == null) {
			return false;
		}
		
		return true;
		
	}
	
	public static boolean loadChunks(World world, int posX, int posY, int posZ, int range, String label) {
		
		log("Starts to load chunks / %s.", world.provider.getDimensionId());
		
		//	check argument and ticket.
		if(range < 0) {
			log("Invalid range.");
			log("Ends of loading chunks / %s.", world.provider.getDimensionId());
			return false;
		}

		HashMap<String, Ticket> ticketsList = bundledTicketsLists.get(world);
		if(ticketsList == null) {
			log("No tickets list.");
			log("Ends of loading chunks / %s.", world.provider.getDimensionId());
			return false;
		}
		
		Ticket targetTicket = ticketsList.get(label);
		if(targetTicket == null) {
			log("No ticket.");
			log("Ends of loading chunks / %s.", world.provider.getDimensionId());
			return false;
		}

		//	force and unforce chunks.
		NBTTagCompound targetNbt = targetTicket.getModData();
		int prevChunkX = targetNbt.getInteger("posX")>>4;
		int prevChunkZ = targetNbt.getInteger("posZ")>>4;
		int prevRange = targetNbt.getInteger("range");
		
		int newChunkX = posX>>4;
		int newChunkZ = posZ>>4;
		int newRange = range;
		
		for(int i = newChunkX + newRange; i >= newChunkX - newRange; --i) {
			for(int j = newChunkZ + newRange; j >= newChunkZ - newRange; --j) {
				ForgeChunkManager.forceChunk(targetTicket, new ChunkCoordIntPair(i, j));

				log("internal load %s/%s/%s.", i, j, newRange);
			}
		}
		
		for(int i = prevChunkX + prevRange; i >= prevChunkX - prevRange; --i) {
			for(int j = prevChunkZ + prevRange; j >= prevChunkZ - prevRange; --j) {
				if(i > newChunkX+newRange || i < newChunkX-newRange || j > newChunkZ+newRange || j < newChunkZ-newRange) {
					ForgeChunkManager.unforceChunk(targetTicket, new ChunkCoordIntPair(i, j));
					
					log("internal unload %s/%s/%s.", i, j, prevRange);
				}
			}
		}

		targetNbt.setInteger("posX", posX);
		targetNbt.setInteger("posY", posY);
		targetNbt.setInteger("posZ", posZ);
		targetNbt.setInteger("range", range);
		
		log("Ends of loading chunks / %s.", world.provider.getDimensionId());
		return true;
		
	}
	
	public static void unloadChunks(World world, String label) {
		
		log("Starts to unload chunks / %s.", world.provider.getDimensionId());
		
		HashMap<String, Ticket> ticketsList = bundledTicketsLists.get(world);
		if(ticketsList == null) {
			log("No tickets list.");
			log("Ends of unloading chunks / %s.", world.provider.getDimensionId());
			return;
		}
		
		Ticket targetTicket = ticketsList.get(label);
		if(targetTicket == null) {
			log("No ticket.");
			log("Ends of unloading chunks / %s.", world.provider.getDimensionId());
			return;
		}
		
		NBTTagCompound targetNbt = targetTicket.getModData();
		int prevChunkX = targetNbt.getInteger("posX")>>4;
		int prevChunkZ = targetNbt.getInteger("posZ")>>4;
		int prevRange = targetNbt.getInteger("range");
		
		//	unforce chunks.
		for(int i = prevChunkX + prevRange; i >= prevChunkX - prevRange; --i) {
			for(int j = prevChunkZ + prevRange; j >= prevChunkZ - prevRange; --j) {
				ForgeChunkManager.unforceChunk(targetTicket, new ChunkCoordIntPair(i, j));
				log("unload %s/%s/%s.", i, j, prevRange);
			}
		}

		targetNbt.setInteger("range", -1);
		
		log("Ends of unloading chunks / %s.", world.provider.getDimensionId());
		
	}
	
	public static void releaseTicket(World world, String label) {
		
		log("Starts to release a ticket / %s.", world.provider.getDimensionId());
		
		HashMap<String, Ticket> ticketsList = bundledTicketsLists.get(world);
		if(ticketsList == null) {
			log("No tickets list.");
			log("Ends of releasing the ticket / %s.", world.provider.getDimensionId());
			return;
		}
		
		Ticket targetTicket = ticketsList.get(label);
		if(targetTicket == null) {
			log("No ticket.");
			log("Ends of releasing the ticket / %s.", world.provider.getDimensionId());
			return;
		}
		
		unloadChunks(world, label);
		ForgeChunkManager.releaseTicket(targetTicket);
		ticketsList.remove(label);
		
		log("Ends of releasing the ticket / %s.", world.provider.getDimensionId());
		
	}
	
	public static void checkIllegalChunks(World world) {
		
		ChunkManager.log("check illegal chunks.");
		
		HashMap<String, Ticket> ticketsList = bundledTicketsLists.get(world);
		if(ticketsList == null) {
			return;
		}
		
		ArrayList<String> deleteLabels = new ArrayList<String>();
		for(Map.Entry<String, Ticket> e: ticketsList.entrySet()) {
			NBTTagCompound nbt = e.getValue().getModData();
			int posX = nbt.getInteger("posX");
			int posY = nbt.getInteger("posY");
			int posZ = nbt.getInteger("posZ");
			Block block = world.getBlockState(new BlockPos(posX, posY, posZ)).getBlock();
			
			if(block != WakingChunksData.chunkAwaker
				&& WakingChunksData.ccLoaded
				&& block != WakingChunksData.cc_turtle
				&& block != WakingChunksData.cc_turtleExpanded
				&& block != WakingChunksData.cc_turtleAdvanced) {
				log("delete label / %s", e.getKey());
				log("pos are %s/%s/%s", posX, posY, posZ);
				log("ccLoaded is %s.", String.valueOf(WakingChunksData.ccLoaded));
				if(WakingChunksData.cc_turtle == null) {
					log("turtle is null.");
				}
				else {
					log("turtle is not null.");
				}
				deleteLabels.add(e.getKey());
			}
		}
		
		for(String each: deleteLabels) {
			releaseTicket(world, each);
		}
		
	}
	
	public static void log(String message, Object... data) {
		
		if(WakingChunksData.debug) {
			FMLLog.fine("[WakingChunks:LOG] "+message, data);
		}
		
	}
	
}
