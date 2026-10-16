package com.example.GameVault.service;

import com.example.GameVault.model.Usuario;
import com.example.GameVault.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class UsuarioService {
    @Autowired
    UsuarioRepository usuarioRepository;

    public Usuario obtenerUsuarioLogeado(){
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(1L);
        if(usuarioOpt.isPresent()){
            return usuarioOpt.get();
        }else {
            Usuario usuario = new Usuario();
            usuario.setNombre("Itson");
            usuario.setEmail("itson@itson.edu.mx");
            usuario.setContrasenia("1234");

            return usuarioRepository.save(usuario);
        }
    }


}
