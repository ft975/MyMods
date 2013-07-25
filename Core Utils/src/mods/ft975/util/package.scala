package mods.ft975

package object util extends PackageObjectBase


import java.util.logging.Level._
import java.util.logging.Logger
import mods.ft975.util.extensibles.TileExt
import net.minecraft.tileentity.TileEntity
import scala.util.Random

trait PackageObjectBase {
	val rand        = new Random()
	var log: Logger = null
	private val isDebug = false

	def DebugOnly(op: => Unit) {
		if (isDebug) op
	}

	def logInfo(a: Any) {
		log.log(INFO, tS(a))
	}

	private def tS(a: Any): String = {
		if (a != null) a.toString else "() NULL"
	}

	def notNull(a: Any): Boolean = a != null

	def asTileExt[R](te: TileEntity, op: (TileExt) => R, els: R): R = {
		if (notNull(te) && te.isInstanceOf[TileExt]) {
			op(te.asInstanceOf[TileExt])
		} else {
			els
		}
	}
}