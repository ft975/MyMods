package mods.ft975.energy.impl

import mods.ft975.energy.api.{PowerMember, PowerExporter, AbstractEnergyNetwork}
import mods.ft975.util.WorldUtil
import net.minecraft.world.World
import net.minecraftforge.common.ForgeDirection
import scala.collection.mutable
import scala.inline
import scala.util.control.Breaks._

class EnergyNet(world: World) extends AbstractEnergyNetwork(world) {
	val networkMap = mutable.Map[PowerMember, Set[PowerMember]]()

	def refreshGraph(pm: PowerMember) {
		/*
		Reset all the elements in the network
		 */
		networkMap.foreach((f) => {f._1.clearEnergyNet()})
		networkMap.clear()


		def iterateDef(x: Int, y: Int, z: Int, currLevel: Int) {
			if (currLevel < WorldUtil.MaxRecursiveDepth) {
				for (deltaCoord <- WorldUtil.ImmediateSurroundings) {
					if (cond(x + deltaCoord.x, y + deltaCoord.y, z + deltaCoord.z, x, y, z)) {
						op(x + deltaCoord.x, y + deltaCoord.y, z + deltaCoord.z, x, y, z)
						iterateDef(x + deltaCoord.x, y + deltaCoord.y, z + deltaCoord.z, currLevel + 1)
					}
				}
			}
		}

		@inline def cond(x: Int, y: Int, z: Int, px: Int, py: Int, pz: Int): Boolean = {
			if (x == px && y == py && z == pz) {false}
			else {
				val te = world.getBlockTileEntity(x, y, z) match {case te: PowerMember => te; case _ => null}
				te != null && te.isSideConnected(WorldUtil.sideFromPos(px, py, pz, x, y, z))
			}
		}

		@inline def op(x: Int, y: Int, z: Int, px: Int, py: Int, pz: Int) {
			val te = world.getBlockTileEntity(x, y, z) match {case te: PowerMember => te; case _ => null}
			var numSides: Int = 0
			breakable {
				for (side <- ForgeDirection.VALID_DIRECTIONS) {
					if (te.isSideConnected(side) && numSides == 1) {
						adjacentSet += te
						break()
					} else if (te.isSideConnected(side)) {
						numSides += 1
					}
				}
			}
		}
	}

	def getBestExporter(x: Int, y: Int, z: Int): Set[PowerExporter] = ???

	private def getAdjacentPoints(x: Int, y: Int, z: Int): Set[PowerMember] = {
		import scala.util.control.Breaks._

		var adjacentSet = Set.empty[PowerMember]

		iterateDef(x, y, z, 0)
		@inline def iterateDef(x: Int, y: Int, z: Int, currLevel: Int) {
			if (currLevel < WorldUtil.MaxRecursiveDepth) {
				for (deltaCoord <- WorldUtil.ImmediateSurroundings) {
					if (cond(x + deltaCoord.x, y + deltaCoord.y, z + deltaCoord.z, x, y, z)) {
						op(x + deltaCoord.x, y + deltaCoord.y, z + deltaCoord.z, x, y, z)
						iterateDef(x + deltaCoord.x, y + deltaCoord.y, z + deltaCoord.z, currLevel + 1)
					}
				}
			}
		}

		@inline def cond(x: Int, y: Int, z: Int, px: Int, py: Int, pz: Int): Boolean = {
			if (x == px && y == py && z == pz) {false}
			else {
				val te = world.getBlockTileEntity(x, y, z) match {case te: PowerMember => te; case _ => null}
				te != null && te.isSideConnected(WorldUtil.sideFromPos(px, py, pz, x, y, z))
			}
		}

		@inline def op(x: Int, y: Int, z: Int, px: Int, py: Int, pz: Int) {
			val te = world.getBlockTileEntity(x, y, z) match {case te: PowerMember => te; case _ => null}
			var numSides: Int = 0
			breakable {
				for (side <- ForgeDirection.VALID_DIRECTIONS) {
					if (te.isSideConnected(side) && numSides == 1) {
						adjacentSet += te
						break()
					} else if (te.isSideConnected(side)) {
						numSides += 1
					}
				}
			}
		}

		adjacentSet
	}
}
