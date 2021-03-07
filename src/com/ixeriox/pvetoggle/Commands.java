package com.ixeriox.pvetoggle;


import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;



public class Commands implements CommandExecutor {
	private static PVEToggle PVEToggle;

	public Commands(com.ixeriox.pvetoggle.PVEToggle PVEToggle) {
		Commands.PVEToggle = PVEToggle; // Store the plugin in situations where you need it.
	}

	public boolean onCommand(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args) {
		String Command = cmd.getName();
			switch( Command ) {
			case "pve":
				
				if (args.length == 0) {
					String status = this.getCurrentStatus(sender.getName());
					String l = conf("PVEToggle.getNullMsg");
					boolean gogo = true;
					if(l.equalsIgnoreCase("null")) {
						gogo = false;
					} else {
						gogo = true;
					}
					if(conf("PVEToggle.getNullMsg") != null && gogo) {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&',conf("PVEToggle.getNullMsg").replace("{status}", status)));
					} else {
					isNull("getNullMsg");
					sender.sendMessage("[PVEToggle] - Current status: "+status);
					sender.sendMessage("[PVEToggle] - To enable/disable your PVE status! Use '/pve toggle'");
					}
					return false;
				} else {
				switch(args[0]) {
				case "reload":
					if(sender.hasPermission(conf("PVEToggle.permission_toreload"))) {
						PVEToggle.reloadConfig();
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', conf("PVEToggle.reloadmsg")));		
					return true;
					} else {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', conf("PVEToggle.no_perms")));	
					return true;
					}
				case "admin":
					if(!sender.hasPermission(conf("PVEToggle.permission_foradmin"))) {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', conf("PVEToggle.no_perms")));	
						return false;
					}
					if (args.length == 1) {
						if(conf("PVEToggle.getCustomAdminMsg") != null) {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&',conf("PVEToggle.CustomAdminMsg")));
							return false;
						} else {
							isNull("getCustomAdminMsg");
							sender.sendMessage(ChatColor.AQUA + " ---------------------------------------------------- ");
							sender.sendMessage("[PVE-Toggle][ADMIN] You can use this feature using the below commands:");
							sender.sendMessage("/pve admin allowdamage TRUE/FALSE");
							sender.sendMessage("/pve admin toggle PLAYER");
							sender.sendMessage("/pve admin check PLAYER");
							sender.sendMessage("/pve admin mob-control ENTITY TRUE/FALSE");
							sender.sendMessage("/pve admin disable-world WORLD(Optional)");
							sender.sendMessage("/pve admin enable-world WORLD(Optional)");
							sender.sendMessage("/pve admin include-pvp TRUE/FALSE");
							sender.sendMessage(ChatColor.AQUA + " ---------------------------------------------------- ");
							return false;
						}
					} else {
						
						if (args.length == 2) {
								if(args[1].equalsIgnoreCase("disable-world") != true && args[1].equalsIgnoreCase("enable-world") != true) {
							if(conf("PVEToggle.RequiresMoreArguments") != null) {
								sender.sendMessage(ChatColor.translateAlternateColorCodes('&',conf("PVEToggle.RequiresMoreArguments")));
								return false;
							} else {
								
								isNull("RequiresMoreArguments");
								sender.sendMessage(ChatColor.RED + "[PVE-Toggle] Sadly "+args[1]+" requires more arguments.");
								return false;
							}
						} 
						}
						switch(args[1]) {
						case "allowdamage":
								String ed;
								if(args[2].toLowerCase().equalsIgnoreCase("true")) {
									ed = "Enabled";
								} else {
									ed = "Disabled";
								}
								if(args[2].toLowerCase().equalsIgnoreCase("true") || args[2].toLowerCase().equalsIgnoreCase("false")) {
								if(conf("PVEToggle.allowDmgMessage") != null) {
								PVEToggle.getConfig().set("PVEToggle.stopmobdmg",Boolean.parseBoolean(args[2]));
								PVEToggle.saveConfig();
								sender.sendMessage(ChatColor.translateAlternateColorCodes('&', conf("PVEToggle.allowDmgMessage")).replace("{value}", ed));
								} else {
									isNull("allowDmgMessage");
									sender.sendMessage(ChatColor.DARK_GREEN + "[PVE-Toggle][CONFIG] Mob Damage has been: "+ed);
								}
								} else {
								if(conf("PVEToggle.RequirementMsg") != null) {
								sender.sendMessage(ChatColor.translateAlternateColorCodes('&', conf("PVEToggle.RequirementMsg")).replace("{requirement}", "True/False").replace("{current}",  args[2]));
								
								} else {
								isNull("RequirementMsg");
								sender.sendMessage(ChatColor.DARK_RED + "[PVE-Toggle] Unfortunately; You didn't meet the argument requirements - You need: True/false || You entered: "+args[2]);
								}
								}

							return true;
						case "toggle":

							Player playerName = Bukkit.getPlayer(args[2]);
							if(playerName instanceof Player) {
								//Continue!
							/*} else {
							if(conf("PVEToggle.admin_notfoundmsg") == null) {
								sender.sendMessage(ChatColor.DARK_RED  + "[PVE-Toggle] "+args[2]+" was not found!");
							} else {
								sender.sendMessage(conf("PVEToggle.admin_notfoundmsg").replace("{user}",args[2])+"2");

							}
							return false;*/
							} else {
								if(PVEToggle.isDebugMode()) {
									System.out.println("[PVE-Toggle][DEBUG] Player was not in instance! Stopping command!");
								}
								return false;
							}
							UUID UUID = playerName.getUniqueId();
							if(Bukkit.getPlayer(UUID) == null) { 
								System.out.println("[PVE-Toggle] Critical error: Player/UUID was null.");
								return false;
							}
							playerName = Bukkit.getPlayer(UUID);
							String on_or_off = getCurrentStatus(playerName.getName());
							if(on_or_off == null) { System.out.println("[PVE-Toggle][ERROR] - Selector was still null, contact the dev!"); return false; }
						switch( on_or_off ) {
							case "on":
								on_or_off = "off";
							break;
							case "off":
								on_or_off = "on";
							break;
							case "":
							default:
								on_or_off = "off";
							break;
							}
							saveTheConfig(playerName.getName(), on_or_off);
							String Togglemsg = conf("PVEToggle.admin_togglemsg");
							if(Togglemsg == null) {
								System.out.println("[PVEToggle][ERROR] - You need to modify the configuration properly!");
								Togglemsg = "&B"+playerName.getName() + " is now  "+on_or_off.toLowerCase().replace("on", conf("PVEToggle.customStatusOn")).replace("off", conf("PVEToggle.customStatusOff"));

							} else {
								Togglemsg = conf("PVEToggle.admin_togglemsg");
							}
							if(playerName.getName() != sender.getName()) {
								if(args.length > 3) {
								if(args[3].equalsIgnoreCase("true")) {
									if(conf("PVEToggle.admin_toggled") != null) {
									playerName.sendMessage(ChatColor.translateAlternateColorCodes('&', conf("PVEToggle.admin_toggled").replace("{user}", args[2]).replace("{status}",  on_or_off.toLowerCase().replace("on", conf("PVEToggle.customStatusOn")).replace("off", conf("PVEToggle.customStatusOff")))));
									} else {
										playerName.sendMessage(ChatColor.AQUA + "[PVE-Toggle] "+args[2]+"'s status is now "+on_or_off.toLowerCase().replace("on", conf("PVEToggle.customStatusOn")).replace("off", conf("PVEToggle.customStatusOff")));
									}
									}
								}
							} else {
								if(conf("PVEToggle.togglemsg") != null) {
									sender.sendMessage(ChatColor.translateAlternateColorCodes('&', conf("PVEToggle.togglemsg")).replace("{status}", on_or_off.toLowerCase().replace("on", conf("PVEToggle.customStatusOn")).replace("off", conf("PVEToggle.customStatusOff"))));			
								} else {
									sender.sendMessage(ChatColor.AQUA + "Your PVE status is now: "+on_or_off.toLowerCase().replace("on", conf("PVEToggle.customStatusOn")).replace("off", conf("PVEToggle.customStatusOff")));
								}
							return false;
							}
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Togglemsg.replace("{user}", args[2]).replace("{status}",  on_or_off.toLowerCase().replace("on", conf("PVEToggle.customStatusOn")).replace("off", conf("PVEToggle.customStatusOff")))));
						return true;
						case "check":
							Player playerNameCheck = Bukkit.getPlayer(args[2]);
							if(playerNameCheck instanceof Player) {
								//Continue!
							/*} else {
							if(conf("PVEToggle.admin_notfoundmsg") == null) {
								sender.sendMessage(ChatColor.DARK_RED  + "[PVE-Toggle] "+args[2]+" was not found!");
							} else {
								sender.sendMessage(conf("PVEToggle.admin_notfoundmsg").replace("{user}",args[2])+"2");

							}
							return false;*/
							} else {
								if(PVEToggle.isDebugMode()) {
									System.out.println("[PVE-Toggle][DEBUG] Player was not in instance! Stopping command!");
								}
								return false;
							}
							UUID UUIDCheck = playerNameCheck.getUniqueId();
							if(Bukkit.getPlayer(UUIDCheck) == null) { 
								System.out.println("[PVE-Toggle] Critical error: Player/UUID was null.");
								return false;
							}
							playerName = Bukkit.getPlayer(UUIDCheck);
							String status;
							if(conf("PVEToggle.users."+playerName.getName()+".status") != null) {
							status = conf("PVEToggle.users."+playerName.getName()+".status");
							} else {
							status = "off";
							}
							if(conf("PVEToggle.userStatusMsg") != null) {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', conf("PVEToggle.userStatusMsg")).replace("{user}",  playerName.getName()).replace("{status}", status.toLowerCase().replace("on", conf("PVEToggle.customStatusOn")).replace("off", conf("PVEToggle.customStatusOff"))));
							} else {
								sender.sendMessage(ChatColor.AQUA + "[PVE-Toggle] "+playerName.getName()+ "'s PVE status is currently "+status+".");
							}
							return false;
						case "mob-control":
							return false;
						case "disable-world":
							if(args.length == 2) {
								PVEToggle.getConfig().set("PVEToggle.worlds."+Bukkit.getPlayer(sender.getName()).getWorld().getName()+".disabled", true);
								PVEToggle.saveConfig();
								if(conf("PVEToggle.admin_worldstatuschangemsg") != null) {
									sender.sendMessage(ChatColor.translateAlternateColorCodes('&', conf("PVEToggle.admin_worldstatuschangemsg")).replace("{status}", "enabled").replace("{world}", Bukkit.getPlayer(sender.getName()).getWorld().getName() ));									
								} else {
									sender.sendMessage("[PVE-Toggle] Now fully disabled for World: "+Bukkit.getPlayer(sender.getName()).getWorld().getName());

								}
							}
							if(PVEToggle.isDebugMode()) {
							sender.sendMessage(" " + args.length);
							}
							return false;
						case "enable-world":
							if(args.length == 2) {
								PVEToggle.getConfig().set("PVEToggle.worlds."+Bukkit.getPlayer(sender.getName()).getWorld().getName()+".disabled", false);
								PVEToggle.saveConfig();
								if(conf("PVEToggle.admin_worldstatuschangemsg") != null) {
									sender.sendMessage(ChatColor.translateAlternateColorCodes('&', conf("PVEToggle.admin_worldstatuschangemsg")).replace("{status}", "enabled").replace("{world}", Bukkit.getPlayer(sender.getName()).getWorld().getName() ));
								} else {
									sender.sendMessage("[PVE-Toggle] Now fully enabled for World: "+Bukkit.getPlayer(sender.getName()).getWorld().getName());
								}
							}
							if(PVEToggle.isDebugMode()) {
							sender.sendMessage(" " + args.length);
							}
							return false;
						case "include-pvp":
							String which;
							String not;
							if(args[2].toLowerCase().equalsIgnoreCase("true") || args[2].toLowerCase().equalsIgnoreCase("false")) {
								PVEToggle.getConfig().set("PVEToggle.IncludePVP", Boolean.parseBoolean(args[2]));
								PVEToggle.saveConfig();
								if(args[2].toLowerCase().equalsIgnoreCase("true")) {
									which = "included with";
									not = "";
								} else {
									which = "excluded from";
									not = "not ";
								}
							} else {
								return false;
							}
							sender.sendMessage("[PVE-Toggle] PVP has been "+which+" the PVE settings. PVE Status will "+not+"effect PVP interaction!");
							
					}
						return false;
					}
				case "toggle":
					String on_or_off = getCurrentStatus(sender.getName());
					if(on_or_off == null) { System.out.println("[PVE-Toggle][ERROR] - Selector was still null, contact the dev!"); return false; }
				switch( on_or_off ) {
					case "on":
						on_or_off = "off";
					break;
					case "off":
						on_or_off = "on";
					break;
					case "":
					default:
						on_or_off = "off";
					break;
					}
					saveTheConfig(sender.getName(), on_or_off);
					String Togglemsg = conf("PVEToggle.togglemsg");
					if(Togglemsg == null) {
						System.out.println("[PVEToggle][ERROR] - You need to modify the configuration properly!");
						return true;
					} else {
						Togglemsg = conf("PVEToggle.togglemsg");
					}
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Togglemsg.replace("{status}", on_or_off.toLowerCase().replace("off", conf("PVEToggle.customStatusOff")).replace("on", conf("PVEToggle.customStatusOn")))));
				return true;
					default:
						if(PVEToggle.isDebugMode()) {
							System.out.println("[DEBUG] No such command exists! "+args[0]);
						}
				return false;
					
				}
				}
			}
			return false;
			}
	


			/*
			 * 					switch(args[1]) {
					case "toggle":
						String on_or_off = getCurrentStatus(sender.getName());
						if(on_or_off == null) { System.out.println("[PVE-Toggle][ERROR] - Selector was still null, contact the dev!"); return false; }
					switch( on_or_off ) {
						case "on":
							on_or_off = "off";
						break;
						case "off":
							on_or_off = "on";
						break;
						case "":
						default:
							on_or_off = "on";
						break;
						}
						saveTheConfig(sender.getName(), on_or_off);
						String Togglemsg = conf("PVEToggle.togglemsg");
						if(Togglemsg == null) {
							System.out.println("[PVEToggle][ERROR] - You need to modify the configuration properly!");
							return true;
						} else {
							Togglemsg = conf("PVEToggle.togglemsg");
						}
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Togglemsg.replace("{status}", on_or_off)));
						break;
					case "":
						String status = this.getCurrentStatus(sender.getName());
						if(conf("PVEToggle.getNullMsg") != null) {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&',conf("PVEToggle.getNullMsg").replace("{status}", status)));
						} else {
						sender.sendMessage("[PVEToggle] - Current status: "+status);
						sender.sendMessage("[PVEToggle] - To enable/disable your PVE status! Use '/pve toggle'");
						}
						break;
						default:
							if(PVEToggle.isDebugMode()) {
								System.out.println("[DEBUG] No such command exists! "+args[0]);
							}
							break;
							
					}
					
						return true;
			}
			return false;
		}
			 */
	private void isNull(String name) {
		if(PVEToggle.isDebugMode()) {
		System.out.println("[PVE-Toggle][DEBUG] "+name+" was null; Using default! Consider editing config!");
		}
	}
	private String getCurrentStatus(String name) {
		// TODO Auto-generated method stub
		String status = conf("PVEToggle.users."+name+".status");
		if(status == null || status.contentEquals("")) {

			if(PVEToggle.isDebugMode()) {
			System.out.println("[DEBUG] Cannot find the user '"+name+"' in the database. Creating!");
			}
			PVEToggle.getConfig().set("PVEToggle.users."+name+".status", "off");
			PVEToggle.saveConfig();
			if(PVEToggle.isDebugMode()) {
			System.out.println("[DEBUG] The status for '"+name+"' has been switched 'off' (Default)");
			}
			status = conf("PVEToggle.users."+name+".status");
			if(status == null) { 

				if(PVEToggle.isDebugMode()) {
				System.out.println("[DEBUG] The status for '"+name+"' was still null in the config. Sending 'off' this could be a bug!");
				}
				return "off";
			}
		}

		return status;
	}
	private String conf(String what){
		return PVEToggle.getConfig().getString(what);
	}
	private void saveTheConfig(String name, String on_or_off) {
		// TODO Auto-generated method stub

		if(PVEToggle.isDebugMode()) {
			System.out.println("[DEBUG] Saving '"+name+"' to the config with the status '"+on_or_off);
		}
		PVEToggle.getConfig().set("PVEToggle.users."+name+".status", on_or_off);
		PVEToggle.saveConfig();
	}

	
}
