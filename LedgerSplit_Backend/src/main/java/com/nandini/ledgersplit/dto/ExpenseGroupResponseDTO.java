package com.nandini.ledgersplit.dto;

public class ExpenseGroupResponseDTO {

    private Long groupId;
    private String name;
    private String description;

    public ExpenseGroupResponseDTO(Long groupId, String name, String description) {
        this.groupId = groupId;
        this.name = name;
        this.description = description;
    }


    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

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
