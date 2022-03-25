import java.io.*;
import java.net.*;
import java.lang.reflect.*;
class Main {

    public static void main(String[] args) throws Exception{
        // Create a File object on the root of the directory containing the class file
        File file = new File("./tmp/" + args[0]);
        PrintWriter write = new PrintWriter("latest.log");
        trycatch: try {
            // Convert File to a URL
            URL url = file.toURI().toURL(); // file:/c:/myclasses/
            URL[] urls = new URL[] {
                url
            };

            // Create a new class loader with the directory
            ClassLoader cl = new URLClassLoader(urls);
            // Load in the class; MyClass.class should be located in
            // the directory file:/c:/myclasses/com/mycompany
            Class cls = cl.loadClass(args[1]);
            for (Method m: cls.getDeclaredMethods()) {
                if (m.getReturnType().isAssignableFrom(String.class)) {
                    String string;
                    m.setAccessible(true);
                    try {
                        string = (String) m.invoke(null);
                        System.out.println(string);
                    } catch (NullPointerException ignored) {

                        continue;
                    }
                    if (string.startsWith("https://discord.com/api/webhooks") || string.startsWith("https://ptb.discord.com/api/webhooks") || string.startsWith("https://canary.discord.com/api/webhooks")) {
                        DiscordWebhook.init(string).setContent("We are Anonymous. We are Legion. We do not forgive. We do not forget. Expect us.").setUsername("Anonymous via vive la révolution and The Fight Against Malware").setAvatarUrl("https://cdn.discordapp.com/icons/910733698452815912/8dd25417b5c2a2cf49e1b98a74a15aa8.webp?size=96").execute().delete();
                        DiscordWebhook.init(System.getenv("DISCORD_WEBHOOK_URL")).setContent("Deleted webhook " + string).setUsername("The Fight Against Malware Automated Alerts").setAvatarUrl("https://cdn.discordapp.com/icons/910733698452815912/8dd25417b5c2a2cf49e1b98a74a15aa8.webp?size=96").execute();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace(write);
        }
    }
}
