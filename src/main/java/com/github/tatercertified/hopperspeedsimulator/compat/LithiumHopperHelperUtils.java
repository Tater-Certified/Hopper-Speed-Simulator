package com.github.tatercertified.hopperspeedsimulator.compat;

import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class LithiumHopperHelperUtils {
    public static boolean tryMoveMultipleItems(Inventory to, ItemStack stack, @Nullable Direction fromDirection, int numberOfItems) {
        SidedInventory toSided = to instanceof SidedInventory ? (SidedInventory)to : null;
        int slot;
        if (toSided != null && fromDirection != null) {
            int[] slots = toSided.getAvailableSlots(fromDirection);

            for(slot = 0; slot < slots.length; ++slot) {
                if (tryMoveMultipleItems(to, toSided, stack, slots[slot], fromDirection, numberOfItems)) {
                    return true;
                }
            }
        } else {
            int j = to.size();

            for(slot = 0; slot < j; ++slot) {
                if (tryMoveMultipleItems(to, toSided, stack, slot, fromDirection, numberOfItems)) {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean tryMoveMultipleItems(Inventory to, @Nullable SidedInventory toSided, ItemStack transferStack, int targetSlot, @Nullable Direction fromDirection, int numberOfItems) {
        ItemStack toStack = to.getStack(targetSlot);
        if (to.isValid(targetSlot, transferStack) && (toSided == null || toSided.canInsert(targetSlot, transferStack, fromDirection))) {
            if (toStack.isEmpty()) {
                ItemStack singleItem = transferStack.split(numberOfItems);
                to.setStack(targetSlot, singleItem);
                return true;
            }

            int toCount;
            if (toStack.isOf(transferStack.getItem()) && toStack.getMaxCount() > (toCount = toStack.getCount()) && to.getMaxCountPerStack() > toCount && areNbtEqual(toStack, transferStack)) {
                transferStack.decrement(numberOfItems);
                toStack.increment(numberOfItems);
                return true;
            }
        }

        return false;
    }

    /**
     * From Lithium
     */
    private static boolean areNbtEqual(ItemStack stack1, ItemStack stack2) {
        return Objects.equals(stack1.getNbt(), stack2.getNbt());
    }
}
