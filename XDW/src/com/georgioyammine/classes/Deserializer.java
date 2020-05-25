package com.georgioyammine.classes;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.util.HashMap;


public class Deserializer {

    /*
     * Mapping that stores the specific new classes to use for old serialized
     * class names in order to transform old classes to the new ones for
     * compatibility reasons
     */
    private static final HashMap<String, Class<?>> classMapping = new HashMap<>();

    static { 
        classMapping.put("application.DataWarehousing",DataWarehousing.class);
    	classMapping.put("application.Project",Project.class);
    	classMapping.put("application.Version",Version.class);
    }

    public Object deserialize(FileInputStream in) {
        try (ObjectInputStream o =
                new ObjectInputStreamAdapter(in)) {
            Object object = o.readObject();
            return object;
            /* ... */
        } catch (Exception e) {
        	System.out.println("cannot deserialize");
        	e.printStackTrace();
        	return null;
        }
    }

    /*
     * Adaptor that transform old classes to the new classes for compatibility
     * reasons
     */
    private class ObjectInputStreamAdapter extends ObjectInputStream {

        public ObjectInputStreamAdapter(InputStream in) throws IOException {
            super(in);
        }

        @Override
        protected Class<?> resolveClass(ObjectStreamClass desc)
                throws IOException, ClassNotFoundException {
            Class<?> klazz = classMapping.get(desc.getName());
            if (klazz != null) {
                return klazz;
            } else {
                return super.resolveClass(desc);
            }
        }

    }

}