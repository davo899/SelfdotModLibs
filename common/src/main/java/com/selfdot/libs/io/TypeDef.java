package com.selfdot.libs.io;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;

class TypeDef<T> {

    private Class<T> clazz = null;
    private TypeToken<T> typeToken = null;

    public TypeDef(Class<T> clazz) {
        this.clazz = clazz;
    }

    public TypeDef(TypeToken<T> typeToken) {
        this.typeToken = typeToken;
    }

    public T fromJson(Gson gson, FileReader reader) {
        if (clazz != null) return gson.fromJson(reader, clazz);
        if (typeToken != null) return gson.fromJson(reader, typeToken);
        return null;
    }

}
