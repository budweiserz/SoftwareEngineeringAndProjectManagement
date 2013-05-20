package at.ticketline.test;

import java.io.FileNotFoundException;
import java.io.IOException;

public class DataGenerator {

    public static void main(String[] args) {
        
        TestInitializer.init();

        // TestUtility.dropDatabaseSchema();

        try {
            TestUtility.generateTestData();
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}