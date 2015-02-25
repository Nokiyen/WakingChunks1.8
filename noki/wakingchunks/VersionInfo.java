package noki.wakingchunks;

import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.Collection;
import java.util.List;


/**********
 * @class VersionInfo
 *
 * @description 
 * @description_en
 */
public class VersionInfo {
	
	//******************************//
	// define member variables.
	//******************************//
	private String modName;
	private String modVersion;
	
	private String updateUrl;
	private boolean complete = false;
	private boolean needNotify = false;

	private String uptodateVersion;
	private boolean notifiedClient = false;
	private boolean notifiedServer = false;
	
	
	//******************************//
	// define member methods.
	//******************************//
	public VersionInfo(String modName, String modVersion, String updateUrl) {
		
		this.modName = modName;
		this.modVersion = modVersion;
		this.updateUrl = updateUrl;
		
		Thread thread = new ThreadVersionInfo();
		thread.start();
		
	}

	private class ThreadVersionInfo extends Thread {
		
		@Override
		public void run() {

			//	get json from the url.
			String json;
			try {
				URL url = new URL(VersionInfo.this.updateUrl);
				InputStream input = url.openStream();
				json = new String(ByteStreams.toByteArray(input));
				input.close();
			}
			catch(IOException e) {
				FMLLog.fine("[Noki:VersionInfo] can't get json.");
				return;
			}
			FMLLog.fine("[Noki:VersionInfo] %s", json);

			//	convert json to objects.
			Type collectionType = new TypeToken<Collection<EachInfo>>(){}.getType();
			List<EachInfo> receivedData;
			try {
				receivedData = new Gson().fromJson(json, collectionType);
			}
			catch(JsonSyntaxException e) {
				FMLLog.fine("[Noki:VersionInfo] invalid json.");
				return;
			}
			
			//	search matching version.
			for(EachInfo each: receivedData) {
				FMLLog.fine("[Noki:VersionInfo] each.");
				if(each.getMcversion().equals(Loader.MC_VERSION)) {
					FMLLog.fine("[Noki:VersionInfo] find target.");
					VersionInfo.this.uptodateVersion = each.getVersion();
					VersionInfo.this.complete = true;
					
					if(!VersionInfo.this.uptodateVersion.equals(VersionInfo.this.modVersion)) {
						VersionInfo.this.needNotify = true;
						FMLLog.fine("[Noki:VersionInfo] need notify / %s.", each.getVersion());
					}
					break;
				}
			}
			
		}
		
	}

	private class EachInfo {
		
		private String mcversion;
		private String version;
		
		@SuppressWarnings("unused")
		public void setMcversion(String _mcversion) {
			this.mcversion = _mcversion;			
		}
		
		@SuppressWarnings("unused")
		public void setVersion(String _version) {
			this.version = _version;
		}
		
		public String getMcversion() {
			return this.mcversion;
		}
		
		public String getVersion() {
			return this.version;
		}
		
	}
	
	public void notifyUpdate(Side side) {
		
		if(this.complete == true && this.needNotify == true) {
			if(side == Side.CLIENT && this.notifiedClient == false) {
				Minecraft.getMinecraft().thePlayer.addChatMessage(
						new ChatComponentTranslation(this.modName+".version.notify", this.uptodateVersion));
				this.notifiedClient = true;
			}
			else if(side == Side.SERVER && this.notifiedServer == false) {
				MinecraftServer.getServer().addChatMessage(
						new ChatComponentTranslation(this.modName+".version.notify", this.uptodateVersion));
				this.notifiedServer = true;
			}
		}
		
	}
	
}
