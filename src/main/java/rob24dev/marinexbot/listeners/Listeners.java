package rob24dev.marinexbot.listeners;

import com.vdurmont.emoji.EmojiParser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.jetbrains.annotations.NotNull;
import rob24dev.marinexbot.managers.PossibilitiesManager;

import java.util.ArrayList;
import java.util.Objects;

public class Listeners extends ListenerAdapter {


    private Modal newPossibilityModal;
    TextInput newPossibility;

    public static boolean inEditMode;
    public static String question;
    public static ArrayList<PossibilitiesManager> possibilitiesList = new ArrayList<>();

    Button add = Button.secondary("MarinexAdd", "Přidat možnost odpovědi");
    Button remove = Button.danger("MarinexRemove", "Zrušit tvoření ankety");
    Button send = Button.success("MarinexSend", "Odeslat anketu");
    Button removePossibility = Button.danger("MarinexPossibilityRemove", "Smazat poslední možnost");

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        Member member = event.getMember();
        if (event.getName().equalsIgnoreCase("say")) {
            if (event.getChannel().getType().equals(ChannelType.TEXT)) {
                if (Objects.requireNonNull(member).getId().equalsIgnoreCase("989832842890579978") | (Objects.requireNonNull(member).getId().equalsIgnoreCase("1070980062050000926") | (Objects.requireNonNull(member).getId().equalsIgnoreCase("970328298875928676")))) {
                    OptionMapping messageArg = event.getOption("message");
                    String message = Objects.requireNonNull(messageArg).getAsString();
                    event.getChannel().sendMessage(message).queue();
                    event.reply("Tvojí zprávu jsem úspěšně odeslal :)").setEphemeral(true).queue();
                } else {
                    event.reply("Je mi to líto ale na toto nemáš oprávnění :(").setEphemeral(true).queue();
                }
            } else {
                event.reply("Tento příkaz lze použít pouze v klasickém textovém kanálu!").setEphemeral(true).queue();
            }
        } else if (event.getName().equalsIgnoreCase("ip")) {
            event.reply("Ahoj <@" + Objects.requireNonNull(event.getMember()).getId() + ">" + ", vídím, že tě zajímá jaká je IP na **Marinex**, rád ti odpovím **IP:** **marinex.rob24.eu**. Těším se, až se tam potkáme!").setEphemeral(true).queue();
        } else if (event.getName().equalsIgnoreCase("roleadd")) {
            if (Objects.requireNonNull(member).getId().equalsIgnoreCase("989832842890579978") | (Objects.requireNonNull(member).getId().equalsIgnoreCase("1070980062050000926") | (Objects.requireNonNull(member).getId().equalsIgnoreCase("970328298875928676")))) {
                OptionMapping targetArg = event.getOption("target");
                User target = Objects.requireNonNull(targetArg).getAsUser();
                OptionMapping roleArg = event.getOption("role");
                Role role = Objects.requireNonNull(roleArg).getAsRole();
                Objects.requireNonNull(event.getGuild()).addRoleToMember(target, role).queue();
                event.reply("Role " + role.getName() + " byla uživatelovi " + target.getEffectiveName() + " úspěšně přidaná!").setEphemeral(true).queue();
            } else {
                event.reply("Je mi to líto ale na toto nemáš oprávnění :(").setEphemeral(true).queue();
            }
        } else if (event.getName().equalsIgnoreCase("roleremove")) {
            if (Objects.requireNonNull(member).getId().equalsIgnoreCase("989832842890579978") | (Objects.requireNonNull(member).getId().equalsIgnoreCase("1070980062050000926") | (Objects.requireNonNull(member).getId().equalsIgnoreCase("970328298875928676")))) {
                OptionMapping targetArg = event.getOption("target");
                User target = Objects.requireNonNull(targetArg).getAsUser();
                OptionMapping roleArg = event.getOption("role");
                Role role = Objects.requireNonNull(roleArg).getAsRole();
                Objects.requireNonNull(event.getGuild()).removeRoleFromMember(target, role).queue();
                event.reply("Role " + role.getName() + " byla uživatelovi " + target.getEffectiveName() + " úspěšně odstraněna!").setEphemeral(true).queue();
            } else {
                event.reply("Je mi to líto ale na toto nemáš oprávnění :(").setEphemeral(true).queue();
            }
        } else if (event.getName().equalsIgnoreCase("anketa")) {
            if (Objects.requireNonNull(member).getId().equalsIgnoreCase("989832842890579978") | (Objects.requireNonNull(member).getId().equalsIgnoreCase("1070980062050000926") | (Objects.requireNonNull(member).getId().equalsIgnoreCase("970328298875928676")))) {
                if (event.getChannel().getType().equals(ChannelType.TEXT)) {
                    if (event.getChannel().getId().equalsIgnoreCase("1062082859067768832")) {
                        if (!inEditMode) {
                            inEditMode = true;
                            OptionMapping questionArg = event.getOption("question");
                            question = Objects.requireNonNull(questionArg).getAsString();
                            EmbedBuilder questionEmbed = new EmbedBuilder();
                            questionEmbed.setTitle("**Marinex | Anketa**");
                            questionEmbed.setDescription("   \n **Otázka:** \n" + question + " \n \n **Možnosti:** \n Zatím žádné");
                            questionEmbed.setColor(0xd48600);
                            questionEmbed.setThumbnail("https://media.giphy.com/media/3FogJGpt7jfu5zlKdB/giphy.gif");
                            Button add = Button.secondary("MarinexAdd", "Přidat možnost odpovědi");
                            Button remove = Button.danger("MarinexRemove", "Zrušit tvoření ankety");
                            Button send = Button.success("MarinexSend", "Odeslat anketu");
                            Button removePossibility = Button.danger("MarinexPossibilityRemove", "Smazat poslední možnost");
                            createModal();
                            event.replyEmbeds(questionEmbed.build()).setEphemeral(true).setActionRow(add, remove, removePossibility, send).queue();
                        } else {
                            event.reply("Již tvoříš anketu nebo jí tvoří jiný člen AT!").setEphemeral(true).queue();
                        }
                    } else {
                        event.reply("Tento příkaz lze použít pouze v kanálu ankety!").setEphemeral(true).queue();
                    }
                } else {
                    event.reply("Tento příkaz lze použít pouze v klasickém textovém kanálu!").setEphemeral(true).queue();
                }
            } else {
                event.reply("Je mi to líto ale na toto nemáš oprávnění :(").setEphemeral(true).queue();
            }
        } else if (event.getName().equalsIgnoreCase("oznameni")) {
            if (Objects.requireNonNull(member).getId().equalsIgnoreCase("989832842890579978") | (Objects.requireNonNull(member).getId().equalsIgnoreCase("1070980062050000926") | (Objects.requireNonNull(member).getId().equalsIgnoreCase("970328298875928676")))) {
                if (event.getChannel().getType().equals(ChannelType.TEXT)) {
                    if (event.getChannel().getId().equalsIgnoreCase("1052919542688518165")) {
                        OptionMapping messageArg = event.getOption("message");
                        String message = Objects.requireNonNull(messageArg).getAsString();
                        OptionMapping pingArg = event.getOption("ping");
                        boolean ping = Objects.requireNonNull(pingArg).getAsBoolean();
                        EmbedBuilder announcement = new EmbedBuilder();
                        announcement.setTitle("**Marinex | Oznámení**");
                        announcement.setDescription("\n **Oznámení:** \n" + message + " \n \n **Váš tým Marinex**");
                        announcement.setThumbnail("https://media.giphy.com/media/v1.Y2lkPTc5MGI3NjExOGsxY2gzczUzOG1hYTIzdWhyeHJsbTVhZncxYmRsNHZ5NTZiYzZrOCZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9cw/s7tpTmE4YhuC3FX0Hi/giphy.gif");
                        announcement.setColor(0xd48600);
                        Objects.requireNonNull(event.getChannel()).sendMessageEmbeds(announcement.build()).complete();
                        if (ping) {
                            event.getChannel().sendMessage("<@&1077240462487846952>").complete();
                        }
                        event.reply("Oznámení bylo úspěšně odeslané!").setEphemeral(true).queue();
                    } else {
                        event.reply("Tento příkaz lze použít pouze v kanálu oznámení!").setEphemeral(true).queue();
                    }
                } else {
                    event.reply("Tento příkaz lze použít pouze v klasickém textovém kanálu!").setEphemeral(true).queue();
                }
            } else {
                event.reply("Je mi to líto ale na toto nemáš oprávnění :(").setEphemeral(true).queue();
            }
        } else if (event.getName().equalsIgnoreCase("resetdata")) {
            if (Objects.requireNonNull(member).getId().equalsIgnoreCase("989832842890579978") | (Objects.requireNonNull(member).getId().equalsIgnoreCase("1070980062050000926") | (Objects.requireNonNull(member).getId().equalsIgnoreCase("970328298875928676")))) {
                inEditMode = false;
                possibilitiesList.clear();
                event.reply("Data bota byly resetovány!").setEphemeral(true).queue();
            } else {
                event.reply("Je mi to líto ale na toto nemáš oprávnění :(").setEphemeral(true).queue();
            }
        }
    }

    private EmbedBuilder getNewView() {
        EmbedBuilder newViewsEmbed = new EmbedBuilder();
        newViewsEmbed.setTitle("**Marinex | Anketa**");
        newViewsEmbed.setColor(0xd48600);
        if(possibilitiesList.size() == 0) {
            newViewsEmbed.setDescription("   \n **Otázka:** \n" + question + " \n \n **Možnosti:** \n Zatím žádné");
        } else {
            newViewsEmbed.setDescription("   \n **Otázka:** \n" + question + " \n \n **Možnosti:** \n");
            for (int fPossibilities = 0; fPossibilities != possibilitiesList.size(); fPossibilities++) {
                PossibilitiesManager currentPossibility = possibilitiesList.get(fPossibilities);
                newViewsEmbed.addField(":regional_indicator_" + convertToLetter(fPossibilities + 1) + ": ", " **-** *" + currentPossibility.getText() + "*", true);
            }
        }
        newViewsEmbed.setThumbnail("https://media.giphy.com/media/3FogJGpt7jfu5zlKdB/giphy.gif");
        return newViewsEmbed;
    }


    public static String convertToLetter(int number) {
        if (number < 1 || number > 26) {
            return "Unknown";
        }

        char letter = (char) ('a' + number - 1);
        return String.valueOf(letter);
    }

    private void createModal() {
        int number = possibilitiesList.size() + 1;
        newPossibility = TextInput.create("PossibilityText", "Jaký bude text možnosti " + number + " ?", TextInputStyle.PARAGRAPH)
                .setMinLength(1)
                .setMaxLength(50)
                .setRequired(true)
                .setPlaceholder("Zde napište text možnosti " + number)
                .build();
        newPossibilityModal = Modal.create("newPossibility", "Jaká bude možnost " + number + " ?").addActionRow(newPossibility).build();

    }

    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        if (event.getModalId().equalsIgnoreCase("newPossibility")) {
            String possibilityText = Objects.requireNonNull(event.getValue("PossibilityText")).getAsString();
            possibilitiesList.add(new PossibilitiesManager(possibilityText));
            event.replyEmbeds(getNewView().build()).setEphemeral(true).setActionRow(add, remove, removePossibility, send).queue();
            createModal();
        }
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        if (event.getChannel().getId().equalsIgnoreCase("1062082859067768832") | Objects.requireNonNull(event.getUser().getName()).equalsIgnoreCase("rob24dev2")) {
            if (Objects.requireNonNull(event.getButton().getId()).equalsIgnoreCase("MarinexAdd")) {
                if (inEditMode) {
                    event.replyModal(newPossibilityModal).queue();
                } else {
                    event.reply("Tato anketa již byla vymazaná nebo publikovaná tyto zprávy můžeš smazat").setEphemeral(true).queue();
                }
            } else if (Objects.requireNonNull(event.getButton().getId()).equalsIgnoreCase("MarinexRemove")) {
                if (inEditMode) {
                    inEditMode = false;
                    event.reply("Tato anketa byla úspěšně smazaná nyní můžeš smazat tyto zprávy!").setEphemeral(true).queue();
                } else {
                    event.reply("Tato anketa již byla vymazaná nebo publikovaná tyto zprávy můžeš smazat").setEphemeral(true).queue();
                }
            } else if (Objects.requireNonNull(event.getButton().getId()).equalsIgnoreCase("MarinexSend")) {
                if (inEditMode) {
                    if (possibilitiesList.size() == 0) {
                        event.reply("Tato anketa musí mít aspoň jednu možnost odpovědi").setEphemeral(true).queue();
                    } else {
                        inEditMode = false;
                        EmbedBuilder finalViewEmbed = new EmbedBuilder();
                        finalViewEmbed.setTitle("**Marinex | Anketa**");
                        finalViewEmbed.setDescription("   \n **Otázka:** \n" + question + " \n \n **Možnosti:** \n");
                        finalViewEmbed.setColor(0xd48600);
                        for (int fPossibilities = 0; fPossibilities != possibilitiesList.size(); fPossibilities++) {
                            PossibilitiesManager currentPossibility = possibilitiesList.get(fPossibilities);
                            finalViewEmbed.addField(":regional_indicator_" + convertToLetter(fPossibilities + 1) + ": ", " **-** *" + currentPossibility.getText() + "*", true);
                        }
                        finalViewEmbed.setThumbnail("https://media.giphy.com/media/3FogJGpt7jfu5zlKdB/giphy.gif");
                        question = null;
                        String emoji;
                        Message message = event.getChannel().sendMessageEmbeds(finalViewEmbed.build()).complete();
                        for (int fPossibilities = 0; fPossibilities != possibilitiesList.size(); fPossibilities++) {
                            emoji = ":regional_indicator_" + convertToLetter(fPossibilities + 1) + ":";
                            message.addReaction(Emoji.fromUnicode(EmojiParser.parseToUnicode(emoji))).queue();
                        }
                        possibilitiesList.clear();
                        event.reply("Anketa byla úspěšně publikovaná!").setEphemeral(true).complete();
                        event.getChannel().sendMessage("<@&1131296090805772328>").complete();
                    }
                } else {
                    event.reply("Tato anketa již byla vymazaná nebo publikovaná tuto zprávu můžeš smazat").setEphemeral(true).queue();
                }
            } else if (Objects.requireNonNull(event.getButton().getId()).equalsIgnoreCase("MarinexPossibilityRemove")) {
                if (inEditMode) {
                    if (possibilitiesList.size() == 0) {
                        event.reply("Tato anketa musí mít aspoň jednu možnost odpovědi").setEphemeral(true).queue();
                    } else {
                        possibilitiesList.remove(possibilitiesList.size() -1);
                        event.replyEmbeds(getNewView().build()).setEphemeral(true).setActionRow(add, remove, removePossibility, send).queue();
                        createModal();
                    }
                } else {
                    event.reply("Tato anketa již byla vymazaná nebo publikovaná tuto zprávu můžeš smazat").setEphemeral(true).queue();
                }
            }
        }
    }
}