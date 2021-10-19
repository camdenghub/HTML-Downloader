import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private static final String[] ends = new String[]{".js", ".css", ".html", ".png", ".svg", ".jpg", ".gif"};

    private static boolean validURL(String url) {
        for (final String end : ends) {
            if (url.endsWith(end) && !url.contains("./"))
                return true;
        }
        return false;
    }

    public static void main(String[] args) throws IOException {
        final File input = new File("input.txt");
        if (!input.exists()) {
            System.out.println("Put html into input.txt");
            input.createNewFile();
            System.exit(-1);
        } else {
            final File output = new File("output");
            if (!output.exists()) {
                output.mkdir();
            }
            System.out.print("Enter Base URL:");
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            String url = bufferedReader.readLine();
            final List<String> dlToCheck = new ArrayList<>(Files.readAllLines(input.toPath()));
            for (String sp : dlToCheck) {
                Pattern p = Pattern.compile("\"([^\"]*)\"");
                Matcher m = p.matcher(sp);
                while (m.find()) {
                    final String cleaned = m.group().replace("\"", "");
                    if (!validURL(cleaned))
                        continue;
                    System.out.println("Downloading: " + cleaned);
                    if (cleaned.toLowerCase().startsWith("http")) {
                        URL website = new URL(cleaned);
                        try (InputStream in = website.openStream()) {
                            Files.copy(in, new File(output + File.separator + m.group(1).substring(m.group(1).lastIndexOf("/"))).toPath(), StandardCopyOption.REPLACE_EXISTING);
                        }
                    } else {
                        URL website = new URL(url + cleaned);
                        try (InputStream in = website.openStream()) {
                            Files.copy(in, new File(output + File.separator + m.group(1).substring(m.group(1).lastIndexOf("/"))).toPath(), StandardCopyOption.REPLACE_EXISTING);
                        }
                    }
                }
            }
        }
    }
}