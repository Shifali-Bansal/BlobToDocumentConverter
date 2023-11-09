package com.newgen.app;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BlobToDocumentConverter {

    public static void main(String[] args) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/your_database";
        String username = "your_username";
        String password = "your_password";
        String sql = "SELECT document_blob FROM your_table WHERE document_id = ?";

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Replace document_id with the actual ID of the document you want to retrieve
            int documentId = 1;
            preparedStatement.setInt(1, documentId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Blob documentBlob = resultSet.getBlob("document_blob");

                    // Convert blob to document (e.g., save as a file)
                    convertBlobToDocument(documentBlob, "path/to/your/output/document.pdf");
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void convertBlobToDocument(Blob blob, String outputPath) throws IOException, SQLException {
        try (InputStream inputStream = blob.getBinaryStream();
             FileOutputStream outputStream = new FileOutputStream(outputPath)) {

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            System.out.println("Blob converted to document successfully.");
        }
    }
}
