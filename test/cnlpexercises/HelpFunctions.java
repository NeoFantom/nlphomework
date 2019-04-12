package cnlpexercises;

import java.io.*;

public class HelpFunctions {
    static BufferedReader getReader(String path) throws IOException {
        return new BufferedReader(new FileReader(path));
    }

    static BufferedWriter getWriter(String path) throws IOException {
        return new BufferedWriter(new FileWriter(path));
    }
}
