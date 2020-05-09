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

    @Override
    public void onEnable() {
        // Plugin startup logic
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equals("mcheque")) {
            if (!(sender instanceof Player)) {
                System.out.println("(今のところ)コンソールからの実行ができないコマンドです");
            } else {
                if (args.length == 0){
                    //args[0]が空白の時の処理
                    sender.sendMessage("/mcheque <金額> : 小切手作成");
                    sender.sendMessage("/mcheque o : 小切手開封");
                } else {
                    boolean result = true;
                    Player p = (Player) sender;
                    for (int i = 0; i < args[0].length(); i++) {
                        if (Character.isDigit(args[0].charAt(i))) {
                            continue;
                        } else {
                            result = false;
                            break;
                        }
                    }
                    if (result == true) {
                        ItemStack item = new ItemStack(Material.STONE);
                        ItemMeta meta = item.getItemMeta();
                        meta.setLore(Arrays.asList(p.getName(),args[0]));//Loreセット
                        meta.setDisplayName("小切手");//アイテム名セット
                        item.setItemMeta(meta);
                        p.getInventory().addItem(item);//アイテムを渡す
                        sender.sendMessage(args[0] + "円の小切手を作成しました");
                        System.out.println("小切手作成" + p + ":" + args[0]);
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
                                    p.sendMessage("小切手を換金しました");
                                    System.out.println("換金" + lore);
                                } else {
                                    p.sendMessage("ええかげんにせえよほんま" + lore);
                                }
                            } catch (NullPointerException lore) {
                                p.sendMessage("小切手と認識されませんでした");
                            }
                        } else {
                            p.sendMessage("そのアイテムは小切手ではありません");
                        }
                    } else{
                        sender.sendMessage("金額の部分に数字以外のものが含まれています");
                    }
                }
            }
        }
        return true;
    }
}
