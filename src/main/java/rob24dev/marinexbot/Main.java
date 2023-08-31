package rob24dev.marinexbot;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.bukkit.plugin.java.JavaPlugin;
import rob24dev.marinexbot.listeners.JoinLeaveListeners;
import rob24dev.marinexbot.listeners.Listeners;
import rob24dev.marinexbot.listeners.TicketListeners;

public final class Main extends JavaPlugin {


    public static JDA jda;

    private static Main instance;


    public Main() {
        instance = this;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        try {
            jda = JDABuilder.createDefault(getToken())
                    .setActivity(Activity.playing("na serveru Marinex"))
                    .enableIntents(GatewayIntent.GUILD_MEMBERS)
                    .setMemberCachePolicy(MemberCachePolicy.ALL)
                    .addEventListeners(new Listeners(), new JoinLeaveListeners(), new TicketListeners())
                    .build().awaitReady();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        jda.upsertCommand("createticket", "Create Ticket Command").queue();
        jda.upsertCommand("resetdata", "Pro resetování dat bota").queue();
        jda.upsertCommand("say", "Příkaz pro odesílání zpráv za bota").addOption(OptionType.STRING, "message", "Zde napiš zprávu kterou chceš poslat :)", true).complete();
        jda.upsertCommand("ip", "Příkaz pro zjištění IP na Marinex").complete();
        jda.upsertCommand("roleadd", "Příkaz pro přidání role dle výběru").addOption(OptionType.USER, "target", "Zde vyber člena kterému chceš dát roli", true).addOption(OptionType.ROLE, "role", "Zde vyber roli", true).complete();
        jda.upsertCommand("roleremove", "Příkaz pro odstranění role dle výběru").addOption(OptionType.USER, "target", "Zde vyber člena kterému chceš odebrat roli", true).addOption(OptionType.ROLE, "role", "Zde vyber roli", true).complete();
        jda.upsertCommand("oznameni", "Příkaz pro oznámení").addOption(OptionType.STRING, "message", "Zde napiš zprávu kterou chceš dát do oznámení", true).addOption(OptionType.BOOLEAN, "ping", "Zde vyber jestli oznámení má mít ping", true).complete();
        jda.upsertCommand("anketa", "Příkaz pro anketu").addOption(OptionType.STRING, "question", "Zde napiš otázku do ankety", true).complete();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Listeners.possibilitiesList.clear();
        Listeners.question = null;
        Listeners.inEditMode = false;
    }

    public static Main getInstance() {
        return instance;
    }

    private String getToken() {
        Dotenv dotenv = Dotenv.load();
        return dotenv.get("TOKEN");
    }
}
