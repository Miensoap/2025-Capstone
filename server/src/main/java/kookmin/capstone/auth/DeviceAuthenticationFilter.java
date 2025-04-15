package kookmin.capstone.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kookmin.capstone.domain.Device;
import kookmin.capstone.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class DeviceAuthenticationFilter extends OncePerRequestFilter {

    private final DeviceRepository deviceRepository;
    private final AntPathMatcher matcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Device ")) {
            String[] parts = authHeader.substring(7).split(",");
            String deviceId = null;
            String secret = null;

            for (String part : parts) {
                String[] kv = part.trim().split("=");
                if (kv.length == 2) {
                    if (kv[0].equals("deviceId")) deviceId = kv[1];
                    else if (kv[0].equals("secret")) secret = kv[1];
                }
            }

            if (deviceId != null && secret != null) {
                Optional<Device> deviceOpt = deviceRepository.findById(deviceId);
                if (deviceOpt.isPresent() && deviceOpt.get().matchesSecret(secret)) {
                    Device device = deviceOpt.get();
                    var auth = new UsernamePasswordAuthenticationToken(
                            device,
                            null,
                            List.of(new SimpleGrantedAuthority("DEVICE"))
                    );

                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return !path.startsWith("/api/device");
    }
} 
