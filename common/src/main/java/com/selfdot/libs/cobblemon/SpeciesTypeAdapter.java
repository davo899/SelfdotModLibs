package com.selfdot.libs.cobblemon;

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.pokemon.Species;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class SpeciesTypeAdapter extends TypeAdapter<Species> {

    @Override
    public void write(JsonWriter out, Species species) throws IOException {
        out.value(species.getResourceIdentifier().getPath());
    }

    @Override
    public Species read(JsonReader in) throws IOException {
        return PokemonSpecies.INSTANCE.getByName(in.nextString());
    }

}
