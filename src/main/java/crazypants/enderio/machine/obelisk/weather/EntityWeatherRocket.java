package crazypants.enderio.machine.obelisk.weather;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFirework;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import crazypants.enderio.machine.obelisk.weather.TileWeatherObelisk.WeatherTask;

public class EntityWeatherRocket extends EntityFireworkRocket {

  private static final int DATA_ID = 24;
  
  private static final int MAX_AGE = 70;

  public EntityWeatherRocket(World world) {
    super(world);
    ReflectionHelper.setPrivateValue(EntityFireworkRocket.class, this, MAX_AGE, "field_92055_b", "lifetime");
  }

  public EntityWeatherRocket(World world, WeatherTask task) {
    this(world);
    this.getDataWatcher().updateObject(DATA_ID, task.ordinal());
  }

  @Override
  protected void entityInit() {
    super.entityInit();
    this.getDataWatcher().addObject(DATA_ID, 0);
    this.getDataWatcher().setObjectWatched(DATA_ID);
  }
  
  @Override
  public void onEntityUpdate() {
    super.onEntityUpdate();
    if (worldObj.isRemote && ticksExisted % (MAX_AGE / 10) == 0 && ticksExisted > 30) {
      doEffect();
    }
  }
  
  @Override
  public void moveEntity(double p_70091_1_, double p_70091_3_, double p_70091_5_) {
    super.moveEntity(p_70091_1_, p_70091_3_, p_70091_5_);
  }
  
  @Override
  public void setDead() {
    super.setDead();
    WeatherTask task = WeatherTask.values()[getDataWatcher().getWatchableObjectInt(DATA_ID)];
    task.complete(worldObj);
  }
  
  @Override
  public void handleStatusUpdate(byte id) {
  }
  
  private void doEffect() {
    String s1 = "fireworks.largeBlast";
    if (ticksExisted > 40) {
      s1 += "_far";
    }
    this.worldObj.playSound(this.posX, this.posY, this.posZ, s1, 20.0F, 0.95F + this.rand.nextFloat() * 0.1F, true);

    double d1 = this.posX;
    double d2 = this.posY;
    double d3 = this.posZ;

    int size = 5;
    double speed = 1;

    for (int j = -size; j <= size; ++j) {
      for (int k = -size; k <= size; ++k) {
        for (int l = -size; l <= size; ++l) {
          double d4 = k + (this.rand.nextDouble() - this.rand.nextDouble()) * 0.5D;
          double d5 = j + (this.rand.nextDouble() - this.rand.nextDouble()) * 0.5D;
          double d6 = l + (this.rand.nextDouble() - this.rand.nextDouble()) * 0.5D;
          double d7 = MathHelper.sqrt_double(d4 * d4 + d5 * d5 + d6 * d6) / speed + this.rand.nextGaussian() * 0.05D;

          EntityFirework.SparkFX entityfireworksparkfx = new EntityFirework.SparkFX(this.worldObj, d1, d2, d3, d4 / d7, d5 / d7, d6 / d7,
              Minecraft.getMinecraft().effectRenderer);

          entityfireworksparkfx.setTrail(true);
          entityfireworksparkfx.setTwinkle(false);
          entityfireworksparkfx.setColour(WeatherTask.values()[getDataWatcher().getWatchableObjectInt(DATA_ID)].color.getRGB());

          Minecraft.getMinecraft().effectRenderer.addEffect(entityfireworksparkfx);
          if (j != -size && j != size && k != -size && k != size) {
            l += size * 2 - 1;
          }
        }
      }
    }
  }
}
