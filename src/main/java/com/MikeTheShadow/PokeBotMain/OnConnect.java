package com.MikeTheShadow.PokeBotMain;
import net.dv8tion.jda.api.entities.TextChannel;
import java.util.Random;


/*
Simple thread that infinitely spams CHARACTER with some variation in timing to avoid any potential time based anticheat
systems TODO keep and eye out for anti-cheat system implementation
 */
public class OnConnect implements Runnable
{
    //try threading
    private  Thread thread;
    private String threadName;
    private TextChannel channel;
    public static int userDelay = 0;
    OnConnect(String name, TextChannel chan)
    {
        this.channel = chan;
        threadName = name;
    }
    public void run()
    {
        while(true)
        {
            if(Main.canRun)
            {
                try
                {
                    if(!Main.stopped)
                    {
                        if(Main.CHARACTER.length() < 1) Main.CHARACTER = ".";
                        if(Main.CHARACTERLIST != null)
                        {
                            Random ran = new Random();
                            int random = ran.nextInt(Main.CHARACTERLIST.length - 1);
                            channel.sendMessage(Main.CHARACTERLIST[random]).complete();
                        }
                        else
                        {
                            channel.sendMessage(Main.CHARACTER).complete();
                        }
                        Random ran = new Random();
                        Thread.sleep(700 + ran.nextInt(500) + userDelay);
                    }
                    else
                    {
                        return;
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }

    }
    void start()
    {
        if(thread == null)
        {
            thread = new Thread(this,threadName);
            thread.start();
        }
    }
}

