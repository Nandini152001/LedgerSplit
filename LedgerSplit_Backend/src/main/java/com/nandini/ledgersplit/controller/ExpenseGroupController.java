package com.nandini.ledgersplit.controller;

import com.nandini.ledgersplit.dto.ExpenseGroupRequestDTO;
import com.nandini.ledgersplit.dto.ExpenseGroupResponseDTO;
import com.nandini.ledgersplit.service.ExpenseGroupService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/groups")
public class ExpenseGroupController {

    private final ExpenseGroupService expenseGroupService;

    public ExpenseGroupController(ExpenseGroupService expenseGroupService) {
        this.expenseGroupService = expenseGroupService;
    }

    @PostMapping
    public ResponseEntity<ExpenseGroupResponseDTO> createExpenseGroup(@Valid @RequestBody ExpenseGroupRequestDTO createdGroup){
        ExpenseGroupResponseDTO savedGroup = expenseGroupService.createGroup(createdGroup);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedGroup);
    }

    @GetMapping
    public List<ExpenseGroupResponseDTO> getAllGroupsList(){
        return expenseGroupService.getAllGroups();
    }

    @GetMapping("/{id}")
    public ExpenseGroupResponseDTO getGroupById(@PathVariable Long id){
        return expenseGroupService.getGroupById(id);
    }

    @PutMapping("/{id}")
    public ExpenseGroupResponseDTO updateGroupById(@PathVariable Long id, @RequestBody ExpenseGroupRequestDTO updatedGroupDetails){
        return expenseGroupService.updateGroupById(id, updatedGroupDetails);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGroupById(@PathVariable Long id){
        expenseGroupService.deleteGroupById(id);
        return ResponseEntity.ok("Expense group deleted successfully with id: " + id);
    }

}
