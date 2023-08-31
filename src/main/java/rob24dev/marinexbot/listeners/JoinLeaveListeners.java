package rob24dev.marinexbot.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import rob24dev.marinexbot.Main;
import java.util.Objects;

public class JoinLeaveListeners extends ListenerAdapter {



    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        event.getGuild().findMembers(member -> member.getRoles().contains(Main.jda.getRoleById("1102959757733675129"))).onSuccess(members -> {
            TextChannel sendChannel = Main.jda.getTextChannelById("1052919349188513792");
            User user = event.getUser();
            EmbedBuilder join = new EmbedBuilder();
            join.setTitle("Marinex | Join");
            join.setImage(Objects.requireNonNull(user.getAvatar()).getUrl(100));
            join.setThumbnail("https://i.postimg.cc/qBFnjxHg/d94e1a54-5192-4f93-8d55-54c3d030e04c.png");
            join.setColor(0xd48600);
            if(event.getMember().getUser().isBot()) {
                join.setDescription("✋ Ahoj " + event.getUser().getEffectiveName() + ", vítej na serveru  **-|\uD835\uDC74\uD835\uDC82\uD835\uDC93\uD835\uDC8A\uD835\uDC8F\uD835\uDC86\uD835\uDC99|-** doufáme, že se ti tu bude líbit! \n Zatím nás tu je furt stejně, protože to je jen bot :sob:.");
            } else {
                join.setDescription("✋ Ahoj " + event.getUser().getEffectiveName() + ", vítej na serveru  **-|\uD835\uDC74\uD835\uDC82\uD835\uDC93\uD835\uDC8A\uD835\uDC8F\uD835\uDC86\uD835\uDC99|-** doufáme, že se ti tu bude líbit! \n Už nás tu je **" + members.size() + "**.");
            }
            Objects.requireNonNull(sendChannel).sendMessageEmbeds(join.build()).queue();
            sendChannel.sendMessage("<@" + event.getUser().getId() + ">").queue();
        });
    }


    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
        event.getGuild().findMembers(member -> member.getRoles().contains(Main.jda.getRoleById("1102959757733675129"))).onSuccess(members -> {
            TextChannel sendChannel = Main.jda.getTextChannelById("1052919349188513792");
            User user = event.getUser();
            EmbedBuilder quit = new EmbedBuilder();
            if(Objects.requireNonNull(event.getMember()).getUser().isBot()) {
                quit.setDescription("**" + event.getUser().getEffectiveName() + "**" + " se odpojil/a :sob:. \n Naštěstí to byl jen bot :sweat_smile:");
            } else {
                quit.setDescription("**" + event.getUser().getEffectiveName() + "**" + " se odpojil/a :sob:. \n Už nás tu je bohužel pouze **" +  members.size() + "**.");
            }
            quit.setTitle("Marinex | Leave");
            quit.setImage(Objects.requireNonNull(user.getAvatar()).getUrl(100));
            quit.setThumbnail("https://i.postimg.cc/qBFnjxHg/d94e1a54-5192-4f93-8d55-54c3d030e04c.png");
            quit.setColor(0xd48600);
            Objects.requireNonNull(sendChannel).sendMessageEmbeds(quit.build()).queue();
        });
    }
}
