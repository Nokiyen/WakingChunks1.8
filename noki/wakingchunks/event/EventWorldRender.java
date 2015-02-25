package noki.wakingchunks.event;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import noki.wakingchunks.packet.PacketRequestMessageHandler.ChunkData;

import org.lwjgl.opengl.GL11;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


/**********
 * @class EventWorldRender
 *
 * @description
 * @description_en
 */
public class EventWorldRender {
	
	//******************************//
	// define member variables.
	//******************************//
	public static ArrayList<ChunkData> loadedChunks = null;

	
	//******************************//
	// define member methods.
	//******************************//
	@SubscribeEvent
	public void renderWorldLastEvent(RenderWorldLastEvent event) {
		
		if(loadedChunks == null) {
			return;
		}

		Minecraft mc = Minecraft.getMinecraft();
		EntityLivingBase entity = mc.thePlayer;
//		PacketHandler.instance.sendToServer(new PacketSampleMessage());
				
		GL11.glDepthMask(false);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);	// alpha blend.
		GL11.glEnable(GL11.GL_BLEND);
		
		GL11.glPushMatrix();
		GL11.glTranslated(
				-(entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * event.partialTicks),
				-(entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * event.partialTicks),
				-(entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * event.partialTicks));
		
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer renderer = tessellator.getWorldRenderer();
		
		
		double y = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * event.partialTicks) + 1;
		
		renderer.startDrawingQuads();
		for(int i=0; i<loadedChunks.size(); i++) {
			ChunkData chunk = loadedChunks.get(i);
			if(chunk.awaker && chunk.other) {
				GlStateManager.color(0F, 1.0F, 0F, 120F/255F);
//				t.setColorRGBA(0, 255, 0, 120);
			}
			else if(chunk.awaker && !chunk.other) {
				GlStateManager.color(0F, 0F, 1.0F, 120F/255F);
//				t.setColorRGBA(0, 0, 255, 120);
			}
			else {
				GlStateManager.color(1.0F, 0F, 0F, 120F/255F);
//				t.setColorRGBA(255, 0, 0, 120);
			}

			double x = chunk.chunkX*16;
			double z = chunk.chunkZ*16;
			renderer.addVertex(x, y+20, z);
			renderer.addVertex(x+16, y+20, z);
			renderer.addVertex(x+16, y+20, z+16);
			renderer.addVertex(x, y+20, z+16);
		}
		tessellator.draw();
		
		renderer.startDrawing(GL11.GL_LINES);
		GL11.glLineWidth(3);
		for(int i=0; i<loadedChunks.size(); i++) {
			ChunkData chunk = loadedChunks.get(i);
			if(chunk.awaker && chunk.other) {
				GlStateManager.color(0F, 1.00F, 0F, 40F/255F);
//				t.setColorRGBA(0, 255, 0, 40);
			}
			else if(chunk.awaker && !chunk.other) {
				GlStateManager.color(0F, 0F, 1.00F, 40F/255F);
//				t.setColorRGBA(0, 0, 255, 40);				
			}
			else {
				GlStateManager.color(1.0F, 0F, 0F, 40F/255F);
//				t.setColorRGBA(255, 0, 0, 40);
			}

			double x = chunk.chunkX*16;
			double z = chunk.chunkZ*16;
			renderer.addVertex(x, y+20, z);
			renderer.addVertex(x+16, y+20, z);
			renderer.addVertex(x+16, y+20, z);
			renderer.addVertex(x+16, y+20, z+16);
			renderer.addVertex(x+16, y+20, z+16);
			renderer.addVertex(x, y+20, z+16);
			renderer.addVertex(x, y+20, z+16);
			renderer.addVertex(x, y+20, z);
		}
		tessellator.draw();
		
		GL11.glEnable(GL11.GL_DEPTH_TEST);	//	necessary to correctly render perspectives.
		renderer.startDrawing(GL11.GL_LINES);
		GlStateManager.color(0F, 1.0F, 0F, 80F/255F);
//		t.setColorRGBA(0, 255, 0, 80);
		GL11.glLineWidth(3);
		int range = mc.gameSettings.renderDistanceChunks;
		for(int i = entity.chunkCoordX-range; i <= entity.chunkCoordX+range; i++) {
			for(int j = entity.chunkCoordZ-range; j <= entity.chunkCoordZ+range; j++) {
				renderer.addVertex(i*16, 0, j*16);
				renderer.addVertex(i*16, y+30, j*16);
			}
		}
		tessellator.draw();
		
		renderer.startDrawingQuads();
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GlStateManager.color(0F, 0F, 1.00F, 40F/255F);
//		t.setColorRGBA(0, 0, 255, 40);
		int currentX = entity.chunkCoordX*16;
		int currentZ = entity.chunkCoordZ*16;
			
		renderer.addVertex(currentX, 0, currentZ);
		renderer.addVertex(currentX, y, currentZ);
		renderer.addVertex(currentX+16, y, currentZ);
		renderer.addVertex(currentX+16, 0, currentZ);
		
		renderer.addVertex(currentX+16, 0, currentZ);
		renderer.addVertex(currentX+16, y, currentZ);
		renderer.addVertex(currentX+16, y, currentZ+16);
		renderer.addVertex(currentX+16, 0, currentZ+16);
		
		renderer.addVertex(currentX+16, 0, currentZ+16);
		renderer.addVertex(currentX+16, y, currentZ+16);
		renderer.addVertex(currentX, y, currentZ+16);
		renderer.addVertex(currentX, 0, currentZ+16);
		
		renderer.addVertex(currentX, 0, currentZ+16);
		renderer.addVertex(currentX, y, currentZ+16);
		renderer.addVertex(currentX, y, currentZ);
		renderer.addVertex(currentX, 0, currentZ);
		
		tessellator.draw();
		
		GL11.glPopMatrix();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glDepthMask(true);
		
	}

}
