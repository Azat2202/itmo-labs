package se.ifmo.is_lab1.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import se.ifmo.is_lab1.messages.collection.StudyGroupResponse;
import se.ifmo.is_lab1.services.CommandsService;

@RestController
@RequestMapping("/api/command")
@RequiredArgsConstructor
public class CommandsController {
    private final CommandsService commandsService;

    @DeleteMapping("/delete_by_expelled")
    public StudyGroupResponse deleteByExpelled(@RequestParam Integer expelled) {
        return commandsService.deleteByShouldBeExpelled(expelled);
    }

    @GetMapping("/min_group_admin")
    public StudyGroupResponse minGroupAdmin() {
        return commandsService.getByMinGroupAdmin();
    }

    @GetMapping("/count_group_admin")
    public Long count_group_admin(@RequestParam(required = false) Long groupAdminId) {
        return commandsService.getCountByGroupAdmin(groupAdminId);
    }

    @GetMapping("/count_expelled")
    public Integer getExpelledCount() {
        return commandsService.getAllExpelledCount();
    }


    @PutMapping("/expel_everybody")
    public StudyGroupResponse expelEverybody(@RequestParam Integer groupId) {
        return commandsService.expelEverybody(groupId);
    }



}
