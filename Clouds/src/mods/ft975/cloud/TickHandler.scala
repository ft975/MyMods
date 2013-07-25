package mods.ft975.cloud

import aurelienribon.tweenengine.TweenManager
import cpw.mods.fml.common.{TickType, ITickHandler}
import java.util
import net.minecraft.client.Minecraft

object TickHandler extends ITickHandler {
	val mc = Minecraft.getMinecraft

	val tweenManager = new TweenManager

	def tickStart(typ: util.EnumSet[TickType], tickData: Object*) {
		if (typ.contains(TickType.CLIENT) && mc.isSingleplayer && mc.currentScreen != null && mc.currentScreen.doesGuiPauseGame && !mc.getIntegratedServer.getPublic) {
			CloudState.time += 1
			tweenManager.update(1)

			//Every CloudUpdateFrequency seconds, update the clouds
			if (CloudState.time % CloudState.CloudUpdateFrequency == 0) {
				val player = mc.thePlayer
				val world = mc.theWorld

				val px = (player.posX.toInt / 12) * 12
				val pz = (player.posZ.toInt / 12) * 12

				for (x <- -264 to 264 by 12;
						 z <- -264 to 264 by 12) {
					if (mc.theWorld.blockExists(px + x, 0, pz + z)) {
						mc.theWorld.getBiomeGenForCoords(px + x, pz + z).getFloatRainfall
					}
				}
			}
		}
	}

	def tickEnd(typ: util.EnumSet[TickType], tickData: Object*) {}
	val ticks = util.EnumSet.of(TickType.CLIENT)
	def getLabel = "ftclouds"
}
