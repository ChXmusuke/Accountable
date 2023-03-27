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

    private static final Path DIR_NAME = Path.of("/Users/artur/Library/Application Support/Accountable/storage/");

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
        } catch (FileAlreadyExistsException e) {
            System.out.println("file already exists");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (DataOutputStream output = new DataOutputStream(new FileOutputStream(dir.resolve(file).toString()))) {
            output.writeInt(list.size());

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
            int entries = input.readInt();

            for (int i = 0 ; i < entries ; i++) {
                String name = input.readUTF();

                byte to = input.readByte();

                byte types = input.readByte();
                TransactionType tt = TransactionType.of((byte) (types >>> 2));
                ValueType vt = ValueType.of(types);

                float value = input.readFloat();

                txs.add(new Transaction(name, to, tt, vt, value));
            }
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return txs;
    }

    public static List<String> getAvailableMonths(String year) {

        return getAvailable(year);
    }

    public static List<String> getAvailableYears() {



        return getAvailable("");
    }

    private static List<String> getAvailable(String path) {

        List<String> valid = new ArrayList<>();
        File[] availableF = new File(DIR_NAME.resolve(path).toString()).listFiles();

        if (availableF != null) {
            for (File f : availableF) {
                try {
                    Integer.parseInt(f.getName());

                    valid.add(f.getName());
                } catch (NumberFormatException ignored) {
                    // Exception ignored
                }
            }
        }

        return valid;
    }
}