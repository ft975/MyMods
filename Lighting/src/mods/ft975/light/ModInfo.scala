package mods.ft975.light

import cpw.mods.fml.relauncher.{Side, SideOnly}

object ModInfo {
	final val modID      = "ftlights"
	final val modName    = "Lighting Mod"
	final val configName = "FT_Lighting"

	final val versionBuild = "1"
	final val versionMinor = "0"
	final val versionMajor = "1"
	final val version      = versionMajor + "." + versionMinor + "_" + versionBuild

	final val versionMinecraft      = "1.6.2"
	@SideOnly(Side.CLIENT)
	final val resourceBlockTextures = "/textures/blocks/"
}