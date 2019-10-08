package com.MikeTheShadow.PokeBotMain;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import java.io.*;
import java.net.URL;
import java.util.Objects;

public class Listener extends ListenerAdapter
{
    @Override
    public void onMessageReceived(MessageReceivedEvent msg)
    {
        //check if bot is afk or not
        if(!Main.canRun) return;
        if(!msg.getAuthor().isBot() || !msg.getAuthor().getId().equals("365975655608745985") || msg.getChannel().getType() == ChannelType.PRIVATE)return;
        if(msg.getMessage().getEmbeds().size() < 1) return;
        try
        {
            String username = Main.api.getSelfUser().getName().toLowerCase();
            String input =  Objects.requireNonNull(msg.getMessage().getEmbeds().get(0).getTitle()).toLowerCase();
            if(Objects.requireNonNull(msg.getMessage().getEmbeds().get(0).getTitle()).toLowerCase().contains("congratulations") && input.contains(username))
            {
                if(Objects.requireNonNull(msg.getMessage().getEmbeds().get(0).getDescription()).contains("100!"))
                {
                    if(Main.levelList == null) return;
                    String checkNextPokemon = Main.checkForNextPokemon();
                    if(!checkNextPokemon.equals("-1")) return;
                    if(Main.checkForNextPokemon().equals("-1"))
                    msg.getChannel().sendMessage(Main.PREFIX + "select " + checkNextPokemon).complete();
                    Main.SaveProperties();
                }
                return;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return;
        }
        if(msg.getMessage().getEmbeds().get(0).getDescription().contains("nd type p!c"))
        {
            System.out.println(msg.getMessage().getEmbeds().get(0));
            try
            {
                MessageEmbed embed = msg.getMessage().getEmbeds().get(0);
                URL url = new URL(embed.getImage().getUrl());
                try
                {
                    //BufferedImage image = ImageIO.read(url);
                    PokeSolverThread solve = new PokeSolverThread("PokeThread",msg.getTextChannel(),url);
                    solve.start();
                }
                catch (Exception e)
                {
                    System.out.println("THIS IS A HTTP.AGENT ERROR! Please report it as such thanks!");
                    e.printStackTrace();

                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onReady(ReadyEvent event)
    {
        while(true)
        {
            try
            {
                System.out.println(Main.api.getSelfUser().getName());
                Thread.sleep(1000);
                Main.CHANNEL = Main.api.getTextChannelById(Main.channelID);
                if(Main.channelID != null && Main.sendMessages)
                {
                    Main.Output("Setting Main channel to: " + Main.CHANNEL.getName());
                    OnConnect newThread = new OnConnect("MessageThread",Main.CHANNEL);
                    newThread.start();
                    try
                    {
                        if(Main.levelList != null)
                        {
                            String checkNextPokemon = Main.checkForNextPokemon();
                            if(!checkNextPokemon.equals("-1")) Main.CHANNEL.sendMessage(Main.PREFIX + "select " + checkNextPokemon).complete();
                        }

                    }
                    catch (Exception e)
                    {
                        System.out.println("Minor levellist error you can ignore this.");
                        e.printStackTrace();
                    }
                    Main.StartSlaves();
                    return;
                }
            }
            catch (Exception e)
            {
                System.out.println("ONLOAD ERROR! Check to make sure your bot acc can see the correct channel");
            }

        }
    }

}