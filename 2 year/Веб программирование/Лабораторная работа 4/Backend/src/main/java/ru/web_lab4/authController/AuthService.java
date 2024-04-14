package ru.web_lab4.authController;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;

@Service
@AllArgsConstructor
public class AuthService {
    private static final String CHARACTERS = """
            ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789<>?:@{!$%^&*()_+£$
            """;
    private static final String PEPPER = "^hE!c&D@+c";

    private final UserRepository repository;
    public void registerUser(String login, String password){
        if(repository.findApplicationUserByLogin(login) != null) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Login is not free");
        }
        if(password.isBlank()){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Password must be not blank");
        }
        String salt = this.generateRandomString();
        String cookedPass = PEPPER + password + salt;
        var user = new ApplicationUser(login, this.getHash(cookedPass), salt);
        repository.save(user);
    }

    public String checkUser(String authorization){
        if(!authorization.startsWith("Basic")){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not valid authentication method");
        }
        String login, password;
        try{
             var base64 = authorization.substring(6);
             String[] data =  new String(Base64.getDecoder().decode(base64)).split(":", 2);
             if(data.length != 2){
                 throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not valid authentication data");
             }
             login = data[0];
             password = data[1];
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid base64");
        }

        ApplicationUser user = repository.findApplicationUserByLogin(login);
        if(user == null){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login is not valid");
        }

        String cookedPass = PEPPER + password + user.getSalt();
        if(!user.getPassword().equals(this.getHash(cookedPass))){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Password is not valid");
        }
        return login;
    }

    private String generateRandomString() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }

    private String getHash(String input){
        byte[] inputBytes = input.getBytes();
        MessageDigest md;
        try{
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {throw new RuntimeException(e);}
        byte[] hashBytes = md.digest(inputBytes);
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
