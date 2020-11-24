package com.devteam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * starting program
 * 
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class DevteamApplication
{
    public static void main(String[] args)
    {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(DevteamApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  Stating Project Success ლ(´ڡ`ლ)ﾞ  \n" +
        				   " _____ _____ _____" +
        				   "|     |_   _|   | |"+
        				   "|   --| | | | | | |"+
        				   "|_____| |_| |_|___|"+
                           " ''-'   `'-'    `-'");
    }
}
