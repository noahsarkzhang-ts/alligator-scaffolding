package org.noashark.util;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.noashark.common.event.BeanModifiedEvent;
import org.noashark.common.pojo.Response;
import org.noashark.common.pojo.vo.PageVO;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * Json 工具类
 *
 * @author zhangxt
 * @date 2021/11/06 12:17
 **/
public class JsonUtils {

    private static final Gson GSON = new GsonBuilder()
            .serializeNulls()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .setNumberToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
            .setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
            .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
            .disableHtmlEscaping()
            /*.setObjectToNumberStrategy(ToNumberPolicy.LAZILY_PARSED_NUMBER)*/
            .create();

    public static <T> T fromJson(String json, Class<T> classz) {
        return GSON.fromJson(json, classz);
    }

    public static Map<String, Object> fromJson(String json) {
        Type type = new TypeToken<Map<String, Object>>() {
        }.getType();

        return fromJson(json, type);
    }

    public static <T> T fromJson(String json, Type typeOfT) {
        return GSON.fromJson(json, typeOfT);
    }

    public static <T> T fromJson(JsonElement json, Class<T> classz) {
        return GSON.fromJson(json, classz);
    }

    public static <T> T fromJson(JsonArray jsonArray, Type typeOfT) {
        return GSON.fromJson(jsonArray, typeOfT);
    }

    public static <T> T fromJson(JsonElement json, Type typeOfT) {
        return GSON.fromJson(json, typeOfT);
    }

    public static <T> List<T> fromJsonList(String json, Class<T> classz) {
        return GSON.fromJson(json, new TypeToken<List<T>>() {
        }.getType());
    }

    public static <T> Response<T> fromJsonObject(String json, Class<T> clazz) {
        Type type = new ParameterizedTypeImpl(Response.class, new Class[]{clazz});
        return GSON.fromJson(json, type);
    }

    public static <T> BeanModifiedEvent<T> fromBeanModifiedObject(String json, Class<T> clazz) {
        Type type = new ParameterizedTypeImpl(BeanModifiedEvent.class, new Class[]{clazz});
        return GSON.fromJson(json, type);
    }

    public static Response<Void> fromCommonObject(String json) {
        return JsonUtils.fromJsonObject(json, Void.class);
    }

    public static <T> Response<PageVO<T>> fromPageObject(String json, Class<T> clazz) {
        // 生成List<T> 中的 List<T>
        /*Type listType = new ParameterizedTypeImpl(List.class, new Class[]{clazz});*/
        // 生成PageVo<T> 中的 PageVO<T>
        Type pageType = new ParameterizedTypeImpl(PageVO.class, new Class[]{clazz});
        // 根据PageVO<T>生成完整的Result<PageVO<T>>
        Type type = new ParameterizedTypeImpl(Response.class, new Type[]{pageType});
        return GSON.fromJson(json, type);
    }

    /**
     * @param json  json字串
     * @param clazz 反序列化的类
     * @return org.noahsark.server.rpc.Result<java.util.List < T>>
     * @author zhangxt
     * @date 2021/11/06 12:16
     */
    public static <T> Response<List<T>> fromJsonArray(String json, Class<T> clazz) {
        // 生成List<T> 中的 List<T>
        Type listType = new ParameterizedTypeImpl(List.class, new Class[]{clazz});
        // 根据List<T>生成完整的Result<List<T>>
        Type type = new ParameterizedTypeImpl(Response.class, new Type[]{listType});
        return GSON.fromJson(json, type);
    }

    public static <T> String toJson(T obj) {
        return GSON.toJson(obj);
    }

    private static class ParameterizedTypeImpl implements ParameterizedType {
        private final Class raw;
        private final Type[] args;

        public ParameterizedTypeImpl(Class raw, Type[] args) {
            this.raw = raw;
            this.args = args != null ? args : new Type[0];
        }

        @Override
        public Type[] getActualTypeArguments() {
            return args;
        }

        @Override
        public Type getRawType() {
            return raw;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }
    }

    public static class LocalDateAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
        @Override
        public JsonElement serialize(LocalDateTime localDateTime, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }

        @Override
        public LocalDateTime deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            String datetime = jsonElement.getAsJsonPrimitive().getAsString();
            return LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
    }

    public static void main(String[] args) {

    }

}
