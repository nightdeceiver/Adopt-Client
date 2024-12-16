package keystrokesmod.script.classes;

import keystrokesmod.utility.BlockUtils;
import keystrokesmod.utility.RotationUtils;
import keystrokesmod.utility.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.init.Blocks;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;

import java.util.*;

public class World {
    private Minecraft mc = Minecraft.getMinecraft();

    public Block getBlockAt(int x, int y, int z) {
        net.minecraft.block.Block block = BlockUtils.getBlock(new BlockPos(x, y, z));
        if (block == null) {
            return new Block(Blocks.air);
        }
        return new Block(block);

    }

    public Block getBlockAt(Vec3 pos) {
        net.minecraft.block.Block block = BlockUtils.getBlock(new BlockPos(pos.x, pos.y, pos.z));
        if (block == null) {
            return new Block(Blocks.air);
        }
        return new Block(block);
    }

    public String getDimension() {
        if (mc.theWorld == null) {
            return "";
        }
        return mc.theWorld.provider.getDimensionName();
    }

    public List<Entity> getEntities() {
        List<Entity> entities = new ArrayList<>();
        for (net.minecraft.entity.Entity entity : mc.theWorld.loadedEntityList) {
            entities.add(Entity.convert(entity));
        }
        return entities;
    }

    public Entity getEntityById(int entityId) {
        for (net.minecraft.entity.Entity entity : mc.theWorld.loadedEntityList) {
            if (entity.getEntityId() == entityId) {
                return Entity.convert(entity);
            }
        }
        return null;
    }

    public List<NetworkPlayer> getNetworkPlayers() {
        List<NetworkPlayer> entities = new ArrayList<>();
        for (NetworkPlayerInfo networkPlayerInfo : Utils.getTablist()) {
            entities.add(new NetworkPlayer(networkPlayerInfo));
        }
        return entities;
    }

    public List<Entity> getPlayerEntities() {
        List<Entity> entities = new ArrayList<>();
        for (net.minecraft.entity.Entity entity : mc.theWorld.playerEntities) {
            entities.add(Entity.convert(entity));
        }
        return entities;
    }

    public List<String> getScoreboard() {
        List<String> sidebarLines = Utils.getSidebarLines();
        if (sidebarLines.isEmpty()) {
            return null;
        }
        return sidebarLines;
    }

    public Map<String, List<String>> getTeams() {
        Map<String, List<String>> teams = new HashMap<>();
        for (Team team : mc.theWorld.getScoreboard().getTeams()) {
            List<String> members = new ArrayList<>();
            for (String member : team.getMembershipCollection()) {
                members.add(member);
            }
            teams.put(team.getRegisteredName(), members);
        }
        return teams;
    }

    public List<TileEntity> getTileEntities() {
        List<TileEntity> tileEntities = new ArrayList<>();
        for (net.minecraft.tileentity.TileEntity entity : mc.theWorld.loadedTileEntityList) {
            tileEntities.add(new TileEntity(entity));
        }
        return tileEntities;
    }

    public Object[] raycastBlock(double distance) {
        return raycastBlock(distance, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);
    }

    public Object[] raycastBlock(double distance, float yaw, float pitch) {
        MovingObjectPosition hit = RotationUtils.rayCast(distance, yaw, pitch);
        if (hit == null || hit.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK) {
            return null;
        }
        return new Object[]{Vec3.convert(hit.getBlockPos()), new Vec3(hit.hitVec.xCoord, hit.hitVec.yCoord, hit.hitVec.zCoord), hit.sideHit.name()};
    }

    public Object[] raycastEntity(double distance) {
        return raycastEntity(distance, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);
    }

    public Object[] raycastEntity(double distance, float yaw, float pitch) {
        MovingObjectPosition hit = RotationUtils.rayCast(distance, yaw, pitch);
        if (hit == null || hit.typeOfHit != MovingObjectPosition.MovingObjectType.ENTITY) {
            return null;
        }
        return new Object[]{Entity.convert(hit.entityHit), new Vec3(hit.hitVec.xCoord, hit.hitVec.yCoord, hit.hitVec.zCoord), mc.thePlayer.getDistanceSqToEntity(hit.entityHit)};
    }
}
