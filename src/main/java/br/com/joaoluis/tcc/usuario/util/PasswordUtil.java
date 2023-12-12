package br.com.joaoluis.tcc.usuario.util;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Random;

@RequiredArgsConstructor
@Component
public class PasswordUtil {

    private final PasswordEncoder passwordEncoder;

    public String encode(String password) {
        return passwordEncoder.encode(password);
    }

    public String geradorSenha() {
        final char[] ALL_CHARS = new char[62];
        final Random RANDOM = new Random();
        int j = 0;
        for (int i = 48; i < 123; i++) {
            if (Character.isLetterOrDigit(i)) {
                ALL_CHARS[j] = (char) i;
                j++;
            }
        }
        final char[] result = new char[8];
        for (int i = 0; i < 8; i++) {
            result[i] = ALL_CHARS[RANDOM.nextInt(ALL_CHARS.length)];
        }
        return new String(result);
    }

    public static void main(String[] args) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println("---> senha: " + passwordEncoder.encode("weblic4231"));
    }
}
