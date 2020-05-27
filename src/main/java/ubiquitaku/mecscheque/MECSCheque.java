package ubiquitaku.mecscheque;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public final class MECSCheque extends JavaPlugin {
    VaultManager vault;
    String plname = "MECSCheque";

    @Override
    public void onEnable() {
        // Plugin startup logic
        vault = new VaultManager(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equals("mcheque")) {
            if (!(sender instanceof Player)) {
                if (args.length != 2) {
                    System.out.println("/mcheque <プレイヤー名> <金額> : 金を消費せずに小切手を作成します(金を合法的に増殖させます)");
                return true;
                }
                boolean res = true;
                for (int i = 0; i < args[1].length(); i++) {
                    if (Character.isDigit(args[0].charAt(i))) {
                        continue;
                    } else {
                        res = false;
                        break;
                    }
                }
                if (res) {
                    Player x = getServer().getPlayer(args[0]);
                    if (x != null) {
                        ItemStack item = new ItemStack(Material.STONE);
                        ItemMeta meta = item.getItemMeta();
                        meta.setLore(Arrays.asList(x.getName(), args[0]));//Loreセット
                        meta.setDisplayName("小切手");//アイテム名セット
                        item.setItemMeta(meta);
                        x.getInventory().addItem(item);//アイテムを渡す
                        System.out.println(plname + args[0] + " " + args[1] + "円の小切手を作成しました");
                        System.out.println(plname + "小切手作成" + x + ":" + args[0]);
                    }
                }
            } else {
                Player p = (Player) sender;
                if (args.length == 0){
                    sender.sendMessage(plname);
                    sender.sendMessage("/mcheque <金額> : 小切手作成");
                    sender.sendMessage("/mcheque o : 小切手開封");
                } else {
                    boolean result = true;
                    for (int i = 0; i < args[0].length(); i++) {
                        if (Character.isDigit(args[0].charAt(i))) {
                            continue;
                        } else {
                            result = false;
                            break;
                        }
                    }
                    if (result == true) {
                        Player y = (Player) sender;
                        if (vault.getBalance(y.getUniqueId()) < Integer.parseInt(args[0])) {
                            sender.sendMessage("指定した金額を所持していないため小切手の作成はできません");
                            return true;
                        }
                        vault.withdraw(y.getUniqueId(),Integer.parseInt(args[0]));
                        ItemStack item = new ItemStack(Material.STONE);
                        ItemMeta meta = item.getItemMeta();
                        meta.setLore(Arrays.asList(p.getName(),args[0]));//Loreセット
                        meta.setDisplayName("小切手");//アイテム名セット
                        item.setItemMeta(meta);
                        p.getInventory().addItem(item);//アイテムを渡す
                        sender.sendMessage(plname + args[0] + "円の小切手を作成しました");
                        System.out.println(plname + "小切手作成" + p + ":" + args[0]);
                    } else if (args[0].equals("o")) {
                        ItemStack item = p.getInventory().getItemInMainHand();
                        if (item.getType() == Material.STONE) {
                            try {
                                String lore = item.getLore().get(1);
                                boolean result_2 = true;
                                for (int i = 0; i < lore.length(); i++) {
                                    if (Character.isDigit(lore.charAt(i))) {
                                        continue;
                                    } else {
                                        result_2 = false;
                                        break;
                                    }
                                }
                                if (result_2 == true) {
                                    //小切手を換金する処理
                                    Player z = (Player) sender;
                                    p.getInventory().getItemInMainHand().setAmount(p.getInventory().getItemInMainHand().getAmount()-1);
                                    vault.deposit(z.getUniqueId(),Integer.parseInt(lore));
                                    p.sendMessage(plname + "小切手を換金しました");
                                    System.out.println(plname + "換金" + lore);
                                } else {
                                    p.sendMessage(plname + "ええかげんにせえよほんま" + lore);
                                }
                            } catch (NullPointerException lore) {
                                p.sendMessage(plname + "小切手と認識されませんでした");
                            }
                        } else {
                            p.sendMessage(plname + "そのアイテムは小切手ではありません");
                        }
                    } else{
                        sender.sendMessage(plname + "金額の部分に数字以外のものが含まれています");
                    }
                }
            }
        }
        return true;
    }
}
