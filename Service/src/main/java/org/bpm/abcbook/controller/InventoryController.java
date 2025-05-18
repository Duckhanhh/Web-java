package org.bpm.abcbook.controller;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import lombok.Data;
import org.bpm.abcbook.model.Inventory;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@Named
@Data
@ViewScoped
public class InventoryController {
    public List<Inventory> inventoryList;
}
