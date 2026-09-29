package com.nandini.ledgersplit.service;

import com.nandini.ledgersplit.dto.ExpenseGroupRequestDTO;
import com.nandini.ledgersplit.dto.ExpenseGroupResponseDTO;
import com.nandini.ledgersplit.exception.GroupNotFoundException;
import com.nandini.ledgersplit.model.ExpenseGroup;
import com.nandini.ledgersplit.repository.ExpenseGroupRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseGroupService {

    private final ExpenseGroupRepository expenseGroupRepository;

    public ExpenseGroupService(ExpenseGroupRepository expenseGroupRepository) {
        this.expenseGroupRepository = expenseGroupRepository;
    }

    public ExpenseGroupResponseDTO createGroup(ExpenseGroupRequestDTO createdGroup) {
        ExpenseGroup expenseGroup = new ExpenseGroup();
        expenseGroup.setName(createdGroup.getName());
        expenseGroup.setDescription(createdGroup.getDescription());

        ExpenseGroup savedGroup = expenseGroupRepository.save(expenseGroup);
        return mapToResponseDTO(savedGroup);
    }

    public List<ExpenseGroupResponseDTO> getAllGroups(){
        List<ExpenseGroupResponseDTO> resultList = expenseGroupRepository.findAll().stream().map(this::mapToResponseDTO).toList();
        return resultList;
    }

    public ExpenseGroupResponseDTO mapToResponseDTO(ExpenseGroup expenseGroup) {
        return new ExpenseGroupResponseDTO(
                expenseGroup.getId(),
                expenseGroup.getName(),
                expenseGroup.getDescription()
        );
    }

    public ExpenseGroupResponseDTO getGroupById(Long id){
       ExpenseGroup groupFound = expenseGroupRepository.findById(id).orElseThrow(() -> new GroupNotFoundException(id));
       return mapToResponseDTO(groupFound);
    }

    public ExpenseGroupResponseDTO updateGroupById(Long id, ExpenseGroupRequestDTO updatedGroupDetails){
        ExpenseGroup existingExpenseGroup = expenseGroupRepository.findById(id).orElseThrow(() -> new GroupNotFoundException(id));

        existingExpenseGroup.setName(updatedGroupDetails.getName());
        existingExpenseGroup.setDescription(updatedGroupDetails.getDescription());

        ExpenseGroup savedExpenseGroup = expenseGroupRepository.save(existingExpenseGroup);
        return mapToResponseDTO(savedExpenseGroup);
    }

    public void deleteGroupById(Long id){
        ExpenseGroup existingGroup = groupEntityById(id);
        expenseGroupRepository.delete(existingGroup);
    }

    public ExpenseGroup groupEntityById(Long id){
        return expenseGroupRepository.findById(id).orElseThrow(() -> new GroupNotFoundException(id));
    }
}
