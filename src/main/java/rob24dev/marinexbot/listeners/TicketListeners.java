package rob24dev.marinexbot.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.StandardGuildMessageChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import rob24dev.marinexbot.Main;

import java.util.Objects;

public class TicketListeners extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        Member member = event.getMember();
        if (!event.getName().equalsIgnoreCase("createticket")) return;
        if (event.getChannel().getId().equalsIgnoreCase("1052930870333411338")) {
            if (Objects.requireNonNull(member).getId().equalsIgnoreCase("989832842890579978") | (Objects.requireNonNull(member).getId().equalsIgnoreCase("1070980062050000926") | (Objects.requireNonNull(member).getId().equalsIgnoreCase("970328298875928676")))) {
                EmbedBuilder TicketMessage = new EmbedBuilder();
                TicketMessage.setTitle("Marinex | Tickets");
                TicketMessage.setDescription("Klikněte na tlačítko níže pro vytvoření ticket!\n" +
                        "\n" +
                        "• Mějte prosím trpělivost, na odpověď si __**vyhrazujeme až 24 hodin.**__\n" +
                        "\n" +
                        "**Pracovní doba**\n" +
                        "Po-Pa:\n" +
                        "15:00-22:00\n" +
                        "Sobota:\n" +
                        "10:00-21:00\n" +
                        "Neděle:\n" +
                        "10:00-20:00\n" +
                        "\n" +
                        "**Když není pracovní doba, neznamená to že neodepisujeme, jen můžete čekat déle na odezvu.**\n" +
                        "\n" +
                        "Nezakládejte tickety bez důvodu a určitě\n" +
                        "ne proto, že jste to chtěli jen zkusit!");
                TicketMessage.setFooter("\n \n Bot created by: Rob24Dev");
                TicketMessage.setColor(0xd48600);
                Button button = Button.success("MarinexTicketCreate", "Vytvořit ticket");
                event.getChannel().sendMessageEmbeds(TicketMessage.build()).addActionRow(button.withEmoji(Emoji.fromUnicode("U+1f4e9"))).queue();
                event.reply("Ticket byl úspěšně odeslán!").setEphemeral(true).queue();
            } else {
                EmbedBuilder noPermission = new EmbedBuilder();
                noPermission.setTitle("Marinex | Tickets");
                noPermission.setDescription("**Nemáš práva!**");
                noPermission.setColor(0xd48600);
                event.replyEmbeds(noPermission.build()).setEphemeral(true).queue();
            }
        } else {
            EmbedBuilder noChannel = new EmbedBuilder();
            noChannel.setTitle("Marinex | Tickets");
            noChannel.setDescription("**Tento příkaz musíš provést v kanálu ticket.**");
            noChannel.setColor(0xd48600);
            event.replyEmbeds(noChannel.build()).setEphemeral(true).queue();
        }
    }
    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        User user = event.getUser();
        String ticketBool = Main.getInstance().getConfig().getString("Data." + user.getId() + ".ticket");
        if (Objects.requireNonNull(event.getButton().getId()).equalsIgnoreCase("MarinexTicketCreate")) {
            if (ticketBool == null | Objects.equals(ticketBool, "false")) {
                Role role = Main.jda.getRoleById("1077481941294665868");
                if (!Objects.requireNonNull(event.getMember()).getRoles().contains(role)) {
                    EmbedBuilder creating = new EmbedBuilder();
                    creating.setTitle("Marinex | Tickets");
                    creating.setDescription("**Ticket se vytváří...**");
                    creating.setThumbnail("https://media.giphy.com/media/7fC26KyeUz7eAwozFH/giphy.gif");
                    creating.setColor(0xd48600);
                    event.replyEmbeds(creating.build()).setEphemeral(true).queue(message -> new BukkitRunnable() {
                        StandardGuildMessageChannel standardGuildMessageChannel;
                        int time = 3;

                        @Override
                        public void run() {
                            time -= 1;
                            if (time == 1) {
                                standardGuildMessageChannel = createTicket(user, event);
                            }
                            if (time == 0) {
                                standardGuildMessageChannel.upsertPermissionOverride(Objects.requireNonNull(event.getGuild()).getPublicRole()).deny(Permission.ALL_PERMISSIONS).complete();
                                standardGuildMessageChannel.upsertPermissionOverride(Objects.requireNonNull(event.getMember())).deny(Permission.ALL_CHANNEL_PERMISSIONS).grant(Permission.VIEW_CHANNEL).grant(Permission.MESSAGE_SEND).grant(Permission.MESSAGE_HISTORY).grant(Permission.MESSAGE_ATTACH_FILES).grant(Permission.MESSAGE_EMBED_LINKS).queue();
                                EmbedBuilder created = new EmbedBuilder();
                                created.setTitle("Marinex | Tickets");
                                created.setDescription("**Ticket byl vytvořen <#" + standardGuildMessageChannel.getId() + ">**");
                                created.setThumbnail("https://media.giphy.com/media/v1.Y2lkPTc5MGI3NjExZmF6Y2ZwanN2dmttYjc0ZHp0dzU2cmljaWs2ZnF3em41cXoxMHNuayZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9cw/91Mw3I0jwJDcxBg3NI/giphy.gif");
                                created.setColor(0xd48600);
                                message.editOriginalEmbeds(created.build()).queue();
                                cancel();
                            }
                        }
                    }.runTaskTimer(Main.getInstance(), 0, 20));
                } else {
                    EmbedBuilder at = new EmbedBuilder();
                    at.setTitle("Marinex | Tickets");
                    at.setDescription("**Ticket nemůžeš otevřít, protože jsi Člen AT.**");
                    at.setColor(0xd48600);
                    event.replyEmbeds(at.build()).setEphemeral(true).queue();
                }
            } else {
                EmbedBuilder already = new EmbedBuilder();
                already.setTitle("Marinex | Tickets");
                already.setDescription("**Již máš vytvoření ticket!**");
                already.setColor(0xd48600);
                event.replyEmbeds(already.build()).setEphemeral(true).queue();
            }
        } else {
            if (event.getButton().getId().equalsIgnoreCase("MarinexTicketClose")) {
                String userId = Main.getInstance().getConfig().getString("ChannelData." + event.getChannel().getId() + ".user");
                if (event.getUser().getId().equalsIgnoreCase(userId)) {
                    EmbedBuilder closeTicket = new EmbedBuilder();
                    closeTicket.setTitle("Marinex | Tickets");
                    closeTicket.setDescription("Děkujeme, že jsi použil náš **ticket systém.** \n Nebojte se nám napsat znova, pokud máte problém týkající se **našeho projektů.** \n Náš team je připraven **vám pomáhat <3** \n \n **Naše pracovní doba:** \n \n **▶**  Po-Pa: **15:00-19:00** \n **▶**  Sobota: **10:00-21:00** \n  **▶** Neděle: **10:00-20:00** \n \n **UPOZORNĚNÍ** \n Když není pracovní doba, neznamená to že neodepisujeme, jen můžete čekat déle na odezvu. \n \n **Bot created by: Rob24Dev**");
                    closeTicket.setThumbnail("https://i.postimg.cc/9QzWZtfm/tickets-png.webp");
                    closeTicket.setColor(0xd48600);
                    event.getChannel().delete().queue();
                    sendPrivateMessage(Objects.requireNonNull(event.getUser()), event.getChannel().asTextChannel(), closeTicket);
                } else {
                    EmbedBuilder closeTicket = new EmbedBuilder();
                    closeTicket.setTitle("Marinex | Tickets");
                    closeTicket.setDescription("Člen AT zavřel váš ticket. \n Nicméně děkujeme, že jsi použil náš **ticket systém.** \n Nebojte se nám napsat znova, pokud máte problém týkající se **našeho projektů.** \n \n Náš team je připraven **vám pomáhat <3** \n **Naše pracovní doba:** \n \n **▶**  Po-Pa: **15:00-19:00** \n **▶**  Sobota: **10:00-21:00** \n  **▶** Neděle: **10:00-20:00** \n \n **UPOZORNĚNÍ** \n Když není pracovní doba, neznamená to že neodepisujeme, jen můžete čekat déle na odezvu. \n \n **Bot created by: Rob24Dev**");
                    closeTicket.setThumbnail("https://i.postimg.cc/9QzWZtfm/tickets-png.webp");
                    closeTicket.setColor(0xd48600);
                    event.getChannel().delete().queue();
                    User createdUser = Objects.requireNonNull(event.getGuild()).getJDA().getUserById(Objects.requireNonNull(userId));
                    sendPrivateMessage(Objects.requireNonNull(createdUser), event.getChannel().asTextChannel(), closeTicket);
                }
            }
        }
    }
    private StandardGuildMessageChannel createTicket(User user, ButtonInteractionEvent event) {
        TextChannel ticket = Objects.requireNonNull(event.getGuild()).createTextChannel("ticket-" + user.getName()).setParent(Main.jda.getCategoryById("1052930789215567952")).complete();
        Main.getInstance().getConfig().set("Data." + user.getId() + ".ticket", "true");
        Main.getInstance().getConfig().set("ChannelData." + ticket.getId() + ".user", event.getUser().getId());
        Main.getInstance().getConfig().set("ChannelData." + ticket.getId() + ".ticketname", ticket.getName());
        Main.getInstance().getConfig().set("ChannelData." + ticket.getId() + ".readyticket", false);
        Main.getInstance().saveConfig();
        Main.getInstance().reloadConfig();
        EmbedBuilder select = new EmbedBuilder();
        select.setTitle("Marinex | Tickets");
        select.setDescription("**Vyber prosím kategorii ticketu.**");
        select.setColor(0xd48600);
        Button close = Button.success("MarinexTicketClose", "Close ticket");
        ticket.sendMessageEmbeds(select.build()).setComponents(ActionRow.of(getCategory()), ActionRow.of(close)).queue();
        new BukkitRunnable() {
            int time = 600;
            @Override
            public void run() {
                boolean readyTicket = Main.getInstance().getConfig().getBoolean("ChannelData." + ticket.getId() + ".readyticket");
                if(!readyTicket) {
                    time -= 1;
                    if(time == 0) {
                        Main.getInstance().getConfig().set("Data." + user.getId() + ".ticket", "false");
                        Main.getInstance().getConfig().set("ChannelData." + ticket.getId(), null);
                        Main.getInstance().saveConfig();
                        ticket.delete().queue();
                    }
                } else {
                    cancel();
                }
            }
        }.runTaskTimer(Main.getInstance(), 0,20);
        return ticket;
    }

    private StringSelectMenu getCategory() {
        return StringSelectMenu.create("Ticket Kategorie")
                .addOption("Nahlášení chyby/bugu", "Nahlášení chyby/bugu")
                .addOption("Žádost o unregister", "Žádost o unregister")
                .addOption("Žádost o převedení ranku", "Žádost o převedení ranku")
                .addOption("Nahlášení cheaterů/ostatních hříšníků", "Nahlášení cheaterů/ostatních hříšníků")
                .addOption("Žádost o unban/unmute", "Žádost o unban/unmute")
                .addOption("Žádost o spolupráci", "Žádost o spolupráci")
                .addOption("Ostatní", "Ostatní")
                .build();
    }
    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
        if (Objects.requireNonNull(event.getComponent().getId()).equals("Ticket Kategorie")) {
            Main.getInstance().getConfig().set("ChannelData." + event.getChannel().getId() + ".readyticket", true);
            Main.getInstance().saveConfig();
            Main.getInstance().reloadConfig();
            String problem;
            String helpField;
            EmbedBuilder main = new EmbedBuilder();
            main.setTitle("Marinex | Tickets");
            main.setDescription("Vítej <@" + event.getUser().getId() + ">" + ", zde popiš tvůj problém a náš **Admin-Team** ti pomůže.");
            for (int i = 0; i < event.getValues().size(); i++) {

                switch (event.getValues().get(i)) {

                    case "Nahlášení chyby/bugu":
                        problem = "Nahlášení chyby/bugu";
                        main.addField("Problém:", Objects.requireNonNull(problem), false);
                        helpField = "**Pro urychlení řešení tvého ticketu, postupujte podle tohoto vzoru:**  \n Bug/Chyba: (**Zde popište bug/chybu**) \n Fotka/Video: (**Pokud máte možnost, udělejte fotku, nebo video, co přesně jsi udělal před bugem/chybou**) \n ";
                        main.addField("", helpField, false);
                        break;

                    case "Žádost o unregister":
                        problem = "Žádost o unregister";
                        main.addField("Problém:", Objects.requireNonNull(problem), false);
                        helpField = "**Pro urychlení řešení tvého ticketu, postupujte podle tohoto vzoru:** \n Nick: (**Zde napište nick v mc**) \n Originální účet: **Ano/Ne** \n Důvod: (**Zde napište proč např zapomněl jsem heslo**)\n ";
                        main.addField("", helpField, false);
                        break;

                    case "Žádost o převedení ranku":
                        problem = "Žádost o převedení ranku";
                        main.addField("Problém:", Objects.requireNonNull(problem), false);
                        helpField = "**Pro urychlení řešení tvého ticketu, postupujte podle tohoto vzoru:** \n Starý Nick: (**Zde napište starý nick v mc**) \n Nový nick: (**Zde napište nový nick**) \n Důvod: (**Zde napište proč portřebuješ rank převést na nový nick**) \n Důkaz: (**Pošlete nějaký důkaz, že vlastníš nový, tak starý účet**) \n \n __**A-team dále rozhodne, jestli je důkaz dostatečný.**__ \n ";
                        main.addField("", helpField, false);
                        break;

                    case "Nahlášení cheaterů/ostatních hříšníků":
                        problem = "Nahlášení cheaterů/ostatních hříšníků";
                        main.addField("Problém:", Objects.requireNonNull(problem), false);
                        helpField = "**Pro urychlení řešení tvého ticketu, postupujte podle tohoto vzoru:** \n Nick hříšníka: (**Zde napište nick hříšníka**) Důvod: (**Zde napište důvod nahlášení hříšníka např cheatuje**) \n Důkaz: (**Pošlete nějaký důkaz, že se toho tento nick doopravdy dopustil hříchu fotka/video**) \n \n __**A-team dále rozhodne, jestli je důkaz dostatečný.**__ \n ";
                        main.addField("", helpField, false);
                        break;

                    case "Žádost o unban/unmute":
                        problem = "Žádost o unban/unmute";
                        main.addField("Problém:", Objects.requireNonNull(problem), false);
                        helpField = "**Pro urychlení řešení tvého ticketu, postupujte podle tohoto vzoru:** \n Uživatel: (**Zde napište, jaký uživatel žádá o unban,unmute...**) Místo: **MC/Discord**  \n Důkaz: (**Napište proč je ban,mute... neprávem, nebo pošli video/fotku proč je neprávem**) \n \n __**A-team dále rozhodne, jestli je důkaz dostatečný.**__  \n \n __**Pokud jde o cheatovaní, bude vám pravděpodobně prohledán PC, doporučujeme stáhnout program anydesk https://anydesk.com/en/downloads/windows**__ \n ";
                        main.addField("", helpField, false);
                        break;

                    case "Žádost o spolupráci":
                        problem = "Žádost o spolupráci";
                        main.addField("Problém:", Objects.requireNonNull(problem), false);
                        helpField = "**Pro urychlení řešení tvého ticketu, postupujte podle tohoto vzoru:** Přečetl/a jsem si podmínky: Ano/Ne (**pokud ne běžte si je přečíst.**) \n Popis: (**Zde rozepište o co by přesně šlo**";
                        main.addField("", helpField, false);
                        break;
                    case "Ostatní":
                        problem = "Ostatní";
                        main.addField("Problém:", Objects.requireNonNull(problem), false);
                        break;
                    default:
                        break;
                }
                event.getMessage().delete().queue();
                main.setFooter("\n \n UPOZORNĚNÍ \n Na odepsání si vyhrazujeme až 24 hodin. \n \n Děkujeme že používáš náš ticket system.");
                main.setThumbnail("https://i.postimg.cc/9QzWZtfm/tickets-png.webp");
                main.setColor(0xd48600);
                Button close = Button.success("MarinexTicketClose", "Close ticket");
                event.getChannel().sendMessageEmbeds(main.build()).addActionRow(close.withEmoji(Emoji.fromUnicode("U+274C"))).queue();
                event.getChannel().asTextChannel().upsertPermissionOverride(Objects.requireNonNull(Objects.requireNonNull(event.getGuild()).getRoleById("1077581008200806440"))).grant(Permission.getPermissions(Permission.ALL_CHANNEL_PERMISSIONS)).deny(Permission.MANAGE_PERMISSIONS).deny(Permission.MANAGE_CHANNEL).deny(Permission.MANAGE_WEBHOOKS).deny(Permission.CREATE_INSTANT_INVITE).queue();
                event.getChannel().asTextChannel().upsertPermissionOverride(Objects.requireNonNull(Objects.requireNonNull(event.getGuild()).getRoleById("1052934928590979072"))).grant(Permission.getPermissions(Permission.ALL_CHANNEL_PERMISSIONS)).deny(Permission.MANAGE_PERMISSIONS).deny(Permission.MANAGE_CHANNEL).deny(Permission.MANAGE_WEBHOOKS).deny(Permission.CREATE_INSTANT_INVITE).queue();
                event.getChannel().asTextChannel().upsertPermissionOverride(Objects.requireNonNull(Objects.requireNonNull(event.getGuild()).getRoleById("1077581087200522260"))).deny(Permission.getPermissions(Permission.ALL_PERMISSIONS)).queue();
                event.getChannel().sendMessage("<@&1077481941294665868>").queue();
                event.getChannel().sendMessage("<@" + event.getUser().getId() + ">").queue();
            }
        }
    }

    private void sendPrivateMessage(User user, TextChannel closechannel, EmbedBuilder embedBuilder) {
        Main.getInstance().getConfig().set("Data." + user.getId() + ".ticket", "false");
        Main.getInstance().getConfig().set("ChannelData." + closechannel.getId(), null);
        Main.getInstance().saveConfig();
        Objects.requireNonNull(user).openPrivateChannel().flatMap(privateChannel -> privateChannel.sendMessageEmbeds(embedBuilder.build())).queue();
    }
}

