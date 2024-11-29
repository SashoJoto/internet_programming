package org.example.homework3;

import org.apache.commons.io.FilenameUtils;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@MultipartConfig
public class UploadServlet extends HttpServlet {
    private static final String UPLOAD_DIR = "uploads";
    private static final String[] ALLOWED_EXTENSIONS = {"txt", "jpg", "png"};

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Part filePart = req.getPart("file"); // Get the file part
        String fileName = FilenameUtils.getName(filePart.getSubmittedFileName());
        String extension = FilenameUtils.getExtension(fileName).toLowerCase();

        boolean validExtension = false;
        for (String allowed : ALLOWED_EXTENSIONS) {
            if (allowed.equals(extension)) {
                validExtension = true;
                break;
            }
        }

        if (!validExtension) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Unsupported file type: " + extension);
            return;
        }

        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) uploadDir.mkdir();

        File file = new File(uploadDir, fileName);
        filePart.write(file.getAbsolutePath());

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write("File uploaded successfully. Download URL: /files/" + fileName);
    }
}
