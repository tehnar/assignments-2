package ru.spbau.mit;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class TestFTP {
    private static final String TEST_FOLDER = "src/test/resources/testFolder";
    @Test
    public void simpleTestList() throws IOException {
        FTPServer server = new FTPServer(4444, TEST_FOLDER);
        FTPClient client = new FTPClient("localhost", 4444);
        assertEquals(Arrays.asList(".\\", "123.txt", "innerFolder\\"), client.listFiles("."));
        assertEquals(Arrays.asList("innerInnerFolder\\", "234.txt"), client.listFiles("innerFolder\\innerInnerFolder"));
        client.getAndSaveFile("123.txt", ".");
        assertEquals(Files.readAllLines(Paths.get(TEST_FOLDER + "/123.txt")), Files.readAllLines(Paths.get("123.txt")));
        server.close();
        client.close();
    }
}
