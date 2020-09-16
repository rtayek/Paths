package paths;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
public class Paths { // https://www.softwarelab.it/2018/10/12/adding-an-existing-project-to-github-using-the-command-line/
    boolean check(String name,Collection<? extends Object> objects) {
        Set<Object> set=new LinkedHashSet<>();
        boolean ok=true;
        for(Object object:objects)
            if(!set.add(object)) {
                System.out.println("\t"+object+" is a duplicate in: "+name);
                ok=false;
            }
        // i get a dup with the jdk that is my java home. check this.
        return ok;
    }
    List<String> split(String string) {
        String[] words=string.split(separator);
        return Arrays.asList(words);
    }
    List<File> toList(String path) throws IOException {
        List<String> strings=split(path);
        List<File> files=new ArrayList<File>();
        for(String string:strings)
            files.add(new File(string).getCanonicalFile());
        return files;
    }
    @SuppressWarnings("unchecked") void process(String name,Object object) throws IOException {
        Collection<?> files;
        if(object instanceof Collection<?>)
            files=(Collection<?>)object;
        else if(object instanceof String)
            files=toList((String)object);
        else throw new RuntimeException();
        System.out.println(name+": "+files);
        if(!check(name,files)) System.out.println("\thas duplicates!");
        findAndPrintTargets(files);
    }
    private void findAndPrintTargets(Collection<? extends Object> objects) {
        Set<String> unique=new TreeSet<>();
        for(Object object:objects)
            for(String target:targets)
                if(object.toString().contains(target)) if(unique.add(object.toString())) System.out.println("\t"+object);
    }
    private void run() throws IOException {
        System.out.println("separator: "+separator);
        System.out.println("-----------");
        System.out.println("java jdk properties: "+properties);
        String javaLibraryPath=properties.getProperty("java.library.path");
        process("java library path",javaLibraryPath);
        process("runtime environment",runtimeEnvironmentMap.entrySet());
        process("runtime path",runtimePath);
        process("user path",userPath);
        process("system path",systemPath);
    }
    public static void main(String[] args) throws IOException {
        new Paths().run();
    }
    final Properties properties=System.getProperties();
    final Map<String,String> runtimeEnvironmentMap=System.getenv();
    final String runtimePath=runtimeEnvironmentMap.get("Path");
    static final String separator=System.getProperty("path.separator");
    static String[] targets_=new String[] {"java","JAVA","Java","dotnet"};
    static Set<String> targets=new TreeSet<>(Arrays.asList(targets_));
    // gotten by hand from system properties using window+x trick and selecting system.
    // so these are not what happens at runtime
    static final String userPath="C:\\Users\\ray\\AppData\\Local\\Programs\\Python\\Launcher\\;d:\\bin;C:\\Users\\ray\\AppData\\Roaming\\npm;C:\\Users\\ray\\AppData\\Local\\atom\\bin;C:\\Program Files (x86)\\FAHClient;x:\\Program Files\\Docker Toolbox";
    static final String systemPath="C:\\Program Files (x86)\\Common Files\\Oracle\\Java\\javapath;c:\\Rtools\\bin;c:\\Rtools\\mingw_32\\bin;C:\\ProgramData\\Oracle\\Java\\javapath;C:\\Program Files (x86)\\AMD APP\\bin\\x86_64;C:\\Program Files (x86)\\AMD APP\\bin\\x86;%SystemRoot%\\system32;%SystemRoot%;%SystemRoot%\\System32\\Wbem;%SYSTEMROOT%\\System32\\WindowsPowerShell\\v1.0\\;C:\\Program Files (x86)\\ATI Technologies\\ATI.ACE\\Core-Static;C:\\Program Files (x86)\\Common Files\\Acronis\\SnapAPI\\;C:\\Program Files (x86)\\AMD\\ATI.ACE\\Core-Static;C:\\gnuplot\\bin;C:\\Program Files\\Git\\cmd;C:\\Program Files\\Calibre2\\;C:\\Program Files\\MiKTeX 2.9\\miktex\\bin\\x64\\;C:\\Program Files\\MySQL\\MySQL Utilities 1.6\\;C:\\Program Files (x86)\\Common Files\\Acronis\\VirtualFile\\;C:\\Program Files (x86)\\Common Files\\Acronis\\VirtualFile64\\;C:\\Program Files (x86)\\Common Files\\Acronis\\FileProtector\\;C:\\Program Files (x86)\\Common Files\\Acronis\\FileProtector64\\;C:\\Program Files\\nodejs\\;C:\\Program Files (x86)\\Brackets\\command;C:\\Program Files\\Microsoft VS Code\\bin";
}
