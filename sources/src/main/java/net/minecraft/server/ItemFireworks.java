package net.minecraft.server;

public class ItemFireworks extends Item {

    public ItemFireworks() {}

    @Override
    public EnumInteractionResult a(EntityHuman entityhuman, World world, BlockPosition blockposition, EnumHand enumhand, EnumDirection enumdirection, float f, float f1, float f2) {
        ItemStack itemstack = entityhuman.b(enumhand);
        EntityFireworks entityfireworks = new EntityFireworks(world, blockposition.getX() + f, blockposition.getY() + f1, blockposition.getZ() + f2, itemstack);

        entityfireworks.spawningEntity = entityhuman.getUniqueID(); // Paper
        world.addEntity(entityfireworks);
        if (!entityhuman.abilities.canInstantlyBuild) {
            itemstack.subtract(1);
        }

        return EnumInteractionResult.SUCCESS;
    }

    @Override
    public InteractionResultWrapper<ItemStack> a(World world, EntityHuman entityhuman, EnumHand enumhand) {
        if (entityhuman.cH()) {
            ItemStack itemstack = entityhuman.b(enumhand);

            EntityFireworks entityfireworks = new EntityFireworks(world, itemstack, entityhuman);

            entityfireworks.spawningEntity = entityhuman.getUniqueID(); // Paper
            world.addEntity(entityfireworks);
            if (!entityhuman.abilities.canInstantlyBuild) {
                itemstack.subtract(1);
            }

            return new InteractionResultWrapper(EnumInteractionResult.SUCCESS, entityhuman.b(enumhand));
        } else {
            return new InteractionResultWrapper(EnumInteractionResult.PASS, entityhuman.b(enumhand));
        }
    }
}
