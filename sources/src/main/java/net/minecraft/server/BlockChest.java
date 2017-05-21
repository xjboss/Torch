package net.minecraft.server;

import java.util.Iterator;
import javax.annotation.Nullable;

public class BlockChest extends BlockTileEntity {

    public static final BlockStateDirection FACING = BlockFacingHorizontal.FACING;
    protected static final AxisAlignedBB b = new AxisAlignedBB(0.0625D, 0.0D, 0.0D, 0.9375D, 0.875D, 0.9375D);
    protected static final AxisAlignedBB c = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.875D, 1.0D);
    protected static final AxisAlignedBB d = new AxisAlignedBB(0.0D, 0.0D, 0.0625D, 0.9375D, 0.875D, 0.9375D);
    protected static final AxisAlignedBB e = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 1.0D, 0.875D, 0.9375D);
    protected static final AxisAlignedBB f = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.875D, 0.9375D);
    public final BlockChest.Type g;

    protected BlockChest(BlockChest.Type blockchest_type) {
        super(Material.WOOD);
        this.y(this.blockStateList.getBlockData().set(BlockChest.FACING, EnumDirection.NORTH));
        this.g = blockchest_type;
        this.a(blockchest_type == BlockChest.Type.TRAP ? CreativeModeTab.d : CreativeModeTab.c);
    }

    @Override
    public boolean b(IBlockData iblockdata) {
        return false;
    }

    @Override
    public boolean c(IBlockData iblockdata) {
        return false;
    }

    @Override
    public EnumRenderType a(IBlockData iblockdata) {
        return EnumRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public AxisAlignedBB b(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition) {
        return iblockaccess.getType(blockposition.north()).getBlock() == this ? BlockChest.b : (iblockaccess.getType(blockposition.south()).getBlock() == this ? BlockChest.c : (iblockaccess.getType(blockposition.west()).getBlock() == this ? BlockChest.d : (iblockaccess.getType(blockposition.east()).getBlock() == this ? BlockChest.e : BlockChest.f)));
    }

    @Override
    public void onPlace(World world, BlockPosition blockposition, IBlockData iblockdata) {
        this.e(world, blockposition, iblockdata);
        Iterator iterator = EnumDirection.EnumDirectionLimit.HORIZONTAL.iterator();

        while (iterator.hasNext()) {
            EnumDirection enumdirection = (EnumDirection) iterator.next();
            BlockPosition blockposition1 = blockposition.shift(enumdirection);
            IBlockData iblockdata1 = world.getType(blockposition1);

            if (iblockdata1.getBlock() == this) {
                this.e(world, blockposition1, iblockdata1);
            }
        }

    }

    @Override
    public IBlockData getPlacedState(World world, BlockPosition blockposition, EnumDirection enumdirection, float f, float f1, float f2, int i, EntityLiving entityliving) {
        return this.getBlockData().set(BlockChest.FACING, entityliving.getDirection());
    }

    @Override
    public void postPlace(World world, BlockPosition blockposition, IBlockData iblockdata, EntityLiving entityliving, ItemStack itemstack) {
        EnumDirection enumdirection = EnumDirection.fromType2(MathHelper.floor(entityliving.yaw * 4.0F / 360.0F + 0.5D) & 3).opposite();

        iblockdata = iblockdata.set(BlockChest.FACING, enumdirection);
        BlockPosition blockposition1 = blockposition.north();
        BlockPosition blockposition2 = blockposition.south();
        BlockPosition blockposition3 = blockposition.west();
        BlockPosition blockposition4 = blockposition.east();
        boolean flag = this == world.getType(blockposition1).getBlock();
        boolean flag1 = this == world.getType(blockposition2).getBlock();
        boolean flag2 = this == world.getType(blockposition3).getBlock();
        boolean flag3 = this == world.getType(blockposition4).getBlock();

        if (!flag && !flag1 && !flag2 && !flag3) {
            world.setTypeAndData(blockposition, iblockdata, 3);
        } else if (enumdirection.k() == EnumDirection.EnumAxis.X && (flag || flag1)) {
            if (flag) {
                world.setTypeAndData(blockposition1, iblockdata, 3);
            } else {
                world.setTypeAndData(blockposition2, iblockdata, 3);
            }

            world.setTypeAndData(blockposition, iblockdata, 3);
        } else if (enumdirection.k() == EnumDirection.EnumAxis.Z && (flag2 || flag3)) {
            if (flag2) {
                world.setTypeAndData(blockposition3, iblockdata, 3);
            } else {
                world.setTypeAndData(blockposition4, iblockdata, 3);
            }

            world.setTypeAndData(blockposition, iblockdata, 3);
        }

        if (itemstack.hasName()) {
            TileEntity tileentity = world.getTileEntity(blockposition);

            if (tileentity instanceof TileEntityChest) {
                ((TileEntityChest) tileentity).a(itemstack.getName());
            }
        }

    }

    public IBlockData e(World world, BlockPosition blockposition, IBlockData iblockdata) {
        IBlockData iblockdata1 = world.getType(blockposition.north());
        IBlockData iblockdata2 = world.getType(blockposition.south());
        IBlockData iblockdata3 = world.getType(blockposition.west());
        IBlockData iblockdata4 = world.getType(blockposition.east());
        EnumDirection enumdirection = iblockdata.get(BlockChest.FACING);

        if (iblockdata1.getBlock() != this && iblockdata2.getBlock() != this) {
            boolean flag = iblockdata1.b();
            boolean flag1 = iblockdata2.b();

            if (iblockdata3.getBlock() == this || iblockdata4.getBlock() == this) {
                BlockPosition blockposition1 = iblockdata3.getBlock() == this ? blockposition.west() : blockposition.east();
                IBlockData iblockdata5 = world.getType(blockposition1.north());
                IBlockData iblockdata6 = world.getType(blockposition1.south());

                enumdirection = EnumDirection.SOUTH;
                EnumDirection enumdirection1;

                if (iblockdata3.getBlock() == this) {
                    enumdirection1 = iblockdata3.get(BlockChest.FACING);
                } else {
                    enumdirection1 = iblockdata4.get(BlockChest.FACING);
                }

                if (enumdirection1 == EnumDirection.NORTH) {
                    enumdirection = EnumDirection.NORTH;
                }

                if ((flag || iblockdata5.b()) && !flag1 && !iblockdata6.b()) {
                    enumdirection = EnumDirection.SOUTH;
                }

                if ((flag1 || iblockdata6.b()) && !flag && !iblockdata5.b()) {
                    enumdirection = EnumDirection.NORTH;
                }
            }
        } else {
            BlockPosition blockposition2 = iblockdata1.getBlock() == this ? blockposition.north() : blockposition.south();
            IBlockData iblockdata7 = world.getType(blockposition2.west());
            IBlockData iblockdata8 = world.getType(blockposition2.east());

            enumdirection = EnumDirection.EAST;
            EnumDirection enumdirection2;

            if (iblockdata1.getBlock() == this) {
                enumdirection2 = iblockdata1.get(BlockChest.FACING);
            } else {
                enumdirection2 = iblockdata2.get(BlockChest.FACING);
            }

            if (enumdirection2 == EnumDirection.WEST) {
                enumdirection = EnumDirection.WEST;
            }

            if ((iblockdata3.b() || iblockdata7.b()) && !iblockdata4.b() && !iblockdata8.b()) {
                enumdirection = EnumDirection.EAST;
            }

            if ((iblockdata4.b() || iblockdata8.b()) && !iblockdata3.b() && !iblockdata7.b()) {
                enumdirection = EnumDirection.WEST;
            }
        }

        iblockdata = iblockdata.set(BlockChest.FACING, enumdirection);
        world.setTypeAndData(blockposition, iblockdata, 3);
        return iblockdata;
    }

    public IBlockData f(World world, BlockPosition blockposition, IBlockData iblockdata) {
        EnumDirection enumdirection = null;
        Iterator iterator = EnumDirection.EnumDirectionLimit.HORIZONTAL.iterator();

        while (iterator.hasNext()) {
            EnumDirection enumdirection1 = (EnumDirection) iterator.next();
            IBlockData iblockdata1 = world.getType(blockposition.shift(enumdirection1));

            if (iblockdata1.getBlock() == this) {
                return iblockdata;
            }

            if (iblockdata1.b()) {
                if (enumdirection != null) {
                    enumdirection = null;
                    break;
                }

                enumdirection = enumdirection1;
            }
        }

        if (enumdirection != null) {
            return iblockdata.set(BlockChest.FACING, enumdirection.opposite());
        } else {
            EnumDirection enumdirection2 = iblockdata.get(BlockChest.FACING);

            if (world.getType(blockposition.shift(enumdirection2)).b()) {
                enumdirection2 = enumdirection2.opposite();
            }

            if (world.getType(blockposition.shift(enumdirection2)).b()) {
                enumdirection2 = enumdirection2.e();
            }

            if (world.getType(blockposition.shift(enumdirection2)).b()) {
                enumdirection2 = enumdirection2.opposite();
            }

            return iblockdata.set(BlockChest.FACING, enumdirection2);
        }
    }

    @Override
    public boolean canPlace(World world, BlockPosition blockposition) {
        int i = 0;
        BlockPosition blockposition1 = blockposition.west();
        BlockPosition blockposition2 = blockposition.east();
        BlockPosition blockposition3 = blockposition.north();
        BlockPosition blockposition4 = blockposition.south();

        if (world.getType(blockposition1).getBlock() == this) {
            if (this.d(world, blockposition1)) {
                return false;
            }

            ++i;
        }

        if (world.getType(blockposition2).getBlock() == this) {
            if (this.d(world, blockposition2)) {
                return false;
            }

            ++i;
        }

        if (world.getType(blockposition3).getBlock() == this) {
            if (this.d(world, blockposition3)) {
                return false;
            }

            ++i;
        }

        if (world.getType(blockposition4).getBlock() == this) {
            if (this.d(world, blockposition4)) {
                return false;
            }

            ++i;
        }

        return i <= 1;
    }

    private boolean d(World world, BlockPosition blockposition) {
        if (world.getType(blockposition).getBlock() != this) {
            return false;
        } else {
            Iterator iterator = EnumDirection.EnumDirectionLimit.HORIZONTAL.iterator();

            EnumDirection enumdirection;

            do {
                if (!iterator.hasNext()) {
                    return false;
                }

                enumdirection = (EnumDirection) iterator.next();
            } while (world.getType(blockposition.shift(enumdirection)).getBlock() != this);

            return true;
        }
    }

    @Override
    public void a(IBlockData iblockdata, World world, BlockPosition blockposition, Block block, BlockPosition blockposition1) {
        super.a(iblockdata, world, blockposition, block, blockposition1);
        TileEntity tileentity = world.getTileEntity(blockposition);

        if (tileentity instanceof TileEntityChest) {
            tileentity.invalidateBlockCache();
        }

    }

    @Override
    public void remove(World world, BlockPosition blockposition, IBlockData iblockdata) {
        TileEntity tileentity = world.getTileEntity(blockposition);

        if (tileentity instanceof IInventory) {
            InventoryUtils.dropInventory(world, blockposition, (IInventory) tileentity);
            world.updateAdjacentComparators(blockposition, this);
        }

        super.remove(world, blockposition, iblockdata);
    }

    @Override
    public boolean interact(World world, BlockPosition blockposition, IBlockData iblockdata, EntityHuman entityhuman, EnumHand enumhand, EnumDirection enumdirection, float f, float f1, float f2) {
        ITileInventory itileinventory = this.c(world, blockposition);

        if (itileinventory != null) {
            entityhuman.openContainer(itileinventory);
            if (this.g == BlockChest.Type.BASIC) {
                entityhuman.b(StatisticList.aa);
            } else if (this.g == BlockChest.Type.TRAP) {
                entityhuman.b(StatisticList.U);
            }
        }

        return true;
    }

    @Nullable
    public ITileInventory c(World world, BlockPosition blockposition) {
        return this.a(world, blockposition, false);
    }

    @Nullable
    public ITileInventory a(World world, BlockPosition blockposition, boolean flag) {
        TileEntity tileentity = world.getTileEntity(blockposition);

        if (!(tileentity instanceof TileEntityChest)) {
            return null;
        } else {
            Object object = tileentity;

            if (!flag && this.e(world, blockposition)) {
                return null;
            } else {
                Iterator iterator = EnumDirection.EnumDirectionLimit.HORIZONTAL.iterator();

                while (iterator.hasNext()) {
                    EnumDirection enumdirection = (EnumDirection) iterator.next();
                    BlockPosition blockposition1 = blockposition.shift(enumdirection);
                    // Paper start - don't load chunks if the other side of the chest is in unloaded chunk
                    final IBlockData type = world.getTypeIfLoaded(blockposition1); // Paper
                    if (type ==  null) {
                        continue;
                    }
                    Block block = type.getBlock();
                    // Paper end

                    if (block == this) {
                        if (this.e(world, blockposition1)) {
                            return null;
                        }

                        TileEntity tileentity1 = world.getTileEntity(blockposition1);

                        if (tileentity1 instanceof TileEntityChest) {
                            if (enumdirection != EnumDirection.WEST && enumdirection != EnumDirection.NORTH) {
                                object = new InventoryLargeChest("container.chestDouble", (ITileInventory) object, (TileEntityChest) tileentity1);
                            } else {
                                object = new InventoryLargeChest("container.chestDouble", (TileEntityChest) tileentity1, (ITileInventory) object);
                            }
                        }
                    }
                }

                return (ITileInventory) object;
            }
        }
    }

    @Override
    public TileEntity a(World world, int i) {
        return new TileEntityChest();
    }

    @Override
    public boolean isPowerSource(IBlockData iblockdata) {
        return this.g == BlockChest.Type.TRAP;
    }

    @Override
    public int b(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, EnumDirection enumdirection) {
        if (!iblockdata.n()) {
            return 0;
        } else {
            int i = 0;
            TileEntity tileentity = iblockaccess.getTileEntity(blockposition);

            if (tileentity instanceof TileEntityChest) {
                i = ((TileEntityChest) tileentity).l;
            }

            return MathHelper.clamp(i, 0, 15);
        }
    }

    @Override
    public int c(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, EnumDirection enumdirection) {
        return enumdirection == EnumDirection.UP ? iblockdata.a(iblockaccess, blockposition, enumdirection) : 0;
    }

    private boolean e(World world, BlockPosition blockposition) {
        return this.i(world, blockposition) || this.j(world, blockposition);
    }

    private boolean i(World world, BlockPosition blockposition) {
        return world.getType(blockposition.up()).m();
    }

    private boolean j(World world, BlockPosition blockposition) {
        // Paper start - Option ti dsiable chest cat detection
        if (world.paperConfig.disableChestCatDetection) {
            return false;
        }
        // Paper end
        Iterator iterator = world.a(EntityOcelot.class, new AxisAlignedBB(blockposition.getX(), blockposition.getY() + 1, blockposition.getZ(), blockposition.getX() + 1, blockposition.getY() + 2, blockposition.getZ() + 1)).iterator();

        EntityOcelot entityocelot;

        do {
            if (!iterator.hasNext()) {
                return false;
            }

            Entity entity = (Entity) iterator.next();

            entityocelot = (EntityOcelot) entity;
        } while (!entityocelot.isSitting());

        return true;
    }

    @Override
    public boolean isComplexRedstone(IBlockData iblockdata) {
        return true;
    }

    @Override
    public int c(IBlockData iblockdata, World world, BlockPosition blockposition) {
        return Container.b(this.c(world, blockposition));
    }

    @Override
    public IBlockData fromLegacyData(int i) {
        EnumDirection enumdirection = EnumDirection.fromType1(i);

        if (enumdirection.k() == EnumDirection.EnumAxis.Y) {
            enumdirection = EnumDirection.NORTH;
        }

        return this.getBlockData().set(BlockChest.FACING, enumdirection);
    }

    @Override
    public int toLegacyData(IBlockData iblockdata) {
        return iblockdata.get(BlockChest.FACING).a();
    }

    @Override
    public IBlockData a(IBlockData iblockdata, EnumBlockRotation enumblockrotation) {
        return iblockdata.set(BlockChest.FACING, enumblockrotation.a(iblockdata.get(BlockChest.FACING)));
    }

    @Override
    public IBlockData a(IBlockData iblockdata, EnumBlockMirror enumblockmirror) {
        return iblockdata.a(enumblockmirror.a(iblockdata.get(BlockChest.FACING)));
    }

    @Override
    protected BlockStateList getStateList() {
        return new BlockStateList(this, new IBlockState[] { BlockChest.FACING});
    }

    public static enum Type {

        BASIC, TRAP;

        private Type() {}
    }
}
