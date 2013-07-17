package mods.ft975.util;

import net.minecraftforge.common.ForgeDirection;

public enum MachineSide {
    FRONT, BACK, LEFT, RIGHT, TOP, BOTTOM;

    public ForgeDirection toDirection(ForgeDirection facing) {
        switch (this) {
            case FRONT:
                return facing;
            case BACK:
                return facing.getOpposite();
            case LEFT: // L and right is thanks to fry from #MinecraftForge
                return ForgeDirection.getOrientation(ForgeDirection.ROTATION_MATRIX[0][facing.ordinal()]);
            case RIGHT:
                return ForgeDirection.getOrientation(ForgeDirection.ROTATION_MATRIX[1][facing.ordinal()]);
            case TOP:
                return ForgeDirection.UP;
            case BOTTOM:
                return ForgeDirection.DOWN;
            default:
                throw new IllegalStateException("Java WTF probably");
        }
    }

    public static MachineSide getSide(ForgeDirection side, ForgeDirection front) {
        switch (side) {
            case DOWN:
                return BOTTOM;
            case UP:
                return TOP;
        }

        if (front == side)
            return FRONT;
        else if (front.getOpposite() == side)
            return BACK;
        else if (front.getRotation(ForgeDirection.UP) == side)
            return RIGHT;
        else
            return LEFT;
    }
}