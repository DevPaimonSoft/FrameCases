package ru.paimonsoft.framecaserecode.commands;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.HumanEntity;
import ru.paimonsoft.framecaserecode.other.CasesContainer;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TabCommands implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        switch(strings.length){
            case 1:
                return Arrays.asList("givekey", "setkey", "takekey", "setcase");
            case 2:
                if(stringCheck(strings[0])){
                    return Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList());
                }
                break;
            case 3:
                if(stringCheck(strings[0])){
                    return CasesContainer.cases();
                }
                break;
            case 4:
                if(stringCheck(strings[0])){
                    return Arrays.asList("1", "5", "10", "30", "50", "100");
                }
                break;
        }
        return null;
    }
    private boolean stringCheck(String onCheck){
        return (onCheck.equalsIgnoreCase("givekey")
                || onCheck.equalsIgnoreCase("setkey")
                || onCheck.equalsIgnoreCase("takekey"));
    }
}
