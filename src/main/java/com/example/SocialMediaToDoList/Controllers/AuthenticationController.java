import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil; // Assuming you have a JwtTokenUtil for generating JWT tokens

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid AuthenticationDTO data) {
        // Your authentication logic here
        // ...

        // Assuming authentication is successful, generate a token
        String token = jwtTokenUtil.generateToken(data.getLogin());

        return ResponseEntity.ok(token);
    }

    // Other methods...

}
