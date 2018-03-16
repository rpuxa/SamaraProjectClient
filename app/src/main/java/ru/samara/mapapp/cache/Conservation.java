package ru.samara.mapapp.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class Conservation {

    public static final Conservation instance = new Conservation();

    public static void addConservedClass(Class clazz) {
        instance.forConserve.add(clazz);
    }

    private Conservation() {
    }

    private ArrayList<Class> forConserve = new ArrayList<>();

    public void conserve(Class conserved) {
        forConserve.add(conserved);
    }

    public void save(File filesDir) {
        ArrayList<Conserved> conservedArrayList = new ArrayList<>(forConserve.size());
        for (Class conserved : forConserve) {
            Field annotationField = getAnnotationFiled(conserved);
            if (annotationField == null)
                throw new SingletonNotFoundException();
            try {
                Conserved conservedField = (Conserved) annotationField.get(null);
                conservedArrayList.add(conservedField);
            } catch (IllegalAccessException ignored) {
                throw new ImplementsException("Класс не реализует интерфейс Conserved");
            }
        }
        serialize(conservedArrayList.toArray(new Conserved[0]), filesDir);
    }

    private Field getAnnotationFiled(Class conserved) {
        for (Field field : conserved.getFields()) {
            if (field.getAnnotation(Singleton.class) != null) {
                return field;
            }
        }
        return null;
    }

    public void load(File fileDir) {
        Conserved[] conservedArray = deserialize(fileDir);
        if (conservedArray == null)
            return;
        for (Conserved conserved : conservedArray) {
            Field annotationField = getAnnotationFiled(conserved.getClass());
            assert annotationField != null;
            try {
                annotationField.set(null, conserved);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private static final String FILE_PATH = "cache";

    public void serialize(Conserved[] conservedArray, File filesDir) {
        try (ObjectOutputStream obj = new ObjectOutputStream(new FileOutputStream(new File(filesDir, FILE_PATH)))){
            for (Conserved conserved : conservedArray) {
                obj.writeObject(conserved);
                obj.flush();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public Conserved[] deserialize(File fliesDir) {
        ArrayList<Conserved> conservedArrayList = new ArrayList<>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(new File(fliesDir, FILE_PATH)))) {
            while (true) {
                conservedArrayList.add((Conserved) in.readObject());
            }
        } catch (FileNotFoundException e) {
            return null;
        } catch (Exception ignored) {
        }
        return conservedArrayList.toArray(new Conserved[0]);
    }
}
