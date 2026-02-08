package com.github.tatercertified.hopperspeedsimulator.compat;

import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

public final class LithiumHopperHelperUtils {
    public static boolean tryMoveMultipleItems(Inventory to, ItemStack stack, @Nullable Direction fromDirection, int numberOfItems) {
        SidedInventory toSided = to instanceof SidedInventory ? (SidedInventory)to : null;
        int remaining = Math.min(numberOfItems, stack.getCount());
        if (remaining <= 0) {
            return false;
        }
        boolean movedAny = false;
        int slot;
        if (toSided != null && fromDirection != null) {
            int[] slots = toSided.getAvailableSlots(fromDirection);

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
            int j = to.size();

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

    private static boolean tryMoveMultipleItems(Inventory to, @Nullable SidedInventory toSided, ItemStack transferStack, int targetSlot, @Nullable Direction fromDirection, int numberOfItems) {
        ItemStack toStack = to.getStack(targetSlot);
        if (to.isValid(targetSlot, transferStack) && (toSided == null || toSided.canInsert(targetSlot, transferStack, fromDirection))) {
            if (toStack.isEmpty()) {
                int maxPerStack = Math.min(transferStack.getMaxCount(), to.getMaxCountPerStack());
                int moveCount = Math.min(numberOfItems, maxPerStack);
                moveCount = Math.min(moveCount, transferStack.getCount());
                if (moveCount <= 0) {
                    return false;
                }
                ItemStack singleItem = transferStack.split(moveCount);
                to.setStack(targetSlot, singleItem);
                return true;
            }

            int toCount;
            if (toStack.isOf(transferStack.getItem()) && toStack.getMaxCount() > (toCount = toStack.getCount()) && to.getMaxCountPerStack() > toCount && ItemStack.areItemsAndComponentsEqual(toStack, transferStack)) {
                int maxPerStack = Math.min(toStack.getMaxCount(), to.getMaxCountPerStack());
                int space = maxPerStack - toCount;
                if (space <= 0) {
                    return false;
                }
                int moveCount = Math.min(numberOfItems, space);
                moveCount = Math.min(moveCount, transferStack.getCount());
                if (moveCount <= 0) {
                    return false;
                }
                transferStack.decrement(moveCount);
                toStack.increment(moveCount);
                return true;
            }
        }

        return false;
    }
}
