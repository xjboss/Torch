package net.minecraft.server;

import java.util.Random;
import javax.annotation.Nullable;

public class EntitySpider extends EntityMonster {

    private static final DataWatcherObject<Byte> a = DataWatcher.a(EntitySpider.class, DataWatcherRegistry.a);

    public EntitySpider(World world) {
        super(world);
        this.setSize(1.4F, 0.9F);
    }

    public static void c(DataConverterManager dataconvertermanager) {
        EntityInsentient.a(dataconvertermanager, EntitySpider.class);
    }

    @Override
    protected void r() {
        this.goalSelector.a(1, new PathfinderGoalFloat(this));
        this.goalSelector.a(3, new PathfinderGoalLeapAtTarget(this, 0.4F));
        this.goalSelector.a(4, new EntitySpider.PathfinderGoalSpiderMeleeAttack(this));
        this.goalSelector.a(5, new PathfinderGoalRandomStrollLand(this, 0.8D));
        this.goalSelector.a(6, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
        this.goalSelector.a(6, new PathfinderGoalRandomLookaround(this));
        this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, false, new Class[0]));
        this.targetSelector.a(2, new EntitySpider.PathfinderGoalSpiderNearestAttackableTarget(this, EntityHuman.class));
        this.targetSelector.a(3, new EntitySpider.PathfinderGoalSpiderNearestAttackableTarget(this, EntityIronGolem.class));
    }

    @Override
    public double ay() {
        return this.length * 0.5F;
    }

    @Override
    protected NavigationAbstract b(World world) {
        return new NavigationSpider(this, world);
    }

    @Override
    protected void i() {
        super.i();
        this.datawatcher.register(EntitySpider.a, Byte.valueOf((byte) 0));
    }

    @Override
    public void A_() {
        super.A_();
        this.a(this.positionChanged);

    }

    @Override
    protected void initAttributes() {
        super.initAttributes();
        this.getAttributeInstance(GenericAttributes.maxHealth).setValue(16.0D);
        this.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.30000001192092896D);
    }

    @Override
    protected SoundEffect G() {
        return SoundEffects.gy;
    }

    @Override
    protected SoundEffect bW() {
        return SoundEffects.gA;
    }

    @Override
    protected SoundEffect bX() {
        return SoundEffects.gz;
    }

    @Override
    protected void a(BlockPosition blockposition, Block block) {
        this.a(SoundEffects.gB, 0.15F, 1.0F);
    }

    @Override
    @Nullable
    protected MinecraftKey J() {
        return LootTables.s;
    }

    @Override
    public boolean m_() {
        return this.o();
    }

    @Override
    public void aS() {}

    @Override
    public EnumMonsterType getMonsterType() {
        return EnumMonsterType.ARTHROPOD;
    }

    @Override
    public boolean d(MobEffect mobeffect) {
        return mobeffect.getMobEffect() == MobEffects.POISON ? false : super.d(mobeffect);
    }

    public boolean o() {
        return (this.datawatcher.get(EntitySpider.a).byteValue() & 1) != 0;
    }

    public void a(boolean flag) {
        byte b0 = this.datawatcher.get(EntitySpider.a).byteValue();

        if (flag) {
            b0 = (byte) (b0 | 1);
        } else {
            b0 &= -2;
        }

        this.datawatcher.set(EntitySpider.a, Byte.valueOf(b0));
    }

    @Override
    @Nullable
    public GroupDataEntity prepare(DifficultyDamageScaler difficultydamagescaler, @Nullable GroupDataEntity groupdataentity) {
        Object object = super.prepare(difficultydamagescaler, groupdataentity);

        if (this.world.random.nextInt(100) == 0) {
            EntitySkeleton entityskeleton = new EntitySkeleton(this.world);

            entityskeleton.setPositionRotation(this.locX, this.locY, this.locZ, this.yaw, 0.0F);
            entityskeleton.prepare(difficultydamagescaler, (GroupDataEntity) null);
            this.world.addEntity(entityskeleton, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.JOCKEY); // CraftBukkit - add SpawnReason
            entityskeleton.startRiding(this);
        }

        if (object == null) {
            object = new EntitySpider.GroupDataSpider();
            if (this.world.getDifficulty() == EnumDifficulty.HARD && this.world.random.nextFloat() < 0.1F * difficultydamagescaler.d()) {
                ((EntitySpider.GroupDataSpider) object).a(this.world.random);
            }
        }

        if (object instanceof EntitySpider.GroupDataSpider) {
            MobEffectList mobeffectlist = ((EntitySpider.GroupDataSpider) object).a;

            if (mobeffectlist != null) {
                this.addEffect(new MobEffect(mobeffectlist, Integer.MAX_VALUE));
            }
        }

        return (GroupDataEntity) object;
    }

    @Override
    public float getHeadHeight() {
        return 0.65F;
    }

    static class PathfinderGoalSpiderNearestAttackableTarget<T extends EntityLiving> extends PathfinderGoalNearestAttackableTarget<T> {

        public PathfinderGoalSpiderNearestAttackableTarget(EntitySpider entityspider, Class<T> oclass) {
            super(entityspider, oclass, true);
        }

        @Override
        public boolean a() {
            float f = this.e.e(1.0F);

            return f >= 0.5F ? false : super.a();
        }
    }

    static class PathfinderGoalSpiderMeleeAttack extends PathfinderGoalMeleeAttack {

        public PathfinderGoalSpiderMeleeAttack(EntitySpider entityspider) {
            super(entityspider, 1.0D, true);
        }

        @Override
        public boolean b() {
            float f = this.b.e(1.0F);

            if (f >= 0.5F && this.b.getRandom().nextInt(100) == 0) {
                this.b.setGoalTarget((EntityLiving) null);
                return false;
            } else {
                return super.b();
            }
        }

        @Override
        protected double a(EntityLiving entityliving) {
            return 4.0F + entityliving.width;
        }
    }

    public static class GroupDataSpider implements GroupDataEntity {

        public MobEffectList a;

        public GroupDataSpider() {}

        public void a(Random random) {
            int i = random.nextInt(5);

            if (i <= 1) {
                this.a = MobEffects.FASTER_MOVEMENT;
            } else if (i <= 2) {
                this.a = MobEffects.INCREASE_DAMAGE;
            } else if (i <= 3) {
                this.a = MobEffects.REGENERATION;
            } else if (i <= 4) {
                this.a = MobEffects.INVISIBILITY;
            }

        }
    }
}
