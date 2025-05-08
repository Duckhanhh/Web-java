package org.example.abcbook.controller;

import org.example.abcbook.dto.request.UpdateBookRequest;
import org.example.abcbook.dto.response.ApiResponse;
import org.example.abcbook.exception.AppException;
import org.example.abcbook.mapper.BooksMapper;
import org.example.abcbook.model.Books;
import org.example.abcbook.service.BooksService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BooksController {
    private static final Logger logger = LoggerFactory.getLogger(BooksController.class);

    @Autowired
    private BooksService booksService;
    @Autowired
    private BooksMapper booksMapper;

    @PostMapping("/findAll")
    public ApiResponse<List<Books>> findAll(String title, Long fromPrice, Long toPrice, double fromRating, double toRating, String publisher,
                               Long bookStatus, Long bookFormat, String author, String category, Date fromAddDate, Date toAddDate) {
        ApiResponse<List<Books>> apiResponse = ApiResponse.<List<Books>>builder()
                .success(true)
                .build();
        try {
            apiResponse.setData(booksService.findAll(title, fromPrice, toPrice, fromRating, toRating, publisher, bookStatus, bookFormat,
                    author, category, fromAddDate, toAddDate));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new AppException("FA0000", e.getMessage());
        }
        return apiResponse;
    }

    @DeleteMapping("/{id}")
    public ApiResponse delete(@PathVariable("id") Long id) {
        ApiResponse<List<Books>> apiResponse = ApiResponse.<List<Books>>builder()
                .success(true)
                .build();
        try {
            booksService.deleteBook(id);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new AppException("FA00000", e.getMessage());
        }
        return apiResponse;
    }

    @PatchMapping("{id}")
    public ApiResponse updateBook(@PathVariable("id") Long id, @RequestBody UpdateBookRequest bookRequest) {
        ApiResponse<List<Books>> apiResponse = ApiResponse.<List<Books>>builder()
                .success(true)
                .build();
        try {
            Books updatingBook = booksMapper.UpdateBookRequestToBooks(bookRequest);
            booksService.updateBook(id, updatingBook);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new AppException("UB00000", e.getMessage());
        }
        return apiResponse;
    }
}
