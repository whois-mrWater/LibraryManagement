package com.library.controller;

import com.library.service.FineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * THANH VIEN D PHU TRACH
 * URL goc: /fines
 */
@Controller
@RequestMapping("/fines")
public class FineController {

    @Autowired
    private FineService fineService;

    @GetMapping
    public String listFines(Model model) {
        model.addAttribute("fines", fineService.getUnpaidFines());
        return "fines/list";
    }

    @PostMapping("/pay/{id}")
    public String payFine(@PathVariable Long id) {
        // TODO (D): Goi fineService.payFine(id)
        return "redirect:/fines";
    }
}
