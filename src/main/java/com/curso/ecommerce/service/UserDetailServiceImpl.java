package com.curso.ecommerce.service;

import com.curso.ecommerce.model.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private HttpSession session;

    private Logger log = LoggerFactory.getLogger(UserDetailServiceImpl.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Buscando usuario con email: {}", username);

        Optional<Usuario> optionalUser = usuarioService.finByEmail(username);
        if (optionalUser.isPresent()) {
            Usuario usuario = optionalUser.get();

            log.info("Usuario encontrado con id: {}", usuario.getId());

            // Guardamos el id en sesión
            session.setAttribute("idusuario", usuario.getId());

            // IMPORTANTE: usar la contraseña que ya está codificada en BD
            return User.builder()
                    .username(usuario.getEmail()) // mejor usar email como username
                    .password(usuario.getPassword())
                    .roles(usuario.getTipo())
                    .build();
        } else {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
    }
}
