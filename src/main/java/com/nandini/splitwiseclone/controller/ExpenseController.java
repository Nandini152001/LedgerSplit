package com.nandini.splitwiseclone.controller;

import com.nandini.splitwiseclone.dto.ExpenseRequestDTO;
import com.nandini.splitwiseclone.dto.ExpenseResponseDTO;
import com.nandini.splitwiseclone.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/groups/{groupId}/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService){
        this.expenseService = expenseService;
    }

    @PostMapping
    public ResponseEntity<ExpenseResponseDTO> createExpense(@PathVariable Long groupId, @Valid @RequestBody ExpenseRequestDTO expenseRequestDTO){
        ExpenseResponseDTO expenseResponseDTO = expenseService.createExpense(groupId, expenseRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(expenseResponseDTO);
    }

    @GetMapping
    public ResponseEntity<List<ExpenseResponseDTO>> getExpensesByGroupId(@PathVariable Long groupId){
        List<ExpenseResponseDTO> expenseResponseDTOs = expenseService.getExpensesByGroupId(groupId);
        return ResponseEntity.ok(expenseResponseDTOs);
    }



}
