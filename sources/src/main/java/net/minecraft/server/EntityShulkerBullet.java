package net.minecraft.server;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;

public class EntityShulkerBullet extends Entity {

    private EntityLiving shooter;
    private Entity target;
    @Nullable
    private EnumDirection c;
    private int d;
    private double e;
    private double f;
    private double g;
    @Nullable
    private UUID h;
    private BlockPosition at;
    @Nullable
    private UUID au;
    private BlockPosition av;

    public EntityShulkerBullet(World world) {
        super(world);
        this.setSize(0.3125F, 0.3125F);
        this.noclip = true;
    }

    @Override
    public SoundCategory bC() {
        return SoundCategory.HOSTILE;
    }

    public EntityShulkerBullet(World world, EntityLiving entityliving, Entity entity, EnumDirection.EnumAxis enumdirection_enumaxis) {
        this(world);
        this.shooter = entityliving;
        BlockPosition blockposition = new BlockPosition(entityliving);
        double d0 = blockposition.getX() + 0.5D;
        double d1 = blockposition.getY() + 0.5D;
        double d2 = blockposition.getZ() + 0.5D;

        this.setPositionRotation(d0, d1, d2, this.yaw, this.pitch);
        this.target = entity;
        this.c = EnumDirection.UP;
        this.a(enumdirection_enumaxis);
        projectileSource = (org.bukkit.entity.LivingEntity) entityliving.getBukkitEntity(); // CraftBukkit
    }

    // CraftBukkit start
    public EntityLiving getShooter() {
        return this.shooter;
    }

    public void setShooter(EntityLiving e) {
        this.shooter = e;
    }

    public Entity getTarget() {
        return this.target;
    }

    public void setTarget(Entity e) {
        this.target = e;
        this.c = EnumDirection.UP;
        this.a(EnumDirection.EnumAxis.X);
    }
    // CraftBukkit end

    @Override
    protected void b(NBTTagCompound nbttagcompound) {
        BlockPosition blockposition;
        NBTTagCompound nbttagcompound1;

        if (this.shooter != null) {
            blockposition = new BlockPosition(this.shooter);
            nbttagcompound1 = GameProfileSerializer.a(this.shooter.getUniqueID());
            nbttagcompound1.setInt("X", blockposition.getX());
            nbttagcompound1.setInt("Y", blockposition.getY());
            nbttagcompound1.setInt("Z", blockposition.getZ());
            nbttagcompound.set("Owner", nbttagcompound1);
        }

        if (this.target != null) {
            blockposition = new BlockPosition(this.target);
            nbttagcompound1 = GameProfileSerializer.a(this.target.getUniqueID());
            nbttagcompound1.setInt("X", blockposition.getX());
            nbttagcompound1.setInt("Y", blockposition.getY());
            nbttagcompound1.setInt("Z", blockposition.getZ());
            nbttagcompound.set("Target", nbttagcompound1);
        }

        if (this.c != null) {
            nbttagcompound.setInt("Dir", this.c.a());
        }

        nbttagcompound.setInt("Steps", this.d);
        nbttagcompound.setDouble("TXD", this.e);
        nbttagcompound.setDouble("TYD", this.f);
        nbttagcompound.setDouble("TZD", this.g);
    }

    @Override
    protected void a(NBTTagCompound nbttagcompound) {
        this.d = nbttagcompound.getInt("Steps");
        this.e = nbttagcompound.getDouble("TXD");
        this.f = nbttagcompound.getDouble("TYD");
        this.g = nbttagcompound.getDouble("TZD");
        if (nbttagcompound.hasKeyOfType("Dir", 99)) {
            this.c = EnumDirection.fromType1(nbttagcompound.getInt("Dir"));
        }

        NBTTagCompound nbttagcompound1;

        if (nbttagcompound.hasKeyOfType("Owner", 10)) {
            nbttagcompound1 = nbttagcompound.getCompound("Owner");
            this.h = GameProfileSerializer.b(nbttagcompound1);
            this.at = new BlockPosition(nbttagcompound1.getInt("X"), nbttagcompound1.getInt("Y"), nbttagcompound1.getInt("Z"));
        }

        if (nbttagcompound.hasKeyOfType("Target", 10)) {
            nbttagcompound1 = nbttagcompound.getCompound("Target");
            this.au = GameProfileSerializer.b(nbttagcompound1);
            this.av = new BlockPosition(nbttagcompound1.getInt("X"), nbttagcompound1.getInt("Y"), nbttagcompound1.getInt("Z"));
        }

    }

    @Override
    protected void i() {}

    private void a(@Nullable EnumDirection enumdirection) {
        this.c = enumdirection;
    }

    private void a(@Nullable EnumDirection.EnumAxis enumdirection_enumaxis) {
        double d0 = 0.5D;
        BlockPosition blockposition;

        if (this.target == null) {
            blockposition = (new BlockPosition(this)).down();
        } else {
            d0 = this.target.length * 0.5D;
            blockposition = new BlockPosition(this.target.locX, this.target.locY + d0, this.target.locZ);
        }

        double d1 = blockposition.getX() + 0.5D;
        double d2 = blockposition.getY() + d0;
        double d3 = blockposition.getZ() + 0.5D;
        EnumDirection enumdirection = null;

        if (blockposition.g(this.locX, this.locY, this.locZ) >= 4.0D) {
            BlockPosition blockposition1 = new BlockPosition(this);
            ArrayList arraylist = Lists.newArrayList();

            if (enumdirection_enumaxis != EnumDirection.EnumAxis.X) {
                if (blockposition1.getX() < blockposition.getX() && this.world.isEmpty(blockposition1.east())) {
                    arraylist.add(EnumDirection.EAST);
                } else if (blockposition1.getX() > blockposition.getX() && this.world.isEmpty(blockposition1.west())) {
                    arraylist.add(EnumDirection.WEST);
                }
            }

            if (enumdirection_enumaxis != EnumDirection.EnumAxis.Y) {
                if (blockposition1.getY() < blockposition.getY() && this.world.isEmpty(blockposition1.up())) {
                    arraylist.add(EnumDirection.UP);
                } else if (blockposition1.getY() > blockposition.getY() && this.world.isEmpty(blockposition1.down())) {
                    arraylist.add(EnumDirection.DOWN);
                }
            }

            if (enumdirection_enumaxis != EnumDirection.EnumAxis.Z) {
                if (blockposition1.getZ() < blockposition.getZ() && this.world.isEmpty(blockposition1.south())) {
                    arraylist.add(EnumDirection.SOUTH);
                } else if (blockposition1.getZ() > blockposition.getZ() && this.world.isEmpty(blockposition1.north())) {
                    arraylist.add(EnumDirection.NORTH);
                }
            }

            enumdirection = EnumDirection.a(this.random);
            if (arraylist.isEmpty()) {
                for (int i = 5; !this.world.isEmpty(blockposition1.shift(enumdirection)) && i > 0; --i) {
                    enumdirection = EnumDirection.a(this.random);
                }
            } else {
                enumdirection = (EnumDirection) arraylist.get(this.random.nextInt(arraylist.size()));
            }

            d1 = this.locX + enumdirection.getAdjacentX();
            d2 = this.locY + enumdirection.getAdjacentY();
            d3 = this.locZ + enumdirection.getAdjacentZ();
        }

        this.a(enumdirection);
        double d4 = d1 - this.locX;
        double d5 = d2 - this.locY;
        double d6 = d3 - this.locZ;
        double d7 = MathHelper.sqrt(d4 * d4 + d5 * d5 + d6 * d6);

        if (d7 == 0.0D) {
            this.e = 0.0D;
            this.f = 0.0D;
            this.g = 0.0D;
        } else {
            this.e = d4 / d7 * 0.15D;
            this.f = d5 / d7 * 0.15D;
            this.g = d6 / d7 * 0.15D;
        }

        this.impulse = true;
        this.d = 10 + this.random.nextInt(5) * 10;
    }

    @Override
    public void A_() {
        if (this.world.getDifficulty() == EnumDifficulty.PEACEFUL) {
            this.die();
        } else {
            super.A_();
            List list;
            Iterator iterator;
            EntityLiving entityliving;

            if (this.target == null && this.au != null) {
                list = this.world.a(EntityLiving.class, new AxisAlignedBB(this.av.a(-2, -2, -2), this.av.a(2, 2, 2)));
                iterator = list.iterator();

                while (iterator.hasNext()) {
                    entityliving = (EntityLiving) iterator.next();
                    if (entityliving.getUniqueID().equals(this.au)) {
                        this.target = entityliving;
                        break;
                    }
                }

                this.au = null;
            }

            if (this.shooter == null && this.h != null) {
                list = this.world.a(EntityLiving.class, new AxisAlignedBB(this.at.a(-2, -2, -2), this.at.a(2, 2, 2)));
                iterator = list.iterator();

                while (iterator.hasNext()) {
                    entityliving = (EntityLiving) iterator.next();
                    if (entityliving.getUniqueID().equals(this.h)) {
                        this.shooter = entityliving;
                        break;
                    }
                }

                this.h = null;
            }

            if (this.target != null && this.target.isAlive() && (!(this.target instanceof EntityHuman) || !((EntityHuman) this.target).isSpectator())) {
                this.e = MathHelper.a(this.e * 1.025D, -1.0D, 1.0D);
                this.f = MathHelper.a(this.f * 1.025D, -1.0D, 1.0D);
                this.g = MathHelper.a(this.g * 1.025D, -1.0D, 1.0D);
                this.motX += (this.e - this.motX) * 0.2D;
                this.motY += (this.f - this.motY) * 0.2D;
                this.motZ += (this.g - this.motZ) * 0.2D;
            } else if (!this.isNoGravity()) {
                this.motY -= 0.04D;
            }

            MovingObjectPosition movingobjectposition = ProjectileHelper.a(this, true, false, this.shooter);

            if (movingobjectposition != null) {
                this.a(movingobjectposition);
            }

            this.setPosition(this.locX + this.motX, this.locY + this.motY, this.locZ + this.motZ);
            ProjectileHelper.a(this, 0.5F);
            if (this.target != null && !this.target.dead) {
                if (this.d > 0) {
                    --this.d;
                    if (this.d == 0) {
                        this.a(this.c == null ? null : this.c.k());
                    }
                }

                if (this.c != null) {
                    BlockPosition blockposition = new BlockPosition(this);
                    EnumDirection.EnumAxis enumdirection_enumaxis = this.c.k();

                    if (this.world.d(blockposition.shift(this.c), false)) {
                        this.a(enumdirection_enumaxis);
                    } else {
                        BlockPosition blockposition1 = new BlockPosition(this.target);

                        if (enumdirection_enumaxis == EnumDirection.EnumAxis.X && blockposition.getX() == blockposition1.getX() || enumdirection_enumaxis == EnumDirection.EnumAxis.Z && blockposition.getZ() == blockposition1.getZ() || enumdirection_enumaxis == EnumDirection.EnumAxis.Y && blockposition.getY() == blockposition1.getY()) {
                            this.a(enumdirection_enumaxis);
                        }
                    }
                }
            }

        }
    }

    @Override
    public boolean isBurning() {
        return false;
    }

    @Override
    public float e(float f) {
        return 1.0F;
    }

    protected void a(MovingObjectPosition movingobjectposition) {
        if (movingobjectposition.entity == null) {
            ((WorldServer) this.world).a(EnumParticle.EXPLOSION_LARGE, this.locX, this.locY, this.locZ, 2, 0.2D, 0.2D, 0.2D, 0.0D, new int[0]);
            this.a(SoundEffects.fC, 1.0F, 1.0F);
        } else {
            boolean flag = movingobjectposition.entity.damageEntity(DamageSource.a(this, this.shooter).b(), 4.0F);

            if (flag) {
                this.a(this.shooter, movingobjectposition.entity);
                if (movingobjectposition.entity instanceof EntityLiving) {
                    ((EntityLiving) movingobjectposition.entity).addEffect(new MobEffect(MobEffects.LEVITATION, 200));
                }
            }
        }

        this.die();
    }

    @Override
    public boolean isInteractable() {
        return true;
    }

    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {
        this.a(SoundEffects.fD, 1.0F, 1.0F);
        ((WorldServer) this.world).a(EnumParticle.CRIT, this.locX, this.locY, this.locZ, 15, 0.2D, 0.2D, 0.2D, 0.0D, new int[0]);
        this.die();

        return true;
    }
}
