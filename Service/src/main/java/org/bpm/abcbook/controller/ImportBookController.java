package org.bpm.abcbook.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Data;

import org.apache.poi.ss.usermodel.*;
import org.bpm.abcbook.dto.BookImportDTO;
import org.bpm.abcbook.exception.AppException;
import org.bpm.abcbook.service.InventoryService;
import org.bpm.abcbook.util.DataUtil;
import org.primefaces.model.StreamedContent;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.primefaces.model.file.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


@Named
@Data
@ViewScoped
public class ImportBookController {
    private static final Logger logger = LoggerFactory.getLogger(ImportBookController.class);

    private static final String TEMPLATE_PATH = "templates/book_import_template.xlsx";
    private StreamedContent templateFile;
    private UploadedFile uploadedFile;
    private List<BookImportDTO> importResults;
    private String currentStaffCode;

    @Inject
    private UserSessionBean userSessionBean;
    @Autowired
    private InventoryService inventoryService;

    @PostConstruct
    public void init() {
        currentStaffCode = userSessionBean == null || userSessionBean.getCurrentStaff() == null ?
                null : userSessionBean.getCurrentStaff().getStaffCode();
    }

    public void clearFile() {
        if (this.uploadedFile == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Lưu ý", "Phải tải file lên trước khi xóa"));
            return;
        }
        this.uploadedFile = null;
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Thành công", "File đã được xóa"));
    }

    public void readExcelFile() {
        List<BookImportDTO> result = new ArrayList<>();
        if (uploadedFile == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Lỗi",
                            "Vui lòng chọn file để import"));
            return;
        }

        try {
            // Lưu file vào bộ nhớ trước khi xử lý
            byte[] fileContent = uploadedFile.getContent();
            if (fileContent == null || fileContent.length == 0) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Lỗi",
                                "File trống hoặc không hợp lệ"));
                return;
            }

            // Đọc file từ bộ nhớ
            try (ByteArrayInputStream inputStream = new ByteArrayInputStream(fileContent);
                 Workbook workbook = WorkbookFactory.create(inputStream)) {

                Sheet sheet = workbook.getSheetAt(0);

                Row rowValidate = sheet.getRow(0);
                Cell idTitle = rowValidate.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                Cell quantityTitle = rowValidate.getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

                String idTitleStr = idTitle.getStringCellValue();
                if (!"ID sách".equals(idTitleStr)) {
                    throw new AppException("REF00001", "Sai định dạng file");
                }

                String quantityTitleStr = quantityTitle.getStringCellValue();
                if (!"Số lượng".equals(quantityTitleStr)) {
                    throw new AppException("REF00002", "Sai định dạng file");
                }

                int rowStart = 1; // Bỏ qua dòng tiêu đề (dòng 0)


                for (int i = rowStart; i < sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    if (row == null) continue;

                    // Đọc từng cột
                    Cell cellbookId = row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    Cell cellquantity = row.getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

                    Long bookId = (long) cellbookId.getNumericCellValue();
                    Long quantity = (long) cellquantity.getNumericCellValue();

                    // Thêm vào danh sách
                    result.add(new BookImportDTO(bookId, quantity));
                }
            }


            if (DataUtil.isNullOrEmpty(result)) {
                throw new AppException("REF00003", "Không có dữ liệu");
            }

            inventoryService.importBook(result, currentStaffCode);

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Thành công", "Đã thêm sách vào kho thành công"));
        } catch (Exception e) {
            logger.error("Lỗi khi đọc file Excel: " + e.getMessage(), e);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Lỗi",
                            "Không thể đọc file excel vui lòng kiểm tra lại định dạng file"));
        }
    }
}
