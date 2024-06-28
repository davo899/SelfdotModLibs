package com.selfdot.libs.io;

import com.google.gson.Gson;

import java.io.FileReader;
import java.lang.reflect.Type;

class TypeDef<T> {

    private Class<T> clazz = null;
    private Type type = null;

    public TypeDef(Class<T> clazz) {
        this.clazz = clazz;
    }

    public TypeDef(Type type) {
        this.type = type;
    }

    public T fromJson(Gson gson, FileReader reader) {
        if (clazz != null) return gson.fromJson(reader, clazz);
        if (type != null) return gson.fromJson(reader, type);
        return null;
    }

}
