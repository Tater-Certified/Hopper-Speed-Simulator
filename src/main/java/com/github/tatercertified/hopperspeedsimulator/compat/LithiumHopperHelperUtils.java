package com.github.tatercertified.hopperspeedsimulator.compat;

import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;

public final class LithiumHopperHelperUtils {
    public static boolean tryMoveMultipleItems(Container to, ItemStack stack, @Nullable Direction fromDirection, int numberOfItems) {
        WorldlyContainer toSided = to instanceof WorldlyContainer ? (WorldlyContainer)to : null;
        int remaining = Math.min(numberOfItems, stack.getCount());
        if (remaining <= 0) {
            return false;
        }
        boolean movedAny = false;
        int slot;
        if (toSided != null && fromDirection != null) {
            int[] slots = toSided.getSlotsForFace(fromDirection);

            for(slot = 0; slot < slots.length; ++slot) {
                if (remaining <= 0 || stack.isEmpty()) {
                    break;
                }
                int before = stack.getCount();
                if (tryMoveMultipleItems(to, toSided, stack, slots[slot], fromDirection, remaining)) {
                    int moved = before - stack.getCount();
                    if (moved > 0) {
                        movedAny = true;
                        remaining -= moved;
                    }
                }
            }
        } else {
            int j = to.getContainerSize();

            for(slot = 0; slot < j; ++slot) {
                if (remaining <= 0 || stack.isEmpty()) {
                    break;
                }
                int before = stack.getCount();
                if (tryMoveMultipleItems(to, toSided, stack, slot, fromDirection, remaining)) {
                    int moved = before - stack.getCount();
                    if (moved > 0) {
                        movedAny = true;
                        remaining -= moved;
                    }
                }
            }
        }

        return movedAny;
    }

    private static boolean tryMoveMultipleItems(Container to, @Nullable WorldlyContainer toSided, ItemStack transferStack, int targetSlot, @Nullable Direction fromDirection, int numberOfItems) {
        ItemStack toStack = to.getItem(targetSlot);
        if (to.canPlaceItem(targetSlot, transferStack) && (toSided == null || toSided.canPlaceItemThroughFace(targetSlot, transferStack, fromDirection))) {
            if (toStack.isEmpty()) {
                int maxPerStack = Math.min(transferStack.getMaxStackSize(), to.getMaxStackSize());
                int moveCount = Math.min(numberOfItems, maxPerStack);
                moveCount = Math.min(moveCount, transferStack.getCount());
                if (moveCount <= 0) {
                    return false;
                }
                ItemStack singleItem = transferStack.split(moveCount);
                to.setItem(targetSlot, singleItem);
                return true;
            }

            int toCount;
            if (toStack.is(transferStack.getItem()) && toStack.getMaxStackSize() > (toCount = toStack.getCount()) && to.getMaxStackSize() > toCount && ItemStack.isSameItemSameComponents(toStack, transferStack)) {
                int maxPerStack = Math.min(toStack.getMaxStackSize(), to.getMaxStackSize());
                int space = maxPerStack - toCount;
                if (space <= 0) {
                    return false;
                }
                int moveCount = Math.min(numberOfItems, space);
                moveCount = Math.min(moveCount, transferStack.getCount());
                if (moveCount <= 0) {
                    return false;
                }
                transferStack.shrink(moveCount);
                toStack.grow(moveCount);
                return true;
            }
        }

        return false;
    }
}
