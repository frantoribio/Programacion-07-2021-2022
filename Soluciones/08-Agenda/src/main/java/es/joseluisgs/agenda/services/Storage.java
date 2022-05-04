package es.joseluisgs.agenda.services;

import es.joseluisgs.agenda.dto.PersonaDTO;
import es.joseluisgs.agenda.utils.Properties;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private static Storage instance;

    private Storage() {
        makeDirectory();
    }

    public static Storage getInstance() {
        if (instance == null) {
            instance = new Storage();
        }
        return instance;
    }

    private void makeDirectory() {
        if (!Files.exists(Paths.get(Properties.DATA_DIR))) {
            try {
                Files.createDirectory(Paths.get(Properties.DATA_DIR));
                Files.createDirectory(Paths.get(Properties.BACKUP_DIR));
                Files.createDirectory(Paths.get(Properties.IMAGES_DIR));
                // Imagenes
            } catch (IOException e) {
                System.err.println("Error al crear directorio de trabajo de la aplicación");
            }
        }
    }

    public void backup(List<PersonaDTO> personas) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(
                Files.newOutputStream(Paths.get(Properties.BACKUP_FILE))
        );
        oos.writeObject(personas);
        oos.close();
    }

    public List<PersonaDTO> restore() throws IOException, ClassNotFoundException {
        List<PersonaDTO> personas = new ArrayList<>();
        ObjectInputStream ois = new ObjectInputStream(
                Files.newInputStream(Paths.get(Properties.BACKUP_FILE))
        );
        personas = (List<PersonaDTO>) ois.readObject();
        ois.close();
        return personas;
    }

    public void copyFile(String source, String destination) throws IOException {
        System.out.println("Copiando " + source + " a " + destination);
        if (Files.exists(Paths.get(source))) {
            Files.copy(Paths.get(source), Paths.get(destination), StandardCopyOption.REPLACE_EXISTING);
        } else {
            System.out.println("No existe el archivo " + source);
        }
    }

    public void deleteFile(String file) throws IOException {
        System.out.println("Borrando " + file);
        if (Files.exists(Paths.get(file))) {
            Files.delete(Paths.get(file));
        } else {
            System.out.println("No existe el archivo " + file);
        }
    }
}
