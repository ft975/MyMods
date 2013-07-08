package mods.ft975.lighting

import net.minecraft.util.Icon
import cpw.mods.fml.client.FMLClientHandler
import mods.ft975.lighting.Shapes._
import net.minecraft.client.renderer.texture.IconRegister

object Resource {
	var textureMap = Map[Shapes, Map[Colors, String]]()
	var particleMap = Map[Shapes, Map[Colors, Icon]]()

	def registerIcons(ir: IconRegister) {
		for (s <- Shapes.vals;
		     c <- Colors.vals) {
			textureMap += s -> Map(c -> FMLClientHandler.instance.getClient.renderEngine.allocateAndSetupTexture(MaskedColor.maskImage(s match {
				case Caged => "caged.png"
				case Block => "block.png"
				case Panel => "panel.png"
				case Bulb => "bulb.png"
			}, c.color.R, c.color.G, c.color.B)))
		}

		for (s <- Shapes.vals;
		     c <- Colors.vals) {
			particleMap += s -> Map(c -> FMLClientHandler.instance.getClient.renderEngine.allocateAndSetupTexture(MaskedColor.maskImage(s match {
				case Caged => "caged.png"
				case Block => "block.png"
				case Panel => "panel.png"
				case Bulb => "bulb.png"
			}, c.color.R, c.color.G, c.color.B)))
		}
	}
}
