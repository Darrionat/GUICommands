package me.Arcator.GUICommands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import me.Arcator.GUICommands.Commands.GUICommands;
import me.Arcator.GUICommands.Listeners.CommandProcess;
import me.Arcator.GUICommands.Listeners.InventoryClick;
import me.Arcator.GUICommands.Listeners.JoinandClickItem;
import me.Arcator.GUICommands.bStats.Metrics;

public class Main extends JavaPlugin {

	private static JavaPlugin instance;

	public void onEnable() {
		String name = "[" + this.getName() + "]";

		System.out.println(name + " Initializing the GUI");
		Main.initialize(this);
		System.out.println(name + " GUI initialized.");

		System.out.println(name + " Registering classes.");
		new InventoryClick(this);
		new CommandProcess(this);
		new JoinandClickItem(this);
		new GUICommands(this);
		System.out.println(name + " Classes registered.");

		System.out.println(name + " Setting up bungee channel.");
		// Bungee things!
		Main.instance = this;
		BungeecordUtil.setupUtil();
		System.out.println(name + " Bungee setup complete. If you aren't running bungee, ignore this.");

		System.out.println(name + " Saving config...");
		saveDefaultConfig();
		System.out.println(name + " Config saved.");

		System.out.println(name + " Setting up bStats.");
		Metrics metrics = new Metrics(this);
		metrics.addCustomChart(new Metrics.SingleLineChart("players", () -> Bukkit.getOnlinePlayers().size()));
		System.out.println(name + " bStats complete.");

		System.out.println(name + " Plugin successfully loaded!");

	}

	public void onDisable() {
		String name = "[" + this.getName() + "]";
		System.out.println(name + " Disabling Bungee channel");
		BungeecordUtil.disableUtil();
		System.out.println(name + " Bungee channel disabled");
		System.out.println(name + " Plugin disabled successfully");
	}

	public static Main getInstance() {
		return (Main) instance;
	}
	// GUI Start

	public static Inventory inv;
	public static String inventory_name;
	public static int inv_rows;

	public static void initialize(JavaPlugin plugin) {
		inventory_name = Utils.chat(plugin.getConfig().getString("GUIName"));
		inv_rows = plugin.getConfig().getInt("GUIRows") * 9;
		inv = Bukkit.createInventory(null, inv_rows);
	}

	public static Inventory GUI(Player p, JavaPlugin plugin) {
		Inventory toReturn = Bukkit.createInventory(null, inv_rows, inventory_name);
		ConfigurationSection gui = plugin.getConfig().getConfigurationSection("GUI");

		for (String key : gui.getKeys(false)) {
			Integer i = Integer.valueOf(key);
			if (i == null || i < 0 || i > 54)
				continue;
			ConfigurationSection section = gui.getConfigurationSection(key);

			// Want it to run when a number through 1-54 is not in the keys.
			for (int slot = 1; slot <= inv_rows; slot++) {
				String slotnum = String.valueOf(slot);
				if (gui.getKeys(false).contains(slotnum)) {
					continue;
				}
				String fillId = plugin.getConfig().getString("FillItemType");
				int fillByte = plugin.getConfig().getInt("FillItemByteId");

				// If it's air, don't put anything.
				if (fillId.equalsIgnoreCase("AIR")) {
					break;
				}

				if (fillByte != 0) {
					Utils.createItemByte(inv, fillId, fillByte, 1, slot, "");
					continue;
				}
				if (fillByte == 0) {
					Utils.createItem(inv, fillId, 1, slot, "");
					continue;
				}

			}
			String id = section.getString("id");
			int qty = section.getInt("qty");
			String name = section.getString("name");
			String lore = section.getString("lore");
			String viewpermission = section.getString("view-permission");
			int byteid = section.getInt("byteid");

			if (i == 0 || i > inv_rows) {
				p.sendMessage(Utils.chat("&cSlot " + String.valueOf(i) + " is invalid."));
				p.sendMessage(Utils.chat("&cIs it between 1 and " + String.valueOf(inv_rows)));
			}

			if (Material.getMaterial(id) == null) {
				p.sendMessage(Utils.chat("&4The ID for slot " + String.valueOf(i) + " is invalid."));
				p.sendMessage(Utils.chat("&cAre you using a correct block type?"));
				p.sendMessage(Utils.chat("&cAre you using a all uppercase?"));
				p.sendMessage(Utils.chat("&cExample-  id: 'DIAMOND_ORE'"));
				p.sendMessage(Utils.chat("&cFix your config and reload!"));
			}

			if (viewpermission.equalsIgnoreCase("")) {
				if (byteid != 0) {
					Utils.createItemByte(inv, id, byteid, qty, i, name, lore);
					continue;
				}
				Utils.createItem(inv, id, qty, i, name, lore);
			} else {
				if (p.hasPermission(viewpermission)) {
					if (byteid != 0) {
						Utils.createItemByte(inv, id, byteid, qty, i, name, lore);
						continue;
					}
					Utils.createItem(inv, id, qty, i, name, lore);
				}
			}
			// }

		}
		toReturn.setContents(inv.getContents());
		return toReturn;
	}

	public static void clicked(Player p, int slot, ItemStack clicked, Inventory inv, Plugin plugin) {
		if (clicked == null) {
			return;
		}
		ConfigurationSection gui = plugin.getConfig().getConfigurationSection("GUI");
		for (String key : gui.getKeys(false)) {
			Integer i = Integer.valueOf(key);
			if (i == null || i < 0 || i > 54)
				continue;
			ConfigurationSection section = gui.getConfigurationSection(key);
			if (section == null) {
				continue;
			}

			// ConfigurationSection GUIi = gui.getConfigurationSection(String.valueOf(i));
			Boolean keepopen = plugin.getConfig().getBoolean("KeepOpen");
			String clickpermission = section.getString("click-permission");
			String nopermmessage = section.getString("click-no-permission-message");
			String command = section.getString("command");
			String itemname = Utils.chat(section.getString("name").replace("&f", ""));
			String clickname = Utils.chat(clicked.getItemMeta().getDisplayName());
			if (clickname.equalsIgnoreCase(itemname)) {
				if (clickpermission.equalsIgnoreCase("")) {
					if (command.contains("server")) {
						String[] args = command.split(" ");
						BungeecordUtil.sendPlayerToServer(p, args[1]);
					} else {
						p.chat("/" + command); // p.performCommand doesn't work with custom command in 1.12
					}
					if (keepopen == false) {
						p.closeInventory();
						return;
					}
					return;
				}
				if (p.hasPermission(clickpermission)) {
					if (command.contains("server")) {
						String[] args = command.split(" ");
						BungeecordUtil.sendPlayerToServer(p, args[1]);
					} else {
						p.chat("/" + command); // p.performCommand doesn't work with custom command in 1.12
					}
					if (keepopen == false) {
						p.closeInventory();
						return;
					}
				} else {
					p.sendMessage(Utils.chat(nopermmessage));
					return;
				}
			}
		}
	}

	// Gui end

}
