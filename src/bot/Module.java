package bot;

import java.util.List;
import java.util.StringTokenizer;

import bot.Bot;
import bot.util.Helpers;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.managers.GuildController;

/**
 * This class is the basis for all modules/commands the bot can handle. Modules
 * are essentially a set of functions the bot can use. New modules (which should
 * be in the `modules` folder) need to be registered with the bot before it can
 * be used. This allows the bot to tell whether a given command is valid
 * 
 * All Modules should be named [Functionality]Module where [Functionality] is
 * the name of your module. For example, "RoleModule" or "BanModule"
 * 
 * 
 * 
 * @author Namdrib
 *
 */
public abstract class Module
{
	// Variables
	protected GuildMessageReceivedEvent	event;
	protected Message					message;
	protected Member					member;
	protected MessageChannel			channel;
	protected Guild						guild;
	protected GuildController			guildController;

	protected StringTokenizer			st;
	protected List<String>				commandList;
	protected String					identifier;

	protected Bot						bot;

	public Module(Bot bot)
	{
		this.bot = bot;
		bot.register(this, identifier);
		setCommandList();
	}

	public Module(Bot bot, String identifier)
	{
		this.bot = bot;
		this.identifier = identifier;
		bot.register(this, this.identifier);
		setCommandList();
	}

	protected abstract void setCommandList();

	/**
	 * Handle the event. Generally, parse the message to figure out what command
	 * was invoked and carry it out.
	 * 
	 * If we got to this point, assume this Module was the intended target
	 * 
	 * @param e
	 *            the guild message received event for this
	 * @throws Exception
	 *             things
	 */
	public void handle(GuildMessageReceivedEvent e, StringTokenizer st)
	{
		if (e.isWebhookMessage())
		{
			return;
		}
		event = e;
		message = e.getMessage();
		member = e.getMember();
		channel = e.getChannel();
		guild = e.getGuild();
		this.st = st;

		if (guild == null)
		{
			return;
		}

		System.out.println(member.getEffectiveName() + " : "
				+ message.getContentDisplay());

		guildController = guild.getController();
		if (!st.hasMoreTokens())
		{
			help();
		}
		else
		{
			String command = st.nextToken().toLowerCase();
			if (commandList.contains(command))
			{
				execute(command);
			}
			else
			{
				help();
			}
		}
	}

	/**
	 * Execute command `command`. Additional arguments may be read from `st`
	 * 
	 * @param command
	 *            the command to execute
	 */
	protected void execute(String command)
	{
		Helpers.send(channel, "Handle " + command + " here");
	}

	/**
	 * Display a help message for this module. Each module needs its own help
	 * function. Ideally, display at least one line for each publicly available
	 * command
	 */
	protected abstract void help();
}
