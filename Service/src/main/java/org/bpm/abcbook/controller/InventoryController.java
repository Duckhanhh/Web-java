package org.bpm.abcbook.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.Data;
import org.bpm.abcbook.model.Books;
import org.bpm.abcbook.model.Inventory;
import org.bpm.abcbook.service.BooksService;
import org.bpm.abcbook.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Named
@Data
@ViewScoped
public class InventoryController {
    private List<Inventory> inventoryList;
    private List<Books> bookList;
    private List<Books> listBookSelected;
    private Long bookFormat; //dinh dang sach
    private List<String> listAuthor; //danh sach tac gia
    private List<String> listSelectedAuthor; //danh sach tac gia duoc chon

    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private BooksService booksService;

    @PostConstruct
    public void init() {
        //Lay ra danh sach sach
        try {
            listBookSelected = new ArrayList<>();
            bookList = booksService.findAllBook();
            listAuthor = booksService.getAllAuthor();
            listSelectedAuthor = new ArrayList<>();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getMessage()));
        }
    }

    public String getSelectedBooksLabel() {
        List<String> selectedTitles = bookList.stream()
                .filter(book -> listBookSelected.contains(book))
                .map(Books::getTitle)
                .collect(Collectors.toList());

        // Join the titles with a comma
        return String.join(", ", selectedTitles);
    }
}
