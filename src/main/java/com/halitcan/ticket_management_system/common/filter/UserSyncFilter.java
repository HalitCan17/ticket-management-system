package com.halitcan.ticket_management_system.common.filter;

import com.halitcan.ticket_management_system.domain.ticket.entity.UserEntity;
import com.halitcan.ticket_management_system.domain.ticket.enums.UserRole;
import com.halitcan.ticket_management_system.infrastructure.persistence.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class UserSyncFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Spring Security'nin doğruladığı kimliği (Token'ı) alıyoruz
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            UUID userId = UUID.fromString(jwt.getSubject());

            if (!userRepository.existsById(userId)) {
                log.info("Yeni kullanıcı tespit edildi, veritabanına kaydediliyor: {}", userId);

                String username = jwt.getClaimAsString("preferred_username");
                String email = jwt.getClaimAsString("email");
                String fullName = jwt.getClaimAsString("name");

                if (email == null) {
                    email = username + "@ticket.com";
                }

                UserEntity newUser = UserEntity.builder()
                        .id(userId)
                        .username(username)
                        .email(email)
                        .fullName(fullName)
                        .role(UserRole.CUSTOMER)
                        .build();

                userRepository.save(newUser);
                log.info("Kullanıcı başarıyla senkronize edildi: {}", username);
            }
        }

        filterChain.doFilter(request, response);
    }
}