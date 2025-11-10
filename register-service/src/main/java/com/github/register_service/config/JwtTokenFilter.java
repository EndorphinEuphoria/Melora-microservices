package com.github.register_service.config;

import java.io.IOException;
import java.rmi.ServerException;
import java.util.Optional;

import org.springframework.web.filter.OncePerRequestFilter;

import com.github.register_service.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtTokenFilter extends OncePerRequestFilter {

    private UserRepository userRepository;
    private JwtService JwtService;
    private final String header = "Authorization";

    @Override
    protected void doFilterInternal(
        HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServerException, IOException {
            getTokenString(request.getHeader(header))
            
        }

    /**
     * Extracts the bearer token string from the given HTTP {@code Authorization} header.
     * <p>
     * The expected format of the header is:
     * <pre>
     * Authorization: Bearer &lt:token&gt;
     * <pre>
     * If the header is {@code null}, malformed, or does not contain a token value,
     * this method returns an empty {@link Optional}.
     * 
     * @param header the value of the HTTP "Authorization" header, possibly {@code null}.
     * @return an {@link Optional} containing the token string if present;
     *         otherwise {@link Optional#empty()}.
     */
    private Optional<String> getTokenString(String header) {
        if (header == null) {
            return Optional.empty();
        } else {
            String[] split = header.split(" "); // Divide el string en partes separadas por espacios = ["Bearer eyJhbGci..."] -> ["Bearer", "eyJhbGci..."] 
            if (split.length < 2) { // En caso de no tener la segunda parte (el token) devuelve optional vacío
                return Optional.empty();
            } else {
                return Optional.ofNullable(split[1]); // Consigue la segunda parte del split, que debería ser el token
            }
        }
    }
}
