package ru.spbau.mit;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class TestFTP {
    private static final String TEST_FOLDER = "src/test/resources/testFolder";
    private static final int TEST_PORT = 4444;

    @Test
    public void simpleTest() throws IOException {
        FTPServer server = new FTPServer(TEST_PORT, TEST_FOLDER);
        FTPClient client = new FTPClient("localhost", TEST_PORT);
        assertEquals(Arrays.asList(".\\", "123.txt", "innerFolder\\"), client.listFiles("."));
        assertEquals(Arrays.asList("innerInnerFolder\\", "234.txt"),
                client.listFiles("innerFolder\\innerInnerFolder"));
        client.getAndSaveFile("123.txt", ".");
        assertEquals(Files.readAllLines(Paths.get(TEST_FOLDER + "/123.txt")),
                Files.readAllLines(Paths.get("123.txt")));
        client.getAndSaveFile("noSuchFile.txt", ".");
        assertEquals(new ArrayList<String>(), Files.readAllLines(Paths.get("noSuchFile.txt")));
        server.close();
        client.close();
    }
}
