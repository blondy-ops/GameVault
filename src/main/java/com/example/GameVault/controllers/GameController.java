package com.example.GameVault.controllers;

import com.example.GameVault.models.Juego;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class GameController {

    private static List<Juego> juegosDb = new ArrayList<>();
    private static long idCounter = 1;

    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";

    static {
        // juegosDb.add(new Juego(idCounter++, "The Legend of Zelda", "Aventura epica.", "zelda.jpg"));
    }

    @GetMapping("/fragments-demo")
    public String fragmentDemo(){
        return "fragments-demo";
    }

    @GetMapping({"/","/juegos"})
    public String listarJuegos(Model model) {
        model.addAttribute("juegos", juegosDb);
        return "juegos";
    }

    @GetMapping("/juegos/nuevo")
    public String mostrarFormulario(){return "formulario";}

    @PostMapping("/juegos")
    public String guardarJuego(@RequestParam("titulo") String titulo,
                               @RequestParam("descripcion") String descripcion,
                               @RequestParam("portada") MultipartFile portada) {

        String nombreArchivo = "default.png"; //imagen por defecto si no suben nada

        //verificamos si el archivo no este vacio
        if(!portada.isEmpty()) {
            try {
                //tip para clase: crear la carpeta si no existe
                Path uploadPath = Paths.get(UPLOAD_DIR);
                if(!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                //Generamos un nombre unico para evitar sobreescribir archivos con el mismo nombre
                nombreArchivo = UUID.randomUUID().toString() + "_" + portada.getOriginalFilename();
                Path filePath = uploadPath.resolve(nombreArchivo);

                //Guardamos el archivo fisicamente en disco
                Files.copy(portada.getInputStream(), filePath);

                System.out.println("Archivo guardado en: " + filePath.toAbsolutePath());

            }catch (IOException e) {
                e.printStackTrace();
                //en un proyecto real, manejariamos esta excepcion apropiadamente (redirigir con un error)
            }
        }

        //Guardamos el juego en nuestra "base de datos"
        Juego nuevoJuego = new Juego(idCounter++, titulo, descripcion, nombreArchivo);
        juegosDb.add(nuevoJuego);

        //redirigimos a la lista de juegos (Patron PRG - Post/Redirect/Get)
        return "redirect:/juegos";
    }
}
