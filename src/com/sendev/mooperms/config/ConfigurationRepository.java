package com.sendev.mooperms.config;

import com.sendev.mooperms.MooPerms;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfigurationRepository
{

    /**
     * The MooPerms plugin instance.
     *
     * @var MooPerms
     */
    private final MooPerms plugin;

    /**
     * The folder file object will be used to generate our
     * permissions folder, and it will work as a parent
     * folder for our yml configurations.
     *
     * @var folder
     */
    private final File folder = new File("permissions");

    /**
     * The local configuration object.
     *
     * @var local
     */
    private final Configuration local;

    /**
     * The global configuration object.
     *
     * @var global
     */
    private final Configuration global;

    /**
     * Creates a new configuration repository, in the
     * process it will create the local.yml, global.yml
     * config files and the example.txt file.
     *
     * @param MooPerms plugin
     */
    public ConfigurationRepository(MooPerms plugin)
    {
        this.plugin = plugin;

        if (!folder.exists()) {
            folder.mkdir();
        }

        // Sets up our configs
        local = new Configuration(plugin, folder, "local.yml", Arrays.asList(
                "",
                "# Write your custom local permission presets here..",
                ""
        ));

        global = new Configuration(plugin, folder, "global.yml", Arrays.asList(
                "",
                "# Write your custom global permission presets here..",
                ""
        ));

        // Creates our example.txt file if it doesn't exists
        File example = new File(folder, "example.txt");
        if (!example.exists()) {
            try {
                example.createNewFile();

                FileWriter fw = new FileWriter(example.getAbsoluteFile());

                try (BufferedWriter bw = new BufferedWriter(fw)) {
                    // Reads from the example.yml file in the resource folder
                    InputStream is = plugin.getResource("example.yml");

                    final char[] buffer = new char[1024];
                    final StringBuilder out = new StringBuilder();

                    // Convert our character buffer to our string
                    try (Reader in = new InputStreamReader(is, "UTF-8")) {
                        for (;;) {
                            int rsz = in.read(buffer, 0, buffer.length);
                            if (rsz < 0) {
                                break;
                            }
                            out.append(buffer, 0, rsz);
                        }
                    } catch (UnsupportedEncodingException ex) {
                        Logger.getLogger(ConfigurationRepository.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(ConfigurationRepository.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    // Write the content of example.yml to our file
                    for (String line : out.toString().split("\n")) {
                        bw.write(line + "\r\n");
                    }

                } catch (IOException ex) {
                    Logger.getLogger(ConfigurationRepository.class.getName()).log(Level.SEVERE, null, ex);
                }

            } catch (IOException ex) {
                Logger.getLogger(ConfigurationRepository.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Gives you a the provided configuration
     * object, if the object was not found
     * the method will return null.
     *
     * @param ConfigFile file
     * @return Configuration|null
     */
    public Configuration getConfiguration(ConfigFile file)
    {
        if (file == null) {
            return null;
        }

        switch (file) {
            case GLOBAL:
                return global;
            case LOCAL:
                return local;
        }

        return null;
    }

    /**
     * Reloads all of our configurations.
     *
     * @return void
     */
    public void reload()
    {
        for (ConfigFile file : ConfigFile.values()) {
            Configuration config = getConfiguration(file);

            config.reloadConfig();
        }
    }

    /**
     * Globally defines our configuration files as enumeration values.
     */
    public enum ConfigFile
    {

        // Local config
        LOCAL("Local", 0),
        // Global config
        GLOBAL("Global", 1);

        /**
         * The configuration name.
         *
         * @var name
         */
        private final String name;

        /**
         * The configuration id.
         *
         * @var id
         */
        private final int id;

        /**
         * Instantiates our ConfigFile.
         *
         * @param String name
         * @param int    id
         */
        private ConfigFile(String name, int id)
        {
            this.name = name;
            this.id = id;
        }

        /**
         * Gives you the configuration name.
         * 
         * @return String name
         */
        public String getName()
        {
            return name;
        }
        
        /**
         * Gives you the configuration id.
         * 
         * @return int id
         */
        public int getId()
        {
            return id;
        }
    }
}
