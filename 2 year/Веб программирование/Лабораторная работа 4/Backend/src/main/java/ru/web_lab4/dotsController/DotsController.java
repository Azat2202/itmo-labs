package ru.web_lab4.dotsController;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.web_lab4.authController.AuthService;

import java.util.List;

@RestController
@AllArgsConstructor
public class DotsController {
    @Autowired
    private final DotsRepository dotsRepository;

    @Autowired
    private final AuthService authService;

    @PostMapping("/api/dots")
    public Dot addDot(@RequestParam("x") Float x,
                      @RequestParam("y") Float y,
                      @RequestParam("r") Float r,
                      @RequestHeader("Authorization") String authorization){
        long timer = System.nanoTime();
        String login = authService.checkUser(authorization);
        if (!Dot.validateInput(x, y, r))
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Not valid x, y, r values");
        Dot dot = new Dot(x, y, r, login);
        dot.setScriptTime(System.nanoTime() - timer);
        dotsRepository.save(dot);
        return dot;
    }

    @GetMapping("/api/dots")
    public List<Dot> getDots(@RequestHeader("Authorization") String authorization){
        return dotsRepository.getDotByOwnerLogin(authService.checkUser(authorization));
    }

    @Transactional
    @DeleteMapping("/api/dots")
    public void deleteDots(@RequestHeader("Authorization") String authorization){
        String login = authService.checkUser(authorization);
        dotsRepository.deleteDotsByOwnerLogin(login);
    }
}
