package me.Arcator.GUICommands.Listeners;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Arcator.GUICommands.Main;
import me.Arcator.GUICommands.Utils.Utils;

public class JoinandClickItem implements Listener {

	private Main plugin;

	public JoinandClickItem(Main plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onPlayerLogin(PlayerJoinEvent e) {
		if (plugin.getConfig().getBoolean("GUIItemOnLogin") == false) {
			return;
		}
		Player p = e.getPlayer();
		// Make the item.
		ItemStack item = new ItemStack(Material.getMaterial(plugin.getConfig().getString("GUIItemType")));
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(Utils.chat(plugin.getConfig().getString("GUIItemLore")));
		meta.setDisplayName(Utils.chat(plugin.getConfig().getString("GUIItemName")));
		meta.setLore(lore);
		item.setItemMeta(meta);

		// Useful variables
		int slot = p.getInventory().firstEmpty();
		int configslot = plugin.getConfig().getInt("GUIItemSlot") - 1;

		// If they already have the item.
		if (p.getInventory().contains(item)) {
			if (p.getInventory().getItem(configslot) == null) {
				p.getInventory().remove(item);
				p.getInventory().setItem(configslot, item);
				return;
			}
			return;
		}
		// If their inventory is full.
		if (slot == -1) {
			System.out.println(Utils.chat("&c[" + plugin.getName() + "] " + p.getDisplayName()
					+ "'s inventory is too full to add your login item."));
			return;
		}
		if (p.getInventory().getItem(configslot) == null) {
			p.getInventory().setItem(configslot, item);
			return;
		}
		// If player already has item
		if (p.getInventory().getItem(configslot).getType() == item.getType()) {
			return;
		}

		// If it's air, make the item there.

		if (p.getInventory().getItem(configslot).getType() == Material.AIR) {
			p.getInventory().setItem(configslot, item);
			return;
		}

		// Doesn't have an item, inventory isn't full, and it's not air, add to
		// inventory.
		System.out.println(
				Utils.chat("&cThat slot is already taken for that player, putting item in first open slot..."));
		p.getInventory().setItem(slot, item);
		return;
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		if (p.getItemInHand() == null) {
			return;
		}
		if (plugin.getConfig().getBoolean("PlayerCanDropItem") == true) {
			return;
		}
		if (e.getItemDrop().getItemStack().getItemMeta().getDisplayName() == null) {
			return;
		}
		Item item = e.getItemDrop();
		ItemMeta handmeta = item.getItemStack().getItemMeta();
		String name = handmeta.getDisplayName();
		String GUIItemName = Utils.chat(plugin.getConfig().getString("GUIItemName"));

		if (!name.equalsIgnoreCase(GUIItemName)) {
			return;
		}
		e.setCancelled(true);
	}

	// Open by right click
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerUse(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (p.getItemInHand().getType() != Material.getMaterial(plugin.getConfig().getString("GUIItemType"))) {
			return;
		}

		ItemMeta hand = p.getItemInHand().getItemMeta();
		String name = hand.getDisplayName();
		String GUIItemName = Utils.chat(plugin.getConfig().getString("GUIItemName"));
		if (!name.equalsIgnoreCase(GUIItemName)) {
			return;
		}
		if (e.getClickedBlock() == null) {

			p.chat(plugin.getConfig().getString("GUICommand"));
			return;
		}

		try {
			Material m = e.getClickedBlock().getType();
			Material oakplate = Material.OAK_PRESSURE_PLATE;
			Material acaciaplate = Material.ACACIA_PRESSURE_PLATE;
			Material birchplate = Material.BIRCH_PRESSURE_PLATE;
			Material doakplate = Material.DARK_OAK_PRESSURE_PLATE;
			Material heavyplate = Material.HEAVY_WEIGHTED_PRESSURE_PLATE;
			Material jungleplate = Material.JUNGLE_PRESSURE_PLATE;
			Material lightplate = Material.LIGHT_WEIGHTED_PRESSURE_PLATE;
			Material spruceplate = Material.SPRUCE_PRESSURE_PLATE;
			Material stoneplate = Material.STONE_PRESSURE_PLATE;
			Material tripwire = Material.TRIPWIRE;
			Material itemframe = Material.ITEM_FRAME;
			// Got to do entityinteract event
			if (m == oakplate || m == acaciaplate || m == birchplate || m == doakplate || m == heavyplate
					|| m == jungleplate || m == jungleplate || m == lightplate || m == spruceplate || m == stoneplate
					|| m == tripwire || m == itemframe) {
				return;
			}
		} catch (NoSuchFieldError npe) {
			// Only will go off in 1.12 when a player steps on one of those things above
			return;
		}
		p.chat(plugin.getConfig().getString("GUICommand"));
		return;

	}

	// This is to stop players from putting the GUI Item in an item frame
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onEntityInteract(PlayerInteractEntityEvent e) {
		Player p = e.getPlayer();
		if (p.getItemInHand().getItemMeta() == null) {
			return;
		}
		ItemMeta hand = p.getItemInHand().getItemMeta();
		String name = hand.getDisplayName();
		String GUIItemName = Utils.chat(plugin.getConfig().getString("GUIItemName"));
		
		if (!(e.getRightClicked() instanceof ItemFrame)) {
			return;
		}
		if (p.getGameMode() == GameMode.CREATIVE) {
			return;
		}
		if (plugin.getConfig().getBoolean("PlayerCanMoveItem") == true) {
			return;
		}
		if (name.equalsIgnoreCase(GUIItemName)) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		if (plugin.getConfig().getBoolean("GUIItemOnLogin") == false) {
			return;
		}
		if (plugin.getConfig().getBoolean("PlayerDropItemOnDeath") == true) {
			return;
		}
	//	Player p = e.getEntity();

		ItemStack item = new ItemStack(Material.getMaterial(plugin.getConfig().getString("GUIItemType")));
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(Utils.chat(plugin.getConfig().getString("GUIItemLore")));
		meta.setDisplayName(Utils.chat(plugin.getConfig().getString("GUIItemName")));
		meta.setLore(lore);
		item.setItemMeta(meta);

		if (e.getDrops().contains(item)) {

			e.getDrops().remove(item);
		}
	}

	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		Player p = e.getPlayer();
		System.out.println(p);
		if (plugin.getConfig().getBoolean("GUIItemOnLogin") == false) {
			return;
		}

		if (plugin.getConfig().getBoolean("PlayerDropItemOnDeath") == true) {
			return;
		}

		// Make the item.
		ItemStack item = new ItemStack(Material.getMaterial(plugin.getConfig().getString("GUIItemType")));
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(Utils.chat(plugin.getConfig().getString("GUIItemLore")));
		meta.setDisplayName(Utils.chat(plugin.getConfig().getString("GUIItemName")));
		meta.setLore(lore);
		item.setItemMeta(meta);

		// Useful variables
		int slot = p.getInventory().firstEmpty();
		int configslot = plugin.getConfig().getInt("GUIItemSlot") - 1;

		// If they already have the item.
		if (p.getInventory().contains(item)) {
			if (p.getInventory().getItem(configslot) == null) {
				p.getInventory().remove(item);
				p.getInventory().setItem(configslot, item);
				return;
			}
			return;
		}
		// If their inventory is full.
		if (slot == -1) {
			System.out.println(Utils.chat("&c[" + plugin.getName() + "] " + p.getDisplayName()
					+ "'s inventory is too full to add your login item on respawn."));
			return;
		}
		if (p.getInventory().getItem(configslot) == null) {
			p.getInventory().setItem(configslot, item);
			return;
		}
		// If player already has item
		if (p.getInventory().getItem(configslot).getType() == item.getType()) {
			return;
		}

		// If it's air, make the item there.

		if (p.getInventory().getItem(configslot).getType() == Material.AIR) {
			p.getInventory().setItem(configslot, item);
			return;
		}

		// Doesn't have an item, inventory isn't full, and it's not air, add to
		// inventory.
		System.out.println(
				Utils.chat("&cThat slot is already taken for that player, putting item in first open slot..."));
		p.getInventory().setItem(slot, item);
		return;

	}
}
