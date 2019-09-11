package com.MikeTheShadow.PokeBotMain;

import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.xml.soap.Text;

public class SlaveListener extends ListenerAdapter
{
    @Override
    public void onReady(ReadyEvent event)
    {
        if(Main.channelID != null && Main.sendMessages)
        {
            Main.Output("Waiting to start slave...");
            try
            {
                Thread.sleep(3000);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            Main.slaveNum++;
            TextChannel channel = Main.slaveList.get(Main.slaveNum).getTextChannelById(Main.channelID);
            Main.Output("Setting a slave channel to: " + channel.getName());
            OnConnect newThread = new OnConnect("MessageThread",channel);
            newThread.start();
        }
    }
}
