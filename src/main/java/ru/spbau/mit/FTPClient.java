package ru.spbau.mit;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tehnar on 07.03.2016.
 */
public class FTPClient implements Closeable {
    private Socket connection;
    private DataInputStream input;
    private DataOutputStream output;

    private static final int LIST_QUERY = 1;
    private static final int GET_QUERY = 2;
    private static final int CHUNK_SIZE = 4096;

    public FTPClient(String host, int port) throws IOException {
        connection = new Socket(host, port);
        input = new DataInputStream(connection.getInputStream());
        output = new DataOutputStream(connection.getOutputStream());
    }

    public List<String> listFiles(String path) throws IOException {
        output.writeInt(LIST_QUERY);
        output.writeUTF(path);
        output.flush();
        int size = input.readInt();
        List<String> files = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            String filename = input.readUTF();
            if (input.readBoolean()) {
                files.add(filename + "\\");
            } else {
                files.add(filename);
            }
        }
        return files;
    }

    public void getAndSaveFile(String filename, String savePath) throws IOException {
        output.writeInt(GET_QUERY);
        output.writeUTF(filename);
        output.flush();

        long size = input.readLong();
        OutputStream stream = Files.newOutputStream(Paths.get(savePath, filename));
        byte[] buffer = new byte[CHUNK_SIZE];
        while (size > 0) {
            int readSize = input.read(buffer, 0, CHUNK_SIZE);
            size -= readSize;
            stream.write(buffer, 0, readSize);
        }
    }

    @Override
    public void close() throws IOException {
        if (connection == null) {
            return;
        }
        connection.close();
        connection = null;
        input = null;
        output = null;
    }
}
