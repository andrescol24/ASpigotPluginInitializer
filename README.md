# Andrescol Spigot Plugin Initializer

This library allows you to create Minecraft Spigot plugins faster.

## How to Configure?

1. Add the maven dependency:
   ```xml
   <dependency>
      <groupId>co.andrescol.mc.library</groupId>
      <artifactId>ASpigotPluginInitializer</artifactId>
      <version>1.0.0</version>
      <scope>compile</scope>
   </dependency>
   ```
2. Add the **maven-shade-plugin** to build.plugins:
   ```xml
   <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-shade-plugin</artifactId>
      <version>3.3.0</version>
      <executions>
         <execution>
            <phase>package</phase>
            <goals>
                  <goal>shade</goal>
            </goals>
         </execution>
      </executions>
      <configuration>
         <filters>
            <filter>
                  <artifact>co.andrescol.mc.library:ASpigotPluginInitializer</artifact>
                  <includes>
                     <include>co/**</include>
                  </includes>
            </filter>
         </filters>
      </configuration>
   </plugin>
   ```
3. Create your custom configuration class. This class will be used to load the configuration from the config.yml into an object:
   - Should extend from co.andrescol.mc.library.configuration.AConfigurationObject
   - Add the annotation @ConfigurationKey mapping the variable with the key in the config.yml
   - It supports: Boolean, String, Integer, Double, integer list and string list.
   
   Example:
   ```java
   public class PluginConfiguration implements AConfigurationObject {

      @AConfigurationKey("storage-path")
      private String storagePath;

      @AConfigurationKey("chest-sizes")
      private List<Integer> chestSizes;

      public String getStoragePath() {
         return storagePath;
      }

      public List<Integer> getChestSizes() {
         return chestSizes;
      }
   }
   ```
4. Create your plugin main class and extends from co.andrescol.mc.library.plugin.APlugin<C\>, the generic type is your custon configuration class:
   - Overrides and implements **onEnable** and **onDisable** (org.bukkit.plugin.JavaPlugin). You don't need to load languages or configurations.
   - Implements the method **initializeCustomConfiguration** just need to initialize the custom configuration object

   Example:
   ```java
   public class MyPlugin extends APlugin<PluginConfiguration> {

      @Override
      public void onEnable() {
         this.getCommand("mycmd").setExecutor(new MyCmdCommandExecutor());
      }

      @Override
      public void onDisable() {
         HandlerList.unregisterAll(this);
      }

      @Override
      protected void initializeCustomConfiguration() {
         this.configurationObject = new PluginConfiguration();
      }
   }
   ```

5. Create lang.properties file in the path resources with these values:
   - CONFIGURATION_RELOAD
   - NOT_PERMISSION
   - UNKNOWN_SUBCOMMAND
   - COMMAND_INFO
   - INCORRECT_USAGE_{SUBCOMMAND_NAME}
   - Other messages

## Add Commands
This library allows you to add main commands and subcommand (one level of subcommands). 

This library will show bad usage messages for subcommand using the language.properties properties INCORRECT_USAGE_{SUBCOMMAND_NAME}.

**Using the AMainCommand class automatically you will have the help (COMMAND_INFO) and reload subcommand (it will use onEnable and onDisable from your plugin class)**

1. Create your main command class, it will validate the permission configured in the plugin.yml file. You can add subcommand or set a default execution of your command:
   ```java
   public class MyMainCommand extends AMainCommand {

      public MyMainCommand() {
         this.defaultCommand = new DefaultCommand();
      }
   }
   ```
2. Create the subcommand:
   ```java
   public class MySubCommand extends ASubCommand {

      protected MySubCommand() {
         super("subcommand", "permission.cmd");
      }

      @Override
      public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
         // Main Logic here without permission validation or usages validation
         return true;
      }

      @Override
      public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
         // Implement this if you want to autocomplete the inputs
         return new LinkedList<>();
      }

      @Override
      public boolean goodUsage(CommandSender sender, Command command, String label, String[] args) {
         // Verify usages: arguments and senders (not permission)
         return sender instanceof Player;
      }
   }
   ```   
## Utilities
This library will manage a singleton for the Plugin class. You can access to your plugin instance using: 

```java 
MyPlugin plugin = APlugin.getInstance();
```

Also, you can access to your configuration by:
```java 
PluginConfiguration configuration = APlugin.getConfigurationObject();
```

Send messages from your language.properties using and Enum or a String name:
```java 
AMessage.sendMessage(sender, Message.NOT_PERMISSION_OPEN, args[0]);
```