import dto.ActivityDTO;
import dto.CoinsDTO;
import dto.MembershipDTO;
import dto.PlayerStatsDTO;
import org.osbot.rs07.api.model.Item;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.MethodProvider;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

public class OsrsBotTracker {

    public final String playerStatsUrl = "/osrsbottracker/api/v1/bot/player";
    public final String playerMoneyUrl = "/osrsbottracker/api/v1/bot/player/money";
    public final String playerMembershipUrl = "/osrsbottracker/api/v1/bot/player/membership";
    public final String playerActivityUrl = "/osrsbottracker/api/v1/bot/player/activity";


    private static OsrsBotTracker instance;
    private String apiFarmKey;
    private MethodProvider mp;

    private LocalDateTime lastPlayerStatsDataPush;
    private LocalDateTime lastCoinDataPush;

    private OsrsBotTracker(MethodProvider mp, String apiFarmKey) {
        this.mp = mp;
        this.apiFarmKey = apiFarmKey;
    }

    public static void initialize(MethodProvider mp, String apiFarmKey) {
        if (instance == null) {
            synchronized (OsrsBotTracker.class) {
                if (instance == null) {
                    instance = new OsrsBotTracker(mp, apiFarmKey);
                }
            }
        }
    }

    public static boolean isInstanceInitialize() {
        return instance != null;
    }

    public static OsrsBotTracker getInstance() {
        if (instance == null) {
            throw new IllegalStateException("OsrsBotTracker must be initialized before use.");
        }
        return instance;
    }


    public void track() {
        if (Objects.isNull(apiFarmKey) || apiFarmKey.length() == 0) {
            return;
        }
        if (Objects.isNull(lastPlayerStatsDataPush) ||
                LocalDateTime.now().isAfter(lastPlayerStatsDataPush.plusMinutes(5))) {
            lastPlayerStatsDataPush = LocalDateTime.now();
            pushPlayerData();

        }
        if (mp.getBank().isOpen() && (Objects.isNull(lastCoinDataPush) ||
                LocalDateTime.now().isAfter(lastCoinDataPush.plusMinutes(5)))) {
            lastCoinDataPush = LocalDateTime.now();
            pushPlayerCoinData();
        }
    }

    public void pushPlayerData() {
        mp.log("Sending player skill data");
        String payload = createPlayerStatsPayload();
        ApiService.post(payload, apiFarmKey, playerStatsUrl, mp);
    }

    public void pushPlayerCoinData() {
        mp.log("Sending player coins data");
        CoinsDTO coinsDTO = new CoinsDTO();
        coinsDTO.setName(mp.myPlayer().getName());
        coinsDTO.setAmount(Long.valueOf(getTotalCoins()));
        String payload;
        try {
            payload = CustomObjectMapper.dtoToJsonString(coinsDTO);
        } catch (Exception e) {
            mp.log("Error sending coins data");
            return;
        }
        ApiService.post(payload, apiFarmKey, playerMoneyUrl, mp);

    }

    public void pushMembershipData(Long days) {
        mp.log("Sending player membership data");
        MembershipDTO membershipDTO = new MembershipDTO();
        membershipDTO.setName(mp.myPlayer().getName());
        membershipDTO.setAmount(days);
        String payload;
        try {
            payload = CustomObjectMapper.dtoToJsonString(membershipDTO);
        } catch (Exception e) {
            mp.log("Error sending membership data");
            return;
        }
        ApiService.post(payload, apiFarmKey, playerMembershipUrl, mp);
    }


    public void pushActivityData(String activity) {
        mp.log("Sending player activity data");
        ActivityDTO activityDTO = new ActivityDTO();
        activityDTO.setName(mp.myPlayer().getName());
        activityDTO.setActivity(activity);
        String payload;
        try {
            payload = CustomObjectMapper.dtoToJsonString(activityDTO);
        } catch (Exception e) {
            mp.log("Error sending activity data");
            return;
        }
        ApiService.post(payload, apiFarmKey, playerActivityUrl, mp);
    }

    private Integer getTotalCoins() {
        Integer coinsInBank = Optional.ofNullable(mp.getBank().getItem("Coins")).map(Item::getAmount).orElse(0);
        Integer inventoryCoins = Optional.ofNullable(mp.getInventory().getItem("Coins")).map(Item::getAmount).orElse(0);
        return inventoryCoins + coinsInBank;
    }

    private String createPlayerStatsPayload() {

        PlayerStatsDTO playerStatsDTO = new PlayerStatsDTO();
        playerStatsDTO.setName(mp.myPlayer().getName());
        playerStatsDTO.setAttackLevel((long) mp.getSkills().getDynamic(Skill.ATTACK));
        playerStatsDTO.setAttackExperience((long) mp.getSkills().getExperience(Skill.ATTACK));
        playerStatsDTO.setDefenseLevel((long) mp.getSkills().getDynamic(Skill.DEFENCE));
        playerStatsDTO.setDefenseExperience((long) mp.getSkills().getExperience(Skill.DEFENCE));
        playerStatsDTO.setStrengthLevel((long) mp.getSkills().getDynamic(Skill.STRENGTH));
        playerStatsDTO.setStrengthExperience((long) mp.getSkills().getExperience(Skill.STRENGTH));
        playerStatsDTO.setHitpointsLevel((long) mp.getSkills().getDynamic(Skill.HITPOINTS));
        playerStatsDTO.setHitpointsExperience((long) mp.getSkills().getExperience(Skill.HITPOINTS));
        playerStatsDTO.setRangeLevel((long) mp.getSkills().getDynamic(Skill.RANGED));
        playerStatsDTO.setRangeExperience((long) mp.getSkills().getExperience(Skill.RANGED));
        playerStatsDTO.setPrayerLevel((long) mp.getSkills().getDynamic(Skill.PRAYER));
        playerStatsDTO.setPrayerExperience((long) mp.getSkills().getExperience(Skill.PRAYER));
        playerStatsDTO.setMagicLevel((long) mp.getSkills().getDynamic(Skill.MAGIC));
        playerStatsDTO.setMagicExperience((long) mp.getSkills().getExperience(Skill.MAGIC));
        playerStatsDTO.setCookingLevel((long) mp.getSkills().getDynamic(Skill.COOKING));
        playerStatsDTO.setCookingExperience((long) mp.getSkills().getExperience(Skill.COOKING));
        playerStatsDTO.setWoodcuttingLevel((long) mp.getSkills().getDynamic(Skill.WOODCUTTING));
        playerStatsDTO.setWoodcuttingExperience((long) mp.getSkills().getExperience(Skill.WOODCUTTING));
        playerStatsDTO.setFletchingLevel((long) mp.getSkills().getDynamic(Skill.FLETCHING));
        playerStatsDTO.setFletchingExperience((long) mp.getSkills().getExperience(Skill.FLETCHING));
        playerStatsDTO.setFishingLevel((long) mp.getSkills().getDynamic(Skill.FISHING));
        playerStatsDTO.setFishingExperience((long) mp.getSkills().getExperience(Skill.FISHING));
        playerStatsDTO.setFiremakingLevel((long) mp.getSkills().getDynamic(Skill.FIREMAKING));
        playerStatsDTO.setFiremakingExperience((long) mp.getSkills().getExperience(Skill.FIREMAKING));
        playerStatsDTO.setCraftingLevel((long) mp.getSkills().getDynamic(Skill.CRAFTING));
        playerStatsDTO.setCraftingExperience((long) mp.getSkills().getExperience(Skill.CRAFTING));
        playerStatsDTO.setSmithingLevel((long) mp.getSkills().getDynamic(Skill.SMITHING));
        playerStatsDTO.setSmithingExperience((long) mp.getSkills().getExperience(Skill.SMITHING));
        playerStatsDTO.setMiningLevel((long) mp.getSkills().getDynamic(Skill.MINING));
        playerStatsDTO.setMiningExperience((long) mp.getSkills().getExperience(Skill.MINING));
        playerStatsDTO.setHerbloreLevel((long) mp.getSkills().getDynamic(Skill.HERBLORE));
        playerStatsDTO.setHerbloreExperience((long) mp.getSkills().getExperience(Skill.HERBLORE));
        playerStatsDTO.setAgilityLevel((long) mp.getSkills().getDynamic(Skill.AGILITY));
        playerStatsDTO.setAgilityExperience((long) mp.getSkills().getExperience(Skill.AGILITY));
        playerStatsDTO.setThievingLevel((long) mp.getSkills().getDynamic(Skill.THIEVING));
        playerStatsDTO.setThievingExperience((long) mp.getSkills().getExperience(Skill.THIEVING));
        playerStatsDTO.setSlayerLevel((long) mp.getSkills().getDynamic(Skill.SLAYER));
        playerStatsDTO.setSlayerExperience((long) mp.getSkills().getExperience(Skill.SLAYER));
        playerStatsDTO.setFarmingLevel((long) mp.getSkills().getDynamic(Skill.FARMING));
        playerStatsDTO.setFarmingExperience((long) mp.getSkills().getExperience(Skill.FARMING));
        playerStatsDTO.setRunecraftingLevel((long) mp.getSkills().getDynamic(Skill.RUNECRAFTING));
        playerStatsDTO.setRunecraftingExperience((long) mp.getSkills().getExperience(Skill.RUNECRAFTING));
        playerStatsDTO.setHunterLevel((long) mp.getSkills().getDynamic(Skill.HUNTER));
        playerStatsDTO.setHunterExperience((long) mp.getSkills().getExperience(Skill.HUNTER));
        playerStatsDTO.setConstructionLevel((long) mp.getSkills().getDynamic(Skill.CONSTRUCTION));
        playerStatsDTO.setConstructionExperience((long) mp.getSkills().getExperience(Skill.CONSTRUCTION));
        playerStatsDTO.setQuestPoints((long) mp.getQuests().getQuestPoints());
        String payload = null;
        try {
            payload = CustomObjectMapper.dtoToJsonString(playerStatsDTO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return payload;
    }


}
