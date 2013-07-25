package mods.ft975.cloud

import cpw.mods.fml.relauncher.{Side, SideOnly}

object ModInfo {
	final val modID      = "ftclouds"
	final val modName    = "Cloud Mod"
	final val configName = "FT_Clouds"

	final val versionBuild = "1"
	final val versionMinor = "0"
	final val versionMajor = "1"
	final val version      = versionMajor + "." + versionMinor + "_" + versionBuild

	final val versionMinecraft      = "1.6.2"
	@SideOnly(Side.CLIENT)
	final val resourceBlockTextures = "/textures/blocks/"
}