package noki.wakingchunks.packet;

import noki.wakingchunks.WakingChunksData;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {
	
	public static final SimpleNetworkWrapper instance = NetworkRegistry.INSTANCE.newSimpleChannel(WakingChunksData.ID);
	
	public static void registerPre() {
		
		instance.registerMessage(PacketRequestMessageHandler.class, PacketRequestMessage.class, 1, Side.SERVER);
		instance.registerMessage(PacketResponseMessageHandler.class, PacketResponseMessage.class, 2, Side.CLIENT);
		instance.registerMessage(PacketBreakMessageHandler.class, PacketBreakMessage.class, 3, Side.CLIENT);
		
	}

}
