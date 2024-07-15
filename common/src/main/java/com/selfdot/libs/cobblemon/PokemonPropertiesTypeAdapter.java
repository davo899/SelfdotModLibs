package com.selfdot.libs.cobblemon;

import com.cobblemon.mod.common.api.pokemon.PokemonProperties;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.MalformedJsonException;

import java.io.IOException;

public class PokemonPropertiesTypeAdapter extends TypeAdapter<PokemonProperties> {

    @Override
    public void write(JsonWriter out, PokemonProperties value) throws IOException {
        out.value(value.asString(" "));
    }

    @Override
    public PokemonProperties read(JsonReader in) throws IOException {
        PokemonProperties pokemonProperties = PokemonProperties.Companion.parse(in.nextString());
        if (pokemonProperties.getSpecies() == null) throw new MalformedJsonException("Invalid form");
        return pokemonProperties;
    }

}
