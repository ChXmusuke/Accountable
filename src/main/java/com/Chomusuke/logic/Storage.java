/*  Accountable: a personal spending monitoring program
    Copyright (C) 2023  Artur Yukhanov

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package com.chomusuke.logic;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static com.chomusuke.logic.Transaction.TransactionType;
import static com.chomusuke.logic.Transaction.ValueType;

public class Storage {

    private static final int MAX_TRANSACTION_COUNT = 512;

    public static final Path ROOT_DIR = Path.of(System.getProperty("user.home")).resolve(System.getProperty("os.name").equals("Mac OS X") ? "Library/Application Support" : "AppData/Roaming");
    private static final Path DIR_NAME = ROOT_DIR.resolve("Accountable/storage/");

    /**
     * Don't let anyone instantiate this class.
     */
    private Storage() {}

    /**
     * Writes the specified list to a file according to the given year and month.
     *
     * @param list  a {@code Transaction} list
     * @param year  a value
     * @param month a value
     *
     * @return {@code true} on success.
     */
    public static boolean write(List<Transaction> list, int year, int month) {

        Path dir = DIR_NAME.resolve(Integer.toString(year));
        Path file = Path.of(Integer.toString(month));

        try {
            Files.createDirectories(dir);
            Files.createFile(dir.resolve(file));
        } catch (FileAlreadyExistsException ignored) {
            // Exception ignored
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (DataOutputStream output = new DataOutputStream(new FileOutputStream(dir.resolve(file).toString()))) {

            for (Transaction t : list) {
                output.writeUTF(t.name());
                output.writeByte(t.to());
                output.writeByte(t.packTypes());
                output.writeFloat(t.value());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    public static List<Transaction> load(int year, int month) {

        Path dir = DIR_NAME.resolve(Integer.toString(year));
        Path file = Path.of(Integer.toString(month));

        LinkedList<Transaction> txs = new LinkedList<>();

        try (DataInputStream input = new DataInputStream(new FileInputStream(dir.resolve(file).toString()))) {
            for (int i = 0 ; i < MAX_TRANSACTION_COUNT ; i++) {
                String name = input.readUTF();

                byte to = input.readByte();

                byte types = input.readByte();
                TransactionType tt = TransactionType.of((byte) (types >>> 2));
                ValueType vt = ValueType.of(types);

                float value = input.readFloat();

                txs.add(new Transaction(name, to, tt, vt, value));
            }

            throw new RuntimeException("Le fichier contient trop de transactions.");
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        } catch (EOFException ignored) {
            // Exception ignored
        } catch (Exception e) {
            e.printStackTrace();
        }

        return txs;
    }

    public static List<String> getAvailableMonths(int year) {

        return getAvailable(Integer.toString(year));
    }

    public static List<String> getAvailableYears() {

        return getAvailable("");
    }

    private static List<String> getAvailable(String path) {

        List<String> valid = new ArrayList<>();
        File[] availableF = new File(DIR_NAME.resolve(path).toString()).listFiles();

        if (availableF != null) {
            Arrays.sort(availableF);
            for (File f : availableF) {
                try {
                    Integer.parseInt(f.getName());

                    valid.add(f.getName());
                } catch (NumberFormatException ignored) {
                    // Exception ignored
                }
            }
        }

        Collections.sort(valid);

        return valid;
    }
}