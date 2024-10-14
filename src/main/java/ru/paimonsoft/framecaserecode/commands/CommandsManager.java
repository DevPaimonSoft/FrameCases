package ru.paimonsoft.framecaserecode.commands;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.paimonsoft.framecaserecode.FrameCasesRecode;
import ru.paimonsoft.framecaserecode.events.SetCase;
import ru.paimonsoft.framecaserecode.other.CasesContainer;
import ru.paimonsoft.framecaserecode.other.Coloriser;
import ru.paimonsoft.framecaserecode.other.ConfigManager;

public class CommandsManager extends ConfigManager implements CommandExecutor {

    private final FrameCasesRecode instance = FrameCasesRecode.getInstance();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 0) {
            if (!commandSender.hasPermission("framecases.cases")) {
                commandSender.sendMessage(Coloriser.colorify(getString("messages.no-perms")));
                return true;
            }
            commandSender.sendMessage(Coloriser.colorify("&7[&6FrameCasesRecode&7] &7- Кейсы, версия плагина: &6v" + instance.getDescription().getVersion()));
            commandSender.sendMessage(Coloriser.colorify("&7Комманды:"));
            commandSender.sendMessage(Coloriser.colorify(" &6/" + s + " setcase &7- Установить позицию для открытия кейсов."));
            commandSender.sendMessage(Coloriser.colorify(" &6/" + s + " givekey [игрок] [кейс] [кол-во] &7- Выдать кейсы игроку"));
            commandSender.sendMessage(Coloriser.colorify(" &6/" + s + " takekey [игрок] [кейс] [кол-во] &7- Забрать кейсы у игрока"));
            commandSender.sendMessage(Coloriser.colorify(" &6/" + s + " setkey [игрок] [кейс] [кол-во] &7- Установить кейсы игроку"));
            commandSender.sendMessage(Coloriser.colorify("&7"));
            commandSender.sendMessage(Coloriser.colorify(" &7Плагин поддерживается и обновляется разработчиками §6PaimonSoft Team§f, "));
            commandSender.sendMessage(Coloriser.colorify(" &7если вы заметили недоработку, или есть предложения, то:"));
            commandSender.sendMessage(Coloriser.colorify(" &7Группа ВК: §6vk.com/paimonsoft"));
            commandSender.sendMessage(Coloriser.colorify(" &7Discord: §6discord.paimonsoft.xyz"));
            commandSender.sendMessage(Coloriser.colorify("&7"));
            return true;
        }
        switch (strings[0]) {
            case "givekey": {
                if(isValidateCase(strings, commandSender)
                        && checks(strings, commandSender, "framecases.givekey", 4)){
                    return true;
                }
                CasesContainer.giveKey(strings[1], strings[2], Integer.parseInt(strings[3]));
                commandSender.sendMessage(Coloriser.colorify(getString("messages.give-key"))
                        .replace("%case%", strings[2].toUpperCase()).replace("%player%", strings[1]).replace("%amount%", strings[3]));
                return true;
            }
            case "takekey": {
                if(isValidateCase(strings, commandSender)
                        && checks(strings, commandSender, "framecases.takekey", 4)){
                    return true;
                } else if (!(CasesContainer.takeKey(strings[1], strings[2], Integer.parseInt(strings[3])))) {
                    commandSender.sendMessage(Coloriser.colorify(getString("messages.error-take-amount")));
                    return true;
                }
                commandSender.sendMessage(Coloriser.colorify(getString("messages.take-key"))
                        .replace("%case%", strings[2].toUpperCase()).replace("%player%", strings[1]).replace("%amount%", strings[3]));
                return true;
            }
            case "setkey": {
                if(isValidateCase(strings, commandSender)
                        && checks(strings, commandSender, "framecaseColoriser.colorifyetkey", 4)){
                    return true;
                }

                CasesContainer.setKey(strings[1], strings[2], Integer.parseInt(strings[3]));
                commandSender.sendMessage(Coloriser.colorify(getString("messageColoriser.colorifyet-key"))
                        .replace("%case%", strings[2].toUpperCase()).replace("%player%", strings[1]).replace("%amount%", strings[3]));
                return true;
            }
            case "setcase": {
                if (!(commandSender instanceof Player)) {
                    commandSender.sendMessage(Coloriser.colorify(getString("messages.error-instance")));
                    return true;
                }
                if(checks(strings, commandSender, "framecaseColoriser.colorifyetcase", 1)){
                    return true;
                }
                Player player = (Player) commandSender;
                SetCase.setCaseList.add(player);
                player.sendMessage(Coloriser.colorify(getString("messages.click-set-position")));
                return true;
            }
        }
        return false;
    }
    private boolean isValidateCase(String[] strings, CommandSender commandSender){
        if (!CasesContainer.isValidateCase(strings[2])) {
            commandSender.sendMessage(Coloriser.colorify(getString("messages.no-contains-case")));
            return true;
        } else {
            return false;
        }
    }
    private boolean checks(String[] strings, CommandSender commandSender, String perms, int length){
        if (strings.length != length) {
            commandSender.sendMessage(Coloriser.colorify(getString("messages.error-argument")));
            return true;
        } else if (!commandSender.hasPermission(perms)) {
            commandSender.sendMessage(Coloriser.colorify(getString("messages.no-perms")));
            return true;
        }
        return false;
    }
}
