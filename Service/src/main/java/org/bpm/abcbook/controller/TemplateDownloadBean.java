package org.bpm.abcbook.controller;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Named
@RequestScoped
public class TemplateDownloadBean {
    private static final Logger logger = LoggerFactory.getLogger(TemplateDownloadBean.class);
    private static final String TEMPLATE_PATH = "templates/book_import_template.xlsx";

    public StreamedContent getTemplateFile() {
        try {
            Path filePath = Paths.get("src/main/resources/templates/book_import_template.xlsx");
            byte[] fileContent = Files.readAllBytes(filePath);
            return DefaultStreamedContent.builder()
                    .name("book_import_template.xlsx")
                    .contentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                    .stream(() -> new ByteArrayInputStream(fileContent))
                    .build();

        } catch (IOException e) {
            String errorMsg = "Lỗi khi đọc file template: " + e.getMessage();
            logger.error(errorMsg, e);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Lỗi", errorMsg));
            return null;
        }
    }
}