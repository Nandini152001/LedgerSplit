package com.nandini.ledgersplit.dto;

import jakarta.validation.constraints.NotBlank;

public class ExpenseGroupRequestDTO {

    @NotBlank(message = "Group name can't be blank")
    private String name;

    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
