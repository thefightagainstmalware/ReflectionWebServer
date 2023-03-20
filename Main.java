import java.io.*;
import java.net.*;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.lang.reflect.*;
class Main {

    public static void main(String[] args) throws Exception{
        File jar = new File(args[0]);
        jar.deleteOnExit(); // delete the jar when this exits to save space
        JarFile jarFile = new JarFile(jar);
        URLClassLoader ucl = new URLClassLoader(new URL[]{jar.toURI().toURL()});
        for (JarEntry entry: new JarFileIterator(jarFile)) {
            if (entry.isDirectory() || !entry.getName().endsWith(".class"))
                continue;
            
            Class<?> cls;
            try {
                cls = ucl.loadClass(entry.getName().replace("/", ".").replace(".class", ""));
            } catch (Throwable t) {
                continue;
            }
            for (Method m: cls.getMethods()) {
                if (m.getReturnType().isAssignableFrom(String.class)) {
                    try {
                        String result = (String) m.invoke(null);
                        System.out.println(result);
                    } catch (Throwable t) {
                        continue;
                    }
                }
            }
        }
        ucl.close();
    }

    static class JarFileIterator implements Iterable<JarEntry> {
        JarFile jar;
        public JarFileIterator(JarFile jar) {
            this.jar = jar;
        }
        @Override
        public Iterator<JarEntry> iterator() {
            return new Iterator<JarEntry>() {
                Enumeration<JarEntry> jarentries = jar.entries(); // might not be needed, safer to keep it

                @Override
                public boolean hasNext() {
                    return jarentries.hasMoreElements();
                }

                @Override
                public JarEntry next() {
                    return jarentries.nextElement();
                }
            };    
        }
    }
}
