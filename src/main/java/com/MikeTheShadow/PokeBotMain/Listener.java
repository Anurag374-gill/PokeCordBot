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
    //public static int storageInt = 0;
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
                    assert  Main.levelList.size() > 0;
                    msg.getChannel().sendMessage(Main.PREFIX + "select " + Main.levelList.get(0)).complete();
                    Main.levelList.remove(0);
                    if(Main.levelList.size() > 0)
                    {
                        System.out.println("Resetting list");
                        MainPokeBotWindow.pokemonLevelList.setText("");
                        for (String pokemon:Main.levelList)
                        {
                            MainPokeBotWindow.pokemonLevelList.append(pokemon + "\n");
                        }
                    }
                    else
                    {
                        MainPokeBotWindow.pokemonLevelList.setText("");
                    }
                    Main.SaveProperties();
                }
                return;
            }
        }
        catch (Exception e)
        {
            return;
        }
        assert msg.getMessage().getEmbeds().get(0).getDescription() != null;
        if(msg.getMessage().getEmbeds().get(0).getDescription().contains("Guess the pokémon and type"))
        {
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
                    System.out.println("THIS IS A HTTP.AGENT ERROR! Please report it in the not catching pokemon thread thansk!");
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
                Thread.sleep(500);
                Main.CHANNEL = Main.api.getTextChannelById(Main.channelID);
                if(Main.channelID != null && Main.sendMessages)
                {
                    Main.Output("Setting Main channel to: " + Main.CHANNEL.getName());
                    OnConnect newThread = new OnConnect("MessageThread",Main.CHANNEL);
                    newThread.start();
                    Main.CHANNEL.sendMessage(Main.PREFIX + "info").complete();
                    Main.StartSlaves();
                    return;
                }
            }
            catch (Exception e)
            {

                System.out.println("ONLOAD ERROR!");
            }

        }
    }

}