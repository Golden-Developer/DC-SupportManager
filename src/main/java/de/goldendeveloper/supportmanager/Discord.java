package de.goldendeveloper.supportmanager;

import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import de.goldendeveloper.supportmanager.events.Events;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;

public class Discord {

    private JDA bot;
    private String activity = "<BOT-Activity>";

    public static String getCmdSettings = "settings";
    public static String getCmdSettingsSubChannel = "support-voicechannel";
    public static String getCmdSettingsSubShutdown = "shutdown";

    public Discord(String token) {
        try {
            bot = JDABuilder.createDefault(token)
                    .enableIntents(GatewayIntent.GUILD_MESSAGE_REACTIONS,
                            GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_EMOJIS,
                            GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_PRESENCES,
                            GatewayIntent.GUILD_BANS, GatewayIntent.DIRECT_MESSAGE_REACTIONS,
                            GatewayIntent.GUILD_INVITES, GatewayIntent.DIRECT_MESSAGE_TYPING,
                            GatewayIntent.GUILD_MESSAGE_TYPING, GatewayIntent.GUILD_VOICE_STATES,
                            GatewayIntent.GUILD_WEBHOOKS, GatewayIntent.GUILD_MEMBERS,
                            GatewayIntent.GUILD_MESSAGE_TYPING)
                    .setActivity(Activity.watching(activity))
                    .addEventListeners(new Events())
                    .setAutoReconnect(true)
                    .build().awaitReady();

            bot.upsertCommand(getCmdSettings, "Legt die Einstellungen für dem SupportManager fest").addSubcommands(
                    new SubcommandData(getCmdSettingsSubChannel, "Setzt den Support Channel für den Discord Server").addOption(OptionType.CHANNEL, "channel", "Support Audio Channel"),
                    new SubcommandData(getCmdSettingsSubShutdown, "Schaltet den Discord Bot ab!")
            ).queue();

            Online();
        } catch (LoginException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public JDA getBot() {
        return bot;
    }

    private void Online() {
        WebhookEmbedBuilder embed = new WebhookEmbedBuilder();
        embed.setAuthor(new WebhookEmbed.EmbedAuthor(getBot().getSelfUser().getName(), getBot().getSelfUser().getAvatarUrl(), "https://Golden-Developer.de"));
        embed.addField(new WebhookEmbed.EmbedField(false, "[Status]", "ONLINE"));
        embed.setColor(0x00FF00);
        embed.setFooter(new WebhookEmbed.EmbedFooter("@Golden-Developer", getBot().getSelfUser().getAvatarUrl()));
        new WebhookClientBuilder(Main.getConfig().getDiscordWebhook()).build().send(embed.build());
    }
}