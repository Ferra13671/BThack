package com.ferra13671.bthack.managers.config;

import com.ferra13671.bthack.render.blur.BlurPass;
import com.ferra13671.bthack.render.blur.BlurProvider;
import com.ferra13671.configprovider.element.ElementContext;
import com.ferra13671.configprovider.element.ElementType;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import lombok.experimental.UtilityClass;
import org.joml.Vector2f;

@UtilityClass
public class BThackElementTypes {

    public ElementType<Vector2f> VECTOR_2F = new ElementType<>(
            jsonElement -> {
                if (jsonElement instanceof JsonArray jsonArray)
                    return jsonArray.size() >= 2 && isNumber(jsonArray.get(0)) && isNumber(jsonArray.get(1));
                return false;
            },
            jsonElement -> {
                JsonArray jsonArray = jsonElement.getAsJsonArray();

                return new Vector2f(jsonArray.get(0).getAsFloat(), jsonArray.get(1).getAsFloat());
            },
            vector2f -> {
                JsonArray jsonArray = new JsonArray();
                jsonArray.add(vector2f.x);
                jsonArray.add(vector2f.y);

                return jsonArray;
            }
    );

    public ElementType<BlurPass> BLUR_PASS = new ElementType<>(
            jsonElement -> {
                if (jsonElement instanceof JsonObject jsonObject)
                    return VECTOR_2F.equalsFunction().apply(jsonObject.get("offsets"));
                return false;
            },
            jsonElement -> {
                JsonObject jsonObject = jsonElement.getAsJsonObject();

                Vector2f offsets = VECTOR_2F.toElementFunction().apply(jsonObject.get("offsets"));
                int blurRadius = new ElementContext(jsonObject.get("radius")).getElementOrDefault(ElementType.NUMBER, BlurProvider.DEFAULT_BLUR_RADIUS).intValue();

                return new BlurPass(offsets, blurRadius);
            },
            blurPass -> {
                JsonObject jsonObject = new JsonObject();

                jsonObject.add("offsets", VECTOR_2F.toJsonFunction().apply(blurPass.getOffset()));
                jsonObject.add("radius", ElementType.NUMBER.toJsonFunction().apply(blurPass.getRadius()));

                return jsonObject;
            }
    );

    private boolean isNumber(JsonElement element) {
        return element instanceof JsonPrimitive primitive && primitive.isNumber();
    }
}
