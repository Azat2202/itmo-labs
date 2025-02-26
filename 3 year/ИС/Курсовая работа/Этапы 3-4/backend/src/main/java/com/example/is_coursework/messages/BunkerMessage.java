package com.example.is_coursework.messages;

import lombok.Data;

import java.util.List;

@Data
public class BunkerMessage {
    private Long id;

    private Integer square;

    private Integer stayDays;

    private Integer foodDays;

    private List<EquipmentMessage> equipments;
}
