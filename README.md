# BossBarTimers
Forge 1.12.2 plugin utilizing the Minecraft boss bar to create timers that count down and update in real time.  
This plugin can be used in conjunction with event plugins or manually triggered to create countdowns for all players to see!  

There are no permission nodes for this; Commands can only be used by OP's (permission level 4) or sent from console, and boss bars will show to ALL players online (...for now.)  

Commands:

**/bbt**-  Shows command usage  
**/bbt colors** - Lists all possible boss bar colors  
**/bbt list** - Lists active boss bars and their IDs  
**/bbt remove \<id>** - Remove an active boss bar by id #   
**/bbt removecontaining \<string>** - Removes a specific boss bar that contains whatever you input  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ex. /bbt removecontaining Shiny  - Removes a boss bar that contains "Shiny" in its title  
**/bbt start \<color> <duration (in seconds)> \<title>** - Creates a new timer that decrements every second automatically. Setting duration to -1 creates a boss bar that will remain on screen until killed, or the server reboots. $time can be used in the title as a placeholder for time remaining in 00:00 format.  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ex. /bbt start blue 10 &aThere is $time remaining!  - Creates a blue boss bar that will count down from 10 until it hits 0.  
