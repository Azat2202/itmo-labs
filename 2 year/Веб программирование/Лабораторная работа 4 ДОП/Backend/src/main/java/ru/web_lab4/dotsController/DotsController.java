package ru.web_lab4.dotsController;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@AllArgsConstructor
public class DotsController {
    @Autowired
    private final DotsRepository dotsRepository;


    @PostMapping("/api/dots")
    public Dot addDot(@RequestParam("x") Float x,
                      @RequestParam("y") Float y,
                      @RequestParam("r") Float r,
                      Authentication authentication){
        long timer = System.nanoTime();
        if (!Dot.validateInput(x, y, r))
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Not valid x, y, r values");
        Dot dot = new Dot(x, y, r, authentication.getName());
        dot.setScriptTime(System.nanoTime() - timer);
        dotsRepository.save(dot);
        return dot;
    }

    @GetMapping("/api/dots")
    public List<Dot> getDots(Authentication authentication){
        return dotsRepository.getDotByOwnerLogin(authentication.getName());
    }

    @Transactional
    @DeleteMapping("/api/dots")
    public void deleteDots(Authentication authentication){
        dotsRepository.deleteDotsByOwnerLogin(authentication.getName());
    }
}
